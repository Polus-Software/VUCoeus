//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for Minutes element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 869)
 * <p>
 * <pre>
 * &lt;element name="Minutes">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ScheduleId">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="10"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="EntryNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="EntryTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="EntryTypeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="EntrySortCode" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *           &lt;element name="ProtocolNumber" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="PrivateCommentFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="FinalFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="ProtocolContingencyCode" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="MinuteEntry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface Minutes
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.reviewcomments.MinutesType
{


}
