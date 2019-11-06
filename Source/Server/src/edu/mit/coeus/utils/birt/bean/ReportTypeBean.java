/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.birt.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author sharathk
 */
public class ReportTypeBean implements Serializable{

    private int typeCode;
    private String typeDescription;
    private String moduleBaseWindow;
    private String userId;
    private Timestamp timestamp;

    /**
     * @return the typeCode
     */
    public int getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode the typeCode to set
     */
    public void setTypeCode(int reportTypeCode) {
        this.typeCode = reportTypeCode;
    }

    /**
     * @return the typeDescription
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * @param typeDescription the typeDescription to set
     */
    public void setTypeDescription(String trportTypeDesc) {
        this.typeDescription = trportTypeDesc;
    }

    /**
     * @return the moduleBaseWindow
     */
    public String getModuleBaseWindow() {
        return moduleBaseWindow;
    }

    /**
     * @param moduleBaseWindow the moduleBaseWindow to set
     */
    public void setModuleBaseWindow(String moduleBaseWindow) {
        this.moduleBaseWindow = moduleBaseWindow;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
}
