//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.common.impl;

public class ContactInfoTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected java.lang.String _FaxNumber;
    protected java.lang.String _Email;
    protected java.lang.String _PhoneNumber;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType _PostalAddress;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.common.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType.class);
    }

    public java.lang.String getFaxNumber() {
        return _FaxNumber;
    }

    public void setFaxNumber(java.lang.String value) {
        _FaxNumber = value;
    }

    public java.lang.String getEmail() {
        return _Email;
    }

    public void setEmail(java.lang.String value) {
        _Email = value;
    }

    public java.lang.String getPhoneNumber() {
        return _PhoneNumber;
    }

    public void setPhoneNumber(java.lang.String value) {
        _PhoneNumber = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType getPostalAddress() {
        return _PostalAddress;
    }

    public void setPostalAddress(edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType value) {
        _PostalAddress = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.common.impl.ContactInfoTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "PostalAddress");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
        context.endElement();
        if ((_PhoneNumber!= null)&&(_Email!= null)) {
            context.startElement("", "PhoneNumber");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PhoneNumber), "PhoneNumber");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
            if (_FaxNumber!= null) {
                context.startElement("", "FaxNumber");
                context.endNamespaceDecls();
                context.endAttributes();
                try {
                    context.text(((java.lang.String) _FaxNumber), "FaxNumber");
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
                context.endElement();
            }
            context.startElement("", "Email");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Email), "Email");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
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
        return (edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000"
+"sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com."
+"sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramm"
+"ar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean"
+"\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0007xq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u00002com.su"
+"n.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003sq\u0000~\u0000\u0011\u0001q\u0000~\u0000\u0016sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.ms"
+"v.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0017"
+"q\u0000~\u0000\u001csr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlo"
+"calNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001exq\u0000~\u0000\u0019t\u0000>edu"
+".mit.coeus.utils.xml.bean.proposal.common.PostalAddressTypet"
+"\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\fppsq\u0000~\u0000\u0013q"
+"\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/"
+"relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/m"
+"sv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.Qnam"
+"eType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L"
+"\u0000\fnamespaceUriq\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.Lcom/sun/"
+"msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/"
+"2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpac"
+"eProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.W"
+"hiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expre"
+"ssion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.ms"
+"v.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURI"
+"q\u0000~\u0000\u001expq\u0000~\u0000/q\u0000~\u0000.sq\u0000~\u0000\u001dt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLS"
+"chema-instanceq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\rPostalAddresst\u0000\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q\u0000"
+"~\u0000\u0012psq\u0000~\u0000\u0000q\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000$ppsr\u0000\'com.sun.msv"
+".datatype.xsd.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom."
+"sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)Lcom/sun/"
+"msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000+t\u00009http://era.nih.gov/"
+"Projectmgmt/SBIR/CGAP/common.namespacet\u0000\u000fPhoneNumberTypeq\u0000~\u0000"
+"2sr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.su"
+"n.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~"
+"\u0000)q\u0000~\u0000.t\u0000\u0005tokenq\u0000~\u00002\u0001\u0000\u0000\u0000\u0000q\u0000~\u00004sq\u0000~\u00005q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\fppsq\u0000~\u0000\u0013"
+"q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u000bPhoneNumberq\u0000~\u0000<sq\u0000~\u0000\fppsq\u0000~\u0000\u0006"
+"q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Bsq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001csq\u0000~\u0000"
+"\u001dt\u0000\tFaxNumberq\u0000~\u0000<q\u0000~\u0000\u001csq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000$ppq\u0000~\u0000Kq\u0000~\u00004sq"
+"\u0000~\u00005q\u0000~\u0000Lq\u0000~\u0000.sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0005Em"
+"ailq\u0000~\u0000<q\u0000~\u0000\u001csr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHa"
+"sh;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8"
+"\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gramma"
+"r/ExpressionPool;xp\u0000\u0000\u0000\u000f\u0001pq\u0000~\u0000>q\u0000~\u0000Zq\u0000~\u0000\u0010q\u0000~\u0000\nq\u0000~\u0000\u0005q\u0000~\u0000?q\u0000~\u0000\r"
+"q\u0000~\u0000=q\u0000~\u0000\"q\u0000~\u0000Nq\u0000~\u0000Uq\u0000~\u0000]q\u0000~\u0000Rq\u0000~\u0000Aq\u0000~\u0000Tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.common.impl.ContactInfoTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("PhoneNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        if (("FaxNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  0 :
                        if (("PostalAddress" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("Street" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("MailStopCode" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("City" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Email" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  12 :
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
                    case  8 :
                        if (("FaxNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 12;
                        continue outer;
                    case  11 :
                        if (("Email" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  5 :
                        if (("PhoneNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("PostalAddress" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
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
                    case  3 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
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
                    case  3 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
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
                        case  3 :
                            state = 12;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  4 :
                            eatText1(value);
                            state = 5;
                            return ;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  10 :
                            eatText3(value);
                            state = 11;
                            return ;
                        case  12 :
                            revertToParentFromText(value);
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
                _PhoneNumber = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FaxNumber = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Email = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
