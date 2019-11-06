/*
 * CoeusProperties.java
 *
 * Created on November 29, 2004, 11:34 AM
 */

package edu.mit.coeus.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author  geot
 */
public class CoeusProperties implements CoeusPropertyKeys{
    private static Properties props = null;
    
    private static final String COEUS_PROP_FILE="/coeus.properties";
    
    /** Creates a new instance of CoeusProperties */
    private CoeusProperties() {}
    /**
     * Get a property value from the <code>coeus.properties</code> file.
     *
     * @param key An key value
     * @return The property value or null if no property exists
     * @throws IOException
     */
    public static String getProperty(String key) throws IOException {
        return getProperty(key,null);
    }
    /**
     * Get a property value from the <code>coeus.properties</code> file.
     *
     * @param key An key value
     * @return The property value or default value if no property exists
     * @throws IOException
     */
    public static String getProperty(String key,String defaultValue) throws IOException {
        if (props == null) {
            synchronized (CoeusProperties.class) {
                if (props == null) {
                    props = loadProperties();
                }
            }
            
        }
        return props.getProperty(key,defaultValue);
    }
    /**
     * Load Properties
     *
     * @return The Properties
     * @throws IOException
     */
    private static Properties loadProperties() throws IOException {
        
        
        InputStream stream = null;
        try {
//            if (getCoeusPropFile() != null) {
                props = new Properties();
//                stream = new FileInputStream(new File(getCoeusPropFile()));
//                stream = new CoeusProperties().getClass().getResourceAsStream(getCoeusPropFile());
                stream = new CoeusProperties().getClass().getResourceAsStream(COEUS_PROP_FILE);
                props.load( stream );
                
//            }
            
            
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
                //ex.printStackTrace();
                UtilFactory.log(ex.getMessage(), ex, "CoeusProperties", "loadProperties");                
            }
        }
        return props;
    }
    
//    /**
//     * Getter for property CoeusPropFile.
//     * @return Value of property CoeusPropFile.
//     */
//    public static java.lang.String getCoeusPropFile() {
////        System.out.println("in get method=>"+coeusPropFile);
//        return coeusPropFile;
//    }
//    
//    /**
//     * Setter for property CoeusPropFile.
//     * @param CoeusPropFile New value of property CoeusPropFile.
//     */
//    public static void setCoeusPropFile(java.lang.String coeusPropFile) {
//        CoeusProperties.coeusPropFile = coeusPropFile;
////        System.out.println("Set the prop file"+CoeusProperties.coeusPropFile);
//    }

    //Case 3243 - Bringing server side properties to client side - start
    /**
     * This method returns the values defined in coeus.properties file
     * @return props Properties returns coeus.properties collection
     * @throws IOException
     */
    public static Properties getPropertyList() throws IOException{
        synchronized(CoeusProperties.class){
            if(props == null){
                props = loadProperties();
            }
        }
        return props;
    }
    //Case 3243 - Bringing server side properties to client side - end
    
}
