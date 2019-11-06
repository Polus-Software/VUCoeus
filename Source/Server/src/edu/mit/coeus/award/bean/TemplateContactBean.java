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
import edu.mit.coeus.award.bean.AwardContactDetailsBean;
/**
 * This class is used to hold Award Contact data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class TemplateContactBean extends AwardContactDetailsBean{
    
    private int templateCode;
    
    /**
     *	Default Constructor
     */    
    public TemplateContactBean(){
    }
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            TemplateContactBean templateContactBean = (TemplateContactBean)obj;
            if(templateContactBean.getTemplateCode() == getTemplateCode()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /** Getter for property templateCode.
     * @return Value of property templateCode.
     *
     */
    public int getTemplateCode() {
        return templateCode;
    }
    
    /** Setter for property templateCode.
     * @param templateCode New value of property templateCode.
     *
     */
    public void setTemplateCode(int templateCode) {
        this.templateCode = templateCode;
    }    
}