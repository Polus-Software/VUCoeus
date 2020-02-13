/**
 * @(#)OrganizationList.java 1.0 8/21/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;


import java.sql.Timestamp;
/**
 * This class represent the Organization List Data Bean and is
 * used to set and gets the Organization list Attributes
 *
 * @version :1.0 August 21,2002
 * @author Guptha K
 */

public class OrganizationListBean implements java.io.Serializable{
    
    private String organizationId;
    private int organizationTypeCode;
    private String description;
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    
    /**
     *	Default Constructor
     */
    
    public OrganizationListBean(){
    }
    
    /**
     *	This method is used to fetch the Organization Id
     *	@return String organizationId
     */
    
    public String getOrganizationId(){
        return organizationId;
    }
    
    /**
     *	This method is used to set the Organization Id
     *	@param organizationId String value
     */
    
    public void setOrganizationId(String organizationId){
        this.organizationId = organizationId;
    }
    
    /**
     *	This method is used to fetch the Organization Type Code
     *	@return organizationTypeCode int value
     */
    
    public int getOrganizationTypeCode() {
        return organizationTypeCode;
    }
    
    /**
     *	This method is used to set the Organization Type Code
     *	@param organizationTypeCode int value
     */
    
    public void setOrganizationTypeCode(int organizationTypeCode) {
        this.organizationTypeCode = organizationTypeCode;
    }
    
    /**
     *	This method is used to fetch the description
     *	@return String description
     */
    
    public String getDescription() {
        return description;
    }
    
    /**
     *	This method is used to set the description
     *	@param description String 
     */
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     *	This method is used to fetch the timestamp of the update
     *	@return  String  updateTimeStamp
     */
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     *	This method is used to set the timestamp of the update
     *	@param updateTimeStamp String 
     */
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
     *	@param updateUser String value
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