/*
 * @(#)ProtocolSubmissionForm.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on November 25, 2002, 5:56 PM
 * @version 1.0
 */

/* PMD check performed, and commented unused imports and variables on 25-AUGUST-2010
 * by Satheesh Kumar K N
 */

package edu.mit.coeus.iacuc.gui;
 
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import java.beans.*;

import edu.mit.coeus.gui.*;
import edu.mit.coeus.brokers.*;
import edu.mit.coeus.utils.*;
import edu.mit.coeus.iacuc.bean.*;
import edu.mit.coeus.bean.CheckListBean;
import edu.mit.coeus.exception.*;
import edu.mit.coeus.routing.gui.RoutingForm;
import edu.mit.coeus.irb.bean.CommitteeMaintenanceFormBean;

/** <CODE>ProtocolSubmissionForm</CODE> dialog is used to submit a Protocol to a Committee for
 * review. In this, user has to choose a Committee which will review the Protocol.
 * And also, user has to specify during which Schedule of the Committee  this
 * Protocol should be reviewed. Along with these details user may choose the
 * reviewers from the active members of selected Committee.
 *
 * @author ravikanth
 * Changes done as per the Change Request id : 175, Feb' 14 2003
 * Changes are made in the ProtocolSubmissionForm.form
 * Modified By: Raghunath P.V. Feb' 17 2003
 *
 */
public class ProtocolSubmissionForm extends CoeusDlgWindow {
    
    /** reference object of CommitteeSelectionForm */
    private CommitteeSelectionForm committeeSelectionForm;
    
    /** reference object of ScheduleSelectionForm */
    private ScheduleSelectionForm scheduleSelectionForm;
    
    /** reference object of ReviewerSelectionForm */
    private ReviewerSelectionForm reviewerSelectionForm;
    
    /** boolean value which specifies whether ScheduleSelectionForm to be
     * or not. */
    private boolean showSchedule;
    
    /** boolean value which specifies whether ReviewerSelectionForm to be
     * or not. */
    private boolean showReviewer;
    
    private boolean showCommittee ;
    
    /** char which specifies the form is opened in Add mode */
    private static final char ADD_MODE = 'A';
    
    /** char which specifies the form is opened in Modify mode */
    private static final char MODIFY_MODE = 'M';
    
    /** holds string value used to specify that the record has to be inserted.*/
    private static final String INSERT_RECORD = "I";
    
    /** holds string value used to specify that the record has to be updated. */
    private static final String UPDATE_RECORD = "U";
    
    /** holds string value used to specify that the record has to be marked as
     * deleted. */
    private static final String DELETE_RECORD = "D";
    
    /** holds character value used to specify that the record has to be saved. */
    private static final char SAVE_RECORD = 'S';
    
    /** holds character value used to specify that the request is to fetch the
     * schedules for a specified committee */
    private static final char SCHEDULE_SELECTION = 'V';
    
    /** holds character value used to specify that the request is to fetch the
     * reviewers for a specified schedule */
    private static final char REVIEWER_SELECTION = 'R';
    
    //prps start - jul 15 2003
    private static final char PROTO_MAX_SUB_NUM = 'X' ;
    //prps end
    
    //Coeus enhancement 32.10 added by shiji - step 1: start
    private static final char GET_SUBMISSION_REVIEW_TYPE = 'F';
    //Coeus enhancement 32.10 - step 1: end
    
    /** holds character value used to specify that the request is to fetch the
     * count of protocols submitted to a schedule */
    private static final char PROTO_SUB_COUNT = 'C';
    
    /** char which specifies the mode in which the form is opened */
    private char functionType = ADD_MODE;
    
    /** collection object which holds the available protocol submission types */
    private ArrayList submissionTypes;
    
    /** collection object which holds the available protocol submission type
     * qualifiers */
    private ArrayList typeQualifiers;
    
    /** collection object which holds the available protocol review types */
    private ArrayList reviewTypes;
    
    /** collection object which holds the available protocol reviewer types */
    private ArrayList reviewerTypes;
    
    /** collection object which holds the list of recommended committees where
     * this particular protocol can be submitted. */
    private ArrayList committeeList;
    
    private ArrayList scheduleList ;
    /** reference object of ProtocolSubmissionInfoBean which will be used to
     * get and set the values of the form objects */
    private ProtocolSubmissionInfoBean submissionBean;
    
    /** reference object of ProtocolReviewerInfoBean which will be used to
     * get and set the values of the form object */
    private ProtocolReviewerInfoBean reviewerBean;
    
    /** reference object of ProtocolInfoBean which is passed as argument from
     * ProtocolDetailsForm to get the protocol number and sequence number */
    private ProtocolInfoBean protocolInfo;
    //Coeus enhancement Case 32.10 - step 1: start
    int submissionCode,reviewCode;
    //Coeus enhancement Case 32.10 - step 1: end
    /** holds the protocol number extracted from protocolInfo */
    private String protocolId;
    //Coeus enhancement Case #1793 - step 1: start
    private Vector reviewTypesForSubmission;
    //Coeus enhancement Case #1793 - step 1: end
    
    /** holds the sequence number of protocol extracted from protocolInfo */
    private int seqNo;
    /** holds the version number of protocol extracted from protocolInfo */
    private int verNo;
    
    /** boolean value which specifies any changes have been made */
    private boolean saveRequired;
    
    /** reference object of CoeusMessageResources which will be used to get the
     * messages to be displayed to the user */
    CoeusMessageResources coeusMessageResources
            = CoeusMessageResources.getInstance();
    
    /** integer used as loop variable*/
    private int revIndex;
    
    /** boolean value which specifies whether the protocol has been successfully
     * submitted or not */
    private boolean dataSaved;
    
    /** boolean value which specifies any changes has been made to
     * reviewer table */
    private boolean reviewersModified;
    
    /** boolean value used in identifying existing reviewers */
    private boolean found;
    private boolean identifyReviewers; // = true;
    
    //Added By sharath for CheckList - START
    private static final String EXPEDITED = "Expedited";
    private static final String EXEMPT = "Exempt";
    private final String LOSE_CHECKED_LIST_INFO =
            coeusMessageResources.parseMessageKey("checkList_mandatory_exceptionCode.1118");
//    private final String SELECT_ATLEAST_ONE_CHECKLIST =
//            coeusMessageResources.parseMessageKey("checkList_mandatory_exceptionCode.1119");
    private String selectedItem = null;
    private int selectedIndex;
    private Vector vecExpedited, vecExempt, vecExpeditedToModify, vecExemptToModify;
    private CheckList checkList = new CheckList(CoeusGuiConstants.getMDIForm(), true);
    //Added By sharath for CheckList - END
    
    //prps start - jul 21 2003
    // this mode will be of three types - OPTIONAL, MANDATORY, DENIED
    //OPTIONAL -  it is optional to select a committee & schedule before submission
    //MANDATORY - it is mandatory to select a commmitee & schedule before submission
    //DENIED - user will not see the committee & schedule selection screen but will be able
    // to submit the protocol
    private char submissionMode ;
    private static final char OPTIONAL = 'O' ;
    private static final char MANDATORY = 'M' ;
    private static final char DENIED = 'D' ;
    // used to check if the schedule is modified to check for no. of protocols submitted to it
    private String oldScheduleId = "";
    //prps end
    
    // Added by Jobin for the Committe Id and Name
    private String committeeId = "";
    private String committeeName = "";
    
    private boolean showDefaultval= true;
    
    //Added for case 2785 - Routing enhancement - start
    private boolean mapsFound;
    private String leadUnitNumber;
    private final char SUBMIT_FOR_APPROVE = 'G';
    private int submissionStatusCode;
    //Added for case 2785 - Routing enhancement - end
    private static final char COMMITTEE_LIST_FOR_MODULE = 'z';
    
    // 3282: reviewer view of protocols
    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
//    private Vector recommendeActiontypes;
    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - End
    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
    private boolean errDisplay = false;
    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
    //Protocol Status
    private static final int PROTO_STATUS_SUBMITTED_TO_IACUC = 101 ;
    //Submission Status
    private final int SUB_STATUS_ROUTING_IN_PROGRESS = 100;
    private static final int SUB_STATUS_SUBMIT_TO_COMMITTEE = 102 ;
    //Submission Servlet
    private static final String SUBMISSION_SERVLET = "/iacucProtocolSubSrvlt";
    // Added for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
    private static final int REVIEWER_REVIEW_TYPE_COLUMN = 2;
    private static final int REVIEWER_ASSIGNED_DATE_COLUMN = 3;
    private static final int REVIEWER_DUE_DATE_COLUMN = 4;
    private static final int REVIEWER_EMP_COLUMN = 5;
    // Added for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - End
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
    public static final int ROUTING_IN_PROGRESS_STATUS = 108;
    // Added for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
    private final char UPD_PROT_STATUS_AND_SUBMISSION_STATUS = 'H';
    
    /** Creates new form <CODE>ProtocolSubmissionForm</CODE> and initializes all the
     * components used to select Committee, Schedule and Reviewers required for
     * reviewing a Protocol.
     *
     * @param parent reference to parent Frame object.
     * @param title String value used to display as title to this dialog.
     * @param modal boolean value which specifies the dialog type i.e either
     * modal or non-modal dialog.
     * @param protocolInfo <CODE>ProtocolInfoBean</CODE> object which will be passed from
     * <CODE>ProtocolDetailsForm</CODE>.
     * @throws Exception if unable to fetch any of the required details.
     */
    public ProtocolSubmissionForm(Frame parent, String title, boolean modal,
            ProtocolInfoBean protocolInfo) throws Exception{
        super(parent, title, modal);
        this.protocolInfo = protocolInfo;
        this.protocolId = protocolInfo.getProtocolNumber();
        this.seqNo = protocolInfo.getSequenceNumber();
        this.verNo = protocolInfo.getVersionNumber();
        
        getDefaultSubmissionMode() ;
        initComponents();
        btnCheckList.setVisible(false);
        // added by manoj for focus traversing among components 16/09/2003
        // starts
        java.awt.Component[] components = {cmbSubmissionType,cmbReviewType,
        cmbTypeQualifier,btnCommittee,btnSelect,btnShowAll,btnSelectReview,
        btnClearSelection,btnOk,btnCancel,btnCheckList};
        ScreenFocusTraversalPolicy  traversePolicy = new ScreenFocusTraversalPolicy( components );
        pnlMain.setFocusTraversalPolicy(traversePolicy);
        pnlMain.setFocusCycleRoot(true);
        //ends
        
        //prps start - jul 21 2003
        if (submissionMode == DENIED){
            // 3282: Reviewer View of Protocol materials - Start
//            setSize(625, 225) ;
            //Modified for COEUSDEV-378 : Protocol Submission screen cut off in Premium - Start
//            setsize(800,225)
            pnlMain.setMinimumSize(new Dimension(700,150));
            pnlMain.setMaximumSize(new Dimension(700,150));
            pnlMain.setPreferredSize(new Dimension(700,150));
            setSize(700, 225) ;
            pnlSubmissionDetails.setPreferredSize(new Dimension(650,125));
            //COEUSDEV-378 : End
            // 3282: Reviewer View of Protocol materials - End
        } else {
            // 3282: Reviewer View of Protocol materials - Start
//            setSize(625, 475) ;
            setSize(800, 475) ;
            // 3282: Reviewer View of Protocol materials - End
        }
        // prps end
        
        btnCommittee.setEnabled(false) ;
        
        // displayScheduleComponents(false);
        showSchedule = true;
        getLookupDetails();
        setFormData();
        formatFields(true);
        
    }
    
