/*
 * CoiDisclForItemBean.java
 *
 * Created on August 18, 2004, 5:07 PM
 */

package edu.mit.coeus.bean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class CoiDisclForItemBean implements java.io.Serializable, BaseBean {
	
	private String coiDisclosureNumber;
    private String moduleItemKey;
    private int moduleCode;
    private String personId;
    private String disclosureType;
	private String coiDisclosureStatusCode;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;    
	
	/** Creates a new instance of CoiDisclForItemBean */
	public CoiDisclForItemBean() {
	}
	
	/**
	 * Getter for property coiDisclosureNumber.
	 * @return Value of property coiDisclosureNumber.
	 */
	public java.lang.String getCoiDisclosureNumber() {
		return coiDisclosureNumber;
	}
	
	/**
	 * Setter for property coiDisclosureNumber.
	 * @param coiDisclosureNumber New value of property coiDisclosureNumber.
	 */
	public void setCoiDisclosureNumber(java.lang.String coiDisclosureNumber) {
		this.coiDisclosureNumber = coiDisclosureNumber;
	}
	
	/**
	 * Getter for property moduleItemKey.
	 * @return Value of property moduleItemKey.
	 */
	public java.lang.String getModuleItemKey() {
		return moduleItemKey;
	}
	
	/**
	 * Setter for property moduleItemKey.
	 * @param moduleItemKey New value of property moduleItemKey.
	 */
	public void setModuleItemKey(java.lang.String moduleItemKey) {
		this.moduleItemKey = moduleItemKey;
	}
	
	/**
	 * Getter for property moduleCode.
	 * @return Value of property moduleCode.
	 */
	public int getModuleCode() {
		return moduleCode;
	}
	
	/**
	 * Setter for property moduleCode.
	 * @param moduleCode New value of property moduleCode.
	 */
	public void setModuleCode(int moduleCode) {
		this.moduleCode = moduleCode;
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
	public java.sql.Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}
	
	/**
	 * Setter for property updateTimestamp.
	 * @param updateTimestamp New value of property updateTimestamp.
	 */
	public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}
	
	/**
	 * Getter for property coiDisclosureStatusCode.
	 * @return Value of property coiDisclosureStatusCode.
	 */
	public java.lang.String getCoiDisclosureStatusCode() {
		return coiDisclosureStatusCode;
	}
	
	/**
	 * Setter for property coiDisclosureStatusCode.
	 * @param coiDisclosureStatusCode New value of property coiDisclosureStatusCode.
	 */
	public void setCoiDisclosureStatusCode(java.lang.String coiDisclosureStatusCode) {
		this.coiDisclosureStatusCode = coiDisclosureStatusCode;
	}
	
	/**
	 * Getter for property disclosureType.
	 * @return Value of property disclosureType.
	 */
	public java.lang.String getDisclosureType() {
		return disclosureType;
	}
	
	/**
	 * Setter for property disclosureType.
	 * @param disclosureType New value of property disclosureType.
	 */
	public void setDisclosureType(java.lang.String disclosureType) {
		this.disclosureType = disclosureType;
	}
	
}
