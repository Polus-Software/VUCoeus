/*
 * BusinessRuleBean.java
 *
 * Created on October 17, 2005, 2:10 PM
 */

package edu.mit.coeus.mapsrules.bean;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author  chandrashekara
 */
public class BusinessRuleBean extends RuleBaseBean implements Serializable{
    private String ruleId;
    private String description;
    private String ruleType;
    private String unitNumber;
    //Added for Coeus 4.3 enhancement PT ID:2785 - start
    private String moduleCode;
    private String submoduleCode;
    private String ruleCategory;
    private Vector businessRuleConditions;
    private String unitName;
    //Added for Coeus 4.3 enhancement PT ID:2785 - end
    
    /** Creates a new instance of BusinessRuleBean */
    public BusinessRuleBean() {
    }
    
    /**
     * Getter for property ruleId.
     * @return Value of property ruleId.
     */
    public java.lang.String getRuleId() {
        return ruleId;
    }
    
    /**
     * Setter for property ruleId.
     * @param ruleId New value of property ruleId.
     */
    public void setRuleId(java.lang.String ruleId) {
        this.ruleId = ruleId;
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
     * Getter for property ruleType.
     * @return Value of property ruleType.
     */
    public String getRuleType() {
        return ruleType;
    }
    
    /**
     * Setter for property ruleType.
     * @param ruleType New value of property ruleType.
     */
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
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
    
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    /**
     * Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public String getModuleCode() {
        return moduleCode;
    }
    /**
     * Setter for property moduleCode.
     * @param description New value of property moduleCode.
     */
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /**
     * Getter for property submoduleCode.
     * @return Value of property submoduleCode.
     */
    public String getSubmoduleCode() {
        return submoduleCode;
    }
    
    /**
     * Setter for property ruleCategory.
     * @param description New value of property ruleCategory.
     */
    public void setSubmoduleCode(String submoduleCode) {
        this.submoduleCode = submoduleCode;
    }
    
    /**
     * Getter for property ruleCategory.
     * @return Value of property ruleCategory.
     */
    public String getRuleCategory() {
        return ruleCategory;
    }
    
    /**
     * Setter for property submoduleCode.
     * @param description New value of property submoduleCode.
     */
    public void setRuleCategory(String ruleCategory) {
        this.ruleCategory = ruleCategory;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end

    public Vector getBusinessRuleConditions() {
        return businessRuleConditions;
    }

    public void setBusinessRuleConditions(Vector businessRuleConditions) {
        this.businessRuleConditions = businessRuleConditions;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public String toString(){
        if(description!=null && !description.trim().equals("")){
            return getDescription();
        }else if(getCode() != null && !getCode().trim().equals("")){
            return getCode();
        }
        return "";
    }    
}
