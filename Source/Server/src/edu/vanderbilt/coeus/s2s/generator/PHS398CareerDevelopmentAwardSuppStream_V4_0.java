/*
 * PHS398CareerDevelopmentAwardSuppStream_V4_0.java
 *
 * Copyright (c) Vanderbilt University and Medical Center
 * @author  Jill McAfee
 * @date	May 26, 2017
 */
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.vanderbilt.coeus.s2s.bean.PHS398CareerDevelopmentAwardSuppTxnBean_V3_0;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.questionnaire.bean.QuestionAnswerBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.bind.JAXBException;

import gov.grants.apply.forms.phs398_careerdevelopmentawardsup_4_0_v4.*;
import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.s2s.generator.*;

/**
 *
 * @author Jill McAfee
 */
public class PHS398CareerDevelopmentAwardSuppStream_V4_0 extends S2SBaseStream { 
    private gov.grants.apply.forms.phs398_careerdevelopmentawardsup_4_0_v4.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    private PHS398CareerDevelopmentAwardSuppTxnBean_V3_0 phs398CareerDevTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
	private HashMap questionnaireData;
    
    public static final int CITIZENSHIP = 586;
    public static final int NON_CITIZEN_TYPE = 587;
    public static final int VISA_STATUS = 588;
    
	private HashMap<Integer, String> instanceData;
	private String citizenship;
	private String nonCitizenType = null;
	private String visaStatus = null;
    
     /** Creates a new instance of PHS398CareerDevelopmentAwardSuppStream_V4_0 */
    public PHS398CareerDevelopmentAwardSuppStream_V4_0(){
    	objFactory = new gov.grants.apply.forms.phs398_careerdevelopmentawardsup_4_0_v4.ObjectFactory();
    	attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
    	globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
      
    	xmlGenerator = new CoeusXMLGenrator();  
    }    

    private PHS398CareerDevelopmentAwardSup40Type getPHS398CareerDevAwardSupp()
		   throws CoeusXMLException,CoeusException,DBException,JAXBException {
     
    	PHS398CareerDevelopmentAwardSup40Type phs398CareerDevAwardType = objFactory.createPHS398CareerDevelopmentAwardSup40();
    	PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType attType =
    			objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsType();
      
    	gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup =
    			attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
    	Attachment attachment = null;
    	
        questionnaireData = processQuestionnaireAnswers();
       
    	try {
     
    		/**
	        *FormVersion
	        */ 
    		phs398CareerDevAwardType.setFormVersion("4.0");
      
	       /**
	        * citizenship
	        */
    		citizenship = "N: No";
    		if (questionnaireData.get(CITIZENSHIP).equals("Y")) {
    			citizenship = "Y: Yes";
    		}
    		phs398CareerDevAwardType.setCitizenshipIndicator(citizenship); 
    		
    		if (questionnaireData.get(NON_CITIZEN_TYPE) != null) {
    			nonCitizenType = (String) questionnaireData.get(NON_CITIZEN_TYPE); 
    		}
			phs398CareerDevAwardType.setIsNonUSCitizenship(nonCitizenType);
			
    		if (questionnaireData.get(VISA_STATUS) != null) {
	    		if (questionnaireData.get(VISA_STATUS).equals("Y")) {
	    			visaStatus = "Y: Yes";
	    		}
	    		
	    		if (questionnaireData.get(VISA_STATUS).equals("N")) {
	    			visaStatus = "N: No";
	    		}
    		}
    		phs398CareerDevAwardType.setPermanentResidentByAwardIndicator(visaStatus); 
    		
            /**
             * attachments
             */
	        String description;
	        int narrativeType;
	        int moduleNum;
	        ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
		    ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean;
                  
		    Vector vctNarrative = proposalNarrativeTxnBean.getPropNarrativePDFForProposal(propNumber); 
     
		    S2STxnBean s2sTxnBean = new S2STxnBean();
		    LinkedHashMap hmArg = new LinkedHashMap();
                 
		    HashMap hmNarrative = new HashMap();
 
		    int size=vctNarrative==null?0:vctNarrative.size();
		    for (int row=0; row < size;row++) {
		    	proposalNarrativePDFSourceBean = (ProposalNarrativePDFSourceBean) vctNarrative.elementAt(row);
                       
		    	moduleNum = proposalNarrativePDFSourceBean.getModuleNumber();   
		    	hmNarrative = new HashMap();
		    	hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
		    	narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
		    	description = hmNarrative.get("DESCRIPTION").toString();
	  
		    	hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
		    	hmArg.put(ContentIdConstants.DESCRIPTION, description);
	           
		    	attachment = getAttachment(hmArg);
           
		    	// Introduction to Application
		    	if (narrativeType == 70) {        
		    		if (attachment == null) {
		    			proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);      
		    			Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		    			if (narrativeAttachment.getContent() != null){
		    				attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
		    				PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.IntroductionToApplicationType
		    					introductionToApplication = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeIntroductionToApplicationType();
		    				introductionToApplication.setAttFile(attachedFileType);
		    				attType.setIntroductionToApplication(introductionToApplication);                         
		    			}
		    		}                  
		    	}
           
		    	// Candidate Information and Goals
		    	if (narrativeType == 204) {
		    		if (attachment == null) {
		    			proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);                   
		    			Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		    			if (narrativeAttachment.getContent() != null){
		    				attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
		    				PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.CandidateInformationAndGoalsType
		    					candidateInfoAndGoals = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeCandidateInformationAndGoalsType();
		    				candidateInfoAndGoals.setAttFile(attachedFileType);
		    				attType.setCandidateInformationAndGoals(candidateInfoAndGoals);  
	                  
		    			}
		    		}                  
		    	}
	           
	           // Specific Aims
		    	if (narrativeType == 71) {
		    		if (attachment == null) {
		    			proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);                   
		    			Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		    			if (narrativeAttachment.getContent() != null){
		    				attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
		    				PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.SpecificAimsType
		    					specificAims = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeSpecificAimsType();
		    				specificAims.setAttFile(attachedFileType);
		    				attType.setSpecificAims(specificAims);  
	                  
		    			}
		    		}                  
		    	}
		    	
