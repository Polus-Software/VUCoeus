/*
 * ChangeHistoryGroup.java
 *
 * Created on July 10, 2007, 4:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

/**
 *
 * @author leenababu
 */
public class ChangeHistoryGroup implements java.io.Serializable{
    private String name;
    private CoeusVector historyBeanList = new CoeusVector();
    /** Creates a new instance of ChangeHistoryGroup */
    public ChangeHistoryGroup() { 
    }
    
    /**
     * Getter for property name.
     * @return Value of property name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Setter for property name.
     * @param name New value of property name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for property historyBeanList.
     * @return Value of property historyBeanList.
     */
    public CoeusVector getHistoryBeanList() {
        return historyBeanList;
    }
    
    /**
     * Setter for property historyBeanList.
     * @param historyBeanList New value of property historyBeanList.
     */
    public void setHistoryBeanList(CoeusVector historyBeanList) {
        this.historyBeanList = historyBeanList;
    }
    
}
