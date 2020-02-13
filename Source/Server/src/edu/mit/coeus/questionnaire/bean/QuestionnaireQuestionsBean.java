/*
 * QuestionnaireQuestionsBean.java
 *
 * Created on October 5, 2006, 11:26 AM
 */

package edu.mit.coeus.questionnaire.bean;

/**
 *
 * @author  tarique
 */
public class QuestionnaireQuestionsBean extends QuestionnaireBean {
    private String answerDataType;
    private int answerMaxLength;
    private String lookUpGui;
    private String lookUpName;
    private String validAnswer;
    private int maxAnswers;
    private String questionnaireCompletionId;
    private int answerNumber ;
    private String answer ;
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    private int answeredSubmoduleCode;
    private String answeredModuleItemKey;
    private int answeredModuleSubItemKey;
    // CoeusQA2313: Completion of Questionnaire for Submission - End
    private String searchName;
    private String awQuestionnaireCompletionId;
    /** Creates a new instance of QuestionnaireQuestionsBean */
    private String answeredModuleSubItemKeyForPpc;
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    private String awAnswer ;
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
    public String getAnsweredModuleSubItemKeyForPpc() {
        return answeredModuleSubItemKeyForPpc;
    }

    public void setAnsweredModuleSubItemKeyForPpc(String answeredModuleSubItemKeyForPpc) {
        this.answeredModuleSubItemKeyForPpc = answeredModuleSubItemKeyForPpc;
    }
    public QuestionnaireQuestionsBean() {
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
     * Getter for property lookUpGui.
     * @return Value of property lookUpGui.
     */
    public java.lang.String getLookUpGui() {
        return lookUpGui;
    }

    /**
     * Setter for property lookUpGui.
     * @param lookUpGui New value of property lookUpGui.
     */
    public void setLookUpGui(java.lang.String lookUpGui) {
        this.lookUpGui = lookUpGui;
    }

    /**
     * Getter for property lookUpName.
     * @return Value of property lookUpName.
     */
    public java.lang.String getLookUpName() {
        return lookUpName;
    }

    /**
     * Setter for property lookUpName.
     * @param lookUpName New value of property lookUpName.
     */
    public void setLookUpName(java.lang.String lookUpName) {
        this.lookUpName = lookUpName;
    }

    /**
     * Getter for property validAnswer.
     * @return Value of property validAnswer.
     */
    public java.lang.String getValidAnswer() {
        return validAnswer;
    }

    /**
     * Setter for property validAnswer.
     * @param validAnswer New value of property validAnswer.
     */
    public void setValidAnswer(java.lang.String validAnswer) {
        this.validAnswer = validAnswer;
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
     * Getter for property searchName.
     * @return Value of property searchName.
     */
    public java.lang.String getSearchName() {
        return searchName;
    }

    /**
     * Setter for property searchName.
     * @param searchName New value of property searchName.
     */
    public void setSearchName(java.lang.String searchName) {
        this.searchName = searchName;
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

    public void setAnsweredModuleItemKey(String answeredModuleItemKey) {
        this.answeredModuleItemKey = answeredModuleItemKey;
    }


    // CoeusQA2313: Completion of Questionnaire for Submission- End

    public String getAnsweredModuleItemKey() {
        return answeredModuleItemKey;
    }

    public int getAnsweredModuleSubItemKey() {
        return answeredModuleSubItemKey;
    }

    public void setAnsweredModuleSubItemKey(int answeredModuleSubItemKey) {
        this.answeredModuleSubItemKey = answeredModuleSubItemKey;
    }
    
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - Start
    /**
     * Method to get aw answer
     * @return awAnswer
     */
    public String getAwAnswer() {
        return awAnswer;
    }

    /**
     * Method to set aw answer
     * @param awAnswer 
     */
    public void setAwAnswer(String awAnswer) {
        this.awAnswer = awAnswer;
    }
    // Added for COEUSQA-3079  Questionnaire feature to Save and Complete when Modifying Questionnaire answers - End
    
}
