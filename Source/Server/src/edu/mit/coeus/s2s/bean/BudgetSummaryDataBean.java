/*
 * @(#)BudgetSummaryDataBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.CoeusBean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.utils.CoeusVector;
import java.math.*;


public class BudgetSummaryDataBean implements CoeusBean, java.io.Serializable{
    
    private String proposalNumber;
    private int version;
    private CoeusVector budgetPeriods; //BudgetPeriodDataBeans
    private String finalVersionFlag;
    private BigDecimal cumTotalFundsForSrPersonnel;
    private BigDecimal cumTotalFundsForOtherPersonnel ;
    private BigDecimal cumTotalFundsForPersonnel ;
    private BigInteger cumNumOtherPersonnel;
    private BigDecimal cumEquipmentFunds ;
    private BigDecimal cumTravel ;
    private BigDecimal cumDomesticTravel ;
    private BigDecimal cumForeignTravel ;
    
    private CoeusVector otherDirect; //otherDirectCostBean    

    private BigDecimal partStipendCost;
    private BigDecimal partTravelCost;
    private BigDecimal partOtherCost;
    private BigDecimal partSubsistence;
    private BigDecimal partTuition;
    private int participantCount;
    
    
    private BigDecimal cumTotalDirectCosts ; 
    private BigDecimal cumTotalIndirectCosts;
    private BigDecimal cumTotalCosts ;
    private BigDecimal cumFee ;
    //start add costSaring for fedNonFedBudget repport
    private BigDecimal cumTotalCostSharing;
    private BigDecimal cumTotalNonFundsForSrPersonnel;
    private BigDecimal cumTotalNonFundsForOtherPersonnel ;
    private BigDecimal cumTotalNonFundsForPersonnel ;
    private BigDecimal cumTravelNonFund ;
    private BigDecimal cumDomesticTravelNonFund ;
    private BigDecimal cumForeignTravelNonFund ;
    private BigDecimal partStipendCostSharing;
    private BigDecimal partTravelCostSharing;
    private BigDecimal partSubsistenceCostSharing;
    private BigDecimal partTuitionCostSharing;
    private BigDecimal partOtherCostSharing;
    private BigDecimal cumEquipmentNonFunds;
    private BigDecimal cumTotalDirectCostSharing;
    private BigDecimal cumTotalIndirectCostSharing;
    //end add costSaring for fedNonFedBudget repport
    
    //holds update user id - needed to implement CoeusBean 
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;
    
     /**
     *	Default Constructor
     */
    
    public BudgetSummaryDataBean(){
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

    /** Getter for property budgetPeriods.
     * @return Value of property budgetPeriods.
     */
    public CoeusVector getBudgetPeriods() {
        return budgetPeriods;
    }
    
    /** Setter for property budgetPeriods.
     * @param budgetPeriods New value of property budgetPeriods.
     */
    public void setBudgetPeriods(CoeusVector budgetPeriods) {
        this.budgetPeriods = budgetPeriods;
    }
    
 
    /** Getter for property cumTotalFundsForSrPersonnel.
     * @return Value of property cumTotalFundsForSrPersonnel.
     */
    public BigDecimal getCumTotalFundsForSrPersonnel() {
        return cumTotalFundsForSrPersonnel;
    }
    
    /** Setter for property cumTotalFundsForSrPersonnel.
     * @param cumTotalFundsForSrPersonnel New value of property cumTotalFundsForSrPersonnel.
     */
    public void setCumTotalFundsForSrPersonnel(BigDecimal cumTotalFundsForSrPersonnel) {
        this.cumTotalFundsForSrPersonnel = cumTotalFundsForSrPersonnel;
    }
    

    /** Getter for property cumTotalFundsForOtherPersonnel.
     * @return Value of property cumTotalFundsForOtherPersonnel.
     */
    public BigDecimal getCumTotalFundsForOtherPersonnel() {
        return cumTotalFundsForOtherPersonnel;
    }
    
    /** Setter for property cumTotalFundsForOtherPersonnel.
     * @param cumTotalFundsForOtherPersonnel New value of property cumTotalFundsForOtherPersonnel.
     */
    public void setCumTotalFundsForOtherPersonnel(BigDecimal cumTotalFundsForOtherPersonnel) {
        this.cumTotalFundsForOtherPersonnel = cumTotalFundsForOtherPersonnel;
    }
    
    
    /** Getter for property cumTotalFundsForPersonnel.
     * @return Value of property cumTotalFundsForPersonnel.
     */
    public BigDecimal getCumTotalFundsForPersonnel() {
        return cumTotalFundsForPersonnel;
    }
    
    /** Setter for property cumTotalFundsForPersonnel.
     * @param cumTotalFundsForPersonnel New value of property cumTotalFundsForPersonnel.
     */
    public void setCumTotalFundsForPersonnel(BigDecimal cumTotalFundsForPersonnel) {
        this.cumTotalFundsForPersonnel = cumTotalFundsForPersonnel;
    }

    
    /** Getter for property cumNumOtherPersonnel.
     * @return Value of property cumNumOtherPersonnel.
     */
    public BigInteger getCumNumOtherPersonnel() {
        return cumNumOtherPersonnel;
    }
    
    /** Setter for property cumNumOtherPersonnel.
     * @param cumNumOtherPersonnel New value of property cumNumOtherPersonnel.
     */
    public void setCumNumOtherPersonnel(BigInteger cumNumOtherPersonnel) {
        this.cumNumOtherPersonnel = cumNumOtherPersonnel;
    }
    
    
    /** Getter for property cumEquipmentFunds.
     * @return Value of property cumEquipmentFunds.
     */
    public BigDecimal getCumEquipmentFunds() {
        return cumEquipmentFunds;
    }
    
    /** Setter for property cumEquipmentFunds.
     * @param cumEquipmentFunds New value of property cumEquipmentFunds.
     */
    public void setCumEquipmentFunds(BigDecimal cumEquipmentFunds) {
        this.cumEquipmentFunds = cumEquipmentFunds;
    }
    
    
    /** Getter for property cumTravel.
     * @return Value of property cumTravel.
     */
    public BigDecimal getCumTravel() {
        return cumTravel;
    }
    
    /** Setter for property cumTravel.
     * @param cumTravel New value of property cumTravel.
     */
    public void setCumTravel(BigDecimal cumTravel) {
        this.cumTravel = cumTravel;
    }
    
     /** Getter for property cumDomesticTravel.
     * @return Value of property cumDomesticTravel.
     */
    public BigDecimal getCumDomesticTravel() {
        return cumDomesticTravel;
    }
    
    /** Setter for property cumDomesticTravel.
     * @param cumDomesticTravel New value of property cumDomesticTravel.
     */
    public void setCumDomesticTravel(BigDecimal cumDomesticTravel) {
        this.cumDomesticTravel = cumDomesticTravel;
    }
    
     /** Getter for property cumForeignTravel.
     * @return Value of property cumForeignTravel.
     */
    public BigDecimal getCumForeignTravel() {
        return cumForeignTravel;
    }
    
    /** Setter for property cumForeignTravel.
     * @param cumForeignTravel New value of property cumForeignTravel.
     */
    public void setCumForeignTravel(BigDecimal cumForeignTravel) {
        this.cumForeignTravel = cumForeignTravel;
    }
    
    
     /** Getter for property partOtherCost.
     * @return Value of property partOtherCost.
     */
    public BigDecimal getpartOtherCost() {
        return partOtherCost;
    }
    
    /** Setter for property partOtherCost.
     * @param partOtherCost New value of property partOtherCost.
     */
    public void setpartOtherCost(BigDecimal partOtherCost) {
        this.partOtherCost = partOtherCost;
    }
    
     /** Getter for property participantCount.
     * @return Value of property participantCount.
     */
    public int getparticipantCount() {
        return participantCount;
    }
    
    /** Setter for property participantCount.
     * @param participantCount New value of property participantCount.
     */
    public void setparticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }
    
     /** Getter for property partTravelCost.
     * @return Value of property partTravelCost.
     */
    public BigDecimal getpartTravelCost() {
        return partTravelCost;
    }
    
    /** Setter for property partTravelCost.
     * @param partTravelCost New value of property partTravelCost.
     */
    public void setpartTravelCost(BigDecimal partTravelCost) {
        this.partTravelCost = partTravelCost;
    }
    
    
  
     /** Getter for property partStipendCost.
     * @return Value of property partStipendCost.
     */
    public BigDecimal getpartStipendCost() {
        return partStipendCost;
    }
    
    /** Setter for property partStipendCost.
     * @param partStipendCost New value of property partStipendCost.
     */
    public void setpartStipendCost(BigDecimal partStipendCost) {
        this.partStipendCost = partStipendCost;
    }

   
    
   
     /** Getter for property otherDirect.
     *   which is a Vector of  otherDirectCostBeans
     * @return Value of property otherDirect.
     */
    public CoeusVector getOtherDirectCosts() {
        return otherDirect;
    }
    
    /** Setter for property otherDirect.
     * @param otherDirect New value of property otherDirect.
     */
    public void setOtherDirectCosts(CoeusVector otherDirect) {
        this.otherDirect = otherDirect;
    }
    
     
    /** Getter for property cumTotalDirectCosts.
     * @return Value of property cumTotalDirectCosts.
     */
    public BigDecimal getCumTotalDirectCosts() {
        return cumTotalDirectCosts;
    }
    
    /** Setter for property cumTotalDirectCosts.
     * @param cumTotalDirectCosts New value of property cumTotalDirectCosts.
     */
    public void setCumTotalDirectCosts(BigDecimal cumTotalDirectCosts) {
        this.cumTotalDirectCosts = cumTotalDirectCosts;
    }
    
    
    /** Getter for property cumTotalIndirectCosts.
     * @return Value of property cumTotalIndirectCosts.
     */
    public BigDecimal getCumTotalIndirectCosts() {
        return cumTotalIndirectCosts;
    }
    
    /** Setter for property cumTotalIndirectCosts.
     * @param cumTotalIndirectCosts New value of property cumTotalIndirectCosts.
     */
    public void setCumTotalIndirectCosts(BigDecimal cumTotalIndirectCosts) {
        this.cumTotalIndirectCosts = cumTotalIndirectCosts;
    }
    
    
    /** Getter for property cumTotalCosts.
     * @return Value of property cumTotalCosts.
     */
    public BigDecimal getCumTotalCosts() {
        return cumTotalCosts;
    }
    
    /** Setter for property cumTotalCosts.
     * @param cumTotalCosts New value of property cumTotalCosts.
     */
    public void setCumTotalCosts(BigDecimal cumTotalCosts) {
        this.cumTotalCosts = cumTotalCosts;
    }
    
    
    /** Getter for property cumFee.
     * @return Value of property cumFee.
     */
    public BigDecimal getCumFee() {
        return cumFee;
    }
    
    /** Setter for property cumFee.
     * @param cumFee New value of property cumFee.
     */
    public void setCumFee(BigDecimal cumFee) {
        this.cumFee = cumFee;
    }
    
    //start add costSaring for fedNonFedBudget repport
    public BigDecimal getCumTotalCostSharing() {
        return cumTotalCostSharing;
    }
    public void setCumTotalCostSharing(BigDecimal cumTotalCostSharing) {
        this.cumTotalCostSharing = cumTotalCostSharing;
    }
    
    public BigDecimal getCumTotalNonFundsForSrPersonnel() {
        return cumTotalNonFundsForSrPersonnel;
    }
    public void setCumTotalNonFundsForSrPersonnel(BigDecimal cumTotalNonFundsForSrPersonnel) {
        this.cumTotalNonFundsForSrPersonnel = cumTotalNonFundsForSrPersonnel;
    }
    
    public BigDecimal getCumTotalNonFundsForOtherPersonnel() {
        return cumTotalNonFundsForOtherPersonnel;
    }
    public void setCumTotalNonFundsForOtherPersonnel(BigDecimal cumTotalNonFundsForOtherPersonnel) {
        this.cumTotalNonFundsForOtherPersonnel = cumTotalNonFundsForOtherPersonnel;
    }
    
    public BigDecimal getCumTotalNonFundsForPersonnel() {
        return cumTotalNonFundsForPersonnel;
    }
    public void setCumTotalNonFundsForPersonnel(BigDecimal cumTotalNonFundsForPersonnel) {
        this.cumTotalNonFundsForPersonnel = cumTotalNonFundsForPersonnel;
    }
     
    public BigDecimal getCumTravelNonFund() {
        return cumTravelNonFund;
    }
    public void setCumTravelNonFund(BigDecimal cumTravelNonFund) {
        this.cumTravelNonFund = cumTravelNonFund;
    }
     
    public BigDecimal getCumDomesticTravelNonFund() {
        return cumDomesticTravelNonFund;
    }
    public void setCumDomesticTravelNonFund(BigDecimal cumDomesticTravelNonFund) {
        this.cumDomesticTravelNonFund = cumDomesticTravelNonFund;
    }
     
    public BigDecimal getCumForeignTravelNonFund() {
        return cumForeignTravelNonFund;
    }
    public void setCumForeignTravelNonFund(BigDecimal cumForeignTravelNonFund) {
        this.cumForeignTravelNonFund = cumForeignTravelNonFund;
    }
       
    public BigDecimal getPartStipendCostSharing() {
        return partStipendCostSharing;
    }
    public void setPartStipendCostSharing(BigDecimal partStipendCostSharing) {
        this.partStipendCostSharing = partStipendCostSharing;
    }
       
    public BigDecimal getPartTravelCostSharing() {
        return partTravelCostSharing;
    }
    public void setPartTravelCostSharing(BigDecimal partTravelCostSharing) {
        this.partTravelCostSharing = partTravelCostSharing;
    }
       
    public BigDecimal getPartOtherCostSharing() {
        return partOtherCostSharing;
    }
    public void setPartOtherCostSharing(BigDecimal partOtherCostSharing) {
        this.partOtherCostSharing = partOtherCostSharing;
    }
    
    public BigDecimal getCumEquipmentNonFunds() {
        return cumEquipmentNonFunds;
    }
    public void setCumEquipmentNonFunds(BigDecimal cumEquipmentNonFunds) {
        this.cumEquipmentNonFunds = cumEquipmentNonFunds;
    }
     
    public BigDecimal getCumTotalDirectCostSharing() {
        return cumTotalDirectCostSharing;
    }
    public void setCumTotalDirectCostSharing(BigDecimal cumTotalDirectCostSharing) {
        this.cumTotalDirectCostSharing = cumTotalDirectCostSharing;
    }
     
    public BigDecimal getCumTotalIndirectCostSharing() {
        return cumTotalIndirectCostSharing;
    }
    public void setCumTotalIndirectCostSharing(BigDecimal cumTotalIndirectCostSharing) {
        this.cumTotalIndirectCostSharing = cumTotalIndirectCostSharing;
    }
    //end add costSaring for fedNonFedBudget repport 
    
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
    
    /**
     * Getter for property partSubsistence.
     * @return Value of property partSubsistence.
     */
    public java.math.BigDecimal getPartSubsistence() {
        return partSubsistence;
    }    
    
    /**
     * Setter for property partSubsistence.
     * @param partSubsistence New value of property partSubsistence.
     */
    public void setPartSubsistence(java.math.BigDecimal partSubsistence) {
        this.partSubsistence = partSubsistence;
    }
    
    /**
     * Getter for property partTuition.
     * @return Value of property partTuition.
     */
    public java.math.BigDecimal getPartTuition() {
        return partTuition;
    }
    
    /**
     * Setter for property partTuition.
     * @param partTuition New value of property partTuition.
     */
    public void setPartTuition(java.math.BigDecimal partTuition) {
        this.partTuition = partTuition;
    }
    
    /**
     * Getter for property partSubsistenceCostSharing.
     * @return Value of property partSubsistenceCostSharing.
     */
    public java.math.BigDecimal getPartSubsistenceCostSharing() {
        return partSubsistenceCostSharing;
    }
    
    /**
     * Setter for property partSubsistenceCostSharing.
     * @param partSubsistenceCostSharing New value of property partSubsistenceCostSharing.
     */
    public void setPartSubsistenceCostSharing(java.math.BigDecimal partSubsistenceCostSharing) {
        this.partSubsistenceCostSharing = partSubsistenceCostSharing;
    }
    
    /**
     * Getter for property partTuitionCostSharing.
     * @return Value of property partTuitionCostSharing.
     */
    public java.math.BigDecimal getPartTuitionCostSharing() {
        return partTuitionCostSharing;
    }
    
    /**
     * Setter for property partTuitionCostSharing.
     * @param partTuitionCostSharing New value of property partTuitionCostSharing.
     */
    public void setPartTuitionCostSharing(java.math.BigDecimal partTuitionCostSharing) {
        this.partTuitionCostSharing = partTuitionCostSharing;
    }
    
}
 
