//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.impl;

public class NonFederalResourcesImpl
    extends gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl
    implements gov.grants.apply.forms.sf424a_v1.NonFederalResources, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424a_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424a_v1.NonFederalResources.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424A-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "NonFederalResources";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424A-V1.0", "NonFederalResources");
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
        return (gov.grants.apply.forms.sf424a_v1.NonFederalResources.class);
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
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.s"
+"un.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0007sr\u0000\u0011java.la"
+"ng.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u000bq\u0000~\u0000\u000fpsq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq"
+"\u0000~\u0000\u000bppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom"
+".sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000\u000fps"
+"r\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\t"
+"nameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u000fpsr\u00002com.sun.msv.grammar.Expression"
+"$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u000e\u0001q\u0000~\u0000\u0019sr\u0000 com.sun"
+".msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar."
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$Eps"
+"ilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.gra"
+"mmar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/Str"
+"ing;L\u0000\fnamespaceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u00001gov.grants.apply.forms.sf42"
+"4a_v1.ResourceLineItemt\u0000+http://java.sun.com/jaxb/xjc/dummy-"
+"elementssq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq"
+"\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u00005gov.grants.apply.forms.sf4"
+"24a_v1.ResourceLineItemTypeq\u0000~\u0000$sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpsr\u0000\u001bcom."
+"sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datat"
+"ype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/Strin"
+"gPair;xq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com."
+"sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUr"
+"iq\u0000~\u0000!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/"
+"xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchem"
+"at\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Co"
+"llapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProc"
+"essor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSet"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair"
+"\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xpq\u0000~\u0000:q\u0000~\u00009"
+"sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~"
+"\u0000\u001fsq\u0000~\u0000 t\u0000\u0010ResourceLineItemt\u0000)http://apply.grants.gov/forms/"
+"SF424A-V1.0sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0007q\u0000~\u0000\u000fpsq\u0000~\u0000\u000bq\u0000~\u0000\u000fpsq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~"
+"\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000#q\u0000~\u0000$s"
+"q\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000f"
+"pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000,q\u0000~\u0000$sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u00002q\u0000~"
+"\u0000Bq\u0000~\u0000\u001fq\u0000~\u0000Esq\u0000~\u0000\u000bppsq\u0000~\u0000\u0007q\u0000~\u0000\u000fpsq\u0000~\u0000\u000bq\u0000~\u0000\u000fpsq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000"
+"~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000#q\u0000~\u0000$"
+"sq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000"
+"\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000,q\u0000~\u0000$sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u00002q\u0000"
+"~\u0000Bq\u0000~\u0000\u001fq\u0000~\u0000Esq\u0000~\u0000\u000bppsq\u0000~\u0000\u000bq\u0000~\u0000\u000fpsq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013"
+"q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000#q\u0000~\u0000$sq\u0000~\u0000\u0000q\u0000~\u0000\u000f"
+"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000"
+"\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 q\u0000~\u0000,q\u0000~\u0000$sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fq\u0000~"
+"\u0000Eq\u0000~\u0000\u001fq\u0000~\u0000\u001fq\u0000~\u0000\u001fq\u0000~\u0000\u001fsq\u0000~\u0000\u000bppsq\u0000~\u0000\u000bq\u0000~\u0000\u000fpsq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~\u0000"
+"\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000/gov.grant"
+"s.apply.forms.sf424a_v1.ResourceTotalsq\u0000~\u0000$sq\u0000~\u0000\u0000q\u0000~\u0000\u000fp\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001f"
+"sq\u0000~\u0000 t\u00003gov.grants.apply.forms.sf424a_v1.ResourceTotalsType"
+"q\u0000~\u0000$sq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u000eResourceTot"
+"alsq\u0000~\u0000Gq\u0000~\u0000\u001fsq\u0000~\u0000\u000bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0013Non"
+"FederalResourcesq\u0000~\u0000Gsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$"
+"ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHa"
+"sh\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/ms"
+"v/grammar/ExpressionPool;xp\u0000\u0000\u0000.\u0001pq\u0000~\u0000kq\u0000~\u0000{q\u0000~\u0000Iq\u0000~\u0000\fq\u0000~\u0000\tq\u0000"
+"~\u0000-q\u0000~\u0000Wq\u0000~\u0000hq\u0000~\u0000xq\u0000~\u0000\u008aq\u0000~\u0000\u008eq\u0000~\u0000\nq\u0000~\u0000&q\u0000~\u0000Qq\u0000~\u0000bq\u0000~\u0000rq\u0000~\u0000\u0012q\u0000"
+"~\u0000(q\u0000~\u0000Lq\u0000~\u0000Sq\u0000~\u0000]q\u0000~\u0000dq\u0000~\u0000mq\u0000~\u0000tq\u0000~\u0000}q\u0000~\u0000\u0085q\u0000~\u0000\u0083q\u0000~\u0000Zq\u0000~\u0000\u0015q\u0000"
+"~\u0000)q\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000^q\u0000~\u0000eq\u0000~\u0000nq\u0000~\u0000uq\u0000~\u0000jq\u0000~\u0000~q\u0000~\u0000Hq\u0000~\u0000\u0086q\u0000~\u0000zq\u0000"
+"~\u0000\rq\u0000~\u0000Yq\u0000~\u0000\u0010q\u0000~\u0000Jq\u0000~\u0000[x"));
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
            return gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("ResourceLineItem" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ResourceLineItem" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ResourceTotals" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ResourceTotals" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("NonFederalResources" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
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
                        if (("NonFederalResources" == ___local)&&("http://apply.grants.gov/forms/SF424A-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        spawnHandlerFromLeaveElement((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        spawnHandlerFromLeaveAttribute((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                            spawnHandlerFromText((((gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesTypeImpl)gov.grants.apply.forms.sf424a_v1.impl.NonFederalResourcesImpl.this).new Unmarshaller(context)), 2, value);
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
