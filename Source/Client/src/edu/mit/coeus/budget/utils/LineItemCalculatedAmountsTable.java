/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.utils;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.budget.bean.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.exception.*;

/** Table component to display line item calculated amounts
 * LineItemCalculatedAmountsTable.java
 * @author  Vyjayanthi
 * Created on October 15, 2003, 10:52 AM
 */
public class LineItemCalculatedAmountsTable extends javax.swing.JPanel {
    
    private static final int RATE_CLASS_COLUMN = 0;
    private static final int RATE_TYPE_COLUMN = 1;
    private static final int APPLY_COLUMN = 2;
    private static final int COST_COLUMN = 3;
    private static final int COST_SHARING_COLUMN = 4;
    private static final String EMPTY_STRING = "";
    private static final String PROPOSAL = "proposalNumber";
    private static final String VERSION = "versionNumber";
    private static final String PERIOD = "budgetPeriod";
    private static final String LINE_ITEM = "lineItemNumber";
    private static final String AC_TYPE = "acType";
    private static final String PERSON_NUMBER = "personNumber";
    private static final String APPLY_FLAG = "applyRateFlag";
    private static final String CALCULATED_COST = "calculatedCost";
    private static final String CALCULATED_COST_SHARING = "calculatedCostSharing";
    private static final String RATE_CLASS_CODE = "rateClassCode";
    private static final String RATE_TYPE_CODE = "rateTypeCode";

    private QueryEngine queryEngine;
    private String queryKey;
    private LineItemCalAmountsTableModel lineItemCalAmountsTableModel;
    private BudgetDetailCalAmountsBean budgetDetailCalAmountsBean ;
    private BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean;
    private CoeusVector vecBudgetLineItemCalAmounts;
    private DollarCurrencyTextField dollarData;
    private boolean saveRequired = false;
    private boolean saveImmediately = false;
    private static final boolean ASCENDING = true;
    private LineItemCalAmountsRenderer lineItemCalAmountsRenderer;
    private LineItemCalAmountsHeaderRenderer lineItemCalAmountsHeaderRenderer;
    
    /** Creates new form LineItemCalculatedAmountsTable */
    public LineItemCalculatedAmountsTable() {
        initComponents();
        queryEngine = QueryEngine.getInstance();
        dollarData = new DollarCurrencyTextField();
        lineItemCalAmountsTableModel = new LineItemCalAmountsTableModel();
        tblCalculatedAmounts.setModel(lineItemCalAmountsTableModel);
        lineItemCalAmountsRenderer = new LineItemCalAmountsRenderer();
        lineItemCalAmountsHeaderRenderer = new LineItemCalAmountsHeaderRenderer();
        setTableEditors();
    }
    
    /** Method to set the column widths of the table */    
    private void setTableEditors(){    
        JTableHeader header = tblCalculatedAmounts.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);

        TableColumn column = tblCalculatedAmounts.getColumnModel().getColumn(0);
        column.setPreferredWidth(140);
        column.setMaxWidth(140);
        column.setMinWidth(140);
        
        column = tblCalculatedAmounts.getColumnModel().getColumn(1);
        column.setMinWidth(140);
        column.setMaxWidth(140);
        column.setPreferredWidth(140);
        
        column= tblCalculatedAmounts.getColumnModel().getColumn(2);
        column.setMinWidth(40);
        column.setMaxWidth(40);
        column.setPreferredWidth(40);
        
        column= tblCalculatedAmounts.getColumnModel().getColumn(3);
        column.setMinWidth(100);
        column.setMaxWidth(100);
        column.setPreferredWidth(100);
        column.setCellRenderer(lineItemCalAmountsRenderer);
        
        column= tblCalculatedAmounts.getColumnModel().getColumn(4);
        column.setMinWidth(100);
        column.setMaxWidth(100);
        column.setPreferredWidth(100);
        column.setCellRenderer(lineItemCalAmountsRenderer);
        
