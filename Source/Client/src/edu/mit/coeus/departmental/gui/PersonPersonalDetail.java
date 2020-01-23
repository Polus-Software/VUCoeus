/**
 * @(#)PersonPersonalDetail.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 14-MAY-2007
 * by Leena
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.propdev.bean.PersonEditableColumnsFormBean;
import java.text.*;
import java.beans.*;
//import java.util.Calendar;
//import java.util.GregorianCalendar;
import java.util.Vector;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <CODE>PersonPersonalDetail </CODE>is a form object which display
 * the Person personal details and it is used to <CODE> display </CODE> the person details.
 * This class will be instantiated from <CODE>PersonDetailsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */

public class PersonPersonalDetail extends javax.swing.JComponent implements TypeConstants{
    
    private DepartmentPersonFormBean departmentPersonFormBean;
    private char functionType;
    private boolean saveRequired = false;
    private char moduleType;
    private boolean canMaintain;
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    private static final String EMPTY_STRING="";
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
    private static final char DEPARTMENT_PERSON_MODULE_CODE = 'D';
    private static final char PROPOSAL_PERSON_MODULE_CODE = 'P';
//    private String[] columnNamesToEdit;
    private DateUtils dtUtils;
    private Vector vecEditableColumnNames;
    private String focusDate;
    private SimpleDateFormat dtFormat;
    private CoeusMessageResources coeusMessageResources;
    //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //Removed '.', since while validation . is not used taking ProposalDetail screens as reference
    //private static final String DATE_SEPARATERS = ":/.,|-";
    private static final String DATE_SEPARATERS = ":/,|-";
    //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
    private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
    private boolean inDOBFocus,inRenFocus;
    
    // JM 02-26-2013 need loginUserName
    private boolean canModifyAllFields;
    // JM END
    
    /** Default Constructor */
    public PersonPersonalDetail() {
    }
    
    /** Creates new form <CODE>PersonPersonalDetail</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param personBean DepartmentPersonFormBean
     * 'D' specifies that the form is in Display Mode
     */
    // JM 2-27-2013 new instantiating method including canModifyAllFields;
    // logic moved to new instantiate method
    public PersonPersonalDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean, boolean canModifyAllFields) {
    	this.canModifyAllFields = canModifyAllFields;
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
    
    public PersonPersonalDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean) {
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
        
    public void instantiate(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean) {
        
        this.functionType = functionType;
        this.canMaintain = maintaintab;
        this.moduleType = moduleCode;
        this.departmentPersonFormBean = personBean;
        dtUtils = new DateUtils();
        dtFormat = new SimpleDateFormat("MM/dd/yyyy");
        coeusMessageResources = CoeusMessageResources.getInstance();
        //initComponents();
        //showPersonalDetails(departmentPersonFormBean);
        //setControlsEnabled(false);
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
                }
            });
        }
    }
    // JM END
    
    //    added for #2697 - start - 27/12/2006
    public void setFocusTraversal(){
        java.awt.Component[] components = {txtDOB, txtAge, txtAgeByFiscal, txtGender, txtRace,
        txtEducationalLevel, txtMajor, txtYearGraduated, txtSchool, txtDegree, txtIdProvided, txtIdVerified,
        txtCitizenship, chkHasVisa, txtVisaCode, txtVisaType, txtRenevalDate};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
    }
// added for #2697 - end - 27/12/2006
    
    public void showPersonal(){
        
        initComponents();
//        Commented for #2697 - start - 27/12/2006
        //Added by Amit 11/17/2003
//        java.awt.Component[] components = {txtDOB, txtAge, txtAgeByFiscal, txtGender, txtRace,
//        txtEducationalLevel, txtMajor, txtYearGraduated, txtSchool, txtDegree, txtIdProvided, txtIdVerified,
//        txtCitizenship, chkHasVisa, txtVisaCode, txtVisaType, txtRenevalDate};
//        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
//        setFocusTraversalPolicy(traversePolicy);
//        setFocusCycleRoot(true);
        //End Amit
//        Commented for #2697 - end - 27/12/2006
        showPersonalDetails(departmentPersonFormBean);
        
        txtDOB.addFocusListener(new CustomFocusAdapter());
        txtRenevalDate.addFocusListener(new CustomFocusAdapter());
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //If module type is personnel set the fields enabled if not in display mode
        if(moduleType == PERSONNEL_MODULE_CODE){
            if(functionType == TypeConstants.DISPLAY_MODE){
                setControlsEnabled(false);
            }
        }//Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        else if(moduleType == DEPARTMENT_PERSON_MODULE_CODE ){
            setControlsEnabled(false);
        }else if(moduleType == PROPOSAL_PERSON_MODULE_CODE){
            setControlsEnabled(false);
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            
            if(functionType != TypeConstants.DISPLAY_MODE && canMaintain){
//            if(canMaintain){//COEUSQA-2293: End
                setComponentsEnabled();
            }
        }
