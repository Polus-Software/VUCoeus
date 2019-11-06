/*
 * @(#)ParameterBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on June 16, 2003, 4:48 PM
 */

package edu.mit.coeus.moduleparameters.bean;

/**
 * This class is the object representation of the parameter name and parameter value
 * with get / set methods, which will be used in ModuleParameterList.
 * @author  ravikanth
 */
public class ParameterBean {
    
    // holds the name of the parameter
    String paramName;
    // holds the value of the parameter
    String paramValue;
    
    /** Creates a new instance of ParameterBean */
    public ParameterBean() {
    }
    
    /**
     * Constructs the ParameterBean with given parameter name and value.
     * @param paramName String representing the parameter name.
     * @param paramValue String representing the parameter value.
     */
    public ParameterBean(String paramName, String paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }
    
    /** Getter for property paramName.
     * @return Value of property paramName.
     */
    public String getParamName() {
        return paramName;
    }    
    
    /** Setter for property paramName.
     * @param paramName New value of property paramName.
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    
    /** Getter for property paramValue.
     * @return Value of property paramValue.
     */
    public String getParamValue() {
        return paramValue;
    }
    
    /** Setter for property paramValue.
     * @param paramValue New value of property paramValue.
     */
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
    
    public String toString() {
        return paramName + ":"+paramValue;
    }
}
