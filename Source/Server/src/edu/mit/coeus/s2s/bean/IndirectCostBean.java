/*
 * @(#)IndirectCostBean.java 
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

public class IndirectCostBean implements CoeusBean, java.io.Serializable{
   
    private CoeusVector indirectCostDetails;  //vector of indirectCostDetailBeans
    private BigDecimal totalIndirectCosts ;
    
   //start add costSaring for fedNonFedBudget repport
    private BigDecimal totalIndirectCostSharing ;
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
    
    public IndirectCostBean(){
    }
      
     /** Getter for property indirectCostDetails.
     * @return Value of property indirectCostDetails.
     */
    public CoeusVector getIndirectCostDetails() {
        return indirectCostDetails;
    }
    
    /** Setter for property indirectCostDetails.
     * @param indirectCostDetails New value of property indirectCostDetails.
     */
    public void setIndirectCostDetails (CoeusVector indirectCostDetails) {
        this.indirectCostDetails = indirectCostDetails;
    }
    
    /** Getter for property totalIndirectCosts.
     * @return Value of property totalIndirectCosts.
     */
    public BigDecimal getTotalIndirectCosts() {
        return totalIndirectCosts;
    }
    
    /** Setter for property totalIndirectCosts.
     * @param totalIndirectCosts New value of property totalIndirectCosts.
     */
    public void setTotalIndirectCosts(BigDecimal totalIndirectCosts) {
        this.totalIndirectCosts = totalIndirectCosts;
    }
    
   //start add costSaring for fedNonFedBudget repport
     /** Getter for property totalIndirectCostSharing.
     * @return Value of property totalIndirectCostSharing.
     */
    public BigDecimal getTotalIndirectCostSharing() {
        return totalIndirectCostSharing;
    }
    
    /** Setter for property totalIndirectCostSharing.
     * @param totalIndirectCostSharing New value of property totalIndirectCostSharing.
     */
    public void setTotalIndirectCostSharing(BigDecimal totalIndirectCostSharing) {
        this.totalIndirectCostSharing = totalIndirectCostSharing;
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