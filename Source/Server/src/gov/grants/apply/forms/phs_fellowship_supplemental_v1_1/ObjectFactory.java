//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.30 at 02:44:18 EDT 
//


package gov.grants.apply.forms.phs_fellowship_supplemental_v1_1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs_fellowship_supplemental_v1_1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
public class ObjectFactory
    extends gov.grants.apply.forms.attachments_v1.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(43, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResponsibleConductOfResearchType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResponsibleConductOfResearchTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfChildrenType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionOfChildrenTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11 .class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11Impl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionEnrollmentReportType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionEnrollmentReportTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProgressReportPublicationListType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ProgressReportPublicationListTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfWomenAndMinoritiesType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionOfWomenAndMinoritiesTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.SupplementationFromOtherSourcesType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.SupplementationFromOtherSourcesTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.IntroductionToApplicationType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.IntroductionToApplicationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SpecificAimsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SpecificAimsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResearchStrategyType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResearchStrategyTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ApplicationTypeType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ApplicationTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.ConcurrentSupportDescriptionType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.ConcurrentSupportDescriptionTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.InstitutionalBaseSalaryType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.InstitutionalBaseSalaryTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResourceSharingPlanType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResourceSharingPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.RespectiveContributionsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.RespectiveContributionsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.FellowshipTrainingAndCareerGoalsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.FellowshipTrainingAndCareerGoalsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.TargetedPlannedEnrollmentType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.TargetedPlannedEnrollmentTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.StemCellsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.StemCellsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.DissertationAndResearchExperienceType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.DissertationAndResearchExperienceTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectionOfSponsorAndInstitutionType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SelectionOfSponsorAndInstitutionTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.GraduateDegreeSoughtType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.GraduateDegreeSoughtTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.ActivitiesPlannedUnderThisAwardType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.ActivitiesPlannedUnderThisAwardTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.FederalStipendRequestedType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.FederalStipendRequestedTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectAgentResearchType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SelectAgentResearchTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.CurrentPriorNRSASupportType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.CurrentPriorNRSASupportTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.VertebrateAnimalsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.VertebrateAnimalsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProtectionOfHumanSubjectsType.class), "gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ProtectionOfHumanSubjectsTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "PHS_Fellowship_Supplemental_1_1"), (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs_fellowship_supplemental_v1_1
     * 
     */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
     * Create an instance of the specified Java content interface.
     * 
     * @param javaContentInterface
     *     the Class object of the javacontent interface to instantiate
     * @return
     *     a new instance
     * @throws JAXBException
     *     if an error occurs
     */
    public java.lang.Object newInstance(java.lang.Class javaContentInterface)
        throws javax.xml.bind.JAXBException
    {
        return super.newInstance(javaContentInterface);
    }

    /**
     * Get the specified property. This method can only be
     * used to get provider specific properties.
     * Attempting to get an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param name
     *     the name of the property to retrieve
     * @return
     *     the value of the requested property
     * @throws PropertyException
     *     when there is an error retrieving the given property or value
     */
    public java.lang.Object getProperty(java.lang.String name)
        throws javax.xml.bind.PropertyException
    {
        return super.getProperty(name);
    }

    /**
     * Set the specified property. This method can only be
     * used to set provider specific properties.
     * Attempting to set an undefined property will result
     * in a PropertyException being thrown.
     * 
     * @param value
     *     the value of the property to be set
     * @param name
     *     the name of the property to retrieve
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value)
        throws javax.xml.bind.PropertyException
    {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResponsibleConductOfResearchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResponsibleConductOfResearchType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResponsibleConductOfResearchType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResponsibleConductOfResearchTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfChildrenType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfChildrenType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfChildrenType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionOfChildrenTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeBudgetType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType createPHSFellowshipSupplemental11TypeBudgetType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11 createPHSFellowshipSupplemental11()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11Impl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionEnrollmentReportType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionEnrollmentReportType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionEnrollmentReportType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionEnrollmentReportTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProgressReportPublicationListType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProgressReportPublicationListType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProgressReportPublicationListType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ProgressReportPublicationListTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfWomenAndMinoritiesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.InclusionOfWomenAndMinoritiesType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeInclusionOfWomenAndMinoritiesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.InclusionOfWomenAndMinoritiesTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeBudgetTypeSupplementationFromOtherSourcesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.SupplementationFromOtherSourcesType createPHSFellowshipSupplemental11TypeBudgetTypeSupplementationFromOtherSourcesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.SupplementationFromOtherSourcesTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeIntroductionToApplicationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.IntroductionToApplicationType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeIntroductionToApplicationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.IntroductionToApplicationTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSpecificAimsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SpecificAimsType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSpecificAimsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SpecificAimsTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResearchStrategyType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResearchStrategyType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResearchStrategyType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResearchStrategyTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeApplicationTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ApplicationTypeType createPHSFellowshipSupplemental11TypeApplicationTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ApplicationTypeTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeConcurrentSupportDescriptionType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.ConcurrentSupportDescriptionType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeConcurrentSupportDescriptionType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.ConcurrentSupportDescriptionTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType createPHSFellowshipSupplemental11TypeAdditionalInformationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeBudgetTypeInstitutionalBaseSalaryType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.InstitutionalBaseSalaryType createPHSFellowshipSupplemental11TypeBudgetTypeInstitutionalBaseSalaryType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.InstitutionalBaseSalaryTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResourceSharingPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ResourceSharingPlanType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeResourceSharingPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ResourceSharingPlanTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeRespectiveContributionsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.RespectiveContributionsType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeRespectiveContributionsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.RespectiveContributionsTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeFellowshipTrainingAndCareerGoalsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.FellowshipTrainingAndCareerGoalsType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeFellowshipTrainingAndCareerGoalsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.FellowshipTrainingAndCareerGoalsTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeTargetedPlannedEnrollmentType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.TargetedPlannedEnrollmentType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeTargetedPlannedEnrollmentType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.TargetedPlannedEnrollmentTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeStemCellsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.StemCellsType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeStemCellsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.StemCellsTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeDissertationAndResearchExperienceType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.DissertationAndResearchExperienceType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeDissertationAndResearchExperienceType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.DissertationAndResearchExperienceTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectionOfSponsorAndInstitutionType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectionOfSponsorAndInstitutionType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectionOfSponsorAndInstitutionType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SelectionOfSponsorAndInstitutionTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeGraduateDegreeSoughtType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.GraduateDegreeSoughtType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeGraduateDegreeSoughtType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.GraduateDegreeSoughtTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type createPHSFellowshipSupplemental11Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeActivitiesPlannedUnderThisAwardType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.ActivitiesPlannedUnderThisAwardType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeActivitiesPlannedUnderThisAwardType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.ActivitiesPlannedUnderThisAwardTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeBudgetTypeFederalStipendRequestedType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.BudgetType.FederalStipendRequestedType createPHSFellowshipSupplemental11TypeBudgetTypeFederalStipendRequestedType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.BudgetTypeImpl.FederalStipendRequestedTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType createPHSFellowshipSupplemental11TypeResearchTrainingPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectAgentResearchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.SelectAgentResearchType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeSelectAgentResearchType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.SelectAgentResearchTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeAdditionalInformationTypeCurrentPriorNRSASupportType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.AdditionalInformationType.CurrentPriorNRSASupportType createPHSFellowshipSupplemental11TypeAdditionalInformationTypeCurrentPriorNRSASupportType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.AdditionalInformationTypeImpl.CurrentPriorNRSASupportTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeVertebrateAnimalsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.VertebrateAnimalsType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeVertebrateAnimalsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.VertebrateAnimalsTypeImpl();
    }

    /**
     * Create an instance of PHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProtectionOfHumanSubjectsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11Type.ResearchTrainingPlanType.ProtectionOfHumanSubjectsType createPHSFellowshipSupplemental11TypeResearchTrainingPlanTypeProtectionOfHumanSubjectsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl.ResearchTrainingPlanTypeImpl.ProtectionOfHumanSubjectsTypeImpl();
    }

}
