/*
 * QuestionBaseBean.java
 *
 * Created on September 19, 2006, 2:18 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author  chandrashekara
 */
public class QuestionBaseBean extends QuestionnaireBaseBean implements BaseBean{
    private Integer questionId;
    private String description;
    // 4272: Maintain history of Questionnaires
    private int versionNumber;
    /** Creates a new instance of QuestionBaseBean */
    public QuestionBaseBean() {
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
        QuestionBaseBean questionBaseBean = (QuestionBaseBean)obj;
        if(questionBaseBean.getQuestionId().equals(getQuestionId())){
            return true;
        }else{
            return false;
        }
    }    
    // 4272: Maintain history of Questionnaires  - Start
    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
   // 4272: Maintain history of Questionnaires - End  
}
