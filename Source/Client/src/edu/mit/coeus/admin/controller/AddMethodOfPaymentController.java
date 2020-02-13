/*
 * AddMethodOfPaymentController.java
 *
 * Created on November 25, 2004, 3:16 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddMethodOfPaymentForm;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import java.awt.event.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author  jinum
 */
public class AddMethodOfPaymentController implements ActionListener{ //, ListSelectionListener
    
    /** Holds an instance of <CODE>AddMethodOfPaymentForm</CODE> */
    private AddMethodOfPaymentForm addMethodOfPaymentForm;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgAddMethodOfPayment;
    private CoeusMessageResources coeusMessageResources;
    private AddMethodOfPaymentTableModel addMethodOfPaymentTableModel;
    private ValidBasisMethodPaymentBean validBasisPaymentBean;
    
    //setting up the width and height of the screen
    private static final int WIDTH = 585;
    private static final int HEIGHT = 335;
    
    //For the code and description column
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final String EMPTY = "";
    private static final String TITLE = "Add Valid Basis and Method of Payment";
    private final String BLANK = "B";
    private final String MANDATORY = "M";
    private final String OPTIONAL = "O";
    
    //For the table data and for the selected data
    private CoeusVector cvTableData;
    private CoeusVector cvSelectedData;
    private MultipleTableColumnSorter sorter;
    
    /** Creates a new instance of AddMethodOfPaymentController */
    public AddMethodOfPaymentController(CoeusAppletMDIForm mdiForm, ValidBasisMethodPaymentBean validBasisPaymentBean) {
        this.mdiForm = mdiForm;
        this.validBasisPaymentBean = validBasisPaymentBean;
        coeusMessageResources = CoeusMessageResources.getInstance();
        registerComponents();
        postInitComponents();
    }
    
    /**
     * registering the components
     * @return void
     */
    public void registerComponents() {
        //Add listeners to all the buttons
        addMethodOfPaymentForm = new AddMethodOfPaymentForm();
        addMethodOfPaymentForm.btnOK.addActionListener(this);
        addMethodOfPaymentForm.btnCancel.addActionListener(this);
        
        addMethodOfPaymentTableModel = new AddMethodOfPaymentTableModel();
        addMethodOfPaymentForm.tblMethodOfPayment.setModel(addMethodOfPaymentTableModel);
    }
    
    /**
     * To set the components before opening the screen
     * @return void
     */
    public void postInitComponents(){
        
        Component[] components = { addMethodOfPaymentForm.btnOK,addMethodOfPaymentForm.btnCancel,addMethodOfPaymentForm.pnlPIFrequency,addMethodOfPaymentForm.rbPIFrOptional,addMethodOfPaymentForm.rbPIFrMandatory,addMethodOfPaymentForm.rbPIFrBlank,addMethodOfPaymentForm.rbInstrOptional,addMethodOfPaymentForm.rbInstrMandatory,addMethodOfPaymentForm.rbInstrBlank};
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addMethodOfPaymentForm.setFocusTraversalPolicy(traversePolicy);
        addMethodOfPaymentForm.setFocusCycleRoot(true);
        
        addMethodOfPaymentForm.lblBasisOfPaymentValue.setText( validBasisPaymentBean.getDescription());
        
        dlgAddMethodOfPayment = new CoeusDlgWindow(mdiForm);
        dlgAddMethodOfPayment.setResizable(false);
        dlgAddMethodOfPayment.setModal(true);
        dlgAddMethodOfPayment.getContentPane().add(addMethodOfPaymentForm);
        // setting up the title and it is getting up from the basewindow
        dlgAddMethodOfPayment.setTitle(TITLE);
        dlgAddMethodOfPayment.setFont(CoeusFontFactory.getLabelFont());
        dlgAddMethodOfPayment.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddMethodOfPayment.getSize();
        dlgAddMethodOfPayment.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddMethodOfPayment.addComponentListener(
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
        
        JTableHeader tableHeader = addMethodOfPaymentForm.tblMethodOfPayment.getTableHeader();
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)addMethodOfPaymentForm.tblMethodOfPayment.getModel());
                sorter.setTableHeader(addMethodOfPaymentForm.tblMethodOfPayment.getTableHeader());
                addMethodOfPaymentForm.tblMethodOfPayment.setModel(sorter);
                
        }
        // setting up the table columns
        TableColumn column = addMethodOfPaymentForm.tblMethodOfPayment.getColumnModel().getColumn(CODE_COLUMN);
