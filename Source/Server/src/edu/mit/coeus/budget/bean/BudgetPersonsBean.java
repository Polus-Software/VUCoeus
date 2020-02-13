/*
 * @(#)BudgetPersonsBean.java September 29, 2003, 11:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.budget.bean;

import java.sql.Date;

import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.UtilFactory;

/**
 * The class used to hold the information of <code>Budget Persons</code>
 *
 * @author Prasanna Kumar
 * @version 1.0 Created on September 29, 2003, 11:58 AM
 */
public class BudgetPersonsBean extends BudgetBean implements PrimaryKey, SortBean {
	// Holds Row Id.
	// This is used to identify the record when the user is allowed to
	// modify Primary Key values.
	private int rowId;

	// holds budget period
	// private int budgetPeriod = -1;
	// holds lineItemNumber
	// private int lineItemNumber = -1;
	// holds person id
	private String personId = null;
	// holds person full name
	private String fullName = null;
	// holds job code
	private String jobCode = null;
	// holds Effective Date
	private java.sql.Date effectiveDate = null;
	// holds calculation base
	private double calculationBase;
	// holds appointment type
	private String appointmentType = null;
	// holds non employee flag
	private boolean nonEmployee;

	// holds aw proposal number
	private String aw_ProposalNumber = null;
	// holds version number
	private int aw_VersionNumber = -1;
	// holds aw person id
	private String aw_PersonId = null;
	// holds job code
	private String aw_JobCode = null;
	// holds Effective Date
	private java.sql.Date aw_EffectiveDate = null;
	private boolean aw_nonEmployeeFlag;
	// Added by Chandra
	// holds Appointment Type
	private String aw_AppointmentType;
	// holds CalculationBase
	private double aw_CalculationBase;
	// Added for Case#2918 - Use of Salary Anniversary Date for calculating
	// inflation in budget development module - starts
	private Date salaryAnniversaryDate;
	private Date aw_SalaryAnniversaryDate;
	// Added for Case#2918 - Use of Salary Anniversary Date for calculating
	// inflation in budget development module - ends

	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - start
	private String status;
	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - end

	// COEUSQA-1559 Display calculated base salary amount on RR Budget form in
	// out years - Start
	// holds the period wise salary for the persons
	private double baseSalaryP1;
	private double aw_BaseSalaryP1;
	private double baseSalaryP2;
	private double aw_BaseSalaryP2;
	private double baseSalaryP3;
	private double aw_BaseSalaryP3;
	private double baseSalaryP4;
	private double aw_BaseSalaryP4;
	private double baseSalaryP5;
	private double aw_BaseSalaryP5;
	private double baseSalaryP6;
	private double aw_BaseSalaryP6;
	private double baseSalaryP7;
	private double aw_BaseSalaryP7;
	private double baseSalaryP8;
	private double aw_BaseSalaryP8;
	private double baseSalaryP9;
	private double aw_BaseSalaryP9;
	private double baseSalaryP10;
	private double aw_BaseSalaryP10;
	// COEUSQA-1559 Display calculated base salary amount on RR Budget form in
	// out years - End

	/* JM 9-4-2015 added person status */
	public String personStatus;
	/* JM END */

	/* JM 4-4-2016 added person status */
	public String isExternalPerson;
	/* JM END */
	
	/** Creates a new instance of BudgetInfo */
	public BudgetPersonsBean() {
	}

	// Added by Chandra 03/10/2003 - start
	@Override
	public boolean equals(Object obj) {
		BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean) obj;
		// if(budgetPersonsBean.getProposalNumber().equals(getProposalNumber())
		// &&
		// budgetPersonsBean.getVersionNumber() == getVersionNumber() &&
		// (budgetPersonsBean.getAw_PersonId()!=null && getAw_PersonId() != null
		// && budgetPersonsBean.getAw_PersonId().equals(getAw_PersonId())) &&
		// (budgetPersonsBean.getAw_JobCode()!=null && getAw_JobCode() != null
		// && budgetPersonsBean.getAw_JobCode().equals(getAw_JobCode())) &&
		// (budgetPersonsBean.getAw_EffectiveDate()!=null &&
		// getAw_EffectiveDate() !=null &&
		// budgetPersonsBean.getAw_EffectiveDate().equals(getAw_EffectiveDate())))
		// {

