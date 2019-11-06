/*
 * TypeListSelectionLookUP.java
 *
 * Created on November 25, 2011, 12:09 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils;

import edu.mit.coeus.bean.CoeusTypeBean;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.gui.CoeusDlgWindow;
import edu.mit.coeus.gui.CoeusFontFactory;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;


/**
 *
 * @author  satheeshkumarkn
 */

/*
 * This class will open a dialog with a table, ok and cancel buttons.
 * Table will have type code and description columns.
 * In setFormData param will the collection it should have only the CoeusTYpeBean's
 *
 */
public class TypeSelectionLookUp extends javax.swing.JPanel implements ActionListener {
    
    private static final int TYPE_CODE_COLUMN = 0;
    private static final int TYPE_CODE_DESCRIPTION = 1;
    private static final int ROW_HEIGHT = 22;
    private static final int TYPE_CODE_COLUMN_WIDTH = 85;
    private static final int TYPE_DESC_COLUMN_WIDTH = 300;
    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 400;
    private String windowTitle;
    private CoeusDlgWindow dlgType;
    private CoeusTypeBean selectedType;
    private CoeusVector cvSelectedTypes;
    private TypeSelectionTableModel typeSelectionTableModel;
    private int selection;
    
    /** Creates new form TypeListSelectionLookUP 
     *  description: The selection mode used by the row and column selection models.
     *        enum: SINGLE_SELECTION            ListSelectionModel.SINGLE_SELECTION
     *              SINGLE_INTERVAL_SELECTION   ListSelectionModel.SINGLE_INTERVAL_SELECTION
     *              MULTIPLE_INTERVAL_SELECTION ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
     */
    public TypeSelectionLookUp(String windowTitle, int selection) {
        this.windowTitle = windowTitle;
        this.selection = selection;
        initComponents();
        registerComponents();
    }
    
    /**
     * Method to set the form data, This param collection should have only the CoeusTypeBean's
     * @param cvTypes
     */
    public void setFormData(CoeusVector cvTypes){
        typeSelectionTableModel = new TypeSelectionTableModel();
        tblType.setModel(typeSelectionTableModel);
        setTableColumnProperties();
        if(cvTypes != null && !cvTypes.isEmpty()){
            typeSelectionTableModel.setData(cvTypes);
            tblType.setRowSelectionInterval(0,0);
        }
    }
    
