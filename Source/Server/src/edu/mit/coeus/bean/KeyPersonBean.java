/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/*
 * KeyPersonBean.java
 *
 * Created on January 6, 2009, 11:35 AM
 */

package edu.mit.coeus.bean;

import edu.mit.coeus.utils.CoeusVector;
import java.sql.Timestamp;

public class KeyPersonBean extends SyncInfoBean implements java.io.Serializable, CoeusBean {
    
    private String personId;
    private String aw_PersonId;
    private String personName;
    private boolean nonMITPersonFlag;
    private boolean facultyFlag;
    private float percentageEffort;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;
    private float academicYearEffort;
    private float summerYearEffort;
    private float calendarYearEffort;
    private String projectRole;
    //holds the vector key person bean
    private CoeusVector keyPersonsUnits;
    private boolean PerOrRol;
    
    //ppc change for keyperson starts
  // private boolean ppcCertifyFlag;
    private int ppcCertifyFlag;

    public int getPpcCertifyFlag() {
        return ppcCertifyFlag;
    }

    public void setPpcCertifyFlag(int ppcCertifyFlag) {
        this.ppcCertifyFlag = ppcCertifyFlag;
    }    
    
    /* JM 9-8-2015 added person status */
    public String status;
    /* JM END */
    
    /* JM 2-2-2016 added isExternalPerson flag */
    private String isExternalPerson;
    /* JM END */
    
    //ppc change for keyperson ends
    
    /**
     * Creates a copy of the given KeyPersonBean
     */
    public KeyPersonBean(){setPerOrRol(false);
        
    }
    public KeyPersonBean(KeyPersonBean keyPersonBean){
        setPersonId(keyPersonBean.getPersonId());
        setAw_PersonId(keyPersonBean.getAw_PersonId());
        setPersonName(keyPersonBean.getPersonName());
        setNonMITPersonFlag(keyPersonBean.isNonMITPersonFlag());
        setFacultyFlag(keyPersonBean.isFacultyFlag());
        setPercentageEffort(keyPersonBean.getPercentageEffort());
        setUpdateUser(keyPersonBean.getUpdateUser());
        setUpdateTimestamp(keyPersonBean.getUpdateTimestamp());
        setAcType(keyPersonBean.getAcType());
        setAcademicYearEffort(keyPersonBean.getAcademicYearEffort());
        setSummerYearEffort(keyPersonBean.getSummerYearEffort());
        setCalendarYearEffort(keyPersonBean.getCalendarYearEffort());
        setProjectRole(keyPersonBean.getProjectRole());
        setKeyPersonsUnits(keyPersonBean.getKeyPersonsUnits());
        setPerOrRol(keyPersonBean.isPerOrRol());
        //ppc certify flag starts
        setPpcCertifyFlag(keyPersonBean.getPpcCertifyFlag());
        //ppc certify flag starts
        /* JM 9-8-2015 added person status */
        setStatus(keyPersonBean.getStatus());
        /* JM END */
        
        /* JM 2-2-2016 added isExternalPerson flag */
        setIsExternalPerson(keyPersonBean.getIsExternalPerson());
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
    
    /* JM 9-8-2015 added person status */
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
    
    public String getProjectRole() {
        return projectRole;
    }
    
    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    /**
     * @return the keyPersonsUnits
     */
    public CoeusVector getKeyPersonsUnits() {
        return keyPersonsUnits;
    }

    /**
     * @param investigatorUnits the keyPersonsUnits to set
     */
    public void setKeyPersonsUnits(CoeusVector keyPersonsUnits) {
        this.keyPersonsUnits = keyPersonsUnits;
    }

    /**
     * @return the PerOrRol
     */
    public boolean isPerOrRol() {
        return PerOrRol;
    }

    /**
     * @param PerOrRol the PerOrRol to set
     */
    public void setPerOrRol(boolean PerOrRol) {
        this.PerOrRol = PerOrRol;
    }

//    public int getSequenceNumber() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    public void setSequenceNumber(int sequenceNumber) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    
}
