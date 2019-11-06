/*
 * SubmissionReviewTypeBean.java
 *
 * Created on August 4, 2005, 4:25 PM
 */

package edu.mit.coeus.irb.bean;

/**
 *
 * @author  shijiv
 */
public class SubmissionReviewTypeBean implements java.io.Serializable {
    
    private int submissionTypeCode;
    private String submissionTypeDescription;
    private int reviewTypeCode;
    private String reviewTypeDescription;
    
    /** Creates a new instance of SubmissionReviewTypeBean */
    public SubmissionReviewTypeBean() {
    }
    
    /**
     * Getter for property submissionTypeCode.
     * @return Value of property submissionTypeCode.
     */
    public int getSubmissionTypeCode() {
        return submissionTypeCode;
    }
    
    /**
     * Setter for property submissionTypeCode.
     * @param submissionTypeCode New value of property submissionTypeCode.
     */
    public void setSubmissionTypeCode(int submissionTypeCode) {
        this.submissionTypeCode = submissionTypeCode;
    }
    
    
    
    /**
     * Getter for property reviewTypeCode.
     * @return Value of property reviewTypeCode.
     */
    public int getReviewTypeCode() {
        return reviewTypeCode;
    }
    
    /**
     * Setter for property reviewTypeCode.
     * @param reviewTypeCode New value of property reviewTypeCode.
     */
    public void setReviewTypeCode(int reviewTypeCode) {
        this.reviewTypeCode = reviewTypeCode;
    }
    
    /**
     * Getter for property reviewTypeDescription.
     * @return Value of property reviewTypeDescription.
     */
    public java.lang.String getReviewTypeDescription() {
        return reviewTypeDescription;
    }
    
    /**
     * Setter for property reviewTypeDescription.
     * @param reviewTypeDescription New value of property reviewTypeDescription.
     */
    public void setReviewTypeDescription(java.lang.String reviewTypeDescription) {
        this.reviewTypeDescription = reviewTypeDescription;
    }
    
    /**
     * Getter for property submissionTypeDescription.
     * @return Value of property submissionTypeDescription.
     */
    public java.lang.String getSubmissionTypeDescription() {
        return submissionTypeDescription;
    }
    
    /**
     * Setter for property submissionTypeDescription.
     * @param submissionTypeDescription New value of property submissionTypeDescription.
     */
    public void setSubmissionTypeDescription(java.lang.String submissionTypeDescription) {
        this.submissionTypeDescription = submissionTypeDescription;
    }
    
}
