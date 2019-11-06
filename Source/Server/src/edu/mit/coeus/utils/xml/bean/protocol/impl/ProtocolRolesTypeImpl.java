//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.03.17 at 08:39:35 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.protocol.impl;

public class ProtocolRolesTypeImpl implements edu.mit.coeus.utils.xml.bean.protocol.ProtocolRolesType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _UserRoles;
    protected java.lang.String _RoleName;
    protected boolean has_Role;
    protected int _Role;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.protocol.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolRolesType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getUserRoles() {
        if (_UserRoles == null) {
            _UserRoles = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _UserRoles;
    }

    public java.util.List getUserRoles() {
        return _getUserRoles();
    }

    public java.lang.String getRoleName() {
        return _RoleName;
    }

    public void setRoleName(java.lang.String value) {
        _RoleName = value;
    }

    public int getRole() {
        return _Role;
    }

    public void setRole(int value) {
        _Role = value;
        has_Role = true;
    }

    public edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolRolesTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_UserRoles == null)? 0 :_UserRoles.size());
        if (!has_Role) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Role"));
        }
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "Role");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Role)), "Role");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "RoleName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _RoleName), "RoleName");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        while (idx1 != len1) {
            context.startElement("http://www.w3.org/2001/ProtocolSummarySchema", "UserRoles");
            int idx_4 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _UserRoles.get(idx_4 ++)), "UserRoles");
            context.endNamespaceDecls();
            int idx_5 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _UserRoles.get(idx_5 ++)), "UserRoles");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _UserRoles.get(idx1 ++)), "UserRoles");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_UserRoles == null)? 0 :_UserRoles.size());
        if (!has_Role) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Role"));
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_UserRoles == null)? 0 :_UserRoles.size());
        if (!has_Role) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "Role"));
        }
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.protocol.ProtocolRolesType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000pps"
+"r\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxn"
+"g/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/uti"
+"l/StringPair;xq\u0000~\u0000\u0003ppsr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&"
+"6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeIm"
+"pl;xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com."
+"sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUr"
+"it\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u0016L\u0000\nwhiteSpacet\u0000.Lcom/s"
+"un/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.o"
+"rg/2001/XMLSchemat\u0000\u0003intsr\u00005com.sun.msv.datatype.xsd.WhiteSpa"
+"ceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*com.sun.msv.datatype.xsd"
+".MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.Ra"
+"ngeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com"
+".sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7"
+"\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000\u0012L\u0000\fcon"
+"creteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetN"
+"ameq\u0000~\u0000\u0016xq\u0000~\u0000\u0015ppq\u0000~\u0000\u001d\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclu"
+"siveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001fppq\u0000~\u0000\u001d\u0000\u0000sr\u0000!com.sun.msv.datatype."
+"xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0011q\u0000~\u0000\u0019t\u0000\u0004longq\u0000~\u0000\u001dsq\u0000~\u0000\u001eppq\u0000~\u0000\u001d\u0000"
+"\u0001sq\u0000~\u0000%ppq\u0000~\u0000\u001d\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0011q\u0000~\u0000\u0019t\u0000\u0007integerq\u0000~\u0000\u001dsr\u0000,com.sun.msv.datatype.xs"
+"d.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.data"
+"type.xsd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\""
+"ppq\u0000~\u0000\u001d\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0013q\u0000~\u0000\u0019t\u0000\u0007decimalq\u0000~\u0000\u001dq\u0000~\u00003t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000-t\u0000\fm"
+"inInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lan"
+"g.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000-t\u0000\fmaxInclusivesq\u0000~\u00007\u007f\u00ff\u00ff\u00ff\u00ff"
+"\u00ff\u00ff\u00ffq\u0000~\u0000(q\u0000~\u00006sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u00008\u0080"
+"\u0000\u0000\u0000q\u0000~\u0000(q\u0000~\u0000:sq\u0000~\u0000<\u007f\u00ff\u00ff\u00ffsr\u00000com.sun.msv.grammar.Expression$Nu"
+"llSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.Strin"
+"gPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamespaceURIq\u0000~\u0000\u0016xpq\u0000~\u0000\u001a"
+"q\u0000~\u0000\u0019sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000"
+" com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tna"
+"meClassq\u0000~\u0000\bxq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp"
+"\u0000psq\u0000~\u0000\fppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0013q\u0000~\u0000\u0019t\u0000\u0005QNameq\u0000~\u0000\u001dq\u0000~\u0000@sq\u0000~\u0000Aq\u0000~\u0000Lq\u0000~\u0000\u0019sr\u0000#com.sun.msv."
+"grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0016L\u0000\fnamesp"
+"aceURIq\u0000~\u0000\u0016xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004"
+"typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun."
+"msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000"
+"~\u0000G\u0001psq\u0000~\u0000Nt\u0000\u0004Rolet\u0000,http://www.w3.org/2001/ProtocolSummaryS"
+"chemasq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fq\u0000~\u0000Hpsr\u0000#com.sun.msv.datatype.x"
+"sd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0013q\u0000~\u0000\u0019t\u0000\u0006string"
+"sr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001c\u0001q\u0000~\u0000@sq\u0000~\u0000Aq\u0000~\u0000^q\u0000~\u0000\u0019sq\u0000~\u0000Cppsq\u0000~\u0000Eq\u0000~\u0000Hpq\u0000~\u0000"
+"Iq\u0000~\u0000Pq\u0000~\u0000Tsq\u0000~\u0000Nt\u0000\bRoleNameq\u0000~\u0000Xsq\u0000~\u0000Cppsr\u0000 com.sun.msv.gra"
+"mmar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000Hpsq\u0000~\u0000\u0007q\u0000~\u0000Hp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000"
+"\u0007pp\u0000sq\u0000~\u0000Cppsq\u0000~\u0000gq\u0000~\u0000Hpsq\u0000~\u0000Eq\u0000~\u0000Hpsr\u00002com.sun.msv.grammar."
+"Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Upsr\u0000 com"
+".sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Oq\u0000~\u0000Tsq\u0000~\u0000Nt\u0000;"
+"edu.mit.coeus.utils.xml.bean.protocol.ProtocolUserRolesTypet"
+"\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000Cppsq\u0000~\u0000Eq"
+"\u0000~\u0000Hpq\u0000~\u0000Iq\u0000~\u0000Pq\u0000~\u0000Tsq\u0000~\u0000Nt\u0000\tUserRolesq\u0000~\u0000Xq\u0000~\u0000Tsr\u0000\"com.sun."
+"msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/"
+"msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gram"
+"mar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVer"
+"sionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\f\u0001p"
+"q\u0000~\u0000iq\u0000~\u0000\u0006q\u0000~\u0000Dq\u0000~\u0000bq\u0000~\u0000wq\u0000~\u0000mq\u0000~\u0000Zq\u0000~\u0000\u0005q\u0000~\u0000fq\u0000~\u0000kq\u0000~\u0000nq\u0000~\u0000\u000b"
+"x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.protocol.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolRolesTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("RoleName" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("UserRoles" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  7 :
                        if (("ProtocolNumber" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            _getUserRoles().add(((edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolUserRolesTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.protocol.impl.ProtocolUserRolesTypeImpl.class), 8, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Role" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("UserRoles" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
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
                        if (("Role" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  8 :
                        if (("UserRoles" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("RoleName" == ___local)&&("http://www.w3.org/2001/ProtocolSummarySchema" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  9 :
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
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
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

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Role = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Role = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _RoleName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
