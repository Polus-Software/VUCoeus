/*
 * @(#)ProposalDataOveridesBean.java 1.0 01/07/04 10:45 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import edu.mit.coeus.customelements.bean.*;
import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Proposal Others</code>
 * which extends the PersonCustomElementsInfoBean.
 *
 * @author  Prasanna Kumar K
 * @version 1.0
 * Created on January 07, 2004, 10:45 AM
 */

public class ProposalDataOveridesBean implements java.io.Serializable, BaseBean {
    //holds the proposal number
    private String columnName;
    private String columnLabel;
    private boolean hasLookUp;
    private String proposalNumber;
    private String oldDisplayValue;
    private int changedNumber;
    private String changedValue;
    private String displayValue;
    private String comments;
     //holds update user id
     private String updateUser;
     //holds update timestamp
     private Timestamp updateTimestamp;
     //holds account type
     private String acType;
     //holds dataType
     private String dataType;
    public ProposalDataOveridesBean(){

    }    

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }
    
    /** Getter for property oldDisplayValue.
     * @return Value of property oldDisplayValue.
     */
    public java.lang.String getOldDisplayValue() {
        return oldDisplayValue;
    }
    
    /** Setter for property oldDisplayValue.
     * @param oldDisplayValue New value of property oldDisplayValue.
     */
    public void setOldDisplayValue(java.lang.String oldDisplayValue) {
        this.oldDisplayValue = oldDisplayValue;
    }    
    
    /** Getter for property changedNumber.
     * @return Value of property changedNumber.
     */
    public int getChangedNumber() {
        return changedNumber;
    }
    
    /** Setter for property changedNumber.
     * @param changedNumber New value of property changedNumber.
     */
    public void setChangedNumber(int changedNumber) {
        this.changedNumber = changedNumber;
    }    
    
    /** Getter for property changedValue.
     * @return Value of property changedValue.
     */
    public String getChangedValue() {
        return changedValue;
    }
    
    /** Setter for property changedValue.
     * @param changedValue New value of property changedValue.
     */
    public void setChangedValue(String changedValue) {
        this.changedValue = changedValue;
    }    
    
    /** Getter for property displayValue.
     * @return Value of property displayValue.
     */
    public java.lang.String getDisplayValue() {
        return displayValue;
    }
    
    /** Setter for property displayValue.
     * @param displayValue New value of property displayValue.
     */
    public void setDisplayValue(java.lang.String displayValue) {
        this.displayValue = displayValue;
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     */
    public java.lang.String getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     */
    public void setComments(java.lang.String comments) {
        this.comments = comments;
    }    
    
    /** Getter for property columnName.
     * @return Value of property columnName.
     */
    public java.lang.String getColumnName() {
        return columnName;
    }
    
    /** Setter for property columnName.
     * @param columnName New value of property columnName.
     */
    public void setColumnName(java.lang.String columnName) {
        this.columnName = columnName;
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
    
    /** Getter for property columnLabel.
     * @return Value of property columnLabel.
     */
    public java.lang.String getColumnLabel() {
        return columnLabel;
    }
    
    /** Setter for property columnLabel.
     * @param columnLabel New value of property columnLabel.
     */
    public void setColumnLabel(java.lang.String columnLabel) {
        this.columnLabel = columnLabel;
    }
    
    /** Getter for property hasLookUp.
     * @return Value of property hasLookUp.
     */
    public boolean isHasLookUp() {
        return hasLookUp;
    }
    
    /** Setter for property hasLookUp.
     * @param hasLookUp New value of property hasLookUp.
     */
    public void setHasLookUp(boolean hasLookUp) {
        this.hasLookUp = hasLookUp;
    }   
    
    public boolean equals(Object obj) {
        ProposalDataOveridesBean proposalDataOveridesBean = (ProposalDataOveridesBean)obj;
        if(proposalDataOveridesBean.getProposalNumber().equals(getProposalNumber()) &&
        proposalDataOveridesBean.getColumnName().equals(getColumnName()) &&
        proposalDataOveridesBean.getChangedNumber()== getChangedNumber()){
            return true;
        }else {
            return false;
        }
    }
    
    /** Getter for property dataType.
     * @return Value of property dataType.
     */
    public java.lang.String getDataType() {
        return dataType;
    }
    
    /** Setter for property dataType.
     * @param dataType New value of property dataType.
     */
    public void setDataType(java.lang.String dataType) {
        this.dataType = dataType;
    }    
}