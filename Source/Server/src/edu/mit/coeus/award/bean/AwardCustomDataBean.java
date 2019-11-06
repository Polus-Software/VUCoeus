/**
 * @(#)AwardCustomDataBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

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

public class AwardCustomDataBean extends CustomElementsInfoBean implements CoeusBean, IBaseDataBean{
    
    private String mitAwardNumber = null;
    private int sequenceNumber = -1;
    
    /**
     *	Default Constructor
     */
    
    public AwardCustomDataBean(){
    }               
    
    /**
     *	Copy Constructor
     */    
    public AwardCustomDataBean(CustomElementsInfoBean customElementsInfoBean){
        super(customElementsInfoBean);
    }

    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }    
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
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
        AwardCustomDataBean awardCustomDataBean = (AwardCustomDataBean)obj;
        if(awardCustomDataBean.getMitAwardNumber().equals(getMitAwardNumber()) &&
            awardCustomDataBean.getSequenceNumber() == getSequenceNumber() &&
            awardCustomDataBean.getColumnName().equals(getColumnName())){
            return true;
        }else{
            return false;
        }
    }    
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return false;
    }
}