//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class ProjectSurveyImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSurvey, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSurvey.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "ProjectSurvey";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace", "ProjectSurvey");
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
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSurvey.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.gramm"
+"ar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd "
+"r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006except"
+"q\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000$com."
+"sun.msv.datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.d"
+"atatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.data"
+"type.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd"
+".XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/Strin"
+"g;L\u0000\btypeNameq\u0000~\u0000.L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/"
+"WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007"
+"booleansr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Coll"
+"apse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProces"
+"sor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000$psr\u0000\u001bcom.sun.msv.util.StringPa"
+"ir\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000.L\u0000\fnamespaceURIq\u0000~\u0000.xpq\u0000~\u00002q\u0000~"
+"\u00001sq\u0000~\u0000 ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003"
+"expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000$psq\u0000~\u0000&ppsr\u0000\"com.sun.msv"
+".datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000+q\u0000~\u00001t\u0000\u0005QNameq\u0000~\u00005q\u0000"
+"~\u00007sq\u0000~\u00008q\u0000~\u0000@q\u0000~\u00001sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000.L\u0000\fnamespaceURIq\u0000~\u0000.xr\u0000\u001dcom.sun.ms"
+"v.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/"
+"2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Ep"
+"silonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000#\u0001q\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nCBQuest"
+"iont\u0000\u0000q\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000&ppsr\u0000\"com.sun."
+"msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatyp"
+"e.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000+q\u0000~\u00001t\u0000\u0005tok"
+"enq\u0000~\u00005\u0001q\u0000~\u00007sq\u0000~\u00008q\u0000~\u0000Tq\u0000~\u00001sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq"
+"\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006CBTextq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000"
+"~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nG3Questionq\u0000~"
+"\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$"
+"pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006G3Textq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$"
+"p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nG"
+"4Questionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Psq\u0000~\u0000 p"
+"psq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006G4Textq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 p"
+"psq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~"
+"\u0000Hsq\u0000~\u0000Bt\u0000\nG6Questionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006G6Textq\u0000~\u0000L"
+"q\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq"
+"\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nG8Questionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~"
+"\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000"
+"\u0006G8Textq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 pps"
+"q\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0011EnvImpactQuestionq\u0000~\u0000Lq\u0000~"
+"\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000"
+"=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\rEnvImpactTextq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~"
+"\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000"
+"\u0014EnvExemptionQuestionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0012EnvExemptio"
+"nCBTextq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 pps"
+"q\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nH1Questionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000"
+" ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq"
+"\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006H1Textq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000"
+"~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\nH4Questionq\u0000~"
+"\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$"
+"pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0012SmallGrantQuestionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 p"
+"psq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~"
+"\u0000Hsq\u0000~\u0000Bt\u0000\u001aNSFbeginningInvestQuestionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000"
+"\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~"
+"\u0000Bt\u0000\u0010StemCellQuestionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000Psq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\fStemCellTex"
+"tq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q"
+"\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0015ClinicalTrialQuestionq\u0000~\u0000Lq\u0000~\u0000H"
+"sq\u0000~\u0000 ppsq\u0000~\u0000\u0000q\u0000~\u0000$p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000)sq\u0000~\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q"
+"\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u001cDisclosurePermissionQuestionq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~"
+"\u0000 ppsq\u0000~\u0000;q\u0000~\u0000$pq\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\rProjectSurveyt\u0000Ehttp"
+"://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.name"
+"spacesr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexp"
+"Tablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000"
+"-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005"
+"countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expres"
+"sionPool;xp\u0000\u0000\u0000]\u0001pq\u0000~\u0000\u0013q\u0000~\u0000\u00b0q\u0000~\u0000\u00a2q\u0000~\u0000\u0094q\u0000~\u0000\u0086q\u0000~\u0000xq\u0000~\u0000jq\u0000~\u0000\\q\u0000~"
+"\u0000%q\u0000~\u0000\u00beq\u0000~\u0000\u00c5q\u0000~\u0000\u00ccq\u0000~\u0000\u00d3q\u0000~\u0000\u00e1q\u0000~\u0000\u00e8q\u0000~\u0000\u0015q\u0000~\u0000\u000fq\u0000~\u0000\rq\u0000~\u0000\u00a9q\u0000~\u0000\u009bq\u0000~"
+"\u0000\u008dq\u0000~\u0000\u007fq\u0000~\u0000qq\u0000~\u0000cq\u0000~\u0000Oq\u0000~\u0000\u00b7q\u0000~\u0000\u00daq\u0000~\u0000\u0012q\u0000~\u0000\u0014q\u0000~\u0000\u0017q\u0000~\u0000\u0016q\u0000~\u0000\u0019q\u0000~"
+"\u0000\u001fq\u0000~\u0000\u000eq\u0000~\u0000\u001bq\u0000~\u0000\u001eq\u0000~\u0000\nq\u0000~\u0000\u001dq\u0000~\u0000\u001aq\u0000~\u0000\u001cq\u0000~\u0000\u00b1q\u0000~\u0000\u00aaq\u0000~\u0000\u00a3q\u0000~\u0000\u009cq\u0000~"
+"\u0000\u0095q\u0000~\u0000\u008eq\u0000~\u0000\u0087q\u0000~\u0000\u0080q\u0000~\u0000yq\u0000~\u0000rq\u0000~\u0000kq\u0000~\u0000dq\u0000~\u0000]q\u0000~\u0000Vq\u0000~\u0000:q\u0000~\u0000\u00a7q\u0000~"
+"\u0000\u0099q\u0000~\u0000\u008bq\u0000~\u0000}q\u0000~\u0000oq\u0000~\u0000aq\u0000~\u0000Mq\u0000~\u0000\u00a0q\u0000~\u0000\u0092q\u0000~\u0000\u0084q\u0000~\u0000vq\u0000~\u0000hq\u0000~\u0000Zq\u0000~"
+"\u0000!q\u0000~\u0000\u00aeq\u0000~\u0000\u00b8q\u0000~\u0000\u00b5q\u0000~\u0000\u00bfq\u0000~\u0000\u00bcq\u0000~\u0000\u00c6q\u0000~\u0000\u0010q\u0000~\u0000\u00c3q\u0000~\u0000\u00cdq\u0000~\u0000\u00caq\u0000~\u0000\u00d4q\u0000~"
+"\u0000\u00d1q\u0000~\u0000\u00dbq\u0000~\u0000\u00d8q\u0000~\u0000\u00e2q\u0000~\u0000\u00dfq\u0000~\u0000\u00e9q\u0000~\u0000\u00e6q\u0000~\u0000\u00edq\u0000~\u0000\u0018q\u0000~\u0000\fq\u0000~\u0000\u0011q\u0000~\u0000\u000bq\u0000~"
+"\u0000\tx"));
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
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this;
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
                        if (("CBQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("CBText" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G3Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G3Text" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G4Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G4Text" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G6Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G6Text" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G8Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("G8Text" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("EnvImpactQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("EnvImpactText" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("EnvExemptionQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("EnvExemptionCBText" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("H1Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("H1Text" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("H4Question" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("SmallGrantQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("NSFbeginningInvestQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("StemCellQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("StemCellText" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ClinicalTrialQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DisclosurePermissionQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("ProjectSurvey" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
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
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("ProjectSurvey" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
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
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case  1 :
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        case  1 :
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSurveyImpl.this).new Unmarshaller(context)), 2, value);
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
