//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sflll_v1_1;


/**
 * Java content class for AwardeeDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/SFLLL-V1.1.xsd line 193)
 * <p>
 * <pre>
 * &lt;complexType name="AwardeeDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType"/>
 *         &lt;element name="Address">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Street1" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}StreetDataType"/>
 *                   &lt;element name="Street2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}StreetDataType" minOccurs="0"/>
 *                   &lt;element name="City" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CityDataType"/>
 *                   &lt;element name="State" type="{http://apply.grants.gov/system/UniversalCodes-V2.0}StateCodeDataType" minOccurs="0"/>
 *                   &lt;element name="ZipPostalCode" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ZipPostalCodeDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CongressionalDistrict" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CongressionalDistrictDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AwardeeDataType {


    /**
     * Gets the value of the congressionalDistrict property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrict();

    /**
     * Sets the value of the congressionalDistrict property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrict(java.lang.String value);

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType}
     */
    gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType getAddress();

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType}
     */
    void setAddress(gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType value);

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationName();

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationName(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/SFLLL-V1.1.xsd line 197)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Street1" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}StreetDataType"/>
     *         &lt;element name="Street2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}StreetDataType" minOccurs="0"/>
     *         &lt;element name="City" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CityDataType"/>
     *         &lt;element name="State" type="{http://apply.grants.gov/system/UniversalCodes-V2.0}StateCodeDataType" minOccurs="0"/>
     *         &lt;element name="ZipPostalCode" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ZipPostalCodeDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface AddressType {


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
         * Gets the value of the zipPostalCode property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getZipPostalCode();

        /**
         * Sets the value of the zipPostalCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setZipPostalCode(java.lang.String value);

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

    }

}
