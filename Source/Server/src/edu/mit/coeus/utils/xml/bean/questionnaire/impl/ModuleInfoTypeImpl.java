//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.11.22 at 02:53:02 PM IST 
//


package edu.mit.coeus.utils.xml.bean.questionnaire.impl;

public class ModuleInfoTypeImpl implements edu.mit.coeus.utils.xml.bean.questionnaire.ModuleInfoType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.ValidatableObject
{

    protected boolean has_ModuleCode;
    protected int _ModuleCode;
    protected java.lang.String _SubModuleDesc;
    protected java.lang.String _ModuleDesc;
    protected boolean has_SubModuleCode;
    protected int _SubModuleCode;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.questionnaire.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.questionnaire.ModuleInfoType.class);
    }

    public int getModuleCode() {
        return _ModuleCode;
    }

    public void setModuleCode(int value) {
        _ModuleCode = value;
        has_ModuleCode = true;
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

    public int getSubModuleCode() {
        return _SubModuleCode;
    }

    public void setSubModuleCode(int value) {
        _SubModuleCode = value;
        has_SubModuleCode = true;
    }

    public edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.questionnaire.impl.ModuleInfoTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
        context.startElement("", "ModuleCode");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ModuleCode)), "ModuleCode");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "ModuleDesc");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ModuleDesc), "ModuleDesc");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (has_SubModuleCode) {
            context.startElement("", "SubModuleCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _SubModuleCode)), "SubModuleCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_SubModuleDesc!= null) {
            context.startElement("", "SubModuleDesc");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SubModuleDesc), "SubModuleDesc");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_ModuleCode) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "ModuleCode"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.questionnaire.ModuleInfoType.class);
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
+"q\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLor"
+"g/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun"
+"/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000 com.sun.msv.datatype.xsd.In"
+"tType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.IntegerDerivedT"
+"ype\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/datatype/xsd/XSDa"
+"tatypeImpl;xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnam"
+"espaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet"
+"\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://"
+"www.w3.org/2001/XMLSchemat\u0000\u0003intsr\u00005com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*com.sun.msv.data"
+"type.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatyp"
+"e.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object"
+";xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFac"
+"et\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~"
+"\u0000\u0013L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L"
+"\u0000\tfacetNameq\u0000~\u0000\u0017xq\u0000~\u0000\u0016ppq\u0000~\u0000\u001e\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd."
+"MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 ppq\u0000~\u0000\u001e\u0000\u0000sr\u0000!com.sun.msv.d"
+"atatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u001at\u0000\u0004longq\u0000~\u0000\u001esq\u0000~\u0000\u001f"
+"ppq\u0000~\u0000\u001e\u0000\u0001sq\u0000~\u0000&ppq\u0000~\u0000\u001e\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.Integer"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u001at\u0000\u0007integerq\u0000~\u0000\u001esr\u0000,com.sun.msv.dat"
+"atype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun."
+"msv.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002"
+"\u0000\u0000xq\u0000~\u0000#ppq\u0000~\u0000\u001e\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0007decimalq\u0000~\u0000\u001eq\u0000~\u00004t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q"
+"\u0000~\u0000.t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010"
+"java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000.t\u0000\fmaxInclusivesq\u0000"
+"~\u00008\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000)q\u0000~\u00007sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valu"
+"exq\u0000~\u00009\u0080\u0000\u0000\u0000q\u0000~\u0000)q\u0000~\u0000;sq\u0000~\u0000=\u007f\u00ff\u00ff\u00ffsr\u00000com.sun.msv.grammar.Expre"
+"ssion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.ut"
+"il.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000"
+"\u0017xpq\u0000~\u0000\u001bq\u0000~\u0000\u001asr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000"
+"~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000"
+"\u0005valuexp\u0000psq\u0000~\u0000\rppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0005QNameq\u0000~\u0000\u001eq\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000Mq\u0000~\u0000\u001asr\u0000#com."
+"sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L"
+"\u0000\fnamespaceURIq\u0000~\u0000\u0017xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000"
+"com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0003sq\u0000~\u0000H\u0001psq\u0000~\u0000Ot\u0000\nModuleCodet\u0000\u0000sq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rq\u0000"
+"~\u0000Ipsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risA"
+"lwaysValidxq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.W"
+"hiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001d\u0001q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000"
+"_q\u0000~\u0000\u001asq\u0000~\u0000Dppsq\u0000~\u0000Fq\u0000~\u0000Ipq\u0000~\u0000Jq\u0000~\u0000Qq\u0000~\u0000Usq\u0000~\u0000Ot\u0000\nModuleDesc"
+"q\u0000~\u0000Ysq\u0000~\u0000Dppsq\u0000~\u0000\bq\u0000~\u0000Ip\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0010sq\u0000~\u0000Dppsq\u0000~\u0000Fq\u0000~\u0000Ipq"
+"\u0000~\u0000Jq\u0000~\u0000Qq\u0000~\u0000Usq\u0000~\u0000Ot\u0000\rSubModuleCodeq\u0000~\u0000Yq\u0000~\u0000Usq\u0000~\u0000Dppsq\u0000~\u0000\b"
+"q\u0000~\u0000Ip\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\\sq\u0000~\u0000Dppsq\u0000~\u0000Fq\u0000~\u0000Ipq\u0000~\u0000Jq\u0000~\u0000Qq\u0000~\u0000Usq\u0000~\u0000"
+"Ot\u0000\rSubModuleDescq\u0000~\u0000Yq\u0000~\u0000Usr\u0000\"com.sun.msv.grammar.Expressio"
+"nPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expressio"
+"nPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Cl"
+"osedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/"
+"sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\r\u0001pq\u0000~\u0000\u0006q\u0000~\u0000\u0007q\u0000~\u0000Eq\u0000~\u0000cq"
+"\u0000~\u0000jq\u0000~\u0000qq\u0000~\u0000gq\u0000~\u0000nq\u0000~\u0000\fq\u0000~\u0000iq\u0000~\u0000[q\u0000~\u0000pq\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.questionnaire.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.questionnaire.impl.ModuleInfoTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("ModuleDesc" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("SubModuleDesc" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("ModuleCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("SubModuleCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
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
                    case  2 :
                        if (("ModuleCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  11 :
                        if (("SubModuleDesc" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("SubModuleCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("ModuleDesc" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
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
                    case  9 :
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        state = 9;
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
                    case  9 :
                        state = 12;
                        continue outer;
                    case  12 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        state = 9;
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
                        case  7 :
                            state = 8;
                            eatText1(value);
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText3(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  10 :
                            state = 11;
                            eatText4(value);
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
                _SubModuleCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_SubModuleCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ModuleDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ModuleCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ModuleCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SubModuleDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
