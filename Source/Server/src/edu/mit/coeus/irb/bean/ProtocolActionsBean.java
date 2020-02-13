/*
 * @(#)ProtocolActionsBean.java October 24, 2002, 12:21 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;
import java.beans.*;
import java.util.Vector;

/**
 * The class used to hold the information of <code>Protocol Actions</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:21 PM
 */

public class ProtocolActionsBean  implements java.io.Serializable , BaseBean{
    // holds the Type code 
    private static final String PROP_ACTION_TYPE_CODE = "actionTypeCode";
    // holds schedule Id
    private static final String PROP_SCHEDULE_ID = "scheduleId";
    // holds action comments
    private static final String PROP_COMMENTS = "comments";
    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds action id
    private int actionId;
    //holds type code
    private int actionTypeCode;
    //holds type decription
    private String actionTypeDescription;
    //holds schedule id
    private String scheduleId;
    //holds action comments
    private String comments;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds account type
    private String acType;
    //holds Review Comments
    private Vector reviewComments;
    //holds Committee Id
    private String committeeId;
        
     //holds approval date
    private java.sql.Date approvalDate;
    //holds expiration date
    private java.sql.Date expirationDate;
	
	//holds action date -- Added by Jobin -start
    private java.sql.Date actionDate;// Jobin - end
    
    //prps start jul 16 2003
    private int submissionNumber ;
    //prps end
    
    //Holds whether there is any Correspondence document for this action
    private boolean isCorrespondenceExists;
    //Added for case #1961 start 1
    //Holds whether there is any correspondence template for this action
    private boolean isCorresTemplateExists;
    //Added for Coeus 4.3 enhancement
    private String finalFlag;
    private Vector protoCorresType;
    //Added for case #1961 end 2
    private PropertyChangeSupport propertySupport;
    
