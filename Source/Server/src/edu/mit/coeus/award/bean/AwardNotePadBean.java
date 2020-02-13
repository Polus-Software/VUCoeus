/**
 * @(#)AwardNotePadBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Notepad data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardNotePadBean extends AwardBaseBean{
    
    private int entryNumber;    
    private String comments;
    private boolean restrictedView;
    
    /**
     *	Default Constructor
     */
    
    public AwardNotePadBean(){
    }              

    /** Getter for property entryNumber.
     * @return Value of property entryNumber.
     */
    public int getEntryNumber() {
        return entryNumber;
    }    
    
    /** Setter for property entryNumber.
     * @param entryNumber New value of property entryNumber.
     */
    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /** Getter for property restrictedView.
     * @return Value of property restrictedView.
     */
    public boolean isRestrictedView() {
        return restrictedView;
    }
    
    /** Setter for property restrictedView.
     * @param restrictedView New value of property restrictedView.
     */
    public void setRestrictedView(boolean restrictedView) {
        this.restrictedView = restrictedView;
    }    
}