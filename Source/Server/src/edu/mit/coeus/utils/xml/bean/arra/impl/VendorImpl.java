//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class VendorImpl
    extends edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl
    implements edu.mit.coeus.utils.xml.bean.arra.Vendor, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected boolean _Nil;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.Vendor.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "urn:us:gov:recoveryrr-ext";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Vendor";
    }

    public boolean isNil() {
        return _Nil;
    }

    public void setNil(boolean value) {
        _Nil = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("urn:us:gov:recoveryrr-ext", "Vendor");
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
        return (edu.mit.coeus.utils.xml.bean.arra.Vendor.class);
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
+"\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\'ppsr\u0000\u001bcom."
+"sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\rL\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000"
+"\u0004nameq\u0000~\u0000\u000exq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFace"
+"t\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTyp"
+"eWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype"
+".xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValue"
+"CheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeI"
+"mpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType"
+";L\u0000\tfacetNameq\u0000~\u0000\u0015xq\u0000~\u0000\u0014t\u0000\u0019urn:us:gov:recoveryrr-extpsr\u00005com"
+".sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u001b\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z"
+"\u0000\risAlwaysValidxq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0006stringq\u0000~\u0000?\u0001q\u0000~\u0000At\u0000\tmaxLength\u0000\u0000"
+"\u00002sr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000 \u0000psq\u0000~\u0000\u001dt\u0000\u000estring-derivedq\u0000~\u0000=sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\nq\u0000~\u0000Fpsq\u0000~\u00005ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005QNameq\u0000~\u0000\u001cq\u0000~\u0000Esq\u0000~\u0000\u001dq\u0000~\u0000Nq\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0004ty"
+"peq\u0000~\u0000&sr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\rAwardIdNumberq\u0000~\u0000=sq\u0000~\u0000\u0007p"
+"psq\u0000~\u0000\u0000q\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'pps"
+"q\u0000~\u00005q\u0000~\u0000Fpsq\u0000~\u00007q\u0000~\u0000=pq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000Aq\u0000~\u0000C\u0000\u0000\u00007q\u0000~\u0000Esq\u0000~\u0000\u001d"
+"t\u0000\u000estring-derivedq\u0000~\u0000=sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000"
+"~\u0000\"t\u0000\u000eSubAwardNumberq\u0000~\u0000=q\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000q\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u00005q\u0000~\u0000Fpsq\u0000~\u00007t\u0000\"htt"
+"p://niem.gov/niem/niem-core/2.0pq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000Aq\u0000~\u0000C\u0000\u0000\u00007q\u0000"
+"~\u0000Esq\u0000~\u0000\u001dt\u0000\u000estring-derivedq\u0000~\u0000lsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000"
+"Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\u0010OrganizationNameq\u0000~\u0000lq\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000q\u0000~\u0000F"
+"p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u00005q\u0000~\u0000Fps"
+"q\u0000~\u00007q\u0000~\u0000=pq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000Aq\u0000~\u0000C\u0000\u0000\u0000\tq\u0000~\u0000Esq\u0000~\u0000\u001dt\u0000\u000estring-de"
+"rivedq\u0000~\u0000=sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\nVendor"
+"DUNSq\u0000~\u0000=q\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000q\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~"
+"\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u00005q\u0000~\u0000Fpsq\u0000~\u00007q\u0000~\u0000lpq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000"
+"Aq\u0000~\u0000C\u0000\u0000\u0000\u0005q\u0000~\u0000Esq\u0000~\u0000\u001dt\u0000\u000estring-derivedq\u0000~\u0000lsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~"
+"\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\u0012LocationPostalCodeq\u0000~\u0000lq\u0000~\u0000Ssq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0000q\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'p"
+"psq\u0000~\u00005q\u0000~\u0000Fpsq\u0000~\u00007q\u0000~\u0000lpq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000Aq\u0000~\u0000C\u0000\u0000\u0000\u0004q\u0000~\u0000Esq\u0000~"
+"\u0000\u001dt\u0000\u000estring-derivedq\u0000~\u0000lsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ss"
+"q\u0000~\u0000\"t\u0000\u001bLocationPostalExtensionCodeq\u0000~\u0000lq\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000q"
+"\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u00005q\u0000~"
+"\u0000Fpsq\u0000~\u00007q\u0000~\u0000=pq\u0000~\u0000?\u0000\u0000q\u0000~\u0000Aq\u0000~\u0000Aq\u0000~\u0000C\u0000\u0000\u0000\u00ffq\u0000~\u0000Esq\u0000~\u0000\u001dt\u0000\u000estrin"
+"g-derivedq\u0000~\u0000=sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\u0019Pr"
+"oductServiceDescriptionq\u0000~\u0000=q\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000q\u0000~\u0000Fp\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u0000\nppq\u0000~\u0000\u0010sq\u0000~\u0000\"q\u0000~\u0000%q\u0000~\u0000&sq\u0000~\u0000\'ppsq\u0000~\u00005q\u0000~\u0000Fpsr\u0000*com.s"
+"un.msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun."
+"msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValueq\u0000~\u0000\u000fxq\u0000~"
+"\u00008q\u0000~\u0000=pq\u0000~\u0000\u001c\u0000\u0000sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeW"
+"ithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u00009q\u0000~\u0000=pq\u0000~\u0000\u001c\u0000\u0000sr\u0000)c"
+"om.sun.msv.datatype.xsd.TotalDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tprecis"
+"ionxq\u0000~\u0000\u00b6q\u0000~\u0000=pq\u0000~\u0000\u001c\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0007decimalq\u0000~\u0000\u001cq\u0000~\u0000\u00bbt\u0000\u000btotalDigits\u0000\u0000\u0000"
+"\u0012q\u0000~\u0000\u00bbt\u0000\u000efractionDigits\u0000\u0000\u0000\u0002q\u0000~\u0000\u00bbt\u0000\fminInclusivesr\u0000\u0014java.math"
+".BigDecimalT\u00c7\u0015W\u00f9\u0081(O\u0003\u0000\u0002I\u0000\u0005scaleL\u0000\u0006intValt\u0000\u0016Ljava/math/BigInte"
+"ger;xr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0000\u0000\u0000\u0000sr\u0000\u0014java.math.BigIn"
+"teger\u008c\u00fc\u009f\u001f\u00a9;\u00fb\u001d\u0003\u0000\u0006I\u0000\bbitCountI\u0000\tbitLengthI\u0000\u0013firstNonzeroByteNu"
+"mI\u0000\flowestSetBitI\u0000\u0006signum[\u0000\tmagnitudet\u0000\u0002[Bxq\u0000~\u0000\u00c2\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00fe"
+"\u00ff\u00ff\u00ff\u00fe\u0000\u0000\u0000\u0000ur\u0000\u0002[B\u00ac\u00f3\u0017\u00f8\u0006\bT\u00e0\u0002\u0000\u0000xp\u0000\u0000\u0000\u0000xxq\u0000~\u0000Esq\u0000~\u0000\u001dt\u0000\u000fdecimal-deriv"
+"edq\u0000~\u0000=sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000\rPaymentAm"
+"ountq\u0000~\u0000=q\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpsq\u0000~\u00005ppsr\u0000\u001fcom.sun.msv.da"
+"tatype.xsd.IDType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.Ncn"
+"ameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000@q\u0000~\u0000\u0018t\u0000\u0002IDq\u0000~\u0000\u001c\u0000q\u0000~\u0000Esq\u0000~\u0000\u001dq\u0000~\u0000\u00d6q\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0002i"
+"dt\u0000#http://niem.gov/niem/structures/2.0q\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000"
+"~\u0000Fpsq\u0000~\u00005ppsr\u0000*com.sun.msv.datatype.xsd.DatatypeFactory$1\u00a1\u00f3"
+"\u000b\u00e3`rj\u000e\u0002\u0000\u0000xr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbas"
+"eTypeq\u0000~\u0000:xq\u0000~\u0000\u0014q\u0000~\u0000\u0018t\u0000\u0006IDREFSq\u0000~\u0000\u001csr\u0000\'com.sun.msv.datatype."
+"xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u00008ppq\u0000~\u0000\u001c\u0000\u0000sr\u0000!"
+"com.sun.msv.datatype.xsd.ListType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bitemTypeq\u0000~\u0000:"
+"xq\u0000~\u0000\u0013ppq\u0000~\u0000\u001csr\u0000\"com.sun.msv.datatype.xsd.IDREFType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u00d3q\u0000~\u0000\u0018t\u0000\u0005IDREFq\u0000~\u0000\u001c\u0000q\u0000~\u0000\u00e5t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000Epsq\u0000~\u0000\""
+"t\u0000\flinkMetadataq\u0000~\u0000\u00daq\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000\u00ddsq\u0000~\u0000\"t\u0000\bm"
+"etadataq\u0000~\u0000\u00daq\u0000~\u0000Ssq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0000Fpq\u0000~\u0000Kq\u0000~\u0000Pq\u0000~\u0000Ssq\u0000~\u0000\"t\u0000"
+"\u0006Vendorq\u0000~\u0000=sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHas"
+"h;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed"
+"\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar"
+"/ExpressionPool;xp\u0000\u0000\u0000.\u0001pq\u0000~\u0000+q\u0000~\u0000xq\u0000~\u0000Vq\u0000~\u0000-q\u0000~\u0000Iq\u0000~\u0000`q\u0000~\u0000oq"
+"\u0000~\u0000}q\u0000~\u0000\u008bq\u0000~\u0000/q\u0000~\u0000\u0099q\u0000~\u0000\u00a7q\u0000~\u0000\u009fq\u0000~\u0000[q\u0000~\u0000\u009dq\u0000~\u0000\u00cbq\u0000~\u0000\u00f0q\u0000~\u0000\u0086q\u0000~\u0000\u00dbq"
+"\u0000~\u00000q\u0000~\u0000\u00abq\u0000~\u0000(q\u0000~\u0000\u0094q\u0000~\u0000\u00a2q\u0000~\u0000)q\u0000~\u0000,q\u0000~\u0000\u0081q\u0000~\u00004q\u0000~\u0000\u008fq\u0000~\u0000\u00b0q\u0000~\u0000\tq"
+"\u0000~\u0000\u0091q\u0000~\u00002q\u0000~\u00001q\u0000~\u0000fq\u0000~\u0000iq\u0000~\u0000\u0083q\u0000~\u0000*q\u0000~\u0000\u00cfq\u0000~\u0000.q\u0000~\u0000uq\u0000~\u0000dq\u0000~\u0000\u00ecq"
+"\u0000~\u0000\u00adq\u0000~\u0000sq\u0000~\u0000Xx"));
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
            return edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 4;
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
                        if (("AwardIdNumber" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Vendor" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  5 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
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
                    case  1 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 4;
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
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        if (("Vendor" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.popAttributes();
                            state = 5;
                            return ;
                        }
                        break;
                    case  5 :
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
                    case  1 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 2;
                            return ;
                        }
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                        return ;
                    case  5 :
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
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 4;
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
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 4;
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
                        case  1 :
                            attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 4;
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
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.arra.impl.VendorTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.VendorImpl.this).new Unmarshaller(context)), 4, value);
                            return ;
                        case  5 :
                            revertToParentFromText(value);
                            return ;
                        case  2 :
                            state = 3;
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
