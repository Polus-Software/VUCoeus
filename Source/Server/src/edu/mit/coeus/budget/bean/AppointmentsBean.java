/*
 * @(#)AppointmentsBean.java September 29, 2003, 11:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import java.util.Hashtable;

/**
 * The class used to hold the information of <code>Appointments</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 29, 2003, 11:58 AM
 */
public class AppointmentsBean implements CoeusBean, java.io.Serializable{
    
    //holds unitNumber
    private String unitNumber = null;
    //holds unit Name
    private String unitName = null;
    //holds person full name
    private String primarySecondaryIndicator = null;
    //holds appointment start date
    private java.sql.Date appointmentStartDate = null;
    //holds appointment end date
    private java.sql.Date appointmentEndDate = null;
    //holds appointment type
    private String appointmentType = null;
    //holds job title
    private String jobTitle = null;
    //holds job title
    private String preferredJobTitle = null;
    //holds job code
    private String jobCode = null;
    //holds salary
    private double salary;
    
    //holds update user id
    private String updateUser = null;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type
    private String acType = null;
    
    /** Creates a new instance of BudgetInfo */
    public AppointmentsBean() {
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
    
    /** Getter for property primarySecondaryIndicator.
     * @return Value of property primarySecondaryIndicator.
     */
    public java.lang.String getPrimarySecondaryIndicator() {
        return primarySecondaryIndicator;
    }
    
    /** Setter for property primarySecondaryIndicator.
     * @param primarySecondaryIndicator New value of property primarySecondaryIndicator.
     */
    public void setPrimarySecondaryIndicator(java.lang.String primarySecondaryIndicator) {
        this.primarySecondaryIndicator = primarySecondaryIndicator;
    }
    
    /** Getter for property appointmentStartDate.
     * @return Value of property appointmentStartDate.
     */
    public java.sql.Date getAppointmentStartDate() {
        return appointmentStartDate;
    }
    
    /** Setter for property appointmentStartDate.
     * @param appointmentStartDate New value of property appointmentStartDate.
     */
    public void setAppointmentStartDate(java.sql.Date appointmentStartDate) {
        this.appointmentStartDate = appointmentStartDate;
    }
    
    /** Getter for property appointmentEndDate.
     * @return Value of property appointmentEndDate.
     */
    public java.sql.Date getAppointmentEndDate() {
        return appointmentEndDate;
    }
    
    /** Setter for property appointmentEndDate.
     * @param appointmentEndDate New value of property appointmentEndDate.
     */
    public void setAppointmentEndDate(java.sql.Date appointmentEndDate) {
        this.appointmentEndDate = appointmentEndDate;
    }
    
    /** Getter for property appointmentType.
     * @return Value of property appointmentType.
     */
    public java.lang.String getAppointmentType() {
        return appointmentType;
    }
    
    /** Setter for property appointmentType.
     * @param appointmentType New value of property appointmentType.
     */
    public void setAppointmentType(java.lang.String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    /** Getter for property jobTitle.
     * @return Value of property jobTitle.
     */
    public java.lang.String getJobTitle() {
        return jobTitle;
    }
    
    /** Setter for property jobTitle.
     * @param jobTitle New value of property jobTitle.
     */
    public void setJobTitle(java.lang.String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    /** Getter for property preferredJobTitle.
     * @return Value of property preferredJobTitle.
     */
    public java.lang.String getPreferredJobTitle() {
        return preferredJobTitle;
    }
    
    /** Setter for property preferredJobTitle.
     * @param preferredJobTitle New value of property preferredJobTitle.
     */
    public void setPreferredJobTitle(java.lang.String preferredJobTitle) {
        this.preferredJobTitle = preferredJobTitle;
    }
    
    /** Getter for property jobCode.
     * @return Value of property jobCode.
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /** Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }
    
    /** Getter for property salary.
     * @return Value of property salary.
     */
    public double getSalary() {
        return salary;
    }
    
    /** Setter for property salary.
     * @param salary New value of property salary.
     */
    public void setSalary(double salary) {
        this.salary = salary;
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
    
    public boolean isLike(ComparableBean comparableBean) {
        return true;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Unit Number =>"+unitNumber);
        strBffr.append(";");
        strBffr.append("Unit Name =>"+unitName);
        strBffr.append(";");
        strBffr.append("Primary Secondary Indicator =>"+primarySecondaryIndicator);
        strBffr.append(";");
        strBffr.append("Appointment Start Date =>"+appointmentStartDate);
        strBffr.append(";");
        strBffr.append("Appointment End Date =>"+appointmentEndDate);
        strBffr.append(";");
        strBffr.append("Appointment Type =>"+appointmentType);
        strBffr.append(";");
        strBffr.append("Job Title =>"+jobTitle);
        strBffr.append(";");
        strBffr.append("Preferred Job Title =>"+preferredJobTitle);
        strBffr.append(";");
        strBffr.append("Job Code =>"+jobCode);
        strBffr.append(";");
        strBffr.append("Salary =>"+salary);
        strBffr.append(";");
        strBffr.append("Update User =>"+updateUser);
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+updateTimestamp);
        strBffr.append(";");
        strBffr.append("AcType =>"+acType);    
        return strBffr.toString();
    }
}