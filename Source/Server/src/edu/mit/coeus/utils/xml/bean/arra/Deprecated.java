//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * The Deprecated element provides a method for
 * identifying components as being deprecated. A deprecated component is
 * one which is provided, but whose use is not
 * recommended.
 * Java content class for Deprecated element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Subset/niem/appinfo/2.0/appinfo.xsd line 21)
 * <p>
 * <pre>
 * &lt;element name="Deprecated">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;attribute name="value" use="required">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}boolean">
 *               &lt;pattern value="true"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/attribute>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Deprecated
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.arra.DeprecatedType
{


}
