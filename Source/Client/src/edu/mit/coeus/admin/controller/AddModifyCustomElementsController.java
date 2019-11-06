/*
 * AddModifyCustomElementsController.java
 *
 * Created on December 21, 2004, 9:48 AM
 */

package edu.mit.coeus.admin.controller;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * @author  nadhgj
 */

import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.admin.gui.AddModifyCustomDataElements;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.customelements.bean.CustomElementsUsageBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.KeyConstants;
import edu.mit.coeus.utils.LimitedPlainDocument;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.StrictEquals;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;

import java.util.Hashtable;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import javax.swing.event.ListSelectionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;


public class AddModifyCustomElementsController extends AdminController implements ActionListener, 
        ListSelectionListener, ItemListener {
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm;
    
    private AddModifyCustomDataElements addModifyCustomDataElements;
    
    private CoeusDlgWindow customElementsDlgWindow;
    
    /*width of the hierarchy list form*/
    private static final int WIDTH = 730;
    
    /*height of the hierarchy list form*/
    private static final int HEIGHT = 300;
    
    private CustomElementsInfoBean customElementsInfoBean;
    
    private CustomElementsUsageBean customElementsUsageBean;
    
    private CoeusVector cvColumnUsage;
    
    private ColumnUsageRenderer columnUsageRenderer;
    
    private ColumnUsageModel columnUsageModel;
    
    private ModuleModel moduleModel;
    
    private ColumnUsageEditor columnUsageEditor;
    
    private CoeusVector cvLookupWindow;
    
    private String EMPTY_STRING = "";
    
    private CoeusVector cvLookupArgs;
    
    private CoeusVector cvModulesNotInUsage;
    
    private static final String GET_SERVLET = "/BudgetMaintenanceServlet";
    
    private static final char GET_ARGS_MODULES_NOT_IN_USAGE = 'W';
    
    private static final String connect = CoeusGuiConstants.CONNECTION_URL + GET_SERVLET;
    
    public static final int MODULE_COLUMN = 0;
    public static final int REQUIRED_COLUMN = 1;
    
    private static final String CODE_TABLE = "CODE_TABLE";
    
    private static final String ARG_TABLE = "ARG_TABLE";
    
    private static final String OTHER = "Other";
    
    private boolean modified;
    
    private boolean comboItemChanged;
    
    private Hashtable htcustomElementData;
    
    /** Creates a new instance of AddModifyCustomElementsController */
    public AddModifyCustomElementsController(Hashtable customElementData, char functionType) throws CoeusException {
        htcustomElementData = customElementData;
        setFunctionType(functionType);
        initComponents();
        registerComponents();
        setFormData(customElementData);
        setColumnData();
    }
    
    private void initComponents() {
        coeusMessageResources = CoeusMessageResources.getInstance();
        columnUsageRenderer = new ColumnUsageRenderer();
        columnUsageModel = new ColumnUsageModel();
        moduleModel = new ModuleModel();
        columnUsageEditor = new ColumnUsageEditor();
        mdiForm = CoeusGuiConstants.getMDIForm();
        addModifyCustomDataElements = new AddModifyCustomDataElements();
        customElementsDlgWindow = new CoeusDlgWindow(mdiForm,true);
        if(cvModulesNotInUsage != null && cvModulesNotInUsage.size() > 0) 
            addModifyCustomDataElements.btnAdd.setEnabled(false);
        else addModifyCustomDataElements.btnAdd.setEnabled(true);
        customElementsDlgWindow.setResizable(false);
        customElementsDlgWindow.getContentPane().add(addModifyCustomDataElements);
        customElementsDlgWindow.setTitle("Add/Modify Custom Data Elements");
        customElementsDlgWindow.setFont(CoeusFontFactory.getLabelFont());
        customElementsDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        customElementsDlgWindow.setSize(WIDTH, HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = customElementsDlgWindow.getSize();
        customElementsDlgWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        customElementsDlgWindow.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent actionEvent){
                if(!performCancelAction()) return;
                    customElementsDlgWindow.dispose();
           }
        });
        customElementsDlgWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                if(!performCancelAction()) return;
                customElementsDlgWindow.dispose();
            }
        });
        
        customElementsDlgWindow.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setRequestFocusInThread();
            }
        });
        
        addModifyCustomDataElements.tblModules.getInputMap( JComponent.WHEN_FOCUSED).put(
        KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ),   "enterPressed" );
        addModifyCustomDataElements.tblModules.getActionMap().put("enterPressed", new AbstractAction("enterPressed") {
            public void actionPerformed(ActionEvent actionEvent) {
                performOkAction();
            }
        });
        
        addModifyCustomDataElements.tblColumnUsage.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "enterPressed");
        addModifyCustomDataElements.getActionMap().put("enterPressed", new AbstractAction(){
            public void actionPerformed(ActionEvent actionEvent) {
                performOkAction();
            }
        });
    }
    
    
    public void display() {
        addModifyCustomDataElements.txtColumnName.requestFocusInWindow();
        customElementsDlgWindow.show();
    }
    
    public void formatFields() {

    }
    
    public java.awt.Component getControlledUI() {
        return null;
    }
    
    public Object getFormData() {
        return null;
    }
    
    public void registerComponents() {
        Component[] component = {addModifyCustomDataElements.txtColumnName,
        addModifyCustomDataElements.txtColumnLabel,addModifyCustomDataElements.cmbDataType,addModifyCustomDataElements.txtDataLength,addModifyCustomDataElements.txtGroupData,addModifyCustomDataElements.txtDefaultValue,
        addModifyCustomDataElements.chkLookUp,addModifyCustomDataElements.cmbLookupWindow,addModifyCustomDataElements.cmbLookupArgument,addModifyCustomDataElements.btnAdd,
        addModifyCustomDataElements.btnRemove,addModifyCustomDataElements.btnOk,addModifyCustomDataElements.btnCancel};
        
        ScreenFocusTraversalPolicy policy = new ScreenFocusTraversalPolicy(component);
        addModifyCustomDataElements.setFocusTraversalPolicy(policy);
        addModifyCustomDataElements.setFocusCycleRoot(true);
        
        LimitedPlainDocument limitedPlainDocument = new LimitedPlainDocument(30);
        limitedPlainDocument.setFilter(LimitedPlainDocument.FORCE_UPPERCASE);
        addModifyCustomDataElements.txtColumnName.setDocument(limitedPlainDocument);
        addModifyCustomDataElements.tblColumnUsage.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addModifyCustomDataElements.tblColumnUsage.getSelectionModel().addListSelectionListener(this);
        addModifyCustomDataElements.tblModules.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addModifyCustomDataElements.tblModules.getSelectionModel().addListSelectionListener(this);
        addModifyCustomDataElements.txtDataLength.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        addModifyCustomDataElements.txtColumnLabel.setDocument(new LimitedPlainDocument(30));
        addModifyCustomDataElements.txtDefaultValue.setDocument(new LimitedPlainDocument(2000));
        addModifyCustomDataElements.txtColumnLabel.addKeyListener(new PressedEnter());
        addModifyCustomDataElements.txtColumnName.addKeyListener(new PressedEnter());
        addModifyCustomDataElements.txtDataLength.addKeyListener(new PressedEnter());
        addModifyCustomDataElements.txtDefaultValue.addKeyListener(new PressedEnter());
        addModifyCustomDataElements.cmbDataType.addItemListener(this);
        addModifyCustomDataElements.cmbLookupArgument.addItemListener(this);
        addModifyCustomDataElements.cmbLookupWindow.addItemListener(this);
        addModifyCustomDataElements.chkLookUp.addItemListener(this);
        addModifyCustomDataElements.btnAdd.addActionListener(this);
        addModifyCustomDataElements.btnCancel.addActionListener(this);
        addModifyCustomDataElements.btnOk.addActionListener(this);
        addModifyCustomDataElements.btnRemove.addActionListener(this);
        addModifyCustomDataElements.tblColumnUsage.setModel(columnUsageModel);
        addModifyCustomDataElements.tblModules.setModel(moduleModel);
    }
    
    public void saveFormData() {
        if(cvModulesNotInUsage != null) {
            for(int index=0;index<cvModulesNotInUsage.size();index++) {
                customElementsUsageBean = (CustomElementsUsageBean)cvModulesNotInUsage.get(index);
                if(customElementsUsageBean.getAcType() != null)
                    cvColumnUsage.add(customElementsUsageBean);
            }
        }
    }
    
    public void setFormData(Object data) throws CoeusException {
        if(data != null) {
            Hashtable elementData = (Hashtable)data;
            customElementsInfoBean = elementData.get(CustomElementsInfoBean.class)==null? new CustomElementsInfoBean() :
                (CustomElementsInfoBean)elementData.get(CustomElementsInfoBean.class);
            cvColumnUsage = (CoeusVector)elementData.get(CustomElementsUsageBean.class);
            elementData = null;
        }else{
            customElementsInfoBean = new CustomElementsInfoBean();
        }
        if(getFunctionType()=='N'){
            cvColumnUsage = new CoeusVector();
        }
        getLookupArgsAndModulesNotInUsage();
        populateDataTypeCombo();
        populateLookupWindowCombo();
        checkModulesInUsage();
        if(getFunctionType()!='N') {
            addModifyCustomDataElements.txtColumnLabel.setText(customElementsInfoBean.getColumnLabel());
            addModifyCustomDataElements.txtColumnName.setEditable(false);
            addModifyCustomDataElements.txtColumnName.setOpaque(true);
            addModifyCustomDataElements.txtColumnName.setBackground(java.awt.Color.white);
            addModifyCustomDataElements.txtColumnName.setText(customElementsInfoBean.getColumnName());
            addModifyCustomDataElements.txtDataLength.setText(new Integer(customElementsInfoBean.getDataLength()).toString());
            
            addModifyCustomDataElements.txtDefaultValue.setText(customElementsInfoBean.getDefaultValue());
            addModifyCustomDataElements.cmbDataType.setSelectedItem(customElementsInfoBean.getDataType());
            // Enhancement to Add Group Name start
            addModifyCustomDataElements.txtGroupData.setText(customElementsInfoBean.getGroupCode());
            // Enhancement to Add Group Name End
            addModifyCustomDataElements.cmbLookupWindow.setSelectedItem(customElementsInfoBean.getLookUpWindow());
            Equals operator = new Equals("description",customElementsInfoBean.getLookUpArgument());
            CoeusVector cvArgData = cvLookupArgs.filter(operator);
            ComboBoxBean comboBoxBean = null;
            if(cvArgData!=null && cvArgData.size()>0)
                comboBoxBean = (ComboBoxBean)cvArgData.get(0);
            else comboBoxBean = new ComboBoxBean(EMPTY_STRING,EMPTY_STRING);
            addModifyCustomDataElements.cmbLookupArgument.setSelectedItem(comboBoxBean);
            addModifyCustomDataElements.chkLookUp.setSelected(customElementsInfoBean.isHasLookUp());
            columnUsageModel.setData(cvColumnUsage);
            moduleModel.setData(cvModulesNotInUsage);
            if(addModifyCustomDataElements.tblModules.getRowCount() > 0)
                addModifyCustomDataElements.tblModules.setRowSelectionInterval(0,0);
        }else {
            addModifyCustomDataElements.btnRemove.setEnabled(false);
        }
        boolean enabled = addModifyCustomDataElements.chkLookUp.isSelected();
        addModifyCustomDataElements.cmbLookupWindow.setEnabled(enabled);
        addModifyCustomDataElements.cmbLookupArgument.setEnabled(enabled);
        if(cvModulesNotInUsage == null || cvModulesNotInUsage.size() == 0)
            addModifyCustomDataElements.btnAdd.setEnabled(false);
        comboItemChanged = true;
    }
    
    private void checkModulesInUsage() {
        for(int index=0;index<cvColumnUsage.size();index++) {
            CustomElementsUsageBean inUsage = (CustomElementsUsageBean)cvColumnUsage.get(index);
            String module = inUsage.getDescription();
            for(int notInUsageIndex=0;notInUsageIndex<cvModulesNotInUsage.size();notInUsageIndex++) {
                CustomElementsUsageBean notInUsage = (CustomElementsUsageBean)cvModulesNotInUsage.get(notInUsageIndex);
                if(module.equals(notInUsage.getDescription()))
                    cvModulesNotInUsage.removeElementAt(notInUsageIndex);
            }
        }
    }
    
    public boolean validate() {
        if(getFunctionType() == 'N') {
            Equals operator = new Equals("columnName",addModifyCustomDataElements.txtColumnName.getText());
            CoeusVector chkDuplicate = ((CoeusVector)htcustomElementData.get(CoeusVector.class)).filter(operator);
            if(chkDuplicate != null && chkDuplicate.size()>0 ) {
        	CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1501"));
 	        return false;
            }
        }
        
        if(addModifyCustomDataElements.txtColumnName.getText() == null ||
                addModifyCustomDataElements.txtColumnName.getText().equals(EMPTY_STRING)) {
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1502"));
               return false;
        }
        if(addModifyCustomDataElements.txtColumnLabel.getText() == null ||
                addModifyCustomDataElements.txtColumnLabel.getText().equals(EMPTY_STRING)) {
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1503"));
               return false;
        }
        
        if(addModifyCustomDataElements.cmbDataType.getSelectedItem() == null ||
                addModifyCustomDataElements.cmbDataType.getSelectedItem().toString().equals(EMPTY_STRING)) {
               CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1504"));
               return false;
        }
      
//        //Added by Nadh For BugFix - 1460
//        if(EMPTY_STRING.equals(addModifyCustomDataElements.txtDataLength.getText()) ||
//                Integer.parseInt(addModifyCustomDataElements.txtDataLength.getText()) <= 0) {
//             CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1506"));
//             return false;   
//        }
//        //End
        return true;
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        customElementsDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        if(source.equals(addModifyCustomDataElements.btnAdd)) {
            performAddAction();
        }else if(source.equals(addModifyCustomDataElements.btnCancel)) {
            if(!performCancelAction()) return;
            customElementsDlgWindow.dispose();
        }else if(source.equals(addModifyCustomDataElements.btnOk)) {
            performOkAction();
        }else if(source.equals(addModifyCustomDataElements.btnRemove)) {
            performRemoveAction();
        }
        customElementsDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void setColumnData() {
       addModifyCustomDataElements.tblColumnUsage.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       addModifyCustomDataElements.tblColumnUsage.setRowHeight(22);
       addModifyCustomDataElements.tblColumnUsage.setOpaque(true);
       addModifyCustomDataElements.tblColumnUsage.setShowGrid(false);
       addModifyCustomDataElements.tblColumnUsage.setBackground((java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background"));
       addModifyCustomDataElements.tblColumnUsage.setRowSelectionAllowed(true);
       
       addModifyCustomDataElements.tblModules.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
       addModifyCustomDataElements.tblModules.setRowHeight(22);
       addModifyCustomDataElements.tblModules.setOpaque(true);
       addModifyCustomDataElements.tblModules.setBackground((java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background"));
       addModifyCustomDataElements.tblModules.setShowGrid(false);
       
       JTableHeader tableheader =  addModifyCustomDataElements.tblColumnUsage.getTableHeader();
       tableheader.setReorderingAllowed(false);
       tableheader.setFont(CoeusFontFactory.getLabelFont());
       TableColumn column = addModifyCustomDataElements.tblColumnUsage.getColumnModel().getColumn(MODULE_COLUMN);
       column.setMinWidth(100);
       column.setPreferredWidth(160);
       column.setResizable(false);
       
       column = addModifyCustomDataElements.tblColumnUsage.getColumnModel().getColumn(REQUIRED_COLUMN);
       column.setMinWidth(60);
       column.setPreferredWidth(90);
       column.setResizable(false);
       column.setCellRenderer(columnUsageRenderer);
       column.setCellEditor(columnUsageEditor);
       
       addModifyCustomDataElements.tblModules.setTableHeader(null);
       column = addModifyCustomDataElements.tblModules.getColumnModel().getColumn(MODULE_COLUMN);
       column.setPreferredWidth(232);
       column.setResizable(false);
    }
    
    public void valueChanged(javax.swing.event.ListSelectionEvent listSelectionEvent) {
        Object source = listSelectionEvent.getSource();
        if( !listSelectionEvent.getValueIsAdjusting()){
            if(source.equals(addModifyCustomDataElements.tblColumnUsage.getSelectionModel()))
               columnUsageEditor.stopCellEditing();
        }
        if(cvModulesNotInUsage != null && cvModulesNotInUsage.size()>0) 
            addModifyCustomDataElements.btnAdd.setEnabled(true);
        else addModifyCustomDataElements.btnAdd.setEnabled(false);
        if(cvColumnUsage != null && cvColumnUsage.size()>0) 
            addModifyCustomDataElements.btnRemove.setEnabled(true);
        else addModifyCustomDataElements.btnRemove.setEnabled(false);
    }
    
    public class PressedEnter extends KeyAdapter {
		
	public void keyPressed(KeyEvent ae) {
            if(ae.getKeyCode() == KeyEvent.VK_ENTER) {
                performOkAction();
            }
        }
    }
    private void getLookupArgsAndModulesNotInUsage() throws CoeusException{
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_ARGS_MODULES_NOT_IN_USAGE);
        requester.setDataObject(customElementsInfoBean.getColumnName());
        AppletServletCommunicator communicator = new AppletServletCommunicator(connect,requester);
        communicator.send();
        ResponderBean responder = communicator.getResponse();
        if(responder != null) {
            if(responder.isSuccessfulResponse()) {
                Hashtable htData = (Hashtable)responder.getDataObject();
                cvLookupArgs = (CoeusVector)htData.get(ComboBoxBean.class);
                cvModulesNotInUsage = (CoeusVector)htData.get(KeyConstants.MODULE_NAMES);
            }
        }else {
            //server error
            throw new CoeusException(responder.getMessage());
        }
    }
    
    
    private void populateDataTypeCombo() {
        String[] code = {"","VARCHAR2","NUMBER","DATE"};
        String[] values = {"","String","Number","Date"};
        ComboBoxBean comboBoxBean;
        for(int i = 0; i<code.length;i++){
            comboBoxBean = new ComboBoxBean(code[i],values[i]);
            addModifyCustomDataElements.cmbDataType.addItem(comboBoxBean);
        }
    }
    
    private void populateLookupWindowCombo() {
        // 4580: Add organization and sponsor search in custom elements - Start
//        String[] lookupWindowData = {"","w_person_select","w_unit_select","w_rolodex_select",
//                "w_select_cost_element","w_arg_code_tbl","w_arg_value_list"};
        String[] lookupWindowData = {"","w_person_select","w_unit_select","w_rolodex_select",
        "w_select_cost_element","w_arg_code_tbl","w_arg_value_list","w_sponsor_select","w_organization_select"};
        // 4580: Add organization and sponsor search in custom elements - End
        ComboBoxBean comboBoxBean;
        for(int index=0;index<lookupWindowData.length;index++) {
            comboBoxBean = new ComboBoxBean(lookupWindowData[index],lookupWindowData[index]);
            addModifyCustomDataElements.cmbLookupWindow.addItem(comboBoxBean);
        }
    }
    
    public void itemStateChanged(java.awt.event.ItemEvent itemEvent) {
        Object source = itemEvent.getSource();
        if(source.equals(addModifyCustomDataElements.chkLookUp)) {
            
            boolean enabled = addModifyCustomDataElements.chkLookUp.isSelected();
            addModifyCustomDataElements.cmbLookupArgument.setEnabled(enabled);
            addModifyCustomDataElements.cmbLookupWindow.setEnabled(enabled);
            if(!enabled) {
                addModifyCustomDataElements.cmbLookupArgument.setSelectedItem(EMPTY_STRING);
                addModifyCustomDataElements.cmbLookupWindow.setSelectedItem(EMPTY_STRING);
            }
            if(comboItemChanged && customElementsInfoBean.isHasLookUp() != addModifyCustomDataElements.chkLookUp.isSelected()) {
                modified = true;
            }
        }else if(source.equals(addModifyCustomDataElements.cmbLookupWindow)) {
            CoeusVector filteredData = null;
            Equals args = null;
            addModifyCustomDataElements.cmbLookupArgument.addItem(new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
            addModifyCustomDataElements.cmbLookupArgument.removeAllItems();
            if(addModifyCustomDataElements.cmbLookupWindow.getSelectedItem().toString().equals("w_arg_code_tbl")) {
                args = new Equals("code", CODE_TABLE);
            }else if(addModifyCustomDataElements.cmbLookupWindow.getSelectedItem().toString().equals("w_arg_value_list")) {
                args = new Equals("code", ARG_TABLE);
            }else args = new Equals("code", OTHER);
            
            filteredData = cvLookupArgs.filter(args);
            if(filteredData != null && filteredData.size()>0) {
                filteredData.add(0, new ComboBoxBean(EMPTY_STRING,EMPTY_STRING));
                for(int indx=0; indx<filteredData.size(); indx++) {
                    addModifyCustomDataElements.cmbLookupArgument.addItem(filteredData.get(indx));
                }
            }
            //customElementsInfoBean.setLookUpWindow("");
            if(comboItemChanged && !addModifyCustomDataElements.cmbLookupWindow.getSelectedItem().toString().equals(customElementsInfoBean.getLookUpWindow())) {
                modified = true;
            }
        }else if(source.equals(addModifyCustomDataElements.cmbDataType)) {
            if(comboItemChanged && !addModifyCustomDataElements.cmbDataType.getSelectedItem().toString().equals(customElementsInfoBean.getDataType())) {
                modified = true;
            }
        }else if(source.equals(addModifyCustomDataElements.cmbLookupWindow)) {
            if(comboItemChanged && !addModifyCustomDataElements.cmbLookupArgument.getSelectedItem().toString().equals(customElementsInfoBean.getLookUpArgument())) {
                modified = true;
            }
        }
        
    }
    
    private void performAddAction() {
        columnUsageEditor.stopCellEditing();
        int selRow = addModifyCustomDataElements.tblModules.getSelectedRow();
        if(selRow != -1) {
            CustomElementsUsageBean customElementsUsageBean = (CustomElementsUsageBean)cvModulesNotInUsage.get(selRow);
            cvModulesNotInUsage.remove(selRow);
            moduleModel.fireTableRowsDeleted(selRow, selRow);
            customElementsUsageBean.setIsRequired(false);
            if(customElementsUsageBean.getAcType() == null || customElementsUsageBean.getAcType() == TypeConstants.INSERT_RECORD) {
                customElementsUsageBean.setAcType(TypeConstants.INSERT_RECORD);
                customElementsUsageBean.setColumnName(addModifyCustomDataElements.txtColumnName.getText());
            }else 
                customElementsUsageBean.setAcType(TypeConstants.UPDATE_RECORD);
            if(cvColumnUsage == null) {
                cvColumnUsage = new CoeusVector();
            }
            cvColumnUsage.addElement(customElementsUsageBean);
            columnUsageModel.fireTableRowsInserted(cvColumnUsage.size(), cvColumnUsage.size());
            addModifyCustomDataElements.tblModules.requestFocusInWindow();
            if(cvModulesNotInUsage != null && cvModulesNotInUsage.size()>0)
                addModifyCustomDataElements.tblModules.setRowSelectionInterval(0,0);
        }
        modified = true;
    }
    
    private void performRemoveAction() {
        columnUsageEditor.stopCellEditing();
        int selRow = addModifyCustomDataElements.tblColumnUsage.getSelectedRow();
        if(selRow != -1) {
            CustomElementsUsageBean usageBean = (CustomElementsUsageBean)cvColumnUsage.get(selRow);
            /*if(usageBean.getAcType() == null) {
                usageBean.setAcType(TypeConstants.DELETE_RECORD);
            }*/if(usageBean.getAcType() != null && 
                    usageBean.getAcType() == TypeConstants.INSERT_RECORD) {
                usageBean.setAcType(null);
            }else 
                usageBean.setAcType(TypeConstants.DELETE_RECORD); //for Bug fix 1432 7-feb-2005
            cvModulesNotInUsage.add(usageBean);
            cvColumnUsage.remove(selRow);
            columnUsageModel.fireTableRowsDeleted(selRow, selRow);
            if(cvColumnUsage != null && cvColumnUsage.size()>0)
                addModifyCustomDataElements.tblColumnUsage.setRowSelectionInterval(0,0);
            moduleModel.fireTableRowsInserted(moduleModel.getRowCount()+1, moduleModel.getRowCount()+1);
            addModifyCustomDataElements.tblModules.setRowSelectionInterval(0,0);
        }
        modified = true;
    }
    
    private void performOkAction() {
        if(!validate()) return;
        isModified();
        customElementsDlgWindow.dispose();
    }
    
    private boolean performCancelAction() {
        if(isModified()) {
            int selOption = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("customElementExceptionCode.1505"), 
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selOption == CoeusOptionPane.SELECTION_YES) {
                if(!validate()) return false;
                return true;
            }
            else if(selOption == CoeusOptionPane.SELECTION_NO){
                htcustomElementData = null;
                return true;
            }
            else {
                customElementsDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return false;
            }
        }
        return true;
    }
    
    public Hashtable showCustomElementData() {
        display();
        return htcustomElementData;
    }
    
    private boolean isModified() {
        CustomElementsInfoBean newBean = new CustomElementsInfoBean();
        if (getFunctionType() != 'N') {
            newBean.setDescription(customElementsInfoBean.getDescription());
            newBean.setRequired(customElementsInfoBean.isRequired());
            newBean.setUpdateTimestamp(customElementsInfoBean.getUpdateTimestamp());
            newBean.setUpdateUser(customElementsInfoBean.getUpdateUser());
            newBean.setColumnValue(customElementsInfoBean.getColumnValue());
            newBean.setLookUpReuired(customElementsInfoBean.getLookUpReuired());
        }
        
        newBean.setColumnLabel(addModifyCustomDataElements.txtColumnLabel.getText());
        newBean.setColumnName(addModifyCustomDataElements.txtColumnName.getText());
        newBean.setDataType(((ComboBoxBean)addModifyCustomDataElements.cmbDataType.getSelectedItem()).getCode());
        // Enhancement to Add Group Name start
        newBean.setGroupCode(addModifyCustomDataElements.txtGroupData.getText().trim());
        // Enhancement to Add Group Name End        
        newBean.setDataLength(EMPTY_STRING.equals(addModifyCustomDataElements.txtDataLength.getText())?0:Integer.parseInt(addModifyCustomDataElements.txtDataLength.getText()));
        newBean.setDefaultValue(addModifyCustomDataElements.txtDefaultValue.getText().trim());
        newBean.setHasLookUp(addModifyCustomDataElements.chkLookUp.isSelected());
        newBean.setLookUpArgument(addModifyCustomDataElements.cmbLookupArgument.getSelectedItem().toString());
        newBean.setLookUpWindow(addModifyCustomDataElements.cmbLookupWindow.getSelectedItem().toString());
        newBean.setAcType(customElementsInfoBean.getAcType());
        StrictEquals saveRequired = new StrictEquals();
        if(!saveRequired.compare(newBean,customElementsInfoBean)) {
            if(customElementsInfoBean.getAcType()== null){
                if(getFunctionType() != 'N'  ) {
                    newBean.setAcType(TypeConstants.UPDATE_RECORD);
                }else {
                    newBean.setAcType(TypeConstants.INSERT_RECORD);
                }
            }
            modified = true;
        }
        
        saveFormData();
        htcustomElementData.put(CustomElementsInfoBean.class, newBean);
        htcustomElementData.put(CustomElementsUsageBean.class, cvColumnUsage==  null ? new CoeusVector() : cvColumnUsage);
        if (getFunctionType() == 'N')
            modified = true;
        return modified;
    }
    
    public class ColumnUsageModel extends AbstractTableModel {
        private String[] columnNames = {"Module","Required"};
        private Class[] columnClass = {String.class,Boolean.class};
        
        public ColumnUsageModel() {
            
        }
        
              
        public Class getColumnClass(int col){
            return columnClass[col];
        }
        
        
        /**
         *This method is to get the column count
         *@return int
         */
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /**
         *This method is to get the row count
         *@return int
         */
        public int getRowCount() {
            if(cvColumnUsage == null ){
                return 0;
            }else{
                return cvColumnUsage.size();
            }
        }
        
        /**
         *This method is to get the value with respect to the row and column
         *@param int row
         *@param int col
         *@return Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            customElementsUsageBean = (CustomElementsUsageBean)cvColumnUsage.get(rowIndex);
            switch(columnIndex) {
                case MODULE_COLUMN:
                    return customElementsUsageBean.getDescription();
                case REQUIRED_COLUMN:
                    return new Boolean(customElementsUsageBean.isIsRequired());
            }
            return EMPTY_STRING;
        }
        
        /**
         *This method is to set the value with respect to the row and column
         *@param Object value
         *@param int row
         *@param int col
         *@return void
         */
        public void setValueAt(Object value, int row, int col) {
            customElementsUsageBean = (CustomElementsUsageBean)cvColumnUsage.get(row);
            if(col == REQUIRED_COLUMN) {
                if(value != null && ((Boolean)value).booleanValue() != customElementsUsageBean.isIsRequired()) {
                    Boolean required = (Boolean)value;
                    modified = true;
                    customElementsUsageBean.setIsRequired(required.booleanValue());
                    customElementsUsageBean.setRequired(required.booleanValue());
                    if(customElementsUsageBean.getAcType() == null) {
                        customElementsUsageBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
            }
        }
        
        /**
         *This method will specifies the data for the table model. Depending upon the value
         *passed, it will hold the Award or Institute Proposal Detail vestor
         *@param CoeusVector cvData
         *@return void
         */
        public void setData(CoeusVector cvData){
            cvColumnUsage = cvData;
            fireTableDataChanged();
        }
        
        /**
         *This method is to check whether the specified cell is editable or not
         *@param int row
         *@param int col
         *@return boolean
         */
        public boolean isCellEditable(int row, int col) {
          return col==MODULE_COLUMN ? false : true;
        }
    
        /**
         *This method is to get the column name
         *@param int column
         *@return String
         */
        public String getColumnName(int column) {
            return columnNames[column];
        }
    
    }
    
    public class ColumnUsageRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
            JLabel lblModule;
            JCheckBox chkIsRequired;
        java.awt.Color backgroundColor = (java.awt.Color) javax.swing.UIManager.
            getDefaults().get("Panel.background");
        public ColumnUsageRenderer() {
            lblModule = new JLabel();
            chkIsRequired = new JCheckBox();
        }
        
        /**
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param boolean hasFocus
         * @param boolean hasFocus
         * @param int row
         * @param int col
         * @return Component
         */
        public Component getTableCellRendererComponent(JTable table,Object value,
        boolean isSelected,boolean hasFocus ,int row, int col) {
            switch(col) {
                case MODULE_COLUMN:
                    lblModule.setText((String)value);
                    return lblModule;
                case REQUIRED_COLUMN:
                   chkIsRequired.setSelected(((Boolean)value).booleanValue());
                   if(isSelected)
                       chkIsRequired.setBackground(table.getSelectionBackground());
                   if(!isSelected)
                       chkIsRequired.setBackground(backgroundColor);
                   return chkIsRequired;
            }
            return lblModule;
        }
    }
    
    public class ColumnUsageEditor extends AbstractCellEditor implements TableCellEditor ,ItemListener{
        JLabel lblModule;
        JCheckBox chkRequired;
        int column;
        public ColumnUsageEditor() {
            lblModule = new JLabel();
            chkRequired = new JCheckBox();
            chkRequired.addItemListener(this);
        }
        /**
         *This method is to get the table cell editor component
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param int row
         * @param int column
         * @return Component
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column) {
                case REQUIRED_COLUMN:
                    chkRequired.setSelected(((Boolean)value).booleanValue());
                    return chkRequired;
        }
            return chkRequired;
        }
        
         public Object getCellEditorValue() {
            switch(column) {
                case MODULE_COLUMN:
                    return lblModule.getText();
                case REQUIRED_COLUMN:
                    return new Boolean(chkRequired.isSelected());
            }
            return chkRequired;
        }
         
         public void itemStateChanged(java.awt.event.ItemEvent e) {
             this.stopCellEditing();
         }
         
    }
    
    public class ModuleModel extends AbstractTableModel {
        String  colNames[] = {"Module Name"};
        Class colClass[] = {String.class};
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            if(cvModulesNotInUsage != null && cvModulesNotInUsage.size()>0) return cvModulesNotInUsage.size();
            return 0;
        }
        
        /**
         *This method is to get the value with respect to the row and column
         *@param int row
         *@param int col
         *@return Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            CustomElementsUsageBean customElementsUsageBean = (CustomElementsUsageBean)cvModulesNotInUsage.get(rowIndex);
                   return customElementsUsageBean.getDescription();
        }
        
        /**
         *This method will specifies the data for the table model. Depending upon the value
         *passed, it will hold the Award or Institute Proposal Detail vestor
         *@param CoeusVector cvData
         *@return void
         */
        public void setData(CoeusVector cvData){
            cvModulesNotInUsage = cvData;
        }
        
        /**
         *This method is to check whether the specified cell is editable or not
         *@param int row
         *@param int col
         *@return boolean
         */
        public boolean isCellEditable(int row, int col) {
          return false;
        }
    
        /**
         *This method is to get the column name
         *@param int column
         *@return String
         */
        public String getColumnName(int column) {
            return colNames[column];
        }
    }
    
    private void setRequestFocusInThread() {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                if(getFunctionType() != 'N')
                    addModifyCustomDataElements.txtColumnLabel.requestFocusInWindow();
                else
                    addModifyCustomDataElements.txtColumnName.requestFocusInWindow();
            }
        });
    }
    
    public void cleanUp() {
        cvColumnUsage = null;
        cvLookupArgs = null;
        cvLookupWindow = null;
        cvModulesNotInUsage = null;
        columnUsageEditor = null;
        columnUsageModel = null;
        columnUsageRenderer = null;
        moduleModel = null;
        customElementsInfoBean = null;
        customElementsUsageBean = null;
        htcustomElementData = null;
    }
}    