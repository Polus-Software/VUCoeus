/*
 * EPSProposalHeaderBean.java
 *
 * Created on July 11, 2006, 6:03 PM
 */

/* PMD check performed, and commented unused imports and variables on 29-MARCH-2007
 * by Nandkumar S N
 */
package edu.utk.coeuslite.propdev.bean;

import java.sql.Date;
//import java.sql.Timestamp;

/**
 *
 * @author  divyasusendran
 */
public class EPSProposalHeaderBean implements java.io.Serializable{
    private String proposalNumber;
    private String title;
    private String personId;
    private String personName;
    private String proposalStatusCode;
    private String proposalStatusDescription;
    private String sponsorCode;
    private String sponsorName;
    private Date  proposalStartDate;
    private Date proposalEndDate;
    private String unitNumber;
    private String unitName;
    private String updateTimestamp;
    private String updateUser;
    private String createTimestamp;
    private String createUser;
    private String leadUnitNumber;
    private String isHierarchy;
    private String isParent;
    // Added for displaying user name for user Id
    private String updateUserName;
    private String createUserName;
    // End   
    //Added for case#2959 - Proposal Lead Unit Missing in Lite
    private String leadUnitName;
    
    /** Creates a new instance of EPSProposalHeaderBean */
    public EPSProposalHeaderBean() {
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    
    
    /**
     * Getter for property proposalStatusDescription.
     * @return Value of property proposalStatusDescription.
     */
    public java.lang.String getProposalStatusDescription() {
        return proposalStatusDescription;
    }
    
    /**
     * Setter for property proposalStatusDescription.
     * @param proposalStatusDescription New value of property proposalStatusDescription.
     */
    public void setProposalStatusDescription(java.lang.String proposalStatusDescription) {
        this.proposalStatusDescription = proposalStatusDescription;
    }
    
    /**
     * Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /**
     * Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /**
     * Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /**
     * Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
   
    
    /**
     * Getter for property proposalEndDate.
     * @return Value of property proposalEndDate.
     */
    public java.sql.Date getProposalEndDate() {
        return proposalEndDate;
    }
    
    /**
     * Setter for property proposalEndDate.
     * @param proposalEndDate New value of property proposalEndDate.
     */
    public void setProposalEndDate(java.sql.Date proposalEndDate) {
        this.proposalEndDate = proposalEndDate;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /**
     * Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }   
    
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property proposalStartDate.
     * @return Value of property proposalStartDate.
     */
    public java.sql.Date getProposalStartDate() {
        return proposalStartDate;
    }
    
    /**
     * Setter for property proposalStartDate.
     * @param proposalStartDate New value of property proposalStartDate.
     */
    public void setProposalStartDate(java.sql.Date proposalStartDate) {
        this.proposalStartDate = proposalStartDate;
    }
    
    /**
     * Getter for property proposalStatusCode.
     * @return Value of property proposalStatusCode.
     */
    public java.lang.String getProposalStatusCode() {
        return proposalStatusCode;
    }
    
    /**
     * Setter for property proposalStatusCode.
     * @param proposalStatusCode New value of property proposalStatusCode.
     */
    public void setProposalStatusCode(java.lang.String proposalStatusCode) {
        this.proposalStatusCode = proposalStatusCode;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.lang.String getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.lang.String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property createTimestamp.
     * @return Value of property createTimestamp.
     */
    public java.lang.String getCreateTimestamp() {
        return createTimestamp;
    }
    
    /**
     * Setter for property createTimestamp.
     * @param createTimestamp New value of property createTimestamp.
     */
    public void setCreateTimestamp(java.lang.String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    /**
     * Getter for property createUser.
     * @return Value of property createUser.
     */
    public java.lang.String getCreateUser() {
        return createUser;
    }
    
    /**
     * Setter for property createUser.
     * @param createUser New value of property createUser.
     */
    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }
    
    /**
     * Getter for property leadUnitNumber.
     * @return Value of property leadUnitNumber.
     */
    public java.lang.String getLeadUnitNumber() {
        return leadUnitNumber;
    }
    
    /**
     * Setter for property leadUnitNumber.
     * @param leadUnitNumber New value of property leadUnitNumber.
     */
    public void setLeadUnitNumber(java.lang.String leadUnitNumber) {
        this.leadUnitNumber = leadUnitNumber;
    }
    
    /**
     * Getter for property isHierarchy.
     * @return Value of property isHierarchy.
     */
    public java.lang.String getIsHierarchy() {
        return isHierarchy;
    }
    
    /**
     * Setter for property isHierarchy.
     * @param isHierarchy New value of property isHierarchy.
     */
    public void setIsHierarchy(java.lang.String isHierarchy) {
        this.isHierarchy = isHierarchy;
    }
    
    /**
     * Getter for property isParent.
     * @return Value of property isParent.
     */
    public java.lang.String getIsParent() {
        return isParent;
    }
    
    /**
     * Setter for property isParent.
     * @param isParent New value of property isParent.
     */
    public void setIsParent(java.lang.String isParent) {
        this.isParent = isParent;
    }
    
    // Added for displaying user name for user Id
    /**
     * Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
    /**
     * Getter for property createUserName.
     * @return Value of property createUserName.
     */
    public java.lang.String getCreateUserName() {
        return createUserName;
    }
    
    /**
     * Setter for property createUserName.
     * @param createUserName New value of property createUserName.
     */
    public void setCreateUserName(java.lang.String createUserName) {
        this.createUserName = createUserName;
    }
    // End

    //Added for case#2959 - Proposal Lead Unit Missing in Lite - start
    public String getLeadUnitName() {
        return leadUnitName;
    }

    public void setLeadUnitName(String leadUnitName) {
        this.leadUnitName = leadUnitName;
    }
    //Added for case#2959 - Proposal Lead Unit Missing in Lite - end
}
