/*
 * @(#)RoutingCommentsBean.java 1.0 01/22/08 8:17 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.BaseBean;
import java.io.Serializable;

/**
 *
 * @author leenababu
 */
public class RoutingCommentsBean implements Serializable, BaseBean{
    private String routingNumber;
    private int mapNumber;
    private int levelNumber;
    private int stopNumber;
    private int approverNumber;
    private int commentNumber;
    private String comments;
    private java.sql.Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    
    /** Creates a new instance of RoutingCommentsBean */
    public RoutingCommentsBean() {
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
    
    /** Getter for property commentId.
     * @return Value of property commentId.
     */
    public int getCommentNumber() {
        return commentNumber;
    }
    
    /** Setter for property commentId.
     * @param commentId New value of property commentId.
     */
    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
    
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
