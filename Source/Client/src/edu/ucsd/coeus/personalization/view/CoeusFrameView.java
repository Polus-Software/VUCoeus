package edu.ucsd.coeus.personalization.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.KeyStroke;

import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuBar;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.coeusforms.Cfield;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;
import edu.ucsd.coeus.personalization.command.ActionArgument;

public class CoeusFrameView extends ModuleView {
    private CoeusAppletMDIForm desktop;
	HashMap maintMenuCache;
    
    /**
     * 
     *
     */
    private void setMenuRef() {
    	this.desktop = (CoeusAppletMDIForm)super.getBaseRef();
		CoeusMenuBar cmenubar = desktop.getCoeusMenuBar();
		int mnucnt = cmenubar.getMenuCount();		
		for (int i=0; i < mnucnt; i++) {
			if (cmenubar.getMenu(i) instanceof CoeusMenu) {
				String menuName = cmenubar.getMenu(i).getName();
				if (menuName.equals("Maintain")) {
					Vector maintMenu = ((CoeusMenu)cmenubar.getMenu(i)).getChildren();
					cacheMaintainMenuItems(maintMenu);
				}
				//TODO.. set other ref here such as file, admin etc
			}
		}				
	}
    
    /**
	 * 
	 * 
	 */
	private void cacheMaintainMenuItems(Vector maintMenu) {
		if (maintMenuCache == null)
			maintMenuCache = new HashMap();
		for (Iterator iter = maintMenu.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof CoeusMenuItem) {
				CoeusMenuItem menuitem = (CoeusMenuItem) element;
				maintMenuCache.put(menuitem.getName(), menuitem);
			}
		}
	}
    
    
	private void modifyCoeusMenuItem(CoeusMenuItem cmenu, Cfield afield) {
		//Set Accelator if avaibale
		if (ClientUtils.isNotBlank(afield.getShortcut())) { 
			cmenu.setAccelerator(KeyStroke.getKeyStroke(afield.getShortcut()));
		}
		//Set name
		if (ClientUtils.isNotBlank(afield.getName()))
			cmenu.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(cmenu, afield.getAttr());
		}
	}	    
    
    /*
     * Using the maintain menu properties defined in coeus_forms.xml, 
     * this method will overwrite the base maintain menu properties defined in
     * the concrete CoeusAppletMDIForm
     */
    public void cmdOverWriteMaintMenu() {
    	setMenuRef();
		ActionArgument actionArg = super.getActionArgument();
		if (actionArg == null || actionArg.isNoarg())
			return;
		ArrayList argslist = actionArg.getActionArg();
		if (argslist == null || argslist.size() == 0)
			return;
		for (Iterator iter = argslist.iterator(); iter.hasNext();) {
			Cfield frmfield = (Cfield) iter.next();
			CoeusMenuItem menuitem = (CoeusMenuItem)maintMenuCache.get(frmfield.getName());
			if (menuitem != null) {
				modifyCoeusMenuItem(menuitem, frmfield);
			}
		}
	}

	public String generateReport(String style, Dataset d) {
		return "No Context Report at Base Frame";
	} 
    
    

}
