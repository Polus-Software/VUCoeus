//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.19 at 08:38:39 CST 
//


package gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification;

public class PHSInclusionEnrollmentReportEthnicCategoryDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType master) {
        if (null == master.getFemale()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Female"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFemale(parentLocator, handler, master, master.getFemale());
        }
        if (null == master.getMale()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Male"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkMale(parentLocator, handler, master, master.getMale());
        }
        if (null!= master.getUnknownGender()) {
            // If left exists
            if (null == master.getUnknownGender()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkUnknownGender(parentLocator, handler, master, master.getUnknownGender());
            }
        }
    }

    public void checkUnknownGender(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType master, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType value) {
        if (value instanceof gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) {
            gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType realValue = ((gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier verifier = new gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "UnknownGender"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "UnknownGender"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkMale(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType master, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType value) {
        if (value instanceof gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) {
            gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType realValue = ((gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier verifier = new gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Male"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Male"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFemale(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType master, gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType value) {
        if (value instanceof gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) {
            gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType realValue = ((gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportRacialCategoryDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier verifier = new gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.verification.PHSInclusionEnrollmentReportRacialCategoryDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Female"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Female"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.phs_inclusion_enrollment_report_v1_0.PHSInclusionEnrollmentReportEthnicCategoryDataType) object));
    }

}
