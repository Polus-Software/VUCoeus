/*
 * AddValidRatesController.java
 *
 * Created on August 21, 2007, 5:09 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.rates.controller;

import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.rates.bean.CERatesBean;
import edu.mit.coeus.rates.gui.AddValidRatesForm;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.FormattedDocument;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;

/**
 *
 * @author  talarianand
 */
public class AddValidRatesController implements ActionListener {
    
    private CoeusDlgWindow dlgAddRates;
    private AddValidRatesForm addRatesForm;
    private CoeusMessageResources coeusMessageResources;
    
    private static final int WIDTH = 450;
    private static final int HEIGHT = 210;
    private static final String EMPTY_STRING ="";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String VALID_FISCAL_YEAR = "ceRate_fiscalYear_exceptionCode.1110";
    private static final String ENTER_RATE = "ceRate_rate_exceptionCode.1112";
    private static final String DUPLICATE_INFORMATION = "ceRateDuplicate_exceptionCode.1116";
    private static final String INVALID_START_DATE = "ceRate_invalidDate_exceptionCode.1113";
    private static final String VALID_START_DATE = "ceRate_validStartDate_exceptionCode.1111";
    
    private CERatesBean ceRatesBean;
    private CoeusVector cvRatesData;
    private HashMap hmData;
    private static boolean modified = false;
    private DateUtils dtUtils;
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    /** Creates a new instance of AddValidRatesController */
    public AddValidRatesController() {
    }
    
    /** Creates a new instance of AddValidRatesController */
    public AddValidRatesController(HashMap hmData, CoeusVector cvRatesData) {
        coeusMessageResources = CoeusMessageResources.getInstance();
        ceRatesBean = new CERatesBean();
        dtUtils = new DateUtils();
        this.hmData = hmData;
        this.cvRatesData = cvRatesData;
        initComponents();
        registerComponents();
        formatFields();
        setFormData(hmData);
    }
    
