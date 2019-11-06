//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.04.21 at 01:37:43 PM IST 
//

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package edu.mit.coeus.utils.xml.bean.impl.runtime;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.helpers.PrintConversionEventImpl;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;

import org.xml.sax.SAXException;

import com.sun.xml.bind.Messages;
import com.sun.xml.bind.serializer.AbortSerializationException;
import com.sun.xml.bind.util.ValidationEventLocatorExImpl;

/**
 * 
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class Util {
    /**
     * Report a print conversion error while marshalling.
     */
    public static void handlePrintConversionException(
        Object caller, Exception e, XMLSerializer serializer ) throws SAXException {
        
        if( e instanceof SAXException )
            // assume this exception is not from application.
            // (e.g., when a marshaller aborts the processing, this exception
            //        will be thrown) 
            throw (SAXException)e;
        
        String message = e.getMessage();
        if(message==null) {
            message = e.toString();
        }
        
        ValidationEvent ve = new PrintConversionEventImpl(
            ValidationEvent.ERROR, message,
            new ValidationEventLocatorImpl(caller), e );
        serializer.reportError(ve);
    }
  /**
     * Reports that the type of an object in a property is unexpected.  
     */
    public static void handleTypeMismatchError( XMLSerializer serializer,
            Object parentObject, String fieldName, Object childObject ) throws AbortSerializationException {
        
         ValidationEvent ve = new ValidationEventImpl(
            ValidationEvent.ERROR, // maybe it should be a fatal error.
            Messages.format(Messages.ERR_TYPE_MISMATCH,
                getUserFriendlyTypeName(parentObject),
                fieldName,
                getUserFriendlyTypeName(childObject) ),
            new ValidationEventLocatorExImpl(parentObject,fieldName) );
         
        serializer.reportError(ve);
    }
    
    private static String getUserFriendlyTypeName( Object o ) {
        if( o instanceof ValidatableObject )
            return ((ValidatableObject)o).getPrimaryInterface().getName();
        else
            return o.getClass().getName();
    }
    
/*    
    private static final Class[] xmlSerializableSatellite = new Class[] {
        XMLSerializable.class,
        XMLSerializer.class,
        NamespaceContext2.class
    };
    
    public static XMLSerializable toXMLSerializable(Object o) {
        return (XMLSerializable)ProxyGroup.blindWrap( o, XMLSerializable.class, xmlSerializableSatellite );
    }
    
    
    private static final Class[] validatableSatellite = new Class[] {
        XMLSerializable.class,
        XMLSerializer.class,
        ValidatableObject.class,
        NamespaceContext2.class
    };
    
    public static ValidatableObject toValidatableObject(Object o) {
        return (ValidatableObject)ProxyGroup.blindWrap( o, ValidatableObject.class, validatableSatellite );
    }
*/
}
