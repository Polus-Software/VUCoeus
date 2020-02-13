/*
 * Created on Jul 1, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gov.grants.apply.soap.util;

import edu.mit.coeus.utils.S2SConstants;
import edu.mit.coeus.utils.UtilFactory;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationServicesLocator;
import gov.grants.apply.WebServices.ApplicantIntegrationServices_V1_0.ApplicantIntegrationSoapBindingStub;
import gov.grants.apply.struts.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

//import org.apache.log4j.Logger;

/**
 * @author Brian Husted
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class SoapUtils implements Constants {
    
//    static final Logger log = Logger.getLogger(SoapUtils.class.getName());
    
    private static Properties props = null;
    
    public static final String SOAP_HOST = "SOAP_HOST";
    
    public static final String SOAP_PORT = "SOAP_PORT";
    
//    public static final String ENCODING_TYPE = "ENCODING_TYPE";
//    
//    public static final String MIME_ENCODING = "MIME";
//    
//    public static final String DIME_ENCODING = "DIME";
    
    private static String soapServerPropFile = null;
    private static String SOAP_HOST_V2_URL = "SOAP_HOST_V2_URL";
    private static String SOAP_PORT_V2_URL = "SOAP_PORT_V2_URL";
    
    /**
     * Get a property value from the soap server properties file.
     *
     * @param key
     *            An key value
     * @return The property value or null if no property exists
     * @throws IOException
     */
    public static String getProperty(String key) throws IOException {
        if (props == null) {
            synchronized (SoapUtils.class) {
                if (props == null) {
                    props = loadProperties();
                }
            }
            
        }
        
        return props.getProperty(key);
        
    }
    
    /**
     * Load Properties
     *
     * @return The Properties
     * @throws IOException
     */
    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        
        InputStream stream = null;
        try {
            String s = S2SConstants.SOAP_SERVER_PROPERTY_FILE;
            stream = new SoapUtils().getClass().getResourceAsStream(s);
            props.load(stream);
        } finally {
            try {
                stream.close();
            } catch (Exception ex) {
                UtilFactory.log(ex.getMessage(),ex, "SoapUtils", "loadProperties");
            }
        }
        return props;
    }
    
    /**
     * Get Soap URL
     *
     * @return soap URL
     * @throws IOException
     */
    public static String getSoapURL() throws IOException {
        
                /*String soapHost = getProperty(SOAP_HOST);
                String soapPort = getProperty(SOAP_PORT);
                if ( (! soapHost.endsWith( "/" )) && (! soapPort.startsWith( "/")) ){
                        soapHost += "/";
                }
                return soapHost + soapPort;
                 */
        return getSoapURL(null, null);
    }
    
    /**
     * Get Soap URL
     *
     * @return soap URL
     * @throws IOException
     */
    public static String getSoapURL(String soapHostKey, String soapPortKey) throws IOException {
        
        String soapHost = soapHostKey==null ? getProperty(SOAP_HOST):getProperty(soapHostKey);
        String soapPort = soapPortKey==null ? getProperty(SOAP_PORT):getProperty(soapPortKey);
        
        if ( (! soapHost.endsWith( "/" )) && (! soapPort.startsWith( "/")) ){
            soapHost += "/";
        }
        return soapHost + soapPort;
    }
    
//    public static String getSoapURLV2(String soapHostKey, String soapPortKey) throws IOException {
//        
//        String soapHost = soapHostKey==null ? getProperty(SOAP_HOST_V2_URL):getProperty(soapHostKey);
//        String soapPort = soapPortKey==null ? getProperty(SOAP_PORT_V2_URL):getProperty(soapPortKey);
//        
//        if ( (! soapHost.endsWith( "/" )) && (! soapPort.startsWith( "/")) ){
//            soapHost += "/";
//        }
//        return soapHost + soapPort;
//    }
//    public static String getSoapURLV2() throws IOException {
//        
//                /*String soapHost = getProperty(SOAP_HOST);
//                String soapPort = getProperty(SOAP_PORT);
//                if ( (! soapHost.endsWith( "/" )) && (! soapPort.startsWith( "/")) ){
//                        soapHost += "/";
//                }
//                return soapHost + soapPort;
//                 */
//        return getSoapURLV2(null, null);
//    }
    /**
     * Get Application Soap Stub
     *
     * @return The ApplicantIntegrationSoapBindingStub
     * @throws Exception
     */
    public static ApplicantIntegrationSoapBindingStub getApplicantSoapStub()
    throws Exception {
        ApplicantIntegrationServicesLocator service = new ApplicantIntegrationServicesLocator();
        ApplicantIntegrationSoapBindingStub stub = new ApplicantIntegrationSoapBindingStub(
        new URL(getSoapURL()), service);
        
        return stub;
    }
    
    /**
     * Get soap server properties file
     *
     * @return The soapServerPropFile.
     */
    public static String getSoapServerPropFile() {
        return soapServerPropFile;
    }
    
    /**
     * Set soap server property file
     *
     * @param soapServerPropFile
     *            The soapServerPropFile to set.
     */
    public static void setSoapServerPropFile(String soapServerPropFile) {
        SoapUtils.soapServerPropFile = soapServerPropFile;
    }
}