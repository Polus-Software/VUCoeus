//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.cd511_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/CD511-V1.1.xsd line 10)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType"/>
 *         &lt;element name="AwardNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ProjectAwardNumberDataType" minOccurs="0"/>
 *         &lt;element name="ProjectName" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="60"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ContactName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
 *         &lt;element name="Signature" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SignatureDataType"/>
 *         &lt;element name="SubmittedDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CD511Type {


    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSignature();

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSignature(java.lang.String value);

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
     * Gets the value of the submittedDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getSubmittedDate();

    /**
     * Sets the value of the submittedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setSubmittedDate(java.util.Calendar value);

    /**
     * Gets the value of the projectName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProjectName();

    /**
     * Sets the value of the projectName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProjectName(java.lang.String value);

    /**
     * Gets the value of the contactName property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getContactName();

    /**
     * Sets the value of the contactName property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setContactName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

    /**
     * Gets the value of the awardNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAwardNumber();

    /**
     * Sets the value of the awardNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAwardNumber(java.lang.String value);

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTitle();

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTitle(java.lang.String value);

    /**
     * Gets the value of the organizationName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationName();

    /**
     * Sets the value of the organizationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationName(java.lang.String value);

}