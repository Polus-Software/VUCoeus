/**
 * @(#)PersonOrganizationDetail.java  1.0  March 13, 2003
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-SEP-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.propdev.bean.PersonEditableColumnsFormBean;
//import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
//import edu.mit.coeus.propdev.bean.ProposalPersonFormBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
//import java.awt.Color;
// JM needed for custom features
import java.awt.Component;
// JM END
import java.awt.GridBagConstraints;
//import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Vector;
import java.beans.*;

/**
 * <CODE>PersonOrganizationDetail </CODE>is a form object which display
 * the Person Organization details and it is used to <CODE> display </CODE> the person details.
 * This class will be instantiated from <CODE>PersonDetailsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */
public class PersonOrganizationDetail extends javax.swing.JComponent implements TypeConstants{
    
    
    DepartmentPersonFormBean departmentPersonFormBean;
    private char functionType;
    private boolean saveRequired;
    private char moduleType;
    private boolean canMaintain;
    private static final char DEPARTMENT_PERSON_MODULE_CODE = 'D';
    private static final char PROPOSAL_PERSON_MODULE_CODE = 'P';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    //holds function type for duplicate id check
    private static final char DUPLICATE_CHK_FN_TYPE='H';
    //holds the url for PersonMaintenanceServlet
    private final String PERSON_SERVLET = "/personMaintenanceServlet";
    private AppletServletCommunicator comm;
    //holds the url to connect to PersonMaintenanceServlet
    private String connectTo = CoeusGuiConstants.CONNECTION_URL + PERSON_SERVLET;
    private static final String EMPTY_STRING="";
    private CoeusMessageResources coeusMessageResources;
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    private String[] columnNamesToEdit;
    private Vector vecEditableColumnNames;
    public static String PERSON_FULLNAME;
    public static String TITLE;
    
    //Added for case 3009 -Make Person Name Editable - start
    public edu.mit.coeus.utils.CoeusTextField txtMiddleName = new edu.mit.coeus.utils.CoeusTextField();
    public javax.swing.JRadioButton  rdBtnActive = new javax.swing.JRadioButton();
    public javax.swing.JRadioButton rdBtnInactive = new javax.swing.JRadioButton();
    //Added for case 3009 -Make Person Name Editable - end
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
    private DateUtils dtUtils = new DateUtils();
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private java.text.SimpleDateFormat dtFormat
            = new java.text.SimpleDateFormat("MM/dd/yyyy");
    private boolean inSalAnnivFocus;
    private String focusDate;

// JM 6-17-2011 added constant for office location length fields
    private static final int OFFICE_LOCATION_LENGTH = 45;
// END
    
    // JM 02-08-2013 need canModifyAllFields permission
    private boolean canModifyAllFields;
    // JM END
    
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
    private final String S2S_SERVLET = "/S2SServlet";
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
    /** Default Constructor */
    
    public PersonOrganizationDetail() {
    }
    
    /** Creates new form <CODE>PersonOrganizationDetail</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param personBean DepartmentPersonFormBean
     * 'D' specifies that the form is in Display Mode
     */
    // JM 2-27-2013 new instantiating method including canModifyAllFields;
    // logic moved to new instantiate method
    public PersonOrganizationDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean, boolean canModifyAllFields) {
    	this.canModifyAllFields = canModifyAllFields;
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
    
    public PersonOrganizationDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean) {
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
        
    public void instantiate(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean) {
        this.functionType = functionType;
        this.departmentPersonFormBean = personBean;
        this.moduleType = moduleCode;
        this.canMaintain = maintaintab;
        //Instantiates CoeusMessageResource object
        coeusMessageResources = CoeusMessageResources.getInstance();
        if(departmentPersonFormBean != null){
            departmentPersonFormBean.addPropertyChangeListener(
                    new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        saveRequired = true;
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            saveRequired = true;
                        }
                    }
                    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                    if( "division".equals(pce.getPropertyName()) && (pce.getNewValue() == null && pce.getOldValue() == null )) {
                        saveRequired = true;
                    }
                    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
                }
            });
        }
        //initComponents();
        //showOrganizationDetails(departmentPersonFormBean);
        
    }
    // JM END
    
    private void setComponentsEnabled(){
        
        if( vecEditableColumnNames != null ){
            int size = vecEditableColumnNames.size();
            PersonEditableColumnsFormBean personEditableColumnsFormBean = null;
            for( int index = 0 ; index < size ; index++ ){
                personEditableColumnsFormBean = (PersonEditableColumnsFormBean)vecEditableColumnNames.elementAt(index);
                String colName = personEditableColumnsFormBean.getColumnName();
                
                //columnNamesToEdit[index] = colName;
                //Modifed for case 2697: Tabbing Problem in Proposal Person Details - start
                //For the tab traversal it checks whether the component is enabled or not
                if(colName != null){
                    if(colName.equalsIgnoreCase("FULL_NAME")){
                        txtFullName.setEditable(true);
                        txtFullName.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("PRIOR_NAME")){
                        txtPriorName.setEditable(true);
                        txtPriorName.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("USER_NAME")){
                        txtUserName.setEditable(true);
                        txtUserName.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("EMAIL_ADDRESS")){
                        txtEMailAddress.setEditable(true);
                        txtEMailAddress.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("OFFICE_LOCATION")){
                        txtOfficeLocation.setEditable(true);
                        txtOfficeLocation.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("SECONDRY_OFFICE_LOCATION")){
                        txtSecOfficeLocation.setEditable(true);
                        txtSecOfficeLocation.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("OFFICE_PHONE")){
                        txtOfficePhone.setEditable(true);
                        txtOfficePhone.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("PRIMARY_TITLE")){
                        txtPrimaryTitle.setEditable(true);
                        txtPrimaryTitle.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("HOME_UNIT")){
                        txtHomeUnit.setEditable(true);
                        txtHomeUnit.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("SECONDRY_OFFICE_PHONE")){
                        txtSecOfficePhone.setEditable(true);
                        txtSecOfficePhone.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("DIRECTORY_TITLE")){
                        txtDirectoryTitle.setEditable(true);
                        txtDirectoryTitle.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("DIRECTORY_DEPARTMENT")){
                        txtDirectoryTitle.setEditable(true);
                        txtDirectoryTitle.setEnabled(true);
                    }
                    //Added for case 3009 -Make Person Name Editable - start
                    else if(colName.equalsIgnoreCase("FIRST_NAME")){ 
                        txtFirstName.setEditable(true);
                        txtFirstName.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("LAST_NAME")){ 
                        txtLastName.setEditable(true);
                        txtLastName.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("MIDDLE_NAME")){ 
                        txtMiddleName.setEditable(true);
                        txtMiddleName.setEnabled(true);
                    }
                    //Added for case 3009 -Make Person Name Editable - end
                    //Modifed for case 2697: Tabbing Problem in Proposal Person Details - end
                    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
                    else if(colName.equalsIgnoreCase("DIVISION")){ 
                        txtDivision.setEditable(true);
                        txtDivision.setEnabled(true);
                    }
                    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
                }
            }
        }else{
            setControlsEnabled(false);
        }
    }
