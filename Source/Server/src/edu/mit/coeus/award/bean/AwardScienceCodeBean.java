/**
 * @(#)AwardScienceCodeBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
/**
 * This class is used to hold Award Science Code data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardScienceCodeBean extends AwardBaseBean{
    
    private String scienceCode;       
    private String description;
    
    /**
     *	Default Constructor
     */
    
    public AwardScienceCodeBean(){        
    }                  

    /** Getter for property scienceCode.
     * @return Value of property scienceCode.
     */
    public java.lang.String getScienceCode() {
        return scienceCode;
    }
    
    /** Setter for property scienceCode.
     * @param scienceCode New value of property scienceCode.
     */
    public void setScienceCode(java.lang.String scienceCode) {
        this.scienceCode = scienceCode;
    }    
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }    
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwardScienceCodeBean awardScienceCodeBean = 
                    (AwardScienceCodeBean)obj;
            if(awardScienceCodeBean.getScienceCode().equalsIgnoreCase(getScienceCode())){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}