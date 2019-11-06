//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.sf424c_v1.verification;

public class BudgetInformationTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master) {
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
        if (null!= master.getProjectCosts()) {
            // If left exists
            if (null == master.getProjectCosts()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkProjectCosts(parentLocator, handler, master, master.getProjectCosts());
            }
        }
        if (null!= master.getFederalFundingPercentageShareValue()) {
            // If left exists
            if (null == master.getFederalFundingPercentageShareValue()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkFederalFundingPercentageShareValue(parentLocator, handler, master, master.getFederalFundingPercentageShareValue());
            }
        }
        if (null!= master.getFederalFundingShareValue()) {
            // If left exists
            if (null == master.getFederalFundingShareValue()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkFederalFundingShareValue(parentLocator, handler, master, master.getFederalFundingShareValue());
            }
        }
    }

    public void checkProgramType(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, java.lang.String value) {
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

    public void checkCoreSchemaVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, java.lang.String value) {
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

    public void checkFederalFundingShareValue(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, java.math.BigDecimal value) {
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
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundingShareValue"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundingShareValue"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFederalFundingPercentageShareValue(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, java.math.BigInteger value) {
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
                if (((java.lang.Comparable) realValue).compareTo(new java.math.BigInteger("999"))<= 0) {
                    // Range is valid
                } else {
                    problem = new de.fzi.dbs.verification.event.datatype.GreaterProblem(realValue, new java.math.BigInteger("999"));
                }
                if (null!= problem) {
                    // Handle event
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundingPercentageShareValue"), problem));
                }
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FederalFundingPercentageShareValue"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersionIdentifier(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, java.lang.String value) {
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

    public void checkProjectCosts(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.sf424c_v1.BudgetInformationType master, gov.grants.apply.forms.sf424c_v1.ProjectCostsType value) {
        if (value instanceof gov.grants.apply.forms.sf424c_v1.ProjectCostsType) {
            gov.grants.apply.forms.sf424c_v1.ProjectCostsType realValue = ((gov.grants.apply.forms.sf424c_v1.ProjectCostsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.sf424c_v1.verification.ProjectCostsTypeVerifier verifier = new gov.grants.apply.forms.sf424c_v1.verification.ProjectCostsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectCosts"), handler, realValue);
            }
        } else {
            if (value instanceof gov.grants.apply.forms.sf424c_v1.ProjectCosts) {
                gov.grants.apply.forms.sf424c_v1.ProjectCosts realValue = ((gov.grants.apply.forms.sf424c_v1.ProjectCosts) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.sf424c_v1.verification.ProjectCostsVerifier verifier = new gov.grants.apply.forms.sf424c_v1.verification.ProjectCostsVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectCosts"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ProjectCosts"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.sf424c_v1.BudgetInformationType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.sf424c_v1.BudgetInformationType) object));
    }

}
