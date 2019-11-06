
package edu.mit.coeus.budget.bean;

import java.util.*;
import java.sql.Date;
import edu.mit.coeus.bean.PrimaryKey;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
/**
 *
 */
public class BudgetInfoBean extends BudgetBean implements PrimaryKey{
    
    //holds start date
    private Date startDate = null;
    
   //holds end date
    private Date endDate = null;
    
    //holds total cost
    private double totalCost;
    
    //holds total direct cost
    private double totalDirectCost;    

    //holds total indirect cost
    private double totalIndirectCost;
    
    //holds cost sharing amount
    private double costSharingAmount;
    
    //holds under recovery amount
    private double underRecoveryAmount;
    
    //holds residual funds
    private double residualFunds;
    
    //holds total cost limit
    private double totalCostLimit;
    
    //holds OH Rate Class Code
    private int ohRateClassCode = -1;
    
    //holds OH Rate Class Description
    private String ohRateClassDescription = null;
    
    
    //holds OH Rate Type Code
    private int ohRateTypeCode = -1;
    
    //holds comments
    private String comments = null;
    
    //holds final version flag
    private boolean finalVersionFlag;
    
    //holds activity Type Code
    private int activityTypeCode;
    
    //holds unit Number
    private String unitNumber;
    
    //holds whether costSharingAmount is present
    private boolean hasCostSharingDistribution;
    
    //holds total costsharing amount
    private double totalCostSharingDistribution;
    
    //holds whether idc Rate is present
    private boolean hasIDCRateDistribution;
    
    //holds total costsharing amount
    private double totalIDCRateDistribution;
    
    //holds UR Rate Class Code
    private int urRateClassCode = -1;
    
    // holds modular budget details-Case Id 1626-start
    private boolean budgetModularFlag;
    
    // holds On/Off campus flag - Case Id 2924
    private boolean onOffCampusFlag;
    
    //Added for case#3654 - Third option 'Default' in the campus dropdown
    private boolean defaultIndicator;
    
    //holds UR Rate Type Code
    //Commented following as this is not required - 15th March 2004
    //private int urRateTypeCode = -1;
    //Code added for Case#3472 - Sync to Direct Cost Limit
    //For adding total direct cost limit
    private double totalDirectCostLimit;
    
    //COEUSQA-1693 - Cost Sharing Submission
    private boolean submitCostSharingFlag;

    public BudgetInfoBean(){
    }
    
    /** Getter for property startDate.
     * @return Value of property startDate.
     */
    public java.sql.Date getStartDate() {
        return startDate;
    }
    
