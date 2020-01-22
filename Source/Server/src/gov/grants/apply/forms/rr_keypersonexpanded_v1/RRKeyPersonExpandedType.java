//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_keypersonexpanded_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_KeyPersonExpanded-V1.0.xsd line 21)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PDPI" type="{http://apply.grants.gov/forms/RR_KeyPersonExpanded-V1.0}PersonProfileDataType"/>
 *         &lt;element name="KeyPerson" type="{http://apply.grants.gov/forms/RR_KeyPersonExpanded-V1.0}PersonProfileDataType" maxOccurs="39" minOccurs="0"/>
 *         &lt;element name="KeyPersonAttachmentFileName1" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max60Type" minOccurs="0"/>
 *         &lt;element name="KeyPersonAttachmentFileName2" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max60Type" minOccurs="0"/>
 *         &lt;element name="KeyPersonAttachmentFileName3" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max60Type" minOccurs="0"/>
 *         &lt;element name="KeyPersonAttachmentFileName4" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max60Type" minOccurs="0"/>
 *         &lt;element name="AdditionalProfilesAttached" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AdditionalProfileAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="BioSketchsAttached" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BioSketchAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="SupportsAttached" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SupportAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}FormVersionDataType" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface RRKeyPersonExpandedType {


    /**
     * Gets the value of the pdpi property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.PersonProfileDataType}
     */
    gov.grants.apply.forms.rr_keypersonexpanded_v1.PersonProfileDataType getPDPI();

    /**
     * Sets the value of the pdpi property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.PersonProfileDataType}
     */
    void setPDPI(gov.grants.apply.forms.rr_keypersonexpanded_v1.PersonProfileDataType value);

    /**
     * Gets the value of the keyPersonAttachmentFileName2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyPersonAttachmentFileName2();

    /**
     * Sets the value of the keyPersonAttachmentFileName2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyPersonAttachmentFileName2(java.lang.String value);

    /**
     * Gets the value of the keyPersonAttachmentFileName4 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyPersonAttachmentFileName4();

    /**
     * Sets the value of the keyPersonAttachmentFileName4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyPersonAttachmentFileName4(java.lang.String value);

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
     * Gets the value of the supportsAttached property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.SupportsAttachedType}
     */
    gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.SupportsAttachedType getSupportsAttached();

    /**
     * Sets the value of the supportsAttached property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.SupportsAttachedType}
     */
    void setSupportsAttached(gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.SupportsAttachedType value);

    /**
     * Gets the value of the bioSketchsAttached property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.BioSketchsAttachedType}
     */
    gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.BioSketchsAttachedType getBioSketchsAttached();

    /**
     * Sets the value of the bioSketchsAttached property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.BioSketchsAttachedType}
     */
    void setBioSketchsAttached(gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.BioSketchsAttachedType value);

    /**
     * Gets the value of the KeyPerson property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the KeyPerson property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKeyPerson().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.PersonProfileDataType}
     * 
     */
    java.util.List getKeyPerson();

    /**
     * Gets the value of the keyPersonAttachmentFileName1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyPersonAttachmentFileName1();

    /**
     * Sets the value of the keyPersonAttachmentFileName1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyPersonAttachmentFileName1(java.lang.String value);

    /**
     * Gets the value of the keyPersonAttachmentFileName3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getKeyPersonAttachmentFileName3();

    /**
     * Sets the value of the keyPersonAttachmentFileName3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setKeyPersonAttachmentFileName3(java.lang.String value);

    /**
     * Gets the value of the additionalProfilesAttached property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType}
     */
    gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType getAdditionalProfilesAttached();

    /**
     * Sets the value of the additionalProfilesAttached property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType}
     */
    void setAdditionalProfilesAttached(gov.grants.apply.forms.rr_keypersonexpanded_v1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_KeyPersonExpanded-V1.0.xsd line 30)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AdditionalProfileAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface AdditionalProfilesAttachedType {


        /**
         * Gets the value of the additionalProfileAttached property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        gov.grants.apply.system.attachments_v1.AttachedFileDataType getAdditionalProfileAttached();

        /**
         * Sets the value of the additionalProfileAttached property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        void setAdditionalProfileAttached(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_KeyPersonExpanded-V1.0.xsd line 37)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BioSketchAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface BioSketchsAttachedType {


        /**
         * Gets the value of the bioSketchAttached property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        gov.grants.apply.system.attachments_v1.AttachedFileDataType getBioSketchAttached();

        /**
         * Sets the value of the bioSketchAttached property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        void setBioSketchAttached(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/RR_KeyPersonExpanded-V1.0.xsd line 44)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SupportAttached" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface SupportsAttachedType {


        /**
         * Gets the value of the supportAttached property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        gov.grants.apply.system.attachments_v1.AttachedFileDataType getSupportAttached();

        /**
         * Sets the value of the supportAttached property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        void setSupportAttached(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    }

}