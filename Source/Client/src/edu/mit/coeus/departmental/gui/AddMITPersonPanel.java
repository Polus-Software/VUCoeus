/*
 * @(#)AddMITPersonPanel.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */


package edu.mit.coeus.departmental.gui;


import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.departmental.bean.DepartmentPersonFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.AutoCompleteCoeusCombo;

import java.awt.ActiveEvent.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Cursor;
import java.util.Date;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

/**
 * This class is used to add a person.
 * It has all the entry fields in a scroll pane and OK and cancel buttons
 * This class handles the insertion of Person details to the data base.
 * Created on March 20, 2004, 10:02 PM
 * @author  bijosh
 */

public class AddMITPersonPanel extends javax.swing.JComponent
implements ActionListener, ItemListener {
    
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private AppletServletCommunicator comm;
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    private DateUtils dtUtils =new DateUtils();
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private CoeusMessageResources coeusMessageResources;
    /** To specify the date format*/
    private java.text.SimpleDateFormat dtFormat
    = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private static final char DUPLICATE_CHK_FN_TYPE='H';
    private static final char ADD_PERSON_FN_TYPE='G';
    private static final String AC_TYPE="I";
    
    private CoeusDlgWindow dlgAddPerson;
    private DepartmentPersonFormBean departmentPersonFormBean;
    
    private static final int DLG_WIDTH=810;
    private static final int DLG_HEIGHT=520;
    
    private static final int SSN_LIMIT=9;
    private static final int LASTNAME_LIMIT=30;
    private static final int FIRSTNAME_LIMIT=30;
    private static final int MIDDLENAME_LIMIT=30;
    private static final int FULLNAME_LIMIT=90;
    private static final int PRIORNAME_LIMIT=30;
    private static final int USERNAME_LIMIT=60;
    private static final int EMAIL_LIMIT=60;
    private static final int GENDER_LIMIT=30;
    private static final int RACE_LIMIT=30;
//JM    private static final int OFFICELOCATION_LIMIT=30;
    private static final int OFFICELOCATION_LIMIT=45; //JM 5-25-2011 updated per 4.4.2
    private static final int OFFICEPHONE_LIMIT=20;
//JM    private static final int SEC_OFFICELOCATION_LIMIT=30;
    private static final int SEC_OFFICELOCATION_LIMIT=45; //JM 5-25-2011 update per 4.4.2
    private static final int SEC_OFFICEPHONE_LIMIT=20;
    private static final int SALUTATION_LIMIT=30;
    private static final int DIRECTORY_TITLE_LIMIT=50;
    private static final int PRIMARY_TITLE_LIMIT=51;
    private static final int DIRECTORY_DEPT_LIMIT=30;
    private static final int HOME_UNIT_LIMIT=8;
    private static final int EDUCATION_LEVEL_LIMIT=30;
    private static final int DEGREE_LIMIT=11;
    private static final int MAJOR_LIMIT=30;
    private static final int SCHOOL_LIMIT=50;
    private static final int YEAR_GRADUATED=30;
    private static final int VETRAN_TYPE_LIMIT=30;
    private static final int HANDICAPPED_TYPE_LIMIT=30;
    private static final int COUNTRY_CITIZENSHIP_LIMIT=30;
    private static final int VISACODE_LIMIT=20;
    private static final int VISATYPE_LIMIT=30;
    private static final int ID_PROVIDED_LIMIT=30;
    private static final int IDVERIFIED_LIMIT=30;
    private static final String EMPTY_STRING="";
    private static final String WINDOW_TITLE="Add Person";
    
    private CoeusAppletMDIForm mdiForm;
    
    private boolean modified = false;
    private boolean recordAdded=false;
    
    //Added for Person Enhancement Case#1602 - Contact Info Start 1
    private AutoCompleteCoeusCombo cmbState;
    private Vector vecStateData;
    private Vector vecCountryData;
    private char STATE_COUNTRY_DATA = 'P';
    //Added for Person Enhancement Case#1602 - Contact Info End 1 
    
    /** Creates new form AddMITPersonPanel
     * @param mdiForm Parent for the dialog
     */
    public AddMITPersonPanel(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        initComponents();
        
        //Added for Person Enhancement Case#1602 - Contact Info Start 2
        initCustomComponents();
        //Added for Person Enhancement Case#1602 - Contact Info End 2
        
        registerComponents();
        setFormData();
        coeusMessageResources = CoeusMessageResources.getInstance();
        postInitComponents();
    }
    
    /* This method registeres the components and sets the tab order for the components
     */
    private void registerComponents(){
        txtDOB.addFocusListener(new CustomFocusAdapter());
        txtVisaRenewalDate.addFocusListener(new CustomFocusAdapter());
        btnOK.addActionListener(this);
        btnCancel.addActionListener(this);
        /* To set the tab order */
        Component[] comp = {txtId,txtSsn,txtLastName,txtFirstName,txtMidleName,
        txtFullName,txtPriorName,txtUserName,txtEmail,txtDOB,
        txtAge,txtAgeByFiscal,txtGender,txtRace,txtOfficeLocation,
        txtOfficePhone,txtSecOfficeLoc,txtSecOfficePhone,txtSalutation,
        txtDirectoryTitle,txtPrimaryTitle,txtDirectoryDept,txtHomeUnit,
        txtEducationalLevel,txtDegree,txtMajor,txtSchool,txtYearGraduated,
        chkVetran,txtVetranType,chkHandicapped,txtHandicappedType,chkHasVisa,
        txtCitzenship,txtVisaCode,txtVisaType,txtVisaRenewalDate,chkFaculty,
        chkGradStrudentStaff,chkResearchStaff,chkServiceStaff,chkSupportStaff,
        chkOtherAccadem,chkMedicalStaff,chkVecationAccural,chkOnSabatical,
        txtIdProvided,txtIdVerified,txtAddress1,txtAddress2,txtAddress3,
        txtCity,txtCounty,cmbState.getEditor().getEditorComponent(),txtPostalCode,cmbCountry,txtFax,
        txtPager,txtMobile,txtERACommonsUserName,btnOK,btnCancel};
        
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        setFocusTraversalPolicy(traversal);
        setFocusCycleRoot(true);
        
        //Added for Person Enhancement Case#1602 - Contact Info Start 3
        cmbCountry.addItemListener(this);
        //Added for Person Enhancement Case#1602 - Contact Info End 3
    }
    
    
    /** This method creates and sets the display attributes for the dialog
     */
    public void postInitComponents(){
        dlgAddPerson = new CoeusDlgWindow(mdiForm);
        dlgAddPerson.setModal(true);
        dlgAddPerson .getContentPane().add(this);
        dlgAddPerson .setTitle(WINDOW_TITLE);
        dlgAddPerson .setFont(CoeusFontFactory.getLabelFont());
        dlgAddPerson .setModal(true);
        dlgAddPerson.setResizable(false);
        dlgAddPerson.setSize(DLG_WIDTH, DLG_HEIGHT);
        dlgAddPerson.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performCancelAction();
                return;
            }
        });
        dlgAddPerson.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgAddPerson.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we) {
                defaultFocus();
            }
            public void windowClosing(WindowEvent we){
                performCancelAction();
                return;
            }
        });
        dlgAddPerson.setLocation(CoeusDlgWindow.CENTER);
    }
    
    /** To set the default focus for the components */
    private void defaultFocus(){
        txtId.requestFocusInWindow();
    }
    /** To display the dialog .
     *  This method is called from the base window.
     *  Depending on whether the record is added or not, it returns the bean  or null
     * @return departmentPersonFormBean If record is not added returns null
     */
    public DepartmentPersonFormBean display(){
        dlgAddPerson.setVisible(true);
        if (recordAdded) {
            return departmentPersonFormBean;
        } else {
            return null;
        }
    }
    
    /** Action performed for OK button and cancel button */
    public void actionPerformed(ActionEvent  actionEvent){
        Object source = actionEvent.getSource();
        if (source.equals(btnOK)) {
            boolean check=isMandatoryFieldsEntered();
            if (check==false) {
                return;
            }
            setBeanData();
            addPersonDataToServer();
            dlgAddPerson.dispose();
        } //OK
        else if (source.equals(btnCancel)) {
            performCancelAction();
        }//cancel
    }
  /* Performs the action on canel pressed
   */
    private void performCancelAction(){
        boolean isDataSame;
        setBeanData();
        StrictEquals strictEquals = new StrictEquals();
        isDataSame = strictEquals.compare(departmentPersonFormBean,
        new DepartmentPersonFormBean());
        if (!isDataSame ) {  // Checking whether anything  is entered in any of the fields.
            int choice=CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.SELECTION_NO);
            if (choice==CoeusOptionPane.SELECTION_NO) {
                dlgAddPerson.dispose();
            }
            else if (choice==CoeusOptionPane.SELECTION_YES) {
                boolean check=isMandatoryFieldsEntered();
                if (check==false) {
                    return;
                }
                addPersonDataToServer();
                dlgAddPerson.dispose();
            }
        } else {
            dlgAddPerson.setVisible(false);
        }
    }
    
    /** This method sets limits for number of charcters to all the text fields.
     *  This is only done for string fields only.
     */
    private void setFormData() {
        //Added for Person Enhancement Case#1602 - Contact Info Start 4
        CoeusVector cvDataFormServer = getStateAndCountryData();
        if(cvDataFormServer != null){
            vecStateData = (Vector)cvDataFormServer.get(0);
            vecCountryData = (Vector)cvDataFormServer.get(1);
        }
       populateCombo();
       //Added for Person Enhancement Case#1602 - Contact Info End 4
        
        txtSsn.setDocument(new LimitedPlainDocument(SSN_LIMIT));
        txtLastName.setDocument(new LimitedPlainDocument(LASTNAME_LIMIT));
        txtFirstName.setDocument(new LimitedPlainDocument(FIRSTNAME_LIMIT));
        txtMidleName.setDocument(new LimitedPlainDocument(MIDDLENAME_LIMIT));
        txtFullName.setDocument(new LimitedPlainDocument(FULLNAME_LIMIT));
        txtPriorName.setDocument(new LimitedPlainDocument(PRIORNAME_LIMIT));
        txtUserName.setDocument(new LimitedPlainDocument(USERNAME_LIMIT));
        txtEmail.setDocument(new LimitedPlainDocument(EMAIL_LIMIT));
        txtGender.setDocument(new LimitedPlainDocument(GENDER_LIMIT));
        txtRace.setDocument(new LimitedPlainDocument(RACE_LIMIT));
        txtOfficeLocation.setDocument(new LimitedPlainDocument(OFFICELOCATION_LIMIT));
        txtOfficePhone.setDocument(new LimitedPlainDocument(OFFICEPHONE_LIMIT));
        txtSecOfficeLoc.setDocument(new LimitedPlainDocument(SEC_OFFICELOCATION_LIMIT));
        txtSecOfficePhone.setDocument(new LimitedPlainDocument(SEC_OFFICEPHONE_LIMIT));
        txtSalutation.setDocument(new LimitedPlainDocument(SALUTATION_LIMIT));
        txtDirectoryTitle.setDocument(new LimitedPlainDocument(DIRECTORY_TITLE_LIMIT));
        txtPrimaryTitle.setDocument(new LimitedPlainDocument(PRIMARY_TITLE_LIMIT));
        txtDirectoryDept.setDocument(new LimitedPlainDocument(DIRECTORY_DEPT_LIMIT));
        txtHomeUnit.setDocument(new LimitedPlainDocument(HOME_UNIT_LIMIT));
        txtEducationalLevel.setDocument(new LimitedPlainDocument(EDUCATION_LEVEL_LIMIT));
        txtDegree.setDocument(new LimitedPlainDocument(DEGREE_LIMIT));
        txtMajor.setDocument(new LimitedPlainDocument(MAJOR_LIMIT));
        txtSchool.setDocument(new LimitedPlainDocument(SCHOOL_LIMIT));
        txtYearGraduated.setDocument(new LimitedPlainDocument(YEAR_GRADUATED));
        txtVetranType.setDocument(new LimitedPlainDocument(VETRAN_TYPE_LIMIT));
        txtHandicappedType.setDocument(new LimitedPlainDocument(HANDICAPPED_TYPE_LIMIT));
        txtCitzenship.setDocument(new LimitedPlainDocument(COUNTRY_CITIZENSHIP_LIMIT));
        txtVisaCode.setDocument(new LimitedPlainDocument(VISACODE_LIMIT));
        txtVisaType.setDocument(new LimitedPlainDocument(VISATYPE_LIMIT));
        txtIdProvided.setDocument(new LimitedPlainDocument(ID_PROVIDED_LIMIT));
        txtIdVerified.setDocument(new LimitedPlainDocument(IDVERIFIED_LIMIT));
        /*@toDo set the limits of the fields*/
    }
    /** This method is used to sreate the bean and to set data to the bean.
     */
    private void setBeanData() {
        try{
            departmentPersonFormBean=new DepartmentPersonFormBean();
            departmentPersonFormBean.setPersonId((txtId.getText()).trim());
            departmentPersonFormBean.setSsn((txtSsn.getText()).trim());
            departmentPersonFormBean.setLastName((txtLastName.getText()).trim());
            departmentPersonFormBean.setFirstName((txtFirstName.getText()).trim());
            departmentPersonFormBean.setMiddleName((txtMidleName.getText()).trim());
            departmentPersonFormBean.setFullName((txtFullName.getText()).trim());
            departmentPersonFormBean.setPriorName((txtPriorName.getText()).trim());
            departmentPersonFormBean.setUserName((txtUserName.getText()).trim());
            departmentPersonFormBean.setEmailAddress((txtEmail.getText()).trim());
            Date modifiedDobDate;
            String dateValue;
            dateValue = dtUtils.restoreDate(txtDOB.getText(), DATE_SEPARATERS);
            if( dateValue.trim().length() >0 && dateValue != null ){
                modifiedDobDate = dtFormat.parse(dateValue);
                departmentPersonFormBean.setDateOfBirth(
                new java.sql.Date(modifiedDobDate.getTime()));
            }
            
            if (!EMPTY_STRING.equals(txtAge.getText())) {
                departmentPersonFormBean.setAge(
                Integer.parseInt((txtAge.getText()).trim()));
            }
            if (!EMPTY_STRING.equals(txtAgeByFiscal.getText())) {
                departmentPersonFormBean.setAgeByFiscalYear(
                Integer.parseInt((txtAgeByFiscal.getText()).trim()));
            }
            departmentPersonFormBean.setGender((txtGender.getText()).trim());
            departmentPersonFormBean.setRace((txtRace.getText()).trim());
            departmentPersonFormBean.setOfficeLocation((txtOfficeLocation.getText()).trim());
            departmentPersonFormBean.setOfficePhone((txtOfficePhone.getText()).trim());
            departmentPersonFormBean.setSecOfficeLocation((txtSecOfficeLoc.getText()).trim());
            departmentPersonFormBean.setSecOfficePhone((txtSecOfficePhone.getText()).trim());
            departmentPersonFormBean.setSaltuation((txtSalutation.getText()).trim());
            departmentPersonFormBean.setDirTitle((txtDirectoryTitle.getText()).trim());
            departmentPersonFormBean.setPrimaryTitle((txtPrimaryTitle.getText()).trim());
            departmentPersonFormBean.setDirDept((txtDirectoryDept.getText()).trim());
            departmentPersonFormBean.setHomeUnit((txtHomeUnit.getText()).trim());
            departmentPersonFormBean.setEduLevel((txtEducationalLevel.getText()).trim());
            departmentPersonFormBean.setDegree((txtDegree.getText()).trim());
            departmentPersonFormBean.setMajor((txtMajor.getText()).trim());
            departmentPersonFormBean.setSchool((txtSchool.getText()).trim());
            departmentPersonFormBean.setYearGraduated((txtYearGraduated.getText()).trim());
            
            departmentPersonFormBean.setVeteran(chkVetran.isSelected());
            departmentPersonFormBean.setVeteranType((txtVetranType.getText()).trim());
            departmentPersonFormBean.setHandicap(chkHandicapped.isSelected());
            departmentPersonFormBean.setHandiCapType(
            (txtHandicappedType.getText()).trim());
            departmentPersonFormBean.setHasVisa(chkHasVisa.isSelected());
            departmentPersonFormBean.setCountryCitizenship(
            (txtCitzenship.getText()).trim());
            departmentPersonFormBean.setVisaCode((txtVisaCode.getText()).trim());
            departmentPersonFormBean.setVisaType((txtVisaType.getText()).trim());
            Date modifiedRenewalDate;
            dateValue = dtUtils.restoreDate(
            txtVisaRenewalDate.getText(), DATE_SEPARATERS);
            if (dateValue.trim().length() >0 && dateValue != null) {
                modifiedRenewalDate = dtFormat.parse(dateValue);
                departmentPersonFormBean.setVisaRenDate(
                new java.sql.Date(modifiedRenewalDate.getTime()));
            }
            departmentPersonFormBean.setFaculty(chkFaculty.isSelected());
            departmentPersonFormBean.setGraduateStudentStaff(
            chkGradStrudentStaff.isSelected());
            departmentPersonFormBean.setResearchStaff(chkResearchStaff.isSelected());
            departmentPersonFormBean.setServiceStaff(chkServiceStaff.isSelected());
            departmentPersonFormBean.setSupportStaff(chkSupportStaff.isSelected());
            departmentPersonFormBean.setOtherAcademicGroup(chkOtherAccadem.isSelected());
            departmentPersonFormBean.setMedicalStaff(chkMedicalStaff.isSelected());
            departmentPersonFormBean.setVacationAccural(chkVecationAccural.isSelected());
            departmentPersonFormBean.setOnSabbatical(chkOnSabatical.isSelected());
            departmentPersonFormBean.setProvided((txtIdProvided.getText()).trim());
            departmentPersonFormBean.setVerified((txtIdVerified.getText()).trim());
            modified = true;
            
            //Added for Person Enhancement Case#1602 - Contact Info Start 5
            
            departmentPersonFormBean.setAddress1(txtAddress1.getText().trim());
            departmentPersonFormBean.setAddress2(txtAddress2.getText().trim());
            departmentPersonFormBean.setAddress3(txtAddress3.getText().trim());
            departmentPersonFormBean.setCity(txtCity.getText().trim());
            departmentPersonFormBean.setCounty(txtCounty.getText().trim());
            departmentPersonFormBean.setPostalCode(txtPostalCode.getText().trim());
            departmentPersonFormBean.setFaxNumber(txtFax.getText().trim());
            departmentPersonFormBean.setPagerNumber(txtPager.getText().trim());
            departmentPersonFormBean.setMobilePhNumber(txtMobile.getText().trim());
            departmentPersonFormBean.setEraCommonsUsrName(txtERACommonsUserName.getText().trim());
            
            ComboBoxBean cmbCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
            departmentPersonFormBean.setCountryCode(cmbCountryBean.getCode());
            
            ComboBoxBean cmbStateBean = (ComboBoxBean)cmbState.getSelectedItem();
            if(cmbCountryBean.getCode().equalsIgnoreCase("USA")){
                departmentPersonFormBean.setState(cmbStateBean.getCode());
            }else{
                JTextField txtState = (JTextField)cmbState.getEditor().getEditorComponent();
                departmentPersonFormBean.setState(txtState.getText().trim());
            }
            //Added for Person Enhancement Case#1602 - Contact Info End 5
            
        } catch(Exception exception) {
            exception.printStackTrace();
        }
        
    }
    /** This is an inner class which is used to set the focus settings
     */
    public class  CustomFocusAdapter extends FocusAdapter {
        public void focusGained(FocusEvent focusEvent) {
            if (focusEvent.isTemporary()) {
                return ;
            }
            //sets the format of date to mm/dd/yy on focus
            if (focusEvent.getSource()== txtDOB){
                String strDate = txtDOB.getText().trim();
                strDate = dtUtils.restoreDate(txtDOB.getText(), DATE_SEPARATERS);
                txtDOB.setText(strDate);
            }
            else if(focusEvent.getSource()== txtVisaRenewalDate){
                String strDate = txtVisaRenewalDate.getText().trim();
                strDate = dtUtils.restoreDate(
                txtVisaRenewalDate.getText(), DATE_SEPARATERS);
                txtVisaRenewalDate.setText(strDate);
            }
        }
        
        public void focusLost(FocusEvent focusEvent) {
            try {
                //sets the format of date to dd-mmm-yyyy when focus is lost
                if ( focusEvent.getSource()== txtDOB) {
                    if (txtDOB.getText().trim().length() > 0 ) {
                        String strDate = txtDOB.getText().trim();
                        String convertedDate = dtUtils.formatDate(
                        strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
                        convertedDate = dtUtils.restoreDate(convertedDate, DATE_SEPARATERS);
                        if (convertedDate==null) {
                            txtDOB.setText(EMPTY_STRING);
                            txtDOB.requestFocusInWindow();
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1100"));
                            
                            return;
                        } else {
                            String focusDate = dtUtils.formatDate(
                            strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                            txtDOB.setText(focusDate);
                        }
                    }
                }
                else if ( focusEvent.getSource()== txtVisaRenewalDate) {
                    if (txtVisaRenewalDate.getText().trim().length() > 0 ) {
                        String strDate = txtVisaRenewalDate.getText().trim();
                        String convertedDate = dtUtils.formatDate(
                        strDate, DATE_SEPARATERS ,REQUIRED_DATEFORMAT);
                        convertedDate = dtUtils.restoreDate(convertedDate, DATE_SEPARATERS);
                        if (convertedDate==null) {
                            txtVisaRenewalDate.setText(EMPTY_STRING);
                            txtVisaRenewalDate.requestFocusInWindow();
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1100"));
                            
                            return;
                        } else {
                            String focusDate = dtUtils.formatDate(
                            strDate,DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                            txtVisaRenewalDate.setText(focusDate);
                        }
                    }
                }
            }catch(Exception exception){
                exception.printStackTrace();
            }
        }
    }
    
    /** This method is to check whether all the mandatory fields are entered or not.
     *  This method also checks whether the ID entered, already exists or not.
     */
    private boolean isMandatoryFieldsEntered(){
        if (EMPTY_STRING.equals(((txtId.getText()).trim()))) {
            
            txtId.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1102"));
            txtId.requestFocusInWindow();
            
            return false;
        }
        if (EMPTY_STRING.equals((txtLastName.getText()).trim())) {
            txtLastName.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1103"));
            txtLastName.requestFocusInWindow();
            
            return false;
        }
        if (EMPTY_STRING.equals((txtFirstName.getText()).trim())) {
            txtFirstName.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1104"));
            txtFirstName.requestFocusInWindow();
            
            return false;
        }
        if (EMPTY_STRING.equals((txtFullName.getText()).trim())) {
            txtFullName.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1105"));
            txtFullName.requestFocusInWindow();
            return false;
        }
        if (EMPTY_STRING.equals((txtOfficeLocation.getText()).trim())) {
            txtOfficeLocation.requestFocusInWindow();
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1106"));
            txtOfficeLocation.requestFocusInWindow();
            
            return false;
        }
        
        //Added for Person Enhancement Case#1602 - Contact Info Start
        ComboBoxBean cmbCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
        if(cmbCountryBean.getCode().equalsIgnoreCase("USA")){
            String strState = ((JTextField)cmbState.getEditor().getEditorComponent()).getText();
            if(strState != null && strState.trim().equals("")){
               cmbState.getEditor().getEditorComponent().requestFocusInWindow();
               CoeusOptionPane.showInfoDialog(
               coeusMessageResources.parseMessageKey("roldxMntDetFrm_exceptionCode.1105"));
               cmbState.getEditor().getEditorComponent().requestFocusInWindow();
               return false;
            }
         }
        //Added for Person Enhancement Case#1602 - Contact Info End
        
        dlgAddPerson.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        boolean isduplicate=isDuplicateId(); // Check whether the Id already exists or not
        dlgAddPerson.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        if (isduplicate) {
            txtId.requestFocusInWindow();
            CoeusOptionPane.showErrorDialog(
            coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1101"));
            txtId.requestFocusInWindow();
            return false;
        }
        
        return true;
    }
    
    
    /** This method is used to check whether the ID entered is already there or not.
     *  It  sends a request to the server and if it is already there it will return true.
     */
    private boolean isDuplicateId(){
        RequesterBean request = new RequesterBean();
        request.setFunctionType(DUPLICATE_CHK_FN_TYPE);
        
        request.setDataObject((txtId.getText()).trim());
        comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        boolean isDuplicate=((Boolean)response.getDataObject()).booleanValue();
        return isDuplicate;
    }
    
   /* This method adds a record to the database.
    * If successfully added it sets the recordAdded flag to true.
    */
    
    private void addPersonDataToServer() {
        dlgAddPerson.setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        RequesterBean request = new RequesterBean();
        request.setFunctionType(ADD_PERSON_FN_TYPE);
        departmentPersonFormBean.setAcType(AC_TYPE);
        request.setDataObject(departmentPersonFormBean);
        comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (!response.isSuccessfulResponse()) {
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        else {
            recordAdded=true;
        }
        dlgAddPerson.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
    }
          
    //Added for Person Enhancement Case#1602 - Contact Info Start 6
    /*Gets the  data for the state and the country combobox
     *from the server
     */
    private CoeusVector getStateAndCountryData(){
        RequesterBean request = new RequesterBean();
        CoeusVector cvDataFromSrv = new CoeusVector();
        request.setFunctionType(STATE_COUNTRY_DATA);
        comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        
        try{
            if(response.hasResponse()){
                cvDataFromSrv =(CoeusVector)response.getDataObject();
            }
        }catch (CoeusException ce){
            ce.printStackTrace();
        }
        
        return cvDataFromSrv;
    }
   
    /*Populates the country combobox
     */
    private void populateCombo(){
        
        for(int index = 0 ; index < vecCountryData.size(); index ++){
            ComboBoxBean cmbCountryBean = (ComboBoxBean)vecCountryData.get(index);
            cmbCountry.addItem(cmbCountryBean);
        }
        ComboBoxBean comboBoxBean = new ComboBoxBean("USA","United States");
        cmbCountry.setSelectedItem(comboBoxBean);
      
    }
    
    /*Populates the state combobox
     */
    private void populateStateCombo(){
        cmbState.removeAllItems();
        for(int index = 0 ; index < vecStateData.size(); index ++){
            ComboBoxBean cmbStateBean = (ComboBoxBean)vecStateData.get(index);
            cmbState.addItem(cmbStateBean);
        }
    }
    
    /*Used for initilization of the custom component
     *AutoCompleteCoeusCombo and for setting 
     *the field size
     */
    
    private void initCustomComponents(){
        
        DefaultComboBoxModel defaultComboBoxModel = new DefaultComboBoxModel();
        cmbState = new AutoCompleteCoeusCombo(defaultComboBoxModel);
        cmbState.setAutoCompleteOnFocusLost(false);
        cmbState.setEditable(true);
        
        java.awt.GridBagConstraints gridBagConstraints;
        cmbState.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlContactInfo.add(cmbState, gridBagConstraints);
        
        final JTextField txtState =(JTextField) cmbState.getEditor().getEditorComponent();
        
        /*@todo Add this if you want to keep a flag for is modified*/
        //txtState.addKeyListener(new CoeusTextListener());
        
        txtState.addFocusListener( new FocusAdapter(){
            public void focusGained( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary()){
                    txtState.selectAll();
                }
            }
            public void focusLost( FocusEvent focusEvt ){
                if( !focusEvt.isTemporary()){
                    String txtEntered = cmbState.getEditor().getItem().toString();
                    checkForCode(txtEntered);
                }
            }
        });
        
        
        txtAddress1.setDocument(new LimitedPlainDocument(80));
        txtAddress2.setDocument(new LimitedPlainDocument(80));
        txtAddress3.setDocument(new LimitedPlainDocument(80));
        txtCity.setDocument(new LimitedPlainDocument(30));
        txtCounty.setDocument(new LimitedPlainDocument(30));
        txtPostalCode.setDocument(new LimitedPlainDocument(15));
        txtFax.setDocument(new LimitedPlainDocument(20));
        txtPager.setDocument(new LimitedPlainDocument(20));
        txtMobile.setDocument(new LimitedPlainDocument(20));
         // Increased the size from 12 to 20. 
        txtERACommonsUserName.setDocument(new LimitedPlainDocument(20));

        txtState.setDocument(new LimitedPlainDocument(30));
    }
    
    /*Checks for the code of the entered state.
     *This is a suppourting method for AutoCompleteCoeusCombo
     */
    private void checkForCode(String txtEntered){
        boolean inside = false;
        if(vecStateData != null && vecStateData.size()>0){
            for(int index = 0; index < vecStateData.size();index++){
                ComboBoxBean stateComboBean = (ComboBoxBean)vecStateData.get(index);
                inside = true;
                if(txtEntered.equalsIgnoreCase(stateComboBean.getCode()) ||
                txtEntered.equalsIgnoreCase(stateComboBean.getDescription())){
                    //cmbState.getEditor().setItem(stateComboBean.getDescription());
                    cmbState.setSelectedItem(stateComboBean);
                    return ;
                }
            }
        }//End of if
        
        if(inside){
            ComboBoxBean comboBean = new ComboBoxBean("",txtEntered);
            cmbState.setSelectedItem(comboBean);
            
        }
    }
    //Added for Person Enhancement Case#1602 - Contact Info End 6
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrpnAddPersonDetails = new javax.swing.JScrollPane();
        pnlAddPersondetails = new javax.swing.JPanel();
        lblID = new javax.swing.JLabel();
        lblSSn = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        lblFirstName = new javax.swing.JLabel();
        lblMiddleName = new javax.swing.JLabel();
        lblFullName = new javax.swing.JLabel();
        lblPriorName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblDOB = new javax.swing.JLabel();
        lblAge = new javax.swing.JLabel();
        lblAgeByFiscal = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        sptrPersonalDetails = new javax.swing.JSeparator();
        lblRace = new javax.swing.JLabel();
        sptrOfficialDetails = new javax.swing.JSeparator();
        sptrEducational = new javax.swing.JSeparator();
        txtAge = new edu.mit.coeus.utils.CoeusTextField();
        txtAgeByFiscal = new edu.mit.coeus.utils.CoeusTextField();
        txtId = new edu.mit.coeus.utils.CoeusTextField();
        txtSsn = new edu.mit.coeus.utils.CoeusTextField();
        txtLastName = new edu.mit.coeus.utils.CoeusTextField();
        txtFirstName = new edu.mit.coeus.utils.CoeusTextField();
        txtMidleName = new edu.mit.coeus.utils.CoeusTextField();
        txtFullName = new edu.mit.coeus.utils.CoeusTextField();
        txtPriorName = new edu.mit.coeus.utils.CoeusTextField();
        txtUserName = new edu.mit.coeus.utils.CoeusTextField();
        txtEmail = new edu.mit.coeus.utils.CoeusTextField();
        txtDOB = new edu.mit.coeus.utils.CoeusTextField();
        txtGender = new edu.mit.coeus.utils.CoeusTextField();
        txtRace = new edu.mit.coeus.utils.CoeusTextField();
        pnlOfficeDetails = new javax.swing.JPanel();
        lblOfficeLocation = new javax.swing.JLabel();
        txtOfficeLocation = new edu.mit.coeus.utils.CoeusTextField();
        lblSecOfficeLoc = new javax.swing.JLabel();
        txtSecOfficeLoc = new javax.swing.JTextField();
        lblSalutaion = new javax.swing.JLabel();
        txtSalutation = new javax.swing.JTextField();
        lblDirectoryTitle = new javax.swing.JLabel();
        txtDirectoryTitle = new javax.swing.JTextField();
        lblPrimaryTitle = new javax.swing.JLabel();
        txtPrimaryTitle = new javax.swing.JTextField();
        lblDirectoryDept = new javax.swing.JLabel();
        txtDirectoryDept = new javax.swing.JTextField();
        lblOfficePhone = new javax.swing.JLabel();
        lblSecOfficePhone = new javax.swing.JLabel();
        txtOfficePhone = new javax.swing.JTextField();
        txtSecOfficePhone = new javax.swing.JTextField();
        lblHomeUnit = new javax.swing.JLabel();
        txtHomeUnit = new javax.swing.JTextField();
        pnlEducation = new javax.swing.JPanel();
        chkVetran = new javax.swing.JCheckBox();
        txtVisaRenewalDate = new javax.swing.JTextField();
        txtCitzenship = new javax.swing.JTextField();
        lblVisaCode = new javax.swing.JLabel();
        txtMajor = new javax.swing.JTextField();
        txtEducationalLevel = new javax.swing.JTextField();
        lblMajor = new javax.swing.JLabel();
        txtDegree = new javax.swing.JTextField();
        lblVisaType = new javax.swing.JLabel();
        lblCitizenship = new javax.swing.JLabel();
        lblYearGraduated = new javax.swing.JLabel();
        lblVisaRenewalDate = new javax.swing.JLabel();
        lblHandicappedType = new javax.swing.JLabel();
        txtVisaCode = new javax.swing.JTextField();
        txtSchool = new javax.swing.JTextField();
        txtVisaType = new javax.swing.JTextField();
        lblSchool = new javax.swing.JLabel();
        txtYearGraduated = new javax.swing.JTextField();
        chkHasVisa = new javax.swing.JCheckBox();
        txtVetranType = new javax.swing.JTextField();
        lblEducationalLevel = new javax.swing.JLabel();
        lblVetranType = new javax.swing.JLabel();
        txtHandicappedType = new javax.swing.JTextField();
        chkHandicapped = new javax.swing.JCheckBox();
        lblDegree = new javax.swing.JLabel();
        sptrFaculty = new javax.swing.JSeparator();
        chkFaculty = new javax.swing.JCheckBox();
        chkGradStrudentStaff = new javax.swing.JCheckBox();
        chkResearchStaff = new javax.swing.JCheckBox();
        chkServiceStaff = new javax.swing.JCheckBox();
        chkSupportStaff = new javax.swing.JCheckBox();
        chkOtherAccadem = new javax.swing.JCheckBox();
        chkMedicalStaff = new javax.swing.JCheckBox();
        chkVecationAccural = new javax.swing.JCheckBox();
        chkOnSabatical = new javax.swing.JCheckBox();
        lblIdProvided = new javax.swing.JLabel();
        txtIdProvided = new javax.swing.JTextField();
        lblIdVerified = new javax.swing.JLabel();
        txtIdVerified = new javax.swing.JTextField();
        pnlContactInfo = new javax.swing.JPanel();
        lblAdderss1 = new javax.swing.JLabel();
        lblAddress2 = new javax.swing.JLabel();
        lblAddress3 = new javax.swing.JLabel();
        txtAddress1 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress2 = new edu.mit.coeus.utils.CoeusTextField();
        txtAddress3 = new edu.mit.coeus.utils.CoeusTextField();
        lblCity = new javax.swing.JLabel();
        lblCounty = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        lblPostalCode = new javax.swing.JLabel();
        txtCity = new edu.mit.coeus.utils.CoeusTextField();
        txtCounty = new edu.mit.coeus.utils.CoeusTextField();
        cmbCountry = new edu.mit.coeus.utils.CoeusComboBox();
        txtPostalCode = new edu.mit.coeus.utils.CoeusTextField();
        lblCountry = new javax.swing.JLabel();
        lblFax = new javax.swing.JLabel();
        lblPager = new javax.swing.JLabel();
        lblMobile = new javax.swing.JLabel();
        txtFax = new edu.mit.coeus.utils.CoeusTextField();
        txtPager = new edu.mit.coeus.utils.CoeusTextField();
        txtMobile = new edu.mit.coeus.utils.CoeusTextField();
        lblERACommonsUserName = new javax.swing.JLabel();
        txtERACommonsUserName = new edu.mit.coeus.utils.CoeusTextField();
        sptrPersonalDetails1 = new javax.swing.JSeparator();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        setAlignmentX(0.0F);
        setPreferredSize(new java.awt.Dimension(785, 610));
        scrpnAddPersonDetails.setMinimumSize(new java.awt.Dimension(710, 475));
        scrpnAddPersonDetails.setPreferredSize(new java.awt.Dimension(770, 500));
        pnlAddPersondetails.setLayout(new java.awt.GridBagLayout());

        lblID.setFont(CoeusFontFactory.getLabelFont());
        lblID.setText("Id :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlAddPersondetails.add(lblID, gridBagConstraints);

        lblSSn.setFont(CoeusFontFactory.getLabelFont());
        lblSSn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSSn.setText("Ssn :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 5, 5);
        pnlAddPersondetails.add(lblSSn, gridBagConstraints);

        lblLastName.setFont(CoeusFontFactory.getLabelFont());
        lblLastName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastName.setText("Last Name :");
        lblLastName.setAlignmentX(0.5F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblLastName, gridBagConstraints);

        lblFirstName.setFont(CoeusFontFactory.getLabelFont());
        lblFirstName.setText(" First Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlAddPersondetails.add(lblFirstName, gridBagConstraints);

        lblMiddleName.setFont(CoeusFontFactory.getLabelFont());
        lblMiddleName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMiddleName.setText("Middle : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(lblMiddleName, gridBagConstraints);

        lblFullName.setFont(CoeusFontFactory.getLabelFont());
        lblFullName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFullName.setText("Full Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblFullName, gridBagConstraints);

        lblPriorName.setFont(CoeusFontFactory.getLabelFont());
        lblPriorName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPriorName.setText("Prior Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        pnlAddPersondetails.add(lblPriorName, gridBagConstraints);

        lblUserName.setFont(CoeusFontFactory.getLabelFont());
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserName.setText("User Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblUserName, gridBagConstraints);

        lblEmail.setFont(CoeusFontFactory.getLabelFont());
        lblEmail.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEmail.setText("Email :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblEmail, gridBagConstraints);

        lblDOB.setFont(CoeusFontFactory.getLabelFont());
        lblDOB.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDOB.setText("Date of Birth :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblDOB, gridBagConstraints);

        lblAge.setFont(CoeusFontFactory.getLabelFont());
        lblAge.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAge.setText("Age :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlAddPersondetails.add(lblAge, gridBagConstraints);

        lblAgeByFiscal.setFont(CoeusFontFactory.getLabelFont());
        lblAgeByFiscal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAgeByFiscal.setText("Age by Fiscal Year : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 90, 0, 0);
        pnlAddPersondetails.add(lblAgeByFiscal, gridBagConstraints);

        lblGender.setFont(CoeusFontFactory.getLabelFont());
        lblGender.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGender.setText("Gender :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblGender, gridBagConstraints);

        sptrPersonalDetails.setBackground(new java.awt.Color(102, 102, 102));
        sptrPersonalDetails.setForeground(new java.awt.Color(102, 102, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(sptrPersonalDetails, gridBagConstraints);

        lblRace.setFont(CoeusFontFactory.getLabelFont());
        lblRace.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRace.setText("Race :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlAddPersondetails.add(lblRace, gridBagConstraints);

        sptrOfficialDetails.setBackground(new java.awt.Color(102, 102, 102));
        sptrOfficialDetails.setForeground(new java.awt.Color(102, 102, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(sptrOfficialDetails, gridBagConstraints);

        sptrEducational.setBackground(new java.awt.Color(102, 102, 102));
        sptrEducational.setForeground(new java.awt.Color(102, 102, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(sptrEducational, gridBagConstraints);

        txtAge.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAge.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtAge, gridBagConstraints);

        txtAgeByFiscal.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAgeByFiscal.setFont(CoeusFontFactory.getNormalFont());
        txtAgeByFiscal.setPreferredSize(new java.awt.Dimension(117, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAddPersondetails.add(txtAgeByFiscal, gridBagConstraints);

        txtId.setBackground(new java.awt.Color(255, 255, 0));
        txtId.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,9)
        );
        txtId.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlAddPersondetails.add(txtId, gridBagConstraints);

        txtSsn.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        pnlAddPersondetails.add(txtSsn, gridBagConstraints);

        txtLastName.setBackground(new java.awt.Color(255, 255, 0));
        txtLastName.setFont(CoeusFontFactory.getNormalFont());
        txtLastName.setMinimumSize(new java.awt.Dimension(200, 20));
        txtLastName.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlAddPersondetails.add(txtLastName, gridBagConstraints);

        txtFirstName.setBackground(new java.awt.Color(255, 255, 0));
        txtFirstName.setFont(CoeusFontFactory.getNormalFont());
        txtFirstName.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtFirstName, gridBagConstraints);

        txtMidleName.setFont(CoeusFontFactory.getNormalFont());
        txtMidleName.setPreferredSize(new java.awt.Dimension(117, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAddPersondetails.add(txtMidleName, gridBagConstraints);

        txtFullName.setBackground(new java.awt.Color(255, 255, 0));
        txtFullName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtFullName, gridBagConstraints);

        txtPriorName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtPriorName, gridBagConstraints);

        txtUserName.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtUserName, gridBagConstraints);

        txtEmail.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtEmail, gridBagConstraints);

        txtDOB.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtDOB, gridBagConstraints);

        txtGender.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtGender, gridBagConstraints);

        txtRace.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(txtRace, gridBagConstraints);

        pnlOfficeDetails.setLayout(new java.awt.GridBagLayout());

        lblOfficeLocation.setFont(CoeusFontFactory.getLabelFont());
        lblOfficeLocation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOfficeLocation.setText("Office Location :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblOfficeLocation, gridBagConstraints);

        txtOfficeLocation.setBackground(new java.awt.Color(255, 255, 0));
        txtOfficeLocation.setFont(CoeusFontFactory.getNormalFont());
        txtOfficeLocation.setMinimumSize(new java.awt.Dimension(200, 20));
        txtOfficeLocation.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtOfficeLocation, gridBagConstraints);

        lblSecOfficeLoc.setFont(CoeusFontFactory.getLabelFont());
        lblSecOfficeLoc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSecOfficeLoc.setText("Secondary Office Location :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblSecOfficeLoc, gridBagConstraints);

        txtSecOfficeLoc.setFont(CoeusFontFactory.getNormalFont());
        txtSecOfficeLoc.setMinimumSize(new java.awt.Dimension(200, 20));
        txtSecOfficeLoc.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtSecOfficeLoc, gridBagConstraints);

        lblSalutaion.setFont(CoeusFontFactory.getLabelFont());
        lblSalutaion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalutaion.setText("Salutation :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblSalutaion, gridBagConstraints);

        txtSalutation.setFont(CoeusFontFactory.getNormalFont());
        txtSalutation.setMinimumSize(new java.awt.Dimension(200, 20));
        txtSalutation.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtSalutation, gridBagConstraints);

        lblDirectoryTitle.setFont(CoeusFontFactory.getLabelFont());
        lblDirectoryTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDirectoryTitle.setText("Directory Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblDirectoryTitle, gridBagConstraints);

        txtDirectoryTitle.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtDirectoryTitle, gridBagConstraints);

        lblPrimaryTitle.setFont(CoeusFontFactory.getLabelFont());
        lblPrimaryTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimaryTitle.setText("Primary Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblPrimaryTitle, gridBagConstraints);

        txtPrimaryTitle.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtPrimaryTitle, gridBagConstraints);

        lblDirectoryDept.setFont(CoeusFontFactory.getLabelFont());
        lblDirectoryDept.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDirectoryDept.setText("Directory Department :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblDirectoryDept, gridBagConstraints);

        txtDirectoryDept.setFont(CoeusFontFactory.getNormalFont());
        txtDirectoryDept.setMinimumSize(new java.awt.Dimension(200, 20));
        txtDirectoryDept.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtDirectoryDept, gridBagConstraints);

        lblOfficePhone.setFont(CoeusFontFactory.getLabelFont());
        lblOfficePhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOfficePhone.setText("Office Phone :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlOfficeDetails.add(lblOfficePhone, gridBagConstraints);

        lblSecOfficePhone.setFont(CoeusFontFactory.getLabelFont());
        lblSecOfficePhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSecOfficePhone.setText("Secondary Office Phone :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 140, 0, 5);
        pnlOfficeDetails.add(lblSecOfficePhone, gridBagConstraints);

        txtOfficePhone.setFont(CoeusFontFactory.getNormalFont());
        txtOfficePhone.setMinimumSize(new java.awt.Dimension(150, 20));
        txtOfficePhone.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtOfficePhone, gridBagConstraints);

        txtSecOfficePhone.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlOfficeDetails.add(txtSecOfficePhone, gridBagConstraints);

        lblHomeUnit.setFont(CoeusFontFactory.getLabelFont());
        lblHomeUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHomeUnit.setText("Home Unit :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlOfficeDetails.add(lblHomeUnit, gridBagConstraints);

        txtHomeUnit.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlOfficeDetails.add(txtHomeUnit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAddPersondetails.add(pnlOfficeDetails, gridBagConstraints);

        pnlEducation.setLayout(new java.awt.GridBagLayout());

        chkVetran.setFont(CoeusFontFactory.getLabelFont());
        chkVetran.setText("Veteran");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEducation.add(chkVetran, gridBagConstraints);

        txtVisaRenewalDate.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(txtVisaRenewalDate, gridBagConstraints);

        txtCitzenship.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(txtCitzenship, gridBagConstraints);

        lblVisaCode.setFont(CoeusFontFactory.getLabelFont());
        lblVisaCode.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVisaCode.setText("Visa Code :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblVisaCode, gridBagConstraints);

        txtMajor.setFont(CoeusFontFactory.getNormalFont());
        txtMajor.setMinimumSize(new java.awt.Dimension(100, 20));
        txtMajor.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(txtMajor, gridBagConstraints);

        txtEducationalLevel.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(txtEducationalLevel, gridBagConstraints);

        lblMajor.setFont(CoeusFontFactory.getLabelFont());
        lblMajor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMajor.setText("Major :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblMajor, gridBagConstraints);

        txtDegree.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(txtDegree, gridBagConstraints);

        lblVisaType.setFont(CoeusFontFactory.getLabelFont());
        lblVisaType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVisaType.setText("Visa Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblVisaType, gridBagConstraints);

        lblCitizenship.setFont(CoeusFontFactory.getLabelFont());
        lblCitizenship.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCitizenship.setText("Country of Citizenship :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblCitizenship, gridBagConstraints);

        lblYearGraduated.setFont(CoeusFontFactory.getLabelFont());
        lblYearGraduated.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblYearGraduated.setText("Year Graduated :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblYearGraduated, gridBagConstraints);

        lblVisaRenewalDate.setFont(CoeusFontFactory.getLabelFont());
        lblVisaRenewalDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVisaRenewalDate.setText("Visa Renewal Date :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblVisaRenewalDate, gridBagConstraints);

        lblHandicappedType.setFont(CoeusFontFactory.getLabelFont());
        lblHandicappedType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHandicappedType.setText("Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblHandicappedType, gridBagConstraints);

        txtVisaCode.setFont(CoeusFontFactory.getNormalFont());
        txtVisaCode.setMinimumSize(new java.awt.Dimension(50, 20));
        txtVisaCode.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(txtVisaCode, gridBagConstraints);

        txtSchool.setFont(CoeusFontFactory.getNormalFont());
        txtSchool.setPreferredSize(new java.awt.Dimension(130, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(txtSchool, gridBagConstraints);

        txtVisaType.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEducation.add(txtVisaType, gridBagConstraints);

        lblSchool.setFont(CoeusFontFactory.getLabelFont());
        lblSchool.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSchool.setText("School :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblSchool, gridBagConstraints);

        txtYearGraduated.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEducation.add(txtYearGraduated, gridBagConstraints);

        chkHasVisa.setFont(CoeusFontFactory.getLabelFont());
        chkHasVisa.setText("Has Visa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(chkHasVisa, gridBagConstraints);

        txtVetranType.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(txtVetranType, gridBagConstraints);

        lblEducationalLevel.setFont(CoeusFontFactory.getLabelFont());
        lblEducationalLevel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEducationalLevel.setText("Educational Level :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblEducationalLevel, gridBagConstraints);

        lblVetranType.setFont(CoeusFontFactory.getLabelFont());
        lblVetranType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVetranType.setText("Type :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblVetranType, gridBagConstraints);

        txtHandicappedType.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEducation.add(txtHandicappedType, gridBagConstraints);

        chkHandicapped.setFont(CoeusFontFactory.getLabelFont());
        chkHandicapped.setText("Handicapped ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        pnlEducation.add(chkHandicapped, gridBagConstraints);

        lblDegree.setFont(CoeusFontFactory.getLabelFont());
        lblDegree.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDegree.setText("Degree :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        pnlEducation.add(lblDegree, gridBagConstraints);

        sptrFaculty.setBackground(new java.awt.Color(102, 102, 102));
        sptrFaculty.setForeground(new java.awt.Color(102, 102, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlEducation.add(sptrFaculty, gridBagConstraints);

        chkFaculty.setFont(CoeusFontFactory.getLabelFont());
        chkFaculty.setText(" Faculty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkFaculty, gridBagConstraints);

        chkGradStrudentStaff.setFont(CoeusFontFactory.getLabelFont());
        chkGradStrudentStaff.setText(" Grad Student Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkGradStrudentStaff, gridBagConstraints);

        chkResearchStaff.setFont(CoeusFontFactory.getLabelFont());
        chkResearchStaff.setText(" Research Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkResearchStaff, gridBagConstraints);

        chkServiceStaff.setFont(CoeusFontFactory.getLabelFont());
        chkServiceStaff.setText(" Service Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        pnlEducation.add(chkServiceStaff, gridBagConstraints);

        chkSupportStaff.setFont(CoeusFontFactory.getLabelFont());
        chkSupportStaff.setText(" Support Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkSupportStaff, gridBagConstraints);

        chkOtherAccadem.setFont(CoeusFontFactory.getLabelFont());
        chkOtherAccadem.setText(" Other Accadem");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkOtherAccadem, gridBagConstraints);

        chkMedicalStaff.setFont(CoeusFontFactory.getLabelFont());
        chkMedicalStaff.setText(" Medical Staff");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkMedicalStaff, gridBagConstraints);

        chkVecationAccural.setFont(CoeusFontFactory.getLabelFont());
        chkVecationAccural.setText(" Vacation Accural");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 25, 0, 0);
        pnlEducation.add(chkVecationAccural, gridBagConstraints);

        chkOnSabatical.setFont(CoeusFontFactory.getLabelFont());
        chkOnSabatical.setText(" On Sabbatical");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(chkOnSabatical, gridBagConstraints);

        lblIdProvided.setFont(CoeusFontFactory.getLabelFont());
        lblIdProvided.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIdProvided.setText("Id Provided :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblIdProvided, gridBagConstraints);

        txtIdProvided.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlEducation.add(txtIdProvided, gridBagConstraints);

        lblIdVerified.setFont(CoeusFontFactory.getLabelFont());
        lblIdVerified.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIdVerified.setText("Id Verified :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        pnlEducation.add(lblIdVerified, gridBagConstraints);

        txtIdVerified.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlEducation.add(txtIdVerified, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAddPersondetails.add(pnlEducation, gridBagConstraints);

        pnlContactInfo.setLayout(new java.awt.GridBagLayout());

        lblAdderss1.setFont(CoeusFontFactory.getLabelFont());
        lblAdderss1.setText("Address 1: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pnlContactInfo.add(lblAdderss1, gridBagConstraints);

        lblAddress2.setFont(CoeusFontFactory.getLabelFont());
        lblAddress2.setText("Address 2: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblAddress2, gridBagConstraints);

        lblAddress3.setFont(CoeusFontFactory.getLabelFont());
        lblAddress3.setText("Address 3: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblAddress3, gridBagConstraints);

        txtAddress1.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 8);
        pnlContactInfo.add(txtAddress1, gridBagConstraints);

        txtAddress2.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtAddress2, gridBagConstraints);

        txtAddress3.setFont(CoeusFontFactory.getNormalFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtAddress3, gridBagConstraints);

        lblCity.setFont(CoeusFontFactory.getLabelFont());
        lblCity.setText("City: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblCity, gridBagConstraints);

        lblCounty.setFont(CoeusFontFactory.getLabelFont());
        lblCounty.setText("County: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblCounty, gridBagConstraints);

        lblState.setFont(CoeusFontFactory.getLabelFont());
        lblState.setText("State: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblState, gridBagConstraints);

        lblPostalCode.setFont(CoeusFontFactory.getLabelFont());
        lblPostalCode.setText("Postal Code: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblPostalCode, gridBagConstraints);

        txtCity.setFont(CoeusFontFactory.getNormalFont());
        txtCity.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(txtCity, gridBagConstraints);

        txtCounty.setFont(CoeusFontFactory.getNormalFont());
        txtCounty.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtCounty, gridBagConstraints);

        cmbCountry.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(cmbCountry, gridBagConstraints);

        txtPostalCode.setFont(CoeusFontFactory.getNormalFont());
        txtPostalCode.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtPostalCode, gridBagConstraints);

        lblCountry.setFont(CoeusFontFactory.getLabelFont());
        lblCountry.setText("Country: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblCountry, gridBagConstraints);

        lblFax.setFont(CoeusFontFactory.getLabelFont());
        lblFax.setText("Fax: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblFax, gridBagConstraints);

        lblPager.setFont(CoeusFontFactory.getLabelFont());
        lblPager.setText("Pager: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblPager, gridBagConstraints);

        lblMobile.setFont(CoeusFontFactory.getLabelFont());
        lblMobile.setText("Mobile: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblMobile, gridBagConstraints);

        txtFax.setFont(CoeusFontFactory.getNormalFont());
        txtFax.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtFax, gridBagConstraints);

        txtPager.setFont(CoeusFontFactory.getNormalFont());
        txtPager.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(txtPager, gridBagConstraints);

        txtMobile.setFont(CoeusFontFactory.getNormalFont());
        txtMobile.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 8);
        pnlContactInfo.add(txtMobile, gridBagConstraints);

        lblERACommonsUserName.setFont(CoeusFontFactory.getLabelFont());
        lblERACommonsUserName.setText("eRA Commons User Name: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(lblERACommonsUserName, gridBagConstraints);

        txtERACommonsUserName.setFont(CoeusFontFactory.getNormalFont());
        txtERACommonsUserName.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlContactInfo.add(txtERACommonsUserName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pnlAddPersondetails.add(pnlContactInfo, gridBagConstraints);

        sptrPersonalDetails1.setBackground(new java.awt.Color(102, 102, 102));
        sptrPersonalDetails1.setForeground(new java.awt.Color(102, 102, 102));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        pnlAddPersondetails.add(sptrPersonalDetails1, gridBagConstraints);

        scrpnAddPersonDetails.setViewportView(pnlAddPersondetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 15;
        add(scrpnAddPersonDetails, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(6, 5, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    //Added for Person Enhancement Case#1602 - Contact Info Start 6
    public void itemStateChanged(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == itemEvent.DESELECTED){
            return ;
        }
        Object source = itemEvent.getSource();
        
        if(source.equals(cmbCountry)){
            ComboBoxBean comboCountryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
            if(!comboCountryBean.getCode().trim().equals("USA")){
                ComboBoxBean emptyBean = new ComboBoxBean("","");
                cmbState.removeAllItems();
                cmbState.addItem(emptyBean);
            }else{
                populateStateCombo();
            }
        }
//        
//        if(source.equals(cmbState)){
//            ComboBoxBean cmbCntryBean = (ComboBoxBean)cmbCountry.getSelectedItem();
//            
//        }
    }
    //Added for Person Enhancement Case#1602 - Contact Info End 6
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JCheckBox chkFaculty;
    public javax.swing.JCheckBox chkGradStrudentStaff;
    public javax.swing.JCheckBox chkHandicapped;
    public javax.swing.JCheckBox chkHasVisa;
    public javax.swing.JCheckBox chkMedicalStaff;
    public javax.swing.JCheckBox chkOnSabatical;
    public javax.swing.JCheckBox chkOtherAccadem;
    public javax.swing.JCheckBox chkResearchStaff;
    public javax.swing.JCheckBox chkServiceStaff;
    public javax.swing.JCheckBox chkSupportStaff;
    public javax.swing.JCheckBox chkVecationAccural;
    public javax.swing.JCheckBox chkVetran;
    public edu.mit.coeus.utils.CoeusComboBox cmbCountry;
    public javax.swing.JLabel lblAdderss1;
    public javax.swing.JLabel lblAddress2;
    public javax.swing.JLabel lblAddress3;
    public javax.swing.JLabel lblAge;
    public javax.swing.JLabel lblAgeByFiscal;
    public javax.swing.JLabel lblCitizenship;
    public javax.swing.JLabel lblCity;
    public javax.swing.JLabel lblCountry;
    public javax.swing.JLabel lblCounty;
    public javax.swing.JLabel lblDOB;
    public javax.swing.JLabel lblDegree;
    public javax.swing.JLabel lblDirectoryDept;
    public javax.swing.JLabel lblDirectoryTitle;
    public javax.swing.JLabel lblERACommonsUserName;
    public javax.swing.JLabel lblEducationalLevel;
    public javax.swing.JLabel lblEmail;
    public javax.swing.JLabel lblFax;
    public javax.swing.JLabel lblFirstName;
    public javax.swing.JLabel lblFullName;
    public javax.swing.JLabel lblGender;
    public javax.swing.JLabel lblHandicappedType;
    public javax.swing.JLabel lblHomeUnit;
    public javax.swing.JLabel lblID;
    public javax.swing.JLabel lblIdProvided;
    public javax.swing.JLabel lblIdVerified;
    public javax.swing.JLabel lblLastName;
    public javax.swing.JLabel lblMajor;
    public javax.swing.JLabel lblMiddleName;
    public javax.swing.JLabel lblMobile;
    public javax.swing.JLabel lblOfficeLocation;
    public javax.swing.JLabel lblOfficePhone;
    public javax.swing.JLabel lblPager;
    public javax.swing.JLabel lblPostalCode;
    public javax.swing.JLabel lblPrimaryTitle;
    public javax.swing.JLabel lblPriorName;
    public javax.swing.JLabel lblRace;
    public javax.swing.JLabel lblSSn;
    public javax.swing.JLabel lblSalutaion;
    public javax.swing.JLabel lblSchool;
    public javax.swing.JLabel lblSecOfficeLoc;
    public javax.swing.JLabel lblSecOfficePhone;
    public javax.swing.JLabel lblState;
    public javax.swing.JLabel lblUserName;
    public javax.swing.JLabel lblVetranType;
    public javax.swing.JLabel lblVisaCode;
    public javax.swing.JLabel lblVisaRenewalDate;
    public javax.swing.JLabel lblVisaType;
    public javax.swing.JLabel lblYearGraduated;
    public javax.swing.JPanel pnlAddPersondetails;
    public javax.swing.JPanel pnlContactInfo;
    public javax.swing.JPanel pnlEducation;
    public javax.swing.JPanel pnlOfficeDetails;
    public javax.swing.JScrollPane scrpnAddPersonDetails;
    public javax.swing.JSeparator sptrEducational;
    public javax.swing.JSeparator sptrFaculty;
    public javax.swing.JSeparator sptrOfficialDetails;
    public javax.swing.JSeparator sptrPersonalDetails;
    public javax.swing.JSeparator sptrPersonalDetails1;
    public edu.mit.coeus.utils.CoeusTextField txtAddress1;
    public edu.mit.coeus.utils.CoeusTextField txtAddress2;
    public edu.mit.coeus.utils.CoeusTextField txtAddress3;
    public edu.mit.coeus.utils.CoeusTextField txtAge;
    public edu.mit.coeus.utils.CoeusTextField txtAgeByFiscal;
    public edu.mit.coeus.utils.CoeusTextField txtCity;
    public javax.swing.JTextField txtCitzenship;
    public edu.mit.coeus.utils.CoeusTextField txtCounty;
    public edu.mit.coeus.utils.CoeusTextField txtDOB;
    public javax.swing.JTextField txtDegree;
    public javax.swing.JTextField txtDirectoryDept;
    public javax.swing.JTextField txtDirectoryTitle;
    public edu.mit.coeus.utils.CoeusTextField txtERACommonsUserName;
    public javax.swing.JTextField txtEducationalLevel;
    public edu.mit.coeus.utils.CoeusTextField txtEmail;
    public edu.mit.coeus.utils.CoeusTextField txtFax;
    public edu.mit.coeus.utils.CoeusTextField txtFirstName;
    public edu.mit.coeus.utils.CoeusTextField txtFullName;
    public edu.mit.coeus.utils.CoeusTextField txtGender;
    public javax.swing.JTextField txtHandicappedType;
    public javax.swing.JTextField txtHomeUnit;
    public edu.mit.coeus.utils.CoeusTextField txtId;
    public javax.swing.JTextField txtIdProvided;
    public javax.swing.JTextField txtIdVerified;
    public edu.mit.coeus.utils.CoeusTextField txtLastName;
    public javax.swing.JTextField txtMajor;
    public edu.mit.coeus.utils.CoeusTextField txtMidleName;
    public edu.mit.coeus.utils.CoeusTextField txtMobile;
    public edu.mit.coeus.utils.CoeusTextField txtOfficeLocation;
    public javax.swing.JTextField txtOfficePhone;
    public edu.mit.coeus.utils.CoeusTextField txtPager;
    public edu.mit.coeus.utils.CoeusTextField txtPostalCode;
    public javax.swing.JTextField txtPrimaryTitle;
    public edu.mit.coeus.utils.CoeusTextField txtPriorName;
    public edu.mit.coeus.utils.CoeusTextField txtRace;
    public javax.swing.JTextField txtSalutation;
    public javax.swing.JTextField txtSchool;
    public javax.swing.JTextField txtSecOfficeLoc;
    public javax.swing.JTextField txtSecOfficePhone;
    public edu.mit.coeus.utils.CoeusTextField txtSsn;
    public edu.mit.coeus.utils.CoeusTextField txtUserName;
    public javax.swing.JTextField txtVetranType;
    public javax.swing.JTextField txtVisaCode;
    public javax.swing.JTextField txtVisaRenewalDate;
    public javax.swing.JTextField txtVisaType;
    public javax.swing.JTextField txtYearGraduated;
    // End of variables declaration//GEN-END:variables
    
}

