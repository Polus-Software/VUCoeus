//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.08 at 04:55:27 PM IST 
//


package edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl;

public class BudgetPeriodDataImpl implements edu.mit.coeus.utils.xml.bean.budget.budgetSalary.BudgetPeriodData, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.ValidatableObject
{

    protected java.util.Calendar _EndDate;
    protected java.util.Calendar _StartDate;
    protected java.math.BigDecimal _PeriodCost;
    protected boolean has_BudgetPeriodID;
    protected int _BudgetPeriodID;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.BudgetPeriodData.class);
    }

    public java.util.Calendar getEndDate() {
        return _EndDate;
    }

    public void setEndDate(java.util.Calendar value) {
        _EndDate = value;
    }

    public java.util.Calendar getStartDate() {
        return _StartDate;
    }

    public void setStartDate(java.util.Calendar value) {
        _StartDate = value;
    }

    public java.math.BigDecimal getPeriodCost() {
        return _PeriodCost;
    }

    public void setPeriodCost(java.math.BigDecimal value) {
        _PeriodCost = value;
    }

    public int getBudgetPeriodID() {
        return _BudgetPeriodID;
    }

    public void setBudgetPeriodID(int value) {
        _BudgetPeriodID = value;
        has_BudgetPeriodID = true;
    }

    public edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        if (has_BudgetPeriodID) {
            context.startElement("", "budgetPeriodID");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printInt(((int) _BudgetPeriodID)), "BudgetPeriodID");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_StartDate!= null) {
            context.startElement("", "startDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _StartDate)), "StartDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_EndDate!= null) {
            context.startElement("", "endDate");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDate(((java.util.Calendar) _EndDate)), "EndDate");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_PeriodCost!= null) {
            context.startElement("", "periodCost");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _PeriodCost)), "PeriodCost");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.BudgetPeriodData.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv.grammar."
