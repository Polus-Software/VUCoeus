//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.phs398_modularbudget_v1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_modularbudget_v1 package. 
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

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(39, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_modularbudget_v1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_modularbudget_v1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.DirectCost2Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.DirectCost2TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.IndirectCost5Type.IndirectCostItems5Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.IndirectCost5TypeImpl.IndirectCostItems5TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudget.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.DirectCost5Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.DirectCost5TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.IndirectCost3Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.IndirectCost3TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.IndirectCostType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.IndirectCostTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.IndirectCost5Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.IndirectCost5TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.IndirectCost2Type.IndirectCostItems2Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.IndirectCost2TypeImpl.IndirectCostItems2TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.IndirectCost2Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.IndirectCost2TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.AdditionalNarrativeJustificationType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.AdditionalNarrativeJustificationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.IndirectCost4Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.IndirectCost4TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.IndirectCost4Type.IndirectCostItems4Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.IndirectCost4TypeImpl.IndirectCostItems4TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.DirectCostType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.DirectCostTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.ConsortiumJustificationType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.ConsortiumJustificationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.PersonnelJustificationType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.PersonnelJustificationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.EntirePeriodTotalCostType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.EntirePeriodTotalCostTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.IndirectCostType.IndirectCostItemsType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.IndirectCostTypeImpl.IndirectCostItemsTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.DirectCost4Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.DirectCost4TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.DirectCost3Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.DirectCost3TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.IndirectCost3Type.IndirectCostItems3Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.IndirectCost3TypeImpl.IndirectCostItems3TypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.class), "gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_ModularBudget-V1.0", "PHS398_ModularBudget"), (gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudget.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_modularbudget_v1
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
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods2TypeDirectCost2Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.DirectCost2Type createPHS398ModularBudgetTypePeriods2TypeDirectCost2Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.DirectCost2TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods5TypeIndirectCost5TypeIndirectCostItems5Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.IndirectCost5Type.IndirectCostItems5Type createPHS398ModularBudgetTypePeriods5TypeIndirectCost5TypeIndirectCostItems5Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.IndirectCost5TypeImpl.IndirectCostItems5TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudget
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudget createPHS398ModularBudget()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods4Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type createPHS398ModularBudgetTypePeriods4Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType createPHS398ModularBudgetType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods5TypeDirectCost5Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.DirectCost5Type createPHS398ModularBudgetTypePeriods5TypeDirectCost5Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.DirectCost5TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods3TypeIndirectCost3Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.IndirectCost3Type createPHS398ModularBudgetTypePeriods3TypeIndirectCost3Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.IndirectCost3TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriodsTypeIndirectCostType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.IndirectCostType createPHS398ModularBudgetTypePeriodsTypeIndirectCostType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.IndirectCostTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods5TypeIndirectCost5Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type.IndirectCost5Type createPHS398ModularBudgetTypePeriods5TypeIndirectCost5Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl.IndirectCost5TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods2TypeIndirectCost2TypeIndirectCostItems2Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.IndirectCost2Type.IndirectCostItems2Type createPHS398ModularBudgetTypePeriods2TypeIndirectCost2TypeIndirectCostItems2Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.IndirectCost2TypeImpl.IndirectCostItems2TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods2TypeIndirectCost2Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type.IndirectCost2Type createPHS398ModularBudgetTypePeriods2TypeIndirectCost2Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl.IndirectCost2TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeAdditionalNarrativeJustificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.AdditionalNarrativeJustificationType createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeAdditionalNarrativeJustificationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.AdditionalNarrativeJustificationTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods4TypeIndirectCost4Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.IndirectCost4Type createPHS398ModularBudgetTypePeriods4TypeIndirectCost4Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.IndirectCost4TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods4TypeIndirectCost4TypeIndirectCostItems4Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.IndirectCost4Type.IndirectCostItems4Type createPHS398ModularBudgetTypePeriods4TypeIndirectCost4TypeIndirectCostItems4Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.IndirectCost4TypeImpl.IndirectCostItems4TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriodsTypeDirectCostType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.DirectCostType createPHS398ModularBudgetTypePeriodsTypeDirectCostType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.DirectCostTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods2Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods2Type createPHS398ModularBudgetTypePeriods2Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods2TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeConsortiumJustificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.ConsortiumJustificationType createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypeConsortiumJustificationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.ConsortiumJustificationTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypePersonnelJustificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.BudgetJustificationsType.PersonnelJustificationType createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeBudgetJustificationsTypePersonnelJustificationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.BudgetJustificationsTypeImpl.PersonnelJustificationTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriodsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType createPHS398ModularBudgetTypePeriodsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoTypeEntirePeriodTotalCostType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType.EntirePeriodTotalCostType createPHS398ModularBudgetTypeCummulativeBudgetInfoTypeEntirePeriodTotalCostType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl.EntirePeriodTotalCostTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriodsTypeIndirectCostTypeIndirectCostItemsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.PeriodsType.IndirectCostType.IndirectCostItemsType createPHS398ModularBudgetTypePeriodsTypeIndirectCostTypeIndirectCostItemsType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.PeriodsTypeImpl.IndirectCostTypeImpl.IndirectCostItemsTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods5Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods5Type createPHS398ModularBudgetTypePeriods5Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods5TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods4TypeDirectCost4Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods4Type.DirectCost4Type createPHS398ModularBudgetTypePeriods4TypeDirectCost4Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods4TypeImpl.DirectCost4TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypeCummulativeBudgetInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.CummulativeBudgetInfoType createPHS398ModularBudgetTypeCummulativeBudgetInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.CummulativeBudgetInfoTypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods3TypeDirectCost3Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.DirectCost3Type createPHS398ModularBudgetTypePeriods3TypeDirectCost3Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.DirectCost3TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods3TypeIndirectCost3TypeIndirectCostItems3Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type.IndirectCost3Type.IndirectCostItems3Type createPHS398ModularBudgetTypePeriods3TypeIndirectCost3TypeIndirectCostItems3Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl.IndirectCost3TypeImpl.IndirectCostItems3TypeImpl();
    }

    /**
     * Create an instance of PHS398ModularBudgetTypePeriods3Type
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_modularbudget_v1.PHS398ModularBudgetType.Periods3Type createPHS398ModularBudgetTypePeriods3Type()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_modularbudget_v1.impl.PHS398ModularBudgetTypeImpl.Periods3TypeImpl();
    }

}
