//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class SalariesAndWagesImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.SalariesAndWagesTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.rar.SalariesAndWages, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.SalariesAndWages.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "SalariesAndWages";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.SalariesAndWagesImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace", "SalariesAndWages");
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
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.SalariesAndWages.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sr\u0000\u001dcom.sun.msv"
+".grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar"
+".OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005v"
+"aluexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003ex"
+"pq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u001fpsr\u00002com.sun.msv.grammar.E"
+"xpression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u001e\u0001q\u0000~\u0000#sr"
+"\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv"
+".grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expre"
+"ssion$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000$q\u0000~\u0000)sr\u0000#com.su"
+"n.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava"
+"/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000+xq\u0000~\u0000&t\u0000<edu.mit.coeus.util"
+"s.xml.bean.proposal.rar.PersonFullNameTypet\u0000+http://java.sun"
+".com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpsr\u0000\u001bcom.sun."
+"msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/"
+"Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPai"
+"r;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%"
+"com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun."
+"msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~"
+"\u0000+L\u0000\btypeNameq\u0000~\u0000+L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/"
+"WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005"
+"QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collap"
+"se\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcesso"
+"r\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001fpsr\u0000\u001bcom.sun.msv.util.StringPair"
+"\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000+L\u0000\fnamespaceURIq\u0000~\u0000+xpq\u0000~\u0000<q\u0000~\u0000;"
+"sq\u0000~\u0000*t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~"
+"\u0000)sq\u0000~\u0000*t\u0000\u0004Namet\u0000\u0000sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsr\u0000)com.sun.msv.d"
+"atatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/uti"
+"l/Set;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstrai"
+"ntFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithF"
+"acet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTy"
+"pet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTyp"
+"et\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000+"
+"xq\u0000~\u00008t\u0000Ehttp://era.nih.gov/Projectmgmt/SBIR/CGAP/researchan"
+"drelated.namespacet\u0000\u000fProjectRoleTypeq\u0000~\u0000?\u0000\u0000sr\u0000\"com.sun.msv.d"
+"atatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd"
+".StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u00006q\u0000~\u0000;t\u0000\u0005tokenq\u0000~"
+"\u0000?\u0001q\u0000~\u0000Xt\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000"
+"\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\nt\u0000\u0012Other Professionalt\u0000\u0005PI/PDt\u0000\u0014Secretarial/Cleric"
+"alt\u0000\u0017Post Doctoral Associatet\u0000\bCo-PI/PDt\u0000\u0010Graduate Studentt\u0000"
+"\u0015Undergraduate Studentt\u0000\nKey Persont\u0000\rPost Doctoralt\u0000\u0005Otherx"
+"q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000Uq\u0000~\u0000Tsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~"
+"\u0000*t\u0000\u000bProjectRoleq\u0000~\u0000Isq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppq\u0000"
+"~\u0000Xq\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000Yq\u0000~\u0000;sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)s"
+"q\u0000~\u0000*t\u0000\u0016ProjectRoleDescriptionq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u00001ppsr\u0000\'com.sun.msv.datatype.xsd.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I"
+"\u0000\nfinalValuexr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"baseTypeq\u0000~\u0000Qxq\u0000~\u00008t\u00009http://era.nih.gov/Projectmgmt/SBIR/CG"
+"AP/common.namespacet\u0000\u0017AppointmentCategoryTypeq\u0000~\u0000?q\u0000~\u0000X\u0000\u0000\u0000\u0000q"
+"\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000Yq\u0000~\u0000{sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000"
+"*t\u0000\u000fAppointmentTypeq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsq\u0000~\u0000xq\u0000~\u0000{"
+"t\u0000\u000fMonthNumberTypeq\u0000~\u0000?sr\u0000#com.sun.msv.datatype.xsd.NumberTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00006q\u0000~\u0000;t\u0000\u0007decimalq\u0000~\u0000?\u0000\u0000\u0000\u0000q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000\u0089q"
+"\u0000~\u0000{sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0011AppointmentM"
+"onthsq\u0000~\u0000Isq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0084sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000"
+"~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\rFundingMonthsq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0019pps"
+"q\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0084sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)"
+"sq\u0000~\u0000*t\u0000\u0013SummerFundingMonthsq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000s"
+"q\u0000~\u0000\u0007ppq\u0000~\u0000\u0084sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0015Acad"
+"emicFundingMonthsq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsq\u0000~\u0000xq\u0000"
+"~\u0000{t\u0000\fCurrencyTypeq\u0000~\u0000?q\u0000~\u0000\u0088\u0000\u0000\u0000\u0000q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000\u0089q\u0000~\u0000{sq\u0000~\u0000\u0019p"
+"psq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\rRequestedCostq\u0000~\u0000Isq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u00a6sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000"
+"\nFringeCostq\u0000~\u0000Isq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u00a6sq\u0000~\u0000\u0019ppsq"
+"\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\nBaseSalaryq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0019"
+"ppsq\u0000~\u0000\u0000q\u0000~\u0000\u001fp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u00a6sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000"
+"~\u0000)sq\u0000~\u0000*t\u0000\u0014SalaryAndFringeTotalq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u0000q\u0000~\u0000"
+"\u001fp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u00a6sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\r"
+"SalariesTotalq\u0000~\u0000Iq\u0000~\u0000)sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq"
+"\u0000~\u0000*t\u0000\u0010SalariesAndWagesq\u0000~\u0000Tsr\u0000\"com.sun.msv.grammar.Expressi"
+"onPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expressi"
+"onPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$C"
+"losedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom"
+"/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u00001\u0001pq\u0000~\u0000\u0012q\u0000~\u0000\u0014q\u0000~\u0000\tq\u0000~\u0000\u0013"
+"q\u0000~\u0000\u0011q\u0000~\u0000\u000fq\u0000~\u0000nq\u0000~\u0000\u000bq\u0000~\u0000\u001dq\u0000~\u0000\rq\u0000~\u0000\u0017q\u0000~\u0000\u001aq\u0000~\u0000\u000eq\u0000~\u0000lq\u0000~\u0000\u00b4q\u0000~\u0000\u00bb"
+"q\u0000~\u0000\u00c2q\u0000~\u0000\u0010q\u0000~\u0000\nq\u0000~\u0000\u0083q\u0000~\u0000\u0091q\u0000~\u0000\u0098q\u0000~\u0000\u009fq\u0000~\u0000\fq\u0000~\u0000/q\u0000~\u0000hq\u0000~\u0000qq\u0000~\u0000~"
+"q\u0000~\u0000\u008bq\u0000~\u0000\u0092q\u0000~\u0000\u0099q\u0000~\u0000\u00a0q\u0000~\u0000\u00aaq\u0000~\u0000\u00b0q\u0000~\u0000\u00b7q\u0000~\u0000\u00beq\u0000~\u0000vq\u0000~\u0000\u00c5q\u0000~\u0000\u00c9q\u0000~\u0000\u0015"
+"q\u0000~\u0000\u008fq\u0000~\u0000\u0096q\u0000~\u0000\u009dq\u0000~\u0000\u00a5q\u0000~\u0000\u00afq\u0000~\u0000\u00b6q\u0000~\u0000\u00bdq\u0000~\u0000Kq\u0000~\u0000\u00c4x"));
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
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.SalariesAndWagesImpl.this;
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
                        if (("SalariesAndWages" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("Name" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.SalariesAndWagesTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.SalariesAndWagesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                        if (("SalariesAndWages" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
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
