//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.11 at 11:55:52 CDT 
//


package gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl;

public class PHS398CumulativeInclusionReportEthnicCategoryDataTypeImpl implements gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportEthnicCategoryDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType _UnknownGender;
    protected gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType _Male;
    protected gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType _Female;
    public final static java.lang.Class version = (gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportEthnicCategoryDataType.class);
    }

    public gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getUnknownGender() {
        return _UnknownGender;
    }

    public void setUnknownGender(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value) {
        _UnknownGender = value;
    }

    public gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getMale() {
        return _Male;
    }

    public void setMale(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value) {
        _Male = value;
    }

    public gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType getFemale() {
        return _Female;
    }

    public void setFemale(gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportRacialCategoryDataType value) {
        _Female = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportEthnicCategoryDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0", "Female");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Female), "Female");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Female), "Female");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Female), "Female");
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0", "Male");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Male), "Male");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Male), "Male");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Male), "Male");
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0", "UnknownGender");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UnknownGender), "UnknownGender");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UnknownGender), "UnknownGender");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _UnknownGender), "UnknownGender");
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
        return (gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportEthnicCategoryDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\u0007pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.m"
+"sv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang"
+".Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attri"
+"buteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\bxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psr"
+"\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u0000\u0017sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003q\u0000~\u0000\u0018q\u0000~\u0000\u001dsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001fxq\u0000~"
+"\u0000\u001at\u0000mgov.grants.apply.forms.cumulative_inclusion_report_v1_0"
+".PHS398CumulativeInclusionReportRacialCategoryDataTypet\u0000+htt"
+"p://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013p"
+"sr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relax"
+"ng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/ut"
+"il/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnam"
+"espaceUriq\u0000~\u0000\u001fL\u0000\btypeNameq\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/d"
+"atatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/"
+"XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProc"
+"essor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression"
+"$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.St"
+"ringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq\u0000~\u0000\u001fxpq\u0000"
+"~\u00000q\u0000~\u0000/sq\u0000~\u0000\u001et\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-ins"
+"tanceq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u0006Femalet\u0000Chttp://apply.grants.gov/forms/PH"
+"S398_CumulativeInclusionReport-V1.0sq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp"
+"\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q"
+"\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u0004Maleq\u0000~\u0000=sq\u0000"
+"~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017"
+"q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001eq\u0000~\u0000!q\u0000~\u0000\"sq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000"
+"\u001dsq\u0000~\u0000\u001et\u0000\rUnknownGenderq\u0000~\u0000=sr\u0000\"com.sun.msv.grammar.Expressi"
+"onPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expressi"
+"onPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$C"
+"losedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom"
+"/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u000e\u0001pq\u0000~\u0000\u0011q\u0000~\u0000Bq\u0000~\u0000Mq\u0000~\u0000\u000e"
+"q\u0000~\u0000Aq\u0000~\u0000Lq\u0000~\u0000\u0005q\u0000~\u0000\u000bq\u0000~\u0000?q\u0000~\u0000Jq\u0000~\u0000#q\u0000~\u0000Eq\u0000~\u0000Pq\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportEthnicCategoryDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            _Male = ((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Female" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        if (("UnknownGender" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("Male" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                    case  7 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            _UnknownGender = ((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  1 :
                        if (("AmericanIndian" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            _Female = ((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.impl.PHS398CumulativeInclusionReportRacialCategoryDataTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
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
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("UnknownGender" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("Male" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("Female" == ___local)&&("http://apply.grants.gov/forms/PHS398_CumulativeInclusionReport-V1.0" == ___uri)) {
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
                    case  9 :
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
                    case  9 :
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
                        case  9 :
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
