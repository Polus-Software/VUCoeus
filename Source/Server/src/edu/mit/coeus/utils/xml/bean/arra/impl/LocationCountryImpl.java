//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class LocationCountryImpl
    extends edu.mit.coeus.utils.xml.bean.arra.impl.CountryAlpha2CodeTypeImpl
    implements edu.mit.coeus.utils.xml.bean.arra.LocationCountry, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected boolean _Nil;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.LocationCountry.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "urn:us:gov:recoveryrr-ext";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "LocationCountry";
    }

    public boolean isNil() {
        return _Nil;
    }

    public void setNil(boolean value) {
        _Nil = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("urn:us:gov:recoveryrr-ext", "LocationCountry");
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
        return (edu.mit.coeus.utils.xml.bean.arra.LocationCountry.class);
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
+"Type;L\u0000\tfacetNameq\u0000~\u0000\u0015xq\u0000~\u0000\u0014t\u0000!http://niem.gov/niem/iso_3166"
+"/2.0t\u0000\u001bCountryAlpha2CodeSimpleTypeq\u0000~\u0000\u001c\u0000\u0000sr\u0000\"com.sun.msv.dat"
+"atype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.S"
+"tringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000\u0005tokenq\u0000~\u0000\u001c"
+"\u0001q\u0000~\u00009t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0002\u0000"
+"?@\u0000\u0000\u0000\u0000\u0000\u00f4t\u0000\u0002VUt\u0000\u0002VNt\u0000\u0002ECt\u0000\u0002VIt\u0000\u0002DZt\u0000\u0002VGt\u0000\u0002VEt\u0000\u0002DMt\u0000\u0002VCt\u0000\u0002DOt\u0000"
+"\u0002VAt\u0000\u0002DEt\u0000\u0002UZt\u0000\u0002UYt\u0000\u0002DKt\u0000\u0002DJt\u0000\u0002USt\u0000\u0002UMt\u0000\u0002UGt\u0000\u0002UAt\u0000\u0002ETt\u0000\u0002ESt\u0000"
+"\u0002ERt\u0000\u0002EHt\u0000\u0002EGt\u0000\u0002TZt\u0000\u0002EEt\u0000\u0002TTt\u0000\u0002TWt\u0000\u0002TVt\u0000\u0002GDt\u0000\u0002GEt\u0000\u0002GFt\u0000\u0002GAt\u0000"
+"\u0002GBt\u0000\u0002FRt\u0000\u0002FOt\u0000\u0002FKt\u0000\u0002FJt\u0000\u0002FMt\u0000\u0002FIt\u0000\u0002WSt\u0000\u0002GYt\u0000\u0002GWt\u0000\u0002GUt\u0000\u0002GTt\u0000"
+"\u0002GSt\u0000\u0002GRt\u0000\u0002GQt\u0000\u0002WFt\u0000\u0002GPt\u0000\u0002GNt\u0000\u0002GMt\u0000\u0002GLt\u0000\u0002GIt\u0000\u0002GHt\u0000\u0002GGt\u0000\u0002REt\u0000"
+"\u0002ROt\u0000\u0002ATt\u0000\u0002ASt\u0000\u0002ARt\u0000\u0002AQt\u0000\u0002AXt\u0000\u0002QAt\u0000\u0002AWt\u0000\u0002AUt\u0000\u0002AZt\u0000\u0002BAt\u0000\u0002PTt\u0000"
+"\u0002ADt\u0000\u0002PWt\u0000\u0002AGt\u0000\u0002PRt\u0000\u0002AEt\u0000\u0002PSt\u0000\u0002AFt\u0000\u0002ALt\u0000\u0002AIt\u0000\u0002AOt\u0000\u0002PYt\u0000\u0002AMt\u0000"
+"\u0002ANt\u0000\u0002TGt\u0000\u0002BWt\u0000\u0002TFt\u0000\u0002BVt\u0000\u0002BYt\u0000\u0002TDt\u0000\u0002TKt\u0000\u0002BSt\u0000\u0002TJt\u0000\u0002BRt\u0000\u0002THt\u0000"
+"\u0002BTt\u0000\u0002TOt\u0000\u0002TNt\u0000\u0002TMt\u0000\u0002TLt\u0000\u0002CAt\u0000\u0002TRt\u0000\u0002BZt\u0000\u0002BFt\u0000\u0002SVt\u0000\u0002BGt\u0000\u0002BHt\u0000"
+"\u0002STt\u0000\u0002BIt\u0000\u0002SYt\u0000\u0002BBt\u0000\u0002SZt\u0000\u0002BDt\u0000\u0002BEt\u0000\u0002BNt\u0000\u0002BOt\u0000\u0002BJt\u0000\u0002TCt\u0000\u0002BMt\u0000"
+"\u0002SDt\u0000\u0002CZt\u0000\u0002SCt\u0000\u0002CYt\u0000\u0002CXt\u0000\u0002SEt\u0000\u0002SHt\u0000\u0002CVt\u0000\u0002SGt\u0000\u0002CUt\u0000\u0002SJt\u0000\u0002SIt\u0000"
+"\u0002SLt\u0000\u0002SKt\u0000\u0002SNt\u0000\u0002SMt\u0000\u0002SOt\u0000\u0002SRt\u0000\u0002CIt\u0000\u0002RSt\u0000\u0002CGt\u0000\u0002RUt\u0000\u0002CHt\u0000\u0002RWt\u0000"
+"\u0002CFt\u0000\u0002CCt\u0000\u0002CDt\u0000\u0002CRt\u0000\u0002COt\u0000\u0002CMt\u0000\u0002CNt\u0000\u0002SAt\u0000\u0002CKt\u0000\u0002SBt\u0000\u0002CLt\u0000\u0002LVt\u0000"
+"\u0002LUt\u0000\u0002LTt\u0000\u0002LYt\u0000\u0002LSt\u0000\u0002LRt\u0000\u0002MGt\u0000\u0002MHt\u0000\u0002MEt\u0000\u0002MKt\u0000\u0002MLt\u0000\u0002MCt\u0000\u0002MDt\u0000"
+"\u0002MAt\u0000\u0002MVt\u0000\u0002MUt\u0000\u0002MXt\u0000\u0002MWt\u0000\u0002MZt\u0000\u0002MYt\u0000\u0002MNt\u0000\u0002MMt\u0000\u0002MPt\u0000\u0002MOt\u0000\u0002MRt\u0000"
+"\u0002MQt\u0000\u0002MTt\u0000\u0002MSt\u0000\u0002NFt\u0000\u0002NGt\u0000\u0002NIt\u0000\u0002NLt\u0000\u0002NAt\u0000\u0002NCt\u0000\u0002NEt\u0000\u0002NZt\u0000\u0002NUt\u0000"
+"\u0002NRt\u0000\u0002NPt\u0000\u0002NOt\u0000\u0002OMt\u0000\u0002PLt\u0000\u0002PMt\u0000\u0002PNt\u0000\u0002PHt\u0000\u0002PKt\u0000\u0002PEt\u0000\u0002PFt\u0000\u0002PGt\u0000"
+"\u0002PAt\u0000\u0002HKt\u0000\u0002ZAt\u0000\u0002HNt\u0000\u0002HMt\u0000\u0002HRt\u0000\u0002HTt\u0000\u0002HUt\u0000\u0002ZMt\u0000\u0002ZWt\u0000\u0002IDt\u0000\u0002IEt\u0000"
+"\u0002ILt\u0000\u0002IMt\u0000\u0002INt\u0000\u0002IOt\u0000\u0002IQt\u0000\u0002IRt\u0000\u0002YEt\u0000\u0002ISt\u0000\u0002ITt\u0000\u0002JEt\u0000\u0002YTt\u0000\u0002JPt\u0000"
+"\u0002JOt\u0000\u0002JMt\u0000\u0002KIt\u0000\u0002KHt\u0000\u0002KGt\u0000\u0002KEt\u0000\u0002KPt\u0000\u0002KRt\u0000\u0002KMt\u0000\u0002KNt\u0000\u0002KWt\u0000\u0002KYt\u0000"
+"\u0002KZt\u0000\u0002LAt\u0000\u0002LCt\u0000\u0002LBt\u0000\u0002LIt\u0000\u0002LKxsr\u00000com.sun.msv.grammar.Express"
+"ion$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000 \u0000psq\u0000~\u0000\u001dq\u0000~\u00006q\u0000~"
+"\u00005sq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00014psq\u0000~\u0000,ppsr\u0000\u001fcom.sun.msv.datatype.xsd.I"
+"DType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.NcnameType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00007q\u0000~\u0000\u0018t\u0000\u0002IDq\u0000~\u0000\u001c\u0000q\u0000~\u00013sq\u0000~\u0000\u001dq\u0000~\u0001<q\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0002id"
+"t\u0000#http://niem.gov/niem/structures/2.0sr\u00000com.sun.msv.gramma"
+"r.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000!q\u0000~\u0001Bsq\u0000"
+"~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00014psq\u0000~\u0000,ppsr\u0000*com.sun.msv.datatype.xsd.Dataty"
+"peFactory$1\u00a1\u00f3\u000b\u00e3`rj\u000e\u0002\u0000\u0000xr\u0000\u001ecom.sun.msv.datatype.xsd.Proxy\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypeq\u0000~\u00002xq\u0000~\u0000\u0014q\u0000~\u0000\u0018t\u0000\u0006IDREFSq\u0000~\u0000\u001csr\u0000\'com.sun."
+"msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u00000"
+"ppq\u0000~\u0000\u001c\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.ListType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"itemTypeq\u0000~\u00002xq\u0000~\u0000\u0013ppq\u0000~\u0000\u001csr\u0000\"com.sun.msv.datatype.xsd.IDREF"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0001:q\u0000~\u0000\u0018t\u0000\u0005IDREFq\u0000~\u0000\u001c\u0000q\u0000~\u0001Mt\u0000\tminLength\u0000\u0000\u0000"
+"\u0001q\u0000~\u00013psq\u0000~\u0000\"t\u0000\flinkMetadataq\u0000~\u0001@q\u0000~\u0001Bsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00014pq\u0000"
+"~\u0001Esq\u0000~\u0000\"t\u0000\bmetadataq\u0000~\u0001@q\u0000~\u0001Bsq\u0000~\u0000\u0007ppsq\u0000~\u0000\nq\u0000~\u00014psq\u0000~\u0000,ppsr"
+"\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0012q\u0000~\u0000\u0018t\u0000"
+"\u0005QNameq\u0000~\u0000\u001cq\u0000~\u00013sq\u0000~\u0000\u001dq\u0000~\u0001]q\u0000~\u0000\u0018sq\u0000~\u0000\"t\u0000\u0004typeq\u0000~\u0000&q\u0000~\u0001Bsq\u0000~\u0000"
+"\"t\u0000\u000fLocationCountryt\u0000\u0019urn:us:gov:recoveryrr-extsr\u0000\"com.sun.m"
+"sv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/m"
+"sv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramm"
+"ar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVers"
+"ionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\t\u0001pq"
+"\u0000~\u0001Xq\u0000~\u0000\tq\u0000~\u0001Tq\u0000~\u0001Cq\u0000~\u00016q\u0000~\u0000*q\u0000~\u0000+q\u0000~\u0000)q\u0000~\u0000(x"));
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
            return edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  5 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
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
                    case  0 :
                        if (("LocationCountry" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.pushAttributes(__atts, true);
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
                    case  5 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
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
                    case  4 :
                        if (("LocationCountry" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
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
                    case  5 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 2;
                            return ;
                        }
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.CountryAlpha2CodeTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.CountryAlpha2CodeTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.CountryAlpha2CodeTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.this).new Unmarshaller(context)), 4, ___uri, ___local, ___qname);
                            return ;
                        }
                        break;
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
                    case  5 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
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
                        case  2 :
                            state = 3;
                            eatText1(value);
                            return ;
                        case  5 :
                            revertToParentFromText(value);
                            return ;
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
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.arra.impl.CountryAlpha2CodeTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.LocationCountryImpl.this).new Unmarshaller(context)), 4, value);
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