//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar;


/**
 * Java content class for anonymous complex type.
 * <p>The following schema fragment specifies the expected content contained within this java content object. (defined at file:/C:/jwsdp-1.4/jaxb/bin/rarschema.xsd line 528)
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}CoverPageQuestionsType"/>
 *         &lt;element name="ApplicantOrganization" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ApplicantOrganizationType"/>
 *         &lt;element name="PrimaryProjectSite" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ProjectSiteType" minOccurs="0"/>
 *         &lt;element ref="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}ProgramDirectorPrincipalInvestigator"/>
 *         &lt;element name="FundingOpportunityDetails" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}FundingOpportunityDetailsType" minOccurs="0"/>
 *         &lt;element name="AuthorizedOrganizationalRepresentative" type="{http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace}AuthorizedOrganizationalRepresentativeType"/>
 *         &lt;element name="OfficialSignatureDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
public interface ResearchCoverPageType {


    /**
     * Gets the value of the applicantOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType getApplicantOrganization();

    /**
     * Sets the value of the applicantOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType}
     */
    void setApplicantOrganization(edu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantOrganizationType value);

    /**
     * Gets the value of the budgetTotals property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreBudgetTotalsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreBudgetTotalsType getBudgetTotals();

    /**
     * Sets the value of the budgetTotals property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreBudgetTotalsType}
     */
    void setBudgetTotals(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreBudgetTotalsType value);

    /**
     * Gets the value of the fundingOpportunityDetails property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.FundingOpportunityDetailsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.FundingOpportunityDetailsType getFundingOpportunityDetails();

    /**
     * Sets the value of the fundingOpportunityDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.FundingOpportunityDetailsType}
     */
    void setFundingOpportunityDetails(edu.mit.coeus.utils.xml.bean.proposal.rar.FundingOpportunityDetailsType value);

    /**
     * Gets the value of the cfdaQuestions property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreCFDAQuestionsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreCFDAQuestionsType getCFDAQuestions();

    /**
     * Sets the value of the cfdaQuestions property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreCFDAQuestionsType}
     */
    void setCFDAQuestions(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreCFDAQuestionsType value);

    /**
     * Gets the value of the officialSignatureDate property.
     * 
     * @return
     *     possible object is
     *     {@link java.util.Calendar}
     */
    java.util.Calendar getOfficialSignatureDate();

    /**
     * Sets the value of the officialSignatureDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.util.Calendar}
     */
    void setOfficialSignatureDate(java.util.Calendar value);

    /**
     * Gets the value of the applicationCategory property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicationCategoryType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicationCategoryType getApplicationCategory();

    /**
     * Sets the value of the applicationCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicationCategoryType}
     */
    void setApplicationCategory(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicationCategoryType value);

    /**
     * Gets the value of the authorizedOrganizationalRepresentative property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.AuthorizedOrganizationalRepresentativeType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.AuthorizedOrganizationalRepresentativeType getAuthorizedOrganizationalRepresentative();

    /**
     * Sets the value of the authorizedOrganizationalRepresentative property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.AuthorizedOrganizationalRepresentativeType}
     */
    void setAuthorizedOrganizationalRepresentative(edu.mit.coeus.utils.xml.bean.proposal.rar.AuthorizedOrganizationalRepresentativeType value);

    /**
     * Gets the value of the submissionCategory property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreSubmissionCategoryType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreSubmissionCategoryType getSubmissionCategory();

    /**
     * Sets the value of the submissionCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreSubmissionCategoryType}
     */
    void setSubmissionCategory(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreSubmissionCategoryType value);

    /**
     * Gets the value of the federalAgencyReceiptQualifiers property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType getFederalAgencyReceiptQualifiers();

    /**
     * Sets the value of the federalAgencyReceiptQualifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType}
     */
    void setFederalAgencyReceiptQualifiers(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType value);

    /**
     * Gets the value of the stateReceiptQualifiers property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateReceiptQualifiersType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateReceiptQualifiersType getStateReceiptQualifiers();

    /**
     * Sets the value of the stateReceiptQualifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateReceiptQualifiersType}
     */
    void setStateReceiptQualifiers(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateReceiptQualifiersType value);

    /**
     * Gets the value of the otherAgencyQuestions property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.OtherAgencyQuestionsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.OtherAgencyQuestionsType getOtherAgencyQuestions();

    /**
     * Sets the value of the otherAgencyQuestions property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.OtherAgencyQuestionsType}
     */
    void setOtherAgencyQuestions(edu.mit.coeus.utils.xml.bean.proposal.rar.OtherAgencyQuestionsType value);

    /**
     * Gets the value of the programDirectorPrincipalInvestigator property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigator}
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigatorType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigatorType getProgramDirectorPrincipalInvestigator();

    /**
     * Sets the value of the programDirectorPrincipalInvestigator property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigator}
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigatorType}
     */
    void setProgramDirectorPrincipalInvestigator(edu.mit.coeus.utils.xml.bean.proposal.rar.ProgramDirectorPrincipalInvestigatorType value);

    /**
     * Gets the value of the projectDates property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreProjectDatesType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreProjectDatesType getProjectDates();

    /**
     * Sets the value of the projectDates property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreProjectDatesType}
     */
    void setProjectDates(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreProjectDatesType value);

    /**
     * Gets the value of the primaryProjectSite property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType getPrimaryProjectSite();

    /**
     * Sets the value of the primaryProjectSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType}
     */
    void setPrimaryProjectSite(edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType value);

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
     * Gets the value of the stateIntergovernmentalReview property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateIntergovernmentalReviewType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateIntergovernmentalReviewType getStateIntergovernmentalReview();

    /**
     * Sets the value of the stateIntergovernmentalReview property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateIntergovernmentalReviewType}
     */
    void setStateIntergovernmentalReview(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreStateIntergovernmentalReviewType value);

    /**
     * Gets the value of the federalDebtDelinquencyQuestions property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalDebtDelinquencyQuestionsType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalDebtDelinquencyQuestionsType getFederalDebtDelinquencyQuestions();

    /**
     * Sets the value of the federalDebtDelinquencyQuestions property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalDebtDelinquencyQuestionsType}
     */
    void setFederalDebtDelinquencyQuestions(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalDebtDelinquencyQuestionsType value);

    /**
     * Gets the value of the applicantSubmissionQualifiers property.
     * 
     * @return
     *     possible object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicantSubmissionQualifiersType}
     */
    edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicantSubmissionQualifiersType getApplicantSubmissionQualifiers();

    /**
     * Sets the value of the applicantSubmissionQualifiers property.
     * 
     * @param value
     *     allowed object is
     *     {@link edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicantSubmissionQualifiersType}
     */
    void setApplicantSubmissionQualifiers(edu.mit.coeus.utils.xml.bean.proposal.rar.CoreApplicantSubmissionQualifiersType value);

}
