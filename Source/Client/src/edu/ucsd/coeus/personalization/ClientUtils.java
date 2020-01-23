/*
 * Created on Jan 31, 2006
 * 
 * 
 * */
package edu.ucsd.coeus.personalization;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import edu.mit.coeus.utils.CoeusOptionPane;


public class ClientUtils {
    /*public static Logger logger = Logger.getRootLogger(); */
	public static ClientLogger logger;
    //public static final String userHomeDir = System.getProperty("user.home");
    //public static final String appHomeDir = userHomeDir+"/.coeus/".replace('/', File.separatorChar);
    public static final String COEUS_FRAME = "MAINFRAME";
    public static final String GENERIC = "GENERIC";
    private static final String WIN_ID = "Windows";
    private static Properties persnProps;
    private static final String EMPTY = "";
    
    static {
    	/* Per sabari do not log into client machine
        initLog(); */
    	logger =  new ClientLogger();
        loadProperty();
    }

    /*
    public static void initLog() {
        File aDir = new File(appHomeDir);
        if (!aDir.exists())
            aDir.mkdir();
        try {
            Properties logProps = new Properties(); 
            InputStream inStrm = ClientUtils.class.getResourceAsStream("/edu/ucsd/coeus/personalization/resource/log4j.properties");
            logProps.load(inStrm);
            PropertyConfigurator.configure(logProps);
            Enumeration  appender = logger.getAllAppenders();
            //Add directory to all file appenders
            while( appender.hasMoreElements()) {
                Appender append = (Appender)appender.nextElement();
                String key = "log4j.appender." + append.getName () + ".File";
                if (logProps.containsKey((String)key)) {
                    String logfile = appHomeDir+logProps.getProperty(key); 
                    logProps.setProperty(key, logfile);
                }
            }
            PropertyConfigurator.configure (logProps); //Reload config with correct file appender
        } catch (Exception fne) {
            System.err.println("Can't read the log4j.properties file. "
                    + "Make sure log4j.properties is in the CLASSPATH");
        }
    } */
    
    public static void loadProperty() {
        try {
            persnProps = new Properties(); 
            InputStream inStrm = ClientUtils.class.getResourceAsStream("/edu/ucsd/coeus/personalization/resource/client.properties");
            persnProps.load(inStrm);
        } catch (Exception fne) {
            System.err.println("Can't read the client.properties file. "
                    + "Make sure client.properties is in the CLASSPATH");
        }
    }    
    
    public static boolean isCached() {
		return Boolean.valueOf(persnProps.getProperty("cache_XML")).booleanValue();
    	
    }
    
    /**
     * 
     * @param str
     * @return
     */
    public static boolean isWhitespace(String str)   {
        if(str == null)
            return false;
        int sz = str.length();
        for(int i = 0; i < sz; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 
     * @param str
     * @return
     */
    public static boolean isBlank(String str)   {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return true;
        for(int i = 0; i < strLen; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
    }

    /**
     * 
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str)  {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return false;
        for(int i = 0; i < strLen; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return true;

        return false;
    }
    
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
        	if (str.charAt(i) == '.') continue; //Ignore decimal 
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isWindowsPlatform()  {
    	String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID))
            return true;
        else
            return false;
    }    
    
    public static void openURL(String url) {
        try {
            javax.jnlp.BasicService bs
            = (javax.jnlp.BasicService)javax.jnlp.ServiceManager.lookup("javax.jnlp.BasicService");
            bs.showDocument(new URL(url));
        }  catch (MalformedURLException e) {    	
        	CoeusOptionPane.showErrorDialog("Invalid URL - Unable to invoke your browser");        	
        }  catch(Exception ue) {
        	CoeusOptionPane.showErrorDialog("JNLP services not initialized - Unable to invoke your browser");
        }
	}

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }
    
    

}
