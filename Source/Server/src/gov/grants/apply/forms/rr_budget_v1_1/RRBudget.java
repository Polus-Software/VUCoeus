//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_budget_v1_1;


/**
 * Java content class for RR_Budget element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_Budget-V1.1.xsd line 7)
 * <p>
 * <pre>
 * &lt;element name="RR_Budget">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="DUNSID" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType"/>
 *           &lt;element name="BudgetType" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetTypeDataType"/>
 *           &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *           &lt;element name="BudgetYear1" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetYear1DataType"/>
 *           &lt;element name="BudgetYear2" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetYearDataType" minOccurs="0"/>
 *           &lt;element name="BudgetYear3" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetYearDataType" minOccurs="0"/>
 *           &lt;element name="BudgetYear4" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetYearDataType" minOccurs="0"/>
 *           &lt;element name="BudgetYear5" type="{http://apply.grants.gov/forms/RR_Budget-V1-1}BudgetYearDataType" minOccurs="0"/>
 *           &lt;element name="BudgetSummary">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CumulativeTotalFundsRequestedSeniorKeyPerson" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedOtherPersonnel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalNoOtherPersonnel" minOccurs="0">
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="9999"/>
 *                       &lt;/restriction>
 *                     &lt;/element>
 *                     &lt;element name="CumulativeTotalFundsRequestedPersonnel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeEquipments" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="CumulativeTotalFundsRequestedEquipment" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="CumulativeTravels" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="CumulativeTotalFundsRequestedTravel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                               &lt;element name="CumulativeDomesticTravelCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeForeignTravelCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="CumulativeTrainee" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="CumulativeTotalFundsRequestedTraineeCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                               &lt;element name="CumulativeTraineeTuitionFeesHealthInsurance" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeTraineeStipends" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeTraineeTravel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeTraineeSubsistence" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeOtherTraineeCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeNoofTrainees" minOccurs="0">
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *                                   &lt;minInclusive value="0"/>
 *                                   &lt;maxInclusive value="999"/>
 *                                 &lt;/restriction>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="CumulativeOtherDirect" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="CumulativeTotalFundsRequestedOtherDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                               &lt;element name="CumulativeMaterialAndSupplies" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativePublicationCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeConsultantServices" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeADPComputerServices" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeSubawardConsortiumContractualCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeEquipmentFacilityRentalFees" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeAlterationsAndRenovations" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeOther1DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeOther2DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                               &lt;element name="CumulativeOther3DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="CumulativeTotalFundsRequestedDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedIndirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedDirectIndirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeFee" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRBudget
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_budget_v1_1.RRBudgetType
{


}
