//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.2-b15-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2005.05.13 at 09:45:36 EDT 
//


package edu.mit.coeus.utils.xml.bean.template.impl;

public class ContactTypeImpl implements edu.mit.coeus.utils.xml.bean.template.ContactType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    protected edu.mit.coeus.utils.xml.bean.template.RolodexDetailsType _RolodexDetails;
    protected boolean has_ContactTypeCode;
    protected int _ContactTypeCode;
    protected java.lang.String _ContactTypeDesc;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.template.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.template.ContactType.class);
    }

    public edu.mit.coeus.utils.xml.bean.template.RolodexDetailsType getRolodexDetails() {
        return _RolodexDetails;
    }

    public void setRolodexDetails(edu.mit.coeus.utils.xml.bean.template.RolodexDetailsType value) {
        _RolodexDetails = value;
    }

    public int getContactTypeCode() {
        return _ContactTypeCode;
    }

    public void setContactTypeCode(int value) {
        _ContactTypeCode = value;
        has_ContactTypeCode = true;
    }

    public java.lang.String getContactTypeDesc() {
        return _ContactTypeDesc;
    }

    public void setContactTypeDesc(java.lang.String value) {
        _ContactTypeDesc = value;
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.template.impl.ContactTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (has_ContactTypeCode) {
            context.startElement("", "ContactTypeCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _ContactTypeCode)), "ContactTypeCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ContactTypeDesc!= null) {
            context.startElement("", "ContactTypeDesc");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ContactTypeDesc), "ContactTypeDesc");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_RolodexDetails!= null) {
            context.startElement("", "RolodexDetails");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _RolodexDetails), "RolodexDetails");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _RolodexDetails), "RolodexDetails");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _RolodexDetails), "RolodexDetails");
            context.endElement();
        }
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
        return (edu.mit.coeus.utils.xml.bean.template.ContactType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0003I\u0000\u000ecachedHashCodeL\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava"
+"/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0002xp\n\u009a\u0084\u00f7ppsq\u0000~\u0000\u0000\u0006\u00b0\u00fd\u00eappsr\u0000\u001dcom"
+".sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001\u0002\u00dd\u009cPppsr\u0000\'com.sun"
+".msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLc"
+"om/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Element"
+"Exp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000"
+"~\u0000\u0002xq\u0000~\u0000\u0003\u0002\u00dd\u009cEsr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq"
+"\u0000~\u0000\u0000\u0002\u00dd\u009c:ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001f"
+"Lorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/"
+"sun/msv/util/StringPair;xq\u0000~\u0000\u0003\u0000kI}ppsr\u0000 com.sun.msv.datatype"
+".xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xsd.IntegerD"
+"erivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/sun/msv/datatype/x"
+"sd/XSDatatypeImpl;xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomic"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001aL\u0000\nwhit"
+"eSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 "
+"http://www.w3.org/2001/XMLSchemat\u0000\u0003intsr\u00005com.sun.msv.dataty"
+"pe.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.ms"
+"v.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*com.sun.m"
+"sv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv."
+"datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang"
+"/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstr"
+"aintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWit"
+"hFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbase"
+"Typeq\u0000~\u0000\u0016L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concret"
+"eType;L\u0000\tfacetNameq\u0000~\u0000\u001axq\u0000~\u0000\u0019ppq\u0000~\u0000!\u0000\u0001sr\u0000*com.sun.msv.dataty"
+"pe.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000#ppq\u0000~\u0000!\u0000\u0000sr\u0000!com.su"
+"n.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0015q\u0000~\u0000\u001dt\u0000\u0004longq\u0000~\u0000"
+"!sq\u0000~\u0000\"ppq\u0000~\u0000!\u0000\u0001sq\u0000~\u0000)ppq\u0000~\u0000!\u0000\u0000sr\u0000$com.sun.msv.datatype.xsd."
+"IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0015q\u0000~\u0000\u001dt\u0000\u0007integerq\u0000~\u0000!sr\u0000,com.sun."
+"msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;c"
+"om.sun.msv.datatype.xsd.DataTypeWithLexicalConstraintFacetT\u0090"
+"\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000&ppq\u0000~\u0000!\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0007decimalq\u0000~\u0000!q\u0000~\u00007t\u0000\u000efractionDigi"
+"ts\u0000\u0000\u0000\u0000q\u0000~\u00001t\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005va"
+"luexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u00001t\u0000\fmaxInclu"
+"sivesq\u0000~\u0000;\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000,q\u0000~\u0000:sr\u0000\u0011java.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001"
+"I\u0000\u0005valuexq\u0000~\u0000<\u0080\u0000\u0000\u0000q\u0000~\u0000,q\u0000~\u0000>sq\u0000~\u0000@\u007f\u00ff\u00ff\u00ffsr\u00000com.sun.msv.gramma"
+"r.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003\u0000\u0000\u0000\nppsr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001aL\u0000\fnames"
+"paceURIq\u0000~\u0000\u001axpq\u0000~\u0000\u001eq\u0000~\u0000\u001dsq\u0000~\u0000\u0007\u0002rR\u00b8ppsr\u0000 com.sun.msv.grammar."
+"AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\nxq\u0000~\u0000\u0003\u0002rR"
+"\u00adq\u0000~\u0000\u000epsq\u0000~\u0000\u0010\u0001\u00e4\u0085Oppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0005QNameq\u0000~\u0000!q\u0000~\u0000Dsq\u0000~\u0000Eq\u0000~\u0000Mq\u0000~\u0000\u001dsr\u0000#com"
+".sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001a"
+"L\u0000\fnamespaceURIq\u0000~\u0000\u001axr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u0000"
+"0com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000\u0003\u0000\u0000\u0000\tsq\u0000~\u0000\r\u0001psq\u0000~\u0000Ot\u0000\u000fContactTypeCodet\u0000\u0000q\u0000~\u0000Usq\u0000~\u0000\u0007\u0003\u00d3a\u0095"
+"ppsq\u0000~\u0000\t\u0003\u00d3a\u008aq\u0000~\u0000\u000ep\u0000sq\u0000~\u0000\u0000\u0003\u00d3a\u007fppsq\u0000~\u0000\u0010\u0000\u0090\u0088\u0091ppsr\u0000#com.sun.msv.d"
+"atatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0017q\u0000~\u0000\u001d"
+"t\u0000\u0006stringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Pr"
+"eserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 \u0001q\u0000~\u0000Dsq\u0000~\u0000Eq\u0000~\u0000`q\u0000~\u0000\u001dsq\u0000~\u0000\u0007\u0003B\u00d8\u00e9ppsq\u0000"
+"~\u0000H\u0003B\u00d8\u00deq\u0000~\u0000\u000epq\u0000~\u0000Jsq\u0000~\u0000Oq\u0000~\u0000Rq\u0000~\u0000Sq\u0000~\u0000Usq\u0000~\u0000Ot\u0000\u000fContactTypeD"
+"escq\u0000~\u0000Yq\u0000~\u0000Usq\u0000~\u0000\u0007\u0003\u00e9\u0087\bppsq\u0000~\u0000\t\u0003\u00e9\u0086\u00fdq\u0000~\u0000\u000ep\u0000sq\u0000~\u0000\u0000\u0003\u00e9\u0086\u00f2ppsq\u0000~\u0000\t"
+"\u0001h~\u009epp\u0000sq\u0000~\u0000\u0007\u0001h~\u0093ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002"
+"xq\u0000~\u0000\u0003\u0001h~\u0088q\u0000~\u0000\u000epsq\u0000~\u0000H\u0001h~\u0085q\u0000~\u0000\u000epsr\u00002com.sun.msv.grammar.Expr"
+"ession$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003\u0000\u0000\u0000\bq\u0000~\u0000Vq\u0000~\u0000ssr\u0000"
+" com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Pq\u0000~\u0000Usq\u0000~\u0000"
+"Ot\u00008edu.mit.coeus.utils.xml.bean.template.RolodexDetailsType"
+"t\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0007\u0002\u0081\bOppsq"
+"\u0000~\u0000H\u0002\u0081\bDq\u0000~\u0000\u000epq\u0000~\u0000Jsq\u0000~\u0000Oq\u0000~\u0000Rq\u0000~\u0000Sq\u0000~\u0000Usq\u0000~\u0000Ot\u0000\u000eRolodexDeta"
+"ilsq\u0000~\u0000Yq\u0000~\u0000Usr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHa"
+"sh;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8"
+"\u00ed\u001c\u0002\u0000\u0004I\u0000\u0005countI\u0000\tthresholdL\u0000\u0006parentq\u0000~\u0000\u007f[\u0000\u0005tablet\u0000![Lcom/sun/"
+"msv/grammar/Expression;xp\u0000\u0000\u0000\r\u0000\u0000\u00009pur\u0000![Lcom.sun.msv.grammar."
+"Expression;\u00d68D\u00c3]\u00ad\u00a7\n\u0002\u0000\u0000xp\u0000\u0000\u0000\u00bfq\u0000~\u0000mpppppq\u0000~\u0000\u0005ppppppppppppppppp"
+"ppppppq\u0000~\u0000dpppppppppppppppppq\u0000~\u0000Gpppppppppppppq\u0000~\u0000yppppppppp"
+"ppppppq\u0000~\u0000kpppppppppppppq\u0000~\u0000\u0006pppppppq\u0000~\u0000ipppppppq\u0000~\u0000\\ppppppp"
+"ppppppppppq\u0000~\u0000\u000fpppq\u0000~\u0000Zpppppppppppppppppq\u0000~\u0000\bppppppppppppppp"
+"ppppppppppppppppq\u0000~\u0000ppppppppppp"));
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
            return edu.mit.coeus.utils.xml.bean.template.impl.ContactTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  6 :
                        if (("RolodexDetails" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  0 :
                        if (("ContactTypeCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  7 :
                        if (("RolodexId" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("LastName" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("FirstName" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("MiddleName" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Suffix" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Prefix" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Title" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Organization" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Address1" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Address2" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Address3" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Fax" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Email" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("City" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("County" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("StateCode" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("StateDescription" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("PostalCode" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("Comments" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("PhoneNumber" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("CountryCode" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("CountryDescription" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SponsorCode" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SponsorName" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("OwnedByUnit" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("OwnedByUnitName" == ___local)&&("" == ___uri)) {
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  3 :
                        if (("ContactTypeDesc" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
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
                        state = 9;
                        continue outer;
                    case  2 :
                        if (("ContactTypeCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ContactTypeDesc" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("RolodexDetails" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  7 :
                        _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  3 :
                        state = 6;
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
                        case  6 :
                            state = 9;
                            continue outer;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  9 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            _RolodexDetails = ((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.template.impl.RolodexDetailsTypeImpl.class), 8, value));
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
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
                _ContactTypeCode = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_ContactTypeCode = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ContactTypeDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