		if (budgetPersonsBean.getProposalNumber().equals(getProposalNumber())
				&& budgetPersonsBean.getVersionNumber() == getVersionNumber()
				&& budgetPersonsBean.getRowId() == getRowId()) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Getter for property appointmentType.
	 * 
	 * @return Value of property appointmentType.
	 */
	public java.lang.String getAppointmentType() {
		return appointmentType;
	}

	/**
	 * Getter for property aw_AppointmentType.
	 * 
	 * @return Value of property aw_AppointmentType.
	 */
	public java.lang.String getAw_AppointmentType() {
		return aw_AppointmentType;
	}

	/**
	 * Getter for property aw_BaseSalaryP1.
	 * 
	 * @return Value of property aw_BaseSalaryP1.
	 */
	public double getAw_BaseSalaryP1() {
		return aw_BaseSalaryP1;
	}

	/**
	 * Getter for property aw_BaseSalaryP10.
	 * 
	 * @return Value of property aw_BaseSalaryP10.
	 */
	public double getAw_BaseSalaryP10() {
		return aw_BaseSalaryP10;
	}

	/**
	 * Getter for property aw_BaseSalaryP2.
	 * 
	 * @return Value of property aw_BaseSalaryP2.
	 */
	public double getAw_BaseSalaryP2() {
		return aw_BaseSalaryP2;
	}

	/**
	 * Getter for property aw_BaseSalaryP3.
	 * 
	 * @return Value of property aw_BaseSalaryP3.
	 */
	public double getAw_BaseSalaryP3() {
		return aw_BaseSalaryP3;
	}

	/**
	 * Getter for property aw_BaseSalaryP4.
	 * 
	 * @return Value of property aw_BaseSalaryP4.
	 */
	public double getAw_BaseSalaryP4() {
		return aw_BaseSalaryP4;
	}

	/**
	 * Getter for property aw_BaseSalaryP5.
	 * 
	 * @return Value of property aw_BaseSalaryP5.
	 */
	public double getAw_BaseSalaryP5() {
		return aw_BaseSalaryP5;
	}

	/**
	 * Getter for property aw_BaseSalaryP6.
	 * 
	 * @return Value of property aw_BaseSalaryP6.
	 */
	public double getAw_BaseSalaryP6() {
		return aw_BaseSalaryP6;
	}

	/**
	 * Getter for property aw_BaseSalaryP7.
	 * 
	 * @return Value of property aw_BaseSalaryP7.
	 */
	public double getAw_BaseSalaryP7() {
		return aw_BaseSalaryP7;
	}

	/**
	 * Getter for property aw_BaseSalaryP8.
	 * 
	 * @return Value of property aw_BaseSalaryP8.
	 */
	public double getAw_BaseSalaryP8() {
		return aw_BaseSalaryP8;
	}

	/**
	 * Getter for property aw_BaseSalaryP9.
	 * 
	 * @return Value of property aw_BaseSalaryP9.
	 */
	public double getAw_BaseSalaryP9() {
		return aw_BaseSalaryP9;
	}

	/**
	 * Getter for property aw_CalculationBase.
	 * 
	 * @return Value of property aw_CalculationBase.
	 */
	public double getAw_CalculationBase() {
		return aw_CalculationBase;
	}

	/**
	 * Getter for property aw_EffectiveDate.
	 * 
	 * @return Value of property aw_EffectiveDate.
	 */
	public java.sql.Date getAw_EffectiveDate() {
		return aw_EffectiveDate;
	}

	/**
	 * Getter for property aw_JobCode.
	 * 
	 * @return Value of property aw_JobCode.
	 */
	public java.lang.String getAw_JobCode() {
		return aw_JobCode;
	}

	/**
	 * Getter for property aw_PersonId.
	 * 
	 * @return Value of property aw_PersonId.
	 */
	public java.lang.String getAw_PersonId() {
		return aw_PersonId;
	}

