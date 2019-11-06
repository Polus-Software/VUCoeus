/* 
 * @(#)ProposalPersonFormBean.java 1.0 03/14/03 12:29 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;
import edu.mit.coeus.departmental.bean.*;

import java.beans.*;
import java.util.Vector;

/**
 * The class used to hold the information of <code>Proposal Person</code>
 * which extends DepartmentPersonFormBean
 *
 * @author  Raghunath
 * @version 1.0
 * Created on March 14, 2003, 12:29 PM
 */
public class ProposalPersonFormBean extends DepartmentPersonFormBean 
                                            implements java.io.Serializable{
    
    //holds the proposalNumber 
    private String proposalNumber;
    private boolean person;
    private Vector propBiography;
    private Vector personBiography;
    
    //Case #1777 Start 1
    private Integer sortId; 
    //Case #1777 End 1
    //Code added for case#3183 - Proposal Hierarchy enhancement
    private boolean editable;
    
    /* JM 9-2-2015 added status */
    private String status;
    
    /* JM 1-8-2016 added isExternalPerson flag */
    private String isExternalPerson;
    
    /** Creates a new instance of ProposalPersonFormBean */
    public ProposalPersonFormBean() {
    }
    
    public ProposalPersonFormBean ( DepartmentPersonFormBean parentBean ) {
        super( parentBean );
    }
    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /* JM 1-8-2016 added isExternalPerson flag */
    public String getIsExternalPerson() {
    	return isExternalPerson;
    }
    
    public void setIsExternalPerson(String isExternalPerson) {
    	this.isExternalPerson = isExternalPerson;
    }
    /* JM END */
    
    /* JM 9-2-2015 added status */
    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    /* JM END */
    
    /** Getter for property person.
     * @return Value of property person.
     */
    public boolean isPerson() {
        return person;
    }
    
    /** Setter for property person.
     * @param mitPerson New value of property person.
     */
    public void setPerson(boolean person) {
        this.person = person;
    }
    
    /** Getter for property propBiography.
     * @return Value of Proposal Biography.
     */
    public Vector getPropBiography() {
        return propBiography;
    }
    
    /** Setter for property propBiography.
     * @param mitPerson New value of Proposal Biography.
     */
    public void setPropBiography(Vector propBiography) {
        this.propBiography = propBiography;
    }    
    
    /** Getter for property personBiography.
     * @return Value of property personBiography.
     */
    public java.util.Vector getPersonBiography() {
        return personBiography;
    }
    
    /** Setter for property personBiography.
     * @param personBiography New value of property personBiography.
     */
    public void setPersonBiography(java.util.Vector personBiography) {
        this.personBiography = personBiography;
    }
    
    //Case #1777 Start 2
    /**
     * Getter for property sortId.
     * @return Value of property sortId.
     */
    public java.lang.Integer getSortId() {
        return sortId;
    }
    
    /**
     * Setter for property sortId.
     * @param sortId New value of property sortId.
     */
    public void setSortId(java.lang.Integer sortId) {
        this.sortId = sortId;
    }
    //Case #1777 End 2

    /**
     * Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }
    
}
