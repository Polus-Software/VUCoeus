//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class ContractPrimeRecipientReportImpl
    extends edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl
    implements edu.mit.coeus.utils.xml.bean.arra.ContractPrimeRecipientReport, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected boolean _Nil;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.ContractPrimeRecipientReport.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "urn:us:gov:recoveryrr-ext";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "ContractPrimeRecipientReport";
    }

    public boolean isNil() {
        return _Nil;
    }

    public void setNil(boolean value) {
        _Nil = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("urn:us:gov:recoveryrr-ext", "ContractPrimeRecipientReport");
        if (_Nil) {
            context.getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", null, true);
        } else {
            super.serializeURIs(context);
        }
        context.endNamespaceDecls();
        if (_Nil) {
            context.startAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Nil)), "Nil");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        } else {
            super.serializeAttributes(context);
        }
        context.endAttributes();
        if (!_Nil) {
            super.serializeBody(context);
        }
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.arra.ContractPrimeRecipientReport.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv."
+"grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000"
+"\fcontentModelt\u0000 Lcom/sun/msv/grammar/Expression;xr\u0000\u001ecom.sun."
+"msv.grammar.Expression\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Lj"
+"ava/lang/Boolean;L\u0000\u000bexpandedExpq\u0000~\u0000\u0003xppp\u0000sr\u0000\u001dcom.sun.msv.gra"
+"mmar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.BinaryExp\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1q\u0000~\u0000\u0003L\u0000\u0004exp2q\u0000~\u0000\u0003xq\u0000~\u0000\u0004ppsr\u0000 com.sun.msv.gra"
+"mmar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~"
+"\u0000\u0004ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/"
+"relaxng/datatype/Datatype;L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/String"
+"Pair;L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004ppsr\u0000$com.sun.msv.da"
+"tatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xs"
+"d.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.C"
+"oncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatyp"
+"eImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeN"
+"ameq\u0000~\u0000\u0015L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpace"
+"Processor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007booleansr\u0000"
+"5com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xpsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalName"
+"q\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xpq\u0000~\u0000\u0019t\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080"
+"\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0001sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0015L\u0000\fnamespaceURIq\u0000~\u0000\u0015xr\u0000\u001dcom.sun.msv"
+".grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0003nilt\u0000)http://www.w3.org/20"
+"01/XMLSchema-instancesr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000"
+"\u0003xq\u0000~\u0000\u0004sq\u0000~\u0000 \u0000psq\u0000~\u0000\nq\u0000~\u00004psr\u00002com.sun.msv.grammar.Expressio"
+"n$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u00007sr\u0000 com.sun."
+"msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000#sr\u00000com.sun.msv.gra"
+"mmar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u0000;"
+"sq\u0000~\u0000\"t\u00005edu.mit.coeus.utils.xml.bean.arra.ContractAwardDeta"
+"ilt\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0000pp\u0000sq\u0000"
+"~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007pps"
+"q\u0000~\u00001q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u00007q\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000\"t\u00009edu.mit.coeus"
+".utils.xml.bean.arra.ContractAwardDetailTypeq\u0000~\u0000>sq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000\nq\u0000~\u00004psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000"
+"\rL\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004nameq\u0000~\u0000\u000exq\u0000~\u0000\u0004ppsr\u0000\"com.sun.msv.datatype."
+"xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005QNameq\u0000~\u0000\u001csr\u00000com.sun."
+"msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~"
+"\u00004psq\u0000~\u0000\u001dq\u0000~\u0000Pq\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0004typeq\u0000~\u0000&q\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u0013ContractA"
+"wardDetailt\u0000\u0019urn:us:gov:recoveryrr-extsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000"
+"\u0007ppsq\u0000~\u00001q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u00007q\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000\"t\u00000edu.mit.c"
+"oeus.utils.xml.bean.arra.PrimeRecipientq\u0000~\u0000>sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007p"
+"psq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"1q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u00007q\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000\"t\u00004edu.mit.coeus.uti"
+"ls.xml.bean.arra.PrimeRecipientTypeq\u0000~\u0000>sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00004p"
+"q\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u000ePrimeRecipientq\u0000~\u0000Xsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u00001q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u00007q\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000\"t\u00004edu."
+"mit.coeus.utils.xml.bean.arra.PlaceOfPerformanceq\u0000~\u0000>sq\u0000~\u0000\u0000p"
+"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u00001q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u00007q\u0000~\u00009q\u0000~\u0000;sq\u0000~\u0000\"t\u00008edu.mit."
+"coeus.utils.xml.bean.arra.PlaceOfPerformanceTypeq\u0000~\u0000>sq\u0000~\u0000\u0007p"
+"psq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u0012PlaceOfPerformanceq\u0000~\u0000X"
+"sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00004psq\u0000~\u0000Lppsr\u0000\u001fcom.sun.msv.datatype.xsd.IDT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.NcnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com"
+".sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidx"
+"q\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0002IDq\u0000~\u0000\u001c\u0000q\u0000~\u0000Rsq\u0000~\u0000\u001dq\u0000~\u0000\u008dq\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0002idt\u0000#http"
+"://niem.gov/niem/structures/2.0q\u0000~\u0000;sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00004psq\u0000~"
+"\u0000Lppsr\u0000*com.sun.msv.datatype.xsd.DatatypeFactory$1\u00a1\u00f3\u000b\u00e3`rj\u000e\u0002\u0000"
+"\u0000xr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypet\u0000)"
+"Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u0014q\u0000~\u0000\u0018t\u0000\u0006IDREF"
+"Sq\u0000~\u0000\u001csr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"I\u0000\tminLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueCo"
+"nstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTyp"
+"eWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\b"
+"baseTypeq\u0000~\u0000\u0097L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Con"
+"creteType;L\u0000\tfacetNameq\u0000~\u0000\u0015xq\u0000~\u0000\u0014ppq\u0000~\u0000\u001c\u0000\u0000sr\u0000!com.sun.msv.da"
+"tatype.xsd.ListType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bitemTypeq\u0000~\u0000\u0097xq\u0000~\u0000\u0013ppq\u0000~\u0000\u001cs"
+"r\u0000\"com.sun.msv.datatype.xsd.IDREFType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0089q\u0000~\u0000\u0018t"
+"\u0000\u0005IDREFq\u0000~\u0000\u001c\u0000q\u0000~\u0000\u00a0t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000Rpsq\u0000~\u0000\"t\u0000\flinkMetadat"
+"aq\u0000~\u0000\u0091q\u0000~\u0000;sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u0000\u0094sq\u0000~\u0000\"t\u0000\bmetadataq\u0000~\u0000\u0091q\u0000"
+"~\u0000;sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00004pq\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000;sq\u0000~\u0000\"t\u0000\u001cContractPrime"
+"RecipientReportq\u0000~\u0000Xsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$C"
+"losedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHas"
+"h\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv"
+"/grammar/ExpressionPool;xp\u0000\u0000\u0000#\u0001pq\u0000~\u0000-q\u0000~\u0000Cq\u0000~\u0000dq\u0000~\u0000zq\u0000~\u0000oq\u0000~"
+"\u0000Jq\u0000~\u0000kq\u0000~\u0000\u0081q\u0000~\u0000\u00abq\u0000~\u0000@q\u0000~\u00003q\u0000~\u0000Fq\u0000~\u0000\\q\u0000~\u0000gq\u0000~\u0000rq\u0000~\u0000}q\u0000~\u0000,q\u0000~"
+"\u0000(q\u0000~\u0000)q\u0000~\u0000\u0085q\u0000~\u0000wq\u0000~\u0000\tq\u0000~\u0000aq\u0000~\u0000*q\u0000~\u0000\u00a7q\u0000~\u00000q\u0000~\u0000Eq\u0000~\u0000[q\u0000~\u0000fq\u0000~"
+"\u0000qq\u0000~\u0000|q\u0000~\u0000+q\u0000~\u0000.q\u0000~\u0000Yq\u0000~\u0000\u0092x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.arra.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
            super(context, "------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 2;
                            eatText1(v);
                            continue outer;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ContractAwardDetail" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("ContractAwardDetail" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("ContractPrimeRecipientReport" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
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

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Nil = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value)));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
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
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("ContractPrimeRecipientReport" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 2;
                            eatText1(v);
                            continue outer;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case  1 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case  1 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 2;
                            eatText1(v);
                            continue outer;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 2;
                            return ;
                        }
                        break;
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
                        case  1 :
                            attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 2;
                                eatText1(v);
                                continue outer;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractPrimeRecipientReportImpl.this).new Unmarshaller(context)), 2, value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText1(value);
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
