/*
 * @(#)DisclosureHeaderBean.java 1.0 3/21/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is used to hold the COI disclosure details.
 * It provides get and set method for each attributes of a COI Disclosure.
 *
 * @version 1.0 March 21, 2002, 12:42 AM
 * @author  Anil Nandakumar
 */
public class DisclosureHeaderBean extends java.lang.Object {
    /* CASE #352 Begin */
    private String personId;
    /* CASE #352 End */
    private String name;
    private String disclosureNo;
    private String disclType;

    /**
     * Disclosure Status.
     * Corresponds to OSP$COI_DISCLOSURE_STATUS.DESCRIPTION.
     */
    private String disclStatus;

    /**
     * Disclosure Status Code.
     * Corresponds to OSP$INV_COI_DISC_STATUS_CHANGE.COI_DISCLOSURE_STATUS_CODE.
     */
    private String disclStatCode;
    private String appliesTo;
    private String keyNumber;
    private String title;
    private String account;
    private String status;
    private String statSeqNumber;

    /**
     * Reviewer Code.
     * Corresponds to OSP$INV_COI_DISC_STATUS_CHANGE.COI_REVIEWER_CODE.
     */
    private String reviewerCode;

    /**
     * Reviewer.
     * Corresponds to OSP$COI_REVIEWER.DESCRIPTION.
     */
    private String reviewer;
    private String moduleCode;
    private String moduleDesc;
    private java.sql.Timestamp updatedDate;
    private String updateUser;
    private String sponsor;
    private String disclNo;
    /* CASE #1400 Begin */
    private String sponsorName;
    /* CASE #1400 End */

    //private String title;

    /** Creates new DisclosureHeaderBean */
    public DisclosureHeaderBean() {
    }
    
    /* CASE #352 Begin */
    /**
     * Get personId.
     * @ return personId.
     */
    public String getPersonId(){
        return personId;
    }
    
    /**
     * Set personId.
    */
    public void setPersonId(String personId){
        this.personId = personId;
    }
     
    /* CASE #352 End */
    /**
     *  Get Module Code
     *  @return String Module Code
     */
    public String getModuleCode() {
        return moduleCode;
    }
    /**
     *  Set Module Code
     *  @param String Module Code
     */
    public void setModuleCode(java.lang.String moduleCode) {
        this.moduleCode = UtilFactory.checkNullStr(moduleCode);
    }

    /**
     *  Get title
     *  @return String title
     */
    public String getTitle() {
        return title;
    }
    /**
     *  Set title
     *  @param String title
     */
    public void setTitle(java.lang.String Title) {
        this.title = UtilFactory.checkNullStr(Title);
    }

    /**
     *  Get sponsor - sponsor code + name
     *  @return String sponsor
     */
    public String getSponsor() {
        return sponsor;
    }
    /**
     *  Set sponsor - sponsor code + name
     *  @param String sponsor
     */
    public void setSponsor(java.lang.String Sponsor) {
        this.sponsor = UtilFactory.checkNullStr(Sponsor);
    }

    /**
     *  Get Module descrition
     *  @return String Module description
     */
    public String getModuleDesc() {
        return moduleDesc;
    }
    /**
     *  Set Module description
     *  @param String Module description
     */
    public void setModuleDesc(java.lang.String moduleDesc) {
        this.moduleDesc = UtilFactory.checkNullStr(moduleDesc);
    }
    /**
     *  Get Disclosure name
     *  @return String Disclosure name
     */
    public String getName() {
        return name;
    }
    /**
     *  Set Disclosure name
     *  @param String Disclosure name
     */
    public void setName(java.lang.String name) {
        this.name = UtilFactory.checkNullStr(name);
    }
    /**
     *  Get Disclosure number + disclosure type
     *  @return String Disclosure number
     */
    public String getDisclosureNo() {
        return disclosureNo;
    }
    /**
     *  Set Disclosure number  + disclosure type
     *  @param String Disclosure number
     */
    public void setDisclosureNo(java.lang.String disclosureNo) {
        this.disclosureNo = UtilFactory.checkNullStr(disclosureNo);
    }

    /**
     *  Get Disclosure number
     *  @return String Disclosure number
     */
    public String getDisclNo() {
        return disclNo;
    }
    /**
     *  Set Disclosure number
     *  @param String Disclosure number
     */
    public void setDisclNo(java.lang.String disclNo) {
        this.disclNo = UtilFactory.checkNullStr(disclNo);
    }