    /**
     * Initializes the Form.
     */
    private void initComponents() {
        addRatesForm = new AddValidRatesForm();
        
        dlgAddRates = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), true);
        dlgAddRates.getContentPane().add(addRatesForm);
        dlgAddRates.setResizable(false);
        dlgAddRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddRates.setFont(CoeusFontFactory.getLabelFont());
        dlgAddRates.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddRates.getSize();
        dlgAddRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance();
        decimalFormat.setMinimumIntegerDigits(0);
        decimalFormat.setMaximumIntegerDigits(10);
        
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setDecimalSeparatorAlwaysShown(true);
        
        
        CoeusTextField textField = addRatesForm.txtRate;
        FormattedDocument formattedDocument = new FormattedDocument(decimalFormat,textField);
        formattedDocument.setNegativeAllowed(true);
        textField.setDocument(formattedDocument);
        textField.setText(".00");
        textField.setHorizontalAlignment(JFormattedTextField.RIGHT);
        
        CoeusTextField coeusTextField = addRatesForm.txtFiscalYear;
        coeusTextField.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        
    }
    
    /** Displays the Form which is being controlled.
     */
    public CERatesBean display() {
        dlgAddRates.show();
        return ceRatesBean;
    }
    
    /** 
     * This method is used to set the listeners to the components.
     */
    private void registerComponents() {
        java.awt.Component[] components = {addRatesForm.btnOk, addRatesForm.btnCancel,
        addRatesForm.txtFiscalYear, addRatesForm.txtStartDate, addRatesForm.txtRate}; 
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addRatesForm.setFocusTraversalPolicy(traversePolicy);
        addRatesForm.setFocusCycleRoot(true);
        
        addRatesForm.btnCancel.addActionListener(this);
        addRatesForm.btnOk.addActionListener(this);
        addRatesForm.txtStartDate.addFocusListener(new AddValidRatesController.customFocusAdapter());
        
        dlgAddRates.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgAddRates.addWindowListener(new java.awt.event.WindowAdapter(){
            
            public void windowOpened(java.awt.event.WindowEvent we) {
                requestDefaultFocus();
            }
            
            public void windowClosing(java.awt.event.WindowEvent we){
                performWindowClosing();
                return;
            }
        });
    }
    
    /**
     * Is used to do the necessary operation when closing the form
     */
    private void performWindowClosing() {
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
        CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            performOkAction();
        }else if(option == CoeusOptionPane.SELECTION_NO){
            ceRatesBean = null;
            dlgAddRates.dispose();
        }else if(option==CoeusOptionPane.SELECTION_CANCEL){
            return;
        }
    }
    
    /**
     * Is used to set the default focus when the form is loaded
     */
    private void requestDefaultFocus() {
        addRatesForm.txtFiscalYear.requestFocusInWindow();
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data data to set to the form
     */
    private void setFormData(Object objData) {
        HashMap hmData = (HashMap) objData;
        if(hmData != null && hmData.size() > 0) {
            addRatesForm.txtCE.setText((String) hmData.get("CostElement"));
            addRatesForm.txtDescription.setText((String) hmData.get("Description"));
            String unitNumber = (String) hmData.get("UnitNumber");
            String unitName = (String) hmData.get("UnitName");
            dlgAddRates.setTitle("Add CE Rates for "+unitNumber+":"+unitName);
        }
    }
    
    /**
     * Is used to format the form fields
     */
    private void formatFields() {
        addRatesForm.txtCE.setEditable(false);
        addRatesForm.txtDescription.setEditable(false);
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        
        if(source.equals(addRatesForm.btnCancel)) {
            performCancelAction();
        } else if(source.equals(addRatesForm.btnOk)) {
            performOkAction();
        }
    }
    
    /**
     * Is used to perform the necessary operation when cancel button is pressed
     */
    
    private void performCancelAction() {
        if(isDataChanged()) {
            performWindowClosing();
        } else {
            ceRatesBean = null;
            dlgAddRates.dispose();
        }
    }
    
    /**
     * Is used to perform the necessary operation when ok button is pressed
     */
    
    private void performOkAction() {
        try {
            if(validate()) {
                ceRatesBean.setAcType("I");
                String value = EMPTY_STRING;
                Date modifiedStartDate = null;
                String costElement = EMPTY_STRING;
                String description = EMPTY_STRING;
                String unitNumber = EMPTY_STRING;
                if(hmData != null && hmData.size() > 0) {
                    costElement = (String) hmData.get("CostElement");
                    description = (String) hmData.get("Description");
                    unitNumber = (String) hmData.get("UnitNumber");
                }
                value = dtUtils.restoreDate(addRatesForm.txtStartDate.getText().trim(), DATE_SEPARATERS);
                if( value.trim().length() >0 && value != null ){
                    modifiedStartDate = dtFormat.parse(value);
                }
                String strRate = addRatesForm.txtRate.getText();
                double monthlyRate = 0.0;
                if(strRate != null && strRate.trim() != "") {
                    strRate = strRate.replaceAll(",", "");
                    monthlyRate = Double.parseDouble(strRate);
                }
                String fiscalYear = addRatesForm.txtFiscalYear.getText().trim();
                if(!isDuplicate(costElement, fiscalYear, unitNumber, new java.sql.Date(modifiedStartDate.getTime()))) {
                    ceRatesBean.setCostElement(costElement);
                    ceRatesBean.setDescription(description);
                    ceRatesBean.setUnitNumber(unitNumber);
                    ceRatesBean.setFiscalYear(fiscalYear);
                    ceRatesBean.setStartDate(new java.sql.Date(modifiedStartDate.getTime()));
                    ceRatesBean.setMonthlyRate(monthlyRate);
                    dlgAddRates.dispose();
                }
            }
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }
    
    // check whether the form data is changed or not. If chansged then throw
    // the save confirmation message while clicking on the cancel button.
    private boolean isDataChanged(){
        double rate = 0.00;
        String strRate = addRatesForm.txtRate.getText();
        if(strRate != null && strRate.trim() != "") {
            strRate = strRate.replaceAll(",", "");
            rate = new Double(strRate).doubleValue();
        }
        modified = false;
        if(addRatesForm.txtFiscalYear.getText().trim() != null &&
        !addRatesForm.txtFiscalYear.getText().equals(EMPTY_STRING)){
            modified = true;
        }
        
        if(rate!=0.00){
            modified = true;
        }
        return modified;
    }
    
    /**
     * Is used to validate the form
     */
    private boolean validate() {
        boolean modified = false;
        double rate = 0.00;
        String strRate = addRatesForm.txtRate.getText();
        if(strRate != null && strRate.trim() != "") {
            strRate = strRate.replaceAll(",", "");
            rate = new Double(strRate.trim()).doubleValue();
        }
        
        if(addRatesForm.txtFiscalYear.getText() == null ||
        addRatesForm.txtFiscalYear.getText().trim().equals(EMPTY_STRING)){
            addRatesForm.txtFiscalYear.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
            addRatesForm.txtFiscalYear.setCaretPosition(0);
            return false;
        }else{
            modified = true;
        }
        
        try{
            int fiscalYear = Integer.parseInt(addRatesForm.txtFiscalYear.getText());
        }catch (NumberFormatException numberFormatException ){
            addRatesForm.txtFiscalYear.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
            return false;
        }
        
        if( addRatesForm.txtFiscalYear.getText().trim().length() < 4 || ( !addRatesForm.txtFiscalYear.getText().startsWith("19")) &&
        !addRatesForm.txtFiscalYear.getText().startsWith("20")){
            addRatesForm.txtFiscalYear.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
            addRatesForm.txtFiscalYear.setCaretPosition(0);
            return false;
        }else{
            modified = true;
        }
        
        if(addRatesForm.txtStartDate.getText().trim() == null ||
        addRatesForm.txtStartDate.getText().trim().equals(EMPTY_STRING)){
            addRatesForm.txtStartDate.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_START_DATE));
            addRatesForm.txtStartDate.setCaretPosition(0);
            return false;
        }else{
            modified = true;
        }
        
        if(rate==0.00){
            addRatesForm.txtRate.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(ENTER_RATE));
            addRatesForm.txtRate.setCaretPosition(0);
            return false;
            
        }else{
            modified = true;
        }
        
        return modified;
    }
    
    private boolean isDuplicate(String costElement, String fiscalYear, String unitNumber, java.sql.Date startDate) {
        boolean isDuplicate = false;
        Equals eqCostElement;
        Equals eqFiscalYear;
        Equals eqRate;
        Equals eqStartDate;
        And fiscalYearAndCostElement;
        And fiscalYearCostElementRate;
        And fisYearCEStrDate;
        CoeusVector cvDuplicates = new CoeusVector();
        if(cvRatesData != null && cvRatesData.size() > 0) {
            eqCostElement = new Equals("costElement", costElement);
            eqFiscalYear = new Equals("fiscalYear", fiscalYear);
            eqRate = new Equals("unitNumber", unitNumber);
            eqStartDate = new Equals("startDate", startDate);
            fiscalYearAndCostElement = new And(eqRate, eqCostElement);
            fiscalYearCostElementRate = new And(fiscalYearAndCostElement, eqFiscalYear);
            fisYearCEStrDate = new And(fiscalYearCostElementRate, eqStartDate);
            cvDuplicates = cvRatesData.filter(fisYearCEStrDate);
        }
        if(cvDuplicates != null && cvDuplicates.size() > 0) {
            isDuplicate = true;
            CoeusOptionPane.showInfoDialog(
                  coeusMessageResources.parseMessageKey(DUPLICATE_INFORMATION));
        }
        return isDuplicate;
    }
    
    public class  customFocusAdapter extends FocusAdapter{
        
        public void focusGained(FocusEvent focusEvent){
            String strDate = addRatesForm.txtStartDate.getText().trim();
            if(focusEvent.isTemporary()) return ;
            strDate = dtUtils.restoreDate(addRatesForm.txtStartDate.getText(), DATE_SEPARATERS);
            addRatesForm.txtStartDate.setText(strDate);
            
        }
        
        public void focusLost(FocusEvent focusEvent){
            try{
                if ( focusEvent.getSource()== addRatesForm.txtStartDate){
                    if(addRatesForm.txtStartDate.getText().trim().length() > 0 ){
                        String strDate = addRatesForm.txtStartDate.getText().trim();
                        String convertedDate = dtUtils.formatDate(strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
                        convertedDate = dtUtils.restoreDate(convertedDate, DATE_SEPARATERS);
                        if (convertedDate==null ){
                            addRatesForm.txtStartDate.setText(EMPTY_STRING);
                            addRatesForm.txtStartDate.requestFocus();
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                        }else{
                            String focusDate = dtUtils.formatDate(strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                            addRatesForm.txtStartDate.setText(focusDate);
                        }
                        
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
}
