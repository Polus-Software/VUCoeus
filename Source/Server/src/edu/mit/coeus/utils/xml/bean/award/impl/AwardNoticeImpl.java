//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award.impl;

public class AwardNoticeImpl
    extends edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl
    implements edu.mit.coeus.utils.xml.bean.award.AwardNotice, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.award.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.award.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.award.AwardNotice.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "AwardNotice";
    }

    public edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "AwardNotice");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.award.AwardNotice.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valu"
+"exp\u0000p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000fppsr\u0000 com.sun.msv.grammar.OneOr"
+"MoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000\u0013psr\u0000 com.sun.msv.grammar.AttributeExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0013psr\u00002com.s"
+"un.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0004sq\u0000~\u0000\u0012\u0001psr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+"\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.g"
+"rammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001epsr"
+"\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalName"
+"t\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000%xq\u0000~\u0000 t\u00001edu.mit.co"
+"eus.utils.xml.bean.award.SchoolInfoTypet\u0000+http://java.sun.co"
+"m/jaxb/xjc/dummy-elementssq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013psr\u0000\u001bcom.sun.msv"
+".grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Dat"
+"atype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;x"
+"q\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*"
+"com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com"
+".sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv"
+".datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000%L"
+"\u0000\btypeNameq\u0000~\u0000%L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Whi"
+"teSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNa"
+"mesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d"
+"\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L\u0000\fnamespaceURIq\u0000~\u0000%xpq\u0000~\u00006q\u0000~\u00005sq\u0000~\u0000$t"
+"\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000#sq\u0000~\u0000"
+"$t\u0000\nSchoolInfot\u0000\u0000q\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0017q\u0000~\u0000\u0013psq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u00006edu."
+"mit.coeus.utils.xml.bean.award.AwardDisclosureTypeq\u0000~\u0000(sq\u0000~\u0000"
+"\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000fAwardDisclosureq\u0000~\u0000Cq"
+"\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0017q\u0000~\u0000"
+"\u0013psq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000,edu.mit.coeus.utils.xm"
+"l.bean.award.AwardTypeq\u0000~\u0000(sq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~"
+"\u0000#sq\u0000~\u0000$t\u0000\u0005Awardq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0017q\u0000~\u0000\u0013psq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000@"
+"edu.mit.coeus.utils.xml.bean.award.AwardNoticeType.AODetails"
+"Typeq\u0000~\u0000(sq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\tAODetai"
+"lsq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000fppsq"
+"\u0000~\u0000\u0017q\u0000~\u0000\u0013psq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000Gedu.mit.coeus."
+"utils.xml.bean.award.AwardNoticeType.PrintRequirementTypeq\u0000~"
+"\u0000(sq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0010PrintRequireme"
+"ntq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0017q\u0000~\u0000\u0013psq\u0000~\u0000\u0000q\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000p"
+"p\u0000sq\u0000~\u0000\u000fppsq\u0000~\u0000\u0017q\u0000~\u0000\u0013psq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u00009ed"
+"u.mit.coeus.utils.xml.bean.award.MoneyHistoryReportTypeq\u0000~\u0000("
+"sq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0012MoneyHistoryRepo"
+"rtq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000fppsq\u0000~\u0000\u001aq\u0000~\u0000\u0013pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000bAwar"
+"dNoticeq\u0000~\u0000Csr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHas"
+"h;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed"
+"\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar"
+"/ExpressionPool;xp\u0000\u0000\u0000&\u0001pq\u0000~\u0000\nq\u0000~\u0000\u000bq\u0000~\u0000\u0016q\u0000~\u0000Hq\u0000~\u0000Uq\u0000~\u0000bq\u0000~\u0000oq"
+"\u0000~\u0000}q\u0000~\u0000xq\u0000~\u0000\fq\u0000~\u0000\rq\u0000~\u0000\u0014q\u0000~\u0000Fq\u0000~\u0000Sq\u0000~\u0000`q\u0000~\u0000mq\u0000~\u0000{q\u0000~\u0000)q\u0000~\u0000Mq"
+"\u0000~\u0000Zq\u0000~\u0000gq\u0000~\u0000tq\u0000~\u0000\u0082q\u0000~\u0000\u0086q\u0000~\u0000yq\u0000~\u0000\tq\u0000~\u0000\u0019q\u0000~\u0000Iq\u0000~\u0000Vq\u0000~\u0000cq\u0000~\u0000pq"
+"\u0000~\u0000~q\u0000~\u0000\u000eq\u0000~\u0000\u0010q\u0000~\u0000Dq\u0000~\u0000Qq\u0000~\u0000^q\u0000~\u0000kx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.award.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("AwardNotice" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("SchoolInfo" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("AwardDisclosure" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("Award" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("AODetails" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("PrintRequirement" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("MoneyHistoryReport" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                    case  2 :
                        if (("AwardNotice" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeTypeImpl)edu.mit.coeus.utils.xml.bean.award.impl.AwardNoticeImpl.this).new Unmarshaller(context)), 2, value);
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
