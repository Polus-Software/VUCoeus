//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nsf_coverpage_1_2_v1_2;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.nsf_coverpage_1_2_v1_2 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.NSFUnitConsiderationType.class), "gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.NSFUnitConsiderationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12 .class), "gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12Impl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.PIInfoType.class), "gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.PIInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.class), "gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.OtherInfoType.class), "gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.OtherInfoTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/NSF_CoverPage_1_2-V1-2", "NSF_CoverPage_1_2"), (gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.nsf_coverpage_1_2_v1_2
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
     * Create an instance of NSFCoverPage12TypeNSFUnitConsiderationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.NSFUnitConsiderationType createNSFCoverPage12TypeNSFUnitConsiderationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.NSFUnitConsiderationTypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage12
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12 createNSFCoverPage12()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12Impl();
    }

    /**
     * Create an instance of NSFCoverPage12TypePIInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.PIInfoType createNSFCoverPage12TypePIInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.PIInfoTypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage12Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type createNSFCoverPage12Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl();
    }

    /**
     * Create an instance of NSFCoverPage12TypeOtherInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.NSFCoverPage12Type.OtherInfoType createNSFCoverPage12TypeOtherInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.nsf_coverpage_1_2_v1_2.impl.NSFCoverPage12TypeImpl.OtherInfoTypeImpl();
    }

}
