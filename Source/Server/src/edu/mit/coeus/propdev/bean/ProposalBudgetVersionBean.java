/*
 * ProposalBudgetVersionBean.java
 *
 * Created on August 17, 2005, 11:06 AM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author  chandrashekara
 */
public class ProposalBudgetVersionBean implements BaseBean,java.io.Serializable{
    private int versionNumber;
    private boolean versionFlag;
    private boolean budgetSynced;
    private String unitNumber;
    private String childType;
    
    /** Creates a new instance of ProposalBudgetVersionBean */
    public ProposalBudgetVersionBean() {
    }
    
    /**
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property versionFlag.
     * @return Value of property versionFlag.
     */
    public boolean isVersionFlag() {
        return versionFlag;
    }
    
    /**
     * Setter for property versionFlag.
     * @param versionFlag New value of property versionFlag.
     */
    public void setVersionFlag(boolean versionFlag) {
        this.versionFlag = versionFlag;
    }
    
     public String toString() {
        return "Version-"+versionNumber;
    }
     
     /**
      * Getter for property budgetSynced.
      * @return Value of property budgetSynced.
      */
     public boolean isBudgetSynced() {
         return budgetSynced;
     }
     
     /**
      * Setter for property budgetSynced.
      * @param budgetSynced New value of property budgetSynced.
      */
     public void setBudgetSynced(boolean budgetSynced) {
         this.budgetSynced = budgetSynced;
     }
     
     /**
      * Getter for property unitNumber.
      * @return Value of property unitNumber.
      */
     public java.lang.String getUnitNumber() {
         return unitNumber;
     }
     
     /**
      * Setter for property unitNumber.
      * @param unitNumber New value of property unitNumber.
      */
     public void setUnitNumber(java.lang.String unitNumber) {
         this.unitNumber = unitNumber;
     }
     
     /**
      * Getter for property childType.
      * @return Value of property childType.
      */
     public java.lang.String getChildType() {
         return childType;
     }
     
     /**
      * Setter for property childType.
      * @param childType New value of property childType.
      */
     public void setChildType(java.lang.String childType) {
         this.childType = childType;
     }
     
}
