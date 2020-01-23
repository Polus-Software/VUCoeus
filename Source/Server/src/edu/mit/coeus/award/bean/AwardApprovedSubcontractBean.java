/**
 * @(#)AwardApprovedSubcontractBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;

/**
 * This class is used to hold Award Approved Subcontract data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardApprovedSubcontractBean extends AwardBaseBean implements java.io.Serializable{
    
    //Holds Row Id. 
    //This is used to identify the record when the user is allowed to 
    //modify Primary Key values.
    private int rowId;
    private String organizationId; // JM 7-30-2013 added
    private String subcontractName;
    private double amount;
    private String aw_SubcontractName = null;
    private int locationTypeCode, awLocationTypeCode;
    private String locationTypeDescription;
    
    /**
     *	Default Constructor
     */
    
    public AwardApprovedSubcontractBean(){
    }    

    /** Getter for property subcontractName.
     * @return Value of property subcontractName.
     */
    public java.lang.String getSubcontractName() {
        return subcontractName;
    }
    
    /** Setter for property subcontractName.
     * @param subcontractName New value of property subcontractName.
     */
    public void setSubcontractName(java.lang.String subcontractName) {
        this.subcontractName = subcontractName;
    }
    
    // JM 7-30-2013 added organization id
    public java.lang.String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(java.lang.String organizationId) {
        this.organizationId = organizationId;
    }
    // JM END
    
    /* JM 5-20-2016 adding location type code */
    public int getLocationTypeCode() {
    	return locationTypeCode;
    }
    
    public void setLocationTypeCode(int locationTypeCode) {
    	this.locationTypeCode = locationTypeCode;
    }
    
    public String getLocationTypeDescription() {
    	return locationTypeDescription;
    }
    
    public void setLocationTypeDescription(String locationTypeDescription) {
    	this.locationTypeDescription = locationTypeDescription;
    }
    
    public int getAwLocationTypeCode() {
    	return awLocationTypeCode;
    }
    
    public void setAwLocationTypeCode(int awLocationTypeCode) {
    	this.awLocationTypeCode = awLocationTypeCode;
    }
    /* JM END */
    
    /** Getter for property amount.
     * @return Value of property amount.
     */
    public double getAmount() {
        return amount;
    }
    
    /** Setter for property amount.
     * @param amount New value of property amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }    
    
    /** Getter for property aw_SubcontractName.
     * @return Value of property aw_SubcontractName.
     */
    public java.lang.String getAw_SubcontractName() {
        return aw_SubcontractName;
    }
    
    /** Setter for property aw_SubcontractName.
     * @param aw_SubcontractName New value of property aw_SubcontractName.
     */
    public void setAw_SubcontractName(java.lang.String aw_SubcontractName) {
        this.aw_SubcontractName = aw_SubcontractName;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardApprovedSubcontractBean awardApprovedSubcontractBean = (AwardApprovedSubcontractBean)obj;
            //if(awardApprovedSubcontractBean.getSubcontractName().equals(getSubcontractName())){
            /*if(awardApprovedSubcontractBean.getAw_SubcontractName() != null && 
                getAw_SubcontractName() != null && 
                awardApprovedSubcontractBean.getAw_SubcontractName().equals(getAw_SubcontractName())){
                return true;
            }else{
                return false;
            }*/
            if(awardApprovedSubcontractBean.getRowId() == getRowId()) {
                return true;       
            }else {
                return false;
            }            
        }else{
            return false;
        }
    }    
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
}