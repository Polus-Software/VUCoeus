package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.bean.BaseBean;

/*
 * MassChangeBean.java
 *
 * Created on January 18, 2005, 12:22 PM
 */

/**
 *
 * @author  chandrashekara
 */
public class PersonTypeBean implements BaseBean,java.io.Serializable{
    private String typeId;
    private String typeDescription;
    private String moduleId;
    private String replaceType;
    private boolean typeSelected;
    /** Creates a new instance of MassChangeBean */
    public PersonTypeBean() {
    }
    
    /** Getter for property typeId.
     * @return Value of property typeId.
     *
     */
    public java.lang.String getTypeId() {
        return typeId;
    }    
   
    /** Setter for property typeId.
     * @param typeId New value of property typeId.
     *
     */
    public void setTypeId(java.lang.String typeId) {
        this.typeId = typeId;
    }
    
    /** Getter for property typeDescription.
     * @return Value of property typeDescription.
     *
     */
    public java.lang.String getTypeDescription() {
        return typeDescription;
    }
    
    /** Setter for property typeDescription.
     * @param typeDescription New value of property typeDescription.
     *
     */
    public void setTypeDescription(java.lang.String typeDescription) {
        this.typeDescription = typeDescription;
    }
    
    /** Getter for property moduleId.
     * @return Value of property moduleId.
     *
     */
    public java.lang.String getModuleId() {
        return moduleId;
    }
    
    /** Setter for property moduleId.
     * @param moduleId New value of property moduleId.
     *
     */
    public void setModuleId(java.lang.String moduleId) {
        this.moduleId = moduleId;
    }
    
    /** Getter for property typeSelected.
     * @return Value of property typeSelected.
     *
     */
    public boolean isTypeSelected() {
        return typeSelected;
    }
    
    /** Setter for property typeSelected.
     * @param typeSelected New value of property typeSelected.
     *
     */
    public void setTypeSelected(boolean typeSelected) {
        this.typeSelected = typeSelected;
    }
    
    /**
     * Getter for property replaceType.
     * @return Value of property replaceType.
     */
    public java.lang.String getReplaceType() {
        return replaceType;
    }
    
    /**
     * Setter for property replaceType.
     * @param replaceType New value of property replaceType.
     */
    public void setReplaceType(java.lang.String replaceType) {
        this.replaceType = replaceType;
    }
    
}
