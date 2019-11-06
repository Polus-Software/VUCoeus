/*
 * IRBInvestigatorBean.java
 *
 * Created on March 3, 2005, 4:25 PM
 */

package edu.mit.coeuslite.irb.bean;

import java.beans.*;
import java.io.Serializable;

/**
 * @author jinum
 */
public class IRBInvestigatorBean extends Object implements Serializable {
    
    private String invPid                   = null;
    private String invFirstName             = null;
    private String invLastName              = null;
    private String invPhone                 = null;
    private String invEmail                 = null;
    private String invDeptName              = null;
    private String invDeptNumber            = null;
    private String invRole                  = null;
    private String invAffiliationType       = null;
    private String personName               = null;
    private String employee                 = null;

    public IRBInvestigatorBean() {
    }
    
    /**
     * Getter for property invPid.
     * @return Value of property invPid.
     */
    public java.lang.String getInvPid() {
        return invPid;
    }
    
    /**
     * Setter for property invPid.
     * @param invPid New value of property invPid.
     */
    public void setInvPid(java.lang.String invPid) {
        this.invPid = invPid;
    }
    
    /**
     * Getter for property invFirstName.
     * @return Value of property invFirstName.
     */
    public java.lang.String getInvFirstName() {
        return invFirstName;
    }
    
    /**
     * Setter for property invFirstName.
     * @param invFirstName New value of property invFirstName.
     */
    public void setInvFirstName(java.lang.String invFirstName) {
        this.invFirstName = invFirstName;
    }
    
    /**
     * Getter for property invLastName.
     * @return Value of property invLastName.
     */
    public java.lang.String getInvLastName() {
        return invLastName;
    }
    
    /**
     * Setter for property invLastName.
     * @param invLastName New value of property invLastName.
     */
    public void setInvLastName(java.lang.String invLastName) {
        this.invLastName = invLastName;
    }
    
    /**
     * Getter for property invPhone.
     * @return Value of property invPhone.
     */
    public java.lang.String getInvPhone() {
        return invPhone;
    }
    
    /**
     * Setter for property invPhone.
     * @param invPhone New value of property invPhone.
     */
    public void setInvPhone(java.lang.String invPhone) {
        this.invPhone = invPhone;
    }
    
    /**
     * Getter for property invEmail.
     * @return Value of property invEmail.
     */
    public java.lang.String getInvEmail() {
        return invEmail;
    }
    
    /**
     * Setter for property invEmail.
     * @param invEmail New value of property invEmail.
     */
    public void setInvEmail(java.lang.String invEmail) {
        this.invEmail = invEmail;
    }
    
    /**
     * Getter for property invDeptName.
     * @return Value of property invDeptName.
     */
    public java.lang.String getInvDeptName() {
        return invDeptName;
    }
    
    /**
     * Setter for property invDeptName.
     * @param invDeptName New value of property invDeptName.
     */
    public void setInvDeptName(java.lang.String invDeptName) {
        this.invDeptName = invDeptName;
    }
    
    /**
     * Getter for property invDeptNumber.
     * @return Value of property invDeptNumber.
     */
    public java.lang.String getInvDeptNumber() {
        return invDeptNumber;
    }
    
    /**
     * Setter for property invDeptNumber.
     * @param invDeptNumber New value of property invDeptNumber.
     */
    public void setInvDeptNumber(java.lang.String invDeptNumber) {
        this.invDeptNumber = invDeptNumber;
    }
    
    /**
     * Getter for property invRole.
     * @return Value of property invRole.
     */
    public java.lang.String getInvRole() {
        return invRole;
    }
    
    /**
     * Setter for property invRole.
     * @param invRole New value of property invRole.
     */
    public void setInvRole(java.lang.String invRole) {
        this.invRole = invRole;
    }
    
    /**
     * Getter for property invAffiliationType.
     * @return Value of property invAffiliationType.
     */
    public java.lang.String getInvAffiliationType() {
        return invAffiliationType;
    }
    
    /**
     * Setter for property invAffiliationType.
     * @param invAffiliationType New value of property invAffiliationType.
     */
    public void setInvAffiliationType(java.lang.String invAffiliationType) {
        this.invAffiliationType = invAffiliationType;
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        setPersonName(invFirstName+" "+invLastName);
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /**
     * Getter for property employee.
     * @return Value of property employee.
     */
    public java.lang.String getEmployee() {
        return employee;
    }
    
    /**
     * Setter for property employee.
     * @param employee New value of property employee.
     */
    public void setEmployee(java.lang.String employee) {
        this.employee = employee;
    }
    
}
