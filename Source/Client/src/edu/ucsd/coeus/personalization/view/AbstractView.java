package edu.ucsd.coeus.personalization.view;

import edu.mit.coeus.award.controller.OtherHeaderController;
import edu.mit.coeus.gui.CoeusFontFactory;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xmlbeans.XmlOptions;

import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.instprop.controller.IPCustomElementsController;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.customelements.CustomElementsForm;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.ucsd.coeus.personalization.ClientUtils;
import edu.ucsd.coeus.personalization.FieldListener;
import edu.ucsd.coeus.personalization.PopUpListener;
import edu.ucsd.coeus.personalization.coeusbean.Beanval;
import edu.ucsd.coeus.personalization.coeusbean.Cbean;
import edu.ucsd.coeus.personalization.coeusbean.CoeusBeansDocument;
import edu.ucsd.coeus.personalization.coeusbean.Values;
import edu.ucsd.coeus.personalization.coeusbean.CoeusBeansDocument.CoeusBeans;
import edu.ucsd.coeus.personalization.coeusforms.Cfield;
import edu.ucsd.coeus.personalization.coeusforms.Dataset;
import edu.ucsd.coeus.personalization.coeusforms.Mnemonic;
import edu.ucsd.coeus.personalization.coeusforms.Value;
import edu.ucsd.coeus.personalization.command.ActionArgument;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author rdias
 * devrmd - This would be base view class for all the modules 
 * All generic reusable code should go in here
 *
 */
public abstract class AbstractView extends java.util.Observable {
	private ActionArgument actionArgument;
	private static InstituteProposalView instituteProposalView = null;
	private static DevProposalView devProposalView = null;
	private static AwardView awardView = null;
	private static ModuleView moduleView = null;
	private static CoeusFrameView coeusFrameView = null;

	public abstract void addFormReferences(Object ref);
    public abstract String getActiveKey();
	public abstract String generateReport(String style, Dataset d);
    public abstract char getEditMode() ;
    public static DecimalFormat decf = new DecimalFormat("0");



	public void setActionArgument(ActionArgument a) {
		this.actionArgument = a;
	}
	

	/**
	 * 
	 * @param modulename
	 * @return
	 */
	public static AbstractView getViewRef(String modulename) {
		if (ClientUtils.isBlank(modulename)) {
			ClientUtils.logger.error("Invalid module name");
			return null;
		}
		if (modulename.equals(CoeusGuiConstants.INSTITUTE_PROPOSAL_FRAME_TITLE)) {
			if (instituteProposalView == null) {
				instituteProposalView = new InstituteProposalView();
			}
			return instituteProposalView;
		}
		if (modulename.equals(CoeusGuiConstants.PROPOSAL_BASE_FRAME_TITLE)) {
			if (devProposalView == null) {
				devProposalView = new DevProposalView();
			}
			return devProposalView;
		}				
		if (modulename.equals(CoeusGuiConstants.AWARD_FRAME_TITLE)) {
			if (awardView == null) {
				awardView = new AwardView();
			}
			return awardView;
		}		
		if (modulename.equals(ClientUtils.GENERIC)) {
			if (moduleView == null) {
				moduleView = new ModuleView();
			}
			return moduleView;
		}
		if (modulename.equals(ClientUtils.COEUS_FRAME)) {
			if (coeusFrameView == null) {
				coeusFrameView = new CoeusFrameView();
			}
			return coeusFrameView;
		}
		return null;
	}
	
	public ActionArgument getActionArgument() {
		return actionArgument;
	}

    /**
     * Need this for new component data binding and also be able to customize the custom fields
     * @return
     */
    public IPCustomElementsController getIPCustomElementController() {
        if (instituteProposalView != null)
            return instituteProposalView.getIPCustomElementsController();
        return null;
    }
    
    public OtherHeaderController getAwCustomElementController() {
        if (awardView != null)
            return awardView.getAwCustomElementController();
        return null;
    }

