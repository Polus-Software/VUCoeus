//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424b_v1.verification;

public class AssuranceTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master) {
        if (null == master.getProgramType()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramType"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkProgramType(parentLocator, handler, master, master.getProgramType());
        }
        if (null == master.getCoreSchemaVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoreSchemaVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCoreSchemaVersion(parentLocator, handler, master, master.getCoreSchemaVersion());
        }
        if (null == master.getFormVersionIdentifier()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersionIdentifier(parentLocator, handler, master, master.getFormVersionIdentifier());
        }
        if (null!= master.getAuthorizedRepresentative()) {
            // If left exists
            if (null == master.getAuthorizedRepresentative()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkAuthorizedRepresentative(parentLocator, handler, master, master.getAuthorizedRepresentative());
            }
        }
        if (null!= master.getApplicantOrganizationName()) {
            // If left exists
            if (null == master.getApplicantOrganizationName()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkApplicantOrganizationName(parentLocator, handler, master, master.getApplicantOrganizationName());
            }
        }
        if (null!= master.getSubmittedDate()) {
            // If left exists
            if (null == master.getSubmittedDate()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkSubmittedDate(parentLocator, handler, master, master.getSubmittedDate());
            }
        }
    }

    public void checkSubmittedDate(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, java.util.Calendar value) {
        if (value instanceof java.util.Calendar) {
            java.util.Calendar realValue = ((java.util.Calendar) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.DateType datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SubmittedDate"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SubmittedDate"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkProgramType(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramType"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkApplicantOrganizationName(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, java.lang.String value) {
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
                if (((null == realValue)? 0 :realValue.length())<= 60) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 60);
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ApplicantOrganizationName"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ApplicantOrganizationName"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkCoreSchemaVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoreSchemaVersion"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersionIdentifier(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, java.lang.String value) {
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
                if (((null == realValue)? 0 :realValue.length())<= 30) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 30);
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkAuthorizedRepresentative(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424b_v1.AssuranceType master, gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType value) {
        if (value instanceof gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType) {
            gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType realValue = ((gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentativeType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424b_v1.verification.AuthorizedRepresentativeTypeVerifier verifier = new gov.grants.apply.forms.sf424b_v1.verification.AuthorizedRepresentativeTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AuthorizedRepresentative"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentative) {
                gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentative realValue = ((gov.grants.apply.forms.sf424b_v1.AuthorizedRepresentative) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424b_v1.verification.AuthorizedRepresentativeVerifier verifier = new gov.grants.apply.forms.sf424b_v1.verification.AuthorizedRepresentativeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AuthorizedRepresentative"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AuthorizedRepresentative"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424b_v1.AssuranceType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424b_v1.AssuranceType) object));
    }

}
