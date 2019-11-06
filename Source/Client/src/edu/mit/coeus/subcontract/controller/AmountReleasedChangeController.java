/*
 * AmountReleasedChangeController.java
 *
 * Created on September 15, 2004, 3:11 PM
 */

package edu.mit.coeus.subcontract.controller;

import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.subcontract.gui.*;
import edu.mit.coeus.subcontract.bean.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;


import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.*;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.*;
import java.util.Date;

/**
 *
 * @author  surekhan
 */
public class AmountReleasedChangeController  extends SubcontractController implements ActionListener , FocusListener{
    
    private AmountReleasedChangeForm amountReleasedChangeForm;
    
    private CoeusDlgWindow dlgAmountReleasedChange;
    
    private CoeusMessageResources coeusMessageResources;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private static final int WIDTH = 600;
    
    private static final int HEIGHT = 315;
    
    private static final String WINDOW_TITLE = "Release Subcontract Amount";
    
    private static final String EMPTY_STRING = "";
    
    // Code added for Princeton enhancements case#2802 - starts
    private DateUtils dateUtils = new DateUtils();
    
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    private boolean tempararyFlag;
    
    private String txtPreviousStartDate, txtPreviousEndDate;
       
    private boolean fileSelected;
    
    private byte[] blobData;
    
    private String userName;
    
    private Timestamp timeStamp;
    // Code added for Princeton enhancements case#2802 - ends
    
    private static final String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    
    /*Please enter a valid effective date*/
    private static final String INVALID_DATE = "subcontractAmountRelease_exceptionCode.1201";
    
    /*Please enter an effective date*/
    private static final String ENTER_DATE = "subcontractAmountRelease_exceptionCode.1202";
    
    /*Please enter a released amount*/
    private static final String ENTER_AMOUNT = "subcontractAmountRelease_exceptionCode.1203";
    
    private double availableAmount;
    
    private SubContractAmountReleased subContractAmountReleased;
    
    private CoeusVector cvChanges;
    
    private SimpleDateFormat simpleDateFormat;
    
    private boolean dataChanged;
    
    private static final String SAVE_CHANGES = "saveConfirmCode.1002";
    
    private boolean modified;
    
    private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private String mimeType; //Case 4007
    /** Creates a new instance of AmountReleasedChangeController */
    public AmountReleasedChangeController(SubContractBean subContractBean , char functionType) {
        amountReleasedChangeForm = new AmountReleasedChangeForm();
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
        setFormData(null);
    }
    
