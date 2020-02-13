//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Information that NIH collects,  which is independent and not directly associated with the information collected within the "Research Project" structure.
 * Java content class for ResearchApplicationExtension element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 527)
 * <p>
 * <pre>
 * &lt;element name="ResearchApplicationExtension">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;choice>
 *             &lt;element name="NewApplicationQualifiers">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                     &lt;choice minOccurs="0">
 *                       &lt;element name="SBIRQuestions">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;choice>
 *                                 &lt;element name="Phase1Indicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                 &lt;element name="Phase2Details">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;sequence>
 *                                           &lt;element name="Phase2Indicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                           &lt;element name="Phase1GrantNumber" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *                                           &lt;element name="InventionsAndPatents" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}InventionsAndPatentsType"/>
 *                                         &lt;/sequence>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                                 &lt;element name="FastTrackIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                               &lt;/choice>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                       &lt;element name="STTRQuestions">
 *                         &lt;complexType>
 *                           &lt;complexContent>
 *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                               &lt;choice>
 *                                 &lt;element name="Phase1Indicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                 &lt;element name="Phase2Details">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;sequence>
 *                                           &lt;element name="Phase2Indicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                                           &lt;element name="Phase1GrantNumber" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *                                           &lt;element name="InventionsAndPatents" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}InventionsAndPatentsType"/>
 *                                         &lt;/sequence>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                                 &lt;element name="FastTrackIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                               &lt;/choice>
 *                             &lt;/restriction>
 *                           &lt;/complexContent>
 *                         &lt;/complexType>
 *                       &lt;/element>
 *                     &lt;/choice>
 *                   &lt;/restriction>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;element name="RevisionOfApplicationNumber" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}GrantNumberType"/>
 *             &lt;element name="CompetingContinuationQualifiers">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                     &lt;sequence>
 *                       &lt;element name="PriorGrantNumber" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}GrantNumberType"/>
 *                       &lt;element name="InventionsAndPatents" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}InventionsAndPatentsType"/>
 *                     &lt;/sequence>
 *                   &lt;/restriction>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;element name="SupplementGrantNumber" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}GrantNumberType"/>
 *           &lt;/choice>
 *           &lt;element name="PIChangeIndicator" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="FormerPIName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ForeignApplicationIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="SmokeFreeWorkplaceIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="ResearchPlan">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;choice minOccurs="0">
 *                       &lt;element name="RevisedApplicationIntroAttachment" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                       &lt;element name="SupplementalApplicationIntroAttachment" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;/choice>
 *                     &lt;element name="Narrative">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="SpecificAims" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                               &lt;element name="BackgroundAndSignificance" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                               &lt;choice>
 *                                 &lt;element name="PreliminaryStudies" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                                 &lt;element name="ProgressReport" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                               &lt;/choice>
 *                               &lt;element name="ResearchDesignAndMethods" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="HumanSubjects" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;element name="VertebrateAnimals" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;element name="LiteratureCited" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;element name="ConsortiumAndContractualArrangements" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;element name="Consultants" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                     &lt;element name="ProductDevelopmentPlan" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="AssuranceAndCertificationCompliance" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}AssuranceType"/>
 *           &lt;element name="InvestigatorCoverLetter" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}FileIdentifierType" minOccurs="0"/>
 *           &lt;element name="NonKeyPersonBiographicalSketch" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace}NonKeyPersonBiosketchType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface ResearchApplicationExtension
    extends javax.xml.bind.Element, edu.mit.coeus.utils.xml.bean.proposal.nih.ResearchApplicationExtensionType
{


}
