package edu.vanderbilt.coeus.utils;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusGuiConstants;

public class DisplayOptions {
  
	public DisplayOptions() {
		// For instantiating class
	}

    /**
     * Method to determine if field should be disabled based on parameter
     * @return boolean true if field is enabled, false if disabled
     * @throws CoeusClientException 
     */
	public static boolean canModifyAllFields(String loggedinUser) throws CoeusClientException {
		boolean getsToDoStuff = false;
		
		UserPermissions permission = new UserPermissions(loggedinUser);
		if (permission.canModifyPerson()) {
			if (permission.hasOspRight().equals("1")) {
				getsToDoStuff = true;
			}
		}
		return getsToDoStuff;
	}
	
	public java.util.List<Image> getInstitutionIcons() {
		/* Get institution */
    	CustomFunctions custom = new CustomFunctions();
    	String[] params = (String[]) custom.getParameterValues("ACRONYM");
    	String acronym = (String) params[0];
    	
    	/* Set icons based on acronym */
        java.util.List<Image> icons = new ArrayList<Image>();
        if (acronym.equals("VUMC")) {
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON128_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON96_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON64_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON48_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON32_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON24_VUMC)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON16_VUMC)).getImage());
        }
        else {
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON128_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON96_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON64_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON48_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON32_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON24_VU)).getImage());
        	icons.add(new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ICON16_VU)).getImage());
        }

		return icons;
	}

	
}
