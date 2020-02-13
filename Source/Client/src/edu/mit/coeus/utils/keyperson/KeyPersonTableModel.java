/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * KeyPersonTableModel.java
 *
 * Created on January 13, 2009, 6:52 PM
 */

package edu.mit.coeus.utils.keyperson;


import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author sreenathv
 */
public class KeyPersonTableModel  extends AbstractTableModel{
    
    private CoeusVector cvKeyPersonData;
    private String EMPTY_STRING = "";
    
    // Specifying the columns numbers for the Key person details.
    private static final int KEY_PERSON_HAND_ICON_COLUMN = 0;
    private static final int KEY_PERSON_NAME_COLUMN = 1;
    private static final int KEY_PERSON_ROLE_COLUMN = 2;
    // JM 6-14-2012 reordered columns
    private static final int KEY_PERSON_FACULTY_COLUMN = 3;
    private static final int KEY_PERSON_EFFORT_COLUMN = 4;
    // JM END
    private static final int KEY_PERSON_ACAD_YEAR_COLUMN= 5;
    private static final int KEY_PERSON_SUM_EFFORT_COLUMN= 6;
    private static final int KEY_PERSON_CAL_YEAR_COLUMN = 7;
    private static final int KEY_PERSON_ID_COLUMN = 8;
    private static final int KEY_PERSON_EMPLOYEE_FLAG_COLUMN = 9;
    
    //ppc change for keyperson starts
    private static final int KEY_PERSON_CERTIFY_FLAG_COLUMN=10;
    //ppc change for keyperson ends
    
    /* JM 9-8-2015 add person status */
    public static final int KEY_PERSON_STATUS_COLUMN = 11;
    /* JM END */
    
    /* JM 2-11-2016 add IS_EXTERNAL_PERSON flag */
    public static final int KEY_PERSON_IS_EXTERNAL_PERSON = 12;
    /* JM END */
    
    // JM 6-14-2012 updated column heading names and reordered
    // JM 9-8-2015 add person status
    // JM 2-11-2016 added IS_EXTERNAL_PERSON column
    private String[] colNames = {EMPTY_STRING, "Name", "Role", "Faculty", 
    		"Effort %", "<html>Academic <BR>Effort %</html>","<html>Summmer <BR>Effort %</html>",
    		"<html>Calendar <BR>Effort %</html>", "Id", "EmployeeFlag","Certify","Status","Is External Person" };
    private Class colClass[] = {ImageIcon.class,  String.class, String.class, Boolean.class,
    		Double.class,Double.class, Double.class, Double.class, String.class, Boolean.class,ImageIcon.class,
    		String.class,String.class};
    // JM END

    private KeyPersonController controller;
    private CoeusMessageResources messageResources = CoeusMessageResources.getInstance();
    private boolean editable;
      private CoeusVector cvUnitInvestigator;
    
    /** Creates a new instance of KeyPersonTableModel */
  
