//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sflll_v1_1.verification;

public class AwardeeDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType master) {
        if (null == master.getOrganizationName()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OrganizationName"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkOrganizationName(parentLocator, handler, master, master.getOrganizationName());
        }
        if (null == master.getAddress()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Address"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkAddress(parentLocator, handler, master, master.getAddress());
        }
        if (null!= master.getCongressionalDistrict()) {
            // If left exists
            if (null == master.getCongressionalDistrict()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkCongressionalDistrict(parentLocator, handler, master, master.getCongressionalDistrict());
            }
        }
    }

    public void checkCongressionalDistrict(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType master, java.lang.String value) {
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
                if (((null == realValue)? 0 :realValue.length())<= 6) {
                    // Value length is correct
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 6);
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CongressionalDistrict"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CongressionalDistrict"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkAddress(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType master, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType value) {
        if (value instanceof gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType) {
            gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType realValue = ((gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sflll_v1_1.verification.AwardeeDataTypeVerifier.AddressTypeVerifier verifier = new gov.grants.apply.forms.sflll_v1_1.verification.AwardeeDataTypeVerifier.AddressTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Address"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Address"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkOrganizationName(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OrganizationName"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OrganizationName"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sflll_v1_1.AwardeeDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sflll_v1_1.AwardeeDataType) object));
    }

    public static class AddressTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {

        protected java.lang.Object[] values = new java.lang.Object[] {"WY: Wyoming", "WQ: Wake Island", "PR: Puerto Rico", "CA: California", "JQ: Johnston Atoll", "KS: Kansas", "PS: Trust Territory of Pacific", "MH: Marshall Islands", "CT: Connecticut", "MD: Maryland", "PW: Palau", "AE: APO/FPO Europe, Middle East, and Africa", "NM: New Mexico", "OR: Oregon", "AR: Arkansas", "VI: Virgin Islands of the U.S.", "KQ: Kingman Reef", "TN: Tennessee", "WA: Washington", "AP: APO/FPO Korea, Japan, Philippines, Other Pacific", "MS: Mississippi", "AK: Alaska", "NJ: New Jersey", "FM: Federated States of Micronesia", "KY: Kentucky", "ID: Idaho", "OH: Ohio", "MN: Minnesota", "NC: North Carolina", "NV: Nevada", "IA: Iowa", "RI: Rhode Island", "FL: Florida", "FQ: Baker Island", "MO: Missouri", "HI: Hawaii", "UT: Utah", "NE: Nebraska", "WV: West Virginia", "ME: Maine", "VT: Vermont", "BQ: Navassa Island", "IN: Indiana", "IL: Illinois", "HQ: Howard Island", "WI: Wisconsin", "DE: Delaware", "AZ: Arizona", "LQ: Palmyra Atoll", "MP: Northern Mariana Islands", "PA: Pennsylvania", "AA: APO/FPO Central and South America", "NY: New York", "AL: Alabama", "VA: Virginia", "TX: Texas", "ND: North Dakota", "MQ: Midway Islands", "OK: Oklahoma", "MA: Massachusetts", "NH: New Hampshire", "GU: Guam", "SC: South Carolina", "MT: Montana", "DC: District of Columbia", "GA: Georgia", "MI: Michigan", "SD: South Dakota", "CO: Colorado", "AS: American Samoa", "LA: Louisiana"};
        protected java.util.Set valueSet = java.util.Collections.unmodifiableSet(new java.util.HashSet(java.util.Arrays.asList(values)));

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master) {
            if (null == master.getStreet1()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Street1"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkStreet1(parentLocator, handler, master, master.getStreet1());
            }
            if (null!= master.getStreet2()) {
                // If left exists
                if (null == master.getStreet2()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkStreet2(parentLocator, handler, master, master.getStreet2());
                }
            }
            if (null == master.getCity()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "City"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkCity(parentLocator, handler, master, master.getCity());
            }
            if (null!= master.getState()) {
                // If left exists
                if (null == master.getState()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkState(parentLocator, handler, master, master.getState());
                }
            }
            if (null!= master.getZipPostalCode()) {
                // If left exists
                if (null == master.getZipPostalCode()) {
                    // Optional field - nothing to report
                } else {
                    // Check value
                    checkZipPostalCode(parentLocator, handler, master, master.getZipPostalCode());
                }
            }
        }

        public void checkStreet2(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master, java.lang.String value) {
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
                    if (((null == realValue)? 0 :realValue.length())<= 55) {
                        // Value length is correct
                    } else {
                        problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 55);
                    }
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Street2"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Street2"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void checkZipPostalCode(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master, java.lang.String value) {
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
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ZipPostalCode"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ZipPostalCode"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void checkState(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master, java.lang.String value) {
            if (value instanceof java.lang.String) {
                java.lang.String realValue = ((java.lang.String) value);
                // Check primitive value
                {
                    // Perform the check
                    // Checking class com.sun.msv.datatype.xsd.EnumerationFacet datatype
                    de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                    if (valueSet.contains(realValue)) {
                        // Value is found in the enumeration, it is valid
                    } else {
                        problem = new de.fzi.dbs.verification.event.datatype.EnumerationProblem(realValue, valueSet);
                    }
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "State"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "State"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void checkCity(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master, java.lang.String value) {
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
                    if (((null == realValue)? 0 :realValue.length())<= 35) {
                        // Value length is correct
                    } else {
                        problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 35);
                    }
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "City"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "City"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void checkStreet1(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType master, java.lang.String value) {
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
                    if (((null == realValue)? 0 :realValue.length())<= 55) {
                        // Value length is correct
                    } else {
                        problem = new de.fzi.dbs.verification.event.datatype.TooLongProblem(realValue, ((null == realValue)? 0 :realValue.length()), 55);
                    }
                    if (null!= problem) {
                        // Handle event
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Street1"), problem));
                    }
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Street1"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.sflll_v1_1.AwardeeDataType.AddressType) object));
        }

    }

}
