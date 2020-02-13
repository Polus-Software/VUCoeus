/*
 * @(#)SetMailAttributes.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.mail;

/**
 * This class is used to get/set mail attributes
 *
 * @author Prasanna Kumar K
 * @version 1.0 July 24, 2003, 4:08 PM
 */

public class SetMailAttributes{
    
    private  String to ;
    private String  cc ;
    private  String bcc;
    private String  from;
    private  String message;
    private String  subject;
    private String  send="y";
    private String  reply;
    private String  delete;
    private String  list="y";
    private boolean attachmentPresent = false;
    private String fileName ;
    private String attachmentName ;
    
    public SetMailAttributes(){
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getTo(){
        return this.to;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getFrom(){
        return this.from;
    }
    public void setCC(String cc) {
        this.cc = cc;
    }
    public String getCC(){
        return this.cc;
    }
    
    public void setBCC(String bcc) {
        this.bcc = bcc;
    }
    public String getBCC(){
        return this.bcc;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getSubject(){
        return this.subject;
    }
    public String getMessage(){
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setSend(String send){
        this.send=send;
    }
    public String getSend(){
        return this.send;
    }
    public void listMails(String list){
        this.list=list;
    }
    public String getListMails(){
        return this.list;
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
    
}//class ends