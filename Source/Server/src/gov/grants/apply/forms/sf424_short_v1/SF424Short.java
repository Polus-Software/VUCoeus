//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424_short_v1;


/**
 * Java content class for SF424_Short element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/apply/forms/schemas/SF424_Short-V1.0.xsd line 7)
 * <p>
 * <pre>
 * &lt;element name="SF424_Short">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="AgencyName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AgencyNameDataType"/>
 *           &lt;element name="CFDANumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDANumberDataType" minOccurs="0"/>
 *           &lt;element name="CFDAProgramTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDATitleDataType" minOccurs="0"/>
 *           &lt;element name="DateReceived" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="FundingOpportunityNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityIDDataType"/>
 *           &lt;element name="FundingOpportunityTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityTitleDataType"/>
 *           &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType"/>
 *           &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *           &lt;element name="ApplicantWebAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="ApplicantTypeCode1" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType"/>
 *           &lt;element name="ApplicantTypeCode2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType" minOccurs="0"/>
 *           &lt;element name="ApplicantTypeCode3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType" minOccurs="0"/>
 *           &lt;element name="ApplicantTypeOtherSpecify" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="30"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="EmployerTaxpayerIdentificationNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmployerIDDataType"/>
 *           &lt;element name="DUNSNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType"/>
 *           &lt;element name="CongressionalDistrictApplicant" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CongressionalDistrictDataType"/>
 *           &lt;element name="ProjectTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ProjectTitleDataType"/>
 *           &lt;element name="ProjectDescription">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="ProjectStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="ProjectEndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="ProjectDirectorSSN" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SocialSecurityNumberDataType" minOccurs="0"/>
 *           &lt;element name="ProjectDirectorGroup" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ContactPersonDataType"/>
 *           &lt;element name="SameAsProjectDirector" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="ContactPersonSSN" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SocialSecurityNumberDataType" minOccurs="0"/>
 *           &lt;element name="ContactPersonGroup" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ContactPersonDataType" minOccurs="0"/>
 *           &lt;element name="ApplicationCertification" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *           &lt;element name="AuthorizedRepresentative" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *           &lt;element name="AuthorizedRepresentativeTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
 *           &lt;element name="AuthorizedRepresentativeEmail" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType"/>
 *           &lt;element name="AuthorizedRepresentativePhoneNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *           &lt;element name="AuthorizedRepresentativeFaxNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *           &lt;element name="AuthorizedRepresentativeSignature" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SignatureDataType"/>
 *           &lt;element name="AuthorizedRepresentativeDateSigned" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.0" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface SF424Short
    extends javax.xml.bind.Element, gov.grants.apply.forms.sf424_short_v1.SF424ShortType
{


}
