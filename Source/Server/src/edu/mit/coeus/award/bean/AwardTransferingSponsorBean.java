/**
 * @(#)AwardTransferingSponsorBean.java 1.0 06/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;

/**
 * This class is used to hold Award Transfering Sponsor data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 06, 2004 12:30 PM
 */

public class AwardTransferingSponsorBean extends AwardBaseBean{
    
    private String sponsorCode;
    private String sponsorName;
    private int rowId;
    private String aw_SponsorCode;
    
    /**
     *	Default Constructor
     */
    
    public AwardTransferingSponsorBean(){        
    }                  

    /** Getter for property sponsorCode.
     * @return Value of property sponsorCode.
     */
    public java.lang.String getSponsorCode() {
        return sponsorCode;
    }
    
    /** Setter for property sponsorCode.
     * @param sponsorCode New value of property sponsorCode.
     */
    public void setSponsorCode(java.lang.String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }        
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardTransferingSponsorBean awardTransferingSponsorBean = 
                    (AwardTransferingSponsorBean)obj;
            if(awardTransferingSponsorBean.getRowId()==getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }    
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     *
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     *
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
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
    
    /** Getter for property aw_SponsorCode.
     * @return Value of property aw_SponsorCode.
     *
     */
    public java.lang.String getAw_SponsorCode() {
        return aw_SponsorCode;
    }
    
    /** Setter for property aw_SponsorCode.
     * @param aw_SponsorCode New value of property aw_SponsorCode.
     *
     */
    public void setAw_SponsorCode(java.lang.String aw_SponsorCode) {
        this.aw_SponsorCode = aw_SponsorCode;
    }
    
}