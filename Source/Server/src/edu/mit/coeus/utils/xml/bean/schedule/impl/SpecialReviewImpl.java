//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule.impl;

public class SpecialReviewImpl
    extends edu.mit.coeus.utils.xml.bean.schedule.impl.SpecialReviewTypeImpl
    implements edu.mit.coeus.utils.xml.bean.schedule.SpecialReview, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.schedule.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.schedule.SpecialReview.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://irb.mit.edu/irbnamespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "SpecialReview";
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.schedule.impl.SpecialReviewImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://irb.mit.edu/irbnamespace", "SpecialReview");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.schedule.SpecialReview.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/r"
+"elaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/ms"
+"v/util/StringPair;xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005v"
+"aluexp\u0000psr\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000+com.sun.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\n"
+"baseFacetst\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xr\u0000*co"
+"m.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.s"
+"un.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.d"
+"atatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava"
+"/lang/String;L\u0000\btypeNameq\u0000~\u0000 L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/da"
+"tatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/X"
+"MLSchemat\u0000\u0007integersr\u00005com.sun.msv.datatype.xsd.WhiteSpacePro"
+"cessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.White"
+"SpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000,com.sun.msv.datatype.xsd.Frac"
+"tionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.x"
+"sd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xr\u0000*com.sun."
+"msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixed"
+"Z\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000\u001cL\u0000\fconcreteTypet\u0000\'Lcom/"
+"sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000 xq\u0000~\u0000\u001fppq"
+"\u0000~\u0000\'\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u001dq\u0000~\u0000#t\u0000\u0007decimalq\u0000~\u0000\'q\u0000~\u0000.t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000sr\u00000com.sun."
+"msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~"
+"\u0000\u0019psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~"
+"\u0000 L\u0000\fnamespaceURIq\u0000~\u0000 xpq\u0000~\u0000$q\u0000~\u0000#sr\u0000\u001dcom.sun.msv.grammar.Ch"
+"oiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.AttributeE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0019psq\u0000~\u0000\u0014p"
+"psr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dq\u0000~\u0000"
+"#t\u0000\u0005QNameq\u0000~\u0000\'q\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000#sr\u0000#com.sun.msv.grammar.S"
+"impleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000 L\u0000\fnamespaceURIq\u0000~"
+"\u0000 xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)ht"
+"tp://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gramm"
+"ar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0018\u0001q\u0000~\u0000D"
+"sq\u0000~\u0000>t\u0000\u0013SpecialReviewNumbert\u0000\u001fhttp://irb.mit.edu/irbnamespa"
+"cesq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0017sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Ds"
+"q\u0000~\u0000>t\u0000\u0015SpecialReviewTypeCodeq\u0000~\u0000Hsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014pps"
+"r\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxL"
+"engthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstrain"
+"tFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xq\u0000~\u0000*q\u0000~\u0000Hpsr\u00005com.sun.msv.datatype.xsd.Wh"
+"iteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000&\u0000\u0000sr\u0000#com.sun.msv"
+".datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u001dq\u0000~"
+"\u0000#t\u0000\u0006stringq\u0000~\u0000V\u0001q\u0000~\u0000Xt\u0000\tmaxLength\u0000\u0000\u0000\u00c8q\u0000~\u00002sq\u0000~\u00003t\u0000\u000estring-d"
+"erivedq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0015Speci"
+"alReviewTypeDescq\u0000~\u0000Hsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u0017sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~"
+"\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u001dSpecialReviewApprovalTypeCodeq\u0000~\u0000"
+"Hsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014ppsq\u0000~\u0000Rq\u0000~\u0000Hpq\u0000~\u0000V\u0000\u0000q\u0000~\u0000Xq\u0000~\u0000Xq\u0000~\u0000Z"
+"\u0000\u0000\u0000\u00c8q\u0000~\u00002sq\u0000~\u00003t\u0000\u000estring-derivedq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~"
+"\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u001dSpecialReviewApprovalTypeDescq\u0000~\u0000Hsq\u0000~\u0000"
+"5ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014ppsq\u0000~\u0000Rq\u0000~\u0000Hpq\u0000~\u0000V\u0000\u0000q\u0000~\u0000Xq\u0000~\u0000"
+"Xq\u0000~\u0000Z\u0000\u0000\u0000\u0014q\u0000~\u00002sq\u0000~\u00003t\u0000\u000estring-derivedq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~"
+"\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u001bSpecialReviewProtocolNumberq\u0000~\u0000Hq"
+"\u0000~\u0000Dsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014q\u0000~\u0000\u0019psr\u0000!com.sun.msv"
+".datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xs"
+"d.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u001dq\u0000~\u0000#t\u0000\u0004dateq\u0000~\u0000\'q\u0000~\u00002sq\u0000"
+"~\u00003q\u0000~\u0000\u0083q\u0000~\u0000#sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u001cSpe"
+"cialReviewApplicationDateq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~"
+"\u0000\u0007ppq\u0000~\u0000\u007fsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0019Special"
+"ReviewApprovalDateq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\u0014q\u0000~\u0000\u0019pq\u0000~\u0000Xq\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000Yq\u0000~\u0000#sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009"
+"q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0015SpecialReviewCommentsq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u00005ppsq"
+"\u0000~\u00007q\u0000~\u0000\u0019pq\u0000~\u00009q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\rSpecialReviewq\u0000~\u0000Hsr\u0000\"com."
+"sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/"
+"sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv."
+"grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstrea"
+"mVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000"
+"\u0000 \u0001pq\u0000~\u0000\u000eq\u0000~\u0000\u000bq\u0000~\u0000\fq\u0000~\u0000hq\u0000~\u0000\tq\u0000~\u0000|q\u0000~\u0000\u0089q\u0000~\u0000\u000fq\u0000~\u0000\rq\u0000~\u0000sq\u0000~\u0000\u0013q"
+"\u0000~\u0000Jq\u0000~\u0000bq\u0000~\u0000\u0090q\u0000~\u0000\nq\u0000~\u0000\u0010q\u0000~\u0000\u0011q\u0000~\u0000Pq\u0000~\u00006q\u0000~\u0000Kq\u0000~\u0000]q\u0000~\u0000cq\u0000~\u0000mq"
+"\u0000~\u0000xq\u0000~\u0000\u0085q\u0000~\u0000\u008cq\u0000~\u0000\u0095q\u0000~\u0000\u0099q\u0000~\u0000~q\u0000~\u0000\u008bq\u0000~\u0000\u0092q\u0000~\u0000qx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.schedule.impl.SpecialReviewImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("SpecialReviewNumber" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.schedule.impl.SpecialReviewTypeImpl)edu.mit.coeus.utils.xml.bean.schedule.impl.SpecialReviewImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("SpecialReview" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
                        if (("SpecialReview" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
