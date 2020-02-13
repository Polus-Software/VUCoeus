/**
 * @(#)QuestionListBean.java 1.0 8/22/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;

/**
 * This class used to set and gets the Organization questions list
 *
 * @version :1.0 August 22,2002
 * @author Guptha K
 */

public class QuestionListBean implements java.io.Serializable{

    private String questionId;
    private String description;
    private int noOfAnswers;
    private String explanationRequiredFor;
    private String dateRequiredFor;

	/**
	 *	Default Constructor
	 */
	 
    public QuestionListBean(){
    }

	/**
	 *	This method is used to fetch the Question Id
	 *	@return String questionId
	 */

    public String getQuestionId() {
        return questionId;
    }

	/**
	 *	This method is used to set the Question Id
	 *	@param questionId String 
	 */

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

	/**
	 *	This method is used to fetch the description
	 *	@return String description
	 */

    public String getDescription() {
        return description;
    }

	/**
	 *	This method is used to set the description
	 *	@param description String 
	 */

    public void setDescription(String description) {
        this.description = description;
    }

	/**
	 *	This method is used to fetch the Number of Answers
	 *	@return int noOfAnswers
	 */

    public int getNoOfAnswers() {
        return noOfAnswers;
    }

	/**
	 *	This method is used to set the Number of Answers
	 *	@param  noOfAnswers int 
	 */

    public void setNoOfAnswers(int noOfAnswers) {
        this.noOfAnswers = noOfAnswers;
    }

	/**
	 *	This method is used to fetch the question for which Explanation is Required
	 *	@return String explanationRequiredFor
	 */

    public String getExplanationRequiredFor() {
        return explanationRequiredFor;
    }

	/**
	 *	This method is used to set the question for which Explanation is Required
	 *	@param explanationRequiredFor String 
	 */

    public void setExplanationRequiredFor(String explanationRequiredFor) {
        this.explanationRequiredFor = explanationRequiredFor;
    }

	/**
	 *	This method is used to fetch the question for which Date is Required
	 *	@return String dateRequiredFor
	 */

    public String getDateRequiredFor() {
        return dateRequiredFor;
    }

	/**
	 *	This method is used to set the question for which date is Required
	 *	@param dateRequiredFor String 
	 */

    public void setDateRequiredFor(String dateRequiredFor) {
        this.dateRequiredFor = dateRequiredFor;
    }
}