		    	// Research Strategy
		    	if (narrativeType == 128) {
		    		if (attachment == null) {
		    			proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);                   
		    			Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
		    			if (narrativeAttachment.getContent() != null){
		    				attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
		    				PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.ResearchStrategyType
		    					researchStrategy = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeResearchStrategyType();
		    				researchStrategy.setAttFile(attachedFileType);
		    				attType.setResearchStrategy(researchStrategy);  
	                  
		    			}
		    		}                  
		    	}
	           
	           // Progress Report Publication List
	           if (narrativeType == 80) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.ProgressReportPublicationListType
	                       progressReportPublicationList  = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeProgressReportPublicationListType();
	                    progressReportPublicationList.setAttFile(attachedFileType);
	                    attType.setProgressReportPublicationList(progressReportPublicationList);                              
	                 }
	              }                  
	           }
	           
	           // Responsible Conduct of Research
	           if (narrativeType == 65) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.ResponsibleConductOfResearchType
	                            conduct = 
	                            objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeResponsibleConductOfResearchType();
	                    conduct.setAttFile(attachedFileType);
	                    attType.setResponsibleConductOfResearch(conduct);
	                 }
	              }                  
	           }
	           
	           // Mentoring Plan
	           if (narrativeType == 66) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.MentoringPlanType
	                            mentoring = 
	                            objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeMentoringPlanType();
	                    mentoring.setAttFile(attachedFileType);
	                    attType.setMentoringPlan(mentoring);
	                 }
	              }                  
	           }
	           
	           // Mentor Statements and Letters
	           if (narrativeType == 67) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.StatementsOfSupportType
	                            statements = 
	                            objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeStatementsOfSupportType();
	                    statements.setAttFile(attachedFileType);
	                    attType.setStatementsOfSupport(statements);
	                 }
	              }                  
	           }
	           
	           // Letters of Support
	           if (narrativeType == 144) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.LettersOfSupportType
	                    	lettersOfSupport = objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeLettersOfSupportType();
	                    lettersOfSupport.setAttFile(attachedFileType);
	                    attType.setLettersOfSupport(lettersOfSupport);
	                 }
	              }                  
	           }
	           
	           
	           // Institutional Environment
	           if (narrativeType == 68) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.InsitutionalEnvironmentType
	                            env = 
	                            objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeInsitutionalEnvironmentType();
	                    env.setAttFile(attachedFileType);
	                    attType.setInsitutionalEnvironment(env);
	                 }
	              }                  
	           }
	           
	           // Institutional Commitment
	           if (narrativeType == 69) {
	              if (attachment == null) {
	                proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	                 Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                 if (narrativeAttachment.getContent() != null){
	                    attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                    PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.InstitutionalCommitmentType
	                            com = 
	                            objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeInstitutionalCommitmentType();
	                    com.setAttFile(attachedFileType);
	                    attType.setInstitutionalCommitment(com);
	                 }
	              }                  
	           }
	           
	           // Vertebrate Animals
	           if (narrativeType == 78) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	        		   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	        		   if (narrativeAttachment.getContent() != null){
	        			   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
	        			   PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.VertebrateAnimalsType vertebrateAnimals =
	        					   objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeVertebrateAnimalsType();
	        			   vertebrateAnimals.setAttFile(attachedFileType);
	        			   attType.setVertebrateAnimals(vertebrateAnimals); 
	        		   }
	        	   }                  
	           }              
	           
	           // Select Agent Research
	           if (narrativeType == 81) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	        		   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	        		   if (narrativeAttachment.getContent() != null){
	        			   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	        			   PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.SelectAgentResearchType selectAgentResearch =
	        					   objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeSelectAgentResearchType();
	        			   selectAgentResearch.setAttFile(attachedFileType);
	        			   attType.setSelectAgentResearch(selectAgentResearch);
	        		   }
	        	   }                  
	           }
	         
	           // Career Consortium Contract
	           if (narrativeType == 83) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	        		   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	        		   if (narrativeAttachment.getContent() != null){
	        			   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	        			   PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.ConsortiumContractualArrangementsType consort =
	        					   objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeConsortiumContractualArrangementsType();
	                    consort.setAttFile(attachedFileType);
	                    attType.setConsortiumContractualArrangements(consort);
	                 }
	              }                  
	           }
	           
	           // Resource Sharing Plan
	           if (narrativeType == 84) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	        		   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	        		   if (narrativeAttachment.getContent() != null){
	        			   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	        			   PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.ResourceSharingPlansType share =
	        					   objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeResourceSharingPlansType();
	        			   share.setAttFile(attachedFileType);
	        			   attType.setResourceSharingPlans(share);
	        		   }
	        	   }                  
	           	}
	           
	           // Authentication of Key Biological and/or Chemical Resources
	           if (narrativeType == 206) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                 
	        		   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	        		   if (narrativeAttachment.getContent() != null){
	        			   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	        			   PHS398CareerDevelopmentAwardSup40Type.CareerDevelopmentAwardAttachmentsType.KeyBiologicalAndOrChemicalResourcesType biochem =
	        					   objFactory.createPHS398CareerDevelopmentAwardSup40TypeCareerDevelopmentAwardAttachmentsTypeKeyBiologicalAndOrChemicalResourcesType();
	        			   biochem.setAttFile(attachedFileType);
	        			   attType.setKeyBiologicalAndOrChemicalResources(biochem);
	        		   }
	        	   }                  
	           	}
	           
	           // Appendix
	           if (narrativeType == 85) {
	        	   if (attachment == null) {
	        		   proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
	                   Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
	                   if (narrativeAttachment.getContent() != null){                           
	                	   attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
	                       attachmentGroup.getAttachedFile().add(attachedFileType);
	                   }
	        	   } 
	           }
		    }
		    
		    if (attachmentGroup != null && attachmentGroup.getAttachedFile().size()  > 0 ) {
		    	attType.setAppendix(attachmentGroup);
		    }
      
		    if (attType != null) {
		    	phs398CareerDevAwardType.setCareerDevelopmentAwardAttachments(attType);
		    }
       
    	}
    	catch (JAXBException jaxbEx) {
    		UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398CareerDevelopmentAwardSup40Type","getPHS398CareerDevAwardSupp()");
    		throw new CoeusXMLException(jaxbEx.getMessage());
    	}     
  
    	return phs398CareerDevAwardType;
    
    }
  
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398CareerDevAwardSupp();
    }
    
    /* Gets questionnaire answers from the database */
    private CoeusVector getQuestionnaireAnswers() throws CoeusException, DBException {
    	CoeusVector cvAnswers = new CoeusVector();
        
    	phs398CareerDevTxnBean = new PHS398CareerDevelopmentAwardSuppTxnBean_V3_0();
    	cvAnswers = phs398CareerDevTxnBean.getQuestionnaireAnswers(propNumber);

    	return cvAnswers;
    }
    
    /* Take questionnaire answers and put in CoeusVector for easy access by question ID */
    private HashMap processQuestionnaireAnswers() {
    	CoeusVector cvForm = new CoeusVector();
	   
    	try {
    		cvForm = getQuestionnaireAnswers();
    	} catch (CoeusException e) {
    		UtilFactory.log("CoeusException:  Unable to retrieve questionnaire answers for PHS Career Development Award Supplement V4-0.");
    		e.printStackTrace();
    	} catch (DBException e) {
    		UtilFactory.log("DBException:  Unable to retrieve questionnaire answers for PHS Career Development Award Supplement V4-0.");
    		e.printStackTrace();
    	}

    	// Extract the questionnaire answers from the returned data
    	QuestionAnswerBean bean = new QuestionAnswerBean();
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
	   
    	if (cvForm.size() > 0) {
    		for (int v=0; v < cvForm.size(); v++) {
    			bean = (QuestionAnswerBean) cvForm.get(v);
    			map.put(bean.getQuestionId(),bean.getAnswer());
    		}
    	}
    	return map;
    }

    /* Return questionnaire answer or 0 given a question ID */
    private Integer getAnswer(int question_id) {
    	Integer answer = 0;
    	
    	if (instanceData.containsKey(question_id)) {
    		answer = Integer.parseInt(instanceData.get(question_id));
    	}
    	
    	return answer;
    }
    
}
