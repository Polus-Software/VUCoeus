/**
 * @(#)RoutingForm.java 1.0 10/19/07
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by keerthyjayaraj
 */
package edu.mit.coeus.routing.gui;

import edu.mit.coeus.award.bean.AwardDocumentRouteBean; 
import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.bean.UserInfoBean;
// JM needed for custom features
import edu.mit.coeus.exception.CoeusClientException;
// JM END
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.irb.bean.ProtocolActionsBean;
import edu.mit.coeus.irb.controller.ProtocolMailController;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingMapBean;
import edu.mit.coeus.search.gui.CoeusSearch;
import edu.mit.coeus.user.bean.UserRolesController;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import edu.mit.coeus.utils.locking.LockingBean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.event.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.utils.query.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.propdev.bean.*;
import edu.vanderbilt.coeus.gui.CoeusToolTip;

/**
 * This screen allows the user to Approve, Reject or Bypass the proposal
 * RoutingForm.java
 *
 * @author Vyjayanthi
 * Created on January 5, 2004, 10:36 AM
 */
public class RoutingForm extends javax.swing.JComponent
        implements ActionListener, ListSelectionListener {

    public CoeusDlgWindow dlgRouting;

    /** Holds an instance of ListSelectionModel */
    private ListSelectionModel routingSelectionModel;

    /**
     * Holds an instance of RoutingApprovalForm
     */
    private edu.mit.coeus.routing.gui.RoutingApprovalForm routingApprovalForm;
    private edu.mit.coeus.routing.gui.RoutingRejectionForm routingRejectionForm;

    private edu.mit.coeus.routing.gui.RoutingForm  routingForm;
    /** Holds an instance of ProposalRejectionForm */
//    private ProposalRejectionForm proposalRejectionForm;

    /** Holds the ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;

    /** Holds an instance of the CoeusAppletMDIForm */
    private CoeusAppletMDIForm mdiForm = CoeusGuiConstants.getMDIForm();

    //    /** Holds the map id, level number and stop number */
//    private ProposalApprovalBean currentApprovalBean;
    private RoutingDetailsBean currentRoutingDetailsBean;
    /** Holds an instance of ProposalApprovalMapBean */
    private RoutingMapBean routingMapBean;

    //private Vector cvRoutingMapBeans;
    private CoeusVector cvRoutingMapBeans;

    /** Flag to check if submit to sponsor action is performed successfully
     * to enable/disable the Submit To Sponsor menu item in Proposal Detail Form
     */
    private boolean sponsorMenuEnabled = true;

    private BaseWindowObservable observable = new BaseWindowObservable();

    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();

    private RoutingMapBean requiredRoutingMapBean;

    private Vector vecParameters;

    private static final String USER_ID_FIELD = "userId";
    private static final String APPROVAL_STATUS_FIELD = "approvalStatus";
    //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//    private static final String PARENT_MAP_ID_FIELD = "parentMapId";
    private static final String PARENT_MAP_NUMBER_FIELD = "parentMapNumber";
    private static final String WAITING_FOR_APPROVAL = "W";
    //Commented during Code-review
    private static final char GET_ROUTING_DATA = 'P';
    private static final char GET_AWD_DOC_DETAILS='H';

    private static final String NO_CURRENT_STOP = "proposal_Action_exceptionCode.8011" ; //"There is no current stop"
    private static final String STOP_NOT_WAITING_FOR_APPROVAL = "proposal_Action_exceptionCode.8012" ; //"This stop is not Waiting for Approval"
    private static final String NO_ROUTING_MAP = "showRouting_exceptionCode.1138"; //"No routing maps have been defined for this proposal";
    private static final String SUNMIT_FAILED_NO_MAPS = "submitforApproval_exceptionCode.1143"; //"Submission of proposal failed because no routing stops have been defined.";
    private static final String AWARD_DOC_NO_MAPS = "award_routing_showRouting_exceptionCode.1001";
//    private static final String BYPASS_COMMENT = "Your approval has been bypassed by OSP because of an approaching deadline";
//    private Frame parent;
//    private boolean modal;

//    private static final int WIDTH = 540;
//    private static final int HEIGHT = 450;
    private static final int WIDTH = 770; // JM 7-21-2015 increased to accommodate new button labels
    private static final int HEIGHT = 700;

    public static final char MODIFY_MODE = 'M';
    //# Added by Ranjeev
    private static final String TO_BE_SUBMITTED = "T";//QUERY_CONSTANTS
//    private static final String PASSED = "P";
    private static final String APPROVED = "A";
    private static final String REJECTED = "R";
    //Commented during Code-review
//    private static final String REJECTED_BY_OTHERS = "J";
//    private static final String BYPASSED = "B";

    private boolean modifiable = true;
    private static final String BUILD_MAPS_OPTION_PARAM = "D";
    private char functionType=LOCKINROUTING;

    private static final char APPROVER_COUNT = 'X';


    //COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status-Start
    private static final int PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS = 7;
    //COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status-End
    boolean isRoutingDataAvailable;

    private DefaultMutableTreeNode treeRoot;
    Color colorPanelBackground ;
    boolean isByPassFlag ;
    private boolean parentProposal;
    //#END Added by Ranjeev


    /** Holds the user data displayed in the table panel */
    private CoeusVector cvExistingApprovers;

    /** Holds all the approvers data */
    private CoeusVector cvEntireApprovers;

    /** Holds the user data displayed in the tree panel */
    private CoeusVector cvTreeView;

    //tempVector to hold the Map Beans
    private CoeusVector tempVecProposalApprovalMapBeans = new CoeusVector();
    //    private static final String SEQUENTIAL_STOP = "Sequential Stop ";

    /** Holds the connection string */
    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
            "/RoutingServlet";

     //  private static final String CONNECTION_STRINGG = CoeusGuiConstants.CONNECTION_URL +
       //     "/RoutingForm";
    //    private static final String EMPTY_STRING = "";

    private int moduleCode;
    private String moduleItemKey;
    private Object moduleBean;
    private boolean buildMapRequired;
    private CoeusVector cvAttachments;
    private CoeusVector cvComments;
    private CommentsTableModel commentsTableModel;
    private AttachmentsTableModel attachmentTableModel;
    private RoutingBean routingBean;
    private ImageIcon iIcnDetails, iIcnPdf;
    private AttachmentCellEditor attachmentCellEditor;
    private AttachmentCellRenderer attachmentCellRenderer;

    private String loggedInUser;
    private HashMap hmMapNumbers;
    private CoeusVector cvApproversData;
    private CoeusVector cvCommentsData;
    private CoeusVector cvModifiedAttachments;
    private static final int COMMENTS_COLUMN = 0;
    private static final int MORE_COMMENTS_COLUMN = 1;
    private static final int VIEW_PDF = 0;
    private static final int PDF_DESCRIPTION = 1;
    private boolean isPreviousData;
    private int originalSubmissionNum;
    private int currentSubmissionNum;

    private boolean mapsNotFound;
    private String unitNumber;
    private int submissionStatusCode;
    private final int IRB_REJECTED_SUBMISSION = 214;
    private final int IACUC_REJECTED_SUBMISSION = 211;
    private boolean showRouting = false;
    private static final char DELETE_ROUTING = 'A';
    //Added for Case#3775 - Manually selected map disappears
    private boolean closeRequired;
    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start

    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
    private boolean hierarchy;
    //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
    private static final int MODULE_CODE_INDEX = 0;
    private static final int MODULE_ITEM_KEY_INDEX  = 1;

    private static final char GET_ROUTING_SEQUENCE_HISTORY = 'H';
    private static final int MODULE_ITEM_NUMBER_INDEX = 0;
    private static final int MODULE_ITEM_KEY_SEQUENCE_INDEX = 1;
    private static final int APPROVAL_SEQUENCE_INDEX = 2;
    private Hashtable hmRoutingSeqHistory = null;
    private int submissionNumber = 0;
    private boolean isModuleSubmission = false;
     private  boolean b;
     private static final char LOCKINROUTING = 'r';
     private static final char UNLOCK_ROUTING = 'l';
    //COEUSQA-2249 : End

    //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
    private static final String COMMENTS_AND_ATTACHMENTS = "Comments and Attachments";
    private CoeusVector cvRoutingCommentsAttachments;
    private CoeusVector cvRoutingComments;
    private static final char GET_ROUTING_COMM_FOR_ALL_APP = 'M';
    private int commentsAttachmentsPanelHeight = 22;
    private String EMPTYSTRING = "";
    //COEUSQA:1445 - End

    //COEUSQA-1433 - Allow Recall from Routing - Start
    private edu.mit.coeus.routing.gui.RoutingRecallForm routingRecallForm;
    private final int IRB_RECAll_SUBMISSION = 215;
    private final int IACUC_RECAll_SUBMISSION = 215;
    private static final char CHECK_ROUTING_RECALL_RIGHTS = 'I';
    private final String PROTOCOL_SERVLET = "/protocolMntServlet";
    private final String IACUC_PROTOCOL_SERVLET = "/IacucProtocolServlet";  
    private final String PROPOSAL_SERVLET = "/ProposalMaintenanceServlet";
    private final String AWARD_SERVLET = "/AwardSubmissionServlet"; 
    private static final char PROTOCOL_ACTION_UPDATE = '6';
    private static final char IACUC_PROTOCOL_ACTION_UPDATE = '7';
    public static final int SUBMISSION_RECALLED_NOTIFICATION_ACTION = 512;
    private static final char ROUTING_UNLOCK = 'J';
    private final int SUBMISSION_RECALLED_ACTION = 123;
    private final String RECALL_PROPOSAL_START = "showRouting_exceptionCode.1137";
    private final String RECALL_PROPOSAL_END = "showRouting_exceptionCode.1140";
    private final String RECALL_IRB_PROTOCOL_START = "showRouting_exceptionCode.1143";
    private final String RECALL_IRB_PROTOCOL_END = "showRouting_exceptionCode.1141";
    private final String RECALL_IACUC_PROTOCOL_START = "showRouting_exceptionCode.1139";
    private final String RECALL_IACUC_PROTOCOL_END = "showRouting_exceptionCode.1142";
    private final String TRUE = "true";
    private final String FALSE = "false";
    //COEUSQA-1433 - Allow Recall from Routing - End
    //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
    private final String RECALLED_STATUS = "C";
    //COEUSQA:3441 - End
    //COEUSQA:1699 - Add Approver Role - Start
    private static final String MAP_NUMBER_FIELD = "mapNumber";
    private static final String LEVEL_NUMBER_FIELD = "levelNumber";
    private static final String STOP_NUMBER_FIELD = "stopNumber";
    private boolean alternateApprover = false;
    private int nextStopNumber = 0;
    private static final String EMPTY_STRING = "";
    private static final String DESCRIPTION = "Approver added by ";
    private RoutingDetailsBean selectedRoutingDetailBean;
    private static final char ROUTING_ADD_APPROVER = 'B';
    private static CoeusVector cvApprover;
    private static final String SELECT_PRIMARY_APPROVER = "proposal_Action_exceptionCode.8000";
    private static final String APPROVERS_MUST_BE_UNIQUE = "proposal_Action_exceptionCode.8003";
    private static final String USER_EXISTS_AS_APPROVER =  " is already present as an approver at this stop";
    private static final String SELECT_APPROVER_FROM_WAITING_FOR_APPROVAL = "routing_AddAlternate_exceptionCode.8020";
    //COEUSQA:1699 - End
    
    // JM 4-1-2014 rolling our own routing - Happy Birthday Isabella and Marie!
    private static final char VU_ROUTING_ADD_APPROVER = 'D';
    private static final String ROUTING_QUEUE_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/RoutingQueueServlet";
    // JM END
    
    // JM 7-22-2015 button size 
    private static final int BTNWIDTH = 170; // originally 111
    private static final int BTNHEIGHT = 23;
    // JM END
    
    /**
     * Creates new form RoutingForm
     *
     * @param parent holds the parent frame
     * @param propDevFormBean holds the proposal development data
     * @param modal holds true if modal, false otherwise
     */
    public RoutingForm(Object moduleBean, int moduleCode, String moduleItemKey,
            int moduleItemKeySeq, String unitNumber, boolean buildMapRequired) {
        //this.proposalDevelopmentFormBean = propDevFormBean;
        this.moduleBean = moduleBean;
        this.moduleCode = moduleCode;
        this.routingBean = new RoutingBean();
        this.routingBean.setModuleCode(moduleCode);
        this.routingBean.setModuleItemKey(moduleItemKey);
        this.routingBean.setModuleItemKeySequence(moduleItemKeySeq);
        this.unitNumber = unitNumber;
        this.buildMapRequired = buildMapRequired;
        initComponents();
        //((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new ProposalApprovalMapBean()));
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new RoutingMapBean()));
        treeSequentialStop.setRootVisible(false);
        postInitComponents();
        registerComponents();
        //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
        if(isProtocol(moduleCode)){
            hmRoutingSeqHistory = getRoutingSequenceHistory(moduleCode+CoeusGuiConstants.EMPTY_STRING,moduleItemKey);
            setMaxSubSeqAndApprovalSeq();
            currentSubmissionNum = submissionNumber;
        }else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
            currentSubmissionNum=moduleItemKeySeq;
        }
        //COEUSQA-2249 : End
        getFormData();
        if(isMapsNotFound()){
            return;
        }
        setFormData();
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        getRecalledLabeData(routingBean.getApprovalSequence());
        //COEUSQA:3441 - End
    }

    public RoutingForm(Object moduleBean, int moduleCode, String moduleItemKey,
            int moduleItemKeySeq, String unitNumber, boolean buildMapRequired, int approvalSequence) {
        //this.proposalDevelopmentFormBean = propDevFormBean;

        this.moduleBean = moduleBean;
        this.moduleCode = moduleCode;
        this.routingBean = new RoutingBean();
        this.routingBean.setModuleCode(moduleCode);
        this.routingBean.setModuleItemKey(moduleItemKey);
        this.routingBean.setModuleItemKeySequence(moduleItemKeySeq);
        this.unitNumber = unitNumber;
        this.buildMapRequired = buildMapRequired;
        this.originalSubmissionNum = approvalSequence;
        this.currentSubmissionNum = approvalSequence-1;
        isPreviousData = true;
        initComponents();
        //((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new ProposalApprovalMapBean()));
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new RoutingMapBean()));
        treeSequentialStop.setRootVisible(false);
        postInitComponents();
        registerComponents();
        getFormData();
        if(isMapsNotFound()){
            return;
        }
        setFormData();
        enableDisableButtons();
        dlgRouting.setTitle("History Details");
    }

    //Added for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved - start
    /*
     * Constructor can be used during submission
     * @param moduleBean
     * @param moduleCode
     * @param moduleItemKey
     * @param moduleItemKeySeq
     * @param unitNumber
     * @param buildMapRequired
     * @param submissionNumber
     * @param isModuleSubmission
     *
     */
      public RoutingForm(Object moduleBean, int moduleCode, String moduleItemKey,
            int moduleItemKeySeq, String unitNumber, boolean buildMapRequired, boolean isModuleSubmission) {
        this.isModuleSubmission = isModuleSubmission;
        this.moduleBean = moduleBean;
        this.moduleCode = moduleCode;
        this.routingBean = new RoutingBean();
        this.routingBean.setModuleCode(moduleCode);
        this.routingBean.setModuleItemKey(moduleItemKey);
        this.routingBean.setModuleItemKeySequence(moduleItemKeySeq);
        this.unitNumber = unitNumber;
        this.buildMapRequired = buildMapRequired;
        initComponents();
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new RoutingMapBean()));
        treeSequentialStop.setRootVisible(false);
        postInitComponents();
        registerComponents();
        if((isProtocol(moduleCode))||(moduleCode==ModuleConstants.AWARD_MODULE_CODE)){  
            hmRoutingSeqHistory = getRoutingSequenceHistory(moduleCode+CoeusGuiConstants.EMPTY_STRING,moduleItemKey);
            setMaxSubSeqAndApprovalSeq();
            currentSubmissionNum = submissionNumber;
        }
        getFormData();
        if(isMapsNotFound()){
            return;
        }
        setFormData();
    }
    /*
     * Constructor for previous submission
     * @param moduleBean
     * @param moduleCode
     * @param moduleItemKey
     * @param moduleItemKeySeq
     * @param unitNumber
     * @param submissionNumber
     * @param hmRoutingSeqHistory
     */
     public RoutingForm(Object moduleBean, int moduleCode, String moduleItemKey,
            int moduleItemKeySeq, String unitNumber ,int submissionNumber, Hashtable hmRoutingSeqHistory) {
        this.moduleBean = moduleBean;
        this.moduleCode = moduleCode;
        this.hmRoutingSeqHistory = hmRoutingSeqHistory;
        this.routingBean = new RoutingBean();
        this.routingBean.setModuleCode(moduleCode);
        this.routingBean.setModuleItemKey(moduleItemKey);
        this.routingBean.setModuleItemKeySequence(moduleItemKeySeq);
        this.unitNumber = unitNumber;
        this.buildMapRequired = buildMapRequired;
        this.originalSubmissionNum = submissionNumber-1;
        this.currentSubmissionNum = originalSubmissionNum;
        this.submissionNumber = currentSubmissionNum;
        this.isModuleSubmission = false;
        isPreviousData = true;
        initComponents();
        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(new RoutingMapBeanNode(new RoutingMapBean()));
        treeSequentialStop.setRootVisible(false);
        postInitComponents();
        registerComponents();
        getFormData();
        if(isMapsNotFound()){
            return;
        }
        setFormData();
        enableDisableButtons();
        dlgRouting.setTitle("History Details");
    }
    //COEUSQA-2249 : End


    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){
        //COEUSQA-1433 - Allow Recall from Routing - Start
        ImageIcon imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
        btnViewComments.setIcon(imgIcnDesc);
        //COEUSQA-1433 - Allow Recall from Routing - End
        dlgRouting = new CoeusDlgWindow( mdiForm, true);
        dlgRouting.getContentPane().add(this);
        dlgRouting.setResizable(false); 
        String title = "Proposal Routing";
        if(isProtocol(moduleCode)){
            title = "Protocol Routing";
        }
        //AWARD ROUTING ENHANACEMENT
      else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE)
        {
            edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean= 
                    (edu.mit.coeus.award.bean.AwardDocumentRouteBean)moduleBean;
            title="Award Document Routing - ";
            title+=awardDocumentRouteBean.getDocumentTypeDesc()+" : ";
            title+=awardDocumentRouteBean.getRoutingDocumentNumber();
        }
       // AWARD ROUTING ENHANACEMENT
        dlgRouting.setTitle(title);
        dlgRouting.setFont(CoeusFontFactory.getLabelFont());
        dlgRouting.setSize(WIDTH,HEIGHT);
        dlgRouting.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRouting.getSize();
        dlgRouting.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
        dlgRouting.setVisible(false);
    }

    /** This method is used to set the listeners to the components. */
    private void registerComponents(){

        /** Code for focus traversal - start */
        //COEUSQA-1433 - Allow Recall from Routing - Start
        //java.awt.Component[] components = { treeSequentialStop,tblProposalRouting, btnBypass,btnClose, btnReject,btnApprove };
        // JM 2-18-2013 added reopen for approval button
    	java.awt.Component[] components = { treeSequentialStop,tblProposalRouting, btnBypass,btnClose, btnReject,btnApprove, btnRecall, btnAddApprover, btnAlternative, btnReopenAddApprover };
        //COEUSQA-1433 - Allow Recall from Routing - End
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);

        /** Code for focus traversal - end */
        iIcnDetails = new ImageIcon(
                getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//        iIcnPdf = new ImageIcon(
//                getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
        iIcnPdf = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
        //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
        routingSelectionModel = tblProposalRouting.getSelectionModel();
        routingSelectionModel.addListSelectionListener( this );
        routingSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
        tblProposalRouting.setSelectionModel( routingSelectionModel );

//        tblProposalRouting.addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e){
//                if(e.getClickCount()==2){
//                    int selectedRow = tblProposalRouting.getSelectedRow();
//                    if(cvExistingApprovers != null && cvExistingApprovers.size() > 0){
//                        RoutingDetailsBean routingDetailsBean= (RoutingDetailsBean)cvExistingApprovers.get(selectedRow);
//                    }
//                }
//            }
//        });

        setTableEditors();
        btnApprove.addActionListener(this);
        btnBypass.addActionListener(this);
        btnClose.addActionListener(this);
        btnReject.addActionListener(this);
        //COEUSQA-1433 - Allow Recall from Routing - Start
        btnRecall.addActionListener(this);
        btnViewComments.addActionListener(this);
        //COEUSQA-1433 - Allow Recall from Routing - End
        btnShowPreviousSubmission.addActionListener(this);
        
        btnAddApprover.addActionListener(this);
        btnAlternative.addActionListener(this);

        // JM 2-18-2013 add reopen for approval button
        btnReopenAddApprover.addActionListener(this);
        // JM END
        
        CommentsRenderer commentsRenderer = new CommentsRenderer();
        CommentsEditor commentsEditor = new CommentsEditor();
        attachmentCellEditor = new AttachmentCellEditor();
        attachmentCellRenderer = new AttachmentCellRenderer();

        tblComments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = tblComments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,0));
        tableHeader.setResizingAllowed(false);
        TableColumn column = tblComments.getColumnModel().getColumn(COMMENTS_COLUMN);
        //Modified for Case#4602 -Proposal routing window too small  - Start
