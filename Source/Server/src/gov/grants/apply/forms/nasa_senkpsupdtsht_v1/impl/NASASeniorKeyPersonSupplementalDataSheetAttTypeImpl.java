//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl;

public class NASASeniorKeyPersonSupplementalDataSheetAttTypeImpl implements gov.grants.apply.forms.nasa_senkpsupdtsht_v1.NASASeniorKeyPersonSupplementalDataSheetAttType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _SeniorKeyPerson;
    public final static java.lang.Class version = (gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.nasa_senkpsupdtsht_v1.NASASeniorKeyPersonSupplementalDataSheetAttType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getSeniorKeyPerson() {
        if (_SeniorKeyPerson == null) {
            _SeniorKeyPerson = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _SeniorKeyPerson;
    }

    public java.util.List getSeniorKeyPerson() {
        return _getSeniorKeyPerson();
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl.NASASeniorKeyPersonSupplementalDataSheetAttTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_SeniorKeyPerson == null)? 0 :_SeniorKeyPerson.size());
        while (idx1 != len1) {
            context.startElement("http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0", "Senior_Key_Person");
            int idx_0 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _SeniorKeyPerson.get(idx_0 ++)), "SeniorKeyPerson");
            context.endNamespaceDecls();
            int idx_1 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _SeniorKeyPerson.get(idx_1 ++)), "SeniorKeyPerson");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _SeniorKeyPerson.get(idx1 ++)), "SeniorKeyPerson");
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_SeniorKeyPerson == null)? 0 :_SeniorKeyPerson.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_SeniorKeyPerson == null)? 0 :_SeniorKeyPerson.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.nasa_senkpsupdtsht_v1.NASASeniorKeyPersonSupplementalDataSheetAttType.class);
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
+"calNamet\u0000\u0012Ljava/lang/String;L\u0000\fnamespaceURIq\u0000~\u0000\u001exq\u0000~\u0000\u0019t\u0000@gov"
+".grants.apply.forms.nasa_senkpsupdtsht_v1.SeniorKeyPersonTyp"
+"et\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\fppsq\u0000~\u0000"
+"\u0013q\u0000~\u0000\u0012psr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLor"
+"g/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun"
+"/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.Qn"
+"ameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomic"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.Lcom/su"
+"n/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.or"
+"g/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSp"
+"aceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd"
+".WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Exp"
+"ression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv."
+"util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURIq\u0000"
+"~\u0000\u001expq\u0000~\u0000/q\u0000~\u0000.sq\u0000~\u0000\u001dt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSch"
+"ema-instanceq\u0000~\u0000\u001csq\u0000~\u0000\u001dt\u0000\u0011Senior_Key_Persont\u00005http://apply.g"
+"rants.gov/forms/NASA_SenKPSupDtSht-V1.0sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012ps"
+"q\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012"
+"pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~"
+"\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006p"
+"p\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000 "
+"q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q"
+"\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000"
+"\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000"
+"~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000pps"
+"q\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000"
+"\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000~\u0000\fpps"
+"q\u0000~\u0000\u0000q\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012"
+"psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000"
+"~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000~\u0000\fppsq\u0000~\u0000\u0000q\u0000~\u0000\u0012psq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000"
+"~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000"
+"\u001csq\u0000~\u0000\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:sq\u0000"
+"~\u0000\fppsq\u0000~\u0000\u0006q\u0000~\u0000\u0012p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0006pp\u0000sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0012psq\u0000~\u0000"
+"\u0013q\u0000~\u0000\u0012pq\u0000~\u0000\u0016q\u0000~\u0000\u001aq\u0000~\u0000\u001csq\u0000~\u0000\u001dq\u0000~\u0000 q\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000\u0013q\u0000~\u0000\u0012pq\u0000"
+"~\u0000\'q\u0000~\u00007q\u0000~\u0000\u001cq\u0000~\u0000:q\u0000~\u0000\u001cq\u0000~\u0000\u001cq\u0000~\u0000\u001cq\u0000~\u0000\u001cq\u0000~\u0000\u001cq\u0000~\u0000\u001cq\u0000~\u0000\u001csr\u0000\"com"
+".sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom"
+"/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv"
+".grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstre"
+"amVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000"
+"\u0000\u0000.\u0001pq\u0000~\u0000tq\u0000~\u0000Tq\u0000~\u0000^q\u0000~\u0000Hq\u0000~\u0000_q\u0000~\u0000\"q\u0000~\u0000Fq\u0000~\u0000Qq\u0000~\u0000\\q\u0000~\u0000gq\u0000~\u0000r"
+"q\u0000~\u0000}q\u0000~\u0000\u0087q\u0000~\u0000jq\u0000~\u0000Sq\u0000~\u0000\u0005q\u0000~\u0000\u007fq\u0000~\u0000>q\u0000~\u0000iq\u0000~\u0000\nq\u0000~\u0000@q\u0000~\u0000Kq\u0000~\u0000V"
+"q\u0000~\u0000\rq\u0000~\u0000Bq\u0000~\u0000Mq\u0000~\u0000Xq\u0000~\u0000cq\u0000~\u0000aq\u0000~\u0000nq\u0000~\u0000lq\u0000~\u0000yq\u0000~\u0000wq\u0000~\u0000\u0083q\u0000~\u0000\u0081"
+"q\u0000~\u0000uq\u0000~\u0000\u0010q\u0000~\u0000Cq\u0000~\u0000Nq\u0000~\u0000Yq\u0000~\u0000dq\u0000~\u0000oq\u0000~\u0000zq\u0000~\u0000\u0084q\u0000~\u0000Iq\u0000~\u0000=x"));
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
            return gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl.NASASeniorKeyPersonSupplementalDataSheetAttTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("Senior_Key_Person" == ___local)&&("http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        if (("SeniorKeyPersonName" == ___local)&&("http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0" == ___uri)) {
                            _getSeniorKeyPerson().add(((gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl.SeniorKeyPersonTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.nasa_senkpsupdtsht_v1.impl.SeniorKeyPersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("Senior_Key_Person" == ___local)&&("http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0" == ___uri)) {
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
                        if (("Senior_Key_Person" == ___local)&&("http://apply.grants.gov/forms/NASA_SenKPSupDtSht-V1.0" == ___uri)) {
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
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

    }

}
