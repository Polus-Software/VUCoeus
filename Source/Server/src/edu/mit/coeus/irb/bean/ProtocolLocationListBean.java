/*
 * @(#)ProtocolLocationListBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;
import edu.mit.coeus.bean.IBaseDataBean;
/**
 * The class used to hold the information of <code>ProtocolLocationListBean</code>
 * 
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 10:41 AM
 */

public class ProtocolLocationListBean implements java.io.Serializable , IBaseDataBean {
    
    private static final String PROP_ROLODEX_ID = "rolodexId";
    private static final String PROP_ORGANIZATION_ID = "organizationId";
    private static final String PROP_ADDRESS = "address";
    private static final String PROP_ORGANIZATION_TYPE_ID = "organizationTypeId";
 
    private String protocolNumber;
    private int sequenceNumber;
    private String organizationId;
    private String organizationName;
    private String address;
    private int rolodexId;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    private int organizationTypeId = 1;
    private String organizationTypeName;
    
    private PropertyChangeSupport propertySupport;
    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-Start
    //holds the canLoadCorrepondents
    private boolean canLoadCorrepondents;
    //Added for COEUSQA-2879_LOG_IACUC and IRB_Correspondents do not auto-populate-End
    
    /** Creates new ProtocolLocationListBean */
    public ProtocolLocationListBean() {
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
     * Method used to get the organizationId
     * @return organizationId String
     */
    public String getOrganizationId() {
        return organizationId;
    }
    /**
     * Method used to set the organizationId
     * @param newOrganizationId String
     */
    public void setOrganizationId(String newOrganizationId) {
        String oldOrganizationId = organizationId;
        this.organizationId = newOrganizationId;
        propertySupport.firePropertyChange(PROP_ORGANIZATION_ID, 
                oldOrganizationId, organizationId);
    }
    
    /**
     * Method used to get the organizationName
     * @return organizationName String
     */
    public String getOrganizationName() {
        return organizationName;
    }
    /**
     * Method used to set the organizationName
     * @param organizationName String
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    /**
     * Method used to get the address
     * @return address String
     */
    public String getAddress(){
        return address;
    }
    /**
     * Method used to set the address
     * @param newAddress String
     */
    public void setAddress(String newAddress){
        String oldAddress = address;
        this.address =newAddress;
        propertySupport.firePropertyChange(PROP_ADDRESS, 
                oldAddress, address);
    }
    /**
     * Method used to get the rolodexId
     * @return rolodexId int
     */
    public int getRolodexId() {
        return rolodexId;
    }
    /**
     * Method used to set the rolodexId
     * @param newRolodexId int
     */
    public void setRolodexId(int newRolodexId) {
        int oldRolodexId = rolodexId;
        this.rolodexId = newRolodexId;
        propertySupport.firePropertyChange(PROP_ROLODEX_ID, 
                oldRolodexId, rolodexId);
        
    }
    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    /**
     * Method used to get the Account Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    /**
     * Method used to set the Account Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    

    /**
     * Method used to get the organizationTypeId
     * @return organizationTypeId String
     */
    public int getOrganizationTypeId() {
        return organizationTypeId;
    }
    /**
     * Method used to set the organizationTypeId
     * @param newOrganizationTypeId String
     */
    public void setOrganizationTypeId(int newOrganizationTypeId) {
        int oldOrganizationTypeId = organizationTypeId;
        this.organizationTypeId = newOrganizationTypeId;
        propertySupport.firePropertyChange(PROP_ORGANIZATION_TYPE_ID, 
                oldOrganizationTypeId, organizationTypeId);
    }
    
    /**
     * Method used to get the organizationTypeName
     * @return organizationTypeName String
     */
    public String getOrganizationTypeName() {
        return organizationTypeName;
    }
    /**
     * Method used to set the organizationTypeName
     * @param organizationTypeName String
     */
    public void setOrganizationTypeName(String organizationTypeName) {
        this.organizationTypeName = organizationTypeName;
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
        strBffr.append("organizationId  =>"+ organizationId);
        strBffr.append("organizationTypeId  =>"+ organizationTypeId);
        strBffr.append("organizationTypeName  =>"+ organizationTypeName);
        strBffr.append("rolodexId =>"+rolodexId);
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
