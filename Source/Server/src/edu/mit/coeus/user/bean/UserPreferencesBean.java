/*
 * UserPreferencesBean.java
 *
 * Created on July 7, 2003, 3:48 PM
 */

package edu.mit.coeus.user.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This Class holds the Preferences for a User.
 * This bean will hold only one preference in variableName with value in
 * varValue.
 * @author  Senthil
 */
public class UserPreferencesBean implements java.io.Serializable{
    
    //holds user id
    private String userId;
    //holds Variable Name
    private String variableName;
    //holds value
    private String varValue;
    //holds Description for the Variable name
    private String varDescription;
    //holds default value of the variable name
    private String varDefaultValue;    
    //holds userId of the person who last updated the details
    private String updateUser;
    //holds timestamp of the last update
    private Timestamp updateTimestamp;
    
    //holds Datatype of the variable name
    private String varDataType;
    
    private static final String YES = "Yes";
    private static final String NO = "No";
    
    //holds AcType
    private String acType;
    
    /** Creates a new instance of UserPreferencesBean */
    public UserPreferencesBean() {
        
    }
    
    /** Getter for property userId.
     * @return Value of property userId.
     */
    public java.lang.String getUserId() {
        return userId;
    }
    
    /** Setter for property userId.
     * @param userId New value of property userId.
     */
    public void setUserId(java.lang.String userId) {
        this.userId = userId;
    }
    
    /** Getter for property varValue.
     * @return Value of property varValue.
     */
    public java.lang.String getVarValue() {
        return varValue;
    }
    
    /** Setter for property varValue.
     * @param varValue New value of property varValue.
     */
    public void setVarValue(java.lang.String varValue) {
        this.varValue = varValue;
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
    
    /** Getter for property variableName.
     * @return Value of property variableName.
     */
    public java.lang.String getVariableName() {
        return variableName;
    }
    
    /** Setter for property variableName.
     * @param variableName New value of property variableName.
     */
    public void setVariableName(java.lang.String variableName) {
        this.variableName = variableName;
    }
    
    /** Getter for property varDescription.
     * @return Value of property varDescription.
     */
    public java.lang.String getVarDescription() {
        return varDescription;
    }
    
    /** Setter for property varDescription.
     * @param varDescription New value of property varDescription.
     */
    public void setVarDescription(java.lang.String varDescription) {
        this.varDescription = varDescription;
    }
    
    /** Getter for property varDataType.
     * @return Value of property varDataType.
     */
    public java.lang.String getVarDataType() {
        return varDataType;
    }
    
    /** Setter for property varDataType.
     * @param varDataType New value of property varDataType.
     */
    public void setVarDataType(java.lang.String varDataType) {
        this.varDataType = varDataType;
    }
    
    /** Getter for property varDefaultValue.
     * @return Value of property varDefaultValue.
     */
    public java.lang.String getVarDefaultValue() {
        return varDefaultValue;
    }
    
    /** Setter for property varDefaultValue.
     * @param varDefaultValue New value of property varDefaultValue.
     */
    public void setVarDefaultValue(java.lang.String varDefaultValue) {
        this.varDefaultValue = varDefaultValue;
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
    
}
