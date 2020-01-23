//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.5-b16-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.02.13 at 11:23:34 AM EST 
//


package gov.grants.apply.forms.sf424_v1.verification;

public class BudgetTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{

    protected java.lang.Object[] values = new java.lang.Object[] {"BGL", "KWD", "NLG", "ANG", "HNL", "MYR", "UYP", "COP", "PHP", "MRO", "INR", "ISK", "CRC", "DZD", "GBP", "CLP", "JMD", "QAR", "LRD", "PLZ", "GIP", "AFA", "MUR", "CAD", "BMD", "SRG", "ZMK", "IRR", "BZD", "SBD", "MNT", "TPE", "FIM", "TOP", "TTD", "GMD", "KYD", "SYP", "PGK", "THB", "LAK", "EGP", "SHP", "NIC", "VUV", "JOD", "KRW", "AOK", "PYG", "BBD", "LUF", "GYD", "SUR", "ADP", "RWF", "CHF", "AED", "MWK", "LBP", "TWD", "HTG", "NPR", "LSL", "LKR", "IQD", "MXP", "PKR", "YUD", "ILS", "MAD", "BOB", "BHD", "JPY", "PEI", "DDM", "GWP", "SCR", "BTN", "SZL", "STD", "FJD", "BSD", "ALL", "KES", "BUK", "SAR", "OMR", "ARA", "KPW", "SOS", "NZD", "MGF", "ROL", "BEF", "PTE", "YDD", "SGD", "CSK", "BND", "ATS", "UGS", "CUP", "GTQ", "TND", "ZAR", "HUF", "PAB", "BIF", "WST", "DJF", "MOP", "TRL", "ECS", "KMF", "IEP", "TZS", "SEK", "HKD", "GNF", "MTL", "CVE", "NOK", "FRF", "ETB", "IDR", "ZRZ", "SLL", "BWP", "SDP", "DKK", "CNY", "NGN", "VND", "VEB", "MZM", "GRD", "BDT", "CYP", "YER", "AUD", "FKP", "SVC", "EUR", "USD", "GHC", "MVR", "KHR", "ESP", "CLF", "LYD", "ITL", "AWG", "BRC", "ZWD", "DOP", "DEM"};
    protected java.util.Set valueSet = java.util.Collections.unmodifiableSet(new java.util.HashSet(java.util.Arrays.asList(values)));

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master) {
        if (null == master.getCurrencyCode()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CurrencyCode"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCurrencyCode(parentLocator, handler, master, master.getCurrencyCode());
        }
        if (null == master.getFederalEstimatedAmount()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalEstimatedAmount"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFederalEstimatedAmount(parentLocator, handler, master, master.getFederalEstimatedAmount());
        }
        if (null!= master.getApplicantEstimatedAmount()) {
            // If left exists
            if (null == master.getApplicantEstimatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkApplicantEstimatedAmount(parentLocator, handler, master, master.getApplicantEstimatedAmount());
            }
        }
        if (null!= master.getStateEstimatedAmount()) {
            // If left exists
            if (null == master.getStateEstimatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkStateEstimatedAmount(parentLocator, handler, master, master.getStateEstimatedAmount());
            }
        }
        if (null!= master.getLocalEstimatedAmount()) {
            // If left exists
            if (null == master.getLocalEstimatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkLocalEstimatedAmount(parentLocator, handler, master, master.getLocalEstimatedAmount());
            }
        }
        if (null!= master.getOtherEstimatedAmount()) {
            // If left exists
            if (null == master.getOtherEstimatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkOtherEstimatedAmount(parentLocator, handler, master, master.getOtherEstimatedAmount());
            }
        }
        if (null!= master.getProgramIncomeEstimatedAmount()) {
            // If left exists
            if (null == master.getProgramIncomeEstimatedAmount()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkProgramIncomeEstimatedAmount(parentLocator, handler, master, master.getProgramIncomeEstimatedAmount());
            }
        }
        if (null == master.getTotalEstimatedAmount()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalEstimatedAmount"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkTotalEstimatedAmount(parentLocator, handler, master, master.getTotalEstimatedAmount());
        }
    }

    public void checkLocalEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "LocalEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "LocalEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFederalEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkApplicantEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ApplicantEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ApplicantEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkTotalEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "TotalEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkCurrencyCode(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CurrencyCode"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CurrencyCode"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkStateEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "StateEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "StateEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkProgramIncomeEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramIncomeEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramIncomeEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkOtherEstimatedAmount(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424_v1.BudgetType master, java.math.BigDecimal value) {
        if (value instanceof java.math.BigDecimal) {
            java.math.BigDecimal realValue = ((java.math.BigDecimal) value);
            // Check primitive value
            {
                // Perform the check
                // Checking class com.sun.msv.datatype.xsd.FractionDigitsFacet datatype
                de.fzi.dbs.verification.event.datatype.ValueProblem problem = null;
                // todo: Check lexical constraints. How???
                // todo: Check lexical constraints. How???
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherEstimatedAmount"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherEstimatedAmount"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424_v1.BudgetType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424_v1.BudgetType) object));
    }

}
