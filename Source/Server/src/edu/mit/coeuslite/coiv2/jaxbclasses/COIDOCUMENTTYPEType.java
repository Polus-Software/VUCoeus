//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/checkouts/Server/src/edu/mit/coeuslite/coiv2/schema/disclosure.xsd line 89)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="COI_DOCUMENT_TYPE_CODE" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
public interface COIDOCUMENTTYPEType {


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
     * Gets the value of the coidocumenttypecode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCOIDOCUMENTTYPECODE();

    /**
     * Sets the value of the coidocumenttypecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCOIDOCUMENTTYPECODE(java.lang.String value);

}
