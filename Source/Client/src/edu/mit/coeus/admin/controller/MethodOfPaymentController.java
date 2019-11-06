/*
 * MethodOfPaymentController.java
 *
 * Created on November 25, 2004, 2:37 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.MethodOfPaymentForm;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.query.QueryEngine;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.Equals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Hashtable;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import java.awt.event.*;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

/**
 *
 * @author  jinum
 */
public class MethodOfPaymentController extends AdminController implements ActionListener, ItemListener, ListSelectionListener  {
    
    private MethodOfPaymentForm methodOfPaymentForm;
    public CoeusDlgWindow dlgMethodOfPayment;
    private CoeusAppletMDIForm mdiForm;
    private MethodOfPaymentTableModel methodOfPaymentTableModel;
    
    private CoeusMessageResources  coeusMessageResources;
    private ValidBasisMethodPaymentBean validBasisPaymentBean;
    
    private static final int WIDTH = 580;
    private static final int HEIGHT = 350;
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private final int INSTRUCTION_BLANK = 1;
    private final int INSTRUCTION_MANDATORY = 2;
    private final int INSTRUCTION_OPTIONAL = 3;
    private final int PI_FREQUENCY_BLANK = 4;
    private final int PI_FREQUENCY_MANDATORY = 5;
    private final int PI_FREQUENCY_OPTIONAL = 6;
    private final String BLANK = "B";
    private final String MANDATORY = "M";
    private final String OPTIONAL = "O";
    private static final String TITLE = "Valid Basis and Method of Payment";
    private static final String EMPTY = "";
    private static final String ADMIN_MAINTENANCE_SERVLET = "/AdminMaintenanceServlet";
    private static final String SELECT_A_ROW="adminAward_exceptionCode.1351";
    private static final String DELETE_CONFIRMATION = "adminAward_exceptionCode.1352";
    private static final String CANCEL_CONFIRMATION = "adminAward_exceptionCode.1353";
    
    private CoeusVector cvMethodOfPaymentFinal;
    private CoeusVector cvMethodOfPayment;
    private CoeusVector cvAddMethodOfPayment;
    private CoeusVector cvFilteredMethodOfPayment;
    private CoeusVector cvFilteredAddMethodOfPayment;
    private CoeusVector cvSelectedData;
    private boolean hasRight = true;
    private boolean dataModified = false;
    private MultipleTableColumnSorter sorter;
    private Object data;
    
