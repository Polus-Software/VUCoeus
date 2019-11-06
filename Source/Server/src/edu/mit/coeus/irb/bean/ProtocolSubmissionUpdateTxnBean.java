/*
 * @(#)ProtocolSubmissionUpdateTxnBean.java 1.0 11/27/02 11:21 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 17-JAN-2012
 * by Bharati
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.RoleInfoBean;
import edu.mit.coeus.departmental.bean.DepartmentPersonTxnBean;
import edu.mit.coeus.irb.notification.ProtocolMailNotification;
//import edu.mit.coeus.mail.MailHandler;
import edu.mit.coeus.mail.bean.MailMessageInfoBean;
import edu.mit.coeus.mail.bean.PersonRecipientBean;
import edu.mit.coeus.mailaction.bean.MailNotification;
import edu.mit.coeus.routing.bean.RoutingDetailsBean;
import edu.mit.coeus.routing.bean.RoutingTxnBean;
import edu.mit.coeus.routing.bean.RoutingUpdateTxnBean;
//import edu.mit.coeus.utils.CoeusConstants;
import edu.mit.coeus.utils.CoeusVector;
//import edu.mit.coeus.utils.MailActions;
import edu.mit.coeus.utils.ModuleConstants;
import edu.mit.coeus.utils.TypeConstants;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.mail.MailProperties;
import java.text.MessageFormat;
import java.util.HashMap;

import java.util.Vector;
import java.sql.Timestamp;
//import java.sql.Date;

import static edu.mit.coeus.utils.mail.MailPropertyKeys.IRB_NOTIFICATION;
import static edu.mit.coeus.utils.mail.MailPropertyKeys.DOT;
import static edu.mit.coeus.utils.mail.MailPropertyKeys.MESSAGE;
        
/**
 * This class provides the methods for performing modify/insert and delete
 * procedure executions for a Protocol Submission functionality. Various 
 * methods are used to modify/insert the data for "ProtocolSubmission" 
 * from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the
 * database interaction.
 *
 * @version 1.0 on November 27, 2002, 11:21 AM
 * @author  Mukundan C
 */

public class ProtocolSubmissionUpdateTxnBean {
    
    // Instance of a dbEngine
    private DBEngineImpl dbEngine;
    // holds the dataset name
    private static final String DSN = "Coeus";
    // holds the charater for insert
    private static final String INSERT = "I";
    // holds the userId for the logged in user
    private String userId;
    // holds the CoeusFunctions instance
    CoeusFunctions coeusFunctions = null;
    
    // holds sequence number of protocol
    int seqNumber; 
    // holds the updated/inserted timestamp    
    Timestamp dbTimestamp;
    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - Start
    private static int USER_ADDED_AS_PROTO_REVIEWER_ACTION_CODE = 402;
    private static int USER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE = 404;
    
