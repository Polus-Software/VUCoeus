/*
 * ProposalNarrativeModuleUsersFormBean.java
 *
 * Created on April 4, 2003, 4:57 PM
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;
import edu.mit.coeus.bean.BaseBean;
/**
 *
 * @author  mukund
 */
public class ProposalNarrativeModuleUsersFormBean implements java.io.Serializable, BaseBean{

    private static final String ACCESS_TYPE ="accessType";    
     //holds the proposal number
    private String proposalNumber;
     //holds the user id
    private String userId;
    //holds right : 'R' - View , 'M' - Modify 
    //private char right;
    //holds the module number
    private int moduleNumber;
    //holds accessType : 'R' - View , 'M' - Modify 
    private char accessType;
    //holds the userName
    private String userName;
    //holds update user id    
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;

    private PropertyChangeSupport propertySupport;

    /** Creates new ProposalNarrativeModuleUsersFormBean */
    public ProposalNarrativeModuleUsersFormBean() {
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

    /*public char getRight() {
        return right;
    }

    public void setRight(char right) {
        this.right = right;
    }*/
    
    public int getModuleNumber() {
        return moduleNumber;
    }

    public void setModuleNumber(int moduleNumber) {
        this.moduleNumber = moduleNumber;
    }

    public char getAccessType() {
        return accessType;
    }

    public void setAccessType(char newAccessType) {
        char oldAccessType = accessType;
        this.accessType = newAccessType;
        propertySupport.firePropertyChange(ACCESS_TYPE,
                oldAccessType, accessType);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
