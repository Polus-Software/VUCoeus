//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class CategorySetImpl
    extends gov.grants.apply.forms.sf424a_v1.impl.CategorySetTypeImpl
    implements gov.grants.apply.forms.sf424a_v1.CategorySet, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.CategorySet.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424A-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "CategorySet";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.CategorySetImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "CategorySet");
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
        return (gov.grants.apply.forms.sf424a_v1.CategorySet.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000"
+"p\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001f"
+"Lorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/"
+"sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000,com.sun.msv.datatype.xsd"
+".FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datat"
+"ype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xr\u0000*com"
+".sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacet"
+"FixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatyp"
+"e/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype"
+"/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.s"
+"un.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUri"
+"q\u0000~\u0000%L\u0000\btypeNameq\u0000~\u0000%L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/x"
+"sd/WhiteSpaceProcessor;xpt\u0000*http://apply.grants.gov/system/G"
+"lobal-V1.0t\u0000\u001bDecimalMin1Max14Places2Typesr\u00005com.sun.msv.data"
+"type.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun."
+"msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000)com.s"
+"un.msv.datatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tprecisionx"
+"q\u0000~\u0000!q\u0000~\u0000)q\u0000~\u0000*q\u0000~\u0000-\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000&t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007decimalq\u0000~\u0000-q\u0000~"
+"\u00003t\u0000\u000btotalDigits\u0000\u0000\u0000\u000eq\u0000~\u00003t\u0000\u000efractionDigits\u0000\u0000\u0000\u0002sr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000"
+"\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L\u0000\fn"
+"amespaceURIq\u0000~\u0000%xpq\u0000~\u0000*q\u0000~\u0000)sq\u0000~\u0000\u0016ppsr\u0000 com.sun.msv.grammar."
+"AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~"
+"\u0000\u001apsq\u0000~\u0000\u001cppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u00001q\u0000~\u00004t\u0000\u0005QNameq\u0000~\u0000-q\u0000~\u00009sq\u0000~\u0000:q\u0000~\u0000Bq\u0000~\u00004sr\u0000#com.sun.msv"
+".grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L\u0000\fnames"
+"paceURIq\u0000~\u0000%xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000"
+"\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun"
+".msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq"
+"\u0000~\u0000\u0019\u0001q\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001eBudgetPersonnelRequestedAmountt\u0000)http://a"
+"pply.grants.gov/forms/SF424A-V1.0q\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000s"
+"q\u0000~\u0000\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000#Budg"
+"etFringeBenefitsRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001a"
+"p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001bB"
+"udgetTravelRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000"
+"~\u0000\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001eBudget"
+"EquipmentRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000"
+"\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001dBudgetSu"
+"ppliesRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000 BudgetContr"
+"actualRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000!BudgetConst"
+"ructionRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007p"
+"pq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001aBudgetOthe"
+"rRequestedAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001f"
+"sq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001eBudgetTotalDirec"
+"tChargesAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001fsq"
+"\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u001bBudgetIndirectChar"
+"gesAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016p"
+"psq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u0011BudgetTotalAmountq\u0000~\u0000Nq"
+"\u0000~\u0000Jsq\u0000~\u0000\u0016ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001fsq\u0000~\u0000\u0016ppsq\u0000~\u0000=q\u0000~\u0000\u001apq\u0000"
+"~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u0013ProgramIncomeAmountq\u0000~\u0000Nq\u0000~\u0000Jsq\u0000~\u0000=pps"
+"q\u0000~\u0000\u001cppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueC"
+"onstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xq\u0000~\u0000\"q\u0000~\u0000)t\u0000\u0014StringMin1Max120Types"
+"r\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u009fq\u0000~\u0000)q\u0000~\u0000\u00a1q\u0000~\u0000\u00a3\u0000\u0000sr\u0000#com.sun.msv."
+"datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u00001q\u0000~\u0000"
+"4t\u0000\u0006stringq\u0000~\u0000\u00a3\u0001q\u0000~\u0000\u00a7t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000\u00a7t\u0000\tmaxLength\u0000\u0000\u0000xq\u0000"
+"~\u00009sq\u0000~\u0000:q\u0000~\u0000\u00a1q\u0000~\u0000)sq\u0000~\u0000Dt\u0000\ractivityTitleq\u0000~\u0000Nsq\u0000~\u0000\u0016ppsq\u0000~\u0000="
+"q\u0000~\u0000\u001apq\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000Jsq\u0000~\u0000Dt\u0000\u000bCategorySetq\u0000~\u0000Nsr\u0000\"com.sun.ms"
+"v.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/ms"
+"v/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramma"
+"r.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersi"
+"onL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u00002\u0001pq\u0000"
+"~\u0000\u000bq\u0000~\u0000\u0013q\u0000~\u0000\u000fq\u0000~\u0000\u0012q\u0000~\u0000<q\u0000~\u0000Rq\u0000~\u0000Yq\u0000~\u0000`q\u0000~\u0000gq\u0000~\u0000nq\u0000~\u0000uq\u0000~\u0000|q\u0000"
+"~\u0000\u0083q\u0000~\u0000\u008aq\u0000~\u0000\u0091q\u0000~\u0000\u0098q\u0000~\u0000\u00aeq\u0000~\u0000\u0015q\u0000~\u0000\u0011q\u0000~\u0000\rq\u0000~\u0000\u000eq\u0000~\u0000\u0017q\u0000~\u0000Oq\u0000~\u0000Vq\u0000"
+"~\u0000]q\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000rq\u0000~\u0000yq\u0000~\u0000\u0080q\u0000~\u0000\u0087q\u0000~\u0000\u008eq\u0000~\u0000\u0095q\u0000~\u0000\tq\u0000~\u0000\nq\u0000~\u0000\u0010q\u0000"
+"~\u0000\u0014q\u0000~\u0000\fq\u0000~\u0000\u001bq\u0000~\u0000Qq\u0000~\u0000Xq\u0000~\u0000_q\u0000~\u0000fq\u0000~\u0000mq\u0000~\u0000tq\u0000~\u0000{q\u0000~\u0000\u0082q\u0000~\u0000\u0089q\u0000"
+"~\u0000\u0090q\u0000~\u0000\u0097x"));
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
            return gov.grants.apply.forms.sf424a_v1.impl.CategorySetImpl.this;
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("CategorySet" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
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
                    case  2 :
                        if (("CategorySet" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
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
                        if (("activityTitle" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424a_v1.impl.CategorySetTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.CategorySetImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
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
