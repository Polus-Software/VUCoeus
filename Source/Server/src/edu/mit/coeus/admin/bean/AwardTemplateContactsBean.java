/*
 * AwardTemplateContactsBean.java
 *
 * Created on January 3, 2005, 11:27 AM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.admin.bean.TemplateBaseBean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class AwardTemplateContactsBean extends TemplateBaseBean {
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
	private int contactTypeCode;
    private int rolodexId;
    private int aw_ContactTypeCode;
    private int aw_RolodexId;
    private String contactTypeDescription;
    private int rowId;
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private String updateUserName;
    //COEUSQA-1456 : End
	/** Creates a new instance of AwardTemplateContactsBean */
	public AwardTemplateContactsBean() {
	}
	
	
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardTemplateContactsBean awardTemplateContactsBean= (AwardTemplateContactsBean)obj;
            if(awardTemplateContactsBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }  
	
	/** Getter for property lastName.
	 * @return Value of property lastName.
	 *
	 */
	public java.lang.String getLastName() {
		return lastName;
	}
	
	/** Setter for property lastName.
	 * @param lastName New value of property lastName.
	 *
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}
	
	/** Getter for property firstName.
	 * @return Value of property firstName.
	 *
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}
	
	/** Setter for property firstName.
	 * @param firstName New value of property firstName.
	 *
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}
	
	/** Getter for property middleName.
	 * @return Value of property middleName.
	 *
	 */
	public java.lang.String getMiddleName() {
		return middleName;
	}
	
	/** Setter for property middleName.
	 * @param middleName New value of property middleName.
	 *
	 */
	public void setMiddleName(java.lang.String middleName) {
		this.middleName = middleName;
	}
	
	/** Getter for property prefix.
	 * @return Value of property prefix.
	 *
	 */
	public java.lang.String getPrefix() {
		return prefix;
	}
	
	/** Setter for property prefix.
	 * @param prefix New value of property prefix.
	 *
	 */
	public void setPrefix(java.lang.String prefix) {
		this.prefix = prefix;
	}
	
	/** Getter for property suffix.
	 * @return Value of property suffix.
	 *
	 */
	public java.lang.String getSuffix() {
		return suffix;
	}
	
	/** Setter for property suffix.
	 * @param suffix New value of property suffix.
	 *
	 */
	public void setSuffix(java.lang.String suffix) {
		this.suffix = suffix;
	}
	
	/** Getter for property title.
	 * @return Value of property title.
	 *
	 */
	public java.lang.String getTitle() {
		return title;
	}
	
	/** Setter for property title.
	 * @param title New value of property title.
	 *
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}
	
	/** Getter for property sponsorCode.
	 * @return Value of property sponsorCode.
	 *
	 */
	public java.lang.String getSponsorCode() {
		return sponsorCode;
	}
	
	/** Setter for property sponsorCode.
	 * @param sponsorCode New value of property sponsorCode.
	 *
	 */
	public void setSponsorCode(java.lang.String sponsorCode) {
		this.sponsorCode = sponsorCode;
	}
	
	/** Getter for property sponsorName.
	 * @return Value of property sponsorName.
	 *
	 */
	public java.lang.String getSponsorName() {
		return sponsorName;
	}
	
	/** Setter for property sponsorName.
	 * @param sponsorName New value of property sponsorName.
	 *
	 */
	public void setSponsorName(java.lang.String sponsorName) {
		this.sponsorName = sponsorName;
	}
	
	/** Getter for property organization.
	 * @return Value of property organization.
	 *
	 */
	public java.lang.String getOrganization() {
		return organization;
	}
	
	/** Setter for property organization.
	 * @param organization New value of property organization.
	 *
	 */
	public void setOrganization(java.lang.String organization) {
		this.organization = organization;
	}
	
	/** Getter for property address1.
	 * @return Value of property address1.
	 *
	 */
	public java.lang.String getAddress1() {
		return address1;
	}
	
	/** Setter for property address1.
	 * @param address1 New value of property address1.
	 *
	 */
	public void setAddress1(java.lang.String address1) {
		this.address1 = address1;
	}
	
	/** Getter for property address2.
	 * @return Value of property address2.
	 *
	 */
	public java.lang.String getAddress2() {
		return address2;
	}
	
	/** Setter for property address2.
	 * @param address2 New value of property address2.
	 *
	 */
	public void setAddress2(java.lang.String address2) {
		this.address2 = address2;
	}
	
	/** Getter for property address3.
	 * @return Value of property address3.
	 *
	 */
	public java.lang.String getAddress3() {
		return address3;
	}
	
	/** Setter for property address3.
	 * @param address3 New value of property address3.
	 *
	 */
	public void setAddress3(java.lang.String address3) {
		this.address3 = address3;
	}
	
	/** Getter for property county.
	 * @return Value of property county.
	 *
	 */
	public java.lang.String getCounty() {
		return county;
	}
	
	/** Setter for property county.
	 * @param county New value of property county.
	 *
	 */
	public void setCounty(java.lang.String county) {
		this.county = county;
	}
	
	/** Getter for property city.
	 * @return Value of property city.
	 *
	 */
	public java.lang.String getCity() {
		return city;
	}
	
	/** Setter for property city.
	 * @param city New value of property city.
	 *
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	/** Getter for property state.
	 * @return Value of property state.
	 *
	 */
	public java.lang.String getState() {
		return state;
	}
	
	/** Setter for property state.
	 * @param state New value of property state.
	 *
	 */
	public void setState(java.lang.String state) {
		this.state = state;
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
	
	/** Getter for property postalCode.
	 * @return Value of property postalCode.
	 *
	 */
	public java.lang.String getPostalCode() {
		return postalCode;
	}
	
	/** Setter for property postalCode.
	 * @param postalCode New value of property postalCode.
	 *
	 */
	public void setPostalCode(java.lang.String postalCode) {
		this.postalCode = postalCode;
	}
	
	/** Getter for property countryCode.
	 * @return Value of property countryCode.
	 *
	 */
	public java.lang.String getCountryCode() {
		return countryCode;
	}
	
	/** Setter for property countryCode.
	 * @param countryCode New value of property countryCode.
	 *
	 */
	public void setCountryCode(java.lang.String countryCode) {
		this.countryCode = countryCode;
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
	
	/** Getter for property emailAddress.
	 * @return Value of property emailAddress.
	 *
	 */
	public java.lang.String getEmailAddress() {
		return emailAddress;
	}
	
	/** Setter for property emailAddress.
	 * @param emailAddress New value of property emailAddress.
	 *
	 */
	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	/** Getter for property phoneNumber.
	 * @return Value of property phoneNumber.
	 *
	 */
	public java.lang.String getPhoneNumber() {
		return phoneNumber;
	}
	
	/** Setter for property phoneNumber.
	 * @param phoneNumber New value of property phoneNumber.
	 *
	 */
	public void setPhoneNumber(java.lang.String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/** Getter for property faxNumber.
	 * @return Value of property faxNumber.
	 *
	 */
	public java.lang.String getFaxNumber() {
		return faxNumber;
	}
	
	/** Setter for property faxNumber.
	 * @param faxNumber New value of property faxNumber.
	 *
	 */
	public void setFaxNumber(java.lang.String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	/** Getter for property comments.
	 * @return Value of property comments.
	 *
	 */
	public java.lang.String getComments() {
		return comments;
	}
	
	/** Setter for property comments.
	 * @param comments New value of property comments.
	 *
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}
	
	/** Getter for property contactTypeCode.
	 * @return Value of property contactTypeCode.
	 *
	 */
	public int getContactTypeCode() {
		return contactTypeCode;
	}
	
	/** Setter for property contactTypeCode.
	 * @param contactTypeCode New value of property contactTypeCode.
	 *
	 */
	public void setContactTypeCode(int contactTypeCode) {
		this.contactTypeCode = contactTypeCode;
	}
	
	/** Getter for property rolodexId.
	 * @return Value of property rolodexId.
	 *
	 */
	public int getRolodexId() {
		return rolodexId;
	}
	
	/** Setter for property rolodexId.
	 * @param rolodexId New value of property rolodexId.
	 *
	 */
	public void setRolodexId(int rolodexId) {
		this.rolodexId = rolodexId;
	}
	
	/** Getter for property aw_ContactTypeCode.
	 * @return Value of property aw_ContactTypeCode.
	 *
	 */
	public int getAw_ContactTypeCode() {
		return aw_ContactTypeCode;
	}
	
	/** Setter for property aw_ContactTypeCode.
	 * @param aw_ContactTypeCode New value of property aw_ContactTypeCode.
	 *
	 */
	public void setAw_ContactTypeCode(int aw_ContactTypeCode) {
		this.aw_ContactTypeCode = aw_ContactTypeCode;
	}
	
	/** Getter for property aw_RolodexId.
	 * @return Value of property aw_RolodexId.
	 *
	 */
	public int getAw_RolodexId() {
		return aw_RolodexId;
	}
	
	/** Setter for property aw_RolodexId.
	 * @param aw_RolodexId New value of property aw_RolodexId.
	 *
	 */
	public void setAw_RolodexId(int aw_RolodexId) {
		this.aw_RolodexId = aw_RolodexId;
	}
	
	/** Getter for property contactTypeDescription.
	 * @return Value of property contactTypeDescription.
	 *
	 */
	public java.lang.String getContactTypeDescription() {
		return contactTypeDescription;
	}
	
	/** Setter for property contactTypeDescription.
	 * @param contactTypeDescription New value of property contactTypeDescription.
	 *
	 */
	public void setContactTypeDescription(java.lang.String contactTypeDescription) {
		this.contactTypeDescription = contactTypeDescription;
	}
	
	/** Getter for property rowId.
	 * @return Value of property rowId.
	 *
	 */
	public int getRowId() {
		return rowId;
	}
	
	/** Setter for property rowId.
	 * @param rowId New value of property rowId.
	 *
	 */
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}
	
	public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
		return true;
	}
        
        //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
        /**
         * Getter for property updateUser.
         * @return updateUser.
         */
        public java.lang.String getUpdateUserName() {
            return updateUserName;
        }
        
        /**
         * Setter for property updateUser.
         * @param updateUser
         */
        public void setUpdateUserName(String updateUserName) {
            this.updateUserName = updateUserName;
        }
        //COEUSQA-1456 : End
	
}
