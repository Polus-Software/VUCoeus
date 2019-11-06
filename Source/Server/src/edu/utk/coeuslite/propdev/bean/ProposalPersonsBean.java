/*
 * ProposalPersonsBean.java
 *
 * Created on August 10, 2006, 10:48 AM
 */

package edu.utk.coeuslite.propdev.bean;

/**
 *
 * @author noorula
 */
public class ProposalPersonsBean {

	private String proposalNumber;
	private String personId;
	private String ssn;
	private String lastName;
	private String firstName;
	private String middleName;
	private String fullName;
	private String priorName;
	private String userName;
	private String emailAddress;
	private String dateOfBirth;
	private String age;
	private String ageByFiscalYear;
	private String gender;
	private String race;
	private String educationLevel;
	private String degree;
	private String major;
	private String isHandicapped;
	private String handicapType;
	private String isVeteran;
	private String veteranType;
	private String visaCode;
	private String visaType;
	private String visaRenewalDate;
	private String hasVisa;
	private String officeLocation;
	private String officePhone;
	private String secondaryOfficeLocation;
	private String secondaryOfficePhone;
	private String school;
	private String yearGraduated;
	private String directoryDepartment;
	private String salutation;
	private String countaryOfCitizenship;
	private String primaryTitle;
	private String directoryTitle;
	private String homeUnit;
	private String isFaculty;
	private String isGraduateStudentStaff;
	private String isResearchStaff;
	private String isServiceStaff;
	private String isSupportStaff;
	private String isOtherAccademicGroup;
	private String isMedicalStaff;
	private String vacationAccural;
	private String isOnSabbatical;
	private String idProvided;
	private String idVerified;
	private String updateTimestamp;
	private String updateUser;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String city;
	private String country;
	private String state;
	private String status;
	private String postalCode;
	private String countryCode;
	private String faxNumber;
	private String pagerNumber;
	private String mobilePhoneNumber;
	private String eraCommonsUserName;
	private String sortId;
	private String acType;
	private String awUpdateTimestamp;
	// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
	// details - Start
	private String division;
	// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
	// details - End

	/** Creates a new instance of ProposalPersonsBean */
	public ProposalPersonsBean() {
	}

	/**
	 * Getter for property acType.
	 * 
	 * @return Value of property acType.
	 */
	public java.lang.String getAcType() {
		return acType;
	}

	/**
	 * Getter for property addressLine1.
	 * 
	 * @return Value of property addressLine1.
	 */
	public java.lang.String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * Getter for property addressLine2.
	 * 
	 * @return Value of property addressLine2.
	 */
	public java.lang.String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * Getter for property addressLine3.
	 * 
	 * @return Value of property addressLine3.
	 */
	public java.lang.String getAddressLine3() {
		return addressLine3;
	}

	/**
	 * Getter for property age.
	 * 
	 * @return Value of property age.
	 */
	public java.lang.String getAge() {
		return age;
	}

	/**
	 * Getter for property ageByFiscalYear.
	 * 
	 * @return Value of property ageByFiscalYear.
	 */
	public java.lang.String getAgeByFiscalYear() {
		return ageByFiscalYear;
	}

