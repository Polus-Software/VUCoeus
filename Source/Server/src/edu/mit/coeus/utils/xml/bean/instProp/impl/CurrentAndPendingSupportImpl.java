//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.29 at 05:02:05 PM IST 
//


package edu.mit.coeus.utils.xml.bean.instProp.impl;

public class CurrentAndPendingSupportImpl
    extends edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl
    implements edu.mit.coeus.utils.xml.bean.instProp.CurrentAndPendingSupport, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.instProp.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.instProp.CurrentAndPendingSupport.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "CurrentAndPendingSupport";
    }

    public edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "CurrentAndPendingSupport");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.instProp.CurrentAndPendingSupport.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001cco"
+"m.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004sr\u0000\u0011ja"
+"va.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psr\u0000 com.sun.msv.grammar.Attribut"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0014psr\u00002c"
+"om.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0004sq\u0000~\u0000\u0013\u0001q\u0000~\u0000\u001dsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.s"
+"un.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004"
+"q\u0000~\u0000\u001eq\u0000~\u0000#sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002"
+"L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000%xq\u0000~\u0000 t"
+"\u0000Uedu.mit.coeus.utils.xml.bean.instProp.CurrentAndPendingSup"
+"portType.CurrentSupportTypet\u0000+http://java.sun.com/jaxb/xjc/d"
+"ummy-elementssq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014psr\u0000\u001bcom.sun.msv.grammar.Dat"
+"aExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exc"
+"eptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv."
+"datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dat"
+"atype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xs"
+"d.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000%L\u0000\btypeNameq\u0000"
+"~\u0000%L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProce"
+"ssor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xps"
+"r\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocal"
+"Nameq\u0000~\u0000%L\u0000\fnamespaceURIq\u0000~\u0000%xpq\u0000~\u00006q\u0000~\u00005sq\u0000~\u0000$t\u0000\u0004typet\u0000)htt"
+"p://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000eCurrentS"
+"upportt\u0000\u0000q\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000U"
+"edu.mit.coeus.utils.xml.bean.instProp.CurrentAndPendingSuppo"
+"rtType.PendingSupportTypeq\u0000~\u0000(sq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000.q\u0000~\u0000>"
+"q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000ePendingSupportq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000s"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000+ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u00000q\u0000~\u00005t\u0000\u0006stringsr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00008\u0001q\u0000~\u0000"
+";sq\u0000~\u0000<q\u0000~\u0000Xq\u0000~\u00005sq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000"
+"\nPersonNameq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000s"
+"q\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000aedu.m"
+"it.coeus.utils.xml.bean.instProp.CurrentAndPendingSupportTyp"
+"e.CurrentReportCEColumnNamesTypeq\u0000~\u0000(sq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~"
+"\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u001aCurrentReportCEColumnNamesq\u0000~\u0000Cq\u0000~\u0000#sq\u0000"
+"~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0014p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u000eppsq\u0000~\u0000\u0010q\u0000~\u0000\u0014psq\u0000~\u0000"
+"\u001aq\u0000~\u0000\u0014pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t\u0000aedu.mit.coeus.utils.xml.bean."
+"instProp.CurrentAndPendingSupportType.PendingReportCEColumnN"
+"amesTypeq\u0000~\u0000(sq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u001aPen"
+"dingReportCEColumnNamesq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u000eppsq\u0000~\u0000\u001aq\u0000~\u0000\u0014pq\u0000~\u0000.q\u0000"
+"~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0018CurrentAndPendingSupportq\u0000~\u0000Csr\u0000\"com.sun.ms"
+"v.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/ms"
+"v/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramma"
+"r.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersi"
+"onL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u001f\u0001pq\u0000"
+"~\u0000\nq\u0000~\u0000\u0018q\u0000~\u0000Iq\u0000~\u0000\fq\u0000~\u0000dq\u0000~\u0000qq\u0000~\u0000\u0012q\u0000~\u0000Eq\u0000~\u0000\u000fq\u0000~\u0000Dq\u0000~\u0000\rq\u0000~\u0000\u000bq\u0000"
+"~\u0000)q\u0000~\u0000Nq\u0000~\u0000\\q\u0000~\u0000iq\u0000~\u0000vq\u0000~\u0000zq\u0000~\u0000Tq\u0000~\u0000\u0016q\u0000~\u0000Gq\u0000~\u0000bq\u0000~\u0000oq\u0000~\u0000Rq\u0000"
+"~\u0000`q\u0000~\u0000mq\u0000~\u0000\tq\u0000~\u0000\u0019q\u0000~\u0000Jq\u0000~\u0000eq\u0000~\u0000rx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.instProp.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("CurrentAndPendingSupport" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("CurrentSupport" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("PendingSupport" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("PersonName" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("CurrentReportCEColumnNames" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("PendingReportCEColumnNames" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
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
                    case  1 :
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("CurrentAndPendingSupport" == ___local)&&("" == ___uri)) {
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
                    case  1 :
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportTypeImpl)edu.mit.coeus.utils.xml.bean.instProp.impl.CurrentAndPendingSupportImpl.this).new Unmarshaller(context)), 2, value);
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