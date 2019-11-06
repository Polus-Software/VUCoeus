/*
 * CoeusToolTipMessage.java
 * 
 * Handler for tool tip messages
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: August 29, 2011
 */
package edu.vanderbilt.coeus.gui;

import java.io.InputStream;
import java.util.Properties;

/**
 * This is a class that provides access to ToolTips.properties file.
 *
 * @version 1.0 
 * @author  Jill McAfee
 * @author  Vanderbilt University
 */
public class CoeusToolTipMessage {
    private Properties coeusToolTips;
    private static final String TOOL_TIPS = "/edu/vanderbilt/coeus/resources/ToolTips.properties";

    /**
     * Constructor
     */
    public CoeusToolTipMessage() {
        init();
    }

    /**
     * Create and load an Input Stream for reading the properties file.
     */
    private void init() {
    	try {
            InputStream inputStream = getClass().getResourceAsStream(TOOL_TIPS);  
            coeusToolTips = new Properties();  
            coeusToolTips.load(inputStream);  

        } catch (Exception fne) {
            System.err.println("Can't read the ToolTips.properties file. " +
                    "Make sure ToolTips.properties is in the CLASSPATH.");
            System.err.println(fne);
        }
    }
    
    /**
     * This method is used to parse the message key.
     * @param messageKey String representing the key in ToolTips.properties file.
     * @return String message value corresponding to the messageKey
     */
    public String parseMessageKey(String messageKey) {
        String mesg="";
        try {
            if (messageKey == null) {
                mesg = coeusToolTips.getProperty("exceptionCode.unknown");
            }
            else if (messageKey.indexOf("Tip.") == -1) {
                mesg = messageKey;
            } else {
                mesg = coeusToolTips.getProperty(messageKey,coeusToolTips.getProperty("exceptionCode.keyNotFound"));                
            }
        } catch (Exception fne) {
            mesg = "Can't read the ToolTips.properties file. " +
            	"Make sure ToolTips.properties is in the CLASSPATH.";		
        }
        return mesg;    
    }
}