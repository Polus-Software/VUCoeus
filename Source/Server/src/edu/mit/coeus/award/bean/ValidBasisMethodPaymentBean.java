/**
 * @(#)ValidBasisMethodPaymentBean.java 1.0 08/03/04
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

public class ValidBasisMethodPaymentBean extends ComboBoxBean implements CoeusBean, java.io.Serializable{
    
    private int basisOfPaymentCode;
    //private int methodOfPaymentCode;
    //private String methodOfPaymentDescription;
    private String frequencyIndicator;
    private String invInstructionsIndicator;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    /**
     *	Default Constructor
     */
    
    public ValidBasisMethodPaymentBean(){
    }      
    
    public boolean isLike(ComparableBean comparableBean){
        return true;
    }
    
    /** Getter for property basisOfPaymentCode.
     * @return Value of property basisOfPaymentCode.
     */
    public int getBasisOfPaymentCode() {
        return basisOfPaymentCode;
    }
    
    /** Setter for property basisOfPaymentCode.
     * @param basisOfPaymentCode New value of property basisOfPaymentCode.
     */
    public void setBasisOfPaymentCode(int basisOfPaymentCode) {
        this.basisOfPaymentCode = basisOfPaymentCode;
    }    
    
    /** Getter for property frequencyIndicator.
     * @return Value of property frequencyIndicator.
     */
    public java.lang.String getFrequencyIndicator() {
        return frequencyIndicator;
    }
    
    /** Setter for property frequencyIndicator.
     * @param frequencyIndicator New value of property frequencyIndicator.
     */
    public void setFrequencyIndicator(java.lang.String frequencyIndicator) {
        this.frequencyIndicator = frequencyIndicator;
    }
    
    /** Getter for property invInstructionsIndicator.
     * @return Value of property invInstructionsIndicator.
     */
    public java.lang.String getInvInstructionsIndicator() {
        return invInstructionsIndicator;
    }
    
    /** Setter for property invInstructionsIndicator.
     * @param invInstructionsIndicator New value of property invInstructionsIndicator.
     */
    public void setInvInstructionsIndicator(java.lang.String invInstructionsIndicator) {
        this.invInstructionsIndicator = invInstructionsIndicator;
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
}