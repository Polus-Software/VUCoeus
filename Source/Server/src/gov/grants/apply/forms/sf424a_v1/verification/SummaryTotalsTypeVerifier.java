//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.verification;

public class SummaryTotalsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master) {
        if (null!= master.getBudgetFederalEstimatedUnobligatedAmount()) {
            // If left exists
            if (null == master.getBudgetFederalEstimatedUnobligatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetFederalEstimatedUnobligatedAmount(parentLocator, handler, master, master.getBudgetFederalEstimatedUnobligatedAmount());
            }
        }
        if (null!= master.getBudgetNonFederalEstimatedUnobligatedAmount()) {
            // If left exists
            if (null == master.getBudgetNonFederalEstimatedUnobligatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetNonFederalEstimatedUnobligatedAmount(parentLocator, handler, master, master.getBudgetNonFederalEstimatedUnobligatedAmount());
            }
        }
        if (null!= master.getBudgetFederalNewOrRevisedAmount()) {
            // If left exists
            if (null == master.getBudgetFederalNewOrRevisedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetFederalNewOrRevisedAmount(parentLocator, handler, master, master.getBudgetFederalNewOrRevisedAmount());
            }
        }
        if (null!= master.getBudgetNonFederalNewOrRevisedAmount()) {
            // If left exists
            if (null == master.getBudgetNonFederalNewOrRevisedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetNonFederalNewOrRevisedAmount(parentLocator, handler, master, master.getBudgetNonFederalNewOrRevisedAmount());
            }
        }
        if (null!= master.getBudgetTotalNewOrRevisedAmount()) {
            // If left exists
            if (null == master.getBudgetTotalNewOrRevisedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetTotalNewOrRevisedAmount(parentLocator, handler, master, master.getBudgetTotalNewOrRevisedAmount());
            }
        }
    }

    public void checkBudgetFederalEstimatedUnobligatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetFederalEstimatedUnobligatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetFederalEstimatedUnobligatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetTotalNewOrRevisedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetTotalNewOrRevisedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetTotalNewOrRevisedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetFederalNewOrRevisedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetFederalNewOrRevisedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetFederalNewOrRevisedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetNonFederalNewOrRevisedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonFederalNewOrRevisedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonFederalNewOrRevisedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetNonFederalEstimatedUnobligatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonFederalEstimatedUnobligatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetNonFederalEstimatedUnobligatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424a_v1.SummaryTotalsType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424a_v1.SummaryTotalsType) object));
    }

}