        /** Set the header renderer for all the columns */
        for ( int index = 0; index < tblCalculatedAmounts.getColumnCount(); index ++){
            column = tblCalculatedAmounts.getColumnModel().getColumn(index);
            column.setHeaderRenderer(lineItemCalAmountsHeaderRenderer);
        }
    }
    
    /** This method is used to set the form data specified in
     * <CODE> data </CODE>
     * @param data takes data to set to the form, here it is table */
    public void setFormData(Object data) {
        if( data instanceof Vector ){
            CoeusVector vecData = (CoeusVector) data;
            if( vecData != null && vecData.size() > 0){
                vecData.sort(RATE_CLASS_CODE, ASCENDING);
            }
            /** If data is an instance of vector then set this vector data to the table */
            lineItemCalAmountsTableModel.setData(vecData);
            lineItemCalAmountsTableModel.fireTableDataChanged();
            return;
        }
        if( data != null ){
            Equals equalsNull, equalsBudgetPeriod, equalsLineItem, equalsPerson;
            NotEquals notEqualsDelete;
            Or or;
            And andPeriodLineItem, andTemp, andOperator;
            if(data instanceof BudgetDetailBean) {
                BudgetDetailBean budgetDetailBean = (BudgetDetailBean)data;
                queryKey = budgetDetailBean.getProposalNumber() + budgetDetailBean.getVersionNumber();
                
                equalsBudgetPeriod = new Equals(PERIOD, new Integer(budgetDetailBean.getBudgetPeriod()));
                equalsLineItem = new Equals(LINE_ITEM, new Integer(budgetDetailBean.getLineItemNumber()));
                equalsNull = new Equals(AC_TYPE, null);
                
                andPeriodLineItem = new And(equalsBudgetPeriod, equalsLineItem);
                notEqualsDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
                or = new Or(notEqualsDelete, equalsNull);
                andOperator = new And(andPeriodLineItem, or);

                try{
                    vecBudgetLineItemCalAmounts = queryEngine.executeQuery(queryKey, BudgetDetailCalAmountsBean.class, andOperator);
                    if( vecBudgetLineItemCalAmounts != null && vecBudgetLineItemCalAmounts.size() > 0){
                        vecBudgetLineItemCalAmounts.sort(RATE_CLASS_CODE, ASCENDING);
                    }
                    /** If data is an instance of BudgetDetailBean then set 
                     *  <CODE>vecBudgetLineItemCalAmounts</CODE> data to the table */
                    lineItemCalAmountsTableModel.setData(vecBudgetLineItemCalAmounts);
                    lineItemCalAmountsTableModel.fireTableDataChanged();
                }catch (CoeusException coeusException) {
                    coeusException.getMessage();
                }
            }else if(data instanceof BudgetPersonnelDetailsBean) {
                BudgetPersonnelDetailsBean budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) data;
                queryKey = budgetPersonnelDetailsBean.getProposalNumber() + budgetPersonnelDetailsBean.getVersionNumber();
                budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean ();

                equalsBudgetPeriod = new Equals(PERIOD, new Integer(budgetPersonnelDetailsBean.getBudgetPeriod()));
                equalsLineItem = new Equals(LINE_ITEM, new Integer(budgetPersonnelDetailsBean.getLineItemNumber()));
                equalsPerson = new Equals(PERSON_NUMBER, new Integer(budgetPersonnelDetailsBean.getLineItemNumber()));
                equalsNull = new Equals(AC_TYPE, null);
                
                andPeriodLineItem = new And(equalsBudgetPeriod, equalsLineItem);
                andTemp = new And(andPeriodLineItem, equalsPerson);
                notEqualsDelete = new NotEquals(AC_TYPE, TypeConstants.DELETE_RECORD);
                or = new Or(notEqualsDelete, equalsNull);
                andOperator = new And(andTemp, or);

                budgetPersonnelCalAmountsBean = new BudgetPersonnelCalAmountsBean ();
                try{
                    vecBudgetLineItemCalAmounts = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, andOperator);
                    if( vecBudgetLineItemCalAmounts != null && vecBudgetLineItemCalAmounts.size() > 0){
                        vecBudgetLineItemCalAmounts.sort(RATE_CLASS_CODE, ASCENDING);
                    }
                    /** If data is an instance of BudgetPersonnelDetailsBean then set 
                     *  <CODE>vecBudgetLineItemCalAmounts</CODE> data to the table */
                    lineItemCalAmountsTableModel.setData(vecBudgetLineItemCalAmounts);
                    lineItemCalAmountsTableModel.fireTableDataChanged();
                }catch (CoeusException coeusException) {
                    coeusException.getMessage();
                }
            }
        }
    }
    
    /** Saves the data to the query engine only after 
     * all modifications are performed */
    public void saveFormData(){
        if( isSaveRequired() ){
            for( int index = 0; index < vecBudgetLineItemCalAmounts.size(); index++){
                budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecBudgetLineItemCalAmounts.get(index);
                try{
                    queryEngine.update(queryKey, budgetDetailCalAmountsBean);
                    updatePersonnelDetails(budgetDetailCalAmountsBean);
                }catch(CoeusException coeusException){
                    coeusException.getMessage();
                }
            }
            setSaveRequired(false);
        }
    }
    
    /** Method to save the personnelDetailCalAmts data in the queryengine */
    private void updatePersonnelDetails(BudgetDetailCalAmountsBean budgetDetailCalAmountsBean){
        Equals eqPropNo = new Equals(PROPOSAL, budgetDetailCalAmountsBean.getProposalNumber());
        Equals eqVerNo = new Equals(VERSION, new Integer( budgetDetailCalAmountsBean.getVersionNumber() ));
        Equals eqPeriod = new Equals(PERIOD, new Integer( budgetDetailCalAmountsBean.getBudgetPeriod() ));
        Equals eqLineItem = new Equals(LINE_ITEM, new Integer( budgetDetailCalAmountsBean.getLineItemNumber() ));
        Equals eqRCC = new Equals(RATE_CLASS_CODE, new Integer(budgetDetailCalAmountsBean.getRateClassCode()));
        Equals eqRCT = new Equals(RATE_TYPE_CODE, new Integer(budgetDetailCalAmountsBean.getRateTypeCode()));
        
        And propVer = new And(eqPropNo, eqVerNo);
        And periodLineItem = new And(eqPeriod, eqLineItem);
        And eqRCCAndEqRCT = new And(eqRCC, eqRCT);
        
        And propVerPrdLineItem = new And(propVer, periodLineItem);
        And propVerPrdLineItemRccRct = new And(propVerPrdLineItem, eqRCCAndEqRCT);
        
        boolean updated = false;
        try{
            CoeusVector vecBudgetPersonnelItemCalAmounts = queryEngine.executeQuery(queryKey, BudgetPersonnelCalAmountsBean.class, propVerPrdLineItemRccRct);
            boolean applyRateFlag = budgetDetailCalAmountsBean.isApplyRateFlag();
            for(int index = 0; index < vecBudgetPersonnelItemCalAmounts.size(); index++) {
                BudgetPersonnelCalAmountsBean budgetPersonnelCalAmountsBean = 
                        (BudgetPersonnelCalAmountsBean)vecBudgetPersonnelItemCalAmounts.get(index);
                budgetPersonnelCalAmountsBean.setApplyRateFlag(applyRateFlag);
                budgetPersonnelCalAmountsBean.setCalculatedCost(0);
                budgetPersonnelCalAmountsBean.setCalculatedCostSharing(0);
                budgetPersonnelCalAmountsBean.setAcType(TypeConstants.UPDATE_RECORD);
                queryEngine.update(queryKey, budgetPersonnelCalAmountsBean);
            }
        }catch (CoeusException coeusException){
            coeusException.getMessage();
        }
    }
    
    /** This method is used to get the saveRequired Flag
     * @return boolean true if changes are made in the form, else false
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** If set to true, saves the modified data in the table
     * instantly to the query engine 
     * @param saveImmediately takes true or false
     */
    public void setSaveImmediately(boolean saveImmediately){
        this.saveImmediately = saveImmediately;
    }    

    /** This method is used to set the saveRequired flag.
     * @param saveRequired true if data is modified, false otherwise */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        scrPnCalAmounts = new javax.swing.JScrollPane();
        tblCalculatedAmounts = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        tblCalculatedAmounts.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
        tblCalculatedAmounts.setFont(CoeusFontFactory.getNormalFont());
        tblCalculatedAmounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCalculatedAmounts.setRowSelectionAllowed(false);
        tblCalculatedAmounts.setShowHorizontalLines(false);
        tblCalculatedAmounts.setShowVerticalLines(false);
        scrPnCalAmounts.setViewportView(tblCalculatedAmounts);

        add(scrPnCalAmounts, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane scrPnCalAmounts;
    public javax.swing.JTable tblCalculatedAmounts;
    // End of variables declaration//GEN-END:variables
    
    //Inner Class Table Model - Start
    class LineItemCalAmountsTableModel extends AbstractTableModel{
        
        String colNames[] = {"Rate Class", "Rate Type", "Apply", "Cost", "Cost Sharing"};
        
        Class[] types = new Class [] {
            String.class,String.class,Boolean.class,String.class,String.class
        };
        
        private Vector dataBean;
        private DateUtils dtUtils;
        
        LineItemCalAmountsTableModel() {
          this.dtUtils = new DateUtils();
        }
        
        public Class getColumnClass(int columnIndex) {
            return types [columnIndex];
        }
        
        public boolean isCellEditable(int row, int col){
            // apply column is editable
            if( col == APPLY_COLUMN ) return true;
            return false;
        }
        
        public void setValueAt(Object value, int row, int col) {
            if( col == APPLY_COLUMN ){
                boolean set = ((Boolean)value).booleanValue();
                budgetDetailCalAmountsBean = (BudgetDetailCalAmountsBean)vecBudgetLineItemCalAmounts.get(row);
                if( set ){  //for checking
                    budgetDetailCalAmountsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    budgetDetailCalAmountsBean.setApplyRateFlag(true);
                }else{  //for unchecking
                    budgetDetailCalAmountsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    budgetDetailCalAmountsBean.setApplyRateFlag(false);
                    budgetDetailCalAmountsBean.setCalculatedCost(0.00);
                    budgetDetailCalAmountsBean.setCalculatedCostSharing(0.00);
                }
                setSaveRequired(true);
                if( saveImmediately ){
                    try{
                        queryEngine.update(queryKey, budgetDetailCalAmountsBean);
                        updatePersonnelDetails(budgetDetailCalAmountsBean);
                        setSaveRequired(false);
                    }catch(CoeusException coeusException){
                        coeusException.getMessage();
                    }
                }
                fireTableDataChanged();
            }
        }

        public Object getValueAt(int row, int column) {
            budgetDetailCalAmountsBean = ( BudgetDetailCalAmountsBean ) dataBean.get(row);
            String tempData;
            switch (column) {
                case RATE_CLASS_COLUMN:
                    return budgetDetailCalAmountsBean.getRateClassDescription();
                case RATE_TYPE_COLUMN:
                    return budgetDetailCalAmountsBean.getRateTypeDescription();
                case APPLY_COLUMN:
                    return new Boolean( budgetDetailCalAmountsBean.isApplyRateFlag() );
                case COST_COLUMN:
                    tempData = String.valueOf(budgetDetailCalAmountsBean.getCalculatedCost());
                    dollarData.setText(tempData);
                    return dollarData.getText();
                case COST_SHARING_COLUMN:
                    tempData = String.valueOf(budgetDetailCalAmountsBean.getCalculatedCostSharing());
                    dollarData.setText(tempData);
                    return dollarData.getText();
                }
            return EMPTY_STRING;
        }
        
        public void setData(Vector dataBean) {
            this.dataBean = dataBean;
        }
        
        public String getColumnName(int column) {
            return colNames[column];
        }
                
        public int getColumnCount() {
            return colNames.length;
        }        
        
        public int getRowCount() {
            if(dataBean == null) return 0;
            return dataBean.size();
        }
        
    }//Inner Class Table Model - End
    
    //LineItemCalAmountsRenderer - Start
    class LineItemCalAmountsRenderer extends DefaultTableCellRenderer {

        LineItemCalAmountsRenderer() {
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(JLabel.RIGHT);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }//LineItemCalAmountsRenderer - End
    
    //LineItemCalAmountsHeaderRenderer - Start
    class LineItemCalAmountsHeaderRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        
        private JLabel label;
        LineItemCalAmountsHeaderRenderer(){
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(CoeusFontFactory.getLabelFont());
            label.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value.toString());
            return label;
        }
        
    }
    //LineItemCalAmountsHeaderRenderer - End

}
