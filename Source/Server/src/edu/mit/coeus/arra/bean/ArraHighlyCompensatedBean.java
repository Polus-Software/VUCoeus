/*
 * ArraHighlyCompensatedBean.java
 *
 * Created on September 21, 2009, 2:09 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.arra.bean;

import java.sql.Timestamp;

/**
 *
 * @author keerthyjayaraj
 */
public class ArraHighlyCompensatedBean implements java.io.Serializable{
    
    private String organizationId;
    private int personNumber;
    private String personName;
    private double compensation;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates a new instance of ArraHighlyCompensatedBean */
    public ArraHighlyCompensatedBean() {
    }
    
    public double getCompensation() {
        return compensation;
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public int getPersonNumber() {
        return personNumber;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setCompensation(double compensation) {
        this.compensation = compensation;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public void setPersonNumber(int personNumber) {
        this.personNumber = personNumber;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    
}
