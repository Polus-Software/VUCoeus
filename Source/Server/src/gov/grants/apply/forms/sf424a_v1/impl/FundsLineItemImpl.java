//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class FundsLineItemImpl
    extends gov.grants.apply.forms.sf424a_v1.impl.FundsLineItemTypeImpl
    implements gov.grants.apply.forms.sf424a_v1.FundsLineItem, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.FundsLineItem.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424A-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "FundsLineItem";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.FundsLineItemImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "FundsLineItem");
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
        return (gov.grants.apply.forms.sf424a_v1.FundsLineItem.class);
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
+"q\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq"
+"\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg"
+"/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/"
+"msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000,com.sun.msv.datatype.xsd.Fra"
+"ctionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype."
+"xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xr\u0000*com.sun"
+".msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixe"
+"dZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xs"
+"d/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd"
+"/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.m"
+"sv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000"
+"\u001dL\u0000\btypeNameq\u0000~\u0000\u001dL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/W"
+"hiteSpaceProcessor;xpt\u0000*http://apply.grants.gov/system/Globa"
+"l-V1.0t\u0000\u001bDecimalMin1Max14Places2Typesr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000)com.sun.m"
+"sv.datatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tprecisionxq\u0000~\u0000"
+"\u0019q\u0000~\u0000!q\u0000~\u0000\"q\u0000~\u0000%\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u001et\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007decimalq\u0000~\u0000%q\u0000~\u0000+t\u0000"
+"\u000btotalDigits\u0000\u0000\u0000\u000eq\u0000~\u0000+t\u0000\u000efractionDigits\u0000\u0000\u0000\u0002sr\u00000com.sun.msv.gr"
+"ammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnames"
+"paceURIq\u0000~\u0000\u001dxpq\u0000~\u0000\"q\u0000~\u0000!sq\u0000~\u0000\u000eppsr\u0000 com.sun.msv.grammar.Attr"
+"ibuteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0012ps"
+"q\u0000~\u0000\u0014ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000)q\u0000~\u0000,t\u0000\u0005QNameq\u0000~\u0000%q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000:q\u0000~\u0000,sr\u0000#com.sun.msv.gra"
+"mmar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespace"
+"URIq\u0000~\u0000\u001dxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typ"
+"et\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv"
+".grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0011"
+"\u0001q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0015BudgetFirstYearAmountt\u0000)http://apply.grants.g"
+"ov/forms/SF424A-V1.0q\u0000~\u0000Bsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0017s"
+"q\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0016BudgetSecondYearA"
+"mountq\u0000~\u0000Fq\u0000~\u0000Bsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000eppsq\u0000"
+"~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0015BudgetThirdYearAmountq\u0000~\u0000Fq"
+"\u0000~\u0000Bsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0017sq\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000"
+"~\u00007q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0016BudgetFourthYearAmountq\u0000~\u0000Fq\u0000~\u0000Bsq\u0000~\u00005"
+"ppsq\u0000~\u0000\u0014ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVal"
+"ueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000!t\u0000\u0014StringMin1Max120Ty"
+"pesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFace"
+"t\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000_q\u0000~\u0000!q\u0000~\u0000aq\u0000~\u0000c\u0000\u0000sr\u0000#com.sun.m"
+"sv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000)q"
+"\u0000~\u0000,t\u0000\u0006stringq\u0000~\u0000c\u0001q\u0000~\u0000gt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000gt\u0000\tmaxLength\u0000\u0000\u0000"
+"xq\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000aq\u0000~\u0000!sq\u0000~\u0000<t\u0000\ractivityTitleq\u0000~\u0000Fsq\u0000~\u0000\u000eppsq\u0000"
+"~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\rFundsLineItemq\u0000~\u0000Fsr\u0000\"com.s"
+"un.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/s"
+"un/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.g"
+"rammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstream"
+"VersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000"
+"\u0012\u0001pq\u0000~\u0000\u000bq\u0000~\u0000\tq\u0000~\u00004q\u0000~\u0000Jq\u0000~\u0000Qq\u0000~\u0000Xq\u0000~\u0000nq\u0000~\u0000\rq\u0000~\u0000\u000fq\u0000~\u0000Gq\u0000~\u0000Nq\u0000"
+"~\u0000Uq\u0000~\u0000\nq\u0000~\u0000\fq\u0000~\u0000\u0013q\u0000~\u0000Iq\u0000~\u0000Pq\u0000~\u0000Wx"));
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
            return gov.grants.apply.forms.sf424a_v1.impl.FundsLineItemImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("FundsLineItem" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("FundsLineItem" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
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
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        if (("activityTitle" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424a_v1.impl.FundsLineItemTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.FundsLineItemImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424A-V1.0", "activityTitle");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