    /** Setter for property startDate.
     * @param startDate New value of property startDate.
     */
    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }
    
    /** Setter for property endDate.
     * @param endDate New value of property endDate.
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }
    
    /** Getter for property totalCost.
     * @return Value of property totalCost.
     */
    public double getTotalCost() {
        return totalCost;
    }
    
    /** Setter for property totalCost.
     * @param totalCost New value of property totalCost.
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    /** Getter for property totalDirectCost.
     * @return Value of property totalDirectCost.
     */
    public double getTotalDirectCost() {
        return totalDirectCost;
    }
    
    /** Setter for property totalDirectCost.
     * @param totalDirectCost New value of property totalDirectCost.
     */
    public void setTotalDirectCost(double totalDirectCost) {
        this.totalDirectCost = totalDirectCost;
    }
    
    /** Getter for property totalIndirectCost.
     * @return Value of property totalIndirectCost.
     */
    public double getTotalIndirectCost() {
        return totalIndirectCost;
    }
    
    /** Setter for property totalIndirectCost.
     * @param totalIndirectCost New value of property totalIndirectCost.
     */
    public void setTotalIndirectCost(double totalIndirectCost) {
        this.totalIndirectCost = totalIndirectCost;
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
    
    /** Getter for property residualFunds.
     * @return Value of property residualFunds.
     */
    public double getResidualFunds() {
        return residualFunds;
    }
    
    /** Setter for property residualFunds.
     * @param residualFunds New value of property residualFunds.
     */
    public void setResidualFunds(double residualFunds) {
        this.residualFunds = residualFunds;
    }
    
    /** Getter for property totalCostLimit.
     * @return Value of property totalCostLimit.
     */
    public double getTotalCostLimit() {
        return totalCostLimit;
    }
    
    /** Setter for property totalCostLimit.
     * @param totalCostLimit New value of property totalCostLimit.
     */
    public void setTotalCostLimit(double totalCostLimit) {
        this.totalCostLimit = totalCostLimit;
    }
    
    /** Getter for property ohRateClassCode.
     * @return Value of property ohRateClassCode.
     */
    public int getOhRateClassCode() {
        return ohRateClassCode;
    }
    
    /** Setter for property ohRateClassCode.
     * @param ohRateClassCode New value of property ohRateClassCode.
     */
    public void setOhRateClassCode(int ohRateClassCode) {
        this.ohRateClassCode = ohRateClassCode;
    }
    
    /** Getter for property ohRateTypeCode.
     * @return Value of property ohRateTypeCode.
     */
    public int getOhRateTypeCode() {
        return ohRateTypeCode;
    }
    
    /** Setter for property ohRateTypeCode.
     * @param ohRateTypeCode New value of property ohRateTypeCode.
     */
    public void setOhRateTypeCode(int ohRateTypeCode) {
        this.ohRateTypeCode = ohRateTypeCode;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
    /** Getter for property finalVersionFlag.
     * @return Value of property finalVersionFlag.
     */
    public boolean isFinalVersion() {
        return finalVersionFlag;
    }    
    
    /** Setter for property finalVersionFlag.
     * @param finalVersionFlag New value of property finalVersionFlag.
     */
    public void setFinalVersion(boolean finalVersionFlag) {
        this.finalVersionFlag = finalVersionFlag;
    }
    //Added for COEUSDEV-124 - CoeusLite - Budget summary On/off campus flag gets reset
    /** Setter for property finalVersionFlag.
     * @param finalVersionFlag New value of property finalVersionFlag.
     */
    public void setFinalVersionFlag(boolean finalVersionFlag) {
        this.finalVersionFlag = finalVersionFlag;
    }
    //COEUSDEV-124 End
    /**
     * This method is used to return the Primary key object of the bean class.
     * @return Object Primary key Object
     */
    public Object getPrimaryKey() {
        return new Integer(getVersionNumber());
    }
    
    public boolean isLike(ComparableBean comparableBean) throws CoeusException{
        boolean success = false;
        if(comparableBean instanceof BudgetInfoBean){
            BudgetInfoBean budgetInfoBean = (BudgetInfoBean)comparableBean;
            //Proposal Number
            if(budgetInfoBean.getProposalNumber()!=null){
                if(getProposalNumber().equals(budgetInfoBean.getProposalNumber())){
                    success = true;
                }else{
                    return false;
                }
            }
            //Version Number
            if(budgetInfoBean.getVersionNumber()!=-1){
                if(getVersionNumber() == budgetInfoBean.getVersionNumber()){
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
    
    /** Getter for property ohRateClassDescription.
     * @return Value of property ohRateClassDescription.
     */
    public String getOhRateClassDescription() {
        return ohRateClassDescription;
    }
    
    /** Setter for property ohRateClassDescription.
     * @param ohRateClassDescription New value of property ohRateClassDescription.
     */
    public void setOhRateClassDescription(String ohRateClassDescription) {
        this.ohRateClassDescription = ohRateClassDescription;
    }
    
    /** Getter for property activityTypeCode.
     * @return Value of property activityTypeCode.
     */
    public int getActivityTypeCode() {
        return activityTypeCode;
    }
    
    /** Setter for property activityTypeCode.
     * @param activityTypeCode New value of property activityTypeCode.
     */
    public void setActivityTypeCode(int activityTypeCode) {
        this.activityTypeCode = activityTypeCode;
    }
    
    /** Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /** Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
    /** Getter for property hasCostSharingDistribution.
     * @return Value of property hasCostSharingDistribution.
     */
    public boolean getHasCostSharingDistribution() {
        return hasCostSharingDistribution;
    }
    
    /** Setter for property hasCostSharingDistribution.
     * @param hasCostSharingDistribution New value of property hasCostSharingDistribution.
     */
    public void setHasCostSharingDistribution(boolean hasCostSharingDistribution) {
        this.hasCostSharingDistribution = hasCostSharingDistribution;
    }
    
    /** Getter for property totalCostSharingDistribution.
     * @return Value of property totalCostSharingDistribution.
     */
    public double getTotalCostSharingDistribution() {
        return totalCostSharingDistribution;
    }
    
    /** Setter for property totalCostSharingDistribution.
     * @param totalCostSharingDistribution New value of property totalCostSharingDistribution.
     */
    public void setTotalCostSharingDistribution(double totalCostSharingDistribution) {
        this.totalCostSharingDistribution = totalCostSharingDistribution;
    }
    
    /** Getter for property totalIDCRateDistribution.
     * @return Value of property totalIDCRateDistribution.
     */
    public double getTotalIDCRateDistribution() {
        return totalIDCRateDistribution;
    }
    
    /** Setter for property totalIDCRateDistribution.
     * @param totalIDCRateDistribution New value of property totalIDCRateDistribution.
     */
    public void setTotalIDCRateDistribution(double totalIDCRateDistribution) {
        this.totalIDCRateDistribution = totalIDCRateDistribution;
    }
    
    /** Getter for property hasIDCRateDistribution.
     * @return Value of property hasIDCRateDistribution.
     */
    public boolean isHasIDCRateDistribution() {
        return hasIDCRateDistribution;
    }
    
    /** Setter for property hasIDCRateDistribution.
     * @param hasIDCRateDistribution New value of property hasIDCRateDistribution.
     */
    public void setHasIDCRateDistribution(boolean hasIDCRateDistribution) {
        this.hasIDCRateDistribution = hasIDCRateDistribution;
    }
    
    // Added by Chandra 04/10/2003 - start
    public boolean equals(Object obj) {
        BudgetInfoBean budgetInfoBean = (BudgetInfoBean)obj;
        if(budgetInfoBean.getProposalNumber().equals(getProposalNumber()) &&
            budgetInfoBean.getVersionNumber() == getVersionNumber()){
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
        strBffr.append("Start Date =>"+startDate);
        strBffr.append(";");
        strBffr.append("End Date =>"+endDate);
        strBffr.append(";");
        strBffr.append("Total Cost =>"+totalCost);
        strBffr.append(";");
        strBffr.append("Total Direct Cost =>"+totalDirectCost);
        strBffr.append(";");
        strBffr.append("Total Indirect Cost =>"+totalIndirectCost);
        strBffr.append(";");
        strBffr.append("Total Cost Sharing Amount =>"+costSharingAmount);
        strBffr.append(";");
        strBffr.append("Under Recovery Amount =>"+underRecoveryAmount);
        strBffr.append(";");
        strBffr.append("Residual Funds =>"+residualFunds);
        strBffr.append(";");
        strBffr.append("Total Cost Limit =>"+totalCostLimit);
        strBffr.append(";");
        strBffr.append("OH Rate Class Code =>"+ohRateClassCode);
        strBffr.append(";");
        strBffr.append("OH Rate Class Description =>"+ohRateClassDescription);
        strBffr.append(";");
        strBffr.append("OH Rate TypeCode =>"+ohRateTypeCode);
        strBffr.append(";");
        strBffr.append("Comments =>"+comments);        
        strBffr.append(";");
        strBffr.append("Final Version Flag =>"+finalVersionFlag);        
        strBffr.append(";");
        strBffr.append("Activity Type Code =>"+activityTypeCode);        
        strBffr.append(";");
        strBffr.append("Unit Number =>"+unitNumber);
        strBffr.append(";");                
        strBffr.append("Has Cost Sharing =>"+hasCostSharingDistribution);        
        strBffr.append(";");                
        strBffr.append("Total Cost Sharing Distribution =>"+totalCostSharingDistribution);        
        strBffr.append(";");
        strBffr.append("Has IDC Rate Distribution =>"+hasIDCRateDistribution);
        strBffr.append(";");
        strBffr.append("Total IDC Rate Distribution =>"+totalIDCRateDistribution);
        strBffr.append(";");
        
        strBffr.append("UR Rate Class Code =>"+urRateClassCode);
        strBffr.append(";");
        strBffr.append("Budget Modular Flag =>"+budgetModularFlag);
        strBffr.append(";");
        strBffr.append("On Off Campus Flag =>"+onOffCampusFlag);
        strBffr.append(";");
        strBffr.append("Default Indicator =>"+defaultIndicator);
        strBffr.append(";");
        strBffr.append("submit Cost Sharing Flag =>"+submitCostSharingFlag);
        strBffr.append(";");
        
        return strBffr.toString();
    }
    
    /** Getter for property urRateClassCode.
     * @return Value of property urRateClassCode.
     */
    public int getUrRateClassCode() {
        return urRateClassCode;
    }
    
    /** Setter for property urRateClassCode.
     * @param urRateClassCode New value of property urRateClassCode.
     */
    public void setUrRateClassCode(int urRateClassCode) {
        this.urRateClassCode = urRateClassCode;
    }
    
    /**
     * Getter for property budgetModularFlag.
     * @return Value of property budgetModularFlag.
     */
    public boolean isBudgetModularFlag() {
        return budgetModularFlag;
    }
    
    /**
     * Setter for property budgetModularFlag.
     * @param budgetModularFlag New value of property budgetModularFlag.
     */
    public void setBudgetModularFlag(boolean budgetModularFlag) {
        this.budgetModularFlag = budgetModularFlag;
    }
    //Added with COEUSDEV-298:Do not copy modular flag and modular budget while only copying period 1
    //This is added to match with the name given in budgetSummary form
    /**
     * Setter for property budgetModularFlag.
     * @param budgetModularFlag New value of property budgetModularFlag.
     */
    public void setModularBudgetFlag(boolean modularBudgetFlag) {
        this.budgetModularFlag = modularBudgetFlag;
    }
    //COEUSDEV-298:End
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
    
    //Commented following as this is not required - 15th March 2004
    /** Getter for property urRateTypeCode.
     * @return Value of property urRateTypeCode.
     */
    /*public int getUrRateTypeCode() {
        return urRateTypeCode;
    }*/
    
    /** Setter for property urRateTypeCode.
     * @param urRateTypeCode New value of property urRateTypeCode.
     */
    /*public void setUrRateTypeCode(int urRateTypeCode) {
        this.urRateTypeCode = urRateTypeCode;
    }*/

    //Added for case#3654 - Third option 'Default' in the campus dropdown - start
    public boolean isDefaultIndicator() {
        return defaultIndicator;
    }

    public void setDefaultIndicator(boolean defaultIndicator) {
        this.defaultIndicator = defaultIndicator;
    }
    //Added for case#3654 - Third option 'Default' in the campus dropdown - end

    public double getTotalDirectCostLimit() {
        return totalDirectCostLimit;
    }

    public void setTotalDirectCostLimit(double totalDirectCostLimit) {
        this.totalDirectCostLimit = totalDirectCostLimit;
    }

    public boolean isSubmitCostSharingFlag() {
        return submitCostSharingFlag;
    }

    public void setSubmitCostSharingFlag(boolean submitCostSharingFlag) {
        this.submitCostSharingFlag = submitCostSharingFlag;
    }
    
} // end BudgetInfoBean