//        column.setMinWidth(405);
//        column.setPreferredWidth(405);
        //Case#4602 - End
        column.setMinWidth(480);
        column.setPreferredWidth(480);
        column.setCellRenderer(new DefaultTableCellRenderer());

        column = tblComments.getColumnModel().getColumn(MORE_COMMENTS_COLUMN);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setCellRenderer(commentsRenderer);
        column.setCellEditor(commentsEditor);

        tblComments.setTableHeader(null);

        tableHeader = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));
        tableHeader.setResizingAllowed(false);
        tblAttachments.setRowHeight(22);
        tblAttachments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblAttachments.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        column = tblAttachments.getColumnModel().getColumn(VIEW_PDF);
        column.setMinWidth(20);
        column.setPreferredWidth(20);
        column.setCellRenderer(attachmentCellRenderer);
        column.setCellEditor(attachmentCellEditor);

        column = tblAttachments.getColumnModel().getColumn(PDF_DESCRIPTION);
        //Modified for Case#4602 -Proposal routing window too small  - Start
//        column.setMinWidth(405);
//        column.setPreferredWidth(405);
        //Case#4602 - End

        column.setMinWidth(480);
        column.setPreferredWidth(480);
        column.setCellRenderer(new DefaultTableCellRenderer());

        dlgRouting.addEscapeKeyListener( new AbstractAction("escPressed") {
            public void actionPerformed(ActionEvent ar) {
                //COEUSQA-1433 - Allow Recall from Routing - Start
                releaseLock();
                //COEUSQA-1433 - Allow Recall from Routing - End
                close();
            }

        });

        dlgRouting.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                //COEUSQA-1433 - Allow Recall from Routing - Start
                releaseLock();
                //COEUSQA-1433 - Allow Recall from Routing - End
                close();
            }

        });

        btnClose.requestFocus();

        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
                if(COMMENTS_AND_ATTACHMENTS.equals(sourceTabbedPane.getTitleAt(sourceTabbedPane.getSelectedIndex()))) {
                    pnlProposalRoutingDetails.removeAll();
                    getCommentsAndDetails();
                    setCommentsAndDetails();
                }
            }
        };
        Routing.addChangeListener(changeListener);
        //COEUSQA:1445 - End

    }

    public void setTableEditors(){

        if(commentsTableModel==null){
            commentsTableModel = new CommentsTableModel();
        }

        if(attachmentTableModel == null){
            attachmentTableModel = new AttachmentsTableModel();
        }
        tblComments.setModel(commentsTableModel);
        tblAttachments.setModel(attachmentTableModel);

        JTableHeader tableHeader = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tblAttachments.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

    }

    public void registerObserver( Observer observer ) {
        observable.addObserver( observer );
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlGeneralInfo = new javax.swing.JPanel();
        lblRoutingStartDate = new edu.mit.coeus.utils.CoeusLabel();
        lblRoutingEndDate = new edu.mit.coeus.utils.CoeusLabel();
        lblSubmissionNumber = new edu.mit.coeus.utils.CoeusLabel();
        lblRoutingStartDateValue = new javax.swing.JLabel();
        lblRoutingEndDateValue = new javax.swing.JLabel();
        lblSubmissionNumberValue = new javax.swing.JLabel();
        btnShowPreviousSubmission = new edu.mit.coeus.utils.CoeusButton();
        lblComments = new edu.mit.coeus.utils.CoeusLabel();
        lblRoutingCommentsValue = new javax.swing.JLabel();
        lblRecallStatus = new javax.swing.JLabel();
        btnViewComments = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        Routing = new javax.swing.JTabbedPane();
        routingMapDetailsPanel = new javax.swing.JPanel();
        scrPnTree = new javax.swing.JScrollPane();
        treeSequentialStop = new javax.swing.JTree();
        scrPnSequentialStop = new javax.swing.JScrollPane();
        tblProposalRouting = new edu.mit.coeus.routing.gui.RoutingTable();
        tbdPnAttmntAndComment = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblComments = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAttachments = new javax.swing.JTable();
        pnlAggregatorInfo = new javax.swing.JPanel();
        lblRoutingAggregator = new edu.mit.coeus.utils.CoeusLabel();
        lblWithdrawnAggregator = new edu.mit.coeus.utils.CoeusLabel();
        lblRoutingAggregatorValue = new javax.swing.JLabel();
        lblWithdrawAggregatorValue = new javax.swing.JLabel();
        pnlRoutingButtons = new javax.swing.JPanel();
        btnApprove = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        btnBypass = new javax.swing.JButton();
        btnRecall = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnAddApprover = new javax.swing.JButton();
        btnAlternative = new javax.swing.JButton();
        commentsAttachmentsJPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlProposalRoutingDetails = new javax.swing.JPanel();

        // JM 2-18-2013 add reopen for approval button
        btnReopenAddApprover = new javax.swing.JButton();
        // JM END
        
        setMinimumSize(new java.awt.Dimension(760, 690)); // JM 8-6-2015 changed to 760; was 698
        setPreferredSize(new java.awt.Dimension(760, 700)); // JM 8-6-2015 changed to 760; was 698
        setLayout(new java.awt.GridBagLayout());

        pnlGeneralInfo.setMinimumSize(new java.awt.Dimension(263, 150));
        pnlGeneralInfo.setName("pnlGeneralInfo"); // NOI18N
        pnlGeneralInfo.setPreferredSize(new java.awt.Dimension(100, 150));
        pnlGeneralInfo.setLayout(new java.awt.GridBagLayout());

        lblRoutingStartDate.setText("Routing Start Date:");
        lblRoutingStartDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblRoutingStartDate.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingStartDate.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingStartDate.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        pnlGeneralInfo.add(lblRoutingStartDate, gridBagConstraints);

        lblRoutingEndDate.setText("Routing End Date:");
        lblRoutingEndDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblRoutingEndDate.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingEndDate.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingEndDate.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlGeneralInfo.add(lblRoutingEndDate, gridBagConstraints);

        lblSubmissionNumber.setText("Submission Number:");
        lblSubmissionNumber.setMaximumSize(new java.awt.Dimension(120, 20));
        lblSubmissionNumber.setMinimumSize(new java.awt.Dimension(120, 20));
        lblSubmissionNumber.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlGeneralInfo.add(lblSubmissionNumber, gridBagConstraints);

        lblRoutingStartDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblRoutingStartDateValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingStartDateValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingStartDateValue.setName("lblModule"); // NOI18N
        lblRoutingStartDateValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 0, 0);
        pnlGeneralInfo.add(lblRoutingStartDateValue, gridBagConstraints);

        lblRoutingEndDateValue.setFont(CoeusFontFactory.getNormalFont());
        lblRoutingEndDateValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingEndDateValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingEndDateValue.setName("lblStartDate"); // NOI18N
        lblRoutingEndDateValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        pnlGeneralInfo.add(lblRoutingEndDateValue, gridBagConstraints);

        lblSubmissionNumberValue.setFont(CoeusFontFactory.getNormalFont());
        lblSubmissionNumberValue.setMaximumSize(new java.awt.Dimension(50, 20));
        lblSubmissionNumberValue.setMinimumSize(new java.awt.Dimension(50, 20));
        lblSubmissionNumberValue.setName("lblEndDate"); // NOI18N
        lblSubmissionNumberValue.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        pnlGeneralInfo.add(lblSubmissionNumberValue, gridBagConstraints);

        btnShowPreviousSubmission.setMnemonic('S');
        btnShowPreviousSubmission.setText("Show Previous Submission");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        pnlGeneralInfo.add(btnShowPreviousSubmission, gridBagConstraints);

        lblComments.setText("Recall Comments:");
        lblComments.setMaximumSize(new java.awt.Dimension(120, 20));
        lblComments.setMinimumSize(new java.awt.Dimension(120, 20));
        lblComments.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 2, 0, 0);
        pnlGeneralInfo.add(lblComments, gridBagConstraints);

        lblRoutingCommentsValue.setFont(CoeusFontFactory.getNormalFont());
        lblRoutingCommentsValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingCommentsValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingCommentsValue.setName("lblModule"); // NOI18N
        lblRoutingCommentsValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 0, 0);
        pnlGeneralInfo.add(lblRoutingCommentsValue, gridBagConstraints);

        lblRecallStatus.setFont(CoeusFontFactory.getLabelFont());
        lblRecallStatus.setMaximumSize(new java.awt.Dimension(150, 20));
        lblRecallStatus.setMinimumSize(new java.awt.Dimension(150, 20));
        lblRecallStatus.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        pnlGeneralInfo.add(lblRecallStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 160;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 2, 1, 0);
        add(pnlGeneralInfo, gridBagConstraints);

        btnViewComments.setMaximumSize(new java.awt.Dimension(21, 20));
        btnViewComments.setMinimumSize(new java.awt.Dimension(21, 20));
        btnViewComments.setPreferredSize(new java.awt.Dimension(21, 20));
        btnViewComments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewCommentsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(25, 2, 0, 66);
        add(btnViewComments, gridBagConstraints);

        mainPanel.setMinimumSize(new java.awt.Dimension(745, 550)); // JM was 695
        mainPanel.setPreferredSize(new java.awt.Dimension(745, 580));
        mainPanel.setLayout(new java.awt.GridBagLayout());

        Routing.setMinimumSize(new java.awt.Dimension(745, 552)); // JM was 695 //745
        Routing.setPreferredSize(new java.awt.Dimension(745, 552)); 

        routingMapDetailsPanel.setLayout(new java.awt.GridBagLayout());

        scrPnTree.setMinimumSize(new java.awt.Dimension(22, 165));
        scrPnTree.setPreferredSize(new java.awt.Dimension(3, 165));
        scrPnTree.setViewportView(treeSequentialStop);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 8, 2);
        routingMapDetailsPanel.add(scrPnTree, gridBagConstraints);

        scrPnSequentialStop.setMinimumSize(new java.awt.Dimension(530, 165)); // JM 8-7-2015 changed to 530; was 570
        scrPnSequentialStop.setPreferredSize(new java.awt.Dimension(530, 165)); // JM 8-7-2015 changed to 530; was 570

        tblProposalRouting.setName("tblProposalRouting"); // NOI18N
        scrPnSequentialStop.setViewportView(tblProposalRouting);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 2);
        routingMapDetailsPanel.add(scrPnSequentialStop, gridBagConstraints);

        tbdPnAttmntAndComment.setMinimumSize(new java.awt.Dimension(530, 126)); // JM 8-7-2015 changed to 530; was 570
        tbdPnAttmntAndComment.setName("tbdPnAttmntAndComment"); // NOI18N
        tbdPnAttmntAndComment.setPreferredSize(new java.awt.Dimension(530, 180)); // JM 8-7-2015 changed to 530; was 570

        jPanel1.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jPanel1.setMinimumSize(new java.awt.Dimension(520, 125));
        jPanel1.setPreferredSize(new java.awt.Dimension(520, 125));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setMaximumSize(new java.awt.Dimension(520, 130));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(520, 97));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(520, 100));

        tblComments.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(tblComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel1.add(jScrollPane1, gridBagConstraints);

        tbdPnAttmntAndComment.addTab("Comments", jPanel1);

        jPanel2.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647)); 
        jPanel2.setMinimumSize(new java.awt.Dimension(520, 125)); 
        jPanel2.setPreferredSize(new java.awt.Dimension(520, 125)); 
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMaximumSize(new java.awt.Dimension(520, 120)); 
        jScrollPane2.setMinimumSize(new java.awt.Dimension(520, 97)); 
        jScrollPane2.setPreferredSize(new java.awt.Dimension(520, 97)); 
        jScrollPane2.setViewportView(tblAttachments);

        jPanel2.add(jScrollPane2, new java.awt.GridBagConstraints());

        tbdPnAttmntAndComment.addTab("Attachments", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 2);
        routingMapDetailsPanel.add(tbdPnAttmntAndComment, gridBagConstraints);

        pnlAggregatorInfo.setMinimumSize(new java.awt.Dimension(570, 66));
        pnlAggregatorInfo.setName("pnlGeneralInfo"); // NOI18N
        pnlAggregatorInfo.setPreferredSize(new java.awt.Dimension(570, 66));
        pnlAggregatorInfo.setLayout(new java.awt.GridBagLayout());

        lblRoutingAggregator.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRoutingAggregator.setText("Routed by Aggregator:");
        lblRoutingAggregator.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblRoutingAggregator.setMaximumSize(new java.awt.Dimension(160, 20));
        lblRoutingAggregator.setMinimumSize(new java.awt.Dimension(160, 20));
        lblRoutingAggregator.setPreferredSize(new java.awt.Dimension(160, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        pnlAggregatorInfo.add(lblRoutingAggregator, gridBagConstraints);

        lblWithdrawnAggregator.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblWithdrawnAggregator.setText("Recall by Aggregator:");
        lblWithdrawnAggregator.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        lblWithdrawnAggregator.setMaximumSize(new java.awt.Dimension(160, 20));
        lblWithdrawnAggregator.setMinimumSize(new java.awt.Dimension(160, 20));
        lblWithdrawnAggregator.setPreferredSize(new java.awt.Dimension(160, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 16, 0);
        pnlAggregatorInfo.add(lblWithdrawnAggregator, gridBagConstraints);

        lblRoutingAggregatorValue.setFont(CoeusFontFactory.getNormalFont());
        lblRoutingAggregatorValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblRoutingAggregatorValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblRoutingAggregatorValue.setName("lblModule"); // NOI18N
        lblRoutingAggregatorValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        pnlAggregatorInfo.add(lblRoutingAggregatorValue, gridBagConstraints);

        lblWithdrawAggregatorValue.setFont(CoeusFontFactory.getNormalFont());
        lblWithdrawAggregatorValue.setMaximumSize(new java.awt.Dimension(120, 20));
        lblWithdrawAggregatorValue.setMinimumSize(new java.awt.Dimension(120, 20));
        lblWithdrawAggregatorValue.setName("lblStartDate"); // NOI18N
        lblWithdrawAggregatorValue.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 16, 0);
        pnlAggregatorInfo.add(lblWithdrawAggregatorValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 2);
        routingMapDetailsPanel.add(pnlAggregatorInfo, gridBagConstraints);

        pnlRoutingButtons.setMaximumSize(new java.awt.Dimension(BTNWIDTH + 12, 210)); // JM 7-22-2015 static width //12
        pnlRoutingButtons.setMinimumSize(new java.awt.Dimension(BTNWIDTH + 12, 210)); // JM 7-22-2015 static width
        pnlRoutingButtons.setPreferredSize(new java.awt.Dimension(BTNWIDTH + 12, 210)); // JM 7-22-2015 static width
        pnlRoutingButtons.setLayout(new java.awt.GridBagLayout());

        btnApprove.setFont(CoeusFontFactory.getLabelFont());
        btnApprove.setMnemonic('A');
        btnApprove.setText("Approve");
        btnApprove.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnApprove.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnApprove.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnApprove, gridBagConstraints);

        btnReject.setFont(CoeusFontFactory.getLabelFont());
        btnReject.setMnemonic('R');
        btnReject.setText("Reject");
        btnReject.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnReject.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnReject.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnReject, gridBagConstraints);

        btnBypass.setFont(CoeusFontFactory.getLabelFont());
        btnBypass.setMnemonic('B');
        btnBypass.setText("Bypass");
        btnBypass.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnBypass.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnBypass.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnBypass, gridBagConstraints);

        btnRecall.setFont(CoeusFontFactory.getLabelFont());
        btnRecall.setMnemonic('E');
        btnRecall.setText("Recall");
        btnRecall.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnRecall.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnRecall.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnRecall, gridBagConstraints);

        btnClose.setFont(CoeusFontFactory.getLabelFont());
        btnClose.setMnemonic('C');
        btnClose.setText("Close");
        btnClose.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnClose.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnClose.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnClose, gridBagConstraints);

        btnAddApprover.setFont(CoeusFontFactory.getLabelFont());
        btnAddApprover.setText("Add Additional Approver");  // JM 7-22-2015 changed label for clarification
        btnAddApprover.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnAddApprover.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnAddApprover.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        // JM 7-21-2015 added tooltip for this button
        btnAddApprover.setToolTipText(CoeusToolTip.getToolTip("addlApproverBtn_toolTip.1001"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        pnlRoutingButtons.add(btnAddApprover, gridBagConstraints);

        btnAlternative.setFont(CoeusFontFactory.getLabelFont());
        btnAlternative.setText("Add Alternate Approver");  // JM 7-22-2015 changed label for clarification
        btnAlternative.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnAlternative.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnAlternative.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        // JM 7-21-2015 added tooltip for this button
        btnAlternative.setToolTipText(CoeusToolTip.getToolTip("altApproverBtn_toolTip.1001"));
        // JM END
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0); // JM 8-11-2015 added
        pnlRoutingButtons.add(btnAlternative, gridBagConstraints);

        // JM 2-18-2013 superuser reopen and add approver
        btnReopenAddApprover.setFont(CoeusFontFactory.getLabelFont());
        btnReopenAddApprover.setText("Reopen");
        btnReopenAddApprover.setMaximumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnReopenAddApprover.setMinimumSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        btnReopenAddApprover.setPreferredSize(new java.awt.Dimension(BTNWIDTH, BTNHEIGHT)); // JM 7-22-2015 static size
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0); // JM 8-11-2015 added
        // The button will only show up if the proposal is approved, submitted,
        // or post-submission approval and the user has rights
        edu.vanderbilt.coeus.utils.CustomFunctions func = new edu.vanderbilt.coeus.utils.CustomFunctions();
        try {
			if (func.getProposalStatusCode(this.routingBean.getModuleItemKey()) == 4 ||
				func.getProposalStatusCode(this.routingBean.getModuleItemKey()) == 5 || 
				func.getProposalStatusCode(this.routingBean.getModuleItemKey()) == 6) {
			    if (hasReopenRight()) {
			    	pnlRoutingButtons.remove(btnBypass);
			    	pnlRoutingButtons.add(btnReopenAddApprover, gridBagConstraints);
			    }
			}
		} catch (CoeusClientException e) {
			System.out.println("Cannot determine proposal status");
			e.printStackTrace();
		}
        // JM END

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        // JM 5-22-2015 increased gridheight to 5 to keep Mac from cutting off at bottom, was 4
        gridBagConstraints.gridheight = 5; 
        // JM END
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 8); // JM 8-11-2015 added
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        
        routingMapDetailsPanel.add(pnlRoutingButtons, gridBagConstraints);
        
        routingMapDetailsPanel.setMinimumSize(new java.awt.Dimension(756, 550)); // JM 
        routingMapDetailsPanel.setPreferredSize(new java.awt.Dimension(756, 550)); // JM        

        Routing.addTab("Routing Map Details", routingMapDetailsPanel);

        commentsAttachmentsJPanel.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setMinimumSize(new java.awt.Dimension(662, 518)); 
        jScrollPane3.setPreferredSize(new java.awt.Dimension(662, 518)); 

        pnlProposalRoutingDetails.setMinimumSize(new java.awt.Dimension(0, 0));
        pnlProposalRoutingDetails.setPreferredSize(new java.awt.Dimension(0, 0));

        jScrollPane3.setViewportView(pnlProposalRoutingDetails);

        commentsAttachmentsJPanel.add(jScrollPane3, new java.awt.GridBagConstraints());

        Routing.addTab("Comments and Attachments", commentsAttachmentsJPanel);

        mainPanel.add(Routing, new java.awt.GridBagConstraints());
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 20, 0);
        add(mainPanel, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewCommentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewCommentsActionPerformed

    }//GEN-LAST:event_btnViewCommentsActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane Routing;
    private javax.swing.JButton btnAddApprover;
    private javax.swing.JButton btnAlternative;
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBypass;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnRecall;
    private javax.swing.JButton btnReject;
    private edu.mit.coeus.utils.CoeusButton btnShowPreviousSubmission;
    private javax.swing.JButton btnViewComments;
    private javax.swing.JPanel commentsAttachmentsJPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private edu.mit.coeus.utils.CoeusLabel lblComments;
    private javax.swing.JLabel lblRecallStatus;
    private edu.mit.coeus.utils.CoeusLabel lblRoutingAggregator;
    private javax.swing.JLabel lblRoutingAggregatorValue;
    private javax.swing.JLabel lblRoutingCommentsValue;
    private edu.mit.coeus.utils.CoeusLabel lblRoutingEndDate;
    private javax.swing.JLabel lblRoutingEndDateValue;
    private edu.mit.coeus.utils.CoeusLabel lblRoutingStartDate;
    private javax.swing.JLabel lblRoutingStartDateValue;
    private edu.mit.coeus.utils.CoeusLabel lblSubmissionNumber;
    private javax.swing.JLabel lblSubmissionNumberValue;
    private javax.swing.JLabel lblWithdrawAggregatorValue;
    private edu.mit.coeus.utils.CoeusLabel lblWithdrawnAggregator;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pnlAggregatorInfo;
    private javax.swing.JPanel pnlGeneralInfo;
    private javax.swing.JPanel pnlProposalRoutingDetails;
    private javax.swing.JPanel pnlRoutingButtons;
    private javax.swing.JPanel routingMapDetailsPanel;
    private javax.swing.JScrollPane scrPnSequentialStop;
    private javax.swing.JScrollPane scrPnTree;
    private javax.swing.JTabbedPane tbdPnAttmntAndComment;
    private javax.swing.JTable tblAttachments;
    private javax.swing.JTable tblComments;
    private edu.mit.coeus.routing.gui.RoutingTable tblProposalRouting;
    private javax.swing.JTree treeSequentialStop;
    
    // JM 2-18-2013 reopen for approval button
    private javax.swing.JButton btnReopenAddApprover;
    // JM END
    
    // End of variables declaration//GEN-END:variables
    
    //    public static void main(String s[]){
    //        JFrame frame = new JFrame("Proposal Routing");
    //        frame.setSize(540, 450);
    //        RoutingForm form = new RoutingForm(frame, new ProposalDevelopmentFormBean(), true);
    //        frame.getContentPane().add(form);
    //        frame.show();
    //    }
    
