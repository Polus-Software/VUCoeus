/*
 * BatchCorrespondenceDetailsBean.java
 *
 * Created on March 8, 2004, 5:11 PM
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.iacuc.bean.*;
import java.io.Serializable;


public class BatchCorrespondenceDetailsBean implements Serializable
{
    private String correspondenceBatchId ;
     
    private String protocolNumber ;
    private String protocolTitle ;
    private java.sql.Date  protocolApprovalDate ; 
    private java.sql.Date protocolExpirationDate ;
    private int protocolSequenceNumber ;
    private int protocolActionId ;
    private String description ;
    private int submissionNumber ;
    
    public BatchCorrespondenceDetailsBean() 
    {
    }
    
    
        /** Getter for property protocolNumber.
     * @return Value of property protocolNumber.
     *
     */
    public java.lang.String getProtocolNumber() {
        return protocolNumber;
    }
    
    /** Setter for property protocolNumber.
     * @param protocolNumber New value of property protocolNumber.
     *
     */
    public void setProtocolNumber(java.lang.String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /** Getter for property protocolTitle.
     * @return Value of property protocolTitle.
     *
     */
    public java.lang.String getProtocolTitle() {
        return protocolTitle;
    }
    
    /** Setter for property protocolTitle.
     * @param protocolTitle New value of property protocolTitle.
     *
     */
    public void setProtocolTitle(java.lang.String protocolTitle) {
        this.protocolTitle = protocolTitle;
    }
    
    /** Getter for property protocolApprovalDate.
     * @return Value of property protocolApprovalDate.
     *
     */
    public java.sql.Date getProtocolApprovalDate() {
        return protocolApprovalDate;
    }
    
    /** Setter for property protocolApprovalDate.
     * @param protocolApprovalDate New value of property protocolApprovalDate.
     *
     */
    public void setProtocolApprovalDate(java.sql.Date protocolApprovalDate) {
        this.protocolApprovalDate = protocolApprovalDate;
    }
    
    /** Getter for property protocolExpirationDate.
     * @return Value of property protocolExpirationDate.
     *
     */
    public java.sql.Date getProtocolExpirationDate() {
        return protocolExpirationDate;
    }
    
    /** Setter for property protocolExpirationDate.
     * @param protocolExpirationDate New value of property protocolExpirationDate.
     *
     */
    public void setProtocolExpirationDate(java.sql.Date protocolExpirationDate) {
        this.protocolExpirationDate = protocolExpirationDate;
    }
    
    /** Getter for property protocolSequenceNumber.
     * @return Value of property protocolSequenceNumber.
     *
     */
    public int getProtocolSequenceNumber() {
        return protocolSequenceNumber;
    }
    
    /** Setter for property protocolSequenceNumber.
     * @param protocolSequenceNumber New value of property protocolSequenceNumber.
     *
     */
    public void setProtocolSequenceNumber(int protocolSequenceNumber) {
        this.protocolSequenceNumber = protocolSequenceNumber;
    }
    
    /** Getter for property protocolActionId.
     * @return Value of property protocolActionId.
     *
     */
    public int getProtocolActionId() {
        return protocolActionId;
    }
    
    /** Setter for property protocolActionId.
     * @param protocolActionId New value of property protocolActionId.
     *
     */
    public void setProtocolActionId(int protocolActionId) {
        this.protocolActionId = protocolActionId;
    }

    /** Getter for property correspondenceBatchId.
     * @return Value of property correspondenceBatchId.
     *
     */
    public java.lang.String getCorrespondenceBatchId() {
        return correspondenceBatchId;
    }    
    
    /** Setter for property correspondenceBatchId.
     * @param correspondenceBatchId New value of property correspondenceBatchId.
     *
     */
    public void setCorrespondenceBatchId(java.lang.String correspondenceBatchId) {
        this.correspondenceBatchId = correspondenceBatchId;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property submissionNumber.
     * @return Value of property submissionNumber.
     *
     */
    public int getSubmissionNumber() {
        return submissionNumber;
    }
    
    /** Setter for property submissionNumber.
     * @param submissionNumber New value of property submissionNumber.
     *
     */
    public void setSubmissionNumber(int submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
}