//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nsf_deviationauthorization_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_DeviationAuthorization-V1.1.xsd line 6)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="DeviationAuthorization" type="{http://apply.grants.gov/forms/NSF_DeviationAuthorization-V1-1}StringMin0Max4000" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NSFDeviationAuthorizationType {


    /**
     * Gets the value of the formVersion property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    java.lang.String getFormVersion();

    /**
     * Sets the value of the formVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     *     {@link java.lang.String}
     */
    void setFormVersion(java.lang.String value);

    /**
     * Gets the value of the deviationAuthorization property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDeviationAuthorization();

    /**
     * Sets the value of the deviationAuthorization property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDeviationAuthorization(java.lang.String value);

}