//    public CoeusVector sortByPrimaryApprover(CoeusVector cvProposalApprovalBean) {
//        CoeusVector cvSortedData =  new CoeusVector();
//        for(int index=0; index < cvProposalApprovalBean.size(); index++) {
//
//            ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean ) cvProposalApprovalBean.get(index);
//
//            if( cvSortedData.contains(proposalApprovalBean) ) continue;
//
//            Equals eqLevelNumber = new Equals("levelNumber", new Integer(proposalApprovalBean.getLevelNumber() ));
//            Equals eqStopNumber = new Equals("stopNumber", new Integer(proposalApprovalBean.getStopNumber() ));
//            And eqLevelAndEqStop = new And(eqLevelNumber, eqStopNumber);
//            CoeusVector cvFilteredData = cvProposalApprovalBean.filter(eqLevelAndEqStop);
//            if( cvFilteredData.size() > 1 ){
//                for( int subIndex = 0; subIndex < cvFilteredData.size(); subIndex++ ){
//                    ProposalApprovalBean tempProposalApprovalBean = (ProposalApprovalBean)cvFilteredData.get(subIndex);
//                    if( tempProposalApprovalBean.isPrimaryApproverFlag() ){
//                        cvSortedData.addElement(tempProposalApprovalBean);
//                        cvFilteredData.removeElement(tempProposalApprovalBean);
//                        cvSortedData.addAll(cvFilteredData);
//                        break;
//                    }else{
//                        continue;
//                    }
//                }
//            }else{
//                cvSortedData.addElement(proposalApprovalBean);
//            }
//        }
//        return cvSortedData;
//    }
    
    /** Sets the form data */
     private void setFormData(){
        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                if((((ProposalDevelopmentFormBean)moduleBean).getCreationStatusCode() == 2) ||
                        (((ProposalDevelopmentFormBean)moduleBean).getCreationStatusCode() == 6)) {
                    enableApproveRejectButtons(true);
                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    enableRecallButton(TRUE);
                    //COEUSQA-1433 - Allow Recall from Routing - End
                }else{
                    enableApproveRejectButtons(false);

                    //COEUSQA-1433 - Allow Recall from Routing - Start
                    enableRecallButton(FALSE);
                    //COEUSQA-1433 - Allow Recall from Routing - End
                }
                //routingBean = ((ProposalDevelopmentFormBean)moduleBean).getRoutingBean();
            }
        }
                //coeus 2111 starts
        else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
        enableApproveRejectButtons(true);
        }
        //coeus 2111 ends
        //COEUSQA-1433 - Allow Recall from Routing - Start
        if(userHasRecallRights()){
            btnRecall.setVisible(true);
        }else{
            btnRecall.setVisible(false);
        }
        enableRecallButton("check");

        //to enable/disable bypass button
        disableByPassButton();
        //COEUSQA-1433 - Allow Recall from Routing - End
        setGeneralDetails();
        //COEUSQA-1433 - Allow Recall from Routing - Start
        if(lblRoutingCommentsValue.getText() != null && lblRoutingCommentsValue.getText().length()>0){
            btnViewComments.setVisible(true);
        }else{
            btnViewComments.setVisible(false);
        }
        //COEUSQA-1433 - Allow Recall from Routing - End
        tblProposalRouting.setModel(tblProposalRouting.getTableModel());
        tblProposalRouting.formatTable();

        //ADDED by Ranjeev

        if(cvRoutingMapBeans != null && cvRoutingMapBeans.size() > 0) {
//            if( cvExistingApprovers != null && cvExistingApprovers.size() > 0 ){
            //Set the table data for the selected node
            //tblProposalRouting.setTableData(addSequentialStops(cvExistingApprovers));
//            }

            treeSequentialStop.setCellRenderer(new TreeNodeRenderer() );
            treeSequentialStop.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            //Creates the Tree with the Vector of ProposalApprovalMapBean
            createTree();

            //Set the current approval bean
            setCurrentApprovalBean();

            treeSequentialStop.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent evt) {
                	
                    // Get all nodes whose selection status has changed
                    try {
                        TreePath path = treeSequentialStop.getSelectionPath();
                        if( path == null ) return ;
                        RoutingMapBeanNode selectedRoutingMapBeanNode = (RoutingMapBeanNode)path.getLastPathComponent();
                        //ProposalApprovalMapBean selectedProposalApprovalMapBean = selectedRoutingMapBeanNode.getDataObject();
                        RoutingMapBean selectedRoutingMapBean = selectedRoutingMapBeanNode.getDataObject();
                        
                        cvExistingApprovers = selectedRoutingMapBean.getRoutingMapDetails();
                        CoeusVector cvOtherApprovers = new CoeusVector();
                        if(cvExistingApprovers != null && cvExistingApprovers.size()>0){
                            for(int index = 0; index < cvExistingApprovers.size(); index++){
                                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvExistingApprovers.get(index);
                                if(!routingDetailsBean.isPrimaryApproverFlag()){
                                    cvOtherApprovers.add(routingDetailsBean);
                                    cvExistingApprovers.remove(index--);
                                }
                            }
                            cvExistingApprovers.addAll(cvOtherApprovers);
                        }
                        if( cvExistingApprovers != null && cvExistingApprovers.size() > 0 ){
                            //Set the table data for the selected node
                            cvApproversData = addSequentialStops(cvExistingApprovers);
                            tblProposalRouting.setTableData(cvApproversData);
                            tblProposalRouting.setRowSelectionInterval(1,1);
                        //Code added for Case#3612 - Parallel Routing and Show Routing implementation
                        } else {
                            tblProposalRouting.setTableData(new CoeusVector());
                        }

                    }catch (Exception exp ) {
                        exp.printStackTrace();
                    }

                }
            });

            //Set the Tree Node Selection corresponding to the map id of the current bean
            setTreeNodeSelection();

            //TreePath [] paths = treeSequentialStop.getPathForRow(row);
            //treeSequentialStop.setSelectionRow(0);
            //treeSequentialStop.setSelectionPath(path);

            treeSequentialStop.setBackground(colorPanelBackground);
            //Check if ByPass Button is to be Visible
            if(isByPassFlag){
                btnBypass.setVisible(true);
            }else{
                btnBypass.setVisible(false);
            }

            //Set the first row as the selected row
            if(tblProposalRouting.getRowCount() > 0){
                tblProposalRouting.setRowSelectionInterval(1, 1);
            }

        } else {
            isRoutingDataAvailable = false;
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(NO_ROUTING_MAP));

            return;
        }

    }


    public void setGeneralDetails(){
//        DateUtils dateUtils = new DateUtils();
//        String DATE_FORMAT_DISPLAY = "dd-MMM-yyyy";
        if(routingBean!=null){
            if(routingBean.getRoutingStartDate() != null && routingBean.getRoutingStartDate().length() > 0){
                lblRoutingStartDateValue.setText(routingBean.getRoutingStartDate()
                    + " by " + UserUtils.getDisplayName(routingBean.getRoutingStartUser()));
            } else {
                lblRoutingStartDateValue.setText("");
            }
            if(routingBean.getRoutingEndDate() != null && routingBean.getRoutingEndDate().length() > 0){
                lblRoutingEndDateValue.setText(routingBean.getRoutingEndDate()
                    + " by " + UserUtils.getDisplayName(routingBean.getRoutingEndUser()));
            } else {
                lblRoutingEndDateValue.setText("");
            }
            //Modified for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved
//            lblSubmissionNumberValue.setText(routingBean.getApprovalSequence()+"");
//            if(routingBean.getApprovalSequence() > 1){
//                btnShowPreviousSubmission.setVisible(true);
//            } else {
//                btnShowPreviousSubmission.setVisible(false);
//            }
            if((isProtocol(moduleCode))||(moduleCode==ModuleConstants.AWARD_MODULE_CODE)){
                lblSubmissionNumberValue.setText(currentSubmissionNum+"");
                if(currentSubmissionNum > 1){
                    btnShowPreviousSubmission.setVisible(true);
                } else {
                    btnShowPreviousSubmission.setVisible(false);
                }
                //COEUSQA-1433 - Allow Recall from Routing - Start
                lblRoutingCommentsValue.setText(routingBean.getRoutingComments());
                if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                    lblRoutingAggregatorValue.setText(((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getCreateUser()+" "+routingBean.getRoutingStartDate());
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - Start
                    lblWithdrawAggregatorValue.setText(CoeusGuiConstants.EMPTY_STRING);
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - End
                    if(routingBean.getRoutingComments().trim().length() > 0){
                        lblWithdrawAggregatorValue.setText(routingBean.getRoutingEndUser()+" "+routingBean.getRoutingEndDate());
                    }
                }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                    lblRoutingAggregatorValue.setText(((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getCreateUser()+" "+routingBean.getRoutingStartDate());
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - Start
                    lblWithdrawAggregatorValue.setText(CoeusGuiConstants.EMPTY_STRING);
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - End
                    if(routingBean.getRoutingComments().trim().length() > 0){
                        lblWithdrawAggregatorValue.setText(routingBean.getRoutingEndUser()+" "+routingBean.getRoutingEndDate());
                    }
                }
                //COEUSQA-1433 - Allow Recall from Routing - End
            }else{
                //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
                lblRoutingCommentsValue.setText(routingBean.getRoutingComments());
                //COEUSQA:3441 - End
                lblSubmissionNumberValue.setText(routingBean.getApprovalSequence()+"");
                if(routingBean.getApprovalSequence() > 1){
                    btnShowPreviousSubmission.setVisible(true);
                } else {
                    btnShowPreviousSubmission.setVisible(false);
                }
                //COEUSQA-1433 - Allow Recall from Routing - Start
                if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                    lblRoutingAggregatorValue.setText(((ProposalDevelopmentFormBean)moduleBean).getCreateUser()+" "+routingBean.getRoutingStartDate());
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - Start
                    lblWithdrawAggregatorValue.setText(CoeusGuiConstants.EMPTY_STRING);
                    // Added for COEUSQA-3824 : Proposal development recall message shows from wrong user - End
                    if(routingBean.getRoutingComments().trim().length() > 0){
                        lblWithdrawAggregatorValue.setText(routingBean.getRoutingEndUser()+" "+routingBean.getRoutingEndDate());
                    }
                }
                //COEUSQA-1433 - Allow Recall from Routing - End
            }
            //COEUSQA-2249 : End
        }
    }

    /**
     * Set the Tree Node Selection based on ApprovalStatus Flag passed to it
     */
//     private boolean setTreeNodeSelection(String approvalStatus) {
//
//
//         boolean isSelectionNodeIdentified  = false;
//
//        try {
//
//        TreePath path = null;
//        for(int index =0 ;index < treeSequentialStop.getRowCount(); index++) {
//            path = treeSequentialStop.getPathForRow(index);
//            RoutingMapBeanNode selectedProposalApprovalMapBeanNode = (RoutingMapBeanNode)path.getPathComponent(treeSequentialStop.getRowForPath(path));
//            ProposalApprovalMapBean selectedProposalApprovalMapBean = selectedProposalApprovalMapBeanNode.getDataObject();
//            CoeusVector  cvEachMapBeanApprovers = selectedProposalApprovalMapBean.getProposalApprovals();
//            for(int subIndex =0 ;subIndex < cvEachMapBeanApprovers.size(); subIndex++) {
//
//                ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean )cvEachMapBeanApprovers.get(subIndex);
//                if(proposalApprovalBean.getApprovalStatus().equalsIgnoreCase(approvalStatus)) {
//                    treeSequentialStop.setSelectionPath(path);
//                    requiredRoutingMapBean = selectedProposalApprovalMapBean;
//                    cvExistingApprovers = selectedProposalApprovalMapBeanNode.getDataObject().getProposalApprovals();
//                    isSelectionNodeIdentified = true;
//                    subIndex = cvEachMapBeanApprovers.size();
//                    index = treeSequentialStop.getRowCount();
//                }
//            }
//        }
//        if((isSelectionNodeIdentified == false ) && ( treeSequentialStop.getRowCount() > 0))
//            treeSequentialStop.setSelectionPath(treeSequentialStop.getPathForRow(treeSequentialStop.getRowCount() - 1));
//
//        }catch(Exception treeSelectionExp) {
//           // treeSelectionExp.printStackTrace();
//        }
//        return isSelectionNodeIdentified;
//    }
    private boolean setTreeNodeSelection() {


        boolean isSelectionNodeIdentified  = false;

        try {
            treeSequentialStop.clearSelection();
            selectNode(treeRoot);
            TreePath selectedPath = treeSequentialStop.getSelectionPath();
            if( selectedPath == null) {
                isSelectionNodeIdentified = false;
            }else{
                isSelectionNodeIdentified = true;
            }
            if((isSelectionNodeIdentified == false ) && ( treeSequentialStop.getRowCount() > 0)){
                treeSequentialStop.setSelectionPath(treeSequentialStop.getPathForRow(treeSequentialStop.getRowCount() - 1));
            }

        }catch(Exception treeSelectionExp) {
            treeSelectionExp.printStackTrace();
        }
        return isSelectionNodeIdentified;
    }

    private void selectNode(DefaultMutableTreeNode node) {
        if( node != null ) {

//            ProposalApprovalMapBean selectedProposalApprovalMapBean =
//                ((RoutingMapBeanNode)node).getDataObject();
            RoutingMapBean selectedRoutingMapBean =
                    ((RoutingMapBeanNode)node).getDataObject();
            
            CoeusVector  cvEachMapBeanApprovers = selectedRoutingMapBean.getRoutingMapDetails();
            
            int approversSize = 0;
            if( cvEachMapBeanApprovers != null ) {
                approversSize = cvEachMapBeanApprovers.size();
            }
            for(int subIndex =0 ;subIndex < approversSize; subIndex++) {

                //ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean )cvEachMapBeanApprovers.get(subIndex);
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean )cvEachMapBeanApprovers.get(subIndex);
                //if(routingDetailsBean.getMapId() == currentRoutingDetailsBean.getMapId() ){
                if(routingDetailsBean.getMapNumber() == currentRoutingDetailsBean.getMapNumber() ){
//                if(proposalApprovalBean.getApprovalStatus().equalsIgnoreCase(approvalStatus)) {
                    treeSequentialStop.setSelectionPath(new TreePath(node.getPath()));
                    requiredRoutingMapBean = selectedRoutingMapBean;
                    //cvExistingApprovers = ((RoutingMapBeanNode)node).getDataObject().getProposalApprovals();
                    cvExistingApprovers = ((RoutingMapBeanNode)node).getDataObject().getRoutingMapDetails();
                    //isSelectionNodeIdentified = true;
                    return;
                }
            }
            if( node.getChildCount() > 0 ) {
                Enumeration enumChildren = node.children();
                while(enumChildren.hasMoreElements()){
                    DefaultMutableTreeNode childNode =
                            (DefaultMutableTreeNode)enumChildren.nextElement();
                    selectNode(childNode);
                }
            }
        }

    }

    /** Creates the Tree with the Vector of ProposalMapBean obtained from server
     *  This Method builds the Tree using the Vector of ProposalApprovalMapBeans
     */
    public void createTree() {

        if(treeRoot != null){
            treeRoot.removeAllChildren();
        }

        //Commented during Code-review
//        Hashtable hashTable = new Hashtable();

        tempVecProposalApprovalMapBeans.removeAllElements();
        tempVecProposalApprovalMapBeans.addAll(cvRoutingMapBeans);
        //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//        tempVecProposalApprovalMapBeans.sort(PARENT_MAP_ID_FIELD, true);
        tempVecProposalApprovalMapBeans.sort(PARENT_MAP_NUMBER_FIELD, true);

        // setting the Root Node
        //ProposalApprovalMapBean proposalApprovalMapBean = getParentMapBean();
        RoutingMapBean routingMapBean = getParentMapBean();
        treeRoot = (DefaultMutableTreeNode)((DefaultTreeModel)treeSequentialStop.getModel()).getRoot();
        hmMapNumbers = new HashMap();
        addNode(treeRoot,routingMapBean);
        (( DefaultTreeModel )treeSequentialStop.getModel() ).reload();
        expandAll(treeSequentialStop,true);


//        String selectedNodeValue = getLastMapIdValue(cvRoutingMapBeans);
//        TreePath selectedPath = treeSequentialStop.getSelectionPath();
//
//         if( selectedPath != null ) {
//             RoutingMapBeanNode selectedNode = ( RoutingMapBeanNode )selectedPath.getLastPathComponent();
//             ProposalApprovalMapBean proposalApprovalMapBeanLinkBean = selectedNode.getDataObject();
//             selectedNodeValue = proposalApprovalMapBeanLinkBean.getDescription().toString();
//             cvExistingApprovers = proposalApprovalMapBeanLinkBean.getProposalApprovals();
//         }
//         TreePath newSelectedPath = findByName(treeSequentialStop,selectedNodeValue );
//         if( newSelectedPath != null ) {
//             treeSequentialStop.expandPath( newSelectedPath );
//             treeSequentialStop.setSelectionPath( newSelectedPath );
//             treeSequentialStop.scrollRowToVisible(
//             treeSequentialStop.getRowForPath(newSelectedPath));
//
//         }


    }

    //Code is not used anywhere, so it is commented
