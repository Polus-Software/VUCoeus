//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_personaldata_1_2_v1_2.impl;

public class DirectorTypeImpl implements gov.grants.apply.forms.rr_personaldata_1_2_v1_2.DirectorType, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    protected com.sun.xml.bind.util.ListImpl _Race;
    protected java.lang.String _Gender;
    protected com.sun.xml.bind.util.ListImpl _DisabilityStatus;
    protected java.lang.String _Citizenship;
    protected java.lang.String _Ethnicity;
    protected gov.grants.apply.system.globallibrary_v2.HumanNameDataType _Name;
    public final static java.lang.Class version = (gov.grants.apply.forms.rr_personaldata_1_2_v1_2.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_personaldata_1_2_v1_2.DirectorType.class);
    }

    protected com.sun.xml.bind.util.ListImpl _getRace() {
        if (_Race == null) {
            _Race = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Race;
    }

    public java.util.List getRace() {
        return _getRace();
    }

    public java.lang.String getGender() {
        return _Gender;
    }

    public void setGender(java.lang.String value) {
        _Gender = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getDisabilityStatus() {
        if (_DisabilityStatus == null) {
            _DisabilityStatus = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _DisabilityStatus;
    }

    public java.util.List getDisabilityStatus() {
        return _getDisabilityStatus();
    }

    public java.lang.String getCitizenship() {
        return _Citizenship;
    }

    public void setCitizenship(java.lang.String value) {
        _Citizenship = value;
    }

    public java.lang.String getEthnicity() {
        return _Ethnicity;
    }

    public void setEthnicity(java.lang.String value) {
        _Ethnicity = value;
    }

    public gov.grants.apply.system.globallibrary_v2.HumanNameDataType getName() {
        return _Name;
    }

    public void setName(gov.grants.apply.system.globallibrary_v2.HumanNameDataType value) {
        _Name = value;
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_personaldata_1_2_v1_2.impl.DirectorTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Race == null)? 0 :_Race.size());
        int idx3 = 0;
        final int len3 = ((_DisabilityStatus == null)? 0 :_DisabilityStatus.size());
        context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "Name");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _Name), "Name");
        context.endElement();
        if (_Gender!= null) {
            context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "Gender");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Gender), "Gender");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx1 != len1) {
            context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "Race");
            int idx_4 = idx1;
            try {
                idx_4 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endNamespaceDecls();
            int idx_5 = idx1;
            try {
                idx_5 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Race.get(idx1 ++)), "Race");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Ethnicity!= null) {
            context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "Ethnicity");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Ethnicity), "Ethnicity");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx3 != len3) {
            context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "DisabilityStatus");
            int idx_8 = idx3;
            try {
                idx_8 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endNamespaceDecls();
            int idx_9 = idx3;
            try {
                idx_9 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DisabilityStatus.get(idx3 ++)), "DisabilityStatus");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Citizenship!= null) {
            context.startElement("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2", "Citizenship");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Citizenship), "Citizenship");
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Race == null)? 0 :_Race.size());
        int idx3 = 0;
        final int len3 = ((_DisabilityStatus == null)? 0 :_DisabilityStatus.size());
        while (idx1 != len1) {
            try {
                idx1 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
        while (idx3 != len3) {
            try {
                idx3 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public void serializeURIs(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx1 = 0;
        final int len1 = ((_Race == null)? 0 :_Race.size());
        int idx3 = 0;
        final int len3 = ((_DisabilityStatus == null)? 0 :_DisabilityStatus.size());
        while (idx1 != len1) {
            try {
                idx1 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
        while (idx3 != len3) {
            try {
                idx3 += 1;
            } catch (java.lang.Exception e) {
                gov.grants.apply.forms.attachments_v1.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (gov.grants.apply.forms.rr_personaldata_1_2_v1_2.DirectorType.class);
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
+"lq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\npp\u0000sr\u0000\u001dcom.sun.msv.grammar.Choi"
+"ceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003exp"
+"q\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tname"
+"Classq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\u0016psr\u00002com.sun.msv.grammar.Expression$Any"
+"StringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0015\u0001q\u0000~\u0000\u001asr\u0000 com.sun.msv"
+".grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.sun.msv.grammar.Name"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$Epsilon"
+"Expression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000\u001bq\u0000~\u0000 sr\u0000#com.sun.msv.grammar"
+".SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNamet\u0000\u0012Ljava/lang/String;"
+"L\u0000\fnamespaceURIq\u0000~\u0000\"xq\u0000~\u0000\u001dt\u0000:gov.grants.apply.system.globall"
+"ibrary_v2.HumanNameDataTypet\u0000+http://java.sun.com/jaxb/xjc/d"
+"ummy-elementssq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016psr\u0000\u001bcom.sun.msv.grammar.Dat"
+"aExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exc"
+"eptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\"c"
+"om.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv."
+"datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dat"
+"atype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xs"
+"d.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\"L\u0000\btypeNameq\u0000"
+"~\u0000\"L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProce"
+"ssor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005QNamesr\u00005com.su"
+"n.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xps"
+"r\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocal"
+"Nameq\u0000~\u0000\"L\u0000\fnamespaceURIq\u0000~\u0000\"xpq\u0000~\u00003q\u0000~\u00002sq\u0000~\u0000!t\u0000\u0004typet\u0000)htt"
+"p://www.w3.org/2001/XMLSchema-instanceq\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0004Namet\u00006h"
+"ttp://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2sq\u0000~\u0000\u0010p"
+"psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000(ppsr\u0000)com.sun.msv.datatype.xsd.E"
+"numerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com"
+".sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7"
+"\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun"
+"/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/"
+"msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\"xq\u0000~\u0000/q\u0000~\u0000@t\u0000"
+"\nGenderTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$"
+"Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00005\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.Stri"
+"ngType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000-q\u0000~\u00002t\u0000\u0006stringq\u0000~\u0000N\u0001q"
+"\u0000~\u0000Pt\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@"
+"\u0000\u0000\u0000\u0000\u0000\u0003t\u0000\u0016Do Not Wish to Providet\u0000\u0004Malet\u0000\u0006Femalexq\u0000~\u00008sq\u0000~\u00009q"
+"\u0000~\u0000Lq\u0000~\u0000@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0006Genderq"
+"\u0000~\u0000@q\u0000~\u0000 sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000(ppsq"
+"\u0000~\u0000Eq\u0000~\u0000@t\u0000\bRaceTypeq\u0000~\u0000N\u0000\u0000q\u0000~\u0000Pq\u0000~\u0000Pq\u0000~\u0000Rsq\u0000~\u0000Sw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000"
+"\u0000\u0006t\u0000\u0016Do Not Wish to Providet\u0000\u0019Black or African Americant\u0000\u0005Wh"
+"itet\u0000)Native Hawaiian or Other Pacific Islandert\u0000\u0005Asiant\u0000 Am"
+"erican Indian or Alaska Nativexq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000cq\u0000~\u0000@sq\u0000~\u0000\u0010pp"
+"sq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0004Raceq\u0000~\u0000@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q"
+"\u0000~\u0000\u0016psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000asq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000"
+";q\u0000~\u0000 q\u0000~\u0000nsq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000asq\u0000"
+"~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 q\u0000~\u0000nsq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000"
+"~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000asq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 q\u0000"
+"~\u0000nsq\u0000~\u0000\u0010ppsq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000asq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~"
+"\u0000+q\u0000~\u0000;q\u0000~\u0000 q\u0000~\u0000nq\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 sq\u0000~\u0000\u0010ppsq\u0000~\u0000\nq\u0000~\u0000"
+"\u0016p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000(ppsq\u0000~\u0000Eq\u0000~\u0000@t\u0000\rEthnicityTypeq\u0000~\u0000N\u0000\u0000q\u0000~\u0000Pq\u0000"
+"~\u0000Pq\u0000~\u0000Rsq\u0000~\u0000Sw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0003t\u0000\u0016Not Hispanic or Latinot\u0000\u0016Do N"
+"ot Wish To Providet\u0000\u0012Hispanic or Latinoxq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000\u008cq\u0000~\u0000"
+"@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 sq\u0000~\u0000!t\u0000\tEthnicityq\u0000~\u0000@q"
+"\u0000~\u0000 sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000(ppsq\u0000~\u0000Eq"
+"\u0000~\u0000@t\u0000\u0015DisablilityStatusTypeq\u0000~\u0000N\u0000\u0000q\u0000~\u0000Pq\u0000~\u0000Pq\u0000~\u0000Rsq\u0000~\u0000Sw\f\u0000\u0000"
+"\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0006t\u0000\u0016Do Not Wish to Providet\u0000\u0004Nonet\u0000\u001eMobility/Orthop"
+"edic Impairmentt\u0000\u0006Visualt\u0000\u0007Hearingt\u0000\u0005Otherxq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000\u009cq"
+"\u0000~\u0000@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 sq\u0000~\u0000!t\u0000\u0010DisabilitySt"
+"atusq\u0000~\u0000@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u009asq\u0000~\u0000"
+"\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 q\u0000~\u0000\u00a7sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0016psq\u0000~\u0000"
+"\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u009asq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 q\u0000~\u0000"
+"\u00a7sq\u0000~\u0000\u0010ppsq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u009asq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+"
+"q\u0000~\u0000;q\u0000~\u0000 q\u0000~\u0000\u00a7q\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 q\u0000~\u0000 sq\u0000~\u0000\u0010ppsq\u0000~\u0000\nq\u0000~\u0000\u0016p\u0000sq\u0000~"
+"\u0000\u0000ppsq\u0000~\u0000(ppsq\u0000~\u0000Eq\u0000~\u0000@t\u0000\u000fCitizenshipTypeq\u0000~\u0000N\u0000\u0000q\u0000~\u0000Pq\u0000~\u0000Pq\u0000"
+"~\u0000Rsq\u0000~\u0000Sw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0004t\u0000\u0016Do Not Wish to Providet\u0000\nUS Citize"
+"nt\u0000\u0014Other non-US Citizent\u0000\u0012Permanent Residentxq\u0000~\u00008sq\u0000~\u00009q\u0000~"
+"\u0000\u00bfq\u0000~\u0000@sq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0017q\u0000~\u0000\u0016pq\u0000~\u0000+q\u0000~\u0000;q\u0000~\u0000 sq\u0000~\u0000!t\u0000\u000bCitizensh"
+"ipq\u0000~\u0000@q\u0000~\u0000 sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHas"
+"h;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed"
+"\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar"
+"/ExpressionPool;xp\u0000\u0000\u00004\u0001pq\u0000~\u0000\u0082q\u0000~\u0000qq\u0000~\u0000|q\u0000~\u0000Cq\u0000~\u0000\u0089q\u0000~\u0000\u0099q\u0000~\u0000\u00acq"
+"\u0000~\u0000\u00b2q\u0000~\u0000\u00b7q\u0000~\u0000\u00afq\u0000~\u0000\u00baq\u0000~\u0000\bq\u0000~\u0000\u0005q\u0000~\u0000Aq\u0000~\u0000\u00a9q\u0000~\u0000&q\u0000~\u0000Yq\u0000~\u0000lq\u0000~\u0000tq"
+"\u0000~\u0000zq\u0000~\u0000\u0080q\u0000~\u0000\u0085q\u0000~\u0000\u0092q\u0000~\u0000pq\u0000~\u0000\u0007q\u0000~\u0000\u00a5q\u0000~\u0000\u00adq\u0000~\u0000\u00b3q\u0000~\u0000\u00b8q\u0000~\u0000\u00c6q\u0000~\u0000}q"
+"\u0000~\u0000\u00b5q\u0000~\u0000\u0087q\u0000~\u0000`q\u0000~\u0000sq\u0000~\u0000yq\u0000~\u0000\u007fq\u0000~\u0000\u0084q\u0000~\u0000\u0096q\u0000~\u0000]q\u0000~\u0000\u0097q\u0000~\u0000^q\u0000~\u0000wq"
+"\u0000~\u0000\u000eq\u0000~\u0000\u00aaq\u0000~\u0000\tq\u0000~\u0000\u0011q\u0000~\u0000vq\u0000~\u0000\u0006q\u0000~\u0000\u0014q\u0000~\u0000\u00b0q\u0000~\u0000\u00bcx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends gov.grants.apply.forms.attachments_v1.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
            super(context, "-----------------");
        }

        protected Unmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return gov.grants.apply.forms.rr_personaldata_1_2_v1_2.impl.DirectorTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("Gender" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  11 :
                        if (("DisabilityStatus" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 12;
                            return ;
                        }
                        if (("Citizenship" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 14;
                            return ;
                        }
                        state = 16;
                        continue outer;
                    case  0 :
                        if (("Name" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
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
                    case  6 :
                        if (("Race" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        if (("Ethnicity" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 9;
                            return ;
                        }
                        state = 11;
                        continue outer;
                    case  16 :
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
                    case  15 :
                        if (("Citizenship" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 16;
                            return ;
                        }
                        break;
                    case  13 :
                        if (("DisabilityStatus" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 11;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  11 :
                        state = 16;
                        continue outer;
                    case  10 :
                        if (("Ethnicity" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 11;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 11;
                        continue outer;
                    case  5 :
                        if (("Gender" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("Race" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  16 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("Name" == ___local)&&("http://apply.grants.gov/forms/RR_PersonalData_1_2-V1-2" == ___uri)) {
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
                        state = 6;
                        continue outer;
                    case  11 :
                        state = 16;
                        continue outer;
                    case  6 :
                        state = 11;
                        continue outer;
                    case  16 :
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
                        state = 6;
                        continue outer;
                    case  11 :
                        state = 16;
                        continue outer;
                    case  6 :
                        state = 11;
                        continue outer;
                    case  16 :
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
                            state = 6;
                            continue outer;
                        case  11 :
                            state = 16;
                            continue outer;
                        case  14 :
                            eatText1(value);
                            state = 15;
                            return ;
                        case  4 :
                            eatText2(value);
                            state = 5;
                            return ;
                        case  6 :
                            state = 11;
                            continue outer;
                        case  9 :
                            eatText3(value);
                            state = 10;
                            return ;
                        case  7 :
                            eatText4(value);
                            state = 8;
                            return ;
                        case  16 :
                            revertToParentFromText(value);
                            return ;
                        case  12 :
                            eatText5(value);
                            state = 13;
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
                _Citizenship = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Gender = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Ethnicity = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _getRace().add(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _getDisabilityStatus().add(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
