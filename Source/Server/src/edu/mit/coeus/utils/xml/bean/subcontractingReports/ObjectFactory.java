//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.09.26 at 01:00:47 EDT 
//


package edu.mit.coeus.utils.xml.bean.subcontractingReports;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.mit.coeus.utils.xml.bean.subcontractingReports package. 
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
    extends edu.mit.coeus.utils.xml.bean.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(20, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static edu.mit.coeus.utils.xml.bean.impl.runtime.GrammarInfo grammarInfo = new edu.mit.coeus.utils.xml.bean.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (edu.mit.coeus.utils.xml.bean.subcontractingReports.ObjectFactory.class));
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.AdministeringOfficialType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.AdministeringOfficialTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.PlanTypeType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.PlanTypeTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.CEOType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.CEOTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.NameAndAddressTypeType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.NameAndAddressTypeTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ReportingPeriodType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ReportingPeriodTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.NameAndAddressType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.NameAndAddressTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ContractorProductsType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ContractorProductsTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReports.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.CompanyInfoType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.CompanyInfoTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType.AwardingAgencyNameType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl.AwardingAgencyNameTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType.VendorTypeType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl.VendorTypeTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl");
        defaultImplementations.put((edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ContractorTypeType.class), "edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ContractorTypeTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "NameAndAddressType"), (edu.mit.coeus.utils.xml.bean.subcontractingReports.NameAndAddressType.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "SubcontractReports"), (edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReports.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.mit.coeus.utils.xml.bean.subcontractingReports
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
     * Create an instance of SubcontractReportsTypeAdministeringOfficialType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.AdministeringOfficialType createSubcontractReportsTypeAdministeringOfficialType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.AdministeringOfficialTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypePlanTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.PlanTypeType createSubcontractReportsTypePlanTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.PlanTypeTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypeCEOType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.CEOType createSubcontractReportsTypeCEOType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.CEOTypeImpl();
    }

    /**
     * Create an instance of NameAndAddressTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.NameAndAddressTypeType createNameAndAddressTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.NameAndAddressTypeTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypeReportingPeriodType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ReportingPeriodType createSubcontractReportsTypeReportingPeriodType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ReportingPeriodTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType createSubcontractReportsType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl();
    }

    /**
     * Create an instance of NameAndAddressType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.NameAndAddressType createNameAndAddressType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.NameAndAddressTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypeContractorProductsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ContractorProductsType createSubcontractReportsTypeContractorProductsType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ContractorProductsTypeImpl();
    }

    /**
     * Create an instance of SubcontractReports
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReports createSubcontractReports()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypeCompanyInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.CompanyInfoType createSubcontractReportsTypeCompanyInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.CompanyInfoTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportPageTypeAwardingAgencyNameType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType.AwardingAgencyNameType createSubcontractReportPageTypeAwardingAgencyNameType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl.AwardingAgencyNameTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportPageTypeVendorTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType.VendorTypeType createSubcontractReportPageTypeVendorTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl.VendorTypeTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportPageType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportPageType createSubcontractReportPageType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportPageTypeImpl();
    }

    /**
     * Create an instance of SubcontractReportsTypeContractorTypeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.utils.xml.bean.subcontractingReports.SubcontractReportsType.ContractorTypeType createSubcontractReportsTypeContractorTypeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.utils.xml.bean.subcontractingReports.impl.SubcontractReportsTypeImpl.ContractorTypeTypeImpl();
    }

}
