//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl;

public class CertificationDebarmentImpl
    extends gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.CertificationDebarmentTypeImpl
    implements gov.grants.apply.forms.ed_certificationdebarment_v1_1.CertificationDebarment, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.ed_certificationdebarment_v1_1.CertificationDebarment.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "CertificationDebarment";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.CertificationDebarmentImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1", "CertificationDebarment");
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
        return (gov.grants.apply.forms.ed_certificationdebarment_v1_1.CertificationDebarment.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.Choic"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000"
+"\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004"
+"namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv."
+"datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.su"
+"n.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002"
+"\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z"
+"\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/ms"
+"v/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv"
+"/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;"
+"xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnam"
+"espaceUriq\u0000~\u0000\u001fL\u0000\btypeNameq\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/d"
+"atatype/xsd/WhiteSpaceProcessor;xpt\u00001http://apply.grants.gov"
+"/system/GlobalLibrary-V2.0t\u0000\u0018OrganizationNameDataTypesr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000"
+"\tminLengthxq\u0000~\u0000\u001bq\u0000~\u0000#q\u0000~\u0000$q\u0000~\u0000\'\u0000\u0000sr\u0000#com.sun.msv.datatype.xs"
+"d.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datat"
+"ype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype"
+".xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 t\u0000 http://www.w3.org/2001/"
+"XMLSchemat\u0000\u0006stringq\u0000~\u0000\'\u0001q\u0000~\u0000-t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000-t\u0000\tmaxLeng"
+"th\u0000\u0000\u0000<sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L"
+"\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq\u0000~\u0000\u001fxpq\u0000~\u0000$q\u0000~\u0000#sq\u0000~\u0000\u0010ppsr\u0000 "
+"com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnam"
+"eClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0014psq\u0000~\u0000\u0016ppsr\u0000\"com.sun.msv.datatype.xsd."
+"QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000+q\u0000~\u0000.t\u0000\u0005QNamesr\u00005com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000&q\u0000~\u00003sq"
+"\u0000~\u00004q\u0000~\u0000<q\u0000~\u0000.sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq\u0000~\u0000\u001fxr\u0000\u001dcom.sun.msv.gra"
+"mmar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/"
+"XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0013\u0001q\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u0010Organization"
+"Namet\u0000<http://apply.grants.gov/forms/ED_CertificationDebarme"
+"nt-V1-1q\u0000~\u0000Fsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0010ppsr\u0000 com.sun.ms"
+"v.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.Una"
+"ryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000\u0014psq\u0000~\u00007q\u0000~\u0000\u0014psr\u00002com.s"
+"un.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0004q\u0000~\u0000Gq\u0000~\u0000Tsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000Aq\u0000~\u0000Fsq\u0000~\u0000@t\u0000:gov.grants.apply.system.globallibrary_v2."
+"HumanNameDataTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-eleme"
+"ntssq\u0000~\u0000\u0010ppsq\u0000~\u00007q\u0000~\u0000\u0014pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u001cAuthorizedRep"
+"resentativeNameq\u0000~\u0000Jsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u001aq\u0000~\u0000#t\u0000\u0012H"
+"umanTitleDataTypeq\u0000~\u0000\'\u0000\u0001sq\u0000~\u0000(q\u0000~\u0000#q\u0000~\u0000bq\u0000~\u0000\'\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q\u0000~"
+"\u00000\u0000\u0000\u0000\u0001q\u0000~\u0000-q\u0000~\u00001\u0000\u0000\u0000-q\u0000~\u00003sq\u0000~\u00004q\u0000~\u0000bq\u0000~\u0000#sq\u0000~\u0000\u0010ppsq\u0000~\u00007q\u0000~\u0000\u0014"
+"pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u001dAuthorizedRepresentativeTitleq\u0000~\u0000Js"
+"q\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0010ppsq\u0000~\u0000Oq\u0000~\u0000\u0014psq\u0000"
+"~\u00007q\u0000~\u0000\u0014pq\u0000~\u0000Tq\u0000~\u0000Vq\u0000~\u0000Fsq\u0000~\u0000@t\u0000;gov.grants.apply.system.att"
+"achments_v1.AttachedFileDataTypeq\u0000~\u0000Ysq\u0000~\u0000\u0010ppsq\u0000~\u00007q\u0000~\u0000\u0014pq\u0000~"
+"\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\nAttachmentq\u0000~\u0000Jq\u0000~\u0000Fsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0016ppsq\u0000~\u0000\u001aq\u0000~\u0000#t\u0000\u0011SignatureDataTypeq\u0000~\u0000\'\u0000\u0001sq\u0000~\u0000(q\u0000~\u0000#q\u0000~\u0000z"
+"q\u0000~\u0000\'\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q\u0000~\u00000\u0000\u0000\u0000\u0001q\u0000~\u0000-q\u0000~\u00001\u0000\u0000\u0000\u0090q\u0000~\u00003sq\u0000~\u00004q\u0000~\u0000zq\u0000~\u0000"
+"#sq\u0000~\u0000\u0010ppsq\u0000~\u00007q\u0000~\u0000\u0014pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000!AuthorizedRepre"
+"sentativeSignatureq\u0000~\u0000Jsq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0016pp"
+"sr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun."
+"msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000+q\u0000~\u0000.t\u0000\u0004da"
+"teq\u0000~\u0000>q\u0000~\u00003sq\u0000~\u00004q\u0000~\u0000\u0088q\u0000~\u0000.sq\u0000~\u0000\u0010ppsq\u0000~\u00007q\u0000~\u0000\u0014pq\u0000~\u00009q\u0000~\u0000Bq\u0000"
+"~\u0000Fsq\u0000~\u0000@t\u0000\rSubmittedDateq\u0000~\u0000Jq\u0000~\u0000Fsq\u0000~\u00007ppsr\u0000\u001ccom.sun.msv.g"
+"rammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0017L\u0000\u0004nameq\u0000~\u0000\u0018L\u0000\u0005valuet\u0000\u0012L"
+"java/lang/Object;xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u001aq\u0000~\u0000#t\u0000\u0013FormVersionDataTypeq\u0000"
+"~\u0000\'\u0000\u0001sq\u0000~\u0000(q\u0000~\u0000#q\u0000~\u0000\u0093q\u0000~\u0000\'\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q\u0000~\u00000\u0000\u0000\u0000\u0001q\u0000~\u0000-q\u0000~\u00001\u0000\u0000\u0000"
+"\u001esq\u0000~\u00004q\u0000~\u0000\u0093q\u0000~\u0000#t\u0000\u00031.1sq\u0000~\u0000@t\u0000\u000bFormVersionq\u0000~\u0000Jsq\u0000~\u0000\u0010ppsq\u0000~"
+"\u00007q\u0000~\u0000\u0014pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u0016CertificationDebarmentq\u0000~\u0000Js"
+"r\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet"
+"\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.s"
+"un.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB"
+"\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPo"
+"ol;xp\u0000\u0000\u0000\u001b\u0001pq\u0000~\u0000\u0081q\u0000~\u0000_q\u0000~\u00006q\u0000~\u0000Zq\u0000~\u0000eq\u0000~\u0000rq\u0000~\u0000}q\u0000~\u0000\u008aq\u0000~\u0000\u0099q\u0000~\u0000"
+"\u000fq\u0000~\u0000\tq\u0000~\u0000wq\u0000~\u0000iq\u0000~\u0000\u0083q\u0000~\u0000Lq\u0000~\u0000kq\u0000~\u0000\nq\u0000~\u0000Nq\u0000~\u0000mq\u0000~\u0000\u0015q\u0000~\u0000Qq\u0000~\u0000"
+"nq\u0000~\u0000\u000bq\u0000~\u0000\rq\u0000~\u0000\u0011q\u0000~\u0000\u000eq\u0000~\u0000\fx"));
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
            return gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.CertificationDebarmentImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("CertificationDebarment" == ___local)&&("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                    case  2 :
                        if (("CertificationDebarment" == ___local)&&("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.CertificationDebarmentTypeImpl)gov.grants.apply.forms.ed_certificationdebarment_v1_1.impl.CertificationDebarmentImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_CertificationDebarment-V1-1", "FormVersion");
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