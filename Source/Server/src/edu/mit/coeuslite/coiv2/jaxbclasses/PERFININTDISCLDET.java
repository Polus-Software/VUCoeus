//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for PER_FIN_INT_DISCL_DET element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 155)
 * <p>
 * <pre>
 * &lt;element name="PER_FIN_INT_DISCL_DET">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ENTITY_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="COLUMN_NAME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="COLUMN_VALUE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="RELATIONSHIP_TYPE_CODE" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="COMMENTS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="UPDATE_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *           &lt;element name="UPDATE_USER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface PERFININTDISCLDET
    extends javax.xml.bind.Element, edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDETType
{


}
