//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 47)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COI_DISCLOSURE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="COI_DISC_DETAILS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="MODULE_CODE" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="MODULE_ITEM_KEY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENTITY_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ENTITY_SEQUENCE_NUMBER" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element ref="{}COI_ENTITY_STATUS_CODE"/>
 *         &lt;element name="DESCRIPTION" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="UPDATE_TIMESTAMP" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="UPDATE_USER" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface COIDISCDETAILSType {


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
     * Gets the value of the entitynumber property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    java.lang.String getENTITYNUMBER();

    /**
     * Sets the value of the entitynumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.lang.String}
     */
    void setENTITYNUMBER(java.lang.String value);

    /**
     * Gets the value of the coidiscdetailsnumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getCOIDISCDETAILSNUMBER();

    /**
     * Sets the value of the coidiscdetailsnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setCOIDISCDETAILSNUMBER(java.math.BigDecimal value);

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
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDESCRIPTION();

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDESCRIPTION(java.lang.String value);

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
     * Gets the value of the entitysequencenumber property.
     * 
     * @return
     *     possible object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getENTITYSEQUENCENUMBER();

    /**
     * Sets the value of the entitysequencenumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link null}
     *     {@link java.math.BigDecimal}
     */
    void setENTITYSEQUENCENUMBER(java.math.BigDecimal value);

    /**
     * Gets the value of the coientitystatuscode property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType}
     *     {@link edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODE}
     */
    edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType getCOIENTITYSTATUSCODE();

    /**
     * Sets the value of the coientitystatuscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType}
     *     {@link edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODE}
     */
    void setCOIENTITYSTATUSCODE(edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType value);

    /**
     * Gets the value of the modulecode property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    java.math.BigDecimal getMODULECODE();

    /**
     * Sets the value of the modulecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     *     {@link null}
     */
    void setMODULECODE(java.math.BigDecimal value);

    /**
     * Gets the value of the moduleitemkey property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    java.lang.String getMODULEITEMKEY();

    /**
     * Sets the value of the moduleitemkey property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link null}
     */
    void setMODULEITEMKEY(java.lang.String value);

}
