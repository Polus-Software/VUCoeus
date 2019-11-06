/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.beans;

import java.sql.Date;

/**
 *
 * @author midhunmk
 */
public class AwardHierarchyPrintBean {

    private String mitAwardNumber;
    private int levelNumber;
    private String accountNumber;
    private int statusCode;
    private String statusDesc;
    private String sponsorCode;
    private Integer accountTypeCode;
    private String accountTypeDesc;
    private String PIName;
    private String leadUnit;
    private String totalObliAmount;
    private String totalAntiAmount;
    private java.sql.Date finalExpDate;

    public String getPIName() {
        return PIName;
    }

    public void setPIName(String PIName) {
        this.PIName = PIName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(Integer accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountTypeDesc() {
        return accountTypeDesc;
    }

    public void setAccountTypeDesc(String accountTypeDesc) {
        this.accountTypeDesc = accountTypeDesc;
    }

    public Date getFinalExpDate() {
        return finalExpDate;
    }

    public void setFinalExpDate(Date finalExpDate) {
        this.finalExpDate = finalExpDate;
    }

    public String getLeadUnit() {
        return leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getSponsorCode() {
        return sponsorCode;
    }

    public void setSponsorCode(String sponsorCode) {
        this.sponsorCode = sponsorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getTotalAntiAmount() {
        return totalAntiAmount;
    }

    public void setTotalAntiAmount(String totalAntiAmount) {
        this.totalAntiAmount = totalAntiAmount;
    }

    public String getTotalObliAmount() {
        return totalObliAmount;
    }

    public void setTotalObliAmount(String totalObliAmount) {
        this.totalObliAmount = totalObliAmount;
    }
    

    /**
     * @return the mitAwardNumber
     */
    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    /**
     * @param mitAwardNumber the mitAwardNumber to set
     */
    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }

   
}
