/*
 * CustomElementsUsageBean.java
 *
 * Created on December 13, 2004, 2:10 PM
 */

package edu.mit.coeus.customelements.bean;

/**
 *
 * @author  shivakumarmj
 */
public class CustomElementsUsageBean extends CustomElementsInfoBean{
    
    private int moduleCode;
    
    private boolean isRequired;
    
    /** Getter for property moduleCode.
     * @return Value of property moduleCode.
     *
     */
    public int getModuleCode() {
        return moduleCode;
    }    
    
    /** Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     *
     */
    public void setModuleCode(int moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /** Getter for property isRequired.
     * @return Value of property isRequired.
     *
     */
    public boolean isIsRequired() {
        return isRequired;
    }
    
    /** Setter for property isRequired.
     * @param isRequired New value of property isRequired.
     *
     */
    public void setIsRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }
    
}