	/**
	 * Getter for property aw_ProposalNumber.
	 * 
	 * @return Value of property aw_ProposalNumber.
	 */
	public java.lang.String getAw_ProposalNumber() {
		return aw_ProposalNumber;
	}

	public Date getAw_SalaryAnniversaryDate() {
		return aw_SalaryAnniversaryDate;
	}

	/**
	 * Getter for property aw_VersionNumber.
	 * 
	 * @return Value of property aw_VersionNumber.
	 */
	public int getAw_VersionNumber() {
		return aw_VersionNumber;
	}

	// COEUSQA-1559 Display calculated base salary amount on RR Budget form in
	// out years - Start
	/**
	 * Getter for property baseSalaryP1.
	 * 
	 * @return Value of property baseSalaryP1.
	 */
	public double getBaseSalaryP1() {
		return baseSalaryP1;
	}

	/**
	 * Getter for property baseSalaryP10.
	 * 
	 * @return Value of property baseSalaryP10.
	 */
	public double getBaseSalaryP10() {
		return baseSalaryP10;
	}

	/**
	 * Getter for property baseSalaryP2.
	 * 
	 * @return Value of property baseSalaryP2.
	 */
	public double getBaseSalaryP2() {
		return baseSalaryP2;
	}

	/**
	 * Getter for property baseSalaryP3.
	 * 
	 * @return Value of property baseSalaryP3.
	 */
	public double getBaseSalaryP3() {
		return baseSalaryP3;
	}

	/**
	 * Getter for property baseSalaryP4.
	 * 
	 * @return Value of property baseSalaryP4.
	 */
	public double getBaseSalaryP4() {
		return baseSalaryP4;
	}

	/**
	 * Getter for property baseSalaryP5.
	 * 
	 * @return Value of property baseSalaryP5.
	 */
	public double getBaseSalaryP5() {
		return baseSalaryP5;
	}

	// Added by chandra - start

	/**
	 * Getter for property baseSalaryP6.
	 * 
	 * @return Value of property baseSalaryP6.
	 */
	public double getBaseSalaryP6() {
		return baseSalaryP6;
	}

	/**
	 * Getter for property baseSalaryP7.
	 * 
	 * @return Value of property baseSalaryP7.
	 */
	public double getBaseSalaryP7() {
		return baseSalaryP7;
	}

	/**
	 * Getter for property baseSalaryP8.
	 * 
	 * @return Value of property baseSalaryP8.
	 */
	public double getBaseSalaryP8() {
		return baseSalaryP8;
	}

	/**
	 * Getter for property baseSalaryP9.
	 * 
	 * @return Value of property baseSalaryP9.
	 */
	public double getBaseSalaryP9() {
		return baseSalaryP9;
	}

	/**
	 * Getter for property calculationBase.
	 * 
	 * @return Value of property calculationBase.
	 */
	public double getCalculationBase() {
		return calculationBase;
	}

	// Added by Chandra 03/10/2003 - end

	/**
	 * Getter for property effectiveDate.
	 * 
	 * @return Value of property effectiveDate.
	 */
	public java.sql.Date getEffectiveDate() {
		return effectiveDate;
	}

	@Override
	public java.util.Date getFieldToBeSorted() {
		return getEffectiveDate();
	}

	/**
	 * Getter for property fullName.
	 * 
	 * @return Value of property fullName.
	 */
	public java.lang.String getFullName() {
		return fullName;
	}

	/**
	 * Getter for property jobCode.
	 * 
	 * @return Value of property jobCode.
	 */
	public java.lang.String getJobCode() {
		return jobCode;
	}

	/**
	 * Getter for property personId.
	 * 
	 * @return Value of property personId.
	 */
	public java.lang.String getPersonId() {
		return personId;
	}

	/* JM 9-4-2015 added person status */
	public String getPersonStatus() {
		return personStatus;
	}
	/* JM END */
	
	/* JM 4-4-2016 added external person */
	public String getIsExternalPerson() {
		return isExternalPerson;
	}
	/* JM END */

