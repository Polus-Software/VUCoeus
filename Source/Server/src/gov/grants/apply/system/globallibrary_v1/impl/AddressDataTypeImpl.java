//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v1.impl;

public class AddressDataTypeImpl implements gov.grants.apply.system.globallibrary_v1.AddressDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.lang.String _Street2;
    protected java.lang.String _State;
    protected java.lang.String _ZipCode;
    protected java.lang.String _Country;
    protected java.lang.String _City;
    protected java.lang.String _Street1;
    protected java.lang.String _County;
    public final static java.lang.Class version = (gov.grants.apply.system.globallibrary_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.system.globallibrary_v1.AddressDataType.class);
    }

    public java.lang.String getStreet2() {
        return _Street2;
    }

    public void setStreet2(java.lang.String value) {
        _Street2 = value;
    }

    public java.lang.String getState() {
        return _State;
    }

    public void setState(java.lang.String value) {
        _State = value;
    }

    public java.lang.String getZipCode() {
        return _ZipCode;
    }

    public void setZipCode(java.lang.String value) {
        _ZipCode = value;
    }

    public java.lang.String getCountry() {
        return _Country;
    }

    public void setCountry(java.lang.String value) {
        _Country = value;
    }

    public java.lang.String getCity() {
        return _City;
    }

    public void setCity(java.lang.String value) {
        _City = value;
    }

    public java.lang.String getStreet1() {
        return _Street1;
    }

    public void setStreet1(java.lang.String value) {
        _Street1 = value;
    }

    public java.lang.String getCounty() {
        return _County;
    }

    public void setCounty(java.lang.String value) {
        _County = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.system.globallibrary_v1.impl.AddressDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "Street1");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Street1), "Street1");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_Street2 != null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "Street2");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Street2), "Street2");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "City");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _City), "City");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_County!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "County");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _County), "County");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_State!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "State");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _State), "State");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ZipCode!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "ZipCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ZipCode), "ZipCode");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Country!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V1.0", "Country");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Country), "Country");
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
        return (gov.grants.apply.system.globallibrary_v1.AddressDataType.class);
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
+"tq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com"
+".sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthx"
+"r\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet"
+"\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lc"
+"om/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lco"
+"m/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lan"
+"g/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u0019L\u0000\btypeNameq\u0000~\u0000\u0019L\u0000\nwhiteSpacet\u0000.Lcom/"
+"sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000*http://apply.g"
+"rants.gov/system/Global-V1.0t\u0000\u0013StringMin1Max55Typesr\u00005com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000"
+"\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmi"
+"nLengthxq\u0000~\u0000\u0015q\u0000~\u0000\u001dq\u0000~\u0000\u001eq\u0000~\u0000!\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.S"
+"tringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype"
+".xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xs"
+"d.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001at\u0000 http://www.w3.org/2001/XML"
+"Schemat\u0000\u0006stringq\u0000~\u0000!\u0001q\u0000~\u0000\'t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000\'t\u0000\tmaxLength\u0000"
+"\u0000\u00007sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tl"
+"ocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xpq\u0000~\u0000\u001eq\u0000~\u0000\u001dsr\u0000\u001dcom.sun.msv"
+".grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar"
+".AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000\u0003sr"
+"\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0010ppsr\u0000\"com.su"
+"n.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%q\u0000~\u0000(t\u0000\u0005QNamesr"
+"\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 q\u0000~\u0000-sq\u0000~\u0000.q\u0000~\u00009q\u0000~\u0000(sr\u0000#com.sun.msv.grammar.Sim"
+"pleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019"
+"xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http"
+"://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar"
+".Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u00004\u0001q\u0000~\u0000Csq"
+"\u0000~\u0000=t\u0000\u0007Street1t\u00001http://apply.grants.gov/system/GlobalLibrar"
+"y-V1.0sq\u0000~\u00000ppsq\u0000~\u0000\u000bq\u0000~\u00005p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005p"
+"q\u0000~\u00006q\u0000~\u0000?q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0007Street2q\u0000~\u0000Gq\u0000~\u0000Csq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq"
+"\u0000~\u0000\u0010ppsq\u0000~\u0000\u0014q\u0000~\u0000\u001dt\u0000\u0013StringMin1Max35Typeq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001dq\u0000~"
+"\u0000Sq\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000~\u0000*\u0000\u0000\u0000\u0001q\u0000~\u0000\'q\u0000~\u0000+\u0000\u0000\u0000#q\u0000~\u0000-sq\u0000~\u0000.q\u0000~\u0000Sq\u0000"
+"~\u0000\u001dsq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005pq\u0000~\u00006q\u0000~\u0000?q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0004Cityq\u0000~\u0000Gsq\u0000~"
+"\u00000ppsq\u0000~\u0000\u000bq\u0000~\u00005p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0014q\u0000~\u0000\u001dt\u0000\u0013StringMin1Max"
+"30Typeq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000\"q\u0000~\u0000\u001dq\u0000~\u0000_q\u0000~\u0000!\u0000\u0000q\u0000~\u0000\'q\u0000~\u0000\'q\u0000~\u0000*\u0000\u0000\u0000\u0001q\u0000~\u0000\'"
+"q\u0000~\u0000+\u0000\u0000\u0000\u001eq\u0000~\u0000-sq\u0000~\u0000.q\u0000~\u0000_q\u0000~\u0000\u001dsq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005pq\u0000~\u00006q\u0000~\u0000?"
+"q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0006Countyq\u0000~\u0000Gq\u0000~\u0000Csq\u0000~\u00000ppsq\u0000~\u0000\u000bq\u0000~\u00005p\u0000sq\u0000~\u0000\u0000ppq"
+"\u0000~\u0000]sq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005pq\u0000~\u00006q\u0000~\u0000?q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0005Stateq\u0000~\u0000Gq\u0000"
+"~\u0000Csq\u0000~\u00000ppsq\u0000~\u0000\u000bq\u0000~\u00005p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000]sq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005pq\u0000~"
+"\u00006q\u0000~\u0000?q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0007ZipCodeq\u0000~\u0000Gq\u0000~\u0000Csq\u0000~\u00000ppsq\u0000~\u0000\u000bq\u0000~\u00005p\u0000s"
+"q\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xq\u0000~\u0000\u0015t\u00002http://apply."
+"grants.gov/system/UniversalCodes-V1.0t\u0000\u000fCountryCodeTypeq\u0000~\u0000;"
+"\u0000\u0000sr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~"
+"\u0000(t\u0000\u0005tokenq\u0000~\u0000;\u0001q\u0000~\u0000~t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095"
+"\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0002\u0000?@\u0000\u0000\u0000\u0000\u0000\u00eft\u0000\u0003PANt\u0000\u0003DEUt\u0000\u0003ATFt\u0000\u0003QATt\u0000\u0003LBRt\u0000\u0003SVKt"
+"\u0000\u0003SGPt\u0000\u0003CAFt\u0000\u0003CHLt\u0000\u0003TUNt\u0000\u0003SLVt\u0000\u0003SDNt\u0000\u0003DMAt\u0000\u0003BLRt\u0000\u0003ROUt\u0000\u0003KAZt"
+"\u0000\u0003CPVt\u0000\u0003MDGt\u0000\u0003PSEt\u0000\u0003SYCt\u0000\u0003BWAt\u0000\u0003KENt\u0000\u0003BLZt\u0000\u0003TKLt\u0000\u0003GRLt\u0000\u0003CHNt"
+"\u0000\u0003SGSt\u0000\u0003ETHt\u0000\u0003UGAt\u0000\u0003TKMt\u0000\u0003WSMt\u0000\u0003AFGt\u0000\u0003BDIt\u0000\u0003SURt\u0000\u0003MYSt\u0000\u0003PLWt"
+"\u0000\u0003MCOt\u0000\u0003SWZt\u0000\u0003MLTt\u0000\u0003MYTt\u0000\u0003DJIt\u0000\u0003BVTt\u0000\u0003IRNt\u0000\u0003AREt\u0000\u0003LVAt\u0000\u0003KHMt"
+"\u0000\u0003CUBt\u0000\u0003KWTt\u0000\u0003BTNt\u0000\u0003GHAt\u0000\u0003TURt\u0000\u0003VIRt\u0000\u0003IDNt\u0000\u0003VATt\u0000\u0003CXRt\u0000\u0003GMBt"
+"\u0000\u0003NPLt\u0000\u0003LCAt\u0000\u0003IRQt\u0000\u0003NRUt\u0000\u0003VGBt\u0000\u0003NLDt\u0000\u0003MOZt\u0000\u0003FROt\u0000\u0003KIRt\u0000\u0003STPt"
+"\u0000\u0003GIBt\u0000\u0003HTIt\u0000\u0003USAt\u0000\u0003GNQt\u0000\u0003ARMt\u0000\u0003MHLt\u0000\u0003PRTt\u0000\u0003GUMt\u0000\u0003AUSt\u0000\u0003SJMt"
+"\u0000\u0003FSMt\u0000\u0003PRIt\u0000\u0003TTOt\u0000\u0003CHEt\u0000\u0003MDAt\u0000\u0003BMUt\u0000\u0003CANt\u0000\u0003IRLt\u0000\u0003NZLt\u0000\u0003BRBt"
+"\u0000\u0003BENt\u0000\u0003MARt\u0000\u0003MMRt\u0000\u0003BHRt\u0000\u0003HMDt\u0000\u0003ATAt\u0000\u0003AIAt\u0000\u0003BHSt\u0000\u0003ATGt\u0000\u0003SPMt"
+"\u0000\u0003LAOt\u0000\u0003LTUt\u0000\u0003GLPt\u0000\u0003BRAt\u0000\u0003ZAFt\u0000\u0003BRNt\u0000\u0003GBRt\u0000\u0003LKAt\u0000\u0003PRKt\u0000\u0003VENt"
+"\u0000\u0003ALBt\u0000\u0003GTMt\u0000\u0003ARGt\u0000\u0003ZWEt\u0000\u0003PCNt\u0000\u0003EGYt\u0000\u0003SOMt\u0000\u0003ANTt\u0000\u0003GUYt\u0000\u0003FJIt"
+"\u0000\u0003SWEt\u0000\u0003SYRt\u0000\u0003SHNt\u0000\u0003KORt\u0000\u0003LBYt\u0000\u0003ESTt\u0000\u0003YEMt\u0000\u0003WLFt\u0000\u0003CCKt\u0000\u0003TGOt"
+"\u0000\u0003NFKt\u0000\u0003PYFt\u0000\u0003ISLt\u0000\u0003ESHt\u0000\u0003AZEt\u0000\u0003CIVt\u0000\u0003COGt\u0000\u0003PNGt\u0000\u0003MSRt\u0000\u0003KNAt"
+"\u0000\u0003PRYt\u0000\u0003ANDt\u0000\u0003TZAt\u0000\u0003UKRt\u0000\u0003VCTt\u0000\u0003COKt\u0000\u0003THAt\u0000\u0003NORt\u0000\u0003NAMt\u0000\u0003PHLt"
+"\u0000\u0003RUSt\u0000\u0003COLt\u0000\u0003MTQt\u0000\u0003LBNt\u0000\u0003SLEt\u0000\u0003NIUt\u0000\u0003NICt\u0000\u0003MUSt\u0000\u0003PERt\u0000\u0003BFAt"
+"\u0000\u0003TCDt\u0000\u0003AGOt\u0000\u0003MLIt\u0000\u0003UMIt\u0000\u0003GABt\u0000\u0003ITAt\u0000\u0003GNBt\u0000\u0003BOLt\u0000\u0003BELt\u0000\u0003HRVt"
+"\u0000\u0003FINt\u0000\u0003MNGt\u0000\u0003MACt\u0000\u0003REUt\u0000\u0003CODt\u0000\u0003MKDt\u0000\u0003ECUt\u0000\u0003SLBt\u0000\u0003BGDt\u0000\u0003CMRt"
+"\u0000\u0003GEOt\u0000\u0003TONt\u0000\u0003IOTt\u0000\u0003MEXt\u0000\u0003BIHt\u0000\u0003AUTt\u0000\u0003TCAt\u0000\u0003RWAt\u0000\u0003FLKt\u0000\u0003FXXt"
+"\u0000\u0003DNKt\u0000\u0003GUFt\u0000\u0003LIEt\u0000\u0003ISRt\u0000\u0003VUTt\u0000\u0003NERt\u0000\u0003DZAt\u0000\u0003LUXt\u0000\u0003FRAt\u0000\u0003SENt"
+"\u0000\u0003SAUt\u0000\u0003LSOt\u0000\u0003CRIt\u0000\u0003KGZt\u0000\u0003MRTt\u0000\u0003MNPt\u0000\u0003PAKt\u0000\u0003INDt\u0000\u0003GINt\u0000\u0003YUGt"
+"\u0000\u0003ASMt\u0000\u0003TUVt\u0000\u0003HKGt\u0000\u0003ERIt\u0000\u0003TWNt\u0000\u0003SMRt\u0000\u0003JPNt\u0000\u0003TLSt\u0000\u0003OMNt\u0000\u0003MDVt"
+"\u0000\u0003MWIt\u0000\u0003ABWt\u0000\u0003JAMt\u0000\u0003ZMBt\u0000\u0003DOMt\u0000\u0003GRDt\u0000\u0003BGRt\u0000\u0003CYPt\u0000\u0003HNDt\u0000\u0003VNMt"
+"\u0000\u0003UZBt\u0000\u0003CYMt\u0000\u0003CZEt\u0000\u0003URYt\u0000\u0003GRCt\u0000\u0003POLt\u0000\u0003ESPt\u0000\u0003TJKt\u0000\u0003JORt\u0000\u0003COMt"
+"\u0000\u0003NGAt\u0000\u0003SVNt\u0000\u0003HUNxq\u0000~\u0000-sq\u0000~\u0000.q\u0000~\u0000|q\u0000~\u0000{sq\u0000~\u00000ppsq\u0000~\u00002q\u0000~\u00005pq"
+"\u0000~\u00006q\u0000~\u0000?q\u0000~\u0000Csq\u0000~\u0000=t\u0000\u0007Countryq\u0000~\u0000Gq\u0000~\u0000Csr\u0000\"com.sun.msv.gram"
+"mar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gram"
+"mar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expr"
+"essionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006p"
+"arentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0019\u0001pq\u0000~\u0000\tq\u0000~"
+"\u0000Zq\u0000~\u0000fq\u0000~\u0000mq\u0000~\u0000\u000fq\u0000~\u0000Jq\u0000~\u0000tq\u0000~\u0000Pq\u0000~\u0000Hq\u0000~\u0000\bq\u0000~\u0000\\q\u0000~\u0000hq\u0000~\u0000oq\u0000~"
+"\u00001q\u0000~\u0000Kq\u0000~\u0000Vq\u0000~\u0000bq\u0000~\u0000iq\u0000~\u0000pq\u0000~\u0001sq\u0000~\u0000\u0007q\u0000~\u0000\u0005q\u0000~\u0000\nq\u0000~\u0000vq\u0000~\u0000\u0006x"));
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
            return gov.grants.apply.system.globallibrary_v1.impl.AddressDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  21 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  18 :
                        if (("Country" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  3 :
                        if (("Street2" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  0 :
                        if (("Street1" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("State" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        if (("ZipCode" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  6 :
                        if (("City" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("County" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
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
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  17 :
                        if (("ZipCode" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("County" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  20 :
                        if (("Country" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("City" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("Street2" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  14 :
                        if (("State" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  2 :
                        if (("Street1" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
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
                    case  21 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
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
                    case  21 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
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
                        case  19 :
                            state = 20;
                            eatText1(value);
                            return ;
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  16 :
                            state = 17;
                            eatText2(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText3(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText4(value);
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  10 :
                            state = 11;
                            eatText5(value);
                            return ;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText6(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText7(value);
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
                _Country = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ZipCode = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _State = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Street2 = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _County = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Street1 = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _City = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
