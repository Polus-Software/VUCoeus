//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_sf424_1_2_v1_2;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.rr_sf424_1_2_v1_2 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(21, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.rr_sf424_1_2_v1_2.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsSociallyEconomicallyDisadvantagedTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.OrganizationContactPersonDataType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.OrganizationContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.StateReviewType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.StateReviewTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.CongressionalDistrictType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.CongressionalDistrictTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.EstimatedProjectFundingType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.EstimatedProjectFundingTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicationTypeType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicationTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsWomenOwnedTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantInfoType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.AORInfoType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.AORInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantInfoType.ContactPersonInfoType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantInfoTypeImpl.ContactPersonInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412 .class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412Impl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ProposedProjectPeriodType.class), "gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ProposedProjectPeriodTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "RR_SF424_1_2"), (gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.rr_sf424_1_2_v1_2
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
     * Create an instance of RRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsSociallyEconomicallyDisadvantagedTypeImpl();
    }

    /**
     * Create an instance of OrganizationContactPersonDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.OrganizationContactPersonDataType createOrganizationContactPersonDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.OrganizationContactPersonDataTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeStateReviewType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.StateReviewType createRRSF42412TypeStateReviewType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.StateReviewTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeCongressionalDistrictType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.CongressionalDistrictType createRRSF42412TypeCongressionalDistrictType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.CongressionalDistrictTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeEstimatedProjectFundingType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.EstimatedProjectFundingType createRRSF42412TypeEstimatedProjectFundingType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.EstimatedProjectFundingTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicantTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType createRRSF42412TypeApplicantTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF42412Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type createRRSF42412Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicationTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicationTypeType createRRSF42412TypeApplicationTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicationTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType createRRSF42412TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsWomenOwnedTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicantInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantInfoType createRRSF42412TypeApplicantInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantInfoTypeImpl();
    }

    /**
     * Create an instance of AORInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.AORInfoType createAORInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.AORInfoTypeImpl();
    }

    /**
     * Create an instance of RRSF42412TypeApplicantInfoTypeContactPersonInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ApplicantInfoType.ContactPersonInfoType createRRSF42412TypeApplicantInfoTypeContactPersonInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ApplicantInfoTypeImpl.ContactPersonInfoTypeImpl();
    }

    /**
     * Create an instance of RRSF42412
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412 createRRSF42412()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412Impl();
    }

    /**
     * Create an instance of RRSF42412TypeProposedProjectPeriodType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.ProposedProjectPeriodType createRRSF42412TypeProposedProjectPeriodType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl.ProposedProjectPeriodTypeImpl();
    }

}
