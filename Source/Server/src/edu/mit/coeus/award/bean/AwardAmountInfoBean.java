/**
 * @(#)AwardAmountInfoBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * This class is used to set and get the Award Header
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 03, 2004 12:30 PM
 */

public class AwardAmountInfoBean extends AwardHierarchyBean implements java.io.Serializable{
    
    //private String mitAwardNumber;
    //private int sequenceNumber;
    private int amountSequenceNumber;
    private double anticipatedTotalAmount;
    private double anticipatedDistributableAmount;
    private Date finalExpirationDate;
    private Date currentFundEffectiveDate;
    private Date effectiveDate;
    private double amountObligatedToDate;
    private double obliDistributableAmount;
    private Date obligationExpirationDate;
    private String transactionId;
    private String entryType;
    private boolean eomProcessFlag;
    private double anticipatedChange;
    private double obligatedChange; 
    
    //Case 1822 Start
    private double totalFNACost;
    private Date awardEffectiveDate;
    //Case 1822 End
    
    //private int awardStatusCode;
    //private String updateUser;
    //private java.sql.Timestamp updateTimestamp;    
    
    private String title;
    //Added for Case:3122 - Award Notice Enhancement -Start
    private Date totalStartDate;
    private Date totalEndDate;
    private double totalObligatedAmount;
    private double totalDistributableAmount;
    private double childGrantTotal;
    private double remainingAnticipatedAmt;
    //Added for Case:3122 - Award Notice Enhancement -End
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    private AwardAmountTransactionBean awardAmountTransaction;
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end
    
    //#Case 3857 -- start
    private double directObligatedChange;
    private double indirectObligatedChange;
    private double directAnticipatedChange;
    private double indirectAnticipatedChange;
    private double directObligatedTotal;
    private double indirectObligatedTotal;
    private double directAnticipatedTotal;
    private double indirectAnticipatedTotal;
     //#Case 3857 -- end
    /**
     *	Default Constructor
     */
    
    public AwardAmountInfoBean(){
    }    
    
   /**
   *	Copy Constructor
   */    
    public AwardAmountInfoBean(AwardHierarchyBean awardHierarchyBean){
        setRootMitAwardNumber(awardHierarchyBean.getRootMitAwardNumber());
        setParentMitAwardNumber(awardHierarchyBean.getParentMitAwardNumber());
        setAccountNumber(awardHierarchyBean.getAccountNumber());
        setStatusCode(awardHierarchyBean.getStatusCode());
        setChildCount(awardHierarchyBean.getChildCount());
        setUpdateUser(awardHierarchyBean.getUpdateUser());
        setUpdateTimestamp(awardHierarchyBean.getUpdateTimestamp());
    }

//    /** Getter for property mitAwardNumber.
//     * @return Value of property mitAwardNumber.
//     */
//    public java.lang.String getMitAwardNumber() {
//        return mitAwardNumber;
//    }    
//    
//    /** Setter for property mitAwardNumber.
//     * @param mitAwardNumber New value of property mitAwardNumber.
//     */
//    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
//        this.mitAwardNumber = mitAwardNumber;
//    }
//    
//    /** Getter for property sequenceNumber.
//     * @return Value of property sequenceNumber.
//     */
//    public int getSequenceNumber() {
//        return sequenceNumber;
//    }
//    
//    /** Setter for property sequenceNumber.
//     * @param sequenceNumber New value of property sequenceNumber.
//     */
//    public void setSequenceNumber(int sequenceNumber) {
//        this.sequenceNumber = sequenceNumber;
//    }    
    
    /** Getter for property amountSequenceNumber.
     * @return Value of property amountSequenceNumber.
     */
    public int getAmountSequenceNumber() {
        return amountSequenceNumber;
    }
    
    /** Setter for property amountSequenceNumber.
     * @param amountSequenceNumber New value of property amountSequenceNumber.
     */
    public void setAmountSequenceNumber(int amountSequenceNumber) {
        this.amountSequenceNumber = amountSequenceNumber;
    }
    
    /** Getter for property anticipatedTotalAmount.
     * @return Value of property anticipatedTotalAmount.
     */
    public double getAnticipatedTotalAmount() {
        return anticipatedTotalAmount;
    }
    
