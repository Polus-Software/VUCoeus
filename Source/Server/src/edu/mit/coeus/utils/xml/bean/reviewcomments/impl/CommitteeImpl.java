//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments.impl;

public class CommitteeImpl
    extends edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeTypeImpl
    implements edu.mit.coeus.utils.xml.bean.reviewcomments.Committee, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.reviewcomments.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.Committee.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://irb.mit.edu/irbnamespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Committee";
    }

    public edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://irb.mit.edu/irbnamespace", "Committee");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.Committee.class);
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
+"q\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bpps"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\rppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000"
+"~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.m"
+"sv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~"
+"\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0015psr\u00002com.sun.msv.grammar.Expression$AnyStringEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0014\u0001q\u0000~\u0000\u0019sr\u0000 com.sun.msv.grammar"
+".AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpressi"
+"on\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar.SimpleN"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnames"
+"paceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u0000?edu.mit.coeus.utils.xml.bean.reviewcomm"
+"ents.CommitteeMasterDatat\u0000+http://java.sun.com/jaxb/xjc/dumm"
+"y-elementssq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~"
+"\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000Cedu.mit.coeus.utils.xml.bean"
+".reviewcomments.CommitteeMasterDataTypeq\u0000~\u0000$sq\u0000~\u0000\rppsq\u0000~\u0000\u0016q\u0000"
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
+"xpq\u0000~\u0000:q\u0000~\u00009sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema"
+"-instanceq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0013CommitteeMasterDatat\u0000\u001fhttp://irb.mit."
+"edu/irbnamespacesq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\rq\u0000~\u0000\u0015psq\u0000~\u0000\u0000q\u0000~\u0000\u0015p"
+"\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000;edu"
+".mit.coeus.utils.xml.bean.reviewcomments.CommitteeMemberq\u0000~\u0000"
+"$sq\u0000~\u0000\u0000q\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~"
+"\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000?edu.mit.coeus.utils.xml.bean.revi"
+"ewcomments.CommitteeMemberTypeq\u0000~\u0000$sq\u0000~\u0000\rppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u00002"
+"q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u000fCommitteeMemberq\u0000~\u0000Gq\u0000~\u0000\u001fsq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000"
+"~\u0000\u0015psq\u0000~\u0000\rq\u0000~\u0000\u0015psq\u0000~\u0000\u0000q\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015"
+"pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u00008edu.mit.coeus.utils.xml.bean.review"
+"comments.ResearchAreaq\u0000~\u0000$sq\u0000~\u0000\u0000q\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000<edu.mit."
+"coeus.utils.xml.bean.reviewcomments.ResearchAreaTypeq\u0000~\u0000$sq\u0000"
+"~\u0000\rppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\fResearchAreaq\u0000~\u0000Gq\u0000"
+"~\u0000\u001fsq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\rq\u0000~\u0000\u0015psq\u0000~\u0000\u0000q\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\rppsq\u0000~"
+"\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u00004edu.mit.coeus.ut"
+"ils.xml.bean.reviewcomments.Scheduleq\u0000~\u0000$sq\u0000~\u0000\u0000q\u0000~\u0000\u0015p\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u0011q\u0000~\u0000\u0015psq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq"
+"\u0000~\u0000 t\u00008edu.mit.coeus.utils.xml.bean.reviewcomments.ScheduleT"
+"ypeq\u0000~\u0000$sq\u0000~\u0000\rppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\bSchedule"
+"q\u0000~\u0000Gq\u0000~\u0000\u001fsq\u0000~\u0000\rppsq\u0000~\u0000\u0016q\u0000~\u0000\u0015pq\u0000~\u00002q\u0000~\u0000Bq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\tCommit"
+"teeq\u0000~\u0000Gsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"expTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xp"
+"sr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003"
+"I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Exp"
+"ressionPool;xp\u0000\u0000\u0000\'\u0001pq\u0000~\u0000\u000bq\u0000~\u0000Iq\u0000~\u0000^q\u0000~\u0000sq\u0000~\u0000-q\u0000~\u0000Yq\u0000~\u0000nq\u0000~\u0000\u0083"
+"q\u0000~\u0000\u0087q\u0000~\u0000&q\u0000~\u0000Rq\u0000~\u0000gq\u0000~\u0000|q\u0000~\u0000\u0010q\u0000~\u0000(q\u0000~\u0000Lq\u0000~\u0000Tq\u0000~\u0000aq\u0000~\u0000iq\u0000~\u0000v"
+"q\u0000~\u0000~q\u0000~\u0000\nq\u0000~\u0000\u000eq\u0000~\u0000Jq\u0000~\u0000_q\u0000~\u0000tq\u0000~\u0000Hq\u0000~\u0000]q\u0000~\u0000rq\u0000~\u0000\u0013q\u0000~\u0000)q\u0000~\u0000M"
+"q\u0000~\u0000Uq\u0000~\u0000bq\u0000~\u0000jq\u0000~\u0000wq\u0000~\u0000\u007fq\u0000~\u0000\tq\u0000~\u0000\fx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeTypeImpl)edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeTypeImpl)edu.mit.coeus.utils.xml.bean.reviewcomments.impl.CommitteeImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Committee" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
                    case  2 :
                        if (("Committee" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
