/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.coiv2.beans;

/**
 *
 * @author Sony
 */
public class CoiQuestionAnswerBean {

    public CoiQuestionAnswerBean() {
    }

    private String questionId;
    private String question;
    private String answer;
    private String answerString;
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
     * @return the answerString
     */
    public String getAnswerString() {
        if(getAnswer().equalsIgnoreCase("Y")){
            answerString = "Yes";
        }else if(getAnswer().equalsIgnoreCase("N")){
            answerString = "No";
        }else{
            answerString = getAnswer();
        }
        return answerString;
    }

    /**
     * @param answerString the answerString to set
     */
    public void setAnswerString(String answerString) {
        this.answerString = answerString;
    }

    
}
