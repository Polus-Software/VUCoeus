//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.09 at 02:29:50 CDT 
//


package gov.grants.apply.forms.planned_report_v1_0.impl;

public class PlannedReportTotalsDataTypeImpl implements gov.grants.apply.forms.planned_report_v1_0.PlannedReportTotalsDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.math.BigInteger _MultipleRace;
    protected java.math.BigInteger _White;
    protected java.math.BigInteger _Asian;
    protected java.math.BigInteger _AmericanIndian;
    protected java.math.BigInteger _Black;
    protected java.math.BigInteger _Hawaiian;
    protected java.math.BigInteger _Total;
    public final static java.lang.Class version = (gov.grants.apply.forms.planned_report_v1_0.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.planned_report_v1_0.PlannedReportTotalsDataType.class);
    }

    public java.math.BigInteger getMultipleRace() {
        return _MultipleRace;
    }

    public void setMultipleRace(java.math.BigInteger value) {
        _MultipleRace = value;
    }

    public java.math.BigInteger getWhite() {
        return _White;
    }

    public void setWhite(java.math.BigInteger value) {
        _White = value;
    }

    public java.math.BigInteger getAsian() {
        return _Asian;
    }

    public void setAsian(java.math.BigInteger value) {
        _Asian = value;
    }

    public java.math.BigInteger getAmericanIndian() {
        return _AmericanIndian;
    }

    public void setAmericanIndian(java.math.BigInteger value) {
        _AmericanIndian = value;
    }

    public java.math.BigInteger getBlack() {
        return _Black;
    }

    public void setBlack(java.math.BigInteger value) {
        _Black = value;
    }

    public java.math.BigInteger getHawaiian() {
        return _Hawaiian;
    }

    public void setHawaiian(java.math.BigInteger value) {
        _Hawaiian = value;
    }

    public java.math.BigInteger getTotal() {
        return _Total;
    }

    public void setTotal(java.math.BigInteger value) {
        _Total = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.planned_report_v1_0.impl.PlannedReportTotalsDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "AmericanIndian");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _AmericanIndian)), "AmericanIndian");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "Asian");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Asian)), "Asian");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "Hawaiian");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Hawaiian)), "Hawaiian");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "Black");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Black)), "Black");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "White");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _White)), "White");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "MultipleRace");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _MultipleRace)), "MultipleRace");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PlannedReport-V1.0", "Total");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Total)), "Total");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
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
        return (gov.grants.apply.forms.planned_report_v1_0.PlannedReportTotalsDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006excep"
+"tq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000*com"
+".sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.su"
+"n.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljav"
+"a/lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValue"
+"ConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataT"
+"ypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL"
+"\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fcon"
+"creteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetN"
+"amet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatat"
+"ypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001bL\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nwhi"
+"teSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000"
+"0http://apply.grants.gov/forms/PlannedReport-V1.0t\u0000&PlannedR"
+"eport_0_to_9999999999_DataTypesr\u00005com.sun.msv.datatype.xsd.W"
+"hiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000*com.sun.msv.dat"
+"atype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0015q\u0000~\u0000\u001fq\u0000~\u0000 q\u0000~\u0000#\u0000"
+"\u0000sr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com."
+"sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFac"
+"etsq\u0000~\u0000\u0019xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u001ct\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007integerq\u0000~\u0000#sr\u0000,com"
+".sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scale"
+"xr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFa"
+"cetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u0018ppq\u0000~\u0000#\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.Nu"
+"mberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000(q\u0000~\u0000+t\u0000\u0007decimalq\u0000~\u0000#q\u0000~\u00001t\u0000\u000efractio"
+"nDigits\u0000\u0000\u0000\u0000q\u0000~\u0000*t\u0000\fminInclusivesr\u0000)com.sun.msv.datatype.xsd."
+"IntegerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000\u001bxr\u0000\u0010java.lang.Number"
+"\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q\u0000~\u0000*t\u0000\fmaxInclusivesq\u0000~\u00005t\u0000\n9999999999sr\u00000"
+"com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNam"
+"eq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000 q\u0000~\u0000\u001fsr\u0000\u001dcom.sun.msv.gramma"
+"r.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Attrib"
+"uteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000\u0003sr\u0000\u0011java."
+"lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0010ppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000(q\u0000~\u0000+t\u0000\u0005QNameq\u0000~\u0000#q\u0000~\u0000"
+"=sq\u0000~\u0000>q\u0000~\u0000Iq\u0000~\u0000+sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv."
+"grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/20"
+"01/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsi"
+"lonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000D\u0001q\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u000eAmericanI"
+"ndianq\u0000~\u0000\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000@ppsq\u0000~\u0000Bq\u0000~\u0000Epq\u0000~\u0000Fq\u0000~"
+"\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0005Asianq\u0000~\u0000\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000@ppsq\u0000~"
+"\u0000Bq\u0000~\u0000Epq\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\bHawaiianq\u0000~\u0000\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000"
+"ppq\u0000~\u0000\u0013sq\u0000~\u0000@ppsq\u0000~\u0000Bq\u0000~\u0000Epq\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0005Blackq\u0000~\u0000"
+"\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000@ppsq\u0000~\u0000Bq\u0000~\u0000Epq\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Qsq"
+"\u0000~\u0000Kt\u0000\u0005Whiteq\u0000~\u0000\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000@ppsq\u0000~\u0000Bq\u0000~\u0000Epq"
+"\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\fMultipleRaceq\u0000~\u0000\u001fsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000"
+"~\u0000\u0010ppsq\u0000~\u0000\u0014q\u0000~\u0000\u001ft\u0000\'PlannedReport_0_to_99999999999_DataTypeq\u0000"
+"~\u0000#\u0000\u0001sq\u0000~\u0000$q\u0000~\u0000\u001fq\u0000~\u0000wq\u0000~\u0000#\u0000\u0000q\u0000~\u0000*q\u0000~\u0000*q\u0000~\u00004sq\u0000~\u00005q\u0000~\u00008q\u0000~\u0000*q"
+"\u0000~\u00009sq\u0000~\u00005t\u0000\u000b99999999999q\u0000~\u0000=sq\u0000~\u0000>q\u0000~\u0000wq\u0000~\u0000\u001fsq\u0000~\u0000@ppsq\u0000~\u0000Bq"
+"\u0000~\u0000Epq\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000Qsq\u0000~\u0000Kt\u0000\u0005Totalq\u0000~\u0000\u001fsr\u0000\"com.sun.msv.gramm"
+"ar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gramm"
+"ar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expre"
+"ssionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006pa"
+"rentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0014\u0001pq\u0000~\u0000\u0007q\u0000~\u0000"
+"\bq\u0000~\u0000tq\u0000~\u0000\u0005q\u0000~\u0000Aq\u0000~\u0000Wq\u0000~\u0000]q\u0000~\u0000\tq\u0000~\u0000cq\u0000~\u0000iq\u0000~\u0000oq\u0000~\u0000}q\u0000~\u0000\u000fq\u0000~\u0000"
+"Vq\u0000~\u0000\\q\u0000~\u0000bq\u0000~\u0000\nq\u0000~\u0000hq\u0000~\u0000nq\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.planned_report_v1_0.impl.PlannedReportTotalsDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  6 :
                        if (("Hawaiian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("Asian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  21 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
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
                        if (("Hawaiian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  14 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("Asian" == ___local)&&("http://apply.grants.gov/forms/PlannedReport-V1.0" == ___uri)) {
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
                    case  21 :
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
                    case  21 :
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
                        case  10 :
                            eatText1(value);
                            state = 11;
                            return ;
                        case  7 :
                            eatText2(value);
                            state = 8;
                            return ;
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  16 :
                            eatText3(value);
                            state = 17;
                            return ;
                        case  4 :
                            eatText4(value);
                            state = 5;
                            return ;
                        case  13 :
                            eatText5(value);
                            state = 14;
                            return ;
                        case  1 :
                            eatText6(value);
                            state = 2;
                            return ;
                        case  19 :
                            eatText7(value);
                            state = 20;
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
                _Black = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Hawaiian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MultipleRace = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Asian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _White = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AmericanIndian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Total = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}