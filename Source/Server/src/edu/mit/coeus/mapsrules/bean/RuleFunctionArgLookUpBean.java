/*
 * RuleFunctionArgLookUpBean.java
 *
 * Created on October 27, 2005, 5:04 PM
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  suhanak
 */
public class RuleFunctionArgLookUpBean {
    
    private int type_code;
    
    private String Description;
    
    /** Creates a new instance of RuleFunctionArgLookUpBean */
    public RuleFunctionArgLookUpBean() {
    }
    
    /**
     * Getter for property Description.
     * @return Value of property Description.
     */
    public String getDescription() {
        return Description;
    }
    
    /**
     * Setter for property Description.
     * @param Description New value of property Description.
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    /**
     * Getter for property type_code.
     * @return Value of property type_code.
     */
    public int getType_code() {
        return type_code;
    }
    
    /**
     * Setter for property type_code.
     * @param type_code New value of property type_code.
     */
    public void setType_code(int type_code) {
        this.type_code = type_code;
    }
    
}
