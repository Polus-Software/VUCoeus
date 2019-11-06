//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.30 at 02:44:18 EDT 
//


package gov.grants.apply.forms.nsf_coverpage_v1_3;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.nsf_coverpage_v1_3 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.nsf_coverpage_v1_3.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.nsf_coverpage_v1_3.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13 .class), "gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13Impl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.PIInfoType.class), "gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.PIInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.OtherInfoType.class), "gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.OtherInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.class), "gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.NSFUnitConsiderationType.class), "gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.NSFUnitConsiderationTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "NSF_CoverPage_1_3"), (gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.nsf_coverpage_v1_3
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
     * Create an instance of NSFCoverPage13
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13 createNSFCoverPage13()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13Impl();
    }

    /**
     * Create an instance of NSFCoverPage13TypePIInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.PIInfoType createNSFCoverPage13TypePIInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.PIInfoTypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage13TypeOtherInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.OtherInfoType createNSFCoverPage13TypeOtherInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.OtherInfoTypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage13Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type createNSFCoverPage13Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage13TypeNSFUnitConsiderationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.NSFUnitConsiderationType createNSFCoverPage13TypeNSFUnitConsiderationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl.NSFUnitConsiderationTypeImpl();
    }

}
