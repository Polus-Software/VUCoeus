/*
 * @(#)CommitteeResearchAreasBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.irb.bean;

/**
 * The class used to hold the information of
 * <code>MembershipAreaofResearch Detail</code>
 * The data will get attached with the 
 * <code>MembershipAreaofResearch Detail</code> form.
 *
 * @author  phani
 * @version 1.0
 * Created on October 3, 2002, 2:54 PM 
 */

public class CommitteeResearchAreasBean implements java.io.Serializable {
    //holds committee Id
    private String committeeId;
    //holds AreaofResearch code
    private String areaOfResearchCode;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds AreaofResearch Description
    private String areaOfResearchDescription;
    //holds account type
    private String acType;
    
    /** 
     * Creates a new instance of CommitteeResearchAreasBean 
     */
    public CommitteeResearchAreasBean() {
    }
    
    /**
     * Method used to get the committee Id
     * @return committeeId String
     */
    public String getCommitteeId() {
        return committeeId;
    }
    
    /**
     * Method used to set the committee Id
     * @param committeeId String
     */
    public void setCommitteeId(String committeeId) {
        this.committeeId = committeeId;
    }
    
    /**
     * Method used to get the AreaOfResearch Code
     * @return areaOfResearchCode String
     */
    public String getAreaOfResearchCode() {
        return areaOfResearchCode;
    }
    
    /**
     * Method used to set the AreaOfResearch Code
     * @param areaOfResearchCode String
     */
    public void setAreaOfResearchCode(String areaOfResearchCode) {
        this.areaOfResearchCode = areaOfResearchCode;
    }
    
    /**
     * Method used to get the update user id
     * @return updateUser String
     */
    public String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the AreaOfResearch Description
     * @return areaOfResearchDescription String
     */
    public String getAreaOfResearchDescription() {
        return areaOfResearchDescription;
    }
    
    /**
     * Method used to set the AreaOfResearch Description
     * @param areaOfResearchDescription String
     */
    public void setAreaOfResearchDescription(String areaOfResearchDescription) {
        this.areaOfResearchDescription = areaOfResearchDescription;
    }
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType() {
        return acType;
    }
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    
}