    /** Setter for property anticipatedTotalAmount.
     * @param anticipatedTotalAmount New value of property anticipatedTotalAmount.
     */
    public void setAnticipatedTotalAmount(double anticipatedTotalAmount) {
        this.anticipatedTotalAmount = anticipatedTotalAmount;
    }
    
    /** Getter for property anticipatedDistributableAmount.
     * @return Value of property anticipatedDistributableAmount.
     */
    public double getAnticipatedDistributableAmount() {
        return anticipatedDistributableAmount;
    }
    
    /** Setter for property anticipatedDistributableAmount.
     * @param anticipatedDistributableAmount New value of property anticipatedDistributableAmount.
     */
    public void setAnticipatedDistributableAmount(double anticipatedDistributableAmount) {
        this.anticipatedDistributableAmount = anticipatedDistributableAmount;
    }
    
    /** Getter for property finalExpirationDate.
     * @return Value of property finalExpirationDate.
     */
    public java.sql.Date getFinalExpirationDate() {
        return finalExpirationDate;
    }
    
    /** Setter for property finalExpirationDate.
     * @param finalExpirationDate New value of property finalExpirationDate.
     */
    public void setFinalExpirationDate(java.sql.Date finalExpirationDate) {
        this.finalExpirationDate = finalExpirationDate;
    }
    
    /** Getter for property currentFundEffectiveDate.
     * @return Value of property currentFundEffectiveDate.
     */
    public java.sql.Date getCurrentFundEffectiveDate() {
        return currentFundEffectiveDate;
    }
    
    /** Setter for property currentFundEffectiveDate.
     * @param currentFundEffectiveDate New value of property currentFundEffectiveDate.
     */
    public void setCurrentFundEffectiveDate(java.sql.Date currentFundEffectiveDate) {
        this.currentFundEffectiveDate = currentFundEffectiveDate;
    }
    
    /** Getter for property amountObligatedToDate.
     * @return Value of property amountObligatedToDate.
     */
    public double getAmountObligatedToDate() {
        return amountObligatedToDate;
    }
    
    /** Setter for property amountObligatedToDate.
     * @param amountObligatedToDate New value of property amountObligatedToDate.
     */
    public void setAmountObligatedToDate(double amountObligatedToDate) {
        this.amountObligatedToDate = amountObligatedToDate;
    }
    
    /** Getter for property obliDistributableAmount.
     * @return Value of property obliDistributableAmount.
     */
    public double getObliDistributableAmount() {
        return obliDistributableAmount;
    }
    
    /** Setter for property obliDistributableAmount.
     * @param obliDistributableAmount New value of property obliDistributableAmount.
     */
    public void setObliDistributableAmount(double obliDistributableAmount) {
        this.obliDistributableAmount = obliDistributableAmount;
    }
    
    /** Getter for property obligationExpirationDate.
     * @return Value of property obligationExpirationDate.
     */
    public java.sql.Date getObligationExpirationDate() {
        return obligationExpirationDate;
    }
    
    /** Setter for property obligationExpirationDate.
     * @param obligationExpirationDate New value of property obligationExpirationDate.
     */
    public void setObligationExpirationDate(java.sql.Date obligationExpirationDate) {
        this.obligationExpirationDate = obligationExpirationDate;
    }
    
    /** Getter for property transactionId.
     * @return Value of property transactionId.
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }
    
    /** Setter for property transactionId.
     * @param transactionId New value of property transactionId.
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }
    
    /** Getter for property entryType.
     * @return Value of property entryType.
     */
    public java.lang.String getEntryType() {
        return entryType;
    }
    
    /** Setter for property entryType.
     * @param entryType New value of property entryType.
     */
    public void setEntryType(java.lang.String entryType) {
        this.entryType = entryType;
    }
    
    /** Getter for property eomProcessFlag.
     * @return Value of property eomProcessFlag.
     */
    public boolean isEomProcessFlag() {
        return eomProcessFlag;
    }
    
    /** Setter for property eomProcessFlag.
     * @param eomProcessFlag New value of property eomProcessFlag.
     */
    public void setEomProcessFlag(boolean eomProcessFlag) {
        this.eomProcessFlag = eomProcessFlag;
    }
    
