//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.24 at 10:09:00 CDT 
//


package gov.grants.apply.forms.humansubjectstudy_v1.verification;

public class HumanSubjectStudyEthnicCategoryDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyEthnicCategoryDataType master) {
        if (null!= master.getFemale()) {
            // If left exists
            if (null == master.getFemale()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkFemale(parentLocator, handler, master, master.getFemale());
            }
        }
        if (null!= master.getMale()) {
            // If left exists
            if (null == master.getMale()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkMale(parentLocator, handler, master, master.getMale());
            }
        }
    }

    public void checkMale(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyEthnicCategoryDataType master, gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType value) {
        if (value instanceof gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType) {
            gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType realValue = ((gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.humansubjectstudy_v1.verification.HumanSubjectStudyRacialCategoryDataTypeVerifier verifier = new gov.grants.apply.forms.humansubjectstudy_v1.verification.HumanSubjectStudyRacialCategoryDataTypeVerifier();
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

    public void checkFemale(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyEthnicCategoryDataType master, gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType value) {
        if (value instanceof gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType) {
            gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType realValue = ((gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyRacialCategoryDataType) value);
            {
                // Check complex value
                gov.grants.apply.forms.humansubjectstudy_v1.verification.HumanSubjectStudyRacialCategoryDataTypeVerifier verifier = new gov.grants.apply.forms.humansubjectstudy_v1.verification.HumanSubjectStudyRacialCategoryDataTypeVerifier();
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
        check(parentLocator, handler, ((gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyEthnicCategoryDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.humansubjectstudy_v1.HumanSubjectStudyEthnicCategoryDataType) object));
    }

}