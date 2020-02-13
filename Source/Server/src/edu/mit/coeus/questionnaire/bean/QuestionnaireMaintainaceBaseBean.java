/*
 * QuestionnaireBaseBean.java
 *
 * Created on September 19, 2006, 2:15 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.sql.Timestamp;

/**
 *
 * @author  chandrashekara
 */
public class QuestionnaireMaintainaceBaseBean implements BaseBean, java.io.Serializable{
    private String acType;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String unitNumber;
    private int questionnaireNumber;
    /** Creates a new instance of QuestionnaireBaseBean */
    public QuestionnaireMaintainaceBaseBean() {
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /**
     * Getter for property questionnaireNumber.
     * @return Value of property questionnaireNumber.
     */
    public int getQuestionnaireNumber() {
        return questionnaireNumber;
    }
    
    /**
     * Setter for property questionnaireNumber.
     * @param questionnaireNumber New value of property questionnaireNumber.
     */
    public void setQuestionnaireNumber(int questionnaireNumber) {
        this.questionnaireNumber = questionnaireNumber;
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
        QuestionnaireMaintainaceBaseBean questionnaireMaintainaceBaseBean = (QuestionnaireMaintainaceBaseBean)obj;
        if(questionnaireMaintainaceBaseBean.getQuestionnaireNumber() == getQuestionnaireNumber()){
            return true;
        }else{
            return false;
        }
    }    
    
    
}
