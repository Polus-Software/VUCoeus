//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_coverletter_v1_1.verification;

public class PHS398CoverLetterTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null == master.getCoverLetterFile()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCoverLetterFile(parentLocator, handler, master, master.getCoverLetterFile());
        }
    }

    public void checkCoverLetterFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType master, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType value) {
        if (value instanceof gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType) {
            gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType realValue = ((gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_coverletter_v1_1.verification.PHS398CoverLetterTypeVerifier.CoverLetterFileTypeVerifier verifier = new gov.grants.apply.forms.phs398_coverletter_v1_1.verification.PHS398CoverLetterTypeVerifier.CoverLetterFileTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFile"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType) object));
    }

    public static class CoverLetterFileTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType master) {
            if (null == master.getCoverLetterFilename()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFilename"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkCoverLetterFilename(parentLocator, handler, master, master.getCoverLetterFilename());
            }
        }

        public void checkCoverLetterFilename(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFilename"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoverLetterFilename"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_coverletter_v1_1.PHS398CoverLetterType.CoverLetterFileType) object));
        }

    }

}
