//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_fednonfedbudget_v1_1.verification;

public class TotalDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType master) {
        if (null == master.getFederal()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Federal"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFederal(parentLocator, handler, master, master.getFederal());
        }
        if (null == master.getNonFederal()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederal"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkNonFederal(parentLocator, handler, master, master.getNonFederal());
        }
        if (null == master.getTotalFedNonFed()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFed"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkTotalFedNonFed(parentLocator, handler, master, master.getTotalFedNonFed());
        }
    }

    public void checkNonFederal(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigDecimal("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("999999999999.99"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigDecimal("999999999999.99"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederal"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederal"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkTotalFedNonFed(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigDecimal("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("9999999999999.99"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigDecimal("9999999999999.99"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFed"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalFedNonFed"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFederal(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigDecimal("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigDecimal("999999999999.99"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigDecimal("999999999999.99"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Federal"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Federal"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1_1.TotalDataType) object));
    }

}
