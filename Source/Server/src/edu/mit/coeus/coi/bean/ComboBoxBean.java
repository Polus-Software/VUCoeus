/*
 * @(#)ComboBoxBean.java 1.0 4/05/02
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.coi.bean;

import edu.mit.coeus.utils.UtilFactory;
import java.io.Serializable;
/**
 * The Class is used to populate any combo box
 *
 * @version 1.0 April 5, 2002, 7:14 PM
 * @author  Geo Thomas
 */
public class ComboBoxBean implements Serializable {
    private String code;
    private String description;
    /** Default Constructor */
    public ComboBoxBean() {
    }
    
    /** Constructor with parameters */
    public ComboBoxBean(String code, String description) {
        this.code = UtilFactory.checkNullStr(code);
        this.description = UtilFactory.checkNullStr(description);
    }
    
    
    /**
     *  This method gets the Code
     *  @return String code
     */
    public String getCode() {
        return code;
    }
    /**
     *  This method sets the Code
     *  @param String code
     */
    public void setCode(String code) {
        this.code = UtilFactory.checkNullStr(code);
    }
    /**
     *  This method gets the Description
     *  @return String description
     */
    public String getDescription() {
        return description;
    }
    /**
     *  This method sets the Description
     *  @param String description
     */
    public void setDescription(String description) {
        this.description = UtilFactory.checkNullStr(description);
    }
}