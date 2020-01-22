//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class ProtocolTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.ProtocolType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected java.lang.String _Description;
    protected boolean has_ProtocolTypeCode;
    protected int _ProtocolTypeCode;
    protected java.util.Calendar _UpdateTimestamp;
    protected java.lang.String _UpdateUser;
    protected java.lang.String _AvailableInLite;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolType.class);
    }

    public java.lang.String getDescription() {
        return _Description;
    }

    public void setDescription(java.lang.String value) {
        _Description = value;
    }

    public int getProtocolTypeCode() {
        return _ProtocolTypeCode;
    }

    public void setProtocolTypeCode(int value) {
        _ProtocolTypeCode = value;
        has_ProtocolTypeCode = true;
    }

    public java.util.Calendar getUpdateTimestamp() {
        return _UpdateTimestamp;
    }

    public void setUpdateTimestamp(java.util.Calendar value) {
        _UpdateTimestamp = value;
    }

    public java.lang.String getUpdateUser() {
        return _UpdateUser;
    }

    public void setUpdateUser(java.lang.String value) {
        _UpdateUser = value;
    }

    public java.lang.String getAvailableInLite() {
        return _AvailableInLite;
    }

    public void setAvailableInLite(java.lang.String value) {
        _AvailableInLite = value;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ProtocolTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolTypeCode"));
        }
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "AvailableInLite");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _AvailableInLite), "AvailableInLite");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "ProtocolTypeCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ProtocolTypeCode)), "ProtocolTypeCode");
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
        if (!has_ProtocolTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolTypeCode"));
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ProtocolTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ProtocolTypeCode"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv."
+"grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/su"
+"n/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq"
+"\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002"
+"dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001d"
+"Lcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080"
+"\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.Builti"
+"nAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0018"
+"L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcesso"
+"r;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun."
+"msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr"
+"\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalN"
+"ameq\u0000~\u0000\u0018L\u0000\fnamespaceURIq\u0000~\u0000\u0018xpq\u0000~\u0000\u001cq\u0000~\u0000\u001bsr\u0000\u001dcom.sun.msv.gram"
+"mar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Attr"
+"ibuteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\u0013ps"
+"q\u0000~\u0000\u000eppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0015q\u0000~\u0000\u001bt\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProces"
+"sor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000!sq\u0000~\u0000\"q\u0000~\u0000+q\u0000~\u0000\u001bsr\u0000#com.su"
+"n.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0018L\u0000\f"
+"namespaceURIq\u0000~\u0000\u0018xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003sq\u0000~\u0000\u0012\u0001psq\u0000~\u0000/t\u0000\u000fAvailableInLitet\u0000,http://www.w3.org/2001"
+"/ProtocolSummarySchemasq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000eppsr\u0000 com.sun.m"
+"sv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.x"
+"sd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv"
+"/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u0015q\u0000~\u0000\u001bt\u0000\u0003intq\u0000~\u0000-sr\u0000*com.s"
+"un.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun."
+"msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/"
+"lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueCo"
+"nstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTyp"
+"eWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\b"
+"baseTypeq\u0000~\u0000?L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Con"
+"creteType;L\u0000\tfacetNameq\u0000~\u0000\u0018xq\u0000~\u0000\u0017ppq\u0000~\u0000-\u0000\u0001sr\u0000*com.sun.msv.da"
+"tatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Cppq\u0000~\u0000-\u0000\u0000sr\u0000!co"
+"m.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000>q\u0000~\u0000\u001bt\u0000\u0004long"
+"q\u0000~\u0000-sq\u0000~\u0000Bppq\u0000~\u0000-\u0000\u0001sq\u0000~\u0000Ippq\u0000~\u0000-\u0000\u0000sr\u0000$com.sun.msv.datatype."
+"xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000>q\u0000~\u0000\u001bt\u0000\u0007integerq\u0000~\u0000-sr\u0000,com."
+"sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalex"
+"r\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFac"
+"etT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Fppq\u0000~\u0000-\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.Num"
+"berType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0015q\u0000~\u0000\u001bt\u0000\u0007decimalq\u0000~\u0000-q\u0000~\u0000Wt\u0000\u000efraction"
+"Digits\u0000\u0000\u0000\u0000q\u0000~\u0000Qt\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J"
+"\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000Qt\u0000\fmaxI"
+"nclusivesq\u0000~\u0000[\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000Lq\u0000~\u0000Zsr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u0087"
+"8\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000\\\u0080\u0000\u0000\u0000q\u0000~\u0000Lq\u0000~\u0000^sq\u0000~\u0000`\u007f\u00ff\u00ff\u00ffq\u0000~\u0000!sq\u0000~\u0000\"q\u0000~\u0000Aq\u0000"
+"~\u0000\u001bsq\u0000~\u0000$ppsq\u0000~\u0000&q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\u0010ProtocolTypeC"
+"odeq\u0000~\u00009sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0011sq\u0000~\u0000$ppsq\u0000~\u0000&q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00001"
+"q\u0000~\u00005sq\u0000~\u0000/t\u0000\u000bDescriptionq\u0000~\u00009sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000eppsr\u0000%c"
+"om.sun.msv.datatype.xsd.DateTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.m"
+"sv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u0015q\u0000~\u0000\u001bt\u0000\bdat"
+"eTimeq\u0000~\u0000-q\u0000~\u0000!sq\u0000~\u0000\"q\u0000~\u0000tq\u0000~\u0000\u001bsq\u0000~\u0000$ppsq\u0000~\u0000&q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u0000"
+"1q\u0000~\u00005sq\u0000~\u0000/t\u0000\u000fUpdateTimestampq\u0000~\u00009sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0011sq\u0000"
+"~\u0000$ppsq\u0000~\u0000&q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\nUpdateUserq\u0000~\u00009sr\u0000\""
+"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/L"
+"com/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun."
+"msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rs"
+"treamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;"
+"xp\u0000\u0000\u0000\u000e\u0001pq\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000oq\u0000~\u0000\u0005q\u0000~\u0000%q\u0000~\u0000dq\u0000~\u0000jq\u0000~\u0000vq\u0000~\u0000|q\u0000~\u0000\rq\u0000"
+"~\u0000iq\u0000~\u0000{q\u0000~\u0000;q\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  9 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ProtocolTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("AvailableInLite" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("Description" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
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
                    case  8 :
                        if (("Description" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ProtocolTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  11 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("AvailableInLite" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
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
                    case  15 :
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
                    case  15 :
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
                        case  7 :
                            state = 8;
                            eatText1(value);
                            return ;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText2(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText3(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText4(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText5(value);
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
                _Description = value;
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
                _ProtocolTypeCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ProtocolTypeCode = true;
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
                _AvailableInLite = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}