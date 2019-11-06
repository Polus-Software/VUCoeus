//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/Coeus%204.4/Printing/correspondenceTemplates/schema/irb.xsd line 479)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProtocolNumber">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="SequenceNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ProtocolTypeCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ProtocolTypeDesc">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="200"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ProtocolStatusCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ProtocolStatusDesc">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="200"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ProtocolTitle">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="2000"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ProtocolDescription" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="2000"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ApplicationDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ApprovalDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="BillableFlag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="FdaApplicationNumber" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="15"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="RefNumber1" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="50"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="RefNumber2" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;maxLength value="50"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ProtocolMasterDataType {


    /**
     * Gets the value of the protocolTypeDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolTypeDesc();

    /**
     * Sets the value of the protocolTypeDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolTypeDesc(java.lang.String value);

    /**
     * Gets the value of the protocolStatusDesc property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolStatusDesc();

    /**
     * Sets the value of the protocolStatusDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolStatusDesc(java.lang.String value);

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
     * Gets the value of the refNumber2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getRefNumber2();

    /**
     * Sets the value of the refNumber2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setRefNumber2(java.lang.String value);

    /**
     * Gets the value of the refNumber1 property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getRefNumber1();

    /**
     * Sets the value of the refNumber1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setRefNumber1(java.lang.String value);

    /**
     * Gets the value of the sequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getSequenceNumber();

    /**
     * Sets the value of the sequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setSequenceNumber(java.math.BigInteger value);

    /**
     * Gets the value of the approvalDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     *     {@link null}
     */
    java.util.Calendar getApprovalDate();

    /**
     * Sets the value of the approvalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     *     {@link null}
     */
    void setApprovalDate(java.util.Calendar value);

    /**
     * Gets the value of the expirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     *     {@link null}
     */
    java.util.Calendar getExpirationDate();

    /**
     * Sets the value of the expirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     *     {@link null}
     */
    void setExpirationDate(java.util.Calendar value);

    /**
     * Gets the value of the protocolDescription property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getProtocolDescription();

    /**
     * Sets the value of the protocolDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setProtocolDescription(java.lang.String value);

    /**
     * Gets the value of the protocolTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProtocolTitle();

    /**
     * Sets the value of the protocolTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProtocolTitle(java.lang.String value);

    /**
     * Gets the value of the protocolTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getProtocolTypeCode();

    /**
     * Sets the value of the protocolTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setProtocolTypeCode(java.math.BigInteger value);

    /**
     * Gets the value of the billableFlag property.
     * 
     */
    boolean isBillableFlag();

    /**
     * Sets the value of the billableFlag property.
     * 
     */
    void setBillableFlag(boolean value);

    /**
     * Gets the value of the protocolStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger}
     */
    java.math.BigInteger getProtocolStatusCode();

    /**
     * Sets the value of the protocolStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger}
     */
    void setProtocolStatusCode(java.math.BigInteger value);

    /**
     * Gets the value of the applicationDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getApplicationDate();

    /**
     * Sets the value of the applicationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setApplicationDate(java.util.Calendar value);

    /**
     * Gets the value of the fdaApplicationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getFdaApplicationNumber();

    /**
     * Sets the value of the fdaApplicationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setFdaApplicationNumber(java.lang.String value);

}
