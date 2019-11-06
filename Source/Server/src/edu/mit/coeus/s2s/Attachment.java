/*
 * @(#)AttachmentBean.java 1.0 11/17/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.s2s;

import java.io.Serializable;

/**
 *
 * @author  Geo Thomas
 */
public class Attachment implements Serializable{
    private String FileName;
    private String ContentId;
    private byte[] Content;
    private String ContentType;
    private String hashValue;
    
    /** Creates a new instance of AttachmentBean */
    public Attachment() {
    }
    
    /**
     * Getter for property FileName.
     * @return Value of property FileName.
     */
    public java.lang.String getFileName() {
        return FileName;
    }
    
    /**
     * Setter for property FileName.
     * @param FileName New value of property FileName.
     */
    public void setFileName(java.lang.String FileName) {
        this.FileName = FileName;
    }
    
    /**
     * Getter for property ContentId.
     * @return Value of property ContentId.
     */
    public java.lang.String getContentId() {
        return ContentId;
    }
    
    /**
     * Setter for property ContentId.
     * @param ContentId New value of property ContentId.
     */
    public void setContentId(java.lang.String ContentId) {
        this.ContentId = ContentId;
    }
    
    /**
     * Getter for property Content.
     * @return Value of property Content.
     */
    public byte[] getContent() {
        return Content;
    }
    
    /**
     * Setter for property Content.
     * @param Content New value of property Content.
     */
    public void setContent(byte[] Content) {
        this.Content = Content;
    }
    
    /**
     * Getter for property ContentType.
     * @return Value of property ContentType.
     */
    public java.lang.String getContentType() {
        return ContentType;
    }
    
    /**
     * Setter for property ContentType.
     * @param ContentType New value of property ContentType.
     */
    public void setContentType(java.lang.String ContentType) {
        this.ContentType = ContentType;
    }
    
    /**
     * Getter for property hashValue.
     * @return Value of property hashValue.
     */
    public java.lang.String getHashValue() {
        return hashValue;
    }
    
    /**
     * Setter for property hashValue.
     * @param hashValue New value of property hashValue.
     */
    public void setHashValue(java.lang.String hashValue) {
        this.hashValue = hashValue;
    }
    
}
