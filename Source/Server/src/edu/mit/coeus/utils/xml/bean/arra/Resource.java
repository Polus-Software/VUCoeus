//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * The Resource element provides a method for
 * application information to define a name within a schema, without the
 * name being bound to a schema component. This is used by the
 * structures schema to define names for structures:Object and
 * structures:Association.
 * Java content class for Resource element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/appinfo/2.0/appinfo.xsd line 9)
 * <p>
 * <pre>
 * &lt;element name="Resource">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Resource
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.arra.ResourceType
{


}
