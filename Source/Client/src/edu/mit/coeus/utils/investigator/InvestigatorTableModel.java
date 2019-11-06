/*
 * InvestigatorTableModel.java
 * Created on March 26, 2004, 1:55 PM
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.irb.bean.PersonInfoFormBean;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.util.Hashtable;
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class InvestigatorTableModel extends AbstractTableModel {
    /**
     * Creates a new instance of InvestigatorTableModel
     */
    public InvestigatorTableModel() {
    }
    
    // Represents the Coeus Vector
    private CoeusVector cvInvestigatorData;
    
    //Represents the empty string
    private String EMPTY_STRING = "";
    
    // Specifying the columns numbers for the investigators details.
    private static final int INVESTIGATOR_HAND_ICON_COLUMN = 0;
    private static final int INVESTIGATOR_NAME_COLUMN = 1;
    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - start
    //Changed the column order
    private static final int INVESTIGATOR_PI_COLUMN = 2;
    private static final int INVETIGATOR_MULTI_PI_COLUMN = 3;
    private static final int INVESTIGATOR_FACULTY_COLUMN = 4;
    private static final int INVESTIGATOR_EFFORT_COLUMN = 5;
    //Modified for Coeus 4.3 -PT ID:2229 Multi PI - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
    private static final int INVESTIGATOR_ACAD_YEAR_COLUMN=6;
    private static final int INVESTIGATOR_SUM_EFFORT_COLUMN=7;
    private static final int INVESTIGATOR_CAL_YEAR_COLUMN =8;
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
        
    /* JM 9-10-2015 add person status */
    public static final int INVESTIGATOR_STATUS_COLUMN = 9;
    /* JM END */
    
    /* JM 2-2-2016 added isExternalPerson flag */
    public static final int INVESTIGATOR_IS_EXTERNAL_PERSON_COLUMN = 10;
    /* JM END */
    
    /** Specifies the column Names */
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - start
    //private String[] colNames = {EMPTY_STRING, "Person Name","%Effort","PI","Faculty"};
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort - end
    // JM 6-21-2012 updated effort column heading names
    // JM 9-8-2015 add person status
    // JM 2-2-2016 added isExternalPerson column
    private String[] colNames = {EMPTY_STRING, "Person Name","PI","Multi PI","Faculty",
    "Effort %","<html>Academic <br>Effort %</html>", "<html>Summer <br>Effort %</html>",
    "<html>Calendar<br>Effort %</html>","Status","Is External Person"
    };
    // JM END
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort - end
    
    /** Specifies the column class and its data types as objects */
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - start
//    private Class colClass[] = {ImageIcon.class, String.class, Double.class, Boolean.class,
//    Boolean.class};
    //Commented for Coeus 4.3-PT ID:2270 Tracking Effort - end
    
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort - start
    // JM 9-8-2015 add person status
    // JM 2-2-2016 added isExternalPerson flag 
    private Class colClass[] = {ImageIcon.class, String.class,  Boolean.class,
    Boolean.class, Boolean.class, Double.class, Double.class, Double.class, Double.class, String.class,String.class};
    //Added for Coeus 4.3-PT ID:2270 Tracking Effort - end
    
    private InvestigatorController controller;
    private CoeusMessageResources messageResources = CoeusMessageResources.getInstance();
    private boolean editable;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    private String moduleName;
    private String moduleNumber;
    private String seqNo;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start    
    
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row,int col) {
        if( isEditable() ) {
            if( col == INVESTIGATOR_HAND_ICON_COLUMN ) {
                return false;
            }
            InvestigatorBean investigatorBean =
                    (InvestigatorBean) cvInvestigatorData.get(row);
            
            return true;
        }
        return false;
    }
    
    /**
     *This method is to get the column name
     *@param int column
     *@return String
     */
    public String getColumnName(int column) {
        return colNames[column];
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
        if (cvInvestigatorData == null) return 0;
        return cvInvestigatorData.size();
    }
    
    /**
     *This method will specifies the data for the table model. Depending upon the value
     *passed, it will hold the Award or Institute Proposal Detail vector
     *@param CoeusVector cvData
     *@return void
     */
    public void setData(CoeusVector cvData){
        cvInvestigatorData = cvData;
    }
    
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col) {
        InvestigatorBean  investigatorBean = (InvestigatorBean) cvInvestigatorData.get(row);
        switch(col) {
            case INVESTIGATOR_HAND_ICON_COLUMN:
                return EMPTY_STRING;
            case INVESTIGATOR_NAME_COLUMN:
                return investigatorBean.getPersonName();
            case INVESTIGATOR_EFFORT_COLUMN:
                return new Double(investigatorBean.getPercentageEffort());
            case INVESTIGATOR_PI_COLUMN:
                return new Boolean(investigatorBean.isPrincipalInvestigatorFlag());
            case INVESTIGATOR_FACULTY_COLUMN:
                return new Boolean(investigatorBean.isFacultyFlag());
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - start
            case INVETIGATOR_MULTI_PI_COLUMN:
                return new Boolean(investigatorBean.isMultiPIFlag());
                //Added for Coeus 4.3 -PT ID:2229 Multi PI - end
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- start
            case INVESTIGATOR_ACAD_YEAR_COLUMN:
                return new Double(investigatorBean.getAcademicYearEffort());
            case INVESTIGATOR_CAL_YEAR_COLUMN:
                return new Double(investigatorBean.getCalendarYearEffort());
            case INVESTIGATOR_SUM_EFFORT_COLUMN:
                return new Double(investigatorBean.getSummerYearEffort());
                //Added for Coeus 4.3-PT ID:2270 Tracking Effort- end
            /* JM 9-10-2015 added person status */
            case INVESTIGATOR_STATUS_COLUMN:
            	return investigatorBean.getStatus();
            /* JM 2-2-2016 added isExternalPerson flag */
            case INVESTIGATOR_IS_EXTERNAL_PERSON_COLUMN:
              	return investigatorBean.getIsExternalPerson();
        }
        return EMPTY_STRING;
    }
    
    public InvestigatorBean getInvestigatorBean(int row){
        if( cvInvestigatorData != null && cvInvestigatorData.size() > row ) {
            return (InvestigatorBean)cvInvestigatorData.get(row);
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
        InvestigatorBean  investigatorBean = (InvestigatorBean) cvInvestigatorData.get(row);
        String acType = investigatorBean.getAcType();
        switch(col) {
            case INVESTIGATOR_HAND_ICON_COLUMN:
                break;
            case INVESTIGATOR_NAME_COLUMN:
                String personName = (String)value;
                if( personName == null ) {
                    personName = "";
                }
                String oldPersonName = investigatorBean.getPersonName();
                if( oldPersonName != null ) {
                    oldPersonName = oldPersonName.trim();
                }
                if( !personName.trim().equalsIgnoreCase(oldPersonName) ){
                    updatePersonDetails(row, personName);
                }
                break;
            case INVESTIGATOR_EFFORT_COLUMN:
                Double effortValue = new Double(0.00);
                try{
                    effortValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    effortValue = new Double(0.00);
                }
                investigatorBean.setPercentageEffort(effortValue.floatValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case INVESTIGATOR_PI_COLUMN:
                Boolean piValue = (Boolean) value;
                if( piValue.booleanValue()) {
                    CoeusVector cvPi = cvInvestigatorData.filter(
                            new Equals("principalInvestigatorFlag",true));
                    if( cvPi != null && cvPi.size() > 0 ) {
                        int piCount = cvPi.size();
                        for( int indx = 0; indx < piCount; indx++){
                            InvestigatorBean prevPiBean =(InvestigatorBean)cvPi.get(indx);
                            String prevAcType = prevPiBean.getAcType();
                            prevPiBean.setPrincipalInvestigatorFlag(false);
                            if( null == prevAcType ) {
                                prevPiBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                            CoeusVector cvUnitInvestigator = prevPiBean.getInvestigatorUnits();
                            resetLead(cvUnitInvestigator);
                            
                        }
                    }
                }else{
                    resetLead(investigatorBean.getInvestigatorUnits());
                }
                investigatorBean.setPrincipalInvestigatorFlag(piValue.booleanValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                controller.unitTableModel.setCellEditable(piValue.booleanValue());
                fireTableRowsUpdated(0,getRowCount()-1);
                break;
            case INVESTIGATOR_FACULTY_COLUMN:
                Boolean facultyValue = (Boolean)value;
                investigatorBean.setFacultyFlag(facultyValue.booleanValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                
                break;
                
                // Added for Case# 2229 - Multi PI Enhancement -Start
            case INVETIGATOR_MULTI_PI_COLUMN:
                Boolean multiPIValue = (Boolean)value;
                investigatorBean.setMultiPIFlag(multiPIValue.booleanValue());
                if( acType == null ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
                // Added for Case# 2229- Multi PI Enhancement - End
                
                // Added for Case# 2270 -Tracking of Effort -Start
            case INVESTIGATOR_ACAD_YEAR_COLUMN:
                Double academicYearValue = new Double(0.00);
                try{
                    academicYearValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    academicYearValue = new Double(0.00);
                }
                investigatorBean.setAcademicYearEffort(academicYearValue.floatValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case INVESTIGATOR_CAL_YEAR_COLUMN:
                Double calYearValue = new Double(0.00);
                try{
                    calYearValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    calYearValue = new Double(0.00);
                }
                investigatorBean.setCalendarYearEffort(calYearValue.floatValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
            case INVESTIGATOR_SUM_EFFORT_COLUMN:
                Double sumEffortValue = new Double(0.00);
                try{
                    sumEffortValue = Double.valueOf((String)value);
                }catch(NumberFormatException nfe){
                    sumEffortValue = new Double(0.00);
                }
                investigatorBean.setSummerYearEffort(sumEffortValue.floatValue());
                if( null == acType ) {
                    investigatorBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.setDataChanged(true);
                break;
                // Added for Case# 2270 -Tracking of Effort -End
        }
        
    }
    
    private void resetLead( CoeusVector cvUnitInvestigator ){
        if( cvUnitInvestigator != null ) {
            //                                if( cvUnitInvestigator.size() > 1 ) {
            CoeusVector cvLeads = cvUnitInvestigator.filter(
                    new Equals("leadUnitFlag",true));
        if( cvLeads != null && cvLeads.size() > 0 ) {
                int leadCount = cvLeads.size();
                for( int inx = 0; inx < leadCount; inx++){
                    InvestigatorUnitBean prevLeadBean =
                            (InvestigatorUnitBean)cvLeads.get(inx);
                    String prevLeadAcType = prevLeadBean.getAcType();
                    prevLeadBean.setLeadUnitFlag(false);
                    if( null == prevLeadAcType ) {
                        prevLeadBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }
                }
                controller.unitTableModel.fireTableRowsUpdated(
                        0,controller.unitTableModel.getRowCount()-1);
            }
            //                                }
        }
        
        
    }
    
    public CoeusVector getData(){
        return cvInvestigatorData;
    }
    public void setController(InvestigatorController invController){
        this.controller = invController;
    }
    
    //supporting method to validate the personName entered. If valid returns
    //PersonInfoFormBean otherwise null.
    private PersonInfoFormBean checkPersonName( String personName ){
        
        //Bug fix for person Validation on focus lost Start
        /*PersonInfoFormBean personInfo = null;
        personInfo = CoeusUtils.getInstance().getPersonInfoID( personName );
        if( personInfo == null || personInfo.getPersonID() == null){
            personInfo = null;
            controller.investigatorCellEditor.cancelCellEditing();
            CoeusOptionPane.showWarningDialog(messageResources.parseMessageKey(
            "investigator_exceptionCode.1007"));
//            JOptionPane.showMessageDialog(CoeusGuiConstants.getMDIForm(),messageResources.parseMessageKey(
//            "investigator_exceptionCode.1007"));
         
        }
        return personInfo;*/
        
        
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
                    CoeusOptionPane.showErrorDialog(
                            messageResources.parseMessageKey("investigator_exceptionCode.1007"));
                    personInfo = null;
                }else if(personInfo.getPersonID().equalsIgnoreCase("TOO_MANY")){
                    CoeusOptionPane.showErrorDialog
                            ("\""+personName+"\""+" " +messageResources.parseMessageKey("repRequirements_exceptionCode.1055"));
                    personInfo = null;
                }
            }else{
                Exception ex = responderBean.getException();
                ex.printStackTrace();
            }
        }
        return personInfo;
        //Bug fix for person Validation on focus lost End
    }
    
        /* supporting method used to construct the investigator bean by fetching
           the details from the database for the entered person name. */
    private void updatePersonDetails(int selectedRow, String personName){
        //            int selectedRow = tblRef.getSelectedRow();
        //            String oldPersonId = "";
        boolean primaryInv = false;
        if(selectedRow != -1){
            //                oldPersonId = (String)tblPerson.getValueAt(selectedRow,5);
            primaryInv = ((Boolean)getValueAt(selectedRow,
                    INVESTIGATOR_PI_COLUMN)).booleanValue();
        }
        InvestigatorBean prevInv = (InvestigatorBean)cvInvestigatorData.get(selectedRow);
        if(personName != null && personName.trim().length()>0){
            /* get the person details from database for the entered name.
               If the person already exists checkPersonName returns null */
            PersonInfoFormBean personInfo = checkPersonName( personName.trim() );
            if(personInfo != null){
                InvestigatorBean invBean = new InvestigatorBean();
                invBean.setPersonId(personInfo.getPersonID());
                invBean.setPersonName(personInfo.getFullName());
                invBean.setPrincipalInvestigatorFlag(primaryInv);
                invBean.setPercentageEffort(
                        ((Double)getValueAt(selectedRow,INVESTIGATOR_EFFORT_COLUMN )).floatValue());
                invBean.setNonMITPersonFlag(false);
                invBean.setFacultyFlag(
                        personInfo.getFacFlag().equalsIgnoreCase("y")
                        ? true : false);
                String homeUnit = personInfo.getHomeUnit();
                String pId = personInfo.getPersonID();
                    /* send the constructed bean, homeUnit number for the
                      investigator and send replaceRow as true as we have to
                      overwrite the existing row */
                
                CoeusVector cvUnits = prevInv.getInvestigatorUnits();
                if( cvUnits != null) {
                    //while replacing the exisiting person with new person,
                    //taking all units for the existing person to the new person
                    int unitSize = cvUnits.size();
                    CoeusVector newUnits = new CoeusVector();
                    for ( int indx = 0 ; indx < unitSize; indx++ ) {
                        InvestigatorUnitBean unitBean = (InvestigatorUnitBean) cvUnits.get(indx);
                        InvestigatorUnitBean newUnitBean = new InvestigatorUnitBean(unitBean);
                        newUnitBean.setPersonId(personInfo.getPersonID());
                        newUnitBean.setPersonName(personInfo.getFullName());
                        newUnitBean.setAcType(TypeConstants.INSERT_RECORD);
                        newUnits.add(newUnitBean);
                    }
                    invBean.setInvestigatorUnits(newUnits);
                }
                invBean.setAcType( TypeConstants.INSERT_RECORD );
                controller.setInvestigatorBean(invBean,homeUnit,true);
            }
        }else if( prevInv.getPersonId() != null ){
            CoeusOptionPane.showInfoDialog(messageResources.parseMessageKey(
                    "investigator_exceptionCode.1008"));
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
    
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    /**
     * Method to get module name
     * @return moduleName
     */
    public String getModuleName() {
        return moduleName;
    }
    
    /**
     * Method to set module name
     * @param moduleName 
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    
    /**
     * Method to get module number
     * @return moduleNumber
     */
    public String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Method to set module number
     * @param moduleNumber 
     */
    public void setModuleNumber(String moduleNumber) {
        this.moduleNumber = moduleNumber;
    }
    
    /**
     * Method to get Sequence number
     * @return seqNo
     */
    public String getSeqNo() {
        return seqNo;
    }
    
    /**
     * Method to set Sequence number
     * @param seqNo 
     */
    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End

}