//     added for #2697 - start - 27/12/2006
    public void setFocusTraversal(){
        
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Added the six components, added for person details into the focus traveral policy
        /*java.awt.Component[] components = {txtFullName,txtPriorName,txtUserName,txtEMailAddress,
          txtOfficeLocation,txtSecOfficeLocation,txtOfficePhone,txtSecOfficePhone,txtPrimaryTitle,
          txtDirectoryTitle,txtHomeUnit,txtDirectoryDepartment,txtUnitName};
         */
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        /*java.awt.Component[] components = {txtId, txtSsn, txtLastName, txtFirstName,
        txtMiddleName, rdBtnActive, rdBtnInactive, txtFullName,txtPriorName,txtUserName,
        txtEMailAddress, txtOfficeLocation,txtSecOfficeLocation,txtOfficePhone,
        txtSecOfficePhone,txtPrimaryTitle, txtDirectoryTitle,txtHomeUnit,
        txtDirectoryDepartment,txtUnitName,txtSalaryAnniversaryDate};*/
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
        java.awt.Component[] components = {txtId, txtSsn, txtLastName, txtFirstName,
        txtMiddleName, rdBtnActive, rdBtnInactive, txtFullName,txtPriorName,txtUserName,
        txtEMailAddress, txtOfficeLocation,txtSecOfficeLocation,txtOfficePhone,
        txtSecOfficePhone,txtPrimaryTitle, txtDirectoryTitle,txtHomeUnit,
        txtDirectoryDepartment,txtUnitName,txtDivision,txtSalaryAnniversaryDate};
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
//     added for #2697 - end - 27/12/2006
    public void showOraganization(){
        
        initComponents();
        //Added for case 3009 -Make Person Name Editable - start
        postInitComponents();
        //Added for case 3009 -Make Person Name Editable - end
        
        //Commented for case 3692 - remove the highlighted fields - start
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //To set the color of the mandatory fields to yellow
//        if(moduleType == PERSONNEL_MODULE_CODE){
//            txtId.setBackground(new Color(255,255,0));
//            txtFirstName.setBackground(new Color(255,255,0));
//            txtLastName.setBackground(new Color(255,255,0));
//            txtFullName.setBackground(new Color(255,255,0));
//            txtOfficeLocation.setBackground(new Color(255,255,0));
//        }
       
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
       //Commented for case 3692 - remove the highlighted fields - end 
//     Commented for #2697 - start - 27/12/2006
        //Added by Amit 11/17/2003
//        java.awt.Component[] components = {txtFullName,txtPriorName,txtUserName,txtEMailAddress,
//        txtOfficeLocation,txtSecOfficeLocation,txtOfficePhone,txtSecOfficePhone,txtPrimaryTitle,
//        txtDirectoryTitle,txtHomeUnit,txtDirectoryDepartment,txtUnitName};
//        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
//        setFocusTraversalPolicy(traversePolicy);
//        setFocusCycleRoot(true);
        //End Amit
//     Commented for #2697 - end - 27/12/2006
        showOrganizationDetails(departmentPersonFormBean);
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Set the controls enabled if the mode is not display when the module is PERSONNEL_MODULE_CODE
        if(moduleType == PERSONNEL_MODULE_CODE){
            if(functionType == DISPLAY_MODE){
                setControlsEnabled(false);
            }
        }else if(moduleType == DEPARTMENT_PERSON_MODULE_CODE){
            setControlsEnabled(false);
            /*To hide the first six components in the form which are only needed
             when the moduleType is PERSONNEL_MODULE_CODE*/
            //Modified for case 3009 -Make Person Name Editable - start
            //changed the method signature
            hideComponents(DEPARTMENT_PERSON_MODULE_CODE);
            //Modified for case 3009 -Make Person Name Editable - end
        }else if (moduleType == PROPOSAL_PERSON_MODULE_CODE){
            setControlsEnabled(false);
            /*To hide the first six components in the form which are only needed
             when the moduleType is PERSONNEL_MODULE_CODE*/
            //Modified for case 3009 -Make Person Name Editable - start
            //changed the method signature
            hideComponents(PROPOSAL_PERSON_MODULE_CODE);
            //Modified for case 3009 -Make Person Name Editable - end
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            if(functionType != DISPLAY_MODE && canMaintain){//COEUSQA-2293 : End
//            if(canMaintain){
                setComponentsEnabled();
            }
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        
//     added for #2697 - start - 27/12/2006
        setFocusTraversal();
//     added for #2697 - end - 27/12/2006
    }
   
    /**
     * This method is invoked when the data should be displayed in the form
     * parameter DepartmentPersonFormBean
     *
     * @param departmentPersonFormBean as DepartmentPersonFormBean
     */
    private void showOrganizationDetails(DepartmentPersonFormBean departmentPersonFormBean){
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //If the functionType is ADD_MODE, setting of fields with values is not required
        if(departmentPersonFormBean != null && functionType != ADD_MODE){
            //These six fields are only required while adding or modifying a person
            //through the Personnel
            //Modified for case 3009 -Make Person Name Editable - start
            //If module type is PROPOSAL_PERSON_MODULE_CODE or PERSONNEL_MODULE_CODE 
            //set the data to first name, last name and middle name.
            if(moduleType == PERSONNEL_MODULE_CODE){
                txtId.setText(
                        departmentPersonFormBean.getPersonId()== null ? ""
                        :departmentPersonFormBean.getPersonId());
                txtId.setEditable(false);
                //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
                txtId.setEnabled(false);
                txtSsn.setText(
                        departmentPersonFormBean.getSsn() == null ? ""
                        :departmentPersonFormBean.getSsn());
                if(departmentPersonFormBean.getStatus().equalsIgnoreCase("A")){
                    rdBtnActive.setSelected(true);
                }else{
                    rdBtnInactive.setSelected(true);
                }
            }
            
            if(moduleType == PERSONNEL_MODULE_CODE || moduleType == PROPOSAL_PERSON_MODULE_CODE){
                txtFirstName.setText(
                        departmentPersonFormBean.getFirstName()== null ? ""
                        :departmentPersonFormBean.getFirstName());
                txtLastName.setText(
                        departmentPersonFormBean.getLastName()== null ? ""
                        :departmentPersonFormBean.getLastName());
                txtMiddleName.setText(
                        departmentPersonFormBean.getMiddleName()== null ? ""
                        :departmentPersonFormBean.getMiddleName());
            }
            //Modified for case 3009 -Make Person Name Editable - end
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            
            String fullName = departmentPersonFormBean.getFullName();
            PERSON_FULLNAME = fullName == null ? "" : fullName;
            
            String personTitle = departmentPersonFormBean.getPrimaryTitle();
            TITLE = personTitle == null ? "" : personTitle;
            
            txtFullName.setText(
                    departmentPersonFormBean.getFullName()== null ? ""
                    :departmentPersonFormBean.getFullName());
            txtPriorName.setText(
                    departmentPersonFormBean.getPriorName()== null ? "" :
                        departmentPersonFormBean.getPriorName());
            txtEMailAddress.setText(
                    departmentPersonFormBean.getEmailAddress()== null ? "" :
                        departmentPersonFormBean.getEmailAddress());
            txtUserName.setText(
                    departmentPersonFormBean.getUserName()== null ? "" :
                        departmentPersonFormBean.getUserName());
            txtOfficeLocation.setText(
                    departmentPersonFormBean.getOfficeLocation()== null ? "" :
                        departmentPersonFormBean.getOfficeLocation());
            txtSecOfficeLocation.setText(
                    departmentPersonFormBean.getSecOfficeLocation()== null ? "" :
                        departmentPersonFormBean.getSecOfficeLocation());
            txtOfficePhone.setText(
                    departmentPersonFormBean.getOfficePhone()== null ? "" :
                        departmentPersonFormBean.getOfficePhone());
            txtSecOfficePhone.setText(
                    departmentPersonFormBean.getSecOfficePhone()== null ? "" :
                        departmentPersonFormBean.getSecOfficePhone());
            
            txtPrimaryTitle.setText(
                    departmentPersonFormBean.getPrimaryTitle()== null ? "" :
                        departmentPersonFormBean.getPrimaryTitle());
            txtDirectoryTitle.setText(
                    departmentPersonFormBean.getDirTitle()== null ? "" :
                        departmentPersonFormBean.getDirTitle());
            txtHomeUnit.setText(
                    departmentPersonFormBean.getHomeUnit()== null ? "" :
                        departmentPersonFormBean.getHomeUnit());
            txtUnitName.setText(
                    departmentPersonFormBean.getUnitName()== null ? "" :
                        departmentPersonFormBean.getUnitName());
            txtDirectoryDepartment.setText(
                    departmentPersonFormBean.getDirDept()== null ? "" :
                        departmentPersonFormBean.getDirDept());
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            if ( departmentPersonFormBean.getSalaryAnniversaryDate() != null ){
                txtSalaryAnniversaryDate.setText(dtUtils.formatDate(
                departmentPersonFormBean.getSalaryAnniversaryDate().toString(), REQUIRED_DATEFORMAT));
            }
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
            if(departmentPersonFormBean.getDivision()==null){
               txtDivision.setText(fetchDivisionValue(departmentPersonFormBean.getHomeUnit())); 
            }else{
                txtDivision.setText(
                        departmentPersonFormBean.getDivision()== null ? ""
                        : departmentPersonFormBean.getDivision());
            }
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End            
        }
        
    	// JM 02-26-2013 set editable based on permissions
        if ((moduleType == PERSONNEL_MODULE_CODE) || (moduleType == DEPARTMENT_PERSON_MODULE_CODE)) {
            java.awt.Component[] fields = {txtId, txtSsn, txtLastName, txtFirstName,
                    txtMiddleName, rdBtnActive, rdBtnInactive, txtFullName,txtPriorName,txtUserName,
                    txtEMailAddress, txtOfficeLocation,txtSecOfficeLocation,txtOfficePhone,
                    txtSecOfficePhone,txtPrimaryTitle, txtDirectoryTitle,txtHomeUnit,
                    txtDirectoryDepartment,txtUnitName,txtDivision,txtSalaryAnniversaryDate};

            for (int f=0; f < fields.length; f++) {
            	Component currField = fields[f];
            	currField.setEnabled(canModifyAllFields);
            }
    }
    	// JM END
    }
    
    /**
     * This method is called for different functionType for setting the field
     * controls like enable or disable ,set the background color.
     *
     * @param functionType char
     */
    /*
    private void setControls(char functionType){
        //if (functionType == 'D'){
            setControlsEnabled(false);
        //}
    }*/
    
    /**
     * This method is called for setting the controls of the text and combobox
     * component like enable or disable .
     *
     * @param value boolean ,if true enable else disable
     */
    private void setControlsEnabled(boolean value){
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Added these 6 fields for add/modify person
        txtId.setEditable(value);
        txtSsn.setEditable(value);
        txtLastName.setEditable(value);
        txtFirstName.setEditable(value);
        txtMiddleName.setEditable(value);
        rdBtnActive.setEnabled(value);
        rdBtnInactive.setEnabled(value);
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        
        txtDirectoryDepartment.setEditable(value);
        txtDirectoryTitle.setEditable(value);
        txtEMailAddress.setEditable(value);
        txtFullName.setEditable(value);
        txtHomeUnit.setEditable(value);
        txtOfficeLocation.setEditable(value);
        txtOfficePhone.setEditable(value);
        txtPrimaryTitle.setEditable(value);
        txtPriorName.setEditable(value);
        txtSecOfficeLocation.setEditable(value);
        txtSecOfficePhone.setEditable(value);
        txtUnitName.setEditable(value);
        txtUserName.setEditable(value);
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        txtSalaryAnniversaryDate.setEditable(value);
        
        //Added for case 2697: Tabbing Problem in Proposal Person Details - start
        //For the tab traversal it checks whether the component is enabled or not
        txtId.setEnabled(value);
        txtSsn.setEnabled(value);
        txtLastName.setEnabled(value);
        txtFirstName.setEnabled(value);
        txtMiddleName.setEnabled(value);
        
        txtDirectoryDepartment.setEnabled(value);
        txtDirectoryTitle.setEnabled(value);
        txtEMailAddress.setEnabled(value);
        txtFullName.setEnabled(value);
        txtHomeUnit.setEnabled(value);
        txtOfficeLocation.setEnabled(value);
        txtOfficePhone.setEnabled(value);
        txtPrimaryTitle.setEnabled(value);
        txtPriorName.setEnabled(value);
        txtSecOfficeLocation.setEnabled(value);
        txtSecOfficePhone.setEnabled(value);
        txtUnitName.setEnabled(value);
        txtUserName.setEnabled(value);
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
        txtSalaryAnniversaryDate.setEnabled(value);
        //Added for case 2697: Tabbing Problem in Proposal Person Details - end
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
        txtDivision.setEditable(value);
        txtDivision.setEnabled(value);
        //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
    }
    
    /**
     * This method is used to set the form data specified in
     * <CODE>DepartmentPersonFormBean</CODE>
     */
    public void setFormData(DepartmentPersonFormBean personBean){
        showOrganizationDetails(personBean);
    }
    
    /** This method is used to find out whether modifications done to the data
     * have been saved or not.
     *
     * @return true if data is not saved after modifications, else false.
     */
    public boolean isSaveRequired(){
        return this.saveRequired;
    }
    /** This method is used to set whether modifications are to be saved or not.
     *
     * @param saveRequired boolean true if data is to be saved after modifications,
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }
    
    /** Method to get the functionType
     * @return a <CODE>Char</CODE> representation of functionType.
     */
    public char getFunctionType(){
        return this.functionType;
    }
    /** Method to set the functionType
     * @param fType is functionType to be set like 'D', 'I', 'M'
     */
    public void setFunctionType(char fType){
        this.functionType = fType;
    }
    
    public boolean validateData() throws Exception{
        boolean valid = true;
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Check for the mandatory fields if the form is taken through the Personnel module
        if(moduleType == PERSONNEL_MODULE_CODE && functionType!=DISPLAY_MODE){
            if (EMPTY_STRING.equals(((txtId.getText()).trim()))) {
                txtId.requestFocusInWindow();
                valid = false;
                throw new Exception(
                        coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1102"));
                
            }else if (EMPTY_STRING.equals((txtLastName.getText()).trim())) {
                txtLastName.requestFocusInWindow();
                valid = false;
                throw new Exception(
                        coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1103"));
            }else if (EMPTY_STRING.equals((txtFirstName.getText()).trim())) {
                txtFirstName.requestFocusInWindow();
                valid = false;
                throw new Exception(
                        coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1104"));
            }else if (EMPTY_STRING.equals((txtFullName.getText()).trim())) {
                txtFullName.requestFocusInWindow();
                valid = false;
                throw new Exception(
                        coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1105"));
            }else if (EMPTY_STRING.equals((txtOfficeLocation.getText()).trim())) {
                txtOfficeLocation.requestFocusInWindow();
                valid = false;
                throw new Exception(
                        coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1106"));
            }else{
                if(functionType == ADD_MODE){
                    boolean isduplicate=isDuplicateId(); // Check whether the Id already exists or not
                    if (isduplicate) {
                        txtId.requestFocusInWindow();
                        valid = false;
                        throw new Exception(
                                coeusMessageResources.parseMessageKey("addPerson_exceptionCode.1101"));
                    }
                }
            }
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - start
            if (txtSalaryAnniversaryDate.getText() != null
                &&  !txtSalaryAnniversaryDate.getText().trim().equals("")){
                  validateSalaryAnnivDate();
                }
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
        }
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        return valid;
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
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
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
    
    /** This method is used to get all the JTable data.
     * @return a DepartmentPersonFormBean.
     */
    public void getFormData(){
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Bug fix : Save person details if the person has canMaintain rights
        //no need to check for function type Start
        //if( functionType != 'D'  && canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//&& !canMaintain
        //if( canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//&& !canMaintain
        //Bug fix : Save person details if the person has canMaintain rights
        //no need to check for function type End
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - end
        
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Included the PERSONNEL module type
        if( (canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE)||
                (moduleType ==PERSONNEL_MODULE_CODE)) {
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //Set the values for these six fields only when the moduleType is PERSONNEL_MODULE_CODE
            //Modified for case 3009 -Make Person Name Editable - start
            //If module type is PROPOSAL_PERSON_MODULE_CODE or PERSONNEL_MODULE_CODE
            //populate the bean with first name, last name  or middle name
            if(moduleType == PERSONNEL_MODULE_CODE){
                departmentPersonFormBean.setPersonId(txtId.getText().trim().length() ==0 ? null
                        : txtId.getText());
                departmentPersonFormBean.setSsn(txtSsn.getText().trim().length() ==0 ? null
                        : txtSsn.getText());
                departmentPersonFormBean.setStatus(rdBtnActive.isSelected()? "A" : "I");
            }
            if(moduleType == PERSONNEL_MODULE_CODE || moduleType == PROPOSAL_PERSON_MODULE_CODE){
                departmentPersonFormBean.setLastName(txtLastName.getText().trim().length() ==0 ? null
                        : txtLastName.getText());
                departmentPersonFormBean.setFirstName(txtFirstName.getText().trim().length() ==0 ? null
                        : txtFirstName.getText());
                departmentPersonFormBean.setMiddleName(txtMiddleName.getText().trim().length() ==0 ? null
                        : txtMiddleName.getText());
            }
            //Modified for case 3009 -Make Person Name Editable - end
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            PERSON_FULLNAME = txtFullName.getText();
            TITLE = txtPrimaryTitle.getText();
            
            departmentPersonFormBean.setFullName(txtFullName.getText().trim().length() ==0 ? null
                    : txtFullName.getText());
            
            departmentPersonFormBean.setPriorName(txtPriorName.getText().trim().length() ==0 ? null
                    : txtPriorName.getText());
            
            departmentPersonFormBean.setUserName(txtUserName.getText().trim().length() ==0 ? null
                    : txtUserName.getText());
            departmentPersonFormBean.setEmailAddress(txtEMailAddress.getText().trim().length() ==0 ? null
                    : txtEMailAddress.getText());
            departmentPersonFormBean.setOfficeLocation(txtOfficeLocation.getText().trim().length() ==0 ? null
                    : txtOfficeLocation.getText());
            departmentPersonFormBean.setSecOfficeLocation(txtSecOfficeLocation.getText().trim().length() ==0 ? null
                    : txtSecOfficeLocation.getText());
            departmentPersonFormBean.setOfficePhone(txtOfficePhone.getText().trim().length() ==0 ? null
                    : txtOfficePhone.getText());
            departmentPersonFormBean.setSecOfficePhone(txtSecOfficePhone.getText().trim().length() ==0 ? null
                    : txtSecOfficePhone.getText());
            departmentPersonFormBean.setPrimaryTitle(txtPrimaryTitle.getText().trim().length() ==0 ? null
                    : txtPrimaryTitle.getText());
            departmentPersonFormBean.setDirTitle(txtDirectoryTitle.getText().trim().length() ==0 ? null
                    : txtDirectoryTitle.getText());
            departmentPersonFormBean.setHomeUnit(txtHomeUnit.getText().trim().length() ==0 ? null
                    : txtHomeUnit.getText());
            departmentPersonFormBean.setDirDept(txtDirectoryDepartment.getText().trim().length() ==0 ? null
                    : txtDirectoryDepartment.getText());
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
            departmentPersonFormBean.setDivision(txtDivision.getText().length() ==0 ? null
                    : txtDivision.getText());
            //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
            String annivFormattedDate = txtSalaryAnniversaryDate.getText();
            String strAnnivDate = dtUtils.restoreDate(annivFormattedDate,DATE_SEPARATERS);
            java.sql.Date annivDate = null;
            try{
                if(strAnnivDate != null && strAnnivDate.trim().length() > 0){
                    annivDate = new java.sql.Date(dtFormat.parse(strAnnivDate).getTime());
                }
            }catch(ParseException pe){
                saveRequired = true;
                pe.printStackTrace();                
            }
            departmentPersonFormBean.setSalaryAnniversaryDate(annivDate);
            //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblFullName = new javax.swing.JLabel();
        lblPriorName = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        lblEMailAddress = new javax.swing.JLabel();
        lblOfficeLocation = new javax.swing.JLabel();
        lblSecOfficeLocation = new javax.swing.JLabel();
        lblOfficePhone = new javax.swing.JLabel();
        lblSecOfficePhone = new javax.swing.JLabel();
        lblPrimaryTitle = new javax.swing.JLabel();
        lblDirectoryTitle = new javax.swing.JLabel();
        lblHomeUnit = new javax.swing.JLabel();
        lblDirectoryDepartment = new javax.swing.JLabel();
        lblUnitName = new javax.swing.JLabel();
        txtFullName = new edu.mit.coeus.utils.CoeusTextField();
        txtEMailAddress = new edu.mit.coeus.utils.CoeusTextField();
        txtDirectoryTitle = new edu.mit.coeus.utils.CoeusTextField();
        txtUnitName = new edu.mit.coeus.utils.CoeusTextField();
        txtPrimaryTitle = new edu.mit.coeus.utils.CoeusTextField();
        txtPriorName = new edu.mit.coeus.utils.CoeusTextField();
        txtSecOfficeLocation = new edu.mit.coeus.utils.CoeusTextField();
        txtSecOfficePhone = new edu.mit.coeus.utils.CoeusTextField();
        txtDirectoryDepartment = new edu.mit.coeus.utils.CoeusTextField();
        txtUserName = new edu.mit.coeus.utils.CoeusTextField();
        txtOfficeLocation = new edu.mit.coeus.utils.CoeusTextField();
        txtOfficePhone = new edu.mit.coeus.utils.CoeusTextField();
        txtHomeUnit = new edu.mit.coeus.utils.CoeusTextField();
        lblId = new javax.swing.JLabel();
        txtId = new edu.mit.coeus.utils.CoeusTextField();
        lblSsn = new javax.swing.JLabel();
        txtSsn = new edu.mit.coeus.utils.CoeusTextField();
        lblLastName = new javax.swing.JLabel();
        txtLastName = new edu.mit.coeus.utils.CoeusTextField();
        lblFirstName = new javax.swing.JLabel();
        txtFirstName = new edu.mit.coeus.utils.CoeusTextField();
        lblMiddleName = new javax.swing.JLabel();
        lblSalaryAnniversaryDate = new javax.swing.JLabel();
        txtSalaryAnniversaryDate = new edu.mit.coeus.utils.CoeusTextField();
        lblDivision = new javax.swing.JLabel();
        txtDivision = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblFullName.setFont(CoeusFontFactory.getLabelFont());
        lblFullName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFullName.setText("Full Name:");
        lblFullName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblFullName, gridBagConstraints);

        lblPriorName.setFont(CoeusFontFactory.getLabelFont());
        lblPriorName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPriorName.setText("Prior Name:");
        lblPriorName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblPriorName, gridBagConstraints);

        lblUserName.setFont(CoeusFontFactory.getLabelFont());
        lblUserName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUserName.setText("User Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblUserName, gridBagConstraints);

        lblEMailAddress.setFont(CoeusFontFactory.getLabelFont());
        lblEMailAddress.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblEMailAddress.setText("Email Address:");
        lblEMailAddress.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblEMailAddress, gridBagConstraints);

        lblOfficeLocation.setFont(CoeusFontFactory.getLabelFont());
        lblOfficeLocation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOfficeLocation.setText("Office Location:");
        lblOfficeLocation.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblOfficeLocation, gridBagConstraints);

        lblSecOfficeLocation.setFont(CoeusFontFactory.getLabelFont());
        lblSecOfficeLocation.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSecOfficeLocation.setText("Sec. Office Location:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblSecOfficeLocation, gridBagConstraints);

        lblOfficePhone.setFont(CoeusFontFactory.getLabelFont());
        lblOfficePhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOfficePhone.setText("Office Phone:");
        lblOfficePhone.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblOfficePhone, gridBagConstraints);

        lblSecOfficePhone.setFont(CoeusFontFactory.getLabelFont());
        lblSecOfficePhone.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSecOfficePhone.setText("Sec. Office Phone:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblSecOfficePhone, gridBagConstraints);

        lblPrimaryTitle.setFont(CoeusFontFactory.getLabelFont());
        lblPrimaryTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPrimaryTitle.setText("Primary Title:");
        lblPrimaryTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblPrimaryTitle, gridBagConstraints);

        lblDirectoryTitle.setFont(CoeusFontFactory.getLabelFont());
        lblDirectoryTitle.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDirectoryTitle.setText("Directory Title:");
        lblDirectoryTitle.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblDirectoryTitle, gridBagConstraints);

        lblHomeUnit.setFont(CoeusFontFactory.getLabelFont());
        lblHomeUnit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHomeUnit.setText("Home Unit:");
        lblHomeUnit.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblHomeUnit, gridBagConstraints);

        lblDirectoryDepartment.setFont(CoeusFontFactory.getLabelFont());
        lblDirectoryDepartment.setText("Directory Department:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblDirectoryDepartment, gridBagConstraints);

        lblUnitName.setFont(CoeusFontFactory.getLabelFont());
        lblUnitName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUnitName.setText("Unit Name:");
        lblUnitName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblUnitName, gridBagConstraints);

        txtFullName.setDocument(new LimitedPlainDocument(90));
        txtFullName.setMaximumSize(new java.awt.Dimension(392, 20));
        txtFullName.setMinimumSize(new java.awt.Dimension(392, 20));
        txtFullName.setPreferredSize(new java.awt.Dimension(392, 20));
// JM 7-22-2011 added required field highlighting
        txtFullName.setRequired(true);
// END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtFullName, gridBagConstraints);

        txtEMailAddress.setDocument(new LimitedPlainDocument(60));
        txtEMailAddress.setMaximumSize(new java.awt.Dimension(392, 20));
        txtEMailAddress.setMinimumSize(new java.awt.Dimension(392, 20));
        txtEMailAddress.setPreferredSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtEMailAddress, gridBagConstraints);

        txtDirectoryTitle.setDocument(new LimitedPlainDocument(50));
        txtDirectoryTitle.setMaximumSize(new java.awt.Dimension(392, 20));
        txtDirectoryTitle.setMinimumSize(new java.awt.Dimension(392, 20));
        txtDirectoryTitle.setPreferredSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtDirectoryTitle, gridBagConstraints);

        txtUnitName.setMaximumSize(new java.awt.Dimension(392, 20));
        txtUnitName.setMinimumSize(new java.awt.Dimension(392, 20));
        txtUnitName.setPreferredSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtUnitName, gridBagConstraints);

        txtPrimaryTitle.setDocument(new LimitedPlainDocument(51));
        txtPrimaryTitle.setMaximumSize(new java.awt.Dimension(392, 20));
        txtPrimaryTitle.setMinimumSize(new java.awt.Dimension(392, 20));
        txtPrimaryTitle.setPreferredSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtPrimaryTitle, gridBagConstraints);

        txtPriorName.setDocument(new LimitedPlainDocument(30));
        txtPriorName.setMaximumSize(new java.awt.Dimension(145, 20));
        txtPriorName.setMinimumSize(new java.awt.Dimension(145, 20));
        txtPriorName.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtPriorName, gridBagConstraints);

