/*
 * DisclosureBean.java
 *
 * Created on February 27, 2008, 4:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

import java.sql.Date;
import java.sql.Timestamp;


/**
 *
 * @author blessy
 */
public class DisclosureBean {
    
    /** Creates a new instance of DisclosureBean */
    public DisclosureBean() {
    }
    private String coiDisclosureNumber;
    private Integer sequenceNumber;
    private String personId;
    private Integer disclosureStatusCode;
    private Date updateTimestamp;
    private String updateUser;
    private Date expirationDate;
    private String acType;
    
    private String fullName;
    private String unitName;
    private int conflictStatus;

    public String getCoiDisclosureNumber() {
        return coiDisclosureNumber;
    }

    public void setCoiDisclosureNumber(String coiDisclosureNumber) {
        this.coiDisclosureNumber = coiDisclosureNumber;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Integer getDisclosureStatusCode() {
        return disclosureStatusCode;
    }

    public void setDisclosureStatusCode(Integer disclosureStatusCode) {
        this.disclosureStatusCode = disclosureStatusCode;
    }

    public Date getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getConflictStatus() {
        return conflictStatus;
    }

    public void setConflictStatus(int conflictStatus) {
        this.conflictStatus = conflictStatus;
    }
    
}
