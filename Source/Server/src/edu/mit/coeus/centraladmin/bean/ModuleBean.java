/*
 * ModuleBean.java
 *
 * Created on January 18, 2005, 4:30 PM
 */

package edu.mit.coeus.centraladmin.bean;

import edu.mit.coeus.bean.BaseBean;
import edu.mit.coeus.utils.CoeusVector;

/**
 *
 * @author  chandrashekara
 */
public class ModuleBean implements BaseBean,java.io.Serializable{
    private String moduleCode;
    private String moduleName;
    private CoeusVector personTypeData;
    private boolean moduleSelected;
    /** Creates a new instance of ModuleBean */
    public ModuleBean() {
    }
    
    /** Getter for property moduleCode.
     * @return Value of property moduleCode.
     *
     */
    public java.lang.String getModuleCode() {
        return moduleCode;
    }
    
    /** Setter for property moduleCode.
     * @param moduleCode New value of property moduleCode.
     *
     */
    public void setModuleCode(java.lang.String moduleCode) {
        this.moduleCode = moduleCode;
    }
    
    /** Getter for property moduleName.
     * @return Value of property moduleName.
     *
     */
    public java.lang.String getModuleName() {
        return moduleName;
    }
    
    /** Setter for property moduleName.
     * @param moduleName New value of property moduleName.
     *
     */
    public void setModuleName(java.lang.String moduleName) {
        this.moduleName = moduleName;
    }
    
    /** Getter for property personTypeData.
     * @return Value of property personTypeData.
     *
     */
    public edu.mit.coeus.utils.CoeusVector getPersonTypeData() {
        return personTypeData;
    }
    
    /** Setter for property personTypeData.
     * @param personTypeData New value of property personTypeData.
     *
     */
    public void setPersonTypeData(edu.mit.coeus.utils.CoeusVector personTypeData) {
        this.personTypeData = personTypeData;
    }
    
    /** Getter for property moduleSelected.
     * @return Value of property moduleSelected.
     *
     */
    public boolean isModuleSelected() {
        return moduleSelected;
    }
    
    /** Setter for property moduleSelected.
     * @param moduleSelected New value of property moduleSelected.
     *
     */
    public void setModuleSelected(boolean moduleSelected) {
        this.moduleSelected = moduleSelected;
    }
    
}
