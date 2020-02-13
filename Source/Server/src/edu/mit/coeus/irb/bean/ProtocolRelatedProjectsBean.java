/*
 * @(#)ProtocolRelatedProjectsBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.ReferencesBean;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol Related Projects</code>
 *
 * @author Prasanna Kumar
 * @version 1.0
 * Created on August 19, 2003, 3:08 PM
 */

public class ProtocolRelatedProjectsBean implements java.io.Serializable, IBaseDataBean {

    // holds the Module Code
    private static final String MODULE_CODE = "moduleCode";
    // holds the Module Desc
    private static final String MODULE_DESCRIPTION = "moduleDesc";    
    // holds the Project Number
    private static final String PROJECT_NUMBER = "projectNumber";        
    
    //holds protocol number
    private  String protocolNumber;
    //holds sequence number
    private int sequenceNumber;
    //holds Module code
    private int moduleCode;    
    //holds Module Description
    private String description;        
    //holds Project Number
    private String projectNumber;            
    //holds Update user
    private String updateUser;
    //holds update Timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;    
    //holds Aw Module code
    private int awModuleCode;        
    //holds Aw Project Number
    private String awProjectNumber;            
    //holds Title
    private String title;

    private PropertyChangeSupport propertySupport;
   
    /** Creates new ProtocolRelatedProjectsBean */
    public ProtocolRelatedProjectsBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Method used to get the protocol number
     * @return protocolNumber String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Method used to set the protocol number
     * @param protocolNumber String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Method used to get the sequence number
     * @return sequenceNumber int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Method used to set the sequence number
     * @param sequenceNumber int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /** Getter for property moduleCode.
     * @return Value of property moduleCode.
     */
    public int getModuleCode() {
        return moduleCode;
    }
    
    /** Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     */
    public void setModuleCode(int newModuleCode) {
        int oldModuleCode = moduleCode;
        this.moduleCode = newModuleCode;
        propertySupport.firePropertyChange(MODULE_CODE, 
                oldModuleCode, moduleCode);        
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String newDescription) {
        String oldDescription = description;
        this.description = newDescription;
        propertySupport.firePropertyChange(MODULE_DESCRIPTION, 
                oldDescription, description);                
    }
    
    /** Getter for property projectNumber.
     * @return Value of property projectNumber.
     */
    public java.lang.String getProjectNumber() {
        return projectNumber;
    }
    
    /** Setter for property projectNumber.
     * @param projectNumber New value of property projectNumber.
     */
    public void setProjectNumber(java.lang.String newProjectNumber) {
        String oldProjectNumber = projectNumber;
        this.projectNumber = newProjectNumber;
        propertySupport.firePropertyChange(PROJECT_NUMBER, 
                oldProjectNumber, projectNumber);
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType() {
        return acType;
    }

    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }    
    
    /** Getter for property awModuleCode.
     * @return Value of property awModuleCode.
     */
    public int getAwModuleCode() {
        return awModuleCode;
    }
    
    /** Setter for property awModuleCode.
     * @param awModuleCode New value of property awModuleCode.
     */
    public void setAwModuleCode(int awModuleCode) {
        this.awModuleCode = awModuleCode;
    }
    
    /** Getter for property awProjectNumber.
     * @return Value of property awProjectNumber.
     */
    public java.lang.String getAwProjectNumber() {
        return awProjectNumber;
    }
    
    /** Setter for property awProjectNumber.
     * @param awProjectNumber New value of property awProjectNumber.
     */
    public void setAwProjectNumber(java.lang.String awProjectNumber) {
        this.awProjectNumber = awProjectNumber;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
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
