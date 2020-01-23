/*
 * @(#)RolodexDetailsFormBean.java 1.0 16/08/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */



package edu.mit.coeus.rolodexmaint.bean;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;
/**
 * <code>RolodexDetailsFormBean</code> is a class to hold the RolodexDetails
 * information.
 *
 * @version 1.0 August 16,2002
 * @author Phaneendra Kumar.
 */

public class RolodexDetailsBean implements Serializable{

    private String rolodexId;
    private String lastUpdateUser;
    private Timestamp lastUpdateTime;
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
    private String city;
    private String county;
    private String state;
    private String postalCode;
    private String country = "USA";
    private String phone;
    private String eMail;
    private String fax;
    private String comments;
    private String deleteFlag;
    private String sponsorAddressFlag = "N";
    private String ownedByUnit;
    private String sponsorRolodexId;
    private String acType;
    private Vector states;
    private Vector countries;
    private String createUser;
    private String refId;
    private String countryName;
    private String province;            //case 4249
    //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry - START
    private String status;       
    //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry - END 


    /** Creates new RolodexDetailsFormBean */
    public RolodexDetailsBean() {

    }

    /**
     * gets the RolodexId of the RolodexDetails
     * @return rolodexId of the RoldexInfo
     */

    public String getRolodexId(){
        return rolodexId;
    }

    /**
     * sets the rolodexId to the RolodexInfo
     * @param rolodexId
     */
    public void setRolodexId(String rolodexId){
        this.rolodexId = rolodexId;
    }
    /**
     * gets the LastUpdateUser of the RolodexDetails
     * @return lastUpdateUser of the RoldexInfo
     */

    public String getLastUpdateUser(){
        return lastUpdateUser;
    }

    /**
     * sets the LastUpdateUser to the RolodexInfo
     * @param lastUpdateUser
     */
    public void setLastUpdateUser(String lastUpdateUser){
        this.lastUpdateUser = lastUpdateUser;
    }
    /**
     * gets the LastUpdateTime of the RolodexDetails
     * @return lastUpdateTime of the RoldexInfo
     */

    public Timestamp getLastUpdateTime(){
        return lastUpdateTime;
    }

    /**
     * sets the LastUpdateTime to the RolodexInfo
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(Timestamp lastUpdateTime){
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * gets the LastName of the RolodexDetails
     * @return lastName of the RoldexInfo
     */

    public String getLastName(){
        return lastName;
    }

    /**
     * sets the LastName( to the RolodexInfo
     * @param lastName
     */
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    /**
     * gets the FirstName of the RolodexDetails
     * @return firstName of the RoldexInfo
     */

    public String getFirstName(){
        return firstName;
    }

    /**
     * sets the FirstName to the RolodexInfo
     * @param firstName
     */
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    /**
     * gets the MiddleName of the RolodexDetails
     * @return middleName of the RoldexInfo
     */

    public String getMiddleName(){
        return middleName;
    }

