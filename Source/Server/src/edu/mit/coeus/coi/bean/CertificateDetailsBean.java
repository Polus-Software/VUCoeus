/*
 * @(#)CertificateDetailsBean.java 1.0 3/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
import java.sql.Timestamp;
/**
 * This class is used to hold the information pertaining to a question and it's answers.
 * This is used to transfer Question details from one page to another page or
 * one object to another object.
 *
 * @version 1.0 March 21, 2002, 3:32 PM
 * @author  Anil Nandakumar
 */
public class CertificateDetailsBean extends java.lang.Object {
    /*
     *  Holds person id
     */
    private String personId;
    /*
     *  Holds sequence number
     */
    private String seqNumber;
    /*
     *  Holds disclosure number or FinanacialEntity number
     */
    private String number;
    /*
     *  Holds question id
     */
    private String code;
    /*
     *  Holds question decription
     */
    private String question;
    /*
     *  Holds answer
     */
    private String answer;
    /*
     *  Holds explanation for a question
     */
    private String explanation;
    /*
     *  Holds type of the question
     *  ie, F for FinanicialEntity
     *      C for COI disclosure
     */
    private String questionType;
    /*
     *  Holds number of answers
     */
    private String numOfAns;
    /*
     *  Holds the flag value for "Explanation required for" a question
     */
    private String explReqFor;
    /*
     *  Holds the flag value for "date required for" a question
     */
    private String dateReqFor;
    /*
     *  Holds the status information
     */
    private String status;
    /*
     *  Holds effective date
     */
    private Timestamp effDate;
    /*
     * Holds last updated date
     */
    private Timestamp lastUpdate;
    /*
     *  Holds review date
     */
    private Timestamp reviewDate;
    /*
     *  Holds update user
     */
    private String updateUser;
    /*
     *  Holds account type - insert/update
     */
    private String accountType;
    
    /* CASE #1374 Begin */
    /**
     * If the cert question is disclosure type, this field holds the
     * corresponding entity certification question.
     */
    private String correspEntQuestId;
    
    /**
     * If the cert question is disclosure type, this field holds the display
     * label for the corresponding entity cert question.
     */
    private String correspEntQuestLabel;
    
    /**
     * Holds the display label for this certification question.
     */
    private String label;
    
    /* CASE #1374 End */
     
    
    /** Creates new CertificateDetailsBean */
    public CertificateDetailsBean() {
    }
    /**
     *  Get the person Id
     *  @return String person id
     */
    public String getPersonID() {
        return personId;
    }

    /**
     *  Get the account type
     *  @return String account type
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     *  Set the account type
     *  @param String account type
     */
    public void setAccountType(java.lang.String accType) {
        this.accountType = accType;
    }

