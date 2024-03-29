//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.10 at 07:45:37 IST 
//


package edu.mit.coeuslite.coiv2.print.approved;


/**
 * Java content class for ApprovedDisclosure element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/COEUS45/Source/Server/src/edu/mit/coeuslite/coiv2/schema/ApprovedDisclosure.xsd line 4)
 * <p>
 * <pre>
 * &lt;element name="ApprovedDisclosure">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="disclosureNumber">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="sequenceNumber">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="personID">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="9"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="certificationText" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="350000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="certifiedBy" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="certificationTimestamp" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="disclosureDipositionStatus">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="disclosureStatus">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="expirationDate">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="updateTimestamp">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="updateUser">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="moduleCode" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="reviewStatus" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element ref="{}person" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}disclosureProjects" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}disclosureDocuments" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}disclosureNotes" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element ref="{}certificationQuestion" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ApprovedDisclosure
    extends javax.xml.bind.Element, edu.mit.coeuslite.coiv2.print.approved.ApprovedDisclosureType
{


}
