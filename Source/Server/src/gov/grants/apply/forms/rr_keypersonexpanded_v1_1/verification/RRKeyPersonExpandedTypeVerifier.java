//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification;

public class RRKeyPersonExpandedTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null == master.getPDPI()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PDPI"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkPDPI(parentLocator, handler, master, master.getPDPI());
        }
        if (null == master.getKeyPerson()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPerson"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getKeyPerson().size()< 0) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPerson"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getKeyPerson().size(), 0)));
            }
            if (master.getKeyPerson().size()> 39) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPerson"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getKeyPerson().size(), 39)));
            }
            // Check value
            checkKeyPerson(parentLocator, handler, master, master.getKeyPerson());
        }
        if (null!= master.getKeyPersonAttachmentFileName1()) {
            // If left exists
            if (null == master.getKeyPersonAttachmentFileName1()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkKeyPersonAttachmentFileName1(parentLocator, handler, master, master.getKeyPersonAttachmentFileName1());
            }
        }
        if (null!= master.getKeyPersonAttachmentFileName2()) {
            // If left exists
            if (null == master.getKeyPersonAttachmentFileName2()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkKeyPersonAttachmentFileName2(parentLocator, handler, master, master.getKeyPersonAttachmentFileName2());
            }
        }
        if (null!= master.getKeyPersonAttachmentFileName3()) {
            // If left exists
            if (null == master.getKeyPersonAttachmentFileName3()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkKeyPersonAttachmentFileName3(parentLocator, handler, master, master.getKeyPersonAttachmentFileName3());
            }
        }
        if (null!= master.getKeyPersonAttachmentFileName4()) {
            // If left exists
            if (null == master.getKeyPersonAttachmentFileName4()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkKeyPersonAttachmentFileName4(parentLocator, handler, master, master.getKeyPersonAttachmentFileName4());
            }
        }
        if (null!= master.getAdditionalProfilesAttached()) {
            // If left exists
            if (null == master.getAdditionalProfilesAttached()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkAdditionalProfilesAttached(parentLocator, handler, master, master.getAdditionalProfilesAttached());
            }
        }
        if (null!= master.getBioSketchsAttached()) {
            // If left exists
            if (null == master.getBioSketchsAttached()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBioSketchsAttached(parentLocator, handler, master, master.getBioSketchsAttached());
            }
        }
        if (null!= master.getSupportsAttached()) {
            // If left exists
            if (null == master.getSupportsAttached()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkSupportsAttached(parentLocator, handler, master, master.getSupportsAttached());
            }
        }
    }

    public void checkPDPI(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType value) {
        if (value instanceof gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType) {
            gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType realValue = ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.PersonProfileDataTypeVerifier verifier = new gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.PersonProfileDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PDPI"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PDPI"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkKeyPersonAttachmentFileName2(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName2"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName2"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkKeyPersonAttachmentFileName4(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName4"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName4"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.lang.String value) {
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

    public void checkSupportsAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType value) {
        if (value instanceof gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType) {
            gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType realValue = ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.SupportsAttachedTypeVerifier verifier = new gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.SupportsAttachedTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SupportsAttached"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SupportsAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBioSketchsAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType value) {
        if (value instanceof gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType) {
            gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType realValue = ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.BioSketchsAttachedTypeVerifier verifier = new gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.BioSketchsAttachedTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BioSketchsAttached"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BioSketchsAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkKeyPerson(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkKeyPerson(parentLocator, handler, master, index, item);
        }
    }

    public void checkKeyPerson(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType) {
            gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType realValue = ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.PersonProfileDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.PersonProfileDataTypeVerifier verifier = new gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.PersonProfileDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "KeyPerson", index), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "KeyPerson", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkKeyPersonAttachmentFileName1(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName1"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName1"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkKeyPersonAttachmentFileName3(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName3"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "KeyPersonAttachmentFileName3"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkAdditionalProfilesAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType master, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType value) {
        if (value instanceof gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType) {
            gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType realValue = ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.AdditionalProfilesAttachedTypeVerifier verifier = new gov.grants.apply.forms.rr_keypersonexpanded_v1_1.verification.RRKeyPersonExpandedTypeVerifier.AdditionalProfilesAttachedTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AdditionalProfilesAttached"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AdditionalProfilesAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType) object));
    }

    public static class AdditionalProfilesAttachedTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType master) {
            if (null!= master.getAdditionalProfileAttached()) {
                // If left exists
                if (null == master.getAdditionalProfileAttached()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkAdditionalProfileAttached(parentLocator, handler, master, master.getAdditionalProfileAttached());
                }
            }
        }

        public void checkAdditionalProfileAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AdditionalProfileAttached"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AdditionalProfileAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.AdditionalProfilesAttachedType) object));
        }

    }

    public static class BioSketchsAttachedTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType master) {
            if (null == master.getBioSketchAttached()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BioSketchAttached"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkBioSketchAttached(parentLocator, handler, master, master.getBioSketchAttached());
            }
        }

        public void checkBioSketchAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BioSketchAttached"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BioSketchAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.BioSketchsAttachedType) object));
        }

    }

    public static class SupportsAttachedTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType master) {
            if (null!= master.getSupportAttached()) {
                // If left exists
                if (null == master.getSupportAttached()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkSupportAttached(parentLocator, handler, master, master.getSupportAttached());
                }
            }
        }

        public void checkSupportAttached(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SupportAttached"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SupportAttached"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.rr_keypersonexpanded_v1_1.RRKeyPersonExpandedType.SupportsAttachedType) object));
        }

    }

}
