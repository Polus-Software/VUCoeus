/*
 * DepartmentCurrentReportBean.java
 *
 * Created on November 3, 2004, 3:26 PM
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
public class DepartmentCurrentReportBean implements java.io.Serializable, BaseBean{
    
    private String sponsorName;
    
    private String title;
    
    private Date awardEffectiveDate;
    
    private Date finalExpirationDate;
    
    private boolean principleInvestigatorFlag;
    
    private String sponsorAwardNumber;
    
    private double obliDistrubutableAmount;
    
    private String personFullName;
    
    private String prinicipleInvestigator;
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
     //holds the direct amount
     private double obliDirectAmount;
     //holds the indirect amount
     private double obliInDirectAmount;
     //holds the mit award number
     private String mitAwardNumber;
     //holds the group code
     private String groupCode;
     //holds the custom data
     private CoeusVector cvCustomData;
     //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
    /** Creates a new instance of DepartmentCurrentReportBean */
    public DepartmentCurrentReportBean() {
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
    
    /** Getter for property awardEffectiveDate.
     * @return Value of property awardEffectiveDate.
     *
     */
    public java.sql.Date getAwardEffectiveDate() {
        return awardEffectiveDate;
    }
    
    /** Setter for property awardEffectiveDate.
     * @param awardEffectiveDate New value of property awardEffectiveDate.
     *
     */
    public void setAwardEffectiveDate(java.sql.Date awardEffectiveDate) {
        this.awardEffectiveDate = awardEffectiveDate;
    }
    
    /** Getter for property finalExpirationDate.
     * @return Value of property finalExpirationDate.
     *
     */
    public java.sql.Date getFinalExpirationDate() {
        return finalExpirationDate;
    }
    
    /** Setter for property finalExpirationDate.
     * @param finalExpirationDate New value of property finalExpirationDate.
     *
     */
    public void setFinalExpirationDate(java.sql.Date finalExpirationDate) {
        this.finalExpirationDate = finalExpirationDate;
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
    
    /** Getter for property sponsorAwardNumber.
     * @return Value of property sponsorAwardNumber.
     *
     */
    public java.lang.String getSponsorAwardNumber() {
        return sponsorAwardNumber;
    }
    
    /** Setter for property sponsorAwardNumber.
     * @param sponsorAwardNumber New value of property sponsorAwardNumber.
     *
     */
    public void setSponsorAwardNumber(java.lang.String sponsorAwardNumber) {
        this.sponsorAwardNumber = sponsorAwardNumber;
    }
    
    /** Getter for property obliDistrubutableAmount.
     * @return Value of property obliDistrubutableAmount.
     *
     */
    public double getObliDistrubutableAmount() {
        return obliDistrubutableAmount;
    }    
    
    /** Setter for property obliDistrubutableAmount.
     * @param obliDistrubutableAmount New value of property obliDistrubutableAmount.
     *
     */
    public void setObliDistrubutableAmount(double obliDistrubutableAmount) {
        this.obliDistrubutableAmount = obliDistrubutableAmount;
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
    
    /** Getter for property prinicipleInvestigator.
     * @return Value of property prinicipleInvestigator.
     *
     */
    public java.lang.String getPrinicipleInvestigator() {
        return prinicipleInvestigator;
    }
    
    /** Setter for property prinicipleInvestigator.
     * @param prinicipleInvestigator New value of property prinicipleInvestigator.
     *
     */
    public void setPrinicipleInvestigator(java.lang.String prinicipleInvestigator) {
        this.prinicipleInvestigator = prinicipleInvestigator;
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
    //Added for case 3505:Add % effort to current and Pending Report - End
    
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - Start
    /** Getter for property obliDirectAmount.
     * @return Value of property obliDistrubutableAmount.
     *
     */
    public double getObliDirectAmount() {
        return obliDirectAmount;
    }    
    
    /** Setter for property obliDistrubutableAmount.
     * @param obliDistrubutableAmount New value of property obliDistrubutableAmount.
     *
     */
    public void setObliDirectAmount(double obliDirectAmount) {
        this.obliDirectAmount = obliDirectAmount;
    }
    
    /** Getter for property obliDistrubutableAmount.
     * @return Value of property obliDistrubutableAmount.
     *
     */
    public double getObliInDirectAmount() {
        return obliInDirectAmount;
    }    
    
    /** Setter for property obliDistrubutableAmount.
     * @param obliDistrubutableAmount New value of property obliDistrubutableAmount.
     *
     */
    public void setObliInDirectAmount(double obliInDirectAmount) {
        this.obliInDirectAmount = obliInDirectAmount;
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
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     *
     */
    public java.lang.String getMITAwardNumber() {
        return mitAwardNumber;
    }
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     *
     */
    public void setMITAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }
    
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
    //COEUSQA-1686 - Add additional fields to the Current  Pending Support Schema - End
}
