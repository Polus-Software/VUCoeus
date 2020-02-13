/*
 * InvestigatorUnitTableModel.java
 * Created on March 26, 2004, 2:35 PM
 */

package edu.mit.coeus.utils.investigator;

import edu.mit.coeus.bean.InvestigatorUnitBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;

import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.propdev.bean.InvCreditTypeBean;
import edu.mit.coeus.propdev.bean.InvestigatorCreditSplitBean;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.query.QueryEngine;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class InvestigatorUnitTableModel extends AbstractTableModel {
    
    private CoeusVector cvUnitInvestigator;
    /** Creates a new instance of InvestigatorUnitTableModel */
    public InvestigatorUnitTableModel() {
    }

    //Represents the empty string
    private String EMPTY_STRING = "";
    //Represents the column names
    private String colNames[] = {EMPTY_STRING,"Lead","Number","Name","Osp Administrator"};
    //Represents the column class
    public Class colClass[] = {ImageIcon.class,Boolean.class,String.class,String.class,
    String.class};
    
    private boolean leadEditable;
    private InvestigatorController controller;
    //Specifying the column numbers for the investigator's unit details.
    public static final int UNIT_HAND_ICON_COLUMN = 0;
    public static final int UNIT_LEAD_FLAG_COLUMN = 1;
    public static final int UNIT_NUMBER_COLUMN = 2;
    public static final int UNIT_NAME_COLUMN = 3;
    public static final int UNIT_OSP_ADMINISTRATOR_COLUMN = 4;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private boolean editable;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    private String moduleName;
    private String moduleNumber;
    private String seqNo;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start        
    
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return 5;
    }
    
    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row,int col) {
        if( isEditable() ){
            InvestigatorUnitBean unitBean = (InvestigatorUnitBean)cvUnitInvestigator.get(row);
            String unitNo = unitBean.getUnitNumber();
            switch (col) {
                case UNIT_LEAD_FLAG_COLUMN:
                    return leadEditable && unitNo != null && unitNo.trim().length() > 0 ;
                case UNIT_NUMBER_COLUMN:
                    return true;
                case UNIT_NAME_COLUMN:
                    return false;
                case UNIT_OSP_ADMINISTRATOR_COLUMN:
                    return false;
            }
        }
        return false;
    }
    
    /**
     *
     **/
    public void setCellEditable(boolean cellEditable) {
        leadEditable = cellEditable;
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
     *This method is to get the row count
     *@return int
     */
    public int getRowCount(){
        if(cvUnitInvestigator== null){
            return 0;
        }else{
            return cvUnitInvestigator.size();
        }
        
    }
    
    public void setData(CoeusVector cvUnitInvestigator){
        this.cvUnitInvestigator = cvUnitInvestigator;
    }
    
    public InvestigatorUnitBean getUnitBean(int selRow){
        if( cvUnitInvestigator != null && cvUnitInvestigator.size() > selRow) {
            return (InvestigatorUnitBean) cvUnitInvestigator.get(selRow);
        }
        return null;
    }
    /**
     *This method is to get the value with respect to the row and column
     *@param int row
     *@param int col
     *@return Object
     */
    public Object getValueAt(int row, int col){
        InvestigatorUnitBean  unitBean = (InvestigatorUnitBean)cvUnitInvestigator.get(row);
        
        switch(col) {
            case UNIT_HAND_ICON_COLUMN:
                return EMPTY_STRING;
            case UNIT_LEAD_FLAG_COLUMN:
                return new Boolean(unitBean.isLeadUnitFlag());
            case UNIT_NUMBER_COLUMN:
                return unitBean.getUnitNumber();
                
            case UNIT_NAME_COLUMN:
                return unitBean.getUnitName();
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
                return unitBean.getOspAdministratorName();
        }
        return EMPTY_STRING;
    }
    
    public void setValueAt(Object value, int row, int col) {
        //if the vector is null return
        if (cvUnitInvestigator == null) return;
        InvestigatorUnitBean unitBean = (InvestigatorUnitBean)cvUnitInvestigator.get(row);
        String acType = unitBean.getAcType();
        switch (col) {
            
            case UNIT_LEAD_FLAG_COLUMN:
                Boolean leadValue = (Boolean) value;
                if( cvUnitInvestigator.size() > 1 && leadValue.booleanValue()) {
                    CoeusVector cvLeads = cvUnitInvestigator.filter(
                    new Equals("leadUnitFlag",true));
                    if( cvLeads != null && cvLeads.size() > 0 ) {
                        int leadCount = cvLeads.size();
                        for( int indx = 0; indx < leadCount; indx++){
                            InvestigatorUnitBean prevLeadBean =
                            (InvestigatorUnitBean)cvLeads.get(indx);
                            String prevAcType = prevLeadBean.getAcType();
                            prevLeadBean.setLeadUnitFlag(false);
                            if( null == prevAcType ) {
                                prevLeadBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }
                    }
                }
                unitBean.setLeadUnitFlag(leadValue.booleanValue());
                if( null == acType ) {
                    unitBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
                controller.updateUnits();
                fireTableRowsUpdated(0,getRowCount()-1);
                break;
            case UNIT_NUMBER_COLUMN:
                if( value == null ) {
                    value = "";
                }
                if( !value.equals(unitBean.getUnitNumber()) ){
                    updateUnitDetails(row, (String)value);
                }
                break;
            case UNIT_NAME_COLUMN:
                // return awardUnitBean.getUnitName();
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
                // return awardUnitBean.getUpdateUser();
        }
        
        
    }
    
    public void setController(InvestigatorController invController){
        this.controller = invController;
    }
    
    public void removeRow( int row ){
        if( cvUnitInvestigator != null && cvUnitInvestigator.size() > row ){
            cvUnitInvestigator.remove(row);
            fireTableDataChanged();
        }
    }
    
    public boolean isDuplicateUnitID( String unitID ){
        
        boolean isDuplicateUnit = false;
        if( cvUnitInvestigator != null && (unitID != null) && (unitID.trim().length()>0)){
            CoeusVector dupUnits = cvUnitInvestigator.filter(new Equals("unitNumber",
            unitID));
            if( dupUnits != null && dupUnits.size() > 0 ) {
                isDuplicateUnit = true;
            }
        }
        return isDuplicateUnit;
    }
    
    private void updateUnitDetails( int selectedUnitRow, String unitNumber){
        if( !"".equals(unitNumber.trim())) {
            controller.unitCellEditor.cancelCellEditing();
            UnitDetailFormBean unitDetail = CoeusUtils.getInstance().getUnitInfoBean( unitNumber );
            if(unitDetail != null){

                boolean leadUnit =((Boolean)getValueAt(selectedUnitRow,
                UNIT_LEAD_FLAG_COLUMN)).booleanValue();
                if( !isDuplicateUnitID( unitNumber) ) {
                    if( cvUnitInvestigator.size() > selectedUnitRow ) {
                        //                    setSaveRequired(true);
                        InvestigatorUnitBean unitBean =
                        (InvestigatorUnitBean)cvUnitInvestigator.get(selectedUnitRow);
                        String oldUnitNumber = unitBean.getUnitNumber();
                        String acType = unitBean.getAcType();
                        InvestigatorUnitBean newUnitBean = new InvestigatorUnitBean();

                        newUnitBean.setUnitNumber(unitDetail.getUnitNumber());
                        newUnitBean.setUnitName(unitDetail.getUnitName());
                        newUnitBean.setPersonId(unitBean.getPersonId());
                        newUnitBean.setOspAdministratorName(unitDetail.getOspAdminName());
                        newUnitBean.setLeadUnitFlag(leadUnit);
                        newUnitBean.setAcType( TypeConstants.INSERT_RECORD );
                        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
                        if(CoeusGuiConstants.AWARD_MODULE.equals(getModuleName())){
                            deleteUnitCreditSplitForInv(unitBean.getPersonId(),oldUnitNumber,acType);
                            addInvUnitCreditSplitDetails(unitBean.getPersonId(),leadUnit,unitNumber);
                        }
                        // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
                        cvUnitInvestigator.set(selectedUnitRow,newUnitBean);
                        setData(cvUnitInvestigator);
                        fireTableRowsUpdated(selectedUnitRow, selectedUnitRow);
                        controller.updateInvestigator(unitBean.getPersonId(),unitBean);
                        //
                    }
                }else{
                    CoeusOptionPane.showWarningDialog("' " + unitNumber + "' " +
                    coeusMessageResources.parseMessageKey("protoInvFrm_exceptionCode.1137"));

                }
            }
        }else{
            InvestigatorUnitBean unitBean = (InvestigatorUnitBean)
                cvUnitInvestigator.get(selectedUnitRow);
            InvestigatorUnitBean newUnitBean = new InvestigatorUnitBean();

            newUnitBean.setUnitNumber(null);
            newUnitBean.setUnitName(null);
            newUnitBean.setPersonId(unitBean.getPersonId());
            newUnitBean.setOspAdministratorName(null);
            newUnitBean.setLeadUnitFlag(unitBean.isLeadUnitFlag());
            newUnitBean.setAcType( TypeConstants.INSERT_RECORD );
            cvUnitInvestigator.set(selectedUnitRow,newUnitBean);

//            unitBean.setUnitNumber(null);
//            unitBean.setUnitName(null);
//            unitBean.setOspAdministratorName(null);
//            cvUnitInvestigator.set(selectedUnitRow,unitBean);
            setData(cvUnitInvestigator);
            fireTableRowsUpdated(selectedUnitRow, selectedUnitRow);
            controller.updateInvestigator(unitBean.getPersonId(),unitBean);
        }
        
    }
    public CoeusVector getData(){
        return cvUnitInvestigator;
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
     * Method to add unit credit split for an investigator
     * @param personId 
     * @param primaryInv 
     * @param unitNumber 
     */
    private void addInvUnitCreditSplitDetails(String personId, boolean primaryInv, String unitNumber){
        try {
            String queryKey = getModuleNumber() + getSeqNo();
            QueryEngine queryEngine = QueryEngine.getInstance();
            CoeusVector cvInvCreditSplitType = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_CREDIT_TYPES_KEY);
            CoeusVector cvAwardCreditSplit = queryEngine.getDetails(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY);

            if(cvInvCreditSplitType != null && !cvInvCreditSplitType.isEmpty()){
                InvestigatorCreditSplitBean investigatorCreditSplitBean  = null;
                for(Object invCreditSplitType : cvInvCreditSplitType){
                    InvCreditTypeBean invCreditTypeBean = (InvCreditTypeBean)invCreditSplitType;
                    investigatorCreditSplitBean = new InvestigatorCreditSplitBean();
                    investigatorCreditSplitBean.setModuleNumber(getModuleNumber());
                    investigatorCreditSplitBean.setSequenceNo(Integer.parseInt(getSeqNo()));
                    investigatorCreditSplitBean.setCredit(0.0);
                    investigatorCreditSplitBean.setUnitNumber(unitNumber);
                    investigatorCreditSplitBean.setInvCreditTypeCode(invCreditTypeBean.getInvCreditTypeCode());
                    investigatorCreditSplitBean.setPersonId(personId);
                    investigatorCreditSplitBean.setPiFlag(primaryInv);
                    investigatorCreditSplitBean.setAcType(TypeConstants.INSERT_RECORD);
                    cvAwardCreditSplit.add(investigatorCreditSplitBean);
                }
                Hashtable htDataCollection = queryEngine.getDataCollection(queryKey);
                htDataCollection.put(CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY, cvAwardCreditSplit == null ? new CoeusVector() : cvAwardCreditSplit);
                queryEngine.removeDataCollection(queryKey);
                queryEngine.addDataCollection(queryKey,htDataCollection);

            }
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Method to delete unit credit split for an investigator
     * @param personId 
     * @param unitNo 
     * @param acType 
     */
    private void deleteUnitCreditSplitForInv(String personId, String unitNo, String acType) {
        try{
            QueryEngine queryEngine = QueryEngine.getInstance();
            String queryKey = getModuleNumber() + getSeqNo();
            Equals eqPersonId = new Equals("personId",personId);
            Equals eqUnitNo = new Equals("unitNumber",unitNo);
            And andPersonIdUnitAcType = new And(eqUnitNo,eqPersonId);
            
            And andPersonUnitAcType = null;
            if(TypeConstants.INSERT_RECORD.equals(acType)){
                Equals eqAcType = new Equals("acType",TypeConstants.INSERT_RECORD);
                andPersonUnitAcType = new And(eqAcType,andPersonIdUnitAcType);
                queryEngine.removeData(queryKey,CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,andPersonUnitAcType);
            }else{
                andPersonUnitAcType = new And( CoeusVector.FILTER_ACTIVE_BEANS,andPersonIdUnitAcType);
                queryEngine.setUpdate(queryKey, CoeusConstants.INVESTIGATOR_UNIT_CREDIT_SPLIT_KEY,
                        InvestigatorCreditSplitBean.class,"acType",String.class,TypeConstants.DELETE_RECORD, andPersonUnitAcType);
            }
        }catch(CoeusException coeusExp){
            coeusExp.printStackTrace();
        }
    }

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
