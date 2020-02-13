/*
 * @(#)CommitteeScheduleActiveMembers.java 
 * Created on Nov 25, 2002, 8:41 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;

import java.util.Vector;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
//import edu.mit.coeus.utils.ScreenFocusTraversalPolicy;

import edu.mit.coeus.irb.bean.CommitteeActiveMemberBean;

/**
 * This is Reusable Component used to Show all the Active Member of a Specific
 * committee. This component will allow the user to select (single/multiple)
 * based on instantiation parameter of this component. It provides a method
 * to get all the Active Selected Members with MemberID, MemberName, isEmployee
 * Flag, comment as Vector of Vector Objects.
 *
 * @author  subramanya 
 */
public class CommitteeScheduleActiveMembers extends javax.swing.JComponent 
                                    implements ActionListener {
    
     //holds the collection of Active Member List
     private Vector activeMemberList = null;
     
     //holds the committee ID
     private String committeeId = null;
     
     private String scheduleId;
          
     //holds Component Dilaog Window
     private JDialog dlgMembers = null;
    
     //holds the selection Mode EX:- true - single selection.
     private boolean isSingleSelection = true;
     
     //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    
    //holds the constant value for Table Row height
    private static final int TABLE_ROW_HEIGHT = 22;
    
    //holds the Dialog window width
    private static final int DLG_WINDOW_WIDTH = 500;
    
    //holds the Dialog window hegith
    private static final int DLG_WINDOW_HEIGHT = 350;
    
    //HOLDS The Zero Count
    private static final int ZERO_COUNT = 0;
    
    //HOLDS the member id column width
    private static final int MEMBER_ID_COLUMN_WIDTH = 120;
    
    
    //holds the server call Function Type 
    private static final char FUNCTION_PARAM_TYPE = 'Z';
    
    
    /** 
     * Creates new form CommitteeScheduleActiveMembers. This is a reusable 
     * Component used to create the Dialog window which will show all the 
     * Active Members of the Sepcific Committee. This component provides 
     * Single or mulitple selection of the Active members concluded with
     * ok and cancle button.
     * @param committeeID String represent the committee ID
     * @param membersDlg JDialog window component
     * @param selectionMode True for Multiple Selection false for single select.
     */
    public CommitteeScheduleActiveMembers( String committeeID,
                                           String scheduleID, 
                                           JDialog membersDlg,
                                           boolean selectionMode ) {
        this.committeeId = committeeID;
        this.scheduleId = scheduleID;
        this.dlgMembers = membersDlg;  
        this.isSingleSelection = selectionMode;        
        initComponents();        
        postInitComponents();        
        dlgMembers.setSize( new Dimension(DLG_WINDOW_WIDTH, DLG_WINDOW_HEIGHT ) ); 
        // Added by chandra
        dlgMembers.setResizable(false);
        coeusMessageResources = CoeusMessageResources.getInstance();        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnMembers = new javax.swing.JScrollPane();
        tblMembersList = new javax.swing.JTable();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        scrPnMembers.setPreferredSize(new java.awt.Dimension(400, 300));
        scrPnMembers.setMinimumSize(new java.awt.Dimension(400, 300));
        scrPnMembers.setMaximumSize(new java.awt.Dimension(150, 150));
        tblMembersList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member ID", "Member Name", "PersonID", "IsEmpoyee", "Comment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMembersList.setFont(CoeusFontFactory.getNormalFont());
        tblMembersList.setPreferredScrollableViewportSize(new java.awt.Dimension(0, 0));
        tblMembersList.setMinimumSize(new java.awt.Dimension(100, 100));
        scrPnMembers.setViewportView(tblMembersList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(scrPnMembers, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setText("OK");
        btnOk.setPreferredSize(new java.awt.Dimension(73, 23));
        btnOk.setMaximumSize(new java.awt.Dimension(73, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(73, 23));
        btnOk.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        add(btnCancel, gridBagConstraints);

    }//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable tblMembersList;
    private javax.swing.JButton btnOk;
    private javax.swing.JScrollPane scrPnMembers;
    private javax.swing.JButton btnCancel;
    // End of variables declaration//GEN-END:variables
 
    
    //supporting method to initalize the components after init
    private void postInitComponents(){
        
        Vector activeMemberTableData = new Vector();
        
        java.awt.Component[] components = 	
	{tblMembersList,btnOk,btnCancel};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);

        //check for the committe id and creates the active member table
        if( committeeId != null ){
            try{
                activeMemberList = getAllActiveMembersOfCommittee( committeeId );  
                activeMemberTableData = getActiveMemberTableData();
            }catch( Exception err ){
                CoeusOptionPane.showErrorDialog( err.getMessage());
            }            
        }        
        
        //construct the active meber list
        if( activeMemberList != null && activeMemberList.size() > ZERO_COUNT ){
          
            ((DefaultTableModel)tblMembersList.getModel()).setDataVector( 
                                            activeMemberTableData, 
                                            getMemberTableColumnNames());
            btnOk.setEnabled( true );
            tblMembersList.getSelectionModel().setSelectionInterval(ZERO_COUNT,
                                           tblMembersList.getColumnCount() -1 );
            //setting the selection mode - single or mulitple
            if( this.isSingleSelection ){
                tblMembersList.getSelectionModel().setSelectionMode( 
                                          ListSelectionModel.SINGLE_SELECTION );
            }else{
                tblMembersList.getSelectionModel().setSelectionMode( 
                               ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
            }
            
            tblMembersList.setRowHeight( TABLE_ROW_HEIGHT );
            tblMembersList.getTableHeader().setFont(
                                            CoeusFontFactory.getLabelFont());                     
            tblMembersList.clearSelection();
            
        }else{
            ((DefaultTableModel)tblMembersList.getModel()).setDataVector( 
                                            new Vector(), 
                                            getMemberTableColumnNames());
        }
        TableColumn idColumn = tblMembersList.getColumn( "Member ID" );
            idColumn.setMaxWidth( MEMBER_ID_COLUMN_WIDTH );
            idColumn = tblMembersList.getColumn( "Person ID" );
            idColumn.setMinWidth( ZERO_COUNT );
            idColumn.setPreferredWidth( ZERO_COUNT );
            idColumn.setMaxWidth( ZERO_COUNT );
            idColumn = tblMembersList.getColumn( "is Employee" );
            idColumn.setMinWidth( ZERO_COUNT );
            idColumn.setPreferredWidth( ZERO_COUNT );
            idColumn.setMaxWidth( ZERO_COUNT );
            idColumn = tblMembersList.getColumn( "Comment" );
            idColumn.setMinWidth( ZERO_COUNT );
            idColumn.setPreferredWidth( ZERO_COUNT );
            idColumn.setMaxWidth( ZERO_COUNT );
            
        JTableHeader header = tblMembersList.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);        
        btnOk.addActionListener( this );
        btnOk.setMnemonic( 'O' );
        btnCancel.addActionListener( this );
        btnCancel.setMnemonic( 'C' );
        tblMembersList.addKeyListener( new KeyAdapter() {
                public void keyPressed( KeyEvent keyEvent  ){
                    if( keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE ){
                        dlgMembers.dispose();
                    }
                }
            });
    }
    
    /**
     * This method is over ridden to capture the Action Event fired from the Ok/
     * cancel button. 
     * @param actionEvent  ActionEvent instance.
     */
     public void actionPerformed( ActionEvent actionEvent) {
        Object sourceAction = actionEvent.getSource();
        
        //ok button ask for confirmation of selection if not active member 
        //selected
        if( sourceAction.equals( btnOk ) ){
            
            int selectedNumRows = tblMembersList.getSelectedRowCount();
            if( selectedNumRows < 1 ){
                CoeusOptionPane.showWarningDialog(
                                   coeusMessageResources.parseMessageKey(
                                        "commSchedAttnFrm_exceptionCode.1602"));            
            return;            
            }
            //cancel clear the selection 
        }else if( sourceAction.equals( btnCancel ) ){
            tblMembersList.clearSelection();
        }
        //both ok/cancel action disposes the window
        dlgMembers.dispose();
    } 
     
    
    //supporting method to get the column Names
    private Vector getMemberTableColumnNames(){
        
        Vector memberColumnNames = new Vector();        
        memberColumnNames.add( "Member ID" );
        memberColumnNames.add( "Member Name" );
        memberColumnNames.add( "Person ID" );//for person id empty display
        memberColumnNames.add( "is Employee" );//for non emp. flag
        memberColumnNames.add( "Comment" );//for comment.
        
        return memberColumnNames;
    }
    
    
    /** The Method makes a server side call to get all the Active Members 
     * of the Committee.
     * @param committeeID String represent the committee ID
     * @return Vector collection of CommitteeActiveMemberBean.
     * @exception throws when any delegated Error occurred during db access.
     */
     public Vector getAllActiveMembersOfCommittee( String committeeID ) 
                                                            throws Exception{

        String connURL = CoeusGuiConstants.CONNECTION_URL +
                                                     "/AgendaServlet";
        Vector committeeMemberList  = new Vector(3,2);        
        // connect to the database and get the formData 
        RequesterBean request = new RequesterBean();
        request.setFunctionType( FUNCTION_PARAM_TYPE );        
        //request.setId( committeeID );        
        //request.setDataObject(request);
        // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_start
        Vector vecInputData = new Vector();
        vecInputData.add(0,CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
        vecInputData.add(1,committeeId);
        vecInputData.add(2,scheduleId);
        request.setDataObject(vecInputData);
        // Modified for COEUSQA-2686 IACUC - Changes to schedule maintenance for 4.4.3 release_end
        //makes a server side call to get the active member list
        AppletServletCommunicator comm = new AppletServletCommunicator(
                                                    connURL, request );
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response.isSuccessfulResponse() ){
            committeeMemberList = (Vector)response.getDataObject();            
        }else{
            throw new Exception(response.getMessage());
        }
        return committeeMemberList ;        
    }
     
    
    /**
     * The Method is used to get all the selected members. This method is 
     * invoked after this Active Member List Dailog is closed.
     * @return Vector collection of Selected Member Details.
     */
    public Vector getSelectedMembers(){
        Vector selectedActiveMebmers = new Vector();
        int selectedRows[] = tblMembersList.getSelectedRows();  
        Vector localActiveMembers = activeMemberList;
        //iterate through to get all the selected active members after ok
        for( int indx = ZERO_COUNT; indx < selectedRows.length; indx++ ){                
            selectedActiveMebmers.add( localActiveMembers.get( 
                                                    selectedRows[ indx ] ) );
        }
        return selectedActiveMebmers;
    }
    
    //supporting method to get the table data from com. active member bean
    private Vector getActiveMemberTableData(){
        Vector actMemTableData = new Vector();
        Vector localActiveMembers = activeMemberList;
        if( localActiveMembers != null ){
            int totalNoOfRows = activeMemberList.size();
            CommitteeActiveMemberBean comActMemberBean = null;
            Vector memberRowData = null;
            //iterate throught to construct the table data of active member.
            for( int indx = ZERO_COUNT; indx < totalNoOfRows; indx++ ){
              comActMemberBean = ( CommitteeActiveMemberBean ) 
                                    localActiveMembers.get( indx );   
              memberRowData = new Vector();
              memberRowData.add( comActMemberBean.getMemberID() );
              memberRowData.add( comActMemberBean.getPersonName() );
              memberRowData.add( comActMemberBean.getPersonID() );
              memberRowData.add( new Boolean( comActMemberBean.isEmployee() ));
              memberRowData.add( comActMemberBean.getComments() );
              memberRowData.add( new Boolean(comActMemberBean.isAlternatePerson() ));
              actMemTableData.add( memberRowData);
            }
        }
        return actMemTableData;
    }
    
    /** Getter for property scheduleId.
     * @return Value of property scheduleId.
     */
    public java.lang.String getScheduleId() {
        return scheduleId;
    }
    
    /** Setter for property scheduleId.
     * @param scheduleId New value of property scheduleId.
     */
    public void setScheduleId(java.lang.String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    public void requestDefaultFocusForComponent(){
        if( tblMembersList.getRowCount() > 0 ) {
            tblMembersList.requestFocusInWindow();
        }else{
            btnCancel.requestFocusInWindow();
        }
    }
    public void clearMemberSelection(){
        tblMembersList.clearSelection();
    }
}
