/**
 * ProposalApprovedSubcontracts.java
 *
 * @author mcafeekj
 *
 * Created on May 1, 2013
 * Vanderbilt University Office of Research
 */

package edu.vanderbilt.coeus.instprop.bean;

import edu.mit.coeus.instprop.bean.InstituteProposalBaseBean;

public class ProposalApprovedSubcontractBean extends InstituteProposalBaseBean implements java.io.Serializable{
    
	private int rowId;
    private String subcontractName;
    private double amount;
    private String organizationId = null;
    private int locationTypeCode, awLocationTypeCode;
    private String locationTypeDescription;
    
    /**
     *	Default Constructor
     */
    public ProposalApprovedSubcontractBean(){
    }    

    public java.lang.String getSubcontractName() {
        return subcontractName;
    }
    
    public void setSubcontractName(java.lang.String subcontractName) {
        this.subcontractName = subcontractName;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public int getLocationTypeCode() {
    	return locationTypeCode;
    }
    
    public void setLocationTypeCode(int locationTypeCode) {
    	this.locationTypeCode = locationTypeCode;
    }

    public int getAwLocationTypeCode() {
    	return awLocationTypeCode;
    }
    
    public void setAwLocationTypeCode(int awLocationTypeCode) {
    	this.awLocationTypeCode = awLocationTypeCode;
    }
    
    public String getLocationTypeDescription() {
    	return locationTypeDescription;
    }
    
    public void setLocationTypeDescription(String locationTypeDescription) {
    	this.locationTypeDescription = locationTypeDescription;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            ProposalApprovedSubcontractBean propApprovedSubcontractBean = (ProposalApprovedSubcontractBean)obj;
            if(propApprovedSubcontractBean.getRowId() == getRowId()) {
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