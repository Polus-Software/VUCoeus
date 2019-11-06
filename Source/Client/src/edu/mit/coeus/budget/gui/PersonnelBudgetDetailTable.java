/*
 * PersonnelBudgetDetailTable.java
 *
 * Created on October 29, 2003, 3:21 PM
 */

package edu.mit.coeus.budget.gui;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.DateUtils;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.budget.bean.BudgetPersonnelDetailsBean;
import edu.mit.coeus.budget.gui.PersonnelBudgetDetailsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.budget.controller.PersonnelBudgetDetailController;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.utils.CoeusLabel;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class PersonnelBudgetDetailTable extends JTable implements TypeConstants{
    
    //=== Table Column index value
    /** Constants indicating Column Index for NAME Column */
    private final int NAME_COL_INDEX = 0;
    /** Constants indicating Column Index for JOB CODE Column */
    private final int JOB_CODE_COL_INDEX = 1;
    /** Constants indicating Column Index for START DATE Column */
    private final int START_DATE_COL_INDEX = 2;
    /** Constants indicating Column Index for END DATE Column */
    private final int END_DATE_COL_INDEX = 3;
    /** Constants indicating Column Index for PERIOD Column */
    private final int PERIOD_COL_INDEX = 4;
    /** Constants indicating Column Index for Percentage Change Column */
    private final int PERCHNG_COL_INDEX = 5;
    /** Constants indicating Column Index for Percentage Effort Column */
    private final int PERCEFFORT_COL_INDEX = 6;
    /** Constants indicating Column Index for Salary Column */
    private final int SALARY_COL_INDEX = 7;
    //constants
    /** Constants indicating EMPTY String */
    //private static final String EMPTY_STRING = "";
    
    private boolean calculatePersonnelLineItem = false;
    // Added by chandra to Fix #1102 - 10-Sept-2004
    private boolean amountChanged = false;
    //Bug fix for Case #1638 start 1
    private static final String EMPTY_STRING="";
    //Bug fix for Case #1638 end 1

    
    //Validation Messages
    /** String Constants indicating Invalid Start Date Message */
    private static final String INVALID_START_DATE_ENTRY = "budget_common_exceptionCode.1001"; //"Invalid Start date. Please Input a valid Start date.";
    /** String Constants indicating Invalid End Date Message */
    private static final String INVALID_END_DATE_ENTRY = "budget_common_exceptionCode.1002"; // "Invalid End date. Please Input a valid End date.";
    /** String Constants indicating Start Date Validation Message */
    private static final String START_DATE_VALIDATION = "budget_personnelBudget_exceptionCode.1204"; //"Start date should be between start and end date of Line Item.";
    /** String Constants indicating End Date Validation Message */
    private static final String END_DATE_VALIDATION = "budget_personnelBudget_exceptionCode.1205"; //"End date should be between start and end date of Line Item.";
    
    /** String Constants indicating Display date Format */
    private static final String DISPLAY_DATE_FORMAT = "dd-MMM-yyyy";
    /** DATE FORMAT */
    private final String SIMPLE_EDIT_DATE_FORMAT = "MM/dd/yyyy";
    /** DATE SEPERATORS */
    private static final String DATE_SEPARATERS = ":/.,|-";
    /** String Constants containg Zero Value */
    private static final String ZEROSTRINGVALUE = "0.00";
    /** Simple Date Format */
    private SimpleDateFormat simpleDateFormat;
    
    /** Returns the Model of the Table */
    public PersonnelBudgetTableModel personnelBudgetTableModel;
    private PersonnelBudgetTableCellRenderer   tableModelRenderer;
    private PersonnelBudgetTableCellEditor tableModelEditor;
    private BudgetPersonnelDetailsBean budgetPersonnelDetailsBean ;
    private DollarCurrencyTextField dollarData;
    
    private Vector vecBudgetPersonnelDetailsBean;
    
    private PersonnelBudgetDetailController persBudgetDetailController;
    
    private DateUtils dateUtils = new DateUtils();
    private Vector tableVector;
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //Bug Fix: 1571 Start 1
    private Color tableBackGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    //Bug Fix: 1571 End 1
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /** Array Holding the PreDefined PeriodType and PeriodCode */
    /*String periodArray [][] = {
        {"CC","Cycle"},
        {"AP","Academic"},
        {"CY","Calendar"},
        {"SP","Summer"}
    };*/
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
    public String[][] periodArray,localPeriodArray ,activePeriodArray;
    // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
    private final char GET_PERIOD_TYPE ='n';
    private final char GET_ACTIVE_PERIOD_TYPE ='o';
    private final String connectURL = "/BudgetMaintenanceServlet";
    private static final String COULD_NOT_CONTACT_SERVER = "Could not contact server";
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
    //Variable for SELECTED TABLE ROW COLOR
    private final java.awt.Color SELECTEDTABLEROWCOLOR = Color.YELLOW; //((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
    //Variable for the parentForm Component for which The Dialog is opened
    private Component parentForm;
    
    /** sets the ParentForm to which this Table belongs
     * @param form passing the ParentForm to this Table
     */
    public void setParentForm(Component form) {
        this.parentForm = form;
    }
    
    /** Creates new form PersonBudgetTable */
    public PersonnelBudgetDetailTable() {
        
        simpleDateFormat = new SimpleDateFormat(SIMPLE_EDIT_DATE_FORMAT);
        coeusMessageResources = CoeusMessageResources.getInstance();
        
        setFont(CoeusFontFactory.getNormalFont());
        dollarData = new DollarCurrencyTextField();
        
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
        try {
            periodArray = fetchActivePeriodTypes();
            // Added for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
            activePeriodArray = periodArray;
            localPeriodArray = fetchPeriodTypes();
            // Added for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
                               
        } catch (CoeusClientException ex) {
            ex.printStackTrace();
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        
        //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
        personnelBudgetTableModel = new PersonnelBudgetTableModel();
        setModel(personnelBudgetTableModel);
        PersonnelBudgetTableHeaderRenderer tableHeaderRenderer = new PersonnelBudgetTableHeaderRenderer();
        tableModelRenderer = new PersonnelBudgetTableCellRenderer();
        tableModelEditor= new PersonnelBudgetTableCellEditor();
        getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        setRowHeight(22);
        
        TableColumn tableColumn = null;
        
        //setting Table Column
        int colSize[] = {150, 80, 100, 100, 110, 95, 100, 120};
        
        //setting the width Render and Editor for each Column
        
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = this.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setCellRenderer(tableModelRenderer);
            tableColumn.setCellEditor(tableModelEditor);
            tableColumn.setHeaderRenderer(tableHeaderRenderer);
        }
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //===Start add the Coloumn Click listener
        
        JTableHeader header = this.getTableHeader();
        header.addMouseListener(new ColumnHeaderListener());
        header.setReorderingAllowed(false);
        
        //======end
        
    }
    
    //Bug Fix: 1571 Start 2
    /** Updates the selection models of the table, depending on the state of the two flags
     * @param row int
     * @param column int
     * @param toggle boolean
     * @param extend boolean
     */    
   /* public void changeSelection(int row, int column, boolean toggle, boolean extend){
        //try {
            super.changeSelection(row, column, toggle, extend);
//            //Bug Fix #809- 04/06/2004 - Added by chandra start
////            if(column < 2) {
////            this.editCellAt(row, 2); 
////            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
////            }
//            if(column == 2) {
//            this.editCellAt(row, 2);
//
//            //Bug Fix: 1571 Start 
////            SwingUtilities.invokeLater( new Runnable() {
////                public void run() {
////                    getTable().dispatchEvent(new KeyEvent(
////                    getTable(),KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
////                    KeyEvent.CHAR_UNDEFINED) );
////                }
////            });
//            //Bug Fix: 1571 End 
//            
//            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            }
//            //Bug Fix 809 - 04/06/2004 - Added by chandra End
//            
//            if(column > 6) {
//            this.editCellAt(row, 6);    
//            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            }
//            
//            
////            if((row == (getRowCount() -1)) && column == 7) {
////                this.processFocusEvent(new java.awt.event.FocusEvent(this,java.awt.event.FocusEvent.FOCUS_LOST));
////                persBudgetDetailController.personnelBudgetDetailsForm.btnOk.grabFocus();
////                return;
////            }
//            
//            //Bug Fix: 1571 Start 
//            //if(column > 1 && column < 7) {
//            if(column > 1 && column < 7 && column!= 2) { //Bug Fix: 1571 End 
//            
//            SwingUtilities.invokeLater( new Runnable() {
//                public void run() {
//                    getTable().dispatchEvent(new KeyEvent(
//                    getTable(),KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
//                    KeyEvent.CHAR_UNDEFINED) );
//
//                }
//            });
//            }
//        } catch(Exception e) {
//            e.getMessage();
//        }
         
          
              SwingUtilities.invokeLater( new Runnable() {
                  public void run() {
                      getTable().dispatchEvent(new KeyEvent(
                      getTable(),KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
                      KeyEvent.CHAR_UNDEFINED) );
                  }
              });
          
//          if(isCellEditable(row, column)){
//              this.scrollRectToVisible(getCellRect(row ,column, true));
//              this.editCellAt(row, column);
//              this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//          }
            
    }*/
    //Bug Fix: 1571 End 2
    
    
    /** Method to Set the Vector of BudgetPersonnelDetailsBean to the Table
     * @param data Vector of BudgetPersonnelDetailsBean
     */
    public void setTableData(Object data) {
        
        tableVector  = (Vector) data;
        personnelBudgetTableModel.setData(tableVector);
        personnelBudgetTableModel.fireTableDataChanged();
    }
    
    /** Method to return the TableCellEditor of this Table Component
     *@return PersonnelBudgetTableCellEditor of PersonnelBudgetDetailTable
     */
    
    public PersonnelBudgetTableCellEditor getTableCellEditor() {
        return tableModelEditor;
    }
    
    /** Method to return the TableModel of this Table Component
     *@return PersonnelBudgetTableModel of PersonnelBudgetDetailTable
     */
    
    public PersonnelBudgetTableModel getTableModel() {
        return personnelBudgetTableModel;
    }
    
    public PersonnelBudgetDetailTable getTable() {
        return this;
    }
    
    /** Method to pass the Controller handle to the Table to access Controller Methods
     * @param persBudgetDetailController PersonnelBudgetDetailController instance
     */
    
    public void setControllerInstance(PersonnelBudgetDetailController persBudgetDetailController) {
        this.persBudgetDetailController = persBudgetDetailController;
    }
    
    /** Getter for property calculatePersonnelLineItem.
     * @return Value of property calculatePersonnelLineItem.
     *
     */
    public boolean isCalculatePersonnelLineItem() {
        return calculatePersonnelLineItem;
    }
    
    /** Setter for property calculatePersonnelLineItem.
     * @param calculatePersonnelLineItem New value of property calculatePersonnelLineItem.
     *
     */
    public void setCalculatePersonnelLineItem(boolean calculatePersonnelLineItem) {
        this.calculatePersonnelLineItem = calculatePersonnelLineItem;
    }
    
    /** Getter for property amountChanged.
     * @return Value of property amountChanged.
     *
     */
    public boolean isAmountChanged() {
        return amountChanged;
    }
    
    /** Setter for property amountChanged.
     * @param amountChanged New value of property amountChanged.
     *
     */
    public void setAmountChanged(boolean amountChanged) {
        this.amountChanged = amountChanged;
    }

    //Inner Class Table Model - Start
    /** PersonnelBudgetDetailTabel Model */
    public class PersonnelBudgetTableModel extends AbstractTableModel{
        
        private Class columnTypes [] = {String.class,String.class,Date.class,Date.class,String.class,Double.class,Double.class,String.class };
        private String columnNames [] = {"Name", "Job Code", "Start Date", "End Date","Period","% Charged","% Effort","Salary" };
        private boolean columnEditables [] ={false,false,true,true,true,true,true,false };
        private Vector dataBean;
        
        /** Default Constructor for the PersonnelBudgetTableModel
         */
        
        PersonnelBudgetTableModel() {
            
        }
        
        /** Return the ColumnClass for the Index
         *@param columnIndex int column Index for which the Column Class is required
         *@return Class
         */
        
        public Class getColumnClass(int columnIndex) {
            return columnTypes [columnIndex];
        }
        
        /** Is the Cell Editable at rowIndex and columnIndex
         * @param rowIndex rowIndex
         * @param columnIndex columnIndex
         * @return if <true> the Cell is editable
         */
        public boolean isCellEditable(int rowIndex, int columnIndex){
            //Bug Fix: 1571 Start 3
            if(persBudgetDetailController.getFunctionType() == DISPLAY_MODE) {
                return false;
            }
          //  return columnEditables[columnIndex];
            switch(columnIndex){
                case NAME_COL_INDEX:
                    return false;
                case JOB_CODE_COL_INDEX:
                    return false;
                case START_DATE_COL_INDEX:
                    return true;
                case END_DATE_COL_INDEX:
                    return true;
                case PERIOD_COL_INDEX:
                    return true;
                case PERCHNG_COL_INDEX:
                    return true;
                case PERCEFFORT_COL_INDEX:
                    return true;
                case SALARY_COL_INDEX:
                    return false;
            }
            return false;
           //Bug Fix: 1571 End 3
        }
        
        /** Set  the Vector of beans
         * @param dataBean Vector of beans
         */
        public void setData(Vector dataBean) {
            this.dataBean = dataBean;
            
            vecBudgetPersonnelDetailsBean = new Vector();
            vecBudgetPersonnelDetailsBean = (Vector) dataBean.clone();
            
        }
        
        /** to get the Column Name for a columnIndex
         * @param columnIndex columnIndex for which Column Name is retrieved
         * @return String Column Name
         */
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
        
        /** get the Column Count
         * @return int Column Count
         */
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /** To get the RowCount
         * @return int Row Count
         */
        public int getRowCount() {
            if(dataBean == null) {
                return 0;
            }
            return dataBean.size();
        }
        
        
        /** get Value at rowIndex columnIndex
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         * @return Object
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) dataBean.get(rowIndex);
            if(budgetPersonnelDetailsBean == null) {
                return null;
            }
            
            switch(columnIndex) {
                case NAME_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getFullName()==null ||
                    budgetPersonnelDetailsBean.getFullName().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    } else
                        return budgetPersonnelDetailsBean.getFullName();
                case JOB_CODE_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getJobCode()==null ||
                    budgetPersonnelDetailsBean.getJobCode().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    } else
                        return budgetPersonnelDetailsBean.getJobCode();
                    
                case START_DATE_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getStartDate()==null ||
                    budgetPersonnelDetailsBean.getStartDate().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    } else
                        return budgetPersonnelDetailsBean.getStartDate();
                    
                case END_DATE_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getEndDate()==null ||
                    budgetPersonnelDetailsBean.getEndDate().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    } else
                        return budgetPersonnelDetailsBean.getEndDate();
                    
                case PERIOD_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getPeriodType()==null ||
                    budgetPersonnelDetailsBean.getPeriodType().equals(EMPTY_STRING)){
                        return EMPTY_STRING;
                    } else
                        return budgetPersonnelDetailsBean.getPeriodType();
                    
                case PERCHNG_COL_INDEX :
                    return new Double(budgetPersonnelDetailsBean.getPercentCharged());
                case PERCEFFORT_COL_INDEX :
                    return new Double(budgetPersonnelDetailsBean.getPercentEffort());
                    
                case SALARY_COL_INDEX :
                    return new Double(budgetPersonnelDetailsBean.getSalaryRequested());
            }
            return null;
            
        }
        
        
        /** set Object at rowIndex columnIndex of table
         * @param value Object to set
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         */
        public void setValueAt(Object value, int rowIndex,int columnIndex) {
            
            Date dt;
            String strDate;
            Date dateStartDate = null;
            Date datetEndDate = null;
           // boolean calculatePersonnelLineItem = false;
            
            try {
                
                budgetPersonnelDetailsBean = (BudgetPersonnelDetailsBean) dataBean.get(rowIndex);
                if(budgetPersonnelDetailsBean == null) {
                    return;
                }
                
                dateStartDate = simpleDateFormat.parse(dateUtils.restoreDate(
                ( (PersonnelBudgetDetailsForm ) persBudgetDetailController.getControlledUI()).txtStartDate.getText(),DATE_SEPARATERS));
                
                datetEndDate = simpleDateFormat.parse(dateUtils.restoreDate(
                ( (PersonnelBudgetDetailsForm ) persBudgetDetailController.getControlledUI()).txtEndDate.getText(),DATE_SEPARATERS));
                
                
            } catch(Exception e) {
                e.getMessage();
            }
            
            switch(columnIndex) {
                case START_DATE_COL_INDEX :
                    
                     
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    
                    //Validate for start Date
                    try{
                        strDate = dateUtils.formatDate(value.toString(), DATE_SEPARATERS, DISPLAY_DATE_FORMAT);
                        if(strDate == null) {
                            throw new CoeusException();
                        }
                        
                        
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                        
                        if(dt.compareTo(dateStartDate) < 0 ) {
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(START_DATE_VALIDATION));
                            return ;
                        }
                        
                        if(dt.compareTo(datetEndDate) > 0 ) {
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(START_DATE_VALIDATION));
                            return ;
                        }
                        
                        //Bug Fix: 1571 Start 4
                        if(budgetPersonnelDetailsBean.getStartDate().compareTo(new java.sql.Date(dt.getTime()))  != 0){
                            calculatePersonnelLineItem = true;
                        }else{
                            calculatePersonnelLineItem = false;
                        }
                        //Bug Fix: 1571 End 4
                        budgetPersonnelDetailsBean.setStartDate(new java.sql.Date(dt.getTime()));
                        if(persBudgetDetailController != null && calculatePersonnelLineItem) {
                            persBudgetDetailController.calculatePersonnelLineItem(budgetPersonnelDetailsBean);
                        }
                        
                    }catch (ParseException parseException) {
                        parseException.getMessage();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(INVALID_START_DATE_ENTRY));
                        return ;
                    }catch (CoeusException coeusException) {
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(INVALID_START_DATE_ENTRY));
                        return ;
                    }catch (CoeusClientException coeusClientException){
                        CoeusOptionPane.showDialog(coeusClientException);
                    }
                    
                    
                    break;
                case END_DATE_COL_INDEX :
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    //  validate the End Date Entry
                    
                    try{
                        strDate = dateUtils.formatDate(value.toString(), DATE_SEPARATERS, DISPLAY_DATE_FORMAT);
                        if(strDate == null) {
                            throw new CoeusException();
                        }
                        
                        
                        dt = simpleDateFormat.parse(dateUtils.restoreDate(strDate, DATE_SEPARATERS));
                        if(dt.compareTo(datetEndDate) > 0) {
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(END_DATE_VALIDATION));
                            return ;
                        }
                        
                        if(dt.compareTo(dateStartDate) < 0) {
                            CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(END_DATE_VALIDATION));
                            return ;
                        }
                        
                        //Bug Fix: 1571 Start 5
                        if(budgetPersonnelDetailsBean.getEndDate().compareTo(new java.sql.Date(dt.getTime()))  != 0){
                            calculatePersonnelLineItem = true;
                        }else{
                            calculatePersonnelLineItem = false;
                        }
                        //Bug Fix: 1571 End 5 
                        
                        budgetPersonnelDetailsBean.setEndDate(new java.sql.Date(dt.getTime()));
                        if(persBudgetDetailController != null && calculatePersonnelLineItem) {
                            try{
                                persBudgetDetailController.calculatePersonnelLineItem(budgetPersonnelDetailsBean);
                            }catch (CoeusClientException coeusClientException){
                                CoeusOptionPane.showDialog(coeusClientException);
                            }
                        }
                        
                    }catch (ParseException parseException) {
                        parseException.getMessage();
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(INVALID_END_DATE_ENTRY));
                        return ;
                    }catch (CoeusException coeusException) {
                        CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(INVALID_END_DATE_ENTRY));
                        return ;
                    }
                    
                    break;
                case PERIOD_COL_INDEX :
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    //if(!budgetPersonnelDetailsBean.getPeriodType().equalsIgnoreCase(value.toString()) )
                    if(budgetPersonnelDetailsBean.getPeriodType()==null || !budgetPersonnelDetailsBean.getPeriodType().equalsIgnoreCase(value.toString()) )
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                    persBudgetDetailController.setSaveRequired(true);
                  
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    budgetPersonnelDetailsBean.setPeriodType(value.toString());
                    break;
                case PERCHNG_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getPercentCharged()==(Double.parseDouble(value.toString()))) {
                        break;
                    }
                    //Bug Fix: 1571 Start 6
                    if(budgetPersonnelDetailsBean.getPercentCharged() != Double.parseDouble(value.toString()) ){
                        calculatePersonnelLineItem = true;
                    }else{
                        calculatePersonnelLineItem = false;
                    }
                    //Bug Fix: 1571 End 6
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    budgetPersonnelDetailsBean.setPercentCharged(Double.parseDouble(value.toString()));
                    // Added by chandra to Fix #1102 - 10-Sept-2004 - start
                    budgetPersonnelDetailsBean.setCostSharingPercent(budgetPersonnelDetailsBean.getPercentEffort()-budgetPersonnelDetailsBean.getPercentCharged());
                    // Added by chandra to Fix #1102 - 10-Sept-2004 - End
                    
                    setAmountChanged(true);
                    if(persBudgetDetailController != null && calculatePersonnelLineItem) {
                        try{
                            persBudgetDetailController.calculatePersonnelLineItem(budgetPersonnelDetailsBean);
                        }catch (CoeusClientException coeusClientException ){
                            CoeusOptionPane.showDialog(coeusClientException);
                        }
                    }
                    
                    
                    break;
                case PERCEFFORT_COL_INDEX :
                    if(budgetPersonnelDetailsBean.getPercentEffort()==(Double.parseDouble(value.toString()))) {
                        break;
                    }
                    //Bug Fix: 1571 Start 7
                    if(budgetPersonnelDetailsBean.getPercentEffort() != Double.parseDouble(value.toString()) ){
                        calculatePersonnelLineItem = true;
                    }else{
                        calculatePersonnelLineItem = false;
                    }
                    //Bug Fix: 1571 End 7
                    budgetPersonnelDetailsBean.setAcType(TypeConstants.UPDATE_RECORD);
                    budgetPersonnelDetailsBean.setPercentEffort(Double.parseDouble(value.toString()));
                    // Added by chandra to Fix #1102 - 10-Sept-2004
                    budgetPersonnelDetailsBean.setCostSharingPercent(budgetPersonnelDetailsBean.getPercentEffort()-budgetPersonnelDetailsBean.getPercentCharged());
                    // Added by chandra to Fix #1102 - 10-Sept-2004 - End
                    setAmountChanged(true);
                    if(persBudgetDetailController != null && calculatePersonnelLineItem) {
                        try{
                            persBudgetDetailController.calculatePersonnelLineItem(budgetPersonnelDetailsBean);
                            persBudgetDetailController.setSaveRequired(true);
                        }catch (CoeusClientException coeusClientException){
                            CoeusOptionPane.showDialog(coeusClientException);
                        }
                    }
                    break;
                case SALARY_COL_INDEX :
                    
            }
            personnelBudgetTableModel.fireTableRowsUpdated(rowIndex,columnIndex);
            
        }
        
        
    }//End of Class
    
    //PersonnelBudgetTableCellRenderer =============
    class PersonnelBudgetTableHeaderRenderer extends DefaultTableCellRenderer 
    implements TableCellRenderer {
        
        private JLabel label;
        PersonnelBudgetTableHeaderRenderer (){
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
    
    //PersonnelBudgetTableCellRenderer =============
    
    class PersonnelBudgetTableCellRenderer extends DefaultTableCellRenderer {
        
        private DollarCurrencyTextField dollarCurrencyTextField;
        private JTextField txtComponent;
        private JComboBox cmbPeriod;
        private CurrencyField txtPercentage;
        private CoeusLabel lblComponent;
        
        PersonnelBudgetTableCellRenderer() {
            txtComponent = new JTextField();
            cmbPeriod = new JComboBox();
            lblComponent=new CoeusLabel();
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            lblComponent.setOpaque(true);
            
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
//            try {
//                periodArray = fetchActivePeriodTypes();
//            } catch (CoeusClientException ex) {
//                ex.printStackTrace();
//            } catch (CoeusException ex) {
//                ex.printStackTrace();
//            }
            // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
            periodArray  = activePeriodArray;
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            for(int i=0;i<periodArray.length;i++ ) {
                cmbPeriod.addItem(periodArray[i][1]);
            }
            txtPercentage =  new CurrencyField();
            dollarCurrencyTextField = new DollarCurrencyTextField();
            dollarCurrencyTextField.setHorizontalAlignment(DollarCurrencyTextField.LEFT);
            
            
            dollarCurrencyTextField.setBorder(new EmptyBorder(0,0,0,0));
            txtPercentage.setBorder(new EmptyBorder(0,0,0,0));
            cmbPeriod.setBorder(new EmptyBorder(0,0,0,0));
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
        }
        
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(JLabel.LEFT);
            
           //Bug Fix: 1571 Start 8
            
            /*if(isSelected) {               
                txtComponent.setBackground(Color.white);
                //txtComponent.setBackground(SELECTEDTABLEROWCOLOR );
                
                cmbPeriod.setBackground(SELECTEDTABLEROWCOLOR );
                txtPercentage.setBackground(SELECTEDTABLEROWCOLOR );
                dollarCurrencyTextField.setBackground(SELECTEDTABLEROWCOLOR );
                dollarCurrencyTextField.setBorder(new EmptyBorder(0,0,0,0));
                txtPercentage.setBorder(new EmptyBorder(0,0,0,0));
                cmbPeriod.setBorder(new EmptyBorder(0,0,0,0));
                txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            }
            else {
                
                 if(persBudgetDetailController.getFunctionType() == DISPLAY_MODE) {
                     
                Color tableBackGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
                txtComponent.setBackground(tableBackGroundColor);
                cmbPeriod.setBackground(tableBackGroundColor);
                txtPercentage.setBackground(tableBackGroundColor);
                dollarCurrencyTextField.setBackground(tableBackGroundColor);
                
                 } else {
                txtComponent.setForeground(Color.BLACK);
                txtComponent.setBackground(Color.WHITE);
                cmbPeriod.setBackground(Color.WHITE);
                cmbPeriod.setForeground(Color.BLACK);
                txtPercentage.setBackground(Color.WHITE);
                txtPercentage.setForeground(Color.BLACK);
                dollarCurrencyTextField.setBackground(Color.WHITE);
                dollarCurrencyTextField.setForeground(Color.BLACK);
                 }
            }*/
            //Bug Fix: 1571 End 8
            
            //Bug Fix: 1571 Start 9 
            //Commented the above code and rendering for all the colums the color here
            switch (column) {
                case NAME_COL_INDEX :
                    lblComponent.setHorizontalAlignment(JLabel.LEFT);
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtComponent.setBackground(tableBackGroundColor);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected){
//                        txtComponent.setBackground(java.awt.Color.YELLOW);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else{
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }
//                    
//                    txtComponent.setText(value.toString());
//                    return txtComponent;
                    // Bug fix for Case #1638 start 2
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }   
                    value=(value==null?EMPTY_STRING:value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    //Bug fix for Case #1638 end 2
                    
                case JOB_CODE_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtComponent.setBackground(tableBackGroundColor);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected){
//                        txtComponent.setBackground(java.awt.Color.YELLOW);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else{
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }
//                                        
//                    txtComponent.setText(value.toString());
//                    return txtComponent;
                    //Bug fix for Case #1638 Start 3
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }   
                    value=(value==null?EMPTY_STRING:value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;
                    //Bug fix for Case #1638 end 3
                    
                case START_DATE_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtComponent.setBackground(tableBackGroundColor);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected && hasFocus){
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }/*else{
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }*/
                    //Bug fix for Case #1638 Start 4
                    lblComponent.setHorizontalAlignment(JLabel.LEFT);    
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }
                    //Bug fix for Case #1638 end 4
                    try {
                        if(value != null) {
                            value = dateUtils.formatDate(value.toString(),DISPLAY_DATE_FORMAT);
                            txtComponent.setText(value.toString());
                            lblComponent.setText(txtComponent.getText());
                            
                        }
                    } catch(Exception exception) {
                        exception.getMessage();
                        txtComponent.setText(value.toString());
                        lblComponent.setText(txtComponent.getText());
                    }
                    //Bug fix for Case #1638 start 5
                   return lblComponent; 
                   //Bug fix for Case #1638 end 5
                   
                    
                case END_DATE_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtComponent.setBackground(tableBackGroundColor);
