/* 
 * @(#)DepartmentBudgetFormBean.java 1.0 03/15/03 11:47 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Department Budget</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 15, 2003, 11:47 AM
 */
public class DepartmentBudgetFormBean implements java.io.Serializable, BaseBean {
    
    //holds the cost element
    private String costElement;
    //holds the description
    private String description;
    //holds the budget category code
    private int budgetCategoryCode;
    //holds the on off campus flag
    private boolean onOffCampusFlag;
    //holds update user id
     private String updateUser;
     //holds update timestamp
     private Timestamp updateTimestamp;
     //holds account type
     private String acType;    
    //holds category type
    private char categoryType;     
     
    public String getCostElement() {
        return costElement;
    }

    public void setCostElement(String costElement) {
        this.costElement = costElement;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }

    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }

    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
 
    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /** Getter for property categoryType.
     * @return Value of property categoryType.
     */
    public char getCategoryType() {
        return categoryType;
    }
    
    /** Setter for property categoryType.
     * @param categoryType New value of property categoryType.
     */
    public void setCategoryType(char categoryType) {
        this.categoryType = categoryType;
    }
    
}
