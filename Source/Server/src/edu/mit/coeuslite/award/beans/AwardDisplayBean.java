/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.beans;

import java.util.Date;


/**
 *
 * @author vineetha
 */
public class AwardDisplayBean  {
    
    
    private String awardNumber;
    private String accntNumber;
    private String status;
    private String awardtype;
    private String PI;
    private String title;
    private String leasdUnit;
    private String sponsor;
    private String sponsorAwardNum;
    private String startDate;
    private String endDate;
    private String anticipatedAmnt;
    private Integer roleId;
    private String accountType;
    private String activityType;
    private String finalExpDate;
    private String awardEffDate;
    private String obligatedAmnt;
    private String sponsorNo;
     private String sponsorNum;
      private String apprvdEq;
      private String apprvdsub;
      private String apprvdfor;
      private String paymnt;
      private String transfer;
      private String costsharing;
      private String indirectcost;
      private String personName;
      private String department;
      private String role;
      private String disclosureStatus;
      private String preAwardEffDate;
      private String primeSponsorNo;
     private String primeSponsor;

    public String getPreAwardEffDate() {
        return preAwardEffDate;
    }

    public void setPreAwardEffDate(String preAwardEffDate) {
        this.preAwardEffDate = preAwardEffDate;
    }

    public String getPrimeSponsor() {
        return primeSponsor;
    }

    public void setPrimeSponsor(String primeSponsor) {
        this.primeSponsor = primeSponsor;
    }

    public String getPrimeSponsorNo() {
        return primeSponsorNo;
    }

    public void setPrimeSponsorNo(String primeSponsorNo) {
        this.primeSponsorNo = primeSponsorNo;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getApprvdEq() {

        return apprvdEq;
    }

    public void setApprvdEq(String apprvdEq) {
        this.apprvdEq = apprvdEq;
    }

    public String getApprvdfor() {
        return apprvdfor;
    }

    public void setApprvdfor(String apprvdfor) {
        this.apprvdfor = apprvdfor;
    }

    public String getApprvdsub() {
        return apprvdsub;
    }

    public void setApprvdsub(String apprvdsub) {
        this.apprvdsub = apprvdsub;
    }

    public String getCostsharing() {
        return costsharing;
    }

    public void setCostsharing(String costsharing) {
        this.costsharing = costsharing;
    }

    public String getIndirectcost() {
        return indirectcost;
    }

    public void setIndirectcost(String indirectcost) {
        this.indirectcost = indirectcost;
    }

    public String getPaymnt() {
        return paymnt;
    }

    public void setPaymnt(String paymnt) {
        this.paymnt = paymnt;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getSponsorNum() {
        return sponsorNum;
    }

    public void setSponsorNum(String sponsorNum) {
        this.sponsorNum = sponsorNum;
    }

    public String getactivityType() {
        return activityType;
    }

    public void setactivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getawardEffDate() {
        return awardEffDate; 
    }

    public void setAwardEffDate(String awardEffDate) {
        this.awardEffDate = awardEffDate;
    }

    public String getfinalExpDate() {
        return finalExpDate;
    }

    public void setFinalExpDate(String finalExpDate) {
        this.finalExpDate = finalExpDate;
    }

    public String getPI() {
        return PI;
    }

    public void setPI(String PI) {
        this.PI = PI;
    }

    public String getAccntNumber() {
        return accntNumber;
    }

    public void setAccntNumber(String accntNumber) {
        this.accntNumber = accntNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAnticipatedAmnt() {
        return anticipatedAmnt;
    }

    public void setAnticipatedAmnt(String anticipatedAmnt) {
        this.anticipatedAmnt = anticipatedAmnt;
    }

    public String getAwardNumber() {
        return awardNumber;
    }

    public void setAwardNumber(String awardNumber) {
        this.awardNumber = awardNumber;
    }

    public String getAwardtype() {
        return awardtype;
    }

    public void setAwardtype(String awardtype) {
        this.awardtype = awardtype;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLeasdUnit() {
        return leasdUnit;
    }

    public void setLeasdUnit(String leasdUnit) {
        this.leasdUnit = leasdUnit;
    }

    public String getObligatedAmnt() {
        return obligatedAmnt;
    }

    public void setObligatedAmnt(String obligatedAmnt) {
        this.obligatedAmnt = obligatedAmnt;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getSponsorAwardNum() {
        return sponsorAwardNum;
    }

    public void setSponsorAwardNum(String sponsorAwardNum) {
        this.sponsorAwardNum = sponsorAwardNum;
    }

    public String getSponsorNo() {
        return sponsorNo;
    }

    public void setSponsorNo(String sponsorNo) {
        this.sponsorNo = sponsorNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return
     */
   
    /**
     *
     */
    public AwardDisplayBean() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the disclosureStatus
     */
    public String getDisclosureStatus() {
        return disclosureStatus;
    }

    /**
     * @param disclosureStatus the disclosureStatus to set
     */
    public void setDisclosureStatus(String disclosureStatus) {
        this.disclosureStatus = disclosureStatus;
    }

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */

}
