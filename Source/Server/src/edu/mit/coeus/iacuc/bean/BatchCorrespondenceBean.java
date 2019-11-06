/*
 * BatchCorrespondenceBean.java
 *
 * Created on March 3, 2004, 10:43 AM
 */

package edu.mit.coeus.iacuc.bean;

import java.io.Serializable;

public class BatchCorrespondenceBean implements Serializable
{
    
    private int correspondenceBatchTypeCode ;
    private String description ;
    private int defaultTimeWindow ;
    private int correspondenceNumber ;
    private int correspondenceTypeCode ;
    private int numberOfDaysTillNext ;

    private String correspondenceBatchId ;
    private String committeeId ;
    private String committeeName ;
    private java.sql.Date batchRunDate ;
    private java.sql.Date  timeWindowStart ; 
    private java.sql.Date timeWindowEnd ;
    
    private java.sql.Timestamp todaysDate ;
     
    private String updateUser ;
    //Added for userid to username enhancement
    private String updateUserName ;
    private java.sql.Timestamp updateTimestamp ;
    
   
    private int finalActionTypeCode ; // FINAL_ACTION_TYPE_CODE   
    private int finalActionCorrespondenceType ; //FINAL_ACTION_CORRESP_TYPE
    private String finalActionTypeDesc;// FINAL_ACTION_CORRESP_TYPE_DESCRIPTION 
    
    
    
    public BatchCorrespondenceBean() 
    {
    }
    
    /** Getter for property correspondenceBatchTypeCode.
     * @return Value of property correspondenceBatchTypeCode.
     *
     */
    public int getCorrespondenceBatchTypeCode() {
        return correspondenceBatchTypeCode;
    }
    
    /** Setter for property correspondenceBatchTypeCode.
     * @param correspondenceBatchTypeCode New value of property correspondenceBatchTypeCode.
     *
     */
    public void setCorrespondenceBatchTypeCode(int correspondenceBatchTypeCode) {
        this.correspondenceBatchTypeCode = correspondenceBatchTypeCode;
    }
    
    /** Getter for property defaultTimeWindow.
     * @return Value of property defaultTimeWindow.
     *
     */
    public int getDefaultTimeWindow() {
        return defaultTimeWindow;
    }
    
    /** Setter for property defaultTimeWindow.
     * @param defaultTimeWindow New value of property defaultTimeWindow.
     *
     */
    public void setDefaultTimeWindow(int defaultTimeWindow) {
        this.defaultTimeWindow = defaultTimeWindow;
    }
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /** Getter for property correspondenceNumber.
     * @return Value of property correspondenceNumber.
     *
     */
    public int getCorrespondenceNumber() {
        return correspondenceNumber;
    }
    
    /** Setter for property correspondenceNumber.
     * @param correspondenceNumber New value of property correspondenceNumber.
     *
     */
    public void setCorrespondenceNumber(int correspondenceNumber) {
        this.correspondenceNumber = correspondenceNumber;
    }
    
    /** Getter for property correspondenceTypeCode.
     * @return Value of property correspondenceTypeCode.
     *
     */
    public int getCorrespondenceTypeCode() {
        return correspondenceTypeCode;
    }
    
    /** Setter for property correspondenceTypeCode.
     * @param correspondenceTypeCode New value of property correspondenceTypeCode.
     *
     */
    public void setCorrespondenceTypeCode(int correspondenceTypeCode) {
        this.correspondenceTypeCode = correspondenceTypeCode;
    }
    
    /** Getter for property numberOfDaysTillNext.
     * @return Value of property numberOfDaysTillNext.
     *
     */
    public int getNumberOfDaysTillNext() {
        return numberOfDaysTillNext;
    }
    
    /** Setter for property numberOfDaysTillNext.
     * @param numberOfDaysTillNext New value of property numberOfDaysTillNext.
     *
     */
    public void setNumberOfDaysTillNext(int numberOfDaysTillNext) {
        this.numberOfDaysTillNext = numberOfDaysTillNext;
    }
    
    /** Getter for property correspondenceBatchId.
     * @return Value of property correspondenceBatchId.
     *
     */
    public java.lang.String getCorrespondenceBatchId() {
        return correspondenceBatchId;
    }
    
    /** Setter for property correspondenceBatchId.
     * @param correspondenceBatchId New value of property correspondenceBatchId.
     *
     */
    public void setCorrespondenceBatchId(java.lang.String correspondenceBatchId) {
        this.correspondenceBatchId = correspondenceBatchId;
    }
    
    /** Getter for property committeeId.
     * @return Value of property committeeId.
     *
     */
    public java.lang.String getCommitteeId() {
        return committeeId;
    }
    
