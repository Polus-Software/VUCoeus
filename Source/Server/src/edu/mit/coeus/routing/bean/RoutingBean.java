/*
 * @(#)RoutingBean.java 1.0 01/22/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;

/**
 *
 * @author leenababu
 */
public class RoutingBean implements Serializable, BaseBean {
    private int moduleCode;
    private String moduleItemKey;
    private int moduleItemKeySequence;
    private int approvalSequence;
    
    private String routingNumber;
    private String routingStartDate;
    private String routingEndDate;
    private String routingStartUser;
    
    private String routingEndUser;
    private String acType;
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    private java.sql.Timestamp endTimestamp;
    private String comments;
    //COEUSQA-1433 - Allow Recall from Routing - End
    
    /** Creates a new instance of RoutingBean */
    public RoutingBean() {
    }
    
    /** Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public int getModuleCode() {
        return moduleCode;
    }
    
    /** Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /** Getter for property moduleItemKey.
     * @return Value of property moduleItemKey.
     */
    public String getModuleItemKey() {
        return moduleItemKey;
    }
    
    /** Setter for property moduleItemKey.
     * @param moduleItemKey New value of property moduleItemKey.
     */
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }
    
    /** Getter for property moduleItemKeySequence.
     * @return Value of property moduleItemKeySequence.
     */
    public int getModuleItemKeySequence() {
        return moduleItemKeySequence;
    }
    
    /** Setter for property moduleItemKeySequence.
     * @param moduleItemKeySequence New value of property moduleItemKeySequence.
     */
    public void setModuleItemKeySequence(int moduleItemKeySequence) {
        this.moduleItemKeySequence = moduleItemKeySequence;
    }
    
    /** Getter for property approvalSequence.
     * @return Value of property approvalSequence.
     */
    public int getApprovalSequence() {
        return approvalSequence;
    }
    
    /** Setter for property approvalSequence.
     * @param approvalSequence New value of property approvalSequence.
     */
    public void setApprovalSequence(int approvalSequence) {
        this.approvalSequence = approvalSequence;
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
    
    /** Getter for property routingStartDate.
     * @return Value of property routingStartDate.
     */
    public String getRoutingStartDate() {
        return routingStartDate;
    }
    
    /** Setter for property routingStartDate.
     * @param routingStartDate New value of property routingStartDate.
     */
    public void setRoutingStartDate(String routingStartDate) {
        this.routingStartDate = routingStartDate;
    }
    
    /** Getter for property routingEndDate.
     * @return Value of property routingEndDate.
     */
    public String getRoutingEndDate() {
        return routingEndDate;
    }
    
    /** Setter for property routingEndDate.
     * @param routingEndDate New value of property routingEndDate.
     */
    public void setRoutingEndDate(String routingEndDate) {
        this.routingEndDate = routingEndDate;
    }
    
    /** Getter for property routingStartUser.
     * @return Value of property routingStartUser.
     */
    public String getRoutingStartUser() {
        return routingStartUser;
    }
    
    /** Setter for property routingStartUser.
     * @param routingStartUser New value of property routingStartUser.
     */
    public void setRoutingStartUser(String routingStartUser) {
        this.routingStartUser = routingStartUser;
    }
    
    /** Getter for property routingEndUser.
     * @return Value of property routingEndUser.
     */
    public String getRoutingEndUser() {
        return routingEndUser;
    }
    
    /** Setter for property routingEndUser.
     * @param routingEndUser New value of property routingEndUser.
     */
    public void setRoutingEndUser(String routingEndUser) {
        this.routingEndUser = routingEndUser;
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
    
    //COEUSQA-1433 - Allow Recall from Routing - Start
    /** Getter for property routingEndDate.
     * @return Value of property routingEndDate.
     */
    public java.sql.Timestamp getRecallEndDate() {
        return endTimestamp;
    }
    
    /** Setter for property routingEndDate.
     * @param routingEndDate New value of property routingEndDate.
     */
    public void setRecallEndDate(java.sql.Timestamp endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public String getRoutingComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setRoutingComments(String comments) {
        this.comments = comments;
    }
    //COEUSQA-1433 - Allow Recall from Routing - End
}
