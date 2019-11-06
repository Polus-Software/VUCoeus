/*
 * @(#)CoeusMessageResourcesBean.java 1.0 28/10/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.bean;

import java.io.IOException;

import java.io.InputStream;
import java.util.Properties;
import java.text.MessageFormat;
/**
 * This class provides message handling mechanism for coeus application, 
 * which loads the message properties/code 
 * from the message properties file during the inception coeus application . 
 * It provides the parse method to display Coeus Message.
 *
 * @author Sagin
 * @version 1.0
 */

public class CoeusMessageResourcesBean {

    //holds message key
    private String messageKey;
    private static Properties coeusMessageProps;
    /**
     * Default constructor
     */
    public CoeusMessageResourcesBean() {
    }

    /** Sets message key
     * @param newMessageKey  Message String
     */
    public void setMessageKey(String newMessageKey) {
        this.messageKey = newMessageKey;
    }

    /**
     * parse message key
     * @param messageKey  Message String
     * @return String parsed message used in message dialog
     */
    public String parseMessageKey(String messageKey) {
        try {
            String errMessage = "";
            if(coeusMessageProps==null){
                coeusMessageProps = loadProperties();
            }
            if (messageKey == null) {
                return coeusMessageProps.getProperty("exceptionCode.unKnown");
            } else {
                if (messageKey.indexOf("exceptionCode")!=-1) {
                    errMessage = coeusMessageProps.getProperty(messageKey,"notFound");
                    
                    if (errMessage.equals("notFound"))
                        return coeusMessageProps.getProperty("exceptionCode.keyNotFound");
                    
                    return errMessage;
                } else {
                    return messageKey;
                }
            }
        } catch (Exception fne) {
            System.err.println("Can't read the CoeusMessages.properties file. " +
                    "Make sure CoeusMessages.properties is in the CLASSPATH");
        }
        return null;
    }
    
  /**
     * The method to parse message key.
     * Retrieves the message from properties file based on the message key
     * and formats it with the message arguments.
     * @param messageKey    The key for the message
     * @param msgArgs    String Array of message arguments
     * @return String parsed message used in message dialog
     */
    public String parseMessageKey(String messageKey, String[] msgArgs) {
    
        String strMessage = parseMessageKey(messageKey);
        
        if(strMessage!=null){      
            MessageFormat formatter = new MessageFormat("");
            return formatter.format(strMessage,msgArgs);  
        }else{
            return null;
        }  
    }
    /**
     * Load Properties
     *
     * @return The Properties
     * @throws IOException
     */
    private static Properties loadProperties() throws IOException {
        InputStream stream = null;
        Properties props = new Properties();
        try {
                stream = new CoeusMessageResourcesBean().getClass().getResourceAsStream("/CoeusMessages.properties");
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