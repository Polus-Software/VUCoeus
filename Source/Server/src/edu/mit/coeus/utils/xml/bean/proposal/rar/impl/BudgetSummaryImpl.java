//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class BudgetSummaryImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.BudgetSummaryTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetSummary, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetSummary.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "BudgetSummary";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.BudgetSummaryImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace", "BudgetSummary");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetSummary.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000\u0000pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000"
+" com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv."
+"grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Bo"
+"olean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attribut"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u001apsr\u00002c"
+"om.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0004sq\u0000~\u0000\u0019\u0001q\u0000~\u0000\u001esr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.s"
+"un.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004"
+"q\u0000~\u0000\u001fq\u0000~\u0000$sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002"
+"L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000&xq\u0000~\u0000!t"
+"\u0000:edu.mit.coeus.utils.xml.bean.proposal.rar.BudgetTotalsType"
+"t\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001b"
+"q\u0000~\u0000\u001apsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg"
+"/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/"
+"msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.Qna"
+"meType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003"
+"L\u0000\fnamespaceUriq\u0000~\u0000&L\u0000\btypeNameq\u0000~\u0000&L\u0000\nwhiteSpacet\u0000.Lcom/sun"
+"/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org"
+"/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpa"
+"ceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expr"
+"ession$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001apsr\u0000\u001bcom.sun.m"
+"sv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000&L\u0000\fnamespaceUR"
+"Iq\u0000~\u0000&xpq\u0000~\u00007q\u0000~\u00006sq\u0000~\u0000%t\u0000\u0004typet\u0000)http://www.w3.org/2001/XML"
+"Schema-instanceq\u0000~\u0000$sq\u0000~\u0000%t\u0000\u0013InitialBudgetTotalst\u0000\u0000sq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q"
+"\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000(q\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000"
+"%t\u0000\u000fAllBudgetTotalsq\u0000~\u0000Dsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq"
+"\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%t\u0000:edu.mit.coeus."
+"utils.xml.bean.proposal.rar.BudgetPeriodTypeq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000"
+"~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%t\u0000\fBudgetPeriodq\u0000~\u0000Dsq\u0000~\u0000\u0014ppsq"
+"\u0000~\u0000\u0007q\u0000~\u0000\u001apsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001ap"
+"sq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000Wq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~"
+"\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$q\u0000~\u0000Zsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0007q\u0000~\u0000\u001apsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$"
+"sq\u0000~\u0000%q\u0000~\u0000Wq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$q\u0000~\u0000Zsq\u0000~"
+"\u0000\u0014ppsq\u0000~\u0000\u0007q\u0000~\u0000\u001apsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016"
+"q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000Wq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000"
+"~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$q\u0000~\u0000Zsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%"
+"q\u0000~\u0000Wq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$q\u0000~\u0000Zq\u0000~\u0000$q\u0000~\u0000$"
+"q\u0000~\u0000$q\u0000~\u0000$sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~"
+"\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%t\u0000>edu.mit.coeus.ut"
+"ils.xml.bean.proposal.rar.DescriptionBlockTypeq\u0000~\u0000)sq\u0000~\u0000\u0014pps"
+"q\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%t\u0000\u0013BudgetJustificationq\u0000~\u0000Dq"
+"\u0000~\u0000$sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000"
+"\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%t\u0000\u001eorg.w3._2001.xmlschema"
+".AnyTypeq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%t\u0000\u0016Bud"
+"getDirectCostsTotalq\u0000~\u0000Dq\u0000~\u0000$sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%"
+"q\u0000~\u0000\u009cq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%t\u0000\u0018Budget"
+"IndirectCostsTotalq\u0000~\u0000Dq\u0000~\u0000$sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001ap\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001apsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000\u001eq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%q"
+"\u0000~\u0000\u009cq\u0000~\u0000)sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%t\u0000\u0010BudgetC"
+"ostsTotalq\u0000~\u0000Dq\u0000~\u0000$sq\u0000~\u0000\u0014ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001apq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000$sq\u0000~\u0000%"
+"t\u0000\rBudgetSummaryt\u0000Ehttp://era.nih.gov/Projectmgmt/SBIR/CGAP/"
+"researchandrelated.namespacesr\u0000\"com.sun.msv.grammar.Expressi"
+"onPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expressi"
+"onPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$C"
+"losedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom"
+"/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000@\u0001pq\u0000~\u0000\u000fq\u0000~\u0000\u00a3q\u0000~\u0000\u0096q\u0000~\u0000\u0089"
+"q\u0000~\u0000\u007fq\u0000~\u0000uq\u0000~\u0000jq\u0000~\u0000_q\u0000~\u0000Qq\u0000~\u0000Fq\u0000~\u0000\u0012q\u0000~\u0000\u00afq\u0000~\u0000hq\u0000~\u0000sq\u0000~\u0000\u00a1q\u0000~\u0000\u0094"
+"q\u0000~\u0000\u0087q\u0000~\u0000}q\u0000~\u0000\u00adq\u0000~\u0000\u00a5q\u0000~\u0000\u0098q\u0000~\u0000\u008bq\u0000~\u0000\u0081q\u0000~\u0000wq\u0000~\u0000lq\u0000~\u0000aq\u0000~\u0000Sq\u0000~\u0000H"
+"q\u0000~\u0000\u0015q\u0000~\u0000\u00b1q\u0000~\u0000rq\u0000~\u0000\u000bq\u0000~\u0000\nq\u0000~\u0000\u000eq\u0000~\u0000\fq\u0000~\u0000\rq\u0000~\u0000\u00a9q\u0000~\u0000\u009dq\u0000~\u0000\u0090q\u0000~\u0000\u0085"
+"q\u0000~\u0000{q\u0000~\u0000pq\u0000~\u0000eq\u0000~\u0000Xq\u0000~\u0000Lq\u0000~\u0000*q\u0000~\u0000\u0010q\u0000~\u0000\u00b5q\u0000~\u0000\u00b9q\u0000~\u0000]q\u0000~\u0000gq\u0000~\u0000\t"
+"q\u0000~\u0000\\q\u0000~\u0000\u00b2q\u0000~\u0000\u00a6q\u0000~\u0000\u0099q\u0000~\u0000\u008cq\u0000~\u0000\u0082q\u0000~\u0000xq\u0000~\u0000mq\u0000~\u0000bq\u0000~\u0000Tq\u0000~\u0000Iq\u0000~\u0000\u0018"
+"x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.BudgetSummaryImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("BudgetSummary" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("InitialBudgetTotals" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.BudgetSummaryTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.BudgetSummaryImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                        if (("BudgetSummary" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
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
