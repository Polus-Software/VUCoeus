/*
 * @(#)QuestionDetailsBean.java 1.0 3/21/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is for getting and setting various details/values concerning Certification Questions.
 *
 * @version 1.0 March 21, 2002, 2:12 PM
 * @author  Anil Nandakumar
 */
public class QuestionDetailsBean extends java.lang.Object {
    private String policy;
    private String regulation;
    private String questionID;
    private String explanation;
    private String description;
    
    private String explanationType;
    
    /**
     * Get Policy information of question
     */
    public String getPolicy(){
        return this.policy;
    }
    
    /**
     * Set Policy information of a question
     */
    public void setPolicy(String policy){
        this.policy=policy;
    }
    
    /**
     * Get Regulation information of question
     */
    public String getRegulation(){
        return this.regulation;
    }
    
    /**
     * Set Regulation information of a question
     */
    public void setRegulation(String regulation){
        this.regulation=regulation;
    }
    
    
    
    
    /**
     *  This method gets the Question ID
     *  @return String questionID
     */
    public String getQuestionID() {
        return questionID;
    }
    /**
     *  This method sets the Question ID
     *  @param String questionID
     */
    public void setQuestionID(java.lang.String questionID) {
        this.questionID = UtilFactory.checkNullStr(questionID);
    }
    /**
     *  This method gets the Explanation
     *  @return String explanation
     */
    public String getExplanation() {
        return explanation;
    }
    /**
     *  This method sets the Explanation
     *  @param String explanation
     */
    public void setExplanation(java.lang.String explanation) {
        this.explanation = UtilFactory.checkNullStr(explanation);
    }
    /**
     *  This method gets the Description
     *  @return String description
     */
    public String getDescription() {
        return description;
    }
    /**
     *  This method sets the Description
     *  @param String description
     */
    public void setDescription(java.lang.String description) {
        this.description = UtilFactory.checkNullStr(description);
    }
    /**
     *  This method gets the Explanation Type
     *  @return String explanationType
     */
    public String getExplanationType() {
        return explanationType;
    }
    /**
     *  This method sets the  Explanation Type
     *  @param String explanationType
     */
    public void setExplanationType(java.lang.String explanationType) {
        this.explanationType = UtilFactory.checkNullStr(explanationType);
    }
}