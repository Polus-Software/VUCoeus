/*
 * @(#)ProposalDetailsBean.java 1.0 3/30/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is for getting and setting various details/values concerning a proposal.
 *
 * @version 1.0 March 30, 2002, 1:12 PM
 * @author  Anil Nandakumar
 */
public class ProposalDetailsBean extends java.lang.Object {
    private String proposalNumber;
    private String accountNumber;
    private String title;
    private String unitNumber;
    private String unitName;
    private String investigatorName;
    private String sponsorCode;
    private String sponsorName;
    private String statusCode;
    private String proposalType;
    /** Creates new ProposalDetailsBean */
    public ProposalDetailsBean() {
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
     *  This method gets the Account Number
     *  @return String accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }
    /**
     *  This method sets the Account Number
     *  @param String accountNumber
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = UtilFactory.checkNullStr(accountNumber);
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
     *  This method gets the Unit Number
     *  @return String unitNumber
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    /**
     *  This method sets the Unit Number
     *  @param String unitNumber
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = UtilFactory.checkNullStr(unitNumber);
    }
    /**
     *  This method gets the Unit Name
     *  @return String unitName
     */
    public String getUnitName() {
        return unitName;
    }
    /**
     *  This method sets the Unit Name
     *  @param String unitName
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = UtilFactory.checkNullStr(unitName);
    }
    /**
     *  This method gets the Investigator Name
     *  @return String investigatorName
     */
    public String getInvestigatorName() {
        return investigatorName;
    }
    /**
     *  This method sets the Investigator Name
     *  @param String investigatorName
     */
    public void setInvestigatorName(java.lang.String investigatorName) {
        this.investigatorName = UtilFactory.checkNullStr(investigatorName);
    }
    /**
     *  This method gets the Sponsor Code
     *  @return String sponsorCode
     */
    public String getSponsorCode() {
        return sponsorCode;
    }
    /**
     *  This method sets the SponsorCode
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
     *  This method gets the Status Code
     *  @return String statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }
    /**
     *  This method sets the Status Code
     *  @param String statusCode
     */
    public void setStatusCode(java.lang.String statusCode) {
        this.statusCode = UtilFactory.checkNullStr(statusCode);
    }
    /**
     *  This method gets the Proposal Type
     *  @return String proposalType
     */
    public String getProposalType() {
        return proposalType;
    }
    /**
     *  This method sets the Proposal Type
     *  @param String proposalType
     */
    public void setProposalType(java.lang.String proposalType) {
        this.proposalType = UtilFactory.checkNullStr(proposalType);
    }
}