//    public String getLastMapIdValue(CoeusVector vecProposalApprovalMapBeans) {
//
//        ProposalApprovalMapBean parentProposalApprovalMapBean = null;
//        CoeusVector cvSortedMapBeans = new CoeusVector();
//        cvSortedMapBeans.addAll(vecProposalApprovalMapBeans);
//        ProposalApprovalMapBean proposalApprovalMapBean;
//        for(int index = 0; index < cvSortedMapBeans.size();index++) {
//
//            proposalApprovalMapBean = (ProposalApprovalMapBean) cvSortedMapBeans.get(index);
//            int  mapNumber = proposalApprovalMapBean.getMapNumber();
//            Equals eqMapNumber = new Equals(PARENT_MAP_NUMBER_FIELD, new Integer(mapNumber));
//            CoeusVector parentVector =  cvSortedMapBeans.filter(eqMapNumber);
//            if (parentVector == null || parentVector.size() == 0) {
//                parentProposalApprovalMapBean = proposalApprovalMapBean;
//                index = tempVecProposalApprovalMapBeans.size();
//            } else {
//                cvSortedMapBeans.remove(proposalApprovalMapBean);
//                index = 0;
//            }
//        }
//        return parentProposalApprovalMapBean.getDescription();
//    }




    /**
     * To get the TreePath from the selected Tree Node.
     * @param tree DnDJTree Instance of complete AOR data
     * @param name Name of the String to be Searched in the tree Nodes.
     *
     * @return TreePath complete path from root to the match found node.
     */
    public TreePath findByName( JTree tree, String name ) {

        TreeNode root = (TreeNode)tree.getModel().getRoot();
        TreePath result = find2(tree, new TreePath(root), name);
        return result;
    }


    //supporting method for findByName method - recurcive implementation.
    private TreePath find2(JTree tree, TreePath parent, String nodeName) {

        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node != null && node.toString().trim().startsWith(nodeName)) {
            return parent;
        }else{

//            Object o = node;  //Commented during Code-review
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode nodes = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(nodes);
                    TreePath result = find2(tree, path, nodeName);
                    if(result!=null){
                        return result;
                    }
                    // Found a match
                }
            }
        }
        // No match at this branch
        return null;
    }


    //private void addNode(DefaultMutableTreeNode parent,ProposalApprovalMapBean proposalApprovalMapBean){
    private void addNode(DefaultMutableTreeNode parent,RoutingMapBean routingMapBean){
        RoutingMapBeanNode childRoutingMapBeanNode = new RoutingMapBeanNode(routingMapBean);

        parent.add(childRoutingMapBeanNode);
        //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//        Equals equalsmapID = new Equals(PARENT_MAP_ID_FIELD, new Integer(routingMapBean.getMapId()));
//        CoeusVector children =  cvRoutingMapBeans.filter(equalsmapID);
        Equals equalsmapNumber = new Equals(PARENT_MAP_NUMBER_FIELD, new Integer(routingMapBean.getMapNumber()));
        CoeusVector children =  cvRoutingMapBeans.filter(equalsmapNumber);
        hmMapNumbers.put(""+routingMapBean.getMapNumber(), ""+routingMapBean.getMapNumber());
        if( children != null && children.size() > 0 ) {
            for( int indx = 0; indx < children.size(); indx++ ) {
//                    ProposalApprovalMapBean childApprovalMapBean =
//                        (ProposalApprovalMapBean)children.elementAt(indx);
                RoutingMapBean childRoutingMapBean =
                        (RoutingMapBean)children.elementAt(indx);
                if(hmMapNumbers.get(""+childRoutingMapBean.getMapNumber()) == null){
                    hmMapNumbers.put(""+childRoutingMapBean.getMapNumber(), ""+childRoutingMapBean.getMapNumber());
                    addNode(childRoutingMapBeanNode, childRoutingMapBean);
                }
            }
        }
    }

    //Code is not used anywhere, so it is commented
//    public void createTree_old() {
//
//        if(treeRoot != null){
//            treeRoot.removeAllChildren();
//        }
//
//        Hashtable hashTable = new Hashtable();
//
//        tempVecProposalApprovalMapBeans.removeAllElements();
//        tempVecProposalApprovalMapBeans.addAll(cvRoutingMapBeans);
//        tempVecProposalApprovalMapBeans.sort(PARENT_MAP_ID_FIELD, true);
//
//        // setting the Root Node
//        //ProposalApprovalMapBean proposalApprovalMapBean = getParentMapBean();
//        RoutingMapBean routingMapBean = getParentMapBean();
//        RoutingMapBeanNode routingMapBeanNode;
//        routingMapBeanNode = new RoutingMapBeanNode(routingMapBean);
//
//        ((DefaultTreeModel)treeSequentialStop.getModel()).setRoot(routingMapBeanNode);
//
//        treeRoot = (DefaultMutableTreeNode)((DefaultTreeModel)treeSequentialStop.getModel()).getRoot();
//
//        //hashTable.put(((ProposalApprovalMapBean)routingMapBeanNode.getDataObject()).getParentMapId()+"",treeRoot );
//        hashTable.put(((RoutingMapBean)routingMapBeanNode.getDataObject()).getParentMapNumber()+"",treeRoot );
//
//        RoutingMapBeanNode parentRoutingMapBeanNode = null;
//        RoutingMapBeanNode childRoutingMapBeanNode;
//        for(int index = 0; index < tempVecProposalApprovalMapBeans.size();index++) {
//
//            childRoutingMapBeanNode = new RoutingMapBeanNode(getParentMapBean());
//            RoutingMapBean routingMapBeans = childRoutingMapBeanNode.getDataObject();
//
//            parentRoutingMapBeanNode = (RoutingMapBeanNode) hashTable.get(routingMapBeans.getParentMapNumber()+"");
//            if(parentRoutingMapBeanNode == null){
//                treeRoot.add(childRoutingMapBeanNode);
//                hashTable.put(((RoutingMapBean)childRoutingMapBeanNode.getDataObject()).getParentMapNumber()+"",childRoutingMapBeanNode);
//            } else {
//
//                //parentRoutingMapBeanNode.add(childRoutingMapBeanNode);
//                //treeRoot.add(childRoutingMapBeanNode);
//                //hashTable.put(routingMapBeans.getParentMapId()+"",childRoutingMapBeanNode);
//            }
//
//            (( DefaultTreeModel )treeSequentialStop.getModel() ).reload();
//
//
//        }
//
//    }



    /**
     *Getting the Each Node MapBean from the Vector of ProposalApprovalMapBean
     */

    public ProposalApprovalMapBean getNodeMapBean(CoeusVector vecProposalApprovalMapBeans) {

        ProposalApprovalMapBean parentProposalApprovalMapBean = null;
        for(int index = 0; index < vecProposalApprovalMapBeans.size();index++) {

            ProposalApprovalMapBean proposalApprovalMapBeans = (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
            int parentNodeID = proposalApprovalMapBeans.getParentMapId();
            NotEquals notEqualsmapID = new NotEquals("mapId",new Integer(parentNodeID));
            CoeusVector parentVector =  vecProposalApprovalMapBeans.filter(notEqualsmapID);

            for(int subindex = 0; subindex < vecProposalApprovalMapBeans.size();subindex ++) {
                ProposalApprovalMapBean subProposalApprovalMapBean =  (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
                if(parentNodeID != subProposalApprovalMapBean.getMapId()){
                    parentProposalApprovalMapBean = proposalApprovalMapBeans;
                }
            }
        }

        return parentProposalApprovalMapBean;
    }


    /**
     * Method Returns the ProposalApprovalMapBean Map  from the Vector of ProposalApprovalMapBean
     */
    public RoutingMapBean getParentMapBean() {
        //tempVecProposalApprovalMapBeans
        //Code modified for Case#3612 - Parallel Routing and Show Routing implementation - starts
        //ProposalApprovalMapBean parentProposalApprovalMapBean = null;
//        RoutingMapBean parentRoutingMapBean = null;
//        CoeusVector cvSortedMapBeans = new CoeusVector();
//        //ProposalApprovalMapBean proposalApprovalMapBean;
//        RoutingMapBean routingMapBean;
//        for(int index = 0; index < tempVecProposalApprovalMapBeans.size();index++) {
//
//            routingMapBean = (RoutingMapBean) tempVecProposalApprovalMapBeans.get(index);
//            int parentNodeID = routingMapBean.getParentMapId();
//            Equals eqMapID = new Equals("mapId",new Integer(parentNodeID));
//            CoeusVector parentVector =  tempVecProposalApprovalMapBeans.filter(eqMapID);
//            if (parentVector == null || parentVector.size() == 0) {
//                parentRoutingMapBean = routingMapBean;
//                tempVecProposalApprovalMapBeans.remove(index);
//                index = tempVecProposalApprovalMapBeans.size();
//            }
//        }
        //Added to show the Root node in the routing window
        RoutingMapBean parentRoutingMapBean = new RoutingMapBean();
        parentRoutingMapBean.setParentMapNumber(-1);
        parentRoutingMapBean.setMapId(0);
        parentRoutingMapBean.setDescription("Routing");
        parentRoutingMapBean.setMapNumber(0);
        //Code modified for Case#3612 - Parallel Routing and Show Routing implementation - ends
        return parentRoutingMapBean;
    }

       //Code is not used any where, so it is commented
//    /**
//     * Getting the Parent Map Id from the Vector containing ProposalApprovalMapBean
//     */
//
//    public int getParentMapId(Vector vecProposalApprovalMapBeans) {
//
//        int parentMapId = 0;
//
//        for(int index = 0; index < vecProposalApprovalMapBeans.size();index++) {
//
//            ProposalApprovalMapBean proposalApprovalMapBeans = (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
//            int parentNodeID = this.routingMapBean.getParentMapId();
//
//            for(int subindex = 0; subindex < vecProposalApprovalMapBeans.size();subindex ++) {
//                ProposalApprovalMapBean subProposalApprovalMapBean =  (ProposalApprovalMapBean) vecProposalApprovalMapBeans.get(index);
//                if(parentNodeID == subProposalApprovalMapBean.getMapId()){
//                    break;
//                }else
//                    parentMapId =  parentNodeID;
//            }
//        }
//
//        return parentMapId;
//    }
//

    private void setCurrentApprovalBean(){
        Equals eqActualUserId = new Equals(USER_ID_FIELD, mdiForm.getUserId());
        Equals eqUpperUserId = new Equals(USER_ID_FIELD, mdiForm.getUserId().toUpperCase());
        Or eqUserId = new Or(eqActualUserId, eqUpperUserId);
        Equals eqApprovalStatus = new Equals(APPROVAL_STATUS_FIELD, WAITING_FOR_APPROVAL);
        And eqUserIdAndEqApprovalStatus = new And(eqUserId, eqApprovalStatus);
        //        ProposalApprovalBean proposalApprovalBean;
        currentRoutingDetailsBean = new RoutingDetailsBean();
        //Get the  waiting for approval row
        CoeusVector cvFilteredData = cvEntireApprovers.filter(eqUserIdAndEqApprovalStatus);//cvExistingApprovers
        if( cvFilteredData != null && cvFilteredData.size() > 0 ){
            //Get the bean corresponding to the last index in the vector
            currentRoutingDetailsBean = (RoutingDetailsBean)cvFilteredData.get(cvFilteredData.size() - 1);
        }else{
            //This user is not waiting for approval
            enableApproveRejectButtons(false);

            //Get the bean corresponding to the first index in the vector
            if(cvEntireApprovers != null && cvEntireApprovers.size() > 0)
                currentRoutingDetailsBean = (RoutingDetailsBean)cvEntireApprovers.get(0);//or cvExistingApprovers

        }
    }

    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        try{
            mdiForm.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(btnApprove) ){
                if(isPreviousData){
                    currentSubmissionNum--;
                    getFormData();
                    if(isMapsNotFound()){
                        return;
                    }
                    setFormData();
                    enableDisableButtons();
                } else {
                    performApproveAction();
                }
                //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
                lblRecallStatus.setText(null);
                //COEUSQA:3441 - End
            }else if( source.equals(btnBypass) ){
                    performBypassAction();
            }else if( source.equals(btnReject) ){
                if(isPreviousData){
                    currentSubmissionNum++;
                    getFormData();
                    //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
                    getRecalledLabeData(routingBean.getApprovalSequence());
                    //COEUSQA:3441 - End
                    if(isMapsNotFound()){
                        return;
                    }
                    setFormData();
                    enableDisableButtons();
                } else {
                    performRejectAction();
                }
            }
            //COEUSQA-1433 - Allow Recall from Routing - Start
            else if( source.equals(btnRecall) ){
                if(isPreviousData){
                    currentSubmissionNum++;
                    getFormData();
                    if(isMapsNotFound()){
                        return;
                    }
                    setFormData();
                    enableDisableButtons();
                } else {
                    performRecallAction();
                }
            }
            else if( source.equals(btnViewComments) ){
                edu.mit.coeus.utils.CommentsForm commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
                commentsForm.setData(lblRoutingCommentsValue.getText());
                commentsForm.display();
            }
            //COEUSQA-1433 - Allow Recall from Routing - End
            else if( source.equals(btnClose) ){
                // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues  - Start
//                if(!isShowRouting()){
//                    releaseLock();
//                }
                // Commented for COEUSQA-3816 : Lite - Proposal routing - Locking issues  - End                
                close();//added by ranjeev     //dlgProposalRouting.dispose();
            }else if( source.equals(btnShowPreviousSubmission) ){
                //Modified for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
//                RoutingForm routingForm = new RoutingForm(moduleBean, moduleCode, moduleItemKey,
//                        routingBean.getModuleItemKeySequence(), unitNumber, false, routingBean.getApprovalSequence());
                RoutingForm routingForm = null;
                if(isProtocol(moduleCode)){
                    routingForm = new RoutingForm(moduleBean, moduleCode, moduleItemKey,
                            routingBean.getModuleItemKeySequence(), unitNumber , submissionNumber, hmRoutingSeqHistory);
                }
                //AWARD ROUTING ENHANCEMENT STARTS
                else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                    AwardDocumentRouteBean newAwardDocumentRouteBean=getAwardDocumentRouteBean();
                    routingForm = new RoutingForm(
                                    newAwardDocumentRouteBean,
                                    ModuleConstants.AWARD_MODULE_CODE,newAwardDocumentRouteBean.getMitAwardNumber(),
                                    newAwardDocumentRouteBean.getRoutingDocumentNumber(), unitNumber, false);
                }
                //AWARD ROUTING ENHANCEMENT ENDS
                else{
                    //COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status-Start
//                    routingForm = new RoutingForm(moduleBean, moduleCode, moduleItemKey,
//                            routingBean.getModuleItemKeySequence(), unitNumber, false, routingBean.getApprovalSequence());
                    if(((ProposalDevelopmentFormBean)moduleBean).getCreationStatusCode() == PROPOSAL_IN_POST_SUBMISSION_REJECTION_STATUS ){
                        routingForm = new RoutingForm(moduleBean, moduleCode, moduleItemKey,
                            routingBean.getModuleItemKeySequence(), unitNumber, true, routingBean.getApprovalSequence());
                    }else{
                        routingForm = new RoutingForm(moduleBean, moduleCode, moduleItemKey,
                            routingBean.getModuleItemKeySequence(), unitNumber, false, routingBean.getApprovalSequence());
                    }
                    //COEUSQA-2400_CLONE_Proposal dev-routing window does not open when proposal is in Post Submission rejection status-End
                }
                //COEUSQA-2249 : End
                routingForm.setParentProposal(true);
                routingForm.display();
            }
            //COEUSQA:1699 - Add Approver Role - Start
            else if( source.equals(btnAddApprover) ){
                performAddApproverAction();
            }
            else if( source.equals(btnAlternative) ){
                 performAddAlternateAction();
            }
            //COEUSQA:1699 - End

            // JM 2-18-2013 add reopen for approval button
            else if (source.equals(btnReopenAddApprover)) {
            	performReopenAddApprover();
            }
            // JM END
            
        } finally{
            mdiForm.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /** Method to enable/disable Approve and Reject buttons when showRouting is selected
     * @param enable true if set, false otherwise
     */
    public void enableApproveRejectButtons(boolean enable){
        if( enable ){
            btnApprove.setEnabled(true);
            btnReject.setEnabled(true);
            //Code commented for Case#2785 - Routing Enhancement bypass bug fix
//            btnBypass.setEnabled(true);
        }else{
            btnApprove.setEnabled(false);
            btnReject.setEnabled(false);
            //Code commented for Case#2785 - Routing Enhancement bypass bug fix
//            btnBypass.setEnabled(false);
        }
    }

    // JM 2-18-2013 reopen proposal and add user
    private boolean hasReopenRight() {
    	boolean hasRight = false;
    	edu.vanderbilt.coeus.utils.UserPermissions perm = new edu.vanderbilt.coeus.utils.UserPermissions(loggedInUser);
    	try {
			hasRight = perm.hasRight("REOPEN_FOR_APPROVAL");
		} catch (CoeusClientException e) {
			System.out.println("Unable to verify user permissions");
			e.printStackTrace();
		}
		return hasRight;
    }
    
    private void performReopenAddApprover() {
    	System.out.println("Reopening proposal " + routingBean.getModuleItemKey() + " for approval");
    	if (hasReopenRight()) {
    		edu.vanderbilt.coeus.utils.CustomFunctions doReopen = new edu.vanderbilt.coeus.utils.CustomFunctions();
    		try {
				Vector retVector = doReopen.reopenProposalForApproval(routingBean.getModuleItemKey());
				Integer retVal = (Integer) retVector.get(0);
				String message = (String) retVector.get(1);
	            CoeusOptionPane.showInfoDialog(message);
	            if (retVal != -1) {
		            getFormData();
		            setFormData();
	            	enableApproveRejectButtons(true);
	            	btnReopenAddApprover.setEnabled(false);
    			}
			} catch (CoeusClientException e) {
				System.out.println("Could not complete reopening of proposal.");
				e.printStackTrace();
			}
    	}
    }
    // JM END

    /** Allows the user to approve the proposal */
    private void performApproveAction(){

        RequesterBean requester = null;
        routingApprovalForm = new edu.mit.coeus.routing.gui.RoutingApprovalForm(mdiForm, moduleCode, moduleItemKey, true);
        routingApprovalForm.setParentProposal(isParentProposal());
        routingApprovalForm.setModuleBean(moduleBean);
        routingApprovalForm.setHierarchy(isHierarchy());
        //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
        //        routingApprovalForm.setCurrentApprovalBean(currentRoutingDetailsBean);!!!!!!!!!!!!!!!!!!UNCOMMENT!!!!!!!!!!!!
        
        /* JM 9-25-2015 added to get table row clicked on */
        int selectedRow = tblProposalRouting.getSelectedRow();
        if( selectedRow < 0 ){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(NO_CURRENT_STOP));
            return;
        }
        else {
        	//Approval status of the selected row should be waiting for approval
        	currentRoutingDetailsBean = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);
        	
        	// FOR DEBUG
            //System.out.println("Currently selected routing details :: map = " + 
            //		currentRoutingDetailsBean.getMapNumber() + ", level = " +
            //		currentRoutingDetailsBean.getLevelNumber() + ", stop = " +
            //		currentRoutingDetailsBean.getStopNumber() + ", user = " +
            //		currentRoutingDetailsBean.getUserId());

	        if( !currentRoutingDetailsBean.getApprovalStatus().equals(WAITING_FOR_APPROVAL) ){
	            CoeusOptionPane.showInfoDialog(
	                    coeusMessageResources.parseMessageKey(STOP_NOT_WAITING_FOR_APPROVAL));
	            return;
	        }
        }
        /* JM END */
        
        currentRoutingDetailsBean.setComments(null);
        currentRoutingDetailsBean.setAttachments(null);
        routingApprovalForm.setCurrentRoutingDetailsBean(currentRoutingDetailsBean);
        routingApprovalForm.setObservable(observable);
        routingApprovalForm.setFormData(cvExistingApprovers);
        routingApprovalForm.setRoutingBean(routingBean);
        
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - Start
//        boolean approveClicked = routingApprovalForm.display();
        boolean isLockExist = lockRouting();
        if(!isLockExist){
            return;
        }
        boolean approveClicked = routingApprovalForm.display();
        releaseUpdateLock();
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - End
        sponsorMenuEnabled = routingApprovalForm.isSponsorMenuEnabled();
        
        if( approveClicked ){
            //release lock
            // releaseUpdateLock();
            observable.notifyObservers(routingApprovalForm.getModuleBean());
            enableApproveRejectButtons(false);
            routingApprovalForm.setApprovalActionPerformed(false);
        }
        if(isProtocol(moduleCode)){
            setSubmissionStatusCode(routingApprovalForm.getSubmissionStatusCode());
        }
        getFormData();
        setFormData();

    }

                        /** Allows the user to bypass the proposal */
                        private void performBypassAction(){
                            routingApprovalForm = new edu.mit.coeus.routing.gui.RoutingApprovalForm(mdiForm, moduleCode, moduleItemKey, true);
                            routingApprovalForm.setParentProposal(isParentProposal());
                            //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-start
                            routingApprovalForm.setHierarchy(isHierarchy());
                            //Added for the case# COEUSQA-1679- Modification for Final Document Indicator-end
                            routingApprovalForm.setModuleBean(moduleBean);
                            int selectedRow = tblProposalRouting.getSelectedRow();
                            if( selectedRow < 0 ){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(NO_CURRENT_STOP));
                                return;
                            }else{
                            //Approval status of the selected row should be waiting for approval
                            //if(selectedRow > 0)
                            //selectedRow -= 1;
                            //currentRoutingDetailsBean = (ProposalApprovalBean)cvExistingApprovers.get(selectedRow);
                            currentRoutingDetailsBean = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);


                            if( !currentRoutingDetailsBean.getApprovalStatus().equals(WAITING_FOR_APPROVAL) ){
                                CoeusOptionPane.showInfoDialog(
                                        coeusMessageResources.parseMessageKey(STOP_NOT_WAITING_FOR_APPROVAL));
                                return;
                            }
                        }
                        Integer moduleCode= routingBean.getModuleCode();
                        String moduleItemKey=routingBean.getModuleItemKey();