    private static int REVIEWER_ADDED_ACTION_CODE = 401;
    private static int REVIEWER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE = 403;
    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - End
/** Creates a new instance of ProtocolSubmissionUpdateTxnBean */
    public ProtocolSubmissionUpdateTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    /**
     * Creates new instance of ProtocolSubmissionUpdateTxnBean and initializes user id.
     * @param userId String which the Loggedin userid
     */
    public ProtocolSubmissionUpdateTxnBean(String userId) {
        this.userId = userId;
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  Method used to update/insert all the details of a Protocol Submission.
     *  <li>To fetch the data, it uses upd_proto_submission procedure.
     *
     *  @param submissionInfoBean  this bean contains data for insert.
     *  @param protocolInfoBean this bean contains data for
     *  modifying the protocol details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public boolean addUpdProtocolSubmission(
        ProtocolSubmissionInfoBean submissionInfoBean,
        ProtocolInfoBean protocolInfoBean)  throws CoeusException,DBException{
        Vector paramAssignment= new Vector();
        boolean success = false;
        Vector procedures = new Vector(5,3);
        coeusFunctions = new CoeusFunctions();
        /**** begin: updated to insert new record whenever submission details changes
         * change requested by geo with refID: 203  */  
        
        /* earlier we used to update the submission record with the new values
           whenever there is any change in submission details. now we are
           inserting a new row with new sequence number in protocol details table,
           protocol submission table and protocol reviewers table */

        /*seqNumber = submissionInfoBean.getSequenceNumber();
        if(protocolInfoBean != null){
            procedures.add(addUpdProtocolInfo(protocolInfoBean));
        }*/
        procedures.add(addUpdProtocolInfo(protocolInfoBean));

        if (submissionInfoBean.getAcType() != null){
            if  (submissionInfoBean.getAcType().equals("U")) {
                submissionInfoBean.setAcType("I");
            }
            if (submissionInfoBean.getAcType().equals("I")) {
                submissionInfoBean.setUpdateTimestamp(dbTimestamp);
            }
        }
        /****** end: updation for refID: 203  */
        
        //Added on 08 Oct, 2003 - Prasanna - Start
        //If Committee is not selected then Submission Status should be 102 - Pending
        //System.out.println("Committeed Id : "+submissionInfoBean.getCommitteeId());
        //Modified for case 2785 - Routing enhancement - start
        //Set the status to Pending(102), if the commitee is not selected and 
        //the submission status is not Routing In Progress(213)
        if(submissionInfoBean.getCommitteeId()==null && submissionInfoBean.getSubmissionStatusCode() != 213){
            //Added for case 2785 - Routing enhancement - end
            submissionInfoBean.setSubmissionStatusCode(102);
        }else{
            if(submissionInfoBean.getSubmissionStatusCode() ==102){
                submissionInfoBean.setSubmissionStatusCode(100);    
            }
        }
        //Added on 08 Oct, 2003 - Prasanna - End
        
        //Added get Max Submission Number if insert mode- start
        if(submissionInfoBean.getAcType().equalsIgnoreCase("I")){
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            submissionInfoBean.setSubmissionNumber(
                protocolSubmissionTxnBean.getMaxSubmissionNumber(submissionInfoBean.getProtocolNumber()));            
        }
        //Added get Max Submission Number if insert mode- end
        
        paramAssignment.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                        submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+seqNumber));
        paramAssignment.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getScheduleId()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_QUAL_CODE",
                    DBEngineConstants.TYPE_STRING,
                    submissionInfoBean.getSubmissionQualTypeCode() == 0 ? null :        
                    ""+submissionInfoBean.getSubmissionQualTypeCode()));
        paramAssignment.addElement(new Parameter("PROTOCOL_REVIEW_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getProtocolReviewTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_STATUS_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionStatusCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_DATE",
                    DBEngineConstants.TYPE_DATE,dbTimestamp));
        paramAssignment.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getComments()));
        paramAssignment.addElement(new Parameter("YES_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                                ""+submissionInfoBean.getYesVoteCount()));
        paramAssignment.addElement(new Parameter("NO_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getNoVoteCount()));
        paramAssignment.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramAssignment.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAWSequenceNumber()));
        paramAssignment.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getAWScheduleId()));
        paramAssignment.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            submissionInfoBean.getUpdateTimestamp()));
        paramAssignment.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getAcType()));
        
        //prps start - jul 15 2003
        paramAssignment.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+ (submissionInfoBean.getSubmissionNumber() + 1)));
        
        paramAssignment.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionNumber()));
                
        paramAssignment.addElement(new Parameter("ABSTAINER_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAbstainerCount()));
        
        paramAssignment.addElement(new Parameter("VOTING_COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getVotingComments())); 
        
        paramAssignment.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getCommitteeId())); 
        
        //prps end
        
        StringBuffer sqlAssignment = new StringBuffer(
                                            "call upd_proto_submission(");
        sqlAssignment.append(" <<PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_QUAL_CODE>> , ");
        sqlAssignment.append(" <<PROTOCOL_REVIEW_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_STATUS_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_DATE>> , ");
        sqlAssignment.append(" <<COMMENTS>> , ");
        sqlAssignment.append(" <<YES_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<NO_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<UPDATE_USER>> , ");
        sqlAssignment.append(" <<UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<ABSTAINER_COUNT>> , ");
        sqlAssignment.append(" <<VOTING_COMMENTS>> , ");
        sqlAssignment.append(" <<SUBMISSION_NUMBER>> , ");
        //prps end 
        
        //prps start - jul 21 2003
        sqlAssignment.append(" <<COMMITTEE_ID>> , ");
        //prps end 
        sqlAssignment.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<AW_UPDATE_USER>> , ");
        sqlAssignment.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<AW_SUBMISSION_NUMBER>> , ");
        //prps end
        sqlAssignment.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAssignment  = new ProcReqParameter();
        procAssignment.setDSN(DSN);
        procAssignment.setParameterInfo(paramAssignment);
        procAssignment.setSqlCommand(sqlAssignment.toString());
        
        if  (submissionInfoBean.getAcType() != null){
                procedures.add(procAssignment);
        }
        
        // inserting new protocol Reviewer list
        Vector reviewerList = submissionInfoBean.getProtocolReviewer();
        if ((reviewerList != null) && (reviewerList.size() > 0 )){
            int oaLength = reviewerList.size();
            for(int rowIndex=0; rowIndex<oaLength; rowIndex++){
                ProtocolReviewerInfoBean protocolReviewerInfoBean =
                (ProtocolReviewerInfoBean)reviewerList.elementAt(rowIndex);
                
                /**** begin: updated to insert new record whenever submission details changes
                 * change requested by geo with refID: 203  */  
                
                    //Commented following code as it is not required to Insert record for every update
                    /*if ( (protocolReviewerInfoBean.getAcType() == null)
                            || (protocolReviewerInfoBean.getAcType().equals("U")) ) {
                        protocolReviewerInfoBean.setAcType("I");
                    }*/
                if (protocolReviewerInfoBean.getAcType() != null){
                    if (protocolReviewerInfoBean.getAcType().equals("I")) {
                        protocolReviewerInfoBean.setUpdateTimestamp(dbTimestamp);
                        protocolReviewerInfoBean.setProtocolNumber(
                                     submissionInfoBean.getProtocolNumber());
                        protocolReviewerInfoBean.setSequenceNumber(seqNumber);
                        //protocolReviewerInfoBean.setScheduleId(
                        //                submissionInfoBean.getScheduleId());
                        protocolReviewerInfoBean.setSubmissionNumber(
                                   submissionInfoBean.getSubmissionNumber()+1);                        
                    }
                    /* ignore the deleted row and insert new row for the other reviewer rows
                       with new sequence number. as procedure returns only reviewers with 
                       latest sequence number we wont get the deleted reviewer details
                       while fetching */
                    if( !protocolReviewerInfoBean.getAcType().equals("D") ) {
                        procedures.add(addUpdProtocolReviewer(protocolReviewerInfoBean));
                    }
                }
                
/*                if  (protocolReviewerInfoBean.getAcType() != null){
                     if (protocolReviewerInfoBean.getAcType().equals(INSERT)) {
                            protocolReviewerInfoBean.setUpdateTimestamp(dbTimestamp);
                            protocolReviewerInfoBean.setProtocolNumber(
                                         submissionInfoBean.getProtocolNumber());
                            protocolReviewerInfoBean.setSequenceNumber(
                                        seqNumber);
                            protocolReviewerInfoBean.setScheduleId(
                                            submissionInfoBean.getScheduleId());
                    }
                    procedures.add(addUpdProtocolReviewer(protocolReviewerInfoBean));
                }*/
                
                /**** end of updation for refID: 203   */
            }
        }
        
        Vector expiditedCheckList = submissionInfoBean.getProtocolExpeditedCheckList();
        if(expiditedCheckList!=null){
            int length = expiditedCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)expiditedCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);
                    procedures.add(addUpdProtocolExpitedCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }
        
        Vector exemptCheckList = submissionInfoBean.getProtocolExemptCheckList();
        if(exemptCheckList!=null){
            int length = exemptCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)exemptCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);                    
                    procedures.add(addUpdProtocolExemptCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }        
        
        //Added to Update Review Comments - 23/10/2003 - start
        boolean hasMaxEntyNumber = false;
        int entryNumber = 0;
        Vector reviewComments = submissionInfoBean.getProtocolReviewComments();
        if(reviewComments!=null){
            int reviewCommentslength = reviewComments.size();
            ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean 
                = new ScheduleMaintenanceUpdateTxnBean(this.userId);
            for(int rowIndex=0; rowIndex < reviewCommentslength; rowIndex++){
                MinuteEntryInfoBean minuteEntryInfoBean =
                (MinuteEntryInfoBean)reviewComments.elementAt(rowIndex);
                if(minuteEntryInfoBean!=null){
                    minuteEntryInfoBean.setProtocolNumber(
                               submissionInfoBean.getProtocolNumber());
                    minuteEntryInfoBean.setSequenceNumber(
                               submissionInfoBean.getSequenceNumber());
                    minuteEntryInfoBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);                    
                    //Set Entry number - start
                    if(minuteEntryInfoBean.getAcType() != null && 
                        minuteEntryInfoBean.getAcType().equalsIgnoreCase("I") 
                            && hasMaxEntyNumber == false){
                        entryNumber = scheduleMaintenanceUpdateTxnBean.getMaxEntryNumber(minuteEntryInfoBean.getScheduleId());
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                        hasMaxEntyNumber = true;                
                    }else if(minuteEntryInfoBean.getAcType() != null && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                    }
                    //Set Entry number - end
                    procedures.add(
                        scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(
                            minuteEntryInfoBean, dbTimestamp));
                }
            }
        }
        //Added to Update Review Comments - 23/10/2003 - end
       
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              
               //prps start
            // Initial submission wries only to protocol table and skips to write to protocol action table. To fix this is the following code.
            // this code will make sure that there is an entry created in the protocol actions table when there is initial submission  
            //Added for Protocol Upload Documents Enhancement start 1
                protocolUploadStatusChanged(submissionInfoBean.getProtocolNumber(), seqNumber);
            //Added for Protocol Upload Documents Enhancement end 1
            int actionCode;
            //Check if Submission is for any Request actions and set corresponding ActionCode.
            if(submissionInfoBean.getSubmissionTypeCode()==108){ //Request to termination
                actionCode = 104;
            }else if(submissionInfoBean.getSubmissionTypeCode()==109){ //Request to Close
                actionCode = 105;
            }else if(submissionInfoBean.getSubmissionTypeCode()==110){ //Request for Suspension
                actionCode = 106;
            }else{
                actionCode =  101; //Normal Submission
            }
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
            
            int subNum = submissionInfoBean.getSubmissionNumber() + 1 ;
            if (actionTxn.logStatusChangeToProtocolAction(submissionInfoBean.getProtocolNumber(), seqNumber,
                                               new Integer(subNum) , userId) != -1)  
            {// status is submit to IRB  
                success = true ;
                
            }    
            else
            {
                success = false ;
            }    
            //prps end
            // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification 
            if(reviewerList != null ){
                sendMailsForReviewerChange(reviewerList);
            }
                  
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    //Added for Protocol Upload Documents Enhancement start 2
    /**
     *Method used to update protocol document upload status if protocol submit to IRB
     *@param protocol number
     *@param sequence number
     *@return boolean for setting status
     */
    public boolean protocolUploadStatusChanged(String protocolNumber, 
        int sequenceNumber ) throws CoeusException, DBException {
        ProtocolDataTxnBean protocolDataTxnBean
            = new ProtocolDataTxnBean(userId);
        Vector vecData = protocolDataTxnBean.getUploadDocumentForProtocol(protocolNumber);
        ProtocolUpdateTxnBean protocolUpdateTxnBean =
                                        new ProtocolUpdateTxnBean(userId);
        if(vecData != null){
            for(int index = 0; index < vecData.size(); index ++ ) {
                UploadDocumentBean uploadDocumentBean
                    = (UploadDocumentBean)vecData.get(index);
                //if doc type is draft.
                if(uploadDocumentBean.getStatusCode() == 1) {
                    //status set to submiited
                    uploadDocumentBean.setAcType("U");
                    uploadDocumentBean.setStatusCode(2);
                    uploadDocumentBean.setChangeStatus(true);
                    protocolUpdateTxnBean.addUpdProtocolUpload(uploadDocumentBean);
                }
            }
        }
        return true;
    }
    //Added for Protocol Upload Documents Enhancement end 2
    /**
     *  Method used to update/insert all the details of a Protocol Submission for all Request Actions.
     *  <li>To fetch the data, it uses upd_proto_submission procedure.
     *
     *  @param submissionInfoBean  this bean contains data for insert.
     *  @param protocolInfoBean this bean contains data for
     *  modifying the protocol details.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    //Changed on 06 Oct, 2003 - start
    /*public int addUpdProtocolSubmissionForRequest(
        ProtocolSubmissionInfoBean submissionInfoBean,
        ProtocolInfoBean protocolInfoBean)  throws CoeusException,DBException{
     */
    //Changed on 06 Oct, 2003 - end
    public int addUpdProtocolSubmissionForRequest(
        ProtocolSubmissionInfoBean submissionInfoBean)  throws CoeusException,DBException{            
        Vector paramAssignment= new Vector();
        boolean success = false;
        Vector procedures = new Vector(5,3);
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        /**** begin: updated to insert new record whenever submission details changes
         * change requested by geo with refID: 203  */  
        
        /* earlier we used to update the submission record with the new values
           whenever there is any change in submission details. now we are
           inserting a new row with new sequence number in protocol details table,
           protocol submission table and protocol reviewers table */

        /*seqNumber = submissionInfoBean.getSequenceNumber();
        if(protocolInfoBean != null){
            procedures.add(addUpdProtocolInfo(protocolInfoBean));
        }*/
        //procedures.add(addUpdProtocolInfo(protocolInfoBean));
        //Added on 06 Oct, 2003 - start    
        seqNumber = submissionInfoBean.getSequenceNumber();
        //Added on 06 Oct, 2003 - end
        if (submissionInfoBean.getAcType() != null){
            if  (submissionInfoBean.getAcType().equals("U")) {
                submissionInfoBean.setAcType("I");
            }
            if (submissionInfoBean.getAcType().equals("I")) {
                submissionInfoBean.setUpdateTimestamp(dbTimestamp);
            }
        }
        /****** end: updation for refID: 203  */
        
        //Added get Max Submission Number if insert mode- start
        if(submissionInfoBean.getAcType().equalsIgnoreCase("I")){
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            submissionInfoBean.setSubmissionNumber(
                protocolSubmissionTxnBean.getMaxSubmissionNumber(submissionInfoBean.getProtocolNumber()));            
        }
        //Added get Max Submission Number if insert mode- end
        
        paramAssignment.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                        submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+seqNumber));
        paramAssignment.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getScheduleId()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_QUAL_CODE",
                    DBEngineConstants.TYPE_STRING,
                    submissionInfoBean.getSubmissionQualTypeCode() == 0 ? null :        
                    ""+submissionInfoBean.getSubmissionQualTypeCode()));
        paramAssignment.addElement(new Parameter("PROTOCOL_REVIEW_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getProtocolReviewTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_STATUS_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionStatusCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_DATE",
                    DBEngineConstants.TYPE_DATE,dbTimestamp));
        paramAssignment.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getComments()));
        paramAssignment.addElement(new Parameter("YES_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                                ""+submissionInfoBean.getYesVoteCount()));
        paramAssignment.addElement(new Parameter("NO_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getNoVoteCount()));
        paramAssignment.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramAssignment.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAWSequenceNumber()));
        paramAssignment.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getAWScheduleId()));
        paramAssignment.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            submissionInfoBean.getUpdateTimestamp()));
        paramAssignment.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getAcType()));
        
        //prps start - jul 15 2003
        paramAssignment.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+ (submissionInfoBean.getSubmissionNumber() + 1)));
        
        paramAssignment.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionNumber()));
                
        paramAssignment.addElement(new Parameter("ABSTAINER_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAbstainerCount()));
        
        paramAssignment.addElement(new Parameter("VOTING_COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getVotingComments())); 
        
        paramAssignment.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getCommitteeId())); 
        
        //prps end
        
        StringBuffer sqlAssignment = new StringBuffer(
                                            "call upd_proto_submission(");
        sqlAssignment.append(" <<PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_QUAL_CODE>> , ");
        sqlAssignment.append(" <<PROTOCOL_REVIEW_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_STATUS_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_DATE>> , ");
        sqlAssignment.append(" <<COMMENTS>> , ");
        sqlAssignment.append(" <<YES_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<NO_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<UPDATE_USER>> , ");
        sqlAssignment.append(" <<UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<ABSTAINER_COUNT>> , ");
        sqlAssignment.append(" <<VOTING_COMMENTS>> , ");
        sqlAssignment.append(" <<SUBMISSION_NUMBER>> , ");
        //prps end 
        
        //prps start - jul 21 2003
        sqlAssignment.append(" <<COMMITTEE_ID>> , ");
        //prps end 
        sqlAssignment.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<AW_UPDATE_USER>> , ");
        sqlAssignment.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<AW_SUBMISSION_NUMBER>> , ");
        //prps end
        sqlAssignment.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAssignment  = new ProcReqParameter();
        procAssignment.setDSN(DSN);
        procAssignment.setParameterInfo(paramAssignment);
        procAssignment.setSqlCommand(sqlAssignment.toString());
        
        if  (submissionInfoBean.getAcType() != null){
                procedures.add(procAssignment);
        }
        
        // inserting new protocol Reviewer list
        Vector reviewerList = submissionInfoBean.getProtocolReviewer();
        if ((reviewerList != null) && (reviewerList.size() > 0 )){
            int oaLength = reviewerList.size();
            for(int rowIndex=0; rowIndex<oaLength; rowIndex++){
                ProtocolReviewerInfoBean protocolReviewerInfoBean =
                (ProtocolReviewerInfoBean)reviewerList.elementAt(rowIndex);
                
                /**** begin: updated to insert new record whenever submission details changes
                 * change requested by geo with refID: 203  */  
                
                
                    /*if ( (protocolReviewerInfoBean.getAcType() == null)
                            || (protocolReviewerInfoBean.getAcType().equals("U")) ) {
                        protocolReviewerInfoBean.setAcType("I");
                    }*/
                if (protocolReviewerInfoBean.getAcType() != null){
                    if (protocolReviewerInfoBean.getAcType().equals("I")) {
                        protocolReviewerInfoBean.setUpdateTimestamp(dbTimestamp);
                        protocolReviewerInfoBean.setProtocolNumber(
                                     submissionInfoBean.getProtocolNumber());
                        protocolReviewerInfoBean.setSequenceNumber(seqNumber);
                        //protocolReviewerInfoBean.setScheduleId(
                        //                submissionInfoBean.getScheduleId());
                        protocolReviewerInfoBean.setSubmissionNumber(
                                   submissionInfoBean.getSubmissionNumber()+1);                                                
                    }
                    /* ignore the deleted row and insert new row for the other reviewer rows
                       with new sequence number. as procedure returns only reviewers with 
                       latest sequence number we wont get the deleted reviewer details
                       while fetching */
                    if( !protocolReviewerInfoBean.getAcType().equals("D") ) {
                        procedures.add(addUpdProtocolReviewer(protocolReviewerInfoBean));
                    }
                }
                /**** end of updation for refID: 203   */
            }
        }
        
        Vector expiditedCheckList = submissionInfoBean.getProtocolExpeditedCheckList();
        if(expiditedCheckList!=null){
            int length = expiditedCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)expiditedCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);                    
                    procedures.add(addUpdProtocolExpitedCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }
        
        Vector exemptCheckList = submissionInfoBean.getProtocolExemptCheckList();
        if(exemptCheckList!=null){
            int length = exemptCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)exemptCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);                                        
                    procedures.add(addUpdProtocolExemptCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }   
        
        //Added to Update Review Comments - 23/10/2003 - start
        Vector reviewComments = submissionInfoBean.getProtocolReviewComments();
        boolean hasMaxEntyNumber = false;
        int entryNumber = 0;
        if(reviewComments!=null){
            int reviewCommentslength = reviewComments.size();
            ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean 
                = new ScheduleMaintenanceUpdateTxnBean(this.userId);
            for(int rowIndex=0; rowIndex < reviewCommentslength; rowIndex++){
                MinuteEntryInfoBean minuteEntryInfoBean =
                (MinuteEntryInfoBean)reviewComments.elementAt(rowIndex);
                if(minuteEntryInfoBean!=null){
                    minuteEntryInfoBean.setProtocolNumber(
                               submissionInfoBean.getProtocolNumber());
                    minuteEntryInfoBean.setSequenceNumber(
                               submissionInfoBean.getSequenceNumber());
                    minuteEntryInfoBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber()+1);                    
                    //Set Entry number - start
                    if(minuteEntryInfoBean.getAcType() != null && 
                        minuteEntryInfoBean.getAcType().equalsIgnoreCase("I") 
                            && hasMaxEntyNumber == false){
                        entryNumber = scheduleMaintenanceUpdateTxnBean.getMaxEntryNumber(minuteEntryInfoBean.getScheduleId());
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                        hasMaxEntyNumber = true;                
                    }else if(minuteEntryInfoBean.getAcType() != null && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                    }
                    //Set Entry number - end
                    procedures.add(
                        scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(
                            minuteEntryInfoBean, dbTimestamp));
                }
            }
        }
        //Added to Update Review Comments - 23/10/2003 - end
        
        int intActionId;
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              
               //prps start
            // Initial submission wries only to protocol table and skips to write to protocol action table. To fix this is the following code.
            // this code will make sure that there is an entry created in the protocol actions table when there is initial submission  
            
            int actionCode;
            //Check if Submission is for any Request actions and set corresponding ActionCode.
            if(submissionInfoBean.getSubmissionTypeCode()==108){ //Request to termination
                actionCode = 104;
            }else if(submissionInfoBean.getSubmissionTypeCode()==109){ //Request to Close
                actionCode = 105;
            }else if(submissionInfoBean.getSubmissionTypeCode()==110){ //Request for Suspension
                actionCode = 106;
            }else if(submissionInfoBean.getSubmissionTypeCode()==112){ //Request to Close Enrollment
                actionCode = 108;
            }else{
                actionCode =  101; //Normal Submission
            }
            ActionTransaction actionTxn = new ActionTransaction(actionCode) ;
            
            int subNum = submissionInfoBean.getSubmissionNumber() + 1 ;
            intActionId = actionTxn.logStatusChangeToProtocolAction(submissionInfoBean.getProtocolNumber(), seqNumber,
                                               new Integer(subNum) , userId);
            if (intActionId != -1)  
            {// status is submit to IRB  
                success = true ;
            }    
            else
            {
                success = false ;
            }    
            //prps end
                  
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return intActionId;
    }    
    
    /**
     * Method used to update Protocol Details with status as Submitted to IRB
     *
     * @param protocolInfoBean this bean contains data for
     * modifying the protocol details.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     * this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    private ProcReqParameter addUpdProtocolInfo( 
        ProtocolInfoBean protocolInfoBean) throws DBException{
        Vector paramInfo= new Vector();
        Vector procedures = new Vector(5,3);
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();

        String protocolNumber;
        boolean success = false;
        //seqNumber = protocolInfoBean.getSequenceNumber()+1;
        seqNumber = protocolInfoBean.getSequenceNumber();
        protocolNumber  = protocolInfoBean.getProtocolNumber();
        if (protocolInfoBean.getAcType() != null){
            /*if  (protocolInfoBean.getAcType().equals("U")) {
                protocolInfoBean.setAcType("I");
            }*/
            if (protocolInfoBean.getAcType().equals("I")) {
                protocolInfoBean.setCreateTimestamp(dbTimestamp);
                protocolInfoBean.setUpdateTimestamp(dbTimestamp);
            }
        }
        paramInfo.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
        paramInfo.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+seqNumber));
        paramInfo.addElement(new Parameter("PROTOCOL_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolTypeCode()));
        paramInfo.addElement(new Parameter("PROTOCOL_STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getProtocolStatusCode()));
        paramInfo.addElement(new Parameter("TITLE",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getTitle()));
        paramInfo.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getDescription()));        
        paramInfo.addElement(new Parameter("SPECIAL_REVIEW_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getSpecialReviewIndicator()));
        paramInfo.addElement(new Parameter("VUL_SUBJECT_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                protocolInfoBean.getVulnerableSubjectIndicator()));
        
        paramInfo.addElement(new Parameter("APPLICATION_DATE",
                DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApplicationDate()));
        paramInfo.addElement(new Parameter("APPROVAL_DATE",
                DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getApprovalDate()));
        paramInfo.addElement(new Parameter("EXPIRATION_DATE",
                DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getExpirationDate()));
        paramInfo.addElement(new Parameter("FDA_APPLICATION_NUMBER",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFDAApplicationNumber()));
        paramInfo.addElement(new Parameter("IS_BILLABLE",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.isBillableFlag()? "Y": "N") );
        paramInfo.addElement(new Parameter("REFERENCE_NUMBER_1",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_1()));
        paramInfo.addElement(new Parameter("REFERENCE_NUMBER_2",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getRefNum_2())); 
        paramInfo.addElement(new Parameter("KEY_STUDY_PERSON_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getKeyStudyIndicator()));
        paramInfo.addElement(new Parameter("FUNDING_SOURCE_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getFundingSourceIndicator()));
        paramInfo.addElement(new Parameter("CORRESPONDENT_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getCorrespondenceIndicator()));
         paramInfo.addElement(new Parameter("REFERENCE_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getReferenceIndicator()));        
         System.out.println("Project Indicator : "+protocolInfoBean.getProjectsIndicator());
         paramInfo.addElement(new Parameter("RELATED_PROJECTS_INDICATOR",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProjectsIndicator()));                 
        paramInfo.addElement(new Parameter("CREATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
                    paramInfo.addElement(new Parameter("CREATE_TIMESTAMP",
        DBEngineConstants.TYPE_TIMESTAMP,
                protocolInfoBean.getCreateTimestamp()));
                    paramInfo.addElement(new Parameter("UPDATE_USER",
        DBEngineConstants.TYPE_STRING, userId));
                paramInfo.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                    dbTimestamp));
        // Added for case # 3090 - start
            paramInfo.addElement(new Parameter("LAST_APPROVAL_DATE",
                    DBEngineConstants.TYPE_DATE,
                    protocolInfoBean.getLastApprovalDate()));
       // Added for case # 3090 - end                
        paramInfo.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getProtocolNumber()));
        paramInfo.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                    ""+protocolInfoBean.getSequenceNumber()));
        paramInfo.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        paramInfo.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                    protocolInfoBean.getUpdateTimestamp()));
        paramInfo.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                    protocolInfoBean.getAcType()));

        StringBuffer sqlInfo = new StringBuffer(
        "call upd_protocol(");
        sqlInfo.append(" <<PROTOCOL_NUMBER>> , ");
        sqlInfo.append(" <<SEQUENCE_NUMBER>> , ");
        sqlInfo.append(" <<PROTOCOL_TYPE_CODE>> , ");
        sqlInfo.append(" <<PROTOCOL_STATUS_CODE>> , ");
        sqlInfo.append(" <<TITLE>> , ");
        sqlInfo.append(" <<DESCRIPTION>> , ");
        sqlInfo.append(" <<SPECIAL_REVIEW_INDICATOR>> , ");
        sqlInfo.append(" <<VUL_SUBJECT_INDICATOR>> , ");
        sqlInfo.append(" <<APPLICATION_DATE>> , ");
        sqlInfo.append(" <<APPROVAL_DATE>> , ");
        sqlInfo.append(" <<EXPIRATION_DATE>> , ");
        sqlInfo.append(" <<FDA_APPLICATION_NUMBER>> , ");
        sqlInfo.append(" <<IS_BILLABLE>> , ");
        sqlInfo.append(" <<REFERENCE_NUMBER_1>> , ");
        sqlInfo.append(" <<REFERENCE_NUMBER_2>> , ");
        sqlInfo.append(" <<KEY_STUDY_PERSON_INDICATOR>> , ");
        sqlInfo.append(" <<FUNDING_SOURCE_INDICATOR>> , ");     
        sqlInfo.append(" <<CORRESPONDENT_INDICATOR>> , ");     
        sqlInfo.append(" <<REFERENCE_INDICATOR>> , ");        
        sqlInfo.append(" <<RELATED_PROJECTS_INDICATOR>> , ");                
        sqlInfo.append(" <<CREATE_USER>> , ");
        sqlInfo.append(" <<CREATE_TIMESTAMP>> , ");
        sqlInfo.append(" <<UPDATE_USER>> , ");
        sqlInfo.append(" <<UPDATE_TIMESTAMP>> , ");
        // Added for case # 3090 - start
        sqlInfo.append(" <<LAST_APPROVAL_DATE>> , ");
        // Added for case # 3090 - end
        sqlInfo.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlInfo.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlInfo.append(" <<AW_UPDATE_USER>> , ");
        sqlInfo.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlInfo.append(" <<AC_TYPE>> )");

        ProcReqParameter procInfo  = new ProcReqParameter();
        procInfo.setDSN(DSN);
        procInfo.setParameterInfo(paramInfo);
        procInfo.setSqlCommand(sqlInfo.toString());
        
        return procInfo;
    }
    
    /**
     *  Method used to update Yes_Vote and No_Vote of a Protocol Submission.
     *  <li>To fetch the data, it uses UPD_PROTO_SUBMISSION_VOTE procedure.
     *
     *  @param ProtocolSubmissionVoteFormBean this bean contains data for Update.
     *  @return boolean true for successful modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
     public boolean addUpdProtocolVoteSubmission(ProtocolSubmissionVoteFormBean 
            submissionInfoBean, Vector reviewComments)  throws CoeusException,DBException{
        Vector paramAssignment= new Vector();
        boolean success = false;
        Vector procedures = new Vector(5,3);
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        
        if (submissionInfoBean.getAcType() != null){
            if  (submissionInfoBean.getAcType().equals("I")) {
                submissionInfoBean.setAcType("U");
            }
        }
        
        paramAssignment.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                        submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSequenceNumber()));
        paramAssignment.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getScheduleId()));
        paramAssignment.addElement(new Parameter("YES_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                                ""+submissionInfoBean.getYesVoteCount()));
        paramAssignment.addElement(new Parameter("NO_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getNoVoteCount()));
        paramAssignment.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramAssignment.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSequenceNumber()));
        paramAssignment.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getScheduleId()));
        paramAssignment.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            submissionInfoBean.getUpdateTimestamp()));
        paramAssignment.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getAcType()));
        //prps start - jul 10 2003
        paramAssignment.addElement(new Parameter("ABSTAINER_COUNT",
                    DBEngineConstants.TYPE_INT,
                            "" + submissionInfoBean.getAbstainerCount()));
        
        paramAssignment.addElement(new Parameter("VOTING_COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getVotingComments()));
        
        paramAssignment.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            "" + submissionInfoBean.getSubmissionNumber()));
        
        paramAssignment.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            "" + submissionInfoBean.getSubmissionNumber()));
        
        //prps end
        StringBuffer sqlAssignment = new StringBuffer(
                                            "call UPD_PROTO_SUBMISSION_VOTE(");
        sqlAssignment.append(" <<PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<YES_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<NO_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<UPDATE_USER>> , ");
        sqlAssignment.append(" <<UPDATE_TIMESTAMP>> , ");
        //prps start jul 15 2003
        sqlAssignment.append(" <<ABSTAINER_COUNT>> , ");
        sqlAssignment.append(" <<VOTING_COMMENTS>> ,");
        sqlAssignment.append(" << SUBMISSION_NUMBER >> ,") ;
        //prps end
        
        sqlAssignment.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<AW_UPDATE_USER>> , ");
        sqlAssignment.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        
        //prps start - jul 15 2003
        sqlAssignment.append(" << AW_SUBMISSION_NUMBER >> ,") ;
        //prps end
        
        sqlAssignment.append(" <<AC_TYPE>>  )");
        
        
        ProcReqParameter procAssignment  = new ProcReqParameter();
        procAssignment.setDSN(DSN);
        procAssignment.setParameterInfo(paramAssignment);
        procAssignment.setSqlCommand(sqlAssignment.toString());
        
        if  (submissionInfoBean.getAcType() != null){
                procedures.add(procAssignment);
        }
         
        Vector reviewerList = submissionInfoBean.getProtocolVoteAbstainee();
        if ((reviewerList != null) && (reviewerList.size() > 0 )){
            int oaLength = reviewerList.size();
            for(int rowIndex=0; rowIndex<oaLength; rowIndex++){
                ProtocolVoteAbsFormBean protocolVoteAbsFormBean =
                (ProtocolVoteAbsFormBean)reviewerList.elementAt(rowIndex);
                procedures.add(addDelProtocolVoteAbstainees(protocolVoteAbsFormBean)); 
            }
        }
        
        //Added for Review Comments in Voting Form - start
        if(reviewComments!=null && reviewComments.size() > 0){
            boolean hasMaxEntyNumber = false;
            int entryNumber = 0;
            ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean 
                = new ScheduleMaintenanceUpdateTxnBean(this.userId);
            for(int row = 0; row < reviewComments.size(); row++){
                MinuteEntryInfoBean minuteEntryInfoBean =
                (MinuteEntryInfoBean)reviewComments.elementAt(row);
                if(minuteEntryInfoBean!=null && minuteEntryInfoBean.getAcType() != null){
                    minuteEntryInfoBean.setProtocolNumber(
                               submissionInfoBean.getProtocolNumber());
                    minuteEntryInfoBean.setSequenceNumber(
                               submissionInfoBean.getSequenceNumber());
                    minuteEntryInfoBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber());
                    //Set Entry number - start
                    if(minuteEntryInfoBean.getAcType() != null && 
                        minuteEntryInfoBean.getAcType().equalsIgnoreCase("I") 
                            && hasMaxEntyNumber == false){
                        entryNumber = scheduleMaintenanceUpdateTxnBean.getMaxEntryNumber(minuteEntryInfoBean.getScheduleId());
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                        hasMaxEntyNumber = true;                
                    }else if(minuteEntryInfoBean.getAcType() != null && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                    }
                    //Set Entry number - end
                    procedures.add(
                        scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(
                            minuteEntryInfoBean, dbTimestamp));
                }                
            }
        }
        //Added for Review Comments in Voting Form - end  
        
        if(dbEngine!=null){ 
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
     }
     
    /**  Method used to update/insert all the details of a Protocol Vote Abstainees.
     *  <li>To fetch the data, it uses UPD_PROTO_VOTE_ABSTAINEES procedure.
     *
     * @param ProtocolVoteAbsFormBean which consists of all the details 
     * regarding vote abstainees for a protocol.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
     public ProcReqParameter addDelProtocolVoteAbstainees( ProtocolVoteAbsFormBean
                                  protocolVoteAbsFormBean)  throws DBException{
        Vector paramVote= new Vector();
        coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
    
        paramVote.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getProtocolNumber()));
        paramVote.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolVoteAbsFormBean.getSequenceNumber()));
        paramVote.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getScheduleId()));
        paramVote.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getPersonId()));
        paramVote.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getComments()));
        paramVote.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                    DBEngineConstants.TYPE_STRING,
                        protocolVoteAbsFormBean.getNonEmployeeFlag()? "Y": "N") );
        paramVote.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramVote.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         paramVote.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getProtocolNumber()));
        paramVote.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolVoteAbsFormBean.getSequenceNumber()));
        paramVote.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getPersonId()));
        paramVote.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramVote.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            protocolVoteAbsFormBean.getUpdateTimestamp()));
        paramVote.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            protocolVoteAbsFormBean.getAcType()));
        
        StringBuffer sqlVote = new StringBuffer(
                                        "call UPD_PROTO_VOTE_ABSTAINEES(");
        sqlVote.append(" <<PROTOCOL_NUMBER>> , ");
        sqlVote.append(" <<SEQUENCE_NUMBER>> , ");
        sqlVote.append(" <<SCHEDULE_ID>> , ");
        sqlVote.append(" <<PERSON_ID>> , ");
        sqlVote.append(" <<COMMENTS>> , ");
        sqlVote.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlVote.append(" <<UPDATE_USER>> , ");
        sqlVote.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlVote.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlVote.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlVote.append(" <<AW_PERSON_ID>> , ");
        sqlVote.append(" <<AW_UPDATE_USER>> , ");
        sqlVote.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlVote.append(" <<AC_TYPE>> )");

        ProcReqParameter procVote  = new ProcReqParameter();
        procVote.setDSN(DSN);
        procVote.setParameterInfo(paramVote);
        procVote.setSqlCommand(sqlVote.toString());
        
        return procVote;
        
    }
     
    /**  Method used to update/insert all the details of a Protocol Reviewer.
     *  <li>To fetch the data, it uses upd_proto_reviewer procedure.
     *
     * @param protocolReviewerInfoBean which consists of all the details 
     * regarding reviewers for a protocol.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolReviewer( ProtocolReviewerInfoBean
                                  protocolReviewerInfoBean)  throws DBException{
        Vector paramReviewer= new Vector();
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
    
        paramReviewer.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getSequenceNumber()));
        /*paramReviewer.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getScheduleId()));*/
        paramReviewer.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getSubmissionNumber()));
        paramReviewer.addElement(new Parameter("PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getPersonId()));
        paramReviewer.addElement(new Parameter("NON_EMPLOYEE_FLAG",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.isNonEmployee() == true ? "Y" : "N"));        
        paramReviewer.addElement(new Parameter("REVIEWER_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getReviewerTypeCode()));
        // 3282: Reviewer View of Protocol materials - Start
        paramReviewer.addElement(new Parameter("ASSIGNED_DATE",
                DBEngineConstants.TYPE_DATE,protocolReviewerInfoBean.getAssignedDate()));
        paramReviewer.addElement(new Parameter("DUE_DATE",
                DBEngineConstants.TYPE_DATE,protocolReviewerInfoBean.getDueDate()));
        paramReviewer.addElement(new Parameter("REVIEW_COMPLETE",
                DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.isReviewComplete() ? "Y" : "N"));
        paramReviewer.addElement(new Parameter("RECOMMENDED_ACTION",
                DBEngineConstants.TYPE_STRING,protocolReviewerInfoBean.getRecommendedActionCode()));
        // 3282: Reviewer View of Protocol materials - End
        paramReviewer.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramReviewer.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         paramReviewer.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getAWSequenceNumber()));
        /*paramReviewer.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getScheduleId()));*/
        paramReviewer.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getSubmissionNumber()));
        paramReviewer.addElement(new Parameter("AW_PERSON_ID",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getAWPersonId()));
        paramReviewer.addElement(new Parameter("AW_NON_EMPLOYEE_FLAG",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.isAwNonEmployee() == true ? "Y" : "N"));
        paramReviewer.addElement(new Parameter("AW_REVIEWER_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewerInfoBean.getAWReviewerTypeCode()));
        paramReviewer.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramReviewer.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            protocolReviewerInfoBean.getUpdateTimestamp()));
        paramReviewer.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewerInfoBean.getAcType()));
        
        StringBuffer sqlReviewer = new StringBuffer(
                                        "call upd_proto_reviewer(");
        sqlReviewer.append(" <<PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<SEQUENCE_NUMBER>> , ");
        //sqlReviewer.append(" <<SCHEDULE_ID>> , ");
        sqlReviewer.append(" <<SUBMISSION_NUMBER>> , ");
        sqlReviewer.append(" <<PERSON_ID>> , ");
        sqlReviewer.append(" <<NON_EMPLOYEE_FLAG>> , ");
        sqlReviewer.append(" <<REVIEWER_TYPE_CODE>> , ");
        // 3282: Reviewer View of Protocol materials - Start
        sqlReviewer.append(" <<ASSIGNED_DATE>> , ");
        sqlReviewer.append(" <<DUE_DATE>> , ");
        sqlReviewer.append(" <<REVIEW_COMPLETE>> , ");
        sqlReviewer.append(" <<RECOMMENDED_ACTION>> , ");
        // 3282: Reviewer View of Protocol materials - End
        sqlReviewer.append(" <<UPDATE_USER>> , ");
        sqlReviewer.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<AW_SEQUENCE_NUMBER>> , ");
        //sqlReviewer.append(" <<AW_SCHEDULE_ID>> , ");
        sqlReviewer.append(" <<AW_SUBMISSION_NUMBER>> , ");
        sqlReviewer.append(" <<AW_PERSON_ID>> , ");
        sqlReviewer.append(" <<AW_NON_EMPLOYEE_FLAG>> , ");
        sqlReviewer.append(" <<AW_REVIEWER_TYPE_CODE>> , ");
        sqlReviewer.append(" <<AW_UPDATE_USER>> , ");
        sqlReviewer.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AC_TYPE>> )");

        ProcReqParameter procReviewer  = new ProcReqParameter();
        procReviewer.setDSN(DSN);
        procReviewer.setParameterInfo(paramReviewer);
        procReviewer.setSqlCommand(sqlReviewer.toString());
        
        return procReviewer;
        
    }

    
    //prps start jul 28 2003
    // this updating to protocol submission table is from submissionDetailsForm
    // this just updates the submission details(there is no resubmission) 
    
    /**
     *  Method used to update all the details of a Protocol Submission.
     *
     *  @param submissionInfoBean  this bean contains data for update.
     *  @return boolean true for successful insert/modify
     *  @exception DBException if the instance of a dbEngine is null.
     *  @exception CoeusException if the DB instance is not available.
     */
    public boolean updProtocolSubmission(
        ProtocolSubmissionInfoBean submissionInfoBean)  throws CoeusException,DBException{
        Vector paramAssignment= new Vector();
        boolean success = false;
        Vector procedures = new Vector(5,3);
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        coeusFunctions = new CoeusFunctions();
        System.out.println("\n*** Current Time - " + dbTimestamp + "***\n") ; 
        
//        if (submissionInfoBean.getAcType() != null){
//            if  (submissionInfoBean.getAcType().equals("U")) {
//                
//            }
//            if (submissionInfoBean.getAcType().equals("U")) {
//                submissionInfoBean.setUpdateTimestamp(dbTimestamp);
//            }
//        }
        
        //Added on 08 Oct, 2003 - Prasanna - Start
        //If Committee is not selected then Submission Status should be 102 - Pending
        //System.out.println("Committeed Id : "+submissionInfoBean.getCommitteeId());
        if(submissionInfoBean.getCommitteeId()==null){
            submissionInfoBean.setSubmissionStatusCode(102);
        }else{
            //If committee is selected and Submission status is 102 - Pending
            //then change the Submission Status to 100 - Submitted to Committee
            if(submissionInfoBean.getSubmissionStatusCode() == 102){
                submissionInfoBean.setSubmissionStatusCode(100);
            }
        }
        //Added on 08 Oct, 2003 - Prasanna - End
        
        submissionInfoBean.setAcType("U");
        
        paramAssignment.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                        submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSequenceNumber()));
        paramAssignment.addElement(new Parameter("SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getScheduleId()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_TYPE_QUAL_CODE",
                    DBEngineConstants.TYPE_STRING,
                    submissionInfoBean.getSubmissionQualTypeCode() == 0 ? null :        
                    ""+submissionInfoBean.getSubmissionQualTypeCode()));
        paramAssignment.addElement(new Parameter("PROTOCOL_REVIEW_TYPE_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getProtocolReviewTypeCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_STATUS_CODE",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionStatusCode()));
        paramAssignment.addElement(new Parameter("SUBMISSION_DATE",
                    DBEngineConstants.TYPE_DATE,submissionInfoBean.getSubmissionDate())); 
        paramAssignment.addElement(new Parameter("COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getComments()));
        paramAssignment.addElement(new Parameter("YES_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                                ""+submissionInfoBean.getYesVoteCount()));
        paramAssignment.addElement(new Parameter("NO_VOTE_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getNoVoteCount()));
        paramAssignment.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        paramAssignment.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getProtocolNumber()));
        paramAssignment.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAWSequenceNumber()));
        paramAssignment.addElement(new Parameter("AW_SCHEDULE_ID",
                    DBEngineConstants.TYPE_STRING,
                                submissionInfoBean.getAWScheduleId()));
        paramAssignment.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramAssignment.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            submissionInfoBean.getUpdateTimestamp()));
        paramAssignment.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            submissionInfoBean.getAcType()));
        
        //prps start - jul 15 2003
        paramAssignment.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+ submissionInfoBean.getSubmissionNumber() ));
        
        paramAssignment.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getSubmissionNumber()));
                
        paramAssignment.addElement(new Parameter("ABSTAINER_COUNT",
                    DBEngineConstants.TYPE_INT,
                            ""+submissionInfoBean.getAbstainerCount()));
        
        paramAssignment.addElement(new Parameter("VOTING_COMMENTS",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getVotingComments())); 
        
        paramAssignment.addElement(new Parameter("COMMITTEE_ID",
                    DBEngineConstants.TYPE_STRING,
                                    submissionInfoBean.getCommitteeId())); 
        //prps end
        
        StringBuffer sqlAssignment = new StringBuffer(
                                            "call upd_proto_submission(");
        sqlAssignment.append(" <<PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_QUAL_CODE>> , ");
        sqlAssignment.append(" <<PROTOCOL_REVIEW_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_STATUS_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_DATE>> , ");
        sqlAssignment.append(" <<COMMENTS>> , ");
        sqlAssignment.append(" <<YES_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<NO_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<UPDATE_USER>> , ");
        sqlAssignment.append(" <<UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<ABSTAINER_COUNT>> , ");
        sqlAssignment.append(" <<VOTING_COMMENTS>> , ");
        sqlAssignment.append(" <<SUBMISSION_NUMBER>> , ");
        //prps end 
        
        //prps start - jul 21 2003
        sqlAssignment.append(" <<COMMITTEE_ID>> , ");
        //prps end 
        sqlAssignment.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<AW_UPDATE_USER>> , ");
        sqlAssignment.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        //prps start - jul 15 2003
        sqlAssignment.append(" <<AW_SUBMISSION_NUMBER>> , ");
        //prps end
        sqlAssignment.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAssignment  = new ProcReqParameter();
        procAssignment.setDSN(DSN);
        procAssignment.setParameterInfo(paramAssignment);
        procAssignment.setSqlCommand(sqlAssignment.toString());
        
        if  (submissionInfoBean.getAcType() != null){
                procedures.add(procAssignment);
        }
        
        // Commented for COEUSQA-3336 - Issues in reviewer notification - Start
        // inserting new protocol Reviewer list
