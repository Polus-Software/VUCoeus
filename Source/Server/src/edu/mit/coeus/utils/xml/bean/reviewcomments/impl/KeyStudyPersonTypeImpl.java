//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments.impl;

public class KeyStudyPersonTypeImpl implements edu.mit.coeus.utils.xml.bean.reviewcomments.KeyStudyPersonType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.ValidatableObject
{

    protected java.lang.String _Affiliation;
    protected java.lang.String _Role;
    protected edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType _Person;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.reviewcomments.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.KeyStudyPersonType.class);
    }

    public java.lang.String getAffiliation() {
        return _Affiliation;
    }

    public void setAffiliation(java.lang.String value) {
        _Affiliation = value;
    }

    public java.lang.String getRole() {
        return _Role;
    }

    public void setRole(java.lang.String value) {
        _Role = value;
    }

    public edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType getPerson() {
        return _Person;
    }

    public void setPerson(edu.mit.coeus.utils.xml.bean.reviewcomments.PersonType value) {
        _Person = value;
    }

    public edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.reviewcomments.impl.KeyStudyPersonTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Person instanceof javax.xml.bind.Element) {
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Person), "Person");
        } else {
            context.startElement("http://irb.mit.edu/irbnamespace", "Person");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Person), "Person");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Person), "Person");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Person), "Person");
            context.endElement();
        }
        if (_Role!= null) {
            context.startElement("http://irb.mit.edu/irbnamespace", "Role");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Role), "Role");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://irb.mit.edu/irbnamespace", "Affiliation");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Affiliation), "Affiliation");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Person instanceof javax.xml.bind.Element) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Person), "Person");
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Person instanceof javax.xml.bind.Element) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Person), "Person");
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.KeyStudyPersonType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0007ppsr\u0000 com.s"
+"un.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramma"
+"r.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd"
+" r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u00002com.sun"
+".msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003"
+"sq\u0000~\u0000\u0011\u0001q\u0000~\u0000\u0016sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv"
+".grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0017q"
+"\u0000~\u0000\u001csr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tloc"
+"alNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001exq\u0000~\u0000\u0019t\u00002edu."
+"mit.coeus.utils.xml.bean.reviewcomments.Persont\u0000+http://java"
+".sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u00006edu.mi"
+"t.coeus.utils.xml.bean.reviewcomments.PersonTypeq\u0000~\u0000!sq\u0000~\u0000\u0007p"
+"psq\u0000~\u0000\u0013q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dt"
+"t\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLc"
+"om/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype."
+"xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Builtin"
+"AtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.L"
+"com/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www"
+".w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.W"
+"hiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.gramm"
+"ar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.su"
+"n.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespac"
+"eURIq\u0000~\u0000\u001expq\u0000~\u00007q\u0000~\u00006sq\u0000~\u0000\u001dt\u0000\u0004typet\u0000)http://www.w3.org/2001/"
+"XMLSchema-instanceq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0006Persont\u0000\u001fhttp://irb.mit.edu/"
+"irbnamespacesq\u0000~\u0000\u0007ppsq\u0000~\u0000\tq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000,ppsr\u0000\'com.sun"
+".msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009c"
+"om.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro"
+"\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/s"
+"un/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/su"
+"n/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001exq\u0000~\u00003q\u0000~\u0000D"
+"psr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00009\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u00001q\u0000~\u00006t\u0000\u0006stringq\u0000~\u0000P\u0001q\u0000~\u0000Rt\u0000\tmax"
+"Length\u0000\u0000\u0000<q\u0000~\u0000<sq\u0000~\u0000=t\u0000\u000estring-derivedq\u0000~\u0000Dsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013q\u0000~"
+"\u0000\u0012pq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0004Roleq\u0000~\u0000Dq\u0000~\u0000\u001csq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq"
+"\u0000~\u0000,ppsq\u0000~\u0000Iq\u0000~\u0000Dpq\u0000~\u0000P\u0000\u0000q\u0000~\u0000Rq\u0000~\u0000Rq\u0000~\u0000T\u0000\u0000\u0000\u00c8q\u0000~\u0000<sq\u0000~\u0000=t\u0000\u000est"
+"ring-derivedq\u0000~\u0000Dsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000"
+"\u000bAffiliationq\u0000~\u0000Dsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$Clos"
+"edHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j"
+"\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gr"
+"ammar/ExpressionPool;xp\u0000\u0000\u0000\u000e\u0001pq\u0000~\u0000\u0006q\u0000~\u0000*q\u0000~\u0000Wq\u0000~\u0000aq\u0000~\u0000#q\u0000~\u0000Gq"
+"\u0000~\u0000\rq\u0000~\u0000%q\u0000~\u0000\bq\u0000~\u0000\u0005q\u0000~\u0000\u0010q\u0000~\u0000&q\u0000~\u0000\\q\u0000~\u0000Ex"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.reviewcomments.impl.KeyStudyPersonTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("Role" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 2;
                            return ;
                        }
                        state = 4;
                        continue outer;
                    case  7 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _Person = ((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl.class), 1, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 8;
                            return ;
                        }
                        break;
                    case  4 :
                        if (("Affiliation" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 5;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("PersonID" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _Person = ((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonTypeImpl.class), 9, ___uri, ___local, ___qname, __atts));
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
                    case  6 :
                        if (("Affiliation" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 7;
                            return ;
                        }
                        break;
                    case  1 :
                        state = 4;
                        continue outer;
                    case  7 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("Role" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 1;
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
                    case  1 :
                        state = 4;
                        continue outer;
                    case  7 :
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
                    case  1 :
                        state = 4;
                        continue outer;
                    case  7 :
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
                        case  1 :
                            state = 4;
                            continue outer;
                        case  7 :
                            revertToParentFromText(value);
                            return ;
                        case  2 :
                            state = 3;
                            eatText1(value);
                            return ;
                        case  5 :
                            state = 6;
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
                _Role = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Affiliation = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