    /**
     * sets the MiddleName to the RolodexInfo
     * @param middleName
     */
    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }

    /**
     * gets the Suffix of the RolodexDetails
     * @return suffix of the RoldexInfo
     */

    public String getSuffix(){
        return suffix;
    }

    /**
     * sets the Suffix to the RolodexInfo
     * @param suffix
     */
    public void setSuffix(String suffix){
        this.suffix = suffix;
    }

    /**
     * gets the Prefix of the RolodexDetails
     * @return prefix of the RoldexInfo
     */

    public String getPrefix(){
        return prefix;
    }

    /**
     * sets the Prefix to the RolodexInfo
     * @param prefix
     */
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }

    /**
     * gets the Title of the RolodexDetails
     * @return title of the RoldexInfo
     */

    public String getTitle(){
        return title;
    }

    /**
     * sets the Title to the RolodexInfo
     * @param title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * gets the SponsorCode of the RolodexDetails
     * @return sponsorCode of the RoldexInfo
     */

    public String getSponsorCode(){
        return sponsorCode;
    }

    /**
     * sets the SponsorCode to the RolodexInfo
     * @param sponsorCode
     */
    public void setSponsorCode(String sponsorCode){
        this.sponsorCode = sponsorCode;
    }

    /**
     * gets the SponsorName of the RolodexDetails
     * @return sponsorName of the RoldexInfo
     */

    public String getSponsorName(){
        return sponsorName;
    }

    /**
     * sets the SponsorName to the RolodexInfo
     * @param sponsorName
     */
    public void setSponsorName(String sponsorName){
        this.sponsorName = sponsorName;
    }

    /**
     * gets the Organization of the RolodexDetails
     * @return organization of the RoldexInfo
     */

    public String getOrganization(){
        return organization;
    }

    /**
     * sets the Organization to the RolodexInfo
     * @param organization
     */
    public void setOrganization(String organization){
        this.organization = organization;
    }

    /**
     * gets the Address1 of the RolodexDetails
     * @return address1 of the RoldexInfo
     */

    public String getAddress1(){
        return address1;
    }

    /**
     * sets the Address1 to the RolodexInfo
     * @param address1
     */
    public void setAddress1(String address1){
        this.address1 = address1;
    }


    /**
     * gets the Address2 of the RolodexDetails
     * @return address2 of the RoldexInfo
     */

    public String getAddress2(){
        return address2;
    }

    /**
     * sets the Address2 to the RolodexInfo
     * @param address2
     */
    public void setAddress2(String address2){
        this.address2 = address2;
    }

    /**
     * gets the Address3 of the RolodexDetails
     * @return address3 of the RoldexInfo
     */

    public String getAddress3(){
        return address3;
    }

    /**
     * sets the Address3 to the RolodexInfo
     * @param address3
     */
    public void setAddress3(String address3){
        this.address3 = address3;
    }

    /**
     * gets the city of the RolodexDetails
     * @return city of the RoldexInfo
     */

    public String getCity(){
        return city;
    }

    /**
     * sets the city to the RolodexInfo
     * @param city
     */
    public void setCity(String city){
        this.city = city;
    }

    /**
     * gets the state of the RolodexDetails
     * @return state of the RoldexInfo
     */

    public String getState(){
        return state;
    }

    /**
     * sets the state to the RolodexInfo
     * @param state
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * gets the county of the RolodexDetails
     * @return county of the RoldexInfo
     */

    public String getCounty(){
        return county;
    }

    /**
     * sets the county to the RolodexInfo
     * @param county
     */
    public void setCounty(String county){
        this.county = county;
    }

    /**
     * gets the postalCode of the RolodexDetails
     * @return postalCode of the RoldexInfo
     */

    public String getPostalCode(){
        return postalCode;
    }

    /**
     * sets the postalCode to the RolodexInfo
     * @param postalCode
     */
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    /**
     * gets the Country of the RolodexDetails
     * @return country of the RoldexInfo
     */

    public String getCountry(){
        return country;
    }

    /**
     * sets the Country to the RolodexInfo
     * @param country
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * gets the Phone of the RolodexDetails
     * @return Phone of the RoldexInfo
     */

    public String getPhone(){
        return phone;
    }

    /**
     * sets the Phone to the RolodexInfo
     * @param Phone
     */
    public void setPhone(String phone){
        this.phone = phone;
    }

    /**
     * gets the Email of the RolodexDetails
     * @return Email of the RoldexInfo
     */

    public String getEMail(){
        return eMail;
    }

    /**
     * sets the EMail to the RolodexInfo
     * @param eMail
     */
    public void setEMail(String eMail){
        this.eMail = eMail;
    }

    /**
     * gets the Fax of the RolodexDetails
     * @return Fax of the RoldexInfo
     */

    public String getFax(){
        return fax;
    }

    /**
     * sets the Fax to the RolodexInfo
     * @param Fax
     */
    public void setFax(String fax){
        this.fax = fax;
    }

    /**
     * gets the comments of the RolodexDetails
     * @return comments  of the RoldexInfo
     */
    
    public String getComments() {
        return comments ;
    }
    
    /**
     * sets the comments to the RolodexInfo
     * @param comments
     */
    public void setComments(String comments){
        this.comments = comments;
    }

    /**
     * gets the deleteFlag of the RolodexDetails
     * @return deleteFlag of the RoldexInfo
     */
    
    public String getDeleteFlag() {
        return deleteFlag ;
    }
    
    /**
     * sets the deleteFlag to the RolodexInfo
     * @param deleteFlag
     */
    public void setDeleteFlag(String deleteFlag){
        this.deleteFlag = deleteFlag;
    }
    
    /**
     * gets the sponsorAddressFlag of the RolodexDetails
     * @return sponsorAddressFlag of the RoldexInfo
     */
    public String getSponsorAddressFlag() {
        return sponsorAddressFlag ;
    }

    /**
     * sets the sponsorAddressFlag to the RolodexInfo
     * @param sponsorAddressFlag
     */
    public void setSponsorAddressFlag(String sponsorAddressFlag){
        this.sponsorAddressFlag = sponsorAddressFlag;
    }
    
    /**
     * gets the ownedByUnit of the RolodexDetails
     * @return ownedByUnit of the RoldexInfo
     */
    public String getOwnedByUnit() {
        return ownedByUnit ;
    }

    /**
     * sets the ownedByUnit info to the RolodexInfo
     * @param ownedByUnit.
     */
    public void setOwnedByUnit(String ownedByUnit){
        this.ownedByUnit = ownedByUnit;
    }
    
    /**
     * gets the sponsorRolodexId of the RolodexDetails
     * @return rolodexId of the RoldexInfo
     */
    public String getSponsorRolodexId() {
        return sponsorRolodexId ;
    }

    /**
     * sets the sponsorRolodexId to the RolodexInfo
     * @param sponsorRolodexId
     */
    public void setSponsorRolodexId(String sponsorRolodexId){
        this.sponsorRolodexId = sponsorRolodexId;
    }
    
    /**
     * gets the AC_Type of the RolodexDetails
     * @return acType of the RoldexInfo
     */
    public String getAcType() {
        return acType ;
    }

    /**
     * sets the AC_Type to the RolodexInfo for database operations
     * @param acType
     */
    public void setAcType(String acType){
        this.acType = acType;
    }

    /**
     * gets the States info of the RolodexDetails
     * @return states 
     */
    public Vector getStates() {
        return states ;
    }

    /**
     * sets the States info to the RolodexInfo
     * @param states
     */
    public void setStates(Vector states){
        this.states = states;
    }

    /**
     * gets the list of Countries for the RolodexDetails
     * @return Countries of the RoldexInfo
     */
    public Vector getCountries() {
        return countries ;
    }

    /**
     * sets the Countries for the Rolodex Info
     * @param countries
     */
    public void setCountries(Vector countries){
        this.countries = countries;
    }
    
    /**
     * gets the Create user of the RolodexDetails
     * @return CreateUser  of the RoldexInfo
     */
    public String getCreateUser() {
        return createUser;
    }
    

    /**
     * sets the Create User to the RolodexInfo
     * @param createUser
     */
    public void setCreateUser(String createUser){
        this.createUser = createUser;
    }
    public void setRefId(String refId){
        this.refId = refId;
    }
    public String getRefId(){
        return this.refId;
    }


    /** Getter for property countryName.
     * @return Value of property countryName.
     */
    public java.lang.String getCountryName() {
        return countryName;
    }
    
    /** Setter for property countryName.
     * @param countryName New value of property countryName.
     */
    public void setCountryName(java.lang.String countryName) {
        this.countryName = countryName;
    }
     public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
   //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
     /**
     * gets the status of the Rolodex
     * @return status of the Roldex
     */
    public String getStatus() {
        return status;
    }
    /**
     * sets the status to the Rolodex for database operations
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }
      //COEUSQA-1528 Add the functionality to set a status on a Rolodex entry START 
}
