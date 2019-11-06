/*
 * FormulatedCostBudgetLineItemController.java
 *
 * Created on December 2, 2011, 12:39 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetDetailBean;
import edu.mit.coeus.budget.bean.BudgetDetailCalAmountsBean;
import edu.mit.coeus.budget.bean.BudgetFormulatedCostDetailsBean;
import edu.mit.coeus.budget.gui.FormulatedCostBudgetLineItemForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.event.BeanEvent;
import edu.mit.coeus.unit.bean.UnitFormulatedCostBean;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusComboBox;
import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.EmptyHeaderRenderer;
import edu.mit.coeus.utils.IconRenderer;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.query.And;
import edu.mit.coeus.utils.query.Equals;
import edu.mit.coeus.utils.query.QueryEngine;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author satheeshkumarkn
 */
public class FormulatedCostBudgetLineItemController extends Controller implements ActionListener{
    
    private char functionType;
    private boolean isSaveRequired;
    private static final int HAND_ICON_COLUMN_INDEX = 0;
    private static final int FORMULATED_TYPE_COLUMN_INDEX = 1;
    private static final int UNIT_COST_COLUMN_INDEX = 2;
    private static final int COUNT_COLUMN_INDEX = 3;
    private static final int FREQUENCY_COLUMN_INDEX = 4;
    private static final int CALCULATED_EXPENSES_COLUMN_INDEX = 5;
    private static final int ROW_HEIGHT = 22;
    private static final int HAND_ICON_COLUMN_WIDTH = 25;
    private static final int FORMULATED_TYPE_COLUMN_WIDTH = 180;
    private static final int UNIT_COST_COLUMN_WIDTH = 100;
    private static final int COUNT_COLUMN_WIDTH = 50;
    private static final int FREQUENCY_COLUMN_WIDTH = 75;
    private static final int CALCULATED_EXPENSES_COLUMN_WIDTH = 135;
    private static final int COUNT_FIELD_CHAR_LENGTH = 4;
    private static final int FREQUENCY_FIELD_CHAR_LENGTH = 4;
    private int WIDTH = 725;
    private int HEIGHT = 350;
    private java.awt.Color disabledBackground = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
    private CoeusVector cvUnitFormulatedCost, cvUnitFormulatedType;
    private FormulatedCostBudgetLineItemForm formulatedCostBudgetLineItemForm;
    private FormulatedCostBudgetLineItemModel formulatedCostBudgetLineItemModel;
    private CoeusVector cvFormulatedCost;
    private BudgetDetailBean budgetDetailBean;
    private CoeusDlgWindow dlgFormulatedCost;
    public static final char GET_UNIT_FORMUALTED_TYPES = '~';
    private CoeusVector cvDeleletedFormualtedCost;
    private static final String FORMULATE_COST_DEL_CONFIRMATION = "budget_formulatedCost_exceptionCode.1000";
    private CoeusMessageResources coeusMessageResources;
    private String queryKey;
    private static final String SAVE_CHANGES_CONFIRMATION = "saveConfirmCode.1002";
    private static final String SERVLET = "/BudgetCalculationMaintenanceServlet";
    private final char CALCULATE_LINE_ITEM = 'L';
    private String  CODE_TABLE_SERVLET = "/CodeTableServlet";
    private static final char GET_CODE_TABLE_TYPE = 'Z';
    private QueryEngine queryEngine;
    private final String SELECT_FORMULATED_TYPE = "formulatedTypeSelection_exceptionCode.1000";
    private final String FORMULATED_TYPE_EXISTS = "formulatedTypeAlreadyExists_exceptionCode.1000";
    private final String CALCULATED_EXPENSES_LIMIT_EXCEED = "formulatedCostCalExpLimitExceed_exceptionCode.1000";
    private DollarCurrencyTextField dollarCurrencyTextField = null;
    private final double MAX_MANTISSA_DIGITS = 10;
    
    /** Creates a new instance of FormulatedCostlBudgetLineItemDetailsController */
    public FormulatedCostBudgetLineItemController(char functionType) {
        coeusMessageResources = CoeusMessageResources.getInstance();
        queryEngine = QueryEngine.getInstance();
        formulatedCostBudgetLineItemForm = new FormulatedCostBudgetLineItemForm();
        dollarCurrencyTextField = new DollarCurrencyTextField();
        registerComponents();
        setTableKeyTraversal();
        this.setFunctionType(functionType);
    }
    
