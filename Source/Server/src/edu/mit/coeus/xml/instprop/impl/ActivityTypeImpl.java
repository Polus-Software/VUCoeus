//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop.impl;

public class ActivityTypeImpl implements edu.mit.coeus.xml.instprop.ActivityType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallableObject, edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializable, edu.mit.coeus.xml.instprop.impl.runtime.ValidatableObject
{

    protected boolean has_ActivityTypeCode;
    protected int _ActivityTypeCode;
    protected java.lang.String _ActivityTypeDesc;
    public final static java.lang.Class version = (edu.mit.coeus.xml.instprop.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.xml.instprop.ActivityType.class);
    }

    public int getActivityTypeCode() {
        return _ActivityTypeCode;
    }

    public void setActivityTypeCode(int value) {
        _ActivityTypeCode = value;
        has_ActivityTypeCode = true;
    }

    public java.lang.String getActivityTypeDesc() {
        return _ActivityTypeDesc;
    }

    public void setActivityTypeDesc(java.lang.String value) {
        _ActivityTypeDesc = value;
    }

    public edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.xml.instprop.impl.ActivityTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (has_ActivityTypeCode) {
            context.startElement("", "activityTypeCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ActivityTypeCode)), "ActivityTypeCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ActivityTypeDesc!= null) {
            context.startElement("", "activityTypeDesc");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ActivityTypeDesc), "ActivityTypeDesc");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.xml.instprop.ActivityType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom."
+"sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttr"
+"ibutesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa"
+"\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002"
+"L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000 com.sun.m"
+"sv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.x"
+"sd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv"
+"/datatype/xsd/XSDatatypeImpl;xr\u0000*com.sun.msv.datatype.xsd.Bu"
+"iltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concr"
+"eteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImp"
+"l\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq"
+"\u0000~\u0000\u0019L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProc"
+"essor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0003intsr\u00005com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+",com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr"
+"\u0000*com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#c"
+"om.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000"
+"\u0012Ljava/lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWith"
+"ValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd."
+"DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheck"
+"FlagL\u0000\bbaseTypeq\u0000~\u0000\u0015L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/"
+"xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u0019xq\u0000~\u0000\u0018ppq\u0000~\u0000 \u0000\u0001sr\u0000*com.sun"
+".msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"ppq\u0000~\u0000 \u0000"
+"\u0000sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001c"
+"t\u0000\u0004longq\u0000~\u0000 sq\u0000~\u0000!ppq\u0000~\u0000 \u0000\u0001sq\u0000~\u0000(ppq\u0000~\u0000 \u0000\u0000sr\u0000$com.sun.msv.da"
+"tatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001ct\u0000\u0007integerq\u0000~\u0000 s"
+"r\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000"
+"\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstr"
+"aintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000%ppq\u0000~\u0000 \u0001\u0000sr\u0000#com.sun.msv.datatype."
+"xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0007decimalq\u0000~\u0000 q\u0000~\u00006t\u0000\u000ef"
+"ractionDigits\u0000\u0000\u0000\u0000q\u0000~\u00000t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc"
+"\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u00000"
+"t\u0000\fmaxInclusivesq\u0000~\u0000:\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000+q\u0000~\u00009sr\u0000\u0011java.lang.Integer"
+"\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000;\u0080\u0000\u0000\u0000q\u0000~\u0000+q\u0000~\u0000=sq\u0000~\u0000?\u007f\u00ff\u00ff\u00ffsr\u00000com.sun"
+".msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003pp"
+"sr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L"
+"\u0000\fnamespaceURIq\u0000~\u0000\u0019xpq\u0000~\u0000\u001dq\u0000~\u0000\u001csq\u0000~\u0000\u0006ppsr\u0000 com.sun.msv.gramm"
+"ar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003"
+"q\u0000~\u0000\rpsq\u0000~\u0000\u000fppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0005QNameq\u0000~\u0000 q\u0000~\u0000Csq\u0000~\u0000Dq\u0000~\u0000Lq\u0000~\u0000\u001csr\u0000#com.sun."
+"msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fna"
+"mespaceURIq\u0000~\u0000\u0019xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"pt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com."
+"sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003sq\u0000~\u0000\f\u0001q\u0000~\u0000Tsq\u0000~\u0000Nt\u0000\u0010activityTypeCodet\u0000\u0000q\u0000~\u0000Tsq\u0000~\u0000\u0006ppsq\u0000~\u0000\b"
+"q\u0000~\u0000\rp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsr\u0000#com.sun.msv.datatype.xsd.StringTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0016q\u0000~\u0000\u001ct\u0000\u0006stringsr\u00005com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u001f\u0001q\u0000~\u0000Csq\u0000~\u0000Dq\u0000~\u0000_q\u0000~\u0000\u001csq\u0000~\u0000\u0006ppsq\u0000~\u0000Gq\u0000~\u0000\rpq\u0000~\u0000Iq\u0000~\u0000Pq\u0000~\u0000T"
+"sq\u0000~\u0000Nt\u0000\u0010activityTypeDescq\u0000~\u0000Xq\u0000~\u0000Tsr\u0000\"com.sun.msv.grammar.E"
+"xpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/E"
+"xpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expressio"
+"nPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parent"
+"t\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0007\u0001pq\u0000~\u0000Yq\u0000~\u0000[q\u0000~"
+"\u0000Fq\u0000~\u0000cq\u0000~\u0000\u000eq\u0000~\u0000\u0005q\u0000~\u0000\u0007x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.xml.instprop.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.xml.instprop.impl.ActivityTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("activityTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  3 :
                        if (("activityTypeDesc" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
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
                    case  5 :
                        if (("activityTypeDesc" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("activityTypeCode" == ___local)&&("" == ___uri)) {
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
                    case  0 :
                        state = 3;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
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
                    case  0 :
                        state = 3;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
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
                            state = 3;
                            continue outer;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText2(value);
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
                _ActivityTypeCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ActivityTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ActivityTypeDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
