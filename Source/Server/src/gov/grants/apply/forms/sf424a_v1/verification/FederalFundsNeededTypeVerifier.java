//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.verification;

public class FederalFundsNeededTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType master) {
        if (null == master.getFundsLineItem()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsLineItem"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getFundsLineItem().size()< 0) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsLineItem"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getFundsLineItem().size(), 0)));
            }
            if (master.getFundsLineItem().size()> 4) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsLineItem"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getFundsLineItem().size(), 4)));
            }
            // Check value
            checkFundsLineItem(parentLocator, handler, master, master.getFundsLineItem());
        }
        if (null!= master.getFundsTotals()) {
            // If left exists
            if (null == master.getFundsTotals()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkFundsTotals(parentLocator, handler, master, master.getFundsTotals());
            }
        }
    }

    public void checkFundsTotals(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType master, gov.grants.apply.forms.sf424a_v1.FundsTotalsType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.FundsTotalsType) {
            gov.grants.apply.forms.sf424a_v1.FundsTotalsType realValue = ((gov.grants.apply.forms.sf424a_v1.FundsTotalsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.FundsTotalsTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FundsTotalsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsTotals"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.FundsTotals) {
                gov.grants.apply.forms.sf424a_v1.FundsTotals realValue = ((gov.grants.apply.forms.sf424a_v1.FundsTotals) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.FundsTotalsVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FundsTotalsVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsTotals"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FundsTotals"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkFundsLineItem(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkFundsLineItem(parentLocator, handler, master, index, item);
        }
    }

    public void checkFundsLineItem(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.FundsLineItemType) {
            gov.grants.apply.forms.sf424a_v1.FundsLineItemType realValue = ((gov.grants.apply.forms.sf424a_v1.FundsLineItemType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.FundsLineItemTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FundsLineItemTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "FundsLineItem", index), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.FundsLineItem) {
                gov.grants.apply.forms.sf424a_v1.FundsLineItem realValue = ((gov.grants.apply.forms.sf424a_v1.FundsLineItem) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.FundsLineItemVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FundsLineItemVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "FundsLineItem", index), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "FundsLineItem", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType) object));
    }

}