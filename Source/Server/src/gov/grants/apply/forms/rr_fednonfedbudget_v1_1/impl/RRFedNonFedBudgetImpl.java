//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl;

public class RRFedNonFedBudgetImpl
    extends gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.RRFedNonFedBudgetTypeImpl
    implements gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallableObject, gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializable, gov.grants.apply.forms.attachments_v1.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "RR_FedNonFedBudget";
    }

    public gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingEventHandler createUnmarshaller(gov.grants.apply.forms.attachments_v1.impl.runtime.UnmarshallingContext context) {
        return new gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.RRFedNonFedBudgetImpl.Unmarshaller(context);
    }

    public void serializeBody(gov.grants.apply.forms.attachments_v1.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1", "RR_FedNonFedBudget");
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
        return (gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget.class);
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
+"\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt"
+"\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLco"
+"m/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.x"
+"sd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.data"
+"type.xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com."
+"sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetF"
+"ixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype"
+"/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/"
+"xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.su"
+"n.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq"
+"\u0000~\u0000\u001eL\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xs"
+"d/WhiteSpaceProcessor;xpt\u00001http://apply.grants.gov/system/Gl"
+"obalLibrary-V2.0t\u0000\u000eDUNSIDDataTypesr\u00005com.sun.msv.datatype.xs"
+"d.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.sun.msv."
+"datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u001aq\u0000~\u0000"
+"\"q\u0000~\u0000#q\u0000~\u0000&\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomi"
+"cType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001ft\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringq\u0000"
+"~\u0000&\u0001q\u0000~\u0000,t\u0000\tminLength\u0000\u0000\u0000\tq\u0000~\u0000,t\u0000\tmaxLength\u0000\u0000\u0000\rsr\u00000com.sun.ms"
+"v.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004ppsr\u0000"
+"\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fn"
+"amespaceURIq\u0000~\u0000\u001expq\u0000~\u0000#q\u0000~\u0000\"sr\u0000\u001dcom.sun.msv.grammar.ChoiceEx"
+"p\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011java.lang.Boole"
+"an\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0015ppsr\u0000\"com.sun.msv.datatype.xs"
+"d.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000*q\u0000~\u0000-t\u0000\u0005QNamesr\u00005com.sun.msv.dat"
+"atype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000%q\u0000~\u00002"
+"sq\u0000~\u00003q\u0000~\u0000>q\u0000~\u0000-sr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fnamespaceURIq\u0000~\u0000\u001exr\u0000\u001dcom.sun.msv.g"
+"rammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/200"
+"1/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$Epsil"
+"onExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u00009\u0001q\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0006DUNSIDt\u00005h"
+"ttp://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1sq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0015ppsr\u0000)com.sun.msv.datatype.xsd.EnumerationFac"
+"et\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006valuest\u0000\u000fLjava/util/Set;xq\u0000~\u0000\u001aq\u0000~\u0000Lt\u0000\u0012Budget"
+"TypeDataTypeq\u0000~\u0000&\u0000\u0000q\u0000~\u0000,q\u0000~\u0000,t\u0000\u000benumerationsr\u0000\u0011java.util.Has"
+"hSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0000\u0010?@\u0000\u0000\u0000\u0000\u0000\u0002t\u0000\u0007Projectt\u0000\u0013Subaward/Consort"
+"iumxq\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000Sq\u0000~\u0000Lsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000H"
+"sq\u0000~\u0000Bt\u0000\nBudgetTypeq\u0000~\u0000Lsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0015p"
+"psq\u0000~\u0000\u0019q\u0000~\u0000\"t\u0000\u0018OrganizationNameDataTypeq\u0000~\u0000&\u0000\u0001sq\u0000~\u0000\'q\u0000~\u0000\"q\u0000~"
+"\u0000cq\u0000~\u0000&\u0000\u0000q\u0000~\u0000,q\u0000~\u0000,q\u0000~\u0000/\u0000\u0000\u0000\u0001q\u0000~\u0000,q\u0000~\u00000\u0000\u0000\u0000<q\u0000~\u00002sq\u0000~\u00003q\u0000~\u0000cq\u0000"
+"~\u0000\"sq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u0010OrganizationN"
+"ameq\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsr\u0000 com.sun.m"
+"sv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.Un"
+"aryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:psr\u00002com."
+"sun.msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000\u0004q\u0000~\u0000Iq\u0000~\u0000ssr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000"
+"xq\u0000~\u0000Cq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Bgov.grants.apply.forms.rr_fednonfedbudge"
+"t_v1_1.BudgetYear1DataTypet\u0000+http://java.sun.com/jaxb/xjc/du"
+"mmy-elementssq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u000bBudg"
+"etYear1q\u0000~\u0000Lsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq"
+"\u0000~\u0000nq\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Agov.grants.app"
+"ly.forms.rr_fednonfedbudget_v1_1.BudgetYearDataTypeq\u0000~\u0000xsq\u0000~"
+"\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u000bBudgetYear2q\u0000~\u0000Lq\u0000~\u0000"
+"Hsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq\u0000~\u0000nq\u0000~\u0000:ps"
+"q\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000Hsq\u0000~\u0000Bq\u0000~\u0000\u0085q\u0000~\u0000xsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000"
+":pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u000bBudgetYear3q\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u0000"
+"\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq\u0000~\u0000nq\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~"
+"\u0000sq\u0000~\u0000uq\u0000~\u0000Hsq\u0000~\u0000Bq\u0000~\u0000\u0085q\u0000~\u0000xsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000"
+"~\u0000Hsq\u0000~\u0000Bt\u0000\u000bBudgetYear4q\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u00005ppsq\u0000~\u0000\u0000q\u0000~\u0000:p\u0000sq\u0000~\u0000\u0007"
+"ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq\u0000~\u0000nq\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000Hsq"
+"\u0000~\u0000Bq\u0000~\u0000\u0085q\u0000~\u0000xsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\u000bBu"
+"dgetYear5q\u0000~\u0000Lq\u0000~\u0000Hsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00005ppsq\u0000~\u0000nq"
+"\u0000~\u0000:psq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000sq\u0000~\u0000uq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000Vgov.grants.apply.fo"
+"rms.rr_fednonfedbudget_v1_1.RRFedNonFedBudgetType.BudgetSumm"
+"aryTypeq\u0000~\u0000xsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~\u0000Hsq\u0000~\u0000Bt\u0000\rBudg"
+"etSummaryq\u0000~\u0000Lsq\u0000~\u00007ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0016L\u0000\u0004nameq\u0000~\u0000\u0017L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~"
+"\u0000\u0004ppsq\u0000~\u0000\u0019q\u0000~\u0000\"t\u0000\u0013FormVersionDataTypeq\u0000~\u0000&\u0000\u0001sq\u0000~\u0000\'q\u0000~\u0000\"q\u0000~\u0000\u00bf"
+"q\u0000~\u0000&\u0000\u0000q\u0000~\u0000,q\u0000~\u0000,q\u0000~\u0000/\u0000\u0000\u0000\u0001q\u0000~\u0000,q\u0000~\u00000\u0000\u0000\u0000\u001esq\u0000~\u00003q\u0000~\u0000\u00bfq\u0000~\u0000\"t\u0000\u00031"
+".1sq\u0000~\u0000Bt\u0000\u000bFormVersionq\u0000~\u0000Lsq\u0000~\u00005ppsq\u0000~\u00007q\u0000~\u0000:pq\u0000~\u0000;q\u0000~\u0000Dq\u0000~"
+"\u0000Hsq\u0000~\u0000Bt\u0000\u0012RR_FedNonFedBudgetq\u0000~\u0000Lsr\u0000\"com.sun.msv.grammar.Ex"
+"pressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Ex"
+"pressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expression"
+"Pool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt"
+"\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000.\u0001pq\u0000~\u0000\u000eq\u0000~\u0000\u0010q\u0000~\u0000"
+"\u000bq\u0000~\u00006q\u0000~\u0000Zq\u0000~\u0000\u0012q\u0000~\u0000fq\u0000~\u0000yq\u0000~\u0000\u0086q\u0000~\u0000\u0092q\u0000~\u0000\u009eq\u0000~\u0000\u00aaq\u0000~\u0000\u00b6q\u0000~\u0000\u00c5q\u0000~\u0000"
+"\fq\u0000~\u0000\nq\u0000~\u0000Nq\u0000~\u0000}q\u0000~\u0000\u008aq\u0000~\u0000\u0096q\u0000~\u0000\u00a2q\u0000~\u0000kq\u0000~\u0000\u007fq\u0000~\u0000\u000fq\u0000~\u0000\u008cq\u0000~\u0000mq\u0000~\u0000"
+"\u0081q\u0000~\u0000\u008eq\u0000~\u0000\u009aq\u0000~\u0000\u0098q\u0000~\u0000\u00a6q\u0000~\u0000`q\u0000~\u0000\u00a4q\u0000~\u0000\u00b1q\u0000~\u0000\u00afq\u0000~\u0000\tq\u0000~\u0000\u0011q\u0000~\u0000pq\u0000~\u0000"
+"\u0082q\u0000~\u0000\u008fq\u0000~\u0000\u009bq\u0000~\u0000\u00a7q\u0000~\u0000\u00b2q\u0000~\u0000\u0014q\u0000~\u0000^q\u0000~\u0000\rx"));
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
            return gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.RRFedNonFedBudgetImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        if (("RR_FedNonFedBudget" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1", "FormVersion");
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
                    case  3 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("RR_FedNonFedBudget" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1", "FormVersion");
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
                        if (("FormVersion" == ___local)&&("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.RRFedNonFedBudgetTypeImpl)gov.grants.apply.forms.rr_fednonfedbudget_v1_1.impl.RRFedNonFedBudgetImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname);
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
                        attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1", "FormVersion");
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
                            attIdx = context.getAttribute("http://apply.grants.gov/forms/RR_FedNonFedBudget-V1-1", "FormVersion");
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
