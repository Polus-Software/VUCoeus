//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1.impl;

public class SubmittingOrganizationImpl
    extends gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl
    implements gov.grants.apply.forms.sf424_v1.SubmittingOrganization, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424_v1.SubmittingOrganization.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "SubmittingOrganization";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424-V1.0", "SubmittingOrganization");
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
        return (gov.grants.apply.forms.sf424_v1.SubmittingOrganization.class);
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
+"q\u0000~\u0000\bppsq\u0000~\u0000\u000esr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003x"
+"q\u0000~\u0000\u0004q\u0000~\u0000\u0012psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0012psr\u00002com.sun.msv.gramma"
+"r.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0011\u0001q\u0000~\u0000"
+"\u001bsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun."
+"msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001cq\u0000~\u0000!sr\u0000#com"
+".sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Lj"
+"ava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000#xq\u0000~\u0000\u001et\u0000Bgov.grants.appl"
+"y.forms.sf424_v1.OrganizationIdentifyingInformationt\u0000+http:/"
+"/java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\""
+"t\u0000Fgov.grants.apply.forms.sf424_v1.OrganizationIdentifyingIn"
+"formationTypeq\u0000~\u0000&sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.gramma"
+"r.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L"
+"\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004pp"
+"sr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun"
+".msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.ms"
+"v.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.dataty"
+"pe.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000#L\u0000\btypeN"
+"ameq\u0000~\u0000#L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpace"
+"Processor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005c"
+"om.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\t"
+"localNameq\u0000~\u0000#L\u0000\fnamespaceURIq\u0000~\u0000#xpq\u0000~\u0000<q\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u0004typet"
+"\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\"Org"
+"anizationIdentifyingInformationt\u0000(http://apply.grants.gov/fo"
+"rms/SF424-V1.0q\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000epp"
+"sq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\'gov.grants.a"
+"pply.forms.sf424_v1.Addressq\u0000~\u0000&sq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000p"
+"p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000+go"
+"v.grants.apply.forms.sf424_v1.AddressTypeq\u0000~\u0000&sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018"
+"q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0007Addressq\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000"
+"\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsr\u0000)com.sun.msv.datatype.xsd.Enumera"
+"tionFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.m"
+"sv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000x"
+"r\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fi"
+"sFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/d"
+"atatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/da"
+"tatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000#xq\u0000~\u00008t\u0000*http://app"
+"ly.grants.gov/system/Global-V1.0t\u0000\tYesNoTypesr\u00005com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000>\u0000\u0000"
+"sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlway"
+"sValidxq\u0000~\u00006q\u0000~\u0000;t\u0000\u0006stringq\u0000~\u0000l\u0001q\u0000~\u0000nt\u0000\u000benumerationsr\u0000\u0011java."
+"util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0001Yt\u0000\u0001Nxq\u0000~\u0000Asq\u0000~\u0000Bq"
+"\u0000~\u0000jq\u0000~\u0000isq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u001eDelinqu"
+"entFederalDebtIndicatorq\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u00001ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~\u0000dq\u0000~\u0000it\u0000\u0015StringMin1Max4096Typeq\u0000~\u0000l\u0000\u0001sr"
+"\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLe"
+"ngthxq\u0000~\u0000dq\u0000~\u0000iq\u0000~\u0000\u0080q\u0000~\u0000l\u0000\u0000q\u0000~\u0000nq\u0000~\u0000nt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000nt\u0000"
+"\tmaxLength\u0000\u0000\u0010\u0000q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000\u0080q\u0000~\u0000isq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004"
+"q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000 DelinquentFederalDebtExplanationq\u0000~\u0000Iq\u0000~\u0000"
+"!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsq\u0000~\u0000~q\u0000~\u0000it\u0000\u0013StringMi"
+"n1Max30Typeq\u0000~\u0000l\u0000\u0001sq\u0000~\u0000\u0081q\u0000~\u0000iq\u0000~\u0000\u008fq\u0000~\u0000l\u0000\u0000q\u0000~\u0000nq\u0000~\u0000nq\u0000~\u0000\u0083\u0000\u0000\u0000\u0001"
+"q\u0000~\u0000nq\u0000~\u0000\u0084\u0000\u0000\u0000\u001eq\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000\u008fq\u0000~\u0000isq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004"
+"q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0015CongressionalDistrictq\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq"
+"\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0016SubmittingOrganizationq\u0000~\u0000"
+"Isr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTabl"
+"et\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com"
+".sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005coun"
+"tB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expression"
+"Pool;xp\u0000\u0000\u0000\u001f\u0001pq\u0000~\u0000zq\u0000~\u0000\u008aq\u0000~\u0000(q\u0000~\u0000Sq\u0000~\u0000`q\u0000~\u0000\rq\u0000~\u0000\fq\u0000~\u0000\u000bq\u0000~\u0000\tq\u0000"
+"~\u0000\u0014q\u0000~\u0000*q\u0000~\u0000Mq\u0000~\u0000Uq\u0000~\u0000/q\u0000~\u0000Zq\u0000~\u0000vq\u0000~\u0000^q\u0000~\u0000\u0086q\u0000~\u0000\u000fq\u0000~\u0000Jq\u0000~\u0000\u0092q\u0000"
+"~\u0000\u008cq\u0000~\u0000\u0096q\u0000~\u0000\nq\u0000~\u0000\u0010q\u0000~\u0000Kq\u0000~\u0000\u0017q\u0000~\u0000+q\u0000~\u0000Nq\u0000~\u0000Vq\u0000~\u0000|x"));
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
            return gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("OrganizationIdentifyingInformation" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("OrganizationIdentifyingInformation" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DelinquentFederalDebtIndicator" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DelinquentFederalDebtExplanation" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("CongressionalDistrict" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("SubmittingOrganization" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
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
                    case  1 :
                        spawnHandlerFromLeaveElement((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("SubmittingOrganization" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
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
                        spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
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
                        spawnHandlerFromLeaveAttribute((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
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
                            spawnHandlerFromText((((gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.SubmittingOrganizationImpl.this).new Unmarshaller(context)), 2, value);
                            return ;
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