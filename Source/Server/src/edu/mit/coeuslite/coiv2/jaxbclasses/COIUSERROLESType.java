//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 144)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COI_DISCLOSURE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="ROLE_ID" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="USER_ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UPDATE_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UPDATE_USER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface COIUSERROLESType {


    /**
     * Gets the value of the updatetimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getUPDATETIMESTAMP();

    /**
     * Sets the value of the updatetimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setUPDATETIMESTAMP(java.util.Calendar value);

    /**
     * Gets the value of the updateuser property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUPDATEUSER();

    /**
     * Sets the value of the updateuser property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUPDATEUSER(java.lang.String value);

    /**
     * Gets the value of the sequencenumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getSEQUENCENUMBER();

    /**
     * Sets the value of the sequencenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setSEQUENCENUMBER(java.math.BigDecimal value);

    /**
     * Gets the value of the roleid property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getROLEID();

    /**
     * Sets the value of the roleid property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setROLEID(java.math.BigDecimal value);

    /**
     * Gets the value of the coidisclosurenumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCOIDISCLOSURENUMBER();

    /**
     * Sets the value of the coidisclosurenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCOIDISCLOSURENUMBER(java.lang.String value);

    /**
     * Gets the value of the userid property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getUSERID();

    /**
     * Sets the value of the userid property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setUSERID(java.lang.String value);

}