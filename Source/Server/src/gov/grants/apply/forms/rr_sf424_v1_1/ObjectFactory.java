//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_sf424_v1_1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.rr_sf424_v1_1 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.rr_sf424_v1_1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_sf424_v1_1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantInfoType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsWomenOwnedTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.AORInfoType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.AORInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.StateReviewType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.StateReviewTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicationTypeType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicationTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.CongressionalDistrictType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.CongressionalDistrictTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantInfoType.ContactPersonInfoType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantInfoTypeImpl.ContactPersonInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.OrganizationContactPersonDataType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.OrganizationContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsSociallyEconomicallyDisadvantagedTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ProposedProjectPeriodType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ProposedProjectPeriodTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424 .class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424Impl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.EstimatedProjectFundingType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.EstimatedProjectFundingTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.class), "gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/RR_SF424-V1-1", "RR_SF424"), (gov.grants.apply.forms.rr_sf424_v1_1.RRSF424 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.rr_sf424_v1_1
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
     * Create an instance of RRSF424TypeApplicantInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantInfoType createRRSF424TypeApplicantInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantInfoTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsWomenOwnedType createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsWomenOwnedType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsWomenOwnedTypeImpl();
    }

    /**
     * Create an instance of AORInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.AORInfoType createAORInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.AORInfoTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeStateReviewType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.StateReviewType createRRSF424TypeStateReviewType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.StateReviewTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicationTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicationTypeType createRRSF424TypeApplicationTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicationTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeCongressionalDistrictType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.CongressionalDistrictType createRRSF424TypeCongressionalDistrictType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.CongressionalDistrictTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicantInfoTypeContactPersonInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantInfoType.ContactPersonInfoType createRRSF424TypeApplicantInfoTypeContactPersonInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantInfoTypeImpl.ContactPersonInfoTypeImpl();
    }

    /**
     * Create an instance of OrganizationContactPersonDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.OrganizationContactPersonDataType createOrganizationContactPersonDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.OrganizationContactPersonDataTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType.SmallBusinessOrganizationTypeType.IsSociallyEconomicallyDisadvantagedType createRRSF424TypeApplicantTypeTypeSmallBusinessOrganizationTypeTypeIsSociallyEconomicallyDisadvantagedType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl.SmallBusinessOrganizationTypeTypeImpl.IsSociallyEconomicallyDisadvantagedTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeProposedProjectPeriodType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ProposedProjectPeriodType createRRSF424TypeProposedProjectPeriodType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ProposedProjectPeriodTypeImpl();
    }

    /**
     * Create an instance of RRSF424
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424 createRRSF424()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424Impl();
    }

    /**
     * Create an instance of RRSF424TypeEstimatedProjectFundingType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.EstimatedProjectFundingType createRRSF424TypeEstimatedProjectFundingType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.EstimatedProjectFundingTypeImpl();
    }

    /**
     * Create an instance of RRSF424TypeApplicantTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type.ApplicantTypeType createRRSF424TypeApplicantTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl.ApplicantTypeTypeImpl();
    }

    /**
     * Create an instance of RRSF424Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_sf424_v1_1.RRSF424Type createRRSF424Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_sf424_v1_1.impl.RRSF424TypeImpl();
    }

}
