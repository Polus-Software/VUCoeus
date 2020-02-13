/*
 * ComboBoxBean.java
 *
 * Created on May 10, 2005, 5:07 PM
 */

package edu.mit.coeuslite.utils;

/**
 *
 * @author  surekhan
 */
public class ComboBoxBean implements java.io.Serializable{
    private String code;
    private String description;
    
    /** Creates a new instance of ComboBoxBean */
    public ComboBoxBean() {
    }
    
    /**
     * Getter for property code.
     * @return Value of property code.
     */
    public java.lang.String getCode() {
        return code;
    }
    
    /**
     * Setter for property code.
     * @param code New value of property code.
     */
    public void setCode(java.lang.String code) {
        this.code = code;
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
    
}