    public void getDefaultSubmissionMode() {
        try {
            String connectTo = CoeusGuiConstants.CONNECTION_URL + "/coeusFunctionsServlet";
            RequesterBean request = new RequesterBean();
            request.setDataObject("IACUC_COMM_SELECTION_DURING_SUBMISSION") ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, request);
            comm.send();
            ResponderBean response = comm.getResponse();
            if (response != null) {
                if (response.isSuccessfulResponse()) {
                    submissionMode = response.getId().charAt(0) ;
                }
            }
        }catch(Exception ex){
            CoeusOptionPane.showInfoDialog(ex.getMessage());
        }
        
        
    }
    
    
    
    /**
     * This method is used to fetch all the lookup details required for protocol
     * submission.
     */
    private void getLookupDetails() throws Exception{
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        
        /* connect to the database and get the Schedule Details for the given
           schedule id */
        RequesterBean request = new RequesterBean();
        request.setFunctionType(functionType);
        request.setId(protocolId);
        request.setDataObject(protocolInfo);
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
        
        Vector dataObjects = response.getDataObjects();
        if (response.isSuccessfulResponse()) {
            /* get all the lookup details required for submissionTypes,
               submissionTypeQualifiers, protocol review types and protocol
               reviewer types */
            submissionTypes = new ArrayList((Vector)dataObjects.get(0));
            typeQualifiers = new ArrayList((Vector)dataObjects.get(1));
            reviewTypes = new ArrayList((Vector)dataObjects.get(2));
            reviewerTypes = new ArrayList((Vector)dataObjects.get(3));
            
            //Added By sharath for CheckList - START
            //btnCheckList.setVisible(false); //For Build
            vecExpedited = (Vector)dataObjects.get(4);
            vecExempt = (Vector)dataObjects.get(5);
            vecExpeditedToModify = (Vector)dataObjects.get(6);
            vecExemptToModify = (Vector)dataObjects.get(7);
            
//            prepareCheckList(vecExempt, vecExemptToModify);
//            prepareCheckList(vecExpedited, vecExpeditedToModify);
//            if(vecExpeditedToModify != null && vecExpeditedToModify.size() > 0) {
//                setCheckedState(vecExpeditedToModify, true);
//            }
//
//            if(vecExemptToModify != null && vecExemptToModify.size() > 0) {
//                setCheckedState(vecExemptToModify, true);
//            }
            //Added By sharath for CheckList - END
            // Added by Jobin to get the committe details
            Vector vCommitte = (Vector) dataObjects.get(8);
            if (vCommitte != null && vCommitte.size() > 0) {
                committeeId = (String) vCommitte.get(0);
                committeeName = (String) vCommitte.get(1);
            }
            
            if(dataObjects.size() > 9){//if(dataObjects.size() > 4){
                /* if there are any committees whose home unit and research areas
                   matches with that of protocol then get the list of those
                   committees */
                //committeeList = new ArrayList((Vector)dataObjects.get(4));
                committeeList = new ArrayList((Vector)dataObjects.get(9));
                /* if the protocol has been submitted then get the submission
                   details from the database */
                submissionBean = (ProtocolSubmissionInfoBean)dataObjects.get(10);
                //dataObjects.get(5);
                //modified by ravi to reset reviewers as we dont know whether
                // reviewers were for a committee or for a schedule.
                if( submissionBean != null ) {
                    submissionBean.setProtocolReviewer(null);
                }
                
            }
            //Coeus enhancement Case #1793 - step 2: start
            reviewTypesForSubmission=(Vector)dataObjects.get(11);
            if(reviewTypesForSubmission == null) {
                reviewTypesForSubmission=new Vector();
            }
            //Coeus enhancement Case #1793 - step 2: end
            
            // 3282: reviewer view of protocols - Start
            // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
//            if(dataObjects.size() > 12){
//                recommendeActiontypes = (Vector) dataObjects.elementAt(13);
//            }
            // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
            // 3282: reviewer view of protocols - End
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
        
        /* determine the mode in which the form is opened by fetching the
           submission details from the database for given protocol number. if
           submission details are present then set the mode to MODIFY_MODE else
           set the form opened mode to ADD_MODE */
        if(submissionBean == null){
            functionType = ADD_MODE;
        }else{
            functionType = MODIFY_MODE;
        }
        /* create the committeeSelectionForm with the available committees */
        committeeSelectionForm = new CommitteeSelectionForm(committeeList);
        /* set the size of the committeeSelectionForm depending on the size of
           the panel where the forms will be dynamically loaded */
        committeeSelectionForm.setPreferredSize(
                new java.awt.Dimension( pnlContent.getWidth()-40
                , pnlContent.getHeight() - 40) );
        
        /* add the committeeSelectionForm dynamically in the Protocol Submission
           form */
        pnlContent.add(committeeSelectionForm,BorderLayout.CENTER);
        
    }
    
    /**
     * This method is used to set the form data as well as all lookup data by
     * extracting details from submissionBean.
     */
    private void setFormData(){
        ComboBoxBean comboBean;
        if( submissionTypes != null ){
            /* add a blank row in Submission Type combo box so that the default
               selection will be blank */
            cmbSubmissionType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < submissionTypes.size();
            loopIndex++ ) {
                /* extract ComboBoxBean from the collection of submissionTypes */
                comboBean = (ComboBoxBean)submissionTypes.get(loopIndex);
                cmbSubmissionType.addItem(comboBean);
            }
        }
        
        if( typeQualifiers != null ){
            /* add a blank row in Submission Type Qualifier combo box so that
               the default selection will be blank */
            cmbTypeQualifier.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < typeQualifiers.size();
            loopIndex++ ) {
                comboBean = (ComboBoxBean)typeQualifiers.get(loopIndex);
                cmbTypeQualifier.addItem(comboBean);
            }
            
        }
        //Modified for Coeus enhancement Case #1793 - step 4: start
        if( reviewTypes != null && submissionBean != null){
            //Coeus enhancement Case #1793 - step 4: end
            /* add a blank row in Protocol Review Type combo box so that the
               default selection will be blank */
            cmbReviewType.addItem(new ComboBoxBean("",""));
            for ( int loopIndex = 0 ; loopIndex < reviewTypes.size();
            loopIndex++ ) {
                comboBean = (ComboBoxBean)reviewTypes.get(loopIndex);
                cmbReviewType.addItem(comboBean);
            }
        }
        
        if(submissionBean != null) {
            /* if the protocol has been already submitted set the previous
               selections to all the form components */
            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getSubmissionTypeCode()).toString());
            comboBean.setDescription(submissionBean.getSubmissionTypeDesc());
            
            /* get the submission type description from the given code */
            for(int typeRow = 0; typeRow < cmbSubmissionType.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbSubmissionType.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbSubmissionType.setSelectedIndex(typeRow);
                }
            }
            
            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getProtocolReviewTypeCode()).toString());
            comboBean.setDescription(submissionBean.getProtocolReviewTypeDesc());
            
            /* get the review type description from the given code */
            for(int typeRow = 0; typeRow < cmbReviewType.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbReviewType.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbReviewType.setSelectedIndex(typeRow);
                }
            }
            
            comboBean = new ComboBoxBean();
            comboBean.setCode(new Integer(
                    submissionBean.getSubmissionQualTypeCode()).toString());
            comboBean.setDescription(submissionBean.getSubmissionQualTypeDesc());
            
            /* get the submission type qualifier description from the given code */
            for(int typeRow = 0; typeRow < cmbTypeQualifier.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbTypeQualifier.getItemAt(
                        typeRow)).toString().equals(comboBean.toString())){
                    cmbTypeQualifier.setSelectedIndex(typeRow);
                }
            }
//            System.out.println("values : - "+submissionBean.getCommitteeId()+":"+submissionBean.getCommitteeName());
            
            txtCommitteeID.setText(submissionBean.getCommitteeId());
            txtCommitteeName.setText(submissionBean.getCommitteeName());
