/*
 * @(#)ProtocolRiskLevelBean.java 1.0 04/22/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.IBaseDataBean;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * The class used to hold the information of <code>Protocol Risk Level</code>
 *
 * @author leenababu
 */
public class ProtocolRiskLevelBean implements java.io.Serializable, IBaseDataBean, BaseBean {
    private String protocolNumber;
    private int sequenceNumber;
    private String riskLevelCode;
    private String comments;
    
    private String status;
    private String updateUser;
    private String acType;
    
    private Date dateAssigned;
    private Date dateUpdated;
    private Timestamp updateTimestamp;
    /** Creates a new instance of ProtocolRiskLevelBean */
    public ProtocolRiskLevelBean() {
    }
    
    /**
     * Method used to get the Protocol Number 
     * @return protocolNumber int
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }
    
    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    /**
     * Method used to get the risk level code
     * @return riskLevelCode String
     */
    public String getRiskLevelCode() {
        return riskLevelCode;
    }
    
    /**
     * Method used to set the risk level code
     * @param riskLevelCode String
     */
    public void setRiskLevelCode(String riskLevelCode) {
        this.riskLevelCode = riskLevelCode;
    }
    
    /**
     * Method used to get the comments
     * @return comments String
     */
    public String getComments() {
        return comments;
    }
    
    /**
     * Method used to set the comments
     * @param comments String
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    /**
     * Method used to get the status
     * @return status String
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Method used to set the status
     * @param status String
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Method used to get the update user
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Method used to set the update user
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the date assigned
     * @return dateAssigned Date
     */
    public Date getDateAssigned() {
        return dateAssigned;
    }
    
    /**
     * Method used to set the date assigned
     * @param dateAssigned Date
     */
    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }
    
    /**
     * Method used to get the date updated
     * @return dateUpdated Date
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }
    
    /**
     * Method used to set the dateUpdated
     * @param dateUpdated Date
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the acType
     * @return acType String
     */
    public String getAcType() {
        return acType;
    }
    
    /**
     * Method used to set the acType
     * @param acType String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
