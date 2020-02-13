/*
 * CoeusAttachment.java
 *
 * Created on February 20, 2009, 10:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.bean;

/**
 *
 * @author keerthyjayaraj
 */
public interface CoeusAttachment {

    public String getFileName();
    
    public void setFileName(String fileName);
    
    public String getMimeType();
    
    public void setMimeType(String mimeType);
    
    public byte[] getFileBytes();
    
    public void setFileBytes(byte[] fileData);
    
    

    
}
