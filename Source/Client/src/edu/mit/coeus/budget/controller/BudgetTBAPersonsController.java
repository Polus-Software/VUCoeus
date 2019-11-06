/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.controller;

import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.budget.bean.BudgetTBAPersonBean;
import edu.mit.coeus.budget.gui.BudgetTBAPersonsForm;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.exception.CoeusUIException;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.AppletServletCommunicator;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.CoeusVector;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * @author  noorula
 * Created on May 9, 2007, 5:15 PM
 */
public class BudgetTBAPersonsController extends Controller implements ActionListener{
    
    private BudgetTBAPersonsForm budgetTBAPersonsForm;
    private CoeusDlgWindow dlgTBAPerson;
    private CoeusAppletMDIForm mdiForm;
    private BudgetTBAPersonTableModel budgetTBAPersonTableModel;
    private static final int WIDTH = 490;
    private static final int HEIGHT =300;
    private CoeusVector cvDataBean;
    private BudgetTBAPersonBean budgetTBAPersonBean;
    private boolean okClicked;
    
    /** Creates a new instance of BudgetTBAPersonsController */
    public BudgetTBAPersonsController(CoeusAppletMDIForm mdiForm) {
        this.mdiForm = mdiForm;
        budgetTBAPersonsForm = new BudgetTBAPersonsForm();
        budgetTBAPersonTableModel = new BudgetTBAPersonTableModel();
    }
    
