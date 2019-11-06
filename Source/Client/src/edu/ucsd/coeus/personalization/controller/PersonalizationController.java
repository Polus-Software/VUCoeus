package edu.ucsd.coeus.personalization.controller;

import java.util.HashMap;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import edu.mit.coeus.utils.TypeConstants;
import edu.ucsd.coeus.personalization.CObserver;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.command.ActionArgument;
import edu.ucsd.coeus.personalization.command.ActionCommand;
import edu.ucsd.coeus.personalization.command.ActionQueue;
import java.awt.Component;
import javax.swing.JPanel;
/**
 * 
 * @author rdias UCSD customization engine
 * A very generic personalization controller
 * that controls the list form, the base form (tools and menus) and the
 * tab/popup forms
 */
public class PersonalizationController extends AbstractController implements
		CObserver {
	private ActionQueue actionQueue;
	private JTabbedPane tabbedPane;
	private Object formobject; //tab and pop-up forms
	private Object listobject; //For search screens
	private Object baseobject; //Menus and toolbars
	private Object basectrl; //For context sensitive reports and validation overide
	//If any new component is added then it should reflect here
	private String [] comptypes = {"javax.swing.JLabel","javax.swing.JRadioButton",
			"javax.swing.JTextField","javax.swing.JTextArea","javax.swing.JScrollPane","javax.swing.JButton",
			"javax.swing.JCheckBox","javax.swing.JTable","javax.swing.JComboBox","javax.swing.JPanel"};
	
	public PersonalizationController() {
		super();
		actionQueue = new ActionQueue();
	}
	
	
	/**
	 * use this to overwrite all the components in the form
	 * @param uiform
	 */
	protected void overWriteComponentsOnForms(Object uiform) {
		if (uiform instanceof JScrollPane) {
			uiform =  getTabObject((JScrollPane)uiform);
            if (uiform instanceof JPanel) {
                JPanel apanel = (JPanel)uiform;
                if (apanel.getComponentCount() == 1) { //This has to be a form (Temp work around) Since award and proposal are not consistent with layout
                    uiform = apanel.getComponent(0);
                }
            }
		}	
		formobject = uiform;
		if (attrmodel == null || uiform == null) return;
		ActionArgument actionArg = attrmodel.getComponentBeans(uiform.getClass().getName(),comptypes);
		attrmodel.getAuthComponentBeans(uiform.getClass().getName(), actionArg);
		if (actionArg == null) return;
    	if (!actionArg.isNoarg())
    		actionQueue.queueAction(ControllerHelper.OVERWRITE_LABELS_INTABS, actionArg);
	}
		
	protected void overWriteComponentsOnList(Object uiform) {
		listobject = uiform;
		if (attrmodel == null) return;
		ActionArgument actionArg = attrmodel.getComponentBeans(uiform.getClass().getName(),
				comptypes);
		if (actionArg == null) return;
    	if (!actionArg.isNoarg())
    		actionQueue.queueAction(ControllerHelper.OVERWRITE_LIST_COMP, actionArg);
	}	
	
	protected void overWriteMenus(Object parent) {
		if (parent == null) return;
		if (attrmodel == null) return;
    	ActionArgument actionArg = attrmodel.getMenuToolsBeans(parent.getClass().getName());
    	baseobject = parent;
    	if (actionArg == null || actionArg.isNoarg()) //Argument is required to set menus
    		return;
    	actionQueue.queueAction(ControllerHelper.OVERWRITE_MENUBAR, actionArg);
	}
	
	protected void overWriteToolbars(Object parent, Object basewindow) {
		if (parent == null) return;
		if (basewindow != null)
			this.basectrl = basewindow;
		if (attrmodel == null) return;
    	ActionArgument actionArg = attrmodel.getMenuToolsBeans(parent.getClass().getName());
    	baseobject = parent;
    	if (actionArg == null || actionArg.isNoarg()) //Argument is required to set menus
    		return;
    	actionQueue.queueAction(ControllerHelper.OVERWRITE_TOOLBAR, actionArg);
	}
	
	protected void overWriteToolbars(Object parent) {
		if (parent == null) return;
		overWriteToolbars(parent, null);
	}	
	
	
	protected void overWriteMaintain(Object parent) {
		if (parent == null) return;
		if (attrmodel == null) return;
    	ActionArgument actionArg = attrmodel.getMenuToolsBeans(parent.getClass().getName());
    	baseobject = parent;
    	if (actionArg == null || actionArg.isNoarg()) //Argument is required to set menus
    		return;
    	actionQueue.queueAction(ControllerHelper.OVERWRITE_MAITAINMENUBAR, actionArg);
	}	
	
    protected void overWriteTabProp(JTabbedPane uiform, String uniqueID, char functionType) {
        tabbedPane = uiform;
        if (attrmodel == null) {
            return;
        }
        Component[] tcomponents = uiform.getComponents();
        for (int i = 0; i < tcomponents.length; i++) {
            Object tabobject = null;
            Component component = tcomponents[i];
            if (component instanceof JScrollPane) {
                 tabobject = getTabObject((JScrollPane) component);
            } else {
                tabobject = component;
            }
            ActionArgument actionArg = attrmodel.getComponentBeans(tabobject.getClass().getName(), comptypes);
            if (actionArg != null) {
            	String tabname = (String) actionArg.getHeaderData();
            	if (ClientUtils.isNotBlank(tabname)) {//tabname change only
            		actionQueue.queueAction(ControllerHelper.OVERWRITE_TABNAME, actionArg);
            	}
            }
            if (functionType != TypeConstants.DISPLAY_MODE) {
            	boolean tabeditable = attrmodel.isTabEditable(tabobject.getClass().getName(), uniqueID);
            	if (!tabeditable) {
            		ActionArgument actionarg1 = new ActionArgument(tabobject.getClass().getName());
            		actionQueue.queueAction(ControllerHelper.DISABLE_TAB, actionarg1);
            	}
            }            
        }
    }
	
	protected void initialize() {
		actionQueue.resetQueue();
	}

	/**
	 * Keep recursing until you get the tab object
	 * @param jpane
	 * @return
	 */
	private Object getTabObject(JScrollPane jpane) {
		Object uiform = jpane.getViewport().getView();
		if (uiform != null && uiform instanceof JScrollPane) {
			uiform = getTabObject((JScrollPane)uiform);
        }
		return uiform;
	}

	
	/**
	 * Not yet implemented
	 */
	protected boolean validate() {
    	return true;
	}

	protected ActionCommand getCommandAction(String frametitle) {
		if (actionQueue.isEmpty())
			return null;
		HashMap reflist = new HashMap();
		//Add references
		if (formobject != null)
			reflist.put("FORM",formobject);
		if (listobject != null)
			reflist.put("LIST",listobject);
		if (baseobject != null)
			reflist.put("BASE",baseobject);
		if (tabbedPane != null)
			reflist.put("TABPANE",tabbedPane);
		if (basectrl != null)
			reflist.put("BASECTRL",basectrl);
		// Encapsulate this into a command
		ActionCommand actionCommand = new ActionCommand(this.actionQueue,reflist,frametitle);
		//Register for callback - Optional
		actionCommand.registerObserver(this);		
		return actionCommand;
	}	

	protected ActionCommand getCommandAction(ActionQueue actqueue, String frametitle) {
		if (actqueue.isEmpty())
			return null;
		// Add references
		HashMap reflist = new HashMap();
		//Add references
		if (formobject != null)
			reflist.put("FORM",formobject);
		if (listobject != null)
			reflist.put("LIST",listobject);
		if (baseobject != null)
			reflist.put("BASE",baseobject);
		if (tabbedPane != null)
			reflist.put("TABPANE",tabbedPane);
		if (basectrl != null)
			reflist.put("BASECTRL",basectrl);
		// Encapsulate this into a command
		ActionCommand actionCommand = new ActionCommand(actqueue, reflist, frametitle);
		return actionCommand;
	}	
	
    /**
     * Callback Handling
     */
    public void update(String actionName, ActionArgument actArg) {
	}

	public void update(String controllerName, ActionQueue actarg) {
		// TODO Auto-generated method stub
		
	}
	
    

    

}
