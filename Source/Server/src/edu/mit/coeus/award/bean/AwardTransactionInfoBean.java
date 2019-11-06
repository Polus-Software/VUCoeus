/**
 * @(#)AwardTransactionInfoBean.java 1.0 June 15, 2004 12:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
/**
 * This class is used to set and get Award Transaction Information
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 15, 2004 12:30 PM
 */

public class AwardTransactionInfoBean extends AwardBaseBean implements java.io.Serializable{
   
    private String transactionId;
    private boolean firstTimeSave;
    
    /**
     *	Default Constructor
     */    
    public AwardTransactionInfoBean(){        
    }   
      
    /** Getter for property transactionId.
     * @return Value of property transactionId.
     *
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }
    
    /** Setter for property transactionId.
     * @param transactionId New value of property transactionId.
     *
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }
    
    /** Getter for property firstTimeSave.
     * @return Value of property firstTimeSave.
     *
     */
    public boolean isFirstTimeSave() {
        return firstTimeSave;
    }
    
    /** Setter for property firstTimeSave.
     * @param firstTimeSave New value of property firstTimeSave.
     *
     */
    public void setFirstTimeSave(boolean firstTimeSave) {
        this.firstTimeSave = firstTimeSave;
    }    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}