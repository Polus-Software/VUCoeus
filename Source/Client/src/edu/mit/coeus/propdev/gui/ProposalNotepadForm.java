/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.mail.controller.ActionValidityChecking;
import edu.mit.coeus.user.gui.UserDelegationForm;
import java.util.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.menu.CoeusMenu;
import edu.mit.coeus.gui.menu.CoeusMenuItem;
import edu.mit.coeus.gui.toolbar.CoeusToolBarButton;
import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.NotepadBean;
import edu.mit.coeus.propdev.bean.ProposalAwardHierarchyLinkBean;
import edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean;
import edu.mit.coeus.propdev.gui.ProposalAwardHierarchyNode;
import edu.mit.coeus.user.gui.UserPreferencesForm;

/** Displays the Notepad Base window
 * ProposalNotepadForm.java
 * @author  Vyjayanthi
 * Created on December 24, 2003, 3:00 PM
 */
public class ProposalNotepadForm extends CoeusInternalFrame
implements ActionListener, VetoableChangeListener, TreeSelectionListener{

    /** Horizontal separator in menu items */
    private static final String SEPERATOR = "seperator";
    
    private static final String EMPTY_STRING = "";
    private static final String ENTER_COMMENTS = "proposal_Notepad_exceptionCode.7114";
    private static final String SAVE_FAILED = "proposal_Notepad_exceptionCode.7115";
    
    private static final String DEVELOPMENT_PROPOSAL = "Notes for Development Proposal - ";
    private static final String INSTITUTE_PROPOSAL = "Notes for Institute Proposal - ";
    private static final String AWARD = "Notes for Award - ";
    
    private static final char GET_PROP_DEV_NOTEPAD = 'D';
    private static final char GET_INSTITUTE_PROP_NOTEPAD = 'E';
    private static final char GET_AWARD_NOTEPAD = 'F';
    private static final char UPDATE_PROP_DEV_NOTEPAD = 'G';
    private static final char UPDATE_INST_PROP_NOTEPAD = 'H';
    private static final char UPDATE_AWARD_NOTEPAD = 'I';
    
    /** Holds the function types to get data from the server and save data 
     * depending on the proposal type(institute proposal/award/development proposal
     */ 
    private char functionType, saveFunctionType;
    
    private static final String FIELD_AC_TYPE = "acType";

    /** Holds the internal frame title */
    private static final String NOTEPAD_FRAME_TITLE = CoeusGuiConstants.NOTEPAD_FRAME_TITLE;
    
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
                                     CoeusGuiConstants.PROPOSAL_ACTION_SERVLET;
    
    /** Holds all the Notepad beans */
    private CoeusVector cvNotepad;
   
    /** Holds all the newly inserted Notepad beans along with the existing ones */
    private CoeusVector cvNewBeans;
    
    /** Holds all the modified Notepad beans along with the existing ones */
    private CoeusVector cvModifiedBeans;
    
    /** Holds the count of newly inserted Notepad beans */
    private int insertedRows = 0;
    
    /** Holds all the instances of Notepad panel */
    private ArrayList arrLstNotepad = new ArrayList();
    
    /** Data has to be saved or not */
    public boolean saveRequired = false;
    
    /** Flag to check if comments are entered or not to set back the selected node */
    private boolean commentsEntered = false;
    
    /** Set if user has right */
    boolean hasRight = false;
    
    private Hashtable hsTable;
    
    /** Set if user has OSP right */
    boolean hasOSPRight = false;
    
    private boolean clickedNo = false;
    
    private boolean cancelClicked = false;
    
    /** Main MDI form */
    CoeusAppletMDIForm mdiForm;
    
    /** Holds either the Institute Proposal Number, Development Proposal Number
     * or Award Number */
    private String proposalAwardNumber;
    
    //Added for bug id 1856 step 1: start
    private String proposalUnitNumber;
    //bug id 1856 step 1 : end
    
    /** Holds an instance of ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    /** Holds the selected tree path */
    private TreePath selectedTreePath;
    
    /** Holds an instance of ProposalAwardHierarchyLinkBean */
    private ProposalAwardHierarchyLinkBean propAwardHierarchyLinkBean;

    /** Menu Items for the Notepad - File Menu */
    private CoeusMenuItem inboxMenuItem, closeMenuItem, saveMenuItem,
    /*printSetupMenuItem,*/ changePasswordMenuItem, 
    /*Added for Case#3682 - Enhancements related to Delegations - Start*/
    delegationsMenuItem,
    /*Added for Case#3682 - Enhancements related to Delegations - End*/
    preferencesMenuItem, exitMenuItem,
    /*Case 2110 Start*/ currentLockMenuItem/*Case 2110 End*/;
    
    /** Menu Items for the Notepad - Edit menu */
    private CoeusMenuItem addMenuItem, notifyMenuItem, medusaMenuItem;
    
    /** ToolBar buttons for Notepad */
    public CoeusToolBarButton btnAddNewRow;
    public CoeusToolBarButton btnSendNotification;
    public CoeusToolBarButton btnSave;
    public CoeusToolBarButton btnClose;
    private boolean treeDataAvailable;
    /** Holds CoeusMessageResources instance used for reading message Properties */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    private ChangePassword changePassword;
    
    private UserPreferencesForm userPreferencesForm;
    //Added for Case#3682 - Enhancements related to Delegations - Start
     private UserDelegationForm userDelegationForm;
    //Added for Case#3682 - Enhancements related to Delegations - End
    //COEUSQA:2653 - Add Protocols to Medusa - Start
    private static final String IRB_PROTOCOL = "Notes for IRB Protocol - ";
    private static final String IACUC_PROTOCOL = "Notes for IACUC Protocol - ";
    private static final char GET_IRB_PROTO_NOTEPAD = 'h';
    private static final char GET_IACUC_PROTO_NOTEPAD = 'i';
    private static final char UPDATE_IRB_PROTO_NOTEPAD = 'k';
    private static final char UPDATE_IACUC_PROTO_NOTEPAD = 'l';
     //COEUSQA: 2653 - End

    /** Creates new form ProposalNotepadForm with the given
     * <CODE>CoeusAppletMDIForm</CODE> as parent.
     * @param proposalAwardNumber reference to <CODE>proposalAwardNumber</CODE>.
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProposalNotepadForm(String proposalAwardNumber, CoeusAppletMDIForm mdiForm) {
        super(NOTEPAD_FRAME_TITLE, mdiForm);
        this.proposalAwardNumber = proposalAwardNumber;
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
    }
    
    /** Creates new form ProposalNotepadForm with the given
     * <CODE>CoeusAppletMDIForm</CODE> as parent.
     * @param hsTable
     * @param selectedNodeId
     * @param mdiForm reference to <CODE>CoeusAppletMDIForm</CODE>.
     */
    public ProposalNotepadForm(Hashtable hsTable, String selectedNodeId,CoeusAppletMDIForm mdiForm,
    ProposalAwardHierarchyLinkBean propAwardHierarchyLinkBean){
        super(NOTEPAD_FRAME_TITLE, mdiForm);
        this.hsTable = hsTable;
        this.proposalAwardNumber = selectedNodeId;
        this.mdiForm = mdiForm;
        this.treeDataAvailable = true;
        this.propAwardHierarchyLinkBean = propAwardHierarchyLinkBean;
        initComponents();
        postInitComponents();
    }
    
    public ProposalNotepadForm(ProposalAwardHierarchyLinkBean propAwardHierarchyLinkBean,
    CoeusAppletMDIForm mdiForm){
        super(NOTEPAD_FRAME_TITLE, mdiForm);
        this.propAwardHierarchyLinkBean = propAwardHierarchyLinkBean;
        this.mdiForm = mdiForm;
        initComponents();
        postInitComponents();
    }
    
    //Modified for bug id 1856 step 2 : start
    /** Addtional method to set up the form */
    public void postInitComponents(){
    //bug id 1856 step 2 : end    
        addVetoableChangeListener(this);
        
        //Do not display subcontract info
        proposalAwardHierarchyTree.setShowSubContract(false);
        
        proposalAwardHierarchyTree.setTreeSelectionListener(this);
        if( !treeDataAvailable ) {
            proposalAwardHierarchyTree.construct(propAwardHierarchyLinkBean);
        }else{
            proposalAwardHierarchyTree.construct(hsTable,proposalAwardNumber,propAwardHierarchyLinkBean );
        }
        createMenus();
        
        //Get the selected tree path
        selectedTreePath = proposalAwardHierarchyTree.treeProposalAwardHierarchy.getLeadSelectionPath();        
        setFormData();
    }
    
    public void setSelectedNodeId( String selectedNodeId ) {
        proposalAwardHierarchyTree.setSelectedNodeId(selectedNodeId);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        proposalAwardHierarchyTree = new edu.mit.coeus.propdev.gui.ProposalAwardHierarchyForm();
        scrPnNotepad = new javax.swing.JScrollPane();
        pnlNotepad = new javax.swing.JPanel();
        pnlTitle = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        proposalAwardHierarchyTree.setMinimumSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(proposalAwardHierarchyTree, gridBagConstraints);

        scrPnNotepad.setBorder(null);
        scrPnNotepad.setMinimumSize(new java.awt.Dimension(500, 22));
        scrPnNotepad.setPreferredSize(new java.awt.Dimension(500, 22));
        pnlNotepad.setLayout(new java.awt.GridLayout(1, 0));

        scrPnNotepad.setViewportView(pnlNotepad);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(scrPnNotepad, gridBagConstraints);

        pnlTitle.setLayout(new java.awt.GridBagLayout());

        pnlTitle.setBackground(new java.awt.Color(153, 153, 153));
        pnlTitle.setMinimumSize(new java.awt.Dimension(52, 22));
        pnlTitle.setPreferredSize(new java.awt.Dimension(52, 22));
        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitle.setText("Notes for");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlTitle.add(lblTitle, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        getContentPane().add(pnlTitle, gridBagConstraints);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel lblTitle;
    public javax.swing.JPanel pnlNotepad;
    public javax.swing.JPanel pnlTitle;
    public edu.mit.coeus.propdev.gui.ProposalAwardHierarchyForm proposalAwardHierarchyTree;
    public javax.swing.JScrollPane scrPnNotepad;
    // End of variables declaration//GEN-END:variables
    
    /** To construct all the components related to Notepad */
    private void createMenus(){
        createNotepadFileMenu();
        createNotepadEditMenu();
        createNotepadToolBar();
    }
    
    /** To construct all the menus related to Notepad */
    private void setMenus() {
        setMenu( getNotepadFileMenu(), 0 );
        setMenu( getNotepadEditMenu(), 1 );
        setFrameIcon( mdiForm.getCoeusIcon() );
        setFrameToolBar( getNotepadToolBar() );
    }
        
    /** Constructs Notepad File menu with sub menu Inbox, Close, Save, Print Setup
     * Change Password, Preferences, Exit */
    private void createNotepadFileMenu(){

        inboxMenuItem = new CoeusMenuItem("Inbox", null, true, true);
        inboxMenuItem.setMnemonic('I');
        inboxMenuItem.addActionListener(this);
        
        closeMenuItem = new CoeusMenuItem("Close", null, true, true);
        closeMenuItem.setMnemonic('C');
        closeMenuItem.addActionListener(this);
        
        saveMenuItem = new CoeusMenuItem("Save", null, true, true);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.setMnemonic('S');
        saveMenuItem.addActionListener(this);
        
        //Commented since we are not using it in Coeus 4.0
        /*printSetupMenuItem = new CoeusMenuItem("Print Setup...", null, true, true);
        printSetupMenuItem.setMnemonic('u');
        printSetupMenuItem.addActionListener(this);
         */
        
        changePasswordMenuItem = new CoeusMenuItem("Change Password", null, true, true);
        changePasswordMenuItem.setMnemonic('h');
        changePasswordMenuItem.addActionListener(this);
        
        //Added for Case#3682 - Enhancements related to Delegations - Start
        delegationsMenuItem = new CoeusMenuItem("Delegations...", null, true, true);
        delegationsMenuItem.setMnemonic('D');
        delegationsMenuItem.addActionListener(this);
        //Added for Case#3682 - Enhancements related to Delegations - End
        preferencesMenuItem = new CoeusMenuItem("Preferences...", null, true, true);
        preferencesMenuItem.setMnemonic('P');
        preferencesMenuItem.addActionListener(this);
        
        exitMenuItem = new CoeusMenuItem("Exit", null, true, true);
        exitMenuItem.setMnemonic('x');
        exitMenuItem.addActionListener(this);
        
        //Case 2110 Start
        currentLockMenuItem = new CoeusMenuItem("Current Locks",null,true,true);
        currentLockMenuItem.setMnemonic('L');
        currentLockMenuItem.addActionListener(this);
        //Case 2110 End
        
    }
    
    /** Method to return the Notepad File menu
     * @return CoeusMenu Notepad File menu */
    private CoeusMenu getNotepadFileMenu() {
        CoeusMenu menuNotepadFile = null;
        Vector fileChildren = new Vector();
        fileChildren.add(inboxMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(closeMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(saveMenuItem);
        fileChildren.add(SEPERATOR);
        
        //Commented since we are not using it in Coeus 4.0
        //fileChildren.add(printSetupMenuItem);
        
        fileChildren.add(changePasswordMenuItem);
        //Case 2110 Start
        fileChildren.add(SEPERATOR);
        fileChildren.add(currentLockMenuItem);
        fileChildren.add(SEPERATOR);
        //Case 2110 End
        //Added for Case#3682 - Enhancements related to Delegations - Start
        fileChildren.add(delegationsMenuItem);
        fileChildren.add(SEPERATOR);
        //Added for Case#3682 - Enhancements related to Delegations - End
        fileChildren.add(preferencesMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(exitMenuItem);
        
        menuNotepadFile = new CoeusMenu("File", null, fileChildren, true, true);
        menuNotepadFile.setMnemonic('F');
        return menuNotepadFile;
    }
    
    /** Constructs Notepad Edit menu with sub menu Add, Notify, Medusa */
    private void createNotepadEditMenu(){

        addMenuItem = new CoeusMenuItem("Add", null, true, true);
        addMenuItem.setMnemonic('A');
        addMenuItem.addActionListener(this);
        
        notifyMenuItem = new CoeusMenuItem("Notify...", null, true, true);
        notifyMenuItem.setMnemonic('N');
        notifyMenuItem.addActionListener(this);
        
        medusaMenuItem = new CoeusMenuItem("Medusa", null, true, true);
        medusaMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));        
        medusaMenuItem.setMnemonic('M');
        medusaMenuItem.addActionListener(this);
        
    }
    
    /** Method to return the Notepad Edit menu
     * @return CoeusMenu Notepad Edit menu */
    private CoeusMenu getNotepadEditMenu(){
        CoeusMenu menuNotepadEdit = null;
        Vector fileChildren = new Vector();

        fileChildren.add(addMenuItem);
        fileChildren.add(notifyMenuItem);
        fileChildren.add(SEPERATOR);
        fileChildren.add(medusaMenuItem);
        
        menuNotepadEdit = new CoeusMenu("Edit", null, fileChildren, true, true);
        menuNotepadEdit.setMnemonic('E');
        return menuNotepadEdit;
    }
    
    /** Constructs Notepad ToolBar with toolbar buttons New, Notify, Save, Close */
    private void createNotepadToolBar() {
        btnAddNewRow = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.ADD_ICON)),
        null,"Add new row");
        
        btnAddNewRow.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DADD_ICON)));

        btnAddNewRow.addActionListener(this);
        
        
        //btnSendNotification = new CoeusToolBarButton(new ImageIcon(
       // getClass().getClassLoader().getResource(CoeusGuiConstants.NOTIFY_ICON)),
       // null,"Send Proposal Notification");
        
        //Added for COEUSQA-2342 - Remove the old notification icon and menu item from Proposal Development window
        //Updating to new mail notification (ICON Change)
        btnSendNotification = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.EMAIL_ICON)),
        null,"Send Mail Notification");
        
        btnSendNotification.addActionListener(this);
        
        btnSave = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.SAVE_ICON)),
        null,"Save");
        
        btnSave.setDisabledIcon(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.DSAVE_ICON)));
        
        btnSave.addActionListener(this);
        
        btnClose = new CoeusToolBarButton(new ImageIcon(
        getClass().getClassLoader().getResource(CoeusGuiConstants.CLOSE_ICON)),
        null,"Close");
        btnClose.addActionListener(this);
        
    }
    
    /** Method to prepare the toolbar
     * @return toolBar the prepared toolbar */
    private JToolBar getNotepadToolBar(){        
        JToolBar toolBar = new JToolBar();
        toolBar.add(btnAddNewRow);
        toolBar.add(btnSendNotification);
        toolBar.addSeparator();
        toolBar.add(btnSave);
        toolBar.addSeparator();
        toolBar.add(btnClose);
        return toolBar;
    }
    
    /** This method is used to add custom tool bar and menu bar to the application
     *  when the internal frame is activated.
     * @param e InternalFrameEvent which delegates the event. */
    public void internalFrameActivated(InternalFrameEvent e) {
        super.internalFrameActivated( e );
        /** Recreate the menu bar
         * Replace the existing File and Edit menu */
        mdiForm.getCoeusMenuBar().remove( 0 );
        mdiForm.getCoeusMenuBar().add( getNotepadFileMenu(), 0 );

        mdiForm.getCoeusMenuBar().remove( 1 );
        mdiForm.getCoeusMenuBar().add( getNotepadEditMenu(), 1 );
        mdiForm.getCoeusMenuBar().validate();
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this screen.
     */
    private void performWindowClosing() throws PropertyVetoException{
        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    if( saveFormData() ){
                        close();
                    }else{
                        throw new PropertyVetoException("Cancel",null);
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    clickedNo = true;
                    close();
                    break;
                case ( JOptionPane.CANCEL_OPTION ):
                    throw new PropertyVetoException("Cancel",null);
            }
        }else{
            close();
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
        if( source.equals(btnAddNewRow) || source.equals(addMenuItem) ){
            addNewRow();
        }else if( source.equals(btnSendNotification) || source.equals(notifyMenuItem) ){
            
            //Updating to new mail notification (functionality Change)
           sendProposalNotification();           
        
        }else if( source.equals(btnSave) || source.equals(saveMenuItem) ){
            saveFormData();
        }else if( source.equals(btnClose) || source.equals(closeMenuItem) ){
            try{
                performWindowClosing();
            }catch (PropertyVetoException propertyVetoException){
                //Throws PropertyVetoException if user clicks Cancel 
                //Hence, no need to print or perform any action
            }
        }else if( source.equals(medusaMenuItem) ){
            openMedusa();
        }else if( source.equals(inboxMenuItem) ){
            openInbox();
        }else if( source.equals(changePasswordMenuItem) ){
            showChangePassword();
        //Added for Case#3682 - Enhancements related to Delegations - Start
        }else if( source.equals(delegationsMenuItem) ){
            displayUserDelegation();
        //Added for Case#3682 - Enhancements related to Delegations - End
        }else if(source.equals(preferencesMenuItem)){
            showPreference();
            //start of bug fix id 1651
        }else if(source.equals(exitMenuItem)) {
            exitApplication();
        }//end of bug fix id 1651
        //Case 2110 Start
        else if(source.equals(currentLockMenuItem)){
            showLocksForm();
        }
        //Case 2110 End
        }catch(CoeusException coeusException){            
           coeusException.printStackTrace();
           CoeusOptionPane.showErrorDialog(coeusException.getMessage());
        }catch(Exception exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }
    }
    
    //Added by shiji for bug fix id 1651
     public void exitApplication(){
        String message = coeusMessageResources.parseMessageKey(
                                    "toolBarFactory_exitConfirmCode.1149");
        int answer = CoeusOptionPane.showQuestionDialog(message,
                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
        if (answer == JOptionPane.YES_OPTION) {
            if( mdiForm.closeInternalFrames() ) {
                mdiForm.dispose();
            }
        }
    }//End bug fix id 1651
    
     //Added for Case#3682 - Enhancements related to Delegations - Start
     /*
      * Display Delegations window
      */
    private void displayUserDelegation() {
        userDelegationForm = new UserDelegationForm(mdiForm,true);
        userDelegationForm.display();
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
     
    // Added by surekha to implement the User Preference details
    private void showPreference(){
        if(userPreferencesForm == null) {
            userPreferencesForm = new UserPreferencesForm(mdiForm,true);
        }
        userPreferencesForm.loadUserPreferences(mdiForm.getUserId());
        userPreferencesForm.setUserName(mdiForm.getUserName());
        userPreferencesForm.display();
    }// End surekha

    // Added by Nadh to implement the change password
    private void showChangePassword(){
        if(changePassword == null) {
            changePassword = new ChangePassword();
        }
        changePassword.display();
    }// End Nadh
    
    //Case 2110 Start To get the Current Locks of user
     private void showLocksForm() throws edu.mit.coeus.exception.CoeusException{
        CurrentLockForm currentLockForm = new CurrentLockForm(mdiForm,true);
        currentLockForm.display();
    }
    //Case 2110 End

    /** Adds a new row on top in the Notepad panel */
    private void addNewRow(){
        /** Check if comments are entered for all the newly inserted rows */
        if( !isCommentsEntered(false) ){
            return;
        }
        NotepadBean notepadBean = new NotepadBean();
        notepadBean.setAcType(TypeConstants.INSERT_RECORD);
        notepadBean.setRestrictedView(false);
        notepadBean.setUpdateUser(mdiForm.getUserName());
        // Case# 3180: By not Populated
        notepadBean.setUpdateUserName(UserUtils.getDisplayName(mdiForm.getUserId()));
        /** To keep track of the newly inserted rows
         * (for enabling the text area of the new rows), 
         * set the proposalAwardNumber as empty string
         * Set the actual value while saving */
        notepadBean.setProposalAwardNumber(EMPTY_STRING);

        //Get Database Timestamp
        java.sql.Timestamp dbTimeStamp = CoeusUtils.getDBTimeStamp();

        notepadBean.setUpdateTimestamp(dbTimeStamp);

        cvNewBeans = new CoeusVector();
        cvNewBeans = cvNotepad;

        //Add the new notepadBean at the top
        cvNewBeans.add(0, notepadBean);
        insertedRows++;
        preparePanel(cvNewBeans);
        
        //Get the current component to set the focus on txtArComments
        ProposalNotepad proposalNotepad = (ProposalNotepad)arrLstNotepad.get(0);
        proposalNotepad.txtArComments.requestFocus();

        setSaveRequired(true);
        proposalAwardHierarchyTree.setSaveRequired(isSaveRequired());
        
    }
    
    /** Method to check if comments are entered for the newly inserted rows
     * if comments are already entered, set the bean property with the new values
     * @param showMessage if true, displays the message to enter comments
     * @return true if comments are entered, false otherwise
     */
    private boolean isCommentsEntered(boolean showMessage){
        for(int index = 0; index < insertedRows; index++ ){
            ProposalNotepad proposalNotepad = (ProposalNotepad)arrLstNotepad.get(index);
            if ( proposalNotepad.txtArComments.getText().trim().length() == 0 ){
                if( showMessage ){
                    CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(ENTER_COMMENTS));
                }
                proposalNotepad.txtArComments.requestFocus();
                commentsEntered = false;                
                return false;
            }else{
                proposalNotepad.notepadBean.setComments(
                    proposalNotepad.txtArComments.getText());
            }
            proposalNotepad.notepadBean.setRestrictedView(
                proposalNotepad.chkRestrictedView.isSelected());
        }
        commentsEntered = true;
        return true;
    }
    
    /** This method is used to set the panel data
     * @param cvNewNotepad data to set the panel */
    private void preparePanel(CoeusVector cvNewNotepad) {
        cvModifiedBeans = new CoeusVector();
        if(cvNewNotepad.size() <= 4 ){
            pnlNotepad.setLayout(new GridLayout(4,1,0,0));
        }else{
            pnlNotepad.setLayout(new GridLayout(cvNewNotepad.size(),1,0,0));
        }
        
        //Remove the existing sub-panels
        pnlNotepad.removeAll();
        if( arrLstNotepad.size() > 0 ){
            arrLstNotepad.clear();
        }
        
        //Reset the selected comments if there are no notes for the given proposal
        if( cvNewNotepad.size() == 0 ){
            ProposalNotepad.selectedComments = null;
            /*added for the Bug Fix:1647 start step:3*/
            ProposalNotepad.comments = null;
            /*end Bug Fix:1647 step:3*/
        }
        
        //Add the new set of sub-panels
        for( int index = 0; index < cvNewNotepad.size(); index++ ){
            NotepadBean notepadBean = (NotepadBean)cvNewNotepad.get(index);
            ProposalNotepad proposalNotepad = new ProposalNotepad(notepadBean);
            proposalNotepad.setRestrictedViewVisible(hasOSPRight);
            //Set the value of the first notepad comment to the selected comments
            if( index == 0 ){
                ProposalNotepad.selectedComments = notepadBean.getComments();
                /*added for the Bug Fix:1647 start step:4*/
                ProposalNotepad.comments = notepadBean.getComments();
                /*end Bug Fix:1647 step:4*/
            }
            pnlNotepad.add(proposalNotepad.getChildPanel());
            arrLstNotepad.add(index, proposalNotepad.getChildPanel());
            cvModifiedBeans.addElement(notepadBean);
        }
        
        //To refresh pnlNotepad with the new data
        pnlNotepad.revalidate();
        pnlNotepad.setVisible(true);
        
        //Add the bean listeners
        addBeanListener();
        
    }
    
    /** Opens the Proposal Notification screen */
    private void sendProposalNotification(){
        //Set the value of message in the Notification screen
       // ProposalNotify proposalNotify = new ProposalNotify(
        //    proposalAwardNumber,ProposalNotepad.comments);/* step:5 For Bug Fix:1647 to pop the comments in the notification window */
       // proposalNotify.showNotify();
        //Updating for COEUSQA-2342 - Remove the old notification icon and menu item from Proposal Development window
        //Updating to new mail notification (functionality Change)
            ActionValidityChecking checkValid = new ActionValidityChecking();
                   synchronized(checkValid) {
                        //COEUSDEV-75:Rework email engine so the email body is picked up from one place
                       checkValid.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, 0, proposalAwardNumber, 0);
           }
    }
    
    /** Saves the form data
     * @return true if successful, false otherwise
     */
    private boolean saveFormData(){
        if( !isSaveRequired() ) return true;
        if( isCommentsEntered(true) ){
            //Filter and get all the beans that are inserted or modified
            Equals eqInsert = new Equals(FIELD_AC_TYPE, TypeConstants.INSERT_RECORD);
            Equals eqUpdate = new Equals(FIELD_AC_TYPE, TypeConstants.UPDATE_RECORD);
            Or eqInsertOrEqUpdate = new Or(eqInsert, eqUpdate);
            cvModifiedBeans = cvModifiedBeans.filter(eqInsertOrEqUpdate);
            for( int index = 0; index < cvModifiedBeans.size(); index++ ){
                NotepadBean noteBean = (NotepadBean)cvModifiedBeans.get(index);
                if( noteBean.getProposalAwardNumber().equals(EMPTY_STRING) ){
                    noteBean.setProposalAwardNumber(proposalAwardNumber);
                }
            }
            
            //Save to the database
            if( cvModifiedBeans.size() > 0 ){
                RequesterBean requester = new RequesterBean();
                requester.setFunctionType(saveFunctionType);
                requester.setDataObjects(cvModifiedBeans);
                AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
                comm.send();

                ResponderBean response = comm.getResponse();
                if ( response != null ){
                    if( response.isSuccessfulResponse() ){
                        setSaveRequired(false);
                        proposalAwardHierarchyTree.setSaveRequired(isSaveRequired());
                        insertedRows = 0;
                        cvNotepad = getNotepadData();
                        preparePanel(cvNotepad);
                    }else{
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(SAVE_FAILED));
                    }
                }
            }
            return true;
        }else{
            return false;
        }
    }
    
    /** Opens the medusa screen */
    private void openMedusa(){
        try{
            MedusaDetailForm medusaDetailForm;
            if( ( medusaDetailForm = (MedusaDetailForm)mdiForm.getFrame(
            CoeusGuiConstants.MEDUSA_BASE_FRAME_TITLE))!= null ){
                if( medusaDetailForm.isIcon() ){
                    medusaDetailForm.setIcon(false);
                }
                medusaDetailForm.setSelectedNodeId(proposalAwardNumber);
                medusaDetailForm.setSelected( true );
                return;
            }
            ProposalAwardHierarchyLinkBean awardHierarchyLinkBean = 
                proposalAwardHierarchyTree.getSelectedObject();
            if( awardHierarchyLinkBean != null && 
                awardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP) ){
                    // Commented and added by chandra for the bug fix #966 - start
//                    medusaDetailForm = new MedusaDetailForm(mdiForm, propAwardHierarchyLinkBean );
                    medusaDetailForm = new MedusaDetailForm(mdiForm, awardHierarchyLinkBean );
                    // Commented and added by chandra for the bug fix #966 - End
            }else{
                String moduleId = "";
                if(awardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP) ){
                    moduleId = awardHierarchyLinkBean.getInstituteProposalNumber();
                }else{
                    moduleId = awardHierarchyLinkBean.getAwardNumber();
                }
                Hashtable htValues = proposalAwardHierarchyTree.getHtMedusa();
                medusaDetailForm = new MedusaDetailForm(mdiForm,htValues, moduleId,awardHierarchyLinkBean);
            }
            medusaDetailForm.setVisible(true);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
    
    /** Opens the inbox screen */
    // added by chandra . bug fix - 971 - start
    private void openInbox(){
         InboxDetailForm inboxDtlForm = null;
        try{
            if( ( inboxDtlForm = (InboxDetailForm)mdiForm.getFrame(
            "Inbox" ))!= null ){
                if( inboxDtlForm.isIcon() ){
                    inboxDtlForm.setIcon(false);
                }
                inboxDtlForm.setSelected( true );
                return;
            }
            inboxDtlForm = new InboxDetailForm(mdiForm);
            inboxDtlForm.setVisible(true);
        }catch(Exception exception){
            CoeusOptionPane.showInfoDialog(exception.getMessage());
        }
    }// added by chandra . bug fix - 971 - end
    
    /** Method to set the form data */
    private void setFormData(){
        cvNotepad = new CoeusVector();
        propAwardHierarchyLinkBean = proposalAwardHierarchyTree.getSelectedObject();
        if( propAwardHierarchyLinkBean == null ) return;
        if( propAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.INST_PROP)){
            //Set the title based on the type of proposal
            proposalAwardNumber = propAwardHierarchyLinkBean.getInstituteProposalNumber();
            lblTitle.setText(INSTITUTE_PROPOSAL + proposalAwardNumber);

            //Set the function types for getting data and saving data
            functionType = GET_INSTITUTE_PROP_NOTEPAD;
            saveFunctionType = UPDATE_INST_PROP_NOTEPAD;
            
            //Disable the notify menu and notify toolbar button
            notifyMenuItem.setEnabled(false);
            btnSendNotification.setEnabled(false);
            
        }else if( propAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
            //Set the title based on the type of proposal
            proposalAwardNumber = propAwardHierarchyLinkBean.getDevelopmentProposalNumber();
            lblTitle.setText(DEVELOPMENT_PROPOSAL + proposalAwardNumber);
            
            //Set the function types for getting data and saving data
            functionType = GET_PROP_DEV_NOTEPAD;
            saveFunctionType = UPDATE_PROP_DEV_NOTEPAD;
            
            //Disable the notify menu and notify toolbar button
            notifyMenuItem.setEnabled(true);
            btnSendNotification.setEnabled(true);
            
        }else if( propAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.AWARD)){
            //Set the title based on the type of proposal
            proposalAwardNumber = propAwardHierarchyLinkBean.getAwardNumber();
            lblTitle.setText(AWARD + proposalAwardNumber);
            
            //Set the function types for getting data and saving data
            functionType = GET_AWARD_NOTEPAD;
            saveFunctionType = UPDATE_AWARD_NOTEPAD;
            
            //Disable the notify menu and notify toolbar button
            notifyMenuItem.setEnabled(false);
            btnSendNotification.setEnabled(false);
        }
        //COEUSQA:2653 - Add Protocols to Medusa - Start
        else if( propAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IRB_PROTOCOL)){
            //Set the title based on the type of proposal
            proposalAwardNumber = propAwardHierarchyLinkBean.getIrbProtocolNumber();
            lblTitle.setText(IRB_PROTOCOL + proposalAwardNumber);
            
            //Set the function types for getting data and saving data
            functionType = GET_IRB_PROTO_NOTEPAD;
            saveFunctionType = UPDATE_IRB_PROTO_NOTEPAD;
            
            //Disable the notify menu and notify toolbar button
            notifyMenuItem.setEnabled(false);
            btnSendNotification.setEnabled(false);
        }else if( propAwardHierarchyLinkBean.getBaseType().equals(CoeusConstants.IACUC_PROTOCOL)){
            //Set the title based on the type of proposal
            proposalAwardNumber = propAwardHierarchyLinkBean.getIacucProtocolNumber();
            lblTitle.setText(IACUC_PROTOCOL + proposalAwardNumber);
            
            //Set the function types for getting data and saving data
            functionType = GET_IACUC_PROTO_NOTEPAD;
            saveFunctionType = UPDATE_IACUC_PROTO_NOTEPAD;
            
            //Disable the notify menu and notify toolbar button
            notifyMenuItem.setEnabled(false);
            btnSendNotification.setEnabled(false);
        }
        //COEUSQA:2653 - End

        this.cvNotepad = getNotepadData();
