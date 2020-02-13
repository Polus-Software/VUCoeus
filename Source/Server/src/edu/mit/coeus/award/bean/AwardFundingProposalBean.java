/**
 * @(#)AwardFundingProposalBean.java 1.0 01/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;
import java.util.Vector;
import java.sql.Date;

/**
 * This class is used to hold Award Funding Proposal Bean 
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 05, 2004 12:30 PM
 */

public class AwardFundingProposalBean extends AwardBaseBean{
    
    private String proposalNumber;
    private int proposalSequenceNumber;
    private String awardAccountNumber;
    private int rowId;
    private int proposalTypeCode;
    private int proposalStatusCode;
    private Date requestStartDateTotal;
    private Date requestEndDateTotal;
    private double totalDirectCostTotal;
    private double totalInDirectCostTotal;
    private String proposalTypeDescription;
   /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
    private String leadUnit;
    private String piName;
   /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    private boolean canSyncIPInvToAward;
    private boolean canSyncIPCreditToAward;
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - End
    
    /**
     *	Default Constructor
     */
    
    public AwardFundingProposalBean(){
    }    

    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property proposalSequenceNumber.
     * @return Value of property proposalSequenceNumber.
     */
    public int getProposalSequenceNumber() {
        return proposalSequenceNumber;
    }    

    /** Setter for property proposalSequenceNumber.
     * @param proposalSequenceNumber New value of property proposalSequenceNumber.
     */
    public void setProposalSequenceNumber(int proposalSequenceNumber) {
        this.proposalSequenceNumber = proposalSequenceNumber;
    }
    
    /** Getter for property awardAccountNumber.
     * @return Value of property awardAccountNumber.
     */
    public java.lang.String getAwardAccountNumber() {
        return awardAccountNumber;
    }
    
    /** Setter for property awardAccountNumber.
     * @param awardAccountNumber New value of property awardAccountNumber.
     */
    public void setAwardAccountNumber(java.lang.String awardAccountNumber) {
        this.awardAccountNumber = awardAccountNumber;
    }    
    
    /** Getter for property rowId.
     * @return Value of property rowId.
     *
     */
    public int getRowId() {
        return rowId;
    }
    
    /** Setter for property rowId.
     * @param rowId New value of property rowId.
     *
     */
    public void setRowId(int rowId) {
        this.rowId = rowId;
    }    
    
     /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */    
    public boolean equals(Object obj) {
        AwardFundingProposalBean awardFundingProposalBean = (AwardFundingProposalBean)obj;
        if(super.equals(obj)){
            if(awardFundingProposalBean.getRowId() == getRowId()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    
    /** Getter for property proposalTypeCode.
     * @return Value of property proposalTypeCode.
     *
     */
    public int getProposalTypeCode() {
        return proposalTypeCode;
    }
    
    /** Setter for property proposalTypeCode.
     * @param proposalTypeCode New value of property proposalTypeCode.
     *
     */
    public void setProposalTypeCode(int proposalTypeCode) {
        this.proposalTypeCode = proposalTypeCode;
    }
    
    /** Getter for property proposalStatusCode.
     * @return Value of property proposalStatusCode.
     *
     */
    public int getProposalStatusCode() {
        return proposalStatusCode;
    }
    
    /** Setter for property proposalStatusCode.
     * @param proposalStatusCode New value of property proposalStatusCode.
     *
     */
    public void setProposalStatusCode(int proposalStatusCode) {
        this.proposalStatusCode = proposalStatusCode;
    }
    
    /** Getter for property requestStartDateTotal.
     * @return Value of property requestStartDateTotal.
     *
     */
    public java.sql.Date getRequestStartDateTotal() {
        return requestStartDateTotal;
    }
    
    /** Setter for property requestStartDateTotal.
     * @param requestStartDateTotal New value of property requestStartDateTotal.
     *
     */
    public void setRequestStartDateTotal(java.sql.Date requestStartDateTotal) {
        this.requestStartDateTotal = requestStartDateTotal;
    }
    
    /** Getter for property requestEndDateTotal.
     * @return Value of property requestEndDateTotal.
     *
     */
    public java.sql.Date getRequestEndDateTotal() {
        return requestEndDateTotal;
    }
    
    /** Setter for property requestEndDateTotal.
     * @param requestEndDateInitial New value of property requestEndDateTotal.
     *
     */
    public void setRequestEndDateTotal(java.sql.Date requestEndDateTotal) {
        this.requestEndDateTotal = requestEndDateTotal;
    }
    
    /** Getter for property totalDirectCostTotal.
     * @return Value of property totalDirectCostTotal.
     *
     */
    public double getTotalDirectCostTotal() {
        return totalDirectCostTotal;
    }
    
    /** Setter for property totalDirectCostTotal.
     * @param totalDirectCostTotal New value of property totalDirectCostTotal.
     *
     */
    public void setTotalDirectCostTotal(double totalDirectCostTotal) {
        this.totalDirectCostTotal = totalDirectCostTotal;
    }
    
    /** Getter for property totalInDirectCostTotal.
     * @return Value of property totalInDirectCostTotal.
     *
     */
    public double getTotalInDirectCostTotal() {
        return totalInDirectCostTotal;
    }
    
    /** Setter for property totalInDirectCostTotal.
     * @param totalInDirectCostTotal New value of property totalInDirectCostTotal.
     *
     */
    public void setTotalInDirectCostTotal(double totalInDirectCostTotal) {
        this.totalInDirectCostTotal = totalInDirectCostTotal;
    }
    
    /** Getter for property proposalTypeDescription.
     * @return Value of property proposalTypeDescription.
     *
     */
    public java.lang.String getProposalTypeDescription() {
        return proposalTypeDescription;
    }
    
    /** Setter for property proposalTypeDescription.
     * @param proposalTypeDescription New value of property proposalTypeDescription.
     *
     */
    public void setProposalTypeDescription(java.lang.String proposalTypeDescription) {
        this.proposalTypeDescription = proposalTypeDescription;
    }    
    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - start*/
     /** Getter for property leadUnit.
     * @return Value of property leadUnit.
     *
     */
    public String getLeadUnit() {
        return leadUnit;
    }
    
    /** Setter for property leadUnit.
     * @param proposalStatusCode New value of property leadUnit.
     *
     */
    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }
    
     /** Getter for property proposalTypeDescription.
     * @return Value of property proposalTypeDescription.
     *
     */
    public java.lang.String getPiName() {
        return piName;
    }
    
    /** Setter for property proposalTypeDescription.
     * @param proposalTypeDescription New value of property proposalTypeDescription.
     *
     */
    public void setPiName(java.lang.String piName) {
        this.piName = piName;
    }    
    /*Added for case 3186:Add fields to the Funding Proposals Screen in the Awards module  - end*/

    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - Start
    public boolean canSyncIPInvToAward() {
        return canSyncIPInvToAward;
    }

    public void setCanSyncIPInvToAward(boolean canSyncInvToAwardFromIP) {
        this.canSyncIPInvToAward = canSyncInvToAwardFromIP;
    }
    // Added for COEUSQA-3642 : Issues with COEUSQA-1676 Credit Split Infrastructure project - En

    public boolean canSyncIPCreditToAward() {
        return canSyncIPCreditToAward;
    }

    public void setCanSyncIPCreditToAward(boolean canSyncIPCreditToAward) {
        this.canSyncIPCreditToAward = canSyncIPCreditToAward;
    }
}