    /**
     *  Get the disclosure type
     *  returns 1 if it is award,
     *  returns 2 if it is proposal
     *  @return String AppliesTo Type
     */
    public String getAppliesTo() {
        return appliesTo;
    }
    /**
     *  Set disclosure type
     *  @param String AppliesTo Type
     */
    public void setAppliesTo(java.lang.String appliesTo) {
        this.appliesTo = UtilFactory.checkNullStr(appliesTo);
    }
    /**
     *  Get Key number
     *  returns award number if it is award,
     *  returns proposal number if it is proposal
     *  @return String Key number
     */
    public String getKeyNumber() {
        return keyNumber;
    }
    /**
     *  Set Key Number
     *  @param String Key Number
     */
    public void setKeyNumber(java.lang.String keyNumber) {
        this.keyNumber = UtilFactory.checkNullStr(keyNumber);
    }

    /**
     *  Get Account
     *  @return String Account
     */
    public String getAccount() {
        return account;
    }
    /**
     *  Set Account
     *  @param String Account
     */
    public void setAccount(java.lang.String account) {
        this.account = UtilFactory.checkNullStr(account);
    }
    /**
     *  Get Status
     *  @return String Status
     */
    public String getStatus() {
        return status;
    }
    /**
     *  Set Status
     *  @param String Status
     */
    public void setStatus(java.lang.String status) {
        this.status = UtilFactory.checkNullStr(status);
    }
    /**
     *  Get Status sequence number
     *  @return String Status sequence number
     */
    public String getStatSeqNumber() {
        return statSeqNumber;
    }
    /**
     *  Get Status sequence number
     *  @param String Status sequence number
     */
    public void setStatSeqNumber(java.lang.String statSeqNumber) {
        this.statSeqNumber = UtilFactory.checkNullStr(statSeqNumber);
    }
    /**
     *  Get Disclosure status code
     *  @return String Disclosure status code
     */
    public String getDisclStatCode() {
        return disclStatCode;
    }
    /**
     *  Set Disclosure status code
     *  @param String Disclosure status code
     */
    public void setDisclStatCode(java.lang.String disclStatCode) {
        this.disclStatCode = UtilFactory.checkNullStr(disclStatCode);
    }
    /**
     *  Get Disclosure type
     *  @return String Disclosure type
     */
    public String getDisclType() {
        return disclType;
    }
    /**
     *  Set Disclosure type
     *  @param String Disclosure type
     */
    public void setDisclType(java.lang.String disclType) {
        this.disclType = UtilFactory.checkNullStr(disclType);
    }
    /**
     *  Get Disclosure status description
     *  @return String Disclosure status description
     */
    public String getDisclStatus() {
        return disclStatus;
    }
    /**
     *  Set Disclosure Description
     *  @param String Disclosure description
     */
    public void setDisclStatus(java.lang.String disclStatus) {
        this.disclStatus = UtilFactory.checkNullStr(disclStatus);
    }
    /**
     *  Get Reviewer description
     *  @return String Reviewer description
     */
    public String getReviewer() {
        return reviewer;
    }
    /**
     *  Set Reviewer description
     *  @param String Reviewer description
     */
    public void setReviewer(java.lang.String reviewer) {
        this.reviewer = UtilFactory.checkNullStr(reviewer);
    }
    /**
     *  Get Reviewer code
     *  @return String Reviewer Code
     */
    public String getReviewerCode() {
        return reviewerCode;
    }
    /**
     *  Set Reviewer code
     *  @param String Reviewer Code
     */
    public void setReviewerCode(java.lang.String reviewerCode) {
        this.reviewerCode = UtilFactory.checkNullStr(reviewerCode);
    }
    /**
     *  Get Updated date
     *  @return Timestamp Updated date
     */
    public java.sql.Timestamp getUpdatedDate() {
        return updatedDate;
    }
    /**
     *  Set Updated date
     *  @param Timestamp Updated date
     */
    public void setUpdatedDate(java.sql.Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
    /**
     *  Get Update user
     *  @return String Update user
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     *  Set Update user
     *  @param String Update user
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = UtilFactory.checkNullStr(updateUser);
    }
    /* CASE #1400 Begin */
    /**
     *  Get sponsorName
     *  @return sponsorName
     */
    public String getSponsorName(){
        return sponsorName;
    }   
    
    /**
     *  Set sponsorName
     *  @param String spoonsorName
     */
    public void setSponsorName(String sponsorName){
        this.sponsorName = sponsorName;
    }
    /* CASE #1400 End */

}
