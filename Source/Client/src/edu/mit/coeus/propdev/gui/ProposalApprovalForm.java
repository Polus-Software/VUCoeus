/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.gui;

import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.search.gui.*;
import edu.mit.coeus.user.bean.*;
import edu.mit.coeus.bean.*;
import edu.mit.coeus.propdev.bean.*;
import edu.mit.coeus.exception.CoeusClientException;
import edu.mit.coeus.exception.CoeusException;

/** Screen to allow the user to Approve/Bypass 
 * the proposal depending on the showBypass flag
 * ProposalApprovalForm.java
 * @author  Vyjayanthi
 * Created on January 5, 2004, 10:58 AM
 */
public class ProposalApprovalForm extends javax.swing.JComponent
implements ActionListener {
    
    public CoeusDlgWindow dlgProposalApproval;
    
    /** Flag that specifies if the Bypass button should be displayed or not */
    private boolean showBypass = false;
    
    /** Flag to check if any modified data exists */
    private boolean saveRequired = false;
    
    /** Flag to check if the any approval action is invoked
     * can be either Approve/Pass/Reject/Bypass
     */
    private boolean approvalActionPerformed = false;
    
    /** Flag to check if the Approve action is invoked */
    private boolean approveClicked = false;
    
    /** Flag to check if some action button is clicked 
     * used for displaying appropriate errors while performing database updation
     */
    private boolean actionButtonClicked = false;
    
    /** Flag to check the bypass status */
    private boolean bypass = false;
    
    /*** Flag to check if the selected approver is primary or alternate */
    private boolean alternateApprover = false;
    
    /** Flag to check if submit to sponsor action is performed successfully 
     * to enable/disable the Submit To Sponsor menu item in Proposal Detail Form
     */
    private boolean sponsorMenuEnabled = false;
    
    /** Holds the row of the selected Approver in the New Approvers table */
    private int selectedApprover;
    
    /** Holds the values 0 or 1 depending on the user choice to approve all */
    private int approveAll = 0;
    
    /** Holds the selected Approval bean in the New Approvers table */
    private ProposalApprovalBean selectedApprovalBean;
    
    /** Holds the current Approval bean from the routing table */
    private ProposalApprovalBean currentApprovalBean;
    
    /** Holds an instance of the proposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private BaseWindowObservable observable;
    
    private String bypassComments;
    
    /** Holds the next stop number */
    private int nextStopNumber = 0;
    
    /** Holds the existing users displayed in the Proposal Routing table panel */
    private CoeusVector cvExistingApprovers;
    
    private static final int WIDTH = 460;
    private static final int HEIGHT = 290 ;
    private static final int INITIAL_HEIGHT = 190;
    
    private static final String EMPTY_STRING = "";
    
    private static final String TITLE = "Proposal Approval";
    private static final String BYPASS = "Bypass";
    private static final String APPROVE = "Approve";
    private static final String CANCEL = "Cancel";
    private static final String CLOSE = "Close";
    
    private static final char APPROVE_MNEMONIC = 'A';
    private static final char BYPASS_MNEMONIC = 'B';
    
    private static final String MAP_ID_FIELD = "mapId";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    private static final String USER_ID_FIELD = "userId";

    private static final String DESCRIPTION = "Approver added by ";
    
    private static final String WAITING_FOR_APPROVAL = "W";
    private static final String TO_BE_SUBMITTED = "T";
    
    private static final String APPROVE_ACTION = "A";
    private static final String PASS_ACTION = "P";
    private static final String BYPASS_ACTION = "B";
        
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
                                             "/ProposalActionServlet";
    private static final String S2S_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/S2SServlet";
    
    private static final String SELECT_PRIMARY_APPROVER = "proposal_Action_exceptionCode.8000";
    private static final String SELECT_APPROVER_TO_DELETE = "proposal_Action_exceptionCode.8001";
    private static final String ENTER_COMMENTS = "proposal_Action_exceptionCode.8002";
    private static final String APPROVERS_MUST_BE_UNIQUE = "proposal_Action_exceptionCode.8003";
    private static final String APPROVE_ALL = "proposal_Action_exceptionCode.8004";
    
    private static final String ERROR_OPENING = "proposal_Action_exceptionCode.8005";
    private static final String ERROR_DURING_BYPASS = "proposal_Action_exceptionCode.8006";
    private static final String ERROR_DURING_APPROVAL = "proposal_Action_exceptionCode.8007";
    private static final String ERROR_DURING_PASS = "proposal_Action_exceptionCode.8008";
    
    private static final String NO_RIGHT_TO_SUBMIT_TO_SPONSOR = "proposal_Action_exceptionCode.8013";
    private static final String APPROVED_STATUS = "proposal_Action_exceptionCode.8014";
    private static final String SUBMITTED_STATUS = "proposal_Action_exceptionCode.8015";
    private static final String APPROVED = "approved";
    private static final String SUBMITTED = "submitted";
    
    private static final char PROPOSAL_APPROVE_UPDATE = 'Q';
    private static final char GET_APPROVAL_STATUS_FOR_APPROVER = 'R';
    private static final char GET_APPROVER_STOP = 'S';
    private static final char UPDATE_PROP_CREATION_STATUS = 'V';
    
    private static final String GENERATE = "G";
    private static final String SUBMISSION_TYPE = "P";
    private static final String SUBMISSION_STATUS = "S";
    
    //COEUSQA-2860  The bypass approval message is hard coded in a java file. It should be in a message properties file 
    //private static final String BYPASS_COMMENT = "Your approval has been bypassed by OSP because of an approaching deadline";
    private static final String BYPASS_COMMENT = "routing_ByPass_exceptionCode.1001";
    private static final String USER_EXISTS_AS_APPROVER =  " is already present as an approver at this stop";
    
    private Frame parent;
    private boolean modal;
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private boolean parentProposal;
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */    
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    
    /** Creates new form ProposalApprovalForm
     * @param parent takes the frame
     * @param modal takes true or false
     */
    public ProposalApprovalForm(Frame parent, boolean modal) { 
        this.parent = parent;
        this.modal = modal;
        initComponents();
        postInitComponents();
        registerComponents();
    }
   
    /** This method is called from within the constructor to
     * initialize the form. 
     */
    private void postInitComponents(){    
        tblNewApprovers.formatTable();
        tblNewApprovers.showStatus(false);
        scrPnNewApprovers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        dlgProposalApproval = new CoeusDlgWindow(parent, modal);
        dlgProposalApproval.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgProposalApproval.getContentPane().add(this);
        dlgProposalApproval.setResizable(false);
        dlgProposalApproval.setTitle(TITLE);
        dlgProposalApproval.setFont(CoeusFontFactory.getLabelFont());

        formatFields(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgProposalApproval.getSize();
        dlgProposalApproval.setLocation(screenSize.width/2 - (dlgSize.width/2),
        screenSize.height/2 - (dlgSize.height/2));
        
        //showBypass(Bypass btn is not visible) is false if all waiting for approvals are set to Bypassed
        formatButtons(showBypass);
    }
    
    /** This method is used to set the listeners to the components.
     */
    private void registerComponents(){
        //Add listeners to components
        btnAddAlternate.addActionListener(this);
        btnAddApprover.addActionListener(this);
        btnApprove.addActionListener(this);
        btnClose.addActionListener(this);
        btnPass.addActionListener(this);
        btnDelete.addActionListener(this);
        
        dlgProposalApproval.addEscapeKeyListener(
            new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    performWindowClosing();
                }
        });
        
        dlgProposalApproval.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                performWindowClosing();
                return;
            }
        });
        
        dlgProposalApproval.addComponentListener(
            new ComponentAdapter(){
                public void componentShown(ComponentEvent e){
                    txtArComments.requestFocus();
                }
        });
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        scrPnComments = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        lblComments = new javax.swing.JLabel();
        btnApprove = new javax.swing.JButton();
        btnAddApprover = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnPass = new javax.swing.JButton();
        btnAddAlternate = new javax.swing.JButton();
        lblNewApprovers = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        scrPnNewApprovers = new javax.swing.JScrollPane();
        tblNewApprovers = new edu.mit.coeus.propdev.gui.ProposalRoutingTable();

        setLayout(new java.awt.GridBagLayout());

        scrPnComments.setMinimumSize(new java.awt.Dimension(325, 90));
        scrPnComments.setPreferredSize(new java.awt.Dimension(325, 90));
        txtArComments.setDocument(new LimitedPlainDocument(300));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComments.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnComments, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblComments, gridBagConstraints);

        btnApprove.setFont(CoeusFontFactory.getLabelFont());
        btnApprove.setMnemonic('A');
        btnApprove.setText("Approve");
        btnApprove.setMaximumSize(new java.awt.Dimension(111, 26));
        btnApprove.setMinimumSize(new java.awt.Dimension(80, 26));
        btnApprove.setPreferredSize(new java.awt.Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnApprove, gridBagConstraints);

        btnAddApprover.setFont(CoeusFontFactory.getLabelFont());
        btnAddApprover.setMnemonic('d');
        btnAddApprover.setText("Add Approver");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnAddApprover, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(111, 26));
        btnClose.setMinimumSize(new java.awt.Dimension(80, 26));
        btnClose.setPreferredSize(new java.awt.Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnClose, gridBagConstraints);

        btnPass.setFont(CoeusFontFactory.getLabelFont());
        btnPass.setMnemonic('P');
        btnPass.setText("Pass");
        btnPass.setMaximumSize(new java.awt.Dimension(80, 26));
        btnPass.setMinimumSize(new java.awt.Dimension(80, 26));
        btnPass.setPreferredSize(new java.awt.Dimension(111, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnPass, gridBagConstraints);

        btnAddAlternate.setFont(CoeusFontFactory.getLabelFont());
        btnAddAlternate.setMnemonic('l');
        btnAddAlternate.setText("Add Alternate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnAddAlternate, gridBagConstraints);

        lblNewApprovers.setFont(CoeusFontFactory.getLabelFont());
        lblNewApprovers.setText("New Approvers:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblNewApprovers, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(111, 26));
        btnDelete.setMinimumSize(new java.awt.Dimension(80, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnDelete, gridBagConstraints);

        scrPnNewApprovers.setMinimumSize(new java.awt.Dimension(325, 90));
        scrPnNewApprovers.setPreferredSize(new java.awt.Dimension(325, 90));
        scrPnNewApprovers.setViewportView(tblNewApprovers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnNewApprovers, gridBagConstraints);

    }//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnAddAlternate;
    public javax.swing.JButton btnAddApprover;
    public javax.swing.JButton btnApprove;
    public javax.swing.JButton btnClose;
    public javax.swing.JButton btnDelete;
    public javax.swing.JButton btnPass;
    public javax.swing.JLabel lblComments;
    public javax.swing.JLabel lblNewApprovers;
    public javax.swing.JScrollPane scrPnComments;
    public javax.swing.JScrollPane scrPnNewApprovers;
    public edu.mit.coeus.propdev.gui.ProposalRoutingTable tblNewApprovers;
    public javax.swing.JTextArea txtArComments;
    // End of variables declaration//GEN-END:variables
    
    /** Set the form data
     * @param data holds the data for display in the form
     */
    public void setFormData(Object data){
        cvExistingApprovers = (CoeusVector)data;
        txtArComments.setText(currentApprovalBean.getComments());
        txtArComments.setCaretPosition(0);
    }
    
    /** Method to set the buttons properties when showBypass flag is set
     * @param showBypass holds true if set, false otherwise
     */
    public void formatButtons(boolean showBypass){
        this.showBypass = showBypass;
        if( showBypass ){
            btnApprove.setText(BYPASS);
            btnApprove.setMnemonic(BYPASS_MNEMONIC);
            btnAddApprover.setVisible(false);
            btnAddAlternate.setVisible(false);
            btnPass.setVisible(false);
            btnClose.setText(CANCEL);
            txtArComments.setText(coeusMessageResources.parseMessageKey(BYPASS_COMMENT));
            txtArComments.setCaretPosition(0);
            
            /** Code for focus traversal - start */            
            java.awt.Component[] components = { txtArComments, btnApprove, btnClose};
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            setFocusTraversalPolicy(traversePolicy);
            setFocusCycleRoot(true);
            
            /** Code for focus traversal - end */

        }else{
            btnApprove.setText(APPROVE);
            btnApprove.setMnemonic(APPROVE_MNEMONIC);
            btnAddApprover.setVisible(true);
            btnAddAlternate.setVisible(true);
            btnPass.setVisible(true);
            btnClose.setText(CLOSE);
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent 
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(btnApprove) ){
            if( showBypass ){
                performBypassAction();
            }else{
                performApproveAction();
            }
        }else if( source.equals(btnAddApprover) ){
            performAddApproverAction();
        }else if( source.equals(btnAddAlternate) ){
            performAddAlternateAction();
        }else if( source.equals(btnPass) ){
            performPassAction();
        }else if( source.equals(btnClose) ){
            performWindowClosing();
        }else if( source.equals(btnDelete) ){
            performDeleteAction();
        }
    }
    
    /** Allows the user to bypass the proposal */
    private void performBypassAction(){
        currentApprovalBean.setAction(BYPASS_ACTION);
        currentApprovalBean.setApprovalAll(0);
        setSaveRequired(true);
        actionButtonClicked = true;
        saveFormData();
    }
    
    /** Allows the user to approve the proposal */
    private void performApproveAction(){
        //Call save method first, if save failed 
        //display msg "An error was encountered during the approval process"
        checkOtherLevels();
        currentApprovalBean.setAction(APPROVE_ACTION);
        currentApprovalBean.setApprovalAll(approveAll);
        approveClicked = true;
        setSaveRequired(true);
        actionButtonClicked = true;
        saveFormData();
    }
    
    /** Allows the user to pass the proposal */
    private void performPassAction(){
        if( txtArComments.getText().trim().length() > 0 ){
            currentApprovalBean.setAction(PASS_ACTION);
            currentApprovalBean.setApprovalAll(0);
            setSaveRequired(true);
            actionButtonClicked = true;
            saveFormData();
        }else{
            txtArComments.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            ENTER_COMMENTS));
        }
    }
    
    /** Check if the Approver is present for more than one level
     */
    private void checkOtherLevels(){
        RequesterBean requester = new RequesterBean();
        Vector vecProp = new Vector();
        Vector vecData = new Vector();
        vecProp.addElement(currentApprovalBean.getProposalNumber());
        vecProp.addElement(TO_BE_SUBMITTED);
        vecProp.addElement(new Boolean(true));
        requester.setFunctionType(GET_APPROVAL_STATUS_FOR_APPROVER);
        requester.setDataObjects(vecProp);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();

        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                vecData = response.getDataObjects();
            }
        }
        //JM 5-25-2011 prevent from approving at all levels per 4.4.2
        /*
        if( vecData != null && vecData.size() > 0 ){
            int option = JOptionPane.NO_OPTION;
            option = CoeusOptionPane.showQuestionDialog(
                coeusMessageResources.parseMessageKey(APPROVE_ALL),
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
            if( option == 1 ){
                approveAll = 0;
            }else{
                approveAll = 1;
            }
        }else{ */
            approveAll = 0;
        /* } */
        //END
        }
    
    /** Method to add alternate to the selected primary approver */
    private void performAddAlternateAction(){
        //Check if any approver is selected
        selectedApprover = tblNewApprovers.getSelectedRow();
        if( selectedApprover >= 0 ){
            selectedApprovalBean = (ProposalApprovalBean)tblNewApprovers.getTableData().get(selectedApprover);
            //Indicate that this is an alternate approver
            alternateApprover = true;
            selectedApprover = selectedApprover + 1;
            performAddApproverAction();
        }else{
            CoeusOptionPane.showInfoDialog(
                coeusMessageResources.parseMessageKey(SELECT_PRIMARY_APPROVER));
        }
    }
    
    /** Displays the search screen to specify a search criteria
     */
    private void performAddApproverAction(){
        CoeusVector cvApprover = new CoeusVector();
        if( !alternateApprover ){
            Equals eqMapId = new Equals(MAP_ID_FIELD, new Integer(currentApprovalBean.getMapId() ));
            Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(currentApprovalBean.getLevelNumber() ));
            And eqMapIdAndEqLevelNumber = new And( eqMapId, eqLevelNumber);
            CoeusVector cvTempData;
            ProposalApprovalBean tempApprovalBean = new ProposalApprovalBean();
            //Prepare to add a new approver
            if ( nextStopNumber > 0 ){
                nextStopNumber++;
            }else{
                //Filter and get the beans satisfying the filter criteria
                cvTempData = cvExistingApprovers.filter(eqMapIdAndEqLevelNumber);

                if( cvTempData != null && cvTempData.size() > 0 ){
                    //Get the max stop number
                    cvTempData.sort(STOP_NUMBER_FIELD, false);
                    tempApprovalBean = (ProposalApprovalBean)cvTempData.get(0);
                    nextStopNumber = tempApprovalBean.getStopNumber() + 1;
                }
            }
        }

        try{
            CoeusSearch coeusSearch = new CoeusSearch(
                CoeusGuiConstants.getMDIForm(), "userSearch", CoeusSearch.TWO_TABS);
            coeusSearch.showSearchWindow();
            
            HashMap hashMap = coeusSearch.getSelectedRow();
            if(hashMap == null) return ;
            UserRolesController userRolesController = new UserRolesController();
            UserInfoBean userInfoBean = userRolesController.getUser(hashMap.get("USER_ID").toString());
            if(userInfoBean.getUserName() == null) {
                userInfoBean.setUserName(EMPTY_STRING);
            }
            if(approverExists(userInfoBean.getUserId().trim()) ){
                return ;
            }
            else {
                //Does Not Exists
                dlgProposalApproval.setVisible(false);
                formatFields(true);
                
                //Create a new ProposalApprovalBean and add it to the table 
                ProposalApprovalBean propApprovalBean = new ProposalApprovalBean();
                propApprovalBean.setAcType(TypeConstants.INSERT_RECORD);
                propApprovalBean.setUserId(userInfoBean.getUserId().trim());
                propApprovalBean.setUserName(userInfoBean.getUserName());
                propApprovalBean.setProposalNumber(currentApprovalBean.getProposalNumber());
                propApprovalBean.setMapId(currentApprovalBean.getMapId());
                propApprovalBean.setLevelNumber(currentApprovalBean.getLevelNumber());
                propApprovalBean.setDescription(DESCRIPTION + mdiForm.getUserName());
                propApprovalBean.setApprovalStatus(WAITING_FOR_APPROVAL);

                if( alternateApprover ){
                    // Add alternate approvers to same stop
                    propApprovalBean.setStopNumber(selectedApprovalBean.getStopNumber());
                    
                    propApprovalBean.setPrimaryApproverFlag(false);
                    
                    //Insert new row after selected row
                    cvApprover.addAll(0, tblNewApprovers.getTableData());
                    cvApprover.add(selectedApprover, propApprovalBean);
                    alternateApprover = false;
                }else{
                    // Add primary approvers to new stop
                    propApprovalBean.setPrimaryApproverFlag(true);
                    propApprovalBean.setStopNumber(nextStopNumber);
                    if( tblNewApprovers.getTableData() != null ){
                        cvApprover.addAll(0, tblNewApprovers.getTableData());
                    }
                    cvApprover.addElement(propApprovalBean);
                }
                tblNewApprovers.setTableData(cvApprover);
                dlgProposalApproval.setVisible(true);
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    
    /** Method to check if the selected user already exists as an approver 
     * @return true if the user exists, false otherwise
     */
    private boolean approverExists(String userId) {
        //Check with the existing rows in the New Approvers table in the current screen
        if( tblNewApprovers.getTableData() != null && tblNewApprovers.getTableData().size() > 0 ){
            for(int index = 0; index < tblNewApprovers.getTableData().size(); index++) {
                if(userId.equalsIgnoreCase(((ProposalApprovalBean)tblNewApprovers.getTableData().get(index)).getUserId())) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                        APPROVERS_MUST_BE_UNIQUE));
                    return true;
                }
            }
        }
        //Filter to get all the records for the current level number
        Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, 
            new Integer(currentApprovalBean.getLevelNumber()));
        CoeusVector cvCurrenLevelApprovers = cvExistingApprovers.filter(eqLevelNumber);
        
        //Check with the existing approvers for the current level in Proposal Routing screen
        for(int index = 0; index < cvCurrenLevelApprovers.size(); index++) {
            if(userId.equalsIgnoreCase(((ProposalApprovalBean)cvCurrenLevelApprovers.get(index)).getUserId())) {
                CoeusOptionPane.showInfoDialog(userId + USER_EXISTS_AS_APPROVER);
                return true;
            }
        }
        return false;
    }

    
    /** Deletes the selected row in the New Approvers table
     */
    private void performDeleteAction(){
        int selectedRow = tblNewApprovers.getSelectedRow();
        if( selectedRow >= 0 ){
            //Delete the selectedRow from the vector and refresh the table
            tblNewApprovers.getTableData().removeElementAt(selectedRow);
            tblNewApprovers.setTableData(tblNewApprovers.getTableData());
        }else{
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
            SELECT_APPROVER_TO_DELETE));
        }
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        //If rows are added in the new approvers table
        if( tblNewApprovers.getTableData() != null && 
        tblNewApprovers.getTableData().size() > 0 ){
            setSaveRequired(true);
        }
        
        //If comments are added/modified
        if( txtArComments.getText().trim().length() > 0 ){
            if( !txtArComments.getText().trim().equals(currentApprovalBean.getComments()) ){
                setSaveRequired(true);
            }
        }

        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            option
            = CoeusOptionPane.showQuestionDialog(
            coeusMessageResources.parseMessageKey(
            "saveConfirmCode.1002"),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    try{
                        saveFormData();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgProposalApproval.setVisible(false);
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    break;
            }
        }else{
            dlgProposalApproval.setVisible(false);
        }

    }
    
    /** Saves the form data
     */
    private void saveFormData(){
        if ( !isSaveRequired() ){
            dlgProposalApproval.setVisible(false);
            return;
        }
        //Save to the database
        if( ( txtArComments.getText().trim().length() > 0 ) || approveClicked ||
        ( tblNewApprovers.getTableData() != null && tblNewApprovers.getTableData().size() > 0 ) ){
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(PROPOSAL_APPROVE_UPDATE);
            
            Vector vecData = new Vector();
            CoeusVector cvNewApprovers = null;
            if( tblNewApprovers.getTableData() != null && 
            tblNewApprovers.getTableData().size() > 0 ){
                vecData.addElement(tblNewApprovers.getTableData());
            }else{
                vecData.addElement(cvNewApprovers);
            }
            
            if( !(txtArComments.getText().trim().equals(EMPTY_STRING)) ){
                currentApprovalBean.setComments(txtArComments.getText().trim());
            }
            currentApprovalBean.setAcType(TypeConstants.UPDATE_RECORD);
            vecData.addElement(currentApprovalBean);
            
            requester.setDataObjects(vecData);
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();

            ResponderBean response = comm.getResponse();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    setSaveRequired(false);
                    Vector vecValue = response.getDataObjects();
                    if( vecValue.get(0) != null ){
                        int returnValue = ((Integer)vecValue.get(0)).intValue();                    
                        performPostSaveAction(returnValue);
                    }
                    //Close the form after saving
                    dlgProposalApproval.setVisible(false);
                    if( actionButtonClicked ){
                        approvalActionPerformed = true;
                        //Reset the flag since updation is successful
                        actionButtonClicked = false;
                    }
                    if(isParentProposal()){
                        updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                    }
                }else{
                    if( currentApprovalBean.getAction().equals(APPROVE_ACTION) &&
                    actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(ERROR_DURING_APPROVAL));
                    }else if( currentApprovalBean.getAction().equals(PASS_ACTION) &&
                    actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(ERROR_DURING_PASS));
                    }else if( currentApprovalBean.getAction().equals(BYPASS_ACTION) &&
                    actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(ERROR_DURING_BYPASS));
                    }
                }
            }
        }
    }
    
    /** Performs other actions after saving */
    private void performPostSaveAction(int returnValue){
//        ProposalDevelopmentFormBean proposalDevelopmentFormBean = getProposalDevelopmentFormBean();
        try{
            if( returnValue == 2 ){
                submitToSponsor(6);
            }else if( returnValue == 3 ){   //User doesn't have right to submit to sponsor
                CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_RIGHT_TO_SUBMIT_TO_SPONSOR));
                proposalDevelopmentFormBean.setCreationStatusCode(4);
                updateProposalStatus(proposalDevelopmentFormBean, APPROVED);
            }else if( returnValue == 4 ){
                proposalDevelopmentFormBean.setCreationStatusCode(5);
                updateProposalStatus(proposalDevelopmentFormBean, SUBMITTED);
            }else if( returnValue == 5 ){
                submitToSponsor(5);
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
    }
    
    /** Method to perform operations for submitting to sponsor */
    private void submitToSponsor(int status){
//        ProposalDevelopmentFormBean proposalDevelopmentFormBean = getProposalDevelopmentFormBean();
        SubmitToSponsor submitToSponsor = new SubmitToSponsor(mdiForm, true,
            proposalDevelopmentFormBean.getProposalNumber());
        submitToSponsor.setProposalDevelopmentFormBean(proposalDevelopmentFormBean);
        submitToSponsor.setObservable(observable);
        submitToSponsor.setFormMode('A');
        if( proposalDevelopmentFormBean.getProposalTypeCode() == 6 ){
            //Check if revision, ask for original proposal number
            submitToSponsor.setCreationStatus(status);
            setSponsorMenuEnabled(submitToSponsor.display());
            if( submitToSponsor.getProposalDevelopmentFormBean() != null ){
                setProposalDevelopmentFormBean(submitToSponsor.getProposalDevelopmentFormBean());
            }
            return ;
        }else if(proposalDevelopmentFormBean.isS2sOppSelected()) {
            //Grants Gov candidate and submission type = Change Corrected
            S2SHeader headerParam = new S2SHeader();
            headerParam.setSubmissionTitle(proposalDevelopmentFormBean.getProposalNumber());
            if(proposalDevelopmentFormBean.getCfdaNumber()!=null){
                StringBuffer tempCfdaNum = new StringBuffer(proposalDevelopmentFormBean.getCfdaNumber());
                int charIndex = tempCfdaNum.indexOf(".");
                if(charIndex==-1){
                    tempCfdaNum.insert(2,'.');
                }
                headerParam.setCfdaNumber(tempCfdaNum.toString());
            }
            headerParam.setOpportunityId(proposalDevelopmentFormBean.getProgramAnnouncementNumber());
            headerParam.setAgency(proposalDevelopmentFormBean.getSponsorCode()+" : "+ proposalDevelopmentFormBean.getSponsorName());
            HashMap params = new HashMap();
            params.put("PROPOSAL_NUMBER", proposalDevelopmentFormBean.getProposalNumber());
            headerParam.setStreamParams(params);
            
            RequesterBean request = new RequesterBean();
            request.setDataObject(headerParam);
            request.setFunctionType(S2SConstants.GET_DATA);
            AppletServletCommunicator comm = new AppletServletCommunicator();
            comm.setConnectTo(S2S_SERVLET);
            comm.setRequest(request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                Object object[] = (Object[])response.getDataObject();
                OpportunityInfoBean opportunityInfoBean = (OpportunityInfoBean)object[0];
                int submissionTypeCode = opportunityInfoBean.getSubmissionTypeCode();
                if(submissionTypeCode == 3) {//Change Corrected Submission
                    submitToSponsor.setCreationStatus(status);
                    setSponsorMenuEnabled(submitToSponsor.display());
                    if( submitToSponsor.getProposalDevelopmentFormBean() != null ){
                        setProposalDevelopmentFormBean(submitToSponsor.getProposalDevelopmentFormBean());
                    }
                    return ;
                }
            }
            
        }//else{
            //Update proposal status
            proposalDevelopmentFormBean.setCreationStatusCode(status);
            try{
                updateProposalStatus(proposalDevelopmentFormBean, null);

                //To generate the institute proposal number
                Vector vecData = new Vector();
                vecData.addElement(proposalDevelopmentFormBean.getProposalNumber());
                vecData.addElement(null);
                vecData.addElement(GENERATE);
                vecData.addElement(SUBMISSION_TYPE);
                vecData.addElement(SUBMISSION_STATUS);
                vecData.addElement(new Integer(status));
                submitToSponsor.feedInstPropForApprove(vecData);
                
                setSponsorMenuEnabled(false);
            }catch (CoeusClientException coeusClientException){
                coeusClientException.printStackTrace();
            }
            try{
                submitToSponsor.submitGrantsGov();
                if( submitToSponsor.getProposalDevelopmentFormBean() != null ){
                    setProposalDevelopmentFormBean(submitToSponsor.getProposalDevelopmentFormBean());
                }                
            }catch (CoeusException coeusException){
                coeusException.printStackTrace();
                CoeusOptionPane.showErrorDialog(coeusException.getMessage());
            }            
        //}
    }

    /** To update the proposal status */
    private void updateProposalStatus(ProposalDevelopmentFormBean proposalDevelopmentFormBean,
    String action) throws CoeusClientException{
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(UPDATE_PROP_CREATION_STATUS);
        requester.setDataObject(proposalDevelopmentFormBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();

        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                setProposalDevelopmentFormBean(
                    (ProposalDevelopmentFormBean)response.getDataObject());
                if( action == null ) return ;
                if( action.equals(APPROVED) ){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(APPROVED_STATUS));
                }else if( action.equals(SUBMITTED) ){
                    CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SUBMITTED_STATUS));
                }
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }
    }
    
    /** Formats the fields and displays the form
     * @return boolean, true if Approve action is invoked, false otherwise
     */
    public boolean display(){
        if ( !performPreOpenOperation() ){
            return false;
        }

        if( btnApprove.getText().equals(BYPASS) ){
            dlgProposalApproval.show();
        }else{
            dlgProposalApproval.show();
        }
        return approvalActionPerformed;
    }
    
    /** Method to perform some checks before opening the screen */
    private boolean performPreOpenOperation(){
        CoeusVector cvData = new CoeusVector();
        CoeusVector cvFilteredData = new CoeusVector();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_APPROVER_STOP);
        requester.setDataObject(currentApprovalBean);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                cvData = (CoeusVector)response.getDataObjects();
            }else{
                CoeusOptionPane.showInfoDialog(ERROR_OPENING);
                return false;
            }
        }
        if( cvData != null && cvData.size() > 0 ){
            Equals eqUserId;
            if( showBypass ){
                //Filter for the bypassed user
                eqUserId = new Equals(USER_ID_FIELD, currentApprovalBean.getUserId());
            }else{
                eqUserId = new Equals(USER_ID_FIELD, mdiForm.getUserId());
            }
            cvFilteredData = cvData.filter(eqUserId);//An error occurred opening the approval window - stop not found
            if( cvFilteredData != null && cvFilteredData.size() > 0 ){
                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean)cvFilteredData.get(0);
                bypassComments = proposalApprovalBean.getComments();
            }
        }
        return true;
        
    }
    
    /** Method to enable and disable components
     * @param formHeightIncreased true if form height is modified, false otherwise
     */
    private void formatFields(boolean formHeightIncreased){
        if( formHeightIncreased ){
            lblNewApprovers.setVisible(true);
            scrPnNewApprovers.setVisible(true);
            btnDelete.setVisible(true);
            btnAddAlternate.setEnabled(true);
            btnPass.setEnabled(true);
            dlgProposalApproval.setSize(WIDTH, HEIGHT);

            /** Code for focus traversal - start */            
            java.awt.Component[] components = { txtArComments, tblNewApprovers, 
                btnApprove, btnAddApprover, btnAddAlternate, btnPass, btnClose, btnDelete};
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            setFocusTraversalPolicy(traversePolicy);
            setFocusCycleRoot(true);
            
            /** Code for focus traversal - end */
        }else{
            lblNewApprovers.setVisible(false);
            scrPnNewApprovers.setVisible(false);
            btnDelete.setVisible(false);
            btnAddAlternate.setEnabled(false);
            btnPass.setEnabled(false);
            dlgProposalApproval.setSize(WIDTH, INITIAL_HEIGHT);

            /** Code for focus traversal - start */            
            java.awt.Component[] components = { txtArComments, btnApprove, 
                btnAddApprover, btnClose};
            ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
            setFocusTraversalPolicy(traversePolicy);
            setFocusCycleRoot(true);
            
            /** Code for focus traversal - end */

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
    
    /** Getter for property currentApprovalBean.
     * @return Value of property currentApprovalBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalApprovalBean getCurrentApprovalBean() {
        return currentApprovalBean;
    }
    
    /** Setter for property currentApprovalBean.
     * @param currentApprovalBean New value of property currentApprovalBean.
     *
     */
    public void setCurrentApprovalBean(edu.mit.coeus.propdev.bean.ProposalApprovalBean currentApprovalBean) {
        this.currentApprovalBean = currentApprovalBean;
    }
    
    /** Getter for property bypass.
     * @return Value of property ypass.
     *
     */
    public boolean isBypass() {
        return bypass;
    }
    
    /** Setter for property bypass.
     * @param bypass New value of property bypass.
     *
     */
    public void setBypass(boolean bypass) {
        this.bypass = bypass;
    }
    
    /** Getter for property approvalActionPerformed.
     * @return Value of property approvalActionPerformed.
     *
     */
    public boolean isApprovalActionPerformed() {
        return approvalActionPerformed;
    }
    
    /** Setter for property approvalActionPerformed.
     * @param approvalActionPerformed New value of property approvalActionPerformed.
     *
     */
    public void setApprovalActionPerformed(boolean approvalActionPerformed) {
        this.approvalActionPerformed = approvalActionPerformed;
    }
    
    /** Getter for property proposalDevelopmentFormBean.
     * @return Value of property proposalDevelopmentFormBean.
     *
     */
    public edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean getProposalDevelopmentFormBean() {
        return proposalDevelopmentFormBean;
    }
    
    /** Setter for property proposalDevelopmentFormBean.
     * @param proposalDevelopmentFormBean New value of property proposalDevelopmentFormBean.
     *
     */
    public void setProposalDevelopmentFormBean(edu.mit.coeus.propdev.bean.ProposalDevelopmentFormBean proposalDevelopmentFormBean) {
        this.proposalDevelopmentFormBean = proposalDevelopmentFormBean;
    }
    
    /** Getter for property sponsorMenuEnabled.
     * @return Value of property sponsorMenuEnabled.
     *
     */
    public boolean isSponsorMenuEnabled() {
        return sponsorMenuEnabled;
    }
    
    /** Setter for property sponsorMenuEnabled.
     * @param sponsorMenuEnabled New value of property sponsorMenuEnabled.
     *
     */
    public void setSponsorMenuEnabled(boolean sponsorMenuEnabled) {
        this.sponsorMenuEnabled = sponsorMenuEnabled;
    }

    /**
     * Getter for property observable.
     * @return Value of property observable.
     */
    public edu.mit.coeus.utils.BaseWindowObservable getObservable() {
        return observable;
    }
    
    /**
     * Setter for property observable.
     * @param observable New value of property observable.
     */
    public void setObservable(edu.mit.coeus.utils.BaseWindowObservable observable) {
        this.observable = observable;
    }
    
    
    /** Update the child proposal's creation status code, if the parent proposal
     *performed actions like Submit, Approve, Reject, PostSubmission
     */
    private void updateChildStatus(String proposalNumber) {
        final String connect = CoeusGuiConstants.CONNECTION_URL +"/ProposalActionServlet";
         boolean success = false;
        Vector data = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType( 'z' );
        request.setDataObject( proposalNumber );
        AppletServletCommunicator comm = new AppletServletCommunicator(
        connect, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if( response.isSuccessfulResponse() ){
            success = ((Boolean)response.getDataObject()).booleanValue();
        }else{
            CoeusOptionPane.showErrorDialog(response.getMessage());
            return ;
        }
    }
    
    /**
     * Getter for property parentProposal.
     * @return Value of property parentProposal.
     */
    public boolean isParentProposal() {
        return parentProposal;
    }
    
    /**
     * Setter for property parentProposal.
     * @param parentProposal New value of property parentProposal.
     */
    public void setParentProposal(boolean parentProposal) {
        this.parentProposal = parentProposal;
    }
    
}
