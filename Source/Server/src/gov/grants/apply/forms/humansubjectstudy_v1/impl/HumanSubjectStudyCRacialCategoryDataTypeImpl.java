//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.24 at 10:09:00 CDT 
//


package gov.grants.apply.forms.humansubjectstudy_v1.impl;

public class HumanSubjectStudyCRacialCategoryDataTypeImpl implements gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyCRacialCategoryDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected boolean has_MultipleRace;
    protected int _MultipleRace;
    protected boolean has_White;
    protected int _White;
    protected boolean has_UnknownRace;
    protected int _UnknownRace;
    protected boolean has_Asian;
    protected int _Asian;
    protected boolean has_AmericanIndian;
    protected int _AmericanIndian;
    protected boolean has_Black;
    protected int _Black;
    protected boolean has_Hawaiian;
    protected int _Hawaiian;
    protected java.math.BigInteger _Total;
    public final static java.lang.Class version = (gov.grants.apply.forms.humansubjectstudy_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyCRacialCategoryDataType.class);
    }

    public int getMultipleRace() {
        return _MultipleRace;
    }

    public void setMultipleRace(int value) {
        _MultipleRace = value;
        has_MultipleRace = true;
    }

    public int getWhite() {
        return _White;
    }

    public void setWhite(int value) {
        _White = value;
        has_White = true;
    }

    public int getUnknownRace() {
        return _UnknownRace;
    }

    public void setUnknownRace(int value) {
        _UnknownRace = value;
        has_UnknownRace = true;
    }

    public int getAsian() {
        return _Asian;
    }

    public void setAsian(int value) {
        _Asian = value;
        has_Asian = true;
    }

    public int getAmericanIndian() {
        return _AmericanIndian;
    }

    public void setAmericanIndian(int value) {
        _AmericanIndian = value;
        has_AmericanIndian = true;
    }

    public int getBlack() {
        return _Black;
    }

    public void setBlack(int value) {
        _Black = value;
        has_Black = true;
    }

    public int getHawaiian() {
        return _Hawaiian;
    }

    public void setHawaiian(int value) {
        _Hawaiian = value;
        has_Hawaiian = true;
    }

    public java.math.BigInteger getTotal() {
        return _Total;
    }

    public void setTotal(java.math.BigInteger value) {
        _Total = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.humansubjectstudy_v1.impl.HumanSubjectStudyCRacialCategoryDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (has_AmericanIndian) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "AmericanIndian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _AmericanIndian)), "AmericanIndian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_Asian) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Asian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Asian)), "Asian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_Hawaiian) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Hawaiian");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Hawaiian)), "Hawaiian");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_Black) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "Black");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Black)), "Black");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_White) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "White");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _White)), "White");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_MultipleRace) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "MultipleRace");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _MultipleRace)), "MultipleRace");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_UnknownRace) {
            context.startElement("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0", "UnknownRace");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _UnknownRace)), "UnknownRace");
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
        return (gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyCRacialCategoryDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005value"
+"xp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dt"
+"t\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLc"
+"om/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000*com.sun.msv.datatype."
+"xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd"
+".RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009"
+"com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7R"
+"o\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/"
+"sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/s"
+"un/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/S"
+"tring;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003"
+"L\u0000\fnamespaceUriq\u0000~\u0000 L\u0000\btypeNameq\u0000~\u0000 L\u0000\nwhiteSpacet\u0000.Lcom/sun"
+"/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u00004http://apply.gran"
+"ts.gov/forms/HumanSubjectStudy-V1.0t\u0000)HumanSubjectStudy_0_to"
+"_999999999_DataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Whit"
+"eSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.M"
+"inInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000$q\u0000~\u0000%q\u0000~\u0000(\u0000\u0000sr\u0000 com.su"
+"n.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatyp"
+"e.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000\u001exr\u0000*com"
+".sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.su"
+"n.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!t\u0000 http://ww"
+"w.w3.org/2001/XMLSchemat\u0000\u0003intq\u0000~\u0000(sq\u0000~\u0000\u0019ppq\u0000~\u0000(\u0000\u0001sq\u0000~\u0000)ppq\u0000~"
+"\u0000(\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,q\u0000"
+"~\u00000t\u0000\u0004longq\u0000~\u0000(sq\u0000~\u0000\u0019ppq\u0000~\u0000(\u0000\u0001sq\u0000~\u0000)ppq\u0000~\u0000(\u0000\u0000sr\u0000$com.sun.msv"
+".datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,q\u0000~\u00000t\u0000\u0007integerq\u0000~"
+"\u0000(sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalCon"
+"straintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u001dppq\u0000~\u0000(\u0001\u0000sr\u0000#com.sun.msv.dataty"
+"pe.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000-q\u0000~\u00000t\u0000\u0007decimalq\u0000~\u0000(q\u0000~\u0000@t"
+"\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000:t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b"
+"\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000"
+"~\u0000:t\u0000\fmaxInclusivesq\u0000~\u0000D\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u00005q\u0000~\u0000Csr\u0000\u0011java.lang.Inte"
+"ger\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000E\u0080\u0000\u0000\u0000q\u0000~\u00005q\u0000~\u0000Gsq\u0000~\u0000I\u007f\u00ff\u00ff\u00ffq\u0000~\u0000/q\u0000~"
+"\u0000Csq\u0000~\u0000I\u0000\u0000\u0000\u0000q\u0000~\u0000/q\u0000~\u0000Gsq\u0000~\u0000I;\u009a\u00c9\u00ffsr\u00000com.sun.msv.grammar.Expr"
+"ession$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.u"
+"til.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000 L\u0000\fnamespaceURIq\u0000~"
+"\u0000 xpq\u0000~\u0000%q\u0000~\u0000$sq\u0000~\u0000\fppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000fxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psq\u0000~\u0000\u0015ppsr\u0000"
+"\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000-q\u0000~\u00000t\u0000\u0005"
+"QNameq\u0000~\u0000(q\u0000~\u0000Osq\u0000~\u0000Pq\u0000~\u0000Xq\u0000~\u00000sr\u0000#com.sun.msv.grammar.Simpl"
+"eNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000 L\u0000\fnamespaceURIq\u0000~\u0000 xr"
+"\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http:/"
+"/www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.E"
+"xpression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u0000`sq\u0000~"
+"\u0000Zt\u0000\u000eAmericanIndianq\u0000~\u0000$q\u0000~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000"
+"~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\u0005Asianq\u0000~\u0000$q\u0000~"
+"\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000"
+"Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\bHawaiianq\u0000~\u0000$q\u0000~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000s"
+"q\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\u0005Blac"
+"kq\u0000~\u0000$q\u0000~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000Sq"
+"\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\u0005Whiteq\u0000~\u0000$q\u0000~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000"
+"~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt"
+"\u0000\fMultipleRaceq\u0000~\u0000$q\u0000~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq"
+"\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\u000bUnknownRaceq\u0000~\u0000$q\u0000"
+"~\u0000`sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0019q\u0000~\u0000$t\u0000*HumanS"
+"ubjectStudy_0_to_9999999999_DataTypeq\u0000~\u0000(\u0000\u0001sq\u0000~\u0000)q\u0000~\u0000$q\u0000~\u0000\u0093q"
+"\u0000~\u0000(\u0000\u0000q\u0000~\u0000:q\u0000~\u0000:q\u0000~\u0000Csr\u0000)com.sun.msv.datatype.xsd.IntegerVal"
+"ueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0005valueq\u0000~\u0000 xq\u0000~\u0000Et\u0000\u00010q\u0000~\u0000:q\u0000~\u0000Gsq\u0000~\u0000\u0095t\u0000\n9"
+"999999999q\u0000~\u0000Osq\u0000~\u0000Pq\u0000~\u0000\u0093q\u0000~\u0000$sq\u0000~\u0000\fppsq\u0000~\u0000Sq\u0000~\u0000\u0013pq\u0000~\u0000Uq\u0000~\u0000\\"
+"q\u0000~\u0000`sq\u0000~\u0000Zt\u0000\u0005Totalq\u0000~\u0000$q\u0000~\u0000`sr\u0000\"com.sun.msv.grammar.Express"
+"ionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Express"
+"ionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$"
+"ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lco"
+"m/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u001f\u0001pq\u0000~\u0000|q\u0000~\u0000\u0083q\u0000~\u0000\u0082q\u0000~\u0000"
+"\u008aq\u0000~\u0000\u0089q\u0000~\u0000\u009bq\u0000~\u0000\u0005q\u0000~\u0000\bq\u0000~\u0000\u000bq\u0000~\u0000\nq\u0000~\u0000\u0007q\u0000~\u0000\u0090q\u0000~\u0000\u0006q\u0000~\u0000\tq\u0000~\u0000\u008eq\u0000~\u0000"
+"\rq\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000rq\u0000~\u0000yq\u0000~\u0000\u0080q\u0000~\u0000\u0087q\u0000~\u0000\u0014q\u0000~\u0000fq\u0000~\u0000mq\u0000~\u0000tq\u0000~\u0000{q\u0000~\u0000"
+"Rq\u0000~\u0000gq\u0000~\u0000nq\u0000~\u0000ux"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.humansubjectstudy_v1.impl.HumanSubjectStudyCRacialCategoryDataTypeImpl.this;
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
                    case  12 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  9 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  24 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  15 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  18 :
                        if (("UnknownRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  6 :
                        if (("Hawaiian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  21 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  0 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  23 :
                        if (("Total" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("Black" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  17 :
                        if (("MultipleRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  14 :
                        if (("White" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
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
                    case  20 :
                        if (("UnknownRace" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("Asian" == ___local)&&("http://apply.grants.gov/forms/HumanSubjectStudy-V1.0" == ___uri)) {
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  24 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  0 :
                        state = 3;
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
                    case  12 :
                        state = 15;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  24 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  0 :
                        state = 3;
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
                        case  3 :
                            state = 6;
                            continue outer;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  24 :
                            revertToParentFromText(value);
                            return ;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  13 :
                            eatText2(value);
                            state = 14;
                            return ;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  22 :
                            eatText3(value);
                            state = 23;
                            return ;
                        case  10 :
                            eatText4(value);
                            state = 11;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  4 :
                            eatText5(value);
                            state = 5;
                            return ;
                        case  16 :
                            eatText6(value);
                            state = 17;
                            return ;
                        case  19 :
                            eatText7(value);
                            state = 20;
                            return ;
                        case  7 :
                            eatText8(value);
                            state = 8;
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
                _AmericanIndian = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_AmericanIndian = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _White = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_White = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Total = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Black = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Black = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Asian = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Asian = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MultipleRace = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_MultipleRace = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UnknownRace = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_UnknownRace = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText8(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Hawaiian = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Hawaiian = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
