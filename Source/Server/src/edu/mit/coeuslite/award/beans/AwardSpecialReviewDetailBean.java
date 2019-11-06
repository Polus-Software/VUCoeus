/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mit.coeuslite.award.beans;



/**
 *
 * @author indhulekha
 */
public class AwardSpecialReviewDetailBean {

    private String specialReview;
    private String approvalTypeCode;
    private String protocolNumber;
    private String approvalDate;

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getApprovalTypeCode() {
        return approvalTypeCode;
    }

    public void setApprovalTypeCode(String approvalTypeCode) {
        this.approvalTypeCode = approvalTypeCode;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getSpecialReview() {
        return specialReview;
    }

    public void setSpecialReview(String specialReview) {
        this.specialReview = specialReview;
    }



}
