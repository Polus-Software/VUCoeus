//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.rar.impl;

public class ProjectSiteTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected java.lang.String _CountyName;
    protected java.lang.String _AffectedAreas;
    protected java.lang.String _CongressionalDistrict;
    protected java.lang.String _OrganizationName;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType _PostalAddress;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.rar.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType.class);
    }

    public java.lang.String getCountyName() {
        return _CountyName;
    }

    public void setCountyName(java.lang.String value) {
        _CountyName = value;
    }

    public java.lang.String getAffectedAreas() {
        return _AffectedAreas;
    }

    public void setAffectedAreas(java.lang.String value) {
        _AffectedAreas = value;
    }

    public java.lang.String getCongressionalDistrict() {
        return _CongressionalDistrict;
    }

    public void setCongressionalDistrict(java.lang.String value) {
        _CongressionalDistrict = value;
    }

    public java.lang.String getOrganizationName() {
        return _OrganizationName;
    }

    public void setOrganizationName(java.lang.String value) {
        _OrganizationName = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType getPostalAddress() {
        return _PostalAddress;
    }

    public void setPostalAddress(edu.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType value) {
        _PostalAddress = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSiteTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_OrganizationName!= null) {
            context.startElement("", "OrganizationName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _OrganizationName), "OrganizationName");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PostalAddress!= null) {
            context.startElement("", "PostalAddress");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _PostalAddress), "PostalAddress");
            context.endElement();
        }
        if (_CountyName!= null) {
            context.startElement("", "CountyName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CountyName), "CountyName");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_CongressionalDistrict!= null) {
            context.startElement("", "CongressionalDistrict");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CongressionalDistrict), "CongressionalDistrict");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_AffectedAreas!= null) {
            context.startElement("", "AffectedAreas");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _AffectedAreas), "AffectedAreas");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
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
        return (edu.mit.coeus.utils.xml.bean.proposal.rar.ProjectSiteType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv."
+"grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011"
+"java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun"
+".msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype"
+"/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPa"
+"ir;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlway"
+"sValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'c"
+"om.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespac"
+"eUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nwhiteSpacet\u0000.Lco"
+"m/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w"
+"3.org/2001/XMLSchemat\u0000\u0005tokensr\u00005com.sun.msv.datatype.xsd.Whi"
+"teSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype"
+".xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.gramma"
+"r.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0010psr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnames"
+"paceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esq\u0000~\u0000\tppsr\u0000 com.sun.msv.grammar.Attr"
+"ibuteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000\u0003q\u0000~\u0000\u0010ps"
+"q\u0000~\u0000\u0012ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0018q\u0000~\u0000\u001et\u0000\u0005QNameq\u0000~\u0000\"q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u0000-q\u0000~\u0000\u001esr\u0000#com.sun.msv.gra"
+"mmar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespace"
+"URIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typ"
+"et\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv"
+".grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000f"
+"\u0001q\u0000~\u00005sq\u0000~\u0000/t\u0000\u0010OrganizationNamet\u0000\u0000q\u0000~\u00005sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\tppsr\u0000 com.sun.msv.grammar.OneOrMoreEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003ex"
+"pq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000(q\u0000~\u0000\u0010psr\u00002com.sun.msv.grammar.Expres"
+"sion$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00006q\u0000~\u0000Dsr\u0000 com.s"
+"un.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00000q\u0000~\u00005sq\u0000~\u0000/t\u0000>ed"
+"u.mit.coeus.utils.xml.bean.proposal.common.PostalAddressType"
+"t\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\tppsq\u0000~\u0000("
+"q\u0000~\u0000\u0010pq\u0000~\u0000*q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\rPostalAddressq\u0000~\u00009q\u0000~\u00005sq\u0000~\u0000\tp"
+"psq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0015sq\u0000~\u0000\tppsq\u0000~\u0000(q\u0000~\u0000\u0010pq\u0000~\u0000*q\u0000~\u00001q\u0000~"
+"\u00005sq\u0000~\u0000/t\u0000\nCountyNameq\u0000~\u00009q\u0000~\u00005sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000pp"
+"q\u0000~\u0000\u0015sq\u0000~\u0000\tppsq\u0000~\u0000(q\u0000~\u0000\u0010pq\u0000~\u0000*q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\u0015Congression"
+"alDistrictq\u0000~\u00009q\u0000~\u00005sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0015sq\u0000~\u0000\t"
+"ppsq\u0000~\u0000(q\u0000~\u0000\u0010pq\u0000~\u0000*q\u0000~\u00001q\u0000~\u00005sq\u0000~\u0000/t\u0000\rAffectedAreasq\u0000~\u00009q\u0000~\u0000"
+"5sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTabl"
+"et\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com"
+".sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005coun"
+"tB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expression"
+"Pool;xp\u0000\u0000\u0000\u0015\u0001pq\u0000~\u0000\u0007q\u0000~\u0000\u0011q\u0000~\u0000Pq\u0000~\u0000Wq\u0000~\u0000^q\u0000~\u0000Aq\u0000~\u0000<q\u0000~\u0000>q\u0000~\u0000\nq\u0000"
+"~\u0000\bq\u0000~\u0000Nq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000:q\u0000~\u0000\'q\u0000~\u0000Jq\u0000~\u0000Qq\u0000~\u0000Xq\u0000~\u0000_q\u0000~\u0000\u0006q\u0000~\u0000\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.rar.impl.ProjectSiteTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("PostalAddress" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  6 :
                        if (("CountyName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  4 :
                        if (("Street" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("MailStopCode" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("City" == ___local)&&("" == ___uri)) {
                            _PostalAddress = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.PostalAddressTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  12 :
                        if (("AffectedAreas" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  0 :
                        if (("OrganizationName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  9 :
                        if (("CongressionalDistrict" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("OrganizationName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("CongressionalDistrict" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("PostalAddress" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("AffectedAreas" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  8 :
                        if (("CountyName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
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
                        state = 6;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  9 :
                        state = 12;
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
                        state = 6;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  9 :
                        state = 12;
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
                        case  10 :
                            eatText1(value);
                            state = 11;
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  1 :
                            eatText2(value);
                            state = 2;
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  13 :
                            eatText4(value);
                            state = 14;
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
                _CongressionalDistrict = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _OrganizationName = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CountyName = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AffectedAreas = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
