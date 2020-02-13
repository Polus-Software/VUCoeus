/*
 * SelectBudgetPersonsForm.java
 *
 * Created on October 20, 2003, 3:02 PM
 */

package edu.mit.coeus.budget.gui;

import java.awt.Component;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JDialog;
import javax.swing.event.*;
import javax.swing.JFrame;
import java.util.Vector;

import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.budget.bean.BudgetPersonsBean;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

//import edu.mit.coeus.budget.controller.PersonnelBudgetDetailController;

/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  ranjeeva
 */

public class SelectBudgetPersonsForm extends javax.swing.JComponent {
    
    /** SelectBudgetPersonsDialog */    
    public JDialog dlgSelectBudgetPersonsForm;
    boolean modal;
    private Component parent;
    /** SelectBudgetPersonsTable Model */    
    public TableModel selectBudgetPersonsTableModel;
    private BudgetPersonsBean budgetPersonsBean;
    private SelectBudgetPersonsTableModel personTableModel;
    private CoeusVector vectorBean;
    
    /** Creates new form JDialog
     * @param parent Component parent form instance
     * @param modal boolean if <true> Model window
     */
    public SelectBudgetPersonsForm(Component parent, boolean modal) {
        this.parent = parent;
        this.modal = modal;
        initUiComponents();
        setTableEditor();
        setupForm();
    }
    /** Bug fix 1868
     *Adding header listener 
     * for the table sorting
     */
    private void setTableEditor(){
        personTableModel = new SelectBudgetPersonsTableModel();
        tblPersonsList.setModel(personTableModel);
        JTableHeader tableHeader = tblPersonsList.getTableHeader();
        tableHeader.addMouseListener(new ColumnHeaderListener());
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        
        
        TableColumn tableColumn = null;
        int columnSize [] = {200,100,175};
        for(int columnIndex = 0; columnIndex<columnSize.length;columnIndex++) {
            tableColumn = tblPersonsList.getColumnModel().getColumn(columnIndex);
            tableColumn.setPreferredWidth(columnSize[columnIndex]);
        }
        
    }
    
    private void setupForm() {
        
        String title = "Select Budget Persons";
        dlgSelectBudgetPersonsForm = new CoeusDlgWindow((JFrame) parent,title,modal);
        dlgSelectBudgetPersonsForm.getContentPane().add(this);
        dlgSelectBudgetPersonsForm.pack();
        dlgSelectBudgetPersonsForm.setVisible(false);
        dlgSelectBudgetPersonsForm.setResizable(false);
        dlgSelectBudgetPersonsForm.setFont(CoeusFontFactory.getLabelFont());
        
        int x= parent.getX() + (parent.getSize().width/5);
        int y = parent.getY() + (parent.getSize().height/3);
        dlgSelectBudgetPersonsForm.setLocation(x,y);
        
    }
    
    /** Get the SelectBudgetPersonsForm Instance
     * @return SelectBudgetPersonsForm instance
     */    
    public SelectBudgetPersonsForm getUiInstance() {
        return this;
    }
    
    /** set Data to table
     * @param bean Object Beans
     */    
    public void setTableData(Object bean) {
        if(bean == null) {
            return;
        }
        
        vectorBean = (CoeusVector) bean;
        personTableModel.setData(vectorBean);
//        ((SelectBudgetPersonsTableModel) selectBudgetPersonsTableModel).setData(vectorBean);
//        ((SelectBudgetPersonsTableModel) selectBudgetPersonsTableModel).fireTableDataChanged();
    }
    
    
    
