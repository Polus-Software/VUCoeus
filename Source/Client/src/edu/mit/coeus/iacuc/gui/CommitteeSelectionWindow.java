/*
 * CommiteeSelectionwindow.java
 *
 * Created on July 22, 2003, 2:19 PM
 */

/* PMD check performed, and commented unused imports and variables on 23-NOV-2010
 * by Bharati 
 */

package edu.mit.coeus.iacuc.gui;

//import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import javax.swing.*;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.Enumeration;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.iacuc.bean.* ;
import java.awt.Frame;
import edu.mit.coeus.utils.* ; 
import java.util.Vector;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import java.awt.Cursor;

/**
 *
 * @author  prahalad
 */
public class CommitteeSelectionWindow extends CoeusDlgWindow
{       
   /** collection which stores all the eligible committees where this 
        particular protocol can be submitted */
    private ArrayList committeeList;
    
    private String committeeId ;
    private String committeeName ;
    
    //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - start
    private static final String DETERMINATION_NOTIFICATION_FOR_REVIEWERS = "iacucProtoSubmission_exceptionCode.1007";
    private boolean mailSent = false;
    private String OldCommiteeId;
    //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - end
    
    /** reference object of CoeusMessageResources which will be used to get the
       messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources 
            = CoeusMessageResources.getInstance();
    private static final char COMMITTEE_LIST_FOR_MODULE = 'z';
    
    /** Creates new form <CODE>CommitteeSelectionForm</CODE> and initializes the components.
     *
     * @deprecated instead use the constructor
     *  CommitteeSelectionForm( ArrayList committeeList )
     */
    public CommitteeSelectionWindow() {
        initComponents();
    }
    
    /** Constructor used to create and initialize the components in
     * <CODE>CommitteeSelectionForm</CODE>. And also used to populate the committee list
     * which has been sent as a parameter.
     *
     * @param committeeList ArrayList of <CODE>CommitteeMaintenanceFormBean</CODE> which will
     * be used to display the list of Committees in a table.
     */    
    
