//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_budget_v1.impl;

public class OtherPersonnelDataTypeImpl implements gov.grants.apply.forms.rr_budget_v1.OtherPersonnelDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.rr_budget_v1.SectBCompensationDataType _Compensation;
    protected java.lang.String _ProjectRole;
    protected java.math.BigInteger _NumberOfPersonnel;
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_budget_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_budget_v1.OtherPersonnelDataType.class);
    }

    public gov.grants.apply.forms.rr_budget_v1.SectBCompensationDataType getCompensation() {
        return _Compensation;
    }

    public void setCompensation(gov.grants.apply.forms.rr_budget_v1.SectBCompensationDataType value) {
        _Compensation = value;
    }

    public java.lang.String getProjectRole() {
        return _ProjectRole;
    }

    public void setProjectRole(java.lang.String value) {
        _ProjectRole = value;
    }

    public java.math.BigInteger getNumberOfPersonnel() {
        return _NumberOfPersonnel;
    }

    public void setNumberOfPersonnel(java.math.BigInteger value) {
        _NumberOfPersonnel = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_budget_v1.impl.OtherPersonnelDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1.0", "NumberOfPersonnel");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInteger(((java.math.BigInteger) _NumberOfPersonnel)), "NumberOfPersonnel");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1.0", "ProjectRole");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProjectRole), "ProjectRole");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1.0", "Compensation");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Compensation), "Compensation");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Compensation), "Compensation");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Compensation), "Compensation");
        context.endElement();
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
        return (gov.grants.apply.forms.rr_budget_v1.OtherPersonnelDataType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000pps"
+"r\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxn"
+"g/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/uti"
+"l/StringPair;xq\u0000~\u0000\u0003ppsr\u0000*com.sun.msv.datatype.xsd.MaxInclusi"
+"veFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.da"
+"tatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*co"
+"m.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFace"
+"tFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/dataty"
+"pe/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatyp"
+"e/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com."
+"sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUr"
+"iq\u0000~\u0000\u0017L\u0000\btypeNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/"
+"xsd/WhiteSpaceProcessor;xpt\u0000,http://apply.grants.gov/forms/R"
+"R_Budget-V1.0psr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcess"
+"or$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpac"
+"eProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInc"
+"lusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0011q\u0000~\u0000\u001bpq\u0000~\u0000\u001e\u0000\u0000sr\u0000/com.sun.msv.dat"
+"atype.xsd.NonNegativeIntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000$com.sun.msv.d"
+"atatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.x"
+"sd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000\u0015xr\u0000*com.su"
+"n.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.m"
+"sv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018t\u0000 http://www.w"
+"3.org/2001/XMLSchemat\u0000\u0012nonNegativeIntegerq\u0000~\u0000\u001esq\u0000~\u0000\u001fppq\u0000~\u0000\u001e\u0000"
+"\u0000sq\u0000~\u0000\"q\u0000~\u0000\'t\u0000\u0007integerq\u0000~\u0000\u001esr\u0000,com.sun.msv.datatype.xsd.Frac"
+"tionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.x"
+"sd.DataTypeWithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u0014ppq\u0000~\u0000"
+"\u001e\u0001\u0000sr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q"
+"\u0000~\u0000\'t\u0000\u0007decimalq\u0000~\u0000\u001eq\u0000~\u00000t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000*t\u0000\fminIncl"
+"usivesr\u0000)com.sun.msv.datatype.xsd.IntegerValueType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\u0005valueq\u0000~\u0000\u0017xr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xpt\u0000\u00010q\u0000~\u0000&q\u0000~\u0000"
+"3sq\u0000~\u00004q\u0000~\u00007q\u0000~\u0000&t\u0000\fmaxInclusivesq\u0000~\u00004t\u0000\u0003999sr\u00000com.sun.msv."
+"grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bc"
+"om.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnam"
+"espaceURIq\u0000~\u0000\u0017xpt\u0000\u001anonNegativeInteger-derivedq\u0000~\u0000\u001bsr\u0000\u001dcom.su"
+"n.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.gr"
+"ammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\bxq\u0000"
+"~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\fppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u0000\'t\u0000\u0005QN"
+"ameq\u0000~\u0000\u001eq\u0000~\u0000=sq\u0000~\u0000>q\u0000~\u0000Jq\u0000~\u0000\'sr\u0000#com.sun.msv.grammar.SimpleN"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xr\u0000\u001d"
+"com.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://w"
+"ww.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Exp"
+"ression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000E\u0001q\u0000~\u0000Rsq\u0000~\u0000L"
+"t\u0000\u0011NumberOfPersonnelq\u0000~\u0000\u001bsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\fppsr\u0000\'com.su"
+"n.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxq\u0000~"
+"\u0000\u0013t\u0000*http://apply.grants.gov/system/Global-V1.0t\u0000\u0014StringMin1"
+"Max100Typesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$P"
+"reserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001d\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLe"
+"ngthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0013q\u0000~\u0000[q\u0000~\u0000\\q\u0000~\u0000^\u0000\u0000sr\u0000#c"
+"om.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysVali"
+"dxq\u0000~\u0000$q\u0000~\u0000\'t\u0000\u0006stringq\u0000~\u0000^\u0001q\u0000~\u0000bt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000bt\u0000\tmaxL"
+"ength\u0000\u0000\u0000dq\u0000~\u0000=sq\u0000~\u0000>q\u0000~\u0000\\q\u0000~\u0000[sq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000N"
+"q\u0000~\u0000Rsq\u0000~\u0000Lt\u0000\u000bProjectRoleq\u0000~\u0000\u001bsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~"
+"\u0000Appsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.s"
+"un.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000Fpsq\u0000"
+"~\u0000Cq\u0000~\u0000Fpsr\u00002com.sun.msv.grammar.Expression$AnyStringExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Sq\u0000~\u0000tsr\u0000 com.sun.msv.grammar.AnyNam"
+"eClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Mq\u0000~\u0000Rsq\u0000~\u0000Lt\u0000=gov.grants.apply.forms."
+"rr_budget_v1.SectBCompensationDataTypet\u0000+http://java.sun.com"
+"/jaxb/xjc/dummy-elementssq\u0000~\u0000Appsq\u0000~\u0000Cq\u0000~\u0000Fpq\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Rs"
+"q\u0000~\u0000Lt\u0000\fCompensationq\u0000~\u0000\u001bsr\u0000\"com.sun.msv.grammar.ExpressionP"
+"ool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionP"
+"ool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Clos"
+"edHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/su"
+"n/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\n\u0001pq\u0000~\u0000\u0005q\u0000~\u0000Wq\u0000~\u0000lq\u0000~\u0000\u000bq\u0000~"
+"\u0000nq\u0000~\u0000Bq\u0000~\u0000gq\u0000~\u0000zq\u0000~\u0000qq\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.rr_budget_v1.impl.OtherPersonnelDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("NumberOfPersonnel" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  7 :
                        if (("CalendarMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("AcademicMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SummerMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("RequestedSalary" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1.impl.SectBCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  3 :
                        if (("ProjectRole" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("Compensation" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        break;
                    case  9 :
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
                    case  8 :
                        if (("Compensation" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("NumberOfPersonnel" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ProjectRole" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  9 :
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
                    case  9 :
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
                    case  9 :
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
                        case  4 :
                            state = 5;
                            eatText1(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText2(value);
                            return ;
                        case  9 :
                            revertToParentFromText(value);
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
                _ProjectRole = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NumberOfPersonnel = javax.xml.bind.DatatypeConverter.parseInteger(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}