/*
 * @(#)InstituteProposalScienceCodeBean.java 1.0 April 29, 2004, 6:55 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.instprop.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/** This class is used to hold Comments of a Institute Proposal
 *
 * @version :1.0 April 29, 2004, 6:55 AM
 * @author Prasanna Kumar K
 */

public class InstituteProposalScienceCodeBean extends InstituteProposalBaseBean{
    
    private String scienceCode;    
    private String scienceDescription;
    
    public InstituteProposalScienceCodeBean(){
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
    
    /** Getter for property scienceDescription.
     * @return Value of property scienceDescription.
     */
    public java.lang.String getScienceDescription() {
        return scienceDescription;
    }
    
    /** Setter for property scienceDescription.
     * @param scienceDescription New value of property scienceDescription.
     */
    public void setScienceDescription(java.lang.String scienceDescription) {
        this.scienceDescription = scienceDescription;
    }
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        InstituteProposalScienceCodeBean instituteProposalScienceCodeBean = (InstituteProposalScienceCodeBean)obj;
        if(instituteProposalScienceCodeBean.getProposalNumber().equals(getProposalNumber()) &&
            instituteProposalScienceCodeBean.getSequenceNumber() == getSequenceNumber() &&        
            instituteProposalScienceCodeBean.getScienceCode().equalsIgnoreCase(getScienceCode())){
                return true;
       }else{
            return false;
        }
    }    
} // end