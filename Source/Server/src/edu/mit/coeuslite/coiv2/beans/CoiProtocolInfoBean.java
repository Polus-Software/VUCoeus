/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Sony
 */
public class CoiProtocolInfoBean implements Serializable {

    public CoiProtocolInfoBean() {
    }

    private String protocolNumber;
    private Integer sequencNumber;
    private Integer protocolTypeCode;
    private Integer protocolStatusCode;
    private String title;
    private String description;
    private Date applicationDate;
    private Date approvalDate;
    private Date expirationDate;
    private String fdaApplicationNumber;
    private String referenceNumber1;
    private String referenceNumber2;
    private String isBillable;
    private String specialReviewIndicator;
    private String vulnerableSubjectIndicator;
    private String keyStudyPersonIndicator;
    private String fundingSourceIndicator;
    private String corrrespondentIndicator;
    private String referenceIndicator;
    private String relatedProjectsIndicator;
    private Date createTimeStamp;
    private String createUser;
    private Date lastApprovalDate;
    private String projectType;
    private String protocolStatusDescription;
    private String projectRole;
    private boolean checked;
    /**
     * @return the protocolNumber
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * @param protocolNumber the protocolNumber to set
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * @return the sequencNumber
     */
    public Integer getSequencNumber() {
        return sequencNumber;
    }

    /**
     * @param sequencNumber the sequencNumber to set
     */
    public void setSequencNumber(Integer sequencNumber) {
        this.sequencNumber = sequencNumber;
    }

    /**
     * @return the protocolTypeCode
     */
    public Integer getProtocolTypeCode() {
        return protocolTypeCode;
    }

    /**
     * @param protocolTypeCode the protocolTypeCode to set
     */
    public void setProtocolTypeCode(Integer protocolTypeCode) {
        this.protocolTypeCode = protocolTypeCode;
    }

    /**
     * @return the protocolStatusCode
     */
    public Integer getProtocolStatusCode() {
        return protocolStatusCode;
    }

    /**
     * @param protocolStatusCode the protocolStatusCode to set
     */
    public void setProtocolStatusCode(Integer protocolStatusCode) {
        this.protocolStatusCode = protocolStatusCode;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

     /**
     * @return the approvalDate
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * @param approvalDate the approvalDate to set
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the fdaApplicationNumber
     */
    public String getFdaApplicationNumber() {
        return fdaApplicationNumber;
    }

    /**
     * @param fdaApplicationNumber the fdaApplicationNumber to set
     */
    public void setFdaApplicationNumber(String fdaApplicationNumber) {
        this.fdaApplicationNumber = fdaApplicationNumber;
    }

    /**
     * @return the referenceNumber1
     */
    public String getReferenceNumber1() {
        return referenceNumber1;
    }

    /**
     * @param referenceNumber1 the referenceNumber1 to set
     */
    public void setReferenceNumber1(String referenceNumber1) {
        this.referenceNumber1 = referenceNumber1;
    }

    /**
     * @return the referenceNumber2
     */
    public String getReferenceNumber2() {
        return referenceNumber2;
    }

    /**
     * @param referenceNumber2 the referenceNumber2 to set
     */
    public void setReferenceNumber2(String referenceNumber2) {
        this.referenceNumber2 = referenceNumber2;
    }

    /**
     * @return the isBillable
     */
    public String getIsBillable() {
        return isBillable;
    }

    /**
     * @param isBillable the isBillable to set
     */
    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    /**
     * @return the specialReviewIndicator
     */
    public String getSpecialReviewIndicator() {
        return specialReviewIndicator;
    }

    /**
     * @param specialReviewIndicator the specialReviewIndicator to set
     */
    public void setSpecialReviewIndicator(String specialReviewIndicator) {
        this.specialReviewIndicator = specialReviewIndicator;
    }

    /**
     * @return the vulnerableSubjectIndicator
     */
    public String getVulnerableSubjectIndicator() {
        return vulnerableSubjectIndicator;
    }

    /**
     * @param vulnerableSubjectIndicator the vulnerableSubjectIndicator to set
     */
    public void setVulnerableSubjectIndicator(String vulnerableSubjectIndicator) {
        this.vulnerableSubjectIndicator = vulnerableSubjectIndicator;
    }

    /**
     * @return the keyStudyPersonIndicator
     */
    public String getKeyStudyPersonIndicator() {
        return keyStudyPersonIndicator;
    }

    /**
     * @param keyStudyPersonIndicator the keyStudyPersonIndicator to set
     */
    public void setKeyStudyPersonIndicator(String keyStudyPersonIndicator) {
        this.keyStudyPersonIndicator = keyStudyPersonIndicator;
    }

    /**
     * @return the fundingSourceIndicator
     */
    public String getFundingSourceIndicator() {
        return fundingSourceIndicator;
    }

    /**
     * @param fundingSourceIndicator the fundingSourceIndicator to set
     */
    public void setFundingSourceIndicator(String fundingSourceIndicator) {
        this.fundingSourceIndicator = fundingSourceIndicator;
    }

    /**
     * @return the corrrespondentIndicator
     */
    public String getCorrrespondentIndicator() {
        return corrrespondentIndicator;
    }

    /**
     * @param corrrespondentIndicator the corrrespondentIndicator to set
     */
    public void setCorrrespondentIndicator(String corrrespondentIndicator) {
        this.corrrespondentIndicator = corrrespondentIndicator;
    }

    /**
     * @return the referenceIndicator
     */
    public String getReferenceIndicator() {
        return referenceIndicator;
    }

    /**
     * @param referenceIndicator the referenceIndicator to set
     */
    public void setReferenceIndicator(String referenceIndicator) {
        this.referenceIndicator = referenceIndicator;
    }

    /**
     * @return the relatedProjectsIndicator
     */
    public String getRelatedProjectsIndicator() {
        return relatedProjectsIndicator;
    }

    /**
     * @param relatedProjectsIndicator the relatedProjectsIndicator to set
     */
    public void setRelatedProjectsIndicator(String relatedProjectsIndicator) {
        this.relatedProjectsIndicator = relatedProjectsIndicator;
    }

    /**
     * @return the createTimeStamp
     */
    public Date getCreateTimeStamp() {
        return createTimeStamp;
    }

    /**
     * @param createTimeStamp the createTimeStamp to set
     */
    public void setCreateTimeStamp(Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    /**
     * @return the createUser
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * @param createUser the createUser to set
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * @return the lastApprovalDate
     */
    public Date getLastApprovalDate() {
        return lastApprovalDate;
    }

    /**
     * @param lastApprovalDate the lastApprovalDate to set
     */
    public void setLastApprovalDate(Date lastApprovalDate) {
        this.lastApprovalDate = lastApprovalDate;
    }

    /**
     * @return the projectType
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * @param projectType the projectType to set
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * @return the protocolStatusDescription
     */
    public String getProtocolStatusDescription() {
        return protocolStatusDescription;
    }

    /**
     * @param protocolStatusDescription the protocolStatusDescription to set
     */
    public void setProtocolStatusDescription(String protocolStatusDescription) {
        this.protocolStatusDescription = protocolStatusDescription;
    }

    /**
     * @return the projectRole
     */
    public String getProjectRole() {
        return projectRole;
    }

    /**
     * @param projectRole the projectRole to set
     */
    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    /**
     * @return the checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked the checked to set
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * @return the applicationDate
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * @param applicationDate the applicationDate to set
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    
}
