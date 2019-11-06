/*
 * MetaRuleBean.java
 *
 * Created on October 17, 2005, 11:48 AM
 */

package edu.mit.coeus.mapsrules.bean;

import java.io.Serializable;


/**
 *
 * @author  chandrashekara
 */
public class MetaRuleBean extends RuleBaseBean implements Serializable{
    private String unitNumber;
    private String metaRuleId;
    private String metaRuleType;
    private String description;
    //Added for Coeus 4.3 enhancement PT ID:2785 - start
    private String moduleCode;
    private String submoduleCode;
    //Added for Coeus 4.3 enhancement PT ID:2785 - end
    /** Creates a new instance of MetaRuleBean */
    public MetaRuleBean() {
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
     * Getter for property metaRuleId.
     * @return Value of property metaRuleId.
     */
    public String getMetaRuleId() {
        return metaRuleId;
    }
    
    /**
     * Setter for property metaRuleId.
     * @param metaRuleId New value of property metaRuleId.
     */
    public void setMetaRuleId(String metaRuleId) {
        this.metaRuleId = metaRuleId;
    }
    
    /**
     * Getter for property metaRuleType.
     * @return Value of property metaRuleType.
     */
    public String getMetaRuleType() {
        return metaRuleType;
    }
    
    /**
     * Setter for property metaRuleType.
     * @param metaRuleType New value of property metaRuleType.
     */
    public void setMetaRuleType(String metaRuleType) {
        this.metaRuleType = metaRuleType;
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
     * Setter for property submoduleCode.
     * @param description New value of property submoduleCode.
     */
    public void setSubmoduleCode(String submoduleCode) {
        this.submoduleCode = submoduleCode;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
}
