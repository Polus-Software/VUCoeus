/*
 * InvCreditTypeBean.java
 *
 * Created on February 21, 2006, 12:33 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.CoeusBean;
import java.sql.Timestamp;

/**
 *
 * @author  ajaygm
 */
public class InvCreditTypeBean implements java.io.Serializable , CoeusBean{
    private int invCreditTypeCode;
    private String description;
    private boolean addsToHundred;
    private String updateUser;
    private Timestamp updateTimestamp;
    private String acType;
    
    /** Creates a new instance of InvCreditTypeBean */
    public InvCreditTypeBean() {
    }
    
    /**
     * Getter for property invCreditTypeCode.
     * @return Value of property invCreditTypeCode.
     */
    public int getInvCreditTypeCode() {
        return invCreditTypeCode;
    }
    
    /**
     * Setter for property invCreditTypeCode.
     * @param invCreditTypeCode New value of property invCreditTypeCode.
     */
    public void setInvCreditTypeCode(int invCreditTypeCode) {
        this.invCreditTypeCode = invCreditTypeCode;
    }
    
    /**
     * Getter for property description.
     * @return Value of property description.
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    
    /**
     * Getter for property addsToHundred.
     * @return Value of property addsToHundred.
     */
    public boolean isAddsToHundred() {
        return addsToHundred;
    }
    
    /**
     * Setter for property addsToHundred.
     * @param addsToHundred New value of property addsToHundred.
     */
    public void setAddsToHundred(boolean addsToHundred) {
        this.addsToHundred = addsToHundred;
    }
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
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
     * Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }
    
}
