/*
 * DBOpportunityInfoBean.java
 *
 * Created on February 11, 2005, 10:22 AM
 */

package edu.mit.coeus.s2s.bean;

import java.sql.Timestamp;

/**
 *
 * @author  geot
 */
public class DBOpportunityInfoBean extends OpportunityInfoBean{
    private Timestamp updateTimestamp;
    private String updateUser;
    private Timestamp awUpdateTimestamp;
    private char acType;
    private String proposalNumber;
    private String awProposalNumber;
    /** Creates a new instance of DBOpportunityInfoBean */
    public DBOpportunityInfoBean() {
    }
    
    /**
     * Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /**
     * Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /**
     * Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /**
     * Getter for property awUpdateTimestamp.
     * @return Value of property awUpdateTimestamp.
     */
    public java.sql.Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }
    
    /**
     * Setter for property awUpdateTimestamp.
     * @param awUpdateTimestamp New value of property awUpdateTimestamp.
     */
    public void setAwUpdateTimestamp(java.sql.Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public char getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(char acType) {
        this.acType = acType;
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
     * Getter for property awProposalNumber.
     * @return Value of property awProposalNumber.
     */
    public java.lang.String getAwProposalNumber() {
        return awProposalNumber;
    }
    
    /**
     * Setter for property awProposalNumber.
     * @param awProposalNumber New value of property awProposalNumber.
     */
    public void setAwProposalNumber(java.lang.String awProposalNumber) {
        this.awProposalNumber = awProposalNumber;
    }
    
}
