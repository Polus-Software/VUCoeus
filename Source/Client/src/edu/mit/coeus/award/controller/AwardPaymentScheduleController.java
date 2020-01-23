/*
 * AwardPaymentScheduleController.java
 *
 * Created on May 20, 2004, 11:06 AM
 */

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.award.controller;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.text.*;

import edu.mit.coeus.award.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.award.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.DateUtils ;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.departmental.gui.*;
import edu.ucsd.coeus.personalization.controller.AbstractController;


/**
 *
 * @author  surekhan
 */

/**
 * Creates an instance of AwardPaymentScheduleController
 */
public class AwardPaymentScheduleController extends AwardController implements ActionListener {
    
    private AwardPaymentScheduleForm awardPaymentScheduleForm;
    private AwardPaymentScheduleBean awardPaymentScheduleBean;
    private CoeusMessageResources coeusMessageResources;
    private AwardDetailsBean awardDetailsBean;
    private CoeusDlgWindow dlgAwardPaymentSchedule;
    private CoeusAppletMDIForm mdiForm;
    private QueryEngine queryEngine;
    private PaymentScheduleTableModel paymentScheduleTableModel;
    private PaymentScheduleEditor paymentScheduleEditor;
    private PaymentScheduleRenderer paymentScheduleRenderer;
    private AmountTableModel amountTableModel;
    private AmountTableCellRenderer amountTableCellRenderer;
    private static final int WIDTH = 680;
    private static final int HEIGHT = 370;
    private static final String WINDOW_TITLE = "Payment Schedule";
    private CoeusVector cvPayment;
    private CoeusVector cvDeletedItem;
    private static final int DUE_DATE = 0;
    private static final int AMOUNT = 1;
    private static final int SUBMIT_DATE = 2;
    private static final int SUBMIT_BY = 3;
    private static final int INV_NO = 4;
    private static final int STATUS_DESCRIPTION = 5;
    private static final int TOTAL_COLUMN = 0;
    private static final int TOTAL_AMOUNT_COLUMN = 1;
    private static final String EMPTY_STRING = "";
    private boolean modified = false;
    private boolean showWindow;
    private int rowId;
    private CoeusUtils coeusUtils = CoeusUtils.getInstance();
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.
    getDefaults().get("Panel.background");
    /** Are you sure you want to delete this row?
     */
    private static final String DELETE_CONFIRMATION = "award_exceptionCode.1009";
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    /** Please enter a valid due date */
    private static final String INVALID_DUE_DATE ="awardPaymentschedule_exceptionCode.1651";
    
    /** Please enter a valid submit date*/
    private static final String INVALID_SUBMIT_DATE = "awardPaymentschedule_exceptionCode.1652";
    
    /** please enter the due date*/
    private static final String DATE_MSG = "awardPaymentschedule_exceptionCode.1653";
    
    /** This is an invalid name*/
    private static final String INVALID_NAME = "award_exceptionCode.1010";
    private static final String DATE_FORMAT = "dd-MMM-yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private DateUtils dateUtils;
    private DateUtils dtUtils = new DateUtils();
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    // private String mesg;
    private CoeusVector cvData;
    
    //Bug Fix: Pass the person id to get the person details Start 1
    private boolean searchClicked = false;
    //Bug Fix: Pass the person id to get the person details End 1
    
    /** Creates a new instance of AwardPaymentScheduleController */
    public AwardPaymentScheduleController(AwardBaseBean awardBaseBean,
    CoeusAppletMDIForm mdiForm, char functionType) {
        super(awardBaseBean);
        this.mdiForm = mdiForm;
        dateUtils = new DateUtils();
        cvData = new CoeusVector();
        queryEngine = QueryEngine.getInstance();
        awardDetailsBean = new AwardDetailsBean();
        postInitComponents();
        paymentScheduleTableModel = new PaymentScheduleTableModel();
        paymentScheduleEditor = new PaymentScheduleEditor();
        paymentScheduleRenderer = new PaymentScheduleRenderer();
        amountTableCellRenderer = new AmountTableCellRenderer();
        cvPayment = new CoeusVector();
        cvDeletedItem = new CoeusVector();
        setFunctionType(functionType);
        registerComponents();
        setTableEditors();
        setFormData(null);
        
    }
    
    private void postInitComponents(){
        awardPaymentScheduleForm = new AwardPaymentScheduleForm();
        dlgAwardPaymentSchedule = new CoeusDlgWindow(mdiForm);
        dlgAwardPaymentSchedule.setResizable(false);
        dlgAwardPaymentSchedule.setModal(true);
        dlgAwardPaymentSchedule.getContentPane().add(awardPaymentScheduleForm);
        dlgAwardPaymentSchedule.setFont(CoeusFontFactory.getLabelFont());
        dlgAwardPaymentSchedule.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAwardPaymentSchedule.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAwardPaymentSchedule.getSize();
        dlgAwardPaymentSchedule.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        try{
            cvData = queryEngine.executeQuery(queryKey,
            AwardDetailsBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            awardDetailsBean = (AwardDetailsBean)cvData.get(0);
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        
        dlgAwardPaymentSchedule.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        
        
        dlgAwardPaymentSchedule.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                
                performCancelAction();
                
            }
        });
        //code for disposing the window ends
        
