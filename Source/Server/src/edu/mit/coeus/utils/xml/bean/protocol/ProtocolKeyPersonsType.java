//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol;


/**
 * Java content class for ProtocolKeyPersonsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/E:/Share/3091/ProtocolSummary.xsd line 363)
 * <p>
 * <pre>
 * &lt;complexType name="ProtocolKeyPersonsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PersonId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PersonName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PersonRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NonEmployeeFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TrainingFlag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AffiliationTypeCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AffiliationTypeDesc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UpdateTimestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UpdateUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolKeyPersonsType {


    /**
     * Gets the value of the nonEmployeeFlag property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getNonEmployeeFlag();

    /**
     * Sets the value of the nonEmployeeFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setNonEmployeeFlag(java.lang.String value);

    /**
     * Gets the value of the affiliationTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAffiliationTypeDesc();

    /**
     * Sets the value of the affiliationTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAffiliationTypeDesc(java.lang.String value);

    /**
     * Gets the value of the trainingFlag property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTrainingFlag();

    /**
     * Sets the value of the trainingFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTrainingFlag(java.lang.String value);

    /**
     * Gets the value of the personName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPersonName();

    /**
     * Sets the value of the personName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPersonName(java.lang.String value);

    /**
     * Gets the value of the personId property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPersonId();

    /**
     * Sets the value of the personId property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPersonId(java.lang.String value);

    /**
     * Gets the value of the updateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getUpdateTimestamp();

    /**
     * Sets the value of the updateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setUpdateTimestamp(java.util.Calendar value);

    /**
     * Gets the value of the protocolNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolNumber();

    /**
     * Sets the value of the protocolNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolNumber(java.lang.String value);

    /**
     * Gets the value of the sequenceNumber property.
     * 
     */
    int getSequenceNumber();

    /**
     * Sets the value of the sequenceNumber property.
     * 
     */
    void setSequenceNumber(int value);

    /**
     * Gets the value of the affiliationTypeCode property.
     * 
     */
    int getAffiliationTypeCode();

    /**
     * Sets the value of the affiliationTypeCode property.
     * 
     */
    void setAffiliationTypeCode(int value);

    /**
     * Gets the value of the updateUser property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUpdateUser();

    /**
     * Sets the value of the updateUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUpdateUser(java.lang.String value);

    /**
     * Gets the value of the personRole property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPersonRole();

    /**
     * Sets the value of the personRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPersonRole(java.lang.String value);

}
