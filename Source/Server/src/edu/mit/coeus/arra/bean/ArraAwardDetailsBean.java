/*
 * ArraAwardDetailsBean.java
 *
 * Created on September 21, 2009, 2:08 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.arra.bean;
import java.sql.Date;
/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardDetailsBean extends ArraAwardBaseBean{
    
    private String awardNumber;
    private int sequenceNumber;
    private String projectStatus;
    private String projectTitle;
    private String activityCode;
    private String awardingAgencyCode;
    private String awardDescription;
    private String activityDescription;
    private double noOfJobs;
    private double jobsAtSubs;
    private double totalJobs;
    private String employmentImpact;
    private String infraContactId;
    private String infrastructureRationale;
    private String primPlaceOfPerfId;
    private String primPlaceCongDistrict;
    private String accountNumber;
    private String awardType;
    private Date awardDate;
    private double awardAmount;
    private String strAwardAmount;
    private double totalFederalInvoiced;
    private double totalExpenditure;  
    private double totalInfraExpenditure;
    private String recipientDUNSNumber;
    private String cfdaNumber;
    private int indSubAwards;
    private double indSubAwardAmount;
    private int vendorLess25K;
    private double vendorLess25KAmount;
    private int subAwdLess25K;
    private double subAwdLess25KAmount;
    private String govContractingOfficeCode;
    private String orderNumber;
    private String recipientCongDistrict;
    private String indicationOfReporting;
    private String fundingAgencyCode;
    private String finalReportIndicator;
    private String agencyTAS;
    private String tasSubCode;
    private Date reportPeriodStart;
    private Date reportPeriodEnd;
    private Date submissionDate;
    private String piName;
    private String sponsorAwardNumber;
    private String leadUnit;
    
    /** Creates a new instance of ArraAwardDetailsBean */
    public ArraAwardDetailsBean() {
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getActivityCode() {
        return activityCode;
    }
    
    public String getActivityDescription() {
        return activityDescription;
    }
    
    public double getAwardAmount() {
        return awardAmount;
    }
    public String getStrAwardAmount() {
        return strAwardAmount;
    }
    public Date getAwardDate() {
        return awardDate;
    }
    public Date getReportPeriodStart() {
        return reportPeriodStart;
    }
    public Date getReportPeriodEnd() {
        return reportPeriodEnd;
    }
    public Date getSubmissionDate() {
        return submissionDate;
    }
    public String getAwardDescription() {
        return awardDescription;
    }
    
    public String getAwardNumber() {
        return awardNumber;
    }
    
    public String getAwardType() {
        return awardType;
    }
    
    public String getAwardingAgencyCode() {
        return awardingAgencyCode;
    }
    
    public String getCfdaNumber() {
        return cfdaNumber;
    }
    
    public String getPrimPlaceCongDistrict() {
        return primPlaceCongDistrict;
    }
    
    public String getEmploymentImpact() {
        return employmentImpact;
    }
    
    public String getGovContractingOfficeCode() {
        return govContractingOfficeCode;
    }
    
    public double getIndSubAwardAmount() {
        return indSubAwardAmount;
    }
    
    public int getIndSubAwards() {
        return indSubAwards;
    }
       
    public String getInfraContactId() {
        return infraContactId;
    }
    
    public String getInfrastructureRationale() {
        return infrastructureRationale;
    }
    
    public double getJobsAtSubs() {
        return jobsAtSubs;
    }
    
    public double getNoOfJobs() {
        return noOfJobs;
    }
        
    public String getPrimPlaceOfPerfId() {
        return primPlaceOfPerfId;
    }
    
    public String getProjectStatus() {
        return projectStatus;
    }
     
    public String getRecipientDUNSNumber() {
        return recipientDUNSNumber;
    }
    
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    public int getSubAwdLess25K() {
        return subAwdLess25K;
    }
    
    public double getSubAwdLess25KAmount() {
        return subAwdLess25KAmount;
    }
    
    public double getTotalExpenditure() {
        return totalExpenditure;
    }
    
    public double getTotalFederalInvoiced() {
        return totalFederalInvoiced;
    }
    
    public double getTotalInfraExpenditure() {
        return totalInfraExpenditure;
    }
    
    public double getTotalJobs() {
        return totalJobs;
    }
    
    public int getVendorLess25K() {
        return vendorLess25K;
    }
    
    public double getVendorLess25KAmount() {
        return vendorLess25KAmount;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
    
    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }
    
    public void setAwardAmount(double awardAmount) {
        this.awardAmount = awardAmount;
    }
    
    public void setStrAwardAmount(String strAwardAmount) {
        this.strAwardAmount = strAwardAmount;
    }
    
    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }
    public void setReportPeriodStart(Date reportPeriodStart) {
        this.reportPeriodStart = reportPeriodStart;
    }
    public void setReportPeriodEnd(Date reportPeriodEnd) {
        this.reportPeriodEnd = reportPeriodEnd;
    }
    public void setSubmissionDate(Date reportPeriodEnd) {
        this.submissionDate = submissionDate;
    }
    public void setAwardDescription(String awardDescription) {
        this.awardDescription = awardDescription;
    }
    
    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }
    
    public void setAwardType(String awardType) {
        this.awardType = awardType;
    }
    
    public void setAwardingAgencyCode(String awardingAgencyCode) {
        this.awardingAgencyCode = awardingAgencyCode;
    }
    
    public void setCfdaNumber(String cfdaNumber) {
        this.cfdaNumber = cfdaNumber;
    }
    
    public void setPrimPlaceCongDistrict(String congressionalDistrict) {
        this.primPlaceCongDistrict = congressionalDistrict;
    }
    
    public void setEmploymentImpact(String employmentImpact) {
        this.employmentImpact = employmentImpact;
    }
    
    public void setGovContractingOfficeCode(String govContractingOfficeCode) {
        this.govContractingOfficeCode = govContractingOfficeCode;
    }
    
    public void setIndSubAwardAmount(double indSubAwardAmount) {
        this.indSubAwardAmount = indSubAwardAmount;
    }
    
    public void setIndSubAwards(int indSubAwards) {
        this.indSubAwards = indSubAwards;
    }
        
