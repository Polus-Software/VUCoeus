/**
 * @(#)AwardHierarchyBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
/**
 * This class is used to hold Award Hierarchy data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardHierarchyBean extends AwardBaseBean implements java.io.Serializable{
    
    private String rootMitAwardNumber;
    private String parentMitAwardNumber;    
    private String accountNumber;
    private int statusCode;
    private int childCount;
    private CoeusVector cvChildren;
    
    /**
     *	Default Constructor
     */
    
    public AwardHierarchyBean(){
    }    
    
    /** Getter for property rootMitAwardNumber.
     * @return Value of property rootMitAwardNumber.
     */
    public java.lang.String getRootMitAwardNumber() {
        return rootMitAwardNumber;
    }
    
    /** Setter for property rootMitAwardNumber.
     * @param rootMitAwardNumber New value of property rootMitAwardNumber.
     */
    public void setRootMitAwardNumber(java.lang.String rootMitAwardNumber) {
        this.rootMitAwardNumber = rootMitAwardNumber;
    }
    
    /** Getter for property parentMitAwardNumber.
     * @return Value of property parentMitAwardNumber.
     */
    public java.lang.String getParentMitAwardNumber() {
        return parentMitAwardNumber;
    }
    
    /** Setter for property parentMitAwardNumber.
     * @param parentMitAwardNumber New value of property parentMitAwardNumber.
     */
    public void setParentMitAwardNumber(java.lang.String parentMitAwardNumber) {
        this.parentMitAwardNumber = parentMitAwardNumber;
    }
    
    /** Getter for property accountNumber.
     * @return Value of property accountNumber.
     */
    public java.lang.String getAccountNumber() {
        return accountNumber;
    }
    
    /** Setter for property accountNumber.
     * @param accountNumber New value of property accountNumber.
     */
    public void setAccountNumber(java.lang.String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    /** Getter for property statusCode.
     * @return Value of property statusCode.
     */
    public int getStatusCode() {
        return statusCode;
    }
    
    /** Setter for property statusCode.
     * @param statusCode New value of property statusCode.
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    
    public boolean equals(Object obj) {
        AwardHierarchyBean awardHierarchyBean = (AwardHierarchyBean)obj;
        if(awardHierarchyBean.getMitAwardNumber().equals(getMitAwardNumber())){
            return true;
        }else{
            return false;
        }
    }    
    
    /** Getter for property childCount.
     * @return Value of property childCount.
     *
     */
    public int getChildCount() {
        return childCount;
    }
    
    /** Setter for property childCount.
     * @param childCount New value of property childCount.
     *
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
    
    /** Getter for property cvChildren.
     * @return Value of property cvChildren.
     *
     */
    public CoeusVector getChildren() {
        return cvChildren;
    }
    
    /** Setter for property cvChildren.
     * @param cvChildren New value of property cvChildren.
     *
     */
    public void setChildren(CoeusVector cvChildren) {
        this.cvChildren = cvChildren;
    }
    
}