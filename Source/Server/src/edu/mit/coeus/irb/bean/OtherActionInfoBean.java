/*
 * @(#)OtherActionInfoBean.java 1.0 11/18/02 3:34 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.io.Serializable;

/**
 * The class used to hold the information of <code>Other Actions</code>
 * The data will get attached with the <code>Schedule OtherActionInfoBean</code> 
 * form.
 *
 * Created on November 18, 2002, 3:34 PM
 * @author  Mukundan C
 * @version 1.0
 */

public class OtherActionInfoBean implements Serializable {
    // holds the actions type code
    private static final String PROP_ACTION_TYPE_CODE = "scheduleActTypeCode";
    // holds the item descriptions
    private static final String PROP_ITEM_DESCRIPTION = "itemDescription";
    // holds the schedule id
    private String scheduleId;
    // holds the action item number
    private int actionItemNumber;
    // holds the schedule action type code
    private int scheduleActTypeCode;
    // holds the schedule action type descriptions
    private String scheduleActTypeDesc;
    // holds the item description
    private String itemDescription;
     //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user id
    private String updateUser;
    //holds account type
    private String acType;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of OtherActionInfoBean */
    public OtherActionInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
    }

    /**
     * Method used to get the schedule id
     * @return scheduleId String
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Method used to set the schedule id
     * @param scheduleId String
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * Method used to get the action item number
     * @return actionItemNumber int
     */
    public int getActionItemNumber() {
        return actionItemNumber;
    }

    /**
     * Method used to set the action item number
     * @param actionItemNumber int
     */
    public void setActionItemNumber(int actionItemNumber) {
        this.actionItemNumber = actionItemNumber;
    }

    /**
     * Method used to get the schedule action type code
     * @return scheduleActTypeCode int
     */
    public int getScheduleActTypeCode() {
        return scheduleActTypeCode;
    }

    /**
     * Method used to set the schedule action type code
     * @param newScheduleActTypeCode int
     */
    public void setScheduleActTypeCode(int newScheduleActTypeCode){
        int oldScheduleActTypeCode = scheduleActTypeCode;
        this.scheduleActTypeCode = newScheduleActTypeCode;
        propertySupport.firePropertyChange(PROP_ACTION_TYPE_CODE, 
                oldScheduleActTypeCode, scheduleActTypeCode);
    }

    /**
     * Method used to get the schedule action type description
     * @return scheduleActTypeDesc String
     */
    public String getScheduleActTypeDesc() {
        return scheduleActTypeDesc;
    }

    /**
     * Method used to set the schedule action type description
     * @param scheduleActTypeDesc String
     */
    public void setScheduleActTypeDesc(String scheduleActTypeDesc) {
        this.scheduleActTypeDesc = scheduleActTypeDesc;
    }

    /**
     * Method used to get the item descriptions
     * @return itemDescription String
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Method used to set the item descriptions
     * @param newItemDescription String
     */
    public void setItemDescription(String newItemDescription){
        String oldItemDescription = itemDescription;
        this.itemDescription = newItemDescription;
        propertySupport.firePropertyChange(PROP_ITEM_DESCRIPTION, 
                oldItemDescription, itemDescription);
    }
   
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
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
