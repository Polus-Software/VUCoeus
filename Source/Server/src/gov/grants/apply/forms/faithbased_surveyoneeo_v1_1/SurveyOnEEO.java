//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.faithbased_surveyoneeo_v1_1;


/**
 * Java content class for SurveyOnEEO element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Coeus/S2S/build/cust/FaithBased_SurveyOnEEO-V1.1.xsd line 5)
 * <p>
 * <pre>
 * &lt;element name="SurveyOnEEO">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *           &lt;element name="DUNSID" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType" minOccurs="0"/>
 *           &lt;element name="OpportunityTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityTitleDataType"/>
 *           &lt;element name="CFDANumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDANumberDataType" minOccurs="0"/>
 *           &lt;element name="ApplicantHas501c3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="FullTimeEmployeeNumber" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="3 or fewer"/>
 *               &lt;enumeration value="4-5"/>
 *               &lt;enumeration value="6-14"/>
 *               &lt;enumeration value="15-50"/>
 *               &lt;enumeration value="51-100"/>
 *               &lt;enumeration value="Over 100"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="ApplicantAnnualBudget" minOccurs="0">
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Less Than $150,000"/>
 *               &lt;enumeration value="$150,000 - $299,999"/>
 *               &lt;enumeration value="$300,000 - $499,999"/>
 *               &lt;enumeration value="$500,000 - $999,999"/>
 *               &lt;enumeration value="$1,000,000 - $4,999,999"/>
 *               &lt;enumeration value="$5,000,000 or more"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="FaithBasedReligious" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="NonReligiousCommunityBased" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="IsIntermediary" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="EverReceivedGovGrantContract" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *           &lt;element name="LocalAffiliateOFNationalOrg" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface SurveyOnEEO
    extends javax.xml.bind.Element, gov.grants.apply.forms.faithbased_surveyoneeo_v1_1.SurveyOnEEOType
{


}
