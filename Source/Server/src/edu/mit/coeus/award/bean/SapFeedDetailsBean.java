/**
 * @(#)SapFeedDetailsBean.java 1.0 08/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.award.bean;

import java.sql.Date;
import java.sql.Timestamp;
import edu.mit.coeus.bean.CoeusBean;
import edu.mit.coeus.bean.IBaseDataBean;
import edu.mit.coeus.bean.ComparableBean;
/**
 * This class is used to hold Sap Feed Details data
 *
 * @author Prasanna Kumar K
 * @version 1.0
 * Created on March 08, 2004 12:30 PM
 */

public class SapFeedDetailsBean implements CoeusBean, IBaseDataBean, java.io.Serializable{
    
    private int feedId;
    private String mitAwardNumber;
    private int sequenceNumber;
    private String feedType;
    private String feedStatus;
    private int batchId;
    private String transactionId;
    private String updateUser;
    //Added for useid to username enhancement
    private String updateUserName;
    
    private java.sql.Timestamp updateTimestamp;
    private String acType;
    
    // Added by Shivakumar for SAP FEED BATCHES    
    private String batchFileName;
    private int noOfRecords;
    private String errorMessage;
    
    /**
     *	Default Constructor
     */
    
    public SapFeedDetailsBean(){
    }    
    
    /** Getter for property feedId.
     * @return Value of property feedId.
     */
    public int getFeedId() {
        return feedId;
    }
    
    /** Setter for property feedId.
     * @param feedId New value of property feedId.
     */
    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }
    
    /** Getter for property mitAwardNumber.
     * @return Value of property mitAwardNumber.
     */
    public java.lang.String getMitAwardNumber() {
        return mitAwardNumber;
    }
    
    /** Setter for property mitAwardNumber.
     * @param mitAwardNumber New value of property mitAwardNumber.
     */
    public void setMitAwardNumber(java.lang.String mitAwardNumber) {
        this.mitAwardNumber = mitAwardNumber;
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
    
    /** Getter for property feedType.
     * @return Value of property feedType.
     */
    public java.lang.String getFeedType() {
        return feedType;
    }
    
    /** Setter for property feedType.
     * @param feedType New value of property feedType.
     */
    public void setFeedType(java.lang.String feedType) {
        this.feedType = feedType;
    }
    
    /** Getter for property feedStatus.
     * @return Value of property feedStatus.
     */
    public java.lang.String getFeedStatus() {
        return feedStatus;
    }
    
    /** Setter for property feedStatus.
     * @param feedStatus New value of property feedStatus.
     */
    public void setFeedStatus(java.lang.String feedStatus) {
        this.feedStatus = feedStatus;
    }
    
    /** Getter for property batchId.
     * @return Value of property batchId.
     */
    public int getBatchId() {
        return batchId;
    }
    
    /** Setter for property batchId.
     * @param batchId New value of property batchId.
     */
    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
    
    /** Getter for property transactionId.
     * @return Value of property transactionId.
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }
    
    /** Setter for property transactionId.
     * @param transactionId New value of property transactionId.
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
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
    
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public boolean isLike(ComparableBean comparableBean){
        return true;
    }
    
    /** Getter for property batchFileName.
     * @return Value of property batchFileName.
     *
     */
    public java.lang.String getBatchFileName() {
        return batchFileName;
    }
    
    /** Setter for property batchFileName.
     * @param batchFileName New value of property batchFileName.
     *
     */
    public void setBatchFileName(java.lang.String batchFileName) {
        this.batchFileName = batchFileName;
    }
    
    /** Getter for property noOfRecords.
     * @return Value of property noOfRecords.
     *
     */
    public int getNoOfRecords() {
        return noOfRecords;
    }
    
    /** Setter for property noOfRecords.
     * @param noOfRecords New value of property noOfRecords.
     *
     */
    public void setNoOfRecords(int noOfRecords) {
        this.noOfRecords = noOfRecords;
    }
    
    /** Getter for property errorMessage.
     * @return Value of property errorMessage.
     *
     */
    public java.lang.String getErrorMessage() {
        return errorMessage;
    }
    
    /** Setter for property errorMessage.
     * @param errorMessage New value of property errorMessage.
     *
     */
    public void setErrorMessage(java.lang.String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    //Userid to username enhancement - Start
    
    /** Getter for property updateUserName.
     * @return Value of property updateUserName.
     */
    public java.lang.String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     */
    public void setUpdateUserName(java.lang.String updateUserName) {
        this.updateUserName = updateUserName;
    }
    //Userid to username enhancement - End
}