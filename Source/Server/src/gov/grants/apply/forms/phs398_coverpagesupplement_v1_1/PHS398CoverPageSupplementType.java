//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_coverpagesupplement_v1_1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_CoverPageSupplement-V1.1.xsd line 6)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PDPI">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PDPIName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *                   &lt;element name="isNewInvestigator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="Degrees" maxOccurs="3" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="10"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ClinicalTrial" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="isClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="isPhaseIIIClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ContactPersonInfo">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ContactName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *                   &lt;element name="ContactPhone" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *                   &lt;element name="ContactFax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *                   &lt;element name="ContactEmail" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType" minOccurs="0"/>
 *                   &lt;element name="ContactTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
 *                   &lt;element name="ContactAddress" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="StemCells" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="isHumanStemCellsInvolved" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="StemCellsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="CellLines" maxOccurs="20" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="4"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.1" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PHS398CoverPageSupplementType {


    /**
     * Gets the value of the pdpi property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.PDPIType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.PDPIType getPDPI();

    /**
     * Sets the value of the pdpi property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.PDPIType}
     */
    void setPDPI(gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.PDPIType value);

    /**
     * Gets the value of the stemCells property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.StemCellsType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.StemCellsType getStemCells();

    /**
     * Sets the value of the stemCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.StemCellsType}
     */
    void setStemCells(gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.StemCellsType value);

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
     * Gets the value of the contactPersonInfo property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ContactPersonInfoType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ContactPersonInfoType getContactPersonInfo();

    /**
     * Sets the value of the contactPersonInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ContactPersonInfoType}
     */
    void setContactPersonInfo(gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ContactPersonInfoType value);

    /**
     * Gets the value of the clinicalTrial property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ClinicalTrialType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ClinicalTrialType getClinicalTrial();

    /**
     * Sets the value of the clinicalTrial property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ClinicalTrialType}
     */
    void setClinicalTrial(gov.grants.apply.forms.phs398_coverpagesupplement_v1_1.PHS398CoverPageSupplementType.ClinicalTrialType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_CoverPageSupplement-V1.1.xsd line 25)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="isClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="isPhaseIIIClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ClinicalTrialType {


        /**
         * Gets the value of the isClinicalTrial property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsClinicalTrial();

        /**
         * Sets the value of the isClinicalTrial property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsClinicalTrial(java.lang.String value);

        /**
         * Gets the value of the isPhaseIIIClinicalTrial property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsPhaseIIIClinicalTrial();

        /**
         * Sets the value of the isPhaseIIIClinicalTrial property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsPhaseIIIClinicalTrial(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_CoverPageSupplement-V1.1.xsd line 33)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ContactName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
     *         &lt;element name="ContactPhone" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
     *         &lt;element name="ContactFax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
     *         &lt;element name="ContactEmail" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType" minOccurs="0"/>
     *         &lt;element name="ContactTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
     *         &lt;element name="ContactAddress" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ContactPersonInfoType {


        /**
         * Gets the value of the contactAddress property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
         */
        gov.grants.apply.system.globallibrary_v2.AddressDataType getContactAddress();

        /**
         * Sets the value of the contactAddress property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
         */
        void setContactAddress(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

        /**
         * Gets the value of the contactEmail property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getContactEmail();

        /**
         * Sets the value of the contactEmail property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setContactEmail(java.lang.String value);

        /**
         * Gets the value of the contactTitle property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getContactTitle();

        /**
         * Sets the value of the contactTitle property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setContactTitle(java.lang.String value);

        /**
         * Gets the value of the contactFax property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getContactFax();

        /**
         * Sets the value of the contactFax property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setContactFax(java.lang.String value);

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
         * Gets the value of the contactPhone property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getContactPhone();

        /**
         * Sets the value of the contactPhone property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setContactPhone(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_CoverPageSupplement-V1.1.xsd line 9)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="PDPIName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
     *         &lt;element name="isNewInvestigator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="Degrees" maxOccurs="3" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="10"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface PDPIType {


        /**
         * Gets the value of the Degrees property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the Degrees property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getDegrees().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link java.lang.String}
         * 
         */
        java.util.List getDegrees();

        /**
         * Gets the value of the pdpiName property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
         */
        gov.grants.apply.system.globallibrary_v2.HumanNameDataType getPDPIName();

        /**
         * Sets the value of the pdpiName property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
         */
        void setPDPIName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

        /**
         * Gets the value of the isNewInvestigator property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsNewInvestigator();

        /**
         * Sets the value of the isNewInvestigator property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsNewInvestigator(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/PHS398_CoverPageSupplement-V1.1.xsd line 45)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="isHumanStemCellsInvolved" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="StemCellsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="CellLines" maxOccurs="20" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="4"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface StemCellsType {


        /**
         * Gets the value of the isHumanStemCellsInvolved property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getIsHumanStemCellsInvolved();

        /**
         * Sets the value of the isHumanStemCellsInvolved property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setIsHumanStemCellsInvolved(java.lang.String value);

        /**
         * Gets the value of the CellLines property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the CellLines property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCellLines().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link java.lang.String}
         * 
         */
        java.util.List getCellLines();

        /**
         * Gets the value of the stemCellsIndicator property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getStemCellsIndicator();

        /**
         * Sets the value of the stemCellsIndicator property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setStemCellsIndicator(java.lang.String value);

    }

}
