//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_budget_v1_1.impl;

public class KeyPersonDataTypeImpl implements gov.grants.apply.forms.rr_budget_v1_1.KeyPersonDataType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected gov.grants.apply.forms.rr_budget_v1_1.KeyPersonCompensationDataType _Compensation;
    protected java.lang.String _ProjectRole;
    protected gov.grants.apply.system.globallibrary_v2.HumanNameDataType _Name;
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_budget_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_budget_v1_1.KeyPersonDataType.class);
    }

    public gov.grants.apply.forms.rr_budget_v1_1.KeyPersonCompensationDataType getCompensation() {
        return _Compensation;
    }

    public void setCompensation(gov.grants.apply.forms.rr_budget_v1_1.KeyPersonCompensationDataType value) {
        _Compensation = value;
    }

    public java.lang.String getProjectRole() {
        return _ProjectRole;
    }

    public void setProjectRole(java.lang.String value) {
        _ProjectRole = value;
    }

    public gov.grants.apply.system.globallibrary_v2.HumanNameDataType getName() {
        return _Name;
    }

    public void setName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value) {
        _Name = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonDataTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1-1", "Name");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1-1", "ProjectRole");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProjectRole), "ProjectRole");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://apply.grants.gov/forms/RR_Budget-V1-1", "Compensation");
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
        return (gov.grants.apply.forms.rr_budget_v1_1.KeyPersonDataType.class);
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
+"q\u0000~\u0000\u0007pp\u0000sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.m"
+"sv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang"
+".Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.Attri"
+"buteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\bxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psr"
+"\u00002com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u0000\u0017sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000co"
+"m.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0003q\u0000~\u0000\u0018q\u0000~\u0000\u001dsr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001fxq\u0000~"
+"\u0000\u001at\u0000:gov.grants.apply.system.globallibrary_v2.HumanNameDataT"
+"ypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\rppsq\u0000"
+"~\u0000\u0014q\u0000~\u0000\u0013psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fL"
+"org/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/s"
+"un/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd."
+"QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtom"
+"icType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001fL\u0000\btypeNameq\u0000~\u0000\u001fL\u0000\nwhiteSpacet\u0000.Lcom/"
+"sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3."
+"org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.White"
+"SpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.x"
+"sd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.E"
+"xpression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.ms"
+"v.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001fL\u0000\fnamespaceURI"
+"q\u0000~\u0000\u001fxpq\u0000~\u00000q\u0000~\u0000/sq\u0000~\u0000\u001et\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLS"
+"chema-instanceq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\u0004Namet\u0000,http://apply.grants.gov/f"
+"orms/RR_Budget-V1-1sq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000%ppsr\u0000\'com.sun.msv."
+"datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.su"
+"n.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002"
+"\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z"
+"\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/ms"
+"v/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv"
+"/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001fxq\u0000~\u0000,t\u0000*http://"
+"apply.grants.gov/system/Global-V1.0t\u0000\u0014StringMin1Max100Typesr"
+"\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00002\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000Bq\u0000~\u0000Gq\u0000~\u0000Hq\u0000~\u0000J\u0000\u0000sr\u0000#com.sun.msv.d"
+"atatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000*q\u0000~\u0000/"
+"t\u0000\u0006stringq\u0000~\u0000J\u0001q\u0000~\u0000Nt\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000Nt\u0000\tmaxLength\u0000\u0000\u0000dq\u0000~"
+"\u00005sq\u0000~\u00006q\u0000~\u0000Hq\u0000~\u0000Gsq\u0000~\u0000\rppsq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dsq\u0000~\u0000\u001et"
+"\u0000\u000bProjectRoleq\u0000~\u0000=sq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0007pp\u0000sq\u0000~\u0000\rppsq\u0000~\u0000\u000fq\u0000"
+"~\u0000\u0013psq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000\u0017q\u0000~\u0000\u001bq\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000Cgov.grants.apply.for"
+"ms.rr_budget_v1_1.KeyPersonCompensationDataTypeq\u0000~\u0000\"sq\u0000~\u0000\rpp"
+"sq\u0000~\u0000\u0014q\u0000~\u0000\u0013pq\u0000~\u0000(q\u0000~\u00008q\u0000~\u0000\u001dsq\u0000~\u0000\u001et\u0000\fCompensationq\u0000~\u0000=sr\u0000\"com"
+".sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom"
+"/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv"
+".grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstre"
+"amVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000"
+"\u0000\u0000\f\u0001pq\u0000~\u0000#q\u0000~\u0000Sq\u0000~\u0000_q\u0000~\u0000?q\u0000~\u0000\u0006q\u0000~\u0000\u000bq\u0000~\u0000Xq\u0000~\u0000\u000eq\u0000~\u0000Zq\u0000~\u0000\u0011q\u0000~\u0000["
+"q\u0000~\u0000\u0005x"));
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
            return gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonDataTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("ProjectRole" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Name" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("Compensation" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("PrefixName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            _Name = ((gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("FirstName" == ___local)&&("http://apply.grants.gov/system/GlobalLibrary-V2.0" == ___uri)) {
                            _Name = ((gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.system.globallibrary_v2.impl.HumanNameDataTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  7 :
                        if (("CalendarMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("AcademicMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("SummerMonths" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("RequestedSalary" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            _Compensation = ((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.rr_budget_v1_1.impl.KeyPersonCompensationDataTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
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
                        if (("Compensation" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ProjectRole" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("Name" == ___local)&&("http://apply.grants.gov/forms/RR_Budget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 3;
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
                            eatText1(value);
                            state = 5;
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

    }

}
