/**
 * @(#)SponsorContactBean.java 1.0 28/01/05
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.sponsormaint.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.sql.Timestamp;

/**
 * This class is used to hold Sponsor Contact data
 *
 * @author Jinu
 * @version 1.0
 * Created on January 28, 2005 12:30 PM
 */

public class SponsorContactBean implements CoeusBean, java.io.Serializable {
    
    private String sponsorCode;
    private int sponsorContactTypeCode;
    private String personId;
    private String personName;
    private String acType;
    private String updateUser;
    private Timestamp updateTimestamp;
    
    private String awSponsorCode;
    private int awSponsorContactTypeCode;
    private String awPersonId;
    /**
     *	Default Constructor
     */
    
    public SponsorContactBean(){
    }       

    /**
     * Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /**
     * Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }
    
    /**
     * Getter for property sponsorContactTypeCode.
     * @return Value of property sponsorContactTypeCode.
     */
    public int getSponsorContactTypeCode() {
        return sponsorContactTypeCode;
    }
    
    /**
     * Setter for property sponsorContactTypeCode.
     * @param sponsorContactTypeCode New value of property sponsorContactTypeCode.
     */
    public void setSponsorContactTypeCode(int sponsorContactTypeCode) {
        this.sponsorContactTypeCode = sponsorContactTypeCode;
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property awSponsorCode.
     * @return Value of property awSponsorCode.
     */
    public java.lang.String getAwSponsorCode() {
        return awSponsorCode;
    }
    
    /**
     * Setter for property awSponsorCode.
     * @param awSponsorCode New value of property awSponsorCode.
     */
    public void setAwSponsorCode(java.lang.String awSponsorCode) {
        this.awSponsorCode = awSponsorCode;
    }
    
    /**
     * Getter for property awSponsorContactTypeCode.
     * @return Value of property awSponsorContactTypeCode.
     */
    public int getAwSponsorContactTypeCode() {
        return awSponsorContactTypeCode;
    }
    
    /**
     * Setter for property awSponsorContactTypeCode.
     * @param awSponsorContactTypeCode New value of property awSponsorContactTypeCode.
     */
    public void setAwSponsorContactTypeCode(int awSponsorContactTypeCode) {
        this.awSponsorContactTypeCode = awSponsorContactTypeCode;
    }
    
    /**
     * Getter for property awPersonId.
     * @return Value of property awPersonId.
     */
    public java.lang.String getAwPersonId() {
        return awPersonId;
    }
    
    /**
     * Setter for property awPersonId.
     * @param awPersonId New value of property awPersonId.
     */
    public void setAwPersonId(java.lang.String awPersonId) {
        this.awPersonId = awPersonId;
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        SponsorContactBean sponsorContactBean = (SponsorContactBean)obj;
        if(sponsorContactBean.getSponsorCode().equals(getSponsorCode()) &&
            sponsorContactBean.getSponsorContactTypeCode() == getSponsorContactTypeCode() &&
            sponsorContactBean.getPersonId().equals(getPersonId())){
            return true;
        }else{
            return false;
        }
    }    
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
}