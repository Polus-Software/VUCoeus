//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424c_v1.verification;

public class CostSubtotalBeforeContingenciesTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType master) {
        if (null!= master.getBudgetEstimatedCostAmount()) {
            // If left exists
            if (null == master.getBudgetEstimatedCostAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetEstimatedCostAmount(parentLocator, handler, master, master.getBudgetEstimatedCostAmount());
            }
        }
        if (null!= master.getBudgetNonAllowableCostAmount()) {
            // If left exists
            if (null == master.getBudgetNonAllowableCostAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetNonAllowableCostAmount(parentLocator, handler, master, master.getBudgetNonAllowableCostAmount());
            }
        }
        if (null!= master.getBudgetTotalAllowableCostAmount()) {
            // If left exists
            if (null == master.getBudgetTotalAllowableCostAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetTotalAllowableCostAmount(parentLocator, handler, master, master.getBudgetTotalAllowableCostAmount());
            }
        }
    }

    public void checkBudgetNonAllowableCostAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonAllowableCostAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonAllowableCostAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetEstimatedCostAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetEstimatedCostAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetEstimatedCostAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetTotalAllowableCostAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetTotalAllowableCostAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetTotalAllowableCostAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424c_v1.CostSubtotalBeforeContingenciesType) object));
    }

}
