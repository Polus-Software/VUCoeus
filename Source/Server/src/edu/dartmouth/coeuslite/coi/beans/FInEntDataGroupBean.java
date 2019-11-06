/*
 * FInEntDataGroupBean.java
 *
 * Created on May 12, 2008, 12:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.dartmouth.coeuslite.coi.beans;

/**
 *
 * @author blessy
 */
public class FInEntDataGroupBean {
    
    /** Creates a new instance of FInEntDataGroupBean */
    public FInEntDataGroupBean() {
    }
    private int code;
    private String description;
    private int sortId;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
}
