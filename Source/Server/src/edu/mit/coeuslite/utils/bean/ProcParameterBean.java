/*
 * ProcParameterBean.java
 *
 * Created on April 20, 2005, 10:49 AM
 */

package edu.mit.coeuslite.utils.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author  bijosht
 */
public class ProcParameterBean implements BaseBean,java.io.Serializable{
    private String procparameter;
    private String property;
    private String type;
    private String procType;
    
    /** Creates a new instance of ProcedureBean */
    public ProcParameterBean() {
    }
    
    /**
     * Getter for property procparameter.
     * @return Value of property procparameter.
     */
    public java.lang.String getProcparameter() {
        return procparameter;
    }
    
    /**
     * Setter for property procparameter.
     * @param procparameter New value of property procparameter.
     */
    public void setProcparameter(java.lang.String procparameter) {
        this.procparameter = procparameter;
    }
    
    /**
     * Getter for property property.
     * @return Value of property property.
     */
    public java.lang.String getProperty() {
        return property;
    }
    
    /**
     * Setter for property property.
     * @param property New value of property property.
     */
    public void setProperty(java.lang.String property) {
        this.property = property;
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
     * Getter for property procType.
     * @return Value of property procType.
     */
    public java.lang.String getProcType() {
        return procType;
    }
    
    /**
     * Setter for property procType.
     * @param procType New value of property procType.
     */
    public void setProcType(java.lang.String procType) {
        this.procType = procType;
    }
    
}