//                        txtComponent.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected && hasFocus){
//                        //scrollRectToVisible(getTable().getCellRect(row, END_DATE_COL_INDEX,true));
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }/*else{
//                        txtComponent.setBackground(java.awt.Color.white);
//                        txtComponent.setForeground(java.awt.Color.black);
//                    }*/
                    //Bug fix for case 1638 Start 6
                    lblComponent.setHorizontalAlignment(JLabel.LEFT);    
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }
                    //Bug fix for case 1638 end 6
                    try {
                        if(value != null) {
                            value = dateUtils.formatDate(value.toString(),DISPLAY_DATE_FORMAT);
                            txtComponent.setText(value.toString());
                            lblComponent.setText(txtComponent.getText());
                        }
                    } catch(Exception exception) {
                        exception.printStackTrace();
                    }
                    //Bug fix for Case #1638 start 7
                    return lblComponent;
                    //Bug fix for Case #1638 end 7
                    
                    //return txtComponent;
                    
                case PERIOD_COL_INDEX :
                   if(isSelected && hasFocus){
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else if(isSelected){
                        txtComponent.setBackground(java.awt.Color.yellow);
                        txtComponent.setForeground(java.awt.Color.black);
                    }else {
                        txtComponent.setBackground(java.awt.Color.white);
                        txtComponent.setForeground(java.awt.Color.black);
                    }
                    if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                        if(isSelected){
                            txtComponent.setEditable(false);
                            txtComponent.setForeground(java.awt.Color.black);
                            txtComponent.setBackground(java.awt.Color.yellow);
                        }else{
                            txtComponent.setEditable(false);
                            txtComponent.setForeground(java.awt.Color.black);
                            txtComponent.setBackground(tableBackGroundColor);
                        }
                    }

                         //Bug fix for case 1638 start 8
