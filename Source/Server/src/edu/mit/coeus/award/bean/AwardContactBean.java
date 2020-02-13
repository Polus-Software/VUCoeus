/**
 * @(#)AwardContactBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Contact data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardContactBean extends AwardBaseBean{
    
    private int contactTypeCode;
    private int rolodexId;
    private int aw_ContactTypeCode;
    private int aw_RolodexId;
    private String contactTypeDescription;
    private int rowId;
    
    /**
     *	Default Constructor
     */
    
    public AwardContactBean(){
    }       

    /** Getter for property contactTypeCode.
     * @return Value of property contactTypeCode.
     */
    public int getContactTypeCode() {
        return contactTypeCode;
    }
    
    /** Setter for property contactTypeCode.
     * @param contactTypeCode New value of property contactTypeCode.
     */
    public void setContactTypeCode(int contactTypeCode) {
        this.contactTypeCode = contactTypeCode;
    }
    
    /** Getter for property rolodexId.
     * @return Value of property rolodexId.
     */
    public int getRolodexId() {
        return rolodexId;
    }
    
    /** Setter for property rolodexId.
     * @param rolodexId New value of property rolodexId.
     */
    public void setRolodexId(int rolodexId) {
        this.rolodexId = rolodexId;
    }    
    
    /** Getter for property aw_ContactTypeCode.
     * @return Value of property aw_ContactTypeCode.
     */
    public int getAw_ContactTypeCode() {
        return aw_ContactTypeCode;
    }
    
    /** Setter for property aw_ContactTypeCode.
     * @param aw_ContactTypeCode New value of property aw_ContactTypeCode.
     */
    public void setAw_ContactTypeCode(int aw_ContactTypeCode) {
        this.aw_ContactTypeCode = aw_ContactTypeCode;
    }
    
    /** Getter for property aw_RolodexId.
     * @return Value of property aw_RolodexId.
     */
    public int getAw_RolodexId() {
        return aw_RolodexId;
    }
    
    /** Setter for property aw_RolodexId.
     * @param aw_RolodexId New value of property aw_RolodexId.
     */
    public void setAw_RolodexId(int aw_RolodexId) {
        this.aw_RolodexId = aw_RolodexId;
    }
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardContactBean awardContactBean = (AwardContactBean)obj;
            if(awardContactBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /** Getter for property contactTypeDescription.
     * @return Value of property contactTypeDescription.
     *
     */
    public java.lang.String getContactTypeDescription() {
        return contactTypeDescription;
    }
    
    /** Setter for property contactTypeDescription.
     * @param contactTypeDescription New value of property contactTypeDescription.
     *
     */
    public void setContactTypeDescription(java.lang.String contactTypeDescription) {
        this.contactTypeDescription = contactTypeDescription;
    }    
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
}