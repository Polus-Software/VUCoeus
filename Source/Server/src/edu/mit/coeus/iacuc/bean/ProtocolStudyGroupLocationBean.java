/*
 * ProtocolStudyGroupLocationBean.java
 *
 * Created on March 18, 2010, 5:44 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved
 */

package edu.mit.coeus.iacuc.bean;

import edu.mit.coeus.bean.IBaseDataBean;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author satheeshkumarkn
 */
public class ProtocolStudyGroupLocationBean implements java.io.Serializable, IBaseDataBean{
    
    private String protocolNumber;
    private int sequenceNumber;
    private int studyGroupId;
    private int studyGroupLocationId;
    private int locationTypeCode;
    private String locationTypeDesc;
    private int locationId;
    private String locationName;
    private String studyGroupLocationName;
    private java.sql.Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    private PropertyChangeSupport propertySupport;
    private static final String LOCATION_TYPE_CODE = "locationTypeCode";
    private static final String LOCATION_ID = "locationId";
    private static final String LOCATION_DESCRIPTION = "studyGroupLocationName";
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol _start
    private String locationRoom;
    private static final String LOCATION_ROOM = "locationRoom";
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
    
    /** Creates a new instance of ProtocolStudyGroupLocationBean */
    public ProtocolStudyGroupLocationBean() {
           propertySupport = new PropertyChangeSupport(this);
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
     * Getter method for protocol number
     * @return protocolNumber - String
     */
    public String getProtocolNumber() {
        return protocolNumber;
    }

    /**
     * Setter method for protocol number
     * @param protocoNumber - String
     */
    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }
    
    /**
     * Getter method for protocol sequence number
     * @return sequenceNumber - int
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Setter method for protocol sequence number
     * @param sequenceNumber - int
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * Getter method for study group id
     * @return studyGroupId - int
     */
    public int getStudyGroupId() {
        return studyGroupId;
    }

    /**
     * Setter method for study group id
     * param studyGroupId - int
     */
    public void setStudyGroupId(int studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    /**
     * Getter method for study group location id
     * @return studyGroupLocationId  - int
     */
    public int getStudyGroupLocationId() {
        return studyGroupLocationId;
    }

    /**
     * Setter method for study group location id
     * @param studyGroupLocationId  - int
     */
    public void setStudyGroupLocationId(int studyGroupLocationId) {
        this.studyGroupLocationId = studyGroupLocationId;
    }

    /**
     * Getter method for location type code
     * @return locationTypeCode - int
     */
    public int getLocationTypeCode() {
        return locationTypeCode;
    }

    /**
     * Setter method for location type code
     * @param locationTypeCode - int
     */
    public void setLocationTypeCode(int newLocationTypeCode) {
        int oldLocationTypeCode = locationTypeCode;
        this.locationTypeCode = newLocationTypeCode;
        propertySupport.firePropertyChange(LOCATION_TYPE_CODE,oldLocationTypeCode, locationTypeCode);
    }

    /**
     * Getter method for location id
     * @return locationId - int
     */
    public int getLocationId() {
        return locationId;
    }

    /**
     * Setter method for location id
     * @param locationId - int
     */
    public void setLocationId(int newLocationId) {
        int oldLocationId = locationId;
        this.locationId = newLocationId;
        propertySupport.firePropertyChange(LOCATION_ID,oldLocationId, locationId);
    }

    /**
     * Getter method for study group location name
     * @return studyGroupLocationName - String
     */
    public String getStudyGroupLocationName() {
        return studyGroupLocationName;
    }

    /**
     * Setter method for study group location anme
     * @param studyGroupLocationName - String
     */
    public void setStudyGroupLocationName(String newStudyGroupLocationName) {
        String oldStudyGroupLocationName = studyGroupLocationName;
        this.studyGroupLocationName = newStudyGroupLocationName;
        propertySupport.firePropertyChange(LOCATION_DESCRIPTION,oldStudyGroupLocationName, studyGroupLocationName);
    }

    /**
     * Getter method for update time stamp 
     * @return updateTimestamp - java.sql.Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Setter method for update timestamp
     * @param updateTimestamp 
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    /**
     * 
     * Getter method for update user id
     * @return updateUser
     */
    public String getUpdateUser() {
        return updateUser;
    }

    /**
     * 
     * Setter method for update user id
     * @param updateUser 
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 
     * Getter method for location anme
     * @return locationName
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * 
     * Setter method for location name
     * @param locationName 
     */
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * 
     * Getter method for location type description
     * @return locationTypeDesc
     */
    public String getLocationTypeDesc() {
        return locationTypeDesc;
    }

    /**
     * 
     * Setter method for location type description
     * @param locationTypeDesc 
     */
    public void setLocationTypeDesc(String locationTypeDesc) {
        this.locationTypeDesc = locationTypeDesc;
    }
    
    /*
     * Method to get the actype of the bean
     * @return acType - String
     */
    public String getAcType() {
        return acType;
    }
    
    /*
     * Method to set acType For the bean
     * @param acType - String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_start
    /**
     * Getter method for location room
     * @return locationRoom - String
     */
    public String getLocationRoom() {
        return locationRoom;
    }
    
    /**
     * Setter method for location room
     * @param newLocationRoom - String
     */
    public void setLocationRoom(String newLocationRoom) {
        String oldLocationRoom = locationRoom;
        this.locationRoom = newLocationRoom;
        propertySupport.firePropertyChange(LOCATION_ROOM,oldLocationRoom, locationRoom);
    }
    // Added for COEUSQA-2625_Add another level of detail for Location in IACUC protocol_end
    
}
