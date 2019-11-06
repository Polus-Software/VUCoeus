//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.performancesite_1_2_v1_2;


/**
 * Java content class for SiteLocationDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PerformanceSite_1_2-V1.2.xsd line 15)
 * <p>
 * <pre>
 * &lt;complexType name="SiteLocationDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Individual" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *         &lt;element name="DUNSNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *         &lt;element name="CongressionalDistrictProgramProject" minOccurs="0">
 *           &lt;restriction base="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CongressionalDistrictDataType">
 *             &lt;minLength value="6"/>
 *             &lt;maxLength value="6"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SiteLocationDataType {


    /**
     * Gets the value of the congressionalDistrictProgramProject property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrictProgramProject();

    /**
     * Sets the value of the congressionalDistrictProgramProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrictProgramProject(java.lang.String value);

    /**
     * Gets the value of the dunsNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDUNSNumber();

    /**
     * Sets the value of the dunsNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDUNSNumber(java.lang.String value);

    /**
     * Gets the value of the individual property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIndividual();

    /**
     * Sets the value of the individual property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIndividual(java.lang.String value);

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    gov.grants.apply.system.globallibrary_v2.AddressDataType getAddress();

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    void setAddress(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

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

}
