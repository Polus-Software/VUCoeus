//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.24 at 10:09:00 CDT 
//


package gov.grants.apply.forms.humansubjectstudy_v1.impl;

public class HumanSubjectStudyTotalsDataTypeImpl implements gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyTotalsDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.math.BigInteger _MultipleRace;
    protected java.math.BigInteger _White;
    protected java.math.BigInteger _Asian;
    protected java.math.BigInteger _AmericanIndian;
    protected java.math.BigInteger _Black;
    protected java.math.BigInteger _Hawaiian;
    protected java.math.BigInteger _Total;
    public final static java.lang.Class version = (gov.grants.apply.forms.humansubjectstudy_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyTotalsDataType.class);
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
        return new gov.grants.apply.forms.humansubjectstudy_v1.impl.HumanSubjectStudyTotalsDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_AmericanIndian!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "AmericanIndian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _AmericanIndian)), "AmericanIndian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Asian!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Asian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Asian)), "Asian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Hawaiian!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Hawaiian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Hawaiian)), "Hawaiian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Black!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Black");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Black)), "Black");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_White!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "White");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _White)), "White");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_MultipleRace!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "MultipleRace");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _MultipleRace)), "MultipleRace");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Total!= null) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Total");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _Total)), "Total");
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
        return (gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyTotalsDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com."
+"sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000"
+"\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Elem"
+"entExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentMode"
+"lq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000"
+"~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/"
+"relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/m"
+"sv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000*com.sun.msv.datatype.xsd.MaxI"
+"nclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun."
+"msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000"
+"xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\f"
+"isFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/"
+"datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/d"
+"atatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr"
+"\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnames"
+"paceUriq\u0000~\u0000\u001fL\u0000\btypeNameq\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/dat"
+"atype/xsd/WhiteSpaceProcessor;xpt\u00004http://apply.grants.gov/f"
+"orms/HumanSubjectStudy-V1.0t\u0000*HumanSubjectStudy_0_to_9999999"
+"999_DataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclu"
+"siveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019q\u0000~\u0000#q\u0000~\u0000$q\u0000~\u0000\'\u0000\u0000sr\u0000$com.sun.msv.d"
+"atatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.x"
+"sd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000\u001dxr\u0000*com.su"
+"n.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.m"
+"sv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 t\u0000 http://www.w"
+"3.org/2001/XMLSchemat\u0000\u0007integerq\u0000~\u0000\'sr\u0000,com.sun.msv.datatype."
+"xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.da"
+"tatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~"
+"\u0000\u001cppq\u0000~\u0000\'\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000,q\u0000~\u0000/t\u0000\u0007decimalq\u0000~\u0000\'q\u0000~\u00005t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000.t\u0000"
+"\fminInclusivesr\u0000)com.sun.msv.datatype.xsd.IntegerValueType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000\u001fxr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q"
+"\u0000~\u0000.t\u0000\fmaxInclusivesq\u0000~\u00009t\u0000\n9999999999sr\u00000com.sun.msv.gramma"
+"r.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun"
+".msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespace"
+"URIq\u0000~\u0000\u001fxpq\u0000~\u0000$q\u0000~\u0000#sq\u0000~\u0000\u000bppsr\u0000 com.sun.msv.grammar.Attribut"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000exq\u0000~\u0000\u0003q\u0000~\u0000\u0012psq\u0000~\u0000"
+"\u0014ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,q\u0000"
+"~\u0000/t\u0000\u0005QNameq\u0000~\u0000\'q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000Jq\u0000~\u0000/sr\u0000#com.sun.msv.grammar"
+".SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq"
+"\u0000~\u0000\u001fxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)"
+"http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gra"
+"mmar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0011\u0001q\u0000~"
+"\u0000Rsq\u0000~\u0000Lt\u0000\u000eAmericanIndianq\u0000~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~"
+"\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000bppsq\u0000~\u0000Eq\u0000~\u0000\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0005Asianq\u0000"
+"~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000bppsq\u0000~\u0000Eq\u0000~\u0000"
+"\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\bHawaiianq\u0000~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000"
+"~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000bppsq\u0000~\u0000Eq\u0000~\u0000\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt"
+"\u0000\u0005Blackq\u0000~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000bpps"
+"q\u0000~\u0000Eq\u0000~\u0000\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0005Whiteq\u0000~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq"
+"\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000bppsq\u0000~\u0000Eq\u0000~\u0000\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rs"
+"q\u0000~\u0000Lt\u0000\fMultipleRaceq\u0000~\u0000#q\u0000~\u0000Rsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\u0014ppsq\u0000~\u0000\u0018q\u0000~\u0000#t\u0000+HumanSubjectStudy_0_to_99999999999_Data"
+"Typeq\u0000~\u0000\'\u0000\u0001sq\u0000~\u0000(q\u0000~\u0000#q\u0000~\u0000~q\u0000~\u0000\'\u0000\u0000q\u0000~\u0000.q\u0000~\u0000.q\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000<"
+"q\u0000~\u0000.q\u0000~\u0000=sq\u0000~\u00009t\u0000\u000b99999999999q\u0000~\u0000Asq\u0000~\u0000Bq\u0000~\u0000~q\u0000~\u0000#sq\u0000~\u0000\u000bpps"
+"q\u0000~\u0000Eq\u0000~\u0000\u0012pq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u0005Totalq\u0000~\u0000#q\u0000~\u0000Rsr\u0000\"com.su"
+"n.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/su"
+"n/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gr"
+"ammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamV"
+"ersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u001b"
+"\u0001pq\u0000~\u0000nq\u0000~\u0000uq\u0000~\u0000\u0084q\u0000~\u0000\u0005q\u0000~\u0000\tq\u0000~\u0000\nq\u0000~\u0000\u0013q\u0000~\u0000Xq\u0000~\u0000_q\u0000~\u0000fq\u0000~\u0000mq\u0000~"
+"\u0000tq\u0000~\u0000{q\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000\u0006q\u0000~\u0000\fq\u0000~\u0000Vq\u0000~\u0000]q\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000rq\u0000~\u0000yq\u0000~"
+"\u0000Dq\u0000~\u0000Yq\u0000~\u0000`q\u0000~\u0000gx"));
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
            return gov.grants.apply.forms.humansubjectstudy_v1.impl.HumanSubjectStudyTotalsDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("Asian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  18 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  0 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("Hawaiian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  15 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  12 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  21 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  9 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
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
                    case  11 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  14 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  2 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  17 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("Hawaiian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("Asian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  21 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  9 :
                        state = 12;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  21 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  9 :
                        state = 12;
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
                        case  7 :
                            eatText1(value);
                            state = 8;
                            return ;
                        case  13 :
                            eatText2(value);
                            state = 14;
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  16 :
                            eatText3(value);
                            state = 17;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  1 :
                            eatText4(value);
                            state = 2;
                            return ;
                        case  4 :
                            eatText5(value);
                            state = 5;
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            eatText6(value);
                            state = 11;
                            return ;
                        case  19 :
                            eatText7(value);
                            state = 20;
                            return ;
                        case  9 :
                            state = 12;
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
                _Hawaiian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _White = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
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
                _AmericanIndian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Asian = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Black = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
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
