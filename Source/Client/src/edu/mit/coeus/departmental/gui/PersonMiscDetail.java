/**
 * @(#)PersonMiscDetail.java  1.0
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 16-MAY-2007
 * by Leena
 */

package edu.mit.coeus.departmental.gui;

import edu.mit.coeus.departmental.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import java.util.Vector;
import edu.mit.coeus.propdev.bean.PersonEditableColumnsFormBean;

//import java.awt.Color;
// JM needed for custom features
import java.awt.Component;
// JM END
import java.beans.*;

/**
 * <CODE>PersonMiscDetail</CODE>is a form object which display
 * the Person Miscellaneous details and it is used to <CODE> display </CODE> the person miscellaneous details.
 * This class will be instantiated from <CODE>PersonDetailsForm</CODE>.
 * @version 1.0 March 13, 2003
 * @author Raghunath P.V.
 */
public class PersonMiscDetail extends javax.swing.JComponent implements TypeConstants{
    
    private DepartmentPersonFormBean departmentPersonFormBean;
    private char functionType;
    private boolean saveRequired;
    private char moduleType;
    private boolean canMaintain;
    private static final char DEPARTMENT_PERSON_MODULE_CODE = 'D';
    private static final char PROPOSAL_PERSON_MODULE_CODE = 'P';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - start
    //used to denote the form is taken from the Personnel module of the Maintain Menu
    private static final char PERSONNEL_MODULE_CODE = 'N';
    //Added for Coeus 4.3 PT ID - 2388:Person Enhancements - end
//    private String[] columnNamesToEdit;
    private Vector vecEditableColumnNames;
    
    // JM 02-26-2013 need loginUserName
    private boolean canModifyAllFields;
    // JM END
    
    /** Default Constructor */
    public PersonMiscDetail() {
    }
    
