/*
 * SubContractCustomDataBean.java
 *
 * Created on September 14, 2004, 12:30 PM
 */

package edu.mit.coeus.subcontract.bean;

import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.IBaseDataBean;

/**
 *
 * @author  bijosht
 */
public class SubContractCustomDataBean extends CustomElementsInfoBean implements CoeusBean,IBaseDataBean {
    
    private String subContractCode;
	private int sequenceNumber = -1;
    
	
    /** Creates a new instance of SubContractCustomDataBean */
    public SubContractCustomDataBean() {
    }
    
	public SubContractCustomDataBean (CustomElementsInfoBean customElementsInfoBean) {
        super(customElementsInfoBean);
    }
    
	/**
	 * Getter for property subContractCode.
	 * @return Value of property subContractCode.
	 */
	public java.lang.String getSubContractCode() {
		return subContractCode;
	}
	
	/**
	 * Setter for property subContractCode.
	 * @param subContractCode New value of property subContractCode.
	 */
	public void setSubContractCode(java.lang.String subContractCode) {
		this.subContractCode = subContractCode;
	}
	
	/**
	 * Getter for property sequenceNumber.
	 * @return Value of property sequenceNumber.
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	
	/**
	 * Setter for property sequenceNumber.
	 * @param sequenceNumber New value of property sequenceNumber.
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return false;
    }
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        SubContractCustomDataBean subContractCustomDataBean = (SubContractCustomDataBean)obj;
        if (subContractCustomDataBean.getSubContractCode().equals(getSubContractCode()) &&
            subContractCustomDataBean.getSequenceNumber() == getSequenceNumber() && 
             subContractCustomDataBean.getColumnName().equals(getColumnName())) {
            return true;
        }else{
            return false;
        }
    }
}
