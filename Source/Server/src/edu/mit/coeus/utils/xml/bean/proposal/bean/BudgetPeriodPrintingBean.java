/*
 * @(#)BudgetPeriodPrintingBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.utils.xml.bean.proposal.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.s2s.bean.IndirectCostBean;
import java.beans.*;
import edu.mit.coeus.utils.xml.bean.proposal.nih.NSFOtherPersonnelType;
import java.sql.Date;
import edu.mit.coeus.utils.CoeusVector;
import java.math.*;


 
/**
 * The class used to hold the information for <code>Proposal Budget Printing</code>
 *
 * @author  ele
 * @version 1.0
 * Created on April 23, 2004
 */


public class BudgetPeriodPrintingBean implements CoeusBean, java.io.Serializable{
    
    private String proposalNumber;
    private int version;
    private String finalVersionFlag;
    private int budgetPeriod ;
    private Date startDate;
    private Date endDate;
    private CoeusVector salaryAndWages; //contains SalaryAndWagesBeans
    private CoeusVector equipmentCosts; //contains costBeans
    private CoeusVector travelCosts;    //contains costBeans
    private CoeusVector participantCosts; //contains costBeans
    private CoeusVector otherDirectCosts; //contains costBeans
    //additions for nsf extension
    private NSFOtherPersonnelBean nSFOtherPersonnel; 
    private CoeusVector nSFSeniorPersonnel; //contains NSFSeniorPersonnel beans;
    //end additions for nsf extension
    private double totalSalaryAndWages ; 
    private double equipmentTotal = 0; 
    private double travelTotal = 0; 
    private double participantPatientTotal = 0; 
    private double otherDirectTotal = 0; 
    private double periodDirectCostsTotal = 0; 
    private double indirectCostsTotal = 0; 
    private double periodCostsTotal = 0; 
    private double totSalaryRequested ;
    private double totFringe ;
    private double nonConsortiumDirectCostSubtotal = 0;
    private double consortiumTotalCosts = 0;
    private double consortiumDirectCosts = 0; 
    private edu.mit.coeus.s2s.bean.IndirectCostBean indirectCostBean;
    private double consortiumIndirectCosts = 0; 
 //  private CoeusVector indirectCostDetails; //contains nSFTotalSeniorPersonnels IndirectCostDetailBeans
    private double costSharingAmt = 0; 
    private double underrecoveryAmt = 0; 
    //holds update user id - needed to implement CoeusBean
    //additions for nsf extension
    private double modularPeriodAmount = 0;
   
    private double nSFTotalOtherDirectCosts = 0;
    private BigInteger nSFTotalSeniorPersonnel ;
    //end additions
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;
    
     /**
     *	Constructor
     */
    
    public BudgetPeriodPrintingBean(){
       
    }
     
     /** Getter for proposalNumber.
     * @return Value of proposalNumber.
     */ 
    
    public String getProposalNumber() {
        return proposalNumber;
    }

    /** Setter for proposalNumber
     * @param proposalNumber New value of proposalNumber.
     */
    
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

      /** Getter for property version.
     * @return Value of property version.
     */
    public int getVersion() {
        return version;
    }
    
    /** Setter for property version.
     * @param version New value of property version.
     */
    public void setVersion(int version) {
        this.version = version;
    }
    
     /** Getter for property finalVersionFlag.
     * @return Value of property finalVersionFlag.
     */
    public String getFinalVersionFlag() {
        return finalVersionFlag;
    }
    
    /** Setter for property finalVersionFlag.
     * @param finalVersionFlag New value of property finalVersionFlag.
     */
    public void setFinalVersionFlag(String finalVersionFlag) {
        this.finalVersionFlag = finalVersionFlag;
    }
    
      /** Getter for property budgetPeriod.
     * @return Value of property budgetPeriod.
     */
    public int getBudgetPeriod() {
        return budgetPeriod;
    }
    
    /** Setter for property budgetPeriod.
     * @param budgetPeriod New value of property budgetPeriod.
     */
    public void setBudgetPeriod(int budgetPeriod) {
        this.budgetPeriod = budgetPeriod;
    }

  /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /** Getter for property nSFOtherPersonnel.
     * @return Value of property nSFOtherPersonnel.
     */
    public NSFOtherPersonnelBean getNSFOtherPersonnel() {
        return nSFOtherPersonnel;
    }
    
    /** Setter for property nSFOtherPersonnel.
     * @param nSFOtherPersonnel New value of property nSFOtherPersonnel.
     */
    public void setNSFOtherPersonnel(NSFOtherPersonnelBean nSFOtherPersonnel) {
        this.nSFOtherPersonnel = nSFOtherPersonnel;
    }
  
