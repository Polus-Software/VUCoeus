/*
 * PHS398ResearchTrainingProgPlanStream_V3_0.java
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

import edu.vanderbilt.coeus.s2s.bean.PHS398ResearchTrainingProgPlanTxnBean_V3_0;
import gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;

import javax.xml.bind.JAXBException;

public class PHS398ResearchTrainingProgPlanStream_V3_0 extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    private PHS398ResearchTrainingProgPlanTxnBean_V3_0 phs398ResearchTrainingProgPlanTxnBean;
     
    private String propNumber;
  
    /** Creates a new instance of PHS398ResearchPlanStream */
    public PHS398ResearchTrainingProgPlanStream_V3_0(){
        objFactory = new gov.grants.apply.forms.phs398_researchtrainingprogramplan_v3_0.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        xmlGenerator = new CoeusXMLGenrator();  
    }
    
    private PHS398ResearchTrainingProgramPlan30Type getPHS398ResearchTrainingProgramPlanV3_0()
    		throws CoeusXMLException,CoeusException,DBException,JAXBException{
                    
    	PHS398ResearchTrainingProgramPlan30Type PHS398ResearchTrainingProgramPlan30Type = objFactory.createPHS398ResearchTrainingProgramPlan30();
        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsType();
     
        //PHS398ResearchPlan30Type.ApplicationTypeType applicationType = objFactory.createPHS398ResearchPlan30TypeApplicationTypeType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try {
        	/** FormVersion */ 
        	PHS398ResearchTrainingProgramPlan30Type.setFormVersion("3.0");
          
        	//applicationType - is this still needed?
        	HashMap hmInfo = new HashMap();
        	phs398ResearchTrainingProgPlanTxnBean = new PHS398ResearchTrainingProgPlanTxnBean_V3_0();
        	hmInfo = phs398ResearchTrainingProgPlanTxnBean.getApplicationType(propNumber);
        	//if (hmInfo.get("APPLICATIONTYPE") != null) {
        	//  applicationType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
        	//  PHS398ResearchPlan30Type.setApplicationType(applicationType);
        	//}
          
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeIntroductionToApplicationType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgramPlanType
                            programPlan = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgramPlanType();
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
                    	 PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ResponsibleConductOfResearchType
                    	 	responsibleConductOfResearch = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeResponsibleConductOfResearchType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MethodsForEnhancingReproducibilityType  
                        	methodsForEnhancingReproducibility = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMethodsForEnhancingReproducibilityType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.MultiplePDPILeadershipPlanType
                        	multiplePDPILeadershipPlan = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeMultiplePDPILeadershipPlanType();
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
                         PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ProgressReportType
                         	progressReport = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeProgressReportType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ParticipatingFacultyBiosketchesType 
                        	participatingFacultyBiosketches = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeParticipatingFacultyBiosketchesType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.LettersOfSupportType
                        	lettersOfSupport = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeLettersOfSupportType();
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
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataTablesType
                        	dataTables = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataTablesType();
                        dataTables.setAttFile(attachedFileType);
                        researchPlanAttachments.setDataTables(dataTables); 
                     }
                  }                  
               } 
               
               if (narrativeType == 118) { //HumanSubjects
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.HumanSubjectsType
                            humanSubjects = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeHumanSubjectsType();
                        humanSubjects.setAttFile(attachedFileType);
                        researchPlanAttachments.setHumanSubjects(humanSubjects);
                     }
                  }                  
               }
               if (narrativeType == 203) { //DataSafetyMonitoringPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.DataSafetyMonitoringPlanType
                            dataSafetyMonitoringPlan = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeDataSafetyMonitoringPlanType();
                        dataSafetyMonitoringPlan.setAttFile(attachedFileType);
                        researchPlanAttachments.setDataSafetyMonitoringPlan(dataSafetyMonitoringPlan);
                     }
                  }                  
               }
               
               if (narrativeType == 119) { //VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.VertebrateAnimalsType
                            vertebrateAnimals = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeVertebrateAnimalsType();
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
                       
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.SelectAgentResearchType
                           	selectAgentResearchType = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeSelectAgentResearchType();
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
                        
                        PHS398ResearchTrainingProgramPlan30Type.ResearchTrainingProgramPlanAttachmentsType.ConsortiumContractualArrangementsType
                        	consortiumContractualArrangementsType = objFactory.createPHS398ResearchTrainingProgramPlan30TypeResearchTrainingProgramPlanAttachmentsTypeConsortiumContractualArrangementsType();
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
           
           PHS398ResearchTrainingProgramPlan30Type.setResearchTrainingProgramPlanAttachments(researchPlanAttachments);
           return PHS398ResearchTrainingProgramPlan30Type;
           
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


