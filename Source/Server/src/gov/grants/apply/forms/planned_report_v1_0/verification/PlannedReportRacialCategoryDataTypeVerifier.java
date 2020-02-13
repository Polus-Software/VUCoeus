//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.09 at 02:29:50 CDT 
//


package gov.grants.apply.forms.planned_report_v1_0.verification;

public class PlannedReportRacialCategoryDataTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master) {
        // No check for primitive values
        checkAmericanIndian(parentLocator, handler, master, new java.lang.Integer(master.getAmericanIndian()));
        // No check for primitive values
        checkAsian(parentLocator, handler, master, new java.lang.Integer(master.getAsian()));
        // No check for primitive values
        checkHawaiian(parentLocator, handler, master, new java.lang.Integer(master.getHawaiian()));
        // No check for primitive values
        checkBlack(parentLocator, handler, master, new java.lang.Integer(master.getBlack()));
        // No check for primitive values
        checkWhite(parentLocator, handler, master, new java.lang.Integer(master.getWhite()));
        // No check for primitive values
        checkMultipleRace(parentLocator, handler, master, new java.lang.Integer(master.getMultipleRace()));
        if (null == master.getTotal()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Total"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkTotal(parentLocator, handler, master, master.getTotal());
        }
    }

    public void checkMultipleRace(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkWhite(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkAsian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkAmericanIndian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkBlack(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkHawaiian(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.lang.Integer value) {
        if (value instanceof java.lang.Integer) {
            java.lang.Integer realValue = ((java.lang.Integer) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.MaxInclusiveFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(0))>= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.LessProblem(realValue, new java.lang.Integer(0));
                }
                if (((java.lang.Comparable) realValue).compareTo(new java.lang.Integer(999999999))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.lang.Integer(999999999));
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

    public void checkTotal(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType master, java.math.BigInteger value) {
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
        check(parentLocator, handler, ((gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.planned_report_v1_0.PlannedReportRacialCategoryDataType) object));
    }

}
