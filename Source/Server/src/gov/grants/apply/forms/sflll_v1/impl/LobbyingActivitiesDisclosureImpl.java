//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sflll_v1.impl;

public class LobbyingActivitiesDisclosureImpl
    extends gov.grants.apply.forms.sflll_v1.impl.LobbyingActivitiesDisclosureTypeImpl
    implements gov.grants.apply.forms.sflll_v1.LobbyingActivitiesDisclosure, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sflll_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sflll_v1.LobbyingActivitiesDisclosure.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SFLLL-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "LobbyingActivitiesDisclosure";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sflll_v1.impl.LobbyingActivitiesDisclosureImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SFLLL-V1.0", "LobbyingActivitiesDisclosure");
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
        return (gov.grants.apply.forms.sflll_v1.LobbyingActivitiesDisclosure.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar."
+"DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006"
+"exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr"
+"\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006val"
+"uest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.xsd.DataTypeWi"
+"thValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xs"
+"d.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueChe"
+"ckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl"
+";L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000"
+"\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd."
+"XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\"L\u0000\btypeNameq\u0000~\u0000"
+"\"L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcess"
+"or;xpt\u0000(http://apply.grants.gov/forms/SFLLL-V1.0psr\u00005com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+",com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000"
+"sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlway"
+"sValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"#t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000)\u0001q\u0000~\u0000-t\u0000\u000be"
+"numerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0006t\u0000"
+"\rLoanInsurancet\u0000\tCoopAgreet\u0000\bContractt\u0000\u0004Loant\u0000\u0005Grantt\u0000\rLoanG"
+"uaranteexsr\u00000com.sun.msv.grammar.Expression$NullSetExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002"
+"\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\"L\u0000\fnamespaceURIq\u0000~\u0000\"xpt\u0000\u000estring-derivedq\u0000"
+"~\u0000&sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tname"
+"Classq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p"
+"sq\u0000~\u0000\u0018ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000+q\u0000~\u0000.t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProce"
+"ssor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000(q\u0000~\u0000:sq\u0000~\u0000;q\u0000~\u0000Gq\u0000~\u0000.sr\u0000#com.s"
+"un.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\"L\u0000"
+"\fnamespaceURIq\u0000~\u0000\"xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000c"
+"om.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u0000\u0004sq\u0000~\u0000B\u0001q\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0011FederalActionTypeq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000\u001cq\u0000~\u0000&pq\u0000~\u0000)\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q\u0000~\u00000sq\u0000~\u00001w\f\u0000\u0000\u0000\u0010?@"
+"\u0000\u0000\u0000\u0000\u0000\u0003t\u0000\fInitialAwardt\u0000\bBidOffert\u0000\tPostAwardxq\u0000~\u0000:sq\u0000~\u0000;t\u0000\u000es"
+"tring-derivedq\u0000~\u0000&sq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt"
+"\u0000\u0013FederalActionStatusq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000\u001cq\u0000~"
+"\u0000&t\u0000\u000eReportDataTypeq\u0000~\u0000)\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q\u0000~\u00000sq\u0000~\u00001w\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000"
+"\u0002t\u0000\rInitialFilingt\u0000\u000eMaterialChangexq\u0000~\u0000:sq\u0000~\u0000;q\u0000~\u0000gq\u0000~\u0000&sq\u0000~"
+"\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\nReportTypeq\u0000~\u0000&sq\u0000~\u0000"
+">ppsq\u0000~\u0000\u0000q\u0000~\u0000Cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>ppsr\u0000 com.sun.msv.gra"
+"mmar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000Cpsq\u0000~\u0000@q\u0000~\u0000Cpsr\u00002com.sun.ms"
+"v.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~"
+"\u0000Rq\u0000~\u0000zsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000L"
+"q\u0000~\u0000Qsq\u0000~\u0000Kt\u0000]gov.grants.apply.forms.sflll_v1.LobbyingActivi"
+"tiesDisclosureType.MaterialChangeSupplementTypet\u0000+http://jav"
+"a.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000"
+"~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0018MaterialChangeSupplementq\u0000~\u0000&q\u0000~\u0000Qsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>ppsq\u0000~\u0000uq\u0000~\u0000Cpsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000zq\u0000~\u0000|q"
+"\u0000~\u0000Qsq\u0000~\u0000Kt\u0000Qgov.grants.apply.forms.sflll_v1.LobbyingActivit"
+"iesDisclosureType.ReportEntityTypeq\u0000~\u0000\u007fsq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq"
+"\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\fReportEntityq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0018ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I"
+"\u0000\tmaxLengthxq\u0000~\u0000\u001eq\u0000~\u0000&pq\u0000~\u0000)\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.M"
+"inLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u001eq\u0000~\u0000&pq\u0000~\u0000)\u0000\u0000q\u0000~\u0000-"
+"q\u0000~\u0000-t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000-t\u0000\tmaxLength\u0000\u0000\u0000(q\u0000~\u0000:sq\u0000~\u0000;t\u0000\u000estri"
+"ng-derivedq\u0000~\u0000&sq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0017F"
+"ederalAgencyDepartmentq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>p"
+"psq\u0000~\u0000uq\u0000~\u0000Cpsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000zq\u0000~\u0000|q\u0000~\u0000Qsq\u0000~\u0000Kt\u0000Wgov.grants."
+"apply.forms.sflll_v1.LobbyingActivitiesDisclosureType.Federa"
+"lProgramNameTypeq\u0000~\u0000\u007fsq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~"
+"\u0000Kt\u0000\u0012FederalProgramNameq\u0000~\u0000&sq\u0000~\u0000>ppsq\u0000~\u0000\u0000q\u0000~\u0000Cp\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0018ppsq\u0000~\u0000\u0093t\u0000*http://apply.grants.gov/system/Global-V1.0t\u0000\u0014S"
+"tringMin1Max110Typeq\u0000~\u0000)\u0000\u0001sq\u0000~\u0000\u0095q\u0000~\u0000\u00b0q\u0000~\u0000\u00b1q\u0000~\u0000)\u0000\u0000q\u0000~\u0000-q\u0000~\u0000-q"
+"\u0000~\u0000\u0097\u0000\u0000\u0000\u0001q\u0000~\u0000-q\u0000~\u0000\u0098\u0000\u0000\u0000nq\u0000~\u0000:sq\u0000~\u0000;q\u0000~\u0000\u00b1q\u0000~\u0000\u00b0sq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~"
+"\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0013FederalActionNumberq\u0000~\u0000&q\u0000~\u0000Qsq\u0000~"
+"\u0000>ppsq\u0000~\u0000\u0000q\u0000~\u0000Cp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsr\u0000,com.sun.msv.datatype.xs"
+"d.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.data"
+"type.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u001f"
+"q\u0000~\u0000\u00b0t\u0000\u001bDecimalMin1Max15Places2Typeq\u0000~\u0000I\u0000\u0000sr\u0000)com.sun.msv.da"
+"tatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tprecisionxq\u0000~\u0000\u00bdq\u0000~\u0000"
+"\u00b0q\u0000~\u0000\u00bfq\u0000~\u0000I\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000+q\u0000~\u0000.t\u0000\u0007decimalq\u0000~\u0000Iq\u0000~\u0000\u00c3t\u0000\u000btotalDigits\u0000\u0000\u0000\u000fq\u0000~\u0000\u00c3t\u0000\u000e"
+"fractionDigits\u0000\u0000\u0000\u0002q\u0000~\u0000:sq\u0000~\u0000;q\u0000~\u0000\u00bfq\u0000~\u0000\u00b0sq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq"
+"\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u000bAwardAmountq\u0000~\u0000&q\u0000~\u0000Qsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007p"
+"psq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>ppsq\u0000~\u0000uq\u0000~\u0000Cpsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000zq\u0000~\u0000|q\u0000~\u0000Qsq\u0000"
+"~\u0000Kt\u0000Wgov.grants.apply.forms.sflll_v1.LobbyingActivitiesDisc"
+"losureType.LobbyingRegistrantTypeq\u0000~\u0000\u007fsq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000"
+"~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0012LobbyingRegistrantq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>ppsq\u0000~\u0000uq\u0000~\u0000Cpsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000zq\u0000~\u0000|q\u0000~\u0000Qsq"
+"\u0000~\u0000Kt\u0000bgov.grants.apply.forms.sflll_v1.LobbyingActivitiesDis"
+"closureType.IndividualsPerformingServicesTypeq\u0000~\u0000\u007fsq\u0000~\u0000>ppsq"
+"\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u001dIndividualsPerformingServi"
+"cesq\u0000~\u0000&sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000>ppsq\u0000~\u0000uq\u0000~\u0000Cpsq\u0000~\u0000@"
+"q\u0000~\u0000Cpq\u0000~\u0000zq\u0000~\u0000|q\u0000~\u0000Qsq\u0000~\u0000Kt\u0000Sgov.grants.apply.forms.sflll_v"
+"1.LobbyingActivitiesDisclosureType.SignatureBlockTypeq\u0000~\u0000\u007fsq"
+"\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u000eSignatureBlockq\u0000~\u0000"
+"&sq\u0000~\u0000@ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~"
+"\u0000\u0019L\u0000\u0004nameq\u0000~\u0000\u001aL\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0093q\u0000~"
+"\u0000\u00b0t\u0000\u0013StringMin1Max30Typeq\u0000~\u0000)\u0000\u0001sq\u0000~\u0000\u0095q\u0000~\u0000\u00b0q\u0000~\u0000\u00f5q\u0000~\u0000)\u0000\u0000q\u0000~\u0000-q"
+"\u0000~\u0000-q\u0000~\u0000\u0097\u0000\u0000\u0000\u0001q\u0000~\u0000-q\u0000~\u0000\u0098\u0000\u0000\u0000\u001esq\u0000~\u0000;q\u0000~\u0000\u00f5q\u0000~\u0000\u00b0t\u0000\u00031.0sq\u0000~\u0000Kt\u0000\u000bFo"
+"rmVersiont\u00001http://apply.grants.gov/system/GlobalLibrary-V1."
+"0sq\u0000~\u0000>ppsq\u0000~\u0000@q\u0000~\u0000Cpq\u0000~\u0000Dq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u001cLobbyingActivit"
+"iesDisclosureq\u0000~\u0000&sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$Clo"
+"sedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7"
+"j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/g"
+"rammar/ExpressionPool;xp\u0000\u0000\u00005\u0001pq\u0000~\u0000rq\u0000~\u0000\u0085q\u0000~\u0000\u00a0q\u0000~\u0000\u00cdq\u0000~\u0000\u00d9q\u0000~\u0000\u00e5"
+"q\u0000~\u0000\u0015q\u0000~\u0000\tq\u0000~\u0000\u0014q\u0000~\u0000\u0010q\u0000~\u0000\u0017q\u0000~\u0000\u000fq\u0000~\u0000\u00b8q\u0000~\u0000pq\u0000~\u0000Vq\u0000~\u0000\u00baq\u0000~\u0000\rq\u0000~\u0000\n"
+"q\u0000~\u0000tq\u0000~\u0000\u0087q\u0000~\u0000\u00a2q\u0000~\u0000\u00cfq\u0000~\u0000?q\u0000~\u0000_q\u0000~\u0000lq\u0000~\u0000\u0080q\u0000~\u0000\u008cq\u0000~\u0000\u009bq\u0000~\u0000\u0011q\u0000~\u0000\u00a7"
+"q\u0000~\u0000\u00b4q\u0000~\u0000\u00c8q\u0000~\u0000\u000eq\u0000~\u0000\u00d4q\u0000~\u0000\u00dbq\u0000~\u0000\u00e0q\u0000~\u0000\u00e7q\u0000~\u0000\u00ecq\u0000~\u0000\u00fcq\u0000~\u0000\u00adq\u0000~\u0000\u0012q\u0000~\u0000\u0091"
+"q\u0000~\u0000wq\u0000~\u0000\u0088q\u0000~\u0000\u00a3q\u0000~\u0000\u00d0q\u0000~\u0000\u00dcq\u0000~\u0000\u00e8q\u0000~\u0000\u0013q\u0000~\u0000\u000bq\u0000~\u0000dq\u0000~\u0000\u00abq\u0000~\u0000\fx"));
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
            return gov.grants.apply.forms.sflll_v1.impl.LobbyingActivitiesDisclosureImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/system/GlobalLibrary-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("LobbyingActivitiesDisclosure" == ___local)&&("http://apply.grants.gov/forms/SFLLL-V1.0" == ___uri)) {
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
                        attIdx = context.getAttribute("http://apply.grants.gov/system/GlobalLibrary-V1.0", "FormVersion");
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
                        if (("LobbyingActivitiesDisclosure" == ___local)&&("http://apply.grants.gov/forms/SFLLL-V1.0" == ___uri)) {
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sflll_v1.impl.LobbyingActivitiesDisclosureTypeImpl)gov.grants.apply.forms.sflll_v1.impl.LobbyingActivitiesDisclosureImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/system/GlobalLibrary-V1.0", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/system/GlobalLibrary-V1.0", "FormVersion");
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
