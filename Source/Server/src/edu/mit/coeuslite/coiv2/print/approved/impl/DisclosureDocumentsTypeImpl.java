//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.10 at 07:45:37 IST 
//


package edu.mit.coeuslite.coiv2.print.approved.impl;

public class DisclosureDocumentsTypeImpl implements edu.mit.coeuslite.coiv2.print.approved.DisclosureDocumentsType, com.sun.xml.bind.JAXBObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallableObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializable, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.ValidatableObject
{

    protected java.lang.String _UPDATETIMESTAMP;
    protected java.lang.String _Description;
    protected java.lang.String _UPDATEUSER;
    protected java.lang.String _DocumentType;
    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.print.approved.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeuslite.coiv2.print.approved.DisclosureDocumentsType.class);
    }

    public java.lang.String getUPDATETIMESTAMP() {
        return _UPDATETIMESTAMP;
    }

    public void setUPDATETIMESTAMP(java.lang.String value) {
        _UPDATETIMESTAMP = value;
    }

    public java.lang.String getDescription() {
        return _Description;
    }

    public void setDescription(java.lang.String value) {
        _Description = value;
    }

    public java.lang.String getUPDATEUSER() {
        return _UPDATEUSER;
    }

    public void setUPDATEUSER(java.lang.String value) {
        _UPDATEUSER = value;
    }

    public java.lang.String getDocumentType() {
        return _DocumentType;
    }

    public void setDocumentType(java.lang.String value) {
        _DocumentType = value;
    }

    public edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeuslite.coiv2.print.approved.impl.DisclosureDocumentsTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "documentType");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _DocumentType), "DocumentType");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "description");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Description), "Description");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "UPDATE_TIMESTAMP");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _UPDATETIMESTAMP), "UPDATETIMESTAMP");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "UPDATE_USER");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _UPDATEUSER), "UPDATEUSER");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeuslite.coiv2.print.approved.DisclosureDocumentsType.class);
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
+"/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype.xsd.Ma"
+"xLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype."
+"xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.m"
+"sv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ"
+"\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/"
+"XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/C"
+"oncreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv"
+".datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u0016L"
+"\u0000\btypeNameq\u0000~\u0000\u0016L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Whi"
+"teSpaceProcessor;xpt\u0000\u0000psr\u00005com.sun.msv.datatype.xsd.WhiteSpa"
+"ceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.x"
+"sd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0012q\u0000~\u0000\u001apq\u0000~\u0000\u001d\u0000\u0000s"
+"r\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlways"
+"Validxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017"
+"t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000\u001d\u0001q\u0000~\u0000#t\u0000\tmi"
+"nLength\u0000\u0000\u0000\u0001q\u0000~\u0000#t\u0000\tmaxLength\u0000\u0000\u0000\u0014sr\u00000com.sun.msv.grammar.Expr"
+"ession$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.u"
+"til.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~"
+"\u0000\u0016xpt\u0000\u000estring-derivedq\u0000~\u0000\u001asr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean"
+"\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\rppsr\u0000\"com.sun.msv.datatype.xsd."
+"QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000$t\u0000\u0005QNamesr\u00005com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001cq\u0000~\u0000)sq"
+"\u0000~\u0000*q\u0000~\u00006q\u0000~\u0000$sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~\u0000\u0016xr\u0000\u001dcom.sun.msv.gra"
+"mmar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/"
+"XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u00001\u0001psq\u0000~\u0000:t\u0000\fdocumentTypeq\u0000~\u0000"
+"\u001asq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u001apq\u0000~\u0000\u001d\u0000\u0001sq\u0000~\u0000\u001eq\u0000~\u0000\u001apq\u0000~"
+"\u0000\u001d\u0000\u0000q\u0000~\u0000#q\u0000~\u0000#q\u0000~\u0000&\u0000\u0000\u0000\u0001q\u0000~\u0000#q\u0000~\u0000\'\u0000\u0000\u0007\u00d0q\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000estring-de"
+"rivedq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u00002pq\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000@sq\u0000~\u0000:t\u0000\u000bdescri"
+"ptionq\u0000~\u0000\u001asq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u001apq\u0000~\u0000\u001d\u0000\u0001sq\u0000~\u0000\u001e"
+"q\u0000~\u0000\u001apq\u0000~\u0000\u001d\u0000\u0000q\u0000~\u0000#q\u0000~\u0000#q\u0000~\u0000&\u0000\u0000\u0000\u0001q\u0000~\u0000#q\u0000~\u0000\'\u0000\u0000\u0007\u00d0q\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000e"
+"string-derivedq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u00002pq\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000@sq\u0000~\u0000:"
+"t\u0000\u0010UPDATE_TIMESTAMPq\u0000~\u0000\u001asq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u001a"
+"pq\u0000~\u0000\u001d\u0000\u0001sq\u0000~\u0000\u001eq\u0000~\u0000\u001apq\u0000~\u0000\u001d\u0000\u0000q\u0000~\u0000#q\u0000~\u0000#q\u0000~\u0000&\u0000\u0000\u0000\u0001q\u0000~\u0000#q\u0000~\u0000\'\u0000\u0000\u0007\u00d0"
+"q\u0000~\u0000)sq\u0000~\u0000*t\u0000\u000estring-derivedq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u00002pq\u0000~\u00003q\u0000"
+"~\u0000<q\u0000~\u0000@sq\u0000~\u0000:t\u0000\u000bUPDATE_USERq\u0000~\u0000\u001asr\u0000\"com.sun.msv.grammar.Exp"
+"ressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Exp"
+"ressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionP"
+"ool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000"
+"$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u000b\u0001pq\u0000~\u0000\u0006q\u0000~\u0000Eq\u0000~\u0000."
+"q\u0000~\u0000Kq\u0000~\u0000Vq\u0000~\u0000aq\u0000~\u0000\fq\u0000~\u0000\u0007q\u0000~\u0000Pq\u0000~\u0000\u0005q\u0000~\u0000[x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeuslite.coiv2.print.approved.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeuslite.coiv2.print.approved.impl.DisclosureDocumentsTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("documentType" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("UPDATE_TIMESTAMP" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  9 :
                        if (("UPDATE_USER" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("description" == ___local)&&("" == ___uri)) {
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
                    case  8 :
                        if (("UPDATE_TIMESTAMP" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("UPDATE_USER" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("documentType" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("description" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
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
                    case  12 :
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
                    case  12 :
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
                        case  4 :
                            eatText1(value);
                            state = 5;
                            return ;
                        case  1 :
                            eatText2(value);
                            state = 2;
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  10 :
                            eatText4(value);
                            state = 11;
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
                _DocumentType = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UPDATETIMESTAMP = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UPDATEUSER = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}