/*
 * AddAwardBasisController.java
 *
 * Created on November 19, 2004, 3:16 PM
 */
/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.admin.controller;

import edu.mit.coeus.admin.gui.AddAwardBasisForm;
import edu.mit.coeus.award.bean.ValidBasisMethodPaymentBean;
import edu.mit.coeus.award.bean.ValidBasisPaymentBean;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusMessageResources;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.MultipleTableColumnSorter;
import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import java.awt.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

/**
 *
 * @author  jinum
 */
public class AddAwardBasisController implements ActionListener, ListSelectionListener{
    
    /** Holds an instance of <CODE>AddAwardBasisForm</CODE> */
    private AddAwardBasisForm addAwardBasisForm;
    
    private CoeusAppletMDIForm mdiForm;
    private CoeusDlgWindow dlgAddAwardBasis;
    private CoeusMessageResources coeusMessageResources;
    private AddAwardBasisTableModel addAwardBasisTableModel;
    private ValidBasisMethodPaymentBean validBasisMethodPaymentBean;
    private ValidBasisPaymentBean validBasisPaymentBean;
    
    //setting up the width and height of the screen
    private static final int WIDTH = 570;
    private static final int HEIGHT = 265;
    
    //For the code and description column
    private static final int CODE_COLUMN = 0;
    private static final int DESCRIPTION_COLUMN = 1;
    private static final String EMPTY = "";
    private static final String TITLE = "Add Award Basis of Payment";
    
    //For the table data and for the selected data
    private CoeusVector cvTableData;
    private CoeusVector cvSelectedData;
    private MultipleTableColumnSorter sorter;
    
    /** Creates a new instance of AddAwardBasisController */
    public AddAwardBasisController(CoeusAppletMDIForm mdiForm, ValidBasisPaymentBean validBasisPaymentBean) {
        this.mdiForm = mdiForm;
        this.validBasisPaymentBean = validBasisPaymentBean;
        cvSelectedData = new CoeusVector();
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
        addAwardBasisForm = new AddAwardBasisForm();
        addAwardBasisForm.btnOK.addActionListener(this);
        addAwardBasisForm.btnCancel.addActionListener(this);
        
        addAwardBasisTableModel = new AddAwardBasisTableModel();
        addAwardBasisForm.tblBasis.setModel(addAwardBasisTableModel);
        addAwardBasisForm.tblBasis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addAwardBasisForm.tblBasis.getSelectionModel().addListSelectionListener(this);
        
    }
    
    /**
     * To set the components before opening the screen
     * @param title String
     * @return void
     */
    public void postInitComponents(){
        
        Component[] components = { addAwardBasisForm.btnOK,addAwardBasisForm.btnCancel};
        
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        addAwardBasisForm.setFocusTraversalPolicy(traversePolicy);
        addAwardBasisForm.setFocusCycleRoot(true);
        
        addAwardBasisForm.lblAwardTypeValue.setText( validBasisPaymentBean.getAwardTypeDescription());
        
        dlgAddAwardBasis = new CoeusDlgWindow(mdiForm);
        dlgAddAwardBasis.setResizable(false);
        dlgAddAwardBasis.setModal(true);
        dlgAddAwardBasis.getContentPane().add(addAwardBasisForm);
        // setting up the title and it is getting up from the basewindow
        dlgAddAwardBasis.setTitle(TITLE);
        dlgAddAwardBasis.setFont(CoeusFontFactory.getLabelFont());
        dlgAddAwardBasis.setSize(WIDTH, HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgAddAwardBasis.getSize();
        dlgAddAwardBasis.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgAddAwardBasis.addComponentListener(
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
        
        JTableHeader tableHeader = addAwardBasisForm.tblBasis.getTableHeader();
        
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
         if( sorter == null ) {
                sorter = new MultipleTableColumnSorter((TableModel)addAwardBasisForm.tblBasis.getModel());
                sorter.setTableHeader(addAwardBasisForm.tblBasis.getTableHeader());
                addAwardBasisForm.tblBasis.setModel(sorter);
                
        }
        
        addAwardBasisForm.tblBasis.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // setting up the table columns
        TableColumn column = addAwardBasisForm.tblBasis.getColumnModel().getColumn(CODE_COLUMN);
        column.setPreferredWidth(50);
        addAwardBasisForm.tblBasis.setRowHeight(22);
        
        column = addAwardBasisForm.tblBasis.getColumnModel().getColumn(DESCRIPTION_COLUMN);
        column.setPreferredWidth(428);
    }
    
    /**
     * Display the Dialog
     * @return void
     **/
    public CoeusVector display() {
        dlgAddAwardBasis.setVisible(true);
        return cvSelectedData;
    }
    
    /**
     * setting up the default focus
     * @return void
     */
    public void requestDefaultFocus(){
        addAwardBasisForm.btnCancel.requestFocus();
    }
    
    /** This method will specify the action performed
     * @param actionEvent ActionEvent
     * @return void
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source != null){
            if( source.equals(addAwardBasisForm.btnCancel)) {
                dlgAddAwardBasis.dispose();
            }else if( source.equals(addAwardBasisForm.btnOK) ){
                performUpdateOperation();
            }
        }
    }
    
    public void valueChanged(ListSelectionEvent e) {
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return addAwardBasisForm;
    }
    
    public Object getFormData() {
        return addAwardBasisForm;
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
            addAwardBasisTableModel.setData(cvTableData);
        }
    }
    
    public boolean validate() throws edu.mit.coeus.exception.CoeusUIException {
        return false;
    }
    /**
     * performing the update operation
     * @return void
     */
    private void performUpdateOperation(){
        int selectedRows[] = addAwardBasisForm.tblBasis.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0) {
            cvSelectedData = new CoeusVector();
            //iterate through each row and set the AwardBasis type
            for (int i = 0; i < selectedRows.length; i++) {
                validBasisMethodPaymentBean = (ValidBasisMethodPaymentBean) cvTableData.get(selectedRows[i]);
                if (validBasisMethodPaymentBean != null) {
                    cvSelectedData.add(validBasisMethodPaymentBean);
                }
                
            }
        }
        dlgAddAwardBasis.dispose();
    }
    
    
        /*
         *It's an inner class which specifies the table model
         */
    public class AddAwardBasisTableModel extends AbstractTableModel {
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
         * @param data CoeusVector
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
                        return new Integer(localValidBasisMethodPaymentBean.getBasisOfPaymentCode());
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
            {"0","basisOfPaymentCode" },
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
                    addAwardBasisTableModel.fireTableRowsUpdated(
                    0, addAwardBasisTableModel.getRowCount());
                }
            } catch(Exception exception) {
                //exception.printStackTrace();
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
}