    /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyLabel(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		JLabel alabel = (JLabel)aobj;
		//Set Mnemonics if avaibale
		Mnemonic mnemonic = afield.getMnemonic();
		if (mnemonic != null) { //Set mnemonics
			String mnemstr = mnemonic.getMchar();
			try {
        		alabel.setDisplayedMnemonic(KeyStroke.getKeyStroke(mnemstr.toUpperCase()).getKeyCode());
        	}catch (IllegalArgumentException e) {
				ClientUtils.logger.error("Invalid short cut key");                		
        	}
    		if (ClientUtils.isNotBlank(mnemonic.getBindto())) {
    			Field bindField = formclass.getClass().getDeclaredField(
    					mnemonic.getBindto());
    			Object bobj = bindField.get(formclass);
    			if (bobj instanceof Component)
    				alabel.setLabelFor((Component)bobj);
    		}
		}
		//Set name
		if (ClientUtils.isNotBlank(afield.getName()))
			alabel.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(alabel, afield.getAttr());
		}
		//Set URL
		if (afield.isSetUrl()) {
			String urlname = "<html><u><font color=\"blue\">" + alabel.getText() + "</font></u></html>";
			alabel.setText(urlname);
			alabel.addMouseListener(new PopUpListener(this, alabel, afield.getUrl()));
		}
		ViewHelper.setMainAttributes(this, alabel, afield);
	}
	
	final protected void modifyComp(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
        if (afield.getVarname().startsWith("#new_field")) {
            handleNewComp(formclass,afield);
            return;
        }
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		if (aobj instanceof JRadioButton) {
			modifyRadioButton(formclass, afield);//Check to see if it is a radio button label
			return;
		}
		if (aobj instanceof JButton) {
			modifyButton(formclass, afield);//Check to see if it is a radio button label
			return;
		}		
		if (aobj instanceof JCheckBox) {
			modifyCheckBox(formclass, afield);
			return;
		}
		if (aobj instanceof JComboBox) {
			modifyComboBox(formclass, afield);
			return;
		}		
		if (aobj instanceof JLabel) {
			modifyLabel(formclass, afield);
			return;
		}
		if (aobj instanceof JTextField) {
			modifyTextField(formclass, afield);
			return;
		}
		if (aobj instanceof JTextArea) {
			modifyTextArea(formclass, afield);
			return;
		}
		if (aobj instanceof JScrollPane) {
			modifyScrollPane(formclass, afield);
			return;
		}		
		if (aobj instanceof JTable) {
			modifyTable(formclass, afield);
			return;
		}				
	}

	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 */
	final protected void modifyTextField(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		JTextField atext = (JTextField)aobj;
		if (ClientUtils.isNotBlank(afield.getDefault()))
			atext.setText(afield.getDefault()); //Set defaults
		if (afield.getAttr() != null) { 		//Set Attributes
			ViewHelper.setAttributes(atext, afield.getAttr());
		}
		if (afield.getDisabled() != null && afield.getDisabled().intValue() == 1) {
			atext.setEnabled(false);
		}		
	}
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 */
	final protected void modifyTable(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		JTable atable = (JTable)aobj;
        atable.setAutoCreateColumnsFromModel(false);
		Dataset dset = afield.getDataset();
		Value[] val = dset.getValueArray();
	    TableColumnModel tcol = atable.getColumnModel();		
		for (int i = 0; i < val.length; i++) {
			String colnm = val[i].getStringValue();
			int colnum = Integer.parseInt(val[i].getKeyindx());
		    TableColumn tcolumn = tcol.getColumn(colnum);
            if (colnm.equalsIgnoreCase("disabled")) {
                //atable.removeColumn(tcolumn); //Removing column causes problem
                tcolumn.setResizable(false);
                tcolumn.setWidth(0);
                tcolumn.setMaxWidth(0);
                tcolumn.setMinWidth(0);
                tcolumn.setPreferredWidth(0);
            } else
                tcolumn.setHeaderValue(new String(colnm));
		}
	}	
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 */
	final protected void modifyTextArea(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		JTextArea atext = (JTextArea)aobj;
		if (ClientUtils.isNotBlank(afield.getName()))
			atext.setText(afield.getName()); //Set defaults
		if (afield.getAttr() != null) { 		//Set Attributes
			ViewHelper.setAttributes(atext, afield.getAttr());
		}
		if (afield.getDisabled() != null && afield.getDisabled().intValue() == 1) {
			atext.setEnabled(false);
		}		
	}
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 */
	final protected void modifyScrollPane(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field lblField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = lblField.get(formclass);
		JScrollPane ascp = (JScrollPane)aobj;
		if (afield.getAttr() != null) { 		//Set Attributes
			ViewHelper.setAttributes(ascp, afield.getAttr());
		}
		if (afield.getDisabled() != null && afield.getDisabled().intValue() == 1) {
			ascp.setEnabled(false);
		}		
	}	
		
	
	
	
	   /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyRadioButton(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field radioField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = radioField.get(formclass);		
		if (!(aobj instanceof JRadioButton))
			return;
		JRadioButton aradio = (JRadioButton)aobj;
		if (ClientUtils.isNotBlank(afield.getName()))
			aradio.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(aradio, afield.getAttr());
		}
		ViewHelper.setMainAttributes(this, aradio, afield);
	}	
	
	   /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyCheckBox(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field chkboxField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = chkboxField.get(formclass);		
		if (!(aobj instanceof JCheckBox))
			return;
		JCheckBox achkbox = (JCheckBox)aobj;
		if (ClientUtils.isNotBlank(afield.getName()))
			achkbox.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(achkbox, afield.getAttr());
		}
		ViewHelper.setMainAttributes(this, achkbox, afield);		
	}
	
	   /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyComboBox(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field cmbboxField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = cmbboxField.get(formclass);		
		if (!(aobj instanceof JComboBox))
			return;
		JComboBox acmbbox = (JComboBox)aobj;
		if (ClientUtils.isNotBlank(afield.getDefault())) {
			acmbbox.setSelectedIndex(Integer.parseInt(afield.getDefault()));
		}
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(acmbbox, afield.getAttr());
		}
		ViewHelper.setMainAttributes(this, acmbbox, afield);		
	}		
	
	
    /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyButton(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field btnField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = btnField.get(formclass);		
		if (!(aobj instanceof JButton))
			return;
		JButton abutton = (JButton)aobj;
		//Set Mnemonics if avaibale
		Mnemonic mnemonic = afield.getMnemonic();
		if (mnemonic != null) { //Set mnemonics
			String mnemstr = mnemonic.getMchar();
        	try {
        		abutton.setMnemonic(KeyStroke.getKeyStroke(mnemstr.toUpperCase()).getKeyCode());
        	}catch (IllegalArgumentException e) {
				ClientUtils.logger.error("invalid shortcut key");                		
        	}
		}
		//Set name
		if (ClientUtils.isNotBlank(afield.getName()))
			abutton.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(abutton, afield.getAttr());
		}
		ViewHelper.setMainAttributes(this, abutton, afield);
	}
	
    /**
     * 
     * @param formclass
     * @param afield
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws NullPointerException
     */
	final protected void modifyToolBarButton(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field btnField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = btnField.get(formclass);		
		if (!(aobj instanceof CoeusToolBarButton))
			return;
		CoeusToolBarButton abutton = (CoeusToolBarButton)aobj;
		modifyToolButtonProp(abutton, afield);
	}
	
	private void modifyToolButtonProp(CoeusToolBarButton abutton, final Cfield afield) {
		//Set Mnemonics if avaibale
		Mnemonic mnemonic = afield.getMnemonic();
		if (mnemonic != null) { //Set mnemonics
			String mnemstr = mnemonic.getMchar();
        	try {
        		abutton.setMnemonic(KeyStroke.getKeyStroke(mnemstr.toUpperCase()).getKeyCode());
        	}catch (IllegalArgumentException e) {
				ClientUtils.logger.error("invalid shortcut key");                		
        	}
		}
		//Set name
		if (ClientUtils.isNotBlank(afield.getName()))
			abutton.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(abutton, afield.getAttr());
		}		
		ViewHelper.setMainAttributes(this, abutton, afield);
	}
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 */
	final protected void modifyCustomToolButton(Object aobj, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		if (!(aobj instanceof CoeusToolBarButton))
			return;
		CoeusToolBarButton abutton = (CoeusToolBarButton)aobj;
		modifyToolButtonProp(abutton, afield);
	}
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 * Set attributes for top level menu
	 */
	final protected void modifyMenu(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field mnuField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = mnuField.get(formclass);		
		if (!(aobj instanceof CoeusMenu))
			return;
		CoeusMenu cmenu = (CoeusMenu)aobj;
		//Set Mnemonics if avaibale
		Mnemonic mnemonic = afield.getMnemonic();
		if (mnemonic != null) { //Set mnemonics
			String mnemstr = mnemonic.getMchar();
			cmenu.setMnemonic(KeyStroke.getKeyStroke(mnemstr.toUpperCase()).getKeyCode());
		}
		//Set name
		if (ClientUtils.isNotBlank(afield.getName()))
			cmenu.setText(afield.getName());
		//Set Attributes
		if (afield.getAttr() != null) {
			ViewHelper.setAttributes(cmenu, afield.getAttr());
		}
		ViewHelper.setMainAttributes(this, cmenu, afield);
	}
	
	/**
	 * 
	 * @param formclass
	 * @param afield
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws NullPointerException
	 * Set attributes for menu items
	 */
	final protected void modifyMenuItem(Object formclass, final Cfield afield)
	throws NoSuchFieldException, IllegalAccessException, NullPointerException {
		Field mnuField = formclass.getClass().getDeclaredField(afield.getVarname());
		Object aobj = mnuField.get(formclass);		
		if (!(aobj instanceof CoeusMenuItem))
			return;
		CoeusMenuItem cmenu = (CoeusMenuItem)aobj;
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
		ViewHelper.setMainAttributes(this, cmenu, afield);
	}	
	
   final protected void overWriteCompProp(Object formclass, ActionArgument actionArg) {
		ArrayList argslist = actionArg.getActionArg();
		if (argslist == null || argslist.size() == 0)
			return;
		for (Iterator iter = argslist.iterator(); iter.hasNext();) {
			Cfield frmfield = (Cfield) iter.next();
			try {
				if (actionArg.getID().equals(formclass.getClass().getName())) {
					modifyComp(formclass, frmfield);
				}
			} catch (NoSuchFieldException n) {
				ClientUtils.logger.error("Label/Bind field name coded incorrectly",n);
	            CoeusOptionPane.showErrorDialog("Error in XML Field: "+n.getMessage()+" Possible incorrect field name personalization xml file");
			} catch (IllegalAccessException e) {
				ClientUtils.logger.error("Access denied to proposal detail form. Field: "+frmfield.getVarname(),e);
				CoeusOptionPane.showErrorDialog("Access denied to form field: "+frmfield.getVarname()+e.getMessage());
			} catch (NullPointerException l) {
				ClientUtils.logger.error("Access denied to proposal detail form",l);
				CoeusOptionPane.showErrorDialog("Access denied to form "+l.getMessage());				
			}
		}
	}
   
   protected void changeTabName(String className, String tabname, JTabbedPane tabbedpane) {
   	if (tabbedpane != null) {
   		int tabcnt = tabbedpane.getTabCount();
   		for (int i = 0; i < tabcnt; i++) {
   			Component tabcomp = tabbedpane.getComponentAt(i);
            if (tabcomp instanceof JScrollPane) {
                tabcomp = (Component)ViewHelper.getTabObject((JScrollPane)tabcomp);
            }
   			if (className.equals(tabcomp.getClass().getName())) {
   				tabbedpane.setTitleAt(i, tabname);
   				break;
   			}
   		}
   	}
   }
   
   protected void disableTab(String className,JTabbedPane tabbedpane) {
	   	if (tabbedpane != null) {
	   		int tabcnt = tabbedpane.getTabCount();
	   		for (int i = 0; i < tabcnt; i++) {
	   			Component tabcomp = tabbedpane.getComponentAt(i);
	            if (tabcomp instanceof JScrollPane) {
	                tabcomp = (Component)ViewHelper.getTabObject((JScrollPane)tabcomp);
	            }
	   			if (className.equals(tabcomp.getClass().getName())) {
	   				tabbedpane.setEnabledAt(i, false);
	   				tabbedpane.setForegroundAt(i, Color.GRAY);
	   				break;
	   			}
	   		}
	   	}
	   }    
      
   final protected void overWriteTabName(JTabbedPane tabbedpane) {
   	ActionArgument actionArg = getActionArgument();
   	if (actionArg == null || actionArg.isNoarg())
   		return;
   	String tabname = (String)actionArg.getHeaderData();
   	String classID = actionArg.getID();
   	if (classID == null) {
   		return;
   	}		
   	if (ClientUtils.isNotBlank(tabname)) {
   		changeTabName(classID,tabname,tabbedpane);
   	}
   }
   
   final protected void disableTab(JTabbedPane tabbedpane) {
	   ActionArgument actionArg = getActionArgument();
	   if (actionArg == null) return;
	   String classID = actionArg.getID();
	   if (classID == null) {
		   return;
	   }		
	   disableTab(classID,tabbedpane);
   }
   
   
	// Default implementation
	protected void modifyComponent(Component comp) {
		if (actionArgument == null || actionArgument.isNoarg())
			return;
		ArrayList argslist = actionArgument.getActionArg();
		if (argslist != null && argslist.size() > 0) {
			for (int i = 0; i < argslist.size(); i++) {
				Object aobj = argslist.get(i);
				if (aobj instanceof List) { // List param for this component
					List poplist = (List) aobj;
					if (comp instanceof JTextArea && poplist != null
							&& poplist.size() > 0) {
						((JTextArea) comp).setText((String) poplist.get(0));
					}
				}
				if (aobj instanceof Map) { // Map param for this component
					Map maplist = (Map) aobj;
				}

			}
		}
	}   
	
	/**
	 * @param stylemap
	 * @param bdata
	 * @param prefix
	 */
	final protected void bindBeanDataByMeth(Object bdata, Values bean) {
		Method[] methods = bdata.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methname = methods[i].getName();
			if (!((methname.startsWith("get") || methname.startsWith("is")) && methods[i].getParameterTypes().length == 0)) {
				continue;
			}
			String attrname;
			if (methname.startsWith("is"))
				attrname = methname.substring(2);
			else
				attrname = methname.substring(3);
			attrname = attrname.substring(0, 1).toLowerCase() + attrname.substring(1);
			try {
				Object result = methods[i].invoke(bdata, new Object[] {});
				if (result != null) { // Currently separated for proper formatting
					if (result instanceof java.lang.String) {
						Beanval val = bean.addNewBeanval();
						val.setName(attrname);
						val.setStringValue(result.toString());
					}
					if (result instanceof java.lang.Boolean) {
						Beanval val = bean.addNewBeanval();
						val.setName(attrname);
						val.setStringValue(result.toString());
					}
					if (result instanceof java.util.Date) {
						Beanval val = bean.addNewBeanval();
						val.setName(attrname);
						val.setStringValue(result.toString());
					}	
					if (result instanceof Double) {
						NumberFormat n = NumberFormat.getCurrencyInstance(Locale.US); 
						Beanval val = bean.addNewBeanval();
						val.setName(attrname);
						//val.setStringValue(n.format(((Double)result).doubleValue()));
                        val.setStringValue(decf.format(result));
					}
					if (result instanceof Integer) {
						Beanval val = bean.addNewBeanval();
						val.setName(attrname);
						val.setStringValue(result.toString());
					}
				}						
			} catch (IllegalAccessException e) {
				ClientUtils.logger.error("Error while invoking get",e);
			} catch (InvocationTargetException e) {
				ClientUtils.logger.error("Error while invoking get",e);
			}
		}
	}
	
	final protected String getContextReport(String style, Dataset d, String queryKey) {
	    QueryEngine queryEngine;
        queryEngine = QueryEngine.getInstance();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Source templateSource = new StreamSource(style); //Using HTTP URL
		CoeusBeansDocument beandoc = CoeusBeansDocument.Factory.newInstance();
		CoeusBeans beans = beandoc.addNewCoeusBeans();
		try {
	        Transformer trans = transformerFactory.newTransformer(templateSource);
	        trans.clearParameters();
	        /* Add data that is not available via xml such as date */
	        trans.setParameter("currentDate", DateFormat.getInstance().format(new Date()));
	        trans.setParameter("loggedinuser", CoeusGuiConstants.getMDIForm().getUserId());
			Value[] val = d.getValueArray();
			for (int i = 0; i < val.length; i++) {
				CoeusVector cvData = queryEngine.getDetails(queryKey, Class.forName(val[i].getStringValue()));
				if (cvData.capacity() == 0) {
					continue;
				}
				Cbean cbean = beans.addNewCbean();
				cbean.setBeanid(val[i].getKeyindx());
				 Enumeration e = cvData.elements ();
				 int k = 0;
				 while (e.hasMoreElements ()) {
					 Object aobj = e.nextElement ();
					 if (aobj != null) {
						 Values bean = cbean.addNewBean();
						 bean.setIdx(k++);
						 bindBeanDataByMeth(aobj,bean);
					 }
				 }
			}
			StringWriter sw = new StringWriter();
			Source xmlSource =   new StreamSource(new StringReader(beandoc.xmlText()));
//			XmlOptions printOptions = new XmlOptions();
//			printOptions.setSavePrettyPrint();
//			System.out.println("DEBUG:\n" + beandoc.xmlText(printOptions));
			trans.transform(xmlSource, new StreamResult(sw));
			return sw.toString();
		} catch (TransformerConfigurationException e) {
			ClientUtils.logger.error(e);
			return "transform config error";
		} catch (TransformerException e1) {
			ClientUtils.logger.error(e1);
			return "transform error";			
		}  catch (Exception e) {
			ClientUtils.logger.error(e);
			return "xslt error";						
		}
	}

   /**
     * Add new component to existing panel
     * @param formclass
     * @param afield
     * @param querystr
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.NullPointerException
     */
    final protected void handleNewComp(Object formclass, final Cfield afield)
            throws NoSuchFieldException, IllegalAccessException, NullPointerException {
        if (!afield.getType().equals("javax.swing.JPanel")) return;
        Dataset dset = afield.getDataset();
        if (dset == null) return;
        String panelnm = ClientUtils.substringAfter(afield.getVarname(), "#new_field_");
		Value[] val = dset.getValueArray();
        String module, columnNM, container, label;
        module=columnNM=container=label="";
        int gridx, gridy, width, height, anchor;
        gridx = -1; gridy = -1; anchor = -1; width = 0; height = 0;
		for (int i = 0; i < val.length; i++) {
			String dataval = val[i].getStringValue();
            String datakey = val[i].getKeyindx();
            if (datakey.equals("module"))
                module = dataval;
            if (datakey.equals("columnNm"))
                columnNM = dataval;
            if (datakey.equals("gridx"))
                gridx = Integer.parseInt(dataval);
            if (datakey.equals("gridy"))
                gridy =  Integer.parseInt(dataval);
            if (datakey.equals("anchor"))
                anchor = Integer.parseInt(dataval);
            if (datakey.equals("label"))
                label = dataval;
            if (datakey.equals("width"))
                width =  Integer.parseInt(dataval);
            if (datakey.equals("height"))
                height =  Integer.parseInt(dataval);
            if (datakey.equals("container"))
                container = dataval;
		}
		JComponent aparent = null;
		Field parentfield = null;
		if (container.equals("parent")) {
			aparent = (JComponent)formclass;
		} else {
			parentfield = formclass.getClass().getDeclaredField(container);
		}
        if (parentfield != null || aparent != null) {
        	if (parentfield != null) {
        		Object parentref = parentfield.get(formclass);
        		aparent = (JComponent)parentref;
        	}
            if (isCreated(aparent, panelnm)) return;
            JPanel npanel = new JPanel();
            npanel.setName(panelnm);
            JTextField txtfield = new JTextField();
            txtfield.addFocusListener(new FieldListener(this, module, columnNM, txtfield));
            JLabel txtlabel = new JLabel(label);
            txtlabel.setFont(CoeusFontFactory.getLabelFont());
            npanel.add(txtlabel);
            npanel.add(txtfield);
            if (width > 0 && height > 0) {
                txtfield.setMinimumSize(new java.awt.Dimension(width, height));
                txtfield.setPreferredSize(new java.awt.Dimension(width, height));
            }
           GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
           if (anchor >= 0) gridBagConstraints.anchor =   anchor;
           if (gridx >= 0) gridBagConstraints.gridx = gridx;
           if (gridy >= 0) gridBagConstraints.gridy = gridy;
           aparent.add(npanel,gridBagConstraints);
           fetchData(module,columnNM,txtfield);
           aparent.validate();
        }
    }

    private boolean isCreated(JComponent comp, String panelnm) {
        boolean fnd = false;
        Component[]  comps = comp.getComponents();
        for (int i = 0; i < comps.length; i++) {
            Component component = comps[i];
            if (component.getName() != null && component.getName().equals(panelnm)) {
                fnd = true;
                break;
            }
        }
        return fnd;
    }

    private void fetchData(String module, String columnnm, JTextField comp) {
        String datastr = "";
        if (module.equalsIgnoreCase("proposal")) {
            IPCustomElementsController cctrl = this.getIPCustomElementController();
             if(getEditMode() == TypeConstants.DISPLAY_MODE) {
                comp.setEnabled(false);
                comp.setDisabledTextColor(Color.BLACK);
            }
            CustomElementsForm cform = (CustomElementsForm) cctrl.getControlledUI();            
            datastr  = getCustomData(cform.getTable(), columnnm);
        }
        if (module.equalsIgnoreCase("award")) {
            OtherHeaderController cctrl = this.getAwCustomElementController();
            CustomElementsForm cform = (CustomElementsForm) cctrl.getControlledUI();
            datastr  = getCustomData(cform.getTable(), columnnm);
            if(getEditMode() == TypeConstants.DISPLAY_MODE) {
                comp.setEnabled(false);
                comp.setDisabledTextColor(Color.BLACK);
            }
        }
        comp.setText(datastr);
    }

    private String getCustomData(JTable atable, String columnnm) {
        String custdata = "";
        int rowCount = atable.getRowCount();
        if(rowCount > 0){
            for(int inInd = 0; inInd < rowCount ;inInd++){
                String fieldid = (String)((DefaultTableModel) atable.getModel()).getValueAt(inInd,9);
                if (fieldid.equals(columnnm)) {
                    custdata = ((DefaultTableModel)atable.getModel()).getValueAt(inInd, 1).toString();
                    break;
                }
            }
        }
        return custdata;
    }

		
	

	
	
}
