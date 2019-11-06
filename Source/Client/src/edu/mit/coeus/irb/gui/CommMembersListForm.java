/*
 * @(#)CommMembersListForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.irb.gui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.Date;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.irb.bean.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.exception.*;



/** <code> CommMembersListForm </code> is a class  used to display all the
 * memebers of the selected Committee depending on their status like active/inactive.
 *
 * @author Lenin Fernandes
 * @date 21-09-02
 * @since 1.0
 * @version 1.0
 * @modified by Phaneendra Kumar
 * @date 03-10-02
 * @version 12.0
 * @modified by Ravikanth
 * @date 04-10-02
 * @version 14.0
 */
public class CommMembersListForm extends javax.swing.JComponent {
    
    private javax.swing.JTable tblMembersList;
    private javax.swing.JScrollPane scrPnMembersList;
    private javax.swing.JButton btnShowAll;
    private javax.swing.JButton btnShowActive;
    private javax.swing.JButton btnAdd;
    //Vinay Start
    private javax.swing.JButton btnDelete;
    //Vinay End
    private javax.swing.JButton btnDetails;
    private javax.swing.JLabel lblCommitteeMembers;
    private CoeusComboBox cmbMemberType;
    private CommitteeMembershipDetailsBean commMemberDetails;
    private Vector colNames;
    private Vector members;
    // this stores all the members added to committee.
    private Hashtable membersList = new Hashtable();
    private Hashtable newMembersList = new Hashtable();
    private char functionType;
    private Vector membershipTypes;
    private DateUtils dtUtils = new DateUtils();
    private CoeusAppletMDIForm parent;
    private String committeeId = "";
    private boolean saveRequired = false;
    //commented and added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - End
//    private boolean showDetails = true;
    public boolean showDetails = true;
    //commented and added for case # 3229 - Inability to modify terms for Members with lapsed term dates  - End
    private boolean windowActivate;    
    //holds CoeusMessageResources instance used for reading message Properties.
    private CoeusMessageResources coeusMessageResources;
    //Adde by Geo to save the data before clicking on Detail button
    private CommitteeDetailsForm commDetailForm;
    
    private String connectionURL = CoeusGuiConstants.CONNECTION_URL;
    CoeusDlgWindow dialog;
    MemberDetailsForm detform;
    //Vinay  Start
    private Vector vecDeleteCommMembers;
    private static final String DELETE_CONFIRM = "memMntFrm_exceptionCode.1051";
    private static final String PROTOCOL_REVIEWER = "memMntFrm_exceptionCode.1052";
    private static final String SCHEDULE_MEETING = "memMntFrm_exceptionCode.1053";
    //Vinay End
    
