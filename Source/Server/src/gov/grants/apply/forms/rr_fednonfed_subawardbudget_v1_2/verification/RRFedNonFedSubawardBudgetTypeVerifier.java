//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.verification;

public class RRFedNonFedSubawardBudgetTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master) {
        if (null == master.getFormVersion()) {
            // Report missing object
            handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "FormVersion"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
        } else {
            // Check value
            checkFormVersion(parentLocator, handler, master, master.getFormVersion());
        }
        if (null!= master.getATT1()) {
            // If left exists
            if (null == master.getATT1()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT1(parentLocator, handler, master, master.getATT1());
            }
        }
        if (null!= master.getATT2()) {
            // If left exists
            if (null == master.getATT2()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT2(parentLocator, handler, master, master.getATT2());
            }
        }
        if (null!= master.getATT3()) {
            // If left exists
            if (null == master.getATT3()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT3(parentLocator, handler, master, master.getATT3());
            }
        }
        if (null!= master.getATT4()) {
            // If left exists
            if (null == master.getATT4()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT4(parentLocator, handler, master, master.getATT4());
            }
        }
        if (null!= master.getATT5()) {
            // If left exists
            if (null == master.getATT5()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT5(parentLocator, handler, master, master.getATT5());
            }
        }
        if (null!= master.getATT6()) {
            // If left exists
            if (null == master.getATT6()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT6(parentLocator, handler, master, master.getATT6());
            }
        }
        if (null!= master.getATT7()) {
            // If left exists
            if (null == master.getATT7()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT7(parentLocator, handler, master, master.getATT7());
            }
        }
        if (null!= master.getATT8()) {
            // If left exists
            if (null == master.getATT8()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT8(parentLocator, handler, master, master.getATT8());
            }
        }
        if (null!= master.getATT9()) {
            // If left exists
            if (null == master.getATT9()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT9(parentLocator, handler, master, master.getATT9());
            }
        }
        if (null!= master.getATT10()) {
            // If left exists
            if (null == master.getATT10()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkATT10(parentLocator, handler, master, master.getATT10());
            }
        }
        if (null!= master.getBudgetAttachments()) {
            // If left exists
            if (null == master.getBudgetAttachments()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkBudgetAttachments(parentLocator, handler, master, master.getBudgetAttachments());
            }
        }
    }

    public void checkATT4(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT4"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT2(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT2"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT1(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT1"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT6(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT6"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkFormVersion(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
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

    public void checkATT8(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT8"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT3(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT3"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT5(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT5"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkBudgetAttachments(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType value) {
        if (value instanceof gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType) {
            gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType realValue = ((gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.verification.RRFedNonFedSubawardBudgetTypeVerifier.BudgetAttachmentsTypeVerifier verifier = new gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.verification.RRFedNonFedSubawardBudgetTypeVerifier.BudgetAttachmentsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetAttachments"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "BudgetAttachments"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT10(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT10"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT7(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT7"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkATT9(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType master, java.lang.String value) {
        if (value instanceof java.lang.String) {
            java.lang.String realValue = ((java.lang.String) value);
            // Check primitive value
            // Primitive value is always valid, nothing to check
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ATT9"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType) object));
    }

    public static class BudgetAttachmentsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType master) {
            if (null == master.getRRFedNonFedBudget()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "RRFedNonFedBudget"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check count
                if (master.getRRFedNonFedBudget().size()< 0) {
                    // Report minimum of occurences violated
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "RRFedNonFedBudget"), new de.fzi.dbs.verification.event.structure.TooFewElementsProblem(master.getRRFedNonFedBudget().size(), 0)));
                }
                if (master.getRRFedNonFedBudget().size()> 10) {
                    // Report maximum of occurences violated
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "RRFedNonFedBudget"), new de.fzi.dbs.verification.event.structure.TooManyElementsProblem(master.getRRFedNonFedBudget().size(), 10)));
                }
                // Check value
                checkRRFedNonFedBudget(parentLocator, handler, master, master.getRRFedNonFedBudget());
            }
        }

        public void checkRRFedNonFedBudget(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType master, java.util.List values) {
            for (int index = 0; (index<values.size()); index ++) {
                java.lang.Object item = values.get(index);
                checkRRFedNonFedBudget(parentLocator, handler, master, index, item);
            }
        }

        public void checkRRFedNonFedBudget(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType master, int index, java.lang.Object value) {
            if (value instanceof gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudgetType) {
                gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudgetType realValue = ((gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudgetType) value);
                {
                    // Check complex value
                    gov.grants.apply.forms.rr_fednonfedbudget_v1_1.verification.RRFedNonFedBudgetTypeVerifier verifier = new gov.grants.apply.forms.rr_fednonfedbudget_v1_1.verification.RRFedNonFedBudgetTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "RRFedNonFedBudget", index), handler, realValue);
                }
            } else {
                if (value instanceof gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget) {
                    gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget realValue = ((gov.grants.apply.forms.rr_fednonfedbudget_v1_1.RRFedNonFedBudget) value);
                    {
                        // Check complex value
                        gov.grants.apply.forms.rr_fednonfedbudget_v1_1.verification.RRFedNonFedBudgetVerifier verifier = new gov.grants.apply.forms.rr_fednonfedbudget_v1_1.verification.RRFedNonFedBudgetVerifier();
                        verifier.check(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "RRFedNonFedBudget", index), handler, realValue);
                    }
                } else {
                    if (null == value) {
                    } else {
                        // Report wrong class
                        handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.EntryLocator(parentLocator, master, "RRFedNonFedBudget", index), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                    }
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.rr_fednonfed_subawardbudget_v1_2.RRFedNonFedSubawardBudgetType.BudgetAttachmentsType) object));
        }

    }

}