    /** Specifies the Modal window */
    private void postInitComponents() {
        dlgTBAPerson = new CoeusDlgWindow(mdiForm);
        dlgTBAPerson.getContentPane().add(budgetTBAPersonsForm);
        dlgTBAPerson.setTitle("TBA Persons");
        dlgTBAPerson.setFont(CoeusFontFactory.getLabelFont());
        dlgTBAPerson.setModal(true);
        dlgTBAPerson.setResizable(false);
        dlgTBAPerson.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgTBAPerson.getSize();
        dlgTBAPerson.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgTBAPerson.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                cancelPersonsAction();
                return;
            }
        });
        dlgTBAPerson.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgTBAPerson.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                cancelPersonsAction();
                return;
            }
        });
    }
    
    
    /** Specifies the cancel action, the validations are as followed earlier */
    private void cancelPersonsAction(){
        dlgTBAPerson.dispose();
    }
    
    private class BudgetTBAPersonTableModel extends AbstractTableModel implements TableModel{

        private String colNames[] = {"TBA Id","Person Name","Job Code"};
        private Class colClass[] = {String.class, String.class, String.class};
        
        BudgetTBAPersonTableModel(){
        }
        
        /**
         * This method tells the cell is editable or not
         * @param row
         * @param column
         * @return boolean
         */        
        public boolean isCellEditable(int row,int column){
            return false;
        }

        /**
         * It gives the number of columns in the table
         * @return int
         */        
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         *
         * @param data
         */        
        public void setData(CoeusVector data){
            cvDataBean = data;
        }
        
        /**
         *
         * @return
         */        
        public int getRowCount() {
            if(cvDataBean==null)return 0;
            return cvDataBean.size();
        }
        
        /** gets the persons information with the specified index
         * @param index
         * @return BudgetTBAPersonBean
         */
        public BudgetTBAPersonBean getRow(int index) {
            return (BudgetTBAPersonBean)cvDataBean.get(index);
        }
        
        /**
         * to get the column name
         * @param column
         * @return
         */        
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /**
         * To set the values to the cells for the table
         * @param row
         * @param column
         * @return Object
         */        
        public Object getValueAt(int row, int column){
            BudgetTBAPersonBean budgetTBAPersonBean = (BudgetTBAPersonBean)cvDataBean.get(row);
            switch(column){
                case TBA_ID:
                    return budgetTBAPersonBean.getTbaId();
                case NAME:
                    return budgetTBAPersonBean.getName();
                case JOB_CODE:
                    return budgetTBAPersonBean.getJobCode();
            }
            return null;
        }
        
    }
    
    public void display(){
        postInitComponents();
        registerComponents();
        setFormData(null);
        setTableEditors();
        if(budgetTBAPersonsForm.tblTBAPersons.getRowCount() >0){
            budgetTBAPersonsForm.tblTBAPersons.setRowSelectionInterval(0,0);
        }
        dlgTBAPerson.setVisible(true);
    }
    
    public void formatFields() {
    }
    
    public java.awt.Component getControlledUI() {
        return budgetTBAPersonsForm;
    }
    
    public Object getFormData() {
        return budgetTBAPersonsForm;
    }
    
    public void registerComponents() {
        budgetTBAPersonsForm.btnOK.addActionListener(this);
        budgetTBAPersonsForm.btnCancel.addActionListener(this);
    }
    
    public void saveFormData() {
    }
    
    /**
     * To set the persons data fr the table
     * @param data
     */    
    public void setFormData(Object data){
        budgetTBAPersonsForm.tblTBAPersons.setModel(budgetTBAPersonTableModel);
        CoeusVector vecData = getTBAPersons();
        budgetTBAPersonTableModel.setData(vecData);         
    }
    
    public boolean validate() throws CoeusUIException {
        return true;
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == budgetTBAPersonsForm.btnOK){
            int selectedRow = budgetTBAPersonsForm.tblTBAPersons.getSelectedRow();
            if(selectedRow!=-1){
                setSelectedTBAPersons(cvDataBean.get(selectedRow));
                setOkClicked(true);
                cancelPersonsAction();
            } else {
                cancelPersonsAction();
            }
        } else if (e.getSource() == budgetTBAPersonsForm.btnCancel){
            cancelPersonsAction();
        }
    }
    
    /** Sets the table header and column header. Specifies the size of the column*/
    private void setTableEditors(){
        try{
            JTableHeader tableHeader = budgetTBAPersonsForm.tblTBAPersons.getTableHeader();
            tableHeader.setReorderingAllowed(false);
            tableHeader.addMouseListener(new ColumnHeaderListener());
            tableHeader.setMaximumSize(new Dimension(50,27));
            tableHeader.setMinimumSize(new Dimension(50,27));
            tableHeader.setPreferredSize(new Dimension(50,27));
            tableHeader.setFont(CoeusFontFactory.getLabelFont());
            
            budgetTBAPersonsForm.tblTBAPersons.setRowHeight(22);
            budgetTBAPersonsForm.tblTBAPersons.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            budgetTBAPersonsForm.tblTBAPersons.setShowHorizontalLines(true);
            budgetTBAPersonsForm.tblTBAPersons.setShowVerticalLines(true);
            budgetTBAPersonsForm.tblTBAPersons.setOpaque(false);
            budgetTBAPersonsForm.tblTBAPersons.setSelectionMode(
            DefaultListSelectionModel.SINGLE_SELECTION);
            budgetTBAPersonsForm.tblTBAPersons.setRowSelectionAllowed(true);
            
            TableColumn column = budgetTBAPersonsForm.tblTBAPersons.getColumnModel().getColumn(0);
            column.setPreferredWidth(55);
            column.setResizable(true);
            
            column = budgetTBAPersonsForm.tblTBAPersons.getColumnModel().getColumn(1);
            column.setPreferredWidth(230);
            column.setResizable(true);
            
            column = budgetTBAPersonsForm.tblTBAPersons.getColumnModel().getColumn(2);
            column.setPreferredWidth(100);
            column.setResizable(true);
        } catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }    
    
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","tbaId" },
            {"1","name" },
            {"2","jobCode" },
        };
        boolean sort =true;
        /**
         * @param evt
         */        
        public void mouseClicked(MouseEvent evt) {
            try {
                JTable table = ((JTableHeader)evt.getSource()).getTable();
                TableColumnModel colModel = table.getColumnModel();
                
                // The index of the column whose header was clicked
                int vColIndex = colModel.getColumnIndexAtX(evt.getX());
                if(cvDataBean!=null && cvDataBean.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)cvDataBean).sort(nameBeanId [vColIndex][1],sort);
                    if(sort) {
                        sort = false;
                    } else {
                        sort = true;
                    }
                    budgetTBAPersonTableModel.fireTableRowsUpdated(0, budgetTBAPersonTableModel.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }

    /**
     * Getter for property okClicked.
     * @return Value of property okClicked.
     */
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Setter for property okClicked.
     * @param okClicked New value of property okClicked.
     */
    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }
    
    /**
     * To get all the TBA persons
     * @return CoeusVector which contains list of TBA persons
     */    
    public CoeusVector getTBAPersons(){
        CoeusVector cvTBAPersons = null;
        try{
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_TBA_PERSONS);
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, requester);
            comm.send();
            ResponderBean responder = comm.getResponse();
            if(responder.isSuccessfulResponse()){
                cvTBAPersons = (CoeusVector)responder.getDataObject();
                
            }
        }catch(Exception exception) {
            exception.getMessage();
        }
        return cvTBAPersons;
    }    
    
    /**
     * To set the selected BudgetTBAPersonBean
     * @param data
     */    
    public void setSelectedTBAPersons(Object data){
        this.budgetTBAPersonBean =(BudgetTBAPersonBean) data;
    }
    
    /**
     * To get the selected BudgetTBAPersonBean
     * @return BudgetTBAPersonBean
     */    
    public BudgetTBAPersonBean getSelectedTBAPersons(){
        return budgetTBAPersonBean;
    }
    
    private static final int TBA_ID = 0;
    private static final int NAME = 1;
    private static final int JOB_CODE = 2;
    private final String BUDGET_PERSONS ="/BudgetMaintenanceServlet";
    private final String connectTo = CoeusGuiConstants.CONNECTION_URL+ BUDGET_PERSONS;
    private static final char GET_TBA_PERSONS = 'p';
}
