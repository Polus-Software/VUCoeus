//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.15 at 04:17:49 GMT+05:30 
//


package edu.mit.coeuslite.coiv2.jaxbclasses.impl;

public class COIDISCLPROJECTSImpl
    extends edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSTypeImpl
    implements edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallableObject, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializable, edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeuslite.coiv2.jaxbclasses.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "COI_DISCL_PROJECTS";
    }

    public edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("", "COI_DISCL_PROJECTS");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeuslite.coiv2.jaxbclasses.COIDISCLPROJECTS.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000"
+"~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000#com.sun.msv.datatype.xsd"
+".StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dataty"
+"pe.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype."
+"xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDa"
+"tatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\b"
+"typeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/White"
+"SpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006strin"
+"gsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d"
+"\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xpq\u0000~\u0000%q\u0000~\u0000$sr\u0000\u001dcom"
+".sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv"
+".grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001"
+"xq\u0000~\u0000\u0004q\u0000~\u0000\u001cpsq\u0000~\u0000\u0017ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.Whi"
+"teSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\'q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u00004q\u0000"
+"~\u0000$sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tloca"
+"lNameq\u0000~\u0000!L\u0000\fnamespaceURIq\u0000~\u0000!xr\u0000\u001dcom.sun.msv.grammar.NameCl"
+"ass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-i"
+"nstancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u001b\u0001psq\u0000~\u00008t\u0000\u0015COI_DISCLOSURE_NUMBERt\u0000\u0000sq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u0000\u001cpsr\u0000#com.sun.msv.datatype.xsd.Numbe"
+"rType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\u0007decimalq\u0000~\u00006q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000Hq\u0000"
+"~\u0000$sq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u000fSEQUENCE_NUMB"
+"ERq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q"
+"\u0000~\u0000>sq\u0000~\u00008t\u0000\u000eCOI_PROJECT_IDq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-"
+"ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0011COI_PROJECT_TITLEq\u0000~\u0000B"
+"sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000"
+"~\u00008t\u0000\u0010COI_PROJECT_TYPEq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000"
+"~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0013COI_PROJECT_SPONSORq\u0000~\u0000Bsq\u0000"
+"~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u0000\u001cpsr\u0000%com.sun.msv.datatype.xsd.DateT"
+"imeType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseT"
+"ype\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u001eq\u0000~\u0000$t\u0000\bdateTimeq\u0000~\u00006q\u0000~\u0000*sq\u0000~\u0000+q\u0000~\u0000lq\u0000~"
+"\u0000$sq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0016COI_PROJECT_ST"
+"ART_DATEq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000hsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001"
+"q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0014COI_PROJECT_END_DATEq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000\u0000q\u0000"
+"~\u0000\u001cp\u0000sq\u0000~\u0000-ppsq\u0000~\u0000/ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0018L\u0000\u0004nameq\u0000~\u0000\u0019L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000"
+"\u0004ppsr\u0000$com.sun.msv.datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001e"
+"q\u0000~\u0000$t\u0000\u0007booleanq\u0000~\u00006sq\u0000~\u0000+q\u0000~\u0000\u0081q\u0000~\u0000Bq\u0000~\u0000?sq\u0000~\u00008t\u0000\u0003nilq\u0000~\u0000<sq"
+"\u0000~\u0000\u0007ppq\u0000~\u0000Esq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u001aCOI_P"
+"ROJECT_FUNDING_AMOUNTq\u0000~\u0000Bq\u0000~\u0000>sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-p"
+"psq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0010COI_PROJECT_ROLEq\u0000~\u0000Bsq"
+"\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000hsq\u0000~\u0000-ppsq\u0000~\u0000/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u0000"
+"8t\u0000\u0010UPDATE_TIMESTAMPq\u0000~\u0000Bsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001asq\u0000~\u0000-ppsq\u0000~\u0000"
+"/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u000bUPDATE_USERq\u0000~\u0000Bsq\u0000~\u0000-ppsq\u0000~\u0000"
+"/q\u0000~\u0000\u001cpq\u0000~\u00001q\u0000~\u0000:q\u0000~\u0000>sq\u0000~\u00008t\u0000\u0012COI_DISCL_PROJECTSq\u0000~\u0000Bsr\u0000\"co"
+"m.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lco"
+"m/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.ms"
+"v.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstr"
+"eamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp"
+"\u0000\u0000\u0000\'\u0001pq\u0000~\u0000zq\u0000~\u0000\nq\u0000~\u0000\rq\u0000~\u0000\u0014q\u0000~\u0000\tq\u0000~\u0000.q\u0000~\u0000Jq\u0000~\u0000Pq\u0000~\u0000Vq\u0000~\u0000\\q\u0000~\u0000"
+"bq\u0000~\u0000nq\u0000~\u0000tq\u0000~\u0000\u0011q\u0000~\u0000\u0086q\u0000~\u0000\u008cq\u0000~\u0000\u0092q\u0000~\u0000\u0098q\u0000~\u0000\u009cq\u0000~\u0000gq\u0000~\u0000sq\u0000~\u0000\u0091q\u0000~\u0000"
+"\u000fq\u0000~\u0000\u000eq\u0000~\u0000\u0012q\u0000~\u0000\u0010q\u0000~\u0000\u0016q\u0000~\u0000Oq\u0000~\u0000Uq\u0000~\u0000[q\u0000~\u0000aq\u0000~\u0000\u008bq\u0000~\u0000\fq\u0000~\u0000\u0097q\u0000~\u0000"
+"xq\u0000~\u0000\u000bq\u0000~\u0000\u0013q\u0000~\u0000Dq\u0000~\u0000\u0085x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeuslite.coiv2.jaxbclasses.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("COI_DISCL_PROJECTS" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("COI_DISCLOSURE_NUMBER" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSTypeImpl)edu.mit.coeuslite.coiv2.jaxbclasses.impl.COIDISCLPROJECTSImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                        if (("COI_DISCL_PROJECTS" == ___local)&&("" == ___uri)) {
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
