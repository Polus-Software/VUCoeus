//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule.impl;

public class CommitteeTypeImpl implements edu.mit.coeus.utils.xml.bean.schedule.CommitteeType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _Schedule;
    protected com.sun.xml.bind.util.ListImpl _CommitteeMember;
    protected com.sun.xml.bind.util.ListImpl _ResearchArea;
    protected edu.mit.coeus.utils.xml.bean.schedule.CommitteeMasterDataType _CommitteeMasterData;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.schedule.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.schedule.CommitteeType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getSchedule() {
        if (_Schedule == null) {
            _Schedule = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Schedule;
    }

    public java.util.List getSchedule() {
        return _getSchedule();
    }

    protected com.sun.xml.bind.util.ListImpl _getCommitteeMember() {
        if (_CommitteeMember == null) {
            _CommitteeMember = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _CommitteeMember;
    }

    public java.util.List getCommitteeMember() {
        return _getCommitteeMember();
    }

    protected com.sun.xml.bind.util.ListImpl _getResearchArea() {
        if (_ResearchArea == null) {
            _ResearchArea = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ResearchArea;
    }

    public java.util.List getResearchArea() {
        return _getResearchArea();
    }

    public edu.mit.coeus.utils.xml.bean.schedule.CommitteeMasterDataType getCommitteeMasterData() {
        return _CommitteeMasterData;
    }

    public void setCommitteeMasterData(edu.mit.coeus.utils.xml.bean.schedule.CommitteeMasterDataType value) {
        _CommitteeMasterData = value;
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Schedule == null)? 0 :_Schedule.size());
        int idx2 = 0;
        final int len2 = ((_CommitteeMember == null)? 0 :_CommitteeMember.size());
        int idx3 = 0;
        final int len3 = ((_ResearchArea == null)? 0 :_ResearchArea.size());
        if (_CommitteeMasterData instanceof javax.xml.bind.Element) {
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
        } else {
            context.startElement("http://irb.mit.edu/irbnamespace", "CommitteeMasterData");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
            context.endElement();
        }
        while (idx2 != len2) {
            if (_CommitteeMember.get(idx2) instanceof javax.xml.bind.Element) {
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx2 ++)), "CommitteeMember");
            } else {
                context.startElement("http://irb.mit.edu/irbnamespace", "CommitteeMember");
                int idx_2 = idx2;
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx_2 ++)), "CommitteeMember");
                context.endNamespaceDecls();
                int idx_3 = idx2;
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx_3 ++)), "CommitteeMember");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx2 ++)), "CommitteeMember");
                context.endElement();
            }
        }
        while (idx3 != len3) {
            if (_ResearchArea.get(idx3) instanceof javax.xml.bind.Element) {
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx3 ++)), "ResearchArea");
            } else {
                context.startElement("http://irb.mit.edu/irbnamespace", "ResearchArea");
                int idx_4 = idx3;
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx_4 ++)), "ResearchArea");
                context.endNamespaceDecls();
                int idx_5 = idx3;
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx_5 ++)), "ResearchArea");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx3 ++)), "ResearchArea");
                context.endElement();
            }
        }
        while (idx1 != len1) {
            if (_Schedule.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx1 ++)), "Schedule");
            } else {
                context.startElement("http://irb.mit.edu/irbnamespace", "Schedule");
                int idx_6 = idx1;
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx_6 ++)), "Schedule");
                context.endNamespaceDecls();
                int idx_7 = idx1;
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx_7 ++)), "Schedule");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx1 ++)), "Schedule");
                context.endElement();
            }
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Schedule == null)? 0 :_Schedule.size());
        int idx2 = 0;
        final int len2 = ((_CommitteeMember == null)? 0 :_CommitteeMember.size());
        int idx3 = 0;
        final int len3 = ((_ResearchArea == null)? 0 :_ResearchArea.size());
        if (_CommitteeMasterData instanceof javax.xml.bind.Element) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
        }
        while (idx2 != len2) {
            if (_CommitteeMember.get(idx2) instanceof javax.xml.bind.Element) {
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx2 ++)), "CommitteeMember");
            } else {
                idx2 += 1;
            }
        }
        while (idx3 != len3) {
            if (_ResearchArea.get(idx3) instanceof javax.xml.bind.Element) {
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx3 ++)), "ResearchArea");
            } else {
                idx3 += 1;
            }
        }
        while (idx1 != len1) {
            if (_Schedule.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx1 ++)), "Schedule");
            } else {
                idx1 += 1;
            }
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Schedule == null)? 0 :_Schedule.size());
        int idx2 = 0;
        final int len2 = ((_CommitteeMember == null)? 0 :_CommitteeMember.size());
        int idx3 = 0;
        final int len3 = ((_ResearchArea == null)? 0 :_ResearchArea.size());
        if (_CommitteeMasterData instanceof javax.xml.bind.Element) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CommitteeMasterData), "CommitteeMasterData");
        }
        while (idx2 != len2) {
            if (_CommitteeMember.get(idx2) instanceof javax.xml.bind.Element) {
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _CommitteeMember.get(idx2 ++)), "CommitteeMember");
            } else {
                idx2 += 1;
            }
        }
        while (idx3 != len3) {
            if (_ResearchArea.get(idx3) instanceof javax.xml.bind.Element) {
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ResearchArea.get(idx3 ++)), "ResearchArea");
            } else {
                idx3 += 1;
            }
        }
        while (idx1 != len1) {
            if (_Schedule.get(idx1) instanceof javax.xml.bind.Element) {
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Schedule.get(idx1 ++)), "Schedule");
            } else {
                idx1 += 1;
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.schedule.CommitteeType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar."
+"ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\bpps"
+"r\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.ms"
+"v.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang."
+"Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attrib"
+"uteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psr\u0000"
+"2com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u0000\u0017sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com"
+".sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0003q\u0000~\u0000\u0018q\u0000~\u0000\u001dsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001fxq\u0000~\u0000"
+"\u001at\u00009edu.mit.coeus.utils.xml.bean.schedule.CommitteeMasterDat"
+"at\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\npp\u0000sq\u0000~"
+"\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001d"
+"sq\u0000~\u0000\u001et\u0000=edu.mit.coeus.utils.xml.bean.schedule.CommitteeMast"
+"erDataTypeq\u0000~\u0000\"sq\u0000~\u0000\bppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013psr\u0000\u001bcom.sun.msv.grammar.D"
+"ataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006e"
+"xceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000"
+"\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.ms"
+"v.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.d"
+"atatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype."
+"xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001fL\u0000\btypeName"
+"q\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpacePro"
+"cessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com."
+"sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"psr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0013psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURIq\u0000~\u0000\u001fxpq\u0000~\u00008q\u0000~\u00007sq\u0000~\u0000\u001et\u0000\u0004type"
+"t\u0000)http://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u0013Co"
+"mmitteeMasterDatat\u0000\u001fhttp://irb.mit.edu/irbnamespacesq\u0000~\u0000\bpps"
+"q\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\bq\u0000~\u0000\u0013psq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~"
+"\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u00005edu.mit.coeus.utils.xml.bean"
+".schedule.CommitteeMemberq\u0000~\u0000\"sq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000"
+"sq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u00009edu."
+"mit.coeus.utils.xml.bean.schedule.CommitteeMemberTypeq\u0000~\u0000\"sq"
+"\u0000~\u0000\bppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u00000q\u0000~\u0000@q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u000fCommitteeMemberq\u0000~"
+"\u0000Eq\u0000~\u0000\u001dsq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\bq\u0000~\u0000\u0013psq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\bpp"
+"sq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u00002edu.mit.coeu"
+"s.utils.xml.bean.schedule.ResearchAreaq\u0000~\u0000\"sq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000sq\u0000~"
+"\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001d"
+"sq\u0000~\u0000\u001et\u00006edu.mit.coeus.utils.xml.bean.schedule.ResearchAreaT"
+"ypeq\u0000~\u0000\"sq\u0000~\u0000\bppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u00000q\u0000~\u0000@q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\fResearch"
+"Areaq\u0000~\u0000Eq\u0000~\u0000\u001dsq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\bq\u0000~\u0000\u0013psq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000s"
+"q\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000.edu.m"
+"it.coeus.utils.xml.bean.schedule.Scheduleq\u0000~\u0000\"sq\u0000~\u0000\nq\u0000~\u0000\u0013p\u0000s"
+"q\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsq\u0000~\u0000\u000fq\u0000~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000"
+"~\u0000\u001dsq\u0000~\u0000\u001et\u00002edu.mit.coeus.utils.xml.bean.schedule.ScheduleTy"
+"peq\u0000~\u0000\"sq\u0000~\u0000\bppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u00000q\u0000~\u0000@q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\bScheduleq"
+"\u0000~\u0000Eq\u0000~\u0000\u001dsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;x"
+"psr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000"
+"\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Ex"
+"pressionPool;xp\u0000\u0000\u0000%\u0001pq\u0000~\u0000$q\u0000~\u0000Pq\u0000~\u0000eq\u0000~\u0000zq\u0000~\u0000Gq\u0000~\u0000\\q\u0000~\u0000qq\u0000~\u0000"
+"Fq\u0000~\u0000[q\u0000~\u0000pq\u0000~\u0000\tq\u0000~\u0000Hq\u0000~\u0000]q\u0000~\u0000rq\u0000~\u0000\u000eq\u0000~\u0000&q\u0000~\u0000\u0011q\u0000~\u0000\'q\u0000~\u0000Kq\u0000~\u0000"
+"Jq\u0000~\u0000Sq\u0000~\u0000Rq\u0000~\u0000`q\u0000~\u0000\u0007q\u0000~\u0000_q\u0000~\u0000hq\u0000~\u0000gq\u0000~\u0000uq\u0000~\u0000tq\u0000~\u0000}q\u0000~\u0000|q\u0000~\u0000"
+"\u0005q\u0000~\u0000+q\u0000~\u0000Wq\u0000~\u0000lq\u0000~\u0000\u0081q\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  11 :
                        if (("ScheduleMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getSchedule().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleTypeImpl.class), 12, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ScheduleMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getSchedule().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleTypeImpl.class), 12, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  3 :
                        if (("CommitteeMember" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getCommitteeMember().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberImpl.class), 4, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("CommitteeMember" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 5;
                            return ;
                        }
                        state = 4;
                        continue outer;
                    case  1 :
                        if (("CommitteeId" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _CommitteeMasterData = ((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMasterDataTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMasterDataTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("ResearchAreaCode" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getResearchArea().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  9 :
                        if (("ResearchArea" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getResearchArea().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaImpl.class), 9, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ResearchArea" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        if (("Schedule" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getSchedule().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleImpl.class), 10, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Schedule" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 11;
                            return ;
                        }
                        state = 10;
                        continue outer;
                    case  10 :
                        if (("Schedule" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getSchedule().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ScheduleImpl.class), 10, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Schedule" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 11;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  4 :
                        if (("CommitteeMember" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getCommitteeMember().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberImpl.class), 4, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("CommitteeMember" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 5;
                            return ;
                        }
                        if (("ResearchArea" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getResearchArea().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.ResearchAreaImpl.class), 9, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ResearchArea" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  5 :
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getCommitteeMember().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberTypeImpl.class), 6, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _getCommitteeMember().add(((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMemberTypeImpl.class), 6, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            _CommitteeMasterData = ((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMasterDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.schedule.impl.CommitteeMasterDataImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
                        state = 4;
                        continue outer;
                    case  9 :
                        state = 10;
                        continue outer;
                    case  8 :
                        if (("ResearchArea" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("Schedule" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 10;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("CommitteeMasterData" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("CommitteeMember" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  10 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 9;
                        continue outer;
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
                        state = 4;
                        continue outer;
                    case  9 :
                        state = 10;
                        continue outer;
                    case  10 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 9;
                        continue outer;
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
                        state = 4;
                        continue outer;
                    case  9 :
                        state = 10;
                        continue outer;
                    case  10 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 9;
                        continue outer;
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
                            state = 4;
                            continue outer;
                        case  9 :
                            state = 10;
                            continue outer;
                        case  10 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            state = 9;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
