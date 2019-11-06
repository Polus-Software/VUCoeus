/*
 * @(#)OtherDirectCostBean.java 
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.CoeusVector;

import java.beans.*;
import java.math.BigDecimal;

public class OtherDirectCostBean implements CoeusBean, java.io.Serializable{

 
    
    private BigDecimal materials;
    private BigDecimal publications;
    private BigDecimal consultants;
    private BigDecimal computer;
    private BigDecimal subAwards;
    private BigDecimal equipRental;
    private BigDecimal alterations;
    private CoeusVector otherCosts;  //vector of hashmap of desc and cost
    private BigDecimal totalOtherDirect;
    
    private BigDecimal domTravel;
    private BigDecimal foreignTravel;
    private BigDecimal totTravel;
 
    private BigDecimal partTravel;
    private BigDecimal partStipends;
    private BigDecimal partOther;
    private BigDecimal participantTotal;
    private BigDecimal partTuition;       //addition for participant cost change
    private BigDecimal partSubsistence;   //addition for participant cost change
    
    //start add costSaring for fedNonFedBudget repport
    private BigDecimal materialsCostSharing;
    private BigDecimal publicationsCostSharing;
    private BigDecimal consultantsCostSharing;
    private BigDecimal computerCostSharing;
    private BigDecimal subAwardsCostSharing;
    private BigDecimal equipRentalCostSharing;
    private BigDecimal alterationsCostSharing;
    private BigDecimal domTravelCostSharing;
    private BigDecimal foreignTravelCostSharing;
    private BigDecimal totTravelCostSharing;
    private BigDecimal partStipendsCostSharing;
    private BigDecimal partTravelCostSharing;
    private BigDecimal partOtherCostSharing;
    private BigDecimal participantTotalCostSharing;
    private BigDecimal totalOtherDirectCostSharing;
    private BigDecimal partTuitionCostSharing;       //addition for participant cost change
    private BigDecimal partSubsistenceCostSharing;   //addition for participant cost change

 
//    private int partTravelCount;
//    private int partStipendsCount;
 //   private int partOtherCount;
    private int particpantTotalCount;
    
    //holds update user id - needed to implement CoeusBean
    private String updateUser = null;
    //holds update timestamp - needed to implement CoeusBean
    private java.sql.Timestamp updateTimestamp = null;
    //holds account type - needed to implement CoeusBean
    private String acType = null;

    /**
     *	Default Constructor
     */
    
    public OtherDirectCostBean(){
    }

    
    /** Getter for property totalOtherDirect
     * @return Value of property totalOtherDirect.
     */
    
    public BigDecimal gettotalOtherDirect() {
        return totalOtherDirect;
    }
    
    /** Setter for property totalOtherDirect
     * @param totalOtherDirect New value of property totalOtherDirect.
      */
    public void settotalOtherDirect(BigDecimal totalOtherDirect) {
        this.totalOtherDirect = totalOtherDirect;
    }
    
    
    /** Getter for property materials
     * @return Value of property materials.
      */
    public BigDecimal getmaterials() {
        return materials;
    }
    
    /** Setter for property materials.
     * @param materials New value of property materials.
      */
    public void setmaterials(BigDecimal materials) {
        this.materials = materials;
    }
    
    /** Getter for property publications
     * @return Value of property publications.
      */
    public BigDecimal getpublications() {
        return publications;
    }
    
    /** Setter for property publications.
     * @param publications New value of property publications.
      */
    public void setpublications(BigDecimal publications) {
        this.publications = publications;
    }

    /** Getter for property consultants
     * @return Value of property consultants.
      */
    public BigDecimal getconsultants() {
        return consultants;
    }
    
    /** Setter for property consultants.
     * @param consultants New value of property consultants.
    */
    public void setconsultants(BigDecimal consultants) {
        this.consultants = consultants;
    }
    
    /** Getter for property computer
     * @return Value of property computer.
      */
    public BigDecimal getcomputer() {
        return computer;
    }
    
    /** Setter for property computer.
     * @param computer New value of property computer.
      */
    public void setcomputer(BigDecimal computer) {
        this.computer = computer;
    }
    
     /** Getter for property subAwards
     * @return Value of property subAwards.
      */
    public BigDecimal getsubAwards() {
        return subAwards;
    }
    
    /** Setter for property subAwards.
     * @param subAwards New value of property subAwards.
      */
    public void setsubAwards(BigDecimal subAwards) {
        this.subAwards = subAwards;
    }
    
    /** Getter for property equipRental
     * @return Value of property EquipRental.
      */
    public BigDecimal getEquipRental() {
        return equipRental;
    }
    
    /** Setter for property equipRental.
     * @param equipRental New value of property equipRental.
      */
    public void setEquipRental(BigDecimal equipRental) {
        this.equipRental= equipRental;
    }
    
     /** Getter for property alterations
     * @return Value of property alterations.
      */
    public BigDecimal getAlterations() {
        return alterations;
    }
    
    /** Setter for property alterations.
     * @param alterations New value of property alterations.
      */
    public void setAlterations(BigDecimal alterations) {
        this.alterations= alterations;
    }
    
     /** Getter for property domTravel
     * @return Value of property domTravel.
     */
    public BigDecimal getDomTravel() {
        return domTravel;
    }
    
    /** Setter for property domTravel.
     * @param domTravel New value of property domTravel.
    */
    public void setDomTravel(BigDecimal domTravel) {
        this.domTravel= domTravel;
    }
    
     /** Getter for property foreignTravel
     * @return Value of property foreignTravel.
      */
    public BigDecimal getForeignTravel() {
        return foreignTravel;
    }
    
    /** Setter for property foreignTravel.
     * @param foreignTravel New value of property foreignTravel.
    */
    public void setForeignTravel(BigDecimal foreignTravel) {
        this.foreignTravel= foreignTravel;
    }
    
     /** Getter for property totTravel
     * @return Value of property totTravel.
     */
    public BigDecimal getTotTravel() {
        return totTravel;
    }
    
    /** Setter for property totTravel.
     * @param totTravel New value of property totTravel.
    */
    public void setTotTravel(BigDecimal totTravel) {
        this.totTravel= totTravel;
    }
    
    /** Getter for property otherCosts
     * @return Value of property otherCosts.
      */
    public CoeusVector getOtherCosts() {
        return otherCosts;
    }
    
    /** Setter for property otherCosts.
     * @param otherCosts New value of property otherCosts.
    */  
    public void setOtherCosts(CoeusVector otherCosts) {
        this.otherCosts= otherCosts;
    }
   
     /** Getter for property partStipends
     * @return Value of property partStipends.
     */
    public BigDecimal getPartStipends() {
        return partStipends;
    }
    
    /** Setter for property partStipends.
     * @param partStipends New value of property partStipends.
    */
    public void setPartStipends(BigDecimal partStipends) {
        this.partStipends = partStipends;
    }
    
   /** Getter for property partStipendsCount
    * @return Value of property partStipendsCount.
    */
