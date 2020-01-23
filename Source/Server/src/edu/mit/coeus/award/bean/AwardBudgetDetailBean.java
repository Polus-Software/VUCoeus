/*
 * AwardBudgetDetailBean.java
 *
 * Created on August 9, 2005, 12:10 PM
 */

/*
 * PMD check performed, and commented unused imports and variables on 24-Aug-2011
 * by Bharati Umarani
 */

package edu.mit.coeus.award.bean;

import java.beans.*;
//import java.io.Serializable;

/**
 * @author ajaygm
 */
public class AwardBudgetDetailBean extends AwardBaseBean{

    private int amountSequenceNo;
    private int versionNo;
    private int lineItemNo;
    private String costElement;
    private String costElementDescription;
    private double oblChangeAmount;
    private double oblAmount;
    private boolean modifiedByUser;
    private String rateClassType;
    private boolean onOffCampusFlag;
    
    private int awLineItemNo;
    
    //Used only for displaying the Line nos on the client side
    private int lineItemNoDisplay;
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    private String proposalNumber;
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets- end
    
    //Used for storing the calculated EB amount for ON off campus
    private double calcOnEBAmount;
    private double calcOffEBAmount;
    
    //Used for storing the calculated EB amount
    private double calcEBAmount;
    
    /**
     * Getter for property amountSequenceNo.
     * @return Value of property amountSequenceNo.
     */
    public int getAmountSequenceNo() {
        return amountSequenceNo;
    }
    
    /**
     * Setter for property amountSequenceNo.
     * @param amountSequenceNo New value of property amountSequenceNo.
     */
    public void setAmountSequenceNo(int amountSequenceNo) {
        this.amountSequenceNo = amountSequenceNo;
    }
    
    /**
     * Getter for property versionNo.
     * @return Value of property versionNo.
     */
    public int getVersionNo() {
        return versionNo;
    }
    
    /**
     * Setter for property versionNo.
     * @param versionNo New value of property versionNo.
     */
    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }
    
    /**
     * Getter for property lineItemNo.
     * @return Value of property lineItemNo.
     */
    public int getLineItemNo() {
        return lineItemNo;
    }
    
    /**
     * Setter for property lineItemNo.
     * @param lineItemNo New value of property lineItemNo.
     */
    public void setLineItemNo(int lineItemNo) {
        this.lineItemNo = lineItemNo;
    }
    
    /**
     * Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /**
     * Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }
    
    /**
     * Getter for property costElementDescription.
     * @return Value of property costElementDescription.
     */
    public java.lang.String getCostElementDescription() {
        return costElementDescription;
    }
    
    /**
     * Setter for property costElementDescription.
     * @param costElementDescription New value of property costElementDescription.
     */
    public void setCostElementDescription(java.lang.String costElementDescription) {
        this.costElementDescription = costElementDescription;
    }
    
    /**
     * Getter for property oblChangeAmount.
     * @return Value of property oblChangeAmount.
     */
    public double getOblChangeAmount() {
        return oblChangeAmount;
    }
    
    /**
     * Setter for property oblChangeAmount.
     * @param oblChangeAmount New value of property oblChangeAmount.
     */
    public void setOblChangeAmount(double oblChangeAmount) {
        this.oblChangeAmount = oblChangeAmount;
    }
    
    /**
     * Getter for property oblAmount.
     * @return Value of property oblAmount.
     */
    public double getOblAmount() {
        return oblAmount;
    }
    
    /**
     * Setter for property oblAmount.
     * @param oblAmount New value of property oblAmount.
     */
    public void setOblAmount(double oblAmount) {
        this.oblAmount = oblAmount;
    }
    
    /**
     * Getter for property modifiedByUser.
     * @return Value of property modifiedByUser.
     */
    public boolean isModifiedByUser() {
        return modifiedByUser;
    }
    
    /**
     * Setter for property modifiedByUser.
     * @param modifiedByUser New value of property modifiedByUser.
     */
    public void setModifiedByUser(boolean modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }
    
    /**
     * Getter for property awLineItemNo.
     * @return Value of property awLineItemNo.
     */
    public int getAwLineItemNo() {
        return awLineItemNo;
    }
    
    /**
     * Setter for property awLineItemNo.
     * @param awLineItemNo New value of property awLineItemNo.
     */
    public void setAwLineItemNo(int awLineItemNo) {
        this.awLineItemNo = awLineItemNo;
    }
    
    /**
     * Getter for property lineItemNoDisplay.
     * @return Value of property lineItemNoDisplay.
     */
    public int getLineItemNoDisplay() {
        return lineItemNoDisplay;
    }
    
    /**
     * Setter for property lineItemNoDisplay.
     * @param lineItemNoDisplay New value of property lineItemNoDisplay.
     */
    public void setLineItemNoDisplay(int lineItemNoDisplay) {
        this.lineItemNoDisplay = lineItemNoDisplay;
    }
    
    /**
     * Getter for property rateClassType.
     * @return Value of property rateClassType.
     */
    public java.lang.String getRateClassType() {
        return rateClassType;
    }
    
    /**
     * Setter for property rateClassType.
     * @param rateClassType New value of property rateClassType.
     */
    public void setRateClassType(java.lang.String rateClassType) {
        this.rateClassType = rateClassType;
    }
    
    /**
     * Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /**
     * Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /**
     * Getter for property calcEBAmount.
     * @return Value of property calcEBAmount.
     */
    public double getCalcEBAmount() {
        return calcEBAmount;
    }
    
    /**
     * Setter for property calcEBAmount.
     * @param calcEBAmount New value of property calcEBAmount.
     */
    public void setCalcEBAmount(double calcEBAmount) {
        this.calcEBAmount = calcEBAmount;
    }
    
    /**
     * Getter for property calcOnEBAmount.
     * @return Value of property calcOnEBAmount.
     */
    public double getCalcOnEBAmount() {
        return calcOnEBAmount;
    }
    
    /**
     * Setter for property calcOnEBAmount.
     * @param calcOnEBAmount New value of property calcOnEBAmount.
     */
    public void setCalcOnEBAmount(double calcOnEBAmount) {
        this.calcOnEBAmount = calcOnEBAmount;
    }
    
    /**
     * Getter for property calcOffEBAmount.
     * @return Value of property calcOffEBAmount.
     */
    public double getCalcOffEBAmount() {
        return calcOffEBAmount;
    }
    
    /**
     * Setter for property calcOffEBAmount.
     * @param calcOffEBAmount New value of property calcOffEBAmount.
     */
    public void setCalcOffEBAmount(double calcOffEBAmount) {
        this.calcOffEBAmount = calcOffEBAmount;
    }
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - start
    /** Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public String getProposalNumber() {
        return proposalNumber;
    }
    
    /** Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    //Added for COEUSQA-3273 : Inactive Cost Element in use Error Alert required for Proposal and Award Budgets - end
    
}
