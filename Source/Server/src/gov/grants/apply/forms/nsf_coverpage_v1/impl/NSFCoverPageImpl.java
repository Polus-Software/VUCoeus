//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.nsf_coverpage_v1.impl;

public class NSFCoverPageImpl
    extends gov.grants.apply.forms.nsf_coverpage_v1.impl.NSFCoverPageTypeImpl
    implements gov.grants.apply.forms.nsf_coverpage_v1.NSFCoverPage, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.nsf_coverpage_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.nsf_coverpage_v1.NSFCoverPage.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/NSF_CoverPage-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "NSF_CoverPage";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.nsf_coverpage_v1.impl.NSFCoverPageImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/NSF_CoverPage-V1.0", "NSF_CoverPage");
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
        return (gov.grants.apply.forms.nsf_coverpage_v1.NSFCoverPage.class);
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
+"tringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MinLengthFace"
+"t\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxr\u00009com.sun.msv.datatype.xsd.DataTyp"
+"eWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype"
+".xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValue"
+"CheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeI"
+"mpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType"
+";L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.x"
+"sd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001cL\u0000\btypeNameq"
+"\u0000~\u0000\u001cL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProc"
+"essor;xpt\u00000http://apply.grants.gov/forms/NSF_CoverPage-V1.0p"
+"sr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~\u0000\u0018t\u00001http://apply.grants.gov/system/Glo"
+"balLibrary-V1.0t\u0000\u0015OpportunityIDDataTypeq\u0000~\u0000#\u0000\u0001sq\u0000~\u0000\u0017q\u0000~\u0000&q\u0000~"
+"\u0000\'q\u0000~\u0000#\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z"
+"\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000#\u0001"
+"q\u0000~\u0000,t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000,t\u0000\tmaxLength\u0000\u0000\u0000dq\u0000~\u0000,q\u0000~\u0000/\u0000\u0000\u0000\u0001sr\u00000"
+"com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNam"
+"eq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxpt\u0000\u000estring-derivedq\u0000~\u0000 sr\u0000\u001dcom.su"
+"n.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.gr"
+"ammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000"
+"~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0013ppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000*q\u0000~\u0000-t\u0000\u0005QN"
+"amesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"q\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000?q\u0000~\u0000-sr\u0000#com.sun.msv.gramma"
+"r.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fnamespaceURI"
+"q\u0000~\u0000\u001cxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000"
+")http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gr"
+"ammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000:\u0001q\u0000"
+"~\u0000Isq\u0000~\u0000Ct\u0000\u0018FundingOpportunityNumberq\u0000~\u0000 sq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u0000;"
+"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013ppsr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002"
+"\u0000\u0000xq\u0000~\u0000*q\u0000~\u0000-t\u0000\u0004dateq\u0000~\u0000Aq\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000Tq\u0000~\u0000-sq\u0000~\u00006ppsq\u0000~\u00008"
+"q\u0000~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000\u0007DueDateq\u0000~\u0000 q\u0000~\u0000Isq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00006ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~"
+"\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000;psq\u0000~\u00008q\u0000~\u0000;psr\u00002com.sun.msv.grammar.Expression"
+"$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000Jq\u0000~\u0000csr\u0000 com.sun.m"
+"sv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Dq\u0000~\u0000Isq\u0000~\u0000Ct\u0000Qgov.gr"
+"ants.apply.forms.nsf_coverpage_v1.NSFCoverPageType.NSFUnitCo"
+"nsiderationTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-element"
+"ssq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000\u0014NSFUnitConsider"
+"ationq\u0000~\u0000 sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00006ppsq\u0000~\u0000^q\u0000~\u0000;psq\u0000~"
+"\u00008q\u0000~\u0000;pq\u0000~\u0000cq\u0000~\u0000eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000Cgov.grants.apply.forms.nsf_c"
+"overpage_v1.NSFCoverPageType.PIInfoTypeq\u0000~\u0000hsq\u0000~\u00006ppsq\u0000~\u00008q\u0000"
+"~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000\u0006PIInfoq\u0000~\u0000 sq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u0000;p\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00006ppsq\u0000~\u0000^q\u0000~\u0000;psq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000cq\u0000~\u0000eq"
+"\u0000~\u0000Isq\u0000~\u0000Ct\u0000Egov.grants.apply.forms.nsf_coverpage_v1.NSFCove"
+"rPageType.CoPIInfoTypeq\u0000~\u0000hsq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~"
+"\u0000Isq\u0000~\u0000Ct\u0000\bCoPIInfoq\u0000~\u0000 q\u0000~\u0000Isq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u0000;p\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u00006ppsq\u0000~\u0000^q\u0000~\u0000;psq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000cq\u0000~\u0000eq\u0000~\u0000Isq\u0000~\u0000C"
+"t\u0000Fgov.grants.apply.forms.nsf_coverpage_v1.NSFCoverPageType."
+"OtherInfoTypeq\u0000~\u0000hsq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct"
+"\u0000\tOtherInfoq\u0000~\u0000 q\u0000~\u0000Isq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u0000;p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000s"
+"q\u0000~\u00006ppsq\u0000~\u0000^q\u0000~\u0000;psq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000cq\u0000~\u0000eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000Hgov.g"
+"rants.apply.system.attachments_v1.AttachmentGroupMin1Max100D"
+"ataTypeq\u0000~\u0000hsq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u0000;pq\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000\u0014Sing"
+"le-CopyDocumentsq\u0000~\u0000 q\u0000~\u0000Isq\u0000~\u00008ppsr\u0000\u001ccom.sun.msv.grammar.Va"
+"lueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0014L\u0000\u0004nameq\u0000~\u0000\u0015L\u0000\u0005valuet\u0000\u0012Ljava/lang"
+"/Object;xq\u0000~\u0000\u0004ppsq\u0000~\u0000$q\u0000~\u0000&t\u0000\u0013FormVersionDataTypeq\u0000~\u0000#\u0000\u0001sq\u0000~"
+"\u0000\u0017q\u0000~\u0000&q\u0000~\u0000\u00a5q\u0000~\u0000#\u0000\u0000q\u0000~\u0000,q\u0000~\u0000,q\u0000~\u0000/\u0000\u0000\u0000\u0001q\u0000~\u0000,q\u0000~\u00000\u0000\u0000\u0000\u001esq\u0000~\u00003q\u0000"
+"~\u0000\u00a5q\u0000~\u0000&t\u0000\u00031.0sq\u0000~\u0000Ct\u0000\u000bFormVersionq\u0000~\u0000 sq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u0000;pq"
+"\u0000~\u0000<q\u0000~\u0000Eq\u0000~\u0000Isq\u0000~\u0000Ct\u0000\rNSF_CoverPageq\u0000~\u0000 sr\u0000\"com.sun.msv.gra"
+"mmar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gra"
+"mmar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Exp"
+"ressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006"
+"parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000%\u0001pq\u0000~\u0000[q\u0000"
+"~\u0000nq\u0000~\u0000{q\u0000~\u0000\u0088q\u0000~\u0000\u0010q\u0000~\u0000\u0095q\u0000~\u0000\u000eq\u0000~\u0000\u0012q\u0000~\u0000Mq\u0000~\u0000yq\u0000~\u0000\u0086q\u0000~\u0000\u0093q\u0000~\u0000\rq\u0000"
+"~\u0000\fq\u0000~\u0000]q\u0000~\u0000pq\u0000~\u0000}q\u0000~\u0000\u008aq\u0000~\u00007q\u0000~\u0000Vq\u0000~\u0000iq\u0000~\u0000uq\u0000~\u0000\u0082q\u0000~\u0000\u008fq\u0000~\u0000\u0097q\u0000"
+"~\u0000\u009cq\u0000~\u0000\u00abq\u0000~\u0000\nq\u0000~\u0000Oq\u0000~\u0000\tq\u0000~\u0000\u000bq\u0000~\u0000`q\u0000~\u0000qq\u0000~\u0000~q\u0000~\u0000\u008bq\u0000~\u0000\u0098q\u0000~\u0000\u000fx"));
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
            return gov.grants.apply.forms.nsf_coverpage_v1.impl.NSFCoverPageImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("NSF_CoverPage" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1.0" == ___uri)) {
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
                        if (("NSF_CoverPage" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1.0", "FormVersion");
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/NSF_CoverPage-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.nsf_coverpage_v1.impl.NSFCoverPageTypeImpl)gov.grants.apply.forms.nsf_coverpage_v1.impl.NSFCoverPageImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1.0", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/NSF_CoverPage-V1.0", "FormVersion");
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
