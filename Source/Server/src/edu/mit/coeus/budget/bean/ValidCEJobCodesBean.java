/*
 * ValidCEJobCodesBean.java
 *
 * Created on November 18, 2004, 9:57 AM
 */

package edu.mit.coeus.budget.bean;

/**
 *
 * @author  shivakumarmj
 */
public class ValidCEJobCodesBean extends CostElementsBean implements java.io.Serializable{
    
    private String jobCode = null;
    
    private String jobTitle = null;
    
    /** Creates a new instance of ValidCEJobCodesBean */
    public ValidCEJobCodesBean() {
    }
    
    /** Getter for property jobCode.
     * @return Value of property jobCode.
     *
     */
    public java.lang.String getJobCode() {
        return jobCode;
    }
    
    /** Setter for property jobCode.
     * @param jobCode New value of property jobCode.
     *
     */
    public void setJobCode(java.lang.String jobCode) {
        this.jobCode = jobCode;
    }
    
    /** Getter for property jobTitle.
     * @return Value of property jobTitle.
     *
     */
    public java.lang.String getJobTitle() {
        return jobTitle;
    }
    
    /** Setter for property jobTitle.
     * @param jobTitle New value of property jobTitle.
     *
     */
    public void setJobTitle(java.lang.String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
}
