//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.27 at 01:42:23 CDT 
//


package gov.grants.apply.forms.rr_budget_1_4_v1.impl;

public class RRBudget14Impl
    extends gov.grants.apply.forms.rr_budget_1_4_v1.impl.RRBudget14TypeImpl
    implements gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.rr_budget_1_4_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14 .class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/RR_Budget_1_4-V1.4";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RR_Budget_1_4";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_budget_1_4_v1.impl.RRBudget14Impl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4", "RR_Budget_1_4");
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
        return (gov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14 .class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001b"
+"com.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/d"
+"atatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/S"
+"tringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFace"
+"t\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTyp"
+"eWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype"
+".xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValue"
+"CheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeI"
+"mpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType"
+";L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.x"
+"sd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001cL\u0000\btypeNameq"
+"\u0000~\u0000\u001cL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProc"
+"essor;xpt\u00001http://apply.grants.gov/system/GlobalLibrary-V2.0"
+"t\u0000\u000eDUNSIDDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProc"
+"essor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.Min"
+"LengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0018q\u0000~\u0000 q\u0000~\u0000!q\u0000~\u0000$\u0000\u0000sr\u0000"
+"#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysVa"
+"lidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dt\u0000"
+" http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000$\u0001q\u0000~\u0000*t\u0000\tminL"
+"ength\u0000\u0000\u0000\tq\u0000~\u0000*t\u0000\tmaxLength\u0000\u0000\u0000\rsr\u00000com.sun.msv.grammar.Expres"
+"sion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.uti"
+"l.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001c"
+"xpq\u0000~\u0000!q\u0000~\u0000 sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\bppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~"
+"\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005"
+"valuexp\u0000psq\u0000~\u0000\u0013ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000(q\u0000~\u0000+t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000#q\u0000~\u00000sq\u0000~\u00001q\u0000~\u0000<q\u0000~\u0000+"
+"sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNa"
+"meq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxr\u0000\u001dcom.sun.msv.grammar.NameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-inst"
+"ancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u00007\u0001q\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u0006DUNSIDt\u00000http://apply.gran"
+"ts.gov/forms/RR_Budget_1_4-V1.4sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013ppsr\u0000)"
+"com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006value"
+"st\u0000\u000fLjava/util/Set;xq\u0000~\u0000\u0018q\u0000~\u0000Jt\u0000\u0012BudgetTypeDataTypeq\u0000~\u0000$\u0000\u0000q\u0000"
+"~\u0000*q\u0000~\u0000*t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000"
+"\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0007Projectt\u0000\u0013Subaward/Consortiumxq\u0000~\u00000sq\u0000~\u00001q\u0000~\u0000Qq"
+"\u0000~\u0000Jsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\nBudgetTypeq\u0000"
+"~\u0000Jsq\u0000~\u00003ppsq\u0000~\u0000\u0000q\u0000~\u00008p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013ppsq\u0000~\u0000\u0017q\u0000~\u0000 t\u0000\u0018Organi"
+"zationNameDataTypeq\u0000~\u0000$\u0000\u0001sq\u0000~\u0000%q\u0000~\u0000 q\u0000~\u0000aq\u0000~\u0000$\u0000\u0000q\u0000~\u0000*q\u0000~\u0000*q\u0000"
+"~\u0000-\u0000\u0000\u0000\u0001q\u0000~\u0000*q\u0000~\u0000.\u0000\u0000\u0000<q\u0000~\u00000sq\u0000~\u00001q\u0000~\u0000aq\u0000~\u0000 sq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u0000"
+"8pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\u0010OrganizationNameq\u0000~\u0000Jq\u0000~\u0000Fsq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00003ppsr\u0000 com.sun.msv.grammar.OneOrMoreE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003e"
+"xpq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u00008psq\u0000~\u00005q\u0000~\u00008psr\u00002com.sun.msv.grammar.Expre"
+"ssion$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000Gq\u0000~\u0000qsr\u0000 com."
+"sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Aq\u0000~\u0000Fsq\u0000~\u0000@t\u0000:g"
+"ov.grants.apply.forms.rr_budget_1_4_v1.BudgetYearDataTypet\u0000+"
+"http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u00003ppsq\u0000~\u00005q\u0000~"
+"\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\nBudgetYearq\u0000~\u0000Jsq\u0000~\u00003ppsq\u0000~\u0000\u0007q\u0000~\u0000"
+"8psq\u0000~\u0000\u0000q\u0000~\u00008p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq\u0000~\u00005q\u0000"
+"~\u00008pq\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fsq\u0000~\u0000@q\u0000~\u0000uq\u0000~\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009"
+"q\u0000~\u0000Bq\u0000~\u0000Fq\u0000~\u0000ysq\u0000~\u00003ppsq\u0000~\u0000\u0007q\u0000~\u00008psq\u0000~\u0000\u0000q\u0000~\u00008p\u0000sq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000\u0000pp\u0000sq\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq\u0000~\u00005q\u0000~\u00008pq\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fsq\u0000~\u0000@q\u0000"
+"~\u0000uq\u0000~\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fq\u0000~\u0000ysq\u0000~\u00003ppsq\u0000~"
+"\u0000\u0007q\u0000~\u00008psq\u0000~\u0000\u0000q\u0000~\u00008p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq"
+"\u0000~\u00005q\u0000~\u00008pq\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fsq\u0000~\u0000@q\u0000~\u0000uq\u0000~\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008"
+"pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fq\u0000~\u0000ysq\u0000~\u00003ppsq\u0000~\u0000\u0000q\u0000~\u00008p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000s"
+"q\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq\u0000~\u00005q\u0000~\u00008pq\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fsq\u0000~\u0000@q\u0000~\u0000uq\u0000~"
+"\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fq\u0000~\u0000yq\u0000~\u0000Fq\u0000~\u0000Fq\u0000~\u0000Fq\u0000~"
+"\u0000Fsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq\u0000~\u00005q\u0000~\u00008p"
+"q\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fsq\u0000~\u0000@t\u0000;gov.grants.apply.system.attachments_"
+"v1.AttachedFileDataTypeq\u0000~\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000"
+"~\u0000Fsq\u0000~\u0000@t\u0000\u001dBudgetJustificationAttachmentq\u0000~\u0000Jsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00003ppsq\u0000~\u0000lq\u0000~\u00008psq\u0000~\u00005q\u0000~\u00008pq\u0000~\u0000qq\u0000~\u0000sq\u0000~\u0000Fs"
+"q\u0000~\u0000@t\u0000Hgov.grants.apply.forms.rr_budget_1_4_v1.RRBudget14Ty"
+"pe.BudgetSummaryTypeq\u0000~\u0000vsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008pq\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000F"
+"sq\u0000~\u0000@t\u0000\rBudgetSummaryq\u0000~\u0000Jsq\u0000~\u00005ppsr\u0000\u001ccom.sun.msv.grammar.V"
+"alueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0014L\u0000\u0004nameq\u0000~\u0000\u0015L\u0000\u0005valuet\u0000\u0012Ljava/lan"
+"g/Object;xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0017q\u0000~\u0000 t\u0000\u0013FormVersionDataTypeq\u0000~\u0000$\u0000\u0001sq\u0000"
+"~\u0000%q\u0000~\u0000 q\u0000~\u0000\u00c3q\u0000~\u0000$\u0000\u0000q\u0000~\u0000*q\u0000~\u0000*q\u0000~\u0000-\u0000\u0000\u0000\u0001q\u0000~\u0000*q\u0000~\u0000.\u0000\u0000\u0000\u001esq\u0000~\u00001q"
+"\u0000~\u0000\u00c3q\u0000~\u0000 t\u0000\u00031.4sq\u0000~\u0000@t\u0000\u000bFormVersionq\u0000~\u0000Jsq\u0000~\u00003ppsq\u0000~\u00005q\u0000~\u00008p"
+"q\u0000~\u00009q\u0000~\u0000Bq\u0000~\u0000Fsq\u0000~\u0000@t\u0000\rRR_Budget_1_4q\u0000~\u0000Jsr\u0000\"com.sun.msv.gr"
+"ammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gr"
+"ammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Ex"
+"pressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000"
+"\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u00003\u0001pq\u0000~\u00004q"
+"\u0000~\u0000Xq\u0000~\u0000\u0012q\u0000~\u0000dq\u0000~\u0000wq\u0000~\u0000\u0084q\u0000~\u0000\u008fq\u0000~\u0000\u009aq\u0000~\u0000\u00a4q\u0000~\u0000|q\u0000~\u0000\u00aeq\u0000~\u0000\fq\u0000~\u0000\u00baq"
+"\u0000~\u0000\u00c9q\u0000~\u0000\rq\u0000~\u0000\u0010q\u0000~\u0000\nq\u0000~\u0000\u009cq\u0000~\u0000{q\u0000~\u0000\u000fq\u0000~\u0000nq\u0000~\u0000\u0081q\u0000~\u0000\u008cq\u0000~\u0000\u0097q\u0000~\u0000\u00a1q"
+"\u0000~\u0000\u00aaq\u0000~\u0000\u00b6q\u0000~\u0000\u000bq\u0000~\u0000\u0087q\u0000~\u0000\tq\u0000~\u0000^q\u0000~\u0000\u0092q\u0000~\u0000iq\u0000~\u0000~q\u0000~\u0000\u0089q\u0000~\u0000\u0094q\u0000~\u0000\u009eq"
+"\u0000~\u0000\u00a7q\u0000~\u0000\u00b3q\u0000~\u0000Lq\u0000~\u0000\u0086q\u0000~\u0000kq\u0000~\u0000\u0080q\u0000~\u0000\u008bq\u0000~\u0000\u0096q\u0000~\u0000\\q\u0000~\u0000\u00a0q\u0000~\u0000\u0091q\u0000~\u0000\u00a9q"
+"\u0000~\u0000\u00b5q\u0000~\u0000\u000ex"));
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
            return gov.grants.apply.forms.rr_budget_1_4_v1.impl.RRBudget14Impl.this;
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
                    case  0 :
                        if (("RR_Budget_1_4" == ___local)&&("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4", "FormVersion");
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
                        if (("RR_Budget_1_4" == ___local)&&("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4", "FormVersion");
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.rr_budget_1_4_v1.impl.RRBudget14TypeImpl)gov.grants.apply.forms.rr_budget_1_4_v1.impl.RRBudget14Impl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_Budget_1_4-V1.4", "FormVersion");
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
