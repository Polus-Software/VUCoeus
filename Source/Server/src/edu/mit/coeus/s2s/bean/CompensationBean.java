/*
 * @(#)CompensationBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.CoeusVector;

import java.beans.*;
import java.math.BigDecimal;

public class CompensationBean implements CoeusBean, java.io.Serializable{

    private BigDecimal calendarMonths;
    private BigDecimal academicMonths ;
    private BigDecimal summerMonths;
    private BigDecimal requestedSalary;
    private BigDecimal fringe;
    private BigDecimal fundsRequested;
    private BigDecimal baseSalary;
    
    //start add costSaring for fedNonFedBudget repport
    private BigDecimal costSharingAmount;
    private BigDecimal fringeCostSharing;
    private BigDecimal nonFundsRequested;
    //end add costSaring for fedNonFedBudget repport

    //holds update user id - needed to implement CoeusBean
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

    /**
     *	Default Constructor
     */
    
    public CompensationBean(){
    }
      
     /** Getter for property calendarMonths.
     * @return Value of property calendarMonths.
     */
    public BigDecimal getCalendarMonths() {
        return calendarMonths;
    }
    
    /** Setter for property calendarMonths.
     * @param calendarMonths New value of property calendarMonths.
     */
    public void setCalendarMonths (BigDecimal calendarMonths) {
        this.calendarMonths = calendarMonths;
    }
    
    /** Getter for property academicMonths.
     * @return Value of property academicMonths.
     */
    public BigDecimal getAcademicMonths() {
        return academicMonths;
    }
    
    /** Setter for property academicMonths.
     * @param academicMonths New value of property academicMonths.
     */
    public void setAcademicMonths(BigDecimal academicMonths) {
        this.academicMonths = academicMonths;
    }
    
     /** Getter for property summerMonths.
     * @return Value of property summerMonths.
     */
    public BigDecimal getSummerMonths() {
        return summerMonths;
    }
    
    /** Setter for property summerMonths.
     * @param summerMonths New value of property summerMonths.
     */
    public void setSummerMonths(BigDecimal summerMonths) {
        this.summerMonths = summerMonths;
    }
    
     /** Getter for property requestedSalary.
     * @return Value of property requestedSalary.
     */
    public BigDecimal getRequestedSalary() {
        return requestedSalary;
    }
    
    /** Setter for property requestedSalary.
     * @param requestedSalary New value of property requestedSalary.
     */
    public void setRequestedSalary(BigDecimal requestedSalary) {
        this.requestedSalary = requestedSalary;
    }
    
     /** Getter for property fringe.
     * @return Value of property fringe.
     */
    public BigDecimal getFringe() {
        return fringe;
    }
    
    /** Setter for property fringe.
     * @param fringe New value of property fringe.
     */
    public void setFringe(BigDecimal fringe) {
        this.fringe = fringe;
    }
    
    /** Getter for property fundsRequested.
     * @return Value of property fundsRequested.
     */
    public BigDecimal getFundsRequested() {
        return fundsRequested;
    }
    
    /** Setter for property fundsRequested.
     * @param fundsRequested New value of property fundsRequested.
     */
    public void setFundsRequested(BigDecimal fundsRequested) {
        this.fundsRequested = fundsRequested;
    }
    
     /** Getter for property baseSalary.
     * @return Value of property baseSalary.
     */
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }
    
    /** Setter for property baseSalary.
     * @param baseSalary New value of property baseSalary.
     */
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    //start add costSaring for fedNonFedBudget repport
    public BigDecimal getCostSharingAmount() {
        return costSharingAmount;
    }
     public void setCostSharingAmount(BigDecimal costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }
     
     public BigDecimal getNonFundsRequested() {
        return nonFundsRequested;
    }
     public void setNonFundsRequested(BigDecimal nonFundsRequested) {
        this.nonFundsRequested = nonFundsRequested;
    }
     
     public BigDecimal getFringeCostSharing() {
        return fringeCostSharing;
    }
     public void setFringeCostSharing(BigDecimal fringeCostSharing) {
        this.fringeCostSharing = fringeCostSharing;
    } 
   //end add costSaring for fedNonFedBudget repport
    
  // following methods are necessary to implement CoeusBean  
   
    public String getUpdateUser() {
     return updateUser;
    }
    public void setUpdateUser(String userId) {
    }
    
  public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
    return true;
  }

    public java.lang.String getAcType() {
     return acType;
    }
    
    public void setAcType(java.lang.String acType) {      
    }  
   
    public java.sql.Timestamp getUpdateTimestamp() {
     return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {   
    }
    
    
}