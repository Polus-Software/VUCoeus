/**
 * @(#)OrganizationIDCBean.java 1.0 8/22/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;


import java.sql.Timestamp;

/**
 * This class represent Organization IDC Data Bean and is 
 * used to set and gets the Organization IDC details
 *
 * @version :1.0 August 22,2002
 * @author Guptha K
 */

public class OrganizationIDCBean implements java.io.Serializable{
    private String organizationId;
    private int idcNumber;
    private String startDate;
    private String endDate;
    private String requestedDate;
    private int idcRateTypeCode;
    private float applicableIdcRate;
    private String idcComment;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    
    /**
     *  Default Constructor
     */
    
    public OrganizationIDCBean(){
    }
    
    /**
     *	This method is used to fetch the organization id
     *  @return String organizationId
     */
    
    public String getOrganizationId(){
        return organizationId;
    }
    
    /**
     *	This method is used to set the organization id
     *  @param orgId String value
     */
    
    public void setOrganizationId(String orgId){
        this.organizationId = orgId;
    }
    
    /**
     *	This method is used to fetch the IDC number
     *  @return int idcNumber
     */
    
    public int getIdcNumber() {
        return idcNumber;
    }
    
    /**
     *	This method is used to set the IDC Number
     *  @param idcNumber int value
     */
    
    public void setIdcNumber(int idcNumber) {
        this.idcNumber = idcNumber;
    }
    
    /**
     *	This method is used to fetch the Start Date
     *  @return String startDate
     */
    
    public String getStartDate() {
        return startDate;
    }
    
    /**
     *	This method is used to set the Start Date
     *  @param startDate String value
     */
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    /**
     *	This method is used to fetch the End Date
     *  @return String endDate
     */
    
    public String getEndDate() {
        return endDate;
    }
    
    /**
     *	This method is used to set the End Date
     *  @param endDate String Value
     */
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    /**
     *	This method is used to fetch the Requested Date
     *  @return requestedDate String value
     */
    
    public String getRequestedDate() {
        return requestedDate;
    }
    
    /**
     *	This method is used to set the Requested Date
     *  @param requestedDate String value
     */
    
    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }
    
    /**
     *	This method is used to fetch the IDC Rate Type Code
     *  @return int idcRateTypeCode
     */
    
    public int getIdcRateTypeCode() {
        return idcRateTypeCode;
    }
    
    /**
     *	This method is used to set the IDC Rate Type Code
     *  @param idcRateTypeCode int value
     */
    
    public void setIdcRateTypeCode(int idcRateTypeCode) {
        this.idcRateTypeCode = idcRateTypeCode;
    }
    
    /**
     *	This method is used to  fetch the Applicable IDC Rate
     *  @return float applicableIdcRate
     */
    
    public float getApplicableIdcRate() {
        return applicableIdcRate;
    }
    
    /**
     *	This method is used to set the Applicable IDC Rate
     *  @param applicableIdcRate float value
     */
    
    public void setApplicableIdcRate(float applicableIdcRate) {
        this.applicableIdcRate = applicableIdcRate;
    }
    
    /**
     *	This method is used to fetch the IDC Comment
     *	@return String idcNumber
     */
    
    public String getIdcComment() {
        return idcComment;
    }
    
    /**
     *	This method is used to set the IDC Number
     *	@param idcNumber String value
     */
    
    public void setIdcComment(String idcComment) {
        this.idcComment = idcComment;
    }
    
    /**
     *	This method is used to fetch the timestamp of the update
     *	@return updateTimeStamp String value
     */
    
    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /**
     *	This method is used to set the timestamp of the update
     *	@param updateTimeStamp String  value
     */
    
    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
    /**
     *	This method is used to fetch the user who updated the details
     *	@return String updateUser
     */
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     *	This method is used to set the user who updated the details
     *	@param	updateUser String value
     */
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     *	This method is used to get the action type Add/Update
     *	@return String updateUser
     */
    
    public String getAcType() {
        return acType;
    }
    
    /**
     *	This method is used to set the action type Add/Update
     *	@param	acType String value
     */
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
}