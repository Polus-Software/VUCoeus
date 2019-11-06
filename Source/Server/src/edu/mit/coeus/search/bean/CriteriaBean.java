/*
 * @(#)CriteriaBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 8, 2002, 2:17 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;

import java.util.Vector;
/**
 *  The class used to represent the search criteria for a particular search.
 * The objects of this class will be created while parsing the XML file in
 * the class ProcessSearchXMLBean.
 * Each criteria bean represents one <code>CRITERIA</code> element in the XML.
 */
public class CriteriaBean implements java.io.Serializable{

    //holds name
    private String name;
    //holds alias
    private String alias;
    //holds data type
    private String dataType;
    //holds join
    private String join;
    //holds table
    private String table;
    //holds size
    private int size;
    //holds field bean
    private FieldBean fieldBean;

    /* CASE #748 Begin */
    //size of display for web application
    private int webSize;
    /* CASE #748 End */

/* CASE #748 Comment Begin */
    /** Creates new CriteriaBean */
    /*public CriteriaBean(String name,String alias,String dataType,
            String join,String table,String size) {
        this.name = name;
        this.alias = alias;
        this.join = join;
        this.dataType = dataType;
        this.table = table;
        //this.size = ((size==null||size.trim().equals(""))?"20":size);
        try{
            this.size = ((size==null||size.trim().equals(""))?20:Integer.parseInt(size));
        }catch(java.lang.NumberFormatException ex){
            this.size = 20;
        }
    }*/
    /* CASE #748 Comment End */

    /* CASE #748 Begin */
    //Add webSize attribute to constructor.
    /** Creates new CriteriaBean */
    public CriteriaBean(String name,String alias,String dataType,
            String join,String table,String size,String webSize) {
        this.name = name;
        this.alias = alias;
        this.join = join;
        this.dataType = dataType;
        this.table = table;
        //this.size = ((size==null||size.trim().equals(""))?"20":size);
        try{
            this.size = ((size==null||size.trim().equals(""))?20:Integer.parseInt(size));
        }catch(java.lang.NumberFormatException ex){
            this.size = 20;
        }
        try{
            this.webSize = ((webSize==null||webSize.trim().equals(""))?20:Integer.parseInt(webSize));
        }catch(java.lang.NumberFormatException ex){
            this.webSize = 20;
        }
    }
    /* CASE #748 End */

    /**
     *  The method used to get the status of the data type.
     *  @return true, if the datatype is number, else, false.
     */
    public boolean isNumber(){
        return (dataType!=null && dataType.trim().equalsIgnoreCase("number"));
    }
    /**
     *  The method used to get the data type.
     *  @return true, if the datatype is date, else, false.
     */
    public boolean isDate(){
        return (dataType!=null && dataType.trim().equalsIgnoreCase("date"));
    }
    /**
     *  The method used to get the name
     *  @return name
     */
    public String getName(){
        return name;
    }
    /**
     *  The method used to get the FieldBean object associated with this class
     *  @return FieldBean object
     */
    public FieldBean getFieldBean(){
        return fieldBean;
    }
    /**
     *  The method used to set the FieldBean instance to this class.
     *  @param FeildBean object
     */
    public void setFieldBean(FieldBean fieldBean){
        this.fieldBean = fieldBean;
    }

    /**
     *  The method used to get the alias.
     *  @return alias
     */
    public String getAlias(){
        return alias;
    }

    /**
     *  The method used to get the join
     *  @return join
     */
    public String getJoin(){
        return join;
    }

    /**
     *  The method used to get the table
     *  @return table
     */
    public String getTable(){
        return table;
    }

    /**
     *  The method used to get the control size
     *  @return control size
     */
    public int getSize(){
        return size;
    }

    /* CASE #748 Begin */
    /**
     * The method used to get the size of the input field for web application.
     * @return webSize
     */
    public int getWebSize(){
        return webSize;
    }
    /* CASE #748 End */

    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("CriteriaBean{");
        strBffr.append("Name=>"+name);
        strBffr.append("\n");
        strBffr.append("join=>"+join);
        strBffr.append("\n");
        strBffr.append("Datatype=>"+dataType);
        strBffr.append("\n");
        strBffr.append("table=>"+table);
        strBffr.append("}");
        return strBffr.toString();
    }
}