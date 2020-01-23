/*
 * @(#)RoutingAttachmentBean.java 1.0 01/22/08 8:15 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.bean.CoeusAttachmentBean;
import java.io.Serializable;

/**
 *
 * @author leenababu
 */
public class RoutingAttachmentBean extends CoeusAttachmentBean implements Serializable, BaseBean{
    private String routingNumber;
    private int mapNumber;
    private int levelNumber;
    private int stopNumber;
    
    private int approverNumber;
    private int attachmentNumber;
    private String description;
    //Commented with case 4007:Icon based on mime Type
//    private String fileName;
//    private byte[] attachment;
    private java.sql.Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    /** Creates a new instance of RoutingAttachmentBean */
    public RoutingAttachmentBean() {
    }
    
    /** Getter for property routingNumber.
     * @return Value of property routingNumber.
     */
    public String getRoutingNumber() {
        return routingNumber;
    }
    
    /** Setter for property routingNumber.
     * @param routingNumber New value of property routingNumber.
     */
    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }
    
    /** Getter for property mapNumber.
     * @return Value of property mapNumber.
     */
    public int getMapNumber() {
        return mapNumber;
    }
    
    /** Setter for property mapNumber.
     * @param mapNumber New value of property mapNumber.
     */
    public void setMapNumber(int mapNumber) {
        this.mapNumber = mapNumber;
    }
    
    /** Getter for property levelNumber.
     * @return Value of property levelNumber.
     */
    public int getLevelNumber() {
        return levelNumber;
    }
    
    /** Setter for property levelNumber.
     * @param levelNumber New value of property levelNumber.
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }
    
    /** Getter for property stopNumber.
     * @return Value of property stopNumber.
     */
    public int getStopNumber() {
        return stopNumber;
    }
    
    /** Setter for property stopNumber.
     * @param stopNumber New value of property stopNumber.
     */
    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }
    
    /** Getter for property approverNumber.
     * @return Value of property approverNumber.
     */
    public int getApproverNumber() {
        return approverNumber;
    }
    
    /** Setter for property approverNumber.
     * @param approverNumber New value of property approverNumber.
     */
    public void setApproverNumber(int approverNumber) {
        this.approverNumber = approverNumber;
    }
    
    /** Getter for property attachmentId.
     * @return Value of property attachmentId.
     */
    public int getAttachmentNumber() {
        return attachmentNumber;
    }
    
    /** Setter for property attachmentId.
     * @param attachmentNumber New value of property attachmentNumber.
     */
    public void setAttachmentNumber(int attachmentNumber) {
        this.attachmentNumber = attachmentNumber;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     */
    public String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
//    public String getFileName() {
//        return fileName;
//    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public String getUpdateUser() {
        return updateUser;
    }
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public String getAcType() {
        return acType;
    }
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
}
