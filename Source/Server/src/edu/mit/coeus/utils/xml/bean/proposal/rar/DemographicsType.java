//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 579)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BirthDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="Gender" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}GenderType"/>
 *         &lt;element name="Ethnicity" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}EthnicityType"/>
 *         &lt;element name="Race" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}RaceType"/>
 *         &lt;element name="CitizenshipStatus" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CitizenshipType"/>
 *         &lt;element name="CountryOfCitizenship" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="DisabilityStatus" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}DisabilityStatusType"/>
 *         &lt;element name="DisabilityCategory" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}DisabilityType" minOccurs="0"/>
 *         &lt;element name="DisabilityDescription" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface DemographicsType {


    /**
     * Gets the value of the disabilityCategory property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDisabilityCategory();

    /**
     * Sets the value of the disabilityCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDisabilityCategory(java.lang.String value);

    /**
     * Gets the value of the countryOfCitizenship property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCountryOfCitizenship();

    /**
     * Sets the value of the countryOfCitizenship property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCountryOfCitizenship(java.lang.String value);

    /**
     * Gets the value of the race property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRace();

    /**
     * Sets the value of the race property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRace(java.lang.String value);

    /**
     * Gets the value of the citizenshipStatus property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCitizenshipStatus();

    /**
     * Sets the value of the citizenshipStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCitizenshipStatus(java.lang.String value);

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getBirthDate();

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setBirthDate(java.util.Calendar value);

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getGender();

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setGender(java.lang.String value);

    /**
     * Gets the value of the disabilityStatus property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDisabilityStatus();

    /**
     * Sets the value of the disabilityStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDisabilityStatus(java.lang.String value);

    /**
     * Gets the value of the ethnicity property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEthnicity();

    /**
     * Sets the value of the ethnicity property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEthnicity(java.lang.String value);

    /**
     * Gets the value of the disabilityDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDisabilityDescription();

    /**
     * Sets the value of the disabilityDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDisabilityDescription(java.lang.String value);

}