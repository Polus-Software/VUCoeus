/*
 * MandatoryFormNotFoundException.java
 *
 * Created on December 27, 2004, 11:47 AM
 */

package edu.mit.coeus.s2s.validator;


/**
 *
 * @author  geot
 */
public class MandatoryFormNotFoundException extends S2SValidationException {
    
    /**
     * Creates a new instance of <code>MandatoryFormNotFoundException</code> without detail message.
     */
    public MandatoryFormNotFoundException() {
    }
    
    
    /**
     * Constructs an instance of <code>MandatoryFormNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MandatoryFormNotFoundException(String msg) {
        super(msg);
    }
}
