//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:33 CST 
//


package gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(48, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.HumanSubjectsType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.HumanSubjectsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataSafetyMonitoringPlanType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.DataSafetyMonitoringPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachedFileDataType.FileLocationType.class), "gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.LettersOfSupportType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.LettersOfSupportTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataTablesType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.DataTablesTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValue.class), "gov.grants.apply.system.global_v1.impl.HashValueImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ResponsibleConductOfResearchType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ResponsibleConductOfResearchTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MultiplePDPILeadershipPlanType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.MultiplePDPILeadershipPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ParticipatingFacultyBiosketchesType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ParticipatingFacultyBiosketchesTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachedFileDataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.IntroductionToApplicationType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.IntroductionToApplicationTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.FormVersionIdentifier.class), "gov.grants.apply.system.global_v1.impl.FormVersionIdentifierImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgressReportType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ProgressReportTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AddressDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin1Max100DataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.ContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgramPlanType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ProgramPlanTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.MultipleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.MultipleAttachmentDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ConsortiumContractualArrangementsType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ConsortiumContractualArrangementsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.VertebrateAnimalsType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.VertebrateAnimalsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValueType.class), "gov.grants.apply.system.global_v1.impl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.SelectAgentResearchType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.SelectAgentResearchTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType.class), "gov.grants.apply.system.attachments_v1.impl.AttachmentGroupMin0Max100DataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.HashValueType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.FileLocationType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30 .class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30Impl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.CFDADataType.class), "gov.grants.apply.system.globallibrary_v2.impl.CFDADataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.SingleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.SingleAttachmentDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MethodsForEnhancingReproducibilityType.class), "gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.MethodsForEnhancingReproducibilityTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.HumanNameDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "HashValue"), (gov.grants.apply.system.global_v1.HashValue.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "FormVersionIdentifier"), (gov.grants.apply.system.global_v1.FormVersionIdentifier.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_ResearchTrainingProgramPlan_3_0-V3.0", "PHS398_ResearchTrainingProgramPlan_3_0"), (gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeHumanSubjectsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.HumanSubjectsType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeHumanSubjectsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.HumanSubjectsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataSafetyMonitoringPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataSafetyMonitoringPlanType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataSafetyMonitoringPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.DataSafetyMonitoringPlanTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeLettersOfSupportType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.LettersOfSupportType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeLettersOfSupportType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.LettersOfSupportTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataTablesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataTablesType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataTablesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.DataTablesTypeImpl();
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

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeResponsibleConductOfResearchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ResponsibleConductOfResearchType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeResponsibleConductOfResearchType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ResponsibleConductOfResearchTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMultiplePDPILeadershipPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MultiplePDPILeadershipPlanType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMultiplePDPILeadershipPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.MultiplePDPILeadershipPlanTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeParticipatingFacultyBiosketchesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ParticipatingFacultyBiosketchesType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeParticipatingFacultyBiosketchesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ParticipatingFacultyBiosketchesTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeIntroductionToApplicationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.IntroductionToApplicationType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeIntroductionToApplicationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.IntroductionToApplicationTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgressReportType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgressReportType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgressReportType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ProgressReportTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgramPlanType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgramPlanType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgramPlanType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ProgramPlanTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeConsortiumContractualArrangementsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ConsortiumContractualArrangementsType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeConsortiumContractualArrangementsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.ConsortiumContractualArrangementsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeVertebrateAnimalsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.VertebrateAnimalsType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeVertebrateAnimalsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.VertebrateAnimalsTypeImpl();
    }

    /**
     * Create an instance of PHS398ResearchTrainingProgramPlan30Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type createPHS398ResearchTrainingProgramPlan30Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeSelectAgentResearchType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.SelectAgentResearchType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeSelectAgentResearchType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.SelectAgentResearchTypeImpl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30 createPHS398ResearchTrainingProgramPlan30()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30Impl();
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
     * Create an instance of PHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMethodsForEnhancingReproducibilityType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MethodsForEnhancingReproducibilityType createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMethodsForEnhancingReproducibilityType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.impl.PHS398ResearchTrainingProgramPlan30TypeImpl.ResearchTrainingProgramPlanAttachmentsTypeImpl.MethodsForEnhancingReproducibilityTypeImpl();
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

}