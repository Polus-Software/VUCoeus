/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mit.coeuslite.coiv2.beans;

import java.io.Serializable;

/**
 *
 * @author anishk
 */
public class CoiInfoBean implements Serializable{
    public CoiInfoBean(){
        
    }
    private String disclosureNumber;
    private Integer sequenceNumber;
    private String personId;
    private String menuType;
    private Integer approvedSequence;
    private String projectType;
    private int eventType;
    private int moduleCode;
    private String userId;
    private String projectId;
    private int projectCount;

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getApprovedSequence() {
        return approvedSequence;
    }

    public void setApprovedSequence(Integer approvedSequence) {
        this.approvedSequence = approvedSequence;
    }

    public String getDisclosureNumber() {
        return disclosureNumber;
    }

    public void setDisclosureNumber(String disclosureNumber) {
        this.disclosureNumber = disclosureNumber;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public int getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
}
