/**
 * @(#)UnitMapBean.java 1.0 01/02/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.propdev.bean;

import java.sql.Timestamp;
import java.util.Vector;
/**
 * This class used to set and get Unit Map
  *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 02, 2004 11:30 PM
 */

public class UnitMapBean implements java.io.Serializable{
    
    private int mapId;
    private String mapType;
    private String defaultMapType;
    private String unitNumber;
    private String description;
    private Timestamp updateTimeStamp;
    private String updateUser;
    //Added for Userid to Username enhancement
    private String updateUserName;
    private String acType;
    private Vector mapDetails;
    
    /**
     *	Default Constructor
     */
    
    public UnitMapBean(){
    }
    
    /** Getter for property mapId.
     * @return Value of property mapId.
     */
    public int getMapId() {
        return mapId;
    }    

    /** Setter for property mapId.
     * @param mapId New value of property mapId.
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    
    /** Getter for property mapType.
     * @return Value of property mapType.
     */
    public java.lang.String getMapType() {
        return mapType;
    }
    
    /** Setter for property mapType.
     * @param mapType New value of property mapType.
     */
    public void setMapType(java.lang.String mapType) {
        this.mapType = mapType;
    }
    
    /** Getter for property defaultMapType.
     * @return Value of property defaultMapType.
     */
    public java.lang.String getDefaultMapType() {
        return defaultMapType;
    }
    
    /** Setter for property defaultMapType.
     * @param defaultMapType New value of property defaultMapType.
     */
    public void setDefaultMapType(java.lang.String defaultMapType) {
        this.defaultMapType = defaultMapType;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property updateTimeStamp.
     * @return Value of property updateTimeStamp.
     */
    public java.sql.Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /** Setter for property updateTimeStamp.
     * @param updateTimeStamp New value of property updateTimeStamp.
     */
    public void setUpdateTimeStamp(java.sql.Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
    /** Getter for property mapDetails.
     * @return Value of property mapDetails.
     */
    public Vector getMapDetails() {
        return mapDetails;
    }
    
    /** Setter for property mapDetails.
     * @param mapDetails New value of property mapDetails.
     */
    public void setMapDetails(Vector mapDetails) {
        this.mapDetails = mapDetails;
    }

    //Userid to Username enhancement - Start
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUsername New value of property updateUserName.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //Userid to Username enhancement - End
}