/**
 * @(#)OrganizationAuditBean.java 1.0 8/22/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.organization.bean;

import java.sql.Timestamp;
/**
 * This class represent the Organization Audit Bean and is 
 * used to set and gets the Organization questions's explanation
 * and reviewed date.
 *
 * @version :1.0 August 22,2002
 * @author Guptha K
 */
public class OrganizationAuditBean implements java.io.Serializable {
    private String organizationId;
    private String fiscalYear;
    private String auditAccepted;
    private String auditComment;
    private Timestamp updateTimeStamp;
    private String updateUser;
    private String acType;
    
    /**
     * Default Constructor
     */
    
    public OrganizationAuditBean(){
    }
    
    /**
     *	This method is used to fetch the organization id.
     *  @return String organizationId
     */
    
    public String getOrganizationId(){
        return organizationId;
    }
    
    /**
     *	This method is used to set the organization id
     *  @param organizationId String organizationId
     */
    
    public void setOrganizationId(String organizationId){
        this.organizationId = organizationId;
    }
    
    /**
     *	This method is used to fetch the Fiscal Year
     *  @return String fiscalYear
     */
    
    public String getFiscalYear() {
        return fiscalYear;
    }
    
    /**
     *	This method is used to set the Fiscal Year.
     *  @param fiscalYear String fiscalYear
     */
    
    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
    
    /**
     *	This method is used to fetch the accepted audit.
     *  @return auditAccepted
     */
    
    public String getAuditAccepted() {
        return auditAccepted;
    }
    
    /**
     *	This method is used to set the accepted audit
     *  @param auditAccepted String auditAccepted
     */
    
    public void setAuditAccepted(String auditAccepted) {
        this.auditAccepted = auditAccepted;
    }
    
    /**
     *	This method is used to fetch the comment on audit
     *  @return String auditComment
     */
    
    public String getAuditComment() {
        return auditComment;
    }
    
    /**
     *	This method is used to set the audit comment
     *  @param auditComment String auditComment
     */
    
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
    
    /**
     *	This method is used to fetch the timestamp of the update.
     *  @return updateTimeStamp
     */
    
    public Timestamp getUpdateTimeStamp() {
        return updateTimeStamp;
    }
    
    /**
     *	This method is used to set the timestamp of the update.
     *  @param updateTimeStamp String updateTimeStamp
     */
    
    public void setUpdateTimeStamp(Timestamp updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }
    
    /**
     *	This method is used to fetch the user, who updated the details.
     *  @return String updateUser
     */
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     *	This method is used to set the user who updated the details
     *  @param updateUser String updateUser
     */
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     *	This method is used to get the action type Add/Update
     *	@return String updateUser
     */
    
    public String getAcType() {
        return acType;
    }
    
    /**
     *	This method is used to set the action type Add/Update
     *	@param	acType String acType
     */
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
}
