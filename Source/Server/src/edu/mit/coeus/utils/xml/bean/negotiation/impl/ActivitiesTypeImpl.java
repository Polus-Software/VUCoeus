//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.24 at 12:07:46 EDT 
//


package edu.mit.coeus.utils.xml.bean.negotiation.impl;

public class ActivitiesTypeImpl implements edu.mit.coeus.utils.xml.bean.negotiation.ActivitiesType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.impl.runtime.ValidatableObject
{

    protected edu.mit.coeus.utils.xml.bean.negotiation.ActivityType _Activity;
    protected java.util.Calendar _ActivityDate;
    protected java.lang.String _Description;
    protected java.lang.String _UpdatedBy;
    protected java.lang.String _DocFileAddress;
    protected java.util.Calendar _LastDate;
    protected java.util.Calendar _FollowupDate;
    protected java.util.Calendar _CreateDate;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.negotiation.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.negotiation.ActivitiesType.class);
    }

    public edu.mit.coeus.utils.xml.bean.negotiation.ActivityType getActivity() {
        return _Activity;
    }

    public void setActivity(edu.mit.coeus.utils.xml.bean.negotiation.ActivityType value) {
        _Activity = value;
    }

    public java.util.Calendar getActivityDate() {
        return _ActivityDate;
    }

    public void setActivityDate(java.util.Calendar value) {
        _ActivityDate = value;
    }

    public java.lang.String getDescription() {
        return _Description;
    }

    public void setDescription(java.lang.String value) {
        _Description = value;
    }

    public java.lang.String getUpdatedBy() {
        return _UpdatedBy;
    }

    public void setUpdatedBy(java.lang.String value) {
        _UpdatedBy = value;
    }

    public java.lang.String getDocFileAddress() {
        return _DocFileAddress;
    }

    public void setDocFileAddress(java.lang.String value) {
        _DocFileAddress = value;
    }

    public java.util.Calendar getLastDate() {
        return _LastDate;
    }

    public void setLastDate(java.util.Calendar value) {
        _LastDate = value;
    }

    public java.util.Calendar getFollowupDate() {
        return _FollowupDate;
    }

    public void setFollowupDate(java.util.Calendar value) {
        _FollowupDate = value;
    }

    public java.util.Calendar getCreateDate() {
        return _CreateDate;
    }

    public void setCreateDate(java.util.Calendar value) {
        _CreateDate = value;
    }

    public edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivitiesTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (_DocFileAddress!= null) {
            context.startElement("", "docFileAddress");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _DocFileAddress), "DocFileAddress");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Description!= null) {
            context.startElement("", "description");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Description), "Description");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Activity!= null) {
            context.startElement("", "activity");
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Activity), "Activity");
            context.endNamespaceDecls();
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Activity), "Activity");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Activity), "Activity");
            context.endElement();
        }
        if (_UpdatedBy!= null) {
            context.startElement("", "updatedBy");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _UpdatedBy), "UpdatedBy");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_ActivityDate!= null) {
            context.startElement("", "activityDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _ActivityDate)), "ActivityDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_CreateDate!= null) {
            context.startElement("", "createDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _CreateDate)), "CreateDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_FollowupDate!= null) {
            context.startElement("", "followupDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _FollowupDate)), "FollowupDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_LastDate!= null) {
            context.startElement("", "lastDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _LastDate)), "LastDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
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
        return (edu.mit.coeus.utils.xml.bean.negotiation.ActivitiesType.class);
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
+"om/sun/msv/util/StringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype."
+"xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.dat"
+"atype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.dataty"
+"pe.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.X"
+"SDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;"
+"L\u0000\btypeNameq\u0000~\u0000\u001dL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/Wh"
+"iteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006st"
+"ringsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Preserv"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcessor"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.sun.msv.grammar.Expression$NullSetExpr"
+"ession\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ej"
+"B\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespaceURIq\u0000~\u0000\u001dxpq\u0000~\u0000!q\u0000~\u0000 sq\u0000~"
+"\u0000\fppsr\u0000 com.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~"
+"\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000fxq\u0000~\u0000\u0003q\u0000~\u0000\u0013psq\u0000~\u0000\u0015ppsr\u0000\"com.sun.msv.datat"
+"ype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u001aq\u0000~\u0000 t\u0000\u0005QNamesr\u00005com.sun.m"
+"sv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000"
+"#q\u0000~\u0000&sq\u0000~\u0000\'q\u0000~\u0000/q\u0000~\u0000 sr\u0000#com.sun.msv.grammar.SimpleNameClas"
+"s\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001dL\u0000\fnamespaceURIq\u0000~\u0000\u001dxr\u0000\u001dcom.sun"
+".msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.o"
+"rg/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expression"
+"$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u0012\u0001q\u0000~\u00009sq\u0000~\u00003t\u0000\u000edocF"
+"ileAddresst\u0000\u0000q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fpp"
+"sq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\u000bdescriptionq\u0000~\u0000=q\u0000~\u00009sq\u0000"
+"~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\fppsr\u0000 com.sun.msv.g"
+"rammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryE"
+"xp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002xq\u0000~\u0000\u0003q\u0000~\u0000\u0013psq\u0000~\u0000*q\u0000~\u0000\u0013psr\u00002com.sun."
+"msv.grammar.Expression$AnyStringExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q"
+"\u0000~\u0000:q\u0000~\u0000Osr\u0000 com.sun.msv.grammar.AnyNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~"
+"\u00004q\u0000~\u00009sq\u0000~\u00003t\u00005edu.mit.coeus.utils.xml.bean.negotiation.Act"
+"ivityTypet\u0000+http://java.sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000"
+"\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\bactivityq\u0000~\u0000=q\u0000~\u00009sq\u0000"
+"~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0018sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u0000"
+"5q\u0000~\u00009sq\u0000~\u00003t\u0000\tupdatedByq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000"
+"\u0000ppsq\u0000~\u0000\u0015ppsr\u0000!com.sun.msv.datatype.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"r\u0000)com.sun.msv.datatype.xsd.DateTimeBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000"
+"\u001aq\u0000~\u0000 t\u0000\u0004dateq\u0000~\u00001q\u0000~\u0000&sq\u0000~\u0000\'q\u0000~\u0000gq\u0000~\u0000 sq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq"
+"\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\factivityDateq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq"
+"\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000csq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003"
+"t\u0000\ncreateDateq\u0000~\u0000=q\u0000~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000csq\u0000"
+"~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\ffollowupDateq\u0000~\u0000=q\u0000"
+"~\u00009sq\u0000~\u0000\fppsq\u0000~\u0000\u000eq\u0000~\u0000\u0013p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000csq\u0000~\u0000\fppsq\u0000~\u0000*q\u0000~\u0000\u0013pq\u0000~"
+"\u0000,q\u0000~\u00005q\u0000~\u00009sq\u0000~\u00003t\u0000\blastDateq\u0000~\u0000=q\u0000~\u00009sr\u0000\"com.sun.msv.gramm"
+"ar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/gramm"
+"ar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar.Expre"
+"ssionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006pa"
+"rentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000!\u0001pq\u0000~\u0000\nq\u0000~\u0000"
+"\u0005q\u0000~\u0000Lq\u0000~\u0000\bq\u0000~\u0000\u000bq\u0000~\u0000\u0014q\u0000~\u0000@q\u0000~\u0000[q\u0000~\u0000\u0007q\u0000~\u0000bq\u0000~\u0000oq\u0000~\u0000vq\u0000~\u0000}q\u0000~\u0000"
+"Iq\u0000~\u0000Gq\u0000~\u0000Eq\u0000~\u0000\rq\u0000~\u0000>q\u0000~\u0000Yq\u0000~\u0000\u0006q\u0000~\u0000`q\u0000~\u0000\tq\u0000~\u0000mq\u0000~\u0000tq\u0000~\u0000{q\u0000~\u0000"
+")q\u0000~\u0000Aq\u0000~\u0000Uq\u0000~\u0000\\q\u0000~\u0000iq\u0000~\u0000pq\u0000~\u0000wq\u0000~\u0000~x"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivitiesTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  21 :
                        if (("lastDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  3 :
                        if (("description" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  15 :
                        if (("createDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        state = 18;
                        continue outer;
                    case  18 :
                        if (("followupDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  12 :
                        if (("activityDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
                        continue outer;
                    case  7 :
                        if (("activityCode" == ___local)&&("" == ___uri)) {
                            _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        if (("activityDesc" == ___local)&&("" == ___uri)) {
                            _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                            return ;
                        }
                        _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname, __atts));
                        return ;
                    case  0 :
                        if (("docFileAddress" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  9 :
                        if (("updatedBy" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        if (("activity" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  24 :
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
                    case  21 :
                        state = 24;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  2 :
                        if (("docFileAddress" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  7 :
                        _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromLeaveElement((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  23 :
                        if (("lastDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("activity" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("updatedBy" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  5 :
                        if (("description" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  24 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  14 :
                        if (("activityDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  17 :
                        if (("createDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("followupDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 21;
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
                    case  21 :
                        state = 24;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  7 :
                        _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  24 :
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
                    case  21 :
                        state = 24;
                        continue outer;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        state = 18;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  12 :
                        state = 15;
                        continue outer;
                    case  7 :
                        _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, ___uri, ___local, ___qname));
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  24 :
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
                        case  21 :
                            state = 24;
                            continue outer;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  15 :
                            state = 18;
                            continue outer;
                        case  22 :
                            eatText1(value);
                            state = 23;
                            return ;
                        case  1 :
                            eatText2(value);
                            state = 2;
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  12 :
                            state = 15;
                            continue outer;
                        case  13 :
                            eatText3(value);
                            state = 14;
                            return ;
                        case  19 :
                            eatText4(value);
                            state = 20;
                            return ;
                        case  10 :
                            eatText5(value);
                            state = 11;
                            return ;
                        case  7 :
                            _Activity = ((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.negotiation.impl.ActivityTypeImpl.class), 8, value));
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  16 :
                            eatText6(value);
                            state = 17;
                            return ;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  4 :
                            eatText7(value);
                            state = 5;
                            return ;
                        case  24 :
                            revertToParentFromText(value);
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
                _LastDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _DocFileAddress = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ActivityDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _FollowupDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _UpdatedBy = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CreateDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Description = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
