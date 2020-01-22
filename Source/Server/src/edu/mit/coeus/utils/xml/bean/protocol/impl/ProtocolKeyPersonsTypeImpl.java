//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class ProtocolKeyPersonsTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.ProtocolKeyPersonsType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected java.lang.String _NonEmployeeFlag;
    protected java.lang.String _AffiliationTypeDesc;
    protected java.lang.String _TrainingFlag;
    protected java.lang.String _PersonName;
    protected java.lang.String _PersonId;
    protected java.util.Calendar _UpdateTimestamp;
    protected java.lang.String _ProtocolNumber;
    protected boolean has_SequenceNumber;
    protected int _SequenceNumber;
    protected boolean has_AffiliationTypeCode;
    protected int _AffiliationTypeCode;
    protected java.lang.String _UpdateUser;
    protected java.lang.String _PersonRole;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolKeyPersonsType.class);
    }

    public java.lang.String getNonEmployeeFlag() {
        return _NonEmployeeFlag;
    }

    public void setNonEmployeeFlag(java.lang.String value) {
        _NonEmployeeFlag = value;
    }

    public java.lang.String getAffiliationTypeDesc() {
        return _AffiliationTypeDesc;
    }

    public void setAffiliationTypeDesc(java.lang.String value) {
        _AffiliationTypeDesc = value;
    }

    public java.lang.String getTrainingFlag() {
        return _TrainingFlag;
    }

    public void setTrainingFlag(java.lang.String value) {
        _TrainingFlag = value;
    }

    public java.lang.String getPersonName() {
        return _PersonName;
    }

    public void setPersonName(java.lang.String value) {
        _PersonName = value;
    }

    public java.lang.String getPersonId() {
        return _PersonId;
    }

    public void setPersonId(java.lang.String value) {
        _PersonId = value;
    }

    public java.util.Calendar getUpdateTimestamp() {
        return _UpdateTimestamp;
    }

    public void setUpdateTimestamp(java.util.Calendar value) {
        _UpdateTimestamp = value;
    }

    public java.lang.String getProtocolNumber() {
        return _ProtocolNumber;
    }

    public void setProtocolNumber(java.lang.String value) {
        _ProtocolNumber = value;
    }

    public int getSequenceNumber() {
        return _SequenceNumber;
    }

    public void setSequenceNumber(int value) {
        _SequenceNumber = value;
        has_SequenceNumber = true;
    }

    public int getAffiliationTypeCode() {
        return _AffiliationTypeCode;
    }

    public void setAffiliationTypeCode(int value) {
        _AffiliationTypeCode = value;
        has_AffiliationTypeCode = true;
    }

    public java.lang.String getUpdateUser() {
        return _UpdateUser;
    }

    public void setUpdateUser(java.lang.String value) {
        _UpdateUser = value;
    }

    public java.lang.String getPersonRole() {
        return _PersonRole;
    }

    public void setPersonRole(java.lang.String value) {
        _PersonRole = value;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolKeyPersonsTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_SequenceNumber) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "SequenceNumber"));
        }
        if (!has_AffiliationTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "AffiliationTypeCode"));
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
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "SequenceNumber");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _SequenceNumber)), "SequenceNumber");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "PersonId");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PersonId), "PersonId");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "PersonName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PersonName), "PersonName");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "PersonRole");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PersonRole), "PersonRole");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "NonEmployeeFlag");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _NonEmployeeFlag), "NonEmployeeFlag");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "TrainingFlag");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _TrainingFlag), "TrainingFlag");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "AffiliationTypeCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _AffiliationTypeCode)), "AffiliationTypeCode");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "AffiliationTypeDesc");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _AffiliationTypeDesc), "AffiliationTypeDesc");
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
        if (!has_SequenceNumber) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "SequenceNumber"));
        }
        if (!has_AffiliationTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "AffiliationTypeCode"));
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_SequenceNumber) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "SequenceNumber"));
        }
        if (!has_AffiliationTypeCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "AffiliationTypeCode"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolKeyPersonsType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex"
+".ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gramma"
+"r/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001a"
+"ignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000"
+"\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/re"
+"laxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv"
+"/util/StringPair;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005va"
+"luexp\u0000psr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\r"
+"isAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fn"
+"amespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpac"
+"et\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http:"
+"//www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000"
+"\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fn"
+"amespaceURIq\u0000~\u0000\u001expq\u0000~\u0000\"q\u0000~\u0000!sr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0010xq\u0000~\u0000\u0003q\u0000~\u0000\u0019psq\u0000~\u0000\u0014ppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0005QN"
+"amesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u0000\'sq\u0000~\u0000(q\u0000~\u00001q\u0000~\u0000!sr\u0000#com.sun.msv.gramma"
+"r.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURI"
+"q\u0000~\u0000\u001exr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000"
+")http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gr"
+"ammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0018\u0001ps"
+"q\u0000~\u00005t\u0000\u000eProtocolNumbert\u0000,http://www.w3.org/2001/ProtocolSumm"
+"arySchemasq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0014ppsr\u0000 com.sun.msv.datatype.x"
+"sd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.IntegerDer"
+"ivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/datatype/xsd"
+"/XSDatatypeImpl;xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0003intq\u0000~\u00003sr\u0000*com.sun.msv.dataty"
+"pe.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype."
+"xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;x"
+"r\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet"
+"\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000E"
+"L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\t"
+"facetNameq\u0000~\u0000\u001exq\u0000~\u0000\u001dppq\u0000~\u00003\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.Mi"
+"nInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Ippq\u0000~\u00003\u0000\u0000sr\u0000!com.sun.msv.dat"
+"atype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Dq\u0000~\u0000!t\u0000\u0004longq\u0000~\u00003sq\u0000~\u0000Hpp"
+"q\u0000~\u00003\u0000\u0001sq\u0000~\u0000Oppq\u0000~\u00003\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.IntegerTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Dq\u0000~\u0000!t\u0000\u0007integerq\u0000~\u00003sr\u0000,com.sun.msv.datat"
+"ype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.ms"
+"v.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000"
+"xq\u0000~\u0000Lppq\u0000~\u00003\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0007decimalq\u0000~\u00003q\u0000~\u0000]t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~"
+"\u0000Wt\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010ja"
+"va.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000Wt\u0000\fmaxInclusivesq\u0000~\u0000"
+"a\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000Rq\u0000~\u0000`sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuex"
+"q\u0000~\u0000b\u0080\u0000\u0000\u0000q\u0000~\u0000Rq\u0000~\u0000dsq\u0000~\u0000f\u007f\u00ff\u00ff\u00ffq\u0000~\u0000\'sq\u0000~\u0000(q\u0000~\u0000Gq\u0000~\u0000!sq\u0000~\u0000*ppsq"
+"\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u000eSequenceNumberq\u0000~\u0000?sq\u0000~\u0000\u000fp"
+"p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\bP"
+"ersonIdq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q"
+"\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\nPersonNameq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000"
+"*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\nPersonRoleq\u0000~\u0000?sq\u0000~\u0000\u000f"
+"pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u000f"
+"NonEmployeeFlagq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000"
+"\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\fTrainingFlagq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000pp"
+"q\u0000~\u0000Bsq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0013Affiliation"
+"TypeCodeq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000."
+"q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0013AffiliationTypeDescq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0014ppsr\u0000%com.sun.msv.datatype.xsd.DateTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~"
+"\u0000\u001bq\u0000~\u0000!t\u0000\bdateTimeq\u0000~\u00003q\u0000~\u0000\'sq\u0000~\u0000(q\u0000~\u0000\u009eq\u0000~\u0000!sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000"
+"~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u000fUpdateTimestampq\u0000~\u0000?sq\u0000~\u0000\u000fpp\u0000sq\u0000"
+"~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\nUpdate"
+"Userq\u0000~\u0000?sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;x"
+"psr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000"
+"\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Ex"
+"pressionPool;xp\u0000\u0000\u0000 \u0001pq\u0000~\u0000\u000bq\u0000~\u0000\u000eq\u0000~\u0000\bq\u0000~\u0000\rq\u0000~\u0000\u0099q\u0000~\u0000+q\u0000~\u0000jq\u0000~\u0000"
+"pq\u0000~\u0000vq\u0000~\u0000\fq\u0000~\u0000|q\u0000~\u0000\u0082q\u0000~\u0000\u0088q\u0000~\u0000\u008eq\u0000~\u0000\u0094q\u0000~\u0000\u00a0q\u0000~\u0000\u00a6q\u0000~\u0000\u0013q\u0000~\u0000oq\u0000~\u0000"
+"uq\u0000~\u0000{q\u0000~\u0000\u0081q\u0000~\u0000\u0087q\u0000~\u0000\u0093q\u0000~\u0000\u0006q\u0000~\u0000\u00a5q\u0000~\u0000\tq\u0000~\u0000\u0007q\u0000~\u0000Aq\u0000~\u0000\u008dq\u0000~\u0000\u0005q\u0000~\u0000"
+"\nx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolKeyPersonsTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  12 :
                        if (("PersonRole" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        break;
                    case  24 :
                        if (("AffiliationTypeDesc" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 25;
                            return ;
                        }
                        break;
                    case  30 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 31;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("SequenceNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
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
                    case  21 :
                        if (("AffiliationTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("PersonName" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  33 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  27 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 28;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("PersonId" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("TrainingFlag" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("NonEmployeeFlag" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
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
                        if (("PersonId" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  26 :
                        if (("AffiliationTypeDesc" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 27;
                            return ;
                        }
                        break;
                    case  32 :
                        if (("UpdateUser" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 33;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("SequenceNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("PersonName" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  33 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  29 :
                        if (("UpdateTimestamp" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 30;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("PersonRole" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("TrainingFlag" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("AffiliationTypeCode" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 24;
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
                    case  17 :
                        if (("NonEmployeeFlag" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 18;
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
                    case  33 :
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
                    case  33 :
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
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  31 :
                            state = 32;
                            eatText3(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText4(value);
                            return ;
                        case  25 :
                            state = 26;
                            eatText5(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText6(value);
                            return ;
                        case  33 :
                            revertToParentFromText(value);
                            return ;
                        case  19 :
                            state = 20;
                            eatText7(value);
                            return ;
                        case  22 :
                            state = 23;
                            eatText8(value);
                            return ;
                        case  28 :
                            state = 29;
                            eatText9(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText10(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText11(value);
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
                _PersonName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SequenceNumber = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_SequenceNumber = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateUser = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PersonId = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AffiliationTypeDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PersonRole = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _TrainingFlag = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText8(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AffiliationTypeCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_AffiliationTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText9(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdateTimestamp = ((java.util.Calendar) com.sun.msv.datatype.xsd.DateTimeType.theInstance.createJavaObject(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value), null));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText10(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProtocolNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText11(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NonEmployeeFlag = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}