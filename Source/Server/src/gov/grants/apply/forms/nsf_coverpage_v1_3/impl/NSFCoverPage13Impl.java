//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.30 at 02:44:18 EDT 
//


package gov.grants.apply.forms.nsf_coverpage_v1_3.impl;

public class NSFCoverPage13Impl
    extends gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl
    implements gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.nsf_coverpage_v1_3.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13 .class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/NSF_CoverPage-V1-3";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "NSF_CoverPage_1_3";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13Impl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "NSF_CoverPage_1_3");
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
        return (gov.grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13 .class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun."
+"msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/"
+"Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPai"
+"r;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tminLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVal"
+"ueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Dat"
+"aTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFla"
+"gL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fc"
+"oncreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tface"
+"tNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDat"
+"atypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001bL\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nw"
+"hiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xp"
+"t\u00000http://apply.grants.gov/forms/NSF_CoverPage-V1-3psr\u00005com."
+"sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"p\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\t"
+"maxLengthxq\u0000~\u0000\u0017t\u00001http://apply.grants.gov/system/GlobalLibra"
+"ry-V2.0t\u0000\u0015OpportunityIDDataTypeq\u0000~\u0000\"\u0000\u0001sq\u0000~\u0000\u0016q\u0000~\u0000%q\u0000~\u0000&q\u0000~\u0000\"\u0000"
+"\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwa"
+"ysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u001ct\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000\"\u0001q\u0000~\u0000+t\u0000\t"
+"minLength\u0000\u0000\u0000\u0001q\u0000~\u0000+t\u0000\tmaxLength\u0000\u0000\u0000(q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0001sr\u00000com.sun."
+"msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004pps"
+"r\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000"
+"\fnamespaceURIq\u0000~\u0000\u001bxpt\u0000\u000estring-derivedq\u0000~\u0000\u001fsr\u0000\u001dcom.sun.msv.gr"
+"ammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.At"
+"tributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011j"
+"ava.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0012ppsr\u0000\"com.sun.m"
+"sv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0005QNamesr\u00005c"
+"om.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000>q\u0000~\u0000,sr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000"
+"\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://"
+"www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u00009\u0001q\u0000~\u0000Hsq\u0000~\u0000"
+"Bt\u0000\u0018FundingOpportunityNumberq\u0000~\u0000\u001fsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0012ppsr"
+"\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.ms"
+"v.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0004date"
+"q\u0000~\u0000@q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000Rq\u0000~\u0000,sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000"
+"Hsq\u0000~\u0000Bt\u0000\u0007DueDateq\u0000~\u0000\u001fsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsr\u0000 "
+"com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.g"
+"rammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:"
+"psr\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000Iq\u0000~\u0000asr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Cq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Ugov.grants.apply.forms.nsf_cove"
+"rpage_v1_3.NSFCoverPage13Type.NSFUnitConsiderationTypet\u0000+htt"
+"p://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:p"
+"q\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0014NSFUnitConsiderationq\u0000~\u0000\u001fsq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq\u0000~\u0000\\q\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000aq\u0000~\u0000cq\u0000~"
+"\u0000Hsq\u0000~\u0000Bt\u0000Ggov.grants.apply.forms.nsf_coverpage_v1_3.NSFCove"
+"rPage13Type.PIInfoTypeq\u0000~\u0000fsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~"
+"\u0000Hsq\u0000~\u0000Bt\u0000\u0006PIInfoq\u0000~\u0000\u001fsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u00005ppsq\u0000~\u0000\\q\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000aq\u0000~\u0000cq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Jgov."
+"grants.apply.forms.nsf_coverpage_v1_3.NSFCoverPage13Type.Oth"
+"erInfoTypeq\u0000~\u0000fsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\tO"
+"therInfoq\u0000~\u0000\u001fq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u00005ppsq\u0000~\u0000\\q\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000aq\u0000~\u0000cq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Hgov.gran"
+"ts.apply.system.attachments_v1.AttachmentGroupMin1Max100Data"
+"Typeq\u0000~\u0000fsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0014Single-"
+"CopyDocumentsq\u0000~\u0000\u001fq\u0000~\u0000Hsq\u0000~\u00007ppsr\u0000\u001ccom.sun.msv.grammar.Value"
+"Exp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0013L\u0000\u0004nameq\u0000~\u0000\u0014L\u0000\u0005valuet\u0000\u0012Ljava/lang/Ob"
+"ject;xq\u0000~\u0000\u0004ppsq\u0000~\u0000#q\u0000~\u0000%t\u0000\u0013FormVersionDataTypeq\u0000~\u0000\"\u0000\u0001sq\u0000~\u0000\u0016q"
+"\u0000~\u0000%q\u0000~\u0000\u0096q\u0000~\u0000\"\u0000\u0000q\u0000~\u0000+q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0001q\u0000~\u0000+q\u0000~\u0000/\u0000\u0000\u0000\u001esq\u0000~\u00002q\u0000~\u0000\u0096"
+"q\u0000~\u0000%t\u0000\u00031.3sq\u0000~\u0000Bt\u0000\u000bFormVersionq\u0000~\u0000\u001fsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000"
+";q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0011NSF_CoverPage_1_3q\u0000~\u0000\u001fsr\u0000\"com.sun.msv.gr"
+"ammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gr"
+"ammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Ex"
+"pressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000"
+"\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u001e\u0001pq\u0000~\u0000[q"
+"\u0000~\u0000nq\u0000~\u0000{q\u0000~\u0000\u0088q\u0000~\u0000Yq\u0000~\u0000lq\u0000~\u0000yq\u0000~\u0000\u0086q\u0000~\u0000\u000fq\u0000~\u0000\nq\u0000~\u0000\u000bq\u0000~\u0000wq\u0000~\u0000\u0084q"
+"\u0000~\u0000^q\u0000~\u0000oq\u0000~\u0000|q\u0000~\u00006q\u0000~\u0000Tq\u0000~\u0000gq\u0000~\u0000sq\u0000~\u0000\u0080q\u0000~\u0000\fq\u0000~\u0000\u0089q\u0000~\u0000\u0011q\u0000~\u0000\u008dq"
+"\u0000~\u0000\u009cq\u0000~\u0000\tq\u0000~\u0000\rq\u0000~\u0000Mq\u0000~\u0000\u000ex"));
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
            return gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13Impl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("NSF_CoverPage_1_3" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1-3" == ___uri)) {
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "FormVersion");
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
                        if (("NSF_CoverPage_1_3" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1-3" == ___uri)) {
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1-3" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13TypeImpl)gov.grants.apply.forms.nsf_coverpage_v1_3.impl.NSFCoverPage13Impl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1-3", "FormVersion");
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
