/*
 * ScheduleActionInputForm.java
 *
 * Created on May 5, 2003, 1:11 PM
 */

/* PMD check performed, and commented unused imports and variables on 27-JUNE-2007
 * by Nandkumar S N
 */

package edu.mit.coeus.iacuc.gui;


import edu.mit.coeus.gui.* ;
import edu.mit.coeus.iacuc.bean.ProtocolActionDocumentBean;
import edu.mit.coeus.iacuc.bean.ProtocolInfoBean;
import edu.mit.coeus.questionnaire.bean.QuestionnaireAnswerHeaderBean;
import edu.mit.coeus.questionnaire.controller.QuestionAnswersController;
import edu.mit.coeus.questionnaire.gui.QuestionAnswersForm;
//import edu.mit.coeus.utils.CoeusGuiConstants;
//import edu.mit.coeus.utils.dbengine.* ;
import edu.mit.coeus.sponsormaint.gui.* ;
import edu.mit.coeus.sponsormaint.bean.* ;
import edu.mit.coeus.utils.document.DocumentBean;
import edu.mit.coeus.utils.document.DocumentConstants;
import edu.mit.coeus.utils.locking.LockingBean;

import java.awt.*;
import java.awt.event.*;
//import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.util.*;
//import java.util.Date;
import java.beans.*;

//import edu.mit.coeus.gui.CoeusAppletMDIForm;
//import edu.mit.coeus.gui.CoeusMessageResources;
//import edu.mit.coeus.gui.CoeusAboutForm ;
//import edu.mit.coeus.gui.CoeusDlgWindow ;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.codetable.bean.*;
import edu.mit.coeus.brokers.* ;
import edu.mit.coeus.search.gui.* ;
import edu.mit.coeus.iacuc.bean.ProtocolSubmissionInfoBean;
import edu.mit.coeus.iacuc.bean.ProtocolActionsBean;
import edu.mit.coeus.exception.*;
//import java.sql.Timestamp;
import edu.mit.coeus.utils.ModuleConstants;

public class ScheduleActionInputForm extends CoeusDlgWindow implements ActionListener {
	private CoeusMessageResources coeusMessageResources = CoeusMessageResources.getInstance();
	
	//private final String CODE_TABLE_SERVLET = "/CodeTableServlet";
	
	private Frame parent;
	
	//private CoeusDlgWindow thisWindow ;
	
	private String title ;
	private String prompt ;
	private String defValue ;
	private String userInput ;
	private boolean continueAction ;
	
	//private CoeusFontFactory fontFactory;
	private boolean lockSchedule = true;
	private Vector reviewComments;
	private ReviewCommentsForm reviewCommentsForm;
	private ProtocolActionsBean actionBean;
	private ProtocolSubmissionInfoBean protocolSubmissionInfoBean;
	private boolean releaseLock;
	//For the date field added by Jobin - start
	private DateUtils dtUtils = new DateUtils();
	//holds action date 
	private java.sql.Date actionDate;
        //Added by Geo to fix the bug of creating new date with local time zone
        private String todayDate;
	private java.text.SimpleDateFormat dtFormat
		= new java.text.SimpleDateFormat("MM/dd/yyyy"); // Jobin - end
        
        //Added for performing Protocol Actions - start - 1
        private String committeeId;        
        private CoeusVector cvCommittee;
        //private static final char GET_COMMITTEE_DATA = 'e';
        //Added for performing Protocol Actions - end - 1
        
        //Added for case#3046 - Notify IRB attachments - start        
//        private String fileName;
//        private byte[] fileBytes;        
        private static final String STREMING_SERVLET = CoeusGuiConstants.CONNECTION_URL + "/StreamingServlet";
        //Added for case#3046 - Notify IRB attachments - end
        //Code added for Case#3554 - Notify IRB enhancement - starts
        private ArrayList typeQualifiers;
        private ArrayList reviewTypes;
        private String notificationType;
        private String reviewerType;
        private char submissionMode ;
        private static final char MANDATORY = 'M' ;
        private static final char DENIED = 'D' ;
        //Code added for Case#3554 - Notify IRB enhancement - ends
        //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
        private ProtocolUploadDocTableModel protocolUploadDocTableModel;
        private ProtocolUploadDocRenderer protocolUploadDocRenderer;
        private static final int DESCRIPTION_COLUMN = 0;
        private static final int LAST_UPDATED_COLUMN = 1;
        private static final int UPDATED_BY_COLUMN = 2;
        private static final char MODIFY_DOCUMENT = 'M';
        private static final char ADD_DOCUMENT = 'A';
        private CoeusVector cvUploadDocuments = new CoeusVector();
        private static final String EMPTY_STRING = "";
        private static final String SELECT_DOCUMENT = "protoSubmissionAttac_exceptionCode.2001";
        private static final String SELECT_DOCUMENT_TO_MODIFY = "protoSubmissionAttac_exceptionCode.2002"; 
        private static final String SELECT_DOCUMENT_TO_DELETE = "protoSubmissionAttac_exceptionCode.2003";
        private static final String SELECT_DOCUMENT_TO_VIEW = "protoSubmissionAttac_exceptionCode.2004";
        private static final String DOCUMENT_DELETE_CONFIRM = "protoSubmissionAttac_exceptionCode.2005";
        private static final int NOTIFY_IRB =116;
        private static final int REQUEST_TO_CLOSE_ACTION = 105;
        private static final int REQUEST_FOR_SUSPENSION_ACTION = 106;
        private static final int REQUEST_TO_CLOSE_ENROLLMENT_ACTION = 108;
        private static final int REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION = 114;
        private static final int REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION= 115;
        private static final int DESCRIPTION_COLUMN_WIDTH = 140;
        private static final int LAST_UPDATED_COLUMN_WIDTH = 110;
        private static final int UPDATED_BY_COLUMN_WIDTH = 60;
        //COEUSDEV-328 : End
        //Added for COEUSDEV-86 : Questionnaire for a Submission
        private final String PROTOCOL_SUBMISSION_SERVLET = "/iacucProtocolSubSrvlt";
        private final String PROTOCOL_SERVLET = "/IacucProtocolServlet";
        private static final String QUESTIONNAIRE_SERVLET = "/questionnaireServlet";
        private int selectedIndex = -1;
        private static final char RESTART = 'S';
        private QuestionAnswersController questionAnswersController;
        private QuestionAnswersForm questionAnswersForm ;
        private static final char MODIFY = 'T';
        private static final char LOCK_PROTOCOL_DURING_SUBMISSION = 'L';
        private static final char RELEASE_LOCK_AFTER_SUBMISSION = 'l';
        private static final char CHECK_ANY_QUESTION_ANSWEREN_IN_MODULE = 'c';
        private static final int SUBMISSION_INFO_BEAN = 0;
        private static final int ACTION_INFO_BEAN = 1;
        private static final char UPDATE_SUBMISSION_ACTION_DETAILS = 'U';
        private static final char CLEAN_TEMPORARY_SUBMISSION = 'u';
        private static final int TEMPORARY_SUBMISSION = 105;
        private static final char CHECK_QUESTIONNAIRE_COMPLETED = 'b';
        private static final char CHECK_IS_PROTOCOL_LOCK_EXISTS = 'U';
        private int tempProtoSubmissionNumber = 0;
        private static final int IACUC_MODULE_ITEM_CODE = 9;
        private static final int PROTOCOL_SUBMISSION_SUB_MODULE = 2;
        private static final String APPEND_TEMP_SUB_CHAR_AFTER_PROTO_NUMBER = "T";
        boolean isTempProtocolUpdated = false;
        private CoeusDlgWindow dlgProtoSubQuestionnaire;
        private static final String SUBMISSION_QUESTIONNAIRE_WINDOE_CLOSE = "protoQuestionnaireSubmmissionFormClose_exceptionCode.1001";
        private static final String NOTIFICATION_TYPE_CHANGE_WITH_ANS_QNR = "iacucProtoSubmission_notifyChange_exceptionCode.1002";
        private static final String NOTIFY_SUBMISSION_WITHOUT_COMMITTEE = "iacucNotifySubmissionWithoutCommittee_exceptionCode.1004";
        private static final String SELECT_NOTIFICATION_TYPE = "iacucProtoSubmissionNotifyType_exceptionCode.1005";
        private static final String SELECT_REVIEW_TYPE = "iacucProtoSubmissionReviewType_exceptionCode.1006";
        private static final String SELECT_COMMITTEE = "iacucProtoSubmissionCommittee_exceptionCode.1007";
        private static final String QUESTIONNAIRE_SAVE_CONFIRMATION = "questions_exceptionCode.1005";
        private static final String NEW_QUESTIONNAIRE_VERSION = "iacucProtoSubquestionnaire_exceptionCode.1010";
        private static final String INCOMPLETE_QUESTIONNAIRE ="iacucProtoSubQuestionnaire_exceptionCode.1009";
        private static final String MANDATORY_QUESTIONNAIRE = "iacucProtoSubQuestionnaire_exceptionCode.1008";
        private static final char SAVE = 'R';
        private static final char GET_IACUC_COMMITTEE_DATA = 'q';
        //COEUSDEV-86 : End
        
