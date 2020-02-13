//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.03 at 04:29:59 PM GMT+05:30 
//


package edu.mit.coeus.utils.xml.bean.budget.impl;

public class ReportHeaderTypeImpl implements edu.mit.coeus.utils.xml.bean.budget.ReportHeaderType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.ValidatableObject
{

    protected boolean has_Period;
    protected int _Period;
    protected java.lang.String _Comments;
    protected java.lang.String _ProposalNumber;
    protected java.lang.String _PeriodEndDate;
    protected java.lang.String _PeriodStartDate;
    protected java.lang.String _PIName;
    protected boolean has_BudgetVersion;
    protected int _BudgetVersion;
    protected java.lang.String _PrintBudgetComment;
    protected java.lang.String _ProposalTitle;
    protected java.lang.String _CreateDate;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.budget.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.budget.ReportHeaderType.class);
    }

    public int getPeriod() {
        return _Period;
    }

    public void setPeriod(int value) {
        _Period = value;
        has_Period = true;
    }

    public java.lang.String getComments() {
        return _Comments;
    }

    public void setComments(java.lang.String value) {
        _Comments = value;
    }

    public java.lang.String getProposalNumber() {
        return _ProposalNumber;
    }

    public void setProposalNumber(java.lang.String value) {
        _ProposalNumber = value;
    }

    public java.lang.String getPeriodEndDate() {
        return _PeriodEndDate;
    }

    public void setPeriodEndDate(java.lang.String value) {
        _PeriodEndDate = value;
    }

    public java.lang.String getPeriodStartDate() {
        return _PeriodStartDate;
    }

    public void setPeriodStartDate(java.lang.String value) {
        _PeriodStartDate = value;
    }

    public java.lang.String getPIName() {
        return _PIName;
    }

    public void setPIName(java.lang.String value) {
        _PIName = value;
    }

    public int getBudgetVersion() {
        return _BudgetVersion;
    }

    public void setBudgetVersion(int value) {
        _BudgetVersion = value;
        has_BudgetVersion = true;
    }

    public java.lang.String getPrintBudgetComment() {
        return _PrintBudgetComment;
    }

    public void setPrintBudgetComment(java.lang.String value) {
        _PrintBudgetComment = value;
    }

    public java.lang.String getProposalTitle() {
        return _ProposalTitle;
    }

    public void setProposalTitle(java.lang.String value) {
        _ProposalTitle = value;
    }

    public java.lang.String getCreateDate() {
        return _CreateDate;
    }

    public void setCreateDate(java.lang.String value) {
        _CreateDate = value;
    }

    public edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.budget.impl.ReportHeaderTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_BudgetVersion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "BudgetVersion"));
        }
        context.startElement("", "ProposalNumber");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _ProposalNumber), "ProposalNumber");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_ProposalTitle!= null) {
            context.startElement("", "ProposalTitle");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _ProposalTitle), "ProposalTitle");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PIName!= null) {
            context.startElement("", "PIName");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PIName), "PIName");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("", "BudgetVersion");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _BudgetVersion)), "BudgetVersion");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "PeriodStartDate");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PeriodStartDate), "PeriodStartDate");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        context.startElement("", "PeriodEndDate");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _PeriodEndDate), "PeriodEndDate");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (has_Period) {
            context.startElement("", "Period");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _Period)), "Period");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_CreateDate!= null) {
            context.startElement("", "CreateDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CreateDate), "CreateDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        context.startElement("", "Comments");
        context.endNamespaceDecls();
        context.endAttributes();
        try {
            context.text(((java.lang.String) _Comments), "Comments");
        } catch (java.lang.Exception e) {
            edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
        }
        context.endElement();
        if (_PrintBudgetComment!= null) {
            context.startElement("", "PrintBudgetComment");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _PrintBudgetComment), "PrintBudgetComment");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_BudgetVersion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "BudgetVersion"));
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (!has_BudgetVersion) {
            context.reportError(com.sun.xml.bind.serializer.Util.createMissingObjectError(this, "BudgetVersion"));
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.budget.ReportHeaderType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000pp"
+"sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\'com.sun.msv.grammar.trex.Element"
+"Pattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/NameCl"
+"ass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aignoreUn"
+"declaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003pp\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bc"
+"om.sun.msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/da"
+"tatype/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/St"
+"ringPair;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinA"
+"tomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteTyp"
+"e\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001bL\u0000"
+"\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;"
+"xpt\u0000 http://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.ms"
+"v.datatype.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,co"
+"m.sun.msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000"
+"com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNam"
+"eq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esr\u0000\u001dcom.sun.msv.gramma"
+"r.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000 com.sun.msv.grammar.Attrib"
+"uteExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\u000fxq\u0000~\u0000\u0003sr\u0000\u0011java."
+"lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000psq\u0000~\u0000\u0013ppsr\u0000\"com.sun.msv.d"
+"atatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0005QNamesr\u00005com.s"
+"un.msv.datatype.xsd.WhiteSpaceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000!q\u0000~\u0000$sq\u0000~\u0000%q\u0000~\u00000q\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleName"
+"Class\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom"
+".sun.msv.grammar.NameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www."
+"w3.org/2001/XMLSchema-instancesr\u00000com.sun.msv.grammar.Expres"
+"sion$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000+\u0001psq\u0000~\u00004t\u0000\u000eProp"
+"osalNumbert\u0000\u0000sq\u0000~\u0000\'ppsq\u0000~\u0000\u000eq\u0000~\u0000,p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000"
+")q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\rProposalTitleq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'"
+"ppsq\u0000~\u0000\u000eq\u0000~\u0000,p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000"
+"~\u0000:sq\u0000~\u00004t\u0000\u0006PINameq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0013ppsr\u0000 com"
+".sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.sun.msv.data"
+"type.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacetst\u0000)Lcom/s"
+"un/msv/datatype/xsd/XSDatatypeImpl;xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0003intq\u0000~\u00002sr\u0000"
+"*com.sun.msv.datatype.xsd.MaxInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#co"
+"m.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\nlimitValuet\u0000\u0012"
+"Ljava/lang/Object;xr\u00009com.sun.msv.datatype.xsd.DataTypeWithV"
+"alueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.msv.datatype.xsd.D"
+"ataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ\u0000\u0012needValueCheckF"
+"lagL\u0000\bbaseTypeq\u0000~\u0000RL\u0000\fconcreteTypet\u0000\'Lcom/sun/msv/datatype/x"
+"sd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001bxq\u0000~\u0000\u001appq\u0000~\u00002\u0000\u0001sr\u0000*com.sun."
+"msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Vppq\u0000~\u00002\u0000\u0000"
+"sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Qq\u0000~\u0000\u001et"
+"\u0000\u0004longq\u0000~\u00002sq\u0000~\u0000Uppq\u0000~\u00002\u0000\u0001sq\u0000~\u0000\\ppq\u0000~\u00002\u0000\u0000sr\u0000$com.sun.msv.dat"
+"atype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000Qq\u0000~\u0000\u001et\u0000\u0007integerq\u0000~\u00002sr"
+"\u0000,com.sun.msv.datatype.xsd.FractionDigitsFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005"
+"scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeWithLexicalConstra"
+"intFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000Yppq\u0000~\u00002\u0001\u0000sr\u0000#com.sun.msv.datatype.x"
+"sd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007decimalq\u0000~\u00002q\u0000~\u0000jt\u0000\u000efr"
+"actionDigits\u0000\u0000\u0000\u0000q\u0000~\u0000dt\u0000\fminInclusivesr\u0000\u000ejava.lang.Long;\u008b\u00e4\u0090\u00cc\u008f"
+"#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u0000dt"
+"\u0000\fmaxInclusivesq\u0000~\u0000n\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000_q\u0000~\u0000msr\u0000\u0011java.lang.Integer\u0012"
+"\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000o\u0080\u0000\u0000\u0000q\u0000~\u0000_q\u0000~\u0000qsq\u0000~\u0000s\u007f\u00ff\u00ff\u00ffq\u0000~\u0000$sq\u0000~\u0000%q"
+"\u0000~\u0000Tq\u0000~\u0000\u001esq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\rBudgetV"
+"ersionq\u0000~\u0000>sq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000"
+"~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\u000fPeriodStartDateq\u0000~\u0000>sq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016s"
+"q\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\rPeriodEndDateq\u0000~\u0000"
+">sq\u0000~\u0000\'ppsq\u0000~\u0000\u000eq\u0000~\u0000,p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000Osq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-"
+"q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\u0006Periodq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\'ppsq\u0000~\u0000\u000eq\u0000~\u0000,p\u0000sq\u0000~"
+"\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\nCreateD"
+"ateq\u0000~\u0000>q\u0000~\u0000:sq\u0000~\u0000\u000epp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-"
+"q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\bCommentsq\u0000~\u0000>sq\u0000~\u0000\'ppsq\u0000~\u0000\u000eq\u0000~\u0000,p\u0000sq\u0000~\u0000\u0000p"
+"pq\u0000~\u0000\u0016sq\u0000~\u0000\'ppsq\u0000~\u0000)q\u0000~\u0000,pq\u0000~\u0000-q\u0000~\u00006q\u0000~\u0000:sq\u0000~\u00004t\u0000\u0012PrintBudge"
+"tCommentq\u0000~\u0000>q\u0000~\u0000:sr\u0000\"com.sun.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv/grammar/ExpressionPool$Clo"
+"sedHash;xpsr\u0000-com.sun.msv.grammar.ExpressionPool$ClosedHash\u00d7"
+"j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/g"
+"rammar/ExpressionPool;xp\u0000\u0000\u0000\"\u0001pq\u0000~\u0000\tq\u0000~\u0000\u0012q\u0000~\u0000Aq\u0000~\u0000Hq\u0000~\u0000|q\u0000~\u0000\n"
+"q\u0000~\u0000\u0082q\u0000~\u0000\u0090q\u0000~\u0000\u0096q\u0000~\u0000\u009dq\u0000~\u0000\u000bq\u0000~\u0000Nq\u0000~\u0000\u0089q\u0000~\u0000?q\u0000~\u0000Fq\u0000~\u0000\u008eq\u0000~\u0000\u009bq\u0000~\u0000\f"
+"q\u0000~\u0000\bq\u0000~\u0000\u0007q\u0000~\u0000\u0006q\u0000~\u0000\u0087q\u0000~\u0000\rq\u0000~\u0000\u0005q\u0000~\u0000(q\u0000~\u0000Bq\u0000~\u0000Iq\u0000~\u0000wq\u0000~\u0000}q\u0000~\u0000\u0083"
+"q\u0000~\u0000\u008aq\u0000~\u0000\u0091q\u0000~\u0000\u0097q\u0000~\u0000\u009ex"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.budget.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------------------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.budget.impl.ReportHeaderTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("ProposalTitle" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  24 :
                        if (("Comments" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 25;
                            return ;
                        }
                        break;
                    case  18 :
                        if (("Period" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 19;
                            return ;
                        }
                        state = 21;
                        continue outer;
                    case  6 :
                        if (("PIName" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  0 :
                        if (("ProposalNumber" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        break;
                    case  9 :
                        if (("BudgetVersion" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        break;
                    case  30 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  27 :
                        if (("PrintBudgetComment" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 28;
                            return ;
                        }
                        state = 30;
                        continue outer;
                    case  21 :
                        if (("CreateDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 22;
                            return ;
                        }
                        state = 24;
                        continue outer;
                    case  15 :
                        if (("PeriodEndDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 16;
                            return ;
                        }
                        break;
                    case  12 :
                        if (("PeriodStartDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
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
                        state = 6;
                        continue outer;
                    case  29 :
                        if (("PrintBudgetComment" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 30;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("PIName" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("ProposalNumber" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  14 :
                        if (("PeriodStartDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  5 :
                        if (("ProposalTitle" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  23 :
                        if (("CreateDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 24;
                            return ;
                        }
                        break;
                    case  30 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  27 :
                        state = 30;
                        continue outer;
                    case  21 :
                        state = 24;
                        continue outer;
                    case  17 :
                        if (("PeriodEndDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 18;
                            return ;
                        }
                        break;
                    case  11 :
                        if (("BudgetVersion" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  26 :
                        if (("Comments" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 27;
                            return ;
                        }
                        break;
                    case  20 :
                        if (("Period" == ___local)&&("" == ___uri)) {
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  30 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  27 :
                        state = 30;
                        continue outer;
                    case  21 :
                        state = 24;
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
                    case  18 :
                        state = 21;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  30 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  27 :
                        state = 30;
                        continue outer;
                    case  21 :
                        state = 24;
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
                        case  28 :
                            state = 29;
                            eatText1(value);
                            return ;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  18 :
                            state = 21;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText3(value);
                            return ;
                        case  25 :
                            state = 26;
                            eatText4(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText5(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText6(value);
                            return ;
                        case  19 :
                            state = 20;
                            eatText7(value);
                            return ;
                        case  30 :
                            revertToParentFromText(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText8(value);
                            return ;
                        case  27 :
                            state = 30;
                            continue outer;
                        case  21 :
                            state = 24;
                            continue outer;
                        case  22 :
                            state = 23;
                            eatText9(value);
                            return ;
                        case  16 :
                            state = 17;
                            eatText10(value);
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
                _PrintBudgetComment = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProposalTitle = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _ProposalNumber = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Comments = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText5(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetVersion = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_BudgetVersion = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText6(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PeriodStartDate = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText7(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Period = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_Period = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText8(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PIName = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText9(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CreateDate = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText10(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PeriodEndDate = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
