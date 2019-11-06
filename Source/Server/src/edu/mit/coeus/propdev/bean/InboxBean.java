/**
 * @(#)InboxBean.java 1.0 12/22/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;

/**
 * This class used to set and get Message details
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on December 22, 2003 10:30 AM
 */

public class InboxBean implements java.io.Serializable, BaseBean{
    private String proposalNumber;
    private String toUser;
    private String messageId;
    private String fromUser;
    private Timestamp arrivalDate;
    private char subjectType;
    private char openedFlag;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    private String userName;
    private String aw_ProposalNumber;
    private int aw_MessageId;
    private String aw_ToUser;
    private Timestamp aw_ArrivalDate;
    private Date proposalDeadLineDate;
    private String proposalTitle;
    private String sponsorCode;
    private String sponsorName;
    private Timestamp sysDate;
    private String unitNumber;
    private String unitName;
    private String personName;
    private int creationStatus;
    private String creationStatusDescription;
    //Added by shiji for Coeus enhancement Case #1828 : step 1 : start
    private String Module;
    private String item;
    //Coeus enhancement Case #1828 : step 1 : end
    /* CASE #748 Begin */
    private String subjectDescription;
    /* CASE #748 End */    
    
    /* CASE #1828 Begin */
    private int moduleCode;
    /* CASE #1828 End */    
    
    //holds message bean
    private MessageBean messageBean;
    /**
     *	Default Constructor
     */
    
    public InboxBean(){
    }    
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param propoaslNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property toUser.
     * @return Value of property toUser.
     */
    public java.lang.String getToUser() {
        return toUser;
    }
    
    /** Setter for property toUser.
     * @param toUser New value of property toUser.
     */
    public void setToUser(java.lang.String toUser) {
        this.toUser = toUser;
    }
    
    /** Getter for property messageId.
     * @return Value of property messageId.
     */
    public String getMessageId() {
        return messageId;
    }
    
    /** Setter for property messageId.
     * @param messageId New value of property messageId.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    /** Getter for property fromUser.
     * @return Value of property fromUser.
     */
    public java.lang.String getFromUser() {
        return fromUser;
    }
    
    /** Setter for property fromUser.
     * @param fromUser New value of property fromUser.
     */
    public void setFromUser(java.lang.String fromUser) {
        this.fromUser = fromUser;
    }
    
    /** Getter for property arrivalDate.
     * @return Value of property arrivalDate.
     */
    public Timestamp getArrivalDate() {
        return arrivalDate;
    }
    
