/*
 * ArraAwardBaseBean.java
 *
 * Created on September 21, 2009, 2:24 PM
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
public class ArraAwardBaseBean implements java.io.Serializable{
    
    private int arraReportNumber;
    private int versionNumber;
    private String mitAwardNumber;
    private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
    private String acType = null;
    
    /** Creates a new instance of ArraAwardBaseBean */
    public ArraAwardBaseBean() {
    }
    
    public String getAcType() {
        return acType;
    }
    
    public int getArraReportNumber() {
        return arraReportNumber;
    }
    
    public String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public int getVersionNumber() {
        return versionNumber;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public void setArraReportNumber(int arraReportNumber) {
        this.arraReportNumber = arraReportNumber;
    }
    
    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
}
