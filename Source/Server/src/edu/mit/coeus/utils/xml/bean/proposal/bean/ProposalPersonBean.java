/*
 * @(#)ProposalPersonBean.java 
 *
 * This bean contains information for printing Proposal Personnel
 * added as part of NSF schema extensions
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.beans.*;
import java.util.Vector;
import java.math.*;
 

public class ProposalPersonBean implements CoeusBean, java.io.Serializable{
 
 
   
    private String personId;
    private String lastName = null;
    private String firstName = null;
    private String middleName = null;
    private String ssn = null;
    private String dob = null; 
    private String phone = null; 
    private String email = null; 
    private String degree = null; 
    private String role = null; 
    private BigDecimal percentEffort = null; 
    private BigDecimal fundingMonths = null; 
    private BigDecimal academicFundingMonths = null; 
    private BigDecimal summerFundingMonths = null; 
    private BigDecimal requestedCost = null; 
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

 
    /**
     *	Default Constructor
     */
    
    public ProposalPersonBean(){
    }
      
   
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
      
    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
      
    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
      
    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
     /** Getter for property ssn.
     * @return Value of property ssn.
     */
    public java.lang.String getSsn() {
        return ssn;
    }
    
    /** Setter for property ssn.
     * @param ssn New value of property ssn.
     */
    public void setSsn(java.lang.String ssn) {
        this.ssn = ssn;
    }
   
  
     /** Getter for property dob.
     * @return Value of property dob.
     */
    public java.lang.String getDob() {
        return dob;
    }    
    
    /** Setter for property dob.
     * @param dob New value of property dob.
     */
    public void setDob(java.lang.String dob) {
        this.dob = dob;
    }
    
     /** Getter for property phone.
     * @return Value of property phone.
     */
    public java.lang.String getPhone() {
        return phone;
    }    
    
    /** Setter for property phone
     * @param phone New value of property phone.
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }
    
     /** Getter for property email.
     * @return Value of property email.
     */
    public java.lang.String getEmail() {
        return email;
    }    
    
    /** Setter for property email
     * @param email New value of property email.
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
    /** Getter for property degree
     * @return Value of property degree
     */
    public java.lang.String getDegree() {
        return degree;
    }    
    
    /** Setter for property degree
     * @param email New value of property degree
     */
    public void setDegree(java.lang.String degree) {
        this.degree = degree;
    }
   
     /** Getter for property role.
     * @return Value of property role.
     */
    public java.lang.String getRole() {
        return role;
    }    
    
    /** Setter for property role
     * @param role New value of property role.
     */
    public void setRole(java.lang.String role) {
        this.role = role;
    }
   
    /** Getter for property percentEffort.
     * @return Value of property percentEffort.
     */
    public BigDecimal getPercentEffort() {
        return percentEffort;
    }    
    
    /** Setter for property percentEffort
     * @param percentEffort New value of property percentEffort.
     */
    public void setPercentEffort(BigDecimal percentEffort) {
        this.percentEffort = percentEffort;
    }
    /** Getter for property fundingMonths.
     * @return Value of property fundingMonths.
     */
    public BigDecimal getFundingMonths() {
        return fundingMonths;
    }
    
    /** Setter for property fundingMonths.
     * @param fundingMonths New value of property fundingMonths.
     */
    public void setFundingMonths(BigDecimal fundingMonths) {
        this.fundingMonths = fundingMonths;
    }
    
    /** Getter for property requestedCost.
     * @return Value of property requestedCost.
     */
    public BigDecimal getRequestedCost() {
        return requestedCost;
    }
    
    /** Setter for property requestedCost.
     * @param requestedCost New value of property requestedCost.
     */
    public void setRequestedCost(BigDecimal  requestedCost) {
        this.requestedCost = requestedCost;
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
     * Getter for property academicFundingMonths.
     * @return Value of property academicFundingMonths.
     */
    public java.math.BigDecimal getAcademicFundingMonths() {
        return academicFundingMonths;
    }    
    
    /**
     * Setter for property academicFundingMonths.
     * @param academicFundingMonths New value of property academicFundingMonths.
     */
    public void setAcademicFundingMonths(java.math.BigDecimal academicFundingMonths) {
        this.academicFundingMonths = academicFundingMonths;
    }
    
    /**
     * Getter for property summerFundingMonths.
     * @return Value of property summerFundingMonths.
     */
    public java.math.BigDecimal getSummerFundingMonths() {
        return summerFundingMonths;
    }
    
    /**
     * Setter for property summerFundingMonths.
     * @param summerFundingMonths New value of property summerFundingMonths.
     */
    public void setSummerFundingMonths(java.math.BigDecimal summerFundingMonths) {
        this.summerFundingMonths = summerFundingMonths;
    }
    
}