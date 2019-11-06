/*
 * QABean.java
 *
 * Created on May 9, 2008, 1:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

/**
 *
 * @author blessy
 */
public class QABean {
    
    /** Creates a new instance of QABean */
    public QABean() {
    }
    private Integer questionId;
    private String answer;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
