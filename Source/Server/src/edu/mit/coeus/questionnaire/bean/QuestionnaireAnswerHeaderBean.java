/*
 * QuestionnaireAnswerHeaderBean.java
 *
 * Created on October 3, 2006, 2:29 PM
 */

package edu.mit.coeus.questionnaire.bean;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author  vinayks
 */
public class QuestionnaireAnswerHeaderBean extends QuestionnaireUsageBean  {

    /** Creates a new instance of QuestionnaireAnswerHeaderBean */
    public QuestionnaireAnswerHeaderBean() {
    }


    private String currentUser;
    private String currentPersonId;

    public String getCurrentPersonId() {
        return currentPersonId;
    }

    public void setCurrentPersonId(String currentPersonId) {
        this.currentPersonId = currentPersonId;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
    private String moduleItemKey ;
    private String moduleSubItemKey ;
    private String moduleItemDescription;
    private String moduleSubItemDescription;
    private String questionnaireCompletionId ;
    private Timestamp awUpdateTimestamp;
    //Code added for coeus4.3 Questionnaire enhancement case#2946
    private String questionnaireCompletionFlag;
    private boolean printAll;
    private boolean printOnlyAnswered;
    private boolean printAnswers;
    // Added for CoeusQA2313: Completion of Questionnaire for Submission
    // To set the the submodulecode/moduleItemKey/Sequence for the questionnaire where it actually applies to
    // This is not a db column and is only used for tracking purpose in amendment/renewals
    // to identify the details of original protocol answers to be populated in amendment/renewals
    private int applicableSubmoduleCode;
    private String applicableModuleItemKey ;
    private int applicableModuleSubItemKey ;
    private String applicableModuleSubItemKeyForPpc ;

    public String getApplicableModuleSubItemKeyForPpc() {
        return applicableModuleSubItemKeyForPpc;
    }

    public void setApplicableModuleSubItemKeyForPpc(String applicableModuleSubItemKeyForPpc) {
        this.applicableModuleSubItemKeyForPpc = applicableModuleSubItemKeyForPpc;
    }


    // CoeusQA2313: Completion of Questionnaire for Submission - End

    /**
     * Getter for property moduleItemKey.
     * @return Value of property moduleItemKey.
     */
    public java.lang.String getModuleItemKey() {
        return moduleItemKey;
    }

    /**
     * Setter for property moduleItemKey.
     * @param moduleItemKey New value of property moduleItemKey.
     */
    public void setModuleItemKey(java.lang.String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }


    /**
     * Getter for property moduleSubItemKey.
     * @return Value of property moduleSubItemKey.
     */
    public java.lang.String getModuleSubItemKey() {
        return moduleSubItemKey;
    }

    /**
     * Setter for property moduleSubItemKey.
     * @param moduleSubItemKey New value of property moduleSubItemKey.
     */
    public void setModuleSubItemKey(java.lang.String moduleSubItemKey) {
        this.moduleSubItemKey = moduleSubItemKey;
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
        QuestionnaireAnswerHeaderBean questionnaireAnswerHeaderBean = (QuestionnaireAnswerHeaderBean)obj;
        if(questionnaireAnswerHeaderBean.getModuleItemKey().equals(getModuleItemKey()) &&
        questionnaireAnswerHeaderBean.getModuleSubItemKey().equals(getModuleSubItemKey())){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Getter for property moduleItemDescription.
     * @return Value of property moduleItemDescription.
     */
    public java.lang.String getModuleItemDescription() {
        return moduleItemDescription;
    }

    /**
     * Setter for property moduleItemDescription.
     * @param moduleItemDescription New value of property moduleItemDescription.
     */
    public void setModuleItemDescription(java.lang.String moduleItemDescription) {
        this.moduleItemDescription = moduleItemDescription;
    }

    /**
     * Getter for property moduleSubItemDescription.
     * @return Value of property moduleSubItemDescription.
     */
    public java.lang.String getModuleSubItemDescription() {
        return moduleSubItemDescription;
    }

    /**
     * Setter for property moduleSubItemDescription.
     * @param moduleSubItemDescription New value of property moduleSubItemDescription.
     */
    public void setModuleSubItemDescription(java.lang.String moduleSubItemDescription) {
        this.moduleSubItemDescription = moduleSubItemDescription;
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
     * Getter for property questionnaireCompletionFlag.
     * @return Value of property questionnaireCompletionFlag.
     */
    public String getQuestionnaireCompletionFlag() {
        return questionnaireCompletionFlag;
    }

    /**
     * Setter for property questionnaireCompletionFlag.
     * @param questionnaireCompletionFlag New value of property questionnaireCompletionFlag.
     */
    public void setQuestionnaireCompletionFlag(String questionnaireCompletionFlag) {
        this.questionnaireCompletionFlag = questionnaireCompletionFlag;
    }

    /**
     * Setter for property printAll
     * @param printAll used to set whether to print all questions
     */
    public void setPrintAll(boolean printAll) {
        this.printAll = printAll;
    }

    /**
     * Getter for property printAll
     * @return value of property printAll
     */
    public boolean getPrintAll() {
        return printAll;
    }

    /**
     * Setter for property printOnlyAnswered
     * @param printOnlyAnswered used to set whether to print only answered questions.
     */
    public void setPrintOnlyAnswered(boolean printOnlyAnswered) {
        this.printOnlyAnswered = printOnlyAnswered;
    }

    /**
     * Getter for property printOnlyAnswered
     * @return value of property printOnlyAnswered
     */
    public boolean getPrintOnlyAnswered() {
        return printOnlyAnswered;
    }

    /**
     * Setter for property printAnswers
     * @param printAnswers used to set whether to print answers or not
     */
    public void setPrintAnswers(boolean printAnswers) {
        this.printAnswers = printAnswers;
    }

    /**
     * Getter for property printAnswers
     * @return value of property printAnswers
     */
    public boolean getPrintAnswers() {
        return printAnswers;
    }
    // Added with CoeusQA2313: Completion of Questionnaire for Submission
    /**
     * Getter for property answeredModuleSubItemCode
     * @return value of property answeredModuleSubItemCode
     */
    public int getApplicableSubmoduleCode() {
        return applicableSubmoduleCode;
    }

    /**
     * Setter for property answeredModuleSubItemCode
     * @param answeredModuleSubItemCode used to set answered submodule Code.
     */
    public void setApplicableSubmoduleCode(int applicableSubmoduleCode) {
        this.applicableSubmoduleCode = applicableSubmoduleCode;
    }

    /**
     * Getter for property applicableModuleItemKey
     * @return value of property applicableModuleItemKey
     */
    public String getApplicableModuleItemKey() {
        return applicableModuleItemKey;
    }

    /**
     * Setter for property applicableModuleItemKey
     * @param applicableModuleItemKey used to set answered module item key.
     */
    public void setApplicableModuleItemKey(String applicableModuleItemKey) {
        this.applicableModuleItemKey = applicableModuleItemKey;
    }

    /**
     * Getter for property applicableModuleSubItemKey
     * @return value of property applicableModuleSubItemKey
     */
    public int getApplicableModuleSubItemKey() {
        return applicableModuleSubItemKey;
    }

    /**
     * Setter for property applicableModuleSubItemKey
     * @param applicableModuleSubItemKey used to set answered module sub item key.
     */
    public void setApplicableModuleSubItemKey(int applicableModuleSubItemKey) {
        this.applicableModuleSubItemKey = applicableModuleSubItemKey;
    }
    // CoeusQA2313: Completion of Questionnaire for Submission - End

}
