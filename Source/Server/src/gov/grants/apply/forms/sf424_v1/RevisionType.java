//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/SF424-V1.0.xsd line 344)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}RevisionCode1" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}RevisionCode2" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424-V1.0}RevisionOtherExplanation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RevisionType {


    /**
     * Gets the value of the revisionCode2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRevisionCode2();

    /**
     * Sets the value of the revisionCode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRevisionCode2(java.lang.String value);

    /**
     * Gets the value of the revisionCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRevisionCode1();

    /**
     * Sets the value of the revisionCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRevisionCode1(java.lang.String value);

    /**
     * Gets the value of the revisionOtherExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRevisionOtherExplanation();

    /**
     * Sets the value of the revisionOtherExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRevisionOtherExplanation(java.lang.String value);

}