    /*Instantiates instance objects*/
    private void postInitComponents(){
        
        dlgAmountReleasedChange = new CoeusDlgWindow(mdiForm);
        dlgAmountReleasedChange.setResizable(false);
        dlgAmountReleasedChange.setModal(true);
        dlgAmountReleasedChange.getContentPane().add(amountReleasedChangeForm);
        dlgAmountReleasedChange.setFont(CoeusFontFactory.getLabelFont());
        dlgAmountReleasedChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAmountReleasedChange.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAmountReleasedChange.getSize();
        dlgAmountReleasedChange.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(0);
        decimalFormat.setMaximumIntegerDigits(10);
        
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        
        CoeusTextField textField = amountReleasedChangeForm.txtAmountReleased;
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
        //Case 2098 - start
        formattedDocument.setNegativeAllowed(true);
        //Case 2098 - End
        textField.setDocument(formattedDocument);
        textField.setText(".00");
        textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        
        simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        
        dlgAmountReleasedChange.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
                return;
            }
        });
        
        dlgAmountReleasedChange.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAmountReleasedChange.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                close();
            }
        });
        
        dlgAmountReleasedChange.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                setDefaultFocusInWindow();
            }
        });
    }
    
    /* diaplys the form*/
    public void display() {
        dlgAmountReleasedChange.setVisible(true);
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
        return amountReleasedChangeForm;
    }
    
    /* to get the form data*/
    public Object getFormData() {
        return null;
    }
    
    /** This method is used to set the listeners to the components.
     */
    public void registerComponents() {
        // Code modified for Princeton enhancements case#2802
//        java.awt.Component[] components = {amountReleasedChangeForm.txtEffectiveDate,amountReleasedChangeForm.txtAmountReleased,
//        amountReleasedChangeForm.txtArComments,amountReleasedChangeForm.btnOk,amountReleasedChangeForm.btnCancel};
        java.awt.Component[] components = {amountReleasedChangeForm.txtInvoiceNumber,
        amountReleasedChangeForm.txtStartDate, amountReleasedChangeForm.txtEndDate, 
        amountReleasedChangeForm.txtEffectiveDate, amountReleasedChangeForm.txtAmountReleased,
        amountReleasedChangeForm.txtArComments, amountReleasedChangeForm.btnOk, amountReleasedChangeForm.btnCancel,
        amountReleasedChangeForm.btnUploadIncPDF, amountReleasedChangeForm.btnViewInvoice};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        amountReleasedChangeForm.setFocusTraversalPolicy(traversePolicy);
        amountReleasedChangeForm.setFocusCycleRoot(true);
        
        amountReleasedChangeForm.btnOk.addActionListener(this);
        amountReleasedChangeForm.btnCancel.addActionListener(this);
        amountReleasedChangeForm.txtAmountReleased.addFocusListener(this);
        amountReleasedChangeForm.txtEffectiveDate.addFocusListener(this);
        amountReleasedChangeForm.txtArComments.setDocument(new LimitedPlainDocument(300));
        // Code added for Princeton enhancements case#2802 - starts
        //Adding focus listener to the Start and End date text boxes
        amountReleasedChangeForm.btnUploadIncPDF.addActionListener(this);
        amountReleasedChangeForm.btnViewInvoice.addActionListener(this);
        amountReleasedChangeForm.txtUploadInvPDF.setEditable(false);
        amountReleasedChangeForm.txtStartDate.addFocusListener(new CustomFocusAdapter());
        amountReleasedChangeForm.txtEndDate.addFocusListener(new CustomFocusAdapter());
        // Code added for Princeton enhancements case#2802 - ends
    }
    
    /* to set the default focus*/
    public void setDefaultFocusInWindow(){
        if(getFunctionType() != TypeConstants.DISPLAY_MODE){
            // Code modified for Princeton enhancements case#2802
            // To set the focus in the Invoice Number text box.
//            amountReleasedChangeForm.txtEffectiveDate.requestFocusInWindow();
            amountReleasedChangeForm.txtInvoiceNumber.requestFocusInWindow();
            dataChanged = false;
        }
    }
    
    /* to save the form data*/
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
        cvChanges = new CoeusVector();
        Date date;
        try{
//            SubContractAmountReleased subContractAmountReleased = new SubContractAmountReleased();
            if(subContractAmountReleased == null){
                subContractAmountReleased = new SubContractAmountReleased();
            }
            String strDate = amountReleasedChangeForm.txtEffectiveDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    subContractAmountReleased.setEffectiveDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountReleased.setEffectiveDate(new java.sql.Date(date.getTime()));
                }
            }else{
                
                subContractAmountReleased.setEffectiveDate(null);
            }
            
            strDate = amountReleasedChangeForm.txtStartDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    subContractAmountReleased.setStartDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountReleased.setStartDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractAmountReleased.setStartDate(null);
            }
            
            strDate = amountReleasedChangeForm.txtEndDate.getText().trim();
            if(! strDate.equals(EMPTY_STRING)) {
                String strDate1 =  dateUtils.formatDate(strDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1 == null){
                    strDate1 =dateUtils.restoreDate(strDate, DATE_SEPARATERS);
                    date = simpleDateFormat.parse(dateUtils.restoreDate(strDate,DATE_SEPARATERS));
                    subContractAmountReleased.setEndDate(new java.sql.Date(date.getTime()));
                }else {
                    date = simpleDateFormat.parse(dateUtils.formatDate(strDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    subContractAmountReleased.setEndDate(new java.sql.Date(date.getTime()));
                }
            }else{
                subContractAmountReleased.setEndDate(null);
            }
            
            subContractAmountReleased.setInvoiceNumber(amountReleasedChangeForm.txtInvoiceNumber.getText());
            
            double amountReleased =Double.parseDouble(amountReleasedChangeForm.txtAmountReleased.getText().replaceAll(",", ""));
            
            String comments = amountReleasedChangeForm.txtArComments.getText();
            
            subContractAmountReleased.setAmountReleased(amountReleased);
            
            subContractAmountReleased.setComments(comments);
            // Code modified for Princeton enhancements case#2802 - starts
            // Code added for modifying the existing invoice.
            subContractAmountReleased.setDocument(getBlobData());
            subContractAmountReleased.setMimeType(getMimeType());//Case 4007
            subContractAmountReleased.setFileName(amountReleasedChangeForm.txtUploadInvPDF.getText());
            
            // Code modified for Princeton enhancements case#2802 - ends
            cvChanges.add(subContractAmountReleased);
            
        }catch(ParseException parseException){
            parseException.printStackTrace();
        }catch(NumberFormatException exception){
            exception.printStackTrace();
        }
        dlgAmountReleasedChange.dispose();
    }
    
    /* to set the form data*/
    public void setFormData(Object data){
        dlgAmountReleasedChange.setTitle(WINDOW_TITLE);
    }
    
    /** Validate the form data/Form and returns true if
     * validation is through else returns false.
     * @throws CoeusUIException if some exception occurs or some validation fails.
     * @return true if
     * validation is through else returns false.
     */
    public boolean validate()  {
       
        // Code added for Princeton enhancements case#2802 - starts
        if(amountReleasedChangeForm.txtInvoiceNumber.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1205"));
            setRequestFocusInThread(amountReleasedChangeForm.txtInvoiceNumber);
            return false;
        } else if(amountReleasedChangeForm.txtInvoiceNumber.getText().trim().length() >50){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1215"));
            setRequestFocusInThread(amountReleasedChangeForm.txtInvoiceNumber);
            return false;            
        }
        
        // Validattion for the Start date
        if(amountReleasedChangeForm.txtStartDate.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1206"));
            setRequestFocusInThread(amountReleasedChangeForm.txtStartDate);
            return false;
        }
        if(amountReleasedChangeForm.txtEndDate.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1207"));
            setRequestFocusInThread(amountReleasedChangeForm.txtEndDate);
            return false;
        }
        if (!validateStartDate(amountReleasedChangeForm.txtStartDate.getText().trim())) {
            return false;
        } else if (!validateEndDate(
                dateUtils.restoreDate(amountReleasedChangeForm.txtEndDate.getText().trim(),"/:-,")) ) {
            return false;
        }
        // Code added for Princeton enhancements case#2802 - ends
        
        String effDate;
        // Validattion for the effective date        
        effDate = amountReleasedChangeForm.txtEffectiveDate.getText().trim();
        if(!effDate.equals(EMPTY_STRING)) {
            String effectiveDate = dateUtils.formatDate(effDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            if(effectiveDate == null) {
                effectiveDate = dateUtils.restoreDate(effDate, DATE_SEPARATERS);
                if( effectiveDate == null || effectiveDate.equals(effDate)) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                    setRequestFocusInThread(amountReleasedChangeForm.txtEffectiveDate);
                    return false;
                } else {
                    Date date = new Date();
                    try {
                        date = simpleDateFormat.parse(dateUtils.formatDate(effectiveDate, DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    //Code modified for case#3121
                    int count = DateUtils.calculateDateDiff(Calendar.DATE, 
                            new java.sql.Date(new java.util.Date().getTime()), new java.sql.Date(date.getTime()));
                    if(count > 30 || count < -30){
                        int option = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1216"),
                            CoeusOptionPane.OPTION_YES_NO, JOptionPane.YES_OPTION);
                        if(option == JOptionPane.NO_OPTION){
                            return false;
                        }
                    }
                }
            }else {
                effDate = effectiveDate;
                amountReleasedChangeForm.txtEffectiveDate.setText(effDate);
            }
        }
        
        
        //End Effective Date Validation.
        
        if(amountReleasedChangeForm.txtEffectiveDate.getText().trim().equals(EMPTY_STRING)){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_DATE));
            setRequestFocusInThread(amountReleasedChangeForm.txtEffectiveDate);
            return false;
        }
        
        if(amountReleasedChangeForm.txtAmountReleased.getText().equals(".00")){
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ENTER_AMOUNT));
            setRequestFocusInThread(amountReleasedChangeForm.txtAmountReleased);
            return false;
        }
        
        if(Double.parseDouble(amountReleasedChangeForm.txtAmountReleased.getText().replaceAll(",", "")) > availableAmount){
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String formattedAvailableAmt= decimalFormat.format(availableAmount);
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("subcontractAmountRelease_exceptionCode.1211")+" "+formattedAvailableAmt);
            setRequestFocusInThread(amountReleasedChangeForm.txtAmountReleased);
            return false;
        }

        return true;
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(amountReleasedChangeForm.btnCancel)){
            close();
        }else if(source.equals(amountReleasedChangeForm.btnOk)){
            try{
                if(validate()){
                    saveFormData();
                }
            }catch(CoeusException exception){
                exception.printStackTrace();
            }
        }
        //Code added for Princeton enhancements case#2802 - starts
        //To upload a file for the particular invoice.
        else if(source.equals(amountReleasedChangeForm.btnUploadIncPDF)){
            String[] fileExtension = {"pdf"};
            CoeusFileChooser fileChooser = new CoeusFileChooser(dlgAmountReleasedChange);
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
                        amountReleasedChangeForm.txtUploadInvPDF.setText(fileChooser.getFileName().getName());
                        setBlobData(fileChooser.getFile());
                        //Added for case 4007: Icon based on mime Type : Start
                        CoeusDocumentUtils docTypeUtils = CoeusDocumentUtils.getInstance();
                        CoeusAttachmentBean attachmentBean = new CoeusAttachmentBean(fileName,fileChooser.getFile());
                        setMimeType(docTypeUtils.getDocumentMimeType(attachmentBean));
                        //4007 End
                        amountReleasedChangeForm.btnViewInvoice.setEnabled(true);
                    }else{
                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                "correspType_exceptionCode.1012"));
                        setFileSelected(false);
                        setBlobData(null);
                        setMimeType(null);//Case 4007
                    }
                }
            }
        }
        else if(source.equals(amountReleasedChangeForm.btnViewInvoice)){
            if(fileSelected){
                try {
                    viewPdfDocument();
                } catch (Exception exception){
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
    
    /* the action performed on the click of the close button*/
    public void close(){
        
        // Code modified for Princeton enhancements case#2802 - starts
        double change = .00;
        String effectiveDate = EMPTY_STRING;
        String startDate = EMPTY_STRING;
        String endDate = EMPTY_STRING;
        String invoiceNumber = EMPTY_STRING;
        String comments = EMPTY_STRING;
        if(subContractAmountReleased != null){
            change = subContractAmountReleased.getAmountReleased();
            effectiveDate = (subContractAmountReleased.getEffectiveDate() == null) ? EMPTY_STRING :
                subContractAmountReleased.getEffectiveDate().toString();
            if(!effectiveDate.equals(EMPTY_STRING)) {
                effectiveDate = dateUtils.formatDate(effectiveDate,"dd-MMM-yyyy");
            }
            startDate = (subContractAmountReleased.getStartDate() == null) ? EMPTY_STRING :
                subContractAmountReleased.getStartDate().toString();
            if(!startDate.equals(EMPTY_STRING)) {
                startDate = dateUtils.formatDate(startDate,"dd-MMM-yyyy");
            }
            endDate = (subContractAmountReleased.getEndDate() == null) ? EMPTY_STRING :
                subContractAmountReleased.getEndDate().toString();
            if(!endDate.equals(EMPTY_STRING)) {
                endDate = dateUtils.formatDate(endDate,"dd-MMM-yyyy");
            }
            invoiceNumber = subContractAmountReleased.getInvoiceNumber();
            comments = subContractAmountReleased.getComments();
        }
        if(amountReleasedChangeForm.txtEffectiveDate.getText().trim().equals(EMPTY_STRING) &&
                amountReleasedChangeForm.txtAmountReleased.getText().equals(".00") &&
                amountReleasedChangeForm.txtArComments.getText().trim().equals(EMPTY_STRING) &&
                amountReleasedChangeForm.txtInvoiceNumber.getText().trim().equals(EMPTY_STRING) &&
                amountReleasedChangeForm.txtStartDate.getText().trim().equals(EMPTY_STRING) &&
                amountReleasedChangeForm.txtEndDate.getText().trim().equals(EMPTY_STRING) &&
                amountReleasedChangeForm.txtUploadInvPDF.getText().trim().equals(EMPTY_STRING)){
            dlgAmountReleasedChange.dispose();
            cvChanges = null;
            return;
        }

        double amtReleased = Double.parseDouble(amountReleasedChangeForm.txtAmountReleased.getText().replaceAll(",", ""));
        if(!amountReleasedChangeForm.txtEffectiveDate.getText().equals(effectiveDate) ||
                change != amtReleased ||
                !amountReleasedChangeForm.txtArComments.getText().equals(
                        comments) ||
                !amountReleasedChangeForm.txtInvoiceNumber.getText().trim().equals(
                        invoiceNumber) ||
                !amountReleasedChangeForm.txtStartDate.getText().trim().equals(startDate) ||
                !amountReleasedChangeForm.txtEndDate.getText().trim().equals(endDate) ||
                !amountReleasedChangeForm.txtUploadInvPDF.getText().trim().equals(EMPTY_STRING)){
            dataChanged = true;
        }else{
            dataChanged = false;
        }
        // Code modified for Princeton enhancements case#2802 - ends
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
                    dlgAmountReleasedChange.dispose();
                    
                    break;
                default:
                    break;
            }
            
        }else{
            dlgAmountReleasedChange.dispose();
        }
    }
    
    /* to clean up all the objects*/
    public void cleanUp(){
        amountReleasedChangeForm = null;
        cvChanges = null;
        coeusMessageResources = null;
        mdiForm = null;
        dlgAmountReleasedChange = null;
        subContractAmountReleased = null;
        subContractBean = null;
        simpleDateFormat = null;
        dateUtils = null;
    }
    
    /* the action on the gain of the focus*/
    public void focusGained(FocusEvent focusEvent) {
        if(focusEvent.isTemporary()) return;
        Object source = focusEvent.getSource();
        if(source.equals(amountReleasedChangeForm.txtEffectiveDate)){
            String effectiveDate;
            effectiveDate = amountReleasedChangeForm.txtEffectiveDate.getText().replaceAll(",","");
            effectiveDate  = dateUtils.restoreDate(effectiveDate , DATE_SEPARATERS);
            amountReleasedChangeForm.txtEffectiveDate.setText(effectiveDate );
        }
    }
    
    /* the action on the loss of the focus*/
    public void focusLost(FocusEvent focusEvent) {
        Object source = focusEvent.getSource();
        if(modified) return;
        if(source.equals(amountReleasedChangeForm.txtAmountReleased)){
            String amtReleased = amountReleasedChangeForm.txtAmountReleased.getText().replaceAll(",","");
            amountReleasedChangeForm.txtAmountReleased.setText(amtReleased);
        }else if(source.equals(amountReleasedChangeForm.txtEffectiveDate)){
            String effectiveDate;
            effectiveDate = amountReleasedChangeForm.txtEffectiveDate.getText().trim();
            
            if(effectiveDate.equals(EMPTY_STRING)) return ;
            
            effectiveDate = dateUtils.formatDate(
                    effectiveDate,DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
            
            if(effectiveDate == null) {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_DATE));
                setRequestFocusInThread(amountReleasedChangeForm.txtEffectiveDate);
            }else {
                amountReleasedChangeForm.txtEffectiveDate.setText(effectiveDate);
            }
            
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
    
    /* to show the change window*/
    public CoeusVector showChangeReleased(double amtReleased) {
        availableAmount = amtReleased;
        subContractAmountReleased = null;
        amountReleasedChangeForm.txtAmountReleased.setText(EMPTY_STRING);
        amountReleasedChangeForm.txtArComments.setText(EMPTY_STRING);
        amountReleasedChangeForm.txtEffectiveDate.setText(EMPTY_STRING);
        //Code added for Princeton enhancements case#2802 - starts
        amountReleasedChangeForm.txtInvoiceNumber.setText(EMPTY_STRING);
        amountReleasedChangeForm.txtStartDate.setText(EMPTY_STRING);
        amountReleasedChangeForm.txtEndDate.setText(EMPTY_STRING);
        amountReleasedChangeForm.txtUploadInvPDF.setText(EMPTY_STRING);
        setFileSelected(false);
        setBlobData(null);    
        setMimeType(null);//Case 4007
        setUserName(EMPTY_STRING);
        amountReleasedChangeForm.btnViewInvoice.setEnabled(false);
        setTimeStamp(null);        
        //Code added for Princeton enhancements case#2802 - ends
        dlgAmountReleasedChange.show();
        return cvChanges;
    }
    
    /* to show the change window*/
    
    
    /**
     * Code added for Princeton enhancements case#2802
     * To set the data in the for for modify mode.
     * @param amtReleased
     * @param subContractAmountReleased bean has data to set
     * @return CoeusVector
     */
    public CoeusVector showChangeReleased(double amtReleased,
            SubContractAmountReleased subContractAmountReleased) {
        this.subContractAmountReleased = subContractAmountReleased;
        amountReleasedChangeForm.txtAmountReleased.setText(
                new Double(subContractAmountReleased.getAmountReleased()).toString());
        availableAmount = amtReleased + subContractAmountReleased.getAmountReleased();
        amountReleasedChangeForm.txtArComments.setText(subContractAmountReleased.getComments());
        String date = (subContractAmountReleased.getEffectiveDate() == null) ? EMPTY_STRING :
            subContractAmountReleased.getEffectiveDate().toString();
        if(!date.equals(EMPTY_STRING)) {
            amountReleasedChangeForm.txtEffectiveDate.setText(dateUtils.formatDate(
                    date,"dd-MMM-yyyy"));
            dlgAmountReleasedChange.setTitle("Modify Invoice");
        } else {
            amountReleasedChangeForm.txtEffectiveDate.setText(EMPTY_STRING);
            dlgAmountReleasedChange.setTitle("Create Invoice");
        }
        amountReleasedChangeForm.txtInvoiceNumber.setText(subContractAmountReleased.getInvoiceNumber());
        date = (subContractAmountReleased.getStartDate() == null) ? EMPTY_STRING :
            subContractAmountReleased.getStartDate().toString();
        if(!date.equals(EMPTY_STRING)) {
            amountReleasedChangeForm.txtStartDate.setText(dateUtils.formatDate(
                    date,"dd-MMM-yyyy"));
            txtPreviousStartDate = amountReleasedChangeForm.txtStartDate.getText();
        } else {
            amountReleasedChangeForm.txtStartDate.setText(EMPTY_STRING);
        }
        date = (subContractAmountReleased.getEndDate() == null) ? EMPTY_STRING :
            subContractAmountReleased.getEndDate().toString();
        if(!date.equals(EMPTY_STRING)) {
            amountReleasedChangeForm.txtEndDate.setText(dateUtils.formatDate(
                    date,"dd-MMM-yyyy"));
            txtPreviousEndDate = amountReleasedChangeForm.txtEndDate.getText();
        } else {
            amountReleasedChangeForm.txtEndDate.setText(EMPTY_STRING);
        }
        amountReleasedChangeForm.txtUploadInvPDF.setText(subContractAmountReleased.getFileName());
        setBlobData(subContractAmountReleased.getDocument());
        setMimeType(subContractAmountReleased.getMimeType());//case 4007
        setUserName(subContractAmountReleased.getUpdateUserName());
        setTimeStamp(subContractAmountReleased.getUpdateTimestamp());
        amountReleasedChangeForm.btnViewInvoice.setEnabled(false);
        setFileSelected(false);
        dlgAmountReleasedChange.show();
        return cvChanges;
    }
    
    public CoeusVector getCvChanges() {
        return cvChanges;
    }
    
    public void setCvChanges(CoeusVector cvChanges) {
        this.cvChanges = cvChanges;
    }
    // Code added for Princeton enhancements case#2802 - starts
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateStartDate(String strDate) {
        
        boolean isValid=true;
        String mesg = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS ,"dd-MMM-yyyy");
        if (convertedDate==null){
            convertedDate = dateUtils.restoreDate(strDate, DATE_SEPARATERS);
            if( convertedDate == null || convertedDate.equals(strDate)) {
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_start_exceptionCode.2505");
                setRequestFocusInThread(amountReleasedChangeForm.txtStartDate);
                isValid = false;
            }
        }else{
            strDate = convertedDate;
            amountReleasedChangeForm.txtStartDate.setText(strDate);
        }
        if (isValid && amountReleasedChangeForm.txtEndDate.getText() !=null
                && amountReleasedChangeForm.txtEndDate.getText().trim().length() > 0 ) {
            // compare the date
            Date startDate = null;
            Date endDate = null;
            
            try {
                String strStartDate = amountReleasedChangeForm.txtStartDate.getText().trim();
                String strDate1 =  dateUtils.formatDate(strStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    startDate = dtFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }else{
                    startDate = dtFormat.parse(dateUtils.formatDate(strStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                String strEndDate = amountReleasedChangeForm.txtEndDate.getText().trim();
                String strDate11 =  dateUtils.formatDate(strEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate11==null){
                    endDate = dtFormat.parse(dateUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }else{
                    endDate= dtFormat.parse(dateUtils.formatDate(strEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                
                
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_start_exceptionCode.2505")+convertedDate;
            }
            if(isValid && endDate!=null && endDate.compareTo(startDate)<0){
                //startdate is greater than end date - not ok
                mesg = coeusMessageResources.parseMessageKey(
                        "subcontractAmountRelease_exceptionCode.1214");
                setRequestFocusInThread(amountReleasedChangeForm.txtStartDate);
            }
            
        }
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            amountReleasedChangeForm.txtStartDate.setText(txtPreviousStartDate);
        }
        return isValid;
    }
    
    /**
     * This method used to check the start date and end date validation on focus lost
     */
    private boolean validateEndDate(String strDate) {
        boolean isValid=true;
        String mesg = null;
        String convertedDate = dateUtils.formatDate(strDate, DATE_SEPARATERS ,"dd-MMM-yyyy");
        if (convertedDate==null){
            convertedDate = dateUtils.restoreDate(strDate, "dd-MMM-yyyy");
            if( convertedDate == null || convertedDate.equals(strDate)) {
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_end_exceptionCode.2506");
                setRequestFocusInThread(amountReleasedChangeForm.txtEndDate);
                isValid = false;
            }
        }else {
            strDate = convertedDate;
            amountReleasedChangeForm.txtEndDate.setText(strDate);
        }
        if (isValid && amountReleasedChangeForm.txtStartDate.getText() !=null
                && amountReleasedChangeForm.txtStartDate.getText().trim().length() > 0 ) {
            // compare the date
            
            Date startDate = null;
            Date endDate = null;
            
            try {
                String strStartDate = amountReleasedChangeForm.txtStartDate.getText().trim();
                String strDate1 =  dateUtils.formatDate(strStartDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate1==null){
                    startDate = dtFormat.parse(dateUtils.restoreDate(strStartDate,DATE_SEPARATERS));
                }else{
                    startDate = dtFormat.parse(dateUtils.formatDate(strStartDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
                String strEndDate = amountReleasedChangeForm.txtEndDate.getText().trim();
                String strDate11 =  dateUtils.formatDate(strEndDate, DATE_SEPARATERS, DATE_FORMAT_DISPLAY);
                if(strDate11==null){
                    endDate = dtFormat.parse(dateUtils.restoreDate(strEndDate,DATE_SEPARATERS));
                }else{
                    endDate= dtFormat.parse(dateUtils.formatDate(strEndDate,DATE_SEPARATERS, SIMPLE_DATE_FORMAT));
                    
                }
                
            }catch(java.text.ParseException pe){
                mesg = coeusMessageResources.parseMessageKey(
                        "prop_invalid_end_exceptionCode.2506");
            }
            if(isValid && startDate !=null && startDate.compareTo(endDate)>0){
                //startdate is greater than end date - not ok
                mesg = coeusMessageResources.parseMessageKey(
                        "subcontractAmountRelease_exceptionCode.1214");
                setRequestFocusInThread(amountReleasedChangeForm.txtEndDate);
            }
            
        }
        
        if (mesg != null){
            isValid=false;
            CoeusOptionPane.showErrorDialog(mesg);
            amountReleasedChangeForm.txtEndDate.setText(txtPreviousEndDate);
        }
        
        return isValid;
    }
    
    /**
     * Custom focus adapter which is used for text fields which consists of
     * date values. This is mainly used to format and restore the date value
     * during focus gained / focus lost of the text field.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        //holds the data display Text Field
        CoeusTextField dateField;
        boolean temporary = false;
        
        public void focusGained(FocusEvent fe){
            
            if (fe.getSource().equals( amountReleasedChangeForm.txtStartDate ) ||
                    fe.getSource().equals( amountReleasedChangeForm.txtEndDate ) ){
                tempararyFlag = false;
                dateField = (CoeusTextField)fe.getSource();
                if ( (dateField.getText() != null)
                &&  (!dateField.getText().trim().equals(""))) {
                    String focusDate = dateUtils.restoreDate(
                            dateField.getText(),"/-:,");
                    dateField.setText(focusDate);
                    
                }
            }
        }
        
        public void focusLost(FocusEvent fe){
            temporary = fe.isTemporary();
            if (!fe.isTemporary())  {
                if(!tempararyFlag){
                    tempararyFlag = true;
                    if (fe.getSource()==amountReleasedChangeForm.txtStartDate){
                        if(amountReleasedChangeForm.txtStartDate==null || amountReleasedChangeForm.txtStartDate.getText().trim().length()==0 ||
                                amountReleasedChangeForm.txtStartDate.equals("")){
                            txtPreviousStartDate = amountReleasedChangeForm.txtStartDate.getText();
                        }else if (amountReleasedChangeForm.txtStartDate.getText().trim().length() > 0 && !validateStartDate(amountReleasedChangeForm.txtStartDate.getText().trim()) ) {
                            amountReleasedChangeForm.txtStartDate.requestFocus();
                        }
                    }else if (fe.getSource()==amountReleasedChangeForm.txtEndDate){
                        if(amountReleasedChangeForm.txtEndDate==null || amountReleasedChangeForm.txtEndDate.getText().trim().length()==0 ||amountReleasedChangeForm.txtEndDate.equals("")){
                            txtPreviousEndDate = amountReleasedChangeForm.txtEndDate.getText();
                        }else if (amountReleasedChangeForm.txtEndDate.getText().trim().length() > 0 &&  !validateEndDate(amountReleasedChangeForm.txtEndDate.getText().trim()) ) {
                            tempararyFlag = true;
                        }
                    }
                }
                
            }
        }
    }
    // Code added for Princeton enhancements case#2802 - end

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    /**
     * Code added for Princeton enhancements case#2802
     * Allows to view the PDF document
     * @throws Exception in case of error occured
     */
    private void viewPdfDocument() throws Exception{
        
        CoeusVector cvDataObject = new CoeusVector();
        HashMap hmDocumentDetails = new HashMap();
        hmDocumentDetails.put("subContractCode", subContractAmountReleased.getSubContractCode());
        hmDocumentDetails.put("sequenceNumber", ""+subContractAmountReleased.getSequenceNumber());
        hmDocumentDetails.put("lineNumber", ""+subContractAmountReleased.getLineNumber());
        hmDocumentDetails.put("fileName", amountReleasedChangeForm.txtUploadInvPDF.getText());
        hmDocumentDetails.put("document", getBlobData());
        cvDataObject.add(hmDocumentDetails);
        RequesterBean requesterBean = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        HashMap map = new HashMap();
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
        map = (HashMap)responder.getDataObject();
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
}
