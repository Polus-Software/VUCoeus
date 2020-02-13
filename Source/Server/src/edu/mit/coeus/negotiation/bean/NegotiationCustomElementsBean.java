/*
 * NegotiationCustomElementsBean.java
 *
 * Created on July 13, 2004, 6:50 PM
 */

package edu.mit.coeus.negotiation.bean;

import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.bean.CoeusBean;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  jobinelias
 */
public class NegotiationCustomElementsBean extends CustomElementsInfoBean implements CoeusBean {
	
	private String negotiationNumber = null;	
	
	/** Creates a new instance of NegotiationCustomElementsBean */
	public NegotiationCustomElementsBean() {
	}
	
	/** Creates a new instance of NegotiationCustomElementsBean */
	public NegotiationCustomElementsBean(CustomElementsInfoBean customElementsInfoBean ) {
		super(customElementsInfoBean);
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
	
	/** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        NegotiationCustomElementsBean negotiationCustomElementsBean = (NegotiationCustomElementsBean)obj;
        if (negotiationCustomElementsBean.getNegotiationNumber().equals(getNegotiationNumber()) &&
			 negotiationCustomElementsBean.getColumnName().equals(getColumnName())) {
            return true;
        } else {
            return false;
        }
    }
	
	public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
		return true;
	}
	
}
