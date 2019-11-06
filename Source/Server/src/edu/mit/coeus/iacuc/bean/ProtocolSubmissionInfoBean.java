/*
 * @(#)ProtocolSubmissionInfoBean.java 1.0 11/18/02 2:24 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables on 12-APRIL-2011
 * by Divya Susendran
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.sql.Date;
import java.io.Serializable;
import java.util.Vector;

/**
 * The class used to hold the information of <code>Protocol Submission</code>
 *
 * @author  Mukundan C
 * @version 1.0
 * Created on November 18, 2002, 2:24 PM
 */

public class ProtocolSubmissionInfoBean implements Serializable {
    // holds the submission type code
    private static final String PROP_SCHEDULE_ID = "scheduleId";
    // holds the submission type code
    private static final String PROP_SUBMISSION_TYPE_CODE = "submissionTypeCode";
    // holds the submission Qualifiers type code
    private static final 
            String PROP_SUBMISSION_QUAL_TYPE_CODE = "submissionQualTypeCode";
    // holds the protocol Review type code
    private static final 
            String PROP_PROTOCOL_REV_TYPE_CODE = "protocolReviewTypeCode";
    // holds the submissions Status code
    private static final 
            String PROP_SUBMISSION_STATUS_CODE = "submissionStatusCode";
    // holds the submissions Date
    private static final String PROP_SUBMISSION_DATE = "submissionDate";
    // holds the comments
    private static final String PROP_COMMENTS = "comments";
    // holds the yes vote count
    private static final String PROP_YES_VOTE = "yesVoteCount";
    // holds the no vote count
    private static final String PROP_NO_VOTE = "noVoteCount";
    
    // holds the protocol number
    private String protocolNumber;
    // holds the sequence number
    private int sequenceNumber;
    // holds the version number
    private int versionNumber;
    // holds the sequence number
    private int awSequenceNumber;
   
    // holds the schedule id
    private String scheduleId;
    // holds the schedule id
    private String awScheduleId;
    // holds the committee id
    private String committeeId;
    // holds the committee name
    private String committeeName;
    // holds the title
    private String title;
    // holds the submission type code
    private int submissionTypeCode;
    // holds the submission type description
    private String submissionTypeDesc;
    // holds the submission qualifier type code
    private int submissionQualTypeCode;
    // holds the submission qualifier type description
    private String submissionQualTypeDesc;
    // holds the protocol review type code
    private int protocolReviewTypeCode;
    // holds the protocol review type description
    private String protocolReviewTypeDesc;
    // holds the submission status code
    private int submissionStatusCode;
    // holds the submission status description
    private String submissionStatusDesc;
    // holds the submission date
    private Date submissionDate;
    // holds the schedule date
    private Date scheduleDate;
    // holds the comments
    private String comments;
    // holds the yes vote count
    private int yesVoteCount;
    // holds the no vote count
    private int noVoteCount;
    //holds protocol areaofresearch bean
    private Vector protocolReviewer;
     //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds Action type
    private String acType;
    //holds Protocol Expedited checklist
    private Vector protocolExpeditedCheckList;
    //holds Protocol Exempt checklist
    private Vector protocolExemptCheckList;    
    // holds the Application date
    private Date applicationDate;
    // holds PIName
    private String PIName;
    //holds Schedule Place
    private String schedulePlace;
    //holds Review Comments 
    private Vector protocolReviewComments;  
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    private Vector protocolReviewAttachments;
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
    //following action info added by ele 9-10-04
    //holds action type code
    private int actionTypeCode;
    //holds action type desc
    private String actionTypeDesc;
    //holds action date
    private Date actionDate;
    //holds action Comments
    private String actionComments;
    //holds action id
    private int actionId;
    
    private PropertyChangeSupport propertySupport;
    
    //prps start - jul15 2003
    // OSP$Protocol_submission table has three new columns ABSTAINER_COUNT,
    // VOTING_COMMENTS and SUBMISSION_NUMBER
    private int abstainerCount ;
    private static final String PROP_ABSTAINER_COUNT = "abstainerCount" ;
    private String votingComments ;
    private static final String PROP_VOTING_COMMENTS = "votingComments" ;
    private int submissionNumber ;
    private static final String PROP_SUBMISSION_NUMBER = "submissionNumber" ;
    //prps end
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - Start
    private Vector vecReviewDeterminations;
    private java.sql.Date determinationDueDate;
    // Added for COEUSQA-3025 : Enhanced functionality to address the IACUC designated member review determination process - End
    
