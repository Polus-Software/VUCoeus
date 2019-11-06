/*
 * @(#)BudgetInfo.java September 25, 2003, 9:58 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
/*
 * PMD check performed, and commented unused imports and variables on 01-MAR-2011
 * by Maharaja Palanichamy
*/

package edu.mit.coeus.budget.bean;

import edu.mit.coeus.bean.PrimaryKey;
//import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;

/**
 * The class used to hold the information of <code>Budget Details</code>
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on September 25, 2003, 9:58 AM
 */
public class BudgetDetailBean extends BudgetBean implements PrimaryKey{
    //holds budget period
    private int budgetPeriod = -1;
    //holds lineItemNumber
    private int lineItemNumber = -1;
    //holds budget category code
    private int budgetCategoryCode = -1;
    //holds cost element
    private String costElement = null;
    //holds cost element description
    private String costElementDescription = null;
    //holds line Item description
    private String lineItemDescription = null;
    //holds based on line item
    private int basedOnLineItem = -1;
    //holds line item sequence
    private int lineItemSequence = -1;
    //holds Line Item Start Date date
    private java.sql.Date lineItemStartDate = null;
    //holds Line Item End Date date
    private java.sql.Date lineItemEndDate = null;
    //holds the line Item cost
    private double lineItemCost;
    //holds the cost sharing amount
    private double costSharingAmount;
    //holds the cost underRecovery amount
    private double underRecoveryAmount;
    //holds the on Off Campus flag
    private boolean onOffCampusFlag;
    //holds the Apply in rate flag
    private boolean applyInRateFlag;
    //holds budget_Justification
    private String budgetJustification = null;
    //holds category type
    private char categoryType;
    //holds quantity
//    private int quantity = -1;   //Commented for Case # 3132 Changing quantity field from integer to float
    //holds Direct Cost
    private double directCost;    
    //holds Indirect Cost
    private double indirectCost;
    //holds the calculated CostSharing
    private double totalCostSharing;
    //Added for Case # 3132 - start
    //Changing quantity field from integer to float
    //holds quantity
    private double quantity;
    //Added for Case # 3132 - end
    //COEUSQA-1693 - Cost Sharing Submission
    private boolean submitCostSharingFlag;
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    //holds unit number
    private String unitNumber = null;
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    private int subAwardNumber;
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    private boolean isFormualtedLineItem;
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - End
    
