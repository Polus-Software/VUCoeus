/* 
 * @(#)ProposalRoleRightFormBean.java 1.0 03/27/03 12:05 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.propdev.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Proposal Roles and Right</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 27, 2003, 12:05 PM 
 */

public class ProposalRoleRightFormBean implements java.io.Serializable {
    
    private static final String PROP_DESC_FLAG_PROPERTY = "descendFlag";
    private static final String PROP_ROLE_DESC_FLAG_PROPERTY = "roleDescendFlag";
    // holds the right id
    private String rightId;
    //holds the role Id
    private int roleId;
    // holds the description
    private String description;
    //holds the rightType
    private char rightType;
    // holds the descendFlag
    private boolean descendFlag;
    // holds the roleDescendFlag
    private boolean roleDescendFlag;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private java.sql.Timestamp updateTimestamp;
    //holds Action type
    private String acType;
     
     private PropertyChangeSupport propertySupport;
    
    /** Creates new ProposalRightFormBean */
    public ProposalRoleRightFormBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public String getRightId(){
        return rightId;
    }
    
    public void setRightId(String rightId){
        this.rightId = rightId;
    }
    
    public int getRoleId(){
        return roleId;
     }
     
     public void setRoleId (int roleId){
        this.roleId = roleId;
     }
     
      public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public char getRightType(){
        return rightType;
    }
    
    public void setRightType(char rightType){
        this.rightType = rightType;
    }
    
     public boolean isDescendFlag(){
        return descendFlag;
    }
    
    public void setDescendFlag(boolean newDescendFlag){
        boolean oldDescendFlag = descendFlag;
        this.descendFlag  = newDescendFlag;
        propertySupport.firePropertyChange(PROP_DESC_FLAG_PROPERTY, 
                oldDescendFlag, descendFlag);
    }

     public boolean isRoleDescendFlag(){
        return roleDescendFlag;
    }
    
    public void setRoleDescendFlag(boolean newRoleDescendFlag){
        boolean oldRoleDescendFlag = roleDescendFlag;
        this.roleDescendFlag  = newRoleDescendFlag;
        propertySupport.firePropertyChange(PROP_ROLE_DESC_FLAG_PROPERTY, 
                oldRoleDescendFlag, roleDescendFlag);
    }
    /**
     * Method used to get the update user id
     * @return updateUser String
     */
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
    
    /**
     * Method used to set the Action Type
     * @param acType String
     */
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
