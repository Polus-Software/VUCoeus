//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.ed_abstract_v1_1.impl;

public class AbstractAttachmentsTypeImpl implements gov.grants.apply.forms.ed_abstract_v1_1.AbstractAttachmentsType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.lang.String _FormVersion;
    protected gov.grants.apply.system.attachments_v1.AttachedFileDataType _Attachments;
    public final static java.lang.Class version = (gov.grants.apply.forms.ed_abstract_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.ed_abstract_v1_1.AbstractAttachmentsType.class);
    }

    public java.lang.String getFormVersion() {
        return _FormVersion;
    }

    public void setFormVersion(java.lang.String value) {
        _FormVersion = value;
    }

    public gov.grants.apply.system.attachments_v1.AttachedFileDataType getAttachments() {
        return _Attachments;
    }

    public void setAttachments(gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
        _Attachments = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.ed_abstract_v1_1.impl.AbstractAttachmentsTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/ED_Abstract-V1-1", "Attachments");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Attachments), "Attachments");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Attachments), "Attachments");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Attachments), "Attachments");
        context.endElement();
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startAttribute("http://apply.grants.gov/forms/ED_Abstract-V1-1", "FormVersion");
        try {
            context.text(((java.lang.String) _FormVersion), "FormVersion");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/ED_Abstract-V1-1", null, true);
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.ed_abstract_v1_1.AbstractAttachmentsType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsr\u0000\'com.sun.msv.grammar.trex.ElementPatt"
+"ern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;"
+"xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndecl"
+"aredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000"
+"sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com."
+"sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramm"
+"ar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean"
+"\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 com.sun.msv.grammar.AttributeExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0007xq\u0000~\u0000\u0003q\u0000~\u0000\u0012psr\u00002com.su"
+"n.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003sq\u0000~\u0000\u0011\u0001q\u0000~\u0000\u0016sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.ms"
+"v.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u0017"
+"q\u0000~\u0000\u001csr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlo"
+"calNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001exq\u0000~\u0000\u0019t\u0000;gov"
+".grants.apply.system.attachments_v1.AttachedFileDataTypet\u0000+h"
+"ttp://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000"
+"\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/rel"
+"axng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/"
+"util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.QnameTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fn"
+"amespaceUriq\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv"
+"/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/200"
+"1/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Whit"
+"eSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expressi"
+"on$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util."
+"StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURIq\u0000~\u0000\u001exp"
+"q\u0000~\u0000/q\u0000~\u0000.sq\u0000~\u0000\u001dt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-i"
+"nstanceq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u000bAttachmentst\u0000.http://apply.grants.gov/f"
+"orms/ED_Abstract-V1-1sq\u0000~\u0000\u0013ppsr\u0000\u001ccom.sun.msv.grammar.ValueEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000%L\u0000\u0004nameq\u0000~\u0000&L\u0000\u0005valuet\u0000\u0012Ljava/lang/Obje"
+"ct;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithVa"
+"lueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.Da"
+"taTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFl"
+"agL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\f"
+"concreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfac"
+"etNameq\u0000~\u0000\u001exq\u0000~\u0000+t\u00001http://apply.grants.gov/system/GlobalLib"
+"rary-V2.0t\u0000\u0013FormVersionDataTypesr\u00005com.sun.msv.datatype.xsd."
+"WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00001\u0000\u0001sr\u0000\'com.sun.m"
+"sv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000Bq"
+"\u0000~\u0000Gq\u0000~\u0000Hq\u0000~\u0000J\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000)q\u0000~\u0000.t\u0000\u0006stringq\u0000~\u0000J\u0001q\u0000~\u0000Nt\u0000\tminL"
+"ength\u0000\u0000\u0000\u0001q\u0000~\u0000Nt\u0000\tmaxLength\u0000\u0000\u0000\u001esq\u0000~\u00005q\u0000~\u0000Hq\u0000~\u0000Gt\u0000\u00031.1sq\u0000~\u0000\u001dt\u0000"
+"\u000bFormVersionq\u0000~\u0000<sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$Clos"
+"edHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j"
+"\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/gr"
+"ammar/ExpressionPool;xp\u0000\u0000\u0000\u0005\u0001pq\u0000~\u0000\u0005q\u0000~\u0000\"q\u0000~\u0000\nq\u0000~\u0000\rq\u0000~\u0000\u0010x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "-------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.ed_abstract_v1_1.impl.AbstractAttachmentsTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  4 :
                        if (("FileName" == ___local)&&("http://apply.grants.gov/system/Attachments-V1.0" == ___uri)) {
                            _Attachments = ((gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl) spawnChildFromEnterElement((gov.grants.apply.system.attachments_v1.impl.AttachedFileDataTypeImpl.class), 5, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("Attachments" == ___local)&&("http://apply.grants.gov/forms/ED_Abstract-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 4;
                            return ;
                        }
                        break;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_Abstract-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
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
                _FormVersion = value;
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
                        if (("Attachments" == ___local)&&("http://apply.grants.gov/forms/ED_Abstract-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_Abstract-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
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
                    case  6 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/ED_Abstract-V1-1" == ___uri)) {
                            state = 1;
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
                    case  2 :
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/ED_Abstract-V1-1" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_Abstract-V1-1", "FormVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            eatText1(v);
                            state = 3;
                            continue outer;
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
                        case  6 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/ED_Abstract-V1-1", "FormVersion");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                eatText1(v);
                                state = 3;
                                continue outer;
                            }
                            break;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
