//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class ResearchCoverPageImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ResearchCoverPageTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.rar.ResearchCoverPage, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ResearchCoverPage.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "ResearchCoverPage";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ResearchCoverPageImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace", "ResearchCoverPage");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ResearchCoverPage.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001fcom.sun.msv.gra"
+"mmar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq"
+"\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun."
+"msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000"
+"~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000$psr\u00002com.sun.msv.grammar.Expression$AnyStringE"
+"xpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000#\u0001q\u0000~\u0000(sr\u0000 com.sun.msv.gramma"
+"r.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000)q\u0000~\u0000.sr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fname"
+"spaceURIq\u0000~\u00000xq\u0000~\u0000+t\u0000Dedu.mit.coeus.utils.xml.bean.proposal."
+"rar.CoreSubmissionCategoryTypet\u0000+http://java.sun.com/jaxb/xj"
+"c/dummy-elementssq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$psr\u0000\u001bcom.sun.msv.grammar."
+"DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006"
+"exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr"
+"\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.m"
+"sv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv."
+"datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype"
+".xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u00000L\u0000\btypeNam"
+"eq\u0000~\u00000L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpacePr"
+"ocessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000$psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L"
+"\u0000\tlocalNameq\u0000~\u00000L\u0000\fnamespaceURIq\u0000~\u00000xpq\u0000~\u0000Aq\u0000~\u0000@sq\u0000~\u0000/t\u0000\u0004typ"
+"et\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0012S"
+"ubmissionCategoryt\u0000\u0000sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 "
+"q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Eedu.mit.coeus.util"
+"s.xml.bean.proposal.rar.CoreApplicationCategoryTypeq\u0000~\u00003sq\u0000~"
+"\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0013ApplicationCategoryq"
+"\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000"
+"$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Oedu.mit.coeus.utils.xml.bean.propo"
+"sal.rar.CoreApplicantSubmissionQualifiersTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq"
+"\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u001dApplicantSubmissionQualifi"
+"ersq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%"
+"q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Pedu.mit.coeus.utils.xml.bean.p"
+"roposal.rar.CoreFederalAgencyReceiptQualifiersTypeq\u0000~\u00003sq\u0000~\u0000"
+"\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u001eFederalAgencyReceiptQ"
+"ualifiersq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$p"
+"sq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Hedu.mit.coeus.utils.xml."
+"bean.proposal.rar.CoreStateReceiptQualifiersTypeq\u0000~\u00003sq\u0000~\u0000\u001ep"
+"psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0016StateReceiptQualifiersq"
+"\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000"
+"$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Nedu.mit.coeus.utils.xml.bean.propo"
+"sal.rar.CoreStateIntergovernmentalReviewTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000"
+"~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u001cStateIntergovernmentalRevie"
+"wq\u0000~\u0000Nsq\u0000~\u0000\u001eppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000"
+"~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000?edu.mit.coeus.utils."
+"xml.bean.proposal.rar.CoreCFDAQuestionsTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~"
+"\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\rCFDAQuestionsq\u0000~\u0000Nq\u0000~\u0000.sq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000"
+"~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Qedu.mit.coeus.utils.xml.bean.proposal.rar.C"
+"oreFederalDebtDelinquencyQuestionsTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~"
+"\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u001fFederalDebtDelinquencyQuestionsq\u0000"
+"~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$"
+"pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000>edu.mit.coeus.utils.xml.bean.propos"
+"al.rar.CoreProjectDatesTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~"
+"\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\fProjectDatesq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000s"
+"q\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000>edu.m"
+"it.coeus.utils.xml.bean.proposal.rar.CoreBudgetTotalsTypeq\u0000~"
+"\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\fBudgetTotalsq\u0000"
+"~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006ppsr\u0000\'com.sun.msv.datatype.xsd.Fin"
+"alComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom.sun.msv.datatype."
+"xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/"
+"XSDatatypeImpl;xq\u0000~\u0000=t\u0000Ehttp://era.nih.gov/Projectmgmt/SBIR/"
+"CGAP/researchandrelated.namespacet\u0000\u0010CoreProjectTitlesr\u00005com."
+"sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000Csr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\ri"
+"sAlwaysValidxq\u0000~\u0000;q\u0000~\u0000@t\u0000\u0006stringq\u0000~\u0000\u00c6\u0001\u0000\u0000\u0000\u0000q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00c9q\u0000"
+"~\u0000\u00c3sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\fProjectTitleq"
+"\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000"
+"$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Bedu.mit.coeus.utils.xml.bean.propo"
+"sal.rar.OtherAgencyQuestionsTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~"
+"\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0014OtherAgencyQuestionsq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.s"
+"q\u0000~\u0000/t\u0000Cedu.mit.coeus.utils.xml.bean.proposal.rar.ApplicantO"
+"rganizationTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000"
+"/t\u0000\u0015ApplicantOrganizationq\u0000~\u0000Nsq\u0000~\u0000\u001eppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000"
+"/t\u00009edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteTyp"
+"eq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0012PrimaryPro"
+"jectSiteq\u0000~\u0000Nq\u0000~\u0000.sq\u0000~\u0000\u001eppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000"
+"%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Nedu.mit.coeus.utils.xml.bean."
+"proposal.rar.ProgramDirectorPrincipalInvestigatorq\u0000~\u00003sq\u0000~\u0000\u0000"
+"pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~"
+"\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Redu.mit.coeus.utils.xml.bean.proposal.rar.Pr"
+"ogramDirectorPrincipalInvestigatorTypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~"
+"\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000$ProgramDirectorPrincipalInvestiga"
+"torq\u0000~\u0000\u00c3sq\u0000~\u0000\u001eppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 "
+"q\u0000~\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Gedu.mit.coeus.util"
+"s.xml.bean.proposal.rar.FundingOpportunityDetailsTypeq\u0000~\u00003sq"
+"\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0019FundingOpportunity"
+"Detailsq\u0000~\u0000Nq\u0000~\u0000.sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u001eppsq\u0000~\u0000 q\u0000~"
+"\u0000$psq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u0000(q\u0000~\u0000,q\u0000~\u0000.sq\u0000~\u0000/t\u0000Tedu.mit.coeus.utils.x"
+"ml.bean.proposal.rar.AuthorizedOrganizationalRepresentativeT"
+"ypeq\u0000~\u00003sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000&Authoriz"
+"edOrganizationalRepresentativeq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006pp"
+"sr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun."
+"msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000;q\u0000~\u0000@t\u0000\u0004da"
+"teq\u0000~\u0000Dq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0001&q\u0000~\u0000@sq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000~\u00009q\u0000~\u0000Iq\u0000"
+"~\u0000.sq\u0000~\u0000/t\u0000\u0015OfficialSignatureDateq\u0000~\u0000Nsq\u0000~\u0000\u001eppsq\u0000~\u0000%q\u0000~\u0000$pq\u0000"
+"~\u00009q\u0000~\u0000Iq\u0000~\u0000.sq\u0000~\u0000/t\u0000\u0011ResearchCoverPageq\u0000~\u0000\u00c3sr\u0000\"com.sun.msv."
+"grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/"
+"grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar."
+"ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersion"
+"L\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000]\u0001pq\u0000~\u0000"
+"\u0013q\u0000~\u0000\u00dfq\u0000~\u0000\u00ecq\u0000~\u0000\u00f7q\u0000~\u0000\u00ffq\u0000~\u0001\fq\u0000~\u0001\u0018q\u0000~\u0000\fq\u0000~\u0000\u0017q\u0000~\u0000\u0019q\u0000~\u0000\u0015q\u0000~\u0000\u0018q\u0000~\u0000"
+"\u00d0q\u0000~\u0000\u00b1q\u0000~\u0000\u00a5q\u0000~\u0000\u0099q\u0000~\u0000\u008dq\u0000~\u0000\u0080q\u0000~\u0000tq\u0000~\u0000hq\u0000~\u0000\\q\u0000~\u0000Pq\u0000~\u0000\u001cq\u0000~\u0000\u00dcq\u0000~\u0000"
+"\u00e9q\u0000~\u0000\u00fcq\u0000~\u0000\u00f4q\u0000~\u0001\tq\u0000~\u0001\u0015q\u0000~\u0000\nq\u0000~\u0000\u008bq\u0000~\u0000\u00e7q\u0000~\u0001\u0007q\u0000~\u0000\u00d2q\u0000~\u0000\u00b3q\u0000~\u0000\u00a7q\u0000~\u0000"
+"\u009bq\u0000~\u0000\u008fq\u0000~\u0000\u0082q\u0000~\u0000vq\u0000~\u0000jq\u0000~\u0000^q\u0000~\u0000Rq\u0000~\u0000\u001fq\u0000~\u0000\u0016q\u0000~\u0000\u00deq\u0000~\u0000\u000fq\u0000~\u0000\u00ebq\u0000~\u0000"
+"\u00f6q\u0000~\u0000\u00feq\u0000~\u0000\u000eq\u0000~\u0001\u000bq\u0000~\u0001\u0017q\u0000~\u0000\tq\u0000~\u0000\u0014q\u0000~\u0000\u0010q\u0000~\u0000\u00d7q\u0000~\u0000\u00cbq\u0000~\u0000\u00b8q\u0000~\u0000\u00acq\u0000~\u0000"
+"\u00a0q\u0000~\u0000\u0094q\u0000~\u0000\u0087q\u0000~\u0000{q\u0000~\u0000oq\u0000~\u0000cq\u0000~\u0000Wq\u0000~\u00004q\u0000~\u0000\u001aq\u0000~\u0000\u00e3q\u0000~\u0000\u00f0q\u0000~\u0001\u0003q\u0000~\u0001"
+"\u0010q\u0000~\u0001\u001cq\u0000~\u0001(q\u0000~\u0001!q\u0000~\u0001,q\u0000~\u0000\u000bq\u0000~\u0000\u0012q\u0000~\u0000\u00bdq\u0000~\u0000\rq\u0000~\u0000\u0011q\u0000~\u0000\u00d3q\u0000~\u0000\u00b4q\u0000~\u0000"
+"\u00a8q\u0000~\u0000\u009cq\u0000~\u0000\u0090q\u0000~\u0000\u0083q\u0000~\u0000wq\u0000~\u0000kq\u0000~\u0000_q\u0000~\u0000Sq\u0000~\u0000\"x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ResearchCoverPageImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("SubmissionCategory" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ResearchCoverPageTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ResearchCoverPageImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("ResearchCoverPage" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  2 :
                        if (("ResearchCoverPage" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveElement(___uri, ___local, ___qname);
                break;
            }
        }

        public void enterAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.enterAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void leaveAttribute(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                }
                super.leaveAttribute(___uri, ___local, ___qname);
                break;
            }
        }

        public void handleText(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                try {
                    switch (state) {
                        case  3 :
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
