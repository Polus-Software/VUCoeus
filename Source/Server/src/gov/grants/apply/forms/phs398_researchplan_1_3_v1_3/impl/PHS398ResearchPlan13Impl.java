//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.19 at 02:55:10 EDT 
//


package gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl;

public class PHS398ResearchPlan13Impl
    extends gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.PHS398ResearchPlan13TypeImpl
    implements gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.PHS398ResearchPlan13, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.PHS398ResearchPlan13 .class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "PHS398_ResearchPlan_1_3";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.PHS398ResearchPlan13Impl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3", "PHS398_ResearchPlan_1_3");
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
        return (gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.PHS398ResearchPlan13 .class);
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
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq"
+"\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun."
+"msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000"
+"~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0015psr\u00002com.sun.msv.grammar.Expression$AnyStringE"
+"xpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0014\u0001q\u0000~\u0000\u0019sr\u0000 com.sun.msv.gramma"
+"r.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fname"
+"spaceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u0000`gov.grants.apply.forms.phs398_research"
+"plan_1_3_v1_3.PHS398ResearchPlan13Type.ApplicationTypeTypet\u0000"
+"+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u000fppsq\u0000~\u0000\u0016q\u0000"
+"~\u0000\u0015psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/r"
+"elaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/ms"
+"v/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.Qname"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000"
+"\fnamespaceUriq\u0000~\u0000!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/m"
+"sv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2"
+"001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpace"
+"Processor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Wh"
+"iteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expres"
+"sion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.uti"
+"l.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!"
+"xpq\u0000~\u00002q\u0000~\u00001sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema"
+"-instanceq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u000fApplicationTypet\u0000:http://apply.grants"
+".gov/forms/PHS398_ResearchPlan_1_3-V1-3sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000\u0000pp\u0000sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000"
+"hgov.grants.apply.forms.phs398_researchplan_1_3_v1_3.PHS398R"
+"esearchPlan13Type.ResearchPlanAttachmentsTypeq\u0000~\u0000$sq\u0000~\u0000\u000fppsq"
+"\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0017ResearchPlanAttachmentsq\u0000~"
+"\u0000?sq\u0000~\u0000\u0016ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000"
+"~\u0000(L\u0000\u0004nameq\u0000~\u0000)L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004ppsr\u0000\'com."
+"sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr"
+"\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\""
+"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lco"
+"m/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom"
+"/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000!xq\u0000~\u0000.t\u0000"
+"1http://apply.grants.gov/system/GlobalLibrary-V2.0t\u0000\u0013FormVer"
+"sionDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00004\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.Min"
+"LengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000Qq\u0000~\u0000Vq\u0000~\u0000Wq\u0000~\u0000Y\u0000\u0000sr\u0000"
+"#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysVa"
+"lidxq\u0000~\u0000,q\u0000~\u00001t\u0000\u0006stringq\u0000~\u0000Y\u0001q\u0000~\u0000]t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000]t\u0000\tma"
+"xLength\u0000\u0000\u0000\u001esq\u0000~\u00008q\u0000~\u0000Wq\u0000~\u0000Vt\u0000\u00031.3sq\u0000~\u0000 t\u0000\u000bFormVersionq\u0000~\u0000?sq"
+"\u0000~\u0000\u000fppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0017PHS398_ResearchPla"
+"n_1_3q\u0000~\u0000?sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L"
+"\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;"
+"xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003"
+"\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/E"
+"xpressionPool;xp\u0000\u0000\u0000\f\u0001pq\u0000~\u0000\tq\u0000~\u0000\rq\u0000~\u0000Aq\u0000~\u0000\u000bq\u0000~\u0000\u0010q\u0000~\u0000Cq\u0000~\u0000%q\u0000~"
+"\u0000Hq\u0000~\u0000eq\u0000~\u0000\nq\u0000~\u0000\u0013q\u0000~\u0000Dx"));
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
            return gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.PHS398ResearchPlan13Impl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("PHS398_ResearchPlan_1_3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3" == ___uri)) {
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("PHS398_ResearchPlan_1_3" == ___local)&&("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3" == ___uri)) {
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.PHS398ResearchPlan13TypeImpl)gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.impl.PHS398ResearchPlan13Impl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS398_ResearchPlan_1_3-V1-3", "FormVersion");
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
