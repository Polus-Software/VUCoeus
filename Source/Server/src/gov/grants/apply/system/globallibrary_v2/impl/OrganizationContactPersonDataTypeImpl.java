//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v2.impl;

public class OrganizationContactPersonDataTypeImpl
    extends gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl
    implements gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.lang.String _DepartmentName;
    protected java.lang.String _DivisionName;
    protected java.lang.String _OrganizationName;
    public final static java.lang.Class version = (gov.grants.apply.system.globallibrary_v2.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType.class);
    }

    public java.lang.String getDepartmentName() {
        return _DepartmentName;
    }

    public void setDepartmentName(java.lang.String value) {
        _DepartmentName = value;
    }

    public java.lang.String getDivisionName() {
        return _DivisionName;
    }

    public void setDivisionName(java.lang.String value) {
        _DivisionName = value;
    }

    public java.lang.String getOrganizationName() {
        return _OrganizationName;
    }

    public void setOrganizationName(java.lang.String value) {
        _OrganizationName = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
        context.startElement("http://apply.grants.gov/system/GlobalLibrary-V2.0", "OrganizationName");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _OrganizationName), "OrganizationName");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_DepartmentName!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V2.0", "DepartmentName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DepartmentName), "DepartmentName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_DivisionName!= null) {
            context.startElement("http://apply.grants.gov/system/GlobalLibrary-V2.0", "DivisionName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DivisionName), "DivisionName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeAttributes(context);
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.system.globallibrary_v2.OrganizationContactPersonDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001e"
+"com.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclared"
+"AttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\rpp\u0000sr\u0000\u001d"
+"com.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun."
+"msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.U"
+"naryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080"
+"\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000exq\u0000~\u0000\u0003q\u0000~\u0000\u0019psr\u00002com.sun.ms"
+"v.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000"
+"~\u0000\u0018\u0001q\u0000~\u0000\u001dsr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001d"
+"com.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.gr"
+"ammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001eq\u0000~\u0000"
+"#sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalN"
+"amet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000%xq\u0000~\u0000 t\u0000:gov.gra"
+"nts.apply.system.globallibrary_v2.HumanNameDataTypet\u0000+http:/"
+"/java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019psr\u0000"
+"\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/"
+"datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/"
+"StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+"\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamesp"
+"aceUriq\u0000~\u0000%L\u0000\btypeNameq\u0000~\u0000%L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/data"
+"type/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XML"
+"Schemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcess"
+"or$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpac"
+"eProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$Nu"
+"llSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.Strin"
+"gPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000%L\u0000\fnamespaceURIq\u0000~\u0000%xpq\u0000~\u00006"
+"q\u0000~\u00005sq\u0000~\u0000$t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instan"
+"ceq\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0004Namet\u00001http://apply.grants.gov/system/Global"
+"Library-V2.0sq\u0000~\u0000\u0013ppsq\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+ppsr\u0000\'com.sun"
+".msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009c"
+"om.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro"
+"\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/s"
+"un/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/su"
+"n/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000%xq\u0000~\u00002q\u0000~\u0000C"
+"t\u0000\u0012HumanTitleDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpace"
+"Processor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00008\u0000\u0001sr\u0000\'com.sun.msv.datatyp"
+"e.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000Iq\u0000~\u0000Cq\u0000~\u0000Nq"
+"\u0000~\u0000P\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\ri"
+"sAlwaysValidxq\u0000~\u00000q\u0000~\u00005t\u0000\u0006stringq\u0000~\u0000P\u0001q\u0000~\u0000Tt\u0000\tminLength\u0000\u0000\u0000\u0001q"
+"\u0000~\u0000Tt\u0000\tmaxLength\u0000\u0000\u0000-q\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000Nq\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019"
+"pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0005Titleq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000"
+"~\u0000\rpp\u0000sq\u0000~\u0000\u0013ppsq\u0000~\u0000\u0015q\u0000~\u0000\u0019psq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000\u001dq\u0000~\u0000!q\u0000~\u0000#sq\u0000~\u0000$t"
+"\u00008gov.grants.apply.system.globallibrary_v2.AddressDataTypeq\u0000"
+"~\u0000(sq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0007Addressq\u0000~\u0000Cs"
+"q\u0000~\u0000\u0013ppsq\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+ppsq\u0000~\u0000Hq\u0000~\u0000Ct\u0000\u0017TelephoneN"
+"umberDataTypeq\u0000~\u0000P\u0000\u0001sq\u0000~\u0000Qq\u0000~\u0000Cq\u0000~\u0000nq\u0000~\u0000P\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000Tq\u0000~\u0000V\u0000\u0000"
+"\u0000\u0001q\u0000~\u0000Tq\u0000~\u0000W\u0000\u0000\u0000\u0019q\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000nq\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~"
+"\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0005Phoneq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u0013ppsq\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000"
+"~\u0000\u0000ppq\u0000~\u0000lsq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0003Faxq\u0000~"
+"\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u0013ppsq\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+ppsq\u0000~\u0000Hq\u0000~\u0000Ct\u0000\rEm"
+"ailDataTypeq\u0000~\u0000P\u0000\u0001sq\u0000~\u0000Qq\u0000~\u0000Cq\u0000~\u0000\u0081q\u0000~\u0000P\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000Tq\u0000~\u0000V\u0000\u0000\u0000\u0001"
+"q\u0000~\u0000Tq\u0000~\u0000W\u0000\u0000\u0000<q\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000\u0081q\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000."
+"q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0005Emailq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\rpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+pps"
+"q\u0000~\u0000Hq\u0000~\u0000Ct\u0000\u0018OrganizationNameDataTypeq\u0000~\u0000P\u0000\u0001sq\u0000~\u0000Qq\u0000~\u0000Cq\u0000~\u0000\u008c"
+"q\u0000~\u0000P\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000Tq\u0000~\u0000V\u0000\u0000\u0000\u0001q\u0000~\u0000Tq\u0000~\u0000W\u0000\u0000\u0000<q\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000\u008cq\u0000~\u0000"
+"Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u0010OrganizationNam"
+"eq\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+ppsq\u0000~\u0000Hq\u0000~\u0000Ct\u0000\u0016Dep"
+"artmentNameDataTypeq\u0000~\u0000P\u0000\u0001sq\u0000~\u0000Qq\u0000~\u0000Cq\u0000~\u0000\u0098q\u0000~\u0000P\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000Tq"
+"\u0000~\u0000V\u0000\u0000\u0000\u0001q\u0000~\u0000Tq\u0000~\u0000W\u0000\u0000\u0000\u001eq\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000\u0098q\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~"
+"\u0000\u0019pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000#sq\u0000~\u0000$t\u0000\u000eDepartmentNameq\u0000~\u0000Cq\u0000~\u0000#sq\u0000~\u0000\u0013pps"
+"q\u0000~\u0000\rq\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000+ppsq\u0000~\u0000Hq\u0000~\u0000Ct\u0000\u0014DivisionNameDataTy"
+"peq\u0000~\u0000P\u0000\u0001sq\u0000~\u0000Qq\u0000~\u0000Cq\u0000~\u0000\u00a4q\u0000~\u0000P\u0000\u0000q\u0000~\u0000Tq\u0000~\u0000Tq\u0000~\u0000V\u0000\u0000\u0000\u0001q\u0000~\u0000Tq\u0000~\u0000"
+"W\u0000\u0000\u0000\u001eq\u0000~\u0000;sq\u0000~\u0000<q\u0000~\u0000\u00a4q\u0000~\u0000Csq\u0000~\u0000\u0013ppsq\u0000~\u0000\u001aq\u0000~\u0000\u0019pq\u0000~\u0000.q\u0000~\u0000>q\u0000~\u0000"
+"#sq\u0000~\u0000$t\u0000\fDivisionNameq\u0000~\u0000Cq\u0000~\u0000#sr\u0000\"com.sun.msv.grammar.Expr"
+"essionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expr"
+"essionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPo"
+"ol$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$"
+"Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000$\u0001pq\u0000~\u0000\u00a1q\u0000~\u0000\u009fq\u0000~\u0000\u0011q"
+"\u0000~\u0000^q\u0000~\u0000Fq\u0000~\u0000\u000bq\u0000~\u0000Dq\u0000~\u0000|q\u0000~\u0000\tq\u0000~\u0000iq\u0000~\u0000uq\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000\u0014q\u0000~\u0000`q"
+"\u0000~\u0000)q\u0000~\u0000Yq\u0000~\u0000eq\u0000~\u0000qq\u0000~\u0000xq\u0000~\u0000\u0084q\u0000~\u0000\u008fq\u0000~\u0000\u009bq\u0000~\u0000\u00a7q\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000\nq"
+"\u0000~\u0000\u0089q\u0000~\u0000kq\u0000~\u0000wq\u0000~\u0000~q\u0000~\u0000\u0017q\u0000~\u0000aq\u0000~\u0000\fq\u0000~\u0000\u0093q\u0000~\u0000\u0095x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "-----------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  7 :
                        if (("DivisionName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 8;
                            return ;
                        }
                        state = 10;
                        continue outer;
                    case  0 :
                        if (("Name" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.system.globallibrary_v2.impl.ContactPersonDataTypeImpl)gov.grants.apply.system.globallibrary_v2.impl.OrganizationContactPersonDataTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  10 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  4 :
                        if (("DepartmentName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 5;
                            return ;
                        }
                        state = 7;
                        continue outer;
                    case  1 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 2;
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
                    case  7 :
                        state = 10;
                        continue outer;
                    case  10 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 7;
                        continue outer;
                    case  3 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("DivisionName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.popAttributes();
                            state = 10;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("DepartmentName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            context.popAttributes();
                            state = 7;
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
                    case  7 :
                        state = 10;
                        continue outer;
                    case  10 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 7;
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
                    case  7 :
                        state = 10;
                        continue outer;
                    case  10 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  4 :
                        state = 7;
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
                        case  7 :
                            state = 10;
                            continue outer;
                        case  8 :
                            state = 9;
                            eatText1(value);
                            return ;
                        case  2 :
                            state = 3;
                            eatText2(value);
                            return ;
                        case  10 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            state = 7;
                            continue outer;
                        case  5 :
                            state = 6;
                            eatText3(value);
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
                _DivisionName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _OrganizationName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DepartmentName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