    public CommitteeSelectionWindow( Frame parent, String title, boolean modal, ArrayList committeeList ){
        super(parent, title, modal);
        this.committeeList = committeeList;
        initComponents();
        
        setSize(500,350) ; 
        
        setTableModel();
        ((DefaultTableModel)tblCommittee.getModel()).setDataVector(
            constructTableData(),getColumnNames());
        
        ((DefaultTableModel)tblCommittee.getModel()).fireTableDataChanged();
        
        tblCommittee.getTableHeader().setFont(CoeusFontFactory.getLabelFont());
        
        //Added by Vyjayanthi 21/12/2003 - Start
        java.awt.Component[] components = {tblCommittee, btnOk, btnCancel, btnShowAll};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        if( tblCommittee.getRowCount() > 0 ){
            tblCommittee.setRowSelectionInterval(0,0);
        }
        btnOk.setMnemonic('O');
        btnCancel.setMnemonic('C');
        btnShowAll.setMnemonic('S');
        //End
        
        setTableFormat();
        addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(java.awt.event.ActionEvent ae){
                committeeId = null ;
                committeeName = null ;
                tblCommittee.clearSelection();
                dispose() ;
            }
        });
        addWindowListener( new java.awt.event.WindowAdapter(){
            public void windowActivated(java.awt.event.WindowEvent we){
                if( tblCommittee.getRowCount() > 0 ) {
                    tblCommittee.requestFocusInWindow();
                }else{
                    btnCancel.requestFocusInWindow();
                }
            
            }
            public void windowClosing(java.awt.event.WindowEvent we){
                committeeId = null ;
                committeeName = null ;
                tblCommittee.clearSelection();
                dispose() ;
            }
        });
        
    }
    
    
    public String getCommitteeId()
    {
        return this.committeeId ;
    }
    
    public String getCommitteeName()
    {
        return this.committeeName ;
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
        tblCommittee.setFont(CoeusFontFactory.getNormalFont()) ;
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
        java.awt.GridBagConstraints gridBagConstraints;

        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCommittee = new javax.swing.JTable();
        btnShowAll = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(btnCancel, gridBagConstraints);

        tblCommittee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {},
                {},
                {},
                {}
            },
            new String []
            {

            }
        ));
        jScrollPane1.setViewportView(tblCommittee);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.ipadx = 360;
        gridBagConstraints.ipady = 250;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setText("Show All");
        btnShowAll.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnShowAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        getContentPane().add(btnShowAll, gridBagConstraints);

    }//GEN-END:initComponents

    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnShowAllActionPerformed
    {//GEN-HEADEREND:event_btnShowAllActionPerformed
        setCommitteeList(new ArrayList(getCommitteList())) ;
    }//GEN-LAST:event_btnShowAllActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        // Add your handling code here:
        this.committeeId = null ;
        this.committeeName = null ;
        tblCommittee.clearSelection();
        this.dispose() ;
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnOkActionPerformed
    {//GEN-HEADEREND:event_btnOkActionPerformed
        // Add your handling code here:
        if (tblCommittee.getSelectedRowCount() > 0) {
            //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - start
            //If determination notification already sent and still user want to select new committee then giving an warning
            //If selects yes then it allows to add selected committee
            
            String newCommitteeId = (String)tblCommittee.getValueAt(tblCommittee.getSelectedRow(), 0) ;
            if(isMailSent() && !newCommitteeId.equalsIgnoreCase(getOldCommiteeId())){
                int selection = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DETERMINATION_NOTIFICATION_FOR_REVIEWERS), CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES);
                if(selection ==CoeusOptionPane.SELECTION_YES){
                    this.committeeId = (String)tblCommittee.getValueAt(tblCommittee.getSelectedRow(), 0) ;
                    this.committeeName = (String)tblCommittee.getValueAt(tblCommittee.getSelectedRow(), 1) ;
                    this.dispose() ;
                } else if(selection ==CoeusOptionPane.SELECTION_NO){
                    this.dispose();
                }
            }else{
                this.committeeId = (String)tblCommittee.getValueAt(tblCommittee.getSelectedRow(), 0) ;
                this.committeeName = (String)tblCommittee.getValueAt(tblCommittee.getSelectedRow(), 1) ;
                this.dispose();
            }
            //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - end
        } else {
            CoeusOptionPane.showErrorDialog("Select a Committee") ;
        }
    }//GEN-LAST:event_btnOkActionPerformed
    
    //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - start
    /**
     * Method used to get the value for mailSent
     * @param mailSent boolean
     */
    public boolean isMailSent() {
        return mailSent;
    }
    
    /**
     * Method used to set the value for mailSent
     * @param mailSent boolean
     */
    public void setMailSent(boolean mailSent) {
        this.mailSent = mailSent;
    }
    
    /**
     * Method used to get the value of existing schedule id
     * @param mailSent boolean
     */
    public String getOldCommiteeId(){
        return OldCommiteeId;
    }
    
    /**
     * Method used to set the value for mailSent
     * @param mailSent boolean
     */
    public void setOldCommiteeId(String OldCommiteeId){
        this.OldCommiteeId = OldCommiteeId;
    }
    //Added for COEUSQA-3335 : IACUC - Allow Removing Schedule in Submission Details - end
    
    private Vector getCommitteList() {
        /**
         * This sends the functionType as 'G' to the servlet indicating to
         * get the details of all existing committees with the required
         * information
         */

        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject(""+CoeusConstants.IACUC_COMMITTEE_TYPE_CODE);
        request.setFunctionType(COMMITTEE_LIST_FOR_MODULE);
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );        
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        if (response.isSuccessfulResponse()) {
            vecBeans = response.getDataObjects();
        }
        return vecBeans;
    }
     
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JTable tblCommittee;
    private javax.swing.JButton btnOk;
    // End of variables declaration//GEN-END:variables
    
}
