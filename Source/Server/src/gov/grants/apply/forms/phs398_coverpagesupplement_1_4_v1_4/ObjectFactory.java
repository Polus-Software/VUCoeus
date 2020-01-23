//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.19 at 02:55:10 EDT 
//


package gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.ContactPersonInfoType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.ContactPersonInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.ClinicalTrialType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.ClinicalTrialTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.PDPIType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.PDPITypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14 .class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14Impl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.StemCellsType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.StemCellsTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_CoverPageSupplement_1_4-V1-4", "PHS398_CoverPageSupplement_1_4"), (gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4
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
     * Create an instance of PHS398CoverPageSupplement14TypeContactPersonInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.ContactPersonInfoType createPHS398CoverPageSupplement14TypeContactPersonInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.ContactPersonInfoTypeImpl();
    }

    /**
     * Create an instance of PHS398CoverPageSupplement14TypeClinicalTrialType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.ClinicalTrialType createPHS398CoverPageSupplement14TypeClinicalTrialType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.ClinicalTrialTypeImpl();
    }

    /**
     * Create an instance of PHS398CoverPageSupplement14TypePDPIType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.PDPIType createPHS398CoverPageSupplement14TypePDPIType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.PDPITypeImpl();
    }

    /**
     * Create an instance of PHS398CoverPageSupplement14
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14 createPHS398CoverPageSupplement14()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14Impl();
    }

    /**
     * Create an instance of PHS398CoverPageSupplement14Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type createPHS398CoverPageSupplement14Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl();
    }

    /**
     * Create an instance of PHS398CoverPageSupplement14TypeStemCellsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.PHS398CoverPageSupplement14Type.StemCellsType createPHS398CoverPageSupplement14TypeStemCellsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_1_4_v1_4.impl.PHS398CoverPageSupplement14TypeImpl.StemCellsTypeImpl();
    }

}
