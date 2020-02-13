/*
 * @(#)CommitteeSelectionForm.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on November 25, 2002, 3:00 PM
 * @version 1.0
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Enumeration;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;

/** This form is used to select a Committee where a particular Protocol can be
 * submitted. This form displays list of Committees whose Research Areas
 * matches with the Protocol Research Areas. User can select only one Committee
 * for submitting a Protocol. If there is no Committee whose Research Areas
 * matches with the Protocol Research Areas then user can select any Committee
 * which are available.
 *
 * @author ravikanth
 */
public class CommitteeSelectionForm extends JComponent {
    
    /** collection which stores all the eligible committees where this 
        particular protocol can be submitted */
    private ArrayList committeeList;
    
    /** reference object of CoeusMessageResources which will be used to get the
       messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources 
            = CoeusMessageResources.getInstance();
    
    /** Creates new form <CODE>CommitteeSelectionForm</CODE> and initializes the components.
     *
     * @deprecated instead use the constructor
     *  CommitteeSelectionForm( ArrayList committeeList )
     */
    public CommitteeSelectionForm() {
        initComponents();
    }
    
    /** Constructor used to create and initialize the components in
     * <CODE>CommitteeSelectionForm</CODE>. And also used to populate the committee list
     * which has been sent as a parameter.
     *
     * @param committeeList ArrayList of <CODE>CommitteeMaintenanceFormBean</CODE> which will
     * be used to display the list of Committees in a table.
     */    
    
