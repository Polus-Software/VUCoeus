//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v2;


/**
 * Java content class for CFDADataType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/system/schemas/GlobalLibrary-V2.0.xsd line 124)
 * <p>
 * <pre>
 * &lt;complexType name="CFDADataType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CFDA_Number" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDANumberDataType"/>
 *         &lt;element name="CFDA_Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDATitleDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CFDADataType {


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
     * Gets the value of the cfdaTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCFDATitle();

    /**
     * Sets the value of the cfdaTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCFDATitle(java.lang.String value);

}