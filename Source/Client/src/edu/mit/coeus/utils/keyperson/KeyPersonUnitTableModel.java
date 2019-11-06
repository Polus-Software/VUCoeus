/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.keyperson;

/**
 *
 * @author midhunmk
 */

import edu.mit.coeus.bean.KeyPersonBean;
import edu.mit.coeus.bean.KeyPersonUnitBean;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.CoeusUtils;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.TypeConstants;

import edu.mit.coeus.unit.bean.*;
import edu.mit.coeus.gui.CoeusMessageResources;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class KeyPersonUnitTableModel extends AbstractTableModel {

    private CoeusVector cvUnitInvestigator;
    /** Creates a new instance of InvestigatorUnitTableModel */
    public KeyPersonUnitTableModel() {
    }
    //Represents the empty string
    private String EMPTY_STRING = "";
     private java.util.Vector kpUnits;
    //Represents the column names
  //  private String colNames[] = {EMPTY_STRING,"Number","Name","Lead","Osp Administrator"};
    private String colNames[] = {EMPTY_STRING,"Unit Number","Unit Name"};
    //Represents the column class
  //  public Class colClass[] = {ImageIcon.class,Boolean.class,String.class,String.class,String.class};
    public Class colClass[] = {ImageIcon.class,String.class,String.class};
    

    private boolean leadEditable;
    private KeyPersonController controller;
    //Specifying the column numbers for the investigator's unit details.
    public static final int UNIT_HAND_ICON_COLUMN = 0;
    public static final int UNIT_LEAD_FLAG_COLUMN =3 ;
    public static final int UNIT_NUMBER_COLUMN = 1;
    public static final int UNIT_NAME_COLUMN = 2;
    public static final int UNIT_OSP_ADMINISTRATOR_COLUMN = 4;
//     public static final int UNIT_HAND_ICON_COLUMN = 0;
//   // public static final int UNIT_LEAD_FLAG_COLUMN = 3;
//    public static final int UNIT_NUMBER_COLUMN = 1;
//    public static final int UNIT_NAME_COLUMN = 2;
//  //  public static final int UNIT_OSP_ADMINISTRATOR_COLUMN = 4;
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private boolean editable;
    /**
     *This method is to get the column count
     *@return int
     */
    public int getColumnCount() {
        return 3;
    }

    /**
     *This method is to check whether the specified cell is editable or not
     *@param int row
     *@param int col
     *@return boolean
     */
    public boolean isCellEditable(int row,int col) {
        if( isEditable() ){
            KeyPersonUnitBean unitBean = (KeyPersonUnitBean)cvUnitInvestigator.get(row);
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
    @Override
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

    public KeyPersonUnitBean getUnitBean(int selRow){
        if( cvUnitInvestigator != null && cvUnitInvestigator.size() > selRow) {
            return (KeyPersonUnitBean) cvUnitInvestigator.get(selRow);
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
        KeyPersonUnitBean  unitBean = (KeyPersonUnitBean)cvUnitInvestigator.get(row);

        switch(col) {
            case UNIT_HAND_ICON_COLUMN:
                return EMPTY_STRING;
            case UNIT_LEAD_FLAG_COLUMN:
               // return new Boolean(unitBean.isLeadUnitFlag());
               return false;
            case UNIT_NUMBER_COLUMN:
                return unitBean.getUnitNumber();

            case UNIT_NAME_COLUMN:
                return unitBean.getUnitName();
            case UNIT_OSP_ADMINISTRATOR_COLUMN:
             //   return unitBean.getOspAdministratorName();
              return EMPTY_STRING;
        }
        return EMPTY_STRING;
    }

    public void setValueAt(Object value, int row, int col) {
        //if the vector is null return
        if (cvUnitInvestigator == null) return;
        KeyPersonUnitBean unitBean = (KeyPersonUnitBean)cvUnitInvestigator.get(row);
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
                            KeyPersonUnitBean prevLeadBean =
                            (KeyPersonUnitBean)cvLeads.get(indx);
                            String prevAcType = prevLeadBean.getAcType();
                            //prevLeadBean.setLeadUnitFlag(false);
                            if( null == prevAcType ) {
                                prevLeadBean.setAcType(TypeConstants.UPDATE_RECORD);
                            }
                        }
                    }
                }
                //unitBean.setLeadUnitFlag(leadValue.booleanValue());
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
//            case UNIT_NAME_COLUMN:
//                // return awardUnitBean.getUnitName();
//                return ("No name");
//            case UNIT_OSP_ADMINISTRATOR_COLUMN:
//                 return awardUnitBean.getUpdateUser();
        }


    }

    public void setController( KeyPersonController  invController){
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

//                boolean leadUnit =((Boolean)getValueAt(selectedUnitRow,
//                UNIT_LEAD_FLAG_COLUMN)).booleanValue();
                if( !isDuplicateUnitID( unitNumber) ) {
                    if( cvUnitInvestigator.size() > selectedUnitRow ) {
                        //                    setSaveRequired(true);
                        KeyPersonUnitBean unitBean =
                        (KeyPersonUnitBean)cvUnitInvestigator.get(selectedUnitRow);
                        String oldUnitNumber = unitBean.getUnitNumber();
                        String acType = unitBean.getAcType();
                        KeyPersonUnitBean newUnitBean = new KeyPersonUnitBean();

                        newUnitBean.setUnitNumber(unitDetail.getUnitNumber());
                        newUnitBean.setUnitName(unitDetail.getUnitName());
                        newUnitBean.setPersonId(unitBean.getPersonId());
                      //  newUnitBean.setOspAdministratorName(unitDetail.getOspAdminName());
                        //newUnitBean.setLeadUnitFlag(leadUnit);
                        newUnitBean.setAcType( TypeConstants.INSERT_RECORD );
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
            KeyPersonUnitBean unitBean = (KeyPersonUnitBean)
                cvUnitInvestigator.get(selectedUnitRow);
            KeyPersonUnitBean newUnitBean = new KeyPersonUnitBean();

            newUnitBean.setUnitNumber(null);
            newUnitBean.setUnitName(null);
            newUnitBean.setPersonId(unitBean.getPersonId());
            //newUnitBean.setOspAdministratorName(null);
            //newUnitBean.setLeadUnitFlag(unitBean.isLeadUnitFlag());
            newUnitBean.setAcType( TypeConstants.INSERT_RECORD );
            cvUnitInvestigator.set(selectedUnitRow,newUnitBean);

        unitBean.setUnitNumber(null);
        unitBean.setUnitName(null);
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
      public java.util.Vector getKpUnits() {
        return kpUnits;
    }

    /**
     * @param kpUnits the kpUnits to set
     */
    public void setKpUnits(java.util.Vector kpUnits) {
        this.kpUnits = kpUnits;
    }

}

