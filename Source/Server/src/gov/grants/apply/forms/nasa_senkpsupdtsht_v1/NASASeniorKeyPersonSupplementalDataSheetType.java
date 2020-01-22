//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nasa_senkpsupdtsht_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NASA_SeniorKeyPersonSupplementalDataSheet-V1.0.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Senior_Key_Person" type="{http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0}Senior_Key_PersonType" maxOccurs="8"/>
 *         &lt;element name="Attachment1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Attachment2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Attachment3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Attachment4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SeniorKeyPersonAttachment" type="{http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0}SeniorKeyPersonAttachmentType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NASASeniorKeyPersonSupplementalDataSheetType {


    /**
     * Gets the value of the attachment2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAttachment2();

    /**
     * Sets the value of the attachment2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAttachment2(java.lang.String value);

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
     * Gets the value of the SeniorKeyPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the SeniorKeyPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeniorKeyPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonType}
     * 
     */
    java.util.List getSeniorKeyPerson();

    /**
     * Gets the value of the seniorKeyPersonAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonAttachmentType}
     */
    gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonAttachmentType getSeniorKeyPersonAttachment();

    /**
     * Sets the value of the seniorKeyPersonAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonAttachmentType}
     */
    void setSeniorKeyPersonAttachment(gov.grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonAttachmentType value);

    /**
     * Gets the value of the attachment3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAttachment3();

    /**
     * Sets the value of the attachment3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAttachment3(java.lang.String value);

    /**
     * Gets the value of the attachment1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAttachment1();

    /**
     * Sets the value of the attachment1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAttachment1(java.lang.String value);

    /**
     * Gets the value of the attachment4 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAttachment4();

    /**
     * Sets the value of the attachment4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAttachment4(java.lang.String value);

}