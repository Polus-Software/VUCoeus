/*
 * @(#)SponsorDetailsBean.java 1.0 3/30/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is for getting and setting various details/values concerning sponsors.
 *
 * @version 1.0 March 30, 2002, 6:22 PM
 * @author  Anil Nandakumar
 */
public class SponsorDetailsBean extends java.lang.Object {
    private String sponsorCode;
    private String sponsorName;
    private String sponsorTypeCode;
    private String state;
    private String postalCode;
    private String countryCode;
    private String dunBradNumber;
    private String auditReportSent;
    //Visual Compliance
    private String visualComplianceChecked;
    private String visualComplianceExpl;
    //Visual Compliance
    private String ownedByUnit;
    private String acronym;
    private String dunsP4;
    private String dodacNumber;
    private String cageNumber;
    /** Creates new SponsorDetailsBean */
    public SponsorDetailsBean() {
    }
    /**
     *  This method gets the Sponsor Code
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
     *  This method gets the Sponsor Type Code
     *  @return String sponsorTypeCode
     */
    public String getSponsorTypeCode() {
        return sponsorTypeCode;
    }
    /**
     *  This method sets the Sponsor Type Code
     *  @param String sponsorTypeCode
     */
    public void setSponsorTypeCode(java.lang.String sponsorTypeCode) {
        this.sponsorTypeCode = UtilFactory.checkNullStr(sponsorTypeCode);
    }
    /**
     *  This method gets the Acronym
     *  @return String acronym
     */
    public String getAcronym() {
        return acronym;
    }
    /**
     *  This method sets the Acronym
     *  @param String acronym
     */
    public void setAcronym(java.lang.String acronym) {
        this.acronym = UtilFactory.checkNullStr(acronym);
    }
    /**
     *  This method gets the Postal Code
     *  @return String postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }
    /**
     *  This method sets the Postal Code
     *  @param String postalCode
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = UtilFactory.checkNullStr(postalCode);
    }
    /**
     *  This method gets the State
     *  @return String state
     */
    public String getState() {
        return state;
    }
    /**
     *  This method sets the State
     *  @param String state
     */
    public void setState(java.lang.String state) {
        this.state = UtilFactory.checkNullStr(state);
    }
    /**
     *  This method gets the Country Code
     *  @return String countryCode
     */
    public String getCountryCode() {
        return countryCode;
    }
    /**
     *  This method sets the Country Code
     *  @param String countryCode
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = UtilFactory.checkNullStr(countryCode);
    }
    /**
     *  This method gets the Audit Report Sent data
     *  @return String auditReportSent
     */
    public String getAuditReportSent() {
        return auditReportSent;
    }
    /**
     *  This method sets the Audit Report Sent data
     *  @param String auditReportSent
     */
    public void setAuditReportSent(java.lang.String auditReportSent) {
        this.auditReportSent = UtilFactory.checkNullStr(auditReportSent);
    }
    /**
     *  This method gets the Dun Brad Number
     *  @return String dunBradNumber
     */
    public String getDunBradNumber() {
        return dunBradNumber;
    }
    /**
     *  This method sets the Dun Brad Number
     *  @param String dunBradNumber
     */
    public void setDunBradNumber(java.lang.String dunBradNumber) {
        this.dunBradNumber = UtilFactory.checkNullStr(dunBradNumber);
    }
    /**
     *  This method gets the Duns Plus four number
     *  @return String dunsP4
     */
    public String getDunsP4() {
        return dunsP4;
    }
    /**
     *  This method sets the Duns Plus four number
     *  @param String dunsP4
     */
    public void setDunsP4(java.lang.String dunsP4) {
        this.dunsP4 = UtilFactory.checkNullStr(dunsP4);
    }
    /**
     *  This method gets the Dodac Number
     *  @return String dodacNumber
     */
    public String getDodacNumber() {
        return dodacNumber;
    }
    /**
     *  This method sets the Dodac Number
     *  @param String dodacNumber
     */
    public void setDodacNumber(java.lang.String dodacNumber) {
        this.dodacNumber = UtilFactory.checkNullStr(dodacNumber);
    }
    /**
     *  This method gets the Cage Number
     *  @return String cageNumber
     */
    public String getCageNumber() {
        return cageNumber;
    }
    /**
     *  This method sets the Cage Number
     *  @param String cageNumber
     */
    public void setCageNumber(java.lang.String cageNumber) {
        this.cageNumber = UtilFactory.checkNullStr(cageNumber);
    }
    /**
     *  This method gets the Owned By Unit
     *  @return String ownedByUnit
     */
    public String getOwnedByUnit() {
        return ownedByUnit;
    }
    /**
     *  This method sets the Owned By Unit
     *  @param String ownedByUnit
     */
    public void setOwnedByUnit(java.lang.String ownedByUnit) {
        this.ownedByUnit = UtilFactory.checkNullStr(ownedByUnit);
    }
     /**
     *  This method gets the Visual Compliance explanation
     *  @return String ownedByUnit
     */
    public String getVisualComplianceChecked() {
        return visualComplianceChecked;
    }
    /**
     *  This method sets the Visual Compliance explanation
     *  @param String visualComplianceChecked
     */
    public void setVisualComplianceChecked(String visualComplianceChecked) {
        this.visualComplianceChecked = visualComplianceChecked;
    }
    /**
     *  This method gets the Visual Compliance explanation
     *  @return String visualComplianceExpl
     */
    public String getVisualComplianceExpl() {
        return visualComplianceExpl;
    }
    /**
     *  This method sets the Visual Compliance explanation
     *  @param String visualComplianceExpl
     */
    public void setVisualComplianceExpl(String visualComplianceExpl) {
        this.visualComplianceExpl = visualComplianceExpl;
    }
    
    
}