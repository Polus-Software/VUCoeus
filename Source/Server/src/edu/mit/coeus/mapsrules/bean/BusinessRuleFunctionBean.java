/*
 * RuleFunctionBean.java
 *
 * Created on October 21, 2005, 12:22 PM
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  vinayks
 */
public class BusinessRuleFunctionBean extends RuleBaseBean{
    
    private String functionName;
    private String description;
    private String returnType;
    private String dbFunctionName;
    
    /** Creates a new instance of RuleFunctionBean */
    public BusinessRuleFunctionBean() {
    }
    
    /**
     * Getter for property functionName.
     * @return Value of property functionName.
     */
    public java.lang.String getFunctionName() {
        return functionName;
    }    
    
    /**
     * Setter for property functionName.
     * @param functionName New value of property functionName.
     */
    public void setFunctionName(java.lang.String functionName) {
        this.functionName = functionName;
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
     * Getter for property returnType.
     * @return Value of property returnType.
     */
    public java.lang.String getReturnType() {
        return returnType;
    }
    
    /**
     * Setter for property returnType.
     * @param returnType New value of property returnType.
     */
    public void setReturnType(java.lang.String returnType) {
        this.returnType = returnType;
    }
    
    /**
     * Getter for property dbFunctionName.
     * @return Value of property dbFunctionName.
     */
    public java.lang.String getDbFunctionName() {
        return dbFunctionName;
    }
    
    /**
     * Setter for property dbFunctionName.
     * @param dbFunctionName New value of property dbFunctionName.
     */
    public void setDbFunctionName(java.lang.String dbFunctionName) {
        this.dbFunctionName = dbFunctionName;
    }
    
}
