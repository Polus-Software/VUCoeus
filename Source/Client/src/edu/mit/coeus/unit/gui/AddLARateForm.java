/*
 * AddLARateForm.java
 *
 * Created on March 2, 2004, 6:15 PM
 */

package edu.mit.coeus.unit.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.budget.bean.InstituteLARatesBean;




import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
/**
 *
 * @author  chandru
 */
public class AddLARateForm extends javax.swing.JComponent implements ActionListener {
    
    private DateUtils dtUtils = new DateUtils();
    private CoeusDlgWindow dlgAddRates;
    private CoeusAppletMDIForm mdiForm;
    private boolean modal;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;
    private static final String EMPTY_STRING = "";
    private String unitNumber = EMPTY_STRING;
    private String unitName = EMPTY_STRING;
    private String rateClassDesc = EMPTY_STRING;
    private int rateClassCode = 0;
    private int rateTypeCode = 0;
    private CoeusVector cvRates = new CoeusVector();
    
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private boolean modified = false;
    private CoeusMessageResources coeusMessageResources;
     /** To specify the date format*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    
    private InstituteLARatesBean instituteLARatesBean;
    
    private static final String VALID_FISCAL_YEAR = "laRate_fiscalYear_exceptionCode.1100";
    private static final String VALID_START_DATE = "laRate_validStartDate_exceptionCode.1101";
    private static final String SELECT_CAMPUS_FLAG = "laRate_campus_exceptionCode.1102";
    private static final String ENTER_RATE = "laRate_rate_exceptionCode.1103";
    private static final String INVALID_START_DATE = "laRate_invalidDate_exceptionCode.1104";
    private static final String DUPLICATE_INFORMATION = "laRateDuplicate_exceptionCode.1107";
    
    
    /** Creates new form AddLARateForm */
    public AddLARateForm(CoeusAppletMDIForm mdiForm, boolean modal,String unitNumber, 
    String unitName,String rateClassDesc, int rateClassCode,int rateTypeCode,CoeusVector cvRates) {
        this.mdiForm = mdiForm;
        this.modal = modal;
        this.unitNumber = unitNumber;
        this.unitName = unitName;
        this.rateClassDesc = rateClassDesc;
        this.rateClassCode = rateClassCode;
        this.rateTypeCode = rateTypeCode;
        this.cvRates = cvRates;
        coeusMessageResources = CoeusMessageResources.getInstance();
        initComponents();
        registerComponents();
        setFormData();
        postInitComponents();
    }
    
    public InstituteLARatesBean display(){
        txtFiscalYear.requestFocusInWindow();
        dlgAddRates.setVisible(true);
        return instituteLARatesBean;
    }
    
    private void registerComponents(){
        btnCancel.addActionListener(this);
        btnOk.addActionListener(this);
        txtStartDate.addFocusListener(new customFocusAdapter());
        
        txtFiscalYear.setDocument(new LimitedPlainDocument(4));
        
        Component[] comp = {txtFiscalYear, txtStartDate, rdBtnBoth,txtRate,btnOk,btnCancel };
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        
    }
    