//        column.setMinWidth(50);
        column.setPreferredWidth(50);
        addMethodOfPaymentForm.tblMethodOfPayment.setRowHeight(22);
        
        column = addMethodOfPaymentForm.tblMethodOfPayment.getColumnModel().getColumn(DESCRIPTION_COLUMN);
//        column.setMinWidth(300);
        column.setPreferredWidth(422);
        addMethodOfPaymentForm.tblMethodOfPayment.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

    }
    
    /**
     * Display the Dialog
     * @return CoeusVector
     **/
    public CoeusVector display() {
        dlgAddMethodOfPayment.setVisible(true);
        return cvSelectedData;
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        addMethodOfPaymentForm.btnCancel.requestFocus();
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source != null){
            if( source.equals(addMethodOfPaymentForm.btnCancel)) {
                dlgAddMethodOfPayment.dispose();
            }else if( source.equals(addMethodOfPaymentForm.btnOK) ){
                performUpdateOperation();
            }
        }
    }
    
    /** Formatting the fields
     * @return void
     */
    public void formatFields() {
    }
    
    /** Getting the ControlledUI
     * @return java.awt.Component
     */
    public java.awt.Component getControlledUI() {
        return addMethodOfPaymentForm;
    }
    
    /** Getting the FormData
     * @return Object
     */
    public Object getFormData() {
        return addMethodOfPaymentForm;
    }
    
    /** Saving the FormData
     * @return void
     */
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
            addMethodOfPaymentTableModel.setData(cvTableData);
        }
    }
    
    /**
     * To set the form data
     * @return boolean
     **/
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    /**
     * performing the update operation
     * @return void
     */
    private void performUpdateOperation(){
        int selectedRows[] = addMethodOfPaymentForm.tblMethodOfPayment.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0) {
            cvSelectedData = new CoeusVector();
            ValidBasisMethodPaymentBean bean = null;
            //set the invInstructionsIndicator and frequencyIndicator of the MethodPaymentBean
            String invInstructionsIndicator = OPTIONAL;
            String frequencyIndicator = OPTIONAL;
            if (addMethodOfPaymentForm.rbInstrBlank.isSelected()) {
                invInstructionsIndicator = BLANK;
            }else if (addMethodOfPaymentForm.rbInstrMandatory.isSelected()) {
                invInstructionsIndicator = MANDATORY;
            }else if (addMethodOfPaymentForm.rbInstrOptional.isSelected()) {
                invInstructionsIndicator = OPTIONAL;
            }
            if (addMethodOfPaymentForm.rbPIFrBlank.isSelected()) {
                frequencyIndicator = BLANK;
            }else if (addMethodOfPaymentForm.rbPIFrMandatory.isSelected()) {
                frequencyIndicator = MANDATORY;
            }else if (addMethodOfPaymentForm.rbPIFrOptional.isSelected()) {
                frequencyIndicator = OPTIONAL;
            }
            //iterate through each row and set the MethodPaymentBean
            for (int i = 0; i < selectedRows.length; i++) {
                bean = (ValidBasisMethodPaymentBean) cvTableData.get(selectedRows[i]);
                if (bean != null) {
                    bean.setInvInstructionsIndicator(invInstructionsIndicator);
                    bean.setFrequencyIndicator(frequencyIndicator);
                    cvSelectedData.add(bean);
                }
                
            }
        }
        dlgAddMethodOfPayment.dispose();
    }
    
        /*
         *It's an inner class which specifies the table model
         */
    public class AddMethodOfPaymentTableModel extends AbstractTableModel {
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
            ValidBasisMethodPaymentBean localValidBasisMethodPaymentBean =
            (ValidBasisMethodPaymentBean)cvTableData.get(rowIndex);
            if (localValidBasisMethodPaymentBean != null) {
                switch(columnIndex) {
                    case CODE_COLUMN:
                        return localValidBasisMethodPaymentBean.getCode();
                    case DESCRIPTION_COLUMN:
                        return localValidBasisMethodPaymentBean.getDescription();
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
                    addMethodOfPaymentTableModel.fireTableRowsUpdated(
                    0, addMethodOfPaymentTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
}
