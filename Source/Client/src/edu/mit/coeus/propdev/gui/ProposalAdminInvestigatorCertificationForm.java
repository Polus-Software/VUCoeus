/*
 * @(#)ProposalAdminInvestigatorCertificationForm.java  Created on May 27, 2003, 2:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * 
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.CoeusGuiConstants;
import edu.mit.coeus.utils.CoeusOptionPane;
import edu.mit.coeus.utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import edu.mit.coeus.propdev.bean.ProposalCertificationFormBean;
/** This Panel is used to display the Investigator Certification
 * details
 * @author Senthil
 */
public class ProposalAdminInvestigatorCertificationForm extends JComponent implements TypeConstants{
    
    
    ProposalCertificationFormBean proposalCertificationFormBean;
    private char functionType;
    private Vector vecCertification ;
    Vector tableData;
    Vector tableRow;
    private java.text.SimpleDateFormat dtFormat;
    private DateUtils dtUtils;
    private boolean saveRequired;
    
    /** Creates new form ProposalAdminInvestigatorCertificationForm */
    public ProposalAdminInvestigatorCertificationForm() {
    }
    /** 
     * Creates new form ProposalAdminInvestigatorCertificationForm
     * @param functionType Contains the functionType passed from the ProposalBaseWindow.
     * @param vecCertification Contains a vector of ProposalCertificationFormBean
     *
     */
    public ProposalAdminInvestigatorCertificationForm(char functionType,
                   Vector vecCertification) {
        this.functionType = functionType;
        this.vecCertification = vecCertification;
        this.dtUtils = new DateUtils();
        this.dtFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
        tableData = new Vector();
        initComponents();
        getDataFromBaseWin();
        setEditors();
    }
    
