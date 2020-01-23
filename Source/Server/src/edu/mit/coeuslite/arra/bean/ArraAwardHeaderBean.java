/*
 * ArraAwardHeaderBean.java
 *
 * Created on August 26, 2009, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeuslite.arra.bean;

import java.sql.Date;

/**
 *
 * @author keerthyjayaraj
 */
public class ArraAwardHeaderBean implements java.io.Serializable {
    
    private int arraReportNumber;
    private int versionNumber;
    private String mitAwardNumber;
     private String awardNumber;
    private String piPersonId;
    private String piPersonName;
    private Date projectStartDate;
    private Date projectEndDate;
    private String sponsorCode;
    private String sponsorName;
    private String leadUnitNumber;
    private String leadUnitName;
    private String title;
    //private boolean complete;
    private String complete;
    private String fundingAgencyCode;
    private String accountNumber;
    private Date awardDate;
    private String updateTimestamp;
    private String updateUser;
    private String updateUserName;
    //Arra Phase II changes
    private boolean finalReportFlag;
    /** Creates a new instance of ArraAwardHeaderBean */
    public ArraAwardHeaderBean() {
    }

    public int getArraReportNumber() {
        return arraReportNumber;
    }

    public String getLeadUnitName() {
        return leadUnitName;
    }

    public String getLeadUnitNumber() {
        return leadUnitNumber;
    }

    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    public String getPiPersonId() {
        return piPersonId;
    }

    public String getPiPersonName() {
        return piPersonName;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public int getVersionNumber() {
        return versionNumber;
    }
    public String getComplete() {
        return complete;
    }

    public void setArraReportNumber(int arraReportNumber) {
        this.arraReportNumber = arraReportNumber;
    }

    public void setLeadUnitName(String leadUnitName) {
        this.leadUnitName = leadUnitName;
    }

    public void setLeadUnitNumber(String leadUnitNumber) {
        this.leadUnitNumber = leadUnitNumber;
    }

    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }

    public void setPiPersonId(String piPersonId) {
        this.piPersonId = piPersonId;
    }

    public void setPiPersonName(String piPersonName) {
        this.piPersonName = piPersonName;
    }

    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

//    public boolean isComplete() {
//        return complete;
//    }
//
//    public void setComplete(boolean complete) {
//        this.complete = complete;
//    }

    
    public boolean isFinalReportFlag() {
        return finalReportFlag;
    }

    public void setFinalReportFlag(boolean finalReportFlag) {
        this.finalReportFlag = finalReportFlag;
    }
    public String getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    public String getFundingAgencyCode() {
        return fundingAgencyCode;
    }

    public void setFundingAgencyCode(String fundingAgencyCode) {
        this.fundingAgencyCode = fundingAgencyCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }
    public void setComplete(String complete) {
        this.complete = complete;
    }
}
