/*
 * @(#)ProtocolReviewerInfoBean.java 11/26/2002 2:37 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

/**
 * The class used to hold the information of <code>ProtocolReviewer InfoBean</code>
 * 
 * @author  Mukundan.C
 * @version 1.0
 * Created on November 26, 2002, 2:37 PM
 */

public class ProtocolReviewerInfoBean implements Serializable {
     // holds the person id
    private static final String PROP_PERSON_ID = "personId";
     // holds the reviewer type code
    private static final String PROP_REVIEWER_TYPE_CODE = "reviewerTypeCode";
    
    // holds the protocol number
    private String protocolNumber;
    // holds the sequence number
    private int sequenceNumber;
    /**** updated on 13-02-03 for maintaining old sequence number  */
    // holds the old sequence number
    private int awSequenceNumber;
    
    // holds the schedule id
    private String scheduleId;
    //holds submissionNumber
    private int submissionNumber;
    // holds the person id
    private String personId;
    // holds the awPerson id
    private String awPersonId;
    // holds the person name
    private String personName;
    //holds non Employee Flag
    private boolean isNonEmployee;
    //holds AW non Employee Flag
    private boolean awIsNonEmployee;    
    // holds the reviewer type code
    private int reviewerTypeCode;
    // holds the awReviewer type code
    private int awReviewerTypeCode;
    // holds the protocol type description
    private String reviewerTypeDesc;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds Action type
    private String acType;
    // 3282: Reviewer View of Protocol materials - Start
    private Date assignedDate;
    private Date dueDate;
    private boolean reviewComplete,awReviewComplete;
    private String recommendedActionCode;
    // 3282: Reviewer View of Protocol materials - End
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of ProtocolReviewerInfoBean */
    public ProtocolReviewerInfoBean() {
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
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**** begin: updated on 13-02-03 for maintaining old sequence number  */

    /**
     * Method used to get the old sequence number
     * @return awSequenceNumber int
     */
    public int getAWSequenceNumber() {
        return awSequenceNumber;
    }

    /**
     * Method used to set the old sequence number
     * @param awSequenceNumber int
     */
    public void setAWSequenceNumber(int sequenceNumber) {
        this.awSequenceNumber = sequenceNumber;
    }
    /****** end */
    /**
     * Method used to get the schedule id
     * @return scheduleId String
     */
    /*public String getScheduleId() {
        return scheduleId;
    }*/

    /**
     * Method used to set the schedule id
     * @param scheduleId String
     */
    /*public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }*/

    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param newPersonId String
     */
    public void setPersonId(String newPersonId){
        String oldPersonId = personId;
        this.personId = newPersonId;
        propertySupport.firePropertyChange(PROP_PERSON_ID, 
                                        oldPersonId, personId);   
    }
    
    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getAWPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param awPersonId String
     */
    public void setAWPersonId(String awPersonId){
        this.awPersonId = awPersonId;
    }

    /**
     * Method used to get the person name
     * @return personName String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Method used to set the person name
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    /**
     * Method used to get the reviwer type code
     * @return reviewerTypeCode int
     */
    public int getReviewerTypeCode() {
        return reviewerTypeCode;
    }

    /**
     * Method used to set the reviwer type code
     * @param newReviewerTypeCode int
     */
    public void setReviewerTypeCode(int newReviewerTypeCode){
        int oldReviewerTypeCode = reviewerTypeCode;
        this.reviewerTypeCode = newReviewerTypeCode;
        propertySupport.firePropertyChange(PROP_REVIEWER_TYPE_CODE, 
                                        oldReviewerTypeCode, reviewerTypeCode);   
    }
 
    /**
     * Method used to get the reviwer type code
     * @return reviewerTypeCode int
     */
    public int getAWReviewerTypeCode() {
        return awReviewerTypeCode;
    }

    /**
     * Method used to set the reviwer type code
     * @param awReviewerTypeCode integer
     */
    public void setAWReviewerTypeCode(int awReviewerTypeCode){
        this.awReviewerTypeCode = awReviewerTypeCode;
    }
    
    /**
     * Method used to get the reviwer type description
     * @return reviewerTypeDesc String
     */
    public String getReviewerTypeDesc() {
        return reviewerTypeDesc;
    }

    /**
     * Method used to set the reviwer type description
     * @param reviewerTypeDesc String
     */
    public void setReviewerTypeDesc(String reviewerTypeDesc) {
        this.reviewerTypeDesc = reviewerTypeDesc;
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
    public String getAcType() {
        return acType;
    }

    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType) {
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
        strBffr.append("personId =>"+personId);
        strBffr.append("personName =>"+personName);
        strBffr.append("AWpersonId =>"+awPersonId);
        strBffr.append("reviewerTypeCode  =>"+ reviewerTypeCode);
        strBffr.append("AWreviewerTypeCode  =>"+ awReviewerTypeCode);
        strBffr.append("reviewerTypeDesc =>"+reviewerTypeDesc);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimeStamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }
    
    /** Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /** Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
    /** Getter for property isNonEmployee.
     * @return Value of property isNonEmployee.
     */
    public boolean isNonEmployee() {
        return isNonEmployee;
    }
    
    /** Setter for property isNonEmployee.
     * @param isNonEmployee New value of property isNonEmployee.
     */
    public void setIsNonEmployee(boolean isNonEmployee) {
        this.isNonEmployee = isNonEmployee;
    }
    
    /** Getter for property awIsNonEmployee.
     * @return Value of property awIsNonEmployee.
     */
    public boolean isAwNonEmployee() {
        return awIsNonEmployee;
    }
    
    /** Setter for property awIsNonEmployee.
     * @param awIsNonEmployee New value of property awIsNonEmployee.
     */
    public void setAwNonEmployee(boolean awIsNonEmployee) {
        this.awIsNonEmployee = awIsNonEmployee;
    }
    // 3282: Reviewer View of Protocol materials - Start
    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public boolean isReviewComplete() {
        return reviewComplete;
    }
    
    public void setReviewComplete(boolean reviewComplete) {
        this.reviewComplete = reviewComplete;
    }
    
    public String getRecommendedActionCode() {
        return recommendedActionCode;
    }
    
    public void setRecommendedActionCode(String recommendedAction) {
        this.recommendedActionCode = recommendedAction;
    }
    // 3282: Reviewer View of Protocol materials - End
    
    // Added for COEUSQA-3012: notification for when a reviewer completes their review in IACUC - start
    /*
     * Method to get the review complete flag value which is saved in the database
     * @return awReviewComplete - boolean
     */
    public boolean isAwReviewComplete() {
        return awReviewComplete;
    }
    
    /*
     * Method to set the review complete flag value which is saved in the database
     * @paramreviewComplete - boolean
     */
    public void setAwReviewComplete(boolean reviewComplete) {
        this.awReviewComplete = reviewComplete;
    }
    // Added for COEUSQA-3012: notification for when a reviewer completes their review in IACUC - end
}
