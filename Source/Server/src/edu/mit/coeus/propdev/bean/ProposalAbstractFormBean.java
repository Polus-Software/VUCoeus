/*
 * @(#)ProposalAbstractFormBean.java 1.0 05/14/03 4:38 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;

/** The class is used to hold the information of <code>Proposal Abstracts</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 14, 2003, 4:33 PM
 */

public class ProposalAbstractFormBean implements java.io.Serializable {
    
     private static final String ABSTRACT_TEXT = "abstractText";

	//holds the proposal number
     private String proposalNumber;
     //holds the abstract type id
     private int abstractTypeCode;
     //holds the aw_Abstract type id
     private int aw_AbstractTypeCode;     
     //holds the abstract
     private String abstractText;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;
     
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalAbstractFormBean */
    public ProposalAbstractFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public int getAbstractTypeCode() {
        return abstractTypeCode;
    }

    public void setAbstractTypeCode(int abstractTypeCode) {
        this.abstractTypeCode = abstractTypeCode;
    }

    public int getAwAbstractTypeCode() {
        return aw_AbstractTypeCode;
    }

    public void setAwAbstractTypeCode(int aw_AbstractTypeCode) {
        this.aw_AbstractTypeCode = aw_AbstractTypeCode;
    }    
    
    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String newAbstractText) {
        String oldAbstractText = abstractText;
        this.abstractText = newAbstractText;
        propertySupport.firePropertyChange(ABSTRACT_TEXT,oldAbstractText, abstractText);
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

	public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