// JM 6-17-2011 changed field length from 30 to 45
        txtSecOfficeLocation.setDocument(new LimitedPlainDocument(OFFICE_LOCATION_LENGTH)); // JM
        txtSecOfficeLocation.setMaximumSize(new java.awt.Dimension(145, 20));
        txtSecOfficeLocation.setMinimumSize(new java.awt.Dimension(145, 20));
        txtSecOfficeLocation.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtSecOfficeLocation, gridBagConstraints);

        txtSecOfficePhone.setDocument(new LimitedPlainDocument(20));
        txtSecOfficePhone.setMaximumSize(new java.awt.Dimension(145, 20));
        txtSecOfficePhone.setMinimumSize(new java.awt.Dimension(145, 20));
        txtSecOfficePhone.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtSecOfficePhone, gridBagConstraints);

        txtDirectoryDepartment.setDocument(new LimitedPlainDocument(30));
        txtDirectoryDepartment.setMaximumSize(new java.awt.Dimension(145, 20));
        txtDirectoryDepartment.setMinimumSize(new java.awt.Dimension(145, 20));
        txtDirectoryDepartment.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtDirectoryDepartment, gridBagConstraints);

        txtUserName.setDocument(new LimitedPlainDocument(60));
        txtUserName.setMaximumSize(new java.awt.Dimension(145, 20));
        txtUserName.setMinimumSize(new java.awt.Dimension(145, 20));
        txtUserName.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtUserName, gridBagConstraints);

