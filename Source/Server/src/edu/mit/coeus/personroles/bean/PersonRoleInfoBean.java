/*
 * PersonRoleInfoBean.java
 *
 * Created on May 17, 2007, 3:56 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.personroles.bean;

import edu.mit.coeus.bean.BaseBean;
import java.sql.Timestamp;

/**
 *
 * @author talarianand
 */
public class PersonRoleInfoBean implements BaseBean, java.io.Serializable {
    
    //holds Action description
    private String actionName;
    //holds action code
    private String actionCode;
    //holds Role Name
    private String roleName;
    //holds Role Code
    private String roleCode;
    //holds the has qualifier
    private boolean hasQualifier;
    //holds the qualifier
    private String roleQualifier;
    //holds the To/Cc
    private char toCc;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds update user
    private String updateUser;
    //holds ac type
    private String acType;
    // holds qualifierCode
    private String qualifierCode;
    //holds selection information
    private boolean selected;
    //holds moduleCode
    private String moduleCode;
    //holds module description
    private String moduleDescription;
    //holds Submodule code
    private String subModuleCode;
    //holds Submodule description
    private String subModuleDescription;
    //holds notification Subject
    private String subject;
    //holdes notification message body
    private String messageBody;
    //holds prompt user 
    private boolean promptUser;
    //holds the notification sequence number
    private int notificationNumber;
    //holds the status 
    private boolean sendNotification;
    
    /**
     * Creates a new instance of PersonRoleInfoBean
     */
    public PersonRoleInfoBean() {
    }
    
    public PersonRoleInfoBean(PersonRoleInfoBean mailInfoBean) {
        this.actionName = mailInfoBean.getActionName();
        this.actionCode = mailInfoBean.getActionCode();
        this.roleName = mailInfoBean.getRoleName();
        this.roleCode = mailInfoBean.getRoleCode();
        this.hasQualifier = mailInfoBean.getHasQualifier();
        this.roleQualifier = mailInfoBean.getRoleQualifier();
        this.toCc = mailInfoBean.getToCc();
        this.updateTimestamp = mailInfoBean.getUpdateTimestamp();
        this.updateUser = mailInfoBean.getUpdateUser();
        this.acType = mailInfoBean.getAcType();
    }
    
    public String getActionName() {
        return actionName;
    }
    
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    
    public String getActionCode() {
        return actionCode;
    }
    
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public boolean getHasQualifier() {
        return hasQualifier;
    }
    
    public void setHasQualifier(boolean hasQualifier) {
        this.hasQualifier = hasQualifier;
    }
    
    public String getRoleQualifier() {
        return roleQualifier;
    }
    
    public void setRoleQualifier(String roleQualifier) {
        this.roleQualifier = roleQualifier;
    }
    
    public char getToCc() {
        return toCc;
    }
    
    public void setToCc(char toCc) {
        this.toCc = toCc;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    public String getQualifierCode() {
        return qualifierCode;
    }
    
    public void setQualifierCode(String qualifierCode) {
        this.qualifierCode = qualifierCode;
    }
    
    public boolean getSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public String getModuleCode() {
        return moduleCode;
    }
    
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public String getModuleDescription() {
        return moduleDescription;
    }
    
    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }
    
    public String getSubModuleCode() {
        return subModuleCode;
    }
    
    public void setSubModuleCode(String subModuleCode) {
        this.subModuleCode = subModuleCode;
    }
    
    public String getSubModuleDescription() {
        return subModuleDescription;
    }
    
    public void setSubModuleDescription(String subModuleDescription) {
        this.subModuleDescription = subModuleDescription;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessageBody() {
        return messageBody;
    }
    
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
    
    public boolean getPromptUser() {
        return promptUser;
    }
    
    public void setPromptUser(boolean promptUser) {
        this.promptUser = promptUser;
    }
    
    public int getNotificationNumber() {
        return notificationNumber;
    }
    
    public void setNotificationNumber(int notificationNumber) {
        this.notificationNumber = notificationNumber;
    }
    
    public boolean getSendNotification() {
        return sendNotification;
    }
    
    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }
    
}
