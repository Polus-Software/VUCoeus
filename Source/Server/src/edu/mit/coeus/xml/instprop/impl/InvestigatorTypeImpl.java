//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop.impl;

public class InvestigatorTypeImpl implements edu.mit.coeus.xml.instprop.InvestigatorType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallableObject, edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializable, edu.mit.coeus.xml.instprop.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _Unit;
    protected edu.mit.coeus.xml.instprop.PersonType _PIName;
    protected boolean has_FacultyFlag;
    protected boolean _FacultyFlag;
    protected boolean has_PrincipalInvFlag;
    protected boolean _PrincipalInvFlag;
    public final static java.lang.Class version = (edu.mit.coeus.xml.instprop.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.xml.instprop.InvestigatorType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getUnit() {
        if (_Unit == null) {
            _Unit = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Unit;
    }

    public java.util.List getUnit() {
        return _getUnit();
    }

    public edu.mit.coeus.xml.instprop.PersonType getPIName() {
        return _PIName;
    }

    public void setPIName(edu.mit.coeus.xml.instprop.PersonType value) {
        _PIName = value;
    }

    public boolean isFacultyFlag() {
        return _FacultyFlag;
    }

    public void setFacultyFlag(boolean value) {
        _FacultyFlag = value;
        has_FacultyFlag = true;
    }

    public boolean isPrincipalInvFlag() {
        return _PrincipalInvFlag;
    }

    public void setPrincipalInvFlag(boolean value) {
        _PrincipalInvFlag = value;
        has_PrincipalInvFlag = true;
    }

    public edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.xml.instprop.impl.InvestigatorTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Unit == null)? 0 :_Unit.size());
        if (_PIName!= null) {
            context.startElement("", "PIName");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _PIName), "PIName");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _PIName), "PIName");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _PIName), "PIName");
            context.endElement();
        }
        if (has_PrincipalInvFlag) {
            context.startElement("", "principalInvFlag");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _PrincipalInvFlag)), "PrincipalInvFlag");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_FacultyFlag) {
            context.startElement("", "facultyFlag");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _FacultyFlag)), "FacultyFlag");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx1 != len1) {
            context.startElement("", "unit");
            int idx_6 = idx1;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Unit.get(idx_6 ++)), "Unit");
            context.endNamespaceDecls();
            int idx_7 = idx1;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Unit.get(idx_7 ++)), "Unit");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Unit.get(idx1 ++)), "Unit");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Unit == null)? 0 :_Unit.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Unit == null)? 0 :_Unit.size());
        while (idx1 != len1) {
            idx1 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.xml.instprop.InvestigatorType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar."
+"ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsr"
+"\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv"
+".grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u000fpsr\u0000 com.s"
+"un.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClas"
+"sq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\u000fpsr\u00002com.sun.msv.grammar.Expression$AnyStri"
+"ngExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000e\u0001q\u0000~\u0000\u0019sr\u0000 com.sun.msv.gra"
+"mmar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.NameClas"
+"s\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$EpsilonExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001aq\u0000~\u0000\u001fsr\u0000#com.sun.msv.grammar.Sim"
+"pleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;L\u0000\fn"
+"amespaceURIq\u0000~\u0000!xq\u0000~\u0000\u001ct\u0000%edu.mit.coeus.xml.instprop.PersonTy"
+"pet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\bppsq\u0000~"
+"\u0000\u0016q\u0000~\u0000\u000fpsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLo"
+"rg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/su"
+"n/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"com.sun.msv.datatype.xsd.Q"
+"nameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomi"
+"cType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000!L\u0000\btypeNameq\u0000~\u0000!L\u0000\nwhiteSpacet\u0000.Lcom/s"
+"un/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 http://www.w3.o"
+"rg/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteS"
+"paceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Ex"
+"pression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv"
+".util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000!L\u0000\fnamespaceURIq"
+"\u0000~\u0000!xpq\u0000~\u00002q\u0000~\u00001sq\u0000~\u0000 t\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSc"
+"hema-instanceq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0006PINamet\u0000\u0000q\u0000~\u0000\u001fsq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000f"
+"p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\'ppsr\u0000$com.sun.msv.datatype.xsd.BooleanType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000,q\u0000~\u00001t\u0000\u0007booleanq\u0000~\u00005q\u0000~\u00007sq\u0000~\u00008q\u0000~\u0000Fq\u0000~\u00001sq\u0000~"
+"\u0000\bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0010principalInvFlagq\u0000~\u0000"
+"?q\u0000~\u0000\u001fsq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Csq\u0000~\u0000\bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fp"
+"q\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u000bfacultyFlagq\u0000~\u0000?q\u0000~\u0000\u001fsq\u0000~\u0000\bppsq\u0000~\u0000\u0013q"
+"\u0000~\u0000\u000fpsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\bppsq\u0000~\u0000\u0013q\u0000~\u0000\u000fpsq\u0000~\u0000"
+"\u0016q\u0000~\u0000\u000fpq\u0000~\u0000\u0019q\u0000~\u0000\u001dq\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000#edu.mit.coeus.xml.instprop.Un"
+"itTypeq\u0000~\u0000$sq\u0000~\u0000\bppsq\u0000~\u0000\u0016q\u0000~\u0000\u000fpq\u0000~\u0000*q\u0000~\u0000:q\u0000~\u0000\u001fsq\u0000~\u0000 t\u0000\u0004unitq"
+"\u0000~\u0000?q\u0000~\u0000\u001fsr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000"
+"\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;x"
+"psr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000"
+"\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Ex"
+"pressionPool;xp\u0000\u0000\u0000\u0014\u0001pq\u0000~\u0000\u0010q\u0000~\u0000Vq\u0000~\u0000%q\u0000~\u0000Hq\u0000~\u0000Oq\u0000~\u0000]q\u0000~\u0000@q\u0000~\u0000"
+"Lq\u0000~\u0000Tq\u0000~\u0000\tq\u0000~\u0000\u0015q\u0000~\u0000Yq\u0000~\u0000Bq\u0000~\u0000Nq\u0000~\u0000\u0006q\u0000~\u0000\u0012q\u0000~\u0000Xq\u0000~\u0000\u0007q\u0000~\u0000Sq\u0000~\u0000"
+"\u0005x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.xml.instprop.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.xml.instprop.impl.InvestigatorTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  10 :
                        if (("unitNumber" == ___local)&&("" == ___uri)) {
                            _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("unitName" == ___local)&&("" == ___uri)) {
                            _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("leadUnitFlag" == ___local)&&("" == ___uri)) {
                            _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                        return ;
                    case  12 :
                        if (("unit" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        if (("principalInvFlag" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  0 :
                        if (("PIName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("unit" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        if (("facultyFlag" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  1 :
                        if (("lastName" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("firstName" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("middleName" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("fullName" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("phone" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("address" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("City" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("state" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("zip" == ___local)&&("" == ___uri)) {
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname, __atts));
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
                    case  10 :
                        _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  2 :
                        if (("PIName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("facultyFlag" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("principalInvFlag" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  11 :
                        if (("unit" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  1 :
                        _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname));
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
                    case  10 :
                        _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  12 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  1 :
                        _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname));
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
                    case  10 :
                        _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  12 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  1 :
                        _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, ___uri, ___local, ___qname));
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
                        case  10 :
                            _getUnit().add(((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl) spawnChildFromText((edu.mit.coeus.xml.instprop.impl.UnitTypeImpl.class), 11, value)));
                            return ;
                        case  4 :
                            state = 5;
                            eatText1(value);
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText2(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  1 :
                            _PIName = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromText((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 2, value));
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
                _PrincipalInvFlag = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_PrincipalInvFlag = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FacultyFlag = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_FacultyFlag = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}