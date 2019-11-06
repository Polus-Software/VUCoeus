
package edu.mit.coeus.s2s.generator;

import edu.mit.coeus.s2s.bean.S2STxnBean;

import edu.mit.coeus.s2s.bean.PHS398CareerDevTxnBean;
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
import gov.grants.apply.forms.phscareer_v1_1.*;
import edu.mit.coeus.exception.CoeusException;

/**
 *
 * @author ele
 */
public class PHS398_CareerDevAwardSup_V1_1Stream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phscareer_v1_1.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn bean
    private PHS398CareerDevTxnBean phs398CareerDevTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
    
     
   
     /** Creates a new instance of PHS398_CareerDevAwardSup_V1Stream */
    public PHS398_CareerDevAwardSup_V1_1Stream(){
        objFactory = new gov.grants.apply.forms.phscareer_v1_1.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
      
        xmlGenerator = new CoeusXMLGenrator();  
           
    }    

  private PHS398CareerDevelopmentAwardSup11Type getPHS398CareerDevAwardPlan()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
  
     
      PHS398CareerDevelopmentAwardSup11Type phs398CareerDevAwardType = objFactory.createPHS398CareerDevelopmentAwardSup11();
      PHS398CareerDevelopmentAwardSup11Type.ApplicationTypeType appType = objFactory.createPHS398CareerDevelopmentAwardSup11TypeApplicationTypeType();
      PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType attType =
             objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsType();
      
      gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
      Attachment attachment = null;
        
    
          
      phs398CareerDevTxnBean = new PHS398CareerDevTxnBean();
       
          try{
         
          /**
            *FormVersion
          */ 
            phs398CareerDevAwardType.setFormVersion("1.1");
          
           /**
            * citizenship
            */
            HashMap hmInfo = new HashMap();
            hmInfo = phs398CareerDevTxnBean.getCitizenship(propNumber);
            if (hmInfo.get("CITIZENSHIP") != null) {
                phs398CareerDevAwardType.setCitizenship(hmInfo.get("CITIZENSHIP").toString());
            }
                   
            /**
             * application Type
             */
           
            hmInfo = new HashMap();
          
            hmInfo = phs398CareerDevTxnBean.getApplicationType(propNumber);
            if (hmInfo.get("APPLICATIONTYPE") != null){
                appType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
                phs398CareerDevAwardType.setApplicationType(appType);
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
                 if (narrativeType == 70) {        
                  if (attachment == null) {
                     proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);      
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        attType.setIntroductionToApplication(introductionToApplication);                         
                     }
                   }                  
                  }
               
                //Specific Aims
                 if (narrativeType == 71) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);                   
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                    
                         PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.SpecificAimsType
                            specificAims = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeSpecificAimsType();
                         specificAims.setAttFile(attachedFileType);
                         attType.setSpecificAims(specificAims);  
                      
                    }
                  }                  
               }
               
                                
               
               //InclusionEnrollmentReport
                if (narrativeType == 79) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.InclusionEnrollmentReportType
                            inclusionEnrollmentReport = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeInclusionEnrollmentReportType();
                        inclusionEnrollmentReport.setAttFile(attachedFileType);
                        attType.setInclusionEnrollmentReport(inclusionEnrollmentReport);                              
                     }
                  }                  
               }
               
               //ProgressReportPublicationList
               if (narrativeType == 80) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ProgressReportPublicationListType
                           progressReportPublicationList  = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeProgressReportPublicationListType();
                        progressReportPublicationList.setAttFile(attachedFileType);
                        attType.setProgressReportPublicationList(progressReportPublicationList);                              
                     }
                  }                  
               }
               
               //ProtectionOfHumanSubjects
               if (narrativeType == 74) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ProtectionOfHumanSubjectsType
                          protectionOfHumanSubjects = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeProtectionOfHumanSubjectsType();
                        protectionOfHumanSubjects.setAttFile(attachedFileType);
                        attType.setProtectionOfHumanSubjects(protectionOfHumanSubjects);                       
                     }
                  }                  
               }               
               
               //InclusionOfWomenAndMinorities
               if (narrativeType == 75) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.InclusionOfWomenAndMinoritiesType
                           inclusionOfWomenAndMinorities = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeInclusionOfWomenAndMinoritiesType();
                        inclusionOfWomenAndMinorities.setAttFile(attachedFileType);
                        attType.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
                     }
                  }                  
               }
               
               //TargetedPlannedEnrollmentTable
                if (narrativeType == 76) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                      
                         PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.TargetedPlannedEnrollmentType
                                 targetedPlannedEnrollmentTable = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeTargetedPlannedEnrollmentType();
                        targetedPlannedEnrollmentTable.setAttFile(attachedFileType);
                        attType.setTargetedPlannedEnrollment(targetedPlannedEnrollmentTable);                             
                     }
                  }                  
               }
               
               //InclusionOfChildren
               if (narrativeType == 77) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.InclusionOfChildrenType
                                inclusionOfChildren = objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeInclusionOfChildrenType();
                        inclusionOfChildren.setAttFile(attachedFileType);
                        attType.setInclusionOfChildren(inclusionOfChildren); 
                     }
                  }                  
               }
               
               //VertebrateAnimals
               if (narrativeType == 78) {
               if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                         PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.VertebrateAnimalsType vertebrateAnimals =
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeVertebrateAnimalsType();
                        vertebrateAnimals.setAttFile(attachedFileType);
                        attType.setVertebrateAnimals(vertebrateAnimals); 
                     }
                  }                  
               }              
               
               //SelectAgentResearch
               if (narrativeType == 81) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.SelectAgentResearchType selectAgentResearch =
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeSelectAgentResearchType();
                        selectAgentResearch.setAttFile(attachedFileType);
                        attType.setSelectAgentResearch(selectAgentResearch);
                     }
                  }                  
               }
               
             
                 //PHS_Career_Consortium_Contract
                 if (narrativeType == 83) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ConsortiumContractualArrangementsType consort =
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeConsortiumContractualArrangementsType();
                        consort.setAttFile(attachedFileType);
                        attType.setConsortiumContractualArrangements(consort);
                     }
                  }                  
               }
               
                //PHS_Career_Resource_Sharing_Plan
                 if (narrativeType == 84) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ResourceSharingPlansType share =
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeResourceSharingPlansType();
                        share.setAttFile(attachedFileType);
                        attType.setResourceSharingPlans(share);
                     }
                  }                  
               }
               
          
                 
               
                //Candidate Background
               if (narrativeType == 62) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.CandidateBackgroundType
                                candidateBackground = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeCandidateBackgroundType();
                        candidateBackground.setAttFile(attachedFileType);
                        attType.setCandidateBackground(candidateBackground);
                     }
                  }                  
               }
               
               //Career Goals and Objectives
               if (narrativeType == 63) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.CareerGoalsAndObjectivesType
                                goalsAndObjectives = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeCareerGoalsAndObjectivesType();
                        goalsAndObjectives.setAttFile(attachedFileType);
                        attType.setCareerGoalsAndObjectives(goalsAndObjectives);
                     }
                  }                  
               }
            
                //Career Development And Training
               if (narrativeType == 64) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.CareerDevelopmentAndTrainingActivitiesType
                                careerDevAndTraining = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeCareerDevelopmentAndTrainingActivitiesType();
                        careerDevAndTraining.setAttFile(attachedFileType);
                        attType.setCareerDevelopmentAndTrainingActivities(careerDevAndTraining);
                     }
                  }                  
               }
               
               //Responsible Conduct of Research
               if (narrativeType == 65) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ResponsibleConductOfResearchType
                                conduct = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeResponsibleConductOfResearchType();
                        conduct.setAttFile(attachedFileType);
                        attType.setResponsibleConductOfResearch(conduct);
                     }
                  }                  
               }
               
                //PHS398_Mentoring_Plan
               if (narrativeType == 66) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.MentoringPlanType
                                mentoring = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeMentoringPlanType();
                        mentoring.setAttFile(attachedFileType);
                        attType.setMentoringPlan(mentoring);
                     }
                  }                  
               }
               
                 //PHS398_Mentor_Statements_Letters
               if (narrativeType == 67) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.StatementsOfSupportType
                                statements = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeStatementsOfSupportType();
                        statements.setAttFile(attachedFileType);
                        attType.setStatementsOfSupport(statements);
                     }
                  }                  
               }
               
             //PSH398_Institutional_Environment
               if (narrativeType == 68) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.InsitutionalEnvironmentType
                                env = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeInsitutionalEnvironmentType();
                        env.setAttFile(attachedFileType);
                        attType.setInsitutionalEnvironment(env);
                     }
                  }                  
               }
               
                //PHS398_Institutional_Commitment
               if (narrativeType == 69) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.InstitutionalCommitmentType
                                com = 
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeInstitutionalCommitmentType();
                        com.setAttFile(attachedFileType);
                        attType.setInstitutionalCommitment(com);
                     }
                  }                  
               }

                 //PHSCareer_Research_Strategy
               if (narrativeType == 128) {
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398CareerDevelopmentAwardSup11Type.CareerDevelopmentAwardAttachmentsType.ResearchStrategyType
                                str =
                                objFactory.createPHS398CareerDevelopmentAwardSup11TypeCareerDevelopmentAwardAttachmentsTypeResearchStrategyType();
                        str.setAttFile(attachedFileType);
                        attType.setResearchStrategy(str);
                       
                     }
                  }
               }

               //PHS_Career_Appendix - can be multiple
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
            if (attachmentGroup != null && attachmentGroup.getAttachedFile().size()  > 0 )
              
                   attType.setAppendix(attachmentGroup);
          
           if (attType != null)    phs398CareerDevAwardType.setCareerDevelopmentAwardAttachments(attType);
                         
           
       }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398_CareerDevAwardSup_V1_1Stream","getPHS398CareerDevAwardPlan()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }     
      
      return phs398CareerDevAwardType;
    
  }
  
 public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398CareerDevAwardPlan();
    }     
    
}
