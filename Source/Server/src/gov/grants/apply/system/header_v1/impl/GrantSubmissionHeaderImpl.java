//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.header_v1.impl;

public class GrantSubmissionHeaderImpl
    extends gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderTypeImpl
    implements gov.grants.apply.system.header_v1.GrantSubmissionHeader, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.system.header_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.system.header_v1.GrantSubmissionHeader.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/system/Header-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "GrantSubmissionHeader";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/system/Header-V1.0", "GrantSubmissionHeader");
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
        return (gov.grants.apply.system.header_v1.GrantSubmissionHeader.class);
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
+"\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u0014ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004s"
+"r\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.g"
+"rammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq"
+"\u0000~\u0000\u0004q\u0000~\u0000\u001cpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpres"
+"sion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u001b\u0001q\u0000~\u0000 sr\u0000 com.sun.msv.grammar.Any"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u0000&sr\u0000#com.sun.msv.grammar.SimpleNameC"
+"lass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespace"
+"URIq\u0000~\u0000(xq\u0000~\u0000#t\u0000+gov.grants.apply.system.global_v1.HashValue"
+"t\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0018q\u0000~\u0000\u001cpsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u0000 q\u0000~\u0000$q\u0000~\u0000&s"
+"q\u0000~\u0000\'t\u0000/gov.grants.apply.system.global_v1.HashValueTypeq\u0000~\u0000+"
+"sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004na"
+"met\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.da"
+"tatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd."
+"BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Con"
+"creteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeI"
+"mpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000(L\u0000\btypeNameq\u0000~\u0000(L\u0000\nwhiteSp"
+"acet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 htt"
+"p://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatyp"
+"e.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv"
+".datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000"
+"\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000(L\u0000\fn"
+"amespaceURIq\u0000~\u0000(xpq\u0000~\u0000Aq\u0000~\u0000@sq\u0000~\u0000\'t\u0000\u0004typet\u0000)http://www.w3.or"
+"g/2001/XMLSchema-instanceq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\tHashValuet\u0000*http://ap"
+"ply.grants.gov/system/Global-V1.0sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u00006ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVal"
+"ueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Dat"
+"aTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFla"
+"gL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fc"
+"oncreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tface"
+"tNameq\u0000~\u0000(xq\u0000~\u0000=q\u0000~\u0000Nt\u0000\u0013StringMin1Max60Typesr\u00005com.sun.msv.d"
+"atatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000C\u0000\u0001s"
+"r\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminL"
+"engthxq\u0000~\u0000Tq\u0000~\u0000Nq\u0000~\u0000Yq\u0000~\u0000[\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.Str"
+"ingType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000;q\u0000~\u0000@t\u0000\u0006stringq\u0000~\u0000[\u0001"
+"q\u0000~\u0000_t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000_t\u0000\tmaxLength\u0000\u0000\u0000<q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000Yq\u0000"
+"~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\nAgencyNamet\u0000*"
+"http://apply.grants.gov/system/Header-V1.0q\u0000~\u0000&sq\u0000~\u0000\u0014ppsq\u0000~\u0000"
+"\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006ppsq\u0000~\u0000Sq\u0000~\u0000Nt\u0000\u0013StringMin1Max15Typeq\u0000~"
+"\u0000[\u0000\u0001sq\u0000~\u0000\\q\u0000~\u0000Nq\u0000~\u0000nq\u0000~\u0000[\u0000\u0000q\u0000~\u0000_q\u0000~\u0000_q\u0000~\u0000a\u0000\u0000\u0000\u0001q\u0000~\u0000_q\u0000~\u0000b\u0000\u0000\u0000\u000f"
+"q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000nq\u0000~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~"
+"\u0000\'t\u0000\nCFDANumberq\u0000~\u0000hq\u0000~\u0000&sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006"
+"ppsq\u0000~\u0000Sq\u0000~\u0000Nt\u0000\u0014StringMin1Max120Typeq\u0000~\u0000[\u0000\u0001sq\u0000~\u0000\\q\u0000~\u0000Nq\u0000~\u0000zq"
+"\u0000~\u0000[\u0000\u0000q\u0000~\u0000_q\u0000~\u0000_q\u0000~\u0000a\u0000\u0000\u0000\u0001q\u0000~\u0000_q\u0000~\u0000b\u0000\u0000\u0000xq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000zq\u0000~\u0000N"
+"sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\rActivityTitleq\u0000~"
+"\u0000hq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006ppsq\u0000~\u0000Sq\u0000~\u0000Nt\u0000\u0014StringMin1Max1"
+"00Typeq\u0000~\u0000[\u0000\u0001sq\u0000~\u0000\\q\u0000~\u0000Nq\u0000~\u0000\u0085q\u0000~\u0000[\u0000\u0000q\u0000~\u0000_q\u0000~\u0000_q\u0000~\u0000a\u0000\u0000\u0000\u0001q\u0000~\u0000_"
+"q\u0000~\u0000b\u0000\u0000\u0000dq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u0085q\u0000~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000I"
+"q\u0000~\u0000&sq\u0000~\u0000\'t\u0000\rOpportunityIDq\u0000~\u0000hsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007p"
+"psq\u0000~\u00006ppsq\u0000~\u0000Sq\u0000~\u0000Nt\u0000\u0014StringMin1Max255Typeq\u0000~\u0000[\u0000\u0001sq\u0000~\u0000\\q\u0000~\u0000"
+"Nq\u0000~\u0000\u0091q\u0000~\u0000[\u0000\u0000q\u0000~\u0000_q\u0000~\u0000_q\u0000~\u0000a\u0000\u0000\u0000\u0001q\u0000~\u0000_q\u0000~\u0000b\u0000\u0000\u0000\u00ffq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~"
+"\u0000\u0091q\u0000~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\u0010Opportuni"
+"tyTitleq\u0000~\u0000hq\u0000~\u0000&sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0083sq\u0000~\u0000\u0014pps"
+"q\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\rCompetitionIDq\u0000~\u0000hq\u0000~\u0000&sq"
+"\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006ppsr\u0000!com.sun.msv.datatype."
+"xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTime"
+"BaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000;q\u0000~\u0000@t\u0000\u0004dateq\u0000~\u0000Dq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00a6q\u0000"
+"~\u0000@sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\u000bOpeningDateq\u0000"
+"~\u0000hq\u0000~\u0000&sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u00a2sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000"
+"\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\u000bClosingDateq\u0000~\u0000hq\u0000~\u0000&sq\u0000~\u0000\u0014ppsq\u0000~\u0000"
+"\u0000q\u0000~\u0000\u001cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00006ppsq\u0000~\u0000Sq\u0000~\u0000Nt\u0000\u0014StringMin1Max240Typeq\u0000"
+"~\u0000[\u0000\u0001sq\u0000~\u0000\\q\u0000~\u0000Nq\u0000~\u0000\u00b8q\u0000~\u0000[\u0000\u0000q\u0000~\u0000_q\u0000~\u0000_q\u0000~\u0000a\u0000\u0000\u0000\u0001q\u0000~\u0000_q\u0000~\u0000b\u0000\u0000\u0000"
+"\u00f0q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00b8q\u0000~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000"
+"~\u0000\'t\u0000\u000fSubmissionTitleq\u0000~\u0000hq\u0000~\u0000&sq\u0000~\u0000\u001dppsr\u0000\u001ccom.sun.msv.gramm"
+"ar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u00007L\u0000\u0004nameq\u0000~\u00008L\u0000\u0005valuet\u0000\u0012Ljava"
+"/lang/Object;xq\u0000~\u0000\u0004ppq\u0000~\u0000_sq\u0000~\u0000Gq\u0000~\u0000`q\u0000~\u0000@t\u0000\u00031.0sq\u0000~\u0000\'t\u0000\rsch"
+"emaVersionq\u0000~\u0000Nsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001dq\u0000~\u0000\u001cpq\u0000~\u00009q\u0000~\u0000Iq\u0000~\u0000&sq\u0000~\u0000\'t\u0000\u0015G"
+"rantSubmissionHeaderq\u0000~\u0000hsr\u0000\"com.sun.msv.grammar.ExpressionP"
+"ool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionP"
+"ool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Clos"
+"edHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/su"
+"n/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000-\u0001pq\u0000~\u0000\u0082q\u0000~\u0000\u009aq\u0000~\u0000-q\u0000~\u0000\rq\u0000~"
+"\u0000\u00b5q\u0000~\u0000\u0098q\u0000~\u0000\nq\u0000~\u0000\u000bq\u0000~\u0000\u00b3q\u0000~\u0000\u0013q\u0000~\u0000\u009fq\u0000~\u0000\u00acq\u0000~\u0000\u008cq\u0000~\u0000\u0012q\u0000~\u0000\fq\u0000~\u0000Qq\u0000~"
+"\u0000\u008eq\u0000~\u0000\u0017q\u0000~\u0000/q\u0000~\u00004q\u0000~\u0000dq\u0000~\u0000qq\u0000~\u0000}q\u0000~\u0000uq\u0000~\u0000\u0088q\u0000~\u0000\u0094q\u0000~\u0000\u009bq\u0000~\u0000\u00a8q\u0000~"
+"\u0000\u00afq\u0000~\u0000\u00bbq\u0000~\u0000\u00c7q\u0000~\u0000\tq\u0000~\u0000\u000eq\u0000~\u0000\u0010q\u0000~\u0000\u00a1q\u0000~\u0000\u00aeq\u0000~\u0000wq\u0000~\u0000\u0015q\u0000~\u0000Oq\u0000~\u0000\u001aq\u0000~"
+"\u00000q\u0000~\u0000kq\u0000~\u0000\u0011q\u0000~\u0000iq\u0000~\u0000\u000fx"));
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
            return gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "schemaVersion");
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
                        if (("GrantSubmissionHeader" == ___local)&&("http://apply.grants.gov/system/Header-V1.0" == ___uri)) {
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
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "schemaVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("GrantSubmissionHeader" == ___local)&&("http://apply.grants.gov/system/Header-V1.0" == ___uri)) {
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
                    case  1 :
                        if (("schemaVersion" == ___local)&&("http://apply.grants.gov/system/Global-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderTypeImpl)gov.grants.apply.system.header_v1.impl.GrantSubmissionHeaderImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "schemaVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "schemaVersion");
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
