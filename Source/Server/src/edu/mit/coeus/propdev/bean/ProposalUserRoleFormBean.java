/*
 * ProposalUserRoleFormBean.java
 *
 * Created on April 4, 2003, 12:30 PM
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;

/**
 *
 * @author  mukund
 */
public class ProposalUserRoleFormBean implements java.io.Serializable {
    
    private static final String PROP_UNIT_NUMBER = "unitNumber";
    //holds the proposal number
    private String proposalNumber;
    //holds the user id
    private String userId;
    //holds the role Id
    private int roleId;
    //holds the user name
    private String userName;
    //holds the unit number
    private String unitNumber;
    //holds the unit name
    private String unitName;
    //holds the status
    private char status;
     //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
    
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalUserRoleFormBean */
    public ProposalUserRoleFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getRoleId(){
        return roleId;
     }
     
     public void setRoleId (int roleId){
        this.roleId = roleId;
     }
     
    public String getUserName(){
        return userName;
     }
     
     public void setUserName (String userName){
        this.userName = userName;
     }
     
    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String newUnitNumber) {
        String oldUnitNumber = unitNumber;
        this.unitNumber  = newUnitNumber;
        propertySupport.firePropertyChange(PROP_UNIT_NUMBER, 
                oldUnitNumber, unitNumber);
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    
    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }
    
    public String getUpdateUser(){
        return updateUser;
    }
    
    /**
     * Method used to set the update user id
     * @param updateUser String
     */
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    /**
     * Method used to get the update timestamp
     * @return updateTimestamp Timestamp
     */
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }
    
    /**
     * Method used to set the update timestamp
     * @param updateTimestamp Timestamp
     */
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }
    
    /**
     * Method used to get the Action Type
     * @return acType String
     */
    public String getAcType(){
        return acType;
    }
    
    public void setAcType(String acType){
        this.acType = acType;
    }
     
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
