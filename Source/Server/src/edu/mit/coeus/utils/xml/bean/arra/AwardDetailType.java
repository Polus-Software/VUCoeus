//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra;


/**
 * A data type containing elements related to a grant, loan, or contract award.
 * Java content class for AwardDetailType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Backup%20%20-%20Mohan/CoeusDocs/Proposal%20Printing/schemas/FederalReportingDataElements_v1/RecoveryRR/Version%201/Version%201.0/Schemas/Extension/recoveryrr-ec.xsd line 48)
 * <p>
 * <pre>
 * &lt;complexType name="AwardDetailType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://niem.gov/niem/structures/2.0}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}FundingAgencyCode"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}AwardingAgencyCode"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}FinalReportIndicator"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}CompleteTreasuryAccountSymbol"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}AwardDate"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}AwardDescription"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}AwardAmount"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}ProjectDescription"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}ProjectStatus"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}JobCreationNumber"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}JobCreationNarrative"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}TotalFederalARRAReceived"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}TotalNumberSubawardsIndividuals"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}TotalAmountSubawardsIndividuals"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}TotalNumberSmallSubawards"/>
 *         &lt;element ref="{urn:us:gov:recoveryrr-ext}TotalAmountSmallSubawards"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AwardDetailType
    extends edu.mit.coeus.utils.xml.bean.arra.ComplexObjectType
{


    /**
     * Gets the value of the projectStatus property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.ProjectStatus}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.ProjectStatusType}
     *     {@link null}
     */
    edu.mit.coeus.utils.xml.bean.arra.ProjectStatusType getProjectStatus();

    /**
     * Sets the value of the projectStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.ProjectStatus}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.ProjectStatusType}
     *     {@link null}
     */
    void setProjectStatus(edu.mit.coeus.utils.xml.bean.arra.ProjectStatusType value);

    /**
     * Gets the value of the totalNumberSubawardsIndividuals property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getTotalNumberSubawardsIndividuals();

    /**
     * Sets the value of the totalNumberSubawardsIndividuals property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    void setTotalNumberSubawardsIndividuals(java.math.BigDecimal value);

    /**
     * Gets the value of the jobCreationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getJobCreationNumber();

    /**
     * Sets the value of the jobCreationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setJobCreationNumber(java.math.BigDecimal value);

    /**
     * Gets the value of the finalReportIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicatorType}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicator}
     */
    edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicatorType getFinalReportIndicator();

    /**
     * Sets the value of the finalReportIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicatorType}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicator}
     */
    void setFinalReportIndicator(edu.mit.coeus.utils.xml.bean.arra.FinalReportIndicatorType value);

    /**
     * Gets the value of the awardingAgencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardingAgencyCode}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType}
     */
    edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType getAwardingAgencyCode();

    /**
     * Sets the value of the awardingAgencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardingAgencyCode}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType}
     */
    void setAwardingAgencyCode(edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType value);

    /**
     * Gets the value of the awardDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getAwardDescription();

    /**
     * Sets the value of the awardDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setAwardDescription(java.lang.String value);

    /**
     * Gets the value of the completeTreasuryAccountSymbol property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbol}
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbolType}
     */
    edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbolType getCompleteTreasuryAccountSymbol();

    /**
     * Sets the value of the completeTreasuryAccountSymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbol}
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbolType}
     */
    void setCompleteTreasuryAccountSymbol(edu.mit.coeus.utils.xml.bean.arra.CompleteTreasuryAccountSymbolType value);

    /**
     * Gets the value of the totalFederalARRAReceived property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getTotalFederalARRAReceived();

    /**
     * Sets the value of the totalFederalARRAReceived property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setTotalFederalARRAReceived(java.math.BigDecimal value);

    /**
     * Gets the value of the fundingAgencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FundingAgencyCode}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType}
     *     {@link null}
     */
    edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType getFundingAgencyCode();

    /**
     * Sets the value of the fundingAgencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.FundingAgencyCode}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType}
     *     {@link null}
     */
    void setFundingAgencyCode(edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType value);

    /**
     * Gets the value of the projectDescription property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getProjectDescription();

    /**
     * Sets the value of the projectDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setProjectDescription(java.lang.String value);

    /**
     * Gets the value of the totalAmountSubawardsIndividuals property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getTotalAmountSubawardsIndividuals();

    /**
     * Sets the value of the totalAmountSubawardsIndividuals property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setTotalAmountSubawardsIndividuals(java.math.BigDecimal value);

    /**
     * Gets the value of the jobCreationNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getJobCreationNarrative();

    /**
     * Sets the value of the jobCreationNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setJobCreationNarrative(java.lang.String value);

    /**
     * Gets the value of the totalNumberSmallSubawards property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getTotalNumberSmallSubawards();

    /**
     * Sets the value of the totalNumberSmallSubawards property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setTotalNumberSmallSubawards(java.math.BigDecimal value);

    /**
     * Gets the value of the awardDate property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.Date}
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardDate}
     */
    edu.mit.coeus.utils.xml.bean.arra.Date getAwardDate();

    /**
     * Sets the value of the awardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.arra.Date}
     *     {@link null}
     *     {@link edu.mit.coeus.utils.xml.bean.arra.AwardDate}
     */
    void setAwardDate(edu.mit.coeus.utils.xml.bean.arra.Date value);

    /**
     * Gets the value of the totalAmountSmallSubawards property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getTotalAmountSmallSubawards();

    /**
     * Sets the value of the totalAmountSmallSubawards property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setTotalAmountSmallSubawards(java.math.BigDecimal value);

    /**
     * Gets the value of the awardAmount property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getAwardAmount();

    /**
     * Sets the value of the awardAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    void setAwardAmount(java.math.BigDecimal value);

}
