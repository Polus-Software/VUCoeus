/*
 * BudgetPersonSyncBean.java
 *
 * Created on January 24, 2006, 2:45 PM
 */

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Date;

/**
 *
 * @author  chandrashekara
 */
public class BudgetPersonSyncBean implements BaseBean, java.io.Serializable{
    private String personId;
    private String appointmentType;
    private String fullName;
    private String jobCode;
    private double salary;
    private String acType;
    private boolean nonEmployee;
    //Added for Case#2918 - Use of Salary Anniversary Date for calculating inflation in budget development module
    private Date salaryAnniversaryDate;
    
    /** Creates a new instance of BudgetPersonSyncBean */
    public BudgetPersonSyncBean() {
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property appointmentType.
     * @return Value of property appointmentType.
     */
    public java.lang.String getAppointmentType() {
        return appointmentType;
    }
    
    /**
     * Setter for property appointmentType.
     * @param appointmentType New value of property appointmentType.
     */
    public void setAppointmentType(java.lang.String appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    /**
     * Getter for property fullName.
     * @return Value of property fullName.
     */
    public java.lang.String getFullName() {
        return fullName;
    }
    
    /**
     * Setter for property fullName.
     * @param fullName New value of property fullName.
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }
    
    /**
     * Getter for property jobCode.
     * @return Value of property jobCode.
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /**
     * Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }
    
    /**
     * Getter for property salary.
     * @return Value of property salary.
     */
    public double getSalary() {
        return salary;
    }
    
    /**
     * Setter for property salary.
     * @param salary New value of property salary.
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }

    public boolean isNonEmployee() {
        return nonEmployee;
    }

    public void setNonEmployee(boolean nonEmployee) {
        this.nonEmployee = nonEmployee;
    }

    public Date getSalaryAnniversaryDate() {
        return salaryAnniversaryDate;
    }

    public void setSalaryAnniversaryDate(Date salaryAnniversaryDate) {
        this.salaryAnniversaryDate = salaryAnniversaryDate;
    }
    
}
