//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.01 at 08:55:08 CDT 
//


package gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl;

public class TotalDataTypeImpl implements gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.TotalDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.math.BigDecimal _TotalFedNonFed;
    protected java.math.BigDecimal _NonFederal;
    protected java.math.BigDecimal _Federal;
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.TotalDataType.class);
    }

    public java.math.BigDecimal getTotalFedNonFed() {
        return _TotalFedNonFed;
    }

    public void setTotalFedNonFed(java.math.BigDecimal value) {
        _TotalFedNonFed = value;
    }

    public java.math.BigDecimal getNonFederal() {
        return _NonFederal;
    }

    public void setNonFederal(java.math.BigDecimal value) {
        _NonFederal = value;
    }

    public java.math.BigDecimal getFederal() {
        return _Federal;
    }

    public void setFederal(java.math.BigDecimal value) {
        _Federal = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.TotalDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2", "Federal");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _Federal)), "Federal");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2", "NonFederal");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _NonFederal)), "NonFederal");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2", "TotalFedNonFed");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _TotalFedNonFed)), "TotalFedNonFed");
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
        return (gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.TotalDataType.class);
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
+"r\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxn"
+"g/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/uti"
+"l/StringPair;xq\u0000~\u0000\u0003ppsr\u0000*com.sun.msv.datatype.xsd.MaxInclusi"
+"veFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.da"
+"tatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*co"
+"m.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFace"
+"tFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/dataty"
+"pe/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatyp"
+"e/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com."
+"sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUr"
+"iq\u0000~\u0000\u0017L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/"
+"xsd/WhiteSpaceProcessor;xpt\u00001http://apply.grants.gov/system/"
+"GlobalLibrary-V2.0t\u0000\u0014BudgetAmountDataTypesr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000*com."
+"sun.msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0011q\u0000~\u0000\u001b"
+"q\u0000~\u0000\u001cq\u0000~\u0000\u001f\u0000\u0000sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWith"
+"LexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001bq\u0000~\u0000\u001cq\u0000~\u0000\u001f\u0000\u0000sr\u0000)"
+"com.sun.msv.datatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tpreci"
+"sionxq\u0000~\u0000#q\u0000~\u0000\u001bq\u0000~\u0000\u001cq\u0000~\u0000\u001f\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.Numb"
+"erType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007decimalq\u0000~"
+"\u0000\u001fq\u0000~\u0000*t\u0000\u000btotalDigits\u0000\u0000\u0000\u000eq\u0000~\u0000*t\u0000\u000efractionDigits\u0000\u0000\u0000\u0002q\u0000~\u0000*t\u0000\fm"
+"inInclusivesr\u0000\u0014java.math.BigDecimalT\u00c7\u0015W\u00f9\u0081(O\u0003\u0000\u0002I\u0000\u0005scaleL\u0000\u0006int"
+"Valt\u0000\u0016Ljava/math/BigInteger;xr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000x"
+"p\u0000\u0000\u0000\u0000sr\u0000\u0014java.math.BigInteger\u008c\u00fc\u009f\u001f\u00a9;\u00fb\u001d\u0003\u0000\u0006I\u0000\bbitCountI\u0000\tbitLen"
+"gthI\u0000\u0013firstNonzeroByteNumI\u0000\flowestSetBitI\u0000\u0006signum[\u0000\tmagnitud"
+"et\u0000\u0002[Bxq\u0000~\u00002\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00fe\u00ff\u00ff\u00ff\u00fe\u0000\u0000\u0000\u0000ur\u0000\u0002[B\u00ac\u00f3\u0017\u00f8\u0006\bT\u00e0\u0002\u0000\u0000xp\u0000\u0000\u0000\u0000xxq\u0000~"
+"\u0000*t\u0000\fmaxInclusivesq\u0000~\u00000\u0000\u0000\u0000\u0002sq\u0000~\u00004\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00fe\u00ff\u00ff\u00ff\u00fe\u0000\u0000\u0000\u0001uq\u0000~\u00007\u0000"
+"\u0000\u0000\u0006Z\u00f3\u0010z?\u00ffxxsr\u00000com.sun.msv.grammar.Expression$NullSetExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d"
+"\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xpq\u0000~\u0000\u001cq\u0000~\u0000\u001bsr\u0000\u001dcom"
+".sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv"
+".grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\b"
+"xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\fppsr"
+"\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000(q\u0000~\u0000+t\u0000"
+"\u0005QNameq\u0000~\u0000\u001fq\u0000~\u0000>sq\u0000~\u0000?q\u0000~\u0000Jq\u0000~\u0000+sr\u0000#com.sun.msv.grammar.Simp"
+"leNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017x"
+"r\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http:"
+"//www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar."
+"Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000E\u0001q\u0000~\u0000Rsq\u0000"
+"~\u0000Lt\u0000\u0007Federalt\u00009http://apply.grants.gov/forms/RR_FedNonFedBu"
+"dget_1_2-V1.2sq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u000fsq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000G"
+"q\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\nNonFederalq\u0000~\u0000Vsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fpps"
+"q\u0000~\u0000\u0010q\u0000~\u0000\u001bt\u0000\u0019BudgetTotalAmountDataTypeq\u0000~\u0000\u001f\u0000\u0001sq\u0000~\u0000 q\u0000~\u0000\u001bq\u0000~\u0000"
+"aq\u0000~\u0000\u001f\u0000\u0000sq\u0000~\u0000\"q\u0000~\u0000\u001bq\u0000~\u0000aq\u0000~\u0000\u001f\u0000\u0000sq\u0000~\u0000%q\u0000~\u0000\u001bq\u0000~\u0000aq\u0000~\u0000\u001f\u0000\u0000q\u0000~\u0000*q"
+"\u0000~\u0000*q\u0000~\u0000-\u0000\u0000\u0000\u000fq\u0000~\u0000*q\u0000~\u0000.\u0000\u0000\u0000\u0002q\u0000~\u0000*q\u0000~\u0000/sq\u0000~\u00000\u0000\u0000\u0000\u0000q\u0000~\u00006xq\u0000~\u0000*q\u0000"
+"~\u00009sq\u0000~\u00000\u0000\u0000\u0000\u0002sq\u0000~\u00004\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00fe\u00ff\u00ff\u00ff\u00fe\u0000\u0000\u0000\u0001uq\u0000~\u00007\u0000\u0000\u0000\u0007\u0003\u008d~\u00a4\u00c6\u007f\u00ffxxq\u0000"
+"~\u0000>sq\u0000~\u0000?q\u0000~\u0000aq\u0000~\u0000\u001bsq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rsq\u0000~\u0000L"
+"t\u0000\u000eTotalFedNonFedq\u0000~\u0000Vsr\u0000\"com.sun.msv.grammar.ExpressionPool"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool"
+"$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedH"
+"ash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/m"
+"sv/grammar/ExpressionPool;xp\u0000\u0000\u0000\b\u0001pq\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000^q\u0000~\u0000\u000bq\u0000~\u0000Xq"
+"\u0000~\u0000Bq\u0000~\u0000Yq\u0000~\u0000jx"));
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
            return gov.grants.apply.forms.rr_fednonfedbudget_1_2_v1.impl.TotalDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("NonFederal" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("TotalFedNonFed" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Federal" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
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
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("NonFederal" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("Federal" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("TotalFedNonFed" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget_1_2-V1.2" == ___uri)) {
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
                        case  7 :
                            eatText1(value);
                            state = 8;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  1 :
                            eatText3(value);
                            state = 2;
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
                _TotalFedNonFed = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NonFederal = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Federal = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