// JM 6-17-2011 changed field length from 30 to 45
        txtOfficeLocation.setDocument(new LimitedPlainDocument(OFFICE_LOCATION_LENGTH)); // JM
        txtOfficeLocation.setMaximumSize(new java.awt.Dimension(145, 20));
        txtOfficeLocation.setMinimumSize(new java.awt.Dimension(145, 20));
        txtOfficeLocation.setPreferredSize(new java.awt.Dimension(145, 20));
// JM 7-22-2011 added required field highlighting
        txtOfficeLocation.setRequired(true);
// END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtOfficeLocation, gridBagConstraints);

        txtOfficePhone.setDocument(new LimitedPlainDocument(20));
        txtOfficePhone.setMaximumSize(new java.awt.Dimension(145, 20));
        txtOfficePhone.setMinimumSize(new java.awt.Dimension(145, 20));
        txtOfficePhone.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtOfficePhone, gridBagConstraints);

        txtHomeUnit.setDocument(new LimitedPlainDocument(8));
        txtHomeUnit.setMaximumSize(new java.awt.Dimension(145, 20));
        txtHomeUnit.setMinimumSize(new java.awt.Dimension(145, 20));
        txtHomeUnit.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtHomeUnit, gridBagConstraints);

        lblId.setFont(CoeusFontFactory.getLabelFont());
        lblId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblId.setText("Id:");
        lblId.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblId, gridBagConstraints);

        // JM 8-12-2015 here is the problem with the non-alpha text field!!
        //txtId.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,9));
        txtId.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC,9));
        txtId.setMaximumSize(new java.awt.Dimension(145, 20));
        txtId.setMinimumSize(new java.awt.Dimension(145, 20));
        txtId.setPreferredSize(new java.awt.Dimension(145, 20));
