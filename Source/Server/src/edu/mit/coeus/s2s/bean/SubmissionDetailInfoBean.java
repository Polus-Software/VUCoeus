/*
 * SubmissionDetailInfoBean.java
 *
 * Created on January 12, 2005, 3:11 PM
 */

package edu.mit.coeus.s2s.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author  geot
 */
public class SubmissionDetailInfoBean extends ApplicationInfoBean implements Serializable{
    private Timestamp updateTimestamp;
    private String updateUser;
    private Timestamp awUpdateTimestamp;
    private String awUpdateUser;
    private char acType;
    private String awProposalNumber;
    private int awSubimissionNumber;
    private String comments;
    

    /** Creates a new instance of SubmissionDetailInfoBean */
    public SubmissionDetailInfoBean() {
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
     * Getter for property awUpdateUser.
     * @return Value of property awUpdateUser.
     */
    public java.lang.String getAwUpdateUser() {
        return awUpdateUser;
    }
    
    /**
     * Setter for property awUpdateUser.
     * @param awUpdateUser New value of property awUpdateUser.
     */
    public void setAwUpdateUser(java.lang.String awUpdateUser) {
        this.awUpdateUser = awUpdateUser;
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
    
    /**
     * Getter for property awSubimissionNumber.
     * @return Value of property awSubimissionNumber.
     */
    public int getAwSubimissionNumber() {
        return awSubimissionNumber;
    }
    
    /**
     * Setter for property awSubimissionNumber.
     * @param awSubimissionNumber New value of property awSubimissionNumber.
     */
    public void setAwSubimissionNumber(int awSubimissionNumber) {
        this.awSubimissionNumber = awSubimissionNumber;
    }
    
    /**
     * Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /**
     * Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }
    
}
