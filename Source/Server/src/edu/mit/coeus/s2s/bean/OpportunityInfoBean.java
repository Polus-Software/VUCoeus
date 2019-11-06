/*
 * OpportunityInfoBean.java
 *
 * Created on January 7, 2005, 12:20 PM
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.utils.S2SConstants;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author  geot
 */
public class OpportunityInfoBean implements java.io.Serializable {
    private String opportunityId;
    private String opportunityTitle;
    private String cfdaNumber;
    private List<CFDADetailsBean> cfdaDetailsList;
    private String competitionId;
    private String schemaUrl;
    private String instructionUrl;
    private Timestamp openingDate;
    private Timestamp closingDate;
    private String opportunity;
    private int submissionTypeCode;
    private String revisionCode;
    private String revisionOtherDescription;
    private boolean isMultiProject;
    private String competitionTitle;
    private String packageID;
    private String offeringAgency;
    private String agencyContactInfo;
    private String submissionEndPoint = S2SConstants.SOAP_HOST;
    
    /** Creates a new instance of OpportunityInfoBean */
    public OpportunityInfoBean() {
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
    
    /**
     * Getter for property opportunityTitle.
     * @return Value of property opportunityTitle.
     */
    public java.lang.String getOpportunityTitle() {
        return opportunityTitle;
    }
    
    /**
     * Setter for property opportunityTitle.
     * @param opportunityTitle New value of property opportunityTitle.
     */
    public void setOpportunityTitle(java.lang.String opportunityTitle) {
        this.opportunityTitle = opportunityTitle;
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
     * Getter for property schemaUrl.
     * @return Value of property schemaUrl.
     */
    public java.lang.String getSchemaUrl() {
        return schemaUrl;
    }
    
    /**
     * Setter for property schemaUrl.
     * @param schemaUrl New value of property schemaUrl.
     */
    public void setSchemaUrl(java.lang.String schemaUrl) {
        this.schemaUrl = schemaUrl;
    }
    
    /**
     * Getter for property instructionUrl.
     * @return Value of property instructionUrl.
     */
    public java.lang.String getInstructionUrl() {
        return instructionUrl;
    }
    
    /**
     * Setter for property instructionUrl.
     * @param instructionUrl New value of property instructionUrl.
     */
    public void setInstructionUrl(java.lang.String instructionUrl) {
        this.instructionUrl = instructionUrl;
    }
    
    /**
     * Getter for property openingDate.
     * @return Value of property openingDate.
     */
    public java.sql.Timestamp getOpeningDate() {
        return openingDate;
    }
    
    /**
     * Setter for property openingDate.
     * @param openingDate New value of property openingDate.
     */
    public void setOpeningDate(java.sql.Timestamp openingDate) {
        this.openingDate = openingDate;
    }
    
    /**
     * Getter for property closingDate.
     * @return Value of property closingDate.
     */
    public java.sql.Timestamp getClosingDate() {
        return closingDate;
    }
    
    /**
     * Setter for property closingDate.
     * @param closingDate New value of property closingDate.
     */
    public void setClosingDate(java.sql.Timestamp closingDate) {
        this.closingDate = closingDate;
    }
    
    /**
     * Getter for property opportunity.
     * @return Value of property opportunity.
     */
    public java.lang.String getOpportunity() {
        return opportunity;
    }
    
    /**
     * Setter for property opportunity.
     * @param opportunity New value of property opportunity.
     */
    public void setOpportunity(java.lang.String opportunity) {
        this.opportunity = opportunity;
    }
    
    /**
     * Getter for property submissionTypeCode.
     * @return Value of property submissionTypeCode.
     */
    public int getSubmissionTypeCode() {
        return submissionTypeCode;
    }
    
    /**
     * Setter for property submissionTypeCode.
     * @param submissionTypeCode New value of property submissionTypeCode.
     */
    public void setSubmissionTypeCode(int submissionTypeCode) {
        this.submissionTypeCode = submissionTypeCode;
    }
    
    /**
     * Getter for property revisionCode.
     * @return Value of property revisionCode.
     */
    public java.lang.String getRevisionCode() {
        return revisionCode;
    }
    
    /**
     * Setter for property revisionCode.
     * @param revisionCode New value of property revisionCode.
     */
    public void setRevisionCode(java.lang.String revisionCode) {
        this.revisionCode = revisionCode;
    }
    
    /**
     * Getter for property revisionOtherDescription.
     * @return Value of property revisionOtherDescription.
     */
    public java.lang.String getRevisionOtherDescription() {
        return revisionOtherDescription;
    }
    
    /**
     * Setter for property revisionOtherDescription.
     * @param revisionOtherDescription New value of property revisionOtherDescription.
     */
    public void setRevisionOtherDescription(java.lang.String revisionOtherDescription) {
        this.revisionOtherDescription = revisionOtherDescription;
    }

    public String getSubmissionEndPoint() {
        return submissionEndPoint;
    }

    public void setSubmissionEndPoint(String submissionEndPoint) {
        this.submissionEndPoint = submissionEndPoint;
    }

    public List<CFDADetailsBean> getCfdaDetailsList() {
        return cfdaDetailsList;
    }

    public void setCfdaDetailsList(List<CFDADetailsBean> cfdaDetailsList) {
        this.cfdaDetailsList = cfdaDetailsList;
    }

    public boolean isIsMultiProject() {
        return isMultiProject;
    }

    public void setIsMultiProject(boolean isMultiProject) {
        this.isMultiProject = isMultiProject;
    }

    public String getCompetitionTitle() {
        return competitionTitle;
    }

    public void setCompetitionTitle(String competitionTitle) {
        this.competitionTitle = competitionTitle;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getOfferingAgency() {
        return offeringAgency;
    }

    public void setOfferingAgency(String offeringAgency) {
        this.offeringAgency = offeringAgency;
    }

    public String getAgencyContactInfo() {
        return agencyContactInfo;
    }

    public void setAgencyContactInfo(String agencyContactInfo) {
        this.agencyContactInfo = agencyContactInfo;
    }
    
        
}
