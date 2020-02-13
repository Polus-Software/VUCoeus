/*
 * @(#)AwardAmountTransactionBean.java 1.0 07/07/08
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.award.bean;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author leenababu
 */
public class AwardAmountTransactionBean implements java.io.Serializable {
    private String mitAwardNumber;
    private String transactionId;
    private int transactionTypeCode;
    private String transactionTypeDescription;
    private Date noticeDate;
    private String comments;
    private Timestamp updateTimestamp;
    private String updateUser;
    private Timestamp awUpdateTimestamp;
    private String acType;
            
    /** Creates a new instance of AwardAmountTransactionBean */
    public AwardAmountTransactionBean() {
    }

    public String getMitAwardNumber() {
        return mitAwardNumber;
    }

    public void setMitAwardNumber(String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getTransactionTypeCode() {
        return transactionTypeCode;
    }

    public void setTransactionTypeCode(int transactionTypeCode) {
        this.transactionTypeCode = transactionTypeCode;
    }

    public Timestamp getAwUpdateTimestamp() {
        return awUpdateTimestamp;
    }

    public void setAwUpdateTimestamp(Timestamp awUpdateTimestamp) {
        this.awUpdateTimestamp = awUpdateTimestamp;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public Date getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(Date noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getTransactionTypeDescription() {
        return transactionTypeDescription;
    }

    public void setTransactionTypeDescription(String transactionTypeDescription) {
        this.transactionTypeDescription = transactionTypeDescription;
    }
    
}
