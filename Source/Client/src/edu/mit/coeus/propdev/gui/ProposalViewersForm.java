/*
 * ProposalViewersForm.java
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
import java.beans.*;
import java.util.Vector;
import java.util.Enumeration;
import java.awt.event.*;
import javax.swing.event.*;
import edu.mit.coeus.brokers.RequesterBean;
import edu.mit.coeus.brokers.ResponderBean;
import edu.mit.coeus.propdev.bean.ProposalRolesFormBean;
import edu.mit.coeus.propdev.bean.ProposalUserRoleFormBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import java.util.HashMap;
import java.sql.Timestamp;

/** This class is used to view the users for a proposal and
 * add more viewers by using the user search.
 *
 * @author Senthil
 * Created on April 1, 2003, 2:48 PM
 */

public class ProposalViewersForm extends javax.swing.JComponent
        implements ActionListener, TypeConstants{

    String proposalNumber;
    int roleId;
    String sponsorCodeName;
    Vector tableData;
    Vector tableRow;
    Vector selectedUsers = null;
    Vector usersForAdd;;
    private CoeusDlgWindow dlgParentComponent;
    private boolean saveRequired;
    private char functionType;
    private boolean commentOnFlag = true ; //True will turn on SOPs.
    private boolean duplicateFlag;
    private CoeusAppletMDIForm mdiForm;
    private CoeusMessageResources coeusMessageResources;
    private final String PROPOSAL_MAINT = "/ProposalMaintenanceServlet";
    private TableSorter sorter;
    /** Creates new form ProposalViewersForm */
    public ProposalViewersForm() {
        initComponents();
    }
    /** Creates new ProposalViewersForm using parameter strings from the
     * ProposalBaseWindow.java
     *
     * @param proposalNumber The Selected proposalNumber value from the search results
     * @param roleId Constant Value Hardcoded in the ProposalBaseWindow class
     * @param sponsorCodeName Concatenated String of Sponsor Code and Sponsor Name.
     */    
    public ProposalViewersForm(String proposalNumber, int roleId,
    	String sponsorCodeName) {
        this.proposalNumber = proposalNumber;
        this.roleId = roleId;
        this.sponsorCodeName = sponsorCodeName;
        tableData = new Vector();

        initComponents();
        postInitComponents();
        setFormData( getProposalViewersFromfromDB() );
        setEditors();
        sorter = new TableSorter( ((DefaultTableModel)tblProposalViewer.getModel()), false );
        tblProposalViewer.setModel(sorter);
        sorter.addMouseListenerToHeaderInTable( tblProposalViewer );
        setEditors();
        Component[] comp = {tblProposalViewer,btnAdd,btnOk,btnCancel};
        ScreenFocusTraversalPolicy traversal = new ScreenFocusTraversalPolicy(comp);
        pnlProposalViewers.setFocusTraversalPolicy(traversal);
        pnlProposalViewers.setFocusCycleRoot(true);
        
        coeusMessageResources = CoeusMessageResources.getInstance();
        dlgParentComponent = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(),
                "Proposal Viewers", true);
        dlgParentComponent.getContentPane().add(pnlProposalViewers);
        dlgParentComponent.pack();
        
        dlgParentComponent.setResizable(false);
        
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgParentComponent.getSize();
        dlgParentComponent.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        dlgParentComponent.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgParentComponent.addEscapeKeyListener(new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
            }
        });
        dlgParentComponent.addWindowListener(new WindowAdapter(){
            public void windowActivated(WindowEvent we){
                if( tblProposalViewer.getRowCount() > 0 ) {
                    tblProposalViewer.requestFocusInWindow();
                }else if( btnAdd.isEnabled() ) {
                    btnAdd.requestFocusInWindow();
                }else{
                    btnCancel.requestFocusInWindow();
                }
            }
            public void windowClosing(WindowEvent we){
                 performWindowClosing();
                 return;
            }
        });
        dlgParentComponent.show();
    }
    
    void performWindowClosing(){
        int option = JOptionPane.NO_OPTION;
            if(isSaveRequired()){
                option
                    = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                                                "saveConfirmCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,
                        CoeusOptionPane.DEFAULT_YES);
            }
            if(option == JOptionPane.YES_OPTION){
                try{
                    saveUserDetails();
                    dlgParentComponent.dispose();
                }catch(Exception e){
                    e.printStackTrace();
                    CoeusOptionPane.showErrorDialog(e.getMessage());
                }
            }else if(option == JOptionPane.NO_OPTION){
                saveRequired = false;
                dlgParentComponent.dispose();
            }
    }
    
    private Vector getProposalViewersFromfromDB(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber and roleId
         */
        RequesterBean request = new RequesterBean();
        Vector reqProposalInfo = new Vector(2);
        reqProposalInfo.add(proposalNumber);
        reqProposalInfo.add(""+roleId);

        request.setDataObject(reqProposalInfo);
        request.setFunctionType('P');
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
                comm.send();
        ResponderBean response = comm.getResponse();
        return (Vector) response.getDataObjects();
    }

    private void setFormData( Vector data ){
        try{
            if( data != null ){

                int dataSize = data.size();
                ProposalUserRoleFormBean roleFormBean=null;
                for( int indx = 0 ; indx < dataSize; indx++) {
                    roleFormBean = ( ProposalUserRoleFormBean) data.elementAt( indx );
                    tableRow = new Vector();
                    if(roleFormBean != null){
                        tableRow.addElement( roleFormBean.getUserId() == null ? "" : roleFormBean.getUserId() );
                        tableRow.addElement( roleFormBean.getUserName() == null ? "" : roleFormBean.getUserName());
                        tableRow.addElement( roleFormBean.getUnitNumber() == null ? "" : roleFormBean.getUnitNumber());
                        tableRow.addElement( roleFormBean.getUnitName() == null ? "" : roleFormBean.getUnitName());
                    }
                    tableData.addElement( tableRow );
                }
                ((DefaultTableModel)tblProposalViewer.getModel()).setDataVector(tableData, getColumnNames() );
                ((DefaultTableModel)tblProposalViewer.getModel()).fireTableDataChanged();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void postInitComponents(){

        btnAdd.addActionListener(this);
        btnOk.addActionListener(this);
        btnCancel.addActionListener(this);
        lblProposalValue.setText(proposalNumber);
        lblSponsorValue.setText(sponsorCodeName);
        JTableHeader tableHeader = tblProposalViewer.getTableHeader();
        tableHeader.setFont( CoeusFontFactory.getLabelFont());
        usersForAdd = new Vector();
    }

    /** Implements the action to be performed on clicking the 3 buttons
     * (OK, CANCEL, ADD) on the ProposalViewerForm
     * @param ae Action
     */    
    public void actionPerformed(ActionEvent ae){
        Object source = ae.getSource();
        if ( source.equals( btnAdd ) ) {
		            try {
		                CoeusSearch userSearch
		                    = new CoeusSearch( CoeusGuiConstants.getMDIForm(), "USERSEARCH",
		                        CoeusSearch.TWO_TABS_WITH_MULTIPLE_SELECTION );
		                userSearch.showSearchWindow();
		                selectedUsers = userSearch.getMultipleSelectedRows();
		                if ( selectedUsers != null ){
		                    HashMap user = null;
                                    saveRequired = true;
		                    for(int indx = 0; indx < selectedUsers.size(); indx++ ){

		                        user = (HashMap)selectedUsers.get( indx ) ;
                                        ProposalRolesFormBean userBean= new ProposalRolesFormBean();
		                        if( user == null || user.isEmpty() ){
		                            continue;
		                        }
		                        String userId = checkForNull(user.get( "USER_ID" ));
                                        if (isDuplicate(userId)){
                                            continue;
                                        }else{
		                        String userName = checkForNull(user.get( "USER_NAME" ));
		                        String homeUnit = checkForNull( user.get( "UNIT_NUMBER" ));
		                        String unitName = checkForNull(user.get( "UNIT_NAME" ));
		                        char status =  user.get("STATUS") == null ? ' '
		                                    : ((String)user.get("STATUS")).charAt(0);
                                        userBean.setProposalNumber(proposalNumber);
                                        userBean.setRoleId(roleId);
                                        userBean.setUserId(userId);
                                        userBean.setAcType(INSERT_RECORD);
                                        userBean.setUpdateUser(userId);
		                        usersForAdd.addElement(userBean);
		                        Vector addedRow = new Vector(4);
		                        addedRow.addElement( userId );
		                        addedRow.addElement( userName );
		                        addedRow.addElement( homeUnit );
		                        addedRow.addElement( unitName );
                                        sorter.insertRow( sorter.getRowCount(), addedRow );
                                        sorter.fireTableDataChanged();
                                        btnAdd.requestFocusInWindow();
                                        
                                        }
                                    }
                                   
		                }
		            }catch ( Exception ex ) {
                                ex.printStackTrace();
		                CoeusOptionPane.showInfoDialog( ex.getMessage() );
                            }
                           
        }else if(source.equals( btnOk )){
            if(usersForAdd!=null && usersForAdd.size() > 0)
            {
                saveUserDetails();
            }
            setSaveRequired(false);
            performWindowClosing();
        }else if(source.equals( btnCancel )){
            performWindowClosing();
        }
    }

    private String checkForNull( Object value ){
	        return (value==null)? "":value.toString();
	    }
    private boolean isDuplicate(String newUserId){
        int count = tblProposalViewer.getRowCount();
        duplicateFlag = false;
        for ( int i = 0; i < count; i++){
            if (newUserId.equalsIgnoreCase((String) tblProposalViewer.getValueAt(i,0))){
                duplicateFlag = true;
                break;
            }
        }
        return duplicateFlag;
    }
    private void saveUserDetails(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROPOSAL_MAINT;
        /* connect to the database and get the formData for the
         * given proposalNumber and roleId
         */
        RequesterBean request = new RequesterBean();
        request.setUserName(CoeusGuiConstants.getMDIForm().getUserName());
        request.setDataObjects(usersForAdd);
        request.setId(proposalNumber);
        request.setFunctionType('H');
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
    }

    private void setEditors(){
        
        tblProposalViewer.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn column = tblProposalViewer.getColumnModel().getColumn(0);
        column.setPreferredWidth(75);
        
        column = tblProposalViewer.getColumnModel().getColumn(1);
        column.setPreferredWidth(147);

        column = tblProposalViewer.getColumnModel().getColumn(2);
        column.setPreferredWidth(100);
        
        column = tblProposalViewer.getColumnModel().getColumn(3);
        column.setPreferredWidth(270);
        tblProposalViewer.getTableHeader().setReorderingAllowed( false );
        tblProposalViewer.getTableHeader().setResizingAllowed(true);
    }
        
    /**
     * This method is used to get the Column Names of Key study personal
     * table data.
     * @return Vector collection of column names of key study personnel table.
     */

    private Vector getColumnNames(){
        Enumeration enumColNames = tblProposalViewer.getColumnModel().getColumns();
        Vector vecColNames = new Vector();
        while(enumColNames.hasMoreElements()){
            String strName = (String)((TableColumn)
            enumColNames.nextElement()).getHeaderValue();
            vecColNames.addElement(strName);
        }
        return vecColNames;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        pnlProposalViewers = new javax.swing.JPanel();
        pnlProposalDescriptionContainer = new javax.swing.JPanel();
        pnlProposalDescription = new javax.swing.JPanel();
        lblProposalNo = new javax.swing.JLabel();
        lblProposalValue = new javax.swing.JLabel();
        lblSponsor = new javax.swing.JLabel();
        lblSponsorValue = new javax.swing.JLabel();
        pnlProposalGridViewer = new javax.swing.JPanel();
        scrPnProposalViewer = new javax.swing.JScrollPane();
        tblProposalViewer = new javax.swing.JTable();
        pnlButtons = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        sptrPropView = new javax.swing.JSeparator();
        lblViewer = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        pnlProposalViewers.setLayout(new java.awt.GridBagLayout());

        pnlProposalDescriptionContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        pnlProposalDescriptionContainer.setMaximumSize(new java.awt.Dimension(585, 50));
        pnlProposalDescriptionContainer.setMinimumSize(new java.awt.Dimension(585, 50));
        pnlProposalDescriptionContainer.setPreferredSize(new java.awt.Dimension(585, 50));
        pnlProposalDescription.setLayout(new java.awt.GridBagLayout());

        lblProposalNo.setFont(CoeusFontFactory.getLabelFont());
        lblProposalNo.setText("Proposal Number:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblProposalNo, gridBagConstraints);

        lblProposalValue.setFont(CoeusFontFactory.getNormalFont());
        lblProposalValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblProposalValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblProposalValue, gridBagConstraints);

        lblSponsor.setFont(CoeusFontFactory.getLabelFont());
        lblSponsor.setText("Sponsor:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        pnlProposalDescription.add(lblSponsor, gridBagConstraints);

        lblSponsorValue.setFont(CoeusFontFactory.getNormalFont());
        lblSponsorValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblSponsorValue.setMinimumSize(new java.awt.Dimension(140, 17));
        lblSponsorValue.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlProposalDescription.add(lblSponsorValue, gridBagConstraints);

        pnlProposalDescriptionContainer.add(pnlProposalDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlProposalViewers.add(pnlProposalDescriptionContainer, gridBagConstraints);

        pnlProposalGridViewer.setMaximumSize(new java.awt.Dimension(610, 360));
        pnlProposalGridViewer.setMinimumSize(new java.awt.Dimension(610, 350));
        pnlProposalGridViewer.setPreferredSize(new java.awt.Dimension(610, 350));
        scrPnProposalViewer.setBorder(new javax.swing.border.EtchedBorder());
        scrPnProposalViewer.setMaximumSize(new java.awt.Dimension(600, 345));
        scrPnProposalViewer.setMinimumSize(new java.awt.Dimension(600, 345));
        scrPnProposalViewer.setPreferredSize(new java.awt.Dimension(600, 345));
        tblProposalViewer.setFont(CoeusFontFactory.getNormalFont());
        tblProposalViewer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User ID", "User Name", "Unit Number", "Unit Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProposalViewer.setPreferredScrollableViewportSize(null);
        tblProposalViewer.setRowHeight(20);
        tblProposalViewer.setSelectionBackground(new java.awt.Color(0, 0, 102));
        tblProposalViewer.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tblProposalViewer.setShowHorizontalLines(false);
        tblProposalViewer.setShowVerticalLines(false);
        tblProposalViewer.setOpaque(false);
        scrPnProposalViewer.setViewportView(tblProposalViewer);

        pnlProposalGridViewer.add(scrPnProposalViewer);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        pnlProposalViewers.add(pnlProposalGridViewer, gridBagConstraints);

        pnlButtons.setLayout(new java.awt.GridBagLayout());

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        btnOk.setMaximumSize(new java.awt.Dimension(81, 27));
        btnOk.setMinimumSize(new java.awt.Dimension(81, 27));
        btnOk.setPreferredSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        pnlButtons.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.setMaximumSize(new java.awt.Dimension(81, 27));
        btnCancel.setMinimumSize(new java.awt.Dimension(81, 27));
        btnCancel.setPreferredSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        pnlButtons.add(btnCancel, gridBagConstraints);

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(81, 27));
        btnAdd.setMinimumSize(new java.awt.Dimension(81, 27));
        btnAdd.setPreferredSize(new java.awt.Dimension(81, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 2);
        pnlButtons.add(btnAdd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 0, 0, 0);
        pnlProposalViewers.add(pnlButtons, gridBagConstraints);

        sptrPropView.setBackground(new java.awt.Color(0, 51, 51));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlProposalViewers.add(sptrPropView, gridBagConstraints);

        lblViewer.setFont(CoeusFontFactory.getLabelFont());
        lblViewer.setText("Viewer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 16, 0, 0);
        pnlProposalViewers.add(lblViewer, gridBagConstraints);

        add(pnlProposalViewers, new java.awt.GridBagConstraints());

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

    /** Getter for property functionType.
     * @return Value of property functionType.
     */
    public char getFunctionType() {
        return functionType;
    }
    
    /** Setter for property functionType.
     * @param functionType New value of property functionType.
     */
    public void setFunctionType(char functionType) {
        this.functionType = functionType;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAdd;
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnOk;
    public javax.swing.JLabel lblProposalNo;
    public javax.swing.JLabel lblProposalValue;
    public javax.swing.JLabel lblSponsor;
    public javax.swing.JLabel lblSponsorValue;
    public javax.swing.JLabel lblViewer;
    public javax.swing.JPanel pnlButtons;
    public javax.swing.JPanel pnlProposalDescription;
    public javax.swing.JPanel pnlProposalDescriptionContainer;
    public javax.swing.JPanel pnlProposalGridViewer;
    public javax.swing.JPanel pnlProposalViewers;
    public javax.swing.JScrollPane scrPnProposalViewer;
    public javax.swing.JSeparator sptrPropView;
    public javax.swing.JTable tblProposalViewer;
    // End of variables declaration//GEN-END:variables
}
 
           