/*
 * @(#)PersonInfoBean.java 1.0 14/06/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.bean;

import java.sql.Date;

/**
 * This class contain the Person Information like personID, Name, Unit Name,
 * phone number, location, designation, department and so forth. It provides the
 * acessor methods to get/set the person attributes/parameters.
 *
 * @version 1.0 April 14, 2002, 4:27 PM
 * @author Geo Thomas
 */
public class PersonInfoBean {
	// holds person id
	private String personId;
	// holds user id
	private String userId;
	// holds full name
	private String personName;
	// Malini:added status
	private String status;
	private String fullName;
	private String affiliationTypeCode;
	// holds last name
	private String lastName;
	// holds first name
	private String firstName;
	// holds unit name
	private String unitName;
	// holds prior name
	private String priorName;
	// holds email
	private String email;
	// holds office location
	private String offLocation;
	// holds office phone
	private String offPhone;
	// holds secondary office location
	private String secOffLoc;
	// holds secondary office phone
	private String secOffPhone;
	// holds user name
	private String userName;
	// holds directory title
	private String dirTitle;
	// holds directory department
	private String dirDept;

	private String facFlag;
	// holds home unit
	private String homeUnit;
	// holds the status of details. whether the details attached with this bean
	// is of logged in user or not
	private boolean ownInfoFlag = false;
	// private boolean ownInfoFlag = true;
	/*
	 * Holds the status of whether the person has any pending financial
	 * intereset disclosures for the annual disclosure.
	 */
	private boolean pendingAnnDisclFlag = false;
	// Added for Case#2918 - Use of Salary Anniversary Date for calculating
	// inflation in budget development module
	private Date salaryAnniversaryDate;
	// Case #3338 - Add new elements the person and rolodex details to
	// Subcontract schema -Start
	// holds middle name
	private String middleName;
	private String fax;
	private String city;
	private String state;
	private String postalCode;
	private String address1;
	private String address2;
	private String address3;

	// holds school or college
	private String school;

	// Case #3338 - Add new elements the person and rolodex details to
	// Subcontract schema -End
	//
	/** Creates new PersonInfoBean */
	public PersonInfoBean() {
	}

	/**
	 * Getter for property address1.
	 * 
	 * @return Value of property address1.
	 */
	public java.lang.String getAddress1() {
		return address1;
	}

	/**
	 * Getter for property address2.
	 * 
	 * @return Value of property address2.
	 */
	public java.lang.String getAddress2() {
		return address2;
	}

	/**
	 * Getter for property address3.
	 * 
	 * @return Value of property address3.
	 */
	public java.lang.String getAddress3() {
		return address3;
	}

	public String getAffiliationTypeCode() {
		return affiliationTypeCode;
	}

	/**
	 * Getter for property city.
	 * 
	 * @return Value of property city.
	 */
	public java.lang.String getCity() {
		return city;
	}

	/**
	 * Get Directory Department
	 * 
	 * @return String Directory Department
	 */
	public String getDirDept() {
		return dirDept;
	}

	/**
	 * Get Directory Title
	 * 
	 * @return String Directory Title
	 */
	public String getDirTitle() {
		return dirTitle;
	}

	/**
	 * Get Email
	 * 
	 * @return String Email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Get Faculty Flag
	 * 
	 * @return String Faculty flag
	 */
	public String getFacFlag() {
		return facFlag;
	}

	/**
	 * Getter for property fax.
	 * 
	 * @return Value of property fax.
	 */
	public java.lang.String getFax() {
		return fax;
	}

	/**
	 * Get First Name
	 * 
	 * @return String First Name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Get Full Name
	 * 
	 * @return String Full Name
	 */
	public String getFullName() {
		return personName;
	}

	/**
	 * Get Home Unit
	 * 
	 * @return String Home Unit
	 */
	public String getHomeUnit() {
		return homeUnit;
	}

	/**
	 * Get Last Name
	 * 
	 * @return String Last Name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter for property middleName.
	 * 
	 * @return Value of property middleName.
	 */
	public java.lang.String getMiddleName() {
		return middleName;
	}

	/**
	 * Get Office Location
	 * 
	 * @return String Office Location
	 */
	public String getOffLocation() {
		return offLocation;
	}

	/**
	 * Get Office Phone
	 * 
	 * @return String Office Phone
	 */
	public String getOffPhone() {
		return offPhone;
	}

	/**
	 * Get the status if the information that attached with this bean is of the
	 * logged in user.
	 * 
	 * @return boolean owner status
	 */
	public boolean getOwnInfo() {
		return ownInfoFlag;
	}

	/**
	 * Get the status of the pending disclosure for a person.
	 * 
	 * @return boolean pending disclosure status
	 */
	public boolean getPendingAnnDisclosure() {
		return pendingAnnDisclFlag;
	}

	/**
	 * Get Person Id
	 * 
	 * @return String Person Id
	 */
	public String getPersonID() {
		return personId;
	}

	/**
	 * Getter for property postalCode.
	 * 
	 * @return Value of property postalCode.
	 */
	public java.lang.String getPostalCode() {
		return postalCode;
	}

	/**
	 * Get Prior Name
	 * 
	 * @return String User Name
	 */
	public String getPriorName() {
		return priorName;
	}

	public Date getSalaryAnniversaryDate() {
		return salaryAnniversaryDate;
	}

	public String getSchool() {
		return school;
	}

	/**
	 * Get Secondary Office Location
	 * 
	 * @return String Secondary Office Location
	 */
	public String getSecOffLoc() {
		return secOffLoc;
	}

