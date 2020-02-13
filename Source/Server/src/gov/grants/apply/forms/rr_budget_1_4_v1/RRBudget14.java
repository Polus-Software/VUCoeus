//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.27 at 01:42:23 CDT 
//


package gov.grants.apply.forms.rr_budget_1_4_v1;


/**
 * Java content class for RR_Budget_1_4 element declaration.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://trainingapply.grants.gov/apply/forms/schemas/RR_Budget_1_4-V1.4.xsd line 6)
 * <p>
 * <pre>
 * &lt;element name="RR_Budget_1_4">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;sequence>
 *           &lt;element name="DUNSID" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType"/>
 *           &lt;element name="BudgetType" type="{http://apply.grants.gov/forms/RR_Budget_1_4-V1.4}BudgetTypeDataType"/>
 *           &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *           &lt;element name="BudgetYear" type="{http://apply.grants.gov/forms/RR_Budget_1_4-V1.4}BudgetYearDataType" maxOccurs="5"/>
 *           &lt;element name="BudgetJustificationAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *           &lt;element name="BudgetSummary">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="CumulativeTotalFundsRequestedSeniorKeyPerson" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedOtherPersonnel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalNoOtherPersonnel" type="{http://apply.grants.gov/forms/RR_Budget_1_4-V1.4}Int4DataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedPersonnel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedEquipment" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedTravel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeDomesticTravelCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeForeignTravelCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedTraineeCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTraineeTuitionFeesHealthInsurance" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTraineeStipends" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTraineeTravel" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTraineeSubsistence" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeOtherTraineeCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeNoofTrainees" type="{http://apply.grants.gov/forms/RR_Budget_1_4-V1.4}Int4DataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedOtherDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeMaterialAndSupplies" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativePublicationCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeConsultantServices" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeADPComputerServices" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeSubawardConsortiumContractualCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeEquipmentFacilityRentalFees" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeAlterationsAndRenovations" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeOther1DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeOther2DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeOther3DirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedDirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedIndirectCost" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalFundsRequestedDirectIndirectCosts" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                     &lt;element name="CumulativeFee" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType" minOccurs="0"/>
 *                     &lt;element name="CumulativeTotalCostsFee" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *         &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.4" />
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 */
public interface RRBudget14
    extends javax.xml.bind.Element, gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14Type
{


}
