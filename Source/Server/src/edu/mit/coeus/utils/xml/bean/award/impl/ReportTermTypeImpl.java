//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.13 at 02:14:23 EST 
//


package edu.mit.coeus.utils.xml.bean.award.impl;

public class ReportTermTypeImpl implements edu.mit.coeus.utils.xml.bean.award.ReportTermType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.award.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _ReportTermDetails;
    protected java.lang.String _Description;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.award.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.award.ReportTermType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getReportTermDetails() {
        if (_ReportTermDetails == null) {
            _ReportTermDetails = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _ReportTermDetails;
    }

    public java.util.List getReportTermDetails() {
        return _getReportTermDetails();
    }

    public java.lang.String getDescription() {
        return _Description;
    }

    public void setDescription(java.lang.String value) {
        _Description = value;
    }

    public edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.award.impl.ReportTermTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ReportTermDetails == null)? 0 :_ReportTermDetails.size());
        if (_Description!= null) {
            context.startElement("", "Description");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Description), "Description");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.award.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx1 != len1) {
            context.startElement("", "ReportTermDetails");
            int idx_2 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ReportTermDetails.get(idx_2 ++)), "ReportTermDetails");
            context.endNamespaceDecls();
            int idx_3 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ReportTermDetails.get(idx_3 ++)), "ReportTermDetails");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ReportTermDetails.get(idx1 ++)), "ReportTermDetails");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ReportTermDetails == null)? 0 :_ReportTermDetails.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.award.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_ReportTermDetails == null)? 0 :_ReportTermDetails.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.award.ReportTermType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom."
+"sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttr"
+"ibutesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa"
+"\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002"
+"L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003q\u0000~\u0000\rpsr\u0000#com.s"
+"un.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000"
+"*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%co"
+"m.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.ms"
+"v.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Lj"
+"ava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv"
+"/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/200"
+"1/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Whi"
+"teSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expres"
+"sion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.uti"
+"l.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017"
+"xpq\u0000~\u0000\u001bq\u0000~\u0000\u001asq\u0000~\u0000\u0006ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\txq\u0000~\u0000\u0003q\u0000~\u0000\rpsq\u0000~\u0000\u000fppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0005QN"
+"amesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dq\u0000~\u0000 sq\u0000~\u0000!q\u0000~\u0000)q\u0000~\u0000\u001asr\u0000#com.sun.msv.gramma"
+"r.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURI"
+"q\u0000~\u0000\u0017xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000"
+")http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gr"
+"ammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\f\u0001ps"
+"q\u0000~\u0000-t\u0000\u000bDescriptiont\u0000\u0000q\u0000~\u00003sq\u0000~\u0000\u0006ppsr\u0000 com.sun.msv.grammar.O"
+"neOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\rpsq\u0000~\u0000\bq\u0000~\u0000\rp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\bpp\u0000sq"
+"\u0000~\u0000\u0006ppsq\u0000~\u00009q\u0000~\u0000\rpsq\u0000~\u0000$q\u0000~\u0000\rpsr\u00002com.sun.msv.grammar.Expres"
+"sion$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00004psr\u0000 com.sun.m"
+"sv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000.q\u0000~\u00003sq\u0000~\u0000-t\u00008edu.mi"
+"t.coeus.utils.xml.bean.award.ReportTermDetailsTypet\u0000+http://"
+"java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0006ppsq\u0000~\u0000$q\u0000~\u0000\rpq\u0000~\u0000"
+"&q\u0000~\u0000/q\u0000~\u00003sq\u0000~\u0000-t\u0000\u0011ReportTermDetailsq\u0000~\u00007q\u0000~\u00003sr\u0000\"com.sun.m"
+"sv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/m"
+"sv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramm"
+"ar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVers"
+"ionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\n\u0001pq"
+"\u0000~\u0000\u0007q\u0000~\u0000\u000eq\u0000~\u0000?q\u0000~\u00008q\u0000~\u0000=q\u0000~\u0000#q\u0000~\u0000Iq\u0000~\u0000\u0005q\u0000~\u0000;q\u0000~\u0000@x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.award.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.award.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.award.impl.ReportTermTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("AwardNumber" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("SequenceNumber" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ReportClassCode" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ReportClassDesc" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ReportCode" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("ReportCodeDesc" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("FrequencyCode" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("FrequencyCodeDesc" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("FrequencyBaseCode" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("FrequencyBaseDesc" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("OSPDistributionCode" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("OSPDistributionDesc" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("MailCopies" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("DueDateModified" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("DueDate" == ___local)&&("" == ___uri)) {
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname, __atts)));
                        return ;
                    case  3 :
                        if (("ReportTermDetails" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        if (("ReportTermDetails" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Description" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
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
                        if (("Description" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ReportTermDetails" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  4 :
                        _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname)));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
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
                    case  4 :
                        _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname)));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  6 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
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
                    case  4 :
                        _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, ___uri, ___local, ___qname)));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  6 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
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
                        case  4 :
                            _getReportTermDetails().add(((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.award.impl.ReportTermDetailsTypeImpl.class), 5, value)));
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Description = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}