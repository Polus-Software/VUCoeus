/*
 * BaseStreamGenerator.java
 *
 * Created on September 8, 2004, 10:45 AM
 */

package edu.mit.coeus.xml.generator;

import java.util.Hashtable;
import org.w3c.dom.Document;

/**
 *
 * @author  geot
 */
public abstract class BaseStreamGenerator {
    
    /** Creates a new instance of BaseStreamGenerator */
    public BaseStreamGenerator() {
    }

    public abstract Document getStream(Hashtable params) throws Exception;
    
}
