//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Java content class for ProjectDescription element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 480)
 * <p>
 * <pre>
 * &lt;element name="ProjectDescription">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}HumanSubject"/>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}AnimalSubject"/>
 *           &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ProjectSurvey"/>
 *           &lt;element name="ProjectSummary" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}DescriptionBlockType" minOccurs="0"/>
 *           &lt;element name="FacilitiesDescription" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}DescriptionBlockType" minOccurs="0"/>
 *           &lt;element name="EquipmentDescription" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}DescriptionBlockType" minOccurs="0"/>
 *           &lt;element name="References" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}DescriptionBlockType" minOccurs="0"/>
 *           &lt;element name="ActivityType" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ActivityTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                     &lt;element name="ActivityTypeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
public interface ProjectDescription
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposal.nih.ProjectDescriptionType
{


}