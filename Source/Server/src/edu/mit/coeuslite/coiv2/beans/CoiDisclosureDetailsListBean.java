/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.util.Date;

/**
 *
 * @author midhunmk
 */
public class CoiDisclosureDetailsListBean {


    private String coiProjectId;
    private String coiProjectTitle;
    private Date coiProjectStartDate;
    private Date coiProjectEndDate;
    private String coiDisclosureStatusDesc;
    private int coiDisclosureStatusCode=0;

    private String entityNumber;
    private String entityName;
    private String entityStatusDesc;
    private int entityStatusCode;

    private int moduleCode=0;
    private String moduleItemKey;
    private int moduleItemSequence=0;
    private String coiDisclosureNumber;
    private int coiSequenceNumber=0;
    private String personId;
    private Date certificationTimestamp;
    private Date expirationDate;
    private int coiDisclActiveStatus;
    private String eventType;
    private String reviewStatus;
    private int reviewStatusCode;
    private String eventName;
    private String destination;
    private String purpose;
    private String coiProjectSponser;

    public String getCoiProjectSponser() {
        return coiProjectSponser;
    }

    public void setCoiProjectSponser(String coiProjectSponser) {
        this.coiProjectSponser = coiProjectSponser;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }



    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getReviewStatusCode() {
        return reviewStatusCode;
    }

    public void setReviewStatusCode(int reviewStatusCode) {
        this.reviewStatusCode = reviewStatusCode;
    }



    public String getEventType() {
        return eventType;
    }


    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getCoiDisclActiveStatus() {
        return coiDisclActiveStatus;
    }

    public void setCoiDisclActiveStatus(int coiDisclActiveStatus) {
        this.coiDisclActiveStatus = coiDisclActiveStatus;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getCertificationTimestamp() {
        return certificationTimestamp;
    }

    public void setCertificationTimestamp(Date certificationTimestamp) {
        this.certificationTimestamp = certificationTimestamp;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    public int getCoiSequenceNumber() {
        return coiSequenceNumber;
    }

    public void setCoiSequenceNumber(int coiSequenceNumber) {
        this.coiSequenceNumber = coiSequenceNumber;
    }


    public int getCoiDisclosureStatusCode() {
        return coiDisclosureStatusCode;
    }

    public void setCoiDisclosureStatusCode(int coiDisclosureStatusCode) {
        this.coiDisclosureStatusCode = coiDisclosureStatusCode;
    }

    public String getCoiDisclosureStatusDesc() {
        return coiDisclosureStatusDesc;
    }

    public void setCoiDisclosureStatusDesc(String coiDisclosureStatusDesc) {
        this.coiDisclosureStatusDesc = coiDisclosureStatusDesc;
    }

    public Date getCoiProjectEndDate() {
        return coiProjectEndDate;
    }

    public void setCoiProjectEndDate(Date coiProjectEndDate) {
        this.coiProjectEndDate = coiProjectEndDate;
    }

    public String getCoiProjectId() {
        return coiProjectId;
    }

    public void setCoiProjectId(String coiProjectId) {
        this.coiProjectId = coiProjectId;
    }

    public Date getCoiProjectStartDate() {
        return coiProjectStartDate;
    }

    public void setCoiProjectStartDate(Date coiProjectStartDate) {
        this.coiProjectStartDate = coiProjectStartDate;
    }

    public String getCoiProjectTitle() {
        return coiProjectTitle;
    }

    public void setCoiProjectTitle(String coiProjectTitle) {
        this.coiProjectTitle = coiProjectTitle;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public int getEntityStatusCode() {
        return entityStatusCode;
    }

    public void setEntityStatusCode(int entityStatusCode) {
        this.entityStatusCode = entityStatusCode;
    }

    public String getEntityStatusDesc() {
        return entityStatusDesc;
    }

    public void setEntityStatusDesc(String entityStatusDesc) {
        this.entityStatusDesc = entityStatusDesc;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleItemKey() {
        return moduleItemKey;
    }

    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    public int getModuleItemSequence() {
        return moduleItemSequence;
    }

    public void setModuleItemSequence(int moduleItemSequence) {
        this.moduleItemSequence = moduleItemSequence;
    }

    /**
     * @return the reviewStatus
     */
    public String getReviewStatus() {
        return reviewStatus;
    }

    /**
     * @param reviewStatus the reviewStatus to set
     */
    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }


}
