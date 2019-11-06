/*
 * @(#)InvestigatorBean.java 1.0 03/01/04 11:45 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;
import java.sql.Timestamp;
import java.sql.Date;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.CoeusVector;

import java.util.Map;

/** The class is used to hold the information of Investigators.
 * This is the base class for Investigators.
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 01, 2004, 11:45 AM
 */

public class InvestigatorBean extends SyncInfoBean implements java.io.Serializable, CoeusBean {
    
    // holds the personid number
    private String personId;
    //holds aw_PersonId
    private String aw_PersonId;
    // holds the person name
    private String personName;
    //holds principal investigator flag
    private boolean principalInvestigatorFlag;
    //holds non mit person flag
    private boolean nonMITPersonFlag;
    //holds the faculty flag
    private boolean facultyFlag;
    //holds the conflict of interset flag
    private boolean conflictOfIntersetFlag;
    //holds the percentage effort
    private float percentageEffort;
    //holds the fedr debr flag
    private boolean fedrDebrFlag;
    //holds the fedr delg flag
    private boolean fedrDelqFlag;
    //holds the vector investigator bean
    private CoeusVector investigatorUnits;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    
    //Declared for Case# 2229
    //Holds the Multi PI Flag
    private boolean multiPIFlag;
    
    // Declared for Case# 2270 Start
    //Holds Academic Year
    private float academicYearEffort;
    //Holds Summer Effort
    private float summerYearEffort;
    //Holds Calendar Year
    private float calendarYearEffort;
    // Declared for Case# 2270 End
    
    /* JM 9-10-2015 added person status */
    private String status;
    /* JM END */
    
    /* JM 2-2-2016 added isExternalPerson flag */
    private String isExternalPerson;
    /* JM END */
    
    /** Creates new InvestigatorBean */
    public InvestigatorBean() {
        
    }
    
