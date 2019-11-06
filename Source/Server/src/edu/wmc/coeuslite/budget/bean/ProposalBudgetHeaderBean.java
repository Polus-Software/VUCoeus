/*
 * ProposalBudgetHeaderBean.java
 *
 * Created on March 8, 2006, 8:36 PM
 */

package edu.wmc.coeuslite.budget.bean;

/**
 *
 * @author  chandrashekara
 */
public class ProposalBudgetHeaderBean implements java.io.Serializable{
    
    private String proposalNumber;
    private int versionNumber;
    private java.sql.Date proposalStartDate;
    private java.sql.Date proposalEndDate;
    private String personId;
    private String personName;
    //change made here
    private String title;
    private String sponsorName;
    private String proposalStatus;

    /** Creates a new instance of ProposalBudgetHeaderBean */
    public ProposalBudgetHeaderBean() {
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
     * Getter for property versionNumber.
     * @return Value of property versionNumber.
     */
    public int getVersionNumber() {
        return versionNumber;
    }
    
    /**
     * Setter for property versionNumber.
     * @param versionNumber New value of property versionNumber.
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    /**
     * Getter for property proposalStartDate.
     * @return Value of property proposalStartDate.
     */
    public java.sql.Date getProposalStartDate() {
        return proposalStartDate;
    }
    
    /**
     * Setter for property proposalStartDate.
     * @param proposalStartDate New value of property proposalStartDate.
     */
    public void setProposalStartDate(java.sql.Date proposalStartDate) {
        this.proposalStartDate = proposalStartDate;
    }
    
    /**
     * Getter for property proposalEndDate.
     * @return Value of property proposalEndDate.
     */
    public java.sql.Date getProposalEndDate() {
        return proposalEndDate;
    }
    
    /**
     * Setter for property proposalEndDate.
     * @param proposalEndDate New value of property proposalEndDate.
     */
    public void setProposalEndDate(java.sql.Date proposalEndDate) {
        this.proposalEndDate = proposalEndDate;
    }
    
    /**
     * Getter for property personId.
     * @return Value of property personId.
     */
    public java.lang.String getPersonId() {
        return personId;
    }
    
    /**
     * Setter for property personId.
     * @param personId New value of property personId.
     */
    public void setPersonId(java.lang.String personId) {
        this.personId = personId;
    }
    
    /**
     * Getter for property personName.
     * @return Value of property personName.
     */
    public java.lang.String getPersonName() {
        return personName;
    }
    
    /**
     * Setter for property personName.
     * @param personName New value of property personName.
     */
    public void setPersonName(java.lang.String personName) {
        this.personName = personName;
    }
    /**
     * Getter for property sponsorName.
     * @return Value of property sponsorName.
     */
    public java.lang.String getSponsorName() {
        return sponsorName;
    }
    
    /**
     * Setter for property sponsorName.
     * @param sponsorName New value of property sponsorName.
     */
    public void setSponsorName(java.lang.String sponsorName) {
        this.sponsorName = sponsorName;
    }
    
    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public java.lang.String getTitle() {
        return title;
    }
    
    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }
    
    /**
     * Getter for property proposalStatus.
     * @return Value of property proposalStatus.
     */
    public java.lang.String getProposalStatus() {
        return proposalStatus;
    }
    
    /**
     * Setter for property proposalStatus.
     * @param proposalStatus New value of property proposalStatus.
     */
    public void setProposalStatus(java.lang.String proposalStatus) {
        this.proposalStatus = proposalStatus;
    }
}
