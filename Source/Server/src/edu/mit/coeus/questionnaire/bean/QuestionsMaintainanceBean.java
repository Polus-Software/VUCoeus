/*
 * @(#)QuestionsMaintainanceBean.java September 19, 2006, 2:19 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionsMaintainanceBean.java
 *
 * Created on September 19, 2006, 2:19 PM
 */

package edu.mit.coeus.questionnaire.bean;

/**
 *
 * @author  chandrashekara
 */
public class QuestionsMaintainanceBean extends QuestionBaseBean{
    private int maxAnswers;
    private String validAnswers;
    private String lookupName;
    private String answerDataType;
    private int answerMaxLength;
    private String lookupGui;
    
    //Added for question group - start - 1
    private int groupTypeCode;
    //Added for question group - start - 1
    
    // 4272: Maintain history of Questionnaires - Start
    private String status;
    /** Creates a new instance of QuestionsMaintainanceBean */
    public QuestionsMaintainanceBean() {
    }
    
    /**
     * Getter for property maxAnswers.
     * @return Value of property maxAnswers.
     */
    public int getMaxAnswers() {
        return maxAnswers;
    }
    
    /**
     * Setter for property maxAnswers.
     * @param maxAnswers New value of property maxAnswers.
     */
    public void setMaxAnswers(int maxAnswers) {
        this.maxAnswers = maxAnswers;
    }
    
    /**
     * Getter for property validAnswers.
     * @return Value of property validAnswers.
     */
    public java.lang.String getValidAnswers() {
        return validAnswers;
    }
    
    /**
     * Setter for property validAnswers.
     * @param validAnswers New value of property validAnswers.
     */
    public void setValidAnswers(java.lang.String validAnswers) {
        this.validAnswers = validAnswers;
    }
    
    /**
     * Getter for property lookupName.
     * @return Value of property lookupName.
     */
    public java.lang.String getLookupName() {
        return lookupName;
    }
    
    /**
     * Setter for property lookupName.
     * @param lookupName New value of property lookupName.
     */
    public void setLookupName(java.lang.String lookupName) {
        this.lookupName = lookupName;
    }
    
    /**
     * Getter for property answerDataType.
     * @return Value of property answerDataType.
     */
    public java.lang.String getAnswerDataType() {
        return answerDataType;
    }
    
    /**
     * Setter for property answerDataType.
     * @param answerDataType New value of property answerDataType.
     */
    public void setAnswerDataType(java.lang.String answerDataType) {
        this.answerDataType = answerDataType;
    }
    
    /**
     * Getter for property answerMaxLength.
     * @return Value of property answerMaxLength.
     */
    public int getAnswerMaxLength() {
        return answerMaxLength;
    }
    
    /**
     * Setter for property answerMaxLength.
     * @param answerMaxLength New value of property answerMaxLength.
     */
    public void setAnswerMaxLength(int answerMaxLength) {
        this.answerMaxLength = answerMaxLength;
    }
    
    /**
     * Getter for property lookupGui.
     * @return Value of property lookupGui.
     */
    public java.lang.String getLookupGui() {
        return lookupGui;
    }
    
    /**
     * Setter for property lookupGui.
     * @param lookupGui New value of property lookupGui.
     */
    public void setLookupGui(java.lang.String lookupGui) {
        this.lookupGui = lookupGui;
    }

    //Added for question group - start - 2
    public int getGroupTypeCode() {
        return groupTypeCode;
    }

    public void setGroupTypeCode(int groupTypeCode) {
        this.groupTypeCode = groupTypeCode;
    }
    //Added for question group - start - 2
    // 4272: Maintain history of Questionnaires - Start
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
  // 4272: Maintain history of Questionnaires - End  
}