//                     lblComponent.setHorizontalAlignment(JLabel.LEFT);    
//                    if(isSelected){
//                       lblComponent.setBackground(java.awt.Color.YELLOW);
//                       lblComponent.setForeground(java.awt.Color.black);
//                       
//                    }else{
//                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
//                            lblComponent.setBackground(tableBackGroundColor);
//                            lblComponent.setForeground(java.awt.Color.black);
//                        }else{
//                            lblComponent.setBackground(java.awt.Color.white);
//                            lblComponent.setForeground(java.awt.Color.black);
//                        }
//                    }   
                          //Bug fix for case 1638 end 8
                          //Bug fix for case 1903 start 1
                       //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                       /*for(int i=0;i<periodArray.length;i++ ) {
                               if(value.toString().equalsIgnoreCase(periodArray[i][0])) {
                                   txtComponent.setText(periodArray[i][1]);
                                   return txtComponent;
                            }
                       }*/
                       if(value != null && value.toString().length()>0) {
                           boolean flag = true;
                           for(int i=0;i<periodArray.length;i++ ) {
                               if(value.toString().equalsIgnoreCase(periodArray[i][0])) {
                                   txtComponent.setText(periodArray[i][1]);
                                   flag = false;
                                   return txtComponent;
                               }
                           }
                           if(flag){
                               String [][] localPeriodArray = null;
                               try {
                                   localPeriodArray = fetchPeriodTypes();
                               } catch (CoeusClientException ex) {
                                   ex.printStackTrace();
                               }
                               for(int i=0;i<localPeriodArray.length;i++ ) {
                                   if(value.toString().equalsIgnoreCase(localPeriodArray[i][0])) {
                                       txtComponent.setText(localPeriodArray[i][1]);
                                       return txtComponent;
                                   }
                               }
                           }
                       }else{
                           txtComponent.setText(periodArray[0][1]);
                           return txtComponent;
                       }
                       //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                     //Bug fix for case 1903 end 1
                     //value=(value==null?EMPTY_STRING:value);
                   // txtComponent.setText(value.toString());
                    //Bug fix for case 1638 start 9
                   // lblComponent.setText(txtComponent.getText());
                   // return lblComponent;
                    //Bug fix for case 1638 end 9
                    //return txtComponent;
                    
                case PERCHNG_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtPercentage.setBackground(tableBackGroundColor);
