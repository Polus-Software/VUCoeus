//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.10 at 07:45:37 IST 
//


package edu.mit.coeuslite.coiv2.print.approved.impl;

public class PersonImpl
    extends edu.mit.coeuslite.coiv2.print.approved.impl.PersonTypeImpl
    implements edu.mit.coeuslite.coiv2.print.approved.Person, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallableObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializable, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.print.approved.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeuslite.coiv2.print.approved.Person.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "person";
    }

    public edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeuslite.coiv2.print.approved.impl.PersonImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "person");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
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
        return (edu.mit.coeuslite.coiv2.print.approved.Person.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001fcom.sun.msv.gra"
+"mmar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.gram"
+"mar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype"
+";L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004"
+"ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tm"
+"axLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstr"
+"aintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWit"
+"hFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbase"
+"Typet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteT"
+"ypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012"
+"Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImp"
+"l\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001aL\u0000\btypeNameq\u0000~\u0000\u001aL\u0000\nwhiteSpac"
+"et\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000\u0000psr\u00005"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001I\u0000\tminLengthxq\u0000~\u0000\u0016q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd"
+".StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dataty"
+"pe.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype."
+"xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bt\u0000 http://www.w3.org/2001/X"
+"MLSchemat\u0000\u0006stringq\u0000~\u0000!\u0001q\u0000~\u0000\'t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000\'t\u0000\tmaxLengt"
+"h\u0000\u0000\u0007\u00d0sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u001aL\u0000\fnamespaceURIq\u0000~\u0000\u001axpt\u0000\u000estring-derivedq\u0000~\u0000\u001es"
+"r\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.s"
+"un.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClas"
+"sq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~"
+"\u0000\u0011ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%q"
+"\u0000~\u0000(t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 q\u0000~\u0000-sq\u0000~\u0000.q\u0000~\u0000:q\u0000~\u0000(sr\u0000#com.sun.m"
+"sv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001aL\u0000\fnam"
+"espaceURIq\u0000~\u0000\u001axr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp"
+"t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.s"
+"un.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004"
+"sq\u0000~\u00005\u0001psq\u0000~\u0000>t\u0000\bfullNameq\u0000~\u0000\u001esq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0011ppsq\u0000~\u0000"
+"\u0015q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000~\u0000*\u0000\u0000\u0000\u0001q\u0000~\u0000\'q\u0000~"
+"\u0000+\u0000\u0000\u0007\u00d0q\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000estring-derivedq\u0000~\u0000\u001esq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u00006pq"
+"\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\baddress1q\u0000~\u0000\u001esq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0011p"
+"psq\u0000~\u0000\u0015q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000~\u0000*\u0000\u0000\u0000\u0001q\u0000"
+"~\u0000\'q\u0000~\u0000+\u0000\u0000\u0007\u00d0q\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000estring-derivedq\u0000~\u0000\u001esq\u0000~\u00001ppsq\u0000~\u00003q"
+"\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0007dirDeptq\u0000~\u0000\u001esq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq"
+"\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000~\u0000*\u0000"
+"\u0000\u0000\u0001q\u0000~\u0000\'q\u0000~\u0000+\u0000\u0000\u0007\u00d0q\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000estring-derivedq\u0000~\u0000\u001esq\u0000~\u00001ppsq"
+"\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0006schoolq\u0000~\u0000\u001esq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000"
+"~\u0000*\u0000\u0000\u0000\u0001q\u0000~\u0000\'q\u0000~\u0000+\u0000\u0000\u0007\u00d0q\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000estring-derivedq\u0000~\u0000\u001esq\u0000~\u00001"
+"ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\boffPhoneq\u0000~\u0000\u001esq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001epq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q"
+"\u0000~\u0000\'q\u0000~\u0000*\u0000\u0000\u0000\u0001q\u0000~\u0000\'q\u0000~\u0000+\u0000\u0000\u0007\u00d0q\u0000~\u0000-sq\u0000~\u0000.t\u0000\u000estring-derivedq\u0000~\u0000\u001e"
+"sq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0005emailq\u0000~\u0000\u001esq\u0000~\u00001"
+"ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0006personq\u0000~\u0000\u001esr\u0000\"com.sun"
+".msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun"
+"/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gra"
+"mmar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVe"
+"rsionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0013\u0001"
+"pq\u0000~\u0000\u000eq\u0000~\u0000\u000bq\u0000~\u0000\tq\u0000~\u0000\u0010q\u0000~\u0000Iq\u0000~\u00002q\u0000~\u0000Oq\u0000~\u0000Zq\u0000~\u0000eq\u0000~\u0000_q\u0000~\u0000pq\u0000~\u0000"
+"{q\u0000~\u0000\u007fq\u0000~\u0000\fq\u0000~\u0000Tq\u0000~\u0000\rq\u0000~\u0000uq\u0000~\u0000jq\u0000~\u0000\nx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeuslite.coiv2.print.approved.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeuslite.coiv2.print.approved.impl.PersonImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("fullName" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeuslite.coiv2.print.approved.impl.PersonTypeImpl)edu.mit.coeuslite.coiv2.print.approved.impl.PersonImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("person" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
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
                    case  2 :
                        if (("person" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
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
                            revertToParentFromText(value);
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