    /** Getter for property anticipatedChange.
     * @return Value of property anticipatedChange.
     */
    public double getAnticipatedChange() {
        return anticipatedChange;
    }
    
    /** Setter for property anticipatedChange.
     * @param anticipatedChange New value of property anticipatedChange.
     */
    public void setAnticipatedChange(double anticipatedChange) {
        this.anticipatedChange = anticipatedChange;
    }
    
    /** Getter for property obligatedChange.
     * @return Value of property obligatedChange.
     */
    public double getObligatedChange() {
        return obligatedChange;
    }
    
    /** Setter for property obligatedChange.
     * @param obligatedChange New value of property obligatedChange.
     */
    public void setObligatedChange(double obligatedChange) {
        this.obligatedChange = obligatedChange;
    }
    
//    /** Getter for property updateUser.
//     * @return Value of property updateUser.
//     */
//    public java.lang.String getUpdateUser() {
//        return updateUser;
//    }
//    
//    /** Setter for property updateUser.
//     * @param updateUser New value of property updateUser.
//     */
//    public void setUpdateUser(java.lang.String updateUser) {
//        this.updateUser = updateUser;
//    }
//    
//    /** Getter for property updateTimestamp.
//     * @return Value of property updateTimestamp.
//     */
//    public java.sql.Timestamp getUpdateTimestamp() {
//        return updateTimestamp;
//    }
//    
//    /** Setter for property updateTimestamp.
//     * @param updateTimestamp New value of property updateTimestamp.
//     */
//    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
//        this.updateTimestamp = updateTimestamp;
//    }    
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     */
    public java.sql.Date getEffectiveDate() {
        return effectiveDate;
    }
    
    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.sql.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /** Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /** Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof AwardAmountInfoBean){
            if(super.equals(obj)){
                AwardAmountInfoBean awardAmountInfoBean = (AwardAmountInfoBean)obj;
                if(awardAmountInfoBean.getSequenceNumber() == getSequenceNumber() &&
                    awardAmountInfoBean.getAmountSequenceNumber() == getAmountSequenceNumber()){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    //Case 1822 Start 2
    
    /**
     * Getter for property totalFNACost.
     * @return Value of property totalFNACost.
     */
    public double getTotalFNACost() {
        return totalFNACost;
    }
    
    /**
     * Setter for property totalFNACost.
     * @param totalFNACost New value of property totalFNACost.
     */
    public void setTotalFNACost(double totalFNACost) {
        this.totalFNACost = totalFNACost;
    }
    
    /**
     * Getter for property awardEffectiveDate.
     * @return Value of property awardEffectiveDate.
     */
    public java.sql.Date getAwardEffectiveDate() {
        return awardEffectiveDate;
    }    
    
    /**
     * Setter for property awardEffectiveDate.
     * @param awardEffectiveDate New value of property awardEffectiveDate.
     */
    public void setAwardEffectiveDate(java.sql.Date awardEffectiveDate) {
        this.awardEffectiveDate = awardEffectiveDate;
    }
    
    /**
     * Getter for property totalStartDate.
     * @return Value of property totalStartDate.
     */
    public java.sql.Date getTotalStartDate() {
        return totalStartDate;
    }
    
    /**
     * Setter for property totalStartDate.
     * @param totalStartDate New value of property totalStartDate.
     */
    public void setTotalStartDate(java.sql.Date totalStartDate) {
        this.totalStartDate = totalStartDate;
    }
    
    /**
     * Getter for property totalEndDate.
     * @return Value of property totalEndDate.
     */
    public java.sql.Date getTotalEndDate() {
        return totalEndDate;
    }
    
    /**
     * Setter for property totalEndDate.
     * @param totalEndDate New value of property totalEndDate.
     */
    public void setTotalEndDate(java.sql.Date totalEndDate) {
        this.totalEndDate = totalEndDate;
    }
    
    /**
     * Getter for property totalObligatedAmount.
     * @return Value of property totalObligatedAmount.
     */
    public double getTotalObligatedAmount() {
        return totalObligatedAmount;
    }
    