	/** Creates new form ScheduleActionInputForm */
	public ScheduleActionInputForm(Frame parent, String title, String prompt, String defValue ) {
		super(parent,title,true);
		this.parent = parent ;
		this.title = title ;
		this.prompt = prompt ;
		this.defValue = defValue ;
		initComponents();
                btnQuestionnaire.addActionListener(this);
		//pnlInput.setSize(500, 325) ;
                //Added for case#3046 - Notify IRB attachments
                //Code commented and modified for Case#3554 - Notify IRB enhancement
		//pnlAttachment.setVisible(false);
                btnView.setEnabled(true);
                setUIForAttachemnts(false);
		initialiseData() ;
	}
	
        
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlInput = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        lblComments = new javax.swing.JLabel();
        scrpnlInput = new javax.swing.JScrollPane();
        txtInput = new javax.swing.JTextArea();
        lblPrompt = new javax.swing.JTextArea();
        btnReviewComments = new javax.swing.JButton();
        lblActionDate = new javax.swing.JLabel();
        txtActionDate = new edu.mit.coeus.utils.CoeusTextField();
        lblCommittee = new javax.swing.JLabel();
        cmbCommittee = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        cmbReviewType = new javax.swing.JComboBox();
        lblNotificationType = new javax.swing.JLabel();
        lblReviewType = new javax.swing.JLabel();
        cmbNotificationType = new javax.swing.JComboBox();
        pnlAttachments = new javax.swing.JPanel();
        scrpnAttachments = new javax.swing.JScrollPane();
        tblAttachments = new javax.swing.JTable();
        pnlAttachmentButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnModify = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnQuestionnaire = new javax.swing.JButton();

        pnlInput.setLayout(new java.awt.GridBagLayout());

        pnlInput.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlInput.setMaximumSize(new java.awt.Dimension(600, 350));
        pnlInput.setMinimumSize(new java.awt.Dimension(600, 350));
        pnlInput.setPreferredSize(new java.awt.Dimension(600, 350));
        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('o');
        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('c');
        btnCancel.setText("Cancel");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(btnCancel, gridBagConstraints);

        lblComments.setFont(CoeusFontFactory.getLabelFont());
        lblComments.setText("Comments :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 2, 0);
        pnlInput.add(lblComments, gridBagConstraints);

        scrpnlInput.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrpnlInput.setMaximumSize(new java.awt.Dimension(100, 100));
        scrpnlInput.setMinimumSize(new java.awt.Dimension(100, 100));
        scrpnlInput.setPreferredSize(new java.awt.Dimension(100, 100));
        txtInput.setRows(2000);
        txtInput.setMaximumSize(new java.awt.Dimension(100, 150));
        txtInput.setMinimumSize(new java.awt.Dimension(100, 150));
        scrpnlInput.setViewportView(txtInput);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 11, 2, 12);
        pnlInput.add(scrpnlInput, gridBagConstraints);

