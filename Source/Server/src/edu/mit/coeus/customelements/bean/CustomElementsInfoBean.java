/*
 * @(#)PersonCustomElementsInfoBean.java 1.0 04/05/03 12:50 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

/* PMD check performed, and commented unused imports and variables on 31-MAY-2007
 * by Leena
 */
package edu.mit.coeus.customelements.bean;

import java.beans.*;
import java.sql.Timestamp;
import edu.mit.coeus.bean.BaseBean;
/**
 * The class used to hold the information of <code>Custom elements for a module </code>
 *
 * @author  Raghunath
 * @version 1.0
 * Created on April 05, 2003, 12:50 PM
 */
public class CustomElementsInfoBean implements BaseBean, java.io.Serializable{
    
    private static final String PROP_COLUMN_VALUE ="columnValue";
    //holds the column name
    private String columnName;
    //holds the column value
    private String columnValue;
    //holds the description
    private String description;
    //holds the column label
    private String columnLabel;
    //holds the data type
    private String dataType;
    //holds the data lenght
    private int dataLength;
    //holds the default value
    private String defaultValue;
    //holds the has look up
    private boolean hasLookUp;
    //holds the has look up
    private String lookUpReuired;
    //holds the isrequired
    private boolean isRequired;
    //holds look up window
    private String lookUpWindow;
    //holds the look up argument
    private String lookUpArgument;
    //holds update user id
    private String updateUser;
    //holds update timestamp
    private Timestamp updateTimestamp;
    //holds account type
    private String acType;
    // Enhancement to hold the group data
    private String groupCode;
    //Added for Coeus 4.3 enhancement: Data Override Nullable validation - start
    //holds whether the column is nullable
    private boolean nullable;
    //holds data precision
    private  int dataPrecision;
    //Added for Coeus 4.3 enhancement: Data Override Nullable validation - end
    
    private PropertyChangeSupport propertySupport;
    
    /** Creates a new instance of CustomElementsInfoBean */
    public CustomElementsInfoBean() {
        propertySupport = new PropertyChangeSupport( this );
    }
    
    public CustomElementsInfoBean(CustomElementsInfoBean customElementsInfoBean){
        
        this.columnLabel = customElementsInfoBean.getColumnLabel();
        this.columnName = customElementsInfoBean.getColumnName();
        this.columnValue = customElementsInfoBean.getColumnValue();
        this.dataType = customElementsInfoBean.getDataType();
        this.dataLength = customElementsInfoBean.getDataLength();
        this.defaultValue = customElementsInfoBean.getDefaultValue();
        this.hasLookUp = customElementsInfoBean.isHasLookUp();
        this.isRequired = customElementsInfoBean.isRequired();
        this.lookUpWindow = customElementsInfoBean.getLookUpWindow();
        this.lookUpArgument = customElementsInfoBean.getLookUpArgument();
        this.updateUser = customElementsInfoBean.getUpdateUser();
        this.updateTimestamp = customElementsInfoBean.getUpdateTimestamp();
        this.acType = customElementsInfoBean.getAcType();
        this.groupCode = customElementsInfoBean.getGroupCode();
        //Added for Coeus 4.3 enhancement : Data Override Nullable validation - start
        this.nullable = customElementsInfoBean.isNullable();
        this.dataPrecision = customElementsInfoBean.getDataPrecision();
        //Added for Coeus 4.3 enhancement : Data Override Nullable validation - end
        propertySupport = new PropertyChangeSupport( this );
    }
    
    /**
     * Method used to add propertychange listener to the fields
     * @param listener PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Method used to remove propertychange listener
     * @param listener PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnValue() {
        return columnValue;
    }
    
    public void setColumnValue(String newColumnValue) {
        String oldColumnValue = columnValue;
        this.columnValue = newColumnValue;
        propertySupport.firePropertyChange(PROP_COLUMN_VALUE,
                oldColumnValue, columnValue);
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getColumnLabel() {
        return columnLabel;
    }
    
    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    
    public int getDataLength() {
        return dataLength;
    }
    
    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public boolean isHasLookUp() {
        return hasLookUp;
    }
    
    public void setHasLookUp(boolean hasLookUp) {
        this.hasLookUp = hasLookUp;
    }
    
    public boolean isRequired() {
        return isRequired;
    }
    
    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    public String getLookUpWindow() {
        return lookUpWindow;
    }
    
    public void setLookUpWindow(String lookUpWindow) {
        this.lookUpWindow = lookUpWindow;
    }
    
    public String getLookUpArgument() {
        return lookUpArgument;
    }
    
    public void setLookUpArgument(String lookUpArgument) {
        this.lookUpArgument = lookUpArgument;
    }
    
    public String getUpdateUser() {
        return updateUser;
    }
    
    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
    
    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }
    
    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }
    public String getAcType() {
        return acType;
    }
    
    public void setAcType(String acType) {
        this.acType = acType;
    }
    
    /**
     * Getter for property lookUpReuired.
     * @return Value of property lookUpReuired.
     */
    public java.lang.String getLookUpReuired() {
        return lookUpReuired;
    }
    
    /**
     * Setter for property lookUpReuired.
     * @param lookUpReuired New value of property lookUpReuired.
     */
    public void setLookUpReuired(java.lang.String lookUpReuired) {
        this.lookUpReuired = lookUpReuired;
    }
    
    /**
     * Getter for property groupCode.
     * @return Value of property groupCode.
     */
    public java.lang.String getGroupCode() {
        return groupCode;
    }
    
    /**
     * Setter for property groupCode.
     * @param groupCode New value of property groupCode.
     */
    public void setGroupCode(java.lang.String groupCode) {
        this.groupCode = groupCode;
    }
    //Added for Coeus 4.3 enhancement : Data Override Nullable validation - start
    /**
     * Getter for property nullable.
     * @return Value of property nullable.
     */
    public boolean isNullable() {
        return nullable;
    }
    
    /**
     * Setter for property nullable.
     * @param nullable New value of property nullable.
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    /**
     * Getter for property dataPrecision.
     * @return Value of property dataPrecision.
     */
    public int getDataPrecision() {
        return dataPrecision;
    }
    
    /**
     * Setter for property dataPrecision.
     * @param dataPrecision New value of property dataPrecision.
     */
    public void setDataPrecision(int dataPrecision) {
        this.dataPrecision = dataPrecision;
    }
    //Added for Coeus 4.3 enhancement : Data Override Nullable validation - end
}
