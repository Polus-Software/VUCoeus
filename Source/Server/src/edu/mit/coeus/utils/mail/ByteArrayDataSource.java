/*
 * @(#)ByteArrayDataSource.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 */
package edu.mit.coeus.utils.mail;
import java.io.*;
import javax.activation.*;

/**
 * This class implements a DataSource from:
 * 	an InputStream
 *	a byte array
 * 	a String
 *
 * @author Prasanna Kumar K
 * @version 1.0 July 24, 2003, 4:08 PM
 */

public class ByteArrayDataSource implements DataSource {
    private byte[] data;	// data
    private String type;	// content-type
    
    /* Create a DataSource from an input stream */
    public ByteArrayDataSource(InputStream is, String type) {
        this.type = type;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int ch;
            
            while ((ch = is.read()) != -1)
                // XXX - must be made more efficient by
                // doing buffered reads, rather than one byte reads
                os.write(ch);
            data = os.toByteArray();
            
        } catch (IOException ioex) { }
    }
    
    /* Create a DataSource from a byte array */
    public ByteArrayDataSource(byte[] data, String type) {
        this.data = data;
        this.type = type;
    }
    
    /* Create a DataSource from a String */
    public ByteArrayDataSource(String data, String type) {
        try {
            // Assumption that the string contains only ASCII
            // characters!  Otherwise just pass a charset into this
            // constructor and use it in getBytes()
            this.data = data.getBytes("iso-8859-1");
        } catch (UnsupportedEncodingException uex) { }
        this.type = type;
    }
    
    /**
     * Return an InputStream for the data.
     * Note - a new stream must be returned each time.
     */
    public InputStream getInputStream() throws IOException {
        if (data == null)
            throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }
    
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("cannot do this");
    }
    
    public String getContentType() {
        return type;
    }
    
    public String getName() {
        return "dummy";
    }
}
