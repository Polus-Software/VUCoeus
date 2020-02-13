/**
 * @(#)IDCRateTypesBean.java 1.0 8/27/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;



import java.sql.Timestamp;

/**
 *
 * This class represent the IDC Rate Data bean and it 
 * provides the setter and getter methods for idc rate types.
 *
 * @version 1.0 August 27, 2002, 12:05 PM
 * @author  Guptha K
 */

public class IDCRateTypesBean implements java.io.Serializable{

    private int idcRateTypeCode;
    private String description;
    private Timestamp updateTimestamp;
    private String updateUser;

	/**
	 *	Default Constructor
	 */
	
	public IDCRateTypesBean() {
    }

	/**
	 *	This method is used to fetch the IDC Rate Type Code
	 *	@return int idcRateTypeCode
	 */
    
	public int getIdcRateTypeCode() {
        return idcRateTypeCode;
    }

	/**
	 *	This method is used to set the IDC Rate Type Code
	 *	@param idcRateTypeCode - int
	 */

    public void setIdcRateTypeCode(int idcRateTypeCode) {
        this.idcRateTypeCode = idcRateTypeCode;
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
	 *	@param description String description
	 */

	public void setDescription(String description) {
        this.description = description;
    }

	/**
	 *	This method is used to fetch the timestamp of the update
	 *	@return Timestamp updateTimeStamp
	 */

	public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

	/**
	 *	This method is used to set the timestamp of the update
	 *	@param updateTimestamp String updateTimeStamp
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
	 *	@param updateUser String updateUser
	 */

	public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

}