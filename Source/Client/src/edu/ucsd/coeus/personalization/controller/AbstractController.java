package edu.ucsd.coeus.personalization.controller;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTabbedPane;

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.ucsd.coeus.personalization.CObserver;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.command.ActionArgument;
import edu.ucsd.coeus.personalization.command.ActionCommand;
import edu.ucsd.coeus.personalization.command.ActionControl;
import edu.ucsd.coeus.personalization.command.ActionQueue;
import edu.ucsd.coeus.personalization.model.FormAttrModel;

/**
 * 
 * @author rdias - UCSD
 * Using compositions for creating other controllers
 */
public abstract class AbstractController implements Observer, CObserver {
	//NOTE: if controller ref gets to be more than five then put inside array or hashmap
	private static PersonalizationController persnController;
	protected FormAttrModel attrmodel = FormAttrModel.getInstance();
	private ActionControl actionControl;
	public static CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();

	
    public AbstractController() {
    	actionControl = new ActionControl(); //If actions need to be stored then implement list
    	//Register with Model
    	attrmodel.registerObserver(this);
	}
	protected abstract void overWriteComponentsOnForms(Object uiform);
	protected abstract void overWriteComponentsOnList(Object uiform);
	protected abstract void overWriteTabProp(JTabbedPane uiform,String uniqueID, char functionType);
	protected abstract void overWriteToolbars(Object toolref, Object basewindow);
	protected abstract void overWriteToolbars(Object toolref);
	protected abstract void overWriteMenus(Object menuref);
	protected abstract void overWriteMaintain(Object menuref);
	protected abstract void initialize();
	protected abstract boolean validate();
	protected abstract ActionCommand getCommandAction(String frametitle);	
	protected abstract ActionCommand getCommandAction(ActionQueue actQueue, String frametitle);
    
	/**
	 * 
	 * @param modulename
	 * @return
	 */
	public static AbstractController getPersonalizationControllerRef() {
		if (persnController == null)
			persnController = new PersonalizationController();
		return persnController;
	}
	
	public final void customize_module(Object menuref, Object toolref, Object basewindow, String frameID) {
		try {
			if (!ClientUtils.isCached()) {
//				Date date = new Date ();
//				ClientUtils.logger.debug("Personalization config refreshed on: " + date.toString());
				attrmodel.refreshPersonalizationXML();
			}
			initialize();
			overWriteMenus(menuref);			
			overWriteToolbars(toolref, basewindow);
			overWriteComponentsOnForms(menuref);
			ActionCommand actionCommand = getCommandAction(frameID);
			if (actionCommand != null) {
				executeAction(actionCommand);
			}
		} catch (RuntimeException e) {
			ClientUtils.logger.error("Unhandled error in personalization", e);
		}
	}
	
	public final void customize_mainFrame(Object menuref, Object toolref,
			Object maintref, String frameID) {
		try {
			if (!ClientUtils.isCached()) {
				Date date = new Date ();
				ClientUtils.logger.debug("Personalization config refreshed on: " + date.toString());
				attrmodel.refreshPersonalizationXML();
			}
			initialize();
			overWriteMenus(menuref);
			overWriteToolbars(toolref);			
			overWriteMaintain(maintref);
			ActionCommand actionCommand = getCommandAction(frameID);
			if (actionCommand != null) {
				executeAction(actionCommand);
			}
		} catch (RuntimeException e) {
			ClientUtils.logger.error("Unhandled error in personalization", e);
		}
	}
	
	public final void setFormSecurity(Object securityXML) {
		attrmodel.setAccessXml(securityXML);
	}	
	
	
	public final boolean formSecurity(String moduleName,String uniqueID) {
		return attrmodel.isModuleEditable(moduleName, uniqueID);
	}	
	
	/*
	 * Generic exposed method to MIT Controller
	 * Template pattern
	 */
	public final void customize_tabs(JTabbedPane tabpane, String frameID,
			String uniqueID, char functionType) {
		try {
			initialize();
			overWriteTabProp(tabpane,uniqueID, functionType);
			ActionCommand actionCommand = getCommandAction(frameID);
			if (actionCommand != null) {
				executeAction(actionCommand);
			}
		} catch (RuntimeException e) {
			ClientUtils.logger.error("Unhandled error in personalization", e);
		}
	}
	
	/*
	 * Generic exposed method to MIT Controller
	 * Template pattern
	 * Need to overwrite individual form such as pop up dialogs
	 */
	public final void customize_Form(Object formobject, String formID) {
		try {
			initialize();
			overWriteComponentsOnForms(formobject);
			ActionCommand actionCommand = getCommandAction(formID);
			if (actionCommand != null) {
				executeAction(actionCommand);
			}
		} catch (RuntimeException e) {
			ClientUtils.logger.error("Unhandled error in personalization", e);
		}
	}
	
	/*
	 * Generic exposed method to MIT Controller
	 * Template pattern
	 * Need to overwrite individual form such as pop up dialogs
	 */
	public final void customize_List(Object listobject, String frameID) {
		try {
			initialize();
			overWriteComponentsOnList(listobject);
			ActionCommand actionCommand = getCommandAction(frameID);
			if (actionCommand != null) {
				executeAction(actionCommand);
			}
		} catch (RuntimeException e) {
			ClientUtils.logger.error("Unhandled error in personalization", e);
		}
	}	
	
	
	/*
	 * Generic exposed method to MIT Controller
	 */
	public final boolean validateForm() {
		initialize();
		return (validate());
	}
	
    /**
     * 
     */
    protected void executeAction(ActionCommand aCommand) {
    	actionControl.setCommand(aCommand);
    	actionControl.processActions();
    }

	
    /**
     * Callback Function when MIT base forms haved changed
     */
    public void update(Observable obs, Object param) {
    }
    
    /**
     * Callback Function when a single command action is processed
     */
    public void update(String actionName, ActionArgument actArg) {
	}
    
	

}
