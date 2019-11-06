/*
 * @(#)SubcontractingReportsDetailBean.java 
 *
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.xml.generator.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.math.BigDecimal;
import edu.mit.coeus.utils.CoeusVector;
 

public class SubcontractingReportsDetailBean {
 

   private String administeringActivity;
   private String sponsor;
   private String remarks;
   private String agencyName;
   private String agencyStreetAddress;
   private String agencyCity;
   private String agencyState;
   private String agencyZip;
   //vendorAmounts is vector of subReportsAmountsBeans - there will be one 
   // subReportsAmountsBean for each vendor type (i.e. Small Bus, large Bus, etc)
   private CoeusVector vendorAmounts; 
 
   
    
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

   
 
    /**
     *	Default Constructor
     */
    
    public SubcontractingReportsDetailBean(){
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
     * Getter for property administeringActivity.
     * @return Value of property administeringActivity.
     */
    public java.lang.String getAdministeringActivity() {
        return administeringActivity;
    }
    
    /**
     * Setter for property administeringActivity.
     * @param administeringActivity New value of property administeringActivity.
     */
    public void setAdministeringActivity(java.lang.String administeringActivity) {
        this.administeringActivity = administeringActivity;
    }
    
    
    /**
     * Getter for property remarks.
     * @return Value of property remarks.
     */
    public java.lang.String getRemarks() {
        return remarks;
    }
    
    /**
     * Setter for property remarks.
     * @param remarks New value of property remarks.
     */
    public void setRemarks(java.lang.String remarks) {
        this.remarks = remarks;
    }
    
    /**
     * Getter for property actualAmounts.
     * @return Value of property actualAmounts.
    
    public edu.mit.coeus.utils.CoeusVector getActualAmounts() {
        return actualAmounts;
    }
     */
    /**
     * Setter for property actualAmounts.
     * @param actualAmounts New value of property actualAmounts.
   
    public void setActualAmounts(edu.mit.coeus.utils.CoeusVector actualAmounts) {
        this.actualAmounts = actualAmounts;
    }
      */
    /**
     * Getter for property goalAmounts.
     * @return Value of property goalAmounts.
    
    public edu.mit.coeus.utils.CoeusVector getGoalAmounts() {
        return goalAmounts;
    }
     */
    /**
     * Setter for property goalAmounts.
     * @param goalAmounts New value of property goalAmounts.
    
    public void setGoalAmounts(edu.mit.coeus.utils.CoeusVector goalAmounts) {
        this.goalAmounts = goalAmounts;
    }
     */
    /**
     * Getter for property agencyName.
     * @return Value of property agencyName.
     */
    public java.lang.String getAgencyName() {
        return agencyName;
    }
    
    /**
     * Setter for property agencyName.
     * @param agencyName New value of property agencyName.
     */
    public void setAgencyName(java.lang.String agencyName) {
        this.agencyName = agencyName;
    }
    
    /**
     * Getter for property agencyStreetAddress.
     * @return Value of property agencyStreetAddress.
     */
    public java.lang.String getAgencyStreetAddress() {
        return agencyStreetAddress;
    }
    
    /**
     * Setter for property agencyStreetAddress.
     * @param agencyStreetAddress New value of property agencyStreetAddress.
     */
    public void setAgencyStreetAddress(java.lang.String agencyStreetAddress) {
        this.agencyStreetAddress = agencyStreetAddress;
    }
    
    /**
     * Getter for property agencyCity.
     * @return Value of property agencyCity.
     */
    public java.lang.String getAgencyCity() {
        return agencyCity;
    }
    
    /**
     * Setter for property agencyCity.
     * @param agencyCity New value of property agencyCity.
     */
    public void setAgencyCity(java.lang.String agencyCity) {
        this.agencyCity = agencyCity;
    }
    
    /**
     * Getter for property agencyState.
     * @return Value of property agencyState.
     */
    public java.lang.String getAgencyState() {
        return agencyState;
    }
    
    /**
     * Setter for property agencyState.
     * @param agencyState New value of property agencyState.
     */
    public void setAgencyState(java.lang.String agencyState) {
        this.agencyState = agencyState;
    }
    
    /**
     * Getter for property agencyZip.
     * @return Value of property agencyZip.
     */
    public java.lang.String getAgencyZip() {
        return agencyZip;
    }
    
    /**
     * Setter for property agencyZip.
     * @param agencyZip New value of property agencyZip.
     */
    public void setAgencyZip(java.lang.String agencyZip) {
        this.agencyZip = agencyZip;
    }
    
    /**
     * Getter for property vendorAmounts.
     * @return Value of property vendorAmounts.
     */
    public edu.mit.coeus.utils.CoeusVector getVendorAmounts() {
        return vendorAmounts;
    }
    
    /**
     * Setter for property vendorAmounts.
     * @param amounts New value of property vendorAmounts.
     */
    public void setVendorAmounts(edu.mit.coeus.utils.CoeusVector vendorAmounts) {
        this.vendorAmounts = vendorAmounts;
    }
    
    /**
     * Getter for property sponsor.
     * @return Value of property sponsor.
     */
    public java.lang.String getSponsor() {
        return sponsor;
    }
    
    /**
     * Setter for property sponsor.
     * @param sponsor New value of property sponsor.
     */
    public void setSponsor(java.lang.String sponsor) {
        this.sponsor = sponsor;
    }
    
}