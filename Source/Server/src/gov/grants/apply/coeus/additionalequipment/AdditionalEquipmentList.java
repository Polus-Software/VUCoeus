//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.04.12 at 03:03:53 EDT 
//


package gov.grants.apply.coeus.additionalequipment;


/**
 * Comment describing your root element
 * Java content class for AdditionalEquipmentList element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/AdditionalEquipmentAttachment.xsd line 6)
 * <p>
 * <pre>
 * &lt;element name="AdditionalEquipmentList">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="EquipmentList" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="EquipmentItem">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="64"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="FundsRequested" type="{http://apply.grants.gov/system/Global-V1.0}DecimalMin1Max14Places2Type"/>
 *                     &lt;element name="NonFederal" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="15"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="TotalFedNonFed" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         &lt;totalDigits value="15"/>
 *                         &lt;fractionDigits value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ProposalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="BudgetPeriod" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface AdditionalEquipmentList
    extends javax.xml.bind.Element, gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType
{


}