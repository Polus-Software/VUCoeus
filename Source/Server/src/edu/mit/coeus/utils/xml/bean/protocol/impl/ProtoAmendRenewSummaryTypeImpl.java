//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class ProtoAmendRenewSummaryTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.ProtoAmendRenewSummaryType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _ProtocolModules;
    protected boolean has_ProtocolModuleCode;
    protected int _ProtocolModuleCode;
    protected java.util.Calendar _UpdateTimestamp;
    protected java.lang.String _ProtocolModuleDesc;
    protected java.lang.String _ProtocolNumber;
    protected java.lang.String _UpdateUser;
    protected java.lang.String _ProtoAmendRenewalNumber;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtoAmendRenewSummaryType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getProtocolModules() {
        if (_ProtocolModules == null) {
            _ProtocolModules = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ProtocolModules;
    }

    public java.util.List getProtocolModules() {
        return _getProtocolModules();
    }

    public int getProtocolModuleCode() {
        return _ProtocolModuleCode;
    }

    public void setProtocolModuleCode(int value) {
        _ProtocolModuleCode = value;
        has_ProtocolModuleCode = true;
    }

    public java.util.Calendar getUpdateTimestamp() {
        return _UpdateTimestamp;
    }

    public void setUpdateTimestamp(java.util.Calendar value) {
        _UpdateTimestamp = value;
    }

    public java.lang.String getProtocolModuleDesc() {
        return _ProtocolModuleDesc;
    }

    public void setProtocolModuleDesc(java.lang.String value) {
        _ProtocolModuleDesc = value;
    }

    public java.lang.String getProtocolNumber() {
        return _ProtocolNumber;
    }

    public void setProtocolNumber(java.lang.String value) {
        _ProtocolNumber = value;
    }

    public java.lang.String getUpdateUser() {
        return _UpdateUser;
    }

    public void setUpdateUser(java.lang.String value) {
        _UpdateUser = value;
    }

    public java.lang.String getProtoAmendRenewalNumber() {
        return _ProtoAmendRenewalNumber;
    }

    public void setProtoAmendRenewalNumber(java.lang.String value) {
        _ProtoAmendRenewalNumber = value;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.ProtoAmendRenewSummaryTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ProtocolModules == null)? 0 :_ProtocolModules.size());
        if (!has_ProtocolModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolModuleCode"));
        }
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolNumber");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProtocolNumber), "ProtocolNumber");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtoAmendRenewalNumber");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProtoAmendRenewalNumber), "ProtoAmendRenewalNumber");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolModuleCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ProtocolModuleCode)), "ProtocolModuleCode");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolModuleDesc");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProtocolModuleDesc), "ProtocolModuleDesc");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "UpdateTimestamp");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(com.sun.msv.datatype.xsd.DateTimeType.theInstance.serializeJavaObject(((java.util.Calendar) _UpdateTimestamp), null), "UpdateTimestamp");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "UpdateUser");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _UpdateUser), "UpdateUser");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        while (idx1 != len1) {
            context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolModules");
            int idx_12 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ProtocolModules.get(idx_12 ++)), "ProtocolModules");
            context.endNamespaceDecls();
            int idx_13 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ProtocolModules.get(idx_13 ++)), "ProtocolModules");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ProtocolModules.get(idx1 ++)), "ProtocolModules");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ProtocolModules == null)? 0 :_ProtocolModules.size());
        if (!has_ProtocolModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolModuleCode"));
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ProtocolModules == null)? 0 :_ProtocolModules.size());
        if (!has_ProtocolModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolModuleCode"));
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtoAmendRenewSummaryType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006excep"
+"tq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003sr\u0000\u0011java."
+"lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.datatype."
+"xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dat"
+"atype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dataty"
+"pe.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.X"
+"SDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;"
+"L\u0000\btypeNameq\u0000~\u0000\u001aL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Wh"
+"iteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006st"
+"ringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserv"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ej"
+"B\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001aL\u0000\fnamespaceURIq\u0000~\u0000\u001axpq\u0000~\u0000\u001eq\u0000~\u0000\u001dsr\u0000\u001d"
+"com.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun."
+"msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000"
+"~\u0000\fxq\u0000~\u0000\u0003q\u0000~\u0000\u0015psq\u0000~\u0000\u0010ppsr\u0000\"com.sun.msv.datatype.xsd.QnameTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000"
+"-q\u0000~\u0000\u001dsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tl"
+"ocalNameq\u0000~\u0000\u001aL\u0000\fnamespaceURIq\u0000~\u0000\u001axr\u0000\u001dcom.sun.msv.grammar.Nam"
+"eClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchem"
+"a-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpressi"
+"on\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0014\u0001psq\u0000~\u00001t\u0000\u000eProtocolNumbert\u0000,http://"
+"www.w3.org/2001/ProtocolSummarySchemasq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013s"
+"q\u0000~\u0000&ppsq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q\u0000~\u00007sq\u0000~\u00001t\u0000\u0017ProtoAmendRenewal"
+"Numberq\u0000~\u0000;sq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010ppsr\u0000 com.sun.msv.datatype"
+".xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.IntegerD"
+"erivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/datatype/x"
+"sd/XSDatatypeImpl;xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0003intq\u0000~\u0000/sr\u0000*com.sun.msv.data"
+"type.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatyp"
+"e.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object"
+";xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFac"
+"et\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~"
+"\u0000GL\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L"
+"\u0000\tfacetNameq\u0000~\u0000\u001axq\u0000~\u0000\u0019ppq\u0000~\u0000/\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd."
+"MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Kppq\u0000~\u0000/\u0000\u0000sr\u0000!com.sun.msv.d"
+"atatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Fq\u0000~\u0000\u001dt\u0000\u0004longq\u0000~\u0000/sq\u0000~\u0000J"
+"ppq\u0000~\u0000/\u0000\u0001sq\u0000~\u0000Qppq\u0000~\u0000/\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.Integer"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Fq\u0000~\u0000\u001dt\u0000\u0007integerq\u0000~\u0000/sr\u0000,com.sun.msv.dat"
+"atype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun."
+"msv.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002"
+"\u0000\u0000xq\u0000~\u0000Nppq\u0000~\u0000/\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0007decimalq\u0000~\u0000/q\u0000~\u0000_t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q"
+"\u0000~\u0000Yt\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010"
+"java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000Yt\u0000\fmaxInclusivesq\u0000"
+"~\u0000c\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000Tq\u0000~\u0000bsr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valu"
+"exq\u0000~\u0000d\u0080\u0000\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000fsq\u0000~\u0000h\u007f\u00ff\u00ff\u00ffq\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000Iq\u0000~\u0000\u001dsq\u0000~\u0000&pp"
+"sq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q\u0000~\u00007sq\u0000~\u00001t\u0000\u0012ProtocolModuleCodeq\u0000~\u0000;s"
+"q\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000&ppsq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q\u0000~\u00007sq\u0000~"
+"\u00001t\u0000\u0012ProtocolModuleDescq\u0000~\u0000;sq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010ppsr\u0000%com"
+".sun.msv.datatype.xsd.DateTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv"
+".datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\bdateT"
+"imeq\u0000~\u0000/q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000|q\u0000~\u0000\u001dsq\u0000~\u0000&ppsq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q"
+"\u0000~\u00007sq\u0000~\u00001t\u0000\u000fUpdateTimestampq\u0000~\u0000;sq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000"
+"&ppsq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q\u0000~\u00007sq\u0000~\u00001t\u0000\nUpdateUserq\u0000~\u0000;sq\u0000~\u0000&"
+"ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun"
+".msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0015psq\u0000~\u0000"
+"\u000bq\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000&ppsq\u0000~\u0000\u0089q\u0000~\u0000\u0015psq\u0000~\u0000(q\u0000~\u0000\u0015psr\u0000"
+"2com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00008psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u00002q\u0000~\u00007sq\u0000~\u00001t\u00009edu.mit.coeus.utils.xml.bean.protocol.P"
+"rotocolModulesTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elem"
+"entssq\u0000~\u0000&ppsq\u0000~\u0000(q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u00003q\u0000~\u00007sq\u0000~\u00001t\u0000\u000fProtocolModu"
+"lesq\u0000~\u0000;q\u0000~\u00007sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHa"
+"sh;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8"
+"\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramma"
+"r/ExpressionPool;xp\u0000\u0000\u0000\u0018\u0001pq\u0000~\u0000\u008bq\u0000~\u0000wq\u0000~\u0000\u0006q\u0000~\u0000\bq\u0000~\u0000\'q\u0000~\u0000>q\u0000~\u0000l"
+"q\u0000~\u0000rq\u0000~\u0000~q\u0000~\u0000\u0084q\u0000~\u0000\u0099q\u0000~\u0000\u008fq\u0000~\u0000\u000fq\u0000~\u0000=q\u0000~\u0000qq\u0000~\u0000\u0083q\u0000~\u0000\u0088q\u0000~\u0000\tq\u0000~\u0000\n"
+"q\u0000~\u0000\u008dq\u0000~\u0000\u0090q\u0000~\u0000Cq\u0000~\u0000\u0007q\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.ProtoAmendRenewSummaryTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  21 :
                        if (("ProtocolModules" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        if (("ProtocolModuleCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("ProtocolModuleDesc" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("ProtocolNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("ProtocolModules" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  19 :
                        if (("ProtocolModuleCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            _getProtocolModules().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolModulesTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolModulesTypeImpl.class), 20, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  12 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ProtoAmendRenewalNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
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
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("ProtoAmendRenewalNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("ProtocolModuleDesc" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  17 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("ProtocolNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("ProtocolModules" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("ProtocolModuleCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 15;
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
                    case  21 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  18 :
                        state = 21;
                        continue outer;
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
                    case  21 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  18 :
                        state = 21;
                        continue outer;
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
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText1(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText2(value);
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  7 :
                            state = 8;
                            eatText3(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText4(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText5(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText6(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateUser = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateTimestamp = ((java.util.Calendar) com.sun.msv.datatype.xsd.DateTimeType.theInstance.createJavaObject(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value), null));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtocolModuleCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ProtocolModuleCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtocolModuleDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtocolNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtoAmendRenewalNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