	/**
	 * Getter for property awUpdateTimestamp.
	 * 
	 * @return Value of property awUpdateTimestamp.
	 */
	public java.lang.String getAwUpdateTimestamp() {
		return awUpdateTimestamp;
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
	 * Getter for property countaryOfCitizenship.
	 * 
	 * @return Value of property countaryOfCitizenship.
	 */
	public java.lang.String getCountaryOfCitizenship() {
		return countaryOfCitizenship;
	}

	/**
	 * Getter for property country.
	 * 
	 * @return Value of property country.
	 */
	public java.lang.String getCountry() {
		return country;
	}

	/**
	 * Getter for property countryCode.
	 * 
	 * @return Value of property countryCode.
	 */
	public java.lang.String getCountryCode() {
		return countryCode;
	}

	/**
	 * Getter for property dateOfBirth.
	 * 
	 * @return Value of property dateOfBirth.
	 */
	public java.lang.String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Getter for property degree.
	 * 
	 * @return Value of property degree.
	 */
	public java.lang.String getDegree() {
		return degree;
	}

	/**
	 * Getter for property directoryDepartment.
	 * 
	 * @return Value of property directoryDepartment.
	 */
	public java.lang.String getDirectoryDepartment() {
		return directoryDepartment;
	}

	/**
	 * Getter for property directoryTitle.
	 * 
	 * @return Value of property directoryTitle.
	 */
	public java.lang.String getDirectoryTitle() {
		return directoryTitle;
	}

	// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
	// details - Start
	/**
	 * Getter for property division.
	 * 
	 * @return Value of property division.
	 */
	public java.lang.String getDivision() {
		return division;
	}

	/**
	 * Getter for property educationLevel.
	 * 
	 * @return Value of property educationLevel.
	 */
	public java.lang.String getEducationLevel() {
		return educationLevel;
	}

	/**
	 * Getter for property emailAddress.
	 * 
	 * @return Value of property emailAddress.
	 */
	public java.lang.String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Getter for property eraCommonsUserName.
	 * 
	 * @return Value of property eraCommonsUserName.
	 */
	public java.lang.String getEraCommonsUserName() {
		return eraCommonsUserName;
	}

	/**
	 * Getter for property faxNumber.
	 * 
	 * @return Value of property faxNumber.
	 */
	public java.lang.String getFaxNumber() {
		return faxNumber;
	}

	/**
	 * Getter for property firstName.
	 * 
	 * @return Value of property firstName.
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}

	/**
	 * Getter for property fullName.
	 * 
	 * @return Value of property fullName.
	 */
	public java.lang.String getFullName() {
		return fullName;
	}

	/**
	 * Getter for property gender.
	 * 
	 * @return Value of property gender.
	 */
	public java.lang.String getGender() {
		return gender;
	}

	/**
	 * Getter for property handicapType.
	 * 
	 * @return Value of property handicapType.
	 */
	public java.lang.String getHandicapType() {
		return handicapType;
	}

	/**
	 * Getter for property hasVisa.
	 * 
	 * @return Value of property hasVisa.
	 */
	public java.lang.String getHasVisa() {
		return hasVisa;
	}

	/**
	 * Getter for property homeUnit.
	 * 
	 * @return Value of property homeUnit.
	 */
	public java.lang.String getHomeUnit() {
		return homeUnit;
	}

	/**
	 * Getter for property idProvided.
	 * 
	 * @return Value of property idProvided.
	 */
	public java.lang.String getIdProvided() {
		return idProvided;
	}

	/**
	 * Getter for property idVerified.
	 * 
	 * @return Value of property idVerified.
	 */
	public java.lang.String getIdVerified() {
		return idVerified;
	}

	/**
	 * Getter for property isFaculty.
	 * 
	 * @return Value of property isFaculty.
	 */
	public java.lang.String getIsFaculty() {
		return isFaculty;
	}

	/**
	 * Getter for property isGraduateStudentStaff.
	 * 
	 * @return Value of property isGraduateStudentStaff.
	 */
	public java.lang.String getIsGraduateStudentStaff() {
		return isGraduateStudentStaff;
	}

	/**
	 * Getter for property isHandicapped.
	 * 
	 * @return Value of property isHandicapped.
	 */
	public java.lang.String getIsHandicapped() {
		return isHandicapped;
	}

	/**
	 * Getter for property isMedicalStaff.
	 * 
	 * @return Value of property isMedicalStaff.
	 */
	public java.lang.String getIsMedicalStaff() {
		return isMedicalStaff;
	}

	/**
	 * Getter for property isOnSabbatical.
	 * 
	 * @return Value of property isOnSabbatical.
	 */
	public java.lang.String getIsOnSabbatical() {
		return isOnSabbatical;
	}

	/**
	 * Getter for property isOtherAccademicGroup.
	 * 
	 * @return Value of property isOtherAccademicGroup.
	 */
	public java.lang.String getIsOtherAccademicGroup() {
		return isOtherAccademicGroup;
	}

	/**
	 * Getter for property isResearchStaff.
	 * 
	 * @return Value of property isResearchStaff.
	 */
	public java.lang.String getIsResearchStaff() {
		return isResearchStaff;
	}

	/**
	 * Getter for property isServiceStaff.
	 * 
	 * @return Value of property isServiceStaff.
	 */
	public java.lang.String getIsServiceStaff() {
		return isServiceStaff;
	}

	/**
	 * Getter for property isSupportStaff.
	 * 
	 * @return Value of property isSupportStaff.
	 */
	public java.lang.String getIsSupportStaff() {
		return isSupportStaff;
	}

	/**
	 * Getter for property isVeteran.
	 * 
	 * @return Value of property isVeteran.
	 */
	public java.lang.String getIsVeteran() {
		return isVeteran;
	}

	/**
	 * Getter for property lastName.
	 * 
	 * @return Value of property lastName.
	 */
	public java.lang.String getLastName() {
		return lastName;
	}

	/**
	 * Getter for property major.
	 * 
	 * @return Value of property major.
	 */
	public java.lang.String getMajor() {
		return major;
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
	 * Getter for property mobilePhoneNumber.
	 * 
	 * @return Value of property mobilePhoneNumber.
	 */
	public java.lang.String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * Getter for property officeLocation.
	 * 
	 * @return Value of property officeLocation.
	 */
	public java.lang.String getOfficeLocation() {
		return officeLocation;
	}

	/**
	 * Getter for property officePhone.
	 * 
	 * @return Value of property officePhone.
	 */
	public java.lang.String getOfficePhone() {
		return officePhone;
	}

	/**
	 * Getter for property pagerNumber.
	 * 
	 * @return Value of property pagerNumber.
	 */
	public java.lang.String getPagerNumber() {
		return pagerNumber;
	}

	/**
	 * Getter for property personId.
	 * 
	 * @return Value of property personId.
	 */
	public java.lang.String getPersonId() {
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
	 * Getter for property primaryTitle.
	 * 
	 * @return Value of property primaryTitle.
	 */
	public java.lang.String getPrimaryTitle() {
		return primaryTitle;
	}

	/**
	 * Getter for property priorName.
	 * 
	 * @return Value of property priorName.
	 */
	public java.lang.String getPriorName() {
		return priorName;
	}

	/**
	 * Getter for property proposalNumber.
	 * 
	 * @return Value of property proposalNumber.
	 */
	public java.lang.String getProposalNumber() {
		return proposalNumber;
	}

	/**
	 * Getter for property race.
	 * 
	 * @return Value of property race.
	 */
	public java.lang.String getRace() {
		return race;
	}

	/**
	 * Getter for property salutation.
	 * 
	 * @return Value of property salutation.
	 */
	public java.lang.String getSalutation() {
		return salutation;
	}

	/**
	 * Getter for property school.
	 * 
	 * @return Value of property school.
	 */
	public java.lang.String getSchool() {
		return school;
	}

	/**
	 * Getter for property secondaryOfficeLocation.
	 * 
	 * @return Value of property secondaryOfficeLocation.
	 */
	public java.lang.String getSecondaryOfficeLocation() {
		return secondaryOfficeLocation;
	}

	/**
	 * Getter for property secondaryOfficePhone.
	 * 
	 * @return Value of property secondaryOfficePhone.
	 */
	public java.lang.String getSecondaryOfficePhone() {
		return secondaryOfficePhone;
	}

	/**
	 * Getter for property sortId.
	 * 
	 * @return Value of property sortId.
	 */
	public java.lang.String getSortId() {
		return sortId;
	}

	/**
	 * Getter for property ssn.
	 * 
	 * @return Value of property ssn.
	 */
	public java.lang.String getSsn() {
		return ssn;
	}

	/**
	 * Getter for property state.
	 * 
	 * @return Value of property state.
	 */
	public java.lang.String getState() {
		return state;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Getter for property updateTimestamp.
	 * 
	 * @return Value of property updateTimestamp.
	 */
	public java.lang.String getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * Getter for property updateUser.
	 * 
	 * @return Value of property updateUser.
	 */
	public java.lang.String getUpdateUser() {
		return updateUser;
	}

	/**
	 * Getter for property userName.
	 * 
	 * @return Value of property userName.
	 */
	public java.lang.String getUserName() {
		return userName;
	}

	/**
	 * Getter for property vacationAccural.
	 * 
	 * @return Value of property vacationAccural.
	 */
	public java.lang.String getVacationAccural() {
		return vacationAccural;
	}

	/**
	 * Getter for property veteranType.
	 * 
	 * @return Value of property veteranType.
	 */
	public java.lang.String getVeteranType() {
		return veteranType;
	}

	/**
	 * Getter for property visaCode.
	 * 
	 * @return Value of property visaCode.
	 */
	public java.lang.String getVisaCode() {
		return visaCode;
	}

	/**
	 * Getter for property visaRenewalDate.
	 * 
	 * @return Value of property visaRenewalDate.
	 */
	public java.lang.String getVisaRenewalDate() {
		return visaRenewalDate;
	}

	/**
	 * Getter for property visaType.
	 * 
	 * @return Value of property visaType.
	 */
	public java.lang.String getVisaType() {
		return visaType;
	}

	/**
	 * Getter for property yearGraduated.
	 * 
	 * @return Value of property yearGraduated.
	 */
	public java.lang.String getYearGraduated() {
		return yearGraduated;
	}

	/**
	 * Setter for property acType.
	 * 
	 * @param acType
	 *            New value of property acType.
	 */
	public void setAcType(java.lang.String acType) {
		this.acType = acType;
	}

	/**
	 * Setter for property addressLine1.
	 * 
	 * @param addressLine1
	 *            New value of property addressLine1.
	 */
	public void setAddressLine1(java.lang.String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * Setter for property addressLine2.
	 * 
	 * @param addressLine2
	 *            New value of property addressLine2.
	 */
	public void setAddressLine2(java.lang.String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * Setter for property addressLine3.
	 * 
	 * @param addressLine3
	 *            New value of property addressLine3.
	 */
	public void setAddressLine3(java.lang.String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	/**
	 * Setter for property age.
	 * 
	 * @param age
	 *            New value of property age.
	 */
	public void setAge(java.lang.String age) {
		this.age = age;
	}

	/**
	 * Setter for property ageByFiscalYear.
	 * 
	 * @param ageByFiscalYear
	 *            New value of property ageByFiscalYear.
	 */
	public void setAgeByFiscalYear(java.lang.String ageByFiscalYear) {
		this.ageByFiscalYear = ageByFiscalYear;
	}

	/**
	 * Setter for property awUpdateTimestamp.
	 * 
	 * @param awUpdateTimestamp
	 *            New value of property awUpdateTimestamp.
	 */
	public void setAwUpdateTimestamp(java.lang.String awUpdateTimestamp) {
		this.awUpdateTimestamp = awUpdateTimestamp;
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
	 * Setter for property countaryOfCitizenship.
	 * 
	 * @param countaryOfCitizenship
	 *            New value of property countaryOfCitizenship.
	 */
	public void setCountaryOfCitizenship(java.lang.String countaryOfCitizenship) {
		this.countaryOfCitizenship = countaryOfCitizenship;
	}

	/**
	 * Setter for property country.
	 * 
	 * @param country
	 *            New value of property country.
	 */
	public void setCountry(java.lang.String country) {
		this.country = country;
	}

	/**
	 * Setter for property countryCode.
	 * 
	 * @param countryCode
	 *            New value of property countryCode.
	 */
	public void setCountryCode(java.lang.String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Setter for property dateOfBirth.
	 * 
	 * @param dateOfBirth
	 *            New value of property dateOfBirth.
	 */
	public void setDateOfBirth(java.lang.String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Setter for property degree.
	 * 
	 * @param degree
	 *            New value of property degree.
	 */
	public void setDegree(java.lang.String degree) {
		this.degree = degree;
	}

	/**
	 * Setter for property directoryDepartment.
	 * 
	 * @param directoryDepartment
	 *            New value of property directoryDepartment.
	 */
	public void setDirectoryDepartment(java.lang.String directoryDepartment) {
		this.directoryDepartment = directoryDepartment;
	}

	/**
	 * Setter for property directoryTitle.
	 * 
	 * @param directoryTitle
	 *            New value of property directoryTitle.
	 */
	public void setDirectoryTitle(java.lang.String directoryTitle) {
		this.directoryTitle = directoryTitle;
	}

	/**
	 * Setter for property division.
	 * 
	 * @param division
	 *            New value of property division.
	 */
	public void setDivision(java.lang.String division) {
		this.division = division;
	}
	// COEUSQA-1674 - Allow Division Lead Unit to be modified in the person
	// details - End

	/**
	 * Setter for property educationLevel.
	 * 
	 * @param educationLevel
	 *            New value of property educationLevel.
	 */
	public void setEducationLevel(java.lang.String educationLevel) {
		this.educationLevel = educationLevel;
	}

	/**
	 * Setter for property emailAddress.
	 * 
	 * @param emailAddress
	 *            New value of property emailAddress.
	 */
	public void setEmailAddress(java.lang.String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Setter for property eraCommonsUserName.
	 * 
	 * @param eraCommonsUserName
	 *            New value of property eraCommonsUserName.
	 */
	public void setEraCommonsUserName(java.lang.String eraCommonsUserName) {
		this.eraCommonsUserName = eraCommonsUserName;
	}

	/**
	 * Setter for property faxNumber.
	 * 
	 * @param faxNumber
	 *            New value of property faxNumber.
	 */
	public void setFaxNumber(java.lang.String faxNumber) {
		this.faxNumber = faxNumber;
	}

	/**
	 * Setter for property firstName.
	 * 
	 * @param firstName
	 *            New value of property firstName.
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Setter for property fullName.
	 * 
	 * @param fullName
	 *            New value of property fullName.
	 */
	public void setFullName(java.lang.String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Setter for property gender.
	 * 
	 * @param gender
	 *            New value of property gender.
	 */
	public void setGender(java.lang.String gender) {
		this.gender = gender;
	}

	/**
	 * Setter for property handicapType.
	 * 
	 * @param handicapType
	 *            New value of property handicapType.
	 */
	public void setHandicapType(java.lang.String handicapType) {
		this.handicapType = handicapType;
	}

	/**
	 * Setter for property hasVisa.
	 * 
	 * @param hasVisa
	 *            New value of property hasVisa.
	 */
	public void setHasVisa(java.lang.String hasVisa) {
		this.hasVisa = hasVisa;
	}

	/**
	 * Setter for property homeUnit.
	 * 
	 * @param homeUnit
	 *            New value of property homeUnit.
	 */
	public void setHomeUnit(java.lang.String homeUnit) {
		this.homeUnit = homeUnit;
	}

	/**
	 * Setter for property idProvided.
	 * 
	 * @param idProvided
	 *            New value of property idProvided.
	 */
	public void setIdProvided(java.lang.String idProvided) {
		this.idProvided = idProvided;
	}

	/**
	 * Setter for property idVerified.
	 * 
	 * @param idVerified
	 *            New value of property idVerified.
	 */
	public void setIdVerified(java.lang.String idVerified) {
		this.idVerified = idVerified;
	}

	/**
	 * Setter for property isFaculty.
	 * 
	 * @param isFaculty
	 *            New value of property isFaculty.
	 */
	public void setIsFaculty(java.lang.String isFaculty) {
		this.isFaculty = isFaculty;
	}

	/**
	 * Setter for property isGraduateStudentStaff.
	 * 
	 * @param isGraduateStudentStaff
	 *            New value of property isGraduateStudentStaff.
	 */
	public void setIsGraduateStudentStaff(java.lang.String isGraduateStudentStaff) {
		this.isGraduateStudentStaff = isGraduateStudentStaff;
	}

	/**
	 * Setter for property isHandicapped.
	 * 
	 * @param isHandicapped
	 *            New value of property isHandicapped.
	 */
	public void setIsHandicapped(java.lang.String isHandicapped) {
		this.isHandicapped = isHandicapped;
	}

	/**
	 * Setter for property isMedicalStaff.
	 * 
	 * @param isMedicalStaff
	 *            New value of property isMedicalStaff.
	 */
	public void setIsMedicalStaff(java.lang.String isMedicalStaff) {
		this.isMedicalStaff = isMedicalStaff;
	}

	/**
	 * Setter for property isOnSabbatical.
	 * 
	 * @param isOnSabbatical
	 *            New value of property isOnSabbatical.
	 */
	public void setIsOnSabbatical(java.lang.String isOnSabbatical) {
		this.isOnSabbatical = isOnSabbatical;
	}

	/**
	 * Setter for property isOtherAccademicGroup.
	 * 
	 * @param isOtherAccademicGroup
	 *            New value of property isOtherAccademicGroup.
	 */
	public void setIsOtherAccademicGroup(java.lang.String isOtherAccademicGroup) {
		this.isOtherAccademicGroup = isOtherAccademicGroup;
	}

	/**
	 * Setter for property isResearchStaff.
	 * 
	 * @param isResearchStaff
	 *            New value of property isResearchStaff.
	 */
	public void setIsResearchStaff(java.lang.String isResearchStaff) {
		this.isResearchStaff = isResearchStaff;
	}

	/**
	 * Setter for property isServiceStaff.
	 * 
	 * @param isServiceStaff
	 *            New value of property isServiceStaff.
	 */
	public void setIsServiceStaff(java.lang.String isServiceStaff) {
		this.isServiceStaff = isServiceStaff;
	}

	/**
	 * Setter for property isSupportStaff.
	 * 
	 * @param isSupportStaff
	 *            New value of property isSupportStaff.
	 */
	public void setIsSupportStaff(java.lang.String isSupportStaff) {
		this.isSupportStaff = isSupportStaff;
	}

	/**
	 * Setter for property isVeteran.
	 * 
	 * @param isVeteran
	 *            New value of property isVeteran.
	 */
	public void setIsVeteran(java.lang.String isVeteran) {
		this.isVeteran = isVeteran;
	}

	/**
	 * Setter for property lastName.
	 * 
	 * @param lastName
	 *            New value of property lastName.
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Setter for property major.
	 * 
	 * @param major
	 *            New value of property major.
	 */
	public void setMajor(java.lang.String major) {
		this.major = major;
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
	 * Setter for property mobilePhoneNumber.
	 * 
	 * @param mobilePhoneNumber
	 *            New value of property mobilePhoneNumber.
	 */
	public void setMobilePhoneNumber(java.lang.String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	/**
	 * Setter for property officeLocation.
	 * 
	 * @param officeLocation
	 *            New value of property officeLocation.
	 */
	public void setOfficeLocation(java.lang.String officeLocation) {
		this.officeLocation = officeLocation;
	}

	/**
	 * Setter for property officePhone.
	 * 
	 * @param officePhone
	 *            New value of property officePhone.
	 */
	public void setOfficePhone(java.lang.String officePhone) {
		this.officePhone = officePhone;
	}

	/**
	 * Setter for property pagerNumber.
	 * 
	 * @param pagerNumber
	 *            New value of property pagerNumber.
	 */
	public void setPagerNumber(java.lang.String pagerNumber) {
		this.pagerNumber = pagerNumber;
	}

	/**
	 * Setter for property personId.
	 * 
	 * @param personId
	 *            New value of property personId.
	 */
	public void setPersonId(java.lang.String personId) {
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
	 * Setter for property primaryTitle.
	 * 
	 * @param primaryTitle
	 *            New value of property primaryTitle.
	 */
	public void setPrimaryTitle(java.lang.String primaryTitle) {
		this.primaryTitle = primaryTitle;
	}

	/**
	 * Setter for property priorName.
	 * 
	 * @param priorName
	 *            New value of property priorName.
	 */
	public void setPriorName(java.lang.String priorName) {
		this.priorName = priorName;
	}

	/**
	 * Setter for property proposalNumber.
	 * 
	 * @param proposalNumber
	 *            New value of property proposalNumber.
	 */
	public void setProposalNumber(java.lang.String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	/**
	 * Setter for property race.
	 * 
	 * @param race
	 *            New value of property race.
	 */
	public void setRace(java.lang.String race) {
		this.race = race;
	}

	/**
	 * Setter for property salutation.
	 * 
	 * @param salutation
	 *            New value of property salutation.
	 */
	public void setSalutation(java.lang.String salutation) {
		this.salutation = salutation;
	}

	/**
	 * Setter for property school.
	 * 
	 * @param school
	 *            New value of property school.
	 */
	public void setSchool(java.lang.String school) {
		this.school = school;
	}

	/**
	 * Setter for property secondaryOfficeLocation.
	 * 
	 * @param secondaryOfficeLocation
	 *            New value of property secondaryOfficeLocation.
	 */
	public void setSecondaryOfficeLocation(java.lang.String secondaryOfficeLocation) {
		this.secondaryOfficeLocation = secondaryOfficeLocation;
	}

	/**
	 * Setter for property secondaryOfficePhone.
	 * 
	 * @param secondaryOfficePhone
	 *            New value of property secondaryOfficePhone.
	 */
	public void setSecondaryOfficePhone(java.lang.String secondaryOfficePhone) {
		this.secondaryOfficePhone = secondaryOfficePhone;
	}

	/**
	 * Setter for property sortId.
	 * 
	 * @param sortId
	 *            New value of property sortId.
	 */
	public void setSortId(java.lang.String sortId) {
		this.sortId = sortId;
	}

	/**
	 * Setter for property ssn.
	 * 
	 * @param ssn
	 *            New value of property ssn.
	 */
	public void setSsn(java.lang.String ssn) {
		this.ssn = ssn;
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

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Setter for property updateTimestamp.
	 * 
	 * @param updateTimestamp
	 *            New value of property updateTimestamp.
	 */
	public void setUpdateTimestamp(java.lang.String updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	/**
	 * Setter for property updateUser.
	 * 
	 * @param updateUser
	 *            New value of property updateUser.
	 */
	public void setUpdateUser(java.lang.String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * Setter for property userName.
	 * 
	 * @param userName
	 *            New value of property userName.
	 */
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	/**
	 * Setter for property vacationAccural.
	 * 
	 * @param vacationAccural
	 *            New value of property vacationAccural.
	 */
	public void setVacationAccural(java.lang.String vacationAccural) {
		this.vacationAccural = vacationAccural;
	}

	/**
	 * Setter for property veteranType.
	 * 
	 * @param veteranType
	 *            New value of property veteranType.
	 */
	public void setVeteranType(java.lang.String veteranType) {
		this.veteranType = veteranType;
	}

	/**
	 * Setter for property visaCode.
	 * 
	 * @param visaCode
	 *            New value of property visaCode.
	 */
	public void setVisaCode(java.lang.String visaCode) {
		this.visaCode = visaCode;
	}

	/**
	 * Setter for property visaRenewalDate.
	 * 
	 * @param visaRenewalDate
	 *            New value of property visaRenewalDate.
	 */
	public void setVisaRenewalDate(java.lang.String visaRenewalDate) {
		this.visaRenewalDate = visaRenewalDate;
	}

	/**
	 * Setter for property visaType.
	 * 
	 * @param visaType
	 *            New value of property visaType.
	 */
	public void setVisaType(java.lang.String visaType) {
		this.visaType = visaType;
	}

	/**
	 * Setter for property yearGraduated.
	 * 
	 * @param yearGraduated
	 *            New value of property yearGraduated.
	 */
	public void setYearGraduated(java.lang.String yearGraduated) {
		this.yearGraduated = yearGraduated;
	}
}
