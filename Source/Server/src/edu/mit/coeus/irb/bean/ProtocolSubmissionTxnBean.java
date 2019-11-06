/*
 * @(#)ProtocolSubmissionTxnBean.java 1.0 11/25/02 10:43 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.CoeusFunctions;
import edu.mit.coeus.utils.dbengine.DBEngineImpl;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.utils.dbengine.Parameter;
import edu.mit.coeus.utils.dbengine.DBEngineConstants;
import edu.mit.coeus.exception.CoeusException;
//import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.CheckListBean;
import edu.mit.coeus.bean.CoeusMessageResourcesBean;
import edu.mit.coeus.utils.dbengine.ProcReqParameter;

import java.util.Vector;
import java.util.HashMap;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

/**
 * This class provides the methods for performing all procedure executions for
 * a Protocol Submission functionality. Various methods are used to fetch
 * the Protocol Submission details from the Database.
 * All methods are used <code>DBEngineImpl</code> instance for the 
 * database interaction.
 *
 * @version 1.0 on November 25, 2002, 10:43 AM
 * @author  Mukundan C
 */

public class ProtocolSubmissionTxnBean {
    
    // Singleton instance of a dbEngine
    private DBEngineImpl dbEngine;

    //Added for case#3046 - Notify IRB attachments - start    
    private static final String DSN = "Coeus";    
    private Timestamp dbTimestamp;    
    //Added for case#3046 - ability to upload an attachment - end
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private static final int NOTIFY_IRB = 116;
    private static final int REQUEST_TO_CLOSE_ACTION = 105;
    private static final int REQUEST_FOR_SUSPENSION_ACTION = 106;
    private static final int REQUEST_TO_CLOSE_ENROLLMENT_ACTION = 108;
    private static final int REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION = 114;
    private static final int REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION= 115;
    private static final int FYI =112;
    private static final int REQUEST_TO_CLOSE_SUBMISSION = 109;
    private static final int REQUEST_FOR_SUSPENSION_SUBMISSION = 110;
    private static final int REQUEST_TO_CLOSE_ENROLLMENT_SUBMISSION = 111;
    private static final int REQUEST_FOR_DATA_ANALYSIS_ONLY_SUBMISSION = 113;
    private static final int REQUEST_FOR_RE_OPEN_ENROLLMENT_SUBMISSION= 114;
    //COEUSDEV-86 : End
    
    /** Creates a new instance of ProtocolSubmissionTxnBean */
    public ProtocolSubmissionTxnBean() {
        dbEngine = new DBEngineImpl();
    }
    
    /**
     *  This method used populates combo box with protocol reviewer type in the 
     *  Protocol submission screen from OSP$PROTOCOL_REVIEWER_TYPE.
     *  <li>To fetch the data, it uses the procedure get_protocol_reviewer_types.
     *
     *  @return Vector map of all Protocol reviewer types with Protocol reviewer 
     *  types code as key and Protocol Reviewer type description as value.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolReviewerTypes() throws CoeusException,DBException{
        Vector result = null;
        Vector reviewerTypes = null;
        Vector param= new Vector();
        HashMap reviewerType = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_protocol_reviewer_types( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int actionCount = result.size();
        if (actionCount > 0){
            reviewerTypes = new Vector();
            for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
                reviewerType = (HashMap)result.elementAt(rowIndex);
                reviewerTypes.addElement(new ComboBoxBean(
                        reviewerType.get(
                        "REVIEWER_TYPE_CODE").toString(),
                        reviewerType.get("DESCRIPTION").toString()));
            }
        }
        return reviewerTypes;
    }
   
    //Coeus enhancement 32.10 added by shiji - step 1: start
    public Vector getProtocolSubmissionReviewType(String protocolId) throws CoeusException,DBException {
        Object value = null;
        Vector resultVector= new Vector();
        Vector param= new Vector();
        param.add(new Parameter("PROTOCOL_ID",
                            DBEngineConstants.TYPE_STRING,protocolId));
        
        HashMap submissionTypeCode = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER VALUE>>=call FN_GET_SUB_TYPE_FOR_PROTO( "
                                        +"<< PROTOCOL_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            submissionTypeCode = (HashMap)result.elementAt(0);
            value = submissionTypeCode.get("VALUE");
            resultVector.addElement(value);
        }
        
        HashMap reviewTypeCode = null;
        result = new Vector();
        value = null;
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER VAL>>=call FN_GET_REVIEW_TYPE_FOR_PROTO( "
                                        +"<< PROTOCOL_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            reviewTypeCode = (HashMap)result.elementAt(0);
            value = reviewTypeCode.get("VAL");
            resultVector.addElement(value);
        }
        return resultVector;
    }
    //Coeus enhancement 32.10 - step 1: end
    
    /**
     *  Method used to get committeeId and commiteeName for a given protocol 
     *  number from OSP$COMMITTEE after the PRA and CRA are matching.
     *  <li>To fetch the data, it uses get_matching_comm_for_proto procedure.
     *
     *  @param protocolNumber to get committee list for protocol number.
     *  @return Vector map of committee list data is set of 
     *  CommitteeMaintenanceFormBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getMatchingCommitteeForProtocol(String protocolNumber) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        CommitteeMaintenanceFormBean committeeMaintenanceFormBean = null;
        HashMap committeeForProtoDetailRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_matching_comm_for_proto( <<PROTOCOL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        // case 799 
        /* When a protocol is submitted for review, if there are not committee that matches its areas of research,
         * user does not get any response. Submission window does not open up and no error message
         */
        
        Vector committeeForProtoList = new Vector(); // to fix case 799 committeeForProtoList is 
                                                    // assigned a new Vector() instead  of null
        
