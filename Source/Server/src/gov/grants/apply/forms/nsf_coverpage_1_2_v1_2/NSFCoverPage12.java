//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nsf_coverpage_1_2_v1_2;


/**
 * Java content class for NSF_CoverPage_1_2 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_CoverPage_1_2-V1.2.xsd line 6)
 * <p>
 * <pre>
 * &lt;element name="NSF_CoverPage_1_2">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="FundingOpportunityNumber">
 *             &lt;restriction base="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityIDDataType">
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/element>
 *           &lt;element name="DueDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *           &lt;element name="NSFUnitConsideration">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="DivisionCode">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="8"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="DivisionName" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *                     &lt;element name="ProgramCode">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="1"/>
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="ProgramName" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="PIInfo">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="isCurrentPI" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="OtherInfo" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="isBeginInvestigator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="isDisclosureLobbyingActivities" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="isExploratoryResearch" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="isAccomplishmentRenewal" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="isHighResolutionGraphics" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Single-CopyDocuments" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachmentGroupMin1Max100DataType" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.2" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface NSFCoverPage12
    extends javax.xml.bind.Element, gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type
{


}