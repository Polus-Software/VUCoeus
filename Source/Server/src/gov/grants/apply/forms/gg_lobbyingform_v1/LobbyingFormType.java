//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.gg_lobbyingform_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/GG_LobbyingForm-V1.0.xsd line 7)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApplicantName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}OrganizationNameDataType"/>
 *         &lt;element name="AuthorizedRepresentativeName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanNameDataType"/>
 *         &lt;element name="AuthorizedRepresentativeTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanTitleDataType"/>
 *         &lt;element name="AuthorizedRepresentativeSignature" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}SignatureDataType"/>
 *         &lt;element name="SubmittedDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersion use="required" fixed="1.0""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface LobbyingFormType {


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
     * Gets the value of the applicantName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantName();

    /**
     * Sets the value of the applicantName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantName(java.lang.String value);

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
     * Gets the value of the authorizedRepresentativeName property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v1.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v1.HumanNameDataType getAuthorizedRepresentativeName();

    /**
     * Sets the value of the authorizedRepresentativeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v1.HumanNameDataType}
     */
    void setAuthorizedRepresentativeName(gov.grants.apply.system.globallibrary_v1.HumanNameDataType value);

}
