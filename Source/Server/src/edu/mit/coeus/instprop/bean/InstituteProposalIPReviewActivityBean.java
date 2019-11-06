/*
 * @(#)InstituteProposalIPReviewActivityBean.java 1.0 May 06 2004, 11:01 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/** This class is used to hold IP Review Activity of a Institute Proposal
 *
 * @version :1.0 May 06 2004, 11:01 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalIPReviewActivityBean extends InstituteProposalBaseBean{
    
    private int activityNumber;
    private int ipReviewActivityTypeCode;
    private Date activityDate;
    private String comments;
    private int rowId;
    
    public InstituteProposalIPReviewActivityBean(){
    }    
    
    /** Getter for property activityNumber.
     * @return Value of property activityNumber.
     */
    public int getActivityNumber() {
        return activityNumber;
    }
    
    /** Setter for property activityNumber.
     * @param activityNumber New value of property activityNumber.
     */
    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }
    
    /** Getter for property ipReviewActivityTypeCode.
     * @return Value of property ipReviewActivityTypeCode.
     */
    public int getIpReviewActivityTypeCode() {
        return ipReviewActivityTypeCode;
    }
    
    /** Setter for property ipReviewActivityTypeCode.
     * @param ipReviewActivityTypeCode New value of property ipReviewActivityTypeCode.
     */
    public void setIpReviewActivityTypeCode(int ipReviewActivityTypeCode) {
        this.ipReviewActivityTypeCode = ipReviewActivityTypeCode;
    }
    
    /** Getter for property activityDate.
     * @return Value of property activityDate.
     */
    public java.sql.Date getActivityDate() {
        return activityDate;
    }
    
    /** Setter for property activityDate.
     * @param activityDate New value of property activityDate.
     */
    public void setActivityDate(java.sql.Date activityDate) {
        this.activityDate = activityDate;
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
    public boolean equals(Object obj) {
        InstituteProposalIPReviewActivityBean instituteProposalIPReviewActivityBean = (InstituteProposalIPReviewActivityBean)obj;
        if(super.equals(instituteProposalIPReviewActivityBean)){
            if( instituteProposalIPReviewActivityBean.getActivityNumber() == getActivityNumber()
                && instituteProposalIPReviewActivityBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
       }else{
            return false;
        }
    }
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
} // end