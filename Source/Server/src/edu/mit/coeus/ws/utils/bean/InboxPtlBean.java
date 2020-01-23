/*
 * InboxBean.java
 *
 * Created on August 22, 2006, 2:43 PM
 */

package edu.mit.coeus.ws.utils.bean;

import java.util.Calendar;


/**
 *
 * @author  geot
 */
public class InboxPtlBean implements java.io.Serializable{

    private String proposalNumber;
    private String toUser;
    private String fromUser;
    private Calendar arrivalDate;
    private String subjectType;
    private String openedFlag;
    private String updateUser;
    private String userName;
    private Calendar proposalDeadLineDate;
    private String proposalTitle;
    private String sponsorCode;
    private String sponsorName;
    private String unitNumber;
    private String unitName;
    private String personName;
    private int statusCode;
    private String status;
    private int moduleCode;
    private String module;
    private String item;
    private String subject;
    private String messageId;
    private String message;
    private String linkURL;
    /**
     *	Default Constructor
     */
    
    
    /** Creates a new instance of InboxBean */
    public InboxPtlBean() {
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
     * Getter for property toUser.
     * @return Value of property toUser.
     */
    public java.lang.String getToUser() {
        return toUser;
    }
    
    /**
     * Setter for property toUser.
     * @param toUser New value of property toUser.
     */
    public void setToUser(java.lang.String toUser) {
        this.toUser = toUser;
    }
    
    /**
     * Getter for property arrivalDate.
     * @return Value of property arrivalDate.
     */
    public java.util.Calendar getArrivalDate() {
        return arrivalDate;
    }
    
    /**
     * Setter for property arrivalDate.
     * @param arrivalDate New value of property arrivalDate.
     */
    public void setArrivalDate(java.util.Calendar arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    
    /**
     * Getter for property fromUser.
     * @return Value of property fromUser.
     */
    public java.lang.String getFromUser() {
        return fromUser;
    }
    
    /**
     * Setter for property fromUser.
     * @param fromUser New value of property fromUser.
     */
    public void setFromUser(java.lang.String fromUser) {
        this.fromUser = fromUser;
    }
    
    /**
     * Getter for property subjectType.
     * @return Value of property subjectType.
     */
    public String getSubjectType() {
        return subjectType;
    }
    
    /**
     * Setter for property subjectType.
     * @param subjectType New value of property subjectType.
     */
    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }
    
    /**
     * Getter for property openedFlag.
     * @return Value of property openedFlag.
     */
    public String getOpenedFlag() {
        return openedFlag;
    }
    
    /**
     * Setter for property openedFlag.
     * @param openedFlag New value of property openedFlag.
     */
    public void setOpenedFlag(String openedFlag) {
        this.openedFlag = openedFlag;
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
     * Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /**
     * Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    /**
     * Getter for property proposalDeadLineDate.
     * @return Value of property proposalDeadLineDate.
     */
    public java.util.Calendar getProposalDeadLineDate() {
        return proposalDeadLineDate;
    }
    
    /**
     * Setter for property proposalDeadLineDate.
     * @param proposalDeadLineDate New value of property proposalDeadLineDate.
     */
    public void setProposalDeadLineDate(java.util.Calendar proposalDeadLineDate) {
        this.proposalDeadLineDate = proposalDeadLineDate;
    }
    
    /**
     * Getter for property proposalTitle.
     * @return Value of property proposalTitle.
     */
    public java.lang.String getProposalTitle() {
        return proposalTitle;
    }
    
    /**
     * Setter for property proposalTitle.
     * @param proposalTitle New value of property proposalTitle.
     */
    public void setProposalTitle(java.lang.String proposalTitle) {
        this.proposalTitle = proposalTitle;
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
     * Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /**
     * Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
     * Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public int getModuleCode() {
        return moduleCode;
    }
    
    /**
     * Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /**
     * Getter for property module.
     * @return Value of property module.
     */
    public java.lang.String getModule() {
        return module;
    }
    
    /**
     * Setter for property module.
     * @param module New value of property module.
     */
    public void setModule(java.lang.String module) {
        this.module = module;
    }
    
    /**
     * Getter for property item.
     * @return Value of property item.
     */
    public java.lang.String getItem() {
        return item;
    }
    
    /**
     * Setter for property item.
     * @param item New value of property item.
     */
    public void setItem(java.lang.String item) {
        this.item = item;
    }
    
    /**
     * Getter for property subject.
     * @return Value of property subject.
     */
    public java.lang.String getSubject() {
        return subject;
    }
    
    /**
     * Setter for property subject.
     * @param subject New value of property subject.
     */
    public void setSubject(java.lang.String subject) {
        this.subject = subject;
    }
    
    /**
     * Getter for property messageId.
     * @return Value of property messageId.
     */
    public java.lang.String getMessageId() {
        return messageId;
    }
    
    /**
     * Setter for property messageId.
     * @param messageId New value of property messageId.
     */
    public void setMessageId(java.lang.String messageId) {
        this.messageId = messageId;
    }
    
    /**
     * Getter for property message.
     * @return Value of property message.
     */
    public java.lang.String getMessage() {
        return message;
    }
    
    /**
     * Setter for property message.
     * @param message New value of property message.
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }
    
    /**
     * Getter for property linkURL.
     * @return Value of property linkURL.
     */
    public java.lang.String getLinkURL() {
        return linkURL;
    }
    
    /**
     * Setter for property linkURL.
     * @param linkURL New value of property linkURL.
     */
    public void setLinkURL(java.lang.String linkURL) {
        this.linkURL = linkURL;
    }
    
    /**
     * Getter for property message.
     * @return Value of property message.
     */
//    public java.lang.String getMessage() {
//        return message;
//    }
//    
//    /**
//     * Setter for property message.
//     * @param message New value of property message.
//     */
//    public void setMessage(java.lang.String message) {
//        this.message = message;
//    }
    
}
