//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.rr_fednonfedbudget_v1.verification;

public class BudgetYear1DataTypeVerifier
    extends gov.grants.apply.forms.rr_fednonfedbudget_v1.verification.BudgetYearDataTypeVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.BudgetYear1DataType master) {
        super.check(parentLocator, handler, master);
        if (null!= master.getBudgetJustificationAttachment()) {
            // If left exists
            if (null == master.getBudgetJustificationAttachment()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetJustificationAttachment(parentLocator, handler, master, master.getBudgetJustificationAttachment());
            }
        }
    }

    public void checkBudgetJustificationAttachment(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfedbudget_v1.BudgetYear1DataType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
        if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
            gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
            {
                // Check complex value
                gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetJustificationAttachment"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetJustificationAttachment"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1.BudgetYear1DataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_fednonfedbudget_v1.BudgetYear1DataType) object));
    }

}