    private void initUiComponents() {
        
        java.awt.GridBagConstraints gridBagConstraints;
        
        pnlSelectBudgetPerson = new javax.swing.JPanel();
        scrPnListTable = new javax.swing.JScrollPane();
        tblPersonsList = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        pnlSelectBudgetPerson.setLayout(new java.awt.GridBagLayout());
        
        pnlSelectBudgetPerson.setMaximumSize(new java.awt.Dimension(475, 250));
        pnlSelectBudgetPerson.setMinimumSize(new java.awt.Dimension(475, 250));
        pnlSelectBudgetPerson.setPreferredSize(new java.awt.Dimension(475, 250));
        //scrPnListTable.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnListTable.setFont(CoeusFontFactory.getLabelFont());
        scrPnListTable.setMaximumSize(new java.awt.Dimension(400, 250));
        scrPnListTable.setMinimumSize(new java.awt.Dimension(400, 250));
        scrPnListTable.setPreferredSize(new java.awt.Dimension(400, 250));
        scrPnListTable.setBorder(new javax.swing.border.EtchedBorder(javax.swing.border.EtchedBorder.LOWERED));
        selectBudgetPersonsTableModel = new SelectBudgetPersonsTableModel();
        //tblPersonsList.setModel(selectBudgetPersonsTableModel);
        
        tblPersonsList.setFont(CoeusFontFactory.getNormalFont());
        
        
        scrPnListTable.setViewportView(tblPersonsList);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.7;
        pnlSelectBudgetPerson.add(scrPnListTable, gridBagConstraints);
        
        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnOK.setMaximumSize(new java.awt.Dimension(65, 25));
        btnOK.setMinimumSize(new java.awt.Dimension(65, 25));
        btnOK.setPreferredSize(new java.awt.Dimension(65, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlSelectBudgetPerson.add(btnOK, gridBagConstraints);
        
        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCancel.setMaximumSize(new java.awt.Dimension(65, 25));
        btnCancel.setMinimumSize(new java.awt.Dimension(65, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(65, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        pnlSelectBudgetPerson.add(btnCancel, gridBagConstraints);
        
        add(pnlSelectBudgetPerson);
        
    }
    
    //===
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    /*
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlSelectBudgetPerson = new javax.swing.JPanel();
        scrPnListTable = new javax.swing.JScrollPane();
        tblPersonsList = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        setTitle("Select Budget Persons");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        pnlSelectBudgetPerson.setLayout(new java.awt.GridBagLayout());

        pnlSelectBudgetPerson.setMaximumSize(new java.awt.Dimension(475, 250));
        pnlSelectBudgetPerson.setMinimumSize(new java.awt.Dimension(475, 250));
        pnlSelectBudgetPerson.setPreferredSize(new java.awt.Dimension(475, 250));
        scrPnListTable.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        scrPnListTable.setFont(CoeusFontFactory.getLabelFont());
        scrPnListTable.setMaximumSize(new java.awt.Dimension(400, 248));
        scrPnListTable.setMinimumSize(new java.awt.Dimension(400, 248));
        scrPnListTable.setPreferredSize(new java.awt.Dimension(400, 248));
        tblPersonsList.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnListTable.setViewportView(tblPersonsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 0.7;
        pnlSelectBudgetPerson.add(scrPnListTable, gridBagConstraints);

        btnOK.setFont(CoeusFontFactory.getLabelFont());
        btnOK.setMnemonic('O');
        btnOK.setText("OK");
        btnOK.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnOK.setMaximumSize(new java.awt.Dimension(70, 26));
        btnOK.setMinimumSize(new java.awt.Dimension(70, 26));
        btnOK.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        pnlSelectBudgetPerson.add(btnOK, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCancel.setMaximumSize(new java.awt.Dimension(70, 26));
        btnCancel.setMinimumSize(new java.awt.Dimension(70, 26));
        btnCancel.setPreferredSize(new java.awt.Dimension(70, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 0);
        pnlSelectBudgetPerson.add(btnCancel, gridBagConstraints);

        getContentPane().add(pnlSelectBudgetPerson);

        pack();
    }//GEN-END:initComponents
     */
    
    
    /** Closes the dialog */
    /*
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
     
    }//GEN-LAST:event_closeDialog
     */
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        // new SelectBudgetPersonsForm(new javax.swing.JFrame(), true).show();
    }
    
    /**
     * Getter for property personTableModel.
     * @return Value of property personTableModel.
     */
    public edu.mit.coeus.budget.gui.SelectBudgetPersonsForm.SelectBudgetPersonsTableModel getPersonTableModel() {
        return personTableModel;
    }    
   
    /**
     * Setter for property personTableModel.
     * @param personTableModel New value of property personTableModel.
     */
    public void setPersonTableModel(edu.mit.coeus.budget.gui.SelectBudgetPersonsForm.SelectBudgetPersonsTableModel personTableModel) {
        this.personTableModel = personTableModel;
    }    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOK;
    public javax.swing.JPanel pnlSelectBudgetPerson;
    public javax.swing.JScrollPane scrPnListTable;
    public javax.swing.JTable tblPersonsList;
    // End of variables declaration//GEN-END:variables
    
    
    
    /** SelectBudgetPersonsTable Model instance */    
    public class SelectBudgetPersonsTableModel extends AbstractTableModel {
        
        private Class columnTypes [] = {String.class,String.class,String.class };
        private String columnNames [] = {"Name", "Job Code", "Appointment Type" };
        private boolean columnEditables [] ={false,false,false};
        private Vector dataBean;
        
        
        SelectBudgetPersonsTableModel() {
            
        }
        
        /** get Column Class
         * @param columnIndex int column Index
         * @return Class Column Class
         */        
        public Class getColumnClass(int columnIndex) {
            return columnTypes [columnIndex];
        }
        
        /** is Cell Editable
         * @param rowIndex int
         * @param columnIndex int
         * @return boolean
         */        
        public boolean isCellEditable(int rowIndex, int columnIndex){
            return columnEditables[columnIndex];
        }
        
        /** set Vector of Beans Data to table
         * @param dataBean Vector containing beans
         */        
        public void setData(Vector dataBean) {
            this.dataBean = dataBean;
        }
        
        /** getColumn Names
         * @param columnIndex int column Index
         * @return String Column Name
         */        
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }
        
        /** get Column Count
         * @return int Column Count
         */        
        public int getColumnCount() {
            return columnNames.length;
        }
        
        /** get Row Count
         * @return int RowCount
         */        
        public int getRowCount() {
            if(dataBean == null) {
                return 0;
            }
            return dataBean.size();
        }
        
        
        /** getValue at rowIndex columnIndex
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         */        
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            budgetPersonsBean = (BudgetPersonsBean) dataBean.get(rowIndex);
            try {
                switch(columnIndex) {
                    
                    case 0 :
                        if(budgetPersonsBean.getFullName() != null) {
                            return budgetPersonsBean.getFullName();
                        }
                        else
                            return "";
                    case 1 :
                        return budgetPersonsBean.getJobCode();
                    case 2 :
                        return budgetPersonsBean.getAppointmentType();
                }
            } catch(Exception e){   
            }
            return null;
            
        }
        
        
        
    }//End Class - PersonnelBudgetTableModelRenderer ----------------------------------------
    
    
      /** This class will sort the column values in ascending and descending order
     *based on number of clicks. This will sort only Name, Job code and AppointmentType
     *columns only which are primary keys.
     */
    /** Bug fix 1868
     *Adding header listener 
     * for the table sorting
     */
    public class ColumnHeaderListener extends MouseAdapter {
        String nameBeanId [][] ={
            {"0","fullName" },
            {"1","jobCode"},
            {"2","" },
            {"3","effectiveDate"},
            {"4","" },
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
                if(vectorBean!=null && vectorBean.size()>0 &&
                nameBeanId [vColIndex][1].length() >1 ){
                    ((CoeusVector)vectorBean).sort(nameBeanId [vColIndex][1],sort);
                    if(sort)
                        sort = false;
                    else
                        sort = true;
                    personTableModel.fireTableRowsUpdated(0, tblPersonsList.getRowCount());
                }
            } catch(Exception exception) {
                exception.getMessage();
            }
        }
    }// End of ColumnHeaderListener.................
    
    
    
}
