//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/forms/schemas/SF424A-V1.0.xsd line 249)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}OtherDirectChargesExplanation" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}OtherIndirectChargesExplanation" minOccurs="0"/>
 *         &lt;element ref="{http://apply.grants.gov/forms/SF424A-V1.0}Remarks" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface OtherInformationType {


    /**
     * Gets the value of the otherIndirectChargesExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOtherIndirectChargesExplanation();

    /**
     * Sets the value of the otherIndirectChargesExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOtherIndirectChargesExplanation(java.lang.String value);

    /**
     * Gets the value of the otherDirectChargesExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOtherDirectChargesExplanation();

    /**
     * Sets the value of the otherDirectChargesExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOtherDirectChargesExplanation(java.lang.String value);

    /**
     * Gets the value of the remarks property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRemarks();

    /**
     * Sets the value of the remarks property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRemarks(java.lang.String value);

}
