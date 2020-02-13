/* 
 * @(#)AwardUnitBean.java 1.0 01/05/04 11:41 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;
import java.util.Map;

/**
 * The class used to hold the information of <code>Investigator Units</code>
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 05, 2004, 11:41 AM
 */

public class InvestigatorUnitBean extends SyncInfoBean implements java.io.Serializable, CoeusBean{
    // holds the unit number
    private String unitNumber;
    // holds the unit number
    private String aw_UnitNumber;    
    // holds the unit name
    private String unitName;
    // holds the lead unit flag
    private boolean leadUnitFlag;
    // holds the person id
    private String personId;
    // holds the aw person id
    private String aw_PersonId;    
    // holds the person name
    private String personName;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    //holds OSP Administrator Name
    private String ospAdministratorName;
    
    /** Creates new AwardUnitBean */
    public InvestigatorUnitBean() {
    }    
    
    /** Creates new AwardUnitBean */
    public InvestigatorUnitBean(InvestigatorUnitBean investigatorUnitBean) {
        setUnitNumber(investigatorUnitBean.getUnitNumber());
        setAw_UnitNumber(investigatorUnitBean.getAw_UnitNumber());
        setUnitName(investigatorUnitBean.getUnitName());
        setLeadUnitFlag(investigatorUnitBean.isLeadUnitFlag());
        setPersonId(investigatorUnitBean.getPersonId());
        setAw_PersonId(investigatorUnitBean.getAw_PersonId());
        setPersonName(investigatorUnitBean.getPersonName());
        setUpdateUser(investigatorUnitBean.getUpdateUser());
        setUpdateTimestamp(investigatorUnitBean.getUpdateTimestamp());
        setAcType(investigatorUnitBean.getAcType());
        setOspAdministratorName(investigatorUnitBean.getOspAdministratorName());
        //Case 2796: Sync To Parent
        setSyncRequired(investigatorUnitBean.isSyncRequired());
        setSyncTarget(investigatorUnitBean.getSyncTarget());
        setParameter((Map)investigatorUnitBean.getParameter());
        //2796 End
    }        
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
        return unitName;
    }
    
    /** Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
        this.unitName = unitName;
    }
    
    /** Getter for property leadUnitFlag.
     * @return Value of property leadUnitFlag.
     */
    public boolean isLeadUnitFlag() {
        return leadUnitFlag;
    }
    
    /** Setter for property leadUnitFlag.
     * @param leadUnitFlag New value of property leadUnitFlag.
     */
    public void setLeadUnitFlag(boolean leadUnitFlag) {
        this.leadUnitFlag = leadUnitFlag;
    }
    
    /** Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /** Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /** Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /** Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
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
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
    
    /** Getter for property aw_UnitNumber.
     * @return Value of property aw_UnitNumber.
     */
    public java.lang.String getAw_UnitNumber() {
        return aw_UnitNumber;
    }
    
    /** Setter for property aw_UnitNumber.
     * @param aw_UnitNumber New value of property aw_UnitNumber.
     */
    public void setAw_UnitNumber(java.lang.String aw_UnitNumber) {
        this.aw_UnitNumber = aw_UnitNumber;
    }
    
    /** Getter for property aw_PersonId.
     * @return Value of property aw_PersonId.
     */
    public java.lang.String getAw_PersonId() {
        return aw_PersonId;
    }
    
    /** Setter for property aw_PersonId.
     * @param aw_PersonId New value of property aw_PersonId.
     */
    public void setAw_PersonId(java.lang.String aw_PersonId) {
        this.aw_PersonId = aw_PersonId;
    }    
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
    }               
    
    /** Getter for property ospAdministratorName.
     * @return Value of property ospAdministratorName.
     */
    public java.lang.String getOspAdministratorName() {
        return ospAdministratorName;
    }
    
    /** Setter for property ospAdministratorName.
     * @param ospAdministratorName New value of property ospAdministratorName.
     */
    public void setOspAdministratorName(java.lang.String ospAdministratorName) {
        this.ospAdministratorName = ospAdministratorName;
    }
    
}