//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_modularbudget_v1_1.impl;

public class PHS398ModularBudgetTypeImpl implements gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type _Periods5;
    protected java.lang.String _FormVersion;
    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType _CummulativeBudgetInfo;
    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type _Periods4;
    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type _Periods2;
    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type _Periods3;
    protected gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType _Periods;
    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType.class);
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type getPeriods5() {
        return _Periods5;
    }

    public void setPeriods5(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type value) {
        _Periods5 = value;
    }

    public java.lang.String getFormVersion() {
        return _FormVersion;
    }

    public void setFormVersion(java.lang.String value) {
        _FormVersion = value;
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType getCummulativeBudgetInfo() {
        return _CummulativeBudgetInfo;
    }

    public void setCummulativeBudgetInfo(gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType value) {
        _CummulativeBudgetInfo = value;
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type getPeriods4() {
        return _Periods4;
    }

    public void setPeriods4(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type value) {
        _Periods4 = value;
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type getPeriods2() {
        return _Periods2;
    }

    public void setPeriods2(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type value) {
        _Periods2 = value;
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type getPeriods3() {
        return _Periods3;
    }

    public void setPeriods3(gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type value) {
        _Periods3 = value;
    }

    public gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType getPeriods() {
        return _Periods;
    }

    public void setPeriods(gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType value) {
        _Periods = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PHS398ModularBudgetTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Periods!= null) {
            context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "Periods");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Periods), "Periods");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Periods), "Periods");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Periods), "Periods");
            context.endElement();
        }
        if (_Periods2 != null) {
            context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "Periods2");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Periods2), "Periods2");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Periods2), "Periods2");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Periods2), "Periods2");
            context.endElement();
        }
        if (_Periods3 != null) {
            context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "Periods3");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Periods3), "Periods3");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Periods3), "Periods3");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Periods3), "Periods3");
            context.endElement();
        }
        if (_Periods4 != null) {
            context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "Periods4");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Periods4), "Periods4");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Periods4), "Periods4");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Periods4), "Periods4");
            context.endElement();
        }
        if (_Periods5 != null) {
            context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "Periods5");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Periods5), "Periods5");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Periods5), "Periods5");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Periods5), "Periods5");
            context.endElement();
        }
        context.startElement("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "CummulativeBudgetInfo");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CummulativeBudgetInfo), "CummulativeBudgetInfo");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CummulativeBudgetInfo), "CummulativeBudgetInfo");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _CummulativeBudgetInfo), "CummulativeBudgetInfo");
        context.endElement();
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startAttribute("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "FormVersion");
        try {
            context.text(((java.lang.String) _FormVersion), "FormVersion");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", null, true);
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType.class);
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
+"~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u000bppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000"
+"~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000exq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u00002com.sun.msv.gr"
+"ammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0011\u0001"
+"q\u0000~\u0000\u001csr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom."
+"sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.gramma"
+"r.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001dq\u0000~\u0000\"sr\u0000"
+"#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet"
+"\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000$xq\u0000~\u0000\u001ft\u0000;gov.grants."
+"apply.forms.phs398_modularbudget_v1_1.PeriodTypet\u0000+http://ja"
+"va.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~\u0000\u0012psr\u0000\u001bco"
+"m.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/dat"
+"atype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/Str"
+"ingPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'co"
+"m.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespace"
+"Uriq\u0000~\u0000$L\u0000\btypeNameq\u0000~\u0000$L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatyp"
+"e/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSch"
+"emat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullS"
+"etExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPa"
+"ir\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000$L\u0000\fnamespaceURIq\u0000~\u0000$xpq\u0000~\u00005q\u0000~"
+"\u00004sq\u0000~\u0000#t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instanceq"
+"\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0007Periodst\u00007http://apply.grants.gov/forms/PHS398_"
+"ModularBudget-V1-1q\u0000~\u0000\"sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp"
+"\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u0012psq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000<gov"
+".grants.apply.forms.phs398_modularbudget_v1_1.Period2Typeq\u0000~"
+"\u0000\'sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\bPeriods2q\u0000~\u0000Bq"
+"\u0000~\u0000\"sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000"
+"\u0012psq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000<gov.grants.apply.forms"
+".phs398_modularbudget_v1_1.Period3Typeq\u0000~\u0000\'sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~"
+"\u0000\u0012pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\bPeriods3q\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq"
+"\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u0012psq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000\u001c"
+"q\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000<gov.grants.apply.forms.phs398_modularbudg"
+"et_v1_1.Period4Typeq\u0000~\u0000\'sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"s"
+"q\u0000~\u0000#t\u0000\bPeriods4q\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000"
+"\rpp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u0012psq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000<"
+"gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type"
+"q\u0000~\u0000\'sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\bPeriods5q\u0000~"
+"\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u0012psq\u0000~\u0000\u0019q"
+"\u0000~\u0000\u0012pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000>gov.grants.apply.forms.phs398_m"
+"odularbudget_v1_1.CumBudgetTypeq\u0000~\u0000\'sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0019q\u0000~\u0000\u0012pq\u0000~\u0000"
+"-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0015CummulativeBudgetInfoq\u0000~\u0000Bsq\u0000~\u0000\u0019ppsr\u0000\u001cco"
+"m.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000+L\u0000\u0004nameq\u0000~\u0000,L"
+"\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype"
+".xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.da"
+"tatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*co"
+"m.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFace"
+"tFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/dataty"
+"pe/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatyp"
+"e/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000$xq\u0000~\u00001t\u00001http://apply.gr"
+"ants.gov/system/GlobalLibrary-V2.0t\u0000\u0013FormVersionDataTypesr\u00005"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u00007\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0088q\u0000~\u0000\u008dq\u0000~\u0000\u008eq\u0000~\u0000\u0090\u0000\u0000sr\u0000#com.sun.msv.dat"
+"atype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000/q\u0000~\u00004t\u0000"
+"\u0006stringq\u0000~\u0000\u0090\u0001q\u0000~\u0000\u0094t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000\u0094t\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u0000"
+";q\u0000~\u0000\u008eq\u0000~\u0000\u008dt\u0000\u00031.1sq\u0000~\u0000#t\u0000\u000bFormVersionq\u0000~\u0000Bsr\u0000\"com.sun.msv.gr"
+"ammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gr"
+"ammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Ex"
+"pressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000"
+"\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000#\u0001pq\u0000~\u0000\tq"
+"\u0000~\u0000\nq\u0000~\u0000\bq\u0000~\u0000(q\u0000~\u0000Lq\u0000~\u0000Yq\u0000~\u0000fq\u0000~\u0000sq\u0000~\u0000\u007fq\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000\u0007q\u0000~\u0000\fq"
+"\u0000~\u0000Cq\u0000~\u0000Pq\u0000~\u0000]q\u0000~\u0000jq\u0000~\u0000\u0013q\u0000~\u0000Eq\u0000~\u0000Rq\u0000~\u0000_q\u0000~\u0000\u0015q\u0000~\u0000Gq\u0000~\u0000Tq\u0000~\u0000aq"
+"\u0000~\u0000nq\u0000~\u0000lq\u0000~\u0000zq\u0000~\u0000xq\u0000~\u0000\u0018q\u0000~\u0000Hq\u0000~\u0000Uq\u0000~\u0000bq\u0000~\u0000oq\u0000~\u0000{x"));
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
            return gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PHS398ModularBudgetTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  12 :
                        if (("Periods4" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  13 :
                        if (("BudgetPeriodStartDate4" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods4 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl.class), 14, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriodEndDate4" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods4 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl.class), 14, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriod4" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods4 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period4TypeImpl.class), 14, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  6 :
                        if (("Periods2" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  19 :
                        if (("EntirePeriodTotalCost" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _CummulativeBudgetInfo = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.CumBudgetTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.CumBudgetTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  10 :
                        if (("BudgetPeriodStartDate3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods3 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriodEndDate3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods3 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriod3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods3 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period3TypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  15 :
                        if (("Periods5" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  16 :
                        if (("BudgetPeriodStartDate5" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods5 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl.class), 17, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriodEndDate5" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods5 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl.class), 17, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriod5" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods5 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period5TypeImpl.class), 17, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  18 :
                        if (("CummulativeBudgetInfo" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Periods3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  21 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("Periods" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  4 :
                        if (("BudgetPeriodStartDate" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriodEndDate" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriod" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.PeriodTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("BudgetPeriodStartDate2" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods2 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriodEndDate2" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods2 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("BudgetPeriod2" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            _Periods2 = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.phs398_modularbudget_v1_1.impl.Period2TypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FormVersion = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        public void leaveElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  12 :
                        state = 15;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  20 :
                        if (("CummulativeBudgetInfo" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("Periods2" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  17 :
                        if (("Periods5" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  5 :
                        if (("Periods" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("Periods4" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("Periods3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
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
                    case  0 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  21 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
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
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  21 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
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
                        case  12 :
                            state = 15;
                            continue outer;
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ModularBudget-V1-1", "FormVersion");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
