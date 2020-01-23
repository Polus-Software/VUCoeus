/*
 * @(#)AwardDetailsBean.java 1.0 3/30/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.coi.bean;
import edu.mit.coeus.utils.UtilFactory;

/**
 * This class used to hold the information pertaining to an award.
 *
 * @version 1.0 March 30, 2002, 1:12 PM
 * @author  Anil Nandakumar
 */

public class AwardDetailsBean extends java.lang.Object {

    private String sponsorAwardNumber;
    private String mitAwardNumber;
    private String unitNumber;
    private String unitName;
    private String accountNumber;
    private String statusCode;
    private String statusDesc;
    private String title;
    private String sponsorCode;
    private String sponsorName;
    private String investigatorName;
    private String fullName;


    /** Creates new AwardDetailsBean */
    public AwardDetailsBean() {
    }

    /**
     *  This method gets the Sponsor Award Number
     *  @return String sponsorAwardNumber
     */
    public String getSponsorAwardNumber() {
        return sponsorAwardNumber;
    }

    /**
     *  This method sets the Sponsor Award Number
     *  @param String sponsorAwardNumber
     */
    public void setSponsorAwardNumber(java.lang.String sponsorAwardNumber) {
        this.sponsorAwardNumber = UtilFactory.checkNullStr(sponsorAwardNumber);
    }

    /**
     *  This method gets the MIT Award Number
     *  @return String mitAwardNumber
     */
    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    /**
     *  This method sets the MIT Award Number
     *  @param String mitAwardNumber
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = UtilFactory.checkNullStr(mitAwardNumber);
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
     *  This method gets the Status Desc
     *  @return String statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }

    /**
     *  This method sets the Status Desc
     *  @param String statusDesc
     */
    public void setStatusDesc(java.lang.String statusDesc) {
        this.statusDesc = UtilFactory.checkNullStr(statusDesc);
    }

    /**
     *  This method gets the Title
     *  @return String title
     */
    public String getTitle() {
        return title;
    }

    /**
     *  This method sets the title
     *  @param String title
     */
    public void setTitle(java.lang.String title) {
        this.title = UtilFactory.checkNullStr(title);
    }

    /**
     *  This method gets the sponsor Code
     *  @return String sponsorCode
     */
    public String getSponsorCode() {
        return sponsorCode;
    }

    /**
     *  This method sets the sponsor Code
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
     *  This method gets the Full Name
     *  @return String fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *  This method sets the Full Name
     *  @param String fullName
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = UtilFactory.checkNullStr(fullName);
    }

}
