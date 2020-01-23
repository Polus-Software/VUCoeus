/*
 * @(#)PersonEditableColumnsFormBean.java 1.0 05/20/03 7:16 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import java.sql.Timestamp;

/** The class is used to hold the information of <code>Person Editable Columns</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on May 20, 2003, 7:16 PM
 */

public class PersonEditableColumnsFormBean implements java.io.Serializable {

     //holds the column name
     private String columnName;
    //holds update user id
     private String updateUser;
    //holds update timestamp
     private Timestamp updateTimestamp;
    //holds account type
     private String acType;

    private PropertyChangeSupport propertySupport;

    /** Creates new PersonEditableColumnsFormBean */
    public PersonEditableColumnsFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /** This method fetches Column Name
     * @return  String columnName*/    
    public String getColumnName() {
        return columnName;
    }

    /** This method sets Column Name
     * @param columnName  String */    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /** This method fetches Update User
     * @return  String updateUser */    
    public String getUpdateUser() {
        return updateUser;
    }

    /** This method sets Update User
     * @param updateUser String  */    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /** This method fetches Update Timestamp
     * @return  Timestamp updateTimestamp*/    
	public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

        /** This method sets Update Timestamp
         * @param updateTimestamp  Timestamp*/        
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /** This method fetches AcType
     * @return  String acType*/    
    public String getAcType() {
        return acType;
    }

    /** This method sets AcType
     * @param acType  String */    
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