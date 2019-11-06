/*
 * @(#)ProtocolActionChangesBean.java October 21, 2003, 11:21 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.beans.*;
import java.sql.Date;
import java.util.*;

/**
 * The class used to hold the information of <code>Protocol Actions</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on October 21, 2003, 11:21 AM
 */

public class ProtocolActionChangesBean implements java.io.Serializable {
    
    //holds protocol number
    private String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds submissionNumber 
    private int submissionNumber;
    //holds protocolStatus 
    private int protocolStatusCode;
    //holds protocolStatusDescription
    private String protocolStatusDescription;
    //holds submission status
    private int submissionStatusCode;
    //holds submissionStatusDescription
    private String submissionStatusDescription;
    //holds submissionTypeCode
    private int submissionTypeCode;
    //holds submissionTypeDescription
    private String submissionTypeDescription;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;   
    //holds Committee Id
    private String committeeId;
    //holds Schedule Date
    private Date scheduleDate;
    //holds schedule Id
    private String scheduleId;
    
    //prps start dec 12 2003
    private Vector followupAction ;
    //prps end dec 12 2003
  
    //holds type code
    private int actionTypeCode;
    //holds type decription
    private String actionTypeDescription;
    
    /** Creates new ProtocolActionChangesBean */
    public ProtocolActionChangesBean() {        
    }    

    /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /** Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /** Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
    /** Getter for property protocolStatusCode.
     * @return Value of property protocolStatusCode.
     */
    public int getProtocolStatusCode() {
        return protocolStatusCode;
    }
    
    /** Setter for property protocolStatusCode.
     * @param protocolStatusCode New value of property protocolStatusCode.
     */
    public void setProtocolStatusCode(int protocolStatusCode) {
        this.protocolStatusCode = protocolStatusCode;
    }
    
    /** Getter for property protocolStatusDescription.
     * @return Value of property protocolStatusDescription.
     */
    public java.lang.String getProtocolStatusDescription() {
        return protocolStatusDescription;
    }
    
    /** Setter for property protocolStatusDescription.
     * @param protocolStatusDescription New value of property protocolStatusDescription.
     */
    public void setProtocolStatusDescription(java.lang.String protocolStatusDescription) {
        this.protocolStatusDescription = protocolStatusDescription;
    }
    
    /** Getter for property submissionStatusCode.
     * @return Value of property submissionStatusCode.
     */
    public int getSubmissionStatusCode() {
        return submissionStatusCode;
    }
    
    /** Setter for property submissionStatusCode.
     * @param submissionStatusCode New value of property submissionStatusCode.
     */
    public void setSubmissionStatusCode(int submissionStatusCode) {
        this.submissionStatusCode = submissionStatusCode;
    }
    
    /** Getter for property submissionStatusDescription.
     * @return Value of property submissionStatusDescription.
     */
    public java.lang.String getSubmissionStatusDescription() {
        return submissionStatusDescription;
    }
    
    /** Setter for property submissionStatusDescription.
     * @param submissionStatusDescription New value of property submissionStatusDescription.
     */
    public void setSubmissionStatusDescription(java.lang.String submissionStatusDescription) {
        this.submissionStatusDescription = submissionStatusDescription;
    }
    
    /** Getter for property submissionTypeCode.
     * @return Value of property submissionTypeCode.
     */
    public int getSubmissionTypeCode() {
        return submissionTypeCode;
    }
    
    /** Setter for property submissionTypeCode.
     * @param submissionTypeCode New value of property submissionTypeCode.
     */
    public void setSubmissionTypeCode(int submissionTypeCode) {
        this.submissionTypeCode = submissionTypeCode;
    }
    
    /** Getter for property submissionTypeDescription.
     * @return Value of property submissionTypeDescription.
     */
    public java.lang.String getSubmissionTypeDescription() {
        return submissionTypeDescription;
    }
    
    /** Setter for property submissionTypeDescription.
     * @param submissionTypeDescription New value of property submissionTypeDescription.
     */
    public void setSubmissionTypeDescription(java.lang.String submissionTypeDescription) {
        this.submissionTypeDescription = submissionTypeDescription;
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
    
    /** Getter for property committeeId.
     * @return Value of property committeeId.
     */
    public java.lang.String getCommitteeId() {
        return committeeId;
    }
    
    /** Setter for property committeeId.
     * @param committeeId New value of property committeeId.
     */
    public void setCommitteeId(java.lang.String committeeId) {
        this.committeeId = committeeId;
    }
    
    /** Getter for property scheduleDate.
     * @return Value of property scheduleDate.
     */
    public Date getScheduleDate() {
        return scheduleDate;
    }
    
    /** Setter for property scheduleDate.
     * @param scheduleDate New value of property scheduleDate.
     */
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    
    /** Getter for property scheduleId.
     * @return Value of property scheduleId.
     */
    public java.lang.String getScheduleId() {
        return scheduleId;
    }
    
    /** Setter for property scheduleId.
     * @param scheduleId New value of property scheduleId.
     */
    public void setScheduleId(java.lang.String scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    
     //prps start dec 12 2003
    
    /** Getter for property followupAction.
     * @return Value of property followupAction.
     *
     */
    public java.util.Vector getFollowupAction() {
        return followupAction;
    }
    
    /** Setter for property followupAction.
     * @param followupAction New value of property followupAction.
     *
     */
    public void setFollowupAction(java.util.Vector followupAction) {
        this.followupAction = followupAction;
    }
    
     //prps end dec 12 2003
     
    /** Getter for property actionTypeCode.
     * @return Value of property actionTypeCode.
     */
    public int getActionTypeCode() {
        return actionTypeCode;
    }

    /** Setter for property actionTypeCode.
     * @param actionTypeCode New value of property actionTypeCode.
     */
    public void setActionTypeCode(int actionTypeCode) {
        this.actionTypeCode = actionTypeCode;
    }

    /** Getter for property actionTypeDescription.
     * @return Value of property actionTypeDescription.
     */
    public String getActionTypeDescription() {
        return actionTypeDescription;
    }
    
    /** Setter for property actionTypeDescription.
     * @param actionTypeDescription New value of property actionTypeDescription.
     */
    public void setActionTypeDescription(String actionTypeDescription) {
        this.actionTypeDescription = actionTypeDescription;
    }
}