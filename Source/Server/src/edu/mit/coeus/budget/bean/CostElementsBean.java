/*
 * @(#)CostElementsBean.java September 29, 2003, 11:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/* PMD check performed, and commented unused imports and variables 
 * on 24-JAN-2011 by Maharaja Palanichamy
 */

package edu.mit.coeus.budget.bean;

//import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
//import java.util.Hashtable;
import edu.mit.coeus.exception.CoeusException;
/**
 * The class used to hold the information of <code>Cost Elements</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 11:58 AM
 */
public class CostElementsBean implements CoeusBean, java.io.Serializable{

    //holds costelement
    private String costElement = null;
    //holds description
    private String description = null;
    //holds budget category code
    private int budgetCategoryCode = -1;
    //holds onOffCampus flag
    private boolean onOffCampusFlag;
    
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType;    
    
    // Added by Shivakumar for Cost Element - 17/11/2004
     private char categoryType;
     
    // Added by Shivakumar - 06/12/2004 
     //holds budget category description
    private String budgetCategoryDescription = null;
    
    // COEUSQA-1414 Allow schools to indicate if cost element is still active
    //holds active status
    private String active;
    
    //holds onOffCampus flag  Added by Shivakumar - 07/12/2004 
    private String campusFlag = "Off";
    
    /** Creates a new instance of BudgetInfo */
    public CostElementsBean() {
    }   
    
    /** Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }    
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }    
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     */
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }
    
    /** Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     */
    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;        
        if(comparableBean instanceof CostElementsBean){
            CostElementsBean costElementsBean = (CostElementsBean)comparableBean;
            //Cost Element
            if(costElementsBean.getCostElement()!=null){
                if(getCostElement().equals(costElementsBean.getCostElement())){
                    success = true;
                }else{
                    return false;
                }
            }
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }
    
// Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        CostElementsBean costElementsBean = (CostElementsBean)obj;
        if(costElementsBean.getCostElement().equals(getCostElement())){
            return true;
        }else {
            return false;
        }
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
    
    /** Getter for property budgetCategoryDescription.
     * @return Value of property budgetCategoryDescription.
     *
     */
    public java.lang.String getBudgetCategoryDescription() {
        return budgetCategoryDescription;
    }    
    
    /** Setter for property budgetCategoryDescription.
     * @param budgetCategoryDescription New value of property budgetCategoryDescription.
     *
     */
    public void setBudgetCategoryDescription(java.lang.String budgetCategoryDescription) {
        this.budgetCategoryDescription = budgetCategoryDescription;
    }
    
    /** Getter for property campusFlag.
     * @return Value of property campusFlag.
     *
     */
    public java.lang.String getCampusFlag() {
        return campusFlag;
    }
    
    /** Setter for property campusFlag.
     * @param campusFlag New value of property campusFlag.
     *
     */
    public void setCampusFlag(java.lang.String campusFlag) {
        this.campusFlag = campusFlag;
    }
    
    // Added by Chandra 04/10/2003 - End
    
    // COEUSQA-1414 Allow schools to indicate if cost element is still active - Start
    
    /** Getter for property active status.
     * @return Value of property active status.
     *
     */
    public java.lang.String getActive() {
        return active;
    }
    
    /** Setter for property active status.
     * @param active status New value of property active status.
     *
     */
    public void setActive(java.lang.String active) {
        this.active = active;
    }
    // COEUSQA-1414 Allow schools to indicate if cost element is still active - End
    
}
