//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1.impl;

public class OrganizationImpl
    extends gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl
    implements gov.grants.apply.forms.sf424_v1.Organization, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.sf424_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424_v1.Organization.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/SF424-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Organization";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/SF424-V1.0", "Organization");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
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
        return (gov.grants.apply.forms.sf424_v1.Organization.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq"
+"\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg"
+"/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/"
+"msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.Max"
+"LengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.x"
+"sd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.ms"
+"v.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000"
+"\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/X"
+"SDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Co"
+"ncreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv."
+"datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001dL\u0000"
+"\btypeNameq\u0000~\u0000\u001dL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Whit"
+"eSpaceProcessor;xpt\u0000*http://apply.grants.gov/system/Global-V"
+"1.0t\u0000\u0014StringMin1Max120Typesr\u00005com.sun.msv.datatype.xsd.White"
+"SpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.x"
+"sd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv.datatyp"
+"e.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0019q\u0000~\u0000!q\u0000~\u0000\"q"
+"\u0000~\u0000%\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\ri"
+"sAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u001et\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000~\u0000%\u0001q\u0000~"
+"\u0000+t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000+t\u0000\tmaxLength\u0000\u0000\u0000xsr\u00000com.sun.msv.gramm"
+"ar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom.su"
+"n.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespac"
+"eURIq\u0000~\u0000\u001dxpq\u0000~\u0000\"q\u0000~\u0000!sq\u0000~\u0000\u000eppsr\u0000 com.sun.msv.grammar.Attribu"
+"teExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u0000\u0012psq\u0000~"
+"\u0000\u0014ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q"
+"\u0000~\u0000,t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000:q\u0000~\u0000,sr\u0000#com.sun.m"
+"sv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnam"
+"espaceURIq\u0000~\u0000\u001dxr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp"
+"t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com.s"
+"un.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004"
+"sq\u0000~\u0000\u0011\u0001q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0010OrganizationNamet\u0000(http://apply.grants."
+"gov/forms/SF424-V1.0q\u0000~\u0000Dsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014"
+"ppsq\u0000~\u0000\u0018q\u0000~\u0000!t\u0000\u0013StringMin1Max13Typeq\u0000~\u0000%\u0000\u0001sq\u0000~\u0000&q\u0000~\u0000!q\u0000~\u0000Nq\u0000"
+"~\u0000%\u0000\u0000q\u0000~\u0000+q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0001q\u0000~\u0000+q\u0000~\u0000/\u0000\u0000\u0000\rq\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000Nq\u0000~\u0000!s"
+"q\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u0006DUNSIDq\u0000~\u0000Hq\u0000~\u0000Ds"
+"q\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0014ppsq\u0000~\u0000\u0018q\u0000~\u0000!t\u0000\u0013StringMin1"
+"Max30Typeq\u0000~\u0000%\u0000\u0001sq\u0000~\u0000&q\u0000~\u0000!q\u0000~\u0000Zq\u0000~\u0000%\u0000\u0000q\u0000~\u0000+q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0001q\u0000"
+"~\u0000+q\u0000~\u0000/\u0000\u0000\u0000\u001eq\u0000~\u00001sq\u0000~\u00002q\u0000~\u0000Zq\u0000~\u0000!sq\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000"
+"~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\u000eDepartmentNameq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012"
+"p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Xsq\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\fD"
+"ivisionNameq\u0000~\u0000Hq\u0000~\u0000Dsq\u0000~\u0000\u000eppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Xsq\u0000~\u0000"
+"\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\nEmployerIDq\u0000~\u0000Hq\u0000~\u0000Ds"
+"q\u0000~\u0000\u000eppsq\u0000~\u00005q\u0000~\u0000\u0012pq\u0000~\u00007q\u0000~\u0000@q\u0000~\u0000Dsq\u0000~\u0000>t\u0000\fOrganizationq\u0000~\u0000H"
+"sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTable"
+"t\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com."
+"sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005count"
+"B\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionP"
+"ool;xp\u0000\u0000\u0000\u0015\u0001pq\u0000~\u0000Uq\u0000~\u0000aq\u0000~\u0000hq\u0000~\u0000\u000bq\u0000~\u0000\fq\u0000~\u0000Wq\u0000~\u0000Iq\u0000~\u0000cq\u0000~\u0000jq\u0000~"
+"\u00004q\u0000~\u0000\u000fq\u0000~\u0000Qq\u0000~\u0000]q\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000\nq\u0000~\u0000oq\u0000~\u0000\rq\u0000~\u0000Kq\u0000~\u0000\u0013q\u0000~\u0000\tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("Organization" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("OrganizationName" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DUNSID" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DepartmentName" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("DivisionName" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("EmployerID" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        spawnHandlerFromLeaveElement((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("Organization" == ___local)&&("http://apply.grants.gov/forms/SF424-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
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
                    case  3 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        spawnHandlerFromLeaveAttribute((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        case  1 :
                            spawnHandlerFromText((((gov.grants.apply.forms.sf424_v1.impl.OrganizationTypeImpl)gov.grants.apply.forms.sf424_v1.impl.OrganizationImpl.this).new Unmarshaller(context)), 2, value);
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