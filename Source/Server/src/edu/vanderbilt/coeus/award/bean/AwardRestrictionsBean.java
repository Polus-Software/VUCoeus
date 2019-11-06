/**
 * AwardRestrictionsBean.java
 * 
 * Used to hold award restrictions data
 *
 * @created	Spetember 19, 2014
 * @author	Jill McAfee, Vanderbilt University
 */

package edu.vanderbilt.coeus.award.bean;

import java.sql.Timestamp;
import java.sql.Date;

public class AwardRestrictionsBean extends edu.mit.coeus.award.bean.AwardBaseBean {
    
	private Integer restrictionTypeCode, awardRestrictionNumber;
	private String restrictionTypeDescription;
	private Date dueDate, actionDate;
	private String status;
	private String assignedUser;
	private String assignedUserName;
	private String comments;
    
    /**
     *	Default Constructor
     */
    public AwardRestrictionsBean(){
    }

    public Integer getAwardRestrictionNumber() {
        return awardRestrictionNumber;
    }
    
    public void setAwardRestrictionNumber(Integer awardRestrictionNumber) {
        this.awardRestrictionNumber = awardRestrictionNumber;
    }
    
    public Integer getRestrictionTypeCode() {
        return restrictionTypeCode;
    }
    
    public void setRestrictionTypeCode(Integer restrictionTypeCode) {
        this.restrictionTypeCode = restrictionTypeCode;
    }
    
    public String getRestrictionTypeDescription() {
        return restrictionTypeDescription;
    }
    
    public void setRestrictionTypeDescription(String restrictionTypeDescription) {
        this.restrictionTypeDescription = restrictionTypeDescription;
    }
        
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Date getActionDate() {
        return actionDate;
    }
    
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
   		this.status = status;
    }

    public String getAssignedUser() {
        return assignedUser;
    }
    
    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }
    
    public String getAssignedUserName() {
        return assignedUserName;
    }
    
    public void setAssignedUserName(String assignedUserName) {
        this.assignedUserName = assignedUserName;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
}    
