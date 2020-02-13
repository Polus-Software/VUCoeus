/*
 * @(#)SalaryAndWagesBean.java 
 *
 * This bean contains budget detail information for a 
 * specific budget person-job code-percent effort combination
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.util.Vector;
 

public class SalaryAndWagesBean implements CoeusBean, java.io.Serializable{
 
  
    private String personId = null;
    private String fullName = null;
    private String lastName = null;
    private String firstName = null;
    private String middleName = null;
    private String jobCode = null;   
    private String role = null;
    private java.sql.Date effectiveDate = null;
    private double appointmentMonths = 0.0;
    private double fundingMonths = 0.0;  //calendar funding months
    //start additions for nih change on may 1, 2006
    private double summerFundingMonths = 0.0;
    private double academicFundingMonths = 0.0;
    //end additions
    private double calculationBase;  
    private String appointmentType = null;      
    private int percentEffort = 0;
    private double salaryRequested = 0.0;
    private double fringe = 0.0;
    private double total = 0.0;
 
    private boolean fullTimeQuestion ;
       //holds update user id - needed to implement CoeusBean
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

   
 
    /**
     *	Default Constructor
     */
    
    public SalaryAndWagesBean(){
    }
      
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property fullName.
     * @return Value of property fullName.
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /** Setter for property fullName.
     * @param fullName New value of property fullName.
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
     /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
     /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }
    
    /** Getter for property jobCode.
     * @return Value of property jobCode.
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /** Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }    

    /** Getter for property role.
     * @return Value of property role.
     */
    public java.lang.String getRole() {
        return role;
    }
    
    /** Setter for property role.
     * @param role New value of property role.
     */
    public void setRole(java.lang.String role) {
        this.role = role;
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
    
     /** Getter for property appointmentMonths.
     * @return Value of property appointmentMonths.
     */
    public double getAppointmentMonths() {
        return appointmentMonths;
    }    
    
    /** Setter for property appointmentMonths.
     * @param appointmentMonths New value of property appointmentMonths.
     */
    public void setAppointmentMonths(double appointmentMonths) {
        this.appointmentMonths = appointmentMonths;
    }
    
     /** Getter for property fundingMonths.
     * @return Value of property fundingMonths.
     */
    public double getFundingMonths() {
        return fundingMonths;
    }    
    
    /** Setter for property fundingMonths.
     * @param fundingMonths New value of property fundingMonths.
     */
    public void setFundingMonths(double fundingMonths) {
        this.fundingMonths = fundingMonths;
    }
    /** Getter for property calculationBase.
     * @return Value of property calculationBase.
     */
    public double getCalculationBase() {
        return calculationBase;
    }
    
    /** Setter for property calculationBase.
     * @param calculationBase New value of property calculationBase.
     */
    public void setCalculationBase(double calculationBase) {
        this.calculationBase = calculationBase;
    }
    
    /** Getter for property appointmentType.
     * @return Value of property appointmentType.
     */
    public java.lang.String getAppointmentType() {
        return appointmentType;
    }
    
    /** Setter for property appointmentType.
     * @param appointmentType New value of property appointmentType.
     */
    public void setAppointmentType(java.lang.String appointmentType) {
        this.appointmentType = appointmentType;
    }
   
    
     public int getPercentEffort() {
        return percentEffort;
    }
    
    
    public void setPercentEffort(int percentEffort) {
        this.percentEffort = percentEffort;
    }
    
    public double getSalaryRequested() {
        return salaryRequested;
    }
    
    
    public void setSalaryRequested(double salaryRequested) {
        this.salaryRequested = salaryRequested;
    }
    
      public double getFringe() {
        return fringe;
    }
      
    public void setFringe(double fringe) {
        this.fringe = fringe;
    }
    
    public double getTotal() {
        return total;
    }
      
    public void setTotal(double total) {
        this.total = total;
    }
     
     public boolean getFullTime() {
        return fullTimeQuestion;
    }
    
    
    public void setFullTime(boolean fullTimeQuestion) {
        this.fullTimeQuestion = fullTimeQuestion;
    }
    
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
    
    /**
     * Getter for property summerFundingMonths.
     * @return Value of property summerFundingMonths.
     */
    public double getSummerFundingMonths() {
        return summerFundingMonths;
    }    
    
    /**
     * Setter for property summerFundingMonths.
     * @param summerFundingMonths New value of property summerFundingMonths.
     */
    public void setSummerFundingMonths(double summerFundingMonths) {
        this.summerFundingMonths = summerFundingMonths;
    }
    
    /**
     * Getter for property academicFundingMonths.
     * @return Value of property academicFundingMonths.
     */
    public double getAcademicFundingMonths() {
        return academicFundingMonths;
    }
    
    /**
     * Setter for property academicFundingMonths.
     * @param academicFundingMonths New value of property academicFundingMonths.
     */
    public void setAcademicFundingMonths(double academicFundingMonths) {
        this.academicFundingMonths = academicFundingMonths;
    }
    
}