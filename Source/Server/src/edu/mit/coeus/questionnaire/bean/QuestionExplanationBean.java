/*
 * QuestionExplanationBean.java
 *
 * Created on September 9, 2008, 12:22 PM
 *
 */

package edu.mit.coeus.questionnaire.bean;

/**
 *
 * @author sreenathv
 */
public class QuestionExplanationBean extends QuestionBaseBean implements java.io.Serializable{
    
    private String explanationType;
    private String explanation;
    
    /** Creates a new instance of QuestionExplanationBean */
    public QuestionExplanationBean() {
    }

    public String getExplanationType() {
        return explanationType;
    }

    public void setExplanationType(String explanationType) {
        this.explanationType = explanationType;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public boolean equals(Object obj) {
        QuestionExplanationBean bean = (QuestionExplanationBean)obj;
        if(bean.getQuestionId().equals(getQuestionId()) &&
                bean.getExplanationType().equals(getExplanationType())) {
            return true;
        }else{
            return false;
        }
    }
}
