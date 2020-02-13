/**
 * @(#)SubContractAmountInfoBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.subcontract.bean;

import edu.mit.coeus.bean.CoeusAttachment;
import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;
/**
 * This class is used to set and get the Subcontract Amount Info
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 03, 2004 12:30 PM
 */

public class SubContractAmountReleased extends SubContractBaseBean implements CoeusAttachment{
    
    private int lineNumber;    
    private double amountReleased;
    private Date effectiveDate;
    private String comments;
    //properties added for Princeton enhancement case#2802 - starts
    private String invoiceNumber;
    private Date startDate;
    private Date endDate;
    private String statusCode;
    private String approvalComments;
    private String approvedByUser;
    private Timestamp approvalDate;
    private byte[] document;
    private String fileName;
    private String createdBy;
    private Timestamp createdDate;
    //properties added for Princeton enhancement case#2802 - ends
    private String mimeType;
    /**
     *	Default Constructor
     */
    
    public SubContractAmountReleased(){
    }        

    
    /** Getter for property lineNumber.
     * @return Value of property lineNumber.
     */
    public int getLineNumber() {
        return lineNumber;
    }
    
    /** Setter for property lineNumber.
     * @param lineNumber New value of property lineNumber.
     */
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    /** Getter for property amountReleased.
     * @return Value of property amountReleased.
     */
    public double getAmountReleased() {
        return amountReleased;
    }
    
    /** Setter for property amountReleased.
     * @param amountReleased New value of property amountReleased.
     */
    public void setAmountReleased(double amountReleased) {
        this.amountReleased = amountReleased;
    }
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     */
    public java.sql.Date getEffectiveDate() {
        return effectiveDate;
    }
    
    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.sql.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
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
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */ 
    // Code added by Shivakumar begin
    public boolean equals(Object obj) {
        SubContractAmountReleased subContractAmountReleased = (SubContractAmountReleased)obj;
        if((subContractAmountReleased.getSubContractCode().equals(subContractAmountReleased.getSubContractCode())) &&
             (subContractAmountReleased.getLineNumber() == getLineNumber())){
                 return true;
        }else{
                 return false;
        }       
    }    
     // end

    /** Getter for property invoiceNumber.
     * @return Value of property invoiceNumber.
     */
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    /** Setter for property invoiceNumber.
     * @param invoiceNumber New value of property invoiceNumber.
     */
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public Date getStartDate() {
        return startDate;
    }

    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public Date getEndDate() {
        return endDate;
    }

    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /** Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public String getStatusCode() {
        return statusCode;
    }
    
    /** Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /** Getter for property approvalComments.
     * @return Value of property approvalComments.
     */
    public String getApprovalComments() {
        return approvalComments;
    }

    /** Setter for property approvalComments.
     * @param approvalComments New value of property approvalComments.
     */
    public void setApprovalComments(String approvalComments) {
        this.approvalComments = approvalComments;
    }

    /** Getter for property approvedByUser.
     * @return Value of property approvedByUser.
     */
    public String getApprovedByUser() {
        return approvedByUser;
    }

    /** Setter for property approvedByUser.
     * @param approvedByUser New value of property approvedByUser.
     */
    public void setApprovedByUser(String approvedByUser) {
        this.approvedByUser = approvedByUser;
    }

    /** Getter for property approvalDate.
     * @return Value of property approvalDate.
     */
    public Timestamp getApprovalDate() {
        return approvalDate;
    }

    /** Setter for property approvalDate.
     * @param approvalDate New value of property approvalDate.
     */
    public void setApprovalDate(Timestamp approvalDate) {
        this.approvalDate = approvalDate;
    }

    /** Getter for property document.
     * @return Value of property document.
     */    
    public byte[] getDocument() {
        return document;
    }

    /** Setter for property document.
     * @param document New value of property document.
     */    
    public void setDocument(byte[] document) {
        this.document = document;
    }

    /** Getter for property fileName.
     * @return Value of property fileName.
     */    
    public String getFileName() {
        return fileName;
    }

    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /** Getter for property createdDate.
     * @return Value of property createdDate.
     */    
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    /** Setter for property createdDate.
     * @param createdDate New value of property createdDate.
     */    
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    /** Getter for property createdBy.
     * @return Value of property createdBy.
     */    
    public String getCreatedBy() {
        return createdBy;
    }

    /** Setter for property createdBy.
     * @param createdBy New value of property createdBy.
     */    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    //Added with case 4007:Icon based on mime type
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getFileBytes() {
        return getDocument();
    }

    public void setFileBytes(byte[] fileData) {
        setDocument(fileData);
    }
    //4007 End
}