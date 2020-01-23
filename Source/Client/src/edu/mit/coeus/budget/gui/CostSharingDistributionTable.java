/*
 * CostSharingDistributionTable.java
 *
 * Created on October 29, 2003, 3:21 PM
 */

package edu.mit.coeus.budget.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.DollarCurrencyTextField;
import edu.mit.coeus.budget.bean.ProposalCostSharingBean;
import edu.mit.coeus.utils.CurrencyField;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.JTextFieldFilter;
import edu.mit.coeus.budget.controller.CostSharingDistributionController;
import edu.mit.coeus.utils.CoeusGuiConstants;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */
public class CostSharingDistributionTable extends JTable implements TypeConstants {
    
    //=== Table Column index value
    /** String Constants for Table Column */
    private final int PERCENTAGE_COL_INDEX = 0;
    /** String Constants for Table Column */
    private final int FISCALYEAR_COL_INDEX = 1;
    /** String Constants for Table Column */
    private final int AMOUNT_COL_INDEX = 2;
    /** String Constants for Table Column */
    private final int ACCOUNT_COL_INDEX = 3;
    
    /** Constants indicating EMPTY String */
    private static final String EMPTY_STRING = "";
    
    /** Returns the Model of the Table */
    public CostSharingTableModel costSharingTableModel;
    
    /** TableCell Renderer  instance */
    private CostSharingTableCellRenderer costSharingTableCellRenderer;
    
    /** table cell editor instance */
    private CostSharingTableCellEditor costSharingTableCellEditor;
    
    /** CostSharingDistributionController instance */
    public CostSharingDistributionController costSharingDistributionController ;
    
    /** ProposalCostSharingBean instance */
    private ProposalCostSharingBean proposalCostSharingBean ;
    
    /** Vector of ProposalCostSharingBean */
    private Vector vecProposalCostSharingBean;
    
    /** Vector of Beans to render to the table */
    private Vector tableVector;
    
    /** Variable for SELECTED TABLE ROW COLOR */
    private final java.awt.Color SELECTEDTABLEROWCOLOR = Color.YELLOW; //((java.awt.Color) javax.swing.UIManager.getDefaults().get("Table.selectionBackground"));
    
