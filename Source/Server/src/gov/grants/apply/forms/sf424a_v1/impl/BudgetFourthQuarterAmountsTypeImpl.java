//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class BudgetFourthQuarterAmountsTypeImpl implements gov.grants.apply.forms.sf424a_v1.BudgetFourthQuarterAmountsType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.math.BigDecimal _BudgetTotalForecastedAmount;
    protected java.math.BigDecimal _BudgetNonFederalForecastedAmount;
    protected java.math.BigDecimal _BudgetFederalForecastedAmount;
    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.BudgetFourthQuarterAmountsType.class);
    }

    public java.math.BigDecimal getBudgetTotalForecastedAmount() {
        return _BudgetTotalForecastedAmount;
    }

    public void setBudgetTotalForecastedAmount(java.math.BigDecimal value) {
        _BudgetTotalForecastedAmount = value;
    }

    public java.math.BigDecimal getBudgetNonFederalForecastedAmount() {
        return _BudgetNonFederalForecastedAmount;
    }

    public void setBudgetNonFederalForecastedAmount(java.math.BigDecimal value) {
        _BudgetNonFederalForecastedAmount = value;
    }

    public java.math.BigDecimal getBudgetFederalForecastedAmount() {
        return _BudgetFederalForecastedAmount;
    }

    public void setBudgetFederalForecastedAmount(java.math.BigDecimal value) {
        _BudgetFederalForecastedAmount = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.BudgetFourthQuarterAmountsTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_BudgetFederalForecastedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetFederalForecastedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetFederalForecastedAmount)), "BudgetFederalForecastedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetNonFederalForecastedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetNonFederalForecastedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetNonFederalForecastedAmount)), "BudgetNonFederalForecastedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetTotalForecastedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetTotalForecastedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetTotalForecastedAmount)), "BudgetTotalForecastedAmount");
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
        return (gov.grants.apply.forms.sf424a_v1.BudgetFourthQuarterAmountsType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolea"
+"n\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.Dat"
+"aExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exc"
+"eptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000,c"
+"om.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005sca"
+"lexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraint"
+"FacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFac"
+"et\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseType"
+"t\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet"
+"\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljav"
+"a/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u0019L\u0000\btypeNameq\u0000~\u0000\u0019L\u0000\nwhiteSpacet\u0000."
+"Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000*http://ap"
+"ply.grants.gov/system/Global-V1.0t\u0000\u001bDecimalMin1Max14Places2T"
+"ypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000)com.sun.msv.datatype.xsd.TotalDigitsFacet\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tprecisionxq\u0000~\u0000\u0015q\u0000~\u0000\u001dq\u0000~\u0000\u001eq\u0000~\u0000!\u0000\u0000sr\u0000#com.sun.msv"
+".datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype."
+"xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd"
+".ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001at\u0000 http://www.w3.org/2001/XMLS"
+"chemat\u0000\u0007decimalq\u0000~\u0000!q\u0000~\u0000\'t\u0000\u000btotalDigits\u0000\u0000\u0000\u000eq\u0000~\u0000\'t\u0000\u000efractionD"
+"igits\u0000\u0000\u0000\u0002sr\u00000com.sun.msv.grammar.Expression$NullSetExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002"
+"\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xpq\u0000~\u0000\u001eq\u0000~\u0000\u001dsq\u0000~\u0000\u0007pps"
+"r\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\t"
+"nameClassq\u0000~\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\u000epsq\u0000~\u0000\u0010ppsr\u0000\"com.sun.msv.datatype.x"
+"sd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%q\u0000~\u0000(t\u0000\u0005QNameq\u0000~\u0000!q\u0000~\u0000-sq\u0000~\u0000.q\u0000"
+"~\u00006q\u0000~\u0000(sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xr\u0000\u001dcom.sun.msv.grammar.N"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSch"
+"ema-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpres"
+"sion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\r\u0001q\u0000~\u0000>sq\u0000~\u00008t\u0000\u001dBudgetFederalForec"
+"astedAmountt\u0000)http://apply.grants.gov/forms/SF424A-V1.0q\u0000~\u0000>"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\tq\u0000~\u0000\u000ep\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000\u0007ppsq\u0000~\u00001q\u0000~\u0000\u000epq\u0000~\u00003q"
+"\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000 BudgetNonFederalForecastedAmountq\u0000~\u0000Bq\u0000~\u0000>"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\tq\u0000~\u0000\u000ep\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0013sq\u0000~\u0000\u0007ppsq\u0000~\u00001q\u0000~\u0000\u000epq\u0000~\u00003q"
+"\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u001bBudgetTotalForecastedAmountq\u0000~\u0000Bq\u0000~\u0000>sr\u0000\"c"
+"om.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lc"
+"om/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.m"
+"sv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rst"
+"reamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;x"
+"p\u0000\u0000\u0000\u000b\u0001pq\u0000~\u00000q\u0000~\u0000Fq\u0000~\u0000Mq\u0000~\u0000\u0006q\u0000~\u0000\bq\u0000~\u0000Cq\u0000~\u0000Jq\u0000~\u0000\u0005q\u0000~\u0000\u000fq\u0000~\u0000Eq\u0000~"
+"\u0000Lx"));
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
            return gov.grants.apply.forms.sf424a_v1.impl.BudgetFourthQuarterAmountsTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("BudgetNonFederalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  0 :
                        if (("BudgetFederalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("BudgetTotalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
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
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  5 :
                        if (("BudgetNonFederalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("BudgetFederalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("BudgetTotalForecastedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
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
                        case  3 :
                            state = 6;
                            continue outer;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  9 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            eatText1(value);
                            state = 8;
                            return ;
                        case  1 :
                            eatText2(value);
                            state = 2;
                            return ;
                        case  4 :
                            eatText3(value);
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
                _BudgetTotalForecastedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetFederalForecastedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetNonFederalForecastedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
