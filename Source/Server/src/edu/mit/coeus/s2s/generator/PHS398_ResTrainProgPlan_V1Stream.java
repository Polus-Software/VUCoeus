

package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.bean.S2STxnBean;
import edu.mit.coeus.s2s.bean.PHS398ResTrainProgTxnBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;
import gov.grants.apply.forms.phsrestrainprogplan_v1_0.*;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author ele
 */
public class PHS398_ResTrainProgPlan_V1Stream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phsrestrainprogplan_v1_0.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn bean
    private PHS398ResTrainProgTxnBean phs398ResTrainProgTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
    
     
   
     /** Creates a new instance of PHS398_CareerDevAwardSup_V1Stream */
    public PHS398_ResTrainProgPlan_V1Stream(){
        objFactory = new gov.grants.apply.forms.phsrestrainprogplan_v1_0.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
      
        xmlGenerator = new CoeusXMLGenrator();  
           
    }    

  private PHS398ResearchTrainingProgramPlanType getPHS398ResTrainProgPlan()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
  
      PHS398ResearchTrainingProgramPlanType phs398ResTrainProgPlanType =
              objFactory.createPHS398ResearchTrainingProgramPlan();
      PHS398ResearchTrainingProgramPlanType.ApplicationTypeType appType = objFactory.createPHS398ResearchTrainingProgramPlanTypeApplicationTypeType();
      PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType attType =
              objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsType();
    
      gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
      Attachment attachment = null;
      HashMap hmInfo;
    
      phs398ResTrainProgTxnBean = new PHS398ResTrainProgTxnBean();
       
          try{
         
          /**
            *FormVersion
          */
            phs398ResTrainProgPlanType.setFormVersion("1.0");
            
          
                   
            /**
             * application Type
             */
           
            hmInfo = new HashMap();
          
            hmInfo = phs398ResTrainProgTxnBean.getApplicationType(propNumber);
            if (hmInfo.get("APPLICATIONTYPE") != null){
                appType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
                phs398ResTrainProgPlanType.setApplicationType(appType);
             
             }
  
            /**
             * attachments
             */
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
               
                //IntroductionToApplication
                 if (narrativeType == 112) {
                  if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);      
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        attType.setIntroductionToApplication(introductionToApplication);                         
                     }
                   }                  
                  }
               
              
               //Background 
                 if (narrativeType == 113) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);        
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                      
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.BackgroundType
                                background = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeBackgroundType();
                        background.setAttFile(attachedFileType);
                        attType.setBackground(background);
                     }
                  }                  
               } 
               
                  //Program Plan
                 if (narrativeType == 114) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);

                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.ProgramPlanType
                                programPlan = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeProgramPlanType();
                        programPlan.setAttFile(attachedFileType);
                        attType.setProgramPlan(programPlan);
                     }
                  }
               }

               //Recruitment Plan
                 if (narrativeType == 115) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);

                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.RecruitmentAndRetentionPlanToEnhanceDiversityType
                                recruitment = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeRecruitmentAndRetentionPlanToEnhanceDiversityType();
                        recruitment.setAttFile(attachedFileType);
                        attType.setRecruitmentAndRetentionPlanToEnhanceDiversity(recruitment);
                     }
                  }
               }


               //Responsible conduct of research
                 if (narrativeType == 116) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);

                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.ResponsibleConductOfResearchType
                                conduct = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeResponsibleConductOfResearchType();
                        conduct.setAttFile(attachedFileType);
                        attType.setResponsibleConductOfResearch(conduct);
                     }
                  }
               }


              //Progress Report
                 if (narrativeType == 117) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){

                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);

                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.ProgressReportType
                                progress = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeProgressReportType();
                        progress.setAttFile(attachedFileType);
                        attType.setProgressReport(progress);
                     }
                  }
               }

               //HumanSubjects
               if (narrativeType == 118) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.HumanSubjectsType
                           humanSubjects = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeHumanSubjectsType();
                        humanSubjects.setAttFile(attachedFileType);
                        attType.setHumanSubjects(humanSubjects);
                     }
                  }                  
               }               
               

               //VertebrateAnimals
               if (narrativeType == 119) {
               if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                          PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.VertebrateAnimalsType
                                vertAnimals =objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeVertebrateAnimalsType();
                        vertAnimals.setAttFile(attachedFileType);
                        attType.setVertebrateAnimals(vertAnimals);
                     }
                  }                  
               }              
               
               //SelectAgentResearch
               if (narrativeType == 120) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.SelectAgentResearchType
                            selectAgentResearch = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeSelectAgentResearchType();
                        selectAgentResearch.setAttFile(attachedFileType);
                        attType.setSelectAgentResearch(selectAgentResearch);
                     }
                  }                  
               }
               
               //Multiple PD/PI leadership plan
               if (narrativeType == 121) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.MultiplePDPILeadershipPlanType
                            multiplePIPlan = objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeMultiplePDPILeadershipPlanType();
                        multiplePIPlan.setAttFile(attachedFileType);
                        attType.setMultiplePDPILeadershipPlan(multiplePIPlan);
                     }
                  }
               }

                 //Consortium contractual arrangments
               
                 if (narrativeType == 122) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.ConsortiumContractualArrangementsType
                             consort =objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeConsortiumContractualArrangementsType();
                        consort.setAttFile(attachedFileType);
                        attType.setConsortiumContractualArrangements(consort);
                     }
                  }                  
               }
               
               //Faculty biosketches

                 if (narrativeType == 123) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.ParticipatingFacultyBiosketchesType
                                 biosketches =objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeParticipatingFacultyBiosketchesType();
                        biosketches.setAttFile(attachedFileType);
                        attType.setParticipatingFacultyBiosketches(biosketches);
                     }
                  }
               }
          
              //Data Tables

                 if (narrativeType == 124) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.DataTablesType
                                 dataTables =objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeDataTablesType();
                        dataTables.setAttFile(attachedFileType);
                        attType.setDataTables(dataTables);
                     }
                  }
               }

               //Letters of support

                 if (narrativeType == 125) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                         PHS398ResearchTrainingProgramPlanType.ResearchTrainingProgramPlanAttachmentsType.LettersOfSupportType
                              letters =objFactory.createPHS398ResearchTrainingProgramPlanTypeResearchTrainingProgramPlanAttachmentsTypeLettersOfSupportType();
                        letters.setAttFile(attachedFileType);
                        attType.setLettersOfSupport(letters);
                     }
                  }
               }

               //Appendix - can be multiple
                  if (narrativeType == 126) {
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
            if (attachmentGroup != null && attachmentGroup.getAttachedFile().size()  > 0 )
               
                   attType.setAppendix(attachmentGroup);
          
           if (attType != null)   phs398ResTrainProgPlanType.setResearchTrainingProgramPlanAttachments(attType);

                         
           
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398_CareerDevAwardSup_V1Stream","getPHS398CareerDevAwardPlan()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }     
      
      return phs398ResTrainProgPlanType;
    
  }
  
 public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResTrainProgPlan();
    }     
    
}
