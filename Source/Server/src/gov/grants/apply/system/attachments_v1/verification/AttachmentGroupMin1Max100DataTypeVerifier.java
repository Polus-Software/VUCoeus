//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.attachments_v1.verification;

public class AttachmentGroupMin1Max100DataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType master) {
        if (null == master.getAttachedFile()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttachedFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check count
            if (master.getAttachedFile().size()< 1) {
                // Report minimum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttachedFile"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getAttachedFile().size(), 1)));
            }
            if (master.getAttachedFile().size()> 100) {
                // Report maximum of occurences violated
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttachedFile"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getAttachedFile().size(), 100)));
            }
            // Check value
            checkAttachedFile(parentLocator, handler, master, master.getAttachedFile());
        }
    }

    public void checkAttachedFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType master, java.util.List values) {
        for (int index = 0; (index<values.size()); index ++) {
            java.lang.Object item = values.get(index);
            checkAttachedFile(parentLocator, handler, master, index, item);
        }
    }

    public void checkAttachedFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType master, int index, java.lang.Object value) {
        if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
            gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
            {
                // Check complex value
                gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "AttachedFile", index), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "AttachedFile", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.system.attachments_v1.AttachmentGroupMin1Max100DataType) object));
    }

}
