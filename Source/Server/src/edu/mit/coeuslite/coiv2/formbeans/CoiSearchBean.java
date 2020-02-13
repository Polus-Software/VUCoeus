/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.formbeans;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Sony
 */
public class CoiSearchBean extends ActionForm{

    private String user;
    private String discl;
    private String dispositionStatus;
    private String disclosureStatus;
    private String disclosureEvent;
    private String subDate;
    private String subEndDate;
    private String entityName;

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(String dateEdited) {
        this.dateEdited = dateEdited;
    }

    public String getGraHeadCount() {
        return graHeadCount;
    }

    public void setGraHeadCount(String graHeadCount) {
        this.graHeadCount = graHeadCount;
    }

    public String getLeadUnit() {
        return leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    public String getMailingAddresId() {
        return mailingAddresId;
    }

    public void setMailingAddresId(String mailingAddresId) {
        this.mailingAddresId = mailingAddresId;
    }

    public String getNewUpdateTimetamp() {
        return newUpdateTimetamp;
    }

    public void setNewUpdateTimetamp(String newUpdateTimetamp) {
        this.newUpdateTimetamp = newUpdateTimetamp;
    }

    public String getOnCampusFlag() {
        return onCampusFlag;
    }

    public void setOnCampusFlag(String onCampusFlag) {
        this.onCampusFlag = onCampusFlag;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPrimeSponsorName() {
        return primeSponsorName;
    }

    public void setPrimeSponsorName(String primeSponsorName) {
        this.primeSponsorName = primeSponsorName;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }
    private String ownerFirstName;
    private String ownerLastName;
    private String personId;
    private String dateEdited;
    private String contactFirstName;
    private String contactLastName;
    private String contactPhone;
    private String contactEmail;
    private String contactFax;
    private String sponsorName;
    private String primeSponsorName;
    private String leadUnit;
    private String amount;
    private String onCampusFlag;
    private String graHeadCount;
    private String newUpdateTimetamp;
    private String mailingAddresId;





    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the discl
     */
    public String getDiscl() {
        return discl;
    }

    /**
     * @param discl the discl to set
     */
    public void setDiscl(String discl) {
        this.discl = discl;
    }

    /**
     * @return the dispositionStatus
     */
    public String getDispositionStatus() {
        return dispositionStatus;
    }

    /**
     * @param dispositionStatus the dispositionStatus to set
     */
    public void setDispositionStatus(String dispositionStatus) {
        this.dispositionStatus = dispositionStatus;
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
     * @return the disclosureEvent
     */
    public String getDisclosureEvent() {
        return disclosureEvent;
    }

    /**
     * @param disclosureEvent the disclosureEvent to set
     */
    public void setDisclosureEvent(String disclosureEvent) {
        this.disclosureEvent = disclosureEvent;
    }

    /**
     * @return the subDate
     */
    public String getSubDate() {
        return subDate;
    }

    /**
     * @param subDate the subDate to set
     */
    public void setSubDate(String subDate) {
        this.subDate = subDate;
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
     * @return the subEndDate
     */
    public String getSubEndDate() {
        return subEndDate;
    }

    /**
     * @param subEndDate the subEndDate to set
     */
    public void setSubEndDate(String subEndDate) {
        this.subEndDate = subEndDate;
    }

}