    /** Creates a copy of the given InvestigatorBean */
    public InvestigatorBean(InvestigatorBean investigatorBean){
        setPersonId(investigatorBean.getPersonId());
        setAw_PersonId(investigatorBean.getAw_PersonId());
        setPersonName(investigatorBean.getPersonName());
        setPrincipalInvestigatorFlag(investigatorBean.isPrincipalInvestigatorFlag());
        setNonMITPersonFlag(investigatorBean.isNonMITPersonFlag());
        setFacultyFlag(investigatorBean.isFacultyFlag());
        setConflictOfIntersetFlag(investigatorBean.isConflictOfIntersetFlag());
        setPercentageEffort(investigatorBean.getPercentageEffort());
        setFedrDebrFlag(investigatorBean.isFedrDebrFlag());
        setFedrDelqFlag(investigatorBean.isFedrDelqFlag());
        setInvestigatorUnits(investigatorBean.getInvestigatorUnits());
        setUpdateUser(investigatorBean.getUpdateUser());
        setUpdateTimestamp(investigatorBean.getUpdateTimestamp());
        setAcType(investigatorBean.getAcType());
        
        //Added for Case# 2229
        setMultiPIFlag(investigatorBean.isMultiPIFlag());
        
        // Added for Case# 2270 Start
        setAcademicYearEffort(investigatorBean.getAcademicYearEffort());
        setSummerYearEffort(investigatorBean.getSummerYearEffort());
        setCalendarYearEffort(investigatorBean.getCalendarYearEffort());
        // Added for Case# 2270 End
        //Case 2796: Sync To Parent
        setSyncRequired(investigatorBean.isSyncRequired());
        setSyncTarget(investigatorBean.getSyncTarget());
        setParameter((Map)investigatorBean.getParameter());
       //2796 End
        /* JM 9-10-2015 added person status */
        setStatus(investigatorBean.getStatus());
        /* JM END */
        
        /* JM 2-2-2016 added isExternalPerson flag */
        setIsExternalPerson(investigatorBean.getIsExternalPerson());
        /* JM END */
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

    /* JM 2-2-2016 added isExternalPerson flag */
    public String getIsExternalPerson() {
    	return isExternalPerson;
    }
    
    public void setIsExternalPerson(String isExternalPerson) {
    	this.isExternalPerson = isExternalPerson;
    }
    /* JM END */
    
    /* JM 9-10-2015 added person status */
    public String getStatus() {
    	return status;
    }
    
    public void setStatus(String status) {
    	this.status = status;
    }
    /* JM END */
    
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
    
    /** Getter for property principalInvestigatorFlag.
     * @return Value of property principalInvestigatorFlag.
     */
    public boolean isPrincipalInvestigatorFlag() {
        return principalInvestigatorFlag;
    }
    
    /** Setter for property principalInvestigatorFlag.
     * @param principalInvestigatorFlag New value of property principalInvestigatorFlag.
     */
    public void setPrincipalInvestigatorFlag(boolean principalInvestigatorFlag) {
        this.principalInvestigatorFlag = principalInvestigatorFlag;
    }
    
    /** Getter for property nonMITPersonFlag.
     * @return Value of property nonMITPersonFlag.
     */
    public boolean isNonMITPersonFlag() {
        return nonMITPersonFlag;
    }
    
    /** Setter for property nonMITPersonFlag.
     * @param nonMITPersonFlag New value of property nonMITPersonFlag.
     */
    public void setNonMITPersonFlag(boolean nonMITPersonFlag) {
        this.nonMITPersonFlag = nonMITPersonFlag;
    }
    
    /** Getter for property facultyFlag.
     * @return Value of property facultyFlag.
     */
    public boolean isFacultyFlag() {
        return facultyFlag;
    }
    
    /** Setter for property facultyFlag.
     * @param facultyFlag New value of property facultyFlag.
     */
    public void setFacultyFlag(boolean facultyFlag) {
        this.facultyFlag = facultyFlag;
    }
    
    /** Getter for property conflictOfIntersetFlag.
     * @return Value of property conflictOfIntersetFlag.
     */
    public boolean isConflictOfIntersetFlag() {
        return conflictOfIntersetFlag;
    }
    
    /** Setter for property conflictOfIntersetFlag.
     * @param conflictOfIntersetFlag New value of property conflictOfIntersetFlag.
     */
    public void setConflictOfIntersetFlag(boolean conflictOfIntersetFlag) {
        this.conflictOfIntersetFlag = conflictOfIntersetFlag;
    }
    
    /** Getter for property percentageEffort.
     * @return Value of property percentageEffort.
     */
    public float getPercentageEffort() {
        return percentageEffort;
    }
    
    /** Setter for property percentageEffort.
     * @param percentageEffort New value of property percentageEffort.
     */
    public void setPercentageEffort(float percentageEffort) {
        this.percentageEffort = percentageEffort;
    }
    
    /** Getter for property fedrDebrFlag.
     * @return Value of property fedrDebrFlag.
     */
    public boolean isFedrDebrFlag() {
        return fedrDebrFlag;
    }
    
    /** Setter for property fedrDebrFlag.
     * @param fedrDebrFlag New value of property fedrDebrFlag.
     */
    public void setFedrDebrFlag(boolean fedrDebrFlag) {
        this.fedrDebrFlag = fedrDebrFlag;
    }
    
    /** Getter for property fedrDelqFlag.
     * @return Value of property fedrDelqFlag.
     */
    public boolean isFedrDelqFlag() {
        return fedrDelqFlag;
    }
    
    /** Setter for property fedrDelqFlag.
     * @param fedrDelqFlag New value of property fedrDelqFlag.
     */
    public void setFedrDelqFlag(boolean fedrDelqFlag) {
        this.fedrDelqFlag = fedrDelqFlag;
    }
    
    /** Getter for property investigatorUnits.
     * @return Value of property investigatorUnits.
     */
    public CoeusVector getInvestigatorUnits() {
        return investigatorUnits;
    }
    
    /** Setter for property investigatorUnits.
     * @param investigatorUnits New value of property investigatorUnits.
     */
    public void setInvestigatorUnits(CoeusVector investigatorUnits) {
        this.investigatorUnits = investigatorUnits;
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
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
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
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
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
    
    // Added for Case# 2229 Start
    // Getter and Setter for multiPIFlag
    public boolean isMultiPIFlag() {
        return multiPIFlag;
    }
    
    public void setMultiPIFlag(boolean multiPIFlag) {
        this.multiPIFlag = multiPIFlag;
    }
    //Added for Case# 2229 End
    
    // Added for Case# 2270 Start
    // Getter and Setter Methods for academicYear, summerEffort, calendarYear 
    public float getAcademicYearEffort() {
        return academicYearEffort;
    }

    public void setAcademicYearEffort(float academicYearEffort) {
        this.academicYearEffort = academicYearEffort;
    }

    public float getSummerYearEffort() {
        return summerYearEffort;
    }

    public void setSummerYearEffort(float summerYearEffort) {
        this.summerYearEffort = summerYearEffort;
    }

    public float getCalendarYearEffort() {
        return calendarYearEffort;
    }

    public void setCalendarYearEffort(float calendarYearEffort) {
        this.calendarYearEffort = calendarYearEffort;
    }
    // Added for Case# 2270 End
}