//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class ApplicantOrganizationTypeImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ApplicantOrganizationTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType _OrganizationClassification;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.class);
    }

    public edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType getOrganizationClassification() {
        return _OrganizationClassification;
    }

    public void setOrganizationClassification(edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType value) {
        _OrganizationClassification = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
        context.startElement("", "OrganizationClassification");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _OrganizationClassification), "OrganizationClassification");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _OrganizationClassification), "OrganizationClassification");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _OrganizationClassification), "OrganizationClassification");
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeAttributes(context);
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'"
+"com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameCla"
+"sst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar."
+"ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontent"
+"Modelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~"
+"\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang"
+".Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.datatype.xsd."
+"StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatyp"
+"e.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.x"
+"sd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDat"
+"atypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\bt"
+"ypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteS"
+"paceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006string"
+"sr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpressi"
+"on\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001cpsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ej"
+"B\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xpq\u0000~\u0000%q\u0000~\u0000$sr\u0000\u001d"
+"com.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun."
+"msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000"
+"~\u0000\u0013xq\u0000~\u0000\u0003q\u0000~\u0000\u001cpsq\u0000~\u0000\u0017ppsr\u0000\"com.sun.msv.datatype.xsd.QnameTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\'q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000"
+"4q\u0000~\u0000$sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tl"
+"ocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xr\u0000\u001dcom.sun.msv.grammar.Nam"
+"eClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchem"
+"a-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpressi"
+"on\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u001b\u0001q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0010OrganizationNamet\u0000\u0000s"
+"q\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0017ppsr\u0000\'com.sun.msv.datatype.xsd.FinalCo"
+"mponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom.sun.msv.datatype.xsd."
+"Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDa"
+"tatypeImpl;xq\u0000~\u0000 t\u00009http://era.nih.gov/Projectmgmt/SBIR/CGAP"
+"/common.namespacet\u0000\bDUNSTypeq\u0000~\u00006sr\u0000\"com.sun.msv.datatype.xs"
+"d.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dq\u0000~\u0000$t\u0000\u0005tokenq\u0000~\u00006\u0001\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~"
+"\u0000+q\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0010Orga"
+"nizationDUNSq\u0000~\u0000Bsq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0017ppq\u0000~\u0000Mq\u0000~\u0000*sq\u0000~\u0000+q\u0000"
+"~\u0000Nq\u0000~\u0000$sq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u000fOrganiza"
+"tionEINq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000\u0012q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Vsq\u0000~\u0000-ppsq\u0000~\u0000/"
+"q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u000fOrganizationTINq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000"
+"-ppsq\u0000~\u0000\u0012q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q"
+"\u0000~\u0000>sq\u0000~\u00008t\u0000\fPHSAccountIDq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012pp"
+"\u0000sq\u0000~\u0000-ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001c"
+"com.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000"
+"\u001cpsq\u0000~\u0000/q\u0000~\u0000\u001cpsr\u00002com.sun.msv.grammar.Expression$AnyStringEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000?q\u0000~\u0000ssr\u0000 com.sun.msv.grammar.A"
+"nyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00009q\u0000~\u0000>sq\u0000~\u00008t\u0000>edu.mit.coeus.util"
+"s.xml.bean.proposal.common.PostalAddressTypet\u0000+http://java.s"
+"un.com/jaxb/xjc/dummy-elementssq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:"
+"q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0013OrganizationAddressq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000\u0012q\u0000~\u0000\u001cp\u0000s"
+"q\u0000~\u0000\u0000ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0012Orga"
+"nizationCountyq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000-ppsq\u0000~\u0000\u0012q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u001asq"
+"\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0012OrganizationalUnit"
+"q\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~"
+"\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000!OrganizationCongressionalDistrictq\u0000~\u0000Bsq\u0000~\u0000\u0012"
+"pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Vsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0018"
+"OrganizationCategoryCodeq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000\u0012q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0000ppq\u0000"
+"~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u001fOrganizationC"
+"ategoryDescriptionq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000-"
+"ppsq\u0000~\u0000nq\u0000~\u0000\u001cpsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000>sq\u0000~\u00008t\u0000aedu.mit.co"
+"eus.utils.xml.bean.proposal.rar.ApplicantOrganizationType.Or"
+"ganizationContactPersonTypeq\u0000~\u0000xsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~"
+"\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0019OrganizationContactPersonq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000\u0012"
+"q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u0000"
+"8t\u0000\nCageNumberq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012pp\u0000sq\u0000~\u0000-ppsq"
+"\u0000~\u0000nq\u0000~\u0000\u001cpsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000>sq\u0000~\u00008t\u0000bedu.mit.coeus."
+"utils.xml.bean.proposal.nih.ApplicantOrganizationType.Organi"
+"zationClassificationTypeq\u0000~\u0000xsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q"
+"\u0000~\u0000>sq\u0000~\u00008t\u0000\u001aOrganizationClassificationq\u0000~\u0000Bsr\u0000\"com.sun.msv."
+"grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/"
+"grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar."
+"ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersion"
+"L\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u00005\u0001pq\u0000~\u0000"
+"\u0007q\u0000~\u0000\fq\u0000~\u0000\bq\u0000~\u0000\u000bq\u0000~\u0000Uq\u0000~\u0000^q\u0000~\u0000\u0092q\u0000~\u0000pq\u0000~\u0000\u00a2q\u0000~\u0000\u00b5q\u0000~\u0000\u0005q\u0000~\u0000kq\u0000~\u0000"
+"\u0011q\u0000~\u0000\tq\u0000~\u0000\u009fq\u0000~\u0000mq\u0000~\u0000\u00a1q\u0000~\u0000\u00b4q\u0000~\u0000\u00b2q\u0000~\u0000\\q\u0000~\u0000\rq\u0000~\u0000cq\u0000~\u0000}q\u0000~\u0000\u0084q\u0000~\u0000"
+"\u0097q\u0000~\u0000\u00aaq\u0000~\u0000\u0016q\u0000~\u0000eq\u0000~\u0000\u007fq\u0000~\u0000\u000fq\u0000~\u0000\u0086q\u0000~\u0000\u008cq\u0000~\u0000\u0099q\u0000~\u0000\u00acq\u0000~\u0000\nq\u0000~\u0000.q\u0000~\u0000"
+"Pq\u0000~\u0000Xq\u0000~\u0000_q\u0000~\u0000fq\u0000~\u0000yq\u0000~\u0000\u0080q\u0000~\u0000\u0087q\u0000~\u0000\u008dq\u0000~\u0000\u0093q\u0000~\u0000\u009aq\u0000~\u0000\u00a6q\u0000~\u0000\u0010q\u0000~\u0000"
+"\u00adq\u0000~\u0000\u00b9q\u0000~\u0000\u0006q\u0000~\u0000\u000eq\u0000~\u0000Dx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public static class OrganizationClassificationTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
    {

        protected java.lang.String _SubCategoryCode;
        protected java.lang.String _CategoryCode;
        public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
        private static com.sun.msv.grammar.Grammar schemaFragment;

        private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
            return (edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType.class);
        }

        public java.lang.String getSubCategoryCode() {
            return _SubCategoryCode;
        }

        public void setSubCategoryCode(java.lang.String value) {
            _SubCategoryCode = value;
        }

        public java.lang.String getCategoryCode() {
            return _CategoryCode;
        }

        public void setCategoryCode(java.lang.String value) {
            _CategoryCode = value;
        }

        public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.OrganizationClassificationTypeImpl.Unmarshaller(context);
        }

        public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
            throws org.xml.sax.SAXException
        {
            context.startElement("", "CategoryCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CategoryCode), "CategoryCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
            context.startElement("", "SubCategoryCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SubCategoryCode), "SubCategoryCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
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
            return (edu.mit.coeus.utils.xml.bean.proposal.nih.ApplicantOrganizationType.OrganizationClassificationType.class);
        }

        public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
            if (schemaFragment == null) {
                schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.s"
+"un.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/dataty"
+"pe/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/String"
+"Pair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlw"
+"aysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+"\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamesp"
+"aceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0014L\u0000\nwhiteSpacet\u0000.L"
+"com/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www"
+".w3.org/2001/XMLSchemat\u0000\u0005tokensr\u00005com.sun.msv.datatype.xsd.W"
+"hiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.gram"
+"mar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.la"
+"ng.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.String"
+"Pair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0014L\u0000\fnamespaceURIq\u0000~\u0000\u0014xpq\u0000~\u0000\u0018q"
+"\u0000~\u0000\u0017sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 "
+"com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnam"
+"eClassq\u0000~\u0000\u0007xq\u0000~\u0000\u0003q\u0000~\u0000\u001fpsq\u0000~\u0000\u000bppsr\u0000\"com.sun.msv.datatype.xsd."
+"QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0011q\u0000~\u0000\u0017t\u0000\u0005QNameq\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000 q\u0000~\u0000)"
+"q\u0000~\u0000\u0017sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlo"
+"calNameq\u0000~\u0000\u0014L\u0000\fnamespaceURIq\u0000~\u0000\u0014xr\u0000\u001dcom.sun.msv.grammar.Name"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema"
+"-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u001e\u0001q\u0000~\u00001sq\u0000~\u0000+t\u0000\fCategoryCodet\u0000\u0000sq\u0000~\u0000\u0006"
+"pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u000esq\u0000~\u0000\"ppsq\u0000~\u0000$q\u0000~\u0000\u001fpq\u0000~\u0000&q\u0000~\u0000-q\u0000~\u00001sq\u0000~\u0000+t\u0000\u000f"
+"SubCategoryCodeq\u0000~\u00005sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$C"
+"losedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHas"
+"h\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv"
+"/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0005\u0001pq\u0000~\u0000\nq\u0000~\u00007q\u0000~\u0000\u0005q\u0000~\u0000#q\u0000~\u00008x"));
            }
            return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
        }

        public class Unmarshaller
            extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
        {


            public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
                super(context, "-------");
            }

            protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
                this(context);
                state = startState;
            }

            public java.lang.Object owner() {
                return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.OrganizationClassificationTypeImpl.this;
            }

            public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
                throws org.xml.sax.SAXException
            {
                int attIdx;
                outer:
                while (true) {
                    switch (state) {
                        case  0 :
                            if (("CategoryCode" == ___local)&&("" == ___uri)) {
                                context.pushAttributes(__atts, true);
                                state = 1;
                                return ;
                            }
                            break;
                        case  6 :
                            revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        case  3 :
                            if (("SubCategoryCode" == ___local)&&("" == ___uri)) {
                                context.pushAttributes(__atts, true);
                                state = 4;
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
                        case  5 :
                            if (("SubCategoryCode" == ___local)&&("" == ___uri)) {
                                context.popAttributes();
                                state = 6;
                                return ;
                            }
                            break;
                        case  2 :
                            if (("CategoryCode" == ___local)&&("" == ___uri)) {
                                context.popAttributes();
                                state = 3;
                                return ;
                            }
                            break;
                        case  6 :
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
                        case  6 :
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
                        case  6 :
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
                            case  4 :
                                eatText1(value);
                                state = 5;
                                return ;
                            case  1 :
                                eatText2(value);
                                state = 2;
                                return ;
                            case  6 :
                                revertToParentFromText(value);
                                return ;
                        }
                    } catch (java.lang.RuntimeException e) {
                        handleUnexpectedTextException(value, e);
                    }
                    break;
                }
            }

            private void eatText1(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                try {
                    _SubCategoryCode = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
                } catch (java.lang.Exception e) {
                    handleParseConversionException(e);
                }
            }

            private void eatText2(final java.lang.String value)
                throws org.xml.sax.SAXException
            {
                try {
                    _CategoryCode = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
                } catch (java.lang.Exception e) {
                    handleParseConversionException(e);
                }
            }

        }

    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "-----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  2 :
                        if (("CategoryCode" == ___local)&&("" == ___uri)) {
                            _OrganizationClassification = ((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.OrganizationClassificationTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.OrganizationClassificationTypeImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("OrganizationName" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ApplicantOrganizationTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ApplicantOrganizationTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  4 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("OrganizationClassification" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 2;
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
                    case  3 :
                        if (("OrganizationClassification" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  4 :
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
                    case  4 :
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
                    case  4 :
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
                        case  4 :
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
