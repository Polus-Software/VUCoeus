//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 7)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CoverSheet">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckCoverSheet" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckRenewal" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckFullApp" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckTypeApp" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckAppCert" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CheckRRSite" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="CheckRROtherInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="CheckProjectSummary" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="ProjectNarrative">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckProjectNarrative" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckMeritReview" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckPriorSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckHRInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckURL" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CheckBiblio" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="CheckFacilities" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="Equipment">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckEquipment" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckSuppDoc" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckAdditionalItems" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="RRSrProfile">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckRRSrProfile" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckBioSketch" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckCurrentPendingSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CheckRRPersonalData" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="RRBudget">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckRRBudget" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckRRBudgetJustification" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="NSFCover">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CheckNSFCover" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckNSFUnit" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="CheckNSFOtherInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckNSFSFLLL" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckNSFDevAuth" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckNSFReg" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                   &lt;element name="CheckDoNotInclude" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.2" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface NSFApplicationChecklist12Type {


    /**
     * Gets the value of the checkProjectSummary property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckProjectSummary();

    /**
     * Sets the value of the checkProjectSummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckProjectSummary(java.lang.String value);

    /**
     * Gets the value of the nsfCover property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.NSFCoverType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.NSFCoverType getNSFCover();

    /**
     * Sets the value of the nsfCover property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.NSFCoverType}
     */
    void setNSFCover(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.NSFCoverType value);

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
     * Gets the value of the checkFacilities property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckFacilities();

    /**
     * Sets the value of the checkFacilities property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckFacilities(java.lang.String value);

    /**
     * Gets the value of the checkRRSite property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckRRSite();

    /**
     * Sets the value of the checkRRSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckRRSite(java.lang.String value);

    /**
     * Gets the value of the rrBudget property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRBudgetType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRBudgetType getRRBudget();

    /**
     * Sets the value of the rrBudget property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRBudgetType}
     */
    void setRRBudget(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRBudgetType value);

    /**
     * Gets the value of the checkRRPersonalData property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckRRPersonalData();

    /**
     * Sets the value of the checkRRPersonalData property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckRRPersonalData(java.lang.String value);

    /**
     * Gets the value of the coverSheet property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.CoverSheetType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.CoverSheetType getCoverSheet();

    /**
     * Sets the value of the coverSheet property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.CoverSheetType}
     */
    void setCoverSheet(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.CoverSheetType value);

    /**
     * Gets the value of the projectNarrative property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.ProjectNarrativeType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.ProjectNarrativeType getProjectNarrative();

    /**
     * Sets the value of the projectNarrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.ProjectNarrativeType}
     */
    void setProjectNarrative(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.ProjectNarrativeType value);

    /**
     * Gets the value of the checkBiblio property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckBiblio();

    /**
     * Sets the value of the checkBiblio property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckBiblio(java.lang.String value);

    /**
     * Gets the value of the equipment property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.EquipmentType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.EquipmentType getEquipment();

    /**
     * Sets the value of the equipment property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.EquipmentType}
     */
    void setEquipment(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.EquipmentType value);

    /**
     * Gets the value of the checkRROtherInfo property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCheckRROtherInfo();

    /**
     * Sets the value of the checkRROtherInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCheckRROtherInfo(java.lang.String value);

    /**
     * Gets the value of the rrSrProfile property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRSrProfileType}
     */
    gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRSrProfileType getRRSrProfile();

    /**
     * Sets the value of the rrSrProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRSrProfileType}
     */
    void setRRSrProfile(gov.grants.apply.forms.nsf_applicationchecklist_1_2_v1_2.NSFApplicationChecklist12Type.RRSrProfileType value);


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 10)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckCoverSheet" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckRenewal" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckFullApp" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckTypeApp" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckAppCert" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface CoverSheetType {


        /**
         * Gets the value of the checkCoverSheet property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckCoverSheet();

        /**
         * Sets the value of the checkCoverSheet property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckCoverSheet(java.lang.String value);

        /**
         * Gets the value of the checkFullApp property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckFullApp();

        /**
         * Sets the value of the checkFullApp property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckFullApp(java.lang.String value);

        /**
         * Gets the value of the checkRenewal property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckRenewal();

        /**
         * Sets the value of the checkRenewal property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckRenewal(java.lang.String value);

        /**
         * Gets the value of the checkTypeApp property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckTypeApp();

        /**
         * Sets the value of the checkTypeApp property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckTypeApp(java.lang.String value);

        /**
         * Gets the value of the checkAppCert property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckAppCert();

        /**
         * Sets the value of the checkAppCert property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckAppCert(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 37)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckEquipment" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckSuppDoc" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckAdditionalItems" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface EquipmentType {


        /**
         * Gets the value of the checkAdditionalItems property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckAdditionalItems();

        /**
         * Sets the value of the checkAdditionalItems property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckAdditionalItems(java.lang.String value);

        /**
         * Gets the value of the checkSuppDoc property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckSuppDoc();

        /**
         * Sets the value of the checkSuppDoc property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckSuppDoc(java.lang.String value);

        /**
         * Gets the value of the checkEquipment property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckEquipment();

        /**
         * Sets the value of the checkEquipment property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckEquipment(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 64)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckNSFCover" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckNSFUnit" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckNSFOtherInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckNSFSFLLL" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckNSFDevAuth" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckNSFReg" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckDoNotInclude" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface NSFCoverType {


        /**
         * Gets the value of the checkDoNotInclude property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckDoNotInclude();

        /**
         * Sets the value of the checkDoNotInclude property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckDoNotInclude(java.lang.String value);

        /**
         * Gets the value of the checkNSFSFLLL property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFSFLLL();

        /**
         * Sets the value of the checkNSFSFLLL property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFSFLLL(java.lang.String value);

        /**
         * Gets the value of the checkNSFDevAuth property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFDevAuth();

        /**
         * Sets the value of the checkNSFDevAuth property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFDevAuth(java.lang.String value);

        /**
         * Gets the value of the checkNSFOtherInfo property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFOtherInfo();

        /**
         * Sets the value of the checkNSFOtherInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFOtherInfo(java.lang.String value);

        /**
         * Gets the value of the checkNSFCover property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFCover();

        /**
         * Sets the value of the checkNSFCover property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFCover(java.lang.String value);

        /**
         * Gets the value of the checkNSFUnit property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFUnit();

        /**
         * Sets the value of the checkNSFUnit property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFUnit(java.lang.String value);

        /**
         * Gets the value of the checkNSFReg property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckNSFReg();

        /**
         * Sets the value of the checkNSFReg property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckNSFReg(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 24)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckProjectNarrative" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckMeritReview" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckPriorSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckHRInfo" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *         &lt;element name="CheckURL" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface ProjectNarrativeType {


        /**
         * Gets the value of the checkMeritReview property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckMeritReview();

        /**
         * Sets the value of the checkMeritReview property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckMeritReview(java.lang.String value);

        /**
         * Gets the value of the checkURL property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckURL();

        /**
         * Sets the value of the checkURL property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckURL(java.lang.String value);

        /**
         * Gets the value of the checkProjectNarrative property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckProjectNarrative();

        /**
         * Sets the value of the checkProjectNarrative property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckProjectNarrative(java.lang.String value);

        /**
         * Gets the value of the checkHRInfo property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckHRInfo();

        /**
         * Sets the value of the checkHRInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckHRInfo(java.lang.String value);

        /**
         * Gets the value of the checkPriorSupport property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckPriorSupport();

        /**
         * Sets the value of the checkPriorSupport property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckPriorSupport(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 56)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckRRBudget" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckRRBudgetJustification" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoNotApplicableDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface RRBudgetType {


        /**
         * Gets the value of the checkRRBudgetJustification property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckRRBudgetJustification();

        /**
         * Sets the value of the checkRRBudgetJustification property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckRRBudgetJustification(java.lang.String value);

        /**
         * Gets the value of the checkRRBudget property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckRRBudget();

        /**
         * Sets the value of the checkRRBudget property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckRRBudget(java.lang.String value);

    }


    /**
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/s2s/build/cust/NSF_ApplicationChecklist_1_2-V1.2.xsd line 46)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CheckRRSrProfile" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckBioSketch" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="CheckCurrentPendingSupport" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface RRSrProfileType {


        /**
         * Gets the value of the checkCurrentPendingSupport property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckCurrentPendingSupport();

        /**
         * Sets the value of the checkCurrentPendingSupport property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckCurrentPendingSupport(java.lang.String value);

        /**
         * Gets the value of the checkRRSrProfile property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckRRSrProfile();

        /**
         * Sets the value of the checkRRSrProfile property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckRRSrProfile(java.lang.String value);

        /**
         * Gets the value of the checkBioSketch property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getCheckBioSketch();

        /**
         * Sets the value of the checkBioSketch property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setCheckBioSketch(java.lang.String value);

    }

}
