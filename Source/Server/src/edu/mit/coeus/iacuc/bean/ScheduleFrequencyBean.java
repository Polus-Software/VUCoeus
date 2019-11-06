/*
 * @(#)ScheduleFrequencyBean.java 1.0 10/19/02 10:30 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.iacuc.bean;

/**
 * The class used to hold the information of <code>Schedule frequency</code>
 *
 * @author  phani
 * @version 1.0
 * Created on October 24, 2002, 12:27 PM
 */

public class ScheduleFrequencyBean implements java.io.Serializable {
    
    private int frequencyCode;
    
    private String frequencyDesc;
    
    private int noOfDays;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of ScheduleFrequencyBean */
    public ScheduleFrequencyBean() {
    }
    
    /**
     * Method used to get the frequency code
     * @return frequencyCode integer
     */
    public int getFrequencyCode() {
        return frequencyCode;
    }
    
     /**
     * Method used to set the frequency code
     * @param frequencyCode integer
     */
    public void setFrequencyCode(int frequencyCode) {
        this.frequencyCode = frequencyCode;
    }
    
    /**
     * Method used to get the frequency description
     * @return frequencyDesc String
     */
    public String getFrequencyDesc() {
        return frequencyDesc;
    }
    
    /**
     * Method used to set the frequency description
     * @param frequencyDesc String
     */
    public void setFrequencyDesc(java.lang.String frequencyDesc) {
        this.frequencyDesc = frequencyDesc;
    }
    
    /**
     * Method used to get the number of days
     * @return noOfDays integer
     */
    public int getNoOfDays(){
        return noOfDays;
    }
    
    /**
     * Method used to set the number of days
     * @param noOfDays integer
     */
    public void setNoOfDays(int noOfDays){
        this.noOfDays = noOfDays;
        
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
    
}
