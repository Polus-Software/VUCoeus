//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class ProtocolActionTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.ProtocolActionType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected java.lang.String _Description;
    protected boolean has_ProtocolActionTypeCode;
    protected int _ProtocolActionTypeCode;
    protected java.lang.String _TriggerSubmission;
    protected java.util.Calendar _UpdateTimestamp;
    protected java.lang.String _TriggerCorrespondence;
    protected java.lang.String _UpdateUser;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolActionType.class);
    }

    public java.lang.String getDescription() {
        return _Description;
    }

    public void setDescription(java.lang.String value) {
        _Description = value;
    }

    public int getProtocolActionTypeCode() {
        return _ProtocolActionTypeCode;
    }

    public void setProtocolActionTypeCode(int value) {
        _ProtocolActionTypeCode = value;
        has_ProtocolActionTypeCode = true;
    }

    public java.lang.String getTriggerSubmission() {
        return _TriggerSubmission;
    }

    public void setTriggerSubmission(java.lang.String value) {
        _TriggerSubmission = value;
    }

    public java.util.Calendar getUpdateTimestamp() {
        return _UpdateTimestamp;
    }

    public void setUpdateTimestamp(java.util.Calendar value) {
        _UpdateTimestamp = value;
    }

    public java.lang.String getTriggerCorrespondence() {
        return _TriggerCorrespondence;
    }

    public void setTriggerCorrespondence(java.lang.String value) {
        _TriggerCorrespondence = value;
    }

    public java.lang.String getUpdateUser() {
        return _UpdateUser;
    }

    public void setUpdateUser(java.lang.String value) {
        _UpdateUser = value;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolActionTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ProtocolActionTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolActionTypeCode"));
        }
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolActionTypeCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ProtocolActionTypeCode)), "ProtocolActionTypeCode");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "Description");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Description), "Description");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "TriggerSubmission");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _TriggerSubmission), "TriggerSubmission");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "TriggerCorrespondence");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _TriggerCorrespondence), "TriggerCorrespondence");
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
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ProtocolActionTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolActionTypeCode"));
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ProtocolActionTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolActionTypeCode"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolActionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com."
+"sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000"
+"\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Elem"
+"entExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentMode"
+"lq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000"
+"\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000 com.sun.msv"
+".datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd"
+".IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/d"
+"atatype/xsd/XSDatatypeImpl;xr\u0000*com.sun.msv.datatype.xsd.Buil"
+"tinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concret"
+"eType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~"
+"\u0000\u0019L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProces"
+"sor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0003intsr\u00005com.sun.m"
+"sv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,c"
+"om.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*"
+"com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com"
+".sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012L"
+"java/lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithVa"
+"lueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Da"
+"taTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFl"
+"agL\u0000\bbaseTypeq\u0000~\u0000\u0015L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xs"
+"d/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u0019xq\u0000~\u0000\u0018ppq\u0000~\u0000 \u0000\u0001sr\u0000*com.sun.m"
+"sv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"ppq\u0000~\u0000 \u0000\u0000s"
+"r\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001ct\u0000"
+"\u0004longq\u0000~\u0000 sq\u0000~\u0000!ppq\u0000~\u0000 \u0000\u0001sq\u0000~\u0000(ppq\u0000~\u0000 \u0000\u0000sr\u0000$com.sun.msv.data"
+"type.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001ct\u0000\u0007integerq\u0000~\u0000 sr\u0000"
+",com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005s"
+"calexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstrai"
+"ntFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000%ppq\u0000~\u0000 \u0001\u0000sr\u0000#com.sun.msv.datatype.xs"
+"d.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0007decimalq\u0000~\u0000 q\u0000~\u00006t\u0000\u000efra"
+"ctionDigits\u0000\u0000\u0000\u0000q\u0000~\u00000t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#"
+"\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u00000t\u0000"
+"\fmaxInclusivesq\u0000~\u0000:\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000+q\u0000~\u00009sr\u0000\u0011java.lang.Integer\u0012\u00e2"
+"\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000;\u0080\u0000\u0000\u0000q\u0000~\u0000+q\u0000~\u0000=sq\u0000~\u0000?\u007f\u00ff\u00ff\u00ffsr\u00000com.sun.m"
+"sv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr"
+"\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\f"
+"namespaceURIq\u0000~\u0000\u0019xpq\u0000~\u0000\u001dq\u0000~\u0000\u001csr\u0000\u001dcom.sun.msv.grammar.ChoiceE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Bool"
+"ean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u000fppsr\u0000\"com.sun.msv.datatype.x"
+"sd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0005QNameq\u0000~\u0000 q\u0000~\u0000Csq\u0000~\u0000Dq\u0000"
+"~\u0000Oq\u0000~\u0000\u001csr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xr\u0000\u001dcom.sun.msv.grammar.N"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSch"
+"ema-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpres"
+"sion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000J\u0001psq\u0000~\u0000Qt\u0000\u0016ProtocolActionTypeCode"
+"t\u0000,http://www.w3.org/2001/ProtocolSummarySchemasq\u0000~\u0000\npp\u0000sq\u0000~"
+"\u0000\u0000ppsq\u0000~\u0000\u000fq\u0000~\u0000Kpsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0006stringsr\u00005com.sun.msv.da"
+"tatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001f\u0001q\u0000~"
+"\u0000Csq\u0000~\u0000Dq\u0000~\u0000aq\u0000~\u0000\u001csq\u0000~\u0000Fppsq\u0000~\u0000Hq\u0000~\u0000Kpq\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000Wsq\u0000~\u0000Qt"
+"\u0000\u000bDescriptionq\u0000~\u0000[sq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000^sq\u0000~\u0000Fppsq\u0000~\u0000Hq\u0000~\u0000Kp"
+"q\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000Wsq\u0000~\u0000Qt\u0000\u0011TriggerSubmissionq\u0000~\u0000[sq\u0000~\u0000\npp\u0000sq\u0000~\u0000"
+"\u0000ppq\u0000~\u0000^sq\u0000~\u0000Fppsq\u0000~\u0000Hq\u0000~\u0000Kpq\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000Wsq\u0000~\u0000Qt\u0000\u0015TriggerC"
+"orrespondenceq\u0000~\u0000[sq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsr\u0000%com.sun.msv.d"
+"atatype.xsd.DateTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype."
+"xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\bdateTimeq\u0000~\u0000 q\u0000"
+"~\u0000Csq\u0000~\u0000Dq\u0000~\u0000{q\u0000~\u0000\u001csq\u0000~\u0000Fppsq\u0000~\u0000Hq\u0000~\u0000Kpq\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000Wsq\u0000~\u0000Q"
+"t\u0000\u000fUpdateTimestampq\u0000~\u0000[sq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000^sq\u0000~\u0000Fppsq\u0000~\u0000Hq"
+"\u0000~\u0000Kpq\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000Wsq\u0000~\u0000Qt\u0000\nUpdateUserq\u0000~\u0000[sr\u0000\"com.sun.msv."
+"grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/"
+"grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar."
+"ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersion"
+"L\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000"
+"\tq\u0000~\u0000\u0006q\u0000~\u0000\bq\u0000~\u0000vq\u0000~\u0000Gq\u0000~\u0000eq\u0000~\u0000kq\u0000~\u0000qq\u0000~\u0000\u0007q\u0000~\u0000}q\u0000~\u0000\u0083q\u0000~\u0000]q\u0000~\u0000"
+"jq\u0000~\u0000pq\u0000~\u0000\u0082q\u0000~\u0000\u0005q\u0000~\u0000\u000ex"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolActionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  15 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("ProtocolActionTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
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
                        if (("Description" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("TriggerSubmission" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  18 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  9 :
                        if (("TriggerCorrespondence" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
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
                    case  17 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("Description" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
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
                    case  2 :
                        if (("ProtocolActionTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  18 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("TriggerSubmission" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("TriggerCorrespondence" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 12;
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
                    case  18 :
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
                    case  18 :
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
                        case  10 :
                            state = 11;
                            eatText1(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText2(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText3(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText4(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText5(value);
                            return ;
                        case  18 :
                            revertToParentFromText(value);
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
                _TriggerCorrespondence = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _TriggerSubmission = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateTimestamp = ((java.util.Calendar) com.sun.msv.datatype.xsd.DateTimeType.theInstance.createJavaObject(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value), null));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateUser = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtocolActionTypeCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ProtocolActionTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Description = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
