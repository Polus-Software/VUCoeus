//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.10 at 07:45:37 IST 
//


package edu.mit.coeuslite.coiv2.print.approved.impl;

public class PersonTypeImpl implements edu.mit.coeuslite.coiv2.print.approved.PersonType, com.sun.xml.bind.JAXBObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallableObject, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializable, edu.mit.coeuslite.coiv2.print.approved.impl.runtime.ValidatableObject
{

    protected java.lang.String _Email;
    protected java.lang.String _DirDept;
    protected java.lang.String _Address1;
    protected java.lang.String _FullName;
    protected java.lang.String _School;
    protected java.lang.String _OffPhone;
    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.print.approved.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeuslite.coiv2.print.approved.PersonType.class);
    }

    public java.lang.String getEmail() {
        return _Email;
    }

    public void setEmail(java.lang.String value) {
        _Email = value;
    }

    public java.lang.String getDirDept() {
        return _DirDept;
    }

    public void setDirDept(java.lang.String value) {
        _DirDept = value;
    }

    public java.lang.String getAddress1() {
        return _Address1;
    }

    public void setAddress1(java.lang.String value) {
        _Address1 = value;
    }

    public java.lang.String getFullName() {
        return _FullName;
    }

    public void setFullName(java.lang.String value) {
        _FullName = value;
    }

    public java.lang.String getSchool() {
        return _School;
    }

    public void setSchool(java.lang.String value) {
        _School = value;
    }

    public java.lang.String getOffPhone() {
        return _OffPhone;
    }

    public void setOffPhone(java.lang.String value) {
        _OffPhone = value;
    }

    public edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeuslite.coiv2.print.approved.impl.PersonTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "fullName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _FullName), "FullName");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "address1");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Address1), "Address1");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "dirDept");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _DirDept), "DirDept");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "school");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _School), "School");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "offPhone");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _OffPhone), "OffPhone");
        } catch (java.lang.Exception e) {
            edu.mit.coeuslite.coiv2.print.approved.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "email");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Email), "Email");
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
        return (edu.mit.coeuslite.coiv2.print.approved.PersonType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com."
+"sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000"
+"\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Elem"
+"entExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentMode"
+"lq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000"
+"\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv"
+".datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.s"
+"un.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT"
+"\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005"
+"Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/m"
+"sv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/ms"
+"v/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String"
+";xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fna"
+"mespaceUriq\u0000~\u0000\u0018L\u0000\btypeNameq\u0000~\u0000\u0018L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/"
+"datatype/xsd/WhiteSpaceProcessor;xpt\u0000\u0000psr\u00005com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.m"
+"sv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.su"
+"n.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~"
+"\u0000\u0014q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAto"
+"micType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006string"
+"q\u0000~\u0000\u001f\u0001q\u0000~\u0000%t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000%t\u0000\tmaxLength\u0000\u0000\u0007\u00d0sr\u00000com.sun."
+"msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003pps"
+"r\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0018L\u0000"
+"\fnamespaceURIq\u0000~\u0000\u0018xpt\u0000\u000estring-derivedq\u0000~\u0000\u001csr\u0000\u001dcom.sun.msv.gr"
+"ammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.At"
+"tributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003sr\u0000\u0011j"
+"ava.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u000fppsr\u0000\"com.sun.m"
+"sv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000#q\u0000~\u0000&t\u0000\u0005QNamesr\u00005c"
+"om.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000+sq\u0000~\u0000,q\u0000~\u00008q\u0000~\u0000&sr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0018L\u0000\fnamespaceURIq\u0000~\u0000\u0018xr\u0000"
+"\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://"
+"www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u00003\u0001psq\u0000~\u0000<t\u0000\b"
+"fullNameq\u0000~\u0000\u001csq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsq\u0000~\u0000\u0013q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0001sq\u0000"
+"~\u0000 q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000\u0000\u0001q\u0000~\u0000%q\u0000~\u0000)\u0000\u0000\u0007\u00d0q\u0000~\u0000+sq\u0000~\u0000,"
+"t\u0000\u000estring-derivedq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000"
+"~\u0000<t\u0000\baddress1q\u0000~\u0000\u001csq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsq\u0000~\u0000\u0013q\u0000~\u0000\u001cpq\u0000~\u0000"
+"\u001f\u0000\u0001sq\u0000~\u0000 q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000\u0000\u0001q\u0000~\u0000%q\u0000~\u0000)\u0000\u0000\u0007\u00d0q\u0000~\u0000+"
+"sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000"
+"~\u0000Bsq\u0000~\u0000<t\u0000\u0007dirDeptq\u0000~\u0000\u001csq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsq\u0000~\u0000\u0013q\u0000~\u0000\u001c"
+"pq\u0000~\u0000\u001f\u0000\u0001sq\u0000~\u0000 q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000\u0000\u0001q\u0000~\u0000%q\u0000~\u0000)\u0000\u0000\u0007\u00d0"
+"q\u0000~\u0000+sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000"
+"~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0006schoolq\u0000~\u0000\u001csq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsq\u0000~\u0000\u0013q"
+"\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0001sq\u0000~\u0000 q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000\u0000\u0001q\u0000~\u0000%q\u0000~\u0000)"
+"\u0000\u0000\u0007\u00d0q\u0000~\u0000+sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~"
+"\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\boffPhoneq\u0000~\u0000\u001csq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fpps"
+"q\u0000~\u0000\u0013q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0001sq\u0000~\u0000 q\u0000~\u0000\u001cpq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000%q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000\u0000\u0001q\u0000~\u0000"
+"%q\u0000~\u0000)\u0000\u0000\u0007\u00d0q\u0000~\u0000+sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~"
+"\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0005emailq\u0000~\u0000\u001csr\u0000\"com.sun.msv.grammar"
+".ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar"
+"/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Express"
+"ionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006pare"
+"ntt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000\tq\u0000~\u0000\u0006q"
+"\u0000~\u0000\u000eq\u0000~\u0000Gq\u0000~\u00000q\u0000~\u0000Mq\u0000~\u0000Xq\u0000~\u0000cq\u0000~\u0000]q\u0000~\u0000nq\u0000~\u0000yq\u0000~\u0000\u0007q\u0000~\u0000Rq\u0000~\u0000\bq"
+"\u0000~\u0000sq\u0000~\u0000hq\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeuslite.coiv2.print.approved.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------");
        }

        protected Unmarshaller(edu.mit.coeuslite.coiv2.print.approved.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeuslite.coiv2.print.approved.impl.PersonTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  18 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  12 :
                        if (("offPhone" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("school" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("fullName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("email" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("dirDept" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("address1" == ___local)&&("" == ___uri)) {
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
                    case  18 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("dirDept" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("address1" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("school" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("offPhone" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("email" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("fullName" == ___local)&&("" == ___uri)) {
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
                    case  18 :
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
                    case  18 :
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
                        case  18 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            eatText1(value);
                            state = 11;
                            return ;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  1 :
                            eatText3(value);
                            state = 2;
                            return ;
                        case  13 :
                            eatText4(value);
                            state = 14;
                            return ;
                        case  16 :
                            eatText5(value);
                            state = 17;
                            return ;
                        case  4 :
                            eatText6(value);
                            state = 5;
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
                _School = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DirDept = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FullName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _OffPhone = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Email = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Address1 = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