    /** Creates a new instance of Class */
    public ProtocolSubmissionInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Method used to get the sequnece number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequnece number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Method used to get the sequnece number
     * @return sequenceNumber int
     */
    public int getVersionNumber() {       
        return versionNumber;
    }

    /**
     * Method used to set the sequnece number
     * @param sequenceNumber int
     */
    public void setVresionNumber(int sequenceNumber) {
        this.versionNumber = versionNumber;
    }
    /**
     * Method used to get the old sequnece number
     * @return awSequenceNumber int
     */
    public int getAWSequenceNumber() {
        return awSequenceNumber;
    }

    /**
     * Method used to set the sequnece number
     * @param sequenceNumber int
     */
    public void setAWSequenceNumber(int sequenceNumber) {
        this.awSequenceNumber = sequenceNumber;
    }
    /**
     * Method used to get the schedule id
     * @return scheduleId String
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Method used to set the schedule id
     * @param newScheduleId String
     */
     public void setScheduleId(String newScheduleId){
        String oldScheduleId = scheduleId;
        this.scheduleId = newScheduleId;
        propertySupport.firePropertyChange(PROP_SCHEDULE_ID, 
                oldScheduleId, scheduleId);
    }
    /**
     * Method used to get the schedule id
     * @return awScheduleId String
     */
    public String getAWScheduleId() {
        return awScheduleId;
    }

    /**
     * Method used to set the schedule id
     * @param awScheduleId String
     */
     public void setAWScheduleId(String awScheduleId){
        this.awScheduleId = awScheduleId;
    }
    
    /**
     * Method used to get the committee id
     * @return committeeId String
     */
    public String getCommitteeId() {
        return committeeId;
    }

    /**
     * Method used to set the committee id 
     * @param committeeId String
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }
    
    /**
     * Method used to get the committee name
     * @return committeeName String
     */
    public String getCommitteeName() {
        return committeeName;
    }

    /**
     * Method used to set the committee name
     * @param committeeName String
     */
    public void setCommitteeName(String committeeName) {
        this.committeeName = committeeName;
    }
    
    /**
     * Method used to get the title
     * @return title String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method used to set the title
     * @param title String
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Method used to get the submission type code
     * @return submissionTypeCode int
     */
    public int getSubmissionTypeCode() {
        return submissionTypeCode;
    }

    /**
     * Method used to set the submission type code
     * @param newSubmissionTypeCode integer
     */
    public void setSubmissionTypeCode(int newSubmissionTypeCode){
        int oldSubmissionTypeCode = submissionTypeCode;
        this.submissionTypeCode = newSubmissionTypeCode;
        propertySupport.firePropertyChange(PROP_SUBMISSION_TYPE_CODE, 
                oldSubmissionTypeCode, submissionTypeCode);
    }

    /**
     * Method used to get the submission type description
     * @return submissionTypeDesc String
     */
    public String getSubmissionTypeDesc() {
        return submissionTypeDesc;
    }

    /**
     * Method used to set the submission type description
     * @param submissionTypeDesc String
     */
    public void setSubmissionTypeDesc(String submissionTypeDesc) {
        this.submissionTypeDesc = submissionTypeDesc;
    }

    /**
     * Method used to get the submission type qualifiers code
     * @return submissionQualTypeCode int
     */
    public int getSubmissionQualTypeCode() {
        return submissionQualTypeCode;
    }

    /**
     * Method used to set the submission type qualifiers code
     * @param newSubmissionQualTypeCode integer
     */
    public void setSubmissionQualTypeCode(int newSubmissionQualTypeCode){
        int oldSubmissionQualTypeCode = submissionQualTypeCode;
        this.submissionQualTypeCode = newSubmissionQualTypeCode;
        propertySupport.firePropertyChange(PROP_SUBMISSION_QUAL_TYPE_CODE, 
                oldSubmissionQualTypeCode, submissionQualTypeCode);
    }
    
    /**
     * Method used to get the submission type qualifiers description
     * @return submissionQualTypeDesc String
     */
    public String getSubmissionQualTypeDesc() {
        return submissionQualTypeDesc;
    }

    /**
     * Method used to set the submission type qualifiers description
     * @param submissionQualTypeDesc String
     */
    public void setSubmissionQualTypeDesc(String submissionQualTypeDesc) {
        this.submissionQualTypeDesc = submissionQualTypeDesc;
    }

