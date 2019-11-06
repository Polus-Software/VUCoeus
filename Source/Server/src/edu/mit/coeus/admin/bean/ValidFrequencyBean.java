/*
 * ValidFrequencyBean.java
 *
 * Created on November 26, 2004, 2:55 PM
 */

package edu.mit.coeus.admin.bean;

import edu.mit.coeus.bean.BaseBean;

/**
 *
 * @author  shivakumarmj
 */
public class ValidFrequencyBean implements java.io.Serializable, BaseBean {
    
    private String frequenctBaseCode;
    private String description="";
    
    /** Creates a new instance of ValidFrequencyBean */
    public ValidFrequencyBean() {
    }
    
    
    /** Getter for property frequenctBaseCode.
     * @return Value of property frequenctBaseCode.
     *
     */
    public java.lang.String getFrequenctBaseCode() {
        return frequenctBaseCode;
    }
    
    /** Setter for property frequenctBaseCode.
     * @param frequenctBaseCode New value of property frequenctBaseCode.
     *
     */
    public void setFrequenctBaseCode(java.lang.String frequenctBaseCode) {
        this.frequenctBaseCode = frequenctBaseCode;
    }    
      
    
    /** Getter for property description.
     * @return Value of property description.
     *
     */
    public java.lang.String getDescription() {
        return description;
    }
    
    /** Setter for property description.
     * @param description New value of property description.
     *
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }
    
    /**
     * @param args the command line arguments
     */    
    
    
    
    public boolean equals(Object obj) {
        ValidFrequencyBean validFrequencyBean = (ValidFrequencyBean)obj;
        if(validFrequencyBean.getFrequenctBaseCode().equals(getFrequenctBaseCode())){             
            return true;
        }else{
            return false;
        }
    }
    
}