    /**
     *  Set the person id
     *  @param String person id
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = UtilFactory.checkNullStr(personId);
    }
    /**
     *  Get the sequence number for a particular question
     *  @return String sequence number
     */
    public String getSeqNumber() {
        return seqNumber;
    }
    /**
     *  Set the sequence number for a particular question
     *  @param String sequence number
     */
    public void setSeqNumber(java.lang.String seqNumber) {
        this.seqNumber = UtilFactory.checkNullStr(seqNumber);
    }
    /**
     *  Get the number for a particular question
     *  <br>It returns the number depends on the type of the question.
     *  <li>If the question is of type 'F', it returns FiniancialEntity number
     *  <li>If the question is of type 'C', it returns COI Disclosure number
     *  @return String number
     */
    public String getNumber() {
        return number;
    }
    /**
     *  Set the number for a particular question
     *  @param String number
     */
    public void setNumber(java.lang.String number) {
        this.number = UtilFactory.checkNullStr(number);
    }
    /**
     *  Get the question id
     *  @return String question id
     */
    public String getCode() {
        return code;
    }
    /**
     *  Set the question id
     *  @return String question id
     */
    public void setCode(java.lang.String code) {
        this.code = UtilFactory.checkNullStr(code);
    }
    /**
     *  Get the question description
     *  @return String Question Description
     */
    public String getQuestion() {
        return question;
    }
    /**
     *  Set the Question description
     *  @param String question description
     */
    public void setQuestion(java.lang.String question) {
        this.question = UtilFactory.checkNullStr(question);
    }
    /**
     *  Get the answer for the question
     *  @return String Answer
     */
    public String getAnswer() {
        return answer;
    }
    /**
     *  Set the Answer for the question
     *  @param String Answer
     */
    public void setAnswer(java.lang.String answer) {
        this.answer = UtilFactory.checkNullStr(answer);
    }
    /**
     *  Get the explanation
     *  @return String Explanation
     */
    public String getExplanation() {
        return explanation;
    }
    /**
     *  Set the explanation
     *  @param String Explanation
     */
    public void setExplanation(java.lang.String explanation) {
        this.explanation = UtilFactory.checkNullStr(explanation);
    }
    /**
     *  Get the type of the question
     *  @return String Question type
     */
    public String getQuestionType() {
        return questionType;
    }
    /**
     *  Set the type of the question
     *  @param String Question type
     */
    public void setQuestionType(java.lang.String questionType) {
        this.questionType = UtilFactory.checkNullStr(questionType);
    }
    /**
     *  Get the number of answers for a particular question
     *  @return String Number Of Answers
     */
    public String getNumOfAns() {
        return numOfAns;
    }
    /**
     *  Set number of answers for a particular question
     *  @param String number of answers
     */
    public void setNumOfAns(java.lang.String numOfAns) {
        this.numOfAns = UtilFactory.checkNullStr(numOfAns);
    }
    /**
     *  Get the flag for Explanation required for
     *  @return String Explanation required flag
     */
    public String getExplReqFor() {
        return explReqFor;
    }
    /**
     *  Set the flag for explanation required for
     *  @param String Explanation required for
     */
    public void setExplReqFor(java.lang.String explReqFor) {
        this.explReqFor = UtilFactory.checkNullStr(explReqFor);
    }
    /**
     *  Get the flag for date required for
     *  @return String date required for
     */
    public String getDateReqFor() {
        return dateReqFor;
    }
    /**
     *  Set the flag for date required for
     *  @param String date required for
     */
    public void setDateReqFor(java.lang.String dateReqFor) {
        this.dateReqFor = UtilFactory.checkNullStr(dateReqFor);
    }
    /**
     *  Get the status
     *  @return String status
     */
    public String getStatus() {
        return status;
    }
    /**
     *  Set the status
     *  @param String status
     */
    public void setStatus(java.lang.String status) {
        this.status = UtilFactory.checkNullStr(status);
    }
    /**
     *  Get the effective date
     *  @return Timestamp Effective Date
     */
    public Timestamp getEffDate() {
        return effDate;
    }
    /**
     *  Set the effective date
     *  @param Timestamp Effective date
     */
    public void setEffDate(Timestamp effDate) {
        this.effDate = effDate;
    }
    /**
     *  Get the Last updated date
     *  @return Timestamp Last Updated Date
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    /**
     *  Set the Last updated date
     *  @param Timestamp Last Updated Date
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     *  Get the review date
     *  @return Timestamp Review Date
     */
    public Timestamp getReviewDate() {
        return reviewDate;
    }
    /**
     *  Set the review date
     *  @param Timestamp Review Date
     */
    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }
    /**
     *  Get the update user name
     *  @return String Update User
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     *  Set the update user name
     *  @param String Update User
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = UtilFactory.checkNullStr(updateUser);
    }
    
    /**
     * get correspEntQuestId
     * @return correspEntQuestId
     */
    public String getCorrespEntQuestId(){
        return correspEntQuestId;
    }
    
    /**
     * set correspEntQuestId
     * @param correspEntQuestId
     */
    public void setCorrespEntQuestId(String correspEntQuestId){
        this.correspEntQuestId = correspEntQuestId;
    }
    
    public String getCorrespEntQuestLabel(){
        return correspEntQuestLabel;
    }
    
    public void setCorrespEntQuestLabel(String correspEntQuestLabel){
        this.correspEntQuestLabel = correspEntQuestLabel;
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setLabel(String label){
        this.label = label;
    }

}