    public CommitteeSelectionForm( ArrayList committeeList ){
        this.committeeList = committeeList;
        initComponents();
        setTableModel();
        ((DefaultTableModel)tblCommittee.getModel()).setDataVector(
            constructTableData(),getColumnNames());
        
        ((DefaultTableModel)tblCommittee.getModel()).fireTableDataChanged();
        
        tblCommittee.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        setTableFormat();
    }
    
    
    /**
     * This method is used to set the model to the committee table with 
     * default values and column names.
     */
    private void setTableModel(){

        tblCommittee.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Committee Id", "Committee Name"
        }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };
            
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        
    }
    
    /**
     * Supporting method used to set the display formats of the table like
     * column lengths etc.
     */
    
    private void setTableFormat() {
        tblCommittee.getTableHeader().setReorderingAllowed(false);
        tblCommittee.getTableHeader().setResizingAllowed(false);
        TableColumn column = tblCommittee.getColumn("Committee Id");
        column.setMaxWidth(150);
        column.setMinWidth(150);
        column.setPreferredWidth(150);
    
    }
    
    /**
     * Supporting method used to get the column names to be used in the table
     * @return  colNames String[] which consists of column header names.
     */    
    
    private String[] getColumnNames(){
        
        Enumeration enumColumns = tblCommittee.getColumnModel().getColumns();
        String[] colNames = new String[ tblCommittee.getColumnCount() ];
        int colIndex = 0;
        while( enumColumns.hasMoreElements()){
            colNames[ colIndex++ ] = ((TableColumn)
                    enumColumns.nextElement()).getHeaderValue().toString();
        }
        return colNames;
    }
    
    /**
     * Supporting method which constructs double dimensional object array from
     * the collection of CommitteeMaintenanceFormBean.
     * @return  tableData Object[][] which will be used in displaying table data
     */    
    
    private Object[][] constructTableData() {
        /** holds the values to be displayed in table */
        Object[][] tableData;
        
        int rows=0;
        if( committeeList != null ){
            rows = committeeList.size();
        }
        tableData = new Object[rows][2];
        CommitteeMaintenanceFormBean committeeBean;
        Object[] tableRowData;
        
        for( int rowIndex = 0 ; rowIndex < rows ; rowIndex++ ) {
            /* extract committee Id and committee name from bean and construct
             * double dimensional object array.
             */
            committeeBean = ( CommitteeMaintenanceFormBean )
                    committeeList.get( rowIndex );
            tableRowData = new Object[] { committeeBean.getCommitteeId(),
                    committeeBean.getCommitteeName() };
            tableData [ rowIndex ] = tableRowData;
        }
        return tableData;
    }
    
    /** This method is used to set the available Committees to the committee
     * table.
     *
     * @param commList Collection of <CODE>CommitteeMaintenanceFormBean</CODE> which are
     * used to display in committee table.
     */
    public void setCommitteeList(ArrayList commList){
        this.committeeList = commList;

        ((DefaultTableModel)tblCommittee.getModel()).setDataVector(
            constructTableData(),getColumnNames());
        
        ((DefaultTableModel)tblCommittee.getModel()).fireTableDataChanged();
        setTableFormat();

    }
    
    /** This method is used to set the selected Committee and select that
     * Committee in committee table. If the Committee is not present in the
     * committee list, it will add that Committee to the committee list and
     * makes it as selected.
     *
     * @param committeeId String representing selected Committee
     * @param committeeName String which holds the selected committee name.
     */
    public void setSelectedCommittee(String committeeId, String committeeName ){
        int rowCount = tblCommittee.getRowCount();
        boolean found = false;
        String commId = "";
        int rowIndex = 0;
        for(; rowIndex < rowCount; rowIndex++){
            commId = (String)tblCommittee.getValueAt(rowIndex,0);
            if(committeeId.equals(commId)){
                tblCommittee.setRowSelectionInterval(rowIndex,rowIndex);
                tblCommittee.scrollRectToVisible(
                    tblCommittee.getCellRect(rowIndex ,0, true));
                found = true;
                break;
            }
        }
        if(!found){
            Object[] rowData = new Object[2];
            rowData[0] = committeeId;
            rowData[1] = committeeName;
            ((DefaultTableModel)tblCommittee.getModel()).addRow(rowData);
            tblCommittee.setRowSelectionInterval(rowIndex,rowIndex);
            tblCommittee.scrollRectToVisible(
                tblCommittee.getCellRect(rowIndex ,0, true));
        }
    }
    
    /** This method is used to check whether user has selected any Committee or
     * not.
     * @return boolean true if any Committee row has been selected else false.
     */
    
    
    public boolean isCommitteeSelected(){
        return ( tblCommittee.getSelectedRow() == -1 ) ? false : true ;
    }
    
    
    /** This method is used to get the selected Committee details
     *
     * @return <CODE>CommitteeMaintenanceFormBean</CODE> with all the
     * Committee details required for Protocol Submission. If no Committee is
     * selected it will return null.
     */
    public CommitteeMaintenanceFormBean getSelectedCommittee(){
        int selRow = tblCommittee.getSelectedRow();
        if(selRow != -1){
            CommitteeMaintenanceFormBean committeeBean 
                = new CommitteeMaintenanceFormBean();
            committeeBean.setCommitteeId((String)tblCommittee.getValueAt(selRow,
                0));
            committeeBean.setCommitteeName((String)tblCommittee.getValueAt(selRow,
                1));
            return committeeBean;
        }
        return null;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents()//GEN-BEGIN:initComponents
    {
        scrPnCommittee = new javax.swing.JScrollPane();
        tblCommittee = new javax.swing.JTable();
        tblCommittee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setLayout(new java.awt.BorderLayout(10, 10));

        tblCommittee.setFont(CoeusFontFactory.getNormalFont());
        tblCommittee.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                tblCommitteeMouseClicked(evt);
            }
        });

        scrPnCommittee.setViewportView(tblCommittee);

        add(scrPnCommittee, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents

    private void tblCommitteeMouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_tblCommitteeMouseClicked
    {//GEN-HEADEREND:event_tblCommitteeMouseClicked
     
    }//GEN-LAST:event_tblCommitteeMouseClicked
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblCommittee;
    private javax.swing.JScrollPane scrPnCommittee;
    // End of variables declaration//GEN-END:variables
    
}
