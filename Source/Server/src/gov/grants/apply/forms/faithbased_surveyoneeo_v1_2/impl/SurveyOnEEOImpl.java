//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl;

public class SurveyOnEEOImpl
    extends gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.SurveyOnEEOTypeImpl
    implements gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.SurveyOnEEO, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.SurveyOnEEO.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "SurveyOnEEO";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.SurveyOnEEOImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2", "SurveyOnEEO");
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
        return (gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.SurveyOnEEO.class);
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
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\bppsq\u0000~\u0000\u0000sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0007"
+"ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/rel"
+"axng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/"
+"util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLeng"
+"thFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.D"
+"ataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.da"
+"tatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012nee"
+"dValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDat"
+"atypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concre"
+"teType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.data"
+"type.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000$L\u0000\btyp"
+"eNameq\u0000~\u0000$L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpa"
+"ceProcessor;xpt\u00001http://apply.grants.gov/system/GlobalLibrar"
+"y-V2.0t\u0000\u0018OrganizationNameDataTypesr\u00005com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv."
+"datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000 q\u0000~\u0000"
+"(q\u0000~\u0000)q\u0000~\u0000,\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomi"
+"cType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000"
+"~\u0000,\u0001q\u0000~\u00002t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u00002t\u0000\tmaxLength\u0000\u0000\u0000<sr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000"
+"\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000$L\u0000\fn"
+"amespaceURIq\u0000~\u0000$xpq\u0000~\u0000)q\u0000~\u0000(sq\u0000~\u0000\u0015ppsr\u0000 com.sun.msv.grammar."
+"AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~"
+"\u0000\u0019psq\u0000~\u0000\u001bppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u00000q\u0000~\u00003t\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpacePr"
+"ocessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000+q\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000Aq\u0000~\u00003sr\u0000#co"
+"m.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000"
+"$L\u0000\fnamespaceURIq\u0000~\u0000$xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr"
+"\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000\u0018\u0001q\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0010OrganizationNamet\u00009http://apply."
+"grants.gov/forms/FaithBased_SurveyOnEEO-V1-2q\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq\u0000"
+"~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u001bppsq\u0000~\u0000\u001fq\u0000~\u0000(t\u0000\u000eDUNSIDDataTypeq\u0000~\u0000,\u0000"
+"\u0001sq\u0000~\u0000-q\u0000~\u0000(q\u0000~\u0000Uq\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u00002q\u0000~\u00005\u0000\u0000\u0000\tq\u0000~\u00002q\u0000~\u00006\u0000\u0000\u0000\rq\u0000~"
+"\u00008sq\u0000~\u00009q\u0000~\u0000Uq\u0000~\u0000(sq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et"
+"\u0000\u0006DUNSIDq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u001bppsq\u0000~\u0000\u001fq\u0000~\u0000(t\u0000\u0018Opp"
+"ortunityTitleDataTypeq\u0000~\u0000,\u0000\u0001sq\u0000~\u0000-q\u0000~\u0000(q\u0000~\u0000`q\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u0000"
+"2q\u0000~\u00005\u0000\u0000\u0000\u0001q\u0000~\u00002q\u0000~\u00006\u0000\u0000\u0000\u00ffq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000`q\u0000~\u0000(sq\u0000~\u0000\u0015ppsq\u0000~\u0000<q"
+"\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0010OpportunityTitleq\u0000~\u0000Osq\u0000~\u0000\u0015ppsq"
+"\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u001bppsq\u0000~\u0000\u001fq\u0000~\u0000(t\u0000\u0012CFDANumberDataTypeq"
+"\u0000~\u0000,\u0000\u0001sq\u0000~\u0000-q\u0000~\u0000(q\u0000~\u0000lq\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u00002q\u0000~\u00005\u0000\u0000\u0000\u0001q\u0000~\u00002q\u0000~\u00006\u0000\u0000"
+"\u0000\u000fq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000lq\u0000~\u0000(sq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq"
+"\u0000~\u0000Et\u0000\nCFDANumberq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000\u001bppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xq\u0000~\u0000 q\u0000~\u0000(t\u0000\rYesNoDataTypeq\u0000~\u0000,\u0000"
+"\u0000q\u0000~\u00002q\u0000~\u00002t\u0000\u000benumerationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw"
+"\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0005N: Not\u0000\u0006Y: Yesxq\u0000~\u00008sq\u0000~\u00009q\u0000~\u0000zq\u0000~\u0000(sq\u0000~\u0000\u0015pp"
+"sq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u001cEverReceivedGovGrantCont"
+"ractq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000vsq\u0000~\u0000\u0015ppsq\u0000~"
+"\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0013FaithBasedReligiousq\u0000~\u0000Oq\u0000~\u0000"
+"Ksq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000vsq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>"
+"q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u001aNonReligiousCommunityBasedq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000"
+"\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000vsq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq"
+"\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0011ApplicantHas501c3q\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p"
+"\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000vsq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u001bLo"
+"calAffiliateOFNationalOrgq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~"
+"\u0000\u0007ppsq\u0000~\u0000\u001bppsq\u0000~\u0000wq\u0000~\u0000Opq\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u00002q\u0000~\u0000{sq\u0000~\u0000|w\f\u0000\u0000\u0000\u0010?@"
+"\u0000\u0000\u0000\u0000\u0000\u0006t\u0000\bOver 100t\u0000\u000515-50t\u0000\u000651-100t\u0000\u00034-5t\u0000\n3 or fewert\u0000\u00046-14"
+"xq\u0000~\u00008sq\u0000~\u00009t\u0000\u000estring-derivedq\u0000~\u0000Osq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q"
+"\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0016FullTimeEmployeeNumberq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000\u0015ppsq"
+"\u0000~\u0000\u0000q\u0000~\u0000\u0019p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u001bppsq\u0000~\u0000wq\u0000~\u0000Opq\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u00002q\u0000~\u0000"
+"{sq\u0000~\u0000|w\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0006t\u0000\u0013$300,000 - $499,999t\u0000\u0017$1,000,000 - $"
+"4,999,999t\u0000\u0012$5,000,000 or moret\u0000\u0013$500,000 - $999,999t\u0000\u0012Less "
+"Than $150,000t\u0000\u0013$150,000 - $299,999xq\u0000~\u00008sq\u0000~\u00009t\u0000\u000estring-der"
+"ivedq\u0000~\u0000Osq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u0015Applica"
+"ntAnnualBudgetq\u0000~\u0000Oq\u0000~\u0000Ksq\u0000~\u0000<ppsr\u0000\u001ccom.sun.msv.grammar.Valu"
+"eExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u001cL\u0000\u0004nameq\u0000~\u0000\u001dL\u0000\u0005valuet\u0000\u0012Ljava/lang/O"
+"bject;xq\u0000~\u0000\u0004ppsq\u0000~\u0000\u001fq\u0000~\u0000(t\u0000\u0013FormVersionDataTypeq\u0000~\u0000,\u0000\u0001sq\u0000~\u0000-"
+"q\u0000~\u0000(q\u0000~\u0000\u00caq\u0000~\u0000,\u0000\u0000q\u0000~\u00002q\u0000~\u00002q\u0000~\u00005\u0000\u0000\u0000\u0001q\u0000~\u00002q\u0000~\u00006\u0000\u0000\u0000\u001esq\u0000~\u00009q\u0000~\u0000"
+"\u00caq\u0000~\u0000(t\u0000\u00031.2sq\u0000~\u0000Et\u0000\u000bFormVersionq\u0000~\u0000Osq\u0000~\u0000\u0015ppsq\u0000~\u0000<q\u0000~\u0000\u0019pq\u0000~"
+"\u0000>q\u0000~\u0000Gq\u0000~\u0000Ksq\u0000~\u0000Et\u0000\u000bSurveyOnEEOq\u0000~\u0000Osr\u0000\"com.sun.msv.grammar"
+".ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar"
+"/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Express"
+"ionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006pare"
+"ntt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000-\u0001pq\u0000~\u0000\u00b5q\u0000~\u0000\u00a3q"
+"\u0000~\u0000\u0013q\u0000~\u0000\u00a1q\u0000~\u0000\u0012q\u0000~\u0000\fq\u0000~\u0000;q\u0000~\u0000Xq\u0000~\u0000cq\u0000~\u0000oq\u0000~\u0000gq\u0000~\u0000\u0081q\u0000~\u0000\u0088q\u0000~\u0000\u0010q"
+"\u0000~\u0000\u008fq\u0000~\u0000Pq\u0000~\u0000\u0096q\u0000~\u0000\u009dq\u0000~\u0000\u00afq\u0000~\u0000\u00c1q\u0000~\u0000\u00d0q\u0000~\u0000\u000eq\u0000~\u0000\nq\u0000~\u0000\u00b3q\u0000~\u0000uq\u0000~\u0000\u0087q"
+"\u0000~\u0000\u008eq\u0000~\u0000\u0095q\u0000~\u0000\u009cq\u0000~\u0000\u0011q\u0000~\u0000]q\u0000~\u0000iq\u0000~\u0000\u001aq\u0000~\u0000\u000fq\u0000~\u0000Rq\u0000~\u0000\tq\u0000~\u0000\u000bq\u0000~\u0000sq"
+"\u0000~\u0000\u0085q\u0000~\u0000\u008cq\u0000~\u0000\u0093q\u0000~\u0000\u0016q\u0000~\u0000\u0014q\u0000~\u0000\u009aq\u0000~\u0000\rx"));
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
            return gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.SurveyOnEEOImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  0 :
                        if (("SurveyOnEEO" == ___local)&&("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2" == ___uri)) {
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
                    case  2 :
                        if (("SurveyOnEEO" == ___local)&&("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.SurveyOnEEOTypeImpl)gov.grants.apply.forms.faithbased_surveyoneeo_v1_2.impl.SurveyOnEEOImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                    case  3 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
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
                        case  3 :
                            revertToParentFromText(value);
                            return ;
                        case  1 :
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/FaithBased_SurveyOnEEO-V1-2", "FormVersion");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
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