//        if(this.cvNotepad!= null && this.cvNotepad.size()> 0){
//            this.cvNotepad.sort("updateTimestamp",false);
//        }
        initializeForm();
   }
    
    /*added for the Bug Fix:1647 start step:2*/
    private void setRequestFocusThread(){
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            setFormData();
        }
        });
    }/*end step:2*/
    
    /** Set the form based on user right and osp right */
    private void initializeForm(){
        
        preparePanel(cvNotepad);
        

//        pnlNotepad.setVisible(hasRight);
//        pnlTitle.setVisible(hasRight);
        pnlNotepad.setVisible(true);
        pnlTitle.setVisible(true);
        
//        btnAddNewRow.setEnabled(hasOSPRight);
//        addMenuItem.setEnabled(hasOSPRight);
//        saveMenuItem.setEnabled(hasOSPRight);
//        btnSave.setEnabled(hasOSPRight);
        // checking for right added by chandra 13th July
            btnAddNewRow.setEnabled(hasOSPRight||hasRight);
            addMenuItem.setEnabled(hasOSPRight||hasRight);
            saveMenuItem.setEnabled(hasOSPRight||hasRight);
            btnSave.setEnabled(hasOSPRight||hasRight);
            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role - Start
            if( propAwardHierarchyLinkBean != null &&
                    CoeusConstants.DEV_PROP.equalsIgnoreCase(propAwardHierarchyLinkBean.getBaseType())){
                btnSendNotification.setEnabled(hasOSPRight||hasRight);
                notifyMenuItem.setEanable(hasOSPRight||hasRight);
            }
            // COEUSDEV-185: Proposal Development and Subawards are viewable from medusa without appropriate role- End
//        ProposalAwardHierarchyLinkBean awardHierarchyLinkBean = 
//                proposalAwardHierarchyTree.getSelectedObject();
        
//        if(awardHierarchyLinkBean.getBaseType().equals(CoeusConstants.DEV_PROP)){
//            btnAddNewRow.setEnabled(hasOSPRight||hasRight);
//            addMenuItem.setEnabled(hasOSPRight||hasRight);
//            saveMenuItem.setEnabled(hasOSPRight||hasRight);
//            btnSave.setEnabled(hasOSPRight||hasRight);
//        }
        
        
        
//else{
//            if( !hasRight ){
//                pnlNotepad.setVisible(false);
//                pnlTitle.setVisible(false);
//            }else{
//                pnlNotepad.setVisible(true);
//                pnlTitle.setVisible(true);
//                if( !hasRight ){
//                    btnAddNewRow.setEnabled(false);
//                    btnSave.setEnabled(false);
//                    addMenuItem.setEnabled(false);
//                    saveMenuItem.setEnabled(false);
//                }else{
//                    btnAddNewRow.setEnabled(true);
//                    btnSave.setEnabled(true);
//                    addMenuItem.setEnabled(true);
//                    saveMenuItem.setEnabled(true);
//                }
//            }
//        }
    }
    
    /** To check if any bean values have changed */
    private void addBeanListener(){
        for (int index = 0; index < cvNotepad.size(); index++ ){
            NotepadBean notepadBean = (NotepadBean)cvNotepad.get(index);
            notepadBean.addPropertyChangeListener(
            new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                    if ( pce.getNewValue() == null && pce.getOldValue() != null ) {
                        setSaveRequired(true);
                        proposalAwardHierarchyTree.setSaveRequired(isSaveRequired());
                    }
                    if( pce.getNewValue() != null && pce.getOldValue() == null ) {
                        setSaveRequired(true);
                        proposalAwardHierarchyTree.setSaveRequired(isSaveRequired());
                    }
                    if( pce.getNewValue()!=null && pce.getOldValue()!=null ) {
                        if (!(  pce.getNewValue().toString().trim().equalsIgnoreCase(pce.getOldValue().toString().trim())))  {
                            setSaveRequired(true);
                            proposalAwardHierarchyTree.setSaveRequired(isSaveRequired());
                        }
                    }
                }
            });
        }
    }
    
    /** Method to get all the Notepad beans from the server 
     * @return vecNotepadData collection of Notepad beans */
    private CoeusVector getNotepadData(){
        RequesterBean requester = new RequesterBean();
        CoeusVector cvNotes = new CoeusVector();
        System.out.println("The function type is : "+functionType);
        requester.setFunctionType(functionType);
        requester.setDataObject(proposalAwardNumber);
        //Added for bug id 1856 step 3 : start 
        requester.setId(proposalUnitNumber);
        //bug id 1856 step 3 : end 
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        Vector vecNotepadData = new Vector();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                vecNotepadData = response.getDataObjects();
                cvNotes = (CoeusVector)vecNotepadData.get(0);
                hasRight = ((Boolean)vecNotepadData.get(1)).booleanValue();
                hasOSPRight = ((Boolean)vecNotepadData.get(2)).booleanValue();
            }
        }
        if( cvNotes == null ){
            return new CoeusVector();
        }else {
            return cvNotes;
        }
    }
    
    /** Method to display the form */
    public void display() {
        setMenus();
        mdiForm.putFrame(NOTEPAD_FRAME_TITLE, this);
        mdiForm.getDeskTopPane().add(this);
        try{
            setVisible(true);
            setSelected(true);
            //Commented during bug-fix
            //setMaximum(true);
        }catch (PropertyVetoException propertyVetoException) {
            propertyVetoException.printStackTrace();
        }
    }
    
    /** Getter for property saveRequired.
     * @return Value of property saveRequired.
     *
     */
    public boolean isSaveRequired() {
        return saveRequired;
    }
    
    /** Setter for property saveRequired.
     * @param saveRequired New value of property saveRequired.
     *
     */
    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }
    
    /** Abstract method of CoeusInternalFrame
     */
    public void saveActiveSheet() {
    }

    /** Abstract method of CoeusInternalFrame
     */
    public void saveAsActiveSheet() {
    }
    
    /** This will catch the window closing event
     * @param pce holds the propertyChangeEvent
     * @throws PropertyVetoException if exception occured
     */
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
        boolean changed = ((Boolean) pce.getNewValue()).booleanValue();
        if(pce.getPropertyName().equals(JInternalFrame.IS_CLOSED_PROPERTY) && changed) {
            if( isSaveRequired() ) {
                if( !clickedNo ){
                    performWindowClosing();
                }else{
                    mdiForm.removeFrame(NOTEPAD_FRAME_TITLE);
                }
            }else{
                mdiForm.removeFrame(NOTEPAD_FRAME_TITLE);
            }
        }
    }
    
    /** Called when Notepad Form is Closed.
     * @throws PropertyVetoException PropertyVetoException
     */
    public void close() throws PropertyVetoException{
        mdiForm.removeFrame(NOTEPAD_FRAME_TITLE);
        doDefaultCloseAction();
    }
    
    /** Performs action on selecting different tree nodes
     * @param treeSelectionEvent holds the generated treeSelectionEvent
     */
    public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

            TreePath oldSelectionPath = treeSelectionEvent.getOldLeadSelectionPath();
            TreePath newSelectedPath = treeSelectionEvent.getNewLeadSelectionPath();

            if(oldSelectionPath == null) return ;

            //Modification - Start

            String prevPropAwdNumber = null, currentPropAwdNumber = null;

            //Get the current selected node
            ProposalAwardHierarchyLinkBean currentPropAwardHierarchyLinkBean =  
                proposalAwardHierarchyTree.getSelectedObject();
            if( currentPropAwardHierarchyLinkBean != null ){
                if( currentPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.AWARD ){
                    currentPropAwdNumber = currentPropAwardHierarchyLinkBean.getAwardNumber();
                }else if( currentPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.INST_PROP ){
                    currentPropAwdNumber = currentPropAwardHierarchyLinkBean.getInstituteProposalNumber();
                }else if( currentPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.DEV_PROP ){
                    currentPropAwdNumber = currentPropAwardHierarchyLinkBean.getDevelopmentProposalNumber();
                }
            }

            ProposalAwardHierarchyNode proposalAwardHierarchyNode = 
                (ProposalAwardHierarchyNode)oldSelectionPath.getLastPathComponent();

            //Get the previous selected node
            ProposalAwardHierarchyLinkBean prevPropAwardHierarchyLinkBean = 
                new ProposalAwardHierarchyLinkBean();
            if( proposalAwardHierarchyNode != null ){
                prevPropAwardHierarchyLinkBean = proposalAwardHierarchyNode.getDataObject();
                if( prevPropAwardHierarchyLinkBean != null ){
                    if( prevPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.AWARD ){
                        prevPropAwdNumber = prevPropAwardHierarchyLinkBean.getAwardNumber();
                    }else if( prevPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.INST_PROP ){
                        prevPropAwdNumber = prevPropAwardHierarchyLinkBean.getInstituteProposalNumber();
                    }else if( prevPropAwardHierarchyLinkBean.getBaseType() == CoeusConstants.DEV_PROP ){
                        prevPropAwdNumber = prevPropAwardHierarchyLinkBean.getDevelopmentProposalNumber();
                    }
                }
            }

            //Modification - End

            int option = JOptionPane.NO_OPTION;
            if( isSaveRequired() ){
                if( currentPropAwdNumber.equals(prevPropAwdNumber) ) return ;
                if( currentPropAwdNumber.equals(EMPTY_STRING) ) cancelClicked = true;
                if( cancelClicked ) {
                    cancelClicked = false;
                    return ;
                }

    //            if(! oldSelectionPath.equals(selectedTreePath)) return ;

                option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("saveConfirmCode.1002"),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL, 
                    CoeusOptionPane.DEFAULT_YES);
                    switch( option ){
                        case ( JOptionPane.YES_OPTION ):
                            saveFormData();
                            if( commentsEntered ){
                                selectedTreePath = newSelectedPath;
                                setFormData();
                            }else{
                                proposalAwardHierarchyTree.treeProposalAwardHierarchy.setSelectionPath(oldSelectionPath);
                            }                        
                            break;
                        case ( JOptionPane.NO_OPTION ):
                            setSaveRequired(false);
                            insertedRows = 0;
                            selectedTreePath = newSelectedPath;
                            setFormData();
                            break;
                        case ( JOptionPane.CANCEL_OPTION ):
                            cancelClicked = true;
                            proposalAwardHierarchyTree.treeProposalAwardHierarchy.setSelectionPath(oldSelectionPath);
                            break;
                    }
            }else{
                selectedTreePath = newSelectedPath;
                //setFormData();
                /*modified for the Bug Fix:1647 step:1*/
                setRequestFocusThread();
                /*end Bug Fix:1647 step:1*/
            }
    }
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    //Added for bug id 1856 : step 4 : start
    /**
     * Getter for property proposalUnitNumber.
     * @return Value of property proposalUnitNumber.
     */
    public java.lang.String getProposalUnitNumber() {
        return proposalUnitNumber;
    }
    
    /**
     * Setter for property proposalUnitNumber.
     * @param proposalUnitNumber New value of property proposalUnitNumber.
     */
    public void setProposalUnitNumber(java.lang.String proposalUnitNumber) {
        this.proposalUnitNumber = proposalUnitNumber;
    }
    //bug id 1856 step 4 : end 
    
}
