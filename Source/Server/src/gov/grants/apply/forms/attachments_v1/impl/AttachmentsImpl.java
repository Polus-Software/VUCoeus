//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.attachments_v1.impl;

public class AttachmentsImpl
    extends gov.grants.apply.forms.attachments_v1.impl.AttachmentsTypeImpl
    implements gov.grants.apply.forms.attachments_v1.Attachments, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.attachments_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.attachments_v1.Attachments.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/Attachments-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Attachments";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.attachments_v1.impl.AttachmentsImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/Attachments-V1.0", "Attachments");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.attachments_v1.Attachments.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.g"
+"rammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boole"
+"an\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsr\u0000 com.s"
+"un.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramma"
+"r.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000\u001dpsr\u0000 com.sun.msv."
+"grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001x"
+"q\u0000~\u0000\u0004q\u0000~\u0000\u001dpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpre"
+"ssion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u001c\u0001q\u0000~\u0000\'sr\u0000 com.sun.msv.grammar.An"
+"yNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000(q\u0000~\u0000-sr\u0000#com.sun.msv.grammar.SimpleName"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespac"
+"eURIq\u0000~\u0000/xq\u0000~\u0000*t\u0000>gov.grants.apply.forms.attachments_v1.Atta"
+"chmentsType.ATT1Typet\u0000+http://java.sun.com/jaxb/xjc/dummy-el"
+"ementssq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000"
+"\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun."
+"msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatyp"
+"e.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.x"
+"sd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDat"
+"atypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000/L\u0000\btypeNameq\u0000~\u0000/L\u0000\nw"
+"hiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xp"
+"t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.d"
+"atatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.s"
+"un.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com."
+"sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~"
+"\u0000/L\u0000\fnamespaceURIq\u0000~\u0000/xpq\u0000~\u0000@q\u0000~\u0000?sq\u0000~\u0000.t\u0000\u0004typet\u0000)http://www"
+".w3.org/2001/XMLSchema-instanceq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT1t\u0000.http://a"
+"pply.grants.gov/forms/Attachments-V1.0q\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~"
+"\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000"
+"~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.grants.apply.forms.attachments_v1.Attac"
+"hmentsType.ATT2Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-s"
+"q\u0000~\u0000.t\u0000\u0004ATT2q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov."
+"grants.apply.forms.attachments_v1.AttachmentsType.ATT3Typeq\u0000"
+"~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT3q\u0000~\u0000Mq\u0000~\u0000"
+"-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dps"
+"q\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.grants.apply.forms.at"
+"tachments_v1.AttachmentsType.ATT4Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000"
+"\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT4q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+"
+"q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.grants.apply.forms.attachments_v1.Attachme"
+"ntsType.ATT5Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~"
+"\u0000.t\u0000\u0004ATT5q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000"
+"~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.gra"
+"nts.apply.forms.attachments_v1.AttachmentsType.ATT6Typeq\u0000~\u00002"
+"sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT6q\u0000~\u0000Mq\u0000~\u0000-sq"
+"\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~"
+"\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.grants.apply.forms.attac"
+"hments_v1.AttachmentsType.ATT7Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq"
+"\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT7q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~"
+"\u0000-sq\u0000~\u0000.t\u0000>gov.grants.apply.forms.attachments_v1.Attachments"
+"Type.ATT8Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t"
+"\u0000\u0004ATT8q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019"
+"ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000>gov.grants"
+".apply.forms.attachments_v1.AttachmentsType.ATT9Typeq\u0000~\u00002sq\u0000"
+"~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0004ATT9q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000"
+"\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q"
+"\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000?gov.grants.apply.forms.attachme"
+"nts_v1.AttachmentsType.ATT10Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~"
+"\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0005ATT10q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000"
+"~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000"
+"-sq\u0000~\u0000.t\u0000?gov.grants.apply.forms.attachments_v1.AttachmentsT"
+"ype.ATT11Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t"
+"\u0000\u0005ATT11q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000?gov.grant"
+"s.apply.forms.attachments_v1.AttachmentsType.ATT12Typeq\u0000~\u00002s"
+"q\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0005ATT12q\u0000~\u0000Mq\u0000~\u0000-sq"
+"\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~"
+"\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000?gov.grants.apply.forms.attac"
+"hments_v1.AttachmentsType.ATT13Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dp"
+"q\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0005ATT13q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q"
+"\u0000~\u0000-sq\u0000~\u0000.t\u0000?gov.grants.apply.forms.attachments_v1.Attachmen"
+"tsType.ATT14Typeq\u0000~\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~"
+"\u0000.t\u0000\u0005ATT14q\u0000~\u0000Mq\u0000~\u0000-sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001dp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0019ppsq\u0000~\u0000!q\u0000~\u0000\u001dpsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u0000\'q\u0000~\u0000+q\u0000~\u0000-sq\u0000~\u0000.t\u0000?gov.gr"
+"ants.apply.forms.attachments_v1.AttachmentsType.ATT15Typeq\u0000~"
+"\u00002sq\u0000~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u0005ATT15q\u0000~\u0000Mq\u0000~\u0000"
+"-sq\u0000~\u0000$ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~"
+"\u00006L\u0000\u0004nameq\u0000~\u00007L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004ppsr\u0000\'com.s"
+"un.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u0000"
+"9com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7"
+"Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom"
+"/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/"
+"sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000/xq\u0000~\u0000<t\u00001"
+"http://apply.grants.gov/system/GlobalLibrary-V1.0t\u0000\u0013FormVers"
+"ionDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000B\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinL"
+"engthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0001\tq\u0000~\u0001\u000eq\u0000~\u0001\u000fq\u0000~\u0001\u0011\u0000\u0000sr\u0000#"
+"com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysVal"
+"idxq\u0000~\u0000:q\u0000~\u0000?t\u0000\u0006stringq\u0000~\u0001\u0011\u0001q\u0000~\u0001\u0015t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0001\u0015t\u0000\tmax"
+"Length\u0000\u0000\u0000\u001esq\u0000~\u0000Fq\u0000~\u0001\u000fq\u0000~\u0001\u000et\u0000\u00031.0sq\u0000~\u0000.t\u0000\u000bFormVersionq\u0000~\u0000Msq\u0000"
+"~\u0000\u0019ppsq\u0000~\u0000$q\u0000~\u0000\u001dpq\u0000~\u00008q\u0000~\u0000Hq\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000bAttachmentsq\u0000~\u0000Msr\u0000"
+"\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/"
+"Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun"
+".msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\r"
+"streamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool"
+";xp\u0000\u0000\u0000\\\u0001pq\u0000~\u0000\nq\u0000~\u0000\u000fq\u0000~\u0000\u0014q\u0000~\u0000\u0011q\u0000~\u0000\u00a9q\u0000~\u0000\u009cq\u0000~\u0000\u008fq\u0000~\u0000\u0082q\u0000~\u0000uq\u0000~\u0000hq"
+"\u0000~\u0000[q\u0000~\u0000Nq\u0000~\u0000\u001aq\u0000~\u0000\u00b6q\u0000~\u0000\u00c3q\u0000~\u0000\u00d0q\u0000~\u0000\u00ddq\u0000~\u0000\u00eaq\u0000~\u0000\u00f7q\u0000~\u0000\u000bq\u0000~\u0000\u0010q\u0000~\u0000\u00bfq"
+"\u0000~\u0000\u00b2q\u0000~\u0000\u00a5q\u0000~\u0000\u0098q\u0000~\u0000\u008bq\u0000~\u0000~q\u0000~\u0000qq\u0000~\u0000dq\u0000~\u0000Wq\u0000~\u00003q\u0000~\u0000\u00ccq\u0000~\u0000\u00d9q\u0000~\u0000\u00e6q"
+"\u0000~\u0000\u00f3q\u0000~\u0001\u0000q\u0000~\u0000\u0018q\u0000~\u0001\u001dq\u0000~\u0000\u00b8q\u0000~\u0000\u00abq\u0000~\u0000\u009eq\u0000~\u0000\u0091q\u0000~\u0000\u0084q\u0000~\u0000wq\u0000~\u0000jq\u0000~\u0000]q"
+"\u0000~\u0000Pq\u0000~\u0000\u001eq\u0000~\u0000\u00c5q\u0000~\u0000\u00d2q\u0000~\u0000\u00dfq\u0000~\u0000\u00ecq\u0000~\u0000\u00f9q\u0000~\u0000\tq\u0000~\u0000\fq\u0000~\u0000\u000eq\u0000~\u0000\u0013q\u0000~\u0000\u00baq"
+"\u0000~\u0000\u00adq\u0000~\u0000\u00a0q\u0000~\u0000\u0093q\u0000~\u0000\u0086q\u0000~\u0000yq\u0000~\u0000lq\u0000~\u0000_q\u0000~\u0000Rq\u0000~\u0000 q\u0000~\u0000\u00c7q\u0000~\u0000\u00d4q\u0000~\u0000\u00e1q"
+"\u0000~\u0000\u00eeq\u0000~\u0000\u00fbq\u0000~\u0000\u0015q\u0000~\u0000\u0016q\u0000~\u0000\u0012q\u0000~\u0000\rq\u0000~\u0000\u00bbq\u0000~\u0000\u00aeq\u0000~\u0000\u00a1q\u0000~\u0000\u0094q\u0000~\u0000\u0087q\u0000~\u0000zq"
+"\u0000~\u0000mq\u0000~\u0000`q\u0000~\u0000Sq\u0000~\u0000#q\u0000~\u0000\u00c8q\u0000~\u0000\u00d5q\u0000~\u0000\u00e2q\u0000~\u0000\u0017q\u0000~\u0000\u00efq\u0000~\u0000\u00fcx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.attachments_v1.impl.AttachmentsImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/Attachments-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Attachments" == ___local)&&("http://apply.grants.gov/forms/Attachments-V1.0" == ___uri)) {
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
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/Attachments-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("Attachments" == ___local)&&("http://apply.grants.gov/forms/Attachments-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
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
                    case  1 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/Attachments-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.attachments_v1.impl.AttachmentsTypeImpl)gov.grants.apply.forms.attachments_v1.impl.AttachmentsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/Attachments-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                        case  1 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/Attachments-V1.0", "FormVersion");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
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
