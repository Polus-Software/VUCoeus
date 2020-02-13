/*
 * CoeusDocument.java
 *
 * Created on June 26, 2006, 3:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.document;

import java.io.Serializable;

/**
 *
 * @author sharathk
 */
public class CoeusDocument implements Serializable{
    
    private String mimeType;
    private byte[] documentData;
    private String documentName;
    
    /** Creates a new instance of CoeusDocument */
    public CoeusDocument() {
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public String getDocumentName() {
        if(documentName == null || documentName.length() == 0) {
            documentName = "CoeusDocument";
        }
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    
}
