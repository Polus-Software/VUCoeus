//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.header_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/system/schemas/Header-V1.0.xsd line 18)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/system/Global-V1.0}HashValue"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}AgencyName" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}CFDANumber" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}ActivityTitle" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}OpportunityID"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}OpportunityTitle" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}CompetitionID" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}OpeningDate" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}ClosingDate" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/system/Header-V1.0}SubmissionTitle" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://apply.grants.gov/system/Global-V1.0}schemaVersion use="required" fixed="1.0""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GrantSubmissionHeaderType {


    /**
     * Gets the value of the competitionID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCompetitionID();

    /**
     * Sets the value of the competitionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCompetitionID(java.lang.String value);

    /**
     * Gets the value of the activityTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActivityTitle();

    /**
     * Sets the value of the activityTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActivityTitle(java.lang.String value);

    /**
     * Gets the value of the opportunityTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOpportunityTitle();

    /**
     * Sets the value of the opportunityTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOpportunityTitle(java.lang.String value);

    /**
     * Gets the value of the cfdaNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCFDANumber();

    /**
     * Sets the value of the cfdaNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCFDANumber(java.lang.String value);

    /**
     * Gets the value of the closingDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getClosingDate();

    /**
     * Sets the value of the closingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setClosingDate(java.util.Calendar value);

    /**
     * Gets the value of the agencyName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAgencyName();

    /**
     * Sets the value of the agencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAgencyName(java.lang.String value);

    /**
     * Gets the value of the schemaVersion property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    java.lang.String getSchemaVersion();

    /**
     * Sets the value of the schemaVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    void setSchemaVersion(java.lang.String value);

    /**
     * Gets the value of the openingDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getOpeningDate();

    /**
     * Sets the value of the openingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setOpeningDate(java.util.Calendar value);

    /**
     * Gets the value of the submissionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSubmissionTitle();

    /**
     * Sets the value of the submissionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSubmissionTitle(java.lang.String value);

    /**
     * Gets the value of the opportunityID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOpportunityID();

    /**
     * Sets the value of the opportunityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOpportunityID(java.lang.String value);

    /**
     * Gets the value of the hashValue property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.global_v1.HashValueType}
     *     {@link gov.grants.apply.system.global_v1.HashValue}
     */
    gov.grants.apply.system.global_v1.HashValueType getHashValue();

    /**
     * Sets the value of the hashValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.global_v1.HashValueType}
     *     {@link gov.grants.apply.system.global_v1.HashValue}
     */
    void setHashValue(gov.grants.apply.system.global_v1.HashValueType value);

}
