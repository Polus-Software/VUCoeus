/**
 * @(#)ProposalApprovalMapBean.java 1.0 01/02/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;
import java.util.Vector;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;
/**
 * This class is used to set and get Proposal Approval Maps
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 02, 2004 11:30 PM
 */

public class ProposalApprovalMapBean implements java.io.Serializable, BaseBean{
    
    private String proposalNumber;
    private int mapId;
    private int parentMapId;    
    private String description;
    private boolean systemFlag;
    private String approvalStatus;    
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    private CoeusVector proposalApprovals;
    
    /**
     *	Default Constructor
     */
    
    public ProposalApprovalMapBean(){
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
    
    /** Getter for property parentMapId.
     * @return Value of property parentMapId.
     */
    public int getParentMapId() {
        return parentMapId;
    }
    
    /** Setter for property parentMapId.
     * @param parentMapId New value of property parentMapId.
     */
    public void setParentMapId(int parentMapId) {
        this.parentMapId = parentMapId;
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
    
    /** Getter for property systemFlag.
     * @return Value of property systemFlag.
     */
    public boolean isSystemFlag() {
        return systemFlag;
    }
    
    /** Setter for property systemFlag.
     * @param systemFlag New value of property systemFlag.
     */
    public void setSystemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
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
    
    /** Getter for property proposalApprovals.
     * @return Value of property proposalApprovals.
     */
    public CoeusVector getProposalApprovals() {
        return proposalApprovals;
    }
    
    /** Setter for property proposalApprovals.
     * @param proposalApprovals New value of property proposalApprovals.
     */
    public void setProposalApprovals(CoeusVector proposalApprovals) {
        this.proposalApprovals = proposalApprovals;
    }
    
}