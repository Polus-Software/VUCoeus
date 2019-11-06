/*
 * RulesDefinitionBean.java
 *
 * Created on October 11, 2005, 6:35 PM
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  chandrashekara
 */
public class RulesDefinitionBean extends RuleBaseBean implements java.io.Serializable{
    // Holds conditionNumber
    private int conditionNumber;
    // Holds Description
    private String  description;
    // Holds condition_sequence
    private int conditionSequence;
    // Holds action
    private int action;
    // Holds condition Expression
    private String conditionExp;
    /** Creates a new instance of RulesDefinitionBean */
    public RulesDefinitionBean() {
    }
    
    /**
     * Getter for property conditionNumber.
     * @return Value of property conditionNumber.
     */
    public int getConditionNumber() {
        return conditionNumber;
    }
    
    /**
     * Setter for property conditionNumber.
     * @param conditionNumber New value of property conditionNumber.
     */
    public void setConditionNumber(int conditionNumber) {
        this.conditionNumber = conditionNumber;
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
     * Getter for property conditionSequence.
     * @return Value of property conditionSequence.
     */
    public int getConditionSequence() {
        return conditionSequence;
    }
    
    /**
     * Setter for property conditionSequence.
     * @param conditionSequence New value of property conditionSequence.
     */
    public void setConditionSequence(int conditionSequence) {
        this.conditionSequence = conditionSequence;
    }
    
    /**
     * Getter for property action.
     * @return Value of property action.
     */
    public int getAction() {
        return action;
    }
    
    /**
     * Setter for property action.
     * @param action New value of property action.
     */
    public void setAction(int action) {
        this.action = action;
    }
    
    /**
     * Getter for property conditionExp.
     * @return Value of property conditionExp.
     */
    public java.lang.String getConditionExp() {
        return conditionExp;
    }
    
    /**
     * Setter for property conditionExp.
     * @param conditionExp New value of property conditionExp.
     */
    public void setConditionExp(java.lang.String conditionExp) {
        this.conditionExp = conditionExp;
    }
    
}
