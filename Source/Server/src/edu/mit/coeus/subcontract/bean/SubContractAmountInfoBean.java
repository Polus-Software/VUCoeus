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

public class SubContractAmountInfoBean extends SubContractBaseBean implements CoeusAttachment{
    
    private int lineNumber;
    private double obligatedAmount;
    private double obligatedChange;
    private double anticipatedAmount;
    private double anticipatedChange;    
    private Date effectiveDate;
    private String comments;
    private String fileName;
    private byte[] document;
    private String mimeType;
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    private Date performanceStartDate;
    private Date performanceEndDate;
    private Date modificationEffectiveDate;
    private String modificationNumber;
    // Added for COEUSQA-1412 Subcontract Module changes - End
    
    
    
    /**
     *	Default Constructor
     */
    
    public SubContractAmountInfoBean(){
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
    
    /** Getter for property obligatedAmount.
     * @return Value of property obligatedAmount.
     */
    public double getObligatedAmount() {
        return obligatedAmount;
    }
    
    /** Setter for property obligatedAmount.
     * @param obligatedAmount New value of property obligatedAmount.
     */
    public void setObligatedAmount(double obligatedAmount) {
        this.obligatedAmount = obligatedAmount;
    }
    
    /** Getter for property obligatedChange.
     * @return Value of property obligatedChange.
     */
    public double getObligatedChange() {
        return obligatedChange;
    }
    
    /** Setter for property obligatedChange.
     * @param obligatedChange New value of property obligatedChange.
     */
    public void setObligatedChange(double obligatedChange) {
        this.obligatedChange = obligatedChange;
    }
    
    /** Getter for property anticipatedAmount.
     * @return Value of property anticipatedAmount.
     */
    public double getAnticipatedAmount() {
        return anticipatedAmount;
    }
    
    /** Setter for property anticipatedAmount.
     * @param anticipatedAmount New value of property anticipatedAmount.
     */
    public void setAnticipatedAmount(double anticipatedAmount) {
        this.anticipatedAmount = anticipatedAmount;
    }
    
    /** Getter for property anticipatedChange.
     * @return Value of property anticipatedChange.
     */
    public double getAnticipatedChange() {
        return anticipatedChange;
    }
    
    /** Setter for property anticipatedChange.
     * @param anticipatedChange New value of property anticipatedChange.
     */
    public void setAnticipatedChange(double anticipatedChange) {
        this.anticipatedChange = anticipatedChange;
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
        // Added by Shivakumar begin
        public boolean equals(Object obj) {
            super.equals(obj);
            SubContractAmountInfoBean subContractAmountInfoBean = (SubContractAmountInfoBean)obj;            
            if(subContractAmountInfoBean.getLineNumber() == getLineNumber()) {
                    return true;
            } else {
                    return false;
            }    
            
        }    
	// end

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
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
    
    // Added for COEUSQA-1412 Subcontract Module changes - Start
    /**
     * Method to get Performance Start Date
     * @return performanceStartDate
     */
    public Date getPerformanceStartDate() {
        return performanceStartDate;
    }

    /**
     * Method to set Performance Start Date
     * @param performanceStartDate 
     */
    public void setPerformanceStartDate(Date performanceStartDate) {
        this.performanceStartDate = performanceStartDate;
    }

    /**
     * Method to get Performance End Date
     * @return 
     */
    public Date getPerformanceEndDate() {
        return performanceEndDate;
    }

    /**
     * Method to set Performance End Date
     * @param performanceEndDate 
     */
    public void setPerformanceEndDate(Date performanceEndDate) {
        this.performanceEndDate = performanceEndDate;
    }

    /**
     * Method to get Modification Effective Date
     * @return modificationEffectiveDate
     */
    public Date getModificationEffectiveDate() {
        return modificationEffectiveDate;
    }

    /**
     * Method to set Modification Effective Date
     * @param modificationEffectiveDate 
     */
    public void setModificationEffectiveDate(Date modificationEffectiveDate) {
        this.modificationEffectiveDate = modificationEffectiveDate;
    }

    /**
     * Method to get Modification Number
     * @return modificationNumber
     */
    public String getModificationNumber() {
        return modificationNumber;
    }

    /**
     * Method to set Modification Number
     * @param modificationNumber 
     */
    public void setModificationNumber(String modificationNumber) {
        this.modificationNumber = modificationNumber;
    }
    // Added for COEUSQA-1412 Subcontract Module changes - End    
}