     /** Getter for property nSFSeniorPersonnel.
     *   which is a Vector of  NSFSeniorPersonnel beans
     * @return Value of property nSFSeniorPersonnel.
     */
    public CoeusVector getNSFSeniorPersonnel() {
        return nSFSeniorPersonnel;
    }
    
    /** Setter for property nSFSeniorPersonnel.
     * @param nSFSeniorPersonnel New value of property nSFSeniorPersonnel.
     */
    public void setNSFSeniorPersonnel(CoeusVector nSFSeniorPersonnel) {
        this.nSFSeniorPersonnel = nSFSeniorPersonnel;
    }
     
    /** Getter for property salaryAndWages.
     *   which is a Vector of  SalaryAndWagesBeans
     * @return Value of property salaryAndWages.
     */
    public CoeusVector getSalaryAndWages() {
        return salaryAndWages;
    }
    
    /** Setter for property salaryAndWages.
     * @param salaryAndWages New value of property salaryAndWages.
     */
    public void setSalaryAndWages(CoeusVector salaryAndWages) {
        this.salaryAndWages = salaryAndWages;
    }
    
      /** Getter for property equipmentCosts.
     *   which is a Vector of  equipmentCostBeans
     * @return Value of property equipmentCosts.
     */
    public CoeusVector getEquipmentCosts() {
        return equipmentCosts;
    }
    
    /** Setter for property equipmentCosts.
     * @param equipmentCosts New value of property equipmentCosts.
     */
    public void setEquipmentCosts(CoeusVector equipmentCosts) {
        this.equipmentCosts = equipmentCosts;
    }
       
     /** Getter for property travelCosts.
     *   which is a Vector of  travelCostBeans
     * @return Value of property travelCosts.
     */
    public CoeusVector getTravelCosts() {
        return travelCosts;
    }
    
    /** Setter for property travelCosts.
     * @param travelCosts New value of property travelCosts.
     */
    public void setTravelCosts(CoeusVector travelCosts) {
        this.travelCosts = travelCosts;
    }
    
    
     /** Getter for property participantCosts.
     *   which is a Vector of  participantCostBeans
     * @return Value of property participantCosts.
     */
    public CoeusVector getParticipantCosts() {
        return participantCosts;
    }
    
    /** Setter for property participantCosts.
     * @param participantCosts New value of property participantCosts.
     */
    public void setParticipantCosts(CoeusVector participantCosts) {
        this.participantCosts = participantCosts;
    }
    
      /** Getter for property otherDirectCosts.
     *   which is a Vector of  otherDirectCostBeans
     * @return Value of property otherDirectCosts.
     */
    public CoeusVector getOtherDirectCosts() {
        return otherDirectCosts;
    }
    
    /** Setter for property otherDirectCosts.
     * @param otherDirectCosts New value of property otherDirectCosts.
     */
    public void setOtherDirectCosts(CoeusVector otherDirectCosts) {
        this.otherDirectCosts = otherDirectCosts;
    }
    
    /** Getter for property totalSalaryAndWages.
     * @return Value of property totalSalaryAndWages.
     */
    public double getTotalSalaryAndWages() {
        return totalSalaryAndWages;
    }
    
    /** Setter for property totalSalaryAndWages.
     * @param totalSalaryAndWages New value of property totalSalaryAndWages.
     */
    public void setTotalSalaryAndWages(double totalSalaryAndWages) {
        this.totalSalaryAndWages = totalSalaryAndWages;
    }
    
    /** Getter for property equipmentTotal.
     * @return Value of property equipmentTotal.
     */
    public double getEquipmentTotal() {
        return equipmentTotal;
    }
    
    /** Setter for property equipmentTotal.
     * @param equipmentTotal New value of property equipmentTotal.
     */
    public void setEquipmentTotal(double equipmentTotal) {
        this.equipmentTotal = equipmentTotal;
    }
    
     /** Getter for property travelTotal.
     * @return Value of property travelTotal.
     */
    public double getTravelTotal() {
        return travelTotal;
    }
    
    /** Setter for property travelTotal.
     * @param travelTotal New value of property travelTotal.
     */
    public void setTravelTotal(double travelTotal) {
        this.travelTotal = travelTotal;
    }
    
     /** Getter for property participantPatientTotal.
     * @return Value of property participantPatientTotal.
     */
    public double getParticipantPatientTotal() {
        return participantPatientTotal;
    }
    
