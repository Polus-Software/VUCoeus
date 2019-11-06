/* 
 * @(#)ProposalLeadUnitFormBean.java 1.0 03/14/03 11:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Proposal Lead Unit</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 14, 2003, 11:41 AM
 */

public class ProposalLeadUnitFormBean implements java.io.Serializable {
     // holds the unit number
    private static final String PROP_UNIT_NUMBER = "unitNumber";
    // holds the lead unit flag
    private static final String PROP_LEAD_UNIT_FLAG = "leadUnitFlag";
    // holds the proposal number
    private String proposalNumber;
    // holds the unit number
    private String unitNumber;
    // holds the unit name
    private String unitName;
    // holds the lead unit flag
    private boolean leadUnitFlag;
    // holds the person id
    private String personId;
    // holds the person name
    private String personName;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolInvestigatorUnitsBean */
    public ProposalLeadUnitFormBean() {
        propertySupport = new PropertyChangeSupport( this );
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

    public String getProposalNumber() {
        return proposalNumber;
    }

    /**
     * Method used to set the proposal number
     * @param proposalNumber String
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    
    /**
     * Method used to get the unit Number
     * @return unitNumber String
     */
    public String getUnitNumber() {
        return unitNumber;
    }

    /**
     * Method used to set the unit Number
     * @param newUnitNumber String
     */
    public void setUnitNumber(String newUnitNumber) {
        String oldUnitNumber = unitNumber;
        this.unitNumber  = newUnitNumber;
        propertySupport.firePropertyChange(PROP_UNIT_NUMBER, 
                oldUnitNumber, unitNumber);
    }

    /**
     * Method used to get the unit name
     * @return unitName String
     */
    public String getUnitName() {
        return unitName;
    }

    /**
     * Method used to set the unit name
     * @param unitName String
     */
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    /**
     * Method used to get the lead Unit Flag
     * @return leadUnitFlag boolean
     */
    public boolean isLeadUnitFlag() {
        return leadUnitFlag;
    }

    /**
     * Method used to set the lead Unit Flag
     * @param newLeadUnitFlag boolean
     */
    public void setLeadUnitFlag(boolean newLeadUnitFlag) {
        boolean oldLeadUnitFlag  = leadUnitFlag;
        this.leadUnitFlag  = newLeadUnitFlag;
        propertySupport.firePropertyChange(PROP_LEAD_UNIT_FLAG, 
                oldLeadUnitFlag, leadUnitFlag);
    }

    /**
     * Method used to get the person id
     * @return personId String
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Method used to set the person id
     * @param personId String
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    /**
     * Method used to get the person Name 
     * @return personName String
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * Method used to set the person Name 
     * @param personName String
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser(){
        return updateUser;
    }
    
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
}