    /** Creates new form <CODE>CommMembersListForm</CODE>
     */
    public CommMembersListForm() {
        displayMembersList();
        if( tblMembersList.getRowCount() > 0 ) {
            tblMembersList.setRowSelectionInterval(0,0);
        }
        
        //Added By Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
        //Disable Details button since nothing to view
        btnAdd.setEnabled(false);
        //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
        
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    /** Creates new form <CODE>CommMembersListForm</CODE> with specified function type and
     * list of Committee members
     * @param functionType which specifies the form opened mode.
     * @param commMembersList Hashtable with row number of committee members table
     * as key and <CODE>CommitteeMembershipDetailsBean</CODE> as value.
     */
    public CommMembersListForm(char functionType,Hashtable commMembersList) {
        this.membersList = commMembersList;
        this.functionType  = functionType;
        coeusMessageResources = CoeusMessageResources.getInstance();
    }
    
    
    /** This method is used to return <CODE>CommMembersListForm</CODE> component to the
     * <CODE>CommitteeDetailsForm</CODE>.This is to show the <CODE>CommMembersListForm</CODE> component.
     *
     * @param parent reference to <CODE>CoeusAppletMDIForm</CODE>.
     * @return JComponent reference to <CODE>CommMembersListForm</CODE> after initializing
     * the component and setting the data if available.
     */
    public JComponent getMembersListForm(CoeusAppletMDIForm parent){
        this.parent = parent;
        displayMembersList();
        if( tblMembersList.getRowCount() > 0 ) {
            tblMembersList.setRowSelectionInterval(0,0);
        }
        formatFields();
        return this;
    }
    
    /** This method is used to set the values in this component from the
     * database.
     *
     * @param memberList Hashtable which consists of <CODE>CommitteeMembershipDetailsBean</CODE>s
     */
    
    public void setValues(Hashtable memberList){
        /* This gets the members hash table from CommitteeDetails form and
         * init the Members in this component.
         */
        int selectedRow=0;
        int countAfter,countBefore;
        countBefore=tblMembersList.getRowCount();
        if(countBefore >0){
            selectedRow=tblMembersList.getSelectedRow();
        }

        membersList = memberList;
        initMembers(membersList);
        setNewModel();
        formatFields();
        newMembersList = null;
        saveRequired = false;
        /*if( tblMembersList.getRowCount() > 0 ) {
            tblMembersList.setRowSelectionInterval(0,0);
        }*/
        
        countAfter=tblMembersList.getRowCount();
        if(countAfter>0){
            if(countAfter>selectedRow){
                
            tblMembersList.setRowSelectionInterval(selectedRow, selectedRow);
            }
            else{
                tblMembersList.setRowSelectionInterval(0, 0);
            }
        }
    }
    
    public void setDefaultFocusToComponent(){
        if( functionType != 'D' ) {
            if( tblMembersList.getRowCount() > 0 ) {
                tblMembersList.requestFocusInWindow();
                int rowNum = tblMembersList.getSelectedRow();
                if(rowNum > 0){                    
                    tblMembersList.setRowSelectionInterval(rowNum,rowNum);                
                }                
                tblMembersList.setColumnSelectionInterval(1,1);
                btnAdd.requestFocusInWindow();
            }else{
                btnShowAll.requestFocusInWindow();
            }
        }
        else if(functionType == 'D'){
             btnShowAll.requestFocusInWindow();
             btnDelete.setEnabled(false);
        }
        
        //raghuSV : settings in Add mode.
        //starts..
        if( functionType=='A'){
            btnAdd.requestFocusInWindow();
            btnShowAll.setEnabled(false);
        }
        //ends
    }
    
    /** This is used to set the <CODE>saveRequired</CODE> flag for this component. <CODE>saveRequired</CODE>
     * flag will be used to confirm the user for saving the modified information.
     *
     * @param saveRequired boolean true if the changes made are to be saved
     * else false.
     */
    public void setSaveRequired(boolean saveRequired){
        this.saveRequired = saveRequired;
    }
    
    /** This method is used to get the <CODE>saveRequired</CODE> flag from this component to
     * prompt the user before closing the form , for saving information if any
     * changes have been done.
     *
     * @return saveRequired boolean true if changes have been done else false.
     */
    public boolean isSaveRequired(){
        return saveRequired;
    }
    
    /** This method is used to get the modified/new members information
     * to the <CODE>CommitteeDetailsForm</CODE> to save the information to the database.
     * @return Vector of <CODE>CommitteeMembershipDetailsBean</CODE>.
     */
    public Vector getCommitteeMembersList(){
        
        Vector membList = new Vector();
        //bug fix case 2538 check for null
        if(membersList!=null && membersList.size() > 0){
            for (int i=0;i< membersList.size(); i++){
                CommitteeMembershipDetailsBean detail =
                (CommitteeMembershipDetailsBean) membersList.get(new Integer(i));
                if(detail!=null){
                    if(!detail.getMemberRolesModified()){
                        detail.setMemberRoles(null);
                    }
                    if(!detail.getMemberStatusModified()){
                        detail.setStatusInfo(null);
                    }
                    if(!detail.getMemberExpertiseModified()){
                        detail.setMemberExpertise(null);
                    }
                    membList.addElement(detail);
                }
            }
        }
        //delete commitee Start
        if(vecDeleteCommMembers!=null && vecDeleteCommMembers.size() > 0){
            for(int index = 0 ;index < vecDeleteCommMembers.size();index ++){
                CommitteeMembershipDetailsBean commMemberDetailsBean =
                (CommitteeMembershipDetailsBean)vecDeleteCommMembers.get(index);
                membList.addElement(commMemberDetailsBean);
            }
        }
        //delete commitee End        
        return membList;
    }
    
    /**
     * This is used to set the membersList table with the members retrieved
     * from the database.
     */
    private void  initMembers(Hashtable memberlist){
        Vector tempMembers = new Vector();
        Vector tempMemberDetail;
        //bug fix case 2538
        if(memberlist!=null && memberlist.size() > 0){
            for (int i=0;i< memberlist.size(); i++){
                tempMemberDetail = new Vector();
                CommitteeMembershipDetailsBean detail =
                (CommitteeMembershipDetailsBean) memberlist.get(new Integer(i));
                tempMemberDetail.addElement("");
                tempMemberDetail.addElement(detail.getPersonName());
                tempMemberDetail.addElement(dtUtils.formatDate(
                detail.getTermStartDate().toString(),"dd-MMM-yyyy"));
                tempMemberDetail.addElement(dtUtils.formatDate(
                detail.getTermEndDate().toString(),"dd-MMM-yyyy"));
                CommitteeMemberStatusChangeBean status = detail.getStatusInfo();
                if(status !=null){
                    tempMemberDetail.addElement(status.getStatusDescription());
                }else{
                    tempMemberDetail.addElement(
                    detail.getStatusDescription() == null ?"":
                        detail.getStatusDescription());
                }
                tempMemberDetail.addElement(detail.getMembershipTypeDesc());
                tempMembers.addElement(tempMemberDetail);
                
            }
        }//bug fix case 2538
        setMembers(tempMembers);
        
        //Added By sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
        //btnDetails.setEnabled(tempMembers.size() > 0);
        //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
    }
    
    
    /*
     * This is used to display this component with the existing members
     * information.
     */
    private void displayMembersList(){
        initMembers(membersList);
        initComponents();
        setNewModel();
        tblMembersList.getTableHeader().setFont(
        CoeusFontFactory.getLabelFont());
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblMembersList.setBackground(bgListColor);
        }
        else{
            tblMembersList.setBackground(Color.WHITE);
        }
        //End Amit
    }
    