    public KeyPersonBean KeyPersonTableModel(int row){
        if( cvKeyPersonData != null && cvKeyPersonData.size() > row ) {
            return (KeyPersonBean)cvKeyPersonData.get(row);
        }
        return null;
    }
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row,int col) {
        if( isEditable() ) {
        	// JM 6-15-2012 all effort fields should be editable
            // JM 9-8-2015 add person status
            // JM 2-11-2016 added IS_EXTERNAL_PERSON column
            //if( col == KEY_PERSON_HAND_ICON_COLUMN || col == KEY_PERSON_EMPLOYEE_FLAG_COLUMN || col == KEY_PERSON_ID_COLUMN ||
            //        col == KEY_PERSON_CAL_YEAR_COLUMN  || col == KEY_PERSON_SUM_EFFORT_COLUMN ||  col == KEY_PERSON_ACAD_YEAR_COLUMN
            //        ||col == KEY_PERSON_CERTIFY_FLAG_COLUMN) {
            if( col == KEY_PERSON_HAND_ICON_COLUMN || col == KEY_PERSON_EMPLOYEE_FLAG_COLUMN || col == KEY_PERSON_ID_COLUMN ||
                        col == KEY_PERSON_CERTIFY_FLAG_COLUMN || col == KEY_PERSON_STATUS_COLUMN ||
                        col == KEY_PERSON_IS_EXTERNAL_PERSON) {
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    public KeyPersonBean getKeypersonBean(int row){
        if( cvKeyPersonData != null && cvKeyPersonData.size() > row ) {
            return (KeyPersonBean)cvKeyPersonData.get(row);
        }
        return null;
    }
    
    /**
     *This method is to get the column name
     *@param int column
     *@return String
     */
    public String getColumnName(int column) {
        return colNames[column];
    }
       public KeyPersonBean getUnitBean(int selRow){
        if( cvUnitInvestigator != null && cvUnitInvestigator.size() > selRow) {
            return (KeyPersonBean) cvUnitInvestigator.get(selRow);
        }
        return null;
    }
    /**
     *This method is to get the column class
     *@param int columnIndex
     *@return Class
     */
    public Class getColumnClass(int columnIndex) {
        return colClass [columnIndex];
    }
    
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return colNames.length;
    }
    
    /**
     *This method is to get the row count
     *@return int
     */
    public int getRowCount() {
        if (cvKeyPersonData == null){
            return 0;
        }
        return cvKeyPersonData.size();
    }
    
    /**
     *This method will specifies the data for the table model. Depending upon the value
     *passed, it will hold the Award or Institute Proposal Detail vestor
     *@param CoeusVector cvData
     *@return void
     */
    public void setData(CoeusVector cvData){
        cvKeyPersonData = cvData;
    }
    
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col) {
        KeyPersonBean  keyPersonBean = (KeyPersonBean) cvKeyPersonData.get(row);
        switch(col) {
            case KEY_PERSON_HAND_ICON_COLUMN:
                return EMPTY_STRING;
            case KEY_PERSON_NAME_COLUMN:
                return keyPersonBean.getPersonName();
            case KEY_PERSON_ROLE_COLUMN:
                return keyPersonBean.getProjectRole();
            case KEY_PERSON_EFFORT_COLUMN:
                return new Double(keyPersonBean.getPercentageEffort());
            case KEY_PERSON_FACULTY_COLUMN:
                return new Boolean(keyPersonBean.isFacultyFlag());
            case KEY_PERSON_ACAD_YEAR_COLUMN:
                return new Double(keyPersonBean.getAcademicYearEffort());
            case KEY_PERSON_CAL_YEAR_COLUMN:
                return new Double(keyPersonBean.getCalendarYearEffort());
            case KEY_PERSON_SUM_EFFORT_COLUMN:
                return new Double(keyPersonBean.getSummerYearEffort());
            case KEY_PERSON_EMPLOYEE_FLAG_COLUMN:
                return new Boolean(keyPersonBean.isNonMITPersonFlag());
            case KEY_PERSON_ID_COLUMN:
                return keyPersonBean.getPersonId();
           	/* JM 9-8-2015 added person status */
            case KEY_PERSON_STATUS_COLUMN:
            	return keyPersonBean.getStatus();
            /* JM END */
           	/* JM 2-11-2016 added IS_EXTERNAL_PERSON column */
            case KEY_PERSON_IS_EXTERNAL_PERSON:
            	return keyPersonBean.getIsExternalPerson();
            /* JM END */
            }
        return EMPTY_STRING;
    }
    
    public KeyPersonBean getKeyPersonBean(int row){
        if( cvKeyPersonData != null && cvKeyPersonData.size() > row ) {
            return (KeyPersonBean)cvKeyPersonData.get(row);
        }
        return null;
    }
    /**
     *This method is to set the value with respect to the row and column
     *@param Object value
     *@param int row
     *@param int col
     *@return void
     */
    public void setValueAt(Object value, int row, int col) {
        KeyPersonBean  keyPersonBean = (KeyPersonBean) cvKeyPersonData.get(row);
        String acType = keyPersonBean.getAcType();
        switch(col) {
            case KEY_PERSON_HAND_ICON_COLUMN:
                break;
            case KEY_PERSON_NAME_COLUMN:
                String personName = (String)value;
                if( personName == null ) {
                    personName = "";
                }
                String oldPersonName = keyPersonBean.getPersonName();
                if( oldPersonName != null ) {
                    oldPersonName = oldPersonName.trim();
                }
                if( !personName.trim().equalsIgnoreCase(oldPersonName) ){
                    updatePersonDetails(row, personName);
                }
                
                break;
            case KEY_PERSON_ROLE_COLUMN:
                String projRole = (String)value;
                if( projRole == null ) {
                    projRole = "";
                }
                String oldprojRole = keyPersonBean.getProjectRole();
                if( oldprojRole != null ) {
                    oldprojRole= oldprojRole.trim();
                }
                if( !projRole.trim().equalsIgnoreCase(oldprojRole) ){
                    keyPersonBean.setProjectRole(projRole);
                    if( null == acType ) {
                        keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    controller.setDataChanged(true);
                }
                break;
            case KEY_PERSON_EFFORT_COLUMN:
                Double effortValue = new Double(0.00);
                try{
                    effortValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    effortValue = new Double(0.00);
                }
                keyPersonBean.setPercentageEffort(effortValue.floatValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                    
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_FACULTY_COLUMN:
                Boolean facultyValue = (Boolean)value;
                keyPersonBean.setFacultyFlag(facultyValue.booleanValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_ACAD_YEAR_COLUMN:
                Double academicYearValue = new Double(0.00);
                try{
                    academicYearValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    academicYearValue = new Double(0.00);
                }
                keyPersonBean.setAcademicYearEffort(academicYearValue.floatValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_CAL_YEAR_COLUMN:
                Double calYearValue = new Double(0.00);
                try{
                    calYearValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    calYearValue = new Double(0.00);
                }
                keyPersonBean.setCalendarYearEffort(calYearValue.floatValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_SUM_EFFORT_COLUMN:
                Double sumEffortValue = new Double(0.00);
                try{
                    sumEffortValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    sumEffortValue = new Double(0.00);
                }
                keyPersonBean.setSummerYearEffort(sumEffortValue.floatValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_EMPLOYEE_FLAG_COLUMN:
                Boolean employee = (Boolean)value;
                keyPersonBean.setNonMITPersonFlag(employee.booleanValue());
                if( null == acType ) {
                    keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case KEY_PERSON_ID_COLUMN:
                String personId = (String)value;
                if( personId == null ) {
                    personId = "";
                }
                String oldpersonId = keyPersonBean.getProjectRole();
                if( oldpersonId != null ) {
                    oldpersonId= oldpersonId.trim();
                }
                if( !personId.trim().equalsIgnoreCase(oldpersonId) ){
                    keyPersonBean.setProjectRole(personId);
                    if( null == acType ) {
                        keyPersonBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                    controller.setDataChanged(true);
                }
                break;
        }
    }
    
    public CoeusVector getData(){
        return cvKeyPersonData;
    }
    public void setController(KeyPersonController controller){
        this.controller = controller;
    }
    
    /**
     * Method to check the passed personName is valid or not. 
     * @param String personName
     * @return PersonInfoFormBean if it is a vlid Person Name otherwise null.
    */
    private PersonInfoFormBean checkPersonName( String personName ){
        
        PersonInfoFormBean personInfo = null;
        RequesterBean requesterBean = new RequesterBean();
        ResponderBean responderBean = new ResponderBean();
        requesterBean.setFunctionType('J');
        requesterBean.setDataObject(personName);
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/unitServlet";
        
        AppletServletCommunicator comm = new AppletServletCommunicator
                (connectTo, requesterBean);
        comm.setRequest(requesterBean);
        comm.send();
        responderBean = comm.getResponse();
        if(responderBean != null){
            if(responderBean.isSuccessfulResponse()) {
                personInfo =(PersonInfoFormBean)responderBean.getDataObject();
                if(personInfo.getPersonID() == null){
                    CoeusOptionPane.showErrorDialog(messageResources.parseMessageKey("keyPerson_exceptionCode.1605"));
                    personInfo = null;
                }else if(personInfo.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    CoeusOptionPane.showErrorDialog
                            ("\""+personName+"\""+" found more than once in person table.\n Use find to choose a person.");
                    personInfo = null;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
            }
        }
        return personInfo;
    }
    
        /** Method used to construct the KeyPerson Bean by fetching
         *  the details from the database for the passed person name. 
         *  @param int selectedRow
         *  @param String personName
         */
    private void updatePersonDetails(int selectedRow, String personName){
        
        KeyPersonBean oldKeyPerson = (KeyPersonBean)cvKeyPersonData.get(selectedRow);
        if(personName != null && personName.trim().length()>0){
            // Get the person details from database for the entered name.
            //  If the person already exists checkPersonName returns null
            PersonInfoFormBean personInfo = checkPersonName( personName.trim() );
            if(personInfo != null){
                KeyPersonBean keyPersonBean = new KeyPersonBean();
                keyPersonBean.setPersonId(personInfo.getPersonID());
                keyPersonBean.setPersonName(personInfo.getFullName());
                keyPersonBean.setProjectRole(personInfo.getDirTitle());
                keyPersonBean.setPercentageEffort(
                        ((Double)getValueAt(selectedRow,KEY_PERSON_EFFORT_COLUMN )).floatValue());
                keyPersonBean.setAcademicYearEffort(
                        ((Double)getValueAt(selectedRow,KEY_PERSON_ACAD_YEAR_COLUMN)).floatValue());
                keyPersonBean.setSummerYearEffort(
                        ((Double)getValueAt(selectedRow,KEY_PERSON_SUM_EFFORT_COLUMN)).floatValue());
                keyPersonBean.setCalendarYearEffort(
                        ((Double)getValueAt(selectedRow,KEY_PERSON_CAL_YEAR_COLUMN)).floatValue());
                keyPersonBean.setNonMITPersonFlag(false);
                keyPersonBean.setFacultyFlag(
                        personInfo.getFacFlag().equalsIgnoreCase("y")
                        ? true : false);
                
                keyPersonBean.setAcType( TypeConstants.INSERT_RECORD );
                controller.setkeyPersonBean(keyPersonBean,true);
                
            }
        }
    }
    /** Getter for property editable.
     * @return Value of property editable.
     *
     */
    public boolean isEditable() {
        return editable;
    }
    
    /** Setter for property editable.
     * @param editable New value of property editable.
     *
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}
