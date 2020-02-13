/*
 * RuleVariableBean.java
 *
 * Created on October 18, 2005, 10:45 AM
 */

package edu.mit.coeus.mapsrules.bean;

/**
 *
 * @author  vinayks
 */
public class BusinessRuleVariableBean extends RuleBaseBean {
    private String variableName ;
    private String variableDescription ;
    private String dataType;
    private String columnName;
    
    /** Creates a new instance of RuleVariableBean */
    public BusinessRuleVariableBean() {
    }
    
    /**
     * Getter for property variableName.
     * @return Value of property variableName.
     */
    public java.lang.String getVariableName() {
        return variableName;
    }
    
    /**
     * Setter for property variableName.
     * @param variableName New value of property variableName.
     */
    public void setVariableName(java.lang.String variableName) {
        this.variableName = variableName;
    }
    
    /**
     * Getter for property variableDescription.
     * @return Value of property variableDescription.
     */
    public java.lang.String getVariableDescription() {
        return variableDescription;
    }
    
    /**
     * Setter for property variableDescription.
     * @param variableDescription New value of property variableDescription.
     */
    public void setVariableDescription(java.lang.String variableDescription) {
        this.variableDescription = variableDescription;
    }
    
    /**
     * Getter for property dataType.
     * @return Value of property dataType.
     */
    public java.lang.String getDataType() {
        return dataType;
    }
    
    /**
     * Setter for property dataType.
     * @param dataType New value of property dataType.
     */
    public void setDataType(java.lang.String dataType) {
        this.dataType = dataType;
    }
    
    /**
     * Getter for property columnName.
     * @return Value of property columnName.
     */
    public java.lang.String getColumnName() {
        return columnName;
    }
    
    /**
     * Setter for property columnName.
     * @param columnName New value of property columnName.
     */
    public void setColumnName(java.lang.String columnName) {
        this.columnName = columnName;
    }
    
}
