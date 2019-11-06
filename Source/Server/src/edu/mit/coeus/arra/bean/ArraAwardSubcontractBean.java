/*
 * ArraAwardSubcontractBean.java
 *
 * Created on September 21, 2009, 2:09 PM
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
public class ArraAwardSubcontractBean extends ArraAwardBaseBean{
    
    private String subcontractCode;
    private String subcontractorID;
    private String subcontractorName;
    private String subSequenceNo;
    private String subRecipientDUNS;
    private String subcontractNo;
    private String subRecipientCongDist;
    private double subAwardAmount;
    private double subAwardAmtDispursed;
    private String strSubAwardAmount;
    private String strSubAwardAmtDispursed;
    private Date subAwardDate;
    private String primPlaceOfPerfId;
    private String primPlaceCongDist;
    private String reportingIndication;
    private double noOfJobs;
    private String employmentImpact;
    private String indicationOfReporting;
    /** Creates a new instance of ArraAwardSubcontractBean */
    public ArraAwardSubcontractBean() {
    }
    
    public String getEmploymentImpact() {
        return employmentImpact;
    }
    
    public double getNoOfJobs() {
        return noOfJobs;
    }
    
    public String getPrimPlaceCongDist() {
        return primPlaceCongDist;
    }
    
    public String getPrimPlaceOfPerfId() {
        return primPlaceOfPerfId;
    }
    
    public String getReportingIndication() {
        return reportingIndication;
    }
    
    public double getSubAwardAmount() {
        return subAwardAmount;
    }
    public String getStrSubAwardAmount() {
        return strSubAwardAmount;
    }
    
    public double getSubAwardAmtDispursed() {
        return subAwardAmtDispursed;
    }
    public String getStrSubAwardAmtDispursed() {
        return strSubAwardAmtDispursed;
    }
    
    public Date getSubAwardDate() {
        return subAwardDate;
    }
    
    public String getSubRecipientCongDist() {
        return subRecipientCongDist;
    }
    
    public String getSubRecipientDUNS() {
        return subRecipientDUNS;
    }
    
    public String getSubSequenceNo() {
        return subSequenceNo;
    }
    
    public String getSubcontractCode() {
        return subcontractCode;
    }
    
    public String getSubcontractNo() {
        return subcontractNo;
    }
    
    public String getSubcontractorID() {
        return subcontractorID;
    }
    
    public String getSubcontractorName() {
        return subcontractorName;
    }
    
    public void setEmploymentImpact(String employmentImpact) {
        this.employmentImpact = employmentImpact;
    }
    
    public void setNoOfJobs(double noOfJobs) {
        this.noOfJobs = noOfJobs;
    }
    
    public void setPrimPlaceCongDist(String primPlaceCongDist) {
        this.primPlaceCongDist = primPlaceCongDist;
    }
    
    public void setPrimPlaceOfPerfId(String primPlaceOfPerfId) {
        this.primPlaceOfPerfId = primPlaceOfPerfId;
    }
    
    public void setReportingIndication(String reportingIndication) {
        this.reportingIndication = reportingIndication;
    }
    
    public void setSubAwardAmount(double subAwardAmount) {
        this.subAwardAmount = subAwardAmount;
    }
    public void setStrSubAwardAmount(String strSubAwardAmount) {
        this.strSubAwardAmount = strSubAwardAmount;
    }
    
    
    public void setSubAwardAmtDispursed(double subAwardAmtDispursed) {
        this.subAwardAmtDispursed = subAwardAmtDispursed;
    }
    public void setStrSubAwardAmtDispursed(String strSubAwardAmtDispursed) {
        this.strSubAwardAmtDispursed = strSubAwardAmtDispursed;
    }
    
    public void setSubAwardDate(Date subAwardDate) {
        this.subAwardDate = subAwardDate;
    }
    
    public void setSubRecipientCongDist(String subRecipientCongDist) {
        this.subRecipientCongDist = subRecipientCongDist;
    }
    
    public void setSubRecipientDUNS(String subRecipientDUNS) {
        this.subRecipientDUNS = subRecipientDUNS;
    }
    
    public void setSubSequenceNo(String subSequenceNo) {
        this.subSequenceNo = subSequenceNo;
    }
    
    public void setSubcontractCode(String subcontractCode) {
        this.subcontractCode = subcontractCode;
    }
    
    public void setSubcontractNo(String subcontractNo) {
        this.subcontractNo = subcontractNo;
    }
    
    public void setSubcontractorID(String subcontractorID) {
        this.subcontractorID = subcontractorID;
    }
    
    public void setSubcontractorName(String subcontractorName) {
        this.subcontractorName = subcontractorName;
    }

    public String getIndicationOfReporting() {
        return indicationOfReporting;
    }

    public void setIndicationOfReporting(String indicationOfReporting) {
        this.indicationOfReporting = indicationOfReporting;
    }
    

    
}
