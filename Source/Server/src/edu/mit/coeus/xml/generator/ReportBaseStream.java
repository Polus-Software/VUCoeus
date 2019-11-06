/*
 * ReportBaseStream.java
 *
 * Created on January 19, 2006, 11:39 AM
 */

package edu.mit.coeus.xml.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.utils.dbengine.DBException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;

/**
 *
 * @author  geot
 */
public class ReportBaseStream extends BaseStreamGenerator{
    
    /** Creates a new instance of ReportBaseStream */
    public ReportBaseStream() {
    }
    
    /*
     * It returns the Jaxb Root Object which should be able to marshal to a Document 
     * by using appropriate package name.
     * Made as concrete method for backward compatibility. 
     * It throws CoeusException if not implemented by concrete class
     */
    public Object getObjectStream(Hashtable params) 
            throws DBException,CoeusException{
        throw new CoeusException("Not implemented");
    }
    
    public org.w3c.dom.Document getStream(java.util.Hashtable params) 
            throws DBException,CoeusException {
        throw new CoeusException("Not implemented");
    }    
    
}
