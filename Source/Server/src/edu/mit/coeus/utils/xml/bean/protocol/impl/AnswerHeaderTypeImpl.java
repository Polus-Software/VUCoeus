//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class AnswerHeaderTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.AnswerHeaderType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected boolean has_ModuleCode;
    protected int _ModuleCode;
    protected com.sun.xml.bind.util.ListImpl _AnswerInfo;
    protected java.lang.String _SubModuleKey;
    protected java.lang.String _CompletedFlag;
    protected java.lang.String _SubModuleDesc;
    protected java.lang.String _ModuleDesc;
    protected java.lang.String _ModuleKey;
    protected boolean has_SubModuleCode;
    protected int _SubModuleCode;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.AnswerHeaderType.class);
    }

    public int getModuleCode() {
        return _ModuleCode;
    }

    public void setModuleCode(int value) {
        _ModuleCode = value;
        has_ModuleCode = true;
    }

    protected com.sun.xml.bind.util.ListImpl _getAnswerInfo() {
        if (_AnswerInfo == null) {
            _AnswerInfo = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _AnswerInfo;
    }

    public java.util.List getAnswerInfo() {
        return _getAnswerInfo();
    }

    public java.lang.String getSubModuleKey() {
        return _SubModuleKey;
    }

    public void setSubModuleKey(java.lang.String value) {
        _SubModuleKey = value;
    }

    public java.lang.String getCompletedFlag() {
        return _CompletedFlag;
    }

    public void setCompletedFlag(java.lang.String value) {
        _CompletedFlag = value;
    }

    public java.lang.String getSubModuleDesc() {
        return _SubModuleDesc;
    }

    public void setSubModuleDesc(java.lang.String value) {
        _SubModuleDesc = value;
    }

    public java.lang.String getModuleDesc() {
        return _ModuleDesc;
    }

    public void setModuleDesc(java.lang.String value) {
        _ModuleDesc = value;
    }

    public java.lang.String getModuleKey() {
        return _ModuleKey;
    }

    public void setModuleKey(java.lang.String value) {
        _ModuleKey = value;
    }

    public int getSubModuleCode() {
        return _SubModuleCode;
    }

    public void setSubModuleCode(int value) {
        _SubModuleCode = value;
        has_SubModuleCode = true;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerHeaderTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
        int idx2 = 0;
        final int len2 = ((_AnswerInfo == null)? 0 :_AnswerInfo.size());
        context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "CompletedFlag");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _CompletedFlag), "CompletedFlag");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "ModuleCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ModuleCode)), "ModuleCode");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "ModuleDesc");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ModuleDesc), "ModuleDesc");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (has_SubModuleCode) {
            context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "SubModuleCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _SubModuleCode)), "SubModuleCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_SubModuleDesc!= null) {
            context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "SubModuleDesc");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SubModuleDesc), "SubModuleDesc");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "ModuleKey");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ModuleKey), "ModuleKey");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_SubModuleKey!= null) {
            context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "SubModuleKey");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SubModuleKey), "SubModuleKey");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx2 != len2) {
            context.startElement("http://www.w3.org/2001/QuesionnaireSchema", "AnswerInfo");
            int idx_14 = idx2;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AnswerInfo.get(idx_14 ++)), "AnswerInfo");
            context.endNamespaceDecls();
            int idx_15 = idx2;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AnswerInfo.get(idx_15 ++)), "AnswerInfo");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _AnswerInfo.get(idx2 ++)), "AnswerInfo");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
        int idx2 = 0;
        final int len2 = ((_AnswerInfo == null)? 0 :_AnswerInfo.size());
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
        int idx2 = 0;
        final int len2 = ((_AnswerInfo == null)? 0 :_AnswerInfo.size());
        while (idx2 != len2) {
            idx2 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.AnswerHeaderType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun."
+"msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttribut"
+"esL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.gramm"
+"ar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;"
+"L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003s"
+"r\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.d"
+"atatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun"
+".msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.ms"
+"v.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.dataty"
+"pe.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang"
+"/String;L\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatyp"
+"e/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSch"
+"emat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$Nul"
+"lSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.String"
+"Pair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq"
+"\u0000~\u0000\u001esr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 "
+"com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnam"
+"eClassq\u0000~\u0000\rxq\u0000~\u0000\u0003q\u0000~\u0000\u0016psq\u0000~\u0000\u0011ppsr\u0000\"com.sun.msv.datatype.xsd."
+"QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0005QNamesr\u00005com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000$sq"
+"\u0000~\u0000%q\u0000~\u0000.q\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.gra"
+"mmar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/"
+"XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0015\u0001psq\u0000~\u00002t\u0000\rCompletedFlagt\u0000)"
+"http://www.w3.org/2001/QuesionnaireSchemasq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppsq"
+"\u0000~\u0000\u0011ppsr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com"
+".sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFa"
+"cetst\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u0018q\u0000~\u0000\u001et"
+"\u0000\u0003intq\u0000~\u00000sr\u0000*com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\n"
+"limitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.datatype.xsd."
+"DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.d"
+"atatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012ne"
+"edValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000BL\u0000\fconcreteTypet\u0000\'Lcom/sun/m"
+"sv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001bxq\u0000~\u0000\u001appq\u0000~\u00000\u0000"
+"\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u0000Fppq\u0000~\u00000\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000Aq\u0000~\u0000\u001et\u0000\u0004longq\u0000~\u00000sq\u0000~\u0000Eppq\u0000~\u00000\u0000\u0001sq\u0000~\u0000Lppq\u0000~\u00000\u0000\u0000sr\u0000$co"
+"m.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Aq\u0000~\u0000\u001et\u0000\u0007i"
+"ntegerq\u0000~\u00000sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithL"
+"exicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Ippq\u0000~\u00000\u0001\u0000sr\u0000#com.sun.m"
+"sv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007decimalq\u0000"
+"~\u00000q\u0000~\u0000Zt\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000Tt\u0000\fminInclusivesr\u0000\u000ejava.la"
+"ng.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000Tt\u0000\fmaxInclusivesq\u0000~\u0000^\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000Oq\u0000~\u0000]sr\u0000\u0011java."
+"lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000_\u0080\u0000\u0000\u0000q\u0000~\u0000Oq\u0000~\u0000asq\u0000~\u0000c\u007f\u00ff\u00ff"
+"\u00ffq\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000Dq\u0000~\u0000\u001esq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000"
+"~\u00002t\u0000\nModuleCodeq\u0000~\u0000<sq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0014sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~"
+"\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\nModuleDescq\u0000~\u0000<sq\u0000~\u0000\'ppsq\u0000~\u0000\fq\u0000~\u0000"
+"\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000?sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\r"
+"SubModuleCodeq\u0000~\u0000<q\u0000~\u00008sq\u0000~\u0000\'ppsq\u0000~\u0000\fq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0014sq\u0000"
+"~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\rSubModuleDescq\u0000~\u0000<q"
+"\u0000~\u00008sq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0014sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u0000"
+"8sq\u0000~\u00002t\u0000\tModuleKeyq\u0000~\u0000<sq\u0000~\u0000\'ppsq\u0000~\u0000\fq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0014sq"
+"\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\fSubModuleKeyq\u0000~\u0000<q"
+"\u0000~\u00008sq\u0000~\u0000\'ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q"
+"\u0000~\u0000\u0016psq\u0000~\u0000\fq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\'ppsq\u0000~\u0000\u008dq\u0000~\u0000\u0016psq\u0000~\u0000"
+")q\u0000~\u0000\u0016psr\u00002com.sun.msv.grammar.Expression$AnyStringExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00009psr\u0000 com.sun.msv.grammar.AnyNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00003q\u0000~\u00008sq\u0000~\u00002t\u00004edu.mit.coeus.utils.xml.bean."
+"protocol.AnswerInfoTypet\u0000+http://java.sun.com/jaxb/xjc/dummy"
+"-elementssq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\nAnswerI"
+"nfoq\u0000~\u0000<q\u0000~\u00008sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHa"
+"sh;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8"
+"\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramma"
+"r/ExpressionPool;xp\u0000\u0000\u0000\u001e\u0001pq\u0000~\u0000\u008fq\u0000~\u0000\u000bq\u0000~\u0000\nq\u0000~\u0000(q\u0000~\u0000gq\u0000~\u0000mq\u0000~\u0000t"
+"q\u0000~\u0000{q\u0000~\u0000\u0081q\u0000~\u0000\u0088q\u0000~\u0000\u009dq\u0000~\u0000\u0093q\u0000~\u0000\u0010q\u0000~\u0000lq\u0000~\u0000zq\u0000~\u0000\u0080q\u0000~\u0000\u0087q\u0000~\u0000\u008cq\u0000~\u0000\u0091"
+"q\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000\u0006q\u0000~\u0000\u0094q\u0000~\u0000>q\u0000~\u0000sq\u0000~\u0000qq\u0000~\u0000\u0005q\u0000~\u0000xq\u0000~\u0000\u0085q\u0000~\u0000\tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerHeaderTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  21 :
                        if (("AnswerInfo" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  0 :
                        if (("CompletedFlag" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("SubModuleCode" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  18 :
                        if (("SubModuleKey" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  22 :
                        if (("QuestionId" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("QuestionNumber" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("AnswerNumber" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Answer" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname, __atts)));
                        return ;
                    case  24 :
                        if (("AnswerInfo" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 22;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("ModuleCode" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("ModuleDesc" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("ModuleKey" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("SubModuleDesc" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
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
                        state = 24;
                        continue outer;
                    case  8 :
                        if (("ModuleDesc" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("ModuleKey" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  11 :
                        if (("SubModuleCode" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  5 :
                        if (("ModuleCode" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("SubModuleDesc" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  22 :
                        _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname)));
                        return ;
                    case  2 :
                        if (("CompletedFlag" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("AnswerInfo" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  20 :
                        if (("SubModuleKey" == ___local)&&("http://www.w3.org/2001/QuesionnaireSchema" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
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
                        state = 24;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  22 :
                        _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname)));
                        return ;
                    case  24 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  12 :
                        state = 15;
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
                        state = 24;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  22 :
                        _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, ___uri, ___local, ___qname)));
                        return ;
                    case  24 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  12 :
                        state = 15;
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
                            state = 24;
                            continue outer;
                        case  7 :
                            state = 8;
                            eatText1(value);
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  22 :
                            _getAnswerInfo().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.protocol.impl.AnswerInfoTypeImpl.class), 23, value)));
                            return ;
                        case  24 :
                            revertToParentFromText(value);
                            return ;
                        case  19 :
                            state = 20;
                            eatText2(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText3(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText4(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText5(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText6(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText7(value);
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
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
                _ModuleDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SubModuleKey = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SubModuleDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CompletedFlag = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SubModuleCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_SubModuleCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ModuleKey = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ModuleCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ModuleCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
