package edu.ucsd.coeus.personalization.view;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import edu.mit.coeus.gui.CoeusInternalFrame;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.coeusforms.Cfield;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;
import edu.ucsd.coeus.personalization.command.ActionArgument;

public class ModuleView extends AbstractView  {
    
	private JTabbedPane tabbedPane;
	private Object formobject; //tab and pop-up forms
	private Object listobject; //For search screens
	private Object baseobject; 
	private Object basectrl;

    
    public void addFormReferences(Object ref) {
    	if (ref instanceof java.util.HashMap) {
    		for (Iterator iter = ((HashMap)ref).entrySet().iterator(); iter.hasNext();) { 
    			Map.Entry entry = (Map.Entry)iter.next();
    		    String key = (String)entry.getKey();
    		    if (key.equals("FORM"))
    		    	formobject = entry.getValue();
    		    if (key.equals("LIST"))
    		    	listobject = entry.getValue();
    		    if (key.equals("BASE"))
    		    	baseobject = entry.getValue();
    		    if (key.equals("TABPANE"))
    		    	tabbedPane = (JTabbedPane)entry.getValue();
    		    if (key.equals("BASECTRL"))
    		    	basectrl = entry.getValue();    		        		    
    		}    		
    	}
    }  
    
    public Object getBaseControllerRef() {
    	return basectrl;
    }
    
    public Object getListControllerRef() {
    	return listobject;
    }
    
    public Object getBaseRef() {
    	return baseobject;
    }

    public void cmdEnableOverWrite() {
    }
    
    public void cmdDisableOverWrite() {
    }
    
    public void cmdOverWriteTabName() {
    	super.overWriteTabName(tabbedPane);
    }
    
    public void cmdDisableTab() {
    	super.disableTab(tabbedPane);
    }    

    public void cmdOverWriteLabelInTab() {
		ActionArgument actionArg = super.getActionArgument();
		if (actionArg == null || actionArg.getID() == null)
			return;
		String classID = actionArg.getID();
		if (actionArg.isNoarg()) return;
		if (formobject != null && classID.equals(formobject.getClass().getName())) {
			overWriteCompProp(formobject, actionArg);
		}
	}    
    
    public void cmdOverWriteToolProp() {
		ActionArgument actionArg = super.getActionArgument();
		if (actionArg == null || actionArg.isNoarg())
			return;
		ArrayList argslist = actionArg.getActionArg();
		if (argslist == null || argslist.size() == 0)
			return;
		for (Iterator iter = argslist.iterator(); iter.hasNext();) {
			Cfield frmfield = (Cfield) iter.next();
			try {
				if (frmfield.getType().equals("edu.mit.coeus.gui.toolbar.CoeusToolBarButton")) {
					if (frmfield.getVarname().startsWith("#new_field")) {
						ImageIcon ic = null;
						//String tooltip = frmfield.getName();
						try {
							ic = new ImageIcon(new URL(frmfield.getIconurl()));
						} catch (MalformedURLException e) {
							ClientUtils.logger.error("Unable to access icon url",e);
						}
						CoeusToolBarButton abutton = new CoeusToolBarButton(ic);
						abutton.setName(null); //There is not enough space for name
						if (!(baseobject instanceof CoeusInternalFrame)) {
							return;
						}
						CoeusInternalFrame baseWindow = (CoeusInternalFrame)baseobject;		
						baseWindow.getFrameToolBar().add(abutton);
						modifyCustomToolButton(abutton,frmfield);
					} else {//This is not a new field
						modifyToolBarButton(baseobject,frmfield);
					}
				}
			} catch (NoSuchFieldException n) {
				ClientUtils.logger.error("Menu field name or toolbar button coded incorrectly",n);
	            CoeusOptionPane.showErrorDialog("Error in XML Field: "+n.getMessage()+" Possible incorrect field name personalization xml file");
			} catch (IllegalAccessException e) {
				ClientUtils.logger.error("Access denied to base form",e);
				CoeusOptionPane.showErrorDialog("Access denied to form "+e.getMessage());
			} catch (NullPointerException l) {
				ClientUtils.logger.error("Access denied to base form",l);
				CoeusOptionPane.showErrorDialog("Access denied to form "+l.getMessage());				
			}
		}
	}
    
    public void cmdOverWriteMenuProp() {
		ActionArgument actionArg = super.getActionArgument();
		if (actionArg == null || actionArg.isNoarg()) return;
		ArrayList argslist = actionArg.getActionArg();
		if (argslist == null || argslist.size() == 0) return;
		for (Iterator iter = argslist.iterator(); iter.hasNext();) {
			Cfield frmfield = (Cfield) iter.next();
			try {
				if (frmfield.getType().equals("edu.mit.coeus.gui.menu.CoeusMenuItem")) {
					modifyMenuItem(baseobject,frmfield);
				}
				if (frmfield.getType().equals("edu.mit.coeus.gui.menu.CoeusMenu")) {
					modifyMenu(baseobject,frmfield);
				}
			} catch (NoSuchFieldException n) {
				ClientUtils.logger.error("Menu field name coded incorrectly",n);
	            CoeusOptionPane.showErrorDialog("Error in XML Field: "+n.getMessage()+" Possible incorrect field name personalization xml file");
			} catch (IllegalAccessException e) {
				ClientUtils.logger.error("Access denied to form",e);
				CoeusOptionPane.showErrorDialog("Access denied to form "+e.getMessage());
			} catch (NullPointerException l) {
				ClientUtils.logger.error("Access denied to form",l);
				CoeusOptionPane.showErrorDialog("Access denied to form "+l.getMessage());				
			}
		}
	}
    

	public String generateReport(String style, Dataset d) {
		return "";
	}

    public String getActiveKey() {
        return "";
    }

    public char getEditMode() {
        return TypeConstants.DISPLAY_MODE;
    }

    
    
    
    
	
	

	
    
    

}
