/*
 * @(#)ColumnBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 15, 2002, 3:33 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;

import java.util.Hashtable;
/**
 * This class is used to represent each column in the search criteria. 
 */
public class ColumnBean implements java.io.Serializable{
    
    //holds name of the column
    private String name;
    //holds attribute list of each column
    private Hashtable attList;
    
    /** Creates new ColumnBean */
    public ColumnBean(String name) {
        this.name = name;
        attList = new Hashtable();
    }

    /**
     *  The method used to get the name
     * @return name
     */
    String getName() {
        return name;
    }
    
    /**
     *  The method used to add attributes into attribute list
     * @param AttributeBean
     */
    public void addAttribute(AttributeBean attribute) {
        attList.put(attribute.getRowId(),attribute);
    }
    
    /**
     * The method used to the attribute list
     * @return attribute list
     */
    Hashtable getAttList() {
        return attList;
    }
    /**
     *  The method used to set the name
     *  @param name
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Column Name=>"+name);
        strBffr.append(";");
        strBffr.append("Attribute List=>"+attList.toString());
        return strBffr.toString();
    }
    
}
