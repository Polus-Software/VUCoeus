/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.beans;

/**
 *
 * @author midhunmk
 */
public class AwardHirarchyBean {

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    public String getEdDate() {
        return edDate;
    }

    public void setEdDate(String edDate) {
        this.edDate = edDate;
    }

    public String getLeadUnit() {
        return leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    public String getpI() {
        return pI;
    }

    public void setpI(String pI) {
        this.pI = pI;
    }

    public String getParentAwdNum() {
        return parentAwdNum;
    }

    public void setParentAwdNum(String parentAwdNum) {
        this.parentAwdNum = parentAwdNum;
    }

    public String getRootAward() {
        return rootAward;
    }

    public void setRootAward(String rootAward) {
        this.rootAward = rootAward;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorAwdNum() {
        return sponsorAwdNum;
    }

    public void setSponsorAwdNum(String sponsorAwdNum) {
        this.sponsorAwdNum = sponsorAwdNum;
    }

    public String getStDate() {
        return stDate;
    }

    public void setStDate(String stDate) {
        this.stDate = stDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private String awardNumber;
    private String accountNumber;
    private String sequenceNumber;
    private String status;
    private String pI;
    private String leadUnit;
    private String sponsor;
    private String sponsorAwdNum;
    private String stDate;
    private String edDate;
    private String parentAwdNum;
    private String rootAward;

}
