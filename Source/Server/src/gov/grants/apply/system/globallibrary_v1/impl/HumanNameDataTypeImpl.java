//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v1.impl;

public class HumanNameDataTypeImpl implements gov.grants.apply.system.globallibrary_v1.HumanNameDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.lang.String _SuffixName;
    protected java.lang.String _FirstName;
    protected java.lang.String _MiddleName;
    protected java.lang.String _LastName;
    protected java.lang.String _PrefixName;
    public final static java.lang.Class version = (gov.grants.apply.system.globallibrary_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.system.globallibrary_v1.HumanNameDataType.class);
    }

    public java.lang.String getSuffixName() {
        return _SuffixName;
    }

    public void setSuffixName(java.lang.String value) {
        _SuffixName = value;
    }

    public java.lang.String getFirstName() {
        return _FirstName;
    }

    public void setFirstName(java.lang.String value) {
        _FirstName = value;
    }

    public java.lang.String getMiddleName() {
        return _MiddleName;
    }

    public void setMiddleName(java.lang.String value) {
        _MiddleName = value;
    }

    public java.lang.String getLastName() {
        return _LastName;
    }

    public void setLastName(java.lang.String value) {
        _LastName = value;
    }

    public java.lang.String getPrefixName() {
        return _PrefixName;
    }

    public void setPrefixName(java.lang.String value) {
        _PrefixName = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.system.globallibrary_v1.impl.HumanNameDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_PrefixName!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "PrefixName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PrefixName), "PrefixName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "FirstName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _FirstName), "FirstName");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_MiddleName!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "MiddleName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _MiddleName), "MiddleName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "LastName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _LastName), "LastName");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_SuffixName!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "SuffixName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SuffixName), "SuffixName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.system.globallibrary_v1.HumanNameDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv."
+"grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011"
+"java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun"
+".msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype"
+"/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPa"
+"ir;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVa"
+"lueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Da"
+"taTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFl"
+"agL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\f"
+"concreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfac"
+"etNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDa"
+"tatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001bL\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\n"
+"whiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;x"
+"pt\u0000*http://apply.grants.gov/system/Global-V1.0t\u0000\u0013StringMin1M"
+"ax10Typesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Pre"
+"serve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProce"
+"ssor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFac"
+"et\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0017q\u0000~\u0000\u001fq\u0000~\u0000 q\u0000~\u0000#\u0000\u0000sr\u0000#com.sun."
+"msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*co"
+"m.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.s"
+"un.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001ct\u0000 http://w"
+"ww.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000#\u0001q\u0000~\u0000)t\u0000\tminLength\u0000\u0000\u0000\u0001"
+"q\u0000~\u0000)t\u0000\tmaxLength\u0000\u0000\u0000\nsr\u00000com.sun.msv.grammar.Expression$Null"
+"SetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringP"
+"air\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000 q\u0000"
+"~\u0000\u001fsq\u0000~\u0000\tppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000\u0012ppsr\u0000\"com.sun.ms"
+"v.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\'q\u0000~\u0000*t\u0000\u0005QNamesr\u00005co"
+"m.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\"q\u0000~\u0000/sq\u0000~\u00000q\u0000~\u00008q\u0000~\u0000*sr\u0000#com.sun.msv.grammar.SimpleN"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001d"
+"com.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://w"
+"ww.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Exp"
+"ression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000f\u0001q\u0000~\u0000Bsq\u0000~\u0000<"
+"t\u0000\nPrefixNamet\u00001http://apply.grants.gov/system/GlobalLibrary"
+"-V1.0q\u0000~\u0000Bsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001ft\u0000\u0013StringMin1M"
+"ax35Typeq\u0000~\u0000#\u0000\u0001sq\u0000~\u0000$q\u0000~\u0000\u001fq\u0000~\u0000Kq\u0000~\u0000#\u0000\u0000q\u0000~\u0000)q\u0000~\u0000)q\u0000~\u0000,\u0000\u0000\u0000\u0001q\u0000~"
+"\u0000)q\u0000~\u0000-\u0000\u0000\u0000#q\u0000~\u0000/sq\u0000~\u00000q\u0000~\u0000Kq\u0000~\u0000\u001fsq\u0000~\u0000\tppsq\u0000~\u00003q\u0000~\u0000\u0010pq\u0000~\u00005q\u0000~"
+"\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\tFirstNameq\u0000~\u0000Fsq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\u0012ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001ft\u0000\u0013StringMin1Max25Typeq\u0000~\u0000#\u0000\u0001sq\u0000~\u0000$q\u0000~\u0000\u001fq\u0000"
+"~\u0000Wq\u0000~\u0000#\u0000\u0000q\u0000~\u0000)q\u0000~\u0000)q\u0000~\u0000,\u0000\u0000\u0000\u0001q\u0000~\u0000)q\u0000~\u0000-\u0000\u0000\u0000\u0019q\u0000~\u0000/sq\u0000~\u00000q\u0000~\u0000Wq"
+"\u0000~\u0000\u001fsq\u0000~\u0000\tppsq\u0000~\u00003q\u0000~\u0000\u0010pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\nMiddleNameq\u0000"
+"~\u0000Fq\u0000~\u0000Bsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0016q\u0000~\u0000\u001ft\u0000\u0013StringMin1Max"
+"60Typeq\u0000~\u0000#\u0000\u0001sq\u0000~\u0000$q\u0000~\u0000\u001fq\u0000~\u0000bq\u0000~\u0000#\u0000\u0000q\u0000~\u0000)q\u0000~\u0000)q\u0000~\u0000,\u0000\u0000\u0000\u0001q\u0000~\u0000)"
+"q\u0000~\u0000-\u0000\u0000\u0000<q\u0000~\u0000/sq\u0000~\u00000q\u0000~\u0000bq\u0000~\u0000\u001fsq\u0000~\u0000\tppsq\u0000~\u00003q\u0000~\u0000\u0010pq\u0000~\u00005q\u0000~\u0000>"
+"q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\bLastNameq\u0000~\u0000Fsq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000"
+"\u0015sq\u0000~\u0000\tppsq\u0000~\u00003q\u0000~\u0000\u0010pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\nSuffixNameq\u0000~\u0000F"
+"q\u0000~\u0000Bsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexp"
+"Tablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000"
+"-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005"
+"countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expres"
+"sionPool;xp\u0000\u0000\u0000\u0011\u0001pq\u0000~\u0000\u0011q\u0000~\u0000kq\u0000~\u0000Rq\u0000~\u0000\bq\u0000~\u0000Hq\u0000~\u0000\u0007q\u0000~\u0000_q\u0000~\u0000\nq\u0000~"
+"\u0000iq\u0000~\u00002q\u0000~\u0000Nq\u0000~\u0000Zq\u0000~\u0000eq\u0000~\u0000lq\u0000~\u0000Tq\u0000~\u0000\u0005q\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.system.globallibrary_v1.impl.HumanNameDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("FirstName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("SuffixName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("PrefixName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("LastName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("MiddleName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
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
                        if (("PrefixName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("FirstName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("MiddleName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  14 :
                        if (("SuffixName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("LastName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
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
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
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
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
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
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  7 :
                            state = 8;
                            eatText3(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  13 :
                            state = 14;
                            eatText4(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText5(value);
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
                _PrefixName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FirstName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MiddleName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SuffixName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _LastName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
