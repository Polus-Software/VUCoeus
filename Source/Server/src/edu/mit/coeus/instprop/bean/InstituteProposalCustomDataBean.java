/**
 * @(#)AwardCustomDataBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import edu.mit.coeus.customelements.bean.CustomElementsInfoBean;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * This class is used to hold Award Custom data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class InstituteProposalCustomDataBean extends CustomElementsInfoBean implements IBaseDataBean, CoeusBean{
    
    private String proposalNumber = null;
    private int sequenceNumber = -1;
    
    /**
     *	Default Constructor
     */
    
    public InstituteProposalCustomDataBean(){
    }                   
  
    public InstituteProposalCustomDataBean(CustomElementsInfoBean customElementsInfoBean){
        super(customElementsInfoBean);
    }
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     *
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     *
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
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
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    
    public boolean equals(Object obj) {
        InstituteProposalCustomDataBean instituteProposalCustomDataBean = (InstituteProposalCustomDataBean)obj;
        if(instituteProposalCustomDataBean.getProposalNumber().equals(getProposalNumber()) &&
            instituteProposalCustomDataBean.getSequenceNumber() == getSequenceNumber() &&
            instituteProposalCustomDataBean.getColumnName().equals(getColumnName())){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }    
}