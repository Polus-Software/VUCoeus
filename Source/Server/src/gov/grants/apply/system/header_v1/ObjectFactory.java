//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.header_v1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.system.header_v1 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(16, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.system.header_v1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.system.header_v1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.system.header_v1.ActivityTitle.class), "gov.grants.apply.system.header_v1.impl.ActivityTitleImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.CompetitionID.class), "gov.grants.apply.system.header_v1.impl.CompetitionIDImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.OpeningDate.class), "gov.grants.apply.system.header_v1.impl.OpeningDateImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.OpportunityID.class), "gov.grants.apply.system.header_v1.impl.OpportunityIDImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.GrantSubmissionHeaderType.class), "gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.SubmissionTitle.class), "gov.grants.apply.system.header_v1.impl.SubmissionTitleImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.AgencyName.class), "gov.grants.apply.system.header_v1.impl.AgencyNameImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.GrantSubmissionHeader.class), "gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.CFDANumber.class), "gov.grants.apply.system.header_v1.impl.CFDANumberImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.ClosingDate.class), "gov.grants.apply.system.header_v1.impl.ClosingDateImpl");
        defaultImplementations.put((gov.grants.apply.system.header_v1.OpportunityTitle.class), "gov.grants.apply.system.header_v1.impl.OpportunityTitleImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "ActivityTitle"), (gov.grants.apply.system.header_v1.ActivityTitle.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "CFDANumber"), (gov.grants.apply.system.header_v1.CFDANumber.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "SubmissionTitle"), (gov.grants.apply.system.header_v1.SubmissionTitle.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "OpportunityTitle"), (gov.grants.apply.system.header_v1.OpportunityTitle.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "OpeningDate"), (gov.grants.apply.system.header_v1.OpeningDate.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "ClosingDate"), (gov.grants.apply.system.header_v1.ClosingDate.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "OpportunityID"), (gov.grants.apply.system.header_v1.OpportunityID.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "CompetitionID"), (gov.grants.apply.system.header_v1.CompetitionID.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "GrantSubmissionHeader"), (gov.grants.apply.system.header_v1.GrantSubmissionHeader.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Header-V1.0", "AgencyName"), (gov.grants.apply.system.header_v1.AgencyName.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.system.header_v1
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
     * Create an instance of ActivityTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.ActivityTitle createActivityTitle()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.ActivityTitleImpl();
    }

    /**
     * Create an instance of ActivityTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.ActivityTitle createActivityTitle(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.ActivityTitleImpl(value);
    }

    /**
     * Create an instance of CompetitionID
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.CompetitionID createCompetitionID()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.CompetitionIDImpl();
    }

    /**
     * Create an instance of CompetitionID
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.CompetitionID createCompetitionID(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.CompetitionIDImpl(value);
    }

    /**
     * Create an instance of OpeningDate
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpeningDate createOpeningDate()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpeningDateImpl();
    }

    /**
     * Create an instance of OpeningDate
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpeningDate createOpeningDate(java.util.Calendar value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpeningDateImpl(value);
    }

    /**
     * Create an instance of OpportunityID
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpportunityID createOpportunityID()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpportunityIDImpl();
    }

    /**
     * Create an instance of OpportunityID
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpportunityID createOpportunityID(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpportunityIDImpl(value);
    }

    /**
     * Create an instance of GrantSubmissionHeaderType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.GrantSubmissionHeaderType createGrantSubmissionHeaderType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderTypeImpl();
    }

    /**
     * Create an instance of SubmissionTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.SubmissionTitle createSubmissionTitle()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.SubmissionTitleImpl();
    }

    /**
     * Create an instance of SubmissionTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.SubmissionTitle createSubmissionTitle(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.SubmissionTitleImpl(value);
    }

    /**
     * Create an instance of AgencyName
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.AgencyName createAgencyName()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.AgencyNameImpl();
    }

    /**
     * Create an instance of AgencyName
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.AgencyName createAgencyName(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.AgencyNameImpl(value);
    }

    /**
     * Create an instance of GrantSubmissionHeader
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.GrantSubmissionHeader createGrantSubmissionHeader()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderImpl();
    }

    /**
     * Create an instance of CFDANumber
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.CFDANumber createCFDANumber()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.CFDANumberImpl();
    }

    /**
     * Create an instance of CFDANumber
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.CFDANumber createCFDANumber(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.CFDANumberImpl(value);
    }

    /**
     * Create an instance of ClosingDate
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.ClosingDate createClosingDate()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.ClosingDateImpl();
    }

    /**
     * Create an instance of ClosingDate
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.ClosingDate createClosingDate(java.util.Calendar value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.ClosingDateImpl(value);
    }

    /**
     * Create an instance of OpportunityTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpportunityTitle createOpportunityTitle()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpportunityTitleImpl();
    }

    /**
     * Create an instance of OpportunityTitle
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.system.header_v1.OpportunityTitle createOpportunityTitle(java.lang.String value)
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.system.header_v1.impl.OpportunityTitleImpl(value);
    }

}
