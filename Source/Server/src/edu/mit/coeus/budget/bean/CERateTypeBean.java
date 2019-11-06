/*
 * CERateTypeBean.java
 *
 * Created on November 25, 2004, 5:09 PM
 */

package edu.mit.coeus.budget.bean;

/**
 *
 * @author  shivakumarmj
 */
public class CERateTypeBean extends ValidCERateTypesBean{
    
    //holds budget category code
    private int budgetCategoryCode = -1;
    //holds onOffCampus flag
    private boolean onOffCampusFlag;
    
    private char categoryType;
    
    /** Creates a new instance of CERateTypeBean */
    public CERateTypeBean() {
    } 
    
    /** Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     *
     */
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }    
    
    /** Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     *
     */
    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     *
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     *
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property categoryType.
     * @return Value of property categoryType.
     *
     */
    public char getCategoryType() {
        return categoryType;
    }
    
    /** Setter for property categoryType.
     * @param categoryType New value of property categoryType.
     *
     */
    public void setCategoryType(char categoryType) {
        this.categoryType = categoryType;
    }
    
}
