/*
 * @(#)QuestionnaireBaseBean.java September 19, 2006, 2:27 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * QuestionnaireBaseBean.java
 *
 * Created on September 19, 2006, 2:27 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireBaseBean extends QuestionnaireMaintainaceBaseBean {
    private int questionnaireId;
    // 4272: Maintain history of Questionnaires 
    private int questionnaireVersionNumber;
    private String name;
    private String description;
    //Code added for coeus4.3 Questionnaire Maintenance enhancement case#2946
    private boolean finalFlag;
    private boolean completedFlag;
    //Fields added for case 4287:lQuestionnaire Templates
    private String templateName;
    private boolean hasTemplate;
    private QuestionnaireTemplateBean questionnaireTemplateBean;

    private int modSubCode;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    private int groupTypeCode;
    private String updateUserName;
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
    
    public String getCUser() {
        return CUser;
    }

    public void setCUser(String CUser) {
        this.CUser = CUser;
    }

    public int getModSubCode() {
        return modSubCode;
    }

    public void setModSubCode(int modSubCode) {
        this.modSubCode = modSubCode;
    }

    private String CUser;






    //4287 - End
    /** Creates a new instance of QuestionnaireBaseBean */
    public QuestionnaireBaseBean() {
    }
    
    /**
     * Getter for property questionnaireId.
     * @return Value of property questionnaireId.
     */
    public int getQuestionnaireId() {
        return questionnaireId;
    }
    
    /**
     * Setter for property questionnaireId.
     * @param questionnaireId New value of property questionnaireId.
     */
    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
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
        QuestionnaireBaseBean questionnaireBaseBean = (QuestionnaireBaseBean)obj;
        if(questionnaireBaseBean.getQuestionnaireId()== getQuestionnaireId()){
            return true;
        }else{
            return false;
        }
    }    

    /**
     * Getter for property finalFlag.
     * @return Value of property finalFlag.
     */    
    public boolean isFinalFlag() {
        return finalFlag;
    }

    /**
     * Setter for property finalFlag.
     * @param finalFlag New value of property finalFlag.
     */    
    public void setFinalFlag(boolean finalFlag) {
        this.finalFlag = finalFlag;
    }

    /**
     * Getter for property completedFlag.
     * @return Value of property completedFlag.
     */
    public boolean isCompletedFlag() {
        return completedFlag;
    }

    /**
     * Setter for property completedFlag.
     * @param completedFlag New value of property completedFlag.
     */
    public void setCompletedFlag(boolean completedFlag) {
        this.completedFlag = completedFlag;
    }
    //Getters and setters added with case 4287:Questionnaire templates - Start
    /**
     * Getter for property questionnaireTemplateBean.
     * @return Value of property questionnaireTemplateBean.
     */
    public QuestionnaireTemplateBean getQuestionnaireTemplateBean() {
        return questionnaireTemplateBean;
    }
    /**
     * Setter for property questionnaireTemplateBean.
     * @param questionnaireTemplateBean New value of property questionnaireTemplateBean.
     */
    public void setQuestionnaireTemplateBean(QuestionnaireTemplateBean questionnaireTemplateBean) {
        this.questionnaireTemplateBean = questionnaireTemplateBean;
    }
    /**
     * Getter for property templateName.
     * @return Value of property templateName.
     */
    public String getTemplateName() {
        return templateName;
    }
    /**
     * Setter for property templateName.
     * @param templateName New value of property templateName.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    /**
     * Getter for property hasTemplate.
     * @return Value of property hasTemplate.
     */
    public boolean getHasTemplate() {
        return hasTemplate;
    }
    /**
     * Setter for property hasTemplate.
     * @param templateName New value of property hasTemplate.
     */
    public void setHasTemplate(boolean hasTemplate) {
        this.hasTemplate = hasTemplate;
    }

 //4287 - End
    // 4272: Maintain history of Questionnaires - Start
    public int getQuestionnaireVersionNumber() {
        return questionnaireVersionNumber;
    }

    public void setQuestionnaireVersionNumber(int questionnaireVersionNumber) {
        this.questionnaireVersionNumber = questionnaireVersionNumber;
    }
    // 4272: Maintain history of Questionnaires - End
    // Added this method for Sorting using final flag in Questionnaire Maintenance Window
    // CoeusVector.sort() uses  "get + Property Name" method to get the value for sorting.
    // CoeusVector.sort() does not work with getter methods that starts with 'is'
    public boolean getFinalFlag(){
        return finalFlag;
    }
        
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - Start
    /**
     * Getter method to get the group type code
     * @return groupTypeCode
     */
    public int getGroupTypeCode() {
        return groupTypeCode;
    }
    
    /**
     * Setter method to get the group type code
     * @param groupTypeCode
     */
    public void setGroupTypeCode(int groupTypeCode) {
        this.groupTypeCode = groupTypeCode;
    }

    /**
     * Method to get update user name
     * @return updateUserName
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Method to set update user name
     * @param updateUserName 
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    // Added for COEUSQA-3287 Questionnaire Maintenance Features - End
}
