//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_checklist_v1_1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phs398_checklist_v1_1 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phs398_checklist_v1_1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_checklist_v1_1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phs398_checklist_v1_1.PHS398Checklist.class), "gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType.IncomeBudgetPeriodType.class), "gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl.IncomeBudgetPeriodTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType.CertificationExplanationType.class), "gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl.CertificationExplanationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType.class), "gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHS398_Checklist-V1-1", "PHS398_Checklist"), (gov.grants.apply.forms.phs398_checklist_v1_1.PHS398Checklist.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phs398_checklist_v1_1
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
     * Create an instance of PHS398Checklist
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_checklist_v1_1.PHS398Checklist createPHS398Checklist()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistImpl();
    }

    /**
     * Create an instance of PHS398ChecklistTypeIncomeBudgetPeriodType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType.IncomeBudgetPeriodType createPHS398ChecklistTypeIncomeBudgetPeriodType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl.IncomeBudgetPeriodTypeImpl();
    }

    /**
     * Create an instance of PHS398ChecklistTypeCertificationExplanationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType.CertificationExplanationType createPHS398ChecklistTypeCertificationExplanationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl.CertificationExplanationTypeImpl();
    }

    /**
     * Create an instance of PHS398ChecklistType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phs398_checklist_v1_1.PHS398ChecklistType createPHS398ChecklistType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phs398_checklist_v1_1.impl.PHS398ChecklistTypeImpl();
    }

}
