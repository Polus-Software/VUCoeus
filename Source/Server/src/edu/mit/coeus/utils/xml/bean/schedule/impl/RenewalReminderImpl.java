//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule.impl;

public class RenewalReminderImpl
    extends edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderTypeImpl
    implements edu.mit.coeus.utils.xml.bean.schedule.RenewalReminder, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.schedule.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.schedule.RenewalReminder.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://irb.mit.edu/irbnamespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RenewalReminder";
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://irb.mit.edu/irbnamespace", "RenewalReminder");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.schedule.RenewalReminder.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\bppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsr\u0000 com.sun.msv.grammar.OneOrMoreExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003exp"
+"q\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tname"
+"Classq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0016psr\u00002com.sun.msv.grammar.Expression$Any"
+"StringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0015\u0001q\u0000~\u0000\u001asr\u0000 com.sun.msv"
+".grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.Name"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001bq\u0000~\u0000 sr\u0000#com.sun.msv.grammar"
+".SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;"
+"L\u0000\fnamespaceURIq\u0000~\u0000\"xq\u0000~\u0000\u001dt\u00009edu.mit.coeus.utils.xml.bean.sc"
+"hedule.CommitteeMasterDatat\u0000+http://java.sun.com/jaxb/xjc/du"
+"mmy-elementssq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq"
+"\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!t\u0000=edu.mit.coeus.utils.xml.be"
+"an.schedule.CommitteeMasterDataTypeq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016p"
+"sr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relax"
+"ng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/ut"
+"il/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnam"
+"espaceUriq\u0000~\u0000\"L\u0000\btypeNameq\u0000~\u0000\"L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/d"
+"atatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/"
+"XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProc"
+"essor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression"
+"$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u0016psr\u0000\u001bcom.sun.msv.uti"
+"l.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\"L\u0000\fnamespaceURIq\u0000~\u0000\""
+"xpq\u0000~\u0000;q\u0000~\u0000:sq\u0000~\u0000!t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema"
+"-instanceq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0013CommitteeMasterDatat\u0000\u001fhttp://irb.mit."
+"edu/irbnamespacesq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q"
+"\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!t\u0000.edu.mit.coeus.utils.xml.bean.sc"
+"hedule.Protocolq\u0000~\u0000%sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012"
+"q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!t\u00002edu.mit.coeus.util"
+"s.xml.bean.schedule.ProtocolTypeq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~"
+"\u00003q\u0000~\u0000Cq\u0000~\u0000 sq\u0000~\u0000!t\u0000\bProtocolq\u0000~\u0000Hsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0007q\u0000~\u0000\u0016psq\u0000~\u0000\u000e"
+"q\u0000~\u0000\u0016psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000"
+"\u001eq\u0000~\u0000 sq\u0000~\u0000!t\u00006edu.mit.coeus.utils.xml.bean.schedule.NextSch"
+"eduleDateq\u0000~\u0000%sq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000"
+"~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!t\u0000:edu.mit.coeus.utils."
+"xml.bean.schedule.NextScheduleDateTypeq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~"
+"\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0010NextScheduleDateq\u0000~\u0000Hsq\u0000~\u0000\u000eppsq\u0000~"
+"\u0000\u0007q\u0000~\u0000\u0016psq\u0000~\u0000\u000eq\u0000~\u0000\u0016psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q"
+"\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000dq\u0000~\u0000%sq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q"
+"\u0000~\u0000lq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 q\u0000~\u0000osq\u0000~\u0000\u000eppsq\u0000"
+"~\u0000\u0007q\u0000~\u0000\u0016psq\u0000~\u0000\u000eq\u0000~\u0000\u0016psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017"
+"q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000dq\u0000~\u0000%sq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!"
+"q\u0000~\u0000lq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 q\u0000~\u0000osq\u0000~\u0000\u000eppsq"
+"\u0000~\u0000\u0007q\u0000~\u0000\u0016psq\u0000~\u0000\u000eq\u0000~\u0000\u0016psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000"
+"\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000dq\u0000~\u0000%sq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000"
+"!q\u0000~\u0000lq\u0000~\u0000%sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 q\u0000~\u0000osq\u0000~\u0000\u000epps"
+"q\u0000~\u0000\u000eq\u0000~\u0000\u0016psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000"
+"\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000dq\u0000~\u0000%sq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000"
+"~\u0000\u000eppsq\u0000~\u0000\u0012q\u0000~\u0000\u0016psq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000\u001aq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000lq\u0000~\u0000%"
+"sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 q\u0000~\u0000oq\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 "
+"q\u0000~\u0000 sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00000q\u0000~\u0000\u0016psr\u0000#com.sun.ms"
+"v.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u00005q\u0000"
+"~\u0000:t\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000=\u0001q\u0000~\u0000@sq\u0000~\u0000Aq\u0000~\u0000\u00baq\u0000~\u0000:sq\u0000~\u0000\u000eppsq\u0000~"
+"\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u000bCurrentUserq\u0000~\u0000Hq\u0000~\u0000 sq\u0000~\u0000\u000ep"
+"psq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00000q\u0000~\u0000\u0016psr\u0000!com.sun.msv.datatype.x"
+"sd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeB"
+"aseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u00005q\u0000~\u0000:t\u0000\u0004dateq\u0000~\u0000>q\u0000~\u0000@sq\u0000~\u0000Aq\u0000~\u0000\u00c9q\u0000~"
+"\u0000:sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u000bCurrentDateq\u0000~"
+"\u0000Hq\u0000~\u0000 sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u00003q\u0000~\u0000Cq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u000fRenewalRe"
+"minderq\u0000~\u0000Hsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash"
+";xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c"
+"\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/"
+"ExpressionPool;xp\u0000\u0000\u0000F\u0001pq\u0000~\u0000rq\u0000~\u0000\fq\u0000~\u0000\nq\u0000~\u0000\u0082q\u0000~\u0000\u00afq\u0000~\u0000\u00a8q\u0000~\u0000\u009fq\u0000"
+"~\u0000\u0098q\u0000~\u0000\u008eq\u0000~\u0000\u0087q\u0000~\u0000}q\u0000~\u0000vq\u0000~\u0000iq\u0000~\u0000aq\u0000~\u0000Tq\u0000~\u0000Lq\u0000~\u0000*q\u0000~\u0000\u0014q\u0000~\u0000\u0094q\u0000"
+"~\u0000\u0083q\u0000~\u0000\u00b6q\u0000~\u0000\tq\u0000~\u0000\u00c4q\u0000~\u0000]q\u0000~\u0000\u00a4q\u0000~\u0000\u0093q\u0000~\u0000\u00b4q\u0000~\u0000qq\u0000~\u0000\u00b2q\u0000~\u0000\u00a2q\u0000~\u0000\u0091q\u0000"
+"~\u0000\u0080q\u0000~\u0000mq\u0000~\u0000Xq\u0000~\u0000.q\u0000~\u0000\u00beq\u0000~\u0000\u00cbq\u0000~\u0000\rq\u0000~\u0000\u00cfq\u0000~\u0000\u000bq\u0000~\u0000\\q\u0000~\u0000\u00a5q\u0000~\u0000\u0095q\u0000"
+"~\u0000\u0084q\u0000~\u0000sq\u0000~\u0000^q\u0000~\u0000Iq\u0000~\u0000\u000fq\u0000~\u0000\u00acq\u0000~\u0000\u009cq\u0000~\u0000\u008bq\u0000~\u0000zq\u0000~\u0000fq\u0000~\u0000Qq\u0000~\u0000\'q\u0000"
+"~\u0000\u00aeq\u0000~\u0000\u00a7q\u0000~\u0000\u009eq\u0000~\u0000\u0097q\u0000~\u0000\u008dq\u0000~\u0000\u0086q\u0000~\u0000|q\u0000~\u0000uq\u0000~\u0000hq\u0000~\u0000`q\u0000~\u0000Sq\u0000~\u0000Kq\u0000"
+"~\u0000)q\u0000~\u0000\u0011q\u0000~\u0000\u00c2x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("RenewalReminder" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderTypeImpl)edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderTypeImpl)edu.mit.coeus.utils.xml.bean.schedule.impl.RenewalReminderImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
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
                        if (("RenewalReminder" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
