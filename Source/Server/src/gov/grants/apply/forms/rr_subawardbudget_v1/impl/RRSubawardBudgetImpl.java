//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_subawardbudget_v1.impl;

public class RRSubawardBudgetImpl
    extends gov.grants.apply.forms.rr_subawardbudget_v1.impl.RRSubawardBudgetTypeImpl
    implements gov.grants.apply.forms.rr_subawardbudget_v1.RRSubawardBudget, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.rr_subawardbudget_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_subawardbudget_v1.RRSubawardBudget.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/RR_SubawardBudget-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RR_SubawardBudget";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_subawardbudget_v1.impl.RRSubawardBudgetImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0", "RR_SubawardBudget");
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
        return (gov.grants.apply.forms.rr_subawardbudget_v1.RRSubawardBudget.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007"
+"ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/rel"
+"axng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/"
+"util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000#com.sun.msv.datatype.xsd.StringT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.B"
+"uiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Conc"
+"reteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeIm"
+"pl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeName"
+"q\u0000~\u0000#L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpacePro"
+"cessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tl"
+"ocalNameq\u0000~\u0000#L\u0000\fnamespaceURIq\u0000~\u0000#xpq\u0000~\u0000\'q\u0000~\u0000&sq\u0000~\u0000\u0015ppsr\u0000 com"
+".sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameCl"
+"assq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0019psq\u0000~\u0000\u001bppsr\u0000\"com.sun.msv.datatype.xsd.Qna"
+"meType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 q\u0000~\u0000&t\u0000\u0005QNamesr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,sq\u0000~\u0000"
+"-q\u0000~\u00005q\u0000~\u0000&sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002L\u0000\tlocalNameq\u0000~\u0000#L\u0000\fnamespaceURIq\u0000~\u0000#xr\u0000\u001dcom.sun.msv.gramma"
+"r.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XML"
+"Schema-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExp"
+"ression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0018\u0001q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT1t\u00004http://a"
+"pply.grants.gov/forms/RR_SubawardBudget-V1.0q\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq"
+"\u0000~\u00009t\u0000\u0004ATT2q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000"
+"\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT3q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015p"
+"psq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~"
+"\u0000?sq\u0000~\u00009t\u0000\u0004ATT4q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001es"
+"q\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT5q\u0000~\u0000Cq\u0000~\u0000?sq\u0000"
+"~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000"
+";q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT6q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000"
+"~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT7q\u0000~\u0000Cq\u0000~\u0000"
+"?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002"
+"q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT8q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007"
+"ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0004ATT9q\u0000~\u0000C"
+"q\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq"
+"\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0005ATT10q\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000s"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0015ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003exp"
+"q\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000\u0019psq\u0000~\u00000q\u0000~\u0000\u0019psr\u00002com.sun.msv.grammar.Express"
+"ion$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000@q\u0000~\u0000\u008dsr\u0000 com.su"
+"n.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000:q\u0000~\u0000?sq\u0000~\u00009t\u0000Vgov"
+".grants.apply.forms.rr_subawardbudget_v1.RRSubawardBudgetTyp"
+"e.BudgetAttachmentsTypet\u0000+http://java.sun.com/jaxb/xjc/dummy"
+"-elementssq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019pq\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0011BudgetA"
+"ttachmentsq\u0000~\u0000Cq\u0000~\u0000?sq\u0000~\u00000ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u001cL\u0000\u0004nameq\u0000~\u0000\u001dL\u0000\u0005valuet\u0000\u0012Ljava/lang/Objec"
+"t;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVal"
+"ueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Dat"
+"aTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFla"
+"gL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fc"
+"oncreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tface"
+"tNameq\u0000~\u0000#xq\u0000~\u0000\"t\u00001http://apply.grants.gov/system/GlobalLibr"
+"ary-V1.0t\u0000\u0013FormVersionDataTypeq\u0000~\u0000*\u0000\u0001sr\u0000\'com.sun.msv.datatyp"
+"e.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u009cq\u0000~\u0000\u00a1q\u0000~\u0000\u00a2q"
+"\u0000~\u0000*\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000%t\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u0000-q"
+"\u0000~\u0000\u00a2q\u0000~\u0000\u00a1t\u0000\u00031.0sq\u0000~\u00009t\u0000\u000bFormVersionq\u0000~\u0000Csq\u0000~\u0000\u0015ppsq\u0000~\u00000q\u0000~\u0000\u0019p"
+"q\u0000~\u00002q\u0000~\u0000;q\u0000~\u0000?sq\u0000~\u00009t\u0000\u0011RR_SubawardBudgetq\u0000~\u0000Csr\u0000\"com.sun.ms"
+"v.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/ms"
+"v/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramma"
+"r.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersi"
+"onL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u00000\u0001pq\u0000"
+"~\u0000\rq\u0000~\u0000\u0085q\u0000~\u0000\u000bq\u0000~\u0000\tq\u0000~\u0000\u0016q\u0000~\u0000Dq\u0000~\u0000Kq\u0000~\u0000Rq\u0000~\u0000Yq\u0000~\u0000`q\u0000~\u0000gq\u0000~\u0000nq\u0000"
+"~\u0000\u000eq\u0000~\u0000uq\u0000~\u0000|q\u0000~\u0000\fq\u0000~\u0000\u0083q\u0000~\u0000\u0014q\u0000~\u0000\u001aq\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000[q\u0000~\u0000bq\u0000"
+"~\u0000iq\u0000~\u0000pq\u0000~\u0000wq\u0000~\u0000~q\u0000~\u0000\u0011q\u0000~\u0000\u0087q\u0000~\u0000/q\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000\u0013q\u0000"
+"~\u0000cq\u0000~\u0000jq\u0000~\u0000qq\u0000~\u0000xq\u0000~\u0000\u007fq\u0000~\u0000\u0093q\u0000~\u0000\u00abq\u0000~\u0000\u000fq\u0000~\u0000\u0012q\u0000~\u0000\u008aq\u0000~\u0000\u0010q\u0000~\u0000\nx"));
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
            return gov.grants.apply.forms.rr_subawardbudget_v1.impl.RRSubawardBudgetImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("RR_SubawardBudget" == ___local)&&("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                        if (("RR_SubawardBudget" == ___local)&&("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.rr_subawardbudget_v1.impl.RRSubawardBudgetTypeImpl)gov.grants.apply.forms.rr_subawardbudget_v1.impl.RRSubawardBudgetImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_SubawardBudget-V1.0", "FormVersion");
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