        lblPrompt.setBackground(javax.swing.UIManager.getDefaults().getColor("InternalFrame.borderColor"));
        lblPrompt.setEditable(false);
        lblPrompt.setRows(2);
        lblPrompt.setMaximumSize(new java.awt.Dimension(100, 50));
        lblPrompt.setMinimumSize(new java.awt.Dimension(100, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 225;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(lblPrompt, gridBagConstraints);

        btnReviewComments.setFont(CoeusFontFactory.getLabelFont());
        btnReviewComments.setMnemonic('R');
        btnReviewComments.setText("Review Comments");
        btnReviewComments.setToolTipText("");
        btnReviewComments.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReviewCommentsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(btnReviewComments, gridBagConstraints);

        lblActionDate.setFont(CoeusFontFactory.getLabelFont()
        );
        lblActionDate.setText("Action Date :");
        lblActionDate.setMaximumSize(new java.awt.Dimension(80, 14));
        lblActionDate.setMinimumSize(new java.awt.Dimension(80, 14));
        lblActionDate.setPreferredSize(new java.awt.Dimension(80, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 0);
        pnlInput.add(lblActionDate, gridBagConstraints);

        txtActionDate.setFont(CoeusFontFactory.getNormalFont());
        txtActionDate.setMaximumSize(new java.awt.Dimension(129, 20));
        txtActionDate.setMinimumSize(new java.awt.Dimension(129, 20));
        txtActionDate.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        pnlInput.add(txtActionDate, gridBagConstraints);

        lblCommittee.setFont(CoeusFontFactory.getLabelFont());
        lblCommittee.setText("Committee :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(lblCommittee, gridBagConstraints);
        lblCommittee.getAccessibleContext().setAccessibleName("lblCommittee");

        cmbCommittee.setMaximumSize(new java.awt.Dimension(120, 20));
        cmbCommittee.setMinimumSize(new java.awt.Dimension(120, 20));
        cmbCommittee.setPreferredSize(new java.awt.Dimension(260, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 8, 100);
        pnlInput.add(cmbCommittee, gridBagConstraints);

        pnlInput.add(jPanel1, new java.awt.GridBagConstraints());

        cmbReviewType.setMaximumSize(new java.awt.Dimension(120, 20));
        cmbReviewType.setMinimumSize(new java.awt.Dimension(120, 20));
        cmbReviewType.setPreferredSize(new java.awt.Dimension(260, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 8, 30);
        pnlInput.add(cmbReviewType, gridBagConstraints);

        lblNotificationType.setFont(CoeusFontFactory.getLabelFont());
        lblNotificationType.setText("Submission Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(lblNotificationType, gridBagConstraints);

        lblReviewType.setFont(CoeusFontFactory.getLabelFont());
        lblReviewType.setText("Review Type:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 0);
        pnlInput.add(lblReviewType, gridBagConstraints);

        cmbNotificationType.setMaximumSize(new java.awt.Dimension(120, 20));
        cmbNotificationType.setMinimumSize(new java.awt.Dimension(120, 20));
        cmbNotificationType.setPreferredSize(new java.awt.Dimension(260, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 8, 30);
        pnlInput.add(cmbNotificationType, gridBagConstraints);

        pnlAttachments.setLayout(new java.awt.GridBagLayout());

        pnlAttachments.setPreferredSize(new java.awt.Dimension(100, 100));
        scrpnAttachments.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Attachments", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, CoeusFontFactory.getLabelFont()));
        scrpnAttachments.setMinimumSize(new java.awt.Dimension(450, 200));
        scrpnAttachments.setPreferredSize(new java.awt.Dimension(450, 200));
        tblAttachments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scrpnAttachments.setViewportView(tblAttachments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        pnlAttachments.add(scrpnAttachments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        pnlInput.add(pnlAttachments, gridBagConstraints);

        pnlAttachmentButtons.setLayout(new java.awt.GridBagLayout());

        btnAdd.setFont(CoeusFontFactory.getLabelFont());
        btnAdd.setMnemonic('A');
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(150, 150));
        btnAdd.setPreferredSize(new java.awt.Dimension(120, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        pnlAttachmentButtons.add(btnAdd, gridBagConstraints);

        btnModify.setFont(CoeusFontFactory.getLabelFont());
        btnModify.setMnemonic('M');
        btnModify.setText("Modify");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        pnlAttachmentButtons.add(btnModify, gridBagConstraints);

        btnView.setFont(CoeusFontFactory.getLabelFont());
        btnView.setMnemonic('V');
        btnView.setText("View");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 100, 0);
        pnlAttachmentButtons.add(btnView, gridBagConstraints);

        btnDelete.setFont(CoeusFontFactory.getLabelFont());
        btnDelete.setMnemonic('D');
        btnDelete.setText("Delete");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        pnlAttachmentButtons.add(btnDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        pnlInput.add(pnlAttachmentButtons, gridBagConstraints);

        btnQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
        btnQuestionnaire.setText("Questionnaire");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 0, 0);
        pnlInput.add(btnQuestionnaire, gridBagConstraints);

        getContentPane().add(pnlInput, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void btnReviewCommentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReviewCommentsActionPerformed
// Add your handling code here:
        try{
            if( reviewCommentsForm == null ) {
                reviewCommentsForm = new ReviewCommentsForm(true);
                reviewCommentsForm.setSaveToDatabase(false);
//				reviewCommentsForm.setLockSchedule(lockSchedule);
                RequesterBean requesterBean = new RequesterBean();
                protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
                protocolSubmissionInfoBean.setProtocolNumber(actionBean.getProtocolNumber());
                protocolSubmissionInfoBean.setSequenceNumber(actionBean.getSequenceNumber());
                protocolSubmissionInfoBean.setSubmissionNumber(actionBean.getSubmissionNumber());
                requesterBean.setDataObject(protocolSubmissionInfoBean);
                requesterBean.setFunctionType('t');
                AppletServletCommunicator comm = new AppletServletCommunicator(
                        CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt",
                        requesterBean);
                comm.send();
                ResponderBean responderBean = comm.getResponse();
                if(!responderBean.isSuccessfulResponse()){
                    throw new CoeusException(responderBean.getMessage());
//					reviewCommentsForm.setFunctionType(TypeConstants.DISPLAY_MODE);
                }else {
                    reviewCommentsForm.setFunctionType(TypeConstants.MODIFY_MODE);
                }
                Vector dataObjects = responderBean.getDataObjects();
                
                //Added by Vyjayanthi for IRB Enhancement - 13/08/2004 - Start
                //To Display message if protocol is not submitted
                if( dataObjects == null || dataObjects.size() == 0 ){
                    CoeusOptionPane.showInfoDialog(
                            coeusMessageResources.parseMessageKey("reviewComments_exceptionCode.3105"));
                    reviewCommentsForm = null;
                    return ;
                }
                //Added by Vyjayanthi for IRB Enhancement - 13/08/2004 - End
                protocolSubmissionInfoBean = ( ProtocolSubmissionInfoBean ) dataObjects.get(0);
            }
            if( reviewComments == null ) {
                String protocolNumber = protocolSubmissionInfoBean.getProtocolNumber();
                int seqNo = protocolSubmissionInfoBean.getSequenceNumber();
                int subNo = protocolSubmissionInfoBean.getSubmissionNumber();
                reviewCommentsForm.setFormData(protocolNumber, subNo, seqNo);
            }else{
                if( reviewCommentsForm.getFunctionType() == TypeConstants.MODIFY_MODE ) {
                    reviewCommentsForm.setLockSchedule(false);
                }
                reviewCommentsForm.setFormData(protocolSubmissionInfoBean,
                        (Vector)ObjectCloner.deepCopy(reviewComments));
            }
            if( reviewCommentsForm.getFunctionType() == TypeConstants.MODIFY_MODE
                    && lockSchedule) {
                releaseLock = true;
            }
            reviewCommentsForm.display();
            if( reviewCommentsForm.isSaveRequired() ) {
                reviewComments = reviewCommentsForm.getData();
            }
        }catch(Exception e ) {
            e.printStackTrace();
            CoeusOptionPane.showInfoDialog( e.getMessage());
        }
    }//GEN-LAST:event_btnReviewCommentsActionPerformed
		
	public boolean releaseScheduleLock(){
		return releaseLock;
	}
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnModify;
    private javax.swing.JButton btnOk;
    private javax.swing.JButton btnQuestionnaire;
    private javax.swing.JButton btnReviewComments;
    private javax.swing.JButton btnView;
    private javax.swing.JComboBox cmbCommittee;
    private javax.swing.JComboBox cmbNotificationType;
    private javax.swing.JComboBox cmbReviewType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblActionDate;
    private javax.swing.JLabel lblComments;
    private javax.swing.JLabel lblCommittee;
    private javax.swing.JLabel lblNotificationType;
    private javax.swing.JTextArea lblPrompt;
    private javax.swing.JLabel lblReviewType;
    private javax.swing.JPanel pnlAttachmentButtons;
    private javax.swing.JPanel pnlAttachments;
    private javax.swing.JPanel pnlInput;
    private javax.swing.JScrollPane scrpnAttachments;
    private javax.swing.JScrollPane scrpnlInput;
    private javax.swing.JTable tblAttachments;
    private edu.mit.coeus.utils.CoeusTextField txtActionDate;
    private javax.swing.JTextArea txtInput;
    // End of variables declaration//GEN-END:variables
	
	
	private void initialiseData() {
            
                //Added for performing Protocol Actions - start - 2                  
                populateCommittee();                             
                //Added for performing Protocol Actions - end - 2
                
                //Modified for performing Protocol Actions - start - 3
		java.awt.Component[] components = {cmbCommittee, txtInput, txtActionDate, btnOk, btnCancel, btnReviewComments,
                                                  btnAdd,btnModify,btnDelete,btnView};
                //Modified for performing Protocol Actions - end - 3
                
		ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
		setFocusTraversalPolicy(traversePolicy);
		setFocusCycleRoot(true);
		btnOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try { // Added by Jobin
                                        //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                                        boolean isValidSubmission = false;
                                        //COEUSDEV-86 : End
					if (validateData()) {
						if (txtInput != null) {
							if (txtInput.getText().trim().length() == 0) {
								userInput = defValue ;
							}else {
								userInput = txtInput.getText().trim() ;
							}
						}else {
							userInput = defValue ;
						}
                                                //Added for performing Protocol Actions - start - 4
                                                committeeId = ((ComboBoxBean)cmbCommittee.getSelectedItem()).getCode();
                                                //Added for performing Protocol Actions - end - 4
                                                //Code added for Case#3554 - Notify IRB enhancement - starts
                                                //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                                                if(actionBean.getActionTypeCode()==116){
                                                int actionCode = actionBean.getActionTypeCode();
                                                if(actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC){//COEUSDEV-86 : END
                                                    ComboBoxBean comboBox = ((ComboBoxBean)cmbNotificationType.getSelectedItem());
                                                    if(comboBox != null){
                                                        notificationType = comboBox.getCode();
                                                    }
                                                    if(notificationType == null || notificationType.equals("")){
                                                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                                                        CoeusOptionPane.showErrorDialog("Please select the notification type.");
                                                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                                                SELECT_NOTIFICATION_TYPE));
                                                        //COEUSDEV-86 : End
                                                        setRequestFocusInThread(cmbNotificationType);
                                                        return;
                                                    }
                                                    comboBox = ((ComboBoxBean)cmbReviewType.getSelectedItem());
                                                    if(comboBox != null){
                                                        reviewerType = comboBox.getCode();
                                                    }
                                                    if(reviewerType == null || reviewerType.equals("")){
                                                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                                                        CoeusOptionPane.showErrorDialog("Please select the review type.");
                                                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                                                SELECT_REVIEW_TYPE));
                                                        //COEUSDEV-86 : End
                                                        setRequestFocusInThread(cmbReviewType);
                                                        return;
                                                    }
                                                    if((committeeId == null || committeeId.equals(""))
                                                    && submissionMode == MANDATORY){
                                                        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
//                                                            CoeusOptionPane.showErrorDialog("Please select a committee.");
                                                        CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                                                                SELECT_COMMITTEE));
                                                        //COEUSDEV-86 : End
                                                            setRequestFocusInThread(cmbCommittee);
                                                            return;
                                                    }
                                                    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                                                    //When temporary submission and action details are not updated for notify IRB
                                                    //details will be updated in OSP$PROTOCOL_SUBMISSION and OSP$PROTOCOL_ACTIONS table
                                                    if(!isTempProtocolUpdated){
                                                        addTempSubmissionActionDetails();
                                                    }
                                                    //Checks all the mandatory questionnaire's are completed
                                                    isValidSubmission = validateQuestionnaireForSubmission();
                                                    if(isValidSubmission){
                                                    //COEUSDEV-86 : End
                                                        if((committeeId == null || committeeId.equals(""))
                                                        && submissionMode == 'O'){
                                                            if (CoeusOptionPane.SELECTION_NO == CoeusOptionPane.showQuestionDialog(
                                                                    coeusMessageResources.parseMessageKey(
                                                                    coeusMessageResources.parseMessageKey(NOTIFY_SUBMISSION_WITHOUT_COMMITTEE)),
                                                                    CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES)) {
                                                                setRequestFocusInThread(cmbCommittee);
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                                //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                                                //When temporary submission and action details are not updated for the following submissions
                                                //details will be updated in OSP$PROTOCOL_SUBMISSION and OSP$PROTOCOL_ACTIONS table
                                                else if(actionCode ==  IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                                                        actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE){
                                                     if(!isTempProtocolUpdated){
                                                         addTempSubmissionActionDetails();
                                                     }
                                                    //Checks all the mandatory questionnaire's are completed
                                                    isValidSubmission = validateQuestionnaireForSubmission();
                                                }else{
                                                    isValidSubmission = true;
                                                }
                                                if(isValidSubmission){//COEUSDEV-86 : End
                                                    //Code added for Case#3554 - Notify IRB enhancement - ends
                                                    //Added by Jobin for the action Date - start
                                                    //	if (!("").equals(txtActionDate.getText())) {
                                                    actionDate = new java.sql.Date(dtFormat.parse(
                                                            dtUtils.restoreDate(txtActionDate.getText(),"/-:,.")).getTime());
                                                    
//                                                    System.out.println("action date=>"+actionDate);
                                                    //	}//end
                                                    continueAction = true ;
                                                     dispose();
                                                    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                                                    //Lock for the protocol is released when the submission window closed and protocol not in edit mode
                                                    if(!actionBean.isProtocolEditable()){
                                                        releaseUpdateProtocolLock();
                                                    }
                                                    //COEUSDEV : End
                                                }
					} else {
						CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
						setRequestFocusInThread(txtActionDate);
					}
				} catch(Exception ex) {
					CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
					ex.printStackTrace() ;
				}
			} // Jobin -end 
		});
		
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - starts
//				userInput = "" ; //defValue ;
//				continueAction = false ;
//				if( releaseScheduleLock() ) {
//					releaseUpdateLock();
//				}
//				dispose() ;
                            close();
                            
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - ends
			}
		});
		addEscapeKeyListener( new AbstractAction("escPressed"){
			public void actionPerformed(ActionEvent ae){
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - starts
//				userInput = "" ; //defValue ;
//				continueAction = false ;
//				if( releaseScheduleLock() ) {
//					releaseUpdateLock();
//				}
//				dispose() ;
                            close();
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - ends
			}
		});
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			public void windowActivated(WindowEvent we){
                                //Modified for performing Protocol Actions - start - 5
                                if(!cmbCommittee.isVisible()){
                                    txtInput.requestFocusInWindow();
                                }else{
                                    cmbCommittee.requestFocusInWindow();
                                }
                                //Modified for performing Protocol Actions - start - 5
                                //Code added for Case#3554 - Notify IRB enhancement - starts
                                if(actionBean.getActionTypeCode()==IacucProtocolActionsConstants.NOTIFY_IACUC){
                                    cmbNotificationType.requestFocusInWindow();
                                }
                                //Code added for Case#3554 - Notify IRB enhancement - ends
			}
			public void windowClosing(WindowEvent we){
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - starts
//				userInput = "" ; //defValue ;
//				continueAction = false ;
//				if( releaseScheduleLock() ) {
//					releaseUpdateLock();
//				}
//				dispose() ;
                            close();
                            //Code commenetd and modified for Case#3554 - Notify IRB enhancement - ends
			}
		});
                
                //Added for case#3046 - Notify IRB attachments - start
//                btnBrowse.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        CoeusFileChooser fileChooser = new CoeusFileChooser(CoeusGuiConstants.getMDIForm());
//                        fileChooser.setAcceptAllFileFilterUsed(true);
//                        fileChooser.showFileChooser();
//                        if(fileChooser.isFileSelected()){
//                              fileName = fileChooser.getSelectedFile();
//                              fileBytes = fileChooser.getFile();
//                              File file = fileChooser.getFileName();
//                              String path = file.getName();
//                              txtAttachment.setText(path);                              
//                              fileName = path;                                                         
//                        }
//                        if(txtAttachment.getText() != null && !txtAttachment.getText().equals("")){
//                            btnView.setEnabled(true);
//                        }else{
//                            btnView.setEnabled(false);
//                        }
//                    }                    
//                }); 
//                
		btnView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                                try{
                                    //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//                                    viewSubmissionDocument();			
                                    int selectedRow = tblAttachments.getSelectedRow();
                                    if(selectedRow < 0){
                                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(SELECT_DOCUMENT_TO_VIEW));
                                    }else{
                                        viewSubmissionDocument();
                                    }
                                }catch(Exception ex){
                                    ex.printStackTrace();
                                }
			}
		});                
		//Added for case#3046 - Notify IRB attachments - end
                
		btnOk.setFont(CoeusFontFactory.getLabelFont()) ;
		btnCancel.setFont(CoeusFontFactory.getLabelFont()) ;
		
		lblComments.setFont(CoeusFontFactory.getLabelFont()) ;
                    
		
		txtInput.setPreferredSize(new Dimension(100, 150)) ;
		
		txtInput.setLineWrap(true) ;
		txtInput.setWrapStyleWord(true) ;
		txtInput.setDocument(new LimitedPlainDocument( 2000 ));
		txtInput.setFont(CoeusFontFactory.getNormalFont()) ;
		
		lblPrompt.setWrapStyleWord(true);
		lblPrompt.setLineWrap(true);
		lblPrompt.setDocument(new LimitedPlainDocument( 150 ));
		lblPrompt.setFont(CoeusFontFactory.getLabelFont()) ;
		
		lblPrompt.setText(prompt);
		txtInput.setText(defValue);
		//getting today's date added by Jobin - start
