/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by keerthyjayaraj
 */
package edu.mit.coeus.routing.gui;


import edu.mit.coeus.propdev.gui.SubmitToSponsor;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.s2s.bean.OpportunityInfoBean;
import edu.mit.coeus.s2s.bean.S2SHeader;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

// JM 7-21-2015 tooltips for approval buttons
import edu.vanderbilt.coeus.gui.CoeusToolTip;

/**
 * Screen to allow the user to Approve/Bypass
 * the proposal depending on the showBypass flag
 * RoutingApprovalForm.java
 *
 * @author Vyjayanthi
 * Created on January 5, 2004, 10:58 AM
 */
public class RoutingApprovalForm extends javax.swing.JComponent
        implements ActionListener {
    
    public CoeusDlgWindow dlgApprovalWindow;
    
    /** Flag that specifies if the Bypass button should be displayed or not */
    private boolean showBypass = false;
    
    /** Flag to check if any modified data exists */
    private boolean saveRequired = false;
    
    /** Flag to check if the any approval action is invoked
     * can be either Approve/Pass/Reject/Bypass
     */
    private boolean approvalActionPerformed = false;
     private boolean closeActionPerformed = false;
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
    private RoutingDetailsBean selectedRoutingDetailBean;
    
    /** Holds the current Approval bean from the routing table */
    private RoutingDetailsBean currentRoutingDetailBean;
    
    /** Holds an instance of the proposalDevelopmentFormBean */
//    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    private BaseWindowObservable observable;
    
    /** Holds the next stop number */
    private int nextStopNumber = 0;
    
    /** Holds the existing users displayed in the Proposal Routing table panel */
    private CoeusVector cvExistingApprovers;
    
    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
    private boolean hierarchy;
    private static final String NARRATIVE_INCOMPLETE = "proposalSubmitValidation_exceptionCode.1140";
    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
    private static final int WIDTH = 620; // JM 7-21-2015 was 550, increased to accommodate new button labels
    private static final int HEIGHT = 395 ;
    private static final int INITIAL_HEIGHT = 290;
    private static final String EMPTY_STRING = "";
    
    private static final String BYPASS = "Bypass";
    private static final String APPROVE = "Approve";
    private static final String CANCEL = "Cancel";
//    private static final String CLOSE = "Close";
    
    private static final char APPROVE_MNEMONIC = 'A';
    private static final char BYPASS_MNEMONIC = 'B';
    
    //private static final String MAP_ID_FIELD = "mapId";
    private static final String MAP_NUMBER_FIELD = "mapNumber";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    
    private static final String DESCRIPTION = "Approver added by ";
    
    private static final String APPROVE_ACTION = "A";
    private static final String PASS_ACTION = "P";
    private static final String BYPASS_ACTION = "B";
    
    /*Streaming Servlet for viewing document*/
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/ProposalActionServlet";
    private static final String S2S_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/S2SServlet";
    
    private static final String SELECT_PRIMARY_APPROVER = "proposal_Action_exceptionCode.8000";
    private static final String SELECT_APPROVER_TO_DELETE = "proposal_Action_exceptionCode.8001";
    private static final String ENTER_COMMENTS = "proposal_Action_exceptionCode.8002";
    private static final String APPROVERS_MUST_BE_UNIQUE = "proposal_Action_exceptionCode.8003";
    private static final String APPROVE_ALL = "proposal_Action_exceptionCode.8004";
    
//    private static final String ERROR_OPENING = "proposal_Action_exceptionCode.8005";
    private static final String ERROR_DURING_BYPASS = "proposal_Action_exceptionCode.8006";
    private static final String ERROR_DURING_APPROVAL = "proposal_Action_exceptionCode.8007";
    private static final String ERROR_DURING_PASS = "proposal_Action_exceptionCode.8008";
    
    private static final String NO_RIGHT_TO_SUBMIT_TO_SPONSOR = "proposal_Action_exceptionCode.8013";
    private static final String APPROVED_STATUS = "proposal_Action_exceptionCode.8014";
    private static final String SUBMITTED_STATUS = "proposal_Action_exceptionCode.8015";
//    private static final String COMMENTS_ENTER = "routing_comments_exceptionCode.1006";
    private static final String SAVE_COMMENTS = "routing_comments_exceptionCode.1007";
    
    private static final String APPROVED = "approved";
    private static final String SUBMITTED = "submitted";
    
    private static final char ROUTING_APPROVE_UPDATE = 'Q';
    private static final char GET_APPROVAL_STATUS_FOR_APPROVER = 'R';
    private static final char UPDATE_PROP_CREATION_STATUS = 'V';
    
    private static final String GENERATE = "G";
    private static final String SUBMISSION_TYPE = "P";
    private static final String SUBMISSION_STATUS = "S";
    
    private static final String BYPASS_COMMENT_PROPDEV = "routing_ByPass_exceptionCode.1001";
    private static final String BYPASS_COMMENT_IRB = "routing_ByPass_exceptionCode.1002";
    private static final String BYPASS_COMMENT_IACUC = "routing_ByPass_exceptionCode.1003";
    private static final String BYPASS_COMMENT_AWARD="routing_ByPass_exceptionCode.1004";
    private static final String ERRKEY_PRIMARY_APP_HAS_ALTERNATE_APP = "proposal_Action_exceptionCode.8018";
    private static final String USER_EXISTS_AS_APPROVER =  " is already present as an approver at this stop";
    private static final char UNLOCK_ROUTING = 'l';
    
    //COEUSQA 2111 STARTS
    private static final String AWARD_SUBMISSION_SERVLET="/AwardSubmissionServlet";
    private static final char UPDATE_AWARD_STATUS='D';
    private static final Integer AWARD_DOC_ROUT_APPROVE=2;
   // private static final Integer AWARD_DOC_ROUT_REJECT=3;
    //COEUSQA 2111 ENDS
      
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();
    private boolean parentProposal;
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private CoeusVector cvModifiedAttachments = new CoeusVector();
    private CoeusVector cvModifiedComments;
    private CommentsTableModel commentsTableModel;
    private AttachmentsTableModel attachmentTableModel;
    
    private int moduleCode;
    private String moduleItemKey;
    private Object moduleBean;
    private RoutingBean routingBean;
    
    private byte[] blobData;
    private boolean fileSelected = false;
    private boolean modifyComment = false;
    private RoutingCommentsBean selectedRoutingCommentsBean;
    
    private boolean saveAttachment = false;
    private boolean saveComment = false;
    private CoeusVector cvCommentsData;
    private static final int COMMENTS_COLUMN = 0;
    private static final int MORE_COMMENTS_COLUMN = 1;
    
    private int submissionStatusCode;
    
    private static final String IRB_PROTOCOL_SUBMISSION_SERVLET = "/protocolSubSrvlt";
    private static final String IACUC_PROTOCOL_SUBMISSION_SERVLET = "/iacucProtocolSubSrvlt";
    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//    private final char UPD_PROT_SUBMISSION_STATUS = 'H';
    private final char UPD_PROT_STATUS_AND_SUBMISSION_STATUS = 'H';
    private final int PROTOCOL_STATUS_SUBMIT_TO_IRB = 101 ;
    private final int PROTOCOL_STATUS_SUBMIT_TO_IACUC = 101 ;
    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    private final String IRB_SUBMISSION_DETAILS_SERVLET = "/SubmissionDetailsServlet" ;
    private final String IACUC_SUBMISSION_DETAILS_SERVLET = "/IacucProtoSubmissionDetailsServlet" ;
    private final int IRB_SUB_STATUS_SUBMIT_TO_COMMITTEE = 100 ;
    private final int IRB_SUB_STATUS_PENDING = 102;
    private final int IACUC_SUB_STATUS_SUBMIT_TO_COMMITTEE = 102 ;
    private final int IACUC_SUB_STATUS_PENDING = 101;
    private String mimeType;//case 4007
    //Added for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
    private boolean byPassClicked = false;
    private static final int BYPASS_WINDOW_WIDTH = 520;
    private static final int BYPASS_WINDOW_HEIGHT = 280 ;
    private static final String BYPASS_ENTER_COMMENTS = "proposal_Action_exceptionCode.8017";
    private boolean closeRequired;
    //COEUSQA-1680 : End
    
    // JM 2-28-2014 rolling our own routing
    private static final char UPDATE_ROUTING_APPROVALS = 'U';
    private static final String ROUTING_QUEUE_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/RoutingQueueServlet";
    // JM END
    
    // JM 7-21-2015 button size 
    private static final int BTNWIDTH = 170; // originally 111
    private static final int BTNHEIGHT = 26;
    // JM END
    
    /**
     * Creates new form RoutingApprovalForm
     *
     * @param parent takes the frame
     * @param modal takes true or false
     */
    public RoutingApprovalForm(Component parent, int moduleCode, String moduleItemKey, boolean modal) {
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        initComponents();
        postInitComponents();
        registerComponents();
    }


     public void setCloseRequired(boolean closeRequired) {
        this.closeRequired = closeRequired;
    }
    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void postInitComponents(){
        tblNewApprovers.formatTable();
        tblNewApprovers.showStatus(false);
        scrPnNewApprovers.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        String title = "Proposal Approval";
        if(isProtocol(moduleCode)){
            title = "Protocol Approval";
        }
        if(moduleCode==1){title="Award Approval";}
        dlgApprovalWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), title, true);
        dlgApprovalWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgApprovalWindow.getContentPane().add(this);
        dlgApprovalWindow.setResizable(false); 
        dlgApprovalWindow.setFont(CoeusFontFactory.getLabelFont());
        
        formatFields(false);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgApprovalWindow.getSize();
        dlgApprovalWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        
        //showBypass(Bypass btn is not visible) is false if all waiting for approvals are set to Bypassed
        formatButtons(showBypass);
        commentsTableModel = new CommentsTableModel();
        attachmentTableModel = new AttachmentsTableModel();
        tblAttachments.setModel(attachmentTableModel);
        tblComments.setModel(commentsTableModel);
        commentsTableModel.fireTableDataChanged();
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
        btnAddAttachments.addActionListener(this);
        btnDeleteAttachments.addActionListener(this);
        btnBrowse.addActionListener(this);
        btnSaveAndNewComment.addActionListener(this);
        btnNewComments.addActionListener(this);
        btnModifyComments.addActionListener(this);
        btnDeleteComments.addActionListener(this);
        
        CommentsRenderer commentsRenderer = new CommentsRenderer();
        CommentsEditor commentsEditor = new CommentsEditor();

        tblComments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = tblComments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,0));        
        TableColumn column = tblComments.getColumnModel().getColumn(COMMENTS_COLUMN);
        column.setMinWidth(272);
        column.setPreferredWidth(272);
        column.setCellRenderer(new DefaultTableCellRenderer());

        column = tblComments.getColumnModel().getColumn(MORE_COMMENTS_COLUMN);
        column.setPreferredWidth(18);
        column.setCellRenderer(commentsRenderer); 
        column.setCellEditor(commentsEditor);
        
        //tblAttachments
        AttachmentsEditor attachmentsEditor = new AttachmentsEditor();
        AttachmentsRenderer attachmentsRenderer = new AttachmentsRenderer();
        
        tblAttachments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableHeader = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
        tblAttachments.setRowHeight(22);
        tblAttachments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblAttachments.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);      
        
        //pdf icon
        TableColumn attachmentColumn = tblAttachments.getColumnModel().getColumn(0);
        attachmentColumn.setPreferredWidth(18);
        attachmentColumn.setCellRenderer(attachmentsRenderer); 
        attachmentColumn.setCellEditor(attachmentsEditor);
        attachmentColumn.setResizable(false);
        
        // description
        attachmentColumn = tblAttachments.getColumnModel().getColumn(1);
        attachmentColumn.setMinWidth(263);
        attachmentColumn.setPreferredWidth(263);
        attachmentColumn.setCellRenderer(new DefaultTableCellRenderer());
        attachmentColumn.setResizable(false);
        
        // #Case 3855 -- start set width of attachment column as 0 so that not visible to the user
        attachmentColumn = tblAttachments.getColumnModel().getColumn(2);
        attachmentColumn.setMinWidth(0);
        attachmentColumn.setPreferredWidth(0);
         // #Case 3855 -- end
        
        dlgApprovalWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgApprovalWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
              // releaseUpdateLock();
                performWindowClosing();
                return;
            }
        });
        
        dlgApprovalWindow.addComponentListener(
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
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnApprove = new javax.swing.JButton();
        btnAddApprover = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnPass = new javax.swing.JButton();
        btnAddAlternate = new javax.swing.JButton();
        lblNewApprovers = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        scrPnNewApprovers = new javax.swing.JScrollPane();
        tblNewApprovers = new edu.mit.coeus.routing.gui.RoutingTable();
        tbdPnAttAndCommts = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        coeusLabel1 = new edu.mit.coeus.utils.CoeusLabel();
        scrPnComment = new javax.swing.JScrollPane();
        txtArComments = new javax.swing.JTextArea();
        btnSaveAndNewComment = new edu.mit.coeus.utils.CoeusButton();
        btnNewComments = new edu.mit.coeus.utils.CoeusButton();
        scrPnCommentsList = new javax.swing.JScrollPane();
        tblComments = new javax.swing.JTable();
        btnDeleteComments = new edu.mit.coeus.utils.CoeusButton();
        btnModifyComments = new edu.mit.coeus.utils.CoeusButton();
        jPanel2 = new javax.swing.JPanel();
        lblDescription = new javax.swing.JLabel();
        txtDescription = new edu.mit.coeus.utils.CoeusTextField();
        lblFileName = new javax.swing.JLabel();
        txtFileName = new edu.mit.coeus.utils.CoeusTextField();
        btnBrowse = new edu.mit.coeus.utils.CoeusButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAttachments = new javax.swing.JTable();
        btnAddAttachments = new edu.mit.coeus.utils.CoeusButton();
        btnDeleteAttachments = new edu.mit.coeus.utils.CoeusButton();

        setLayout(new java.awt.GridBagLayout());

        btnApprove.setFont(CoeusFontFactory.getLabelFont());
        btnApprove.setMnemonic('A');
        btnApprove.setText("Approve");
        btnApprove.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnApprove, gridBagConstraints);

        btnAddApprover.setFont(CoeusFontFactory.getLabelFont());
        btnAddApprover.setMnemonic('r');
        btnAddApprover.setText("Add Additional Approver"); // JM 7-21-2015 changed label for clarification
        btnAddApprover.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        // JM 7-21-2015 added tooltip for this button
        btnAddApprover.setToolTipText(CoeusToolTip.getToolTip("addlApproverBtn_toolTip.1001"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnAddApprover, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnClose, gridBagConstraints);

        btnPass.setFont(CoeusFontFactory.getLabelFont());
        btnPass.setMnemonic('P');
        btnPass.setText("Pass");
        btnPass.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnPass, gridBagConstraints);

        btnAddAlternate.setFont(CoeusFontFactory.getLabelFont());
        btnAddAlternate.setMnemonic('l');
        btnAddAlternate.setText("Add Alternate Approver"); // JM 7-21-2015 changed label for clarification
        btnAddAlternate.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        // JM 7-21-2015 added tooltip for this button
        btnAddAlternate.setToolTipText(CoeusToolTip.getToolTip("altApproverBtn_toolTip.1001"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        add(btnAddAlternate, gridBagConstraints);

        lblNewApprovers.setFont(CoeusFontFactory.getLabelFont());
        lblNewApprovers.setText("New Approvers:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblNewApprovers, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        btnDelete.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-21-2015 set to static dimensions
        btnDelete.setMinimumSize(new java.awt.Dimension(80, 26));
        btnDelete.setPreferredSize(new java.awt.Dimension(80, 26));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnDelete, gridBagConstraints);

        scrPnNewApprovers.setMinimumSize(new java.awt.Dimension(325, 90));
        scrPnNewApprovers.setPreferredSize(new java.awt.Dimension(325, 90));
        scrPnNewApprovers.setViewportView(tblNewApprovers);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(scrPnNewApprovers, gridBagConstraints);

        tbdPnAttAndCommts.setName("tbdPnAttAndCommts");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        coeusLabel1.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel1.add(coeusLabel1, gridBagConstraints);

        scrPnComment.setMinimumSize(new java.awt.Dimension(310, 70));
        scrPnComment.setPreferredSize(new java.awt.Dimension(310, 70));
        txtArComments.setDocument(new LimitedPlainDocument(300));
        txtArComments.setFont(CoeusFontFactory.getNormalFont());
        txtArComments.setLineWrap(true);
        txtArComments.setWrapStyleWord(true);
        scrPnComment.setViewportView(txtArComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        jPanel1.add(scrPnComment, gridBagConstraints);

        btnSaveAndNewComment.setMnemonic('S');
        btnSaveAndNewComment.setText("Save & New");
        btnSaveAndNewComment.setName("btnSaveAndNewComment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 0);
        jPanel1.add(btnSaveAndNewComment, gridBagConstraints);

        btnNewComments.setMnemonic('N');
        btnNewComments.setText("New");
        btnNewComments.setName("btnNewComments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 95, 3, 0);
        jPanel1.add(btnNewComments, gridBagConstraints);

        scrPnCommentsList.setMinimumSize(new java.awt.Dimension(310, 70));
        scrPnCommentsList.setPreferredSize(new java.awt.Dimension(310, 70));
        tblComments.setName("tblComments");
        scrPnCommentsList.setViewportView(tblComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel1.add(scrPnCommentsList, gridBagConstraints);

        btnDeleteComments.setMnemonic('e');
        btnDeleteComments.setText("Delete");
        btnDeleteComments.setName("btnDeleteComments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(30, 0, 0, 0);
        jPanel1.add(btnDeleteComments, gridBagConstraints);

        btnModifyComments.setMnemonic('M');
        btnModifyComments.setText("Modify");
        btnModifyComments.setName("btnModifyComments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(btnModifyComments, gridBagConstraints);

        tbdPnAttAndCommts.addTab("Comments", jPanel1);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMinimumSize(new java.awt.Dimension(370, 200));
        jPanel2.setPreferredSize(new java.awt.Dimension(370, 200));
        lblDescription.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblDescription.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDescription.setText("Description: ");
        lblDescription.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblDescription.setName("lblDescription");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        jPanel2.add(lblDescription, gridBagConstraints);

        txtDescription.setDocument(new LimitedPlainDocument(200));
        txtDescription.setMinimumSize(new java.awt.Dimension(210, 21));
        txtDescription.setName("txtDescription");
        txtDescription.setPreferredSize(new java.awt.Dimension(200, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel2.add(txtDescription, gridBagConstraints);

        lblFileName.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));
        lblFileName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFileName.setText("File Name: ");
        lblFileName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblFileName.setMaximumSize(new java.awt.Dimension(72, 14));
        lblFileName.setMinimumSize(new java.awt.Dimension(72, 14));
        lblFileName.setName("lblFileName");
        lblFileName.setPreferredSize(new java.awt.Dimension(72, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 0);
        jPanel2.add(lblFileName, gridBagConstraints);

        txtFileName.setDocument(new LimitedPlainDocument(300));
        txtFileName.setEditable(false);
        txtFileName.setMinimumSize(new java.awt.Dimension(200, 21));
        txtFileName.setName("txtFileName");
        txtFileName.setPreferredSize(new java.awt.Dimension(180, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        jPanel2.add(txtFileName, gridBagConstraints);

        btnBrowse.setMnemonic('B');
        btnBrowse.setText("Browse...");
        btnBrowse.setName("btnBrowse");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(btnBrowse, gridBagConstraints);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(300, 120));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(300, 120));
        tblAttachments.setName("tblAttachments");
        jScrollPane1.setViewportView(tblAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        btnAddAttachments.setMnemonic('d');
        btnAddAttachments.setText("Add");
        btnAddAttachments.setName("btnAddAttachments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(btnAddAttachments, gridBagConstraints);

        btnDeleteAttachments.setMnemonic('t');
        btnDeleteAttachments.setText("Delete");
        btnDeleteAttachments.setName("btnDeleteAttachments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(btnDeleteAttachments, gridBagConstraints);

        tbdPnAttAndCommts.addTab("Attachments", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(tbdPnAttAndCommts, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAlternate;
    private javax.swing.JButton btnAddApprover;
    private edu.mit.coeus.utils.CoeusButton btnAddAttachments;
    private javax.swing.JButton btnApprove;
    private edu.mit.coeus.utils.CoeusButton btnBrowse;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private edu.mit.coeus.utils.CoeusButton btnDeleteAttachments;
    private edu.mit.coeus.utils.CoeusButton btnDeleteComments;
    private edu.mit.coeus.utils.CoeusButton btnModifyComments;
    private edu.mit.coeus.utils.CoeusButton btnNewComments;
    private javax.swing.JButton btnPass;
    private edu.mit.coeus.utils.CoeusButton btnSaveAndNewComment;
    private edu.mit.coeus.utils.CoeusLabel coeusLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblNewApprovers;
    private javax.swing.JScrollPane scrPnComment;
    private javax.swing.JScrollPane scrPnCommentsList;
    private javax.swing.JScrollPane scrPnNewApprovers;
    private javax.swing.JTabbedPane tbdPnAttAndCommts;
    private javax.swing.JTable tblAttachments;
    private javax.swing.JTable tblComments;
    private edu.mit.coeus.routing.gui.RoutingTable tblNewApprovers;
    private javax.swing.JTextArea txtArComments;
    private edu.mit.coeus.utils.CoeusTextField txtDescription;
    private edu.mit.coeus.utils.CoeusTextField txtFileName;
    // End of variables declaration//GEN-END:variables
    
    /** Set the form data
     * @param data holds the data for display in the form
     */
    public void setFormData(Object data){
        populateAttachmentsTable();
        populateCommentsTable();
        cvExistingApprovers = (CoeusVector)data;
    }
    
    /** Method to set the buttons properties when showBypass flag is set
     * @param showBypass holds true if set, false otherwise
     */
    public void formatButtons(boolean showBypass){
        this.showBypass = showBypass;
        if( showBypass ){
            //Added for case 3612 - Routing Issues - start
            //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
//            dlgApprovalWindow.setTitle("Bypass");
            String title = "Proposal Bypass";
            if(isProtocol(moduleCode)){
                title = "Protocol Bypass";
            }
            if(moduleCode==1){title="Award Bypass";}
            dlgApprovalWindow.setTitle(title);
            //COEUSQA-1680 : End
            //Added for case 3612 - Routing Issues - end
            btnApprove.setText(BYPASS);
            btnApprove.setMnemonic(BYPASS_MNEMONIC);
            btnAddApprover.setVisible(false);
            btnAddAlternate.setVisible(false);
            btnPass.setVisible(false);
            btnClose.setText(CANCEL);
            //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
            //Bypass specific comments are disabled, enabled the comments and attachment used 
            //for approval, during Bypass user can add mutiple comments and upload multiple documents
//            txtArCommentsBypass.setText(BYPASS_COMMENT);
//            txtArCommentsBypass.setCaretPosition(0);
//            tbdPnAttAndCommts.remove(1);
//            btnSaveAndNewComment.setVisible(false);
//            btnNewComments.setVisible(false);
//            btnModifyComments.setVisible(false);
//            btnDeleteComments.setVisible(false);
//            scrPnCommentsList.setVisible(false);  
//            scrPnComments.setMinimumSize(new Dimension(300, 110));
//            scrPnComments.setPreferredSize(new Dimension(300, 110));
//            tbdPnAttAndCommts.setVisible(true);            
//            scrPnComments.setVisible(false);
            //btnClose.ge
//            dlgApprovalWindow.setSize(400,220);
            if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                txtArComments.setText(coeusMessageResources.parseMessageKey(BYPASS_COMMENT_IRB));
            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                txtArComments.setText(coeusMessageResources.parseMessageKey(BYPASS_COMMENT_IACUC));
            }else if (moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                txtArComments.setText(coeusMessageResources.parseMessageKey(BYPASS_COMMENT_AWARD));
            }
            else {
                txtArComments.setText(coeusMessageResources.parseMessageKey(BYPASS_COMMENT_PROPDEV));
            }
            txtArComments.setCaretPosition(0);
            tbdPnAttAndCommts.setVisible(true);
            dlgApprovalWindow.setSize(BYPASS_WINDOW_WIDTH,BYPASS_WINDOW_HEIGHT);
            
            //COEUSQA-1680 : End
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
            //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
//            btnClose.setText(CLOSE);
            btnClose.setText(CANCEL);
            //COEUSQA-1680 : End
//            scrPnComments.setVisible(false);
        }
    }
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if( source.equals(btnApprove) ){
            //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium - start
            if(validateAttachmentsForSave()) {
               if(isAttachmentSaveRequired()) {
                 performAttachmentsAction("Add");
               }
               //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium - end
               if( showBypass ){
                   performBypassAction();
               }else{
                   performApproveAction();
               }
            }
        }else if( source.equals(btnAddApprover) ){
            performAddApproverAction();
        }else if( source.equals(btnAddAlternate) ){
            performAddAlternateAction();
        }else if( source.equals(btnPass) ){
             //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium  - start
            if(validateAttachmentsForSave()) {
                if(isAttachmentSaveRequired()) {
                    performAttachmentsAction("Add");
                }
                //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium  - end
                performPassAction();
            }
        }else if( source.equals(btnClose) ){
             // releaseUpdateLock();
            performWindowClosing();
        }else if( source.equals(btnDelete) ){
            performDeleteAction();
        }else if(source.equals(btnAddAttachments)){
            performAttachmentsAction("Add");
        }else if(source.equals(btnDeleteAttachments)){
            performAttachmentsAction("Delete");
            attachmentTableModel.fireTableDataChanged();
        }else if(source.equals(btnBrowse)){
            performBrowseAction();
        }else if(source.equals(btnNewComments)){
            if(txtArComments.getText().trim().length() > 0 ){
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_COMMENTS),
                            CoeusOptionPane.OPTION_YES_NO,2);
                switch( option ){
                    case ( JOptionPane.YES_OPTION ):
                             if(modifyComment){
                                 performCommentsAction("Update");
                            }else{
                                    performCommentsAction("Add");
                            }
                            break;
                    case ( JOptionPane.NO_OPTION ):
                        break;
                    case ( JOptionPane.CANCEL_OPTION ) :
                        break;
                }
            }
            txtArComments.requestFocus();
            txtArComments.setText("");
            modifyComment = false;
            selectedRoutingCommentsBean = null;
        }else if(source.equals(btnSaveAndNewComment)){
            if(modifyComment){
                performCommentsAction("Update");
            }else{
                performCommentsAction("Add");
            }
        }else if(source.equals(btnModifyComments)){
            performCommentsAction("Modify");
        }else if(source.equals(btnDeleteComments)){
            performCommentsAction("Delete");
        }
        
    }
    public void performBrowseAction(){
        CoeusFileChooser fileChooser = new CoeusFileChooser(dlgApprovalWindow);
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.showFileChooser();
        if(fileChooser.isFileSelected()){
            String fileName = fileChooser.getSelectedFile();
            if(fileName != null && !fileName.trim().equals("")){
                int index = fileName.lastIndexOf('.');
                if(index != -1 && index != fileName.length()){
                    setFileSelected(true);
                    txtFileName.setText(fileChooser.getFileName().getName());
                    setBlobData(fileChooser.getFile());
                    //Added with case 4007: Icon based on mime type : Start
                    CoeusDocumentUtils coeusDocumentUtils = CoeusDocumentUtils.getInstance();
                    CoeusAttachmentBean attachmentBean = new CoeusAttachmentBean(fileName,getBlobData());
                    setMimeType(coeusDocumentUtils.getDocumentMimeType(attachmentBean));
                    //4007:End
                }else{
                    CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                            "correspType_exceptionCode.1012"));
                    setFileSelected(false);
                    setBlobData(null);
                    setMimeType(null);//case 4007
                }
            }
        }
    }
    public void performAttachmentsAction(String action){
        if(action.equals("Add")){
            if(validateAttachmentData()){
                RoutingAttachmentBean routingAttachmentBean = new RoutingAttachmentBean();
                routingAttachmentBean.setRoutingNumber(currentRoutingDetailBean.getRoutingNumber());
                routingAttachmentBean.setApproverNumber(currentRoutingDetailBean.getApproverNumber());
                routingAttachmentBean.setFileBytes(getBlobData());
                routingAttachmentBean.setMimeType(getMimeType());//case 4007
                routingAttachmentBean.setDescription(txtDescription.getText());
                routingAttachmentBean.setFileName(txtFileName.getText());
                routingAttachmentBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
                routingAttachmentBean.setMapNumber(currentRoutingDetailBean.getMapNumber());
                routingAttachmentBean.setStopNumber(currentRoutingDetailBean.getStopNumber());
                routingAttachmentBean.setAcType("I");
                if(currentRoutingDetailBean!=null){
                    if(currentRoutingDetailBean.getAttachments()==null){
                        CoeusVector cvAttachments = new CoeusVector();
                        cvAttachments.add(routingAttachmentBean);
                        currentRoutingDetailBean.setAttachments(cvAttachments);
                        cvModifiedAttachments.addAll(cvAttachments);/**/
                        
                    }else{
                        currentRoutingDetailBean.getAttachments().add(routingAttachmentBean);
                        cvModifiedAttachments.add(routingAttachmentBean);/**/
                    }
                  
                }
                clearAttachmentsFormData();
                saveAttachment = true;
            }
        }else if(action.equals("Delete")){
            int selectedRow = tblAttachments.getSelectedRow();
            if(selectedRow!=-1){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1004"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
                switch(option){
                    case (CoeusOptionPane.SELECTION_YES):
                        RoutingAttachmentBean routingAttachmentBean =
                                (RoutingAttachmentBean)currentRoutingDetailBean.getAttachments().get(selectedRow);
                        if(routingAttachmentBean!=null){
                            if(routingAttachmentBean.getAcType()!=null &&
                                    routingAttachmentBean.getAcType().equals("I")){
                                cvModifiedAttachments.remove(selectedRow);
                                currentRoutingDetailBean.getAttachments().remove(selectedRow);
                            }
                        }
                        saveAttachment = true;
                        break;
                    case (CoeusOptionPane.SELECTION_NO):
                        break;
                    default:
                        break;
                }
            }else{
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1005"));
            }
        }
        attachmentTableModel.setData(cvModifiedAttachments);
        attachmentTableModel.fireTableDataChanged();
    }
    
    public void clearAttachmentsFormData(){
        txtFileName.setText("");
        txtDescription.setText("");
        setFileSelected(false);
        setBlobData(new byte[0]);
        setMimeType(null);
    }
    
    public void clearCommentsFormData(){
        txtArComments.setText("");
        modifyComment = false;
        selectedRoutingCommentsBean = null;
    }
    public boolean validateAttachmentData(){
        if(txtDescription.getText().trim().length() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1000"));
            txtDescription.requestFocusInWindow();
            return false;
        }
        
        if(txtFileName.getText().trim().length() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1001"));
            return false;
        }
        
        if(isFileSelected()){
            if(getBlobData().length == 0 ){
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1002"));
                return false;
            }
        }else{
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1001"));
            return false;
        }
        return true;
    }
    
    public boolean validateAttachmentsForSave() {
        if(txtDescription.getText().trim().length() == 0 && txtFileName.getText().trim().length() != 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1000"));
            txtDescription.requestFocusInWindow();
            return false;
        }
        else if(txtDescription.getText().trim().length() != 0 && txtFileName.getText().trim().length() == 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey("routing_attachments_exceptionCode.1001"));
            return false;
        }
        return true;
    }
    
    public boolean isAttachmentSaveRequired() {
         if(txtDescription.getText().trim().length() != 0 && txtFileName.getText().trim().length() != 0){
            return true;
         } 
         return false;
    }
    
    public void performCommentsAction(String action){
        if(action.equals("Add")){
            if(txtArComments.getText().trim().length()==0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1006"));
                return;
            }
            RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
            routingCommentsBean.setRoutingNumber(currentRoutingDetailBean.getRoutingNumber());
            routingCommentsBean.setApproverNumber(currentRoutingDetailBean.getApproverNumber());
            routingCommentsBean.setComments(txtArComments.getText());
            routingCommentsBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
            routingCommentsBean.setMapNumber(currentRoutingDetailBean.getMapNumber());
            routingCommentsBean.setStopNumber(currentRoutingDetailBean.getStopNumber());
            routingCommentsBean.setAcType("I");
            if(currentRoutingDetailBean!=null){
                if(currentRoutingDetailBean.getComments()==null){
                    CoeusVector cvComments = new CoeusVector();
                    cvComments.add(routingCommentsBean);
                    currentRoutingDetailBean.setComments(cvComments);
                }else{
                    currentRoutingDetailBean.getComments().add(routingCommentsBean);
                }
            }
            populateCommentsTable();
            
            txtArComments.setText("");
           
            saveComment = true;
        }else if(action.equals("Delete")){
            int selectedRow = tblComments.getSelectedRow();
            if(selectedRow!=-1){
                RoutingCommentsBean routingCommentsBean =
                        (RoutingCommentsBean)currentRoutingDetailBean.getComments().get(selectedRow);
//                txtArComments.setText(routingCommentsBean.getComments());
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
                switch(option){
                    case CoeusOptionPane.SELECTION_YES:
                        if(routingCommentsBean!=null){
                            if(routingCommentsBean.getAcType()!=null &&
                                    routingCommentsBean.getAcType().equals("I") ){
                                currentRoutingDetailBean.getComments().remove(selectedRow);
                            }else if(routingCommentsBean.getAcType()==null ||
                                    (routingCommentsBean.getAcType()!=null && routingCommentsBean.getAcType().equals("U"))){
                                routingCommentsBean.setAcType("D");
                                cvModifiedComments.add(routingCommentsBean);
                                currentRoutingDetailBean.getComments().remove(selectedRow);
                            }
                        }
                }
                populateCommentsTable();
 
                saveComment = true;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1003"));
            }
            //Commented for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
//            clearCommentsFormData();
            //COEUSQA-1680 : End
        }else if(action.equals("Modify")){
            int selectedRow = tblComments.getSelectedRow();
            if(selectedRow!=-1){
                selectedRoutingCommentsBean =
                        (RoutingCommentsBean)currentRoutingDetailBean.getComments().get(selectedRow);
                if(selectedRoutingCommentsBean!=null){
                    txtArComments.setText(selectedRoutingCommentsBean.getComments());
                }
                modifyComment = true;
                saveComment = true;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1001"));
            }
        }else if(action.equals("Update")){
            if(txtArComments.getText().trim().length()==0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1006"));
                return;
            }
            if(selectedRoutingCommentsBean!=null){
                if(selectedRoutingCommentsBean.getAcType()==null){
                    selectedRoutingCommentsBean.setAcType("U");
                }
                selectedRoutingCommentsBean.setComments(txtArComments.getText());
                populateCommentsTable();
            }
            clearCommentsFormData();
        }
    }
    /** Allows the user to bypass the proposal */
    private void performBypassAction(){
        //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
        //Bypass action will perform only if atleast one comments are entered, othere wise warning message is thrown
//           currentRoutingDetailBean.setAction(BYPASS_ACTION);
//            currentRoutingDetailBean.setApproveAll(0);
//            setSaveRequired(true);
//            actionButtonClicked = true;
//            saveFormData()
        if((txtArComments.getText().length() > 0) ||  currentRoutingDetailBean.getComments() != null
                && currentRoutingDetailBean.getComments().size() > 0 ){
            currentRoutingDetailBean.setAction(BYPASS_ACTION);
            currentRoutingDetailBean.setApproveAll(0);
            //Added for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
            byPassClicked = true;
            //COEUSQA-1680 : End
            setSaveRequired(true);
            actionButtonClicked = true;
            saveFormData();
        } else{
            txtArComments.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    BYPASS_ENTER_COMMENTS));
        }
        //COEUSQA-1680 : end
    }
    
    /** Allows the user to approve the proposal */
    private void performApproveAction(){
        //Call save method first, if save failed
        //display msg "An error was encountered during the approval process"
        checkOtherLevels();
        currentRoutingDetailBean.setAction(APPROVE_ACTION);
        currentRoutingDetailBean.setApproveAll(approveAll);
        approveClicked = true;
        setSaveRequired(true);
        actionButtonClicked = true;
        saveFormData()  ;
    }
    
    public void setAttachmentCommentToBean(){
        //Get all the attachment bean to be saved
        /*if(saveAttachment){
            CoeusVector cvAttachmentsToSave = new CoeusVector();
            if(currentRoutingDetailBean.getAttachments()!=null){
                RoutingAttachmentBean routingAttachmentBean = null;
                for(int i=0; i<currentRoutingDetailBean.getAttachments().size(); i++){
                    routingAttachmentBean = (RoutingAttachmentBean)currentRoutingDetailBean.getAttachments().get(i);
                    if(routingAttachmentBean.getAcType()!=null &&
                            (routingAttachmentBean.getAcType().equals("I") ||
                            routingAttachmentBean.getAcType().equals("U"))){
                        cvAttachmentsToSave.add(routingAttachmentBean);
                    }
                }
            }
            if(cvModifiedAttachments!=null){
                cvAttachmentsToSave.addAll(cvModifiedAttachments);
            }
            if(currentRoutingDetailBean!=null){
                currentRoutingDetailBean.setAttachments(cvAttachmentsToSave);
            }
        }*/
        
        //Get all the comment beans to be saved
        if(saveComment){
            CoeusVector cvCommentsToSave = new CoeusVector();
            if(currentRoutingDetailBean.getComments()!=null){
                RoutingCommentsBean routingCommentsBean = null;
                for(int i=0; i<currentRoutingDetailBean.getComments().size(); i++){
                    routingCommentsBean = (RoutingCommentsBean)currentRoutingDetailBean.getComments().get(i);
                    if(routingCommentsBean.getAcType()!=null &&
                            (routingCommentsBean.getAcType().equals("I") ||
                            routingCommentsBean.getAcType().equals("U"))){
                        cvCommentsToSave.add(routingCommentsBean);
                    }
                }
            }
            if(cvModifiedComments!=null){
                cvCommentsToSave.addAll(cvModifiedComments);
            }
            if(currentRoutingDetailBean!=null){
                currentRoutingDetailBean.setComments(cvCommentsToSave);
            }
        }
        
    }
    
    /** Allows the user to pass the proposal */
    private void performPassAction(){
        //Modified for case 3612 - Routing Issues - start
        if( txtArComments.getText().trim().length() > 0 || 
                (currentRoutingDetailBean.getComments()!=null && currentRoutingDetailBean.getComments().size() >0)){
            //Modified for case 3612 - Routing Issues - end
            currentRoutingDetailBean.setAction(PASS_ACTION);
            currentRoutingDetailBean.setApproveAll(0);
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
        Vector vecData = new Vector();
        requester.setFunctionType(GET_APPROVAL_STATUS_FOR_APPROVER);
        requester.setDataObject(currentRoutingDetailBean);
        String CONNECT_TO = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECT_TO, requester);
        comm.send();
        
        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                vecData = response.getDataObjects();
            }
        }
        
        Vector vecFilteredData = new Vector();
        
        if(vecData != null && vecData.size() >0){
            for(int index = 0; index < vecData.size(); index++){
                RoutingDetailsBean routingDetailsBean = 
                        (RoutingDetailsBean) vecData.get(index);
                if(routingDetailsBean != null &&
                        (routingDetailsBean.getApprovalStatus().equals("W")
                            || routingDetailsBean.getApprovalStatus().equals("T"))){
                    if(!(currentRoutingDetailBean.getMapNumber() == routingDetailsBean.getMapNumber()
                        && currentRoutingDetailBean.getLevelNumber() == routingDetailsBean.getLevelNumber()
                        && currentRoutingDetailBean.getStopNumber() == routingDetailsBean.getStopNumber())){
                        vecFilteredData.add(routingDetailsBean);
                    }
                }
                
            }
        }
       
        /* JM 10-12-2011 removed - don't want this feature 
        if( vecFilteredData != null && vecFilteredData.size() > 0 ){
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
        }else{
            approveAll = 0;
        } */
    }
    
    /** Method to add alternate to the selected primary approver */
    private void performAddAlternateAction(){
        //Check if any approver is selected
        selectedApprover = tblNewApprovers.getSelectedRow();
        if( selectedApprover >= 0 ){
            //selectedRoutingDetailBean = (ProposalApprovalBean)tblNewApprovers.getTableData().get(selectedApprover);
            selectedRoutingDetailBean = (RoutingDetailsBean)tblNewApprovers.getTableData().get(selectedApprover);
            if(selectedRoutingDetailBean!=null && selectedRoutingDetailBean.isPrimaryApproverFlag()){
                //Indicate that this is an alternate approver
                alternateApprover = true;
                selectedApprover = selectedApprover + 1;
                performAddApproverAction();
            }else{
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SELECT_PRIMARY_APPROVER));
            }
        }else{
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_PRIMARY_APPROVER));
        }
    }
    
    /** Displays the search screen to specify a search criteria
     */
    private void performAddApproverAction(){
        CoeusVector cvApprover = new CoeusVector();
        Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(currentRoutingDetailBean.getMapNumber() ));
        Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(currentRoutingDetailBean.getLevelNumber() ));
        And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber); 
        CoeusVector cvTempData;
        if( !alternateApprover ){
            //Equals eqMapId = new Equals(MAP_ID_FIELD, new Integer(currentRoutingDetailBean.getMapId() ));
            RoutingDetailsBean tempRoutingDetailsBean = new RoutingDetailsBean();
            //Prepare to add a new approver
            if ( nextStopNumber > 0 ){
                nextStopNumber++;
            }else{
                //Filter and get the beans satisfying the filter criteria
                cvTempData = cvExistingApprovers.filter(eqMapIdAndEqLevelNumber);
                if( cvTempData != null && cvTempData.size() > 0 ){
                    //Get the max stop number
                    cvTempData.sort(STOP_NUMBER_FIELD, false);
                    tempRoutingDetailsBean = (RoutingDetailsBean)cvTempData.get(0);
                    nextStopNumber = tempRoutingDetailsBean.getStopNumber() + 1;
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
            } else {
                //Does Not Exists
                dlgApprovalWindow.setVisible(false);
                formatFields(true);
                
                //Create a new ProposalApprovalBean and add it to the table
//                ProposalApprovalBean propApprovalBean = new ProposalApprovalBean();
//                propApprovalBean.setAcType(TypeConstants.INSERT_RECORD);
//                propApprovalBean.setUserId(userInfoBean.getUserId().trim());
//                propApprovalBean.setUserName(userInfoBean.getUserName());
//                propApprovalBean.setProposalNumber(currentRoutingDetailBean.getProposalNumber());
//                propApprovalBean.setMapId(currentRoutingDetailBean.getMapId());
//                propApprovalBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
//                propApprovalBean.setDescription(DESCRIPTION + mdiForm.getUserName());
//                propApprovalBean.setApprovalStatus(WAITING_FOR_APPROVAL);
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                routingDetailsBean.setUserId(userInfoBean.getUserId().trim());
                routingDetailsBean.setUserName(userInfoBean.getUserName());
                routingDetailsBean.setRoutingNumber(currentRoutingDetailBean.getRoutingNumber());
                routingDetailsBean.setMapNumber(currentRoutingDetailBean.getMapNumber());
                routingDetailsBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
                routingDetailsBean.setDescription(DESCRIPTION + mdiForm.getUserName());
                routingDetailsBean.setApprovalStatus("W");
                if( alternateApprover ){
                    // Add alternate approvers to same stop
                    routingDetailsBean.setStopNumber(selectedRoutingDetailBean.getStopNumber());
                    eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(selectedRoutingDetailBean.getMapNumber() ));
                    eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(selectedRoutingDetailBean.getLevelNumber() ));
                    eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);                    
                    CoeusVector cvApprovers = tblNewApprovers.getTableData();
                    cvApprovers = (cvApprovers == null)? new CoeusVector() : cvApprovers;
                    Equals eqStopNumber = new Equals("stopNumber", new Integer(selectedRoutingDetailBean.getStopNumber()));
                    And eqStopNumAndMapIdAndEqLevelNum = new And( eqStopNumber, eqMapIdAndEqLevelNumber); 
                    cvTempData = cvApprovers.filter(eqStopNumAndMapIdAndEqLevelNum);
                    if(cvTempData == null){
                        routingDetailsBean.setApproverNumber(1);
                    } else {
                        routingDetailsBean.setApproverNumber(cvTempData.size()+1);
                    }
                    routingDetailsBean.setPrimaryApproverFlag(false);
                    
                    //Insert new row after selected row
                    cvApprover.addAll(0, cvApprovers);
                    cvApprover.add(selectedApprover, routingDetailsBean);
                    alternateApprover = false;
                }else{
                    // Add primary approvers to new stop                  
                    routingDetailsBean.setPrimaryApproverFlag(true);
                    routingDetailsBean.setStopNumber(nextStopNumber);
                    routingDetailsBean.setApproverNumber(1);
                    if( tblNewApprovers.getTableData() != null ){
                        cvApprover.addAll(0, tblNewApprovers.getTableData());
                    }
                    cvApprover.addElement(routingDetailsBean);
                }
                tblNewApprovers.setTableData(cvApprover);
                dlgApprovalWindow.setVisible(true);
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
                if(userId.equalsIgnoreCase(((RoutingDetailsBean)tblNewApprovers.getTableData().get(index)).getUserId())) {
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                            APPROVERS_MUST_BE_UNIQUE));
                    return true;
                }
            }
        }
        //Filter to get all the records for the current level number
        Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD,
                new Integer(currentRoutingDetailBean.getLevelNumber()));
        CoeusVector cvCurrenLevelApprovers = cvExistingApprovers.filter(eqLevelNumber);
        
        //Check with the existing approvers for the current level in Proposal Routing screen
        for(int index = 0; index < cvCurrenLevelApprovers.size(); index++) {
            if(userId.equalsIgnoreCase(((RoutingDetailsBean)cvCurrenLevelApprovers.get(index)).getUserId())) {
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
            selectedRoutingDetailBean = (RoutingDetailsBean)tblNewApprovers.getTableData().get(selectedRow);
            
            //Validate if there are alternate approvers for the primary approver
            if(selectedRoutingDetailBean!=null && selectedRoutingDetailBean.isPrimaryApproverFlag()){
                  
                Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(selectedRoutingDetailBean.getMapNumber() ));
                Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(selectedRoutingDetailBean.getLevelNumber() ));
                Equals eqStopNumber = new Equals("stopNumber", new Integer(selectedRoutingDetailBean.getStopNumber()));
                And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
                And eqStopNumAndMapIdAndEqLevelNum = new And( eqStopNumber, eqMapIdAndEqLevelNumber);
                
                CoeusVector cvApprovers = tblNewApprovers.getTableData();
                cvApprovers = (cvApprovers == null)? new CoeusVector() : cvApprovers;
                CoeusVector cvTempData = cvApprovers.filter(eqStopNumAndMapIdAndEqLevelNum);
                if( cvTempData.size() > 1 ){
                    //Cannot delete since the primary approver has alternate approver(s) 
                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(ERRKEY_PRIMARY_APP_HAS_ALTERNATE_APP));
                    return;
                }
            }
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
        // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
