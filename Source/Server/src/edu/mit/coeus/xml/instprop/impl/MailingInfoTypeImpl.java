//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.27 at 09:21:38 AM IST 
//


package edu.mit.coeus.xml.instprop.impl;

public class MailingInfoTypeImpl implements edu.mit.coeus.xml.instprop.MailingInfoType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallableObject, edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializable, edu.mit.coeus.xml.instprop.impl.runtime.ValidatableObject
{

    protected java.lang.String _MailAccount;
    protected java.lang.String _MailType;
    protected java.util.Calendar _DeadlineDate;
    protected java.lang.String _Comments;
    protected edu.mit.coeus.xml.instprop.PersonType _MailToPerson;
    protected java.lang.String _MailByOSP;
    protected boolean has_NumberCopies;
    protected int _NumberCopies;
    protected java.lang.String _DeadlineType;
    public final static java.lang.Class version = (edu.mit.coeus.xml.instprop.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.xml.instprop.MailingInfoType.class);
    }

    public java.lang.String getMailAccount() {
        return _MailAccount;
    }

    public void setMailAccount(java.lang.String value) {
        _MailAccount = value;
    }

    public java.lang.String getMailType() {
        return _MailType;
    }

    public void setMailType(java.lang.String value) {
        _MailType = value;
    }

    public java.util.Calendar getDeadlineDate() {
        return _DeadlineDate;
    }

    public void setDeadlineDate(java.util.Calendar value) {
        _DeadlineDate = value;
    }

    public java.lang.String getComments() {
        return _Comments;
    }

    public void setComments(java.lang.String value) {
        _Comments = value;
    }

    public edu.mit.coeus.xml.instprop.PersonType getMailToPerson() {
        return _MailToPerson;
    }

    public void setMailToPerson(edu.mit.coeus.xml.instprop.PersonType value) {
        _MailToPerson = value;
    }

    public java.lang.String getMailByOSP() {
        return _MailByOSP;
    }

    public void setMailByOSP(java.lang.String value) {
        _MailByOSP = value;
    }

    public int getNumberCopies() {
        return _NumberCopies;
    }

    public void setNumberCopies(int value) {
        _NumberCopies = value;
        has_NumberCopies = true;
    }

    public java.lang.String getDeadlineType() {
        return _DeadlineType;
    }

    public void setDeadlineType(java.lang.String value) {
        _DeadlineType = value;
    }

    public edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.xml.instprop.impl.MailingInfoTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_DeadlineDate!= null) {
            context.startElement("", "deadlineDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _DeadlineDate)), "DeadlineDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_DeadlineType!= null) {
            context.startElement("", "deadlineType");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DeadlineType), "DeadlineType");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_MailByOSP!= null) {
            context.startElement("", "mailByOSP");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _MailByOSP), "MailByOSP");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_MailType!= null) {
            context.startElement("", "mailType");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _MailType), "MailType");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_MailAccount!= null) {
            context.startElement("", "mailAccount");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _MailAccount), "MailAccount");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (has_NumberCopies) {
            context.startElement("", "numberCopies");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _NumberCopies)), "NumberCopies");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_MailToPerson!= null) {
            context.startElement("", "mailToPerson");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _MailToPerson), "MailToPerson");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _MailToPerson), "MailToPerson");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _MailToPerson), "MailToPerson");
            context.endElement();
        }
        if (_Comments!= null) {
            context.startElement("", "comments");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Comments), "Comments");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.xml.instprop.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.xml.instprop.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.xml.instprop.MailingInfoType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001pp"
