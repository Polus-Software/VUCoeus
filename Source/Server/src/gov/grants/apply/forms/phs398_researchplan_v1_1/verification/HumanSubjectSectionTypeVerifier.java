//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_researchplan_v1_1.verification;

public class HumanSubjectSectionTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType master) {
        if (null!= master.getProtectionOfHumanSubjects()) {
            // If left exists
            if (null == master.getProtectionOfHumanSubjects()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkProtectionOfHumanSubjects(parentLocator, handler, master, master.getProtectionOfHumanSubjects());
            }
        }
        if (null!= master.getInclusionOfWomenAndMinorities()) {
            // If left exists
            if (null == master.getInclusionOfWomenAndMinorities()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkInclusionOfWomenAndMinorities(parentLocator, handler, master, master.getInclusionOfWomenAndMinorities());
            }
        }
        if (null!= master.getTargetedPlannedEnrollmentTable()) {
            // If left exists
            if (null == master.getTargetedPlannedEnrollmentTable()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkTargetedPlannedEnrollmentTable(parentLocator, handler, master, master.getTargetedPlannedEnrollmentTable());
            }
        }
        if (null!= master.getInclusionOfChildren()) {
            // If left exists
            if (null == master.getInclusionOfChildren()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkInclusionOfChildren(parentLocator, handler, master, master.getInclusionOfChildren());
            }
        }
    }

    public void checkTargetedPlannedEnrollmentTable(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType master, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType) {
            gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType realValue = ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.TargetedPlannedEnrollmentTableTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.TargetedPlannedEnrollmentTableTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TargetedPlannedEnrollmentTable"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TargetedPlannedEnrollmentTable"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkProtectionOfHumanSubjects(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType master, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType) {
            gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType realValue = ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.ProtectionOfHumanSubjectsTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.ProtectionOfHumanSubjectsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProtectionOfHumanSubjects"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProtectionOfHumanSubjects"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkInclusionOfChildren(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType master, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType) {
            gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType realValue = ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.InclusionOfChildrenTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.InclusionOfChildrenTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "InclusionOfChildren"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "InclusionOfChildren"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkInclusionOfWomenAndMinorities(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType master, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType) {
            gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType realValue = ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.InclusionOfWomenAndMinoritiesTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_v1_1.verification.HumanSubjectSectionTypeVerifier.InclusionOfWomenAndMinoritiesTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "InclusionOfWomenAndMinorities"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "InclusionOfWomenAndMinorities"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType) object));
    }

    public static class InclusionOfChildrenTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfChildrenType) object));
        }

    }

    public static class InclusionOfWomenAndMinoritiesTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType) object));
        }

    }

    public static class ProtectionOfHumanSubjectsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.ProtectionOfHumanSubjectsType) object));
        }

    }

    public static class TargetedPlannedEnrollmentTableTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_v1_1.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType) object));
        }

    }

}