//        releaseUpdateLock();
        // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
        //If rows are added in the new approvers table
        if( tblNewApprovers.getTableData() != null &&
                tblNewApprovers.getTableData().size() > 0 ){
             
            setSaveRequired(true);
        }


        //Commented for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start        
        //For Bypass also txtArComments - TextArea is Used instead of txtArCommentsBypass
        //If comments are added/modified
//        if( showBypass && txtArCommentsBypass.getText().trim().length() > 0 ){
//                setSaveRequired(true);
//        }
//      //COEUSQA-1680 : End
        if(saveAttachment || saveComment){
            setSaveRequired(true);
        }
        
        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            String messageKey = "saveConfirmCode.1002";
            //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start        
            //Provided save dialog for Bypass also
//            if(saveComment && saveAttachment){
//                messageKey = "routing_attachments_exceptionCode.1007";
//            }else if(saveComment){
//                messageKey = "routing_comments_exceptionCode.1004";
//            }else if(saveAttachment){
//                messageKey = "routing_attachments_exceptionCode.1006";
//            }
            if(showBypass){
                if(saveComment && saveAttachment){
                    messageKey = "routing_attachments_exceptionCode.1010";
                }else if(saveComment){
                    messageKey = "routing_comments_exceptionCode.1011";
                }else if(saveAttachment){
                    messageKey = "routing_attachments_exceptionCode.1012";
                }
            }else{
                if(saveComment && saveAttachment){
                    messageKey = "routing_attachments_exceptionCode.1007";
                }else if(saveComment){
                    messageKey = "routing_comments_exceptionCode.1004";
                }else if(saveAttachment){
                    messageKey = "routing_attachments_exceptionCode.1006";
                }
            }
            //COEUSQA-1680 : End
            option
                    = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(
                    messageKey),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    try{
                        if(showBypass){
                             performBypassAction();
                        } else {
                            performApproveAction();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgApprovalWindow.setVisible(false);
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    break;
            }
        }else{
            dlgApprovalWindow.setVisible(false);
        }
        
    }
    
    /** Saves the form data
     */
    private void saveFormData(){
        if ( !isSaveRequired() ){
            dlgApprovalWindow.setVisible(false);
            return;
        }
        //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
        //Save to the database
//        if( ( txtArCommentsBypass.getText().trim().length() > 0 ) || approveClicked ||
        if( byPassClicked || approveClicked || //COEUSQA-1680 : End
                ( tblNewApprovers.getTableData() != null && tblNewApprovers.getTableData().size() > 0 ) ){
            
            RequesterBean requester = new RequesterBean();
            // JM 2-28-2014 rolling our own routing
            //requester.setFunctionType(ROUTING_APPROVE_UPDATE);
            requester.setFunctionType(UPDATE_ROUTING_APPROVALS);
            // JM END
            
            Vector vecData = new Vector();
            CoeusVector cvNewApprovers = null;
            if( tblNewApprovers.getTableData() != null &&
                    tblNewApprovers.getTableData().size() > 0 ){
                vecData.addElement(tblNewApprovers.getTableData());
            }else{
                vecData.addElement(cvNewApprovers);
            }
            
             if(txtArComments.getText().trim().length() > 0 ){
                //Code commented for Case#2785 - Routing Enhancement save confirmation issue - starts
//                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_COMMENTS),
//                            CoeusOptionPane.OPTION_YES_NO,2);
//                switch( option ){
//                    case ( JOptionPane.YES_OPTION ):
                //Code commented for Case#2785 - Routing Enhancement save confirmation issue - ends
                             if(modifyComment){
                                 performCommentsAction("Update");
                            }
                             else {
                                    performCommentsAction("Add");
                            }
                     //Code commented for Case#2785 - Routing Enhancement save confirmation issue - starts
//                            break;
//                    case ( JOptionPane.NO_OPTION ):
//                        break;
//                    case ( JOptionPane.CANCEL_OPTION ) :
//                        break;
//                }
                 //Code commented for Case#2785 - Routing Enhancement save confirmation issue - ends
            }
            //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start            
//            if( !(txtArCommentsBypass.getText().trim().equals(EMPTY_STRING)) ){
            if( !(txtArComments.getText().trim().equals(EMPTY_STRING)) ){//COEUSQA-1680 : End
                RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
                routingCommentsBean.setRoutingNumber(currentRoutingDetailBean.getRoutingNumber());
                routingCommentsBean.setApproverNumber(currentRoutingDetailBean.getApproverNumber());
                routingCommentsBean.setComments(txtArComments.getText());
                routingCommentsBean.setLevelNumber(currentRoutingDetailBean.getLevelNumber());
                routingCommentsBean.setMapNumber(currentRoutingDetailBean.getMapNumber());
                routingCommentsBean.setStopNumber(currentRoutingDetailBean.getStopNumber());
                routingCommentsBean.setAcType("I");
                if(currentRoutingDetailBean!=null){
                    if(currentRoutingDetailBean.getComments()==null){
                        CoeusVector cvComments = new CoeusVector();
                        cvComments.add(routingCommentsBean);
                        currentRoutingDetailBean.setComments(cvComments);
                    }else{
                        currentRoutingDetailBean.getComments().add(routingCommentsBean);
                    }
                }
            }
            
            setAttachmentCommentToBean();
            currentRoutingDetailBean.setAcType(TypeConstants.UPDATE_RECORD);
            vecData.addElement(currentRoutingDetailBean);
            vecData.addElement(routingBean);
            requester.setDataObjects(vecData);
            String CONNECT_TO = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
            // JM 3-5-2014 rolling our own routing
            //AppletServletCommunicator comm = new AppletServletCommunicator(CONNECT_TO, requester);
            AppletServletCommunicator comm = new AppletServletCommunicator(ROUTING_QUEUE_SERVLET, requester);
            // JM END
            comm.send();
            
            ResponderBean response = comm.getResponse();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    setSaveRequired(false);
                    Vector vecValue = response.getDataObjects();
                    if( vecValue.get(0) != null){
                        int returnValue = ((Integer)vecValue.get(0)).intValue();
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
                        ProposalDevelopmentFormBean proposalDevelopmentFormBean = new ProposalDevelopmentFormBean();
                        //If module is proposal, in order to check narrative is incomplete, we need to get value ofindex of 1.
                        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            if(vecValue.get(1) != null){
                                proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) vecValue.get(1);
                            }
                        }
                        //performPostSaveAction(returnValue);
                        performPostSaveAction(returnValue,proposalDevelopmentFormBean);
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
                    }
                    //Close the form after saving
                    dlgApprovalWindow.setVisible(false);
                    if( actionButtonClicked ){
                        approvalActionPerformed = true;
                        //Reset the flag since updation is successful
                        actionButtonClicked = false;
                    }
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if(isParentProposal()){
                            // COEUSDEV-146: Proposal Hierarchy - Child proposal status does not change when parent propsoal is submitted to sponsor 
                            ProposalDevelopmentFormBean proposalDevelopmentFormBean = (ProposalDevelopmentFormBean) vecValue.get(1);
                            updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                        }
                    }
                }else{
                    if( currentRoutingDetailBean.getAction().equals(APPROVE_ACTION) &&
                            actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(ERROR_DURING_APPROVAL));
                    }else if( currentRoutingDetailBean.getAction().equals(PASS_ACTION) &&
                            actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(ERROR_DURING_PASS));
                    }else if( currentRoutingDetailBean.getAction().equals(BYPASS_ACTION) &&
                            actionButtonClicked ){
                        CoeusOptionPane.showErrorDialog(
                                coeusMessageResources.parseMessageKey(ERROR_DURING_BYPASS));
                    }
                }
            }
        }
    }
    
    /** Performs other actions after saving */
//Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
//   private void performPostSaveAction(int returnValue){
    private void performPostSaveAction(int returnValue,ProposalDevelopmentFormBean proposalDevelopmentFormBean){
//        ProposalDevelopmentFormBean proposalDevelopmentFormBean = getProposalDevelopmentFormBean();
        try{
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){//For Proposal module
                if( returnValue == 2 ){
                    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
                    //Check whether the Proposal is in hierarchy and narrative status is complete
                    if(!isHierarchy() && "I".equalsIgnoreCase(proposalDevelopmentFormBean.getNarrativeStatus())){
                        ((ProposalDevelopmentFormBean)moduleBean).setCreationStatusCode(4);
                        updateProposalStatus((ProposalDevelopmentFormBean)moduleBean, APPROVED);
                        setSponsorMenuEnabled(true);
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NARRATIVE_INCOMPLETE));
                        return;
                    }else{                        
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
                        submitToSponsor(6);
                    }
                }else if( returnValue == 3 ){   //User doesn't have right to submit to sponsor
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(NO_RIGHT_TO_SUBMIT_TO_SPONSOR));
                    ((ProposalDevelopmentFormBean)moduleBean).setCreationStatusCode(4);
                    //proposalDevelopmentFormBean.setCreationStatusCode(4);
                    updateProposalStatus((ProposalDevelopmentFormBean)moduleBean, APPROVED);
                }else if( returnValue == 4 ){
                    ((ProposalDevelopmentFormBean)moduleBean).setCreationStatusCode(5);
                    updateProposalStatus((ProposalDevelopmentFormBean)moduleBean, SUBMITTED);
                }else if( returnValue == 5 ){
                    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
                    //Check whether the Proposal is in hierarchy and narrative status is complete
                    if(!isHierarchy() && "I".equalsIgnoreCase(proposalDevelopmentFormBean.getNarrativeStatus())){
                        ((ProposalDevelopmentFormBean)moduleBean).setCreationStatusCode(4);
                        updateProposalStatus((ProposalDevelopmentFormBean)moduleBean, APPROVED);
                        setSponsorMenuEnabled(true);
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NARRATIVE_INCOMPLETE));
                        return;
                    }else{
                        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
                        submitToSponsor(5);
                    }
                }
            }else if(moduleCode == ModuleConstants.AWARD_MODULE_CODE){//award module
                if(returnValue==3){
                    edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean= 
                            (edu.mit.coeus.award.bean.AwardDocumentRouteBean)moduleBean;
               UpdateAwardStatus(awardDocumentRouteBean.getMitAwardNumber(),awardDocumentRouteBean.getRoutingDocumentNumber(),AWARD_DOC_ROUT_APPROVE);
               submissionStatusCode= AWARD_DOC_ROUT_APPROVE.intValue();
            }
            }
            
            else if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){//For protocol module

                //if return value is 5, then the last approver has approved
                //if return value is 3, then the last approver has bypassed
                //Update the submission status of the Protocol to SUBMITTED TO COMMITTE,
                //if a committee is selected, else update to PENDING
                if(returnValue == 5 || returnValue == 3){
                    edu.mit.coeus.irb.bean.ProtocolInfoBean irbProtoInfoBean = (edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean;
                    setSubmissionStatusCode(IRB_SUB_STATUS_SUBMIT_TO_COMMITTEE);
                    edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean irbSubmissionInfoBean =
                            getIRBProtocolSubmissionInfo(irbProtoInfoBean.getProtocolNumber(), irbProtoInfoBean.getSequenceNumber());
                      // Modified COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//                    if(irbSubmissionInfoBean!=null && irbSubmissionInfoBean.getCommitteeId()!=null
//                             && irbSubmissionInfoBean.getCommitteeId()!=""){
//                         updateProtocolSubmissionStatus(irbProtoInfoBean.getProtocolNumber(),
//                                 irbProtoInfoBean.getSequenceNumber(),irbSubmissionInfoBean.getSubmissionNumber(),
//                                 IRB_SUB_STATUS_SUBMIT_TO_COMMITTEE);
//
//                     }else{
//                         updateProtocolSubmissionStatus(irbProtoInfoBean.getProtocolNumber(),
//                                 irbProtoInfoBean.getSequenceNumber(), irbSubmissionInfoBean.getSubmissionNumber(), IRB_SUB_STATUS_PENDING);
//
//                     }
                    if(irbSubmissionInfoBean!=null && irbSubmissionInfoBean.getCommitteeId()!=null
                             && irbSubmissionInfoBean.getCommitteeId()!=""){
                         updateProtocolStatusSubmissionStatus(irbProtoInfoBean.getProtocolNumber(),
                                 irbProtoInfoBean.getSequenceNumber(),irbSubmissionInfoBean.getSubmissionNumber(),
                                 IRB_SUB_STATUS_SUBMIT_TO_COMMITTEE, PROTOCOL_STATUS_SUBMIT_TO_IRB);

                     }else{
                         updateProtocolStatusSubmissionStatus(irbProtoInfoBean.getProtocolNumber(),
                                 irbProtoInfoBean.getSequenceNumber(), irbSubmissionInfoBean.getSubmissionNumber(),
                                 IRB_SUB_STATUS_PENDING,PROTOCOL_STATUS_SUBMIT_TO_IRB);

                     }
                     // Modified COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End                                             
                    CoeusOptionPane.showInfoDialog("Protocol " + 
                            irbProtoInfoBean.getProtocolNumber() + " Submitted for Review") ;
                }
            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){//For iacuc module
                //if return value is 5, then the last approver has approved
                //if return value is 3, then the last approver has bypassed
                //Update the submission status of the Protocol to SUBMITTED TO COMMITTE,
                //if a committee is selected, else update to PENDING
                if(returnValue == 5 || returnValue == 3){
                    edu.mit.coeus.iacuc.bean.ProtocolInfoBean iacucProtoInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean;
                    setSubmissionStatusCode(IACUC_SUB_STATUS_SUBMIT_TO_COMMITTEE);
                    edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean iacucSubmissionInfoBean =
                            getIACUCProtocolSubmissionInfo(iacucProtoInfoBean.getProtocolNumber(), iacucProtoInfoBean.getSequenceNumber());
                    // Modified COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start                    
//                    if(iacucSubmissionInfoBean!=null && iacucSubmissionInfoBean.getCommitteeId()!=null
//                             && iacucSubmissionInfoBean.getCommitteeId()!=""){
//                         updateProtocolSubmissionStatus(iacucProtoInfoBean.getProtocolNumber(),
//                                 iacucProtoInfoBean.getSequenceNumber(),iacucSubmissionInfoBean.getSubmissionNumber(),
//                                 IACUC_SUB_STATUS_SUBMIT_TO_COMMITTEE);
//                     }else{
//                         updateProtocolSubmissionStatus(iacucProtoInfoBean.getProtocolNumber(),
//                                 iacucProtoInfoBean.getSequenceNumber(), iacucSubmissionInfoBean.getSubmissionNumber(), IACUC_SUB_STATUS_PENDING);
//                     }
                    if(iacucSubmissionInfoBean!=null && iacucSubmissionInfoBean.getCommitteeId()!=null
                             && iacucSubmissionInfoBean.getCommitteeId()!=""){
                         updateProtocolStatusSubmissionStatus(iacucProtoInfoBean.getProtocolNumber(),
                                 iacucProtoInfoBean.getSequenceNumber(),iacucSubmissionInfoBean.getSubmissionNumber(),
                                 IACUC_SUB_STATUS_SUBMIT_TO_COMMITTEE,PROTOCOL_STATUS_SUBMIT_TO_IACUC);
                         
                     }else{
                         updateProtocolStatusSubmissionStatus(iacucProtoInfoBean.getProtocolNumber(),
                                 iacucProtoInfoBean.getSequenceNumber(), iacucSubmissionInfoBean.getSubmissionNumber(), 
                                 IACUC_SUB_STATUS_PENDING,PROTOCOL_STATUS_SUBMIT_TO_IACUC);
                         
                     }
                     // Modified COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End                                                                 
                    CoeusOptionPane.showInfoDialog("Protocol " + 
                            iacucProtoInfoBean.getProtocolNumber() + " Submitted for Review") ;
                }
            }
        }catch (CoeusClientException coeusClientException){
            coeusClientException.printStackTrace();
        }
    }
    
    /** Method to perform operations for submitting to sponsor */
    private void submitToSponsor(int status){
        ProposalDevelopmentFormBean proposalDevelopmentFormBean = 
                (ProposalDevelopmentFormBean)moduleBean;
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
                setModuleBean(submitToSponsor.getProposalDevelopmentFormBean());
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
                        setModuleBean(submitToSponsor.getProposalDevelopmentFormBean());
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
                setModuleBean(submitToSponsor.getProposalDevelopmentFormBean());
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
                setModuleBean(
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
        //!!!!!!!!!!THINKS NOT NEEDED!!!!!!!!UNCOMMENTED!!!!!!!!!!!!!!
//        if ( !performPreOpenOperation() ){
//            return false;
//        }
        
        if( btnApprove.getText().equals(BYPASS) ){
            dlgApprovalWindow.show();
        }else{
            dlgApprovalWindow.show();
        }
        return approvalActionPerformed;
    }


    public void DisableRoutingApprovalForm(){

        btnAddApprover.setEnabled(false);
            btnApprove.setEnabled(false);
            btnClose.setEnabled(false);
            btnDelete.setEnabled(false);
                 btnModifyComments.setEnabled(false);
                 btnNewComments.setEnabled(false);
                 tblComments.setEnabled(false);
                 btnDeleteComments.setEnabled(false);
                 btnSaveAndNewComment.setEnabled(false);
                 btnNewComments.setEnabled(false);
                 txtArComments.setEnabled(false);
                 tbdPnAttAndCommts.setEnabled(false);
            }


//public void close(){
//        {
//
//            performWindowClosing();
//        }
//
//}
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
             //Modified for COEUSQA-1680 : Ability to Add Attachments when Bypassing - Start
            dlgApprovalWindow.setSize(WIDTH, HEIGHT);
//            dlgApprovalWindow.setSize(WIDTH, INITIAL_HEIGHT);
            //COEUSQA-1680 : End
            
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
            dlgApprovalWindow.setSize(WIDTH, INITIAL_HEIGHT);
            
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
    
    /**
     * Getter for property currentRoutingDetailBean.
     *
     * @return Value of property currentRoutingDetailBean.
     */
//    public edu.mit.coeus.propdev.bean.ProposalApprovalBean getCurrentApprovalBean() {
//        return currentRoutingDetailBean;
//    }
    public RoutingDetailsBean getCurrentRoutingDetailsBean() {
        return currentRoutingDetailBean;
    }
    /**
     * Setter for property currentRoutingDetailBean.
     *
     * @param currentRoutingDetailBean New value of property currentRoutingDetailBean.
     */
    public void setCurrentRoutingDetailsBean(RoutingDetailsBean currentRoutingDetailBean) {
        this.currentRoutingDetailBean = currentRoutingDetailBean;
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
    
    /** Getter for property moduleBean.
     * @return Value of property moduleBean.
     *
     */
    public Object getModuleBean() {
        return moduleBean;
    }
    
    /** Setter for property moduleBean.
     * @param moduleBean New value of property moduleBean.
     *
     */
    public void setModuleBean(Object moduleBean) {
        this.moduleBean = moduleBean;
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
    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
    /**
     * Getter for property hierarchy.
     * @return Value of property hierarchy.
     */
    public boolean isHierarchy() {
        return hierarchy;
        //return false;
    }
    
    /**
     * Setter for property hierarchy.
     * @param hierarchy New value of property hierarchy.
     */
    public void setHierarchy(boolean hierarchy) {
        this.hierarchy = hierarchy;
    }

    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
    
    public void populateCommentsTable(){
        if(currentRoutingDetailBean!=null){
            commentsTableModel.setData(currentRoutingDetailBean.getComments());
        }else{
            commentsTableModel.setData(null);
        }
        commentsTableModel.fireTableDataChanged();
    }
    
    public void populateAttachmentsTable(){
        if(currentRoutingDetailBean!=null){
            attachmentTableModel.setData(currentRoutingDetailBean.getAttachments());
            if(currentRoutingDetailBean.getAttachments()!= null){
                cvModifiedAttachments.addAll(currentRoutingDetailBean.getAttachments());
            }else{
                cvModifiedAttachments.addAll(new CoeusVector());
            }
            if(currentRoutingDetailBean.getAttachments()!=null && currentRoutingDetailBean.getAttachments().size()>0){
                tbdPnAttAndCommts.setIconAt(1,
                        new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DETAILS_ICON)));
            }else{
                tbdPnAttAndCommts.setIconAt(1, null);
            }
        }else{
            attachmentTableModel.setData(null);
//            attachmentTableModel.fireTableDataChanged();
        }        
         attachmentTableModel.fireTableDataChanged();
    }
    
    class CommentsTableModel extends AbstractTableModel{
        String colNames[] = {"", ""};
        Class[] colTypes = new Class [] {String.class, Boolean.class};
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            if( col == MORE_COMMENTS_COLUMN){
                return true;
            } else {
                return false;
            }
        }
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            if(cvCommentsData == null ){
                return 0;
            }else{
                return cvCommentsData.size();
            }
        }
        
        private void setData(CoeusVector commentsData){
            cvCommentsData = commentsData;
        }
        /* gets the value at a particular cell*/
        public Object getValueAt(int row, int column) {
            RoutingCommentsBean routingCommentsBean =
                    (RoutingCommentsBean)cvCommentsData.get(row);
            if(column==0){
                return routingCommentsBean.getComments();
            }else{
                return "";
            }
        }
    }
    class AttachmentsTableModel extends AbstractTableModel{
       //#Case 3855 -- start  Added new column which is not visible to user to store file name.
        String colNames[] = {"", "Description",""};
        Class[] colTypes = new Class [] {Boolean.class, String.class,RoutingAttachmentBean.class};
       //#Case 3855 -- end
       /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(col==0){
                return true;
            }else{
                return false;
            }
        }
        
        /**
         * Returns the number of columns
         */
        public int getColumnCount() {
            return colNames.length;
        }
        
        /**
         * Returns the column class
         */
        public Class getColumnClass(int columnIndex) {
            return colTypes [columnIndex];
        }
        
        /* returns the column names*/
        public String getColumnName(int column) {
            return colNames[column];
        }
        
        /* returns the number of rows*/
        public int getRowCount() {
            if(cvModifiedAttachments == null ){
                return 0;
            }else{
                return cvModifiedAttachments.size();
            }
        }
        
        /* gets the value at a particular cell*/
        public Object getValueAt(int row, int column) {
            RoutingAttachmentBean routingAttachmentBean =
                    (RoutingAttachmentBean)cvModifiedAttachments.get(row);
            if(column==1){
                return routingAttachmentBean.getDescription();
            }
            // # case 3855 start added file name for the second column which is not visible to the user
            else if(column == 2){
                 return routingAttachmentBean;//4007
            }
            // # case 3855 end
            else{
                return "";
            }
        }
        /* Sets the value in the cell*/
        public void setValueAt(Object value, int row, int col){
            
        }
        public void setData(CoeusVector cvModifiedAttachments){
            cvModifiedAttachments = cvModifiedAttachments;
        }
    }
    
    public byte[] getBlobData() {
        return blobData;
    }
    
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }
    
    public boolean isFileSelected() {
        return fileSelected;
    }
    
    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }

	class CommentsRenderer extends DefaultTableCellRenderer{
		private JLabel lblText;
		private JButton btnMoreComments;
                private ImageIcon imgIcnDesc;
		/**
		 * Default Constructor
		 */		
		public CommentsRenderer(){
			lblText = new JLabel();
			lblText.setBorder(new EmptyBorder(0,0,0,0));
			lblText.setOpaque(true);
                        lblText.setFont(CoeusFontFactory.getNormalFont());
                        imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                        btnMoreComments  = new JButton();
                        btnMoreComments.setIcon(imgIcnDesc);
		}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
			Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
                        switch(col){
                            case COMMENTS_COLUMN:
                                return returnComponent;
                                
                            case MORE_COMMENTS_COLUMN:
                                return btnMoreComments; 
                        }
			return returnComponent;
			
		}
		
	}
        
        class CommentsEditor extends DefaultCellEditor implements ActionListener{
            
            private JButton btnComments;
            private String comments;
            private ImageIcon imgIcnDesc;
          
            CommentsEditor() {
                super(new JComboBox());
                imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                btnComments = new JButton();
                btnComments.setIcon(imgIcnDesc);
                btnComments.setOpaque(true);
                btnComments.addActionListener(this);               
            }
            public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
                comments = ((RoutingCommentsBean)cvCommentsData.get(row)).getComments();
                return btnComments;
            }

            public void actionPerformed(ActionEvent actionEvent) {
                this.stopCellEditing();
                if( btnComments.equals(actionEvent.getSource())){
                    edu.mit.coeus.utils.CommentsForm commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
                    commentsForm.setData(comments);
                    commentsForm.display();
                }
            }
        }
        
        /****************************/
         class AttachmentsEditor extends DefaultCellEditor implements ActionListener{
             private JButton btnDocument;
             int row;
             private ImageIcon  pdfIcon;
             AttachmentsEditor() {
                 super(new JComboBox());
                 //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                 pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
                 pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                 //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
               //#Case 3855 modified to set attachment specific icon.
                 btnDocument = new JButton();
                 btnDocument.addActionListener(this);
             }
             public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
                 this.row = row;
                 RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)cvModifiedAttachments.elementAt(row);
                 switch(column){
                     case 0:
                         //Modified with case 4007:Icon based on mime Type : Start
                         // #Case - 3855 start added attachment specific icon for the column
//                         String fileExtension = UserUtils.getFileExtension((String)jTable.getModel().getValueAt(row,2));
//                         btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                         // #Case - 3855 end
                         CoeusAttachmentBean attachment = (CoeusAttachmentBean)jTable.getModel().getValueAt(row,2);
                         CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                         btnDocument.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                         //4007 End
                         return btnDocument;
                 }
                 return btnDocument;
             }
             public void actionPerformed(ActionEvent actionEvent) {
                 if( btnDocument.equals(actionEvent.getSource())){
                     this.stopCellEditing();
                     try {
                         RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)cvModifiedAttachments.elementAt(row);
                         performViewDocument(routingAttachmentBean);
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                 }
             }
         }
        

    /**
     * Method for viewing the selected file on the browser
     * @param 
     * @return
     */
    private void performViewDocument(RoutingAttachmentBean routingAttachmentBean) throws Exception{        
//        CoeusVector cvDataObject = new CoeusVector();
//        HashMap hmDocumentDetails = new HashMap();
        RequesterBean requesterBean = new RequesterBean();
        DocumentBean documentBean = new DocumentBean();
        Map map = new HashMap();
        map.put("FUNCTION_TYPE","SHOW_ROUTING_ATTACHMENT");
        map.put("DATA", routingAttachmentBean);
        map.put("ATTACHMENT_SAVED", "N");
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);        
        AppletServletCommunicator appletServletCommunicator = new
                AppletServletCommunicator(STREAMING_SERVLET, requesterBean);
        appletServletCommunicator.send();
        ResponderBean responder = appletServletCommunicator.getResponse();        
        if(!responder.isSuccessfulResponse()){
            throw new CoeusException(responder.getMessage(),0);
        }
        map = (Map)responder.getDataObject();
        String url = (String)map.get(DocumentConstants.DOCUMENT_URL);
        if(url == null || url.trim().length() == 0 ) {
            CoeusOptionPane.showErrorDialog(
                    coeusMessageResources.parseMessageKey("protocolUpload_exceptionCode.1009"));
            return;
        }
        url = url.replace('\\', '/') ;
        try{
            URL urlObj = new URL(url);
            URLOpener.openUrl(urlObj);
        }catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }catch( Exception ue) {
            ue.printStackTrace() ;
        }
    }
    
        class AttachmentsRenderer extends DefaultTableCellRenderer{
            private JLabel lblText;
            private JButton btnDocument;
            private ImageIcon imgIcnDesc;
            /**
             * Default Constructor
             */
            public AttachmentsRenderer(){
                lblText = new JLabel();
                lblText.setBorder(new EmptyBorder(0,0,0,0));
                lblText.setOpaque(true);
                lblText.setFont(CoeusFontFactory.getNormalFont());
                //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
                imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
                btnDocument  = new JButton();
                btnDocument.setIcon(imgIcnDesc);
            }
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
                switch(col){
                    case 1:
                        return returnComponent;
                        
                    case 0:
                        //Modified with case 4007:Icon based on mime Type : Start
                        //#Case 3855 -- start Modified to set attachment specific icon
//                        String fileExtension = UserUtils.getFileExtension((String)table.getModel().getValueAt(row,2));
//                        btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                        //#Case 3855 -- end
                        CoeusAttachmentBean attachment = (CoeusAttachmentBean)table.getModel().getValueAt(row,2);;
                        CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                        btnDocument.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                        //4007 End
                        return btnDocument;
                }
                return returnComponent;
            }
            
        }
    /**
     * Update the Protocol and Submission status of the protocol
     * 
     * @param protocolnumber protocol number
     * @param sequenceNumber sequence number
     * @param submissionstatusCode submission status code
     * @param protocolStatusCode - int
     */   
    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//    public void updateProtocolSubmissionStatus(String protocolNumber, 
