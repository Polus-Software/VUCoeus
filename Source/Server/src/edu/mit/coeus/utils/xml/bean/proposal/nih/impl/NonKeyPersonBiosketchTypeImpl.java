//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class NonKeyPersonBiosketchTypeImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.nih.impl.KeyPersonBiosketchTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.nih.NonKeyPersonBiosketchType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected java.lang.String _AccountIdentifier;
    protected java.lang.String _PositionTitle;
    protected edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType _Name;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.NonKeyPersonBiosketchType.class);
    }

    public java.lang.String getAccountIdentifier() {
        return _AccountIdentifier;
    }

    public void setAccountIdentifier(java.lang.String value) {
        _AccountIdentifier = value;
    }

    public java.lang.String getPositionTitle() {
        return _PositionTitle;
    }

    public void setPositionTitle(java.lang.String value) {
        _PositionTitle = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType getName() {
        return _Name;
    }

    public void setName(edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType value) {
        _Name = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.NonKeyPersonBiosketchTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
        context.startElement("", "Name");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endElement();
        if (_PositionTitle!= null) {
            context.startElement("", "PositionTitle");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PositionTitle), "PositionTitle");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_AccountIdentifier!= null) {
            context.startElement("", "AccountIdentifier");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _AccountIdentifier), "AccountIdentifier");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeAttributes(context);
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.NonKeyPersonBiosketchType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv."
+"grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/su"
+"n/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq"
+"\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002"
+"dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001d"
+"Lcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatyp"
+"e.xsd.FinalComponent\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\nfinalValuexr\u0000\u001ecom.sun.msv."
+"datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)Lcom/sun/msv/data"
+"type/xsd/XSDatatypeImpl;xr\u0000\'com.sun.msv.datatype.xsd.XSDatat"
+"ypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btyp"
+"eNameq\u0000~\u0000\u0016L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpa"
+"ceProcessor;xpt\u00009http://era.nih.gov/Projectmgmt/SBIR/CGAP/co"
+"mmon.namespacet\u0000\u0012FileIdentifierTypesr\u00005com.sun.msv.datatype."
+"xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.d"
+"atatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000#com.sun.msv."
+"datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.su"
+"n.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.m"
+"sv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0015t\u0000 http://www.w"
+"3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000\u001d\u0001\u0000\u0000\u0000\u0000sr\u00000com.sun.msv.gramm"
+"ar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.StringP"
+"air\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~\u0000\u0016xpq\u0000~\u0000#q\u0000"
+"~\u0000\u0019sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tname"
+"Classq\u0000~\u0000\nxq\u0000~\u0000\u0003q\u0000~\u0000\'psq\u0000~\u0000\u000eppsr\u0000\"com.sun.msv.datatype.xsd.Q"
+"nameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001fq\u0000~\u0000\"t\u0000\u0005QNamesr\u00005com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001cq\u0000~\u0000%sq\u0000"
+"~\u0000(q\u0000~\u00001q\u0000~\u0000\"sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~\u0000\u0016xr\u0000\u001dcom.sun.msv.gram"
+"mar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/X"
+"MLSchema-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonE"
+"xpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000&\u0001q\u0000~\u0000;sq\u0000~\u00005t\u0000&PositionsHono"
+"rsCitationsFileIdentifiert\u0000\u0000sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0011sq\u0000~\u0000*ppsq"
+"\u0000~\u0000,q\u0000~\u0000\'pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u001dResearchSupportFileIdentif"
+"ierq\u0000~\u0000?sq\u0000~\u0000\tpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\tpp\u0000sq\u0000~\u0000*ppsr\u0000 com.sun.msv.gr"
+"ammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\'psq\u0000~\u0000,q\u0000~\u0000\'psr\u00002com.sun.m"
+"sv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000"
+"~\u0000<q\u0000~\u0000Osr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"6q\u0000~\u0000;sq\u0000~\u00005t\u0000<edu.mit.coeus.utils.xml.bean.proposal.rar.Per"
+"sonFullNameTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-element"
+"ssq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\'pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0004Nameq\u0000~\u0000?sq\u0000~\u0000*"
+"ppsq\u0000~\u0000\tq\u0000~\u0000\'p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000eq\u0000~\u0000\'pq\u0000~\u0000!q\u0000~\u0000%sq\u0000~\u0000(q\u0000~\u0000#q\u0000~\u0000"
+"\"sq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\'pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\rPositionTitleq\u0000"
+"~\u0000?q\u0000~\u0000;sq\u0000~\u0000*ppsq\u0000~\u0000\tq\u0000~\u0000\'p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000eppsr\u0000\'com.sun.msv"
+".datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.s"
+"un.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT"
+"\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005"
+"Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000\u0014L\u0000\fconcr"
+"eteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNam"
+"eq\u0000~\u0000\u0016xq\u0000~\u0000\u0015t\u0000>http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihs"
+"pecific.namespacepq\u0000~\u0000\u001d\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLen"
+"gthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000gq\u0000~\u0000kpq\u0000~\u0000\u001d\u0000\u0000q\u0000~\u0000!q\u0000~\u0000!"
+"t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000!t\u0000\tmaxLength\u0000\u0000\u0000\u0014q\u0000~\u0000%sq\u0000~\u0000(t\u0000\u000estring-de"
+"rivedq\u0000~\u0000ksq\u0000~\u0000*ppsq\u0000~\u0000,q\u0000~\u0000\'pq\u0000~\u0000.q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0011Accoun"
+"tIdentifierq\u0000~\u0000?q\u0000~\u0000;sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$"
+"ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHa"
+"sh\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/ms"
+"v/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0012\u0001pq\u0000~\u0000\rq\u0000~\u0000Aq\u0000~\u0000\bq\u0000~\u0000\u0006q\u0000~\u0000Lq\u0000"
+"~\u0000Gq\u0000~\u0000Iq\u0000~\u0000bq\u0000~\u0000\u0005q\u0000~\u0000Yq\u0000~\u0000\u0007q\u0000~\u0000dq\u0000~\u0000[q\u0000~\u0000+q\u0000~\u0000Bq\u0000~\u0000Uq\u0000~\u0000^q\u0000"
+"~\u0000rx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "-----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.NonKeyPersonBiosketchTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("PositionsHonorsCitationsFileIdentifier" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.KeyPersonBiosketchTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.nih.impl.NonKeyPersonBiosketchTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  2 :
                        if (("NamePrefix" == ___local)&&("" == ___uri)) {
                            _Name = ((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("FirstName" == ___local)&&("" == ___uri)) {
                            _Name = ((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  1 :
                        if (("Name" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 2;
                            return ;
                        }
                        break;
                    case  10 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  7 :
                        if (("AccountIdentifier" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 8;
                            return ;
                        }
                        state = 10;
                        continue outer;
                    case  4 :
                        if (("PositionTitle" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 5;
                            return ;
                        }
                        state = 7;
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
                    case  6 :
                        if (("PositionTitle" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 7;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("Name" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  10 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        state = 10;
                        continue outer;
                    case  4 :
                        state = 7;
                        continue outer;
                    case  9 :
                        if (("AccountIdentifier" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 10;
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
                    case  10 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        state = 10;
                        continue outer;
                    case  4 :
                        state = 7;
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
                    case  10 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        state = 10;
                        continue outer;
                    case  4 :
                        state = 7;
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
                        case  5 :
                            eatText1(value);
                            state = 6;
                            return ;
                        case  10 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            state = 10;
                            continue outer;
                        case  4 :
                            state = 7;
                            continue outer;
                        case  8 :
                            eatText2(value);
                            state = 9;
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
                _PositionTitle = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AccountIdentifier = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}