//                              try {
//                                boolean b = getApproverCountForLocking();

//                             if(b){
//                            //String proposalNumber =((ProposalDevelopmentFormBean)moduleBean).getProposalNumber();
//                            Vector dataObjects = new Vector();
//                            String connectTo = CONNECTION_STRING ;
//                            // connect to the database and get the formData for the given organization id
//                               RequesterBean  requesterBean = new RequesterBean();
//                              requesterBean.setFunctionType(LOCKINROUTING );
//                               requesterBean.setId( moduleItemKey );
//                               dataObjects.add(unitNumber);
//                               dataObjects.add((String)mdiForm.getUserId());
//                               dataObjects.add(moduleCode);
//                               requesterBean.setDataObjects(dataObjects);
//                                AppletServletCommunicator comm
//                                = new AppletServletCommunicator( connectTo, requesterBean );
//                                comm.send();
//                                ResponderBean response = comm.getResponse();
//                                if (response == null) {
//                                    response = new ResponderBean();
//                                    response.setResponseStatus(false);
//                                    response.setMessage(coeusMessageResources.parseMessageKey(
//                                    "server_exceptionCode.1000"));
//                                   }
//                        if (response.isSuccessfulResponse()) {
//                            dataObjects = response.getDataObjects();
//                            //Code Added by Vyjayanthi on 27/01/2004 to check for rights - Start
//                           // hasSubmitForApprovalRt = ((Boolean)dataObjects.get(9)).booleanValue();
//                            //hasSubmitToSponsorRt = ((Boolean)dataObjects.get(10)).booleanValue();
//                            //hasAddViewerRt = ((Boolean)dataObjects.get(11)).booleanValue();
//                            //Code Added by Vyjayanthi on 27/01/2004 to check for rights - End
//                        } else {
//                            if (response.isLocked()) {
//                            /* the row is being locked by some other user
//                             */
//                                setModifiable(false);
//                                //Modified for case# 3439 to include the locking message - start
//                                //throw new Exception("proposal_row_clocked_exceptionCode.777777");
//                                coeusMessageResources = CoeusMessageResources.getInstance();
//                                String msg = "";
//                                if(response.getMessage()!=null){
//                                    msg = msg + response.getMessage().trim() + ". ";
//                                }
//
//                                 CoeusOptionPane.showWarningDialog( msg);
////                                msg =  msg + coeusMessageResources.parseMessageKey(
////                                    "proposal_row_clocked_exceptionCode.777777");
//                               // throw new Exception(msg);
////                                 int resultConfirm = CoeusOptionPane.showQuestionDialog(msg,
////                                    CoeusOptionPane.OPTION_YES_NO,
////                                    CoeusOptionPane.DEFAULT_YES);
////                                    //Modified for case# 3439to include the locking message - end
////                                    if (resultConfirm == 0) {
////                                 //for locking
////
////                                        routingApprovalForm.DisableRoutingApprovalForm();
////                                        routingApprovalForm.display();
////                                        }
//
//                                }else {
//
//                            routingApprovalForm.setCurrentRoutingDetailsBean(currentRoutingDetailsBean);
//                            //routingApprovalForm.setObservable(observable);
//                            routingApprovalForm.setFormData(cvExistingApprovers);
//                            routingApprovalForm.setRoutingBean(routingBean);
//                    ////        routingApprovalForm = new RoutingApprovalForm(parent, modal);!!!!!!!!!!!!!!UNCOMMENT!!!!!!!!!!!!!!!!!!!!!!!!!!
//                    //        routingApprovalForm.setModuleBean(proposalDevelopmentFormBean);
//                    ////        routingApprovalForm.setCurrentApprovalBean(currentRoutingDetailsBean);!!!!!!!!!!!!!!!!UNCOMMENT
//                    //        routingApprovalForm.setFormData(cvExistingApprovers);
//                    //        //To set the appropriate buttons for Bypass action and remove all Approve buttons
//                            routingApprovalForm.formatButtons(true);
//                            boolean bypassClicked = routingApprovalForm.display();
//                            if(isProtocol(moduleCode)){
//                                setSubmissionStatusCode(routingApprovalForm.getSubmissionStatusCode());
//                            }
//                            if( bypassClicked ){
//                                 releaseUpdateLock();
//                                observable.notifyObservers(routingApprovalForm.getModuleBean());
//                                enableApproveRejectButtons(false);
//                                getFormData();
//                                createTree();
//                                cvApproversData = addSequentialStops(cvExistingApprovers);
//                                tblProposalRouting.setTableData(cvApproversData);
//                                }
//                                if(currentRoutingDetailsBean != null){
//                                    currentRoutingDetailsBean.setComments(null);
//                                    currentRoutingDetailsBean.setAttachments(null);
//                                }
//                                setTreeNodeSelection();
//                             }
//                           }
//                        }
//                            else{
                             routingApprovalForm.setCurrentRoutingDetailsBean(currentRoutingDetailsBean);
                            //routingApprovalForm.setObservable(observable);
                            routingApprovalForm.setFormData(cvExistingApprovers);
                            routingApprovalForm.setRoutingBean(routingBean);
                    ////        routingApprovalForm = new RoutingApprovalForm(parent, modal);!!!!!!!!!!!!!!UNCOMMENT!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //        routingApprovalForm.setModuleBean(proposalDevelopmentFormBean);
                    ////        routingApprovalForm.setCurrentApprovalBean(currentRoutingDetailsBean);!!!!!!!!!!!!!!!!UNCOMMENT
                    //        routingApprovalForm.setFormData(cvExistingApprovers);
                    //        //To set the appropriate buttons for Bypass action and remove all Approve buttons
                            routingApprovalForm.formatButtons(true);
                            // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - Start
                    //        boolean bypassClicked = routingApprovalForm.display();
                            boolean isLockExist = lockRouting();
                            if(!isLockExist){
                                return;
                            }
                            boolean bypassClicked = routingApprovalForm.display();
                            releaseUpdateLock();
                            // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - End

                            if(isProtocol(moduleCode)){
                                setSubmissionStatusCode(routingApprovalForm.getSubmissionStatusCode());
                             }
                            if( bypassClicked ){
                                // releaseUpdateLock();
                                observable.notifyObservers(routingApprovalForm.getModuleBean());
                                enableApproveRejectButtons(false);
                                getFormData();
                                createTree();
                                cvApproversData = addSequentialStops(cvExistingApprovers);
                                tblProposalRouting.setTableData(cvApproversData);
                                }
                                if(currentRoutingDetailsBean != null){
                                    currentRoutingDetailsBean.setComments(null);
                                    currentRoutingDetailsBean.setAttachments(null);
                                }
                            setTreeNodeSelection();
//                          }

