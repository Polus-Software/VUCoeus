//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sflll_v1;


/**
 * Java content class for AwardeeDataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SFLLL-V1.0.xsd line 179)
 * <p>
 * <pre>
 * &lt;complexType name="AwardeeDataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/system/GlobalLibrary-V1.0}OrganizationName"/>
 *         &lt;element name="Address" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}AddressDataType"/>
 *         &lt;element ref="{http://apply.grants.gov/system/GlobalLibrary-V1.0}CongressionalDistrict" minOccurs="0"/>
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
     *     {@link gov.grants.apply.system.globallibrary_v1.AddressDataType}
     */
    gov.grants.apply.system.globallibrary_v1.AddressDataType getAddress();

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v1.AddressDataType}
     */
    void setAddress(gov.grants.apply.system.globallibrary_v1.AddressDataType value);

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
