//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * The ExternalAdapterTypeIndicator element indicates
 * that a complex type is an external adapter type. Such a type is one
 * that is composed of elements and attributes from non-NIEM-conformant
 * schemas. The indicator allows schema processors to switch to
 * alternative processing modes when processing NIEM-conformant versus
 * non-NIEM-conformant content.
 * Java content class for ExternalAdapterTypeIndicator element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/appinfo/2.0/appinfo.xsd line 84)
 * <p>
 * <pre>
 * &lt;element name="ExternalAdapterTypeIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * </pre>
 * 
 */
public interface ExternalAdapterTypeIndicator
    extends javax.xml.bind.Element
{


    /**
     * Gets the value of the value property.
     * 
     */
    boolean isValue();

    /**
     * Sets the value of the value property.
     * 
     */
    void setValue(boolean value);

}
