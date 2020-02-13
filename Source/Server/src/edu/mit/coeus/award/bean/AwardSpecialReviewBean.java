/*
 * @(#)AwardSpecialReviewBean.java 1.0 06/02/04 1:01 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;
import edu.mit.coeus.bean.SpecialReviewFormBean;
import edu.mit.coeus.bean.IBaseDataBean;

/** The class is used to hold the information of <code>Award Special Review</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 02, 2004, 1:01 PM
 */

public class AwardSpecialReviewBean extends SpecialReviewFormBean implements IBaseDataBean, java.io.Serializable  {

    //holds the award number.....
     private String mitAwardNumber;
     //private int rowId;
     
     private PropertyChangeSupport propertySupport;

    /** Creates new AwardSpecialReviewBean */
     public AwardSpecialReviewBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /** This is a Copy Constructor. This is used to copy all the contents 
     *  of a given SpecialReviewFormBean to ProposalSpecialReviewFormBean.
     *  
     * @param specialReviewFormBean to be copied to ProposalSpecialReviewFormBean
     */
    
     public AwardSpecialReviewBean(SpecialReviewFormBean specialReviewFormBean) {
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
        //For the Coeus Enhancement case:#1799 start step:1
        setPrevSpRevProtocolNumber(specialReviewFormBean.getPrevSpRevProtocolNumber());
        setProtoSequenceNumber(specialReviewFormBean.getProtoSequenceNumber());
        setAw_ApprovalCode(specialReviewFormBean.getAw_ApprovalCode());
        setAw_SpecialReviewCode(specialReviewFormBean.getAw_SpecialReviewCode());
        //End Coeus Enhancement case:#1799 step:1
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
        AwardSpecialReviewBean awardSpecialReviewBean = (AwardSpecialReviewBean)obj;
        if(awardSpecialReviewBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
            awardSpecialReviewBean.getSequenceNumber() == getSequenceNumber() &&        
            awardSpecialReviewBean.getRowId() == getRowId()){
                return true;
       }else{
            return false;
        }
    }
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     *
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     *
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }    
}