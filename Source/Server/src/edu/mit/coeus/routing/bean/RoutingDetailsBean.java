/*
 * @(#)RoutingDetailsBean.java 1.0 01/22/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author leenababu
 */
public class RoutingDetailsBean implements Serializable, BaseBean{
    private String routingNumber;
    private int mapNumber;
    private int levelNumber;
    private int stopNumber;
    
    private int approverNumber;
    private String userId;
    private boolean primaryApproverFlag;
    private String description;
    
    private String approvalStatus;
    private Date submissionDate;
    private Date approvalDate;
    private Timestamp updateTimestamp;
    
    private CoeusVector comments;
    private CoeusVector attachments;
    private String action;
    private int approveAll;
    private String updateUser;
    
    private String updateUserName;
    private String userName;
    private String acType;
    
    /**
     * Creates a new instance of RoutingDetailsBean
     */
    public RoutingDetailsBean() {
    }
    
    /** Getter for property routingNumber.
     * @return Value of property routingNumber.
     */
    public String getRoutingNumber() {
        return routingNumber;
    }
    
    /** Setter for property routingNumber.
     * @param routingNumber New value of property routingNumber.
     */
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
    
    /** Getter for property mapNumber.
     * @return Value of property mapNumber.
     */
    public int getMapNumber() {
        return mapNumber;
    }
    
    /** Setter for property mapNumber.
     * @param mapNumber New value of property mapNumber.
     */
    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
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
    
    /** Getter for property approverNumber.
     * @return Value of property approverNumber.
     */
    public int getApproverNumber() {
        return approverNumber;
    }
    
    /** Setter for property approverNumber.
     * @param approverNumber New value of property approverNumber.
     */
    public void setApproverNumber(int approverNumber) {
        this.approverNumber = approverNumber;
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(String userId) {
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
    public String getDescription() {
        return description;
    }
    
    /** Setter for property parentMapId.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** Getter for property approvalStatus.
     * @return Value of property approvalStatus.
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }
    
    /** Setter for property approvalStatus.
     * @param approvalStatus New value of property approvalStatus.
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
    
    /** Getter for property submissionDate.
     * @return Value of property submissionDate.
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }
    
    /** Setter for property submissionDate.
     * @param submissionDate New value of property submissionDate.
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }
    
    /** Getter for property approvalDate.
     * @return Value of property approvalDate.
     */
    public Date getApprovalDate() {
        return approvalDate;
    }
    
    /** Setter for property approvalDate.
     * @param approvalDate New value of property approvalDate.
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property userName.
     * @return Value of property userName.
     */
    public String getUserName() {
        return userName;
    }
    
    /** Setter for property userName.
     * @param updateUser New value of property userName.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public CoeusVector getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(CoeusVector comments) {
        this.comments = comments;
    }
    
    /** Getter for property attachments.
     * @return Value of property attachments.
     */
    public CoeusVector getAttachments() {
        return attachments;
    }
    
    /** Setter for property attachments.
     * @param attachments New value of property attachments.
     */
    public void setAttachments(CoeusVector attachments) {
        this.attachments = attachments;
    }
    
    /** Getter for property action.
     * @return Value of property action.
     */
    public String getAction() {
        return action;
    }
    
    /** Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(String action) {
        this.action = action;
    }
    
    /** Getter for property approveAll.
     * @return Value of property approveAll.
     */
    public int getApproveAll() {
        return approveAll;
    }
    
    /** Setter for property approveAll.
     * @param comments New value of property approveAll.
     */
    public void setApproveAll(int approveAll) {
        this.approveAll = approveAll;
    }
    
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    /** Setter for property updateUserName.
     * @param comments New value of property updateUserName.
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
}
