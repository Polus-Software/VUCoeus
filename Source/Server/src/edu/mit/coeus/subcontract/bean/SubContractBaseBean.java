/**
 * @(#)AwardBean.java 1.0 01/19/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.subcontract.bean;
import java.util.Vector;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.ComparableBean;
import edu.mit.coeus.exception.CoeusException;
/**
 * This class is used to set and get the SubContract details
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on January 19, 2004 12:30 PM
**/

public class SubContractBaseBean implements CoeusBean, IBaseDataBean, java.io.Serializable{
    
    private String subContractCode = null;
    private int sequenceNumber = -1;
    private String updateUser = null;
    private java.sql.Timestamp updateTimestamp = null;
    private String acType = null;
    //Code added for Princeton enhancements case#2802
    //To display the userName
    private String updateUserName = null;   
    /**
     *	Default Constructor
     */
    
    public SubContractBaseBean(){
    }    

    /** Getter for property subContractCode.
     * @return Value of property subContractCode.
     */
    public java.lang.String getSubContractCode() {
        return subContractCode;
    }
    
    /** Setter for property subContractCode.
     * @param subContractCode New value of property subContractCode.
     */
    public void setSubContractCode(java.lang.String subContractCode) {
        this.subContractCode = subContractCode;
    }
    
    /** Getter for property sequenceNumber.
     * @return Value of property sequenceNumber.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }
    
    /** Setter for property sequenceNumber.
     * @param sequenceNumber New value of property sequenceNumber.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
    
    public boolean isLike(ComparableBean comparableBean)
    throws CoeusException{
        return true;
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
    /** 
     * Equals method to check whether Primary Key values are same for the given Bean
     * @param obj Object
     * @return  boolean indicating whether the two beans are same or not
     */ 
    
    public boolean equals(Object obj) {
        SubContractBaseBean subContractBaseBean = (SubContractBaseBean)obj;
        if((subContractBaseBean.getSubContractCode().equals(getSubContractCode())) &&
               (subContractBaseBean.getSequenceNumber() == getSequenceNumber())){
                   return true;
        }else{
                   return false; 
        }    
    }    

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
    
}