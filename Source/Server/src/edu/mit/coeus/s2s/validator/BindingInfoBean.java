/*
 * BindingInfoBean.java
 *
 * Created on December 29, 2004, 2:29 PM
 */

package edu.mit.coeus.s2s.validator;

/**
 *
 * @author  geot
 */
public class BindingInfoBean {
    private String nameSpace;
    private String className;
//    private String methName;
//    private String argType;
    private String formName;
    private String templateName;
    private String jaxbPkgName;
    private String cgdNameSpace;
    private boolean nsChanged;
    private String sortIndex="zzzzzzz";//initialize for sorting purpose
    private boolean authzCheck;
    /** Creates a new instance of BindingInfoBean */
    public BindingInfoBean() {
    }
    
    /**
     * Getter for property className.
     * @return Value of property className.
     */
    public java.lang.String getClassName() {
        return className;
    }
    
    /**
     * Setter for property className.
     * @param className New value of property className.
     */
    public void setClassName(java.lang.String className) {
        this.className = className;
    }
    
//    /**
//     * Getter for property methName.
//     * @return Value of property methName.
//     */
//    public java.lang.String getMethName() {
//        return methName;
//    }
//    
//    /**
//     * Setter for property methName.
//     * @param methName New value of property methName.
//     */
//    public void setMethName(java.lang.String methName) {
//        this.methName = methName;
//    }
//    
    public String toString(){
        return "\nNamespace=>"+nameSpace+"\nClassName=>"+className;
    }
    
    /**
     * Getter for property nameSpace.
     * @return Value of property nameSpace.
     */
    public java.lang.String getNameSpace() {
        return nameSpace;
    }
    
    /**
     * Setter for property nameSpace.
     * @param nameSpace New value of property nameSpace.
     */
    public void setNameSpace(java.lang.String nameSpace) {
        this.nameSpace = nameSpace;
    }
    
//    /**
//     * Getter for property argType.
//     * @return Value of property argType.
//     */
//    public java.lang.String getArgType() {
//        return argType;
//    }
//    
//    /**
//     * Setter for property argType.
//     * @param argType New value of property argType.
//     */
//    public void setArgType(java.lang.String argType) {
//        this.argType = argType;
//    }
    
    /**
     * Getter for property formName.
     * @return Value of property formName.
     */
    public java.lang.String getFormName() {
        return formName;
    }
    
    /**
     * Setter for property formName.
     * @param formName New value of property formName.
     */
    public void setFormName(java.lang.String formName) {
        this.formName = formName;
    }
    
    /**
     * Getter for property jaxbPkgName.
     * @return Value of property jaxbPkgName.
     */
    public java.lang.String getJaxbPkgName() {
        return jaxbPkgName;
    }
    
    /**
     * Setter for property jaxbPkgName.
     * @param jaxbPkgName New value of property jaxbPkgName.
     */
    public void setJaxbPkgName(java.lang.String jaxbPkgName) {
        this.jaxbPkgName = jaxbPkgName;
    }
    
    /**
     * Getter for property templateName.
     * @return Value of property templateName.
     */
    public java.lang.String getTemplateName() {
        return templateName;
    }
    
    /**
     * Setter for property templateName.
     * @param templateName New value of property templateName.
     */
    public void setTemplateName(java.lang.String templateName) {
        this.templateName = templateName;
    }
    
    /**
     * Getter for property cgdNameSpace.
     * @return Value of property cgdNameSpace.
     */
    public java.lang.String getCgdNameSpace() {
        return cgdNameSpace;
    }
    
    /**
     * Setter for property cgdNameSpace.
     * @param cgdNameSpace New value of property cgdNameSpace.
     */
    public void setCgdNameSpace(java.lang.String cgdNameSpace) {
        if(nameSpace!=null && cgdNameSpace!=null &&
                !cgdNameSpace.equals("")&&
                !nameSpace.equalsIgnoreCase(cgdNameSpace)){
            setNsChanged(true);
        }
        this.cgdNameSpace = cgdNameSpace;
    }
    
    /**
     * Getter for property nsChanged.
     * @return Value of property nsChanged.
     */
    public boolean isNsChanged() {
        return nsChanged;
    }
    
    /**
     * Setter for property nsChanged.
     * @param nsChanged New value of property nsChanged.
     */
    public void setNsChanged(boolean nsChanged) {
        this.nsChanged = nsChanged;
    }
    
    /**
     * Getter for property sortIndex.
     * @return Value of property sortIndex.
     */
    public String getSortIndex() {
        return sortIndex;
    }
    
    /**
     * Setter for property sortIndex.
     * @param sortIndex New value of property sortIndex.
     */
    public void setSortIndex(String sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean hasAuthzCheck() {
        return authzCheck;
    }

    public void setAuthzCheck(boolean authzCheck) {
        this.authzCheck = authzCheck;
    }
    
}