	/**
	 * Does ...
	 *
	 *
	 * @return
	 */
	@Override
	public Object getPrimaryKey() {
		return new String(getProposalNumber() + getVersionNumber() + getPersonId() + getJobCode() + getEffectiveDate());
	}

	/**
	 * Getter for property rowId.
	 * 
	 * @return Value of property rowId.
	 */
	public int getRowId() {
		return rowId;
	}

	public Date getSalaryAnniversaryDate() {
		return salaryAnniversaryDate;
	}

	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - start
	/**
	 * Getter for property active status.
	 * 
	 * @return Value of property active status.
	 *
	 */
	public java.lang.String getStatus() {
		return status;
	}

	public boolean isAw_nonEmployeeFlag() {
		return aw_nonEmployeeFlag;
	}

	@Override
	public boolean isLike(ComparableBean comparableBean) throws CoeusException {
		boolean success = false;
		if (comparableBean instanceof BudgetPersonsBean) {
			BudgetPersonsBean budgetPersonsBean = (BudgetPersonsBean) comparableBean;
			// Proposal Number
			if (budgetPersonsBean.getProposalNumber() != null) {
				if (getProposalNumber().equals(budgetPersonsBean.getProposalNumber())) {
					success = true;
				} else {
					return false;
				}
			}
			// Version Number
			if (budgetPersonsBean.getVersionNumber() != -1) {
				if (getVersionNumber() == budgetPersonsBean.getVersionNumber()) {
					success = true;
				} else {
					return false;
				}
			}
			// Person Id
			if (budgetPersonsBean.getPersonId() != null) {
				if (getPersonId().equals(budgetPersonsBean.getPersonId())) {
					success = true;
				} else {
					return false;
				}
			}
			// Job Code
			if (budgetPersonsBean.getJobCode() != null) {
				if (getJobCode().equals(budgetPersonsBean.getJobCode())) {
					success = true;
				} else {
					return false;
				}
			}
			// Effective Date
			if (budgetPersonsBean.getEffectiveDate() != null) {
				if (getEffectiveDate().equals(budgetPersonsBean.getEffectiveDate())) {
					success = true;
				} else {
					return false;
				}
			}
		} else {
			throw new CoeusException("budget_exception.1000");
		}
		return success;
	}

	public boolean isNonEmployee() {
		return nonEmployee;
	}

	/**
	 * Setter for property appointmentType.
	 * 
	 * @param appointmentType
	 *            New value of property appointmentType.
	 */
	public void setAppointmentType(java.lang.String appointmentType) {
		this.appointmentType = appointmentType;
	}

	/**
	 * setter for property aw_AppointmentType
	 * 
	 * @param aw_AppointmentType
	 *            New Value of property aw_AppointmentType
	 */
	public void setAw_AppointmentType(java.lang.String aw_AppointmentType) {
		this.aw_AppointmentType = aw_AppointmentType;
	}

	/**
	 * Setter for property aw_BaseSalaryP1.
	 * 
	 * @param aw_BaseSalaryP1
	 *            New value of property aw_BaseSalaryP1.
	 */
	public void setAw_BaseSalaryP1(double aw_BaseSalaryP1) {
		this.aw_BaseSalaryP1 = aw_BaseSalaryP1;
	}

	/**
	 * Setter for property aw_BaseSalaryP10.
	 * 
	 * @param aw_BaseSalaryP10
	 *            New value of property aw_BaseSalaryP10.
	 */
	public void setAw_BaseSalaryP10(double aw_BaseSalaryP10) {
		this.aw_BaseSalaryP10 = aw_BaseSalaryP10;
	}
	// COEUSQA-1559 Display calculated base salary amount on RR Budget form in
	// out years - End

	/**
	 * Setter for property aw_BaseSalaryP2.
	 * 
	 * @param aw_BaseSalaryP2
	 *            New value of property aw_BaseSalaryP2.
	 */
	public void setAw_BaseSalaryP2(double aw_BaseSalaryP2) {
		this.aw_BaseSalaryP2 = aw_BaseSalaryP2;
	}

