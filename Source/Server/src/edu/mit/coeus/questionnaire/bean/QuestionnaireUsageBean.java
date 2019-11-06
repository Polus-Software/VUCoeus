/*
 * QuestionnaireUsageBean.java
 *
 * Created on September 19, 2006, 2:36 PM
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
public class QuestionnaireUsageBean extends QuestionnaireBaseBean implements BaseBean{
    private int moduleItemCode;
    private int moduleSubItemCode;
    private int ruleId;
    private String label;
    private Timestamp usageTimestamp;
    private String usageUser;
    private int awModuleItemCode;
    private int awModuleSubItemCode;
    // 4272: Maintain history of Questionnaires
    private boolean mandatory;
    /** Creates a new instance of QuestionnaireUsageBean */
    public QuestionnaireUsageBean() {
    }
    
    /**
     * Getter for property moduleItemCode.
     * @return Value of property moduleItemCode.
     */
    public int getModuleItemCode() {
        return moduleItemCode;
    }
    
    /**
     * Setter for property moduleItemCode.
     * @param moduleItemCode New value of property moduleItemCode.
     */
    public void setModuleItemCode(int moduleItemCode) {
        this.moduleItemCode = moduleItemCode;
    }
    
    /**
     * Getter for property moduleSubItemCode.
     * @return Value of property moduleSubItemCode.
     */
    public int getModuleSubItemCode() {
        return moduleSubItemCode;
    }
    
    /**
     * Setter for property moduleSubItemCode.
     * @param moduleSubItemCode New value of property moduleSubItemCode.
     */
    public void setModuleSubItemCode(int moduleSubItemCode) {
        this.moduleSubItemCode = moduleSubItemCode;
    }
    
    /**
     * Getter for property ruleId.
     * @return Value of property ruleId.
     */
    public int getRuleId() {
        return ruleId;
    }
    
    /**
     * Setter for property ruleId.
     * @param ruleId New value of property ruleId.
     */
    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }
    
    /**
     * Getter for property label.
     * @return Value of property label.
     */
    public java.lang.String getLabel() {
        return label;
    }
    
    /**
     * Setter for property label.
     * @param label New value of property label.
     */
    public void setLabel(java.lang.String label) {
        this.label = label;
    }
    
    public String toString(){
        StringBuffer strBffr = new StringBuffer();
        
        strBffr.append("AcType =>"+getAcType());
        strBffr.append(";");
        strBffr.append("Questionnaire Id  =>"+getQuestionnaireId());
        strBffr.append(";");
        strBffr.append("Questionnaire Name =>"+getName());
        strBffr.append(";");
        strBffr.append("Questionniare Description =>"+getDescription());
        strBffr.append(";");
        strBffr.append("Module Item Code =>"+this.getModuleItemCode());
        strBffr.append(";");
        strBffr.append("Sub Module Item Code =>"+this.getModuleSubItemCode());
        strBffr.append(";");
        strBffr.append("Rule Id =>"+this.getRuleId());
        strBffr.append(";");
        strBffr.append("Questionnaire Label =>"+this.getLabel());
        strBffr.append(";");
        return strBffr.toString();
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
        QuestionnaireUsageBean questionnaireUsageBean = (QuestionnaireUsageBean)obj;
        if(questionnaireUsageBean.getModuleItemCode() == getModuleItemCode() &&
        questionnaireUsageBean.getModuleSubItemCode() == getModuleSubItemCode()){
            return true;
        }else{
            return false;
        }
    }    
    
    /**
     * Getter for property usageTimestamp.
     * @return Value of property usageTimestamp.
     */
    public java.sql.Timestamp getUsageTimestamp() {
        return usageTimestamp;
    }    
    
    /**
     * Setter for property usageTimestamp.
     * @param usageTimestamp New value of property usageTimestamp.
     */
    public void setUsageTimestamp(java.sql.Timestamp usageTimestamp) {
        this.usageTimestamp = usageTimestamp;
    }    
    
    /**
     * Getter for property usageUser.
     * @return Value of property usageUser.
     */
    public java.lang.String getUsageUser() {
        return usageUser;
    }
    
    /**
     * Setter for property usageUser.
     * @param usageUser New value of property usageUser.
     */
    public void setUsageUser(java.lang.String usageUser) {
        this.usageUser = usageUser;
    }
    
    /**
     * Getter for property awModuleItemCode.
     * @return Value of property awModuleItemCode.
     */
    public int getAwModuleItemCode() {
        return awModuleItemCode;
    }
    
    /**
     * Setter for property awModuleItemCode.
     * @param awModuleItemCode New value of property awModuleItemCode.
     */
    public void setAwModuleItemCode(int awModuleItemCode) {
        this.awModuleItemCode = awModuleItemCode;
    }
    
    /**
     * Getter for property awModuleSubItemCode.
     * @return Value of property awModuleSubItemCode.
     */
    public int getAwModuleSubItemCode() {
        return awModuleSubItemCode;
    }    
    
    /**
     * Setter for property awModuleSubItemCode.
     * @param awModuleSubItemCode New value of property awModuleSubItemCode.
     */
    public void setAwModuleSubItemCode(int awModuleSubItemCode) {
        this.awModuleSubItemCode = awModuleSubItemCode;
    }
    // 4272: Maintain history of Questionnaires - Start
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
    // 4272: Maintain history of Questionnaires - End
}
