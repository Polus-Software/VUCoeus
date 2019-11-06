/*
 * SPApprovalInfoBean.java
 *
 * Created on April 13, 2003, 5:33 PM
 */

package edu.mit.coeus.bean;

import java.beans.*;
import edu.mit.coeus.bean.BaseBean;
/**
 *
 * @author  mukund
 */
public class SRApprovalInfoBean implements java.io.Serializable, BaseBean {
    
    private int specialReviewCode;
    private int approvalTypeCode;
    private String protocolFlag;
    private String approvalDateFlag;
    private String applicationDateFlag;
    //COEUSQA-1724-Added For new expiration date column-start
    private String expirationDateFlag;
    //COEUSQA-1724-Added For new expiration date column-end
    private String acType;
    
    public SRApprovalInfoBean(){}
    
    /** Getter for property protocolFlag.
     * @return Value of property protocolFlag.
     */
    public java.lang.String getProtocolFlag() {
        return protocolFlag;
    }    
    
    /** Setter for property protocolFlag.
     * @param protocolFlag New value of property protocolFlag.
     */
    public void setProtocolFlag(java.lang.String protocolFlag) {
        this.protocolFlag = protocolFlag;
    }
    
    /** Getter for property approvalDateFlag.
     * @return Value of property approvalDateFlag.
     */
    public java.lang.String getApprovalDateFlag() {
        return approvalDateFlag;
    }
    
    /** Setter for property approvalDateFlag.
     * @param approvalDateFlag New value of property approvalDateFlag.
     */
    public void setApprovalDateFlag(java.lang.String approvalDateFlag) {
        this.approvalDateFlag = approvalDateFlag;
    }
    
    /** Getter for property applicationDateFlag.
     * @return Value of property applicationDateFlag.
     */
    public java.lang.String getApplicationDateFlag() {
        return applicationDateFlag;
    }
    
    /** Setter for property applicationDateFlag.
     * @param applicationDateFlag New value of property applicationDateFlag.
     */
    public void setApplicationDateFlag(java.lang.String applicationDateFlag) {
        this.applicationDateFlag = applicationDateFlag;
    }
    
    //COEUSQA-1724-Added For new expiration date column-start
    /** Getter for property expirationDateFlag.
     * @return Value of property expirationDateFlag.
     */
    public java.lang.String getExpirationDateFlag() {
        return expirationDateFlag;
    }
    
    /** Setter for property expirationDateFlag.
     * @param applicationDateFlag New value of property expirationDateFlag.
     */
    public void setExpirationDateFlag(java.lang.String expirationDateFlag) {
        this.expirationDateFlag = expirationDateFlag;
    }
    //COEUSQA-1724-Added For new expiration date column-end
    
    /** Getter for property specialReviewCode.
     * @return Value of property specialReviewCode.
     */
    public int getSpecialReviewCode() {
        return specialReviewCode;
    }
    
    /** Setter for property specialReviewCode.
     * @param specialReviewCode New value of property specialReviewCode.
     */
    public void setSpecialReviewCode(int specialReviewCode) {
        this.specialReviewCode = specialReviewCode;
    }
    
    /** Getter for property approvalTypeCode.
     * @return Value of property approvalTypeCode.
     */
    public int getApprovalTypeCode() {
        return approvalTypeCode;
    }
    
    /** Setter for property approvalTypeCode.
     * @param approvalTypeCode New value of property approvalTypeCode.
     */
    public void setApprovalTypeCode(int approvalTypeCode) {
        this.approvalTypeCode = approvalTypeCode;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     *
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     *
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
}
