/*
 * @(#)RoleRightInfoBean.java 1.0 04/14/03 10:40 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.bean;

import java.beans.*;


/**
 * The class used to hold the information of <code>Role Right Details</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on April 14, 2003, 10:40 AM
 */

public class RoleRightInfoBean implements java.io.Serializable, BaseBean {
    
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
    //Added for Coeus 4.3 enhancement PT ID:2232 - Custom Roles - start
    private String acType;
    //Added for Coeus 4.3 enhancement PT ID:2232 - Custom Roles - end
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates new RoleRightInfoBean */
    public RoleRightInfoBean() {
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
    
    public void setRoleId(int roleId){
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
    
    public String getUpdateUser(){
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser){
        this.updateUser = updateUser;
    }
    
    public java.sql.Timestamp getUpdateTimestamp(){
        return updateTimestamp;
    }
    
    public void setUpdateTimestamp(java.sql.Timestamp updateTimestamp){
        this.updateTimestamp = updateTimestamp;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    //Added for Coeus 4.3 enhancement PT ID:2232 - Custom Roles - start
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public String getAcType() {
        return acType;
    }
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(String acType) {
        this.acType = acType;
    }
    //Added for Coeus 4.3 enhancement PT ID:2232 - Custom Roles - start
}
