/*
 * SubmissionInfoBean.java
 *
 * Created on June 14, 2018, 12:46 PM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author  farsana
 */
public class SubmissionInfoBean implements Serializable,BaseBean{
    private String submissionTitle;//Proposal number
    private String packageID;
    private String submissionDetails;
    private int submissionNumber;
    private String opportunityID;
    private String competitionID;
    private String cfdaNumber;
    private String grantsGovTrackingNumber;
    private Timestamp receivedDateTime;
    private String status;
    private Timestamp statusDate;
    private String agencyTrackingNumber;
    private String applicationData;// string value of xml file
    private ArrayList attachments;
    private Timestamp lastNotifiedDate;
    private String sortId;
    /** Creates a new instance of SubmissionInfoBean */
    public SubmissionInfoBean() {
    }
    
    
//    public String getAvailableSubmissionNumber() {
//        return availableSubmissionNumber;
//    }
//
//    public void setAvailableSubmissionNumber(String availableSubmissionNumber) {
//        this.availableSubmissionNumber = availableSubmissionNumber;
//    }

    public String getSubmissionDetails() {
        return submissionDetails;
    }

    public void setSubmissionDetails(String submissionDetails) {
        this.submissionDetails = submissionDetails;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }
    
    /**
     * Getter for property opportunityID.
     * @return Value of property opportunityID.
     */
    public java.lang.String getOpportunityID() {
        return opportunityID;
    }
    
    /**
     * Setter for property opportunityID.
     * @param opportunityID New value of property opportunityID.
     */
    public void setOpportunityID(java.lang.String opportunityID) {
        this.opportunityID = opportunityID;
    }
    
    /**
     * Getter for property competitionID.
     * @return Value of property competitionID.
     */
    public java.lang.String getCompetitionID() {
        return competitionID;
    }
    
    /**
     * Setter for property competitionID.
     * @param competitionID New value of property competitionID.
     */
    public void setCompetitionID(java.lang.String competitionID) {
        this.competitionID = competitionID;
    }
    
    /**
     * Getter for property cfdaNumber.
     * @return Value of property cfdaNumber.
     */
    public java.lang.String getCfdaNumber() {
        return cfdaNumber;
    }
    
    /**
     * Setter for property cfdaNumber.
     * @param cfdaNumber New value of property cfdaNumber.
     */
    public void setCfdaNumber(java.lang.String cfdaNumber) {
        this.cfdaNumber = cfdaNumber;
    }
    
    /**
     * Getter for property grantsGovTrackingNumber.
     * @return Value of property grantsGovTrackingNumber.
     */
    public java.lang.String getGrantsGovTrackingNumber() {
        return grantsGovTrackingNumber;
    }
    
    /**
     * Setter for property grantsGovTrackingNumber.
     * @param grantsGovTrackingNumber New value of property grantsGovTrackingNumber.
     */
    public void setGrantsGovTrackingNumber(java.lang.String grantsGovTrackingNumber) {
        this.grantsGovTrackingNumber = grantsGovTrackingNumber;
    }
    
    /**
     * Getter for property receivedDateTime.
     * @return Value of property receivedDateTime.
     */
    public java.sql.Timestamp getReceivedDateTime() {
        return receivedDateTime;
    }
    
    /**
     * Setter for property receivedDateTime.
     * @param receivedDateTime New value of property receivedDateTime.
     */
    public void setReceivedDateTime(java.sql.Timestamp receivedDateTime) {
        this.receivedDateTime = receivedDateTime;
    }
    
    /**
     * Getter for property status.
     * @return Value of property status.
     */
    public java.lang.String getStatus() {
        return status;
    }
    
    /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    /**
     * Getter for property statusDate.
     * @return Value of property statusDate.
     */
    public java.sql.Timestamp getStatusDate() {
        return statusDate;
    }
    
    /**
     * Setter for property statusDate.
     * @param statusDate New value of property statusDate.
     */
    public void setStatusDate(java.sql.Timestamp statusDate) {
        this.statusDate = statusDate;
    }
    
    /**
     * Getter for property agencyTrackingNumber.
     * @return Value of property agencyTrackingNumber.
     */
    public java.lang.String getAgencyTrackingNumber() {
        return agencyTrackingNumber;
    }
    
    /**
     * Setter for property agencyTrackingNumber.
     * @param agencyTrackingNumber New value of property agencyTrackingNumber.
     */
    public void setAgencyTrackingNumber(java.lang.String agencyTrackingNumber) {
        this.agencyTrackingNumber = agencyTrackingNumber;
    }
    
    /**
     * Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /**
     * Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
    /**
     * Getter for property applicationData.
     * @return Value of property applicationData.
     */
    public java.lang.String getApplicationData() {
        return applicationData;
    }
    
    /**
     * Setter for property applicationData.
     * @param applicationData New value of property applicationData.
     */
    public void setApplicationData(java.lang.String applicationData) {
        this.applicationData = applicationData;
    }
    
    /**
     * Getter for property attachments.
     * @return Value of property attachments.
     */
    public ArrayList getAttachments() {
        return attachments;
    }
    
    /**
     * Setter for property attachments.
     * @param attachments New value of property attachments.
     */
    public void setAttachments(ArrayList attachments) {
        this.attachments = attachments;
    }
    
    /**
     * Getter for property submissionTitle.
     * @return Value of property submissionTitle.
     */
    public java.lang.String getSubmissionTitle() {
        return submissionTitle;
    }
    
    /**
     * Setter for property submissionTitle.
     * @param submissionTitle New value of property submissionTitle.
     */
    public void setSubmissionTitle(java.lang.String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }
    
    /**
     * Getter for property lastNotifiedDate.
     * @return Value of property lastNotifiedDate.
     */
    public java.sql.Timestamp getLastNotifiedDate() {
        return lastNotifiedDate;
    }
    
    /**
     * Setter for property lastNotifiedDate.
     * @param lastNotifiedDate New value of property lastNotifiedDate.
     */
    public void setLastNotifiedDate(java.sql.Timestamp lastNotifiedDate) {
        this.lastNotifiedDate = lastNotifiedDate;
    }
    
    /**
     * Getter for property sortId.
     * @return Value of property sortId.
     */
    public java.lang.String getSortId() {
        return sortId;
    }
    
    /**
     * Setter for property sortId.
     * @param sortId New value of property sortId.
     */
    public void setSortId(java.lang.String sortId) {
        this.sortId = sortId;
    }
    
}