//                       }   catch (Exception ex) {}
                     }                                      //else{


    /** Displays the ProposalRejectionForm and allows the user to Reject the proposal */
    private void performRejectAction(){
//        proposalRejectionForm = new ProposalRejectionForm(parent, modal);!!!!!!!!!!!!!!UNCOMMENT!!!!!!!!!!!!!!!!!!!!!!!!!!
//        proposalRejectionForm.setCurrentApprovalBean(currentRoutingDetailsBean);!!!!!!!!!!!!!!!!!UNCOMMENT!!!!!!!!!!!!!!!!
        routingRejectionForm = new RoutingRejectionForm(mdiForm, moduleCode, moduleItemKey, true);
        currentRoutingDetailsBean.setComments(null);
        currentRoutingDetailsBean.setAttachments(null);
        //coeus 2111 starts
       routingRejectionForm.setModuleBean(moduleBean);
       //coeus 2111 ends
        Integer moduleCode= routingBean.getModuleCode();
        String moduleItemKey=routingBean.getModuleItemKey();
        routingRejectionForm = new RoutingRejectionForm(mdiForm, moduleCode, moduleItemKey, true);
        currentRoutingDetailsBean.setComments(null);
        currentRoutingDetailsBean.setAttachments(null);
        routingRejectionForm.setCurrentApprovalBean(currentRoutingDetailsBean);
        routingRejectionForm.setRoutingBean(this.routingBean);
        routingRejectionForm.setRoutingBean(routingBean);
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - Start
//        boolean rejectClicked = routingRejectionForm.display();
        boolean isLockExist = lockRouting();
        if(!isLockExist){
            return;
        }
        boolean rejectClicked = routingRejectionForm.display();
        releaseUpdateLock();
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - End
        if( rejectClicked ){
            observable.notifyObservers(routingRejectionForm.getModuleBean());
            enableApproveRejectButtons(false);
            if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                setSubmissionStatusCode(IRB_REJECTED_SUBMISSION);
            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                setSubmissionStatusCode(IACUC_REJECTED_SUBMISSION);
            }else if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
                setModuleBean(routingRejectionForm.getModuleBean());
            }
            //code commented and modified for Case#3775 - Manually selected map disappears - ends
            setCloseRequired(true);
            //Added for COEUSQA-3391 : rejecting proposal or protocol locks the proposal or protocol - start
            //lock release after reject action
            releaseLock();
            //Added for COEUSQA-3391 : rejecting proposal or protocol locks the proposal or protocol - end
            close();
            return;
        }
        
        if(currentRoutingDetailsBean != null){
            currentRoutingDetailsBean.setComments(null);
            currentRoutingDetailsBean.setAttachments(null);
        }
        setTreeNodeSelection();
        
    }

    /** This method sets the comments based on the valueChanged of listSelectionEvent
     * @param listSelectionEvent takes the listSelectionEvent */
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if( listSelectionEvent.getValueIsAdjusting() ) return ;
        try {

            Object source = listSelectionEvent.getSource();
            int selectedRow = tblProposalRouting.getSelectedRow();
            if( (source.equals(routingSelectionModel) )&& (selectedRow >= 0 ) &&
                    (cvApproversData != null)) {
                //ProposalApprovalBean proposalApprovalBean = null;// = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);
                RoutingDetailsBean routingDetailsBean = null;// = tblProposalRouting.getSelectedProposalApprovalBean(selectedRow);
//                if(selectedRow > 0){
                    //selectedRow -= 1;
//                }
                if(cvApproversData != null && cvApproversData.size() > 0){
                    routingDetailsBean= (RoutingDetailsBean)cvApproversData.get(selectedRow);
                }
                //getApproverAttachmentsAndComments(routingDetailsBean);
                populateCommentsAndAttachments(routingDetailsBean);
//                txtArComments.setLineWrap(true);
//                txtArComments.setWrapStyleWord(true);
//                txtArComments.setEditable(false);
//                txtArComments.setText("TO BE CHANGED");

                dlgRouting.pack();
                dlgRouting.validate();

            }
        }catch(Exception exp) {
            exp.printStackTrace();
        }
    }

    public void getApproverAttachmentsAndComments(RoutingDetailsBean routingDetailsBean){
        if(routingDetailsBean!=null){
            //Create the filter criteria
            Equals eqRoutingNumber = new Equals("routingNumber", routingDetailsBean.getRoutingNumber());
            Equals eqMapNumber  = new Equals("mapNumber", String.valueOf(routingDetailsBean.getMapNumber()));
            Equals eqLevelNumber = new Equals("levelNumber", String.valueOf(routingDetailsBean.getLevelNumber()));
            Equals eqStopNumber = new Equals("stopNumber", String.valueOf(routingDetailsBean.getStopNumber()));
            Equals eqApproverNumber = new Equals("approverNumber", String.valueOf(routingDetailsBean.getApproverNumber()));

            And andRoutingAndMapNumber = new And(eqRoutingNumber, eqMapNumber);
            And andLevelAndStopNumber = new And(eqLevelNumber, eqStopNumber);
            And andCriteria = new And(new And(andRoutingAndMapNumber, andLevelAndStopNumber), eqApproverNumber);

            //Filter the vector and set it to the bean
            CoeusVector cvApproverAttachments = cvAttachments.filter(andCriteria);
            CoeusVector cvApproverComments = cvComments.filter(andCriteria);
            routingDetailsBean.setComments(cvApproverComments);
            routingDetailsBean.setAttachments(cvApproverAttachments);
        }
    }
    public void viewAttachment(){
        int selectedRow = tblAttachments.getSelectedRow();
        if( selectedRow != -1){
            RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)cvModifiedAttachments.get(selectedRow);
            DocumentBean documentBean = new DocumentBean();
            Map map = new HashMap();
            map.put("DATA", routingAttachmentBean);
            map.put("USER_ID", CoeusGuiConstants.getMDIForm().getUserId());
            map.put("FUNCTION_TYPE", "GET_ROUTING_ATTACHMENT");
            map.put(DocumentConstants.DOC_ON_URL_GENERATION, new Boolean(true));
            map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.routing.RoutingAttachmentsReader");
            documentBean.setParameterMap(map);
            RequesterBean requester = new RequesterBean();
            requester.setDataObject(documentBean);
            requester.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
            //For Streaming
            String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";

            AppletServletCommunicator comm
                    = new AppletServletCommunicator(STREAMING_SERVLET, requester);
            comm.send();

            ResponderBean response = comm.getResponse();
            if(response.isSuccessfulResponse()){
                map = (Map)response.getDataObject();
                String reportUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);
                reportUrl = reportUrl.replace('\\', '/') ;
                try {
                    URL urlObj = new URL(reportUrl);
                    URLOpener.openUrl(urlObj);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (CoeusException ex) {
                    ex.printStackTrace();
                }
            }else {
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }
    }

    public void viewComment(){
        int selectedRow = tblComments.getSelectedRow();
        if(selectedRow!=-1){
            if(cvComments!=null && cvComments.get(selectedRow)!=null){
                RoutingCommentsBean routingCommentsBean = (RoutingCommentsBean)cvComments.get(selectedRow);
                if(routingCommentsBean!=null){
                    edu.mit.coeus.utils.CommentsForm commentsForm = new edu.mit.coeus.utils.CommentsForm("Comments");
                    commentsForm.setData(routingCommentsBean.getComments());
                    commentsForm.display();
                }
            }
        }
    }

    /**
     * Populate the comments and attachments
     *
     * @param routingDetailsBean object of RoutingDetailBean set
     *      with the comments and attachments and comments
     */
    public void populateCommentsAndAttachments(RoutingDetailsBean routingDetailsBean){
        if(commentsTableModel==null){
            commentsTableModel = new CommentsTableModel();
        }

        if(attachmentTableModel == null){
            attachmentTableModel = new AttachmentsTableModel();
        }
        cvCommentsData = routingDetailsBean.getComments();
        cvModifiedAttachments = routingDetailsBean.getAttachments();
        commentsTableModel.setData(routingDetailsBean.getComments());
        attachmentTableModel.setData(routingDetailsBean.getAttachments());
        if(routingDetailsBean.getAttachments()!=null && routingDetailsBean.getAttachments().size()>0){
            tbdPnAttmntAndComment.setIconAt(1,
                    new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DETAILS_ICON)));
        }else{
            tbdPnAttmntAndComment.setIconAt(1, null);
        }

        commentsTableModel.fireTableDataChanged();
        attachmentTableModel.fireTableDataChanged();
    }

    /** Method to get all the beans from the server */
    private void getFormData(){
        //Get the user data into cvExistingApprovers and tree data into cvTreeView
        try {
            isRoutingDataAvailable = false;
            Vector vecRequestParameters = new Vector();
            //Added for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved
            String protoApprovalSeq = CoeusGuiConstants.EMPTY_STRING;
            //COEUSQA-2249 : End
            vecRequestParameters.add(0, String.valueOf(moduleCode));
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                    vecRequestParameters.add(1, ((ProposalDevelopmentFormBean)moduleBean).getProposalNumber());
                    vecRequestParameters.add(2, "0");
                }
            }//AWARD ROUTING ENHANCEMENT STARTS
           else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                   vecRequestParameters.add(1, routingBean.getModuleItemKey());
                   vecRequestParameters.add(2, String.valueOf(routingBean.getModuleItemKeySequence()) );
             
                }
            //AWARD ROUTING ENHANCEMENT ENDS
            else if(isProtocol(moduleCode)){
                    //Modified for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved - Start
                    //If the routing is opened during submission original protocol numbar and seqeunce number is used to get the details
                    String protocolNumber,sequenceNumber;
                    if(moduleBean!=null && this.isModuleSubmission){
                        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                            protocolNumber = ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
                            sequenceNumber = String.valueOf(((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                            vecRequestParameters.add(1, protocolNumber );
                        vecRequestParameters.add(2, sequenceNumber );
                        }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                            protocolNumber = ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
                            sequenceNumber = String.valueOf(((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                            vecRequestParameters.add(1, protocolNumber );
                            vecRequestParameters.add(2, sequenceNumber );
                        }
                    }else if(hmRoutingSeqHistory != null && hmRoutingSeqHistory.size() > 0){
                        CoeusVector cvApprovalSequence = (CoeusVector)hmRoutingSeqHistory.get(new Integer(currentSubmissionNum));
                        protocolNumber = (String)cvApprovalSequence.get(MODULE_ITEM_NUMBER_INDEX);
                        sequenceNumber = (String)cvApprovalSequence.get(MODULE_ITEM_KEY_SEQUENCE_INDEX);
                        protoApprovalSeq = (String)cvApprovalSequence.get(APPROVAL_SEQUENCE_INDEX);
                        vecRequestParameters.add(1, protocolNumber);
                        vecRequestParameters.add(2, sequenceNumber);
                    }
            }
            vecRequestParameters.add(3, unitNumber);
            vecRequestParameters.add(4, new Boolean(buildMapRequired));
            vecRequestParameters.add(5, BUILD_MAPS_OPTION_PARAM);
            //Modified for COEUSQA-2249  : Routing history is lost after an amendment is routed and approved
//            if(isPreviousData){
//                vecRequestParameters.add(6, new Integer(currentSubmissionNum));
//            } else {
//                vecRequestParameters.add(6, null);
//            }
            if(!this.isModuleSubmission && isProtocol(moduleCode)){
                vecRequestParameters.add(6, new Integer(protoApprovalSeq));
            }
            //coeus 2111 starts
            else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                vecRequestParameters.add(6,currentSubmissionNum );
            }
                    //coeus 2111 ends
            else{
                if(isPreviousData){
                    vecRequestParameters.add(6, new Integer(currentSubmissionNum));
                } else {
                    vecRequestParameters.add(6, null);
                }
            }
            //COEUSQA-2249 : End
            cvExistingApprovers = new CoeusVector();
            cvRoutingMapBeans = new CoeusVector();
            cvTreeView = new CoeusVector();
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_ROUTING_DATA); //GET_PROP_ROUTING_DATA
            requester.setDataObjects(vecRequestParameters);

            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector vecData = new Vector();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    vecData = response.getDataObjects();
                    //modified by Ranjeev

                    try {

                        if(vecData != null && vecData .size() > 0) {
                            int buildMapID = 0;
                            if(vecData.get(0).getClass() == Integer.class) {
                                buildMapID = ((Integer) vecData.get(0)).intValue();
                                if(buildMapID < 1){
                                    return;
                                }
                            }

                            if(vecData.get(1) != null && vecData.get(1).getClass() == CoeusVector.class) {
                                cvRoutingMapBeans = (CoeusVector)vecData.get(1);
                                //Set the approvers
                                cvEntireApprovers = new CoeusVector();
                                for( int index = 0; index < cvRoutingMapBeans.size(); index++ ){
                                    RoutingMapBean routingMapBean =
                                            (RoutingMapBean)cvRoutingMapBeans.get(index);
                                    if(routingMapBean.getRoutingMapDetails() != null)
                                        cvEntireApprovers.addAll(routingMapBean.getRoutingMapDetails());
                                }
                                this.routingMapBean = (RoutingMapBean)cvRoutingMapBeans.get(0);
                                cvExistingApprovers = this.routingMapBean.getRoutingMapDetails();
                            }

                            if(vecData.get(2).getClass() == Boolean.class) {
                                //  Checking the enable bypass buuton if user has right
                                //  and there are some stops Waiting for approval

                                if(((Boolean) vecData.get(2)).booleanValue()) {
                                    //if(cvExistingApprovers != null && cvExistingApprovers.size() > 0)
                                    if(cvEntireApprovers != null && cvEntireApprovers.size() > 0){
                                        isByPassFlag = isAnyWaitingState(cvEntireApprovers);//cvExistingApprovers
                                    }
                                }

                            }
                            if(vecData.get(3)!=null && vecData.get(3).getClass() == CoeusVector.class){
                                cvAttachments = (CoeusVector)vecData.get(3);
                            }

                            if(vecData.get(4)!=null && vecData.get(4).getClass() == RoutingBean.class){
                                routingBean = (RoutingBean)vecData.get(4);
                            } else {
                                if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(AWARD_DOC_NO_MAPS));
                                    }
                                else{
                                    CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SUNMIT_FAILED_NO_MAPS));
                                }
                                    setMapsNotFound(true);
                                    close();
                                    return;
                            }

                            loggedInUser = "";
                            if(vecData.get(5)!=null && vecData.get(5).getClass() == String.class){
                                loggedInUser = (String)vecData.get(5);
                            }
                            //COEUSQA:1699 - Add Approver Role - Start
                             if(vecData.get(6).getClass() == Boolean.class) {
                                // If the logged in user has assigned ADD_APPROVER right, enable the Add Approver and Alternate buttons
                                if(((Boolean) vecData.get(6)).booleanValue()) {
                                   btnAddApprover.setVisible(true);
                                   btnAlternative.setVisible(true);
                                }
                                else{
                                   btnAddApprover.setVisible(false);
                                   btnAlternative.setVisible(false);
                                   /* JM 9-23-2015 commented out so that panel won't resize weirdly */
                                   //pnlRoutingButtons.setMinimumSize(new java.awt.Dimension(110, 140));
                                   //pnlRoutingButtons.setPreferredSize(new java.awt.Dimension(110, 140));
                                   /* JM END */
                                }
                            }
                            //COEUSQA:1699 - End

                            isRoutingDataAvailable = true;
                        }

                    }catch(Exception exp) {
                        exp.printStackTrace();
                    }

                }
            }

        }catch(Exception exp) {
            exp.printStackTrace();
        }


    }

    /**
     * Method to Check any Waiting For approval State in the Vector of vecProposalApprovalMap
     */
    private boolean  isAnyWaitingState(CoeusVector vecProposalApprovalMap) {
        Equals equalsWait = new Equals("approvalStatus", WAITING_FOR_APPROVAL);
        CoeusVector vecFilteredMap  = vecProposalApprovalMap.filter(equalsWait);
        if(vecFilteredMap != null && vecFilteredMap.size() > 0){
            return true;
        }else{
            return false;
        }
    }

    /** Displays the screen */
    public boolean display(){
        // if(isRoutingDataAvailable){
        dlgRouting.setVisible(true);
//        }else{
//            close();
//        }
        return sponsorMenuEnabled;
    }

    /** Close To Dispose the Form*/
    public void close(){
        if(isShowRouting()){
            deleteRouting();
        }
        dlgRouting.dispose();
    }


    /** Getter for property vecParameters.
     * @return Value of property vecParameters.
     *
     */
    public java.util.Vector getVecParameters() {
        return vecParameters;
    }

    /** Setter for property vecParameters.
     * @param vecParameters New value of property vecParameters.
     *
     */
    public void setVecParameters(java.util.Vector vecParameters) {
        this.vecParameters = vecParameters;
    }


    // START ===== Added by Ranjeev

    /**
     * Method to Add the Sequential Row to Table tblProposalRouting while Setting the
     * Vector of ProposalApprovalBeans. This Bean is a dunmmy bean which is checked in
     * ProposalRouting Table Renderer while rendering the rows.
     */

    public CoeusVector  addSequentialStops(CoeusVector vecRoutingDetailBeans) {
        Hashtable eachSeqlevel = new Hashtable();
        CoeusVector tempcVecProposalApprovalBean = new CoeusVector();
        String sortParameter [] = {"levelNumber","stopNumber"};
        vecRoutingDetailBeans.sort(sortParameter,true);
        RoutingDetailsBean routingDetailsBean;
        for(int index=0;index < vecRoutingDetailBeans.size();index++) {
            //ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean) vecRoutingDetailBeans.get(index);
            routingDetailsBean = (RoutingDetailsBean)vecRoutingDetailBeans.get(index);
            String levelKey = routingDetailsBean.getLevelNumber()+"";
            if(!eachSeqlevel.containsKey((Object) levelKey)) {
                eachSeqlevel.put(levelKey,levelKey);

                //ProposalApprovalBean newRoutingDetailsBean = new ProposalApprovalBean();
                RoutingDetailsBean newRoutingDetailsBean = new RoutingDetailsBean();
                //newRoutingDetailsBean.setProposalNumber(routingDetailsBean.getProposalNumber());
                newRoutingDetailsBean.setRoutingNumber(routingDetailsBean.getRoutingNumber());
                newRoutingDetailsBean.setMapNumber(routingDetailsBean.getMapNumber());
                newRoutingDetailsBean.setDescription(routingDetailsBean.getDescription());
                newRoutingDetailsBean.setLevelNumber(routingDetailsBean.getLevelNumber());
                newRoutingDetailsBean.setStopNumber(routingDetailsBean.getStopNumber());
                newRoutingDetailsBean.setUserId(routingDetailsBean.getUserId());
                newRoutingDetailsBean.setPrimaryApproverFlag(routingDetailsBean.isPrimaryApproverFlag());
                newRoutingDetailsBean.setApprovalStatus("Z");
                newRoutingDetailsBean.setApproverNumber(routingDetailsBean.getApproverNumber());
//                newRoutingDetailsBean.setComments(null);
                tempcVecProposalApprovalBean.add(tempcVecProposalApprovalBean.size(),newRoutingDetailsBean);

            }
            tempcVecProposalApprovalBean.add(routingDetailsBean);


        }
        return tempcVecProposalApprovalBean;
//        Hashtable eachSeqlevel = new Hashtable();
//        CoeusVector tempcVecProposalApprovalBean = new CoeusVector();
//        String sortParameter [] = {"levelNumber","stopNumber"};
//        vecRoutingDetailBeans.sort(sortParameter,true);
//        for(int index=0;index < vecRoutingDetailBeans.size();index++) {
//
//            ProposalApprovalBean proposalApprovalBean = (ProposalApprovalBean) vecRoutingDetailBeans.get(index);
//            String levelKey = proposalApprovalBean.getLevelNumber()+"";
//            if(!eachSeqlevel.containsKey((Object) levelKey)) {
//                eachSeqlevel.put(levelKey,levelKey);
//
//                ProposalApprovalBean newRoutingDetailsBean = new ProposalApprovalBean();
//                newRoutingDetailsBean.setProposalNumber(proposalApprovalBean.getProposalNumber());
//                newRoutingDetailsBean.setMapId(proposalApprovalBean.getLevelNumber());
//                newRoutingDetailsBean.setDescription(proposalApprovalBean.getDescription());
//                newRoutingDetailsBean.setLevelNumber(proposalApprovalBean.getLevelNumber());
//                newRoutingDetailsBean.setStopNumber(proposalApprovalBean.getStopNumber());
//                newRoutingDetailsBean.setUserId(proposalApprovalBean.getUserId());
//                newRoutingDetailsBean.setPrimaryApproverFlag(proposalApprovalBean.isPrimaryApproverFlag());
//                newRoutingDetailsBean.setApprovalStatus("Z");
//                newRoutingDetailsBean.setComments(null);
//                tempcVecProposalApprovalBean.add(tempcVecProposalApprovalBean.size(),newRoutingDetailsBean);
//
//            }
//            tempcVecProposalApprovalBean.add(proposalApprovalBean);
//
//
//        }
//        return tempcVecProposalApprovalBean;

    }

    /**
     * Method used to expand/ collapse all the nodes in the tree.
     * @param tree JTree whose nodes are to be expanded/ collapsed.
     * @param expand  boolean true to expand all nodes, false to collapse.
     */
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
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

    public void setModifiable(boolean modifiable){
        this.modifiable = modifiable;
    }

    /** Represent the Node of the Tree that Contains the ProposalApprovalMapBean  for each Node*/
