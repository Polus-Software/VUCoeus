/*
 * PHS398ResearchPlanV1_1Stream.java
 *
 * Copyright (c) Massachusetts Institute of Technology
 * 77 Massachusetts Avenue, Cambridge, MA 02139-4307
 * All rights reserved.
 */
package edu.mit.coeus.s2s.generator;

//import edu.mit.coeus.s2s.generator.stream.*;

import edu.mit.coeus.exception.CoeusException;
import edu.mit.coeus.propdev.bean.ProposalNarrativePDFSourceBean;
import edu.mit.coeus.propdev.bean.ProposalNarrativeTxnBean;
//import edu.mit.coeus.propdev.bean.web.GetNarrativeDocumentBean;
import edu.mit.coeus.s2s.bean.*;
import edu.mit.coeus.s2s.Attachment;
import edu.mit.coeus.s2s.bean.PHS398ResearchPlanTxnBean;
//import edu.mit.coeus.s2s.util.S2SHashValue;
//import edu.mit.coeus.utils.CoeusConstants;
//import edu.mit.coeus.utils.CoeusFunctions;
//import edu.mit.coeus.utils.CoeusVector;
import edu.mit.coeus.utils.UtilFactory;
import edu.mit.coeus.utils.dbengine.DBException;
import edu.mit.coeus.xml.generator.CoeusXMLException;
import edu.mit.coeus.xml.generator.CoeusXMLGenrator;
import gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.*;
import java.util.HashMap;
//import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.xml.bind.JAXBException;

/**
 * @author  jenlu 
 *
 */

 public class PHS398ResearchPlanV1_3Stream extends S2SBaseStream  { 
    private gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.ObjectFactory objFactory;
    private gov.grants.apply.system.attachments_v1.ObjectFactory attachmentsObjFactory;
    private gov.grants.apply.system.global_v1.ObjectFactory globalObjFactory;
    private CoeusXMLGenrator xmlGenerator;

    gov.grants.apply.system.attachments_v1.AttachedFileDataType attachedFileType;
    //txn bean
    private PHS398ResearchPlanTxnBean phs398ResearchPlanTxnBean;
     
    private String propNumber;
    private UtilFactory utilFactory;
   
  
    /** Creates a new instance of PHS398ResearchPlanStream */
    public PHS398ResearchPlanV1_3Stream(){
        objFactory = new gov.grants.apply.forms.phs398_researchplan_1_3_v1_3.ObjectFactory();
        attachmentsObjFactory = new gov.grants.apply.system.attachments_v1.ObjectFactory();
        globalObjFactory = new gov.grants.apply.system.global_v1.ObjectFactory();
        
      
        xmlGenerator = new CoeusXMLGenrator();  
           
    }    
    private PHS398ResearchPlan13Type getPHS398ResearchPlanV1_3()
                throws CoeusXMLException,CoeusException,DBException,JAXBException{
                    
        PHS398ResearchPlan13Type phs398ResearchPlan13Type = objFactory.createPHS398ResearchPlan13();
        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType researchPlanAttachments = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsType();
        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType otherResearchPlanSections =
                objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsType();
        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.HumanSubjectSectionType humanSubjectSection = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeHumanSubjectSectionType();
     
        PHS398ResearchPlan13Type.ApplicationTypeType applicationType = objFactory.createPHS398ResearchPlan13TypeApplicationTypeType();

        gov.grants.apply.system.attachments_v1.AttachmentGroupMin0Max100DataType attachmentGroup 
                = attachmentsObjFactory.createAttachmentGroupMin0Max100DataType();
        Attachment attachment = null;
       
        try{
          /**
            *FormVersion
          */ 
          phs398ResearchPlan13Type.setFormVersion("1.3");
          
          //applicationType 
          HashMap hmInfo = new HashMap();
          phs398ResearchPlanTxnBean = new PHS398ResearchPlanTxnBean();
          hmInfo = phs398ResearchPlanTxnBean.getApplicationType(propNumber);
          if (hmInfo.get("APPLICATIONTYPE") != null){
            applicationType.setTypeOfApplication(hmInfo.get("APPLICATIONTYPE").toString());
            phs398ResearchPlan13Type.setApplicationType(applicationType);
          }
          
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
  
//               String fileNameForOtherType = proposalNarrativePDFSourceBean.getFileName();
               
               hmNarrative = new HashMap();
               hmNarrative = s2sTxnBean.getNarrativeInfo(propNumber,moduleNum);
               narrativeType = Integer.parseInt(hmNarrative.get("NARRATIVE_TYPE_CODE").toString());
               description = hmNarrative.get("DESCRIPTION").toString();
      
               hmArg.put(ContentIdConstants.MODULE_NUMBER, Integer.toString(moduleNum));            
               hmArg.put(ContentIdConstants.DESCRIPTION, description);
               
               attachment = getAttachment(hmArg);
                   
               if (narrativeType == 20) {//IntroductionToApplication
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.IntroductionToApplicationType
                            introductionToApplication = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeIntroductionToApplicationType();
                        introductionToApplication.setAttFile(attachedFileType);
                        researchPlanAttachments.setIntroductionToApplication(introductionToApplication);                           
                     }
                  }                  
               } 
               
               if (narrativeType == 21) {//SpecificAims
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                    
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.SpecificAimsType
                            specificAims = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeSpecificAimsType();
                        specificAims.setAttFile(attachedFileType);
                        researchPlanAttachments.setSpecificAims(specificAims);                          
                     }
                  }                  
               } 
               
