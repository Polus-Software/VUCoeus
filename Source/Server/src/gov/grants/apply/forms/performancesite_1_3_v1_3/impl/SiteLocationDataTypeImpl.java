//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.19 at 02:55:10 EDT 
//


package gov.grants.apply.forms.performancesite_1_3_v1_3.impl;

public class SiteLocationDataTypeImpl implements gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.lang.String _CongressionalDistrictProgramProject;
    protected java.lang.String _DUNSNumber;
    protected java.lang.String _Individual;
    protected gov.grants.apply.system.globallibrary_v2.AddressDataType _Address;
    protected java.lang.String _OrganizationName;
    public final static java.lang.Class version = (gov.grants.apply.forms.performancesite_1_3_v1_3.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType.class);
    }

    public java.lang.String getCongressionalDistrictProgramProject() {
        return _CongressionalDistrictProgramProject;
    }

    public void setCongressionalDistrictProgramProject(java.lang.String value) {
        _CongressionalDistrictProgramProject = value;
    }

    public java.lang.String getDUNSNumber() {
        return _DUNSNumber;
    }

    public void setDUNSNumber(java.lang.String value) {
        _DUNSNumber = value;
    }

    public java.lang.String getIndividual() {
        return _Individual;
    }

    public void setIndividual(java.lang.String value) {
        _Individual = value;
    }

    public gov.grants.apply.system.globallibrary_v2.AddressDataType getAddress() {
        return _Address;
    }

    public void setAddress(gov.grants.apply.system.globallibrary_v2.AddressDataType value) {
        _Address = value;
    }

    public java.lang.String getOrganizationName() {
        return _OrganizationName;
    }

    public void setOrganizationName(java.lang.String value) {
        _OrganizationName = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.performancesite_1_3_v1_3.impl.SiteLocationDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_Individual!= null) {
            context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3", "Individual");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Individual), "Individual");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_OrganizationName!= null) {
            context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3", "OrganizationName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _OrganizationName), "OrganizationName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_DUNSNumber!= null) {
            context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3", "DUNSNumber");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DUNSNumber), "DUNSNumber");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3", "Address");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Address), "Address");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Address), "Address");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Address), "Address");
        context.endElement();
        if (_CongressionalDistrictProgramProject!= null) {
            context.startElement("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3", "CongressionalDistrictProgramProject");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CongressionalDistrictProgramProject), "CongressionalDistrictProgramProject");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv."
