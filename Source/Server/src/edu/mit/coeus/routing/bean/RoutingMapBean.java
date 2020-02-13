/*
 * @(#)RoutingMapBean.java 1.0 01/22/08 8:14 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.routing.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;
import java.io.Serializable;

/**
 *
 * @author leenababu
 */
public class RoutingMapBean implements Serializable, BaseBean{
    private String routingNumber;
    private int mapNumber;
    private int mapId;
    //Code modified for Case#3612 - Parallel Routing and Show Routing implementation
//    private int parentMapId;
    private int parentMapNumber;
    
    private String description;
    private boolean systemFlag;
    private String approvalStatus;
    private java.sql.Timestamp updateTimestamp;
    
    private String updateUser;
    private CoeusVector routingMapDetails;
    private String acType;
    
    /** Creates a new instance of RoutingMapBean */
    public RoutingMapBean() {
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
    
    /** Getter for property mapId.
     * @return Value of property mapId.
     */
    public int getMapId() {
        return mapId;
    }
    
    /** Setter for property mapId.
     * @param mapId New value of property mapId.
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    
    /** Getter for property parentMapNumber.
     * @return Value of property parentMapNumber.
     */
    public int getParentMapNumber() {
        return parentMapNumber;
    }
    
    /** Setter for property parentMapNumber.
     * @param parentMapNumber New value of property parentMapNumber.
     */
    public void setParentMapNumber(int parentMapNumber) {
        this.parentMapNumber = parentMapNumber;
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
    
    /** Getter for property systemFlag.
     * @return Value of property systemFlag.
     */
    public boolean getSystemFlag() {
        return systemFlag;
    }
    
    /** Setter for property systemFlag.
     * @param systemFlag New value of property systemFlag.
     */
    public void setSystemFlag(boolean systemFlag) {
        this.systemFlag = systemFlag;
    }
    
    /** Getter for property approvalStatus.
     * @return Value of property approvalStatus.
     */
    public String getApprovalStatus() {
        return approvalStatus;
    }
    
    /** Setter for property proposalNumber.
     * @param approvalStatus New value of property approvalStatus.
     */
    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
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
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public CoeusVector getRoutingMapDetails() {
        return routingMapDetails;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setRoutingMapDetails(CoeusVector routingMapDetails) {
        this.routingMapDetails = routingMapDetails;
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
