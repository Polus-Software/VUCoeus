/*
 * @(#)FieldBean.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 *
 * Created on July 1, 2002, 12:14 PM
 * @author  Geo Thomas
 * @version 1.0
 */

package edu.mit.coeus.search.bean;

import java.util.Vector;
import java.util.LinkedList;
import java.util.Hashtable;

import edu.mit.coeus.utils.ComboBoxBean;

/**
 *  The class used to represent <code>COLUMN</code> element in the XML file
 */
public class FieldBean implements java.io.Serializable{

    //holds name
    private String name;
    //holds label
    private String label;
    //holds type
    private String type;
    //holds list of ComboBox instances
    private Vector comboList;
    //holds procedure name
    private String procedure;
    //holds label of the code, if the Field element is a combobox
    private String codeLabel;
    //holds label of the description, if the Field element is a combobox
    private String descLabel;
    //holds parameter for selecting the code from the resultset
    private String codeSltParam;
    //holds parameter for selecting the description from the resultset
    private String descSltParam;
    
    /** Creates new FieldBean */
    public FieldBean() {
        //dbEngine = new DBEngineImpl();
    }

    /**
     *  The method used to get the status of the type of the field.
     *  It retruns true if the field is a list box, else, returns false
     *  @return field type status
     */
    public boolean isListBox(){
        return (type!=null && type.trim().equalsIgnoreCase("list"));
    }   
    
    /**
     *  The method used to get the name
     *  @return name
     */
    public String getName() {
        return name;
    }
    
    /**
     *  The method used to set the name
     *  @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     *  The method used to get the label
     *  @return label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     *  The method used to set the label
     *  @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     *  The method used to get the type
     *  @return type
     */
    public String getType() {
        return type;
    }
    
    /**
     *  The method used to set the type
     *  @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * Method used to set the combolist
     */
    public void setComboList(Vector comboList){
        this.comboList = comboList;
    }
    /**
     *  The method used to get the combobox list
     *  @return combobox list
     */
    public Vector getComboList(){
        return comboList;
    }

    /**
     *  The method used to get the procedure name
     *  @return procedure name
     */
    public String getProcedure() {
        return procedure;
    }
    
    /**
     * The method used to set the procedure name
     * @param procedure name
     */
    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }
    
    /**
     *  The method used to get the label to be displayed for the code in the listbox
     *  @return code label
     */
    public String getCodeLabel() {
        return codeLabel;
    }
    
    /**
     *  The method used to set the label to be displayed for the code in the listbox
     *  @param code label
     */
    public void setCodeLabel(String codeLabel) {
        this.codeLabel = codeLabel;
    }
    
    /**
     *  The method used to get the label to be displayed for the description 
     *  in the listbox
     *  @return description label
     */
    public String getDescLabel() {
        return descLabel;
    }
    
    /**
     *  The method used to set the label to be displayed for the description 
     *  in the listbox
     *  @param description label
     */
    public void setDescLabel(String descLabel) {
        this.descLabel = descLabel;
    }

    /**
     *  The method used to get the parameter for fetching the code from the resulset
     *  @return select parameter for code
     */
    public String getCodeSltParam() {
        return codeSltParam;
    }
    
    /**
     *  The method used to set the parameter for fetching the code from the resultset
     *  @param select parameter for code
     */
    public void setCodeSltParam(String codeSltParam) {
        this.codeSltParam = codeSltParam;
    }
    
    /**
     *  The method used to get the parameter for fetching the description from the resulset
     *  @return select parameter for description
     */
    public String getDescSltParam() {
        return descSltParam;
    }
    
    /**
     *  The method used to set the parameter for fetching the description from the resulset
     *  @param select parameter for description
     */
    public void setDescSltParam(String descSltParam) {
        this.descSltParam = descSltParam;
    }

    /*public boolean validate() {
        return true;
    }*/
    
    /**
     * Overridden method of toString. It will form a string representation of
     * each element associated with this class.
     * @return Concatinated string representation of each element 
     */
    public String toString(){
        StringBuffer strBffr = new StringBuffer("");
        strBffr.append("Name=>"+name);
        strBffr.append(";");
        strBffr.append("Label=>"+label);
        strBffr.append(";");
        strBffr.append("Type=>"+type);
        strBffr.append(";");
        strBffr.append("Procedure name=>"+procedure);
        strBffr.append(";");
        strBffr.append("Code Label=>"+codeLabel);
        strBffr.append(";");
        strBffr.append("Description Label=>"+descLabel);
        strBffr.append(";");
        strBffr.append("Code select param=>"+codeSltParam);
        strBffr.append(";");
        strBffr.append("Description select param=>"+descSltParam);
        if(isListBox()){
            strBffr.append("Combo list=>"+comboList.toString());
        }
        return strBffr.toString();
    }
}