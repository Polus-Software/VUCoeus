/*
 * StatementResult.java
 *
 * Created on May 6, 2005, 11:30 AM
 */

package edu.mit.coeuslite.utils.statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 * @author  sharathk
 */
public class Result {
    
    private String type;
    private List statementMapList;
    
    /** Creates a new instance of StatementResult */
    public Result() {
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
     * Getter for property statementMapList.
     * @return Value of property statementMapList.
     */
    public java.util.List getStatementMapList() {
        if(statementMapList == null) {
            statementMapList = new ArrayList();
        }
        return statementMapList;
    }
    
    /**
     * Setter for property statementMapList.
     * @param statementMapList New value of property statementMapList.
     */
    public void setStatementMapList(java.util.List statementMapList) {
        this.statementMapList = statementMapList;
    }
    
}
