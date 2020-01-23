//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.19 at 02:55:10 EDT 
//


package gov.grants.apply.forms.performancesite_1_3_v1_3.verification;

public class PerformanceSite13TypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null == master.getPrimarySite()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PrimarySite"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkPrimarySite(parentLocator, handler, master, master.getPrimarySite());
        }
        if (null == master.getOtherSite()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherSite"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getOtherSite().size()< 0) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherSite"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getOtherSite().size(), 0)));
            }
            if (master.getOtherSite().size()> 29) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherSite"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getOtherSite().size(), 29)));
            }
            // Check value
            checkOtherSite(parentLocator, handler, master, master.getOtherSite());
        }
        if (null!= master.getAttachedFile()) {
            // If left exists
            if (null == master.getAttachedFile()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkAttachedFile(parentLocator, handler, master, master.getAttachedFile());
            }
        }
    }

    public void checkPrimarySite(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master, gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType value) {
        if (value instanceof gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType) {
            gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType realValue = ((gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.performancesite_1_3_v1_3.verification.SiteLocationDataTypeVerifier verifier = new gov.grants.apply.forms.performancesite_1_3_v1_3.verification.SiteLocationDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PrimarySite"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "PrimarySite"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master, java.lang.String value) {
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

    public void checkAttachedFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
        if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
            gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
            {
                // Check complex value
                gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttachedFile"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttachedFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkOtherSite(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkOtherSite(parentLocator, handler, master, index, item);
        }
    }

    public void checkOtherSite(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType) {
            gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType realValue = ((gov.grants.apply.forms.performancesite_1_3_v1_3.SiteLocationDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.performancesite_1_3_v1_3.verification.SiteLocationDataTypeVerifier verifier = new gov.grants.apply.forms.performancesite_1_3_v1_3.verification.SiteLocationDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "OtherSite", index), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "OtherSite", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.performancesite_1_3_v1_3.PerformanceSite13Type) object));
    }

}
