/*
 * ProtocolReviewDeterminationBean.java
 *
 * Created on April 6, 2011, 12:25 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 
 * @author satheeshkumarkn
 */
public class ProtocolReviewDeterminationBean implements java.io.Serializable{
    
    private String protocolNumber;
    private int sequenceNumber;
    private int submissionNumber;
    private String personId;
    private String personFullName;
    private int recommendedReviewTypeCode;
    private String recommendedReviewTypeDescription;
    private Date sendNotificationDate;
    private Timestamp updateTimestamp;
    private String updateUser;
    private Timestamp awUpdateTimestamp;
    private String awUpdateUser;
    private String acType;
    private PropertyChangeSupport propertySupport;
    private static final String REVIEW_TYPE_CODE = "recommendedReviewTypeCode";
    private Date determinationDueDate;
    
    /** Creates a new instance of ProtocolReviewDeterminationBean */
    public ProtocolReviewDeterminationBean() {
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     * Method to get the protocolNumber
     * @return protocolNumber
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method to get the sequence number
     * @return sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method to set the sequenceNumber
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Method to get the submission number
     * @return submissionNumber
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }

    /**
     * Method to set the submission number
     * @param submissionNumber int
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }

    /**
     * Method to get the personId
     * @return personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method to set the personId
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * Method to get the person full name
     * @return personFullName
     */
    public String getPersonFullName() {
        return personFullName;
    }

    /**
     * Method to set the person full name
     * @param personFullName String
     */
    public void setPersonFullName(String personFullName) {
        this.personFullName = personFullName;
    }

    /**
     * Method to get the recommended review type code(Protocol Review type code)
     * @return recommendedReviewTypeCode
     */
    public int getRecommendedReviewTypeCode() {
        return recommendedReviewTypeCode;
    }
    
    /**
     * Method to set the recommeded review type code
     * @param recommendedReviewTypeCode int
     */
    public void setRecommendedReviewTypeCode(int newRecommendedReviewTypeCode) {
        int oldRecommendedReviewTypeCode = recommendedReviewTypeCode;
        this.recommendedReviewTypeCode = newRecommendedReviewTypeCode;
        propertySupport.firePropertyChange(REVIEW_TYPE_CODE,oldRecommendedReviewTypeCode, recommendedReviewTypeCode);        
    }
    
        /**
     * Method to get the recommended review type Description(Protocol Review type code)
     * @return recommendedReviewTypeDescription
     */
    public String getRecommendedReviewTypeDescription() {
        return recommendedReviewTypeDescription;
    }

    /**
     * Method to set the recommeded review type Description
     * @param recommendedReviewTypeDescription int
     */
    public void setRecommendedReviewTypeDescriptione(String recommendedReviewTypeDescription) {
        this.recommendedReviewTypeDescription = recommendedReviewTypeDescription;
    }

    /**
     * Method to get the notifcation send date
     * @return sendNotificationDate
     */
    public Date getSendNotificationDate() {
        return sendNotificationDate;
    }

    /**
     * Method to set the notification send date
     * @param sendNotificationDate Date
     */
    public void setSendNotificationDate(Date sendNotificationDate) {
        this.sendNotificationDate = sendNotificationDate;
    }

    /**
     * Method to get the update timestamp
     * @return updateTimestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method to set the updateTimestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * Method to get the updateUser
     * @return updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * Method to set the updateUser
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * Method to get the awUpdateTimestamp
     * @return awUpdateTimestamp
     */
    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    /**
     * Method to set the awUpdateTimestamp
     * @param awUpdateTimestamp TimeStamp
     */
    public void setAwUpdateTimestamp(Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }

    /**
     * Method to get the awUpdateUser
     * @return awUpdateUser
     */
    public String getAwUpdateUser() {
        return awUpdateUser;
    }

    /**
     * Method to set the awUpdateUser
     * @param awUpdateUser String
     */
    public void setAwUpdateUser(String awUpdateUser) {
        this.awUpdateUser = awUpdateUser;
    }

    /**
     * Method to get the actype
     * @return acType
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Method to set the actype
     * @param acType 
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
     * Method to get the determination due date
     * @return determinationDueDate
     */
    public Date getDeterminationDueDate() {
        return determinationDueDate;
    }

    /**
     * Method to set the determination due date
     * @param determinationDueDate 
     */
    public void setDeterminationDueDate(Date determinationDueDate) {
        this.determinationDueDate = determinationDueDate;
    }

    /**
     * To print the bean data in the server side
     *
     * @return String
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{ protocolNumber =>"+protocolNumber);
        strBffr.append("sequenceNumber =>"+sequenceNumber);
        strBffr.append("submissionNumber  =>"+ submissionNumber);
        strBffr.append("personId =>"+personId);
        strBffr.append("personFullName =>"+personFullName);
        strBffr.append("recommendedReviewTypeCode  =>"+ recommendedReviewTypeCode);
        strBffr.append("recommendedReviewTypeDescription =>"+recommendedReviewTypeDescription);
        strBffr.append("sendNotificationDate =>"+sendNotificationDate);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("updateUser  =>"+ updateUser);
        strBffr.append("awUpdateTimestamp =>"+awUpdateTimestamp);
        strBffr.append("awUpdateUser =>"+awUpdateUser);
        strBffr.append("acType =>"+acType);
        strBffr.append("determinationDueDate =>"+determinationDueDate);

        return strBffr.toString();
    }
}
