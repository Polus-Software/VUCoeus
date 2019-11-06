/**
 * @(#)YNQBean.java 1.0 4/22/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.utils.question.bean;


import java.sql.Timestamp;
import java.sql.Date;
import java.beans.*;
/**
 * This class used to set and gets the Organization questions's explanation
 * and reviewed date
 *
 * @version :1.0 August 22,2002
 * @author Guptha K
 * @updated Subramanya: 1.1 April 23 2003
 */

public class YNQBean implements java.io.Serializable{

    private static final String YNQ_QUESTION_ID="questionId";
    private static final String YNQ_QUESTION_TYPE ="questionType";
    private static final String YNQ_ANSWER ="answer";
    private static final String YNQ_EXPLAN_REQ_FOR="explanationRequiredFor";
    private static final String YNQ_STATUS = "status";
    private static final String YNQ_EFFECTIVE_DATE = "effectiveDate";


    //holds the question id
    private String questionId;

    //holds the question description
    private String questionDesc;

    //holds the question type
    private char questionType;

    //holds the number of answer ( options )
    private String answer;

    //holds the explanation required for
    private String explanationRequiredFor;

    //holds the date required for
    private String dateRequiredFor;

    //holds the statue
    private String status;

    //holds the effective date
    private Date effectiveDate;

    //holds the update time stamp
    private Timestamp updateTimeStamp;

    //holds the update user.
    private String updateUser;

    //private String explanation;
    private String explanation;
    //private String reviewDate;

     private PropertyChangeSupport propertySupport;

    //holds account type
    private String acType;



    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     *	Default Constructor
     */

    public YNQBean(){
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * This constructor is used to construct ynq bean with all its data
     */
     /*
    public YNQBean( String questionId, String questionDesc, char questionType,
                    String answer,String explanationRequiredFor,
                    String dateRequiredFor,String status,Date effectiveDate,
                    Timestamp updateTimeStamp, String updateUser ){
        this.questionId = questionId;
        this.questionDesc = questionDesc;
        this.questionType = questionType;
        this.answer = answer;
        this.explanationRequiredFor = explanationRequiredFor;
        this.dateRequiredFor = dateRequiredFor;
        this.status = status;
        this.effectiveDate = effectiveDate;
        this.updateTimeStamp = updateTimeStamp;
        this.updateUser = updateUser;
    }
    */
    /**
     *	This method is used to fetch the Question Id
     *	@return String questionId
     */

    public String getQuestionId() {
        return this.questionId;
    }

    /**
     *	This method is used to set the Organization Id
     *	@param orgId String
     */

    public void setQuestionId(String newQuestionId) {
        String oldQuestionId = questionId;
        this.questionId = newQuestionId;
        propertySupport.firePropertyChange(YNQ_QUESTION_ID,
               oldQuestionId, questionId);
    }

    /**
     *	This method is used to fetch the Question Description.
     *	@return String questionId
     */

    public String getQuestionDesc() {
        return this.questionDesc;
    }

    /**
     *	This method is used to set the question description.
     *	@param questionDesc String
     */

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }


    /**
     *	This method is used to get the question type
     *	@return char question type
     */

    public char getQuestionType() {
        return this.questionType;
    }

    /**
     *	This method is used to set the question Type.
     *	@param	acType String
     */

    public void setQuestionType(char newQuestionType) {
        char oldQuestionType = questionType;
        this.questionType = newQuestionType;
        propertySupport.firePropertyChange(YNQ_QUESTION_TYPE,
               oldQuestionType, questionType);
    }


    /**
     *	This method is used to fetch the Answer
     *	@return int answer
     */

    public String getAnswer() {
        return this.answer;
    }

    /**
     *	This method is used to set the Answer
     *	@param answer integer value
     */

    public void setAnswer(String newAnswer) {
        String oldAnswer = answer;
        this.answer = newAnswer;
        propertySupport.firePropertyChange(YNQ_ANSWER,
               oldAnswer, answer);
    }


    /**
     *	This method is used to fetch the Explanation Required For.
     *	@return String explanationRequiredFor
     */

    public String getExplanationRequiredFor() {
        return this.explanationRequiredFor;
    }

    /**
     *	This method is used to set the explanation
     *	@param explanationRequiredFor represent String explanation
     */
    public void setExplanationRequiredFor(String newExplanationRequiredFor) {
        String oldExplanationRequiredFor = explanationRequiredFor;
        this.explanationRequiredFor = newExplanationRequiredFor;
        propertySupport.firePropertyChange(YNQ_EXPLAN_REQ_FOR,
               oldExplanationRequiredFor, explanationRequiredFor);
    }

    /**
     *	This method is used to fetch the Required Date
     *	@return String required Date
     */

    public String getRequiredForDate() {
        return this.dateRequiredFor;
    }

    /**
     *	This method is used to set the Review Date
     *	@param dateRequiredFor String
     */

    public void setRequiredForDate(String dateRequiredFor) {
        this.dateRequiredFor = dateRequiredFor;
    }

    /**
     *	This method is used to fetch the Question Status
     *	@return String questionId
     */

    public String getStatus() {
        return this.status;
    }

    /**
     *	This method is used to set the Question Status
     *	@param status String
     */

    public void setStatus(String newStatus) {
        String oldStatus = status;
        this.status = newStatus;
        propertySupport.firePropertyChange(YNQ_STATUS,
               oldStatus, status);
    }



    /**
     *	This method is used to fetch the Effective Date
     *	@return Date effectiveDate
     */

    public Date getEffectiveDate() {
        return this.effectiveDate;
    }

    /**
     *	This method is used to set the Effective Date
     *	@param effectiveDate Date
     */

    public void setEffectiveDate(Date newEffectiveDate) {
        Date oldEffectiveDate = effectiveDate;
        this.effectiveDate = newEffectiveDate;
        propertySupport.firePropertyChange(YNQ_EFFECTIVE_DATE,
               oldEffectiveDate, effectiveDate);
    }


    /**
     *	This method is used to fetch the timestamp of the update
     *	@return String updateTimeStamp
     */

    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    /**
     *	This method is used to set the timestamp of the update
     *	@param updateTimeStamp Timestamp
     */

    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    /**
     *	This method is used to fetch the user who updated the details
     *	@return String updateUser
     */

    public String getUpdateUser() {
        return updateUser;
    }

    /**
     *	This method is used to set the user who updated the details
     *	@param updateUser String
     */

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getExplanation() {
        return explanation;
    }

    /**
     *	This method is used to set the explanation
     *	@param explanation String
     */

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

}