    /*
     *
     * Method to register all the components in the form
     */
    public void registerComponents() {
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        java.awt.Component[] components={btnOk,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
    }
    
    /*
     * Method to set the all the column properties in the table
     */
    private void setTableColumnProperties(){
        tblType.setOpaque(false);
        tblType.setShowVerticalLines(false);
        tblType.setShowHorizontalLines(false);
        tblType.setRowHeight(ROW_HEIGHT);
        tblType.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblType.setSelectionMode(selection);
        tblType.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        // Type code column properties
        TableColumn column = tblType.getColumnModel().getColumn(TYPE_CODE_COLUMN);
        column.setMinWidth(TYPE_CODE_COLUMN_WIDTH);
        column.setMaxWidth(TYPE_CODE_COLUMN_WIDTH);
        column.setPreferredWidth(TYPE_CODE_COLUMN_WIDTH);
        column.setResizable(false);
        
        // Type description
        column = tblType.getColumnModel().getColumn(TYPE_CODE_DESCRIPTION);
        column.setPreferredWidth(TYPE_DESC_COLUMN_WIDTH);
        column.setResizable(true);
        
        tblType.getTableHeader().setReorderingAllowed(false);
        tblType.getTableHeader().setResizingAllowed(true);
         
        tblType.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
    }
    
   /*
    * Method to display the form in a dialog
    *
    */
    public void display()throws CoeusException{
        dlgType =new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), windowTitle, true);
        dlgType.addWindowListener(new WindowAdapter(){
            public void windowOpened(WindowEvent we){
                btnOk.requestFocusInWindow();
                btnOk.setFocusable(true);
                btnOk.requestFocus();
            }
        });
        dlgType.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                dlgType.dispose();
            }
        });
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dlgType.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        Dimension dlgSize = dlgType.getSize();
        dlgType.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgType.getContentPane().add(this);
        dlgType.setResizable(false);
        dlgType.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scrnType = new javax.swing.JScrollPane();
        tblType = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrnType.setMaximumSize(new java.awt.Dimension(400, 350));
        scrnType.setMinimumSize(new java.awt.Dimension(400, 350));
        scrnType.setPreferredSize(new java.awt.Dimension(400, 350));
        tblType.setModel(new javax.swing.table.DefaultTableModel(
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
        scrnType.setViewportView(tblType);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(scrnType, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('K');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(73, 23));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(73, 23));
        btnCancel.setMinimumSize(new java.awt.Dimension(73, 23));
        btnCancel.setPreferredSize(new java.awt.Dimension(73, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        add(btnCancel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrnType;
    private javax.swing.JTable tblType;
    // End of variables declaration//GEN-END:variables
    
    
    /**
     * All the button Event trigger are handled in this method
     * @param aE
     */
    public void actionPerformed(ActionEvent aE) {
        if(aE.getSource().equals(btnOk)){
            if(selection == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION){
                 int[] selectedRow = tblType.getSelectedRows();
                 CoeusVector cvTypes = typeSelectionTableModel.getData();
                 CoeusVector cvSelectedTypes = new CoeusVector();
                 if(cvTypes != null && !cvTypes.isEmpty()){
                     for(int index=0;index<selectedRow.length;index++){
                         CoeusTypeBean coeusTypeBean = (CoeusTypeBean)cvTypes.get(selectedRow[index]);
                         cvSelectedTypes.add(coeusTypeBean);
                     }
                     if(cvSelectedTypes.isEmpty()){
                         CoeusOptionPane.showWarningDialog("Select a type");
                     }else{
                         setSelectedTypes(cvSelectedTypes);
                         dlgType.dispose();
                     }
                 }
            }else if(selection == ListSelectionModel.SINGLE_SELECTION){
                int selectedRow = tblType.getSelectedRow();
                if(selectedRow > -1 ){
                    CoeusVector cvTypes = typeSelectionTableModel.getData();
                    if(cvTypes != null && !cvTypes.isEmpty()){
                        CoeusTypeBean coeusTypeBean = (CoeusTypeBean)cvTypes.get(selectedRow);
                        setSelectedType(coeusTypeBean);
                    }
                    dlgType.dispose();
                }else{
                    CoeusOptionPane.showWarningDialog("Select a type");
                }
            }
        }else if(aE.getSource().equals(btnCancel)){
            dlgType.dispose();
        }
    }
    
    /**
     * Method to get the selected type
     * @return CoeusTypeBean
     */
    public CoeusTypeBean getSelectedType() {
        return selectedType;
    }
    
    /**
     * Method to set the selected type
     * @param selectedType  - CoeusTypeBean
     */
    public void setSelectedType(CoeusTypeBean selectedType) {
        this.selectedType = selectedType;
    }
    
    class TypeSelectionTableModel extends DefaultTableModel {
        private String colNames[] = {"Code","Description"};
        private Class colTypes[]  = {Integer.class, String.class};
        private CoeusVector cvData;
        
        /**
         * Method to check whether the selected column can be edited
         * @param row
         * @param column
         * @return
         */
        public boolean isCellEditable(int row, int column) {
            return false;
        }
        
        /**
         * Method to get the value for row and column
         * @param row
         * @param column
         * @return
         */
        public Object getValueAt(int row, int column) {
            if(cvData != null && !cvData.isEmpty()){
                CoeusTypeBean coeusTypeBean = (CoeusTypeBean)cvData.get(row);
                switch(column) {
                    case TYPE_CODE_COLUMN:
                        return coeusTypeBean.getTypeCode();
                    case TYPE_CODE_DESCRIPTION:
                        return coeusTypeBean.getTypeDescription();
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
            dataVector = cvData;
            this.cvData = cvData;
            fireTableDataChanged();
        }
        
        /**
         * Method to get the model data
         * @return cvData
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
    }

    /**
     * Method to get the selection details mutiple or single
     * @return selection
     */
    public int getSelection() {
        return selection;
    }

    /**
     * Method to get the all the seleted types
     * @return cvSelectedTypes
     */
    public CoeusVector getSelectedTypes() {
        return cvSelectedTypes;
    }

    /**
     * Method to set the all the seleted types
     * @param cvSelectedTypes 
     */
    public void setSelectedTypes(CoeusVector cvSelectedTypes) {
        this.cvSelectedTypes = cvSelectedTypes;
    }
    
}
