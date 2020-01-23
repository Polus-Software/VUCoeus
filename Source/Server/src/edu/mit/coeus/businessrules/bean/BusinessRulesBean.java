/**
 * @(#)MessageBean.java 1.0 01/17/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.businessrules.bean;

/**
 * This class is used to get and set Business Rules Details
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 17, 2004 1:01 PM
 */

public class BusinessRulesBean implements java.io.Serializable{
    
    private String ruleId;
    private String description;
    private String ruleType;
    private String unitName;
    
    /**
     *	Default Constructor
     */
    
    public BusinessRulesBean(){
    }    

    /** Getter for property ruleId.
     * @return Value of property ruleId.
     */
    public java.lang.String getRuleId() {
        return ruleId;
    }
    
    /** Setter for property ruleId.
     * @param ruleId New value of property ruleId.
     */
    public void setRuleId(java.lang.String ruleId) {
        this.ruleId = ruleId;
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
    
    /** Getter for property ruleType.
     * @return Value of property ruleType.
     */
    public java.lang.String getRuleType() {
        return ruleType;
    }
    
    /** Setter for property ruleType.
     * @param ruleType New value of property ruleType.
     */
    public void setRuleType(java.lang.String ruleType) {
        this.ruleType = ruleType;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitNumber New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }    
}