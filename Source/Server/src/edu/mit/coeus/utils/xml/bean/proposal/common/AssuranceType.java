//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.common;


/**
 * Java content class for AssuranceType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/commontypes.xsd line 104)
 * <p>
 * <pre>
 * &lt;complexType name="AssuranceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="YesNoAnswer" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Explanation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface AssuranceType {


    /**
     * Gets the value of the explanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getExplanation();

    /**
     * Sets the value of the explanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setExplanation(java.lang.String value);

    /**
     * Gets the value of the yesNoAnswer property.
     * 
     */
    boolean isYesNoAnswer();

    /**
     * Sets the value of the yesNoAnswer property.
     * 
     */
    void setYesNoAnswer(boolean value);

}