//            int sequenceNumber, int submissionNumber, int status)throws CoeusClientException{
//        RequesterBean requester = new RequesterBean();
//        requester.setFunctionType(UPD_PROT_SUBMISSION_STATUS); 
    public void updateProtocolStatusSubmissionStatus(String protocolNumber, 
            int sequenceNumber, int submissionNumber, int submissionstatusCode, int protocolStatusCode)throws CoeusClientException{
        RequesterBean requester = new RequesterBean();
        
        requester.setFunctionType(UPD_PROT_STATUS_AND_SUBMISSION_STATUS); 
        // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
        Vector serverDataObjects = new Vector();
        serverDataObjects.add(protocolNumber);
        serverDataObjects.add(new Integer(sequenceNumber));
        serverDataObjects.add(new Integer(submissionNumber));
        serverDataObjects.add(new Integer(submissionstatusCode));
        // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start         
        serverDataObjects.add(new Integer(protocolStatusCode));
        // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
        requester.setDataObjects(serverDataObjects);
        StringBuffer connectTo = new StringBuffer(CoeusGuiConstants.CONNECTION_URL);
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
            connectTo.append(IRB_PROTOCOL_SUBMISSION_SERVLET);
        }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            connectTo.append(IACUC_PROTOCOL_SUBMISSION_SERVLET);
        }
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo.toString(), requester); 
        comm.send();
        
        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
               
            }else{
                throw new CoeusClientException(response.getMessage());
            }
        }
    }
    
    /**
     * Get the submission information for the protocol from the database and filters
     * the result with current protocol id and sequence id 
     * 
     * @param protocolId protocol number
     * @param sequenceId sequence number
     * @return ProtocolSubmissionInfoBean
     */
    public edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean getIRBProtocolSubmissionInfo(String protocolId, int sequenceId){
        
        boolean protSubmissionDetailsFound = false;
        edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean maxSubmissionInfoBean = null;
        edu.mit.coeus.irb.bean.SubmissionDetailsBean detailsBean = new edu.mit.coeus.irb.bean.SubmissionDetailsBean() ;
        detailsBean.setProtocolNumber(protocolId) ;
        detailsBean.setScheduleId(null) ;
        detailsBean.setSequenceNumber(new Integer(sequenceId)) ; 
        Vector vecDetails = getSubmissionDetails(detailsBean);
        
        if(vecDetails!=null && vecDetails.size()>0){
            Vector vecSubmissionDetails = (Vector)vecDetails.get(0);
            if(vecSubmissionDetails!=null){
                edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                int maxSubmissionNo = 0;
                
                for(int i=0; i<vecSubmissionDetails.size(); i++){
                    protocolSubmissionInfoBean = (edu.mit.coeus.irb.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                    if(protocolSubmissionInfoBean.getProtocolNumber().equals(protocolId) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == sequenceId){
                        if(protocolSubmissionInfoBean.getSubmissionNumber()>maxSubmissionNo){
                            maxSubmissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
                            maxSubmissionInfoBean = protocolSubmissionInfoBean;
                        }
                         protSubmissionDetailsFound = true;
                    }
                }
            }
        }
        if(protSubmissionDetailsFound){
            return maxSubmissionInfoBean;
        }else{
            return null;
        } 
    }
    
    public edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean getIACUCProtocolSubmissionInfo(String protocolId, int sequenceId){
        
        boolean protSubmissionDetailsFound = false;
        edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean maxSubmissionInfoBean = null;
        edu.mit.coeus.iacuc.bean.SubmissionDetailsBean detailsBean = new edu.mit.coeus.iacuc.bean.SubmissionDetailsBean() ;
        detailsBean.setProtocolNumber(protocolId) ;
        detailsBean.setScheduleId(null) ;
        detailsBean.setSequenceNumber(new Integer(sequenceId)) ; 
        Vector vecDetails = getSubmissionDetails(detailsBean);
        
        if(vecDetails!=null && vecDetails.size()>0){
            Vector vecSubmissionDetails = (Vector)vecDetails.get(0);
            if(vecSubmissionDetails!=null){
                edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                int maxSubmissionNo = 0;
                
                for(int i=0; i<vecSubmissionDetails.size(); i++){
                    protocolSubmissionInfoBean = (edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
                    if(protocolSubmissionInfoBean.getProtocolNumber().equals(protocolId) &&
                        protocolSubmissionInfoBean.getSequenceNumber() == sequenceId){
                        if(protocolSubmissionInfoBean.getSubmissionNumber()>maxSubmissionNo){
                            maxSubmissionNo = protocolSubmissionInfoBean.getSubmissionNumber();
                            maxSubmissionInfoBean = protocolSubmissionInfoBean;
                        }
                         protSubmissionDetailsFound = true;
                    }
                }
            }
        }
        if(protSubmissionDetailsFound){
            return maxSubmissionInfoBean;
        }else{
            return null;
        } 
    }
    
    
    private Vector getSubmissionDetails(edu.mit.coeus.irb.bean.SubmissionDetailsBean detailsBean) {
        Vector vecDetails= new Vector() ;
        try {    // send request
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('G') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL + IRB_SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                vecDetails = (Vector)responder.getDataObject();
            }
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
        return vecDetails;
    }
    
    private Vector getSubmissionDetails(edu.mit.coeus.iacuc.bean.SubmissionDetailsBean detailsBean) {
        Vector vecDetails= new Vector() ;
        try {    // send request
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('G') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL + IACUC_SUBMISSION_DETAILS_SERVLET ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                vecDetails = (Vector)responder.getDataObject();
            }
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
        return vecDetails;
    }
    
    private boolean isProtocol(int moduleCode){
        boolean isProtocol = false;
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            isProtocol = true;
        }
        return isProtocol;
    }
    
    public RoutingBean getRoutingBean() {
        return routingBean;
    }

    public void setRoutingBean(RoutingBean routingBean) {
        this.routingBean = routingBean;
    }

    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }

    public void setSubmissionStatusCode(int submissionStatusCode) {
        this.submissionStatusCode = submissionStatusCode;
    }
    //Added with case 4007: Icon based on mime type : Start
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    //4007 : End

   private void releaseUpdateLock(){
        try{
            //connect to server and release the lock
              String moduleItemKey=routingBean.getModuleItemKey();
               Integer moduleCode=routingBean.getModuleCode();
               Vector dataObjects = new Vector();

              //  String proposalNumber =((ProposalDevelopmentFormBean)moduleBean).getProposalNumber();
            String rowId = null;
            //Case #1769  - start
//            char funType = MODIFY_MODE;
//            if(functionType==DISPLAY_MODE){
//                if(checkLock){
//                    funType = MODIFY_MODE;
//                    checkLock = false;
//                }else{
//                    funType = DISPLAY_MODE;
//                }
//            }// Case #1769 - End
            //if ( functionType == MODIFY_MODE ) {
           // if ( funType == MODIFY_MODE ) {
                rowId = moduleItemKey;
                RequesterBean requester = new RequesterBean();
               // dataObjects.add(moduleCode);
                requester.setDataObject(moduleCode);
                requester.setId(rowId);
                requester.setFunctionType( UNLOCK_ROUTING );
                String connectTo= CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
                AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
                comm.send();
                ResponderBean res = comm.getResponse();
                if (res != null && !res.isSuccessfulResponse()){
                    CoeusOptionPane.showErrorDialog(res.getMessage());
                    return;
                }
            //}
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }


//COEUSQA 2111 STARTS
   private boolean UpdateAwardStatus(String mitAwardNumber,Integer docNumber,Integer stausCode){
       Vector dataToServer=new Vector();
       dataToServer.add(mitAwardNumber);
       dataToServer.add(docNumber);
       dataToServer.add(stausCode);
           try {    // send request
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(UPDATE_AWARD_STATUS) ;
            requester.setDataObject(dataToServer) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL + AWARD_SUBMISSION_SERVLET ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if ((responder==null)||(!responder.isSuccessfulResponse()) ){
               return false; 
            }
        }
        catch(Exception ex) {
            ex.printStackTrace() ;
        }
       
   return true;
   }
   
 
//COEUSQA 2111 ENDS



}
