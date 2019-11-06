//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for KeyPersonType complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 251)
 * <p>
 * <pre>
 * &lt;complexType name="KeyPersonType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ContactPersonType"/>
 *         &lt;sequence>
 *           &lt;element name="PositionTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="OrganizationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="OrganizationDivision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="OrganizationDepartment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="AuthenticationCredential" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="SocialSecurityNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="BiographicalSketch" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="keyPersonFlag" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="keyPersonFlagCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                     &lt;element name="keyPersonFlagDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface KeyPersonType {


    /**
     * Gets the value of the authenticationCredential property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthenticationCredential();

    /**
     * Sets the value of the authenticationCredential property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthenticationCredential(java.lang.String value);

    /**
     * Gets the value of the keyPersonFlag property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.KeyPersonType.KeyPersonFlagType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.KeyPersonType.KeyPersonFlagType getKeyPersonFlag();

    /**
     * Sets the value of the keyPersonFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.KeyPersonType.KeyPersonFlagType}
     */
    void setKeyPersonFlag(edu.mit.coeus.utils.xml.bean.proposal.rar.KeyPersonType.KeyPersonFlagType value);

    /**
     * Gets the value of the biographicalSketch property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getBiographicalSketch();

    /**
     * Sets the value of the biographicalSketch property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setBiographicalSketch(java.lang.String value);

    /**
     * Gets the value of the organizationDivision property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationDivision();

    /**
     * Sets the value of the organizationDivision property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationDivision(java.lang.String value);

    /**
     * Gets the value of the positionTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPositionTitle();

    /**
     * Sets the value of the positionTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPositionTitle(java.lang.String value);

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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType getName();

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType}
     */
    void setName(edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType value);

    /**
     * Gets the value of the contactInformation property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType getContactInformation();

    /**
     * Sets the value of the contactInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType}
     */
    void setContactInformation(edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType value);

    /**
     * Gets the value of the socialSecurityNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSocialSecurityNumber();

    /**
     * Sets the value of the socialSecurityNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSocialSecurityNumber(java.lang.String value);

    /**
     * Gets the value of the organizationDepartment property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationDepartment();

    /**
     * Sets the value of the organizationDepartment property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationDepartment(java.lang.String value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 263)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="keyPersonFlagCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="keyPersonFlagDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface KeyPersonFlagType {


        /**
         * Gets the value of the keyPersonFlagDesc property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getKeyPersonFlagDesc();

        /**
         * Sets the value of the keyPersonFlagDesc property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setKeyPersonFlagDesc(java.lang.String value);

        /**
         * Gets the value of the keyPersonFlagCode property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getKeyPersonFlagCode();

        /**
         * Sets the value of the keyPersonFlagCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setKeyPersonFlagCode(java.lang.String value);

    }

}
