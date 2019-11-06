/*
 * NegotiationLocationBean.java
 *
 * Created on May 28, 2008
 */

package edu.mit.coeus.negotiation.bean;

import java.sql.Date;
import java.beans.*;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved. 
 */
public class NegotiationLocationBean extends NegotiationBaseBean {    
        private int locationNumber;
        private int negotiationLocationTypeCode;
	private Date effectiveDate;
        private String negotiationLocationTypeDes;
	
    /** Creates a new instance of NegotiationLocationBean */
    public NegotiationLocationBean() {
    }
    
    public int getLocationNumber() {
        return locationNumber;
    }	
	
    public void SetLocationNumber(int locationNumber) {
      	this.locationNumber = locationNumber;
    }
    
    public int getNegotiationLocationTypeCode() {
        return negotiationLocationTypeCode;
    }	
	
    public void SetNegotiationLocationTypeCode(int negotiationLocationTypeCode) {
      	this.negotiationLocationTypeCode = negotiationLocationTypeCode;
    }
    
    public java.sql.Date getEffectiveDate() {
	return effectiveDate;
    }
	
    public void setEffectiveDate(java.sql.Date effectiveDate) {
	this.effectiveDate = effectiveDate;
    }
    
    public java.lang.String getNegotiationLocationTypeDes() {
	return negotiationLocationTypeDes;
    }	
    public void setNegotiationLocationTypeDes(java.lang.String negotiationLocationTypeDes) {
	this.negotiationLocationTypeDes = negotiationLocationTypeDes;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationLocationBean negotiationLocationBean = (NegotiationLocationBean)obj;
        if (negotiationLocationBean.getNegotiationNumber().equals(getNegotiationNumber()) && negotiationLocationBean.getLocationNumber() == getLocationNumber()) {
            return true;
        } else {
            return false;
        }
    }
}
