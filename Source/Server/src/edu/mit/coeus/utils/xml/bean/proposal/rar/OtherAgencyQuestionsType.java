//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for OtherAgencyQuestionsType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 182)
 * <p>
 * <pre>
 * &lt;complexType name="OtherAgencyQuestionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OtherAgencyIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OtherAgencyNames" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface OtherAgencyQuestionsType {


    /**
     * Gets the value of the otherAgencyIndicator property.
     * 
     */
    boolean isOtherAgencyIndicator();

    /**
     * Sets the value of the otherAgencyIndicator property.
     * 
     */
    void setOtherAgencyIndicator(boolean value);

    /**
     * Gets the value of the otherAgencyNames property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOtherAgencyNames();

    /**
     * Sets the value of the otherAgencyNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOtherAgencyNames(java.lang.String value);

}