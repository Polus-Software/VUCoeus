//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.08 at 04:55:27 PM IST 
//


package edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl;

public class SalaryTypeImpl implements edu.mit.coeus.utils.xml.bean.budget.budgetSalary.SalaryType, com.sun.xml.bind.JAXBObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallableObject, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializable, edu.mit.coeus.utils.xml.bean.budget.impl.runtime.ValidatableObject
{

    protected java.lang.String _CostElementDesc;
    protected java.lang.String _CostElementCode;
    protected java.lang.String _Name;
    protected java.math.BigDecimal _Total;
    protected com.sun.xml.bind.util.ListImpl _Period;
    public final static java.lang.Class version = (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.JAXBVersion.class);
    private static com.sun.msv.grammar.Grammar schemaFragment;

    private final static java.lang.Class PRIMARY_INTERFACE_CLASS() {
        return (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.SalaryType.class);
    }

    public java.lang.String getCostElementDesc() {
        return _CostElementDesc;
    }

    public void setCostElementDesc(java.lang.String value) {
        _CostElementDesc = value;
    }

    public java.lang.String getCostElementCode() {
        return _CostElementCode;
    }

    public void setCostElementCode(java.lang.String value) {
        _CostElementCode = value;
    }

    public java.lang.String getName() {
        return _Name;
    }

    public void setName(java.lang.String value) {
        _Name = value;
    }

    public java.math.BigDecimal getTotal() {
        return _Total;
    }

    public void setTotal(java.math.BigDecimal value) {
        _Total = value;
    }

    protected com.sun.xml.bind.util.ListImpl _getPeriod() {
        if (_Period == null) {
            _Period = new com.sun.xml.bind.util.ListImpl(new java.util.ArrayList());
        }
        return _Period;
    }

    public java.util.List getPeriod() {
        return _getPeriod();
    }

