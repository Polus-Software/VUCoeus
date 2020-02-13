/*
 * @(#)DisplayBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 12, 2002, 2:35 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;

/**
 *  The class used to represent the <code>Display</code> element in the XML file.
 */
public class DisplayBean implements java.io.Serializable{

    //holds name
    private String name;
    // holds value
    private String value;
    // holds cell size
    private int size = 120;
    // holds visible property
    private boolean visible = true;
    private String dataType;
    private String format;
   
    /** Creates new DisplayBean */
    //Modified for Case#2908 - Exports from Search Results Do Not Preserve Data Format  - Start
    //public DisplayBean(String name,String value,String size,String visible) {
    public DisplayBean(String name,String value,String size,String visible,String dataType,String format) {
        this.name = name;
        this.value = value;
        this.dataType = dataType;
        this.format = format;
        this.size = ((size==null||size.trim().equals(""))?120:Integer.parseInt(size));
        this.visible = (visible==null||visible.trim().equals(""))?true:
                            new Boolean(visible).booleanValue();
    }

    /**
     *  The method use to get the name
     *  @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     *  The method used to get the value
     *  @return value
     */
    public String getValue() {
        return value;
    }

    /**
     *  The method used to get the size
     *  @return size
     */
    public int getSize() {
        return size;
    }
    //Case#2908 - Exports from Search Results Do Not Preserve Data Format - Start
    /**
     *  Mthod to get the dataType of column
     *  @return dataType
     */
    public String getDataType() {
        return dataType;
    }
    /**
     *  Method to get date format of column
     *  @return format
     */
    public String getFormat() {
        return format;
    }
    //Case#2908 - End
    
    /**
     *  The method used to get the boolean
     *  @return boolean
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Name=>"+name);
        strBffr.append(";");
        strBffr.append("Value=>"+value);
        strBffr.append(";");
        strBffr.append("Size=>"+size);
        //Case#2908 - Exports from Search Results Do Not Preserve Data Format - Start
        strBffr.append(";");
        strBffr.append("DataType=>"+dataType);
        strBffr.append(";");
        strBffr.append("Format=>"+format);
        //Case#2908 - End
        
        return strBffr.toString();
    }
}