//      added for # 2697 -start  - 27/12/2006
        setFocusTraversal();
//      added for #2697 - end   - 27/12/2006
        
    	// JM 02-26-2013 set editable based on permissions
        java.awt.Component[] fields = {txtDOB, txtAge, txtAgeByFiscal, txtGender, txtRace,
                txtEducationalLevel, txtMajor, txtYearGraduated, txtSchool, txtDegree, txtIdProvided, txtIdVerified,
                txtCitizenship, chkHasVisa, txtVisaCode, txtVisaType, txtRenevalDate};
        if ((moduleType == PERSONNEL_MODULE_CODE) || (moduleType == DEPARTMENT_PERSON_MODULE_CODE)) {
        	for (int f=0; f < fields.length; f++) {
            	Component currField = fields[f];
            	currField.setEnabled(canModifyAllFields);
    }
        }
        else {
        	for (int f=0; f < fields.length; f++) {
            	Component currField = fields[f];
            	currField.setEnabled(false);
            }        	
        }
    	// JM END
    }
    
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
                    if(colName.equalsIgnoreCase("DATE_OF_BIRTH")){
                        txtDOB.setEditable(true);
                        txtDOB.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("GENDER")){
                        txtGender.setEditable(true);
                        txtGender.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("EDUCATION_LEVEL")){
                        txtEducationalLevel.setEditable(true);
                        txtEducationalLevel.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("SCHOOL")){
                        txtSchool.setEditable(true);
                        txtSchool.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("YEAR_GRADUATED")){
                        txtYearGraduated.setEditable(true);
                        txtYearGraduated.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("ID_PROVIDED")){
                        txtIdProvided.setEditable(true);
                        txtIdProvided.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("ID_VERIFIED")){
                        txtIdVerified.setEditable(true);
                        txtIdVerified.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("COUNTRY_OF_CITIZENSHIP")){
                        txtCitizenship.setEditable(true);
                        txtCitizenship.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("VISA_TYPE")){
                        txtVisaType.setEditable(true);
                        txtVisaType.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("RACE")){
                        txtRace.setEditable(true);
                        txtRace.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("AGE")){
                        txtAge.setEditable(true);
                        txtAge.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("DEGREE")){
                        txtDegree.setEditable(true);
                        txtDegree.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("HAS_VISA")){
                        chkHasVisa.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("VISA_CODE")){
                        txtVisaCode.setEditable(true);
                        txtVisaCode.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("VISA_RENEWAL_DATE")){
                        txtRenevalDate.setEditable(true);
                        txtRenevalDate.setEnabled(true);
                    }
                    //Modifed for case 2697: Tabbing Problem in Proposal Person Details - end
                }
            }
        }else{
            setControlsEnabled(false);
        }
    }
    
    /**
     * This method is invoked when the data should be displayed in the form
     * parameter DepartmentPersonFormBean
     *
     * @param departmentPersonFormBean as DepartmentPersonFormBean
     */
    private void showPersonalDetails(DepartmentPersonFormBean departmentPersonFormBean){
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //if(departmentPersonFormBean != null){
        //If the functionType is ADD_MODE, setting of fields with values is not required
        if(departmentPersonFormBean != null && functionType != ADD_MODE){
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            
            txtAge.setText(""+departmentPersonFormBean.getAge());
            txtAgeByFiscal.setText(""+departmentPersonFormBean.getAgeByFiscalYear());
            txtCitizenship.setText(departmentPersonFormBean.getCountryCitizenship()== null ? ""
                    :departmentPersonFormBean.getCountryCitizenship());
            txtDOB.setText(departmentPersonFormBean.getDateOfBirth()== null ? ""
                    :dtUtils.formatDate(
                    departmentPersonFormBean.getDateOfBirth().toString(),"dd-MMM-yyyy"));
            txtDegree.setText(departmentPersonFormBean.getDegree()== null ? ""
                    :departmentPersonFormBean.getDegree());
            txtEducationalLevel.setText(departmentPersonFormBean.getEduLevel()== null ? ""
                    :departmentPersonFormBean.getEduLevel());
            txtGender.setText(departmentPersonFormBean.getGender()== null ? ""
                    :departmentPersonFormBean.getGender());
            txtIdProvided.setText(departmentPersonFormBean.getProvided()== null ? ""
                    :departmentPersonFormBean.getProvided());
            txtIdVerified.setText(departmentPersonFormBean.getVerified()== null ? ""
                    :departmentPersonFormBean.getVerified());
            txtMajor.setText(departmentPersonFormBean.getMajor()== null ? ""
                    :departmentPersonFormBean.getMajor());
            txtRace.setText(departmentPersonFormBean.getRace()== null ? ""
                    :departmentPersonFormBean.getRace());
            txtRenevalDate.setText(departmentPersonFormBean.getVisaRenDate()== null ? ""
                    :dtUtils.formatDate(
                    departmentPersonFormBean.getVisaRenDate().toString(),"dd-MMM-yyyy"));
            txtSchool.setText(departmentPersonFormBean.getSchool()== null ? ""
                    :departmentPersonFormBean.getSchool());
            txtVisaCode.setText(departmentPersonFormBean.getVisaCode()== null ? ""
                    :departmentPersonFormBean.getVisaCode());
            txtVisaType.setText(departmentPersonFormBean.getVisaType()== null ? ""
                    :departmentPersonFormBean.getVisaType());
            txtYearGraduated.setText(departmentPersonFormBean.getYearGraduated()== null ? ""
                    :departmentPersonFormBean.getYearGraduated());
            if(departmentPersonFormBean.getHasVisa() == true){
                chkHasVisa.setSelected(true);
            }else{
                chkHasVisa.setSelected(false);
            }
            
        }
    }
    
    public boolean validateData() throws Exception{
        
        boolean valid = true;
        if(!validateDOB()){
            return false;
        }else if(!validateGraduationDate()){
            return false;
        }else if(!validateRenevalDate()){
            return false;
        }
        return valid;
    }
    
    /**
     * This method validates the DOB date text field component
     * If data is not entered this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateDOB() throws Exception{
        boolean valid = true;
        String dobDate = txtDOB.getText();
        String formattedDate = null;
        if (dobDate != null && dobDate.trim().length() > 0) {
            // validate date field
            if( !inDOBFocus ) {
                formattedDate = new DateUtils().restoreDate(dobDate,DATE_SEPARATERS);
                if( formattedDate!= null && formattedDate.equals( dobDate ) ) {
                    //Added for Coeus4.3 enhancement - 2356
                    //Validation done only while saving and to set focus to the component
                    txtDOB.requestFocus();
                    throw new Exception("Please enter valid date");
                }
            }else{
                //System.out.println("formatting if in focus");
                formattedDate = new DateUtils().formatDate(dobDate,
                        DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                //System.out.println("formattedDate:"+formattedDate);
                if(formattedDate == null) {
                    // invalid date
                    //CoeusOptionPane.showErrorDialog("Item '"+dobDate+"' does not pass validation test");
                    //Added for Coeus4.3 enhancement - 2356 -
                    //Validation done only while saving and to set focus to the component
                    txtDOB.requestFocus();
                    throw new Exception("Please enter valid date");
                    //txtDOB.setText(dobDate);
                    //valid = false;
                    //return valid;
                }
            }
        }
        if(valid){
            String convertedDate = dtUtils.formatDate(txtDOB.getText(),
                    "/-:," , "dd-MMM-yyyy");
            if(convertedDate!=null){
                txtDOB.setText(convertedDate);
            }
        }
        return valid;
    }
    /**
     * Validates the graduation year. Checks whether uset enterd a valid year of
     * the range 1900 - 9999
     * @return boolean true if valid
     * @throws Exception if invalid
     */
    public boolean validateGraduationDate() throws Exception{
        boolean valid = true;
        if(!txtYearGraduated.getText().trim().equals("")){
            String year = txtYearGraduated.getText().trim();
            if(year.length()>4){
                valid = false;
            }else{
                try{
                    int intYear = Integer.parseInt(year);
                    if(intYear<1900){
                        valid = false;
                    }
                }catch(NumberFormatException e){
                    valid = false;
                }
            }
        }
        if(!valid){
            txtYearGraduated.requestFocus();
            throw new Exception(coeusMessageResources.parseMessageKey("person_personal_exceptionCode.1100"));
        }
        return valid;
    }
    
    /**
     * This method validates the RENEVAL date text field component
     * If data is not entered this method shows the error
     * message to the user with appropriate message.
     */
    private boolean validateRenevalDate() throws Exception{
        boolean valid = true;
        String renevalDate = txtRenevalDate.getText();
        String formattedDate = null;
        if (renevalDate != null && renevalDate.trim().length() > 0) {
            // validate date field
            if( !inRenFocus ) {
                formattedDate = new DateUtils().restoreDate(renevalDate,DATE_SEPARATERS);
                if( formattedDate!= null && formattedDate.equals( renevalDate ) ) {
                    //Added for Coeus4.3 enhancement - 2356 -
                    //Validation done only while saving and to set focus to the component
                    txtRenevalDate.requestFocus();
                    throw new Exception("Please enter valid date");
                }
            }else{
                formattedDate = new DateUtils().formatDate(renevalDate,
                        DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(formattedDate == null) {
                    // invalid date
                    //CoeusOptionPane.showErrorDialog("Item '"+dobDate+"' does not pass validation test");
                    //Added for Coeus4.3 enhancement - 2356 -
                    //Validation done only while saving and to set focus to the component
                    txtRenevalDate.requestFocus();
                    throw new Exception("Please enter valid date");
                    //txtDOB.setText(dobDate);
                    //valid = false;
                    //return valid;
                }
            }
        }
        if(valid){
            String convertedDate = dtUtils.formatDate(txtRenevalDate.getText(),
                    "/-:," , "dd-MMM-yyyy");
            if(convertedDate!=null){
                txtRenevalDate.setText(convertedDate);
            }
        }
        return valid;
    }
    
    /**
     * This method is called for different functionType for setting the field
     * controls like enable or disable ,set the background color.
     *
     * @param functionType char
     */
    /**
     * private void setControls(char functionType){
     * //if (functionType == 'D'){
     * setControlsEnabled(false);
     * //}
     * }*/
    
    /**
     * This method is called for setting the controls of the text and combobox
     * component like enable or disable .
     *
     * @param value boolean ,if true enable else disable
     */
    
    private void setControlsEnabled(boolean value){
        
        txtAge.setEditable(value);
        txtAgeByFiscal.setEditable(value);
        txtCitizenship.setEditable(value);
        txtDOB.setEditable(value);
        txtDegree.setEditable(value);
        txtEducationalLevel.setEditable(value);
        txtGender.setEditable(value);
        txtIdProvided.setEditable(value);
        txtIdVerified.setEditable(value);
        txtMajor.setEditable(value);
        txtRace.setEditable(value);
        txtRenevalDate.setEditable(value);
        txtSchool.setEditable(value);
        txtVisaCode.setEditable(value);
        txtVisaType.setEditable(value);
        txtYearGraduated.setEditable(value);
        chkHasVisa.setEnabled(value);
        
        //Added for case 2697: Tabbing Problem in Proposal Person Details - start
        //For the tab traversal it checks whether the component is enabled or not
        txtAge.setEnabled(value);
        txtAgeByFiscal.setEnabled(value);
        txtCitizenship.setEnabled(value);
        txtDOB.setEnabled(value);
        txtDegree.setEnabled(value);
        txtEducationalLevel.setEnabled(value);
        txtGender.setEnabled(value);
        txtIdProvided.setEnabled(value);
        txtIdVerified.setEnabled(value);
        txtMajor.setEnabled(value);
        txtRace.setEnabled(value);
        txtRenevalDate.setEnabled(value);
        txtSchool.setEnabled(value);
        txtVisaCode.setEnabled(value);
        txtVisaType.setEnabled(value);
        txtYearGraduated.setEnabled(value);
        //Added for case 2697: Tabbing Problem in Proposal Person Details - end
        
    }
    
    /**
     * This method is used to set the form data specified in
     * <CODE>DepartmentPersonFormBean</CODE>
     */
    
    public void setFormData(DepartmentPersonFormBean personBean){
        showPersonalDetails(personBean);
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
    /** This method is used to get all the JTable data.
     * @return a DepartmentPersonFormBean.
     */
    public void getFormData(){
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Bug fix : Save person details if the person has canMaintain rights
        //no need to check for function type Start
        //if( functionType != 'D' && canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//&& !canMaintain
        //if( canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//&& !canMaintain
        //Bug fix : Save person details if the person has canMaintain rights
        //no need to check for function type Start
        //Commented for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
        //Included the PERSONNEL module type
        if(( canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE) ||
                (moduleType == PERSONNEL_MODULE_CODE)){
            //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
            departmentPersonFormBean.setGender(txtGender.getText().trim().length() ==0 ? null
                    : txtGender.getText());
            departmentPersonFormBean.setEduLevel(txtEducationalLevel.getText().trim().length() ==0 ? null
                    : txtEducationalLevel.getText());
            departmentPersonFormBean.setSchool(txtSchool.getText().trim().length() ==0 ? null
                    : txtSchool.getText());
            departmentPersonFormBean.setYearGraduated(txtYearGraduated.getText().trim().length() ==0 ? null
                    : txtYearGraduated.getText());
            departmentPersonFormBean.setProvided(txtIdProvided.getText().trim().length() ==0 ? null
                    : txtIdProvided.getText());
            departmentPersonFormBean.setVerified(txtIdVerified.getText().trim().length() ==0 ? null
                    : txtIdVerified.getText());
            departmentPersonFormBean.setCountryCitizenship(txtCitizenship.getText().trim().length() ==0 ? null
                    : txtCitizenship.getText());
            
            departmentPersonFormBean.setVisaType(txtVisaType.getText().trim().length() ==0 ? null
                    : txtVisaType.getText());
            //Modified to include to check for empty string -start
            if (!EMPTY_STRING.equals(txtAge.getText())) {
                departmentPersonFormBean.setAge(
                        Integer.parseInt((txtAge.getText()).trim()));
            }
            if (!EMPTY_STRING.equals(txtAgeByFiscal.getText())) {
                departmentPersonFormBean.setAgeByFiscalYear(
                        Integer.parseInt((txtAgeByFiscal.getText()).trim()));
            }
            //Modified to include to check for empty string -end
            departmentPersonFormBean.setRace(txtRace.getText().trim().length() ==0 ? null
                    : txtRace.getText());
            departmentPersonFormBean.setMajor(txtMajor.getText().trim().length() ==0 ? null
                    : txtMajor.getText());
            departmentPersonFormBean.setDegree(txtDegree.getText().trim().length() ==0 ? null
                    : txtDegree.getText());
            departmentPersonFormBean.setVisaCode(txtVisaCode.getText().trim().length() ==0 ? null
                    : txtVisaCode.getText());
            departmentPersonFormBean.setHasVisa(chkHasVisa.isSelected());
            String dobFormattedDate = txtDOB.getText();
            //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //DATE_SEPARATERS variable is used
            //String strDOBDate = dtUtils.restoreDate(dobFormattedDate,"/:-,");
            String strDOBDate = dtUtils.restoreDate(dobFormattedDate,DATE_SEPARATERS);
            //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - end
            java.sql.Date dobDate = null;
            try{
                if(strDOBDate != null && strDOBDate.trim().length() > 0){
                    dobDate = new java.sql.Date(dtFormat.parse(strDOBDate).getTime());
                }
            }catch(ParseException pe){
                saveRequired = true;
                pe.printStackTrace();
                
            }
            departmentPersonFormBean.setDateOfBirth(dobDate);
            
            String renevalFormattedDate = txtRenevalDate.getText();
            //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
            //DATE_SEPARATERS variable is used
            //String strRenevalDate = dtUtils.restoreDate(renevalFormattedDate,"/:-,");
            String strRenevalDate = dtUtils.restoreDate(renevalFormattedDate,DATE_SEPARATERS);
            //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
            java.sql.Date renevalDate = null;
            try{
                if(strRenevalDate != null && strRenevalDate.trim().length() > 0){
                    renevalDate = new java.sql.Date(dtFormat.parse(strRenevalDate).getTime());
                }
            }catch(ParseException pe){
                saveRequired = true;
                pe.printStackTrace();
            }
            departmentPersonFormBean.setVisaRenDate(renevalDate);
            
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

        lblDateOfBirth = new javax.swing.JLabel();
        lblAge = new javax.swing.JLabel();
        lblAgeForFiscalYear = new javax.swing.JLabel();
        lblGender = new javax.swing.JLabel();
        lblRace = new javax.swing.JLabel();
        lblEducationLevel = new javax.swing.JLabel();
        lblMajor = new javax.swing.JLabel();
        lblSchool = new javax.swing.JLabel();
        lblYearGraduated = new javax.swing.JLabel();
        lblDegree = new javax.swing.JLabel();
        lblIdProvided = new javax.swing.JLabel();
        lblIdVerified = new javax.swing.JLabel();
        lblCitizenShip = new javax.swing.JLabel();
        lblVisaCode = new javax.swing.JLabel();
        lblVisaType = new javax.swing.JLabel();
        lblVisaRenewalDate = new javax.swing.JLabel();
        chkHasVisa = new javax.swing.JCheckBox();
        lblHasVisa = new javax.swing.JLabel();
        txtGender = new edu.mit.coeus.utils.CoeusTextField();
        txtEducationalLevel = new edu.mit.coeus.utils.CoeusTextField();
        txtYearGraduated = new edu.mit.coeus.utils.CoeusTextField();
        txtIdProvided = new edu.mit.coeus.utils.CoeusTextField();
        txtCitizenship = new edu.mit.coeus.utils.CoeusTextField();
        txtVisaCode = new edu.mit.coeus.utils.CoeusTextField();
        txtRenevalDate = new edu.mit.coeus.utils.CoeusTextField();
        txtDOB = new edu.mit.coeus.utils.CoeusTextField();
        txtRace = new edu.mit.coeus.utils.CoeusTextField();
        txtMajor = new edu.mit.coeus.utils.CoeusTextField();
        txtDegree = new edu.mit.coeus.utils.CoeusTextField();
        txtIdVerified = new edu.mit.coeus.utils.CoeusTextField();
        txtVisaType = new edu.mit.coeus.utils.CoeusTextField();
        txtAgeByFiscal = new edu.mit.coeus.utils.CoeusTextField();
        txtAge = new edu.mit.coeus.utils.CoeusTextField();
        txtSchool = new edu.mit.coeus.utils.CoeusTextField();

        setLayout(new java.awt.GridBagLayout());

        lblDateOfBirth.setText("Date Of Birth:");
        lblDateOfBirth.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 2, 2);
        add(lblDateOfBirth, gridBagConstraints);

        lblAge.setText("Age:");
        lblAge.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 6, 2, 2);
        add(lblAge, gridBagConstraints);

        lblAgeForFiscalYear.setText("Age By  Fiscal Year:");
        lblAgeForFiscalYear.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblAgeForFiscalYear, gridBagConstraints);

        lblGender.setText("Gender:");
        lblGender.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblGender, gridBagConstraints);

        lblRace.setText("Race:");
        lblRace.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblRace, gridBagConstraints);

        lblEducationLevel.setText("Education Level:");
        lblEducationLevel.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblEducationLevel, gridBagConstraints);

        lblMajor.setText("Major:");
        lblMajor.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblMajor, gridBagConstraints);

        lblSchool.setText("School:");
        lblSchool.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblSchool, gridBagConstraints);

        lblYearGraduated.setText("Year Graduated:");
        lblYearGraduated.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblYearGraduated, gridBagConstraints);

        lblDegree.setText("Degree:");
        lblDegree.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblDegree, gridBagConstraints);

        lblIdProvided.setText("Id Provided:");
        lblIdProvided.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblIdProvided, gridBagConstraints);

        lblIdVerified.setText("Id Verified:");
        lblIdVerified.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblIdVerified, gridBagConstraints);

        lblCitizenShip.setText("Citizenship:");
        lblCitizenShip.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblCitizenShip, gridBagConstraints);

        lblVisaCode.setText("Visa Code:");
        lblVisaCode.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblVisaCode, gridBagConstraints);

        lblVisaType.setText("Visa Type:");
        lblVisaType.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblVisaType, gridBagConstraints);

        lblVisaRenewalDate.setText("Visa Renewal Date:");
        lblVisaRenewalDate.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 2);
        add(lblVisaRenewalDate, gridBagConstraints);

        chkHasVisa.setBackground(new java.awt.Color(204, 204, 204));
        chkHasVisa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkHasVisa, gridBagConstraints);

        lblHasVisa.setText("Has Visa: ");
        lblHasVisa.setFont(CoeusFontFactory.getLabelFont());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblHasVisa, gridBagConstraints);

        txtGender.setDocument(new LimitedPlainDocument(30));
        txtGender.setMaximumSize(new java.awt.Dimension(145, 20));
        txtGender.setMinimumSize(new java.awt.Dimension(145, 20));
        txtGender.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtGender, gridBagConstraints);

        txtEducationalLevel.setDocument(new LimitedPlainDocument(30));
        txtEducationalLevel.setMaximumSize(new java.awt.Dimension(145, 20));
        txtEducationalLevel.setMinimumSize(new java.awt.Dimension(145, 20));
        txtEducationalLevel.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtEducationalLevel, gridBagConstraints);

        txtYearGraduated.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,4));
        txtYearGraduated.setMaximumSize(new java.awt.Dimension(145, 20));
        txtYearGraduated.setMinimumSize(new java.awt.Dimension(145, 20));
        txtYearGraduated.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtYearGraduated, gridBagConstraints);

        txtIdProvided.setDocument(new LimitedPlainDocument(30));
        txtIdProvided.setMaximumSize(new java.awt.Dimension(145, 20));
        txtIdProvided.setMinimumSize(new java.awt.Dimension(145, 20));
        txtIdProvided.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtIdProvided, gridBagConstraints);

        txtCitizenship.setDocument(new LimitedPlainDocument(30));
        txtCitizenship.setMaximumSize(new java.awt.Dimension(145, 20));
        txtCitizenship.setMinimumSize(new java.awt.Dimension(145, 20));
        txtCitizenship.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtCitizenship, gridBagConstraints);

        txtVisaCode.setDocument(new LimitedPlainDocument(20));
        txtVisaCode.setMaximumSize(new java.awt.Dimension(145, 20));
        txtVisaCode.setMinimumSize(new java.awt.Dimension(145, 20));
        txtVisaCode.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtVisaCode, gridBagConstraints);

        txtRenevalDate.setDocument(new LimitedPlainDocument(12));
        txtRenevalDate.setMaximumSize(new java.awt.Dimension(145, 20));
        txtRenevalDate.setMinimumSize(new java.awt.Dimension(145, 20));
        txtRenevalDate.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtRenevalDate, gridBagConstraints);

        txtDOB.setDocument(new LimitedPlainDocument(12));
        txtDOB.setMaximumSize(new java.awt.Dimension(145, 20));
        txtDOB.setMinimumSize(new java.awt.Dimension(145, 20));
        txtDOB.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtDOB, gridBagConstraints);

        txtRace.setDocument(new LimitedPlainDocument(30));
        txtRace.setMaximumSize(new java.awt.Dimension(145, 20));
        txtRace.setMinimumSize(new java.awt.Dimension(145, 20));
        txtRace.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtRace, gridBagConstraints);

        txtMajor.setDocument(new LimitedPlainDocument(30));
        txtMajor.setMaximumSize(new java.awt.Dimension(145, 20));
        txtMajor.setMinimumSize(new java.awt.Dimension(145, 20));
        txtMajor.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtMajor, gridBagConstraints);

        txtDegree.setDocument(new LimitedPlainDocument(11));
        txtDegree.setMaximumSize(new java.awt.Dimension(145, 20));
        txtDegree.setMinimumSize(new java.awt.Dimension(145, 20));
        txtDegree.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtDegree, gridBagConstraints);

        txtIdVerified.setDocument(new LimitedPlainDocument(30));
        txtIdVerified.setMaximumSize(new java.awt.Dimension(145, 20));
        txtIdVerified.setMinimumSize(new java.awt.Dimension(145, 20));
        txtIdVerified.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtIdVerified, gridBagConstraints);

        txtVisaType.setDocument(new LimitedPlainDocument(30));
        txtVisaType.setMaximumSize(new java.awt.Dimension(145, 20));
        txtVisaType.setMinimumSize(new java.awt.Dimension(145, 20));
        txtVisaType.setPreferredSize(new java.awt.Dimension(145, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtVisaType, gridBagConstraints);

        txtAgeByFiscal.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAgeByFiscal.setMaximumSize(new java.awt.Dimension(45, 20));
        txtAgeByFiscal.setMinimumSize(new java.awt.Dimension(45, 20));
        txtAgeByFiscal.setPreferredSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(txtAgeByFiscal, gridBagConstraints);

        txtAge.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC,3));
        txtAge.setMaximumSize(new java.awt.Dimension(45, 20));
        txtAge.setMinimumSize(new java.awt.Dimension(45, 20));
        txtAge.setPreferredSize(new java.awt.Dimension(45, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 2, 0);
        add(txtAge, gridBagConstraints);

        txtSchool.setDocument(new LimitedPlainDocument(50));
        txtSchool.setMaximumSize(new java.awt.Dimension(392, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        add(txtSchool, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    /** Getter for property vecEditableColumnNames.
     * @return Value of property vecEditableColumnNames.
     */
    public Vector getVecEditableColumnNames() {
        return vecEditableColumnNames;
    }
    
    /** Setter for property vecEditableColumnNames.
     * @param vecEditableColumnNames New value of property vecEditableColumnNames.
     */
    public void setVecEditableColumnNames(Vector vecEditableColumnNames) {
        this.vecEditableColumnNames = vecEditableColumnNames;
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
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkHasVisa;
    private javax.swing.JLabel lblAge;
    private javax.swing.JLabel lblAgeForFiscalYear;
    private javax.swing.JLabel lblCitizenShip;
    private javax.swing.JLabel lblDateOfBirth;
    private javax.swing.JLabel lblDegree;
    private javax.swing.JLabel lblEducationLevel;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblHasVisa;
    private javax.swing.JLabel lblIdProvided;
    private javax.swing.JLabel lblIdVerified;
    private javax.swing.JLabel lblMajor;
    private javax.swing.JLabel lblRace;
    private javax.swing.JLabel lblSchool;
    private javax.swing.JLabel lblVisaCode;
    private javax.swing.JLabel lblVisaRenewalDate;
    private javax.swing.JLabel lblVisaType;
    private javax.swing.JLabel lblYearGraduated;
    private edu.mit.coeus.utils.CoeusTextField txtAge;
    private edu.mit.coeus.utils.CoeusTextField txtAgeByFiscal;
    private edu.mit.coeus.utils.CoeusTextField txtCitizenship;
    private edu.mit.coeus.utils.CoeusTextField txtDOB;
    private edu.mit.coeus.utils.CoeusTextField txtDegree;
    private edu.mit.coeus.utils.CoeusTextField txtEducationalLevel;
    private edu.mit.coeus.utils.CoeusTextField txtGender;
    private edu.mit.coeus.utils.CoeusTextField txtIdProvided;
    private edu.mit.coeus.utils.CoeusTextField txtIdVerified;
    private edu.mit.coeus.utils.CoeusTextField txtMajor;
    private edu.mit.coeus.utils.CoeusTextField txtRace;
    private edu.mit.coeus.utils.CoeusTextField txtRenevalDate;
    private edu.mit.coeus.utils.CoeusTextField txtSchool;
    private edu.mit.coeus.utils.CoeusTextField txtVisaCode;
    private edu.mit.coeus.utils.CoeusTextField txtVisaType;
    private edu.mit.coeus.utils.CoeusTextField txtYearGraduated;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Internal class which is used to listen to focus events of start date
     * and end date fields. On focusGained the date will be restored to
     * MM/dd/yyyy, and on focusLost the date will be formatted to dd-MMM-yyyy.
     */
    private class CustomFocusAdapter extends FocusAdapter{
        JTextField txtFld = null;
        String strData = "";
        boolean temporary = false;
        
        public void focusGained(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            if(fe.getSource().equals(txtDOB) || fe.getSource().equals(txtRenevalDate)){
                if( fe.getSource().equals(txtDOB) ) {
                    inDOBFocus = true;
                }else{
                    inRenFocus = true;
                }
                if ( (txtFld.getText() != null)
                && (!txtFld.getText().trim().equals("")) ) {
                    if(!strData.equals(txtFld.getText()) && !temporary){
                        //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - start
                        //DATE_SEPARATERS variable is used
                        //focusDate = dtUtils.restoreDate(txtFld.getText(),"/-:,");
                        focusDate = dtUtils.restoreDate(txtFld.getText(),DATE_SEPARATERS);
                        //Modified for Coeus Coeus 4.3 PT ID - 2388:Person Enhancements - end
                        txtFld.setText(focusDate);
                    }
                }
            }
        }
        
        public void focusLost(FocusEvent fe){
            txtFld = (JTextField)fe.getSource();
            /* check whether the focus lost is temporary or permanent*/
            temporary = fe.isTemporary();
            
//            String editingValue = null;
            if ( (txtFld.getText() != null)
            &&  (!txtFld.getText().trim().equals("")) && (!temporary)) {
                strData = txtFld.getText();
                
                if(fe.getSource().equals(txtDOB) || fe.getSource().equals(txtRenevalDate)){
                    String convertedDate = dtUtils.formatDate(txtFld.getText(),
                            "/-:," , "dd-MMM-yyyy");
                    if (convertedDate==null){
                        //Commented for Coeus 4.3 enhancement - start
                        //To remove the bug of the JOptionPane getting shown again and again
//                        CoeusOptionPane.showErrorDialog( "Please enter valid date" );
//                        txtFld.requestFocus();
//                        temporary = true;
                        //Commented for Coeus 4.3 enhancement - end
                    }else {
                        focusDate = txtFld.getText();
                        txtFld.setText(convertedDate);
                        temporary = false;
                        if( fe.getSource().equals(txtDOB) ) {
                            inDOBFocus = false;
                        }else{
                            inRenFocus = false;
                        }
                    }
                }
            }
        }
    }
}
