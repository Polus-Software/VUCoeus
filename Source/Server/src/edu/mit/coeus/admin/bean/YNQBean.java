/*
 * YNQBean.java
 *
 * Created on November 25, 2004, 7:09 PM
 */

package edu.mit.coeus.admin.bean;

import java.sql.Date;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class YNQBean extends YNQBaseBean implements java.io.Serializable {
    
    private String description;
    private String questionType;
    private int noOfAnswers;
    private String explanationRequiredFor;
    private String dateRequiredFor;
    private String status;
    private Date effectiveDate;
    private String groupName;
    
    
    /** Creates a new instance of YNQBean */
    public YNQBean() {
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
    
    /** Getter for property questionType.
     * @return Value of property questionType.
     *
     */
    public java.lang.String getQuestionType() {
        return questionType;
    }
    
    /** Setter for property questionType.
     * @param questionType New value of property questionType.
     *
     */
    public void setQuestionType(java.lang.String questionType) {
        this.questionType = questionType;
    }
    
    /** Getter for property noOfAnswers.
     * @return Value of property noOfAnswers.
     *
     */
    public int getNoOfAnswers() {
        return noOfAnswers;
    }
    
    /** Setter for property noOfAnswers.
     * @param noOfAnswers New value of property noOfAnswers.
     *
     */
    public void setNoOfAnswers(int noOfAnswers) {
        this.noOfAnswers = noOfAnswers;
    }
    
    /** Getter for property explanationRequiredFor.
     * @return Value of property explanationRequiredFor.
     *
     */
    public java.lang.String getExplanationRequiredFor() {
        return explanationRequiredFor;
    }
    
    /** Setter for property explanationRequiredFor.
     * @param explanationRequiredFor New value of property explanationRequiredFor.
     *
     */
    public void setExplanationRequiredFor(java.lang.String explanationRequiredFor) {
        this.explanationRequiredFor = explanationRequiredFor;
    }
    
    /** Getter for property dateRequiredFor.
     * @return Value of property dateRequiredFor.
     *
     */
    public java.lang.String getDateRequiredFor() {
        return dateRequiredFor;
    }
    
    /** Setter for property dateRequiredFor.
     * @param dateRequiredFor New value of property dateRequiredFor.
     *
     */
    public void setDateRequiredFor(java.lang.String dateRequiredFor) {
        this.dateRequiredFor = dateRequiredFor;
    }
    
    /** Getter for property status.
     * @return Value of property status.
     *
     */
    public java.lang.String getStatus() {
        return status;
    }
    
    /** Setter for property status.
     * @param status New value of property status.
     *
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     *
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    
    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     *
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    public boolean equals(Object obj) {
        YNQBean bean = (YNQBean)obj;
        if(bean.getQuestionId().equals(getQuestionId())){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Getter for property groupName.
     * @return Value of property groupName.
     */
    public java.lang.String getGroupName() {
        return groupName;
    }
    
    /**
     * Setter for property groupName.
     * @param groupName New value of property groupName.
     */
    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }
    
}
