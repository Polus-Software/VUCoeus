package edu.vanderbilt.coeus.gui.toolbar;

import edu.vanderbilt.coeus.utils.CustomFunctions;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ToolFunctions {
    
    public ToolFunctions() {

    }
    
    /**
     * Method to open mail window for contacting Coeus Help
     * @return void
     * @throws URISyntaxException 
     */
    public void contactCoeusHelp() {
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = custom.getParameterValues("CMS_REPLY_TO_ID");
    	String emailAddress = params[0];
    	System.out.println("Send emails to " + emailAddress);
    	
    	Desktop desktop;
  	  	URI mailto;
    	if (Desktop.isDesktopSupported() 
    	    && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
			try {
				mailto = new URI("mailto:" + emailAddress + "?subject=Coeus%20Help");
				try {
					desktop.mail(mailto);
				} catch (IOException e) {
					System.out.println("Unable to initiate mail desktop mail application");
					e.printStackTrace();
				}
			} catch (URISyntaxException e) {
				System.out.println("Invalid email address for dektop mail appliation");
				e.printStackTrace();
			}
    	}
    }
}