	/**
	 * Setter for property aw_BaseSalaryP3.
	 * 
	 * @param aw_BaseSalaryP3
	 *            New value of property aw_BaseSalaryP3.
	 */
	public void setAw_BaseSalaryP3(double aw_BaseSalaryP3) {
		this.aw_BaseSalaryP3 = aw_BaseSalaryP3;
	}

	/**
	 * Setter for property aw_BaseSalaryP4.
	 * 
	 * @param aw_BaseSalaryP4
	 *            New value of property aw_BaseSalaryP4.
	 */
	public void setAw_BaseSalaryP4(double aw_BaseSalaryP4) {
		this.aw_BaseSalaryP4 = aw_BaseSalaryP4;
	}

	/**
	 * Setter for property aw_BaseSalaryP5.
	 * 
	 * @param aw_BaseSalaryP5
	 *            New value of property aw_BaseSalaryP5.
	 */
	public void setAw_BaseSalaryP5(double aw_BaseSalaryP5) {
		this.aw_BaseSalaryP5 = aw_BaseSalaryP5;
	}

	/**
	 * Setter for property aw_BaseSalaryP6.
	 * 
	 * @param aw_BaseSalaryP6
	 *            New value of property aw_BaseSalaryP6.
	 */
	public void setAw_BaseSalaryP6(double aw_BaseSalaryP6) {
		this.aw_BaseSalaryP6 = aw_BaseSalaryP6;
	}

	/**
	 * Setter for property aw_BaseSalaryP7.
	 * 
	 * @param aw_BaseSalaryP7
	 *            New value of property aw_BaseSalaryP7.
	 */
	public void setAw_BaseSalaryP7(double aw_BaseSalaryP7) {
		this.aw_BaseSalaryP7 = aw_BaseSalaryP7;
	}

	/**
	 * Setter for property aw_BaseSalaryP8.
	 * 
	 * @param aw_BaseSalaryP8
	 *            New value of property aw_BaseSalaryP8.
	 */
	public void setAw_BaseSalaryP8(double aw_BaseSalaryP8) {
		this.aw_BaseSalaryP8 = aw_BaseSalaryP8;
	}

	/**
	 * Setter for property aw_BaseSalaryP9.
	 * 
	 * @param aw_BaseSalaryP9
	 *            New value of property aw_BaseSalaryP9.
	 */
	public void setAw_BaseSalaryP9(double aw_BaseSalaryP9) {
		this.aw_BaseSalaryP9 = aw_BaseSalaryP9;
	}

	/**
	 * setter for property aw_CalculationBase
	 * 
	 * @param aw_CalculationBase
	 *            New Value of property aw_CalculationBase
	 */
	public void setAw_CalculationBase(double aw_CalculationBase) {
		this.aw_CalculationBase = aw_CalculationBase;
	}

	/**
	 * Setter for property aw_EffectiveDate.
	 * 
	 * @param aw_EffectiveDate
	 *            New value of property aw_EffectiveDate.
	 */
	public void setAw_EffectiveDate(java.sql.Date aw_EffectiveDate) {
		this.aw_EffectiveDate = aw_EffectiveDate;
	}

	/**
	 * Setter for property aw_JobCode.
	 * 
	 * @param aw_JobCode
	 *            New value of property aw_JobCode.
	 */
	public void setAw_JobCode(java.lang.String aw_JobCode) {
		this.aw_JobCode = aw_JobCode;
	}

	public void setAw_nonEmployeeFlag(boolean aw_nonEmployeeFlag) {
		this.aw_nonEmployeeFlag = aw_nonEmployeeFlag;
	}

	/**
	 * Setter for property aw_PersonId.
	 * 
	 * @param aw_PersonId
	 *            New value of property aw_PersonId.
	 */
	public void setAw_PersonId(java.lang.String aw_PersonId) {
		this.aw_PersonId = aw_PersonId;
	}

	/**
	 * Setter for property aw_ProposalNumber.
	 * 
	 * @param aw_ProposalNumber
	 *            New value of property aw_ProposalNumber.
	 */
	public void setAw_ProposalNumber(java.lang.String aw_ProposalNumber) {
		this.aw_ProposalNumber = aw_ProposalNumber;
	}

