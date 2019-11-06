/*
 * PHS398ResearchPlanStream_V3_0.java
 *
 * Copyright (c) Vanderbilt University and Medical Center
 * @author  Jill McAfee
 * @date	February 3, 2016
 */
package edu.vanderbilt.coeus.s2s.generator;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.generator.ContentIdConstants;
import edu.mit.coeus.s2s.generator.S2SBaseStream;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;

import edu.vanderbilt.coeus.s2s.bean.PHS398ResearchPlanTxnBean_V3_0;
import gov.grants.apply.forms.phs398_researchplan_v3_0.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.bind.JAXBException;

public class PHS398ResearchPlanStream_V3_0 extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchplan_v3_0.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    private PHS398ResearchPlanTxnBean_V3_0 phs398ResearchPlanTxnBean;
     
    private String propNumber;
  
    /** Creates a new instance of PHS398ResearchPlanStream */
    public PHS398ResearchPlanStream_V3_0(){
        objFactory = new gov.grants.apply.forms.phs398_researchplan_v3_0.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();  
    }
    
    private PHS398ResearchPlan30Type getPHS398ResearchPlanV3_0()
    		throws CoeusXMLException,CoeusException,DBException,JAXBException {
                    
    	PHS398ResearchPlan30Type PHS398ResearchPlan30Type = objFactory.createPHS398ResearchPlan30();
        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsType();
     
        //PHS398ResearchPlan30Type.ApplicationTypeType applicationType = objFactory.createPHS398ResearchPlan30TypeApplicationTypeType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try {
        	/** FormVersion */ 
        	PHS398ResearchPlan30Type.setFormVersion("3.0");
          
        	HashMap hmInfo = new HashMap();
        	phs398ResearchPlanTxnBean = new PHS398ResearchPlanTxnBean_V3_0();
        	hmInfo = phs398ResearchPlanTxnBean.getApplicationType(propNumber);
          
        	String description;
        	int narrativeType;
        	int moduleNum;
        	ProposalNarrativeTxnBean proposalNarrativeTxnBean = new ProposalNarrativeTxnBean();
        	ProposalNarrativePDFSourceBean proposalNarrativePDFSourceBean ;
                      
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
                   
        		/** START form attachments */
        		if (narrativeType == 20) { //IntroductionToApplication
        			if (attachment == null) {
        				proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        researchPlanAttachments.setIntroductionToApplication(introductionToApplication);                           
                     }
                  }                  
               } 
               
               if (narrativeType == 21) { //SpecificAims
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SpecificAimsType
                            specificAims = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSpecificAimsType();
                        specificAims.setAttFile(attachedFileType);
                        researchPlanAttachments.setSpecificAims(specificAims);                          
                     }
                  }                  
               } 

               if (narrativeType == 111) { //ResearchStrategy 
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                    	 attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                    	 PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResearchStrategyType
                         	researchStrategy = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResearchStrategyType();
                    	 researchStrategy.setAttFile(attachedFileType);
                    	 researchPlanAttachments.setResearchStrategy(researchStrategy);
                     }
                  }
               }
               
               if (narrativeType == 44) { //ProgressReportPublicationList
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProgressReportPublicationListType  progressReportPublicationList
                            = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProgressReportPublicationListType();
                        progressReportPublicationList.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgressReportPublicationList(progressReportPublicationList);                              
                     }
                  }                  
               }

               if (narrativeType == 25) { //ProtectionOfHumanSubjects
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ProtectionOfHumanSubjectsType
                            protectionOfHumanSubjects = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeProtectionOfHumanSubjectsType();
                        protectionOfHumanSubjects.setAttFile(attachedFileType);
                        researchPlanAttachments.setProtectionOfHumanSubjects(protectionOfHumanSubjects);                       
                     }
                  }                  
               }  
               
               if (narrativeType == 29) { //DataSafetyMonitoringPlane
                   if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                      
                      Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                      if (narrativeAttachment.getContent() != null){
                         attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.DataSafetyMonitoringPlanType
                             dataSafetyMonitoringPlan = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeDataSafetyMonitoringPlanType();
                         dataSafetyMonitoringPlan.setAttFile(attachedFileType);
                         researchPlanAttachments.setDataSafetyMonitoringPlan(dataSafetyMonitoringPlan);                       
                      }
                   }                  
                } 
               
               if (narrativeType == 26) { //InclusionOfWomenAndMinorities
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfWomenAndMinoritiesType inclusionOfWomenAndMinorities =
                                objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfWomenAndMinoritiesType();
                        inclusionOfWomenAndMinorities.setAttFile(attachedFileType);
                        researchPlanAttachments.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
                     }
                  }                  
               }

               if (narrativeType == 28) { //InclusionOfChildren
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.InclusionOfChildrenType
                            inclusionOfChildren = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeInclusionOfChildrenType();
                        inclusionOfChildren.setAttFile(attachedFileType);
                        researchPlanAttachments.setInclusionOfChildren(inclusionOfChildren); 
                     }
                  }                  
               }  

               if (narrativeType == 30) { //VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                       
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.VertebrateAnimalsType
                            vertebrateAnimals = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeVertebrateAnimalsType();
                        vertebrateAnimals.setAttFile(attachedFileType);
                        researchPlanAttachments.setVertebrateAnimals(vertebrateAnimals); 
                     }
                  }                  
               } 
               
               if (narrativeType == 45) { //SelectAgentResearch
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.SelectAgentResearchType
                            selectAgentResearch = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeSelectAgentResearchType();
                        selectAgentResearch.setAttFile(attachedFileType);
                        researchPlanAttachments.setSelectAgentResearch(selectAgentResearch);
                     }
                  }                  
               }
               if (narrativeType == 46) { //MultiplePILeadershipPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.MultiplePDPILeadershipPlanType
                            multiplePILeadershipPlan = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeMultiplePDPILeadershipPlanType();
                        multiplePILeadershipPlan.setAttFile(attachedFileType);
                        researchPlanAttachments.setMultiplePDPILeadershipPlan(multiplePILeadershipPlan);
                     }
                  }                  
               }
               
               if (narrativeType == 31) { //ConsortiumContractualArrangements
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ConsortiumContractualArrangementsType
                            consortiumContractualArrangements = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeConsortiumContractualArrangementsType();
                        consortiumContractualArrangements.setAttFile(attachedFileType);
                        researchPlanAttachments.setConsortiumContractualArrangements(consortiumContractualArrangements);
                     }
                  }                  
               }
               
               if (narrativeType == 32) { //LettersOfSupport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.LettersOfSupportType
                            lettersOfSupport = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeLettersOfSupportType();
                        lettersOfSupport.setAttFile(attachedFileType);
                        researchPlanAttachments.setLettersOfSupport(lettersOfSupport); 
                     }
                  }                  
               }
               
               if (narrativeType == 33) { //ResourceSharingPlans
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.ResourceSharingPlansType
                            resourceSharingPlans = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeResourceSharingPlansType();
                        resourceSharingPlans.setAttFile(attachedFileType);
                        researchPlanAttachments.setResourceSharingPlans(resourceSharingPlans);                            
                     }
                  }                  
               }
               
               if (narrativeType == 201) { //KeyBiologicalAndOrChemicalResources
                   if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                      
                      Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                      if (narrativeAttachment.getContent() != null){
                         attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         
                         PHS398ResearchPlan30Type.ResearchPlanAttachmentsType.KeyBiologicalAndOrChemicalResourcesType
                         	keyBiologicalAndOrChemicalResources = objFactory.createPHS398ResearchPlan30TypeResearchPlanAttachmentsTypeKeyBiologicalAndOrChemicalResourcesType();
                         keyBiologicalAndOrChemicalResources.setAttFile(attachedFileType);
                         researchPlanAttachments.setKeyBiologicalAndOrChemicalResources(keyBiologicalAndOrChemicalResources);                            
                      }
                   }                  
                }
               
               /** START appendix */
               if (narrativeType == 34) { //Appendix
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attachmentGroup.getAttachedFile().add(attachedFileType);

                     }
                  }                  
               } /** END appendix */
               
           }  /** END form attachments */

           if (attachmentGroup != null)
               researchPlanAttachments.setAppendix(attachmentGroup);
           
            PHS398ResearchPlan30Type.setResearchPlanAttachments(researchPlanAttachments);
            return PHS398ResearchPlan30Type;
           
         }
        catch (JAXBException jaxbEx) {
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ResearchPlanStream_V3_0","getPHS398ResearchPlanV3_0");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }        
                    
 }    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResearchPlanV3_0();
    }     
    
}