//        Vector reviewerList = submissionInfoBean.getProtocolReviewer();
//        if ((reviewerList != null) && (reviewerList.size() > 0 )){
//            int oaLength = reviewerList.size();
//            for(int rowIndex=0; rowIndex<oaLength; rowIndex++){
//                ProtocolReviewerInfoBean protocolReviewerInfoBean =
//                (ProtocolReviewerInfoBean)reviewerList.elementAt(rowIndex);
//                
//                /**** begin: updated to insert new record whenever submission details changes
//                 * change requested by geo with refID: 203  */  
//                
//                
//                    if ( (protocolReviewerInfoBean.getAcType() == null)
//                            || (protocolReviewerInfoBean.getAcType().equals("U")) ) {
//                        protocolReviewerInfoBean.setAcType("U");
//                         protocolReviewerInfoBean.setProtocolNumber(
//                                     submissionInfoBean.getProtocolNumber());
//                        protocolReviewerInfoBean.setSequenceNumber(submissionInfoBean.getSequenceNumber());
//                        //protocolReviewerInfoBean.setScheduleId(
//                        //                submissionInfoBean.getScheduleId());
//                        protocolReviewerInfoBean.setSubmissionNumber(
//                                   submissionInfoBean.getSubmissionNumber());                                                                       
//                    }
//                    if (protocolReviewerInfoBean.getAcType().equals("I")) {
//                        protocolReviewerInfoBean.setUpdateTimestamp(dbTimestamp);
//                        protocolReviewerInfoBean.setProtocolNumber(
//                                     submissionInfoBean.getProtocolNumber());
//                        protocolReviewerInfoBean.setSequenceNumber(submissionInfoBean.getSequenceNumber());
//                        //protocolReviewerInfoBean.setScheduleId(
//                        //               submissionInfoBean.getScheduleId());
//                        protocolReviewerInfoBean.setSubmissionNumber(
//                                   submissionInfoBean.getSubmissionNumber());                                                
//                    }
//                    /* ignore the deleted row and insert new row for the other reviewer rows
//                       with new sequence number. as procedure returns only reviewers with 
//                       latest sequence number we wont get the deleted reviewer details
//                       while fetching */
//                       //prps 31 jul 2003 - now that sequence number is not used, delete the record
//                    //if( !protocolReviewerInfoBean.getAcType().equals("D") ) {
//                        procedures.add(addUpdProtocolReviewer(protocolReviewerInfoBean));
//                    //}
//                
//            }
            // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification
