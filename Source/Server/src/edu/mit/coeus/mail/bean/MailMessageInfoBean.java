/*
 * MailMessageInfoBean.java
 *
 * Created on July 10, 2009, 4:32 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.mail.bean;

import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

/**
 *
 * @author keerthyjayaraj
 */
public class MailMessageInfoBean implements Serializable{
    
    //message info
    private int notificationNumber;
    private String subject;
    private String message;
    private boolean active;
    private boolean promptUser;
    private boolean systemGenerated;
    //attachment info
    private boolean hasAttachment;
    private String filePath;
    private String attachmentName;
    //footer info   
    private String mailFooter;
    private String moduleFooter;
    private String url;
    //recipient info
    private Vector personRecipientList;
    private Vector roleRecipientList;
    private String coiUrl;

    private Timestamp updateTimestamp;

    public MailMessageInfoBean() {
        
    }
    public int getNotificationNumber() {
        return notificationNumber;
    }


    public String getMailFooter() {
        return mailFooter;
    }

    public String getMessage() {
        return message;
    }

    public String getModuleFooter() {
        return moduleFooter;
    }

    public Vector getPersonRecipientList() {
        return personRecipientList;
    }

    public Vector getRoleRecipientList() {
        return roleRecipientList;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isPromptUser() {
        return promptUser;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isSystemGenerated() {
        return systemGenerated;
    }
    
    public void setNotificationNumber(int notificationNumber) {
        this.notificationNumber = notificationNumber;
    }
    

    public void setMailFooter(String mailFooter) {
        this.mailFooter = mailFooter;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setModuleFooter(String moduleFooter) {
        this.moduleFooter = moduleFooter;
    }

    public void setPersonRecipientList(Vector personRecipientList) {
        this.personRecipientList = personRecipientList;
    }

    public void setPromptUser(boolean promptUser) {
        this.promptUser = promptUser;
    }

    public void setRoleRecipientList(Vector roleRecipientList) {
        this.roleRecipientList = roleRecipientList;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setSystemGenerated(boolean systemGenerated) {
        this.systemGenerated = systemGenerated;
    }   

    public String getAttachmentName() {
        return attachmentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return the coiUrl
     */
    public String getCoiUrl() {
        return coiUrl;
    }

    /**
     * @param coiUrl the coiUrl to set
     */
    public void setCoiUrl(String coiUrl) {
        this.coiUrl = coiUrl;
    }
    public void addRecipient(RoleRecipientBean bean){
        if (roleRecipientList == null){
            roleRecipientList = new Vector();
        }
        if(!roleRecipientList.contains(bean)){
             roleRecipientList.addElement(bean);
         }
    }

     public void addRecipient(PersonRecipientBean bean){
         if (personRecipientList == null){
            personRecipientList = new Vector();
        }
         if(!personRecipientList.contains(bean)){
             personRecipientList.addElement(bean);
         }
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
     
    public void appendMessage(String messagePart, String separator) {
        StringBuilder msg = new StringBuilder(message);
        if(message != null && !"".equals(message)){
            msg.append(separator);
            
        }
        if(messagePart != null){
            msg.append(messagePart);
        }
        this.message = msg.toString();
    }
    // COEUSQA-2105: No notification for some IRB actions - Start
    public void appendSubject(String subjectPart, String separator) {
        StringBuilder sub = new StringBuilder(subject);
        if(subject != null && !"".equals(subject)){
            sub.append(separator);
        }
        if(subjectPart != null){
            sub.append(subjectPart);
        }
        this.subject = sub.toString();
    }
    
    public void addAttachment(String attachmentFilePath){
        if(attachmentFilePath!=null && !"".equals(attachmentFilePath)){
            setHasAttachment(true);
            setFilePath(attachmentFilePath);
            String fileName = "";
            //Using System Dependent File.seperator
            //if(attachmentFilePath.lastIndexOf("\\") > 0){
            if(attachmentFilePath.lastIndexOf(File.separator) > 0){
                //attachmentFilePath = attachmentFilePath.substring(filePath.lastIndexOf("\\")+1,attachmentFilePath.length());
                attachmentFilePath = attachmentFilePath.substring(filePath.lastIndexOf(File.separator)+1,attachmentFilePath.length());
            }else{
                attachmentFilePath = "Attachment.pdf";
            }
            setAttachmentName(attachmentFilePath);
        }
    }

 


    // COEUSQA-2105: No notification for some IRB actions - End
}
