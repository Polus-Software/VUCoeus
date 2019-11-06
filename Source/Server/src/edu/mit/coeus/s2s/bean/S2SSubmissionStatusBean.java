/*
 * S2SSubmissionStatusBean.java
 *
 * Created on October 20, 2004, 9:04 AM
 */

package edu.mit.coeus.s2s.bean;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author  geot
 */
public class S2SSubmissionStatusBean implements Serializable{
    
    private String ggTrackingNumber;
    private Calendar appReceiptDate;
    /** Creates a new instance of S2SSubmissionStatusBean */
    public S2SSubmissionStatusBean() {
    }
    
    /**
     * Getter for property ggTrackingNumber.
     * @return Value of property ggTrackingNumber.
     */
    public java.lang.String getGgTrackingNumber() {
        return ggTrackingNumber;
    }
    
    /**
     * Setter for property ggTrackingNumber.
     * @param ggTrackingNumber New value of property ggTrackingNumber.
     */
    public void setGgTrackingNumber(java.lang.String ggTrackingNumber) {
        this.ggTrackingNumber = ggTrackingNumber;
    }
    
    /**
     * Getter for property appReceiptDate.
     * @return Value of property appReceiptDate.
     */
    public java.util.Calendar getAppReceiptDate() {
        return appReceiptDate;
    }
    
    /**
     * Setter for property appReceiptDate.
     * @param appReceiptDate New value of property appReceiptDate.
     */
    public void setAppReceiptDate(java.util.Calendar appReceiptDate) {
        this.appReceiptDate = appReceiptDate;
    }
    
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Recieved Date=>"+this.getAppReceiptDate().getTime().toString());
        strBffr.append(" : GrantsGov Tracking Number=>"+this.getGgTrackingNumber());
        return strBffr.toString();
    }
}
