/*
 *This class has the implementation of all the getter- setter methods
 * of CoeusAttachment so that any bean using attachments can extend this.
 *
 * CoeusAttachmentBean.java
 *
 * Created on February 20, 2009, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.bean;

import java.io.Serializable;

/**
 *
 * @author keerthyjayaraj
 */
public class CoeusAttachmentBean implements CoeusAttachment,Serializable {
    
    private String fileName;
    private byte[] fileBytes;
    private String mimeType;
    /** Creates a new instance of CoeusAttachmentBean */
    public CoeusAttachmentBean() {
    }
    
    public CoeusAttachmentBean(String fileName , byte[] fileBytes){
        this.fileName  = fileName;
        this.fileBytes = fileBytes;
    }
     public String getMimeType() {
         return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

     public byte[] getFileBytes() {
        return fileBytes;
    }

    public String getFileName() {
        return fileName;
    }
}
