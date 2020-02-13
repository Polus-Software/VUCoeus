/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * AmountInfoChangeController.java
 *
 * Created on September 8, 2004, 11:11 AM
 */

/* PMD check performed, and commented unused imports and variables on 19-Aug-2010
 * by George J Nirappeal
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.awt.event.*;

import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.query.*;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.*;
import javax.swing.*;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author  surekhan
 */
public class AmountInfoChangeController extends SubcontractController implements ActionListener,FocusListener {
    
    private SubcontractAmountInfoChangeForm subcontractAmountInfoChangeForm;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private CoeusDlgWindow dlgAmountInfoChange;
    
    // Modified for COEUSQA-1412 Subcontract Module changes - Start
//    private static final int WIDTH = 630;
//    
//    private static final int HEIGHT = 280;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 340;
    // Modified for COEUSQA-1412 Subcontract Module changes - End
    
    private static final String WINDOW_TITLE  =  "Change Subcontract Amount";
    
    private DateUtils dateUtils = new DateUtils();
    
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    private CoeusMessageResources coeusMessageResources;
    
    /*Please enter a valid effective date*/
    private static final String INVALID_DATE = "subcontractAmountInfo_exceptionCode.1152";
    
    /*Please enter an effective date*/
    private static final String ENTER_DATE = "subcontractAmountInfo_exceptionCode.1151";
    
    /*Obligated amount can not be more than the anticipated amount*/
    private static final String OBL_CHANGE = "subcontractAmountInfo_exceptionCode.1153";
    
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
     //Commented for pmd check unused private field
    //private DecimalFormat format = (DecimalFormat)NumberFormat.getInstance();
    
    private CoeusVector cvChanges;
    
    private SimpleDateFormat simpleDateFormat;
    
    private QueryEngine queryEngine;
    
    private boolean dataChanged;
    
    private boolean modified;
    
    private boolean fileSelected;
    
    private byte[] blobData;
    
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    
    private SubContractAmountInfoBean subContractAmountInfoBean;
    private String mimeType; //Case 4007
    
