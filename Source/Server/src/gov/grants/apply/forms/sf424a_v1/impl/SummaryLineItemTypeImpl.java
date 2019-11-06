//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class SummaryLineItemTypeImpl implements gov.grants.apply.forms.sf424a_v1.SummaryLineItemType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.math.BigDecimal _BudgetFederalEstimatedUnobligatedAmount;
    protected java.lang.String _ActivityTitle;
    protected java.lang.String _CFDANumber;
    protected java.math.BigDecimal _BudgetTotalNewOrRevisedAmount;
    protected java.math.BigDecimal _BudgetFederalNewOrRevisedAmount;
    protected java.math.BigDecimal _BudgetNonFederalNewOrRevisedAmount;
    protected java.math.BigDecimal _BudgetNonFederalEstimatedUnobligatedAmount;
    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.SummaryLineItemType.class);
    }

    public java.math.BigDecimal getBudgetFederalEstimatedUnobligatedAmount() {
        return _BudgetFederalEstimatedUnobligatedAmount;
    }

    public void setBudgetFederalEstimatedUnobligatedAmount(java.math.BigDecimal value) {
        _BudgetFederalEstimatedUnobligatedAmount = value;
    }

    public java.lang.String getActivityTitle() {
        return _ActivityTitle;
    }

    public void setActivityTitle(java.lang.String value) {
        _ActivityTitle = value;
    }

    public java.lang.String getCFDANumber() {
        return _CFDANumber;
    }

    public void setCFDANumber(java.lang.String value) {
        _CFDANumber = value;
    }

    public java.math.BigDecimal getBudgetTotalNewOrRevisedAmount() {
        return _BudgetTotalNewOrRevisedAmount;
    }

    public void setBudgetTotalNewOrRevisedAmount(java.math.BigDecimal value) {
        _BudgetTotalNewOrRevisedAmount = value;
    }

    public java.math.BigDecimal getBudgetFederalNewOrRevisedAmount() {
        return _BudgetFederalNewOrRevisedAmount;
    }

    public void setBudgetFederalNewOrRevisedAmount(java.math.BigDecimal value) {
        _BudgetFederalNewOrRevisedAmount = value;
    }

    public java.math.BigDecimal getBudgetNonFederalNewOrRevisedAmount() {
        return _BudgetNonFederalNewOrRevisedAmount;
    }

    public void setBudgetNonFederalNewOrRevisedAmount(java.math.BigDecimal value) {
        _BudgetNonFederalNewOrRevisedAmount = value;
    }

    public java.math.BigDecimal getBudgetNonFederalEstimatedUnobligatedAmount() {
        return _BudgetNonFederalEstimatedUnobligatedAmount;
    }

    public void setBudgetNonFederalEstimatedUnobligatedAmount(java.math.BigDecimal value) {
        _BudgetNonFederalEstimatedUnobligatedAmount = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.SummaryLineItemTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_CFDANumber!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "CFDANumber");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CFDANumber), "CFDANumber");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetFederalEstimatedUnobligatedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetFederalEstimatedUnobligatedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetFederalEstimatedUnobligatedAmount)), "BudgetFederalEstimatedUnobligatedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetNonFederalEstimatedUnobligatedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetNonFederalEstimatedUnobligatedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetNonFederalEstimatedUnobligatedAmount)), "BudgetNonFederalEstimatedUnobligatedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetFederalNewOrRevisedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetFederalNewOrRevisedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetFederalNewOrRevisedAmount)), "BudgetFederalNewOrRevisedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetNonFederalNewOrRevisedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetNonFederalNewOrRevisedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetNonFederalNewOrRevisedAmount)), "BudgetNonFederalNewOrRevisedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_BudgetTotalNewOrRevisedAmount!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetTotalNewOrRevisedAmount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _BudgetTotalNewOrRevisedAmount)), "BudgetTotalNewOrRevisedAmount");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
        try {
            context.text(((java.lang.String) _ActivityTitle), "ActivityTitle");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/SF424A-V1.0", null, true);
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.sf424a_v1.SummaryLineItemType.class);
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
+"sv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype.xsd.MaxL"
+"engthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xs"
+"d.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv"
+".datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012"
+"needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XS"
+"DatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Con"
+"creteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.d"
+"atatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001dL\u0000\b"
+"typeNameq\u0000~\u0000\u001dL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/White"
+"SpaceProcessor;xpt\u0000*http://apply.grants.gov/system/Global-V1"
+".0t\u0000\u0013StringMin1Max15Typesr\u00005com.sun.msv.datatype.xsd.WhiteSp"
+"aceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd"
+".WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype."
+"xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0019q\u0000~\u0000!q\u0000~\u0000\"q\u0000~"
+"\u0000%\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risA"
+"lwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u001et\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000%\u0001q\u0000~\u0000+"
+"t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000+t\u0000\tmaxLength\u0000\u0000\u0000\u000fsr\u00000com.sun.msv.grammar"
+".Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun."
+"msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespaceU"
+"RIq\u0000~\u0000\u001dxpq\u0000~\u0000\"q\u0000~\u0000!sq\u0000~\u0000\u000bppsr\u0000 com.sun.msv.grammar.Attribute"
+"Exp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000exq\u0000~\u0000\u0003q\u0000~\u0000\u0012psq\u0000~\u0000\u0014"
+"ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~"
+"\u0000,t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$C"
+"ollapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000:q\u0000~\u0000,sr\u0000#com.sun.msv"
+".grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnames"
+"paceURIq\u0000~\u0000\u001dxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000"
+"\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun"
+".msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq"
+"\u0000~\u0000\u0011\u0001q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\nCFDANumbert\u0000)http://apply.grants.gov/form"
+"s/SF424A-V1.0q\u0000~\u0000Dsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0014ppsr\u0000,c"
+"om.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005sca"
+"lexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstraint"
+"FacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000!t\u0000\u001bDecimalMin1Max14Places2Typeq\u0000~"
+"\u0000<\u0000\u0000sr\u0000)com.sun.msv.datatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"I\u0000\tprecisionxq\u0000~\u0000Nq\u0000~\u0000!q\u0000~\u0000Pq\u0000~\u0000<\u0000\u0000sr\u0000#com.sun.msv.datatype."
+"xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0007decimalq\u0000~\u0000<q\u0000~\u0000Tt\u0000\u000bt"
+"otalDigits\u0000\u0000\u0000\u000eq\u0000~\u0000Tt\u0000\u000efractionDigits\u0000\u0000\u0000\u0002q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000Pq\u0000~\u0000"
+"!sq\u0000~\u0000\u000bppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\'BudgetFederalEs"
+"timatedUnobligatedAmountq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~\u0000\u0012p\u0000sq\u0000~\u0000"
+"\u0000ppq\u0000~\u0000Lsq\u0000~\u0000\u000bppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000*BudgetNo"
+"nFederalEstimatedUnobligatedAmountq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000"
+"~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Lsq\u0000~\u0000\u000bppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t"
+"\u0000\u001fBudgetFederalNewOrRevisedAmountq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq\u0000~"
+"\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Lsq\u0000~\u0000\u000bppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000"
+"\"BudgetNonFederalNewOrRevisedAmountq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000bppsq\u0000~\u0000\rq"
+"\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Lsq\u0000~\u0000\u000bppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>"
+"t\u0000\u001dBudgetTotalNewOrRevisedAmountq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u00005ppsq\u0000~\u0000\u0014ppsq"
+"\u0000~\u0000\u0018q\u0000~\u0000!t\u0000\u0014StringMin1Max120Typeq\u0000~\u0000%\u0000\u0001sq\u0000~\u0000&q\u0000~\u0000!q\u0000~\u0000|q\u0000~\u0000%"
+"\u0000\u0000q\u0000~\u0000+q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0001q\u0000~\u0000+q\u0000~\u0000/\u0000\u0000\u0000xq\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000|q\u0000~\u0000!sq\u0000~"
+"\u0000>t\u0000\ractivityTitleq\u0000~\u0000Hsr\u0000\"com.sun.msv.grammar.ExpressionPoo"
+"l\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPoo"
+"l$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Closed"
+"Hash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/"
+"msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0018\u0001pq\u0000~\u0000\bq\u0000~\u0000\tq\u0000~\u0000\u0006q\u0000~\u0000\u0013q\u0000~\u00004"
+"q\u0000~\u0000Yq\u0000~\u0000`q\u0000~\u0000gq\u0000~\u0000nq\u0000~\u0000uq\u0000~\u0000Iq\u0000~\u0000]q\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000rq\u0000~\u0000Kq\u0000~\u0000_"
+"q\u0000~\u0000fq\u0000~\u0000mq\u0000~\u0000tq\u0000~\u0000\u0005q\u0000~\u0000\fq\u0000~\u0000\u0007q\u0000~\u0000\nx"));
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
            return gov.grants.apply.forms.sf424a_v1.impl.SummaryLineItemTypeImpl.this;
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
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  12 :
                        if (("BudgetFederalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  3 :
                        if (("CFDANumber" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  18 :
                        if (("BudgetTotalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  15 :
                        if (("BudgetNonFederalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  6 :
                        if (("BudgetFederalEstimatedUnobligatedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        if (("BudgetNonFederalEstimatedUnobligatedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
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

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ActivityTitle = value;
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
                    case  21 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  5 :
                        if (("CFDANumber" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  17 :
                        if (("BudgetNonFederalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("BudgetTotalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("BudgetFederalEstimatedUnobligatedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("BudgetNonFederalEstimatedUnobligatedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  14 :
                        if (("BudgetFederalNewOrRevisedAmount" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
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
                    case  0 :
                        if (("activityTitle" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  6 :
                        state = 9;
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
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  2 :
                        if (("activityTitle" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  6 :
                        state = 9;
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
                        case  21 :
                            revertToParentFromText(value);
                            return ;
                        case  13 :
                            eatText2(value);
                            state = 14;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  16 :
                            eatText3(value);
                            state = 17;
                            return ;
                        case  7 :
                            eatText4(value);
                            state = 8;
                            return ;
                        case  19 :
                            eatText5(value);
                            state = 20;
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  4 :
                            eatText6(value);
                            state = 5;
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  10 :
                            eatText7(value);
                            state = 11;
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

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetFederalNewOrRevisedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetNonFederalNewOrRevisedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetFederalEstimatedUnobligatedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetTotalNewOrRevisedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CFDANumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetNonFederalEstimatedUnobligatedAmount = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
