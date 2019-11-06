/*
 * @(#)QuestionnaireBean.java September 19, 2006, 2:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireBean.java
 *
 * Created on September 19, 2006, 2:30 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.util.Vector;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireBean extends QuestionnaireBaseBean implements BaseBean{
    private Integer questionId;
    // 4272: Maintain history of Questionnaires - Start
    private Integer questionVersionNumber;
    private boolean questionStatus;
    // 4272: Maintain history of Questionnaires - End
    private Integer questionNumber;
    private Integer parentQuestionNumber;
    private boolean conditionalFlag;
    private String condition;
    private String conditionValue;
    private Vector questions;
    private QuestionnaireUsageBean questionnaireUsageBean;
    private int moduleItemCode;
    private int moduleSubItemCode;
    //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
    private int questionSequenceNumber;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    private int conditionRuleId;
    private boolean previousQuestionFlag;
    private boolean ruleSelectionFlag;
    private String conditionRuleDesc;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    /** Creates a new instance of QuestionnaireBean */
    public QuestionnaireBean() {
    }
    
    /**
     * Getter for property questionId.
     * @return Value of property questionId.
     */
    public java.lang.Integer getQuestionId() {
        return questionId;
    }
    
    /**
     * Setter for property questionId.
     * @param questionId New value of property questionId.
     */
    public void setQuestionId(java.lang.Integer questionId) {
        this.questionId = questionId;
    }
    
    /**
     * Getter for property questionNumber.
     * @return Value of property questionNumber.
     */
    public java.lang.Integer getQuestionNumber() {
        return questionNumber;
    }
    
    /**
     * Setter for property questionNumber.
     * @param questionNumber New value of property questionNumber.
     */
    public void setQuestionNumber(java.lang.Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
    
    /**
     * Getter for property parentQuestionNumber.
     * @return Value of property parentQuestionNumber.
     */
    public java.lang.Integer getParentQuestionNumber() {
        return parentQuestionNumber;
    }
    
    /**
     * Setter for property parentQuestionNumber.
     * @param parentQuestionNumber New value of property parentQuestionNumber.
     */
    public void setParentQuestionNumber(java.lang.Integer parentQuestionNumber) {
        this.parentQuestionNumber = parentQuestionNumber;
    }
    
    /**
     * Getter for property conditionalFlag.
     * @return Value of property conditionalFlag.
     */
    public boolean isConditionalFlag() {
        return conditionalFlag;
    }
    
    /**
     * Setter for property conditionalFlag.
     * @param conditionalFlag New value of property conditionalFlag.
     */
    public void setConditionalFlag(boolean conditionalFlag) {
        this.conditionalFlag = conditionalFlag;
    }
    
    /**
     * Getter for property condition.
     * @return Value of property condition.
     */
    public java.lang.String getCondition() {
        return condition;
    }
    
    /**
     * Setter for property condition.
     * @param condition New value of property condition.
     */
    public void setCondition(java.lang.String condition) {
        this.condition = condition;
    }
    
    /**
     * Getter for property conditionValue.
     * @return Value of property conditionValue.
     */
    public java.lang.String getConditionValue() {
        return conditionValue;
    }
    
    /**
     * Setter for property conditionValue.
     * @param conditionValue New value of property conditionValue.
     */
    public void setConditionValue(java.lang.String conditionValue) {
        this.conditionValue = conditionValue;
    }
    
    /**
     * Getter for property questions.
     * @return Value of property questions.
     */
    public java.util.Vector getQuestions() {
        return questions;
    }
    
    /**
     * Setter for property questions.
     * @param questions New value of property questions.
     */
    public void setQuestions(java.util.Vector questions) {
        this.questions = questions;
    }
    
    /**
     * Getter for property questionnaireUsageBean.
     * @return Value of property questionnaireUsageBean.
     */
    public edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean getQuestionnaireUsageBean() {
        return questionnaireUsageBean;
    }
    
    /**
     * Setter for property questionnaireUsageBean.
     * @param questionnaireUsageBean New value of property questionnaireUsageBean.
     */
    public void setQuestionnaireUsageBean(edu.mit.coeus.questionnaire.bean.QuestionnaireUsageBean questionnaireUsageBean) {
        this.questionnaireUsageBean = questionnaireUsageBean;
    }
    
    public String tostring(){
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("Questionnaire Id : "+getQuestionnaireId());
        strBuffer.append(";");
        strBuffer.append("Questionnaire Name : "+getName());
        strBuffer.append(";");
        strBuffer.append("Questionnaire Description : "+getDescription());
        strBuffer.append(";");
        strBuffer.append("QuestionId : "+this.getQuestionId());
        strBuffer.append(";");
        strBuffer.append("Question Number : "+this.getQuestionNumber());     
        strBuffer.append(";");
        strBuffer.append("Parent Question Number : "+this.getParentQuestionNumber());
        strBuffer.append(";");
        strBuffer.append("Conditional Flag : "+this.isConditionalFlag());
        strBuffer.append(";");
        strBuffer.append("Condition : "+this.getCondition());
        strBuffer.append(";");
        strBuffer.append("Condition Value : "+this.getConditionValue());
        strBuffer.append(";");
        strBuffer.append("Questionnaire Usage : "+this.getQuestionnaireUsageBean().toString());
        strBuffer.append(";");
        return strBuffer.toString();
    }
    
    /**
     * Getter for property moduleItemCode.
     * @return Value of property moduleItemCode.
     */
    public int getModuleItemCode() {
        return moduleItemCode;
    }    
   
    /**
     * Setter for property moduleItemCode.
     * @param moduleItemCode New value of property moduleItemCode.
     */
    public void setModuleItemCode(int moduleItemCode) {
        this.moduleItemCode = moduleItemCode;
    }    
    
    /**
     * Getter for property moduleSubItemCode.
     * @return Value of property moduleSubItemCode.
     */
    public int getModuleSubItemCode() {
        return moduleSubItemCode;
    }
    
    /**
     * Setter for property moduleSubItemCode.
     * @param moduleSubItemCode New value of property moduleSubItemCode.
     */
    public void setModuleSubItemCode(int moduleSubItemCode) {
        this.moduleSubItemCode = moduleSubItemCode;
    }
    
      public boolean isLike(ComparableBean comparableBean)
    throws CoeusException{
        return true;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        QuestionnaireBean questionnaireBean = (QuestionnaireBean)obj;
        if(questionnaireBean.getQuestionId().equals(getQuestionId()) &&
            questionnaireBean.getQuestionNumber().equals(getQuestionNumber())){
            return true;
        }else{
            return false;
        }
    }    

    /**
     * Getter for property questionSequenceNumber.
     * @return Value of property questionSequenceNumber.
     */
    public int getQuestionSequenceNumber() {
        return questionSequenceNumber;
    }

    /**
     * Setter for property questionSequenceNumber.
     * @param questionSequenceNumber New value of property questionSequenceNumber.
     */    
    public void setQuestionSequenceNumber(int questionSequenceNumber) {
        this.questionSequenceNumber = questionSequenceNumber;
    }
    // 4272: Maintain history of Questionnaires - Start
    public Integer getQuestionVersionNumber() {
        return questionVersionNumber;
    }

    public void setQuestionVersionNumber(Integer questionVersionNumber) {
        this.questionVersionNumber = questionVersionNumber;
    }
    // 4272: Maintain history of Questionnaires - End

    public boolean isQuestionStatus() {
        return questionStatus;
    }

    public void setQuestionStatus(boolean questionStatus) {
        this.questionStatus = questionStatus;
    }
    
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Method to get condition rule id
     * @return conditionRuleId
     */
    public int getConditionRuleId() {
        return conditionRuleId;
    }

    /**
     * Method to set condition rule id
     * @param conditionRuleId 
     */
    public void setConditionRuleId(int conditionRuleId) {
        this.conditionRuleId = conditionRuleId;
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End

    public boolean isPreviousQuestionFlag() {
        return previousQuestionFlag;
    }

    public void setPreviousQuestionFlag(boolean previousQuestionFlag) {
        this.previousQuestionFlag = previousQuestionFlag;
    }

    public boolean isRuleSelectionFlag() {
        return ruleSelectionFlag;
    }

    public void setRuleSelectionFlag(boolean ruleSelectionFlag) {
        this.ruleSelectionFlag = ruleSelectionFlag;
    }

    public String getConditionRuleDesc() {
        return conditionRuleDesc;
    }

    public void setConditionRuleDesc(String conidtionRuleDesc) {
        this.conditionRuleDesc = conidtionRuleDesc;
    }
}
