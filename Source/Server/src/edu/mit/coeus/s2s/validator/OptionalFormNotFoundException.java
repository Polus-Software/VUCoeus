/*
 * OptionalFormNotFoundException.java
 *
 * Created on December 27, 2004, 11:48 AM
 */

package edu.mit.coeus.s2s.validator;


/**
 *
 * @author  geot
 */
public class OptionalFormNotFoundException extends S2SValidationException {
    
    /**
     * Creates a new instance of <code>OptionalFormNotFoundException</code> without detail message.
     */
    public OptionalFormNotFoundException() {
    }
    
    
    /**
     * Constructs an instance of <code>OptionalFormNotFoundException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public OptionalFormNotFoundException(String msg) {
        super(msg);
    }
}