// JM 7-22-2011 added required field highlighting
        txtId.setRequired(true);
// END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtId, gridBagConstraints);

        lblSsn.setFont(CoeusFontFactory.getLabelFont());
        lblSsn.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSsn.setText("SSN:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblSsn, gridBagConstraints);

        txtSsn.setDocument(new LimitedPlainDocument(9));
        txtSsn.setMaximumSize(new java.awt.Dimension(145, 20));
        txtSsn.setMinimumSize(new java.awt.Dimension(145, 20));
        txtSsn.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtSsn, gridBagConstraints);

        lblLastName.setFont(CoeusFontFactory.getLabelFont());
        lblLastName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLastName.setText("Last Name:");
        lblLastName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblLastName, gridBagConstraints);

        txtLastName.setDocument(new LimitedPlainDocument(30));
        txtLastName.setMaximumSize(new java.awt.Dimension(145, 20));
        txtLastName.setMinimumSize(new java.awt.Dimension(145, 20));
        txtLastName.setPreferredSize(new java.awt.Dimension(145, 20));
// JM 7-22-2011 added required field highlighting
        txtLastName.setRequired(true);
// END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtLastName, gridBagConstraints);

        lblFirstName.setFont(CoeusFontFactory.getLabelFont());
        lblFirstName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFirstName.setText("First Name:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 3);
        add(lblFirstName, gridBagConstraints);

        txtFirstName.setDocument(new LimitedPlainDocument(30));
        txtFirstName.setMaximumSize(new java.awt.Dimension(145, 20));
        txtFirstName.setMinimumSize(new java.awt.Dimension(145, 20));
        txtFirstName.setPreferredSize(new java.awt.Dimension(145, 20));
// JM 7-22-2011 added required field highlighting
        txtFirstName.setRequired(true);
// END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
        add(txtFirstName, gridBagConstraints);

        lblMiddleName.setFont(CoeusFontFactory.getLabelFont());
        lblMiddleName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMiddleName.setText("Middle:");
        lblMiddleName.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 3);
        add(lblMiddleName, gridBagConstraints);

        lblSalaryAnniversaryDate.setFont(CoeusFontFactory.getLabelFont());
        lblSalaryAnniversaryDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSalaryAnniversaryDate.setText("Salary Anniversary Date:");
        lblSalaryAnniversaryDate.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblSalaryAnniversaryDate, gridBagConstraints);

        txtSalaryAnniversaryDate.setDocument(new LimitedPlainDocument(15));
        txtSalaryAnniversaryDate.setMaximumSize(new java.awt.Dimension(145, 20));
        txtSalaryAnniversaryDate.setMinimumSize(new java.awt.Dimension(145, 20));
        txtSalaryAnniversaryDate.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtSalaryAnniversaryDate, gridBagConstraints);

        lblDivision.setFont(CoeusFontFactory.getLabelFont());
        lblDivision.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDivision.setText("Division :");
        lblDivision.setMaximumSize(new java.awt.Dimension(53, 14));
        lblDivision.setMinimumSize(new java.awt.Dimension(53, 14));
        lblDivision.setPreferredSize(new java.awt.Dimension(53, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        add(lblDivision, gridBagConstraints);

        txtDivision.setMaximumSize(new java.awt.Dimension(392, 20));
        txtDivision.setMinimumSize(new java.awt.Dimension(392, 20));
        txtDivision.setPreferredSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
        add(txtDivision, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    //Modified for case 3009 -Make Person Name Editable - start
    //Changed the  method signature 
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    /**
     * Hide the components when the form is used to view person details other
     * than through the Personnel of the Maintain Menu
     *
     * @param module module type
     */
    public void hideComponents(char module){
        //public void hideComponents(){
        if(module == DEPARTMENT_PERSON_MODULE_CODE){
            lblId.setVisible(false);
            txtId.setVisible(false);
            lblSsn.setVisible(false);
            txtSsn.setVisible(false);
            lblLastName.setVisible(false);
            txtLastName.setVisible(false);
            lblFirstName.setVisible(false);
            txtFirstName.setVisible(false);
            lblMiddleName.setVisible(false);
            txtMiddleName.setVisible(false);
            rdBtnActive.setVisible(false);
            rdBtnInactive.setVisible(false);
        }else if(module == PROPOSAL_PERSON_MODULE_CODE){
            lblId.setVisible(false);
            txtId.setVisible(false);
            lblSsn.setVisible(false);
            txtSsn.setVisible(false);
            rdBtnActive.setVisible(false);
            rdBtnInactive.setVisible(false);
        }
    }
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
     //Modified for case 3009 -Make Person Name Editable - start
    
    //Added for case 3009 -Make Person Name Editable - start
    public void postInitComponents(){
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        if(moduleType == PROPOSAL_PERSON_MODULE_CODE){
            txtMiddleName.setMaximumSize(new java.awt.Dimension(145, 20));
            txtMiddleName.setMinimumSize(new java.awt.Dimension(145, 20));
            txtMiddleName.setPreferredSize(new java.awt.Dimension(145, 20));
            //Added for case 3618 - Setting the maximum limit for components - start
            txtMiddleName.setDocument(new LimitedPlainDocument(30));
            //Added for case 3618 - Setting the maximum limit for components - end
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
            add(txtMiddleName, gridBagConstraints);

            buttonGroup1.add(rdBtnActive);
            rdBtnActive.setFont(CoeusFontFactory.getLabelFont());
            rdBtnActive.setSelected(true);
            rdBtnActive.setText("Active");
            rdBtnActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdBtnActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 0);
            add(rdBtnActive, gridBagConstraints);

            buttonGroup1.add(rdBtnInactive);
            rdBtnInactive.setFont(CoeusFontFactory.getLabelFont());
            rdBtnInactive.setText("Inactive");
            rdBtnInactive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdBtnInactive.setMargin(new java.awt.Insets(0, 0, 0, 0));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
            add(rdBtnInactive, gridBagConstraints);
        }else{
            txtMiddleName.setMaximumSize(new java.awt.Dimension(145, 20));
            txtMiddleName.setMinimumSize(new java.awt.Dimension(145, 20));
            txtMiddleName.setPreferredSize(new java.awt.Dimension(145, 20));
            //Added for case 3618 - Setting the maximum limit for components - start
            txtMiddleName.setDocument(new LimitedPlainDocument(30));
            //Added for case 3618 - Setting the maximum limit for components - end
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 5, 1, 4);
            add(txtMiddleName, gridBagConstraints);

            buttonGroup1.add(rdBtnActive);
            rdBtnActive.setFont(CoeusFontFactory.getLabelFont());
            rdBtnActive.setSelected(true);
            rdBtnActive.setText("Active");
            rdBtnActive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdBtnActive.setMargin(new java.awt.Insets(0, 0, 0, 0));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 0);
            add(rdBtnActive, gridBagConstraints);

            buttonGroup1.add(rdBtnInactive);
            rdBtnInactive.setFont(CoeusFontFactory.getLabelFont());
            rdBtnInactive.setText("Inactive");
            rdBtnInactive.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rdBtnInactive.setMargin(new java.awt.Insets(0, 0, 0, 0));
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 4;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
            gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 4);
            add(rdBtnInactive, gridBagConstraints);
        }
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
        txtSalaryAnniversaryDate.addFocusListener(new FocusListener() {
             boolean temporary = false;
             String annivData = "";
            public void focusGained(FocusEvent focusEvent) {
               
                if( focusEvent.getSource().equals(txtSalaryAnniversaryDate) ) {
                    inSalAnnivFocus = true;
                }
                Object source = focusEvent.getSource();
                if (txtSalaryAnniversaryDate.getText() != null
                        &&  !txtSalaryAnniversaryDate.getText().trim().equals("")){
                    if(!annivData.equals(txtSalaryAnniversaryDate.getText()) && !temporary){
                        String strDate = dtUtils.restoreDate(txtSalaryAnniversaryDate.getText(), DATE_SEPARATERS);
                        txtSalaryAnniversaryDate.setText(strDate);
                    }
                }
            }
            
            public void focusLost(FocusEvent focusEvent) {
                 /* check whether the focus lost is temporary or permanent*/
                temporary = focusEvent.isTemporary();
                if (txtSalaryAnniversaryDate.getText() != null
                        &&  !txtSalaryAnniversaryDate.getText().trim().equals("") && (!temporary) ){
                    annivData = txtSalaryAnniversaryDate.getText();
                    String convertedDate =
                            dtUtils.formatDate(txtSalaryAnniversaryDate.getText(), "/-:,"
                            ,"dd-MMM-yyyy");
                    if (convertedDate==null){
//                        CoeusOptionPane.showErrorDialog(
//                                coeusMessageResources.parseMessageKey(
//                                "memMntFrm_exceptionCode.1048"));
//                        txtSalaryAnniversaryDate.requestFocus();
                    }else {
                        temporary = false;
                        focusDate = txtSalaryAnniversaryDate.getText();
                        txtSalaryAnniversaryDate.setText(convertedDate);
                        temporary = false;
                         if( focusEvent.getSource().equals(txtSalaryAnniversaryDate) ) {
                            inSalAnnivFocus = false;
                        }
                    }
                }
            }
        });
        //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - ends
    }
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - Start
    /**
     * This method validates the Salary Anniversary date text field component
     * If data is not entered this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateSalaryAnnivDate() throws Exception{
        boolean valid = true;
        String annivDate = txtSalaryAnniversaryDate.getText();
        String formattedDate = null;
        if (annivDate != null && annivDate.trim().length() > 0) {
            // validate date field
            if( !inSalAnnivFocus ) {
                formattedDate = new DateUtils().restoreDate(annivDate,DATE_SEPARATERS);
                if( formattedDate!= null && formattedDate.equals( annivDate ) ) {
                    //Validation done only while saving and to set focus to the component
                    txtSalaryAnniversaryDate.requestFocus();
                    throw new Exception(
                        coeusMessageResources.parseMessageKey("memMntFrm_exceptionCode.1048"));
                }
            }else{
                formattedDate = new DateUtils().formatDate(annivDate,
                        DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(formattedDate == null) {
                    //Validation done only while saving and to set focus to the component
                    txtSalaryAnniversaryDate.requestFocus();
                     throw new Exception(
                        coeusMessageResources.parseMessageKey("memMntFrm_exceptionCode.1048"));
                }
            }
        }
        if(valid){
            String convertedDate = dtUtils.formatDate(txtSalaryAnniversaryDate.getText(),
                    "/-:," , "dd-MMM-yyyy");
            if(convertedDate!=null){
                txtSalaryAnniversaryDate.setText(convertedDate);
            }
        }
        return valid;
    }
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - end
    
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - Start
    /**
     * This method fetches the division value based on the home unit of the person.
     * @param String homeUnit
     * @return String division
     */
    private String fetchDivisionValue(String homeUnit) {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + S2S_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(S2SConstants.GET_DIVISION);
        request.setDataObject(homeUnit);
        comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        String division =((String)response.getDataObject());
        return division;
    }
    //COEUSQA-1674 - Allow Division Lead Unit to be modified in the person details - End
    
    /** Getter for property moduleType.
     * @return Value of property moduleType.
     */
    public char getModuleType() {
        return moduleType;
    }
    
    /** Setter for property moduleType.
     * @param moduleType New value of property moduleType.
     */
    public void setModuleType(char moduleType) {
        this.moduleType = moduleType;
    }
    
    /** Getter for property canMaintain.
     * @return Value of property canMaintain.
     */
    public boolean isCanMaintain() {
        return canMaintain;
    }
    
    /** Setter for property canMaintain.
     * @param canMaintain New value of property canMaintain.
     */
    public void setCanMaintain(boolean canMaintain) {
        this.canMaintain = canMaintain;
    }
    
    /** Getter for property columnNamesToEdit.
     * @return Value of property columnNamesToEdit.
     */
    public java.lang.String[] getColumnNamesToEdit() {
        return this.columnNamesToEdit;
    }
    
    /** Setter for property columnNamesToEdit.
     * @param columnNamesToEdit New value of property columnNamesToEdit.
     */
    public void setColumnNamesToEdit(java.lang.String[] columnNamesToEdit) {
        this.columnNamesToEdit = columnNamesToEdit;
    }
    
    /** Getter for property vecEditableColumnNames.
     * @return Value of property vecEditableColumnNames.
     */
    public java.util.Vector getVecEditableColumnNames() {
        return vecEditableColumnNames;
    }
    
    /** Setter for property vecEditableColumnNames.
     * @param vecEditableColumnNames New value of property vecEditableColumnNames.
     */
    public void setVecEditableColumnNames(java.util.Vector vecEditableColumnNames) {
        this.vecEditableColumnNames = vecEditableColumnNames;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel lblDirectoryDepartment;
    private javax.swing.JLabel lblDirectoryTitle;
    public javax.swing.JLabel lblDivision;
    private javax.swing.JLabel lblEMailAddress;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblFullName;
    private javax.swing.JLabel lblHomeUnit;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblMiddleName;
    private javax.swing.JLabel lblOfficeLocation;
    private javax.swing.JLabel lblOfficePhone;
    private javax.swing.JLabel lblPrimaryTitle;
    private javax.swing.JLabel lblPriorName;
    private javax.swing.JLabel lblSalaryAnniversaryDate;
    private javax.swing.JLabel lblSecOfficeLocation;
    private javax.swing.JLabel lblSecOfficePhone;
    private javax.swing.JLabel lblSsn;
    private javax.swing.JLabel lblUnitName;
    private javax.swing.JLabel lblUserName;
    private edu.mit.coeus.utils.CoeusTextField txtDirectoryDepartment;
    private edu.mit.coeus.utils.CoeusTextField txtDirectoryTitle;
    public edu.mit.coeus.utils.CoeusTextField txtDivision;
    private edu.mit.coeus.utils.CoeusTextField txtEMailAddress;
    private edu.mit.coeus.utils.CoeusTextField txtFirstName;
    private edu.mit.coeus.utils.CoeusTextField txtFullName;
    private edu.mit.coeus.utils.CoeusTextField txtHomeUnit;
    private edu.mit.coeus.utils.CoeusTextField txtId;
    private edu.mit.coeus.utils.CoeusTextField txtLastName;
    private edu.mit.coeus.utils.CoeusTextField txtOfficeLocation;
    private edu.mit.coeus.utils.CoeusTextField txtOfficePhone;
    private edu.mit.coeus.utils.CoeusTextField txtPrimaryTitle;
    private edu.mit.coeus.utils.CoeusTextField txtPriorName;
    private edu.mit.coeus.utils.CoeusTextField txtSalaryAnniversaryDate;
    private edu.mit.coeus.utils.CoeusTextField txtSecOfficeLocation;
    private edu.mit.coeus.utils.CoeusTextField txtSecOfficePhone;
    private edu.mit.coeus.utils.CoeusTextField txtSsn;
    private edu.mit.coeus.utils.CoeusTextField txtUnitName;
    private edu.mit.coeus.utils.CoeusTextField txtUserName;
    // End of variables declaration//GEN-END:variables
    
    
}
