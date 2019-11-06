/*
 * @(#)PersonInfoBean.java 1.0 14/06/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.irb.bean;

import edu.mit.coeus.utils.UtilFactory;
import java.sql.Date;

/**
 * This class is pertaining to a Person which contain the Person Information and
 * accessor methods to get & set data values.
 *
 * @version 1.0 April 14, 2002, 4:27 PM
 * @author  Geo Thomas
 */
public class PersonInfoFormBean implements java.io.Serializable {
    //holds person id
    private String personId;
    //holds full name
    private String personName;
    //holds last name
    private String lastName;
    //holds first name
    private String firstName;
    //case1646 - salutation
    private String saluation;
    //case 1646 - degree
    private String degree;
    //holds unit name
    private String unitName;
    //holds prior name
    private String priorName;
    //holds email
    private String email;
    //holds office location
    private String offLocation;
    //holds office phone
    private String offPhone;
    //holds secondary office location
    private String secOffLoc;
    //holds secondary office phone
    private String secOffPhone;
    //holds user name
    private String userName;
    //holds directory title
    private String dirTitle;
    //holds directory department
    private String dirDept;

    private String facFlag;
    //holds home unit
    private String homeUnit;
    //holds the status of details. whether the details attached with this bean is of logged in user or not
    private boolean ownInfoFlag = false;
    //private boolean ownInfoFlag = true;
    /* Holds the status of whether the person has any pending financial intereset disclosures
     * for the annual disclosure.
     */
    private boolean pendingAnnDisclFlag = false;
  //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - Start
      private String addressLine1;
      private String addressLine2;
      private String addressLine3;
      private String city;
      private String state;
      private String country;
      private String postalCode;
      private String countryCode;
      private String faxNumber;
      private String pagerNumber;
      private String mobilePhoneNumber;          
 //Added for Case 2081 - Investigators Address in templates : Bring forward the address of an individual in correspondences - End   
     //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
      private Date salaryAnniversaryDate;
     //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module - starts
    /** Creates new PersonInfoFormBean */
    public PersonInfoFormBean() {
    }
    
