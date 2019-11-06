/*
 * BussinessRuleConditionsBean.java
 *
 * Created on October 21, 2005, 1:39 PM
 */

package edu.mit.coeus.mapsrules.bean;

import java.io.Serializable;
import java.util.Vector;
/**
 *
 * @author  ajaygm
 */
public class BusinessRuleConditionsBean extends RuleBaseBean implements Serializable{
    
    private int ruleId;
    private int conditionNumber;
    private int conditionSequence;
    private String ruleDescription;
    private String mapDescription;
    private int action;
    private String conditionExp;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
    private String userMessage;
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end
    private int awConditionNumber;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - start
    private Vector businessRuleConditions;
    //Added for Coeus 4.3 enhancement PT ID 2785 - Routing enhancement - end
    /** Creates a new instance of BussinessRuleConditionsBean */
    public BusinessRuleConditionsBean() {
    }
    
    /**
     * Getter for property ruleId.
     * @return Value of property ruleId.
     */
    public int getRuleId() {
        return ruleId;
    }
    
    /**
     * Setter for property ruleId.
     * @param ruleId New value of property ruleId.
     */
    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
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
     * Getter for property ruleDescription.
     * @return Value of property ruleDescription.
     */
    public java.lang.String getRuleDescription() {
        return ruleDescription;
    }
    
    /**
     * Setter for property ruleDescription.
     * @param ruleDescription New value of property ruleDescription.
     */
    public void setRuleDescription(java.lang.String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }
    
    /**
     * Getter for property mapDescription.
     * @return Value of property mapDescription.
     */
    public java.lang.String getMapDescription() {
        return mapDescription;
    }
    
    /**
     * Setter for property mapDescription.
     * @param mapDescription New value of property mapDescription.
     */
    public void setMapDescription(java.lang.String mapDescription) {
        this.mapDescription = mapDescription;
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
    
    /**
     * Getter for property awConditionNumber.
     * @return Value of property awConditionNumber.
     */
    public int getAwConditionNumber() {
        return awConditionNumber;
    }
    
    /**
     * Setter for property awConditionNumber.
     * @param awConditionNumber New value of property awConditionNumber.
     */
    public void setAwConditionNumber(int awConditionNumber) {
        this.awConditionNumber = awConditionNumber;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - start
     /**
     * Getter for property userMessage.
     * @return Value of property userMessage.
     */
    public String getUserMessage() {
        return userMessage;
    }
    /**
     * Setter for property userMessage.
     * @param userMessage New value of property userMessage.
     */
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    //Added for Coeus 4.3 Routing enhancement -PT ID:2785 - end

}
