//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_modularbudget_v1_1.verification;

public class PHS398ModularBudgetTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null!= master.getPeriods()) {
            // If left exists
            if (null == master.getPeriods()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkPeriods(parentLocator, handler, master, master.getPeriods());
            }
        }
        if (null!= master.getPeriods2()) {
            // If left exists
            if (null == master.getPeriods2()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkPeriods2(parentLocator, handler, master, master.getPeriods2());
            }
        }
        if (null!= master.getPeriods3()) {
            // If left exists
            if (null == master.getPeriods3()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkPeriods3(parentLocator, handler, master, master.getPeriods3());
            }
        }
        if (null!= master.getPeriods4()) {
            // If left exists
            if (null == master.getPeriods4()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkPeriods4(parentLocator, handler, master, master.getPeriods4());
            }
        }
        if (null!= master.getPeriods5()) {
            // If left exists
            if (null == master.getPeriods5()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkPeriods5(parentLocator, handler, master, master.getPeriods5());
            }
        }
        if (null == master.getCummulativeBudgetInfo()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CummulativeBudgetInfo"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCummulativeBudgetInfo(parentLocator, handler, master, master.getCummulativeBudgetInfo());
        }
    }

    public void checkPeriods5(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.Period5Type) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period5TypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period5TypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods5"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods5"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, java.lang.String value) {
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

    public void checkCummulativeBudgetInfo(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.CumBudgetType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.CumBudgetTypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.CumBudgetTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CummulativeBudgetInfo"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CummulativeBudgetInfo"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkPeriods4(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.Period4Type) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period4TypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period4TypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods4"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods4"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkPeriods2(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.Period2Type) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period2TypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period2TypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods2"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods2"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkPeriods3(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.Period3Type) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period3TypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.Period3TypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods3"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods3"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkPeriods(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType master, gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType value) {
        if (value instanceof gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType) {
            gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType realValue = ((gov.grants.apply.forms.phs398_modularbudget_v1_1.PeriodType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.PeriodTypeVerifier verifier = new gov.grants.apply.forms.phs398_modularbudget_v1_1.verification.PeriodTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "Periods"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.phs398_modularbudget_v1_1.PHS398ModularBudgetType) object));
    }

}