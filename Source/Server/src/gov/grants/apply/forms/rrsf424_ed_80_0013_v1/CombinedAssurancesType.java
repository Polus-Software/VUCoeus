//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.18 at 12:00:15 EDT 
//


package gov.grants.apply.forms.rrsf424_ed_80_0013_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/RRSF424_ED_80_0013-V1_1.xsd line 7)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExplanationAttachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *         &lt;element name="PerformanceLocation" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType" minOccurs="0"/>
 *         &lt;element name="AdditionalLocation" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *         &lt;element name="AuthorizedRepresentativeName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *         &lt;element name="AuthorizedRepresentativeTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
 *         &lt;element name="AuthorizedRepresentativeSignature" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SignatureDataType"/>
 *         &lt;element name="SignedDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface CombinedAssurancesType {


    /**
     * Gets the value of the authorizedRepresentativeTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthorizedRepresentativeTitle();

    /**
     * Sets the value of the authorizedRepresentativeTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthorizedRepresentativeTitle(java.lang.String value);

    /**
     * Gets the value of the performanceLocation property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    gov.grants.apply.system.globallibrary_v2.AddressDataType getPerformanceLocation();

    /**
     * Sets the value of the performanceLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    void setPerformanceLocation(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

    /**
     * Gets the value of the authorizedRepresentativeSignature property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthorizedRepresentativeSignature();

    /**
     * Sets the value of the authorizedRepresentativeSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthorizedRepresentativeSignature(java.lang.String value);

    /**
     * Gets the value of the signedDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getSignedDate();

    /**
     * Sets the value of the signedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setSignedDate(java.util.Calendar value);

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
     * Gets the value of the explanationAttachment property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    gov.grants.apply.system.attachments_v1.AttachedFileDataType getExplanationAttachment();

    /**
     * Sets the value of the explanationAttachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    void setExplanationAttachment(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    /**
     * Gets the value of the authorizedRepresentativeName property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getAuthorizedRepresentativeName();

    /**
     * Sets the value of the authorizedRepresentativeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setAuthorizedRepresentativeName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

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

    /**
     * Gets the value of the additionalLocation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAdditionalLocation();

    /**
     * Sets the value of the additionalLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAdditionalLocation(java.lang.String value);

}