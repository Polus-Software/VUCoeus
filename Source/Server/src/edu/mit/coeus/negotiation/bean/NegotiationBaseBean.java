/*
 * NegotiationBaseBean.java
 * This is the Base class for all Negotiation Beans
 * Created on July 13, 2004, 12:42 PM
 */

package edu.mit.coeus.negotiation.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.CoeusBean; 
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public abstract class NegotiationBaseBean implements CoeusBean, IBaseDataBean, 
											java.io.Serializable {
												
	private String negotiationNumber = null;											
	private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;    
    private String acType = null;  
	private int sequenceNumber = -1;
	
	/** Creates a new instance of NegotiationBaseBean */
	public NegotiationBaseBean() {
	}
	
	/** Getter for property negotiation Number.
     * @return Value of property negotiation number.
     */
    public java.lang.String getNegotiationNumber() {
        return negotiationNumber;
    }
    
    /** Setter for property negotiation number.
     * @param negotiationNumber New value of property mitAwardNumber.
     */
    public void setNegotiationNumber(java.lang.String negotiationNumber) {
        this.negotiationNumber = negotiationNumber;
    }
	
	/** Getter for property sequenceNumber.
	 * @return Value of property sequenceNumber.
	 *
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	/** Setter for property sequenceNumber.
	 * @param sequenceNumber New value of property sequenceNumber.
	 *
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
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
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
	
	public boolean isLike(ComparableBean comparableBean)
    throws CoeusException{
        return true;
    }
	
	 /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationBaseBean negotiationBaseBean = (NegotiationBaseBean)obj;
        if (negotiationBaseBean.getNegotiationNumber().equals(getNegotiationNumber())) {
            return true;
        } else {
            return false;
        }
    }    
}
