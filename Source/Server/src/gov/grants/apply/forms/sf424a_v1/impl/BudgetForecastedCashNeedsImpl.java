//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class BudgetForecastedCashNeedsImpl
    extends gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl
    implements gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424A-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "BudgetForecastedCashNeeds";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "BudgetForecastedCashNeeds");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
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
        return (gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001fcom.sun.msv.gra"
+"mmar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\bppsq\u0000~\u0000\u000esr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003x"
+"q\u0000~\u0000\u0004q\u0000~\u0000\u0012psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000"
+"\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0012psr\u00002com.sun.msv.gramma"
+"r.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0011\u0001q\u0000~\u0000"
+"\u001bsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun."
+"msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001cq\u0000~\u0000!sr\u0000#com"
+".sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Lj"
+"ava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000#xq\u0000~\u0000\u001et\u00007gov.grants.appl"
+"y.forms.sf424a_v1.BudgetFirstYearAmountst\u0000+http://java.sun.c"
+"om/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000;gov.gran"
+"ts.apply.forms.sf424a_v1.BudgetFirstYearAmountsTypeq\u0000~\u0000&sq\u0000~"
+"\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000"
+"\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000"
+"\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.dataty"
+"pe.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Buil"
+"tinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concret"
+"eType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000#L\u0000\btypeNameq\u0000~\u0000#L\u0000\nwhiteSpacet"
+"\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://"
+"www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.gr"
+"ammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000#L\u0000\fnames"
+"paceURIq\u0000~\u0000#xpq\u0000~\u0000<q\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u0004typet\u0000)http://www.w3.org/20"
+"01/XMLSchema-instanceq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0016BudgetFirstYearAmountst\u0000)"
+"http://apply.grants.gov/forms/SF424A-V1.0q\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u000e"
+"q\u0000~\u0000\u0012psq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000"
+"\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000:gov.grants.apply.forms.sf424a_v1.BudgetFirstQ"
+"uarterAmountsq\u0000~\u0000&sq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~"
+"\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000>gov.grants.apply"
+".forms.sf424a_v1.BudgetFirstQuarterAmountsTypeq\u0000~\u0000&sq\u0000~\u0000\u000epps"
+"q\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0019BudgetFirstQuarterAmounts"
+"q\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~"
+"\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000;gov.grants.apply.form"
+"s.sf424a_v1.BudgetSecondQuarterAmountsq\u0000~\u0000&sq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!"
+"sq\u0000~\u0000\"t\u0000?gov.grants.apply.forms.sf424a_v1.BudgetSecondQuarte"
+"rAmountsTypeq\u0000~\u0000&sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000"
+"\u001aBudgetSecondQuarterAmountsq\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000"
+"~\u0000\"t\u0000:gov.grants.apply.forms.sf424a_v1.BudgetThirdQuarterAmo"
+"untsq\u0000~\u0000&sq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012ps"
+"q\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000>gov.grants.apply.forms.sf"
+"424a_v1.BudgetThirdQuarterAmountsTypeq\u0000~\u0000&sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000"
+"\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u0019BudgetThirdQuarterAmountsq\u0000~\u0000Iq\u0000~\u0000"
+"!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018"
+"q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000;gov.grants.apply.forms.sf424a_"
+"v1.BudgetFourthQuarterAmountsq\u0000~\u0000&sq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0015q\u0000~\u0000\u0012psq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u0000\u001bq\u0000~\u0000\u001fq\u0000~\u0000!sq\u0000~\u0000\"t\u0000?"
+"gov.grants.apply.forms.sf424a_v1.BudgetFourthQuarterAmountsT"
+"ypeq\u0000~\u0000&sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000~\u0000!sq\u0000~\u0000\"t\u0000\u001aBudgetFo"
+"urthQuarterAmountsq\u0000~\u0000Iq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0018q\u0000~\u0000\u0012pq\u0000~\u00004q\u0000~\u0000Dq\u0000"
+"~\u0000!sq\u0000~\u0000\"t\u0000\u0019BudgetForecastedCashNeedsq\u0000~\u0000Isr\u0000\"com.sun.msv.gr"
+"ammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gr"
+"ammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Ex"
+"pressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000"
+"\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000.\u0001pq\u0000~\u0000sq"
+"\u0000~\u0000\u0087q\u0000~\u0000\tq\u0000~\u0000\nq\u0000~\u0000/q\u0000~\u0000Zq\u0000~\u0000nq\u0000~\u0000\u0082q\u0000~\u0000\u0096q\u0000~\u0000\u009aq\u0000~\u0000\rq\u0000~\u0000(q\u0000~\u0000Sq"
+"\u0000~\u0000gq\u0000~\u0000{q\u0000~\u0000\u0014q\u0000~\u0000*q\u0000~\u0000Mq\u0000~\u0000Uq\u0000~\u0000aq\u0000~\u0000iq\u0000~\u0000uq\u0000~\u0000}q\u0000~\u0000\u0089q\u0000~\u0000\u0091q"
+"\u0000~\u0000\u008fq\u0000~\u0000\fq\u0000~\u0000\u0017q\u0000~\u0000+q\u0000~\u0000\u000fq\u0000~\u0000Nq\u0000~\u0000Vq\u0000~\u0000Jq\u0000~\u0000bq\u0000~\u0000jq\u0000~\u0000^q\u0000~\u0000vq"
+"\u0000~\u0000~q\u0000~\u0000rq\u0000~\u0000\u008aq\u0000~\u0000\u0092q\u0000~\u0000\u0086q\u0000~\u0000\u000bq\u0000~\u0000\u0010q\u0000~\u0000Kq\u0000~\u0000_x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("BudgetFirstYearAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetFirstYearAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetFirstQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetFirstQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetSecondQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetSecondQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetThirdQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetThirdQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetFourthQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("BudgetFourthQuarterAmounts" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("BudgetForecastedCashNeeds" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
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
                    case  2 :
                        if (("BudgetForecastedCashNeeds" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        spawnHandlerFromLeaveElement((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
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
                    case  1 :
                        spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
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
                    case  1 :
                        spawnHandlerFromLeaveAttribute((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
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
                        case  1 :
                            spawnHandlerFromText((((gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.BudgetForecastedCashNeedsImpl.this).new Unmarshaller(context)), 2, value);
                            return ;
                        case  3 :
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
