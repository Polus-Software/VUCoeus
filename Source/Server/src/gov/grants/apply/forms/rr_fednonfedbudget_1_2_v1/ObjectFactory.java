//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.01 at 08:55:08 CDT 
//


package gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(37, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.KeyPersonDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.KeyPersonDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.ParticipantTraineeSupportCostsType.OtherType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.ParticipantTraineeSupportCostsTypeImpl.OtherTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType.OthersType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl.OthersTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.IndirectCostsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.IndirectCostsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12Type.BudgetSummaryType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12TypeImpl.BudgetSummaryTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12 .class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12Impl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.UndergraduateStudentsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.UndergraduateStudentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.PostDocAssociatesType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.PostDocAssociatesTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.KeyPersonsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.KeyPersonsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.ParticipantTraineeSupportCostsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.ParticipantTraineeSupportCostsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType.OthersType.OtherType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl.OthersTypeImpl.OtherTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.KeyPersonCompensationDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.KeyPersonCompensationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.SecretarialClericalType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.SecretarialClericalTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SummaryDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SummaryDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.EquipmentType.EquipmentListType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.EquipmentTypeImpl.EquipmentListTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SectBCompensationDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SectBCompensationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12Type.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.TravelType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.TravelTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.EquipmentType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.EquipmentTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SectACompensationDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SectACompensationDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.GraduateStudentsType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.GraduateStudentsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.OtherPersonnelDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.OtherPersonnelDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.TotalDataType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.TotalDataTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.IndirectCostsType.IndirectCostType.class), "gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.IndirectCostsTypeImpl.IndirectCostTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2", "RR_FedNonFedBudget_1_2"), (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12 .class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1
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
     * Create an instance of KeyPersonDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.KeyPersonDataType createKeyPersonDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.KeyPersonDataTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeParticipantTraineeSupportCostsTypeOtherType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.ParticipantTraineeSupportCostsType.OtherType createBudgetYearDataTypeParticipantTraineeSupportCostsTypeOtherType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.ParticipantTraineeSupportCostsTypeImpl.OtherTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherDirectCostsTypeOthersType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType.OthersType createBudgetYearDataTypeOtherDirectCostsTypeOthersType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl.OthersTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeIndirectCostsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.IndirectCostsType createBudgetYearDataTypeIndirectCostsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.IndirectCostsTypeImpl();
    }

    /**
     * Create an instance of RRFedNonFedBudget12TypeBudgetSummaryType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12Type.BudgetSummaryType createRRFedNonFedBudget12TypeBudgetSummaryType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12TypeImpl.BudgetSummaryTypeImpl();
    }

    /**
     * Create an instance of RRFedNonFedBudget12
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12 createRRFedNonFedBudget12()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12Impl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherDirectCostsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType createBudgetYearDataTypeOtherDirectCostsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherPersonnelTypeUndergraduateStudentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.UndergraduateStudentsType createBudgetYearDataTypeOtherPersonnelTypeUndergraduateStudentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.UndergraduateStudentsTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherPersonnelTypePostDocAssociatesType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.PostDocAssociatesType createBudgetYearDataTypeOtherPersonnelTypePostDocAssociatesType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.PostDocAssociatesTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeKeyPersonsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.KeyPersonsType createBudgetYearDataTypeKeyPersonsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.KeyPersonsTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeParticipantTraineeSupportCostsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.ParticipantTraineeSupportCostsType createBudgetYearDataTypeParticipantTraineeSupportCostsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.ParticipantTraineeSupportCostsTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherDirectCostsType.OthersType.OtherType createBudgetYearDataTypeOtherDirectCostsTypeOthersTypeOtherType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherDirectCostsTypeImpl.OthersTypeImpl.OtherTypeImpl();
    }

    /**
     * Create an instance of KeyPersonCompensationDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.KeyPersonCompensationDataType createKeyPersonCompensationDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.KeyPersonCompensationDataTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherPersonnelTypeSecretarialClericalType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.SecretarialClericalType createBudgetYearDataTypeOtherPersonnelTypeSecretarialClericalType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.SecretarialClericalTypeImpl();
    }

    /**
     * Create an instance of SummaryDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SummaryDataType createSummaryDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SummaryDataTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeEquipmentTypeEquipmentListType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.EquipmentType.EquipmentListType createBudgetYearDataTypeEquipmentTypeEquipmentListType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.EquipmentTypeImpl.EquipmentListTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherPersonnelType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType createBudgetYearDataTypeOtherPersonnelType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl();
    }

    /**
     * Create an instance of SectBCompensationDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SectBCompensationDataType createSectBCompensationDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SectBCompensationDataTypeImpl();
    }

    /**
     * Create an instance of RRFedNonFedBudget12Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.RRFedNonFedBudget12Type createRRFedNonFedBudget12Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.RRFedNonFedBudget12TypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeTravelType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.TravelType createBudgetYearDataTypeTravelType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.TravelTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeEquipmentType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.EquipmentType createBudgetYearDataTypeEquipmentType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.EquipmentTypeImpl();
    }

    /**
     * Create an instance of SectACompensationDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.SectACompensationDataType createSectACompensationDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.SectACompensationDataTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeOtherPersonnelTypeGraduateStudentsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.OtherPersonnelType.GraduateStudentsType createBudgetYearDataTypeOtherPersonnelTypeGraduateStudentsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.OtherPersonnelTypeImpl.GraduateStudentsTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType createBudgetYearDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl();
    }

    /**
     * Create an instance of OtherPersonnelDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.OtherPersonnelDataType createOtherPersonnelDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.OtherPersonnelDataTypeImpl();
    }

    /**
     * Create an instance of TotalDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.TotalDataType createTotalDataType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.TotalDataTypeImpl();
    }

    /**
     * Create an instance of BudgetYearDataTypeIndirectCostsTypeIndirectCostType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.BudgetYearDataType.IndirectCostsType.IndirectCostType createBudgetYearDataTypeIndirectCostsTypeIndirectCostType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.BudgetYearDataTypeImpl.IndirectCostsTypeImpl.IndirectCostTypeImpl();
    }

}