    /**
     * This is used to initialize the memberslist component with the available
     * information
     */
    private void initComponents() {
        lblCommitteeMembers = new javax.swing.JLabel();
        Vector cols = new Vector();
        cols.addElement("Icon");
        cols.addElement("Name");
        cols.addElement("Term Start");
        cols.addElement("Term End");
        cols.addElement("Status");
        cols.addElement("Type");
        setColumnNames(cols);
        //Vinay Start
        vecDeleteCommMembers = new Vector();
        //Vinay End
        java.awt.GridBagConstraints gridBagConstraints;
        
        //        setPreferredSize(new java.awt.Dimension(750, 250));
        //        setMinimumSize(new java.awt.Dimension(750, 250));
        //        setMaximumSize(new java.awt.Dimension(750, 250));
        // Added by chandra - 29-8-2003 - To increase the scrollpane size
        setPreferredSize(new java.awt.Dimension(788, 480));
        setMinimumSize(new java.awt.Dimension(788, 480));
        setMaximumSize(new java.awt.Dimension(788, 480));
        // End chandra
        scrPnMembersList = new javax.swing.JScrollPane();
        tblMembersList = new javax.swing.JTable();
        tblMembersList.setFont(CoeusFontFactory.getNormalFont());
        
        //Added by Amit 11/18/2003
        if(functionType == CoeusGuiConstants.DISPLAY_MODE){
            java.awt.Color bgListColor = (java.awt.Color)javax.swing.UIManager.getDefaults().get("Panel.background");
            
            tblMembersList.setSelectionBackground(bgListColor);
            tblMembersList.setSelectionForeground(Color.black );
        }
        else {
            tblMembersList.setSelectionBackground( Color.white );
            tblMembersList.setSelectionForeground( Color.black );
        }
        //end Amit
        
        // Display the member Details
        tblMembersList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me){
                if (me.getClickCount() == 2) {
                    setMemberDetails();
                }
            }
            
        } );
        btnAdd = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        btnShowActive = new javax.swing.JButton();
        btnDetails = new javax.swing.JButton();
         //Vinay Start
        btnDelete = new javax.swing.JButton();
        //Vinay End
        setLayout(new java.awt.GridBagLayout());
        
        //        scrPnMembersList.setPreferredSize(new java.awt.Dimension(650, 200));
        //        scrPnMembersList.setMinimumSize(new java.awt.Dimension(650, 200));
        //        scrPnMembersList.setMaximumSize(new java.awt.Dimension(650, 200));
        // Added by Chandra - To increase the scrollpane size.
        scrPnMembersList.setPreferredSize(new java.awt.Dimension(670, 420));
        scrPnMembersList.setMinimumSize(new java.awt.Dimension(670, 420));
        scrPnMembersList.setMaximumSize(new java.awt.Dimension(670, 420));
        // End Chandra
        
        tblMembersList.setModel(new javax.swing.table.DefaultTableModel(
        getMembers(), getColumnNames()){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        });
        
        tblMembersList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
        scrPnMembersList.setViewportView(tblMembersList);
        tblMembersList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblMembersList.setShowHorizontalLines(false);
        tblMembersList.setShowVerticalLines(false);
        tblMembersList.setOpaque(false);
        tblMembersList.setRowHeight(22);
        JTableHeader header = tblMembersList.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        setTableColumnWidths();
        lblCommitteeMembers.setFont(CoeusFontFactory.getLabelFont());
        lblCommitteeMembers.setText("Members for this committee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 10, 0);
        add(lblCommitteeMembers, gridBagConstraints);
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        add(scrPnMembersList, gridBagConstraints);
        
        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        /*
         * This opens the memberDetails form in New mode for adding the
         * new member information.
         */
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(authorizedToPerform('I')){                    
                    showMemberDetails('A',null,-1);
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 5, 0, 8);
        add(btnAdd, gridBagConstraints);
        
        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setMnemonic('S');
        // Added by chandra 20/11/2003
        btnShowAll.setText("Show All");
        btnShowAll.setPreferredSize(new Dimension(110,25)); 
        btnShowAll.setMinimumSize(new Dimension(110,25)); 
        /* This is to get all the Members for this committee and display in the
         * commMembers table.
         */
        btnShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(showDetails){
                        showDetails = false;
                        if(saveChanges()) {
                            membersList = getAllMembersList();
                            //Added by sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                            btnDetails.setEnabled(membersList.size() > 0);
                            //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
                            initMembers(membersList);
                            setNewModel();
                            btnShowAll.setText("Show Active");
                            //bY Default Select 1st Row. Updated Subramanya
                            if( tblMembersList.getRowCount() > 0 ){
                                     tblMembersList.setRowSelectionInterval(  0, 0 );
                            }
                        }
                }else{
                        btnShowAll.setText("Show All");
                        if( saveChanges() ) {
                            membersList = getCurrentActiveMembers();
                            btnDetails.setEnabled(membersList.size() > 0);
                            initMembers(membersList);
                            setNewModel();
                            showDetails = true;
                            //bY Default Select 1st Row.
                            if( tblMembersList.getRowCount() > 0 ){
                                tblMembersList.setRowSelectionInterval(  0, 0 );
                            }
                        }
//                //bY Default Select 1st Row. Updated Subramanya
//                if( tblMembersList.getRowCount() > 0 ){
//                    tblMembersList.setRowSelectionInterval(  0, 0 );
//                }
//                btnShowAll.setVisible(false);
//                btnShowActive.setVisible(true);
//                showDetails = false;
            }
        }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        add(btnShowAll, gridBagConstraints);
        
