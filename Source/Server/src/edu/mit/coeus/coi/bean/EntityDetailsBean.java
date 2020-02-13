/*
 * @(#)EntityDetailsBean.java 1.0 3/19/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
/**
 * This class is used to hold the entity details of a particular user.
 *
 * @version 1.0 March 19, 2002, 3:32 PM
 * @author  Anil Nandakumar
 */
public class EntityDetailsBean extends java.lang.Object {
    private String number;
    private String name;
    private String type;
    private String typeCode;
    private String status;
    private String statusCode;
    private String statusDesc;
    private String shareOwnship;
    private String entityDescription;
    private String personReType;
    private String personReTypeCode;
    private String personReDesc;
    private String orgRelationship;
    private String orgDescription;
    private String sponsor;
    private java.sql.Timestamp lastUpdate;
    private String updateUser;
    private String seqNumber;
    private String sponsorName;
    /* CASE #352 Begin */
    private String personId;
    /* CASE #352 End */
    /* CASE #410 Begin */
    private String annDisclUpdated;
    /* CASE #410 End */

    /** Creates new EntityDetailsBean */
    public EntityDetailsBean() {
    }

    /* CASE #410 Begin */
    /**
     * Get annDisclUpdated
     * @return
     */
    public String getAnnDisclUpdated(){
        return annDisclUpdated;
    }

    /**
     * Set annDisclUpdated
     * @param annDisclUpdated
     */
    public void setAnnDisclUpdated(String annDisclUpdated){
        this.annDisclUpdated = annDisclUpdated;
    }
    /* CASE #410 End */

    /* CASE #352 Begin */
    /**
     * Set personId.
     */
    public void setPersonId(String personId){
        this.personId = personId;
    }

