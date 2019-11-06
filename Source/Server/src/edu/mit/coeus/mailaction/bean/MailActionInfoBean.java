/*
 * MailActionInfoBean.java
 *
 * Created on May 24, 2007, 3:23 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mailaction.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;
import java.io.Serializable;

/**
 *
 * @author talarianand
 */
public class MailActionInfoBean implements BaseBean, Serializable{
    
    //holds the information whether action is valid or not
    private boolean validAction;
    //holds prompt user
    private String promptUser;
    //holds actionId
    private String actionId;
    //holds moduleCode
    private String moduleCode;
    //holds roletype code
    private String roleCode;
    //holds roledescription
    private String roleDesc;
    //holds qualifier code;
    private String qualifier;
    //holds rolequalifier
    private String roleQualifier;
    //holds to/cc
    private String toCc;
    //holds ModuleItem key
    private String moduleItemKey;
    //holds modulesequence
    private String moduleSequence;
    //holds personId
    private String personId;
    //holds personName
    private String personName;
    //holds EmailId
    private String mailId;
    //holds personInfo
    private CoeusVector vecPersonData;
    //holds body of the mail
    private String message;
    //holds subject of the mail;
    private String subject;
    //holds whether the mail has attachment or not
    private boolean attachmentPresent;
    //holds the attachment filename
    private String fileName;
    //holds the full path of the attachment
    private String attachmentName;
    //holds the notification sequence number
    private int notificationNumber;
    //holds the status 
    private boolean sendNotification;
//    added with case 4350 - Mail Notification redesign
    //holds the footerInfo
    private String footerInfo;
    
    /**
     * Creates a new instance of MailActionInfoBean
     */
    public MailActionInfoBean() {
    }
    
    public MailActionInfoBean(MailActionInfoBean mailInfoBean) {
        this.notificationNumber = mailInfoBean.getNotificationNumber();
        this.actionId = mailInfoBean.getActionId();
        this.roleCode = mailInfoBean.getRoleCode();
        this.roleDesc = mailInfoBean.getRoleDescription();
        this.roleQualifier = mailInfoBean.getRoleQualifier();
        this.qualifier = mailInfoBean.getQualifier();
    }
    
    public boolean getValidAction() {
        return validAction;
    }
    
    public void setValidAction(boolean validAction) {
        this.validAction = validAction;
    }
    
    public String getPromptUser() {
        return promptUser;
    }
    
    public void setPromptUser(String promptUser) {
        this.promptUser = promptUser;
    }
    
    public String getActionId() {
        return actionId;
    }
    
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
    
    public String getModuleCode() {
        return moduleCode;
    }
    
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public String getRoleDescription() {
        return roleDesc;
    }
    
    public void setRoleDescription(String roleDesc) {
        this.roleDesc = roleDesc;
    }
    
    public String getQualifier() {
        return qualifier;
    }
    
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }
    
    public String getRoleQualifier() {
        return roleQualifier;
    }
    
    public void setRoleQualifier(String roleQualifier) {
        this.roleQualifier = roleQualifier;
    }
    
    public String getToCc() {
        return toCc;
    }
    
    public void setToCc(String toCc) {
        this.toCc = toCc;
    }
    
    public String getModuleItemKey() {
        return moduleItemKey;
    }
    
    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }
    
    public String getModuleSequence() {
        return moduleSequence;
    }
    
    public void setModuleSequence(String moduleSequence) {
        this.moduleSequence = moduleSequence;
    }
    
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getPersonName() {
        return personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    public String getEmailId() {
        return mailId;
    }
    
    public void setEmailId(String mailId) {
        this.mailId = mailId;
    }
    
    public CoeusVector getPersonData() {
        return vecPersonData;
    }
    
    public void setPersonData(CoeusVector vecPerson) {
        this.vecPersonData = vecPerson;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public boolean getHasAttachment() {
        return attachmentPresent;
    }
    
    public void setHasAttachment(boolean attachmentPresent) {
        this.attachmentPresent = attachmentPresent;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getAttachmentName() {
        return attachmentName;
    }
    
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
    
    public String toString(){
        if(getQualifier() != null && getQualifier().length() > 0) {
            return getRoleDescription()+" <"+getQualifier()+">";
        } else {
            return getRoleDescription();
        }
        //String mailId = getRoleDescription()+" <"+Utils.convertNull(getEmailId())+">";
        //return mailId;
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
//    added with case 4350 - Mail Notification redesign -getters and setters - Start
    public void setFooterInfo(String footerInfo) {
        this.footerInfo = footerInfo;
    }

    public String getFooterInfo() {
        return footerInfo;
    }
//added with case 4350 - Mail Notification redesign - End
}