    /** Creates a new instance of MethodOfPaymentController */
    public MethodOfPaymentController(CoeusAppletMDIForm mdiForm) {
        try{
            this.mdiForm = mdiForm;
            coeusMessageResources = CoeusMessageResources.getInstance();
            registerComponents();
            setFormData(null);
            setColumnData();
            formatFields();
            postInitComponents();
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    /**
     * Registering the components
     * @return void
     **/
    public void registerComponents() {
        methodOfPaymentForm = new MethodOfPaymentForm();
        methodOfPaymentTableModel = new MethodOfPaymentTableModel();
        methodOfPaymentForm.btnOK.addActionListener(this);
        methodOfPaymentForm.btnCancel.addActionListener(this);
        methodOfPaymentForm.btnAdd.addActionListener(this);
        methodOfPaymentForm.btnDelete.addActionListener(this);
        methodOfPaymentForm.cmbBasisOfPayment.addItemListener(this);
        methodOfPaymentForm.tblMethodOfPayment.setModel(methodOfPaymentTableModel);
        methodOfPaymentForm.tblMethodOfPayment.getSelectionModel().addListSelectionListener(this);
        methodOfPaymentForm.rbInstrBlank.addActionListener(this);
        methodOfPaymentForm.rbInstrMandatory.addActionListener(this);
        methodOfPaymentForm.rbInstrOptional.addActionListener(this);
        methodOfPaymentForm.rbPIFrBlank.addActionListener(this);
        methodOfPaymentForm.rbPIFrMandatory.addActionListener(this);
        methodOfPaymentForm.rbPIFrOptional.addActionListener(this);
        
        java.awt.Component[] components = {methodOfPaymentForm.cmbBasisOfPayment,methodOfPaymentForm.btnOK,methodOfPaymentForm.btnCancel,methodOfPaymentForm.btnAdd,methodOfPaymentForm.btnDelete,methodOfPaymentForm.pnlPIFrequency,methodOfPaymentForm.rbPIFrOptional,methodOfPaymentForm.rbPIFrMandatory,methodOfPaymentForm.rbPIFrBlank,methodOfPaymentForm.rbInstrOptional,methodOfPaymentForm.rbInstrMandatory,methodOfPaymentForm.rbInstrBlank};
        ScreenFocusTraversalPolicy traversalPolicy = new ScreenFocusTraversalPolicy(components);
        methodOfPaymentForm.setFocusTraversalPolicy(traversalPolicy);
        methodOfPaymentForm.setFocusCycleRoot(true);
    }
    
    /**
     * Initializing the components
     * @return void
     **/
    private void postInitComponents() {
        dlgMethodOfPayment = new CoeusDlgWindow(mdiForm);
        dlgMethodOfPayment.getContentPane().add(methodOfPaymentForm);
        dlgMethodOfPayment.setResizable(false);
        dlgMethodOfPayment.setModal(true);
        dlgMethodOfPayment.setFont(CoeusFontFactory.getLabelFont());
        dlgMethodOfPayment.setSize(WIDTH, HEIGHT);
        Dimension dlgLocation = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgMethodOfPaymentSize = dlgMethodOfPayment.getSize();
        dlgMethodOfPayment.setLocation((dlgLocation.width/2 - dlgMethodOfPaymentSize.width/2), (dlgLocation.height/2 - dlgMethodOfPaymentSize.height/2));
        dlgMethodOfPayment.setTitle(TITLE);
        
        dlgMethodOfPayment.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performCancelAction();
                return;
            }
        });
        dlgMethodOfPayment.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgMethodOfPayment.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                performCancelAction();
                return;
            }
        });
        dlgMethodOfPayment.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
    }
    
    /**
     * Display the Dialog
     * @return void
     **/
    public void display() {
        dlgMethodOfPayment.setVisible(true);
    }
    
    /**
     * Format the Fields or Controls.
     * @return void
     **/
    public void formatFields() {
        if(!hasRight){
            methodOfPaymentForm.btnAdd.setEnabled(false);
            methodOfPaymentForm.btnDelete.setEnabled(false);
            methodOfPaymentForm.btnOK.setEnabled(false);
            methodOfPaymentForm.rbPIFrOptional.setEnabled(false);
            methodOfPaymentForm.rbPIFrMandatory.setEnabled(false);
            methodOfPaymentForm.rbPIFrBlank.setEnabled(false);
            methodOfPaymentForm.rbInstrOptional.setEnabled(false);
            methodOfPaymentForm.rbInstrMandatory.setEnabled(false);
            methodOfPaymentForm.rbInstrBlank.setEnabled(false);
            methodOfPaymentForm.tblMethodOfPayment.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
        }
    }
    
    /**
     * To get the controlled UI
     * @return java.awt.Component
     **/
    public java.awt.Component getControlledUI() {
        return methodOfPaymentForm;
    }
    
    /**
     * To set the form data
     * @param data Object
     * @return void
     **/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        
        HashMap htData  = getMethodOfPayment();
        cvMethodOfPaymentFinal = new CoeusVector();
        cvMethodOfPayment = new CoeusVector();
        cvAddMethodOfPayment = new CoeusVector();
        CoeusVector cvBasisOfPayment = new CoeusVector();
        if(htData != null){
            try{
                hasRight = ((Boolean)htData.get(new Integer(0))).booleanValue();
                cvBasisOfPayment = (CoeusVector)htData.get(new Integer(1));
                setComboData(cvBasisOfPayment);
                methodOfPaymentForm.cmbBasisOfPayment.setShowCode(true);
                cvMethodOfPayment = (CoeusVector)htData.get(new Integer(2));
                if(cvMethodOfPayment != null)
                    cvMethodOfPaymentFinal = (CoeusVector)ObjectCloner.deepCopy(cvMethodOfPayment);
                cvAddMethodOfPayment = (CoeusVector)htData.get(new Integer(3));
                performBasisOPChangeOperation(((ComboBoxBean)methodOfPaymentForm.cmbBasisOfPayment.getSelectedItem()).getCode());
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    /**
     * To set the ComboBox data
     * @return void
     **/
    private void setComboData(CoeusVector data){
        if( ( data != null ) && ( data.size() > 0 )
        && ( methodOfPaymentForm.cmbBasisOfPayment.getItemCount() == 0 ) ) {
            int size = data.size();
            for(int index = 0 ; index < size ; index++){
                methodOfPaymentForm.cmbBasisOfPayment.addItem(
                (ValidBasisMethodPaymentBean)data.elementAt(index));
            }
            validBasisPaymentBean= (ValidBasisMethodPaymentBean)data.elementAt(0);
            methodOfPaymentForm.cmbBasisOfPayment.setSelectedItem(validBasisPaymentBean);
        }
    }
    
    /**
     * To get the form data
     * @return Object
     **/
    public Object getFormData() {
        return null;
    }
    
    /**
     * save form data
     * @return void
     */
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        methodOfPaymentForm.cmbBasisOfPayment.requestFocus();
    }
    
    /** This method will specify the state change of an item
     * @param itemEvent ItemEvent
     * @return void
     */
    public void itemStateChanged(ItemEvent itemEvent){
        Object source = itemEvent.getSource();
        if (source != null && source.equals(methodOfPaymentForm.cmbBasisOfPayment)) {
            performBasisOPChangeOperation(((ComboBoxBean)methodOfPaymentForm.cmbBasisOfPayment.getSelectedItem()).getCode());
        }
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            if(source != null){
                if (source.equals(methodOfPaymentForm.btnOK)) {
                    dlgMethodOfPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    performOKOperation();
                }else if (source.equals(methodOfPaymentForm.btnCancel)) {
                    performCancelAction();
                }else if (source.equals(methodOfPaymentForm.btnAdd)) {
                    dlgMethodOfPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                    performAddRow();
                }else if (source.equals(methodOfPaymentForm.btnDelete)) {
                    performDeleteOperation();
                }else if (source.equals(methodOfPaymentForm.rbInstrBlank)) {
                    performUpdateBeanAction(INSTRUCTION_BLANK);
                }else if (source.equals(methodOfPaymentForm.rbInstrMandatory)) {
                    performUpdateBeanAction(INSTRUCTION_MANDATORY);
                }else if (source.equals(methodOfPaymentForm.rbInstrOptional)) {
                    performUpdateBeanAction(INSTRUCTION_OPTIONAL);
                }else if (source.equals(methodOfPaymentForm.rbPIFrBlank)) {
                    performUpdateBeanAction(PI_FREQUENCY_BLANK);
                }else if (source.equals(methodOfPaymentForm.rbPIFrMandatory)) {
                    performUpdateBeanAction(PI_FREQUENCY_MANDATORY);
                }else if (source.equals(methodOfPaymentForm.rbPIFrOptional)) {
                    performUpdateBeanAction(PI_FREQUENCY_OPTIONAL);
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }finally{
            dlgMethodOfPayment.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * Method which is called on ListSelectionChange
     * @param listSelectionEvent ListSelectionEvent
     * @return void
     **/
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        // If cell selection is enabled, both row and column change events are fired
        if (listSelectionEvent.getSource() == methodOfPaymentForm.tblMethodOfPayment.getSelectionModel()
        && methodOfPaymentForm.tblMethodOfPayment.getRowSelectionAllowed()) {
            int rowIndex = methodOfPaymentForm.tblMethodOfPayment.getSelectedRow();
            if(cvFilteredMethodOfPayment != null && cvFilteredMethodOfPayment.size()>0 && rowIndex >= 0){
                ValidBasisMethodPaymentBean bean = (ValidBasisMethodPaymentBean)cvFilteredMethodOfPayment.get(rowIndex);
                if(bean != null){
                    String freqValue = bean.getFrequencyIndicator();
                    setSelected("F", freqValue);
                    String instrValue  = bean.getInvInstructionsIndicator();
                    setSelected("I", instrValue);
                }
            }
        }
    }
    
    /**
     * Method to set the RadioButtons
     * @param item String
     * @param value String
     * @return void
     **/
    private void setSelected(String item, String value){
        if(value.trim().equals(BLANK)){
            if(item.equals("F")){
                methodOfPaymentForm.rbPIFrBlank.setSelected(true);
            }else if(item.equals("I")){
                methodOfPaymentForm.rbInstrBlank.setSelected(true);
            }
        }else if(value.trim().equals(MANDATORY)){
            if(item.equals("F")){
                methodOfPaymentForm.rbPIFrMandatory.setSelected(true);
            }else if(item.equals("I")){
                methodOfPaymentForm.rbInstrMandatory.setSelected(true);
            }
        }else{
            if(item.equals("F")){
                methodOfPaymentForm.rbPIFrOptional.setSelected(true);
            }else if(item.equals("I")){
                methodOfPaymentForm.rbInstrOptional.setSelected(true);
            }
        }
    }
    
    /**
     * Method called while Basis of Payment selection change
     * @param code String
     * @return void
     **/
    private void performBasisOPChangeOperation(String code){
        
        try{
            validBasisPaymentBean = (ValidBasisMethodPaymentBean)methodOfPaymentForm.cmbBasisOfPayment.getSelectedItem();
            Equals eqBasisOfPayment =  new Equals("basisOfPaymentCode" ,new Integer(code));
            cvFilteredMethodOfPayment = new CoeusVector();
            if(cvMethodOfPayment != null){
                cvFilteredMethodOfPayment = cvMethodOfPayment.filter(eqBasisOfPayment);
                methodOfPaymentTableModel.setData(cvFilteredMethodOfPayment);
                if (cvFilteredMethodOfPayment != null && cvFilteredMethodOfPayment.size() > 0) {
                    methodOfPaymentForm.tblMethodOfPayment.setRowSelectionInterval(0,0);
                }
                filterMethodOfPayment();
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * Method to filter the Method of Payment
     * @return void
     **/
    private void filterMethodOfPayment(){
        cvFilteredAddMethodOfPayment = new CoeusVector();
        if(cvAddMethodOfPayment != null && cvFilteredMethodOfPayment != null){
            for(int i=0;i<cvAddMethodOfPayment.size();i++){
                int count=0;
                for(int j=0;j<cvFilteredMethodOfPayment.size();j++){
                    if(((ValidBasisMethodPaymentBean)cvFilteredMethodOfPayment.elementAt(j)).getCode().equals(((ValidBasisMethodPaymentBean)cvAddMethodOfPayment.elementAt(i)).getCode())){
                        break;
                    }else{
                        count++;
                    }
                }
                if(count>=cvFilteredMethodOfPayment.size()){
                    cvFilteredAddMethodOfPayment.add(cvAddMethodOfPayment.elementAt(i));
                }
            }
        }
    }
    
    /**
     * Method called on OK Button click
     * @return void
     **/
    private void performOKOperation() throws CoeusException{
        if(dataModified && cvMethodOfPaymentFinal != null){
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType('G');//"UPDATE_METHOD_OF_PAYMENT = 'G'"
            requesterBean.setDataObject(cvMethodOfPaymentFinal);
            AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
            appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ADMIN_MAINTENANCE_SERVLET);
            appletServletCommunicator.setRequest(requesterBean);
            appletServletCommunicator.send();
            ResponderBean responderBean = appletServletCommunicator.getResponse();
            if(responderBean!= null){
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage(), 1);
                }
            }
        }
        dlgMethodOfPayment.setVisible(false);
    }
    
    /**
     * Method called on Cancel Button click
     * @return void
     **/
    private void performCancelAction() {
        try{
            if(dataModified){
                String mesg = coeusMessageResources.parseMessageKey(CANCEL_CONFIRMATION);
                int selectedOption = CoeusOptionPane.showQuestionDialog(mesg+" ",
                CoeusOptionPane.OPTION_YES_NO_CANCEL,
                CoeusOptionPane.DEFAULT_YES);
                if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                    performOKOperation();
                } else if(selectedOption == CoeusOptionPane.OPTION_OK_CANCEL){
                    dlgMethodOfPayment.setVisible(false);
                } else{}
            }else{
                dlgMethodOfPayment.setVisible(false);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * Method called on Add Button click
     * @return void
     **/
    private void performAddRow() {
        try{
            if(validBasisPaymentBean != null && cvFilteredAddMethodOfPayment != null){
                
                AddMethodOfPaymentController addMethodOfPaymentController = new AddMethodOfPaymentController(mdiForm, validBasisPaymentBean);
                addMethodOfPaymentController.setFormData(cvFilteredAddMethodOfPayment);
                addMethodOfPaymentController.setColumnData();
                cvSelectedData = new CoeusVector();
                cvSelectedData = addMethodOfPaymentController.display();
                if(cvSelectedData != null && cvSelectedData.size()>0){
                    for(int i=0;i<cvSelectedData.size();i++){
                        ValidBasisMethodPaymentBean methodPaymentBean = (ValidBasisMethodPaymentBean)cvSelectedData.elementAt(i);
                        methodPaymentBean.setBasisOfPaymentCode(Integer.parseInt(validBasisPaymentBean.getCode()));
                        methodPaymentBean.setAcType(TypeConstants.INSERT_RECORD);
                        cvMethodOfPayment.add(methodPaymentBean);
                        ValidBasisMethodPaymentBean searchedBean = (ValidBasisMethodPaymentBean)search(methodPaymentBean,null);
                        if(searchedBean == null)
                            searchedBean = (ValidBasisMethodPaymentBean)search(methodPaymentBean,TypeConstants.DELETE_RECORD);
                        if(searchedBean != null){
                            searchedBean.setAcType(TypeConstants.DELETE_RECORD);
                            ValidBasisMethodPaymentBean insertBean = (ValidBasisMethodPaymentBean)ObjectCloner.deepCopy(methodPaymentBean);
                            cvMethodOfPaymentFinal.add(insertBean);
                        }else{
                            cvMethodOfPaymentFinal.add(methodPaymentBean);
                        }
                    }
                    performBasisOPChangeOperation(((ComboBoxBean)methodOfPaymentForm.cmbBasisOfPayment.getSelectedItem()).getCode());
                    dataModified = true;
                }
            }
        }catch(Exception exception){exception.printStackTrace();}
    }
    
    /**
     * Method called to update the selected Method of Payment Bean
     * @param button int
     * @return void
     **/
    private void performUpdateBeanAction(int button){
        try{
            int rowIndex = methodOfPaymentForm.tblMethodOfPayment.getSelectedRow();
            if(rowIndex >= 0){
                ValidBasisMethodPaymentBean bean = (ValidBasisMethodPaymentBean)cvFilteredMethodOfPayment.get(rowIndex);
                ValidBasisMethodPaymentBean finalBean = (ValidBasisMethodPaymentBean)search(bean,null);
                ValidBasisMethodPaymentBean modifyBean = null;
                if(finalBean != null){
                    finalBean.setAcType(TypeConstants.DELETE_RECORD);
                    modifyBean = (ValidBasisMethodPaymentBean)ObjectCloner.deepCopy(finalBean);
                    modifyBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvMethodOfPaymentFinal.add(modifyBean);
                }else{
                    finalBean = (ValidBasisMethodPaymentBean)search(bean,TypeConstants.INSERT_RECORD);
                    if(finalBean != null){
                        modifyBean = finalBean;
                    }
                }
                if(bean != null && modifyBean != null){
                    switch(button){
                        case INSTRUCTION_BLANK:
                            bean.setInvInstructionsIndicator(BLANK);
                            modifyBean.setInvInstructionsIndicator(BLANK);
                            break;
                        case INSTRUCTION_MANDATORY:
                            bean.setInvInstructionsIndicator(MANDATORY);
                            modifyBean.setInvInstructionsIndicator(MANDATORY);
                            break;
                        case INSTRUCTION_OPTIONAL:
                            bean.setInvInstructionsIndicator(OPTIONAL);
                            modifyBean.setInvInstructionsIndicator(OPTIONAL);
                            break;
                        case PI_FREQUENCY_BLANK:
                            bean.setFrequencyIndicator(BLANK);
                            modifyBean.setFrequencyIndicator(BLANK);
                            break;
                        case PI_FREQUENCY_MANDATORY:
                            bean.setFrequencyIndicator(MANDATORY);
                            modifyBean.setFrequencyIndicator(MANDATORY);
                            break;
                        case PI_FREQUENCY_OPTIONAL:
                            bean.setFrequencyIndicator(OPTIONAL);
                            modifyBean.setFrequencyIndicator(OPTIONAL);
                            break;
                        default:
                            
                    }
                    dataModified = true;
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }
    
    /**
     * Method called on Delete Button click
     * @return void
     **/
    private void performDeleteOperation() {
        int rowIndex = methodOfPaymentForm.tblMethodOfPayment.getSelectedRow();
        if (rowIndex < 0) {
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_A_ROW)+" ");
            return;
        }
        if (rowIndex >= 0) {
            String mesg = coeusMessageResources.parseMessageKey(DELETE_CONFIRMATION);
            int selectedOption = CoeusOptionPane.showQuestionDialog(mesg+" ",
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                
                ValidBasisMethodPaymentBean bean = (ValidBasisMethodPaymentBean)cvFilteredMethodOfPayment.get(rowIndex);
                if(bean != null){
                    searchAndDelete(bean);
                    methodOfPaymentTableModel.fireTableRowsDeleted(rowIndex,rowIndex);
                    performBasisOPChangeOperation(((ComboBoxBean)methodOfPaymentForm.cmbBasisOfPayment.getSelectedItem()).getCode());
                    dataModified = true;
                }
            }
        }
        
    }
    
    /**
     * Method to Search a Bean and delete it
     * @param object Object
     * @return void
     **/
    private void searchAndDelete(Object object){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean")){
            ValidBasisMethodPaymentBean bean = (ValidBasisMethodPaymentBean)object;
            ValidBasisMethodPaymentBean searchedBean = null;
            ValidBasisMethodPaymentBean searchedInsertBean = null;
            for(int i=0;i<cvMethodOfPayment.size();i++){
                if(((ValidBasisMethodPaymentBean)cvMethodOfPayment.elementAt(i)).getCode().equals(bean.getCode()) && ((ValidBasisMethodPaymentBean)cvMethodOfPayment.elementAt(i)).getBasisOfPaymentCode()==(bean.getBasisOfPaymentCode())){
                    searchedBean = (ValidBasisMethodPaymentBean)search(bean, null);
                    searchedInsertBean = (ValidBasisMethodPaymentBean)search(bean, TypeConstants.INSERT_RECORD);
                    if(searchedBean != null)
                        searchedBean.setAcType(TypeConstants.DELETE_RECORD);
                    if(searchedInsertBean != null)
                        cvMethodOfPaymentFinal.removeElementAt(searchIndex(bean,TypeConstants.INSERT_RECORD));
                    cvMethodOfPayment.removeElementAt(i);
                    break;
                }
            }
        }
    }
    
    /**
     * Method to search an object
     * @param object Object
     * @return Object
     **/
    private Object search(Object object, String acType){
        try{
            if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean")){
                ValidBasisMethodPaymentBean bean = null;
                for(int i=0;i<cvMethodOfPaymentFinal.size();i++){
                    bean = (ValidBasisMethodPaymentBean)cvMethodOfPaymentFinal.elementAt(i);
                    if(acType != null && bean.getAcType() != null && !acType.equals("") && bean.getCode().equals(((ValidBasisMethodPaymentBean)object).getCode()) && bean.getBasisOfPaymentCode()==(((ValidBasisMethodPaymentBean)object).getBasisOfPaymentCode()) && bean.getAcType().equals(acType)){
                        return bean;
                    }else if( acType == null && bean.getCode().equals(((ValidBasisMethodPaymentBean)object).getCode()) && bean.getBasisOfPaymentCode()==(((ValidBasisMethodPaymentBean)object).getBasisOfPaymentCode())&& bean.getAcType() == null){
                        return bean;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Method to search index of an object
     * @param object Object
     * @return int
     **/
    private int searchIndex(Object object, String acType){
        if(object != null && object.getClass().getName().equals("edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean")){
            ValidBasisMethodPaymentBean bean = null;
            for(int i=0;i<cvMethodOfPaymentFinal.size();i++){
                bean = (ValidBasisMethodPaymentBean)cvMethodOfPaymentFinal.elementAt(i);
                if(acType != null && bean.getAcType() != null && !acType.equals("") && bean.getCode().equals(((ValidBasisMethodPaymentBean)object).getCode()) && bean.getBasisOfPaymentCode()==(((ValidBasisMethodPaymentBean)object).getBasisOfPaymentCode()) && bean.getAcType().equals(acType)){
                    return i;
                }else if(acType == null && bean.getCode().equals(((ValidBasisMethodPaymentBean)object).getCode()) && bean.getBasisOfPaymentCode()==(((ValidBasisMethodPaymentBean)object).getBasisOfPaymentCode()) && bean.getAcType() == null){
                    return i;
                }
            }
        }
        return -1;
    }
    
    /**
     * Method to fetch the records from Server
     * @return HashMap
     **/
    private HashMap getMethodOfPayment()throws CoeusException{
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setFunctionType('E');//"GET_METHOD_OF_PAYMENT"
        HashMap data=null;
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(CoeusGuiConstants.CONNECTION_URL + ADMIN_MAINTENANCE_SERVLET);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean!= null){
            if(!responderBean.isSuccessfulResponse()){
                throw new CoeusException(responderBean.getMessage(), 1);
            }else{
                data = (HashMap)responderBean.getDataObject();
            }
        }
        return data;
    }
    
    /**
     * Setting up the column data
     * @return void
     **/
    private void setColumnData(){
        JTableHeader tableHeader = methodOfPaymentForm.tblMethodOfPayment.getTableHeader();
        
        methodOfPaymentForm.tblMethodOfPayment.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)methodOfPaymentForm.tblMethodOfPayment.getModel());
                sorter.setTableHeader(methodOfPaymentForm.tblMethodOfPayment.getTableHeader());
                methodOfPaymentForm.tblMethodOfPayment.setModel(sorter);
        }
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        //tableHeader.addMouseListener(new ColumnHeaderListener());
        // setting up the table columns
        TableColumn column = methodOfPaymentForm.tblMethodOfPayment.getColumnModel().getColumn(CODE_COLUMN);
//        column.setMinWidth(50);
        column.setPreferredWidth(50);
        methodOfPaymentForm.tblMethodOfPayment.setRowHeight(22);
        
        column = methodOfPaymentForm.tblMethodOfPayment.getColumnModel().getColumn(DESCRIPTION_COLUMN);
//        column.setMinWidth(410);
        column.setPreferredWidth(410);
        
        methodOfPaymentForm.tblMethodOfPayment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        if (cvFilteredMethodOfPayment != null && cvFilteredMethodOfPayment.size() > 0) {
            methodOfPaymentForm.tblMethodOfPayment.setRowSelectionInterval(0,0);
        }
        
    }
    
    /**
     * validate method
     * @return boolean
     * @throws edu.mit.coeus.exception.CoeusUIException
     **/
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","code" },
            {"1","description" }
            
        };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvFilteredMethodOfPayment != null && cvFilteredMethodOfPayment.size()>0 &&
                nameBeanId [vColIndex][1].length() >1){
                    ((CoeusVector)cvFilteredMethodOfPayment).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    methodOfPaymentTableModel.fireTableRowsUpdated(
                    0, methodOfPaymentTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    /**
     * This is an inner class represents the table model for the Method of Payment
     * screen table
     **/
    public class MethodOfPaymentTableModel extends AbstractTableModel {
        
        // represents the column names of the table
        private String colName[] = {"Code","Description"};
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To check whether the table cell is editable or not
         * @param row int
         * @param col int
         * @return boolean
         **/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * To get the column count of the table
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        /**
         * To get the row count of the table
         * @return int
         **/
        public int getRowCount() {
            if (cvFilteredMethodOfPayment == null){
                return 0;
            } else {
                return cvFilteredMethodOfPayment.size();
            }
        }
        
        /**
         * To set the data for the model.
         * @param cvFilterMethodOfPayment CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvFilterMethodOfPayment) {
            cvFilteredMethodOfPayment = cvFilterMethodOfPayment;
            fireTableDataChanged();
        }
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            ValidBasisMethodPaymentBean validBasisMethodPaymentBean =
            (ValidBasisMethodPaymentBean)cvFilteredMethodOfPayment.get(rowIndex);
            if (validBasisMethodPaymentBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return validBasisMethodPaymentBean.getCode();
                    case DESCRIPTION_COLUMN:
                        return validBasisMethodPaymentBean.getDescription();
                }
            }
            return EMPTY;
        }
        
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To get the column name
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return colName[col];
        }
    }
    
}
