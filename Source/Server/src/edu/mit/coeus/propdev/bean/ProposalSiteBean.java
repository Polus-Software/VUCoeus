/*
 * @(#)ProposalSiteBean.java 1.0 6/13/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.rolodexmaint.bean.RolodexDetailsBean;
import edu.mit.coeus.utils.CoeusVector;
import java.sql.Timestamp;
import java.util.Vector;

/**
 *
 * @author leenababu
 */
public class ProposalSiteBean implements BaseBean, java.io.Serializable{
    private String proposalNumber;
    private int siteNumber;
    private String locationName;
    private int locationTypeCode ;
    
    private String locationTypeName;
    private String organizationId;
    private RolodexDetailsBean rolodexDetailsBean;
    private CoeusVector congDistricts;
    
    private Timestamp updateTimestamp;
    private String updateUser;
    private String acType;
    /** Creates a new instance of ProposalSiteBean */
    public ProposalSiteBean() {
    }
    
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    public String getLocationName() {
        return locationName;
    }
    
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public String getLocationTypeName() {
        return locationTypeName;
    }
    
    public void setLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
    }
    
    public String getOrganizationId() {
        return organizationId;
    }
    
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public int getSiteNumber() {
        return siteNumber;
    }
    
    public void setSiteNumber(int siteNumber) {
        this.siteNumber = siteNumber;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    public RolodexDetailsBean getRolodexDetailsBean() {
        return rolodexDetailsBean;
    }
    
    public void setRolodexDetailsBean(RolodexDetailsBean rolodexDetailsBean) {
        this.rolodexDetailsBean = rolodexDetailsBean;
    }
    
    public CoeusVector getCongDistricts() {
        return congDistricts;
    }
    
    public void setCongDistricts(CoeusVector congDistricts) {
        this.congDistricts = congDistricts;
    }
    
    public int getLocationTypeCode() {
        return locationTypeCode;
    }
    
    public void setLocationTypeCode(int locationTypeCode) {
        this.locationTypeCode = locationTypeCode;
    }
}