+"sr\u0000\'com.sun.msv.grammar.trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnam"
+"eClasst\u0000\u001fLcom/sun/msv/grammar/NameClass;xr\u0000\u001ecom.sun.msv.gram"
+"mar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcon"
+"tentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005value"
+"xp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dt"
+"t\u0000\u001fLorg/relaxng/datatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLc"
+"om/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000!com.sun.msv.datatype."
+"xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTime"
+"BaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomi"
+"cType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001eL\u0000\nwhi"
+"teSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000"
+" http://www.w3.org/2001/XMLSchemat\u0000\u0004datesr\u00005com.sun.msv.data"
+"type.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun."
+"msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u00000com.sun"
+".msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003pp"
+"sr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL"
+"\u0000\fnamespaceURIq\u0000~\u0000\u001expq\u0000~\u0000\"q\u0000~\u0000!sq\u0000~\u0000\fppsr\u0000 com.sun.msv.gramm"
+"ar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000fxq\u0000~\u0000\u0003"
+"q\u0000~\u0000\u0013psq\u0000~\u0000\u0015ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001"
+"\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0005QNameq\u0000~\u0000%q\u0000~\u0000\'sq\u0000~\u0000(q\u0000~\u00000q\u0000~\u0000!sr\u0000#com.sun."
+"msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001eL\u0000\fna"
+"mespaceURIq\u0000~\u0000\u001exr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"pt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instancesr\u00000com."
+"sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u00008sq\u0000~\u00002t\u0000\fdeadlineDatet\u0000\u0000q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000"
+"\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0015ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0006stringsr\u00005com.sun.msv"
+".datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000$\u0001"
+"q\u0000~\u0000\'sq\u0000~\u0000(q\u0000~\u0000Cq\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~"
+"\u00002t\u0000\fdeadlineTypeq\u0000~\u0000<q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000"
+"@sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\tmailByOSPq\u0000~\u0000<q"
+"\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000@sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000"
+"~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\bmailTypeq\u0000~\u0000<q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p"
+"\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000@sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\u000bma"
+"ilAccountq\u0000~\u0000<q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0015ppsr\u0000 "
+"com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.d"
+"atatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lco"
+"m/sun/msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0003intq\u0000~\u0000%"
+"sr\u0000*com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000"
+"#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValue"
+"t\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWi"
+"thValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xs"
+"d.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueChe"
+"ckFlagL\u0000\bbaseTypeq\u0000~\u0000fL\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatyp"
+"e/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001exq\u0000~\u0000\u001dppq\u0000~\u0000%\u0000\u0001sr\u0000*com.s"
+"un.msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000jppq\u0000~\u0000"
+"%\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000eq\u0000~"
+"\u0000!t\u0000\u0004longq\u0000~\u0000%sq\u0000~\u0000ippq\u0000~\u0000%\u0000\u0001sq\u0000~\u0000pppq\u0000~\u0000%\u0000\u0000sr\u0000$com.sun.msv."
+"datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000eq\u0000~\u0000!t\u0000\u0007integerq\u0000~\u0000"
+"%sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001"
+"I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalCons"
+"traintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000mppq\u0000~\u0000%\u0001\u0000sr\u0000#com.sun.msv.datatyp"
+"e.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001bq\u0000~\u0000!t\u0000\u0007decimalq\u0000~\u0000%q\u0000~\u0000~t\u0000"
+"\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000xt\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4"
+"\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~"
+"\u0000xt\u0000\fmaxInclusivesq\u0000~\u0000\u0082\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000sq\u0000~\u0000\u0081sr\u0000\u0011java.lang.Integ"
+"er\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000\u0083\u0080\u0000\u0000\u0000q\u0000~\u0000sq\u0000~\u0000\u0085sq\u0000~\u0000\u0087\u007f\u00ff\u00ff\u00ffq\u0000~\u0000\'sq\u0000~"
+"\u0000(q\u0000~\u0000hq\u0000~\u0000!sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\fnumb"
+"erCopiesq\u0000~\u0000<q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000epp\u0000sq\u0000~"
+"\u0000\fppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.s"
+"un.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0013psq\u0000"
+"~\u0000+q\u0000~\u0000\u0013psr\u00002com.sun.msv.grammar.Expression$AnyStringExpress"
+"ion\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00009q\u0000~\u0000\u0099sr\u0000 com.sun.msv.grammar.AnyNam"
+"eClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00003q\u0000~\u00008sq\u0000~\u00002t\u0000%edu.mit.coeus.xml.instp"
+"rop.PersonTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elements"
+"sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013pq\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\fmailToPersonq\u0000~\u0000"
+"<q\u0000~\u00008sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000@sq\u0000~\u0000\fppsq\u0000~\u0000+q\u0000~\u0000\u0013p"
+"q\u0000~\u0000-q\u0000~\u00004q\u0000~\u00008sq\u0000~\u00002t\u0000\bcommentsq\u0000~\u0000<q\u0000~\u00008sr\u0000\"com.sun.msv.gr"
+"ammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gr"
+"ammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Ex"
+"pressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000"
+"\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000!\u0001pq\u0000~\u0000\u0014q"
+"\u0000~\u0000\nq\u0000~\u0000\u0091q\u0000~\u0000=q\u0000~\u0000Kq\u0000~\u0000Rq\u0000~\u0000Yq\u0000~\u0000?q\u0000~\u0000Mq\u0000~\u0000Tq\u0000~\u0000[q\u0000~\u0000\u00a5q\u0000~\u0000\u00a3q"
+"\u0000~\u0000*q\u0000~\u0000Gq\u0000~\u0000Nq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000\u008bq\u0000~\u0000\u009fq\u0000~\u0000\u00a6q\u0000~\u0000\rq\u0000~\u0000\u0007q\u0000~\u0000bq\u0000~\u0000\u008fq"
+"\u0000~\u0000\bq\u0000~\u0000\u0096q\u0000~\u0000\u000bq\u0000~\u0000\u0093q\u0000~\u0000\tq\u0000~\u0000\u0005q\u0000~\u0000\u0006q\u0000~\u0000`x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.xml.instprop.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.xml.instprop.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.xml.instprop.impl.MailingInfoTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("deadlineType" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  21 :
                        if (("comments" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  9 :
                        if (("mailType" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  0 :
                        if (("deadlineDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  19 :
                        if (("lastName" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("firstName" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("middleName" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("fullName" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("phone" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("address" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("City" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("state" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("zip" == ___local)&&("" == ___uri)) {
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  12 :
                        if (("mailAccount" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  18 :
                        if (("mailToPerson" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  6 :
                        if (("mailByOSP" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  24 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  15 :
                        if (("numberCopies" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
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
                        state = 6;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  5 :
                        if (("deadlineType" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("numberCopies" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  19 :
                        _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname));
                        return ;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  8 :
                        if (("mailByOSP" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  20 :
                        if (("mailToPerson" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
                            return ;
                        }
                        break;
                    case  14 :
                        if (("mailAccount" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  11 :
                        if (("mailType" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("deadlineDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  23 :
                        if (("comments" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
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
                    case  21 :
                        state = 24;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  19 :
                        _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname));
                        return ;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  24 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  19 :
                        _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, ___uri, ___local, ___qname));
                        return ;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  24 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  15 :
                        state = 18;
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
                        case  3 :
                            state = 6;
                            continue outer;
                        case  16 :
                            state = 17;
                            eatText1(value);
                            return ;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  19 :
                            _MailToPerson = ((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl) spawnChildFromText((edu.mit.coeus.xml.instprop.impl.PersonTypeImpl.class), 20, value));
                            return ;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText2(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText3(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  13 :
                            state = 14;
                            eatText4(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText5(value);
                            return ;
                        case  22 :
                            state = 23;
                            eatText6(value);
                            return ;
                        case  24 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText7(value);
                            return ;
                        case  15 :
                            state = 18;
                            continue outer;
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
                _NumberCopies = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_NumberCopies = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DeadlineDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DeadlineType = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MailAccount = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MailByOSP = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Comments = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _MailType = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}