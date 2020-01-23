/*
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * XMLStreamInfoBean.java
 *
 * Created on September 7, 2004, 11:46 AM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.utils.CoeusVector;
import java.util.Vector;

/**
 *
 * @author  Geo Thomas
 */
public class XMLStreamInfoBean {
    
    private String moduleName;
    private Class streamGeneratorClass;
    private String methodName;
    private Object[] methodParams;
    private Class[] methodArgs;
    private char acType;
    
    /** Creates a new instance of XMLStreamInfoBean */
    public XMLStreamInfoBean() {
    }
    
    /**
     * Getter for property moduleName.
     * @return Value of property moduleName.
     */
    public java.lang.String getModuleName() {
        return moduleName;
    }
    
    /**
     * Setter for property moduleName.
     * @param moduleName New value of property moduleName.
     */
    public void setModuleName(java.lang.String moduleName) {
        this.moduleName = moduleName;
    }
    
    
    /**
     * Getter for property methodName.
     * @return Value of property methodName.
     */
    public java.lang.String getMethodName() {
        return methodName;
    }
    
    /**
     * Setter for property methodName.
     * @param methodName New value of property methodName.
     */
    public void setMethodName(java.lang.String methodName) {
        this.methodName = methodName;
    }
    
    /**
     * Getter for property streamGeneratorClass.
     * @return Value of property streamGeneratorClass.
     */
    public java.lang.Class getStreamGeneratorClass() {
        return streamGeneratorClass;
    }
    
    /**
     * Setter for property streamGeneratorClass.
     * @param streamGeneratorClass New value of property streamGeneratorClass.
     */
    public void setStreamGeneratorClass(java.lang.Class streamGeneratorClass) {
        this.streamGeneratorClass = streamGeneratorClass;
    }
    
    /**
     * Getter for property acType.
     * @return Value of property acType.
     */
    public char getAcType() {
        return acType;
    }
    
    /**
     * Setter for property acType.
     * @param acType New value of property acType.
     */
    public void setAcType(char acType) {
        this.acType = acType;
    }
    
    /**
     * Setter for property methodParams.
     * @param methodParams New value of property methodParams.
     */
    public void setMethodParams(java.lang.Object[] methodParams) {
        this.methodParams = methodParams;
    }
    
    /**
     * Setter for property methodArgs.
     * @param methodArgs New value of property methodArgs.
     */
    public void setMethodArgs(java.lang.Class[] methodArgs) {
        this.methodArgs = methodArgs;
    }
    
    /**
     * Getter for property methodParams.
     * @return Value of property methodParams.
     */
    public java.lang.Object[] getMethodParams() {
        return this.methodParams;
    }
    
    /**
     * Getter for property methodArgs.
     * @return Value of property methodArgs.
     */
    public java.lang.Class[] getMethodArgs() {
        return this.methodArgs;
    }
    
}
