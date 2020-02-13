/*
 * CoeusToolTip.java
 * 
 * Handler for tool tip messages
 * 
 * @version: 1.0
 * @author: Jill McAfee, Vanderbilt University Office of Research
 * @created: August 29, 2011
 */
package edu.vanderbilt.coeus.gui;

/**
 * This is a class for looking up tooltips by code
 *
 * @version 1.0 
 * @created August 29, 2011
 * @author: Jill McAfee, Vanderbilt University Office of Research
 */
public class CoeusToolTip {
    public static String toolTip = "";
    private static CoeusToolTipMessage coeusToolTipMessage;

    /**
     * Constructor
     */
    public CoeusToolTip() {
    }
    
    public static String getToolTip(String toolTipCode) {
    	coeusToolTipMessage = new CoeusToolTipMessage();
    	toolTip = coeusToolTipMessage.parseMessageKey(toolTipCode);
    	return toolTip;
    }
}