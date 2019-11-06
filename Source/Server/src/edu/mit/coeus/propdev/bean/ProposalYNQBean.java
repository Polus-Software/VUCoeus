/**
 * @(#)ProposalYNQBean.java 1.0 5/19/03
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;

/**
 * This class used to set and gets the Proposal question answers details
 * and reviewed date
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 19, 2003 3:30 PM
 */

public class ProposalYNQBean implements java.io.Serializable{
    
    private String proposalNumber;
    private String questionId;
    private String answer;
    private String explanation;
    private String reviewDate;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    
    /**
     *	Default Constructor
     */
    
    public ProposalYNQBean(){
    }
    
    /**
     *	This method is used to fetch the Organization Id
     *	@return String proposalNumber
     */
    
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     *	This method is used to set the Organization Id
     *	@param proposalNumber String 
     */
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     *	This method is used to fetch the Explanation
     *	@return String explanation
     */
    
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
    
    /**
     *	This method is used to fetch the Review Date
     *	@return String reviewDate
     */
    
    public String getReviewDate() {
        return reviewDate;
    }
    
    /**
     *	This method is used to set the Review Date
     *	@param reviewDate String 
     */
    
    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
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
     *	This method is used to fetch the Answer
     *	@return String answer
     */
    
    public String getAnswer() {
        return answer;
    }
    
    /**
     *	This method is used to set the Answer
     *	@param answer String 
     */
    
    public void setAnswer(String answer) {
        this.answer = answer;
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
    
    /**
     *	This method is used to get the action type Add/Update
     *	@return String updateUser
     */
    
    public String getAcType() {
        return acType;
    }
    
    /**
     *	This method is used to set the action type Add/Update
     *	@param	acType String 
     */
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
}