    public edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingEventHandler createUnmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
        return new edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.SalaryTypeImpl.Unmarshaller(context);
    }

    public void serializeBody(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_Period == null)? 0 :_Period.size());
        if (_CostElementDesc!= null) {
            context.startElement("", "costElementDesc");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CostElementDesc), "CostElementDesc");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_CostElementCode!= null) {
            context.startElement("", "costElementCode");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _CostElementCode), "CostElementCode");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        if (_Name!= null) {
            context.startElement("", "name");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(((java.lang.String) _Name), "Name");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
        while (idx5 != len5) {
            context.startElement("", "Period");
            int idx_6 = idx5;
            context.childAsURIs(((com.sun.xml.bind.JAXBObject) _Period.get(idx_6 ++)), "Period");
            context.endNamespaceDecls();
            int idx_7 = idx5;
            context.childAsAttributes(((com.sun.xml.bind.JAXBObject) _Period.get(idx_7 ++)), "Period");
            context.endAttributes();
            context.childAsBody(((com.sun.xml.bind.JAXBObject) _Period.get(idx5 ++)), "Period");
            context.endElement();
        }
        if (_Total!= null) {
            context.startElement("", "total");
            context.endNamespaceDecls();
            context.endAttributes();
            try {
                context.text(javax.xml.bind.DatatypeConverter.printDecimal(((java.math.BigDecimal) _Total)), "Total");
            } catch (java.lang.Exception e) {
                edu.mit.coeus.utils.xml.bean.budget.impl.runtime.Util.handlePrintConversionException(this, e, context);
            }
            context.endElement();
        }
    }

    public void serializeAttributes(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_Period == null)? 0 :_Period.size());
        while (idx5 != len5) {
            idx5 += 1;
        }
    }

    public void serializeURIs(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.XMLSerializer context)
        throws org.xml.sax.SAXException
    {
        int idx5 = 0;
        final int len5 = ((_Period == null)? 0 :_Period.size());
        while (idx5 != len5) {
            idx5 += 1;
        }
    }

    public java.lang.Class getPrimaryInterface() {
        return (edu.mit.coeus.utils.xml.bean.budget.budgetSalary.SalaryType.class);
    }

    public com.sun.msv.verifier.DocumentDeclaration createRawValidator() {
        if (schemaFragment == null) {
            schemaFragment = com.sun.xml.bind.validator.SchemaDeserializer.deserialize((
 "\u00ac\u00ed\u0000\u0005sr\u0000\u001fcom.sun.msv.grammar.SequenceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\u001dcom.su"
+"n.msv.grammar.BinaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0004exp1t\u0000 Lcom/sun/msv/gra"
+"mmar/Expression;L\u0000\u0004exp2q\u0000~\u0000\u0002xr\u0000\u001ecom.sun.msv.grammar.Expressi"
+"on\u00f8\u0018\u0082\u00e8N5~O\u0002\u0000\u0002L\u0000\u0013epsilonReducibilityt\u0000\u0013Ljava/lang/Boolean;L\u0000\u000b"
+"expandedExpq\u0000~\u0000\u0002xpppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsq\u0000~\u0000\u0000ppsr\u0000\u001dcom.sun.msv."
+"grammar.ChoiceExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0001ppsr\u0000\'com.sun.msv.grammar."
+"trex.ElementPattern\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\tnameClasst\u0000\u001fLcom/sun/msv/gr"
+"ammar/NameClass;xr\u0000\u001ecom.sun.msv.grammar.ElementExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0002Z\u0000\u001aignoreUndeclaredAttributesL\u0000\fcontentModelq\u0000~\u0000\u0002xq\u0000~\u0000\u0003sr\u0000\u0011"
+"java.lang.Boolean\u00cd r\u0080\u00d5\u009c\u00fa\u00ee\u0002\u0000\u0001Z\u0000\u0005valuexp\u0000p\u0000sq\u0000~\u0000\u0000ppsr\u0000\u001bcom.sun"
+".msv.grammar.DataExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003L\u0000\u0002dtt\u0000\u001fLorg/relaxng/datatype"
+"/Datatype;L\u0000\u0006exceptq\u0000~\u0000\u0002L\u0000\u0004namet\u0000\u001dLcom/sun/msv/util/StringPa"
+"ir;xq\u0000~\u0000\u0003ppsr\u0000#com.sun.msv.datatype.xsd.StringType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000"
+"\u0001Z\u0000\risAlwaysValidxr\u0000*com.sun.msv.datatype.xsd.BuiltinAtomicT"
+"ype\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000%com.sun.msv.datatype.xsd.ConcreteType\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000\'com.sun.msv.datatype.xsd.XSDatatypeImpl\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0003"
+"L\u0000\fnamespaceUrit\u0000\u0012Ljava/lang/String;L\u0000\btypeNameq\u0000~\u0000\u001aL\u0000\nwhite"
+"Spacet\u0000.Lcom/sun/msv/datatype/xsd/WhiteSpaceProcessor;xpt\u0000 h"
+"ttp://www.w3.org/2001/XMLSchemat\u0000\u0006stringsr\u00005com.sun.msv.data"
+"type.xsd.WhiteSpaceProcessor$Preserve\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xr\u0000,com.sun."
+"msv.datatype.xsd.WhiteSpaceProcessor\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xp\u0001sr\u00000com.su"
+"n.msv.grammar.Expression$NullSetExpression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003p"
+"psr\u0000\u001bcom.sun.msv.util.StringPair\u00d0t\u001ejB\u008f\u008d\u00a0\u0002\u0000\u0002L\u0000\tlocalNameq\u0000~\u0000\u001a"
+"L\u0000\fnamespaceURIq\u0000~\u0000\u001axpq\u0000~\u0000\u001eq\u0000~\u0000\u001dsq\u0000~\u0000\tppsr\u0000 com.sun.msv.gram"
+"mar.AttributeExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\u0003expq\u0000~\u0000\u0002L\u0000\tnameClassq\u0000~\u0000\fxq\u0000~\u0000"
+"\u0003q\u0000~\u0000\u0010psq\u0000~\u0000\u0012ppsr\u0000\"com.sun.msv.datatype.xsd.QnameType\u0000\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0005QNamesr\u00005com.sun.msv.datatype.xsd.WhiteSpa"
+"ceProcessor$Collapse\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000 q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000,q\u0000~\u0000\u001dsr"
+"\u0000#com.sun.msv.grammar.SimpleNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0002L\u0000\tlocalName"
+"q\u0000~\u0000\u001aL\u0000\fnamespaceURIq\u0000~\u0000\u001axr\u0000\u001dcom.sun.msv.grammar.NameClass\u0000\u0000"
+"\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xpt\u0000\u0004typet\u0000)http://www.w3.org/2001/XMLSchema-instan"
+"cesr\u00000com.sun.msv.grammar.Expression$EpsilonExpression\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003sq\u0000~\u0000\u000f\u0001q\u0000~\u00006sq\u0000~\u00000t\u0000\u000fcostElementDesct\u0000\u0000q\u0000~\u00006sq\u0000~\u0000"
+"\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppq\u0000~\u0000\u0015sq\u0000~\u0000\tppsq\u0000~\u0000\'q\u0000~\u0000\u0010pq\u0000~\u0000)q\u0000~\u00002q"
+"\u0000~\u00006sq\u0000~\u00000t\u0000\u000fcostElementCodeq\u0000~\u0000:q\u0000~\u00006sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000s"
+"q\u0000~\u0000\u0000ppq\u0000~\u0000\u0015sq\u0000~\u0000\tppsq\u0000~\u0000\'q\u0000~\u0000\u0010pq\u0000~\u0000)q\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\u0004name"
+"q\u0000~\u0000:q\u0000~\u00006sq\u0000~\u0000\tppsr\u0000 com.sun.msv.grammar.OneOrMoreExp\u0000\u0000\u0000\u0000\u0000\u0000"
+"\u0000\u0001\u0002\u0000\u0000xr\u0000\u001ccom.sun.msv.grammar.UnaryExp\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\u0003expq\u0000~\u0000\u0002x"
+"q\u0000~\u0000\u0003q\u0000~\u0000\u0010psq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000ppsq\u0000~\u0000\u000bpp\u0000sq\u0000~\u0000\tppsq\u0000~\u0000Jq\u0000~\u0000\u0010"
+"psq\u0000~\u0000\'q\u0000~\u0000\u0010psr\u00002com.sun.msv.grammar.Expression$AnyStringExp"
+"ression\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u0000\u0003q\u0000~\u00007q\u0000~\u0000Tsr\u0000 com.sun.msv.grammar.An"
+"yNameClass\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000xq\u0000~\u00001q\u0000~\u00006sq\u0000~\u00000t\u0000Aedu.mit.coeus.utils"
+".xml.bean.budget.budgetSalary.BudgetPeriodDatat\u0000+http://java"
+".sun.com/jaxb/xjc/dummy-elementssq\u0000~\u0000\tppsq\u0000~\u0000\'q\u0000~\u0000\u0010pq\u0000~\u0000)q\u0000~"
+"\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\u0006Periodq\u0000~\u0000:q\u0000~\u00006sq\u0000~\u0000\tppsq\u0000~\u0000\u000bq\u0000~\u0000\u0010p\u0000sq\u0000~\u0000\u0000p"
+"psq\u0000~\u0000\u0012ppsr\u0000#com.sun.msv.datatype.xsd.NumberType\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0000x"
+"q\u0000~\u0000\u0017q\u0000~\u0000\u001dt\u0000\u0007decimalq\u0000~\u0000.q\u0000~\u0000#sq\u0000~\u0000$q\u0000~\u0000dq\u0000~\u0000\u001dsq\u0000~\u0000\tppsq\u0000~\u0000\'"
+"q\u0000~\u0000\u0010pq\u0000~\u0000)q\u0000~\u00002q\u0000~\u00006sq\u0000~\u00000t\u0000\u0005totalq\u0000~\u0000:q\u0000~\u00006sr\u0000\"com.sun.msv"
+".grammar.ExpressionPool\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0001\u0002\u0000\u0001L\u0000\bexpTablet\u0000/Lcom/sun/msv"
+"/grammar/ExpressionPool$ClosedHash;xpsr\u0000-com.sun.msv.grammar"
+".ExpressionPool$ClosedHash\u00d7j\u00d0N\u00ef\u00e8\u00ed\u001c\u0003\u0000\u0003I\u0000\u0005countB\u0000\rstreamVersio"
+"nL\u0000\u0006parentt\u0000$Lcom/sun/msv/grammar/ExpressionPool;xp\u0000\u0000\u0000\u0016\u0001pq\u0000~"
+"\u0000\nq\u0000~\u0000;q\u0000~\u0000Bq\u0000~\u0000&q\u0000~\u0000>q\u0000~\u0000Eq\u0000~\u0000Zq\u0000~\u0000\u0006q\u0000~\u0000fq\u0000~\u0000^q\u0000~\u0000Nq\u0000~\u0000Pq\u0000~"
+"\u0000`q\u0000~\u0000Lq\u0000~\u0000\u0007q\u0000~\u0000Qq\u0000~\u0000\u0011q\u0000~\u0000=q\u0000~\u0000Dq\u0000~\u0000\u0005q\u0000~\u0000\bq\u0000~\u0000Ix"));
        }
        return new com.sun.msv.verifier.regexp.REDocumentDeclaration(schemaFragment);
    }

    public class Unmarshaller
        extends edu.mit.coeus.utils.xml.bean.budget.impl.runtime.AbstractUnmarshallingEventHandlerImpl
    {


        public Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context) {
            super(context, "----------------");
        }

        protected Unmarshaller(edu.mit.coeus.utils.xml.bean.budget.impl.runtime.UnmarshallingContext context, int startState) {
            this(context);
            state = startState;
        }

        public java.lang.Object owner() {
            return edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.SalaryTypeImpl.this;
        }

        public void enterElement(java.lang.String ___uri, java.lang.String ___local, java.lang.String ___qname, org.xml.sax.Attributes __atts)
            throws org.xml.sax.SAXException
        {
            int attIdx;
            outer:
            while (true) {
                switch (state) {
                    case  6 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 7;
                            return ;
                        }
                        state = 9;
                        continue outer;
                    case  9 :
                        if (("Period" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        state = 12;
                        continue outer;
                    case  10 :
                        if (("budgetPeriodID" == ___local)&&("" == ___uri)) {
                            _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("startDate" == ___local)&&("" == ___uri)) {
                            _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("endDate" == ___local)&&("" == ___uri)) {
                            _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        if (("periodCost" == ___local)&&("" == ___uri)) {
                            _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                            return ;
                        }
                        _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname, __atts)));
                        return ;
                    case  3 :
                        if (("costElementCode" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 4;
                            return ;
                        }
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterElement(___uri, ___local, ___qname, __atts);
                        return ;
                    case  0 :
                        if (("costElementDesc" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 1;
                            return ;
                        }
                        state = 3;
                        continue outer;
                    case  12 :
                        if (("Period" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, false);
                            state = 10;
                            return ;
                        }
                        if (("total" == ___local)&&("" == ___uri)) {
                            context.pushAttributes(__atts, true);
                            state = 13;
                            return ;
                        }
                        state = 15;
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
                    case  14 :
                        if (("total" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 15;
                            return ;
                        }
                        break;
                    case  2 :
                        if (("costElementDesc" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 3;
                            return ;
                        }
                        break;
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromLeaveElement((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  5 :
                        if (("costElementCode" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 6;
                            return ;
                        }
                        break;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  11 :
                        if (("Period" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 12;
                            return ;
                        }
                        break;
                    case  15 :
                        revertToParentFromLeaveElement(___uri, ___local, ___qname);
                        return ;
                    case  8 :
                        if (("name" == ___local)&&("" == ___uri)) {
                            context.popAttributes();
                            state = 9;
                            return ;
                        }
                        break;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromEnterAttribute((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromEnterAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                    case  6 :
                        state = 9;
                        continue outer;
                    case  9 :
                        state = 12;
                        continue outer;
                    case  10 :
                        _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromLeaveAttribute((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, ___uri, ___local, ___qname)));
                        return ;
                    case  3 :
                        state = 6;
                        continue outer;
                    case  15 :
                        revertToParentFromLeaveAttribute(___uri, ___local, ___qname);
                        return ;
                    case  0 :
                        state = 3;
                        continue outer;
                    case  12 :
                        state = 15;
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
                        case  1 :
                            state = 2;
                            eatText1(value);
                            return ;
                        case  6 :
                            state = 9;
                            continue outer;
                        case  9 :
                            state = 12;
                            continue outer;
                        case  4 :
                            state = 5;
                            eatText2(value);
                            return ;
                        case  10 :
                            _getPeriod().add(((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl) spawnChildFromText((edu.mit.coeus.utils.xml.bean.budget.budgetSalary.impl.BudgetPeriodDataImpl.class), 11, value)));
                            return ;
                        case  3 :
                            state = 6;
                            continue outer;
                        case  7 :
                            state = 8;
                            eatText3(value);
                            return ;
                        case  13 :
                            state = 14;
                            eatText4(value);
                            return ;
                        case  15 :
                            revertToParentFromText(value);
                            return ;
                        case  0 :
                            state = 3;
                            continue outer;
                        case  12 :
                            state = 15;
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
                _CostElementDesc = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText2(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _CostElementCode = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText3(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Name = value;
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

        private void eatText4(final java.lang.String value)
            throws org.xml.sax.SAXException
        {
            try {
                _Total = javax.xml.bind.DatatypeConverter.parseDecimal(com.sun.xml.bind.WhiteSpaceProcessor.collapse(value));
            } catch (java.lang.Exception e) {
                handleParseConversionException(e);
            }
        }

    }

}
