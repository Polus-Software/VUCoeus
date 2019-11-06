/*
 * @(#)InstituteProposalLogBean.java 1.0 May 10, 2004, 2:58 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.utils.CoeusVector;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.CoeusBean;

/**
 * The class used to hold the information of <code>Proposal Log</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on May 10, 2004, 2:58 PM
 */

public class InstituteProposalLogBean implements java.io.Serializable, CoeusBean {

    //holds the proposal number
    private String proposalNumber;
    //holds the proposal type code    
    private int proposalTypeCode ;
    //holds the proposal type Description
    private String proposalTypeDescription;
    //holds the title
    private String title;
    //holds PI Id
    private String principleInvestigatorId;
    //holds PI Id
    private String principleInvestigatorName;    
    //holds non Mit Person flag
    private boolean nonMITPersonFlag;
    //holds lead unit
    private String leadUnit;   
    //holds Unit Name
    private String unitName;   
    //holds Sponsor Code
    private String sponsorCode;
    //holds sponsor name
    private String sponsorName;
    //holds Log Status
    private char logStatus;
    //holds comments
    private String comments;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    //holds update userName
    private String userName;
    //holds type of Log - Temporary or Normal Proposal Log
    private char logType;
    //holds faculty flag
    private boolean facultyFlag;
    
    //case 3263 start
    private String createUser;
    private String createUserName;
    private Timestamp createTimestamp;
    private Date deadlineDate;    
    //case 3263 end
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    private CoeusVector mergedData;
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
    
    //Created new InstituteProposalLogBean
    public InstituteProposalLogBean(){
    }            

    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }    
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property proposalTypeCode.
     * @return Value of property proposalTypeCode.
     */
    public int getProposalTypeCode() {
        return proposalTypeCode;
    }
    
    /** Setter for property proposalTypeCode.
     * @param proposalTypeCode New value of property proposalTypeCode.
     */
    public void setProposalTypeCode(int proposalTypeCode) {
        this.proposalTypeCode = proposalTypeCode;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property principleInvestigatorId.
     * @return Value of property principleInvestigatorId.
     */
    public java.lang.String getPrincipleInvestigatorId() {
        return principleInvestigatorId;
    }
    
    /** Setter for property principleInvestigatorId.
     * @param principleInvestigatorId New value of property principleInvestigatorId.
     */
    public void setPrincipleInvestigatorId(java.lang.String principleInvestigatorId) {
        this.principleInvestigatorId = principleInvestigatorId;
    }
    
    /** Getter for property principleInvestigatorName.
     * @return Value of property principleInvestigatorName.
     */
    public java.lang.String getPrincipleInvestigatorName() {
        return principleInvestigatorName;
    }
    
    /** Setter for property principleInvestigatorName.
     * @param principleInvestigatorName New value of property principleInvestigatorName.
     */
    public void setPrincipleInvestigatorName(java.lang.String principleInvestigatorName) {
        this.principleInvestigatorName = principleInvestigatorName;
    }
    
    /** Getter for property nonMITPersonFlag.
     * @return Value of property nonMITPersonFlag.
     */
    public boolean isNonMITPersonFlag() {
        return nonMITPersonFlag;
    }
    
    /** Setter for property nonMITPersonFlag.
     * @param nonMITPersonFlag New value of property nonMITPersonFlag.
     */
    public void setNonMITPersonFlag(boolean nonMITPersonFlag) {
        this.nonMITPersonFlag = nonMITPersonFlag;
    }
    
    /** Getter for property leadUnit.
     * @return Value of property leadUnit.
     */
    public java.lang.String getLeadUnit() {
        return leadUnit;
    }
    
    /** Setter for property leadUnit.
     * @param leadUnit New value of property leadUnit.
     */
    public void setLeadUnit(java.lang.String leadUnit) {
        this.leadUnit = leadUnit;
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
    
    /** Getter for property logStatus.
     * @return Value of property logStatus.
     */
    public char getLogStatus() {
        return logStatus;
    }
    
    /** Setter for property logStatus.
     * @param logStatus New value of property logStatus.
     */
    public void setLogStatus(char logStatus) {
        this.logStatus = logStatus;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
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
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
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
    
    /** Getter for property proposalTypeDescription.
     * @return Value of property proposalTypeDescription.
     */
    public java.lang.String getProposalTypeDescription() {
        return proposalTypeDescription;
    }
    
    /** Setter for property proposalTypeDescription.
     * @param proposalTypeDescription New value of property proposalTypeDescription.
     */
    public void setProposalTypeDescription(java.lang.String proposalTypeDescription) {
        this.proposalTypeDescription = proposalTypeDescription;
    }   
    
    /** Getter for property logType.
     * @return Value of property logType.
     */
    public char getLogType() {
        return logType;
    }
    
    /** Setter for property logType.
     * @param logType New value of property logType.
     */
    public void setLogType(char logType) {
        this.logType = logType;
    }
    
    /** Getter for property facultyFlag.
     * @return Value of property facultyFlag.
     *
     */
    public boolean isFacultyFlag() {
        return facultyFlag;
    }
    
    /** Setter for property facultyFlag.
     * @param facultyFlag New value of property facultyFlag.
     *
     */
    public void setFacultyFlag(boolean facultyFlag) {
        this.facultyFlag = facultyFlag;
    }    
    
    //case 3263 start    
    public java.lang.String getCreateUser() {
        return createUser;
    }    
   
    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }    
   
    public java.lang.String getCreateUserName() {
        return createUserName;
    }
    
    public void setCreateUserName(java.lang.String createUserName) {
        this.createUserName = createUserName;
    }  
    
    public java.sql.Timestamp getCreateTimestamp() {
        return createTimestamp;
    }    
    
    public void setCreateTimestamp(java.sql.Timestamp createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
    
    public java.sql.Date getDeadlineDate() {
        return deadlineDate;
    }    
    
    public void setDeadlineDate(java.sql.Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    //case 3263 end

    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_start
    /*Method to get merged data
     *@return mergedData
     */
    public CoeusVector getMergedData() {
        return mergedData;
    }

    /*Method to set merged data
     *@param mergedData
     */
    public void setMergedData(CoeusVector mergedData) {
        this.mergedData = mergedData;
    }
    // Added for COEUSQA-1471_show institute proposal for merged proposal logs_end
}