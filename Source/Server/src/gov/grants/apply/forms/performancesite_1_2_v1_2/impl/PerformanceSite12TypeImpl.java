//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.performancesite_1_2_v1_2.impl;

public class PerformanceSite12TypeImpl implements gov.grants.apply.forms.performancesite_1_2_v1_2.PerformanceSite12Type, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.performancesite_1_2_v1_2.SiteLocationDataType _PrimarySite;
    protected java.lang.String _FormVersion;
    protected com.sun.xml.bind.util.ListImpl _OtherSite;
    public final static java.lang.Class version = (gov.grants.apply.forms.performancesite_1_2_v1_2.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.performancesite_1_2_v1_2.PerformanceSite12Type.class);
    }

    public gov.grants.apply.forms.performancesite_1_2_v1_2.SiteLocationDataType getPrimarySite() {
        return _PrimarySite;
    }

    public void setPrimarySite(gov.grants.apply.forms.performancesite_1_2_v1_2.SiteLocationDataType value) {
        _PrimarySite = value;
    }

    public java.lang.String getFormVersion() {
        return _FormVersion;
    }

    public void setFormVersion(java.lang.String value) {
        _FormVersion = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getOtherSite() {
        if (_OtherSite == null) {
            _OtherSite = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _OtherSite;
    }

    public java.util.List getOtherSite() {
        return _getOtherSite();
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.performancesite_1_2_v1_2.impl.PerformanceSite12TypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "PrimarySite");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endElement();
        while (idx3 != len3) {
            context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "OtherSite");
            int idx_2 = idx3;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx_2 ++)), "OtherSite");
            context.endNamespaceDecls();
            int idx_3 = idx3;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx_3 ++)), "OtherSite");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx3 ++)), "OtherSite");
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.startAttribute("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "FormVersion");
        try {
            context.text(((java.lang.String) _FormVersion), "FormVersion");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        while (idx3 != len3) {
            idx3 += 1;
        }
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", null, true);
        while (idx3 != len3) {
            idx3 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.performancesite_1_2_v1_2.PerformanceSite12Type.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\u0007pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.m"
+"sv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang"
+".Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attri"
+"buteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\bxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psr"
+"\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u0000\u0017sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003q\u0000~\u0000\u0018q\u0000~\u0000\u001dsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001fxq\u0000~"
+"\u0000\u001at\u0000Dgov.grants.apply.forms.performancesite_1_2_v1_2.SiteLoc"
+"ationDataTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementss"
+"q\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004nam"
+"et\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.dat"
+"atype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.B"
+"uiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Conc"
+"reteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeIm"
+"pl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001fL\u0000\btypeNameq\u0000~\u0000\u001fL\u0000\nwhiteSpa"
+"cet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http"
+"://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv"
+".grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001b"
+"com.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fna"
+"mespaceURIq\u0000~\u0000\u001fxpq\u0000~\u00000q\u0000~\u0000/sq\u0000~\u0000\u001et\u0000\u0004typet\u0000)http://www.w3.org"
+"/2001/XMLSchema-instanceq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u000bPrimarySitet\u00006http://a"
+"pply.grants.gov/forms/PerformanceSite_1_2-V1-2sq\u0000~\u0000\rppsq\u0000~\u0000\u0000"
+"q\u0000~\u0000\u0013psq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~"
+"\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq"
+"\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\tOtherSiteq\u0000~\u0000=sq\u0000~\u0000\rppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013psq\u0000"
+"~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq"
+"\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008"
+"q\u0000~\u0000\u001dq\u0000~\u0000Isq\u0000~\u0000\rppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013psq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000"
+"sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000"
+"~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dq\u0000~\u0000Isq\u0000~\u0000\rppsq\u0000~\u0000\u0000q\u0000~"
+"\u0000\u0013psq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q"
+"\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000"
+"(q\u0000~\u00008q\u0000~\u0000\u001dq\u0000~\u0000Isq\u0000~\u0000\rppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013psq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000"
+"~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq"
+"\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dq\u0000~\u0000Isq\u0000~\u0000\rppsq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0013psq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013ps"
+"q\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000"
+"\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dq\u0000~\u0000Isq\u0000~\u0000\rppsq\u0000~\u0000\u0007q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000"
+"sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000"
+"~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dq\u0000~\u0000Iq\u0000~\u0000\u001dq\u0000~\u0000\u001dq\u0000~\u0000\u001dq\u0000"
+"~\u0000\u001dq\u0000~\u0000\u001dq\u0000~\u0000\u001dq\u0000~\u0000\u001dsq\u0000~\u0000\u0014ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000&L\u0000\u0004nameq\u0000~\u0000\'L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;"
+"xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValue"
+"ConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataT"
+"ypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL"
+"\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fcon"
+"creteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetN"
+"ameq\u0000~\u0000\u001fxq\u0000~\u0000,t\u00001http://apply.grants.gov/system/GlobalLibrar"
+"y-V2.0t\u0000\u0013FormVersionDataTypesr\u00005com.sun.msv.datatype.xsd.Whi"
+"teSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00002\u0000\u0001sr\u0000\'com.sun.msv."
+"datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0091q\u0000~\u0000"
+"\u0096q\u0000~\u0000\u0097q\u0000~\u0000\u0099\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000*q\u0000~\u0000/t\u0000\u0006stringq\u0000~\u0000\u0099\u0001q\u0000~\u0000\u009dt\u0000\tminLeng"
+"th\u0000\u0000\u0000\u0001q\u0000~\u0000\u009dt\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u00006q\u0000~\u0000\u0097q\u0000~\u0000\u0096t\u0000\u00031.2sq\u0000~\u0000\u001et\u0000\u000bFo"
+"rmVersionq\u0000~\u0000=sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedH"
+"ash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef"
+"\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramm"
+"ar/ExpressionPool;xp\u0000\u0000\u0000/\u0001pq\u0000~\u0000wq\u0000~\u0000Wq\u0000~\u0000aq\u0000~\u0000Kq\u0000~\u0000bq\u0000~\u0000#q\u0000~\u0000"
+"Gq\u0000~\u0000Tq\u0000~\u0000_q\u0000~\u0000jq\u0000~\u0000uq\u0000~\u0000\u0080q\u0000~\u0000\u008aq\u0000~\u0000mq\u0000~\u0000\u0005q\u0000~\u0000Vq\u0000~\u0000\u0006q\u0000~\u0000\u0082q\u0000~\u0000"
+"?q\u0000~\u0000lq\u0000~\u0000\u000bq\u0000~\u0000Aq\u0000~\u0000Nq\u0000~\u0000Yq\u0000~\u0000\u000eq\u0000~\u0000Cq\u0000~\u0000Pq\u0000~\u0000[q\u0000~\u0000fq\u0000~\u0000dq\u0000~\u0000"
+"qq\u0000~\u0000oq\u0000~\u0000|q\u0000~\u0000zq\u0000~\u0000\u0086q\u0000~\u0000\u0084q\u0000~\u0000xq\u0000~\u0000\u0011q\u0000~\u0000Dq\u0000~\u0000Qq\u0000~\u0000\\q\u0000~\u0000gq\u0000~\u0000"
+"rq\u0000~\u0000}q\u0000~\u0000\u0087q\u0000~\u0000Lq\u0000~\u0000>x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "---------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.performancesite_1_2_v1_2.impl.PerformanceSite12TypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("Individual" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("DUNSNumber" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  7 :
                        if (("Individual" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("DUNSNumber" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.performancesite_1_2_v1_2.impl.SiteLocationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  6 :
                        if (("OtherSite" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("PrimarySite" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FormVersion = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  8 :
                        if (("OtherSite" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  5 :
                        if (("PrimarySite" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  6 :
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
                    case  0 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
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
                    case  2 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  6 :
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
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/PerformanceSite_1_2-V1-2", "FormVersion");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            eatText1(value);
                            state = 2;
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