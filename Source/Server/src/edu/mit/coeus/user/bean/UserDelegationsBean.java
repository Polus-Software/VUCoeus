/*
 * UserDelegationsBean.java
 *
 * Created on July 10, 2003, 6:48 PM
 */

package edu.mit.coeus.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;

/**
 * This Class holds the Delegations for a User.
 *
 * @author  Prasanna Kumar
 */
public class UserDelegationsBean implements java.io.Serializable,BaseBean{

    //holds user Name or delegated By name
    private String userName;
    //holds delegated By
    private String delegatedBy;
    //holds delegated To
    private String delegatedTo;
    //holds delegated To Name
    private String delegatedToName;
    //holds value effectiveDate
    private java.sql.Date effectiveDate;
    //holds value endDate
    private java.sql.Date endDate;    
    //holds status
    private char status;
    //holds userId of the person who last updated the details
    private String updateUser;
    //holds timestamp of the last update
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    //holds aw parameter of delegated by
    private String aw_DelegatedBy;
    //holds aw parameter of delegated To
    private String aw_DelegatedTo;    
    //holds aw parameter of effectiveDate
    private java.sql.Date aw_EffectiveDate;    
    //holds aw parameter of status
    private char aw_Status;
    
    //Added for Case#3682 - Enhancements related to Delegations - Start
    private int delegationID;
    //Added for Case#3682 - Enhancements related to Delegations - End
    
    /** Creates a new instance of UserDelegationsBean */
    public UserDelegationsBean() {

    }

    /** Getter for property userName.
     * @return Value of property userName.
     */
    public java.lang.String getUserName() {
        return userName;
    }

    /** Setter for property userName.
     * @param userId New value of property userName.
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    /** Getter for property delegatedBy.
     * @return Value of property delegatedBy.
     */
    public java.lang.String getDelegatedBy() {
        return delegatedBy;
    }

    /** Setter for property delegatedBy.
     * @param delegatedBy New value of property delegatedBy.
     */
    public void setDelegatedBy(java.lang.String delegatedBy) {
        this.delegatedBy = delegatedBy;
    }

    /** Getter for property delegatedTo.
     * @return Value of property delegatedTo.
     */
    public java.lang.String getDelegatedTo() {
        return delegatedTo;
    }

    /** Setter for property delegatedTo.
     * @param delegatedTo New value of property delegatedTo.
     */
    public void setDelegatedTo(java.lang.String delegatedTo) {
        this.delegatedTo = delegatedTo;
    }
    
    /** Getter for property effectiveDate.
     * @return Value of property effectiveDate.
     */
    public java.sql.Date getEffectiveDate() {
        return effectiveDate;
    }

    /** Setter for property effectiveDate.
     * @param effectiveDate New value of property effectiveDate.
     */
    public void setEffectiveDate(java.sql.Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
    
    /** Getter for property endDate.
     * @return Value of property endDate.
     */
    public java.sql.Date getEndDate() {
        return endDate;
    }

    /** Setter for property endDate.
     * @param effectiveDate New value of property endDate.
     */
    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    /** Getter for property status.
     * @return Value of property status.
     */
    public char getStatus() {
        return status;
    }

    /** Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(char status) {
        this.status = status    ;
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
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    /** Setter for property updateTimestamp.
     * @param updateTimestamp New value of property updateTimestamp.
     */
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Account Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    /**
     * Method used to set the Account Type
     * @param acType String
     */
    public void setAcType(String acType){
        this.acType = acType;
    }
    
    /** Getter for property aw_DelegatedBy.
     * @return Value of property aw_DelegatedBy.
     */
    public java.lang.String getAw_DelegatedBy() {
        return aw_DelegatedBy;
    }
    
    /** Setter for property aw_DelegatedBy.
     * @param aw_DelegatedBy New value of property aw_DelegatedBy.
     */
    public void setAw_DelegatedBy(java.lang.String aw_DelegatedBy) {
        this.aw_DelegatedBy = aw_DelegatedBy;
    }
    
    /** Getter for property aw_DelegatedTo.
     * @return Value of property aw_DelegatedTo.
     */
    public java.lang.String getAw_DelegatedTo() {
        return aw_DelegatedTo;
    }
    
    /** Setter for property aw_DelegatedTo.
     * @param aw_DelegatedTo New value of property aw_DelegatedTo.
     */
    public void setAw_DelegatedTo(java.lang.String aw_DelegatedTo) {
        this.aw_DelegatedTo = aw_DelegatedTo;
    }
    
    /** Getter for property aw_EffectiveDate.
     * @return Value of property aw_EffectiveDate.
     */
    public java.sql.Date getAw_EffectiveDate() {
        return aw_EffectiveDate;
    }
    
    /** Setter for property aw_EffectiveDate.
     * @param aw_EffectiveDate New value of property aw_EffectiveDate.
     */
    public void setAw_EffectiveDate(java.sql.Date aw_EffectiveDate) {
        this.aw_EffectiveDate = aw_EffectiveDate;
    }
    
    /** Getter for property aw_Status.
     * @return Value of property aw_Status.
     */
    public char getAw_Status() {
        return aw_Status;
    }
    
    /** Setter for property aw_Status.
     * @param aw_Status New value of property aw_Status.
     */
    public void setAw_Status(char aw_Status) {
        this.aw_Status = aw_Status;
    }
    
    /** Getter for property delegatedToName.
     * @return Value of property delegatedToName.
     */
    public java.lang.String getDelegatedToName() {
        return delegatedToName;
    }
    
    /** Setter for property delegatedToName.
     * @param delegatedToName New value of property delegatedToName.
     */
    public void setDelegatedToName(java.lang.String delegatedToName) {
        this.delegatedToName = delegatedToName;
    }
   //Added for Case#3682 - Enhancements related to Delegations - Start
    /** Getter for property delegationID.
     * @return Value of property delegationID.
     */
    public int getDelegationID() {
        return delegationID;
    }
    
    /** Setter for property delegationID.
     * @param delegationID New value of property delegationID.
     */
    public void setDelegationID(int delegationID) {
        this.delegationID = delegationID;
    }
    //Added for Case#3682 - Enhancements related to Delegations - End
}
