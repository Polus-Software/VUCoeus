//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_otherprojectinfo_v1_1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.rr_otherprojectinfo_v1_1 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(20, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.rr_otherprojectinfo_v1_1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EnvironmentalImpactType.EnvironmentalExemptionType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EnvironmentalImpactTypeImpl.EnvironmentalExemptionTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.AbstractAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.AbstractAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.HumanSubjectsSupplementType.ExemptionNumbersType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.HumanSubjectsSupplementTypeImpl.ExemptionNumbersTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EnvironmentalImpactType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EnvironmentalImpactTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.FacilitiesAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.FacilitiesAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EquipmentAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EquipmentAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.BibliographyAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.BibliographyAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.HumanSubjectsSupplementType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.HumanSubjectsSupplementTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.VertebrateAnimalsSupplementType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.VertebrateAnimalsSupplementTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfo.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.OtherAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.OtherAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.ProjectNarrativeAttachmentsType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.ProjectNarrativeAttachmentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.InternationalActivitiesType.class), "gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.InternationalActivitiesTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1-1", "RR_OtherProjectInfo"), (gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfo.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.rr_otherprojectinfo_v1_1
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
     * Create an instance of RROtherProjectInfoTypeEnvironmentalImpactTypeEnvironmentalExemptionType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EnvironmentalImpactType.EnvironmentalExemptionType createRROtherProjectInfoTypeEnvironmentalImpactTypeEnvironmentalExemptionType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EnvironmentalImpactTypeImpl.EnvironmentalExemptionTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeAbstractAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.AbstractAttachmentsType createRROtherProjectInfoTypeAbstractAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.AbstractAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType createRROtherProjectInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeHumanSubjectsSupplementTypeExemptionNumbersType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.HumanSubjectsSupplementType.ExemptionNumbersType createRROtherProjectInfoTypeHumanSubjectsSupplementTypeExemptionNumbersType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.HumanSubjectsSupplementTypeImpl.ExemptionNumbersTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeEnvironmentalImpactType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EnvironmentalImpactType createRROtherProjectInfoTypeEnvironmentalImpactType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EnvironmentalImpactTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeFacilitiesAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.FacilitiesAttachmentsType createRROtherProjectInfoTypeFacilitiesAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.FacilitiesAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeEquipmentAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.EquipmentAttachmentsType createRROtherProjectInfoTypeEquipmentAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.EquipmentAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeBibliographyAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.BibliographyAttachmentsType createRROtherProjectInfoTypeBibliographyAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.BibliographyAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeHumanSubjectsSupplementType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.HumanSubjectsSupplementType createRROtherProjectInfoTypeHumanSubjectsSupplementType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.HumanSubjectsSupplementTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeVertebrateAnimalsSupplementType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.VertebrateAnimalsSupplementType createRROtherProjectInfoTypeVertebrateAnimalsSupplementType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.VertebrateAnimalsSupplementTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfo
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfo createRROtherProjectInfo()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeOtherAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.OtherAttachmentsType createRROtherProjectInfoTypeOtherAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.OtherAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeProjectNarrativeAttachmentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.ProjectNarrativeAttachmentsType createRROtherProjectInfoTypeProjectNarrativeAttachmentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.ProjectNarrativeAttachmentsTypeImpl();
    }

    /**
     * Create an instance of RROtherProjectInfoTypeInternationalActivitiesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_otherprojectinfo_v1_1.RROtherProjectInfoType.InternationalActivitiesType createRROtherProjectInfoTypeInternationalActivitiesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1_1.impl.RROtherProjectInfoTypeImpl.InternationalActivitiesTypeImpl();
    }

}