//        btnShowActive.setFont(CoeusFontFactory.getLabelFont());
//        btnShowActive.setMnemonic('A');
//        btnShowActive.setText("Show Active");
        
        // Added by chandra - If click on btnShowActive will display only the 
        // active members list.
//        btnShowActive.addActionListener(new java.awt.event.ActionListener(){
//            public void actionPerformed(java.awt.event.ActionEvent evt){
//                // Confirmation message if the user enters the person details ..
//                showConfirmationMessage();
//                membersList = getCurrentActiveMembers();
//                btnDetails.setEnabled(membersList.size() > 0);
//                initMembers(membersList);
//                setNewModel();
//                //bY Default Select 1st Row.
//                if( tblMembersList.getRowCount() > 0 ){
//                    tblMembersList.setRowSelectionInterval(  0, 0 );
//                }
//                btnShowAll.setVisible(true);
//                btnShowActive.setVisible(false);
//                showDetails=true;
//            }
//        });
//        
//        
//        gridBagConstraints = new java.awt.GridBagConstraints();
//        gridBagConstraints.gridx = 1;
//        gridBagConstraints.gridy = 2;
//        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
//        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
//        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
//        add(btnShowActive, gridBagConstraints);
        
        
        btnDetails.setFont(CoeusFontFactory.getLabelFont());
        btnDetails.setMnemonic('D');
        btnDetails.setText("Details");        
        
        /* This will set the memberDetails form with the selected member
         * information
         */
        btnDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(authorizedToPerform('N')){     
                    setMemberDetails();      
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        add(btnDetails, gridBagConstraints);
         //Vinay Start        
        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('l');
        btnDelete.setText(coeusMessageResources.parseLabelKey("committeMembers.delete.1109"));
        /* This will set the memberDetails form with the selected member
         * information
         */
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(authorizedToPerform('N')){
                    int yesNo=CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(DELETE_CONFIRM), 2, 3);
                    if(yesNo==CoeusOptionPane.SELECTION_YES){
                        int canDelete = canDeleteCommitee();
                        if(canDelete ==1){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(PROTOCOL_REVIEWER));
                            return;
                        }else if(canDelete == 2){
                            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SCHEDULE_MEETING));
                            return;
                        }else{
                            deleteMemberDetails();
                        }
                    }
                }
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 8);
        add(btnDelete, gridBagConstraints);
        //Vinay End
        // Added by Chandra 12/09/2003
       // java.awt.Component[] components = {tblMembersList,btnAdd,btnShowAll,btnDetails};
        java.awt.Component[] components = {tblMembersList,btnAdd,btnShowAll,btnDetails,btnDelete};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        // End Chandra
    }
    
    private boolean saveChanges(){
        if(saveRequired){
            int option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,CoeusOptionPane.DEFAULT_CANCEL);
            if(option == CoeusOptionPane.SELECTION_YES){
                ((CoeusInternalFrame)parent.getSelectedFrame()).saveActiveSheet();
            }else if(option == CoeusOptionPane.SELECTION_NO){
                setSaveRequired(false);
            }else if(option==CoeusOptionPane.SELECTION_CANCEL){
                return false;
            }
        }
        return true;
    }
    
    private boolean authorizedToPerform(char functionType){
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setId(committeeId) ; //prps added this jan 16 2004
        request.setFunctionType(functionType);
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response.isSuccessfulResponse()){
            return true;
        }else{
            if(response.getDataObject() != null){
                CoeusOptionPane.showDialog(new CoeusClientException
                ((CoeusException)response.getDataObject()));
            }else{
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
        }
        System.out.println(" Server Side Error ");
        return false;
    }
    
    private void setTableColumnWidths(){
        TableColumn column = tblMembersList.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new EmptyHeaderRenderer());
        column.setCellRenderer(new IconRenderer());
        column.setPreferredWidth(30);
        column.setMaxWidth(30);
        column.setMinWidth(30);
        
        column = tblMembersList.getColumnModel().getColumn(1);
        column.setPreferredWidth(220);
        column.setMinWidth(220);
        
        column = tblMembersList.getColumn("Term Start");
        column.setPreferredWidth(80);
        column.setMinWidth(80);
        
        column = tblMembersList.getColumn("Term End");
        column.setPreferredWidth(80);
        column.setMinWidth(80);
        
        column = tblMembersList.getColumn("Status");
        column.setPreferredWidth(80);
        column.setMinWidth(80);
        
        column = tblMembersList.getColumn("Type");
        column.setPreferredWidth(150);
        column.setMinWidth(150);
        
        
    }
    /**
     * This methos is used to refresh the commMemberslist table with the new
     * members list information.
     */
    private void setNewModel(){
        ((DefaultTableModel)tblMembersList.getModel()).setDataVector(
        getMembers(), getColumnNames());
        ((DefaultTableModel)tblMembersList.getModel()).fireTableDataChanged();
        tblMembersList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        tblMembersList.setShowHorizontalLines(false);
        tblMembersList.setShowVerticalLines(false);
        tblMembersList.setOpaque(false);
        tblMembersList.setRowHeight(22);
        
        JTableHeader header = tblMembersList.getTableHeader();
        header.setReorderingAllowed(false);
        //header.setResizingAllowed(false);
        setTableColumnWidths();
        
        //Added By Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
        btnDetails.setEnabled(tblMembersList.getRowCount() > 0);
        //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
        //Added by Vinay 24/8/2006 start
        btnDelete.setEnabled(tblMembersList.getRowCount() > 0);
        //Vinay End
    }
    
    /** Gets the default data that is shown in table of this panel in a Vector
     * This will be called from <CODE>CommitteeDetailsForm</CODE> component.
     *
     * @return Vector of <CODE>CommitteeMembershipDetailsBean</CODE>.
     */
    public  Vector getMembers(){
        return this.members;
    }
    
    /** Sets the members list to this component. This will be called from
     * <CODE>CommitteeDetailsForm</CODE> to set the data to this form.
     *
     * @param members Vector of <CODE>CommitteeMembershipDetailsBean</CODE>.
     */
    public  void setMembers(Vector members){
        this.members = members;
    }
    
    
    /** Sets the <CODE>functionType</CODE> to this component. This will be called from
     * <CODE>CommitteeDetailsForm</CODE> to set the <CODE>functionType</CODE> to this form.
     *
     * @param functionType character which specifies the form opened mode.
     */
    public void setFunctionType(char functionType){
        this.functionType =functionType;
    }
    
    /** Gets the <CODE>functionType</CODE> from this component. This will be called from
     * <CODE>CommitteeDetailsForm</CODE> to get the <CODE>functionType</CODE> to this form.
     * @return functionType character which specifies the form opened mode.
     */
    public char getFunctionType(){
        return this.functionType;
    }
    
    /** Sets the <CODE>committeeId</CODE> to this component. This will be called from
     * <CODE>CommitteeDetailsForm</CODE> to set the <CODE>functionType</CODE> to this form. This
     * can be used for Server communication to retrieve information from
     * the database.
     *
     * @param committeeId String representing the Committee ID.
     */
    public void setCommitteeId(String committeeId){
        this.committeeId = committeeId;
    }
    
    /** Gets the <CODE>committeeId</CODE> from this component. This will be called from
     * <CODE>CommitteeDetailsForm</CODE> to get the <CODE>committeeId</CODE> from this form.
     *
     * @return String representing the Committee ID.
     */
    public String getCommitteeId(){
        return committeeId;
    }
    
    /** Gets the column names of the table in this panel as a Vector.
     *
     * @return Vector which consists of the column names of the table which is
     * used to display the members.
     */
    public Vector getColumnNames(){
        return this.colNames;
    }
    
    /** Sets the column names that are required for the table used to display the
     * members.
     * @param colNames Vector consisting of column names.
     */
    public void setColumnNames(Vector colNames){
        this.colNames = colNames;
    }
    
    
    /**
     * This is used to get the all the members list in this committee from the
     * database. This will be called on clicking 'ShowActive' button in this form.
     * This will communicatte with the committeeMaintenance servlet with
     * functionType 'L' to retrive all the members for this committee.
     */
    private  Hashtable getAllMembersList(){
        
        Hashtable membs = new Hashtable();
        Vector cols  = new Vector();
        Vector roles ;
        CommitteeMembershipDetailsBean  memberDetail;
        CommitteeMemberStatusChangeBean status;
        CommitteeMemberRolesBean roleBean;
        Hashtable htAllMembers = null ;
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        /* connect to the database and get all members list for the given
         * committee id
         */
        RequesterBean request = new RequesterBean();
        request.setFunctionType('L');
        request.setId(committeeId);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        Vector vecData = new Vector();                
        vecData.add(0,CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
        request.setDataObjects(vecData);
        // modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm =
        new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector members = null;
        if (response.isSuccessfulResponse()) {
            members = (Vector)response.getDataObject();
        }
        if(members!=null){
            htAllMembers = new Hashtable();
            /*
             *To fix the bug #2560
             */
            
//            membersList.clear();
             
            if( newMembersList != null ) {
                newMembersList.clear();
            }
            for(int memberRow=0;memberRow<members.size();memberRow++){
                memberDetail = (CommitteeMembershipDetailsBean)
                members.elementAt(memberRow);
                htAllMembers.put(new Integer(memberRow),memberDetail);
            }
        }
        return htAllMembers;
    }
    
    
    
    /**
     * This is used to get the all the members list in this committee from the
     * database. This will be called on clicking 'ShowActive' button in this form.
     * This will communicatte with the committeeMaintenance servlet with
     * functionType 'K' to retrive all the members for this committee.
     */
    // Added by chandra to get all active member list..
    
    private  Hashtable getCurrentActiveMembers(){
        
        Hashtable membs = new Hashtable();
        Vector cols  = new Vector();
        Vector roles ;
        CommitteeMembershipDetailsBean  memberDetail;
        CommitteeMemberStatusChangeBean status;
        CommitteeMemberRolesBean roleBean;
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/comMntServlet";
        /* connect to the database and get all members list for the given
         * committee id
         */
        RequesterBean request = new RequesterBean();
        request.setFunctionType('K');
        request.setId(committeeId);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start  
        request.setDataObject(CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm =
        new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        //Vector members = null;
        if (response.isSuccessfulResponse()) {
            //activeMembers = (Vector)response.getDataObjects();
            members = (Vector)response.getDataObjects();
        }
        if(members!=null){
            membersList.clear();
            if( newMembersList != null ) {
                newMembersList.clear();
            }
            for(int activeMemberRow=0;activeMemberRow<members.size();activeMemberRow++){
                memberDetail = (CommitteeMembershipDetailsBean)
                members.elementAt(activeMemberRow);
                membersList.put(new Integer(activeMemberRow),memberDetail);
            }
        }
        return membersList;
    }
    
    
    
    
    /**
     * This will set all the fields in this form based on the functionType
     */
    private void formatFields(){
        if( functionType == 'D'){
            btnAdd.setEnabled(false);
        }else if (functionType == 'A'){
            btnDetails.setEnabled(false);
        }
    }
    
    private void showMemberDetails(char functionType,
    CommitteeMembershipDetailsBean detail,int selectedRow){        
        dialog = new CoeusDlgWindow(parent, "Membership Details", true);
        detform = new MemberDetailsForm(dialog,detail,functionType);  
        //for duplicate personId bugfix
        detform.setAllMemberList(getAllMembersList());
        //for duplicate personId bugfix
        detform.setAvailableMembers(membersList);
        if(selectedRow != -1){
            detform.setCurrentMember(selectedRow);
            if(detail.getMembershipId() == null
            || detail.getMembershipId().trim().length() == 0){
                detform.setMemberSaved(false);
            }else{
                detform.setMemberSaved(true);
            }
            
        }
        dialog.getContentPane().add(detform.createMemberDetails(parent));
        dialog.setResizable(false);
        dialog.pack();
        Dimension screenSize
        = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dialog.getSize();
        dialog.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
           /* This is a window listener attached to the Dialog
            * MemberDetails form.This will handle the window closing
            * operation.Before closing the window if any unsaved
            * changes exists it will prompt the user for confirmation
            * of saving.If any changes exists it sets the saveRequired 'true'
            */
        windowActivate = false;
        dialog.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){           
                if( !windowActivate ) {
                    detform.requestDefaultFocusForComponent();
                    windowActivate = true;
                }
            }
            public void windowClosing(WindowEvent we){
                // Added by chandra
                performWindowClosing();
            }
        });
        dialog.addEscapeKeyListener(
        new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae) {
                performWindowClosing();
            }
        });
        
        dialog.show();
        // Added by Chandra
        if(detform.isSaveRequired()&& detform.functionType!='D'){
            saveRequired = true;
        }
        /*Fix by Geo. We shouldnt set it to null, since its class instance.
         *Once it becomes null, detail form show up since it passes null then onwards
         */
        /*else{
            commMemberDetails = null;
        }*/
            /* If saveRequired true then it will get the
             * commMembershipDetails bean  from the memberDetails form.
             */
        if(isSaveRequired()){
            commMemberDetails = detform.getMemberDetails();
        }
        if(membersList == null){
            membersList = new Hashtable();
        }
        if(newMembersList == null){
            newMembersList = new Hashtable();
        }
        
        if(selectedRow == -1){
            selectedRow = membersList.size();
        }
        
        
            /* When the user closes the memberDetails form based on
             * functionType the enetered/modified Member Information
             * will be added to membersList hashtable and newMembersList
             * hashtable.And refreshes the MembersTable information
             */
        /*
         * Fix by Geo, when user clicks cancel, commMemberDetails was setting null
	 * Since its a global reference, it we shouldnt set it to Null.
         * Here we check isSaveRequired() as condition to set member details back to list
         */
        if(functionType != 'D' &&  detform.isSaveRequired()) {//commMemberDetails!= null){
            membersList.put(new Integer(selectedRow),commMemberDetails);
            newMembersList.put(new Integer(selectedRow),commMemberDetails);
            initMembers(membersList);
            setNewModel();
            //Updated for New Added Row Selection. Subramanya
            int newRowAdded = tblMembersList.getRowCount() - 1;
            tblMembersList.setRowSelectionInterval(  newRowAdded,
            newRowAdded );
            tblMembersList.scrollRectToVisible( tblMembersList.getCellRect(
            newRowAdded ,0, true));
            
            //Sharath - Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
            btnDetails.setEnabled(true);
            //Bug Fix( IRB-SystemTestingDL-01.xls) Sl No -29
            btnDelete.setEnabled(true);
        }
    }
    
    private void performWindowClosing(){
        if(detform.functionType=='D'){
            setSaveRequired(false);
            dialog.dispose();
            return;
        }
        int option = JOptionPane.NO_OPTION;
        if(detform.isSaveRequired()){
            option = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO,
            CoeusOptionPane.DEFAULT_YES);
            if(option == JOptionPane.YES_OPTION){
                try{
                    detform.setMemberInfo();
                    saveRequired = true;
                    //dlg.dispose();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                    dialog.setVisible(true);
                    saveRequired = false;
                    commMemberDetails = null;
                }
            }
            if(option == JOptionPane.NO_OPTION){
                commMemberDetails = null;
                saveRequired = false;
                dialog.dispose();
            }
        }else{
            dialog.dispose();
        }
    }
    /**
     * This method is used to display the member details based on the function
     * Type for modifying the information or for viewing the membership details
     * information.
     * This will be called from double click event on tblMembersList and
     * on clicking Details button after selecting a member in the list.
     *
     */
    private void setMemberDetails(){
        try{
            //commented for case # 3229 - Inability to modify terms for Members with lapsed term dates  - Start
            //commDetailForm.setCommitteeInfo();
            //commented for case # 3229 - Inability to modify terms for Members with lapsed term dates  - End
        }catch(Exception ex){
            CoeusOptionPane.showErrorDialog(ex.getMessage());
            return;
        }
        /**
         * on double clicking the tblMembersList table open the
         * memberDetails with selected member information
         * based on the functionType.
         * If the data is already existing in the newMembersList
         * hashtable it will display memberdetails form with this
         * information.If it is not existing in newMembers hastable then
         * it will get the selected Member information including
         * Roles,Expertise and status from the database and displays
         * in memberDetails form
         */
        if ( (tblMembersList != null ) && (tblMembersList.getRowCount() != -1)
        && (tblMembersList.getSelectedRow() != -1) ) {
            int row = tblMembersList.getSelectedRow();
            CommitteeMembershipDetailsBean detail =null;
            boolean getFromDb = true;
            if ( (newMembersList!= null) &&
            (newMembersList.containsKey(new Integer(row)))){
                detail = (CommitteeMembershipDetailsBean)
                newMembersList.get(new Integer(row));
                getFromDb = false;
            }else{
                detail =  (CommitteeMembershipDetailsBean)
                membersList.get(new Integer(row));
            }
            //Modified for COEUSQA-2524 : Cannot view member details or areas of research when creating a committee - Start
            //Member details are fetched only for the saved data
//            if(getFromDb){
            if(!TypeConstants.INSERT_RECORD.equals(detail.getAcType()) && getFromDb){//COEUSQA-2524 :End
                detail = getMemberDetailsfromDB(detail);
            }
            showMemberDetails(functionType,detail,row);
            //added by raghuSV
            tblMembersList.setRowSelectionInterval(row,row);
        }
    }
    
    /** This method is used to get the Member Details from database. This returns
     * <CODE>CommitteeMembershipDetails</CODE> with complete information including Roles,
     * Expertise and Status.
     *
     * @param memberdetail <CODE>CommitteeMembershipDetailsBean</CODE> which consists of
     * membership id which will be used to fetch all the member details.
     * @return CommitteeMembershipDetailsBean with all the member information.
     */
    public CommitteeMembershipDetailsBean getMemberDetailsfromDB(
    CommitteeMembershipDetailsBean memberdetail){
        
        String connectTo = connectionURL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        request.setFunctionType('E');
        request.setDataObject(memberdetail);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_start
        Vector vecData = new Vector();                
        vecData.add(0,CoeusConstants.IRB_COMMITTEE_TYPE_CODE);
        request.setDataObjects(vecData);
        // Modified for COEUSQA-2685_IACUC - comm member areas of research should point to IACUC areas of research_end
        AppletServletCommunicator comm
        = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response!=null){
            if (response.isSuccessfulResponse()){
                memberdetail
                = (CommitteeMembershipDetailsBean)response.getDataObject();
            }
        }
        return memberdetail;
    }
    
    /**This method removes the selected member from the view model and 
      resets the view model i.e membersList since it is a Hashtable
     */
    public void deleteMemberDetails(){
        Vector vecMemberList = new Vector();
        
        if ( (tblMembersList != null ) && (tblMembersList.getRowCount() != -1)
        && (tblMembersList.getSelectedRow() != -1) ) {
            int selectedRow = tblMembersList.getSelectedRow();
            CommitteeMembershipDetailsBean detail =
            (CommitteeMembershipDetailsBean)membersList.get(new Integer(selectedRow));
            membersList.remove(new Integer(selectedRow));
            if(detail.getAcType()!=null && detail.getAcType().equals(TypeConstants.INSERT_RECORD)){
                System.out.println("Do nothing");
            }else{
                detail.setAcType(TypeConstants.DELETE_RECORD);
                vecDeleteCommMembers.add(detail);
            }            
            vecMemberList.addAll(membersList.values());
            membersList = getMembersList(vecMemberList);            
            initMembers(membersList);
            setNewModel();
            if(tblMembersList.getRowCount() > 0){
                if(selectedRow!=0){
                    tblMembersList.setRowSelectionInterval(selectedRow-1, selectedRow-1);
                }else{
                    tblMembersList.setRowSelectionInterval(selectedRow, selectedRow);
                }
            }
            
        }
    }
    
     /**
     *  The method to get the members list in a hash Table structure
     * @param Vector of MemberDetails in a committee
     */
    private Hashtable getMembersList(Vector members){
        CommitteeMembershipDetailsBean committeeMember = null;
        Hashtable membersList = new Hashtable();
        if (members != null && members.size() > 0 ) {
            for (int member = 0 ; member < members.size();member++)  {
                if (members.get(member) instanceof
                CommitteeMembershipDetailsBean) {
                    committeeMember = (CommitteeMembershipDetailsBean)
                    members.get(member);
                    membersList.put(new Integer(member),committeeMember);
                }
            }
        }
        return membersList;
    }  
   
    
    /**
     * This method is to check whether the  commitee can be deleted or not
     * @return int 
     */    
    private int canDeleteCommitee(){
        int canDelete = -1 ;
        String connectTo = connectionURL + "/comMntServlet";
        RequesterBean request = new RequesterBean();
        if ( (tblMembersList != null ) && (tblMembersList.getRowCount() != -1)
        && (tblMembersList.getSelectedRow() != -1) ) {
            int selectedRow = tblMembersList.getSelectedRow();
            CommitteeMembershipDetailsBean detail =
            (CommitteeMembershipDetailsBean)membersList.get(new Integer(selectedRow));
            request.setFunctionType('d');
            request.setDataObject(detail);
            AppletServletCommunicator comm
            = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response!=null){
                if (response.isSuccessfulResponse()){
                    canDelete
                    = ((Integer)response.getDataObject()).intValue();
                }
            }
        }//end tblMembersList
        return canDelete;
    }
    
    /**
     * Getter for property commDetailForm.
     * @return Value of property commDetailForm.
     */
    public edu.mit.coeus.irb.gui.CommitteeDetailsForm getCommDetailForm() {
        return commDetailForm;
    }
    
    /**
     * Setter for property commDetailForm.
     * @param commDetailForm New value of property commDetailForm.
     */
    public void setCommDetailForm(edu.mit.coeus.irb.gui.CommitteeDetailsForm commDetailForm) {
        this.commDetailForm = commDetailForm;
    }
    
}
