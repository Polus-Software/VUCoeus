/*
 * Created on Jan 31, 2006
 * 
 * 
 * */
package edu.ucsd.coeus;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.UtilFactory;

public class ServerUtils {
    static private final String propertyfile = "personalization.properties";
    private static final boolean THROW_ON_LOAD_FAILURE = true;
    private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
    private static final String SUFFIX = ".properties";
    static private Properties config = null;
    static DecimalFormat decf = new DecimalFormat("0");
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    static {
        if (config == null) load_properties();
    }

    /**
     * 
     *
     */
    public static void load_properties() {
        try {
            config = loadProperties (propertyfile,
                    Thread.currentThread ().getContextClassLoader ());
            if (config == null) {
            	UtilFactory.log("personalization.properties not found");
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	UtilFactory.log("personalization.properties not found");
        }
    }
    

    /**
     * 
     * @param name
     * @param loader
     * @return
     */
    public static Properties loadProperties(String name, ClassLoader loader) {
        if (name == null)
            throw new IllegalArgumentException("null input: name");

        if (name.startsWith("/"))
            name = name.substring(1);

        if (name.endsWith(SUFFIX))
            name = name.substring(0, name.length() - SUFFIX.length());

        Properties result = null;

        InputStream in = null;
        try {
            if (loader == null)
                loader = ClassLoader.getSystemClassLoader();

            if (LOAD_AS_RESOURCE_BUNDLE) {
                name = name.replace('/', '.');

                // Throws MissingResourceException on lookup failures:
                final ResourceBundle rb = ResourceBundle.getBundle(name, Locale
                        .getDefault(), loader);

                result = new Properties();
                for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
                    final String key = (String) keys.nextElement();
                    final String value = rb.getString(key);

                    result.put(key, value);
                }
            } else {
                name = name.replace('.', '/');

                if (!name.endsWith(SUFFIX))
                    name = name.concat(SUFFIX);

                // Returns null on lookup failures:
                in = loader.getResourceAsStream(name);
                if (in != null) {
                    result = new Properties();
                    result.load(in); // Can throw IOException
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	UtilFactory.log("personalization.properties load error");
            result = null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Throwable ignore) {
                }
        }

        if (THROW_ON_LOAD_FAILURE && (result == null)) {
            throw new IllegalArgumentException("could not load ["
                    + name
                    + "]"
                    + " as "
                    + (LOAD_AS_RESOURCE_BUNDLE
                            ? "a resource bundle"
                            : "a classloader resource"));
        }

        return result;
    }
    
    /**
     * 
     * @return
     */
    public static boolean getCacheXMLFlag() {
        if (config == null) return true;
        return config.getProperty("cache_XML","true").equals("true");
    }
    
    public static Method getGetterMethod(Class classobj, String methodnm) {
    	Method method = null;
		try {
			method = classobj.getMethod(methodnm);
		} catch (SecurityException e1) {
		} catch (NoSuchMethodException e1) {
		}
		return method;
    }
    
    public static String getBeanVarDataByMeth(Object beanclass, String givenAttr) {
		String getterMethod1 = "get" + givenAttr.substring(0,1).toUpperCase() + givenAttr.substring(1);
		String getterMethod2 = "is" + givenAttr.substring(0,1).toUpperCase() + givenAttr.substring(1);
		Method classmethod = getGetterMethod(beanclass.getClass(),getterMethod1);
		if (classmethod == null)
			classmethod = getGetterMethod(beanclass.getClass(),getterMethod2);
		if (classmethod == null) return "";
		try {
			Object result = classmethod.invoke(beanclass, new Object[] {});
			if (result != null) { // Currently separated for proper formatting
				if (result instanceof java.lang.String) {
					return result.toString();
				} else if (result instanceof java.lang.Boolean) {
					return result.toString();
				} else if (result instanceof java.util.Date) {
					return sdf.format(result);
				} else if (result instanceof Double) {
					NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
					return decf.format(result);
				} else if (result instanceof Integer) {
					return result.toString();
				}
			}						
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return "";
    }
    
    /**
     * Limit this to coeusbean and basebean
     * @param gobj
     * @param gmethod
     * @return
     */
    public static Object getGetterObject(Object gobj, String gmethod) {
		Method classmethod = getGetterMethod(gobj.getClass(),gmethod);
		if (classmethod == null) return null;
		try {
			Object result = classmethod.invoke(gobj, new Object[] {});
			if (result != null && (result instanceof CoeusBean ||
					result instanceof BaseBean)) {
				return result;
			}						
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}
		return null;    	
    }

}