        dlgAwardPaymentSchedule.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
        
        
    }
    
    
    
    /** To set the default focus for the component
     */
    private void requestDefaultFocus(){
        if( getFunctionType() == TypeConstants.DISPLAY_MODE ){
            awardPaymentScheduleForm.btnCancel.requestFocus();
        }else{
            awardPaymentScheduleForm.btnOK.requestFocusInWindow();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if(source.equals(awardPaymentScheduleForm.btnAdd)){
            paymentScheduleEditor.stopCellEditing();
            performAddAction();
        }else if(source.equals(awardPaymentScheduleForm.btnDelete)){
            paymentScheduleEditor.stopCellEditing();
            performDeleteAction();
        }else if(source.equals(awardPaymentScheduleForm.btnCancel)){
            paymentScheduleEditor.stopCellEditing();
            performCancelAction();
        }else if(source.equals(awardPaymentScheduleForm.btnSearch)){
            performFindPerson();
        }else if(source.equals(awardPaymentScheduleForm.btnOK)){
            try{
                paymentScheduleEditor.stopCellEditing();
                if( !showWindow ){
                    if( validate() ){
                        saveFormData();
                    }
                }else{
                    showWindow = false;
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    
    private void close(){
        dlgAwardPaymentSchedule.dispose();
    }
    
    /* adds a new row to the table when the add button is clicked*/
    private void performAddAction(){
        //double cost = 0.0;
        if(cvPayment != null && cvPayment.size() > 0){
            paymentScheduleEditor.stopCellEditing();
        }
        
        
        AwardPaymentScheduleBean newBean = new AwardPaymentScheduleBean();
        
        newBean.setMitAwardNumber(awardBaseBean.getMitAwardNumber());
        newBean.setSequenceNumber(awardBaseBean.getSequenceNumber());
        newBean.setAmount(0.00);
        newBean.setSubmitBy(EMPTY_STRING);
        newBean.setInvoiceNumber(EMPTY_STRING);
        newBean.setStatusDescription(EMPTY_STRING);
        newBean.setAcType(TypeConstants.INSERT_RECORD);
        modified = true;
        cvPayment.add(newBean);
        paymentScheduleTableModel.fireTableRowsInserted(paymentScheduleTableModel.getRowCount(),
        paymentScheduleTableModel.getRowCount());
        
        int lastRow = awardPaymentScheduleForm.tblPayment.getRowCount()-1;
        if(lastRow >= 0){
            awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(lastRow,lastRow);
            awardPaymentScheduleForm.tblPayment.setColumnSelectionInterval(0,0);
            awardPaymentScheduleForm.tblPayment.editCellAt(lastRow,DUE_DATE);
            awardPaymentScheduleForm.tblPayment.getEditorComponent().requestFocusInWindow();
            awardPaymentScheduleForm.tblPayment.scrollRectToVisible(
            awardPaymentScheduleForm.tblPayment.getCellRect(lastRow, DUE_DATE, true));
            
        }
        awardPaymentScheduleForm.tblPayment.editCellAt(lastRow,DUE_DATE);
        
        if(awardPaymentScheduleForm.tblPayment.getRowCount() == 0){
            awardPaymentScheduleForm.tblAmount.setVisible(false);
        }else{
            awardPaymentScheduleForm.tblAmount.setVisible(true);
        }
        
    }
    
    /* deletes the selected row on the click of the delete button*/
    private void performDeleteAction(){
        paymentScheduleEditor.stopCellEditing();
        int selectedRow = awardPaymentScheduleForm.tblPayment.getSelectedRow();
        if(selectedRow == -1){
            return;
        }
        if(selectedRow != -1 && selectedRow >= 0){
            String mesg = DELETE_CONFIRMATION;
            int selectedOption = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(mesg),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(selectedOption == CoeusOptionPane.SELECTION_YES) {
                AwardPaymentScheduleBean deletedPaymentBean = (AwardPaymentScheduleBean)cvPayment.get(selectedRow);
                cvDeletedItem.add(deletedPaymentBean);
                if (deletedPaymentBean.getAcType() == null ||
                deletedPaymentBean.getAcType().equals(TypeConstants.UPDATE_RECORD)) {
                    cvDeletedItem.add(deletedPaymentBean);
                }
                if(cvPayment!=null && cvPayment.size() > 0){
                    cvPayment.remove(selectedRow);
                    paymentScheduleTableModel.fireTableRowsDeleted(selectedRow, selectedRow);
                    modified = true;
                    deletedPaymentBean.setAcType(TypeConstants.DELETE_RECORD);
                    
                }
                if(selectedRow >0){
                    awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(
                    selectedRow-1,selectedRow-1);
                    awardPaymentScheduleForm.tblPayment.scrollRectToVisible(
                    awardPaymentScheduleForm.tblPayment.getCellRect(
                    selectedRow -1 ,0, true));
                }else{
                    if(awardPaymentScheduleForm.tblPayment.getRowCount()>0){
                        awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(0,0);
                    }
                }
                if(awardPaymentScheduleForm.tblPayment.getRowCount() == 0){
                    awardPaymentScheduleForm.tblAmount.setVisible(false);
                }
            }
        }
    }
    
    /*The window disposes if nothing is modified else if modified saves and then disposes*/
    private void performCancelAction(){
        paymentScheduleEditor.stopCellEditing();
        
        if(modified){
            
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            switch( option ) {
                case (JOptionPane.YES_OPTION ):
                    setSaveRequired(true);
                    try{
                        if( validate() ){
                            saveFormData();
                        }
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                    break;
                case(JOptionPane.NO_OPTION ):
                    close();
                    break;
                default:
                    break;
            }
        }else{
            close();
        }
        
        
    }
    
    private int showDeleteConfirmMessage(String msg){
        int selectedOption = CoeusOptionPane.showQuestionDialog(msg,
        CoeusOptionPane.OPTION_YES_NO,
        CoeusOptionPane.DEFAULT_YES);
        return selectedOption;
    }
    
    /* the search screen is displayed on the click of the search button*/
    private void performFindPerson(){
        
        paymentScheduleEditor.stopCellEditing();
        int selRow = awardPaymentScheduleForm.tblPayment.getSelectedRow();
        if( selRow == -1 ) return ;
        AwardPaymentScheduleBean paymentBean = (AwardPaymentScheduleBean)cvPayment.get(selRow);
        try{
            CoeusSearch personSearch = null;
            dlgAwardPaymentSchedule.setCursor( new Cursor(Cursor.WAIT_CURSOR));
            personSearch = new CoeusSearch(CoeusGuiConstants.getMDIForm(), "PERSONSEARCH",1);
            personSearch.showSearchWindow();
            HashMap personData = personSearch.getSelectedRow();
            
            if( personData != null){
                //Bug Fix: Pass the person id to get the person details Start 2
                searchClicked = true;
                //Bug Fix: Pass the person id to get the person details End 2
                
                String personName = checkForNull(personData.get( "FULL_NAME" ));
                paymentBean.setSubmitBy((String)personData.get("PERSON_ID"));
                paymentScheduleTableModel.setValueAt(personName, selRow, SUBMIT_BY);
                
                awardPaymentScheduleForm.tblPayment.editCellAt(cvPayment.size() - 1,SUBMIT_BY);
                setTableEditing(cvPayment.size() - 1, SUBMIT_BY);
                awardPaymentScheduleForm.tblPayment.getEditorComponent().requestFocusInWindow();
                
            }
        }catch( Exception err ){
            err.printStackTrace();
        }
        finally {
            dlgAwardPaymentSchedule.setCursor( new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    private String checkForNull( Object value ){
        return (value==null)? "":value.toString();
    }
    
    
    public void display() {
    	//    	rdias - UCSD's coeus personalization - Begin
    	AbstractController persnref = AbstractController.getPersonalizationControllerRef();
        persnref.customize_Form(getControlledUI(),"GENERIC");
        persnref.customize_Form(awardPaymentScheduleForm.awardHeaderForm1,"GENERIC");
        //		rdias - UCSD's coeus personalization - End    	
        int rowCount = awardPaymentScheduleForm.tblPayment.getRowCount();
        if(cvPayment.size() > 0){
            awardPaymentScheduleForm.tblPayment.editCellAt(0,0);
        }
        if(rowCount == 0){
            awardPaymentScheduleForm.tblAmount.setVisible(false);
        }
        
        dlgAwardPaymentSchedule.show();
    }
    
    /**
     * Registers listener and other components
     */
    public void registerComponents(){
        coeusMessageResources = CoeusMessageResources.getInstance();
        java.awt.Component[] components = {awardPaymentScheduleForm.btnOK,
        awardPaymentScheduleForm.btnCancel,
        awardPaymentScheduleForm.btnAdd,
        awardPaymentScheduleForm.btnDelete,
        awardPaymentScheduleForm.btnSearch};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        awardPaymentScheduleForm.setFocusTraversalPolicy(traversePolicy);
        awardPaymentScheduleForm.setFocusCycleRoot(true);
        
        awardPaymentScheduleForm.btnAdd.addActionListener(this);
        awardPaymentScheduleForm.btnCancel.addActionListener(this);
        awardPaymentScheduleForm.btnDelete.addActionListener(this);
        awardPaymentScheduleForm.btnOK.addActionListener(this);
        awardPaymentScheduleForm.btnSearch.addActionListener(this);
        
        paymentScheduleTableModel = new PaymentScheduleTableModel();
        awardPaymentScheduleForm.tblPayment.setModel(paymentScheduleTableModel);
        paymentScheduleRenderer = new PaymentScheduleRenderer();
        
        amountTableCellRenderer = new AmountTableCellRenderer();
        amountTableModel = new AmountTableModel();
        awardPaymentScheduleForm.tblAmount.setModel(amountTableModel);
        awardPaymentScheduleForm.tblAmount.setShowHorizontalLines(false);
        awardPaymentScheduleForm.tblAmount.setShowVerticalLines(false);
        
    }
    
    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            awardPaymentScheduleForm.btnAdd.setEnabled(false);
            awardPaymentScheduleForm.btnCancel.setEnabled(true);
            awardPaymentScheduleForm.btnDelete.setEnabled(false);
            awardPaymentScheduleForm.btnOK.setEnabled(false);
            awardPaymentScheduleForm.btnSearch.setEnabled(false);
            awardPaymentScheduleForm.tblAmount.setEnabled(false);
            awardPaymentScheduleForm.tblPayment.setEnabled(false);
        }
        
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            
            java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            awardPaymentScheduleForm.tblAmount.setBackground(bgColor);
            awardPaymentScheduleForm.tblAmount.setSelectionBackground(bgColor);
            
            awardPaymentScheduleForm.tblPayment.setBackground(bgColor);
            awardPaymentScheduleForm.tblPayment.setSelectionBackground(bgColor);
            
        }
    }
    
    public Component getControlledUI() {
        return awardPaymentScheduleForm;
    }
    
    /* to get the form data*/
    public Object getFormData() {
        return awardPaymentScheduleForm;
    }
    
    /* saves the form data*/
    public void saveFormData() {
        
        try{
            paymentScheduleEditor.stopCellEditing();
           
            //Delete all the deleted beans from the query engine
            for( int index = 0; index < cvDeletedItem.size(); index++ ){
                AwardPaymentScheduleBean bean = (AwardPaymentScheduleBean)cvDeletedItem.get(index);
                queryEngine.delete(queryKey, bean);
            }
            
            for( int index = 0; index < cvPayment.size(); index++ ){
                AwardPaymentScheduleBean bean = (AwardPaymentScheduleBean)cvPayment.get(index);
                if( bean.getAcType() != null ){
                    if( bean.getAcType().equals(TypeConstants.UPDATE_RECORD) ){
                        //Delete the existing bean in the query engine
                        //got from the database and insert it with new rowId
                        AwardPaymentScheduleBean existingBean = bean;
                        queryEngine.delete(queryKey, bean);
                        existingBean.setAcType(TypeConstants.INSERT_RECORD);
                        existingBean.setRowId(getExistingMaxId() + index + 1);
                        queryEngine.insert(queryKey, existingBean);
                    }else if( bean.getAcType().equals(TypeConstants.INSERT_RECORD) ){
                        //Set the rowId of the bean if not already set
                        if( bean.getRowId() == 0 ){
                            bean.setRowId(getExistingMaxId() + index + 1);
                            
                        }
                        queryEngine.insert(queryKey, bean);
                    }
                }
            }
         
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        
        close();
      
    }
    /**
     * To make the class variable instances to null
     */
    public void cleanUp() {
        awardPaymentScheduleForm = null;
        awardPaymentScheduleBean = null;
        awardDetailsBean = null;
        dlgAwardPaymentSchedule = null;
        paymentScheduleTableModel = null;
        paymentScheduleEditor = null;
        paymentScheduleRenderer = null;
        amountTableModel = null;
        amountTableCellRenderer = null;
        cvPayment = null;
        cvDeletedItem = null;
    }
    
    private int getExistingMaxId() {
        CoeusVector cvExistingRecords = new CoeusVector();
        int maxRowId = 0;
        try{
            cvExistingRecords = queryEngine.getDetails(queryKey, AwardPaymentScheduleBean.class);
            cvExistingRecords.sort("rowId",false);
            if( cvExistingRecords != null && cvExistingRecords.size() > 0 ){
                AwardPaymentScheduleBean bean = (AwardPaymentScheduleBean)cvExistingRecords.get(0);
                maxRowId = bean.getRowId();
            }else{
                maxRowId = 0;
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
        return maxRowId;
    }
    
    /* sets the formdata */
    
    public void setFormData(Object data) {
        cvPayment = new CoeusVector();
        AwardPaymentScheduleBean awardPaymentScheduleBean;
        try{
            cvPayment = queryEngine.executeQuery(queryKey, AwardPaymentScheduleBean.class, CoeusVector.FILTER_ACTIVE_BEANS);
            
            
        }catch (CoeusException coeusException){
            coeusException.printStackTrace();
        }
        dlgAwardPaymentSchedule.setTitle(WINDOW_TITLE);
        if(cvPayment!= null && cvPayment.size() >0){
            rowId = cvPayment.size();
        }
        if(cvPayment != null && cvPayment.size() > 0){
            awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(0,0);
            awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPayment.get(0);
            
        }
        awardPaymentScheduleForm.awardHeaderForm1.setFormData(awardDetailsBean);
        //Case #2336 start
        awardPaymentScheduleForm.awardHeaderForm1.lblSequenceNumberValue.setText(EMPTY_STRING+awardBaseBean.getSequenceNumber());
        //Case #2336 end
    }
    
    
    /**
     * validate the form data/Form and returns true if
     * validation is through else returns false.
     */
    public boolean validate() throws CoeusUIException {
        //validate for due date and submitted By
        int row = awardPaymentScheduleForm.tblPayment.getSelectedRow();
        if(row != -1){
            
            AwardPaymentScheduleBean awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPayment.get(row);
            
            if(awardPaymentScheduleForm.tblPayment.isEditing()){
                if(awardPaymentScheduleForm.tblPayment.getEditingColumn() == DUE_DATE) {
                    String message;
                    try{
                        String dueDate, value;
                        Date date;
                        value = paymentScheduleEditor.getCellEditorValue().toString();
                        dueDate = dtUtils.formatDate(
                        value.toString().trim(), DATE_SEPARATERS,DATE_FORMAT);
                        if(dueDate==null) {
                            if( !value.toString().trim().equals(EMPTY_STRING)){
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_DUE_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                                setTableEditing(awardPaymentScheduleForm.tblPayment.getEditingRow(), DUE_DATE);
                                awardPaymentScheduleForm.tblPayment.getEditorComponent().requestFocusInWindow();
                                return false;
                            }else{
                                awardPaymentScheduleBean.setDueDate(null);
                                modified = true;
                            }
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_MSG));
                            setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                            return false;
                        }//End if due Date == null
                        date = dtFormat.parse(dateUtils.restoreDate(dueDate, DATE_SEPARATERS));
                        
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_DUE_DATE);
                        CoeusOptionPane.showInfoDialog(message);
                        return false;
                        
                    }
                    
                }
                //Bug Fix: Pass the person id to get the person details Start 3
                /*else if(awardPaymentScheduleForm.tblPayment.getEditingColumn() == SUBMIT_BY){ //End if - Editing column == Due Date
                    PersonInfoFormBean personInfoFormBean;
                    if(awardPaymentScheduleBean.getPersonFullName() != null){
                        String value = awardPaymentScheduleBean.getPersonFullName().trim();
                        personInfoFormBean = coeusUtils.getPersonInfoID(value.toString());
                        if( personInfoFormBean == null ){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NAME));
                            setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                            showWindow = true;
                            return false;
                        }
                    }
                    
                    
                }*/
                //Bug Fix: Pass the person id to get the person details End 3
                
                else if(awardPaymentScheduleForm.tblPayment.getEditingColumn() == SUBMIT_DATE ){
                    String message;
                    try{
                        String submitDate, value;
                        Date date;
                        value = paymentScheduleEditor.getCellEditorValue().toString();
                        submitDate = dtUtils.formatDate(
                        value.toString().trim(), DATE_SEPARATERS,DATE_FORMAT);
                        if(submitDate==null) {
                            if( !value.toString().trim().equals(EMPTY_STRING)){
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_SUBMIT_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                                return false;
                                
                            }else{
                                awardPaymentScheduleBean.setSubmitDate(null);
                                modified = true;
                            }
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_MSG));
                            setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                            
                            
                            return false;
                        }//End if due Date == null
                        date = dtFormat.parse(dateUtils.restoreDate(submitDate, DATE_SEPARATERS));
                        
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_SUBMIT_DATE);
                        CoeusOptionPane.showErrorDialog(message);
                        setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                        return false;
                    }
                }
            }
            
        }
        
        paymentScheduleEditor.stopCellEditing();
        PersonInfoFormBean personInfoFormBean;
        for(int index = 0;index < cvPayment.size() ;index++){
            AwardPaymentScheduleBean rowBean = (AwardPaymentScheduleBean)cvPayment.elementAt(index);
            if(rowBean.getDueDate() == null){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(DATE_MSG));
                awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(index,index);
                awardPaymentScheduleForm.tblPayment.setColumnSelectionInterval(0,0);
                awardPaymentScheduleForm.tblPayment.scrollRectToVisible(
                awardPaymentScheduleForm.tblPayment.getCellRect(
                index ,0, true));
                awardPaymentScheduleForm.tblPayment.editCellAt(index,DUE_DATE);
                setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                
                return false;
            }
            
            //Bug Fix: Pass the person id to get the person details Start 4
            /*personInfoFormBean = coeusUtils.getPersonInfoID(rowBean.getPersonFullName());
            if( rowBean.getPersonFullName() != null && !rowBean.getPersonFullName().equals(EMPTY)  &&
            personInfoFormBean == null ){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NAME));
                awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(index,index);
                awardPaymentScheduleForm.tblPayment.setColumnSelectionInterval(0,0);
                awardPaymentScheduleForm.tblPayment.scrollRectToVisible(
                awardPaymentScheduleForm.tblPayment.getCellRect(
                index ,0, true));
                awardPaymentScheduleForm.tblPayment.editCellAt(index,SUBMIT_BY);
                setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                return false;
                
                
            }*/
            //Bug Fix: Pass the person id to get the person details End 4
        }
        if(checkDuplicateRow()) return false;
        return true;
        
    }
    
    
    
    /* if the duedate is same in more than one row,it displays the duplicate message*/
    private boolean checkDuplicateRow(){
        paymentScheduleEditor.stopCellEditing();
        Equals dueDateEquals;
        CoeusVector coeusVector = null;
        if(cvPayment!=null && cvPayment.size() > 0){
            for(int index = 0; index < cvPayment.size(); index++){
                AwardPaymentScheduleBean awardPaymentScheduleBean = ( AwardPaymentScheduleBean )cvPayment.get(index);
                dueDateEquals = new Equals("dueDate",awardPaymentScheduleBean.getDueDate());
                coeusVector = cvPayment.filter(dueDateEquals);
                if(coeusVector.size()==-1)return false;
                if(coeusVector!=null && coeusVector.size() > 1){
                    CoeusOptionPane.showInfoDialog("A row duplicates another");
                    awardPaymentScheduleForm.tblPayment.editCellAt(index,DUE_DATE);
                    paymentScheduleEditor.txtComponent.requestFocus();
                    awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(index,index);
                    awardPaymentScheduleForm.tblPayment.scrollRectToVisible(
                    awardPaymentScheduleForm.tblPayment.getCellRect(
                    index ,0, true));
                    modified = true;
                    return true;
                }
                
            }
        }
        return false;
    }
    
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
        
    }
    
    private void setTableEditing(int row,int column) {
        
        awardPaymentScheduleForm.tblPayment.requestFocusInWindow();
        awardPaymentScheduleForm.tblPayment.setRowSelectionInterval(row, row);
        awardPaymentScheduleForm.tblPayment.setColumnSelectionInterval(1,1);
        
        // saves the row and column when you enter a cell
        // in this case the values in prevRow and prevCol is set and
        // SwingUtilities.invokeLater() method is called to do the rest
        final int indexRow = row;
        final int indexColumn = column;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run(){
                awardPaymentScheduleForm.tblPayment.requestFocusInWindow();
                awardPaymentScheduleForm.tblPayment.changeSelection(indexRow , indexColumn, false, false);
                awardPaymentScheduleForm.tblPayment.setEditingColumn(indexColumn);
                awardPaymentScheduleForm.tblPayment.editCellAt(indexRow ,indexColumn);
                awardPaymentScheduleForm.tblPayment.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                
                Component editingComponent = awardPaymentScheduleForm.tblPayment.getEditorComponent();
                editingComponent.requestFocus();
                
                
            }  });
            
            
    }
    
    
    private void setTableEditors(){
        awardPaymentScheduleForm.tblPayment.setRowHeight(22);
        JTableHeader tableHeader = awardPaymentScheduleForm.tblPayment.getTableHeader();
        JTableHeader header = awardPaymentScheduleForm.tblAmount.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        header.setFont(CoeusFontFactory.getLabelFont());
        awardPaymentScheduleForm.tblPayment.setSelectionBackground(java.awt.Color.yellow);
        awardPaymentScheduleForm.tblPayment.setSelectionForeground(java.awt.Color.black);
        awardPaymentScheduleForm.tblPayment.setOpaque(false);
        awardPaymentScheduleForm.tblPayment.setSelectionMode(
        DefaultListSelectionModel.SINGLE_SELECTION);
        
        TableColumn column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(DUE_DATE);
        column.setMinWidth(65);
        column.setPreferredWidth(65);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(AMOUNT);
        column.setMinWidth(110);
        column.setPreferredWidth(110);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(SUBMIT_DATE);
        column.setMinWidth(85);
        column.setPreferredWidth(85);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(SUBMIT_BY);
        column.setMinWidth(125);
        column.setPreferredWidth(125);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(INV_NO);
        column.setMinWidth(58);
        column.setPreferredWidth(58);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        column =  awardPaymentScheduleForm.tblPayment.getColumnModel().getColumn(STATUS_DESCRIPTION);
        column.setMinWidth(138);
        column.setPreferredWidth(138);
        column.setResizable(true);
        column.setCellRenderer(paymentScheduleRenderer);
        column.setCellEditor(paymentScheduleEditor);
        tableHeader.setReorderingAllowed(false);
        
        awardPaymentScheduleForm.tblAmount.setRowSelectionAllowed(false);
        TableColumn amountColumn = awardPaymentScheduleForm.tblAmount.getColumnModel().getColumn(TOTAL_COLUMN);
        column.setMinWidth(55);
        amountColumn.setPreferredWidth(55);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
        amountColumn = awardPaymentScheduleForm.tblAmount.getColumnModel().getColumn(TOTAL_AMOUNT_COLUMN);
        column.setMinWidth(75);
        amountColumn.setPreferredWidth(75);
        amountColumn.setResizable(true);
        amountColumn.setCellRenderer(amountTableCellRenderer);
        
       
    }
    
    
    
    
        /* This method sets the maximum Row ID from the vector
         *that is present in queryEngine
         */
    private void setMaxRowID() {
        CoeusVector cvPayment = new CoeusVector();
        AwardPaymentScheduleBean awardPaymentScheduleBean;
        try {
            cvPayment = queryEngine.getDetails(queryKey,
            AwardPaymentScheduleBean.class);
            if (cvPayment!= null && cvPayment.size() > 0) {
                cvPayment.sort("rowId", false);
                awardPaymentScheduleBean = (AwardPaymentScheduleBean) cvPayment.get(0);
                rowId = awardPaymentScheduleBean.getRowId() + 1;
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    /* table model for paymentschedueletable*/
    class PaymentScheduleTableModel extends AbstractTableModel {
        
        String colNames[] = {"Due Date","Amount","Submit Date" , "Submit By","Inv No", "Status Description"};
        Class[] colTypes = new Class [] {String.class , String.class, String.class, String.class, String.class, String.class};
        
        
        /* if the cell is editable return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                return false;
            }else{
                
                return true;
            }
        }
        
        
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /* returns rowcount*/
        public int getRowCount() {
            if(cvPayment == null ){
                return 0;
            }else{
                return cvPayment.size();
            }
        }
        
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int rowIndex, int columnIndex) {
            AwardPaymentScheduleBean awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPayment.get(rowIndex);
            switch(columnIndex){
                case DUE_DATE:
                    return awardPaymentScheduleBean.getDueDate();
                case AMOUNT:
                    double amount = awardPaymentScheduleBean.getAmount();
                    amountTableModel.fireTableDataChanged();
                    return (""+amount );
                case SUBMIT_DATE:
                    return awardPaymentScheduleBean.getSubmitDate();
                case SUBMIT_BY:
                    return awardPaymentScheduleBean.getPersonFullName();
                case INV_NO:
                    return awardPaymentScheduleBean.getInvoiceNumber();
                case STATUS_DESCRIPTION:
                    return awardPaymentScheduleBean.getStatusDescription();
                    
            }
            return EMPTY_STRING;
        }
        
        
        /* sets the value at a particular cell*/
        public void setValueAt(Object value, int row, int col){
            String dueDate ;
            String subDate;
            String message=null;
            if(cvPayment == null) return;
            PersonInfoFormBean personInfoFormBean = null;
            AwardPaymentScheduleBean awardPaymentScheduleBean = (AwardPaymentScheduleBean)cvPayment.get(row);
            switch(col){
                case DUE_DATE:
                    Date date = null;
                    try{
                        dueDate = dtUtils.formatDate(
                        value.toString().trim(), DATE_SEPARATERS,DATE_FORMAT);
                        if(dueDate==null) {
                            if( !value.toString().trim().equals(EMPTY_STRING)){
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_DUE_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                //awardPaymentScheduleForm.tblPayment.editCellAt(row,DUE_DATE);
                                setTableEditing(row, DUE_DATE);
                                showWindow = true;
                            }else{
                                awardPaymentScheduleBean.setDueDate(null);
                                modified = true;
                            }
                            return ;
                        }
                        date = dtFormat.parse(dateUtils.restoreDate(dueDate, DATE_SEPARATERS));
                        
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_DUE_DATE);
                        CoeusOptionPane.showInfoDialog(message);
                        setTableEditing(row, DUE_DATE);
                        return ;
                        
                    }
                    awardPaymentScheduleBean.setDueDate(new java.sql.Date(date.getTime()));
                    modified = true;
                    showWindow = false;
                    break;
                case AMOUNT:
                    double cost = 0.00;
                    cost = new Double(value.toString()).doubleValue();
                    if(cost != awardPaymentScheduleBean.getAmount()) {
                        awardPaymentScheduleBean.setAmount(cost);
                        amountTableModel.fireTableCellUpdated(TOTAL_COLUMN, TOTAL_AMOUNT_COLUMN);
                        modified = true;
                    }
                    break;
                case SUBMIT_DATE:
                    Date dt = null;
                    try{
                        subDate = dtUtils.formatDate(
                        value.toString().trim(), DATE_SEPARATERS,DATE_FORMAT);
                        if(subDate==null) {
                            if( !value.toString().trim().equals(EMPTY_STRING)){
                                message = coeusMessageResources.parseMessageKey(
                                INVALID_SUBMIT_DATE);
                                CoeusOptionPane.showInfoDialog(message);
                                setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                                showWindow = true;
                                return;
                                //setTableEditing(row, SUBMIT_DATE);
                                //return;
                            }else{
                                awardPaymentScheduleBean.setSubmitDate(null);
                                modified = true;
                            }
                            return;
                        }
                        dt = dtFormat.parse(dateUtils.restoreDate(subDate, DATE_SEPARATERS));
                    }catch (ParseException parseException) {
                        parseException.printStackTrace();
                        message = coeusMessageResources.parseMessageKey(
                        INVALID_SUBMIT_DATE);
                        CoeusOptionPane.showInfoDialog(message);
                        setRequestFocusInThread(paymentScheduleEditor.txtComponent);
                        return ;
                        
                    }
                    awardPaymentScheduleBean.setSubmitDate(new java.sql.Date(dt.getTime()));
                    modified = true;
                    showWindow = false;
                    break;
                case SUBMIT_BY:
                    
                    if (awardPaymentScheduleBean.getPersonFullName() == null || (awardPaymentScheduleBean.getPersonFullName() != null && !value.toString().equals(awardPaymentScheduleBean.getPersonFullName().trim())) ) {
                        if( !value.toString().trim().equals(EMPTY_STRING)){
                            //Bug Fix: Pass the person id to get the person details Start 5
                            if(!searchClicked){
                                personInfoFormBean = coeusUtils.getPersonInfoID(value.toString());
                                if( personInfoFormBean == null ){
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_NAME));
                                    showWindow = true;
                                    return ;
                                }
                            //Bug Fix: Pass the person id to get the person details End 5
                            }else {
                                
                                //Bug Fix: Pass the person id to get the person details Start 6
                                //awardPaymentScheduleBean.setSubmitBy(personInfoFormBean.getPersonID());
                                searchClicked = false;
                                //Bug Fix: Pass the person id to get the person details End 6
                               
                                awardPaymentScheduleBean.setPersonFullName(value.toString());
                                paymentScheduleTableModel.fireTableCellUpdated(row, SUBMIT_BY);
                            }
                        }else{
                            
                            awardPaymentScheduleBean.setSubmitBy(EMPTY_STRING);
                            awardPaymentScheduleBean.setPersonFullName(EMPTY_STRING);
                        }
                        
                        
                    }
                    break;
                case INV_NO:
                    if (!value.toString().trim().equals(awardPaymentScheduleBean.getInvoiceNumber())) {
                        awardPaymentScheduleBean.setInvoiceNumber(value.toString());
                        modified = true;
                    }
                    break;
                case STATUS_DESCRIPTION:
                    if (!value.toString().trim().equals(awardPaymentScheduleBean.getStatusDescription())) {
                        awardPaymentScheduleBean.setStatusDescription(value.toString());
                        modified = true;
                    }
                    break;
            }
            if(awardPaymentScheduleBean.getAcType()== null){
                awardPaymentScheduleBean.setAcType(TypeConstants.UPDATE_RECORD);
            }
        }
        
    }
    
    
    /* table cell editor for payment schedule table*/
    class PaymentScheduleEditor extends AbstractCellEditor implements TableCellEditor, MouseListener {
        private JTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private int column;
        
        public PaymentScheduleEditor(){
            txtComponent = new JTextField();
            txtAmount = new DollarCurrencyTextField();
            txtComponent.addMouseListener(this);
            txtAmount.addMouseListener(this);
            
            
        }
        
        
        /* returns cell editor value*/
        public Object getCellEditorValue() {
            switch(column){
                case DUE_DATE:
                    return txtComponent.getText();
                case AMOUNT:
                    return txtAmount.getValue();
                case SUBMIT_DATE:
                    return txtComponent.getText();
                case SUBMIT_BY:
                    return txtComponent.getText();
                case INV_NO:
                    return txtComponent.getText();
                case STATUS_DESCRIPTION:
                    return txtComponent.getText();
            }
            return  txtComponent;
        }
        
        /* returns celleditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            switch(column){
                case DUE_DATE:
                    txtComponent.setDocument(new LimitedPlainDocument(12));
                    if(value != null){
                        txtComponent.setText(dateUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
                    }else {
                        txtComponent.setText(EMPTY_STRING);
                    }
                    return txtComponent;
                case AMOUNT:
                    if(value == null){
                        txtAmount.setValue(0.00);
                    }else{
                        txtAmount.setValue(new Double(value.toString()).doubleValue());
                    }
                    return txtAmount;
                case SUBMIT_DATE:
                    txtComponent.setDocument(new LimitedPlainDocument(12));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(dateUtils.formatDate(value.toString(), SIMPLE_DATE_FORMAT));
                    }
                    return txtComponent;
                case SUBMIT_BY:
                    txtComponent.setDocument(new LimitedPlainDocument(50));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case INV_NO:
                    txtComponent.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,10));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
                case STATUS_DESCRIPTION:
                    txtComponent.setDocument(new LimitedPlainDocument(50));
                    if(value == null){
                        txtComponent.setText(EMPTY_STRING);
                    }else{
                        txtComponent.setText(value.toString());
                    }
                    return txtComponent;
            }
            return txtComponent;
        }
        
        
        /* on the dounleclick on any of the column,the persondetails window is opened*/
        public void mouseClicked(MouseEvent me) {
            int selRow = awardPaymentScheduleForm.tblPayment.getSelectedRow();
            if( me.getClickCount() == 2 ) {
                if( selRow < 0 ) return ;
                AwardPaymentScheduleBean personBean = (AwardPaymentScheduleBean)cvPayment.get(selRow);
                String personName = personBean.getPersonFullName();
                
                if(personName == null || personName.trim().length() == 0 ){
                    return;
                }else {
                    //Bug Fix: Pass the person id to get the person details Start 7
                    //PersonInfoFormBean personInfoFormBean = coeusUtils.getPersonInfoID(personName);
                    //if( personInfoFormBean == null ){
                    //    return ;
                    //}
                    //Bug Fix: Pass the person id to get the person details End 7
                    
                    txtComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    txtAmount.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    String loginUserName = CoeusGuiConstants.getMDIForm().getUserName();
                    
                    //Bug Fix: Pass the person id to get the person details Start 8
                    //new PersonDetailForm(personInfoFormBean.getPersonID(),loginUserName,TypeConstants.DISPLAY_MODE);
                    
                    new PersonDetailForm(personBean.getSubmitBy(),loginUserName,TypeConstants.DISPLAY_MODE);
                    //Bug Fix: Pass the person id to get the person details End 8
                    
                    txtComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    txtAmount.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    
                    
                }
                
            }
            
        }
        
        public void mouseEntered(MouseEvent e) {
        }
        
        public void mouseExited(MouseEvent e) {
        }
        
        public void mousePressed(MouseEvent e) {
        }
        
        public void mouseReleased(MouseEvent e) {
        }
        
    }
    
   
    
    /* renderer for the payment schedule table*/
    class PaymentScheduleRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        private JTextField txtComponent;
        private DollarCurrencyTextField txtAmount;
        private JLabel lblAmount,lblText;
        
        public PaymentScheduleRenderer(){
            txtComponent = new JTextField();
            txtAmount = new DollarCurrencyTextField();
            lblText = new JLabel();
            lblAmount = new JLabel();
            lblText.setOpaque(true);
            lblAmount.setOpaque(true);
            lblAmount.setHorizontalAlignment(RIGHT);
            txtComponent.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        
        /* returns the table renderer component*/
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col){
                case DUE_DATE:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        value = dateUtils.formatDate(value.toString(), DATE_FORMAT);
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case AMOUNT:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblAmount.setBackground(disabledBackground);
                        lblAmount.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblAmount.setBackground(java.awt.Color.YELLOW);
                        lblAmount.setForeground(java.awt.Color.black);
                    }else{
                        lblAmount.setBackground(java.awt.Color.white);
                        lblAmount.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblAmount.setText(txtAmount.getText());
                    }else{
                        txtAmount.setValue(Double.parseDouble(value.toString()));
                        lblAmount.setText(txtAmount.getText());
                    }
                    return lblAmount;
                case SUBMIT_DATE:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        value = dateUtils.formatDate(value.toString(), DATE_FORMAT);
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case SUBMIT_BY:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case INV_NO:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
                case STATUS_DESCRIPTION:
                    if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                        lblText.setBackground(disabledBackground);
                        lblText.setForeground(java.awt.Color.BLACK);
                    }else if(isSelected){
                        lblText.setBackground(java.awt.Color.YELLOW);
                        lblText.setForeground(java.awt.Color.black);
                    }else{
                        lblText.setBackground(java.awt.Color.white);
                        lblText.setForeground(java.awt.Color.black);
                    }
                    
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtComponent.setText(EMPTY_STRING);
                        lblText.setText(txtComponent.getText());
                    }else{
                        txtComponent.setText(value.toString());
                        lblText.setText(txtComponent.getText());
                    }
                    return lblText;
            }
            return txtComponent;
        }
    }
    
    
    /* table model for amount table*/
    public class AmountTableModel extends AbstractTableModel{
        private String colName[] = { "Total", "", "", "", "",""};
        private Class colClass[] = {String.class, Double.class, String.class,
        String.class, String.class, String.class};
        
        
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        public int getColumnCount(){
            return colName.length;
        }
        
        public Class getColumnClass(int colIndex){
            return colClass[colIndex];
        }
        public int getRowCount(){
            return 1;
        }
        
        public void setData( ){
            
            
        }
        public String getColumnName(int column){
            return colName[column];
        }
        
        public Object getValueAt(int row, int col) {
            double totalAmount = 0.00;
            String name = "Total:";
            if(col==TOTAL_COLUMN){
                return name;
            }
            if(col==TOTAL_AMOUNT_COLUMN){
                totalAmount = cvPayment.sum("amount");
                return new Double(totalAmount);
            }
            return EMPTY_STRING;
        }
        
    }
    
    
    /* renderer for the amount table*/
    public class AmountTableCellRenderer extends DefaultTableCellRenderer
    implements TableCellRenderer {
        private JTextField txtAmount;
        private DollarCurrencyTextField txtAmtValue;
        private JLabel lblTxt,lblValue;
        
        public AmountTableCellRenderer(){
            txtAmount = new JTextField();
            lblTxt = new JLabel();
            lblValue = new JLabel();
            lblTxt.setOpaque(true);
            lblValue.setOpaque(true);
            lblValue.setHorizontalAlignment(RIGHT);
            lblTxt.setFont(CoeusFontFactory.getLabelFont());
            txtAmtValue = new DollarCurrencyTextField();
            txtAmount.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtAmount.setHorizontalAlignment(JTextField.CENTER);
            txtAmount.setForeground(java.awt.Color.BLACK);
            txtAmount.setFont(CoeusFontFactory.getLabelFont());
            txtAmtValue.setBackground(
            (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            txtAmtValue.setForeground(java.awt.Color.BLACK);
            txtAmount.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            txtAmtValue.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int col){
            switch(col) {
                case TOTAL_COLUMN:
                    if(value == null && value.toString().trim().equals(EMPTY_STRING)){
                        txtAmount.setText(EMPTY_STRING);
                        lblTxt.setText(txtAmount.getText());
                    }else{
                        txtAmount.setText(value.toString());
                        lblTxt.setText(txtAmount.getText());
                    }
                    return lblTxt;
                case TOTAL_AMOUNT_COLUMN:
                    if(value == null || value.toString().trim().equals(EMPTY_STRING)){
                        txtAmtValue.setText(EMPTY_STRING);
                        lblValue.setText(txtAmtValue.getText());
                    }else{
                        txtAmtValue.setValue(new Double(value.toString()).doubleValue());
                        lblValue.setText(txtAmtValue.getText());
                    }
                    return lblValue;
            }
            return txtAmount;
        }
        
    }
}










































