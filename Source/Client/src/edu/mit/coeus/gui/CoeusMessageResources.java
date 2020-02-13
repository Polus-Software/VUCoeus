/*
 * @(#) CoeusMessageResources.java 1.0 14/11/2002
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.gui;

import edu.mit.coeus.utils.CoeusGuiConstants;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * This is a Singleton class that provides access to Messages.Properties file.
 * A client gets access to the single instance through the static getInstance()
 * method.
 *
 * @version 1.0 November 14, 2002, 10:31 PM
 * @author  Sagin Divakar
 */
public class CoeusMessageResources {
    static private CoeusMessageResources instance;       // The single instance
    private Properties coeusMessageProps,coeusLabelProps,displayProperties;


    /**
     * Returns the single instance, creating one if it's the
     * first time this method is called.
     *
     * @return CoeusMessageResources The single instance.
     */
    static synchronized public CoeusMessageResources getInstance() {
        if (instance == null) {
            instance = new CoeusMessageResources();
        }
        return instance;
    }

    /**
     * A private constructor since this is a Singleton
     */
    private CoeusMessageResources() {
        init();
        initLabelProps();//Adde for Protocol enhencement
        //#Added for case 3855
        loadDisplayProperties();
    }

    /**
     * Create and load an Input Stream for reading the Messages.properties file.
     */
    private void init() {
        try {
            InputStream inStrm = getClass().getResourceAsStream("/edu/mit/coeus/resources/Messages.properties");
            coeusMessageProps = new Properties();
            coeusMessageProps.load(inStrm);
        } catch (Exception fne) {
            System.err.println("Can't read the Messages.properties file. " +
                    "Make sure Messages.properties is in the CLASSPATH");
        }
    }
    
    
    /**
     * This method is used to parse the message key.
     * @param messageKey String representing the key in Message.properties file.
     * @return String message value corresponding to the messageKey 
     * from Message.properties
     */
    public String parseMessageKey(String messageKey) {
        String mesg="";
        try {
            if (messageKey == null) {
                mesg = coeusMessageProps.getProperty("exceptionCode.unKnown");
            }
            else if (messageKey.indexOf("Code.") == -1) {
                mesg = messageKey;
            } else {
                mesg = coeusMessageProps.getProperty(messageKey,
                    coeusMessageProps.getProperty("exceptionCode.keyNotFound"));                
            }
        } catch (Exception fne) {
            mesg = "Can't read the Messages.properties file. " +
                    "Make sure Messages.properties is in the CLASSPATH";
        }
        return mesg;    
        
    }
    
    //Adde for Protocol enhencement - Start
    
    /**
     * Create and load an Input Stream for reading the CoeusLabels.properties file.
     */
    private void initLabelProps() {
        try {
            
            InputStream labelsInStrm = getClass().getResourceAsStream(CoeusGuiConstants.COEUS_LABEL_PATH);
            coeusLabelProps = new Properties();
            coeusLabelProps.load(labelsInStrm);
        } catch (Exception fne) {
            System.err.println("Can't read the CoeusLabels.properties file. " +
            "Make sure CoeusLabels.properties is in the CLASSPATH");
        }
    }
    
    /**
     * This method is used to parse the label key.
     * @param labelKey String representing the key in Message.properties file.
     * @return String label value corresponding to the labelKey
     * from CoeusLabels.properties
     */
    public String parseLabelKey(String labelKey) {
        String mesg="";
        try {
            if (labelKey == null) {
                mesg = "";
            } else {
                mesg = coeusLabelProps.getProperty(labelKey,labelKey);
            }
        } catch (Exception fne) {
        	// JM 5-18-2012 typo in message
            // mesg = "Can't reProtoad the Messages.properties file. " +
            mesg = "Can't load the Messages.properties file. " +
            "Make sure Messages.properties is in the CLASSPATH";
        }
        return mesg;
        
    }
    //Adde for Protocol enhencement - End
    
    //#Added for case 3855 -- start
     /**
     * Returns the displayProperty file
     * @return DisplayProperty File
     */
    public Properties getDisplayProperties() {
        return displayProperties;
    }
    /**
     * Loads the display property from the property file
     */
    private void loadDisplayProperties() {
        try {
            InputStream displayInStrm = getClass().getResourceAsStream(CoeusGuiConstants.COEUS_DISPLAY_PROPERTY_PATH);            
            displayProperties = new Properties();
            displayProperties.load(displayInStrm);
        } catch (IOException ex) {
            System.err.println("Not able to load the property file please check the location of the propertiFile "
                    + CoeusGuiConstants.COEUS_LABEL_PATH);
            ex.printStackTrace();
        }
    }
     //#Added for case 3855 -- end
}