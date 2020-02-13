/*
 * @(#)InstituteProposalSpecialReviewBean.java 1.0 05/14/03 4:38 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.instprop.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.bean.IBaseDataBean;
/** The class is used to hold the information of <code>Institute Proposal Special Review</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 12, 2003, 11:45 AM
 */

public class InstituteProposalSpecialReviewBean extends SpecialReviewFormBean implements IBaseDataBean,java.io.Serializable  {

    //holds the proposal number.....
     private String proposalNumber;
     //private int rowId;
     
     private PropertyChangeSupport propertySupport;
     
    
    /** Creates new ProposalSpecialReviewFormBean */
    public InstituteProposalSpecialReviewBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /** This is a Copy Constructor. This is used to copy all the contents 
     *  of a given SpecialReviewFormBean to ProposalSpecialReviewFormBean.
     *  
     * @param specialReviewFormBean to be copied to ProposalSpecialReviewFormBean
     */
    
    public InstituteProposalSpecialReviewBean(SpecialReviewFormBean specialReviewFormBean) {
        propertySupport = new PropertyChangeSupport( this );
        
        setRowId(specialReviewFormBean.getRowId());
        setSequenceNumber(specialReviewFormBean.getSequenceNumber());
        setSpecialReviewNumber(specialReviewFormBean.getSpecialReviewNumber());
        setSpecialReviewCode(specialReviewFormBean.getSpecialReviewCode());
        setSpecialReviewDescription(specialReviewFormBean.getSpecialReviewDescription());
        setApprovalCode(specialReviewFormBean.getApprovalCode());
        setApprovalDescription(specialReviewFormBean.getApprovalDescription());
        setProtocolSPRevNumber(specialReviewFormBean.getProtocolSPRevNumber());
        setComments(specialReviewFormBean.getComments());
        setApplicationDate(specialReviewFormBean.getApplicationDate());
        setApprovalDate(specialReviewFormBean.getApprovalDate());
        setUpdateUser(specialReviewFormBean.getUpdateUser());
        setUpdateTimestamp(specialReviewFormBean.getUpdateTimestamp());
        setAcType(specialReviewFormBean.getAcType());
        //Added for the Coeus Enhancement case:#1799 start 
        setPrevSpRevProtocolNumber(specialReviewFormBean.getPrevSpRevProtocolNumber());
        setProtoSequenceNumber(specialReviewFormBean.getProtoSequenceNumber());
        setAw_ApprovalCode(specialReviewFormBean.getAw_ApprovalCode());
        setAw_SpecialReviewCode(specialReviewFormBean.getAw_SpecialReviewCode());
        //End Coeus Enhancement case:#1799 
    }
    
    /** This method is used to fetch the Proposal Number 
     * @return String proposalNumber*/    
    public String getProposalNumber() {
        return proposalNumber;
    }

    /** This method is used to set the Proposal Number 
     * @param proposalNumber String */    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        InstituteProposalSpecialReviewBean instituteProposalSpecialReviewBean= (InstituteProposalSpecialReviewBean)obj;
        if(instituteProposalSpecialReviewBean.getProposalNumber().equals(getProposalNumber()) &&
            instituteProposalSpecialReviewBean.getSequenceNumber() == getSequenceNumber() &&        
            instituteProposalSpecialReviewBean.getRowId() == getRowId()){
                return true;
       }else{
            return false;
        }
    }
    
      
    
//    /** Getter for property rowId.
//     * @return Value of property rowId.
//     */
//    public int getRowId() {
//        return rowId;
//    }
//    
//    /** Setter for property rowId.
//     * @param rowId New value of property rowId.
//     */
//    public void setRowId(int rowId) {
//        this.rowId = rowId;
//    }
    
}