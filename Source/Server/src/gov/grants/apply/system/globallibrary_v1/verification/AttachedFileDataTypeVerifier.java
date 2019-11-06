//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.system.globallibrary_v1.verification;

public class AttachedFileDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType master) {
        if (null == master.getFileName()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileName"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFileName(parentLocator, handler, master, master.getFileName());
        }
        if (null == master.getMimeType()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MimeType"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkMimeType(parentLocator, handler, master, master.getMimeType());
        }
        if (null == master.getFileLocation()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileLocation"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFileLocation(parentLocator, handler, master, master.getFileLocation());
        }
        if (null == master.getHashValue()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "HashValue"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkHashValue(parentLocator, handler, master, master.getHashValue());
        }
    }

    public void checkMimeType(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MimeType"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MimeType"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFileLocation(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType master, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType value) {
        if (value instanceof gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType) {
            gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType realValue = ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType) value);
            {
                // Check complex value
                gov.grants.apply.system.globallibrary_v1.verification.AttachedFileDataTypeVerifier.FileLocationTypeVerifier verifier = new gov.grants.apply.system.globallibrary_v1.verification.AttachedFileDataTypeVerifier.FileLocationTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileLocation"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileLocation"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFileName(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType master, java.lang.String value) {
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
                if (((null == realValue)? 0 :realValue.length())<= 255) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 255);
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileName"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FileName"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkHashValue(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType master, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType value) {
        if (value instanceof gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType) {
            gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType realValue = ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType) value);
            {
                // Check complex value
                gov.grants.apply.system.globallibrary_v1.verification.AttachedFileDataTypeVerifier.HashValueTypeVerifier verifier = new gov.grants.apply.system.globallibrary_v1.verification.AttachedFileDataTypeVerifier.HashValueTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "HashValue"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "HashValue"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType) object));
    }

    public static class FileLocationTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType master) {
            if (null!= master.getHref()) {
                // If left exists
                if (null == master.getHref()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkHref(parentLocator, handler, master, master.getHref());
                }
            }
        }

        public void checkHref(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType master, java.lang.String value) {
            if (value instanceof java.lang.String) {
                java.lang.String realValue = ((java.lang.String) value);
                // Check primitive value
                {
                    // Perform the check
                    // Checking class com.sun.msv.datatype.xsd.AnyURIType datatype
                    de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                    if (de.fzi.dbs.verification.util.ValidationUtils.isAnyURI(realValue)) {
                        // This value is a valid URI
                    } else {
                        problem = new de.fzi.dbs.verification.event.datatype.AnyURIProblem(realValue);
                    }
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Href"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Href"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.FileLocationType) object));
        }

    }

    public static class HashValueTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType master) {
            if (null == master.getHashAlgorithm()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "HashAlgorithm"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkHashAlgorithm(parentLocator, handler, master, master.getHashAlgorithm());
            }
            if (null == master.getValue()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkValue(parentLocator, handler, master, master.getValue());
            }
        }

        public void checkValue(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType master, byte[] value) {
            if (value instanceof byte[]) {
                byte[] realValue = ((byte[]) value);
                // Check primitive value
                {
                    // Perform the check
                    // Checking class com.sun.msv.datatype.xsd.Base64BinaryType datatype
                    de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Value"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void checkHashAlgorithm(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType master, java.lang.String value) {
            if (value instanceof java.lang.String) {
                java.lang.String realValue = ((java.lang.String) value);
                // Check primitive value
                // Primitive value is always valid, nothing to check
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "HashAlgorithm"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.system.globallibrary_v1.AttachedFileDataType.HashValueType) object));
        }

    }

}