    /** Setter for property arrivalDate.
     * @param arrivalDate New value of property arrivalDate.
     */
    public void setArrivalDate(Timestamp arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    
    /** Getter for property subjectType.
     * @return Value of property subjectType.
     */
    public char getSubjectType() {
        return subjectType;
    }
    
    /** Setter for property subjectType.
     * @param subjectType New value of property subjectType.
     */
    public void setSubjectType(char subjectType) {
        this.subjectType = subjectType;
    }
    
    /** Getter for property openedFlag.
     * @return Value of property openedFlag.
     */
    public char getOpenedFlag() {
        return openedFlag;
    }
    
    /** Setter for property openedFlag.
     * @param openedFlag New value of property openedFlag.
     */
    public void setOpenedFlag(char openedFlag) {
        this.openedFlag = openedFlag;
    }
    
    /** Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /** Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.sql.Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }   
    
    /** Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }
    
    /** Setter for property userName.
     * @param userName New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }
    
    /** Getter for property aw_ProposalNumber.
     * @return Value of property aw_ProposalNumber.
     */
    public java.lang.String getAw_ProposalNumber() {
        return aw_ProposalNumber;
    }
    
    /** Setter for property aw_ProposalNumber.
     * @param aw_ProposalNumber New value of property aw_ProposalNumber.
     */
    public void setAw_ProposalNumber(java.lang.String aw_ProposalNumber) {
        this.aw_ProposalNumber = aw_ProposalNumber;
    }
    
    /** Getter for property aw_MessageId.
     * @return Value of property aw_MessageId.
     */
    public int getAw_MessageId() {
        return aw_MessageId;
    }
    
    /** Setter for property aw_MessageId.
     * @param aw_MessageId New value of property aw_MessageId.
     */
    public void setAw_MessageId(int aw_MessageId) {
        this.aw_MessageId = aw_MessageId;
    }
    
    /** Getter for property aw_ArrivalDate.
     * @return Value of property aw_ArrivalDate.
     */
    public Timestamp getAw_ArrivalDate() {
        return aw_ArrivalDate;
    }
    
    /** Setter for property aw_ArrivalDate.
     * @param aw_ArrivalDate New value of property aw_ArrivalDate.
     */
    public void setAw_ArrivalDate(Timestamp aw_ArrivalDate) {
        this.aw_ArrivalDate = aw_ArrivalDate;
    }
    
    /** Getter for property aw_ToUser.
     * @return Value of property aw_ToUser.
     */
    public java.lang.String getAw_ToUser() {
        return aw_ToUser;
    }
    
    /** Setter for property aw_ToUser.
     * @param aw_ToUser New value of property aw_ToUser.
     */
    public void setAw_ToUser(java.lang.String aw_ToUser) {
        this.aw_ToUser = aw_ToUser;
    }    
    
    /** Getter for property proposalDeadLineDate.
     * @return Value of property proposalDeadLineDate.
     */
    public java.sql.Date getProposalDeadLineDate() {
        return proposalDeadLineDate;
    }
    
    /** Setter for property proposalDeadLineDate.
     * @param proposalDeadLineDate New value of property proposalDeadLineDate.
     */
    public void setProposalDeadLineDate(java.sql.Date proposalDeadLineDate) {
        this.proposalDeadLineDate = proposalDeadLineDate;
    }
    
    /** Getter for property proposalTitle.
     * @return Value of property proposalTitle.
     */
    public java.lang.String getProposalTitle() {
        return proposalTitle;
    }
    
    /** Setter for property proposalTitle.
     * @param proposalTitle New value of property proposalTitle.
     */
    public void setProposalTitle(java.lang.String proposalTitle) {
        this.proposalTitle = proposalTitle;
    }
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /** Getter for property sysDate.
     * @return Value of property sysDate.
     */
    public java.sql.Timestamp getSysDate() {
        return sysDate;
    }
    
    /** Setter for property sysDate.
     * @param sysDate New value of property sysDate.
     */
    public void setSysDate(java.sql.Timestamp sysDate) {
        this.sysDate = sysDate;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property creationStatus.
     * @return Value of property creationStatus.
     */
    public int getCreationStatus() {
        return creationStatus;
    }
    
    /** Setter for property creationStatus.
     * @param creationStatus New value of property creationStatus.
     */
    public void setCreationStatus(int creationStatus) {
        this.creationStatus = creationStatus;
    }    
    
    /** Getter for property messageBean.
     * @return Value of property messageBean.
     */
    public MessageBean getMessageBean() {
        return messageBean;
    }
    
    /** Setter for property messageBean.
     * @param messageBean New value of property messageBean.
     */
    public void setMessageBean(MessageBean messageBean) {
        this.messageBean = messageBean;
    }    
    
    /** Getter for property creationStatusDescription.
     * @return Value of property creationStatusDescription.
     */
    public java.lang.String getCreationStatusDescription() {
        return creationStatusDescription;
    }
    
    /** Setter for property creationStatusDescription.
     * @param creationStatusDescription New value of property creationStatusDescription.
     */
    public void setCreationStatusDescription(java.lang.String creationStatusDescription) {
        this.creationStatusDescription = creationStatusDescription;
    } 
    
    /* CASE #748 Begin */
    /**
     * Getter for property subjectDescription.
     * @return subjectDescription
     */
    public String getSubjectDescription(){
        return subjectDescription;
    }

    /**
     * Setter for property subjectDescription.
     * @param subjectDescription
     */
    public void setSubjectDescription(String subjectDescription){
        this.subjectDescription = subjectDescription;
    }
    
    /* CASE #748 End */  
    
    
    /* CASE #1828 Start */
    
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
     //Added by shiji for Coeus enhancement Case #1828 : step 2 : start
    /**
     * Getter for property Module.
     * @return Value of property Module.
     */
    public java.lang.String getModule() {
        return Module;
    }
    
    /**
     * Setter for property Module.
     * @param Module New value of property Module.
     */
    public void setModule(java.lang.String Module) {
        this.Module = Module;
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
    //Coeus enhancement Case #1828 : step 2 : end
    /* CASE #1828 End */    
}