    /** boolean flag for checking is Table Data Modified */
    boolean isTableDataModified = false;
    private int accountNumberMaxLength = 0;
    /** Creates new form CostSharingDistributionTable */
    public CostSharingDistributionTable() {
        
        setFont(CoeusFontFactory.getNormalFont());
        
        costSharingTableModel = new CostSharingTableModel();
        setModel(costSharingTableModel);
        CostSharingTableHeaderRenderer tableHeaderRenderer = new CostSharingTableHeaderRenderer();
        costSharingTableCellRenderer = new CostSharingTableCellRenderer();
        costSharingTableCellEditor= new CostSharingTableCellEditor();
        getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        setRowHeight(23);
        
        TableColumn tableColumn = null;
        //setting Table Column
        // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
        //int colSize[] = {100, 100, 150, 150};
        int colSize[] = {100, 150, 120, 150};
        // modified for COEUSQA-1426: Ability to enter data besides YYYY  end
        
        //setting the width Render and Editor for each Column
        
        for(int col = 0; col < colSize.length; col++) {
            tableColumn = this.getColumnModel().getColumn(col);
            tableColumn.setPreferredWidth(colSize[col]);
            tableColumn.setCellRenderer(costSharingTableCellRenderer);
            tableColumn.setCellEditor(costSharingTableCellEditor);
            tableColumn.setHeaderRenderer(tableHeaderRenderer);
            
        }
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setResizingAllowed(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
    }
    
    /** Updates the selection models of the table, depending on the state of the two flags
     * @param row int
     * @param column int
     * @param toggle boolean
     * @param extend boolean
     */
    public void changeSelection(int row, int column, boolean toggle, boolean extend){
        try {
            super.changeSelection(row, column, toggle, extend);
            this.editCellAt(row, column);
            
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    getTable().dispatchEvent(new KeyEvent(
                    getTable(),KeyEvent.KEY_PRESSED,0,0,KeyEvent.VK_F2,
                    KeyEvent.CHAR_UNDEFINED) );
                    
                    
                }
            });
            
            
            
        } catch(Exception e) {
            e.getMessage();
        }
    }
    
    
    /** Method to Set the Vector of ProposalCostSharingBean to the Table
     * @param data Vector of ProposalCostSharingBean
     */
    public void setTableData(Object data) {
        
        tableVector  = (Vector) data;
        costSharingTableModel.setData(tableVector);
        costSharingTableModel.fireTableDataChanged();
        if(costSharingDistributionController.getFunctionType() != DISPLAY_MODE) {
            this.setRowSelectionInterval(0,0);
            this.editCellAt(0,0);
        }
    }
    
    /** Method to return the TableCellEditor of this Table Component
     *@return CostSharingTableCellEditor of CostSharingDistributionTable
     */
    
    public CostSharingTableCellEditor getTableCellEditor() {
        return costSharingTableCellEditor;
    }
    
    /** Method to return the TableModel of this Table Component
     *@return CostSharingTableModel of CostSharingDistributionTable
     */
    
    public CostSharingTableModel getTableModel() {
        return costSharingTableModel;
    }
    
    /** get the current table instance
     * @return CostSharingDistributionTable instance
     */
    public CostSharingDistributionTable getTable() {
        return this;
    }
    
    /** returns whether this Table Date being Modified
     * @return boolean if <CODE>true</CODE> Table Date is Modified
     */
    public boolean isTableDateModified() {
        return isTableDataModified;
    }

    /** sets the variable indicating the Table being Modified to false
     * @return boolean if <CODE>false</CODE> reset the Modified variable
     */
    public void setIsTableDateModified(boolean isTableDataModified) {
        this.isTableDataModified = isTableDataModified;
    }

    
    /** Method to pass the Controller handle to the Table to access Controller Methods
     * @param costSharingDistributionController CostSharingDistributionController instance
     */
    
    public void setControllerInstance(CostSharingDistributionController costSharingDistributionController) {
        this.costSharingDistributionController = costSharingDistributionController;
    }
    
    //Inner Class Table Model - Start
    /** CostSharingTableModel Model */
    public class CostSharingTableModel extends AbstractTableModel{
        
        /** String Array forColumn Types */
        private Class columnTypes [] = {Double.class,Integer.class,String.class,Integer.class};
        /** String array for Column Names */
        
        // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
        //private String columnNames [] = {" Percentage ", " Fiscal Year ", " Amount", " Source Account"};
        // JM 5-25-2011 changed label from account to center
        private String columnNames [] = {" Percentage ", " Project Year ", " Amount", " Source Center"};
        // modified for COEUSQA-1426: Ability to enter data besides YYYY  start
        
        /** String array of Column editable */
        private boolean columnEditables [] ={true,true,true,true};
        
        /** Holds Vector of beans for the table */
        private Vector dataBean;
        
        /** Default Constructor for the CostSharingTableModel
         */
        
        CostSharingTableModel() {
            
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
            return columnEditables[columnIndex];
        }
        
        /** Set  the Vector of beans
         * @param dataBean Vector of beans
         */
        public void setData(Vector dataBean) {
            this.dataBean = dataBean;
            
            vecProposalCostSharingBean = new Vector();
            vecProposalCostSharingBean = (Vector) dataBean.clone();
            
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
            
            proposalCostSharingBean = (ProposalCostSharingBean) dataBean.get(rowIndex);
            if(proposalCostSharingBean != null) {
                
                switch(columnIndex) {
                    case PERCENTAGE_COL_INDEX :
                        return proposalCostSharingBean.getCostSharingPercentage()+"";
                    case FISCALYEAR_COL_INDEX :
                        if(proposalCostSharingBean.getFiscalYear() ==null ||
                        proposalCostSharingBean.getFiscalYear().equals(EMPTY_STRING)){
                            return EMPTY_STRING;
                        } else
                            return proposalCostSharingBean.getFiscalYear();
                        
                    case AMOUNT_COL_INDEX :
                        return proposalCostSharingBean.getAmount()+"";
                    case ACCOUNT_COL_INDEX :
                        if(proposalCostSharingBean.getSourceAccount() ==null ||
                        proposalCostSharingBean.getSourceAccount().equals(EMPTY_STRING)){
                            return EMPTY_STRING;
                        } else
                            return proposalCostSharingBean.getSourceAccount();
                        
                }
            }
            return null;
            
        }
        
        
        /** set Object at rowIndex columnIndex of table
         * @param value Object to set
         * @param rowIndex int rowIndex
         * @param columnIndex int columnIndex
         */
        public void setValueAt(Object value, int rowIndex,int columnIndex) {
            
            try {
                
                proposalCostSharingBean = (ProposalCostSharingBean) dataBean.get(rowIndex);
                if(proposalCostSharingBean != null) {
                    //isTableDataModified = false;
                    switch(columnIndex) {
                        case PERCENTAGE_COL_INDEX :
                            double oldValue = proposalCostSharingBean.getCostSharingPercentage();
                            if(oldValue != Double.parseDouble(value.toString())) {
                                isTableDataModified = true;
                                if(proposalCostSharingBean.getAcType() == null) {
                                    proposalCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                
                            }
                            proposalCostSharingBean.setCostSharingPercentage(Double.parseDouble(value.toString()));
                            break;
                        case FISCALYEAR_COL_INDEX :
                            String oldDateValue = proposalCostSharingBean.getFiscalYear();
                            if(!oldDateValue.equals(value.toString().trim())) {
                                isTableDataModified = true;
                                if(proposalCostSharingBean.getAcType() == null) {
                                    proposalCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                            }
                            proposalCostSharingBean.setFiscalYear(value.toString());
                            break;
                        case AMOUNT_COL_INDEX :
                            double oldAmount = proposalCostSharingBean.getAmount();
                            if(oldAmount  != Double.parseDouble(value.toString())) {
                                isTableDataModified = true;
                                if(proposalCostSharingBean.getAcType() == null) {
                                    proposalCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                            }
                            proposalCostSharingBean.setAmount(Double.parseDouble(value.toString()));
                            break;
                        case ACCOUNT_COL_INDEX :
                            String oldAccountValue = proposalCostSharingBean.getSourceAccount();
                            if(!oldAccountValue.equals(value.toString().trim())) {
                                isTableDataModified = true;
                                if(proposalCostSharingBean.getAcType() == null) {
                                    proposalCostSharingBean.setAcType(TypeConstants.UPDATE_RECORD);
                                }
                                
                            }
                            proposalCostSharingBean.setSourceAccount(value.toString());
                            break;
                            
                    }
                    costSharingTableModel.fireTableRowsUpdated(rowIndex,columnIndex);
                    
                }
                
            } catch(Exception e) {
                costSharingTableModel.fireTableRowsUpdated(rowIndex,columnIndex);
                e.printStackTrace();
            }
            
        }
        
        
    }//End of Class
    
    //CostSharingTableCellRenderer =============
    /** TableHeader Renderer instance */
    class CostSharingTableHeaderRenderer extends DefaultTableCellRenderer {
        
        /** Label instance */
        private JLabel label;
        /** CostSharingTableHeaderRenderer */
        CostSharingTableHeaderRenderer(){
            label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(CoeusFontFactory.getLabelFont());
            label.setBorder(BorderFactory.createRaisedBevelBorder());
        }
        
        /** return TableCellRendererComponent
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean
         * @param hasFocus boolean
         * @param row int
         * @param column int
         * @return Component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            label.setText(value.toString());
            return label;
        }
        
    }
    
    //CostSharingTableCellRenderer =============
    
    /** CostSharingTableCellRenderer */
    class CostSharingTableCellRenderer extends DefaultTableCellRenderer {
        //implements FocusListener {
        
        /** DollarCurrencyTextfield for Amount */
        private DollarCurrencyTextField txtAmount;
        /** JTextField Component */
        private JTextField txtComponent;
        /** CurrencyField Component */
        private CurrencyField txtPercentage;
        //Added for show grey color in display mode by tarique start 1
        /**Label which grey color in display mode */
        private JLabel lblComponent;
        //Added for show grey color in display mode by tarique end 1
                
        /** constructor CostSharingTableCellRenderer */
        CostSharingTableCellRenderer() {
            txtPercentage =  new CurrencyField();
            txtComponent = new JTextField();
            txtAmount = new DollarCurrencyTextField();
            //Added for show grey color in display mode by tarique start 2
            lblComponent = new JLabel();
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            lblComponent.setOpaque(true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            //Added for show grey color in display mode by tarique end 2
            txtAmount.setBorder(new EmptyBorder(0,0,0,0));
            txtPercentage.setBorder(new EmptyBorder(0,0,0,0));
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            
        }
        
        /** return TableCellRendererComponent
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean
         * @param hasFocus boolean
         * @param row int
         * @param column int
         * @return Component
         */
        public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setHorizontalAlignment(JLabel.LEFT);
            //Added for show grey color in display mode by tarique start 3
            lblComponent.setHorizontalAlignment(JLabel.LEFT);
            //Added for show grey color in display mode by tarique end 3
            txtAmount.setBorder(new EmptyBorder(0,0,0,0));
            txtPercentage.setBorder(new EmptyBorder(0,0,0,0));
            txtComponent.setBorder(new EmptyBorder(0,0,0,0));
            
            if((costSharingDistributionController.getFunctionType() == DISPLAY_MODE)) {
                Color tableBackGroundColor = (java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background");
//                txtComponent.setBackground(tableBackGroundColor);
//                txtPercentage.setBackground(tableBackGroundColor);
//                txtAmount.setBackground(tableBackGroundColor);
                //Added for show grey color in display mode by tarique start 4
                lblComponent.setBackground(tableBackGroundColor);
                //Added for show grey color in display mode by tarique end 4
            } else {
                
                if(isSelected ) {
                    //Added for show grey color in display mode by tarique start 5
                    lblComponent.setBackground(SELECTEDTABLEROWCOLOR);
                    //Added for show grey color in display mode by tarique end 5
                    txtComponent.setBackground(SELECTEDTABLEROWCOLOR );
                    txtPercentage.setBackground(SELECTEDTABLEROWCOLOR );
                    txtAmount.setBackground(SELECTEDTABLEROWCOLOR );
                    
                } else {
                    //Added for show grey color in display mode by tarique start 6
                    lblComponent.setBackground(Color.WHITE);
                    //Added for show grey color in display mode by tarique end 6
                    txtComponent.setBackground(Color.WHITE);
                    txtPercentage.setBackground(Color.WHITE);
                    txtAmount.setBackground(Color.WHITE);
                    
                }
                
            }
            
            switch (column) {
                
                case PERCENTAGE_COL_INDEX :
                    txtPercentage.setText(value.toString());
                    lblComponent.setText(txtPercentage.getText());
                    //Added for show grey color in display mode by tarique start 7
                    lblComponent.setHorizontalAlignment(JLabel.RIGHT);
                    return lblComponent;
                    //Added for show grey color in display mode by tarique end 8
                    //return txtPercentage;
                    
                case FISCALYEAR_COL_INDEX :
                    txtComponent.setText(value.toString());
                    //Added for show grey color in display mode by tarique start 9
                    lblComponent.setText(txtComponent.getText());
                    return lblComponent;
                    //Added for show grey color in display mode by tarique end 9
                   // return txtComponent;
                    
                case AMOUNT_COL_INDEX :
                    txtAmount.setText(value.toString());
                    //Added for show grey color in display mode by tarique start 10
                    lblComponent.setText(txtAmount.getText()); 
                    lblComponent.setHorizontalAlignment(JLabel.RIGHT);
                    return lblComponent;
                    //Added for show grey color in display mode by tarique end 10
                   // return txtAmount;
                    
                case ACCOUNT_COL_INDEX :
                    //Case#2402- use a parameter to set the length of the account number throughout app
//                    txtComponent.setText(value.toString());
//                    lblComponent.setText(txtComponent.getText());
                    String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                    if(costSharingDistributionController.getFunctionType() != DISPLAY_MODE 
                            && sourceAccountNumber.length() > accountNumberMaxLength){
                        sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                    }
                    txtComponent.setText(sourceAccountNumber);
                    //Case#2402 - End
                    //Added for show grey color in display mode by tarique start 11
                    lblComponent.setText(txtComponent.getText());
                    return lblComponent;
                    //Added for show grey color in display mode by tarique end 11
                   // return txtComponent;
                    
                    
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            
        }
        
        
    }//End Class - CostSharingTableCellRenderer ----------------------------------------
    
    //CostSharingTableCellEditor--------------------------------------------------------
    /** Instance to TableCellEditor */
    public class CostSharingTableCellEditor extends AbstractCellEditor   implements TableCellEditor{
        
        /** CurrencyField Component */
        private CurrencyField txtPercentage;
        /** TextField Component */
        private JTextField txtFiscalYear;
        /** DollarCurrencyTextfield for Amount */
        private DollarCurrencyTextField txtAmount;
        /** TextField Component */
        private JTextField txtSourceAccount;
        
        /** Column Index */
        private int columnIndex;
        
        /** constructor CostSharingTableCellEditor */
        CostSharingTableCellEditor() {
            
            txtFiscalYear = new JTextField();
            txtSourceAccount = new JTextField();
            txtPercentage = new CurrencyField();
            txtAmount = new DollarCurrencyTextField();
            
            txtAmount.setBorder(new EmptyBorder(0,0,0,0));
            txtPercentage.setBorder(new EmptyBorder(0,0,0,0));
            txtFiscalYear.setBorder(new EmptyBorder(0,0,0,0));
            txtSourceAccount.setBorder(new EmptyBorder(0,0,0,0));
            txtFiscalYear.setBackground(SELECTEDTABLEROWCOLOR );
            txtSourceAccount.setBackground(SELECTEDTABLEROWCOLOR );
            txtPercentage.setBackground(SELECTEDTABLEROWCOLOR );
            txtAmount.setBackground(SELECTEDTABLEROWCOLOR );
            
            txtFiscalYear.setDocument(new JTextFieldFilter(JTextFieldFilter.NUMERIC, 4));
            //Modified by shiji for bug fix id 1730 - start
            //txtSourceAccount.setDocument(new JTextFieldFilter(JTextFieldFilter.ALPHA_NUMERIC, 7));
            //Case#2402- use a parameter to set the length of the account number throughout app
            txtSourceAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
            //Case#2402 - End
            //bug fix id 1730 - end
        }
        
        /** To get The editable component
         * @return Component
         * @param row int
         * @param column int
         * @param table Table instance
         * @param value Object
         * @param isSelected boolean true if selected
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.columnIndex = column;
            
            switch (columnIndex) {
                
                case PERCENTAGE_COL_INDEX :
                    
                    txtPercentage.setText(value.toString());
                    return txtPercentage;
                    
                case FISCALYEAR_COL_INDEX :
                    txtFiscalYear.setText(value.toString());
                    return txtFiscalYear;
                    
                case AMOUNT_COL_INDEX :
                    txtAmount.setText(value.toString());
                    return txtAmount;
                    
                case ACCOUNT_COL_INDEX :
                    //Case#2402- use a parameter to set the length of the account number throughout app
                    //txtSourceAccount.setText(value.toString());
                    txtSourceAccount.setDocument(new JTextFieldFilter((JTextFieldFilter.ALPHA_NUMERIC+JTextFieldFilter.COMMA_HYPHEN_PERIOD),accountNumberMaxLength));
                    String sourceAccountNumber = value == null ? CoeusGuiConstants.EMPTY_STRING : value.toString().trim();
                    if(sourceAccountNumber.length() > accountNumberMaxLength){
                        sourceAccountNumber = sourceAccountNumber.substring(0,accountNumberMaxLength);
                    }
                    //Case#2402 - End
                    txtSourceAccount.setText(sourceAccountNumber);
                    return txtSourceAccount;
            }
            return txtAmount;
            // return super.getTableCellEditorComponent(table, value, isSelected, rowIndex, columnIndex);
        }
        
        /** get Cell Editor Value
         * @return Object
         */
        public Object getCellEditorValue() {
            switch (columnIndex) {
                
                case PERCENTAGE_COL_INDEX :
                    return txtPercentage.getText();
                    
                case FISCALYEAR_COL_INDEX :
                    return txtFiscalYear.getText();
                    
                case AMOUNT_COL_INDEX :
                    return txtAmount.getValue();
                case ACCOUNT_COL_INDEX :
                    return txtSourceAccount.getText();
            }
            return txtSourceAccount.getText();
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
    //End Class CostSharingTableCellEditor----------------------------------------------
    
    //Case#2402- use a parameter to set the length of the account number throughout app
    /**
     * Method to intialize account number field size
     */
    public void initAccountNumberMaxLength(int accountNumberMaxLength){
        this.accountNumberMaxLength = accountNumberMaxLength;
    }
    //Case#2402 - End
    

}//end of class definition