    /**
     * Method used to get the protocol review type code
     * @return protocolReviewTypeCode int
     */
    public int getProtocolReviewTypeCode() {
        return protocolReviewTypeCode;
    }

    /**
     * Method used to set the protocol review type code
     * @param newProtocolReviewTypeCode integer
     */
    public void setProtocolReviewTypeCode(int newProtocolReviewTypeCode){
        int oldProtocolReviewTypeCode = protocolReviewTypeCode;
        this.protocolReviewTypeCode = newProtocolReviewTypeCode;
        propertySupport.firePropertyChange(PROP_PROTOCOL_REV_TYPE_CODE, 
                oldProtocolReviewTypeCode, protocolReviewTypeCode);
    }
    
    /**
     * Method used to get the protocol review type description
     * @return protocolReviewTypeDesc String
     */
    public String getProtocolReviewTypeDesc() {
        return protocolReviewTypeDesc;
    }

    /**
     * Method used to set the protocol review type description
     * @param protocolReviewTypeDesc String
     */
    public void setProtocolReviewTypeDesc(String protocolReviewTypeDesc) {
        this.protocolReviewTypeDesc = protocolReviewTypeDesc;
    }

    /**
     * Method used to get the submission status code
     * @return submissionStatusCode int
     */
    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }
    
    /**
     * Method used to set the submission status code
     * @param newSubmissionStatusCode integer
     */
    public void setSubmissionStatusCode(int newSubmissionStatusCode){
        int oldSubmissionStatusCode = submissionStatusCode;
        this.submissionStatusCode = newSubmissionStatusCode;
        propertySupport.firePropertyChange(PROP_SUBMISSION_STATUS_CODE, 
                oldSubmissionStatusCode, submissionStatusCode);
    }
 
    /**
     * Method used to get the submission status descriptions
     * @return submissionStatusDesc String
     */
    public String getSubmissionStatusDesc() {
        return submissionStatusDesc;
    }

    /**
     * Method used to set the submission status descriptions
     * @param submissionStatusDesc String
     */
    public void setSubmissionStatusDesc(String submissionStatusDesc) {
        this.submissionStatusDesc = submissionStatusDesc;
    }

    /**
     * Method used to get the submission date
     * @return submissionDate Date
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Method used to set the submission date
     * @param newSubmissionDate Date
     */
    public void setSubmissionDate(Date newSubmissionDate){
        Date oldSubmissionDate = submissionDate;
        this.submissionDate = newSubmissionDate;
        propertySupport.firePropertyChange(PROP_SUBMISSION_DATE, 
                oldSubmissionDate, submissionDate);   
    }

    /**
     * Method used to get the schedule date
     * @return scheduleDate Date
     */
    public Date getScheduleDate() {
        return scheduleDate;
    }

    /**
     * Method used to set the schedule date
     * @param scheduleDate Date
     */
    public void setScheduleDate(Date scheduleDate){
        this.scheduleDate = scheduleDate;
    }
    
    /**
     * Method used to get the comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }

    /**
     * Method used to set the comments
     * @param newComments String
     */
    public void setComments(String newComments){
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(PROP_COMMENTS, 
                oldComments, comments);   
    }
    
    /**
     * Method used to get the yes vote count
     * @return yesVoteCount int
     */
    public int getYesVoteCount() {
        return yesVoteCount;
    }

    /**
     * Method used to set the yes vote count
     * @param newYesVoteCount integer
     */
    public void setYesVoteCount(int newYesVoteCount){
        int oldYesVoteCount = yesVoteCount;
        this.yesVoteCount = newYesVoteCount;
        propertySupport.firePropertyChange(PROP_YES_VOTE, 
                oldYesVoteCount, yesVoteCount);
    }

    /**
     * Method used to get the no vote count
     * @return noVoteCount int
     */
    public int getNoVoteCount() {
        return noVoteCount;
    }

    /**
     * Method used to set the no vote count
     * @param newNoVoteCount integer
     */
    public void setNoVoteCount(int newNoVoteCount){
        int oldNoVoteCount = noVoteCount;
        this.noVoteCount = newNoVoteCount;
        propertySupport.firePropertyChange(PROP_NO_VOTE, 
                oldNoVoteCount, noVoteCount);
    }
    
    /**
     * Method used to get the Vector of ProtocolReviewerInfoBean
     * @return protocolReviewer Vector
     */
    public Vector getProtocolReviewer(){
        return protocolReviewer;
    }
    
    /**
     * Method used to set the Vector of ProtocolReviewerInfoBean
     * @param protocolReviewer Vector
     */
    public void setProtocolReviewer(Vector protocolReviewer){
        this.protocolReviewer = protocolReviewer;
    }
    
     /**
     * Method used to get the action id
     * @return actionId int
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * Method used to set the actionId
     * @param actionId integer
     */
    public void setActionId(int actionId){
        this.actionId = actionId;
    }
    
    /**
     * Method used to get the actionTypeCode
     * @return actionTypeCode int
     */
    public int getActionTypeCode() {
        return actionTypeCode;
    }

    /**
     * Method used to set the actionTypeCode
     * @param actionTypeCode integer
     */
    public void setActionTypeCode(int actionTypeCode){
        this.actionTypeCode = actionTypeCode;
    }
    
    /**
     * Method used to get the actionTypeDesc
     * @return actionTypeDesc String
     */
    public String getActionTypeDesc() {
        return actionTypeDesc;
    }
    
    /**
     * Method used to set the actionTypeDesc
     * @param actionTypeDesc String
     */
    public void setActionTypeDesc(String actionTypeDesc) {
        this.actionTypeDesc = actionTypeDesc;
    }

    /**
     * Method used to get the actionDate
     * @return actionDate Date
     */
    public Date getActionDate() {
        return actionDate;
    }
    
    /**
     * Method used to set the actionDate
     * @param actionDate Date
     */
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

     /**
     * Method used to get the actionComments
     * @return actionComments String
     */
    public String getActionComments() {
        return actionComments;
    }
    
    /**
     * Method used to set the actionComments
     * @param actionComments String
     */
    public void setActionComments(String actionComments) {
        this.actionComments = actionComments;
    }
   
  

    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
     /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */ 
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /**
     * To print the bean data in the server side 
     * 
     * @return String 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{ Protocol Number =>"+protocolNumber);
        strBffr.append("sequenceNumber =>"+sequenceNumber);
        strBffr.append("scheduleId  =>"+ scheduleId);
        strBffr.append("committeeId =>"+committeeId);
        strBffr.append("committeeName =>"+committeeName);
        strBffr.append("title  =>"+ title);
        strBffr.append("submissionTypeCode =>"+submissionTypeCode);
        strBffr.append("submissionTypeDesc =>"+submissionTypeDesc);
        strBffr.append("submissionQualTypeCode  =>"+ submissionQualTypeCode);
        strBffr.append("protocolReviewTypeCode =>"+protocolReviewTypeCode);
        strBffr.append("submissionStatusCode =>"+submissionStatusCode);
        strBffr.append("submissionStatusDesc =>"+submissionStatusDesc);
        strBffr.append("submissionDate =>"+submissionDate);
        strBffr.append("scheduleDate =>"+scheduleDate);
        strBffr.append("yesVoteCount =>"+yesVoteCount);
        strBffr.append("noVoteCount =>"+noVoteCount);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimeStamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }    
   
    //prps start - jul 15 2003
    
    /**
     * Method used to get the abstainerCount
     * @return abstainerCount int
     */
    public int getAbstainerCount() {
        return abstainerCount;
    }

    /**
     * Method used to set abstainerCount 
     * @param abstainerCount int
     */
    public void setAbstainerCount(int abstainerCount){
        int oldAbstainerCount = abstainerCount;
        this.abstainerCount = abstainerCount;
        propertySupport.firePropertyChange(PROP_ABSTAINER_COUNT, 
                oldAbstainerCount, abstainerCount);
    }
    
    
    /**
     * Method used to get votingComments
     * @return voting votingComments String
     */ 
    public String getVotingComments() {
        return votingComments ;
    }

    /**
     * Method used to set votingComments
     * @param votingComments String
     */
    public void setVotingComments(String votingComments){
        String oldVotingComments = votingComments;
        this.votingComments = votingComments;
        propertySupport.firePropertyChange(PROP_VOTING_COMMENTS, 
                oldVotingComments, votingComments);
    }
  
        
    /**
     * Method used to get the submission number
     * @return submission number int
     */
    public int getSubmissionNumber() {
        return submissionNumber ;
    }

    /**
     * Method used to set submission number 
     * @param submission number int
     */
    public void setSubmissionNumber(int submissionNumber){
        int oldSubmissionNumber = submissionNumber;
        this.submissionNumber = submissionNumber;
        propertySupport.firePropertyChange(PROP_SUBMISSION_NUMBER, 
                oldSubmissionNumber, submissionNumber);
    }
    //prps end
        
    /** Getter for property protocolExpeditedCheckList.
     * @return Value of property protocolExpeditedCheckList.
     */
    public java.util.Vector getProtocolExpeditedCheckList() {
        return protocolExpeditedCheckList;
    }
    
    /** Setter for property protocolExpeditedCheckList.
     * @param protocolExpeditedCheckList New value of property protocolExpeditedCheckList.
     */
    public void setProtocolExpeditedCheckList(java.util.Vector protocolExpeditedCheckList) {
        this.protocolExpeditedCheckList = protocolExpeditedCheckList;
    }
    
    /** Getter for property protocolExemptCheckList.
     * @return Value of property protocolExemptCheckList.
     */
    public java.util.Vector getProtocolExemptCheckList() {
        return protocolExemptCheckList;
    }
    
    /** Setter for property protocolExemptCheckList.
     * @param protocolExemptCheckList New value of property protocolExemptCheckList.
     */
    public void setProtocolExemptCheckList(java.util.Vector protocolExemptCheckList) {
        this.protocolExemptCheckList = protocolExemptCheckList;
    }
    
    /** Getter for property applicationDate.
     * @return Value of property applicationDate.
     */
    public java.sql.Date getApplicationDate() {
        return applicationDate;
    }
    
    /** Setter for property applicationDate.
     * @param applicationDate New value of property applicationDate.
     */
    public void setApplicationDate(java.sql.Date applicationDate) {
        this.applicationDate = applicationDate;
    }
    
    /** Getter for property PIName.
     * @return Value of property PIName.
     */
    public java.lang.String getPIName() {
        return PIName;
    }
    
    /** Setter for property PIName.
     * @param PIName New value of property PIName.
     */
    public void setPIName(java.lang.String PIName) {
        this.PIName = PIName;
    }
    
    /** Getter for property schedulePlace.
     * @return Value of property schedulePlace.
     */
    public java.lang.String getSchedulePlace() {
        return schedulePlace;
    }
    
    /** Setter for property schedulePlace.
     * @param schedulePlace New value of property schedulePlace.
     */
    public void setSchedulePlace(java.lang.String schedulePlace) {
        this.schedulePlace = schedulePlace;
    }    
    
    /** Getter for property protocolReviewComments.
     * @return Value of property protocolReviewComments.
     */
    public java.util.Vector getProtocolReviewComments() {
        return protocolReviewComments;
    }    
    
    /** Setter for property protocolReviewComments.
     * @param protocolReviewComments New value of property protocolReviewComments.
     */
    public void setProtocolReviewComments(java.util.Vector protocolReviewComments) {
        this.protocolReviewComments = protocolReviewComments;
    }    

    /**
     * Method to get the review determination details
     * @return vecReviewDetermination
     */
    public Vector getReviewDeterminations() {
        return vecReviewDeterminations;
    }

    /**
     * Method to set the review determination details
     * @param vecReviewDeterminations 
     */
    public void setReviewDeterminations(Vector vecReviewDeterminations) {
        this.vecReviewDeterminations = vecReviewDeterminations;
    }

    /**
     * Method ro get the determination due date
     * @return determinationDueDate
     */
    public Date getDeterminationDueDate() {
        return determinationDueDate;
    }

    /**
     * Method to set the determination due date
     * @param determinationDueDate 
     */
    public void setDeterminationDueDate(java.sql.Date determinationDueDate) {
        this.determinationDueDate = determinationDueDate;
    }
    
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_start
    /** Getter for property protocolReviewAttachments.
     * @return Value of property protocolReviewAttachments.
     */
    public Vector getProtocolReviewAttachments() {
        return protocolReviewAttachments;
    }
    
    /** Setter for property protocolReviewAttachments.
     * @param protocolReviewAttachments New value of property protocolReviewAttachments.
     */
    public void setProtocolReviewAttachments(Vector protocolReviewAttachments) {
        this.protocolReviewAttachments = protocolReviewAttachments;
    }
    // Added for COEUSQA-2542_Allow Protocol Reviewer to upload Attachments_end
}
