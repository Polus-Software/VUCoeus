//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.11 at 11:55:52 CDT 
//


package gov.grants.apply.forms.cumulative_inclusion_report_v1_0.verification;

public class PHS398CumulativeInclusionReportTotalsDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master) {
        if (null == master.getAmericanIndian()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AmericanIndian"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkAmericanIndian(parentLocator, handler, master, master.getAmericanIndian());
        }
        if (null == master.getAsian()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Asian"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkAsian(parentLocator, handler, master, master.getAsian());
        }
        if (null == master.getHawaiian()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Hawaiian"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkHawaiian(parentLocator, handler, master, master.getHawaiian());
        }
        if (null == master.getBlack()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Black"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkBlack(parentLocator, handler, master, master.getBlack());
        }
        if (null == master.getWhite()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "White"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkWhite(parentLocator, handler, master, master.getWhite());
        }
        if (null == master.getMultipleRace()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MultipleRace"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkMultipleRace(parentLocator, handler, master, master.getMultipleRace());
        }
        if (null == master.getUnknownRace()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "UnknownRace"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkUnknownRace(parentLocator, handler, master, master.getUnknownRace());
        }
        if (null == master.getTotal()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Total"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkTotal(parentLocator, handler, master, master.getTotal());
        }
    }

    public void checkMultipleRace(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MultipleRace"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MultipleRace"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkWhite(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "White"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "White"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkUnknownRace(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "UnknownRace"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "UnknownRace"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkAsian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Asian"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Asian"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkAmericanIndian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AmericanIndian"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AmericanIndian"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBlack(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Black"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Black"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkHawaiian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("9999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("9999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Hawaiian"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Hawaiian"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkTotal(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType master, java.math.BigInteger value) {
        if (value instanceof java.math.BigInteger) {
            java.math.BigInteger realValue = ((java.math.BigInteger) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("0"))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.math.BigInteger("0"));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("99999999999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("99999999999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Total"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Total"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.cumulative_inclusion_report_v1_0.PHS398CumulativeInclusionReportTotalsDataType) object));
    }

}