+"grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011"
+"java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun"
+".msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype"
+"/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPa"
+"ir;xq\u0000~\u0000\u0003ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.x"
+"sd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.ms"
+"v.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000"
+"\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/X"
+"SDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Co"
+"ncreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv."
+"datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001cL\u0000"
+"\btypeNameq\u0000~\u0000\u001cL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Whit"
+"eSpaceProcessor;xpt\u00001http://apply.grants.gov/system/GlobalLi"
+"brary-V2.0t\u0000\rYesNoDataTypesr\u00005com.sun.msv.datatype.xsd.White"
+"SpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.x"
+"sd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000#com.sun.msv.datatyp"
+"e.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.d"
+"atatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.data"
+"type.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001dt\u0000 http://www.w3.org/2"
+"001/XMLSchemat\u0000\u0006stringq\u0000~\u0000$\u0001q\u0000~\u0000(t\u0000\u000benumerationsr\u0000\u0011java.util"
+".HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0005N: Not\u0000\u0006Y: Yesxsr\u00000com"
+".sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000"
+"~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxpq\u0000~\u0000!q\u0000~\u0000 sq\u0000~\u0000\tppsr\u0000 com.sun.msv.g"
+"rammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq"
+"\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000\u0012ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000&q\u0000~\u0000)t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.White"
+"SpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000#q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000:q\u0000~\u0000"
+")sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalN"
+"ameq\u0000~\u0000\u001cL\u0000\fnamespaceURIq\u0000~\u0000\u001cxr\u0000\u001dcom.sun.msv.grammar.NameClas"
+"s\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-ins"
+"tancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000f\u0001q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\nIndividualt\u00006http://apply"
+".grants.gov/forms/PerformanceSite_1_3-V1-3q\u0000~\u0000Dsq\u0000~\u0000\tppsq\u0000~\u0000"
+"\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLeng"
+"thFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~\u0000\u0018q\u0000~\u0000 t\u0000\u0018OrganizationName"
+"DataTypeq\u0000~\u0000$\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0018q\u0000~\u0000 q\u0000~\u0000Oq\u0000~\u0000$\u0000\u0000q\u0000~\u0000(q\u0000~\u0000(t\u0000\tmin"
+"Length\u0000\u0000\u0000\u0001q\u0000~\u0000(t\u0000\tmaxLength\u0000\u0000\u0000<q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000Oq\u0000~\u0000 sq\u0000~\u0000\tpp"
+"sq\u0000~\u00005q\u0000~\u0000\u0010pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0010OrganizationNameq\u0000~\u0000Hq\u0000~"
+"\u0000Dsq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsq\u0000~\u0000Mq\u0000~\u0000 t\u0000\u000eDUNSIDD"
+"ataTypeq\u0000~\u0000$\u0000\u0001sq\u0000~\u0000Pq\u0000~\u0000 q\u0000~\u0000^q\u0000~\u0000$\u0000\u0000q\u0000~\u0000(q\u0000~\u0000(q\u0000~\u0000R\u0000\u0000\u0000\tq\u0000~\u0000"
+"(q\u0000~\u0000S\u0000\u0000\u0000\rq\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000^q\u0000~\u0000 sq\u0000~\u0000\tppsq\u0000~\u00005q\u0000~\u0000\u0010pq\u0000~\u00007q\u0000~\u0000"
+"@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\nDUNSNumberq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000bpp"
+"\u0000sq\u0000~\u0000\tppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001c"
+"com.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000"
+"\u0010psq\u0000~\u00005q\u0000~\u0000\u0010psr\u00002com.sun.msv.grammar.Expression$AnyStringEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Eq\u0000~\u0000nsr\u0000 com.sun.msv.grammar.A"
+"nyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000?q\u0000~\u0000Dsq\u0000~\u0000>t\u00008gov.grants.apply.s"
+"ystem.globallibrary_v2.AddressDataTypet\u0000+http://java.sun.com"
+"/jaxb/xjc/dummy-elementssq\u0000~\u0000\tppsq\u0000~\u00005q\u0000~\u0000\u0010pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Ds"
+"q\u0000~\u0000>t\u0000\u0007Addressq\u0000~\u0000Hsq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0012ppsq\u0000"
+"~\u0000Mq\u0000~\u0000Hpq\u0000~\u0000$\u0000\u0001sq\u0000~\u0000Pq\u0000~\u0000Hpq\u0000~\u0000$\u0000\u0001sq\u0000~\u0000Mq\u0000~\u0000 t\u0000\u001dCongression"
+"alDistrictDataTypeq\u0000~\u0000$\u0000\u0001sq\u0000~\u0000Pq\u0000~\u0000 q\u0000~\u0000\u007fq\u0000~\u0000$\u0000\u0000q\u0000~\u0000(q\u0000~\u0000(q\u0000"
+"~\u0000R\u0000\u0000\u0000\u0001q\u0000~\u0000(q\u0000~\u0000S\u0000\u0000\u0000\u0006q\u0000~\u0000(q\u0000~\u0000R\u0000\u0000\u0000\u0006q\u0000~\u0000(q\u0000~\u0000S\u0000\u0000\u0000\u0006q\u0000~\u00001sq\u0000~\u00002"
+"t\u0000\u000estring-derivedq\u0000~\u0000Hsq\u0000~\u0000\tppsq\u0000~\u00005q\u0000~\u0000\u0010pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000"
+"~\u0000>t\u0000#CongressionalDistrictProgramProjectq\u0000~\u0000Hq\u0000~\u0000Dsr\u0000\"com.s"
+"un.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/s"
+"un/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.g"
+"rammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstream"
+"VersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000"
+"\u0014\u0001pq\u0000~\u0000zq\u0000~\u0000xq\u0000~\u0000Iq\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000[q\u0000~\u0000fq\u0000~\u0000\bq\u0000~\u0000\u0011q\u0000~\u0000hq\u0000~\u0000\nq\u0000"
+"~\u00004q\u0000~\u0000Uq\u0000~\u0000aq\u0000~\u0000Yq\u0000~\u0000tq\u0000~\u0000\u0083q\u0000~\u0000\u0007q\u0000~\u0000kq\u0000~\u0000Kx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.performancesite_1_3_v1_3.impl.SiteLocationDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  10 :
                        if (("Street1" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            _Address = ((gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.system.globallibrary_v2.impl.AddressDataTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        if (("DUNSNumber" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Individual" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
                        if (("CongressionalDistrictProgramProject" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
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
                    case  11 :
                        if (("Address" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("Individual" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("DUNSNumber" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  5 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("CongressionalDistrictProgramProject" == ___local)&&("http://apply.grants.gov/forms/PerformanceSite_1_3-V1-3" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                        case  3 :
                            state = 6;
                            continue outer;
                        case  4 :
                            eatText1(value);
                            state = 5;
                            return ;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  13 :
                            eatText2(value);
                            state = 14;
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  7 :
                            eatText3(value);
                            state = 8;
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  1 :
                            eatText4(value);
                            state = 2;
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
                _OrganizationName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CongressionalDistrictProgramProject = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DUNSNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Individual = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
