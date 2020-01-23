/*
 * SubContractAttachmentBean.java
 *
 * Created on January 3, 2012, 10:36 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.subcontract.bean;

import edu.mit.coeus.bean.CoeusAttachment;

/**
 *
 * @author manjunathabn
 */
public class SubContractAttachmentBean extends SubContractBaseBean implements CoeusAttachment {
    
    private String subContractId;
    private String fileName;
    private byte[] document;
    private String acType;
    private String mimeType;
    private String attachmentTypeDescription;
    private String description;
    private int attachmentTypeCode;
    private int attachmentId;
    private String attachmentType;
    
        
    /** Creates a new instance of SubContractAttachmentBean */
    public SubContractAttachmentBean() {
    }
    
     public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public void setFileBytes(byte[] fileBytes) {
        setDocument(fileBytes);
    }
    
    public byte[] getFileBytes() {
        return getDocument();
    }
    
    public byte[] getDocument() {
        return document;
    }
    
    public void setDocument(byte[] document) {
        this.document = document;
    }
    
     public String getAttachmentTypeDescription() {
        return attachmentTypeDescription;
    }

    public void setAttachmentTypeDescription(String attachmentTypeDescription) {
        this.attachmentTypeDescription = attachmentTypeDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAttachmentTypeCode() {
        return attachmentTypeCode;
    }

    public void setAttachmentTypeCode(int attachmentTypeCode) {
        this.attachmentTypeCode = attachmentTypeCode;
    }

    public String getSubContractId() {
        return subContractId;
    }

    public void setSubContractId(String subContractId) {
        this.subContractId = subContractId;
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
    
    
}
