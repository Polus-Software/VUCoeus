//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for COI_DISCLOSURE element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 4)
 * <p>
 * <pre>
 * &lt;element name="COI_DISCLOSURE">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="COI_DISCLOSURE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="PERSON_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="CERTIFICATION_TEXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="CERTIFIED_BY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="CERTIFICATION_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *           &lt;element ref="{}COI_DISPOSITION_STATUS"/>
 *           &lt;element name="EXPIRATION_DATE" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *           &lt;element name="UPDATE_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *           &lt;element ref="{}QUESTION"/>
 *           &lt;element name="UPDATE_USER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="MODULE_CODE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *           &lt;element ref="{}COI_DISC_DETAILS" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}COI_DISCL_PROJECTS" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}COI_DOCUMENTS" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}COI_NOTEPAD" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}COI_USER_ROLES" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}COI_STATUS"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface COIDISCLOSURE
    extends javax.xml.bind.Element, edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSUREType
{


}