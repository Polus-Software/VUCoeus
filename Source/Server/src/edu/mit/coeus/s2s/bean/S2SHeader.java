/*
 * S2SHeader.java
 *
 * Created on March 9, 2005, 5:56 PM
 */

package edu.mit.coeus.s2s.bean;

import java.io.Serializable;
import java.util.HashMap;


/**
 *
 * @author  geot
 */
public class S2SHeader implements IS2SParam,Serializable{
    
    private String submissionTitle;
    private String opportunityId;
    private String cfdaNumber;
    private String competitionId;
    private String agency;
    private String agencyTitle;
    private HashMap streamParams;
    private String status;
    private String packageId;
    private String applicationFilingName;
    private String grantsGovTrackingNum;
    /** Creates a new instance of S2SHeader */
    public S2SHeader() {
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
     * Getter for property competitionId.
     * @return Value of property competitionId.
     */
    public java.lang.String getCompetitionId() {
        return competitionId;
    }
    
    /**
     * Setter for property competitionId.
     * @param competitionId New value of property competitionId.
     */
    public void setCompetitionId(java.lang.String competitionId) {
        this.competitionId = competitionId;
    }
    
    /**
     * Getter for property agency.
     * @return Value of property agency.
     */
    public java.lang.String getAgency() {
        return agency;
    }
    
    /**
     * Setter for property agency.
     * @param agency New value of property agency.
     */
    public void setAgency(java.lang.String agency) {
        this.agency = agency;
    }
    
    /**
     * Getter for property agencyTitle.
     * @return Value of property agencyTitle.
     */
    public java.lang.String getAgencyTitle() {
        return agencyTitle;
    }
    
    /**
     * Setter for property agencyTitle.
     * @param agencyTitle New value of property agencyTitle.
     */
    public void setAgencyTitle(java.lang.String agencyTitle) {
        this.agencyTitle = agencyTitle;
    }
    
    /**
     * Getter for property opportunityId.
     * @return Value of property opportunityId.
     */
    public java.lang.String getOpportunityId() {
        return opportunityId;
    }
    
    /**
     * Setter for property opportunityId.
     * @param opportunityId New value of property opportunityId.
     */
    public void setOpportunityId(java.lang.String opportunityId) {
        this.opportunityId = opportunityId;
    }
    
    public java.util.HashMap getStreamParams() {
        return streamParams;
    }
    
    /**
     * Setter for property streamParams.
     * @param streamParams New value of property streamParams.
     */
    public void setStreamParams(java.util.HashMap streamParams) {
        this.streamParams = streamParams;
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

    public void setPackageId(java.lang.String packageId) {
        this.packageId = packageId;
    }
    
    public String getPackageId(){
        return packageId;
    }

    public void setApplicationFilingName(java.lang.String applicationFilingName) {
        this.applicationFilingName = applicationFilingName;
    }
    public String getApplicationFilingName(){
        return applicationFilingName;
    }

    public String getGrantsGovTrackingNum() {
        return grantsGovTrackingNum;
    }

    public void setGrantsGovTrackingNum(String grantsGovTrackingNum) {
        this.grantsGovTrackingNum = grantsGovTrackingNum;
    }



}
