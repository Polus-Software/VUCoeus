
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.s2s.validator.S2SValidator;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import edu.vanderbilt.coeus.s2s.bean.PHSFellowshipSupplementalTxnBean_V3_1;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;

import gov.grants.apply.forms.phs_fellowship_supplemental_4_0_v4.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.organization.bean.OrganizationMaintenanceFormBean;
import edu.mit.coeus.utils.DateUtils;
import gov.grants.apply.system.attachments_v1.AttachedFileDataType;

/**
 *
 * @author Jill McAfee
 */
public class PHSFellowshipSupplementalStream_V4_0 extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs_fellowship_supplemental_4_0_v4.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    private AttachedFileDataType attachedFileType;
    
    private PHSFellowshipSupplementalTxnBean_V3_1 phsFellowshipSupTxnBean;
    private String propNumber;
    private CoeusVector cvAnswers;
    private CoeusVector cvLevel, cvType, cvStartYN, cvStartDate, cvEndYN, cvEndDate, cvGrantNumber;
    private HashMap hmAnswers;
    private HashMap<Integer,CoeusVector> kirschAnswers;
    private Attachment attachment;
    private UtilFactory utilFactory;
    
    /* Questionnaire questions */
    private static final int VERTEBRATE_ANIMALS_USED = 589;
    private static final int VERTEBRATE_ANIMALS_EUTHANIZED = 590;
    private static final int VERTEBRATE_ANIMALS_METHOD = 591;
    private static final int VERTEBRATE_ANIMALS_JUSTIFICATION = 592;
    private static final int STEM_CELLS_USED = 5;
    private static final int STEM_CELL_LINES_KNOWN = 6;
    private static final int STEM_CELL_LINES = 142;
    private static final int DEGREE_SOUGHT_YN = 42;
    private static final int DEGREE_SOUGHT = 99;
    private static final int DEGREE_SOUGHT_OTHER = 100;
    private static final int DEGREE_SOUGHT_DATE = 606;
    private static final int FIELD_OF_TRAINING = 607;
    private static final int KIRSCH_YN = 24;
    private static final int KIRSCH_LEVEL = 32;
    private static final int KIRSCH_TYPE = 33;
    private static final int KIRSCH_START_YN = 43;
    private static final int KIRSCH_START_DATE = 44;
    private static final int KIRSCH_END_YN = 49;
    private static final int KIRSCH_END_DATE = 45;
    private static final int KIRSCH_GRANT_NO = 27;
    private static final int KIRSCH_NEXT = 31;
    private static final int CITIZENSHIP = 586;
    private static final int NON_CITIZEN_TYPE = 608;
    private static final int VISA_STATUS = 588;
    private static final int CHANGE_OF_INST = 609;
    private static final int FORMER_INST = 117;
    private static final int SENIOR_FELLOWSHIP = 36;
    private static final int PRESENT_INSTITUTIONAL_SALARY_AMOUNT = 47;
    private static final int PRESENT_INSTITUTIONAL_SALARY_PERIOD = 48;
    private static final int PRESENT_INSTITUTIONAL_SALARY_MONTHS = 50;
    private static final int SUPP_OTHER_SOURCES = 37;
    private static final int SUPP_FUNDING_AMT = 38;
    private static final int SUPP_MONTHS = 51;
    private static final int SUPP_TYPE = 40;
    private static final int SUPP_SOURCE = 41;
    
    /* Narrative types */
    private static final int INTRODUCTION = 97;
    private static final int BACKGROUND_AND_GOALS = 92;
    private static final int SPECIFIC_AIMS = 98;
    private static final int RESEARCH_STRATEGY = 127;
    private static final int RESPECTIVE_CONTRIBUTIONS = 88;
    private static final int SPONSOR_AND_INSTITUTION = 89;
    private static final int PROGRESS_REPORT = 103;
    private static final int RESPONSIBLE_CONDUCT = 90;
    private static final int SPONSOR_STATEMENTS = 134;
    private static final int LETTERS_OF_SUPPORT = 207;
    private static final int INSTITUTIONAL_ENVIRONMENT = 208;
    private static final int HUMAN_SUBJECTS = 104;
    private static final int DATA_SAFETY_MONITORING = 209;
    private static final int WOMEN_AND_MINORITIES = 105;
    private static final int CHILDREN = 107;
    private static final int VERTEBRATE_ANIMALS = 108;
    private static final int SELECT_AGENT_RESEARCH = 109;
    private static final int RESOURCE_SHARING = 110;
    private static final int BIOLOGICAL_CHEMICAL_RESOURCES = 210;
    private static final int CONCURRENT_SUPPORT = 91;
    private static final int APPENDIX = 96;
    
    /* Values */
    private final static String YES = "Y: Yes";
    private final static String NO = "N: No";
    private final static String TEMPORARY_VISA = "With a Temporary U.S. Visa";
    private final static String BUDGET_EXISTS = "BUDGET_EXISTS";
    private final static String STIPEND_COST = "STIPEND_COST";
    private final static String STIPEND_MONTHS = "STIPEND_MONTHS";    
    private final static String TUITION_TOTAL = "TUITION_TOTAL";
    private final static String TUITION1 = "TUITION1";
    private final static String TUITION2 = "TUITION2";
    private final static String TUITION3 = "TUITION3";
    private final static String TUITION4 = "TUITION4";
    private final static String TUITION5 = "TUITION5";
    private final static String TUITION6 = "TUITION6";
    
    private HashMap narratives, narrativeContent;
    private ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean;
    private ProposalNarrativeTxnBean proposalNarrativeTxnBean;
   
    public PHSFellowshipSupplementalStream_V4_0() {
        objFactory = new gov.grants.apply.forms.phs_fellowship_supplemental_4_0_v4.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
        xmlGenerator = new CoeusXMLGenrator();  
    }    

    private PHSFellowshipSupplemental40Type getPHSFellowshipSupplemental()
    		throws CoeusXMLException,CoeusException,DBException,JAXBException {
  
    	PHSFellowshipSupplemental40Type phsFellowshipSupplementalType = objFactory.createPHSFellowshipSupplemental40();

    	gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup = 
    			attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
    
          
    	phsFellowshipSupTxnBean = new PHSFellowshipSupplementalTxnBean_V3_1();
    	
    	cvAnswers = getQuestionnaireAnswers();
    	if (cvAnswers == null || cvAnswers.size() == 0) {
    		S2SValidator.addCustError(propNumber, "The fellowship questionnaire has not been completed.");
    	}
    	hmAnswers = processQuestionnaireAnswers(cvAnswers);
    	getKirschsteinNRSAQuestionnaireAnswers(cvAnswers);

    	
       /** FormVersion */
       phsFellowshipSupplementalType.setFormVersion("4.0");

       /** Attachments */
       String description;
       int narrativeType;
       int moduleNum;
       proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
               
	   Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber); 
  
	   S2STxnBean s2sTxnBean = new S2STxnBean();
	   LinkedHashMap hmArg = new LinkedHashMap();
              
	   HashMap hmNarrative = new HashMap();
	   narratives = new HashMap();
	   narrativeContent = new HashMap();
	   attachment = null;
	    
	   int size=vctNarrative==null?0:vctNarrative.size();
	   for (int row=0; row < size; row++) {
		   hmArg = new LinkedHashMap(); // JM
		   proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                    
		   moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
		   hmNarrative = new HashMap();
		   hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
		   narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
		   description = hmNarrative.get("DESCRIPTION").toString();
  
		   hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
		   hmArg.put(ContentIdConstants.DESCRIPTION, description);
	   		
		   narratives.put(narrativeType, hmArg);
		   narrativeContent.put(narrativeType,proposalNarrativePDFSourceBean);
	   		
		   // Appendix - can be multiple
           if (narrativeType == APPENDIX) {
        	   if (attachment == null) {
        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                   if (narrativeAttachment.getContent() != null) {                           
                	   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       attachmentGroup.getAttachedFile().add(attachedFileType);
                   }
        	   } 
           }
	   }

       if (attachmentGroup != null && attachmentGroup.getAttachedFile().size()  > 0) {
    	   phsFellowshipSupplementalType.setAppendix(attachmentGroup);
       }


       /* Get all the form parts */
       phsFellowshipSupplementalType.setIntroduction(getIntroductionType());
       phsFellowshipSupplementalType.setFellowshipApplicant(getFellowshipApplicantType());
       phsFellowshipSupplementalType.setResearchTrainingPlan(getResearchTrainingPlanType());
       phsFellowshipSupplementalType.setSponsors(getSponsorsType());
       phsFellowshipSupplementalType.setInstitutionalEnvironment(getInstitutionalEnvironmentType());
       phsFellowshipSupplementalType.setOtherResearchTrainingPlan(getOtherResearchTrainingPlanType());
       phsFellowshipSupplementalType.setAdditionalInformation(getAdditionalInformationType());
       phsFellowshipSupplementalType.setBudget(getBudgetType());
    
       return phsFellowshipSupplementalType;

    }
    
    
    /* Introduction to Application type */    
    private PHSFellowshipSupplemental40Type.IntroductionType getIntroductionType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.IntroductionType introductionType = 
    			objFactory.createPHSFellowshipSupplemental40TypeIntroductionType();
    	
	    // Introduction to Application
	    if (narratives.get(INTRODUCTION) != null) {
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(INTRODUCTION);
	    				
	    	PHSFellowshipSupplemental40Type.IntroductionType.IntroductionToApplicationType introductionToApplicationType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeIntroductionTypeIntroductionToApplicationType();
			introductionToApplicationType.setAttFile(thisAttachedFileType);
			introductionType.setIntroductionToApplication(introductionToApplicationType);

	    }   
	    return introductionType;
    }
    
    /* Fellowship Applicant type */    
    private PHSFellowshipSupplemental40Type.FellowshipApplicantType getFellowshipApplicantType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.FellowshipApplicantType fellowshipApplicantType = 
    			objFactory.createPHSFellowshipSupplemental40TypeFellowshipApplicantType();
    	
	    // Background and Goals
	    if (narratives.get(BACKGROUND_AND_GOALS) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(BACKGROUND_AND_GOALS);
	    				
	    	PHSFellowshipSupplemental40Type.FellowshipApplicantType.BackgroundandGoalsType backgroundAndGoalsType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeFellowshipApplicantTypeBackgroundandGoalsType();
	    	backgroundAndGoalsType.setAttFile(thisAttachedFileType);
			fellowshipApplicantType.setBackgroundandGoals(backgroundAndGoalsType);

	    }   
	    return fellowshipApplicantType;
    }
    
    /* Research Training Plan type */    
    private PHSFellowshipSupplemental40Type.ResearchTrainingPlanType getResearchTrainingPlanType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType researchTrainingPlanType = 
    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanType();
    	
	    // Specific Aims
	    if (narratives.get(SPECIFIC_AIMS) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(SPECIFIC_AIMS);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.SpecificAimsType specificAimsType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeSpecificAimsType();
	    	specificAimsType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setSpecificAims(specificAimsType);
	    }
	    
	    // Research Strategy
	    if (narratives.get(RESEARCH_STRATEGY) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(RESEARCH_STRATEGY);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.ResearchStrategyType researchStrategyType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeResearchStrategyType();
	    	researchStrategyType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setResearchStrategy(researchStrategyType);
	    }
	    
	    // Respective Contributions
	    if (narratives.get(RESPECTIVE_CONTRIBUTIONS) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(RESPECTIVE_CONTRIBUTIONS);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.RespectiveContributionType respectiveContributionType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeRespectiveContributionType();
	    	respectiveContributionType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setRespectiveContribution(respectiveContributionType);
	    }
	    
	    // Sponsor and Institution
	    if (narratives.get(SPONSOR_AND_INSTITUTION) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(SPONSOR_AND_INSTITUTION);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.SponsorandInstitutionType sponsorandInstitutionType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeSponsorandInstitutionType();
	    	sponsorandInstitutionType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setSponsorandInstitution(sponsorandInstitutionType);
	    }
	    
	    // Progress Report
	    if (narratives.get(PROGRESS_REPORT) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(PROGRESS_REPORT);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.ProgressReportPublicationListType progressReportPublicationListType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeProgressReportPublicationListType();
	    	progressReportPublicationListType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setProgressReportPublicationList(progressReportPublicationListType);
	    }
	    
	    // Responsible Conduct of Research
	    if (narratives.get(RESPONSIBLE_CONDUCT) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(RESPONSIBLE_CONDUCT);
	    				
	    	PHSFellowshipSupplemental40Type.ResearchTrainingPlanType.TrainingInResponsibleConductOfResearchType trainingInResponsibleConductOfResearchType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeResearchTrainingPlanTypeTrainingInResponsibleConductOfResearchType();
	    	trainingInResponsibleConductOfResearchType.setAttFile(thisAttachedFileType);
	    	researchTrainingPlanType.setTrainingInResponsibleConductOfResearch(trainingInResponsibleConductOfResearchType);
	    }
	    
	    return researchTrainingPlanType;
    }
    
    /* Sponsors and Collaborators type */    
    private PHSFellowshipSupplemental40Type.SponsorsType getSponsorsType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.SponsorsType sponsorsType = 
    			objFactory.createPHSFellowshipSupplemental40TypeSponsorsType();
    	
	    // Sponsor Statements
	    if (narratives.get(SPONSOR_STATEMENTS) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(SPONSOR_STATEMENTS);
	    				
	    	PHSFellowshipSupplemental40Type.SponsorsType.SponsorAndCoSponsorStatementsType sponsorAndCoSponsorStatementsType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeSponsorsTypeSponsorAndCoSponsorStatementsType();
	    	sponsorAndCoSponsorStatementsType.setAttFile(thisAttachedFileType);
	    	sponsorsType.setSponsorAndCoSponsorStatements(sponsorAndCoSponsorStatementsType);
	    }
	    
	    // Letters of Support
	    if (narratives.get(LETTERS_OF_SUPPORT) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(LETTERS_OF_SUPPORT);
	    				
	    	PHSFellowshipSupplemental40Type.SponsorsType.LettersOfSupportType lettersOfSupportType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeSponsorsTypeLettersOfSupportType();
	    	lettersOfSupportType.setAttFile(thisAttachedFileType);
	    	sponsorsType.setLettersOfSupport(lettersOfSupportType);
	    }
	    
	    return sponsorsType;
    }
    
    /* Institutional Environment type */    
    private PHSFellowshipSupplemental40Type.InstitutionalEnvironmentType getInstitutionalEnvironmentType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.InstitutionalEnvironmentType institutionalEnvironmentType = 
    			objFactory.createPHSFellowshipSupplemental40TypeInstitutionalEnvironmentType();
    	
	    // Sponsor Statements
	    if (narratives.get(INSTITUTIONAL_ENVIRONMENT) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(INSTITUTIONAL_ENVIRONMENT);
	    				
	    	PHSFellowshipSupplemental40Type.InstitutionalEnvironmentType.InstitutionalEnvironmentCommitmenttoTrainingType 
	    		institutionalEnvironmentCommitmenttoTrainingType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeInstitutionalEnvironmentTypeInstitutionalEnvironmentCommitmenttoTrainingType();
	    	institutionalEnvironmentCommitmenttoTrainingType.setAttFile(thisAttachedFileType);
	    	institutionalEnvironmentType.setInstitutionalEnvironmentCommitmenttoTraining(
	    			institutionalEnvironmentCommitmenttoTrainingType);
	    }
	    
	    return institutionalEnvironmentType;
    }
    
    /* Other Research Training Plan type */    
    private PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType getOtherResearchTrainingPlanType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType otherResearchTrainingPlanType = 
    			objFactory.createPHSFellowshipSupplemental40TypeOtherResearchTrainingPlanType();
    	
    	/** Vertebrate Animals Area - START */
 	    // Vertebrate Animals
	    if (narratives.get(VERTEBRATE_ANIMALS) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(VERTEBRATE_ANIMALS);
	    				
	    	PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType.VertebrateAnimalsType vertebrateAnimalsType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeOtherResearchTrainingPlanTypeVertebrateAnimalsType();
	    	vertebrateAnimalsType.setAttFile(thisAttachedFileType);
	    	otherResearchTrainingPlanType.setVertebrateAnimals(vertebrateAnimalsType);
	    }

	    // Are vertebrate animals used? 
	    HashMap vertebrateAnimalsAnswers = getVertebrateAnimalsInfo();
        if (vertebrateAnimalsAnswers != null) {
        	if (vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_USED) != null) {
	        	String vertebrateAnimalsUsed = (String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_USED);
	            if (vertebrateAnimalsUsed.equals("Y")) {
	        	    otherResearchTrainingPlanType.setVertebrateAnimalsUsed(YES);
	                 
	                 // Are vertebrate animals euthanized?
	                 String vertebrateAnimalsEuthanized = (String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_EUTHANIZED);
	                 if (vertebrateAnimalsEuthanized.equals("Y")) {
	                	 otherResearchTrainingPlanType.setAreAnimalsEuthanized(YES);
	                	 
		                 // Euthanization consistent with AVMA?
		                 String vertebrateAnimalsMethod = (String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_METHOD);
		                 if (vertebrateAnimalsMethod != null) {
			                 if (vertebrateAnimalsMethod.equals("Y")) {
			                	 otherResearchTrainingPlanType.setAVMAConsistentIndicator(YES);
			                 } 
			                 else {
			                	 otherResearchTrainingPlanType.setAVMAConsistentIndicator(NO);
			                	 
			                	 // Euthanization explanation
			                	 otherResearchTrainingPlanType.setEuthanasiaMethodDescription((String) vertebrateAnimalsAnswers.get(VERTEBRATE_ANIMALS_JUSTIFICATION));
			                 }
		                 }
	                 } 
	                 else {
	                	 otherResearchTrainingPlanType.setAreAnimalsEuthanized(NO);
	                 }
	            }
	            else {
	            	otherResearchTrainingPlanType.setVertebrateAnimalsUsed(NO);
	            }
            }
        }
    	/** Vertebrate Animals Area - END */
	    
    	/** Other Research Training Plan Information - START */
	    // Select Agent Research
	    if (narratives.get(SELECT_AGENT_RESEARCH) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(SELECT_AGENT_RESEARCH);
	    				
	    	PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType.SelectAgentResearchType selectAgentResearchType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeOtherResearchTrainingPlanTypeSelectAgentResearchType();
	    	selectAgentResearchType.setAttFile(thisAttachedFileType);
	    	otherResearchTrainingPlanType.setSelectAgentResearch(selectAgentResearchType);
	    }
	    
	    // Resource Sharing Plan
	    if (narratives.get(RESOURCE_SHARING) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(RESOURCE_SHARING);
	    				
	    	PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType.ResourceSharingPlanType resourceSharingPlanType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeOtherResearchTrainingPlanTypeResourceSharingPlanType();
	    	resourceSharingPlanType.setAttFile(thisAttachedFileType);
	    	otherResearchTrainingPlanType.setResourceSharingPlan(resourceSharingPlanType);
	    }
	    
	    // Authentication of Key Biological and/or Chemical Resources
	    if (narratives.get(BIOLOGICAL_CHEMICAL_RESOURCES) != null) { 
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(BIOLOGICAL_CHEMICAL_RESOURCES);
	    				
	    	PHSFellowshipSupplemental40Type.OtherResearchTrainingPlanType.KeyBiologicalAndOrChemicalResourcesType 
	    		keyBiologicalAndOrChemicalResourcesType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeOtherResearchTrainingPlanTypeKeyBiologicalAndOrChemicalResourcesType();
	    	keyBiologicalAndOrChemicalResourcesType.setAttFile(thisAttachedFileType);
	    	otherResearchTrainingPlanType.setKeyBiologicalAndOrChemicalResources(keyBiologicalAndOrChemicalResourcesType);
	    }
    	/** Other Research Training Plan Information - END */
	    
	    return otherResearchTrainingPlanType;
    }
    
    /* Additional Information type */    
    private PHSFellowshipSupplemental40Type.AdditionalInformationType getAdditionalInformationType() 
    		throws JAXBException, CoeusException, DBException {
    
    	PHSFellowshipSupplemental40Type.AdditionalInformationType additionalInformationType = 
    			objFactory.createPHSFellowshipSupplemental40TypeAdditionalInformationType();
    	
    	/** Stem Cells Area - START */
    	additionalInformationType.setStemCells(getStemCellsType());
    	/** Stem Cells Area - END */
    	
    	/** Alternate Phone - START */
    	additionalInformationType.setAlernatePhoneNumber(getAlternatePhone());
    	/** Alternate Phone - END */
    	
    	/** Degree Sought - START */
    	if (hmAnswers.get(DEGREE_SOUGHT_YN) != null) {
	    	if (hmAnswers.get(DEGREE_SOUGHT_YN).equals("Y")) {
	        	PHSFellowshipSupplemental40Type.AdditionalInformationType.GraduateDegreeSoughtType degreeSought = 
	        			objFactory.createPHSFellowshipSupplemental40TypeAdditionalInformationTypeGraduateDegreeSoughtType();
	    		
	    		degreeSought.setDegreeType((String) hmAnswers.get(DEGREE_SOUGHT)); 
	    		degreeSought.setDegreeDate((String) hmAnswers.get(DEGREE_SOUGHT_DATE)); 
	    		
	    		if (hmAnswers.get(DEGREE_SOUGHT_OTHER) != null) {
	    			degreeSought.setOtherDegreeTypeText((String) hmAnswers.get(DEGREE_SOUGHT_OTHER)); 
	    		}
	        	additionalInformationType.setGraduateDegreeSought(degreeSought);
	    	}
    	}
    	/** Degree Sought - END */
    	
    	/** Field of Training - START */    	
		if (hmAnswers.get(FIELD_OF_TRAINING) != null) {
			additionalInformationType.setFieldOfTraining((String) hmAnswers.get(FIELD_OF_TRAINING)); 
		}
    	/** Field of Training - END */
    	
    	/** Support - START */
    	// Concurrent Support
	    additionalInformationType.setConcurrentSupport(NO);
		if (narratives.get(CONCURRENT_SUPPORT) != null) { 
		    additionalInformationType.setConcurrentSupport(YES);
	    	AttachedFileDataType thisAttachedFileType = getAttachedFile(CONCURRENT_SUPPORT);
	    				
	    	PHSFellowshipSupplemental40Type.AdditionalInformationType.ConcurrentSupportDescriptionType 
	    		concurrentSupportDescriptionType = 
	    			objFactory.createPHSFellowshipSupplemental40TypeAdditionalInformationTypeConcurrentSupportDescriptionType();
	    	concurrentSupportDescriptionType.setAttFile(thisAttachedFileType);
	    	additionalInformationType.setConcurrentSupportDescription(concurrentSupportDescriptionType);
	    }
	    
	    // Kirschstein-NRSA support
	    if (hmAnswers.get(KIRSCH_YN) != null) {
	    	if (hmAnswers.get(KIRSCH_YN).equals("Y")) {
	    	    additionalInformationType.setCurrentPriorNRSASupportIndicator(YES);
	    		
	        	PHSFellowshipSupplemental40Type.AdditionalInformationType.CurrentPriorNRSASupportType supportType;
	        	KirschsteinNRSABean levelBean, typeBean, startDateYNBean, startDateBean, endDateYNBean, endDateBean, grantNumberBean;
	        	
	        	for (int k=0; k < cvLevel.size(); k++) {
	        		supportType =  objFactory.createPHSFellowshipSupplemental40TypeAdditionalInformationTypeCurrentPriorNRSASupportType();
	
		       		levelBean = (KirschsteinNRSABean) cvLevel.get(k);
		       		typeBean = (KirschsteinNRSABean) cvType.get(k);
		       		startDateYNBean = (KirschsteinNRSABean) cvStartYN.get(k);
		       		startDateBean = (KirschsteinNRSABean) cvStartDate.get(k);
		       		endDateYNBean = (KirschsteinNRSABean) cvEndYN.get(k);
		       		endDateBean = (KirschsteinNRSABean) cvEndDate.get(k);
		       		grantNumberBean = (KirschsteinNRSABean) cvGrantNumber.get(k);
		       		
		       		supportType.setLevel(levelBean.getLevel());
		       		supportType.setType(typeBean.getType());    
		       		supportType.setGrantNumber(grantNumberBean.getGrantNumber()); 
		       		
		       		if (startDateYNBean.getStartDateKnown().equals("Y")) {
		       			supportType.setStartDate(convertDateStringToCalendar(startDateBean.getStartDate()));
		       		}
		       		
		       		if (endDateYNBean.getEndDateKnown().equals("Y")) {
		       			supportType.setEndDate(convertDateStringToCalendar(endDateBean.getEndDate()));
		       		}
		       		
		       		additionalInformationType.getCurrentPriorNRSASupport().add(supportType);
	        	}
	    	}
	    	else {
	    		additionalInformationType.setCurrentPriorNRSASupportIndicator(NO);
	    	}
	    }
    	/** Support - END */
    	
    	/** Citizenship - START */
	    if (hmAnswers.get(CITIZENSHIP) != null) {
	   		if (hmAnswers.get(CITIZENSHIP).equals("Y")) {
	   			additionalInformationType.setUSCitizen(YES);
	   		}
		    else {
		    	additionalInformationType.setUSCitizen(NO);
	
		    	if (hmAnswers.get(NON_CITIZEN_TYPE) != null) {
		    		String nonCitizenType = (String) hmAnswers.get(NON_CITIZEN_TYPE);
		    		additionalInformationType.setNonUSCitizen(nonCitizenType);
		    		
		    		if (nonCitizenType.equals(TEMPORARY_VISA)) {
			 			if (hmAnswers.get(VISA_STATUS) != null) {
			    			if (hmAnswers.get(VISA_STATUS).equals("Y")) {
			    				additionalInformationType.setPermanentResidentByAwardIndicator(YES);
			    			}
			    			else {
			    				additionalInformationType.setPermanentResidentByAwardIndicator(NO);
			    			}
			    		}
		    		}
	        	}
	        }
	    }
    	/** Citizenship - END */
	    
    	/** Sponsoring Institution Change - START */
    	//additionalInformationType.setChangeOfInstitution(NO);
	    if (hmAnswers.get(CHANGE_OF_INST) != null) {
	   		if (hmAnswers.get(CHANGE_OF_INST).equals("Y")) {
	   			additionalInformationType.setChangeOfInstitution(YES);
	
		    	if (hmAnswers.get(FORMER_INST) != null) {
		    		additionalInformationType.setFormerInstitution(getOrganizationName((String) hmAnswers.get(FORMER_INST)));
		    	}
	   		}
	    }
    	/** Sponsoring Institution Change - END */
    	
	    return additionalInformationType;
    }
    
    /* Budget type */    
    private PHSFellowshipSupplemental40Type.BudgetType getBudgetType() 
    		throws JAXBException, CoeusException, DBException {
    	
    	phsFellowshipSupTxnBean = new PHSFellowshipSupplementalTxnBean_V3_1();
    	HashMap hmBudget = phsFellowshipSupTxnBean.getBudgetInfo(propNumber);
    	
    	PHSFellowshipSupplemental40Type.BudgetType budgetType = 
    			objFactory.createPHSFellowshipSupplemental40TypeBudgetType();
    	PHSFellowshipSupplemental40Type.BudgetType.InstitutionalBaseSalaryType institutionalSalary = 
    			objFactory.createPHSFellowshipSupplemental40TypeBudgetTypeInstitutionalBaseSalaryType();
    	PHSFellowshipSupplemental40Type.BudgetType.FederalStipendRequestedType federalStipend = 
    			objFactory.createPHSFellowshipSupplemental40TypeBudgetTypeFederalStipendRequestedType();
    	PHSFellowshipSupplemental40Type.BudgetType.SupplementationFromOtherSourcesType supplementalSources = 
    			objFactory.createPHSFellowshipSupplemental40TypeBudgetTypeSupplementationFromOtherSourcesType();
    	
    	if (hmBudget.get(BUDGET_EXISTS) != null) {
            if (hmBudget.get(BUDGET_EXISTS).toString().equals("1")) {
 	    		
	        	/** All Fellowship Applications - START */
	    		if (hmBudget.get(TUITION_TOTAL) != null) {
	    			budgetType.setTuitionAndFeesRequested(YES);
		    		budgetType.setTuitionRequestedYear1(new BigDecimal(hmBudget.get(TUITION1).toString()));
		    		budgetType.setTuitionRequestedYear2(new BigDecimal(hmBudget.get(TUITION2).toString()));
		    		budgetType.setTuitionRequestedYear3(new BigDecimal(hmBudget.get(TUITION3).toString()));
		    		budgetType.setTuitionRequestedYear4(new BigDecimal(hmBudget.get(TUITION4).toString()));
		    		budgetType.setTuitionRequestedYear5(new BigDecimal(hmBudget.get(TUITION5).toString()));
		    		budgetType.setTuitionRequestedYear6(new BigDecimal(hmBudget.get(TUITION6).toString()));
		    		budgetType.setTuitionRequestedTotal(new BigDecimal(hmBudget.get(TUITION_TOTAL).toString()));
	    		}
	    		else {
	    			budgetType.setTuitionAndFeesRequested(NO);
	    		}
	        	/** All Fellowship Applications - END */
	    		
	        	/** Senior Fellowship Applicants - START */
	    		if (hmAnswers.get(SENIOR_FELLOWSHIP) != null) {
	    			if (hmAnswers.get(SENIOR_FELLOWSHIP).equals("Y")) {
	    				institutionalSalary.setAmount(new BigDecimal(hmAnswers.get(PRESENT_INSTITUTIONAL_SALARY_AMOUNT).toString()));
	    				institutionalSalary.setAcademicPeriod((String) hmAnswers.get(PRESENT_INSTITUTIONAL_SALARY_PERIOD));
	    				institutionalSalary.setNumberOfMonths(new BigDecimal(hmAnswers.get(PRESENT_INSTITUTIONAL_SALARY_MONTHS).toString()));

	    				budgetType.setInstitutionalBaseSalary(institutionalSalary);
	    				
	    		    	HashMap hmStipend = phsFellowshipSupTxnBean.getFellowshipStipends(propNumber);
	    				
			    		if (hmStipend.get(STIPEND_COST) != null) {
				    		federalStipend.setAmount(new BigDecimal(hmStipend.get(STIPEND_COST).toString()));
				    		federalStipend.setNumberOfMonths(new BigDecimal(hmStipend.get(STIPEND_MONTHS).toString()));
				    		budgetType.setFederalStipendRequested(federalStipend);
				    	}
	    				
			    		if (hmAnswers.get(SUPP_OTHER_SOURCES) != null) {
		    				if (hmAnswers.get(SUPP_OTHER_SOURCES).equals("Y")) {
		    					supplementalSources.setAmount(new BigDecimal(hmAnswers.get(SUPP_FUNDING_AMT).toString()));
		    					supplementalSources.setNumberOfMonths(new BigDecimal(hmAnswers.get(SUPP_MONTHS).toString()));
		    					supplementalSources.setType((String) hmAnswers.get(SUPP_TYPE));	    					
		    					supplementalSources.setSource((String) hmAnswers.get(SUPP_SOURCE));
		    					budgetType.setSupplementationFromOtherSources(supplementalSources);
		    				}
			    		}
	    			}
	    		}
	        	/** Senior Fellowship Applicants - END */
	    	}
	    }
    	
	    return budgetType;
    }
    
    /* Method to process narratives */
    private AttachedFileDataType getAttachedFile(int narrativeType) throws CoeusException, DBException, JAXBException {
    	if (narratives.get(narrativeType) != null) {
    		if (attachment == null) {
    			proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
    			proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(
    					(ProposalNarrativePDFSourceBean) narrativeContent.get(narrativeType));      
    			Attachment narrativeAttachment = getAndAddNarrative((LinkedHashMap) narratives.get(narrativeType), proposalNarrativePDFSourceBean);
    			
    			if (narrativeAttachment.getContent() != null) {
    				attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
    			}
    		}
    	}
    	return attachedFileType;
    }

    // Human Subjects
    private HashMap getHumanSubjects() 
    		throws JAXBException, DBException, CoeusException {

        cvAnswers = phsFellowshipSupTxnBean.getHumanSubjects(propNumber);
    	if (cvAnswers == null || cvAnswers.size() == 0) {
    		S2SValidator.addCustError(propNumber, "The proposal questionnaire has not been completed.");
    	}
        HashMap hmAnswers = processQuestionnaireAnswers(cvAnswers);
        return hmAnswers;
    }
  
    // Clinical trials
    private HashMap getClinicalTrials() 
    		throws JAXBException, DBException, CoeusException {

        cvAnswers = phsFellowshipSupTxnBean.getClinicalTrial(propNumber);
        HashMap hmAnswers = processQuestionnaireAnswers(cvAnswers);
        return hmAnswers;
     }

    // Vertebrate Animals
    private HashMap getVertebrateAnimalsInfo() 
    		throws JAXBException, DBException, CoeusException {

        cvAnswers = phsFellowshipSupTxnBean.getVertebrateAnimals(propNumber);
        HashMap hmAnswers = processQuestionnaireAnswers(cvAnswers);
        return hmAnswers;
    }

    // Get previous institution name
    private String getOrganizationName(String orgId) throws CoeusException, DBException, JAXBException {
    	OrganizationMaintenanceFormBean bean = phsFellowshipSupTxnBean.getOrganizationName(orgId);
    	String organizationName = (String) bean.getOrganizationName();
    	return organizationName;
    }
    
    // Get alternate phone
    private String getAlternatePhone() 
    		throws JAXBException, DBException, CoeusException {
       	return phsFellowshipSupTxnBean.getAlternatePhone(propNumber);
    }
   
    /* Get stem cell information */
    private PHSFellowshipSupplemental40Type.AdditionalInformationType.StemCellsType getStemCellsType() 
    		throws JAXBException, DBException, CoeusException {
    	PHSFellowshipSupplemental40Type.AdditionalInformationType.StemCellsType stemCellsType = 
    			objFactory.createPHSFellowshipSupplemental40TypeAdditionalInformationTypeStemCellsType();

    	cvAnswers = phsFellowshipSupTxnBean.getStemCells(propNumber);
    	HashMap stemCellAnswers = processQuestionnaireAnswers(cvAnswers);
    
    	if (stemCellAnswers != null) {
    		if (stemCellAnswers.get(STEM_CELLS_USED) != null) {
    			String stemCellsUsed = (String) stemCellAnswers.get(STEM_CELLS_USED);
    			if (stemCellsUsed.equals("Y")) {
    				stemCellsType.setIsHumanStemCellsInvolved(YES);
	
    				// The question asks if stem cell lines are known, but the box should be checked if they are NOT known
    				String stemCellLinesKnown = (String) stemCellAnswers.get(STEM_CELL_LINES_KNOWN);
    				if (stemCellLinesKnown.equals("Y")) {
    					stemCellsType.setStemCellsIndicator(NO);
    					ArrayList<String> cellLines = getStemCellLines(cvAnswers);
    					for (int c=0; c < cellLines.size(); c++) {
    						stemCellsType.getCellLines().add(cellLines.get(c));
    					}
    				} 
    				else {
    					stemCellsType.setStemCellsIndicator(YES);
    				}
    			}
    			else {
    				stemCellsType.setIsHumanStemCellsInvolved(NO);
    			}
    		}
    	}
    	return stemCellsType;
    }
      
    /* Get stem cell lines from questionnaire answers and breaks out lists into individual entries */
    private ArrayList<String> getStemCellLines (CoeusVector dbAnswers) {
	   	QuestionAnswerBean bean = new QuestionAnswerBean();
	   	ArrayList<String> cellLines = new ArrayList<String>();
	   	String[] answerList = new String[10];
	
	   	if (dbAnswers.size() > 0) {
	   		for (int v=0; v < dbAnswers.size(); v++) {
	   			bean = (QuestionAnswerBean) dbAnswers.get(v);       			
	   			if (bean.getQuestionId() == STEM_CELL_LINES) {
	   				answerList = bean.getAnswer().split(",");
	   				for (int a=0; a < answerList.length; a++) {
	   					cellLines.add(answerList[a].trim());
	   				}
	   			}
	   		}
	   	}
	   	return cellLines;
    }

    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
        
    	phsFellowshipSupTxnBean = new PHSFellowshipSupplementalTxnBean_V3_1();
    	cvAnswers = phsFellowshipSupTxnBean.getQuestionnaireAnswers(propNumber);
    	return cvAnswers;
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private HashMap processQuestionnaireAnswers(CoeusVector dbAnswers) throws CoeusException {
    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
	   
    	if (dbAnswers.size() > 0) {
    		for (int v=0; v < dbAnswers.size(); v++) {
    			bean = (QuestionAnswerBean) dbAnswers.get(v);
              		map.put(bean.getQuestionId(),bean.getAnswer());
    		}
    	}

    	return map;
    }
    
    /* Take questionnaire answers to the Kirschstein-NRSA and put in beans within a HashMap */
    private void getKirschsteinNRSAQuestionnaireAnswers(CoeusVector dbAnswers) {
    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();

    	KirschsteinNRSABean	kirschBean;
    	
    	cvLevel = new CoeusVector();
    	cvType = new CoeusVector();
    	cvStartYN = new CoeusVector();
    	cvStartDate = new CoeusVector();
    	cvEndYN = new CoeusVector();
    	cvEndDate = new CoeusVector();
    	cvGrantNumber = new CoeusVector();
    	
    	if (dbAnswers.size() > 0) {
    		for (int v=0; v < dbAnswers.size(); v++) {
    			bean = (QuestionAnswerBean) dbAnswers.get(v);
            	kirschBean = new KirschsteinNRSABean();

                switch (bean.getQuestionId()) {
                case KIRSCH_LEVEL:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setLevel(bean.getAnswer());
                    	cvLevel.add(kirschBean);
                    }
                    break;
                case KIRSCH_TYPE:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setType(bean.getAnswer());
                    	cvType.add(kirschBean);
                    }
                    break; 
                case KIRSCH_START_YN:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setStartDateKnown(bean.getAnswer());
                    	cvStartYN.add(kirschBean);
                    	
                    	if (bean.getAnswer().equals("N")) {
                    		kirschBean = new KirschsteinNRSABean();
                    		cvStartDate.add(kirschBean);
                    	}
                    }
                    break;
                case KIRSCH_START_DATE:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setStartDate(bean.getAnswer());
                    	cvStartDate.add(kirschBean);
                    }
                    break;
                case KIRSCH_END_YN:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setEndDateKnown(bean.getAnswer());
                    	cvEndYN.add(kirschBean);
                    	
                    	if (bean.getAnswer().equals("N")) {
                    		kirschBean = new KirschsteinNRSABean();
                    		cvEndDate.add(kirschBean);
                    	}
                    }
                    break;
                case KIRSCH_END_DATE:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setEndDate(bean.getAnswer());
                    	cvEndDate.add(kirschBean);
                    }
                    break;
                case KIRSCH_GRANT_NO:
                    if (bean.getAnswer() != null) {
                    	kirschBean.setGrantNumber(bean.getAnswer());
                    	cvGrantNumber.add(kirschBean);
                    }
                    break;
                }
    		}
    	}
    }
    
    /* Convert strings to dates */
    public Calendar convertDateStringToCalendar(String dateStr) {
    	try {
    		java.util.GregorianCalendar calDate = new java.util.GregorianCalendar();

    		DateUtils dtUtils = new DateUtils();
    		if (dateStr != null) {

    			if (dateStr.indexOf('-')!= -1) { // if the format obtd is YYYY-MM-DD
    				dateStr = dtUtils.formatDate(dateStr,"MM/dd/yyyy");
    			}
    			
    			calDate.set(Integer.parseInt(dateStr.substring(6,10)),
    					Integer.parseInt(dateStr.substring(0,2)) - 1,
                        Integer.parseInt(dateStr.substring(3,5))) ;

    			return calDate ;
    		}
        }
    	catch (Exception ex) {

        }
        return null;
    }

	public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException {
        this.propNumber = (String) ht.get("PROPOSAL_NUMBER");
        return getPHSFellowshipSupplemental();
    }   
	
	private class KirschsteinNRSABean implements java.io.Serializable {

		private String level;
		private String type;
		private String startDateKnown;
		private String startDate;
		private String endDateKnown;
		private String endDate;
		private String grantNumber;
		
	    private KirschsteinNRSABean() {
	    	
	    }
	    
	    private String getLevel() {
	    	return level;
	    }
	    
	    private void setLevel(String level) {
	    	this.level = level;
	    }
	    
	    private String getType() {
	    	return type;
	    }
	    
	    private void setType(String type) {
	    	this.type = type;
	    }

	    private String getStartDateKnown() {
	    	return startDateKnown;
	    }
	    
	    private void setStartDateKnown(String startDateKnown) {
	    	this.startDateKnown = startDateKnown;
	    }
	    
	    private String getStartDate() {
	    	return startDate;
	    }
	    
	    private void setStartDate(String startDate) {
	    	this.startDate = startDate;
	    }
	    
	    private String getEndDateKnown() {
	    	return endDateKnown;
	    }
	    
	    private void setEndDateKnown(String endDateKnown) {
	    	this.endDateKnown = endDateKnown;
	    }
	    
	    private String getEndDate() {
	    	return endDate;
	    }
	    
	    private void setEndDate(String endDate) {
	    	this.endDate = endDate;
	    }
	    
	    private String getGrantNumber() {
	    	return grantNumber;
	    }
	    
	    private void setGrantNumber(String grantNumber) {
	    	this.grantNumber = grantNumber;
	    }
		
	}
}
