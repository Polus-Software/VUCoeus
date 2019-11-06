/*
 * @(#)CostBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;

public  class CostBean implements CoeusBean, java.io.Serializable{

    //budget version
    private int versionNumber;
    //budget period
    private int budgetPeriod ;
    // description - concatenation of line item descriptions
    private String description = null;
    //budget category code (target category code)
    private String budgetCategoryCode = null;
    // budget category code description
    private String budgetCategoryDesc = null;
    // category type
    private String categoryType = null;
    //category cost
    private double cost = 0;   
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
      
     /** Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /** Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
     /** Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /** Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }
    
    /** Getter for property Description.
     * @return Value of property Description
     */
    public String getDescription() {
        return description;
    }
    
    /** Setter for property costElementDesc.
     * this is the cost Element description
     * @param costElementDesc New value of property costElementDesc.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
  
    /** Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     */
    public String getBudgetCategoryCode() {
        return budgetCategoryCode;
    }
    
    /** Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     */
    public void setBudgetCategoryCode(String budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
     /** Getter for property budgetCategoryDesc
     * @return Value of property budgetCategoryDesc.
     */
    public String getBudgetCategoryDesc() {
        return budgetCategoryDesc;
    }
    
    /** Setter for property budgetCategoryDesc.
     * @param budgetCategoryDesc New value of property budgetCategoryDesc.
     */
    public void setBudgetCategoryDesc(String budgetCategoryDesc) {
        this.budgetCategoryDesc = budgetCategoryDesc;
    }
  
     /** Getter for property categoryType
     * @return Value of property categoryType.
     */
    public String getCategoryType() {
        return categoryType;
    }
    
    /** Setter for property categoryType.
     * @param categoryType New value of property categoryType.
     */
    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
    
      /** Getter for property cost.
     * @return Value of property cost.
     */
    public double getCost() {
        return cost;
    }
    
    /** Setter for property cost.
     * @param cost New value of property cost.
     */
    public void setCost(double cost) {
        this.cost = cost;
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
    
    
}