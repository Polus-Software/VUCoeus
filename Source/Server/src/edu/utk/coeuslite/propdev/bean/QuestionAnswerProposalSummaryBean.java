/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utk.coeuslite.propdev.bean;

/**
 *
 * @author athul
 */
public class QuestionAnswerProposalSummaryBean {

    private String questionnaireId;
    private String questionnaireName;
    private String questionId;
    private String question;
    private String answer;
    private String validAnswers;
    private String versionnumber;

    /**
     * @return the validAnswers
     */
    public String getValidAnswers() {
        return validAnswers;
    }

    /**
     * @param validAnswers the validAnswers to set
     */
    public void setValidAnswers(String validAnswers) {
        this.validAnswers = validAnswers;
    }

    /**
     * @return the questionnaireId
     */
    public String getQuestionnaireId() {
        return questionnaireId;
    }

    /**
     * @param questionnaireId the questionnaireId to set
     */
    public void setQuestionnaireId(String questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    /**
     * @return the questionnaireName
     */
    public String getQuestionnaireName() {
        return questionnaireName;
    }

    /**
     * @param questionnaireName the questionnaireName to set
     */
    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

    /**
     * @return the questionId
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * @param questionId the questionId to set
     */
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    /**
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question the question to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * @param answer the answer to set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    /**
     * @return the versionnumber
     */
    public String getVersionnumber() {
        return versionnumber;
    }

    /**
     * @param versionnumber the versionnumber to set
     */
    public void setVersionnumber(String versionnumber) {
        this.versionnumber = versionnumber;
    }
}
