//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_personaldata_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RR_PersonalData-V1.1.xsd line 13)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProjectDirector" type="{http://apply.grants.gov/forms/RR_PersonalData-V1-1}DirectorType"/>
 *         &lt;element name="Co-ProjectDirector" type="{http://apply.grants.gov/forms/RR_PersonalData-V1-1}DirectorType" maxOccurs="4" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RRPersonalDataType {


    /**
     * Gets the value of the projectDirector property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_personaldata_v1_1.DirectorType}
     */
    gov.grants.apply.forms.rr_personaldata_v1_1.DirectorType getProjectDirector();

    /**
     * Sets the value of the projectDirector property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_personaldata_v1_1.DirectorType}
     */
    void setProjectDirector(gov.grants.apply.forms.rr_personaldata_v1_1.DirectorType value);

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
     * Gets the value of the CoProjectDirector property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the CoProjectDirector property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCoProjectDirector().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.rr_personaldata_v1_1.DirectorType}
     * 
     */
    java.util.List getCoProjectDirector();

}