        if (listSize > 0){
            committeeForProtoList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            committeeMaintenanceFormBean = new CommitteeMaintenanceFormBean();
            committeeForProtoDetailRow = (HashMap)result.elementAt(rowIndex);
            committeeMaintenanceFormBean.setCommitteeId(
                      committeeForProtoDetailRow.get("COMMITTEE_ID").toString());
            committeeMaintenanceFormBean.setCommitteeName(
                      committeeForProtoDetailRow.get("COMMITTEE_NAME").toString());
            
            committeeForProtoList.add(committeeMaintenanceFormBean);
            }
        }
        return committeeForProtoList;
    }
    
    /**
     *  Method used to get protocol Reviewers list for a given protocol number
     *  and sequence number from OSP$PROTOCOL_REVIEWERS,OSP$COMM_MEMBERSHIPS.
     *  <li>To fetch the data, it uses get_proto_reviewers procedure.
     *
     *  @param protocolNumber input to the procedure to get protocol reviewer 
     *  @param seqNumber to get protocol reviewer.
     *  @return Vector map of reviewer list data is set of ProtocolReviewerInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getProtocolReviewers(String protocolNumber,int seqNumber, int submissionNumber) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolReviewerInfoBean protocolReviewerInfoBean = null;
        HashMap reviewerDetailRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                 DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(submissionNumber).toString()));        
       
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_proto_reviewers( <<PROTOCOL_NUMBER>>, <<SEQUENCE_NUMBER>>, <<SUBMISSION_NUMBER>>, "
            + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector reviewersList = null;
         if (listSize > 0){
            reviewersList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            protocolReviewerInfoBean = new ProtocolReviewerInfoBean();
            reviewerDetailRow = (HashMap)result.elementAt(rowIndex);
            //Commented following code as Schedule_Id is removed and Submission_Number is added
            /*protocolReviewerInfoBean.setScheduleId( (String)
                            reviewerDetailRow.get("SCHEDULE_ID"));
            */
            protocolReviewerInfoBean.setSubmissionNumber( 
                Integer.parseInt(reviewerDetailRow.get("SUBMISSION_NUMBER").toString()));            
            protocolReviewerInfoBean.setProtocolNumber( (String)
                        reviewerDetailRow.get("PROTOCOL_NUMBER"));
            protocolReviewerInfoBean.setSequenceNumber(
                                Integer.parseInt(reviewerDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            
            /* inserted on 13-02-03 for maintaining old sequence number which
               will be used in where clause in the procedure */
            protocolReviewerInfoBean.setAWSequenceNumber(
                                Integer.parseInt(reviewerDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            
            protocolReviewerInfoBean.setPersonId( (String)
                                 reviewerDetailRow.get("PERSON_ID"));
            protocolReviewerInfoBean.setAWPersonId( (String)
                                 reviewerDetailRow.get("PERSON_ID"));
            protocolReviewerInfoBean.setPersonName( (String)
                               reviewerDetailRow.get("PERSON_NAME"));
            protocolReviewerInfoBean.setIsNonEmployee(
                reviewerDetailRow.get("NON_EMPLOYEE_FLAG")==null ? false : 
                    (reviewerDetailRow.get("NON_EMPLOYEE_FLAG").toString().equalsIgnoreCase("Y") ? true : false));
            protocolReviewerInfoBean.setAwNonEmployee(            
                reviewerDetailRow.get("NON_EMPLOYEE_FLAG")==null ? false : 
                    (reviewerDetailRow.get("NON_EMPLOYEE_FLAG").toString().equalsIgnoreCase("Y") ? true : false));                    
            protocolReviewerInfoBean.setReviewerTypeCode(     
                        Integer.parseInt(reviewerDetailRow.get(
                        "REVIEWER_TYPE_CODE") == null ? "0" :
                        reviewerDetailRow.get("REVIEWER_TYPE_CODE").toString()));
            protocolReviewerInfoBean.setAWReviewerTypeCode(     
                        Integer.parseInt(reviewerDetailRow.get(
                        "REVIEWER_TYPE_CODE") == null ? "0" :
                        reviewerDetailRow.get("REVIEWER_TYPE_CODE").toString()));
            // 3282: Reviewer View of Protocol materials - Start
            protocolReviewerInfoBean.setAssignedDate(                 
                    reviewerDetailRow.get("ASSIGNED_DATE") == null ? null
                            :new Date( ((Timestamp) reviewerDetailRow.get(
                                "ASSIGNED_DATE")).getTime()));
            protocolReviewerInfoBean.setDueDate(reviewerDetailRow.get("DUE_DATE") == null ? null
                            :new Date( ((Timestamp) reviewerDetailRow.get(
                                "DUE_DATE")).getTime()));
            protocolReviewerInfoBean.setRecommendedActionCode(
                    reviewerDetailRow.get("RECOMMENDED_ACTION") == null ? "" :
                    reviewerDetailRow.get("RECOMMENDED_ACTION").toString());
            protocolReviewerInfoBean.setReviewComplete(            
                reviewerDetailRow.get("REVIEW_COMPLETE") == null ? false : 
                    (reviewerDetailRow.get("REVIEW_COMPLETE").toString().equalsIgnoreCase("Y") ? true : false));
            //3012: notification for when a reviewer completes their review in IACUC - start
            protocolReviewerInfoBean.setAwReviewComplete(protocolReviewerInfoBean.isReviewComplete());
            //3012: notification for when a reviewer completes their review in IACUC - end
            // 3282: Reviewer View of Protocol materials - End
            protocolReviewerInfoBean.setReviewerTypeDesc( (String)
                    reviewerDetailRow.get("DESCRIPTION"));
            protocolReviewerInfoBean.setUpdateTimestamp( 
                        (Timestamp)reviewerDetailRow.get("UPDATE_TIMESTAMP"));
            protocolReviewerInfoBean.setUpdateUser( (String)
                            reviewerDetailRow.get("UPDATE_USER"));

            reviewersList.add(protocolReviewerInfoBean);
            }
        }
        return reviewersList;
    }
    //Coeus enhancement Case #1791 - step 1: start
     public Vector getSubmissionReviewType(String protocolNumber) 
                                            throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        SubmissionReviewTypeBean submissionReviewTypeBean = null;
        HashMap submissionReviewDetailRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                 DBEngineConstants.TYPE_STRING,protocolNumber));     
       
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_VALID_PROTO_SUB_REV_TYPES( <<PROTOCOL_NUMBER>>, "
            + " <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector SubmissionReviewList = null;
         if (listSize > 0){
            SubmissionReviewList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            submissionReviewTypeBean = new SubmissionReviewTypeBean();
            submissionReviewDetailRow = (HashMap)result.elementAt(rowIndex);
            
            submissionReviewTypeBean.setSubmissionTypeCode(Integer.parseInt(
                           submissionReviewDetailRow.get("SUBMISSION_TYPE_CODE").toString()));
            submissionReviewTypeBean.setSubmissionTypeDescription((String)
                           submissionReviewDetailRow.get("SUBMISSION_TYPE"));
            submissionReviewTypeBean.setReviewTypeCode(Integer.parseInt(
                           submissionReviewDetailRow.get("PROTOCOL_REVIEW_TYPE_CODE").toString()));
            submissionReviewTypeBean.setReviewTypeDescription((String)
                           submissionReviewDetailRow.get("REVIEW_TYPE"));
            
            
            SubmissionReviewList.add(submissionReviewTypeBean);
            }
        }
        return SubmissionReviewList;
    }
     //Coeus enhancement Case #1791 - step 1: end
    
    /**
     *  Method used to get protocol Vote Abstainees list for a given protocol number
     *  sequence number and schedule id from OSP$PROTOCOL_VOTE_ABSTAINEES.
     *  <li>To fetch the data, it uses GET_PROTO_VOTE_ABSTAINEES procedure.
     *
     *  @param protocolNumber input to the procedure to get protocol vote abstainees
     *  @param seqNumber to get protocol vote abstainees.
     *  @param schedule Id to get protocol vote abstainees.
     *  @return Vector map of vote abstainees data is set of ProtocolVoteAbsFormBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */ 
    public Vector getProtocolVoteAbstainees(String protocolNumber,int seqNumber,
            String scheduleId) throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolVoteAbsFormBean protocolVoteAbsFormBean = null;
        HashMap voteAbstaineesRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                 DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT,new Integer(seqNumber).toString()));
        param.addElement(new Parameter("SCHEDULE_ID",
                                 DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROTO_VOTE_ABSTAINEES( <<PROTOCOL_NUMBER>>, <<SEQUENCE_NUMBER>>,"
            + "<<SCHEDULE_ID>>, <<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector voteAbstaineesList = null;
         if (listSize > 0){
            voteAbstaineesList = new Vector(3,2);
            for(int rowIndex=0; rowIndex<listSize; rowIndex++){
            protocolVoteAbsFormBean = new ProtocolVoteAbsFormBean();
            voteAbstaineesRow = (HashMap)result.elementAt(rowIndex);
            
            protocolVoteAbsFormBean.setProtocolNumber( (String)
                        voteAbstaineesRow.get("PROTOCOL_NUMBER"));
            protocolVoteAbsFormBean.setSequenceNumber(
                                Integer.parseInt(voteAbstaineesRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            protocolVoteAbsFormBean.setPersonId( (String)
                                 voteAbstaineesRow.get("PERSON_ID"));
            protocolVoteAbsFormBean.setPersonName( (String)
                                 voteAbstaineesRow.get("PERSON_NAME"));
            protocolVoteAbsFormBean.setScheduleId( (String)
                               voteAbstaineesRow.get("SCHEDULE_ID"));
            protocolVoteAbsFormBean.setComments( (String)
                               voteAbstaineesRow.get("COMMENTS"));
            protocolVoteAbsFormBean.setNonEmployeeFlag(voteAbstaineesRow.get(
                        "NON_EMPLOYEE_FLAG") == null ? false :
                          (voteAbstaineesRow.get("NON_EMPLOYEE_FLAG").toString()
                                    .equalsIgnoreCase("y") ? true :false));
            protocolVoteAbsFormBean.setUpdateTimestamp( 
                        (Timestamp)voteAbstaineesRow.get("UPDATE_TIMESTAMP"));
            protocolVoteAbsFormBean.setUpdateUser( (String)
                            voteAbstaineesRow.get("UPDATE_USER"));

            voteAbstaineesList.add(protocolVoteAbsFormBean);
            }
        }
        return voteAbstaineesList;
    }
    
    /**
     *  Method used to get protocol submission list for a given protocol number
     *  from OSP$PROTOCOL_SUBMISSION,OSP$PROTOCOL.
     *  <li>To fetch the data, it uses get_submissions_for_proto procedure.
     *
     *  @param protocolNumber to get protocol submission list for this id.
     *  @return Vector map of submission list data is set of ProtocolSubmissionInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProtocolSubmissionInfoBean getProtocolSubmissionDetails(
                    String protocolNumber) throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolSubmissionInfoBean submissionInfoBean = null;
        HashMap submissionDetailRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call get_submissions_for_proto( <<PROTOCOL_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            submissionDetailRow = (HashMap)result.elementAt(0);
            submissionInfoBean = new ProtocolSubmissionInfoBean();
            
            submissionInfoBean.setScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
            submissionInfoBean.setAWScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
            submissionInfoBean.setProtocolNumber( (String)
                        submissionDetailRow.get("PROTOCOL_NUMBER"));
            submissionInfoBean.setCommitteeId( (String)
                           submissionDetailRow.get("COMMITTEE_ID"));
            submissionInfoBean.setCommitteeName( (String)
                         submissionDetailRow.get("COMMITTEE_NAME"));
            submissionInfoBean.setSequenceNumber(
                                Integer.parseInt(submissionDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            /* inserted on 13-02-03 for maintaining old sequence number which
               will be used in where clause in the procedure */
            
            submissionInfoBean.setAWSequenceNumber(
                                Integer.parseInt(submissionDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            submissionInfoBean.setTitle( (String)
                                submissionDetailRow.get("TITLE"));
            submissionInfoBean.setScheduleDate(
                    submissionDetailRow.get("SCHEDULED_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                                "SCHEDULED_DATE")).getTime()) );
            submissionInfoBean.setSubmissionTypeCode(     
                        Integer.parseInt(submissionDetailRow.get(
                                    "SUBMISSION_TYPE_CODE").toString()));
            submissionInfoBean.setSubmissionTypeDesc( (String)
                    submissionDetailRow.get("SUBMISSION_TYPE_DESC"));
            submissionInfoBean.setSubmissionQualTypeCode(
                   Integer.parseInt(submissionDetailRow.get(
                   "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                 submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
            submissionInfoBean.setSubmissionQualTypeDesc(
                    (String)submissionDetailRow.get(
                                "SUBMISSION_TYPE_QUALIFIER_DESC"));
            submissionInfoBean.setProtocolReviewTypeCode(
                            Integer.parseInt(submissionDetailRow.get(
                                    "PROTOCOL_REVIEW_TYPE_CODE").toString()));
            submissionInfoBean.setProtocolReviewTypeDesc( (String)
                    submissionDetailRow.get("PROTOCOL_REVIEW_TYPE_DESC"));
            submissionInfoBean.setSubmissionStatusCode(    
                            Integer.parseInt(submissionDetailRow.get(
                                        "SUBMISSION_STATUS_CODE").toString()));
            submissionInfoBean.setSubmissionStatusDesc( (String)
                    submissionDetailRow.get("SUBMISSION_STATUS_DESC"));
            submissionInfoBean.setSubmissionDate(
                    submissionDetailRow.get("SUBMISSION_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                                "SUBMISSION_DATE")).getTime()) );
            submissionInfoBean.setComments( (String)
                            submissionDetailRow.get("COMMENTS"));
            submissionInfoBean.setYesVoteCount(                
                 Integer.parseInt(submissionDetailRow.get(
                 "YES_VOTE_COUNT") == null ? "0" :
                 submissionDetailRow.get("YES_VOTE_COUNT").toString()));
            submissionInfoBean.setNoVoteCount(    
                        Integer.parseInt(submissionDetailRow.get(
                        "NO_VOTE_COUNT")== null ? "0" :
                        submissionDetailRow.get("NO_VOTE_COUNT").toString()));
            submissionInfoBean.setAbstainerCount(
                        Integer.parseInt(submissionDetailRow.get(
                        "ABSTAINER_COUNT")== null ? "0" :
                        submissionDetailRow.get("ABSTAINER_COUNT").toString()));
            submissionInfoBean.setVotingComments( (String)
                            submissionDetailRow.get("VOTING_COMMENTS"));
                        
            submissionInfoBean.setUpdateTimestamp(
                        (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
            submissionInfoBean.setUpdateUser( (String)
                            submissionDetailRow.get("UPDATE_USER"));
            submissionInfoBean.setSubmissionNumber(submissionDetailRow.get("SUBMISSION_NUMBER")!=null ? Integer.parseInt(submissionDetailRow.get("SUBMISSION_NUMBER").toString()) : 0);            
            //get the reviewer details for the given protcolnumber and seqnumber
            /*Vector vecReviewer = getProtocolReviewers(
                                        submissionInfoBean.getProtocolNumber(),
                                        submissionInfoBean.getSequenceNumber());*/
            //Pass Submission Number as well to get Reviewers
            Vector vecReviewer = getProtocolReviewers(
                                        submissionInfoBean.getProtocolNumber(),
                                        submissionInfoBean.getSequenceNumber(),
                                        submissionInfoBean.getSubmissionNumber());            
            submissionInfoBean.setProtocolReviewer(vecReviewer);
           
            //Get Expidited Check list if present
            submissionInfoBean.setProtocolExemptCheckList(getProtocolExemptCheckList(
                                        submissionInfoBean.getProtocolNumber(),
                                        submissionInfoBean.getSequenceNumber(),
                                        submissionInfoBean.getSubmissionNumber()));
            //Get Exempt Check list if present                                        
            submissionInfoBean.setProtocolExpeditedCheckList(getProtocolExpeditedCheckList(
                                        submissionInfoBean.getProtocolNumber(),
                                        submissionInfoBean.getSequenceNumber(),
                                        submissionInfoBean.getSubmissionNumber()));
            
            //Get Review Comments - 23/10/2003 - start
            ScheduleMaintenanceTxnBean scheduleMaintenanceTxnBean = new ScheduleMaintenanceTxnBean();
            submissionInfoBean.setProtocolReviewComments(scheduleMaintenanceTxnBean.getMinutesForSubmission(
                submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()));
            //Get Review Comments - 23/10/2003 - end
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
            submissionInfoBean.setProtocolReviewAttachments(scheduleMaintenanceTxnBean.getAttachmentsForSubmission(
                    submissionInfoBean.getProtocolNumber(), submissionInfoBean.getSubmissionNumber()));
            // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
           
        }
        return submissionInfoBean;
    }
    
    /**
     *  Method used to get protocol submission list for a given protocol number
     *  from OSP$PROTOCOL_SUBMISSION,OSP$PROTOCOL.
     *  @param protocolNumber to get protocol submission list for this id.
     *  @return ProtocolSubmissionVoteFormBean
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProtocolSubmissionVoteFormBean getProtocolVotingDetails(
                    String protocolNumber) throws CoeusException,DBException{
           
           ProtocolSubmissionVoteFormBean protocolSubmissionVoteFormBean =
                        new ProtocolSubmissionVoteFormBean();
           ProtocolSubmissionInfoBean submissionInfoBean =  
                                getProtocolSubmissionDetails(protocolNumber);
            protocolSubmissionVoteFormBean.setProtocolNumber(submissionInfoBean.getProtocolNumber());
            protocolSubmissionVoteFormBean.setSequenceNumber(submissionInfoBean.getSequenceNumber());
            protocolSubmissionVoteFormBean.setScheduleId(submissionInfoBean.getScheduleId());
            protocolSubmissionVoteFormBean.setYesVoteCount(submissionInfoBean.getYesVoteCount());
            protocolSubmissionVoteFormBean.setNoVoteCount(submissionInfoBean.getNoVoteCount());
            protocolSubmissionVoteFormBean.setAbstainerCount(submissionInfoBean.getAbstainerCount());
            protocolSubmissionVoteFormBean.setVotingComments(submissionInfoBean.getVotingComments());
            protocolSubmissionVoteFormBean.setUpdateTimestamp(submissionInfoBean.getUpdateTimestamp());
            protocolSubmissionVoteFormBean.setUpdateUser(submissionInfoBean.getUpdateUser());
            //get the vote abstainees details for the given protocolnumber and seqnumber
            Vector vecVoteAbstainees = getProtocolVoteAbstainees(
                                        submissionInfoBean.getProtocolNumber(),
                                        submissionInfoBean.getSequenceNumber(),
                                        submissionInfoBean.getScheduleId());
            
            protocolSubmissionVoteFormBean.setProtocolVoteAbstainee(vecVoteAbstainees);
            
            return protocolSubmissionVoteFormBean;
    }
    
    /**
     *  Method used to get all committee schedules which are scheduled 
     *  from OSP$COMM_SCHEDULE  table for given committeeId.
     *  <li>To fetch the data, it uses get_schedule_list_for_comm procedure.
     *
     *  @param committeeId this is given as input parameter for the 
     *  procedure to execute.
     *  @return Vector map of all membership details data is set of 
     *  ScheduleDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getScheduleListForCommittee(String committeeId)
                                    throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap scheduleListRow = null;
        ScheduleDetailsBean scheduleDetailsBean = null;
        param.addElement(new Parameter("COMMITTEE_ID",
                            DBEngineConstants.TYPE_STRING,committeeId));
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call get_schedule_list_for_comm( <<COMMITTEE_ID>> , "
                            +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector scheduleList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            scheduleDetailsBean = new ScheduleDetailsBean();
            scheduleListRow = (HashMap)result.elementAt(rowIndex);
            
            scheduleDetailsBean.setScheduleId( (String)
                            scheduleListRow.get("SCHEDULE_ID"));
            scheduleDetailsBean.setCommitteeId( (String)
                            scheduleListRow.get("COMMITTEE_ID"));
            scheduleDetailsBean.setScheduleDate( 
                    scheduleListRow.get("SCHEDULED_DATE") == null ? null
                            :new Date( ((Timestamp) scheduleListRow.get(
                                "SCHEDULED_DATE")).getTime()) );
            scheduleDetailsBean.setScheduledPlace( (String)
                        scheduleListRow.get("PLACE"));
            scheduleDetailsBean.setScheduledTime(
                scheduleListRow.get("TIME") ==null ? null : new Time(
                        ((Timestamp) scheduleListRow.get("TIME")).getTime()) );
            scheduleDetailsBean.setProtocolSubDeadLine(
                scheduleListRow.get("PROTOCOL_SUB_DEADLINE") ==null ? 
                        null : new Date(((Timestamp) scheduleListRow.get(
                                "PROTOCOL_SUB_DEADLINE")).getTime()) );
            scheduleDetailsBean.setScheduleStatusCode(Integer.parseInt(
                      scheduleListRow.get("SCHEDULE_STATUS_CODE") == null ? "0"
                    : scheduleListRow.get("SCHEDULE_STATUS_CODE").toString()));
            scheduleDetailsBean.setScheduleStatusDesc( (String)
                                scheduleListRow.get("DESCRIPTION"));
            scheduleDetailsBean.setMaxProtocols(Integer.parseInt(
                            scheduleListRow.get("MAX_PROTOCOLS") == null ? "0"
                        : scheduleListRow.get("MAX_PROTOCOLS").toString()));
            scheduleDetailsBean.setUpdateTimestamp(
                            (Timestamp)scheduleListRow.get("UPDATE_TIMESTAMP"));
            scheduleDetailsBean.setUpdateUser( (String)
                                scheduleListRow.get("UPDATE_USER"));

            scheduleList.add(scheduleDetailsBean);
        }
        return scheduleList;
    }
    
    /**
     *  Method used to get active committee members from OSP$COMM_MEMBERSHIPS 
     *  for a given schedule id.
     *  <li>To fetch the data, it uses get_comm_members_for_schedule procedure.
     *
     *  @param scheduleId this is given as input parameter for the 
     *  procedure to execute.
     *  @return Vector map of all membership details data is set of 
     *  CommitteeMembershipDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeMembersForSchedule(String scheduleId)
                                        throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap memberListCurrentRow = null;
        CommitteeMembershipDetailsBean memberListCurrent = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                        DBEngineConstants.TYPE_STRING,scheduleId));
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call get_comm_members_for_schedule( <<SCHEDULE_ID>> , "
                        +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector memberList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            memberListCurrent = new CommitteeMembershipDetailsBean();
            memberListCurrentRow = (HashMap)result.elementAt(rowIndex);
            
            memberListCurrent.setCommitteeId( (String)
                        memberListCurrentRow.get("COMMITTEE_ID"));
            memberListCurrent.setPersonId( (String)
                            memberListCurrentRow.get("PERSON_ID"));
            memberListCurrent.setMembershipId( (String)
                        memberListCurrentRow.get("MEMBERSHIP_ID"));
            memberListCurrent.setSequenceNumber(Integer.parseInt(
             memberListCurrentRow.get("SEQUENCE_NUMBER") == null ? "0" 
                    :memberListCurrentRow.get("SEQUENCE_NUMBER").toString()));
            memberListCurrent.setPersonName((String)
                        memberListCurrentRow.get("PERSON_NAME"));
            memberListCurrent.setNonEmployeeFlag(memberListCurrentRow.get(
                   "NON_EMPLOYEE_FLAG").toString().charAt(0));
            memberListCurrent.setPaidMemberFlag(memberListCurrentRow.get(
                        "PAID_MEMBER_FLAG").toString().charAt(0));
            memberListCurrent.setTermStartDate(
            memberListCurrentRow.get("TERM_START_DATE") == null ? null 
                    : new Date(((Timestamp) memberListCurrentRow.get(
                                            "TERM_START_DATE")).getTime()));
            memberListCurrent.setTermEndDate(
              memberListCurrentRow.get("TERM_END_DATE") == null ? null 
                    : new Date( ((Timestamp) memberListCurrentRow.get(
                            "TERM_END_DATE")).getTime()) );
            memberListCurrent.setMembershipTypeCode(Integer.parseInt(
                memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE") == null ? "0"
                : memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberListCurrent.setMembershipTypeDesc((String)
                    memberListCurrentRow.get("MEMBERSHIP_TYPE_DESCRIPTION"));
            memberListCurrent.setStatusDescription((String)
                            memberListCurrentRow.get(
                                "MEMBERSHIP_STATUS_DESCRIPTION"));
            memberListCurrent.setComments((String)
                            memberListCurrentRow.get("COMMENTS"));
            memberListCurrent.setUpdateTimestamp(
                    (Timestamp)memberListCurrentRow.get("UPDATE_TIMESTAMP"));
            memberListCurrent.setUpdateUser((String)
                        memberListCurrentRow.get("UPDATE_USER"));

            memberList.add(memberListCurrent);
        }
        return memberList;
    }
      
    
    //prps start - jul 29 2003
   
      /**
     *  Method used to get active committee members from OSP$COMM_MEMBERSHIPS 
     *  for a given schedule id.
     *  <li>To fetch the data, it uses get_comm_members_for_schedule procedure.
     *
     *  @param scheduleId this is given as input parameter for the 
     *  procedure to execute.
     *  @return Vector map of all membership details data is set of 
     *  CommitteeMembershipDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public Vector getCommitteeMembersForCommittee(String committeeId, String protocolNumber)
                                        throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap memberListCurrentRow = null;
        CommitteeMembershipDetailsBean memberListCurrent = null;
        param.addElement(new Parameter("AW_COMMITTEE_ID",
                        DBEngineConstants.TYPE_STRING, committeeId));
        param.addElement(new Parameter("AW_PROTOCOL_NUMBER",
                        DBEngineConstants.TYPE_STRING, protocolNumber));
        
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call GET_COMM_COMMITTEE_REVIEWERS ( <<AW_COMMITTEE_ID>> , <<AW_PROTOCOL_NUMBER>>, "
                        +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector memberList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            memberListCurrent = new CommitteeMembershipDetailsBean();
            memberListCurrentRow = (HashMap)result.elementAt(rowIndex);
            
            memberListCurrent.setCommitteeId( (String)
                        memberListCurrentRow.get("COMMITTEE_ID"));
            memberListCurrent.setPersonId( (String)
                            memberListCurrentRow.get("PERSON_ID"));
            memberListCurrent.setMembershipId( (String)
                        memberListCurrentRow.get("MEMBERSHIP_ID"));
            memberListCurrent.setSequenceNumber(Integer.parseInt(
             memberListCurrentRow.get("SEQUENCE_NUMBER") == null ? "0" 
                    :memberListCurrentRow.get("SEQUENCE_NUMBER").toString()));
            memberListCurrent.setPersonName((String)
                        memberListCurrentRow.get("PERSON_NAME"));
            memberListCurrent.setNonEmployeeFlag(memberListCurrentRow.get(
                   "NON_EMPLOYEE_FLAG").toString().charAt(0));
            memberListCurrent.setPaidMemberFlag(memberListCurrentRow.get(
                        "PAID_MEMBER_FLAG").toString().charAt(0));
            memberListCurrent.setTermStartDate(
            memberListCurrentRow.get("TERM_START_DATE") == null ? null 
                    : new Date(((Timestamp) memberListCurrentRow.get(
                                            "TERM_START_DATE")).getTime()));
            memberListCurrent.setTermEndDate(
              memberListCurrentRow.get("TERM_END_DATE") == null ? null 
                    : new Date( ((Timestamp) memberListCurrentRow.get(
                            "TERM_END_DATE")).getTime()) );
            memberListCurrent.setMembershipTypeCode(Integer.parseInt(
                memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE") == null ? "0"
                : memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberListCurrent.setMembershipTypeDesc((String)
                    memberListCurrentRow.get("MEMBERSHIP_TYPE_DESCRIPTION"));
            memberListCurrent.setStatusDescription((String)
                            memberListCurrentRow.get(
                                "MEMBERSHIP_STATUS_DESCRIPTION"));
            memberListCurrent.setComments((String)
                            memberListCurrentRow.get("COMMENTS"));
            memberListCurrent.setUpdateTimestamp(
                    (Timestamp)memberListCurrentRow.get("UPDATE_TIMESTAMP"));
            memberListCurrent.setUpdateUser((String)
                        memberListCurrentRow.get("UPDATE_USER"));

            memberList.add(memberListCurrent);
        }
        return memberList;
    }
   
    // prps end
    
    /**
     *  This method compares research areas of a given protocol with the 
     *  research areas of given member. If all protocol research areas 
     *  are matching with member expertise (member research areas), 
     *  then the function returns 1 or else returns 0.
     *  <li>To fetch the data, it uses fn_check_reviewer_for_proto function.
     *
     *  @param membershipId this is given as input parameter for the 
     *  procedure to execute.
     *  @param protocolNumber to get reviewer for protocol number
     *  @return integer check it is the 0 for not matching 1 for matching.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int checkReviewerForProtocol(String membershipId,String protocolNumber)
                            throws  CoeusException, DBException{
        int check = 0;
        Vector param= new Vector();
        param.add(new Parameter("MEMBERSHIP_ID",
                            DBEngineConstants.TYPE_STRING,membershipId));
        param.add(new Parameter("PROTOCOL_NUMBER",
                            DBEngineConstants.TYPE_STRING,protocolNumber));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER CHECK>>=call fn_check_reviewer_for_proto( "
                    +"<< MEMBERSHIP_ID >> , << PROTOCOL_NUMBER >>)}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            check = Integer.parseInt(nextNumRow.get("CHECK").toString());
        }
        return check;
    }
    
    /**
     *  This method takes in SCHEDULE_ID and returns count of protocol 
     *  submitted to this schedule id.
     *  <li>To fetch the data, it uses fn_get_proto_submission_count function.
     *
     *  @param scheduleId this is given as input parameter for the 
     *  procedure to execute.
     *  @return integer count number protocol submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getNumberOfSubmittedProtocols(String scheduleId)
                            throws  CoeusException, DBException{
        int count = 0;
        Vector param= new Vector();
        param.add(new Parameter("SCHEDULE_ID",
                            DBEngineConstants.TYPE_STRING,scheduleId));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>>=call fn_get_proto_submission_count( "
                                        +"<< SCHEDULE_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("COUNT").toString());
        }
        return count;
    }
    
    /**
     *  This method takes in PROTOCOL_NUMBER and returns boolean true if the
     *  minutes available for protocol else false.
     *  <li>To fetch the data, it uses fn_check_minutes_for_proto function.
     *
     *  @param protocolNumber this is given as input parameter for the 
     *  procedure to execute.
     *  @return boolean for the protocol number submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean checkMinutesForProtocol(String protocolNumber)
                            throws  CoeusException, DBException{
        int count = 0;
        boolean check = false;
        Vector param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                            DBEngineConstants.TYPE_STRING,protocolNumber));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>>=call fn_check_minutes_for_proto( "
                                        +"<< PROTOCOL_NUMBER >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("COUNT").toString());
            if ( count == 1)
                check = true;
            else 
                check = false;
        }
        return check;
    }
    
    /**
     *  This method takes in PROTOCOL_NUMBER and returns the sequence number
     *  for regeneration of correspondence.
     *  <li>To fetch the data, it uses fn_get_curr_seq_for_corresp function.
     *
     *  @param protocolNumber this is given as input parameter for the 
     *  procedure to execute.
     *  @return int for the protocol number submitted.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getSeqNumberForRegeneration(String protocolNumber,int seqNumber,
                            int actionId)
                            throws  CoeusException, DBException{
        int seqNum = 0;
        Vector param= new Vector();
        param.add(new Parameter("PROTOCOL_NUMBER",
                            DBEngineConstants.TYPE_STRING,protocolNumber));
        param.add(new Parameter("SEQUENCE_NUMBER",
                            DBEngineConstants.TYPE_INT,""+seqNumber));
        param.add(new Parameter("ACTION_ID",
                            DBEngineConstants.TYPE_INT,""+actionId));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER SEQUENCE_NUMBER>>=call fn_get_curr_seq_for_corresp( "
                                        +"<< PROTOCOL_NUMBER >> ," +
                                        "<< SEQUENCE_NUMBER >> ," +
                                        "<< ACTION_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            seqNum = Integer.parseInt(nextNumRow.get("SEQUENCE_NUMBER").toString());
            if ( seqNum == 0)
                throw new CoeusException("protocolAction_SeqNum_exceptionCode.1001");
        }
        return seqNum;
    }
    
    
    //prps start jul15 2003
    /**
     *  This method takes in PROTOCOL_ID and returns MAX SUBMISSION NUMBER 
     
     *  <li>To fetch the data, it uses get_MAX_submission_number function.
     *
     *  @param protocolId this is given as input parameter for the 
     *  procedure to execute.
     *  @return integer count mac submission number.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public int getMaxSubmissionNumber(String protocolId)
                            throws  CoeusException, DBException{
        int count = 0;
        Vector param= new Vector();
        param.add(new Parameter("AW_PROTOCOL_ID",
                            DBEngineConstants.TYPE_STRING,protocolId));
        
        HashMap nextNumRow = null;
        Vector result = new Vector();
        if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{<<OUT INTEGER COUNT>>=call get_max_submission_num( "
                                        +"<< AW_PROTOCOL_ID >> )}", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            nextNumRow = (HashMap)result.elementAt(0);
            count = Integer.parseInt(nextNumRow.get("COUNT").toString());
        }
        return count;
    }
    
    //prps end

    /**
     * This method populates the Expedited check list box in the protocol submission
     * screen when Expedited is selected as Review Type.
     * <li>To fetch the data, it uses the procedure GET_EXPEDITED_REVIEW_CHECKLIST.
     *
     * @return Vector map of all expedited check list as ProtocolReviewTypeCheckListBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getExpeditedCheckList() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecExpedited=null;
        HashMap hasExpedited=null;
        Vector param= new Vector();
        CheckListBean checkListBean = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_EXPEDITED_REVIEW_CHECKLIST ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecExpedited=new Vector();
            for(int types=0;types<typesCount;types++){
                checkListBean = new CheckListBean();
                hasExpedited = (HashMap)result.elementAt(types);
                checkListBean.setCheckListCode(
                    (String)hasExpedited.get("EXPEDITED_REV_CHKLST_CODE"));
                checkListBean.setDescription(hasExpedited.get("DESCRIPTION").toString());
                vecExpedited.addElement(checkListBean);
            }
        }
        return vecExpedited;
    }    
    
    /**
     * This method populates the Expedited check list box in the protocol submission
     * screen when Expedited is selected as Review Type.
     * <li>To fetch the data, it uses the procedure GET_EXPEDITED_REVIEW_CHECKLIST.
     *
     * @return Vector map of all expedited check list as ProtocolReviewTypeCheckListBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getExemptCheckList() throws DBException{
        Vector result = new Vector(3,2);
        Vector vecExempt=null;
        HashMap hasExempt=null;
        Vector param= new Vector();
        CheckListBean checkListBean = null;
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_EXEMPT_STUDIES_CHECKLIST ( <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecExempt=new Vector();
            for(int types=0;types<typesCount;types++){
                checkListBean = new CheckListBean();
                hasExempt = (HashMap)result.elementAt(types);
                checkListBean.setCheckListCode(
                    (String)hasExempt.get("EXEMPT_STUDIES_CHECKLIST_CODE"));
                checkListBean.setDescription(hasExempt.get("DESCRIPTION").toString());
                vecExempt.addElement(checkListBean);
            }
        }
        return vecExempt;
    }        
    
    /**
     * This method populates the Expedited check list box in the protocol submission
     * screen when Expedited is selected as Review Type.
     * <li>To fetch the data, it uses the procedure GET_EXPEDITED_REVIEW_CHECKLIST.
     *
     * @return Vector map of all expedited check list as ProtocolReviewTypeCheckListBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolExpeditedCheckList(String protocolNumber, int sequenceNumber, int submissionNumber) throws DBException{
        Vector result = new Vector(3,2);
        Vector vecExpedited=null;
        HashMap hasExpedited=null;
        Vector param= new Vector();
        ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean = null;

        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                                    DBEngineConstants.TYPE_INT, ""+sequenceNumber));        
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                                    DBEngineConstants.TYPE_INT, ""+submissionNumber));                
        
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROTO_EXPIDITED_CHKLST ( <<PROTOCOL_NUMBER>>, <<SEQUENCE_NUMBER>>, <<SUBMISSION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new DBException("DB instance is not available");
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecExpedited=new Vector();
            for(int types=0;types<typesCount;types++){
                protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                hasExpedited = (HashMap)result.elementAt(types);
                protocolReviewTypeCheckListBean.setProtocolNumber(
                    (String) hasExpedited.get("PROTOCOL_NUMBER"));
                protocolReviewTypeCheckListBean.setSequenceNumber(
                    Integer.parseInt(hasExpedited.get("SEQUENCE_NUMBER").toString()));
                protocolReviewTypeCheckListBean.setSubmissionNumber(
                    Integer.parseInt(hasExpedited.get("SUBMISSION_NUMBER").toString()));                
                protocolReviewTypeCheckListBean.setCheckListCode(
                    (String)hasExpedited.get("EXPEDITED_REV_CHKLST_CODE"));
                protocolReviewTypeCheckListBean.setDescription(
                    (String) hasExpedited.get("DESCRIPTION"));
                protocolReviewTypeCheckListBean.setUpdateTimestamp(
                    (Timestamp)hasExpedited.get("UPDATE_TIMESTAMP"));
                protocolReviewTypeCheckListBean.setUpdateUser(
                    (String) hasExpedited.get("UPDATE_USER"));
                vecExpedited.addElement(protocolReviewTypeCheckListBean);
            }
        }
        return vecExpedited;
    }    
    
    /**
     * This method populates the Exempt check list box in the protocol submission
     * screen when Exempt is selected as Review Type.
     * <li>To fetch the data, it uses the procedure GET_PROTO_EXEMPT_CHKLST.
     *
     * @return Vector map of all expedited check list as ProtocolReviewTypeCheckListBean
     * @exception DBException if the instance of a dbEngine is null.
     */
    public Vector getProtocolExemptCheckList(String protocolNumber, int sequenceNumber, int submissionNumber) 
        throws DBException, CoeusException{
        Vector result = new Vector(3,2);
        Vector vecExempt=null;
        HashMap hasExempt=null;
        Vector param= new Vector();
        ProtocolReviewTypeCheckListBean protocolReviewTypeCheckListBean = null;

        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SEQUENCE_NUMBER",
                                    DBEngineConstants.TYPE_INT, ""+sequenceNumber));        
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                                    DBEngineConstants.TYPE_INT, ""+submissionNumber));                
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus",
            "call GET_PROTO_EXEMPT_CHKLST ( <<PROTOCOL_NUMBER>> , <<SEQUENCE_NUMBER>>, <<SUBMISSION_NUMBER>>, <<OUT RESULTSET rset>> )",
            "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000") ;
        }
        int typesCount = result.size();
        if (typesCount >0){
            vecExempt=new Vector();
            for(int types=0;types<typesCount;types++){
                protocolReviewTypeCheckListBean = new ProtocolReviewTypeCheckListBean();
                hasExempt = (HashMap)result.elementAt(types);
                protocolReviewTypeCheckListBean.setProtocolNumber(
                    (String) hasExempt.get("PROTOCOL_NUMBER"));
                protocolReviewTypeCheckListBean.setSequenceNumber(
                    Integer.parseInt(hasExempt.get("SEQUENCE_NUMBER").toString()));
                protocolReviewTypeCheckListBean.setSubmissionNumber(
                    Integer.parseInt(hasExempt.get("SUBMISSION_NUMBER").toString()));                
                protocolReviewTypeCheckListBean.setCheckListCode(
                    (String)hasExempt.get("EXEMPT_STUDIES_CHECKLIST_CODE"));
                protocolReviewTypeCheckListBean.setDescription(
                    (String) hasExempt.get("DESCRIPTION"));
                protocolReviewTypeCheckListBean.setUpdateTimestamp(
                    (Timestamp)hasExempt.get("UPDATE_TIMESTAMP"));
                protocolReviewTypeCheckListBean.setUpdateUser(
                    (String) hasExempt.get("UPDATE_USER"));
                vecExempt.addElement(protocolReviewTypeCheckListBean);
            }
        }
        return vecExempt;
    }        
    
    /**
     *  Method used to get active and valid committee members from OSP$COMM_MEMBERSHIPS 
     *  for a given schedule id and Protocol Number.
     *  <li>To fetch the data, it uses get_comm_members_for_schedule procedure.
     *
     *  @param scheduleId this is given as input parameter 
     *  @param protocolNumber this is given as input parameter 
     *  @return Vector map of all membership details data is set of 
     *  CommitteeMembershipDetailsBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
     public Vector getCommitteeReviewers(String scheduleId, String protocolNumber)
                                        throws CoeusException, DBException{
        Vector result = null;
        Vector param= new Vector();
        HashMap memberListCurrentRow = null;
        CommitteeMembershipDetailsBean memberListCurrent = null;
        param.addElement(new Parameter("SCHEDULE_ID",
                        DBEngineConstants.TYPE_STRING,scheduleId));
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                        DBEngineConstants.TYPE_STRING,protocolNumber));        
        if(dbEngine!=null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                "call GET_COMM_REVIEWERS ( <<SCHEDULE_ID>>, <<PROTOCOL_NUMBER>>, "
                        +" <<OUT RESULTSET rset>>)", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector memberList = new Vector(3,2);
        for(int rowIndex=0;rowIndex<listSize;rowIndex++){
            memberListCurrent = new CommitteeMembershipDetailsBean();
            memberListCurrentRow = (HashMap)result.elementAt(rowIndex);
            
            memberListCurrent.setCommitteeId( (String)
                        memberListCurrentRow.get("COMMITTEE_ID"));
            memberListCurrent.setPersonId( (String)
                            memberListCurrentRow.get("PERSON_ID"));
            memberListCurrent.setMembershipId( (String)
                        memberListCurrentRow.get("MEMBERSHIP_ID"));
            memberListCurrent.setSequenceNumber(Integer.parseInt(
             memberListCurrentRow.get("SEQUENCE_NUMBER") == null ? "0" 
                    :memberListCurrentRow.get("SEQUENCE_NUMBER").toString()));
            memberListCurrent.setPersonName((String)
                        memberListCurrentRow.get("PERSON_NAME"));
            memberListCurrent.setNonEmployeeFlag(memberListCurrentRow.get(
                   "NON_EMPLOYEE_FLAG").toString().charAt(0));
            memberListCurrent.setPaidMemberFlag(memberListCurrentRow.get(
                        "PAID_MEMBER_FLAG").toString().charAt(0));
            memberListCurrent.setTermStartDate(
            memberListCurrentRow.get("TERM_START_DATE") == null ? null 
                    : new Date(((Timestamp) memberListCurrentRow.get(
                                            "TERM_START_DATE")).getTime()));
            memberListCurrent.setTermEndDate(
              memberListCurrentRow.get("TERM_END_DATE") == null ? null 
                    : new Date( ((Timestamp) memberListCurrentRow.get(
                            "TERM_END_DATE")).getTime()) );
            memberListCurrent.setMembershipTypeCode(Integer.parseInt(
                memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE") == null ? "0"
                : memberListCurrentRow.get("MEMBERSHIP_TYPE_CODE").toString()));
            memberListCurrent.setMembershipTypeDesc((String)
                    memberListCurrentRow.get("MEMBERSHIP_TYPE_DESCRIPTION"));
            memberListCurrent.setStatusDescription((String)
                            memberListCurrentRow.get(
                                "MEMBERSHIP_STATUS_DESCRIPTION"));
            memberListCurrent.setComments((String)
                            memberListCurrentRow.get("COMMENTS"));
            memberListCurrent.setUpdateTimestamp(
                    (Timestamp)memberListCurrentRow.get("UPDATE_TIMESTAMP"));
            memberListCurrent.setUpdateUser((String)
                        memberListCurrentRow.get("UPDATE_USER"));

            memberList.add(memberListCurrent);
        }
        return memberList;
    }
    
    
     
    /**
     *  Method used to get protocol submission list for a given protocol number
     *  from OSP$PROTOCOL_SUBMISSION,OSP$PROTOCOL.
     *  <li>To fetch the data, it uses GET_SUBMISSION_FOR_SUB_NUM procedure.
     *
     *  @param protocolNumber to get protocol submission list for this id.
     *  @return Vector map of submission list data is set of ProtocolSubmissionInfoBean.
     *  @exception DBException if any error during database transaction. 
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProtocolSubmissionInfoBean getSubmissionForSubmissionNumber(
                    String protocolNumber, int submissionNumber) throws CoeusException,DBException{
        Vector result = null;
        Vector param= new Vector();
        ProtocolSubmissionInfoBean submissionInfoBean = null;
        HashMap submissionDetailRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                                    DBEngineConstants.TYPE_STRING,protocolNumber));
        param.addElement(new Parameter("SUBMISSION_NUMBER",
                                    DBEngineConstants.TYPE_INT, ""+submissionNumber));        
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
            "call GET_SUBMISSION_FOR_SUB_NUM( <<PROTOCOL_NUMBER>> , <<SUBMISSION_NUMBER>> , "+
                            "<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(!result.isEmpty()){
            submissionDetailRow = (HashMap)result.elementAt(0);
            submissionInfoBean = new ProtocolSubmissionInfoBean();

            submissionInfoBean.setScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
            submissionInfoBean.setAWScheduleId( (String)
                            submissionDetailRow.get("SCHEDULE_ID"));
            submissionInfoBean.setScheduleDate(
                    submissionDetailRow.get("SCHEDULED_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                                "SCHEDULED_DATE")).getTime()) );                        
            submissionInfoBean.setProtocolNumber( (String)
                        submissionDetailRow.get("PROTOCOL_NUMBER"));
            submissionInfoBean.setCommitteeId( (String)
                           submissionDetailRow.get("COMMITTEE_ID"));
            submissionInfoBean.setSequenceNumber(
                                Integer.parseInt(submissionDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            submissionInfoBean.setAWSequenceNumber(
                                Integer.parseInt(submissionDetailRow.get(
                                            "SEQUENCE_NUMBER").toString()));
            submissionInfoBean.setSubmissionTypeCode(     
                        Integer.parseInt(submissionDetailRow.get(
                                    "SUBMISSION_TYPE_CODE").toString()));
            submissionInfoBean.setSubmissionTypeDesc( (String)
                    submissionDetailRow.get("SUBMISSION_TYPE_DESC"));            
            submissionInfoBean.setSubmissionQualTypeCode(
                   Integer.parseInt(submissionDetailRow.get(
                   "SUBMISSION_TYPE_QUAL_CODE") == null ? "0" :
                 submissionDetailRow.get("SUBMISSION_TYPE_QUAL_CODE").toString()));
            submissionInfoBean.setSubmissionQualTypeDesc(
                    (String)submissionDetailRow.get(
                                "SUBMISSION_TYPE_QUALIFIER_DESC"));            
            submissionInfoBean.setProtocolReviewTypeCode(
                            Integer.parseInt(submissionDetailRow.get(
                                    "PROTOCOL_REVIEW_TYPE_CODE").toString()));
            submissionInfoBean.setProtocolReviewTypeDesc( (String)
                    submissionDetailRow.get("PROTOCOL_REVIEW_TYPE_DESC"));            
            submissionInfoBean.setSubmissionStatusCode(    
                            Integer.parseInt(submissionDetailRow.get(
                                        "SUBMISSION_STATUS_CODE").toString()));
            submissionInfoBean.setSubmissionStatusDesc( (String)
                    submissionDetailRow.get("SUBMISSION_STATUS_DESC"));            
            submissionInfoBean.setSubmissionDate(
                    submissionDetailRow.get("SUBMISSION_DATE") == null ? null
                            :new Date( ((Timestamp) submissionDetailRow.get(
                                "SUBMISSION_DATE")).getTime()) );
            submissionInfoBean.setComments( (String)
                            submissionDetailRow.get("COMMENTS"));
            submissionInfoBean.setYesVoteCount(                
                 Integer.parseInt(submissionDetailRow.get(
                 "YES_VOTE_COUNT") == null ? "0" :
                 submissionDetailRow.get("YES_VOTE_COUNT").toString()));
            submissionInfoBean.setNoVoteCount(    
                        Integer.parseInt(submissionDetailRow.get(
                        "NO_VOTE_COUNT")== null ? "0" :
                        submissionDetailRow.get("NO_VOTE_COUNT").toString()));
            submissionInfoBean.setUpdateTimestamp(
                        (Timestamp)submissionDetailRow.get("UPDATE_TIMESTAMP"));
            submissionInfoBean.setUpdateUser( (String)
                            submissionDetailRow.get("UPDATE_USER"));
            submissionInfoBean.setSubmissionNumber(submissionDetailRow.get("SUBMISSION_NUMBER")!=null ? Integer.parseInt(submissionDetailRow.get("SUBMISSION_NUMBER").toString()) : 0);            
           
            //following added by ele on 9-10-04 for printing action information
            submissionInfoBean.setActionId(submissionDetailRow.get("ACTION_ID")!=null ? Integer.parseInt(submissionDetailRow.get("ACTION_ID").toString()) : 0);
            submissionInfoBean.setActionDate(new Date(((Timestamp) submissionDetailRow.get("ACTION_DATE")).getTime()) );
            submissionInfoBean.setActionTypeCode(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE")!=null ? Integer.parseInt(submissionDetailRow.get("PROTOCOL_ACTION_TYPE_CODE").toString()) : 0);
            submissionInfoBean.setActionTypeDesc(submissionDetailRow.get("ACTION_TYPE_DESC").toString());
            submissionInfoBean.setActionComments(submissionDetailRow.get("ACTION_COMMENTS") != null ?
                                                 submissionDetailRow.get("ACTION_COMMENTS").toString() : null);
   
        }
        return submissionInfoBean;
    }     
    
    /**
     * This method is used to check whether the given set of Expedited Approved Protocols can be assigned to Schedule.
     * 
     * <li>To fetch the data, it uses the procedure FN_CAN_ASSIGN_TO_SCHEDULE.
     *
     * @return HashMap of all Submission Info.
     * @exception DBException if the instance of a dbEngine is null.
     */
    public HashMap canAssignToSchedule(HashMap submissionInfo) 
        throws DBException, CoeusException{
        Vector param = null;
        ProtocolSubmissionInfoBean protocolSubmissionInfoBean = null;
        Vector vctSubmissionInfo = null;
        
        java.util.Iterator iterator =  submissionInfo.values().iterator();
        int check = 0;
        boolean blnReturn = true;
        HashMap dataToSend = new HashMap();
        CoeusMessageResourcesBean coeusMessageResourcesBean = new CoeusMessageResourcesBean();
        String errMsg = "";
        while(iterator.hasNext()){            
            check = 0;
            protocolSubmissionInfoBean = (ProtocolSubmissionInfoBean)iterator.next();
            param = new Vector();
            param.add(new Parameter("PROTOCOL_NUMBER",
                                DBEngineConstants.TYPE_STRING, protocolSubmissionInfoBean.getProtocolNumber()));
            param.add(new Parameter("SEQUENCE_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+protocolSubmissionInfoBean.getSequenceNumber()));
            param.add(new Parameter("SUBMISSION_NUMBER",
                                DBEngineConstants.TYPE_INT, ""+protocolSubmissionInfoBean.getSubmissionNumber()
                                ));

            HashMap nextNumRow = null;
            Vector result = new Vector();
            if(dbEngine!=null){
                result = dbEngine.executeFunctions("Coeus",
                "{<<OUT INTEGER CHECK>>=call FN_CAN_ASSIGN_TO_SCHEDULE( "
                        +"<< PROTOCOL_NUMBER >> , << SEQUENCE_NUMBER >>, <<SUBMISSION_NUMBER>>)}", param);
            }else{
                throw new CoeusException("db_exceptionCode.1000");
            }
            if(!result.isEmpty()){
                nextNumRow = (HashMap)result.elementAt(0);
                check = Integer.parseInt(nextNumRow.get("CHECK").toString());
            }
            if(check!=1){
                errMsg = "Protocol '"+protocolSubmissionInfoBean.getProtocolNumber() +"' cannot be Assigned to Schedule : \n";
                errMsg = errMsg + coeusMessageResourcesBean.parseMessageKey(
                            "protocolAction_exceptionCode.2020");
                
                //protocolSubmissionInfoBean = getSubmissionForSubmissionNumber(protocolSubmissionInfoBean.getProtocolNumber(),protocolSubmissionInfoBean.getSubmissionNumber());                
                dataToSend.put(protocolSubmissionInfoBean.getProtocolNumber()+protocolSubmissionInfoBean.getSubmissionNumber(),errMsg);
            }else{
                protocolSubmissionInfoBean = getSubmissionForSubmissionNumber(protocolSubmissionInfoBean.getProtocolNumber(),protocolSubmissionInfoBean.getSubmissionNumber());
                dataToSend.put(protocolSubmissionInfoBean.getProtocolNumber()+protocolSubmissionInfoBean.getSubmissionNumber(),protocolSubmissionInfoBean);
            }
        }
        return dataToSend;
    }    
    
    //Added for case#3046 - Notify IRB attachments - start    
    /**
     * This method gets the blob data of the submission document
     * @param String protocolNumber
     * @param String sequenceNumber
     * @param String submissionNumber
     * @param String documentId
     * @return byte[] fileData
     * @throws Exception CoeusException
     * @throws Exception DBException
     */
    public synchronized byte[] getSubmissionDocument(String protocolNumber, String sequenceNumber,
            String submissionNumber, String documentId) throws CoeusException, DBException{
        
        Vector parameter = new Vector();
        Vector result = null;
        HashMap resultRow = null;
        byte[] fileData = null;
        parameter.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolNumber));
        parameter.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+Integer.parseInt(sequenceNumber)));
        parameter.addElement(new Parameter("SUBMISSION_NUMBER",
                DBEngineConstants.TYPE_INT, ""+Integer.parseInt(submissionNumber)));
        parameter.addElement(new Parameter("DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, ""+Integer.parseInt(documentId)));
        
        String selectQuery = "SELECT DOCUMENT FROM OSP$PROTOCOL_SUBMISSION_DOC " +
                "WHERE PROTOCOL_NUMBER =  <<PROTOCOL_NUMBER>> " +
                " AND SEQUENCE_NUMBER =  <<SEQUENCE_NUMBER>> " +
                " AND SUBMISSION_NUMBER =  <<SUBMISSION_NUMBER>> " +
                " AND DOCUMENT_ID =  <<DOCUMENT_ID>> ";
                
        if(dbEngine!=null){
            result = dbEngine.executeRequest("Coeus", selectQuery, "Coeus", parameter);            
            if(!result.isEmpty()){
                resultRow = (HashMap)result.get(0);
                java.io.ByteArrayOutputStream documentBytes =
                        (java.io.ByteArrayOutputStream)resultRow.get("DOCUMENT");
                fileData = documentBytes.toByteArray();
            }
        }else {
            throw new CoeusException("db_exceptionCode.1000");
        }
        return fileData;
    }
    
    /**
     * Method used to update/insert/delete Protocol Action Document
     * @param protocolActionDocumentBean ProtocolActionDocumentBean
     * @return isSuccess boolean to indicate whether data is saved or not
     * @exception DBException if any error during database transaction.
     * @exception CoeusException if the instance of dbEngine is not available.
     */
    public boolean addUpdProtocolActionDocument(ProtocolActionDocumentBean protocolActionDocumentBean)
    throws CoeusException, DBException{
        boolean isSuccess = false;
        java.util.List lstData = new Vector();
        ProcReqParameter procReqParameter
                = addUpdProtoActionDocument(protocolActionDocumentBean);
        if(procReqParameter != null){
            lstData.add(procReqParameter);
        }
        if(dbEngine!=null) {
            java.sql.Connection conn = null;
            conn = dbEngine.beginTxn();
            if(lstData != null && lstData.size() > 0){
                dbEngine.batchSQLUpdate((Vector)lstData,conn);
            }
            dbEngine.commit(conn);            
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        isSuccess = true;
        return isSuccess;
    }    
    
    /**
     *  Method used to update/insert/delete Protocol Action Document
     *  @param protocolActionDocumentBean ProtocolActionDocumentBean
     *  @return ProcReqParameter ProcReqParameter
     *  @exception DBException if any error during database transaction.
     *  @exception CoeusException if the instance of dbEngine is not available.
     */
    public ProcReqParameter addUpdProtoActionDocument(ProtocolActionDocumentBean protocolActionDocumentBean)
    throws CoeusException, DBException{
        
        Vector parameter = new Vector();
        CoeusFunctions coeusFunctions = new CoeusFunctions();
        dbTimestamp = coeusFunctions.getDBTimestamp();        
        parameter.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING, protocolActionDocumentBean.getProtocolNumber()));
        parameter.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INT, ""+protocolActionDocumentBean.getSequenceNumber()));
        parameter.addElement(new Parameter("SUBMISSION_NUMBER",
            DBEngineConstants.TYPE_INT, ""+protocolActionDocumentBean.getSubmissionNumber()));      
        //Commented for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
//        int documentId = getNextDocumentID(protocolActionDocumentBean.getProtocolNumber(),
//                protocolActionDocumentBean.getSequenceNumber(),
//                protocolActionDocumentBean.getSubmissionNumber());
//        protocolActionDocumentBean.setDocumentId(documentId);                
        //COEUSDEV-328 : End
        parameter.addElement(new Parameter("DOCUMENT_ID",
                DBEngineConstants.TYPE_INT, ""+protocolActionDocumentBean.getDocumentId()));
        parameter.addElement(new Parameter("FILE_NAME",
                DBEngineConstants.TYPE_STRING, protocolActionDocumentBean.getFileName()));
        //Added with case 4007: Icon based on mime Type
        parameter.addElement(new Parameter("MIME_TYPE",
                DBEngineConstants.TYPE_STRING, protocolActionDocumentBean.getMimeType()));
        parameter.addElement(new Parameter("DOCUMENT",
                DBEngineConstants.TYPE_BLOB, protocolActionDocumentBean.getFileBytes()));
        //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
        parameter.addElement(new Parameter("DESCRIPTION",
                DBEngineConstants.TYPE_STRING, protocolActionDocumentBean.getDescription()));
        //COEUSDEV-328 : End
        parameter.addElement(new Parameter("UPDATE_TIMESTAMP",
                DBEngineConstants.TYPE_TIMESTAMP, dbTimestamp));
        parameter.addElement(new Parameter("UPDATE_USER",
                DBEngineConstants.TYPE_STRING, protocolActionDocumentBean.getUpdateUser()));
        
        StringBuffer sqlProtocolActionDocument = new StringBuffer("");
        if(protocolActionDocumentBean.getAcType().trim().equals("I")){            
            sqlProtocolActionDocument.append("INSERT INTO OSP$PROTOCOL_SUBMISSION_DOC(");
            sqlProtocolActionDocument.append(" PROTOCOL_NUMBER , ");
            sqlProtocolActionDocument.append(" SEQUENCE_NUMBER , ");
            sqlProtocolActionDocument.append(" SUBMISSION_NUMBER , ");
            sqlProtocolActionDocument.append(" DOCUMENT_ID , ");
            sqlProtocolActionDocument.append(" FILE_NAME , ");
            sqlProtocolActionDocument.append(" MIME_TYPE , ");//4007
            sqlProtocolActionDocument.append(" DOCUMENT , ");
            sqlProtocolActionDocument.append(" DESCRIPTION , ");//COEUSDEV-328 
            sqlProtocolActionDocument.append(" UPDATE_TIMESTAMP , ");
            sqlProtocolActionDocument.append(" UPDATE_USER ) ");
            sqlProtocolActionDocument.append(" VALUES (");
            sqlProtocolActionDocument.append(" <<PROTOCOL_NUMBER>> , ");
            sqlProtocolActionDocument.append(" <<SEQUENCE_NUMBER>> , ");
            sqlProtocolActionDocument.append(" <<SUBMISSION_NUMBER>> , ");
            sqlProtocolActionDocument.append(" <<DOCUMENT_ID>> , ");
            sqlProtocolActionDocument.append(" <<FILE_NAME>> , ");
            sqlProtocolActionDocument.append(" <<MIME_TYPE>> , ");//4007
            sqlProtocolActionDocument.append(" <<DOCUMENT>> , ");
            sqlProtocolActionDocument.append(" <<DESCRIPTION>> , ");//COEUSDEV-328 
            sqlProtocolActionDocument.append(" <<UPDATE_TIMESTAMP>> , ");
            sqlProtocolActionDocument.append(" <<UPDATE_USER>> ) ");
        }
        ProcReqParameter procReqParameter  = new ProcReqParameter();
        procReqParameter.setDSN(DSN);
        procReqParameter.setParameterInfo(parameter);
        procReqParameter.setSqlCommand(sqlProtocolActionDocument.toString());
        return procReqParameter;
    }    
    
    /**
     * This method creates next Document Id.
     * Note : Since only one document is allowed, it always returns 1
     * @param protocolNumber String
     * @param sequenceNumber String
     * @param submissionNumber String
     * @return documentId int
     * @exception DBException if the instance of a dbEngine is null
     */
    public int getNextDocumentID(String protocolNumber, int sequenceNumber, int submissionNumber) throws  DBException{
        int documentId = 1;        
        return documentId;
    }        
    //Added for case#3046 - Notify IRB attachments - end
    
    /*public static void main(String args[]) {
        try{
            HashMap canAssignToSchedule = new HashMap();
            HashMap dataAfterValidation = new HashMap();
            ProtocolSubmissionInfoBean protocolSubmissionInfoBean = new ProtocolSubmissionInfoBean();
            protocolSubmissionInfoBean.setProtocolNumber("0310002122");
            protocolSubmissionInfoBean.setSubmissionNumber(1);
            protocolSubmissionInfoBean.setSequenceNumber(1);
            
            canAssignToSchedule.put("0310002122"+1,protocolSubmissionInfoBean);
            ProtocolSubmissionTxnBean protocolSubmissionTxnBean = new ProtocolSubmissionTxnBean();
            dataAfterValidation = protocolSubmissionTxnBean.canAssignToSchedule(canAssignToSchedule);
            System.out.println("Value got : "+dataAfterValidation.get("0310002122"+1));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }*/
    // 3282: Reviewer view of Protocols - Start
    public Vector getRecommendedActionsForReviewer() throws DBException, CoeusException{
        Vector vecActions = new Vector();
        //Commented for COEUSQA-2288 : Ability for Reviewer to mark Review Comments complete and make recommendation - Start
        //Empty comb box element is specific to premium, not needed for lite(Provide 'Please Select' in UI)
//        vecActions.addElement(new ComboBoxBean("",""));
        //COEUSQA-2288 : end
        Vector result = null;
        Vector param= new Vector();
        HashMap hmAction = null;
        if(dbEngine != null){
            result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_REVIEWER_RECOMMEND_ACTIONS( <<OUT RESULTSET rset>> )",
                    "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }        
        if (result != null && result.size() > 0){    
            int actionCount = result.size();
            for(int rowIndex=0; rowIndex<actionCount; rowIndex++){
                hmAction = (HashMap)result.elementAt(rowIndex);
                vecActions.addElement(new ComboBoxBean(hmAction.get(
                        "PROTOCOL_ACTION_TYPE_CODE").toString(),
                        hmAction.get("DESCRIPTION").toString()));
            }
        }
        return vecActions;
    }
    // 3282: Reviewer view of Protocols - End
    
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    /*
     * Method to get the submission type code based on the action code
     * @param - actionCode
     * @return - submissionTypeCode
     */
    public int getSubmissionTypeCodeForAction(int actionCode){
        int submissionTypeCode = 0;
        if(actionCode == REQUEST_TO_CLOSE_ACTION){
            submissionTypeCode = REQUEST_TO_CLOSE_SUBMISSION;
        }else if(actionCode == REQUEST_FOR_SUSPENSION_ACTION){
            submissionTypeCode = REQUEST_FOR_SUSPENSION_SUBMISSION;
        }else if(actionCode == REQUEST_TO_CLOSE_ENROLLMENT_ACTION){
            submissionTypeCode = REQUEST_TO_CLOSE_ENROLLMENT_SUBMISSION;
        }else if(actionCode == REQUEST_FOR_DATA_ANALYSIS_ONLY_ACTION){
            submissionTypeCode = REQUEST_FOR_DATA_ANALYSIS_ONLY_SUBMISSION;
        }else if(actionCode == REQUEST_FOR_RE_OPEN_ENROLLMENT_ACTION){
            submissionTypeCode = REQUEST_FOR_RE_OPEN_ENROLLMENT_SUBMISSION;
        }else if(NOTIFY_IRB == NOTIFY_IRB){
            submissionTypeCode = FYI;
        } 
        return submissionTypeCode ;
    }
    //COEUSDEV-86 : END
    //code added to get protocol persons
     public Vector getNotificationPersonsDetails(String proposalNumber)throws CoeusException, DBException{
        Vector protocolPersonsVector= new Vector();
        Vector result = null;
        Vector param= new Vector();
        Vector personid=new Vector();
        HashMap hmRow = null;
        param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
                
        if(dbEngine !=null){
            //result = new Vector(3,2);
            result = dbEngine.executeRequest("Coeus",
                    "call GET_IRB_INV_KPS( <<PROTOCOL_NUMBER>> ,<<OUT RESULTSET rset>> )", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int protoPersonsCertifyCount = result.size();
        if(protoPersonsCertifyCount>0){
            protocolPersonsVector=new Vector();
            for(int rowIndex=0;rowIndex<protoPersonsCertifyCount;rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                Vector data= new Vector();
                data.add(new Boolean( false ));
                data.add((String)hmRow.get("PERSON_NAME"));
                data.add((Timestamp)hmRow.get("LAST_NOTIFICATION_DATE"));
                personid.add((String)hmRow.get("PERSON_ID"));
                protocolPersonsVector.add(data);

            }
            protocolPersonsVector.add(personid);
        }

     return protocolPersonsVector;
    }
     //code added to get protocol details to send coi notification
public HashMap getProtoPersonDetailsForMail(String proposalNumber)throws CoeusException, DBException{
        Vector result = null;
        HashMap hmRow = null;
        Vector param= new Vector();
               param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
              
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_IRB_PROTO_DETS_SND_NOT( <<PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        if(result!=null){
            hmRow=(HashMap)result.get(0);
        }
            return hmRow;
    }


public Vector getPropPersonDetailsForNotif(String proposalNumber)throws CoeusException, DBException{
    Vector result = null;
    Vector param= new Vector();
               param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
              
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PROTOCOL_PERSON_DETAILS( <<PROTOCOL_NUMBER>> , <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
       return result;
}

public Vector getProtoPersonDetailsForMail(String proposalNumber,String seqno)throws CoeusException, DBException{
        Vector result = null;
        HashMap hmRow = null;
        Vector param= new Vector();
               param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,proposalNumber));
               param.addElement(new Parameter("SEQUENCE_NUMBER",
                DBEngineConstants.TYPE_INTEGER,Integer.parseInt(seqno)));
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_IRB_PROTO_NOTIFIC_DETAILS( <<PROTOCOL_NUMBER>> ,<<SEQUENCE_NUMBER>>, <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        Vector protocolPersonsVector=new Vector();
        if (listSize > 0){
              for(int rowIndex=0;rowIndex<listSize;rowIndex++){
                hmRow = (HashMap)result.elementAt(rowIndex);
                Vector data= new Vector();

                data.add((String)hmRow.get("TITLE"));
                data.add((String)hmRow.get("PERSON_ID"));
                data.add((String)hmRow.get("UNIT_NUMBER"));
                data.add((String)hmRow.get("UNIT_NAME"));
                data.add((String)hmRow.get("LEAD_UNIT_FLAG"));
                data.add((String)hmRow.get("PERSON_ROLE"));
                data.add((Timestamp)hmRow.get("APPLICATION_DATE"));
                data.add((Timestamp)hmRow.get("APPROVAL_DATE"));
                data.add((String)hmRow.get("PROTOCOL_TYPE"));
                data.add((String)hmRow.get("FULL_NAME"));
                protocolPersonsVector.add(data);
            }
        }
                return protocolPersonsVector;
    }
//code added to get email id.
 
 public String getEmailIdForPerson(String personId)throws CoeusException, DBException{
        String emailId=null;
        Vector result = null;
        Vector param= new Vector();
        HashMap protocolDevRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
        if(dbEngine !=null){
            result = dbEngine.executeRequest("Coeus","call GET_PERSON_EMAIL_ID( <<PERSON_ID>>, <<OUT RESULTSET rset>>) ", "Coeus", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
        int listSize = result.size();
        if (listSize > 0){
            protocolDevRow = (HashMap)result.elementAt(0);
            emailId=(String) protocolDevRow.get("EMAIL_ADDRESS");
        }
        return emailId;
    }
 //code added to update last notification date
   public Integer updateLastNotificationDate(String personId,String protocolnumber,String selModule)throws CoeusException, DBException{
        Integer updatedResult=null;
        Vector result = null;
        Vector param= new Vector();
        String module=selModule.toString();
        HashMap protocolDevRow = null;
        param.addElement(new Parameter("PERSON_ID",
                DBEngineConstants.TYPE_STRING,personId));
         param.addElement(new Parameter("PROTOCOL_NUMBER",
                DBEngineConstants.TYPE_STRING,protocolnumber));
          param.addElement(new Parameter("module",
                DBEngineConstants.TYPE_STRING,module));

           if(dbEngine!=null){
            result = dbEngine.executeFunctions("Coeus",
            "{ <<OUT INTEGER COUNT>> = "
            +" call FN_UPD_LAST_NOTIFIC_IRB_DATE(<< PERSON_ID >>,<<PROTOCOL_NUMBER>>, <<module>> ) }", param);
        }else{
            throw new CoeusException("db_exceptionCode.1000");
        }
            if(!result.isEmpty()){
            HashMap rowParameter = (HashMap)result.elementAt(0);
            updatedResult = Integer.parseInt(rowParameter.get("COUNT").toString());
        }
        return updatedResult;
    }
}
