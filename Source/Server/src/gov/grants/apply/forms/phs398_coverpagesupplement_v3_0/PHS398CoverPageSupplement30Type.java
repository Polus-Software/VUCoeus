//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:53 CST 
//


package gov.grants.apply.forms.phs398_coverpagesupplement_v3_0;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CoverPageSupplement_3_0-V3.0.xsd line 6)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
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
 *         &lt;element name="VertebrateAnimals" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AnimalEuthanasiaIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="AVMAConsistentIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="EuthanasiaMethodDescription" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;minLength value="0"/>
 *                       &lt;maxLength value="1000"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ProgramIncome" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="IncomeBudgetPeriod" maxOccurs="10" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="BudgetPeriod">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                       &lt;minInclusive value="1"/>
 *                       &lt;maxInclusive value="10"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="AnticipatedAmount" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
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
 *         &lt;element name="StemCells" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="isHumanStemCellsInvolved" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="StemCellsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="CellLines" maxOccurs="200" minOccurs="0">
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
 *         &lt;element name="IsInventionsAndPatents" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="IsPreviouslyReported" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="IsChangeOfPDPI" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="FormerPD_Name" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType" minOccurs="0"/>
 *         &lt;element name="IsChangeOfInstitution" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *         &lt;element name="FormerInstitutionName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="3.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PHS398CoverPageSupplement30Type {


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
     * Gets the value of the clinicalTrial property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType getClinicalTrial();

    /**
     * Sets the value of the clinicalTrial property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType}
     */
    void setClinicalTrial(gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType value);

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
     * {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.IncomeBudgetPeriodType}
     * 
     */
    java.util.List getIncomeBudgetPeriod();

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
     * Gets the value of the stemCells property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType getStemCells();

    /**
     * Sets the value of the stemCells property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType}
     */
    void setStemCells(gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType value);

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
     * Gets the value of the formerPDName property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getFormerPDName();

    /**
     * Sets the value of the formerPDName property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setFormerPDName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

    /**
     * Gets the value of the vertebrateAnimals property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType}
     */
    gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType getVertebrateAnimals();

    /**
     * Sets the value of the vertebrateAnimals property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType}
     */
    void setVertebrateAnimals(gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CoverPageSupplement_3_0-V3.0.xsd line 9)
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

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CoverPageSupplement_3_0-V3.0.xsd line 34)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="BudgetPeriod">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
     *             &lt;minInclusive value="1"/>
     *             &lt;maxInclusive value="10"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="AnticipatedAmount" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
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

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CoverPageSupplement_3_0-V3.0.xsd line 57)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="isHumanStemCellsInvolved" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="StemCellsIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="CellLines" maxOccurs="200" minOccurs="0">
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

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS398_CoverPageSupplement_3_0-V3.0.xsd line 17)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="AnimalEuthanasiaIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="AVMAConsistentIndicator" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="EuthanasiaMethodDescription" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;minLength value="0"/>
     *             &lt;maxLength value="1000"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface VertebrateAnimalsType {


        /**
         * Gets the value of the animalEuthanasiaIndicator property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAnimalEuthanasiaIndicator();

        /**
         * Sets the value of the animalEuthanasiaIndicator property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAnimalEuthanasiaIndicator(java.lang.String value);

        /**
         * Gets the value of the euthanasiaMethodDescription property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getEuthanasiaMethodDescription();

        /**
         * Sets the value of the euthanasiaMethodDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setEuthanasiaMethodDescription(java.lang.String value);

        /**
         * Gets the value of the avmaConsistentIndicator property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getAVMAConsistentIndicator();

        /**
         * Sets the value of the avmaConsistentIndicator property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setAVMAConsistentIndicator(java.lang.String value);

    }

}
