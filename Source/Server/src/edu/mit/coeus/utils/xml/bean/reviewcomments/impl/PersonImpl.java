//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.06.04 at 03:49:17 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.reviewcomments.impl;

public class PersonImpl
    extends edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonTypeImpl
    implements edu.mit.coeus.utils.xml.bean.reviewcomments.Person, com.sun.xml.bind.RIElement, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.ValidatableObject
{

    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.reviewcomments.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.Person.class);
    }

    public java.lang.String ____jaxb_ri____getNamespaceURI() {
        return "http://irb.mit.edu/irbnamespace";
    }

    public java.lang.String ____jaxb_ri____getLocalName() {
        return "Person";
    }

    public edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        context.startElement("http://irb.mit.edu/irbnamespace", "Person");
        super.serializeURIs(context);
        context.endNamespaceDecls();
        super.serializeAttributes(context);
        context.endAttributes();
        super.serializeBody(context);
        context.endElement();
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.reviewcomments.Person.class);
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
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007pps"
+"q\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000"
+"\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppsr\u0000\u001bcom."
+"sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datat"
+"ype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0003L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/Strin"
+"gPair;xq\u0000~\u0000\u0004ppsr\u0000\'com.sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthxr\u00009com.sun.msv.datatype.xsd.DataTypeWit"
+"hValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd"
+".DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueChec"
+"kFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;"
+"L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000\t"
+"facetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd.X"
+"SDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u00002L\u0000\btypeNameq\u0000~\u00002"
+"L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcesso"
+"r;xpt\u0000\u001fhttp://irb.mit.edu/irbnamespacepsr\u00005com.sun.msv.datat"
+"ype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.m"
+"sv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0001sr\u0000\'com.su"
+"n.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~"
+"\u0000.q\u0000~\u00006pq\u0000~\u00009\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAto"
+"micType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00003t\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006string"
+"q\u0000~\u00009\u0001q\u0000~\u0000?t\u0000\tminLength\u0000\u0000\u0000\u0006q\u0000~\u0000?t\u0000\tmaxLength\u0000\u0000\u0000\tsr\u00000com.sun."
+"msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004pps"
+"r\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u00002L\u0000"
+"\fnamespaceURIq\u0000~\u00002xpt\u0000\u000estring-derivedq\u0000~\u00006sr\u0000\u001dcom.sun.msv.gr"
+"ammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\bppsr\u0000 com.sun.msv.grammar.At"
+"tributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0003L\u0000\tnameClassq\u0000~\u0000\u0001xq\u0000~\u0000\u0004sr\u0000\u0011j"
+"ava.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000)ppsr\u0000\"com.sun.m"
+"sv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000=q\u0000~\u0000@t\u0000\u0005QNamesr\u00005c"
+"om.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u00008q\u0000~\u0000Esq\u0000~\u0000Fq\u0000~\u0000Rq\u0000~\u0000@sr\u0000#com.sun.msv.grammar.Simple"
+"NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u00002L\u0000\fnamespaceURIq\u0000~\u00002xr\u0000"
+"\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://"
+"www.w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Ex"
+"pression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004sq\u0000~\u0000M\u0001q\u0000~\u0000\\sq\u0000~\u0000"
+"Vt\u0000\bPersonIDq\u0000~\u00006sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppsr\u0000\u001ccom.sun.msv.gr"
+"ammar.ValueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000*L\u0000\u0004nameq\u0000~\u0000+L\u0000\u0005valuet\u0000\u0012Lj"
+"ava/lang/Object;xq\u0000~\u0000\u0004ppsr\u0000$com.sun.msv.datatype.xsd.Boolean"
+"Type\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000=q\u0000~\u0000@t\u0000\u0007booleanq\u0000~\u0000Tsq\u0000~\u0000Fq\u0000~\u0000ht\u0000\u0000q\u0000~\u0000]"
+"sq\u0000~\u0000Vt\u0000\u0003nilq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000"
+"?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~"
+"\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\bLastNameq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q"
+"\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~"
+"\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estrin"
+"g-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nMi"
+"ddlenameq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000e"
+"sq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?"
+"q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000"
+"Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nSalutationq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ipps"
+"q\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000"
+"~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000"
+"\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000"
+"Vt\u0000\tFirstnameq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq"
+"\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000"
+"~\u0000C\u0000\u0000\u0000Zq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Np"
+"q\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\bFullnameq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq"
+"\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000"
+"-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000<q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derive"
+"dq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0005Emailq\u0000~\u00006"
+"q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000"
+"~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000"
+"\u000bq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq"
+"\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0006Degreeq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000"
+"Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000"
+"~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000"
+"~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000eOfficeLocatio"
+"nq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq"
+"\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000"
+"~\u0000C\u0000\u0000\u0000\u0014q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Np"
+"q\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bOfficePhoneq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q"
+"\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~"
+"\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u00002q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estrin"
+"g-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0006Sc"
+"hoolq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~"
+"\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000"
+"?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~"
+"\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\rYearGraduatedq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq"
+"\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~"
+"\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000Pq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000e"
+"string-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000V"
+"t\u0000\u0017Department_Organizationq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000"
+"~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-"
+"q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derived"
+"q\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bCitizenship"
+"q\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000"
+"~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~"
+"\u0000C\u0000\u0000\u00003q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq"
+"\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fPrimaryTitleq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q"
+"\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~"
+"\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u00002q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estrin"
+"g-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000eDi"
+"rectoryTitleq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000\u0007ppsq\u0000~\u0000\u0000pp\u0000"
+"sq\u0000~\u0000Ippsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001cc"
+"om.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0003xq\u0000~\u0000\u0004q\u0000~\u0000N"
+"psq\u0000~\u0000Kq\u0000~\u0000Npsr\u00002com.sun.msv.grammar.Expression$AnyStringExp"
+"ression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0004q\u0000~\u0000]q\u0000~\u0001Csr\u0000 com.sun.msv.grammar.An"
+"yNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Wq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000Cedu.mit.coeus.utils"
+".xml.bean.reviewcomments.PersonType.HomeUnitTypet\u0000+http://ja"
+"va.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq"
+"\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\bHomeUnitq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kp"
+"pq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npq\u0000~\u0000gq\u0000~\u0000Esq\u0000~\u0000Fq\u0000"
+"~\u0000hq\u0000~\u0000@sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bFacultyF"
+"lagq\u0000~\u00006sq\u0000~\u0000\u0000pp\u0000sq\u0000~\u0000\u0007ppq\u0000~\u0001Rsq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000X"
+"q\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fEmployeeFlagq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ipp"
+"sq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006"
+"pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000Pq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006"
+"sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fAddressLine1q\u0000~\u0000"
+"6q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq"
+"\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000"
+"\u0000Pq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000O"
+"q\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fAddressLine2q\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000N"
+"p\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Nps"
+"q\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000Pq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-de"
+"rivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\fAddres"
+"sLine3q\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq"
+"\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000"
+"~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq"
+"\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0004Cityq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~"
+"\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000N"
+"psq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-"
+"derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0007Coun"
+"tryq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000"
+"Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?"
+"q\u0000~\u0000C\u0000\u0000\u0000\u001eq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000"
+"Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0005Stateq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000N"
+"p\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Nps"
+"q\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u000fq\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-de"
+"rivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\nPostal"
+"Codeq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~"
+"\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000"
+"?q\u0000~\u0000C\u0000\u0000\u0000\u0003q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~"
+"\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bCountryCodeq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~"
+"\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)"
+"q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u0014q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000est"
+"ring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000"
+"\tFaxNumberq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~"
+"\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007ppsq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~"
+"\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u0014q\u0000~\u0000Esq\u0000~\u0000Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000"
+"~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u000bPagerNumberq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000I"
+"ppsq\u0000~\u0000\u0000q\u0000~\u0000Np\u0000sq\u0000~\u0000Ippsq\u0000~\u0000Kppq\u0000~\u0000esq\u0000~\u0000Vq\u0000~\u0000lq\u0000~\u0000Zsq\u0000~\u0000\u0007pp"
+"sq\u0000~\u0000)q\u0000~\u0000Npsq\u0000~\u0000-q\u0000~\u00006pq\u0000~\u00009\u0000\u0000q\u0000~\u0000?q\u0000~\u0000?q\u0000~\u0000C\u0000\u0000\u0000\u0014q\u0000~\u0000Esq\u0000~\u0000"
+"Ft\u0000\u000estring-derivedq\u0000~\u00006sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq\u0000~\u0000Xq\u0000~\u0000\\sq"
+"\u0000~\u0000Vt\u0000\u0011MobilePhoneNumberq\u0000~\u00006q\u0000~\u0000\\sq\u0000~\u0000Ippsq\u0000~\u0000Kq\u0000~\u0000Npq\u0000~\u0000Oq"
+"\u0000~\u0000Xq\u0000~\u0000\\sq\u0000~\u0000Vt\u0000\u0006Personq\u0000~\u00006sr\u0000\"com.sun.msv.grammar.Express"
+"ionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/Express"
+"ionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$"
+"ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lco"
+"m/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0091\u0001pq\u0000~\u0000\u00d7q\u0000~\u0001\u008aq\u0000~\u0001\u00c0q\u0000~\u0000"
+"\u00f3q\u0000~\u0000\u0014q\u0000~\u0000\u00e5q\u0000~\u0000vq\u0000~\u0000{q\u0000~\u0001nq\u0000~\u0000\u00bbq\u0000~\u0000#q\u0000~\u0000\tq\u0000~\u0000\u001dq\u0000~\u0001\u0003q\u0000~\u0001\u00ecq\u0000~\u0000"
+"\u0010q\u0000~\u0000\u0011q\u0000~\u0000\u0017q\u0000~\u0000\u00c9q\u0000~\u0000\fq\u0000~\u0001\u00a6q\u0000~\u0000\u000fq\u0000~\u0000\u00dcq\u0000~\u0000\u00b2q\u0000~\u0000\u000bq\u0000~\u0000\u00e7q\u0000~\u0001\u0011q\u0000~\u0000"
+"\u000eq\u0000~\u0001\u00a4q\u0000~\u0001\u008dq\u0000~\u0001Nq\u0000~\u0001\u00eaq\u0000~\u0000\u00bdq\u0000~\u0001\u0014q\u0000~\u0000\u00d9q\u0000~\u0000\u0013q\u0000~\u0000\u00cbq\u0000~\u0001\u00d3q\u0000~\u0001\u00aeq\u0000~\u0001"
+"\u00a0q\u0000~\u0000\u00eaq\u0000~\u0001\u0092q\u0000~\u0001\u0084q\u0000~\u0001vq\u0000~\u0001hq\u0000~\u0001Zq\u0000~\u0000\"q\u0000~\u0001\u0088q\u0000~\u0001Tq\u0000~\u0001Iq\u0000~\u0001\u007fq\u0000~\u0001"
+"5q\u0000~\u0001@q\u0000~\u0001\'q\u0000~\u0001\u0019q\u0000~\u0000Jq\u0000~\u0000rq\u0000~\u0000\u0080q\u0000~\u0000\u008eq\u0000~\u0000\u009cq\u0000~\u0000\u00a9q\u0000~\u0000\u00b7q\u0000~\u0000\u00c5q\u0000~\u0000"
+"\u00d3q\u0000~\u0000\u00e1q\u0000~\u0000\u00efq\u0000~\u0000\u00fdq\u0000~\u0001\u000bq\u0000~\u0000\u00a1q\u0000~\u0001\u00bcq\u0000~\u0001\u00caq\u0000~\u0001\u00d8q\u0000~\u0001\u00e6q\u0000~\u0001\u00f4q\u0000~\u0001\u00f8q\u0000~\u0000"
+"&q\u0000~\u0001\u0098q\u0000~\u0001^q\u0000~\u00019q\u0000~\u0001\u00a9q\u0000~\u0001\u00deq\u0000~\u0001\u0001q\u0000~\u0001\u000fq\u0000~\u0001\u00dcq\u0000~\u0000\u001cq\u0000~\u0000\u001bq\u0000~\u0000%q\u0000~\u0001"
+"\u00c5q\u0000~\u0001\u00b2q\u0000~\u0001|q\u0000~\u0001\u00b4q\u0000~\u0001lq\u0000~\u0001\u00ceq\u0000~\u0000\u0019q\u0000~\u0001\u00c2q\u0000~\u00010q\u0000~\u0000\u0089q\u0000~\u0001-q\u0000~\u0000aq\u0000~\u0000"
+"\u00ceq\u0000~\u0001cq\u0000~\u0000\u00a4q\u0000~\u0001\u00e1q\u0000~\u0000\u0015q\u0000~\u0000\u0092q\u0000~\u0000\u00f5q\u0000~\u0000\u001fq\u0000~\u0001qq\u0000~\u0000xq\u0000~\u0001\u001dq\u0000~\u0000\u001eq\u0000~\u0001"
+"`q\u0000~\u0001\u00b7q\u0000~\u0000mq\u0000~\u0001\u00d0q\u0000~\u0000\u00f8q\u0000~\u0001Yq\u0000~\u0001Qq\u0000~\u0000\nq\u0000~\u0001\u009bq\u0000~\u0000\u0086q\u0000~\u0001zq\u0000~\u0000\u00afq\u0000~\u0000"
+"\rq\u0000~\u0000\u0097q\u0000~\u0000\u0094q\u0000~\u0001;q\u0000~\u0000\u001aq\u0000~\u0001\"q\u0000~\u0001+q\u0000~\u0001\u0096q\u0000~\u0000\u00c0q\u0000~\u0001=q\u0000~\u0000$q\u0000~\u0001\u001fq\u0000~\u0000"
+"(q\u0000~\u0000\u0016q\u0000~\u0000\u00adq\u0000~\u0000!q\u0000~\u0000\u0018q\u0000~\u0000\u0084q\u0000~\u0000\u0012q\u0000~\u0001\u0006q\u0000~\u0001\u00efq\u0000~\u0000 x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context) {
            super(context, "----");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.reviewcomments.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl.this;
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
                    case  0 :
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 1;
                            return ;
                        }
                        break;
                    case  1 :
                        if (("PersonID" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonTypeImpl)edu.mit.coeus.utils.xml.bean.reviewcomments.impl.PersonImpl.this).new Unmarshaller(context)), 2, ___uri, ___local, ___qname, __atts);
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
                        if (("Person" == ___local)&&("http://irb.mit.edu/irbnamespace" == ___uri)) {
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
