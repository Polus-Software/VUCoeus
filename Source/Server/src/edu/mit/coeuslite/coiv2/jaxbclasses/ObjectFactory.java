//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.mit.coeuslite.coiv2.jaxbclasses package. 
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
    extends edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(36, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.GrammarInfo grammarInfo = new edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (edu.mit.coeuslite.coiv2.jaxbclasses.ObjectFactory.class));
    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.jaxbclasses.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDET.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.PERFININTDISCLDETImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSUREType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLOSURETypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCDETAILSType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCDETAILSTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTTYPE.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTTYPEImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCDETAILS.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCDETAILSImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPE.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADENTRYTYPEImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPEType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADENTRYTYPETypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPAD.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSURE.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLOSUREImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COISTATUSType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COISTATUSTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUS.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTTYPEType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTTYPETypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COISTATUS.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COISTATUSImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIUSERROLES.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIUSERROLESImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.QUESTIONType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.QUESTIONTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUSType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTSType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTSTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTS.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTSImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTSType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIENTITYSTATUSCODETypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODE.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIENTITYSTATUSCODEImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDETType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.PERFININTDISCLDETTypeImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.QUESTION.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.QUESTIONImpl");
        defaultImplementations.put((edu.mit.coeuslite.coiv2.jaxbclasses.COIUSERROLESType.class), "edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIUSERROLESTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "QUESTION"), (edu.mit.coeuslite.coiv2.jaxbclasses.QUESTION.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DISCL_PROJECTS"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DOCUMENT_TYPE"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTTYPE.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_NOTEPAD_ENTRY_TYPE"), (edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPE.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DISPOSITION_STATUS"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUS.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "PER_FIN_INT_DISCL_DET"), (edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDET.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_STATUS"), (edu.mit.coeuslite.coiv2.jaxbclasses.COISTATUS.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_ENTITY_STATUS_CODE"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODE.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_NOTEPAD"), (edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPAD.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DOCUMENTS"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTS.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DISC_DETAILS"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCDETAILS.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_USER_ROLES"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIUSERROLES.class));
        rootTagMap.put(new javax.xml.namespace.QName("", "COI_DISCLOSURE"), (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSURE.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.mit.coeuslite.coiv2.jaxbclasses
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
     * Create an instance of PERFININTDISCLDET
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDET createPERFININTDISCLDET()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.PERFININTDISCLDETImpl();
    }

    /**
     * Create an instance of COIDISCLOSUREType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSUREType createCOIDISCLOSUREType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLOSURETypeImpl();
    }

    /**
     * Create an instance of COIDISCDETAILSType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCDETAILSType createCOIDISCDETAILSType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCDETAILSTypeImpl();
    }

    /**
     * Create an instance of COIDOCUMENTTYPE
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTTYPE createCOIDOCUMENTTYPE()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTTYPEImpl();
    }

    /**
     * Create an instance of COIDISCDETAILS
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCDETAILS createCOIDISCDETAILS()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCDETAILSImpl();
    }

    /**
     * Create an instance of COINOTEPADENTRYTYPE
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPE createCOINOTEPADENTRYTYPE()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADENTRYTYPEImpl();
    }

    /**
     * Create an instance of COINOTEPADType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADType createCOINOTEPADType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADTypeImpl();
    }

    /**
     * Create an instance of COINOTEPADENTRYTYPEType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPADENTRYTYPEType createCOINOTEPADENTRYTYPEType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADENTRYTYPETypeImpl();
    }

    /**
     * Create an instance of COINOTEPAD
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COINOTEPAD createCOINOTEPAD()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COINOTEPADImpl();
    }

    /**
     * Create an instance of COIDISCLOSURE
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLOSURE createCOIDISCLOSURE()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLOSUREImpl();
    }

    /**
     * Create an instance of COIDISCLPROJECTS
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS createCOIDISCLPROJECTS()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSImpl();
    }

    /**
     * Create an instance of COISTATUSType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COISTATUSType createCOISTATUSType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COISTATUSTypeImpl();
    }

    /**
     * Create an instance of COIDISPOSITIONSTATUS
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUS createCOIDISPOSITIONSTATUS()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSImpl();
    }

    /**
     * Create an instance of COIDOCUMENTTYPEType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTTYPEType createCOIDOCUMENTTYPEType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTTYPETypeImpl();
    }

    /**
     * Create an instance of COISTATUS
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COISTATUS createCOISTATUS()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COISTATUSImpl();
    }

    /**
     * Create an instance of COIUSERROLES
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIUSERROLES createCOIUSERROLES()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIUSERROLESImpl();
    }

    /**
     * Create an instance of QUESTIONType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.QUESTIONType createQUESTIONType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.QUESTIONTypeImpl();
    }

    /**
     * Create an instance of COIDISPOSITIONSTATUSType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUSType createCOIDISPOSITIONSTATUSType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSTypeImpl();
    }

    /**
     * Create an instance of COIDOCUMENTSType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTSType createCOIDOCUMENTSType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTSTypeImpl();
    }

    /**
     * Create an instance of COIDOCUMENTS
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDOCUMENTS createCOIDOCUMENTS()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDOCUMENTSImpl();
    }

    /**
     * Create an instance of COIDISCLPROJECTSType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTSType createCOIDISCLPROJECTSType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSTypeImpl();
    }

    /**
     * Create an instance of COIENTITYSTATUSCODEType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODEType createCOIENTITYSTATUSCODEType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIENTITYSTATUSCODETypeImpl();
    }

    /**
     * Create an instance of COIENTITYSTATUSCODE
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIENTITYSTATUSCODE createCOIENTITYSTATUSCODE()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIENTITYSTATUSCODEImpl();
    }

    /**
     * Create an instance of PERFININTDISCLDETType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.PERFININTDISCLDETType createPERFININTDISCLDETType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.PERFININTDISCLDETTypeImpl();
    }

    /**
     * Create an instance of QUESTION
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.QUESTION createQUESTION()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.QUESTIONImpl();
    }

    /**
     * Create an instance of COIUSERROLESType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeuslite.coiv2.jaxbclasses.COIUSERROLESType createCOIUSERROLESType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIUSERROLESTypeImpl();
    }

}