    /** Setter for property participantPatientTotal.
     * @param participantPatientTotal New value of property participantPatientTotal.
     */
    public void setParticipantPatientTotal(double participantPatientTotal) {
        this.participantPatientTotal = participantPatientTotal;
    }
    
     /** Getter for property otherDirectTotal.
     * @return Value of property otherDirectTotal.
     */
    public double getOtherDirectTotal() {
        return otherDirectTotal;
    }
    
    /** Setter for property otherDirectTotal.
     * @param otherDirectTotal New value of property otherDirectTotal.
     */
    public void setOtherDirectTotal(double otherDirectTotal) {
        this.otherDirectTotal = otherDirectTotal;
    }
    
     /** Getter for property periodDirectCostsTotal.
     * @return Value of property periodDirectCostsTotal.
     */
    public double getPeriodDirectCostsTotal() {
        return periodDirectCostsTotal;
    }
    
    /** Setter for property periodDirectCostsTotal.
     * @param periodDirectCostsTotal New value of property periodDirectCostsTotal.
     */
    public void setPeriodDirectCostsTotal(double periodDirectCostsTotal) {
        this.periodDirectCostsTotal = periodDirectCostsTotal;
    }
    
      /** Getter for property indirectCostsTotal.
     * @return Value of property indirectCostsTotal.
     */
    public double getIndirectCostsTotal() {
        return indirectCostsTotal;
    }
    
    /** Setter for property indirectCostsTotal.
     * @param indirectCostsTotal New value of property indirectCostsTotal.
     */
    public void setIndirectCostsTotal(double indirectCostsTotal) {
        this.indirectCostsTotal = indirectCostsTotal;
    }
     
     /** Getter for property periodCostsTotal.
     * @return Value of property periodCostsTotal.
     */
    public double getPeriodCostsTotal() {
        return periodCostsTotal;
    }
    
    /** Setter for property periodCostsTotal.
     * @param periodCostsTotal New value of property periodCostsTotal.
     */
    public void setPeriodCostsTotal(double periodCostsTotal) {
        this.periodCostsTotal = periodCostsTotal;
    }
      
      /** Getter for property totSalaryRequested.
     * @return Value of property totSalaryRequested.
     */
    public double getTotSalaryRequested() {
        return totSalaryRequested;
    }
    
    /** Setter for property totSalaryRequested.
     * @param totSalaryRequested New value of property totSalaryRequested.
     */
    public void setTotSalaryRequested(double totSalaryRequested) {
        this.totSalaryRequested = totSalaryRequested;
    }
    
    /** Getter for property totFringe.
     * @return Value of property totFringe.
     */
    public double getTotFringe() {
        return totFringe;
    }
    
    /** Setter for property totFringe.
     * @param totFringe New value of property totFringe.
     */
    public void setTotFringe(double totFringe) {
        this.totFringe = totFringe;
    }
  
     /** Getter for property nonConsortiumDirectCostSubtotal.
     * @return Value of property nonConsortiumDirectCostSubtotal.
     */
    public double getNonConsortiumDirectCostSubtotal() {
        return nonConsortiumDirectCostSubtotal;
    }
    
    /** Setter for property nonConsortiumDirectCostSubtotal.
     * @param nonConsortiumDirectCostSubtotal New value of property nonConsortiumDirectCostSubtotal.
     */
    public void setNonConsortiumDirectCostSubtotal(double nonConsortiumDirectCostSubtotal) {
        this.nonConsortiumDirectCostSubtotal = nonConsortiumDirectCostSubtotal;
    }
  
  
     /** Getter for property consortiumDirectCosts.
     * @return Value of property consortiumDirectCosts.
     */
    public double getConsortiumDirectCosts() {
        return consortiumDirectCosts;
    }
    
    /** Setter for property consortiumDirectCosts.
     * @param consortiumDirectCosts New value of property consortiumDirectCosts.
     */
    public void setConsortiumDirectCosts(double consortiumDirectCosts) {
        this.consortiumDirectCosts = consortiumDirectCosts;
    }
    
     /** Getter for property consortiumIndirectCosts.
     * @return Value of property consortiumIndirectCosts.
     */
    public double getConsortiumIndirectCosts() {
        return consortiumIndirectCosts;
    }
    
    /** Setter for property consortiumIndirectCosts.
     * @param consortiumIndirectCosts New value of property consortiumIndirectCosts.
     */
    public void setConsortiumIndirectCosts(double consortiumIndirectCosts) {
        this.consortiumIndirectCosts = consortiumIndirectCosts;
    }
    
