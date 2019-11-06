/*
 * @(#)BudgetPeriodDataBean.java 
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


public class BudgetPeriodDataBean implements CoeusBean, java.io.Serializable{
    
    private String proposalNumber;
    private int version;
    private String finalVersionFlag;
    private int budgetPeriod ;
    private Date startDate;
    private Date endDate;
    private CoeusVector keyPersons; //keyPersonBeans
    private CoeusVector extraKeyPersons ;//keyPersonBeans
    private BigDecimal totalFundsKeyPersons;
    private BigDecimal totalFundsAttachedKeyPersons;
    private CoeusVector otherPersonnel; //otherPersonnelBeans
    private BigDecimal totalOtherPersonnelFunds;
    private BigInteger otherPersonnelTotalNumber;
    private BigDecimal totalCompensation;
    private CoeusVector equipment; //equipmentBean
    private CoeusVector extraEquipment; //equipmentBean
    private BigDecimal totalFundsEquipment;
    private BigDecimal totalFundsAttachedEquipment;
    private BigDecimal domesticTravelCost;
    private BigDecimal foreignTravelCost;
    private BigDecimal totalTravelCost;
    private BigDecimal partStipendCost;
    private BigDecimal partTravelCost;
    private BigDecimal partOtherCost;
    //addition for participant cost change
    private BigDecimal partTuition;
    private BigDecimal partSubsistence;
    //end addition
    private int participantCount;
    private CoeusVector otherDirectCosts; //otherDirectCostBeans
    private BigDecimal directCostsTotal; 
    private IndirectCostBean indirectCosts; 
    private String cognizantFedAgency;
    private BigDecimal totalCosts ; 
    private BigDecimal totalIndirectCost; //added to fix rounding problem
    //start add costSaring for fedNonFedBudget repport
    private BigDecimal costSharingAmount; // total costsharing
    private BigDecimal domesticTravelCostSharing;
    private BigDecimal foreignTravelCostSharing;
    private BigDecimal totalTravelCostSharing;
    private BigDecimal partStipendCostSharing;
    private BigDecimal partTravelCostSharing;
    private BigDecimal partTuitionCostSharing; 
    private BigDecimal partSubsistenceCostSharing;
    private BigDecimal partOtherCostSharing;
    private BigDecimal totalNonFundsKeyPersons;
    private BigDecimal totalNonFundsAttachedKeyPersons;
    private BigDecimal totalOtherPersonnelNonFunds;
    private BigDecimal totalCompensationCostSharing;
    private BigDecimal totalDirectCostSharing;
    private BigDecimal totalIndirectCostSharing;
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
    
    public BudgetPeriodDataBean(){
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
    
 
     /** Getter for property keyPersons.
     *   which is a Vector of  keyPersonnel beans
     * @return Value of property keyPersons
     */
    public CoeusVector getKeyPersons() {
        return keyPersons;
    }
    
    /** Setter for property keyPersons.
     * @param keyPersons New value of property keyPersons.
     */
    public void setKeyPersons(CoeusVector keyPersons) {
        this.keyPersons = keyPersons;
    }
     
    
    /** Getter for property extraKeyPersons.
     *   which is a Vector of  keyPersonnel beans
     * @return Value of property extraKeyPersons
     */
    public CoeusVector getExtraKeyPersons() {
        return extraKeyPersons;
    }
    
    /** Setter for property extraKeyPersons.
     * @param extraKeyPersons New value of property extraKeyPersons.
     */
    public void setExtraKeyPersons(CoeusVector extraKeyPersons) {
        this.extraKeyPersons = extraKeyPersons;
    }
     
    /** Getter for property totalFundsKeyPersons.
     * @return Value of property totalFundsKeyPersons.
     */
    public BigDecimal getTotalFundsKeyPersons() {
        return totalFundsKeyPersons;
    }
    
    /** Setter for property totalFundsKeyPersons.
     * @param totalFundsKeyPersons New value of property totalFundsKeyPersons.
     */
    public void setTotalFundsKeyPersons(BigDecimal totalFundsKeyPersons) {
        this.totalFundsKeyPersons = totalFundsKeyPersons;
    }
    
     /** Getter for property totalFundsAttachedKeyPersons.
     * @return Value of property totalFundsAttachedKeyPersons.
     */
    public BigDecimal getTotalFundsAttachedKeyPersons() {
        return totalFundsAttachedKeyPersons;
    }
    
    /** Setter for property totalFundsAttachedKeyPersons.
     * @param totalFundsAttachedKeyPersons New value of property totalFundsAttachedKeyPersons.
     */
    public void setTotalFundsAttachedKeyPersons(BigDecimal totalFundsAttachedKeyPersons) {
        this.totalFundsAttachedKeyPersons = totalFundsAttachedKeyPersons;
    }
    
    /** Getter for property totalOtherPersonnelFunds.
     * @return Value of property totalOtherPersonnelFunds.
     */
    public BigDecimal getTotalOtherPersonnelFunds() {
        return totalOtherPersonnelFunds;
    }
    
    /** Setter for property totalOtherPersonnelFunds.
     * @param totalOtherPersonnelFunds New value of property totalOtherPersonnelFunds.
     */
    public void setTotalOtherPersonnelFunds(BigDecimal totalOtherPersonnelFunds) {
        this.totalOtherPersonnelFunds =totalOtherPersonnelFunds;
    }
    
     /** Getter for property OtherPersonnelTotalNumber.
     * @return Value of property OtherPersonnelTotalNumber.
     */
    public BigInteger getOtherPersonnelTotalNumber() {
        return otherPersonnelTotalNumber;
    }
    
    /** Setter for property OtherPersonnelTotalNumber.
     * @param OtherPersonnelTotalNumber New value of property OtherPersonnelTotalNumber.
     */
    public void setOtherPersonnelTotalNumber(BigInteger otherPersonnelTotalNumber) {
        this.otherPersonnelTotalNumber = otherPersonnelTotalNumber;
    }
  
    
    /** Getter for property otherPersonnel.
     *   which is a Vector of  otherPersonnelBeans
     * @return Value of property otherPersonnel.
     */
    public CoeusVector getOtherPersonnel() {
        return otherPersonnel;
    }
    
    /** Setter for property otherPersonnel.
     * @param otherPersonnel New value of property otherPersonnel.
     */
    public void setOtherPersonnel(CoeusVector otherPersonnel) {
        this.otherPersonnel = otherPersonnel;
    }
    
    /** Getter for property totalCompensation.
     * @return Value of property totalCompensation.
     */
    public BigDecimal getTotalCompensation() {
        return totalCompensation;
    }
    
    /** Setter for property totalCompensation.
     * @param totalCompensation New value of property totalCompensation.
     */
    public void setTotalCompensation(BigDecimal totalCompensation) {
        this.totalCompensation = totalCompensation;
    }
    
    /** Getter for property equipment.
     *   which is a Vector of  equipmentBeans
     * @return Value of property equipment.
     */
    public CoeusVector getEquipment() {
        return equipment;
    }
    
    /** Setter for property equipment.
     * @param equipment New value of property equipment.
     */
    public void setEquipment(CoeusVector equipment) {
        this.equipment = equipment;
    }
       
    /** Getter for property extraEquipment.
     *   which is a Vector of  equipmentBeans
     * @return Value of property extraEquipment..
     */
    public CoeusVector getExtraEquipment() {
        return extraEquipment;
    }
    
    /** Setter for property extraEquipment..
     * @param extraEquipment. New value of property extraEquipment..
     */
    public void setExtraEquipment(CoeusVector extraEquipment) {
        this.extraEquipment = extraEquipment;
    }
       
    
    /** Getter for property domesticTravelCost.
     * @return Value of property domesticTravelCost.
     */
    public BigDecimal getDomesticTravelCost() {
        return domesticTravelCost;
    }
    
    /** Setter for property domesticTravelCost.
     * @param domesticTravelCost New value of property domesticTravelCost.
     */
    public void setDomesticTravelCost(BigDecimal domesticTravelCost) {
        this.domesticTravelCost = domesticTravelCost;
    }
    
    /** Getter for property foreignTravelCost.
     * @return Value of property foreignTravelCost.
     */
    public BigDecimal getForeignTravelCost() {
        return foreignTravelCost;
    }
    
    /** Setter for property foreignTravelCost.
     * @param foreignTravelCost New value of property foreignTravelCost.
     */
    public void setForeignTravelCost(BigDecimal foreignTravelCost) {
        this.foreignTravelCost = foreignTravelCost;
    }
    
    /** Getter for property totalTravelCost.
     * @return Value of property totalTravelCost.
     */
    public BigDecimal getTotalTravelCost() {
        return totalTravelCost;
    }
    
    /** Setter for property totalTravelCost.
     * @param totalTravelCost New value of property totalTravelCost.
     */
    public void setTotalTravelCost(BigDecimal totalTravelCost) {
        this.totalTravelCost = totalTravelCost;
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
    
    /** Getter for property directCostsTotal.
     * @return Value of property directCostsTotal.
     */
    public BigDecimal getDirectCostsTotal() {
        return directCostsTotal;
    }
    
    /** Setter for property directCostsTotal.
     * @param directCostsTotal New value of property directCostsTotal.
     */
    public void setDirectCostsTotal(BigDecimal directCostsTotal) {
        this.directCostsTotal = directCostsTotal;
    }
  
    /** Getter for property indirectCosts.
     *   which is an indirectCostsBean
     * @return Value of property indirectCosts.
     */
    public IndirectCostBean getIndirectCosts() {
        return indirectCosts;
    }
    
    /** Setter for property indirectCosts.
     * @param indirectCosts New value of property indirectCosts.
     */
    public void setIndirectCosts(IndirectCostBean indirectCosts) {
        this.indirectCosts = indirectCosts;
    }
    
    
    /** Getter for property cognizantFedAgency.
     * @return Value of property cognizantFedAgency.
     */
    public String getCognizantFedAgency() {
        return cognizantFedAgency;
    }
    
    /** Setter for property cognizantFedAgency.
     * @param cognizantFedAgency New value of property cognizantFedAgency.
     */
    public void setCognizantFedAgency(String cognizantFedAgency) {
        this.cognizantFedAgency = cognizantFedAgency;
    }
    
    /** Getter for property totalCosts.
     * @return Value of property totalCosts.
     */
    public BigDecimal getTotalCosts() {
        return totalCosts;
    }
    
    /** Setter for property totalCosts.
     * @param totalCosts New value of property totalCosts.
     */
    public void setTotalCosts(BigDecimal totalCosts) {
        this.totalCosts = totalCosts;
    }
    
     //start add costSaring for fedNonFedBudget repport
    /**
     * Getter for property costSharingAmount.
     * @return Value of property costSharingAmount.
     */
    public java.math.BigDecimal getCostSharingAmount() {
        return costSharingAmount;
    }
    
    /**
     * Setter for property costSharingAmount.
     * @param costSharingAmount New value of property costSharingAmount.
     */
    public void setCostSharingAmount(java.math.BigDecimal costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }
    
    public java.math.BigDecimal getDomesticTravelCostSharing() {
        return domesticTravelCostSharing;
    }
    public void setDomesticTravelCostSharing(java.math.BigDecimal domesticTravelCostSharing) {
        this.domesticTravelCostSharing = domesticTravelCostSharing;
    }
    
    public java.math.BigDecimal getForeignTravelCostSharing() {
        return foreignTravelCostSharing;
    }
    public void setForeignTravelCostSharing(java.math.BigDecimal foreignTravelCostSharing) {
        this.foreignTravelCostSharing = foreignTravelCostSharing;
    }
    
    public java.math.BigDecimal getTotalTravelCostSharing() {
        return totalTravelCostSharing;
    }
    public void setTotalTravelCostSharing(java.math.BigDecimal totalTravelCostSharing) {
        this.totalTravelCostSharing = totalTravelCostSharing;
    }
    public java.math.BigDecimal getPartStipendCostSharing() {
        return partStipendCostSharing;
    }
    public void setPartStipendCostSharing(java.math.BigDecimal partStipendCostSharing) {
        this.partStipendCostSharing = partStipendCostSharing;
    }
    
    public java.math.BigDecimal getPartTravelCostSharing() {
        return partTravelCostSharing;
    }
    public void setPartTravelCostSharing(java.math.BigDecimal partTravelCostSharing) {
        this.partTravelCostSharing = partTravelCostSharing;
    }
    
    public java.math.BigDecimal getPartOtherCostSharing() {
        return partOtherCostSharing;
    }
    public void setPartOtherCostSharing(java.math.BigDecimal partOtherCostSharing) {
        this.partOtherCostSharing = partOtherCostSharing;
    }
    
    public java.math.BigDecimal getTotalNonFundsKeyPersons() {
        return totalNonFundsKeyPersons;
    }
    public void setTotalNonFundsKeyPersons(java.math.BigDecimal totalNonFundsKeyPersons) {
        this.totalNonFundsKeyPersons = totalNonFundsKeyPersons;
    }
    
    public java.math.BigDecimal getTotalNonFundsAttachedKeyPersons() {
        return totalNonFundsAttachedKeyPersons;
    }
    public void setTotalNonFundsAttachedKeyPersons(java.math.BigDecimal totalNonFundsAttachedKeyPersons) {
        this.totalNonFundsAttachedKeyPersons = totalNonFundsAttachedKeyPersons;
    }
    
    public java.math.BigDecimal getTotalOtherPersonnelNonFunds() {
        return totalOtherPersonnelNonFunds;
    }
    public void setTotalOtherPersonnelNonFunds(java.math.BigDecimal totalOtherPersonnelNonFunds) {
        this.totalOtherPersonnelNonFunds = totalOtherPersonnelNonFunds;
    }
    
    public java.math.BigDecimal getTotalCompensationCostSharing() {
        return totalCompensationCostSharing;
    }
    public void setTotalCompensationCostSharing(java.math.BigDecimal totalCompensationCostSharing) {
        this.totalCompensationCostSharing = totalCompensationCostSharing;
    }
    
     public java.math.BigDecimal getTotalDirectCostSharing() {
        return totalDirectCostSharing;
    }
    public void setTotalDirectCostSharing(java.math.BigDecimal totalDirectCostSharing) {
        this.totalDirectCostSharing = totalDirectCostSharing;
    }
    
    public java.math.BigDecimal getTotalIndirectCostSharing() {
        return totalIndirectCostSharing;
    }
    public void setTotalIndirectCostSharing(java.math.BigDecimal totalIndirectCostSharing) {
        this.totalIndirectCostSharing = totalIndirectCostSharing;
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
     * Getter for property totalFundsEquipment.
     * @return Value of property totalFundsEquipment.
     */
    public java.math.BigDecimal getTotalFundsEquipment() {
        return totalFundsEquipment;
    }    
    
    /**
     * Setter for property totalFundsEquipment.
     * @param totalFundsEquipment New value of property totalFundsEquipment.
     */
    public void setTotalFundsEquipment(java.math.BigDecimal totalFundsEquipment) {
        this.totalFundsEquipment = totalFundsEquipment;
    }
    
    /**
     * Getter for property totalFundsAttachedEquipment.
     * @return Value of property totalFundsAttachedEquipment.
     */
    public java.math.BigDecimal getTotalFundsAttachedEquipment() {
        return totalFundsAttachedEquipment;
    }
    
    /**
     * Setter for property totalFundsAttachedEquipment.
     * @param totalFundsAttachedEquipment New value of property totalFundsAttachedEquipment.
     */
    public void setTotalFundsAttachedEquipment(java.math.BigDecimal totalFundsAttachedEquipment) {
        this.totalFundsAttachedEquipment = totalFundsAttachedEquipment;
    }
    
    /**
     * Getter for property totalIndirectCost.
     * @return Value of property totalIndirectCost.
     */
    public java.math.BigDecimal getTotalIndirectCost() {
        return totalIndirectCost;
    }
    
    /**
     * Setter for property totalIndirectCost.
     * @param totalIndirectCost New value of property totalIndirectCost.
     */
    public void setTotalIndirectCost(java.math.BigDecimal totalIndirectCost) {
        this.totalIndirectCost = totalIndirectCost;
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