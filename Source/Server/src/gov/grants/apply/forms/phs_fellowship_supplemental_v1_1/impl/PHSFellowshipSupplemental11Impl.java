//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.30 at 02:44:18 EDT 
//


package gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl;

public class PHSFellowshipSupplemental11Impl
    extends gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl
    implements gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11 .class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "PHS_Fellowship_Supplemental_1_1";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11Impl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "PHS_Fellowship_Supplemental_1_1");
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
        return (gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental11 .class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sr\u0000\u001dcom.sun"
+".msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.gra"
+"mmar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001"
+"Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L"
+"\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0018psr\u00002com.sun.msv.gramm"
+"ar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0017\u0001q\u0000~"
+"\u0000\u001csr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun"
+".msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.E"
+"xpression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001dq\u0000~\u0000\"sr\u0000#co"
+"m.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012L"
+"java/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000$xq\u0000~\u0000\u001ft\u0000kgov.grants.app"
+"ly.forms.phs_fellowship_supplemental_v1_1.PHSFellowshipSuppl"
+"emental11Type.ApplicationTypeTypet\u0000+http://java.sun.com/jaxb"
+"/xjc/dummy-elementssq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018psr\u0000\u001bcom.sun.msv.gramm"
+"ar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;"
+"L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004p"
+"psr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.su"
+"n.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.m"
+"sv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datat"
+"ype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000$L\u0000\btype"
+"Nameq\u0000~\u0000$L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpac"
+"eProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000$L\u0000\fnamespaceURIq\u0000~\u0000$xpq\u0000~\u00005q\u0000~\u00004sq\u0000~\u0000#t\u0000\u0004type"
+"t\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u000fAp"
+"plicationTypet\u0000>http://apply.grants.gov/forms/PHS_Fellowship"
+"_Supplemental-V1-1sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0014q\u0000"
+"~\u0000\u0018psq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000pgov.grants.apply.for"
+"ms.phs_fellowship_supplemental_v1_1.PHSFellowshipSupplementa"
+"l11Type.ResearchTrainingPlanTypeq\u0000~\u0000\'sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~"
+"\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0014ResearchTrainingPlanq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0014q\u0000~\u0000\u0018psq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"s"
+"q\u0000~\u0000#t\u0000qgov.grants.apply.forms.phs_fellowship_supplemental_v"
+"1_1.PHSFellowshipSupplemental11Type.AdditionalInformationTyp"
+"eq\u0000~\u0000\'sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u0015Additional"
+"Informationq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0014q\u0000~\u0000"
+"\u0018psq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000bgov.grants.apply.forms"
+".phs_fellowship_supplemental_v1_1.PHSFellowshipSupplemental1"
+"1Type.BudgetTypeq\u0000~\u0000\'sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~"
+"\u0000#t\u0000\u0006Budgetq\u0000~\u0000Bsq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0018p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0012"
+"ppsq\u0000~\u0000\u0014q\u0000~\u0000\u0018psq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000\u001cq\u0000~\u0000 q\u0000~\u0000\"sq\u0000~\u0000#t\u0000Hgov.grants"
+".apply.system.attachments_v1.AttachmentGroupMin0Max100DataTy"
+"peq\u0000~\u0000\'sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\bAppendixq"
+"\u0000~\u0000Bq\u0000~\u0000\"sq\u0000~\u0000\u0019ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003"
+"L\u0000\u0002dtq\u0000~\u0000+L\u0000\u0004nameq\u0000~\u0000,L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004pps"
+"r\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxL"
+"engthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstrain"
+"tFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTyp"
+"et\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteType"
+"t\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000$x"
+"q\u0000~\u00001t\u00001http://apply.grants.gov/system/GlobalLibrary-V2.0t\u0000\u0013"
+"FormVersionDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00007\u0000\u0001sr\u0000\'com.sun.msv.datatype."
+"xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000yq\u0000~\u0000~q\u0000~\u0000\u007fq\u0000~"
+"\u0000\u0081\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risA"
+"lwaysValidxq\u0000~\u0000/q\u0000~\u00004t\u0000\u0006stringq\u0000~\u0000\u0081\u0001q\u0000~\u0000\u0085t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~"
+"\u0000\u0085t\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u0000;q\u0000~\u0000\u007fq\u0000~\u0000~t\u0000\u00031.1sq\u0000~\u0000#t\u0000\u000bFormVersion"
+"q\u0000~\u0000Bsq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0019q\u0000~\u0000\u0018pq\u0000~\u0000-q\u0000~\u0000=q\u0000~\u0000\"sq\u0000~\u0000#t\u0000\u001fPHS_Fellows"
+"hip_Supplemental_1_1q\u0000~\u0000Bsr\u0000\"com.sun.msv.grammar.ExpressionP"
+"ool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionP"
+"ool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Clos"
+"edHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/su"
+"n/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u001c\u0001pq\u0000~\u0000\tq\u0000~\u0000\u0013q\u0000~\u0000Fq\u0000~\u0000Rq\u0000~"
+"\u0000^q\u0000~\u0000kq\u0000~\u0000\nq\u0000~\u0000\u0010q\u0000~\u0000Dq\u0000~\u0000Pq\u0000~\u0000\\q\u0000~\u0000iq\u0000~\u0000\fq\u0000~\u0000gq\u0000~\u0000\u0016q\u0000~\u0000Gq\u0000~"
+"\u0000Sq\u0000~\u0000(q\u0000~\u0000Kq\u0000~\u0000Wq\u0000~\u0000_q\u0000~\u0000cq\u0000~\u0000lq\u0000~\u0000pq\u0000~\u0000\u008dq\u0000~\u0000\u000bq\u0000~\u0000\rq\u0000~\u0000\u000ex"));
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
            return gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11Impl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("PHS_Fellowship_Supplemental_1_1" == ___local)&&("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "FormVersion");
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
                    case  2 :
                        if (("PHS_Fellowship_Supplemental_1_1" == ___local)&&("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11TypeImpl)gov.grants.apply.forms.phs_fellowship_supplemental_v1_1.impl.PHSFellowshipSupplemental11Impl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/PHS_Fellowship_Supplemental-V1-1", "FormVersion");
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