     /** Getter for property indirectCostBean.
     *  
     * @return Value of property indirectCostBean.
     */
    public IndirectCostBean getIndirectCostBean() {
        return indirectCostBean;
    }
    
    /** Setter for property indirectCostsBean.
     * @param indirectCostBean New value of property indirectCostBean.
     */
    public void setIndirectCosts(IndirectCostBean indirectCostBean) {
        this.indirectCostBean = indirectCostBean;
    }
    
   
    
     /** Getter for property costSharingAmt.
     * @return Value of property costSharingAmt.
     */
    public double getCostSharingAmt() {
        return costSharingAmt;
    }
    
    /** Setter for property costSharingAmt.
     * @param costSharingAmt New value of property costSharingAmt.
     */
    public void setCostSharingAmt(double costSharingAmt) {
        this.costSharingAmt = costSharingAmt;
    }
   
    /** Getter for property underrecoveryAmt.
     * @return Value of property underrecoveryAmt.
     */
    public double getUnderrecoveryAmt() {
        return underrecoveryAmt; //nsf extensions
        
    }
    
    /** Setter for property underrecoveryAmt.
     * @param underrecoveryAmt New value of property underrecoveryAmt.
     */
    public void setUnderrecoveryAmt(double underrecoveryAmt) {
        this.underrecoveryAmt = underrecoveryAmt;
    }
    
     /** Getter for property modularPeriodAmount.
     * @return Value of property modularPeriodAmount.
     */
    public double getModularPeriodAmount() {
        return modularPeriodAmount;
    }
    
    /** Setter for property modularPeriodAmount.
     * @param modularPeriodAmount New value of property modularPeriodAmount.
     */
     public void setModularPeriodAmount(double modularPeriodAmount) {
        this.modularPeriodAmount = modularPeriodAmount;
    }
  
  
     /** Getter for property nSFTotalOtherDirectCosts.
     * @return Value of property nSFTotalOtherDirectCosts.
     */
    public double getNSFTotalOtherDirectCosts() {
        return nSFTotalOtherDirectCosts;
    }
     
    /** Setter for property nSFTotalOtherDirectCosts.
     * @param nSFTotalOtherDirectCosts New value of property nSFTotalOtherDirectCosts.
     */
     public void setNSFTotalOtherDirectCosts(double nSFTotalOtherDirectCosts) {
        this.nSFTotalOtherDirectCosts = nSFTotalOtherDirectCosts;
    }
     
     /** Getter for property nSFTotalSeniorPersonnel.
     * @return Value of property nSFTotalSeniorPersonnel.
     */
    public BigInteger getNSFTotalSeniorPersonnel() {
        return nSFTotalSeniorPersonnel;
    }
    
    /** Setter for property nSFTotalSeniorPersonnel.
     * @param nSFTotalSeniorPersonnel New value of property nSFTotalSeniorPersonnel.
     */
     public void setNSFTotalSeniorPersonnel(BigInteger nSFTotalSeniorPersonnel) {
        this.nSFTotalSeniorPersonnel = nSFTotalSeniorPersonnel;
    }
 
     
    /**
     * Getter for property indirectCostDetails.
     * @return Value of property indirectCostDetails.
     */
     /*
    public edu.mit.coeus.utils.CoeusVector getIndirectCostDetails() {
        return indirectCostDetails;
    }    
 */
    /**
     * Setter for property indirectCostDetails.
     * @param indirectCostDetails New value of property indirectCostDetails.
     */
     /*
    public void setIndirectCostDetails(edu.mit.coeus.utils.CoeusVector indirectCostDetails) {
        this.indirectCostDetails = indirectCostDetails;
    }
    */
    /**
     * Getter for property consortiumTotalCosts.
     * @return Value of property consortiumTotalCosts.
     */
    public double getConsortiumTotalCosts() {
        return consortiumTotalCosts;
    }
    
    /**
     * Setter for property consortiumTotalCosts.
     * @param consortiumTotalCosts New value of property consortiumTotalCosts.
     */
    public void setConsortiumTotalCosts(double consortiumTotalCosts) {
        this.consortiumTotalCosts = consortiumTotalCosts;
    }
    
     // following methods are necessary to implement CoeusBean  
   
    public String getUpdateUser() {
     return updateUser;
    }
    public void setUpdateUser(String userId) {
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
     return true;
    }

    public java.lang.String getAcType() {
     return acType;
    }
    
    public void setAcType(java.lang.String acType) {      
    }  
   
    public java.sql.Timestamp getUpdateTimestamp() {
     return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {   
    }
  
} 
