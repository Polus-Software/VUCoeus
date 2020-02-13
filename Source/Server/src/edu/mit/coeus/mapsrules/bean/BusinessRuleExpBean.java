/*
 * BussinessRuleExpBean.java
 *
 * Created on October 21, 2005, 1:47 PM
 */

package edu.mit.coeus.mapsrules.bean;

import java.io.Serializable;

/**
 *
 * @author  ajaygm
 */
public class BusinessRuleExpBean extends RuleBaseBean implements Serializable{
    
    private int ruleId;
    private int conditionNumber;
    private int expressionNumber;
    private String expressionType;
    private String lvalue;
    private String operator;
    private String rvalue;
    private String logicalOperator;
    private int noOfAnswers;
    
    private int awConditionNumber;
    private int awExpressionNumber;
    // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
    private String expressionPrefix;
    private String expressionSuffix;
    // COEUSQA-2458-End
    
    /** Creates a new instance of BussinessRuleExpBean */
    public BusinessRuleExpBean() {
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
     * Getter for property expressionNumber.
     * @return Value of property expressionNumber.
     */
    public int getExpressionNumber() {
        return expressionNumber;
    }
    
    /**
     * Setter for property expressionNumber.
     * @param expressionNumber New value of property expressionNumber.
     */
    public void setExpressionNumber(int expressionNumber) {
        this.expressionNumber = expressionNumber;
    }
    
    /**
     * Getter for property expressionType.
     * @return Value of property expressionType.
     */
    public java.lang.String getExpressionType() {
        return expressionType;
    }
    
    /**
     * Setter for property expressionType.
     * @param expressionType New value of property expressionType.
     */
    public void setExpressionType(java.lang.String expressionType) {
        this.expressionType = expressionType;
    }
    
    /**
     * Getter for property lvalue.
     * @return Value of property lvalue.
     */
    public java.lang.String getLvalue() {
        return lvalue;
    }
    
    /**
     * Setter for property lvalue.
     * @param lvalue New value of property lvalue.
     */
    public void setLvalue(java.lang.String lvalue) {
        this.lvalue = lvalue;
    }
    
    /**
     * Getter for property operator.
     * @return Value of property operator.
     */
    public java.lang.String getOperator() {
        return operator;
    }
    
    /**
     * Setter for property operator.
     * @param operator New value of property operator.
     */
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }
    
    /**
     * Getter for property rvalue.
     * @return Value of property rvalue.
     */
    public java.lang.String getRvalue() {
        return rvalue;
    }
    
    /**
     * Setter for property rvalue.
     * @param rvalue New value of property rvalue.
     */
    public void setRvalue(java.lang.String rvalue) {
        this.rvalue = rvalue;
    }
    
    /**
     * Getter for property logicalOperator.
     * @return Value of property logicalOperator.
     */
    public java.lang.String getLogicalOperator() {
        return logicalOperator;
    }
    
    /**
     * Setter for property logicalOperator.
     * @param logicalOperator New value of property logicalOperator.
     */
    public void setLogicalOperator(java.lang.String logicalOperator) {
        this.logicalOperator = logicalOperator;
    }
    
    /**
     * Getter for property noOfAnswers.
     * @return Value of property noOfAnswers.
     */
    public int getNoOfAnswers() {
        return noOfAnswers;
    }
    
    /**
     * Setter for property noOfAnswers.
     * @param noOfAnswers New value of property noOfAnswers.
     */
    public void setNoOfAnswers(int noOfAnswers) {
        this.noOfAnswers = noOfAnswers;
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
    
    /**
     * Getter for property awExpressionNumber.
     * @return Value of property awExpressionNumber.
     */
    public int getAwExpressionNumber() {
        return awExpressionNumber;
    }
    
    /**
     * Setter for property awExpressionNumber.
     * @param awExpressionNumber New value of property awExpressionNumber.
     */
    public void setAwExpressionNumber(int awExpressionNumber) {
        this.awExpressionNumber = awExpressionNumber;
    }
    
    // Added with COEUSQA-2458-Make 'and' and 'or' work in business rules
    /**
     * Getter for property expressionPrefix.
     * @return Value of property expressionPrefix.
     */
    public String getExpressionPrefix() {
        return expressionPrefix;
    }
    
    /**
     * Getter for property expressionSuffix.
     * @return Value of property expressionSuffix.
     */
    public String getExpressionSuffix() {
        return expressionSuffix;
    }

    /**
     * Setter for property expressionPrefix.
     * @param expressionPrefix New value of property expressionPrefix.
     */
    public void setExpressionPrefix(String expressionPrefix) {
        this.expressionPrefix = expressionPrefix;
    }

    /**
     * Setter for property expressionSuffix.
     * @param expressionSuffix New value of property expressionSuffix.
     */
    public void setExpressionSuffix(String expressionSuffix) {
        this.expressionSuffix = expressionSuffix;
    }

    // COEUSQA-2458-End

}
