/*
 * CoeusXMLException.java
 *
 * Created on October 4, 2004, 1:02 PM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.exception.CoeusException;
import javax.xml.bind.ValidationEvent;

/**
 *
 * @author  geot
 */
public class CoeusXMLException extends CoeusException {
    private ValidationEvent[] events;
    /**
     * Creates a new instance of <code>CoeusXMLException</code> without detail message.
     */
    public CoeusXMLException() {
    }
    
    
    /**
     * Constructs an instance of <code>CoeusXMLException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public CoeusXMLException(String msg) {
        super(msg);
    }

    public CoeusXMLException(ValidationEvent[] events) {
        this("The Genrated XML is not valid against the given Schema");
        this.events = events;
    }
}
