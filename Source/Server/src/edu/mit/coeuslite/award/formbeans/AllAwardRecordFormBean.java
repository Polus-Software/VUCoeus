/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.formbeans;

import java.util.Date;


/**
 *
 * @author indhulekha
 */
public class AllAwardRecordFormBean  {
    
    private String awardNumber;

    private String accntNumber;

    private String status;
    private String awardtype;


    private String PI;
    private String title;
    private String leasdUnit;
    private String sponsor;
    private String sponsorAwardNum;
    private Date startDate;
    private Date endDate;
    private Integer anticipatedAmnt;
    private Integer roleId;
 private String accountType;
  private String ActivityType;
  private Date FinalExpDate;
  private Date AwardEffDate;
    private Integer obligatedAmnt;

    public Integer getObligatedAmnt() {
        return obligatedAmnt;
    }

    public void setObligatedAmnt(Integer obligatedAmnt) {
        this.obligatedAmnt = obligatedAmnt;
    }

    public Date getFinalExpDate() {
        return FinalExpDate;
    }

    public void setFinalExpDate(Date FinalExpDate) {
        this.FinalExpDate = FinalExpDate;
    }



    public Date getAwardEffDate() {
        return AwardEffDate;
    }

    public void setAwardEffDate(Date AwardEffDate) {
        this.AwardEffDate = AwardEffDate;
    }
    public String getActivityType() {
        return ActivityType;
    }

    public void setActivityType(String ActivityType) {
        this.ActivityType = ActivityType;
    }
private Integer sponsorNo;

    public Integer getSponsorNo() {
        return sponsorNo;
    }

    public void setSponsorNo(Integer sponsorNo) {
        this.sponsorNo = sponsorNo;
    }
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
    /**
     * @return the awardNumber
     */
    public String getAwardNumber() {
        return awardNumber;
    }

    /**
     * @param awardNumber the awardNumber to set
     */
    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    /**
     * @return the accntNumber
     */
    public String getAccntNumber() {
        return accntNumber;
    }

    /**
     * @param accntNumber the accntNumber to set
     */
    public void setAccntNumber(String accntNumber) {
        this.accntNumber = accntNumber;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the PI
     */
    public String getPI() {
        return PI;
    }

    /**
     * @param PI the PI to set
     */
    public void setPI(String PI) {
        this.PI = PI;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the leasdUnit
     */
    public String getLeasdUnit() {
        return leasdUnit;
    }

    /**
     * @param leasdUnit the leasdUnit to set
     */
    public void setLeasdUnit(String leasdUnit) {
        this.leasdUnit = leasdUnit;
    }

    /**
     * @return the sponsor
     */
    public String getSponsor() {
        return sponsor;
    }

    /**
     * @param sponsor the sponsor to set
     */
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    /**
     * @return the sponsorAwardNum
     */
    public String getSponsorAwardNum() {
        return sponsorAwardNum;
    }

    /**
     * @param sponsorAwardNum the sponsorAwardNum to set
     */
    public void setSponsorAwardNum(String sponsorAwardNum) {
        this.sponsorAwardNum = sponsorAwardNum;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the anticipatedAmnt
     */
    public Integer getAnticipatedAmnt() {
        return anticipatedAmnt;
    }

    /**
     * @param anticipatedAmnt the anticipatedAmnt to set
     */
    public void setAnticipatedAmnt(Integer anticipatedAmnt) {
        this.anticipatedAmnt = anticipatedAmnt;
    }

    /**
     * @return the roleId
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * @param roleId the roleId to set
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getAwardtype() {
        return awardtype;
    }

    public void setAwardtype(String awardtype) {
        this.awardtype = awardtype;
    }

}
