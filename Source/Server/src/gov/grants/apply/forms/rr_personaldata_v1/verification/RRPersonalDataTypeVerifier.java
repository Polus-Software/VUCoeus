//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_personaldata_v1.verification;

public class RRPersonalDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null == master.getProjectDirector()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectDirector"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkProjectDirector(parentLocator, handler, master, master.getProjectDirector());
        }
        if (null == master.getCoProjectDirector()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoProjectDirector"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getCoProjectDirector().size()< 0) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoProjectDirector"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getCoProjectDirector().size(), 0)));
            }
            if (master.getCoProjectDirector().size()> 4) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoProjectDirector"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getCoProjectDirector().size(), 4)));
            }
            // Check value
            checkCoProjectDirector(parentLocator, handler, master, master.getCoProjectDirector());
        }
    }

    public void checkProjectDirector(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType master, gov.grants.apply.forms.rr_personaldata_v1.DirectorType value) {
        if (value instanceof gov.grants.apply.forms.rr_personaldata_v1.DirectorType) {
            gov.grants.apply.forms.rr_personaldata_v1.DirectorType realValue = ((gov.grants.apply.forms.rr_personaldata_v1.DirectorType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_personaldata_v1.verification.DirectorTypeVerifier verifier = new gov.grants.apply.forms.rr_personaldata_v1.verification.DirectorTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectDirector"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectDirector"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType master, java.lang.String value) {
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

    public void checkCoProjectDirector(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkCoProjectDirector(parentLocator, handler, master, index, item);
        }
    }

    public void checkCoProjectDirector(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.forms.rr_personaldata_v1.DirectorType) {
            gov.grants.apply.forms.rr_personaldata_v1.DirectorType realValue = ((gov.grants.apply.forms.rr_personaldata_v1.DirectorType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_personaldata_v1.verification.DirectorTypeVerifier verifier = new gov.grants.apply.forms.rr_personaldata_v1.verification.DirectorTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "CoProjectDirector", index), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "CoProjectDirector", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_personaldata_v1.RRPersonalDataType) object));
    }

}
