//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.3-b18-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.19 at 04:23:48 EDT 
//


package gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification;

public class OtherResearchPlanSectionsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
{


    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master) {
        if (null!= master.getVertebrateAnimals()) {
            // If left exists
            if (null == master.getVertebrateAnimals()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkVertebrateAnimals(parentLocator, handler, master, master.getVertebrateAnimals());
            }
        }
        if (null!= master.getSelectAgentResearch()) {
            // If left exists
            if (null == master.getSelectAgentResearch()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkSelectAgentResearch(parentLocator, handler, master, master.getSelectAgentResearch());
            }
        }
        if (null!= master.getMultiplePDPILeadershipPlan()) {
            // If left exists
            if (null == master.getMultiplePDPILeadershipPlan()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkMultiplePDPILeadershipPlan(parentLocator, handler, master, master.getMultiplePDPILeadershipPlan());
            }
        }
        if (null!= master.getConsortiumContractualArrangements()) {
            // If left exists
            if (null == master.getConsortiumContractualArrangements()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkConsortiumContractualArrangements(parentLocator, handler, master, master.getConsortiumContractualArrangements());
            }
        }
        if (null!= master.getLettersOfSupport()) {
            // If left exists
            if (null == master.getLettersOfSupport()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkLettersOfSupport(parentLocator, handler, master, master.getLettersOfSupport());
            }
        }
        if (null!= master.getResourceSharingPlans()) {
            // If left exists
            if (null == master.getResourceSharingPlans()) {
                // Optional field - nothing to report
            } else {
                // Check value
                checkResourceSharingPlans(parentLocator, handler, master, master.getResourceSharingPlans());
            }
        }
    }

    public void checkLettersOfSupport(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.LettersOfSupportTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.LettersOfSupportTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "LettersOfSupport"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "LettersOfSupport"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkConsortiumContractualArrangements(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.ConsortiumContractualArrangementsTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.ConsortiumContractualArrangementsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ConsortiumContractualArrangements"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ConsortiumContractualArrangements"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkSelectAgentResearch(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.SelectAgentResearchTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.SelectAgentResearchTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SelectAgentResearch"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "SelectAgentResearch"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkVertebrateAnimals(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.VertebrateAnimalsTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.VertebrateAnimalsTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "VertebrateAnimals"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "VertebrateAnimals"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkResourceSharingPlans(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.ResourceSharingPlansTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.ResourceSharingPlansTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ResourceSharingPlans"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "ResourceSharingPlans"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void checkMultiplePDPILeadershipPlan(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType master, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType value) {
        if (value instanceof gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType) {
            gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType realValue = ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType) value);
            {
                // Check complex value
                gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.MultiplePDPILeadershipPlanTypeVerifier verifier = new gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.verification.OtherResearchPlanSectionsTypeVerifier.MultiplePDPILeadershipPlanTypeVerifier();
                verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MultiplePDPILeadershipPlan"), handler, realValue);
            }
        } else {
            if (null == value) {
            } else {
                // Report wrong class
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "MultiplePDPILeadershipPlan"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
            }
        }
    }

    public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType) object));
    }

    public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
        check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType) object));
    }

    public static class ConsortiumContractualArrangementsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType) object));
        }

    }

    public static class LettersOfSupportTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.LettersOfSupportType) object));
        }

    }

    public static class MultiplePDPILeadershipPlanTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType) object));
        }

    }

    public static class ResourceSharingPlansTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.ResourceSharingPlansType) object));
        }

    }

    public static class SelectAgentResearchTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.SelectAgentResearchType) object));
        }

    }

    public static class VertebrateAnimalsTypeVerifier implements de.fzi.dbs.verification.ObjectVerifier
    {


        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType master) {
            if (null == master.getAttFile()) {
                // Report missing object
                handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.EmptyFieldProblem()));
            } else {
                // Check value
                checkAttFile(parentLocator, handler, master, master.getAttFile());
            }
        }

        public void checkAttFile(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType master, gov.grants.apply.system.attachments_v1.AttachedFileDataType value) {
            if (value instanceof gov.grants.apply.system.attachments_v1.AttachedFileDataType) {
                gov.grants.apply.system.attachments_v1.AttachedFileDataType realValue = ((gov.grants.apply.system.attachments_v1.AttachedFileDataType) value);
                {
                    // Check complex value
                    gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier verifier = new gov.grants.apply.system.attachments_v1.verification.AttachedFileDataTypeVerifier();
                    verifier.check(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), handler, realValue);
                }
            } else {
                if (null == value) {
                } else {
                    // Report wrong class
                    handler.handleEvent(new de.fzi.dbs.verification.event.VerificationEvent(new de.fzi.dbs.verification.event.VerificationEventLocator(parentLocator, master, "AttFile"), new de.fzi.dbs.verification.event.structure.NonExpectedClassProblem(value.getClass())));
                }
            }
        }

        public void check(de.fzi.dbs.verification.event.AbstractVerificationEventLocator parentLocator, javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(parentLocator, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType) object));
        }

        public void check(javax.xml.bind.ValidationEventHandler handler, java.lang.Object object) {
            check(null, handler, ((gov.grants.apply.forms.phs398_researchplan_1_2_v1_2.OtherResearchPlanSectionsType.VertebrateAnimalsType) object));
        }

    }

}
