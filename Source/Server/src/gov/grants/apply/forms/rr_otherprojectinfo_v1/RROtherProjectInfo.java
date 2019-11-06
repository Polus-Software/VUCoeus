//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_otherprojectinfo_v1;


/**
 * Java content class for RR_OtherProjectInfo element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_OtherProjectInfo-V1.0.xsd line 19)
 * <p>
 * <pre>
 * &lt;element name="RR_OtherProjectInfo">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="HumanSubjectsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="HumanSubjectsSupplement" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="HumanSubjectIRBReviewIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="HumanSubjectIRBReviewDate" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>date">
 *                             &lt;attribute name="HumanSubjectIRBReviewIndicator" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="No" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="ExemptionNumbers" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="ExemptionNumber" maxOccurs="6">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;enumeration value="E1"/>
 *                                   &lt;enumeration value="E2"/>
 *                                   &lt;enumeration value="E3"/>
 *                                   &lt;enumeration value="E4"/>
 *                                   &lt;enumeration value="E5"/>
 *                                   &lt;enumeration value="E6"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="HumanSubjectAssuranceNumber" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max10Type" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="VertebrateAnimalsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="VertebrateAnimalsSupplement" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="VertebrateAnimalsIACUCReviewIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="VertebrateAnimalsIACUCApprovalDateReviewDate" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>date">
 *                             &lt;attribute name="VertebrateAnimalsIACUCReviewIndicator" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="No" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="AssuranceNumber" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="9"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ProprietaryInformationIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="EnvironmentalImpact">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="EnvironmentalImpactIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="EnvironmentalImpactExplanation" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max55Type">
 *                             &lt;attribute name="EnvironmentalImpactIndicator" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="Yes" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="EnvironmentalExemption" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="EnvironmentalExemptionIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                               &lt;element name="EnvironmentalExemptionExplanation" minOccurs="0">
 *                                 &lt;complexType>
 *                                   &lt;simpleContent>
 *                                     &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max55Type">
 *                                       &lt;attribute name="EnvironmentalExemptionIndicator" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="Yes" />
 *                                     &lt;/extension>
 *                                   &lt;/simpleContent>
 *                                 &lt;/complexType>
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
 *           &lt;element name="InternationalActivities">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="InternationalActivitiesIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="ActivitiesPartnershipsCountries" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max55Type">
 *                             &lt;attribute name="InternationalActivitiesIndicator" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="Yes" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="InternationalActivitiesExplanation" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max55Type" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="AbstractAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="AbstractAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="FacilitiesAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="FacilitiesAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="EquipmentAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="EquipmentAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="BibliographyAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="BibliographyAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ProjectNarrativeAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ProjectNarrativeAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="OtherAttachments" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="OtherAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" maxOccurs="unbounded"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.0" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RROtherProjectInfo
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfoType
{


}