//                        txtPercentage.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected && hasFocus){
//                        txtPercentage.setBackground(java.awt.Color.white);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }else if(isSelected){
//                        txtPercentage.setBackground(java.awt.Color.yellow);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }else {
//                        txtPercentage.setBackground(java.awt.Color.white);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }
//                   
//                    txtPercentage.setHorizontalAlignment(JTextField.RIGHT);
                    //Bug fix for case 1638 start 10
                     lblComponent.setHorizontalAlignment(JLabel.RIGHT);    
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }   
                     //Bug fix for case 1638 end 10
                    if(value != null){
                        txtPercentage.setText(value.toString());
                    }else{
                        txtPercentage.setText(ZEROSTRINGVALUE);
                    }
                     //Bug fix for case 1638 start 11
                     lblComponent.setText(txtPercentage.getText());
                     return lblComponent;
                     //Bug fix for case 1638 end 11
                    //return txtPercentage;
                    
                case PERCEFFORT_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        txtPercentage.setBackground(tableBackGroundColor);
//                        txtPercentage.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected && hasFocus){
//                        txtPercentage.setBackground(java.awt.Color.white);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }else if(isSelected){
//                        txtPercentage.setBackground(java.awt.Color.yellow);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }else {
//                        txtPercentage.setBackground(java.awt.Color.white);
//                        txtPercentage.setForeground(java.awt.Color.black);
//                    }
                                        
