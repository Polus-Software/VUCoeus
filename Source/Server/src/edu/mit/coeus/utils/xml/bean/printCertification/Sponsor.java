//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.07.12 at 03:16:29 PM IST 
//


package edu.mit.coeus.utils.xml.bean.printCertification;


/**
 * Java content class for sponsor complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/D:/VSS/SVN/coeusSource/Branches/4.4/Printing/schemas/printCertification.xsd line 23)
 * <p>
 * <pre>
 * &lt;complexType name="sponsor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sponsorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sponsorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sponsorType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface Sponsor {


    /**
     * Gets the value of the sponsorName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSponsorName();

    /**
     * Sets the value of the sponsorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSponsorName(java.lang.String value);

    /**
     * Gets the value of the sponsorType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSponsorType();

    /**
     * Sets the value of the sponsorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSponsorType(java.lang.String value);

    /**
     * Gets the value of the sponsorCode property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSponsorCode();

    /**
     * Sets the value of the sponsorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSponsorCode(java.lang.String value);

}