    /** Setter for property committeeId.
     * @param committeeId New value of property committeeId.
     *
     */
    public void setCommitteeId(java.lang.String committeeId) {
        this.committeeId = committeeId;
    }
    
    /** Getter for property batchRunDate.
     * @return Value of property batchRunDate.
     *
     */
    public java.sql.Date getBatchRunDate() {
        return batchRunDate;
    }
    
    /** Setter for property batchRunDate.
     * @param batchRunDate New value of property batchRunDate.
     *
     */
    public void setBatchRunDate(java.sql.Date batchRunDate) {
        this.batchRunDate = batchRunDate;
    }
    
    /** Getter for property timeWindowStart.
     * @return Value of property timeWindowStart.
     *
     */
    public java.sql.Date getTimeWindowStart() {
        return timeWindowStart;
    }
    
    /** Setter for property timeWindowStart.
     * @param timeWindowStart New value of property timeWindowStart.
     *
     */
    public void setTimeWindowStart(java.sql.Date timeWindowStart) {
        this.timeWindowStart = timeWindowStart;
    }
    
    /** Getter for property timeWindowEnd.
     * @return Value of property timeWindowEnd.
     *
     */
    public java.sql.Date getTimeWindowEnd() {
        return timeWindowEnd;
    }
    
    /** Setter for property timeWindowEnd.
     * @param timeWindowEnd New value of property timeWindowEnd.
     *
     */
    public void setTimeWindowEnd(java.sql.Date timeWindowEnd) {
        this.timeWindowEnd = timeWindowEnd;
    }
    
    /** Getter for property todaysDate.
     * @return Value of property todaysDate.
     *
     */
    public java.sql.Timestamp getTodaysDate() {
        return todaysDate;
    }
    
    /** Setter for property todaysDate.
     * @param todaysDate New value of property todaysDate.
     *
     */
    public void setTodaysDate(java.sql.Timestamp todaysDate) {
        this.todaysDate = todaysDate;
    }
    
    /** Getter for property updateUser.
     * @return Value of property updateUser.
     *
     */
    public java.lang.String getUpdateUser() {
        return updateUser;
    }    
    
    /** Setter for property updateUser.
     * @param updateUser New value of property updateUser.
     *
     */
    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }
    
    
    /** Getter for property committeeName.
     * @return Value of property committeeName.
     *
     */
    public java.lang.String getCommitteeName() {
        return committeeName;
    }
    
    /** Setter for property committeeName.
     * @param committeeName New value of property committeeName.
     *
     */
    public void setCommitteeName(java.lang.String committeeName) {
        this.committeeName = committeeName;
    }
    
    /** Getter for property updateTimestamp.
     * @return Value of property updateTimestamp.
     *
     */
    public java.sql.Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }    
    
    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     *
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }    
         
    
    /** Getter for property finalActionTypeCode.
     * @return Value of property finalActionTypeCode.
     *
     */
    public int getFinalActionTypeCode() {
        return finalActionTypeCode;
    }
       
    /** Setter for property finalActionTypeCode.
     * @param finalActionTypeCode New value of property finalActionTypeCode.
     *
     */
    public void setFinalActionTypeCode(int finalActionTypeCode) {
        this.finalActionTypeCode = finalActionTypeCode;
    }
          
    /** Getter for property finalActionCorrespondenceType.
     * @return Value of property finalActionCorrespondenceType.
     *
     */
    public int getFinalActionCorrespondenceType() {
        return finalActionCorrespondenceType;
    }
    
    /** Setter for property finalActionCorrespondenceType.
     * @param finalActionCorrespondenceType New value of property finalActionCorrespondenceType.
     *
     */
    public void setFinalActionCorrespondenceType(int finalActionCorrespondenceType) {
        this.finalActionCorrespondenceType = finalActionCorrespondenceType;
    }
    
    /** Getter for property finalActionCorrespondenceType.
     * @return Value of property finalActionCorrespondenceType.
     *
     */
    public String getFinalActionTypeDesc() {
        return finalActionTypeDesc;
    }
    
    /**
     * Setter for property finalActionTypeDesc.
     * 
     * @param finalActionTypeDesc New value of property finalActionTypeDesc.
     */
    public void setFinalActionTypeDesc(String finalActionTypeDesc) {
        this.finalActionTypeDesc = finalActionTypeDesc;
    }
    
    //Userid to Username enhancement - Start
    /** Getter for property updateUserName
     * @return Value of property updateUserName.
     */
    public String getUpdateUserName() {
        return updateUserName;
    }
    
    /** Setter for property updateUserName.
     * @param updateUserName new value of property updateUserName.
     */
    
    public void setUpdateUserName(String udpateUserName) {
        this.updateUserName = udpateUserName;
    }
    //Userid to Username enhancement - End
    //prps code end Apr 1 2004
}
