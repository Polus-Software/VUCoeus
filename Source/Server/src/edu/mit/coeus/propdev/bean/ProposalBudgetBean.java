/*
 * ProposalBaseBean.java
 *
 * Created on August 11, 2005, 3:52 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;

/**
 *
 * @author  chandrashekara
 */
public class ProposalBudgetBean implements BaseBean,java.io.Serializable {
    
    private String proposalNumber;
    private CoeusVector budgetVersions;
    private boolean proposalSynced;
    private String childTypeDesc;
    private String unitNumber;
    
    /** Check for the status Complete Or Incomplete
     */
    private boolean budgetStatus;
    
    /** Creates a new instance of ProposalBaseBean */
    public ProposalBudgetBean() {
    }
    
    /**
     * Getter for property proposalNumber.
     * @return Value of property proposalNumber.
     */
    public java.lang.String getProposalNumber() {
        return proposalNumber;
    }
    
    /**
     * Setter for property proposalNumber.
     * @param proposalNumber New value of property proposalNumber.
     */
    public void setProposalNumber(java.lang.String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /**
     * Getter for property budgetVersions.
     * @return Value of property budgetVersions.
     */
    public edu.mit.coeus.utils.CoeusVector getBudgetVersions() {
        return budgetVersions;
    }
    
    /**
     * Setter for property budgetVersions.
     * @param budgetVersions New value of property budgetVersions.
     */
    public void setBudgetVersions(edu.mit.coeus.utils.CoeusVector budgetVersions) {
        this.budgetVersions = budgetVersions;
    }
    
    /**
     * Getter for property budgetStatus.
     * @return Value of property budgetStatus.
     */
    public boolean isBudgetStatus() {
        return budgetStatus;
    }
    
    /**
     * Setter for property budgetStatus.
     * @param budgetStatus New value of property budgetStatus.
     */
    public void setBudgetStatus(boolean budgetStatus) {
        this.budgetStatus = budgetStatus;
    }
    
    public String toString() {
        return proposalNumber;
    }
    
    /**
     * Getter for property proposalSynced.
     * @return Value of property proposalSynced.
     */
    public boolean isProposalSynced() {
        return proposalSynced;
    }
    
    /**
     * Setter for property proposalSynced.
     * @param proposalSynced New value of property proposalSynced.
     */
    public void setProposalSynced(boolean proposalSynced) {
        this.proposalSynced = proposalSynced;
    }
    
    /**
     * Getter for property childTypeDesc.
     * @return Value of property childTypeDesc.
     */
    public java.lang.String getChildTypeDesc() {
        return childTypeDesc;
    }
    
    /**
     * Setter for property childTypeDesc.
     * @param childTypeDesc New value of property childTypeDesc.
     */
    public void setChildTypeDesc(java.lang.String childTypeDesc) {
        this.childTypeDesc = childTypeDesc;
    }
    
    /**
     * Getter for property unitNumber.
     * @return Value of property unitNumber.
     */
    public java.lang.String getUnitNumber() {
        return unitNumber;
    }
    
    /**
     * Setter for property unitNumber.
     * @param unitNumber New value of property unitNumber.
     */
    public void setUnitNumber(java.lang.String unitNumber) {
        this.unitNumber = unitNumber;
    }
    
}
