//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424b_v1.impl;

public class AssuranceTypeImpl implements gov.grants.apply.forms.sf424b_v1.AssuranceType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected java.util.Calendar _SubmittedDate;
    protected java.lang.String _ProgramType;
    protected java.lang.String _ApplicantOrganizationName;
    protected java.lang.String _CoreSchemaVersion;
    protected java.lang.String _FormVersionIdentifier;
    protected gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType _AuthorizedRepresentative;
    public final static java.lang.Class version = (gov.grants.apply.forms.sf424b_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.sf424b_v1.AssuranceType.class);
    }

    public java.util.Calendar getSubmittedDate() {
        return _SubmittedDate;
    }

    public void setSubmittedDate(java.util.Calendar value) {
        _SubmittedDate = value;
    }

    public java.lang.String getProgramType() {
        return _ProgramType;
    }

    public void setProgramType(java.lang.String value) {
        _ProgramType = value;
    }

    public java.lang.String getApplicantOrganizationName() {
        return _ApplicantOrganizationName;
    }

    public void setApplicantOrganizationName(java.lang.String value) {
        _ApplicantOrganizationName = value;
    }

    public java.lang.String getCoreSchemaVersion() {
        return _CoreSchemaVersion;
    }

    public void setCoreSchemaVersion(java.lang.String value) {
        _CoreSchemaVersion = value;
    }

    public java.lang.String getFormVersionIdentifier() {
        return _FormVersionIdentifier;
    }

    public void setFormVersionIdentifier(java.lang.String value) {
        _FormVersionIdentifier = value;
    }

    public gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType getAuthorizedRepresentative() {
        return _AuthorizedRepresentative;
    }

    public void setAuthorizedRepresentative(gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType value) {
        _AuthorizedRepresentative = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.sf424b_v1.impl.AssuranceTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/system/Global-V1.0", "FormVersionIdentifier");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _FormVersionIdentifier), "FormVersionIdentifier");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_AuthorizedRepresentative!= null) {
            if (_AuthorizedRepresentative instanceof javax.xml.bind.Element) {
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
            } else {
                context.startElement("http://apply.grants.gov/forms/SF424B-V1.0", "AuthorizedRepresentative");
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
                context.endNamespaceDecls();
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
                context.endAttributes();
                context.childAsBody(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
                context.endElement();
            }
        }
        if (_ApplicantOrganizationName!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424B-V1.0", "ApplicantOrganizationName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ApplicantOrganizationName), "ApplicantOrganizationName");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_SubmittedDate!= null) {
            context.startElement("http://apply.grants.gov/forms/SF424B-V1.0", "SubmittedDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _SubmittedDate)), "SubmittedDate");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
        try {
            context.text(((java.lang.String) _ProgramType), "ProgramType");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        context.startAttribute("http://apply.grants.gov/system/Global-V1.0", "coreSchemaVersion");
        try {
            context.text(((java.lang.String) _CoreSchemaVersion), "CoreSchemaVersion");
        } catch (java.lang.Exception e) {
            gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endAttribute();
        if (_AuthorizedRepresentative!= null) {
            if (_AuthorizedRepresentative instanceof javax.xml.bind.Element) {
                context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
            }
        }
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/forms/SF424B-V1.0", null, true);
        context.getNamespaceContext().declareNamespace("http://apply.grants.gov/system/Global-V1.0", null, true);
        if (_AuthorizedRepresentative!= null) {
            if (_AuthorizedRepresentative instanceof javax.xml.bind.Element) {
                context.childAsURIs(((com.sun.xml.bind.JAXBObject) _AuthorizedRepresentative), "AuthorizedRepresentative");
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.sf424b_v1.AssuranceType.class);
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
+"\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com.sun.msv"
+".datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.s"
+"un.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT"
+"\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005"
+"Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/m"
+"sv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/ms"
+"v/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String"
+";xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fna"
+"mespaceUriq\u0000~\u0000\u0018L\u0000\btypeNameq\u0000~\u0000\u0018L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/"
+"datatype/xsd/WhiteSpaceProcessor;xpt\u0000*http://apply.grants.go"
+"v/system/Global-V1.0t\u0000\u0013StringMin1Max30Typesr\u00005com.sun.msv.da"
+"tatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com"
+".sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthx"
+"q\u0000~\u0000\u0014q\u0000~\u0000\u001cq\u0000~\u0000\u001dq\u0000~\u0000 \u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.Bui"
+"ltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concre"
+"teType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0019t\u0000 http://www.w3.org/2001/XMLSchemat\u0000"
+"\u0006stringq\u0000~\u0000 \u0001q\u0000~\u0000&t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000&t\u0000\tmaxLength\u0000\u0000\u0000\u001esr\u00000c"
+"om.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalName"
+"q\u0000~\u0000\u0018L\u0000\fnamespaceURIq\u0000~\u0000\u0018xpq\u0000~\u0000\u001dq\u0000~\u0000\u001csr\u0000\u001dcom.sun.msv.grammar"
+".ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Attribu"
+"teExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000bxq\u0000~\u0000\u0003sr\u0000\u0011java.l"
+"ang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u000fppsr\u0000\"com.sun.msv.da"
+"tatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u0000\'t\u0000\u0005QNamesr\u00005com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u0000\u001fq\u0000~\u0000,sq\u0000~\u0000-q\u0000~\u00008q\u0000~\u0000\'sr\u0000#com.sun.msv.grammar.SimpleNameC"
+"lass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0018L\u0000\fnamespaceURIq\u0000~\u0000\u0018xr\u0000\u001dcom."
+"sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w"
+"3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Express"
+"ion$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u00003\u0001q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0015F"
+"ormVersionIdentifierq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u0000/q\u0000~\u00004psq\u0000~\u0000\nq\u0000~\u00004p\u0000sq"
+"\u0000~\u0000/ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom"
+".sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u00004ps"
+"q\u0000~\u00001q\u0000~\u00004psr\u00002com.sun.msv.grammar.Expression$AnyStringExpre"
+"ssion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Cq\u0000~\u0000Osr\u0000 com.sun.msv.grammar.AnyN"
+"ameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000=q\u0000~\u0000Bsq\u0000~\u0000<t\u00009gov.grants.apply.form"
+"s.sf424b_v1.AuthorizedRepresentativet\u0000+http://java.sun.com/j"
+"axb/xjc/dummy-elementssq\u0000~\u0000\nq\u0000~\u00004p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sq\u0000~\u0000/pp"
+"sq\u0000~\u0000Jq\u0000~\u00004psq\u0000~\u00001q\u0000~\u00004pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000Bsq\u0000~\u0000<t\u0000=gov.grants.a"
+"pply.forms.sf424b_v1.AuthorizedRepresentativeTypeq\u0000~\u0000Tsq\u0000~\u0000/"
+"ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\u0018AuthorizedRepresentati"
+"vet\u0000)http://apply.grants.gov/forms/SF424B-V1.0q\u0000~\u0000Bsq\u0000~\u0000/pps"
+"q\u0000~\u0000\nq\u0000~\u00004p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsq\u0000~\u0000\u0013q\u0000~\u0000\u001ct\u0000\u0013StringMin1Max60Typ"
+"eq\u0000~\u0000 \u0000\u0001sq\u0000~\u0000!q\u0000~\u0000\u001cq\u0000~\u0000gq\u0000~\u0000 \u0000\u0000q\u0000~\u0000&q\u0000~\u0000&q\u0000~\u0000)\u0000\u0000\u0000\u0001q\u0000~\u0000&q\u0000~\u0000*"
+"\u0000\u0000\u0000<q\u0000~\u0000,sq\u0000~\u0000-q\u0000~\u0000gq\u0000~\u0000\u001csq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000B"
+"sq\u0000~\u0000<t\u0000\u0019ApplicantOrganizationNameq\u0000~\u0000aq\u0000~\u0000Bsq\u0000~\u0000/ppsq\u0000~\u0000\nq\u0000"
+"~\u00004p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000fppsr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5"
+"\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000$q\u0000~\u0000\'t\u0000\u0004dateq\u0000~\u0000:q\u0000~\u0000,sq\u0000~\u0000-q\u0000~\u0000uq\u0000~\u0000\'sq\u0000~\u0000/ppsq\u0000"
+"~\u00001q\u0000~\u00004pq\u0000~\u00005q\u0000~\u0000>q\u0000~\u0000Bsq\u0000~\u0000<t\u0000\rSubmittedDateq\u0000~\u0000aq\u0000~\u0000Bsq\u0000~"
+"\u00001ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0010L\u0000\u0004"
+"nameq\u0000~\u0000\u0011L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0003ppq\u0000~\u0000&sq\u0000~\u0000-q\u0000~"
+"\u0000(q\u0000~\u0000\'t\u0000\u0010Non-Constructionsq\u0000~\u0000<t\u0000\u000bprogramTypeq\u0000~\u0000asq\u0000~\u00001pps"
+"q\u0000~\u0000|ppq\u0000~\u0000&q\u0000~\u0000\u007ft\u0000\u00031.0sq\u0000~\u0000<t\u0000\u0011coreSchemaVersionq\u0000~\u0000\u001csr\u0000\"co"
+"m.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lco"
+"m/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.ms"
+"v.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstr"
+"eamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp"
+"\u0000\u0000\u0000\u0015\u0001pq\u0000~\u0000Vq\u0000~\u0000\u0007q\u0000~\u0000nq\u0000~\u0000\bq\u0000~\u0000\u0006q\u0000~\u0000\u0005q\u0000~\u0000dq\u0000~\u0000\u000eq\u0000~\u0000Iq\u0000~\u0000Xq\u0000~\u0000"
+"0q\u0000~\u0000]q\u0000~\u0000jq\u0000~\u0000wq\u0000~\u0000Fq\u0000~\u0000pq\u0000~\u0000\tq\u0000~\u0000Gq\u0000~\u0000bq\u0000~\u0000Lq\u0000~\u0000Yx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.sf424b_v1.impl.AssuranceTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  17 :
                        if (("RepresentativeName" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("RepresentativeTitle" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  16 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  13 :
                        if (("SubmittedDate" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 14;
                            return ;
                        }
                        state = 16;
                        continue outer;
                    case  10 :
                        if (("ApplicantOrganizationName" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 11;
                            return ;
                        }
                        state = 13;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "coreSchemaVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText2(v);
                            continue outer;
                        }
                        break;
                    case  6 :
                        if (("FormVersionIdentifier" == ___local)&&("http://apply.grants.gov/system/Global-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("AuthorizedRepresentative" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeImpl) spawnChildFromEnterElement((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeImpl.class), 10, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("AuthorizedRepresentative" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 17;
                            return ;
                        }
                        state = 10;
                        continue outer;
                }
                super.enterElement(___uri, ___local, ___qname, __atts);
                break;
            }
        }

        private void eatText1(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProgramType = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CoreSchemaVersion = value;
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
                    case  15 :
                        if (("SubmittedDate" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 16;
                            return ;
                        }
                        break;
                    case  17 :
                        _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromLeaveElement((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname));
                        return ;
                    case  16 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  13 :
                        state = 16;
                        continue outer;
                    case  12 :
                        if (("ApplicantOrganizationName" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 13;
                            return ;
                        }
                        break;
                    case  10 :
                        state = 13;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "coreSchemaVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText2(v);
                            continue outer;
                        }
                        break;
                    case  8 :
                        if (("FormVersionIdentifier" == ___local)&&("http://apply.grants.gov/system/Global-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("AuthorizedRepresentative" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 10;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 10;
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
                    case  17 :
                        _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromEnterAttribute((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname));
                        return ;
                    case  16 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  13 :
                        state = 16;
                        continue outer;
                    case  10 :
                        state = 13;
                        continue outer;
                    case  0 :
                        if (("programType" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        if (("coreSchemaVersion" == ___local)&&("http://apply.grants.gov/system/Global-V1.0" == ___uri)) {
                            state = 4;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 10;
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
                    case  5 :
                        if (("coreSchemaVersion" == ___local)&&("http://apply.grants.gov/system/Global-V1.0" == ___uri)) {
                            state = 6;
                            return ;
                        }
                        break;
                    case  17 :
                        _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromLeaveAttribute((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, ___uri, ___local, ___qname));
                        return ;
                    case  16 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  13 :
                        state = 16;
                        continue outer;
                    case  2 :
                        if (("programType" == ___local)&&("http://apply.grants.gov/forms/SF424B-V1.0" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  10 :
                        state = 13;
                        continue outer;
                    case  0 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  3 :
                        attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "coreSchemaVersion");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 6;
                            eatText2(v);
                            continue outer;
                        }
                        break;
                    case  9 :
                        state = 10;
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
                            state = 8;
                            eatText3(value);
                            return ;
                        case  14 :
                            state = 15;
                            eatText4(value);
                            return ;
                        case  17 :
                            _AuthorizedRepresentative = ((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl) spawnChildFromText((gov.grants.apply.forms.sf424b_v1.impl.AuthorizedRepresentativeTypeImpl.class), 18, value));
                            return ;
                        case  16 :
                            revertToParentFromText(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  13 :
                            state = 16;
                            continue outer;
                        case  11 :
                            state = 12;
                            eatText5(value);
                            return ;
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  10 :
                            state = 13;
                            continue outer;
                        case  0 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/SF424B-V1.0", "programType");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText1(v);
                                continue outer;
                            }
                            break;
                        case  3 :
                            attIdx = context.getAttribute("http://apply.grants.gov/system/Global-V1.0", "coreSchemaVersion");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 6;
                                eatText2(v);
                                continue outer;
                            }
                            break;
                        case  9 :
                            state = 10;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FormVersionIdentifier = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _SubmittedDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ApplicantOrganizationName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
