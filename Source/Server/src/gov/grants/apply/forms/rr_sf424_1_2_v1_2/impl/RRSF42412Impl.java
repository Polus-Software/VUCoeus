//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl;

public class RRSF42412Impl
    extends gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl
    implements gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412 .class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/RR_SF424_1_2-V1-2";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RR_SF424_1_2";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412Impl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "RR_SF424_1_2");
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
        return (gov.grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412 .class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000"
+"~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000)com.su"
+"n.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLj"
+"ava/util/Set;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueC"
+"onstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTy"
+"peWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000"
+"\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconc"
+"reteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNa"
+"met\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDataty"
+"peImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u00000L\u0000\btypeNameq\u0000~\u00000L\u0000\nwhit"
+"eSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000/"
+"http://apply.grants.gov/forms/RR_SF424_1_2-V1-2t\u0000\u0016Submission"
+"TypeDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.Bu"
+"iltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concr"
+"eteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00001t\u0000 http://www.w3.org/2001/XMLSchemat"
+"\u0000\u0006stringq\u0000~\u00008\u0001q\u0000~\u0000<t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8"
+"\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0003t\u0000\u001cChange/Corrected Applicationt\u0000\u000bAppli"
+"cationt\u0000\u000ePreapplicationxsr\u00000com.sun.msv.grammar.Expression$N"
+"ullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.Stri"
+"ngPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u00000L\u0000\fnamespaceURIq\u0000~\u00000xpq\u0000~\u0000"
+"5q\u0000~\u00004sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr"
+"\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tn"
+"ameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuex"
+"p\u0000psq\u0000~\u0000&ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000:q\u0000~\u0000=t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00007q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000Rq\u0000~\u0000=sr\u0000#co"
+"m.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000"
+"0L\u0000\fnamespaceURIq\u0000~\u00000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr"
+"\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000M\u0001q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0012SubmissionTypeCodeq\u0000~\u00004sq\u0000~\u0000Ipps"
+"q\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsr\u0000!com.sun.msv.datatype.xsd.Dat"
+"eType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseTyp"
+"e\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000:q\u0000~\u0000=t\u0000\u0004dateq\u0000~\u0000Tq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000gq\u0000~\u0000=sq\u0000~"
+"\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\rSubmittedDateq\u0000~\u00004q\u0000"
+"~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsr\u0000\'com.sun.msv.data"
+"type.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~\u0000,t\u00001http:"
+"//apply.grants.gov/system/GlobalLibrary-V2.0t\u0000\u0013ApplicantIDDa"
+"taTypeq\u0000~\u00008\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000,q\u0000~\u0000sq\u0000~\u0000tq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<t\u0000\tminLe"
+"ngth\u0000\u0000\u0000\u0001q\u0000~\u0000<t\u0000\tmaxLength\u0000\u0000\u0000\u001eq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000tq\u0000~\u0000ssq\u0000~\u0000Ippsq"
+"\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bApplicantIDq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000"
+"Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000csq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq"
+"\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0011StateReceivedDateq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u000fStateIDDataTypeq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq"
+"\u0000~\u0000sq\u0000~\u0000\u008aq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000\u001eq\u0000~\u0000Fsq\u0000~\u0000G"
+"q\u0000~\u0000\u008aq\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0007StateI"
+"Dq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st"
+"\u0000\u0011FederalIDDataTypeq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0000\u0096q\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q"
+"\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000\u001eq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u0096q\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~"
+"\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\tFederalIDq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000"
+"q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u00004pq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u00004pq\u0000~\u00008\u0000"
+"\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000Kq\u0000~\u0000Fsq\u0000~\u0000Gt\u0000\u000estring-deriv"
+"edq\u0000~\u00004sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0013AgencyRou"
+"tingNumberq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsr\u0000 co"
+"m.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gra"
+"mmar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Nps"
+"r\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000]q\u0000~\u0000\u00b2sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Wq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Hgov.grants.apply.forms.rr_sf424_1"
+"_2_v1_2.RRSF42412Type.ApplicantInfoTypet\u0000+http://java.sun.co"
+"m/jaxb/xjc/dummy-elementssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\"
+"sq\u0000~\u0000Vt\u0000\rApplicantInfoq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000"
+"~\u00004pq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u00004pq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\tq\u0000~\u0000<q\u0000~\u0000x\u0000"
+"\u0000\u0000\u001eq\u0000~\u0000Fsq\u0000~\u0000Gt\u0000\u000estring-derivedq\u0000~\u00004sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000"
+"Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nEmployerIDq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Hgov"
+".grants.apply.forms.rr_sf424_1_2_v1_2.RRSF42412Type.Applican"
+"tTypeTypeq\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\rAp"
+"plicantTypeq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000"
+"Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Jgov.grants.apply.forms"
+".rr_sf424_1_2_v1_2.RRSF42412Type.ApplicationTypeTypeq\u0000~\u0000\u00b7sq\u0000"
+"~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000fApplicationTypeq\u0000~\u0000"
+"4sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u0012AgencyNameDataTypeq\u0000"
+"~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0000\u00e3q\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000"
+"<q\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00e3q\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000"
+"~\u0000Vt\u0000\u0011FederalAgencyNameq\u0000~\u00004sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u0012CFDANumberDataTypeq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0000\u00ef"
+"q\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000\u000fq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00efq\u0000~\u0000"
+"ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nCFDANumberq\u0000~\u00004"
+"q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u001cProg"
+"ramActivityTitleDataTypeq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0000\u00fbq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q"
+"\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000xq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0000\u00fbq\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~"
+"\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\rActivityTitleq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u0014ProjectTitleDataTypeq\u0000~\u00008\u0000"
+"\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0001\u0006q\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000\u00c8q\u0000~"
+"\u0000Fsq\u0000~\u0000Gq\u0000~\u0001\u0006q\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt"
+"\u0000\fProjectTitleq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq"
+"\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Pgov.grants.apply.fo"
+"rms.rr_sf424_1_2_v1_2.RRSF42412Type.ProposedProjectPeriodTyp"
+"eq\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0015ProposedPr"
+"ojectPeriodq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000"
+"Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Pgov.grants.apply.forms"
+".rr_sf424_1_2_v1_2.RRSF42412Type.CongressionalDistrictTypeq\u0000"
+"~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0015Congressional"
+"Districtq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Nps"
+"q\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Jgov.grants.apply.forms.rr"
+"_sf424_1_2_v1_2.OrganizationContactPersonDataTypeq\u0000~\u0000\u00b7sq\u0000~\u0000I"
+"ppsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000fPDPIContactInfoq\u0000~\u00004sq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000"
+"\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Rgov.grants.apply.forms.rr_sf424_1_2_v1_2"
+".RRSF42412Type.EstimatedProjectFundingTypeq\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000"
+"Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0017EstimatedProjectFundingq\u0000~\u00004s"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~"
+"\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Fgov.grants.apply.forms.rr_sf424_1_2_v1_"
+"2.RRSF42412Type.StateReviewTypeq\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000"
+"Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bStateReviewq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&p"
+"psq\u0000~\u0000*q\u0000~\u0000st\u0000\rYesNoDataTypeq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000?sq\u0000~\u0000@w\f\u0000\u0000"
+"\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0005N: Not\u0000\u0006Y: Yesxq\u0000~\u0000Fsq\u0000~\u0000Gq\u0000~\u0001Mq\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000"
+"~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nTrustAgreeq\u0000~\u00004sq\u0000~\u0000Ippsq\u0000~"
+"\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000"
+"~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000;gov.grants.apply.system.attachments_v1"
+".AttachedFileDataTypeq\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000"
+"\\sq\u0000~\u0000Vt\u0000\u000fSFLLLAttachmentq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vt\u00004gov"
+".grants.apply.forms.rr_sf424_1_2_v1_2.AORInfoTypeq\u0000~\u0000\u00b7sq\u0000~\u0000I"
+"ppsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0007AORInfoq\u0000~\u00004sq\u0000~\u0000Ippsq"
+"\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000\u00adq\u0000~\u0000Npsq\u0000~\u0000Kq\u0000~\u0000Np"
+"q\u0000~\u0000\u00b2q\u0000~\u0000\u00b4q\u0000~\u0000\\sq\u0000~\u0000Vq\u0000~\u0001^q\u0000~\u0000\u00b7sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000"
+"Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0018PreApplicationAttachmentq\u0000~\u00004q\u0000~\u0000\\sq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u0011SignatureDataTypeq\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000"
+"~\u0000sq\u0000~\u0001\u007fq\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000\u0000\u0090q\u0000~\u0000Fsq\u0000~\u0000Gq"
+"\u0000~\u0001\u007fq\u0000~\u0000ssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\rAOR_Sig"
+"natureq\u0000~\u00004sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000csq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000"
+"~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000eAOR_SignedDateq\u0000~\u00004sq\u0000~\u0000Kppsr\u0000\u001ccom.sun.msv."
+"grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\'L\u0000\u0004nameq\u0000~\u0000(L\u0000\u0005valuet\u0000\u0012"
+"Ljava/lang/Object;xq\u0000~\u0000\u0004ppsq\u0000~\u0000qq\u0000~\u0000st\u0000\u0013FormVersionDataTypeq"
+"\u0000~\u00008\u0000\u0001sq\u0000~\u0000uq\u0000~\u0000sq\u0000~\u0001\u0091q\u0000~\u00008\u0000\u0000q\u0000~\u0000<q\u0000~\u0000<q\u0000~\u0000w\u0000\u0000\u0000\u0001q\u0000~\u0000<q\u0000~\u0000x\u0000\u0000"
+"\u0000\u001esq\u0000~\u0000Gq\u0000~\u0001\u0091q\u0000~\u0000st\u0000\u00031.2sq\u0000~\u0000Vt\u0000\u000bFormVersionq\u0000~\u00004sq\u0000~\u0000Ippsq\u0000"
+"~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fRR_SF424_1_2q\u0000~\u00004sr\u0000\"com.su"
+"n.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/su"
+"n/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gr"
+"ammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamV"
+"ersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000p"
+"\u0001pq\u0000~\u0000\u000fq\u0000~\u0001Jq\u0000~\u0000\u00f6q\u0000~\u0000\u0018q\u0000~\u0000\u0080q\u0000~\u0000bq\u0000~\u0001\u0087q\u0000~\u0000\nq\u0000~\u0000\tq\u0000~\u0000\fq\u0000~\u0001Vq\u0000~"
+"\u0001oq\u0000~\u0000\u0012q\u0000~\u0001\tq\u0000~\u0000\u00feq\u0000~\u0000\u00f2q\u0000~\u0000\u00e6q\u0000~\u0000\u00dbq\u0000~\u0000\u00cfq\u0000~\u0000\u00c3q\u0000~\u0000\u00b8q\u0000~\u0000\u00a5q\u0000~\u0000\u0099q\u0000~"
+"\u0000\u00e0q\u0000~\u0000\u008dq\u0000~\u0000\u0081q\u0000~\u0000zq\u0000~\u0000iq\u0000~\u0000Jq\u0000~\u0001\u0015q\u0000~\u0001!q\u0000~\u0000\u0014q\u0000~\u0001-q\u0000~\u00019q\u0000~\u0001Eq\u0000~"
+"\u0000\u00d4q\u0000~\u0000\u00c8q\u0000~\u0000\u00aaq\u0000~\u0000 q\u0000~\u0001\u000eq\u0000~\u0001\u001aq\u0000~\u0001&q\u0000~\u00012q\u0000~\u0001>q\u0000~\u0001Rq\u0000~\u0001\u0003q\u0000~\u0001_q\u0000~"
+"\u0001Xq\u0000~\u0001kq\u0000~\u0001dq\u0000~\u0001wq\u0000~\u0000\u00eaq\u0000~\u0001qq\u0000~\u0000\rq\u0000~\u0001\u0082q\u0000~\u0000\u009dq\u0000~\u0001\u0088q\u0000~\u0001\u0097q\u0000~\u0000#q\u0000~"
+"\u0000\u0085q\u0000~\u0000\u0013q\u0000~\u0000\u00bdq\u0000~\u0000\"q\u0000~\u0000\u000eq\u0000~\u0000\u00d6q\u0000~\u0000\u00caq\u0000~\u0000\u00acq\u0000~\u0001\u0010q\u0000~\u0001\u001cq\u0000~\u0001(q\u0000~\u00014q\u0000~"
+"\u0001@q\u0000~\u0001Zq\u0000~\u0001fq\u0000~\u0001sq\u0000~\u0001|q\u0000~\u0000\u001aq\u0000~\u0000\u00ecq\u0000~\u0000\u001cq\u0000~\u0000\u0015q\u0000~\u0000\u001fq\u0000~\u0000!q\u0000~\u0000~q\u0000~"
+"\u0000`q\u0000~\u0000\u00f8q\u0000~\u0000\u001eq\u0000~\u0000\u00d7q\u0000~\u0000\u00cbq\u0000~\u0000\u00afq\u0000~\u0000\u009fq\u0000~\u0001\u0011q\u0000~\u0001\u001dq\u0000~\u0000\u001bq\u0000~\u0001)q\u0000~\u00015q\u0000~"
+"\u0001Aq\u0000~\u0000\u0087q\u0000~\u0000\u001dq\u0000~\u0000\u0011q\u0000~\u0000\u0010q\u0000~\u0000\u0016q\u0000~\u0001[q\u0000~\u0001gq\u0000~\u0001tq\u0000~\u0000%q\u0000~\u0000\u000bq\u0000~\u0000\u0091q\u0000~"
+"\u0000\u0017q\u0000~\u0000\u0019q\u0000~\u0000mq\u0000~\u0000\u0093q\u0000~\u0000ox"));
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
            return gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412Impl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("RR_SF424_1_2" == ___local)&&("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2" == ___uri)) {
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("RR_SF424_1_2" == ___local)&&("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2" == ___uri)) {
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
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412TypeImpl)gov.grants.apply.forms.rr_sf424_1_2_v1_2.impl.RRSF42412Impl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                        case  1 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SF424_1_2-V1-2", "FormVersion");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
