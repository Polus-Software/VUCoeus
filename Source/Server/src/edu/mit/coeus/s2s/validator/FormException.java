/*
 * MandatoryFormNotFoundException.java
 *
 * Created on December 27, 2004, 11:47 AM
 */

package edu.mit.coeus.s2s.validator;

import edu.mit.coeus.s2s.bean.FormInfoBean;

/**
 *
 * @author  geot
 */
public class FormException extends S2SValidationException {
    private FormInfoBean formInfo;
    /**
     * Creates a new instance of <code>MandatoryFormNotFoundException</code> without detail message.
     */
    public FormException() {
    }
    
    
    /**
     * Constructs an instance of <code>MandatoryFormNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public FormException(String msg) {
        super(msg);
    }
    
    /**
     * Getter for property formInfo.
     * @return Value of property formInfo.
     */
    public FormInfoBean getFormInfo() {
        return formInfo;
    }
    
    /**
     * Setter for property formInfo.
     * @param formInfo New value of property formInfo.
     */
    public void setFormInfo(FormInfoBean formInfo) {
        this.formInfo = formInfo;
    }
    
    public void addError(FormInfoBean formInfo){
        int severity = formInfo.isMandatory()?S2SValidationException.ERROR:S2SValidationException.CONFIRMATION;
        addError(formInfo,severity);
    }
}
