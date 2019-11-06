/*
 * QuestionAnswerBean.java
 *
 * Created on October 3, 2006, 2:35 PM
 */

package edu.mit.coeus.questionnaire.bean;

import java.sql.Timestamp;

/**
 *
 * @author  vinayks
 */
public class QuestionAnswerBean extends QuestionBaseBean implements java.io.Serializable {

    /** Creates a new instance of QuestionAnswerBean */
    public QuestionAnswerBean() {
    }


    private String questionnaireCompletionId;
    private int questionNumber ;
    private int answerNumber ;
    private String answer ;
    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    // To set the the submodulecode/moduleItemKey/Sequence for the answers where it actually applies to
    // This is not a db column and is only used for tracking purpose in amendment/renewals
    // to identify the details of original protocol answers to be populated in amendment/renewals
    private int answeredSubmoduleCode;
    private String answeredModuleItemKey;
    private int answeredModuleSubItemKey;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    private Timestamp awUpdateTimestamp;
    private String awQuestionnaireCompletionId;
    private int awQuestionNumber ;
    private int awAnswerNumber ;
    /*ppc*/
    private String answeredModuleSubItemKeyForppc;
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    private String awAnswer ;
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - end
    
    public String getAnsweredModuleSubItemKeyForppc() {
        return answeredModuleSubItemKeyForppc;
    }

    public void setAnsweredModuleSubItemKeyForppc(String answeredModuleSubItemKeyForppc) {
        this.answeredModuleSubItemKeyForppc = answeredModuleSubItemKeyForppc;
    }

    /**
     * Getter for property questionNumber.
     * @return Value of property questionNumber.
     */
    public int getQuestionNumber() {
        return questionNumber;
    }

    /**
     * Setter for property questionNumber.
     * @param questionNumber New value of property questionNumber.
     */
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * Getter for property answerNumber.
     * @return Value of property answerNumber.
     */
    public int getAnswerNumber() {
        return answerNumber;
    }

    /**
     * Setter for property answerNumber.
     * @param answerNumber New value of property answerNumber.
     */
    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    /**
     * Getter for property answer.
     * @return Value of property answer.
     */
    public java.lang.String getAnswer() {
        return answer;
    }

    /**
     * Setter for property answer.
     * @param answer New value of property answer.
     */
    public void setAnswer(java.lang.String answer) {
        this.answer = answer;
    }

    /**
     * Getter for property awUpdateTimestamp.
     * @return Value of property awUpdateTimestamp.
     */
    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    /**
     * Setter for property awUpdateTimestamp.
     * @param awUpdateTimestamp New value of property awUpdateTimestamp.
     */
    public void setAwUpdateTimestamp(Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }


    /**
     * Getter for property awQuestionNumber.
     * @return Value of property awQuestionNumber.
     */
    public int getAwQuestionNumber() {
        return awQuestionNumber;
    }

    /**
     * Setter for property awQuestionNumber.
     * @param awQuestionNumber New value of property awQuestionNumber.
     */
    public void setAwQuestionNumber(int awQuestionNumber) {
        this.awQuestionNumber = awQuestionNumber;
    }

    /**
     * Getter for property awAnswerNumber.
     * @return Value of property awAnswerNumber.
     */
    public int getAwAnswerNumber() {
        return awAnswerNumber;
    }

    /**
     * Setter for property awAnswerNumber.
     * @param awAnswerNumber New value of property awAnswerNumber.
     */
    public void setAwAnswerNumber(int awAnswerNumber) {
        this.awAnswerNumber = awAnswerNumber;
    }

    /**
     * Getter for property questionnaireCompletionId.
     * @return Value of property questionnaireCompletionId.
     */
    public java.lang.String getQuestionnaireCompletionId() {
        return questionnaireCompletionId;
    }

    /**
     * Setter for property questionnaireCompletionId.
     * @param questionnaireCompletionId New value of property questionnaireCompletionId.
     */
    public void setQuestionnaireCompletionId(java.lang.String questionnaireCompletionId) {
        this.questionnaireCompletionId = questionnaireCompletionId;
    }

    /**
     * Getter for property awQuestionnaireCompletionId.
     * @return Value of property awQuestionnaireCompletionId.
     */
    public java.lang.String getAwQuestionnaireCompletionId() {
        return awQuestionnaireCompletionId;
    }

    /**
     * Setter for property awQuestionnaireCompletionId.
     * @param awQuestionnaireCompletionId New value of property awQuestionnaireCompletionId.
     */
    public void setAwQuestionnaireCompletionId(java.lang.String awQuestionnaireCompletionId) {
        this.awQuestionnaireCompletionId = awQuestionnaireCompletionId;
    }

    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Getter for property answeredSubmoduleCode.
     * @return Value of property answeredSubmoduleCode.
     */
    public int getAnsweredSubmoduleCode() {
        return answeredSubmoduleCode;
    }

    /**
     * Setter for property answeredSubmoduleCode.
     * @param answeredSubmoduleCode New value of property answeredSubmoduleCode.
     */
    public void setAnsweredSubmoduleCode(int answeredSubmoduleCode) {
        this.answeredSubmoduleCode = answeredSubmoduleCode;
    }

    /**
     * Setter for property answeredModuleItemKey.
     * @param answeredModuleItemKey New value of property answeredModuleItemKey.
     */
    public void setAnsweredModuleItemKey(String answeredModuleItemKey) {
        this.answeredModuleItemKey = answeredModuleItemKey;
    }

    /**
     * Getter for property answeredModuleItemKey.
     * @return Value of property answeredModuleItemKey.
     */
    public String getAnsweredModuleItemKey() {
        return answeredModuleItemKey;
    }

    /**
     * Getter for property answeredModuleSubItemKey.
     * @return Value of property answeredModuleSubItemKey.
     */
    public int getAnsweredModuleSubItemKey() {
        return answeredModuleSubItemKey;
    }

    /**
     * Setter for property answeredModuleSubItemKey.
     * @param answeredModuleSubItemKey New value of property answeredModuleSubItemKey.
     */
    public void setAnsweredModuleSubItemKey(int answeredModuleSubItemKey) {
        this.answeredModuleSubItemKey = answeredModuleSubItemKey;
    }
    // CoeusQA2313: Completion of Questionnaire for Submission- End

    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * Method to get the aw answer
     * @return awAnswer
     */
    public String getAwAnswer() {
        return awAnswer;
    }

    /**
     * Method to set the aw answer
     * @param awAnswer 
     */
    public void setAwAnswer(String awAnswer) {
        this.awAnswer = awAnswer;
    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
}