    /** Creates a new instance of BudgetInfo */
    public BudgetDetailBean() {
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
    
    /** Getter for property lineItemNumber.
     * @return Value of property lineItemNumber.
     */
    public int getLineItemNumber() {
        return lineItemNumber;
    }
    
    /** Setter for property lineItemNumber.
     * @param lineItemNumber New value of property lineItemNumber.
     */
    public void setLineItemNumber(int lineItemNumber) {
        this.lineItemNumber = lineItemNumber;
    }
    
    /** Getter for property budgetCategoryCode.
     * @return Value of property budgetCategoryCode.
     */
    public int getBudgetCategoryCode() {
        return budgetCategoryCode;
    }
    
    /** Setter for property budgetCategoryCode.
     * @param budgetCategoryCode New value of property budgetCategoryCode.
     */
    public void setBudgetCategoryCode(int budgetCategoryCode) {
        this.budgetCategoryCode = budgetCategoryCode;
    }
    
    /** Getter for property costElement.
     * @return Value of property costElement.
     */
    public java.lang.String getCostElement() {
        return costElement;
    }
    
    /** Setter for property costElement.
     * @param costElement New value of property costElement.
     */
    public void setCostElement(java.lang.String costElement) {
        this.costElement = costElement;
    }
    
    /** Getter for property costElementDescription.
     * @return Value of property costElementDescription.
     */
    public java.lang.String getCostElementDescription() {
        return costElementDescription;
    }
    
    /** Setter for property costElementDescription.
     * @param costElementDescription New value of property costElementDescription.
     */
    public void setCostElementDescription(java.lang.String costElementDescription) {
        this.costElementDescription = costElementDescription;
    }
    
    /** Getter for property lineItemDescription.
     * @return Value of property lineItemDescription.
     */
    public java.lang.String getLineItemDescription() {
        return lineItemDescription;
    }
    
    /** Setter for property lineItemDescription.
     * @param lineItemDescription New value of property lineItemDescription.
     */
    public void setLineItemDescription(java.lang.String lineItemDescription) {
        this.lineItemDescription = lineItemDescription;
    }
    
    /** Getter for property basedOnLineItem.
     * @return Value of property basedOnLineItem.
     */
    public int getBasedOnLineItem() {
        return basedOnLineItem;
    }
    
    /** Setter for property basedOnLineItem.
     * @param basedOnLineItem New value of property basedOnLineItem.
     */
    public void setBasedOnLineItem(int basedOnLineItem) {
        this.basedOnLineItem = basedOnLineItem;
    }
    
    /** Getter for property lineItemSequence.
     * @return Value of property lineItemSequence.
     */
    public int getLineItemSequence() {
        return lineItemSequence;
    }
    
    /** Setter for property lineItemSequence.
     * @param lineItemSequence New value of property lineItemSequence.
     */
    public void setLineItemSequence(int lineItemSequence) {
        this.lineItemSequence = lineItemSequence;
    }
    
    /** Getter for property lineItemStartDate.
     * @return Value of property lineItemStartDate.
     */
    public java.sql.Date getLineItemStartDate() {
        return lineItemStartDate;
    }
    
    /** Setter for property lineItemStartDate.
     * @param lineItemStartDate New value of property lineItemStartDate.
     */
    public void setLineItemStartDate(java.sql.Date lineItemStartDate) {
        this.lineItemStartDate = lineItemStartDate;
    }
    
    /** Getter for property lineItemEndDate.
     * @return Value of property lineItemEndDate.
     */
    public java.sql.Date getLineItemEndDate() {
        return lineItemEndDate;
    }
    
    /** Setter for property lineItemEndDate.
     * @param lineItemEndDate New value of property lineItemEndDate.
     */
    public void setLineItemEndDate(java.sql.Date lineItemEndDate) {
        this.lineItemEndDate = lineItemEndDate;
    }
    
    /** Getter for property lineItemCost.
     * @return Value of property lineItemCost.
     */
    public double getLineItemCost() {
        return lineItemCost;
    }
    
    /** Setter for property lineItemCost.
     * @param lineItemCost New value of property lineItemCost.
     */
    public void setLineItemCost(double lineItemCost) {
        this.lineItemCost = lineItemCost;
    }
    
    /** Getter for property costSharingAmount.
     * @return Value of property costSharingAmount.
     */
    public double getCostSharingAmount() {
        return costSharingAmount;
    }
    
    /** Setter for property costSharingAmount.
     * @param costSharingAmount New value of property costSharingAmount.
     */
    public void setCostSharingAmount(double costSharingAmount) {
        this.costSharingAmount = costSharingAmount;
    }
    
    /** Getter for property underRecoveryAmount.
     * @return Value of property underRecoveryAmount.
     */
    public double getUnderRecoveryAmount() {
        return underRecoveryAmount;
    }
    
    /** Setter for property underRecoveryAmount.
     * @param underRecoveryAmount New value of property underRecoveryAmount.
     */
    public void setUnderRecoveryAmount(double underRecoveryAmount) {
        this.underRecoveryAmount = underRecoveryAmount;
    }
    
    /** Getter for property onOffCampusFlag.
     * @return Value of property onOffCampusFlag.
     */
    public boolean isOnOffCampusFlag() {
        return onOffCampusFlag;
    }
    
    /** Setter for property onOffCampusFlag.
     * @param onOffCampusFlag New value of property onOffCampusFlag.
     */
    public void setOnOffCampusFlag(boolean onOffCampusFlag) {
        this.onOffCampusFlag = onOffCampusFlag;
    }
    
    /** Getter for property applyInRateFlag.
     * @return Value of property applyInRateFlag.
     */
    public boolean isApplyInRateFlag() {
        return applyInRateFlag;
    }
    
    /** Setter for property applyInRateFlag.
     * @param applyInRateFlag New value of property applyInRateFlag.
     */
    public void setApplyInRateFlag(boolean applyInRateFlag) {
        this.applyInRateFlag = applyInRateFlag;
    }
    
    /** Getter for property budgetJustification.
     * @return Value of property budgetJustification.
     */
    public java.lang.String getBudgetJustification() {
        return budgetJustification;
    }
    
    /** Setter for property budgetJustification.
     * @param budgetJustification New value of property budgetJustification.
     */
    public void setBudgetJustification(java.lang.String budgetJustification) {
        this.budgetJustification = budgetJustification;
    }
    
    /** Getter for property quantity.
     * @return Value of property quantity.
     */
    //Modified for Case # 3132 - start
    //Changing quantity field from integer to float
//    public int getQuantity() {
//        return quantity;
//    }
    public double getQuantity() {
        return quantity;
    }
    
    /** Setter for property quantity.
     * @param quantity New value of property quantity.
     */
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
    public  void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    //Modified for Case # 3132 - end
    
    /** Getter for property categoryType.
     * @return Value of property categoryType.
     */
    public char getCategoryType() {
        return categoryType;
    }
    
    /** Setter for property categoryType.
     * @param categoryType New value of property categoryType.
     */
    public void setCategoryType(char categoryType) {
        this.categoryType = categoryType;
    }      
    
    /** Getter for property directCost.
     * @return Value of property directCost.
     */
    public double getDirectCost() {
        return directCost;
    }
    
    /** Setter for property directCost.
     * @param directCost New value of property directCost.
     */
    public void setDirectCost(double directCost) {
        this.directCost = directCost;
    }
    
    /** Getter for property indirectCost.
     * @return Value of property indirectCost.
     */
    public double getIndirectCost() {
        return indirectCost;
    }
    
    /** Setter for property indirectCost.
     * @param indirectCost New value of property indirectCost.
     */
    public void setIndirectCost(double indirectCost) {
        this.indirectCost = indirectCost;
    }
    
    /** Getter for property totalCostSharing.
     * @return Value of property totalCostSharing.
     */
    public double getTotalCostSharing() {
        return totalCostSharing;
    }
    
    /** Setter for property totalCostSharing.
     * @param totalCostSharing New value of property totalCostSharing.
     */
    public void setTotalCostSharing(double totalCostSharing) {
        this.totalCostSharing = totalCostSharing;
    }
    
    /**
     * This method is used to return the Primary key object of the bean class.
     * @return Object Primary key Object
     */
    public Object getPrimaryKey() {
        return new Integer(getLineItemNumber());
    }
    
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - Start
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }
    //COEUSQA-1535-Access to institutionally maintained salaries in proposal budget - End
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetDetailBean){
            BudgetDetailBean budgetDetailBean = (BudgetDetailBean)comparableBean;
            //Proposal Number
            if(budgetDetailBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetDetailBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetDetailBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetDetailBean.getVersionNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Period Number
            if(budgetDetailBean.getBudgetPeriod()!=-1){
                if(getBudgetPeriod() == budgetDetailBean.getBudgetPeriod()){
                    success = true;
                }else{
                    return false;
                }
            }
            //Line Item Number
            if(budgetDetailBean.getLineItemNumber()!=-1){
                if(getLineItemNumber() == budgetDetailBean.getLineItemNumber()){
                    success = true;
                }else{
                    return false;
                }
            }
        }else{
            throw new CoeusException("budget_exception.1000");
        }
        return success;
    }
    
    // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        BudgetDetailBean budgetDetailBean = (BudgetDetailBean)obj;
        if(budgetDetailBean.getProposalNumber().equals(getProposalNumber()) &&
        budgetDetailBean.getVersionNumber() == getVersionNumber() &&
        budgetDetailBean.getBudgetPeriod()== getBudgetPeriod() &&
        budgetDetailBean.getLineItemNumber()== getLineItemNumber()){
            return true;
        }else {
            return false;
        }
    }
    // Added by Chandra 04/10/2003 - End
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Proposal Number =>"+this.getProposalNumber());
        strBffr.append(";");
        strBffr.append("Version Name =>"+this.getVersionNumber());
        strBffr.append(";");
        strBffr.append("Budget Period =>"+budgetPeriod);
        strBffr.append(";");
        strBffr.append("Line Item Number =>"+lineItemNumber);
        strBffr.append(";");
        strBffr.append("Budget Category Code =>"+budgetCategoryCode);
        strBffr.append(";");
        strBffr.append("Cost Element =>"+costElement);
        strBffr.append(";");
        strBffr.append("Cost Element Description =>"+costElementDescription);
        strBffr.append(";");
        strBffr.append("Line Item Description =>"+lineItemDescription);
        strBffr.append(";");             
        strBffr.append("Based on Line Item =>"+basedOnLineItem);
        strBffr.append(";");                     
        strBffr.append("Line Item sequence =>"+lineItemSequence);
        strBffr.append(";");                     
        strBffr.append("Line Item Start Date =>"+lineItemStartDate);
        strBffr.append(";");                     
        strBffr.append("Line Item End Date =>"+lineItemEndDate);
        strBffr.append(";");                     
        strBffr.append("Line Item Cost =>"+lineItemCost);
        strBffr.append(";");                             
        strBffr.append("Cost Sharing Amount =>"+costSharingAmount);
        strBffr.append(";");        
        strBffr.append("Under Recovery Amount =>"+underRecoveryAmount);
        strBffr.append(";");        
        strBffr.append("Direct Cost =>"+directCost);
        strBffr.append(";");        
        strBffr.append("Indirect Cost =>"+indirectCost);
        strBffr.append(";");        
        strBffr.append("Total Cost Sharing =>"+totalCostSharing);
        strBffr.append(";");
        strBffr.append("On Off Campus Flag =>"+onOffCampusFlag);
        strBffr.append(";");
        strBffr.append("Apply In Rate Flag =>"+applyInRateFlag);
        strBffr.append(";");        
        strBffr.append("Budget Justification =>"+budgetJustification);
        strBffr.append(";");        
        strBffr.append("Category Type =>"+categoryType);
        strBffr.append(";");        
        strBffr.append("Quantity =>"+quantity);
        strBffr.append(";");                
        strBffr.append("Update User =>"+this.getUpdateUser());
        strBffr.append(";");
        strBffr.append("Update Time Stamp =>"+this.getUpdateTimestamp());
        strBffr.append(";");
        strBffr.append("AcType =>"+this.getAcType());  
        strBffr.append(";");
        strBffr.append("submit Cost Sharing Flag =>"+submitCostSharingFlag);
        strBffr.append(";");
        strBffr.append("Unit Number =>"+unitNumber);
        strBffr.append(";");
        return strBffr.toString();
    }

    public boolean isSubmitCostSharingFlag() {
        return submitCostSharingFlag;
    }

    public void setSubmitCostSharingFlag(boolean submitCostSharingFlag) {
        this.submitCostSharingFlag = submitCostSharingFlag;
    }

    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - Start
    /**
     * Method to get the sub award number
     * @return subAwardNumber
     */
    public int getSubAwardNumber() {
        return subAwardNumber;
    }

    /**
     * Method to set the sub award number
     * @param subAwardNumber 
     */
    public void setSubAwardNumber(int subAwardNumber) {
        this.subAwardNumber = subAwardNumber;
    }
    // Added for COEUSQA-2115 Subaward budgeting for Proposal Development - End
    
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
    /**
     * Method to get isFormualtedLineItem
     * @return isFormualtedLineItem
     */
    public boolean isFormualtedLineItem() {
        return isFormualtedLineItem;
    }
    
    /**
     * Method to set budget detail is a formulated detail
     * @param isFormualtedLineItem 
     */
    public void setIsFormualtedLineItem(boolean isFormualtedLineItem) {
        this.isFormualtedLineItem = isFormualtedLineItem;
    }
    // Added for COEUSQA-1725 Extend the functionality of Lab Allocation in proposal development budgeting. - Start
}