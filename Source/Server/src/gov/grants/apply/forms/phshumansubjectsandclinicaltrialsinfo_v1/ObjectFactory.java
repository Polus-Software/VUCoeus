//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.24 at 10:09:00 CDT 
//


package gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1 package. 
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
    public final static gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfo grammarInfo = new gov.grants.apply.forms.attachments_v1.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.ObjectFactory.class));
    public final static java.lang.Class version = (gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.OtherRequestedInformationType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.OtherRequestedInformationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.ExplanationType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.ExplanationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfo.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.HumanSubjectStudyAttachmentType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.HumanSubjectStudyAttachmentTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.DelayedOnsetStudyType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.DelayedOnsetStudyTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.DelayedOnsetStudyType.JustificationType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.DelayedOnsetStudyTypeImpl.JustificationTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.ExemptionNumbersType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.ExemptionNumbersTypeImpl");
        defaultImplementations.put((gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.HumanSubjectATTType.class), "gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.HumanSubjectATTTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("http://apply.grants.gov/forms/PHSHumanSubjectsAndClinicalTrialsInfo-V1.0", "PHSHumanSubjectsAndClinicalTrialsInfo"), (gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfo.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1
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
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeOtherRequestedInformationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.OtherRequestedInformationType createPHSHumanSubjectsAndClinicalTrialsInfoTypeOtherRequestedInformationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.OtherRequestedInformationTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeExplanationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.ExplanationType createPHSHumanSubjectsAndClinicalTrialsInfoTypeExplanationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.ExplanationTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfo
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfo createPHSHumanSubjectsAndClinicalTrialsInfo()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeHumanSubjectStudyAttachmentType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.HumanSubjectStudyAttachmentType createPHSHumanSubjectsAndClinicalTrialsInfoTypeHumanSubjectStudyAttachmentType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.HumanSubjectStudyAttachmentTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeDelayedOnsetStudyType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.DelayedOnsetStudyType createPHSHumanSubjectsAndClinicalTrialsInfoTypeDelayedOnsetStudyType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.DelayedOnsetStudyTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType createPHSHumanSubjectsAndClinicalTrialsInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeDelayedOnsetStudyTypeJustificationType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.DelayedOnsetStudyType.JustificationType createPHSHumanSubjectsAndClinicalTrialsInfoTypeDelayedOnsetStudyTypeJustificationType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.DelayedOnsetStudyTypeImpl.JustificationTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeExemptionNumbersType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.ExemptionNumbersType createPHSHumanSubjectsAndClinicalTrialsInfoTypeExemptionNumbersType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.ExemptionNumbersTypeImpl();
    }

    /**
     * Create an instance of PHSHumanSubjectsAndClinicalTrialsInfoTypeHumanSubjectATTType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.PHSHumanSubjectsAndClinicalTrialsInfoType.HumanSubjectATTType createPHSHumanSubjectsAndClinicalTrialsInfoTypeHumanSubjectATTType()
        throws javax.xml.bind.JAXBException
    {
        return new gov.grants.apply.forms.phshumansubjectsandclinicaltrialsinfo_v1.impl.PHSHumanSubjectsAndClinicalTrialsInfoTypeImpl.HumanSubjectATTTypeImpl();
    }

}
