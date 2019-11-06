/*
 * @(#)RolodexReferencesBean.java 1.0 04/03/04
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */



package edu.mit.coeus.rolodexmaint.bean;


import java.io.Serializable;
import edu.mit.coeus.bean.BaseBean;

/**
 * The class is the used to hold data of Rolodex References
 *
 * @author  Prasanna Kumar
 * @version 1.0
 * Created on March 04, 2004, 6:15 PM
 */

public class RolodexReferencesBean implements Serializable,BaseBean{

    private String tableName;
    private String columnName;
    private int count;
    private String currentValue;
    private String newValue;
    private String columnType;

    /** Creates new RolodexDetailsFormBean */
    public RolodexReferencesBean() {

    }

    /** Getter for property tableName.
     * @return Value of property tableName.
     */
    public java.lang.String getTableName() {
        return tableName;
    }
    
    /** Setter for property tableName.
     * @param tableName New value of property tableName.
     */
    public void setTableName(java.lang.String tableName) {
        this.tableName = tableName;
    }
    
    /** Getter for property columnName.
     * @return Value of property columnName.
     */
    public java.lang.String getColumnName() {
        return columnName;
    }
    
    /** Setter for property columnName.
     * @param columnName New value of property columnName.
     */
    public void setColumnName(java.lang.String columnName) {
        this.columnName = columnName;
    }
    
    /** Getter for property count.
     * @return Value of property count.
     */
    public int getCount() {
        return count;
    }
    
    /** Setter for property count.
     * @param count New value of property count.
     */
    public void setCount(int count) {
        this.count = count;
    }    
    
    /** Getter for property currentValue.
     * @return Value of property currentValue.
     */
    public java.lang.String getCurrentValue() {
        return currentValue;
    }
    
    /** Setter for property currentValue.
     * @param currentValue New value of property currentValue.
     */
    public void setCurrentValue(java.lang.String currentValue) {
        this.currentValue = currentValue;
    }
    
    /** Getter for property newValue.
     * @return Value of property newValue.
     */
    public java.lang.String getNewValue() {
        return newValue;
    }
    
    /** Setter for property newValue.
     * @param newValue New value of property newValue.
     */
    public void setNewValue(java.lang.String newValue) {
        this.newValue = newValue;
    }
    
    /** Getter for property columnType.
     * @return Value of property columnType.
     */
    public java.lang.String getColumnType() {
        return columnType;
    }
    
    /** Setter for property columnType.
     * @param columnType New value of property columnType.
     */
    public void setColumnType(java.lang.String columnType) {
        this.columnType = columnType;
    }    
}