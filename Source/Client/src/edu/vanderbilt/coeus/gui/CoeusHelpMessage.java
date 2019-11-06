/*
 * CoeusHelpMessage.java
 * 
 * Handler for help messages
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: June 28, 2011
 */
package edu.vanderbilt.coeus.gui;

import java.io.InputStream;
import java.util.Properties;

/**
 * This is a class that provides access to HelpMessages.properties file.
 *
 * @version 1.0 June 22, 2011
 * @author  Jill McAfee
 * @author  Vanderbilt University
 */
public class CoeusHelpMessage {
    private Properties coeusHelpMessages;
    private static final String HELP_MESSAGES = "/edu/vanderbilt/coeus/resources/HelpMessages.properties";
    
    /**
     * Constructor
     */
    public CoeusHelpMessage() {
        init();
    }

    /**
     * Create and load an Input Stream for reading the properties file.
     */
    private void init() {
    	try {
    		InputStream inputStream = getClass().getResourceAsStream(HELP_MESSAGES); 
            coeusHelpMessages = new Properties();  
            coeusHelpMessages.load(inputStream);  

        } catch (Exception fne) {
            System.err.println("Can't read the HelpMessages.properties file. " +
                    "Make sure HelpMessages.properties is in the CLASSPATH.");
            System.err.println(fne);
        }
    }
    
    /**
     * This method is used to parse the message key.
     * @param messageKey String representing the key in HelpMessages.properties file.
     * @return String message value corresponding to the messageKey
     */
    public String parseMessageKey(String messageKey) {
        String mesg="";
        try {
            if (messageKey == null) {
                mesg = coeusHelpMessages.getProperty("exceptionCode.unknown");
            }
            else if (messageKey.indexOf("Code.") == -1) {
                mesg = messageKey;
            } else {
                mesg = coeusHelpMessages.getProperty(messageKey,coeusHelpMessages.getProperty("exceptionCode.keyNotFound"));                
            }
        } catch (Exception fne) {
            mesg = "Can't read the HelpMessages.properties file. " +
            	"Make sure HelpMessages.properties is in the CLASSPATH.";		
        }
        return mesg;    
    }
}