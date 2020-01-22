//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.04.12 at 03:07:00 EDT 
//


package gov.grants.apply.coeus.extrakeyperson;


/**
 * Comment describing your root element
 * Java content class for ExtraKeyPersonList element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/ExtraKeyPersonAttachment.xsd line 5)
 * <p>
 * <pre>
 * &lt;element name="ExtraKeyPersonList">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="ProposalNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="BudgetPeriod" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *           &lt;element name="KeyPersons" maxOccurs="unbounded">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="Prefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="MiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="Suffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="ProjectRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                     &lt;element name="Compensation">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="CalendarMonths" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="4"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="SummerMonths" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="4"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="AcademicMonths" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="4"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="RequestedSalary">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="14"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="FringeBenefits">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="14"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="FundsRequested">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="15"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="NonFederal" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="15"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="TotalFedNonFed" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="15"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                               &lt;element name="BaseSalary" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   &lt;totalDigits value="14"/>
 *                                   &lt;fractionDigits value="2"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ExtraKeyPersonList
    extends javax.xml.bind.Element, gov.grants.apply.coeus.extrakeyperson.ExtraKeyPersonListType
{


}