//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * The ConformantIndicator element may be used in two
 * ways. First, it is included as application information for a schema
 * document element to indicate that the schema is NIEM-conformant.
 * Second, it is used as application information of a namespace import
 * to indicate that the schema is not
 * NIEM-conformant.
 * Java content class for ConformantIndicator element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/appinfo/2.0/appinfo.xsd line 74)
 * <p>
 * <pre>
 * &lt;element name="ConformantIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 * </pre>
 * 
 */
public interface ConformantIndicator
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
