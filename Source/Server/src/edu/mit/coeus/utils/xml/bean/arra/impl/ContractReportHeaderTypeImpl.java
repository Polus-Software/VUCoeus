//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class ContractReportHeaderTypeImpl
    extends edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl
    implements edu.mit.coeus.utils.xml.bean.arra.ContractReportHeaderType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected java.lang.String _OrderNumber;
    protected edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType _ContractAwardCategory;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.ContractReportHeaderType.class);
    }

    public java.lang.String getOrderNumber() {
        return _OrderNumber;
    }

    public void setOrderNumber(java.lang.String value) {
        _OrderNumber = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType getContractAwardCategory() {
        return _ContractAwardCategory;
    }

    public void setContractAwardCategory(edu.mit.coeus.utils.xml.bean.arra.AwardCategoryType value) {
        _ContractAwardCategory = value;
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeBody(context);
        if (_OrderNumber!= null) {
            context.startElement("urn:us:gov:recoveryrr-ext", "OrderNumber");
            if (_OrderNumber == null) {
                context.getNamespaceContext().declareNamespace("http://www.w3.org/2001/XMLSchema-instance", null, true);
            }
            context.endNamespaceDecls();
            if (_OrderNumber == null) {
                context.startAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                try {
                    context.text("true", "OrderNumber");
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
                context.endAttribute();
            }
            context.endAttributes();
            if (!(_OrderNumber == null)) {
                if (_OrderNumber instanceof java.lang.String) {
                    try {
                        context.text(((java.lang.String) _OrderNumber), "OrderNumber");
                    } catch (java.lang.Exception e) {
                        edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                    }
                } else {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handleTypeMismatchError(context, this, "OrderNumber", _OrderNumber);
                }
            }
            context.endElement();
        }
        if (_ContractAwardCategory instanceof javax.xml.bind.Element) {
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
        } else {
            context.startElement("urn:us:gov:recoveryrr-ext", "ContractAwardCategory");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeAttributes(context);
        if (_ContractAwardCategory instanceof javax.xml.bind.Element) {
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        super.serializeURIs(context);
        if (_ContractAwardCategory instanceof javax.xml.bind.Element) {
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _ContractAwardCategory), "ContractAwardCategory");
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.arra.ContractReportHeaderType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006excep"
+"tq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000\'com"
+".sun.msv.datatype.xsd.MaxLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tmaxLengthx"
+"r\u00009com.sun.msv.datatype.xsd.DataTypeWithValueConstraintFacet"
+"\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckFlagL\u0000\bbaseTypet\u0000)Lc"
+"om/sun/msv/datatype/xsd/XSDatatypeImpl;L\u0000\fconcreteTypet\u0000\'Lco"
+"m/sun/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNamet\u0000\u0012Ljava/lan"
+"g/String;xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u0019L\u0000\btypeNameq\u0000~\u0000\u0019L\u0000\nwhiteSpacet\u0000.Lcom/"
+"sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000\u0019urn:us:gov:rec"
+"overyrr-extpsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceP"
+"rocessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000#com.sun.msv.datatype.xsd.StringTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.Bu"
+"iltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.Concr"
+"eteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001at\u0000 http://www.w3.org/2001/XMLSchemat"
+"\u0000\u0006stringq\u0000~\u0000 \u0001q\u0000~\u0000$t\u0000\tmaxLength\u0000\u0000\u0000\tsr\u00000com.sun.msv.grammar.E"
+"xpression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Bo"
+"olean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0"
+"t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xpt\u0000\u000estring-d"
+"erivedq\u0000~\u0000\u001dsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0001ppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000"
+"\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000\u0003q\u0000~\u0000+psq\u0000~\u0000\u0010ppsr\u0000\"com.sun.msv.dataty"
+"pe.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"q\u0000~\u0000%t\u0000\u0005QNamesr\u00005com.sun.ms"
+"v.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001f"
+"q\u0000~\u0000)sq\u0000~\u0000,q\u0000~\u00006q\u0000~\u0000%sr\u0000#com.sun.msv.grammar.SimpleNameClass"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u0019L\u0000\fnamespaceURIq\u0000~\u0000\u0019xr\u0000\u001dcom.sun."
+"msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.or"
+"g/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression$"
+"EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000*\u0001q\u0000~\u0000@sq\u0000~\u0000:t\u0000\u0012Prime"
+"RecipientDUNSq\u0000~\u0000\u001dsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010ppsq\u0000~\u0000\u0014q\u0000~\u0000\u001dpq\u0000~\u0000 "
+"\u0000\u0000q\u0000~\u0000$q\u0000~\u0000$q\u0000~\u0000\'\u0000\u0000\u00002q\u0000~\u0000)sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001dsq\u0000~\u0000/"
+"ppsq\u0000~\u00001q\u0000~\u0000+pq\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000@sq\u0000~\u0000:t\u0000\rAwardIdNumberq\u0000~\u0000\u001dsq\u0000~"
+"\u0000/ppsq\u0000~\u0000\u000bq\u0000~\u0000+p\u0000sq\u0000~\u0000/ppsq\u0000~\u00001ppsr\u0000\u001ccom.sun.msv.grammar.Val"
+"ueExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtq\u0000~\u0000\u0011L\u0000\u0004nameq\u0000~\u0000\u0012L\u0000\u0005valuet\u0000\u0012Ljava/lang/"
+"Object;xq\u0000~\u0000\u0003ppsr\u0000$com.sun.msv.datatype.xsd.BooleanType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\"q\u0000~\u0000%t\u0000\u0007booleanq\u0000~\u00008sq\u0000~\u0000,q\u0000~\u0000Wt\u0000\u0000q\u0000~\u0000Asq\u0000~\u0000:t\u0000\u0003"
+"nilq\u0000~\u0000>sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0010q\u0000~\u0000+psq\u0000~\u0000\u0014q\u0000~\u0000\u001dpq\u0000~\u0000 \u0000\u0000q\u0000~\u0000$q\u0000~\u0000$q\u0000~"
+"\u0000\'\u0000\u0000\u00002q\u0000~\u0000)sq\u0000~\u0000,t\u0000\u000estring-derivedq\u0000~\u0000\u001dsq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u0000+pq"
+"\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000@sq\u0000~\u0000:t\u0000\u000bOrderNumberq\u0000~\u0000\u001dq\u0000~\u0000@sq\u0000~\u0000/ppsq\u0000~\u0000\u000bpp"
+"\u0000sq\u0000~\u0000/ppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001c"
+"com.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000"
+"+psq\u0000~\u00001q\u0000~\u0000+psr\u00002com.sun.msv.grammar.Expression$AnyStringEx"
+"pression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u0000Aq\u0000~\u0000msr\u0000 com.sun.msv.grammar.A"
+"nyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000;q\u0000~\u0000@sq\u0000~\u0000:t\u00007edu.mit.coeus.util"
+"s.xml.bean.arra.ContractAwardCategoryt\u0000+http://java.sun.com/"
+"jaxb/xjc/dummy-elementssq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000/ppsq\u0000"
+"~\u0000hq\u0000~\u0000+psq\u0000~\u00001q\u0000~\u0000+pq\u0000~\u0000mq\u0000~\u0000oq\u0000~\u0000@sq\u0000~\u0000:t\u00003edu.mit.coeus.u"
+"tils.xml.bean.arra.AwardCategoryTypeq\u0000~\u0000rsq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u0000+"
+"pq\u0000~\u00003q\u0000~\u0000<q\u0000~\u0000@sq\u0000~\u0000:t\u0000\u0015ContractAwardCategoryq\u0000~\u0000\u001dsq\u0000~\u0000/pps"
+"q\u0000~\u00001q\u0000~\u0000+psq\u0000~\u0000\u0010ppsr\u0000\u001fcom.sun.msv.datatype.xsd.IDType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.NcnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\"c"
+"om.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000!q\u0000~\u0000%t\u0000\u0002ID"
+"q\u0000~\u00008\u0000q\u0000~\u0000)sq\u0000~\u0000,q\u0000~\u0000\u0086q\u0000~\u0000%sq\u0000~\u0000:t\u0000\u0002idt\u0000#http://niem.gov/nie"
+"m/structures/2.0q\u0000~\u0000@sq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u0000+psq\u0000~\u0000\u0010ppsr\u0000*com.sun"
+".msv.datatype.xsd.DatatypeFactory$1\u00a1\u00f3\u000b\u00e3`rj\u000e\u0002\u0000\u0000xr\u0000\u001ecom.sun.ms"
+"v.datatype.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypeq\u0000~\u0000\u0017xq\u0000~\u0000\u001aq\u0000~\u0000%t\u0000"
+"\u0006IDREFSq\u0000~\u00008sr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\tminLengthxq\u0000~\u0000\u0015ppq\u0000~\u00008\u0000\u0000sr\u0000!com.sun.msv.datatype.xs"
+"d.ListType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bitemTypeq\u0000~\u0000\u0017xq\u0000~\u0000#ppq\u0000~\u00008sr\u0000\"com.su"
+"n.msv.datatype.xsd.IDREFType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0083q\u0000~\u0000%t\u0000\u0005IDREFq\u0000"
+"~\u00008\u0000q\u0000~\u0000\u0095t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000)psq\u0000~\u0000:t\u0000\flinkMetadataq\u0000~\u0000\u008aq\u0000~"
+"\u0000@sq\u0000~\u0000/ppsq\u0000~\u00001q\u0000~\u0000+pq\u0000~\u0000\u008dsq\u0000~\u0000:t\u0000\bmetadataq\u0000~\u0000\u008aq\u0000~\u0000@sr\u0000\"co"
+"m.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lco"
+"m/sun/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.ms"
+"v.grammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstr"
+"eamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp"
+"\u0000\u0000\u0000\u0018\u0001pq\u0000~\u0000eq\u0000~\u0000\\q\u0000~\u0000tq\u0000~\u0000\u0007q\u0000~\u00000q\u0000~\u0000Jq\u0000~\u0000aq\u0000~\u0000{q\u0000~\u0000jq\u0000~\u0000wq\u0000~\u0000"
+"\u0005q\u0000~\u0000\bq\u0000~\u0000\u000fq\u0000~\u0000\u009cq\u0000~\u0000Pq\u0000~\u0000Nq\u0000~\u0000\tq\u0000~\u0000Eq\u0000~\u0000\u008bq\u0000~\u0000\nq\u0000~\u0000gq\u0000~\u0000vq\u0000~\u0000"
+"\u007fq\u0000~\u0000\u0006x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.arra.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
            super(context, "----------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  8 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        break;
                    case  7 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        if (("PrimeRecipientDUNS" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        spawnHandlerFromEnterElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname, __atts);
                        return ;
                    case  6 :
                        if (("ContractAwardCategory" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            _ContractAwardCategory = ((edu.mit.coeus.utils.xml.bean.arra.impl.ContractAwardCategoryImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.arra.impl.ContractAwardCategoryImpl.class), 7, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("ContractAwardCategory" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 8;
                            return ;
                        }
                        break;
                    case  2 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 5;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  1 :
                        if (("OrderNumber" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 2;
                            return ;
                        }
                        state = 6;
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
                _OrderNumber = null;
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
                    case  9 :
                        if (("ContractAwardCategory" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.popAttributes();
                            state = 7;
                            return ;
                        }
                        break;
                    case  8 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  7 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveElement((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("OrderNumber" == ___local)&&("urn:us:gov:recoveryrr-ext" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  2 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 5;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  1 :
                        state = 6;
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
                    case  8 :
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            _ContractAwardCategory = ((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            _ContractAwardCategory = ((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            _ContractAwardCategory = ((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl.class), 9, ___uri, ___local, ___qname));
                            return ;
                        }
                        break;
                    case  7 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                            return ;
                        }
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromEnterAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  1 :
                        state = 6;
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
                    case  8 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        break;
                    case  4 :
                        if (("nil" == ___local)&&("http://www.w3.org/2001/XMLSchema-instance" == ___uri)) {
                            state = 5;
                            return ;
                        }
                        break;
                    case  7 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        spawnHandlerFromLeaveAttribute((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, ___uri, ___local, ___qname);
                        return ;
                    case  2 :
                        attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 5;
                            eatText1(v);
                            continue outer;
                        }
                        break;
                    case  1 :
                        state = 6;
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
                        case  8 :
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            _ContractAwardCategory = ((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.arra.impl.AwardCategoryTypeImpl.class), 9, value));
                            return ;
                        case  7 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            spawnHandlerFromText((((edu.mit.coeus.utils.xml.bean.arra.impl.ReportHeaderTypeImpl)edu.mit.coeus.utils.xml.bean.arra.impl.ContractReportHeaderTypeImpl.this).new Unmarshaller(context)), 1, value);
                            return ;
                        case  3 :
                            state = 4;
                            eatText1(value);
                            return ;
                        case  2 :
                            attIdx = context.getAttribute("http://www.w3.org/2001/XMLSchema-instance", "nil");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 5;
                                eatText1(v);
                                continue outer;
                            }
                            state = 5;
                            eatText2(value);
                            return ;
                        case  1 :
                            state = 6;
                            continue outer;
                    }
                } catch (java.lang.RuntimeException e) {
                    handleUnexpectedTextException(value, e);
                }
                break;
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _OrderNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}