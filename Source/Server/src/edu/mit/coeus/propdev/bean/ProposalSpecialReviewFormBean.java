/*
 * @(#)ProposalSpecialReviewFormBean.java 1.0 05/14/03 4:38 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.SpecialReviewFormBean;

/** The class is used to hold the information of <code>Proposal Special Review</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 19, 2003, 11:45 AM
 */

public class ProposalSpecialReviewFormBean extends SpecialReviewFormBean implements java.io.Serializable  {

    //holds the proposal number.....
     private String proposalNumber;
     //holds the special review number
     /*private int specialReviewNumber;
     //holds the special review code
     private int specialReviewCode;
     //holds the approval type code
     private int approvalTypeCode;
     //holds the protocol number
     private String protocolNumber;
     //holds the application date
     private Date applicationDate;
	//holds the approval date
     private Date approvalDate;
     //holds the comments
     private String comments;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;*/
     //holds status code
     private int protocolStatusCode;

    private PropertyChangeSupport propertySupport;

    /** Creates new ProposalSpecialReviewFormBean */
    public ProposalSpecialReviewFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /** This is a Copy Constructor. This is used to copy all the contents 
     *  of a given SpecialReviewFormBean to ProposalSpecialReviewFormBean.
     *  
     * @param specialReviewFormBean to be copied to ProposalSpecialReviewFormBean
     */
    
    public ProposalSpecialReviewFormBean(SpecialReviewFormBean specialReviewFormBean) {
        propertySupport = new PropertyChangeSupport( this );
        
        setSequenceNumber(specialReviewFormBean.getSequenceNumber());
        setSpecialReviewNumber(specialReviewFormBean.getSpecialReviewNumber());
        setSpecialReviewCode(specialReviewFormBean.getSpecialReviewCode());
        setSpecialReviewDescription(specialReviewFormBean.getSpecialReviewDescription());
        setApprovalCode(specialReviewFormBean.getApprovalCode());
        setApprovalDescription(specialReviewFormBean.getApprovalDescription());
        setProtocolSPRevNumber(specialReviewFormBean.getProtocolSPRevNumber());
        //COEUSQA-2984 : Statuses in special review - start
        //setting the status code
        setProtocolStatusCode(specialReviewFormBean.getProtocolStatusCode());
        //COEUSQA-2984 : Statuses in special review - end
        setComments(specialReviewFormBean.getComments());
        setApplicationDate(specialReviewFormBean.getApplicationDate());
        setApprovalDate(specialReviewFormBean.getApprovalDate());
        setUpdateUser(specialReviewFormBean.getUpdateUser());
        setUpdateTimestamp(specialReviewFormBean.getUpdateTimestamp());
        setAcType(specialReviewFormBean.getAcType());
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
    
     //COEUSQA-2984 : Statuses in special review - start
    /**
     * Method used to get the protocol Status Code
     * @return protocolStatusCode int
     */
    public int getProtocolStatusCode() {
        return protocolStatusCode;
    }
    
    /**
     * Method used to set the protocol Status Code
     * @param protocolStatusCode int
     */
    public void setProtocolStatusCode(int protocolStatusCode) {
        this.protocolStatusCode = protocolStatusCode;
    }
    //COEUSQA-2984 : Statuses in special review - end
}
