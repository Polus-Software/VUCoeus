//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_otherprojectinfo_v1.impl;

public class RROtherProjectInfoImpl
    extends gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.RROtherProjectInfoTypeImpl
    implements gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfo, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfo.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RR_OtherProjectInfo";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.RROtherProjectInfoImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0", "RR_OtherProjectInfo");
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
        return (gov.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfo.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv."
+"grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Data"
+"type;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq"
+"\u0000~\u0000\u0004ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.xsd.Da"
+"taTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.dat"
+"atype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012need"
+"ValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSData"
+"typeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/Concret"
+"eType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datat"
+"ype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000#L\u0000\btype"
+"Nameq\u0000~\u0000#L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpac"
+"eProcessor;xpt\u00001http://apply.grants.gov/system/GlobalLibrary"
+"-V1.0t\u0000\rYesNoDataTypesr\u00005com.sun.msv.datatype.xsd.WhiteSpace"
+"Processor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.Wh"
+"iteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd"
+".StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dataty"
+"pe.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype."
+"xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$t\u0000 http://www.w3.org/2001/X"
+"MLSchemat\u0000\u0006stringq\u0000~\u0000+\u0001q\u0000~\u0000/t\u0000\u000benumerationsr\u0000\u0011java.util.Hash"
+"Set\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0003Yest\u0000\u0002Noxsr\u00000com.sun.msv.gr"
+"ammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000\u001bcom"
+".sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000#L\u0000\fnames"
+"paceURIq\u0000~\u0000#xpq\u0000~\u0000(q\u0000~\u0000\'sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boolean\u00cd "
+"r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0019ppsr\u0000\"com.sun.msv.datatype.xsd.Qn"
+"ameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000-q\u0000~\u00000t\u0000\u0005QNamesr\u00005com.sun.msv.datatyp"
+"e.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000*q\u0000~\u00008sq\u0000~"
+"\u00009q\u0000~\u0000Dq\u0000~\u00000sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000#L\u0000\fnamespaceURIq\u0000~\u0000#xr\u0000\u001dcom.sun.msv.gramm"
+"ar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XM"
+"LSchema-instancesr\u00000com.sun.msv.grammar.Expression$EpsilonEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000?\u0001q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0016HumanSubjectsI"
+"ndicatort\u00006http://apply.grants.gov/forms/RR_OtherProjectInfo"
+"-V1.0sq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsr\u0000 com.s"
+"un.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.gramma"
+"r.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@psr\u00002"
+"com.sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xq\u0000~\u0000\u0004q\u0000~\u0000Oq\u0000~\u0000]sr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000Iq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000`gov.grants.apply.forms.rr_otherproje"
+"ctinfo_v1.RROtherProjectInfoType.HumanSubjectsSupplementType"
+"t\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000;ppsq\u0000~\u0000="
+"q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0017HumanSubjectsSupplementq\u0000~\u0000Rq\u0000"
+"~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001csq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000N"
+"sq\u0000~\u0000Ht\u0000\u001aVertebrateAnimalsIndicatorq\u0000~\u0000Rsq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000@p"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~\u0000_"
+"q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000dgov.grants.apply.forms.rr_otherprojectinfo_v1."
+"RROtherProjectInfoType.VertebrateAnimalsSupplementTypeq\u0000~\u0000bs"
+"q\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u001bVertebrateAnimals"
+"Supplementq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000\u001csq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000"
+"@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u001fProprietaryInformationIndicatorq\u0000~"
+"\u0000Rsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@p"
+"q\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\\gov.grants.apply.forms.rr_otherproje"
+"ctinfo_v1.RROtherProjectInfoType.EnvironmentalImpactTypeq\u0000~\u0000"
+"bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0013EnvironmentalIm"
+"pactq\u0000~\u0000Rsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000"
+"=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000`gov.grants.apply.forms.rr_oth"
+"erprojectinfo_v1.RROtherProjectInfoType.InternationalActivit"
+"iesTypeq\u0000~\u0000bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0017Inte"
+"rnationalActivitiesq\u0000~\u0000Rsq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000p"
+"p\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\\go"
+"v.grants.apply.forms.rr_otherprojectinfo_v1.RROtherProjectIn"
+"foType.AbstractAttachmentsTypeq\u0000~\u0000bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000A"
+"q\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0013AbstractAttachmentsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000;ppsq\u0000~"
+"\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000"
+"~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000^gov.grants.apply.forms.rr_otherproject"
+"info_v1.RROtherProjectInfoType.FacilitiesAttachmentsTypeq\u0000~\u0000"
+"bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0015FacilitiesAttac"
+"hmentsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;"
+"ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000]gov.grants"
+".apply.forms.rr_otherprojectinfo_v1.RROtherProjectInfoType.E"
+"quipmentAttachmentsTypeq\u0000~\u0000bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000"
+"~\u0000Nsq\u0000~\u0000Ht\u0000\u0014EquipmentAttachmentsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000"
+"@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~"
+"\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000`gov.grants.apply.forms.rr_otherprojectinfo_v"
+"1.RROtherProjectInfoType.BibliographyAttachmentsTypeq\u0000~\u0000bsq\u0000"
+"~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0017BibliographyAttachm"
+"entsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000;ppsq\u0000~\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;pp"
+"sq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000dgov.grants.a"
+"pply.forms.rr_otherprojectinfo_v1.RROtherProjectInfoType.Pro"
+"jectNarrativeAttachmentsTypeq\u0000~\u0000bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000"
+"~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u001bProjectNarrativeAttachmentsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000;"
+"ppsq\u0000~\u0000\u0000q\u0000~\u0000@p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000;ppsq\u0000~\u0000Xq\u0000~\u0000@psq\u0000~\u0000=q\u0000"
+"~\u0000@pq\u0000~\u0000]q\u0000~\u0000_q\u0000~\u0000Nsq\u0000~\u0000Ht\u0000Ygov.grants.apply.forms.rr_otherp"
+"rojectinfo_v1.RROtherProjectInfoType.OtherAttachmentsTypeq\u0000~"
+"\u0000bsq\u0000~\u0000;ppsq\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0010OtherAttachmen"
+"tsq\u0000~\u0000Rq\u0000~\u0000Nsq\u0000~\u0000=ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u001aL\u0000\u0004nameq\u0000~\u0000\u001bL\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004"
+"ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tm"
+"axLengthxq\u0000~\u0000\u001fq\u0000~\u0000\'t\u0000\u0013FormVersionDataTypeq\u0000~\u0000+\u0000\u0001sr\u0000\'com.sun."
+"msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u001f"
+"q\u0000~\u0000\'q\u0000~\u0000\u00ecq\u0000~\u0000+\u0000\u0000q\u0000~\u0000/q\u0000~\u0000/t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000/t\u0000\tmaxLength"
+"\u0000\u0000\u0000\u001esq\u0000~\u00009q\u0000~\u0000\u00ecq\u0000~\u0000\'t\u0000\u00031.0sq\u0000~\u0000Ht\u0000\u000bFormVersionq\u0000~\u0000Rsq\u0000~\u0000;pps"
+"q\u0000~\u0000=q\u0000~\u0000@pq\u0000~\u0000Aq\u0000~\u0000Jq\u0000~\u0000Nsq\u0000~\u0000Ht\u0000\u0013RR_OtherProjectInfoq\u0000~\u0000Rs"
+"r\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet"
+"\u0000/Lcom/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.s"
+"un.msv.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB"
+"\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPo"
+"ol;xp\u0000\u0000\u0000E\u0001pq\u0000~\u0000iq\u0000~\u0000cq\u0000~\u0000<q\u0000~\u0000\u00e2q\u0000~\u0000\u00f5q\u0000~\u0000\u00d1q\u0000~\u0000\u00c4q\u0000~\u0000\u00b7q\u0000~\u0000\u00aaq\u0000~\u0000"
+"\u009dq\u0000~\u0000\u0090q\u0000~\u0000\u0084q\u0000~\u0000rq\u0000~\u0000Zq\u0000~\u0000\fq\u0000~\u0000\u00deq\u0000~\u0000\u0012q\u0000~\u0000\rq\u0000~\u0000{q\u0000~\u0000hq\u0000~\u0000\u0018q\u0000~\u0000"
+"\u0010q\u0000~\u0000\u0011q\u0000~\u0000\u0013q\u0000~\u0000\u00d0q\u0000~\u0000\u00c3q\u0000~\u0000\u00b6q\u0000~\u0000\u00a9q\u0000~\u0000\u009cq\u0000~\u0000\u008fq\u0000~\u0000\u0083q\u0000~\u0000qq\u0000~\u0000Wq\u0000~\u0000"
+"\u00c1q\u0000~\u0000\u00b4q\u0000~\u0000\u00a7q\u0000~\u0000\u009aq\u0000~\u0000\u008dq\u0000~\u0000\u0081q\u0000~\u0000oq\u0000~\u0000Uq\u0000~\u0000\u00ceq\u0000~\u0000\u00ddq\u0000~\u0000\u00dbq\u0000~\u0000\u0016q\u0000~\u0000"
+"\u000bq\u0000~\u0000\nq\u0000~\u0000\u00bfq\u0000~\u0000\u00b2q\u0000~\u0000\u00a5q\u0000~\u0000\u0098q\u0000~\u0000mq\u0000~\u0000Sq\u0000~\u0000\u00ccq\u0000~\u0000\u00d9q\u0000~\u0000\u000fq\u0000~\u0000\u000eq\u0000~\u0000"
+"\u0015q\u0000~\u0000\tq\u0000~\u0000\u0014q\u0000~\u0000\u00d5q\u0000~\u0000\u00c8q\u0000~\u0000\u00bbq\u0000~\u0000\u00aeq\u0000~\u0000\u00a1q\u0000~\u0000\u0094q\u0000~\u0000\u0088q\u0000~\u0000|q\u0000~\u0000vx"));
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
            return gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.RROtherProjectInfoImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("RR_OtherProjectInfo" == ___local)&&("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0", "FormVersion");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                        if (("RR_OtherProjectInfo" == ___local)&&("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0", "FormVersion");
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.RROtherProjectInfoTypeImpl)gov.grants.apply.forms.rr_otherprojectinfo_v1.impl.RROtherProjectInfoImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_OtherProjectInfo-V1.0", "FormVersion");
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
