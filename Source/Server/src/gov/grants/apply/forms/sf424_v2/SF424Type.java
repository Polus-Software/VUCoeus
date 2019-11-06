//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424_v2;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at http://apply07.grants.gov/apply/forms/schemas/SF424-V2.0.xsd line 19)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubmissionType">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Preapplication"/>
 *             &lt;enumeration value="Application"/>
 *             &lt;enumeration value="Changed/Corrected Application"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ApplicationType">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="New"/>
 *             &lt;enumeration value="Continuation"/>
 *             &lt;enumeration value="Revision"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="RevisionType" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="A: Increase Award"/>
 *             &lt;enumeration value="B: Decrease Award"/>
 *             &lt;enumeration value="C: Increase Duration"/>
 *             &lt;enumeration value="D: Decrease Duration"/>
 *             &lt;enumeration value="E: Other (specify)"/>
 *             &lt;enumeration value="AC: Increase Award, Increase Duration"/>
 *             &lt;enumeration value="AD: Increase Award, Decrease Duration"/>
 *             &lt;enumeration value="BC: Decrease Award, Increase Duration"/>
 *             &lt;enumeration value="BD: Decrease Award, Decrease Duration"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="RevisionOtherSpecify" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="21"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="DateReceived" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ApplicantID" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantIDDataType" minOccurs="0"/>
 *         &lt;element name="FederalEntityIdentifier" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FederalIDDataType" minOccurs="0"/>
 *         &lt;element name="FederalAwardIdentifier" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ProjectAwardNumberDataType" minOccurs="0"/>
 *         &lt;element name="StateReceiveDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="StateApplicationID" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="OrganizationName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType"/>
 *         &lt;element name="EmployerTaxpayerIdentificationNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmployerIDDataType"/>
 *         &lt;element name="DUNSNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DUNSIDDataType"/>
 *         &lt;element name="Applicant" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AddressDataType"/>
 *         &lt;element name="DepartmentName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DepartmentNameDataType" minOccurs="0"/>
 *         &lt;element name="DivisionName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}DivisionNameDataType" minOccurs="0"/>
 *         &lt;element name="ContactPerson" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType" minOccurs="0"/>
 *         &lt;element name="Title" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType" minOccurs="0"/>
 *         &lt;element name="OrganizationAffiliation" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OrganizationNameDataType" minOccurs="0"/>
 *         &lt;element name="PhoneNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *         &lt;element name="Fax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *         &lt;element name="Email" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType"/>
 *         &lt;element name="ApplicantTypeCode1" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType"/>
 *         &lt;element name="ApplicantTypeCode2" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType" minOccurs="0"/>
 *         &lt;element name="ApplicantTypeCode3" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ApplicantTypeCodeDataType" minOccurs="0"/>
 *         &lt;element name="ApplicantTypeOtherSpecify" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="30"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="AgencyName" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}AgencyNameDataType"/>
 *         &lt;element name="CFDANumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDANumberDataType" minOccurs="0"/>
 *         &lt;element name="CFDAProgramTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CFDATitleDataType" minOccurs="0"/>
 *         &lt;element name="FundingOpportunityNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityIDDataType"/>
 *         &lt;element name="FundingOpportunityTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}OpportunityTitleDataType"/>
 *         &lt;element name="CompetitionIdentificationNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CompetitionIDDataType" minOccurs="0"/>
 *         &lt;element name="CompetitionIdentificationTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CompetitionTitleDataType" minOccurs="0"/>
 *         &lt;element name="AffectedAreas" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="250"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="ProjectTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}ProjectTitleDataType"/>
 *         &lt;element name="AdditionalProjectTitle" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachmentGroupMin0Max100DataType" minOccurs="0"/>
 *         &lt;element name="CongressionalDistrictApplicant" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CongressionalDistrictDataType"/>
 *         &lt;element name="CongressionalDistrictProgramProject" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}CongressionalDistrictDataType"/>
 *         &lt;element name="AdditionalCongressionalDistricts" type="{http://apply.grants.gov/system/Attachments-V1.0}AttachedFileDataType" minOccurs="0"/>
 *         &lt;element name="ProjectStartDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="ProjectEndDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="FederalEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="ApplicantEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="StateEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="LocalEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="OtherEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="ProgramIncomeEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetAmountDataType"/>
 *         &lt;element name="TotalEstimatedFunding" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}BudgetTotalAmountDataType"/>
 *         &lt;element name="StateReview">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="a. This application was made available to the State under the Executive Order 12372 Process for review on"/>
 *             &lt;enumeration value="b. Program is subject to E.O. 12372 but has not been selected by the State for review."/>
 *             &lt;enumeration value="c. Program is not covered by E.O. 12372."/>
 *           &lt;/restriction>
 *         &lt;/element>
 *         &lt;element name="StateReviewAvailableDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DelinquentFederalDebt" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="CertificationAgree" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}YesNoDataType"/>
 *         &lt;element name="AuthorizedRepresentative" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanNameDataType"/>
 *         &lt;element name="AuthorizedRepresentativeTitle" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}HumanTitleDataType"/>
 *         &lt;element name="AuthorizedRepresentativePhoneNumber" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType"/>
 *         &lt;element name="AuthorizedRepresentativeEmail" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}EmailDataType"/>
 *         &lt;element name="AuthorizedRepresentativeFax" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}TelephoneNumberDataType" minOccurs="0"/>
 *         &lt;element name="AORSignature" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}SignatureDataType"/>
 *         &lt;element name="DateSigned" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DelinquentFederalDebtExplanation" minOccurs="0">
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="0"/>
 *             &lt;maxLength value="4000"/>
 *           &lt;/restriction>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="FormVersion" use="required" type="{http://apply.grants.gov/system/GlobalLibrary-V2.0}FormVersionDataType" fixed="2.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface SF424Type {


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
     * Gets the value of the projectEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getProjectEndDate();

    /**
     * Sets the value of the projectEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setProjectEndDate(java.util.Calendar value);

    /**
     * Gets the value of the fundingOpportunityTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFundingOpportunityTitle();

    /**
     * Sets the value of the fundingOpportunityTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFundingOpportunityTitle(java.lang.String value);

    /**
     * Gets the value of the competitionIdentificationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCompetitionIdentificationNumber();

    /**
     * Sets the value of the competitionIdentificationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCompetitionIdentificationNumber(java.lang.String value);

    /**
     * Gets the value of the federalEntityIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFederalEntityIdentifier();

    /**
     * Sets the value of the federalEntityIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFederalEntityIdentifier(java.lang.String value);

    /**
     * Gets the value of the stateReviewAvailableDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getStateReviewAvailableDate();

    /**
     * Sets the value of the stateReviewAvailableDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setStateReviewAvailableDate(java.util.Calendar value);

    /**
     * Gets the value of the stateReview property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStateReview();

    /**
     * Sets the value of the stateReview property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStateReview(java.lang.String value);

    /**
     * Gets the value of the divisionName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDivisionName();

    /**
     * Sets the value of the divisionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDivisionName(java.lang.String value);

    /**
     * Gets the value of the revisionOtherSpecify property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRevisionOtherSpecify();

    /**
     * Sets the value of the revisionOtherSpecify property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRevisionOtherSpecify(java.lang.String value);

    /**
     * Gets the value of the fax property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFax();

    /**
     * Sets the value of the fax property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFax(java.lang.String value);

    /**
     * Gets the value of the congressionalDistrictApplicant property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrictApplicant();

    /**
     * Sets the value of the congressionalDistrictApplicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrictApplicant(java.lang.String value);

    /**
     * Gets the value of the additionalProjectTitle property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType}
     */
    gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType getAdditionalProjectTitle();

    /**
     * Sets the value of the additionalProjectTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType}
     */
    void setAdditionalProjectTitle(gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType value);

    /**
     * Gets the value of the competitionIdentificationTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCompetitionIdentificationTitle();

    /**
     * Sets the value of the competitionIdentificationTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCompetitionIdentificationTitle(java.lang.String value);

    /**
     * Gets the value of the projectTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getProjectTitle();

    /**
     * Sets the value of the projectTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setProjectTitle(java.lang.String value);

    /**
     * Gets the value of the authorizedRepresentativePhoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthorizedRepresentativePhoneNumber();

    /**
     * Sets the value of the authorizedRepresentativePhoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthorizedRepresentativePhoneNumber(java.lang.String value);

    /**
     * Gets the value of the delinquentFederalDebtExplanation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDelinquentFederalDebtExplanation();

    /**
     * Sets the value of the delinquentFederalDebtExplanation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDelinquentFederalDebtExplanation(java.lang.String value);

    /**
     * Gets the value of the authorizedRepresentativeFax property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthorizedRepresentativeFax();

    /**
     * Sets the value of the authorizedRepresentativeFax property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthorizedRepresentativeFax(java.lang.String value);

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
     * Gets the value of the applicantTypeCode2 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantTypeCode2();

    /**
     * Sets the value of the applicantTypeCode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantTypeCode2(java.lang.String value);

    /**
     * Gets the value of the federalAwardIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFederalAwardIdentifier();

    /**
     * Sets the value of the federalAwardIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFederalAwardIdentifier(java.lang.String value);

    /**
     * Gets the value of the employerTaxpayerIdentificationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEmployerTaxpayerIdentificationNumber();

    /**
     * Sets the value of the employerTaxpayerIdentificationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEmployerTaxpayerIdentificationNumber(java.lang.String value);

    /**
     * Gets the value of the congressionalDistrictProgramProject property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCongressionalDistrictProgramProject();

    /**
     * Sets the value of the congressionalDistrictProgramProject property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCongressionalDistrictProgramProject(java.lang.String value);

    /**
     * Gets the value of the applicantID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantID();

    /**
     * Sets the value of the applicantID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantID(java.lang.String value);

    /**
     * Gets the value of the applicantTypeOtherSpecify property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantTypeOtherSpecify();

    /**
     * Sets the value of the applicantTypeOtherSpecify property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantTypeOtherSpecify(java.lang.String value);

    /**
     * Gets the value of the authorizedRepresentative property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getAuthorizedRepresentative();

    /**
     * Sets the value of the authorizedRepresentative property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setAuthorizedRepresentative(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

    /**
     * Gets the value of the programIncomeEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getProgramIncomeEstimatedFunding();

    /**
     * Sets the value of the programIncomeEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setProgramIncomeEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the cfdaNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCFDANumber();

    /**
     * Sets the value of the cfdaNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCFDANumber(java.lang.String value);

    /**
     * Gets the value of the dunsNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDUNSNumber();

    /**
     * Sets the value of the dunsNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDUNSNumber(java.lang.String value);

    /**
     * Gets the value of the stateReceiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getStateReceiveDate();

    /**
     * Sets the value of the stateReceiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setStateReceiveDate(java.util.Calendar value);

    /**
     * Gets the value of the dateReceived property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getDateReceived();

    /**
     * Sets the value of the dateReceived property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setDateReceived(java.util.Calendar value);

    /**
     * Gets the value of the aorSignature property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAORSignature();

    /**
     * Sets the value of the aorSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAORSignature(java.lang.String value);

    /**
     * Gets the value of the certificationAgree property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCertificationAgree();

    /**
     * Sets the value of the certificationAgree property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCertificationAgree(java.lang.String value);

    /**
     * Gets the value of the delinquentFederalDebt property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDelinquentFederalDebt();

    /**
     * Sets the value of the delinquentFederalDebt property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDelinquentFederalDebt(java.lang.String value);

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getTitle();

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setTitle(java.lang.String value);

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
     * Gets the value of the organizationAffiliation property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getOrganizationAffiliation();

    /**
     * Sets the value of the organizationAffiliation property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setOrganizationAffiliation(java.lang.String value);

    /**
     * Gets the value of the dateSigned property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getDateSigned();

    /**
     * Sets the value of the dateSigned property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setDateSigned(java.util.Calendar value);

    /**
     * Gets the value of the revisionType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getRevisionType();

    /**
     * Sets the value of the revisionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setRevisionType(java.lang.String value);

    /**
     * Gets the value of the applicantTypeCode1 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantTypeCode1();

    /**
     * Sets the value of the applicantTypeCode1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantTypeCode1(java.lang.String value);

    /**
     * Gets the value of the applicant property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    gov.grants.apply.system.globallibrary_v2.AddressDataType getApplicant();

    /**
     * Sets the value of the applicant property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.AddressDataType}
     */
    void setApplicant(gov.grants.apply.system.globallibrary_v2.AddressDataType value);

    /**
     * Gets the value of the departmentName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getDepartmentName();

    /**
     * Sets the value of the departmentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setDepartmentName(java.lang.String value);

    /**
     * Gets the value of the submissionType property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getSubmissionType();

    /**
     * Sets the value of the submissionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setSubmissionType(java.lang.String value);

    /**
     * Gets the value of the stateEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getStateEstimatedFunding();

    /**
     * Sets the value of the stateEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setStateEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the contactPerson property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    gov.grants.apply.system.globallibrary_v2.HumanNameDataType getContactPerson();

    /**
     * Sets the value of the contactPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.globallibrary_v2.HumanNameDataType}
     */
    void setContactPerson(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value);

    /**
     * Gets the value of the applicantTypeCode3 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getApplicantTypeCode3();

    /**
     * Sets the value of the applicantTypeCode3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setApplicantTypeCode3(java.lang.String value);

    /**
     * Gets the value of the localEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getLocalEstimatedFunding();

    /**
     * Sets the value of the localEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setLocalEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the totalEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getTotalEstimatedFunding();

    /**
     * Sets the value of the totalEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setTotalEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the authorizedRepresentativeEmail property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAuthorizedRepresentativeEmail();

    /**
     * Sets the value of the authorizedRepresentativeEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAuthorizedRepresentativeEmail(java.lang.String value);

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
     * Gets the value of the fundingOpportunityNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getFundingOpportunityNumber();

    /**
     * Sets the value of the fundingOpportunityNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setFundingOpportunityNumber(java.lang.String value);

    /**
     * Gets the value of the projectStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getProjectStartDate();

    /**
     * Sets the value of the projectStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setProjectStartDate(java.util.Calendar value);

    /**
     * Gets the value of the otherEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getOtherEstimatedFunding();

    /**
     * Sets the value of the otherEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setOtherEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the cfdaProgramTitle property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getCFDAProgramTitle();

    /**
     * Sets the value of the cfdaProgramTitle property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setCFDAProgramTitle(java.lang.String value);

    /**
     * Gets the value of the stateApplicationID property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getStateApplicationID();

    /**
     * Sets the value of the stateApplicationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setStateApplicationID(java.lang.String value);

    /**
     * Gets the value of the federalEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getFederalEstimatedFunding();

    /**
     * Sets the value of the federalEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setFederalEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getEmail();

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setEmail(java.lang.String value);

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getPhoneNumber();

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setPhoneNumber(java.lang.String value);

    /**
     * Gets the value of the affectedAreas property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAffectedAreas();

    /**
     * Sets the value of the affectedAreas property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAffectedAreas(java.lang.String value);

    /**
     * Gets the value of the agencyName property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String}
     */
    java.lang.String getAgencyName();

    /**
     * Sets the value of the agencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String}
     */
    void setAgencyName(java.lang.String value);

    /**
     * Gets the value of the applicantEstimatedFunding property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigDecimal}
     */
    java.math.BigDecimal getApplicantEstimatedFunding();

    /**
     * Sets the value of the applicantEstimatedFunding property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.math.BigDecimal}
     */
    void setApplicantEstimatedFunding(java.math.BigDecimal value);

    /**
     * Gets the value of the additionalCongressionalDistricts property.
     * 
     * @return
     *     possible object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    gov.grants.apply.system.attachments_v1.AttachedFileDataType getAdditionalCongressionalDistricts();

    /**
     * Sets the value of the additionalCongressionalDistricts property.
     * 
     * @param value
     *     allowed object is
     *     {@link gov.grants.apply.system.attachments_v1.AttachedFileDataType}
     */
    void setAdditionalCongressionalDistricts(gov.grants.apply.system.attachments_v1.AttachedFileDataType value);

}
