/*
 * BudgetSubAwardDetailController.java
 *
 * Created on July 1, 2011, 12:47 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.BudgetSubAwardConstants;
import edu.mit.coeus.budget.bean.BudgetSubAwardBean;
import edu.mit.coeus.budget.bean.BudgetSubAwardDetailBean;
import edu.mit.coeus.budget.gui.BudgetSubAwardDetailForm;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusTextField;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.utils.ObjectCloner;
import edu.mit.coeus.utils.TypeConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author satheeshkumarkn
 */
public class BudgetSubAwardDetailController extends Controller implements ActionListener{
    private BudgetSubAwardDetailForm subAwardBudgetDetailForm;
    private static final int PERIOD_COLUMN_INDEX = 0;
    private static final int DIRECT_COST_COLUMN_INDEX = 1;
    private static final int IN_DIRECT_COST_COLUMN_INDEX = 2;
    private static final int COST_SHARING_COST_COLUMN_INDEX = 3;
    private static final int TOTAL_COST_COLUMN_INDEX = 4;
    private SubAwardBudgetDetailTableModel subAwardBudgetDetailTableModel;
    private int columnWidth[] = {50, 125, 125, 125, 125};
    private SubAwardBudgetDetailEditor subAwardBudgetDetailEditor;
    private CoeusDlgWindow subAwardDetailDlgWindow;
    private static final String TITLE = "Sub Award Detail Entry";
    private CoeusAppletMDIForm mdiForm;
    private int width = 675;
    private int height = 150;
    private int detailHeight = 120;
    private String organizationName;
    private static final String connect = CoeusGuiConstants.CONNECTION_URL+"/BudgetSubAwardServlet";
    private BudgetSubAwardBean budgetSubAwardBean;
        
    /** Creates a new instance of BudgetSubAwardDetailController */
    public BudgetSubAwardDetailController(char functionType, BudgetSubAwardBean budgetSubAwardBean) {
        setFunctionType(functionType);
        this.organizationName = budgetSubAwardBean.getOrganizationName();
        this.budgetSubAwardBean = budgetSubAwardBean;
        initComponents();
        registerComponents();
        setTableKeyTraversal();
    }
    
    /*
     * Method to intialize the components
     */
    private void initComponents(){
        subAwardBudgetDetailForm = new BudgetSubAwardDetailForm();
        JTableHeader header = subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getTableHeader();
        header.setFont(CoeusFontFactory.getLabelFont());
        mdiForm = CoeusGuiConstants.getMDIForm();
        
        subAwardBudgetDetailTableModel = new SubAwardBudgetDetailTableModel(getFunctionType());
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setModel(subAwardBudgetDetailTableModel);
        
        for(int index = 0; index < columnWidth.length; index++) {
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(index).setMinWidth(columnWidth[index]/2);
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(index).setPreferredWidth(columnWidth[index]);
        }
        ColumnPeriodRenderer columnPeriodRenderer = new ColumnPeriodRenderer();
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(PERIOD_COLUMN_INDEX).setCellRenderer(columnPeriodRenderer);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(DIRECT_COST_COLUMN_INDEX).setCellRenderer(columnPeriodRenderer);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(IN_DIRECT_COST_COLUMN_INDEX).setCellRenderer(columnPeriodRenderer);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(COST_SHARING_COST_COLUMN_INDEX).setCellRenderer(columnPeriodRenderer);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(TOTAL_COST_COLUMN_INDEX).setCellRenderer(columnPeriodRenderer);
        
        if(getFunctionType() != TypeConstants.DISPLAY_MODE) {
            subAwardBudgetDetailEditor = new SubAwardBudgetDetailEditor();
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(DIRECT_COST_COLUMN_INDEX).setCellEditor(subAwardBudgetDetailEditor);
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(IN_DIRECT_COST_COLUMN_INDEX).setCellEditor(subAwardBudgetDetailEditor);
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(COST_SHARING_COST_COLUMN_INDEX).setCellEditor(subAwardBudgetDetailEditor);
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getColumnModel().getColumn(TOTAL_COST_COLUMN_INDEX).setCellEditor(subAwardBudgetDetailEditor);
        }
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setSelectionBackground(Color.YELLOW);
        subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setSelectionForeground(Color.BLACK);
        
        subAwardDetailDlgWindow = new CoeusDlgWindow(mdiForm, TITLE, true);
        subAwardDetailDlgWindow.getContentPane().add(subAwardBudgetDetailForm);
        subAwardDetailDlgWindow.setSize(width, height + detailHeight);
        subAwardBudgetDetailForm.setPreferredSize(new Dimension(width, height + detailHeight));
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        subAwardDetailDlgWindow.setLocation(screenSize.width/2 - (width/2), screenSize.height/2 - ((height + detailHeight)/2));
    }
    
