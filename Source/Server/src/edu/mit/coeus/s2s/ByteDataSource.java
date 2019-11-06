/*
 * @(#)ByteDataSource.java 1.0 12/07/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

/**
 *
 * @author  Geo Thomas
 */
public class ByteDataSource implements DataSource{
    private byte[] data;
    private String fileName;
    private String contentType="application/octet-stream";
    public ByteDataSource(byte[] data,String fileName,String contentType){
        this.data = data;
        this.fileName = fileName;
        this.contentType = contentType;
    }
    
    public java.io.InputStream getInputStream() throws java.io.IOException {
        InputStream is = new ByteArrayInputStream(data);
        return is;
    }
    
    public String getName() {
        return fileName;
    }
    
    public java.io.OutputStream getOutputStream() throws java.io.IOException {
        OutputStream os = new ByteArrayOutputStream(data.length);
        os.write(data);
        return os;
    }
    
    /**
     * Setter for property contentType.
     * @param contentType New value of property contentType.
     */
    public void setContentType(java.lang.String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * Getter for property contentType.
     * @return Value of property contentType.
     */
    public java.lang.String getContentType() {
        return contentType;
    }
    
}
