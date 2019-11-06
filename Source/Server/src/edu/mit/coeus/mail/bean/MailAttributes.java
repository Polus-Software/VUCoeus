/*
 * MailAttributes.java
 *
 * Created on May 28, 2007, 10:00 AM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */

package edu.mit.coeus.mail.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author talarianand
 */
public class MailAttributes implements Serializable {
    
//    //holds to information
//    private String[] strTo;
//    //holds cc information
//    private String[] strCc;
    //holds from information
    private String strFrom;
    //holds subject of mail
    private String strSub;
    //holds body of mail
    private String strMessage;
    //holds recipients information
    private List recipients;
    //holds whether attachment presents or not
    private boolean attachmentPresent = false;
    //holds file information
    private String fileName;
    //holds attachment information
    private String attachmentName;
    
    /** Creates a new instance of MailAttributes */
    public MailAttributes() {
    }
    
//    public String[] getTo() {
//        return strTo;
//    }
//
//    public void setTo(String[] strTo) {
//        this.strTo = strTo;
//    }
//
//    public String[] getCc() {
//        return strCc;
//    }
//
//    public void setCc(String[] cc) {
//        this.strCc = cc;
//    }
    
    public String getFrom() {
        return strFrom;
    }
    
    public void setFrom(String from) {
        this.strFrom = from;
    }
    
    public String getSubject() {
        return strSub;
    }
    
    public void setSubject(String subject) {
        this.strSub = subject;
    }
    
    public String getMessage() {
        return strMessage;
    }
    
    public void setMessage(String message) {
        this.strMessage = message;
    }
    
     
    /** Getter for property attachmentPresent.
     * @return Value of property attachmentPresent.
     */
    public boolean isAttachmentPresent() {
        return attachmentPresent;
    }
    
    /** Setter for property attachmentPresent.
     * @param attachmentPresent New value of property attachmentPresent.
     */
    public void setAttachmentPresent(boolean attachmentPresent) {
        this.attachmentPresent = attachmentPresent;
    }
    
    /** Getter for property fileName.
     * @return Value of property fileName.
     */
    public java.lang.String getFileName() {
        return fileName;
    }
    
    /** Setter for property fileName.
     * @param fileName New value of property fileName.
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = fileName;
    }
    
    /** Getter for property attachmentName.
     * @return Value of property attachmentName.
     */
    public java.lang.String getAttachmentName() {
        return attachmentName;
    }
    
    /** Setter for property attachmentName.
     * @param attachmentName New value of property attachmentName.
     */
    public void setAttachmentName(java.lang.String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public List getRecipients() {
        return recipients;
    }

    public void setRecipients(List recipients) {
        this.recipients = recipients;
    }
    
}
