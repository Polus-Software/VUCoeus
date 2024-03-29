//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.common;


/**
 * Java content class for PostalAddressType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/commontypes.xsd line 75)
 * <p>
 * <pre>
 * &lt;complexType name="PostalAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Street" type="{http://www.w3.org/2001/XMLSchema}token" maxOccurs="4" minOccurs="0"/>
 *         &lt;element name="MailStopCode" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="State" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}StateCodeType" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}PostalCodeType"/>
 *         &lt;element name="Country" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CountryCodeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PostalAddressType {


    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPostalCode();

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPostalCode(java.lang.String value);

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
     * Gets the value of the mailStopCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMailStopCode();

    /**
     * Sets the value of the mailStopCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMailStopCode(java.lang.String value);

    /**
     * Gets the value of the Street property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Street property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link java.lang.String}
     * 
     */
    java.util.List getStreet();

}
