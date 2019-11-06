/*
 * @(#)SponsorSearchBean.java 1.0 4/02/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
import java.sql.Timestamp;
/**
 * This class is for getting and setting various details/values concerning Temporary Proposals.
 *
 * @version 1.0 April 2, 2002, 12:06 PM
 * @author  Anil Nandakumar
 */
public class TempProposalDetailsBean extends java.lang.Object {
    private String proposalNumber;
    private String proposalTypeCode;
    private String title;
    private String piId;
    private String piName;
    private String leadUnit;
    private String sponsorCode;
    private String sponsorName;
    private String logStatus;
    private String comments;
    private String updateUser;

    /* CASE #231 Begin */
    private Timestamp lastUpdated;
    private String lastUpdateUser;
    /* CASE #231 End */
    
    /* CASE #1374 Begin */
    private String proposalType;
    /* CASE #1374 End */

    /** Creates new TempProposalDetailsBean */
    public TempProposalDetailsBean() {
    }
    /**
     *  This method gets the Proposal Number
     *  @return String proposalNumber
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    /**
     *  This method sets the Proposal Number
     *  @param String proposalNumber
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = UtilFactory.checkNullStr(proposalNumber);
    }
    /**
     *  This method gets the Proposal Type Code
     *  @return String proposalTypeCode
     */
    public String getProposalTypeCode() {
        return proposalTypeCode;
    }
    /**
     *  This method sets the Proposal Type Code
     *  @param String proposalTypeCode
     */
    public void setProposalTypeCode(java.lang.String proposalTypeCode) {
        this.proposalTypeCode = UtilFactory.checkNullStr(proposalTypeCode);
    }
    /**
     *  This method gets the Title
     *  @return String title
     */
    public String getTitle() {
        return title;
    }
    /**
     *  This method sets the Title
     *  @param String title
     */
    public void setTitle(java.lang.String title) {
        this.title = UtilFactory.checkNullStr(title);
    }
    /**
     *  This method gets the PiId
     *  @return String piId
     */
    public String getPiId() {
        return piId;
    }
    /**
     *  This method sets the PiId
     *  @param String piId
     */
    public void setPiId(java.lang.String piId) {
        this.piId = UtilFactory.checkNullStr(piId);
    }
    /**
     *  This method gets the Investigator Name
     *  @return String piName
     */
    public String getPiName() {
        return piName;
    }
    /**
     *  This method sets the Investigator Name
     *  @param String piName
     */
    public void setPiName(java.lang.String piName) {
        this.piName = UtilFactory.checkNullStr(piName);
    }
    /**
     *  This method gets the Lead Unit
     *  @return String leadUnit
     */
    public String getLeadUnit() {
        return leadUnit;
    }
    /**
     *  This method sets the Lead Unit
     *  @param String leadUnit
     */
    public void setLeadUnit(java.lang.String leadUnit) {
        this.leadUnit = UtilFactory.checkNullStr(leadUnit);
    }
    /**
     *  This method gets the Sponsor Code
     *  @return String sponsorCode
     */
    public String getSponsorCode() {
        return sponsorCode;
    }
    /**
     *  This method sets the Sponsor Code
     *  @param String sponsorCode
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = UtilFactory.checkNullStr(sponsorCode);
    }
    /**
     *  This method gets the Sponsor Name
     *  @return String sponsorName
     */
    public String getSponsorName() {
        return sponsorName;
    }
    /**
     *  This method sets the Sponsor Name
     *  @param String sponsorName
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = UtilFactory.checkNullStr(sponsorName);
    }
    /**
     *  This method gets the Log Status
     *  @return String logStatus
     */
    public String getLogStatus() {
        return logStatus;
    }
    /**
     *  This method sets the Log Status
     *  @param String logStatus
     */
    public void setLogStatus(java.lang.String logStatus) {
        this.logStatus = UtilFactory.checkNullStr(logStatus);
    }
    /**
     *  This method gets the Comments
     *  @return String comments
     */
    public String getComments() {
        return comments;
    }
    /**
     *  This method sets the Comments
     *  @param String comments
     */
    public void setComments(java.lang.String comments) {
        this.comments = UtilFactory.checkNullStr(comments);
    }
    /**
     *  This method gets the Update user
     *  @return String Update User
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     *  This method sets the Update User
     *  @param String UpdaetUser
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = UtilFactory.checkNullStr(updateUser);
    }

    /* CASE #231 Begin */
    public Timestamp getLastUpdated(){
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated){
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdateUser(){
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser){
        this.lastUpdateUser = lastUpdateUser;
    }

    /* CASE #231 End. */
    
    /* CASE #1374 Begin */
    public String getProposalType(){
        return proposalType;
    }
    
    public void setProposalType(String proposalType){
        this.proposalType = proposalType;
    }
    /* CASE #1374 End */
}