//    class RoutingMapBeanNode extends javax.swing.tree.DefaultMutableTreeNode {
//
//        private ProposalApprovalMapBean proposalApprovalMapBean;
//        /** Creates a new instance of RoutingMapBeanNode */
//        public RoutingMapBeanNode() {
//        }
//        public RoutingMapBeanNode(ProposalApprovalMapBean proposalApprovalMapBean) {
//            super(proposalApprovalMapBean);
//            this.proposalApprovalMapBean = proposalApprovalMapBean;
//        }
//        public void setDataObject(ProposalApprovalMapBean proposalApprovalMapBean){
//            this.proposalApprovalMapBean =proposalApprovalMapBean;
//        }
//
//        public ProposalApprovalMapBean getDataObject(){
//            return proposalApprovalMapBean;
//        }
//    }
    class RoutingMapBeanNode extends javax.swing.tree.DefaultMutableTreeNode {

        private RoutingMapBean routingMapBean;
        /**
         * Creates a new instance of RoutingMapBeanNode
         */
        public RoutingMapBeanNode() {
        }
        public RoutingMapBeanNode(RoutingMapBean routingMapBean) {
            super(routingMapBean);
            this.routingMapBean = routingMapBean;
        }
        public void setDataObject(RoutingMapBean routingMapBean){
            this.routingMapBean =routingMapBean;
        }

        public RoutingMapBean getDataObject(){
            return routingMapBean;
        }
    }
    /** Tree Node Renderer for the Tree treeSequentialStop */

    class TreeNodeRenderer extends DefaultTreeCellRenderer{
        //Code added for Case#3612 - Parallel Routing and Show Routing implementation
        ImageIcon imgVerifyIcon,imageChildIcon,imgApproveIcon,imgPassIcon,imgRejectIcon,rootIcon;

        TreeNodeRenderer() {

            java.net.URL imageURLVerifyIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.VERIFY_ICON_PATH);
            java.net.URL imageURLChildIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.CHILD_TREE_NODE );
            java.net.URL imageURLApproveIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.APPROVE_ICON_PATH );

            java.net.URL imageURLPassIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.PASS_ICON_PATH  );
            java.net.URL imageURLRejectIcon = getClass().getClassLoader().getResource( CoeusGuiConstants.REJECT_ICON_PATH);
            java.net.URL rootNode = getClass().getClassLoader().getResource( CoeusGuiConstants.RULE_PARENT_NODE );

            imgVerifyIcon = new ImageIcon(imageURLVerifyIcon);
            imageChildIcon = new ImageIcon(imageURLChildIcon);
            imgApproveIcon = new ImageIcon(imageURLApproveIcon);

            imgPassIcon = new ImageIcon(imageURLPassIcon );
            imgRejectIcon = new ImageIcon(imageURLRejectIcon);
            rootIcon = new ImageIcon(rootNode);


        }

        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean selected,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {




            super.getTreeCellRendererComponent(tree, value,selected, expanded, leaf, row, hasFocus);
            Object obj = ((RoutingMapBeanNode )value).getDataObject();


//            if( obj instanceof ProposalApprovalMapBean  ){
            if( obj instanceof RoutingMapBean  ){

//                ProposalApprovalMapBean proposalApprovalMapBean = (ProposalApprovalMapBean) obj;
                RoutingMapBean proposalApprovalMapBean = (RoutingMapBean) obj;
                //                QUERY_CONSTANTS = "T";
                //                PASSED_CONSTANTS = "P";
                //                APPROVE_CONSTANTS = "A";
                //                REJECTED_CONSTANTS = "R";



                setIconTextGap(7);
                setFont(CoeusFontFactory.getNormalFont());

                setIcon(imgVerifyIcon);
                if(row > 0) {
                    //Check with the status of the ProposalApprovalMapBean
                    if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(TO_BE_SUBMITTED) ){
                        setIcon(imgVerifyIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(APPROVED) ){
                        setIcon(imgApproveIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase(REJECTED) ){
                        setIcon(imgRejectIcon);
                    }else if( proposalApprovalMapBean.getApprovalStatus().equalsIgnoreCase("P") ){
                        setIcon(imageChildIcon);
                    }
                    //Commented the following used for checking with the vector
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), APPROVED))
//                        setIcon(imgApproveIcon);
//
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), BYPASSED))
//                        setIcon(imgApproveIcon);
//
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), TO_BE_SUBMITTED))
//                        setIcon(imgVerifyIcon);
//
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), WAITING_FOR_APPROVAL))
//                        setIcon(imageChildIcon);
//
//                    if(isMatchingState(proposalApprovalMapBean.getProposalApprovals(), REJECTED))
//                        setIcon(imgRejectIcon);
                //Case#3612 - Parallel Routing and Show Routing implementation - starts
                //To show the root node.
                } else {
                    setIcon(rootIcon);
                }
                //Case#3612 - Parallel Routing and Show Routing implementation - ends
                setText(proposalApprovalMapBean.getDescription());
            }

            setBackgroundNonSelectionColor((java.awt.Color) javax.swing.UIManager.getDefaults().get("Panel.background"));
            return this;

        }
    }


    // END===== Added by Ranjeev
    //public get
    class CommentsTableModel extends AbstractTableModel{
        String colNames[] = {"", "."};
        Class[] colTypes = new Class [] {String.class, Boolean.class};
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(col == MORE_COMMENTS_COLUMN){
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
        /* Sets the value in the cell*/
        public void setValueAt(Object value, int row, int col){

        }
    }
    class AttachmentsTableModel extends AbstractTableModel{
        String colNames[] = {"", "Description"};
        Class[] colTypes = new Class [] {Boolean.class, String.class};
        CoeusVector cvAttachmentsData;
        /* If the cell is editable,return true else return false*/
        public boolean isCellEditable(int row, int col){
            if(col==VIEW_PDF){
                return true;
            } else{
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
            if(column==PDF_DESCRIPTION){
                return routingAttachmentBean.getDescription();
            }
// # case 3855 start
             else if(column ==2) {
                //Modified with case 4007:Icon based on mime Type
//                return routingAttachmentBean.getFileName();
                return routingAttachmentBean;
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

    class AttachmentCellRenderer extends DefaultTableCellRenderer{
        private JButton btnDetails;

        public AttachmentCellRenderer(){
            btnDetails = new JButton();
// Commented for case #3855
          //  btnDetails.setIcon(iIcnPdf);
        }
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col){
            if(col==VIEW_PDF){
                //Modified with case 4007:Icon based on mime Type : Start
                // # Case 3855 ---- start added attachment specific icon
//                  String fileExtension = UserUtils.getFileExtension((String)table.getModel().getValueAt(row,2));
//                  btnDetails.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                // # Case 3855 ---- end
                CoeusAttachmentBean attachment = (CoeusAttachmentBean)table.getModel().getValueAt(row,2);
                CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                btnDetails.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                //4007 End
                return btnDetails;
            }
            return null;
        }
    }

    class AttachmentCellEditor extends AbstractCellEditor implements TableCellEditor,
            ActionListener{
        private JButton btnDetails;
        private int column;
        private JTable table;
        /* Creates a CostSharing Editor*/
        public AttachmentCellEditor() {
            btnDetails = new JButton();
        // Commented for # case 3855
        //    btnDetails.setIcon(iIcnPdf);
            btnDetails.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e){
            if(e.getSource().equals(btnDetails)){
                viewAttachment();
                tblAttachments.getCellEditor().stopCellEditing();
            }
        }

        /* Returns the CellEditor value*/
        public Object getCellEditorValue() {
            if(column == 0){
                return btnDetails;
            }
            return "";
        }


        /* returns the cellEditor component*/
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.column = column;
            this.table = table;
            if(column == 0){
                //Modified with case 4007:Icon based on mime Type : Start
                // # Case 3855 start added attachment specific icon
//                String fileExtension = UserUtils.getFileExtension((String)table.getModel().getValueAt(row,2));
//                btnDetails.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                // # Case 3855 end
                CoeusAttachmentBean attachment = (CoeusAttachmentBean)table.getModel().getValueAt(row,2);
                CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                btnDetails.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                //4007 End
                return btnDetails;
            }
            return null;
        }
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


        //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
        class AppCommentsEditor extends DefaultCellEditor implements ActionListener{

            private JButton btnComments;
            private String comments;
            private ImageIcon imgIcnDesc;

            AppCommentsEditor() {
                super(new JComboBox());
                imgIcnDesc = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.JUSTIFIED));
                btnComments = new JButton();
                btnComments.setIcon(imgIcnDesc);
                btnComments.setOpaque(true);
                btnComments.addActionListener(this);
            }

            public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
                comments = (String) jTable.getValueAt(row,0);
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
        //COEUSQA:1445 - End

        private void enableDisableButtons(){
            btnShowPreviousSubmission.setVisible(false);
            btnBypass.setVisible(false);
            btnApprove.setText("Previous");
            btnReject.setText("Next");
            btnApprove.setEnabled(true);
            btnReject.setEnabled(true);
            if(currentSubmissionNum == 1){
                btnApprove.setEnabled(false);
            }
            if(currentSubmissionNum == originalSubmissionNum){
                btnReject.setEnabled(false);
            }
            //COEUSQA-1433 - Allow Recall from Routing - Start
            btnRecall.setVisible(false);
            //COEUSQA-1433 - Allow Recall from Routing - End
            
            //COEUSQA:1699 - Add Approver Role - Start
            btnAddApprover.setVisible(false);
            btnAlternative.setVisible(false);
            //COEUSQA:1699 - End
        }

    public Object getModuleBean() {
        return moduleBean;
    }

    public void setModuleBean(Object moduleBean) {
        this.moduleBean = moduleBean;
    }

    public boolean isMapsNotFound() {
        return mapsNotFound;
    }

    public void setMapsNotFound(boolean mapsNotFound) {
        this.mapsNotFound = mapsNotFound;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }

    public void setSubmissionStatusCode(int submissionStatusCode) {
        this.submissionStatusCode = submissionStatusCode;
    }

    public boolean isShowRouting() {
        return showRouting;
    }

    public void setShowRouting(boolean showRouting) {
        this.showRouting = showRouting;
    }

    public void deleteRouting(){
        Vector vecRequestParameters = new Vector();
        vecRequestParameters.add(new Integer(moduleCode));
        vecRequestParameters.add(routingBean.getModuleItemKey());
        vecRequestParameters.add(new Integer(routingBean.getModuleItemKeySequence()));
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(DELETE_ROUTING); //GET_PROP_ROUTING_DATA
        requester.setDataObjects(vecRequestParameters);

        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!=null){
            if(!response.isSuccessfulResponse()){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(response.getMessage()));
            }
        }
    }

    /**
     * Added for Case#3775 - Manually selected map disappears
     * @return boolean
     */
    public boolean isCloseRequired() {
        return closeRequired;
    }

    /**
     * Added for Case#3775 - Manually selected map disappears
     * @param closeRequired boolean
     */
    public void setCloseRequired(boolean closeRequired) {
        this.closeRequired = closeRequired;
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
    //Added for COEUSQA-2249 : Routing history is lost after an amendment is routed and approved - Start
    /*
     * Method to get sequence and their approval sequence in a collection
     * @param moduleCode
     * @param moduleItemKey
     * @return hmRoutingSeqHistory
     */
    private Hashtable getRoutingSequenceHistory(String moduleCode, String moduleItemKey){
        RequesterBean requester = new RequesterBean();
        Vector vecModuleData = new Vector();
        Hashtable hmRoutingSeqHistory = null;
        vecModuleData.add(MODULE_CODE_INDEX,moduleCode);
        vecModuleData.add(MODULE_ITEM_KEY_INDEX,moduleItemKey);
        requester.setFunctionType(GET_ROUTING_SEQUENCE_HISTORY);
        requester.setDataObjects(vecModuleData);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if ( response != null ){
            if( response.isSuccessfulResponse() ){
                hmRoutingSeqHistory = (Hashtable)response.getDataObject();
            }else{
               CoeusOptionPane.showErrorDialog(response.getMessage());
            }
        }

        return hmRoutingSeqHistory;
    }

    /*
     * Method to set the moduleItemKeySequence and approvalSequence to the routingBean
     */
    private void setMaxSubSeqAndApprovalSeq(){
        if(hmRoutingSeqHistory != null && hmRoutingSeqHistory.size() > 0){
            CoeusVector cvApprovalSequence = (CoeusVector)hmRoutingSeqHistory
                    .get((Integer)hmRoutingSeqHistory.get("MAX_SUBMISSION_NUMBER"));
            if(cvApprovalSequence != null && cvApprovalSequence.size() > 0){
                String keySequence = (String)cvApprovalSequence.get(MODULE_ITEM_KEY_SEQUENCE_INDEX);
                String appSequence = (String)cvApprovalSequence.get(APPROVAL_SEQUENCE_INDEX);
                this.routingBean.setModuleItemKeySequence((new Integer(keySequence)).intValue());
                this.routingBean.setApprovalSequence((new Integer(appSequence)).intValue());
            }
            submissionNumber = ((Integer)hmRoutingSeqHistory.get("MAX_SUBMISSION_NUMBER")).intValue();
        }
    }
    //COEUSQA-2249 : End

    private boolean isProtocol(int moduleCode){
        boolean isProtocol = false;
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            isProtocol = true;
        }
        return isProtocol;
    }

    //COEUSQA:1445 - User Not able to View Routing Comments through Show Routing - Start
    /* To get the Comments and Attachments details for all approvers
     *
     */
    private void getCommentsAndDetails(){
        try {
            Vector vecRequestParameters = new Vector();
            String protoApprovalSeq = CoeusGuiConstants.EMPTY_STRING;
            vecRequestParameters.add(0, String.valueOf(moduleCode));
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                    vecRequestParameters.add(1, ((ProposalDevelopmentFormBean)moduleBean).getProposalNumber());
                    vecRequestParameters.add(2, "0");
                }
            }else if(isProtocol(moduleCode)){
                String protocolNumber,sequenceNumber;
                if(moduleBean!=null && this.isModuleSubmission){
                    if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                        protocolNumber = ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
                        sequenceNumber = String.valueOf(((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                        vecRequestParameters.add(1, protocolNumber );
                        vecRequestParameters.add(2, sequenceNumber );
                    }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                        protocolNumber = ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
                        sequenceNumber = String.valueOf(((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                        vecRequestParameters.add(1, protocolNumber );
                        vecRequestParameters.add(2, sequenceNumber );
                    }
                }else if(hmRoutingSeqHistory != null && hmRoutingSeqHistory.size() > 0){
                    CoeusVector cvApprovalSequence = (CoeusVector)hmRoutingSeqHistory.get(new Integer(currentSubmissionNum));
                    protocolNumber = (String)cvApprovalSequence.get(MODULE_ITEM_NUMBER_INDEX);
                    sequenceNumber = (String)cvApprovalSequence.get(MODULE_ITEM_KEY_SEQUENCE_INDEX);
                    protoApprovalSeq = (String)cvApprovalSequence.get(APPROVAL_SEQUENCE_INDEX);
                    vecRequestParameters.add(1, protocolNumber);
                    vecRequestParameters.add(2, sequenceNumber);
                }
            }
            else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                AwardDocumentRouteBean awardDocumentRouteBean=(AwardDocumentRouteBean)moduleBean;
                vecRequestParameters.add(1, awardDocumentRouteBean.getMitAwardNumber());
                vecRequestParameters.add(2, awardDocumentRouteBean.getRoutingDocumentNumber());
            }
            vecRequestParameters.add(3, unitNumber);
            vecRequestParameters.add(4, new Boolean(buildMapRequired));
            vecRequestParameters.add(5, BUILD_MAPS_OPTION_PARAM);
            if(!this.isModuleSubmission && isProtocol(moduleCode)){
                vecRequestParameters.add(6, new Integer(protoApprovalSeq));
            }else{
                if(isPreviousData){
                    vecRequestParameters.add(6, new Integer(currentSubmissionNum));
                } else {
                    vecRequestParameters.add(6, null);
                }
            }
            cvExistingApprovers = new CoeusVector();
            cvRoutingMapBeans = new CoeusVector();
            cvTreeView = new CoeusVector();
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_ROUTING_COMM_FOR_ALL_APP);
            requester.setDataObjects(vecRequestParameters);

            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector vecData = new Vector();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    vecData = response.getDataObjects();

                    try {
                        if(vecData != null && vecData .size() > 0) {
                            if(vecData.get(0) != null) {
                                cvRoutingCommentsAttachments =  new CoeusVector();
                                cvRoutingCommentsAttachments = (CoeusVector)vecData.get(0);
                            }
                        }
                    }catch(Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }
        }catch(Exception exp) {
            exp.printStackTrace();
        }


    }

    /*
     * To set the Comments and Attachments details of all approvers
     * to the Coments and Attachments form
     */
    private void setCommentsAndDetails(){

        if(cvRoutingCommentsAttachments != null && cvRoutingCommentsAttachments.size() > 0) {
            try {
                int tblAttachmentHeight = 80;
                tblAttachmentHeight = tblAttachmentHeight + (cvRoutingCommentsAttachments.size() * commentsAttachmentsPanelHeight);
                for (Object routingCommentsAttachments : cvRoutingCommentsAttachments) {
                    RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) routingCommentsAttachments;
                    RoutingCommentsAndAttachmentsForm CommentsAttachmentsPanel  = new RoutingCommentsAndAttachmentsForm(routingDetailsBean, routingDetailsBean.getAttachments());

                    pnlProposalRoutingDetails.add(CommentsAttachmentsPanel.getChildPanel());
                    tblAttachmentHeight = tblAttachmentHeight + commentsAttachmentsPanelHeight;
                    cvRoutingComments = routingDetailsBean.getComments();

                    JTable tblAppComments = new JTable(cvRoutingComments.size(),3) {
                        public boolean isCellEditable(int row,int column) {
                            if(column ==1)
                                return true;
                            else
                                return false;
                        }
                    };
                    int nNumberRows = cvRoutingComments.size();
                    int nRowHeight = 18;
                    int nTblHeight = 0;
                    nTblHeight = nRowHeight * nNumberRows;
                    tblAppComments.setMinimumSize(new java.awt.Dimension(645, nTblHeight));
                    tblAppComments.setPreferredSize(new java.awt.Dimension(645, nTblHeight));
                    tblAppComments.setRowHeight(nRowHeight);
                    tblAppComments.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
                    //tblAppComments.setBackground(Color.LIGHT_GRAY);
                    tblAppComments.setBackground((Color) UIManager.getDefaults().get("Panel.background"));
                    TableColumn column = tblAppComments.getColumnModel().getColumn(0);
                    column.setMinWidth(498);
                    column.setPreferredWidth(498);

                    column = tblAppComments.getColumnModel().getColumn(1);
                    column.setMinWidth(17);
                    column.setPreferredWidth(17);
                    column.setCellRenderer(new CommentsRenderer());
                    column.setCellEditor(new AppCommentsEditor());

                    column = tblAppComments.getColumnModel().getColumn(2);
                    column.setMinWidth(130);
                    column.setPreferredWidth(130);

                    for( int indexComments = 0; indexComments < cvRoutingComments.size(); indexComments++ ){
                        RoutingCommentsBean routingCommentsBean = (RoutingCommentsBean)cvRoutingComments.get(indexComments);
                        tblAppComments.setValueAt(" "+routingCommentsBean.getComments(),indexComments,0);
                        tblAppComments.setValueAt(" "+routingCommentsBean.getUpdateTimestamp(),indexComments,2);
                    }
                    pnlProposalRoutingDetails.add(tblAppComments);
                    tblAttachmentHeight = tblAttachmentHeight + nTblHeight;
                }
                pnlProposalRoutingDetails.setPreferredSize(new java.awt.Dimension(600, tblAttachmentHeight));
            }catch (Exception exp ) {
                exp.printStackTrace();
            }
        } else {
            RoutingCommentsAndAttachmentsForm CommentsAttachmentsPanel  = new RoutingCommentsAndAttachmentsForm(EMPTYSTRING);
            pnlProposalRoutingDetails.add(CommentsAttachmentsPanel.getChildPanel());
        }
    }
    //COEUSQA:1445 - End


    //COEUSQA-1433 - Allow Recall from Routing - Start
    /** Displays the ProposalRejectionForm and allows the user to Reject the proposal */
    private void performRecallAction(){
        routingRecallForm = new RoutingRecallForm(mdiForm, moduleCode, moduleItemKey, true);
        currentRoutingDetailsBean.setComments(null);
        currentRoutingDetailsBean.setAttachments(null);             
        
        //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
        currentRoutingDetailsBean = null;
        CoeusVector cvRoutingApprovers = cvEntireApprovers;
        for(Object cvapproversData : cvRoutingApprovers) {
            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvapproversData;
            if(((String)mdiForm.getUserId()).equals(routingDetailsBean.getUserId()) && WAITING_FOR_APPROVAL.equals(routingDetailsBean.getApprovalStatus())){
                currentRoutingDetailsBean = routingDetailsBean;
                break;
            }
        }
        if(currentRoutingDetailsBean == null){
            for(Object cvapproversData : cvRoutingApprovers) {
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean) cvapproversData;
                if(WAITING_FOR_APPROVAL.equals(routingDetailsBean.getApprovalStatus())) {
                    currentRoutingDetailsBean = routingDetailsBean;
                    break;
                }
            }
        }
        //COEUSQA:3441 - End
        
        routingRecallForm.setCurrentApprovalBean(currentRoutingDetailsBean);
        routingRecallForm.setRoutingBean(this.routingBean);
        routingRecallForm.setRoutingBean(routingBean);
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - Start
//        boolean recallClicked = routingRecallForm.display();
        boolean isLockExist = lockRouting();
        if(!isLockExist){
            return;
        }
        boolean recallClicked = routingRecallForm.display();
        releaseUpdateLock();
        // Modified for COEUSQA-3816  Lite - Proposal routing - Locking issues - End
        if( recallClicked ){
            observable.notifyObservers(routingRecallForm.getModuleBean());
            enableApproveRejectButtons(false);
            enableRecallButton(FALSE);
            //if the module is protocol module set the submission status
            if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                setSubmissionStatusCode(IRB_RECAll_SUBMISSION);
                //to set the action code in actions tab
                setIRBProtocolActionValues();
                //to display recalled message
                String messageValue = coeusMessageResources.parseMessageKey(RECALL_IRB_PROTOCOL_START)+" "+routingBean.getModuleItemKey()+" "+
                        coeusMessageResources.parseMessageKey(RECALL_IRB_PROTOCOL_END);
                CoeusOptionPane.showQuestionDialog(messageValue, CoeusOptionPane.OPTION_OK,1);
                //to send the mail notification
                ProtocolMailController mailController = new ProtocolMailController();
                synchronized(mailController) {
                    mailController.sendMail(SUBMISSION_RECALLED_NOTIFICATION_ACTION, ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolNumber(),
                            ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                }
            }else if(moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                setSubmissionStatusCode(IACUC_RECAll_SUBMISSION);
                //to set the action code in actions tab
                setIACUCProtocolActionValues();
                //to display recalled message
                String messageValue = coeusMessageResources.parseMessageKey(RECALL_IACUC_PROTOCOL_START)+" "+routingBean.getModuleItemKey()+" "+
                        coeusMessageResources.parseMessageKey(RECALL_IACUC_PROTOCOL_END);
                CoeusOptionPane.showQuestionDialog(messageValue, CoeusOptionPane.OPTION_OK,1);
                //to send the mail notification
                edu.mit.coeus.iacuc.controller.ProtocolMailController mailController = new edu.mit.coeus.iacuc.controller.ProtocolMailController();
                synchronized(mailController) {
                    mailController.sendMail(SUBMISSION_RECALLED_NOTIFICATION_ACTION, ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolNumber(),
                            ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
                }
            }else if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE) {
                setModuleBean(routingRecallForm.getModuleBean());
                //to display recalled message
                String messageValue = coeusMessageResources.parseMessageKey(RECALL_PROPOSAL_START)+" "+routingBean.getModuleItemKey()+" "+
                        coeusMessageResources.parseMessageKey(RECALL_PROPOSAL_END);
                CoeusOptionPane.showQuestionDialog(messageValue, CoeusOptionPane.OPTION_OK,1);
                //to send the mail notification
                /* JM 3-27-2014 handling this elsewhere
                ActionValidityChecking checkValid = new ActionValidityChecking();
                synchronized(checkValid) {
                    checkValid.sendMail(ModuleConstants.PROPOSAL_DEV_MODULE_CODE, SUBMISSION_RECALLED_NOTIFICATION_ACTION,
                            ((ProposalDevelopmentFormBean)moduleBean).getProposalNumber(), 0);
                }
                */
            }
            setCloseRequired(true);
            releaseLock();
            close();
            return;
        }
        //setTreeNodeSelection();
    }

    /*
     * Method to check whether the user has rights to recall
     * @return hasRights
     */
    private boolean userHasRecallRights(){
        boolean hasRights = false;
        Vector vecRequestParameters = new Vector();
        String proNumber = getModuleItemKeyNumber();
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(CHECK_ROUTING_RECALL_RIGHTS);
        vecRequestParameters.add(MODULE_CODE_INDEX, moduleCode);
        vecRequestParameters.add(MODULE_ITEM_KEY_INDEX, proNumber);
        requester.setDataObjects(vecRequestParameters);
        AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!=null){
            if(response.isSuccessfulResponse()){
                hasRights = (Boolean)response.getDataObject();
            }
        }
        return hasRights;
    }



    /*
     * Method to get module item key number
     * @return itemKeyNumber
     */
    private String getModuleItemKeyNumber(){
        String itemKeyNumber = CoeusGuiConstants.EMPTY_STRING;
        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                itemKeyNumber = ((ProposalDevelopmentFormBean)moduleBean).getProposalNumber();
            }
        }else if(isProtocol(moduleCode)){
            //If the routing is opened during submission original protocol numbar and seqeunce number is used to get the details
            if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                itemKeyNumber = ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
            }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                itemKeyNumber = ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolNumber();
            }
        }
        return itemKeyNumber;
    }
	    //locking in Routing

       private boolean getApproverCountForLocking()throws Exception{
        boolean hasApprovers = false;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(APPROVER_COUNT);
        requesterBean.setId(routingBean.getRoutingNumber());
        
        AppletServletCommunicator comm = new AppletServletCommunicator(
        CONNECTION_STRING, requesterBean);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response!= null){
            if(response.isSuccessfulResponse()){
                hasApprovers = ((Boolean)response.getDataObject()).booleanValue();
            }else {
                throw new Exception(response.getMessage());
            }
        }
        return hasApprovers;
    }



    /*
     * Method to release the lock obtained. The lock is released when the user closes the recall screen
     */
    private void releaseLock(){
        String connectTo = CoeusGuiConstants.EMPTY_STRING;
        RequesterBean requester = new RequesterBean();
        Vector vecDataObject = new Vector();
        vecDataObject.add(moduleCode+CoeusGuiConstants.EMPTY_STRING);
        vecDataObject.add(getModuleItemKeyNumber());
        requester.setDataObjects(vecDataObject);
        requester.setFunctionType(ROUTING_UNLOCK);        
        try{
            connectTo =CoeusGuiConstants.CONNECTION_URL+"/LockingServlet";
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }
          
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }
	private void releaseUpdateLock(){
        try{
            //connect to server and release the lock
              String moduleItemKey=routingBean.getModuleItemKey();
               Integer moduleCode=routingBean.getModuleCode();
               Vector dataObjects = new Vector();

              //  String proposalNumber =((ProposalDevelopmentFormBean)moduleBean).getProposalNumber();
                String rowId = null;
                rowId = moduleItemKey;
                RequesterBean requester = new RequesterBean();
               // dataObjects.add(moduleCode);
                requester.setDataObject(moduleCode);
                requester.setId(rowId);
                requester.setFunctionType( UNLOCK_ROUTING );
                String connectTo= CONNECTION_STRING;
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


    /*
     * Method to fetch and set the required values for IRB action and trigger the save function
     * @return void
     */
    private void setIRBProtocolActionValues(){
        String sequenceNumber = String.valueOf(((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
        ProtocolActionsBean protocolActionsBean = new ProtocolActionsBean();
        protocolActionsBean.setActionId(SUBMISSION_RECALLED_ACTION);
        protocolActionsBean.setActionTypeCode(SUBMISSION_RECALLED_ACTION);
        protocolActionsBean.setSubmissionNumber(currentSubmissionNum);
        protocolActionsBean.setProtocolNumber(getModuleItemKeyNumber());
        protocolActionsBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
        protocolActionsBean.setActionTypeDescription("Submission Recalled");
        protocolActionsBean.setAcType("I");
        saveProtocolAction(protocolActionsBean);
    }

    /*
     * Method to fetch and set the required values for IACUC action and trigger the save function
     * @return void
     */
    private void setIACUCProtocolActionValues(){
        String sequenceNumber = String.valueOf(((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getSequenceNumber());
        edu.mit.coeus.iacuc.bean.ProtocolActionsBean iacucProtocolActionsBean = new edu.mit.coeus.iacuc.bean.ProtocolActionsBean();
        iacucProtocolActionsBean.setActionId(SUBMISSION_RECALLED_ACTION);
        iacucProtocolActionsBean.setActionTypeCode(SUBMISSION_RECALLED_ACTION);
        iacucProtocolActionsBean.setSubmissionNumber(currentSubmissionNum);
        iacucProtocolActionsBean.setProtocolNumber(getModuleItemKeyNumber());
        iacucProtocolActionsBean.setSequenceNumber(Integer.parseInt(sequenceNumber));
        iacucProtocolActionsBean.setActionTypeDescription("Submission Recalled");
        iacucProtocolActionsBean.setAcType("I");
        saveIACUCProtocolAction(iacucProtocolActionsBean);
    }

    /*
     * Method to save recall action for protocol
     * @param protocolActionsBean
     */
    private void saveProtocolAction(ProtocolActionsBean protocolActionsBean){
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(PROTOCOL_ACTION_UPDATE);
        requester.setDataObject(protocolActionsBean);
        try{
            String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }

    /*
     * Method to save recall action for protocol
     * @param iacucProtocolActionsBean
     */
    private void saveIACUCProtocolAction(edu.mit.coeus.iacuc.bean.ProtocolActionsBean iacucProtocolActionsBean){
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(IACUC_PROTOCOL_ACTION_UPDATE);
        requester.setDataObject(iacucProtocolActionsBean);
        try{
            String connectTo =CoeusGuiConstants.CONNECTION_URL+IACUC_PROTOCOL_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if (res != null && !res.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(res.getMessage());
                return;
            }
        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
    }

    /*
     * Method to enable/disable recall button
     * @param boolean enable
     */
    public void enableRecallButton(String enable){
        if(TRUE.equalsIgnoreCase(enable)){
            btnRecall.setEnabled(true);
        }else if(FALSE.equalsIgnoreCase(enable)){
            btnRecall.setEnabled(false);
        }else{
            int moduleItemStatus = 0;
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                    moduleItemStatus = ((ProposalDevelopmentFormBean)moduleBean).getCreationStatusCode();
                }
            }else if(isProtocol(moduleCode)){
                //If the routing is opened during submission original protocol numbar and seqeunce number is used to get the details
                if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                    moduleItemStatus = ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolStatusCode();
                }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                    moduleItemStatus = ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolStatusCode();
                }
            }

            if((moduleItemStatus==2 || moduleItemStatus==107 || moduleItemStatus==108)){
                if(cvEntireApprovers != null && cvEntireApprovers.size() > 0){

                    for(Object approverData : cvEntireApprovers){
                        RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)approverData;

                        String approvalStatus = routingDetailsBean.getApprovalStatus();
                        if(approvalStatus!=null && (approvalStatus.equals("W"))){
                            btnRecall.setEnabled(true);

                            break;
                        }else{
                            btnRecall.setEnabled(false);
                        }

                    }
                }else{
                    btnRecall.setEnabled(false);
                }
            }else{
                btnRecall.setEnabled(false);
            }
        }
    }


    /*
     * Method to enable/disable bypass button
     * @param boolean enable
     */
    public void disableByPassButton(){
        int moduleItemStatus = 0;
        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
            if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                moduleItemStatus = ((ProposalDevelopmentFormBean)moduleBean).getCreationStatusCode();
            }
        }else if(isProtocol(moduleCode)){
            //If the routing is opened during submission original protocol numbar and seqeunce number is used to get the details
            if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE){
                moduleItemStatus = ((edu.mit.coeus.irb.bean.ProtocolInfoBean)moduleBean).getProtocolStatusCode();
            }else if (moduleCode == ModuleConstants.IACUC_MODULE_CODE){
                moduleItemStatus = ((edu.mit.coeus.iacuc.bean.ProtocolInfoBean)moduleBean).getProtocolStatusCode();
            }
        }
        //coeus-2111 starts
        else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
            moduleItemStatus=2;//award status is independent of award routing
        }
       // coeus-2111 ends
        
        if(!(moduleItemStatus==2 || moduleItemStatus==107 || moduleItemStatus==108 ||moduleItemStatus==6)){
            btnBypass.setEnabled(false);
            btnApprove.setEnabled(false);
            btnReject.setEnabled(false);
            //COEUSQA:1699 - Add Approver Role - Start
            btnAddApprover.setEnabled(false);
            btnAlternative.setEnabled(false);
            //COEUSQA:1699 - End
        }
        
                
        
    }
    //COEUSQA-1433 - Allow Recall from Routing - End
    
    //COEUSQA 2111 STARTS
    private AwardDocumentRouteBean getAwardDocumentRouteBean(){
        AwardDocumentRouteBean awardDocumentRouteBean=(AwardDocumentRouteBean)moduleBean;
        AwardDocumentRouteBean newtmp=new AwardDocumentRouteBean(awardDocumentRouteBean);
        int approvalSeq=awardDocumentRouteBean.getRoutingApprovalSeq();
        if(approvalSeq>1){
            newtmp.setRoutingApprovalSeq(approvalSeq-1);
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_AWD_DOC_DETAILS);
        requester.setDataObject(newtmp);
        try{
            String connectTo =CoeusGuiConstants.CONNECTION_URL+AWARD_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            ResponderBean res = comm.getResponse();
            if((res!=null)&&res.isSuccessfulResponse()){
               newtmp=(AwardDocumentRouteBean)res.getDataObject();
            }

        }catch(Exception e){
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        }
        return newtmp;    
    }
    //COEUSQA 2111 ENDS    
    //COEUSQA:3441 - Recalled Proposal notifications, record status changes, and inbox messages - Start
    /**
     * Method to get the value for the RecallStatus label
     * @param sequenceNum
     */
    private void getRecalledLabeData(int sequenceNum){
        
        try {
            sequenceNum = sequenceNum - 1;            
            Vector vecRequestParameters = new Vector();
            String protoApprovalSeq = CoeusGuiConstants.EMPTY_STRING;
            vecRequestParameters.add(0, String.valueOf(moduleCode));
            if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                if(moduleBean!=null && moduleBean.getClass().equals(ProposalDevelopmentFormBean.class)){
                    vecRequestParameters.add(1, ((ProposalDevelopmentFormBean)moduleBean).getProposalNumber());
                    vecRequestParameters.add(2, "0");
                }
            }else if(isProtocol(moduleCode)){
                String protocolNumber,sequenceNumber;
                if(hmRoutingSeqHistory != null && hmRoutingSeqHistory.size() > 0){
                    CoeusVector cvApprovalSequence = (CoeusVector)hmRoutingSeqHistory.get(new Integer(currentSubmissionNum));
                    protocolNumber = (String)cvApprovalSequence.get(MODULE_ITEM_NUMBER_INDEX);
                    sequenceNumber = (String)cvApprovalSequence.get(MODULE_ITEM_KEY_SEQUENCE_INDEX);
                    vecRequestParameters.add(1, protocolNumber);
                    vecRequestParameters.add(2, sequenceNumber);
                }
            }
            vecRequestParameters.add(3, unitNumber);
            vecRequestParameters.add(4, new Boolean(buildMapRequired));
            vecRequestParameters.add(5, BUILD_MAPS_OPTION_PARAM);
            vecRequestParameters.add(6, new Integer(sequenceNum));
                        
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(GET_ROUTING_DATA);
            requester.setDataObjects(vecRequestParameters);
            
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECTION_STRING, requester);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector vecData = new Vector();
            if (response != null && response.isSuccessfulResponse()){
                vecData = response.getDataObjects();
                if(vecData != null && vecData .size() > 0) {
                    CoeusVector cvRouteMapBeans = new CoeusVector();
                    CoeusVector cvApprovers = new CoeusVector();
                    
                    if(vecData.get(1) != null && vecData.get(1).getClass() == CoeusVector.class) {
                        cvRouteMapBeans = (CoeusVector)vecData.get(1);
                        for( int index = 0; index < cvRouteMapBeans.size(); index++ ){
                            RoutingMapBean routingMapBean =
                                    (RoutingMapBean)cvRouteMapBeans.get(index);
                            if(routingMapBean.getRoutingMapDetails() != null)
                                cvApprovers.addAll(routingMapBean.getRoutingMapDetails());
                        }
                    }
                    if(cvApprovers != null && !cvApprovers.isEmpty()){
                        for(int index = 0; index < cvApprovers.size(); index++){
                            RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvApprovers.get(index);
                            if(RECALLED_STATUS.equals(routingDetailsBean.getApprovalStatus())){
                                if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                                    lblRecallStatus.setText(coeusMessageResources.parseMessageKey("proposal_Recalled_MessageCode.1001"));
                                } else{
                                    lblRecallStatus.setText(coeusMessageResources.parseMessageKey("protocol_Recalled_MessageCode.1001"));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }catch(Exception exp) {
            exp.printStackTrace();
        }
    }
    //COEUSQA:3441 - End
    
    
    //COEUSQA:1699 - Add Approver Role - Start
    /**
     * Method to perform the Add Approver Action
     */
    private void performAddApproverAction(){
        cvApprover = new CoeusVector();
        CoeusVector cvApprovers = cvExistingApprovers;
        RoutingDetailsBean addApproverRoutingDetailsBean = null;
        if(!alternateApprover){
            for(Object cvapprovers : cvExistingApprovers){
                RoutingDetailsBean routingDetailsBean = (RoutingDetailsBean)cvapprovers;
                if("W".equals(routingDetailsBean.getApprovalStatus()) && routingDetailsBean.isPrimaryApproverFlag() == true){
                    addApproverRoutingDetailsBean = routingDetailsBean;                    
                }
            }
            if(addApproverRoutingDetailsBean == null){                 
                  CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_AddApprover_exceptionCode.8019"));
                 return;
            }
        } else {
            addApproverRoutingDetailsBean = selectedRoutingDetailBean;
        }
        
        Equals eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(addApproverRoutingDetailsBean.getMapNumber() ));
        Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(addApproverRoutingDetailsBean.getLevelNumber() ));
        And eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
        CoeusVector cvTempData;
        if( !alternateApprover ){
            RoutingDetailsBean tempRoutingDetailsBean = new RoutingDetailsBean();            
            cvTempData = cvExistingApprovers.filter(eqMapIdAndEqLevelNumber);
            if( cvTempData != null && cvTempData.size() > 0 ){
                cvTempData.sort(STOP_NUMBER_FIELD, false);
                tempRoutingDetailsBean = (RoutingDetailsBean)cvTempData.get(0);
                nextStopNumber = tempRoutingDetailsBean.getStopNumber() + 1;
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
            if(approverExists(userInfoBean.getUserId().trim(), addApproverRoutingDetailsBean.getLevelNumber())){
                return ;
            } else {
                RoutingDetailsBean routingDetailsBean = new RoutingDetailsBean();
                routingDetailsBean.setAcType(TypeConstants.INSERT_RECORD);
                routingDetailsBean.setUserId(userInfoBean.getUserId().trim());
                routingDetailsBean.setUserName(userInfoBean.getUserName());
                routingDetailsBean.setRoutingNumber(addApproverRoutingDetailsBean.getRoutingNumber());
                routingDetailsBean.setMapNumber(addApproverRoutingDetailsBean.getMapNumber());
                routingDetailsBean.setLevelNumber(addApproverRoutingDetailsBean.getLevelNumber());
                routingDetailsBean.setDescription(DESCRIPTION + mdiForm.getUserName());
                routingDetailsBean.setApprovalStatus("W");
                routingDetailsBean.setUpdateUser(addApproverRoutingDetailsBean.getUserId());
                if( alternateApprover ){
                    // Add alternate approvers to same stop
                    routingDetailsBean.setStopNumber(addApproverRoutingDetailsBean.getStopNumber());
                    eqMapNumber = new Equals(MAP_NUMBER_FIELD, new Integer(addApproverRoutingDetailsBean.getMapNumber() ));
                    eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD, new Integer(addApproverRoutingDetailsBean.getLevelNumber() ));
                    eqMapIdAndEqLevelNumber = new And( eqMapNumber, eqLevelNumber);
                    cvApprovers = (cvApprovers == null)? new CoeusVector() : cvApprovers;
                    Equals eqStopNumber = new Equals(STOP_NUMBER_FIELD, new Integer(addApproverRoutingDetailsBean.getStopNumber()));
                    And eqStopNumAndMapIdAndEqLevelNum = new And( eqStopNumber, eqMapIdAndEqLevelNumber);
                    cvTempData = cvApprovers.filter(eqStopNumAndMapIdAndEqLevelNum);
                    if(cvTempData == null){
                        routingDetailsBean.setApproverNumber(1);
                    } else {
                        routingDetailsBean.setApproverNumber(cvTempData.size()+1);
                    }
                    routingDetailsBean.setPrimaryApproverFlag(false);
                    cvApprover.addElement(routingDetailsBean);
                    alternateApprover = false;
                }else{
                    // Add primary approvers to new stop
                    routingDetailsBean.setPrimaryApproverFlag(true);
                    routingDetailsBean.setStopNumber(nextStopNumber);
                    routingDetailsBean.setApproverNumber(1);
                    cvApprover.addElement(routingDetailsBean);
                }
                saveFormData();
            }
        }catch (Exception exception) {
            exception.printStackTrace();
        }
        
    }
    
    /**
     * Method to add alternate to the selected primary approver
     */
    private void performAddAlternateAction(){
        int selectedRow = tblProposalRouting.getSelectedRow();
        if( selectedRow >= 0 ){
            selectedRoutingDetailBean = (RoutingDetailsBean)tblProposalRouting.getTableData().get(selectedRow);
            if("W".equals(selectedRoutingDetailBean.getApprovalStatus())){
                if(selectedRoutingDetailBean!=null && selectedRoutingDetailBean.isPrimaryApproverFlag()){
                    alternateApprover = true;
                    selectedRow = selectedRow + 1;
                    performAddApproverAction();
                }else{
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey(SELECT_PRIMARY_APPROVER));
                }
            }else {
                CoeusOptionPane.showInfoDialog(
                        coeusMessageResources.parseMessageKey(SELECT_APPROVER_FROM_WAITING_FOR_APPROVAL));
            }
        }else{
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_PRIMARY_APPROVER));
        }
    }

    /**
     * Method to check if the selected user already exists as an approver
     * @return true if the user exists, false otherwise
     */
    private boolean approverExists(String userId, int levelNumber) {
        Equals eqLevelNumber = new Equals(LEVEL_NUMBER_FIELD,levelNumber);
        CoeusVector cvCurrenLevelApprovers = cvExistingApprovers.filter(eqLevelNumber);        
        
        for(Object cvcurrenLevelApprovers : cvCurrenLevelApprovers) {            
             if(userId.equalsIgnoreCase(((RoutingDetailsBean)cvcurrenLevelApprovers).getUserId())) {
                CoeusOptionPane.showInfoDialog(userId + USER_EXISTS_AS_APPROVER);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Methos to Saves the form data
     */
    private void saveFormData(){
        if(cvApprover != null && cvApprover.size() > 0 ){
            
            RequesterBean requester = new RequesterBean();
            // JM 4-1-2014 rolling our own routing
            requester.setFunctionType(VU_ROUTING_ADD_APPROVER);
            // JM END
            
            Vector vecData = new Vector();
            vecData.addElement(cvApprover);
            // JM 4-1-2014 needed for custom routing
            vecData.addElement(this.routingBean);
            // JM END
            vecData.addElement(null);
            requester.setDataObjects(vecData);          
            // JM 4-1-2014 rolling our own routing
            //String CONNECT_TO = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
            String CONNECT_TO = ROUTING_QUEUE_SERVLET;
            // JM END
            AppletServletCommunicator comm = new AppletServletCommunicator(CONNECT_TO, requester);
            comm.send();
            
            ResponderBean response = comm.getResponse();
            if ( response != null ){
                getFormData();
                setFormData();
            }
        }
        
    }
    
    //COEUSQA:1699 - End
    // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - Start
    /**
     * Method to get lock for the routing
     * @return isGotLock
     */
    private boolean lockRouting(){
        boolean isGotLock = false;
        Vector dataObjects = new Vector();
        String connectTo = CONNECTION_STRING ;
        RequesterBean  requesterBean = new RequesterBean();
        requesterBean.setFunctionType(LOCKINROUTING );
        requesterBean.setId( getModuleItemKeyNumber() );
        dataObjects.add(unitNumber);
        dataObjects.add((String)mdiForm.getUserId());
        dataObjects.add(moduleCode);
        requesterBean.setDataObjects(dataObjects);
        AppletServletCommunicator comm
                = new AppletServletCommunicator( connectTo, requesterBean );
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response != null ){
            if(response.isSuccessfulResponse()){
                LockingBean lockingBean = (LockingBean)response.getLockingBean();
                isGotLock = lockingBean.isGotLock();
            }else{
                CoeusOptionPane.showWarningDialog(response.getMessage());
            }
            
        }
        return isGotLock;
    }
    // Added for COEUSQA-3816 : Lite - Proposal routing - Locking issues - End
    
}