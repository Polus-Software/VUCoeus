//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.mit.coeus.xml.instprop package. 
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
    extends edu.mit.coeus.xml.instprop.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(33, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static edu.mit.coeus.xml.instprop.impl.runtime.GrammarInfo grammarInfo = new edu.mit.coeus.xml.instprop.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (edu.mit.coeus.xml.instprop.ObjectFactory.class));
    public final static java.lang.Class version = (edu.mit.coeus.xml.instprop.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((edu.mit.coeus.xml.instprop.DisclosureItemType.class), "edu.mit.coeus.xml.instprop.impl.DisclosureItemTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.NoticeOfOppType.class), "edu.mit.coeus.xml.instprop.impl.NoticeOfOppTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.ProposalType.class), "edu.mit.coeus.xml.instprop.impl.ProposalTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.InstituteProposalType.class), "edu.mit.coeus.xml.instprop.impl.InstituteProposalTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.OtherGroupDetailsType.class), "edu.mit.coeus.xml.instprop.impl.OtherGroupDetailsTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.IDCRateType.class), "edu.mit.coeus.xml.instprop.impl.IDCRateTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.NSFcodeType.class), "edu.mit.coeus.xml.instprop.impl.NSFcodeTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.BudgetDataType.class), "edu.mit.coeus.xml.instprop.impl.BudgetDataTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.KeyPersonType.class), "edu.mit.coeus.xml.instprop.impl.KeyPersonTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.OtherGroupType.class), "edu.mit.coeus.xml.instprop.impl.OtherGroupTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.PersonType.class), "edu.mit.coeus.xml.instprop.impl.PersonTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.AnticipatedAwardType.class), "edu.mit.coeus.xml.instprop.impl.AnticipatedAwardTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.MailingInfoType.class), "edu.mit.coeus.xml.instprop.impl.MailingInfoTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.SpecialReviewType.class), "edu.mit.coeus.xml.instprop.impl.SpecialReviewTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.InstProposalMasterData.class), "edu.mit.coeus.xml.instprop.impl.InstProposalMasterDataImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.ActivityType.class), "edu.mit.coeus.xml.instprop.impl.ActivityTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.UnitType.class), "edu.mit.coeus.xml.instprop.impl.UnitTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.InvestigatorType.class), "edu.mit.coeus.xml.instprop.impl.InvestigatorTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.InstituteProposal.class), "edu.mit.coeus.xml.instprop.impl.InstituteProposalImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.SponsorType.class), "edu.mit.coeus.xml.instprop.impl.SponsorTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.CostSharingType.class), "edu.mit.coeus.xml.instprop.impl.CostSharingTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.ScienceCodeType.class), "edu.mit.coeus.xml.instprop.impl.ScienceCodeTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.ProposalStatusType.class), "edu.mit.coeus.xml.instprop.impl.ProposalStatusTypeImpl");
        defaultImplementations.put((edu.mit.coeus.xml.instprop.SchoolInfoType.class), "edu.mit.coeus.xml.instprop.impl.SchoolInfoTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "InstituteProposal"), (edu.mit.coeus.xml.instprop.InstituteProposal.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.mit.coeus.xml.instprop
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
     * Create an instance of DisclosureItemType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.DisclosureItemType createDisclosureItemType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.DisclosureItemTypeImpl();
    }

    /**
     * Create an instance of NoticeOfOppType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.NoticeOfOppType createNoticeOfOppType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.NoticeOfOppTypeImpl();
    }

    /**
     * Create an instance of ProposalType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.ProposalType createProposalType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.ProposalTypeImpl();
    }

    /**
     * Create an instance of InstituteProposalType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.InstituteProposalType createInstituteProposalType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.InstituteProposalTypeImpl();
    }

    /**
     * Create an instance of OtherGroupDetailsType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.OtherGroupDetailsType createOtherGroupDetailsType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.OtherGroupDetailsTypeImpl();
    }

    /**
     * Create an instance of IDCRateType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.IDCRateType createIDCRateType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.IDCRateTypeImpl();
    }

    /**
     * Create an instance of NSFcodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.NSFcodeType createNSFcodeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.NSFcodeTypeImpl();
    }

    /**
     * Create an instance of BudgetDataType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.BudgetDataType createBudgetDataType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.BudgetDataTypeImpl();
    }

    /**
     * Create an instance of KeyPersonType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.KeyPersonType createKeyPersonType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.KeyPersonTypeImpl();
    }

    /**
     * Create an instance of OtherGroupType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.OtherGroupType createOtherGroupType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.OtherGroupTypeImpl();
    }

    /**
     * Create an instance of PersonType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.PersonType createPersonType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.PersonTypeImpl();
    }

    /**
     * Create an instance of AnticipatedAwardType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.AnticipatedAwardType createAnticipatedAwardType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.AnticipatedAwardTypeImpl();
    }

    /**
     * Create an instance of MailingInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.MailingInfoType createMailingInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.MailingInfoTypeImpl();
    }

    /**
     * Create an instance of SpecialReviewType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.SpecialReviewType createSpecialReviewType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.SpecialReviewTypeImpl();
    }

    /**
     * Create an instance of InstProposalMasterData
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.InstProposalMasterData createInstProposalMasterData()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.InstProposalMasterDataImpl();
    }

    /**
     * Create an instance of ActivityType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.ActivityType createActivityType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.ActivityTypeImpl();
    }

    /**
     * Create an instance of UnitType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.UnitType createUnitType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.UnitTypeImpl();
    }

    /**
     * Create an instance of InvestigatorType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.InvestigatorType createInvestigatorType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.InvestigatorTypeImpl();
    }

    /**
     * Create an instance of InstituteProposal
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.InstituteProposal createInstituteProposal()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.InstituteProposalImpl();
    }

    /**
     * Create an instance of SponsorType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.SponsorType createSponsorType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.SponsorTypeImpl();
    }

    /**
     * Create an instance of CostSharingType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.CostSharingType createCostSharingType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.CostSharingTypeImpl();
    }

    /**
     * Create an instance of ScienceCodeType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.ScienceCodeType createScienceCodeType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.ScienceCodeTypeImpl();
    }

    /**
     * Create an instance of ProposalStatusType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.ProposalStatusType createProposalStatusType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.ProposalStatusTypeImpl();
    }

    /**
     * Create an instance of SchoolInfoType
     * 
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.mit.coeus.xml.instprop.SchoolInfoType createSchoolInfoType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.mit.coeus.xml.instprop.impl.SchoolInfoTypeImpl();
    }

}
