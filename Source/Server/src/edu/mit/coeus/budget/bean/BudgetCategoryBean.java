/*
 * BudgetCategoryBean.java
 *
 * Created on October 1, 2003, 11:55 AM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.utils.ComboBoxBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;

/**
 *
 * @author  prasanna
 */
public class BudgetCategoryBean extends ComboBoxBean implements CoeusBean{
    
    private char categoryType;    
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;    
    
    /** Creates a new instance of BudgetCategoryBean */
    public BudgetCategoryBean() {
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
    
    public boolean isLike(ComparableBean comparableBean) {
        return true;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Code  =>"+this.getCode());
        strBffr.append(";");
        strBffr.append("Description =>"+this.getDescription());
        strBffr.append(";");        
        strBffr.append("Category Type =>"+categoryType);
        strBffr.append(";");                 
        strBffr.append("Update User =>"+updateUser);
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+updateTimestamp);
        strBffr.append(";");
        strBffr.append("AcType =>"+acType);    
        return strBffr.toString();
    }     
}
