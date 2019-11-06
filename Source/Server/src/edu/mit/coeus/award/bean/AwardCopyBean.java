/**
 * @(#)AwardCopyBean.java 1.0 March 23, 2004 12:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;


/**
 * This class is used to hold Award Copy data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 23, 2004 12:30 PM
 */

public class AwardCopyBean implements BaseBean, java.io.Serializable{
    
    private String copyAsNewOrChild;
    private boolean copyDescendents;
    private CoeusVector descendents;
    private String sourceAwardNumber;
    private String targetAwardNumber;
    
    /**
     *	Default Constructor
     */
    
    public AwardCopyBean(){
    }        

    /** Getter for property copyAsNewOrChild.
     * @return Value of property copyAsNewOrChild.
     */
    public java.lang.String getCopyAsNewOrChild() {
        return copyAsNewOrChild;
    }    
    
    /** Setter for property copyAsNewOrChild.
     * @param copyAsNewOrChild New value of property copyAsNewOrChild.
     */
    public void setCopyAsNewOrChild(java.lang.String copyAsNewOrChild) {
        this.copyAsNewOrChild = copyAsNewOrChild;
    }    
    
    /** Getter for property sourceAwardNumber.
     * @return Value of property sourceAwardNumber.
     */
    public java.lang.String getSourceAwardNumber() {
        return sourceAwardNumber;
    }
    
    /** Setter for property sourceAwardNumber.
     * @param sourceAwardNumber New value of property sourceAwardNumber.
     */
    public void setSourceAwardNumber(java.lang.String sourceAwardNumber) {
        this.sourceAwardNumber = sourceAwardNumber;
    }
    
    /** Getter for property targetAwardNumber.
     * @return Value of property targetAwardNumber.
     */
    public java.lang.String getTargetAwardNumber() {
        return targetAwardNumber;
    }
    
    /** Setter for property targetAwardNumber.
     * @param targetAwardNumber New value of property targetAwardNumber.
     */
    public void setTargetAwardNumber(java.lang.String targetAwardNumber) {
        this.targetAwardNumber = targetAwardNumber;
    }
    
    /** Getter for property descendents.
     * @return Value of property descendents.
     */
    public edu.mit.coeus.utils.CoeusVector getDescendents() {
        return descendents;
    }
    
    /** Setter for property descendents.
     * @param descendents New value of property descendents.
     */
    public void setDescendents(edu.mit.coeus.utils.CoeusVector descendents) {
        this.descendents = descendents;
    }
    
    /** Getter for property copyDescendents.
     * @return Value of property copyDescendents.
     */
    public boolean isCopyDescendents() {
        return copyDescendents;
    }
    
    /** Setter for property copyDescendents.
     * @param copyDescendents New value of property copyDescendents.
     */
    public void setCopyDescendents(boolean copyDescendents) {
        this.copyDescendents = copyDescendents;
    }
    
}