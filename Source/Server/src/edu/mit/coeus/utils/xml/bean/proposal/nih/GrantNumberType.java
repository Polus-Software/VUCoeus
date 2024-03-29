//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih;


/**
 * Java content class for GrantNumberType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/nihschema.xsd line 354)
 * <p>
 * <pre>
 * &lt;complexType name="GrantNumberType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApplicationTypeCode" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="ActivityCode" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="AdministeringOrganizationID">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *             &lt;minLength value="2"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="SupportYear" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="SuffixCode" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface GrantNumberType {


    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getSerialNumber();

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setSerialNumber(java.math.BigInteger value);

    /**
     * Gets the value of the applicationTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicationTypeCode();

    /**
     * Sets the value of the applicationTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicationTypeCode(java.lang.String value);

    /**
     * Gets the value of the administeringOrganizationID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAdministeringOrganizationID();

    /**
     * Sets the value of the administeringOrganizationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAdministeringOrganizationID(java.lang.String value);

    /**
     * Gets the value of the supportYear property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getSupportYear();

    /**
     * Sets the value of the supportYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setSupportYear(java.math.BigInteger value);

    /**
     * Gets the value of the suffixCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSuffixCode();

    /**
     * Sets the value of the suffixCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSuffixCode(java.lang.String value);

    /**
     * Gets the value of the activityCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getActivityCode();

    /**
     * Sets the value of the activityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setActivityCode(java.lang.String value);

}
