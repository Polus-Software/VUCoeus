//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule.impl;

public class ProtocolSubmissionTypeImpl implements edu.mit.coeus.utils.xml.bean.schedule.ProtocolSubmissionType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _Minutes;
    protected edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType _SubmissionDetails;
    protected edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType _ProtocolSummary;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.schedule.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.schedule.ProtocolSubmissionType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getMinutes() {
        if (_Minutes == null) {
            _Minutes = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Minutes;
    }

    public java.util.List getMinutes() {
        return _getMinutes();
    }

    public edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType getSubmissionDetails() {
        return _SubmissionDetails;
    }

    public void setSubmissionDetails(edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetailsType value) {
        _SubmissionDetails = value;
    }

    public edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType getProtocolSummary() {
        return _ProtocolSummary;
    }

    public void setProtocolSummary(edu.mit.coeus.utils.xml.bean.schedule.ProtocolSummaryType value) {
        _ProtocolSummary = value;
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSubmissionTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Minutes == null)? 0 :_Minutes.size());
        if (_ProtocolSummary instanceof javax.xml.bind.Element) {
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
        } else {
            context.startElement("http://irb.mit.edu/irbnamespace", "ProtocolSummary");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
            context.endElement();
        }
        if (_SubmissionDetails instanceof javax.xml.bind.Element) {
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
        } else {
            context.startElement("http://irb.mit.edu/irbnamespace", "SubmissionDetails");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
            context.endElement();
        }
        while (idx1 != len1) {
            if (_Minutes.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx1 ++)), "Minutes");
            } else {
                context.startElement("http://irb.mit.edu/irbnamespace", "Minutes");
                int idx_4 = idx1;
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx_4 ++)), "Minutes");
                context.endNamespaceDecls();
                int idx_5 = idx1;
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx_5 ++)), "Minutes");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx1 ++)), "Minutes");
                context.endElement();
            }
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Minutes == null)? 0 :_Minutes.size());
        if (_ProtocolSummary instanceof javax.xml.bind.Element) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
        }
        if (_SubmissionDetails instanceof javax.xml.bind.Element) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
        }
        while (idx1 != len1) {
            if (_Minutes.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx1 ++)), "Minutes");
            } else {
                idx1 += 1;
            }
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Minutes == null)? 0 :_Minutes.size());
        if (_ProtocolSummary instanceof javax.xml.bind.Element) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ProtocolSummary), "ProtocolSummary");
        }
        if (_SubmissionDetails instanceof javax.xml.bind.Element) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SubmissionDetails), "SubmissionDetails");
        }
        while (idx1 != len1) {
            if (_Minutes.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Minutes.get(idx1 ++)), "Minutes");
            } else {
                idx1 += 1;
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.schedule.ProtocolSubmissionType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0007ppsr\u0000 com.s"
+"un.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramma"
+"r.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd"
+" r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u00002com.sun"
+".msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003"
+"sq\u0000~\u0000\u0011\u0001q\u0000~\u0000\u0016sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv"
+".grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0017q"
+"\u0000~\u0000\u001csr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tloc"
+"alNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001exq\u0000~\u0000\u0019t\u00005edu."
+"mit.coeus.utils.xml.bean.schedule.ProtocolSummaryt\u0000+http://j"
+"ava.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u00009edu"
+".mit.coeus.utils.xml.bean.schedule.ProtocolSummaryTypeq\u0000~\u0000!s"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004nam"
+"et\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.dat"
+"atype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.B"
+"uiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Conc"
+"reteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeIm"
+"pl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpa"
+"cet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http"
+"://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv."
+"datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv"
+".grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0012p"
+"sr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL"
+"\u0000\fnamespaceURIq\u0000~\u0000\u001expq\u0000~\u00007q\u0000~\u00006sq\u0000~\u0000\u001dt\u0000\u0004typet\u0000)http://www.w3"
+".org/2001/XMLSchema-instanceq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u000fProtocolSummaryt\u0000\u001f"
+"http://irb.mit.edu/irbnamespacesq\u0000~\u0000\u0007ppsq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u00007edu.mit.coeus.ut"
+"ils.xml.bean.schedule.SubmissionDetailsq\u0000~\u0000!sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000p"
+"psq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000"
+"~\u0000\u001dt\u0000;edu.mit.coeus.utils.xml.bean.schedule.SubmissionDetail"
+"sTypeq\u0000~\u0000!sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0011Submis"
+"sionDetailsq\u0000~\u0000Dsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0007q\u0000~\u0000\u0012psq\u0000~\u0000\tq\u0000~\u0000\u0012p"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000-edu"
+".mit.coeus.utils.xml.bean.schedule.Minutesq\u0000~\u0000!sq\u0000~\u0000\tq\u0000~\u0000\u0012p\u0000"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq"
+"\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u00001edu.mit.coeus.utils.xml.bean.schedule.MinutesTy"
+"peq\u0000~\u0000!sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000/q\u0000~\u0000?q\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0007Minutesq\u0000"
+"~\u0000Dq\u0000~\u0000\u001csr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"expTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xp"
+"sr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003"
+"I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Exp"
+"ressionPool;xp\u0000\u0000\u0000\u0019\u0001pq\u0000~\u0000#q\u0000~\u0000Mq\u0000~\u0000bq\u0000~\u0000Yq\u0000~\u0000Xq\u0000~\u0000\bq\u0000~\u0000Eq\u0000~\u0000Z"
+"q\u0000~\u0000\rq\u0000~\u0000%q\u0000~\u0000\u0010q\u0000~\u0000&q\u0000~\u0000Hq\u0000~\u0000Gq\u0000~\u0000Pq\u0000~\u0000Oq\u0000~\u0000]q\u0000~\u0000\\q\u0000~\u0000eq\u0000~\u0000d"
+"q\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000*q\u0000~\u0000Tq\u0000~\u0000ix"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSubmissionTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("ProtocolNumber" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _SubmissionDetails = ((edu.mit.coeus.utils.xml.bean.schedule.impl.SubmissionDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.SubmissionDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  1 :
                        if (("ProtocolMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _ProtocolSummary = ((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("ProtocolMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _ProtocolSummary = ((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("ScheduleId" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getMinutes().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  6 :
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getMinutes().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl.class), 9, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  3 :
                        if (("SubmissionDetails" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _SubmissionDetails = ((edu.mit.coeus.utils.xml.bean.schedule.impl.SubmissionDetailsImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.SubmissionDetailsImpl.class), 6, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SubmissionDetails" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getMinutes().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl.class), 9, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("ProtocolSummary" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _ProtocolSummary = ((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ProtocolSummaryImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("ProtocolSummary" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
                        if (("ProtocolSummary" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("SubmissionDetails" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 6;
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
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
                        case  6 :
                            state = 9;
                            continue outer;
                        case  9 :
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