    /**
     * Setter for property totalObligatedAmount.
     * @param totalObligatedAmount New value of property totalObligatedAmount.
     */
    public void setTotalObligatedAmount(double totalObligatedAmount) {
        this.totalObligatedAmount = totalObligatedAmount;
    }
    
    /**
     * Getter for property totalDistributableAmount.
     * @return Value of property totalDistributableAmount.
     */
    public double getTotalDistributableAmount() {
        return totalDistributableAmount;
    }
    
    /**
     * Setter for property totalDistributableAmount.
     * @param totalDistributableAmount New value of property totalDistributableAmount.
     */
    public void setTotalDistributableAmount(double totalDistributableAmount) {
        this.totalDistributableAmount = totalDistributableAmount;
    }
    
    /**
     * Getter for property childGrantTotal.
     * @return Value of property childGrantTotal.
     */
    public double getChildGrantTotal() {
        return childGrantTotal;
    }
    
    /**
     * Setter for property childGrantTotal.
     * @param childGrantTotal New value of property childGrantTotal.
     */
    public void setChildGrantTotal(double childGrantTotal) {
        this.childGrantTotal = childGrantTotal;
    }
    
    /**
     * Getter for property remainingAnticipatedAmt.
     * @return Value of property remainingAnticipatedAmt.
     */
    public double getRemainingAnticipatedAmt() {
        return remainingAnticipatedAmt;
    }
    
    /**
     * Setter for property remainingAnticipatedAmt.
     * @param remainingAnticipatedAmt New value of property remainingAnticipatedAmt.
     */
    public void setRemainingAnticipatedAmt(double remainingAnticipatedAmt) {
        this.remainingAnticipatedAmt = remainingAnticipatedAmt;
    }
    
    //Case 1822 End 2
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - start
    public AwardAmountTransactionBean getAwardAmountTransaction() {
        return awardAmountTransaction;
    }

    public void setAwardAmountTransaction(AwardAmountTransactionBean awardAmountTransaction) {
        this.awardAmountTransaction= awardAmountTransaction;
    }
    //Added for Case 2269: Money & End Dates Tab/Panel in Awards Module - end

    //#Case 3857 --  Added to include deirect and indirect obligated and anticipated amount  start
    public double getDirectObligatedChange() {
        return directObligatedChange;
    }

    public void setDirectObligatedChange(double directObligatedChange) {
        this.directObligatedChange = directObligatedChange;
    }

    public double getIndirectObligatedChange() {
        return indirectObligatedChange;
    }

    public void setIndirectObligatedAmount(double indirectObligatedChange) {
        this.indirectObligatedChange = indirectObligatedChange;
    }

    public double getDirectAnticipatedChange() {
        return directAnticipatedChange;
    }

    public void setDirectAnticipatedChange(double directAnticipatedChange) {
        this.directAnticipatedChange = directAnticipatedChange;
    }

    public double getIndirectAnticipatedChange() {
        return indirectAnticipatedChange;
    }

    public void setIndirectAnticipatedChange(double indirectAnticipatedChange) {
        this.indirectAnticipatedChange = indirectAnticipatedChange;
    }
    

    public double getDirectObligatedTotal() {
        return directObligatedTotal;
    }

    public void setDirectObligatedTotal(double directObligatedTotal) {
        this.directObligatedTotal = directObligatedTotal;
    }

    public double getIndirectObligatedTotal() {
        return indirectObligatedTotal;
    }

    public void setIndirectObligatedTotal(double indirectObligatedTotal) {
        this.indirectObligatedTotal = indirectObligatedTotal;
    }

    public double getDirectAnticipatedTotal() {
        return directAnticipatedTotal;
    }

    public void setDirectAnticipatedTotal(double directAnticipatedTotal) {
        this.directAnticipatedTotal = directAnticipatedTotal;
    }

    public double getIndirectAnticipatedTotal() {
        return indirectAnticipatedTotal;
    }

    public void setIndirectAnticipatedTotal(double indirectAnticipatedTotal) {
        this.indirectAnticipatedTotal = indirectAnticipatedTotal;
    }
     //#Case 3857 -- end
}