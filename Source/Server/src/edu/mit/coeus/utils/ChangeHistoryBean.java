/*
 * ChangeHistoryBean.java
 *
 * Created on July 10, 2007, 4:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mit.coeus.utils;

/**
 * This class is used to set the changed statuses of properties
 *
 * @author leenababu
 */
public class ChangeHistoryBean implements java.io.Serializable{
    //holds the name of the property
    private String name ;
    //holds the value of the property
    private String value;
    //holds the status of the property and can have values 0-No change, 
    //1 - Modified, 2 - New, 3 - Deleted
    private int status;
    //holds the type of the property
    private String type;
    
    /** Creates a new instance of ChangeHistoryBean */
    public ChangeHistoryBean() {
    }
    
    /** Creates a new instance of ChangeHistoryBean with the arguments set into the member variables*/
    public ChangeHistoryBean(String name, String value, String type, int status){
        this.name = name ;
        this.value = value;
        this.type = type;
        this.status = status;
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
     * Getter for property value.
     * @return Value of property value.
     */
    public String getValue() {
        return value;
    }
    
     /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(String value) {
        this.value = value;
    }
    
     /**
     * Getter for property status.
     * @return Value of property status.
     */
    public int getStatus() {
        return status;
    }
    
     /**
     * Setter for property status.
     * @param status New value of property status.
     */
    public void setStatus(int status) {
        this.status = status;
    }
    
     /**
     * Getter for property type.
     * @return Value of property type.
     */
    public String getType() {
        return type;
    }
    
     /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(String type) {
        this.type = type;
    }
    
}
