//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class ProgramDirectorPrincipalInvestigatorTypeImpl implements edu.mit.coeus.utils.xml.bean.proposal.nih.ProgramDirectorPrincipalInvestigatorType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _Degree;
    protected java.lang.String _AccountIdentifier;
    protected edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType _Name;
    protected edu.mit.coeus.utils.xml.bean.proposal.nih.SignatureType _DirectorInvestigatorSignature;
    protected boolean has_NewInvestigatorQuestion;
    protected boolean _NewInvestigatorQuestion;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType _ContactInformation;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ProgramDirectorPrincipalInvestigatorType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getDegree() {
        if (_Degree == null) {
            _Degree = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Degree;
    }

    public java.util.List getDegree() {
        return _getDegree();
    }

    public java.lang.String getAccountIdentifier() {
        return _AccountIdentifier;
    }

    public void setAccountIdentifier(java.lang.String value) {
        _AccountIdentifier = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType getName() {
        return _Name;
    }

    public void setName(edu.mit.coeus.utils.xml.bean.proposal.rar.PersonFullNameType value) {
        _Name = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.nih.SignatureType getDirectorInvestigatorSignature() {
        return _DirectorInvestigatorSignature;
    }

    public void setDirectorInvestigatorSignature(edu.mit.coeus.utils.xml.bean.proposal.nih.SignatureType value) {
        _DirectorInvestigatorSignature = value;
    }

    public boolean isNewInvestigatorQuestion() {
        return _NewInvestigatorQuestion;
    }

    public void setNewInvestigatorQuestion(boolean value) {
        _NewInvestigatorQuestion = value;
        has_NewInvestigatorQuestion = true;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType getContactInformation() {
        return _ContactInformation;
    }

    public void setContactInformation(edu.mit.coeus.utils.xml.bean.proposal.common.ContactInfoType value) {
        _ContactInformation = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProgramDirectorPrincipalInvestigatorTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Degree == null)? 0 :_Degree.size());
        if (!has_NewInvestigatorQuestion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "NewInvestigatorQuestion"));
        }
        context.startElement("", "NewInvestigatorQuestion");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printBoolean(((boolean) _NewInvestigatorQuestion)), "NewInvestigatorQuestion");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace", "AccountIdentifier");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _AccountIdentifier), "AccountIdentifier");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "DirectorInvestigatorSignature");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _DirectorInvestigatorSignature), "DirectorInvestigatorSignature");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _DirectorInvestigatorSignature), "DirectorInvestigatorSignature");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _DirectorInvestigatorSignature), "DirectorInvestigatorSignature");
        context.endElement();
        context.startElement("", "Name");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endElement();
        context.startElement("", "ContactInformation");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ContactInformation), "ContactInformation");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ContactInformation), "ContactInformation");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _ContactInformation), "ContactInformation");
        context.endElement();
        while (idx1 != len1) {
            context.startElement("", "Degree");
            int idx_10 = idx1;
            try {
                idx_10 += 1;
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endNamespaceDecls();
            int idx_11 = idx1;
            try {
                idx_11 += 1;
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Degree.get(idx1 ++)), "Degree");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Degree == null)? 0 :_Degree.size());
        if (!has_NewInvestigatorQuestion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "NewInvestigatorQuestion"));
        }
        while (idx1 != len1) {
            try {
                idx1 += 1;
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Degree == null)? 0 :_Degree.size());
        if (!has_NewInvestigatorQuestion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "NewInvestigatorQuestion"));
        }
        while (idx1 != len1) {
            try {
                idx1 += 1;
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.ProgramDirectorPrincipalInvestigatorType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com."
+"sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000"
+"\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.Elem"
+"entExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentMode"
+"lq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000"
+"\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000$com.sun.msv"
+".datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype"
+".xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xs"
+"d.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSData"
+"typeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\bty"
+"peNameq\u0000~\u0000\u0017L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSp"
+"aceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007boolean"
+"sr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpressio"
+"n\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp"
+"\u0000psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000"
+"\u0017L\u0000\fnamespaceURIq\u0000~\u0000\u0017xpq\u0000~\u0000\u001bq\u0000~\u0000\u001asr\u0000\u001dcom.sun.msv.grammar.Cho"
+"iceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\"psq\u0000~\u0000\u000fpp"
+"sr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0014q\u0000~\u0000\u001a"
+"t\u0000\u0005QNameq\u0000~\u0000\u001eq\u0000~\u0000 sq\u0000~\u0000#q\u0000~\u0000,q\u0000~\u0000\u001asr\u0000#com.sun.msv.grammar.Si"
+"mpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0017L\u0000\fnamespaceURIq\u0000~\u0000"
+"\u0017xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)htt"
+"p://www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.gramma"
+"r.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000!\u0001q\u0000~\u00004s"
+"q\u0000~\u0000.t\u0000\u0017NewInvestigatorQuestiont\u0000\u0000sq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fpps"
+"r\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxL"
+"engthxr\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstrain"
+"tFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTyp"
+"et\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteType"
+"t\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u0017x"
+"q\u0000~\u0000\u0016t\u0000>http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific"
+".namespacepsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001d\u0000\u0001sr\u0000\'com.sun.msv.datatype.xsd.MinL"
+"engthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000=q\u0000~\u0000Bpq\u0000~\u0000D\u0000\u0000sr\u0000#com."
+"sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq"
+"\u0000~\u0000\u0014q\u0000~\u0000\u001at\u0000\u0006stringq\u0000~\u0000D\u0001q\u0000~\u0000Ht\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000Ht\u0000\tmaxLeng"
+"th\u0000\u0000\u0000\u0014q\u0000~\u0000 sq\u0000~\u0000#t\u0000\u000estring-derivedq\u0000~\u0000Bsq\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq"
+"\u0000~\u0000)q\u0000~\u00000q\u0000~\u00004sq\u0000~\u0000.t\u0000\u0011AccountIdentifierq\u0000~\u0000Bsq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000"
+"ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000%ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002"
+"xq\u0000~\u0000\u0003q\u0000~\u0000\"psq\u0000~\u0000\'q\u0000~\u0000\"psr\u00002com.sun.msv.grammar.Expression$A"
+"nyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00005q\u0000~\u0000[sr\u0000 com.sun.msv"
+".grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000/q\u0000~\u00004sq\u0000~\u0000.t\u00007edu.mit."
+"coeus.utils.xml.bean.proposal.nih.SignatureTypet\u0000+http://jav"
+"a.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000)q\u0000"
+"~\u00000q\u0000~\u00004sq\u0000~\u0000.t\u0000\u001dDirectorInvestigatorSignatureq\u0000~\u00008sq\u0000~\u0000\npp\u0000"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000%ppsq\u0000~\u0000Vq\u0000~\u0000\"psq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000[q\u0000~\u0000]q"
+"\u0000~\u00004sq\u0000~\u0000.t\u0000<edu.mit.coeus.utils.xml.bean.proposal.rar.Perso"
+"nFullNameTypeq\u0000~\u0000`sq\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000)q\u0000~\u00000q\u0000~\u00004sq\u0000~\u0000.t"
+"\u0000\u0004Nameq\u0000~\u00008sq\u0000~\u0000\npp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000%ppsq\u0000~\u0000Vq\u0000~\u0000\"psq\u0000"
+"~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000[q\u0000~\u0000]q\u0000~\u00004sq\u0000~\u0000.t\u0000<edu.mit.coeus.utils.xml.bea"
+"n.proposal.common.ContactInfoTypeq\u0000~\u0000`sq\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000"
+"~\u0000)q\u0000~\u00000q\u0000~\u00004sq\u0000~\u0000.t\u0000\u0012ContactInformationq\u0000~\u00008sq\u0000~\u0000%ppsq\u0000~\u0000\u0000q"
+"\u0000~\u0000\"psq\u0000~\u0000\nq\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fq\u0000~\u0000\"pq\u0000~\u0000Hq\u0000~\u0000 sq\u0000~\u0000#q\u0000~\u0000Iq"
+"\u0000~\u0000\u001asq\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000)q\u0000~\u00000q\u0000~\u00004sq\u0000~\u0000.t\u0000\u0006Degreeq\u0000~\u00008s"
+"q\u0000~\u0000%ppsq\u0000~\u0000\u0000q\u0000~\u0000\"psq\u0000~\u0000\nq\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0081sq\u0000~\u0000%ppsq\u0000~\u0000\'q"
+"\u0000~\u0000\"pq\u0000~\u0000)q\u0000~\u00000q\u0000~\u00004q\u0000~\u0000\u0085sq\u0000~\u0000%ppsq\u0000~\u0000\nq\u0000~\u0000\"p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0081s"
+"q\u0000~\u0000%ppsq\u0000~\u0000\'q\u0000~\u0000\"pq\u0000~\u0000)q\u0000~\u00000q\u0000~\u00004q\u0000~\u0000\u0085q\u0000~\u00004q\u0000~\u00004q\u0000~\u00004sr\u0000\"co"
+"m.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lco"
+"m/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.ms"
+"v.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstr"
+"eamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp"
+"\u0000\u0000\u0000 \u0001pq\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000:q\u0000~\u0000\u000eq\u0000~\u0000\u0007q\u0000~\u0000Xq\u0000~\u0000iq\u0000~\u0000uq\u0000~\u0000\u0088q\u0000~\u0000Sq\u0000~\u0000"
+"fq\u0000~\u0000rq\u0000~\u0000Uq\u0000~\u0000hq\u0000~\u0000tq\u0000~\u0000}q\u0000~\u0000\u008dq\u0000~\u0000\u0080q\u0000~\u0000\u008aq\u0000~\u0000\u008fq\u0000~\u0000\u0087q\u0000~\u0000&q\u0000~\u0000"
+"Nq\u0000~\u0000aq\u0000~\u0000mq\u0000~\u0000yq\u0000~\u0000\u0083q\u0000~\u0000\u008bq\u0000~\u0000\u0090q\u0000~\u0000\bq\u0000~\u0000~q\u0000~\u0000\tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.ProgramDirectorPrincipalInvestigatorTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("AccountIdentifier" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        break;
                    case  0 :
                        if (("NewInvestigatorQuestion" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  10 :
                        if (("NamePrefix" == ___local)&&("" == ___uri)) {
                            _Name = ((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("FirstName" == ___local)&&("" == ___uri)) {
                            _Name = ((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.PersonFullNameTypeImpl.class), 11, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  12 :
                        if (("ContactInformation" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 13;
                            return ;
                        }
                        break;
                    case  7 :
                        if (("SignatureAuthentication" == ___local)&&("" == ___uri)) {
                            _DirectorInvestigatorSignature = ((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.SignatureTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.nih.impl.SignatureTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  9 :
                        if (("Name" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("DirectorInvestigatorSignature" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("Degree" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  13 :
                        if (("PostalAddress" == ___local)&&("" == ___uri)) {
                            _ContactInformation = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.ContactInfoTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.ContactInfoTypeImpl.class), 14, ___uri, ___local, ___qname, __atts));
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
                    case  17 :
                        if (("Degree" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("NewInvestigatorQuestion" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("AccountIdentifier" == ___local)&&("http://era.nih.gov/Projectmgmt/SBIR/CGAP/nihspecific.namespace" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("DirectorInvestigatorSignature" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("Name" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  14 :
                        if (("ContactInformation" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
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
                    case  15 :
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
                    case  15 :
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
                        case  1 :
                            eatText1(value);
                            state = 2;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  16 :
                            eatText3(value);
                            state = 17;
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
                _NewInvestigatorQuestion = javax.xml.bind.DatatypeConverter.parseBoolean(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_NewInvestigatorQuestion = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _AccountIdentifier = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _getDegree().add(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
