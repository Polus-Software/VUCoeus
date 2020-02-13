/**
 * @(#)AwardTermsBean.java 1.0 June 03, 2004 12:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;

import edu.mit.coeus.award.bean.AwardTermsBean;

/**
 * This class is used to hold Template Terms data
 * This is a common beans for all Terms in Template
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 03, 2004 12:30 PM
 */

public class TemplateTermsBean extends AwardTermsBean{
    
    private int templateCode;
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private String updateUserName;
    //COEUSQA-1456 : End
    /**
     *	Default Constructor
     */
    
    public TemplateTermsBean(){
    }    
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        TemplateTermsBean templateTermsBean = (TemplateTermsBean)obj;
        if(templateTermsBean.getTemplateCode() == getTemplateCode()){
            return true;
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
    
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    /**
     * Getter for property updateUser.
     * @return updateUser.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //COEUSQA-1456 : End
    
}