    /**
     * Get personId.
     */
    public String getPersonId(){
        return personId;
    }
    /* CASE #352 End */
    /**
     *  This method gets the Sequence Number
     *  @return String seqNumber
     */
    public String getSeqNumber() {
        return seqNumber;
    }
    /**
     *  This method sets the Sequence Number
     *  @param String seqNumber
     */
    public void setSeqNumber(java.lang.String seqNumber) {
        this.seqNumber = UtilFactory.checkNullStr(seqNumber);
    }
    /**
     *  This method gets the Number
     *  @return String number
     */
    public String getNumber() {
        return number;
    }
    /**
     *  This method sets the Number
     *  @param String number
     */
    public void setNumber(java.lang.String number) {
        this.number = UtilFactory.checkNullStr(number);
    }
    /**
     *  This method gets the Update User
     *  @return String updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     *  This method sets the Update User
     *  @param String updateUser
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = UtilFactory.checkNullStr(updateUser);
    }
    /**
     *  This method gets the Last Update
     *  @return String lastUpdate
     */
    public java.sql.Timestamp getLastUpdate() {
        return lastUpdate;
    }
    /**
     *  This method sets the Last Update
     *  @param String lastUpdate
     */
    public void setLastUpdate(java.sql.Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     *  This method gets the Sponsor
     *  @return String sponsor
     */
    public String getSponsor() {
        return sponsor;
    }
    /**
     *  This method sets the Sponsor
     *  @param String sponsor
     */
    public void setSponsor(java.lang.String sponsor) {
        this.sponsor = UtilFactory.checkNullStr(sponsor);
    }
    /**
     *  The method gets the Sponsor name
     *  @return String sponsor name
     */
    public String getSponsorName() {
        return sponsorName;
    }
    /**
     *  The method sets the Sponsor name
     *  @param String sponsor name
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = UtilFactory.checkNullStr(sponsorName);
    }
    /**
     *  This method gets the Organisation Description
     *  @return String orgDescription
     */
    public String getOrgDescription() {
        return orgDescription;
    }
    /**
     *  This method sets the Organisation Description
     *  @param String orgDescription
     */
    public void setOrgDescription(java.lang.String orgDescription) {
        this.orgDescription = UtilFactory.checkNullStr(orgDescription);
    }
    /**
     *  This method gets the Organisation Relationship
     *  @return String orgRelationship
     */
    public String getOrgRelationship() {
        return orgRelationship;
    }
    /**
     *  This method sets the Organisation Relationship
     *  @param String orgRelationship
     */
    public void setOrgRelationship(java.lang.String orgRelationship) {
        this.orgRelationship = UtilFactory.checkNullStr(orgRelationship);
    }
    /**
     *  This method gets the Person Relationship Desc
     *  @return String personReDesc
     */
    public String getPersonReDesc() {
        return personReDesc;
    }
    /**
     *  This method sets the Person Relationship Desc
     *  @param String personReDesc
     */
    public void setPersonReDesc(java.lang.String personReDesc) {
        this.personReDesc = UtilFactory.checkNullStr(personReDesc);
    }
    /**
     *  This method gets the Person Relationship Type
     *  @return String personReType
     */
    public String getPersonReType() {
        return personReType;
    }
    /**
     *  This method sets the Person Relationship Type
     *  @param String personReType
     */
    public void setPersonReType(java.lang.String personReType) {
        this.personReType = UtilFactory.checkNullStr(personReType);
    }
    /**
     *  This method gets the Person Relationship Type code
     *  @return String personReTypeCode
     */
    public String getPersonReTypeCode() {
        return personReTypeCode;
    }
    /**
     *  This method sets the Person Relationship Type code
     *  @param String personReTypeCode
     */
    public void setPersonReTypeCode(java.lang.String personReTypeCode) {
        this.personReTypeCode = UtilFactory.checkNullStr(personReTypeCode);
    }
    /**
     *  This method gets the Entity Description
     *  @return String entityDescription
     */
    public String getEntityDescription() {
        return entityDescription;
    }
    /**
     *  This method sets the Entity Description
     *  @param String entityDescription
     */
    public void setEntityDescription(java.lang.String entityDescription) {
        this.entityDescription = UtilFactory.checkNullStr(entityDescription);
    }
    /**
     *  This method gets the Name
     *  @return String name
     */
    public String getName() {
        return name;
    }
    /**
     *  This method sets the Name
     *  @param String name
     */
    public void setName(java.lang.String name) {
        this.name = UtilFactory.checkNullStr(name);
    }
    /**
     *  This method gets the Type
     *  @return String type
     */
    public String getType() {
        return type;
    }
    /**
     *  This method sets the Type
     *  @param String type
     */
    public void setType(java.lang.String type) {
        this.type = UtilFactory.checkNullStr(type);
    }
    /**
     *  This method gets the Type Code
     *  @return String Type Code
     */
    public String getTypeCode() {
        return typeCode;
    }
    /**
     *  This method sets the Type Code
     *  @param String Type Code
     */
    public void setTypeCode(java.lang.String typeCode) {
        this.typeCode = UtilFactory.checkNullStr(typeCode);
    }
    /**
     *  This method gets the Status
     *  @return String status
     */
    public String getStatus() {
        return status;
    }
    /**
     *  This method sets the Status
     *  @param String status
     */
    public void setStatus(java.lang.String status) {
        this.status = UtilFactory.checkNullStr(status);
    }
    /**
     *  This method gets the Status Code
     *  @return String status code
     */
    public String getStatusCode() {
        return statusCode;
    }
    /**
     *  This method sets the Status Code
     *  @param String status code
     */
    public void setStatusCode(java.lang.String statusCode) {
        this.statusCode = UtilFactory.checkNullStr(statusCode);
    }
    /**
     *  This method gets the Status Description
     *  @return String statusDesc
     */
    public String getStatusDesc() {
        return statusDesc;
    }
    /**
     *  This method sets the Status Description
     *  @param String statusDesc
     */
    public void setStatusDesc(java.lang.String statusDesc) {
        this.statusDesc = UtilFactory.checkNullStr(statusDesc);
    }
    /**
     *  This method gets the Share Ownership
     *  @return String shareOwnership
     */
    public String getShareOwnship() {
        return shareOwnship;
    }
    /**
     *  This method sets the Share Ownership
     *  @param String shareOwnership
     */
    public void setShareOwnship(java.lang.String shareOwnship) {
        this.shareOwnship = UtilFactory.checkNullStr(shareOwnship);
    }
}