    //Added for case#3046 - Notify IRB attachments - start    
    //Commented with case 4007:Icon based on Mime Type
//    private String fileName;
//    private byte[] fileBytes;
    //4007 End
    private boolean isDocumentExists;    
    //Added for case#3046 - Notify IRB attachments - end
    //Added for case 2176 - Risk Level Category - start
    private Vector riskLevels;
    //Added for case 2176 - Risk Level Category - end
    //Code added for Case#3554 - Notify IRB enhancement
    private String submissionTypeCode;
    private String reviwerTypeCode;
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start
    private CoeusVector actionDocuments;
    private ProtocolActionDocumentBean protocolActionDocumentBean;
    //COEUSDEV-328 : End
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    private boolean isProtocolEditable;
    private boolean isAnsweredQnrPresent;
    //Added for COEUSDEV-86 : Questionnaire for a Submission - End
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-start
    private String protocolActionDocumentDescription;
    //  Added for the case#COEUSQA-2353Need to a better way to handle large numbers of protocol attachments-end
    /** Creates new ProtocolActionsBean */
    public ProtocolActionsBean() {
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
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber(){
        return sequenceNumber;
    }
    
    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber){
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * Method used to get the action Id
     * @return actionId int
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * Method used to set the action Id
     * @param actionId int
     */
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    /**
     * Method used to get the action type code
     * @return actionTypeCode int
     */
    public int getActionTypeCode() {
        return actionTypeCode;
    }

     /**
     * Method used to set the action type code
     * @param newActionTypeCode int
     */
    public void setActionTypeCode(int newActionTypeCode) {
        int oldActionTypeCode = actionTypeCode;
        this.actionTypeCode = newActionTypeCode;
        propertySupport.firePropertyChange(PROP_ACTION_TYPE_CODE, 
                oldActionTypeCode, actionTypeCode);
        
    }

    /**
     * Method used to get the action type description
     * @return actionTypeDescription String
     */
    public String getActionTypeDescription() {
        return actionTypeDescription;
    }

    /**
     * Method used to set the action type description
     * @param actionTypeDescription String
     */
    public void setActionTypeDescription(String actionTypeDescription) {
        this.actionTypeDescription = actionTypeDescription;
    }

    /**
     * Method used to get the schedule Id
     * @return scheduleId String
     */

    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Method used to set the schedule Id
     * @param newScheduleId String
     */
    public void setScheduleId(String newScheduleId) {
        String oldScheduleId = scheduleId;
        this.scheduleId = newScheduleId;
        propertySupport.firePropertyChange(PROP_SCHEDULE_ID, 
                oldScheduleId, scheduleId);
    }

    /**
     * Method used to get the action comments
     * @return comments String
     */

    public String getComments() {
        return comments;
    }

     /**
     * Method used to set the action comments
     * @param newComments String
     */
    public void setComments(String newComments) {
        String oldComments = comments;
        this.comments = newComments;
        propertySupport.firePropertyChange(PROP_COMMENTS, 
                oldComments, comments);
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
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Account Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Account Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
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
        strBffr.append("actionId =>"+actionId);
        strBffr.append("actionTypeCode =>"+actionTypeCode);
        strBffr.append("actionTypeDescription =>"+actionTypeDescription );
        strBffr.append("scheduleId =>"+scheduleId);
        strBffr.append("comments =>"+comments);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
    
        return strBffr.toString();
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
 

    
    public java.sql.Date getApprovalDate()
    {
        return approvalDate;
    }
    

    public void setApprovalDate(java.sql.Date approvalDate)
    {
        this.approvalDate = approvalDate ;
    }

    public java.sql.Date getExpirationDate()
    {
        return expirationDate ;
    }
    

    public void setExpirationDate(java.sql.Date expirationDate)
    {
        this.expirationDate = expirationDate ;
    }

    
    //prps start jul 16 2003
    
    /**
     * Method used to get the submission number
     * @return submissionNumber int
     */
    public int getSubmissionNumber(){
        return submissionNumber;
    }
    
    /**
     * Method used to set the submission number
     * @param submissionNumber int
     */
    public void setSubmissionNumber(int submissionNumber){
        this.submissionNumber = submissionNumber;
    }
    
    //prps end    
    
    /** Getter for property isCorrespondenceExists.
     * @return Value of property isCorrespondenceExists.
     */
    public boolean isCorrespondenceExists() {
        return isCorrespondenceExists;
    }
    
    /** Setter for property isCorrespondenceExists.
     * @param isCorrespondenceExists New value of property isCorrespondenceExists.
     */
    public void setIsCorrespondenceExists(boolean isCorrespondenceExists) {
        this.isCorrespondenceExists = isCorrespondenceExists;
    }
    
    /** Getter for property reviewComments.
     * @return Value of property reviewComments.
     */
    public Vector getReviewComments() {
        return reviewComments;
    }
    
    /** Setter for property reviewComments.
     * @param reviewComments New value of property reviewComments.
     */
    public void setReviewComments(Vector reviewComments) {
        this.reviewComments = reviewComments;
    }
    
    /** Getter for property committeeId.
     * @return Value of property committeeId.
     */
    public java.lang.String getCommitteeId() {
        return committeeId;
    }
    
    /** Setter for property committeeId.
     * @param committeeId New value of property committeeId.
     */
    public void setCommitteeId(java.lang.String committeeId) {
        this.committeeId = committeeId;
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
	//Added for case #1961 start 2
        /**
         * Getter for property isCorresTemplateExists.
         * @return Value of property isCorresTemplateExists.
         */
        public boolean isIsCorresTemplateExists() {
            return isCorresTemplateExists;
        }
        
        /**
         * Setter for property isCorresTemplateExists.
         * @param isCorresTemplateExists New value of property isCorresTemplateExists.
         */
        public void setIsCorresTemplateExists(boolean isCorresTemplateExists) {
            this.isCorresTemplateExists = isCorresTemplateExists;
        }
        
        /**
         * Getter for property protoCorresType.
         * @return Value of property protoCorresType.
         */
        public java.util.Vector getProtoCorresType() {
            return protoCorresType;
        }        
       
        /**
         * Setter for property protoCorresType.
         * @param protoCorresType New value of property protoCorresType.
         */
        public void setProtoCorresType(java.util.Vector protoCorresType) {
            this.protoCorresType = protoCorresType;
        }
        
        /**
         * Getter for property finalFlag.
         * @return Value of property finalFlag.
         */
        public java.lang.String getFinalFlag() {
            return finalFlag;
        }
        
        /**
         * Setter for property finalFlag.
         * @param finalFlag New value of property finalFlag.
         */
        public void setFinalFlag(java.lang.String finalFlag) {
            this.finalFlag = finalFlag;
        }
        
        //Added for case #1961 end 2
   //Added for case#3046 - Notify IRB attachments - start    
    public boolean isIsDocumentExists() {
        return isDocumentExists;
    }

    public void setIsDocumentExists(boolean isDocumentExists) {
        this.isDocumentExists = isDocumentExists;
    }    
    //Added for case#3046 - Notify IRB attachments - end
   //Added for case 2176 - Risk Level Category - end
    public Vector getRiskLevels() {
        return riskLevels;
    }

    public void setRiskLevels(Vector riskLevels) {
        this.riskLevels = riskLevels;
    }
    //Added for case 2176 - Risk Level Category - end

    public String getSubmissionTypeCode() {
        return submissionTypeCode;
    }

    public void setSubmissionTypeCode(String submissionTypeCode) {
        this.submissionTypeCode = submissionTypeCode;
    }

    public String getReviwerTypeCode() {
        return reviwerTypeCode;
    }

    public void setReviwerTypeCode(String reviwerTypeCode) {
        this.reviwerTypeCode = reviwerTypeCode;
    }
    //Added for COEUSDEV-328 : Notify IRB FYI submission only permits the Aggregator to upload one document - Start  
    /*
     * Setter method to set documents uploaded in a action
     * @param actionDocuments
     */
    public void setActionDocuments(CoeusVector actionDocuments){
        this.actionDocuments = actionDocuments;
    }
    /*
     * Getter method to get the documents uploaded during a action
     * @return actionDocuments
     */
    public CoeusVector getActionDocuments(){
        return this.actionDocuments;
    }
    
    /*
     * Setter method to set ProtocolActionDocumentBean
     * @param protocolActionDocumentBean
     */
    
    public void setProtocolActionDocumentBean(ProtocolActionDocumentBean protocolActionDocumentBean){
        this.protocolActionDocumentBean = protocolActionDocumentBean;
    }
    
    /*
     * Getter method to get ProtocolActionDocumentBean
     * @Param protocolActionDocumentBean
     */
    public ProtocolActionDocumentBean getProtocolActionDocumentBean(){
        return this.protocolActionDocumentBean;
    }
    //COEUSDEV-328 : End
    
    //Added for COEUSDEV-86 : Questionnaire for a Submission - Start
    /**
     * Setter method to set protocol is in editabl emode
     * @param isProtocolEditable
     */
    public void setProtocolEditable(boolean isProtocolEditable){
        this.isProtocolEditable = isProtocolEditable;
    }
    //  Added for the case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments-start
     /*
     * Setter method to set protocolActionDocumentDescription
     * @param protocolActionDocumentDescription
     */
    
    public void setProtocolActionDocumentDescription(String protocolActionDocumentDescription){
        this.protocolActionDocumentDescription = protocolActionDocumentDescription;
    }
    
    /*
     * Getter method to get protocolActionDocumentDescription
     * @Param protocolActionDocumentDescription
     */
    public String getProtocolActionDocumentDescription(){
        return this.protocolActionDocumentDescription;
    }
    
    //  Added for the case#COEUSQA-2353-Need to a better way to handle large numbers of protocol attachments - end
    /**
     * Getter method to get is protocol in edit mode
     * @return isProtocolEditable
     */
    public boolean isProtocolEditable(){
        return isProtocolEditable;
    }
    
    /**
     * Setter method to set is any question is answered
     * @param isAnsweredQnrPresent
     */
    public void setAnsweredQnrPresent(boolean isAnsweredQnrPresent){
        this.isAnsweredQnrPresent = isAnsweredQnrPresent;
    }
    
    /**
     * Getter method to get isAnsweredQnrPresent
     * @return isAnswereQnrPresent
     */
    public boolean isAnsweredQnrPresent(){
        return isAnsweredQnrPresent;
    }
    
    
    //COEUSDEV-86 : End
}
