//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2006.04.12 at 03:03:53 EDT 
//


package gov.grants.apply.coeus.additionalequipment;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.coeus.additionalequipment package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.coeus.additionalequipment.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.coeus.additionalequipment.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType.EquipmentListType.class), "gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListTypeImpl.EquipmentListTypeImpl");
        defaultImplementations.put((gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType.class), "gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListTypeImpl");
        defaultImplementations.put((gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentList.class), "gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/coeus/AdditionalEquipment", "AdditionalEquipmentList"), (gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentList.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.coeus.additionalequipment
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
     * Create an instance of AdditionalEquipmentListTypeEquipmentListType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType.EquipmentListType createAdditionalEquipmentListTypeEquipmentListType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListTypeImpl.EquipmentListTypeImpl();
    }

    /**
     * Create an instance of AdditionalEquipmentListType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentListType createAdditionalEquipmentListType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListTypeImpl();
    }

    /**
     * Create an instance of AdditionalEquipmentList
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.coeus.additionalequipment.AdditionalEquipmentList createAdditionalEquipmentList()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.coeus.additionalequipment.impl.AdditionalEquipmentListImpl();
    }

}
