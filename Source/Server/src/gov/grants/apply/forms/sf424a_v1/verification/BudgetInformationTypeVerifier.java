//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424a_v1.verification;

public class BudgetInformationTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master) {
        if (null == master.getProgramType()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramType"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkProgramType(parentLocator, handler, master, master.getProgramType());
        }
        if (null == master.getCoreSchemaVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoreSchemaVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkCoreSchemaVersion(parentLocator, handler, master, master.getCoreSchemaVersion());
        }
        if (null == master.getFormVersionIdentifier()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersionIdentifier(parentLocator, handler, master, master.getFormVersionIdentifier());
        }
        if (null!= master.getBudgetSummary()) {
            // If left exists
            if (null == master.getBudgetSummary()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetSummary(parentLocator, handler, master, master.getBudgetSummary());
            }
        }
        if (null!= master.getBudgetCategories()) {
            // If left exists
            if (null == master.getBudgetCategories()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetCategories(parentLocator, handler, master, master.getBudgetCategories());
            }
        }
        if (null!= master.getNonFederalResources()) {
            // If left exists
            if (null == master.getNonFederalResources()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkNonFederalResources(parentLocator, handler, master, master.getNonFederalResources());
            }
        }
        if (null!= master.getBudgetForecastedCashNeeds()) {
            // If left exists
            if (null == master.getBudgetForecastedCashNeeds()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetForecastedCashNeeds(parentLocator, handler, master, master.getBudgetForecastedCashNeeds());
            }
        }
        if (null!= master.getFederalFundsNeeded()) {
            // If left exists
            if (null == master.getFederalFundsNeeded()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkFederalFundsNeeded(parentLocator, handler, master, master.getFederalFundsNeeded());
            }
        }
        if (null!= master.getOtherInformation()) {
            // If left exists
            if (null == master.getOtherInformation()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkOtherInformation(parentLocator, handler, master, master.getOtherInformation());
            }
        }
    }

    public void checkBudgetSummary(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.BudgetSummaryType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetSummaryType) {
            gov.grants.apply.forms.sf424a_v1.BudgetSummaryType realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetSummaryType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.BudgetSummaryTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetSummaryTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetSummary"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetSummary) {
                gov.grants.apply.forms.sf424a_v1.BudgetSummary realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetSummary) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.BudgetSummaryVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetSummaryVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetSummary"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetSummary"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkFederalFundsNeeded(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType) {
            gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType realValue = ((gov.grants.apply.forms.sf424a_v1.FederalFundsNeededType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.FederalFundsNeededTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FederalFundsNeededTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundsNeeded"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.FederalFundsNeeded) {
                gov.grants.apply.forms.sf424a_v1.FederalFundsNeeded realValue = ((gov.grants.apply.forms.sf424a_v1.FederalFundsNeeded) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.FederalFundsNeededVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.FederalFundsNeededVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundsNeeded"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundsNeeded"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkProgramType(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProgramType"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkCoreSchemaVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "CoreSchemaVersion"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkOtherInformation(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.OtherInformationType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.OtherInformationType) {
            gov.grants.apply.forms.sf424a_v1.OtherInformationType realValue = ((gov.grants.apply.forms.sf424a_v1.OtherInformationType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.OtherInformationTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.OtherInformationTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherInformation"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.OtherInformation) {
                gov.grants.apply.forms.sf424a_v1.OtherInformation realValue = ((gov.grants.apply.forms.sf424a_v1.OtherInformation) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.OtherInformationVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.OtherInformationVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherInformation"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "OtherInformation"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkBudgetForecastedCashNeeds(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeedsType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeedsType) {
            gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeedsType realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeedsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.BudgetForecastedCashNeedsTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetForecastedCashNeedsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetForecastedCashNeeds"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds) {
                gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetForecastedCashNeeds) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.BudgetForecastedCashNeedsVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetForecastedCashNeedsVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetForecastedCashNeeds"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetForecastedCashNeeds"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkFormVersionIdentifier(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, java.lang.String value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersionIdentifier"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkNonFederalResources(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.NonFederalResourcesType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.NonFederalResourcesType) {
            gov.grants.apply.forms.sf424a_v1.NonFederalResourcesType realValue = ((gov.grants.apply.forms.sf424a_v1.NonFederalResourcesType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.NonFederalResourcesTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.NonFederalResourcesTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalResources"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.NonFederalResources) {
                gov.grants.apply.forms.sf424a_v1.NonFederalResources realValue = ((gov.grants.apply.forms.sf424a_v1.NonFederalResources) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.NonFederalResourcesVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.NonFederalResourcesVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalResources"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "NonFederalResources"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void checkBudgetCategories(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424a_v1.BudgetInformationType master, gov.grants.apply.forms.sf424a_v1.BudgetCategoriesType value) {
        if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetCategoriesType) {
            gov.grants.apply.forms.sf424a_v1.BudgetCategoriesType realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetCategoriesType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424a_v1.verification.BudgetCategoriesTypeVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetCategoriesTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetCategories"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424a_v1.BudgetCategories) {
                gov.grants.apply.forms.sf424a_v1.BudgetCategories realValue = ((gov.grants.apply.forms.sf424a_v1.BudgetCategories) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424a_v1.verification.BudgetCategoriesVerifier verifier = new gov.grants.apply.forms.sf424a_v1.verification.BudgetCategoriesVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetCategories"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetCategories"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetInformationType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424a_v1.BudgetInformationType) object));
    }

}