    /**
     * Method to get the controller UI
     * @return 
     */
    public Component getControlledUI() {
        return subAwardBudgetDetailForm;
    }
    
    /**
     * Method to set the sub award detail form data
     * @param data 
     */
    public void setFormData(Object data) {
        subAwardBudgetDetailTableModel.setData((Vector)data);
    }
    
    /**
     * Method to get the sub award detail form data
     * @return 
     */
    public Object getFormData() {
        return subAwardBudgetDetailTableModel.getData();
    }
    
    public void formatFields() {
    }
    
    /**
     * Method to  validate the form data
     * @throws edu.mit.coeus.exception.CoeusUIException 
     * @return 
     */
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    /*
     * Method to register all the components
     */
    public void registerComponents() {
        if(getFunctionType() == TypeConstants.DISPLAY_MODE){
            subAwardBudgetDetailForm.btnOk.setEnabled(false);
            subAwardBudgetDetailForm.btnSyncXml.setEnabled(false);
        }else{
            if(!BudgetSubAwardConstants.XML_GENERATED_SUCCESSFULLY.equals(this.budgetSubAwardBean.getTranslationComments())){
                subAwardBudgetDetailForm.btnSyncXml.setEnabled(false);
            }
            subAwardBudgetDetailForm.btnOk.addActionListener(this);
            subAwardBudgetDetailForm.btnSyncXml.addActionListener(this);
        }
        subAwardBudgetDetailForm.btnCancel.addActionListener(this);
        subAwardDetailDlgWindow.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                close();
                return;
            }
        });
        subAwardDetailDlgWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        subAwardDetailDlgWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                close();
            }
        });
    }
    
    /*
     * Method to be called when the window is closed
     */
    private void close(){
        if(subAwardBudgetDetailEditor != null){
            subAwardBudgetDetailEditor.stopCellEditing();
        }
        if(isSaveRequired()){
            CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
            int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                                                                CoeusOptionPane.OPTION_YES_NO_CANCEL, CoeusOptionPane.DEFAULT_YES);
            if(selection == CoeusOptionPane.SELECTION_YES){
                updateSubAwardDetailBean();
            }else if(selection == CoeusOptionPane.SELECTION_NO || selection == CoeusOptionPane.SELECTION_CANCEL) {
                // Need to get the data before modification
                Vector vecSubAwardPeriods = (Vector)subAwardBudgetDetailTableModel.getData();
                if(vecSubAwardPeriods != null && !vecSubAwardPeriods.isEmpty()){
                    for(Object subAwardPeriodDetails : vecSubAwardPeriods){
                        BudgetSubAwardDetailBean subAwardPeriodBean = (BudgetSubAwardDetailBean)subAwardPeriodDetails;
                        subAwardPeriodBean.setDirectCost(subAwardPeriodBean.getBeforeModifiedDirectCost());
                        subAwardPeriodBean.setIndirectCost(subAwardPeriodBean.getBeforeModifiedIndirectCost());
                        subAwardPeriodBean.setCostSharingAmount(subAwardPeriodBean.getBeforeModifiedCostSharingAmount());
                        subAwardPeriodBean.setTotalCost(subAwardPeriodBean.getBeforeModifiedTotalCost());
                    }
                }
                setSaveRequired(false);
            }
            
        }
  
        subAwardDetailDlgWindow.dispose();
    }
 
    /*
     * Method to save the form data
     */
    public void saveFormData() {
    }
    
    /*
     * Method to display the form 
     */
    public void display() {
        subAwardBudgetDetailForm.lblOrganizationNameValue.setText(organizationName);
        subAwardDetailDlgWindow.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        subAwardDetailDlgWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        blockEvents(true);
        Object source = actionEvent.getSource();
        if(subAwardBudgetDetailForm.btnOk.equals(source)) {
            if(subAwardBudgetDetailEditor != null){
                subAwardBudgetDetailEditor.stopCellEditing();
            }
            updateSubAwardDetailBean();
            subAwardDetailDlgWindow.dispose();
        }else if(subAwardBudgetDetailForm.btnCancel.equals(source)) {
            close();
        }else if(subAwardBudgetDetailForm.btnSyncXml.equals(source)){
            CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
            String message = coeusMessageResources.parseMessageKey("budgetSubAward_exceptionCode.1558");
            int option = CoeusOptionPane.showQuestionDialog(message,CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
            if (option == JOptionPane.YES_OPTION) {
                syncXMLData();
            }
        }
        subAwardDetailDlgWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    /*
     * method to update SubAwardDetail bean
     */
    private void updateSubAwardDetailBean(){
        Vector vecSubAwardPeriodsDetail =(Vector)subAwardBudgetDetailTableModel.getData();
        if(vecSubAwardPeriodsDetail != null && !vecSubAwardPeriodsDetail.isEmpty()){
            for(Object subAwardPeriodDetails : vecSubAwardPeriodsDetail){
                BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)subAwardPeriodDetails;
                subAwardDetailBean.setBeforeModifiedCostSharingAmount(subAwardDetailBean.getCostSharingAmount());
                subAwardDetailBean.setBeforeModifiedDirectCost(subAwardDetailBean.getDirectCost());
                subAwardDetailBean.setBeforeModifiedIndirectCost(subAwardDetailBean.getIndirectCost());
                subAwardDetailBean.setBeforeModifiedTotalCost(subAwardDetailBean.getTotalCost());
                if(!TypeConstants.INSERT_RECORD.equals(subAwardDetailBean.getAcType())){
                    subAwardDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                }
            }
        }
        setSaveRequired(false);
    }
    
    /*
     * Model class for the SubAward Details
     */
    class SubAwardBudgetDetailTableModel extends DefaultTableModel{
        private List data;
        private String columnNames[] = {"Period", "Direct Cost", "Indirect Cost", "Cost Sharing", "Total Cost"};
        private char mode;
        
        public SubAwardBudgetDetailTableModel(char mode){
            this.mode = mode;
        }
        
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            BudgetSubAwardDetailBean budgetSubAwarDetailBean = (BudgetSubAwardDetailBean)data.get(rowIndex);
            switch (columnIndex) {
                case DIRECT_COST_COLUMN_INDEX:
                    if(value == null) {
                        budgetSubAwarDetailBean.setDirectCost(0.0);
                    }else{
                        try{
                            double directCost = Double.parseDouble(value.toString());
                            if(directCost != budgetSubAwarDetailBean.getDirectCost()){
                                setSaveRequired(true);
                            }
                            budgetSubAwarDetailBean.setDirectCost(directCost);
                            if(budgetSubAwarDetailBean.getDirectCost() == 0.0 && budgetSubAwarDetailBean.getIndirectCost() == 0.0){
                                budgetSubAwarDetailBean.setTotalCost(0.0);
                            }else{
                                budgetSubAwarDetailBean.setTotalCost(budgetSubAwarDetailBean.getDirectCost() + budgetSubAwarDetailBean.getIndirectCost());
                            }
                        }catch(NumberFormatException numFormatExcep){
                            CoeusOptionPane.showErrorDialog("Enter a valid Direct cost");
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setRowSelectionInterval(rowIndex, rowIndex);
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setColumnSelectionInterval(columnIndex,columnIndex);
                            return;
                        }
                    }
                    break;
                case IN_DIRECT_COST_COLUMN_INDEX:
                    if(value == null) {
                        budgetSubAwarDetailBean.setIndirectCost(0.0);
                    }else{
                        try{
                            double inDirectCost = Double.parseDouble(value.toString());
                            if(inDirectCost != budgetSubAwarDetailBean.getIndirectCost()){
                                setSaveRequired(true);
                            }
                            budgetSubAwarDetailBean.setIndirectCost(inDirectCost);
                            if(budgetSubAwarDetailBean.getDirectCost() == 0.0 && budgetSubAwarDetailBean.getIndirectCost() == 0.0){
                                budgetSubAwarDetailBean.setTotalCost(0.0);
                            }else{
                                budgetSubAwarDetailBean.setTotalCost(budgetSubAwarDetailBean.getDirectCost() + budgetSubAwarDetailBean.getIndirectCost());
                            }
                        }catch(NumberFormatException numFormatExcep){
                            CoeusOptionPane.showErrorDialog("Enter a valid Indirect cost");
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setRowSelectionInterval(rowIndex, rowIndex);
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setColumnSelectionInterval(columnIndex,columnIndex);
                            return;
                        }
                    }
                    break;
                    
                case COST_SHARING_COST_COLUMN_INDEX:
                    if(value == null) {
                        budgetSubAwarDetailBean.setCostSharingAmount(0.0);
                    }else{
                        try{
                            double costSharingAmount = Double.parseDouble(value.toString());
                            if(costSharingAmount != budgetSubAwarDetailBean.getCostSharingAmount()){
                                setSaveRequired(true);
                            }
                            budgetSubAwarDetailBean.setCostSharingAmount(costSharingAmount);

                        }catch(NumberFormatException numFormatExcep){
                            CoeusOptionPane.showErrorDialog("Enter a valid Cost Sharing cost");
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setRowSelectionInterval(rowIndex, rowIndex);
                            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.setColumnSelectionInterval(columnIndex,columnIndex);
                            return;
                        }
                    }
                    break;
               
                    
            }
            subAwardBudgetDetailTableModel.fireTableRowsUpdated(rowIndex,rowIndex);
        }
        
        public int getRowCount() {
            if(data == null) return 0;
            return data.size();
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(mode == TypeConstants.DISPLAY_MODE || columnIndex == PERIOD_COLUMN_INDEX || columnIndex == TOTAL_COST_COLUMN_INDEX) {
                return false;
            }
            return true;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            BudgetSubAwardDetailBean budgetSubAwardDetailBean = (BudgetSubAwardDetailBean)data.get(rowIndex);
            switch (columnIndex) {
                case PERIOD_COLUMN_INDEX:
                    return budgetSubAwardDetailBean.getBudgetPeriod();
                case DIRECT_COST_COLUMN_INDEX:
                    return budgetSubAwardDetailBean.getDirectCost();
                case IN_DIRECT_COST_COLUMN_INDEX :
                    return budgetSubAwardDetailBean.getIndirectCost();
                case COST_SHARING_COST_COLUMN_INDEX:
                    return budgetSubAwardDetailBean.getCostSharingAmount();
                case TOTAL_COST_COLUMN_INDEX:
                    return budgetSubAwardDetailBean.getDirectCost()+budgetSubAwardDetailBean.getIndirectCost();
            }
            return null;
        }
        
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
        
        public Class getColumnClass(int columnIndex) {
            return String.class;
        }
        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /**
         * Getter for property data.
         * @return Value of property data.
         */
        public java.util.List getData() {
            return data;
        }
        
        /**
         * Setter for property data.
         * @param data New value of property data.
         */
        public void setData(java.util.List data) {
            this.data = data;
        }
    }//End SubAwardBudgetDetailTableModel
    
    /*
     * Editor class for Sub award details
     */
    public class SubAwardBudgetDetailEditor extends  AbstractCellEditor implements TableCellEditor {
        
        private CoeusTextField txtDateComponent;
        private DollarCurrencyTextField txtDollarCurrencyTextField;
        private int column;
        
        public SubAwardBudgetDetailEditor(){
            txtDateComponent = new CoeusTextField();
            txtDollarCurrencyTextField = new DollarCurrencyTextField(12,DollarCurrencyTextField.RIGHT,false);
            
        }
        /**
         * 
         * @param table 
         * @param value 
         * @param isSelected 
         * @param row 
         * @param column 
         * @return 
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            
            switch(column){
                case PERIOD_COLUMN_INDEX:
                case DIRECT_COST_COLUMN_INDEX:
                case IN_DIRECT_COST_COLUMN_INDEX :
                case COST_SHARING_COST_COLUMN_INDEX:
                case TOTAL_COST_COLUMN_INDEX:
                    if(isSelected){
                        txtDollarCurrencyTextField.selectAll();
                    }
                    txtDollarCurrencyTextField.setValue(new Double(value.toString()).doubleValue());
                    return txtDollarCurrencyTextField;
            }
            return txtDollarCurrencyTextField;
        }
        
        /**
         * 
         * @return 
         */
        public Object getCellEditorValue() {
            switch (column) {
                case PERIOD_COLUMN_INDEX:
                case DIRECT_COST_COLUMN_INDEX:
                case IN_DIRECT_COST_COLUMN_INDEX :
                case COST_SHARING_COST_COLUMN_INDEX:
                case TOTAL_COST_COLUMN_INDEX:
                    return txtDollarCurrencyTextField.getValue();
            }
            return ((JTextField)txtDollarCurrencyTextField).getText();
        }
        
        /**
         * 
         * @return 
         */
        public int getClickCountToStart(){
            return 1;
        }
        
        /**
         * 
         * @return 
         */
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
        
    }//End of Cell Editor Class
    
     /*
     * Renderer class for Sub award details
     */
    private class ColumnPeriodRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
        
        private DollarCurrencyTextField dollarCurrencyTextField;
        private JTextField txtPeriod;
        private int selRow;
        private int selCol;
        
        public ColumnPeriodRenderer() {
            dollarCurrencyTextField = new DollarCurrencyTextField(12,JTextField.RIGHT,true);
            dollarCurrencyTextField.setHorizontalAlignment(DollarCurrencyTextField.RIGHT);
            dollarCurrencyTextField.setBorder(new EmptyBorder(0,0,0,0));
            setOpaque(true);
            subAwardBudgetDetailForm.tblSubAwardBudgetDetails.editCellAt(selRow,selCol);
            setFont(CoeusFontFactory.getNormalFont());
            
            txtPeriod = new JTextField();
            setOpaque(true);
            setFont(CoeusFontFactory.getNormalFont());
            txtPeriod.setBorder(new BevelBorder(BevelBorder.LOWERED));
        }
        
        public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if( isSelected ){
                txtPeriod.setBackground(Color.YELLOW);
                dollarCurrencyTextField.setBackground(Color.YELLOW);
            }else{
                txtPeriod.setBackground(Color.WHITE);
                dollarCurrencyTextField.setBackground(Color.WHITE);
            }
            if(getFunctionType() == TypeConstants.DISPLAY_MODE){
                txtPeriod.setBorder(new EmptyBorder(0,0,0,0));
                txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                dollarCurrencyTextField.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            }
            
            selRow = row;
            selCol = column;
            switch (column){
                case PERIOD_COLUMN_INDEX:
                    if(isSelected){
                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtPeriod.setForeground(Color.black);
                    } else {
                        txtPeriod.setBackground((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
                        txtPeriod.setForeground(Color.black);
                    }
                    txtPeriod.setText(value.toString());
                    txtPeriod.setHorizontalAlignment(JTextField.RIGHT);
                    return txtPeriod;
                case DIRECT_COST_COLUMN_INDEX:
                case IN_DIRECT_COST_COLUMN_INDEX:
                case COST_SHARING_COST_COLUMN_INDEX:
                case TOTAL_COST_COLUMN_INDEX:
                    dollarCurrencyTextField.setValue(new Double(value.toString()).doubleValue());
                    return dollarCurrencyTextField;
                    
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        
    }// End Class ColumnPeriodRenderer
    
    public void setTableKeyTraversal(){
        
        javax.swing.InputMap im = subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        // Have the enter key work the same as the tab key
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        KeyStroke shiftTab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB,KeyEvent.SHIFT_MASK );
        
        // Override the default tab behaviour
        // Tab to the next editable cell. When no editable cells goto next cell.
        final Action oldTabAction = subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getActionMap().get(im.get(tab));
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
       subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getActionMap().put(im.get(tab), tabAction);

       // for the shift+tab action
        // Override the default tab behaviour
        // Tab to the previous editable cell. When no editable cells goto next cell.
        
        final Action oldTabAction1 = subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getActionMap().get(im.get(shiftTab));
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
       subAwardBudgetDetailForm.tblSubAwardBudgetDetails.getActionMap().put(im.get(shiftTab), tabAction1);
    }
    
    /**
     * Method to sync XML period details
     * @return syncSuccessful
     */
    private boolean syncXMLData(){
        boolean syncSuccessful = false;
        RequesterBean requesterBean = new RequesterBean();
        requesterBean.setDataObject(budgetSubAwardBean);
        requesterBean.setFunctionType(BudgetSubAwardConstants.SYNC_XML_FOR_SUB_AWARD_DETAILS);
        AppletServletCommunicator appletServletCommunicator = new AppletServletCommunicator();
        appletServletCommunicator.setConnectTo(connect);
        appletServletCommunicator.setRequest(requesterBean);
        appletServletCommunicator.send();
        ResponderBean responderBean = appletServletCommunicator.getResponse();
        if(responderBean.isSuccessfulResponse()){
            Vector vecSubAwardPDFDetail = responderBean.getDataObjects();
            if(subAwardBudgetDetailEditor != null){
                subAwardBudgetDetailEditor.stopCellEditing();
            }
            Vector vecCurrentSubAwardData = (Vector)subAwardBudgetDetailTableModel.getData();
            if(vecCurrentSubAwardData != null && !vecCurrentSubAwardData.isEmpty()){
                vecSubAwardPDFDetail = updSubAwdDetailBeanAfterSync(vecCurrentSubAwardData,vecSubAwardPDFDetail);
            }
            subAwardBudgetDetailTableModel.setData(vecSubAwardPDFDetail);
            subAwardBudgetDetailTableModel.fireTableDataChanged();
            syncSuccessful = true;
        }else{
            CoeusOptionPane.showWarningDialog((String)responderBean.getMessage());
        }
        return syncSuccessful;
    }
    
    /**
     * Method to update the bean after the sync, will iterate the xml period details a
     * update the actype and the directCost, Indirect Cost and costsharing from the XML period details to the sub award details
     * @param vecCurrentSubAwardData 
     * @param vecSubAwardPDFDetail 
     * @return vecSubAwardPDFDetail
     */
    private Vector updSubAwdDetailBeanAfterSync(Vector vecCurrentSubAwardData, Vector vecSubAwardPDFDetail){
        for(int subAwardPDFDetailIndex=0;subAwardPDFDetailIndex<vecSubAwardPDFDetail.size();subAwardPDFDetailIndex++){
            BudgetSubAwardDetailBean subAwardPdfDetail = (BudgetSubAwardDetailBean)vecSubAwardPDFDetail.get(subAwardPDFDetailIndex);
            for(int subAwardDetailIndex=0;subAwardDetailIndex<vecCurrentSubAwardData.size();subAwardDetailIndex++){
                BudgetSubAwardDetailBean subAwardDetailBean = (BudgetSubAwardDetailBean)vecCurrentSubAwardData.get(subAwardDetailIndex);
                if(subAwardDetailBean.getBudgetPeriod() == subAwardPdfDetail.getBudgetPeriod()){
                    if(subAwardDetailBean.getAcType() == null){
                        subAwardDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
                    }else{
                        subAwardDetailBean.setAcType(subAwardDetailBean.getAcType());
                    }
                    subAwardDetailBean.setDirectCost(subAwardPdfDetail.getDirectCost());
                    subAwardDetailBean.setIndirectCost(subAwardPdfDetail.getIndirectCost());
                    subAwardDetailBean.setCostSharingAmount(subAwardPdfDetail.getCostSharingAmount());
                    subAwardDetailBean.setPeriodStartDate(subAwardPdfDetail.getPeriodStartDate());
                    subAwardDetailBean.setPeriodEndDate(subAwardPdfDetail.getPeriodEndDate());
                }
            }
        }
        return vecCurrentSubAwardData;
    }
    
}


