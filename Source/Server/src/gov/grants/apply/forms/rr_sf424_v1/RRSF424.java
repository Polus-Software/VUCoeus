//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_sf424_v1;


/**
 * Java content class for RR_SF424 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_SF424-V1.0.xsd line 20)
 * <p>
 * <pre>
 * &lt;element name="RR_SF424">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="SubmissionTypeCode" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}SubmissionTypeDataType"/>
 *           &lt;element name="SubmittedDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *           &lt;element name="ApplicantID" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}ApplicantIDDataType" minOccurs="0"/>
 *           &lt;element name="StateReceivedDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *           &lt;element name="StateID" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}StateIDDataType" minOccurs="0"/>
 *           &lt;element name="FederalID" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FederalIDDataType" minOccurs="0"/>
 *           &lt;element name="ApplicantInfo">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="OrganizationInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}OrganizationDataTypeV2"/>
 *                     &lt;element name="ContactPersonInfo">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="Name" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanNameDataType"/>
 *                               &lt;element name="Phone" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}TelephoneNumberDataType"/>
 *                               &lt;element name="Fax" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}TelephoneNumberDataType" minOccurs="0"/>
 *                               &lt;element name="Email" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}EmailDataType" minOccurs="0"/>
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
 *           &lt;element name="EmployerID">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="9"/>
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="ApplicantType">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ApplicantTypeCode" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}ApplicantTypeCodeDataType"/>
 *                     &lt;element name="ApplicantTypeCodeOtherExplanation" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max50Type">
 *                             &lt;attribute name="ApplicantTypeCode" use="required" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}ApplicantTypeCodeDataType" fixed="P: Other (specify)" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="SmallBusinessOrganizationType" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="isWomenOwned" minOccurs="0">
 *                                 &lt;complexType>
 *                                   &lt;simpleContent>
 *                                     &lt;extension base="&lt;http://apply.grants.gov/system/GlobalLibrary-V1.0>YesNoDataType">
 *                                     &lt;/extension>
 *                                   &lt;/simpleContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                               &lt;element name="isSociallyEconomicallyDisadvantaged" minOccurs="0">
 *                                 &lt;complexType>
 *                                   &lt;simpleContent>
 *                                     &lt;extension base="&lt;http://apply.grants.gov/system/GlobalLibrary-V1.0>YesNoDataType">
 *                                     &lt;/extension>
 *                                   &lt;/simpleContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                             &lt;attribute name="ApplicantTypeCode" use="required" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}ApplicantTypeCodeDataType" fixed="O: Small Business" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ApplicationType">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ApplicationTypeCode" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}ApplicationTypeCodeDataType"/>
 *                     &lt;element name="RevisionCode" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/forms/RR_SF424-V1.0>RevisionTypeCodeDataType">
 *                             &lt;attribute name="ApplicationTypeCode" use="required" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}ApplicationTypeCodeDataType" fixed="Revision" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="RevisionCodeOtherExplanation" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max45Type">
 *                             &lt;attribute name="RevisionCode" use="required" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}RevisionTypeCodeDataType" fixed="E" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="isOtherAgencySubmission" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="OtherAgencySubmissionExplanation" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://apply.grants.gov/forms/RR_SF424-V1.0>StringMin1Max20Type">
 *                             &lt;attribute name="isOtherAgencySubmission" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" fixed="Yes" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="FederalAgencyName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}AgencyNameDataType"/>
 *           &lt;element name="CFDANumber" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}CFDANumberDataType" minOccurs="0"/>
 *           &lt;element name="ActivityTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}ProgramActivityTitleDataType" minOccurs="0"/>
 *           &lt;element name="ProjectTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}ProjectTitleDataType"/>
 *           &lt;element name="Location" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max45Type"/>
 *           &lt;element name="ProposedProjectPeriod">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ProposedStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                     &lt;element name="ProposedEndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="CongressionalDistrict">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ApplicantCongressionalDistrict" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}CongressionalDistrictDataType"/>
 *                     &lt;element name="ProjectCongressionalDistrict" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}CongressionalDistrictDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="PDPIContactInfo" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}OrganizationContactPersonDataType"/>
 *           &lt;element name="EstimatedProjectFunding">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="TotalEstimatedAmount" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="TotalfedNonfedrequested" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="EstimatedProgramIncome" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}BudgetTotalAmountDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="StateReview">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="StateReviewCodeType" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}StateReviewCodeTypeDataType"/>
 *                     &lt;element name="StateReviewDate" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;simpleContent>
 *                           &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>date">
 *                             &lt;attribute name="StateReviewCodeType" use="required" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}StateReviewCodeTypeDataType" fixed="Yes" />
 *                           &lt;/extension>
 *                         &lt;/simpleContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="TrustAgree" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="AORInfo" type="{http://apply.grants.gov/forms/RR_SF424-V1.0}AORInfoType"/>
 *           &lt;element name="PreApplicationAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *           &lt;element name="AOR_Signature" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}SignatureDataType"/>
 *           &lt;element name="AOR_SignedDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;/sequence>
 *         &lt;attribute ref="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersion use="required" fixed="1.0""/>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRSF424
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_sf424_v1.RRSF424Type
{


}