/*
 * @(#)Parameter.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 11, 2002, 5:51 PM
 * @author  Geo Thomas
 * @version 1.0
 */
package edu.mit.coeus.search.bean;

/**
 *  The class used to represent <code>PARAM</code> element in the 
 *  <code>PROCEDURE</code> element.
 */
public class ParameterBean implements java.io.Serializable{

    //holds direction of the parameter
    private String direction;
    //holds name
    private String name;
    //holds datatype
    private String dataType;
    
    /** Creates new Parameter */
    public ParameterBean(String name, String dataType, String direction) {
        this.direction = direction;
        this.name = name;
        this.dataType = dataType;
    }

    /**
     *  The method used to get the name of the parameter
     *  @return name
     */
    String getName() {
        return name;
    }
    
    /**
     *  The method used to get the status of the direction of the parameter
     *  @return true, if the direction is <code>out</code>
     */
    boolean isOutParam() {
        return (direction.trim().equalsIgnoreCase("out"));
    }
    
    /*
     * The method used to get the datatype
     * @return datatype
     */
    String getDataType() {
        return dataType;
    }
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Param Name=>"+name);
        strBffr.append(";");
        strBffr.append("datatype=>"+dataType);
        strBffr.append(";");
        strBffr.append("direction=>"+direction);
        return strBffr.toString();
    }
}
