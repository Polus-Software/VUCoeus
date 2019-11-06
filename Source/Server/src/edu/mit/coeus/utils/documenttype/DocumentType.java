/*
 * DocumentType.java
 *
 * Created on November 16, 2006, 6:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sharathk
 */
public class DocumentType implements Serializable {
    
    private String type;
    private String mimeType;
    private String description;
    private List matchList;
    
    /**
     * Creates a new instance of DocumentType
     */
    public DocumentType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List getMatchList() {
        return matchList;
    }

    public void setMatchList(List matchList) {
        this.matchList = matchList;
    }

    public String toString() {
        String retValue;
        retValue = description;
        if(retValue == null) {
            retValue = super.toString();
        }
        return description;
    }
    
    
}