    /** Creates a new instance of AmountInfoChangeController */
    public AmountInfoChangeController(SubContractBean subContractBean , char functionType) {
        super(subContractBean);
        this.subContractBean = subContractBean;
        subcontractAmountInfoChangeForm = new SubcontractAmountInfoChangeForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        queryEngine = QueryEngine.getInstance();
        postInitComponents();
        registerComponents();
        setDefaultFocusInWindow();
        setFormData(null);
        
        
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents(){
        dlgAmountInfoChange = new CoeusDlgWindow(mdiForm);
        dlgAmountInfoChange.setResizable(false);
        dlgAmountInfoChange.setModal(true);
        dlgAmountInfoChange.getContentPane().add(subcontractAmountInfoChangeForm);
        dlgAmountInfoChange.setFont(CoeusFontFactory.getLabelFont());
        dlgAmountInfoChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAmountInfoChange.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAmountInfoChange.getSize();
        dlgAmountInfoChange.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAmountInfoChange.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                
                close();
                return;
            }
        });
        
        dlgAmountInfoChange.addWindowListener(new WindowAdapter(){
            
            public void windowClosing(WindowEvent we){
                close();
            }
        });
    }
    
    /* to display the form*/
    //Code modified for princeton enhancement case#2802
    public CoeusVector showChangeInfo(SubContractAmountInfoBean subContractAmountInfoBean) {
        this.subContractAmountInfoBean = subContractAmountInfoBean;
        subcontractAmountInfoChangeForm.txtObligatedChange.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtAnticipatedChange.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtArComments.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtEffectiveDate.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtUploadAgreementPDF.setText(EMPTY_STRING);
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        subcontractAmountInfoChangeForm.txtPerformStartDate.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtPerformEndDate.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtModificationNum.setText(EMPTY_STRING);
        subcontractAmountInfoChangeForm.txtModificationEffecDate.setText(EMPTY_STRING);
        // Added for COEUSQA-1412 Subcontract Module changes - End
        
        setBlobData(null);
        setMimeType(null);//4007
        fileSelected = false;
        subcontractAmountInfoChangeForm.btnViewAgreement.setEnabled(false);
        setDefaultFocusInWindow();
        dlgAmountInfoChange.show();
        subcontractAmountInfoChangeForm.txtEffectiveDate.requestFocusInWindow();
        return cvChanges;
    }
    
    /* to display the form*/
    public void display() {
        subcontractAmountInfoChangeForm.txtEffectiveDate.requestFocusInWindow();
        dlgAmountInfoChange.show();
    }
    
    /** Perform field formatting.
     * enabling, disabling components depending on the different conditions
     */
    public void formatFields() {
        
    }
    
    /** An overridden method of the controller
     * @return subcontractContactsForm returns the controlled form component
     */
    public java.awt.Component getControlledUI() {
        return subcontractAmountInfoChangeForm;
    }
    
    /* to get the form data*/
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        //Code modified for princeton enhancement case#2802
        java.awt.Component[] components = {subcontractAmountInfoChangeForm.txtEffectiveDate,
        subcontractAmountInfoChangeForm.txtObligatedChange,subcontractAmountInfoChangeForm.txtAnticipatedChange,
        subcontractAmountInfoChangeForm.txtPerformStartDate,subcontractAmountInfoChangeForm.txtPerformEndDate,
        subcontractAmountInfoChangeForm.txtModificationNum,subcontractAmountInfoChangeForm.txtModificationEffecDate,
        subcontractAmountInfoChangeForm.txtArComments,subcontractAmountInfoChangeForm.btnOk,
        subcontractAmountInfoChangeForm.btnCancel,subcontractAmountInfoChangeForm.btnUploadAgreement,
        subcontractAmountInfoChangeForm.btnViewAgreement};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        subcontractAmountInfoChangeForm.setFocusTraversalPolicy(traversePolicy);
        subcontractAmountInfoChangeForm.setFocusCycleRoot(true);
        
        subcontractAmountInfoChangeForm.btnOk.addActionListener(this);
        subcontractAmountInfoChangeForm.btnCancel.addActionListener(this);
        subcontractAmountInfoChangeForm.txtEffectiveDate.addFocusListener(this);
        subcontractAmountInfoChangeForm.txtObligatedChange.addFocusListener(this);
        subcontractAmountInfoChangeForm.txtAnticipatedChange.addFocusListener(this);
        subcontractAmountInfoChangeForm.txtArComments.setDocument(new LimitedPlainDocument(300));
        //Code added for princeton enhancement case#2802
        subcontractAmountInfoChangeForm.btnUploadAgreement.addActionListener(this);
        subcontractAmountInfoChangeForm.txtUploadAgreementPDF.setEditable(false);
        subcontractAmountInfoChangeForm.btnViewAgreement.addActionListener(this);
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        CustomFocusAdapter customFocusAdapter = new CustomFocusAdapter();
        subcontractAmountInfoChangeForm.txtPerformStartDate.addFocusListener(customFocusAdapter);
        subcontractAmountInfoChangeForm.txtPerformEndDate.addFocusListener(customFocusAdapter);
        subcontractAmountInfoChangeForm.txtModificationEffecDate.addFocusListener(customFocusAdapter);
        subcontractAmountInfoChangeForm.txtModificationNum.setDocument(new LimitedPlainDocument(50));
        // Added for COEUSQA-1412 Subcontract Module changes - End
        
        
        dlgAmountInfoChange.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setDefaultFocusInWindow();
            }
        });
    }
    
    /* saves the form data*/
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        cvChanges = new CoeusVector();
        Date date;
        try{
            SubContractAmountInfoBean subContractAmountInfoBean = new SubContractAmountInfoBean();
            String strDate = subcontractAmountInfoChangeForm.txtEffectiveDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    subContractAmountInfoBean.setEffectiveDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountInfoBean.setEffectiveDate(new java.sql.Date(date.getTime()));
                }
            }else{
                
                subContractAmountInfoBean.setEffectiveDate(null);
            }
            
            double obligatedAmount =Double.parseDouble(subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",", ""));
            double anticipatedAmount = Double.parseDouble(subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",", ""));
            String comments = subcontractAmountInfoChangeForm.txtArComments.getText();
            
            subContractAmountInfoBean.setObligatedChange(obligatedAmount);
            
            subContractAmountInfoBean.setAnticipatedChange(anticipatedAmount);
            subContractAmountInfoBean.setComments(comments);
            //Code added for princeton enhancement case#2802 - starts
            subContractAmountInfoBean.setFileName(subcontractAmountInfoChangeForm.txtUploadAgreementPDF.getText());
            if(getBlobData() == null){
                subContractAmountInfoBean.setDocument(new byte[0]);
                subContractAmountInfoBean.setMimeType(null);//Case 4007
            } else {
                subContractAmountInfoBean.setDocument(getBlobData());
                subContractAmountInfoBean.setMimeType(getMimeType());//Case 4007
            }
            //Code added for princeton enhancement case#2802 - ends
            
            // Added for COEUSQA-1412 Subcontract Module changes - Start            
            // Performance Start Date            
            String performStartDate = subcontractAmountInfoChangeForm.txtPerformStartDate.getText().trim();
            if(! performStartDate.equals(EMPTY_STRING)) {
                String performStartDate1 =  dateUtils.formatDate(performStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(performStartDate1 == null){
                    performStartDate1 =dateUtils.restoreDate(performStartDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(performStartDate,DATE_SEPARATERS));
                    subContractAmountInfoBean.setPerformanceStartDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(performStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountInfoBean.setPerformanceStartDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractAmountInfoBean.setPerformanceStartDate(null);
            }
            // Performance End Date            
            String performEndDate = subcontractAmountInfoChangeForm.txtPerformEndDate.getText().trim();
            if(! performEndDate.equals(EMPTY_STRING)) {
                String performEndDate1 =  dateUtils.formatDate(performEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(performEndDate1 == null){
                    performEndDate1 =dateUtils.restoreDate(performEndDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(performEndDate,DATE_SEPARATERS));
                    subContractAmountInfoBean.setPerformanceEndDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(performEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountInfoBean.setPerformanceEndDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractAmountInfoBean.setPerformanceEndDate(null);
            }
            // Modification Number
            subContractAmountInfoBean.setModificationNumber(subcontractAmountInfoChangeForm.txtModificationNum.getText().trim());
            // Modification Effective Date
            String modificationEffecDate = subcontractAmountInfoChangeForm.txtModificationEffecDate.getText().trim();
            if(! modificationEffecDate.equals(EMPTY_STRING)) {
                String modificationEffecDate1 =  dateUtils.formatDate(modificationEffecDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(modificationEffecDate1 == null){
                    modificationEffecDate1 =dateUtils.restoreDate(modificationEffecDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(modificationEffecDate,DATE_SEPARATERS));
                    subContractAmountInfoBean.setModificationEffectiveDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(modificationEffecDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountInfoBean.setModificationEffectiveDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractAmountInfoBean.setModificationEffectiveDate(null);
            }

            // Added for COEUSQA-1412 Subcontract Module changes - End                        
            
            cvChanges.add(subContractAmountInfoBean);
            
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }catch(NumberFormatException exception){
            exception.printStackTrace();
        }
        dlgAmountInfoChange.dispose();
        
    }
    
    /* to set the form data*/
    public void setFormData(Object data){
        dlgAmountInfoChange.setTitle(WINDOW_TITLE);
        
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(0);
        decimalFormat.setMaximumIntegerDigits(10);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        CoeusTextField textField = subcontractAmountInfoChangeForm.txtObligatedChange;
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
        //Case 2098 - start
        formattedDocument.setNegativeAllowed(true);
        //Case 2098 - End
        textField.setDocument(formattedDocument);
        textField.setText(".00");
        textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        
        DecimalFormat decFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decFormat.setMinimumIntegerDigits(0);
        decFormat.setMaximumIntegerDigits(10);
        
        decFormat.setMinimumFractionDigits(2);
        decFormat.setMaximumFractionDigits(2);
        decFormat.setDecimalSeparatorAlwaysShown(true);
        
        CoeusTextField txtField = subcontractAmountInfoChangeForm.txtAnticipatedChange;
        FormattedDocument formattedDoc = new FormattedDocument(decFormat , txtField);
        //Case 2098 - start
        formattedDoc.setNegativeAllowed(true);
        //Case 2098 - End
        txtField.setDocument(formattedDoc);
        txtField.setText(".00");
        txtField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        
        String effDate;
        //Commented for pmd check unused local variable
        //Date date;
        
        effDate = subcontractAmountInfoChangeForm.txtEffectiveDate.getText().trim();
        if(!effDate.equals(EMPTY_STRING)){
            if(subcontractAmountInfoChangeForm.txtEffectiveDate.hasFocus()) {
                effDate = dateUtils.formatDate(effDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                effDate = dateUtils.restoreDate(effDate, DATE_SEPARATERS);
                effDate = dateUtils.formatDate(effDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(effDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                 setRequestFocusInThread(subcontractAmountInfoChangeForm.txtEffectiveDate);
                return false;
            }else {
                subcontractAmountInfoChangeForm.txtEffectiveDate.setText(effDate);
            }
        }
        //End Effective Date Validation.
        
        if(subcontractAmountInfoChangeForm.txtEffectiveDate.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_DATE));
            setRequestFocusInThread(subcontractAmountInfoChangeForm.txtEffectiveDate);
            return false;
        }
        
        // Added for COEUSQA-1412 Subcontract Module changes - Start
        // Performance Start Date
        String performStartDate = subcontractAmountInfoChangeForm.txtPerformStartDate.getText().trim();
        if(!performStartDate.equals(EMPTY_STRING)){
            if(subcontractAmountInfoChangeForm.txtPerformStartDate.hasFocus()) {
                performStartDate = dateUtils.formatDate(performStartDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                performStartDate = dateUtils.restoreDate(performStartDate, DATE_SEPARATERS);
                performStartDate = dateUtils.formatDate(performStartDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(performStartDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                setRequestFocusInThread(subcontractAmountInfoChangeForm.txtPerformStartDate);
                return false;
            }else {
                subcontractAmountInfoChangeForm.txtPerformStartDate.setText(performStartDate);
            }
        }
        // Performance End Date        
        String performEndDate = subcontractAmountInfoChangeForm.txtPerformEndDate.getText().trim();
        if(!performEndDate.equals(EMPTY_STRING)){
            if(subcontractAmountInfoChangeForm.txtPerformEndDate.hasFocus()) {
                performEndDate = dateUtils.formatDate(performEndDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                performEndDate = dateUtils.restoreDate(performEndDate, DATE_SEPARATERS);
                performEndDate = dateUtils.formatDate(performEndDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(performEndDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                setRequestFocusInThread(subcontractAmountInfoChangeForm.txtPerformEndDate);
                return false;
            }else {
                subcontractAmountInfoChangeForm.txtPerformEndDate.setText(performEndDate);
            }
        }        
        // Performance End Date
        String modificationEffecDate = subcontractAmountInfoChangeForm.txtModificationEffecDate.getText().trim();
        if(!modificationEffecDate.equals(EMPTY_STRING)){
            if(subcontractAmountInfoChangeForm.txtModificationEffecDate.hasFocus()) {
                modificationEffecDate = dateUtils.formatDate(modificationEffecDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }else {
                modificationEffecDate = dateUtils.restoreDate(modificationEffecDate, DATE_SEPARATERS);
                modificationEffecDate = dateUtils.formatDate(modificationEffecDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            }
            if(modificationEffecDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                setRequestFocusInThread(subcontractAmountInfoChangeForm.txtModificationEffecDate);
                return false;
            }else {
                subcontractAmountInfoChangeForm.txtModificationEffecDate.setText(modificationEffecDate);
            }
        }
        
        // Added for COEUSQA-1412 Subcontract Module changes - End
        
        CoeusVector cvTemp = new CoeusVector();
        try{
            cvTemp = queryEngine.getDetails(queryKey ,SubContractAmountInfoBean.class);
            cvTemp.sort("obligatedAmount" , false);
            if(cvTemp != null && cvTemp.size() > 0){
                //Bug Fix : 2160 - Subcontract bug with negative numbers - START                
                //SubContractAmountInfoBean bean = (SubContractAmountInfoBean)cvTemp.get(0);
                //double value = Double.parseDouble(subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",", "")) + bean.getObligatedAmount();
                //double amt = Double.parseDouble(subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",", "")) + bean.getAnticipatedAmount();
                
                double obligatedChange = Double.parseDouble(subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",", ""));
                double anticipatedChange = Double.parseDouble(subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",", ""));
                
                double anticipatedSum = cvTemp.sum("anticipatedChange");
                double obligatedSum = cvTemp.sum("obligatedChange");
                
                double value = obligatedChange + obligatedSum;
                double amt = anticipatedChange + anticipatedSum;
               // Added for COEUSQA-2536 Subcontract Amount Info Screen displays error when adding money so Obligated amt and Anticipated amt match start.
                DecimalFormat decimalFormat =  new DecimalFormat("#.##");
                value = Double.valueOf(decimalFormat.format(value));
                amt = Double.valueOf(decimalFormat.format(amt));
               // Added for COEUSQA-2536 Subcontract Amount Info Screen displays error when adding money so Obligated amt and Anticipated amt match end.
                
                //Bug Fix : 2160 - Subcontract bug with negative numbers - END
                
                if(value > amt ){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_CHANGE));
                    setRequestFocusInThread(subcontractAmountInfoChangeForm.txtObligatedChange);
                    return false;
                }
            }else{
                if((Double.parseDouble(subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",", ""))) > Double.parseDouble(subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",", ""))){
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(OBL_CHANGE));
                    setRequestFocusInThread(subcontractAmountInfoChangeForm.txtObligatedChange);
                    return false;
                }
            }
        }catch(CoeusException exception){
            
        }
        
        
        return true;
    }
    
    /* tha action performed on the click of the close button*/
    public void close(){
        String change = ".00";
        
        
        if(subcontractAmountInfoChangeForm.txtArComments.getText().trim().equals(EMPTY_STRING) &&
        subcontractAmountInfoChangeForm.txtEffectiveDate.getText().trim().equals(EMPTY_STRING)&&
        subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().equals(change) &&
        subcontractAmountInfoChangeForm.txtObligatedChange.getText().equals(change) &&
        //Code added for princeton enhancement case#2802
        subcontractAmountInfoChangeForm.txtUploadAgreementPDF.getText().equals(EMPTY_STRING)){
            dlgAmountInfoChange.dispose();
            cvChanges = null;
            return;
        }
        
        
        if(!subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().equals(EMPTY_STRING) ||
        !subcontractAmountInfoChangeForm.txtObligatedChange.getText().equals(EMPTY_STRING) ||
        !subcontractAmountInfoChangeForm.txtEffectiveDate.getText().equals(EMPTY_STRING) ||
        !subcontractAmountInfoChangeForm.txtArComments.getText().equals(EMPTY_STRING) ||
        //Code added for princeton enhancement case#2802
        !subcontractAmountInfoChangeForm.txtUploadAgreementPDF.getText().equals(EMPTY_STRING)){
            dataChanged = true;
        }else{
            dataChanged = false;
        }
        if(dataChanged){
            modified = true;
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(SAVE_CHANGES),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            JOptionPane.YES_OPTION);
            modified = false;
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
                    cvChanges = null;
                    dlgAmountInfoChange.dispose();
                    
                    break;
                default:
                    break;
            }
            
        }else{
            dlgAmountInfoChange.dispose();
        }
        
    }
    
    /*to set the default focus*/
    public void setDefaultFocusInWindow(){
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            subcontractAmountInfoChangeForm.txtEffectiveDate.requestFocusInWindow();
        }
    }
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(subcontractAmountInfoChangeForm.btnCancel)){
            close();
        }else if(source.equals(subcontractAmountInfoChangeForm.btnOk)){
            try{
                if(validate()){
                    saveFormData();
                }
                
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
        //Code added for Princeton enhancements case#2802 - starts
        //To upload a file for the particular invoice.
        else if(source.equals(subcontractAmountInfoChangeForm.btnUploadAgreement)){
            String[] fileExtension = {"pdf"};
            CoeusFileChooser fileChooser = new CoeusFileChooser(dlgAmountInfoChange);
            fileChooser.setAcceptAllFileFilterUsed(true);
            //Code commented for Case#3648 - Uploading non-pdf files
//            fileChooser.setSelectedFileExtension(fileExtension);
            fileChooser.showFileChooser();
            if(fileChooser.isFileSelected()){
                String fileName = fileChooser.getSelectedFile();
                if(fileName != null && !fileName.trim().equals("")){
                    int index = fileName.lastIndexOf('.');
                    if(index != -1 && index != fileName.length()){
                        setFileSelected(true);
                        subcontractAmountInfoChangeForm.txtUploadAgreementPDF.setText(fileChooser.getFileName().getName());
                        setBlobData(fileChooser.getFile());
                        //Added for case 4007: Icon based on mime Type : Start
                        CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                        CoeusAttachmentBean attachmentBean = new CoeusAttachmentBean(fileName,fileChooser.getFile());
                        setMimeType(docTypeUtils.getDocumentMimeType(attachmentBean));
                        //4007 End
                        subcontractAmountInfoChangeForm.btnViewAgreement.setEnabled(true);
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                "correspType_exceptionCode.1012"));
                        setFileSelected(false);
                        setBlobData(null);
                        setMimeType(null);
                    }
                }
            }
        }
        else if(source.equals(subcontractAmountInfoChangeForm.btnViewAgreement)){
            if(fileSelected){
                try{
                    viewPdfDocument();
                }catch (Exception exception){
                    exception.printStackTrace();
                    if(!( exception.getMessage().equals(
                            coeusMessageResources.parseMessageKey(
                            "protoDetFrm_exceptionCode.1130")) )){
                        CoeusOptionPane.showInfoDialog(exception.getMessage());
                    }
                }
            }
        }
        //Code added for Princeton enhancements case#2802 - ends
    }
    
    /*to clean up all the objects*/
    public void cleanUp(){
        mdiForm = null;
        dlgAmountInfoChange = null;
        coeusMessageResources = null;
        subcontractAmountInfoChangeForm = null;
        dateUtils = null;
        cvChanges = null;
        simpleDateFormat = null;
    }
    
    /* the action on the gain of the focus*/
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()){
            return;
        }
        Object source = focusEvent.getSource();
        if(source.equals(subcontractAmountInfoChangeForm.txtEffectiveDate)){
            String effectiveDate;
            effectiveDate = subcontractAmountInfoChangeForm.txtEffectiveDate.getText();
            effectiveDate  = dateUtils.restoreDate(effectiveDate , DATE_SEPARATERS);
            subcontractAmountInfoChangeForm.txtEffectiveDate.setText(effectiveDate );
        }else if(source.equals(subcontractAmountInfoChangeForm.txtObligatedChange)){
            String oblChange;
            oblChange = subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",","");
            subcontractAmountInfoChangeForm.txtObligatedChange.setText(oblChange);
        }else if(source.equals(subcontractAmountInfoChangeForm.txtAnticipatedChange)){
            String oblChange;
            oblChange = subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",","");
            subcontractAmountInfoChangeForm.txtAnticipatedChange.setText(oblChange);
        }
        
    }
    
    /* the action on the loss of the focus*/
    public void focusLost(FocusEvent focusEvent) {
        if(modified){
            return;
        }
        if(focusEvent.isTemporary()){
            return;
        }
        Object source = focusEvent.getSource();
        if(source.equals(subcontractAmountInfoChangeForm.txtEffectiveDate)) {
            String effectiveDate;
            effectiveDate = subcontractAmountInfoChangeForm.txtEffectiveDate.getText().trim();
            
            if(!effectiveDate.equals(EMPTY_STRING)) {
                String strDate1 = dateUtils.formatDate(effectiveDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null) {
                    strDate1 = dateUtils.restoreDate(effectiveDate, DATE_SEPARATERS);
                    if( strDate1 == null || strDate1.equals(effectiveDate)) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                        setRequestFocusInThread(subcontractAmountInfoChangeForm.txtEffectiveDate);
                        return ;
                    }
                }else {
                    effectiveDate = strDate1;
                    subcontractAmountInfoChangeForm.txtEffectiveDate.setText(effectiveDate);
                    
                }
            }
            
        }else if(source.equals(subcontractAmountInfoChangeForm.txtObligatedChange)){
            String oblChange;
            oblChange = subcontractAmountInfoChangeForm.txtObligatedChange.getText().replaceAll(",","");
            subcontractAmountInfoChangeForm.txtObligatedChange.setText(oblChange);
        }else if(source.equals(subcontractAmountInfoChangeForm.txtAnticipatedChange)){
            String antChange;
            antChange = subcontractAmountInfoChangeForm.txtAnticipatedChange.getText().replaceAll(",","");
            subcontractAmountInfoChangeForm.txtAnticipatedChange.setText(antChange);
            
        }
        
    }
    
    /** Supporting method which will be used for the focus lost for date
     *fields. This will be fired when the request focus for the specified
     *date field is invoked
     */
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }

    public boolean isFileSelected() {
        return fileSelected;
    }

    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }

    public byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    /** 
     * Code added for Princeton enhancements case#2802
     * Allows to view the PDF document
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        
            CoeusVector cvDataObject = new CoeusVector();
            HashMap hmDocumentDetails = new HashMap();
            hmDocumentDetails.put("subContractCode", subContractAmountInfoBean.getSubContractCode());
            hmDocumentDetails.put("sequenceNumber", ""+subContractAmountInfoBean.getSequenceNumber());
            hmDocumentDetails.put("lineNumber", ""+subContractAmountInfoBean.getLineNumber());
            hmDocumentDetails.put("fileName", subcontractAmountInfoChangeForm.txtUploadAgreementPDF.getText());
            hmDocumentDetails.put("document", getBlobData());
            cvDataObject.add(hmDocumentDetails);
            RequesterBean requesterBean = new RequesterBean();
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", cvDataObject);
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.subcontract.SubcontractDocumentReader");
            map.put("USER", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("MODULE_NAME", "VIEW_DOCUMENT");
            documentBean.setParameterMap(map);
            requesterBean.setDataObject(documentBean);
            requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            
            AppletServletCommunicator appletServletCommunicator = new
                    AppletServletCommunicator(STREMING_SERVLET, requesterBean);
            appletServletCommunicator.send();
            ResponderBean responder = appletServletCommunicator.getResponse();
            
            if(!responder.isSuccessfulResponse()){
                throw new CoeusException(responder.getMessage(),0);
            }
            map = (Map)responder.getDataObject();
            String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
            if(url == null || url.trim().length() == 0 ) {
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
                return;
            }
            url = url.replace('\\', '/') ;
            try{
                URL urlObj = new URL(url);
                URLOpener.openUrl(urlObj);
            }catch (MalformedURLException malformedURLException) {
                malformedURLException.printStackTrace();
            }catch( Exception ue) {
                ue.printStackTrace() ;
            }
    }    
    //Added with case 4007: Icon based on mime Type ; Start
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    //4007:End
    
    
    // Added for COEUSQA-1412 Subcontract Module changes - Change - Start
    public class CustomFocusAdapter extends FocusAdapter{
        private final String DATE_SEPARATERS = "-:/.,|";
        private final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
        private DateUtils dateUtils = new DateUtils();
        private final String INVALID_DATE = "subcontractAmountInfo_exceptionCode.1154";
        
        /*the action performed on the focus gain*/
        public void focusGained(FocusEvent focusEvent) {
            if(focusEvent.isTemporary()) return ;
            Object source = focusEvent.getSource();
            CoeusTextField dateTextField = (CoeusTextField)source;
            String date = dateTextField.getText();
            date  = dateUtils.restoreDate(date , DATE_SEPARATERS);
            if(date != null){
                dateTextField.setText(date);
            }
        }
        
        /*action performed on the focus lost*/
        public void focusLost(FocusEvent focusEvent){
            if(focusEvent.isTemporary()) return;
            Object source = focusEvent.getSource();
            CoeusTextField dateTextField = (CoeusTextField)source;
            String date = dateTextField.getText().trim();
            if(!date.equals(EMPTY_STRING)) {
                String date1 = dateUtils.formatDate(date, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(date1 == null) {
                    date1 = dateUtils.restoreDate(date, DATE_SEPARATERS);
                    if( date1 == null || date1.equals(date)) {
                        MessageFormat formatter = new MessageFormat("");
                        String fieldName = CoeusGuiConstants.EMPTY_STRING;
                        if(dateTextField.equals(subcontractAmountInfoChangeForm.txtPerformStartDate)){
                            fieldName = "period of performance start date";
                        }else if(dateTextField.equals(subcontractAmountInfoChangeForm.txtPerformEndDate)){
                            fieldName = "period of performance end date";
                        }else if(dateTextField.equals(subcontractAmountInfoChangeForm.txtModificationEffecDate)){
                            fieldName = "modification effective date";
                        }
                        String message = formatter.format(coeusMessageResources.parseMessageKey(INVALID_DATE), fieldName);
                        CoeusOptionPane.showWarningDialog(message);
                        setRequestFocusInThread(dateTextField);
                        return ;
                    }
                }else {
                    date = date1;
                    dateTextField.setText(date);
                }
            }
            
        }
    }
    
    // Added for COEUSQA-1412 Subcontract Module changes - Change - End
}
