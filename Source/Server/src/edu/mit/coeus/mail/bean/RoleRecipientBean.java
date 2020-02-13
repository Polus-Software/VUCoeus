/*
 * RoleRecipientBean.java
 *
 * Created on July 10, 2009, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.mail.bean;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author keerthyjayaraj
 */
public class RoleRecipientBean implements Serializable{
    
    private int roleId;
    private String roleName;
    private String roleQualifier;
    private String roleQualifierName;
    private char toOrCC;
    private Vector personInfo;
    
    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleQualifier() {
        return roleQualifier;
    }

    public String getRoleQualifierName() {
        return roleQualifierName;
    }

    public char getToOrCC() {
        return toOrCC;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setRoleQualifier(String roleQualifier) {
        this.roleQualifier = roleQualifier;
    }

    public void setRoleQualifierName(String roleQualifierName) {
        this.roleQualifierName = roleQualifierName;
    }

    public void setToOrCC(char toOrCC) {
        this.toOrCC = toOrCC;
    }

    public Vector getPersonInfo() {
        return personInfo;
    }

    public void setPersonInfo(Vector personInfo) {
        this.personInfo = personInfo;
    }
     
    public String toString(){
        if(getRoleQualifierName() != null && getRoleQualifierName().length() > 0) {
            return getRoleName()+" <"+getRoleQualifierName()+">";
        } else {
            return getRoleName();
        }
    }
    
    public boolean equals(Object compareTo){
        if(!(compareTo instanceof RoleRecipientBean)){
            return false;
        }
        RoleRecipientBean bean = (RoleRecipientBean)compareTo;
        if(getRoleQualifier() != null) {
            return (getRoleId() == bean.getRoleId() && getRoleQualifier().equals(bean.getRoleQualifier()));
        }else{
            return getRoleId() == bean.getRoleId();
        }
    }
}
