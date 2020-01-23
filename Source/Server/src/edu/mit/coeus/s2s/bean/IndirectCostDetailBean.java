/*
 * @(#)IndirectCostDetailBean.java 
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

public class IndirectCostDetailBean implements CoeusBean, java.io.Serializable{

    private String costType;
    private BigDecimal rate ;
    private BigDecimal base;
    private BigDecimal funds;
    //start add costSaring for fedNonFedBudget repport
    private BigDecimal baseCostSharing;
    private BigDecimal costSharing;
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
    
    public IndirectCostDetailBean(){
    }
      
     /** Getter for property costType.
     * @return Value of property costType.
     */
    public String getCostType() {
        return costType;
    }
    
    /** Setter for property costType.
     * @param costType New value of property costType.
     */
    public void setCostType (String costType) {
        this.costType = costType;
    }
    
    /** Getter for property rate.
     * @return Value of property rate.
     */
    public BigDecimal getRate() {
        return rate;
    }
    
    /** Setter for property rate.
     * @param rate New value of property rate.
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    
     /** Getter for property base.
     * @return Value of property base.
     */
    public BigDecimal getBase() {
        return base;
    }
    
    /** Setter for property base.
     * @param base New value of property base.
     */
    public void setBase(BigDecimal base) {
        this.base = base;
    }
    
     /** Getter for property funds.
     * @return Value of property funds.
     */
    public BigDecimal getFunds() {
        return funds;
    }
    
    /** Setter for property funds.
     * @param funds New value of property funds.
     */
    public void setFunds(BigDecimal funds) {
        this.funds = funds;
    }
    
    //start add costSaring for fedNonFedBudget repport
    
    public BigDecimal getBaseCostSharing() {
        return baseCostSharing;
    }
    
    public void setBaseCostSharing(BigDecimal baseCostSharing) {
        this.baseCostSharing = baseCostSharing;
    }
    
    public BigDecimal getCostSharing() {
        return costSharing;
    }
    
    public void setCostSharing(BigDecimal costSharing) {
        this.costSharing = costSharing;
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