//            Comment for IRB bug fix Case #1041 - begin
//            btnOk.setEnabled(false);
//            Comment for IRB bug fix Case #1041 - end
            btnShowAll.setEnabled(true);
            committeeSelectionForm.setSelectedCommittee(
                    submissionBean.getCommitteeId(),
                    submissionBean.getCommitteeName());
        } else {
            //Coeus enhancement 32.10 added by shiji - step 2: start
            Vector submissionReviewTypes = getSubmissionReviewTypes();
            submissionCode = Integer.parseInt((String)submissionReviewTypes.get(0));
            Vector reviewTypesForSubmission = getReviewTypeForSubmission(submissionCode);
            if( reviewTypesForSubmission != null ){
                cmbReviewType.removeAllItems();
                cmbReviewType.addItem(new ComboBoxBean("",""));
                for ( int loopIndex = 0 ; loopIndex < reviewTypesForSubmission.size();
                loopIndex++ ) {
                    comboBean = (ComboBoxBean)reviewTypesForSubmission.get(loopIndex);
                    cmbReviewType.addItem(comboBean);
                }
            }
            reviewCode = Integer.parseInt((String)submissionReviewTypes.get(1));
            
            /* get the submission type description from the given code */
            for(int typeRow = 0; typeRow < cmbSubmissionType.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbSubmissionType.getItemAt(
                        typeRow)).getCode().equals((new Integer(submissionCode)).toString())){
                    cmbSubmissionType.setSelectedIndex(typeRow);
                }
            }
            
            for(int typeRow = 0; typeRow < cmbReviewType.getItemCount();
            typeRow++){
                if(((ComboBoxBean)cmbReviewType.getItemAt(
                        typeRow)).getCode().equals((new Integer(reviewCode)).toString())){
                    cmbReviewType.setSelectedIndex(typeRow);
                    break;
                }else {
                    cmbReviewType.setSelectedIndex(0);
                }
            }
            //Coeus enhancement 32.10 - step 2: end
            txtCommitteeID.setText(committeeId);
            txtCommitteeName.setText(committeeName);
        }
        
        saveRequired = false;
        
    }
    
    private Vector getReviewTypeForSubmission(int submissionTypeCode) {
        Vector revTypeCodesForSub= new Vector();
        if(reviewTypesForSubmission.size() != 0) {
            for(int i=0;i<reviewTypesForSubmission.size();i++) {
                SubmissionReviewTypeBean submissionReviewTypeBean = (SubmissionReviewTypeBean)reviewTypesForSubmission.get(i);
                if(submissionReviewTypeBean.getSubmissionTypeCode() == submissionTypeCode) {
                    ComboBoxBean cBean= new ComboBoxBean();
                    cBean.setCode(new Integer(submissionReviewTypeBean.getReviewTypeCode()).toString());
                    cBean.setDescription(submissionReviewTypeBean.getReviewTypeDescription());
                    revTypeCodesForSub.add(cBean);
                }
            }
        }else {
            cmbSubmissionType.removeAllItems();
        }
        return revTypeCodesForSub;
    }
    
    //Coeus enhancement 32.10 added by shiji - step 3: start
    private Vector getSubmissionReviewTypes() {
        Vector vecSubRevTypes = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(GET_SUBMISSION_REVIEW_TYPE);
        request.setId(protocolId);
        // added by ravi for restricting investigators and key study personnel as reviewers
        //request.setDataObject(protocolId);
        AppletServletCommunicator comm = new AppletServletCommunicator(
                connectTo, request);
        /**
         * Updated for REF ID :0003  Feb'21 2003.
         * Hour Glass implementation while DB Trsactions Wait
         * by Subramanya Feb' 21 2003
         */
        //setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        //setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        if (response.isSuccessfulResponse()) {
            vecSubRevTypes = (Vector)(
                    (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecSubRevTypes;
        
    }
    //Coeus enhancement 32.10 - step 3: end
    
    
    /**
     * This method is used to set the enabled status of all the components in
     * the form depending on the screen displayed.
     *
     * @param inCommitteeSelection boolean value which specifies whether the
     * current displayed form is CommitteeSelectionForm or not.
     */
    private void formatFields(boolean inCommitteeSelection){
        //prps start - jul 21 2003
        if (submissionMode == DENIED) {
            btnOk.setEnabled(true) ;
            pnlContent.setVisible(false) ;
            btnShowAll.setVisible(false);
            btnSelect.setVisible(false) ;
            btnSelectReview.setVisible(false) ;
            btnClearSelection.setVisible(false) ;
            btnCommittee.setVisible(false) ;
            lblId.setVisible(false);
            txtCommitteeID.setVisible(false);
            lblCommName.setVisible(false);
            txtCommitteeName.setVisible(false);
            lblSchId.setVisible(false) ;
            lblSchDate.setVisible(false) ;
            lblTable.setVisible(false) ;
            txtScheduleId.setVisible(false) ;
            txtScheduleDate.setVisible(false) ;
            return ;
        }
        
        if (submissionMode == OPTIONAL) {
            btnOk.setEnabled(true);
            btnShowAll.setVisible(true);
            lblId.setVisible(true);
            txtCommitteeID.setVisible(true);
            lblCommName.setVisible(true);
            txtCommitteeName.setVisible(true);
            btnSelectReview.setVisible(true) ;
            btnClearSelection.setVisible(true) ;
            btnCommittee.setVisible(true) ;
            lblSchId.setVisible(true) ;
            lblSchDate.setVisible(true) ;
            lblTable.setVisible(true) ;
            txtScheduleId.setVisible(true) ;
            txtScheduleDate.setVisible(true) ;
            return ;
        }
        
        //prps end
        
        
        //prps 21 jul 2003 added the condtn submissionMode == MANDATORY
        if (submissionMode == MANDATORY) {
            //            Comment for IRB bug fix Case #1041 - begin
            //            btnOk.setEnabled(false);
            //            Comment for IRB bug fix Case #1041 - end
            btnShowAll.setVisible(true);
            lblId.setVisible(true);
            lblCommName.setVisible(true);
            txtCommitteeID.setVisible(true);
            txtCommitteeName.setVisible(true);
            btnSelectReview.setVisible(true) ;
            btnClearSelection.setVisible(true) ;
            btnCommittee.setVisible(true) ;
            lblSchId.setVisible(true) ;
            lblSchDate.setVisible(true) ;
            lblTable.setVisible(true) ;
            txtScheduleId.setVisible(true) ;
            txtScheduleDate.setVisible(true) ;
            return ;
        }
        
    }
    
    
    /**
     * This method is used to set the visible status of the schedule components
     * the form depending on the screen displayed.
     *
     * @param show boolean value which specifies whether the
     * schedule id and schedule date components should be displayed or not.
     */
    private void displayScheduleComponents(boolean show){
        lblSchId.setVisible(show);
        lblSchDate.setVisible(show);
        txtScheduleId.setVisible(show);
        txtScheduleDate.setVisible(show);
    }
    
    /**
     * This method is used to get all the committees  to display in the
     * committee table of CommitteeSelectionForm
     *
     * @return committees collection of CommitteeMaintenanceFormBean
     */
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
            vecBeans = (Vector)response.getDataObjects();
        }
        return vecBeans;
    }
    
    /**
     * This method is used to get all the schedules for a given committee,
     * to display in the schedule table of ScheduleSelectionForm
     *
     * @param committeeId represents the committee whose schedules will be
     * fetched.
     * @return schedules collection of ScheduleDetailsBean
     */
    private Vector getSchedules(String committeeId){
        
        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SCHEDULE_SELECTION);
        request.setId(committeeId);
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
            vecBeans = (Vector)(
                    (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
    }
    
    /**
     * This method is used to get all the reviewers for a given schedule,
     * to display in the available reviewers table of ReviewerSelectionForm
     *
     * @param scheduleId represents the schedule id which is used to fetch all
     * active members of the committee for that schedule
     * @return schedules collection of ProtocolReviewerInfoBean
     */
    
    private Vector getAvailableReviewers( String scheduleId ) {
        
        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(REVIEWER_SELECTION);
        request.setId(scheduleId);
        // added by ravi for restricting investigators and key study personnel as reviewers
        request.setDataObject(protocolId);
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
            vecBeans = (Vector)(
                    (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
        
    }
    
    
    /**
     * This method is used to get the number of protocols submitted to a given
     * schedule.
     *
     * @param scheduleId String representing the schedule number to which the
     * protocol submission count is to be fetched.
     *
     * @return count which represents the number of protocols submitted to the
     * specified schedule.
     */
    private int getProtocolSubCount( String scheduleId ) {
        
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_SUB_COUNT);
        request.setId(scheduleId);
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
        
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }
    
    /** This method is used to check the validity of data entered by user.
     *
     * @return boolean true if all the entered data is valid else false.
     * @throws Exception Exception with the custom message will be thrown if the
     * data for any paritcular field is not valid.
     */
    private boolean validateData() throws Exception{
        /** string which holds the selected submission type description */
        String submissionType;
        
        /** string which holds the selected protocol review type description */
        String reviewType;
        
        submissionType = ((ComboBoxBean)
        cmbSubmissionType.getSelectedItem()).toString();
        
        reviewType =  ((ComboBoxBean)
        cmbReviewType.getSelectedItem()).toString();
        
        if(submissionType.trim().length() == 0){
            log(coeusMessageResources.parseMessageKey(
                    "protoSubmissionFrm_exceptionCode.2000"));
            return false;
        }else if( reviewType.trim().length() == 0 ){
            log(coeusMessageResources.parseMessageKey(
                    "protoSubmissionFrm_exceptionCode.2001"));
            return false;
        } //prps start - jul 21 2003 added the second validation(submission mode == MANDATORY)
        else if ( !committeeSelectionForm.isCommitteeSelected()
        && (("").equals(txtCommitteeID.getText().trim()) && ("").equals(txtCommitteeName.getText().trim()))
        && submissionMode == MANDATORY ) {//Added by Jobin for the Bug Fix : 1196 - start (11-Sep-2004)
            log(coeusMessageResources.parseMessageKey(
                    "commBaseWin_exceptionCode.1007"));
            return false;
        }else if ( functionType == ADD_MODE && submissionMode == MANDATORY){
            if( ( scheduleSelectionForm == null )
            || ( !scheduleSelectionForm.isScheduleSelected() ) ){
                log(coeusMessageResources.parseMessageKey(
                        "commSchdDetFrm_exceptionCode.1026"));
                return false;
            }
        }
        if( ( scheduleSelectionForm != null )
        && ( scheduleSelectionForm.isScheduleSelected() )
        && ( reviewerSelectionForm == null ) ){
            identifyReviewers = true;
        }
        
        //Added By sharath for CheckList - START
        //to check if atleast one checklist is Selected
//        if(selectedItem.equalsIgnoreCase(EXEMPT)) {
//            checkList.setFormData(vecExempt);
//            if(checkList.getData(true).size() == 0) {
//                log(SELECT_ATLEAST_ONE_CHECKLIST);
//                return false;
//            }
//        }else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
//            checkList.setFormData(vecExpedited);
//            if(checkList.getData(true).size() == 0){
//                log(SELECT_ATLEAST_ONE_CHECKLIST);
//                return false;
//            }
//        }
        //Added By sharath for CheckList - END
        // 3282: Reviewer view of protocols - Start
        // Throw Validation message if the Assiged date is After the due date
        // for the selected reviewers.
        if(reviewerSelectionForm != null){
            Vector vecSelectedReviewers = reviewerSelectionForm.getSelectedReviewers();
            if(vecSelectedReviewers != null && vecSelectedReviewers.size() > 0){
                int size = vecSelectedReviewers.size();
                Vector reviewerData;
                String strAssigedDate = "";
                String strDueDate = "";
                Date assigedDate, dueDate = null;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/dd/yyyy");
                for(int index = 0; index < size; index++){
                    reviewerData = (Vector) vecSelectedReviewers.elementAt(index);
                    if( reviewerData != null ){
                        strAssigedDate = (String)reviewerData.elementAt(REVIEWER_ASSIGNED_DATE_COLUMN);
                        strDueDate = (String) reviewerData.elementAt(REVIEWER_DUE_DATE_COLUMN);
                        if(strAssigedDate != null && !strAssigedDate.equals("") &&
                                strDueDate != null && !strDueDate.equals("")){
                            assigedDate = simpleDateFormat.parse(strAssigedDate);
                            dueDate = simpleDateFormat.parse(strDueDate);
                            if(assigedDate.after(dueDate)){
                                CoeusOptionPane.showErrorDialog(
                                        coeusMessageResources.parseMessageKey("protoSubmissionFrm_exceptionCode.2012"));
                                return false;
                            }
                        }
                    }
                }
            }
        }
        // 3282: Reviewer view of protocols - End
        return true;
    }
    
    /**
     * This method is used to check whether user has submitted the protocol
     * to any other schedule than the one he selected earlier.
     *
     * @return boolean true if user changed his selection else false.
     */
    private boolean isScheduleChanged(){
        
        if( scheduleSelectionForm != null){
            ScheduleDetailsBean scheduleBean
                    = scheduleSelectionForm.getSelectedSchedule();
            if(scheduleBean != null && submissionBean != null){
            /* compare schedule id where the protocol has been submitted with
               the selected schedule id */
                if(!scheduleBean.getScheduleId().equals(
                        submissionBean.getScheduleId())){
                    saveRequired = true;
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /** This method returns the ProtocolSubmissionInfoBean after constructing it
     * from the selected data in the forms.
     *
     * @return submissionBean ProtocolSubmissionInfoBean with all the data
     * populated from the form objects.
     * @throws Exception Exception with custom message while constructing bean
     * value from any form object goes wrong.
     */
    private ProtocolSubmissionInfoBean getSubmissionData() throws Exception {
        
        /** collection object which holds all the available reviewers*/
        Vector existingReviewers;
        String selectedScheduleId="";
        
        if(functionType == ADD_MODE){
            submissionBean = new ProtocolSubmissionInfoBean();
            submissionBean.setAcType(INSERT_RECORD);
        }else{
            /* in modify mode add the propery change listener to the bean so that
               modifications to any of the bean values can be catched */
            submissionBean.addPropertyChangeListener(
                    new PropertyChangeListener(){
                public void propertyChange(PropertyChangeEvent pce){
                        /* if any of the bean values changes set the acType of
                           the bean to UPDATE_RECORD so that the corresponding
                           table row will be updated */
                    submissionBean.setAcType(UPDATE_RECORD);
                }
            }
            );
        }
        
        /* get the details of the selected schedule */
        if( scheduleSelectionForm != null ){
            if( scheduleSelectionForm.isScheduleSelected() ) {
                selectedScheduleId
                        = scheduleSelectionForm.getSelectedSchedule().getScheduleId();
            }
        }else{
            selectedScheduleId = submissionBean.getScheduleId();
        }
        
        ComboBoxBean comboBean
                = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
        
        submissionBean.setProtocolNumber(protocolId);
        submissionBean.setSequenceNumber(seqNo);
        submissionBean.setVresionNumber(verNo);
        submissionBean.setSubmissionTypeCode(
                Integer.parseInt(comboBean.getCode()));
        
        //prps start jul 15 2003
        submissionBean.setSubmissionNumber(getMaxSubmissionNumber(protocolId))  ;
        //prps end
        submissionBean.setSubmissionTypeDesc(comboBean.getDescription());
        
        comboBean = ( ComboBoxBean ) cmbReviewType.getSelectedItem();
        
        submissionBean.setProtocolReviewTypeCode(
                Integer.parseInt(comboBean.getCode()));
        
        submissionBean.setProtocolReviewTypeDesc(comboBean.getDescription());
        
        comboBean = ( ComboBoxBean ) cmbTypeQualifier.getSelectedItem();
        /* as the protocol submission type qualifier selection is not mandatory
           check whether user has selected any value. if so set the selected code
           and description to the bean */
        if(comboBean.getCode().length() > 0 ){
            submissionBean.setSubmissionQualTypeCode(
                    Integer.parseInt(comboBean.getCode()));
            submissionBean.setSubmissionQualTypeDesc(
                    comboBean.getDescription());
        }
        /********************
         * hard coded the submission status code as 1
         ************/
        /****** begin: changed on 12-Feb-03 to fix the bug for
         * reference id: 202 */
        //Added for case 2785 - Routing enhancement - start
        if(isMapsFound()){
            submissionBean.setSubmissionStatusCode(SUB_STATUS_ROUTING_IN_PROGRESS);
        }else{
            submissionBean.setSubmissionStatusCode(SUB_STATUS_SUBMIT_TO_COMMITTEE);
        }
        //Added for case 2785 - Routing enhancement - end
        /*** end of bug fix refID: 202 */
        
        //prps strat 21 jul 2003
        // add commitee id (new field in OSP$Protocol_submission) to submissionBean
        //submissionBean.setCommitteeId(txtCommitteeID.getText()) ;
        if( committeeSelectionForm.isCommitteeSelected()  ) {
            submissionBean.setCommitteeId(
                    committeeSelectionForm.getSelectedCommittee().getCommitteeId());
        } else if (!("").equals(txtCommitteeID.getText())) { // bug fix: 1274 - jobin
            submissionBean.setCommitteeId(txtCommitteeID.getText()); //(if the committee id is not selected check the previous one and set that)
        }
        //prps end
        
        /* if user has changed the schedule then set all reviewers details for
           that schedule as DELETE_RECORD */
        existingReviewers = submissionBean.getProtocolReviewer();
        if( functionType == MODIFY_MODE){
            /* check whether user has changed the schedule  or deleted all the
               the existing reviewers */
            if(isScheduleChanged()
            || ( (reviewerSelectionForm != null)
            && (reviewerSelectionForm.getSelectedReviewers() == null ))){
                
                if(existingReviewers != null){
                    for( int index = 0 ; index < existingReviewers.size();
                    index++ ){
                        reviewerBean = (ProtocolReviewerInfoBean)
                        existingReviewers.get(index);
                        reviewerBean.setAcType(DELETE_RECORD);
                        existingReviewers.set(index,reviewerBean);
                    }
                }
            }
        }
        submissionBean.setScheduleId(selectedScheduleId);
        if( reviewerSelectionForm != null){
            
            Vector currentReviewers
                    = reviewerSelectionForm.getSelectedReviewers();
           /* loop through all reviewer table rows and construct beans and check
            * whether this reviewer is already exists or not. If not exists
            * setAcType to INSERT_RECORD and send. If exists, add property
            * change listener and set all the new data to the bean. If any
            * data is changed then the listener will fire and changes the
            * acType of the bean to UPDATE_RECORD
            */
            if(currentReviewers != null){
                int reviewerTypesSize = reviewerTypes.size();
                int currentRevSize = currentReviewers.size();
                for(int currIndex = 0; currIndex < currentRevSize; currIndex++){
                    found = false;
                    int foundIndex = -1;
                    Vector reviewerRow = (Vector)currentReviewers.get(currIndex);
                    ProtocolReviewerInfoBean newReviewerBean
                            = new ProtocolReviewerInfoBean();
                    
                    // populate bean data
                    newReviewerBean.setProtocolNumber(protocolId);
                    newReviewerBean.setSequenceNumber(seqNo);
                    //commented as scheduleId is removed from reviewers table
                    //newReviewerBean.setScheduleId(submissionBean.getScheduleId());
                    newReviewerBean.setPersonId(
                            reviewerRow.elementAt(0).toString());
                    // get reviewType code for the selected review type
                    //  3282: Reviewer View of Protocol materials - Start
//                   newReviewerBean.setIsNonEmployee(((Boolean)reviewerRow.get(3)).booleanValue());
                    // Modified for COEUSQA-2961 : IRB protocol submission screen: unable to move selected reviewer back to available reviewers column - Start
//                    newReviewerBean.setIsNonEmployee(((Boolean)reviewerRow.get(REVIEWER_EMP_COLUMN)).booleanValue());                    
                    if(reviewerRow.get(REVIEWER_EMP_COLUMN) != null){
                        newReviewerBean.setIsNonEmployee(((Boolean)reviewerRow.get(REVIEWER_EMP_COLUMN)).booleanValue());
                    }
                    // Modified for COEUSQA-2961 : IRB protocol submission screen: unable to move selected reviewer back to available reviewers column - End
                    SimpleDateFormat dateFormat = new SimpleDateFormat(CoeusGuiConstants.DEFAULT_DATE_FORMAT);
                    if(reviewerRow.get(REVIEWER_ASSIGNED_DATE_COLUMN) != null &&
                            !CoeusGuiConstants.EMPTY_STRING.equals(reviewerRow.get(REVIEWER_ASSIGNED_DATE_COLUMN))){
                        String strAssignedDate = (String) reviewerRow.get(REVIEWER_ASSIGNED_DATE_COLUMN);
                        Date assignedDate = dateFormat.parse(strAssignedDate);
                        newReviewerBean.setAssignedDate(new java.sql.Date(assignedDate.getTime()));
                    }
                    if(null != reviewerRow.get(REVIEWER_DUE_DATE_COLUMN) && 
                            !CoeusGuiConstants.EMPTY_STRING.equals(reviewerRow.get(REVIEWER_DUE_DATE_COLUMN))){
                        String strDueDate = (String) reviewerRow.get(REVIEWER_DUE_DATE_COLUMN);
                        Date dueDate = dateFormat.parse(strDueDate);
                        newReviewerBean.setDueDate(new java.sql.Date(dueDate.getTime()));
                    }
                    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
//                    if(reviewerRow.get(6) != null){
//                        ComboBoxBean cmbBean = (ComboBoxBean)reviewerRow.get(6);
//                        if(cmbBean != null ){
//                            newReviewerBean.setRecommendedActionCode(cmbBean.getCode());
//                        } else{
//                            newReviewerBean.setRecommendedActionCode("");
//                        }
//                    }
//                    if( reviewerRow.get(2) != null){
//                        Boolean reviewComplete = (Boolean) reviewerRow.get(1);
//                        newReviewerBean.setReviewComplete(reviewComplete.booleanValue());
//                    }
                    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - End
                    // 3282: Reviewer View of Protocol materials - End
                    
                    //For Case Fix #1879
                    
                    
                    for(int codeIndex=0; codeIndex < reviewerTypesSize;
                    codeIndex++){
                        ComboBoxBean sCode = (ComboBoxBean)
                        reviewerTypes.get(codeIndex);
                        if(sCode.getDescription().equals(
                                reviewerRow.elementAt(REVIEWER_REVIEW_TYPE_COLUMN).toString())){
                            newReviewerBean.setReviewerTypeCode(
                                    Integer.parseInt(sCode.getCode()));
                            newReviewerBean.setReviewerTypeDesc(
                                    sCode.getDescription());
                            break;
                        }
                    }
                    
                    // loop through available reviewers
                    if(existingReviewers != null){
                        for(revIndex = 0 ; revIndex < existingReviewers.size();
                        revIndex++ ){
                            reviewerBean = (ProtocolReviewerInfoBean)
                            existingReviewers.get(revIndex);
                            // add property change listener
                            reviewerBean.addPropertyChangeListener(
                                    new PropertyChangeListener(){
                                public void propertyChange(
                                        PropertyChangeEvent pce){
                                    reviewerBean.setAcType(UPDATE_RECORD);
                                    reviewersModified = true;
                                }
                            }
                            );
                            if( reviewerBean.getPersonId().equals(
                                    newReviewerBean.getPersonId())){
                                found  = true;
                                foundIndex = revIndex;
                                break;
                            }
                        }
                    }
                    if( !found ){
                        // new reviewer so add it to existing reviewers
                        newReviewerBean.setAcType(INSERT_RECORD);
                        newReviewerBean.setAWPersonId(null);
                        newReviewerBean.setAWReviewerTypeCode(0);
                        reviewersModified = true;
                        if(existingReviewers == null){
                            existingReviewers = new Vector();
                        }
                        existingReviewers.addElement(newReviewerBean);
                    }else{
                        /* set the selected reviewer type for the existing
                           reviewer and update in existing reviewers list */
                        reviewerBean.setReviewerTypeCode(
                                newReviewerBean.getReviewerTypeCode());
                        if(foundIndex != -1){
                            existingReviewers.set(foundIndex, reviewerBean);
                        }
                        
                    }
                    
                    
                }
                /* loop through all available reviewers and check whether any
                 * reviewer has been deleted. If yes set acType to DELETE_RECORD
                 * and send.
                 */
                if(existingReviewers != null){
                    ProtocolReviewerInfoBean existingReviewer;
                    for( int index = 0 ; index < existingReviewers.size();
                    index++ ){
                        existingReviewer = (ProtocolReviewerInfoBean)
                        existingReviewers.get(index);
                        found = false;
                        for(int currIndex = 0;
                        currIndex < currentReviewers.size(); currIndex++){
                            Vector reviewerRow
                                    = (Vector)currentReviewers.get(currIndex);
                            if( existingReviewer.getPersonId().equals(
                                    reviewerRow.elementAt(0))){
                                found = true;
                                break;
                            }
                            
                        }
                        
                        if( !found ) {
                            existingReviewer.setAcType(DELETE_RECORD);
                            reviewersModified = true;
                            existingReviewers.set(index,existingReviewer);
                        }
                        
                    }
                }
            }
        }
        submissionBean.setProtocolReviewer(existingReviewers);
        if(reviewersModified && functionType == MODIFY_MODE){
            submissionBean.setSequenceNumber(seqNo);
            submissionBean.setAcType(UPDATE_RECORD);
        }
        return submissionBean;
    }
    
    /** This method is used to throw the exception with the specifed message.
     *
     * @param msg String representing custom message to display.
     * @throws Exception with specified custom message.
     */
    private static void log(String msg) throws Exception {
        throw new Exception(msg);
    }
    
    /** This method is used to check whether user has modified any existing
     * details.
     *
     * @return true if user modified any details, else false.
     */
    public boolean isSaveRequired(){
        
        /*if( ( reviewerSelectionForm != null ) &&
        ( reviewerSelectionForm.isSaveRequired() ) && !saveRequired ){
            saveRequired  = true;
        }
        if( (scheduleSelectionForm != null)
        && (scheduleSelectionForm.isScheduleSelected())){
            if(functionType == ADD_MODE){
                saveRequired = true;
            }
        }
        return saveRequired;
         */
        //The above code is commented
        // Since this form will be used only for New/Inserting Data
        //for Defferred as well as new Submission.
        //So save REquired should always return true.
        return true;
    }
    
    /** This method is used to check whether the protocol has been successfully
     * submitted or not.
     *
     * @return true if the data has been successfully
     * submitted, else false.
     */
    public boolean isDataSaved(){
        return dataSaved;
    }
    
    /** This method is used to get the protocol details with latest sequence
     * number after updating the status.
     *
     * @return <CODE>ProtocolInfoBean</CODE> with latest sequence number and
     * with updated status information.
     */
    public ProtocolInfoBean getProtocolDetails(){
        
        return protocolInfo;
    }
    
    /** This method is used to connect to the database and send the submission
     * details for saving in to database. This method calls the private methods
     * <CODE>validateData()</CODE> and <CODE>getSubmissionData()</CODE> to validate and get the
     * submission details.
     *
     * @throws Exception with custom message thrown by either
     * of the private methods called in this method or from the server if it
     * is unable to save the submission details sent.
     */
    
    public void submitProtocol() throws Exception{
        if( validateData() ){
            
            /* in add mode check whether the reviewerSelectionForm is showed or
               not. If not showed, ask user whether he wants to identify the
               reviewers or not and show the reviewer selection form if he wishes.
               In Modify mode check whether there are any reviewers already selected.
               If not selected, check whether user is in ScheduleSelectionForm.
               If yes, ask user to identify reviewers. If user is in
               CommitteeSelectionForm and wishes to save the modifications by
               clicking 'X' button , dont ask the user to identify the reviewers
               though there are no reviewers selected.
             */
            if( functionType == ADD_MODE ) {
                /* added mandatory checking by ravi to prompt user for selecting reviewer
                 * if submission mode is mandatory */
                if ( reviewerSelectionForm == null && submissionMode == MANDATORY){
                    identifyReviewers = true;
                }
            }else if ( functionType == MODIFY_MODE ) {
                if( ( submissionBean.getProtocolReviewer() == null )
                &&   ( scheduleSelectionForm != null )
                && ( reviewerSelectionForm == null ) ) {
                    identifyReviewers = true;
                }
            }
            
            if( identifyReviewers ){
                int option = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(
                        "protoSubmissionFrm_exceptionCode.2003"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                if( option == CoeusOptionPane.SELECTION_NO){
                    saveSubmissionDetails();
                }else if( option == CoeusOptionPane.SELECTION_YES){
                    showReviewer();
                }
            }else{
                saveSubmissionDetails();
            }
            
        }
    }
    
    /**
     * This method is used to save the submission details to database after
     * getting confirmation from the user.
     */
    
    private void saveSubmissionDetails() throws Exception{
        
        if( ( submissionBean != null ) && ( scheduleSelectionForm != null ) ){
            String oldScheduleId = submissionBean.getScheduleId();
            ScheduleDetailsBean scheduleDetailsBean
                    = scheduleSelectionForm.getSelectedSchedule();
            if( scheduleDetailsBean != null){
                String scheduleId = scheduleDetailsBean.getScheduleId();
                if(!oldScheduleId.equals(scheduleId)){
                    saveRequired = true;
                }
            }
        }
        if(isSaveRequired()){
            int option = CoeusOptionPane.showQuestionDialog(
                    coeusMessageResources.parseMessageKey("protoSubmissionFrm_exceptionCode.2005"),
                    CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_YES);
            
            if(option == CoeusOptionPane.SELECTION_YES){
                //When map is available the submisison status is set to 'Routing In Progress', otherwise 'Submitted to Committee'
//                mapsFound = doSubmitApproveAction();
//                if(mapsFound){
//                    ProtocolSubmissionInfoBean submissionInfoBean = getSubmissionData();
//                    
//                    String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
//                    
//                    RequesterBean request = new RequesterBean();
//                    request.setFunctionType(SAVE_RECORD);
//                    Vector dataObjects = new Vector();
//                    if(selectedItem.equalsIgnoreCase(EXEMPT)) {
//                        setCheckedState(vecExpedited, false);
//                    } else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
//                        setCheckedState(vecExempt, false);
//                    }
//                    
//                    //included 27-Oct-2003 so as to send beans in insert mode - Start
//                    if(vecExemptToModify != null) {
//                        vecExemptToModify.removeAllElements();
//                    }
//                    if(vecExpeditedToModify != null) {
//                        vecExpeditedToModify.removeAllElements();
//                    }
//                    //included 27-Oct-2003 so as to send beans in insert mode - End
//                    
////                    submissionInfoBean.setProtocolExemptCheckList(getSubmissionBeans(vecExempt, vecExemptToModify));
////                    submissionInfoBean.setProtocolExpeditedCheckList(getSubmissionBeans(vecExpedited, vecExpeditedToModify));
//                    dataObjects.addElement(submissionInfoBean);
//                /* setting protocol status to "Submitted to IRB" after submitting the
//                        protocol to a committee schedule */
//                    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
////                    protocolInfo.setProtocolStatusCode(PROTO_STATUS_SUBMITTED_TO_IACUC);                
//                    if(submissionInfoBean.getSubmissionStatusCode() == SUB_STATUS_ROUTING_IN_PROGRESS){
//                        protocolInfo.setProtocolStatusCode(ROUTING_IN_PROGRESS_STATUS);
//                    }else{
//                        protocolInfo.setProtocolStatusCode(PROTO_STATUS_SUBMITTED_TO_IACUC);
//                    }
//                    // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End
//                    
//                    protocolInfo.setAcType(UPDATE_RECORD);
//                    protocolInfo.setVulnerableSubjectLists(null);
//                    protocolInfo.setLocationLists(null);
//                    protocolInfo.setInvestigators(null);
//                    protocolInfo.setAreaOfResearch(null);
//                    protocolInfo.setCorrespondants(null);
//                    protocolInfo.setFundingSources(null);
//                    protocolInfo.setKeyStudyPersonnel(null);
//                    dataObjects.addElement(protocolInfo);
//                    request.setDataObjects(dataObjects);
//                    AppletServletCommunicator comm
//                            = new AppletServletCommunicator(connectTo, request);
//                    setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
//                    comm.send();
//                    ResponderBean response = comm.getResponse();
//                    setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
//                    
//                    if (!response.isSuccessfulResponse()) {
//                        throw new Exception( response.getMessage());
//                    }else{
//                        dataSaved = true;
//                        protocolInfo = (ProtocolInfoBean)
//                        response.getDataObjects().elementAt(0);
//                        this.dispose();
//                        if(mapsFound){
//                            
//                            CoeusOptionPane.showInfoDialog("The Protocol " + protocolId +
//                                    " has been successfully submitted for routing") ;
//                            RoutingForm routingForm = new RoutingForm(protocolInfo, ModuleConstants.IACUC_MODULE_CODE, protocolId,
//                                    protocolInfo.getSequenceNumber(), leadUnitNumber, false,true);
//                            routingForm.display();
//                            setSubmissionStatusCode(routingForm.getSubmissionStatusCode());
//                        }
//                    }
//                }else{
                    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
//                    if(errDisplay){
                        saveSubmission();
//                    }else{
//                        this.dispose();
//                    }
                    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
//                }
                //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_End
            }else{
                this.dispose();
            }
        }else{
            this.dispose();
        }
        
    }
    
    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
    /* Save the submission details when protocol does not have a map
     * @Exception throws Exception
     */
    
    private void saveSubmission() throws Exception{
        
        ProtocolSubmissionInfoBean submissionInfoBean = getSubmissionData();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(SAVE_RECORD);
        Vector dataObjects = new Vector();
//        if(selectedItem.equalsIgnoreCase(EXEMPT)) {
//            setCheckedState(vecExpedited, false);
//        } else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
//            setCheckedState(vecExempt, false);
//        }
//        if(vecExemptToModify != null) vecExemptToModify.removeAllElements();
//        if(vecExpeditedToModify != null) vecExpeditedToModify.removeAllElements();
        
//        submissionInfoBean.setProtocolExemptCheckList(getSubmissionBeans(vecExempt, vecExemptToModify));
//        submissionInfoBean.setProtocolExpeditedCheckList(getSubmissionBeans(vecExpedited, vecExpeditedToModify));
        
        dataObjects.addElement(submissionInfoBean);
        
        // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - Start
//                    protocolInfo.setProtocolStatusCode(PROTO_STATUS_SUBMITTED_TO_IACUC);
        if(submissionInfoBean.getSubmissionStatusCode() == SUB_STATUS_ROUTING_IN_PROGRESS){
            protocolInfo.setProtocolStatusCode(ROUTING_IN_PROGRESS_STATUS);
        }else{
            protocolInfo.setProtocolStatusCode(PROTO_STATUS_SUBMITTED_TO_IACUC);
        }
        // Modified for COEUSQA-2556 : When a protocol is in routing it should not have a status of Submitted to IRB - End

        protocolInfo.setAcType(UPDATE_RECORD);
        protocolInfo.setVulnerableSubjectLists(null);
        protocolInfo.setLocationLists(null);
        protocolInfo.setInvestigators(null);
        protocolInfo.setAreaOfResearch(null);
        protocolInfo.setCorrespondants(null);
        protocolInfo.setFundingSources(null);
        protocolInfo.setKeyStudyPersonnel(null);
        dataObjects.addElement(protocolInfo);
        
        request.setDataObjects(dataObjects);
        AppletServletCommunicator comm
                = new AppletServletCommunicator(connectTo, request);
        
        setCursor( new Cursor( Cursor.WAIT_CURSOR ) );
        comm.send();
        ResponderBean response = comm.getResponse();
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        if (!response.isSuccessfulResponse()) {
            throw new Exception( response.getMessage());
        }else{
            dataSaved = true;
            protocolInfo = (ProtocolInfoBean)
            response.getDataObjects().elementAt(0);
            
            this.dispose();
            mapsFound = doSubmitApproveAction();
            if(mapsFound){
                request.setFunctionType(UPD_PROT_STATUS_AND_SUBMISSION_STATUS);
                Vector serverDataObjects = new Vector();
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean =  getProtocolSubmissionInfo(protocolInfo.getProtocolNumber(), protocolInfo.getSequenceNumber());
                serverDataObjects.add(protocolSubmissionInfoBean.getProtocolNumber());
                serverDataObjects.add(new Integer(protocolSubmissionInfoBean.getSequenceNumber()));
                serverDataObjects.add(new Integer(protocolSubmissionInfoBean.getSubmissionNumber()));
                serverDataObjects.add(new Integer(SUB_STATUS_ROUTING_IN_PROGRESS));
                serverDataObjects.add(new Integer(ROUTING_IN_PROGRESS_STATUS));
                request.setDataObjects(serverDataObjects);
                comm = new AppletServletCommunicator(connectTo, request);
                comm.send();
                response = comm.getResponse();
                if (!response.isSuccessfulResponse()) {
                    throw new Exception( response.getMessage());
                }else{
                    protocolInfo = (ProtocolInfoBean)response.getDataObject();
                }    
                CoeusOptionPane.showInfoDialog("The Protocol " + protocolId +
                        " has been successfully submitted for routing") ;
                RoutingForm routingForm = new RoutingForm(protocolInfo, ModuleConstants.IACUC_MODULE_CODE, protocolId,
                        protocolInfo.getSequenceNumber(), leadUnitNumber, false,true);
                routingForm.display();
                setSubmissionStatusCode(routingForm.getSubmissionStatusCode());
            }
        }
    }
    //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
    
    /**
     * This method is used to show the ReviewerSelctionForm
     */
    private void showReviewer(){
        /* check whether user has selected any schedule before displaying
           reivewerSelectionForm */
        boolean submitToSchedule = false;
        Vector reviewers = null;
        if( committeeSelectionForm.isCommitteeSelected() ||
                (!("").equals(txtCommitteeID.getText().trim()) &&
                !("").equals(txtCommitteeName.getText().trim()))) {   //Added by Jobin for the Bug Fix : 1196 - start (11-Sep-2004)
            
            CommitteeMaintenanceFormBean committeeBean = committeeSelectionForm.getSelectedCommittee();
            if (committeeBean != null) {
                String committeeId = committeeBean.getCommitteeId();
                txtCommitteeID.setText(committeeId );
                txtCommitteeName.setText(committeeBean.getCommitteeName());
            }
            /*if( scheduleSelectionForm != null &&
                    scheduleSelectionForm.isScheduleSelected()){
                ScheduleDetailsBean scheduleDetailsBean
                = scheduleSelectionForm.getSelectedSchedule();
                if( scheduleDetailsBean != null){
                    String scheduleId = scheduleDetailsBean.getScheduleId();
                    int maxCount = scheduleDetailsBean.getMaxProtocols();
                    int count = getProtocolSubCount( scheduleId );
                    boolean setScheduleData = false;
                    // check whether schedule exceeds its maximum protocol
                    //   review count. if exceeds inform the user with this detail.
                    //   If he still wants to submit to the same schedule, let him
                    //   submit
                    if ( count >= maxCount ) {
                        int option = CoeusOptionPane.showQuestionDialog(
             
                        coeusMessageResources.parseMessageKey(
                        "protoSubmissionFrm_exceptionCode.2002"),
                        CoeusOptionPane.OPTION_YES_NO,
                        CoeusOptionPane.DEFAULT_YES);
                        if( option == CoeusOptionPane.SELECTION_YES){
                            setScheduleData = true;
                            reviewers = getAvailableReviewers(scheduleId);
                        }else{
                            reviewers = null;
                        }
                    }else{
                        setScheduleData = true;
                        showScheduleWarning = false;
                    }
                    if( setScheduleData ) {
                        submitToSchedule = true;
                        txtScheduleId.setText(scheduleId);
                        Date schDate = scheduleDetailsBean.getScheduleDate();
                        DateUtils dtUtils = new DateUtils();
                        String convertedDate = dtUtils.formatDate(
                        schDate.toString(),"dd-MMM-yyyy");
                        if(convertedDate != null){
                            txtScheduleDate.setText(convertedDate);
                        }
                    }
                }
            }*/
            int scheduleStatus = validateSchedule();
            if( scheduleStatus == 1 ) {
                ScheduleDetailsBean scheduleDetailsBean
                        = scheduleSelectionForm.getSelectedSchedule();
                String scheduleId = scheduleDetailsBean.getScheduleId();
                
                reviewers = getAvailableReviewers(scheduleId);
                submitToSchedule = true;
                txtScheduleId.setText(scheduleId);
                Date schDate = scheduleDetailsBean.getScheduleDate();
                DateUtils dtUtils = new DateUtils();
                String convertedDate = dtUtils.formatDate(
                        schDate.toString(),"dd-MMM-yyyy");
                if(convertedDate != null){
                    txtScheduleDate.setText(convertedDate);
                }
                
            }else if( scheduleStatus == 0 ){
                if( submissionMode == OPTIONAL ) {
                    submitToSchedule = true;
                    // getting the committee id and get the reviewers
                    CommitteeMaintenanceFormBean committeeFormBean =
                            committeeSelectionForm.getSelectedCommittee();
                    if(committeeFormBean != null) {
                        committeeId = committeeFormBean.getCommitteeId();
                    }
                    reviewers = getAvailableReviewersForCommittee(committeeId);
                    txtScheduleId.setText("");
                    txtScheduleDate.setText("");
                }else{
                    submitToSchedule = false;
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "commSchdDetFrm_exceptionCode.1026"));
                }
            }
            
            if( submitToSchedule ){
                if(reviewers != null && reviewers.size() > 0){
                    //                    btnSelect.setEnabled( false );
                    displayScheduleComponents(true);
                    /*if(submissionBean != null && submissionBean.getProtocolReviewer() != null){
                        String oldScheduleId = submissionBean.getScheduleId();
                        if(oldScheduleId.equals(scheduleId)){
                            reviewerSelectionForm = new ReviewerSelectionForm(
                                    new ArrayList(submissionBean.getProtocolReviewer()));
                        }else{
                            reviewerSelectionForm = new ReviewerSelectionForm(null);
                        }
                    }else{
                        reviewerSelectionForm = new ReviewerSelectionForm(null);
                    }*/
                    identifyReviewers = false;
                    if( reviewerSelectionForm == null ) {
                        reviewerSelectionForm = new ReviewerSelectionForm(null);
                    }
                    // 3282: reviewer view of protocols
                    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - Start
//                    reviewerSelectionForm.setRecommendedActionTypes(recommendeActiontypes);
                    // Commented for COEUSQA-2707 : Available Recommended Actions for IACUC Reviewers - End
                    reviewerSelectionForm.setAvailableReviewers(new ArrayList(reviewers));
                    /* pass the available reivewer types and reviewers to
                       ReviewerSelectionForm */
                    reviewerSelectionForm.setReviewerTypes(reviewerTypes);
                    reviewerSelectionForm.setCommitteeId(txtCommitteeID.getText());
                    reviewerSelectionForm.setPreferredSize(
                            new Dimension( pnlContent.getWidth()-40 , pnlContent.getHeight()-40) );
                    reviewerSelectionForm.setComponentSizes();
                    if( scheduleSelectionForm != null ) {
                        scheduleSelectionForm.setVisible(false);
                    }else{
                        committeeSelectionForm.setVisible( false );
                    }
                    pnlContent.add(reviewerSelectionForm,BorderLayout.CENTER);
                } else {
                    if (scheduleStatus == 0) {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2009"));
                    } else {
                        CoeusOptionPane.showInfoDialog(coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2006"));
                    }
                }
            }
        }else{
            CoeusOptionPane.showErrorDialog(coeusMessageResources.parseMessageKey(
                    "commBaseWin_exceptionCode.1007"));
        }
    }
    
    /**
     * This method is used to get all the reviewers for a given committee,
     * to display in the available reviewers table of ReviewerSelectionForm
     *
     * @param scheduleId represents the schedule id which is used to fetch all
     * active members of the committee for that schedule
     * @return schedules collection of ProtocolReviewerInfoBean
     */
    
    private Vector getAvailableReviewersForCommittee( String committeeId ) {
        
        Vector vecBeans = new Vector();
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType('W');
        //request.setId(committeeId);
        Vector dataObject = new Vector();
        dataObject.addElement(committeeId);
        dataObject.addElement(protocolId);
        request.setDataObject(dataObject);
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
            vecBeans = (Vector)(
                    (Vector) response.getDataObjects()).elementAt(0);
        }
        return vecBeans;
        
    }
    
    private int validateSchedule() {
        if( scheduleSelectionForm != null &&
                scheduleSelectionForm.isScheduleSelected()){
            ScheduleDetailsBean scheduleDetailsBean
                    = scheduleSelectionForm.getSelectedSchedule();
            if( scheduleDetailsBean != null){
                String scheduleId = scheduleDetailsBean.getScheduleId();
                int maxCount = scheduleDetailsBean.getMaxProtocols();
                if( !oldScheduleId.equals( scheduleId ) ){
                    int count = getProtocolSubCount( scheduleId );
                    /* check whether schedule exceeds its maximum protocol
                       review count. if exceeds inform the user with this detail.
                       If he still wants to submit to the same schedule, let him
                       submit */
                    if ( count >= maxCount ) {
                        int option = CoeusOptionPane.showQuestionDialog(
                                
                                coeusMessageResources.parseMessageKey(
                                "protoSubmissionFrm_exceptionCode.2002"),
                                CoeusOptionPane.OPTION_YES_NO,
                                CoeusOptionPane.DEFAULT_YES);
                        if( option == CoeusOptionPane.SELECTION_YES){
                            oldScheduleId = scheduleId;
                            return 1;
                        }else{
                            oldScheduleId = "";
                            return -1;
                        }
                    }else{
                        oldScheduleId = scheduleId;
                        return 1;
                    }
                }else{
                    return 1;
                }
            }
        }
        oldScheduleId = "";
        return 0;
    }
    
    
    //prps start - jul 15 2003
    
    /**
     * This method is used to get the max submission number for a particular protocol
     * from OSP$Protocol_submission table, as every submission needs to increment the
     * submission_number field in this table.
     *
     * @param protocolId
     *
     * @return max submission number
     */
    private int getMaxSubmissionNumber( String protocolId ) {
        String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
        RequesterBean request = new RequesterBean();
        request.setFunctionType(PROTO_MAX_SUB_NUM);
        request.setId(protocolId);
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
        
        Integer count = new Integer(0);
        if (response.isSuccessfulResponse()) {
            count = (Integer)((Vector) response.getDataObjects()).elementAt(0);
        }
        if(count != null){
            return count.intValue();
        }
        return 0;
    }
    
    //prps end
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlMain = new javax.swing.JPanel();
        pnlContent = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        btnShowAll = new javax.swing.JButton();
        scrPnText = new javax.swing.JScrollPane();
        txtArText = new javax.swing.JTextArea();
        btnSelectReview = new javax.swing.JButton();
        btnClearSelection = new javax.swing.JButton();
        btnCommittee = new javax.swing.JButton();
        btnCheckList = new javax.swing.JButton();
        pnlSubmissionDetails = new javax.swing.JPanel();
        lblSubmissionDetails = new javax.swing.JLabel();
        lblSchId = new javax.swing.JLabel();
        lblSubmissionType = new javax.swing.JLabel();
        cmbSubmissionType = new edu.mit.coeus.utils.CoeusComboBox();
        lblTypeQualifier = new javax.swing.JLabel();
        cmbTypeQualifier = new edu.mit.coeus.utils.CoeusComboBox();
        lblReviewType = new javax.swing.JLabel();
        lblCommName = new javax.swing.JLabel();
        cmbReviewType = new edu.mit.coeus.utils.CoeusComboBox();
        txtCommitteeID = new edu.mit.coeus.utils.CoeusTextField();
        txtCommitteeName = new edu.mit.coeus.utils.CoeusTextField();
        txtScheduleId = new edu.mit.coeus.utils.CoeusTextField();
        txtScheduleDate = new edu.mit.coeus.utils.CoeusTextField();
        lblId = new javax.swing.JLabel();
        lblSchDate = new javax.swing.JLabel();
        lblTable = new javax.swing.JLabel();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setModal(true);
        setResizable(false);
        pnlMain.setLayout(new java.awt.GridBagLayout());

        pnlMain.setMinimumSize(new java.awt.Dimension(800, 420));
        pnlMain.setPreferredSize(new java.awt.Dimension(800, 420));
        pnlContent.setLayout(new java.awt.BorderLayout());

        pnlContent.setMinimumSize(new java.awt.Dimension(600, 200));
        pnlContent.setOpaque(false);
        pnlContent.setPreferredSize(new java.awt.Dimension(600, 200));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        pnlMain.add(pnlContent, gridBagConstraints);

        btnOk.setFont(CoeusFontFactory.getLabelFont());
        btnOk.setMnemonic('O');
        btnOk.setText("OK");
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 2, 10);
        pnlMain.add(btnOk, gridBagConstraints);

        btnCancel.setFont(CoeusFontFactory.getLabelFont());
        btnCancel.setMnemonic('C');
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 8, 10);
        pnlMain.add(btnCancel, gridBagConstraints);

        btnSelect.setFont(CoeusFontFactory.getLabelFont());
        btnSelect.setMnemonic('S');
        btnSelect.setText("Select Schedule");
        btnSelect.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 0, 10);
        pnlMain.add(btnSelect, gridBagConstraints);

        btnShowAll.setFont(CoeusFontFactory.getLabelFont());
        btnShowAll.setMnemonic('A');
        btnShowAll.setText("Show All");
        btnShowAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowAllActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 1, 10);
        pnlMain.add(btnShowAll, gridBagConstraints);

        scrPnText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtArText.setEditable(false);
        txtArText.setFont(CoeusFontFactory.getNormalFont());
        txtArText.setLineWrap(true);
        txtArText.setRows(3);
        txtArText.setText(coeusMessageResources.parseMessageKey(
            "protoSubmissionFrm_exceptionCode.2007"));
    txtArText.setOpaque(false);
    scrPnText.setViewportView(txtArText);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    pnlMain.add(scrPnText, gridBagConstraints);

    btnSelectReview.setFont(CoeusFontFactory.getLabelFont());
    btnSelectReview.setText("Select Reviewer");
    btnSelectReview.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnSelectReviewActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 11;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 10, 1, 10);
    pnlMain.add(btnSelectReview, gridBagConstraints);

    btnClearSelection.setFont(CoeusFontFactory.getLabelFont());
    btnClearSelection.setText("Clear Selection");
    btnClearSelection.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnClearSelectionActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 12;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 10, 10, 10);
    pnlMain.add(btnClearSelection, gridBagConstraints);

    btnCommittee.setFont(CoeusFontFactory.getLabelFont());
    btnCommittee.setText("Select Committee");
    btnCommittee.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCommitteeActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 10);
    pnlMain.add(btnCommittee, gridBagConstraints);

    btnCheckList.setFont(CoeusFontFactory.getLabelFont());
    btnCheckList.setText("Check List");
    btnCheckList.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnCheckListActionPerformed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 6;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
    pnlMain.add(btnCheckList, gridBagConstraints);

    pnlSubmissionDetails.setLayout(new java.awt.GridBagLayout());

    lblSubmissionDetails.setFont(CoeusFontFactory.getLabelFont());
    lblSubmissionDetails.setForeground(new java.awt.Color(0, 0, 204));
    lblSubmissionDetails.setText("Submission Details");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipady = 14;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
    pnlSubmissionDetails.add(lblSubmissionDetails, gridBagConstraints);

    lblSchId.setFont(CoeusFontFactory.getLabelFont());
    lblSchId.setText("Schedule ID :");
    lblSchId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 5);
    pnlSubmissionDetails.add(lblSchId, gridBagConstraints);

    lblSubmissionType.setFont(CoeusFontFactory.getLabelFont());
    lblSubmissionType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblSubmissionType.setText("Type :");
    lblSubmissionType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
    pnlSubmissionDetails.add(lblSubmissionType, gridBagConstraints);

    cmbSubmissionType.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbSubmissionType.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbSubmissionTypeItemStateChanged(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.ipadx = 114;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 13);
    pnlSubmissionDetails.add(cmbSubmissionType, gridBagConstraints);

    lblTypeQualifier.setFont(CoeusFontFactory.getLabelFont());
    lblTypeQualifier.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblTypeQualifier.setText("Type Qualifier :");
    lblTypeQualifier.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 5, 5);
    pnlSubmissionDetails.add(lblTypeQualifier, gridBagConstraints);

    cmbTypeQualifier.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbTypeQualifierItemStateChanged(evt);
        }
    });
    cmbTypeQualifier.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            cmbQualifierKeyPressed(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.gridwidth = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 10);
    pnlSubmissionDetails.add(cmbTypeQualifier, gridBagConstraints);

    lblReviewType.setFont(CoeusFontFactory.getLabelFont());
    lblReviewType.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblReviewType.setText("Review Type :");
    lblReviewType.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    pnlSubmissionDetails.add(lblReviewType, gridBagConstraints);

    lblCommName.setFont(CoeusFontFactory.getLabelFont());
    lblCommName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblCommName.setText("Committee Name :");
    lblCommName.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
    pnlSubmissionDetails.add(lblCommName, gridBagConstraints);

    cmbReviewType.setMinimumSize(new java.awt.Dimension(100, 20));
    cmbReviewType.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cmbReviewTypeItemStateChanged(evt);
        }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.gridwidth = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 46;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
    pnlSubmissionDetails.add(cmbReviewType, gridBagConstraints);

    txtCommitteeID.setEditable(false);
    txtCommitteeID.setMinimumSize(new java.awt.Dimension(4, 20));
    txtCommitteeID.setPreferredSize(new java.awt.Dimension(120, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.ipadx = 111;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 17);
    pnlSubmissionDetails.add(txtCommitteeID, gridBagConstraints);

    txtCommitteeName.setEditable(false);
    txtCommitteeName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
    txtCommitteeName.setMinimumSize(new java.awt.Dimension(4, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 10);
    pnlSubmissionDetails.add(txtCommitteeName, gridBagConstraints);

    txtScheduleId.setEditable(false);
    txtScheduleId.setPreferredSize(new java.awt.Dimension(120, 20));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.ipadx = 111;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 17);
    pnlSubmissionDetails.add(txtScheduleId, gridBagConstraints);

    txtScheduleDate.setEditable(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.ipadx = 107;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
    pnlSubmissionDetails.add(txtScheduleDate, gridBagConstraints);

    lblId.setFont(CoeusFontFactory.getLabelFont());
    lblId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    lblId.setText("Committee ID :");
    lblId.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 5);
    pnlSubmissionDetails.add(lblId, gridBagConstraints);

    lblSchDate.setFont(CoeusFontFactory.getLabelFont());
    lblSchDate.setText("Scheduled Date :");
    lblSchDate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 5);
    pnlSubmissionDetails.add(lblSchDate, gridBagConstraints);

    lblTable.setFont(CoeusFontFactory.getLabelFont());
    lblTable.setForeground(new java.awt.Color(0, 0, 204));
    lblTable.setText("Committee List");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.ipady = 14;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
    pnlSubmissionDetails.add(lblTable, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 4;
    gridBagConstraints.gridy = 2;
    pnlMain.add(pnlSubmissionDetails, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    getContentPane().add(pnlMain, gridBagConstraints);

    pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cmbQualifierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbQualifierKeyPressed
        //prps code starts
        int keyCode = evt.getKeyCode() ;
        if (keyCode == KeyEvent.VK_DELETE) {
            cmbTypeQualifier.setSelectedIndex(0) ;
        }
        
        //prps code ends
    }//GEN-LAST:event_cmbQualifierKeyPressed
    
    private void btnCheckListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckListActionPerformed
        // Add your handling code here:
        //Added By sharath for CheckList - START
        String selectedItem = cmbReviewType.getSelectedItem().toString().trim();
        //if(functionType == ADD_MODE) {
        if(selectedItem.equalsIgnoreCase(EXPEDITED)){
            checkList.setFormData(vecExpedited);
        } else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
            checkList.setFormData(vecExempt);
        }
        //} //Add Mode
        int button = checkList.display();
