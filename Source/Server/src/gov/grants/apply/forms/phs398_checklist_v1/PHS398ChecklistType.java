//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.phs398_checklist_v1;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/PHS398_Checklist-V1.0.xsd line 11)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ApplicationType">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="New"/>
 *             &lt;enumeration value="Resubmission"/>
 *             &lt;enumeration value="Renewal"/>
 *             &lt;enumeration value="Continuation"/>
 *             &lt;enumeration value="Revision"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="FederalID" type="{http://apply.grants.gov/system/Global-V1.0}StringMin1Max30Type" minOccurs="0"/>
 *         &lt;element name="IsChangeOfPDPI" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="FormerPD_Name" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}HumanNameDataType" minOccurs="0"/>
 *         &lt;element name="IsChangeOfInstitution" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="FormerInstitutionName" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}OrganizationNameDataType" minOccurs="0"/>
 *         &lt;element name="IsInventionsAndPatents" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="IsPreviouslyReported" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="ProgramIncome" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}YesNoDataType"/>
 *         &lt;element name="IncomeBudgetPeriod" maxOccurs="5" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BudgetPeriod">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                       &lt;minInclusive value="1"/>
 *                       &lt;maxInclusive value="5"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="AnticipatedAmount" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}BudgetAmountDataType"/>
 *                   &lt;element name="Source">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="150"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CertificationExplanation" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Certifications" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
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
public interface PHS398ChecklistType {


    /**
     * Gets the value of the formerInstitutionName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFormerInstitutionName();

    /**
     * Sets the value of the formerInstitutionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFormerInstitutionName(java.lang.String value);

    /**
     * Gets the value of the programIncome property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProgramIncome();

    /**
     * Sets the value of the programIncome property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProgramIncome(java.lang.String value);

    /**
     * Gets the value of the isPreviouslyReported property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsPreviouslyReported();

    /**
     * Sets the value of the isPreviouslyReported property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsPreviouslyReported(java.lang.String value);

    /**
     * Gets the value of the isChangeOfPDPI property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsChangeOfPDPI();

    /**
     * Sets the value of the isChangeOfPDPI property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsChangeOfPDPI(java.lang.String value);

    /**
     * Gets the value of the federalID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFederalID();

    /**
     * Sets the value of the federalID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFederalID(java.lang.String value);

    /**
     * Gets the value of the isChangeOfInstitution property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsChangeOfInstitution();

    /**
     * Sets the value of the isChangeOfInstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsChangeOfInstitution(java.lang.String value);

    /**
     * Gets the value of the IncomeBudgetPeriod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the IncomeBudgetPeriod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncomeBudgetPeriod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.phs398_checklist_v1.PHS398ChecklistType.IncomeBudgetPeriodType}
     * 
     */
    java.util.List getIncomeBudgetPeriod();

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
     * Gets the value of the isInventionsAndPatents property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getIsInventionsAndPatents();

    /**
     * Sets the value of the isInventionsAndPatents property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setIsInventionsAndPatents(java.lang.String value);

    /**
     * Gets the value of the applicationType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicationType();

    /**
     * Sets the value of the applicationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicationType(java.lang.String value);

    /**
     * Gets the value of the formerPDName property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v1.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v1.HumanNameDataType getFormerPDName();

    /**
     * Sets the value of the formerPDName property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v1.HumanNameDataType}
     */
    void setFormerPDName(gov.grants.apply.system.globallibrary_v1.HumanNameDataType value);

    /**
     * Gets the value of the certificationExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_checklist_v1.PHS398ChecklistType.CertificationExplanationType}
     */
    gov.grants.apply.forms.phs398_checklist_v1.PHS398ChecklistType.CertificationExplanationType getCertificationExplanation();

    /**
     * Sets the value of the certificationExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_checklist_v1.PHS398ChecklistType.CertificationExplanationType}
     */
    void setCertificationExplanation(gov.grants.apply.forms.phs398_checklist_v1.PHS398ChecklistType.CertificationExplanationType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/PHS398_Checklist-V1.0.xsd line 56)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Certifications" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface CertificationExplanationType {


        /**
         * Gets the value of the certifications property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        gov.grants.apply.system.attachments_v1.AttachedFileDataType getCertifications();

        /**
         * Sets the value of the certifications property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
         */
        void setCertifications(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply.grants.gov/forms/schemas/PHS398_Checklist-V1.0.xsd line 33)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BudgetPeriod">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *             &lt;minInclusive value="1"/>
     *             &lt;maxInclusive value="5"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="AnticipatedAmount" type="{http://apply.grants.gov/system/GlobalLibrary-V1.0}BudgetAmountDataType"/>
     *         &lt;element name="Source">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="150"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface IncomeBudgetPeriodType {


        /**
         * Gets the value of the anticipatedAmount property.
         * 
         * @return
         *     possible object is
         *     {@link java.math.BigDecimal}
         */
        java.math.BigDecimal getAnticipatedAmount();

        /**
         * Sets the value of the anticipatedAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.math.BigDecimal}
         */
        void setAnticipatedAmount(java.math.BigDecimal value);

        /**
         * Gets the value of the budgetPeriod property.
         * 
         */
        int getBudgetPeriod();

        /**
         * Sets the value of the budgetPeriod property.
         * 
         */
        void setBudgetPeriod(int value);

        /**
         * Gets the value of the source property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getSource();

        /**
         * Sets the value of the source property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setSource(java.lang.String value);

    }

}
