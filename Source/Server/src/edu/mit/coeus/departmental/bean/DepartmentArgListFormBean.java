/* 
 * @(#)DepartmentArgListFormBean.java 1.0 03/15/03 4:49 PM
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */

package edu.mit.coeus.departmental.bean;

import java.beans.*;

/**
 * The class used to hold the information of <code>Department ArgumentList</code>
 *
 * @author  Mukundan.C
 * @version 1.0
 * Created on March 15, 2003, 4:49 PM
 */

public class DepartmentArgListFormBean implements java.io.Serializable {
    
    //holds the argument name
    private String argumentName;
    //holds the argument value
    private String value;
    //holds the description
    private String description;
    
    public String getArgumentName() {
        return argumentName;
    }

    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