//                    txtPercentage.setHorizontalAlignment(JTextField.RIGHT);
                    //Bug fix for case 1638 start 12
                     lblComponent.setHorizontalAlignment(JLabel.RIGHT);    
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }   
                     //Bug fix for case 1638 end 12
                    if(value != null){
                        txtPercentage.setText(value.toString());
                    }else{
                        txtPercentage.setText(ZEROSTRINGVALUE);
                    }
                     //Bug fix for case 1638 start 13
                     lblComponent.setText(txtPercentage.getText());
                     return lblComponent;
                     //Bug fix for case 1638 end 13
                    //return txtPercentage;
                    
                case SALARY_COL_INDEX :
//                    if(persBudgetDetailController.getFunctionType() == TypeConstants.DISPLAY_MODE ){
//                        dollarCurrencyTextField.setBackground(tableBackGroundColor);
//                        dollarCurrencyTextField.setForeground(java.awt.Color.BLACK);
//                    }else if(isSelected){
//                        dollarCurrencyTextField.setBackground(java.awt.Color.YELLOW);
//                        dollarCurrencyTextField.setForeground(java.awt.Color.BLACK);
//                    }else{
//                        dollarCurrencyTextField.setBackground(java.awt.Color.white);
//                        dollarCurrencyTextField.setForeground(java.awt.Color.black);
//                    }
                    //Bug fix for case 1638 start 14
                     lblComponent.setHorizontalAlignment(JLabel.RIGHT);    
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        if(persBudgetDetailController.getFunctionType()==TypeConstants.DISPLAY_MODE){
                            lblComponent.setBackground(tableBackGroundColor);
                            lblComponent.setForeground(java.awt.Color.black);
                        }else{
                            lblComponent.setBackground(java.awt.Color.white);
                            lblComponent.setForeground(java.awt.Color.black);
                        }
                    }   
                     //Bug fix for case 1638 end 14
                    dollarCurrencyTextField.setText(value.toString());
                    //dollarCurrencyTextField.setHorizontalAlignment(JTextField.RIGHT);
                   // return dollarCurrencyTextField;
                     //Bug fix for case 1638 start 15
                    lblComponent.setText(dollarCurrencyTextField.getText());
                    return lblComponent;
                     //Bug fix for case 1638 end 15
            }
            //Bug Fix: 1571 End 9
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
        
    }//End Class - PersonnelBudgetTableCellRenderer ----------------------------------------
    
    //PersonnelBudgetTableCellEditor--------------------------------------------------------
    /** Instance to TableCellEditor */  //   DefaultCellEditor
    public class PersonnelBudgetTableCellEditor extends AbstractCellEditor   implements TableCellEditor {
        
        private JComboBox cmbPeriod;
        private JTextField txtComponent;
        private JTextField txtDate;
        private CurrencyField txtPerChange;
        private CurrencyField txtPerEffort;
        private DollarCurrencyTextField dollarCurrencyTextField;
        private int columnIndex;
        
        PersonnelBudgetTableCellEditor() {
            //  super(new JComboBox());
            txtComponent = new JTextField();
            
            txtDate = new JTextField();
            
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
            // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
//            try {
//                periodArray = fetchActivePeriodTypes();
//            } catch (CoeusClientException ex) {
//                ex.printStackTrace();
//            } catch (CoeusException ex) {
//                ex.printStackTrace();
//            }
            // Commented for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End
             periodArray  = activePeriodArray;
            //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
            cmbPeriod = new JComboBox();
            for(int i=0;i<periodArray.length;i++ ) {
                cmbPeriod.addItem(periodArray[i][1]);
            }
            txtPerChange = new CurrencyField();
            txtPerEffort = new CurrencyField();
            
            dollarCurrencyTextField = new DollarCurrencyTextField();
            
            txtDate.setCaretPosition(0);
            txtPerChange.setCaretPosition(0);
            txtPerEffort.setCaretPosition(0);
            txtDate.addMouseListener(new CellMouseListener() );
            cmbPeriod.addMouseListener(new CellMouseListener() );
            txtPerChange.addMouseListener(new CellMouseListener() );
            txtPerEffort.addMouseListener(new CellMouseListener() );
            
            txtDate.setBorder(new EmptyBorder(0,0,0,0));
            cmbPeriod.setBorder(new EmptyBorder(0,0,0,0));
            txtPerChange.setBorder(new EmptyBorder(0,0,0,0));
            txtPerEffort.setBorder(new EmptyBorder(0,0,0,0));
            
            // Travel all the components while pressing tab button
            java.awt.Component[] components = {
                txtDate,cmbPeriod,txtPerChange,txtPerEffort
            };
            
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            getTable().setFocusTraversalPolicy(traversePolicy);
            getTable().setFocusCycleRoot(true);
            
        }
        
        /** To get The editable component
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean true if selected
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         * @return Component
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex) {
            this.columnIndex = columnIndex;
            switch (columnIndex) {
                
                case NAME_COL_INDEX :
                    txtComponent.setText(value.toString());
                    return txtComponent;
                    
                case JOB_CODE_COL_INDEX :
                    txtComponent.setText(value.toString());
                    return txtComponent;
                    
                case START_DATE_COL_INDEX :
                    
                case END_DATE_COL_INDEX :
                    //Bug Fix: 1571 Start 10 
//                    if(isSelected) {
//                        txtDate.setBackground(SELECTEDTABLEROWCOLOR);
//                    }
                    //Bug Fix: 1571 End 10
                    
                    value = dateUtils.formatDate(value.toString(), SIMPLE_EDIT_DATE_FORMAT);
                    txtDate.setText(value.toString());
                    return txtDate;
                    
                case PERCHNG_COL_INDEX :
                    
                    if(value != null) {
                        txtPerChange.setText(value.toString());
                    }
                    else
                        txtPerChange.setText(ZEROSTRINGVALUE);
                    txtPerChange.setHorizontalAlignment(JTextField.RIGHT);
                    //Bug Fix: 1571 Start 11 
//                    if(isSelected) {
//                        txtPerChange.setBackground(SELECTEDTABLEROWCOLOR);
//                    }
                    //Bug Fix: 1571 End 11
                    return txtPerChange;
                    
                case PERCEFFORT_COL_INDEX :
                    
                    if(value != null) {
                        txtPerEffort.setText(value.toString());
                    }
                    else
                        txtPerEffort.setText(ZEROSTRINGVALUE);
                    
                    txtPerEffort.setHorizontalAlignment(JTextField.RIGHT);
                    //Bug Fix: 1571 Start 12
//                    if(isSelected) {
//                        txtPerEffort.setBackground(SELECTEDTABLEROWCOLOR);
//                    }
                    //Bug Fix: 1571 End 12 
                    
                    return txtPerEffort;
                    
                case SALARY_COL_INDEX :
                    dollarCurrencyTextField.setText(value.toString());
                    dollarCurrencyTextField.setHorizontalAlignment(JTextField.RIGHT);
                    return dollarCurrencyTextField;
                    
                case PERIOD_COL_INDEX :
                    //Bug Fix: 1571 Start 13 
//                    if(isSelected) {
//                        cmbPeriod.setBackground(SELECTEDTABLEROWCOLOR);
//                    }
                    //Bug Fix: 1571 End 13
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
                    periodArray = constructPeriodTypes(value.toString());
                    cmbPeriod.removeAllItems();
                    for(int k=0;k<periodArray.length;k++ ) {
                        cmbPeriod.addItem(periodArray[k][1]);
                    }
                    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
                    for(int i=0;i<periodArray.length;i++ ) {
                        if(value.toString().equalsIgnoreCase(periodArray[i][0])) {
                            cmbPeriod.setSelectedIndex(i);
                            return cmbPeriod;
                        }
                    }
        
            }
            return cmbPeriod;
            // return super.getTableCellEditorComponent(table, value, isSelected, rowIndex, columnIndex);
        }
        
        /** get Cell Editor Value
         * @return Object
         */
        public Object getCellEditorValue() {
            switch (columnIndex) {
                case NAME_COL_INDEX :
                    return txtComponent.getText();
                    
                case JOB_CODE_COL_INDEX :
                    return txtComponent.getText();
                    
                case START_DATE_COL_INDEX :
                case END_DATE_COL_INDEX :
                    return txtDate.getText();
                    
                case PERIOD_COL_INDEX :
                   for(int i=0;i<periodArray.length;i++ ) {
                        if( ((String) cmbPeriod.getSelectedItem()).equalsIgnoreCase(periodArray[i][1])) {
                            return periodArray[i][0];
                        }
                    }
                    
                case PERCHNG_COL_INDEX :
                    return txtPerChange.getText();
                case PERCEFFORT_COL_INDEX :
                    return txtPerEffort.getText();
                case SALARY_COL_INDEX :
                    return dollarCurrencyTextField.getValue();
                    
            }
            return dollarCurrencyTextField.getText();
            //return super.getCellEditorValue();
        }
        
        /** get the Click Count from Start
         * @return int click count
         */
        public int getClickCountToStart() {
            return 1;
        }
        
        /** To Stop cell Editing
         * @return boolean if <true> editinging of cell stopped
         */
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
        
        
    }
    //End Class PersonnelBudgetTableCellEditor----------------------------------------------
    
    /** Class to act as Listener Object for the Cells of the table
     */
    
    /** CellMouseListener Object */
    public class CellMouseListener extends MouseInputAdapter {
        
        /** Mouse Clicked */
        public void mouseClicked(MouseEvent mouseEvent) {
            if(mouseEvent.getClickCount() == 2) {
                persBudgetDetailController.callPersonnelBudgetLineItemBean(-1);
            }
        }
        /** Mouse Pressed */
        public void mousePressed(MouseEvent e) {
            
        }
        // implements java.awt.event.MouseListener
        /** Mouse Released */
        public void mouseReleased(MouseEvent e) {
            
        }
        // implements java.awt.event.MouseListener
        /** Mouse Entered */
        public void mouseEntered(MouseEvent e) {
        }
        // implements java.awt.event.MouseListener
        /** Mouse Exit */
        public void mouseExited(MouseEvent e) {
        }
    }
    
    /** ColumnHeaderListener for clcik on Column Headers */
    public class ColumnHeaderListener extends MouseAdapter {
        
        String nameBeanId [][] ={
            {"0","fullName" },
            {"1","jobCode"},
            {"2","startDate"},
            {"3","endDate"},
            {"4","periodType"},
            {"5","percentCharged"},
            {"6","percentEffort"},
            {"7","salaryRequested"}
        };
        boolean sort = true;
        /** Mouse Clicked */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                //int mColIndex = table.convertColumnIndexToModel(vColIndex);
                
                if(vColIndex >=0 && vColIndex <8 && ((CoeusVector)tableVector) != null && ((CoeusVector)tableVector).size() > 0) {
                    ((CoeusVector)tableVector).sort(nameBeanId [vColIndex][1],sort);
                    
                    if(sort) {
                        sort = false;
                    }
                    else
                        sort = true;
                    
                }
                getTableModel().fireTableRowsUpdated(0, getTableModel().getRowCount());
            } catch(Exception e) {
                e.getMessage();
            }
            
        }
    }
    
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - Start
    /**
     * Fetch all the period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String [][] fetchPeriodTypes() throws CoeusClientException{
        String [] [] arrPeriodType = null;
        HashMap<String,String> hmdata = new HashMap<String,String>();
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_PERIOD_TYPE);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + connectURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean !=null ){
            if(responderBean.isSuccessfulResponse()) {
                hmdata = (HashMap)responderBean.getDataObject();
                if (hmdata  != null && hmdata.size() > 0) {
                    arrPeriodType = storeDataToArray(hmdata);
                }
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
        return arrPeriodType;
    }
    
    /**
     * Fetch all the active period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     * @throws Exception CoeusException
     */
    public String [][] fetchActivePeriodTypes() throws CoeusClientException, CoeusException{
        String [] [] arrPeriodType = null;
        /* JM -12-16-2013 replaced with custom code returning sorted array   
        HashMap<String,String> hmdata = new HashMap<String,String>();
        RequesterBean requesterBean  = new RequesterBean();
        requesterBean.setFunctionType(GET_ACTIVE_PERIOD_TYPE);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + connectURL;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean );
        comm.send();
        ResponderBean responderBean = comm.getResponse();
         if ( responderBean !=null ){
            if(responderBean.hasResponse()) {
                hmdata = (HashMap)responderBean.getDataObject();
                if (hmdata  != null && hmdata.size() > 0) {
                    arrPeriodType = storeDataToArray(hmdata);
                }
            }else {
                throw new CoeusClientException(responderBean.getMessage(), CoeusClientException.ERROR_MESSAGE);
            }
         }else{
             throw new CoeusClientException(COULD_NOT_CONTACT_SERVER);
         }
         */
        arrPeriodType = fetchSortedActivePeriodTypes(); // JM 12-16-2013
        return arrPeriodType;
    }
    
    // JM 12-16-2013 gets active period types
    /**
     * Fetch sorted active period types from the database
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     */
    public String[][] fetchSortedActivePeriodTypes() throws CoeusClientException {
        edu.vanderbilt.coeus.budget.controller.Controller vController = 
        	new edu.vanderbilt.coeus.budget.controller.Controller();
        String [][] sortedPeriodTypes = vController.fetchActivePeriodTypes();
        return sortedPeriodTypes;    	
    }
    // JM END
    
    /**
     * Stores all the period types from HashMap to two dimesional array
     * @param HashMap hmPeriodData
     * @return String [][] arrPeriodData
     */
    private String [][] storeDataToArray(HashMap hmPeriodData){
        int leftincrement = 0;
        Set<Map.Entry<String, String>> setData = hmPeriodData.entrySet();
        int totSize = hmPeriodData.size();
        String [] [] arrPeriodData = new String[totSize][2];
        for(Map.Entry<String,String> mapData : setData){
            int rightincrement = 0;
            arrPeriodData[leftincrement][rightincrement] = mapData.getKey();
            rightincrement++;
            arrPeriodData[leftincrement][rightincrement] = mapData.getValue();
            leftincrement++;
        }
        return arrPeriodData;
    }
    
    /**
     * Fetch all the active period types from the database and add the inactive element if already present
     * @return String [][] arrPeriodType
     * @throws Exception CoeusClientException
     * @throws Exception CoeusException
     */
    private String [][] constructPeriodTypes(String selectedValue){
        String [][] activePeriodTypes = null;
        String [] [] modifiedActivePeriodTypes = null;
        String selectedCode = "";
        // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - Start
        // Instead getting the  active period types from server, uses the instance object which has the ative types
//        try {
            //Fetch all the active period types
//        activePeriodTypes = fetchActivePeriodTypes();
            activePeriodTypes = activePeriodArray;
            //Fetch all the period types
            String [][] allPeriodTypes = localPeriodArray;
            for(int i=0;i<allPeriodTypes.length;i++ ) {
                if(selectedValue.equalsIgnoreCase(allPeriodTypes[i][0])) {
                    selectedCode = allPeriodTypes[i][1];
                }
            }
//        } catch (CoeusException ex) {
//            ex.printStackTrace();
//        } catch (CoeusClientException ex) {
//            ex.printStackTrace();
//        }
        // Modified for COEUSQA-3709 : Coeus4.5: Budget Dropdown Issue - End    
        if(selectedValue.trim()!=null && selectedValue.trim().length()>0){
            for(int i=0;i<activePeriodTypes.length;i++ ) {
                if(selectedValue.equalsIgnoreCase(activePeriodTypes[i][0])) {
                    return activePeriodTypes;
                }else{
                    int size = activePeriodTypes.length+1;
                    modifiedActivePeriodTypes = new String[size][2];
                    for(int k=0; k<activePeriodTypes.length; k++){
                        for(int j=0; j<activePeriodTypes[k].length; j++){
                            modifiedActivePeriodTypes[k][j]=activePeriodTypes[k][j];
                        }
                    }
                    modifiedActivePeriodTypes[size-1][0] = selectedValue;
                    modifiedActivePeriodTypes[size-1][1] = selectedCode;
                }
            }
        }else{
            modifiedActivePeriodTypes = activePeriodTypes;
        }
        return modifiedActivePeriodTypes;
    }
    //COEUSQA-2036 Code Table Prop Dev Appt Type and Period Types - End
    
}//eld of class definition