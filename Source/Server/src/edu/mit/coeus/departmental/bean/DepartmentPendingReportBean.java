/*
 * DepartmentCurrentReportBean.java
 *
 * Created on November 3, 2004, 2:31 PM
 */
/*
 * PMD check performed, and commented unused imports and variables on 19-APR-2011
 * by Maharaja Palanichamy
 */
package edu.mit.coeus.departmental.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;

import java.sql.Date;

/**
 *
 * @author  shivakumarmj
 */
public class DepartmentPendingReportBean implements java.io.Serializable, BaseBean{
    
    //holds the proposal number
     private String proposalNumber;
    
     private String sponsorName;
     
     private boolean principleInvestigatorFlag;
     
     private String title;
     
     private Date requestEndDateTotal;
     
     private Date requestedStartDateTotal;
     
     private double totalDirectCostTotal;
     
     private double totalIndirectCostTotal;
     
     private String personFullName;
     
     private double totalCost;
     
     private String principleInvestigator;     
     
     //Added for case 3505:Add % effort to current and Pending Report - Start
     //holds the percentage effort
     private float percentageEffort;
     //Holds Academic Year
     private float academicYearEffort;
     //Holds Summer Effort
     private float summerYearEffort;
     //Holds Calendar Year
     private float calendarYearEffort;
     //Added for case 3505:Add % effort to current and Pending Report - End
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
     //holds the group code
     private String groupCode;
     //holds the custom data
     private CoeusVector cvCustomData;
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    /** Creates a new instance of DepartmentCurrentReportBean */
    public DepartmentPendingReportBean() {
    }
    
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     *
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     *
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property sponsorName.
     * @return Value of property sponsorName.
     *
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /** Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     *
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /** Getter for property principleInvestigatorFlag.
     * @return Value of property principleInvestigatorFlag.
     *
     */
    public boolean isPrincipleInvestigatorFlag() {
        return principleInvestigatorFlag;
    }
    
    /** Setter for property principleInvestigatorFlag.
     * @param principleInvestigatorFlag New value of property principleInvestigatorFlag.
     *
     */
    public void setPrincipleInvestigatorFlag(boolean principleInvestigatorFlag) {
        this.principleInvestigatorFlag = principleInvestigatorFlag;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     *
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     *
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /** Getter for property requestEndDateTotal.
     * @return Value of property requestEndDateTotal.
     *
     */
    public Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }
    
    /** Setter for property requestEndDateTotal.
     * @param requestEndDateTotal New value of property requestEndDateTotal.
     *
     */
    public void setRequestEndDateTotal(Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }
    
    /** Getter for property requestedStartDateTotal.
     * @return Value of property requestedStartDateTotal.
     *
     */
    public Date getRequestedStartDateTotal() {
        return requestedStartDateTotal;
    }
    
    /** Setter for property requestedStartDateTotal.
     * @param requestedStartDateTotal New value of property requestedStartDateTotal.
     *
     */
    public void setRequestedStartDateTotal(Date requestedStartDateTotal) {
        this.requestedStartDateTotal = requestedStartDateTotal;
    }
    
    /** Getter for property totalDirectCostTotal.
     * @return Value of property totalDirectCostTotal.
     *
     */
    public double getTotalDirectCostTotal() {
        return totalDirectCostTotal;
    }    
    
    /** Setter for property totalDirectCostTotal.
     * @param totalDirectCostTotal New value of property totalDirectCostTotal.
     *
     */
    public void setTotalDirectCostTotal(double totalDirectCostTotal) {
        this.totalDirectCostTotal = totalDirectCostTotal;
    }    
   
    /** Getter for property totalIndirectCostTotal.
     * @return Value of property totalIndirectCostTotal.
     *
     */
    public double getTotalIndirectCostTotal() {
        return totalIndirectCostTotal;
    }    
    
    /** Setter for property totalIndirectCostTotal.
     * @param totalIndirectCostTotal New value of property totalIndirectCostTotal.
     *
     */
    public void setTotalIndirectCostTotal(double totalIndirectCostTotal) {
        this.totalIndirectCostTotal = totalIndirectCostTotal;
    }
    
    /** Getter for property personFullName.
     * @return Value of property personFullName.
     *
     */
    public java.lang.String getPersonFullName() {
        return personFullName;
    }
    
    /** Setter for property personFullName.
     * @param personFullName New value of property personFullName.
     *
     */
    public void setPersonFullName(java.lang.String personFullName) {
        this.personFullName = personFullName;
    }
    
    /** Getter for property totalCost.
     * @return Value of property totalCost.
     *
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /** Setter for property totalCost.
     * @param totalCost New value of property totalCost.
     *
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    /** Getter for property principleInvestigator.
     * @return Value of property principleInvestigator.
     *
     */
    public java.lang.String getPrincipleInvestigator() {
        return principleInvestigator;
    }
    
    /** Setter for property principleInvestigator.
     * @param principleInvestigator New value of property principleInvestigator.
     *
     */
    public void setPrincipleInvestigator(java.lang.String principleInvestigator) {
        this.principleInvestigator = principleInvestigator;
    }
    //Added for case 3505:Add % effort to current and Pending Report - Start
    
    /** Getter for property academicYearEffort.
     * @return float Value of property academicYearEffort.
     *
     */
    public float getAcademicYearEffort() {
        return academicYearEffort;
    }
    /** Getter for property calendarYearEffort.
     * @return float Value of property calendarYearEffort.
     *
     */
    public float getCalendarYearEffort() {
        return calendarYearEffort;
    }
     /** Getter for property percentageEffort.
     * @return float Value of property percentageEffort.
     *
     */
    public float getPercentageEffort() {
        return percentageEffort;
    }
    /** Getter for property summerYearEffort.
     * @return float Value of property summerYearEffort.
     *
     */
    public float getSummerYearEffort() {
        return summerYearEffort;
    }
    /** Setter for property academicYearEffort.
     * @param float academicYearEffort: New value of property academicYearEffort.
     *
     */
    public void setAcademicYearEffort(float academicYearEffort) {
        this.academicYearEffort = academicYearEffort;
    }
    /** Setter for property calendarYearEffort.
     * @param float calendarYearEffort: New value of property calendarYearEffort.
     *
     */
    public void setCalendarYearEffort(float calendarYearEffort) {
        this.calendarYearEffort = calendarYearEffort;
    }
     /** Setter for property percentageEffort.
     * @param float percentageEffort: New value of property percentageEffort.
     *
     */
    public void setPercentageEffort(float percentageEffort) {
        this.percentageEffort = percentageEffort;
    }
     /** Setter for property summerYearEffort.
     * @param float summerYearEffort: New value of property summerYearEffort.
     *
     */
    public void setSummerYearEffort(float summerYearEffort) {
        this.summerYearEffort = summerYearEffort;
    }
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** Getter for property groupCode.
     * @return Value of property groupCode.
     *
     */
    public java.lang.String getGroupCode() {
        return groupCode;
    }
    
    /** Setter for property groupCode.
     * @param groupCode New value of property groupCode.
     *
     */
    public void setGroupCode(java.lang.String groupCode) {
        this.groupCode = groupCode;
    }
    
    /** Getter for property cvCustomData.
     * @return Value of property cvCustomData.
     *
     */
    public CoeusVector getCustomElements() {
        return cvCustomData;
    }    
    
    /** Setter for property customColumnName.
     * @param cvCustomData New value of property cvCustomData.
     *
     */
    public void setCustomElements(CoeusVector cvCustomData) {
        this.cvCustomData = cvCustomData;
    }
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    //Added for case 3505:Add % effort to current and Pending Report - End
}
