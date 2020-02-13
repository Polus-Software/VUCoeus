//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_budget_v1_1.verification;

public class OtherPersonnelDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType master) {
        if (null == master.getNumberOfPersonnel()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NumberOfPersonnel"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkNumberOfPersonnel(parentLocator, handler, master, master.getNumberOfPersonnel());
        }
        if (null == master.getProjectRole()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectRole"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkProjectRole(parentLocator, handler, master, master.getProjectRole());
        }
        if (null == master.getCompensation()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Compensation"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCompensation(parentLocator, handler, master, master.getCompensation());
        }
    }

    public void checkCompensation(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType master, gov.grants.apply.forms.rr_budget_v1_1.SectBCompensationDataType value) {
        if (value instanceof gov.grants.apply.forms.rr_budget_v1_1.SectBCompensationDataType) {
            gov.grants.apply.forms.rr_budget_v1_1.SectBCompensationDataType realValue = ((gov.grants.apply.forms.rr_budget_v1_1.SectBCompensationDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_budget_v1_1.verification.SectBCompensationDataTypeVerifier verifier = new gov.grants.apply.forms.rr_budget_v1_1.verification.SectBCompensationDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Compensation"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Compensation"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkProjectRole(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxLengthFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((null == realValue)? 0 :realValue.length())>= 1) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooShortProblem(realValue, ((null == realValue)? 0 :realValue.length()), 1);
                }
                if (((null == realValue)? 0 :realValue.length())<= 100) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 100);
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectRole"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectRole"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkNumberOfPersonnel(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (realValue.signum()< 0) {
                    problem = new de.fzi.dbs.verification.event.datatype.NegativeProblem(realValue);
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NumberOfPersonnel"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NumberOfPersonnel"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_budget_v1_1.OtherPersonnelDataType) object));
    }

}
