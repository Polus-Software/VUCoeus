//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.ed_sf424_supplement_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/ED_SF424_Supplement-V1.1.xsd line 11)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ProjectDirector" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ContactPersonDataType"/>
 *         &lt;element name="IsNoviceApplicant" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType" minOccurs="0"/>
 *         &lt;element name="IsHumanResearch" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="IsHumanResearchExempt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="ExemptionsNumber" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max255Type">
 *                 &lt;attribute name="IsHumanResearchExempt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AssuranceNumber" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max255Type">
 *                 &lt;attribute name="IsHumanResearchExempt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Attachment" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface EDSF424SupplementType {


    /**
     * Gets the value of the projectDirector property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.ContactPersonDataType}
     */
    gov.grants.apply.system.globallibrary_v2.ContactPersonDataType getProjectDirector();

    /**
     * Sets the value of the projectDirector property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.ContactPersonDataType}
     */
    void setProjectDirector(gov.grants.apply.system.globallibrary_v2.ContactPersonDataType value);

    /**
     * Gets the value of the isHumanResearch property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsHumanResearch();

    /**
     * Sets the value of the isHumanResearch property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsHumanResearch(java.lang.String value);

    /**
     * Gets the value of the exemptionsNumber property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.ExemptionsNumberType}
     */
    gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.ExemptionsNumberType getExemptionsNumber();

    /**
     * Sets the value of the exemptionsNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.ExemptionsNumberType}
     */
    void setExemptionsNumber(gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.ExemptionsNumberType value);

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
     * Gets the value of the isNoviceApplicant property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsNoviceApplicant();

    /**
     * Sets the value of the isNoviceApplicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsNoviceApplicant(java.lang.String value);

    /**
     * Gets the value of the assuranceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.AssuranceNumberType}
     */
    gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.AssuranceNumberType getAssuranceNumber();

    /**
     * Sets the value of the assuranceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.AssuranceNumberType}
     */
    void setAssuranceNumber(gov.grants.apply.forms.ed_sf424_supplement_v1_1.EDSF424SupplementType.AssuranceNumberType value);

    /**
     * Gets the value of the isHumanResearchExempt property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsHumanResearchExempt();

    /**
     * Sets the value of the isHumanResearchExempt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsHumanResearchExempt(java.lang.String value);

    /**
     * Gets the value of the attachment property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachment();

    /**
     * Sets the value of the attachment property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    void setAttachment(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/ED_SF424_Supplement-V1.1.xsd line 27)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max255Type">
     *       &lt;attribute name="IsHumanResearchExempt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface AssuranceNumberType {


        /**
         * String - Min length 1, max length 255
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * String - Min length 1, max length 255
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

        /**
         * Gets the value of the isHumanResearchExempt property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsHumanResearchExempt();

        /**
         * Sets the value of the isHumanResearchExempt property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsHumanResearchExempt(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/ED_SF424_Supplement-V1.1.xsd line 18)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://apply.grants.gov/system/Global-V1.0>StringMin1Max255Type">
     *       &lt;attribute name="IsHumanResearchExempt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ExemptionsNumberType {


        /**
         * String - Min length 1, max length 255
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getValue();

        /**
         * String - Min length 1, max length 255
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setValue(java.lang.String value);

        /**
         * Gets the value of the isHumanResearchExempt property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsHumanResearchExempt();

        /**
         * Sets the value of the isHumanResearchExempt property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsHumanResearchExempt(java.lang.String value);

    }

}
