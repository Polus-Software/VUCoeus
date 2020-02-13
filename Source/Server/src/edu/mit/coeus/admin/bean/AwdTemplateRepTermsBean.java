/*
 * AwdTemplateRepTermsBean.java
 *
 * Created on December 28, 2004, 12:11 PM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.sql.Date;

/**
 *
 * @author  ajaygm
 */

public class AwdTemplateRepTermsBean extends TemplateBaseBean implements
java.io.Serializable {
    
    private int reportClassCode;
    private int reportCode;
    private int frequencyCode;
    private int frequencyBaseCode;
    private int ospDistributionCode;
    private int contactTypeCode;
    private int rolodexId;
    private Date dueDate;
    private int numberOfCopies;
    private int rowId;
    private String lastName;
    private String firstName;
    private String middleName;
    private String organization;
    private String suffix;
    private String prefix;
    private int aw_ReportClassCode;
    private int aw_ReportCode;
    private int aw_FrequencyCode;
    private int aw_FrequencyBaseCode;
    private int aw_OspDistributionCode;
    private int aw_ContactTypeCode;
    private int aw_RolodexId;
    private Date aw_DueDate;
    private String reportDescription;
    private String frequencyDescription;
    private String ospDistributionDescription;
    private String frequencyBaseDescription;
    private boolean finalReport;
    private String contactTypeDescription;
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    private String updateUserName;
    //COEUSQA-1456 : End
    
    /** Creates a new instance of AwdTemplateRepTermsBean */
    public AwdTemplateRepTermsBean() {
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws
    edu.mit.coeus.exception.CoeusException {
        return true;
    }
    
    /**
     * Getter for property reportClassCode.
     * @return Value of property reportClassCode.
     */
    public int getReportClassCode() {
        return reportClassCode;
    }
    
    /**
     * Setter for property reportClassCode.
     * @param reportClassCode New value of property reportClassCode.
     */
    public void setReportClassCode(int reportClassCode) {
        this.reportClassCode = reportClassCode;
    }
    
    /**
     * Getter for property reportCode.
     * @return Value of property reportCode.
     */
    public int getReportCode() {
        return reportCode;
    }
    
    /**
     * Setter for property reportCode.
     * @param reportCode New value of property reportCode.
     */
    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }
    
    /**
     * Getter for property frequencyCode.
     * @return Value of property frequencyCode.
     */
    public int getFrequencyCode() {
        return frequencyCode;
    }
    
    /**
     * Setter for property frequencyCode.
     * @param frequencyCode New value of property frequencyCode.
     */
    public void setFrequencyCode(int frequencyCode) {
        this.frequencyCode = frequencyCode;
    }
    
    /**
     * Getter for property frequencyBaseCode.
     * @return Value of property frequencyBaseCode.
     */
    public int getFrequencyBaseCode() {
        return frequencyBaseCode;
    }
    
    /**
     * Setter for property frequencyBaseCode.
     * @param frequencyBaseCode New value of property frequencyBaseCode.
     */
    public void setFrequencyBaseCode(int frequencyBaseCode) {
        this.frequencyBaseCode = frequencyBaseCode;
    }
    
    /**
     * Getter for property ospDistributionCode.
     * @return Value of property ospDistributionCode.
     */
    public int getOspDistributionCode() {
        return ospDistributionCode;
    }
    
    /**
     * Setter for property ospDistributionCode.
     * @param ospDistributionCode New value of property ospDistributionCode.
     */
    public void setOspDistributionCode(int ospDistributionCode) {
        this.ospDistributionCode = ospDistributionCode;
    }
    
    /**
     * Getter for property contactTypeCode.
     * @return Value of property contactTypeCode.
     */
    public int getContactTypeCode() {
        return contactTypeCode;
    }
    
    /**
     * Setter for property contactTypeCode.
     * @param contactTypeCode New value of property contactTypeCode.
     */
    public void setContactTypeCode(int contactTypeCode) {
        this.contactTypeCode = contactTypeCode;
    }
    
    /**
     * Getter for property rolodexId.
     * @return Value of property rolodexId.
     */
    public int getRolodexId() {
        return rolodexId;
    }
    
    /**
     * Setter for property rolodexId.
     * @param rolodexId New value of property rolodexId.
     */
    public void setRolodexId(int rolodexId) {
        this.rolodexId = rolodexId;
    }
    
    /**
     * Getter for property dueDate.
     * @return Value of property dueDate.
     */
    public java.sql.Date getDueDate() {
        return dueDate;
    }
    
    /**
     * Setter for property dueDate.
     * @param dueDate New value of property dueDate.
     */
    public void setDueDate(java.sql.Date dueDate) {
        this.dueDate = dueDate;
    }
    
    /**
     * Getter for property numberOfCopies.
     * @return Value of property numberOfCopies.
     */
    public int getNumberOfCopies() {
        return numberOfCopies;
    }
    
    /**
     * Setter for property numberOfCopies.
     * @param numberOfCopies New value of property numberOfCopies.
     */
    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }
    
    /**
     * Getter for property rowId.
     * @return Value of property rowId.
     */
    public int getRowId() {
        return rowId;
    }
    
    /**
     * Setter for property rowId.
     * @param rowId New value of property rowId.
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    /**
     * Getter for property lastName.
     * @return Value of property lastName.
     */
    public java.lang.String getLastName() {
        return lastName;
    }
    
    /**
     * Setter for property lastName.
     * @param lastName New value of property lastName.
     */
    public void setLastName(java.lang.String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Getter for property firstName.
     * @return Value of property firstName.
     */
    public java.lang.String getFirstName() {
        return firstName;
    }
    
    /**
     * Setter for property firstName.
     * @param firstName New value of property firstName.
     */
    public void setFirstName(java.lang.String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Getter for property middleName.
     * @return Value of property middleName.
     */
    public java.lang.String getMiddleName() {
        return middleName;
    }
    
    /**
     * Setter for property middleName.
     * @param middleName New value of property middleName.
     */
    public void setMiddleName(java.lang.String middleName) {
        this.middleName = middleName;
    }
    
    /**
     * Getter for property organization.
     * @return Value of property organization.
     */
    public java.lang.String getOrganization() {
        return organization;
    }
    
    /**
     * Setter for property organization.
     * @param organization New value of property organization.
     */
    public void setOrganization(java.lang.String organization) {
        this.organization = organization;
    }
    
    /**
     * Getter for property suffix.
     * @return Value of property suffix.
     */
    public java.lang.String getSuffix() {
        return suffix;
    }
    
    /**
     * Setter for property suffix.
     * @param suffix New value of property suffix.
     */
    public void setSuffix(java.lang.String suffix) {
        this.suffix = suffix;
    }
    
    /**
     * Getter for property prefix.
     * @return Value of property prefix.
     */
    public java.lang.String getPrefix() {
        return prefix;
    }
    
    /**
     * Setter for property prefix.
     * @param prefix New value of property prefix.
     */
    public void setPrefix(java.lang.String prefix) {
        this.prefix = prefix;
    }
    
    /**
     * Getter for property aw_ReportClassCode.
     * @return Value of property aw_ReportClassCode.
     */
    public int getAw_ReportClassCode() {
        return aw_ReportClassCode;
    }
    
    /**
     * Setter for property aw_ReportClassCode.
     * @param aw_ReportClassCode New value of property aw_ReportClassCode.
     */
    public void setAw_ReportClassCode(int aw_ReportClassCode) {
        this.aw_ReportClassCode = aw_ReportClassCode;
    }
    
    /**
     * Getter for property aw_ReportCode.
     * @return Value of property aw_ReportCode.
     */
    public int getAw_ReportCode() {
        return aw_ReportCode;
    }
    
    /**
     * Setter for property aw_ReportCode.
     * @param aw_ReportCode New value of property aw_ReportCode.
     */
    public void setAw_ReportCode(int aw_ReportCode) {
        this.aw_ReportCode = aw_ReportCode;
    }
    
    /**
     * Getter for property aw_FrequencyCode.
     * @return Value of property aw_FrequencyCode.
     */
    public int getAw_FrequencyCode() {
        return aw_FrequencyCode;
    }
    
    /**
     * Setter for property aw_FrequencyCode.
     * @param aw_FrequencyCode New value of property aw_FrequencyCode.
     */
    public void setAw_FrequencyCode(int aw_FrequencyCode) {
        this.aw_FrequencyCode = aw_FrequencyCode;
    }
    
    /**
     * Getter for property aw_FrequencyBaseCode.
     * @return Value of property aw_FrequencyBaseCode.
     */
    public int getAw_FrequencyBaseCode() {
        return aw_FrequencyBaseCode;
    }
    
    /**
     * Setter for property aw_FrequencyBaseCode.
     * @param aw_FrequencyBaseCode New value of property aw_FrequencyBaseCode.
     */
    public void setAw_FrequencyBaseCode(int aw_FrequencyBaseCode) {
        this.aw_FrequencyBaseCode = aw_FrequencyBaseCode;
    }
    
    /**
     * Getter for property aw_OspDistributionCode.
     * @return Value of property aw_OspDistributionCode.
     */
    public int getAw_OspDistributionCode() {
        return aw_OspDistributionCode;
    }
    
    /**
     * Setter for property aw_OspDistributionCode.
     * @param aw_OspDistributionCode New value of property aw_OspDistributionCode.
     */
    public void setAw_OspDistributionCode(int aw_OspDistributionCode) {
        this.aw_OspDistributionCode = aw_OspDistributionCode;
    }
    
    /**
     * Getter for property aw_ContactTypeCode.
     * @return Value of property aw_ContactTypeCode.
     */
    public int getAw_ContactTypeCode() {
        return aw_ContactTypeCode;
    }
    
    /**
     * Setter for property aw_ContactTypeCode.
     * @param aw_ContactTypeCode New value of property aw_ContactTypeCode.
     */
    public void setAw_ContactTypeCode(int aw_ContactTypeCode) {
        this.aw_ContactTypeCode = aw_ContactTypeCode;
    }
    
    /**
     * Getter for property aw_RolodexId.
     * @return Value of property aw_RolodexId.
     */
    public int getAw_RolodexId() {
        return aw_RolodexId;
    }
    
    /**
     * Setter for property aw_RolodexId.
     * @param aw_RolodexId New value of property aw_RolodexId.
     */
    public void setAw_RolodexId(int aw_RolodexId) {
        this.aw_RolodexId = aw_RolodexId;
    }
    
    /**
     * Getter for property aw_DueDate.
     * @return Value of property aw_DueDate.
     */
    public java.sql.Date getAw_DueDate() {
        return aw_DueDate;
    }
    
    /**
     * Setter for property aw_DueDate.
     * @param aw_DueDate New value of property aw_DueDate.
     */
    public void setAw_DueDate(java.sql.Date aw_DueDate) {
        this.aw_DueDate = aw_DueDate;
    }
    
    /**
     * Getter for property reportDescription.
     * @return Value of property reportDescription.
     */
    public java.lang.String getReportDescription() {
        return reportDescription;
    }
    
    /**
     * Setter for property reportDescription.
     * @param reportDescription New value of property reportDescription.
     */
    public void setReportDescription(java.lang.String reportDescription) {
        this.reportDescription = reportDescription;
    }
    
    /**
     * Getter for property frequencyDescription.
     * @return Value of property frequencyDescription.
     */
    public java.lang.String getFrequencyDescription() {
        return frequencyDescription;
    }
    
    /**
     * Setter for property frequencyDescription.
     * @param frequencyDescription New value of property frequencyDescription.
     */
    public void setFrequencyDescription(java.lang.String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
    }
    
    /**
     * Getter for property ospDistributionDescription.
     * @return Value of property ospDistributionDescription.
     */
    public java.lang.String getOspDistributionDescription() {
        return ospDistributionDescription;
    }
    
    /**
     * Setter for property ospDistributionDescription.
     * @param ospDistributionDescription New value of property ospDistributionDescription.
     */
    public void setOspDistributionDescription(java.lang.String ospDistributionDescription) {
        this.ospDistributionDescription = ospDistributionDescription;
    }
    
    /**
     * Getter for property frequencyBaseDescription.
     * @return Value of property frequencyBaseDescription.
     */
    public java.lang.String getFrequencyBaseDescription() {
        return frequencyBaseDescription;
    }
    
    /**
     * Setter for property frequencyBaseDescription.
     * @param frequencyBaseDescription New value of property frequencyBaseDescription.
     */
    public void setFrequencyBaseDescription(java.lang.String frequencyBaseDescription) {
        this.frequencyBaseDescription = frequencyBaseDescription;
    }
    
    /**
     * Getter for property finalReport.
     * @return Value of property finalReport.
     */
    public boolean isFinalReport() {
        return finalReport;
    }
    
    /**
     * Setter for property finalReport.
     * @param finalReport New value of property finalReport.
     */
    public void setFinalReport(boolean finalReport) {
        this.finalReport = finalReport;
    }
    
    /**
     * Getter for property contactTypeDescription.
     * @return Value of property contactTypeDescription.
     */
    public java.lang.String getContactTypeDescription() {
        return contactTypeDescription;
    }
    
    /**
     * Setter for property contactTypeDescription.
     * @param contactTypeDescription New value of property contactTypeDescription.
     */
    public void setContactTypeDescription(java.lang.String contactTypeDescription) {
        this.contactTypeDescription = contactTypeDescription;
    }
    
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        if(super.equals(obj)){
            AwdTemplateRepTermsBean awdTemplateRepTermsBean= (AwdTemplateRepTermsBean)obj;
            if(awdTemplateRepTermsBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }  
    //Added for COEUSQA-1456 : Templates-add User ID stamp & Timestamp - Start
    /**
     * Getter for property updateUser.
     * @return updateUser.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //COEUSQA-1456 : End
    
}//End of class
