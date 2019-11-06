/*
 * InvestigatorCreditSplitBean.java
 *
 * Created on February 20, 2006, 3:26 PM
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.utils.CoeusVector;
import java.sql.Timestamp;

/**
 *
 * @author  ajaygm
 */
public class InvestigatorCreditSplitBean implements java.io.Serializable , CoeusBean{
    
    // holds the proposal number
    private String moduleNumber;
    
    // holds the person Id
    private String personId;
    
    //holds non mit person Name
    private String personName;
    
    //holds credit type number 
    private int invCreditTypeCode;
    
    //holds principal investigator flag
    private boolean piFlag;
    
    //holds the credit
    private Double credit;
    
    //holds description
    private String description;
    
    //holds the unit no
    private String unitNumber;

    //holds the sequence no 
    private int sequenceNo;
    
    //holds the vector investigator bean
    //private CoeusVector investigatorUnits;  
    
    //holds credit type number 
    private boolean investigator;
    
    //holds update user id
    private String updateUser;
    
    //holds update timestamp
    private Timestamp updateTimestamp;
    
    //holds account type
    private String acType;
    
    //holds aw sequence Number
    private int awSequenceNo;
    
    
    /** Creates a new instance of InvestigatorCreditSplitBean */
    public InvestigatorCreditSplitBean() {
    }
    
    /**
     * Getter for property moduleNumber.
     * @return Value of property moduleNumber.
     */
    public java.lang.String getModuleNumber() {
        return moduleNumber;
    }
    
    /**
     * Setter for property moduleNumber.
     * @param moduleNumber New value of property moduleNumber.
     */
    public void setModuleNumber(java.lang.String moduleNumber) {
        this.moduleNumber = moduleNumber;
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
     * Getter for property piFlag.
     * @return Value of property piFlag.
     */
    public boolean isPiFlag() {
        return piFlag;
    }
    
    /**
     * Setter for property piFlag.
     * @param piFlag New value of property piFlag.
     */
    public void setPiFlag(boolean piFlag) {
        this.piFlag = piFlag;
    }
    
    /**
     * Getter for property credit.
     * @return Value of property credit.
     */
    public Double getCredit() {
        return credit;
    }
    
    /**
     * Setter for property credit.
     * @param credit New value of property credit.
     */
    public void setCredit(Double credit) {
        this.credit = credit;
    }
    
    /**
     * Getter for property investigatorUnits.
     * @return Value of property investigatorUnits.
     */
    /*public edu.mit.coeus.utils.CoeusVector getInvestigatorUnits() {
        return investigatorUnits;
    }*/
    
    /**
     * Setter for property investigatorUnits.
     * @param investigatorUnits New value of property investigatorUnits.
     */
    /*public void setInvestigatorUnits(edu.mit.coeus.utils.CoeusVector investigatorUnits) {
        this.investigatorUnits = investigatorUnits;
    }*/
    
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
    
    public boolean isLike(edu.mit.coeus.bean.ComparableBean comparableBean) throws edu.mit.coeus.exception.CoeusException {
        return true;
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
     * Getter for property investigator.
     * @return Value of property investigator.
     */
    public boolean isInvestigator() {
        return investigator;
    }
    
    /**
     * Setter for property investigator.
     * @param investigator New value of property investigator.
     */
    public void setInvestigator(boolean investigator) {
        this.investigator = investigator;
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
    
    /**
     * Getter for property sequenceNo.
     * @return Value of property sequenceNo.
     */
    public int getSequenceNo() {
        return sequenceNo;
    }
    
    /**
     * Setter for property sequenceNo.
     * @param sequenceNo New value of property sequenceNo.
     */
    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }
    
    /**
     * Getter for property awSequenceNo.
     * @return Value of property awSequenceNo.
     */
    public int getAwSequenceNo() {
        return awSequenceNo;
    }
    
    /**
     * Setter for property awSequenceNo.
     * @param awSequenceNo New value of property awSequenceNo.
     */
    public void setAwSequenceNo(int awSequenceNo) {
        this.awSequenceNo = awSequenceNo;
    }
    
}
