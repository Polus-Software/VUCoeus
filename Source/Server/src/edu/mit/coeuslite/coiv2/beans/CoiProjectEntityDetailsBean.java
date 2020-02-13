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
public class CoiProjectEntityDetailsBean implements Serializable{

    public CoiProjectEntityDetailsBean() {
    }

    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String personId;
    private Integer disclosureDispositionCode;
    private Integer disclosureStatusCode;
    private String disclosureStatus;
    private Integer coiDiscDetailsNumber;
    private Integer moduleCode;
    private String moduleItemKey;
    private String entityNumber;
    private Integer entitySequenceNumber;
    private Integer entityStatusCode;
    private String entityStatus;
    private String description;
    private String coiProjectId;
    private String coiProjectTitle;
    private String coiProjectType;
    private String coiProjectSponsor;
    private Date coiProjectStartDate;
    private Date coiProjectEndDate;
    private Double coiProjectFundingAmount;
    private String coiProjectRole;
    private Date updateTimeStamp;
    private String updateUser;
    private String entityName;
    private String comments;
    private String projectId;
    private Date applicationDate;
    private Date expirationDate;
    private String eventName;
     private String destination;
    private String purpose;
     private String sDate;
      private String eDate;

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    /**
     * @return the coiDisclosureNumber
     */
    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    /**
     * @param coiDisclosureNumber the coiDisclosureNumber to set
     */
    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    /**
     * @return the sequenceNumber
     */
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the disclosureDispositionCode
     */
    public Integer getDisclosureDispositionCode() {
        return disclosureDispositionCode;
    }

    /**
     * @param disclosureDispositionCode the disclosureDispositionCode to set
     */
    public void setDisclosureDispositionCode(Integer disclosureDispositionCode) {
        this.disclosureDispositionCode = disclosureDispositionCode;
    }

    /**
     * @return the disclosureStatusCode
     */
    public Integer getDisclosureStatusCode() {
        return disclosureStatusCode;
    }

    /**
     * @param disclosureStatusCode the disclosureStatusCode to set
     */
    public void setDisclosureStatusCode(Integer disclosureStatusCode) {
        this.disclosureStatusCode = disclosureStatusCode;
    }

    /**
     * @return the disclosureStatus
     */
    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    /**
     * @param disclosureStatus the disclosureStatus to set
     */
    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }

    /**
     * @return the coiDiscDetailsNumber
     */
    public Integer getCoiDiscDetailsNumber() {
        return coiDiscDetailsNumber;
    }

    /**
     * @param coiDiscDetailsNumber the coiDiscDetailsNumber to set
     */
    public void setCoiDiscDetailsNumber(Integer coiDiscDetailsNumber) {
        this.coiDiscDetailsNumber = coiDiscDetailsNumber;
    }

    /**
     * @return the moduleCode
     */
    public Integer getModuleCode() {
        return moduleCode;
    }

    /**
     * @param moduleCode the moduleCode to set
     */
    public void setModuleCode(Integer moduleCode) {
        this.moduleCode = moduleCode;
    }

    /**
     * @return the moduleItemKey
     */
    public String getModuleItemKey() {
        return moduleItemKey;
    }

    /**
     * @param moduleItemKey the moduleItemKey to set
     */
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    /**
     * @return the entityNumber
     */
    public String getEntityNumber() {
        return entityNumber;
    }

    /**
     * @param entityNumber the entityNumber to set
     */
    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    /**
     * @return the entitySequenceNumber
     */
    public Integer getEntitySequenceNumber() {
        return entitySequenceNumber;
    }

    /**
     * @param entitySequenceNumber the entitySequenceNumber to set
     */
    public void setEntitySequenceNumber(Integer entitySequenceNumber) {
        this.entitySequenceNumber = entitySequenceNumber;
    }

    /**
     * @return the entitystatusCode
     */
    public Integer getEntityStatusCode() {
        return entityStatusCode;
    }

    /**
     * @param entitystatusCode the entitystatusCode to set
     */
    public void setEntityStatusCode(Integer entitystatusCode) {
        this.entityStatusCode = entitystatusCode;
    }

    /**
     * @return the entityStatus
     */
    public String getEntityStatus() {
        return entityStatus;
    }

    /**
     * @param entityStatus the entityStatus to set
     */
    public void setEntityStatus(String entityStatus) {
        this.entityStatus = entityStatus;
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
     * @return the coiProjectId
     */
    public String getCoiProjectId() {
        return coiProjectId;
    }

    /**
     * @param coiProjectId the coiProjectId to set
     */
    public void setCoiProjectId(String coiProjectId) {
        this.coiProjectId = coiProjectId;
    }

    /**
     * @return the coiProjectTitle
     */
    public String getCoiProjectTitle() {
        return coiProjectTitle;
    }

    /**
     * @param coiProjectTitle the coiProjectTitle to set
     */
    public void setCoiProjectTitle(String coiProjectTitle) {
        this.coiProjectTitle = coiProjectTitle;
    }

    /**
     * @return the coiProjectType
     */
    public String getCoiProjectType() {
        return coiProjectType;
    }

    /**
     * @param coiProjectType the coiProjectType to set
     */
    public void setCoiProjectType(String coiProjectType) {
        this.coiProjectType = coiProjectType;
    }

    /**
     * @return the coiProjectSponsor
     */
    public String getCoiProjectSponsor() {
        return coiProjectSponsor;
    }

    /**
     * @param coiProjectSponsor the coiProjectSponsor to set
     */
    public void setCoiProjectSponsor(String coiProjectSponsor) {
        this.coiProjectSponsor = coiProjectSponsor;
    }

    /**
     * @return the coiProjectStartDate
     */
    public Date getCoiProjectStartDate() {
        return coiProjectStartDate;
    }

    /**
     * @param coiProjectStartDate the coiProjectStartDate to set
     */
    public void setCoiProjectStartDate(Date coiProjectStartDate) {
        this.coiProjectStartDate = coiProjectStartDate;
    }

    /**
     * @return the coiProjectEndDate
     */
    public Date getCoiProjectEndDate() {
        return coiProjectEndDate;
    }

    /**
     * @param coiProjectEndDate the coiProjectEndDate to set
     */
    public void setCoiProjectEndDate(Date coiProjectEndDate) {
        this.coiProjectEndDate = coiProjectEndDate;
    }

    /**
     * @return the coiProjectFundingAmount
     */
    public Double getCoiProjectFundingAmount() {
        return coiProjectFundingAmount;
    }

    /**
     * @param coiProjectFundingAmount the coiProjectFundingAmount to set
     */
    public void setCoiProjectFundingAmount(Double coiProjectFundingAmount) {
        this.coiProjectFundingAmount = coiProjectFundingAmount;
    }

    /**
     * @return the coiProjectRole
     */
    public String getCoiProjectRole() {
        return coiProjectRole;
    }

    /**
     * @param coiProjectRole the coiProjectRole to set
     */
    public void setCoiProjectRole(String coiProjectRole) {
        this.coiProjectRole = coiProjectRole;
    }

    /**
     * @return the updateTimeStamp
     */
    public Date getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    /**
     * @param updateTimeStamp the updateTimeStamp to set
     */
    public void setUpdateTimeStamp(Date updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    /**
     * @return the updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * @param updateUser the updateUser to set
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the applicationDate
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param applicationDate the applicationDate to set
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
}
