//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.phs398_coverpagesupplement_v1;


/**
 * Java content class for PHS398_CoverPageSupplement element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/PHS398_CoverPageSupplement-V1.0.xsd line 7)
 * <p>
 * <pre>
 * &lt;element name="PHS398_CoverPageSupplement">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="PDPI">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="PDPIName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanNameDataType"/>
 *                     &lt;element name="isNewInvestigator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="Degrees" maxOccurs="3" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="10"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ClinicalTrial" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="isClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="isPhaseIIIClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="ContactPersonInfo">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="ContactName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanNameDataType"/>
 *                     &lt;element name="ContactPhone" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}TelephoneNumberDataType"/>
 *                     &lt;element name="ContactFax" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}TelephoneNumberDataType" minOccurs="0"/>
 *                     &lt;element name="ContactEmail" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}EmailDataType" minOccurs="0"/>
 *                     &lt;element name="ContactTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanTitleDataType"/>
 *                     &lt;element name="ContactAddress" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}AddressRequireCountryDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="StemCells" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="isHumanStemCellsInvolved" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="StemCellsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *                     &lt;element name="CellLines" maxOccurs="20" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;minLength value="0"/>
 *                         &lt;maxLength value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
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
public interface PHS398CoverPageSupplement
    extends javax.xml.bind.Element, gov.grants.apply.forms.phs398_coverpagesupplement_v1.PHS398CoverPageSupplementType
{


}
