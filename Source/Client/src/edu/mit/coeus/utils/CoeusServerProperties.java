/*
 * @(#)CoeusServerProperties.java 1.0 September 27, 2007, 12:45 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * CoeusServerProperties.java
 *
 * Created on September 27, 2007, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import java.util.Properties;

/**
 *
 * @author nandkumarsn
 */
public class CoeusServerProperties implements CoeusPropertyKeys{
    
    private static Properties properties = null;
    private static final char READ_COEUS_PROPERTIES = 'G';
    private static final String UTILITY_SERVLET = "/UtilityServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Error occured while connecting to UtilityServlet";
    
    /** Creates a new instance of CoeusServerProperties */
    public CoeusServerProperties() {
    }
    
    /**
     * This method gets the properties collection from server side
     * @return properties Properties
     *
     */
    private static Properties loadProperties() throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();        
        requesterBean.setFunctionType(READ_COEUS_PROPERTIES);
        AppletServletCommunicator appletServletCommunicator = 
                new AppletServletCommunicator(CoeusGuiConstants.CONNECTION_URL+UTILITY_SERVLET, requesterBean);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        //If connection to server broke or could not get response from server, throw an exception
        if(responderBean == null){
            throw new CoeusException(COULD_NOT_CONTACT_SERVER);
        }else{
            //Read the properties collection from the respose object
            if(responderBean.hasResponse()){
                properties = (Properties)responderBean.getDataObject();
            }
        }       
        return properties;
    }    

    /**
     * Get a property value from the <code>coeus.properties</code> file.
     *
     * @param key A key value
     * @return The property value or null if no property exists     
     */
    public static String getProperty(String key){
        return getProperty(key, null);
    }
    /**
     * Get a property value from the <code>coeus.properties</code> file.
     *
     * @param key A key value
     * @param defaultValue The default value
     * @return The property value or default value if no property exists     
     */
    public static String getProperty(String key, String defaultValue){
        
        if (properties == null){
            try{
                properties = loadProperties();
            }catch(CoeusException coeusException){
                coeusException.printStackTrace();
                //An exception occured while fetching the properties from server side,
                //return the default value
                return defaultValue;
            }
        }
        return properties.getProperty(key, defaultValue);
    }
}
