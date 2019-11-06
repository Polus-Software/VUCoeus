/*
 * CategoryBean.java
 *
 * Created on April 6, 2006, 7:56 PM
 *
 * PMD check performed, and commented unused imports and variables on 15-FEB-2011
 * by Maharaja Palanichamy
 */

package edu.wmc.coeuslite.budget.bean;

import java.io.Serializable;

/**
 *
 * @author  tarique
 */
public class CategoryBean implements Serializable {
    //holds cost element code
    private String costElement;
    //holds cost element description
    private String description;
    //holds budget category code
    private int budgetCategoryCode;
    //holds canmpus flag default off
    private String campusFlag = "F";
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
    //holds active/inactive status
    private String activeFlag;
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End

    private String costElemDesc;

    public String getCostElemDesc() {
        return costElemDesc;
    }

    public void setCostElemDesc(String costElemDesc) {
        this.costElemDesc = costElemDesc;
    }
    /** Creates a new instance of CategoryBean */
    public CategoryBean() {
    }
    
    /**
     * Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /**
     * Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     */
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }
    
    /**
     * Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     */
    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property campusFlag.
     * @return Value of property campusFlag.
     */
    public java.lang.String getCampusFlag() {
        return campusFlag;
    }    
       
    /**
     * Setter for property campusFlag.
     * @param campusFlag New value of property campusFlag.
     */
    public void setCampusFlag(java.lang.String campusFlag) {
        this.campusFlag = campusFlag;
    }
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
    /**
     * Setter for property activeFlag.
     * @param activeFlag New value of property activeFlag.
     */
    public void setActiveFlag(java.lang.String activeFlag) {
        this.activeFlag = activeFlag;
    }
    
    /**
     * Getter for property activeFlag.
     * @return Value of property activeFlag.
     */
    public java.lang.String getActiveFlag() {
        return activeFlag;
    }
    //COEUSQA-1414 Allow schools to indicate if cost element is still active - End
}
