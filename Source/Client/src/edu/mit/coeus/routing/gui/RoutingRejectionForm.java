/** Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */


package edu.mit.coeus.routing.gui;
/* PMD check performed, and commented unused imports and variables on 12-JULY-2010
 * by keerthyjayaraj
 */
import edu.mit.coeus.bean.CoeusAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingAttachmentBean;
import edu.mit.coeus.routing.bean.RoutingBean;
import edu.mit.coeus.routing.bean.RoutingCommentsBean;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.documenttype.CoeusDocumentUtils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * Screen to allow the user to Reject the proposal
 * RoutingRejectionForm.java
 *
 * @author Vyjayanthi
 * Created on January 5, 2004, 11:58 AM
 */

public class RoutingRejectionForm extends javax.swing.JComponent
        implements ActionListener {
    
    private CoeusDlgWindow dlgRejectionWindow;
    
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    
//    private Frame parent;
//    private boolean modal;
    
    /** Flag to check if the proposal is rejected */
    private boolean rejectClicked = false;
    
    /** Flag to check if any data is modified */
    private boolean saveRequired = false;
    
    private static final String REJECT_ACTION = "R";
    
    private static final char PROPOSAL_APPROVE_UPDATE = 'Q';
    
    /** Holds the current Approval bean from the routing table */
    private RoutingDetailsBean currentApprovalBean;
    
    /** Holds an instance of the ProposalDevelopmentFormBean */
    private ProposalDevelopmentFormBean proposalDevelopmentFormBean;
    
    /** Holds the connection string */
//    private static final String CONNECTION_STRING = CoeusGuiConstants.CONNECTION_URL +
//            "/ProposalActionServlet";
    
    private static final String ENTER_COMMENTS = "proposal_Action_exceptionCode.8009";
    private static final String ERROR_DURING_REJECTION = "proposal_Action_exceptionCode.8010";
//    private static final String COMMENTS_ENTER = "routing_comments_exceptionCode.1006";
    private static final String SAVE_COMMENTS = "routing_comments_exceptionCode.1007";
    
    /*Streaming Servlet for viewing document*/
    private static final String STREAMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
    private static final char UNLOCK_ROUTING = 'l';
  
    //COEUSQA 2111 STARTS
    private static final String AWARD_SUBMISSION_SERVLET="/AwardSubmissionServlet";
    private static final char UPDATE_AWARD_STATUS='D';
    //private static final Integer AWARD_DOC_ROUT_APPROVE=2;
    private static final Integer AWARD_DOC_ROUT_REJECT=3;
    //COEUSQA 2111 ENDS
    
    /** Holds CoeusMessageResources instance used for reading message Properties. */
    private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
    private boolean parenProposal;
    
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
    private CoeusVector cvComments;
    private CoeusVector cvModifiedAttachments = new CoeusVector();
//    private int selectedRow;
//    private boolean isModify;
    private CommentsTableModel commentsTableModel;
    private AttachmentsTableModel attachmentTableModel;
    private CoeusVector cvCommentsData;
    private static final int COMMENTS_COLUMN = 0;
    private static final int MORE_COMMENTS_COLUMN = 1;   
    private int submissionStatusCode;
    private String mimeType;//Case 4007
    
    // JM 3-26-2014 rolling our own routing
    private static final char UPDATE_ROUTING_APPROVALS = 'U';
    private static final String ROUTING_QUEUE_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/RoutingQueueServlet";
    // JM END
    
    /**
     * Creates new form RoutingRejectionForm
     *
     * @param parent holds the frame
     * @param modal holds true if modal, false otherwise
     */
    public RoutingRejectionForm(Component parent, int moduleCode, String moduleItemKey, boolean modal) {
        this.moduleCode = moduleCode;
        this.moduleItemKey = moduleItemKey;
        cvComments = new CoeusVector();
        initComponents();
        postInitComponents();
        registerComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form. */
    private void postInitComponents(){
        dlgRejectionWindow = new CoeusDlgWindow(CoeusGuiConstants.getMDIForm(), "Rejection", true);
        dlgRejectionWindow.setDefaultCloseOperation(CoeusDlgWindow.DO_NOTHING_ON_CLOSE);
        dlgRejectionWindow.getContentPane().add(this);
        dlgRejectionWindow.setResizable(false);
         String title = "Proposal Rejection";
        if(isProtocol(moduleCode)){
            title = "Protocol Rejection";
            lblTitle.setText("Confirm your Rejection of the Protocol");
        }
        //AWARD ROUTING ENHANCEMENT STARTS
        if(moduleCode==1){
            title="Award Rejection";
            lblTitle.setText("Confirm your Rejection of the Award");
        }//AWARD ROUTING ENHANCEMENT STARTS
        dlgRejectionWindow.setTitle(title);
        dlgRejectionWindow.setFont(CoeusFontFactory.getLabelFont());
        dlgRejectionWindow.setSize(WIDTH,HEIGHT);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dlgSize = dlgRejectionWindow.getSize();
        dlgRejectionWindow.setLocation(screenSize.width/2 - (dlgSize.width/2),
                screenSize.height/2 - (dlgSize.height/2));
    }
    
    /** This method is used to set the listeners to the components. */
    private void registerComponents(){
        
        /** Code for focus traversal - start */
        java.awt.Component[] components = { txtArCommentsList, btnReject, btnCancel};
        ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
        setFocusTraversalPolicy(traversePolicy);
        setFocusCycleRoot(true);
        
        /** Code for focus traversal - end */
        
        //Add listeners to components
        btnReject.addActionListener(this);
        btnCancel.addActionListener(this);
        
        btnSaveAndNewComment.addActionListener(this);
        btnNewComments.addActionListener(this);
        btnModifyComments.addActionListener(this);
        btnDeleteComments.addActionListener(this);
        btnAddAttachments.addActionListener(this);
        btnDeleteAttachments.addActionListener(this);
        btnBrowse.addActionListener(this);
        attachmentTableModel = new AttachmentsTableModel();
        tblAttachments.setModel(attachmentTableModel);
        commentsTableModel = new CommentsTableModel();
        tblComments.setModel(commentsTableModel);
        commentsTableModel.fireTableDataChanged();
        
        CommentsRenderer commentsRenderer = new CommentsRenderer();
        CommentsEditor commentsEditor = new CommentsEditor();
        
        tblComments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader tableHeader = tblComments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,0));         
        TableColumn column = tblComments.getColumnModel().getColumn(COMMENTS_COLUMN);
        column.setMinWidth(280);
        column.setPreferredWidth(280);
        column.setResizable(false);
        column.setCellRenderer(new DefaultTableCellRenderer());

        column = tblComments.getColumnModel().getColumn(MORE_COMMENTS_COLUMN);
        column.setPreferredWidth(18);
        column.setResizable(false);
        column.setCellRenderer(commentsRenderer); 
        column.setCellEditor(commentsEditor);  
        
        //tblAttachments
        AttachmentsEditor attachmentsEditor = new AttachmentsEditor();
        AttachmentsRenderer attachmentsRenderer = new AttachmentsRenderer();
        
        
        //pdf icon
        tblAttachments.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableHeader = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tableHeader.setPreferredSize(new Dimension(0,22));           
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
        
        dlgRejectionWindow.addEscapeKeyListener(
                new AbstractAction("escPressed"){
            public void actionPerformed(ActionEvent ae){
                performWindowClosing();
            }
        });
        
        dlgRejectionWindow.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we){
                 releaseUpdateLock();
                performWindowClosing();
                return;
            }
        });
        
        dlgRejectionWindow.addComponentListener(
                new ComponentAdapter(){
            public void componentShown(ComponentEvent e){
                txtArCommentsList.requestFocus();
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

        btnCancel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        btnReject = new javax.swing.JButton();
        tbdPnAttAndCommts = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        coeusLabel1 = new edu.mit.coeus.utils.CoeusLabel();
        scrPnComments1 = new javax.swing.JScrollPane();
        txtArCommentsList = new javax.swing.JTextArea();
        btnSaveAndNewComment = new edu.mit.coeus.utils.CoeusButton();
        btnNewComments = new edu.mit.coeus.utils.CoeusButton();
        jScrollPane2 = new javax.swing.JScrollPane();
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

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnCancel, gridBagConstraints);

        lblTitle.setFont(CoeusFontFactory.getLabelFont());
        lblTitle.setText("Confirm your Rejection of the Proposal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        btnReject.setFont(CoeusFontFactory.getLabelFont());
        btnReject.setMnemonic('R');
        btnReject.setText("Reject");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(btnReject, gridBagConstraints);

        tbdPnAttAndCommts.setName("tbdPnAttAndCommts");
        jPanel1.setLayout(new java.awt.GridBagLayout());

        coeusLabel1.setText("Comments:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel1.add(coeusLabel1, gridBagConstraints);

        scrPnComments1.setMinimumSize(new java.awt.Dimension(310, 70));
        scrPnComments1.setPreferredSize(new java.awt.Dimension(310, 70));
        txtArCommentsList.setDocument(new LimitedPlainDocument(300));
        txtArCommentsList.setFont(CoeusFontFactory.getNormalFont());
        txtArCommentsList.setLineWrap(true);
        txtArCommentsList.setWrapStyleWord(true);
        scrPnComments1.setViewportView(txtArCommentsList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 5, 5);
        jPanel1.add(scrPnComments1, gridBagConstraints);

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

        jScrollPane2.setMinimumSize(new java.awt.Dimension(310, 70));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(310, 70));
        tblComments.setName("tblComments");
        jScrollPane2.setViewportView(tblComments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        jPanel1.add(jScrollPane2, gridBagConstraints);

        btnDeleteComments.setMnemonic('D');
        btnDeleteComments.setText("Delete");
        btnDeleteComments.setName("btnDeleteComments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        btnAddAttachments.setMnemonic('A');
        btnAddAttachments.setText("Add");
        btnAddAttachments.setName("btnAddAttachments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(btnAddAttachments, gridBagConstraints);

        btnDeleteAttachments.setMnemonic('D');
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(tbdPnAttAndCommts, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public edu.mit.coeus.utils.CoeusButton btnAddAttachments;
    public edu.mit.coeus.utils.CoeusButton btnBrowse;
    public javax.swing.JButton btnCancel;
    public edu.mit.coeus.utils.CoeusButton btnDeleteAttachments;
    public edu.mit.coeus.utils.CoeusButton btnDeleteComments;
    public edu.mit.coeus.utils.CoeusButton btnModifyComments;
    public edu.mit.coeus.utils.CoeusButton btnNewComments;
    public javax.swing.JButton btnReject;
    public edu.mit.coeus.utils.CoeusButton btnSaveAndNewComment;
    public edu.mit.coeus.utils.CoeusLabel coeusLabel1;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lblDescription;
    public javax.swing.JLabel lblFileName;
    public javax.swing.JLabel lblTitle;
    public javax.swing.JScrollPane scrPnComments1;
    public javax.swing.JTabbedPane tbdPnAttAndCommts;
    public javax.swing.JTable tblAttachments;
    public javax.swing.JTable tblComments;
    public javax.swing.JTextArea txtArCommentsList;
    public edu.mit.coeus.utils.CoeusTextField txtDescription;
    public edu.mit.coeus.utils.CoeusTextField txtFileName;
    // End of variables declaration//GEN-END:variables
    
    /** This method triggers all actions based on the event occured
     * @param actionEvent takes the actionEvent */
    public void actionPerformed(ActionEvent actionEvent) {
        try{
            Object source = actionEvent.getSource();
            dlgRejectionWindow.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if( source.equals(btnReject) ){
                //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium - start
                if(validateAttachmentsForSave()) {
                    if(isAttachmentSaveRequired()) {
                         performAttachmentsAction("Add");
                    }
                    //Added for COEUSQA-2457-Administrator cannot view Attachments in Routing in Premium - end
                    performRejectOperation();
                    // Update all the child propsoal status
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if(isParenProposal()){
                            updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                            
                        }
                    }
                    else if(moduleCode==ModuleConstants.AWARD_MODULE_CODE){
                        edu.mit.coeus.award.bean.AwardDocumentRouteBean awardDocumentRouteBean= 
                            (edu.mit.coeus.award.bean.AwardDocumentRouteBean)moduleBean;
                   UpdateAwardStatus(awardDocumentRouteBean.getMitAwardNumber(),awardDocumentRouteBean.getRoutingDocumentNumber() ,AWARD_DOC_ROUT_REJECT);
                    }
                }
            }else if( source.equals(btnCancel) ){
              releaseUpdateLock();
                performWindowClosing();
            }else if(source.equals(btnAddAttachments)){
                performAttachmentsAction("Add");
            }else if(source.equals(btnDeleteAttachments)){
                performAttachmentsAction("Delete");
            attachmentTableModel.fireTableDataChanged();
            }else if(source.equals(btnBrowse)){
                performBrowseAction();
            }else if(source.equals(btnNewComments)){
                if(txtArCommentsList.getText().trim().length() > 0 ){
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
                txtArCommentsList.requestFocus();
                txtArCommentsList.setText("");
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
        }catch (CoeusException exception){
            exception.printStackTrace();
            CoeusOptionPane.showErrorDialog(exception.getMessage());
        }finally{
            dlgRejectionWindow.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    
    /**
     * This method is used to perform the Window closing operation. Before closing
     * the window it checks the saveRequired flag and the function type.
     * If the saveRequired is true then it saves the details to the
     * database else dispose this JDialog.
     */
    private void performWindowClosing(){
        //If comments are added/modified
//        if( txtArCommentsList.getText().trim().length() > 0 ){
//            if( !txtArCommentsList.getText().trim().equals(currentApprovalBean.getComments()) ){
//                setSaveRequired(true);
//            }
//        }
    // releaseUpdateLock();
        if(saveAttachment || saveComment){

            setSaveRequired(true);
        }        
        
        int option = JOptionPane.NO_OPTION;
        if(isSaveRequired()){
            String messageKey = "saveConfirmCode.1002";
            if(saveComment && saveAttachment){
                messageKey = "routing_attachments_exceptionCode.1008";
            }else if(saveComment){
                messageKey = "routing_comments_exceptionCode.1005";
            }else if(saveAttachment){
                messageKey = "routing_attachments_exceptionCode.1009";
            }            
            option
                    = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey(messageKey),
                    CoeusOptionPane.OPTION_YES_NO_CANCEL,
                    CoeusOptionPane.DEFAULT_YES);
            switch( option ){
                case ( JOptionPane.YES_OPTION ):
                    try{
                        performRejectOperation();
                        if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                            updateChildStatus(proposalDevelopmentFormBean.getProposalNumber());
                        }
                    }catch(Exception e){
                        e.getMessage();
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgRejectionWindow.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION ) :
                    break;
            }
        }else{
            dlgRejectionWindow.dispose();
        }
    }
    
    /** */
    private void performRejectOperation(){
        if(txtArCommentsList.getText().trim().length() > 0){
            //Code commented for Case#2785 - Routing Enhancement save confirmation issue - starts
//            int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(SAVE_COMMENTS),
//                        CoeusOptionPane.OPTION_YES_NO,2);
//            switch( option ){
//                case ( JOptionPane.YES_OPTION ):
            //Code commented for Case#2785 - Routing Enhancement save confirmation issue - ends
                         if(modifyComment){
                             performCommentsAction("Update");
                        }else{
                            performCommentsAction("Add");
                        }
             //Code commented for Case#2785 - Routing Enhancement save confirmation issue - starts
//                        break;
//                case ( JOptionPane.NO_OPTION ):
//                    break;
//                case ( JOptionPane.CANCEL_OPTION ) :
//                    break;
//            }
             //Code commented for Case#2785 - Routing Enhancement save confirmation issue - ends
        }
        if( currentApprovalBean.getComments() != null
                && currentApprovalBean.getComments().size() > 0 ){
            
            currentApprovalBean.setAction(REJECT_ACTION);
            currentApprovalBean.setApproveAll(0);
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType(UPDATE_ROUTING_APPROVALS); // JM 3-24-2014 our own routing
            Vector vecData = new Vector();
            currentApprovalBean.setAcType(TypeConstants.UPDATE_RECORD);
            vecData.addElement(null);
            vecData.addElement(currentApprovalBean);
            vecData.addElement(routingBean);
            String CONNECT_TO = CoeusGuiConstants.CONNECTION_URL + "/RoutingServlet";
            requester.setDataObjects(vecData);
            // JM 3-26-2014 our routing
            AppletServletCommunicator comm = new AppletServletCommunicator(ROUTING_QUEUE_SERVLET, requester);
            comm.send();
            // JM END
            
            ResponderBean response = comm.getResponse();
            if ( response != null ){
                if( response.isSuccessfulResponse() ){
                    Vector vecUpdatedData = response.getDataObjects();
                    if(moduleCode == ModuleConstants.PROPOSAL_DEV_MODULE_CODE){
                        if( vecUpdatedData.get(1) != null ){
                            setProposalDevelopmentFormBean((ProposalDevelopmentFormBean)vecUpdatedData.get(1));
                            //code added for Case#3775 - Manually selected map disappears
                            setModuleBean(vecUpdatedData.get(1));
                        }
                    }
                    setSaveRequired(false);
                    //Close the form after saving
                    dlgRejectionWindow.dispose();
                    rejectClicked = true;
                }else{
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(ERROR_DURING_REJECTION));
                }
            }
        }else{
            txtArCommentsList.requestFocus();
            CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                    ENTER_COMMENTS));
        }
    }
    
    /** Displays the form
     * @return rejectClicked holds true if proposal is rejected, false otherwise
     */
    public boolean display(){
        //txtArComments.setText(currentApprovalBean.getComments());
        dlgRejectionWindow.show();
        return rejectClicked;
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
    public RoutingDetailsBean getCurrentApprovalBean() {
        return currentApprovalBean;
    }
    
    /** Setter for property currentApprovalBean.
     * @param currentApprovalBean New value of property currentApprovalBean.
     *
     */
    public void setCurrentApprovalBean(RoutingDetailsBean currentApprovalBean) {
        this.currentApprovalBean = currentApprovalBean;
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
    
    /** Update the child proposal's creation status code, if the parent proposal
     *performed actions like Submit, Approve, Reject, PostSubmission
     */
    private boolean updateChildStatus(String proposalNumber) throws CoeusException{
        final String connect = CoeusGuiConstants.CONNECTION_URL +"/ProposalActionServlet";
        boolean success = false;
//        Vector data = new Vector();
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
            throw new CoeusException(response.getMessage());
        }
        return success;
    }
    
    /**
     * Getter for property parenProposal.
     * @return Value of property parenProposal.
     */
    public boolean isParenProposal() {
        return parenProposal;
    }
    
    /**
     * Setter for property parenProposal.
     * @param parenProposal New value of property parenProposal.
     */
    public void setParenProposal(boolean parenProposal) {
        this.parenProposal = parenProposal;
    }
    
    public Object getModuleBean() {
        return moduleBean;
    }
    
    public void setModuleBean(Object moduleBean) {
        this.moduleBean = moduleBean;
    }

    public RoutingBean getRoutingBean() {
        return routingBean;
    }

    public void setRoutingBean(RoutingBean routingBean) {
        this.routingBean = routingBean;
    }
    
    public void populateCommentsTable(){
        if(currentApprovalBean!=null){
            commentsTableModel.setData(currentApprovalBean.getComments());
        }else{
            commentsTableModel.setData(null);
        }
        commentsTableModel.fireTableDataChanged();
    }
    
    public void populateAttachmentsTable(){
        if(currentApprovalBean!=null){
            attachmentTableModel.setData(currentApprovalBean.getAttachments());
            if(currentApprovalBean.getAttachments()!=null && currentApprovalBean.getAttachments().size()>0){
                tbdPnAttAndCommts.setIconAt(1,
                        new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.DETAILS_ICON)));
            }else{
                tbdPnAttAndCommts.setIconAt(1, null);
            }
        }else{
            attachmentTableModel.setData(null);
            attachmentTableModel.fireTableDataChanged();
        }
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
        // #Case 3855 -- start added new dummy column to store file name value which is not visible to the user.
        String colNames[] = {"", "Description",""};
        Class[] colTypes = new Class [] {Boolean.class, String.class,RoutingAttachmentBean.class};
       // #Case 3855 -- end
        CoeusVector cvAttachmentsData;
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
            //# Case 3855 -- start Added to return the file name
            else if(column ==2) {
                return routingAttachmentBean;//case 4007
            }
            //#Case 3855 -- end
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
    
    public void performCommentsAction(String action){
        if(action.equals("Add")){
            if(txtArCommentsList.getText().trim().length()==0){
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1006"));
                return;
            }
            RoutingCommentsBean routingCommentsBean = new RoutingCommentsBean();
            routingCommentsBean.setRoutingNumber(currentApprovalBean.getRoutingNumber());
            routingCommentsBean.setApproverNumber(currentApprovalBean.getApproverNumber());
            routingCommentsBean.setComments(txtArCommentsList.getText());
            routingCommentsBean.setLevelNumber(currentApprovalBean.getLevelNumber());
            routingCommentsBean.setMapNumber(currentApprovalBean.getMapNumber());
            routingCommentsBean.setStopNumber(currentApprovalBean.getStopNumber());
            routingCommentsBean.setAcType("I");
            if(currentApprovalBean!=null){
                if(currentApprovalBean.getComments()==null){
                    CoeusVector cvComments = new CoeusVector();
                    cvComments.add(routingCommentsBean);
                    currentApprovalBean.setComments(cvComments);
                }else{
                    currentApprovalBean.getComments().add(routingCommentsBean);
                }
            }
            populateCommentsTable();
            txtArCommentsList.setText("");
            saveComment = true;
        }else if(action.equals("Delete")){
            int selectedRow = tblComments.getSelectedRow();
            if(selectedRow!=-1){
                RoutingCommentsBean routingCommentsBean =
                        (RoutingCommentsBean)currentApprovalBean.getComments().get(selectedRow);
                txtArCommentsList.setText(routingCommentsBean.getComments());
                int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1002"),
                        CoeusOptionPane.OPTION_YES_NO_CANCEL,2);
                switch(option){
                    case CoeusOptionPane.SELECTION_YES:
                        if(routingCommentsBean!=null){
                            if(routingCommentsBean.getAcType()!=null &&
                                    routingCommentsBean.getAcType().equals("I") ){
                                currentApprovalBean.getComments().remove(selectedRow);
                            }
                        }
                }
                populateCommentsTable();
                saveComment = true;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1003"));
            }
            clearCommentsFormData();
        }else if(action.equals("Modify")){
            int selectedRow = tblComments.getSelectedRow();
            if(selectedRow!=-1){
                selectedRoutingCommentsBean =
                        (RoutingCommentsBean)currentApprovalBean.getComments().get(selectedRow);
                if(selectedRoutingCommentsBean!=null){
                    txtArCommentsList.setText(selectedRoutingCommentsBean.getComments());
                }
                modifyComment = true;
                saveComment = true;
            }else{
                CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey("routing_comments_exceptionCode.1001"));
            }
        }else if(action.equals("Update")){
            if(selectedRoutingCommentsBean!=null){
                if(selectedRoutingCommentsBean.getAcType()==null){
                    selectedRoutingCommentsBean.setAcType("U");
                }
                selectedRoutingCommentsBean.setComments(txtArCommentsList.getText());
                populateCommentsTable();
            }
            clearCommentsFormData();
        }
        commentsTableModel.fireTableDataChanged();
    }
    
    public void clearCommentsFormData(){
        txtArCommentsList.setText("");
        modifyComment = false;
        selectedRoutingCommentsBean = null;
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
        
               public void DisableRoutingRejectionForm(){

                btnCancel.setEnabled(false);
                btnReject.setEnabled(false);
           // btnClose.setEnabled(false);
            //txtArCommentsList.setEnabled(false);
                 btnModifyComments.setEnabled(false);
                 btnNewComments.setEnabled(false);
                 tblComments.setEnabled(false);
                 btnDeleteComments.setEnabled(false);
                 btnSaveAndNewComment.setEnabled(false);
                 btnNewComments.setEnabled(false);
                 txtArCommentsList.setEnabled(false);
                 tbdPnAttAndCommts.setEnabled(false);
            }
        public void performAttachmentsAction(String action){
        if(action.equals("Add")){
            if(validateAttachmentData()){
                RoutingAttachmentBean routingAttachmentBean = new RoutingAttachmentBean();
                routingAttachmentBean.setRoutingNumber(currentApprovalBean.getRoutingNumber());
                routingAttachmentBean.setApproverNumber(currentApprovalBean.getApproverNumber());
                routingAttachmentBean.setFileBytes(getBlobData());
                routingAttachmentBean.setMimeType(getMimeType());//case 4007
                routingAttachmentBean.setDescription(txtDescription.getText());
                routingAttachmentBean.setFileName(txtFileName.getText());
                routingAttachmentBean.setLevelNumber(currentApprovalBean.getLevelNumber());
                routingAttachmentBean.setMapNumber(currentApprovalBean.getMapNumber());
                routingAttachmentBean.setStopNumber(currentApprovalBean.getStopNumber());
                routingAttachmentBean.setAcType("I");
                if(currentApprovalBean!=null){
                    if(currentApprovalBean.getAttachments()==null){
                        CoeusVector cvAttachments = new CoeusVector();
                        cvAttachments.add(routingAttachmentBean);
                        currentApprovalBean.setAttachments(cvAttachments);
                        cvModifiedAttachments.addAll(cvAttachments);/**/
                        
                    }else{
                        currentApprovalBean.getAttachments().add(routingAttachmentBean);
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
                                (RoutingAttachmentBean)currentApprovalBean.getAttachments().get(selectedRow);
                        if(routingAttachmentBean!=null){
                            if(routingAttachmentBean.getAcType()!=null &&
                                    routingAttachmentBean.getAcType().equals("I")){
                                cvModifiedAttachments.remove(selectedRow);
                                currentApprovalBean.getAttachments().remove(selectedRow);
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
      
     public void performBrowseAction(){
        CoeusFileChooser fileChooser = new CoeusFileChooser(dlgRejectionWindow);
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
                    setMimeType(null);
                }
            }
        }
    }
     
     
     public void clearAttachmentsFormData(){
        txtFileName.setText("");
        txtDescription.setText("");
        setFileSelected(false);
        setBlobData(new byte[0]);
        setMimeType(null);
    }
     
        class AttachmentsEditor extends DefaultCellEditor implements ActionListener{
             private JButton btnDocument;
             int row;
//             private ImageIcon  pdfIcon;
             AttachmentsEditor() {
                 super(new JComboBox());
                 //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - start
//                 pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_PDF_ICON));
              //   pdfIcon = new ImageIcon(getClass().getClassLoader().getResource(CoeusGuiConstants.ENABLED_ATTACHMENT_ICON));
                 //Commented/Added for case#2156 - Uploading a Narrative - Use of Adobe Icon - end
                 //#Case 3855  -- commented to set attachment specific iconfor the button 
                //   btnDocument = new JButton(pdfIcon);
                   btnDocument = new JButton();
                 btnDocument.addActionListener(this);
             }
             public java.awt.Component getTableCellEditorComponent(javax.swing.JTable jTable, Object value, boolean isSelected, int row, int column) {
                 this.row = row;
                 RoutingAttachmentBean routingAttachmentBean = (RoutingAttachmentBean)cvModifiedAttachments.elementAt(row);
                 switch(column){
                     case 0:
                         //#Case 3855 -- start Added attachment specific icon.
//                         String fileExtension = UserUtils.getFileExtension((String)jTable.getModel().getValueAt(row,2));
//                         btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));            
                         //#Case 3855 -- end
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
             // #Case 3855 -- commented to add attachment specific icon.
                  btnDocument  = new JButton();
            //    btnDocument.setIcon(imgIcnDesc);
            }
            
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col){
                Component returnComponent = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,col);
                switch(col){
                    case 1:
                        return returnComponent;
                        
                    case 0:
                     // #Case 3855 -- start  Add attachment specific icon to the button
//                        String fileExtension = UserUtils.getFileExtension((String)table.getModel().getValueAt(row,2));
//                        btnDocument.setIcon(UserUtils.getAttachmentIcon(fileExtension));
                         // #Case 3855 -- end
                        CoeusAttachmentBean attachment = (CoeusAttachmentBean) table.getModel().getValueAt(row,2);
                        CoeusDocumentUtils docTypeUtils  = CoeusDocumentUtils.getInstance();
                        btnDocument.setIcon(docTypeUtils.getAttachmentIcon(attachment));
                         //4007 End
                        return btnDocument;
                }
                return returnComponent;
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
    
    private boolean isProtocol(int moduleCode){
        boolean isProtocol = false;
        if(moduleCode == ModuleConstants.PROTOCOL_MODULE_CODE || moduleCode == ModuleConstants.IACUC_MODULE_CODE){
            isProtocol = true;
        }
        return isProtocol;
    }
    
    public boolean isFileSelected() {
        return fileSelected;
    }

    public void setFileSelected(boolean fileSelected) {
        this.fileSelected = fileSelected;
    }

    public byte[] getBlobData() {
        return blobData;
    }

    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
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
  // for releasing lock
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