	/**
	 * Get Secondary Office Phone
	 * 
	 * @return String Secondary Office Phone
	 */
	public String getSecOffPhone() {
		return secOffPhone;
	}

	/**
	 * Getter for property state.
	 * 
	 * @return Value of property state.
	 */
	public java.lang.String getState() {
		return state;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * Get unit Name
	 * 
	 * @return String unit Name
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * Get User Id
	 * 
	 * @return String User Id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Get User Name
	 * 
	 * @return String User Name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter for property address1.
	 * 
	 * @param address1
	 *            New value of property address1.
	 */
	public void setAddress1(java.lang.String address1) {
		this.address1 = address1;
	}

	/**
	 * Setter for property address2.
	 * 
	 * @param address2
	 *            New value of property address2.
	 */
	public void setAddress2(java.lang.String address2) {
		this.address2 = address2;
	}

	/**
	 * Setter for property address3.
	 * 
	 * @param address3
	 *            New value of property address3.
	 */
	public void setAddress3(java.lang.String address3) {
		this.address3 = address3;
	}

	public void setAffiliationTypeCode(String affiliationTypeCode) {
		this.affiliationTypeCode = affiliationTypeCode;
	}

	/**
	 * Setter for property city.
	 * 
	 * @param city
	 *            New value of property city.
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * Set Directory Department
	 * 
	 * @param dirDept
	 *            String Directory Department
	 */
	public void setDirDept(String dirDept) {
		this.dirDept = dirDept;
	}

	/**
	 * Set Directory Title
	 * 
	 * @param dirTitle
	 *            String Directory Title
	 */
	public void setDirTitle(String dirTitle) {
		this.dirTitle = dirTitle;
	}

	/**
	 * Set Email
	 * 
	 * @param email
	 *            String Email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Set Faculty Flag
	 * 
	 * @param facFlag
	 *            String Faculty Flag
	 */
	public void setFacFlag(String facFlag) {
		this.facFlag = facFlag;
	}

	/**
	 * Setter for property fax.
	 * 
	 * @param fax
	 *            New value of property fax.
	 */
	public void setFax(java.lang.String fax) {
		this.fax = fax;
	}

	/**
	 * Set First Name
	 * 
	 * @param firstName
	 *            String First Name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Set Full Name
	 * 
	 * @param personName
	 *            String represent Full Name
	 */
	public void setFullName(String personName) {
		this.personName = personName;
	}

	/**
	 * Set Home Unit
	 * 
	 * @param homeUnit
	 *            String Home Unit
	 */
	public void setHomeUnit(String homeUnit) {
		this.homeUnit = homeUnit;
	}

	/**
	 * Set First Name
	 * 
	 * @param lastName
	 *            String First Name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Setter for property middleName.
	 * 
	 * @param middleName
	 *            New value of property middleName.
	 */
	public void setMiddleName(java.lang.String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Set Office Location
	 * 
	 * @param offLocation
	 *            String Office Location
	 */
	public void setOffLocation(String offLocation) {
		this.offLocation = offLocation;
	}

	/**
	 * Set Office Phone
	 * 
	 * @param offPhone
	 *            String Office Phone
	 */
	public void setOffPhone(String offPhone) {
		this.offPhone = offPhone;
	}

	/**
	 * Set the status if the information that attached with this bean is of the
	 * logged in user.
	 * 
	 * @param ownInfoFlag
	 *            boolean status
	 */
	public void setOwnInfo(boolean ownInfoFlag) {
		this.ownInfoFlag = ownInfoFlag;
	}

	/**
	 * Set of the pending disclosure for a person.
	 * 
	 * @param pendingAnnDisclFlag
	 *            boolean pending disclosure status
	 */
	public void setPendingAnnDisclosure(boolean pendingAnnDisclFlag) {
		this.pendingAnnDisclFlag = pendingAnnDisclFlag;
	}

	/**
	 * Set Person Id
	 * 
	 * @param personId
	 *            String Person Id
	 */
	public void setPersonID(String personId) {
		this.personId = personId;
	}

	/**
	 * Setter for property postalCode.
	 * 
	 * @param postalCode
	 *            New value of property postalCode.
	 */
	public void setPostalCode(java.lang.String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * Set Prior Name
	 * 
	 * @param priorName
	 *            String Prior Name
	 */
	public void setPriorName(String priorName) {
		this.priorName = priorName;
	}

	public void setSalaryAnniversaryDate(Date salaryAnniversaryDate) {
		this.salaryAnniversaryDate = salaryAnniversaryDate;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * Set Secondary Office Location
	 * 
	 * @param secOffLoc
	 *            String Secondary Office Location
	 */
	public void setSecOffLoc(String secOffLoc) {
		this.secOffLoc = secOffLoc;
	}

	/**
	 * Set Secondary Office Phone
	 * 
	 * @param secOffPhone
	 *            String Secondary Office Phone
	 */
	public void setSecOffPhone(String secOffPhone) {
		this.secOffPhone = secOffPhone;
	}

	/**
	 * Setter for property state.
	 * 
	 * @param state
	 *            New value of property state.
	 */
	public void setState(java.lang.String state) {
		this.state = state;
	}

	public void setStatus(String newStatus) {
		this.status = newStatus;
	}

	/**
	 * Set Unit Name
	 * 
	 * @param unitName
	 *            String Unit Name
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * Set User Id
	 * 
	 * @param userId
	 *            String User Id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Set User Name
	 * 
	 * @param userName
	 *            String User Name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
}