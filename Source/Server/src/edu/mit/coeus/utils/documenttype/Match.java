/*
 * Match.java
 *
 * Created on November 16, 2006, 6:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils.documenttype;

import java.io.Serializable;

/**
 *
 * @author sharathk
 */
public class Match implements Serializable{
    
    private String identifier;
    private String value;
    
    /** Creates a new instance of Match */
    public Match() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