	public void setAw_SalaryAnniversaryDate(Date aw_SalaryAnniversaryDate) {
		this.aw_SalaryAnniversaryDate = aw_SalaryAnniversaryDate;
	}

	/**
	 * Setter for property aw_VersionNumber.
	 * 
	 * @param aw_VersionNumber
	 *            New value of property aw_VersionNumber.
	 */
	public void setAw_VersionNumber(int aw_VersionNumber) {
		this.aw_VersionNumber = aw_VersionNumber;
	}

	/**
	 * Setter for property baseSalaryP1.
	 * 
	 * @param baseSalaryP1
	 *            New value of property baseSalaryP1.
	 */
	public void setBaseSalaryP1(double baseSalaryP1) {
		this.baseSalaryP1 = baseSalaryP1;
	}

	/**
	 * Setter for property baseSalaryP10.
	 * 
	 * @param baseSalaryP10
	 *            New value of property baseSalaryP10.
	 */
	public void setBaseSalaryP10(double baseSalaryP10) {
		this.baseSalaryP10 = baseSalaryP10;
	}

	/**
	 * Setter for property baseSalaryP2.
	 * 
	 * @param baseSalaryP2
	 *            New value of property baseSalaryP2.
	 */
	public void setBaseSalaryP2(double baseSalaryP2) {
		this.baseSalaryP2 = baseSalaryP2;
	}

	/**
	 * Setter for property baseSalaryP3.
	 * 
	 * @param baseSalaryP3
	 *            New value of property baseSalaryP3.
	 */
	public void setBaseSalaryP3(double baseSalaryP3) {
		this.baseSalaryP3 = baseSalaryP3;
	}

	/**
	 * Setter for property baseSalaryP4.
	 * 
	 * @param baseSalaryP4
	 *            New value of property baseSalaryP4.
	 */
	public void setBaseSalaryP4(double baseSalaryP4) {
		this.baseSalaryP4 = baseSalaryP4;
	}

	/**
	 * Setter for property baseSalaryP5.
	 * 
	 * @param baseSalaryP5
	 *            New value of property baseSalaryP5.
	 */
	public void setBaseSalaryP5(double baseSalaryP5) {
		this.baseSalaryP5 = baseSalaryP5;
	}

	/**
	 * Setter for property baseSalaryP6.
	 * 
	 * @param baseSalaryP6
	 *            New value of property baseSalaryP6.
	 */
	public void setBaseSalaryP6(double baseSalaryP6) {
		this.baseSalaryP6 = baseSalaryP6;
	}

	/**
	 * Setter for property baseSalaryP7.
	 * 
	 * @param baseSalaryP7
	 *            New value of property baseSalaryP7.
	 */
	public void setBaseSalaryP7(double baseSalaryP7) {
		this.baseSalaryP7 = baseSalaryP7;
	}

	/**
	 * Setter for property baseSalaryP8.
	 * 
	 * @param baseSalaryP8
	 *            New value of property baseSalaryP8.
	 */
	public void setBaseSalaryP8(double baseSalaryP8) {
		this.baseSalaryP8 = baseSalaryP8;
	}

	/**
	 * Setter for property baseSalaryP9.
	 * 
	 * @param baseSalaryP9
	 *            New value of property baseSalaryP9.
	 */
	public void setBaseSalaryP9(double baseSalaryP9) {
		this.baseSalaryP9 = baseSalaryP9;
	}

	/**
	 * Setter for property calculationBase.
	 * 
	 * @param calculationBase
	 *            New value of property calculationBase.
	 */
	public void setCalculationBase(double calculationBase) {
		this.calculationBase = calculationBase;
	}

