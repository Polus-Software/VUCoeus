//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for CoreStateReceiptQualifiersType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 108)
 * <p>
 * <pre>
 * &lt;complexType name="CoreStateReceiptQualifiersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StateReceiptDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="StateApplicationIdentifier" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CoreStateReceiptQualifiersType {


    /**
     * Gets the value of the stateApplicationIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStateApplicationIdentifier();

    /**
     * Sets the value of the stateApplicationIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStateApplicationIdentifier(java.lang.String value);

    /**
     * Gets the value of the stateReceiptDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getStateReceiptDate();

    /**
     * Sets the value of the stateReceiptDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setStateReceiptDate(java.util.Calendar value);

}
