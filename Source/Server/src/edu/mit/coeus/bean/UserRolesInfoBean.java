/*
 * UserRolesInfoBean.java
 *
 * Created on April 10, 2003, 4:59 PM
 */

package edu.mit.coeus.bean;

import java.util.Vector;
import java.io.Serializable;
/**
 *
 * @author  ravikanth
 */
public class UserRolesInfoBean implements Serializable{
    
    private RoleInfoBean roleBean;
    private UserInfoBean userBean;
    private boolean isRole;
    private boolean hasChildren;
    private Vector users;
    private String updateUser;
    private java.sql.Timestamp updateTimestamp;
    
    /** Creates new UserRolesInfoBean */
    public UserRolesInfoBean() {
    }
    
    /** Getter for property users.
     * @return Value of property users.
     */
    public Vector getUsers() {
        return users;
    }
    
    /** Setter for property users.
     * @param users New value of property users.
     */
    public void setUsers(Vector users) {
        this.users = users;
    }
    
    /** Getter for property isRole.
     * @return Value of property isRole.
     */
    public boolean isRole() {
        return isRole;
    }
    
    /** Setter for property isRole.
     * @param isRole New value of property isRole.
     */
    public void setIsRole(boolean isRole) {
        this.isRole = isRole;
    }
    
    /** Getter for property userBean.
     * @return Value of property userBean.
     */
    public UserInfoBean getUserBean() {
        return userBean;
    }
    
    /** Setter for property userBean.
     * @param userBean New value of property userBean.
     */
    public void setUserBean(UserInfoBean userBean) {
        this.userBean = userBean;
    }
    
    /** Getter for property roleBean.
     * @return Value of property roleBean.
     */
    public RoleInfoBean getRoleBean() {
        return roleBean;
    }
    
    /** Setter for property roleBean.
     * @param roleBean New value of property roleBean.
     */
    public void setRoleBean(RoleInfoBean roleBean) {
        this.roleBean = roleBean;
    }
    
    /** Getter for property hasChildren.
     * @return Value of property hasChildren.
     */
    public boolean hasChildren() {
        return hasChildren;
    }
    
    /** Setter for property hasChildren.
     * @param hasChildren New value of property hasChildren.
     */
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    
    public String toString() {
        if(isRole){
            if( roleBean != null ) {
                return roleBean.getRoleName();
            }
        }else {
            if( userBean != null ) {
                return userBean.getUserId() + "  "+userBean.getUserName();
            }
        }
        return "";
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
    
}
