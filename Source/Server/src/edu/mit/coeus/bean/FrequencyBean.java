/*
 * @(#)FrequencyBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;
import java.sql.Date;
import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.ComboBoxBean;
/**
 * The class used to hold the information of <code>Frequency</code>
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 17, 2003, 12:26 PM
 */

public class FrequencyBean extends ComboBoxBean implements java.io.Serializable, BaseBean {

    //holds Reference number    
    //private int frequencyCode;
    //holds description
    //private String description;
    //holds numberOfDays
    private int numberOfDays;
    //holds numberOfMonths
    private int numberOfMonths;
    //holds repeatFlag
    private String repeatFlag;
    //holds proposalDueFlag
    private String proposalDueFlag;
    //holds invoiceFlag
    private String invoiceFlag;
    //holds advanceNumberOfDays
    private int advanceNumberOfDays;
    //holds advanceNumberOfMonths
    private int advanceNumberOfMonths;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;

    /** Creates new FrequencyBean */
    public FrequencyBean() {
    }
    
    /** Getter for property numberOfDays.
     * @return Value of property numberOfDays.
     */
    public int getNumberOfDays() {
        return numberOfDays;
    }
    
    /** Setter for property numberOfDays.
     * @param numberOfDays New value of property numberOfDays.
     */
    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
    
    /** Getter for property numberOfMonths.
     * @return Value of property numberOfMonths.
     */
    public int getNumberOfMonths() {
        return numberOfMonths;
    }
    
    /** Setter for property numberOfMonths.
     * @param numberOfMonths New value of property numberOfMonths.
     */
    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }
    
    /** Getter for property repeatFlag.
     * @return Value of property repeatFlag.
     */
    public java.lang.String getRepeatFlag() {
        return repeatFlag;
    }
    
    /** Setter for property repeatFlag.
     * @param repeatFlag New value of property repeatFlag.
     */
    public void setRepeatFlag(java.lang.String repeatFlag) {
        this.repeatFlag = repeatFlag;
    }
    
    /** Getter for property proposalDueFlag.
     * @return Value of property proposalDueFlag.
     */
    public java.lang.String getProposalDueFlag() {
        return proposalDueFlag;
    }
    
    /** Setter for property proposalDueFlag.
     * @param proposalDueFlag New value of property proposalDueFlag.
     */
    public void setProposalDueFlag(java.lang.String proposalDueFlag) {
        this.proposalDueFlag = proposalDueFlag;
    }
    
    /** Getter for property invoiceFlag.
     * @return Value of property invoiceFlag.
     */
    public java.lang.String getInvoiceFlag() {
        return invoiceFlag;
    }
    
    /** Setter for property invoiceFlag.
     * @param invoiceFlag New value of property invoiceFlag.
     */
    public void setInvoiceFlag(java.lang.String invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }
    
    /** Getter for property advanceNumberOfDays.
     * @return Value of property advanceNumberOfDays.
     */
    public int getAdvanceNumberOfDays() {
        return advanceNumberOfDays;
    }
    
    /** Setter for property advanceNumberOfDays.
     * @param advanceNumberOfDays New value of property advanceNumberOfDays.
     */
    public void setAdvanceNumberOfDays(int advanceNumberOfDays) {
        this.advanceNumberOfDays = advanceNumberOfDays;
    }
    
    /** Getter for property advanceNumberOfMonths.
     * @return Value of property advanceNumberOfMonths.
     */
    public int getAdvanceNumberOfMonths() {
        return advanceNumberOfMonths;
    }
    
    /** Setter for property advanceNumberOfMonths.
     * @param advanceNumberOfMonths New value of property advanceNumberOfMonths.
     */
    public void setAdvanceNumberOfMonths(int advanceNumberOfMonths) {
        this.advanceNumberOfMonths = advanceNumberOfMonths;
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