//        System.out.println(button);
        if(button == checkList.OK && checkList.isSaveRequired()) {
            saveRequired =true ;
            if(selectedItem.equalsIgnoreCase(EXPEDITED)){
                vecExpedited = checkList.getFormData();
            }else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
                vecExempt = checkList.getFormData();
            }
        }
        
        //Added By sharath for CheckList - END
    }//GEN-LAST:event_btnCheckListActionPerformed
    
    //Added By sharath for CheckList - START
//    private void prepareCheckList(Vector masterData, Vector toModify) {
//        if(toModify == null || toModify.size() < 1) return ;
//        String code;
//        toModify:for(int indexToModify = 0; indexToModify < toModify.size(); indexToModify++) {
//            code = ((CheckListBean)toModify.get(indexToModify)).getCheckListCode();
//            for(int index = 0; index < masterData.size(); index++) {
//                if(code.equals( ((CheckListBean)masterData.get(index)).getCheckListCode()) ){
//                    masterData.set(index, toModify.get(indexToModify));
//                    continue toModify;
//                }
//            }//For master Data
//        }//For toModify
//    }//End prepareCheckList(Vector masterData, Vector toModify)
    
    //Prepares Vector of ProtocolReviewTypeCheckListBean to Send to Server
    private Vector getSubmissionBeans(Vector masterData, Vector toModify) {
        Vector data = new Vector();
        ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean;
        CheckListBean checkListBean;
        
        if(toModify != null && toModify.size() > 0) {
            String code;
            boolean isChecked;
            String description;
            masterData:for(int index = 0; index < masterData.size(); index++) {
                code = ((CheckListBean)masterData.get(index)).getCheckListCode();
                isChecked = ((CheckListBean)masterData.get(index)).isChecked();
                description = ((CheckListBean)masterData.get(index)).getDescription();
                
                for(int indexToModify = 0; indexToModify < toModify.size(); indexToModify++) {
                    checkListBean = (CheckListBean)toModify.get(indexToModify);
                    if(code.equals(checkListBean.getCheckListCode())){
                        if(!isChecked) {
                            //protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                            //protocolReviewTypeCheckListBean.setCheckListCode(checkListBean.getCheckListCode());
                            //protocolReviewTypeCheckListBean.setDescription(checkListBean.getDescription());
                            protocolReviewTypeCheckListBean = (ProtocolReviewTypeCheckListBean)toModify.get(indexToModify);
                            protocolReviewTypeCheckListBean.setAcType(DELETE_RECORD);
                            data.add(protocolReviewTypeCheckListBean);
                            continue masterData;
                        } else continue masterData;
                    }
                }//For toModify
                if(isChecked) {
                    protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                    protocolReviewTypeCheckListBean.setCheckListCode(code);
                    protocolReviewTypeCheckListBean.setDescription(description);
                    protocolReviewTypeCheckListBean.setAcType(INSERT_RECORD);
                    protocolReviewTypeCheckListBean.setProtocolNumber(protocolInfo.getProtocolNumber());
                    protocolReviewTypeCheckListBean.setSequenceNumber(protocolInfo.getSequenceNumber());
                    data.add(protocolReviewTypeCheckListBean);
                }
            }//Formaster Data
        }//if
        else {
            for(int index = 0; index < masterData.size(); index++) {
                checkListBean = (CheckListBean)masterData.get(index);
                if(checkListBean.isChecked()) {
                    protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                    protocolReviewTypeCheckListBean.setCheckListCode(checkListBean.getCheckListCode());
                    protocolReviewTypeCheckListBean.setDescription(checkListBean.getDescription());
                    protocolReviewTypeCheckListBean.setAcType(INSERT_RECORD);
                    protocolReviewTypeCheckListBean.setProtocolNumber(protocolInfo.getProtocolNumber());
                    protocolReviewTypeCheckListBean.setSequenceNumber(protocolInfo.getSequenceNumber());
                    data.add(protocolReviewTypeCheckListBean);
                }
            }
        }//Not Selected
        return data;
    }
    
    private void setCheckedState(Vector data, boolean checked) {
        for(int index = 0; index < data.size(); index++) {
            ((CheckListBean)data.get(index)).setIsChecked(checked);
        }
    }
    //Added By sharath for CheckList - END
    
    private void btnCommitteeActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCommitteeActionPerformed
    {//GEN-HEADEREND:event_btnCommitteeActionPerformed
        if( showCommittee ){
            if (scheduleSelectionForm != null) {
                scheduleSelectionForm.setVisible(false) ;
                pnlContent.remove(scheduleSelectionForm) ;
                scheduleSelectionForm = null ;
            }
            if (reviewerSelectionForm != null) {
                reviewerSelectionForm.setVisible(false) ;
                pnlContent.remove(reviewerSelectionForm) ;
                reviewerSelectionForm = null ;
            }
            committeeSelectionForm.setVisible(true) ;
            lblTable.setText("Committee List") ;
            showSchedule = true ;
            btnSelect.setEnabled(true) ;
            btnSelectReview.setEnabled(true) ;
            btnCommittee.setEnabled(false) ;
            btnClearSelection.setEnabled(true) ;
            btnShowAll.setEnabled(true) ;
            oldScheduleId = "";
        }
    }//GEN-LAST:event_btnCommitteeActionPerformed
    
    private void btnClearSelectionActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnClearSelectionActionPerformed
    {//GEN-HEADEREND:event_btnClearSelectionActionPerformed
        if (committeeSelectionForm != null) {
            if (committeeSelectionForm.isVisible()) {
                committeeSelectionForm.setCommitteeList(committeeList) ;
                // if committee is cleared, then both committee n schedule shud be cleared
                txtCommitteeID.setText("") ;
                txtCommitteeName.setText("") ;
                txtScheduleId.setText("") ;
                txtScheduleDate.setText("") ;
            }
        }
        if (scheduleSelectionForm!= null) {
            if(scheduleSelectionForm.isVisible()) {
                scheduleSelectionForm.setScheduleList(scheduleList) ;
                txtScheduleId.setText("") ;
                txtScheduleDate.setText("") ;
            }
        }
    }//GEN-LAST:event_btnClearSelectionActionPerformed
    
    
    private void btnSelectReviewActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSelectReviewActionPerformed
    {//GEN-HEADEREND:event_btnSelectReviewActionPerformed
        //if ( scheduleSelectionForm != null) {
        //if( showReviewer ) {
        showReviewer();
        showCommittee = true ;
        btnCommittee.setEnabled(true) ;
        showSchedule = true;
        btnSelect.setEnabled(true) ;
        
        if (reviewerSelectionForm != null) {
            if (reviewerSelectionForm.isVisible()) {
                btnSelectReview.setEnabled(false) ;
                btnClearSelection.setEnabled(false) ;
                btnShowAll.setEnabled(false) ;
                lblTable.setText("Reviewer List") ;
            }
        }
        //}
        //}
        
    }//GEN-LAST:event_btnSelectReviewActionPerformed
    
    private void cmbReviewTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbReviewTypeItemStateChanged
        // if user changes the selection then set saveRequired to true
        saveRequired = true;
        //Added By sharath for CheckList - START
        if(evt.getStateChange() == evt.DESELECTED){
            return ;
        }
        
        String newSelectedItem = cmbReviewType.getSelectedItem().toString().trim();
        if(selectedItem == null) selectedItem = newSelectedItem;
        else if((selectedItem.equalsIgnoreCase(EXEMPT) || selectedItem.equalsIgnoreCase(EXPEDITED)) && !selectedItem.equalsIgnoreCase(newSelectedItem)) {
            int checkedSize = 0;
            if(selectedItem.equalsIgnoreCase(EXEMPT)) {
                checkList.setFormData(vecExempt);
                checkedSize = checkList.getData(true).size();
            } else if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
                checkList.setFormData(vecExpedited);
                checkedSize = checkList.getData(true).size();
            }
            if(checkedSize > 0) {
                int selection = CoeusOptionPane.showQuestionDialog(
                        coeusMessageResources.parseMessageKey(LOSE_CHECKED_LIST_INFO),CoeusOptionPane.OPTION_YES_NO,CoeusOptionPane.DEFAULT_NO);
                if(selection == CoeusOptionPane.SELECTION_YES) {
                    selectedItem = newSelectedItem;
                } else if(selection == CoeusOptionPane.SELECTION_NO) {
                    cmbReviewType.setSelectedIndex(selectedIndex);
                }
            } else selectedItem = newSelectedItem;
        }else{
            selectedItem = newSelectedItem;
        }
        
        if(selectedItem.equalsIgnoreCase(EXPEDITED)) {
            selectedIndex = cmbReviewType.getSelectedIndex();
            btnCheckList.setEnabled(true);
        } else if(selectedItem.equalsIgnoreCase(EXEMPT)) {
            selectedIndex = cmbReviewType.getSelectedIndex();
            btnCheckList.setEnabled(true);
        } else btnCheckList.setEnabled(false);
        //Added By sharath for CheckList - END
    }//GEN-LAST:event_cmbReviewTypeItemStateChanged
    
    private void cmbTypeQualifierItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTypeQualifierItemStateChanged
        // if user changes the selection then set saveRequired to true
        
        saveRequired = true;
    }//GEN-LAST:event_cmbTypeQualifierItemStateChanged
    
    private void cmbSubmissionTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbSubmissionTypeItemStateChanged
        // if user changes the selection then set saveRequired to true
        saveRequired = true;
        //Coeus enhancement Case #1793 - step 3: start
        ComboBoxBean newSelectedItem = (ComboBoxBean)cmbSubmissionType.getSelectedItem();
        if(!newSelectedItem.getCode().equals("")) {
            Vector reviewTypesForSubmission = getReviewTypeForSubmission(Integer.parseInt(newSelectedItem.getCode()));
            if( reviewTypesForSubmission != null ){
                cmbReviewType.removeAllItems();
                //cmbReviewType.addItem(new ComboBoxBean("",""));
                for ( int loopIndex = 0 ; loopIndex < reviewTypesForSubmission.size();
                loopIndex++ ) {
                    ComboBoxBean comboBean = (ComboBoxBean)reviewTypesForSubmission.get(loopIndex);
                    cmbReviewType.addItem(comboBean);
                }
            }
            if(showDefaultval) {
                for(int typeRow = 0; typeRow < cmbReviewType.getItemCount();
                typeRow++){
                    if(((ComboBoxBean)cmbReviewType.getItemAt(
                            typeRow)).getCode().equals((new Integer(reviewCode)).toString())){
                        cmbReviewType.setSelectedIndex(typeRow);
                        break;
                    }else {
                        cmbReviewType.setSelectedIndex(0);
                    }
                }
                showDefaultval=false;
            }
        }
        //Coeus enhancement Case #1793 - step 3: end
    }//GEN-LAST:event_cmbSubmissionTypeItemStateChanged
    
    private void btnShowAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowAllActionPerformed
        /* fetch all the available committees and send them as collection to
           committeeSelectionForm */
        committeeSelectionForm.setCommitteeList(
                new ArrayList(getCommitteList()));
        
    }//GEN-LAST:event_btnShowAllActionPerformed
    
    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        
        // check whether the ScheduleSelectionForm should be displayed or not
        if( showSchedule){
            if(committeeSelectionForm.isCommitteeSelected() || // add the committee details to the screen so that u can use this
                    (!("").equals(txtCommitteeID.getText().trim()) && !("").equals(txtCommitteeName.getText().trim()))) {   //Added by Jobin for the Bug Fix : 1196 - start (11-Sep-2004)
                CommitteeMaintenanceFormBean committeeBean =
                        committeeSelectionForm.getSelectedCommittee();
                Vector schedules = new Vector();
                if(committeeBean != null) {
                    txtCommitteeID.setText(committeeBean.getCommitteeId());
                    txtCommitteeName.setText(committeeBean.getCommitteeName());
                    lblTable.setText("Schedule List") ;
                    txtScheduleId.setText("");
                    txtScheduleDate.setText("");
                    schedules = getSchedules(committeeBean.getCommitteeId());
                } else {
                    //if the committee is not selected then get the displayed committe id
                    //to get the schedules -- added by Jobin
                    schedules = getSchedules(txtCommitteeID.getText().trim());
                }
                if(schedules != null && schedules.size() >0 ) {
                    scheduleList = new ArrayList(schedules) ;
                    btnSelectReview.setEnabled(true) ;
                    btnCommittee.setEnabled(true) ;
                    showSchedule = false;
                    btnSelect.setEnabled(false) ;
                    if( reviewerSelectionForm != null ) {
                        reviewerSelectionForm.setVisible(false);
                        reviewerSelectionForm = null;
                    }
                    btnShowAll.setEnabled(false) ;
                    btnClearSelection.setEnabled(true) ;
                    showReviewer = true;
                    showCommittee = true ;
                    formatFields(false);
                    
                        /* fetch the schedules whose status is "SCHEDULED" for
                           the selected committee and send them to
                           scheduleSelectionForm */
                    if( scheduleSelectionForm == null ) {
                        scheduleSelectionForm
                                = new ScheduleSelectionForm(new ArrayList(
                                schedules));
                        
                        scheduleSelectionForm.setPreferredSize(
                                new java.awt.Dimension(pnlContent.getWidth()-40,
                                pnlContent.getHeight() - 40) );
                    }else{
                        scheduleSelectionForm.setVisible(true);
                    }
                    committeeSelectionForm.setVisible(false);
                    
                    pnlContent.add(scheduleSelectionForm,
                            BorderLayout.CENTER);
                        /* selecting the user selected schedule. this code should
                           be after adding the component otherwise scrolling to
                           selected schedule won't happen*/
                    if( submissionBean != null){
                        String scheduleId = submissionBean.getScheduleId();
                        scheduleSelectionForm.setSelectedSchedule(
                                scheduleId);
                    }
                } else{
                    CoeusOptionPane.showErrorDialog(
                            coeusMessageResources.parseMessageKey(
                            "protoSubmissionFrm_exceptionCode.2004"));
                }
                
            }else{
                CoeusOptionPane.showErrorDialog(
                        coeusMessageResources.parseMessageKey(
                        "commBaseWin_exceptionCode.1007"));
            }
        }
    }//GEN-LAST:event_btnSelectActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        int option = CoeusOptionPane.SELECTION_NO;
        
        /* take the user confirmation for saving the details if anything has
           been modified before closing the dialog. */
        
        /* Case 652
            Cancelling submission - prahalad Mar 12 2004
         */
        
        isScheduleChanged();
        if(isSaveRequired()){
            option = CoeusOptionPane.showQuestionDialog( "Do you want to cancel the submission?",
                    CoeusOptionPane.OPTION_YES_NO,
                    CoeusOptionPane.DEFAULT_YES);
        }
        if(option == CoeusOptionPane.SELECTION_YES) {
            saveRequired = false;
            this.dispose();
        }
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        try{
            
            if (validateSubmit()) //prps added this if condtn - jul 21 2003
                submitProtocol();
        }catch(Exception e){
            e.printStackTrace();
            CoeusOptionPane.showErrorDialog(e.getMessage());
        }
        
    }//GEN-LAST:event_btnOkActionPerformed
    
    //prps start - jul 21 2003
    // this validation is done only when the submission mode is in OPTIONAL.
    // if the user doesnt select commitee  or if the user selects committee
    // and doesnt select schedule, prompt the user abt this. User shud be
    // able to submit a protocol without committe or schedule selection
    private boolean validateSubmit() {
        if (submissionMode == OPTIONAL) {   // check if committee selected
            if(committeeSelectionForm.isCommitteeSelected() || // add the committee details to the screen so that u can use this
                    (!("").equals(txtCommitteeID.getText().trim()) && !("").equals(txtCommitteeName.getText().trim()))) {   //Added by Jobin for the Bug Fix : 1196 - start (11-Sep-2004)
                // to save it to the table
                CommitteeMaintenanceFormBean committeeBean =
                        committeeSelectionForm.getSelectedCommittee();
                if(committeeBean != null) {
                    txtCommitteeID.setText(committeeBean.getCommitteeId());
                    txtCommitteeName.setText(committeeBean.getCommitteeName());
                }
                
                // check if schedule selected
                if( scheduleSelectionForm != null) {
                    if (scheduleSelectionForm.getSelectedSchedule() == null ) {
                        int choice = CoeusOptionPane.showQuestionDialog("Are you sure you want to submit this protocol without selecting a schedule ?"
                                , CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES) ;
                        if (choice == CoeusOptionPane.SELECTION_YES) {
                                /* modified by ravi not to ask for reviewer selection if
                                 * user doesn't want to select schedule itself */
                            identifyReviewers = false;
                            return true ;
                        } else {
                            return false ;
                        }
                    }else{
                        return validateSchedule() == 1 ? true : false;
                    }
                } else {
                    int choice = CoeusOptionPane.showQuestionDialog("Are you sure you want to submit this protocol without selecting a schedule ?"
                            , CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES) ;
                    if (choice == CoeusOptionPane.SELECTION_YES) {
                                /* modified by ravi not to ask for reviewer selection if
                                 * user doesn't want to select schedule itself */
                        identifyReviewers = false;
                        return true ;
                    } else {
                        return false ;
                    }
                }
            } else { // committee not selected
                int choice = CoeusOptionPane.showQuestionDialog("Are you sure you want to submit this protocol without selecting a committee ?", CoeusOptionPane.OPTION_YES_NO, CoeusOptionPane.DEFAULT_YES) ;
                if (choice == CoeusOptionPane.SELECTION_YES) {
                            /* modified by ravi not to ask for reviewer selection if
                             * user doesn't want to select committee itself */
                    identifyReviewers = false;
                    return true ;
                } else {
                    return false ;
                }
            }
            
        }
        
        return true ;
    }//end validateSubmit
    
    //prps end
    
    public void requestDefaultFocusForComponent(){
        cmbSubmissionType.requestFocusInWindow();
    }
    //Added for case 2785 Routing enhancement - start
    /**
     * Builds the maps for the protocol. Gets the information whether maps are
     * available
     *
     * @returns boolean true if maps exist
     */
    public boolean doSubmitApproveAction() {
        
        try {
            
            Vector requestParameters = new Vector();
            //Passes Protocol Number, unit number,"S" submit for approve option
            requestParameters.add(protocolInfo.getProtocolNumber());
            requestParameters.add(new Integer(protocolInfo.getSequenceNumber()));
            requestParameters.add(leadUnitNumber);
            requestParameters.add("S");
            RequesterBean requesterBean = new RequesterBean();
            requesterBean.setFunctionType(SUBMIT_FOR_APPROVE);
            requesterBean.setDataObjects(requestParameters);
            String connectTo = CoeusGuiConstants.CONNECTION_URL + SUBMISSION_SERVLET;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo, requesterBean);
            comm.send();
            ResponderBean response = comm.getResponse();
            Vector submitApproveResultSetData = null;
            if(response.isSuccessfulResponse() && response.getDataObjects() != null){
                submitApproveResultSetData = response.getDataObjects();
                if(submitApproveResultSetData != null && submitApproveResultSetData.size() > 0) {
                    if(submitApproveResultSetData.get(0) != null) {
                        // returns Integer representing Action on server end
                        if(((Integer) submitApproveResultSetData.get(0)).intValue() > 0) {
                            return true;
                        } //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_Start
                        else if(((Integer) submitApproveResultSetData.get(0)).intValue() ==  0) {
                            errDisplay = true;
                        }
                        //COEUSQA-2554_Parent key not found error while submitting protocol for Routing_end
                    }
                }
            } else if(!response.isSuccessfulResponse()){
                CoeusOptionPane.showErrorDialog(response.getMessage());
            }
        }catch(Exception expec) {
            expec.printStackTrace();
        }
        return false;
    }
    
    
    /**
     * Get the submission information for the protocol from the database and filters
     * the result with current protocol id and sequence id
     *
     * @param protocolId protocol number
     * @param sequenceId sequence number
     * @return ProtocolSubmissionInfoBean
     */
    public ProtocolSubmissionInfoBean getProtocolSubmissionInfo(String protocolId, int sequenceId){
        
        boolean protSubmissionDetailsFound = false;
        ProtocolSubmissionInfoBean maxSubmissionInfoBean = null;
        SubmissionDetailsBean detailsBean = new SubmissionDetailsBean() ;
        detailsBean.setProtocolNumber(protocolId) ;
        detailsBean.setScheduleId(null) ;
        detailsBean.setSequenceNumber(new Integer(sequenceId)) ;
        Vector vecDetails = getSubmissionDetails(detailsBean);
        
        if(vecDetails!=null && vecDetails.size()>0){
            Vector vecSubmissionDetails = (Vector)vecDetails.get(0);
            if(vecSubmissionDetails!=null){
                ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
                int maxSubmissionNo = 0;
                
                for(int i=0; i<vecSubmissionDetails.size(); i++){
                    protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)vecSubmissionDetails.get(i);
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
    
    private Vector getSubmissionDetails(SubmissionDetailsBean detailsBean) {
        Vector vecDetails= new Vector() ;
        try {    
            RequesterBean requester = new RequesterBean();
            requester.setFunctionType('G') ;
            requester.setDataObject(detailsBean) ;
            
            String connectTo =CoeusGuiConstants.CONNECTION_URL + "/IacucProtoSubmissionDetailsServlet" ;
            AppletServletCommunicator comm = new AppletServletCommunicator(connectTo,requester);
            comm.send();
            
            ResponderBean responder = comm.getResponse();
            if (responder.isSuccessfulResponse()) {
                vecDetails = (Vector)responder.getDataObject();
            }
        } catch(Exception ex) {
            ex.printStackTrace() ;
        }
        return vecDetails;
    }
    
    public String getLeadUnitNumber() {
        return leadUnitNumber;
    }
    
    public void setLeadUnitNumber(String leadUnitNumber) {
        this.leadUnitNumber = leadUnitNumber;
    }
    
    public boolean isMapsFound() {
        return mapsFound;
    }
    
    public void setMapsFound(boolean mapsFound) {
        this.mapsFound = mapsFound;
    }
    
    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }
    
    public void setSubmissionStatusCode(int submissionStatusCode) {
        this.submissionStatusCode = submissionStatusCode;
    }
    //Added case 2785 for Routing enhancement - end
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancel;
    public javax.swing.JButton btnCheckList;
    public javax.swing.JButton btnClearSelection;
    public javax.swing.JButton btnCommittee;
    public javax.swing.JButton btnOk;
    public javax.swing.JButton btnSelect;
    public javax.swing.JButton btnSelectReview;
    public javax.swing.JButton btnShowAll;
    public edu.mit.coeus.utils.CoeusComboBox cmbReviewType;
    public edu.mit.coeus.utils.CoeusComboBox cmbSubmissionType;
    public edu.mit.coeus.utils.CoeusComboBox cmbTypeQualifier;
    public javax.swing.JLabel lblCommName;
    public javax.swing.JLabel lblId;
    public javax.swing.JLabel lblReviewType;
    public javax.swing.JLabel lblSchDate;
    public javax.swing.JLabel lblSchId;
    public javax.swing.JLabel lblSubmissionDetails;
    public javax.swing.JLabel lblSubmissionType;
    public javax.swing.JLabel lblTable;
    public javax.swing.JLabel lblTypeQualifier;
    public javax.swing.JPanel pnlContent;
    public javax.swing.JPanel pnlMain;
    public javax.swing.JPanel pnlSubmissionDetails;
    public javax.swing.JScrollPane scrPnText;
    public javax.swing.JTextArea txtArText;
    public edu.mit.coeus.utils.CoeusTextField txtCommitteeID;
    public edu.mit.coeus.utils.CoeusTextField txtCommitteeName;
    public edu.mit.coeus.utils.CoeusTextField txtScheduleDate;
    public edu.mit.coeus.utils.CoeusTextField txtScheduleId;
    // End of variables declaration//GEN-END:variables
    
}
