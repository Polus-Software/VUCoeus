//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses.impl;

public class COIDISPOSITIONSTATUSTypeImpl implements edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUSType, com.sun.xml.bind.JAXBObject, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallableObject, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializable, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.ValidatableObject
{

    protected java.util.Calendar _UPDATETIMESTAMP;
    protected java.math.BigDecimal _COIDISPOSITIONCODE;
    protected java.lang.String _UPDATEUSER;
    protected java.lang.String _DESCRIPTION;
    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.jaxbclasses.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUSType.class);
    }

    public java.util.Calendar getUPDATETIMESTAMP() {
        return _UPDATETIMESTAMP;
    }

    public void setUPDATETIMESTAMP(java.util.Calendar value) {
        _UPDATETIMESTAMP = value;
    }

    public java.math.BigDecimal getCOIDISPOSITIONCODE() {
        return _COIDISPOSITIONCODE;
    }

    public void setCOIDISPOSITIONCODE(java.math.BigDecimal value) {
        _COIDISPOSITIONCODE = value;
    }

    public java.lang.String getUPDATEUSER() {
        return _UPDATEUSER;
    }

    public void setUPDATEUSER(java.lang.String value) {
        _UPDATEUSER = value;
    }

    public java.lang.String getDESCRIPTION() {
        return _DESCRIPTION;
    }

    public void setDESCRIPTION(java.lang.String value) {
        _DESCRIPTION = value;
    }

    public edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "COI_DISPOSITION_CODE");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _COIDISPOSITIONCODE)), "COIDISPOSITIONCODE");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "DESCRIPTION");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _DESCRIPTION), "DESCRIPTION");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "UPDATE_TIMESTAMP");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(com.sun.msv.datatype.xsd.DateTimeType.theInstance.serializeJavaObject(((java.util.Calendar) _UPDATETIMESTAMP), null), "UPDATETIMESTAMP");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "UPDATE_USER");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _UPDATEUSER), "UPDATEUSER");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISPOSITIONSTATUSType.class);
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
+"/msv/util/StringPair;xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z"
+"\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.su"
+"n.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit"
+"\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun"
+"/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org"
+"/2001/XMLSchemat\u0000\u0007decimalsr\u00005com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Ex"
+"pression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv"
+".util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq"
+"\u0000~\u0000\u0017xpq\u0000~\u0000\u001bq\u0000~\u0000\u001asr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003ex"
+"pq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003q\u0000~\u0000\u0012psq\u0000~\u0000\rppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0005QNameq\u0000~\u0000\u001eq\u0000~\u0000"
+" sq\u0000~\u0000!q\u0000~\u0000*q\u0000~\u0000\u001asr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xr\u0000\u001dcom.sun.msv."
+"grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/20"
+"01/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsi"
+"lonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0011\u0001psq\u0000~\u0000,t\u0000\u0014COI_DISPOSITI"
+"ON_CODEt\u0000\u0000sq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rq\u0000~\u0000\u0012psr\u0000#com.sun.msv.datat"
+"ype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0006s"
+"tringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preser"
+"ve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001d\u0001q\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000<q\u0000~\u0000\u001asq\u0000~\u0000#ppsq\u0000~\u0000%q\u0000~\u0000\u0012"
+"pq\u0000~\u0000\'q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\u000bDESCRIPTIONq\u0000~\u00006sq\u0000~\u0000\bpp\u0000sq\u0000~\u0000\u0000ppsq"
+"\u0000~\u0000\rq\u0000~\u0000\u0012psr\u0000%com.sun.msv.datatype.xsd.DateTimeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq"
+"\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\bdateTimeq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000Jq\u0000~\u0000\u001asq\u0000~\u0000#ppsq\u0000~\u0000%"
+"q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\u0010UPDATE_TIMESTAMPq\u0000~\u00006sq\u0000~\u0000\bpp\u0000"
+"sq\u0000~\u0000\u0000ppq\u0000~\u00009sq\u0000~\u0000#ppsq\u0000~\u0000%q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\u000bUPD"
+"ATE_USERq\u0000~\u00006sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHa"
+"sh;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8"
+"\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramma"
+"r/ExpressionPool;xp\u0000\u0000\u0000\u000b\u0001pq\u0000~\u0000\u0007q\u0000~\u0000$q\u0000~\u0000@q\u0000~\u0000Lq\u0000~\u0000Rq\u0000~\u0000\u0006q\u0000~\u0000E"
+"q\u0000~\u0000\u0005q\u0000~\u00008q\u0000~\u0000Qq\u0000~\u0000\fx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISPOSITIONSTATUSTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("DESCRIPTION" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("COI_DISPOSITION_CODE" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
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
                    case  6 :
                        if (("UPDATE_TIMESTAMP" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
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
                    case  2 :
                        if (("COI_DISPOSITION_CODE" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
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
                    case  5 :
                        if (("DESCRIPTION" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("UPDATE_TIMESTAMP" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
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
                        case  7 :
                            eatText1(value);
                            state = 8;
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            eatText2(value);
                            state = 11;
                            return ;
                        case  4 :
                            eatText3(value);
                            state = 5;
                            return ;
                        case  1 :
                            eatText4(value);
                            state = 2;
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
                _UPDATETIMESTAMP = ((java.util.Calendar) com.sun.msv.datatype.xsd.DateTimeType.theInstance.createJavaObject(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value), null));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UPDATEUSER = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DESCRIPTION = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _COIDISPOSITIONCODE = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
