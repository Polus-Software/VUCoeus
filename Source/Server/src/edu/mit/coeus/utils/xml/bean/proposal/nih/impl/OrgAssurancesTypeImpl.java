//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.12 at 01:32:44 EDT 
//


package edu.mit.coeus.utils.xml.bean.proposal.nih.impl;

public class OrgAssurancesTypeImpl
    extends edu.mit.coeus.utils.xml.bean.proposal.rar.impl.OrgAssurancesTypeImpl
    implements edu.mit.coeus.utils.xml.bean.proposal.nih.OrgAssurancesType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.ValidatableObject
{

    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _HumanFetalTissue;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _RecombinantDNAHumanGeneTransfer;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _VertebrateAnimals;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _WomenAndMinorityInclusionPolicy;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _HumanEmbryonicStemCells;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _NondelinquencyOnFederalDebt;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _HumanSubjects;
    protected edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType _ChildrenInclusionPolicy;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.proposal.nih.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.OrgAssurancesType.class);
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getHumanFetalTissue() {
        return _HumanFetalTissue;
    }

    public void setHumanFetalTissue(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _HumanFetalTissue = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getRecombinantDNAHumanGeneTransfer() {
        return _RecombinantDNAHumanGeneTransfer;
    }

    public void setRecombinantDNAHumanGeneTransfer(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _RecombinantDNAHumanGeneTransfer = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getVertebrateAnimals() {
        return _VertebrateAnimals;
    }

    public void setVertebrateAnimals(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _VertebrateAnimals = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getWomenAndMinorityInclusionPolicy() {
        return _WomenAndMinorityInclusionPolicy;
    }

    public void setWomenAndMinorityInclusionPolicy(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _WomenAndMinorityInclusionPolicy = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getHumanEmbryonicStemCells() {
        return _HumanEmbryonicStemCells;
    }

    public void setHumanEmbryonicStemCells(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _HumanEmbryonicStemCells = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getNondelinquencyOnFederalDebt() {
        return _NondelinquencyOnFederalDebt;
    }

    public void setNondelinquencyOnFederalDebt(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _NondelinquencyOnFederalDebt = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getHumanSubjects() {
        return _HumanSubjects;
    }

    public void setHumanSubjects(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _HumanSubjects = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType getChildrenInclusionPolicy() {
        return _ChildrenInclusionPolicy;
    }

    public void setChildrenInclusionPolicy(edu.mit.coeus.utils.xml.bean.proposal.common.AssuranceType value) {
        _ChildrenInclusionPolicy = value;
    }

    public edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.proposal.nih.impl.OrgAssurancesTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
        context.startElement("", "HumanSubjects");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _HumanSubjects), "HumanSubjects");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _HumanSubjects), "HumanSubjects");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _HumanSubjects), "HumanSubjects");
        context.endElement();
        context.startElement("", "HumanFetalTissue");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _HumanFetalTissue), "HumanFetalTissue");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _HumanFetalTissue), "HumanFetalTissue");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _HumanFetalTissue), "HumanFetalTissue");
        context.endElement();
        context.startElement("", "WomenAndMinorityInclusionPolicy");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _WomenAndMinorityInclusionPolicy), "WomenAndMinorityInclusionPolicy");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _WomenAndMinorityInclusionPolicy), "WomenAndMinorityInclusionPolicy");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _WomenAndMinorityInclusionPolicy), "WomenAndMinorityInclusionPolicy");
        context.endElement();
        context.startElement("", "ChildrenInclusionPolicy");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ChildrenInclusionPolicy), "ChildrenInclusionPolicy");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ChildrenInclusionPolicy), "ChildrenInclusionPolicy");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _ChildrenInclusionPolicy), "ChildrenInclusionPolicy");
        context.endElement();
        context.startElement("", "HumanEmbryonicStemCells");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _HumanEmbryonicStemCells), "HumanEmbryonicStemCells");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _HumanEmbryonicStemCells), "HumanEmbryonicStemCells");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _HumanEmbryonicStemCells), "HumanEmbryonicStemCells");
        context.endElement();
        context.startElement("", "VertebrateAnimals");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _VertebrateAnimals), "VertebrateAnimals");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _VertebrateAnimals), "VertebrateAnimals");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _VertebrateAnimals), "VertebrateAnimals");
        context.endElement();
        context.startElement("", "NondelinquencyOnFederalDebt");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _NondelinquencyOnFederalDebt), "NondelinquencyOnFederalDebt");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _NondelinquencyOnFederalDebt), "NondelinquencyOnFederalDebt");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _NondelinquencyOnFederalDebt), "NondelinquencyOnFederalDebt");
        context.endElement();
        context.startElement("", "RecombinantDNAHumanGeneTransfer");
        context.childAsURIs(((com.sun.xml.bind.JAXBObject) _RecombinantDNAHumanGeneTransfer), "RecombinantDNAHumanGeneTransfer");
        context.endNamespaceDecls();
        context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _RecombinantDNAHumanGeneTransfer), "RecombinantDNAHumanGeneTransfer");
        context.endAttributes();
        context.childAsBody(((com.sun.xml.bind.JAXBObject) _RecombinantDNAHumanGeneTransfer), "RecombinantDNAHumanGeneTransfer");
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeAttributes(context);
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.proposal.nih.OrgAssurancesType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun."
+"msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLco"
+"m/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~"
+"\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004nam"
+"et\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000$com.sun.msv.dat"
+"atype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd"
+".BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Co"
+"ncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatype"
+"Impl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNa"
+"meq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceP"
+"rocessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0007booleansr\u00005"
+"com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xpsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr"
+"\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\f"
+"namespaceURIq\u0000~\u0000\u001expq\u0000~\u0000\"q\u0000~\u0000!sr\u0000\u001dcom.sun.msv.grammar.ChoiceE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u0012xq\u0000~\u0000\u0003q\u0000~\u0000)psq\u0000~\u0000\u0016ppsr\u0000\""
+"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0005Q"
+"Nameq\u0000~\u0000%q\u0000~\u0000\'sq\u0000~\u0000*q\u0000~\u00003q\u0000~\u0000!sr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURIq\u0000~\u0000\u001exr\u0000"
+"\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://"
+"www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000(\u0001q\u0000~\u0000;sq\u0000~\u0000"
+"5t\u0000\u001cGeneralCertificationQuestiont\u0000\u0000sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0019sq\u0000"
+"~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0010LobbyingQuestionq\u0000~"
+"\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsr\u0000 com.sun.msv.grammar."
+"OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)psr\u00002com.sun.msv.gra"
+"mmar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000<q\u0000~"
+"\u0000Osr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00006q\u0000~\u0000;"
+"sq\u0000~\u00005t\u0000:edu.mit.coeus.utils.xml.bean.proposal.common.Assura"
+"nceTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000,p"
+"psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0011DrugFreeWorkplaceq\u0000~\u0000?s"
+"q\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~"
+"\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000"
+"~\u0000;sq\u0000~\u00005t\u0000\u0016DebarmentAndSuspensionq\u0000~\u0000?sq\u0000~\u0000,ppsq\u0000~\u0000,q\u0000~\u0000)ps"
+"q\u0000~\u0000\u0011q\u0000~\u0000)p\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;s"
+"q\u0000~\u00005t\u00004edu.mit.coeus.utils.xml.bean.proposal.rar.SBIRSurvey"
+"q\u0000~\u0000Tsq\u0000~\u0000\u0011q\u0000~\u0000)p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000"
+".q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005t\u00008edu.mit.coeus.utils.xml.bean."
+"proposal.rar.SBIRSurveyTypeq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~"
+"\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\nSBIRSurveyt\u0000Ehttp://era.nih.gov/Projectmgmt/"
+"SBIR/CGAP/researchandrelated.namespaceq\u0000~\u0000;sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~"
+"\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\rHuma"
+"nSubjectsq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)p"
+"sq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~"
+"\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0010HumanFetalTissueq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000"
+"~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000"
+";sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000"
+"\u001fWomenAndMinorityInclusionPolicyq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011"
+"pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000"
+"Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0017ChildrenIn"
+"clusionPolicyq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000"
+"~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000"
+".q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u0017HumanEmbryonicStemCellsq\u0000~\u0000?s"
+"q\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~"
+"\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000"
+"~\u0000;sq\u0000~\u00005t\u0000\u0011VertebrateAnimalsq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000"
+"sq\u0000~\u0000,ppsq\u0000~\u0000Jq\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000"
+"~\u0000Tsq\u0000~\u0000,ppsq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u001bNondelinquenc"
+"yOnFederalDebtq\u0000~\u0000?sq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011pp\u0000sq\u0000~\u0000,ppsq\u0000~\u0000Jq"
+"\u0000~\u0000)psq\u0000~\u0000.q\u0000~\u0000)pq\u0000~\u0000Oq\u0000~\u0000Qq\u0000~\u0000;sq\u0000~\u00005q\u0000~\u0000Sq\u0000~\u0000Tsq\u0000~\u0000,ppsq\u0000~"
+"\u0000.q\u0000~\u0000)pq\u0000~\u00000q\u0000~\u00007q\u0000~\u0000;sq\u0000~\u00005t\u0000\u001fRecombinantDNAHumanGeneTrans"
+"ferq\u0000~\u0000?sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\b"
+"expTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xp"
+"sr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003"
+"I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/Exp"
+"ressionPool;xp\u0000\u0000\u0000@\u0001pq\u0000~\u0000Lq\u0000~\u0000\u00caq\u0000~\u0000\nq\u0000~\u0000Aq\u0000~\u0000\u0015q\u0000~\u0000\u00b1q\u0000~\u0000\u00a6q\u0000~\u0000\u009b"
+"q\u0000~\u0000\u0090q\u0000~\u0000\u0085q\u0000~\u0000zq\u0000~\u0000mq\u0000~\u0000Zq\u0000~\u0000eq\u0000~\u0000Gq\u0000~\u0000\fq\u0000~\u0000\u00bcq\u0000~\u0000\u00c7q\u0000~\u0000\tq\u0000~\u0000\b"
+"q\u0000~\u0000\u00beq\u0000~\u0000\u00b3q\u0000~\u0000\u00a8q\u0000~\u0000\u009dq\u0000~\u0000\u0092q\u0000~\u0000\u0087q\u0000~\u0000|q\u0000~\u0000oq\u0000~\u0000gq\u0000~\u0000\\q\u0000~\u0000Iq\u0000~\u0000\u00c9"
+"q\u0000~\u0000\u0005q\u0000~\u0000\rq\u0000~\u0000\u000eq\u0000~\u0000\u000fq\u0000~\u0000\u0007q\u0000~\u0000\u0006q\u0000~\u0000\u00c2q\u0000~\u0000\u00b7q\u0000~\u0000\u00acq\u0000~\u0000\u00a1q\u0000~\u0000\u0096q\u0000~\u0000\u008b"
+"q\u0000~\u0000\u0080q\u0000~\u0000tq\u0000~\u0000`q\u0000~\u0000Uq\u0000~\u0000Bq\u0000~\u0000-q\u0000~\u0000\u0010q\u0000~\u0000\u00cdq\u0000~\u0000dq\u0000~\u0000\u00bfq\u0000~\u0000\u00b4q\u0000~\u0000\u00a9"
+"q\u0000~\u0000\u009eq\u0000~\u0000\u0093q\u0000~\u0000\u0088q\u0000~\u0000}q\u0000~\u0000pq\u0000~\u0000hq\u0000~\u0000]q\u0000~\u0000\u000bx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context) {
            super(context, "--------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.proposal.common.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.proposal.nih.impl.OrgAssurancesTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  11 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _ChildrenInclusionPolicy = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 12, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  5 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _HumanFetalTissue = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 6, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  16 :
                        if (("VertebrateAnimals" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 17;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _HumanSubjects = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 3, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  20 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _NondelinquencyOnFederalDebt = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 21, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  10 :
                        if (("ChildrenInclusionPolicy" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 11;
                            return ;
                        }
                        break;
                    case  7 :
                        if (("WomenAndMinorityInclusionPolicy" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 8;
                            return ;
                        }
                        break;
                    case  25 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  4 :
                        if (("HumanFetalTissue" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 5;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _WomenAndMinorityInclusionPolicy = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 9, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  22 :
                        if (("RecombinantDNAHumanGeneTransfer" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 23;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _RecombinantDNAHumanGeneTransfer = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 24, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  17 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _VertebrateAnimals = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 18, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  19 :
                        if (("NondelinquencyOnFederalDebt" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 20;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("HumanSubjects" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 2;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("YesNoAnswer" == ___local)&&("" == ___uri)) {
                            _HumanEmbryonicStemCells = ((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.proposal.common.impl.AssuranceTypeImpl.class), 15, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        break;
                    case  0 :
                        if (("GeneralCertificationQuestion" == ___local)&&("" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.proposal.rar.impl.OrgAssurancesTypeImpl)edu.mit.coeus.utils.xml.bean.proposal.nih.impl.OrgAssurancesTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  13 :
                        if (("HumanEmbryonicStemCells" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 14;
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
                        if (("HumanSubjects" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 4;
                            return ;
                        }
                        break;
                    case  21 :
                        if (("NondelinquencyOnFederalDebt" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 22;
                            return ;
                        }
                        break;
                    case  6 :
                        if (("HumanFetalTissue" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 7;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("ChildrenInclusionPolicy" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 13;
                            return ;
                        }
                        break;
                    case  25 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  24 :
                        if (("RecombinantDNAHumanGeneTransfer" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 25;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("VertebrateAnimals" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 19;
                            return ;
                        }
                        break;
                    case  15 :
                        if (("HumanEmbryonicStemCells" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 16;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("WomenAndMinorityInclusionPolicy" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 10;
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
                    case  25 :
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
                    case  25 :
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
                        case  25 :
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
