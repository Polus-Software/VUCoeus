//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.ed_sf424_supplement_v1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.ed_sf424_supplement_v1 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.ed_sf424_supplement_v1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.ed_sf424_supplement_v1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType.class), "gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424Supplement.class), "gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementImpl");
        defaultImplementations.put((gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType.AssuranceNumberType.class), "gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl.AssuranceNumberTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType.ExemptionsNumberType.class), "gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl.ExemptionsNumberTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/ED_SF424_Supplement-V1.0", "ED_SF424_Supplement"), (gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424Supplement.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.ed_sf424_supplement_v1
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
     * Create an instance of EDSF424SupplementType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType createEDSF424SupplementType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl();
    }

    /**
     * Create an instance of EDSF424Supplement
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424Supplement createEDSF424Supplement()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementImpl();
    }

    /**
     * Create an instance of EDSF424SupplementTypeAssuranceNumberType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType.AssuranceNumberType createEDSF424SupplementTypeAssuranceNumberType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl.AssuranceNumberTypeImpl();
    }

    /**
     * Create an instance of EDSF424SupplementTypeExemptionsNumberType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.ed_sf424_supplement_v1.EDSF424SupplementType.ExemptionsNumberType createEDSF424SupplementTypeExemptionsNumberType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.ed_sf424_supplement_v1.impl.EDSF424SupplementTypeImpl.ExemptionsNumberTypeImpl();
    }

}
