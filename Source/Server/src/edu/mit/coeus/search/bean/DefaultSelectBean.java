/*
 * DefaultSelectBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on May 24, 2004, 4:15 PM
 */

package edu.mit.coeus.search.bean;

/**
 *
 * @author  ravikanth
 */
public class DefaultSelectBean implements java.io.Serializable{
    private String id;
    private String name;
    
    /** Creates a new instance of DefaultSelectBean */
    public DefaultSelectBean() {
    }
    
    /**
     * Getter for property id.
     * @return Value of property id.
     */
    public java.lang.String getId() {
        return id;
    }
    
    /**
     * Setter for property id.
     * @param id New value of property id.
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public java.lang.String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
}
