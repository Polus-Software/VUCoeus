/**
 * @(#)AwardReportTermsBean.java 1.0 June 01, 2005 4:30 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;
import edu.mit.coeus.award.bean.AwardReportTermsBean;

/**
 * This class is used to hold Award Report Terms data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on June 01, 2005 4:30 PM
 */

public class TemplateReportTermsBean extends AwardReportTermsBean{
    
    private int templateCode;
    
    /**
     *	Default Constructor
     */
    public TemplateReportTermsBean(){
    }    

    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            TemplateReportTermsBean templateReportTermsBean = (TemplateReportTermsBean)obj;
            if(templateReportTermsBean.getTemplateCode() == getTemplateCode()){
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