//               if (narrativeType == 22) {//BackgroundSignificance
//                  if (attachment == null) {
//                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
//
//                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
//                     if (narrativeAttachment.getContent() != null){
////                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
//
//                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
//
//                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.BackgroundSignificanceType
//                            backgroundSignificance = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeBackgroundSignificanceType();
//                        backgroundSignificance.setAttFile(attachedFileType);
//                        researchPlanAttachments.setBackgroundSignificance(backgroundSignificance);
//                     }
//                  }
//               }
//               if (narrativeType == 23) {//ProgressReport
//                  if (attachment == null) {
//                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
//
//                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
//                     if (narrativeAttachment.getContent() != null){
////                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
//
//                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
//                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.ProgressReportType
//                            progressReport = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeProgressReportType();
//                        progressReport.setAttFile(attachedFileType);
//                        researchPlanAttachments.setProgressReport(progressReport);
//                     }
//                  }
//               }
//               if (narrativeType == 24) {//ResearchDesignMethods
//                  if (attachment == null) {
//                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
//
//                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
//                     if (narrativeAttachment.getContent() != null){
////                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
//
//                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
//                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.ResearchDesignMethodsType
//                            researchDesignMethods = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeResearchDesignMethodsType();
//                        researchDesignMethods.setAttFile(attachedFileType);
//                        researchPlanAttachments.setResearchDesignMethods(researchDesignMethods);
//                     }
//                  }
//               }
               if (narrativeType == 111) {//ResearchStrategy new type for v1.3 version
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);

                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.ResearchStrategyType
                            researchStrategy = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeResearchStrategyType();
                        researchStrategy.setAttFile(attachedFileType);
                        researchPlanAttachments.setResearchStrategy(researchStrategy);
                     }
                  }
               }
               if (narrativeType == 43) {//InclusionEnrollmentReport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.InclusionEnrollmentReportType
                            inclusionEnrollmentReport = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeInclusionEnrollmentReportType();
                        inclusionEnrollmentReport.setAttFile(attachedFileType);
                        researchPlanAttachments.setInclusionEnrollmentReport(inclusionEnrollmentReport);                              
                     }
                  }                  
               }
               if (narrativeType == 44) {//ProgressReportPublicationList
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.ProgressReportPublicationListType  progressReportPublicationList
                            = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeProgressReportPublicationListType();
                        progressReportPublicationList.setAttFile(attachedFileType);
                        researchPlanAttachments.setProgressReportPublicationList(progressReportPublicationList);                              
                     }
                  }                  
               }
               //start HumanSubjectSetion
               if (narrativeType == 25) {//ProtectionOfHumanSubjects
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.HumanSubjectSectionType.ProtectionOfHumanSubjectsType
                            protectionOfHumanSubjects = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeProtectionOfHumanSubjectsType();
                        protectionOfHumanSubjects.setAttFile(attachedFileType);
                        humanSubjectSection.setProtectionOfHumanSubjects(protectionOfHumanSubjects);                       
                     }
                  }                  
               }               
               if (narrativeType == 26) {//InclusionOfWomenAndMinorities
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.HumanSubjectSectionType.InclusionOfWomenAndMinoritiesType inclusionOfWomenAndMinorities =
                                objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeInclusionOfWomenAndMinoritiesType();
                        inclusionOfWomenAndMinorities.setAttFile(attachedFileType);
                        humanSubjectSection.setInclusionOfWomenAndMinorities(inclusionOfWomenAndMinorities);
                     }
                  }                  
               }
               if (narrativeType == 27) {//TargetedPlannedEnrollmentTable
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);

                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.HumanSubjectSectionType.TargetedPlannedEnrollmentTableType
                            targetedPlannedEnrollmentTable = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeTargetedPlannedEnrollmentTableType();
                        targetedPlannedEnrollmentTable.setAttFile(attachedFileType);
                        humanSubjectSection.setTargetedPlannedEnrollmentTable(targetedPlannedEnrollmentTable);                             
                     }
                  }                  
               }
               if (narrativeType == 28) {//InclusionOfChildren
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                        
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.HumanSubjectSectionType.InclusionOfChildrenType
                            inclusionOfChildren = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeHumanSubjectSectionTypeInclusionOfChildrenType();
                        inclusionOfChildren.setAttFile(attachedFileType);
                        humanSubjectSection.setInclusionOfChildren(inclusionOfChildren); 
                     }
                  }                  
               }               
               //end HumanSubjectSetion
               //start other research plan sections 
               if (narrativeType == 30) {//VertebrateAnimals
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);                       
                        
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.VertebrateAnimalsType
                            vertebrateAnimals = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeVertebrateAnimalsType();
                        vertebrateAnimals.setAttFile(attachedFileType);
                        otherResearchPlanSections.setVertebrateAnimals(vertebrateAnimals); 
                     }
                  }                  
               }              
               if (narrativeType == 45) {//SelectAgentResearch
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.SelectAgentResearchType
                            selectAgentResearch = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeSelectAgentResearchType();
                        selectAgentResearch.setAttFile(attachedFileType);
                        otherResearchPlanSections.setSelectAgentResearch(selectAgentResearch);
                     }
                  }                  
               }
               if (narrativeType == 46) {//MultiplePILeadershipPlan
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.MultiplePDPILeadershipPlanType
                            multiplePILeadershipPlan = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeMultiplePDPILeadershipPlanType();
                        multiplePILeadershipPlan.setAttFile(attachedFileType);
                        otherResearchPlanSections.setMultiplePDPILeadershipPlan(multiplePILeadershipPlan);
                     }
                  }                  
               }              
               if (narrativeType == 31) {//ConsortiumContractualArrangements
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.ConsortiumContractualArrangementsType
                            consortiumContractualArrangements = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeConsortiumContractualArrangementsType();
                        consortiumContractualArrangements.setAttFile(attachedFileType);
                        otherResearchPlanSections.setConsortiumContractualArrangements(consortiumContractualArrangements);
                     }
                  }                  
               }
               if (narrativeType == 32) {//LettersOfSupport
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                       
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.LettersOfSupportType
                            lettersOfSupport = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeLettersOfSupportType();
                        lettersOfSupport.setAttFile(attachedFileType);
                        otherResearchPlanSections.setLettersOfSupport(lettersOfSupport); 
                     }
                  }                  
               }
               if (narrativeType == 33) {//ResourceSharingPlans
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        
                        PHS398ResearchPlan13Type.ResearchPlanAttachmentsType.OtherResearchPlanSectionsType.ResourceSharingPlansType
                            resourceSharingPlans = objFactory.createPHS398ResearchPlan13TypeResearchPlanAttachmentsTypeOtherResearchPlanSectionsTypeResourceSharingPlansType();
                        resourceSharingPlans.setAttFile(attachedFileType);
                        otherResearchPlanSections.setResourceSharingPlans(resourceSharingPlans);                            
                     }
                  }                  
               }
               //end other research plan sections
               // for appendix 
               if (narrativeType == 34) {//Appendix
                  if (attachment == null) {
                    proposalNarrativePDFSourceBean = proposalNarrativeTxnBean.getNarrativePDF(proposalNarrativePDFSourceBean);
                     
                     Attachment narrativeAttachment = getAndAddNarrative(hmArg, proposalNarrativePDFSourceBean);
                     if (narrativeAttachment.getContent() != null){
//                        narrativeAttachment.setFileName(AttachedFileDataTypeStream.addExtension(fileNameForOtherType));
                     
                        attachedFileType = getAttachedFileType(narrativeAttachment,attachmentsObjFactory);
                        attachmentGroup.getAttachedFile().add(attachedFileType);
//                        researchPlanAttachments.getAppendix().getAttachedFile().add(attachedFileType);

                     }
                  }                  
               } 
               
           }  //end for
           if (humanSubjectSection != null)
               researchPlanAttachments.setHumanSubjectSection(humanSubjectSection);
           
           if (otherResearchPlanSections != null)
               researchPlanAttachments.setOtherResearchPlanSections(otherResearchPlanSections);             
           
           if (attachmentGroup != null)
               researchPlanAttachments.setAppendix(attachmentGroup);
           
            phs398ResearchPlan13Type.setResearchPlanAttachments(researchPlanAttachments);
            return phs398ResearchPlan13Type;
           
         }catch (JAXBException jaxbEx){
            utilFactory.log(jaxbEx.getMessage(),jaxbEx,"PHS398ResearchPlanV1_3Stream","getPHS398ResearchPlanV1_3()");
            throw new CoeusXMLException(jaxbEx.getMessage());
         }        
                    
 }    
    public Object getStream(HashMap ht) throws JAXBException, CoeusException, DBException{
        this.propNumber = (String)ht.get("PROPOSAL_NUMBER");
        return getPHS398ResearchPlanV1_3();
    }     
    
}