    /**
     *  Get Person Id
     *  @return String Person Id
     */
    public String getPersonID() {
        return personId;
    }
    /**
     *  Set Person Id
     *  @param personId String
     */
    public void setPersonID(String personId) {
        this.personId = personId;
    }
    /**
     *  Get Full Name
     *  @return String Full Name
     */
    public String getFullName() {
        return personName;
    }
    /**
     *  Set Full Name
     *  @param personName String
     */
    public void setFullName(String personName) {
        this.personName = personName;
    }
    /**
     *  Get Last Name
     *  @return String Last Name
     */
    public String getLastName() {
        return lastName;
    }
    /**
     *  Set Last Name
     *  @param lastName String
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     *  Get First Name
     *  @return String First Name
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     *  Set First Name
     *  @param firstName String 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     *  Get unit Name
     *  @return String unit Name
     */
    public String getUnitName() {
        return unitName;
    }
    /**
     *  Set Unit Name
     *  @param unitName String 
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    /**
     *  Get User Name
     *  @return String User Name
     */
    public String getUserName() {
        return userName;
    }
    /**
     *  Set User Name
     *  @param userName String 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     *  Get Prior Name
     *  @return String User Name
     */
    public String getPriorName() {
        return priorName;
    }
    /**
     *  Set Prior Name
     *  @param priorName String 
     */
    public void setPriorName(String priorName) {
        this.priorName = priorName;
    }
    /**
     *  Get Email
     *  @return String Email
     */
    public String getEmail() {
        return email;
    }
    /**
     *  Set Email
     *  @param email String 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     *  Get Office Location
     *  @return String Office Location
     */
    public String getOffLocation() {
        return offLocation;
    }
    /**
     *  Set Office Location
     *  @param offLocation String 
     */
    public void setOffLocation(String offLocation) {
        this.offLocation = offLocation;
    }
    /**
     *  Get Office Phone
     *  @return String Office Phone
     */
    public String getOffPhone() {
        return offPhone;
    }
    /**
     *  Set Office Phone
     *  @param offPhone String 
     */
    public void setOffPhone(String offPhone) {
        this.offPhone = offPhone;
    }
    /**
     *  Get Secondary Office Location
     *  @return String Secondary Office Location
     */
    public String getSecOffLoc() {
        return secOffLoc;
    }
    /**
     *  Set Secondary Office Location
     *  @param secOffLoc String 
     */
    public void setSecOffLoc(String secOffLoc) {
        this.secOffLoc = secOffLoc;
    }
    /**
     *  Get Secondary Office Phone
     *  @return String Secondary Office Phone
     */
    public String getSecOffPhone() {
        return secOffPhone;
    }
    /**
     *  Set Secondary Office Phone
     *  @param secOffPhone String 
     */
    public void setSecOffPhone(String secOffPhone) {
        this.secOffPhone = secOffPhone;
    }
    /**
     *  Get Directory Title
     *  @return String Directory Title
     */
    public String getDirTitle() {
        return dirTitle;
    }
    /**
     *  Set Directory Title
     *  @param dirTitle String 
     */
    public void setDirTitle(String dirTitle) {
        this.dirTitle = dirTitle;
    }
    /**
     *  Get Directory Department
     *  @return String Directory Department
     */
    public String getDirDept() {
        return dirDept;
    }
    /**
     *  Set Directory Department
     *  @param dirDept String 
     */
    public void setDirDept(String dirDept) {
        this.dirDept = dirDept;
    }
    /**
     *  Get Faculty Flag
     *  @return String Faculty flag
     */
    public String getFacFlag() {
        return facFlag;
    }
    /**
     *  Set Faculty Flag
     *  @param facFlag String 
     */
    public void setFacFlag(String facFlag) {
        this.facFlag = facFlag;
    }
    /**
     *  Get Home Unit
     *  @return String Home Unit
     */
    public String getHomeUnit() {
        return homeUnit;
    }
    /**
     *  Set Home Unit
     *  @param homeUnit String
     */
    public void setHomeUnit(String homeUnit) {
        this.homeUnit = homeUnit;
    }
    /**
     *  Get the status if the information that attached with this bean is of the logged in user.
     *  @return boolean owner status
     */
    public boolean getOwnInfo() {
        return ownInfoFlag;
    }

    /**
     *  Set the status  if the information that attached with this bean is of the logged in user.
     *  @param ownInfoFlag boolean status
     */
    public void setOwnInfo(boolean ownInfoFlag) {
        this.ownInfoFlag = ownInfoFlag;
    }
    /**
     *  Get the status of the pending disclosure for a person.
     *  @return boolean pending disclosure status
     */
    public boolean getPendingAnnDisclosure() {
        return pendingAnnDisclFlag;
    }

    /**
     *  Set of the pending disclosure for a person.
     *  @param pendingAnnDisclFlag boolean pending disclosure status
     */
    public void setPendingAnnDisclosure(boolean pendingAnnDisclFlag) {
        this.pendingAnnDisclFlag = pendingAnnDisclFlag;
    }
    
    /**
     * Getter for property saluation.
     * @return Value of property saluation.
     */
    public java.lang.String getSaluation() {
        return saluation;
    }
    
    /**
     * Setter for property saluation.
     * @param saluation New value of property saluation.
     */
    public void setSaluation(java.lang.String saluation) {
        this.saluation = saluation;
    }
    
    /**
     * Getter for property degree.
     * @return Value of property degree.
     */
    public java.lang.String getDegree() {
        return degree;
    }
    
    /**
     * Setter for property degree.
     * @param degree New value of property degree.
     */
    public void setDegree(java.lang.String degree) {
        this.degree = degree;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getPagerNumber() {
        return pagerNumber;
    }

    public void setPagerNumber(String pagerNumber) {
        this.pagerNumber = pagerNumber;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber;
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        this.mobilePhoneNumber = mobilePhoneNumber;
    }

    public Date getSalaryAnniversaryDate() {
        return salaryAnniversaryDate;
    }

    public void setSalaryAnniversaryDate(Date salaryAnniversaryDate) {
        this.salaryAnniversaryDate = salaryAnniversaryDate;
    }
    
}