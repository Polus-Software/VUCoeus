//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class GrantNumberTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.nih.GrantNumberType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected java.math.BigInteger _SerialNumber;
    protected java.lang.String _ApplicationTypeCode;
    protected java.lang.String _AdministeringOrganizationID;
    protected java.math.BigInteger _SupportYear;
    protected java.lang.String _SuffixCode;
    protected java.lang.String _ActivityCode;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.GrantNumberType.class);
    }

    public java.math.BigInteger getSerialNumber() {
        return _SerialNumber;
    }

    public void setSerialNumber(java.math.BigInteger value) {
        _SerialNumber = value;
    }

    public java.lang.String getApplicationTypeCode() {
        return _ApplicationTypeCode;
    }

    public void setApplicationTypeCode(java.lang.String value) {
        _ApplicationTypeCode = value;
    }

    public java.lang.String getAdministeringOrganizationID() {
        return _AdministeringOrganizationID;
    }

    public void setAdministeringOrganizationID(java.lang.String value) {
        _AdministeringOrganizationID = value;
    }

    public java.math.BigInteger getSupportYear() {
        return _SupportYear;
    }

    public void setSupportYear(java.math.BigInteger value) {
        _SupportYear = value;
    }

    public java.lang.String getSuffixCode() {
        return _SuffixCode;
    }

    public void setSuffixCode(java.lang.String value) {
        _SuffixCode = value;
    }

    public java.lang.String getActivityCode() {
        return _ActivityCode;
    }

    public void setActivityCode(java.lang.String value) {
        _ActivityCode = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.GrantNumberTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_ApplicationTypeCode!= null) {
            context.startElement("", "ApplicationTypeCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ApplicationTypeCode), "ApplicationTypeCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ActivityCode!= null) {
            context.startElement("", "ActivityCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ActivityCode), "ActivityCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("", "AdministeringOrganizationID");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _AdministeringOrganizationID), "AdministeringOrganizationID");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "SerialNumber");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _SerialNumber)), "SerialNumber");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_SupportYear!= null) {
            context.startElement("", "SupportYear");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _SupportYear)), "SupportYear");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_SuffixCode!= null) {
            context.startElement("", "SuffixCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _SuffixCode), "SuffixCode");
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
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.GrantNumberType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom."
+"sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv."
+"grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/su"
+"n/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq"
+"\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000"
+"\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/"
+"datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/"
+"StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000"
+"\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\f"
+"namespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001cL\u0000\nwhiteSpa"
+"cet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http"
+"://www.w3.org/2001/XMLSchemat\u0000\u0005tokensr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0011"
+"psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001c"
+"L\u0000\fnamespaceURIq\u0000~\u0000\u001cxpq\u0000~\u0000 q\u0000~\u0000\u001fsq\u0000~\u0000\nppsr\u0000 com.sun.msv.gram"
+"mar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\rxq\u0000~\u0000"
+"\u0003q\u0000~\u0000\u0011psq\u0000~\u0000\u0013ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u0005QNameq\u0000~\u0000#q\u0000~\u0000%sq\u0000~\u0000&q\u0000~\u0000.q\u0000~\u0000\u001fsr\u0000#com.sun"
+".msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001cL\u0000\fn"
+"amespaceURIq\u0000~\u0000\u001cxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com"
+".sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0003sq\u0000~\u0000\u0010\u0001q\u0000~\u00006sq\u0000~\u00000t\u0000\u0013ApplicationTypeCodet\u0000\u0000q\u0000~\u00006sq\u0000~\u0000\nppsq"
+"\u0000~\u0000\fq\u0000~\u0000\u0011p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\nppsq\u0000~\u0000)q\u0000~\u0000\u0011pq\u0000~\u0000+q\u0000~\u00002q\u0000~\u00006s"
+"q\u0000~\u00000t\u0000\fActivityCodeq\u0000~\u0000:q\u0000~\u00006sq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0013ppsr\u0000\'c"
+"om.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengt"
+"hxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFac"
+"et\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)"
+"Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'L"
+"com/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001cxq\u0000~\u0000"
+"\u001bt\u0000>http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.nam"
+"espacepq\u0000~\u0000#\u0000\u0000q\u0000~\u0000\u001eq\u0000~\u0000\u001et\u0000\tminLength\u0000\u0000\u0000\u0002q\u0000~\u0000%sq\u0000~\u0000&t\u0000\rtoken-"
+"derivedq\u0000~\u0000Ksq\u0000~\u0000\nppsq\u0000~\u0000)q\u0000~\u0000\u0011pq\u0000~\u0000+q\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\u001bAdmi"
+"nisteringOrganizationIDq\u0000~\u0000:sq\u0000~\u0000\fpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0013ppsr\u0000,com"
+".sun.msv.datatype.xsd.PositiveIntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000$com."
+"sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.d"
+"atatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000Hx"
+"q\u0000~\u0000\u0019q\u0000~\u0000\u001ft\u0000\u000fpositiveIntegerq\u0000~\u0000#sr\u0000*com.sun.msv.datatype.xs"
+"d.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.R"
+"angeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000F"
+"ppq\u0000~\u0000#\u0000\u0000sr\u0000/com.sun.msv.datatype.xsd.NonNegativeIntegerType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Wq\u0000~\u0000\u001ft\u0000\u0012nonNegativeIntegerq\u0000~\u0000#sq\u0000~\u0000[ppq\u0000~\u0000"
+"#\u0000\u0000sq\u0000~\u0000Wq\u0000~\u0000\u001ft\u0000\u0007integerq\u0000~\u0000#sr\u0000,com.sun.msv.datatype.xsd.Fr"
+"actionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype"
+".xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Gppq\u0000"
+"~\u0000#\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0019q\u0000~\u0000\u001ft\u0000\u0007decimalq\u0000~\u0000#q\u0000~\u0000it\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000ct\u0000\fminIn"
+"clusivesr\u0000)com.sun.msv.datatype.xsd.IntegerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000\u001cxr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q\u0000~\u0000`q\u0000"
+"~\u0000lsq\u0000~\u0000mt\u0000\u00011q\u0000~\u0000%sq\u0000~\u0000&q\u0000~\u0000Zq\u0000~\u0000\u001fsq\u0000~\u0000\nppsq\u0000~\u0000)q\u0000~\u0000\u0011pq\u0000~\u0000+q"
+"\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\fSerialNumberq\u0000~\u0000:sq\u0000~\u0000\nppsq\u0000~\u0000\fq\u0000~\u0000\u0011p\u0000sq\u0000~"
+"\u0000\u0000ppq\u0000~\u0000Usq\u0000~\u0000\nppsq\u0000~\u0000)q\u0000~\u0000\u0011pq\u0000~\u0000+q\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\u000bSupport"
+"Yearq\u0000~\u0000:q\u0000~\u00006sq\u0000~\u0000\nppsq\u0000~\u0000\fq\u0000~\u0000\u0011p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\nppsq\u0000~"
+"\u0000)q\u0000~\u0000\u0011pq\u0000~\u0000+q\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\nSuffixCodeq\u0000~\u0000:q\u0000~\u00006sr\u0000\"com."
+"sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/"
+"sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv."
+"grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstrea"
+"mVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000"
+"\u0000\u0015\u0001pq\u0000~\u0000Cq\u0000~\u0000\u0007q\u0000~\u0000\u0012q\u0000~\u0000=q\u0000~\u0000\u0081q\u0000~\u0000\u0006q\u0000~\u0000\bq\u0000~\u0000\u000bq\u0000~\u0000;q\u0000~\u0000\u007fq\u0000~\u0000\tq"
+"\u0000~\u0000xq\u0000~\u0000\u0005q\u0000~\u0000(q\u0000~\u0000>q\u0000~\u0000Oq\u0000~\u0000tq\u0000~\u0000{q\u0000~\u0000\u0082q\u0000~\u0000Tq\u0000~\u0000zx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.GrantNumberTypeImpl.this;
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
                    case  15 :
                        if (("SuffixCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  9 :
                        if (("SerialNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ActivityCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        if (("AdministeringOrganizationID" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("ApplicationTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
                        if (("SupportYear" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
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
                        if (("ApplicationTypeCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("SuffixCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  18 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  11 :
                        if (("SerialNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("SupportYear" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  5 :
                        if (("ActivityCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("AdministeringOrganizationID" == ___local)&&("" == ___uri)) {
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
                    case  18 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                    case  18 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                            eatText1(value);
                            state = 2;
                            return ;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  18 :
                            revertToParentFromText(value);
                            return ;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  16 :
                            eatText3(value);
                            state = 17;
                            return ;
                        case  13 :
                            eatText4(value);
                            state = 14;
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  4 :
                            eatText5(value);
                            state = 5;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  10 :
                            eatText6(value);
                            state = 11;
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
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
                _ApplicationTypeCode = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AdministeringOrganizationID = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SuffixCode = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SupportYear = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ActivityCode = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SerialNumber = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}