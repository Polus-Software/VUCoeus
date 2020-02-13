/**
 * @(#)ProposalApprovalBean.java 1.0 01/02/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;
import java.util.Vector;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to set and get Proposal Approvals
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 02, 2004 11:30 PM
 */

public class ProposalApprovalBean implements java.io.Serializable, BaseBean{
    
    private String proposalNumber;
    private int mapId;
    private int levelNumber;
    private int stopNumber;
    private String userId;
    private boolean primaryApproverFlag;  
    private String description;
    private String approvalStatus;    
    private java.sql.Date approvalDate;
    private java.sql.Date submissionDate;
    private String comments;    
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    private int approvalAll;
    private String action;
    private String userName;
    //Added for userid to username enhancement
    private String updateUserName;
     //Added for Coeus 4.3 enhancement PT ID:2785 - start
    private String approverNumber;
     //Added for Coeus 4.3 enhancement PT ID:2785 - end
    /**
     *	Default Constructor
     */
    
    public ProposalApprovalBean(){
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
    
    /** Getter for property mapId.
     * @return Value of property mapId.
     */
    public int getMapId() {
        return mapId;
    }
    
    /** Setter for property mapId.
     * @param mapId New value of property mapId.
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    
    /** Getter for property levelNumber.
     * @return Value of property levelNumber.
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /** Setter for property levelNumber.
     * @param levelNumber New value of property levelNumber.
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    /** Getter for property stopNumber.
     * @return Value of property stopNumber.
     */
    public int getStopNumber() {
        return stopNumber;
    }
    
    /** Setter for property stopNumber.
     * @param stopNumber New value of property stopNumber.
     */
    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /** Getter for property primaryApproverFlag.
     * @return Value of property primaryApproverFlag.
     */
    public boolean isPrimaryApproverFlag() {
        return primaryApproverFlag;
    }
    
    /** Setter for property primaryApproverFlag.
     * @param primaryApproverFlag New value of property primaryApproverFlag.
     */
    public void setPrimaryApproverFlag(boolean primaryApproverFlag) {
        this.primaryApproverFlag = primaryApproverFlag;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property approvalStatus.
     * @return Value of property approvalStatus.
     */
    public java.lang.String getApprovalStatus() {
        return approvalStatus;
    }
    
    /** Setter for property approvalStatus.
     * @param approvalStatus New value of property approvalStatus.
     */
    public void setApprovalStatus(java.lang.String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    
    /** Getter for property approvalDate.
     * @return Value of property approvalDate.
     */
    public java.sql.Date getApprovalDate() {
        return approvalDate;
    }
    
    /** Setter for property approvalDate.
     * @param approvalDate New value of property approvalDate.
     */
    public void setApprovalDate(java.sql.Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    /** Getter for property submissionDate.
     * @return Value of property submissionDate.
     */
    public java.sql.Date getSubmissionDate() {
        return submissionDate;
    }
    
    /** Setter for property submissionDate.
     * @param submissionDate New value of property submissionDate.
     */
    public void setSubmissionDate(java.sql.Date submissionDate) {
        this.submissionDate = submissionDate;
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
    
    /** Getter for property approvalAll.
     * @return Value of property approvalAll.
     */
    public int getApprovalAll() {
        return approvalAll;
    }
    
    /** Setter for property approvalAll.
     * @param approvalAll New value of property approvalAll.
     */
    public void setApprovalAll(int approvalAll) {
        this.approvalAll = approvalAll;
    }
    
    /** Getter for property action.
     * @return Value of property action.
     */
    public java.lang.String getAction() {
        return action;
    }
    
    /** Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(java.lang.String action) {
        this.action = action;
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
    
    //UserId to Username Enhancement - Start
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUserName New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //UserId to Username Enhancement - End
    //Added for Coeus 4.3 enhancement PT ID:2785 - start
    /** Getter for property approverNumber.
     * @return Value of property approverNumber.
     */
    public String getApproverNumber() {
        return approverNumber;
    }

    /** Setter for property approverNumber.
     * @param approverNumber New value of property approverNumber.
     */
    public void setApproverNumber(String approverNumber) {
        this.approverNumber = approverNumber;
    }
     //Added for Coeus 4.3 enhancement PT ID:2785 - end
}