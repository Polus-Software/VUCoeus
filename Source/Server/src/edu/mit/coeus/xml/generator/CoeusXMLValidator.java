/*
 * CoeusXMLValidator.java
 *
 * Created on December 16, 2004, 2:54 PM
 */

package edu.mit.coeus.xml.generator;

import com.sun.xml.bind.JAXBObject;
import com.sun.xml.bind.RIElement;
import edu.mit.coeus.utils.CoeusVector;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.bind.Element;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;

/**
 *
 * @author  geot
 */
public class CoeusXMLValidator implements ValidationEventHandler{
    
    private ArrayList errors,fatalErrors,warnings;
    /** Creates a new instance of CoeusXMLValidator */
    public CoeusXMLValidator() {
        super();
    }
    
    public boolean handleEvent(ValidationEvent validationEvent) {
        return true;
    }
    public boolean isAnyError(){
        return (errors!=null|fatalErrors!=null|warnings!=null);
    }

    public static void main(String args[]){
        String errors=null,fatalErrors=null,warnings=null;
        System.out.println((errors!=null|fatalErrors!=null|warnings!=null));
    }
    
    /**
     * Getter for property errors.
     * @return Value of property errors.
     */
    public java.util.ArrayList getErrors() {
        return errors;
    }
    
    /**
     * Getter for property fatalErrors.
     * @return Value of property fatalErrors.
     */
    public java.util.ArrayList getFatalErrors() {
        return fatalErrors;
    }
    
    /**
     * Getter for property warnings.
     * @return Value of property warnings.
     */
    public java.util.ArrayList getWarnings() {
        return warnings;
    }
}
