/*
 * PHS398ResearchTrainingProgPlanStream_V4_0.java
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

import edu.vanderbilt.coeus.s2s.bean.PHS398ResearchTrainingProgPlanTxnBean_V3_0;
import gov.grants.apply.forms.phs398_researchtrainingprogramplan_4_0_v4.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.bind.JAXBException;

public class PHS398ResearchTrainingProgPlanStream_V4_0 extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchtrainingprogramplan_4_0_v4.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    private PHS398ResearchTrainingProgPlanTxnBean_V3_0 phs398ResearchTrainingProgPlanTxnBean;
     
    private String propNumber;
  
    /** Creates a new instance of PHS398ResearchTrainingProgPlanStream_V4_0 */
    public PHS398ResearchTrainingProgPlanStream_V4_0(){
        objFactory = new gov.grants.apply.forms.phs398_researchtrainingprogramplan_4_0_v4.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();  
    }
    
    private PHS398ResearchTrainingProgramPlan40Type getPHS398ResearchTrainingProgramPlanV3_0()
    		throws CoeusXMLException,CoeusException,DBException,JAXBException{
                    
    	PHS398ResearchTrainingProgramPlan40Type phs398ResearchTrainingProgramPlan40Type = objFactory.createPHS398ResearchTrainingProgramPlan40();
        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try {
        	/** FormVersion */ 
        	phs398ResearchTrainingProgramPlan40Type.setFormVersion("4.0");
          
        	HashMap hmInfo = new HashMap();
        	phs398ResearchTrainingProgPlanTxnBean = new PHS398ResearchTrainingProgPlanTxnBean_V3_0();
        	hmInfo = phs398ResearchTrainingProgPlanTxnBean.getApplicationType(propNumber);
          
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
               if (narrativeType == 112) { //IntroductionToApplication
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        researchPlanAttachments.setIntroductionToApplication(introductionToApplication);                           
                     }
                  }                  
               } 
               
               if (narrativeType == 114) { //ProgramPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.ProgramPlanType
                            programPlan = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeProgramPlanType();
                        programPlan.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgramPlan(programPlan);                          
                     }
                  }                  
               } 

               if (narrativeType == 116) { //ResponsibleConductOfResearch
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                    	 attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                    	 PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.ResponsibleConductOfResearchType
                    	 	responsibleConductOfResearch = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeResponsibleConductOfResearchType();
                    	 responsibleConductOfResearch.setAttFile(attachedFileType);
                    	 researchPlanAttachments.setResponsibleConductOfResearch(responsibleConductOfResearch);
                     }
                  }
               }
               
               if (narrativeType == 202) { //MethodsForEnhancingReproducibility
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.MethodsForEnhancingReproducibilityType  
                        	methodsForEnhancingReproducibility = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeMethodsForEnhancingReproducibilityType();
                        methodsForEnhancingReproducibility.setAttFile(attachedFileType);
                        researchPlanAttachments.setMethodsForEnhancingReproducibility(methodsForEnhancingReproducibility);                              
                     }
                  }                  
               }

               if (narrativeType == 121) { //MultiplePDPILeadershipPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.MultiplePDPILeadershipPlanType
                        	multiplePDPILeadershipPlan = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeMultiplePDPILeadershipPlanType();
                        multiplePDPILeadershipPlan.setAttFile(attachedFileType);
                        researchPlanAttachments.setMultiplePDPILeadershipPlan(multiplePDPILeadershipPlan);                       
                     }
                  }                  
               }  
               
               if (narrativeType == 117) { //ProgressReport
                   if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                      
                      Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                      if (narrativeAttachment.getContent() != null){
                         attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.ProgressReportType
                         	progressReport = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeProgressReportType();
                         progressReport.setAttFile(attachedFileType);
                         researchPlanAttachments.setProgressReport(progressReport);                       
                      }
                   }                  
                } 
               
               if (narrativeType == 123) { //ParticipatingFacultyBiosketches
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.ParticipatingFacultyBiosketchesType 
                        	participatingFacultyBiosketches = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeParticipatingFacultyBiosketchesType();
                        participatingFacultyBiosketches.setAttFile(attachedFileType);
                        researchPlanAttachments.setParticipatingFacultyBiosketches(participatingFacultyBiosketches);
                     }
                  }                  
               }

               if (narrativeType == 125) { //LettersOfSupport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.LettersOfSupportType
                        	lettersOfSupport = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeLettersOfSupportType();
                        lettersOfSupport.setAttFile(attachedFileType);
                        researchPlanAttachments.setLettersOfSupport(lettersOfSupport); 
                     }
                  }                  
               }  
               

               if (narrativeType == 124) { //DataTables
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                       
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.DataTablesType
                        	dataTables = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeDataTablesType();
                        dataTables.setAttFile(attachedFileType);
                        researchPlanAttachments.setDataTables(dataTables); 
                     }
                  }                  
               } 
               
               if (narrativeType == 119) { //VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.VertebrateAnimalsType
                            vertebrateAnimals = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeVertebrateAnimalsType();
                        vertebrateAnimals.setAttFile(attachedFileType);
                        researchPlanAttachments.setVertebrateAnimals(vertebrateAnimals);
                     }
                  }                  
               }
               
               if (narrativeType == 120) { //SelectAgentResearch
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.SelectAgentResearchType
                           	selectAgentResearchType = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeSelectAgentResearchType();
                        selectAgentResearchType.setAttFile(attachedFileType);
                        researchPlanAttachments.setSelectAgentResearch(selectAgentResearchType); 
                     }
                  }                  
               }
               
               if (narrativeType == 122) { //ConsortiumContractualArrangements
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchTrainingProgramPlan40Type.ResearchTrainingProgramPlanAttachmentsType.ConsortiumContractualArrangementsType
                        	consortiumContractualArrangementsType = objFactory.createPHS398ResearchTrainingProgramPlan40TypeResearchTrainingProgramPlanAttachmentsTypeConsortiumContractualArrangementsType();
                        consortiumContractualArrangementsType.setAttFile(attachedFileType);
                        researchPlanAttachments.setConsortiumContractualArrangements(consortiumContractualArrangementsType);                            
                     }
                  }                  
               }
               
               /** START appendix */
               if (narrativeType == 126) { //Appendix
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
           
           phs398ResearchTrainingProgramPlan40Type.setResearchTrainingProgramPlanAttachments(researchPlanAttachments);
           return phs398ResearchTrainingProgramPlan40Type;
           
         }
        catch (JAXBException jaxbEx) {
            UtilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ResearchTrainingProgPlanStream_V3_0","getPHS398ResearchPlanV3_0");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }        
                    
 }    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResearchTrainingProgramPlanV3_0();
    }     
    
}


