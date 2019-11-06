//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//

package edu.mit.coeus.utils.xml.bean.arra.impl.runtime;

import org.xml.sax.SAXException;
import com.sun.xml.bind.JAXBObject;

/**
 * For a generated class to be serializable, it has to
 * implement this interface.
 * 
 * @author Kohsuke Kawaguchi
 */
public interface XMLSerializable extends JAXBObject
{
    /**
     * Serializes child elements and texts into the specified target.
     */
    void serializeBody( XMLSerializer target ) throws SAXException;
    
    /**
     * Serializes attributes into the specified target.
     */
    void serializeAttributes( XMLSerializer target ) throws SAXException;
    
    /**
     * Declares all the namespace URIs this object is using at
     * its top-level scope into the specified target.
     */
    void serializeURIs( XMLSerializer target ) throws SAXException;

}
