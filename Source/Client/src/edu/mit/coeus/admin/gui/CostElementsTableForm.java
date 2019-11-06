/*
 * CostElementsTableForm.java
 *
 * Created on December 3, 2004, 2:41 PM
 */

package edu.mit.coeus.admin.gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import edu.mit.coeus.gui.CoeusFontFactory;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusAppletMDIForm;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.budget.bean.ValidCEJobCodesBean;



/**
 *
 * @author  surekhan
 */
public class CostElementsTableForm extends javax.swing.JPanel implements ListSelectionListener , ActionListener{
    
    
    private CostElementsTableForm costElementsTableForm;
    
    private CoeusDlgWindow dlgCostElements;
    
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    
    private static final int WIDTH = 410;
    
    private static final int HEIGHT = 315;
    
    private static final int COST_ELEMENT = 0;
    
    private static final int DESCRIPTION = 1;
    
    private CoeusVector cvCostElement;
    
    private CostElementsTableModel costElementsTableModel;
    
    private static final String EMPTY_STRING = "";
    
    private String costElementName;
    private String code;
    
    /** Creates new form CostElementsTableForm */
    public CostElementsTableForm(CoeusVector cvTemp,String jobCode) {
        code = jobCode;
        cvCostElement = new CoeusVector();
        cvCostElement = cvTemp;
        initComponents();
        registerComponents();
        setTableEditors();
        postInitComponents();
        display();
    }
    

    
    private void postInitComponents(){
        String title = "Cost Elements Containing " + code + " Job Code ";
        dlgCostElements = new CoeusDlgWindow(mdiForm);
        dlgCostElements.getContentPane().add(this);
        dlgCostElements.setTitle(title);
        dlgCostElements.setFont(CoeusFontFactory.getLabelFont());
        dlgCostElements.setModal(true);
        dlgCostElements.setResizable(false);
        dlgCostElements.setSize(WIDTH,HEIGHT);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgCostElements.getSize();
        dlgCostElements.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        dlgCostElements.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                dlgCostElements.dispose();
                return;
            }
        });
        dlgCostElements.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgCostElements.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                dlgCostElements.dispose();
                return;
            }
        });
        
    }
    
    private void display(){
        tblCostElements.setRowSelectionInterval(0,0);
        setRequestFocusInThread(btnGoTo);
        dlgCostElements.show();
    }
    
    private void setTableEditors(){
        JTableHeader tableHeader = tblCostElements.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        java.awt.Color bgColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
        tblCostElements.setBackground(bgColor);
        tblCostElements.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCostElements.setRowHeight(22);
        tblCostElements.setShowHorizontalLines(true);
        tblCostElements.setShowVerticalLines(true);
        tblCostElements.setOpaque(true);
        tblCostElements.setRowSelectionAllowed(true);
        
        TableColumn column = tblCostElements.getColumnModel().getColumn(COST_ELEMENT);
        column.setPreferredWidth(100);
        column.setResizable(true);
        
        column = tblCostElements.getColumnModel().getColumn(DESCRIPTION);
        column.setPreferredWidth(210);
        column.setResizable(true);
    }
    
    private void registerComponents(){
        btnClose.addActionListener(this);
        btnGoTo.addActionListener(this);
        costElementsTableModel = new CostElementsTableModel();
        tblCostElements.setModel(costElementsTableModel);
        tblCostElements.getSelectionModel().addListSelectionListener(this);
        tblCostElements.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /*
     *It's an inner class which specifies the table model
     */
    public class CostElementsTableModel extends AbstractTableModel{
        
        // represents the column names of the columns of table
        private String[] colName = {"Cost Element","Description"};
        
        // represents the column class of the fields of table
        private Class[] colClass = {String.class,String.class};
        
        
        /*returns true if the cell is editable else returns false*/
        public boolean isCellEditable(int row, int col){
            return false;
        }
        
        /**
         * To get the column count
         * @return int
         **/
        public int getColumnCount() {
            return colName.length;
        }
        
        
        /**
         * To get the column count
         * @param col int
         * @return String
         **/
        public String getColumnName(int col){
            return colName[col];
        }
        
        /**
         * To get the column class of the table
         * @param col int
         * @return Class
         **/
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * To set the  data in the table
         * @param cvTableData CoeusVector
         * @return void
         **/
        public void setData(CoeusVector cvJobCodesAndTitles){
            cvJobCodesAndTitles = cvJobCodesAndTitles;
            fireTableDataChanged();
        }
        
        
        /**
         * To get the row count
         * @return int
         **/
        public int getRowCount() {
            if(cvCostElement==null){
                return 0;
            }else{
                return cvCostElement.size();
            }
            
        }
        
        /**
         * To get the value from the table
         * @param rowIndex int
         * @param columnIndex int
         * @return Object
         **/
        public Object getValueAt(int row, int col) {
            ValidCEJobCodesBean validCEJobCodesBean = (ValidCEJobCodesBean)cvCostElement.get(row);
            switch(col){
                case COST_ELEMENT:
                    return validCEJobCodesBean.getCostElement();
                case DESCRIPTION:
                    return validCEJobCodesBean.getDescription();
            }
            return EMPTY_STRING;
    
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnCostElements = new javax.swing.JScrollPane();
        tblCostElements = new javax.swing.JTable();
        btnClose = new javax.swing.JButton();
        btnGoTo = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnCostElements.setMinimumSize(new java.awt.Dimension(315, 275));
        scrPnCostElements.setPreferredSize(new java.awt.Dimension(315, 275));
        tblCostElements.setModel(new javax.swing.table.DefaultTableModel(
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
        scrPnCostElements.setViewportView(tblCostElements);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        add(scrPnCostElements, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMinimumSize(new java.awt.Dimension(73, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnClose, gridBagConstraints);

        btnGoTo.setFont(CoeusFontFactory.getLabelFont());
        btnGoTo.setMnemonic('G');
        btnGoTo.setText("Go To");
        btnGoTo.setMinimumSize(new java.awt.Dimension(73, 26));
        btnGoTo.setPreferredSize(new java.awt.Dimension(73, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(8, 4, 0, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(btnGoTo, gridBagConstraints);

    }//GEN-END:initComponents

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
    }    
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source.equals(btnClose)){
            dlgCostElements.dispose();
        }else if(source.equals(btnGoTo)){
            performGoToAction();
        }
    }    
    
    private void performGoToAction(){
        int selectedRow = tblCostElements.getSelectedRow();
        ValidCEJobCodesBean bean = (ValidCEJobCodesBean)cvCostElement.get(selectedRow);
        costElementName = bean.getCostElement();
        dlgCostElements.dispose();
    }
    
    public String searchString(){
        return costElementName;
    }
    
    public String disposeWindow(){
        dlgCostElements.dispose();
        return costElementName;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnGoTo;
    public javax.swing.JScrollPane scrPnCostElements;
    public javax.swing.JTable tblCostElements;
    // End of variables declaration//GEN-END:variables
    
}
