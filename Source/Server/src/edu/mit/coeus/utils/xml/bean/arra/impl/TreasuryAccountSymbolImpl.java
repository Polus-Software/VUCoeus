//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class TreasuryAccountSymbolImpl
    extends edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolTypeImpl
    implements edu.mit.coeus.utils.xml.bean.arra.TreasuryAccountSymbol, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected boolean _Nil;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.TreasuryAccountSymbol.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "urn:us:gov:recoveryrr-ext";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "TreasuryAccountSymbol";
    }

    public boolean isNil() {
        return _Nil;
    }

    public void setNil(boolean value) {
        _Nil = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("urn:us:gov:recoveryrr-ext", "TreasuryAccountSymbol");
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
        return (edu.mit.coeus.utils.xml.bean.arra.TreasuryAccountSymbol.class);
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
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsq\u0000~\u0000\'ppsr\u0000\u001bcom.sun.msv.gramm"
+"ar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\rL\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004nameq\u0000~\u0000\u000exq\u0000"
+"~\u0000\u0004ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.xsd.Dat"
+"aTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.data"
+"type.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needV"
+"alueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatat"
+"ypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concrete"
+"Type;L\u0000\tfacetNameq\u0000~\u0000\u0015xq\u0000~\u0000\u0014t\u0000\u001burn:us:gov:recoveryrr-facett\u0000"
+"\u001fTreasuryAccountSymbolSimpleTypeq\u0000~\u0000\u001c\u0000\u0000sr\u0000\"com.sun.msv.datat"
+"ype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.Str"
+"ingType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005tokenq\u0000~\u0000\u001c\u0001q"
+"\u0000~\u00009t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0002\u0000?@"
+"\u0000\u0000\u0000\u0000\u0001,t\u0000\u000712-1408t\u0000\u000736-0171t\u0000\u000712-1402t\u0000\u000775-0900t\u0000\u000775-0901t\u0000\u00077"
+"5-0902t\u0000\u000775-0903t\u0000\u000775-0904t\u0000\u000775-0905t\u0000\u000791-0302t\u0000\u000775-0906t\u0000\u00077"
+"5-0907t\u0000\u000775-0392t\u0000\u000715-0411t\u0000\u000714-5541t\u0000\u000712-3540t\u0000\u000786-0345t\u0000\u00071"
+"5-0412t\u0000\u000786-0346t\u0000\u000712-3542t\u0000\u000786-0347t\u0000\u000786-0348t\u0000\u000775-1501t\u0000\u00076"
+"8-0252t\u0000\u000736-0184t\u0000\u000713-0110t\u0000\u000786-0330t\u0000\u000775-0510t\u0000\u000775-0909t\u0000\u00077"
+"5-0908t\u0000\u000775-0518t\u0000\u000775-0389t\u0000\u000775-0911t\u0000\u000757-3605t\u0000\u000713-0451t\u0000\u00079"
+"7-4091t\u0000\u000715-0402t\u0000\u000760-8262t\u0000\u000713-0118t\u0000\u000775-0910t\u0000\u000736-0150t\u0000\u00077"
+"0-0201t\u0000\u000716-0105t\u0000\u000712-4146t\u0000\u000714-1613t\u0000\u000736-0158t\u0000\u000797-0150t\u0000\u00071"
+"2-1102t\u0000\u000714-1610t\u0000\u000716-0107t\u0000\u000775-1523t\u0000\u000768-0249t\u0000\u000757-3744t\u0000\u00074"
+"7-0403t\u0000\u000712-1902t\u0000\u000712-1118t\u0000\u000736-0168t\u0000\u000712-0111t\u0000\u000775-1516t\u0000\u00071"
+"4-4401t\u0000\u000791-0207t\u0000\u000786-0203t\u0000\u000791-0103t\u0000\u000715-0421t\u0000\u000716-0400t\u0000\u00072"
+"0-0906t\u0000\u000736-0130t\u0000\u000769-1749t\u0000\u000712-3504t\u0000\u000712-0403t\u0000\u000712-4225t\u0000\u00071"
+"2-4226t\u0000\u000712-4227t\u0000\u000712-3509t\u0000\u000705-0108t\u0000\u000780-0125t\u0000\u000775-0942t\u0000\u00078"
+"0-0123t\u0000\u000727-0200t\u0000\u000714-1112t\u0000\u000780-0121t\u0000\u000775-0351t\u0000\u000786-0193t\u0000\u00077"
+"5-0143t\u0000\u000775-0141t\u0000\u000714-1108t\u0000\u000791-1909t\u0000\u000712-4212t\u0000\u000714-4523t\u0000\u00072"
+"0-0129t\u0000\u000714-4524t\u0000\u000712-4215t\u0000\u000786-0190t\u0000\u000773-0101t\u0000\u000719-1079t\u0000\u00071"
+"2-4216t\u0000\u000749-0552t\u0000\u000780-0119t\u0000\u000789-0253t\u0000\u000780-0116t\u0000\u000714-2629t\u0000\u00073"
+"3-0101t\u0000\u000768-0113t\u0000\u000713-2051t\u0000\u000712-0803t\u0000\u000775-0144t\u0000\u000717-1206t\u0000\u00079"
+"7-0401t\u0000\u000791-0901t\u0000\u000772-0302t\u0000\u000775-0131t\u0000\u000769-1101t\u0000\u000720-0135t\u0000\u00071"
+"2-1951t\u0000\u000797-0501t\u0000\u000769-1306t\u0000\u000719-1069t\u0000\u000769-1304t\u0000\u000791-1401t\u0000\u00072"
+"0-0139t\u0000\u000789-0227t\u0000\u000717-1116t\u0000\u000769-1102t\u0000\u000717-1117t\u0000\u000717-1807t\u0000\u00079"
+"7-0112t\u0000\u000717-1805t\u0000\u000712-1140t\u0000\u000712-1142t\u0000\u000775-0120t\u0000\u000749-0301t\u0000\u00077"
+"5-0121t\u0000\u000715-0326t\u0000\u000786-4585t\u0000\u000789-0237t\u0000\u000714-0786t\u0000\u000791-1001t\u0000\u00072"
+"0-0140t\u0000\u000714-1126t\u0000\u000775-0129t\u0000\u000717-1320t\u0000\u000786-0177t\u0000\u000770-0556t\u0000\u00078"
+"9-4486t\u0000\u000775-0839t\u0000\u000716-1800t\u0000\u000714-1041t\u0000\u000712-5591t\u0000\u000775-0874t\u0000\u00072"
+"0-1882t\u0000\u000712-0599t\u0000\u000714-0681t\u0000\u000714-2101t\u0000\u000775-0871t\u0000\u000714-2302t\u0000\u00071"
+"4-0803t\u0000\u000773-4280t\u0000\u000789-0209t\u0000\u000720-0942t\u0000\u000789-4405t\u0000\u000789-4404t\u0000\u00071"
+"6-0167t\u0000\u000712-2081t\u0000\u000769-0131t\u0000\u000770-0567t\u0000\u000715-0699t\u0000\u000712-1073t\u0000\u00077"
+"5-1701t\u0000\u000768-0102t\u0000\u000721-2022t\u0000\u000775-0863t\u0000\u000713-1440t\u0000\u000721-2066t\u0000\u00077"
+"0-0563t\u0000\u000789-0211t\u0000\u000716-8042t\u0000\u000728-8704t\u0000\u000768-0108t\u0000\u000720-0930t\u0000\u00072"
+"8-0403t\u0000\u000769-0504t\u0000\u000720-0934t\u0000\u000720-0933t\u0000\u000736-0101t\u0000\u000786-0161t\u0000\u00072"
+"0-0938t\u0000\u000795-2730t\u0000\u000773-4268t\u0000\u000770-0536t\u0000\u000775-0818t\u0000\u000770-0535t\u0000\u00079"
+"5-2731t\u0000\u000770-0534t\u0000\u000713-0554t\u0000\u000775-0850t\u0000\u000713-1454t\u0000\u000775-0852t\u0000\u00072"
+"8-0418t\u0000\u000728-0417t\u0000\u000721-2051t\u0000\u000773-0201t\u0000\u000719-4519t\u0000\u000713-0556t\u0000\u00071"
+"2-3317t\u0000\u000716-0182t\u0000\u000716-0184t\u0000\u000773-1156t\u0000\u000720-0923t\u0000\u000716-0186t\u0000\u00072"
+"0-0922t\u0000\u000775-0847t\u0000\u000768-8196t\u0000\u000775-0808t\u0000\u000768-8195t\u0000\u000775-0845t\u0000\u00077"
+"3-4279t\u0000\u000791-0196t\u0000\u000791-0197t\u0000\u000770-0546t\u0000\u000791-0198t\u0000\u000769-0106t\u0000\u00079"
+"1-0199t\u0000\u000719-0530t\u0000\u000775-0840t\u0000\u000791-0299t\u0000\u000795-2729t\u0000\u000719-1119t\u0000\u00071"
+"2-1980t\u0000\u000721-2041t\u0000\u000714-1035t\u0000\u000775-0842t\u0000\u000769-1133t\u0000\u000716-0179t\u0000\u00071"
+"3-0549t\u0000\u000759-0102t\u0000\u000716-0176t\u0000\u000716-0326t\u0000\u000716-0172t\u0000\u000786-0305t\u0000\u00075"
+"7-3844t\u0000\u000775-1546t\u0000\u000796-3134t\u0000\u000786-0303t\u0000\u000796-3133t\u0000\u000769-0724t\u0000\u00078"
+"6-0306t\u0000\u000712-1232t\u0000\u000747-4534t\u0000\u000796-3135t\u0000\u000757-0748t\u0000\u000716-1700t\u0000\u00079"
+"6-3136t\u0000\u000796-3137t\u0000\u000795-8266t\u0000\u000770-0118t\u0000\u000757-0743t\u0000\u000747-0505t\u0000\u00077"
+"5-1537t\u0000\u000757-3834t\u0000\u000714-0107t\u0000\u000727-0400t\u0000\u000714-0101t\u0000\u000770-0618t\u0000\u00077"
+"0-0617t\u0000\u000786-0327t\u0000\u000757-3404t\u0000\u000775-0899t\u0000\u000721-2094t\u0000\u000789-5657t\u0000\u00078"
+"9-0323t\u0000\u000789-5655t\u0000\u000749-0101t\u0000\u000789-0328t\u0000\u000786-0328t\u0000\u000769-1771t\u0000\u00071"
+"3-0514t\u0000\u000757-3307t\u0000\u000749-0107t\u0000\u000796-3113t\u0000\u000789-4576t\u0000\u000775-1558t\u0000\u00076"
+"0-0116t\u0000\u000775-0883t\u0000\u000760-0115t\u0000\u000769-0718t\u0000\u000796-8873t\u0000\u000721-2081t\u0000\u00078"
+"9-0335t\u0000\u000789-0336t\u0000\u000760-0114t\u0000\u000721-0721t\u0000\u000789-0331t\u0000\u000712-4284t\u0000\u00071"
+"3-0500t\u0000\u000789-4180t\u0000\u000747-4543t\u0000\u000770-0708t\u0000\u000719-0112t\u0000\u000747-0112t\u0000\u00072"
+"1-0726xsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000 \u0000psq\u0000~\u0000\u001dq\u0000~\u00006q\u0000~\u00005sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0001lp"
+"sq\u0000~\u0000,ppsr\u0000\u001fcom.sun.msv.datatype.xsd.IDType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#co"
+"m.sun.msv.datatype.xsd.NcnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00007q\u0000~\u0000\u0018t\u0000\u0002ID"
+"q\u0000~\u0000\u001c\u0000q\u0000~\u0001ksq\u0000~\u0000\u001dq\u0000~\u0001tq\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0002idt\u0000#http://niem.gov/nie"
+"m/structures/2.0sr\u00000com.sun.msv.grammar.Expression$EpsilonEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u0001zsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0001lpsq\u0000~\u0000"
+",ppsr\u0000*com.sun.msv.datatype.xsd.DatatypeFactory$1\u00a1\u00f3\u000b\u00e3`rj\u000e\u0002\u0000\u0000"
+"xr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypeq\u0000~\u0000"
+"2xq\u0000~\u0000\u0014q\u0000~\u0000\u0018t\u0000\u0006IDREFSq\u0000~\u0000\u001csr\u0000\'com.sun.msv.datatype.xsd.MinLe"
+"ngthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u00000ppq\u0000~\u0000\u001c\u0000\u0000sr\u0000!com.sun.m"
+"sv.datatype.xsd.ListType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bitemTypeq\u0000~\u00002xq\u0000~\u0000\u0013ppq"
+"\u0000~\u0000\u001csr\u0000\"com.sun.msv.datatype.xsd.IDREFType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0001rq"
+"\u0000~\u0000\u0018t\u0000\u0005IDREFq\u0000~\u0000\u001c\u0000q\u0000~\u0001\u0085t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0001kpsq\u0000~\u0000\"t\u0000\flinkMe"
+"tadataq\u0000~\u0001xq\u0000~\u0001zsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0001lpq\u0000~\u0001}sq\u0000~\u0000\"t\u0000\bmetadataq\u0000"
+"~\u0001xq\u0000~\u0001zsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u0001lpsq\u0000~\u0000,ppsr\u0000\"com.sun.msv.datatype"
+".xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005QNameq\u0000~\u0000\u001cq\u0000~\u0001ksq\u0000~\u0000\u001d"
+"q\u0000~\u0001\u0095q\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0004typeq\u0000~\u0000&q\u0000~\u0001zsq\u0000~\u0000\"t\u0000\u0015TreasuryAccountSym"
+"bolt\u0000\u0019urn:us:gov:recoveryrr-extsr\u0000\"com.sun.msv.grammar.Expre"
+"ssionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expre"
+"ssionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPoo"
+"l$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$L"
+"com/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq\u0000~\u0000+q\u0000~\u0001\u0090q\u0000~\u0000(q\u0000"
+"~\u0001nq\u0000~\u0000\tq\u0000~\u0001{q\u0000~\u0000)q\u0000~\u0001\u008cq\u0000~\u0000*x"));
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
            return edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("TreasuryAccountSymbol" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
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
                        break;
                    case  5 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        if (("TreasuryAccountSymbol" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.popAttributes();
                            state = 5;
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
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 2;
                            return ;
                        }
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  3 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
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
                        break;
                    case  5 :
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
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.TreasuryAccountSymbolImpl.this).new Unmarshaller(context)), 4, value);
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