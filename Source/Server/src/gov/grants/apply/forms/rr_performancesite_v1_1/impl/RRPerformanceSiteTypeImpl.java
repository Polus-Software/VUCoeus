//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_performancesite_v1_1.impl;

public class RRPerformanceSiteTypeImpl implements gov.grants.apply.forms.rr_performancesite_v1_1.RRPerformanceSiteType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.rr_performancesite_v1_1.SiteLocationDataType _PrimarySite;
    protected java.lang.String _FormVersion;
    protected gov.grants.apply.system.attachments_v1.AttachedFileDataType _AttachedFile;
    protected com.sun.xml.bind.util.ListImpl _OtherSite;
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_performancesite_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_performancesite_v1_1.RRPerformanceSiteType.class);
    }

    public gov.grants.apply.forms.rr_performancesite_v1_1.SiteLocationDataType getPrimarySite() {
        return _PrimarySite;
    }

    public void setPrimarySite(gov.grants.apply.forms.rr_performancesite_v1_1.SiteLocationDataType value) {
        _PrimarySite = value;
    }

    public java.lang.String getFormVersion() {
        return _FormVersion;
    }

    public void setFormVersion(java.lang.String value) {
        _FormVersion = value;
    }

    public gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachedFile() {
        return _AttachedFile;
    }

    public void setAttachedFile(gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
        _AttachedFile = value;
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
        return new gov.grants.apply.forms.rr_performancesite_v1_1.impl.RRPerformanceSiteTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.startElement("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "PrimarySite");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _PrimarySite), "PrimarySite");
        context.endElement();
        while (idx4 != len4) {
            context.startElement("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "OtherSite");
            int idx_2 = idx4;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx_2 ++)), "OtherSite");
            context.endNamespaceDecls();
            int idx_3 = idx4;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx_3 ++)), "OtherSite");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _OtherSite.get(idx4 ++)), "OtherSite");
            context.endElement();
        }
        if (_AttachedFile!= null) {
            context.startElement("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "AttachedFile");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AttachedFile), "AttachedFile");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AttachedFile), "AttachedFile");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _AttachedFile), "AttachedFile");
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.startAttribute("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "FormVersion");
        try {
            context.text(((java.lang.String) _FormVersion), "FormVersion");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        while (idx4 != len4) {
            idx4 += 1;
        }
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx4 = 0;
        final int len4 = ((_OtherSite == null)? 0 :_OtherSite.size());
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", null, true);
        while (idx4 != len4) {
            idx4 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.rr_performancesite_v1_1.RRPerformanceSiteType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000s"
+"q\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001cc"
+"om.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011j"
+"ava.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.gramm"
+"ar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003"
+"q\u0000~\u0000\u0014psr\u00002com.sun.msv.grammar.Expression$AnyStringExpression"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0013\u0001q\u0000~\u0000\u0018sr\u0000 com.sun.msv.grammar.AnyName"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0019q\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq"
+"\u0000~\u0000 xq\u0000~\u0000\u001bt\u0000Cgov.grants.apply.forms.rr_performancesite_v1_1."
+"SiteLocationDataTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-el"
+"ementssq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000"
+"\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun."
+"msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatyp"
+"e.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.x"
+"sd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDat"
+"atypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000 L\u0000\btypeNameq\u0000~\u0000 L\u0000\nw"
+"hiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xp"
+"t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.d"
+"atatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.s"
+"un.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com."
+"sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~"
+"\u0000 L\u0000\fnamespaceURIq\u0000~\u0000 xpq\u0000~\u00001q\u0000~\u00000sq\u0000~\u0000\u001ft\u0000\u0004typet\u0000)http://www"
+".w3.org/2001/XMLSchema-instanceq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\u000bPrimarySitet\u00005h"
+"ttp://apply.grants.gov/forms/RR_PerformanceSite-V1-1sq\u0000~\u0000\u000epp"
+"sq\u0000~\u0000\u0000q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000"
+"\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q"
+"\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\tOtherSiteq\u0000~\u0000>sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~"
+"\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q"
+"\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000"
+")q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000"
+"~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq"
+"\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jsq\u0000~\u0000\u000eppsq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014ps"
+"q\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000"
+"\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000"
+"\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001es"
+"q\u0000~\u0000\u001fq\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jsq\u0000~\u0000"
+"\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014psq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q"
+"\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~"
+"\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jsq\u0000~\u0000\u000eppsq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000"
+"~\u0000\bpp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001fq"
+"\u0000~\u0000\"q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001eq\u0000~\u0000Jq\u0000~\u0000\u001eq\u0000~\u0000\u001eq"
+"\u0000~\u0000\u001eq\u0000~\u0000\u001eq\u0000~\u0000\u001eq\u0000~\u0000\u001eq\u0000~\u0000\u001esq\u0000~\u0000\u000eppsq\u0000~\u0000\bq\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bp"
+"p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000\u0018q\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000;go"
+"v.grants.apply.system.attachments_v1.AttachedFileDataTypeq\u0000~"
+"\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0014pq\u0000~\u0000)q\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\u001ft\u0000\fAttachedFileq\u0000"
+"~\u0000>q\u0000~\u0000\u001esq\u0000~\u0000\u0015ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L"
+"\u0000\u0002dtq\u0000~\u0000\'L\u0000\u0004nameq\u0000~\u0000(L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0003ppsr"
+"\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLe"
+"ngthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraint"
+"Facet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFac"
+"et\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseType"
+"t\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet"
+"\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000 xq"
+"\u0000~\u0000-t\u00001http://apply.grants.gov/system/GlobalLibrary-V2.0t\u0000\u0013F"
+"ormVersionDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePro"
+"cessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00003\u0000\u0001sr\u0000\'com.sun.msv.datatype.x"
+"sd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u009fq\u0000~\u0000\u00a4q\u0000~\u0000\u00a5q\u0000~\u0000"
+"\u00a7\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAl"
+"waysValidxq\u0000~\u0000+q\u0000~\u00000t\u0000\u0006stringq\u0000~\u0000\u00a7\u0001q\u0000~\u0000\u00abt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000"
+"\u00abt\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u00007q\u0000~\u0000\u00a5q\u0000~\u0000\u00a4t\u0000\u00031.1sq\u0000~\u0000\u001ft\u0000\u000bFormVersionq"
+"\u0000~\u0000>sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpT"
+"ablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-"
+"com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005c"
+"ountB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Express"
+"ionPool;xp\u0000\u0000\u00005\u0001pq\u0000~\u0000xq\u0000~\u0000Xq\u0000~\u0000bq\u0000~\u0000Lq\u0000~\u0000cq\u0000~\u0000$q\u0000~\u0000Hq\u0000~\u0000Uq\u0000~\u0000"
+"`q\u0000~\u0000kq\u0000~\u0000vq\u0000~\u0000\u0081q\u0000~\u0000\u008bq\u0000~\u0000\u0096q\u0000~\u0000\u0005q\u0000~\u0000nq\u0000~\u0000Wq\u0000~\u0000\u0007q\u0000~\u0000\u0083q\u0000~\u0000\u008dq\u0000~\u0000"
+"@q\u0000~\u0000mq\u0000~\u0000\fq\u0000~\u0000Bq\u0000~\u0000Oq\u0000~\u0000Zq\u0000~\u0000\u000fq\u0000~\u0000Dq\u0000~\u0000Qq\u0000~\u0000\\q\u0000~\u0000gq\u0000~\u0000eq\u0000~\u0000"
+"rq\u0000~\u0000pq\u0000~\u0000}q\u0000~\u0000{q\u0000~\u0000\u0087q\u0000~\u0000\u0085q\u0000~\u0000yq\u0000~\u0000\u0091q\u0000~\u0000\u008fq\u0000~\u0000\u0012q\u0000~\u0000Eq\u0000~\u0000Rq\u0000~\u0000"
+"]q\u0000~\u0000hq\u0000~\u0000sq\u0000~\u0000~q\u0000~\u0000\u0088q\u0000~\u0000\u0092q\u0000~\u0000\u0006q\u0000~\u0000Mq\u0000~\u0000?x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.rr_performancesite_v1_1.impl.RRPerformanceSiteTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("PrimarySite" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                    case  10 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            _getOtherSite().add(((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("FileName" == ___local)&&("http://apply.grants.gov/system/Attachments-V1.0" == ___uri)) {
                            _AttachedFile = ((gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  6 :
                        if (("OtherSite" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        if (("AttachedFile" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  4 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            _PrimarySite = ((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_performancesite_v1_1.impl.SiteLocationDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
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
                    case  5 :
                        if (("PrimarySite" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("OtherSite" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("AttachedFile" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  9 :
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  9 :
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  9 :
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
                            eatText1(value);
                            state = 2;
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_PerformanceSite-V1-1", "FormVersion");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case  9 :
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