//            sendMailsForReviewerChange(reviewerList);
//        }
//            // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification
//            
//        }
        // Commented for COEUSQA-3336 - Issues in reviewer notification - End
        
        Vector expiditedCheckList = submissionInfoBean.getProtocolExpeditedCheckList();
        if(expiditedCheckList!=null){
            int length = expiditedCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)expiditedCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber());   
                    protocolReviewTypeCheckListBean.setSequenceNumber(
                                submissionInfoBean.getSequenceNumber());
                    procedures.add(addUpdProtocolExpitedCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }
        
        Vector exemptCheckList = submissionInfoBean.getProtocolExemptCheckList();
        if(exemptCheckList!=null){
            int length = exemptCheckList.size();
            for(int rowIndex=0; rowIndex<length; rowIndex++){
                ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean =
                (ProtocolReviewTypeCheckListBean)exemptCheckList.elementAt(rowIndex);
                if(protocolReviewTypeCheckListBean!=null){
                    protocolReviewTypeCheckListBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber());                                        
                    protocolReviewTypeCheckListBean.setSequenceNumber(
                                submissionInfoBean.getSequenceNumber());                    
                    procedures.add(addUpdProtocolExemptCheckList(protocolReviewTypeCheckListBean));
                }
            }
        }                
       
        //Added to Update Review Comments - 23/10/2003 - start
        Vector reviewComments = submissionInfoBean.getProtocolReviewComments();
        if(reviewComments!=null){
            int reviewCommentslength = reviewComments.size();
            ScheduleMaintenanceUpdateTxnBean scheduleMaintenanceUpdateTxnBean 
                = new ScheduleMaintenanceUpdateTxnBean(this.userId);
            int entryNumber = 0;
            boolean hasMaxEntyNumber = false;
                        
            for(int rowIndex=0; rowIndex < reviewCommentslength; rowIndex++){
                MinuteEntryInfoBean minuteEntryInfoBean =
                (MinuteEntryInfoBean)reviewComments.elementAt(rowIndex);
                if(minuteEntryInfoBean!=null){
                    minuteEntryInfoBean.setProtocolNumber(
                               submissionInfoBean.getProtocolNumber());
                    
                    //prps start - jan 09 2003
                    if (minuteEntryInfoBean.getScheduleId() == null
                    || minuteEntryInfoBean.getScheduleId().trim().length() <=0)
                    {
                        minuteEntryInfoBean.setScheduleId("9999999999") ;
                    }
                    //prps end - jan 09 2003
                    
                    minuteEntryInfoBean.setSequenceNumber(
                               submissionInfoBean.getSequenceNumber());
                    minuteEntryInfoBean.setSubmissionNumber(
                               submissionInfoBean.getSubmissionNumber());                    
                    //Set Entry number - start
                    if(minuteEntryInfoBean.getAcType() != null 
                        && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I") 
                            && hasMaxEntyNumber == false){
                        entryNumber = scheduleMaintenanceUpdateTxnBean.getMaxEntryNumber(minuteEntryInfoBean.getScheduleId());
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                        hasMaxEntyNumber = true;                
                    }else if(minuteEntryInfoBean.getAcType() != null && minuteEntryInfoBean.getAcType().equalsIgnoreCase("I")){
                        entryNumber = entryNumber + 1;
                        minuteEntryInfoBean.setEntryNumber(entryNumber);
                    }
                    //Set Entry number - end
                    procedures.add(
                        scheduleMaintenanceUpdateTxnBean.addUpdReviewComments(
                            minuteEntryInfoBean, dbTimestamp));
                }
            }
        }
        //Added to Update Review Comments - 23/10/2003 - end
        
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              // Added for COEUSQA-3336 - Issues in reviewer notification - Start
              // Reviewer details and notification will be send after submission details gets saved
              updateSendMailToReviewer(submissionInfoBean);
              // Added for COEUSQA-3336 - Issues in reviewer notification - End
              
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    
    //prps end 
    
    /**  Method used to update/insert all the details of a Protocol Expidited Check List.
     *  <li>To fetch the data, it uses UPD_PROTO_EXPIDITED_CHKLST procedure.
     *
     * @param protocolReviewTypeCheckListBean which consists of all the details 
     * regarding expidited check list.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolExpitedCheckList( ProtocolReviewTypeCheckListBean
                                  protocolReviewTypeCheckListBean)  throws DBException, CoeusException{
        //boolean success = true;             
        //Vector procedures = new Vector(5,3);
        Vector paramReviewer= new Vector();
        //coeusFunctions = new CoeusFunctions();
        //Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
    
        paramReviewer.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSequenceNumber()));
        paramReviewer.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSubmissionNumber()));        
        paramReviewer.addElement(new Parameter("EXPEDITED_CHKLST_CODE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getCheckListCode()));
        paramReviewer.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramReviewer.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         paramReviewer.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSequenceNumber()));
        paramReviewer.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSubmissionNumber()));                
        paramReviewer.addElement(new Parameter("AW_EXPEDITED_CHKLST_CODE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getCheckListCode()));
        paramReviewer.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, 
                            protocolReviewTypeCheckListBean.getUpdateUser()));
        paramReviewer.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            protocolReviewTypeCheckListBean.getUpdateTimestamp()));
        paramReviewer.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getAcType()));
        
        StringBuffer sqlReviewer = new StringBuffer(
                                        "call UPD_PROTO_EXPIDITED_CHKLST(");
        sqlReviewer.append(" <<PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<SEQUENCE_NUMBER>> , ");
        sqlReviewer.append(" <<SUBMISSION_NUMBER>> , ");
        sqlReviewer.append(" <<EXPEDITED_CHKLST_CODE>> , ");
        sqlReviewer.append(" <<UPDATE_USER>> , ");
        sqlReviewer.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlReviewer.append(" <<AW_SUBMISSION_NUMBER>> , ");
        sqlReviewer.append(" <<AW_EXPEDITED_CHKLST_CODE>> , ");
        sqlReviewer.append(" <<AW_UPDATE_USER>> , ");
        sqlReviewer.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AC_TYPE>> )");

        ProcReqParameter procReviewer  = new ProcReqParameter();
        procReviewer.setDSN(DSN);
        procReviewer.setParameterInfo(paramReviewer);
        procReviewer.setSqlCommand(sqlReviewer.toString());
        /*procedures.add(procReviewer);
        
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
        
        return procReviewer;
    }    
    
    /**  Method used to update/insert all the details of a Protocol Expidited Check List.
     *  <li>To fetch the data, it uses UPD_PROTO_EXEMPT_CHKLST procedure.
     *
     * @param protocolReviewTypeCheckListBean which consists of all the details 
     * regarding expidited check list.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public ProcReqParameter addUpdProtocolExemptCheckList( ProtocolReviewTypeCheckListBean
                                  protocolReviewTypeCheckListBean)  throws DBException, CoeusException{
        //boolean success = true;             
        //Vector procedures = new Vector(5,3);
        Vector paramReviewer= new Vector();
        //coeusFunctions = new CoeusFunctions();
        //Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
    
        paramReviewer.addElement(new Parameter("PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSequenceNumber()));
        paramReviewer.addElement(new Parameter("SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSubmissionNumber()));        
        paramReviewer.addElement(new Parameter("EXEMPT_CHKLST_CODE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getCheckListCode()));
        paramReviewer.addElement(new Parameter("UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, userId));
        paramReviewer.addElement(new Parameter("UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
         paramReviewer.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getProtocolNumber()));
        paramReviewer.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSequenceNumber()));
        paramReviewer.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                    DBEngineConstants.TYPE_INT,
                            ""+protocolReviewTypeCheckListBean.getSubmissionNumber()));        
        paramReviewer.addElement(new Parameter("AW_EXEMPT_CHKLST_CODE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getCheckListCode()));
        paramReviewer.addElement(new Parameter("AW_UPDATE_USER",
                    DBEngineConstants.TYPE_STRING, 
                            protocolReviewTypeCheckListBean.getUpdateUser()));
        paramReviewer.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                    DBEngineConstants.TYPE_TIMESTAMP,
                            protocolReviewTypeCheckListBean.getUpdateTimestamp()));
        paramReviewer.addElement(new Parameter("AC_TYPE",
                    DBEngineConstants.TYPE_STRING,
                            protocolReviewTypeCheckListBean.getAcType()));
        
        StringBuffer sqlReviewer = new StringBuffer(
                                        "call UPD_PROTO_EXEMPT_CHKLST(");
        sqlReviewer.append(" <<PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<SEQUENCE_NUMBER>> , ");
        sqlReviewer.append(" <<SUBMISSION_NUMBER>> , ");        
        sqlReviewer.append(" <<EXEMPT_CHKLST_CODE>> , ");
        sqlReviewer.append(" <<UPDATE_USER>> , ");
        sqlReviewer.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlReviewer.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlReviewer.append(" <<AW_SUBMISSION_NUMBER>> , ");
        sqlReviewer.append(" <<AW_EXEMPT_CHKLST_CODE>> , ");
        sqlReviewer.append(" <<AW_UPDATE_USER>> , ");
        sqlReviewer.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlReviewer.append(" <<AC_TYPE>> )");

        ProcReqParameter procReviewer  = new ProcReqParameter();
        procReviewer.setDSN(DSN);
        procReviewer.setParameterInfo(paramReviewer);
        procReviewer.setSqlCommand(sqlReviewer.toString());
        
        /*procedures.add(procReviewer);
        
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;*/
        
        return procReviewer;
        
    }        
    
    
    /**  Method used to update/insert all the details of a Protocol Expidited Check List.
     *  <li>To fetch the data, it uses UPD_PROTO_EXEMPT_CHKLST procedure.
     *
     * @param protocolReviewTypeCheckListBean which consists of all the details 
     * regarding expidited check list.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public boolean updateAssignToSchedule( Vector submissionInfo)  throws DBException, CoeusException{
        boolean success = true;             
        Vector procedures = new Vector(5,3);
        Vector param = new Vector();
        coeusFunctions = new CoeusFunctions();
        Timestamp dbTimestamp = coeusFunctions.getDBTimestamp();
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        for(int row = 0; row < submissionInfo.size(); row++){            
            param = new Vector();
            protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean) submissionInfo.elementAt(row);
            param.addElement(new Parameter("PROTOCOL_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                                protocolSubmissionInfoBean.getProtocolNumber()));
            param.addElement(new Parameter("SEQUENCE_NUMBER",
                        DBEngineConstants.TYPE_INT,
                                ""+protocolSubmissionInfoBean.getSequenceNumber()));
            param.addElement(new Parameter("SUBMISSION_NUMBER",
                        DBEngineConstants.TYPE_INT,
                                ""+protocolSubmissionInfoBean.getSubmissionNumber()));        
            param.addElement(new Parameter("SCHEDULE_ID",
                        DBEngineConstants.TYPE_STRING,
                                protocolSubmissionInfoBean.getScheduleId()));
            param.addElement(new Parameter("COMMITTEE_ID",
                        DBEngineConstants.TYPE_STRING,
                                protocolSubmissionInfoBean.getCommitteeId()));
            param.addElement(new Parameter("UPDATE_USER",
                        DBEngineConstants.TYPE_STRING, userId));
            param.addElement(new Parameter("UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
             param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                        DBEngineConstants.TYPE_STRING,
                                protocolSubmissionInfoBean.getProtocolNumber()));
            param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                        DBEngineConstants.TYPE_INT,
                                ""+protocolSubmissionInfoBean.getSequenceNumber()));
            param.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                        DBEngineConstants.TYPE_INT,
                                ""+protocolSubmissionInfoBean.getSubmissionNumber()));
            param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                        DBEngineConstants.TYPE_TIMESTAMP,
                                protocolSubmissionInfoBean.getUpdateTimestamp()));

            StringBuffer sqlSubmission = new StringBuffer(
                                            "call UPD_ASSIGN_TO_SCHEDULE(");
            sqlSubmission.append(" <<PROTOCOL_NUMBER>> , ");
            sqlSubmission.append(" <<SEQUENCE_NUMBER>> , ");
            sqlSubmission.append(" <<SUBMISSION_NUMBER>> , ");        
            sqlSubmission.append(" <<SCHEDULE_ID>> , ");
            sqlSubmission.append(" <<COMMITTEE_ID>> , ");
            sqlSubmission.append(" <<UPDATE_USER>> , ");
            sqlSubmission.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlSubmission.append(" <<AW_PROTOCOL_NUMBER>> , ");
            sqlSubmission.append(" <<AW_SEQUENCE_NUMBER>> , ");
            sqlSubmission.append(" <<AW_SUBMISSION_NUMBER>> , ");
            sqlSubmission.append(" <<AW_UPDATE_TIMESTAMP>> ) ");

            ProcReqParameter procReqParameter = new ProcReqParameter();
            procReqParameter.setDSN(DSN);
            procReqParameter.setParameterInfo(param);
            procReqParameter.setSqlCommand(sqlSubmission.toString());

            procedures.add(procReqParameter);
        }
        
         if(dbEngine!=null){ 
              dbEngine.executeStoreProcs(procedures);
              
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;      
    }            
    //Added for case 2785 - Routing enhancement - start
    /**
     *  Method used to Submit to Approve
     *
     *  @param protocolNumber protocol Number 
     *  @param unitNumber Unit Number
     *  @param option Option
     *  @return int integer 1 if updated successfully else 0
     *  
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */     
    public Vector submitToApprove(String protocolNumber , int moduleItemKeySeq, String unitNumber, String option)
                            throws CoeusException, DBException {
        Vector procedures = new Vector(5,3);
        Vector result = null;
        HashMap resultRow = null;
        boolean isUpdate = false;
        int PROTOCOL_MODULE = 7;
        int PROTOCOL_APPROVER_ROLE_ID = 201;
        
        dbTimestamp = (new CoeusFunctions()).getDBTimestamp();
        
        CoeusVector approvalForPropNo = null;
        CoeusVector filteredData = new CoeusVector();
        Vector vctReturnValues = new Vector();
        //Check if Maps Exist and  call Build Maps only if there are Maps
        RoutingUpdateTxnBean routingUpdateTxnBean = new RoutingUpdateTxnBean(userId);
        int mapsExist = routingUpdateTxnBean.buildMapsForRouting(protocolNumber,
                unitNumber,PROTOCOL_MODULE, moduleItemKeySeq, option);
        // COEUSDEV-273: Protocol roles update error - new se & save  - Start
//        int approvalSeq = routingUpdateTxnBean.getApprovalSequenceNumber(protocolNumber, PROTOCOL_MODULE, moduleItemKeySeq);
        //Assign rights to the approvers only if maps exist and option is S and only once
        // ie the first time
//        if(mapsExist > 0 && approvalSeq == 2 && option.equals("S")){
        // COEUSDEV-273: Protocol roles update error - new se & save  - End
        // If map is present and action is aubmit for review
        if(mapsExist > 0 && option.equals("S")){
            RoutingTxnBean routingTxnBean = new RoutingTxnBean();
            CoeusVector cvApprovers = routingTxnBean.getAllRoutingApprovers("7", protocolNumber, moduleItemKeySeq);
            ProtocolDataTxnBean  protocolDataTxnBean  = new ProtocolDataTxnBean();
            Vector vecExistingApprovers = protocolDataTxnBean.getProtocolUser(protocolNumber, moduleItemKeySeq, PROTOCOL_APPROVER_ROLE_ID);
            int roleSequenceNumber = moduleItemKeySeq;
            if(vecExistingApprovers != null && vecExistingApprovers.size() > 0){
                // get the max sequence number for the protocol from Protocol User Roles table
                // and use the same sequence number to save the new approvers.
                ProtocolRoleInfoBean roleInfoBean = (ProtocolRoleInfoBean) vecExistingApprovers.elementAt(0);
                int sequenceFromBean = roleInfoBean.getSequenceNumber();
                if(sequenceFromBean > 0){
                    roleSequenceNumber = sequenceFromBean;;
                }
            }
            //Added for Case 4363 - Notification Rules not working -Start
            //COEUSDEV-75:Rework email engine so the email body is picked up from one place
            routingUpdateTxnBean.sendNotification(ModuleConstants.PROTOCOL_MODULE_CODE, protocolNumber, moduleItemKeySeq, unitNumber);
            //COEUSDEV-75:End
            //Added for Case 4363 - Notification Rules not working -End
            if(cvApprovers!=null && cvApprovers.size()>0){
                RoutingDetailsBean routingDetailsBean = null;
                ProtocolRolesFormBean protocolRolesFormBean = null;
                ProtocolUpdateTxnBean protocolUpdateTxnBean = new ProtocolUpdateTxnBean(userId);
                for(int index = 0; index< cvApprovers.size(); index++){
                    routingDetailsBean = (RoutingDetailsBean)cvApprovers.get(index);
                    protocolRolesFormBean = new ProtocolRolesFormBean();
                    protocolRolesFormBean.setProtocolNumber(protocolNumber);
//                    protocolRolesFormBean.setSequenceNumber(moduleItemKeySeq);
                    protocolRolesFormBean.setSequenceNumber(roleSequenceNumber);
                    protocolRolesFormBean.setUserId(routingDetailsBean.getUserId());
                    protocolRolesFormBean.setRoleId(PROTOCOL_APPROVER_ROLE_ID);
                    // COEUSDEV-273: Protocol roles update error - new se & save  
                    // Insert approver only if he is not present
                    if(!checkApproverPresent(vecExistingApprovers, routingDetailsBean.getUserId())){
                        protocolRolesFormBean.setAcType("I");
                        procedures.add(protocolUpdateTxnBean.addProtocolRoles(protocolRolesFormBean));
                    }
                }
            }
            if(procedures.size() > 0){
                if(dbEngine!=null){ 
                    dbEngine.executeStoreProcs(procedures);
                }else{
                    throw new CoeusException("db_exceptionCode.1000");
               }
            }
            
            
        }
        vctReturnValues.addElement(new Integer(mapsExist)); //Maps exist indicator
        return vctReturnValues;
    }
    
    /**
     * Update the protocol submission status
     * 
     * @param protocolNumber protocol number
     * @param sequenceNumber sequence number
     * @param submissionNumber submission number
     * @param status
     */
    public boolean updateProtoSubmissionStatus(String protocolNumber, 
            int sequenceNumber, int submissionNumber, int status )throws CoeusException, DBException {
        boolean success = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+sequenceNumber));
        param.add(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+submissionNumber));
        param.add(new Parameter("STATUS",
                DBEngineConstants.TYPE_INT,""+status));
        param.add(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call fn_upd_prot_submission_status(");
        sql.append(" <<PROTOCOL_NUMBER>> , ");
        sql.append(" <<SEQUENCE_NUMBER>> , ");
        sql.append(" <<SUBMISSION_NUMBER>> , ");
        sql.append(" <<STATUS>> , ");
        sql.append(" <<UPDATE_USER>> )} ");
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                success = true;
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
            
    }
    //Added for case 2785 - Routing enhancement - end
    
     //Main method for testing the bean
    /*public static void main(String args[]) {
        ProtocolSubmissionUpdateTxnBean txnUser = new ProtocolSubmissionUpdateTxnBean("COEUS");
        ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
        ProtocolSubmissionVoteFormBean svBean = new ProtocolSubmissionVoteFormBean();
        ProtocolVoteAbsFormBean vaBean = new ProtocolVoteAbsFormBean();
        try{
            Vector vctSubmissionInfo = new Vector();
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
            protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails("0310002144");
            protocolSubmissionInfoBean.setAcType("U");
            vctSubmissionInfo.addElement(protocolSubmissionInfoBean);
            protocolSubmissionInfoBean = protocolSubmissionTxnBean.getProtocolSubmissionDetails("0310002101");
            protocolSubmissionInfoBean.setAcType("U");
            vctSubmissionInfo.addElement(protocolSubmissionInfoBean);            
            boolean blnSuccess = txnUser.updateAssignToSchedule(vctSubmissionInfo);
            System.out.println("Update :"+blnSuccess);
      */      
            /*Vector vct = protocolSubmissionTxnBean.getProtocolExemptCheckList("0000000528", 1);
            ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean = null;
            protocolReviewTypeCheckListBean = (ProtocolReviewTypeCheckListBean)vct.elementAt(0);
            protocolReviewTypeCheckListBean.setAcType("U");
            boolean success = txnUser.addUpdProtocolExemptCheckList(protocolReviewTypeCheckListBean);
            System.out.println("Success :" +success);
            */
            
            // for testing protocol Assignment method
            /*java.sql.Date dt = new java.sql.Date(1234);
            dt.setDate(19);
            dt.setMonth(10);
            dt.setYear(2002-1900);*/
            /*Timestamp ts = Timestamp.valueOf("2003-02-13 12:22:12");
            
            vaBean.setScheduleId("3541");
            vaBean.setProtocolNumber("0000000728");
            vaBean.setSequenceNumber(2);
            vaBean.setPersonId("5460");
            vaBean.setComments("Testing");
            vaBean.setUpdateTimestamp(ts);
            vaBean.setAcType("I");

            Vector vecSub = new Vector();
            vecSub.add(vaBean);
            
            svBean.setProtocolVoteAbstainee(vecSub);
            svBean.setScheduleId("3541");
            svBean.setProtocolNumber("0000000728");
            svBean.setSequenceNumber(2);
            svBean.setYesVoteCount(3);
            svBean.setNoVoteCount(3);
            svBean.setUpdateTimestamp(ts);
            svBean.setAcType("U");
            
            // end of schedule bean
            
            
            success = txnUser.addUpdProtocolVoteSubmission(svBean);
            if(success)
                System.out.println("successfully Update");
            else
                System.out.println("exception while insert");
            */
        /*    
        }catch(Exception e){
            System.out.println("Exception :"+e);
            e.printStackTrace();
        }
    }*/
    /**
     * Method accepts a Vecor of RoleInfoBean and a user id
     * return true if the user id is the same user id of any of the RoleInfoBean
     */
    private boolean checkApproverPresent(Vector vecExistingApprovers, String userId) {
        boolean present = false;
        if(vecExistingApprovers != null &&  !vecExistingApprovers.isEmpty()){
            int size = vecExistingApprovers.size();
            RoleInfoBean roleInfoBean;
            for(int index = 0; index < size; index++){
                roleInfoBean = (RoleInfoBean) vecExistingApprovers.elementAt(index);
                if(roleInfoBean.getUserId().equalsIgnoreCase(userId)){
                    present = true;
                    break;
                }
            }
        }
        return present;
    }

    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    
    /*
     * The Method to update protocol action details in OSP$PROTOCOL_ACTIONS table by calling UPD_PROTOCOL_ACTION procedure
     * @param protocolActionsBean
     * @return success
     */
    public boolean updateProtocolAction(ProtocolActionsBean protocolActionsBean)throws CoeusException, DBException {
        boolean success = false;
        Vector parmAction= new Vector();
        Vector procedures = new Vector();
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        parmAction.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolActionsBean.getProtocolNumber()));
        
        parmAction.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getSequenceNumber()));
        
        parmAction.addElement(new Parameter("ACTION_ID",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getActionId()));
        
        parmAction.addElement(new Parameter("PROTOCOL_ACTION_TYPE_CODE",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getActionTypeCode()));
        
        parmAction.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,""+protocolActionsBean.getSubmissionNumber()));
        
        parmAction.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,protocolActionsBean.getComments()));
        
        parmAction.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        
        parmAction.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        
        parmAction.addElement(new Parameter("ACTION_DATE",
                DBEngineConstants.TYPE_DATE,protocolActionsBean.getActionDate()));
        
        parmAction.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,protocolActionsBean.getAcType()));
        
        StringBuffer sqlProtoAction = new StringBuffer("call UPD_PROTOCOL_ACTION(");
        sqlProtoAction.append(" <<PROTOCOL_NUMBER>> , ");
        sqlProtoAction.append(" <<SEQUENCE_NUMBER>> , ");
        sqlProtoAction.append(" <<ACTION_ID>> , ");
        sqlProtoAction.append(" <<PROTOCOL_ACTION_TYPE_CODE>> , ");
        sqlProtoAction.append(" <<SUBMISSION_NUMBER>> , ");
        sqlProtoAction.append(" <<COMMENTS>> , ");
        sqlProtoAction.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlProtoAction.append(" <<UPDATE_USER>> , ");
        sqlProtoAction.append(" <<ACTION_DATE>>, ");
        sqlProtoAction.append(" <<AC_TYPE>> )");
        
        ProcReqParameter procAction  = new ProcReqParameter();
        procAction.setDSN(DSN);
        procAction.setParameterInfo(parmAction);
        procAction.setSqlCommand(sqlProtoAction.toString());
        
        procedures.add(procAction);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /*
     * The Method to update protocol action details in OSP$PROTOCOL_SUBMISSION table by calling upd_proto_submission procedure
     * @param submissionInfoBean
     * @return success
     */ 
    public boolean updateProtocolSubmission(ProtocolSubmissionInfoBean submissionInfoBean)throws CoeusException, DBException{
        boolean success = false;
        Vector param= new Vector();
        Vector procedures = new Vector();
        coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getProtocolNumber()));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getSequenceNumber()));
        param.addElement(new Parameter("SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getScheduleId()));
        param.addElement(new Parameter("SUBMISSION_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getSubmissionTypeCode()));
        param.addElement(new Parameter("SUBMISSION_TYPE_QUAL_CODE",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getSubmissionQualTypeCode() == 0 ? null :
                    ""+submissionInfoBean.getSubmissionQualTypeCode()));
        param.addElement(new Parameter("PROTOCOL_REVIEW_TYPE_CODE",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getProtocolReviewTypeCode()));
        param.addElement(new Parameter("SUBMISSION_STATUS_CODE",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getSubmissionStatusCode()));
        param.addElement(new Parameter("SUBMISSION_DATE",
                DBEngineConstants.TYPE_DATE,dbTimestamp));
        param.addElement(new Parameter("COMMENTS",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getComments()));
        param.addElement(new Parameter("YES_VOTE_COUNT",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getYesVoteCount()));
        param.addElement(new Parameter("NO_VOTE_COUNT",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getNoVoteCount()));
        param.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,dbTimestamp));
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getProtocolNumber()));
        param.addElement(new Parameter("AW_SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getAWSequenceNumber()));
        param.addElement(new Parameter("AW_SCHEDULE_ID",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getAWScheduleId()));
        param.addElement(new Parameter("AW_UPDATE_USER",
                DBEngineConstants.TYPE_STRING, userId));
        param.addElement(new Parameter("AW_UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP,
                submissionInfoBean.getUpdateTimestamp()));
        param.addElement(new Parameter("AC_TYPE",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getAcType()));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+ (submissionInfoBean.getSubmissionNumber())));
        param.addElement(new Parameter("AW_SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getSubmissionNumber()));
        param.addElement(new Parameter("ABSTAINER_COUNT",
                DBEngineConstants.TYPE_INT,
                ""+submissionInfoBean.getAbstainerCount()));
        param.addElement(new Parameter("VOTING_COMMENTS",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getVotingComments()));
        param.addElement(new Parameter("COMMITTEE_ID",
                DBEngineConstants.TYPE_STRING,
                submissionInfoBean.getCommitteeId()));
        
        StringBuffer sqlAssignment = new StringBuffer("call upd_proto_submission(");
        sqlAssignment.append(" <<PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_TYPE_QUAL_CODE>> , ");
        sqlAssignment.append(" <<PROTOCOL_REVIEW_TYPE_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_STATUS_CODE>> , ");
        sqlAssignment.append(" <<SUBMISSION_DATE>> , ");
        sqlAssignment.append(" <<COMMENTS>> , ");
        sqlAssignment.append(" <<YES_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<NO_VOTE_COUNT>> , ");
        sqlAssignment.append(" <<UPDATE_USER>> , ");
        sqlAssignment.append(" <<UPDATE_TIMESTAMP>> , ");
        sqlAssignment.append(" <<ABSTAINER_COUNT>> , ");
        sqlAssignment.append(" <<VOTING_COMMENTS>> , ");
        sqlAssignment.append(" <<SUBMISSION_NUMBER>> , ");
        sqlAssignment.append(" <<COMMITTEE_ID>> , ");
        sqlAssignment.append(" <<AW_PROTOCOL_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SEQUENCE_NUMBER>> , ");
        sqlAssignment.append(" <<AW_SCHEDULE_ID>> , ");
        sqlAssignment.append(" <<AW_UPDATE_USER>> , ");
        sqlAssignment.append(" <<AW_UPDATE_TIMESTAMP>> , ");
        sqlAssignment.append(" <<AW_SUBMISSION_NUMBER>> , ");
        sqlAssignment.append(" <<AC_TYPE>> )");

        ProcReqParameter procAssignment  = new ProcReqParameter();
        procAssignment.setDSN(DSN);
        procAssignment.setParameterInfo(param);
        procAssignment.setSqlCommand(sqlAssignment.toString());
        ProcReqParameter procAction  = new ProcReqParameter();
        procAction.setDSN(DSN);
        procAction.setParameterInfo(param);
        procAction.setSqlCommand(sqlAssignment.toString());
        
        procedures.add(procAction);
        
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        success = true;
        return success;
    }
    
    /*
     * The method to clean all the temporary protocol submission data in OSP$PROTOCOL_SUBMISSION,
     * OSP$PROTOCOL_ACTIONS, OSP$QUESTIONNAIRE_ANS_HEADER and OSP$QUESTIONNAIRE_ANSWERS tables 
     * through fn_del_temp_proto_sub_details function
     * @param protocolNumber
     * @return success
     */
     public boolean cleanTemporarySubmissionDetails(String protocolNumber)throws CoeusException, DBException {
        boolean success = false;
        Vector param= new Vector();
        Vector result = new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolNumber));
        
        StringBuffer sql = new StringBuffer("{ <<OUT INTEGER STATUS>> = call fn_del_temp_proto_sub_details(");
        sql.append(" <<PROTOCOL_NUMBER>> )} ");
        
        if(dbEngine!=null){
            try{
                result = dbEngine.executeFunctions("Coeus", sql.toString(), param);
                success = true;
            }catch (DBException dbEx){
                throw new CoeusException(dbEx.getMessage());
            }
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        return success;
    }
    //COEUSDEV-86 : End  
    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - Start
    public void sendMailsForReviewerChange(Vector reviewerList) {
        
        boolean mailSent;
        
        PersonInfoTxnBean personTxnBean = new PersonInfoTxnBean();
        int totalReviewers = reviewerList.size();
        PersonInfoFormBean personBean = null;
        
       // MailHandler mailHandler = new MailHandler();
        MailNotification mailNotification = new  ProtocolMailNotification();
        for(int index = 0; index < totalReviewers; index ++){
            ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean)reviewerList.elementAt(index);
            
            if(TypeConstants.INSERT_RECORD.equalsIgnoreCase(protocolReviewerInfoBean.getAcType())){
                
                // Send mail to the added user 
                try {
                    personBean = personTxnBean.getPersonInfo(protocolReviewerInfoBean.getPersonId()) ;
                } catch (Exception ex) {
                    UtilFactory.log(ex.getMessage());
                }
                PersonRecipientBean recipient = new PersonRecipientBean();
                recipient.setEmailId(personBean.getEmail());
                //Added for COEUSQA-3389 : IACUC Email Notifications vs. User Preferences - start
                //Using personid of user chkecking whether userid is thr in user table.
                //If userid is there for particular user then setting that value to the recipient
                String userId = null;
                DepartmentPersonTxnBean departmentPersonTxnBean = new DepartmentPersonTxnBean();
                if(protocolReviewerInfoBean.getPersonId() != null){
                    Vector userInformation = null;
                    try {
                        userInformation = (Vector) departmentPersonTxnBean.getUserInfoForPerson(protocolReviewerInfoBean.getPersonId());
                    } catch (Exception ex) {
                        UtilFactory.log(ex.getMessage());
                    } 
                    if(userInformation != null && userInformation.size() > 0){
                        userId = (String)userInformation.elementAt(0);
                    }
                }
                if(userId != null){
                    recipient.setUserId(userId);
                }
                //Added for COEUSQA-3389 : IACUC Email Notifications vs. User Preferences - end
                Vector vecRecipients = new Vector();
                vecRecipients.add(recipient);
                MailMessageInfoBean mailMsgInfoBean = null;
                // Send mail to newly added reviewer
                try{
                    mailMsgInfoBean = mailNotification.prepareNotification(USER_ADDED_AS_PROTO_REVIEWER_ACTION_CODE,
                            protocolReviewerInfoBean.getProtocolNumber(), protocolReviewerInfoBean.getSequenceNumber());
                    
                    if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                        mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                        mailSent = mailNotification.sendNotification(USER_ADDED_AS_PROTO_REVIEWER_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(),
                                protocolReviewerInfoBean.getSequenceNumber(),mailMsgInfoBean);
                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                    
                }
                // Send mail to  all the recipients
                try {
                    MailMessageInfoBean  mailMsgInfoBeanForRole = mailNotification.prepareNotification( REVIEWER_ADDED_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(), protocolReviewerInfoBean.getSequenceNumber());
                    
                    if( mailMsgInfoBeanForRole!= null && mailMsgInfoBeanForRole.isActive()){
                        
                        
                        Object[] arguments = {personBean.getFullName(), protocolReviewerInfoBean.getProtocolNumber()};
                       // String mailBody = mailBody = MessageFormat.format(mailMsgInfoBeanForRole.getMessage(), arguments);
                      //  mailMsgInfoBeanForRole.setMessage(mailBody);
                        
                        
                        String message = MailProperties.getProperty(IRB_NOTIFICATION+DOT+REVIEWER_ADDED_ACTION_CODE+DOT+MESSAGE, "");
                        message = MessageFormat.format(message, arguments);
                        mailMsgInfoBeanForRole.appendMessage(message, "\n");
                        // Added for COEUSQA-3336 - Issues in reviewer notification - Start
                        // Notificatio to be send to the added reviewer, even he/she has roles or not
                        mailMsgInfoBeanForRole.setPersonRecipientList(vecRecipients);
                        // Added for COEUSQA-3336 - Issues in reviewer notification - End
                        mailSent = mailNotification.sendNotification( REVIEWER_ADDED_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(),
                                protocolReviewerInfoBean.getSequenceNumber(),mailMsgInfoBeanForRole);
                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
            } else if(TypeConstants.DELETE_RECORD.equalsIgnoreCase(protocolReviewerInfoBean.getAcType())){
                
                
                try {
                    personBean = personTxnBean.getPersonInfo(protocolReviewerInfoBean.getPersonId()) ;
                } catch (Exception ex) {
                    UtilFactory.log(ex.getMessage());
                }
                PersonRecipientBean recipient = new PersonRecipientBean();
                recipient.setEmailId(personBean.getEmail());
                Vector vecRecipients = new Vector();
                vecRecipients.add(recipient);
                // Send mail to removed reviewer.       
                try {
                    MailMessageInfoBean  mailMsgInfoBean = mailNotification.prepareNotification(USER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(), protocolReviewerInfoBean.getSequenceNumber());
                    
                    if(mailMsgInfoBean != null && mailMsgInfoBean.isActive()){
                        mailMsgInfoBean.setPersonRecipientList(vecRecipients);
                        mailSent = mailNotification.sendNotification( USER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(),
                                protocolReviewerInfoBean.getSequenceNumber(),mailMsgInfoBean);
                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
                
                // Send mail to all the recipients
                try {
                    MailMessageInfoBean  mailMsgInfoBeanForRole = mailNotification.prepareNotification(REVIEWER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(), protocolReviewerInfoBean.getSequenceNumber());
                    
                    if(mailMsgInfoBeanForRole.isActive()){
                        
                        Object[] arguments = {personBean.getFullName(), protocolReviewerInfoBean.getProtocolNumber()};
                        //String mailBody = mailBody = MessageFormat.format(mailMsgInfoBeanForRole.getMessage(), arguments);
                        //mailMsgInfoBeanForRole.setMessage(mailBody);
                        
                        String message = MailProperties.getProperty(IRB_NOTIFICATION+DOT+REVIEWER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE+DOT+MESSAGE, "");
                        message = MessageFormat.format(message, arguments);
                        mailMsgInfoBeanForRole.appendMessage(message, "\n");
                        // Added for COEUSQA-3336 - Issues in reviewer notification - Start
                        // Notification to be send to the removed reviewer, even he/she has roles or not
                        mailMsgInfoBeanForRole.setPersonRecipientList(vecRecipients);
                        // Added for COEUSQA-3336 - Issues in reviewer notification - End
                        mailSent = mailNotification.sendNotification( REVIEWER_REMOVED_FROM_REVIEWER_LIST_ACTION_CODE, protocolReviewerInfoBean.getProtocolNumber(),
                                protocolReviewerInfoBean.getSequenceNumber(),mailMsgInfoBeanForRole);
                    }
                } catch (Exception ex){
                    UtilFactory.log(ex.getMessage());
                }
            }
        }
    }

    // COEUSQA-2045: Coeus 4.4 QA - IRB Online Review - Review Notification - End
    
    //Added for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
     /**  Method used to update/insert all the details of a Protocol Reviewer and commit it to DB.
     *  <li>To fetch the data, it uses upd_proto_reviewer procedure.
     * @param protocolReviewerInfoBean which consists of all the details 
     * regarding reviewers for a protocol.
     * @return ProcReqParameter the DSN,StringBuffer and parameter is set in
     *  this class before executing the procedure.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public boolean addUpdProtocolReviewerDetails( ProtocolReviewerInfoBean
            protocolReviewerInfoBean)  throws DBException, CoeusException{
        boolean isSuccess = false;
        Vector procedures = new Vector();
        procedures.add(addUpdProtocolReviewer(protocolReviewerInfoBean));
        if(dbEngine!=null){
            dbEngine.executeStoreProcs(procedures);
            
        }else{
            isSuccess = false;
            throw new CoeusException("db_exceptionCode.1000");
        }
        return isSuccess;
    }
    //COEUSQA-2288 : End
    
    // Added for COEUSQA-3336 - Issues in reviewer notification - Start
    /**
     * Method to update the reviewer details and send notificaiton for added and delete reviewers
     * @param submissionInfoBean
     * @throws edu.mit.coeus.utils.dbengine.DBException
     */
    private void updateSendMailToReviewer(ProtocolSubmissionInfoBean submissionInfoBean) throws DBException{
        Vector vecUpdateReviewers = new Vector();
        Vector vecAddReviewers = new Vector();
        Vector vecDeleteReviewers = new Vector();
        Vector vecReviewers = submissionInfoBean.getProtocolReviewer();
        if (vecReviewers != null && !vecReviewers.isEmpty()){
            for(Object reviewerDetails : vecReviewers){
                ProtocolReviewerInfoBean protocolReviewerInfoBean = (ProtocolReviewerInfoBean)reviewerDetails;
                if (protocolReviewerInfoBean.getAcType() == null || TypeConstants.UPDATE_RECORD.equals(protocolReviewerInfoBean.getAcType())) {
                    protocolReviewerInfoBean.setAcType(TypeConstants.UPDATE_RECORD);
                    protocolReviewerInfoBean.setProtocolNumber(submissionInfoBean.getProtocolNumber());
                    protocolReviewerInfoBean.setSequenceNumber(submissionInfoBean.getSequenceNumber());
                    protocolReviewerInfoBean.setSubmissionNumber(submissionInfoBean.getSubmissionNumber());
                    vecUpdateReviewers.add(protocolReviewerInfoBean);
                }
                if (TypeConstants.INSERT_RECORD.equals(protocolReviewerInfoBean.getAcType())) {
                    protocolReviewerInfoBean.setUpdateTimestamp(dbTimestamp);
                    protocolReviewerInfoBean.setProtocolNumber(submissionInfoBean.getProtocolNumber());
                    protocolReviewerInfoBean.setSequenceNumber(submissionInfoBean.getSequenceNumber());
                    protocolReviewerInfoBean.setSubmissionNumber(submissionInfoBean.getSubmissionNumber());
                    vecAddReviewers.add(protocolReviewerInfoBean);
                }
                if(TypeConstants.DELETE_RECORD.equals(protocolReviewerInfoBean.getAcType())){
                    vecDeleteReviewers.add(protocolReviewerInfoBean);
                }
            }
            // Modified reviewer details will be saved first
            if(!vecUpdateReviewers.isEmpty()){
                Vector vecReviewerProc = new Vector();
                for(Object reviewerDetails : vecUpdateReviewers){
                    ProtocolReviewerInfoBean reviewerInfoBean = (ProtocolReviewerInfoBean)reviewerDetails;
                    vecReviewerProc.add(addUpdProtocolReviewer(reviewerInfoBean));
                }
                dbEngine.executeStoreProcs(vecReviewerProc);
            }
            //  For newly added reviewers notification will be send first and reviewers will save to the database
            if(!vecAddReviewers.isEmpty()){
                sendMailsForReviewerChange(vecAddReviewers);
                Vector vecReviewerProc = new Vector();
                for(Object reviewerDetails : vecAddReviewers){
                    ProtocolReviewerInfoBean reviewerInfoBean = (ProtocolReviewerInfoBean)reviewerDetails;
                    vecReviewerProc.add(addUpdProtocolReviewer(reviewerInfoBean));
                }
                dbEngine.executeStoreProcs(vecReviewerProc);
            }
            // For deleted reviewers, reviewers will deleted fom the dataabse first and notification will be send
            if(!vecDeleteReviewers.isEmpty()){
                Vector vecReviewerProc = new Vector();
                for(Object reviewerDetails : vecDeleteReviewers){
                    ProtocolReviewerInfoBean reviewerInfoBean = (ProtocolReviewerInfoBean)reviewerDetails;
                    vecReviewerProc.add(addUpdProtocolReviewer(reviewerInfoBean));
                }
                dbEngine.executeStoreProcs(vecReviewerProc);
                sendMailsForReviewerChange(vecDeleteReviewers);
            }
        }
    }
    // Added for COEUSQA-3336 - Issues in reviewer notification - End
    
}//End of class
