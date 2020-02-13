/*
 * @(#)AbstractTypeFormBean.java 1.0 05/14/03 4:38 AM
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
 * Created on March 11, 2003, 9:33 AM
 */

public class AbstractTypeFormBean implements java.io.Serializable {

     //holds the abstract type code Id
     private int abstractTypeCode;
     //holds the abstract
     private String description;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;

    private PropertyChangeSupport propertySupport;

    /** Creates new ProposalAbstractFormBean */
    public AbstractTypeFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    public int getAbstractTypeId() {
        return abstractTypeCode;
    }

    public void setAbstractTypeId(int abstractTypeCode) {
        this.abstractTypeCode = abstractTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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