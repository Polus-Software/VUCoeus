/*
 * MultiCampusUserInfoBean.java
 *
 * Created on January 26, 2007, 11:53 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.user.auth.bean;

import java.sql.Timestamp;

/**
 *
 * @author geot
 */
public class MultiCampusUserInfoBean {
    private String multiCampusPersonId;
    private String userId;
    private String campusCode;
    private String campusPersonId;
    private String campusUserId;
    private Timestamp updateTimestamp;
    private String updateUser;
    /** Creates a new instance of MultiCampusUserInfoBean */
    public MultiCampusUserInfoBean() {
    }

    public String getMultiCampusPersonId() {
        return multiCampusPersonId;
    }

    public void setMultiCampusPersonId(String multiCampusPersonId) {
        this.multiCampusPersonId = multiCampusPersonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public void setCampusCode(String campusCode) {
        this.campusCode = campusCode;
    }

    public String getCampusPersonId() {
        return campusPersonId;
    }

    public void setCampusPersonId(String campusPersonId) {
        this.campusPersonId = campusPersonId;
    }

    public String getCampusUserId() {
        return campusUserId;
    }

    public void setCampusUserId(String campusUserId) {
        this.campusUserId = campusUserId;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
}
