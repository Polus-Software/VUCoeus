//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.nsf_applicationchecklist_v1.impl;

public class NSFApplicationChecklistImpl
    extends gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.NSFApplicationChecklistTypeImpl
    implements gov.grants.apply.forms.nsf_applicationchecklist_v1.NSFApplicationChecklist, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.nsf_applicationchecklist_v1.NSFApplicationChecklist.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "NSF_ApplicationChecklist";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.NSFApplicationChecklistImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0", "NSF_ApplicationChecklist");
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
        return (gov.grants.apply.forms.nsf_applicationchecklist_v1.NSFApplicationChecklist.class);
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
+"/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000+xq\u0000~\u0000&t\u0000]gov.grants.apply.f"
+"orms.nsf_applicationchecklist_v1.NSFApplicationChecklistType"
+".CoverSheetTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-element"
+"ssq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004n"
+"amet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd"
+".BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Co"
+"ncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatype"
+"Impl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000+L\u0000\btypeNameq\u0000~\u0000+L\u0000\nwhiteS"
+"pacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 ht"
+"tp://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.ms"
+"v.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.m"
+"sv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr"
+"\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000+L\u0000\f"
+"namespaceURIq\u0000~\u0000+xpq\u0000~\u0000<q\u0000~\u0000;sq\u0000~\u0000*t\u0000\u0004typet\u0000)http://www.w3.o"
+"rg/2001/XMLSchema-instanceq\u0000~\u0000)sq\u0000~\u0000*t\u0000\nCoverSheett\u0000;http://"
+"apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0sq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u00001ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFac"
+"et\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.data"
+"type.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com."
+"sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetF"
+"ixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype"
+"/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/"
+"xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000+xq\u0000~\u00008t\u00001http://apply.gran"
+"ts.gov/system/GlobalLibrary-V1.0t\u0000\rYesNoDataTypesr\u00005com.sun."
+"msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000>\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risA"
+"lwaysValidxq\u0000~\u00006q\u0000~\u0000;t\u0000\u0006stringq\u0000~\u0000W\u0001q\u0000~\u0000Yt\u0000\u000benumerationsr\u0000\u0011j"
+"ava.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0003Yest\u0000\u0002Noxq\u0000~\u0000A"
+"sq\u0000~\u0000Bq\u0000~\u0000Uq\u0000~\u0000Tsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000b"
+"CheckRRSiteq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Lsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000"
+"~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0010CheckRROtherInfoq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007pp"
+"q\u0000~\u0000Lsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0013CheckProjec"
+"tSummaryq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001fps"
+"q\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u0000#q\u0000~\u0000\'q\u0000~\u0000)sq\u0000~\u0000*t\u0000cgov.grants.apply.forms.ns"
+"f_applicationchecklist_v1.NSFApplicationChecklistType.Projec"
+"tNarrativeTypeq\u0000~\u0000.sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*"
+"t\u0000\u0010ProjectNarrativeq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Lsq\u0000~\u0000\u0019ppsq\u0000~\u0000 "
+"q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000bCheckBiblioq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppq\u0000~\u0000Lsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000fCheckFac"
+"ilitiesq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001fpsq"
+"\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u0000#q\u0000~\u0000\'q\u0000~\u0000)sq\u0000~\u0000*t\u0000\\gov.grants.apply.forms.nsf"
+"_applicationchecklist_v1.NSFApplicationChecklistType.Equipme"
+"ntTypeq\u0000~\u0000.sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\tEquip"
+"mentq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001fpsq\u0000~\u0000"
+" q\u0000~\u0000\u001fpq\u0000~\u0000#q\u0000~\u0000\'q\u0000~\u0000)sq\u0000~\u0000*t\u0000^gov.grants.apply.forms.nsf_ap"
+"plicationchecklist_v1.NSFApplicationChecklistType.RRSrProfil"
+"eTypeq\u0000~\u0000.sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000bRRSrPr"
+"ofileq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Lsq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~"
+"\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0013CheckRRPersonalDataq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001fpsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u0000#q\u0000~\u0000\'q\u0000~\u0000)sq\u0000~\u0000*t"
+"\u0000[gov.grants.apply.forms.nsf_applicationchecklist_v1.NSFAppl"
+"icationChecklistType.RRBudgetTypeq\u0000~\u0000.sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000"
+"~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\bRRBudgetq\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000\u0019ppsq\u0000~\u0000\u001bq\u0000~\u0000\u001fpsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u0000#q\u0000~\u0000\'q\u0000~\u0000)sq\u0000~\u0000*t\u0000[gov"
+".grants.apply.forms.nsf_applicationchecklist_v1.NSFApplicati"
+"onChecklistType.NSFCoverTypeq\u0000~\u0000.sq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000"
+"~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\bNSFCoverq\u0000~\u0000Isq\u0000~\u0000 ppsr\u0000\u001ccom.sun.msv.gramma"
+"r.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u00002L\u0000\u0004nameq\u0000~\u00003L\u0000\u0005valuet\u0000\u0012Ljava/"
+"lang/Object;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~\u0000Oq\u0000~\u0000Tt\u0000\u0013FormVersionDataTypeq"
+"\u0000~\u0000W\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"I\u0000\tminLengthxq\u0000~\u0000Oq\u0000~\u0000Tq\u0000~\u0000\u00c5q\u0000~\u0000W\u0000\u0000q\u0000~\u0000Yq\u0000~\u0000Yt\u0000\tminLength\u0000\u0000\u0000"
+"\u0001q\u0000~\u0000Yt\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u0000Bq\u0000~\u0000\u00c5q\u0000~\u0000Tt\u0000\u00031.0sq\u0000~\u0000*t\u0000\u000bFormVer"
+"sionq\u0000~\u0000Isq\u0000~\u0000\u0019ppsq\u0000~\u0000 q\u0000~\u0000\u001fpq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000)sq\u0000~\u0000*t\u0000\u0018NSF_App"
+"licationChecklistq\u0000~\u0000Isr\u0000\"com.sun.msv.grammar.ExpressionPool"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool"
+"$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedH"
+"ash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/m"
+"sv/grammar/ExpressionPool;xp\u0000\u0000\u00002\u0001pq\u0000~\u0000\rq\u0000~\u0000\u000fq\u0000~\u0000Kq\u0000~\u0000fq\u0000~\u0000\u0017q"
+"\u0000~\u0000\u0014q\u0000~\u0000lq\u0000~\u0000rq\u0000~\u0000~q\u0000~\u0000\u0084q\u0000~\u0000\u008aq\u0000~\u0000\u0096q\u0000~\u0000\u00a2q\u0000~\u0000\u00a8q\u0000~\u0000\u00b4q\u0000~\u0000\u0013q\u0000~\u0000\u0011q"
+"\u0000~\u0000\fq\u0000~\u0000\u000bq\u0000~\u0000\u001aq\u0000~\u0000tq\u0000~\u0000\u008cq\u0000~\u0000\u0098q\u0000~\u0000/q\u0000~\u0000aq\u0000~\u0000gq\u0000~\u0000mq\u0000~\u0000yq\u0000~\u0000\u007fq"
+"\u0000~\u0000\u0085q\u0000~\u0000\u0091q\u0000~\u0000\u009dq\u0000~\u0000\u00a3q\u0000~\u0000\u00aaq\u0000~\u0000\u00afq\u0000~\u0000\u00b6q\u0000~\u0000\u00bbq\u0000~\u0000\nq\u0000~\u0000\u00ceq\u0000~\u0000\tq\u0000~\u0000\u0012q"
+"\u0000~\u0000\u000eq\u0000~\u0000\u0010q\u0000~\u0000\u001dq\u0000~\u0000uq\u0000~\u0000\u008dq\u0000~\u0000\u0015q\u0000~\u0000\u0099q\u0000~\u0000\u00abq\u0000~\u0000\u00b7x"));
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
            return gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.NSFApplicationChecklistImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("NSF_ApplicationChecklist" == ___local)&&("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0" == ___uri)) {
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
                    case  2 :
                        if (("NSF_ApplicationChecklist" == ___local)&&("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0", "FormVersion");
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.NSFApplicationChecklistTypeImpl)gov.grants.apply.forms.nsf_applicationchecklist_v1.impl.NSFApplicationChecklistImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_ApplicationChecklist-V1.0", "FormVersion");
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
