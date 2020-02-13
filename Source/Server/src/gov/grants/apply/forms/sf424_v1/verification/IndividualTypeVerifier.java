//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1.verification;

public class IndividualTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.IndividualType master) {
        if (null!= master.getContact()) {
            // If left exists
            if (null == master.getContact()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkContact(parentLocator, handler, master, master.getContact());
            }
        }
        if (null == master.getAuthorizedRepresentative()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AuthorizedRepresentative"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkAuthorizedRepresentative(parentLocator, handler, master, master.getAuthorizedRepresentative());
        }
    }

    public void checkContact(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.IndividualType master, gov.grants.apply.forms.sf424_v1.ContactType value) {
        if (value instanceof gov.grants.apply.forms.sf424_v1.ContactType) {
            gov.grants.apply.forms.sf424_v1.ContactType realValue = ((gov.grants.apply.forms.sf424_v1.ContactType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424_v1.verification.ContactTypeVerifier verifier = new gov.grants.apply.forms.sf424_v1.verification.ContactTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Contact"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424_v1.Contact) {
                gov.grants.apply.forms.sf424_v1.Contact realValue = ((gov.grants.apply.forms.sf424_v1.Contact) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424_v1.verification.ContactVerifier verifier = new gov.grants.apply.forms.sf424_v1.verification.ContactVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Contact"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Contact"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkAuthorizedRepresentative(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.IndividualType master, gov.grants.apply.forms.sf424_v1.AuthorizedRepresentativeType value) {
        if (value instanceof gov.grants.apply.forms.sf424_v1.AuthorizedRepresentativeType) {
            gov.grants.apply.forms.sf424_v1.AuthorizedRepresentativeType realValue = ((gov.grants.apply.forms.sf424_v1.AuthorizedRepresentativeType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424_v1.verification.AuthorizedRepresentativeTypeVerifier verifier = new gov.grants.apply.forms.sf424_v1.verification.AuthorizedRepresentativeTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AuthorizedRepresentative"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424_v1.AuthorizedRepresentative) {
                gov.grants.apply.forms.sf424_v1.AuthorizedRepresentative realValue = ((gov.grants.apply.forms.sf424_v1.AuthorizedRepresentative) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424_v1.verification.AuthorizedRepresentativeVerifier verifier = new gov.grants.apply.forms.sf424_v1.verification.AuthorizedRepresentativeVerifier();
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
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424_v1.IndividualType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424_v1.IndividualType) object));
    }

}
