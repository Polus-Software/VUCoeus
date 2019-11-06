/*
 * @(#)LocationBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Location Detail</code>
 * The data will get attached with the <code>ProtocolMaintenanceForm</code>.
 *
 * 
 * @author  ravikanth
 * @version 1.0
 * Created on October 24, 2002, 07:17 PM
 */

public class LocationBean implements java.io.Serializable {
    //holds location id
    private String locationId;
    //holds location
    private String location;
    //holds address
    private String address;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new LocationBean */
    public void LocationBean() {
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
     * Method used to get the location
     * @return location String
     */
    public String getLocation() {
        return location;
    }

    /**
     * Method used to set the location
     * @param location String
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Method used to get the location id
     * @return locationId String
     */
    public String getLocationId() {
        return locationId;
    }

    /**
     * Method used to set the location id
     * @param locationId String
     */
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
    
    /**
     * Method used to set the address
     * @param address String
     */
    public void setAddress(String address){
        this.address = address;
    }
    
    /**
     * Method used to get the address
     * @return address String
     */
    public String getAddress(){
        return address;
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

}
