//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424b_v1.impl;

public class AssurancesImpl
    extends gov.grants.apply.forms.sf424b_v1.impl.AssuranceTypeImpl
    implements gov.grants.apply.forms.sf424b_v1.Assurances, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424b_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424b_v1.Assurances.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424B-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Assurances";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424b_v1.impl.AssurancesImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424B-V1.0", "Assurances");
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
        return (gov.grants.apply.forms.sf424b_v1.Assurances.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.gram"
+"mar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype"
+";L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004"
+"ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tm"
+"axLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstr"
+"aintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWit"
+"hFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbase"
+"Typet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteT"
+"ypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012"
+"Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImp"
+"l\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001aL\u0000\btypeNameq\u0000~\u0000\u001aL\u0000\nwhiteSpac"
+"et\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000*http:"
+"//apply.grants.gov/system/Global-V1.0t\u0000\u0013StringMin1Max30Types"
+"r\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0016q\u0000~\u0000\u001eq\u0000~\u0000\u001fq\u0000~\u0000\"\u0000\u0000sr\u0000#com.sun.msv.datat"
+"ype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv"
+".datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.da"
+"tatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bt\u0000 http://www.w3.org"
+"/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000\"\u0001q\u0000~\u0000(t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000(t\u0000\tm"
+"axLength\u0000\u0000\u0000\u001esr\u00000com.sun.msv.grammar.Expression$NullSetExpres"
+"sion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f"
+"\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001aL\u0000\fnamespaceURIq\u0000~\u0000\u001axpq\u0000~\u0000\u001fq\u0000~\u0000\u001esr\u0000\u001dco"
+"m.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.ms"
+"v.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000"
+"\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0011pps"
+"r\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000&q\u0000~\u0000)t"
+"\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Coll"
+"apse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000.sq\u0000~\u0000/q\u0000~\u0000:q\u0000~\u0000)sr\u0000#com.sun.msv.gr"
+"ammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001aL\u0000\fnamespac"
+"eURIq\u0000~\u0000\u001axr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004ty"
+"pet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.ms"
+"v.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000"
+"5\u0001q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0015FormVersionIdentifierq\u0000~\u0000\u001esq\u0000~\u00001ppsq\u0000~\u00001q\u0000~\u0000"
+"6psq\u0000~\u0000\u0000q\u0000~\u00006p\u0000sq\u0000~\u00001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq"
+"\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u00006psq\u0000~\u00003q\u0000~\u00006psr\u00002com.sun.msv.grammar.Expressi"
+"on$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000Eq\u0000~\u0000Qsr\u0000 com.sun"
+".msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000?q\u0000~\u0000Dsq\u0000~\u0000>t\u00009gov."
+"grants.apply.forms.sf424b_v1.AuthorizedRepresentativet\u0000+http"
+"://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000q\u0000~\u00006p\u0000sq\u0000~\u0000\u0007pp"
+"sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00001ppsq\u0000~\u0000Lq\u0000~\u00006psq\u0000~\u00003q\u0000~\u00006pq\u0000~\u0000Qq\u0000~\u0000Sq\u0000~\u0000Dsq\u0000~"
+"\u0000>t\u0000=gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentati"
+"veTypeq\u0000~\u0000Vsq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0018Autho"
+"rizedRepresentativet\u0000)http://apply.grants.gov/forms/SF424B-V"
+"1.0q\u0000~\u0000Dsq\u0000~\u00001ppsq\u0000~\u0000\u0000q\u0000~\u00006p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0011ppsq\u0000~\u0000\u0015q\u0000~\u0000\u001et\u0000\u0013S"
+"tringMin1Max60Typeq\u0000~\u0000\"\u0000\u0001sq\u0000~\u0000#q\u0000~\u0000\u001eq\u0000~\u0000iq\u0000~\u0000\"\u0000\u0000q\u0000~\u0000(q\u0000~\u0000(q\u0000"
+"~\u0000+\u0000\u0000\u0000\u0001q\u0000~\u0000(q\u0000~\u0000,\u0000\u0000\u0000<q\u0000~\u0000.sq\u0000~\u0000/q\u0000~\u0000iq\u0000~\u0000\u001esq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u0000"
+"6pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0019ApplicantOrganizationNameq\u0000~\u0000cq\u0000~\u0000"
+"Dsq\u0000~\u00001ppsq\u0000~\u0000\u0000q\u0000~\u00006p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0011ppsr\u0000!com.sun.msv.dataty"
+"pe.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateT"
+"imeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000&q\u0000~\u0000)t\u0000\u0004dateq\u0000~\u0000<q\u0000~\u0000.sq\u0000~\u0000/q\u0000~\u0000"
+"wq\u0000~\u0000)sq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\rSubmittedD"
+"ateq\u0000~\u0000cq\u0000~\u0000Dsq\u0000~\u00003ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0012L\u0000\u0004nameq\u0000~\u0000\u0013L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000"
+"\u0004ppq\u0000~\u0000(sq\u0000~\u0000/q\u0000~\u0000*q\u0000~\u0000)t\u0000\u0010Non-Constructionsq\u0000~\u0000>t\u0000\u000bprogramT"
+"ypeq\u0000~\u0000csq\u0000~\u00003ppsq\u0000~\u0000~ppq\u0000~\u0000(q\u0000~\u0000\u0081t\u0000\u00031.0sq\u0000~\u0000>t\u0000\u0011coreSchemaV"
+"ersionq\u0000~\u0000\u001esq\u0000~\u00001ppsq\u0000~\u00003q\u0000~\u00006pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\nAssur"
+"ancesq\u0000~\u0000csr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L"
+"\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;"
+"xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003"
+"\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/E"
+"xpressionPool;xp\u0000\u0000\u0000\u0017\u0001pq\u0000~\u0000Xq\u0000~\u0000\fq\u0000~\u0000pq\u0000~\u0000\rq\u0000~\u0000\u000bq\u0000~\u0000\nq\u0000~\u0000fq\u0000~"
+"\u0000\u0010q\u0000~\u0000Kq\u0000~\u0000Zq\u0000~\u00002q\u0000~\u0000_q\u0000~\u0000lq\u0000~\u0000yq\u0000~\u0000\u008aq\u0000~\u0000Hq\u0000~\u0000rq\u0000~\u0000\u000eq\u0000~\u0000\tq\u0000~"
+"\u0000Iq\u0000~\u0000dq\u0000~\u0000Nq\u0000~\u0000[x"));
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
            return gov.grants.apply.forms.sf424b_v1.impl.AssurancesImpl.this;
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Assurances" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("Assurances" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
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
                        if (("programType" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424b_v1.impl.AssuranceTypeImpl)gov.grants.apply.forms.sf424b_v1.impl.AssurancesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
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
