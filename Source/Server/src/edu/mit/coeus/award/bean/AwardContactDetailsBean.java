/*
 * @(#)AwardContactDetailsBean.java 1.0 16/08/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

/**
 * This class is used to hold Award Contact details information.
 *
 * @version 1.0 April 28, 2004
 * @author Prasanna Kumar.
 */

public class AwardContactDetailsBean extends AwardContactBean implements Serializable{

    private String lastName;
    private String firstName;
    private String middleName;
    private String prefix;
    private String suffix;
    private String title;
    private String sponsorCode;
    private String sponsorName;
    private String organization;
    private String address1;
    private String address2;
    private String address3;
    private String county;
    private String city;
    private String state;
    private String stateName;
    private String postalCode;
    private String countryCode;
    private String countryName;
    private String emailAddress;
    private String phoneNumber;
    private String faxNumber;
    private String comments;

    /** Creates new AwardContactDetailsBean */
    public AwardContactDetailsBean() {

    }
    
    /** Creates new AwardContactDetailsBean */
    public AwardContactDetailsBean(TemplateContactBean templateContactBean) {
        this.setContactTypeCode(templateContactBean.getContactTypeCode());
        this.setContactTypeDescription(templateContactBean.getContactTypeDescription());
        this.setRolodexId(templateContactBean.getRolodexId());
        this.setAw_ContactTypeCode(templateContactBean.getAw_ContactTypeCode());
        this.setAw_RolodexId(templateContactBean.getAw_RolodexId());        
        this.setLastName(templateContactBean.getLastName());
        this.setFirstName(templateContactBean.getFirstName());
        this.setMiddleName(templateContactBean.getMiddleName());
        this.setSuffix(templateContactBean.getSuffix());
        this.setPrefix(templateContactBean.getPrefix());
        this.setTitle(templateContactBean.getTitle());
        this.setSponsorCode(templateContactBean.getSponsorCode());
        this.setSponsorName(templateContactBean.getSponsorName());
        this.setOrganization(templateContactBean.getOrganization());
        this.setAddress1(templateContactBean.getAddress1());
        this.setAddress2(templateContactBean.getAddress2());
        this.setAddress3(templateContactBean.getAddress3());
        this.setCounty(templateContactBean.getCounty());
        this.setCity(templateContactBean.getCity());
        this.setState(templateContactBean.getState());
        this.setPostalCode(templateContactBean.getPostalCode());        
        this.setCountryCode(templateContactBean.getCountryCode());
        this.setEmailAddress(templateContactBean.getEmailAddress());
        this.setPhoneNumber(templateContactBean.getPhoneNumber());
        this.setFaxNumber(templateContactBean.getFaxNumber());
        this.setComments(templateContactBean.getComments());       
    }
    
    /** Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /** Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
    /** Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /** Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /** Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /** Setter for property middleName.
     * @param middleName New value of property middleName.
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }
    
    /** Getter for property prefix.
     * @return Value of property prefix.
     */
    public java.lang.String getPrefix() {
        return prefix;
    }
    
    /** Setter for property prefix.
     * @param prefix New value of property prefix.
     */
    public void setPrefix(java.lang.String prefix) {
        this.prefix = prefix;
    }
    
    /** Getter for property suffix.
     * @return Value of property suffix.
     */
    public java.lang.String getSuffix() {
        return suffix;
    }
    
    /** Setter for property suffix.
     * @param suffix New value of property suffix.
     */
    public void setSuffix(java.lang.String suffix) {
        this.suffix = suffix;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /** Getter for property organization.
     * @return Value of property organization.
     */
    public java.lang.String getOrganization() {
        return organization;
    }
    
    /** Setter for property organization.
     * @param organization New value of property organization.
     */
    public void setOrganization(java.lang.String organization) {
        this.organization = organization;
    }
    
    /** Getter for property address1.
     * @return Value of property address1.
     */
    public java.lang.String getAddress1() {
        return address1;
    }
    
    /** Setter for property address1.
     * @param address1 New value of property address1.
     */
    public void setAddress1(java.lang.String address1) {
        this.address1 = address1;
    }
    
    /** Getter for property address2.
     * @return Value of property address2.
     */
    public java.lang.String getAddress2() {
        return address2;
    }
    
    /** Setter for property address2.
     * @param address2 New value of property address2.
     */
    public void setAddress2(java.lang.String address2) {
        this.address2 = address2;
    }
    
    /** Getter for property address3.
     * @return Value of property address3.
     */
    public java.lang.String getAddress3() {
        return address3;
    }
    
    /** Setter for property address3.
     * @param address3 New value of property address3.
     */
    public void setAddress3(java.lang.String address3) {
        this.address3 = address3;
    }
    
    /** Getter for property county.
     * @return Value of property county.
     */
    public java.lang.String getCounty() {
        return county;
    }
    
    /** Setter for property county.
     * @param county New value of property county.
     */
    public void setCounty(java.lang.String county) {
        this.county = county;
    }
    
    /** Getter for property city.
     * @return Value of property city.
     */
    public java.lang.String getCity() {
        return city;
    }
    
    /** Setter for property city.
     * @param city New value of property city.
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }
    
    /** Getter for property state.
     * @return Value of property state.
     */
    public java.lang.String getState() {
        return state;
    }
    
    /** Setter for property state.
     * @param state New value of property state.
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }
    
    /** Getter for property postalCode.
     * @return Value of property postalCode.
     */
    public java.lang.String getPostalCode() {
        return postalCode;
    }
    
    /** Setter for property postalCode.
     * @param postalCode New value of property postalCode.
     */
    public void setPostalCode(java.lang.String postalCode) {
        this.postalCode = postalCode;
    }
    
    /** Getter for property countryCode.
     * @return Value of property countryCode.
     */
    public java.lang.String getCountryCode() {
        return countryCode;
    }
    
    /** Setter for property countryCode.
     * @param countryCode New value of property countryCode.
     */
    public void setCountryCode(java.lang.String countryCode) {
        this.countryCode = countryCode;
    }
    
    /** Getter for property emailAddress.
     * @return Value of property emailAddress.
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }
    
    /** Setter for property emailAddress.
     * @param emailAddress New value of property emailAddress.
     */
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    /** Getter for property faxNumber.
     * @return Value of property faxNumber.
     */
    public java.lang.String getFaxNumber() {
        return faxNumber;
    }
    
    /** Setter for property faxNumber.
     * @param faxNumber New value of property faxNumber.
     */
    public void setFaxNumber(java.lang.String faxNumber) {
        this.faxNumber = faxNumber;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }  
    
    /** Getter for property phoneNumber.
     * @return Value of property phoneNumber.
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }
    
    /** Setter for property phoneNumber.
     * @param phoneNumber New value of property phoneNumber.
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }    
    
    /** Getter for property countryName.
     * @return Value of property countryName.
     *
     */
    public java.lang.String getCountryName() {
        return countryName;
    }
    
    /** Setter for property countryName.
     * @param countryName New value of property countryName.
     *
     */
    public void setCountryName(java.lang.String countryName) {
        this.countryName = countryName;
    }
    
    /** Getter for property stateName.
     * @return Value of property stateName.
     *
     */
    public java.lang.String getStateName() {
        return stateName;
    }
    
    /** Setter for property stateName.
     * @param stateName New value of property stateName.
     *
     */
    public void setStateName(java.lang.String stateName) {
        this.stateName = stateName;
    }
    
}