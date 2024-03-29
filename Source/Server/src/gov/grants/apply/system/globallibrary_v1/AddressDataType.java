//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v1;


/**
 * Java content class for AddressDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/system/schemas/GlobalLibrary-V1.0.xsd line 77)
 * <p>
 * <pre>
 * &lt;complexType name="AddressDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Street1" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max55Type"/>
 *         &lt;element name="Street2" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max55Type" minOccurs="0"/>
 *         &lt;element name="City" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max35Type"/>
 *         &lt;element name="County" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *         &lt;element name="State" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *         &lt;element name="ZipCode" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *         &lt;element name="Country" type="{http://apply.grants.gov/system/UniversalCodes-V1.0}CountryCodeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AddressDataType {


    /**
     * Gets the value of the street2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStreet2();

    /**
     * Sets the value of the street2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStreet2(java.lang.String value);

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getState();

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setState(java.lang.String value);

    /**
     * Gets the value of the zipCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getZipCode();

    /**
     * Sets the value of the zipCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setZipCode(java.lang.String value);

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCountry();

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCountry(java.lang.String value);

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCity();

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCity(java.lang.String value);

    /**
     * Gets the value of the street1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStreet1();

    /**
     * Sets the value of the street1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStreet1(java.lang.String value);

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCounty();

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCounty(java.lang.String value);

}
