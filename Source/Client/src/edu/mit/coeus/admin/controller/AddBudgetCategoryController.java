/*
 * AddBudgetCategoryController.java
 *
 * Created on December 03, 2004, 5:16 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddBudgetCategoryForm;
import edu.mit.coeus.budget.bean.BudgetCategoryBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;
import edu.mit.coeus.utils.CoeusOptionPane;

import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 *
 * @author  jinum
 */
public class AddBudgetCategoryController implements ActionListener{
    
    /** Holds an instance of <CODE>AddBudgetCategoryForm</CODE> */
    private AddBudgetCategoryForm addBudgetCategoryForm;
    
    private CoeusDlgWindow dlgAddBudgetCategory;
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private AddBudgetCategoryTableModel addBudgetCategoryTableModel;
    private BudgetCategoryBean budgetCategoryBean;
    
    //setting up the width and height of the screen
    private static final int WIDTH = 470;
    private static final int HEIGHT = 245;
    
    //For the code and description column
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final String EMPTY = "";
    private static final String TITLE = "Select Budget Category";
    
    //For the table data and for the selected data
    private CoeusVector cvTableData;
    
    /** Creates a new instance of AddBudgetCategoryController */
    public AddBudgetCategoryController(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        setColumnData();
        postInitComponents();
    }
    
    /**
     * registering the components
     * @return void
     */
    public void registerComponents() {
        
        //Add listeners to all the buttons
        addBudgetCategoryForm = new AddBudgetCategoryForm();
        addBudgetCategoryForm.btnOK.addActionListener(this);
        addBudgetCategoryForm.btnCancel.addActionListener(this);
        
        addBudgetCategoryTableModel = new AddBudgetCategoryTableModel();
        addBudgetCategoryForm.tblBudgetCategory.setModel(addBudgetCategoryTableModel);
        addBudgetCategoryForm.tblBudgetCategory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    }
    
    /**
     * To set the components before opening the screen
     * @param title String
     * @return void
     */
    public void postInitComponents(){
        
        Component[] components = { addBudgetCategoryForm.btnOK,addBudgetCategoryForm.btnCancel};
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addBudgetCategoryForm.setFocusTraversalPolicy(traversePolicy);
        addBudgetCategoryForm.setFocusCycleRoot(true);
        
        dlgAddBudgetCategory = new CoeusDlgWindow(mdiForm);
        dlgAddBudgetCategory.setResizable(false);
        dlgAddBudgetCategory.setModal(true);
        dlgAddBudgetCategory.getContentPane().add(addBudgetCategoryForm);
        // setting up the title and it is getting up from the basewindow
        dlgAddBudgetCategory.setTitle(TITLE);
        dlgAddBudgetCategory.setFont(CoeusFontFactory.getLabelFont());
        dlgAddBudgetCategory.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddBudgetCategory.getSize();
        dlgAddBudgetCategory.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddBudgetCategory.addComponentListener(
        new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                requestDefaultFocus();
            }
        });
    }
    
    /**
     * Setting up the column data
     * @return void
     **/
    public void setColumnData(){
        
        JTableHeader tableHeader = addBudgetCategoryForm.tblBudgetCategory.getTableHeader();
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.addMouseListener(new ColumnHeaderListener());
        // setting up the table columns
        TableColumn column = addBudgetCategoryForm.tblBudgetCategory.getColumnModel().getColumn(CODE_COLUMN);
//        column.setMinWidth(50);
        column.setPreferredWidth(50);
        addBudgetCategoryForm.tblBudgetCategory.setRowHeight(22);
        
        column = addBudgetCategoryForm.tblBudgetCategory.getColumnModel().getColumn(DESCRIPTION_COLUMN);
//        column.setMinWidth(300);
        column.setPreferredWidth(350);
        addBudgetCategoryForm.tblBudgetCategory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
    }
    
    /**
     * Display the Dialog
     * @return void
     **/
    public BudgetCategoryBean display() {
        dlgAddBudgetCategory.setVisible(true);
        return this.budgetCategoryBean;
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        addBudgetCategoryForm.btnCancel.requestFocus();
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source != null){
            if( source.equals(addBudgetCategoryForm.btnCancel)) {
                dlgAddBudgetCategory.dispose();
            }else if( source.equals(addBudgetCategoryForm.btnOK) ){
                performOKOperation();
            }
        }
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return addBudgetCategoryForm;
    }
    
    public Object getFormData() {
        return addBudgetCategoryForm;
    }
    
    public void saveFormData() throws edu.mit.coeus.exception.CoeusException {
    }
    
    /**
     * To set the form data
     * @param data Object
     * @return void
     **/
    public void setFormData(Object data) throws edu.mit.coeus.exception.CoeusException {
        cvTableData = new CoeusVector();
        if(data != null){
            cvTableData = (CoeusVector)data;
            addBudgetCategoryTableModel.setData(cvTableData);
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    /**
     * performing the update operation
     * @return void
     */
    private void performOKOperation(){
        int selectedIndex = addBudgetCategoryForm.tblBudgetCategory.getSelectedRow();
        if(selectedIndex != -1){
            this.budgetCategoryBean = (BudgetCategoryBean) cvTableData.get(selectedIndex);
            dlgAddBudgetCategory.dispose();
        }else{
            CoeusOptionPane.showInfoDialog(
            coeusMessageResources.parseMessageKey("search_exceptionCode.1119"));
        }
    }
    
    
        /*
         *It's an inner class which specifies the table model
         */
    public class AddBudgetCategoryTableModel extends AbstractTableModel {
        String columnName[] = {"Code","Description"};
        
        // represents the column class of the fields of table
        private Class colClass[] = {String.class, String.class};
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col) {
            return colClass[col];
        }
        
        /**
         * To get the column count
         * @return int
         **/
        public int getColumnCount() {
            return columnName.length;
        }
        /**
         * To get the column count
         * @param col int
         * @return String
         **/
        public String getColumnName(int col) {
            return columnName[col];
        }
        /**
         * To get the row count
         * @return int
         **/
        public int getRowCount() {
            if (cvTableData == null){
                return 0;
            } else {
                return cvTableData.size();
            }
        }
        /**
         * To set the value in the table
         * @param value Object
         * @param row int
         * @param col int
         * @return void
         **/
        public void setValueAt(Object value, int row, int col) {
            //have to set value in bean
        }
        /**
         * To set the  data in the table
         * @param cvTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector data) {
            cvTableData = data;
            fireTableDataChanged();
        }
        
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int rowIndex, int columnIndex) {
            //have to change to the value from bean
            BudgetCategoryBean localBudgetCategoryBean =
            (BudgetCategoryBean)cvTableData.get(rowIndex);
            if (localBudgetCategoryBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return localBudgetCategoryBean.getCode();
                    case DESCRIPTION_COLUMN:
                        return localBudgetCategoryBean.getDescription();
                }
            }
            return EMPTY;
        }
    }
    
    /** This class will sort the column values in ascending and descending order
     * based on number of clicks.
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","code" },
            {"1","description" }
            
        };
        boolean sort =true;
        /** Mouse click handler for the table headers to sort upon the headers
         * @param evt mouse event
         */
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                // int mColIndex = table.convertColumnIndexToModel(vColIndex);
                if(cvTableData != null && cvTableData.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvTableData).sort(nameBeanId [vColIndex][1],sort);
                    if (sort) {
                        sort = false;
                    }
                    else {
                        sort = true;
                    }
                    addBudgetCategoryTableModel.fireTableRowsUpdated(
                    0, addBudgetCategoryTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
}
