//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for COI_NOTEPAD element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 108)
 * <p>
 * <pre>
 * &lt;element name="COI_NOTEPAD">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="COI_DISCLOSURE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="ENTRY_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element ref="{}COI_NOTEPAD_ENTRY_TYPE" minOccurs="0"/>
 *           &lt;element name="COMMENTS" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="RESTRICTED_VIEW" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="UPDATE_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *           &lt;element name="UPDATE_USER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="TITLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface COINOTEPAD
    extends javax.xml.bind.Element, edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADType
{


}