    /**
     * Method to set the form data for the form
     * @param data 
     */
    public void setFormData(Object data){
        if(data instanceof BudgetDetailBean){
            budgetDetailBean = (BudgetDetailBean)data;
            Equals eqProposalNumber = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
            Equals eqVersionNumber = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
            Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
            Equals eqLineItemNumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
            And andProposalVersion = new And(eqProposalNumber,eqVersionNumber);
            And andBudgetPeriods = new And(andProposalVersion,eqBudgetPeriod);
            And andBudgetLineItem = new And(andBudgetPeriods,eqLineItemNumber);
             queryKey = budgetDetailBean.getProposalNumber() + budgetDetailBean.getVersionNumber();
            try {
                formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.setFormData(budgetDetailBean);
                cvFormulatedCost = queryEngine.executeQuery(queryKey, BudgetFormulatedCostDetailsBean.class, andBudgetLineItem);
                Equals eqDelete = new Equals("acType", TypeConstants.DELETE_RECORD);
                cvFormulatedCost.removeAll(cvFormulatedCost.filter(eqDelete));
            } catch (CoeusException ex) {
                ex.printStackTrace();
            }

             if(getFunctionType() != TypeConstants.DISPLAY_MODE && (cvUnitFormulatedCost == null || cvUnitFormulatedCost.isEmpty())){
                cvUnitFormulatedCost = getUnitFormulatedTypesFromServer();
            }
            
            if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
                cvUnitFormulatedType = getFormulatedTypes();
            }
            
            formulatedCostBudgetLineItemModel = new FormulatedCostBudgetLineItemModel();
            getFormulatedTable().setModel(formulatedCostBudgetLineItemModel);
            setTableColumnProperties();
           
            
            if(cvFormulatedCost != null && !cvFormulatedCost.isEmpty()){
                formulatedCostBudgetLineItemModel.setData(cvFormulatedCost);
                getFormulatedTable().setRowSelectionInterval(0,0);
            }
           formatFields(); 
           
        }
    }
    
    
    /*
     * Method to set the column properties for the formulated table
     */
    private void setTableColumnProperties(){
        getFormulatedTable().setOpaque(false);
        getFormulatedTable().setShowVerticalLines(false);
        getFormulatedTable().setShowHorizontalLines(false);
        getFormulatedTable().setRowHeight(ROW_HEIGHT);
        getFormulatedTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        getFormulatedTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TableColumn column = getFormulatedTable().getColumnModel().getColumn(HAND_ICON_COLUMN_INDEX);
        column.setMinWidth(HAND_ICON_COLUMN_WIDTH);
        column.setMaxWidth(HAND_ICON_COLUMN_WIDTH);
        column.setPreferredWidth(HAND_ICON_COLUMN_WIDTH);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setResizable(false);
        
        FormulatedCostLineItemDetailRenderer formulatedCostLineItemDetailRenderer = new FormulatedCostLineItemDetailRenderer();
        column = getFormulatedTable().getColumnModel().getColumn(FORMULATED_TYPE_COLUMN_INDEX);
        column.setMinWidth(FORMULATED_TYPE_COLUMN_WIDTH);
        column.setMaxWidth(FORMULATED_TYPE_COLUMN_WIDTH);
        column.setPreferredWidth(FORMULATED_TYPE_COLUMN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != getFunctionType()){
            column.setCellEditor(new FormulatedTypeEditor());
        }
        column.setCellRenderer(formulatedCostLineItemDetailRenderer);
        column.setResizable(true);
        
        column = getFormulatedTable().getColumnModel().getColumn(UNIT_COST_COLUMN_INDEX);
        column.setMinWidth(UNIT_COST_COLUMN_WIDTH);
        column.setMaxWidth(UNIT_COST_COLUMN_WIDTH);
        column.setPreferredWidth(UNIT_COST_COLUMN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != getFunctionType()){
            column.setCellEditor(new UnitCostEditor());
        }
        column.setCellRenderer(formulatedCostLineItemDetailRenderer);
        column.setResizable(true);
        
        column = getFormulatedTable().getColumnModel().getColumn(COUNT_COLUMN_INDEX);
        column.setMinWidth(COUNT_COLUMN_WIDTH);
        column.setMaxWidth(COUNT_COLUMN_WIDTH);
        column.setPreferredWidth(COUNT_COLUMN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != getFunctionType()){
            column.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new JTextFieldFilter(JTextFieldFilter.NUMERIC,COUNT_FIELD_CHAR_LENGTH),"",COUNT_COLUMN_INDEX)));
        }
        column.setCellRenderer(formulatedCostLineItemDetailRenderer);
        column.setResizable(true);
        
        column = getFormulatedTable().getColumnModel().getColumn(FREQUENCY_COLUMN_INDEX);
        column.setMinWidth(FREQUENCY_COLUMN_WIDTH);
        column.setMaxWidth(FREQUENCY_COLUMN_WIDTH);
        column.setPreferredWidth(FREQUENCY_COLUMN_WIDTH);
        if(TypeConstants.DISPLAY_MODE != getFunctionType()){
            column.setCellEditor(new DefaultCellEditor(
                    new CoeusTextField(new JTextFieldFilter(JTextFieldFilter.NUMERIC,FREQUENCY_FIELD_CHAR_LENGTH),"",FREQUENCY_COLUMN_INDEX)));
        }
        column.setCellRenderer(formulatedCostLineItemDetailRenderer);
        column.setResizable(true);
        
        column = getFormulatedTable().getColumnModel().getColumn(CALCULATED_EXPENSES_COLUMN_INDEX);
        column.setMinWidth(CALCULATED_EXPENSES_COLUMN_WIDTH);
        column.setMaxWidth(CALCULATED_EXPENSES_COLUMN_WIDTH);
        column.setPreferredWidth(CALCULATED_EXPENSES_COLUMN_WIDTH);
        column.setCellRenderer(formulatedCostLineItemDetailRenderer);
        column.setResizable(true);

        getFormulatedTable().getTableHeader().setReorderingAllowed( false );
        getFormulatedTable().getTableHeader().setResizingAllowed(true);
        getFormulatedTable().setFont(CoeusFontFactory.getNormalFont());
        getFormulatedTable().getTableHeader().setFont(CoeusFontFactory.getLabelFont());
    }
    
    /*
     * Method to set the table key traversal
     *
     */
    public void setTableKeyTraversal(){
        javax.swing.InputMap im = formulatedCostBudgetLineItemForm.tblFormulateType.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = formulatedCostBudgetLineItemForm.tblFormulateType.getActionMap().get(im.get(tab));
        Action tabAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                while (!table.isCellEditable(row, column) ) {
                    column += 1;
                    if (column == columnCount) {
                        column = 0;
                        row +=1;
                    }
                    if (row == rowCount) {
                        row = 0;
                    }
                    // Back to where we started, get out.
                    if (row == table.getSelectedRow() && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                table.changeSelection(row, column, false, false);
                table.editCellAt(row,column);
            }
        };
        formulatedCostBudgetLineItemForm.tblFormulateType.getActionMap().put(im.get(tab), tabAction);
        
        // for the shift+tab action
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        final Action oldTabAction1 = formulatedCostBudgetLineItemForm.tblFormulateType.getActionMap().get(im.get(shiftTab));
        Action tabAction1 = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                oldTabAction1.actionPerformed( e );
                JTable table = (JTable)e.getSource();
                int rowCount = table.getRowCount();
                int columnCount = table.getColumnCount();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                while (! table.isCellEditable(row, column) ) {
                    column -= 1;
                    if (column <= 0) {
                        column = 7;
                        row -=1;
                    }
                    if (row < 0) {
                        row = rowCount-1;
                    }
                    // Back to where we started, get out.
                    if (row == table.getSelectedRow() && column == table.getSelectedColumn()) {
                        break;
                    }
                }
                table.changeSelection(row, column, false, false);
                table.editCellAt(row,column);
            }
        };
        formulatedCostBudgetLineItemForm.tblFormulateType.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    /*
     * Method to register all the components in the form
     *
     */
    public void registerComponents(){
        formulatedCostBudgetLineItemForm.btnOk.addActionListener(this);
        formulatedCostBudgetLineItemForm.btnAdd.addActionListener(this);
        formulatedCostBudgetLineItemForm.btnDelete.addActionListener(this);
        formulatedCostBudgetLineItemForm.btnCalculate.addActionListener(this);
        formulatedCostBudgetLineItemForm.btnCancel.addActionListener(this);
        
        java.awt.Component[] components={formulatedCostBudgetLineItemForm.btnOk,formulatedCostBudgetLineItemForm.btnCancel,
        formulatedCostBudgetLineItemForm.btnAdd,formulatedCostBudgetLineItemForm.btnDelete,formulatedCostBudgetLineItemForm.btnCalculate};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        formulatedCostBudgetLineItemForm.setFocusTraversalPolicy(traversePolicy);
        formulatedCostBudgetLineItemForm.setFocusCycleRoot(true);
    }
    
    class  FormulatedCostBudgetLineItemModel extends DefaultTableModel {
        private String colNames[] = {"","Formulated Type","Unit Cost","Count", "Frequency", "Calculated Expenses"};
        private Class colTypes[]  = {Object.class, Object.class, Double.class, Integer.class,Integer.class, Double.class};
        private CoeusVector cvData;
        private double totalCost;
        DollarCurrencyTextField txtTotal= new DollarCurrencyTextField();
        
        /**
         * Method to check whether the selected column can be edited
         * @param row
         * @param column
         * @return
         */
        public boolean isCellEditable(int row, int column) {
            boolean canEdit = false;
            if((column == FORMULATED_TYPE_COLUMN_INDEX || column == UNIT_COST_COLUMN_INDEX ||
                    column == COUNT_COLUMN_INDEX || column == FREQUENCY_COLUMN_INDEX) && getFunctionType() != TypeConstants.DISPLAY_MODE){
                canEdit = true;
            }
            return canEdit;
        }
        
        /**
         * Method to get the value for row and column
         * @param row
         * @param column
         * @return Object
         */
        public Object getValueAt(int row, int column) {
            if(cvData != null && !cvData.isEmpty()){
                BudgetFormulatedCostDetailsBean formulatedCostDetailBean = (BudgetFormulatedCostDetailsBean)cvData.get(row);
                switch(column) {
                    case FORMULATED_TYPE_COLUMN_INDEX:
                        if(TypeConstants.DISPLAY_MODE != getFunctionType()){
                            ComboBoxBean comboBoxBean = new ComboBoxBean("","");
                            int formualtedCode = formulatedCostDetailBean.getFormulatedCode();
                            CoeusVector filteredVector = cvUnitFormulatedType.filter(new Equals("code", ""+formualtedCode));
                            if(filteredVector!=null && filteredVector.size() > 0){
                                comboBoxBean = (ComboBoxBean)filteredVector.get(0);
                            }
                            return comboBoxBean;
                        }else{
                            return formulatedCostDetailBean.getFormulatedCodeDescription();
                        }
                    case UNIT_COST_COLUMN_INDEX:
                        return formulatedCostDetailBean.getUnitCost();
                    case COUNT_COLUMN_INDEX:
                        return formulatedCostDetailBean.getCount();
                    case FREQUENCY_COLUMN_INDEX:
                        return formulatedCostDetailBean.getFrequency();
                    case CALCULATED_EXPENSES_COLUMN_INDEX:
                        return formulatedCostDetailBean.getUnitCost() * formulatedCostDetailBean.getCount() * formulatedCostDetailBean.getFrequency();
                    default:
                        break;
                }
                
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
        
        /**
         * Method to get the column name
         * @return String
         */
        public String getColumnName(int colIndex) {
            return colNames[colIndex];
        }
        
        /**
         * Method to set the data for the model
         * @param cvData
         */
        public void setData(CoeusVector cvData) {
            cvData.sort("formulatedNumber");
            dataVector = cvData;
            this.cvData = cvData;
            fireTableDataChanged();
        }
        
        /**
         * Method to get the model data
         * @return
         */
        public CoeusVector getData(){
            return cvData;
        }
        
        
        public int getColumnCount() {
            return colNames.length;
        }
        
        public int getRowCount() {
            return dataVector.size();
        }
        
        public Class getColumnClass(int colIndex) {
            return colTypes[colIndex];
        }
        
        
        /**
         * Method to set the model value to the table
         * @param value
         * @param row
         * @param column
         */
        public void setValueAt(Object value, int row, int column) {
            if(TypeConstants.DISPLAY_MODE == getFunctionType() ||
                    value == null || cvData == null ||
                    (cvData != null && cvData.isEmpty()) || row > cvData.size()-1 ){
                return;
            }
            BudgetFormulatedCostDetailsBean formulatedCostDetailBean = (BudgetFormulatedCostDetailsBean)cvData.get(row);
            double calculatedExpense = 0.0;
            boolean isMantissaLimitExceeded = false;
            boolean isUnitCostFromUnitFormCost = false;
            switch(column){
                case FORMULATED_TYPE_COLUMN_INDEX:
                    ComboBoxBean formulatedTypeComboBoxBean = (ComboBoxBean)value;
                    if(formulatedTypeComboBoxBean != null && !"".equals(formulatedTypeComboBoxBean.getCode())){
                            if(!TypeConstants.INSERT_RECORD.equals(formulatedCostDetailBean.getAcType())){
                                formulatedCostDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                                try {
                                    queryEngine.update(queryKey,formulatedCostDetailBean);
                                } catch (CoeusException ex) {
                                    ex.printStackTrace();
                                }
                                fireTableDataChanged();
                            }
                        }
                    break;
                case UNIT_COST_COLUMN_INDEX :
                    double unitCost = 0.0;
                    if(isUnitCostFromUnitFormCost){
                        unitCost = formulatedCostDetailBean.getUnitCost();
                    }else{
                        unitCost = Double.parseDouble(value.toString());
                    }
                    if(formulatedCostDetailBean.getUnitCost() != unitCost){
                        if(!TypeConstants.INSERT_RECORD.equals(formulatedCostDetailBean.getAcType())){
                            formulatedCostDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                        }
                        setSaveRequired(true);
                    }
                    
                    
                    calculatedExpense =  unitCost * formulatedCostDetailBean.getCount() * formulatedCostDetailBean.getFrequency();
                    isMantissaLimitExceeded = isMantissaLimitExceeded(calculatedExpense+CoeusGuiConstants.EMPTY_STRING);
                    if(isMantissaLimitExceeded){
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CALCULATED_EXPENSES_LIMIT_EXCEED));
                    }else{
                        formulatedCostDetailBean.setUnitCost(unitCost);
                        formulatedCostDetailBean.setCalculatedExpenses(calculatedExpense);
                    }
                    break;
                case COUNT_COLUMN_INDEX :
                    int count = Integer.parseInt(value.toString());
                    if(formulatedCostDetailBean.getCount() != count){
                        if(!TypeConstants.INSERT_RECORD.equals(formulatedCostDetailBean.getAcType())){
                            formulatedCostDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                            setSaveRequired(true);
                        }
                    }

                    calculatedExpense =  formulatedCostDetailBean.getUnitCost() * count * formulatedCostDetailBean.getFrequency();
                    isMantissaLimitExceeded = isMantissaLimitExceeded(calculatedExpense+CoeusGuiConstants.EMPTY_STRING);
                    if(isMantissaLimitExceeded){
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CALCULATED_EXPENSES_LIMIT_EXCEED));
                    }else{
                        formulatedCostDetailBean.setCount(count);
                        formulatedCostDetailBean.setCalculatedExpenses(calculatedExpense);
                    }
                    break;
                case FREQUENCY_COLUMN_INDEX :
                    int frequency = Integer.parseInt(value.toString());
                    if(formulatedCostDetailBean.getFrequency() != frequency){
                        if(!TypeConstants.INSERT_RECORD.equals(formulatedCostDetailBean.getAcType())){
                            formulatedCostDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                            setSaveRequired(true);
                        }
                    }
                    calculatedExpense =  formulatedCostDetailBean.getUnitCost() * formulatedCostDetailBean.getCount() * frequency;
                    isMantissaLimitExceeded = isMantissaLimitExceeded(calculatedExpense+CoeusGuiConstants.EMPTY_STRING);
                    if(isMantissaLimitExceeded){
                        CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(CALCULATED_EXPENSES_LIMIT_EXCEED));
                    }else{
                        formulatedCostDetailBean.setFrequency(frequency);
                        formulatedCostDetailBean.setCalculatedExpenses(calculatedExpense);
                    }
                    break;
                case CALCULATED_EXPENSES_COLUMN_INDEX :
                    calculatedExpense =  formulatedCostDetailBean.getUnitCost() * formulatedCostDetailBean.getCount() * formulatedCostDetailBean.getFrequency();
                    formulatedCostDetailBean.setCalculatedExpenses(calculatedExpense);
                    break;
                    
                default:
                    break;
            }
            fireTableDataChanged();
        }
        
        public void fireTableDataChanged() {
            int selectedRow = getFormulatedTable().getSelectedRow();
            fireTableChanged(new TableModelEvent(this));
            double totalCost = 0.0;
            if(cvData != null && !cvData.isEmpty()){
                // Total will be calculated for based on all the formulated CalculatedExpenses and sets to the total label
                for(Object formulatedDetails : cvData){
                    BudgetFormulatedCostDetailsBean formulatedCostDetailBean = (BudgetFormulatedCostDetailsBean)formulatedDetails;
                    totalCost = totalCost + formulatedCostDetailBean.getCalculatedExpenses();
                }
            }
            txtTotal.setText(totalCost+CoeusGuiConstants.EMPTY_STRING);
            formulatedCostBudgetLineItemForm.lblTotalValue.setText(txtTotal.getText());
            if(selectedRow > -1 && getFormulatedTable().getRowCount()-1 >= selectedRow){
                getFormulatedTable().setRowSelectionInterval(selectedRow,selectedRow);
            }
        }
    }
    
    
    public class UnitCostEditor extends AbstractCellEditor implements TableCellEditor{
        private DollarCurrencyTextField txtUnitCost;
        private CoeusTextField txtCount, txtFrequency;
        private int column;
        
        public UnitCostEditor(){
            txtUnitCost= new DollarCurrencyTextField();
            txtUnitCost.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
        }
        
        /**
         * method to get the editing cell componenet
         * @param table
         * @param value
         * @param isSelected
         * @param row
         * @param col
         * @return Component
         */
        public java.awt.Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int col){
            this.column = col;
            switch(col) {
                case UNIT_COST_COLUMN_INDEX:
                    txtUnitCost.setValue(new Double(value.toString()).doubleValue());
                    return txtUnitCost;
            }
            return txtUnitCost;
        }
        
        /**
         * Method to the cell value which is editing
         * @return String
         */
        public Object getCellEditorValue() {
            switch (column) {
                case UNIT_COST_COLUMN_INDEX:
                    return txtUnitCost.getValue();
            }
            return txtUnitCost.getValue();
        }
    }
    
    class FormulatedTypeEditor extends AbstractCellEditor implements TableCellEditor{
        private CoeusComboBox  cmbFormulatedType;
        private boolean populated = false;
        private int column;
        
        FormulatedTypeEditor() {
            cmbFormulatedType = new CoeusComboBox();
            cmbFormulatedType.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if(e.getStateChange() == ItemEvent.DESELECTED){
                        return;
                    }
                    if(e.getStateChange() == ItemEvent.SELECTED){
                        ComboBoxBean comboBoxBean = (ComboBoxBean)e.getItem();
                        String code = comboBoxBean.getCode();
                        if(!CoeusGuiConstants.EMPTY_STRING.equals(code)){
                            int formulatedCode = Integer.parseInt(code);
                            CoeusVector cvDataFromModel = formulatedCostBudgetLineItemModel.getData();
                            int selectedRow = getFormulatedTable().getEditingRow();
                            BudgetFormulatedCostDetailsBean formulatedCostDetailBean = null;
                            if(selectedRow > -1 && cvDataFromModel != null && !cvDataFromModel.isEmpty()){
                                formulatedCostDetailBean = (BudgetFormulatedCostDetailsBean)cvDataFromModel.get(selectedRow);
                            }
                            if(formulatedCostDetailBean != null && formulatedCostDetailBean.getFormulatedCode() != formulatedCode){
                                boolean isFormualtedCodeExists = isFormulatedCodeExists(cvDataFromModel, formulatedCode);
                                if(isFormualtedCodeExists){
                                    MessageFormat formatter = new MessageFormat("");
                                    String message = formatter.format(coeusMessageResources.parseMessageKey(FORMULATED_TYPE_EXISTS),
                                            "'",comboBoxBean.getDescription(),"'");
                                    CoeusOptionPane.showWarningDialog(message);
                                    stopCellEditing(); 
                                    getFormulatedTable().setRowSelectionInterval(selectedRow, selectedRow);
                                }else{
                                    if(formulatedCode != formulatedCostDetailBean.getFormulatedCode() && cvUnitFormulatedCost != null && !cvUnitFormulatedCost.isEmpty()){
                                        CoeusVector cvFilteredVector = cvUnitFormulatedCost.filter(new Equals("formulatedCode",formulatedCode));
                                        if(cvFilteredVector != null && !cvFilteredVector.isEmpty()){
                                            UnitFormulatedCostBean unitFormulatedCostBean = (UnitFormulatedCostBean)cvFilteredVector.get(0);
                                            formulatedCostDetailBean.setUnitCost(unitFormulatedCostBean.getUnitCost());
                                            formulatedCostDetailBean.setCount(0);
                                            formulatedCostDetailBean.setFrequency(0);
                                            double calculatedExpense =  formulatedCostDetailBean.getUnitCost() * formulatedCostDetailBean.getCount() * formulatedCostDetailBean.getFrequency();
                                            formulatedCostDetailBean.setCalculatedExpenses(calculatedExpense);
                                            
                                        }else{
                                            formulatedCostDetailBean.setUnitCost(0);
                                            formulatedCostDetailBean.setCount(0);
                                            formulatedCostDetailBean.setFrequency(0);
                                            formulatedCostDetailBean.setCalculatedExpenses(0);
                                        }
                                        setSaveRequired(true);
                                    }
                                    formulatedCostDetailBean.setFormulatedCode(formulatedCode);
                                    formulatedCostDetailBean.setFormulatedCodeDescription(comboBoxBean.getDescription());
                                    stopCellEditing();
                                }
                            }
                        }

                    }
                }
            });
        }
        
        /**
         * Populating the Pain Category drop down
         */
        private void populateCombo() {
            cmbFormulatedType.setModel(new DefaultComboBoxModel(cvUnitFormulatedType));
        }
        
        /**
         * Get editor component
         * @param JTable table
         * @param Object value
         * @param boolean isSelected
         * @param int row
         * @param int column
         * @return Component editor component
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            ComboBoxBean comboBoxBean = (ComboBoxBean)value;
            this.column = column;
            switch(column) {
                case FORMULATED_TYPE_COLUMN_INDEX:
                    if(! populated) {
                        populateCombo();
                        populated = true;
                    }
                    if(comboBoxBean != null && (comboBoxBean.getDescription() == null ||
                            comboBoxBean.getDescription().equals(CoeusGuiConstants.EMPTY_STRING))) {
                        ComboBoxBean selBean = (ComboBoxBean)cvUnitFormulatedType.get(0);
                        cmbFormulatedType.setSelectedItem(selBean);
                        return cmbFormulatedType;
                    }
                    cmbFormulatedType.setSelectedItem(value);
                    return cmbFormulatedType;
                default:
                    break;
            }
            return null;
        }
        
        /**
         * Get value of cell editor
         * @return Object
         */
        public Object getCellEditorValue() {
            this.column = column;
            switch(column) {
                case FORMULATED_TYPE_COLUMN_INDEX:
                    return cmbFormulatedType.getSelectedItem();
                default:
                    break;
            }
            return cmbFormulatedType;
        }
        
    }
    
    /*
     * Cell renderer for formualted cost
     */
    public class FormulatedCostLineItemDetailRenderer extends DefaultTableCellRenderer{
        
        private DollarCurrencyTextField txtDollar;
        private JLabel lblLabel;
        private java.awt.Color color;
        public FormulatedCostLineItemDetailRenderer(){
            txtDollar= new DollarCurrencyTextField();
            lblLabel = new JLabel();
            lblLabel.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
            lblLabel.setOpaque(true);
            color = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
        }
        
        /**
         * method to get the cell renderer Component
         * @param table
         * @param value
         * @param isSelected
         * @param hasFocus
         * @param row
         * @param col
         * @return Component
         */
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            
            if(getFunctionType() == TypeConstants.DISPLAY_MODE ){
                lblLabel.setBackground(disabledBackground);
                lblLabel.setForeground(java.awt.Color.BLACK);
            }else if(isSelected){
                lblLabel.setBackground(Color.YELLOW);
                lblLabel.setForeground(Color.black);
            } else {
                lblLabel.setBackground(Color.white);
                lblLabel.setForeground(Color.black);
            }
            
            switch(col) {
                case FORMULATED_TYPE_COLUMN_INDEX:
                    lblLabel.setText(CoeusGuiConstants.EMPTY_STRING);
                    lblLabel.setHorizontalAlignment(LEFT);
                    lblLabel.setText(value.toString());
                    return lblLabel;
                case UNIT_COST_COLUMN_INDEX :
                case CALCULATED_EXPENSES_COLUMN_INDEX :
                    txtDollar.setText(value.toString());
                    lblLabel.setHorizontalAlignment(RIGHT);
                    lblLabel.setText(txtDollar.getText());
                    return lblLabel;
                case COUNT_COLUMN_INDEX :
                case FREQUENCY_COLUMN_INDEX :
                    lblLabel.setText(value.toString());
                    lblLabel.setHorizontalAlignment(RIGHT);
                    return lblLabel;
            }
            return lblLabel;
        }
        
    }
    
    /**
     * Method to display the form in a dialog
     */
    public void display(){
        dlgFormulatedCost =new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Formulated Cost Line item details", true);
        dlgFormulatedCost.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                formulatedCostBudgetLineItemForm.btnOk.requestFocusInWindow();
                formulatedCostBudgetLineItemForm.btnOk.setFocusable(true);
                formulatedCostBudgetLineItemForm.btnOk.requestFocus();
            }
            public void windowClosing(WindowEvent we){
                checkBeforeClose();
            }
        });
        dlgFormulatedCost.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                checkBeforeClose();
            }
        });
        
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgFormulatedCost.setSize(WIDTH, HEIGHT);
        Dimension dlgSize = dlgFormulatedCost.getSize();
        dlgFormulatedCost.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgFormulatedCost.getContentPane().add(formulatedCostBudgetLineItemForm);
        dlgFormulatedCost.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgFormulatedCost.setFont(CoeusFontFactory.getLabelFont());
        dlgFormulatedCost.setResizable(false);
        dlgFormulatedCost.setVisible(true);

    }
    
    /**
     * Method to get the unit formulated types from the server
     * @return cvunitFormulatedType
     */
    private CoeusVector getUnitFormulatedTypesFromServer() {
        CoeusVector cvunitFormulatedType = null;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + BudgetBaseWindowController.SERVLET;
        RequesterBean request = new RequesterBean();
        request.setId(budgetDetailBean.getProposalNumber());
        request.setFunctionType(GET_UNIT_FORMUALTED_TYPES);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()) {
            cvunitFormulatedType =  (CoeusVector)response.getDataObject();
        }
        return cvunitFormulatedType;
    }
    
    public void actionPerformed(ActionEvent e) {
       

        if(getFormulatedTable().getCellEditor() !=null){
            getFormulatedTable().getCellEditor().stopCellEditing();
        }
        if(e.getSource().equals(formulatedCostBudgetLineItemForm.btnAdd)){
            addFormulatedRow();
        }else if(e.getSource().equals(formulatedCostBudgetLineItemForm.btnDelete)){
            deleteFormulatedCostRow();
        }else if(e.getSource().equals(formulatedCostBudgetLineItemForm.btnOk)){
            if(validateForm()){
                saveFormData();
            }else{
                dlgFormulatedCost.show(true);
            }
        }else if(e.getSource().equals(formulatedCostBudgetLineItemForm.btnCancel)){
            checkBeforeClose();
        }else if(e.getSource().equals(formulatedCostBudgetLineItemForm.btnCalculate)){
            try{
                dlgFormulatedCost.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                blockEvents(true);
                CoeusVector cvDataFromModel = formulatedCostBudgetLineItemModel.getData();
                if(cvDataFromModel != null && !cvDataFromModel.isEmpty()){
                    double totalCalculatedExpenses = 0.0;
                    for(Object formulatedCostDetails : cvDataFromModel){
                        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                        totalCalculatedExpenses = totalCalculatedExpenses + budgetFormulatedCostDetailsBean.getCalculatedExpenses();
                    }
                    budgetDetailBean.setLineItemCost(totalCalculatedExpenses);
                }
                formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.saveFormData();
                CoeusVector cvCalAmts = calculate(queryKey);
                Equals eqProposalNumber = new Equals("proposalNumber", budgetDetailBean.getProposalNumber());
                Equals eqVersionNumber = new Equals("versionNumber", new Integer(budgetDetailBean.getVersionNumber()));
                Equals eqBudgetPeriod = new Equals("budgetPeriod", new Integer(budgetDetailBean.getBudgetPeriod()));
                Equals eqLineItemNumber = new Equals("lineItemNumber", new Integer(budgetDetailBean.getLineItemNumber()));
                And andProposalVersion = new And(eqProposalNumber,eqVersionNumber);
                And andBudgetPeriods = new And(andProposalVersion,eqBudgetPeriod);
                And andBudgetLineItem = new And(andBudgetPeriods,eqLineItemNumber);
                
                queryEngine.removeData(queryKey,BudgetDetailCalAmountsBean.class,andBudgetLineItem);
                CoeusVector cvCalAmtsFromQueryEng = queryEngine.getDetails(queryKey,BudgetDetailCalAmountsBean.class);
                if(cvCalAmtsFromQueryEng != null && !cvCalAmtsFromQueryEng.isEmpty()){
                    cvCalAmtsFromQueryEng.addAll(cvCalAmts);
                    queryEngine.addCollection(queryKey,BudgetDetailCalAmountsBean.class,cvCalAmtsFromQueryEng);
                }else{
                    queryEngine.addCollection(queryKey,BudgetDetailCalAmountsBean.class,cvCalAmts);
                }
                formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.setSaveRequired(true);
                formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.setFormData(budgetDetailBean);
                dlgFormulatedCost.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                blockEvents(false);
            }catch(CoeusException coEx){
                coEx.printStackTrace();
                dlgFormulatedCost.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                blockEvents(false);
            }
        }
       
    }
    
    /**
     * Method to add a new row to define formulated cost
     *
     */
    private void addFormulatedRow(){
        formulatedCostBudgetLineItemForm.btnDelete.setEnabled(true);
        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = new BudgetFormulatedCostDetailsBean();
        budgetFormulatedCostDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
        budgetFormulatedCostDetailsBean.setProposalNumber(budgetDetailBean.getProposalNumber());
        budgetFormulatedCostDetailsBean.setVersionNumber(budgetDetailBean.getVersionNumber());
        budgetFormulatedCostDetailsBean.setBudgetPeriod(budgetDetailBean.getBudgetPeriod());
        budgetFormulatedCostDetailsBean.setLineItemNumber(budgetDetailBean.getLineItemNumber());
        budgetFormulatedCostDetailsBean.setFormulatedNumber(getMaxFormulatedNumber());
        CoeusVector cvFormulatedCostFromModel = formulatedCostBudgetLineItemModel.getData();
        if(cvFormulatedCostFromModel == null){
            cvFormulatedCostFromModel = new CoeusVector();
        }
        cvFormulatedCostFromModel.add(budgetFormulatedCostDetailsBean);
        formulatedCostBudgetLineItemModel.setData(cvFormulatedCostFromModel);
        setSaveRequired(true);
        int lastAddedRow = getFormulatedTable().getRowCount()-1;
        getFormulatedTable().setRowSelectionInterval(lastAddedRow,lastAddedRow);
    }
    
    /**
     * Method to delete a selected row
     *
     */    
    private void deleteFormulatedCostRow(){
        int selectedRow = getFormulatedTable().getSelectedRow();
        if(selectedRow > -1){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(FORMULATE_COST_DEL_CONFIRMATION),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            switch(selectedOption){
                case CoeusOptionPane.SELECTION_YES:
                    setSaveRequired(true);
                    CoeusVector cvFormulatedCostFromModel = formulatedCostBudgetLineItemModel.getData();
                    if(cvFormulatedCostFromModel != null && !cvFormulatedCostFromModel.isEmpty()){
                        BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)cvFormulatedCostFromModel.get(selectedRow);
                        if(!TypeConstants.INSERT_RECORD.equals(budgetFormulatedCostDetailsBean.getAcType())){
                            budgetFormulatedCostDetailsBean.setAcType(TypeConstants.DELETE_RECORD);
                        }
                        if(cvDeleletedFormualtedCost == null){
                            cvDeleletedFormualtedCost = new CoeusVector();
                        }
                        cvDeleletedFormualtedCost.add(budgetFormulatedCostDetailsBean);
                        cvFormulatedCostFromModel.remove(selectedRow);
                        formulatedCostBudgetLineItemModel.setData(cvFormulatedCostFromModel);
                        if(cvFormulatedCostFromModel != null && !cvFormulatedCostFromModel.isEmpty()){
                            int newRowCount = getFormulatedTable().getRowCount();
                            if(newRowCount >0){
                                if(newRowCount > selectedRow){
                                    getFormulatedTable().setRowSelectionInterval(selectedRow,selectedRow);
                                }else{
                                    getFormulatedTable().setRowSelectionInterval(newRowCount - 1,newRowCount - 1);
                                }
                            }
                        }
                    }
                    if(cvFormulatedCostFromModel == null || cvFormulatedCostFromModel.isEmpty()){
                        formulatedCostBudgetLineItemForm.btnDelete.setEnabled(false);
                    }
                    
            }
        }
    }
    
    /**
     * Method to get the formulated table
     * @return JTable
     */
    private JTable getFormulatedTable(){
        return formulatedCostBudgetLineItemForm.tblFormulateType;
    }
    
    /**
     * Method to get the max formulated number from the collection
     * @return maxFormualtedNumber
     */
    private int getMaxFormulatedNumber(){
        int maxFormualtedNumber = 1;
        CoeusVector cvDataFromModel = formulatedCostBudgetLineItemModel.getData();
        if(cvDataFromModel != null && !cvDataFromModel.isEmpty()){
            cvDataFromModel.sort("formulatedNumber");
            BudgetFormulatedCostDetailsBean budgetFormualtedCostBean = (BudgetFormulatedCostDetailsBean)cvDataFromModel.get(cvDataFromModel.size()-1);
            maxFormualtedNumber = budgetFormualtedCostBean.getFormulatedNumber()+1;
        }
        return maxFormualtedNumber;
    }
    
    /**
     * Method to validate the form.
     * Validation will done to select a formulated type if not selected
     * @return isValidate
     */
    private boolean validateForm(){
        boolean isValidate = true;
        CoeusVector cvDataFromModel = formulatedCostBudgetLineItemModel.getData();
        if(cvDataFromModel != null && !cvDataFromModel.isEmpty()){
            for(int formualtedIndex = 0 ; formualtedIndex < cvDataFromModel.size() ; formualtedIndex++){
                BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)cvDataFromModel.get(formualtedIndex);
                if(budgetFormulatedCostDetailsBean.getFormulatedCode() == 0){
                    isValidate = false;
                    CoeusOptionPane.showWarningDialog(coeusMessageResources.parseMessageKey(SELECT_FORMULATED_TYPE));
                    getFormulatedTable().setRowSelectionInterval(formualtedIndex,formualtedIndex);
                    break;
                }
            }
        }
        
        return isValidate;
    }
    
    /**
     * Method to check the form before close
     */
    private void checkBeforeClose(){
        if(getFormulatedTable().isEditing()){
            if(getFormulatedTable().getCellEditor() != null){
                getFormulatedTable().getCellEditor().stopCellEditing();
            }
        }
        if(isSaveRequired() ){
            int selectedOption = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(SAVE_CHANGES_CONFIRMATION),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_NO);
            switch(selectedOption){
                case CoeusOptionPane.SELECTION_YES:
                    if(validateForm()){
                        formulatedCostBudgetLineItemForm.btnOk.doClick();
                        setSaveRequired(false);
                        dlgFormulatedCost.dispose();
                    }else{
                        dlgFormulatedCost.setVisible(true);
                    }
                    break;
                case CoeusOptionPane.SELECTION_NO:
                    setSaveRequired(false);
                    dlgFormulatedCost.dispose();
                    break;
                case CoeusOptionPane.SELECTION_CANCEL:
                    dlgFormulatedCost.setVisible(true);
                    break;
            }
        }else{
            dlgFormulatedCost.dispose();
        }
    }

    
    /**
     * Method to calculate the line item rates based on the total of all formulated cost calculated expenses
     * @param key 
     * @return cvCalAmts
     */
    public CoeusVector calculate(String key) {
        CoeusVector cvCalAmts = new CoeusVector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SERVLET;
        RequesterBean request = new RequesterBean();
        request.setDataObject(queryEngine.getDataCollection(key));
        Vector vecBudgetDetails = new Vector();
        vecBudgetDetails.add(new Integer(budgetDetailBean.getBudgetPeriod()));
        vecBudgetDetails.add(budgetDetailBean);
        request.setDataObjects(vecBudgetDetails);
        request.setFunctionType(CALCULATE_LINE_ITEM);

        ResponderBean response;
        try {
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            response = comm.getResponse();
            if(response==null){
                CoeusOptionPane.showErrorDialog("Could Not Fetch Data From Server After Calculation ");
                 
            }
            if(response.isSuccessfulResponse()) {
                cvCalAmts =  (CoeusVector)response.getDataObject();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        
        return cvCalAmts;
    }

    /**
     * 
     * @return 
     */
    public Component getControlledUI() {
        return formulatedCostBudgetLineItemForm;
    }

    /**
     * 
     * @return 
     */
    public Object getFormData() {
        return null;
    }

    public void formatFields() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            formulatedCostBudgetLineItemForm.btnAdd.setEnabled(false);
            formulatedCostBudgetLineItemForm.btnOk.setEnabled(false);
            formulatedCostBudgetLineItemForm.btnDelete.setEnabled(false);
            formulatedCostBudgetLineItemForm.btnCalculate.setEnabled(false);
            formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts.setEnabled(false);
        }else{
            formulatedCostBudgetLineItemForm.btnAdd.setEnabled(true);
            formulatedCostBudgetLineItemForm.btnOk.setEnabled(true);
            if(cvFormulatedCost != null && !cvFormulatedCost.isEmpty()){
                formulatedCostBudgetLineItemForm.btnDelete.setEnabled(true);
            }else{
                formulatedCostBudgetLineItemForm.btnDelete.setEnabled(false);
            }
            
            formulatedCostBudgetLineItemForm.btnCalculate.setEnabled(true);
            formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.tblCalculatedAmounts.setEnabled(true);
        }
    }

    /**
     * 
     * @throws edu.mit.coeus.exception.CoeusUIException 
     * @return 
     */
    public boolean validate() throws CoeusUIException {
        return false;
    }

    public void saveFormData() {
        if(formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.isSaveRequired()){
            formulatedCostBudgetLineItemForm.pnlLineItemCalAmtsTable.saveFormData();
        }
        try{
            boolean isFormulatedCostDeleted = false;
            if(cvDeleletedFormualtedCost != null && !cvDeleletedFormualtedCost.isEmpty()){
                for(Object formulatedCostDetails : cvDeleletedFormualtedCost){
                    BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                    Equals eqDelete = new Equals("acType",TypeConstants.DELETE_RECORD);
                    queryEngine.delete(queryKey,budgetFormulatedCostDetailsBean);
                }
                isFormulatedCostDeleted = true;
                cvDeleletedFormualtedCost.removeAllElements();
            }
            
            CoeusVector cvDataFromModel = formulatedCostBudgetLineItemModel.getData();
            double totalCalculatedExpenses = 0.0;
            if(cvDataFromModel != null && !cvDataFromModel.isEmpty()){
                Equals eqInsert = new Equals("acType",TypeConstants.INSERT_RECORD);
                Equals eqPeriod = new Equals("budgetPeriod",budgetDetailBean.getBudgetPeriod());
                Equals eqLineitem = new Equals("lineItemNumber",budgetDetailBean.getLineItemNumber());
                And andPeriodLineItem = new And(eqPeriod,eqLineitem);
                And andPeriodLineInsert = new And(andPeriodLineItem,eqInsert);
                queryEngine.removeData(queryKey,BudgetFormulatedCostDetailsBean.class,andPeriodLineInsert);
                for(Object formulatedCostDetails : cvDataFromModel){
                    BudgetFormulatedCostDetailsBean budgetFormulatedCostDetailsBean = (BudgetFormulatedCostDetailsBean)formulatedCostDetails;
                    if(TypeConstants.INSERT_RECORD.equals(budgetFormulatedCostDetailsBean.getAcType())){
                        queryEngine.insert(queryKey,BudgetFormulatedCostDetailsBean.class,budgetFormulatedCostDetailsBean);
                    }else if(TypeConstants.UPDATE_RECORD.equals(budgetFormulatedCostDetailsBean.getAcType())){
                        queryEngine.update(queryKey,budgetFormulatedCostDetailsBean);
                    }
                    totalCalculatedExpenses = totalCalculatedExpenses + budgetFormulatedCostDetailsBean.getCalculatedExpenses();
                }
            }else if(isFormulatedCostDeleted){
                totalCalculatedExpenses = 0.0;
            }else{
                totalCalculatedExpenses = budgetDetailBean.getLineItemCost();
            }
            budgetDetailBean.setLineItemCost(totalCalculatedExpenses);
            if(cvDataFromModel != null && !cvDataFromModel.isEmpty()){
                budgetDetailBean.setQuantity(cvDataFromModel.size());
            }
            budgetDetailBean.setCostSharingAmount(0.0);
            budgetDetailBean.setUnderRecoveryAmount(0.0);
            budgetDetailBean.setApplyInRateFlag(false);
            budgetDetailBean.setBudgetJustification(CoeusGuiConstants.EMPTY_STRING);
            budgetDetailBean.setSubmitCostSharingFlag(false);
            
            queryEngine.update(queryKey, budgetDetailBean);
            
        } catch (CoeusException ex) {
            ex.printStackTrace();
        }
        BeanEvent beanEvent = new BeanEvent();
        beanEvent.setBean(budgetDetailBean);
        beanEvent.setSource(this);
        fireBeanUpdated(beanEvent);
        
        setSaveRequired(false);
        dlgFormulatedCost.dispose();
    }

    /**
     * Method to check formulated code already exists
     * @param cvModelData 
     * @param formualtedCode 
     * @return isFormulatedCodeExists
     */
    private boolean isFormulatedCodeExists(CoeusVector cvModelData, int formualtedCode) {
        boolean isFormulatedCodeExists = false;
        if(cvModelData != null && !cvModelData.isEmpty()){
            CoeusVector cvFilterFormCost = cvModelData.filter(new Equals("formulatedCode",formualtedCode));
            if(cvFilterFormCost != null && !cvFilterFormCost.isEmpty()){
                isFormulatedCodeExists = true;
            }
            
        }
        return isFormulatedCodeExists;
    }
    
    /**
     * Method to get the formualted types
     */
    private CoeusVector getFormulatedTypes() {
        CoeusVector cvFormualtedTypes = null;
        Hashtable htData =  queryEngine.getDataCollection(queryKey);
        if(htData != null){
            cvFormualtedTypes = (CoeusVector) htData.get(CoeusConstants.FORMULATED_TYPES);
            ComboBoxBean emptyComboBoxBean = new ComboBoxBean("","");
            cvFormualtedTypes.add(0,emptyComboBoxBean);

        }
        return cvFormualtedTypes;
    }
    
    /**
     *  Checks if mantissa Limit has Exceeded returns if <code>true</code> when exceeded
     *  used to check whether the parameter is exceeded and then modification of subsequent
     *  characters in the parameter is carried out
     *  @param String parseString String .
     *  @return boolean true  
     */
    public boolean isMantissaLimitExceeded(String parseString) {
        boolean isMantissaLimitExceeded = false;
        dollarCurrencyTextField.setText(parseString);
        parseString = dollarCurrencyTextField.getText();
        int mantissa = parseString.substring((parseString.indexOf('$')+1),parseString.indexOf('.')).replaceAll(",","" ).length() ;
        if(parseString.indexOf('-') != -1){
            --mantissa;
        }
        if((mantissa+1) > MAX_MANTISSA_DIGITS){
            isMantissaLimitExceeded = true;
        }
        return isMantissaLimitExceeded;
    }
    
}

