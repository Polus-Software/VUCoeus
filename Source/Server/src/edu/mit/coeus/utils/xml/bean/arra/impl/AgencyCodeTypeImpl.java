//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.18 at 01:32:49 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.arra.impl;

public class AgencyCodeTypeImpl implements edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializable, com.sun.xml.bind.marshaller.IdentifiableObject, edu.mit.coeus.utils.xml.bean.arra.impl.runtime.ValidatableObject
{

    protected java.lang.String _Value;
    protected java.lang.String _Id;
    protected com.sun.xml.bind.util.ListImpl _Metadata;
    protected com.sun.xml.bind.util.ListImpl _LinkMetadata;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.arra.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType.class);
    }

    public java.lang.String getValue() {
        return _Value;
    }

    public void setValue(java.lang.String value) {
        _Value = value;
    }

    public java.lang.String getId() {
        return _Id;
    }

    public void setId(java.lang.String value) {
        _Id = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getMetadata() {
        if (_Metadata == null) {
            _Metadata = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Metadata;
    }

    public java.util.List getMetadata() {
        return _getMetadata();
    }

    protected com.sun.xml.bind.util.ListImpl _getLinkMetadata() {
        if (_LinkMetadata == null) {
            _LinkMetadata = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _LinkMetadata;
    }

    public java.util.List getLinkMetadata() {
        return _getLinkMetadata();
    }

    public edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.arra.impl.AgencyCodeTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_Metadata == null)? 0 :_Metadata.size());
        int idx4 = 0;
        final int len4 = ((_LinkMetadata == null)? 0 :_LinkMetadata.size());
        try {
            context.text(((java.lang.String) _Value), "Value");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_Metadata == null)? 0 :_Metadata.size());
        int idx4 = 0;
        final int len4 = ((_LinkMetadata == null)? 0 :_LinkMetadata.size());
        if (_Id!= null) {
            context.startAttribute("http://niem.gov/niem/structures/2.0", "id");
            try {
                context.text(context.onID(this, ((java.lang.String) _Id)), "Id");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endAttribute();
        }
        if (((_LinkMetadata == null)? 0 :_LinkMetadata.size())>= 1) {
            context.startAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
            while (idx4 != len4) {
                try {
                    context.text(context.onIDREF(((com.sun.xml.bind.marshaller.IdentifiableObject)((java.lang.Object) _LinkMetadata.get(idx4 ++)))), "LinkMetadata");
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
            }
            context.endAttribute();
        }
        if (((_Metadata == null)? 0 :_Metadata.size())>= 1) {
            context.startAttribute("http://niem.gov/niem/structures/2.0", "metadata");
            while (idx3 != len3) {
                try {
                    context.text(context.onIDREF(((com.sun.xml.bind.marshaller.IdentifiableObject)((java.lang.Object) _Metadata.get(idx3 ++)))), "Metadata");
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
            }
            context.endAttribute();
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx3 = 0;
        final int len3 = ((_Metadata == null)? 0 :_Metadata.size());
        int idx4 = 0;
        final int len4 = ((_LinkMetadata == null)? 0 :_LinkMetadata.size());
        if (_Id!= null) {
            context.getNamespaceContext().declareNamespace("http://niem.gov/niem/structures/2.0", null, true);
        }
        if (((_LinkMetadata == null)? 0 :_LinkMetadata.size())>= 1) {
            context.getNamespaceContext().declareNamespace("http://niem.gov/niem/structures/2.0", null, true);
            while (idx4 != len4) {
                try {
                    idx4 += 1;
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
            }
        }
        if (((_Metadata == null)? 0 :_Metadata.size())>= 1) {
            context.getNamespaceContext().declareNamespace("http://niem.gov/niem/structures/2.0", null, true);
            while (idx3 != len3) {
                try {
                    idx3 += 1;
                } catch (java.lang.Exception e) {
                    edu.mit.coeus.utils.xml.bean.arra.impl.runtime.Util.handlePrintConversionException(this, e, context);
                }
            }
        }
    }

    public java.lang.String ____jaxb____getId() {
        return ((java.lang.String) _Id);
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.arra.AgencyCodeType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar."
+"DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006"
+"exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr"
+"\u0000)com.sun.msv.datatype.xsd.EnumerationFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0006val"
+"uest\u0000\u000fLjava/util/Set;xr\u00009com.sun.msv.datatype.xsd.DataTypeWi"
+"thValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xs"
+"d.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueChe"
+"ckFlagL\u0000\bbaseTypet\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl"
+";L\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/xsd/ConcreteType;L\u0000"
+"\tfacetNamet\u0000\u0012Ljava/lang/String;xr\u0000\'com.sun.msv.datatype.xsd."
+"XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUriq\u0000~\u0000\u0012L\u0000\btypeNameq\u0000~\u0000"
+"\u0012L\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcess"
+"or;xpt\u0000\u001burn:us:gov:recoveryrr-facett\u0000\u0014AgencyCodeSimpleTypesr"
+"\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xp\u0000\u0000sr\u0000\"com.sun.msv.datatype.xsd.TokenType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr"
+"\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysV"
+"alidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0013t"
+"\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0005tokenq\u0000~\u0000\u001a\u0001q\u0000~\u0000\u001ft\u0000\u000benum"
+"erationsr\u0000\u0011java.util.HashSet\u00baD\u0085\u0095\u0096\u00b8\u00b74\u0003\u0000\u0000xpw\f\u0000\u0000\u0001\u0000?@\u0000\u0000\u0000\u0000\u0000{t\u0000\u000489"
+"28t\u0000\u00048925t\u0000\u00041727t\u0000\u000412H2t\u0000\u000419BMt\u0000\u00041621t\u0000\u00049146t\u0000\u00047545t\u0000\u00048600t\u0000"
+"\u00041422t\u0000\u000489NAt\u0000\u00041425t\u0000\u00041900t\u0000\u00041325t\u0000\u00041904t\u0000\u00041323t\u0000\u00041330t\u0000\u000489N"
+"4t\u0000\u00049150t\u0000\u000496CEt\u0000\u000489N1t\u0000\u00047200t\u0000\u000489N6t\u0000\u00046800t\u0000\u00041434t\u0000\u00046804t\u0000\u0004"
+"574Zt\u0000\u00049577t\u0000\u00048653t\u0000\u000412F2t\u0000\u00041504t\u0000\u00047523t\u0000\u00046000t\u0000\u00041700t\u0000\u000497EX"
+"t\u0000\u00047528t\u0000\u00041448t\u0000\u00047529t\u0000\u00041467t\u0000\u00047526t\u0000\u00047527t\u0000\u00041443t\u0000\u00040500t\u0000\u00042"
+"100t\u0000\u00041772t\u0000\u00041304t\u0000\u00047051t\u0000\u00049760t\u0000\u00046955t\u0000\u00041635t\u0000\u000421GBt\u0000\u00041450t"
+"\u0000\u00047530t\u0000\u00041630t\u0000\u00042001t\u0000\u00047022t\u0000\u00043640t\u0000\u00042050t\u0000\u000412D3t\u0000\u00042700t\u0000\u000486"
+"35t\u0000\u00047590t\u0000\u00043620t\u0000\u00047300t\u0000\u00041930t\u0000\u00049700t\u0000\u00049104t\u0000\u00046920t\u0000\u00043630t\u0000"
+"\u000412D2t\u0000\u00046925t\u0000\u00041560t\u0000\u00042800t\u0000\u00041341t\u0000\u00043300t\u0000\u000421HRt\u0000\u00043651t\u0000\u000412E"
+"4t\u0000\u00047505t\u0000\u00045700t\u0000\u00044745t\u0000\u00045920t\u0000\u00042804t\u0000\u0004570Mt\u0000\u00041650t\u0000\u00046938t\u0000\u0004"
+"1335t\u0000\u000412E2t\u0000\u000412E3t\u0000\u000412E0t\u0000\u00046930t\u0000\u00047004t\u0000\u00041550t\u0000\u00044704t\u0000\u00043604"
+"t\u0000\u00041605t\u0000\u00044732t\u0000\u00044700t\u0000\u00043600t\u0000\u00048620t\u0000\u00048900t\u0000\u00044900t\u0000\u00049124t\u0000\u00048"
+"98Pt\u0000\u00046904t\u0000\u00047560t\u0000\u00047014t\u0000\u000412C3t\u0000\u000412C2t\u0000\u00047012t\u0000\u00047013t\u0000\u00041403t"
+"\u0000\u00041404t\u0000\u00048604t\u0000\u00041400t\u0000\u00048000t\u0000\u00041204t\u0000\u00049134t\u0000\u00049139t\u0000\u00041201t\u0000\u000469"
+"0St\u0000\u00049131t\u0000\u00047008xsr\u00000com.sun.msv.grammar.Expression$NullSetE"
+"xpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000"
+"\u0005valuexp\u0000psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocal"
+"Nameq\u0000~\u0000\u0012L\u0000\fnamespaceURIq\u0000~\u0000\u0012xpq\u0000~\u0000\u0017q\u0000~\u0000\u0016sr\u0000\u001dcom.sun.msv.gra"
+"mmar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Att"
+"ributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/g"
+"rammar/NameClass;xq\u0000~\u0000\u0003q\u0000~\u0000\u00a3psq\u0000~\u0000\bppsr\u0000\u001fcom.sun.msv.datatyp"
+"e.xsd.IDType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.NcnameTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000 t\u0000\u0002IDq\u0000~\u0000\u001a\u0000q\u0000~\u0000\u00a1sq\u0000~\u0000\u00a4q\u0000~\u0000\u00afq\u0000~\u0000 sr\u0000#"
+"com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000"
+"~\u0000\u0012L\u0000\fnamespaceURIq\u0000~\u0000\u0012xr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0002idt\u0000#http://niem.gov/niem/structures/2.0sr\u00000com."
+"sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003sq\u0000~\u0000\u00a2\u0001q\u0000~\u0000\u00b7sq\u0000~\u0000\u00a6ppsq\u0000~\u0000\u00a8q\u0000~\u0000\u00a3psq\u0000~\u0000\bppsr\u0000*com.sun.msv.dat"
+"atype.xsd.DatatypeFactory$1\u00a1\u00f3\u000b\u00e3`rj\u000e\u0002\u0000\u0000xr\u0000\u001ecom.sun.msv.dataty"
+"pe.xsd.Proxy\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bbaseTypeq\u0000~\u0000\u0010xq\u0000~\u0000\u0013q\u0000~\u0000 t\u0000\u0006IDREFSq"
+"\u0000~\u0000\u001asr\u0000\'com.sun.msv.datatype.xsd.MinLengthFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000"
+"\tminLengthxq\u0000~\u0000\u000eppq\u0000~\u0000\u001a\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.ListTy"
+"pe\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bitemTypeq\u0000~\u0000\u0010xq\u0000~\u0000\u001eppq\u0000~\u0000\u001asr\u0000\"com.sun.msv.da"
+"tatype.xsd.IDREFType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u00adq\u0000~\u0000 t\u0000\u0005IDREFq\u0000~\u0000\u001a\u0000q\u0000~\u0000"
+"\u00c3t\u0000\tminLength\u0000\u0000\u0000\u0001q\u0000~\u0000\u00a1psq\u0000~\u0000\u00b1t\u0000\flinkMetadataq\u0000~\u0000\u00b5q\u0000~\u0000\u00b7sq\u0000~\u0000\u00a6"
+"ppsq\u0000~\u0000\u00a8q\u0000~\u0000\u00a3pq\u0000~\u0000\u00bbsq\u0000~\u0000\u00b1t\u0000\bmetadataq\u0000~\u0000\u00b5q\u0000~\u0000\u00b7sr\u0000\"com.sun.ms"
+"v.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/ms"
+"v/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gramma"
+"r.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersi"
+"onL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0006\u0001pq\u0000"
+"~\u0000\u00caq\u0000~\u0000\u0005q\u0000~\u0000\u0007q\u0000~\u0000\u00b9q\u0000~\u0000\u0006q\u0000~\u0000\u00a7x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.arra.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context) {
            super(context, "----L--L---");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.arra.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.arra.impl.AgencyCodeTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  10 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().enterElement(___uri, ___local, ___qname, __atts);
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
                _Id = context.addToIdTable(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
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
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  10 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveElement(___uri, ___local, ___qname);
                            return ;
                        }
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
                    case  0 :
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  10 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 4;
                            return ;
                        }
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
                    case  0 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                        if (attIdx >= 0) {
                            final java.lang.String v = context.eatAttribute(attIdx);
                            state = 3;
                            eatText1(v);
                            continue outer;
                        }
                        state = 3;
                        continue outer;
                    case  6 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  10 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  3 :
                        attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                        if (attIdx >= 0) {
                            context.consumeAttribute(attIdx);
                            context.getCurrentHandler().leaveAttribute(___uri, ___local, ___qname);
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  8 :
                        if (("metadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("id" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 3;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("linkMetadata" == ___local)&&("http://niem.gov/niem/structures/2.0" == ___uri)) {
                            state = 6;
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
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  0 :
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "id");
                            if (attIdx >= 0) {
                                final java.lang.String v = context.eatAttribute(attIdx);
                                state = 3;
                                eatText1(v);
                                continue outer;
                            }
                            state = 3;
                            continue outer;
                        case  6 :
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "metadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            state = 9;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText3(value);
                            return ;
                        case  10 :
                            revertToParentFromText(value);
                            return ;
                        case  9 :
                            state = 10;
                            eatText4(value);
                            return ;
                        case  3 :
                            attIdx = context.getAttribute("http://niem.gov/niem/structures/2.0", "linkMetadata");
                            if (attIdx >= 0) {
                                context.consumeAttribute(attIdx);
                                context.getCurrentHandler().text(value);
                                return ;
                            }
                            state = 6;
                            continue outer;
                        case  8 :
                            state = 8;
                            eatText3(value);
                            return ;
                        case  5 :
                            state = 5;
                            eatText2(value);
                            return ;
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
                context.addPatcher(new java.lang.Runnable() {


                    public void run() {
                        _getLinkMetadata().add(((java.lang.Object) context.getObjectFromId(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value))));
                    }

                }
                );
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                context.addPatcher(new java.lang.Runnable() {


                    public void run() {
                        _getMetadata().add(((java.lang.Object) context.getObjectFromId(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value))));
                    }

                }
                );
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Value = com.sun.xml.bind.WhiteSpaceProcessor.collapse(value);
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
