//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class DemographicsImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.rar.Demographics, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.Demographics.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Demographics";
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace", "Demographics");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.Demographics.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.m"
+"sv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.B"
+"oolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.gramma"
+"r.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L"
+"\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004pp"
+"sr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun."
+"msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xr\u0000*com.sun.msv."
+"datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dat"
+"atype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xs"
+"d.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/Stri"
+"ng;L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd"
+"/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000"
+"\u0004datesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collap"
+"se\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcesso"
+"r\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u0016psr\u0000\u001bcom.sun.msv.util.StringPair"
+"\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xpq\u0000~\u0000%q\u0000~\u0000$"
+"sq\u0000~\u0000\u0012ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003ex"
+"pq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0016psq\u0000~\u0000\u0018ppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\u0005QNameq\u0000~\u0000(q\u0000~\u0000"
+"*sq\u0000~\u0000+q\u0000~\u00003q\u0000~\u0000$sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xr\u0000\u001dcom.sun.msv."
+"grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/20"
+"01/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsi"
+"lonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0015\u0001q\u0000~\u0000;sq\u0000~\u00005t\u0000\tBirthDate"
+"t\u0000\u0000q\u0000~\u0000;sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsr\u0000\'com.sun.msv.datatype.xs"
+"d.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom.sun.msv.data"
+"type.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype"
+"/xsd/XSDatatypeImpl;xq\u0000~\u0000 t\u00009http://era.nih.gov/Projectmgmt/"
+"SBIR/CGAP/common.namespacet\u0000\nGenderTypeq\u0000~\u0000(sr\u0000\"com.sun.msv."
+"datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xs"
+"d.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\u0005tokenq\u0000"
+"~\u0000(\u0001\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q"
+"\u0000~\u0000;sq\u0000~\u00005t\u0000\u0006Genderq\u0000~\u0000?sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000Cq\u0000~\u0000G"
+"t\u0000\rEthnicityTypeq\u0000~\u0000(q\u0000~\u0000K\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012pps"
+"q\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\tEthnicityq\u0000~\u0000?sq\u0000~\u0000\u0000pp\u0000sq"
+"\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000Cq\u0000~\u0000Gt\u0000\bRaceTypeq\u0000~\u0000(q\u0000~\u0000K\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~\u0000"
+"+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0004Raceq"
+"\u0000~\u0000?sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000Cq\u0000~\u0000Gt\u0000\u000fCitizenshipTypeq\u0000"
+"~\u0000(q\u0000~\u0000K\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000"
+"~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0011CitizenshipStatusq\u0000~\u0000?sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppq\u0000~\u0000Kq\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000$sq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000"
+"\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0014CountryOfCitizenshipq\u0000~\u0000?q\u0000~\u0000;sq\u0000~"
+"\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000Cq\u0000~\u0000Gt\u0000\u0014DisabilityStatusTypeq\u0000~\u0000("
+"q\u0000~\u0000K\u0000\u0000\u0000\u0000q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007"
+"q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0010DisabilityStatusq\u0000~\u0000?sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u0018ppsq\u0000~\u0000Cq\u0000~\u0000Gt\u0000\u000eDisabilityTypeq\u0000~\u0000(q\u0000~\u0000K\u0000\u0000\u0000\u0000q\u0000~\u0000*s"
+"q\u0000~\u0000+q\u0000~\u0000Lq\u0000~\u0000Gsq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0012D"
+"isabilityCategoryq\u0000~\u0000?q\u0000~\u0000;sq\u0000~\u0000\u0012ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000"
+"ssq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0015DisabilityDescr"
+"iptionq\u0000~\u0000?q\u0000~\u0000;sq\u0000~\u0000\u0012ppsq\u0000~\u0000.q\u0000~\u0000\u0016pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\f"
+"Demographicst\u0000Ehttp://era.nih.gov/Projectmgmt/SBIR/CGAP/rese"
+"archandrelated.namespacesr\u0000\"com.sun.msv.grammar.ExpressionPo"
+"ol\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPo"
+"ol$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Close"
+"dHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun"
+"/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000 \u0001pq\u0000~\u0000\u0085q\u0000~\u0000Aq\u0000~\u0000gq\u0000~\u0000\u0010q\u0000~\u0000"
+"\u000eq\u0000~\u0000\u0011q\u0000~\u0000rq\u0000~\u0000\u0090q\u0000~\u0000\u0017q\u0000~\u0000\tq\u0000~\u0000pq\u0000~\u0000\u008eq\u0000~\u0000]q\u0000~\u0000\u0013q\u0000~\u0000\u000fq\u0000~\u0000\u000bq\u0000~\u0000"
+"\rq\u0000~\u0000-q\u0000~\u0000Nq\u0000~\u0000Xq\u0000~\u0000bq\u0000~\u0000lq\u0000~\u0000uq\u0000~\u0000\u007fq\u0000~\u0000\u008aq\u0000~\u0000\u0091q\u0000~\u0000\u0095q\u0000~\u0000\u0083q\u0000~\u0000"
+"\nq\u0000~\u0000zq\u0000~\u0000Sq\u0000~\u0000\fx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("BirthDate" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("Gender" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.rar.impl.DemographicsImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Demographics" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
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
                    case  2 :
                        if (("Demographics" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/researchandrelated.namespace" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
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
