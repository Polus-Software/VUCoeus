/*
 * S2SErrorMessages.java
 *
 * Created on February 4, 2005, 5:21 PM
 */

package edu.mit.coeus.s2s.validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author  geot
 */
public class S2SErrorMessages {
    
    private static Properties props = null;
    
    private static String s2sErrPropFile;

    private static final String errorFileName = "/edu/mit/coeus/s2s/validator/S2SErrorMessages.properties";

    /** Creates a new instance of S2SErrorMessages */
    private S2SErrorMessages() {
    }

    
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
            synchronized (S2SErrorMessages.class) {
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
                props = new Properties();
                stream = new S2SErrorMessages().getClass().getResourceAsStream(errorFileName);
                props.load( stream );
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return props;
    }
    
    
}
