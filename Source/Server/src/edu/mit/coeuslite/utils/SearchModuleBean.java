/*
 * SearchModuleBean.java
 *
 * Created on August 7, 2006, 8:35 PM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  chandrashekara
 */
public class SearchModuleBean implements java.io.Serializable{
    private String moduleKey;
    private String moduleNumber;
    private String unitNumber;
    private String userId;
    private String oldModuleNumber;
    private String mode;
    private String oldMode;
    
    /** Creates a new instance of SearchModuleBean */
    public SearchModuleBean() {
    }
    
    /**
     * Getter for property moduleKey.
     * @return Value of property moduleKey.
     */
    public java.lang.String getModuleKey() {
        return moduleKey;
    }
    
    /**
     * Setter for property moduleKey.
     * @param moduleKey New value of property moduleKey.
     */
    public void setModuleKey(java.lang.String moduleKey) {
        this.moduleKey = moduleKey;
    }
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public java.lang.String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(java.lang.String moduleNumber) {
        this.moduleNumber = moduleNumber;
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
     * Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /**
     * Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /**
     * Getter for property oldModuleNumber.
     * @return Value of property oldModuleNumber.
     */
    public java.lang.String getOldModuleNumber() {
        return oldModuleNumber;
    }    
  
    /**
     * Setter for property oldModuleNumber.
     * @param oldModuleNumber New value of property oldModuleNumber.
     */
    public void setOldModuleNumber(java.lang.String oldModuleNumber) {
        this.oldModuleNumber = oldModuleNumber;
    }    
    
    /**
     * Getter for property mode.
     * @return Value of property mode.
     */
    public java.lang.String getMode() {
        return mode;
    }
    
    /**
     * Setter for property mode.
     * @param mode New value of property mode.
     */
    public void setMode(java.lang.String mode) {
        this.mode = mode;
    }
    
    /**
     * Getter for property oldMode.
     * @return Value of property oldMode.
     */
    public java.lang.String getOldMode() {
        return oldMode;
    }
    
    /**
     * Setter for property oldMode.
     * @param oldMode New value of property oldMode.
     */
    public void setOldMode(java.lang.String oldMode) {
        this.oldMode = oldMode;
    }
    
}
