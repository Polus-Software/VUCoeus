/*
 * @(#)EquipmentBean.java 
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

public class EquipmentBean implements CoeusBean, java.io.Serializable{

    //equipment list - will be a vector of costBeans
    private CoeusVector cvEquipmentList;
    //total cost
    private BigDecimal totalFund ;
    //extra equipment list
    private CoeusVector cvExtraEquipmentList;
    //extra equip cost
    private BigDecimal totalExtraFund;
    //start add costSaring for fedNonFedBudget repport
    //total costSharing
    private BigDecimal totalNonFund ;
    //extra equip costSharing
    private BigDecimal totalExtraNonFund;
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
    
    public EquipmentBean(){
    }
      
     /** Getter for property cvEquipmentList.
     * @return Value of property cvEquipmentList.
     */
    public CoeusVector getEquipmentList() {
        return cvEquipmentList;
    }
    
    /** Setter for property cvEquipmentList.
     * @param versionNumber New value of property versionNumber.
     */
    public void setEquipmentList(CoeusVector cvEquipmentList) {
        this.cvEquipmentList = cvEquipmentList;
    }
   
    
    
      /** Getter for property totalFund.
     * @return Value of property totalFund.
     */
    public BigDecimal getTotalFund() {
        return totalFund;
    }
    
    /** Setter for property totalFund.
     * @param totalFund New value of property totalFund.
     */
    public void setTotalFund(BigDecimal totalFund) {
        this.totalFund = totalFund;
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
     * Getter for property cvExtraEquipmentList.
     * @return Value of property cvExtraEquipmentList.
     */
    public edu.mit.coeus.utils.CoeusVector getExtraEquipmentList() {
        return cvExtraEquipmentList;
    }    
    
    /**
     * Setter for property cvExtraEquipmentList.
     * @param cvExtraEquipmentList New value of property cvExtraEquipmentList.
     */
    public void setExtraEquipmentList(edu.mit.coeus.utils.CoeusVector cvExtraEquipmentList) {
        this.cvExtraEquipmentList = cvExtraEquipmentList;
    }
    
    /**
     * Getter for property totalExtraFund.
     * @return Value of property totalExtraFund.
     */
    public java.math.BigDecimal getTotalExtraFund() {
        return totalExtraFund;
    }
    
    /**
     * Setter for property totalExtraFund.
     * @param totalExtraFund New value of property totalExtraFund.
     */
    public void setTotalExtraFund(java.math.BigDecimal totalExtraFund) {
        this.totalExtraFund = totalExtraFund;
    }
    
    //start add costSaring for fedNonFedBudget repport
     /** Getter for property totalNonFund.
     * @return Value of property totalNonFund.
     */
    public BigDecimal getTotalNonFund() {
        return totalNonFund;
    }
    
    /** Setter for property totalNonFund.
     * @param totalNonFund New value of property totalNonFund.
     */
    public void setTotalNonFund(BigDecimal totalNonFund) {
        this.totalNonFund = totalNonFund;
    }
    
     /**
     * Getter for property totalExtraNonFund.
     * @return Value of property totalExtraNonFund.
     */
    public java.math.BigDecimal getTotalExtraNonFund() {
        return totalExtraNonFund;
    }
    
    /**
     * Setter for property totalExtraNonFund.
     * @param totalExtraNonFund New value of property totalExtraNonFund.
     */
    public void setTotalExtraNonFund(java.math.BigDecimal totalExtraNonFund) {
        this.totalExtraNonFund = totalExtraNonFund;
    }
    //end add costSaring for fedNonFedBudget repport
    
}