	/**
	 * Setter for property effectiveDate.
	 * 
	 * @param effectiveDate
	 *            New value of property effectiveDate.
	 */
	public void setEffectiveDate(java.sql.Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	/**
	 * Setter for property fullName.
	 * 
	 * @param fullName
	 *            New value of property fullName.
	 */
	public void setFullName(java.lang.String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Setter for property jobCode.
	 * 
	 * @param jobCode
	 *            New value of property jobCode.
	 */
	public void setJobCode(java.lang.String jobCode) {
		this.jobCode = jobCode;
	}

	public void setNonEmployee(boolean nonEmployee) {
		this.nonEmployee = nonEmployee;
	}

	/**
	 * Setter for property personId.
	 * 
	 * @param personId
	 *            New value of property personId.
	 */
	public void setPersonId(java.lang.String personId) {
		this.personId = personId;
	}

	/* JM 9-4-2015 added person status */
	public void setPersonStatus(String personStatus) {
		this.personStatus = personStatus;
	}
	/* JM END */
	
	/* JM 4-4-2016 added external person */
	public void setIsExternalPerson(String isExternalPerson) {
		this.isExternalPerson = isExternalPerson;
	}
	/* JM END */

	/**
	 * Setter for property rowId.
	 * 
	 * @param rowId
	 *            New value of property rowId.
	 */
	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	public void setSalaryAnniversaryDate(Date salaryAnniversaryDate) {
		this.salaryAnniversaryDate = salaryAnniversaryDate;
	}

	/**
	 * Setter for property active status.
	 * 
	 * @param active
	 *            status New value of property active status.
	 *
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	// Added for COEUSQA-3309 Inactive Appointment Type and Period Types error
	// validation alert required in Budget - end

	/**
	 * Overridden method of toString. It will form a string representation of
	 * each element associated with this class.
	 * 
	 * @return Concatinated string representation of each element
	 */
	@Override
	public String toString() {
		StringBuffer strBffr = new StringBuffer("");
		strBffr.append("Proposal Number =>" + this.getProposalNumber());
		strBffr.append(";");
		strBffr.append("Version Name =>" + this.getVersionNumber());
		strBffr.append(";");
		strBffr.append("Row ID  =>" + rowId);
		strBffr.append(";");
		strBffr.append("Person Id  =>" + personId);
		strBffr.append(";");
		strBffr.append("Full Name  =>" + fullName);
		strBffr.append(";");
		strBffr.append("Job Code  =>" + jobCode);
		strBffr.append(";");
		strBffr.append("Effective Date  =>" + effectiveDate);
		strBffr.append(";");
		strBffr.append("Calculation Base  =>" + calculationBase);
		strBffr.append(";");
		strBffr.append("Appointment Type  =>" + appointmentType);
		strBffr.append(";");
		// Added for Case#2918 - Use of Salary Anniversary Date for calculating
		// inflation in budget development module - starts
		strBffr.append("Salary Anniversary Date Type  =>" + salaryAnniversaryDate);
		strBffr.append(";");
		// Added for Case#2918 - Use of Salary Anniversary Date for calculating
		// inflation in budget development module - ends
		strBffr.append("Aw Proposal Number  =>" + aw_ProposalNumber);
		strBffr.append(";");
		strBffr.append("Aw Version Name =>" + aw_VersionNumber);
		strBffr.append(";");
		strBffr.append("Aw Person Id  =>" + aw_PersonId);
		strBffr.append(";");
		strBffr.append("Aw Job Code  =>" + aw_JobCode);
		strBffr.append(";");
		strBffr.append("Aw Effective Date  =>" + aw_EffectiveDate);
		strBffr.append(";");
		strBffr.append("Aw Calculation Base  =>" + aw_CalculationBase);
		strBffr.append(";");
		strBffr.append("Aw Appointment Type  =>" + aw_AppointmentType);
		strBffr.append(";");
		// Added for Case#2918 - Use of Salary Anniversary Date for calculating
		// inflation in budget development module - starts
		strBffr.append("Aw Salary Anniversary Date Type  =>" + aw_SalaryAnniversaryDate);
		strBffr.append(";");
		// Added for Case#2918 - Use of Salary Anniversary Date for calculating
		// inflation in budget development module - ends
		strBffr.append("Update User =>" + this.getUpdateUser());
		strBffr.append(";");
		strBffr.append("Update Time Stamp =>" + this.getUpdateTimestamp());
		strBffr.append(";");
		strBffr.append("AcType =>" + this.getAcType());
		strBffr.append("\n");
		return strBffr.toString();
	}
}