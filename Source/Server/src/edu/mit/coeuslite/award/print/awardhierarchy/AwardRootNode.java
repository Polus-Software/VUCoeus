//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.12 at 04:36:38 IST 
//


package edu.mit.coeuslite.award.print.awardhierarchy;


/**
 * Comment describing your root element
 * Java content class for AwardRootNode element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/NetBean7.1/COEUS4.5YORN/Source/Server/src/edu/mit/coeuslite/award/schema/AwardHierarchy.xsd line 4)
 * <p>
 * <pre>
 * &lt;element name="AwardRootNode">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="AwardItems" type="{}AwardDetails" maxOccurs="unbounded"/>
 *           &lt;element name="MITAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="AwardPI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="AwardDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface AwardRootNode
    extends javax.xml.bind.Element, edu.mit.coeuslite.award.print.awardhierarchy.AwardRootNodeType
{


}