//		todayDate = dtUtils.formatDate(
//		(new java.sql.Timestamp(
//		(new java.util.Date()).getTime())).toString(),
//		"dd-MMM-yyyy");
                todayDate = dtUtils.formatDate(CoeusUtils.getDBTimeStamp().toString(),"dd-MMM-yyyy");
		txtActionDate.setDocument(new LimitedPlainDocument(11));
		txtActionDate.setFont(CoeusFontFactory.getNormalFont()) ;
		txtActionDate.setText(todayDate);
		txtActionDate.setDocument(new LimitedPlainDocument(11));
		txtActionDate.addFocusListener(new CustomFocusAdapter());// Jobin - end
		
	}
	//Added by Jobin to validate the action date field
	
	public boolean validateData() throws Exception {
		if (txtActionDate.getText()== null) {
			/* Application Date doesn't have any value */
			return false;
			
		} else if (txtActionDate.getText().trim().length() <= 0) {
			return false ;
		} else {
			Date applnDate = null;
			Date apprDate = null;
			Date expDate = null;
			
			String oldDate;
			String convertedDate ;
			if((txtActionDate.getText() != null)
			&& (txtActionDate.getText().trim().length() > 0)){
				
				convertedDate = dtUtils.formatDate(txtActionDate.getText(),
				"/-:,." , "dd-MMM-yyyy");
				
				if (convertedDate==null){
					oldDate = dtUtils.restoreDate(txtActionDate.getText(),"/-:.,");
					if(oldDate == null || oldDate.equals(txtActionDate.getText())){
						
						return false;
					}
				}
				apprDate = dtFormat.parse(
				dtUtils.restoreDate(txtActionDate.getText(),"/:-.,"));
				
				if(apprDate == null) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void showForm(){
            try {
		setSize(650, 520);
		setLocation(CoeusDlgWindow.CENTER);
		setResizable(false);
                  int actionCode = actionBean.getActionTypeCode();
                  if(actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC ||
                          actionCode == IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD || 
                          actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE){
                      setUIForAttachemnts(true);
                      enableDocumentButtons(false);
                      lblPrompt.setVisible(false);
                      lblNotificationType.setVisible(true);
                      lblReviewType.setVisible(true);
                      cmbNotificationType.setVisible(true);
                      cmbReviewType.setVisible(true);
                      btnReviewComments.setVisible(false);
                      getLookupDetails();
                      ComboBoxBean comboBean;

                      if( typeQualifiers != null ){
                          for ( int loopIndex = 0 ; loopIndex < typeQualifiers.size(); loopIndex++ ) {
                              comboBean = (ComboBoxBean)typeQualifiers.get(loopIndex);
                              cmbNotificationType.addItem(comboBean);
                          }
                      }
                      if( reviewTypes != null ){
                          for ( int loopIndex = 0 ; loopIndex < reviewTypes.size();
                          loopIndex++ ) {
                              comboBean = (ComboBoxBean)reviewTypes.get(loopIndex);
                              cmbReviewType.addItem(comboBean);
                          }
                      }
                      getDefaultSubmissionMode();
                      java.awt.Component[] components = {cmbNotificationType, cmbReviewType, cmbCommittee, txtInput, txtActionDate, btnAdd, btnModify,
                      btnDelete,btnView , btnOk, btnCancel};
                      ScreenFocusTraversalPolicy traversePolicy = new ScreenFocusTraversalPolicy( components );
                      if(submissionMode == DENIED){
                          java.awt.Component[] newComponents = {cmbNotificationType, cmbReviewType, txtInput, txtActionDate,  btnAdd, btnModify,
                          btnDelete,btnView,btnOk, btnCancel};
                          traversePolicy = new ScreenFocusTraversalPolicy( newComponents );
                          setFocusTraversalPolicy(traversePolicy);
                          lblCommittee.setVisible(false);
                          cmbCommittee.setVisible(false);
                          int scrpnAttachmentsWidth = 450;
                          pnlAttachments.setMinimumSize(new Dimension(450,200));
                          pnlAttachments.setMaximumSize(new Dimension(450,200));
                          pnlAttachments.setPreferredSize(new Dimension(450,200));
                          btnAdd.setPreferredSize(new Dimension(120, 23));
                          btnAdd.setMinimumSize(new Dimension(120, 23));
                          btnAdd.setMaximumSize(new Dimension(120, 23));
                          setSize(600, 520);
                          
                      }
                       if(actionCode == IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                              actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE){
                          lblReviewType.setVisible(false);
                          cmbReviewType.setVisible(false);
                          lblNotificationType.setVisible(false);
                          cmbNotificationType.setVisible(false);
                       }
                      
                      
                      //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                      //Based on the Notificaiton type selection rule question rule will evaluvated
                      selectedIndex = cmbNotificationType.getSelectedIndex();
                      cmbNotificationType.addItemListener(new ItemListener() {
                          public void itemStateChanged(ItemEvent e) {
                              if(e.getStateChange() == e.DESELECTED){
                                  return ;
                              }
                              int newSelectedIndex = cmbNotificationType.getSelectedIndex();
                              if(selectedIndex == -1 && e.getStateChange() == e.SELECTED){
                                  selectedIndex = newSelectedIndex;
                              } else if(e.getStateChange() == e.SELECTED){
                                  //When Notification type is changed, checks any question is answered for the current notification type
                                  //If answered and if user press 'Yes' then the temporary protocol details in OSP$PROTOCOL_SUBMISSION, OSP$PROTOCOL_ACTIONS ,
                                  //OSP$QUENSTIONNAIRE_ANS_HEADER and OSP$QUESTIONNAIRE_ANSWERS are deleted.
                                  //Whenever user selectes Questionnaire button or ok button temporary submission details are updated.
                                  //If user selects 'No' then previous notificaiton type is selcted
                                  if(isAnyQuestionAnswered()){
                                      int option = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(NOTIFICATION_TYPE_CHANGE_WITH_ANS_QNR)
                                              ,CoeusOptionPane.OPTION_YES_NO,2);
                                      switch( option ) {
                                          case (CoeusOptionPane.SELECTION_YES):
                                              setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
                                              selectedIndex = newSelectedIndex;
                                              // Removes all the temporary submission details with temporary questionnaire answer details
                                              cleanTemporarySubmissionData();
                                              setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
                                              break;
                                          case(CoeusOptionPane.SELECTION_NO):
                                              int selectPreviousIndex = selectedIndex;
                                              selectedIndex = -1;
                                              cmbNotificationType.setSelectedIndex(selectPreviousIndex);
                                              break;
                                          default:
                                              break;
                                      }
                                  }else{
                                      cleanTemporarySubmissionData();
                                  }
                              }
                          }
                      });
                      //COEUSDEV-86 : END
                      setFocusTraversalPolicy(traversePolicy);
                      setFocusCycleRoot(true);
                      if(actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC){
                          setRequestFocusInThread(cmbNotificationType);
                      }else if(actionCode == IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                              actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE){
                          setRequestFocusInThread(cmbCommittee);
                      }
                  }else {
                      btnQuestionnaire.setVisible(false);
                      //COEUSQA-3183 IRB - Review Comments should not be available when performing a withdraw - Start
                      if(actionCode == IacucProtocolActionsConstants.WITHDRAWN){
                          btnReviewComments.setEnabled(false);
                      }else{
                          btnReviewComments.setEnabled(true);
                      }
                      //COEUSQA-3183 IRB - Review Comments should not be available when performing a withdraw - End
                      lblNotificationType.setVisible(false);
                      lblReviewType.setVisible(false);
                      lblCommittee.setVisible(false);
                      cmbCommittee.setVisible(false);
                      cmbNotificationType.setVisible(false);
                      cmbReviewType.setVisible(false);
                      setSize(500, 325);
                  }
                  
                  //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
                  //When review button is not availble for the submission and questionnaire is available
                  //Questionnaire button is moved next to the cancel button
                  if(!btnReviewComments.isVisible() && btnQuestionnaire.isVisible()){
                      java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
                      gridBagConstraints.gridx = 2;
                      gridBagConstraints.gridy = 3;
                      gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                      gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
                      gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
                      pnlInput.add(btnQuestionnaire, gridBagConstraints);
                      btnAdd.setPreferredSize(new Dimension(150, 23));
                  }
                  //COEUSDEV-86 : End
                //Code added for Case#3554 - Notify IRB enhancement- ends
                //Added for case#3046 - Notify IRB attachments - end
                if(todayDate==null) todayDate = dtUtils.formatDate(CoeusUtils.getDBTimeStamp());
		txtActionDate.setText(todayDate);        
                //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
                //Before opening the window , if the protocol is in edit mode, then protocol is not locked
                //else protocol is locked
//                show();
                if(!actionBean.isProtocolEditable()){
                    if(lockProtocol()){
                        show();
                    }
                }else{
                    show();
                }
                //COEUSDEV-86 : End
                
            } catch(Exception ex) {
                ex.printStackTrace();
                CoeusOptionPane.showErrorDialog(ex.getMessage());
            }
	}
	
	public String getUserInput() {
		return userInput ;
	}
	
	public void setUserInput(String userInput ) {
		this.userInput = userInput ;
	}
	
	
	public boolean performAction() {
		return continueAction ;
	}
	
	public void setContinueAction(boolean continueAction) {
		this.continueAction = continueAction ;
	}
	
	/** Getter for property lockSchedule.
	 * @return Value of property lockSchedule.
	 *
	 */
	public boolean isLockSchedule() {
		return lockSchedule;
	}
	
	/** Setter for property lockSchedule.
	 * @param lockSchedule New value of property lockSchedule.
	 *
	 */
	public void setLockSchedule(boolean lockSchedule) {
		this.lockSchedule = lockSchedule;
	}
	
	/** Getter for property reviewComments.
	 * @return Value of property reviewComments.
	 *
	 */
	public java.util.Vector getReviewComments() {
		return reviewComments;
	}
	
	/** Getter for property actionBean.
	 * @return Value of property actionBean.
	 *
	 */
	public ProtocolActionsBean getActionBean() {
		return actionBean;
	}
	
	/** Setter for property actionBean.
	 * @param actionBean New value of property actionBean.
	 *
	 */
	public void setActionBean(ProtocolActionsBean actionBean) {
		this.actionBean = actionBean;
	}
	private void releaseUpdateLock() {
		String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
		RequesterBean requester = new RequesterBean();
		String lockedScheduleId = protocolSubmissionInfoBean.getScheduleId();
		if( lockedScheduleId != null ) {
			requester.setDataObject(lockedScheduleId);
			requester.setFunctionType('Z');
			AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
			//comm.releaseUpdateLock(refId,"/scheduleMaintSrvlt");
			comm.send();
			ResponderBean res = comm.getResponse();
			if (res != null && !res.isSuccessfulResponse()){
				CoeusOptionPane.showErrorDialog(res.getMessage());
				return;
			}
		}
	}
	
	/**
	 * Getter for property actionDate.
	 * @return Value of property actionDate.
	 */
	public java.sql.Date getActionDate() {
		return actionDate;
	}
	
	/**
	 * Setter for property actionDate.
	 * @param actionDate New value of property actionDate.
	 */
	public void setActionDate(java.sql.Date actionDate) {
		this.actionDate = actionDate;
	}
	
	/**
	 * Custom focus adapter which is used for text fields which consists of
	 * date values. This is mainly used to format and restore the date value
	 * during focus gained / focus lost of the text field.Added by Jobin For Action Date Field
	 */
	private class CustomFocusAdapter extends FocusAdapter{
		//hols the data display Text Field
		CoeusTextField dateField;
		String strDate = "";
		String oldData = "";
		boolean temporary = false;
		
		public void focusGained(FocusEvent fe){
			if (fe.getSource() instanceof CoeusTextField){
				dateField = (CoeusTextField)fe.getSource();
				if ( (dateField.getText() != null)
				&&  (!dateField.getText().trim().equals(""))) {
					oldData = dateField.getText();
					String focusDate = dtUtils.restoreDate(
					dateField.getText(),"/-:,.");
					dateField.setText(focusDate);
				}
			}
		}
		
		public void focusLost(FocusEvent fe){
			if (fe.getSource() instanceof CoeusTextField){
				dateField = (CoeusTextField)fe.getSource();
				
				temporary = fe.isTemporary();
				if ( (dateField.getText() != null)
				&&  (!dateField.getText().trim().equals(""))
				&& (!temporary) ) {
					strDate = dateField.getText();
					String convertedDate =
					dtUtils.formatDate(dateField.getText(), "/-:,." ,
					"dd-MMM-yyyy");
					if (convertedDate==null){
						CoeusOptionPane.showErrorDialog("Please enter a valid date in mm/dd/yyyy format.");
						setRequestFocusInThread(txtActionDate);
						dateField.setText(oldData);
						temporary = true;
					}else {
						dateField.setText(convertedDate);
						temporary = false;
					}
				}
			}
		}
	}
	
    public void disableReviewCommBtn(boolean flag){
        this.btnReviewComments.setEnabled(!flag);
    }
    
    //Added for Case id COEUSQA-1724_Reviewer View of Protocol start
    /**
     *  The method displayReviewCommBtn display and hide the reviewcommets button based on the
     *  boolean parameter passing to the method.
     *  @param displayFlag boolean
     */
    
    public void displayReviewCommBtn(boolean displayFlag){
        this.btnReviewComments.setVisible(displayFlag);
    }
    //Added for Case id COEUSQA-1724_Reviewer View of Protocol end
    
	/*to set the focus in the respective fields*/
    private void setRequestFocusInThread(final Component component) {
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                component.requestFocusInWindow();
            }
        });
    }
    
    /**
     * Added for performing Protocol Actions - start - 6
     * This method gets the committee list and sets 
     * the collection to committee combo box
     * @return void
     */
    public void populateCommittee(){        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/scheduleMaintSrvlt";
        RequesterBean requester = new RequesterBean();
        requester.setFunctionType(GET_IACUC_COMMITTEE_DATA);
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);        
        comm.send();
        ResponderBean res = comm.getResponse();
        if (res != null){
            cvCommittee = (CoeusVector)res.getDataObject();
            if(cvCommittee != null && cvCommittee.size() > 0){
                cmbCommittee.setModel(new DefaultComboBoxModel(cvCommittee));
            }
        }
    }

    /**
     * Getter method for committeeId
     */
    public String getCommitteeId() {
        return committeeId;
    }

    /**
     * Setter method for committeeId
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }
    
    public void setVisibility(){
        lblCommittee.setVisible(false);
        cmbCommittee.setVisible(false);
        setSize(500, 300);
    }
    //Added for performing Protocol Actions - end - 6
    //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    //Files are handled through ProtocolActionDocumentbean
    //Added for case#3046 - Notify IRB attachments - start
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public byte[] getFileBytes() {
//        return fileBytes;
//    }
//
//    public void setFileBytes(byte[] fileBytes) {
//        this.fileBytes = fileBytes;
//    }
    
    /*
     * Method to get uploaded documents for the action
     * @reutn cvUploadDocuments
     */
    public CoeusVector getUploadDocuments(){
        return this.cvUploadDocuments;
    }
    //COEUSDEV-328 : End
    /**
     * This method facilitates the view of blob data
     * @throws Exception
     */
    private void viewSubmissionDocument() throws Exception{
        
        String templateUrl =  null;
        DocumentBean documentBean = new DocumentBean();
        RequesterBean requesterBean = new RequesterBean();
        //COEUSDEV-327
        int selectedRow = tblAttachments.getSelectedRow();
        ProtocolActionDocumentBean actionDocumentBean = (ProtocolActionDocumentBean)cvUploadDocuments.get(selectedRow);
        Map map = new HashMap();
        map.put("DOCUMENT_TYPE", "SUBMISSION_DOC");
        //Modified for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
        //Selected row file bytes and file name are set
//        map.put("FILE_BYTES", fileBytes);
//        map.put("FILE_NAME", fileName);
        map.put("FILE_BYTES", actionDocumentBean.getFileBytes());
        map.put("FILE_NAME", actionDocumentBean.getFileName());
        //COEUSDEV-328 : End
        map.put(DocumentConstants.READER_CLASS, "edu.mit.coeus.iacuc.ProtocolDocumentReader");
        documentBean.setParameterMap(map);
        requesterBean.setDataObject(documentBean);
        requesterBean.setFunctionType(DocumentConstants.GENERATE_STREAM_URL);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(STREMING_SERVLET, requesterBean);
        comm.send();
        ResponderBean responderBean = comm.getResponse();
        responderBean = comm.getResponse();
        if(!responderBean.isSuccessfulResponse()){
            throw new CoeusException(responderBean.getMessage(),0);
        }
        map = (Map)responderBean.getDataObject();
        templateUrl = (String)map.get(DocumentConstants.DOCUMENT_URL);       
        
        try{
            URL urlObj = new URL(templateUrl);
            URLOpener.openUrl(urlObj);
        }catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();            
        }catch(Exception ue) {
            ue.printStackTrace() ;
        }        
    }        
    //Added for case#3046 - Notify IRB attachments - end
    
    /**
     * This method is used to fetch all the lookup details required for protocol
     * submission.
     */
    private void getLookupDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL
                + "/iacucProtocolSubSrvlt";
        ProtocolInfoBean protocolInfoBean = new ProtocolInfoBean();
        protocolInfoBean.setProtocolNumber(actionBean.getProtocolNumber());
        /* connect to the database and get the Schedule Details for the given
           schedule id */
        RequesterBean request = new RequesterBean();
        request.setFunctionType('A');
        request.setId(actionBean.getProtocolNumber());
        request.setDataObject(protocolInfoBean);
        request.setRequestedForm("104");
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        Vector dataObjects = response.getDataObjects();
        String committeeId = null;
        String committeeName = null;
        if (response.isSuccessfulResponse()) {
            /* get all the lookup details required for submissionTypes,
               submissionTypeQualifiers, protocol review types and protocol
               reviewer types */
            if(dataObjects.get(2) != null){
                reviewTypes = new ArrayList((Vector)dataObjects.get(2));
            } else {
                reviewTypes = new ArrayList();
            }
            Vector vCommitte = (Vector) dataObjects.get(8);
            if(dataObjects.get(12) != null){
                typeQualifiers = new ArrayList((Vector)dataObjects.get(12));
            } else {
                typeQualifiers = new ArrayList();
            }
            if (vCommitte != null && vCommitte.size() > 0) {
                committeeId = (String) vCommitte.get(0);
                committeeName = (String) vCommitte.get(1);
                if(committeeId != null){
                    cmbCommittee.setSelectedItem(new ComboBoxBean(committeeId, committeeName));
                }                
            }
        }else{
            if( response.getDataObject() != null){
                if( response.getDataObject() instanceof CoeusException){
                    throw (CoeusException)response.getDataObject();
                }
            }else{
                String message = response.getMessage();
                throw new Exception(message);
            }
        }
    }
    
    /**
     * To get the committee selection mode.
     * @throws java.lang.Exception
     */
    public void getDefaultSubmissionMode() throws Exception{
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
        RequesterBean request = new RequesterBean();
        request.setDataObject("IACUC_COMM_SELECTION_DURING_SUBMISSION");
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null) {
            if (response.isSuccessfulResponse()) {
                submissionMode = response.getId().charAt(0) ;
            }
        }
    }   

    /**
     * Getter method for notificationType
     * @return notificationType
     */
    public String getNotificationType() {
        return notificationType;
    }

    /**
     * Setter method for notificationType
     * @param notificationType 
     */
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
    
    /**
     * To close the dialogue window
     */
    private void close(){
        userInput = "" ;
        int option = CoeusOptionPane.showQuestionDialog( "Do you want to cancel the submission?",
                CoeusOptionPane.OPTION_YES_NO,
                CoeusOptionPane.DEFAULT_YES);
        if(option == CoeusOptionPane.SELECTION_YES) {
            continueAction = false;
            dispose();
            //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
            //When user closes the window for the notify IRB, Request to close, Request for suspension
            // Request to close-enrollment, Request for data nalysis only, Request for re-open enrollment
            // and Notify IRB temporary submission details are removed and lock for the protocol is also released
            int actionCode = actionBean.getActionTypeCode();
            if(actionCode ==  IacucProtocolActionsConstants.REQUEST_TO_LIFT_HOLD ||
                    actionCode == IacucProtocolActionsConstants.REQUEST_TO_DEACTIVATE ||
                    actionCode == IacucProtocolActionsConstants.NOTIFY_IACUC){
                cleanTemporarySubmissionData();
            }
            //When protocol is opnened in edit mode lock is not released  
            if(!actionBean.isProtocolEditable()){
                releaseUpdateProtocolLock();
            }
            //COEUSDEV-86 : End
        }
    }

    /**
     * Getter method for reviewerType
     * @return reviewerType
     */
    public String getReviewerType() {
        return reviewerType;
    }

    /**
     * Setter method for reviewerType
     * @param reviewerType 
     */
    public void setReviewerType(String reviewerType) {
        this.reviewerType = reviewerType;
    }
    
    private void setUIForAttachemnts(boolean isVisible){
        pnlAttachments.setVisible(isVisible);
        scrpnAttachments.setVisible(isVisible);
        tblAttachments.setVisible(isVisible);
        btnAdd.setVisible(isVisible);
        btnModify.setVisible(isVisible);
        btnDelete.setVisible(isVisible);
        btnView.setVisible(isVisible);
        if(isVisible){
            registerComponents();
        }
    }
    
    //Added for COEUSDEV-328 :  Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    /*
     * Method to register attachment components
     */
    private void registerComponents(){
        btnAdd.addActionListener(this);
        btnModify.addActionListener(this);
        btnDelete.addActionListener(this);
        protocolUploadDocTableModel = new ProtocolUploadDocTableModel();
        protocolUploadDocRenderer = new ProtocolUploadDocRenderer();
        tblAttachments.setModel(protocolUploadDocTableModel);
        try{
            setTableEditors();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param actionEvent 
     */
    public void actionPerformed(ActionEvent actionEvent){
         Object actionSource = actionEvent.getSource();
        if(actionSource.equals(btnAdd)){
             performAddAction();
        }else if(actionSource.equals(btnModify)){
             performModifyAction();
        }else if(actionSource.equals(btnDelete)){
             int selectedRow = tblAttachments.getSelectedRow();
             if(selectedRow == -1) {
                 CoeusOptionPane.showInfoDialog(
                         coeusMessageResources.parseMessageKey(SELECT_DOCUMENT_TO_DELETE));
             }else{
                 int selectedOption = CoeusOptionPane.showQuestionDialog(
                         coeusMessageResources.parseMessageKey(DOCUMENT_DELETE_CONFIRM),
                         CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                 //When yes option is selected, document will be deleted from collection and also from table
                 if(selectedOption == CoeusOptionPane.SELECTION_YES){
                     performDeleteAction();
                 }
             }
        }
        //Modified for COEUSDEV-86 : Questionnaire for a Submission - Start
        //If the temporary submission details are not updated, it will be updated when user clicks questionnaire button
        else if(actionSource.equals(btnQuestionnaire)){
             try{
                 if(!isTempProtocolUpdated){
                     addTempSubmissionActionDetails();
                 }
                 showQuestionnaireForSubmissionWindow();
             }catch(Exception e){
               CoeusOptionPane.showInfoDialog(e.getMessage());
             }
        }
         //COEUSDEV-86 : End
    }
    
    /**
     * set The protocol attachment editor and headers
     * @throws CoeusException If Any exception occurs
     */
    private void setTableEditors() throws CoeusException {
        JTableHeader tableHeader = tblAttachments.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setMaximumSize(new Dimension(100, 25));
        tableHeader.setMinimumSize(new Dimension(100, 25));
        tableHeader.setPreferredSize(new Dimension(100, 25));
        tableHeader.setFont(CoeusFontFactory.getLabelFont());
        tblAttachments.setRowHeight(22);
        tblAttachments.setShowHorizontalLines(true);
        tblAttachments.setShowVerticalLines(true);
        tblAttachments.setOpaque(false);
        tblAttachments.setSelectionMode(
                DefaultListSelectionModel.SINGLE_SELECTION);
        TableColumn columnDetails;
        int []size = {DESCRIPTION_COLUMN_WIDTH,
        LAST_UPDATED_COLUMN_WIDTH,
        UPDATED_BY_COLUMN_WIDTH};
        for(int index = 0; index < size.length; index++) {
            columnDetails = tblAttachments.getColumnModel().getColumn(index);
            columnDetails.setPreferredWidth(size[index]);
            columnDetails.setMinWidth(size[index]);
            columnDetails.setCellRenderer(protocolUploadDocRenderer);
        }
    }
    
     /**
     * Class for Upload Document model
     */
    private class ProtocolUploadDocTableModel extends AbstractTableModel{
        private String [] colNames = {"Description","Last Updated","Updated By"};
        private Class colClass[]={String.class,String.class,String.class};
        private Vector cvUploadDocuments;
        
        /**
         * Method to make cell editable
         * @param row disable this row
         * @param col disable this column
         * @return boolean for cell editable state
         */
        public boolean isCellEditable(int row, int col){
            return false;
        }
        /**
         * method to count total column
         * @return number of column
         */
        public int getColumnCount() {
            return colNames.length;
        }
        /**
         * Method to get column name
         * @param col column number to get name
         * @return name of the column
         */
        public String getColumnName(int col){
            return colNames[col];
        }
        /**
         * method to get column class
         * @param col number of column
         * @return Class of column
         */
        public Class getColumnClass(int col){
            return colClass[col];
        }
        
        /**
         * method to get total rows
         * @return total number of rows
         */
        public int getRowCount() {
            if(cvUploadDocuments == null || cvUploadDocuments.size() == 0){
                return 0;
            }
            return cvUploadDocuments.size();
            
        }
        /**
         * method to set data in table
         * @param vecListData contains data for table
         */
        public void setData(Vector cvUploadDocuments){
            this.cvUploadDocuments = cvUploadDocuments;
        }
        /**
         * method to get value
         * @param rowIndex which row
         * @param columnIndex which column
         * @return object value related to this row and column
         */
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(cvUploadDocuments != null && cvUploadDocuments.size() > 0){
               ProtocolActionDocumentBean attachmentDetails
                        = (ProtocolActionDocumentBean)cvUploadDocuments.get(rowIndex);
                switch(columnIndex){
                    case DESCRIPTION_COLUMN:
                        return attachmentDetails.getDescription();
                    case LAST_UPDATED_COLUMN:
                        return CoeusDateFormat.format(attachmentDetails.getUpdateTimestamp().toString());
                    case UPDATED_BY_COLUMN:
                        return attachmentDetails.getUpdateUserName();
                }
            }
            return CoeusGuiConstants.EMPTY_STRING;
        }
    }
    /**
     * Class for Upload Document renderer
     */
    private class ProtocolUploadDocRenderer extends DefaultTableCellRenderer{
        private CoeusLabel lblComponent;
        
        /**
         * construtor for Upload Document renderer
         */
        public ProtocolUploadDocRenderer(){
            lblComponent = new CoeusLabel();
            lblComponent.setOpaque(true);
            lblComponent.setBorder(new EmptyBorder(0,0,0,0));
            lblComponent.setFont(CoeusFontFactory.getNormalFont());
            
        }
        /**
         * Method to get table cell component
         * @param table table object
         * @param value component value
         * @param isSelected component state
         * @param hasFocus component focus
         * @param row on which row exist
         * @param col on which column exist
         * @return object component
         */
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col){
            switch (col){
                case DESCRIPTION_COLUMN:
                case LAST_UPDATED_COLUMN:
                case UPDATED_BY_COLUMN:
                    lblComponent.setHorizontalAlignment(CoeusLabel.LEFT);
                    lblComponent.setFont(CoeusFontFactory.getNormalFont());
                    if(isSelected){
                        lblComponent.setBackground(java.awt.Color.YELLOW);
                        lblComponent.setForeground(java.awt.Color.black);
                    }else{
                        lblComponent.setBackground(javax.swing.UIManager.getDefaults().getColor("Panel.background"));
                        lblComponent.setForeground(java.awt.Color.black);
                    }
                    value = (value==null ? "" : value);
                    lblComponent.setText(value.toString().trim());
                    return lblComponent;

            }
            return lblComponent;
        }
        
    }
    
    /*
     * Method to upload a new document with description
     *
     */
    private void performAddAction(){
        ProtocolActionDocumentBean protocolActionDocumentBean = new ProtocolActionDocumentBean();
        ProtocolActionUploadDocumentForm uploadDocumentForm = new ProtocolActionUploadDocumentForm(protocolActionDocumentBean,ADD_DOCUMENT);
        uploadDocumentForm.showWindow();
        if(uploadDocumentForm.isDocumentLoad()){
            enableDocumentButtons(true);
            cvUploadDocuments.add(protocolActionDocumentBean);
            protocolUploadDocTableModel.setData(cvUploadDocuments);
            protocolUploadDocTableModel.fireTableDataChanged();
            tblAttachments.setRowSelectionInterval(tblAttachments.getRowCount()-1,tblAttachments.getRowCount()-1);
            enableDocumentButtons(true);
        }
        
    }

    /*
     * Method to modify a selected document
     */
    private void performModifyAction(){
        int selectedRow = tblAttachments.getSelectedRow();
        if(selectedRow < 0){
            CoeusOptionPane.showInfoDialog(
                    coeusMessageResources.parseMessageKey(SELECT_DOCUMENT_TO_MODIFY));
        }else{
            ProtocolActionDocumentBean protocolActionDocumentBean = (ProtocolActionDocumentBean)cvUploadDocuments.get(selectedRow);;
            ProtocolActionUploadDocumentForm uploadDocumentForm = new ProtocolActionUploadDocumentForm(protocolActionDocumentBean,MODIFY_DOCUMENT);
            uploadDocumentForm.showWindow();
            protocolUploadDocTableModel.fireTableDataChanged();
            tblAttachments.setRowSelectionInterval(selectedRow,selectedRow);
        }
    }
    
    /*
     * Method to delete selected document in attachment table
     */
    private void performDeleteAction(){
        int selectedRow = tblAttachments.getSelectedRow();
        cvUploadDocuments.remove(selectedRow);
        protocolUploadDocTableModel.setData(cvUploadDocuments);
        protocolUploadDocTableModel.fireTableDataChanged();
        if(selectedRow >0 ){
            tblAttachments.setRowSelectionInterval(selectedRow-1,selectedRow-1);
        //When first row document is deleted,first visible is selected
        }else if(tblAttachments.getRowCount() > 0){
            tblAttachments.setRowSelectionInterval(0,0);
        }
    }
    
    /*
     * Method to enable Modify, Delete and View buttons for attachment
     * @param enableDocumentButtons
     */
    private void enableDocumentButtons(boolean enableDocumentButtons){
        btnModify.setEnabled(enableDocumentButtons);
        btnDelete.setEnabled(enableDocumentButtons);
        btnView.setEnabled(enableDocumentButtons);
    }
    //COEUSDEV-328 : End
    
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    /*
     * Method to show the questionnaire window in dialog window
     */
    private void showQuestionnaireForSubmissionWindow()throws Exception{
        questionAnswersController = new QuestionAnswersController(actionBean.getProtocolNumber(),true);
        questionAnswersController.setFunctionType(TypeConstants.MODIFY_MODE);
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = new QuestionnaireAnswerHeaderBean();
        questionnaireAnswerHeaderBean.setModuleItemCode(ModuleConstants.IACUC_MODULE_CODE);
        questionnaireAnswerHeaderBean.setModuleSubItemCode(PROTOCOL_SUBMISSION_SUB_MODULE);
        questionnaireAnswerHeaderBean.setModuleItemKey(actionBean.getProtocolNumber()+APPEND_TEMP_SUB_CHAR_AFTER_PROTO_NUMBER);
        questionnaireAnswerHeaderBean.setModuleSubItemKey(tempProtoSubmissionNumber+"");
        questionAnswersController.setFormData(questionnaireAnswerHeaderBean);
        CoeusVector cvFormData = (CoeusVector)questionAnswersController.getFormData();
        questionAnswersForm = ((QuestionAnswersForm)questionAnswersController.getControlledUI());
        questionAnswersForm.setFocusable(true);
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                questionAnswersForm.requestFocusInWindow();
            }
        });
        questionAnswersForm.btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(questionAnswersController.checkDataModified()){
                    checkForQuestionnaireSave();
                }else{
                    dlgProtoSubQuestionnaire.dispose();
                }
            }
        });
        
        if(cvFormData != null && cvFormData.size() > 0){
            String title = actionBean.getActionTypeDescription()+" Questionnaire - "+actionBean.getProtocolNumber();
            dlgProtoSubQuestionnaire= new CoeusDlgWindow(this, title, true);
            dlgProtoSubQuestionnaire.setFocusable(true);
            dlgProtoSubQuestionnaire.setEnabled(true);
            dlgProtoSubQuestionnaire.addEscapeKeyListener(new AbstractAction("escPressed"){
                public void actionPerformed(ActionEvent ae){
                    if(questionAnswersController.checkDataModified()){
                        checkForQuestionnaireSave();
                    }else{
                        dlgProtoSubQuestionnaire.dispose();
                    }
                }
            });
            dlgProtoSubQuestionnaire.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dlgProtoSubQuestionnaire.addWindowListener( new WindowAdapter() {
                public void windowClosing(WindowEvent we){
                    if(questionAnswersController.checkDataModified()){
                        checkForQuestionnaireSave();
                    }else{
                        dlgProtoSubQuestionnaire.dispose();
                    }
                }
            });
            questionAnswersForm.btnModify.setEnabled(false);
            questionAnswersForm.btnStartOver.setEnabled(false);
            questionAnswersForm.btnStartOver.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selection = CoeusOptionPane.showQuestionDialog(
                            coeusMessageResources.parseMessageKey("questionnaire_exceptionCode.1017"),
                            CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_NO);
                    if(selection == 0){
                        //Restart the questionnaire
                        questionAnswersController.processData(RESTART);
                    }
                }
            });
            questionAnswersForm.btnModify.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //Allows the use to modify the quesrtionnaire once it is completed
                    questionAnswersController.processData(MODIFY);
                }
            });
            
            
            dlgProtoSubQuestionnaire.setResizable(false);
            dlgProtoSubQuestionnaire.getContentPane().add(questionAnswersController.getControlledUI());
            dlgProtoSubQuestionnaire.setFont(CoeusFontFactory.getLabelFont());
            dlgProtoSubQuestionnaire.setSize(990, 600);
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension dlgSize = dlgProtoSubQuestionnaire.getSize();
            dlgProtoSubQuestionnaire.setLocation(screenSize.width/2 - (dlgSize.width/2),
                    screenSize.height/2 - (dlgSize.height/2));
            dlgProtoSubQuestionnaire.show();
        }else{
            String message = "";
            MessageFormat formatter = new MessageFormat("");
            message = formatter.format(coeusMessageResources.parseMessageKey("protocoSubmissionQuestions_exceptionCode.1003"), actionBean.getActionTypeDescription());
            CoeusOptionPane.showWarningDialog(message);
            
        }
        
    }
    
    /*
     * The method check if the protocol is locked by an other user
     * and locks the protocol if lock is available
     * @returns - lockAvailable
     */
    public boolean lockProtocol() {
        boolean lockAvailable = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(LOCK_PROTOCOL_DURING_SUBMISSION);
        request.setId(getActionBean().getProtocolNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response==null){
            CoeusOptionPane.showErrorDialog("Could not contact server");
        } else if(response.isSuccessfulResponse()){
            LockingBean lockingBean = (LockingBean)response.getLockingBean();
            if(lockingBean.isGotLock()){
                lockAvailable = true;
            }
        }else{
            lockAvailable = false;
            CoeusOptionPane.showErrorDialog(response.getMessage());
        }
        
        return lockAvailable;
    }
    
    /*
     * The method will release lock for the protocol
     */ 
    public void releaseUpdateProtocolLock(){
        String protocolNumber = getActionBean().getProtocolNumber();
        RequesterBean requester = new RequesterBean();
        requester.setId(protocolNumber);
        requester.setFunctionType(RELEASE_LOCK_AFTER_SUBMISSION);
        String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean res = comm.getResponse();
        if (res != null && !res.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(res.getMessage());
            return;
        }
    }
    
    /*
     * The method will insert temporary submission details in OSP$PROTOCOL_SUBMISSION, OSP$PROTOCOL_ACTIONS with
     * the current submission and action details
     */
    public void addTempSubmissionActionDetails() throws Exception{
        Vector dataObjects = new Vector();
        RequesterBean requester = new RequesterBean();
        
        ProtocolActionsBean protocolActionBean = getActionBean();
        String protocolNumber = protocolActionBean.getProtocolNumber();
        requester.setId(protocolNumber);
        String tempProtocolnumber = protocolNumber + APPEND_TEMP_SUB_CHAR_AFTER_PROTO_NUMBER;
        ProtocolSubmissionInfoBean submissionInfoBean = new ProtocolSubmissionInfoBean();
        ProtocolActionsBean clonedActionBean = (ProtocolActionsBean)ObjectCloner.deepCopy(protocolActionBean);
        //Setting temp protocol Number and status
        submissionInfoBean.setProtocolNumber(tempProtocolnumber);
        submissionInfoBean.setSubmissionStatusCode(TEMPORARY_SUBMISSION); 
        submissionInfoBean.setAcType("I");
        submissionInfoBean.setSequenceNumber(protocolActionBean.getSequenceNumber());
        submissionInfoBean.setScheduleId(protocolActionBean.getScheduleId());
        submissionInfoBean.setCommitteeId(protocolActionBean.getCommitteeId());
        //If the submission is Notify IRB then sets review and notificaiton type 
        //Else other submission, then default review type '1' is assigned
        if(actionBean.getActionTypeCode() == IacucProtocolActionsConstants.NOTIFY_IACUC){
            ComboBoxBean reviewTypeBean = (ComboBoxBean)cmbReviewType.getSelectedItem();
            String reviewType = reviewTypeBean.getCode();
            int reviewTypeCode = (new Integer(reviewType)).intValue();
            submissionInfoBean.setProtocolReviewTypeCode((new Integer(reviewTypeCode)).intValue());
            ComboBoxBean notificationType = (ComboBoxBean)cmbNotificationType.getSelectedItem();
            String submissionTypeQual = notificationType.getCode();
            int submissionTypeQualCode = (new Integer(submissionTypeQual)).intValue();
            submissionInfoBean.setSubmissionQualTypeCode(submissionTypeQualCode);
       }else{
            submissionInfoBean.setProtocolReviewTypeCode(1);
        }
        
        clonedActionBean.setProtocolNumber(tempProtocolnumber);
        clonedActionBean.setAcType("I");
        dataObjects.add(SUBMISSION_INFO_BEAN,submissionInfoBean);
        dataObjects.add(ACTION_INFO_BEAN,clonedActionBean);
        requester.setDataObjects(dataObjects);
        requester.setFunctionType(UPDATE_SUBMISSION_ACTION_DETAILS);
        String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SUBMISSION_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null && !response.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            return;
        }else{
            tempProtoSubmissionNumber = ((Integer)response.getDataObject()).intValue();
            isTempProtocolUpdated = true;
        }
        
    }
   
    /*
     * Method will check the questionnaire is completed for the current submission for temporary protocol number
     * @return vecUnfilledQnr
     */
    private Vector validateQuestionnaireCompleted() {
        Vector vecUnfilledQnr = new Vector();
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CHECK_QUESTIONNAIRE_COMPLETED);
        CoeusVector proposalData = new CoeusVector();
        proposalData.add(0, actionBean.getProtocolNumber()+APPEND_TEMP_SUB_CHAR_AFTER_PROTO_NUMBER);
        proposalData.add(1, new Integer(ModuleConstants.IACUC_MODULE_CODE));
        proposalData.add(2, String.valueOf(PROTOCOL_SUBMISSION_SUB_MODULE));
        proposalData.add(3, new Integer(tempProtoSubmissionNumber));
        request.setDataObjects(proposalData);
        String connectTo = CoeusGuiConstants.CONNECTION_URL + "/questionnaireServlet";
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null){
            if (response.isSuccessfulResponse()){
                vecUnfilledQnr = (Vector) response.getDataObject();
            }
        }
        return vecUnfilledQnr;
    }
    
    /*
     * Method will validate whether madatory questionnaire is completed and newer version of the questionnaire is completed 
     * @return true - validation succeed
     */
    private boolean validateQuestionnaireForSubmission(){
        if(isProtocolLockExists()){
            Vector vecUnfilledQnr = validateQuestionnaireCompleted();
            
            if(vecUnfilledQnr != null && vecUnfilledQnr.size() > 0){
                String mandatoryQnr = (String) vecUnfilledQnr.get(0);
                String incompleteQnr = (String) vecUnfilledQnr.get(1);
                String newVersionQnr = (String) vecUnfilledQnr.get(2);
                
                boolean mandatoryQnrPresent = false;
                boolean incompleteQnrPresent = false;
                boolean newVersionQnrPresent = false;
                MessageFormat formatter = new MessageFormat("");
                if(mandatoryQnr != null && !"".equals(mandatoryQnr.trim())){
                    mandatoryQnrPresent = true;
                }
                
                if(incompleteQnr != null && !"".equals(incompleteQnr.trim())){
                    incompleteQnrPresent = true;
                }
                
                if(newVersionQnr != null && !"".equals(newVersionQnr.trim())){
                    newVersionQnrPresent = true;
                }
                
                String validationMessage = "";
                
                if(mandatoryQnrPresent){
                    validationMessage = formatter.format(
                            coeusMessageResources.parseMessageKey(MANDATORY_QUESTIONNAIRE),
                            actionBean.getActionTypeDescription());
                    
                    
                    StringTokenizer stokenMessage = new StringTokenizer(mandatoryQnr,"~");
                    while (stokenMessage.hasMoreTokens()){
                        String message = stokenMessage.nextToken();
                        validationMessage = validationMessage + "\n \t" + message;
                    }
                }
                
                if(incompleteQnrPresent){
                    if(mandatoryQnrPresent){
                        validationMessage = validationMessage + "\n \n";
                    }
                    
                    validationMessage = validationMessage  + formatter.format(
                            coeusMessageResources.parseMessageKey(INCOMPLETE_QUESTIONNAIRE),
                            actionBean.getActionTypeDescription());
                    StringTokenizer stokenMessage = new StringTokenizer(incompleteQnr,"~");
                    while (stokenMessage.hasMoreTokens()){
                        String message = stokenMessage.nextToken();
                        validationMessage = validationMessage + "\n \t" + message;
                    }
                }
                
                if(newVersionQnrPresent){
                    if(incompleteQnrPresent || mandatoryQnrPresent){
                        validationMessage = validationMessage + "\n \n";
                    }
                    
                    
                    validationMessage = validationMessage  + formatter.format(
                            coeusMessageResources.parseMessageKey(NEW_QUESTIONNAIRE_VERSION),
                            actionBean.getActionTypeDescription());
                    
                    StringTokenizer stokenMessage = new StringTokenizer(newVersionQnr,"~");
                    while (stokenMessage.hasMoreTokens()){
                        String message = stokenMessage.nextToken();
                        validationMessage = validationMessage + "\n \t" + message;
                    }
                }
                
                if(mandatoryQnrPresent || incompleteQnrPresent || newVersionQnrPresent){
                    CoeusOptionPane.showWarningDialog(validationMessage);
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }
    
    /*
     * The method will delete all the temporary submission details in
     * OSP$PROTOCOL_SUBMISSION, OSP$PROTOCOL_ACTIONS, OSP$QUESTIONNAIRE_ANS_HEADER and OSP$QUESTIONNAIRE_ANSWERS tables
     */
    private void cleanTemporarySubmissionData(){
        String connectTo = CoeusGuiConstants.CONNECTION_URL + PROTOCOL_SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(CLEAN_TEMPORARY_SUBMISSION);
        request.setId(getActionBean().getProtocolNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response==null){
            CoeusOptionPane.showErrorDialog("Could not contact server");
        } else{
            isTempProtocolUpdated = false;
        }
    }
    
    /*
     * The Method will check, whether any one of the questions is answered for the submission
     */
    private boolean isAnyQuestionAnswered(){
        boolean isAnyQuestionAnswered = false;
        String connectTo = CoeusGuiConstants.CONNECTION_URL + QUESTIONNAIRE_SERVLET;
        RequesterBean request = new RequesterBean();
        CoeusVector cvModuleData = new CoeusVector();
        cvModuleData.add(new Integer(ModuleConstants.IACUC_MODULE_CODE));
        cvModuleData.add(getActionBean().getProtocolNumber()+APPEND_TEMP_SUB_CHAR_AFTER_PROTO_NUMBER);
        cvModuleData.add(new Integer(PROTOCOL_SUBMISSION_SUB_MODULE));
        cvModuleData.add(String.valueOf(tempProtoSubmissionNumber));
        request.setDataObjects(cvModuleData);
        request.setFunctionType(CHECK_ANY_QUESTION_ANSWEREN_IN_MODULE);
        request.setId(getActionBean().getProtocolNumber());
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        comm.send();
        ResponderBean response = comm.getResponse();
        if(response==null){
            CoeusOptionPane.showErrorDialog("Could not contact server");
        }if(response.isSuccessfulResponse()){
               isAnyQuestionAnswered = ((Boolean)response.getDataObject()).booleanValue();
        }
        return isAnyQuestionAnswered;
    }
   
    private void checkForQuestionnaireSave(){
         int option = JOptionPane.NO_OPTION;
            option  = CoeusOptionPane.showQuestionDialog(coeusMessageResources.parseMessageKey(QUESTIONNAIRE_SAVE_CONFIRMATION),
            CoeusOptionPane.OPTION_YES_NO_CANCEL,
            CoeusOptionPane.DEFAULT_CANCEL);
            switch(option) {
                case ( JOptionPane.YES_OPTION ):
                    try{
                        if(questionAnswersController.validate()){
                            questionAnswersController.processData(SAVE);
                            dlgProtoSubQuestionnaire.dispose();
                        }
                    }catch(Exception e){
                        CoeusOptionPane.showErrorDialog(e.getMessage());
                    }
                    break;
                case ( JOptionPane.NO_OPTION ):
                    dlgProtoSubQuestionnaire.dispose();
                    break;
                case ( JOptionPane.CANCEL_OPTION):
                    break;
                    
            }
    }
    
    /*
     * Method to check lock is available for the protocol
     */
    private boolean isProtocolLockExists(){
        String protocolNumber = getActionBean().getProtocolNumber();
        RequesterBean requester = new RequesterBean();
        requester.setId(protocolNumber);
        requester.setFunctionType(CHECK_IS_PROTOCOL_LOCK_EXISTS);
        String connectTo =CoeusGuiConstants.CONNECTION_URL+PROTOCOL_SERVLET;
        AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
        comm.send();
        ResponderBean response = comm.getResponse();
        if (response != null && !response.isSuccessfulResponse()){
            CoeusOptionPane.showErrorDialog(response.getMessage());
            return false;
        }else{
            boolean isLockExists = ((Boolean)response.getDataObject()).booleanValue();
            if(isLockExists){
                return true;
            }else{
                return false;
            }
        }
    }
     //COEUSDEV-86 : End
}
