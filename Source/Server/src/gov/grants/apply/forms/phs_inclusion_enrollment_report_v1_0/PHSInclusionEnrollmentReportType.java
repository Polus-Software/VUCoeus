//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:39 CST 
//


package gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS_Inclusion_Enrollment_Report-V1.0.xsd line 6)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Study" maxOccurs="150">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="StudyTitle" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_String1_250DataType"/>
 *                   &lt;element name="DelayedOnsetStudy" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *                   &lt;element name="EnrollmentType" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Planned"/>
 *                       &lt;enumeration value="Cumulative (Actual)"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="ExistingDatasetOrResource" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="EnrollmentLocation" minOccurs="0">
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Domestic"/>
 *                       &lt;enumeration value="Foreign"/>
 *                     &lt;/restriction>
 *                   &lt;/element>
 *                   &lt;element name="ClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="PhaseIIIClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
 *                   &lt;element name="Comments" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_String1_500DataType" minOccurs="0"/>
 *                   &lt;element name="NotHispanic" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
 *                   &lt;element name="Hispanic" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
 *                   &lt;element name="UnknownEthnicity" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
 *                   &lt;element name="Total" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_TotalsDataType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="1.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface PHSInclusionEnrollmentReportType {


    /**
     * Gets the value of the Study property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the Study property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStudy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportType.StudyType}
     * 
     */
    java.util.List getStudy();

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
     * Java content class for anonymous complex type.
     * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/Petitforms/schema/PHS_Inclusion_Enrollment_Report-V1.0.xsd line 9)
     * <p>
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="StudyTitle" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_String1_250DataType"/>
     *         &lt;element name="DelayedOnsetStudy" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
     *         &lt;element name="EnrollmentType" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Planned"/>
     *             &lt;enumeration value="Cumulative (Actual)"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="ExistingDatasetOrResource" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="EnrollmentLocation" minOccurs="0">
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Domestic"/>
     *             &lt;enumeration value="Foreign"/>
     *           &lt;/restriction>
     *         &lt;/element>
     *         &lt;element name="ClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="PhaseIIIClinicalTrial" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType" minOccurs="0"/>
     *         &lt;element name="Comments" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_String1_500DataType" minOccurs="0"/>
     *         &lt;element name="NotHispanic" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
     *         &lt;element name="Hispanic" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
     *         &lt;element name="UnknownEthnicity" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_EthnicCategoryDataType" minOccurs="0"/>
     *         &lt;element name="Total" type="{http://apply.grants.gov/forms/PHS_Inclusion_Enrollment_Report-V1.0}PHS_Inclusion_Enrollment_Report_TotalsDataType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     */
    public interface StudyType {


        /**
         * Gets the value of the phaseIIIClinicalTrial property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getPhaseIIIClinicalTrial();

        /**
         * Sets the value of the phaseIIIClinicalTrial property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setPhaseIIIClinicalTrial(java.lang.String value);

        /**
         * Gets the value of the delayedOnsetStudy property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getDelayedOnsetStudy();

        /**
         * Sets the value of the delayedOnsetStudy property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setDelayedOnsetStudy(java.lang.String value);

        /**
         * Gets the value of the comments property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getComments();

        /**
         * Sets the value of the comments property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setComments(java.lang.String value);

        /**
         * Gets the value of the clinicalTrial property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getClinicalTrial();

        /**
         * Sets the value of the clinicalTrial property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setClinicalTrial(java.lang.String value);

        /**
         * Gets the value of the enrollmentLocation property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getEnrollmentLocation();

        /**
         * Sets the value of the enrollmentLocation property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setEnrollmentLocation(java.lang.String value);

        /**
         * Gets the value of the existingDatasetOrResource property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getExistingDatasetOrResource();

        /**
         * Sets the value of the existingDatasetOrResource property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setExistingDatasetOrResource(java.lang.String value);

        /**
         * Gets the value of the notHispanic property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType getNotHispanic();

        /**
         * Sets the value of the notHispanic property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        void setNotHispanic(gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType value);

        /**
         * Gets the value of the studyTitle property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getStudyTitle();

        /**
         * Sets the value of the studyTitle property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setStudyTitle(java.lang.String value);

        /**
         * Gets the value of the unknownEthnicity property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType getUnknownEthnicity();

        /**
         * Sets the value of the unknownEthnicity property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        void setUnknownEthnicity(gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType value);

        /**
         * Gets the value of the hispanic property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType getHispanic();

        /**
         * Sets the value of the hispanic property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType}
         */
        void setHispanic(gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType value);

        /**
         * Gets the value of the enrollmentType property.
         * 
         * @return
         *     possible object is
         *     {@link java.lang.String}
         */
        java.lang.String getEnrollmentType();

        /**
         * Sets the value of the enrollmentType property.
         * 
         * @param value
         *     allowed object is
         *     {@link java.lang.String}
         */
        void setEnrollmentType(java.lang.String value);

        /**
         * Gets the value of the total property.
         * 
         * @return
         *     possible object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportTotalsDataType}
         */
        gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportTotalsDataType getTotal();

        /**
         * Sets the value of the total property.
         * 
         * @param value
         *     allowed object is
         *     {@link gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportTotalsDataType}
         */
        void setTotal(gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportTotalsDataType value);

    }

}