    /** Creates new form <CODE>PersonOrganizationDetail</CODE>
     *
     * @param functionType this will open the different mode like Display
     * @param personBean DepartmentPersonFormBean
     * 'D' specifies that the form is in Display Mode
     */
    // JM 2-27-2013 new instantiating method including canModifyAllFields;
    // logic moved to new instantiate method
    public PersonMiscDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean, boolean canModifyAllFields) {
    	this.canModifyAllFields = canModifyAllFields;
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
    
    public PersonMiscDetail(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean personBean) {
    	instantiate(functionType, moduleCode, maintaintab, personBean);
    }
        
    public void instantiate(char functionType, char moduleCode, boolean maintaintab, DepartmentPersonFormBean departmentPersonFormBean) {
        
        this.functionType = functionType;
        this.departmentPersonFormBean = departmentPersonFormBean;
        this.canMaintain = maintaintab;
        this.moduleType = moduleCode;
        //initComponents();
        //showMiscDetails(departmentPersonFormBean);
        //setControls(functionType);
    }
    // JM END
    
//     added for #2697 - start - 27/12/2006
    public void setFocusTraversal(){
        java.awt.Component[] components = {chkHandicapped, txtHandicapType, chkVeteran, txtVeteranType,
        chkFaculty, chkResearchStaff,  chkserviceStaff ,chkSupportStaff,chkMedicalStaff, chkGraduateStudentStaff,
        chkSabbaticalStaff, chkVacationAccural, chkOtherAcademicStaff};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
//     added for #2697 - end - 27/12/2006
    
    public void showMisc(){
        
        initComponents();
//     Commented for #2697 - start - 27/12/2006
        //Added By Amit 11/17/2003
//        java.awt.Component[] components = {chkHandicapped, txtHandicapType, chkVeteran, txtVeteranType,
//        chkFaculty, chkResearchStaff,  chkserviceStaff ,chkSupportStaff,chkMedicalStaff, chkGraduateStudentStaff,
//        chkSabbaticalStaff, chkVacationAccural, chkOtherAcademicStaff};
//        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
//        setFocusTraversalPolicy(traversePolicy);
//        setFocusCycleRoot(true);
        //End Amit
//     Commented for #2697 - end - 27/12/2006
        
        showMiscDetails(departmentPersonFormBean);
        //Added for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //Set the controls disabled if the module type is PERSONNEL and in display mode
        if(moduleType == PERSONNEL_MODULE_CODE){
            if(functionType == TypeConstants.DISPLAY_MODE){
                setControlsEnabled(false);
            }
        }//Added for Coeus 4.3 PT ID - 2388:Person Enhancements -end
        else if( moduleType == DEPARTMENT_PERSON_MODULE_CODE ){
            setControlsEnabled(false);
        }else if(moduleType == PROPOSAL_PERSON_MODULE_CODE){
            setControlsEnabled(false);
            //Modified for COEUSQA-2293 - Citizenship Info field (other/custom data) should be editable in Person details on a proposal - Start
            if(functionType != TypeConstants.DISPLAY_MODE && canMaintain){//COEUSQA-2293 : End
//            if(canMaintain){
                setComponentsEnabled();
            }
        }
//     added for #2697 - start - 27/12/2006
        setFocusTraversal();
//     added for #2697 - end - 27/12/2006
        
    	// JM 02-26-2013 set editable based on permissions
        if ((moduleType == PERSONNEL_MODULE_CODE) || (moduleType == DEPARTMENT_PERSON_MODULE_CODE)) {
            java.awt.Component[] fields = {chkHandicapped, txtHandicapType, chkVeteran, txtVeteranType,
                    chkFaculty, chkResearchStaff,  chkserviceStaff ,chkSupportStaff,chkMedicalStaff, chkGraduateStudentStaff,
                    chkSabbaticalStaff, chkVacationAccural, chkOtherAcademicStaff};
            
            for (int f=0; f < fields.length; f++) {
            	Component currField = fields[f];
            	currField.setEnabled(canModifyAllFields);
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
                if(colName != null){
                    if(colName.equalsIgnoreCase("IS_HANDICAPPED")){
                        chkHandicapped.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_VETERAN")){
                        chkVeteran.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_FACULTY")){
                        chkFaculty.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_SUPPORT_STAFF")){
                        chkSupportStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_ON_SABBATICAL")){
                        chkSabbaticalStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_RESEARCH_STAFF")){
                        chkResearchStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_MEDICAL_STAFF")){
                        chkMedicalStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("VACATION_ACCURAL")){
                        chkVacationAccural.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_SERVICE_STAFF")){
                        chkserviceStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_GRADUATE_STUDENT_STAFF")){
                        chkGraduateStudentStaff.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("IS_OTHER_ACCADEMIC_GROUP")){
                        chkOtherAcademicStaff.setEnabled(true);
                    }
                    //Modifed for case 2697: Tabbing Problem in Proposal Person Details - start
                    //For the tab traversal it checks whether the component is enabled or not
                    else if(colName.equalsIgnoreCase("VETERAN_TYPE")){
                        txtVeteranType.setEditable(true);
                        txtVeteranType.setEnabled(true);
                    }else if(colName.equalsIgnoreCase("HANDICAP_TYPE")){
                        txtHandicapType.setEditable(true);
                        txtHandicapType.setEnabled(true);
                    }
                    //Modifed for case 2697: Tabbing Problem in Proposal Person Details - end
                }
            }
        }else{
            setControlsEnabled(false);
        }
    }
    /**
     * This method is called for different functionType for setting the field
     * controls like enable or disable ,set the background color.
     *
     * @param functionType char
     */
    
//    private void setControls(char functionType){
//        //if (functionType == 'D'){
//        setControlsEnabled(false);
//        //}
//    }
    
    /**
     * This method is called for setting the controls of the text and combobox
     * component like enable or disable .
     *
     * @param value boolean ,if true enable else disable
     */
    private void setControlsEnabled(boolean value){
        txtHandicapType.setEditable(value);
        txtVeteranType.setEditable(value);
        
        chkFaculty.setEnabled(value);
        chkGraduateStudentStaff.setEnabled(value);
        chkHandicapped.setEnabled(value);
        chkMedicalStaff.setEnabled(value);
        chkOtherAcademicStaff.setEnabled(value);
        chkResearchStaff.setEnabled(value);
        chkSabbaticalStaff.setEnabled(value);
        chkSupportStaff.setEnabled(value);
        chkVacationAccural.setEnabled(value);
        chkVeteran.setEnabled(value);
        chkserviceStaff.setEnabled(value);
        
        //Added for case 2697: Tabbing Problem in Proposal Person Details - start
        //For the tab traversal it checks whether the component is enabled or not
        txtHandicapType.setEnabled(value);
        txtVeteranType.setEnabled(value);
        //Added for case 2697: Tabbing Problem in Proposal Person Details - end
    }
    
    public boolean validateData(){
        return true;
    }
    
    /**
     * This method is invoked when the data should be displayed in the form
     * parameter DepartmentPersonFormBean
     *
     * @param departmentPersonFormBean as DepartmentPersonFormBean
     */
    
    private void showMiscDetails(DepartmentPersonFormBean departmentPersonFormBean){
        if(departmentPersonFormBean != null){
            
            txtHandicapType.setText(departmentPersonFormBean.getHandiCapType()== null ? ""
                    :departmentPersonFormBean.getHandiCapType());
            txtVeteranType.setText(departmentPersonFormBean.getVeteranType()== null ? ""
                    :departmentPersonFormBean.getVeteranType());
            
            if(departmentPersonFormBean.getVeteran() == true){
                chkVeteran.setSelected(true);
            }else{
                chkVeteran.setSelected(false);
            }
            if(departmentPersonFormBean.getFaculty() == true){
                chkFaculty.setSelected(true);
            }else{
                chkFaculty.setSelected(false);
            }
            if(departmentPersonFormBean.getGraduateStudentStaff() == true){
                chkGraduateStudentStaff.setSelected(true);
            }else{
                chkGraduateStudentStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getHandicap() == true){
                chkHandicapped.setSelected(true);
            }else{
                chkHandicapped.setSelected(false);
            }
            if(departmentPersonFormBean.getMedicalStaff() == true){
                chkMedicalStaff.setSelected(true);
            }else{
                chkMedicalStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getOtherAcademicGroup() == true){
                chkOtherAcademicStaff.setSelected(true);
            }else{
                chkOtherAcademicStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getResearchStaff() == true){
                chkResearchStaff.setSelected(true);
            }else{
                chkResearchStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getOnSabbatical() == true){
                chkSabbaticalStaff.setSelected(true);
            }else{
                chkSabbaticalStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getSupportStaff() == true){
                chkSupportStaff.setSelected(true);
            }else{
                chkSupportStaff.setSelected(false);
            }
            if(departmentPersonFormBean.getVacationAccural() == true){
                chkVacationAccural.setSelected(true);
            }else{
                chkVacationAccural.setSelected(false);
            }
            if(departmentPersonFormBean.getServiceStaff() == true){
                chkserviceStaff.setSelected(true);
            }else{
                chkserviceStaff.setSelected(false);
            }
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
    
    /**
     * This method is used to set the form data specified in
     * <CODE>DepartmentPersonFormBean</CODE>
     */
    public void setFormData(DepartmentPersonFormBean personBean){
        showMiscDetails(personBean);
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
        
        //Bug fix : Save person details if the person has canMaintain rights
        //no need to check for function type Start
        //if( functionType != 'D' && canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//!canMaintain &&
        //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -start
        //if( canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE){//!canMaintain &&
        if( (canMaintain && moduleType == PROPOSAL_PERSON_MODULE_CODE) ||
                (moduleType == PERSONNEL_MODULE_CODE)){
            //Modified for Coeus 4.3 PT ID - 2388:Person Enhancements -end
            //Bug fix : Save person details if the person has canMaintain rights
            //no need to check for function type End
            
            departmentPersonFormBean.setHandiCapType(txtHandicapType.getText().trim().length() ==0 ? null
                    : txtHandicapType.getText());
            departmentPersonFormBean.setVeteranType(txtVeteranType.getText().trim().length() ==0 ? null
                    : txtVeteranType.getText());
            departmentPersonFormBean.setGraduateStudentStaff(chkGraduateStudentStaff.isSelected());
            departmentPersonFormBean.setHandicap(chkHandicapped.isSelected());
            departmentPersonFormBean.setMedicalStaff(chkMedicalStaff.isSelected());
            departmentPersonFormBean.setOtherAcademicGroup(chkOtherAcademicStaff.isSelected());
            departmentPersonFormBean.setSupportStaff(chkSupportStaff.isSelected());
            departmentPersonFormBean.setServiceStaff(chkserviceStaff.isSelected());
            
            departmentPersonFormBean.setResearchStaff( chkResearchStaff.isSelected() );
            departmentPersonFormBean.setVacationAccural( chkVacationAccural.isSelected());
            departmentPersonFormBean.setVeteran(chkVeteran.isSelected());
            
            departmentPersonFormBean.setOnSabbatical(chkSabbaticalStaff.isSelected());
            departmentPersonFormBean.setFaculty(chkFaculty.isSelected());
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

        lblHandicapType = new javax.swing.JLabel();
        lblVeteranType = new javax.swing.JLabel();
        chkHandicapped = new javax.swing.JCheckBox();
        chkVeteran = new javax.swing.JCheckBox();
        chkFaculty = new javax.swing.JCheckBox();
        chkResearchStaff = new javax.swing.JCheckBox();
        chkserviceStaff = new javax.swing.JCheckBox();
        chkMedicalStaff = new javax.swing.JCheckBox();
        chkSupportStaff = new javax.swing.JCheckBox();
        chkGraduateStudentStaff = new javax.swing.JCheckBox();
        chkSabbaticalStaff = new javax.swing.JCheckBox();
        chkVacationAccural = new javax.swing.JCheckBox();
        chkOtherAcademicStaff = new javax.swing.JCheckBox();
        lblResearchStaff = new javax.swing.JLabel();
        lblMedicalStaff = new javax.swing.JLabel();
        lblVacationAccural = new javax.swing.JLabel();
        txtHandicapType = new edu.mit.coeus.utils.CoeusTextField();
        txtVeteranType = new edu.mit.coeus.utils.CoeusTextField();
        lblHandicap = new javax.swing.JLabel();
        lblVeteran = new javax.swing.JLabel();
        lblServiceStaff = new javax.swing.JLabel();
        lblGraduateStudentStaff = new javax.swing.JLabel();
        lblOtherAcademicGroup = new javax.swing.JLabel();
        lblFaculty = new javax.swing.JLabel();
        lblSupportStaff = new javax.swing.JLabel();
        lblOnSabbaticalStaff = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblHandicapType.setFont(CoeusFontFactory.getLabelFont());
        lblHandicapType.setText("Handicap Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblHandicapType, gridBagConstraints);

        lblVeteranType.setFont(CoeusFontFactory.getLabelFont());
        lblVeteranType.setText("Veteran Type: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblVeteranType, gridBagConstraints);

        chkHandicapped.setFont(CoeusFontFactory.getLabelFont());
        chkHandicapped.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkHandicapped, gridBagConstraints);

        chkVeteran.setFont(CoeusFontFactory.getLabelFont());
        chkVeteran.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkVeteran, gridBagConstraints);

        chkFaculty.setFont(CoeusFontFactory.getLabelFont());
        chkFaculty.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkFaculty, gridBagConstraints);

        chkResearchStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkResearchStaff, gridBagConstraints);

        chkserviceStaff.setFont(CoeusFontFactory.getLabelFont());
        chkserviceStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        add(chkserviceStaff, gridBagConstraints);

        chkMedicalStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkMedicalStaff, gridBagConstraints);

        chkSupportStaff.setFont(CoeusFontFactory.getLabelFont());
        chkSupportStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkSupportStaff, gridBagConstraints);

        chkGraduateStudentStaff.setFont(CoeusFontFactory.getLabelFont());
        chkGraduateStudentStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkGraduateStudentStaff.setMaximumSize(new java.awt.Dimension(15, 21));
        chkGraduateStudentStaff.setMinimumSize(new java.awt.Dimension(15, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        add(chkGraduateStudentStaff, gridBagConstraints);

        chkSabbaticalStaff.setFont(CoeusFontFactory.getLabelFont());
        chkSabbaticalStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkSabbaticalStaff, gridBagConstraints);

        chkVacationAccural.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(chkVacationAccural, gridBagConstraints);

        chkOtherAcademicStaff.setFont(CoeusFontFactory.getLabelFont());
        chkOtherAcademicStaff.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        chkOtherAcademicStaff.setMaximumSize(new java.awt.Dimension(15, 21));
        chkOtherAcademicStaff.setMinimumSize(new java.awt.Dimension(15, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        add(chkOtherAcademicStaff, gridBagConstraints);

        lblResearchStaff.setFont(CoeusFontFactory.getLabelFont());
        lblResearchStaff.setText("Research Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblResearchStaff, gridBagConstraints);

        lblMedicalStaff.setFont(CoeusFontFactory.getLabelFont());
        lblMedicalStaff.setText("Medical Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblMedicalStaff, gridBagConstraints);

        lblVacationAccural.setFont(CoeusFontFactory.getLabelFont());
        lblVacationAccural.setText("Vacation Accural: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblVacationAccural, gridBagConstraints);

        txtHandicapType.setDocument(new LimitedPlainDocument(30));
        txtHandicapType.setMaximumSize(new java.awt.Dimension(150, 20));
        txtHandicapType.setMinimumSize(new java.awt.Dimension(150, 20));
        txtHandicapType.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        add(txtHandicapType, gridBagConstraints);

        txtVeteranType.setDocument(new LimitedPlainDocument(30));
        txtVeteranType.setMaximumSize(new java.awt.Dimension(150, 20));
        txtVeteranType.setMinimumSize(new java.awt.Dimension(150, 20));
        txtVeteranType.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 3);
        add(txtVeteranType, gridBagConstraints);

        lblHandicap.setFont(CoeusFontFactory.getLabelFont());
        lblHandicap.setText("Handicapped: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblHandicap, gridBagConstraints);

        lblVeteran.setFont(CoeusFontFactory.getLabelFont());
        lblVeteran.setText("Veteran: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblVeteran, gridBagConstraints);

        lblServiceStaff.setFont(CoeusFontFactory.getLabelFont());
        lblServiceStaff.setText("Service Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblServiceStaff, gridBagConstraints);

        lblGraduateStudentStaff.setFont(CoeusFontFactory.getLabelFont());
        lblGraduateStudentStaff.setText("Graduate Student Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblGraduateStudentStaff, gridBagConstraints);

        lblOtherAcademicGroup.setFont(CoeusFontFactory.getLabelFont());
        lblOtherAcademicGroup.setText("OSP Awards Team: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        add(lblOtherAcademicGroup, gridBagConstraints);

        lblFaculty.setFont(CoeusFontFactory.getLabelFont());
        lblFaculty.setText("Faculty: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblFaculty, gridBagConstraints);

        lblSupportStaff.setFont(CoeusFontFactory.getLabelFont());
        lblSupportStaff.setText("Support Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblSupportStaff, gridBagConstraints);

        lblOnSabbaticalStaff.setFont(CoeusFontFactory.getLabelFont());
        lblOnSabbaticalStaff.setText("On Sabbatical Staff: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(lblOnSabbaticalStaff, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
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
    private javax.swing.JCheckBox chkFaculty;
    private javax.swing.JCheckBox chkGraduateStudentStaff;
    private javax.swing.JCheckBox chkHandicapped;
    private javax.swing.JCheckBox chkMedicalStaff;
    private javax.swing.JCheckBox chkOtherAcademicStaff;
    private javax.swing.JCheckBox chkResearchStaff;
    private javax.swing.JCheckBox chkSabbaticalStaff;
    private javax.swing.JCheckBox chkSupportStaff;
    private javax.swing.JCheckBox chkVacationAccural;
    private javax.swing.JCheckBox chkVeteran;
    private javax.swing.JCheckBox chkserviceStaff;
    private javax.swing.JLabel lblFaculty;
    private javax.swing.JLabel lblGraduateStudentStaff;
    private javax.swing.JLabel lblHandicap;
    private javax.swing.JLabel lblHandicapType;
    private javax.swing.JLabel lblMedicalStaff;
    private javax.swing.JLabel lblOnSabbaticalStaff;
    private javax.swing.JLabel lblOtherAcademicGroup;
    private javax.swing.JLabel lblResearchStaff;
    private javax.swing.JLabel lblServiceStaff;
    private javax.swing.JLabel lblSupportStaff;
    private javax.swing.JLabel lblVacationAccural;
    private javax.swing.JLabel lblVeteran;
    private javax.swing.JLabel lblVeteranType;
    private edu.mit.coeus.utils.CoeusTextField txtHandicapType;
    private edu.mit.coeus.utils.CoeusTextField txtVeteranType;
    // End of variables declaration//GEN-END:variables
    
}
