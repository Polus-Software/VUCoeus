//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for EquipmentCosts element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 479)
 * <p>
 * <pre>
 * &lt;element name="EquipmentCosts">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *           &lt;element name="Cost" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CurrencyType"/>
 *           &lt;element name="Total" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CurrencyType" minOccurs="0"/>
 *           &lt;element name="EquipmentDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface EquipmentCosts
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposal.rar.EquipmentCostsType
{


}
