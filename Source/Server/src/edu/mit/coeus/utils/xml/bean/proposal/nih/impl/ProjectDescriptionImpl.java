//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class ProjectDescriptionImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.nih.ProjectDescription, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ProjectDescription.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "ProjectDescription";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace", "ProjectDescription");
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
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ProjectDescription.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.gramm"
+"ar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsr\u0000 com.sun."
+"msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.U"
+"naryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080"
+"\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0019psr\u00002com.sun.ms"
+"v.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000"
+"~\u0000\u0018\u0001q\u0000~\u0000\u001dsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001d"
+"com.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.gr"
+"ammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001eq\u0000~\u0000"
+"#sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalN"
+"amet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000%xq\u0000~\u0000 t\u00006edu.mit"
+".coeus.utils.xml.bean.proposal.nih.HumanSubjectt\u0000+http://jav"
+"a.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000s"
+"q\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000;edu.m"
+"it.coeus.utils.xml.bean.proposal.nih.HumanSubjectsTypeq\u0000~\u0000(s"
+"q\u0000~\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004nam"
+"et\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.dat"
+"atype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.B"
+"uiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Conc"
+"reteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeIm"
+"pl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000%L\u0000\btypeNameq\u0000~\u0000%L\u0000\nwhiteSpa"
+"cet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http"
+"://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv"
+".grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u0019p"
+"sr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L"
+"\u0000\fnamespaceURIq\u0000~\u0000%xpq\u0000~\u0000>q\u0000~\u0000=sq\u0000~\u0000$t\u0000\u0004typet\u0000)http://www.w3"
+".org/2001/XMLSchema-instanceq\u0000~\u0000#sq\u0000~\u0000$t\u0000\fHumanSubjectt\u0000>htt"
+"p://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespaces"
+"q\u0000~\u0000\u0011ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000"
+"~\u0000#sq\u0000~\u0000$t\u00007edu.mit.coeus.utils.xml.bean.proposal.rar.Animal"
+"Subjectq\u0000~\u0000(sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq"
+"\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000;edu.mit.coeus.utils.xml.be"
+"an.proposal.rar.AnimalSubjectTypeq\u0000~\u0000(sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000"
+"~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\rAnimalSubjectt\u0000Ehttp://era.nih.gov/Pro"
+"jectmgmt/SBIR/CGAP/researchandrelated.namespacesq\u0000~\u0000\u0011ppsq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u00007"
+"edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSurveyq\u0000~\u0000("
+"sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000"
+"~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000;edu.mit.coeus.utils.xml.bean.proposal."
+"rar.ProjectSurveyTypeq\u0000~\u0000(sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000"
+"#sq\u0000~\u0000$t\u0000\rProjectSurveyq\u0000~\u0000_sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t"
+"\u0000>edu.mit.coeus.utils.xml.bean.proposal.rar.DescriptionBlock"
+"Typeq\u0000~\u0000(sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000eProject"
+"Summaryt\u0000\u0000q\u0000~\u0000#sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011p"
+"psq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000{q\u0000~\u0000(sq\u0000~"
+"\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0015FacilitiesDescriptio"
+"nq\u0000~\u0000\u0080q\u0000~\u0000#sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000"
+"~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000{q\u0000~\u0000(sq\u0000~\u0000\u0011pp"
+"sq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0014EquipmentDescriptionq\u0000~\u0000"
+"\u0080q\u0000~\u0000#sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000"
+"~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000{q\u0000~\u0000(sq\u0000~\u0000\u0011ppsq\u0000~\u0000"
+"\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\nReferencesq\u0000~\u0000\u0080q\u0000~\u0000#sq\u0000~\u0000\u0011pps"
+"q\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019"
+"pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000Qedu.mit.coeus.utils.xml.bean.propos"
+"al.nih.ProjectDescriptionType.ActivityTypeTypeq\u0000~\u0000(sq\u0000~\u0000\u0011pps"
+"q\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\fActivityTypeq\u0000~\u0000\u0080q\u0000~\u0000#sq\u0000"
+"~\u0000\u0011ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u00006q\u0000~\u0000Fq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0012ProjectDescriptionq"
+"\u0000~\u0000Ksr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpT"
+"ablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-"
+"com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005c"
+"ountB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Express"
+"ionPool;xp\u0000\u0000\u00007\u0001pq\u0000~\u0000\u0010q\u0000~\u0000\u000bq\u0000~\u0000\u0017q\u0000~\u0000-q\u0000~\u0000Oq\u0000~\u0000Wq\u0000~\u0000cq\u0000~\u0000kq\u0000~\u0000"
+"xq\u0000~\u0000\u0086q\u0000~\u0000\u0092q\u0000~\u0000\u009eq\u0000~\u0000\u00aaq\u0000~\u0000*q\u0000~\u0000Tq\u0000~\u0000hq\u0000~\u0000uq\u0000~\u0000\u0014q\u0000~\u0000,q\u0000~\u0000Nq\u0000~\u0000"
+"Vq\u0000~\u0000bq\u0000~\u0000jq\u0000~\u0000wq\u0000~\u0000\u0085q\u0000~\u0000\u0083q\u0000~\u0000\u0091q\u0000~\u0000\u008fq\u0000~\u0000\u009dq\u0000~\u0000\u009bq\u0000~\u0000\u00a9q\u0000~\u0000\u00a7q\u0000~\u0000"
+"\rq\u0000~\u0000\u000eq\u0000~\u0000\u000fq\u0000~\u0000\tq\u0000~\u0000sq\u0000~\u0000\u0081q\u0000~\u0000\u008dq\u0000~\u0000\u0099q\u0000~\u0000\u00a5q\u0000~\u0000\u0012q\u0000~\u00001q\u0000~\u0000[q\u0000~\u0000"
+"Lq\u0000~\u0000oq\u0000~\u0000`q\u0000~\u0000|q\u0000~\u0000\u0089q\u0000~\u0000\u0095q\u0000~\u0000\fq\u0000~\u0000\u00a1q\u0000~\u0000\u00aeq\u0000~\u0000\u00b2q\u0000~\u0000\nx"));
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
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("HumanSubject" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("HumanSubject" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProjectDescriptionImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("ProjectDescription" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
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
                    case  2 :
                        if (("ProjectDescription" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
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
