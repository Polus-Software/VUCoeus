//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for AnimalSubject element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 381)
 * <p>
 * <pre>
 * &lt;element name="AnimalSubject">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="VertebrateAnimalsUsedQuestion" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;sequence minOccurs="0">
 *             &lt;element name="AssuranceNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *             &lt;choice minOccurs="0">
 *               &lt;element name="IACUCApprovalPending">
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *                   &lt;length value="0"/>
 *                 &lt;/restriction>
 *               &lt;/element>
 *               &lt;element name="IACUCApprovalDate">
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *                 &lt;/restriction>
 *               &lt;/element>
 *             &lt;/choice>
 *           &lt;/sequence>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface AnimalSubject
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposal.rar.AnimalSubjectType
{


}
