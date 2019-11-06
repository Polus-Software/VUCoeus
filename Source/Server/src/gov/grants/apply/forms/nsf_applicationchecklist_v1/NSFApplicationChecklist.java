//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.nsf_applicationchecklist_v1;


/**
 * Java content class for NSF_ApplicationChecklist element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/NSF_ApplicationChecklist-V1.0.xsd line 18)
 * <p>
 * <pre>
 * &lt;element name="NSF_ApplicationChecklist">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="CoverSheet">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckCoverSheet" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckRenewal" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckFullApp" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckTypeApp" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckAppCert" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="CheckRRSite" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="CheckRROtherInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="CheckProjectSummary" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="ProjectNarrative">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckProjectNarrative" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckMeritReview" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckPriorSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckHRInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckURL" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="CheckBiblio" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="CheckFacilities" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="Equipment">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckEquipment" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckSuppDoc" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckAdditionalItems" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="RRSrProfile">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckRRSrProfile" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckBioSketch" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckCurrentPendingSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="CheckRRPersonalData" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *           &lt;element name="RRBudget">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckRRBudget" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckRRBudgetJustification" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckCostSharing" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="NSFCover">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CheckNSFCover" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckNSFUnit" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *                     &lt;element name="CheckNSFOtherInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckNSFSFLLL" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckNSFDevAuth" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckNSFReg" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
 *                     &lt;element name="CheckDoNotInclude" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoNotApplicableDataType"/>
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
public interface NSFApplicationChecklist
    extends javax.xml.bind.Element, gov.grants.apply.forms.nsf_applicationchecklist_v1.NSFApplicationChecklistType
{


}
