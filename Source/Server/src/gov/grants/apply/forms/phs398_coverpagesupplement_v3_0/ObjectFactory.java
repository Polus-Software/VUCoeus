//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:53 CST 
//


package gov.grants.apply.forms.phs398_coverpagesupplement_v3_0;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_coverpagesupplement_v3_0 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(28, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.ClinicalTrialTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.HashValueType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.StemCellsTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.FileLocationType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl.FileLocationTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AttachedFileDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AttachedFileDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.IncomeBudgetPeriodType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.IncomeBudgetPeriodTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.HumanNameDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30 .class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30Impl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.OrganizationDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.OrganizationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.AddressDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.SingleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.SingleAttachmentDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValue.class), "gov.grants.apply.system.global_v1.impl.HashValueImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.FormVersionIdentifier.class), "gov.grants.apply.system.global_v1.impl.FormVersionIdentifierImpl");
        defaultImplementations.put((gov.grants.apply.system.global_v1.HashValueType.class), "gov.grants.apply.system.global_v1.impl.HashValueTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType.class), "gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.VertebrateAnimalsTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.CFDADataType.class), "gov.grants.apply.system.globallibrary_v2.impl.CFDADataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.ContactPersonDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.system.globallibrary_v2.MultipleAttachmentDataType.class), "gov.grants.apply.system.globallibrary_v2.impl.MultipleAttachmentDataTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "HashValue"), (gov.grants.apply.system.global_v1.HashValue.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/system/Global-V1.0", "FormVersionIdentifier"), (gov.grants.apply.system.global_v1.FormVersionIdentifier.class));
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_CoverPageSupplement_3_0-V3.0", "PHS398_CoverPageSupplement_3_0"), (gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_coverpagesupplement_v3_0
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

    /**
     * Create an instance of PHS398CoverPageSupplement30TypeClinicalTrialType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.ClinicalTrialType createPHS398CoverPageSupplement30TypeClinicalTrialType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.ClinicalTrialTypeImpl();
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
     * Create an instance of PHS398CoverPageSupplement30TypeStemCellsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.StemCellsType createPHS398CoverPageSupplement30TypeStemCellsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.StemCellsTypeImpl();
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
     * Create an instance of PHS398CoverPageSupplement30TypeIncomeBudgetPeriodType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.IncomeBudgetPeriodType createPHS398CoverPageSupplement30TypeIncomeBudgetPeriodType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.IncomeBudgetPeriodTypeImpl();
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
     * Create an instance of PHS398CoverPageSupplement30
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30 createPHS398CoverPageSupplement30()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30Impl();
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
     * Create an instance of PHS398CoverPageSupplement30Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type createPHS398CoverPageSupplement30Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl();
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
     * Create an instance of PHS398CoverPageSupplement30TypeVertebrateAnimalsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.PHS398CoverPageSupplement30Type.VertebrateAnimalsType createPHS398CoverPageSupplement30TypeVertebrateAnimalsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_coverpagesupplement_v3_0.impl.PHS398CoverPageSupplement30TypeImpl.VertebrateAnimalsTypeImpl();
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

}