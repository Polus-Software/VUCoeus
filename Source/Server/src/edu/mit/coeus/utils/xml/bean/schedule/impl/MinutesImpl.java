//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.26 at 12:29:58 PM IST 
//


package edu.mit.coeus.utils.xml.bean.schedule.impl;

public class MinutesImpl
    extends edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesTypeImpl
    implements edu.mit.coeus.utils.xml.bean.schedule.Minutes, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.schedule.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.schedule.Minutes.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://irb.mit.edu/irbnamespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Minutes";
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://irb.mit.edu/irbnamespace", "Minutes");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.schedule.Minutes.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000"
+"~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.su"
+"n.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009"
+"com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet\"\u00a7R"
+"o\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lcom/"
+"sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lcom/s"
+"un/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lang/S"
+"tring;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003"
+"L\u0000\fnamespaceUriq\u0000~\u0000 L\u0000\btypeNameq\u0000~\u0000 L\u0000\nwhiteSpacet\u0000.Lcom/sun"
+"/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000\u001fhttp://irb.mit.ed"
+"u/irbnamespacepsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProces"
+"sor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpa"
+"ceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.Strin"
+"gType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd"
+".BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Co"
+"ncreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!t\u0000 http://www.w3.org/2001/XMLSche"
+"mat\u0000\u0006stringq\u0000~\u0000\'\u0001q\u0000~\u0000+t\u0000\tmaxLength\u0000\u0000\u0000\nsr\u00000com.sun.msv.gramma"
+"r.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sr\u0000\u0011java.lang"
+".Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.StringPa"
+"ir\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000 L\u0000\fnamespaceURIq\u0000~\u0000 xpt\u0000\u000estrin"
+"g-derivedq\u0000~\u0000$sr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq"
+"\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq"
+"\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004q\u0000~\u00002psq\u0000~\u0000\u0017ppsr\u0000\"com.sun.msv.dat"
+"atype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0005QNamesr\u00005com.sun"
+".msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000"
+"~\u0000&q\u0000~\u00000sq\u0000~\u00003q\u0000~\u0000=q\u0000~\u0000,sr\u0000#com.sun.msv.grammar.SimpleNameCl"
+"ass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000 L\u0000\fnamespaceURIq\u0000~\u0000 xr\u0000\u001dcom.s"
+"un.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3"
+".org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expressi"
+"on$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u00001\u0001q\u0000~\u0000Gsq\u0000~\u0000At\u0000\nSc"
+"heduleIdq\u0000~\u0000$sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u00002psr\u0000$com.sun.msv.da"
+"tatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.datatype.xs"
+"d.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetsq\u0000~\u0000\u001exq\u0000~\u0000)q\u0000~\u0000,"
+"t\u0000\u0007integerq\u0000~\u0000?sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeW"
+"ithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\u001dppq\u0000~\u0000?\u0001\u0000sr\u0000#com.s"
+"un.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0007decim"
+"alq\u0000~\u0000?q\u0000~\u0000Vt\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u00000sq\u0000~\u00003q\u0000~\u0000Qq\u0000~\u0000,sq\u0000~\u00006"
+"ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u000bEntryNumberq\u0000~\u0000$sq\u0000~\u0000\u0000"
+"pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Msq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\r"
+"EntryTypeCodeq\u0000~\u0000$sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u00002pq\u0000~\u0000+q\u0000~\u00000sq\u0000"
+"~\u00003q\u0000~\u0000-q\u0000~\u0000,sq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\rEnt"
+"ryTypeDescq\u0000~\u0000$sq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u00002p\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000Msq\u0000~\u00006ppsq\u0000"
+"~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\rEntrySortCodeq\u0000~\u0000$q\u0000~\u0000Gsq\u0000~"
+"\u00006ppsq\u0000~\u0000\u0000q\u0000~\u00002p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017ppsq\u0000~\u0000\u001bq\u0000~\u0000$pq\u0000~\u0000\'\u0000\u0000q\u0000~\u0000+q\u0000~"
+"\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0014q\u0000~\u00000sq\u0000~\u00003t\u0000\u000estring-derivedq\u0000~\u0000$sq\u0000~\u00006ppsq\u0000~\u00008q\u0000"
+"~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u000eProtocolNumberq\u0000~\u0000$q\u0000~\u0000Gsq\u0000~\u0000\u0000pp"
+"\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u00002psr\u0000$com.sun.msv.datatype.xsd.BooleanTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0007booleanq\u0000~\u0000?q\u0000~\u00000sq\u0000~\u00003q\u0000~\u0000\u0083q\u0000~\u0000,s"
+"q\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u0012PrivateCommentFla"
+"gq\u0000~\u0000$sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000fsq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000"
+"~\u0000Gsq\u0000~\u0000At\u0000\tFinalFlagq\u0000~\u0000$sq\u0000~\u00006ppsq\u0000~\u0000\u0000q\u0000~\u00002p\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0017ppsq\u0000~\u0000\u001bq\u0000~\u0000$pq\u0000~\u0000\'\u0000\u0000q\u0000~\u0000+q\u0000~\u0000+q\u0000~\u0000.\u0000\u0000\u0000\u0004q\u0000~\u00000sq\u0000~\u00003t\u0000\u000estrin"
+"g-derivedq\u0000~\u0000$sq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u0017Pr"
+"otocolContingencyCodeq\u0000~\u0000$q\u0000~\u0000Gsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0000fsq\u0000~\u00006p"
+"psq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u000bMinuteEntryq\u0000~\u0000$sq\u0000~\u0000\u0000p"
+"p\u0000sq\u0000~\u00006ppsq\u0000~\u00008ppsr\u0000\u001ccom.sun.msv.grammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0003L\u0000\u0002dtq\u0000~\u0000\u0018L\u0000\u0004nameq\u0000~\u0000\u0019L\u0000\u0005valuet\u0000\u0012Ljava/lang/Object;xq\u0000~\u0000\u0004pp"
+"q\u0000~\u0000\u0082sq\u0000~\u00003q\u0000~\u0000\u0083t\u0000\u0000q\u0000~\u0000Hsq\u0000~\u0000At\u0000\u0003nilq\u0000~\u0000Esq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0017q\u0000~\u00002"
+"psr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun"
+".msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000)q\u0000~\u0000,t\u0000\u0004d"
+"ateq\u0000~\u0000?q\u0000~\u00000sq\u0000~\u00003q\u0000~\u0000\u00afq\u0000~\u0000,sq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000Cq"
+"\u0000~\u0000Gsq\u0000~\u0000At\u0000\u000fUpdateTimestampq\u0000~\u0000$sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u00006ppsq\u0000~\u00008ppq\u0000"
+"~\u0000\u00a5sq\u0000~\u0000Aq\u0000~\u0000\u00a9q\u0000~\u0000Esq\u0000~\u0000\u0007ppq\u0000~\u0000fsq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~"
+"\u0000Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\nUpdateUserq\u0000~\u0000$sq\u0000~\u00006ppsq\u0000~\u00008q\u0000~\u00002pq\u0000~\u0000:q\u0000~\u0000"
+"Cq\u0000~\u0000Gsq\u0000~\u0000At\u0000\u0007Minutesq\u0000~\u0000$sr\u0000\"com.sun.msv.grammar.Expressio"
+"nPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Expressio"
+"nPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$Cl"
+"osedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/"
+"sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000*\u0001pq\u0000~\u0000\u000fq\u0000~\u0000\nq\u0000~\u0000\rq\u0000~\u0000\u00b6q"
+"\u0000~\u0000lq\u0000~\u0000\u0010q\u0000~\u0000\u007fq\u0000~\u0000\u0011q\u0000~\u0000\tq\u0000~\u0000\u000eq\u0000~\u0000\u0016q\u0000~\u0000sq\u0000~\u0000\u000bq\u0000~\u0000\u0014q\u0000~\u0000\fq\u0000~\u0000Lq"
+"\u0000~\u0000_q\u0000~\u0000nq\u0000~\u0000\u008fq\u0000~\u0000\u0012q\u0000~\u0000\u00a1q\u0000~\u0000uq\u0000~\u00007q\u0000~\u0000Zq\u0000~\u0000`q\u0000~\u0000hq\u0000~\u0000oq\u0000~\u0000zq"
+"\u0000~\u0000\u0085q\u0000~\u0000\u008bq\u0000~\u0000\u0096q\u0000~\u0000\u009cq\u0000~\u0000\u00b1q\u0000~\u0000\u00aaq\u0000~\u0000\u00baq\u0000~\u0000\u00beq\u0000~\u0000eq\u0000~\u0000\u008aq\u0000~\u0000\u009bq\u0000~\u0000\u00b9q"
+"\u0000~\u0000\u0091q\u0000~\u0000\u0013x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  1 :
                        if (("ScheduleId" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesTypeImpl)edu.mit.coeus.utils.xml.bean.schedule.impl.MinutesImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  3 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
                        if (("Minutes" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
