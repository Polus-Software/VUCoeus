//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class CoreFederalAgencyReceiptQualifiersTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected java.util.Calendar _AgencyReceiptDate;
    protected java.lang.String _AgencyName;
    protected java.lang.String _AgencyApplicationIdentifier;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType.class);
    }

    public java.util.Calendar getAgencyReceiptDate() {
        return _AgencyReceiptDate;
    }

    public void setAgencyReceiptDate(java.util.Calendar value) {
        _AgencyReceiptDate = value;
    }

    public java.lang.String getAgencyName() {
        return _AgencyName;
    }

    public void setAgencyName(java.lang.String value) {
        _AgencyName = value;
    }

    public java.lang.String getAgencyApplicationIdentifier() {
        return _AgencyApplicationIdentifier;
    }

    public void setAgencyApplicationIdentifier(java.lang.String value) {
        _AgencyApplicationIdentifier = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.CoreFederalAgencyReceiptQualifiersTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "AgencyReceiptDate");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _AgencyReceiptDate)), "AgencyReceiptDate");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_AgencyApplicationIdentifier!= null) {
            context.startElement("", "AgencyApplicationIdentifier");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _AgencyApplicationIdentifier), "AgencyApplicationIdentifier");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("", "AgencyName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _AgencyName), "AgencyName");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.CoreFederalAgencyReceiptQualifiersType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000pps"
+"r\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxn"
+"g/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/uti"
+"l/StringPair;xq\u0000~\u0000\u0003ppsr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5"
+"\u00b4\u00e5\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'co"
+"m.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespace"
+"Urit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0015L\u0000\nwhiteSpacet\u0000.Lcom"
+"/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3"
+".org/2001/XMLSchemat\u0000\u0004datesr\u00005com.sun.msv.datatype.xsd.White"
+"SpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.x"
+"sd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.E"
+"xpression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Bo"
+"olean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0"
+"t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xpq\u0000~\u0000\u0019q\u0000~\u0000\u0018s"
+"r\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.s"
+"un.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClas"
+"sq\u0000~\u0000\bxq\u0000~\u0000\u0003q\u0000~\u0000 psq\u0000~\u0000\fppsr\u0000\"com.sun.msv.datatype.xsd.Qname"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005QNameq\u0000~\u0000\u001cq\u0000~\u0000\u001esq\u0000~\u0000!q\u0000~\u0000*q\u0000~\u0000\u0018"
+"sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNa"
+"meq\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xr\u0000\u001dcom.sun.msv.grammar.NameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-inst"
+"ancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u001f\u0001q\u0000~\u00002sq\u0000~\u0000,t\u0000\u0011AgencyReceiptDatet\u0000\u0000sq\u0000~\u0000#"
+"ppsq\u0000~\u0000\u0007q\u0000~\u0000 p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fppsr\u0000\"com.sun.msv.datatype.xsd."
+"TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.StringType\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005tokenq\u0000~\u0000\u001c\u0001q\u0000~\u0000\u001esq\u0000~"
+"\u0000!q\u0000~\u0000>q\u0000~\u0000\u0018sq\u0000~\u0000#ppsq\u0000~\u0000%q\u0000~\u0000 pq\u0000~\u0000\'q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\u001bAgen"
+"cyApplicationIdentifierq\u0000~\u00006q\u0000~\u00002sq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fq\u0000~\u0000"
+" psq\u0000~\u0000<q\u0000~\u0000\u0018t\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpac"
+"eProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001b\u0001q\u0000~\u0000\u001esq\u0000~\u0000!q\u0000~\u0000Hq\u0000~\u0000\u0018sq"
+"\u0000~\u0000#ppsq\u0000~\u0000%q\u0000~\u0000 pq\u0000~\u0000\'q\u0000~\u0000.q\u0000~\u00002sq\u0000~\u0000,t\u0000\nAgencyNameq\u0000~\u00006sr\u0000"
+"\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/"
+"Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun"
+".msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\r"
+"streamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool"
+";xp\u0000\u0000\u0000\t\u0001pq\u0000~\u0000\u0006q\u0000~\u00009q\u0000~\u0000\u000bq\u0000~\u00007q\u0000~\u0000\u0005q\u0000~\u0000Eq\u0000~\u0000$q\u0000~\u0000@q\u0000~\u0000Lx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.CoreFederalAgencyReceiptQualifiersTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        if (("AgencyName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("AgencyApplicationIdentifier" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  0 :
                        if (("AgencyReceiptDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
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
                        if (("AgencyName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("AgencyApplicationIdentifier" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("AgencyReceiptDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
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
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
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
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
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
                        case  9 :
                            revertToParentFromText(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  7 :
                            eatText1(value);
                            state = 8;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  1 :
                            eatText3(value);
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
                _AgencyName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AgencyApplicationIdentifier = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AgencyReceiptDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