+"ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar.trex.Ele"
+"mentPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/grammar/Na"
+"meClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002Z\u0000\u001aigno"
+"reUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011java.lan"
+"g.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun.msv.gra"
+"mmar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype/Datatyp"
+"e;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPair;xq\u0000~\u0000"
+"\u0003ppsr\u0000 com.sun.msv.datatype.xsd.IntType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000+com.su"
+"n.msv.datatype.xsd.IntegerDerivedType\u0099\u00f1]\u0090&6k\u00be\u0002\u0000\u0001L\u0000\nbaseFacet"
+"st\u0000)Lcom/sun/msv/datatype/xsd/XSDatatypeImpl;xr\u0000*com.sun.msv"
+".datatype.xsd.BuiltinAtomicType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.da"
+"tatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.x"
+"sd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/Str"
+"ing;L\u0000\btypeNameq\u0000~\u0000\u001bL\u0000\nwhiteSpacet\u0000.Lcom/sun/msv/datatype/xs"
+"d/WhiteSpaceProcessor;xpt\u0000 http://www.w3.org/2001/XMLSchemat"
+"\u0000\u0003intsr\u00005com.sun.msv.datatype.xsd.WhiteSpaceProcessor$Collap"
+"se\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun.msv.datatype.xsd.WhiteSpaceProcesso"
+"r\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpsr\u0000*com.sun.msv.datatype.xsd.MaxInclusiveFacet"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000#com.sun.msv.datatype.xsd.RangeFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001L\u0000\nlimitValuet\u0000\u0012Ljava/lang/Object;xr\u00009com.sun.msv.datatype."
+"xsd.DataTypeWithValueConstraintFacet\"\u00a7Ro\u00ca\u00c7\u008aT\u0002\u0000\u0000xr\u0000*com.sun.m"
+"sv.datatype.xsd.DataTypeWithFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0005Z\u0000\fisFacetFixedZ"
+"\u0000\u0012needValueCheckFlagL\u0000\bbaseTypeq\u0000~\u0000\u0017L\u0000\fconcreteTypet\u0000\'Lcom/s"
+"un/msv/datatype/xsd/ConcreteType;L\u0000\tfacetNameq\u0000~\u0000\u001bxq\u0000~\u0000\u001appq\u0000"
+"~\u0000\"\u0000\u0001sr\u0000*com.sun.msv.datatype.xsd.MinInclusiveFacet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002"
+"\u0000\u0000xq\u0000~\u0000$ppq\u0000~\u0000\"\u0000\u0000sr\u0000!com.sun.msv.datatype.xsd.LongType\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001et\u0000\u0004longq\u0000~\u0000\"sq\u0000~\u0000#ppq\u0000~\u0000\"\u0000\u0001sq\u0000~\u0000*ppq\u0000~\u0000\"\u0000\u0000sr"
+"\u0000$com.sun.msv.datatype.xsd.IntegerType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0016q\u0000~\u0000\u001e"
+"t\u0000\u0007integerq\u0000~\u0000\"sr\u0000,com.sun.msv.datatype.xsd.FractionDigitsFa"
+"cet\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001I\u0000\u0005scalexr\u0000;com.sun.msv.datatype.xsd.DataTypeW"
+"ithLexicalConstraintFacetT\u0090\u001c>\u001azb\u00ea\u0002\u0000\u0000xq\u0000~\u0000\'ppq\u0000~\u0000\"\u0001\u0000sr\u0000#com.s"
+"un.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0007decim"
+"alq\u0000~\u0000\"q\u0000~\u00008t\u0000\u000efractionDigits\u0000\u0000\u0000\u0000q\u0000~\u00002t\u0000\fminInclusivesr\u0000\u000ejav"
+"a.lang.Long;\u008b\u00e4\u0090\u00cc\u008f#\u00df\u0002\u0000\u0001J\u0000\u0005valuexr\u0000\u0010java.lang.Number\u0086\u00ac\u0095\u001d\u000b\u0094\u00e0\u008b\u0002\u0000"
+"\u0000xp\u0080\u0000\u0000\u0000\u0000\u0000\u0000\u0000q\u0000~\u00002t\u0000\fmaxInclusivesq\u0000~\u0000<\u007f\u00ff\u00ff\u00ff\u00ff\u00ff\u00ff\u00ffq\u0000~\u0000-q\u0000~\u0000;sr\u0000\u0011j"
+"ava.lang.Integer\u0012\u00e2\u00a0\u00a4\u00f7\u0081\u00878\u0002\u0000\u0001I\u0000\u0005valuexq\u0000~\u0000=\u0080\u0000\u0000\u0000q\u0000~\u0000-q\u0000~\u0000?sq\u0000~\u0000"
+"A\u007f\u00ff\u00ff\u00ffsr\u00000com.sun.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003ppsr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000"
+"\tlocalNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxpq\u0000~\u0000\u001fq\u0000~\u0000\u001esq\u0000~\u0000\bppsr\u0000 c"
+"om.sun.msv.grammar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tname"
+"Classq\u0000~\u0000\u000bxq\u0000~\u0000\u0003q\u0000~\u0000\u000fpsq\u0000~\u0000\u0011ppsr\u0000\"com.sun.msv.datatype.xsd.Q"
+"nameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0005QNameq\u0000~\u0000\"q\u0000~\u0000Esq\u0000~\u0000Fq\u0000~\u0000Nq"
+"\u0000~\u0000\u001esr\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tloc"
+"alNameq\u0000~\u0000\u001bL\u0000\fnamespaceURIq\u0000~\u0000\u001bxr\u0000\u001dcom.sun.msv.grammar.NameC"
+"lass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-"
+"instancesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression"
+"\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000e\u0001q\u0000~\u0000Vsq\u0000~\u0000Pt\u0000\u000ebudgetPeriodIDt\u0000\u0000q\u0000~\u0000V"
+"sq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011ppsr\u0000!com.sun.msv.datatyp"
+"e.xsd.DateType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000)com.sun.msv.datatype.xsd.DateTi"
+"meBaseType\u0014W\u001a@3\u00a5\u00b4\u00e5\u0002\u0000\u0000xq\u0000~\u0000\u0018q\u0000~\u0000\u001et\u0000\u0004dateq\u0000~\u0000\"q\u0000~\u0000Esq\u0000~\u0000Fq\u0000~\u0000b"
+"q\u0000~\u0000\u001esq\u0000~\u0000\bppsq\u0000~\u0000Iq\u0000~\u0000\u000fpq\u0000~\u0000Kq\u0000~\u0000Rq\u0000~\u0000Vsq\u0000~\u0000Pt\u0000\tstartDateq\u0000"
+"~\u0000Zq\u0000~\u0000Vsq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000^sq\u0000~\u0000\bppsq\u0000~\u0000Iq\u0000~\u0000"
+"\u000fpq\u0000~\u0000Kq\u0000~\u0000Rq\u0000~\u0000Vsq\u0000~\u0000Pt\u0000\u0007endDateq\u0000~\u0000Zq\u0000~\u0000Vsq\u0000~\u0000\bppsq\u0000~\u0000\nq\u0000~"
+"\u0000\u000fp\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0011ppq\u0000~\u00008q\u0000~\u0000Esq\u0000~\u0000Fq\u0000~\u00009q\u0000~\u0000\u001esq\u0000~\u0000\bppsq\u0000~\u0000I"
+"q\u0000~\u0000\u000fpq\u0000~\u0000Kq\u0000~\u0000Rq\u0000~\u0000Vsq\u0000~\u0000Pt\u0000\nperiodCostq\u0000~\u0000Zq\u0000~\u0000Vsr\u0000\"com.su"
+"n.msv.grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/su"
+"n/msv/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.gr"
+"ammar.ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamV"
+"ersionL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u000f"
+"\u0001pq\u0000~\u0000\u0010q\u0000~\u0000\u0005q\u0000~\u0000Hq\u0000~\u0000dq\u0000~\u0000kq\u0000~\u0000tq\u0000~\u0000\u0007q\u0000~\u0000oq\u0000~\u0000\u0006q\u0000~\u0000]q\u0000~\u0000jq\u0000~"
+"\u0000[q\u0000~\u0000hq\u0000~\u0000qq\u0000~\u0000\tx"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.budget.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
            super(context, "-------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  3 :
                        if (("startDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  9 :
                        if (("periodCost" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  6 :
                        if (("endDate" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("budgetPeriodID" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
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
                    case  9 :
                        state = 12;
                        continue outer;
                    case  11 :
                        if (("periodCost" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  2 :
                        if (("budgetPeriodID" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  8 :
                        if (("endDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  12 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  5 :
                        if (("startDate" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
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
                    case  3 :
                        state = 6;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  12 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
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
                    case  9 :
                        state = 12;
                        continue outer;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  12 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
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
                        case  4 :
                            state = 5;
                            eatText1(value);
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  1 :
                            state = 2;
                            eatText2(value);
                            return ;
                        case  12 :
                            revertToParentFromText(value);
                            return ;
                        case  10 :
                            state = 11;
                            eatText3(value);
                            return ;
                        case  7 :
                            state = 8;
                            eatText4(value);
                            return ;
                        case  0 :
                            state = 3;
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
                _StartDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _BudgetPeriodID = javax.xml.bind.DatatypeConverter.parseInt(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
                has_BudgetPeriodID = true;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _PeriodCost = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _EndDate = javax.xml.bind.DatatypeConverter.parseDate(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
