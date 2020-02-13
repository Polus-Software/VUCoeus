/**
 * @(#)ValidBasisPaymentBean.java 1.0 08/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * This class is used to hold Sap Feed Details data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 08, 2004 12:30 PM
 */

public class ValidBasisPaymentBean extends ComboBoxBean implements CoeusBean, java.io.Serializable{
    
    private int awardTypeCode;
    private String awardTypeDescription;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    /**
     *	Default Constructor
     */
    
    public ValidBasisPaymentBean(){
    }          
    
    /** Getter for property awardTypeCode.
     * @return Value of property awardTypeCode.
     */
    public int getAwardTypeCode() {
        return awardTypeCode;
    }    

    /** Setter for property awardTypeCode.
     * @param awardTypeCode New value of property awardTypeCode.
     */
    public void setAwardTypeCode(int awardTypeCode) {
        this.awardTypeCode = awardTypeCode;
    }
    
    /** Getter for property awardTypeDescription.
     * @return Value of property awardTypeDescription.
     */
    public java.lang.String getAwardTypeDescription() {
        return awardTypeDescription;
    }
    
    /** Setter for property awardTypeDescription.
     * @param awardTypeDescription New value of property awardTypeDescription.
     */
    public void setAwardTypeDescription(java.lang.String awardTypeDescription) {
        this.awardTypeDescription = awardTypeDescription;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /** Getter for property acType.
     * @return Value of property acType.
     */
    public java.lang.String getAcType() {
        return acType;
    }
    
    /** Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(java.lang.String acType) {
        this.acType = acType;
    }    
    
    public boolean isLike(ComparableBean comparableBean){
        return true;
    }
}