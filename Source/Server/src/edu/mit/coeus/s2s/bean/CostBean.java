/*
 * @(#)CostBean.java 
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

public class CostBean implements CoeusBean, java.io.Serializable{

   
    private int budgetPeriod;
    private BigDecimal cost;
    private String category;
    private String categoryType;
    private String description;
    private int quantity;
   //start add costSaring for fedNonFedBudget repport
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
    
    public CostBean(){
    }
      
         
    /** Getter for property budgetPeriod
     * @return Value of property budgetPEriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /** Setter for property budgetPeriod
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }
    
    
     /** Getter for property cost
     * @return Value of property cost.
     */
    public BigDecimal getCost() {
        return cost;
    }
    
    /** Setter for property cost
     * @param cost New value of property cost.
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    
    /** Getter for property category
     * @return Value of property category.
     */
    public String getCategory() {
        return category;
    }
    
    /** Setter for property category
     * @param category New value of property category.
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
      /** Getter for property categoryType
     * @return Value of property categoryType.
     */
    public String getCategoryType() {
        return categoryType;
    }
    
    /** Setter for property categoryType
     * @param categoryType New value of property categoryType.
     */
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
     /** Getter for property description
     * @return Value of property description.
     */
    public String getDescription() {
        return description;
    }
    
    /** Setter for property description
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
     /** Getter for property quantity
     * @return Value of property quantity.
     */
    public int getQuantity() {
        return quantity;
    }
    
    /** Setter for property quantity
     * @param quantity New value of property quantity.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    //start add costSaring for fedNonFedBudget repport
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