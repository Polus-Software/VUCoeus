/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.sql.Date;
import org.apache.naming.java.javaURLContextFactory;

/**
 *
 * @author midhunmk
 */
public class CoiFinancialEntityBean {
    private String personId;
    private String entityNumber;
    private Integer sequenceNumber;
    private Integer statusCode;
    private String statusDescription;
    private String entityName;
    private Integer entityTypeCode;
    private String entityOwnerShipTypeCode;
    private Integer relationshipTypeCode;
    private String relationshipDescription;
    private String relatedToOrgFlag;
    private String orgRelationDescription;
    private String sponsorCode;
    private java.sql.Date updateTimeStamp;
    private String updateUser;
    private String code; 
    private String description;
    private String invlmntStud;
    private String invlmntStaff;
    private String resoucreMit;

    public String getInvlmntStaff() {
        return invlmntStaff;
    }

    public void setInvlmntStaff(String invlmntStaff) {
        this.invlmntStaff = invlmntStaff;
    }

    public String getInvlmntStud() {
        return invlmntStud;
    }

    public void setInvlmntStud(String invlmntStud) {
        this.invlmntStud = invlmntStud;
    }

    public String getResoucreMit() {
        return resoucreMit;
    }

    public void setResoucreMit(String resoucreMit) {
        this.resoucreMit = resoucreMit;
    }

      public String getCode() {
    
        if(((code==null)||(code.trim().length()==0))&&(sequenceNumber!=null))
        {code=entityNumber+":"+sequenceNumber.toString();}
        return code;
    } 

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        if((description==null)||description.trim().length()==0)
        {description=entityName;}
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityNumber() {
        return entityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        this.entityNumber = entityNumber;
    }

    public String getEntityOwnerShipTypeCode() {
        return entityOwnerShipTypeCode;
    }

    public void setEntityOwnerShipTypeCode(String entityOwnerShipTypeCode) {
        this.entityOwnerShipTypeCode = entityOwnerShipTypeCode;
    }

    public Integer getEntityTypeCode() {
        return entityTypeCode;
    }

    public void setEntityTypeCode(Integer entityTypeCode) {
        this.entityTypeCode = entityTypeCode;
    }

    public String getOrgRelationDescription() {
        return orgRelationDescription;
    }

    public void setOrgRelationDescription(String orgRelationDescription) {
        this.orgRelationDescription = orgRelationDescription;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getRelatedToOrgFlag() {
        return relatedToOrgFlag;
    }

    public void setRelatedToOrgFlag(String relatedToOrgFlag) {
        this.relatedToOrgFlag = relatedToOrgFlag;
    }

    public String getRelationshipDescription() {
        return relationshipDescription;
    }

    public void setRelationshipDescription(String relationshipDescription) {
        this.relationshipDescription = relationshipDescription;
    }

    public Integer getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    public void setRelationshipTypeCode(Integer relationshipTypeCode) {
        this.relationshipTypeCode = relationshipTypeCode;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Date getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(Date updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
        
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
        
    }
    
   
}
