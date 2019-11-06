//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.verification;

public class BudgetSummaryTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetSummaryType master) {
        if (null == master.getSummaryLineItem()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryLineItem"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getSummaryLineItem().size()< 0) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryLineItem"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getSummaryLineItem().size(), 0)));
            }
            if (master.getSummaryLineItem().size()> 4) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryLineItem"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getSummaryLineItem().size(), 4)));
            }
            // Check value
            checkSummaryLineItem(parentLocator, handler, master, master.getSummaryLineItem());
        }
        if (null!= master.getSummaryTotals()) {
            // If left exists
            if (null == master.getSummaryTotals()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkSummaryTotals(parentLocator, handler, master, master.getSummaryTotals());
            }
        }
    }

    public void checkSummaryLineItem(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetSummaryType master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkSummaryLineItem(parentLocator, handler, master, index, item);
        }
    }

    public void checkSummaryLineItem(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetSummaryType master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.SummaryLineItemType) {
            gov.grants.apply.forms.sf424a_v1.SummaryLineItemType realValue = ((gov.grants.apply.forms.sf424a_v1.SummaryLineItemType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.SummaryLineItemTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.SummaryLineItemTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "SummaryLineItem", index), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.SummaryLineItem) {
                gov.grants.apply.forms.sf424a_v1.SummaryLineItem realValue = ((gov.grants.apply.forms.sf424a_v1.SummaryLineItem) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.SummaryLineItemVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.SummaryLineItemVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "SummaryLineItem", index), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "SummaryLineItem", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkSummaryTotals(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetSummaryType master, gov.grants.apply.forms.sf424a_v1.SummaryTotalsType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.SummaryTotalsType) {
            gov.grants.apply.forms.sf424a_v1.SummaryTotalsType realValue = ((gov.grants.apply.forms.sf424a_v1.SummaryTotalsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.SummaryTotalsTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.SummaryTotalsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryTotals"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.SummaryTotals) {
                gov.grants.apply.forms.sf424a_v1.SummaryTotals realValue = ((gov.grants.apply.forms.sf424a_v1.SummaryTotals) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.SummaryTotalsVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.SummaryTotalsVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryTotals"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SummaryTotals"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetSummaryType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetSummaryType) object));
    }

}