    private boolean isDuplicate(String fiscalYear, java.sql.Date startDate, boolean campus){
        boolean isDuplicate = false;
        InstituteLARatesBean instituteLARatesBean;
        Equals eqFiscalYear;
        Equals eqStartDate;
        Equals eqCampus;
        And fiscalYearAndStartDate;
        And fiscalYearStartDateAndCampus;
        CoeusVector cvDuplicates;
        if(cvRates!= null && cvRates.size() > 0){
            eqFiscalYear = new Equals("fiscalYear", fiscalYear);
            eqStartDate = new Equals("startDate", startDate);
            
            fiscalYearAndStartDate = new And(eqFiscalYear,eqStartDate );
            
            if(! isBothSelected()) {
                eqCampus = new Equals("onOffCampusFlag", campus);
                fiscalYearStartDateAndCampus = new And(fiscalYearAndStartDate, eqCampus);
                cvDuplicates = cvRates.filter(fiscalYearStartDateAndCampus);
            }else {            
                cvDuplicates = cvRates.filter(fiscalYearAndStartDate);
            }
            
            if(cvDuplicates.size() > 0){
                isDuplicate = true;
                 CoeusOptionPane.showInfoDialog(
                       coeusMessageResources.parseMessageKey(DUPLICATE_INFORMATION));
            }
        }
        return isDuplicate;
    }
    
    
    private void postInitComponents(){
        String unitInfo = unitNumber + " : " + unitName;
        dlgAddRates = new CoeusDlgWindow(mdiForm);
        dlgAddRates.getContentPane().add(this);
        dlgAddRates.setTitle("Add LA Rate for " + unitInfo);
        dlgAddRates.setFont(CoeusFontFactory.getLabelFont());
        dlgAddRates.setModal(true);
        dlgAddRates.setResizable(false);
        dlgAddRates.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddRates.getSize();
        dlgAddRates.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddRates.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgAddRates.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddRates.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we) {
                txtFiscalYear.requestFocusInWindow();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
    }
    private void performCancelAction(){
        
        try{
            if(isDataChanged()){
                performWindowClosing();
            }else{
                dlgAddRates.dispose();
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
    
    public void performWindowClosing(){
        int option = CoeusOptionPane.showQuestionDialog(
        coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
        CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
        if(option == CoeusOptionPane.SELECTION_YES){
            performOkAction();
        }else if(option == CoeusOptionPane.SELECTION_NO){
            instituteLARatesBean = null;
            dlgAddRates.dispose();
        }else if(option==CoeusOptionPane.SELECTION_CANCEL){
            return;
        }
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        Object source= actionEvent.getSource();
        if(source.equals(btnOk)){
            performOkAction();
        }else if(source.equals(btnCancel)){
            performCancelAction();
        }
    }
    
    private void performOkAction(){
        try{
            if(validateData()){
                String value =  EMPTY_STRING;
                Date modifiedStartDate;
                boolean onOffFlag;
                instituteLARatesBean = new InstituteLARatesBean();
                instituteLARatesBean.setFiscalYear(txtFiscalYear.getText().trim());            
                value = dtUtils.restoreDate(txtStartDate.getText().trim(), DATE_SEPARATERS);
                if( value.trim().length() >0 && value != null ){
                    modifiedStartDate = dtFormat.parse(value);
                    instituteLARatesBean.setStartDate( new java.sql.Date(modifiedStartDate.getTime()));
                }
                onOffFlag = rdBtnOn.isSelected();
                onOffFlag = ! rdBtnOff.isSelected();
                instituteLARatesBean.setOnOffCampusFlag(onOffFlag);
                
                // If the fiscal year and start date are same then shsow the message
                if(!isDuplicate(instituteLARatesBean.getFiscalYear(), 
                        instituteLARatesBean.getStartDate(),instituteLARatesBean.isOnOffCampusFlag())){

                        instituteLARatesBean = new InstituteLARatesBean();
                        instituteLARatesBean.setFiscalYear(txtFiscalYear.getText().trim());
                        //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - start
                        //If the rate is empty set rate asa 0
                        String strRate = txtRate.getText().trim();
                        if(strRate != null && strRate.trim() != "") {
                            instituteLARatesBean.setInstituteRate(Double.parseDouble(strRate));
                        }else{
                            instituteLARatesBean.setInstituteRate(0.0);
                        }
                        //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - end
                        value = dtUtils.restoreDate(txtStartDate.getText().trim(), DATE_SEPARATERS);
                        if( value.trim().length() >0 && value != null ){
                            modifiedStartDate = dtFormat.parse(value);
                            instituteLARatesBean.setStartDate( new java.sql.Date(modifiedStartDate.getTime()));
                        }
                        instituteLARatesBean.setUnitNumber(unitNumber);
                        instituteLARatesBean.setRateClassCode(rateClassCode);
                        instituteLARatesBean.setRateClassDescription(rateClassDesc);
                        instituteLARatesBean.setRateTypeCode(rateTypeCode);
                        if(rdBtnOn.isSelected()){
                            instituteLARatesBean.setOnOffCampusFlag(true);
                            //rdBtnOn.setSelected(true);
                            
                            rdBtnOff.setSelected(false);
                            rdBtnBoth.setSelected(false);
                        }else if(rdBtnOff.isSelected()){
                            instituteLARatesBean.setOnOffCampusFlag(false);
                            //rdBtnOff.setSelected(true);
                            
                            rdBtnOn.setSelected(false);
                            rdBtnBoth.setSelected(false);
                        }else if(rdBtnBoth.isSelected()){
                            //rdBtnBoth.setSelected(true);
                            
                            rdBtnOn.setSelected(false);
                            rdBtnOff.setSelected(false);
                            instituteLARatesBean.setOnOffCampusFlag(true);
                        }
                        instituteLARatesBean.setAcType("I");
                        modified = true;

                    dlgAddRates.dispose();
                }
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }            
    }
    // check whether if the 'both' option selected in campus. if both is selected
    // add two rows one is with "On" and "Off"
    public boolean isBothSelected(){
        boolean isBoth = false;
        instituteLARatesBean = new InstituteLARatesBean();
        if(rdBtnBoth.isSelected()){
            instituteLARatesBean.setUnitNumber(unitNumber);
            instituteLARatesBean.setRateClassCode(rateClassCode);
            instituteLARatesBean.setRateClassDescription(rateClassDesc);
            instituteLARatesBean.setRateTypeCode(rateTypeCode);
            isBoth = true; 
        }
        return isBoth;
    }
    // set the form data.
    public void setFormData(){
        txtRateClass.setText(rateClassDesc);
        txtRateClass.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        txtRateClass.setForeground(Color.black);
    }
    
    // Validate the form data
    public boolean validateData(){
        double rate = 0.00;
        rate = new Double(txtRate.getText().trim()).doubleValue();
        
        if(txtFiscalYear.getText() == null ||
        txtFiscalYear.getText().trim().equals(EMPTY_STRING)){
            txtFiscalYear.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
            txtFiscalYear.setCaretPosition(0);
            return false;
        }else{
            modified = true;
        }
        
        try{
                int fiscalYear = Integer.parseInt(txtFiscalYear.getText());
            }catch (NumberFormatException numberFormatException ){
                 txtFiscalYear.requestFocus();
                CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
                return false;
            }
        
        if( txtFiscalYear.getText().trim().length() < 4 || ( !txtFiscalYear.getText().startsWith("19")) &&
        !txtFiscalYear.getText().startsWith("20")){
            
            
            txtFiscalYear.requestFocus();
             CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_FISCAL_YEAR));
            txtFiscalYear.setCaretPosition(0);
            
            
            return false;
        }else{
            modified = true;
        }
        
        
        if(txtStartDate.getText().trim() == null ||
        txtStartDate.getText().trim().equals(EMPTY_STRING)){
            txtStartDate.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(VALID_START_DATE));
            txtStartDate.setCaretPosition(0);
            return false;
        }else{
            modified = true;
        }
        
        if(btnGrpCampus.getSelection() == null){
            rdBtnBoth.requestFocus();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey(SELECT_CAMPUS_FLAG));
            return false;
        }else{
            modified = true;
        }
        //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - start
        //User is allowed to enter 0
//        if(rate==0.00){
//            txtRate.requestFocus();
//            CoeusOptionPane.showInfoDialog(
//            coeusMessageResources.parseMessageKey(ENTER_RATE));
//            txtRate.setCaretPosition(0);
//            return false;
//            
//        }else{
//            modified = true;
//        }
        //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - end
        
        // Check if the statrt date is not correct. If it is invalid date then show the error message
//        if(txtStartDate.getText().trim().length() > 0 ){
//            String strDate = txtStartDate.getText().trim();
//            String convertedDate = dtUtils.formatDate(strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
//            convertedDate = dtUtils.restoreDate(convertedDate, DATE_SEPARATERS);
//            if (convertedDate==null ){
//                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
//                //txtStartDate.requestFocusInWindow();
//                return false;
//            }
//        }
        return true;
    }
    // check whether the form data is changed or not. If chansged then throw
    // the save confirmation message while clicking on the cancel button.
    private boolean isDataChanged(){
        double rate = 0.00;
        rate = new Double(txtRate.getText().trim()).doubleValue();
        modified = false;
        if(txtFiscalYear.getText().trim() != null &&
        !txtFiscalYear.getText().equals(EMPTY_STRING)){
            modified = true;
        }
        
        if(txtStartDate.getText().trim() != null &&
        !txtStartDate.getText().trim().equals(EMPTY_STRING)){
            modified = true;
        }
        
        if(btnGrpCampus.getSelection() != null){
            modified = true;
        }
        //Commented for case 3632 - Data Error in rates maintenance when current rate is 0 - start
        //User is allowed to enter 0 for rate
//        if(rate!=0.00){
//            modified = true;
//        }
        //Modified for case 3632 - Data Error in rates maintenance when current rate is 0 - end
        return modified;
    }
    
    public class  customFocusAdapter extends FocusAdapter{
        
        public void focusGained(FocusEvent focusEvent){
            String strDate = txtStartDate.getText().trim();
            if(focusEvent.isTemporary()) return ;
            strDate = dtUtils.restoreDate(txtStartDate.getText(), DATE_SEPARATERS);
            txtStartDate.setText(strDate);
            
        }
        
        public void focusLost(FocusEvent focusEvent){
            try{
                if ( focusEvent.getSource()== txtStartDate){
                    if(txtStartDate.getText().trim().length() > 0 ){
                        String strDate = txtStartDate.getText().trim();
                        String convertedDate = dtUtils.formatDate(strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
                        convertedDate = dtUtils.restoreDate(convertedDate, DATE_SEPARATERS);
                        if (convertedDate==null ){
                            txtStartDate.setText(EMPTY_STRING);
                            txtStartDate.requestFocus();
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(INVALID_START_DATE));
                        }else{
                            String focusDate = dtUtils.formatDate(strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                            txtStartDate.setText(focusDate);
                        }
                        
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnGrpCampus = new javax.swing.ButtonGroup();
        lblRateClass = new javax.swing.JLabel();
        lblFiscalYear = new javax.swing.JLabel();
        lblStartDate = new javax.swing.JLabel();
        lblCampusFlag = new javax.swing.JLabel();
        txtRateClass = new javax.swing.JTextField();
        txtFiscalYear = new javax.swing.JTextField();
        txtStartDate = new javax.swing.JTextField();
        rdBtnOn = new javax.swing.JRadioButton();
        rdBtnOff = new javax.swing.JRadioButton();
        rdBtnBoth = new javax.swing.JRadioButton();
        lblRate = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        txtRate = new edu.mit.coeus.utils.CurrencyField();

        setLayout(new java.awt.GridBagLayout());

        setFont(CoeusFontFactory.getNormalFont()        );
        lblRateClass.setFont(CoeusFontFactory.getLabelFont());
        lblRateClass.setText("Rate Class:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(12, 10, 0, 0);
        add(lblRateClass, gridBagConstraints);

        lblFiscalYear.setFont(CoeusFontFactory.getLabelFont());
        lblFiscalYear.setText("Fiscal Year:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        add(lblFiscalYear, gridBagConstraints);

        lblStartDate.setFont(CoeusFontFactory.getLabelFont());
        lblStartDate.setText("Start Date:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(9, 5, 0, 0);
        add(lblStartDate, gridBagConstraints);

        lblCampusFlag.setFont(CoeusFontFactory.getLabelFont());
        lblCampusFlag.setText("Campus Flag:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 5, 0, 0);
        add(lblCampusFlag, gridBagConstraints);

        txtRateClass.setEditable(false);
        txtRateClass.setFont(CoeusFontFactory.getNormalFont());
        txtRateClass.setMinimumSize(new java.awt.Dimension(200, 20));
        txtRateClass.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 0, 0);
        add(txtRateClass, gridBagConstraints);

        txtFiscalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtFiscalYear.setFont(CoeusFontFactory.getNormalFont());
        txtFiscalYear.setMinimumSize(new java.awt.Dimension(90, 22));
        txtFiscalYear.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 0);
        add(txtFiscalYear, gridBagConstraints);

        txtStartDate.setFont(CoeusFontFactory.getNormalFont());
        txtStartDate.setMinimumSize(new java.awt.Dimension(90, 22));
        txtStartDate.setPreferredSize(new java.awt.Dimension(90, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 0);
        add(txtStartDate, gridBagConstraints);

        btnGrpCampus.add(rdBtnOn);
        rdBtnOn.setFont(CoeusFontFactory.getLabelFont());
        rdBtnOn.setMnemonic('n');
        rdBtnOn.setText("On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 9, 0, 0);
        add(rdBtnOn, gridBagConstraints);

        btnGrpCampus.add(rdBtnOff);
        rdBtnOff.setFont(CoeusFontFactory.getLabelFont());
        rdBtnOff.setMnemonic('f');
        rdBtnOff.setText("Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        add(rdBtnOff, gridBagConstraints);

        btnGrpCampus.add(rdBtnBoth);
        rdBtnBoth.setFont(CoeusFontFactory.getLabelFont());
        rdBtnBoth.setMnemonic('B');
        rdBtnBoth.setText("Both");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        add(rdBtnBoth, gridBagConstraints);

        lblRate.setFont(CoeusFontFactory.getLabelFont());
        lblRate.setText("Rate:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        add(lblRate, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 3);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 3);
        add(btnCancel, gridBagConstraints);

        txtRate.setText("currencyField1");
        txtRate.setMinimumSize(new java.awt.Dimension(59, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        add(txtRate, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.ButtonGroup btnGrpCampus;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblCampusFlag;
    public javax.swing.JLabel lblFiscalYear;
    public javax.swing.JLabel lblRate;
    public javax.swing.JLabel lblRateClass;
    public javax.swing.JLabel lblStartDate;
    public javax.swing.JRadioButton rdBtnBoth;
    public javax.swing.JRadioButton rdBtnOff;
    public javax.swing.JRadioButton rdBtnOn;
    public javax.swing.JTextField txtFiscalYear;
    public edu.mit.coeus.utils.CurrencyField txtRate;
    public javax.swing.JTextField txtRateClass;
    public javax.swing.JTextField txtStartDate;
    // End of variables declaration//GEN-END:variables
    
}