//    public int getPartStipendsCount() {
 //       return partStipendsCount;
 //   }
    
    /** Setter for property partStipendsCount.
     * @param partStipendsCount New value of property partStipendsCount.
    */
  //  public void setPartStipendsCount(int partStipendsCount) {
   //     this.partStipendsCount = partStipendsCount;
  //  }
    
     /** Getter for property partOther
     * @return Value of property partOther.
     */
    public BigDecimal getPartOther() {
        return partOther;
    }
    
    /** Setter for property partOther.
     * @param partOther New value of property partOther.
    */
    public void setPartOther(BigDecimal partOther) {
        this.partOther= partOther;
    }
    
    /** Getter for property partOtherCount
     * @return Value of property partOtherCount.
     */
   // public int getPartOtherCount() {
   //     return partOtherCount;
  //  }
    
    /** Setter for property partOtherCount.
     * @param partOtherCount New value of property partOtherCount.
    */
   // public void setPartOtherCount(int partOtherCount) {
   //     this.partOtherCount= partOtherCount;
//    }
    
    /** Getter for property partTravel
     * @return Value of property partTravel.
     */
    public BigDecimal getPartTravel() {
        return partTravel;
    }
    
    /** Setter for property partTravel.
     * @param partTravel New value of property partTravel.
    */
    public void setPartTravel(BigDecimal partTravel) {
        this.partTravel= partTravel;
    }
    
    
     /** Getter for property partTravelCount
     * @return Value of property partTravelCount.
     */
   // public int getPartTravelCount() {
   //     return partTravelCount;
  //  }
    
    /** Setter for property partTravelCount.
     * @param partTravelCount New value of property partTravelCount.
    */
   // public void setPartTravelCount(int partTravelCount) {
   //     this.partTravelCount= partTravelCount;
   // }
    
     /** Getter for property participantTotal
     * @return Value of property participantTotal.
     */
    public BigDecimal getParticipantTotal() {
        return participantTotal;
    }
    
    /** Setter for property participantTotal.
     * @param participantTotal New value of property participantTotal.
    */
    public void setParticipantTotal(BigDecimal participantTotal) {
        this.participantTotal= participantTotal;
    }
    
      /** Getter for property particpantTotalCount
     * @return Value of property particpantTotalCount.
     */
    public int getParticpantTotalCount() {
        return particpantTotalCount;
    }
    
    /** Setter for property participantTotalCount.
     * @param participantTotalCount New value of property particpantTotalCount.
    */
    public void setParticipantTotalCount(int particpantTotalCount) {
        this.particpantTotalCount= particpantTotalCount;
    }
    
   //start add costSaring for fedNonFedBudget repport
    /** Getter for property materialsCostSharing
     * @return Value of property materialsCostSharing.
      */
    public BigDecimal getMaterialsCostSharing() {
        return materialsCostSharing;
    }
    
    /** Setter for property materialsCostSharing.
     * @param materialsCostSharing New value of property materialsCostSharing.
      */
    public void setMaterialsCostSharing(BigDecimal materialsCostSharing) {
        this.materialsCostSharing = materialsCostSharing;
    }
    
    public BigDecimal getPublicationsCostSharing() {
        return publicationsCostSharing;
    }
    public void setPublicationsCostSharing(BigDecimal publicationsCostSharing) {
        this.publicationsCostSharing = publicationsCostSharing;
    }
    
     public BigDecimal getConsultantsCostSharing() {
        return consultantsCostSharing;
    }
    public void setConsultantsCostSharing(BigDecimal consultantsCostSharing) {
        this.consultantsCostSharing = consultantsCostSharing;
    }
    
    public BigDecimal getComputerCostSharing() {
        return computerCostSharing;
    }
    public void setComputerCostSharing(BigDecimal computerCostSharing) {
        this.computerCostSharing = computerCostSharing;
    }
     public BigDecimal getSubAwardsCostSharing() {
        return subAwardsCostSharing;
    }
    public void setSubAwardsCostSharing(BigDecimal subAwardsCostSharing) {
        this.subAwardsCostSharing = subAwardsCostSharing;
    }
    
    public BigDecimal getEquipRentalCostSharing() {
        return equipRentalCostSharing;
    }
    public void setEquipRentalCostSharing(BigDecimal equipRentalCostSharing) {
        this.equipRentalCostSharing = equipRentalCostSharing;
    }
    
    public BigDecimal getAlterationsCostSharing() {
        return alterationsCostSharing;
    }
    public void setAlterationsCostSharing(BigDecimal alterationsCostSharing) {
        this.alterationsCostSharing = alterationsCostSharing;
    }
    
    public BigDecimal getDomTravelCostSharing() {
        return domTravelCostSharing;
    }
    public void setDomTravelCostSharing(BigDecimal domTravelCostSharing) {
        this.domTravelCostSharing = domTravelCostSharing;
    }
        
    public BigDecimal getForeignTravelCostSharing() {
        return foreignTravelCostSharing;
    }
    public void setForeignTravelCostSharing(BigDecimal foreignTravelCostSharing) {
        this.foreignTravelCostSharing = foreignTravelCostSharing;
    }
    
    public BigDecimal getTotTravelCostSharing() {
        return totTravelCostSharing;
    }
    public void setTotTravelCostSharing(BigDecimal totTravelCostSharing) {
        this.totTravelCostSharing = totTravelCostSharing;
    }
        
    public BigDecimal getPartStipendsCostSharing() {
        return partStipendsCostSharing;
    }
    public void setPartStipendsCostSharing(BigDecimal partStipendsCostSharing) {
        this.partStipendsCostSharing = partStipendsCostSharing;
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
          
    public BigDecimal getParticipantTotalCostSharing() {
        return participantTotalCostSharing;
    }
    public void setParticipantTotalCostSharing(BigDecimal participantTotalCostSharing) {
        this.participantTotalCostSharing = participantTotalCostSharing;
    }
    
    public BigDecimal getTotalOtherDirectCostSharing() {
        return totalOtherDirectCostSharing;
    }
    public void setTotalOtherDirectCostSharing(BigDecimal totalOtherDirectCostSharing) {
        this.totalOtherDirectCostSharing = totalOtherDirectCostSharing;
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
    
}