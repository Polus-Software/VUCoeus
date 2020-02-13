 /*
 * @(#)SubReportsAmountsBean.java 
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

public class SubReportsAmountsBean {
 
    private String vendorType;
    private BigDecimal amount;
    private BigDecimal percent;
    private BigDecimal goalAmount;
    private BigDecimal goalPercent;
    /*
   private BigDecimal largeBusAmount;
   private BigDecimal largeBusPercent;
   private BigDecimal smallBusAmount;
   private BigDecimal smallBusPercent;
   private BigDecimal totalAmount;
   private BigDecimal womanOwnedAmount;
   private BigDecimal womanOwnedPercent;
   private BigDecimal disadvantagedAmount;
   private BigDecimal disadvantagedPercent;
   private BigDecimal hubAmount;
   private BigDecimal hubPercent;
   private BigDecimal vetAmount;
   private BigDecimal vetPercent;
   private BigDecimal sdvoAmount;
   private BigDecimal sdvoPercent;
   private BigDecimal hbcuAmount;
   private BigDecimal hbcuPercent;

   */
    
   private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
   private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

 
    /**
     *	Default Constructor
     */
    
    public SubReportsAmountsBean(){
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
     * Getter for property vendorType.
     * @return Value of property vendorType.
     */
    public java.lang.String getVendorType() {
        return vendorType;
    }
    
    /**
     * Setter for property vendorType.
     * @param vendorType New value of property vendorType.
     */
    public void setVendorType(java.lang.String vendorType) {
        this.vendorType = vendorType;
    }
    
    /**
     * Getter for property amount.
     * @return Value of property amount.
     */
    public java.math.BigDecimal getAmount() {
        return amount;
    }
    
    /**
     * Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }
    
    /**
     * Getter for property percent.
     * @return Value of property percent.
     */
    public java.math.BigDecimal getPercent() {
        return percent;
    }
    
    /**
     * Setter for property percent.
     * @param percent New value of property percent.
     */
    public void setPercent(java.math.BigDecimal percent) {
        this.percent = percent;
    }
    
    /**
     * Getter for property goalAmount.
     * @return Value of property goalAmount.
     */
    public java.math.BigDecimal getGoalAmount() {
        return goalAmount;
    }
    
    /**
     * Setter for property goalAmount.
     * @param goalAmount New value of property goalAmount.
     */
    public void setGoalAmount(java.math.BigDecimal goalAmount) {
        this.goalAmount = goalAmount;
    }
    
    /**
     * Getter for property goalPercent.
     * @return Value of property goalPercent.
     */
    public java.math.BigDecimal getGoalPercent() {
        return goalPercent;
    }
    
    /**
     * Setter for property goalPercent.
     * @param goalPercent New value of property goalPercent.
     */
    public void setGoalPercent(java.math.BigDecimal goalPercent) {
        this.goalPercent = goalPercent;
    }
    
    /*
    /**
     * Getter for property largeBusAmount.
     * @return Value of property largeBusAmount.
     
    public java.math.BigDecimal getLargeBusAmount() {
        return largeBusAmount;
    }
    
    /**
     * Setter for property largeBusAmount.
     * @param largeBusAmount New value of property largeBusAmount.
    
    public void setLargeBusAmount(java.math.BigDecimal largeBusAmount) {
        this.largeBusAmount = largeBusAmount;
    }
    
    /**
     * Getter for property largeBusPercent.
     * @return Value of property largeBusPercent.
    
    public java.math.BigDecimal getLargeBusPercent() {
        return largeBusPercent;
    }
    
    /**
     * Setter for property largeBusPercent.
     * @param largeBusPercent New value of property largeBusPercent.
    
    public void setLargeBusPercent(java.math.BigDecimal largeBusPercent) {
        this.largeBusPercent = largeBusPercent;
    }
    
    /**
     * Getter for property smallBusAmount.
     * @return Value of property smallBusAmount.
    
    public java.math.BigDecimal getSmallBusAmount() {
        return smallBusAmount;
    }
    
    /**
     * Setter for property smallBusAmount.
     * @param smallBusAmount New value of property smallBusAmount.
   
    public void setSmallBusAmount(java.math.BigDecimal smallBusAmount) {
        this.smallBusAmount = smallBusAmount;
    }
    
    /**
     * Getter for property smallBusPercent.
     * @return Value of property smallBusPercent.
       public java.math.BigDecimal getSmallBusPercent() {
        return smallBusPercent;
    }
    
    /**
     * Setter for property smallBusPercent.
     * @param smallBusPercent New value of property smallBusPercent.
    
    public void setSmallBusPercent(java.math.BigDecimal smallBusPercent) {
        this.smallBusPercent = smallBusPercent;
    }
    
    /**
     * Getter for property totalAmount.
     * @return Value of property totalAmount.
    
    public java.math.BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Setter for property totalAmount.
     * @param totalAmount New value of property totalAmount.
    
    public void setTotalAmount(java.math.BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    /**
     * Getter for property womanOwnedAmount.
     * @return Value of property womanOwnedAmount.
    
    public java.math.BigDecimal getWomanOwnedAmount() {
        return womanOwnedAmount;
    }
    
    /**
     * Setter for property womanOwnedAmount.
     * @param womanOwnedAmount New value of property womanOwnedAmount.
    
    public void setWomanOwnedAmount(java.math.BigDecimal womanOwnedAmount) {
        this.womanOwnedAmount = womanOwnedAmount;
    }
    
    /**
     * Getter for property womanOwnedPercent.
     * @return Value of property womanOwnedPercent.
    
    public java.math.BigDecimal getWomanOwnedPercent() {
        return womanOwnedPercent;
    }
    
    /**
     * Setter for property womanOwnedPercent.
     * @param womanOwnedPercent New value of property womanOwnedPercent.
     
    public void setWomanOwnedPercent(java.math.BigDecimal womanOwnedPercent) {
        this.womanOwnedPercent = womanOwnedPercent;
    }
    
    /**
     * Getter for property hubAmount.
     * @return Value of property hubAmount.
     
    public java.math.BigDecimal getHubAmount() {
        return hubAmount;
    }
    
    /**
     * Setter for property hubAmount.
     * @param hubAmount New value of property hubAmount.
     
    public void setHubAmount(java.math.BigDecimal hubAmount) {
        this.hubAmount = hubAmount;
    }
    
    /**
     * Getter for property hubPercent.
     * @return Value of property hubPercent.
     
    public java.math.BigDecimal getHubPercent() {
        return hubPercent;
    }
    
    /**
     * Setter for property hubPercent.
     * @param hubPercent New value of property hubPercent.
     
    public void setHubPercent(java.math.BigDecimal hubPercent) {
        this.hubPercent = hubPercent;
    }
    
    /**
     * Getter for property vetAmount.
     * @return Value of property vetAmount.
     
    public java.math.BigDecimal getVetAmount() {
        return vetAmount;
    }
    
    /**
     * Setter for property vetAmount.
     * @param vetAmount New value of property vetAmount.
     
    public void setVetAmount(java.math.BigDecimal vetAmount) {
        this.vetAmount = vetAmount;
    }
    
    /**
     * Getter for property vetPercent.
     * @return Value of property vetPercent.
     
    public java.math.BigDecimal getVetPercent() {
        return vetPercent;
    }
    
    /**
     * Setter for property vetPercent.
     * @param vetPercent New value of property vetPercent.
    
    public void setVetPercent(java.math.BigDecimal vetPercent) {
        this.vetPercent = vetPercent;
    }
    
    /**
     * Getter for property sdvoAmount.
     * @return Value of property sdvoAmount.
     
    public java.math.BigDecimal getSdvoAmount() {
        return sdvoAmount;
    }
    
    /**
     * Setter for property sdvoAmount.
     * @param sdvoAmount New value of property sdvoAmount.
     
    public void setSdvoAmount(java.math.BigDecimal sdvoAmount) {
        this.sdvoAmount = sdvoAmount;
    }
    
    /**
     * Getter for property sdvoPercent.
     * @return Value of property sdvoPercent.
    
    public java.math.BigDecimal getSdvoPercent() {
        return sdvoPercent;
    }
    
    /**
     * Setter for property sdvoPercent.
     * @param sdvoPercent New value of property sdvoPercent.
     
    public void setSdvoPercent(java.math.BigDecimal sdvoPercent) {
        this.sdvoPercent = sdvoPercent;
    }
    
    /**
     * Getter for property hbcuAmount.
     * @return Value of property hbcuAmount.
     
    public java.math.BigDecimal getHbcuAmount() {
        return hbcuAmount;
    }
    
    /**
     * Setter for property hbcuAmount.
     * @param hbcuAmount New value of property hbcuAmount.
     
    public void setHbcuAmount(java.math.BigDecimal hbcuAmount) {
        this.hbcuAmount = hbcuAmount;
    }
    
    /**
     * Getter for property hbcuPercent.
     * @return Value of property hbcuPercent.
     
    public java.math.BigDecimal getHbcuPercent() {
        return hbcuPercent;
    }
    
    /**
     * Setter for property hbcuPercent.
     * @param hbcuPercent New value of property hbcuPercent.
     
    public void setHbcuPercent(java.math.BigDecimal hbcuPercent) {
        this.hbcuPercent = hbcuPercent;
    }
    
    /**
     * Getter for property disadvantagedAmount.
     * @return Value of property disadvantagedAmount.
     
    public java.math.BigDecimal getDisadvantagedAmount() {
        return disadvantagedAmount;
    }
    
    /**
     * Setter for property disadvantagedAmount.
     * @param disadvantagedAmount New value of property disadvantagedAmount.
     
    public void setDisadvantagedAmount(java.math.BigDecimal disadvantagedAmount) {
        this.disadvantagedAmount = disadvantagedAmount;
    }
    
    /**
     * Getter for property disadvantagedPercent.
     * @return Value of property disadvantagedPercent.
     
    public java.math.BigDecimal getDisadvantagedPercent() {
        return disadvantagedPercent;
    }
    */
    /**
     * Setter for property disadvantagedPercent.
     * @param disadvantagedPercent New value of property disadvantagedPercent.
   
    public void setDisadvantagedPercent(java.math.BigDecimal disadvantagedPercent) {
        this.disadvantagedPercent = disadvantagedPercent;
    }
    
     */
    
}