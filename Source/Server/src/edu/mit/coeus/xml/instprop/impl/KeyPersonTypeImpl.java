//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop.impl;

public class KeyPersonTypeImpl implements edu.mit.coeus.xml.instprop.KeyPersonType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallableObject, edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializable, edu.mit.coeus.xml.instprop.impl.runtime.ValidatableObject
{

    protected java.lang.String _ProposalNumber;
    protected boolean has_Faculty;
    protected boolean _Faculty;
    protected java.lang.String _PersonName;
    protected boolean has_NonEmployee;
    protected boolean _NonEmployee;
    protected java.math.BigDecimal _PercentEffort;
    protected java.lang.String _RoleName;
    protected java.lang.String _PersonAddress;
    protected java.lang.String _PersonId;
    public final static java.lang.Class version = (edu.mit.coeus.xml.instprop.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.xml.instprop.KeyPersonType.class);
    }

    public java.lang.String getProposalNumber() {
        return _ProposalNumber;
    }

    public void setProposalNumber(java.lang.String value) {
        _ProposalNumber = value;
    }

    public boolean isFaculty() {
        return _Faculty;
    }

    public void setFaculty(boolean value) {
        _Faculty = value;
        has_Faculty = true;
    }

    public java.lang.String getPersonName() {
        return _PersonName;
    }

    public void setPersonName(java.lang.String value) {
        _PersonName = value;
    }

    public boolean isNonEmployee() {
        return _NonEmployee;
    }

    public void setNonEmployee(boolean value) {
        _NonEmployee = value;
        has_NonEmployee = true;
    }

    public java.math.BigDecimal getPercentEffort() {
        return _PercentEffort;
    }

    public void setPercentEffort(java.math.BigDecimal value) {
        _PercentEffort = value;
    }

    public java.lang.String getRoleName() {
        return _RoleName;
    }

    public void setRoleName(java.lang.String value) {
        _RoleName = value;
    }

    public java.lang.String getPersonAddress() {
        return _PersonAddress;
    }

    public void setPersonAddress(java.lang.String value) {
        _PersonAddress = value;
    }

    public java.lang.String getPersonId() {
        return _PersonId;
    }

    public void setPersonId(java.lang.String value) {
        _PersonId = value;
    }

    public edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.xml.instprop.impl.KeyPersonTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_ProposalNumber!= null) {
            context.startElement("", "proposalNumber");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ProposalNumber), "ProposalNumber");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PersonId!= null) {
            context.startElement("", "PersonId");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PersonId), "PersonId");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PersonName!= null) {
            context.startElement("", "PersonName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PersonName), "PersonName");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_RoleName!= null) {
            context.startElement("", "RoleName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _RoleName), "RoleName");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PersonAddress!= null) {
            context.startElement("", "PersonAddress");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PersonAddress), "PersonAddress");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_NonEmployee) {
            context.startElement("", "NonEmployee");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _NonEmployee)), "NonEmployee");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_Faculty) {
            context.startElement("", "Faculty");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _Faculty)), "Faculty");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PercentEffort!= null) {
            context.startElement("", "PercentEffort");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _PercentEffort)), "PercentEffort");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.xml.instprop.KeyPersonType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005value"
+"xp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dt"
+"t\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLc"
+"om/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype."
+"xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dat"
+"atype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dataty"
+"pe.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.X"
+"SDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;"
+"L\u0000\btypeNameq\u0000~\u0000\u001dL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Wh"
+"iteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006st"
+"ringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserv"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ej"
+"B\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespaceURIq\u0000~\u0000\u001dxpq\u0000~\u0000!q\u0000~\u0000 sq\u0000~"
+"\u0000\fppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~"
+"\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000fxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psq\u0000~\u0000\u0015ppsr\u0000\"com.sun.msv.datat"
+"ype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000 t\u0000\u0005QNamesr\u00005com.sun.m"
+"sv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"#q\u0000~\u0000&sq\u0000~\u0000\'q\u0000~\u0000/q\u0000~\u0000 sr\u0000#com.sun.msv.grammar.SimpleNameClas"
+"s\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespaceURIq\u0000~\u0000\u001dxr\u0000\u001dcom.sun"
+".msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.o"
+"rg/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression"
+"$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u00009sq\u0000~\u00003t\u0000\u000eprop"
+"osalNumbert\u0000\u0000q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fpp"
+"sq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\bPersonIdq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\f"
+"ppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000"
+"~\u00009sq\u0000~\u00003t\u0000\nPersonNameq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000p"
+"pq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\bRoleNameq\u0000"
+"~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000"
+"\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\rPersonAddressq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000"
+"~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0015ppsr\u0000$com.sun.msv.datatype.xsd.Boole"
+"anType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000 t\u0000\u0007booleanq\u0000~\u00001q\u0000~\u0000&sq\u0000~\u0000\'q\u0000~\u0000`q"
+"\u0000~\u0000 sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\u000bNonEmployeeq"
+"\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000]sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~"
+"\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\u0007Facultyq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000"
+"~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0015ppsr\u0000#com.sun.msv.datatype.xsd.NumberType"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000 t\u0000\u0007decimalq\u0000~\u00001q\u0000~\u0000&sq\u0000~\u0000\'q\u0000~\u0000sq\u0000~\u0000 sq"
+"\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\rPercentEffortq\u0000~\u0000="
+"q\u0000~\u00009sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexp"
+"Tablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000"
+"-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005"
+"countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Expres"
+"sionPool;xp\u0000\u0000\u0000\u001f\u0001pq\u0000~\u0000oq\u0000~\u0000\rq\u0000~\u0000>q\u0000~\u0000Eq\u0000~\u0000Lq\u0000~\u0000\u0014q\u0000~\u0000@q\u0000~\u0000Gq\u0000~"
+"\u0000Nq\u0000~\u0000Uq\u0000~\u0000Sq\u0000~\u0000)q\u0000~\u0000Aq\u0000~\u0000Hq\u0000~\u0000Oq\u0000~\u0000Vq\u0000~\u0000bq\u0000~\u0000iq\u0000~\u0000uq\u0000~\u0000\u000bq\u0000~"
+"\u0000Zq\u0000~\u0000fq\u0000~\u0000\u0007q\u0000~\u0000\bq\u0000~\u0000\tq\u0000~\u0000\\q\u0000~\u0000hq\u0000~\u0000mq\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000\nx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.xml.instprop.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.xml.instprop.impl.KeyPersonTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  24 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("proposalNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("PersonName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        if (("RoleName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  18 :
                        if (("Faculty" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  21 :
                        if (("PercentEffort" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  3 :
                        if (("PersonId" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  12 :
                        if (("PersonAddress" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  15 :
                        if (("NonEmployee" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
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
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  8 :
                        if (("PersonName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  11 :
                        if (("RoleName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  17 :
                        if (("NonEmployee" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  14 :
                        if (("PersonAddress" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("Faculty" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  5 :
                        if (("PersonId" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  2 :
                        if (("proposalNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("PercentEffort" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
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
                    case  24 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        state = 18;
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
                    case  24 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  15 :
                        state = 18;
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
                        case  24 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  13 :
                            state = 14;
                            eatText1(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText2(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText3(value);
                            return ;
                        case  22 :
                            state = 23;
                            eatText4(value);
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText5(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  7 :
                            state = 8;
                            eatText6(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText7(value);
                            return ;
                        case  19 :
                            state = 20;
                            eatText8(value);
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
                _PersonAddress = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _RoleName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProposalNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PercentEffort = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PersonId = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PersonName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _NonEmployee = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_NonEmployee = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText8(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Faculty = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Faculty = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
