//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:24 CST 
//


package gov.grants.apply.forms.phs398_researchplan_v3_0;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_researchplan_v3_0 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(49, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_researchplan_v3_0.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_researchplan_v3_0.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SpecificAimsType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.SpecificAimsTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.HumanNameDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.FileLocationType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SelectAgentResearchType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.SelectAgentResearchTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.VertebrateAnimalsType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.VertebrateAnimalsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ConsortiumContractualArrangementsType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ConsortiumContractualArrangementsTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType.class), "gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValueType.class), "gov.grants.apply.system.global_v1.impl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AddressDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin1Max100DataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfWomenAndMinoritiesType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.InclusionOfWomenAndMinoritiesTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.IntroductionToApplicationType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.IntroductionToApplicationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.MultiplePDPILeadershipPlanType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.MultiplePDPILeadershipPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResourceSharingPlansType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ResourceSharingPlansTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachedFileDataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfChildrenType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.InclusionOfChildrenTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.HashValueType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30 .class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30Impl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProtectionOfHumanSubjectsType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ProtectionOfHumanSubjectsTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.MultipleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.MultipleAttachmentDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.CFDADataType.class), "gov.grants.apply.system.globallibrary_v2.impl.CFDADataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin0Max100DataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProgressReportPublicationListType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ProgressReportPublicationListTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.LettersOfSupportType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.LettersOfSupportTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.SingleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.SingleAttachmentDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.FormVersionIdentifier.class), "gov.grants.apply.system.global_v1.impl.FormVersionIdentifierImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.ContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.DataSafetyMonitoringPlanType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.DataSafetyMonitoringPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResearchStrategyType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ResearchStrategyTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.KeyBiologicalAndOrChemicalResourcesType.class), "gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.KeyBiologicalAndOrChemicalResourcesTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValue.class), "gov.grants.apply.system.global_v1.impl.HashValueImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_ResearchPlan_3_0-V3.0", "PHS398_ResearchPlan_3_0"), (gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30 .class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "HashValue"), (gov.grants.apply.system.global_v1.HashValue.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "FormVersionIdentifier"), (gov.grants.apply.system.global_v1.FormVersionIdentifier.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_researchplan_v3_0
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
     * @param name
     *     the name of the property to retrieve
     * @param value
     *     the value of the property to be set
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value)
        throws javax.xml.bind.PropertyException
    {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType createPHS398ResearchPlan30TypeResearchPlanAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSpecificAimsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SpecificAimsType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSpecificAimsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.SpecificAimsTypeImpl();
    }

    /**
     * Create an instance of HumanNameDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.HumanNameDataType createHumanNameDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl();
    }

    /**
     * Create an instance of GlobalAttachedFileDataTypeFileLocationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.FileLocationType createGlobalAttachedFileDataTypeFileLocationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSelectAgentResearchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SelectAgentResearchType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSelectAgentResearchType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.SelectAgentResearchTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeVertebrateAnimalsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.VertebrateAnimalsType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeVertebrateAnimalsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.VertebrateAnimalsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeConsortiumContractualArrangementsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ConsortiumContractualArrangementsType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeConsortiumContractualArrangementsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ConsortiumContractualArrangementsTypeImpl();
    }

    /**
     * Create an instance of AttachedFileDataTypeFileLocationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType createAttachedFileDataTypeFileLocationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl();
    }

    /**
     * Create an instance of HashValueType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.global_v1.HashValueType createHashValueType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.global_v1.impl.HashValueTypeImpl();
    }

    /**
     * Create an instance of AddressDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.AddressDataType createAddressDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl();
    }

    /**
     * Create an instance of AttachmentGroupMin1Max100DataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType createAttachmentGroupMin1Max100DataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin1Max100DataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type createPHS398ResearchPlan30Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfWomenAndMinoritiesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfWomenAndMinoritiesType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfWomenAndMinoritiesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.InclusionOfWomenAndMinoritiesTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeIntroductionToApplicationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.IntroductionToApplicationType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeIntroductionToApplicationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.IntroductionToApplicationTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeMultiplePDPILeadershipPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.MultiplePDPILeadershipPlanType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeMultiplePDPILeadershipPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.MultiplePDPILeadershipPlanTypeImpl();
    }

    /**
     * Create an instance of GlobalAttachedFileDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.AttachedFileDataType createGlobalAttachedFileDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResourceSharingPlansType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResourceSharingPlansType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResourceSharingPlansType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ResourceSharingPlansTypeImpl();
    }

    /**
     * Create an instance of AttachedFileDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.attachments_v1.AttachedFileDataType createAttachedFileDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfChildrenType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfChildrenType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfChildrenType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.InclusionOfChildrenTypeImpl();
    }

    /**
     * Create an instance of GlobalAttachedFileDataTypeHashValueType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.HashValueType createGlobalAttachedFileDataTypeHashValueType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.HashValueTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30 createPHS398ResearchPlan30()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30Impl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProtectionOfHumanSubjectsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProtectionOfHumanSubjectsType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProtectionOfHumanSubjectsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ProtectionOfHumanSubjectsTypeImpl();
    }

    /**
     * Create an instance of MultipleAttachmentDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.MultipleAttachmentDataType createMultipleAttachmentDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.MultipleAttachmentDataTypeImpl();
    }

    /**
     * Create an instance of CFDADataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.CFDADataType createCFDADataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.CFDADataTypeImpl();
    }

    /**
     * Create an instance of OrganizationDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.OrganizationDataType createOrganizationDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.OrganizationDataTypeImpl();
    }

    /**
     * Create an instance of AttachmentGroupMin0Max100DataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType createAttachmentGroupMin0Max100DataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin0Max100DataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProgressReportPublicationListType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProgressReportPublicationListType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProgressReportPublicationListType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ProgressReportPublicationListTypeImpl();
    }

    /**
     * Create an instance of OrganizationContactPersonDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType createOrganizationContactPersonDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeLettersOfSupportType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.LettersOfSupportType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeLettersOfSupportType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.LettersOfSupportTypeImpl();
    }

    /**
     * Create an instance of SingleAttachmentDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.SingleAttachmentDataType createSingleAttachmentDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.SingleAttachmentDataTypeImpl();
    }

    /**
     * Create an instance of FormVersionIdentifier
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.global_v1.FormVersionIdentifier createFormVersionIdentifier()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.global_v1.impl.FormVersionIdentifierImpl();
    }

    /**
     * Create an instance of FormVersionIdentifier
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.global_v1.FormVersionIdentifier createFormVersionIdentifier(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.global_v1.impl.FormVersionIdentifierImpl(value);
    }

    /**
     * Create an instance of ContactPersonDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.globallibrary_v2.ContactPersonDataType createContactPersonDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeDataSafetyMonitoringPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.DataSafetyMonitoringPlanType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeDataSafetyMonitoringPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.DataSafetyMonitoringPlanTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResearchStrategyType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResearchStrategyType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResearchStrategyType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.ResearchStrategyTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchPlan30TypeResearchPlanAttachmentsTypeKeyBiologicalAndOrChemicalResourcesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchplan_v3_0.PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.KeyBiologicalAndOrChemicalResourcesType createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeKeyBiologicalAndOrChemicalResourcesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchplan_v3_0.impl.PHS398ResearchPlan30TypeImpl.ResearchPlanAttachmentsTypeImpl.KeyBiologicalAndOrChemicalResourcesTypeImpl();
    }

    /**
     * Create an instance of HashValue
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.global_v1.HashValue createHashValue()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.global_v1.impl.HashValueImpl();
    }

}
