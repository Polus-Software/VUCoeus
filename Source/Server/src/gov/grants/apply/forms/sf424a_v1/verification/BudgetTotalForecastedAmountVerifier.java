//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.verification;

public class BudgetTotalForecastedAmountVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetTotalForecastedAmount master) {
        if (null == master.getValue()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkValue(parentLocator, handler, master, master.getValue());
        }
    }

    public void checkValue(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetTotalForecastedAmount master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetTotalForecastedAmount) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetTotalForecastedAmount) object));
    }

}
