/*
 * @(#)ProtocolInvestigatorUnitsBean.java October 24, 2002, 12:33 PM 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>Protocol InfoBean</code>
 *
 * 
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:33 PM 
 */

public class ProtocolInvestigatorUnitsBean implements java.io.Serializable, IBaseDataBean {
    // holds the unit number
    private static final String PROP_UNIT_NUMBER = "unitNumber";
    // holds the lead unit flag
    private static final String PROP_LEAD_UNIT_FLAG = "leadUnitFlag";
    // holds the protocol number
    private String protocolNumber;
    // holds the sequence number
    private int sequenceNumber;
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
    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
    //holds the canLoadCorrepondents
    private boolean canLoadCorrepondents;
    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProtocolInvestigatorUnitsBean */
    public ProtocolInvestigatorUnitsBean() {
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
    
    /**
     * To print the bean data in the server side 
     * 
     * @return String 
     */
     public String toString(){
        StringBuffer strBffr = new StringBuffer();
        strBffr.append("{ Protocol Number =>"+protocolNumber);
        strBffr.append("sequenceNumber =>"+sequenceNumber);
        strBffr.append("personId  =>"+ personId);
        strBffr.append("personName =>"+personName);
        strBffr.append("unitNumber  =>"+unitNumber );
        strBffr.append("unitName   =>"+unitName);
        strBffr.append("leadUnitFlag   =>"+leadUnitFlag);
        strBffr.append("updateUser =>"+updateUser);
        strBffr.append("updateTimestamp =>"+updateTimestamp);
        strBffr.append("acType =>"+acType);
    
        return strBffr.toString();
    }

    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
    /**
     * Method used to get can be added default correspondence
     * @return canLoadCorrepondents Boolean
     */
    public boolean isCanLoadCorrepondents() {
        return canLoadCorrepondents;
    }

    /**
     * Method used to set can be added default correspondence
     * @param canLoadCorrepondents Boolean
     */
    public void setCanLoadCorrepondents(boolean canLoadCorrepondents) {
        this.canLoadCorrepondents = canLoadCorrepondents;
    }
    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End

}
