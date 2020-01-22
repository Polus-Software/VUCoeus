//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.12 at 03:28:23 IST 
//


package gov.grants.apply.system.metagrantapplication.verification;

public class GrantApplicationTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType master) {
        if (null!= master.getGrantSubmissionHeader()) {
            // If left exists
            if (null == master.getGrantSubmissionHeader()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkGrantSubmissionHeader(parentLocator, handler, master, master.getGrantSubmissionHeader());
            }
        }
        if (null!= master.getHeader20()) {
            // If left exists
            if (null == master.getHeader20()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkHeader20(parentLocator, handler, master, master.getHeader20());
            }
        }
        if (null == master.getForms()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Forms"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkForms(parentLocator, handler, master, master.getForms());
        }
        if (null!= master.getGrantSubmissionFooter()) {
            // If left exists
            if (null == master.getGrantSubmissionFooter()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkGrantSubmissionFooter(parentLocator, handler, master, master.getGrantSubmissionFooter());
            }
        }
    }

    public void checkGrantSubmissionFooter(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType master, gov.grants.apply.system.footer_v1.GrantSubmissionFooterType value) {
        if (value instanceof gov.grants.apply.system.footer_v1.GrantSubmissionFooterType) {
            gov.grants.apply.system.footer_v1.GrantSubmissionFooterType realValue = ((gov.grants.apply.system.footer_v1.GrantSubmissionFooterType) value);
            {
                // Check complex value
                gov.grants.apply.system.footer_v1.verification.GrantSubmissionFooterTypeVerifier verifier = new gov.grants.apply.system.footer_v1.verification.GrantSubmissionFooterTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionFooter"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.system.footer_v1.GrantSubmissionFooter) {
                gov.grants.apply.system.footer_v1.GrantSubmissionFooter realValue = ((gov.grants.apply.system.footer_v1.GrantSubmissionFooter) value);
                {
                    // Check complex value
                    gov.grants.apply.system.footer_v1.verification.GrantSubmissionFooterVerifier verifier = new gov.grants.apply.system.footer_v1.verification.GrantSubmissionFooterVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionFooter"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionFooter"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkForms(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType master, gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType value) {
        if (value instanceof gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType) {
            gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType realValue = ((gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType) value);
            {
                // Check complex value
                gov.grants.apply.system.metagrantapplication.verification.GrantApplicationTypeVerifier.FormsTypeVerifier verifier = new gov.grants.apply.system.metagrantapplication.verification.GrantApplicationTypeVerifier.FormsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Forms"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Forms"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkHeader20(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType master, gov.grants.apply.system.header_2_0_v2.Header20Type value) {
        if (value instanceof gov.grants.apply.system.header_2_0_v2.Header20Type) {
            gov.grants.apply.system.header_2_0_v2.Header20Type realValue = ((gov.grants.apply.system.header_2_0_v2.Header20Type) value);
            {
                // Check complex value
                gov.grants.apply.system.header_2_0_v2.verification.Header20TypeVerifier verifier = new gov.grants.apply.system.header_2_0_v2.verification.Header20TypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Header20"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.system.header_2_0_v2.Header20) {
                gov.grants.apply.system.header_2_0_v2.Header20 realValue = ((gov.grants.apply.system.header_2_0_v2.Header20) value);
                {
                    // Check complex value
                    gov.grants.apply.system.header_2_0_v2.verification.Header20Verifier verifier = new gov.grants.apply.system.header_2_0_v2.verification.Header20Verifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Header20"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Header20"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkGrantSubmissionHeader(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType master, gov.grants.apply.system.header_v1.GrantSubmissionHeaderType value) {
        if (value instanceof gov.grants.apply.system.header_v1.GrantSubmissionHeaderType) {
            gov.grants.apply.system.header_v1.GrantSubmissionHeaderType realValue = ((gov.grants.apply.system.header_v1.GrantSubmissionHeaderType) value);
            {
                // Check complex value
                gov.grants.apply.system.header_v1.verification.GrantSubmissionHeaderTypeVerifier verifier = new gov.grants.apply.system.header_v1.verification.GrantSubmissionHeaderTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionHeader"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.system.header_v1.GrantSubmissionHeader) {
                gov.grants.apply.system.header_v1.GrantSubmissionHeader realValue = ((gov.grants.apply.system.header_v1.GrantSubmissionHeader) value);
                {
                    // Check complex value
                    gov.grants.apply.system.header_v1.verification.GrantSubmissionHeaderVerifier verifier = new gov.grants.apply.system.header_v1.verification.GrantSubmissionHeaderVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionHeader"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "GrantSubmissionHeader"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.system.metagrantapplication.GrantApplicationType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.system.metagrantapplication.GrantApplicationType) object));
    }

    public static class FormsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType master) {
            if (null == master.getAny()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Any"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check count
                if (master.getAny().size()< 1) {
                    // Report minimum of occurences violated
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Any"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getAny().size(), 1)));
                }
                // Check value
                checkAny(parentLocator, handler, master, master.getAny());
            }
        }

        public void checkAny(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType master, java.util.List values) {
            for (int index = 0; (index<values.size()); index ++) {
                java.lang.Object item = values.get(index);
                checkAny(parentLocator, handler, master, index, item);
            }
        }

        public void checkAny(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType master, int index, java.lang.Object value) {
            if (value instanceof java.lang.Object) {
                java.lang.Object realValue = ((java.lang.Object) value);
                // Type item class com.sun.tools.xjc.grammar.ext.WildcardItem is not supported
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "Any", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.system.metagrantapplication.GrantApplicationType.FormsType) object));
        }

    }

}