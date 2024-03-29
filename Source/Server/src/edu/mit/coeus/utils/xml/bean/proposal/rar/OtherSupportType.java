//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 615)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}PersonFullNameType"/>
 *         &lt;element name="ProjectTitle" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="Location" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}PostalAddressType"/>
 *         &lt;element name="Source" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="TotalAwardAmount" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}CurrencyType"/>
 *         &lt;element name="PeriodStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="PeriodEndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="PersonMonths" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="AppointmentCategory" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/common.namespace}AppointmentCategoryType"/>
 *         &lt;element name="SupportFileIdentifier" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="SupportStatus" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface OtherSupportType {


    /**
     * Gets the value of the periodStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getPeriodStartDate();

    /**
     * Sets the value of the periodStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setPeriodStartDate(java.util.Calendar value);

    /**
     * Gets the value of the periodEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getPeriodEndDate();

    /**
     * Sets the value of the periodEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setPeriodEndDate(java.util.Calendar value);

    /**
     * Gets the value of the totalAwardAmount property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getTotalAwardAmount();

    /**
     * Sets the value of the totalAwardAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setTotalAwardAmount(java.math.BigDecimal value);

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSource();

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSource(java.lang.String value);

    /**
     * Identifies a file that describes the other support for this person.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSupportFileIdentifier();

    /**
     * Identifies a file that describes the other support for this person.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSupportFileIdentifier(java.lang.String value);

    /**
     * Gets the value of the supportStatus property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSupportStatus();

    /**
     * Sets the value of the supportStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSupportStatus(java.lang.String value);

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFirstName();

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFirstName(java.lang.String value);

    /**
     * Gets the value of the appointmentCategory property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAppointmentCategory();

    /**
     * Sets the value of the appointmentCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAppointmentCategory(java.lang.String value);

    /**
     * Gets the value of the projectTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProjectTitle();

    /**
     * Sets the value of the projectTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProjectTitle(java.lang.String value);

    /**
     * Gets the value of the personMonths property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getPersonMonths();

    /**
     * Sets the value of the personMonths property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setPersonMonths(java.math.BigInteger value);

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getMiddleName();

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setMiddleName(java.lang.String value);

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType getLocation();

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType}
     */
    void setLocation(edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType value);

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getLastName();

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setLastName(java.lang.String value);

}