    private void setEditors(){
        
        TableColumn column = tblInvCertifications.getColumnModel().getColumn(0);
        column.setMinWidth(330);
        //column.setMaxWidth(330);
        column.setPreferredWidth(330);
        column.setResizable(true);
        
        
        column = tblInvCertifications.getColumnModel().getColumn(1);
        column.setMinWidth(75);
        //column.setMaxWidth(75);
        column.setPreferredWidth(75);
        column.setResizable(true);
        

        column = tblInvCertifications.getColumnModel().getColumn(2);
        column.setMinWidth(100);
        //column.setMaxWidth(100);
        column.setPreferredWidth(100);
        column.setResizable(true);
        column.setCellEditor(new DateEditor("Certified On"));
        
        column = tblInvCertifications.getColumnModel().getColumn(3);
        column.setMinWidth(120);
        //column.setMaxWidth(120);
        column.setPreferredWidth(120);
        column.setResizable(true);
        column.setCellEditor(new DateEditor("Received On"));
        tblInvCertifications.getTableHeader().setReorderingAllowed( false );
        tblInvCertifications.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        tblInvCertifications.setRowSelectionAllowed(false);
    }
    
    
    private void getDataFromBaseWin(){
        if ((vecCertification != null) && ((vecCertification.size())>0)){
            try{
                int dataSize = vecCertification.size();
                ProposalCertificationFormBean proposalCertificationFormBean=null;
                for( int indx = 0 ; indx < dataSize; indx++) {
                    proposalCertificationFormBean = 
                        (ProposalCertificationFormBean) vecCertification.elementAt( indx );

                    tableRow = new Vector();
                    if(proposalCertificationFormBean != null){
                        tableRow.addElement( proposalCertificationFormBean.getPersonName() == null ? "" : proposalCertificationFormBean.getPersonName());
                        tableRow.addElement( new Boolean(proposalCertificationFormBean.isCertifyFlag()));
                        tableRow.addElement( proposalCertificationFormBean.getDateCertify() == null ? "" : dtUtils.formatDate(
                            Utils.convertNull(proposalCertificationFormBean.getDateCertify()), "dd-MMM-yyyy"));
                        tableRow.addElement( proposalCertificationFormBean.getDateReceivedByOsp() == null ? "" : dtUtils.formatDate(
                            Utils.convertNull(proposalCertificationFormBean.getDateReceivedByOsp()), "dd-MMM-yyyy"));
                        
                        
                        // adding listener
                            proposalCertificationFormBean.addPropertyChangeListener(
                            new PropertyChangeListener(){
                                public void propertyChange(PropertyChangeEvent pce){
                                    
                                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                                        saveRequired = true;
                                    }
                                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                                        saveRequired = true;
                                    }
                                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                                            saveRequired = true;
                                        }
                                    }
                                }
                            });
                    }
                    tableData.addElement( tableRow );
                }
                ((DefaultTableModel)tblInvCertifications.getModel()).setDataVector(tableData, getColumnNames() );
                ((DefaultTableModel)tblInvCertifications.getModel()).fireTableDataChanged();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    /**
     * This method is used to get the Column Names of Key study personal
     * table data.
     * @return Vector collection of column names of key study personnel table.
     */

    private Vector getColumnNames(){
        Enumeration enumColNames = tblInvCertifications.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /** Method to get all the table data from JTable
     * @throws Exception Exception Object
     * @return Vector, a Vector which consists of ProposalCertificationFormBean
     */    
    
    public Vector getFormData() throws Exception{
        Vector updTableData = new Vector();
         if(tblInvCertifications.getRowCount() > 0){

            int rowCount = tblInvCertifications.getRowCount();
            for(int inInd = 0 ; inInd < rowCount ; inInd++){

                proposalCertificationFormBean = new ProposalCertificationFormBean();

                proposalCertificationFormBean = 
                  (ProposalCertificationFormBean) vecCertification.elementAt( inInd );
                
                String pName = (String)((DefaultTableModel)
                    tblInvCertifications.getModel()).getValueAt(inInd,0);
                boolean isCertfied = ((Boolean)((DefaultTableModel)
                    tblInvCertifications.getModel()).getValueAt(inInd,1)).booleanValue();
                
                String dtStrCertified = (String)((DefaultTableModel)
                    tblInvCertifications.getModel()).getValueAt(inInd,2);
                if ( (dtStrCertified != null) && (dtStrCertified.trim().length()>0)){
                    Date dtCertified = (Date)dtFormat.parse(dtUtils.restoreDate(
                        tblInvCertifications.getValueAt(inInd,2).toString(),"/:-,"));
                    proposalCertificationFormBean.setDateCertify(new java.sql.Date(dtCertified.getTime()));                    
                }
                                
                String dtStrReceived = (String)((DefaultTableModel)
                    tblInvCertifications.getModel()).getValueAt(inInd,3);
                if ((dtStrReceived != null)&&(dtStrReceived.trim().length()>0)){
                    Date dtReceived = (Date) dtFormat.parse(dtUtils.restoreDate(
                    tblInvCertifications.getValueAt(inInd,3).toString(),"/:-,"));
                    proposalCertificationFormBean.setDateReceivedByOsp(new java.sql.Date(dtReceived.getTime()));
                }
                proposalCertificationFormBean.setPersonName(pName);
                proposalCertificationFormBean.setCertifyFlag(isCertfied);
                proposalCertificationFormBean.setAcType(UPDATE_RECORD);
                updTableData.addElement(proposalCertificationFormBean);
                
            }//END FOR
        }//END ROW COUNT IF CHECK
        return updTableData;
    }
        
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        scrPnInvCertifications = new javax.swing.JScrollPane();
        tblInvCertifications = new javax.swing.JTable();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        scrPnInvCertifications.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrPnInvCertifications.setMaximumSize(new java.awt.Dimension(625, 350));
        scrPnInvCertifications.setMinimumSize(new java.awt.Dimension(625, 350));
        scrPnInvCertifications.setPreferredSize(new java.awt.Dimension(625, 350));
        tblInvCertifications.setFont(CoeusFontFactory.getNormalFont());
        tblInvCertifications.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Investigator", "Certified", "Certified on", "Received on"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvCertifications.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblInvCertifications.setRowHeight(20);
        scrPnInvCertifications.setViewportView(tblInvCertifications);

        add(scrPnInvCertifications);

    }//GEN-END:initComponents

    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }    
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JScrollPane scrPnInvCertifications;
    public javax.swing.JTable tblInvCertifications;
    // End of variables declaration//GEN-END:variables
 
    class ColumnValueEditor extends DefaultCellEditor 
                                                implements TableCellEditor {
        private JTextField txtDesc;
        private int selectedRow ;
        
        ColumnValueEditor(int len ){
            super(new JTextField());
            txtDesc = new JTextField();
            txtDesc.setFont(CoeusFontFactory.getNormalFont());
        }
        

        /**
         * Adding a table cell editor 
         * @param table table reference passed
         * @param value value in the cell
         * @param isSelected boolean variable
         * @param row table row
         * @param column table column
         * @return  Component*/        
        public Component getTableCellEditorComponent(JTable table,
        Object value,
        boolean isSelected,
        int row,
        int column){

            String newValue = ( String ) value ;
            if( newValue != null && newValue.length() > 0 ){
                txtDesc.setText( (String)newValue );
            }else{
                txtDesc.setText("");
            }
            return txtDesc;
        }
        
        /** Method used by Cell Editor
         * @return Integer value of 1.
         */        
        public int getClickCountToStart(){
            return 1;
        }
        
        /** Stop Cell Editing.
         * @return Boolean value.
         */        
        public boolean stopCellEditing() {
          return super.stopCellEditing();
        }
        
        /** Method used by cell Editor
         */        
        protected void fireEditingStopped() {
          super.fireEditingStopped();
        }
    }
    
    /** ColumnValueRenderer Class
     */    
    public class ColumnValueRenderer extends JTextField implements TableCellRenderer {

        /** Creates ColumnValueRenderer.
         */        
        public ColumnValueRenderer() {
          setOpaque(true);
        }

        /** Table cell renderer
         * @param table Table reference
         * @param value Value in cell
         * @param isSelected boolean value
         * @param row row number
         * @param column column number
         * @return component
         * @param hasFocus boolean value
         */
        public Component getTableCellRendererComponent(JTable table, 
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
              
              setText( (value ==null) ? "" : value.toString() );
              return this;
        }
     }
    /*
     * Inner class to set the editor for date columns/cells.
     */
    class DateEditor extends AbstractCellEditor implements TableCellEditor {

        private String colName;
        private static final String DATE_SEPARATERS = ":/.,|-";
        private static final String REQUIRED_DATEFORMAT = "dd-MMM-yyyy";
        private JTextField dateComponent = new JTextField();
        
        private String stDateValue;
        private int selectedRow;
        private int selectedColumn;
        boolean temporary;
        DateEditor(String colName) {
            this.colName = colName;
            ((JTextField)dateComponent).setFont(CoeusFontFactory.getNormalFont());
            ((JTextField)dateComponent).setDocument(new LimitedPlainDocument(12));
            dateComponent.addFocusListener(new FocusAdapter(){
                public void focusGained(FocusEvent fe){
                    temporary = false;
                }
                public void focusLost(FocusEvent fe){
                    if ( !fe.isTemporary()  ){
                        if(!temporary){
                            stopCellEditing();
                        }
                    }
                }
            });
        }
        
        /**
         * Date validation
         */

        private void validateEditorComponent(){

            temporary = true;
            String formattedDate = null;
            String editingValue = (String) getCellEditorValue();
            if (editingValue != null && editingValue.trim().length() > 0) {
                // validate date field
                formattedDate = new DateUtils().formatDate(editingValue,
                    DATE_SEPARATERS,REQUIRED_DATEFORMAT);
                if(formattedDate == null) {
                    // invalid date                    
                    CoeusOptionPane.showInfoDialog("Please enter a valid "+colName+" date.");
                    dateComponent.setText(stDateValue);
                }else{
                    // valid date
                    dateComponent.setText(formattedDate);
                    if(!editingValue.equals(stDateValue)){
                        //setModel(formattedDate);
                    }
                    //saveRequired = true;
                }
            }
            if( ((editingValue == null ) || (editingValue.trim().length()== 0 )) && 
                        (stDateValue != null) && (stDateValue.trim().length()>= 0 )){
                //setModel(null);
            }
        }
        
        // Sets the editing value to the Bean
        private void setModel(String formatDate){//editingValue
            //saveRequired=true;
            String appDate = dtUtils.restoreDate(formatDate,"/:-,");
            ProposalCertificationFormBean pCertBean = 
                        (ProposalCertificationFormBean)vecCertification.elementAt(selectedRow);
            String aType = pCertBean.getAcType();                
            try{
                if(selectedColumn == 2){
                    if(appDate != null){//editingValue    Handle
                        pCertBean.setDateCertify(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pCertBean.setDateCertify(null);
                    }
                }else if(selectedColumn == 3){
                    if(appDate != null){
                        pCertBean.setDateReceivedByOsp(new java.sql.Date(dtFormat.parse(appDate).getTime()));
                    }else{
                        pCertBean.setDateReceivedByOsp(null);
                    }
                }
            pCertBean.setAcType(UPDATE_RECORD);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        /** Table cell Editor
         * @param table Table reference
         * @param value Value in cell
         * @param isSelected boolean value
         * @param row row number
         * @param column column number
         * @return component
         */        
        public Component getTableCellEditorComponent(JTable table,Object value,
            boolean isSelected, int row,int column){

            selectedRow = row;
            selectedColumn = column;
            JTextField tfield =(JTextField)dateComponent;
            String currentValue = (String)value;
            String stTempValue =(String)tblInvCertifications.getValueAt(row,column);
            if(stTempValue != null){
                stDateValue = dtUtils.restoreDate(stTempValue,DATE_SEPARATERS) ;
            }
            if( ( currentValue != null  ) && (currentValue.trim().length()!= 0) ){
                String newValue = dtUtils.restoreDate(currentValue,
                    DATE_SEPARATERS) ;
                tfield.setText(newValue);
                return dateComponent;
            }

            tfield.setText( ((String)value));
            return dateComponent;
        }

        /**
         * Forwards the message from the CellEditor to the delegate.
         * @return true if editing was stopped; false otherwise
         */
        public boolean stopCellEditing() {
            validateEditorComponent();
            return super.stopCellEditing();
        }

        /** Returns the value contained in the editor.
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            return ((JTextField)dateComponent).getText();
        }

        /**
         * Invoked when an cell has been selected or deselected by the user.
         * The code written for this method performs the operations that need to
         * occur when an cell is selected (or deselected).
         * @param e an ItemEvent.
         */
        public void itemStateChanged(ItemEvent e) {
            super.fireEditingStopped();
        }
        
        /** Method used by Cell Editor
         * @return integer value of 1
         */        
        public int getClickCountToStart(){
            return 1;
        }
    }
}