//    public void setInfraContactAddress(String infraContactAddress) {
//        this.infraContactAddress = infraContactAddress;
//    }
    
    public void setInfraContactId(String infraContactId) {
        this.infraContactId = infraContactId;
    }
    
    public void setInfrastructureRationale(String infrastructureRationale) {
        this.infrastructureRationale = infrastructureRationale;
    }
    
    public void setJobsAtSubs(double jobsAtSubs) {
        this.jobsAtSubs = jobsAtSubs;
    }
    
    public void setNoOfJobs(double noOfJobs) {
        this.noOfJobs = noOfJobs;
    }
    
//    public void setPrimPlaceOfPerfAddress(String primPlaceOfPerfAddress) {
//        this.primPlaceOfPerfAddress = primPlaceOfPerfAddress;
//    }
    
    public void setPrimPlaceOfPerfId(String primPlaceOfPerfId) {
        this.primPlaceOfPerfId = primPlaceOfPerfId;
    }
    
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }
    
    public void setRecipientDUNSNumber(String recipientDUNSNumber) {
        this.recipientDUNSNumber = recipientDUNSNumber;
    }
    
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    public void setSubAwdLess25K(int subAwdLess25K) {
        this.subAwdLess25K = subAwdLess25K;
    }
    
    public void setSubAwdLess25KAmount(double subAwdLess25KAmount) {
        this.subAwdLess25KAmount = subAwdLess25KAmount;
    }
    
    public void setTotalExpenditure(double totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }
    
    public void setTotalFederalInvoiced(double totalFederalInvoiced) {
        this.totalFederalInvoiced = totalFederalInvoiced;
    }
    
    public void setTotalInfraExpenditure(double totalInfraExpenditure) {
        this.totalInfraExpenditure = totalInfraExpenditure;
    }
    
    public void setTotalJobs(double totalJobs) {
        this.totalJobs = totalJobs;
    }
    
    public void setVendorLess25K(int vendorLess25K) {
        this.vendorLess25K = vendorLess25K;
    }
    
    public void setVendorLess25KAmount(double vendorLess25KAmount) {
        this.vendorLess25KAmount = vendorLess25KAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRecipientCongDistrict() {
        return recipientCongDistrict;
    }

    public void setRecipientCongDistrict(String recipientCongDistrict) {
        this.recipientCongDistrict = recipientCongDistrict;
    }

    public String getIndicationOfReporting() {
        return indicationOfReporting;
    }

    public void setIndicationOfReporting(String indicationOfReporting) {
        this.indicationOfReporting = indicationOfReporting;
    }

    public String getFundingAgencyCode() {
        return fundingAgencyCode;
    }

    public void setFundingAgencyCode(String fundingAgencyCode) {
        this.fundingAgencyCode = fundingAgencyCode;
    }

    public String getFinalReportIndicator() {
        return finalReportIndicator;
    }

    public void setFinalReportIndicator(String finalReportIndicator) {
        this.finalReportIndicator = finalReportIndicator;
    }

    public String getAgencyTAS() {
        return agencyTAS;
    }

    public void setAgencyTAS(String agencyTAS) {
        this.agencyTAS = agencyTAS;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    /**
     * @return the tasSubCode
     */
    public String getTasSubCode() {
        return tasSubCode;
    }

    /**
     * @param tasSubCode the tasSubCode to set
     */
    public void setTasSubCode(String tasSubCode) {
        this.tasSubCode = tasSubCode;
    }

    public String getPiName() {
        return piName;
    }

    public void setPiName(String piName) {
        this.piName = piName;
    }

    public String getSponsorAwardNumber() {
        return sponsorAwardNumber;
    }

    public void setSponsorAwardNumber(String sponsorAwardNumber) {
        this.sponsorAwardNumber = sponsorAwardNumber;
    }

    public String getLeadUnit() {
        return leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }
 
}
