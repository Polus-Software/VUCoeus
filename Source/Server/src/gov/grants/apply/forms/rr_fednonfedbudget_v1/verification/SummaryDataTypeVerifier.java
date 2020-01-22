//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_fednonfedbudget_v1.verification;

public class SummaryDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType master) {
        if (null == master.getFederalSummary()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalSummary"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFederalSummary(parentLocator, handler, master, master.getFederalSummary());
        }
        if (null == master.getNonFederalSummary()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalSummary"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkNonFederalSummary(parentLocator, handler, master, master.getNonFederalSummary());
        }
        if (null == master.getTotalFedNonFedSummary()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFedSummary"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkTotalFedNonFedSummary(parentLocator, handler, master, master.getTotalFedNonFedSummary());
        }
    }

    public void checkTotalFedNonFedSummary(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFedSummary"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFedSummary"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkNonFederalSummary(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalSummary"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalSummary"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFederalSummary(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalSummary"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalSummary"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1.SummaryDataType) object));
    }

}