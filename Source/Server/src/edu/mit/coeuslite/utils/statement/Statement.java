/*
 * Statement.java
 *
 * Created on April 21, 2005, 12:38 PM
 */

package edu.mit.coeuslite.utils.statement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author  sharathk
 */
public class Statement {
    
    private String type;
    private String id;
    private String name;
    private String value;    
    private List statementMapList;
    private List statementOutputList;
    private Result result;
    
    /** Creates a new instance of Statement */
    public Statement() {
    }
    
    /**
     * Getter for property statementMapList.
     * @return Value of property statementMapList.
     */
    public List getStatementMapList() {
        if(statementMapList == null) {
            statementMapList = new ArrayList();
        }
        return statementMapList;
    }
    
    /**
     * Setter for property statementMapList.
     * @param statementMapList New value of property statementMapList.
     */
    public void setStatementMapList(List statementMapList) {
        this.statementMapList = statementMapList;
    }
    
    /**
     * Getter for property type.
     * @return Value of property type.
     */
    public java.lang.String getType() {
        return type;
    }
    
    /**
     * Setter for property type.
     * @param type New value of property type.
     */
    public void setType(java.lang.String type) {
        this.type = type;
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
    
    /**
     * Getter for property value.
     * @return Value of property value.
     */
    public java.lang.String getValue() {
        return value;
    }
    
    /**
     * Setter for property value.
     * @param value New value of property value.
     */
    public void setValue(java.lang.String value) {
        this.value = value;
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
     * Getter for property statementOutputList.
     * @return Value of property statementOutputList.
     */
    public java.util.List getStatementOutputList() {
        if(statementOutputList == null) {
            statementOutputList = new ArrayList();
        }
        return statementOutputList;
    }
    
    /**
     * Setter for property statementOutputList.
     * @param statementOutputList New value of property statementOutputList.
     */
    public void setStatementOutputList(java.util.List statementOutputList) {
        this.statementOutputList = statementOutputList;
    }
    
    /**
     * Getter for property result.
     * @return Value of property result.
     */
    public edu.mit.coeuslite.utils.statement.Result getResult() {
        return result;
    }
    
    /**
     * Setter for property result.
     * @param result New value of property result.
     */
    public void setResult(edu.mit.coeuslite.utils.statement.Result result) {
        this.result = result;
    }
    
}
