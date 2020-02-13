/*
 * PHS398ResearchPlanStream_V4_0.java
 *
 * Copyright (c) Vanderbilt University and Medical Center
 * @author  Jill McAfee
 * @date	May 25, 2017
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
import gov.grants.apply.forms.phs398_researchplan_4_0_v4.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.bind.JAXBException;

public class PHS398ResearchPlanStream_V4_0 extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchplan_4_0_v4.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    private PHS398ResearchPlanTxnBean_V3_0 phs398ResearchPlanTxnBean; 
     
    private String propNumber;
  
    /** Creates a new instance of PHS398ResearchPlanStream_V4_0 */
    public PHS398ResearchPlanStream_V4_0(){
        objFactory = new gov.grants.apply.forms.phs398_researchplan_4_0_v4.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();  
    }
    
    private PHS398ResearchPlan40Type getPHS398ResearchPlanV4_0()
    		throws CoeusXMLException,CoeusException,DBException,JAXBException {
                    
    	PHS398ResearchPlan40Type phs398ResearchPlan40Type = objFactory.createPHS398ResearchPlan40();
        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try {
        	/** FormVersion */ 
        	phs398ResearchPlan40Type.setFormVersion("4.0");
          
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
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeIntroductionToApplicationType();
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
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.SpecificAimsType
                            specificAims = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeSpecificAimsType();
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
                    	 PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.ResearchStrategyType
                         	researchStrategy = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeResearchStrategyType();
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
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.ProgressReportPublicationListType  progressReportPublicationList
                            = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeProgressReportPublicationListType();
                        progressReportPublicationList.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgressReportPublicationList(progressReportPublicationList);                              
                     }
                  }                  
               }

               if (narrativeType == 30) { //VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                       
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.VertebrateAnimalsType
                            vertebrateAnimals = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeVertebrateAnimalsType();
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
                        
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.SelectAgentResearchType
                            selectAgentResearch = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeSelectAgentResearchType();
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
                       
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.MultiplePDPILeadershipPlanType
                            multiplePILeadershipPlan = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeMultiplePDPILeadershipPlanType();
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
                       
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.ConsortiumContractualArrangementsType
                            consortiumContractualArrangements = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeConsortiumContractualArrangementsType();
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
                       
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.LettersOfSupportType
                            lettersOfSupport = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeLettersOfSupportType();
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
                        
                        PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.ResourceSharingPlansType
                            resourceSharingPlans = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeResourceSharingPlansType();
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
                         
                         PHS398ResearchPlan40Type.ResearchPlanAttachmentsType.KeyBiologicalAndOrChemicalResourcesType
                         	keyBiologicalAndOrChemicalResources = objFactory.createPHS398ResearchPlan40TypeResearchPlanAttachmentsTypeKeyBiologicalAndOrChemicalResourcesType();
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
           
           phs398ResearchPlan40Type.setResearchPlanAttachments(researchPlanAttachments);
           return phs398ResearchPlan40Type;
           
         }
        catch (JAXBException jaxbEx) {
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ResearchPlanStream_V3_0","getPHS398ResearchPlanV3_0");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }        
                    
 }    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResearchPlanV4_0();
    }     
    
}


